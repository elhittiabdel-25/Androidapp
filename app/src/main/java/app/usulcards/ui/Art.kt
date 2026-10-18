package app.usulcards.ui

import android.content.Context
import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import app.usulcards.data.Scene
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/*
 * Illustrated scenes ("art"): each card acts out its idea with objects and places — a key and its teeth, a
 * rope over a chasm, a ship in a storm — instead of animating its text. Everything is drawn in code (no image
 * files), so the app does not grow. Each scene item is a beat that plays when the voice reaches it; its `kind`
 * names its role in the scene, its `sub` is the short tag drawn in the picture ("a|b" for two tags), and its
 * `label` is the caption under the picture. Rules: no people or animals, no depiction of Allah, the angels or
 * the Throne.
 */

internal const val ART_ASPECT = 16f / 11f

/** Colours shared by every scene: ivory and gold light, brass, and the two signal tones. */
internal object ArtInk {
    val Ivory = Color(0xFFFBF3D5)
    val Gold = Color(0xFFF2C45A)
    val Brass = Color(0xFFD29C45)
    val BrassLight = Color(0xFFF4D68A)
    val BrassDark = Color(0xFF7E5520)
    val Ember = Color(0xFFE8794A)
    val Jade = Color(0xFF86D6AE)
    val Shade = Color(0xFF070B1A)
}

// ───────────── live scene and pure frame ─────────────

/** Live: each beat plays when its item is shown (when the voice reaches it); wind, water and light keep moving. */
@Composable
internal fun ArtScene(scene: Scene, shown: Int, showDiacritics: Boolean) {
    val context = LocalContext.current
    val still = remember { animationsOff(context) }
    val anims = remember(scene) { List(scene.items.size) { Animatable(0f) } }
    scene.items.forEachIndexed { i, item ->
        val on = i < shown
        key(i) {
            LaunchedEffect(scene, on) {
                when {
                    !on -> anims[i].snapTo(0f)
                    still -> anims[i].snapTo(1f)
                    else -> anims[i].animateTo(1f, tween(beatMillis(item.kind), easing = LinearEasing))
                }
            }
        }
    }
    val clock = rememberArtClock(still)
    ArtFrame(scene, { anims.map { it.value } }, { clock.value }, shown, showDiacritics)
}

/** One frame: [beats] gives each item's progress (0 to 1), [time] the seconds of ambient motion. */
@Composable
internal fun ArtFrame(scene: Scene, beats: () -> List<Float>, time: () -> Float, shown: Int, showDiacritics: Boolean) {
    val pal = LocalPalette.current
    val txt: (String) -> String = { s -> if (showDiacritics) s else stripHarakat(s) }
    val tm = rememberTextMeasurer()
    val tags = remember(scene, showDiacritics) {
        val style = TextStyle(fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = ArtInk.Ivory)
        scene.items.map { item ->
            if (item.sub.isEmpty()) emptyList<TextLayoutResult>() else item.sub.split("|").map { tm.measure(txt(it.trim()), style) }
        }
    }
    val extras = remember(scene, showDiacritics) {
        val style = TextStyle(fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = ArtInk.Gold)
        scene.sides.map { tm.measure(txt(it.first), style) }
    }
    Column(Modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().aspectRatio(ART_ASPECT)) {
            val a = ArtCtx(scene.items.map { it.kind }, beats(), time(), tags, extras, scene.items.map { it.side }, scene.items.map { it.value })
            when (scene.layout) {
                "key" -> keyScene(a)
                "rope" -> ropeScene(a)
                "ship" -> shipScene(a)
                "lantern" -> lanternScene(a)
                "trail" -> trailScene(a)
                "ridge" -> ridgeScene(a)
                "tree" -> treeScene(a)
                "streams" -> streamsScene(a)
                "building" -> buildingScene(a)
                "gate" -> gateScene(a)
                "shrine" -> shrineScene(a)
                "hourglass" -> hourglassScene(a)
                "night" -> nightScene(a)
                "grain" -> grainScene(a)
                else -> drawRect(pal.line)
            }
        }
        Box(Modifier.fillMaxWidth().background(pal.bg)) { SceneCaption(scene, shown, txt) }
    }
}

private fun animationsOff(context: Context): Boolean = try {
    Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
} catch (e: Exception) {
    false
}

/** How long each kind of beat takes to play out. */
internal fun beatMillis(kind: String): Int = when (kind) {
    "open" -> 2600
    "cut" -> 1700
    "hold" -> 1500
    "storm", "storm2" -> 2200
    "later", "trumpet", "scrolls", "takbir" -> 1800
    "house", "pillars", "excess", "forget", "stone", "steps", "show", "shut", "fall" -> 1600
    "rise", "dome", "feast", "door", "gold", "bridge" -> 2200
    "pass", "idol" -> 2400
    "rival", "light", "lock", "filter", "scale", "copy", "near", "out", "tree", "names", "shirk", "bind", "lower", "gaze", "hidden", "swap" -> 1400
    "charm", "wall", "many", "raise", "level", "cloud", "roll", "shrink", "quake", "truth", "nocall", "adorn", "shoot", "guide", "credit", "rain", "challenge", "breath" -> 1600
    "nights", "grain" -> 3200
    "untie", "return" -> 2200
    else -> 1000
}

/** Seconds since the scene appeared (stays at 0 when the system's animations are off). */
@Composable
private fun rememberArtClock(still: Boolean): State<Float> {
    val t = remember { mutableFloatStateOf(0f) }
    if (!still) {
        LaunchedEffect(Unit) {
            var last = withFrameNanos { it }
            while (true) {
                withFrameNanos { now ->
                    t.floatValue += ((now - last) / 1_000_000_000f).coerceAtMost(0.05f)
                    last = now
                }
            }
        }
    }
    return t
}

/** What a scene's drawing reads: beat progress and tags by kind (the n-th item of that kind), and the clock. */
internal class ArtCtx(
    private val kinds: List<String>,
    private val beats: List<Float>,
    val t: Float,
    private val tags: List<List<TextLayoutResult>>,
    val extras: List<TextLayoutResult>,
    private val sides: List<Int> = emptyList(),
    private val values: List<Float> = emptyList(),
) {
    /** Items by position, for scenes whose beats act on a target (`side`) with a strength (`value`). */
    val size: Int get() = kinds.size
    fun kindAt(i: Int): String = kinds[i]
    fun beatAt(i: Int): Float = beats.getOrElse(i) { 0f }
    fun sideAt(i: Int): Int = sides.getOrElse(i) { -1 }
    fun valueAt(i: Int): Float = values.getOrElse(i) { 0f }
    fun tagOf(i: Int, part: Int = 0): TextLayoutResult? = tags.getOrNull(i)?.getOrNull(part)
    fun parts(i: Int): Int = tags.getOrNull(i)?.size ?: 0
    /** The n-th item of [kind], or -1. */
    fun nth(kind: String, n: Int = 0): Int = index(kind, n)

    private fun index(kind: String, n: Int): Int {
        var c = 0
        for (i in kinds.indices) {
            if (kinds[i] == kind) {
                if (c == n) return i
                c++
            }
        }
        return -1
    }

    fun b(kind: String, n: Int = 0): Float = index(kind, n).let { if (it < 0) 0f else beats.getOrElse(it) { 0f } }
    fun tag(kind: String, n: Int = 0, part: Int = 0): TextLayoutResult? =
        index(kind, n).let { if (it < 0) null else tags.getOrNull(it)?.getOrNull(part) }
    fun count(kind: String): Int = kinds.count { it == kind }
}

// ───────────── shared helpers ─────────────

internal fun seg(p: Float, a: Float, b: Float): Float = ((p - a) / (b - a)).coerceIn(0f, 1f)
internal fun mix(a: Float, b: Float, f: Float): Float = a + (b - a) * f
internal fun easeOut(x: Float): Float = 1f - (1f - x.coerceIn(0f, 1f)).pow(3)
internal fun easeInOut(x: Float): Float {
    val c = x.coerceIn(0f, 1f)
    return if (c < 0.5f) 4f * c * c * c else 1f - (-2f * c + 2f).pow(3) / 2f
}
internal fun bump(x: Float): Float = if (x <= 0f || x >= 1f) 0f else sin(PI.toFloat() * x)
internal fun fract(x: Float): Float = x - floor(x)

/** A short tag on a rounded backing, centred at [c] and kept inside the picture. */
internal fun DrawScope.tagAt(tl: TextLayoutResult?, c: Offset, alpha: Float, back: Color = Color(0xB3101426), ink: Color = Color.Unspecified) {
    if (tl == null || alpha <= 0.01f) return
    val padX = 6f * density
    val padY = 2.5f * density
    val w = tl.size.width + 2 * padX
    val h = tl.size.height + 2 * padY
    val x = (c.x - w / 2).coerceIn(2f * density, max(2f * density, size.width - w - 2f * density))
    val y = (c.y - h / 2).coerceIn(2f * density, max(2f * density, size.height - h - 2f * density))
    val a = alpha.coerceIn(0f, 1f)
    drawRoundRect(back, topLeft = Offset(x, y), size = Size(w, h), cornerRadius = CornerRadius(h / 2, h / 2), alpha = a)
    drawText(tl, color = ink, topLeft = Offset(x + padX, y + padY), alpha = a)
}

/** Eight-pointed star (two squares), the app's emblem and the unit of zellige patterns. */
internal fun starPath(c: Offset, r: Float): Path = Path().apply {
    val inner = r * 0.76f
    for (k in 0 until 16) {
        val ang = (k * 22.5f - 90f) * PI.toFloat() / 180f
        val rr = if (k % 2 == 0) r else inner
        val x = c.x + rr * cos(ang)
        val y = c.y + rr * sin(ang)
        if (k == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

/** Horseshoe arch opening: jambs [j] from the centre, arch circle [r] centred at ([cx], [cy]), down to [floorY]. */
internal fun horseshoe(cx: Float, cy: Float, r: Float, j: Float, floorY: Float): Path {
    val alpha = Math.toDegrees(acos((j / r).toDouble().coerceIn(-1.0, 1.0))).toFloat()
    val dy = sqrt(max(0f, r * r - j * j))
    return Path().apply {
        moveTo(cx - j, floorY)
        lineTo(cx - j, cy + dy)
        arcTo(Rect(cx - r, cy - r, cx + r, cy + r), 180f - alpha, 180f + 2 * alpha, false)
        lineTo(cx + j, floorY)
        close()
    }
}

/** Point on a cubic curve. */
internal fun cubic(p0: Offset, p1: Offset, p2: Offset, p3: Offset, t: Float): Offset {
    val u = 1f - t
    val a = u * u * u
    val b = 3f * u * u * t
    val c = 3f * u * t * t
    val d = t * t * t
    return Offset(a * p0.x + b * p1.x + c * p2.x + d * p3.x, a * p0.y + b * p1.y + c * p2.y + d * p3.y)
}

/** The part of a cubic curve between [from] and [to] (0..1), as a polyline path, shifted by [shift]. */
internal fun cubicPart(p0: Offset, p1: Offset, p2: Offset, p3: Offset, from: Float, to: Float, shift: Offset = Offset.Zero): Path {
    val path = Path()
    val steps = 28
    for (k in 0..steps) {
        val q = cubic(p0, p1, p2, p3, mix(from, to, k / steps.toFloat())) + shift
        if (k == 0) path.moveTo(q.x, q.y) else path.lineTo(q.x, q.y)
    }
    return path
}

/** A Moroccan brass lantern hanging from its ring at [ring]; [s] is its size unit, [glow] its light (0..1). */
internal fun DrawScope.lantern(ring: Offset, s: Float, glow: Float, flicker: Float) {
    val x = ring.x
    val y0 = ring.y
    val light = (glow * (1f + flicker)).coerceIn(0f, 1.2f)
    // halo
    val bodyC = Offset(x, y0 + 2.9f * s)
    if (light > 0.01f) {
        val r = s * (4.5f + 3.5f * light)
        drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.42f * min(1f, light)), Color.Transparent), center = bodyC, radius = r), radius = r, center = bodyC)
    }
    // ring and dome cap
    drawCircle(ArtInk.Brass, radius = 0.38f * s, center = Offset(x, y0), style = Stroke(0.16f * s))
    val cap = Path().apply {
        moveTo(x - 0.9f * s, y0 + 1.45f * s)
        cubicTo(x - 0.9f * s, y0 + 0.75f * s, x - 0.25f * s, y0 + 0.45f * s, x, y0 + 0.4f * s)
        cubicTo(x + 0.25f * s, y0 + 0.45f * s, x + 0.9f * s, y0 + 0.75f * s, x + 0.9f * s, y0 + 1.45f * s)
        close()
    }
    drawPath(cap, Brush.verticalGradient(listOf(ArtInk.BrassLight, ArtInk.BrassDark), startY = y0 + 0.4f * s, endY = y0 + 1.45f * s))
    // glass body: tapered top and bottom
    val body = Path().apply {
        moveTo(x - 0.75f * s, y0 + 1.45f * s)
        lineTo(x + 0.75f * s, y0 + 1.45f * s)
        lineTo(x + 1.05f * s, y0 + 2.6f * s)
        lineTo(x + 0.8f * s, y0 + 4.2f * s)
        lineTo(x - 0.8f * s, y0 + 4.2f * s)
        lineTo(x - 1.05f * s, y0 + 2.6f * s)
        close()
    }
    val glass = lerp(Color(0xFF3A2A18), ArtInk.Gold, (0.25f + 0.75f * min(1f, light)))
    drawPath(body, Brush.radialGradient(listOf(lerp(glass, ArtInk.Ivory, 0.5f * min(1f, light)), glass, lerp(glass, Color(0xFF2A1A0C), 0.5f)), center = bodyC, radius = 1.6f * s))
    // brass frame over the glass
    drawPath(body, ArtInk.BrassDark, style = Stroke(0.14f * s, join = StrokeJoin.Round))
    drawLine(ArtInk.BrassDark, Offset(x, y0 + 1.45f * s), Offset(x, y0 + 4.2f * s), 0.11f * s)
    drawLine(ArtInk.BrassDark, Offset(x - 1.05f * s, y0 + 2.6f * s), Offset(x + 1.05f * s, y0 + 2.6f * s), 0.11f * s)
    // flame
    if (light > 0.05f) {
        val fh = s * (0.55f + 0.35f * light)
        val f = Path().apply {
            moveTo(x, y0 + 3.3f * s - fh)
            cubicTo(x + 0.28f * s, y0 + 3.1f * s - fh * 0.4f, x + 0.25f * s, y0 + 3.35f * s, x, y0 + 3.45f * s)
            cubicTo(x - 0.25f * s, y0 + 3.35f * s, x - 0.28f * s, y0 + 3.1f * s - fh * 0.4f, x, y0 + 3.3f * s - fh)
            close()
        }
        drawPath(f, ArtInk.Ivory.copy(alpha = min(1f, 0.4f + light)))
    }
    // base and finial
    drawRoundRect(ArtInk.Brass, topLeft = Offset(x - 0.85f * s, y0 + 4.2f * s), size = Size(1.7f * s, 0.32f * s), cornerRadius = CornerRadius(0.12f * s, 0.12f * s))
    drawPath(Path().apply {
        moveTo(x - 0.45f * s, y0 + 4.52f * s)
        lineTo(x + 0.45f * s, y0 + 4.52f * s)
        lineTo(x, y0 + 5.3f * s)
        close()
    }, ArtInk.BrassDark)
}

// ───────────── key: «لا إله إلا الله» and its seven teeth ─────────────

private object KeyInk {
    val WallTop = Color(0xFF17463F)
    val WallBottom = Color(0xFF0A2421)
    val Stone = Color(0xFF2A6B61)
    val StoneEdge = Color(0xFF4A9585)
    val Wood = Color(0xFF5C3820)
    val WoodLight = Color(0xFF74482A)
    val WoodDark = Color(0xFF3A2212)
    val Iron = Color(0xFF24170D)
    val Band = Color(0xFF0E322E)
    val Floor = Color(0xFF071A18)
    val Beyond = Color(0xFFFFF4D2)
}

private val TOOTH_H = floatArrayOf(0.66f, 1f, 0.78f, 0.92f, 0.56f, 0.96f, 0.72f)

/**
 * The key of «لا إله إلا الله» gains one tooth per condition; with all seven it turns in the lock and the
 * door opens on light (Wahb ibn Munabbih: every key has teeth). Beats: key, tooth ×7, open.
 */
private fun DrawScope.keyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val keyIn = easeOut(a.b("key"))
    val open = a.b("open")
    val toLock = easeInOut(seg(open, 0f, 0.3f))
    val turn = easeInOut(seg(open, 0.3f, 0.42f))
    val swing = easeInOut(seg(open, 0.42f, 0.82f))
    val light = easeOut(seg(open, 0.46f, 1f))

    // wall, with a soft vignette
    drawRect(Brush.verticalGradient(listOf(KeyInk.WallTop, KeyInk.WallBottom)))
    drawRect(Brush.radialGradient(listOf(Color.Transparent, Color(0x73000000)), center = Offset(w * 0.45f, h * 0.42f), radius = w * 0.8f))

    // the door, in a horseshoe arch on the left
    val cx = w * 0.165f
    val cy = h * 0.36f
    val r = w * 0.118f
    val j = r * 0.84f
    val floorY = h * 0.85f
    val opening = horseshoe(cx, cy, r, j, floorY)
    val frame = horseshoe(cx, cy, r * 1.22f, j * 1.22f, floorY)
    if (light > 0f) {
        val g = r * (2.2f + 1.2f * light)
        drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.45f * light), Color.Transparent), center = Offset(cx, cy + r * 0.6f), radius = g), radius = g, center = Offset(cx, cy + r * 0.6f))
    }
    drawPath(frame, KeyInk.Stone)
    drawPath(frame, KeyInk.StoneEdge.copy(alpha = 0.7f), style = Stroke(1.2f * dp))
    drawPath(opening, KeyInk.WallBottom, style = Stroke(2.2f * dp))
    val keyhole = Offset(cx, floorY - (floorY - cy) * 0.42f)
    clipPath(opening) {
        if (light > 0f) {
            drawRect(Brush.verticalGradient(listOf(KeyInk.Beyond, ArtInk.Gold), startY = cy - r, endY = floorY), alpha = 0.35f + 0.65f * light)
        }
        withTransform({ scale(1f - 0.86f * swing, 1f, pivot = Offset(cx - j, floorY)) }) {
            doorLeaf(cx, cy, r, j, floorY, keyhole)
            if (swing > 0f) drawPath(opening, Color.Black.copy(alpha = 0.5f * swing))
        }
    }

    // floor with a zellige band
    drawRect(KeyInk.Floor, topLeft = Offset(0f, floorY), size = Size(w, h - floorY))
    val bandH = h * 0.055f
    drawRect(KeyInk.Band, topLeft = Offset(0f, floorY), size = Size(w, bandH))
    var sx = w * 0.035f
    var k = 0
    while (sx < w) {
        drawPath(starPath(Offset(sx, floorY + bandH / 2), bandH * 0.34f), if (k % 2 == 0) ArtInk.Brass.copy(alpha = 0.6f) else ArtInk.Ivory.copy(alpha = 0.22f))
        sx += w * 0.058f
        k++
    }
    if (light > 0f) {
        val pool = Path().apply {
            moveTo(cx - j, floorY)
            lineTo(cx + j, floorY)
            lineTo(cx + j * 2.8f, h)
            lineTo(cx - j * 2.2f, h)
            close()
        }
        drawPath(pool, Brush.verticalGradient(listOf(ArtInk.Gold.copy(alpha = 0.45f * light), Color.Transparent), startY = floorY, endY = h))
        // dust in the light
        for (m in 0 until 14) {
            val px = cx + (fract(m * 0.618f) - 0.5f) * j * 3.2f
            val py = cy + fract(m * 0.381f + t * 0.03f) * (floorY - cy) * 1.1f
            val tw = 0.5f + 0.5f * sin(t * 1.3f + m)
            drawCircle(ArtInk.Ivory.copy(alpha = 0.55f * light * tw), radius = (0.8f + fract(m * 0.27f)) * dp, center = Offset(px + sin(t * 0.7f + m) * 3f * dp, py))
        }
    }

    // the key: bow on the right, tip on the left pointing at the door
    val ky = h * 0.27f
    val tip = Offset(w * 0.31f, ky)
    val ringC = Offset(w * 0.915f, ky)
    val ringR = w * 0.058f
    val th = max(h * 0.042f, 6f * dp)
    val maxTooth = h * 0.1f
    val toothX = FloatArray(7) { i -> w * 0.84f - i * (w * 0.48f / 6f) }   // first condition next to the bow
    val teeth = FloatArray(7) { i -> a.b("tooth", i) }
    val keyAlpha = keyIn * (1f - seg(open, 0.42f, 0.52f))
    val bob = if (open > 0f) 0f else sin(t * 1.2f) * 1.5f * dp
    val pos = Offset(mix(tip.x, keyhole.x, toLock), mix(tip.y, keyhole.y, toLock))
    val sc = mix(1f, 0.3f, toLock)
    withTransform({
        translate(pos.x - tip.x, pos.y - tip.y + bob + (1f - keyIn) * 10f * dp)
        rotate(-90f * turn, pivot = tip)
        scale(sc, sc, pivot = tip)
    }) {
        drawKey(tip.x, ky, ringC, ringR, th, toothX, maxTooth, teeth, keyAlpha)
    }
    if (turn > 0f && turn < 1f || seg(open, 0.42f, 0.5f) in 0.01f..0.99f) {
        val f = bump(seg(open, 0.36f, 0.5f))
        drawCircle(Brush.radialGradient(listOf(ArtInk.Ivory.copy(alpha = 0.9f * f), Color.Transparent), center = keyhole, radius = 14f * dp), radius = 14f * dp, center = keyhole)
    }

    // tags: the word over the shaft, one tag per tooth in two staggered rows (lines first, tags over them)
    val fade = 1f - seg(open, 0f, 0.18f)
    tagAt(a.tag("key"), Offset(w * 0.6f, ky - h * 0.12f), keyIn * fade, back = Color(0xCC2A1C08), ink = ArtInk.BrassLight)
    val rowY = { i: Int -> ky + th / 2 + maxTooth + h * (0.08f + 0.09f * (i % 2)) }
    for (i in 0 until 7) {
        val p = seg(teeth[i], 0.3f, 0.9f) * fade * keyIn
        if (p <= 0f) continue
        val bottom = ky + th / 2 + maxTooth * TOOTH_H[i]
        drawLine(ArtInk.Ivory.copy(alpha = 0.35f * p), Offset(toothX[i], bottom + 2f * dp), Offset(toothX[i], rowY(i)), 1f * dp)
    }
    for (i in 0 until 7) {
        val p = seg(teeth[i], 0.3f, 0.9f) * fade * keyIn
        if (p > 0f) tagAt(a.tag("tooth", i), Offset(toothX[i], rowY(i)), p, back = Color(0xE6101426))
    }
}

private fun DrawScope.doorLeaf(cx: Float, cy: Float, r: Float, j: Float, floorY: Float, keyhole: Offset) {
    val dp = density
    val top = cy - r
    drawRect(
        Brush.horizontalGradient(listOf(KeyInk.Wood, KeyInk.WoodLight, KeyInk.Wood), startX = cx - j, endX = cx + j),
        topLeft = Offset(cx - r, top), size = Size(2 * r, floorY - top),
    )
    var x = cx - j + j * 0.42f
    while (x < cx + j) {
        drawLine(KeyInk.WoodDark, Offset(x, top), Offset(x, floorY), 1.1f * dp)
        x += j * 0.42f
    }
    for (fy in floatArrayOf(0.3f, 0.78f)) {
        val y = top + (floorY - top) * fy
        drawRect(KeyInk.Iron.copy(alpha = 0.85f), topLeft = Offset(cx - r, y - 1.6f * dp), size = Size(2 * r, 3.2f * dp))
        var sx = cx - j + j * 0.2f
        while (sx < cx + j) {
            drawCircle(ArtInk.Brass, radius = 1.5f * dp, center = Offset(sx, y))
            sx += j * 0.4f
        }
    }
    // brass plate and keyhole
    drawCircle(ArtInk.Brass, radius = 5.5f * dp, center = keyhole)
    drawCircle(ArtInk.BrassDark, radius = 5.5f * dp, center = keyhole, style = Stroke(1f * dp))
    drawCircle(KeyInk.Iron, radius = 1.7f * dp, center = keyhole - Offset(0f, 1f * dp))
    drawPath(Path().apply {
        moveTo(keyhole.x - 1f * dp, keyhole.y)
        lineTo(keyhole.x + 1f * dp, keyhole.y)
        lineTo(keyhole.x + 1.6f * dp, keyhole.y + 3.2f * dp)
        lineTo(keyhole.x - 1.6f * dp, keyhole.y + 3.2f * dp)
        close()
    }, KeyInk.Iron)
}

private fun DrawScope.drawKey(
    x0: Float, ky: Float, ringC: Offset, ringR: Float, th: Float,
    toothX: FloatArray, maxTooth: Float, teeth: FloatArray, alpha: Float,
) {
    if (alpha <= 0.01f) return
    val dp = density
    val shaftEnd = ringC.x - ringR * 0.9f
    val tw = th * 1.25f
    // teeth hang under the shaft, cut one by one
    for (i in toothX.indices) {
        val p = easeOut(seg(teeth[i], 0f, 0.6f))
        if (p <= 0f) continue
        val top = ky + th / 2 - 1f
        val bh = max(1f, maxTooth * TOOTH_H[i] * p)
        val bevel = min(tw * 0.25f, bh * 0.5f)
        val tooth = Path().apply {
            moveTo(toothX[i] - tw / 2, top)
            lineTo(toothX[i] + tw / 2, top)
            lineTo(toothX[i] + tw / 2, top + bh - bevel)
            lineTo(toothX[i] + tw / 4, top + bh)
            lineTo(toothX[i] - tw / 4, top + bh)
            lineTo(toothX[i] - tw / 2, top + bh - bevel)
            close()
        }
        drawPath(tooth, Brush.verticalGradient(listOf(ArtInk.Brass, ArtInk.BrassDark), startY = top, endY = top + maxTooth), alpha = alpha)
        val glint = 1f - seg(teeth[i], 0.45f, 1f)
        if (glint > 0f && teeth[i] > 0.15f) {
            val c = Offset(toothX[i], top + bh)
            drawCircle(Brush.radialGradient(listOf(ArtInk.Ivory.copy(alpha = 0.9f * glint * alpha), Color.Transparent), center = c, radius = tw * 1.6f), radius = tw * 1.6f, center = c)
        }
    }
    // shaft, collar and bit end
    val brass = Brush.verticalGradient(listOf(ArtInk.BrassLight, ArtInk.Brass, ArtInk.BrassDark), startY = ky - th / 2, endY = ky + th / 2)
    drawRoundRect(brass, topLeft = Offset(x0, ky - th / 2), size = Size(shaftEnd - x0, th), cornerRadius = CornerRadius(th / 2, th / 2), alpha = alpha)
    val collar = Brush.verticalGradient(listOf(ArtInk.BrassLight, ArtInk.BrassDark), startY = ky - th, endY = ky + th)
    drawRoundRect(collar, topLeft = Offset(shaftEnd - th * 1.4f, ky - th * 0.9f), size = Size(th * 0.8f, th * 1.8f), cornerRadius = CornerRadius(th * 0.3f, th * 0.3f), alpha = alpha)
    drawRoundRect(collar, topLeft = Offset(x0 - th * 0.2f, ky - th * 0.75f), size = Size(th * 0.7f, th * 1.5f), cornerRadius = CornerRadius(th * 0.25f, th * 0.25f), alpha = alpha)
    // bow: a ring holding an eight-pointed star
    drawCircle(
        Brush.radialGradient(listOf(ArtInk.BrassLight, ArtInk.Brass, ArtInk.BrassDark), center = ringC - Offset(ringR * 0.3f, ringR * 0.3f), radius = ringR * 1.4f),
        radius = ringR, center = ringC, style = Stroke(ringR * 0.3f), alpha = alpha,
    )
    drawPath(starPath(ringC, ringR * 0.52f), ArtInk.Brass, alpha = alpha)
    drawPath(starPath(ringC, ringR * 0.52f), ArtInk.BrassDark, alpha = alpha, style = Stroke(0.8f * dp))
    drawCircle(KeyInk.WallBottom, radius = ringR * 0.13f, center = ringC, alpha = alpha)
    drawLine(ArtInk.Ivory.copy(alpha = 0.55f * alpha), Offset(x0 + th, ky - th * 0.22f), Offset(shaftEnd - th * 1.8f, ky - th * 0.22f), max(1f, th * 0.12f), StrokeCap.Round)
}

// ───────────── rope: the firmest handhold ─────────────

private object DuskInk {
    val Top = Color(0xFF171A3D)
    val Mid = Color(0xFF5B3A6B)
    val Low = Color(0xFFD88560)
    val Horizon = Color(0xFFF2B97B)
    val Sun = Color(0xFFFFE2AC)
    val FarHills = Color(0xFF3A2A57)
    val NearHills = Color(0xFF261C43)
    val Cliff = Color(0xFF120D24)
    val Rim = Color(0xFFE08A5F)
    val Deep = Color(0xFF05030D)
    val Rope = Color(0xFF8E6C45)
    val RopeDark = Color(0xFF4F3820)
}

/**
 * Taghut: whatever a servant exceeds his bounds with — worshipped, followed, obeyed — hangs on frayed ropes
 * over a chasm. Disbelief in taghut cuts them; belief in Allah is the firmest handhold, which never breaks
 * (al-Baqarah 256). Beats: rope ×3, cut, hold, meaning («لا إله» | «إلا الله»).
 */
private fun DrawScope.ropeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cut = a.b("cut")
    val hold = easeInOut(a.b("hold"))
    val meaning = easeOut(a.b("meaning"))

    // dusk sky, sun, hills
    drawRect(Brush.verticalGradient(0f to DuskInk.Top, 0.5f to DuskInk.Mid, 0.8f to DuskInk.Low, 1f to DuskInk.Horizon))
    val sun = Offset(w * 0.18f, h * 0.6f)
    drawCircle(Brush.radialGradient(listOf(DuskInk.Sun.copy(alpha = 0.6f), Color.Transparent), center = sun, radius = w * 0.26f), radius = w * 0.26f, center = sun)
    drawCircle(DuskInk.Sun, radius = w * 0.045f, center = sun)
    hills(DuskInk.FarHills, h * 0.6f, h * 0.035f, 0.011f, 0.023f, 1.7f)
    hills(DuskInk.NearHills, h * 0.67f, h * 0.03f, 0.017f, 0.041f, 0.4f)

    // light from above for the firm rope
    val anchorG = Offset(w * 0.6f, -h * 0.02f)
    if (hold > 0f) {
        drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.55f * hold), Color.Transparent), center = anchorG, radius = w * 0.24f), radius = w * 0.24f, center = anchorG)
    }

    // the chasm between two cliffs
    val left = Path().apply {
        moveTo(0f, h * 0.7f); lineTo(w * 0.2f, h * 0.69f); lineTo(w * 0.36f, h * 0.73f); lineTo(w * 0.4f, h * 0.86f); lineTo(w * 0.37f, h); lineTo(0f, h); close()
    }
    val right = Path().apply {
        moveTo(w, h * 0.67f); lineTo(w * 0.8f, h * 0.69f); lineTo(w * 0.64f, h * 0.74f); lineTo(w * 0.6f, h * 0.88f); lineTo(w * 0.63f, h); lineTo(w, h); close()
    }
    drawRect(Brush.verticalGradient(listOf(Color.Transparent, DuskInk.Deep), startY = h * 0.7f, endY = h * 0.92f), topLeft = Offset(w * 0.3f, h * 0.7f), size = Size(w * 0.4f, h * 0.3f))
    drawPath(left, DuskInk.Cliff)
    drawPath(right, DuskInk.Cliff)
    drawLine(DuskInk.Rim.copy(alpha = 0.5f), Offset(0f, h * 0.7f), Offset(w * 0.2f, h * 0.69f), 1.3f * dp)
    drawLine(DuskInk.Rim.copy(alpha = 0.35f), Offset(w * 0.2f, h * 0.69f), Offset(w * 0.36f, h * 0.73f), 1.3f * dp)
    drawLine(DuskInk.Rim.copy(alpha = 0.35f), Offset(w, h * 0.67f), Offset(w * 0.8f, h * 0.69f), 1.3f * dp)
    drawLine(DuskInk.Rim.copy(alpha = 0.2f), Offset(w * 0.8f, h * 0.69f), Offset(w * 0.64f, h * 0.74f), 1.3f * dp)

    // the lantern: unsteady on frayed ropes, steady once it holds the firm one
    val unsteady = 1f - hold
    val sway = (sin(t * 1.7f) * 0.012f + sin(t * 3.1f + 1f) * 0.006f) * w * unsteady + sin(t * 1.1f) * 0.004f * w * hold
    val dip = 0.03f * h * bump(seg(cut, 0.2f, 0.6f))
    val ring = Offset(w * 0.5f + sway, h * 0.4f + dip)
    val s = w * 0.03f

    // the firm rope and its loop: faint at first, gold once held
    val g1 = Offset(anchorG.x, h * 0.18f)
    val g2 = Offset(ring.x + (anchorG.x - ring.x) * 0.3f, ring.y - h * 0.12f)
    val firm = cubicPart(anchorG, g1, g2, ring, 0f, 1f)
    val base = 0.18f + 0.3f * seg(cut, 0.2f, 0.7f)
    val firmA = mix(base, 1f, hold)
    if (hold > 0f) drawPath(firm, ArtInk.Gold.copy(alpha = 0.3f * hold), style = Stroke(8f * dp, cap = StrokeCap.Round))
    drawPath(
        firm, lerp(ArtInk.Ivory, ArtInk.Gold, hold).copy(alpha = firmA),
        style = Stroke(mix(1.5f, 3f, hold) * dp, cap = StrokeCap.Round, pathEffect = if (hold < 0.5f) PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)) else null),
    )
    drawCircle(lerp(ArtInk.Ivory, ArtInk.Gold, hold).copy(alpha = firmA), radius = 0.75f * s, center = ring + Offset(0f, -0.2f * s), style = Stroke(mix(1.2f, 2.4f, hold) * dp))
    if (hold > 0.5f) {
        val q = fract(t * 0.35f)
        val spark = cubic(anchorG, g1, g2, ring, q)
        drawCircle(ArtInk.Ivory.copy(alpha = (hold - 0.5f) * 2f * (1f - q)), radius = 2f * dp, center = spark)
    }

    // three frayed ropes: worshipped, followed, obeyed
    val anchors = listOf(Offset(w * 0.13f, -2f), Offset(w * 0.32f, -2f), Offset(w * 0.86f, -2f))
    for (k in 0 until 3) {
        val appear = easeOut(seg(a.b("rope", k), 0f, 0.7f))
        if (appear <= 0f) continue
        val p0 = anchors[k]
        val p1 = p0 + Offset(0f, h * 0.24f)
        val p2 = ring + Offset((p0.x - ring.x) * 0.28f, -h * 0.11f)
        val q = easeInOut(seg(cut, 0.12f * k, 0.12f * k + 0.62f))
        val brk = 0.3f
        if (q <= 0f) {
            braid(cubicPart(p0, p1, p2, ring, 0f, appear))
            frays(p0, p1, p2, ring, k, appear, t, 0.5f + 0.5f * seg(cut, 0f, 0.1f))
            val tagP = cubic(p0, p1, p2, ring, 0.42f)
            tagAt(a.tag("rope", k), tagP + Offset(if (k == 2) 22f * dp else -22f * dp, 0f), seg(a.b("rope", k), 0.35f, 0.9f), back = Color(0xCC4A1A10))
        } else {
            // the upper end springs back, the rest falls into the chasm
            val up = Offset(0f, -q * h * 0.12f)
            braid(cubicPart(p0, p1, p2, ring, 0f, brk, up), alpha = 1f - q)
            val fall = Offset((k - 1f) * q * w * 0.05f, q * q * h * 0.6f)
            braid(cubicPart(p0, p1, p2, ring, brk, 1f, fall), alpha = (1f - q).pow(1.5f))
            val tagP = cubic(p0, p1, p2, ring, 0.42f) + fall
            tagAt(a.tag("rope", k), tagP + Offset(if (k == 2) 22f * dp else -22f * dp, 0f), 1f - q, back = Color(0xCC4A1A10))
        }
    }

    // the lantern itself
    val flick = (0.14f * sin(t * 9f) + 0.09f * sin(t * 13.7f)) * (0.25f + unsteady)
    lantern(ring, s, mix(0.35f, 1f, hold), flick)

    // tags: the firm handhold, then the meaning
    tagAt(a.tag("hold"), cubic(anchorG, g1, g2, ring, 0.58f) + Offset(44f * dp, 0f), seg(a.b("hold"), 0.3f, 0.9f), back = Color(0xCC3A2808), ink = ArtInk.Gold)
    tagAt(a.tag("meaning", 0, 0), Offset(w * 0.2f, h * 0.84f), meaning, back = Color(0xCC4A1A10), ink = ArtInk.Ember)
    tagAt(a.tag("meaning", 0, 1), Offset(w * 0.82f, h * 0.07f), meaning, back = Color(0xCC3A2808), ink = ArtInk.Gold)
}

internal fun DrawScope.hills(color: Color, base: Float, amp: Float, f1: Float, f2: Float, ph: Float) {
    val w = size.width
    val h = size.height
    val path = Path().apply {
        moveTo(0f, h)
        var x = 0f
        while (x <= w + 8f) {
            val xd = x / density
            lineTo(x, base + amp * (sin(xd * f1 + ph) * 0.6f + sin(xd * f2 + ph * 2.3f) * 0.4f))
            x += 6f * density
        }
        lineTo(w, h)
        close()
    }
    drawPath(path, color)
}

/** A braided rope stroke. */
private fun DrawScope.braid(path: Path, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    drawPath(path, DuskInk.Rope, alpha = alpha, style = Stroke(2.6f * density, cap = StrokeCap.Round))
    drawPath(path, DuskInk.RopeDark, alpha = alpha, style = Stroke(2.6f * density, pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.4f * density, 2.4f * density))))
}

/** Loose fibres along a rope; they stand out more as [strain] grows. */
private fun DrawScope.frays(p0: Offset, p1: Offset, p2: Offset, p3: Offset, k: Int, upTo: Float, t: Float, strain: Float) {
    for (m in 0 until 5) {
        val q = 0.15f + 0.16f * m + 0.05f * k
        if (q > upTo) continue
        val c = cubic(p0, p1, p2, p3, q)
        val d = cubic(p0, p1, p2, p3, min(1f, q + 0.02f)) - c
        val len = sqrt(d.x * d.x + d.y * d.y).coerceAtLeast(0.001f)
        val n = Offset(-d.y / len, d.x / len)
        val side = if ((m + k) % 2 == 0) 1f else -1f
        val l = (3f + 3f * strain + sin(t * 2f + m) * 1f) * density
        drawLine(DuskInk.Rope.copy(alpha = 0.8f), c, c + n * (l * side) + Offset(0f, 1.5f * density), 0.8f * density, StrokeCap.Round)
    }
}

// ───────────── ship: sincerity in the storm ─────────────

private object SeaInk {
    val CalmTop = Color(0xFF283B69)
    val CalmMid = Color(0xFF7A5A7E)
    val CalmLow = Color(0xFFF0B07A)
    val StormTop = Color(0xFF0D121C)
    val StormMid = Color(0xFF232C3D)
    val StormLow = Color(0xFF3B4658)
    val SeaCalm = Color(0xFF284A75)
    val SeaCalmDeep = Color(0xFF122746)
    val SeaStorm = Color(0xFF17212D)
    val SeaStormDeep = Color(0xFF080E16)
    val Land = Color(0xFF2B263B)
    val LandStorm = Color(0xFF151520)
    val Stone = Color(0xFF1B1726)
    val Hull = Color(0xFF2E1C11)
    val HullLight = Color(0xFF6A4528)
    val Sail = Color(0xFFEFE4C8)
    val Cloud = Color(0xFF2B3344)
}

/**
 * The polytheists of old called on Allah alone at sea in the storm, and on others once safe on land (al-Ankabut
 * 65); some later ones call on others in both. Beats: lord (they acknowledged the Creator), ease (calls split
 * to the "intercessors" on the shore), storm (one call, upward), later (the later ones: lordship claimed for
 * them too), storm2 (the split calls stay, even in the storm). Extras: the two act titles.
 */
private fun DrawScope.shipScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val lord = easeOut(a.b("lord"))
    val ease = easeOut(a.b("ease"))
    val stormA = easeInOut(seg(a.b("storm"), 0f, 0.6f)) * (1f - easeInOut(seg(a.b("later"), 0f, 0.5f)))
    val later = easeOut(a.b("later"))
    val stormB = easeInOut(seg(a.b("storm2"), 0f, 0.6f))
    val s = max(stormA, stormB)
    val horizon = h * 0.56f

    // sky: dusk calm, slate storm
    drawRect(Brush.verticalGradient(
        0f to lerp(SeaInk.CalmTop, SeaInk.StormTop, s),
        0.55f to lerp(SeaInk.CalmMid, SeaInk.StormMid, s),
        1f to lerp(SeaInk.CalmLow, SeaInk.StormLow, s),
        startY = 0f, endY = horizon,
    ), size = Size(w, horizon))
    // they acknowledged the Creator: the sky brightens at dawn of the scene
    if (lord > 0f && s < 1f) {
        val c = Offset(w * 0.6f, horizon)
        val r = w * (0.2f + 0.25f * lord)
        drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.5f * lord * (1f - s)), Color.Transparent), center = c, radius = r), radius = r, center = c)
        drawCircle(Color(0xFFFFE2AC).copy(alpha = (0.5f + 0.5f * lord) * (1f - s)), radius = w * 0.045f, center = c)
    }
    // storm clouds roll in
    if (s > 0f) {
        for (m in 0 until 9) {
            val cxx = fract(m * 0.137f + t * 0.012f * (1 + m % 3)) * (w * 1.3f) - w * 0.15f
            val cyy = h * (0.04f + 0.06f * (m % 3))
            val rr = w * (0.09f + 0.04f * (m % 4))
            drawCircle(SeaInk.Cloud.copy(alpha = 0.95f * s), radius = rr, center = Offset(cxx, cyy))
        }
        // lightning now and then
        val ph = fract(t * 0.27f + 0.3f)
        if (s > 0.55f && ph < 0.05f) {
            val f = (1f - ph / 0.05f) * s
            drawRect(Color.White.copy(alpha = 0.16f * f), size = Size(w, horizon))
            val bolt = Path().apply {
                val x0 = w * 0.16f
                moveTo(x0, h * 0.1f)
                lineTo(x0 + w * 0.03f, h * 0.22f); lineTo(x0 - w * 0.01f, h * 0.24f); lineTo(x0 + w * 0.035f, h * 0.38f)
                lineTo(x0 + w * 0.005f, h * 0.4f); lineTo(x0 + w * 0.04f, h * 0.52f)
            }
            drawPath(bolt, Color.White.copy(alpha = 0.9f * f), style = Stroke(1.6f * dp, join = StrokeJoin.Round))
        }
    }

    // sea
    drawRect(
        Brush.verticalGradient(listOf(lerp(SeaInk.SeaCalm, SeaInk.SeaStorm, s), lerp(SeaInk.SeaCalmDeep, SeaInk.SeaStormDeep, s)), startY = horizon, endY = h),
        topLeft = Offset(0f, horizon), size = Size(w, h - horizon),
    )
    for (k in 0 until 3) wave(k, horizon, s, t)

    // the shore and its standing stones (what they called on besides Allah)
    val landC = lerp(SeaInk.Land, SeaInk.LandStorm, s)
    drawPath(Path().apply {
        moveTo(w * 0.7f, h)
        lineTo(w * 0.73f, h * 0.66f)
        cubicTo(w * 0.78f, h * 0.6f, w * 0.86f, h * 0.585f, w, h * 0.58f)
        lineTo(w, h)
        close()
    }, landC)
    val stones = listOf(Offset(w * 0.82f, h * 0.6f), Offset(w * 0.885f, h * 0.588f), Offset(w * 0.945f, h * 0.585f))
    val shore = ease * (1f - stormA * (1f - later))
    for ((n, st) in stones.withIndex()) {
        val sw = w * 0.022f
        val sh = h * (0.07f + 0.012f * (n % 2))
        if (shore > 0f) {
            drawCircle(Brush.radialGradient(listOf(ArtInk.Ember.copy(alpha = 0.55f * shore), Color.Transparent), center = st - Offset(0f, sh), radius = sw * 2.6f), radius = sw * 2.6f, center = st - Offset(0f, sh))
        }
        drawRoundRect(SeaInk.Stone, topLeft = Offset(st.x - sw / 2, st.y - sh), size = Size(sw, sh + 2f * dp), cornerRadius = CornerRadius(sw / 2, sw / 2))
        // the later ones: lordship claimed for them, rays to the sky
        if (later > 0f) {
            val top = st - Offset(0f, sh)
            val end = top + Offset((n - 1) * w * 0.03f, -h * 0.22f * later)
            drawLine(ArtInk.Ember.copy(alpha = 0.25f * later), top, end, 5f * dp, StrokeCap.Round)
            drawLine(ArtInk.Ember.copy(alpha = 0.9f * later), top, end, 1.8f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(3f * dp, 4f * dp), -t * 14f * dp))
        }
    }

    // the ship, rocking more as the storm grows
    val angle = (2f + 10f * s) * sin(t * (1.2f + 0.8f * s))
    val bob = (0.004f + 0.014f * s) * h * sin(t * (1.6f + 1.2f * s))
    val p = Offset(w * 0.36f, h * 0.7f + bob)
    val l = w * 0.16f
    withTransform({ rotate(angle, pivot = p) }) { ship(p, l, s, t) }
    val rad = angle * PI.toFloat() / 180f
    val mLocal = Offset(0.05f * l, -0.95f * l)
    val mast = p + Offset(mLocal.x * cos(rad) - mLocal.y * sin(rad), mLocal.x * sin(rad) + mLocal.y * cos(rad))
    wave(3, horizon, s, t)

    // the calls: one upward to Allah; in ease (or always, for the later ones) also to the stones
    if (ease > 0f) {
        val up = 0.55f + 0.45f * ease + 0.5f * stormA
        val top = Offset(mast.x + w * 0.05f, -4f)
        val flow = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp), t * 22f * dp)
        drawLine(ArtInk.Gold.copy(alpha = 0.25f * min(1f, up)), mast, top, 6f * dp * min(1.6f, up), StrokeCap.Round)
        drawLine(ArtInk.Gold.copy(alpha = min(1f, up)), mast, top, 1.8f * dp, StrokeCap.Round, flow)
        if (shore > 0f) {
            for ((n, st) in stones.withIndex()) {
                val end = st - Offset(0f, h * 0.085f)
                val ctrl = Offset((mast.x + end.x) / 2f, min(mast.y, end.y) - h * (0.12f + 0.03f * n))
                val arc = Path().apply {
                    moveTo(mast.x, mast.y)
                    cubicTo(ctrl.x, ctrl.y, ctrl.x, ctrl.y, end.x, end.y)
                }
                drawPath(arc, ArtInk.Ember.copy(alpha = 0.85f * shore), style = Stroke(1.4f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp), -t * 18f * dp)))
            }
        }
    }

    // rain
    if (s > 0f) {
        val drops = (60 * s).toInt()
        for (m in 0 until drops) {
            val x0 = fract(m * 0.618f) * w * 1.1f
            val y0 = fract(t * (0.9f + 0.4f * fract(m * 0.37f)) + m * 0.13f) * h
            drawLine(Color(0xFFBFD0F5).copy(alpha = 0.45f * s), Offset(x0, y0), Offset(x0 - 4f * dp, y0 + 11f * dp), 1f * dp, StrokeCap.Round)
        }
    }

    // tags
    tagAt(a.tag("lord"), Offset(w * 0.52f, h * 0.1f), lord * (1f - s), back = Color(0xCC2A2410), ink = ArtInk.Gold)
    tagAt(a.tag("ease"), Offset(w * 0.885f, h * 0.44f), ease * (1f - 0.8f * stormA * (1f - later)), back = Color(0xCC4A1A10), ink = ArtInk.Ember)
    tagAt(a.tag("storm"), Offset(mast.x + w * 0.2f, h * 0.2f), stormA, back = Color(0xCC2A2410), ink = ArtInk.Gold)
    tagAt(a.tag("later"), Offset(w * 0.85f, h * 0.27f), later * (1f - 0.75f * stormB), back = Color(0xCC4A1A10), ink = ArtInk.Ember)
    tagAt(a.tag("storm2"), Offset(w * 0.6f, h * 0.48f), stormB, back = Color(0xE64A1A10), ink = ArtInk.Ember)
    // act titles, top right (where a right-to-left reader starts)
    val first = a.extras.getOrNull(0)
    val second = a.extras.getOrNull(1)
    if (first != null) tagAt(first, Offset(w - first.size.width / 2f - 14f * dp, 14f * dp), (1f - later) * min(1f, lord * 2f), back = Color(0x99101426))
    if (second != null) tagAt(second, Offset(w - second.size.width / 2f - 14f * dp, 14f * dp), later, back = Color(0x99101426))
}

/** Wave layer [k] (0 far … 3 front) between the horizon and the bottom. */
private fun DrawScope.wave(k: Int, horizon: Float, s: Float, t: Float) {
    val w = size.width
    val h = size.height
    val dp = density
    val y = horizon + (h - horizon) * (0.12f + 0.25f * k)
    val amp = (0.006f + 0.032f * s) * h * (1f + 0.35f * k)
    val len = w * (0.22f - 0.025f * k)
    val speed = (0.6f + 1.6f * s) * (1f + 0.2f * k)
    val col = lerp(lerp(SeaInk.SeaCalm, SeaInk.SeaCalmDeep, 0.3f * k), lerp(SeaInk.SeaStorm, SeaInk.SeaStormDeep, 0.25f * k), s)
    val path = Path().apply {
        moveTo(0f, h)
        var x = 0f
        while (x <= w + 6f * dp) {
            val ph = (x / len) * 2f * PI.toFloat() + t * speed + k * 1.7f
            lineTo(x, y + amp * sin(ph) + amp * 0.35f * sin(ph * 2.3f + 1f))
            x += 5f * dp
        }
        lineTo(w, h)
        close()
    }
    drawPath(path, col.copy(alpha = if (k == 3) 0.92f else 1f))
    // foam on the crests in the storm
    if (s > 0.2f) {
        var x = 0f
        while (x <= w) {
            val ph = (x / len) * 2f * PI.toFloat() + t * speed + k * 1.7f
            if (sin(ph) < -0.85f) drawCircle(ArtInk.Ivory.copy(alpha = 0.5f * s), radius = 1.3f * dp, center = Offset(x, y + amp * sin(ph) + amp * 0.35f * sin(ph * 2.3f + 1f)))
            x += 7f * dp
        }
    }
}

/** A dhow with a lateen sail, centred at [p] (waterline), [l] half its length. */
private fun DrawScope.ship(p: Offset, l: Float, s: Float, t: Float) {
    val dp = density
    val hull = Path().apply {
        moveTo(p.x - 1.0f * l, p.y - 0.2f * l)
        cubicTo(p.x - 0.72f * l, p.y + 0.04f * l, p.x - 0.4f * l, p.y + 0.17f * l, p.x, p.y + 0.18f * l)
        cubicTo(p.x + 0.45f * l, p.y + 0.17f * l, p.x + 0.76f * l, p.y + 0.08f * l, p.x + 0.86f * l, p.y - 0.14f * l)
        lineTo(p.x + 0.8f * l, p.y - 0.08f * l)
        lineTo(p.x - 0.88f * l, p.y - 0.1f * l)
        close()
    }
    drawPath(hull, SeaInk.Hull)
    drawLine(SeaInk.HullLight, Offset(p.x - 0.86f * l, p.y - 0.06f * l), Offset(p.x + 0.8f * l, p.y - 0.04f * l), 1.4f * dp)
    // mast and yard
    val mastTop = Offset(p.x + 0.05f * l, p.y - 0.95f * l)
    drawLine(SeaInk.Hull, Offset(p.x + 0.05f * l, p.y - 0.08f * l), mastTop, 2f * dp)
    val yardA = Offset(p.x - 0.62f * l, p.y - 0.24f * l)
    val yardB = Offset(p.x + 0.36f * l, p.y - 1.04f * l)
    val flutter = sin(t * 11f) * 0.03f * l * s
    val clew = Offset(p.x + 0.14f * l + flutter, p.y - 0.13f * l)
    val sail = Path().apply {
        moveTo(yardA.x, yardA.y)
        lineTo(yardB.x, yardB.y)
        cubicTo(p.x + 0.34f * l + flutter, p.y - 0.62f * l, p.x + 0.26f * l, p.y - 0.3f * l, clew.x, clew.y)
        close()
    }
    drawPath(sail, Brush.linearGradient(listOf(SeaInk.Sail, lerp(SeaInk.Sail, Color(0xFF8C8270), 0.55f)), start = yardB, end = clew))
    drawLine(SeaInk.Hull, yardA, yardB, 1.6f * dp, StrokeCap.Round)
    // a small lamp at the masthead
    drawCircle(ArtInk.Gold, radius = 1.8f * dp, center = mastTop)
}
