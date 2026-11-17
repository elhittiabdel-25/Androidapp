package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.sin

/*
 * Wave 100 (2026-11-17): the nine cards of «الغزوات» redrawn in the illustrated style (boards in wave 95).
 * Armies are rows of faceless figures (believers jade, their enemies ember); no blood, no blows: the story is told by
 * numbers, ground, banners and light. His ﷺ named light; the angels at Badr are cool white light; companions are
 * roundels (the closest in colour: Abu Bakr 0, Umar 1, Uthman 2, his family 3; others plain).
 */

private val Jd100 = Color(0xFFC8E8D0)
private val Em100 = Color(0xFFB8786A)

/** A row of [n] faceless figures between x0 and x1 standing on [ground]. */
private fun DrawScope.army100(x0: Float, x1: Float, ground: Float, n: Int, color: Color, alpha: Float, walk: Float = 0f, tall: Float = 0.08f) {
    if (alpha <= 0.01f) return
    for (k in 0 until n) {
        val u = if (n == 1) 0.5f else k / (n - 1f)
        person99(Offset(mix(x0, x1, u), ground - (k % 2) * size.height * 0.015f), size.height * tall, color, alpha, walk = walk + k * 0.3f)
    }
}

private fun DrawScope.tent100(foot: Offset, s: Float, color: Color, alpha: Float, fall: Float = 0f) {
    if (alpha <= 0.01f) return
    val top = foot + Offset(s * 0.2f * fall, -s * (1f - 0.7f * fall))
    drawPath(Path().apply { moveTo(foot.x - s * 0.6f, foot.y); lineTo(top.x, top.y); lineTo(foot.x + s * 0.6f, foot.y); close() }, color, alpha = alpha)
}

private fun DrawScope.fort100(foot: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val c = Color(0xFF8A7A66)
    drawRect(c, Offset(foot.x - s * 0.5f, foot.y - s), Size(s, s), alpha = alpha)
    for (k in 0 until 3) drawRect(c, Offset(foot.x - s * 0.5f + k * s * 0.4f, foot.y - s * 1.15f), Size(s * 0.2f, s * 0.15f), alpha = alpha)
    drawRect(Color(0xFF3A3028), Offset(foot.x - s * 0.1f, foot.y - s * 0.35f), Size(s * 0.2f, s * 0.35f), alpha = alpha)
}

private fun DrawScope.hill100(c: Offset, w: Float, hgt: Float, color: Color, alpha: Float) {
    drawPath(Path().apply { moveTo(c.x - w / 2, c.y); quadraticBezierTo(c.x, c.y - hgt * 2f, c.x + w / 2, c.y); close() }, color, alpha = alpha)
}

private fun DrawScope.dusk100(a: ArtCtx, ground: Float) {
    nightSky(a.t, S97.Dusk, S97.DuskLow, ground, 0.5f)
    dunes97(ground, S97.SandDark, Color(0xFF6A5236))
}

// ═════════════ gz-badr ═════════════
internal fun DrawScope.gzbadrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "gzbadr", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.6f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1.05f)),
        { dusk100(a, ground) },
        { p, f ->
            qIcon("crescent", Offset(w * 0.8f, h * 0.18f), w * 0.08f, Qn.GoldLight, f * seg(p, 0f, 0.2f), a.t)
            army100(w * 0.15f, w * 0.6f, ground, 9, Jd100, f * seg(p, 0.15f, 0.4f), a.t * 2f)
            sirajMunir(Offset(w * 0.68f, ground - h * 0.18f), w * 0.025f, f, a.t, named = false)
            tagAt(a.tag("gzbadr0", 0, 0), Offset(w * 0.8f, h * 0.29f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("gzbadr0", 0, 2), Offset(w * 0.4f, h * 0.18f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzbadr0", 0, 1), Offset(w * 0.38f, ground - h * 0.18f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            army100(w * 0.06f, w * 0.34f, ground, 6, Jd100, f)
            army100(w * 0.5f, w * 0.96f, ground, 16, Em100, f * seg(p, 0.2f, 0.5f), tall = 0.09f)
            tagAt(a.tag("gzbadr1", 0, 0), Offset(w * 0.2f, ground - h * 0.18f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("gzbadr1", 0, 1), Offset(w * 0.73f, ground - h * 0.2f), f * seg(p, 0.4f, 0.55f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            army100(w * 0.06f, w * 0.34f, ground, 6, Jd100, f)
            val down = easeOut(seg(p, 0.05f, 0.45f))
            for (k in 0 until 5) angel98(Offset(w * (0.1f + 0.06f * k), mix(-h * 0.1f, h * (0.28f + 0.04f * (k % 2)), down)), w * 0.03f, f)
            val flee = easeInOut(seg(p, 0.45f, 0.85f))
            army100(w * 0.55f + flee * w * 0.3f, w * 0.96f + flee * w * 0.3f, ground, 12, Em100, f * (1f - 0.5f * flee), walk = a.t * 2f * flee)
            tagAt(a.tag("gzbadr2", 0, 0), Offset(w * 0.22f, h * 0.14f), f * seg(p, 0.2f, 0.35f), back = Color(0xCC20283A), ink = ArtInk.Ivory)
            tagAt(a.tag("gzbadr2", 0, 1), Offset(w * 0.72f, h * 0.4f), f * seg(p, 0.6f, 0.72f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("gzbadr2", 0, 2), Offset(w * 0.72f, h * 0.52f), f * seg(p, 0.72f, 0.85f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.5f * f)
            person99(Offset(w * 0.32f, ground), h * 0.14f, Em100, f)
            for (k in 0 until 4) person99(Offset(w * (0.5f + 0.1f * k), ground), h * 0.08f, Qn.Person, f * seg(p, 0.25f + 0.05f * k, 0.4f + 0.05f * k))
            qIcon("pen", Offset(w * 0.42f, h * 0.48f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.2f, 0.4f), a.t)
            qIcon("lock", Offset(w * 0.32f, h * 0.48f), w * 0.05f, Qn.GoldLight, f * (1f - seg(p, 0.65f, 0.85f)), a.t)
            tagAt(a.tag("gzbadr3", 0, 0), Offset(w * 0.32f, h * 0.36f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzbadr3", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            tagAt(a.tag("gzbadr3", 0, 2), Offset(w * 0.7f, ground - h * 0.16f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ gz-uhud ═════════════
internal fun DrawScope.gzuhudScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    val mount = Offset(w * 0.3f, ground)
    film98(a, "gzuhud", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.35f, h * 0.55f), 1.1f), Shot(Offset(w * 0.4f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { daySky12(Qn.DayTop, Color(0xFFF0D8A8), ground, Color(0xFFA88A5E)); hill100(mount, w * 0.7f, h * 0.22f, Color(0xFF8A6A4E), 1f); hill100(Offset(w * 0.12f, ground), w * 0.25f, h * 0.08f, Color(0xFF9A7A58), 1f) },
        { p, f ->
            val come = easeInOut(seg(p, 0.05f, 0.7f))
            army100(mix(w * 1.1f, w * 0.55f, come), mix(w * 1.6f, w * 0.97f, come), ground, 14, Em100, f, walk = a.t * 2f * (1f - come))
            qIcon("hourglass", Offset(w * 0.2f, h * 0.2f), w * 0.07f, Qn.GoldLight, f * seg(p, 0f, 0.15f), a.t)
            tagAt(a.tag("gzuhud0", 0, 1), Offset(w * 0.2f, h * 0.32f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("gzuhud0", 0, 0), Offset(w * 0.76f, ground - h * 0.2f), f * seg(p, 0.4f, 0.55f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("gzuhud0", 0, 2), Offset(w * 0.76f, h * 0.14f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 5) person99(Offset(w * (0.22f + 0.04f * k), ground - h * (0.14f + 0.04f * (2 - kotlin.math.abs(k - 2)))), h * 0.06f, Jd100, f * seg(p, 0.05f + 0.04f * k, 0.2f + 0.04f * k))
            army100(w * 0.5f, w * 0.75f, ground, 6, Jd100, f)
            sirajMunir(Offset(w * 0.62f, ground - h * 0.17f), w * 0.022f, f, a.t, named = false)
            tagAt(a.tag("gzuhud1", 0, 0), Offset(w * 0.3f, h * 0.3f), f * seg(p, 0.2f, 0.35f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("gzuhud1", 0, 1), Offset(w * 0.62f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            val down = easeInOut(seg(p, 0.05f, 0.4f))
            for (k in 0 until 4) person99(Offset(w * (0.22f + 0.04f * k) + down * w * 0.1f, mix(ground - h * 0.17f, ground, down)), h * 0.06f, Jd100, f, walk = a.t * 2f * down)
            person99(Offset(w * 0.3f, ground - h * 0.18f), h * 0.06f, Jd100, f)
            val ride = easeInOut(seg(p, 0.45f, 0.95f))
            for (k in 0 until 5) person99(Offset(mix(w * -0.05f, w * 0.2f, ride) - k * w * 0.04f, ground), h * 0.08f, Em100, f * seg(p, 0.4f, 0.55f), walk = a.t * 3f)
            tagAt(a.tag("gzuhud2", 0, 0), Offset(w * 0.4f, h * 0.36f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            plain97(Offset(w * 0.14f, h * 0.36f), w * 0.07f, a, "gzuhud2", 1, f * seg(p, 0.55f, 0.75f))
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.55f * f)
            close97(Offset(w * 0.5f, h * 0.46f), w * 0.1f, a, "gzuhud3", 0, 3, f * seg(p, 0.05f, 0.3f))
            for (k in 0 until 8) {
                val u = ((a.t * 0.2f + k / 8f) % 1f)
                drawCircle(Qn.GoldLight, w * 0.006f, Offset(w * (0.38f + 0.03f * k), h * 0.46f - u * h * 0.35f), alpha = f * seg(p, 0.35f, 0.55f) * (1f - u))
            }
            qIcon("star", Offset(w * 0.5f, h * 0.18f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.45f, 0.65f), a.t)
            tagAt(a.tag("gzuhud3", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ gz-khandaq ═════════════
internal fun DrawScope.gzkhandaqScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    val trenchX = w * 0.5f
    film98(a, "gzkhandaq", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.6f), 1.05f), Shot(Offset(w * 0.6f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { madinah100(ground, a.t) },
        { p, f ->
            for (side in 0 until 3) {
                val come = easeInOut(seg(p, 0.05f + 0.15f * side, 0.6f + 0.1f * side))
                army100(mix(w * 1.2f, w * (0.55f + 0.14f * side), come), mix(w * 1.4f, w * (0.67f + 0.14f * side), come), ground, 3, Em100, f, walk = a.t * 2f)
            }
            tagAt(a.tag("gzkhandaq0", 0, 0), Offset(w * 0.22f, ground - h * 0.28f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzkhandaq0", 0, 1), Offset(w * 0.75f, h * 0.18f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            val dig = easeInOut(seg(p, 0.15f, 0.75f))
            drawRect(Color(0xFF3A2A1C), Offset(trenchX - w * 0.03f, ground - h * 0.01f), Size(w * 0.06f, h * 0.16f * dig), alpha = f)
            drawLine(Qn.Jade, Offset(trenchX, ground - h * 0.4f * dig), Offset(trenchX, ground), 1.5f * density, alpha = 0.5f * f)
            for (k in 0 until 3) person99(Offset(w * (0.3f + 0.06f * k), ground), h * 0.08f, Jd100, f, arms = 0.5f + 0.5f * sin(a.t * 4f + k))
            army100(w * 0.62f, w * 0.95f, ground, 7, Em100, f)
            plain97(Offset(w * 0.2f, h * 0.36f), w * 0.07f, a, "gzkhandaq1", 2, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("gzkhandaq1", 0, 1), Offset(trenchX, h * 0.36f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("gzkhandaq1", 0, 0), Offset(w * 0.8f, ground - h * 0.2f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.5f * f)
            val blow = seg(p, 0.15f, 0.7f)
            for (k in 0 until 9) {
                val y = h * (0.2f + 0.07f * k)
                val x = ((a.t * 0.6f + k * 0.13f) % 1.3f) * w - w * 0.15f
                drawLine(Color(0xFFD8DCE8), Offset(x, y), Offset(x + w * 0.18f, y + h * 0.01f), 1.6f * density, StrokeCap.Round, alpha = 0.6f * f * blow)
            }
            for (k in 0 until 4) tent100(Offset(w * (0.55f + 0.11f * k), ground), w * 0.06f, Color(0xFF8A5A4A), f, fall = easeOut(seg(p, 0.3f + 0.08f * k, 0.6f + 0.08f * k)))
            for (k in 0 until 3) drawCircle(Qn.Ember, w * 0.012f * (1f - seg(p, 0.5f, 0.8f)), Offset(w * (0.6f + 0.12f * k), ground - h * 0.02f), alpha = f)
            qIcon("cloud", Offset(w * 0.3f, h * 0.2f), w * 0.09f, Color(0xFFD8DCE8), f * blow, a.t)
            tagAt(a.tag("gzkhandaq2", 0, 0), Offset(w * 0.3f, h * 0.32f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzkhandaq2", 0, 1), Offset(w * 0.72f, ground - h * 0.16f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("gzkhandaq2", 0, 2), Offset(w * 0.72f, ground + h * 0.06f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.6f * f)
            qIcon("heart", Offset(w * 0.3f, h * 0.45f), w * 0.11f, Qn.Rose, f * seg(p, 0.05f, 0.25f), a.t)
            balance12(Offset(w * 0.7f, h * 0.6f), w * 0.12f, 0.15f * sin(a.t), Qn.GoldLight, f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("gzkhandaq3", 0, 0), Offset(w * 0.3f, h * 0.68f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzkhandaq3", 0, 1), Offset(w * 0.7f, h * 0.72f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

private fun DrawScope.madinah100(ground: Float, t: Float) {
    val w = size.width
    val h = size.height
    daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB09868))
    for (k in 0 until 3) house12(Offset(w * (0.06f + 0.13f * k), ground), w * 0.09f, Color(0xFFE2CCA0), Color(0xFF8A6A40), 1f)
    palm12(Offset(w * 0.42f, ground), h * 0.18f, Color(0xFF6A4A30), Qn.Leaf, 0.85f, t)
}

// ═════════════ gz-hudaybiya ═════════════
internal fun DrawScope.gzhudayScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "gzhuday", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.4f, h * 0.55f), 1.08f), Shot(Offset(w * 0.5f, h * 0.5f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { daySky12(Qn.DayTop, Color(0xFFF0D8A8), ground, Color(0xFFB09868)) },
        { p, f ->
            kaaba29(Offset(w * 0.85f, ground), w * 0.07f, f)
            val go = easeInOut(seg(p, 0.05f, 0.5f))
            army100(w * 0.05f, mix(w * 0.15f, w * 0.45f, go), ground, 8, Color(0xFFF2EEE4), f, walk = a.t * 2f * (1f - seg(p, 0.5f, 0.6f)))
            army100(w * 0.6f, w * 0.75f, ground, 4, Em100, f * seg(p, 0.45f, 0.6f))
            qIcon("cross", Offset(w * 0.53f, h * 0.5f), w * 0.07f, Qn.Ember, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("gzhuday0", 0, 0), Offset(w * 0.85f, ground - h * 0.2f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("gzhuday0", 0, 2), Offset(w * 0.25f, h * 0.2f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzhuday0", 0, 1), Offset(w * 0.67f, ground - h * 0.18f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            palm12(Offset(w * 0.4f, ground), h * 0.36f, Color(0xFF6A4A30), Qn.Leaf, f, a.t)
            sirajMunir(Offset(w * 0.4f, ground - h * 0.16f), w * 0.028f, f, a.t, named = false)
            for (k in 0 until 8) {
                val x = w * (0.08f + 0.1f * k + if (k >= 3) 0.12f else 0f)
                person99(Offset(x, ground), h * 0.09f, Jd100, f * seg(p, 0.05f * k, 0.15f + 0.05f * k), arms = 0.7f * seg(p, 0.4f, 0.6f))
            }
            tagAt(a.tag("gzhuday1", 0, 2), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("gzhuday1", 0, 0), Offset(w * 0.4f, h * 0.36f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzhuday1", 0, 1), Offset(w * 0.75f, ground - h * 0.2f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.5f * f)
            sirajMunir(Offset(w * 0.22f, h * 0.45f), w * 0.04f, f, a.t, named = false)
            plain97(Offset(w * 0.78f, h * 0.45f), w * 0.08f, a, "gzhuday2", 1, f * seg(p, 0.1f, 0.3f))
            val c = Offset(w * 0.5f, h * 0.5f)
            drawRoundRect(Color(0xFFEFE2C0), Offset(c.x - w * 0.12f, c.y - h * 0.16f), Size(w * 0.24f, h * 0.32f), CornerRadius(6f * density), alpha = f * seg(p, 0.3f, 0.45f))
            for (k in 0 until 4) drawLine(Color(0xFF6A5030), Offset(c.x - w * 0.09f, c.y - h * 0.1f + k * h * 0.06f), Offset(c.x - w * 0.09f + w * 0.18f * seg(p, 0.4f + 0.1f * k, 0.5f + 0.1f * k), c.y - h * 0.1f + k * h * 0.06f), 1.4f * density, alpha = f)
            tagAt(a.tag("gzhuday2", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.55f * f)
            close97(Offset(w * 0.3f, h * 0.45f), w * 0.09f, a, "gzhuday3", 0, 1, f * seg(p, 0.05f, 0.25f))
            sirajMunir(Offset(w * 0.7f, h * 0.42f), w * 0.045f, f * seg(p, 0.3f, 0.5f), a.t, nameAt = Offset(w * 0.7f, h * 0.6f))
            drawLine(Qn.GoldLight, Offset(w * 0.4f, h * 0.45f), Offset(w * 0.62f, h * 0.43f), 1.5f * density, alpha = 0.6f * f * seg(p, 0.45f, 0.65f))
        },
    )
}

// ═════════════ gz-khaybar ═════════════
internal fun DrawScope.gzkhaybarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "gzkhaybar", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1.05f)),
        { daySky12(Qn.DayTop, Color(0xFFF0D8A8), ground, Color(0xFFA8926A)) },
        { p, f ->
            house12(Offset(w * 0.85f, ground), w * 0.1f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f)
            fort100(Offset(w * 0.15f, ground), w * 0.12f, f)
            val go = easeInOut(seg(p, 0.15f, 0.85f))
            army100(mix(w * 0.8f, w * 0.35f, go), mix(w * 0.95f, w * 0.55f, go), ground, 6, Jd100, f, walk = a.t * 2f)
            sirajMunir(Offset(mix(w * 0.88f, w * 0.45f, go), ground - h * 0.17f), w * 0.025f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.14f))
            tagAt(a.tag("gzkhaybar0", 0, 0), Offset(w * 0.85f, ground - h * 0.18f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzkhaybar0", 0, 1), Offset(w * 0.15f, ground - h * 0.22f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 8) fort100(Offset(w * (0.08f + 0.12f * k), ground - (k % 2) * h * 0.06f), w * 0.07f, f * seg(p, 0.05f * k, 0.12f + 0.05f * k))
            tagAt(a.tag("gzkhaybar1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            qIcon("scroll", Offset(w * 0.5f, h * 0.42f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.6f, 0.75f), a.t)
            tagAt(a.tag("gzkhaybar1", 0, 1), Offset(w * 0.5f, h * 0.53f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            kaaba29(Offset(w * 0.5f, ground), w * 0.1f, f)
            for (k in 0 until 10) {
                val ang = a.t * 0.5f + k * 0.63f
                person99(Offset(w * 0.5f + kotlin.math.cos(ang) * w * 0.25f, ground + kotlin.math.sin(ang) * h * 0.03f), h * 0.07f, Color(0xFFF2EEE4), f * seg(p, 0.1f, 0.3f))
            }
            qIcon("hourglass", Offset(w * 0.18f, h * 0.2f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("gzkhaybar2", 0, 1), Offset(w * 0.18f, h * 0.3f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
            tagAt(a.tag("gzkhaybar2", 0, 0), Offset(w * 0.5f, h * 0.38f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.5f * f)
            val give = easeInOut(seg(p, 0.35f, 0.8f))
            qIcon("scroll", Offset(mix(w * 0.3f, w * 0.7f, give), h * 0.45f), w * 0.1f, Color(0xFFEFE2C0), f, a.t)
            for (k in 0 until 2) person99(Offset(w * (0.22f + 0.06f * k), ground), h * 0.1f, Jd100, f)
            for (k in 0 until 3) person99(Offset(w * (0.68f + 0.06f * k), ground), h * 0.1f, Color(0xFF9A8A9A), f, arms = 0.8f * give)
            tagAt(a.tag("gzkhaybar3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzkhaybar3", 0, 1), Offset(w * 0.75f, ground - h * 0.2f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ gz-mutah ═════════════
internal fun DrawScope.gzmutahScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "gzmutah", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { dusk100(a, ground) },
        { p, f ->
            house12(Offset(w * 0.12f, ground), w * 0.1f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f)
            hill100(Offset(w * 0.85f, ground), w * 0.35f, h * 0.14f, Color(0xFF5A4A58), f)
            val go = easeInOut(seg(p, 0.1f, 0.9f))
            army100(mix(w * 0.15f, w * 0.45f, go), mix(w * 0.35f, w * 0.7f, go), ground, 7, Jd100, f, walk = a.t * 2f)
            tagAt(a.tag("gzmutah0", 0, 0), Offset(w * 0.12f, ground - h * 0.18f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzmutah0", 0, 1), Offset(w * 0.85f, ground - h * 0.32f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzmutah0", 0, 2), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.55f * f)
            plain97(Offset(w * 0.2f, h * 0.42f), w * 0.075f, a, "gzmutah1", 0, f * seg(p, 0.05f, 0.25f))
            close97(Offset(w * 0.5f, h * 0.42f), w * 0.075f, a, "gzmutah1", 1, 3, f * seg(p, 0.3f, 0.5f))
            plain97(Offset(w * 0.8f, h * 0.42f), w * 0.075f, a, "gzmutah1", 2, f * seg(p, 0.55f, 0.75f))
            for (k in 0 until 3) qIcon("star", Offset(w * (0.2f + 0.3f * k), h * 0.2f), w * 0.04f, Qn.GoldLight, f * seg(p, 0.75f + 0.05f * k, 0.9f), a.t)
        },
        { p, f ->
            val lift = easeOut(seg(p, 0.1f, 0.45f))
            qIcon95("flag", Offset(w * 0.5f, ground - h * (0.2f + 0.12f * lift)), w * 0.1f, Qn.Gold, f, a.t)
            army100(w * 0.3f, w * 0.7f, ground, 7, Jd100, f)
            plain97(Offset(w * 0.18f, h * 0.3f), w * 0.07f, a, "gzmutah2", 1, f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("gzmutah2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.6f * f)
            val c = Offset(w * 0.5f, h * 0.45f)
            val open = easeOut(seg(p, 0.3f, 0.7f))
            for (s in listOf(-1f, 1f)) {
                drawPath(Path().apply {
                    moveTo(c.x + s * w * 0.08f, c.y)
                    cubicTo(c.x + s * w * 0.2f, c.y - h * 0.2f * open, c.x + s * w * 0.35f, c.y - h * 0.15f * open, c.x + s * w * 0.38f * open, c.y + h * 0.02f)
                    quadraticBezierTo(c.x + s * w * 0.2f, c.y + h * 0.04f, c.x + s * w * 0.08f, c.y)
                }, Qn.GoldLight, alpha = 0.45f * f * open)
            }
            close97(c, w * 0.08f, a, "gzmutah3", 0, 3, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("gzmutah3", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ gz-fath ═════════════
internal fun DrawScope.gzfathScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "gzfath", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { daySky12(Qn.DayTop, Color(0xFFF2E0B4), ground, Color(0xFFB09868)) },
        { p, f ->
            army100(w * 0.05f, w * 0.38f, ground, 5, Em100, f)
            army100(w * 0.62f, w * 0.95f, ground, 5, Jd100, f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.45f), w * 0.08f, Color(0xFFEFE2C0), f * (1f - 0.7f * seg(p, 0.5f, 0.7f)), a.t)
            drawLine(Qn.Ember, Offset(w * 0.44f, h * 0.38f), Offset(w * 0.56f, h * 0.52f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("gzfath0", 0, 0), Offset(w * 0.2f, ground - h * 0.2f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("gzfath0", 0, 1), Offset(w * 0.8f, ground - h * 0.2f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("gzfath0", 0, 2), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            house12(Offset(w * 0.2f, ground), w * 0.16f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = seg(p, 0.1f, 0.3f))
            kaaba29(Offset(w * 0.5f, ground), w * 0.07f, f)
            house12(Offset(w * 0.8f, ground), w * 0.14f, Color(0xFFD8C098), Color(0xFF8A6A40), f, lit = seg(p, 0.55f, 0.75f))
            drawRect(Color(0xFF3A2A1C), Offset(w * 0.785f, ground - h * 0.06f), Size(w * 0.03f, h * 0.06f), alpha = f * seg(p, 0.6f, 0.75f))
            for (k in 0 until 3) qIcon("light", Offset(w * (0.2f + 0.3f * k), h * 0.3f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k), a.t)
            tagAt(a.tag("gzfath1", 0, 3), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("gzfath1", 0, 0), Offset(w * 0.2f, ground - h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzfath1", 0, 1), Offset(w * 0.5f, ground - h * 0.2f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzfath1", 0, 2), Offset(w * 0.8f, ground - h * 0.24f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            kaaba29(Offset(w * 0.5f, ground), w * 0.12f, f)
            for (k in 0 until 6) {
                val fall = easeInOut(seg(p, 0.15f + 0.07f * k, 0.35f + 0.07f * k))
                val x = w * (0.15f + 0.14f * k + if (k >= 3) 0.02f else -0.02f)
                drawRoundRect(Color(0xFF6A5A4A), Offset(x - w * 0.015f, ground - h * 0.1f * (1f - fall) - h * 0.02f * fall), Size(w * 0.03f + w * 0.05f * fall, h * 0.1f * (1f - fall) + h * 0.02f), CornerRadius(3f * density), alpha = f * (1f - 0.6f * fall))
            }
            for (k in 0 until 3) drawArc(Qn.GoldLight, -150f, 120f, false, Offset(w * 0.5f - w * 0.06f * (k + 1), h * 0.3f - h * 0.04f * (k + 1)), Size(w * 0.12f * (k + 1), h * 0.08f * (k + 1)), alpha = 0.5f * f * seg(p, 0.65f + 0.06f * k, 0.8f + 0.06f * k), style = Stroke(1.6f * density))
            tagAt(a.tag("gzfath2", 0, 1), Offset(w * 0.2f, h * 0.48f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("gzfath2", 0, 2), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.55f * f)
            for (k in 0 until 2) {
                val c = Offset(w * (0.32f + 0.36f * k), h * 0.62f)
                val fall = easeInOut(seg(p, 0.35f + 0.2f * k, 0.6f + 0.2f * k))
                drawRoundRect(Color(0xFF6A5A4A), Offset(c.x - w * 0.04f, c.y - h * 0.2f * (1f - fall)), Size(w * 0.08f, h * 0.2f * (1f - fall) + h * 0.02f), CornerRadius(4f * density), alpha = f * (1f - 0.5f * fall))
                tagAt(a.tag("gzfath3", 0, k), c + Offset(0f, h * 0.1f), f * seg(p, 0.05f + 0.2f * k, 0.2f + 0.2f * k), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ gz-hunayn ═════════════
internal fun DrawScope.gzhunaynScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "gzhunayn", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { dusk100(a, ground); hill100(Offset(w * 0.12f, ground), w * 0.4f, h * 0.16f, Color(0xFF4A3A40), 1f); hill100(Offset(w * 0.88f, ground), w * 0.4f, h * 0.16f, Color(0xFF4A3A40), 1f) },
        { p, f ->
            army100(w * 0.05f, w * 0.55f, ground, 16, Jd100, f * seg(p, 0f, 0.25f))
            army100(w * 0.68f, w * 0.95f, ground, 6, Em100, f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("gzhunayn0", 0, 0), Offset(w * 0.3f, h * 0.2f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("gzhunayn0", 0, 1), Offset(w * 0.8f, h * 0.32f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            val scat = easeInOut(seg(p, 0.45f, 0.85f))
            for (k in 0 until 14) {
                val base = w * (0.12f + 0.055f * k)
                val dir = if (k % 2 == 0) -1f else 1f
                person99(Offset(base + dir * scat * w * (0.05f + 0.01f * k), ground), h * 0.08f, Jd100, f * (1f - 0.4f * scat), walk = a.t * 3f * scat, lean = 0.2f * dir * scat)
            }
            for (k in 0 until 4) person99(Offset(w * (0.06f + 0.02f * k), ground - h * 0.14f), h * 0.05f, Em100, f * seg(p, 0.35f, 0.5f))
            for (k in 0 until 4) person99(Offset(w * (0.88f + 0.02f * k), ground - h * 0.14f), h * 0.05f, Em100, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("gzhunayn1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzhunayn1", 0, 1), Offset(w * 0.12f, h * 0.48f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzhunayn1", 0, 2), Offset(w * 0.5f, h * 0.48f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            val c = Offset(w * 0.5f, ground - h * 0.22f)
            sirajMunir(c, w * 0.045f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.14f))
            close97(Offset(w * 0.25f, h * 0.38f), w * 0.07f, a, "gzhunayn2", 1, 3, f * seg(p, 0.2f, 0.4f))
            val gather = easeInOut(seg(p, 0.4f, 0.9f))
            for (k in 0 until 10) {
                val from = if (k % 2 == 0) w * (0.02f + 0.04f * k) else w * (0.98f - 0.04f * k)
                person99(Offset(mix(from, w * (0.32f + 0.036f * k), gather), ground), h * 0.08f, Jd100, f, walk = a.t * 3f * (1f - gather))
            }
            tagAt(a.tag("gzhunayn2", 0, 2), Offset(w * 0.78f, h * 0.38f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.55f * f)
            army100(w * 0.06f, w * 0.42f, h * 0.7f, 10, Jd100, f * (1f - 0.6f * seg(p, 0.35f, 0.6f)))
            drawLine(Qn.Ember, Offset(w * 0.05f, h * 0.6f), Offset(w * 0.43f, h * 0.6f), 2.5f * density, alpha = f * seg(p, 0.35f, 0.5f))
            qIcon("heart", Offset(w * 0.72f, h * 0.5f), w * 0.11f, Qn.Jade, f * seg(p, 0.5f, 0.7f), a.t)
            softGlow(Offset(w * 0.72f, h * 0.5f), w * 0.12f, Qn.Jade, 0.4f * f * seg(p, 0.6f, 0.8f))
            tagAt(a.tag("gzhunayn3", 0, 0), Offset(w * 0.24f, h * 0.8f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("gzhunayn3", 0, 1), Offset(w * 0.72f, h * 0.68f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ gz-tabuk ═════════════
internal fun DrawScope.gztabukScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "gztabuk", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1.05f)),
        { daySky12(Color(0xFFE8B070), Color(0xFFF6DCA0), ground, Color(0xFFC0975E)); drawCircle(Color(0xFFFFF1C0), w * 0.06f, Offset(w * 0.82f, h * 0.16f)) },
        { p, f ->
            army100(w * 0.62f, w * 0.96f, ground, 9, Em100, f * seg(p, 0f, 0.25f))
            fqCoins(Offset(w * 0.25f, ground - h * 0.02f), w * 0.025f, 5, f * seg(p, 0.45f, 0.65f))
            tagAt(a.tag("gztabuk0", 0, 0), Offset(w * 0.78f, ground - h * 0.2f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("gztabuk0", 0, 1), Offset(w * 0.25f, ground - h * 0.16f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.5f * f)
            close97(Offset(w * 0.22f, h * 0.36f), w * 0.08f, a, "gztabuk1", 0, 0, f * seg(p, 0.05f, 0.25f))
            close97(Offset(w * 0.5f, h * 0.36f), w * 0.08f, a, "gztabuk1", 1, 2, f * seg(p, 0.3f, 0.5f))
            for (k in 0 until 3) person99(Offset(w * (0.72f + 0.06f * k), ground), h * 0.09f, Qn.Rose, f * seg(p, 0.55f, 0.7f))
            for (k in 0 until 3) fqCoins(Offset(w * (0.22f + 0.28f * k), h * 0.58f), w * 0.018f, 3 + k, f * seg(p, 0.15f + 0.25f * k, 0.3f + 0.25f * k), Qn.GoldLight)
            tagAt(a.tag("gztabuk1", 0, 2), Offset(w * 0.78f, ground - h * 0.18f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val go = easeInOut(seg(p, 0.05f, 0.9f))
            army100(mix(w * -0.3f, w * 0.1f, go), mix(w * 0.3f, w * 0.7f, go), ground, 18, Jd100, f, walk = a.t * 2f)
            for (k in 0 until 3) person99(Offset(w * (0.88f + 0.04f * k), ground), h * 0.08f, Color(0xFF8A8494), f * seg(p, 0.5f, 0.65f) * (1f - 0.5f * seg(p, 0.7f, 0.9f)), walk = a.t * 2f)
            tagAt(a.tag("gztabuk2", 0, 0), Offset(w * 0.4f, h * 0.2f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("gztabuk2", 0, 1), Offset(w * 0.9f, ground - h * 0.16f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val heat = 0.5f + 0.5f * sin(a.t * 2f)
            softGlow(Offset(w * 0.82f, h * 0.16f), w * 0.15f, Color(0xFFFFD890), 0.5f * f * heat)
            qIcon("drop", Offset(w * 0.32f, h * 0.42f), w * 0.08f, Color(0xFF8AB8D8), f * (1f - 0.6f * seg(p, 0.3f, 0.5f)), a.t)
            qIcon("wheat", Offset(w * 0.6f, h * 0.42f), w * 0.08f, Qn.Gold, f * (1f - 0.6f * seg(p, 0.45f, 0.65f)), a.t)
            army100(w * 0.12f, w * 0.88f, ground, 10, Jd100, f, walk = a.t)
            tagAt(a.tag("gztabuk3", 0, 2), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("gztabuk3", 0, 0), Offset(w * 0.32f, h * 0.55f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("gztabuk3", 0, 1), Offset(w * 0.6f, h * 0.55f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}
