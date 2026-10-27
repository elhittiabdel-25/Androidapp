package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.sin

/*
 * Round 19 of the redraw (2026-10-27): «السُّنَّةُ وَالْبِدْعَةُ». The other cards of this section were drawn in wave 7
 * under the same rules; one scene is new: the road and the side road painted off it into the fog, and a deed as a
 * dove that rises only on its two wings, sincerity and following, and falls when either is missing.
 */

private object WingC31 {
    val Sky = Color(0xFFBFD6EA)
    val SkyLow = Color(0xFFF6E6C8)
    val Land = Color(0xFFD8C49A)
    val LandLow = Color(0xFFBFA676)
    val Road = Color(0xFFE8C66A)
    val Fog = Color(0xFF8A8A98)
    val Ink = Color(0xFF2A2430)
    val Dove = Color(0xFFFBF8F0)
    val Gold = Color(0xFFF2D27A)
    val Smoke = Color(0xFF4A4450)
    val Patch = listOf(Color(0xFFC0463A), Color(0xFF3A6AB0), Color(0xFF6AA04A), Color(0xFFE8B83A))
    val Back = Color(0xE6201810)
}

/** A dove seen from the front, wings up or down by [flap]; each wing is whole (0), smoke (1) or a patchwork (2). */
private fun DrawScope.dove31(c: Offset, s: Float, rightKind: Int, leftKind: Int, flap: Float, t: Float, alpha: Float, tilt: Float = 0f) {
    if (alpha <= 0.01f) return
    withTransform({ rotate(tilt, c) }) {
        val lift = sin(flap) * s * 0.35f
        for (side in listOf(1f, -1f)) {
            val kind = if (side > 0f) rightKind else leftKind
            val wing = Path().apply {
                moveTo(c.x + side * s * 0.12f, c.y - s * 0.05f)
                quadraticTo(c.x + side * s * 0.6f, c.y - s * 0.3f - lift, c.x + side * s * 1.05f, c.y - s * 0.15f - lift * 1.4f)
                quadraticTo(c.x + side * s * 0.65f, c.y + s * 0.05f - lift * 0.4f, c.x + side * s * 0.12f, c.y + s * 0.12f)
                close()
            }
            when (kind) {
                0 -> {
                    drawPath(wing, WingC31.Dove, alpha = alpha)
                    drawPath(wing, WingC31.Gold, alpha = 0.7f * alpha, style = Stroke(1.2f * density))
                }
                1 -> {
                    drawPath(wing, WingC31.Smoke, alpha = 0.75f * alpha)
                    for (k in 0 until 4) {
                        val f = fract(t * 0.7f + k / 4f)
                        drawCircle(WingC31.Smoke, s * (0.08f + 0.1f * f), Offset(c.x + side * s * (0.4f + 0.15f * k), c.y - s * 0.2f - lift - s * 0.3f * f), alpha = 0.5f * (1f - f) * alpha)
                    }
                }
                else -> {
                    drawPath(wing, WingC31.Dove, alpha = alpha)
                    for (k in 0 until 4) {
                        val px = c.x + side * s * (0.25f + 0.18f * k)
                        drawRect(WingC31.Patch[k], Offset(px - s * 0.08f, c.y - s * (0.12f + 0.07f * k) - lift * (0.3f + 0.25f * k)), Size(s * 0.16f, s * 0.12f), alpha = 0.85f * alpha)
                    }
                    drawPath(wing, WingC31.Ink, alpha = 0.5f * alpha, style = Stroke(1f * density, pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f * density, 2f * density))))
                }
            }
        }
        drawOval(WingC31.Dove, Offset(c.x - s * 0.16f, c.y - s * 0.12f), Size(s * 0.32f, s * 0.5f), alpha = alpha)
        drawCircle(WingC31.Dove, s * 0.12f, c + Offset(0f, -s * 0.2f), alpha = alpha)
        drawPath(Path().apply { moveTo(c.x - s * 0.1f, c.y + s * 0.34f); lineTo(c.x, c.y + s * 0.55f); lineTo(c.x + s * 0.1f, c.y + s * 0.34f); close() }, WingC31.Dove, alpha = alpha)
    }
}

/**
 * Dawn over open land. Beats: sideroad (a gold road runs straight to the horizon; a man paints a new road off it and
 * walks it into the fog), twowings (a white dove rises from the road on two wings, «الْإِخْلَاصُ» and «الْمُتَابَعَةُ», up
 * into the light), nosincere (another dove whose wing of sincerity is smoke: «الشِّرْكُ»; it cannot rise and falls),
 * nofollow (a third whose wing of following is a patchwork of its own making: «الْبِدْعَةُ»; it falls too).
 */
internal fun DrawScope.twowingsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sideroad" to Shot(Offset(w * 0.35f, h * 0.66f), 1.2f),
        "twowings" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "nosincere" to Shot(Offset(w * 0.28f, h * 0.62f), 1.3f),
        "nofollow" to Shot(Offset(w * 0.72f, h * 0.62f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { twowingsDawn(a) }
}

private fun DrawScope.twowingsDawn(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sr = a.b("sideroad")
    val tw = a.b("twowings")
    val ns = a.b("nosincere")
    val nf = a.b("nofollow")
    val horizon = h * 0.58f
    drawRect(Brush.verticalGradient(listOf(WingC31.Sky, WingC31.SkyLow), endY = horizon), size = Size(w, horizon))
    lightShafts(Offset(w * 0.5f, -h * 0.05f), PI.toFloat() / 2f, 0.6f, h * 0.55f, Color(0xFFFFF4D8), 0.25f + 0.25f * easeOut(seg(tw, 0.4f, 0.9f)), t, rays = 5)
    drawRect(Brush.verticalGradient(listOf(WingC31.Land, WingC31.LandLow), startY = horizon, endY = h), Offset(0f, horizon), Size(w, h - horizon))
    // the straight road to the horizon
    drawPath(Path().apply { moveTo(w * 0.49f, horizon); lineTo(w * 0.51f, horizon); lineTo(w * 0.6f, h); lineTo(w * 0.4f, h); close() }, WingC31.Road, alpha = 0.85f)
    // the side road painted off it, into the fog
    val paint = easeInOut(seg(sr, 0.15f, 0.7f))
    val fogA = easeOut(seg(sr, 0.3f, 0.7f)) * (1f - 0.6f * easeOut(seg(tw, 0f, 0.3f)))
    if (sr > 0f) {
        val from = Offset(w * 0.47f, h * 0.84f)
        val to = Offset(w * 0.08f, h * 0.66f)
        val ctrl = Offset(w * 0.3f, h * 0.84f)
        val side = Path()
        for (k in 0..20) {
            val u = k / 20f * paint
            val q = from * ((1 - u) * (1 - u)) + ctrl * (2 * u * (1 - u)) + to * (u * u)
            if (k == 0) side.moveTo(q.x, q.y) else side.lineTo(q.x, q.y)
        }
        drawPath(side, Color(0xFFC8A060), alpha = 0.9f * (1f - 0.5f * easeOut(seg(tw, 0f, 0.3f))), style = Stroke(h * 0.022f, cap = StrokeCap.Round))
        for (k in 0 until 6) {
            val f = fract(t * 0.05f + k / 6f)
            drawCircle(WingC31.Fog, w * (0.08f + 0.03f * (k % 3)), Offset(w * (0.04f + 0.06f * k) + w * 0.03f * sin(t * 0.3f + k), h * (0.64f + 0.03f * (k % 2))), alpha = 0.45f * fogA * (0.7f + 0.3f * f))
        }
        val u = paint
        val man = from * ((1 - u) * (1 - u)) + ctrl * (2 * u * (1 - u)) + to * (u * u)
        val vanish = 1f - easeOut(seg(sr, 0.7f, 0.95f))
        figure(man, h * 0.15f * (1f - 0.4f * u), WingC31.Ink, easeOut(seg(sr, 0f, 0.2f)) * vanish, walk = if (paint in 0.01f..0.99f) t * 5f else 0f, arms = 0.5f)
    }
    // the accepted deed: both wings, up into the light
    if (tw > 0f) {
        val rise = easeInOut(seg(tw, 0.2f, 0.9f))
        val c = Offset(w * 0.5f, mix(h * 0.8f, h * 0.22f, rise))
        val s = w * 0.07f
        val o = easeOut(seg(tw, 0f, 0.2f))
        dove31(c, s, 0, 0, t * 6f, t, o)
        if (rise > 0.95f) softGlow(c, w * 0.08f, WingC31.Gold, 0.5f)
        val tg = seg(tw, 0.15f, 0.35f)
        tagAt(a.tag("twowings", 0, 0), c + Offset(s * 1.6f, -s * 0.2f), tg * (1f - seg(ns, 0f, 0.3f)), back = WingC31.Back, ink = ArtInk.BrassLight)
        tagAt(a.tag("twowings", 0, 1), c + Offset(-s * 1.6f, -s * 0.2f), tg * (1f - seg(ns, 0f, 0.3f)), back = WingC31.Back, ink = ArtInk.BrassLight)
    }
    // without sincerity: the wing is smoke
    if (ns > 0f) {
        val up = easeOut(seg(ns, 0.1f, 0.4f))
        val down = easeIn31(seg(ns, 0.5f, 0.85f))
        val c = Offset(w * 0.26f, h * 0.8f - h * 0.2f * up + h * 0.24f * down)
        dove31(c, w * 0.055f, 1, 0, t * 9f, t, easeOut(seg(ns, 0f, 0.15f)), tilt = -40f * down + 8f * sin(t * 7f) * up * (1f - down))
        tagAt(a.tag("nosincere"), Offset(w * 0.12f, h * 0.42f), seg(ns, 0.3f, 0.5f), back = WingC31.Back, ink = Color(0xFFFFB0A0))
    }
    // without following: a patchwork wing of its own making
    if (nf > 0f) {
        val up = easeOut(seg(nf, 0.1f, 0.4f))
        val down = easeIn31(seg(nf, 0.5f, 0.85f))
        val c = Offset(w * 0.74f, h * 0.8f - h * 0.2f * up + h * 0.24f * down)
        dove31(c, w * 0.055f, 0, 2, t * 9f, t, easeOut(seg(nf, 0f, 0.15f)), tilt = 40f * down + 8f * sin(t * 7f) * up * (1f - down))
        tagAt(a.tag("nofollow"), Offset(w * 0.88f, h * 0.42f), seg(nf, 0.3f, 0.5f), back = WingC31.Back, ink = Color(0xFFFFB0A0))
    }
}

private fun easeIn31(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }
