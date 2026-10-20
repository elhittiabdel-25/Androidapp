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
    /** Scenes where dust would read wrong (rain, a storm at sea). */
    val noDust = setOf("ship")
    /** The camera of the frame being drawn (drawing happens on one thread, one frame at a time). */
    val cam = Camera()
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
    val paper = layout in Cinema.paper
    val p = easeInOut(progress.coerceIn(0f, 1f))
    val breathe = 0.5f + 0.5f * sin(t * 0.33f)
    val zoom = if (paper) 1f + 0.018f * p else 1.016f + 0.034f * p + 0.006f * breathe
    val dx = if (paper) 0f else w * 0.0032f * sin(t * 0.21f + 1.3f)
    val dy = if (paper) 0f else h * 0.0032f * cos(t * 0.17f)
    val c = Cinema.cam
    c.zoom = zoom; c.px = w * 0.5f; c.py = h * 0.46f; c.dx = dx; c.dy = dy
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

