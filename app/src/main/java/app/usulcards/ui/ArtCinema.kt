package app.usulcards.ui

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextLayoutResult
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * The cinema around every scene, in the spirit of the short Islamic animations people follow on social media:
 * a camera that slowly pushes in as the voice moves through the card, light that falls off towards the edges,
 * dust floating in the light, a fine film grain, and helpers for glows and shafts of light. All drawn in code.
 */

/** Where the camera is for the frame being drawn; labels read it to stay inside what the viewer sees. */
internal class Camera(var zoom: Float = 1f, var px: Float = 0f, var py: Float = 0f, var dx: Float = 0f, var dy: Float = 0f) {
    /** The part of the scene (in scene coordinates) that ends up on screen, as left, top, right, bottom. */
    fun visible(w: Float, h: Float): FloatArray = floatArrayOf(
        px + (0f - dx - px) / zoom, py + (0f - dy - py) / zoom,
        px + (w - dx - px) / zoom, py + (h - dy - py) / zoom,
    )
}

internal object Cinema {
    /** Layouts drawn on the notebook page: they follow the app theme, move less and get no dust. */
    val paper = setOf("chart", "circuit", "verdict", "board", "oath", "kin")
    /** Scenes whose drawing fills the frame edge to edge: the camera barely moves. */
    val tight = setOf("hourglass")
    /** Scenes in daylight or on a lit page: the edges darken less. */
    val day = setOf("ink", "zellige", "study", "canyon", "map", "books", "athar", "delta", "loom", "sabil", "ruler", "mosque", "maze", "field", "atelier", "scroll", "majlis", "lane", "wayside", "flock", "deck", "dam", "aerial", "souk", "sapling")
    /** Scenes where dust would read wrong (rain, a storm at sea). */
    val noDust = setOf("ship")
    /** The camera of the frame being drawn (drawing happens on one thread, one frame at a time). */
    val cam = Camera()
    /** The frame being drawn is in the dark theme: pen lines on the notebook glow a little. */
    var darkFrame = true
    /** His ﷺ name, set under his light wherever it is drawn, so that it is never taken for the sun or a star. */
    var prophetName: TextLayoutResult? = null
    private var grainBitmap: ImageBitmap? = null

    /** A small tile of film grain, made once from a fixed seed so every run looks the same. */
    fun grain(): ImageBitmap = grainBitmap ?: run {
        val n = 96
        val px = IntArray(n * n)
        var s = 0x2545F491L
        for (i in px.indices) {
            s = (s * 6364136223846793005L + 1442695040888963407L)
            val r = ((s ushr 33) and 0xFF).toInt()
            val light = r > 127
            val a = (kotlin.math.abs(r - 127) * 2 * 0.55f).toInt().coerceIn(0, 255)
            px[i] = (a shl 24) or (if (light) 0xFFFFFF else 0x000000)
        }
        Bitmap.createBitmap(px, n, n, Bitmap.Config.ARGB_8888).asImageBitmap().also { grainBitmap = it }
    }
}

/** Draws [block] through the camera: a slow push-in as the card advances ([progress] 0 to 1) and a gentle drift. */
internal fun DrawScope.throughCamera(layout: String, progress: Float, t: Float, block: DrawScope.() -> Unit) {
    val w = size.width
    val h = size.height
    val paper = layout in Cinema.paper || layout in Cinema.tight
    val p = easeInOut(progress.coerceIn(0f, 1f))
    val breathe = 0.5f + 0.5f * sin(t * 0.33f)
    val zoom = when {
        layout in Cinema.tight -> 1f + 0.008f * p
        paper -> 1f + 0.018f * p
        else -> 1.016f + 0.034f * p + 0.006f * breathe
    }
    val dx = if (paper) 0f else w * 0.0032f * sin(t * 0.21f + 1.3f)
    val dy = if (paper) 0f else h * 0.0032f * cos(t * 0.17f)
    val c = Cinema.cam
    c.zoom = zoom; c.px = w * 0.5f; c.py = if (paper) h * 0.5f else h * 0.46f; c.dx = dx; c.dy = dy
    withTransform({
        translate(dx, dy)
        scale(zoom, zoom, pivot = Offset(c.px, c.py))
    }, block)
    c.zoom = 1f; c.dx = 0f; c.dy = 0f
}

/** The air over the scene: light falling off towards the edges, dust drifting in the light, film grain. */
internal fun DrawScope.drawAir(layout: String, t: Float, dark: Boolean) {
    val w = size.width
    val h = size.height
    val paper = layout in Cinema.paper
    // light falls off towards the corners
    val edge = when {
        paper && !dark -> 0.10f
        paper -> 0.30f
        layout in Cinema.day -> 0.24f
        else -> 0.46f
    }
    drawRect(
        Brush.radialGradient(
            0.45f to Color.Transparent, 0.8f to ArtInk.Shade.copy(alpha = edge * 0.45f), 1f to ArtInk.Shade.copy(alpha = edge),
            center = Offset(w * 0.5f, h * 0.44f), radius = hypot(w, h) * 0.6f,
        ),
    )
    // dust in the light: slow, warm, a few motes catching the light as they turn
    if (!paper && layout !in Cinema.noDust) {
        val d = density
        for (i in 0 until 18) {
            val r1 = fract(sin(i * 12.9898f) * 43758.547f)
            val r2 = fract(sin(i * 78.233f) * 12345.679f)
            val r3 = fract(sin(i * 37.719f) * 24634.635f)
            val speed = 0.010f + 0.022f * r2
            val y = h * (1.05f - fract(r1 + t * speed) * 1.1f)
            val x = w * (0.04f + 0.92f * r3) + w * 0.012f * sin(t * (0.25f + 0.3f * r1) + i)
            val life = fract(r1 + t * speed)
            val fade = bump(life)
            val twinkle = 0.55f + 0.45f * sin(t * (1.1f + r2) + i * 2.1f)
            val a = (0.10f + 0.22f * r2) * fade * twinkle
            if (a < 0.01f) continue
            val rad = (0.7f + 1.3f * r3) * d
            drawCircle(ArtInk.Ivory.copy(alpha = a * 0.25f), rad * 3.2f, Offset(x, y))
            drawCircle(ArtInk.Ivory.copy(alpha = a), rad, Offset(x, y))
        }
    }
    // film grain: the tile jumps 12 times a second, like film
    val g = Cinema.grain()
    val step = floor(t * 12f)
    val ox = fract(step * 0.618034f) * g.width
    val oy = fract(step * 0.414214f) * g.height
    val grainAlpha = when {
        paper && !dark -> 0.035f
        else -> 0.055f
    }
    translate(-ox, -oy) {
        drawRect(
            ShaderBrush(ImageShader(g, TileMode.Repeated, TileMode.Repeated)),
            size = Size(w + g.width, h + g.height), alpha = grainAlpha,
        )
    }
}

/** A light that blooms: a bright core and wide, soft halos (a lamp, a star, an open door). */
internal fun DrawScope.bloom(c: Offset, r: Float, color: Color, strength: Float = 1f) {
    if (strength <= 0.01f) return
    val s = strength.coerceIn(0f, 1.5f)
    drawCircle(Brush.radialGradient(listOf(color.copy(alpha = 0.22f * s), Color.Transparent), c, r * 4.5f), r * 4.5f, c)
    drawCircle(Brush.radialGradient(listOf(color.copy(alpha = 0.45f * s), Color.Transparent), c, r * 2f), r * 2f, c)
    drawCircle(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.75f * min(1f, s)), color.copy(alpha = 0.6f * min(1f, s)), Color.Transparent), c, r), r, c)
}

/**
 * Shafts of light from [from] fanning around [angle] (radians, 0 = right, PI/2 = down) over [spread], [length]
 * long; they sway slowly with [t]. [strength] fades them in.
 */
internal fun DrawScope.lightShafts(from: Offset, angle: Float, spread: Float, length: Float, color: Color, strength: Float, t: Float, rays: Int = 7) {
    if (strength <= 0.01f) return
    for (k in 0 until rays) {
        val f = if (rays == 1) 0.5f else k / (rays - 1f)
        val a = angle - spread / 2 + spread * f + 0.03f * sin(t * 0.4f + k * 1.7f)
        val half = (0.018f + 0.02f * fract(sin(k * 7.13f) * 91.7f)) * (1f + 0.25f * sin(t * 0.6f + k))
        val far = length * (0.75f + 0.25f * fract(sin(k * 3.1f) * 47.3f))
        val p = Path().apply {
            moveTo(from.x, from.y)
            lineTo(from.x + cos(a - half) * far, from.y + sin(a - half) * far)
            lineTo(from.x + cos(a + half) * far, from.y + sin(a + half) * far)
            close()
        }
        val alpha = strength * (0.10f + 0.08f * sin(t * 0.7f + k * 2.3f).let { it * it })
        drawPath(p, Brush.radialGradient(listOf(color.copy(alpha = alpha), Color.Transparent), from, far))
    }
}

/** Mean progress of the beats that have started, for the camera. */
internal fun cameraProgress(beats: List<Float>): Float {
    if (beats.isEmpty()) return 0f
    var s = 0f
    for (b in beats) s += b
    return s / max(1, beats.size)
}


// ───────────── sky and depth, shared by the night scenes ─────────────

/** Faint far stars that give the sky depth (fixed positions, from a seed). */
internal val DEEP_STARS: List<Twinkle> = run {
    var seed = 97L
    fun rnd(): Float {
        seed = (seed * 16807L) % 2147483647L
        return seed / 2147483647f
    }
    List(150) { Twinkle(rnd(), rnd(), 0.35f + rnd() * 0.5f, rnd() * 6.3f) }
}

/** A four-pointed glint over a bright star or a polished edge. */
internal fun DrawScope.glint(c: Offset, r: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f || r <= 0f) return
    val a = alpha.coerceIn(0f, 1f)
    val th = 0.9f * density
    drawLine(Brush.horizontalGradient(listOf(Color.Transparent, color.copy(alpha = a), Color.Transparent), c.x - r, c.x + r), Offset(c.x - r, c.y), Offset(c.x + r, c.y), th)
    drawLine(Brush.verticalGradient(listOf(Color.Transparent, color.copy(alpha = a), Color.Transparent), c.y - r, c.y + r), Offset(c.x, c.y - r), Offset(c.x, c.y + r), th)
    drawCircle(Brush.radialGradient(listOf(color.copy(alpha = 0.5f * a), Color.Transparent), c, r * 0.45f), r * 0.45f, c)
}

/**
 * A deep night sky down to [horizon] (px): the gradient, a glow along the horizon, the band of the galaxy,
 * far stars, the twinkling stars and glints on the brightest. [shine] makes the stars brighter (an adornment).
 */
internal fun DrawScope.deepSky(t: Float, horizon: Float, top: Color, low: Color, glow: Color = Color(0xFF6E5A9A), starAlpha: Float = 1f, shine: Float = 0f, twinkles: Boolean = true) {
    val w = size.width
    val dp = density
    drawRect(Brush.verticalGradient(0f to top, 0.6f to lerpColor(top, low, 0.55f), 1f to low, endY = horizon), size = Size(w, horizon))
    // the glow along the horizon
    drawOval(
        Brush.radialGradient(listOf(glow.copy(alpha = 0.38f), Color.Transparent), Offset(w * 0.5f, horizon), w * 0.6f),
        topLeft = Offset(-w * 0.1f, horizon - w * 0.22f), size = Size(w * 1.2f, w * 0.44f),
    )
    // the galaxy: a soft band across the sky, dense with faint stars
    withTransform({ rotate(-17f, Offset(w * 0.5f, horizon * 0.42f)) }) {
        drawRect(
            Brush.verticalGradient(0f to Color.Transparent, 0.5f to ArtInk.Ivory.copy(alpha = 0.07f * starAlpha), 1f to Color.Transparent, startY = horizon * 0.30f, endY = horizon * 0.56f),
            topLeft = Offset(-w * 0.2f, horizon * 0.30f), size = Size(w * 1.4f, horizon * 0.26f),
        )
    }
    for (s in DEEP_STARS) {
        val y = s.y * horizon * 0.95f
        drawCircle(ArtInk.Ivory.copy(alpha = (0.12f + 0.2f * (0.5f + 0.5f * sin(t * 0.7f + s.p))) * starAlpha), s.r * dp, Offset(s.x * w, y))
    }
    if (twinkles) for (s in TWINKLES) {
        val y = s.y * horizon * 0.8f
        val tw = 0.5f + 0.5f * sin(t * (1.3f + 1.4f * shine) + s.p)
        val a = (0.35f + 0.5f * tw) * (0.75f + 0.25f * shine) * starAlpha
        val c = Offset(s.x * w, y)
        drawCircle(ArtInk.Ivory.copy(alpha = a.coerceIn(0f, 1f)), s.r * dp * (1f + 0.4f * shine), c)
        if (s.r > 1.3f) glint(c, s.r * dp * (4f + 3f * shine) * (0.6f + 0.4f * tw), ArtInk.Ivory, a * (0.5f + 0.5f * shine))
    }
}

/** A far range of hills in the haze behind the near ones: lighter, softer, slower. */
internal fun DrawScope.farHills(near: Color, sky: Color, base: Float, amp: Float, ph: Float) {
    hills(lerpColor(near, sky, 0.45f), base, amp, 0.008f, 0.021f, ph)
    drawRect(
        Brush.verticalGradient(listOf(sky.copy(alpha = 0.35f), Color.Transparent), startY = base - amp, endY = base + amp * 2f),
        topLeft = Offset(0f, base - amp), size = Size(size.width, amp * 3f),
    )
}

internal fun lerpColor(a: Color, b: Color, f: Float): Color = androidx.compose.ui.graphics.lerp(a, b, f.coerceIn(0f, 1f))

/** A soft light: a bright shoulder around the centre and a wide, faint halo (light that carries in the air). */
internal fun DrawScope.softGlow(c: Offset, r: Float, color: Color, alpha: Float) {
    if (alpha <= 0.005f || r <= 0f) return
    val a = alpha.coerceIn(0f, 1f)
    drawCircle(Brush.radialGradient(listOf(color.copy(alpha = a * 0.26f), Color.Transparent), c, r * 2.4f), r * 2.4f, c)
    drawCircle(Brush.radialGradient(0f to color.copy(alpha = a), 0.35f to color.copy(alpha = a * 0.45f), 1f to Color.Transparent, center = c, radius = r), r, c)
}

/**
 * «وَسِرَاجًا مُنِيرًا» (al-Ahzab 33:46): how the Prophet ﷺ is shown — a lamp spreading light, never a body or an
 * outline. A bright core, a wide warm halo, soft rays turning slowly and a few motes of light around it; it
 * lights what is near it. [r] is the core radius, [strength] fades it in. His name goes with it (under the light,
 * or at [nameAt]) so the light is never taken for the sun; [named] false only where his name is already written
 * beside it.
 */
internal fun DrawScope.sirajMunir(c: Offset, r: Float, strength: Float, t: Float, nameAt: Offset? = null, named: Boolean = true) {
    if (strength <= 0.01f) return
    val s = strength.coerceIn(0f, 1f)
    val warm = Color(0xFFFFE6B0)
    drawCircle(Brush.radialGradient(listOf(warm.copy(alpha = 0.16f * s), Color.Transparent), c, r * 14f), r * 14f, c)
    lightShafts(c, t * 0.08f, 2f * kotlin.math.PI.toFloat(), r * 11f, warm, 0.9f * s, t, 12)
    bloom(c, r * 1.6f, warm, s)
    for (k in 0 until 6) {
        val ang = t * (0.25f + 0.05f * k) + k * 1.047f
        val rr = r * (3.2f + 1.4f * sin(t * 0.6f + k))
        val p = c + Offset(cos(ang) * rr, sin(ang) * rr * 0.8f)
        drawCircle(warm.copy(alpha = 0.5f * s * (0.6f + 0.4f * sin(t * 1.7f + k))), r * 0.22f, p)
    }
    if (named) tagAt(Cinema.prophetName, nameAt ?: (c + Offset(0f, r * 7.5f)), s, back = Color(0xE6101426), ink = ArtInk.Gold)
}
