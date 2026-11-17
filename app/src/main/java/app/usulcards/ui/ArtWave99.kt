package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 99 (2026-11-17): the six cards of «بناء المجتمع في المدينة» redrawn in the illustrated style (boards in wave 95).
 * Madinah in daylight among palms; the mosque of palm trunks and a roof of fronds; his ﷺ named light; companions in
 * roundels (Hamza, of his family, in colour; the rest plain); crowds are faceless figures.
 */

private val Palm99 = Color(0xFF6A4A30)

/** The first mosque: palm-trunk pillars, a roof of fronds, mud-brick walls; [build] 0..1 raises it. */
private fun DrawScope.masjid99(foot: Offset, s: Float, build: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val wall = seg(build, 0f, 0.5f)
    drawRect(Color(0xFFB89870), Offset(foot.x - s, foot.y - s * 0.4f * wall), Size(s * 2f, s * 0.4f * wall), alpha = alpha)
    for (k in 0 until 6) {
        val x = foot.x - s * 0.85f + k * s * 0.34f
        val up = seg(build, 0.3f + 0.05f * k, 0.6f + 0.05f * k)
        drawLine(Palm99, Offset(x, foot.y), Offset(x, foot.y - s * 0.7f * up), s * 0.06f, StrokeCap.Round, alpha = alpha)
    }
    val roof = seg(build, 0.75f, 1f)
    drawRect(Color(0xFF7A8A4A), Offset(foot.x - s * 1.05f, foot.y - s * 0.76f), Size(s * 2.1f * roof, s * 0.08f), alpha = alpha)
}

private fun DrawScope.madinah99(ground: Float, t: Float) {
    val w = size.width
    val h = size.height
    daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB09868))
    for (k in 0 until 5) palm12(Offset(w * (0.04f + 0.23f * k), ground), h * 0.22f, Palm99, Qn.Leaf, 0.85f, t)
}

// ═════════════ mb-masjid ═════════════
internal fun DrawScope.mbmasjidScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mbmasjid", listOf(Shot(Offset(w * 0.5f, h * 0.6f), 1f), Shot(Offset(w * 0.4f, h * 0.6f), 1.08f), Shot(Offset(w * 0.5f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.55f), 1f)),
        { madinah99(ground, a.t) },
        { p, f ->
            camel26(Offset(w * 0.5f, ground), h * 0.16f, Color(0xFF7A5A3A), f, a.t)
            sirajMunir(Offset(w * 0.5f, ground - h * 0.27f), w * 0.025f, f, a.t, named = false)
            drawCircle(Qn.GoldLight, w * 0.08f * easeOut(seg(p, 0.3f, 0.7f)), Offset(w * 0.5f, ground), alpha = 0.6f * f, style = Stroke(2f * density))
            tagAt(a.tag("mbmasjid0", 0, 1), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("mbmasjid0", 0, 0), Offset(w * 0.5f, ground + h * 0.06f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 2) figure(Offset(w * (0.24f + 0.07f * k), ground), h * 0.09f, Qn.Person, f)
            fqCoins(Offset(w * 0.6f, ground - h * 0.02f), w * 0.02f, 4, f * seg(p, 0.4f, 0.6f))
            sirajMunir(Offset(w * 0.7f, ground - h * 0.2f), w * 0.025f, f, a.t, named = false)
            tagAt(a.tag("mbmasjid1", 0, 0), Offset(w * 0.27f, ground - h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("mbmasjid1", 0, 1), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            masjid99(Offset(w * 0.5f, ground), w * 0.3f, easeInOut(seg(p, 0.05f, 0.9f)), f)
            val carry = ((a.t * 0.5f) % 1f)
            for (k in 0 until 4) {
                val x = w * (0.12f + 0.2f * k)
                figure(Offset(x, ground), h * 0.11f, Qn.Person, f, arms = 0.8f, walk = a.t * 2f)
                drawRoundRect(Color(0xFF9A7A5A), Offset(x - w * 0.015f, ground - h * 0.15f), Size(w * 0.03f, h * 0.025f), CornerRadius(2f * density), alpha = f)
            }
            sirajMunir(Offset(w * (0.2f + 0.6f * carry), ground - h * 0.3f), w * 0.025f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.12f))
            tagAt(a.tag("mbmasjid2", 0, 1), Offset(w * 0.82f, h * 0.36f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
        { p, f ->
            masjid99(Offset(w * 0.5f, ground), w * 0.32f, 1f, f)
            qIcon("key", Offset(w * 0.5f, h * 0.3f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.2f, 0.4f), a.t)
            softGlow(Offset(w * 0.5f, h * 0.3f), w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("mbmasjid3", 0, 0), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mb-muakhat ═════════════
internal fun DrawScope.mbmuakhatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mbmuakhat", listOf(Shot(Offset(w * 0.5f, h * 0.6f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { madinah99(ground, a.t); masjid99(Offset(w * 0.5f, ground), w * 0.2f, 1f, 0.6f) },
        { p, f ->
            val meet = easeInOut(seg(p, 0.1f, 0.6f))
            for (k in 0 until 4) {
                figure(Offset(mix(w * 0.05f, w * 0.38f, meet) - k * w * 0.05f, ground), h * 0.11f, Color(0xFFD8C8E8), f, walk = a.t * 2f * (1f - meet))
                figure(Offset(mix(w * 0.95f, w * 0.62f, meet) + k * w * 0.05f, ground), h * 0.11f, Color(0xFFC8E8D0), f, walk = a.t * 2f * (1f - meet))
            }
            for (k in 0 until 4) drawLine(Qn.GoldLight, Offset(mix(w * 0.05f, w * 0.38f, meet) - k * w * 0.05f, ground - h * 0.07f), Offset(mix(w * 0.95f, w * 0.62f, meet) + k * w * 0.05f, ground - h * 0.07f), 1.2f * density, alpha = 0.6f * f * seg(p, 0.6f, 0.8f))
            tagAt(a.tag("mbmuakhat0", 0, 2), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("mbmuakhat0", 0, 0), Offset(w * 0.2f, ground - h * 0.22f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("mbmuakhat0", 0, 1), Offset(w * 0.8f, ground - h * 0.22f), f * seg(p, 0.2f, 0.35f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.45f * f)
            house12(Offset(w * 0.5f, ground), w * 0.3f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.5f)
            for (k in 0 until 2) figure(Offset(w * (0.42f + 0.16f * k), ground), h * 0.13f, if (k == 0) Color(0xFFD8C8E8) else Color(0xFFC8E8D0), f)
            qIcon("heart", Offset(w * 0.5f, h * 0.36f), w * 0.08f, Qn.Rose, f * seg(p, 0.1f, 0.3f), a.t)
            fqCoins(Offset(w * 0.5f, ground - h * 0.01f), w * 0.02f, 3, f * seg(p, 0.45f, 0.65f))
            tagAt(a.tag("mbmuakhat1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("mbmuakhat1", 0, 1), Offset(w * 0.8f, ground - h * 0.12f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.55f * f)
            val move = easeInOut(seg(p, 0.3f, 0.75f))
            fqCoins(Offset(mix(w * 0.3f, w * 0.7f, move), ground - h * 0.1f), w * 0.025f, 3, f)
            house12(Offset(w * 0.75f, ground), w * 0.22f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = seg(p, 0.6f, 0.85f))
            for (k in 0 until 3) figure(Offset(w * (0.68f + 0.07f * k), ground), h * 0.09f, Qn.Person, f * seg(p, 0.6f, 0.8f))
            tagAt(a.tag("mbmuakhat2", 0, 0), Offset(w * 0.3f, h * 0.36f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("mbmuakhat2", 0, 1), Offset(w * 0.75f, ground - h * 0.32f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.6f * f)
            val c = Offset(w * 0.5f, h * 0.52f)
            val n = 12
            for (k in 0 until n) {
                val ang = 2f * PI.toFloat() * k / n + a.t * 0.08f
                val at = c + Offset(cos(ang) * w * 0.28f, sin(ang) * h * 0.3f)
                drawCircle(if (k % 2 == 0) Color(0xFFD8C8E8) else Color(0xFFC8E8D0), w * 0.02f, at, alpha = f * seg(p, 0.05f + 0.03f * k, 0.15f + 0.03f * k))
                val next = c + Offset(cos(ang + 2f * PI.toFloat() / n) * w * 0.28f, sin(ang + 2f * PI.toFloat() / n) * h * 0.3f)
                drawLine(Qn.GoldLight, at, next, 1.5f * density, alpha = f * seg(p, 0.5f, 0.8f))
            }
            masjid99(c + Offset(0f, h * 0.1f), w * 0.1f, 1f, f)
            tagAt(a.tag("mbmuakhat3", 0, 0), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ mb-wathiqa ═════════════
internal fun DrawScope.mbwathiqaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mbwathiqa", listOf(Shot(Offset(w * 0.5f, h * 0.6f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1.05f), Shot(Offset(w * 0.5f, h * 0.6f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { madinah99(ground, a.t) },
        { p, f ->
            for (k in 0 until 3) figure(Offset(w * (0.42f + 0.06f * k), ground), h * 0.11f, Color(0xFFC8E8D0), f)
            for (k in 0 until 5) figure(Offset(w * (0.05f + 0.06f * k), ground), h * 0.11f, Color(0xFF9A8A9A), f * seg(p, 0.3f, 0.5f))
            for (k in 0 until 5) figure(Offset(w * (0.7f + 0.06f * k), ground), h * 0.11f, Color(0xFF8A7A6A), f * seg(p, 0.5f, 0.7f))
            tagAt(a.tag("mbwathiqa0", 0, 0), Offset(w * 0.48f, ground - h * 0.22f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("mbwathiqa0", 0, 1), Offset(w * 0.17f, ground - h * 0.22f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
            tagAt(a.tag("mbwathiqa0", 0, 2), Offset(w * 0.82f, ground - h * 0.22f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.55f * f)
            val c = Offset(w * 0.5f, h * 0.48f)
            drawRoundRect(Color(0xFFEFE2C0), Offset(c.x - w * 0.2f, c.y - h * 0.26f), Size(w * 0.4f, h * 0.52f), CornerRadius(8f * density), alpha = f)
            for (k in 0 until 6) drawLine(Color(0xFF6A5030), Offset(c.x - w * 0.15f, c.y - h * 0.18f + k * h * 0.07f), Offset(c.x + w * 0.15f * seg(p, 0.05f + 0.1f * k, 0.15f + 0.1f * k), c.y - h * 0.18f + k * h * 0.07f), 1.6f * density, StrokeCap.Round, alpha = f)
            tagAt(a.tag("mbwathiqa1", 0, 0), c + Offset(0f, -h * 0.32f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("mbwathiqa1", 0, 1), Offset(w * 0.15f, h * 0.7f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            qIcon95("shield", Offset(w * 0.85f, h * 0.5f), w * 0.09f, Qn.Jade, f * seg(p, 0.65f, 0.8f), a.t)
            tagAt(a.tag("mbwathiqa1", 0, 2), Offset(w * 0.85f, h * 0.68f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            for (k in 0 until 6) house12(Offset(w * (0.08f + 0.17f * k), ground), w * 0.12f, if (k % 2 == 0) Color(0xFFE2CCA0) else Color(0xFFC8B898), Color(0xFF8A6A40), f, lit = seg(p, 0.3f, 0.7f))
            qIcon("check", Offset(w * 0.5f, h * 0.36f), w * 0.09f, Qn.Jade, f * seg(p, 0.5f, 0.7f), a.t)
            tagAt(a.tag("mbwathiqa2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.6f * f)
            val c = Offset(w * 0.5f, h * 0.5f)
            for (k in 0 until 5) drawCircle(Color(0xFF8A8494), w * 0.03f, c + Offset((k - 2) * w * 0.07f, 0f), alpha = f, style = Stroke(3f * density))
            val brk = easeInOut(seg(p, 0.35f, 0.7f))
            drawLine(Qn.Ember, c + Offset(-w * 0.22f, -h * 0.15f), c + Offset(w * 0.22f, h * 0.15f), 3.5f * density, StrokeCap.Round, alpha = f * brk)
            tagAt(a.tag("mbwathiqa3", 0, 0), c + Offset(0f, h * 0.22f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ mb-ism ═════════════
internal fun DrawScope.mbismScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mbism", listOf(Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.6f, h * 0.55f), 1.05f)),
        { madinah99(ground, a.t); masjid99(Offset(w * 0.5f, ground), w * 0.16f, 1f, 0.7f) },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.4f * f)
            val swap = easeInOut(seg(p, 0.35f, 0.7f))
            tagAt(a.tag("mbism0", 0, 0), Offset(w * 0.5f, mix(h * 0.32f, h * 0.2f, swap)), f * (1f - swap * 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            if (swap > 0.2f) drawLine(Qn.Ember, Offset(w * 0.42f, mix(h * 0.32f, h * 0.2f, swap)), Offset(w * 0.58f, mix(h * 0.32f, h * 0.2f, swap)), 2.5f * density, alpha = f * (1f - swap * 0.7f))
            tagAt(a.tag("mbism0", 0, 1), Offset(w * 0.5f, mix(h * 0.6f, h * 0.38f, swap)), f * swap, back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.4f * f)
            for (k in 0 until 2) {
                val on = seg(p, 0.15f + 0.3f * k, 0.35f + 0.3f * k)
                softGlow(Offset(w * (0.32f + 0.36f * k), h * 0.35f), w * 0.08f, Qn.GoldLight, 0.4f * f * on)
                tagAt(a.tag("mbism1", 0, k), Offset(w * (0.32f + 0.36f * k), h * 0.35f), f * on, ink = ArtInk.Gold)
            }
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.5f * f)
            kaaba29(Offset(w * 0.28f, h * 0.7f), w * 0.08f, f)
            masjid99(Offset(w * 0.72f, h * 0.7f), w * 0.1f, 1f, f)
            for (k in 0 until 2) drawCircle(Qn.Jade, w * 0.14f, Offset(w * (0.28f + 0.44f * k), h * 0.6f), alpha = 0.7f * f * seg(p, 0.2f + 0.3f * k, 0.4f + 0.3f * k), style = Stroke(2f * density))
            tagAt(a.tag("mbism2", 0, 2), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("mbism2", 0, 0), Offset(w * 0.28f, h * 0.82f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("mbism2", 0, 1), Offset(w * 0.72f, h * 0.82f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val c = Offset(w * 0.6f, ground - h * 0.1f)
            for (k in 0 until 10) {
                val u = ((a.t * 0.35f + k / 10f) % 1f)
                val from = Offset(if (k % 2 == 0) -w * 0.05f else w * 1.05f, h * (0.2f + 0.05f * (k % 5)))
                drawCircle(Qn.GoldLight, w * 0.007f, Offset(mix(from.x, c.x, u), mix(from.y, c.y, u)), alpha = f * seg(p, 0.1f, 0.3f) * (1f - u * 0.3f))
            }
            softGlow(c, w * 0.12f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("mbism3", 0, 0), Offset(w * 0.25f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("mbism3", 0, 1), c + Offset(0f, -h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ mb-sukkan ═════════════
internal fun DrawScope.mbsukkanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mbsukkan", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1.05f), Shot(Offset(w * 0.5f, h * 0.6f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { madinah99(ground, a.t) },
        { p, f ->
            house12(Offset(w * 0.3f, ground), w * 0.22f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.5f)
            val c = Offset(w * 0.62f, h * 0.45f)
            softGlow(c, w * 0.1f, Qn.GoldLight, 0.6f * f * seg(p, 0.1f, 0.4f))
            plain97(c, w * 0.08f, a, "mbsukkan0", 1, f * seg(p, 0.3f, 0.55f))
            for (k in 0 until 5) figure(Offset(w * (0.12f + 0.18f * k), ground), h * 0.09f, Qn.Person, f * seg(p, 0.5f, 0.7f), arms = 0.9f)
            tagAt(a.tag("mbsukkan0", 0, 0), Offset(w * 0.62f, h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.5f * f)
            plain97(Offset(w * 0.25f, h * 0.45f), w * 0.09f, a, "mbsukkan1", 0, f * seg(p, 0.05f, 0.25f))
            sirajMunir(Offset(w * 0.72f, h * 0.45f), w * 0.04f, f * seg(p, 0.2f, 0.4f), a.t, nameAt = Offset(w * 0.72f, h * 0.6f))
            drawOval(Color(0xFF8A5A2A), Offset(w * 0.48f, h * 0.44f), Size(w * 0.04f, h * 0.03f), alpha = f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("mbsukkan1", 0, 2), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            masjid99(Offset(w * 0.28f, ground), w * 0.16f, 1f, f)
            for (k in 0 until 5) house12(Offset(w * (0.55f + 0.09f * k), ground), w * 0.08f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.4f)
            val stay = seg(p, 0.5f, 0.8f)
            for (k in 0 until 4) figure(Offset(mix(w * (0.6f + 0.08f * k), w * (0.4f + 0.04f * k), 0.5f * seg(p, 0.1f, 0.4f) * (1f - stay)), ground), h * 0.09f, Qn.Person, f)
            tagAt(a.tag("mbsukkan2", 0, 2), Offset(w * 0.72f, h * 0.2f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("mbsukkan2", 0, 0), Offset(w * 0.28f, h * 0.38f), f * seg(p, 0.2f, 0.35f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("mbsukkan2", 0, 1), Offset(w * 0.72f, ground - h * 0.2f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.5f * f)
            for (k in 0 until 18) figure(Offset(w * (0.12f + 0.085f * (k % 9)), h * (0.6f + 0.18f * (k / 9))), h * 0.08f, Qn.Person, f * seg(p, 0.02f * k, 0.1f + 0.02f * k))
            drawRoundRect(Color(0xFFEFE2C0), Offset(w * 0.38f, h * 0.12f), Size(w * 0.24f, h * 0.28f), CornerRadius(6f * density), alpha = f * seg(p, 0.4f, 0.6f))
            for (k in 0 until 4) drawLine(Color(0xFF6A5030), Offset(w * 0.42f, h * (0.18f + 0.05f * k)), Offset(w * 0.58f, h * (0.18f + 0.05f * k)), 1.4f * density, alpha = f * seg(p, 0.5f + 0.08f * k, 0.6f + 0.08f * k))
            tagAt(a.tag("mbsukkan3", 0, 0), Offset(w * 0.5f, h * 0.46f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mb-idhn ═════════════
internal fun DrawScope.mbidhnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mbidhn", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1.05f), Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1f)),
        { nightSky(a.t, S97.Night, S97.NightLow, ground); dunes97(ground, S97.SandDark, Color(0xFF5A4630)) },
        { p, f ->
            for (k in 0 until 6) figure(Offset(w * (0.06f + 0.06f * k), ground), h * 0.11f, Color(0xFF6A5A6A), f, arms = 0.6f)
            masjid99(Offset(w * 0.75f, ground), w * 0.14f, 1f, f)
            for (k in 0 until 3) figure(Offset(w * (0.68f + 0.07f * k), ground), h * 0.1f, Color(0xFFC8E8D0), f)
            tagAt(a.tag("mbidhn0", 0, 2), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("mbidhn0", 0, 0), Offset(w * 0.22f, ground - h * 0.22f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("mbidhn0", 0, 1), Offset(w * 0.75f, ground - h * 0.32f), f * seg(p, 0.35f, 0.5f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.5f * f)
            val c = Offset(w * 0.5f, h * 0.48f)
            drawRoundRect(Color(0xFF1E5A4A), Offset(c.x - w * 0.14f, c.y - h * 0.2f), Size(w * 0.28f, h * 0.4f), CornerRadius(8f * density), alpha = f)
            drawRoundRect(Qn.Gold, Offset(c.x - w * 0.12f, c.y - h * 0.17f), Size(w * 0.24f, h * 0.34f), CornerRadius(6f * density), alpha = f, style = Stroke(2f * density))
            softGlow(c, w * 0.2f, Qn.GoldLight, 0.5f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("mbidhn1", 0, 0), c + Offset(0f, h * 0.3f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            val go = easeInOut(seg(p, 0.1f, 0.8f))
            qIcon95("flag", Offset(mix(w * 0.15f, w * 0.6f, go), ground - h * 0.25f), w * 0.08f, Qn.Gold, f, a.t)
            for (k in 0 until 6) figure(Offset(mix(w * 0.1f, w * 0.55f, go) - k * w * 0.04f, ground), h * 0.1f, Qn.Person, f, walk = a.t * 2f)
            close97(Offset(w * 0.8f, h * 0.36f), w * 0.08f, a, "mbidhn2", 1, 3, f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("mbidhn2", 0, 0), Offset(w * 0.4f, h * 0.18f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.5f * f)
            plain97(Offset(w * 0.3f, h * 0.45f), w * 0.08f, a, "mbidhn3", 0, f * seg(p, 0.05f, 0.25f))
            plain97(Offset(w * 0.7f, h * 0.45f), w * 0.08f, a, "mbidhn3", 1, f * seg(p, 0.25f, 0.45f) * (1f - 0.6f * seg(p, 0.55f, 0.8f)))
            tagAt(a.tag("mbidhn3", 0, 2), Offset(w * 0.7f, h * 0.7f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}
