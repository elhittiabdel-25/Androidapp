package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 105 (2026-11-17): the twenty tafsir cards of batch 19 (tz-*, Juz' Tabarak and Qad Sami'a) redrawn in the
 * illustrated style (boards in wave 94). Each beat is a small staged picture: the gathering that makes room, the
 * garden withheld from the poor and burnt, the book given in the right hand or the left, the fingerprint's whorls.
 * People are faceless figures; angels cool white lights; the jinn are never drawn (only what they heard); Nuh عليه
 * السلام is his name in a roundel; any line with the Name of Allah sits high in the frame.
 */

private val Fig105 = Color(0xFFD8D0C4)
private val Good105 = Color(0xFFC8E8D0)
private val Bad105 = Color(0xFFB8786A)

private fun DrawScope.veil105(alpha: Float, k: Float = 0.55f) = drawRect(Color(0xFF14102A), alpha = k * alpha)

private fun DrawScope.town105(a: ArtCtx, ground: Float) {
    val w = size.width
    daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB09868))
    for (k in 0 until 4) house12(Offset(w * (0.08f + 0.28f * k), ground), w * 0.1f, Color(0xFFE2CCA0), Color(0xFF8A6A40), 0.5f)
}

private fun DrawScope.garden105(ground: Float, n: Int, alpha: Float, burn: Float = 0f, t: Float = 0f) {
    val w = size.width
    val h = size.height
    for (k in 0 until n) {
        val leaf = lerpColor(Color(0xFF4A8A4A), Color(0xFF2A2420), burn)
        val fruit = lerpColor(Color(0xFFD8A040), Color(0xFF2A2420), burn)
        tree104(Offset(w * (0.1f + 0.8f * k / (n - 1).coerceAtLeast(1)), ground), h * 0.18f, leaf, fruit, alpha)
    }
    if (burn > 0.01f) for (k in 0 until 10) {
        val u = ((t * 0.4f + k * 0.1f) % 1f)
        drawCircle(Color(0xFF5A5050), w * 0.02f * (1f + u), Offset(w * (0.1f + 0.08f * k), ground - h * 0.2f - u * h * 0.2f), alpha = alpha * burn * (1f - u) * 0.6f)
    }
}

/** A book held out: [right] true lit jade on the right, false dark on the left. */
private fun DrawScope.held105(c: Offset, right: Boolean, alpha: Float) {
    val w = size.width
    val h = size.height
    val x = c.x + (if (right) 1f else -1f) * w * 0.07f
    person99(c, h * 0.13f, Fig105, alpha)
    drawRoundRect(if (right) Qn.Jade else Color(0xFF3A2E30), Offset(x - w * 0.03f, c.y - h * 0.11f), Size(w * 0.06f, h * 0.05f), CornerRadius(2f * density), alpha = alpha)
    softGlow(Offset(x, c.y - h * 0.09f), w * 0.06f, if (right) Qn.Jade else Qn.Ember, 0.5f * alpha)
}

// ═════════════ tz-mujadila ═════════════
internal fun DrawScope.tzmujadilaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzmujadila", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground) },
        { p, f ->
            for (k in 0 until 2) person99(Offset(w * (0.42f + 0.1f * k), ground), h * 0.11f, Bad105, f, lean = if (k == 0) 0.15f else -0.15f)
            for (k in 0 until 3) person99(Offset(w * (0.12f + 0.07f * k), ground), h * 0.1f, Good105, f, lean = -0.05f)
            for (k in 0 until 4) drawCircle(Qn.Ember, w * 0.006f, Offset(w * 0.47f + cos(a.t * 3f + k) * w * 0.03f, ground - h * 0.12f), alpha = f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("tzmujadila0", 0, 0), Offset(w * 0.47f, h * 0.36f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            qIcon("flame", Offset(w * 0.3f, h * 0.42f), w * 0.09f, Qn.Ember, f * seg(p, 0.05f, 0.25f), a.t)
            person99(Offset(w * 0.68f, h * 0.76f), h * 0.12f, Good105, f, lean = 0.2f * seg(p, 0.4f, 0.7f))
            qIcon("heart", Offset(w * 0.68f, h * 0.44f), w * 0.06f, Color(0xFF8A8494), f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("tzmujadila1", 0, 0), Offset(w * 0.3f, h * 0.58f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmujadila1", 0, 1), Offset(w * 0.68f, h * 0.3f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val open = easeInOut(seg(p, 0.2f, 0.6f))
            drawRoundRect(Color(0xFF8A6A48), Offset(w * 0.08f, ground - h * 0.02f), Size(w * 0.84f, h * 0.04f), CornerRadius(4f * density), alpha = f)
            for (k in 0 until 6) {
                val x = w * 0.5f + (k - 2.5f) * w * mix(0.06f, 0.12f, open)
                person99(Offset(x, ground), h * 0.09f, Good105, f)
            }
            person99(Offset(mix(w * 1.05f, w * 0.5f + 3.2f * w * 0.12f, seg(p, 0.55f, 0.85f)), ground), h * 0.09f, Fig105, f * seg(p, 0.5f, 0.6f), walk = a.t * 2f)
            tagAt(a.tag("tzmujadila2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmujadila2", 0, 1), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil105(f, 0.4f)
            for (k in 0 until 4) person99(Offset(w * (0.15f + 0.08f * k), ground), h * 0.09f, Good105, f)
            val grow = easeOut(seg(p, 0.35f, 0.75f))
            tree104(Offset(w * 0.72f, ground), h * 0.3f, Color(0xFF4A9A5A), Qn.GoldLight, f, 0.3f + 0.7f * grow)
            softGlow(Offset(w * 0.72f, ground - h * 0.3f), w * 0.2f * grow, Qn.Jade, 0.4f * f)
            tagAt(a.tag("tzmujadila3", 0, 0), Offset(w * 0.27f, ground - h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmujadila3", 0, 1), Offset(w * 0.72f, h * 0.14f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tz-hashr ═════════════
internal fun DrawScope.tzhashrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzhashr", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground) },
        { p, f ->
            drawRect(Color(0xFFC9A46A), Offset(0f, ground - h * 0.02f), Size(w, h), alpha = f)
            kaaba29(Offset(w * 0.85f, ground), w * 0.07f, f)
            val go = easeInOut(seg(p, 0.15f, 0.9f))
            for (k in 0 until 6) person99(Offset(mix(w * 0.8f, w * 0.25f, go) + k * w * 0.04f, ground), h * 0.09f, Color(0xFFD8C8E8), f, walk = a.t * 2f)
            tagAt(a.tag("tzhashr0", 0, 0), Offset(w * 0.85f, ground - h * 0.22f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzhashr0", 0, 1), Offset(w * 0.4f, h * 0.3f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            house12(Offset(w * 0.5f, ground), w * 0.3f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.6f)
            for (k in 0 until 3) person99(Offset(w * (0.38f + 0.05f * k), ground), h * 0.1f, Good105, f, arms = 0.7f)
            for (k in 0 until 3) person99(Offset(w * (0.56f + 0.05f * k), ground), h * 0.1f, Color(0xFFD8C8E8), f)
            fqCoins(Offset(w * 0.5f, ground - h * 0.01f), w * 0.02f, 4, f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("tzhashr1", 0, 0), Offset(w * 0.2f, h * 0.36f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("tzhashr1", 0, 1), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            tagAt(a.tag("tzhashr1", 0, 2), Offset(w * 0.8f, h * 0.36f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            val give = easeInOut(seg(p, 0.3f, 0.7f))
            person99(Offset(w * 0.3f, h * 0.8f), h * 0.12f, Good105, f, arms = 0.8f * give)
            person99(Offset(w * 0.7f, h * 0.8f), h * 0.12f, Color(0xFFD8C8E8), f)
            qIcon("cup", Offset(mix(w * 0.34f, w * 0.64f, give), h * 0.62f), w * 0.05f, Qn.GoldLight, f, a.t)
            qIcon("heart", Offset(w * 0.5f, h * 0.36f), w * 0.08f, Qn.Rose, f * seg(p, 0.6f, 0.8f), a.t)
            tagAt(a.tag("tzhashr2", 0, 1), Offset(w * 0.3f, h * 0.52f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzhashr2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f, 0.4f)
            for (k in 0 until 5) qIcon("star", Offset(w * (0.2f + 0.15f * k), h * 0.2f), w * 0.04f, Qn.GoldLight, f * seg(p, 0.4f + 0.05f * k, 0.55f + 0.05f * k), a.t)
            for (k in 0 until 6) person99(Offset(w * (0.2f + 0.12f * k), ground), h * 0.09f, Good105, f, arms = 0.9f * seg(p, 0.2f, 0.4f))
            tagAt(a.tag("tzhashr3", 0, 0), Offset(w * 0.5f, ground - h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzhashr3", 0, 1), Offset(w * 0.5f, h * 0.48f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("tzhashr3", 0, 2), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tz-mumtahana ═════════════
internal fun DrawScope.tzmumtahanaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzmumtahana", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground) },
        { p, f ->
            veil105(f)
            person99(Offset(w * 0.3f, h * 0.8f), h * 0.12f, Good105, f)
            person99(Offset(w * 0.7f, h * 0.8f), h * 0.12f, Bad105, f)
            qIcon("chain", Offset(w * 0.5f, h * 0.6f), w * 0.08f, Qn.Ember, f * seg(p, 0.1f, 0.3f) * (1f - 0.5f * seg(p, 0.5f, 0.7f)), a.t)
            drawLine(Qn.Ember, Offset(w * 0.42f, h * 0.52f), Offset(w * 0.58f, h * 0.68f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("tzmumtahana0", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.2f, 0.35f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 3) house12(Offset(w * (0.2f + 0.3f * k), ground), w * 0.14f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = seg(p, 0.15f + 0.2f * k, 0.3f + 0.2f * k))
            val icons = listOf("heart", "house", "cup")
            for (k in 0 until 3) {
                qIcon(icons[k], Offset(w * (0.2f + 0.3f * k), h * 0.42f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.15f + 0.2f * k, 0.3f + 0.2f * k), a.t)
                tagAt(a.tag("tzmumtahana1", 0, k), Offset(w * (0.2f + 0.3f * k), h * 0.52f), f * seg(p, 0.15f + 0.2f * k, 0.3f + 0.2f * k), ink = ArtInk.Ivory)
            }
            tagAt(a.tag("tzmumtahana1", 0, 3), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f)
            balance12(Offset(w * 0.5f, h * 0.38f), w * 0.1f, 0f, Qn.GoldLight, f * seg(p, 0f, 0.15f))
            tagAt(a.tag("tzmumtahana2", 0, 3), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            val icons = listOf("hands", "key", "coin")
            for (k in 0 until 3) {
                qIcon(icons[k], Offset(w * (0.2f + 0.3f * k), h * 0.62f), w * 0.07f, Qn.Jade, f * seg(p, 0.2f + 0.2f * k, 0.35f + 0.2f * k), a.t)
                tagAt(a.tag("tzmumtahana2", 0, k), Offset(w * (0.2f + 0.3f * k), h * 0.75f), f * seg(p, 0.2f + 0.2f * k, 0.35f + 0.2f * k), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            val turn = easeInOut(seg(p, 0.3f, 0.75f))
            person99(Offset(w * 0.3f, ground), h * 0.11f, Good105, f)
            person99(Offset(w * 0.7f, ground), h * 0.11f, lerpColor(Bad105, Good105, turn), f)
            qIcon("heart", Offset(w * 0.5f, h * 0.5f), w * 0.08f, Qn.Rose, f * turn, a.t)
            tagAt(a.tag("tzmumtahana3", 0, 0), Offset(w * 0.7f, ground - h * 0.22f), f * seg(p, 0.05f, 0.2f) * (1f - 0.6f * turn), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmumtahana3", 0, 1), Offset(w * 0.5f, h * 0.34f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tz-saff ═════════════
internal fun DrawScope.tzsaffScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzsaff", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground) },
        { p, f ->
            for (k in 0 until 4) person99(Offset(w * (0.2f + 0.08f * k), ground), h * 0.1f, Good105, f, arms = 0.4f)
            qIcon("heart", Offset(w * 0.7f, h * 0.42f), w * 0.09f, Qn.Rose, f * seg(p, 0.2f, 0.4f), a.t)
            softGlow(Offset(w * 0.7f, h * 0.42f), w * 0.1f, Qn.Rose, 0.4f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("tzsaff0", 0, 0), Offset(w * 0.7f, h * 0.58f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("star", Offset(w * 0.5f, h * 0.24f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 6) {
                val back = k >= 4
                person99(Offset(w * (0.2f + 0.1f * k) + if (back) w * 0.05f * seg(p, 0.4f, 0.7f) else 0f, ground), h * 0.1f, if (back) Color(0xFF8A8494) else Good105, f, lean = if (back) -0.15f * seg(p, 0.4f, 0.7f) else 0f)
            }
            tagAt(a.tag("tzsaff1", 0, 0), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("tzsaff1", 0, 1), Offset(w * 0.75f, ground - h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            person99(Offset(w * 0.5f, h * 0.82f), h * 0.14f, Fig105, f)
            qIcon("lips", Offset(w * 0.3f, h * 0.4f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("hands", Offset(w * 0.7f, h * 0.4f), w * 0.08f, Color(0xFF6A6474), f * seg(p, 0.35f, 0.5f), a.t)
            drawLine(Qn.Ember, Offset(w * 0.62f, h * 0.32f), Offset(w * 0.78f, h * 0.48f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("tzsaff2", 0, 0), Offset(w * 0.3f, h * 0.54f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzsaff2", 0, 1), Offset(w * 0.7f, h * 0.54f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            softGlow(Offset(w * 0.5f, h * 0.2f), w * 0.15f, Qn.GoldLight, 0.6f * f)
            tagAt(a.tag("tzsaff3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            val tilt = easeInOut(seg(p, 0.35f, 0.7f))
            qIcon("heart", Offset(w * 0.5f + w * 0.2f * tilt, h * 0.6f + h * 0.05f * tilt), w * 0.09f, lerpColor(Qn.Rose, Color(0xFF5A4A50), tilt), f, a.t)
            tagAt(a.tag("tzsaff3", 0, 1), Offset(w * 0.7f, h * 0.78f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tz-jumua ═════════════
internal fun DrawScope.tzjumuaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    val mosque = Offset(w * 0.72f, ground)
    film98(a, "tzjumua", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground); drawRect(Color(0xFFD8C8A0), Offset(mosque.x - w * 0.16f, mosque.y - h * 0.2f), Size(w * 0.32f, h * 0.2f)); drawRect(Color(0xFFD8C8A0), Offset(mosque.x + w * 0.12f, mosque.y - h * 0.38f), Size(w * 0.04f, h * 0.38f)) },
        { p, f ->
            for (k in 0 until 3) drawArc(Qn.GoldLight, -60f, 120f, false, Offset(mosque.x + w * 0.14f - w * 0.04f * (k + 1), mosque.y - h * 0.4f - h * 0.04f * (k + 1)), Size(w * 0.08f * (k + 1), h * 0.08f * (k + 1)), alpha = 0.5f * f * seg(p, 0.05f + 0.06f * k, 0.2f + 0.06f * k), style = Stroke(1.4f * density))
            val go = easeInOut(seg(p, 0.3f, 0.9f))
            for (k in 0 until 6) person99(Offset(mix(w * 0.05f, w * 0.5f, go) - k * w * 0.05f, ground), h * 0.09f, Fig105, f, walk = a.t * 2f)
            tagAt(a.tag("tzjumua0", 0, 1), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzjumua0", 0, 0), Offset(mosque.x, mosque.y - h * 0.26f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            for (k in 0 until 2) drawRoundRect(Color(0xFF8A6A48), Offset(w * (0.08f + 0.22f * k), ground - h * 0.08f), Size(w * 0.16f, h * 0.08f), CornerRadius(3f * density), alpha = f)
            fqCoins(Offset(w * 0.24f, ground - h * 0.1f), w * 0.02f, 4, f * (1f - 0.7f * seg(p, 0.3f, 0.5f)))
            drawLine(Qn.Ember, Offset(w * 0.08f, ground - h * 0.18f), Offset(w * 0.4f, ground - h * 0.02f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("tzjumua1", 0, 0), Offset(w * 0.15f, h * 0.36f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tzjumua1", 0, 1), Offset(w * 0.38f, h * 0.48f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            val out = easeInOut(seg(p, 0.4f, 0.9f))
            for (k in 0 until 6) person99(Offset(mix(w * 0.6f, w * (0.05f + 0.1f * k), out), ground), h * 0.09f, Fig105, f, walk = a.t * 2f * out)
            tagAt(a.tag("tzjumua2", 0, 0), Offset(mosque.x, mosque.y - h * 0.26f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            fqCoins(Offset(w * 0.25f, ground - h * 0.12f), w * 0.02f, 3, f * seg(p, 0.7f, 0.85f))
            tagAt(a.tag("tzjumua2", 0, 1), Offset(w * 0.25f, h * 0.4f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil105(f)
            for (k in 0 until 12) {
                val ang = 2f * PI.toFloat() * k / 12f + a.t * 0.3f
                drawCircle(Qn.GoldLight, w * 0.008f, Offset(w * 0.35f + cos(ang) * w * 0.12f, h * 0.45f + sin(ang) * w * 0.12f), alpha = f * seg(p, 0.05f + 0.02f * k, 0.15f + 0.02f * k))
            }
            qIcon("lips", Offset(w * 0.35f, h * 0.45f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("heart", Offset(w * 0.72f, h * 0.45f), w * 0.09f, Qn.Rose, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("tzjumua3", 0, 0), Offset(w * 0.35f, h * 0.66f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("tzjumua3", 0, 1), Offset(w * 0.72f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tz-munafiqun ═════════════
internal fun DrawScope.tzmunafiqunScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzmunafiqun", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground) },
        { p, f ->
            person99(Offset(w * 0.5f, ground), h * 0.12f, Fig105, f, lean = 0.15f)
            fqCoins(Offset(w * 0.32f, ground - h * 0.01f), w * 0.025f, 6, f * seg(p, 0.05f, 0.2f))
            for (k in 0 until 2) person99(Offset(w * (0.66f + 0.07f * k), ground), h * 0.06f, Color(0xFFE8D8B8), f * seg(p, 0.2f, 0.35f))
            softGlow(Offset(w * 0.5f, h * 0.2f), w * 0.1f, Qn.GoldLight, 0.4f * f * (1f - 0.7f * seg(p, 0.5f, 0.8f)))
            tagAt(a.tag("tzmunafiqun0", 0, 0), Offset(w * 0.32f, ground - h * 0.12f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmunafiqun0", 0, 1), Offset(w * 0.7f, ground - h * 0.14f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmunafiqun0", 0, 2), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            qIcon("mat", Offset(w * 0.3f, h * 0.45f), w * 0.09f, Qn.Jade, f * seg(p, 0.05f, 0.2f), a.t)
            openBook21(Offset(w * 0.7f, h * 0.45f), w * 0.16f, T104.Page, T104.Rule, f * seg(p, 0.35f, 0.5f), 0.4f)
            tagAt(a.tag("tzmunafiqun1", 0, 0), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmunafiqun1", 0, 1), Offset(w * 0.7f, h * 0.6f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            val tilt = 0.35f * easeInOut(seg(p, 0.2f, 0.6f))
            balance12(Offset(w * 0.5f, h * 0.66f), w * 0.15f, tilt, Qn.GoldLight, f)
            tagAt(a.tag("tzmunafiqun2", 0, 0), Offset(w * 0.3f, h * 0.38f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmunafiqun2", 0, 1), Offset(w * 0.7f, h * 0.38f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmunafiqun2", 0, 2), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            val give = easeInOut(seg(p, 0.1f, 0.5f))
            fqCoins(Offset(mix(w * 0.3f, w * 0.5f, give), h * 0.62f), w * 0.022f, 4, f)
            person99(Offset(w * 0.62f, h * 0.8f), h * 0.1f, Color(0xFF9A8A80), f)
            qIcon("hourglass", Offset(w * 0.8f, h * 0.36f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("tzmunafiqun3", 0, 0), Offset(w * 0.3f, h * 0.48f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmunafiqun3", 0, 1), Offset(w * 0.8f, h * 0.5f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tz-taghabun ═════════════
internal fun DrawScope.tztaghabunScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tztaghabun", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground) },
        { p, f ->
            veil105(f, 0.4f)
            qIcon("cloud", Offset(w * 0.35f, h * 0.3f), w * 0.12f, Color(0xFF6A6474), f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 8) drawLine(T104.Water, Offset(w * (0.25f + 0.03f * k), h * 0.38f + ((a.t + k * 0.3f) % 1f) * h * 0.3f), Offset(w * (0.25f + 0.03f * k), h * 0.42f + ((a.t + k * 0.3f) % 1f) * h * 0.3f), 1.5f * density, alpha = f * seg(p, 0.15f, 0.3f))
            qIcon("pen", Offset(w * 0.75f, h * 0.24f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("tztaghabun0", 0, 0), Offset(w * 0.35f, h * 0.16f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tztaghabun0", 0, 1), Offset(w * 0.75f, h * 0.12f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f, 0.4f)
            qIcon("cloud", Offset(w * 0.5f, h * 0.2f), w * 0.12f, Color(0xFF6A6474), f * (1f - 0.5f * seg(p, 0.4f, 0.7f)), a.t)
            person99(Offset(w * 0.5f, ground), h * 0.12f, Fig105, f)
            qIcon("heart", Offset(w * 0.5f, h * 0.55f), w * 0.07f, Qn.Jade, f * seg(p, 0.35f, 0.55f), a.t)
            softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.1f, Qn.Jade, 0.5f * f * seg(p, 0.45f, 0.75f))
            tagAt(a.tag("tztaghabun1", 0, 0), Offset(w * 0.78f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tztaghabun1", 0, 1), Offset(w * 0.75f, h * 0.55f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil105(f)
            pray12(Offset(w * 0.5f, h * 0.8f), h * 0.12f, Fig105, f * seg(p, 0.05f, 0.25f))
            qIcon("hands", Offset(w * 0.5f, h * 0.42f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("tztaghabun2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f)
            fqCoins(Offset(w * 0.22f, h * 0.62f), w * 0.022f, 5, f * seg(p, 0.05f, 0.2f))
            for (k in 0 until 2) person99(Offset(w * (0.75f + 0.06f * k), h * 0.68f), h * 0.07f, Color(0xFFE8D8B8), f * seg(p, 0.25f, 0.4f))
            balance12(Offset(w * 0.5f, h * 0.5f), w * 0.1f, 0.2f * sin(a.t * 1.5f), Qn.GoldLight, f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("tztaghabun3", 0, 0), Offset(w * 0.22f, h * 0.76f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tztaghabun3", 0, 1), Offset(w * 0.78f, h * 0.78f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
            tagAt(a.tag("tztaghabun3", 0, 2), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tz-talaq ═════════════
internal fun DrawScope.tztalaqScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tztalaq", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground) },
        { p, f ->
            veil105(f)
            qIcon("drop", Offset(w * 0.3f, h * 0.42f), w * 0.08f, Color(0xFFBFE0F0), f * seg(p, 0.05f, 0.2f), a.t)
            years102(Offset(w * 0.7f, h * 0.45f), 3, seg(p, 0.45f, 0.65f), f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("tztalaq0", 0, 0), Offset(w * 0.3f, h * 0.58f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tztalaq0", 0, 1), Offset(w * 0.7f, h * 0.58f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f)
            qIcon("hourglass", Offset(w * 0.5f, h * 0.42f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            val run = seg(p, 0.25f, 0.9f)
            drawLine(Qn.GoldLight, Offset(w * 0.2f, h * 0.66f), Offset(w * (0.2f + 0.6f * run), h * 0.66f), 2.5f * density, StrokeCap.Round, alpha = f)
            tagAt(a.tag("tztalaq1", 0, 0), Offset(w * 0.2f, h * 0.74f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tztalaq1", 0, 1), Offset(w * 0.8f, h * 0.74f), f * seg(p, 0.8f, 0.92f), ink = ArtInk.Gold)
        },
        { p, f ->
            house12(Offset(w * 0.5f, ground), w * 0.28f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.6f)
            softGlow(Offset(w * 0.5f, ground - h * 0.1f), w * 0.15f, Qn.GoldLight, 0.4f * f * seg(p, 0.2f, 0.5f))
            tagAt(a.tag("tztalaq2", 0, 0), Offset(w * 0.5f, ground - h * 0.32f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            qIcon("hourglass", Offset(w * 0.82f, h * 0.3f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("tztalaq2", 0, 1), Offset(w * 0.82f, h * 0.42f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.2f), w * 0.09f, T104.Page, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tztalaq3", 0, 0), Offset(w * 0.5f, h * 0.08f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            drawRoundRect(Color(0xFF3A3448), Offset(w * 0.3f, h * 0.45f), Size(w * 0.4f, h * 0.35f), CornerRadius(8f * density), alpha = f)
            val open = easeInOut(seg(p, 0.4f, 0.75f))
            drawRoundRect(Color(0xFFFFE8B0), Offset(w * 0.45f, h * 0.55f), Size(w * 0.1f * open, h * 0.25f), CornerRadius(4f * density), alpha = f)
            softGlow(Offset(w * 0.5f, h * 0.67f), w * 0.12f * open, Qn.GoldLight, 0.6f * f)
            tagAt(a.tag("tztalaq3", 0, 1), Offset(w * 0.5f, h * 0.88f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tz-tahrim ═════════════
internal fun DrawScope.tztahrimScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tztahrim", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground) },
        { p, f ->
            veil105(f, 0.4f)
            person99(Offset(w * 0.4f, ground), h * 0.12f, Fig105, f, arms = 0.8f * seg(p, 0.3f, 0.5f))
            for (k in 0 until 2) person99(Offset(w * (0.5f + 0.06f * k), ground), h * 0.06f, Color(0xFFE8D8B8), f)
            drawArc(Qn.Jade, 180f, 180f, false, Offset(w * 0.3f, ground - h * 0.24f), Size(w * 0.36f, h * 0.3f), alpha = 0.7f * f * seg(p, 0.35f, 0.55f), style = Stroke(3f * density))
            qIcon("flame", Offset(w * 0.85f, ground - h * 0.08f), w * 0.08f, Qn.Ember, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tztahrim0", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
            tagAt(a.tag("tztahrim0", 0, 1), Offset(w * 0.85f, h * 0.56f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            house12(Offset(w * 0.5f, ground), w * 0.3f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.6f)
            openBook21(Offset(w * 0.5f, h * 0.36f), w * 0.16f, T104.Page, T104.Rule, f * seg(p, 0.05f, 0.2f), 0.5f)
            for (k in 0 until 3) person99(Offset(w * (0.4f + 0.1f * k), ground), h * 0.06f, Color(0xFFE8D8B8), f * seg(p, 0.3f + 0.1f * k, 0.45f + 0.1f * k))
            tagAt(a.tag("tztahrim1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("tztahrim1", 0, 1), Offset(w * 0.82f, ground - h * 0.12f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            for (k in 0 until 4) {
                val c = Offset(w * 0.5f, h * (0.24f + 0.16f * k))
                drawLine(Qn.GoldLight, Offset(w * 0.3f, c.y + h * 0.05f), Offset(w * 0.7f, c.y + h * 0.05f), 1.2f * density, alpha = 0.4f * f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k))
                tagAt(a.tag("tztahrim2", 0, k), c, f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k), ink = if (k == 3) ArtInk.Gold else ArtInk.Ivory)
            }
        },
        { p, f ->
            veil105(f)
            qIcon("crown", Offset(w * 0.25f, h * 0.45f), w * 0.09f, Color(0xFF6A5A48), f * seg(p, 0.05f, 0.2f), a.t)
            drawLine(Qn.Ember, Offset(w * 0.17f, h * 0.37f), Offset(w * 0.33f, h * 0.53f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.25f, 0.4f))
            tree104(Offset(w * 0.72f, h * 0.75f), h * 0.25f, Color(0xFF4A9A5A), Qn.GoldLight, f, easeOut(seg(p, 0.4f, 0.75f)))
            softGlow(Offset(w * 0.72f, h * 0.5f), w * 0.18f, Qn.Jade, 0.4f * f * seg(p, 0.5f, 0.8f))
            tagAt(a.tag("tztahrim3", 0, 0), Offset(w * 0.25f, h * 0.6f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tztahrim3", 0, 1), Offset(w * 0.72f, h * 0.2f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tz-mulk ═════════════
internal fun DrawScope.tzmulkScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.88f
    film98(a, "tzmulk", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { nightSky(a.t, S97.Night, S97.NightLow, ground, 1f); drawRect(Color(0xFF1A1610), Offset(0f, ground), Size(w, h - ground)) },
        { p, f ->
            for (k in 0 until 24) {
                val ang = 2f * PI.toFloat() * k / 24f
                drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.18f) + Offset(cos(ang), sin(ang)) * (w * 0.08f), Offset(w * 0.5f, h * 0.18f) + Offset(cos(ang), sin(ang)) * (w * (0.08f + 0.3f * seg(p, 0.2f, 0.7f))), 1f * density, alpha = 0.4f * f)
            }
            tagAt(a.tag("tzmulk0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            for (k in 0 until 10) tree104(Offset(w * (0.05f + 0.1f * k), ground), h * 0.08f, Color(0xFF3A6A4A), Qn.GoldLight, f * seg(p, 0.4f + 0.03f * k, 0.55f + 0.03f * k))
        },
        { p, f ->
            veil105(f)
            qIcon("bed", Offset(w * 0.3f, h * 0.5f), w * 0.09f, Color(0xFF8A8494), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("sprout", Offset(w * 0.7f, h * 0.5f), w * 0.09f, Qn.Jade, f * seg(p, 0.4f, 0.55f), a.t)
            drawCircle(Color(0xFFE8F0FF), w * 0.015f, Offset(mix(w * 0.3f, w * 0.3f, 0f), h * 0.36f - h * 0.1f * seg(p, 0.15f, 0.4f)), alpha = f * seg(p, 0.15f, 0.25f) * (1f - seg(p, 0.35f, 0.45f)))
            tagAt(a.tag("tzmulk1", 0, 0), Offset(w * 0.3f, h * 0.66f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmulk1", 0, 1), Offset(w * 0.7f, h * 0.66f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil105(f)
            balance12(Offset(w * 0.4f, h * 0.62f), w * 0.12f, 0.2f * sin(a.t), Qn.GoldLight, f * seg(p, 0.05f, 0.2f))
            qIcon("star", Offset(w * 0.75f, h * 0.36f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.45f, 0.6f), a.t)
            softGlow(Offset(w * 0.75f, h * 0.36f), w * 0.12f, Qn.GoldLight, 0.5f * f * seg(p, 0.55f, 0.85f))
            tagAt(a.tag("tzmulk2", 0, 0), Offset(w * 0.4f, h * 0.74f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmulk2", 0, 1), Offset(w * 0.75f, h * 0.5f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            plain97(Offset(w * 0.15f, h * 0.15f), w * 0.06f, a, "tzmulk3", 3, f * seg(p, 0f, 0.15f))
            for (k in 0 until 3) {
                val c = Offset(w * (0.22f + 0.28f * k), h * 0.42f)
                val on = seg(p, 0.15f + 0.25f * k, 0.3f + 0.25f * k)
                when (k) {
                    0 -> for (j in 0 until 5) drawCircle(Color(0xFFFFF4D0), w * 0.008f, c + Offset(cos(j * 1.3f) * w * 0.06f, sin(j * 1.3f) * w * 0.05f), alpha = f * on)
                    1 -> drawLine(Qn.Ember, c + Offset(-w * 0.06f, -h * 0.06f), c + Offset(w * 0.04f, h * 0.04f), 2.5f * density, StrokeCap.Round, alpha = f * on)
                    else -> { drawCircle(Color(0xFFFFF4D0), w * 0.012f, c, alpha = f * on); drawLine(Qn.GoldLight, c, c + Offset(0f, h * 0.38f), 1.2f * density, alpha = 0.5f * f * on) }
                }
                tagAt(a.tag("tzmulk3", 0, k), c + Offset(0f, h * 0.14f), f * on, ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ tz-qalam ═════════════
internal fun DrawScope.tzqalamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzqalam", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { daySky12(Qn.DayTop, Color(0xFFF0E4C0), ground, Color(0xFF8A7A50)) },
        { p, f ->
            garden105(ground, 5, f)
            for (k in 0 until 3) person99(Offset(w * (0.82f + 0.05f * k), ground), h * 0.08f, Color(0xFF9A8A80), f * seg(p, 0.3f, 0.45f))
            drawLine(Qn.Ember, Offset(w * 0.76f, ground - h * 0.2f), Offset(w * 0.76f, ground), 3f * density, alpha = f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("tzqalam0", 0, 0), Offset(w * 0.4f, h * 0.2f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("tzqalam0", 0, 1), Offset(w * 0.87f, ground - h * 0.18f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            garden105(ground, 5, f * 0.5f)
            for (k in 0 until 3) person99(Offset(w * (0.38f + 0.1f * k), ground), h * 0.1f, Fig105, f, lean = if (k == 1) 0.1f else 0f)
            drawCircle(Qn.GoldLight, w * 0.03f, Offset(w * 0.48f, ground - h * 0.16f), alpha = 0.8f * f * seg(p, 0.1f, 0.3f), style = Stroke(1.5f * density))
            balance12(Offset(w * 0.75f, h * 0.4f), w * 0.08f, 0.3f, Qn.Ember, f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("tzqalam1", 0, 0), Offset(w * 0.48f, h * 0.36f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("tzqalam1", 0, 1), Offset(w * 0.75f, h * 0.24f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            val burn = easeInOut(seg(p, 0.2f, 0.7f))
            drawRect(Color(0xFF14102A), alpha = 0.5f * f * burn)
            garden105(ground, 5, f, burn, a.t)
            qIcon("chain", Offset(w * 0.5f, h * 0.2f), w * 0.07f, Qn.Ember, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tzqalam2", 0, 0), Offset(w * 0.5f, h * 0.32f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tzqalam2", 0, 1), Offset(w * 0.5f, ground + h * 0.06f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            garden105(ground, 5, f * 0.4f, 1f, a.t)
            for (k in 0 until 3) pray12(Offset(w * (0.35f + 0.15f * k), ground), h * 0.1f, Fig105, f)
            qIcon("hands", Offset(w * 0.5f, h * 0.4f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            softGlow(Offset(w * 0.5f, h * 0.2f), w * 0.15f, Qn.GoldLight, 0.4f * f * seg(p, 0.5f, 0.8f))
            tagAt(a.tag("tzqalam3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tz-haqqa ═════════════
internal fun DrawScope.tzhaqqaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzhaqqa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { drawRect(Brush.verticalGradient(listOf(Color(0xFF1A1830), Color(0xFF4A3A40)))); drawRect(Color(0xFF2A2420), Offset(0f, ground), Size(w, h - ground)) },
        { p, f ->
            for (k in 0 until 20) person99(Offset(w * (0.05f + 0.047f * k), ground + (k % 2) * h * 0.02f), h * 0.06f, Color(0xFFB8B4C0), f * seg(p, 0.02f * k, 0.1f + 0.02f * k))
            balance12(Offset(w * 0.5f, h * 0.5f), w * 0.15f, 0.1f * sin(a.t), Qn.GoldLight, f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("tzhaqqa0", 0, 0), Offset(w * 0.5f, ground - h * 0.14f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzhaqqa0", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            held105(Offset(w * 0.5f, ground), true, f * seg(p, 0.1f, 0.3f))
            for (k in 0 until 3) drawArc(Qn.Jade, -60f, 120f, false, Offset(w * 0.57f - w * 0.04f * (k + 1), ground - h * 0.12f - h * 0.04f * (k + 1)), Size(w * 0.08f * (k + 1), h * 0.08f * (k + 1)), alpha = 0.5f * f * seg(p, 0.45f + 0.06f * k, 0.6f + 0.06f * k), style = Stroke(1.4f * density))
            tagAt(a.tag("tzhaqqa1", 0, 0), Offset(w * 0.6f, h * 0.36f), f * seg(p, 0.35f, 0.5f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            daySky12(Qn.DayTop, Color(0xFFE8F0D0), ground, Color(0xFF6A8A4A))
            for (k in 0 until 5) tree104(Offset(w * (0.1f + 0.2f * k), ground), h * 0.25f, Color(0xFF4A9A5A), Qn.GoldLight, f, 1f)
            for (k in 0 until 6) drawCircle(Qn.GoldLight, w * 0.012f, Offset(w * (0.12f + 0.15f * k), ground - h * 0.08f - h * 0.03f * sin(a.t + k)), alpha = f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("tzhaqqa2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("tzhaqqa2", 0, 1), Offset(w * 0.5f, ground + h * 0.06f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            held105(Offset(w * 0.5f, ground), false, f * seg(p, 0.1f, 0.3f))
            tagAt(a.tag("tzhaqqa3", 0, 0), Offset(w * 0.38f, h * 0.4f), f * seg(p, 0.35f, 0.5f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tz-maarij ═════════════
internal fun DrawScope.tzmaarijScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzmaarij", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground) },
        { p, f ->
            veil105(f, 0.4f)
            qIcon("cloud", Offset(w * 0.5f, h * 0.22f), w * 0.12f, Color(0xFF6A6474), f * seg(p, 0.05f, 0.2f), a.t)
            person99(Offset(w * 0.5f + w * 0.01f * sin(a.t * 12f) * seg(p, 0.3f, 0.5f), ground), h * 0.12f, Fig105, f, arms = 0.9f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("tzmaarij0", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmaarij0", 0, 1), Offset(w * 0.78f, h * 0.56f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.45f, ground), h * 0.12f, Fig105, f)
            fqCoins(Offset(w * 0.6f, ground - h * 0.01f), w * 0.025f, 6, f * seg(p, 0.05f, 0.2f))
            qIcon("lock", Offset(w * 0.6f, ground - h * 0.18f), w * 0.06f, Qn.Ember, f * seg(p, 0.4f, 0.55f), a.t)
            person99(Offset(w * 0.85f, ground), h * 0.09f, Color(0xFF9A8A80), f * seg(p, 0.3f, 0.45f), arms = 0.6f)
            tagAt(a.tag("tzmaarij1", 0, 0), Offset(w * 0.6f, h * 0.36f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmaarij1", 0, 1), Offset(w * 0.3f, h * 0.48f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            for (k in 0 until 5) pray12(Offset(w * (0.2f + 0.15f * k), h * 0.8f), h * 0.1f, Good105, f * seg(p, 0.05f + 0.05f * k, 0.2f + 0.05f * k))
            softGlow(Offset(w * 0.5f, h * 0.7f), w * 0.35f, Qn.Jade, 0.3f * f * seg(p, 0.4f, 0.7f))
            tagAt(a.tag("tzmaarij2", 0, 0), Offset(w * 0.5f, h * 0.4f), f * seg(p, 0.35f, 0.5f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil105(f)
            qIcon("key", Offset(w * 0.3f, h * 0.45f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.05f, 0.25f), a.t)
            qIcon("hands", Offset(w * 0.7f, h * 0.45f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.35f, 0.55f), a.t)
            tagAt(a.tag("tzmaarij3", 0, 0), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmaarij3", 0, 1), Offset(w * 0.7f, h * 0.6f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tz-nuh ═════════════
internal fun DrawScope.tznuhScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tznuh", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { drawRect(Brush.verticalGradient(listOf(Color(0xFF3A4458), Color(0xFF8A8A8A)), endY = ground)); drawRect(Color(0xFF4A3E30), Offset(0f, ground), Size(w, h - ground)) },
        { p, f ->
            nameRoundel(Offset(w * 0.22f, h * 0.3f), w * 0.08f, a.big("tznuh0", 0, 2) ?: a.tag("tznuh0", 0, 2), Color(0xFF1E4A5A), Roundel.Rim, f * seg(p, 0.05f, 0.25f))
            for (k in 0 until 8) person99(Offset(w * (0.4f + 0.07f * k), ground), h * 0.08f, Color(0xFF8A7A6A), f, lean = -0.1f)
            qIcon("cloud", Offset(w * 0.72f, h * 0.2f), w * 0.12f, Color(0xFF4A4458), f * seg(p, 0.45f, 0.65f), a.t)
            tagAt(a.tag("tznuh0", 0, 0), Offset(w * 0.22f, h * 0.48f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("tznuh0", 0, 1), Offset(w * 0.72f, h * 0.34f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 2) person99(Offset(w * (0.3f + 0.08f * k), ground), h * 0.13f, Color(0xFF6A5A6A), f, arms = 0.6f)
            qIcon("crown", Offset(w * 0.34f, ground - h * 0.2f), w * 0.05f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 8) person99(Offset(w * (0.5f + 0.06f * k), ground), h * 0.08f, Color(0xFF8A7A6A), f * seg(p, 0.3f, 0.5f), lean = -0.1f)
            tagAt(a.tag("tznuh1", 0, 0), Offset(w * 0.34f, h * 0.42f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tznuh1", 0, 1), Offset(w * 0.72f, h * 0.56f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 5) person99(Offset(w * (0.2f + 0.15f * k), ground), h * 0.1f, Good105, f * (1f - 0.7f * seg(p, 0.35f, 0.6f)))
            for (k in 0 until 5) drawRoundRect(Color(0xFF7A6A5A), Offset(w * (0.18f + 0.15f * k), ground - h * 0.12f), Size(w * 0.04f, h * 0.12f), CornerRadius(3f * density), alpha = f * seg(p, 0.45f, 0.7f))
            tagAt(a.tag("tznuh2", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tznuh2", 0, 1), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f)
            for (k in 0 until 5) drawRoundRect(Color(0xFF7A6A5A), Offset(w * (0.18f + 0.15f * k), ground - h * 0.18f), Size(w * 0.05f, h * 0.18f), CornerRadius(3f * density), alpha = f)
            for (k in 0 until 5) person99(Offset(w * (0.25f + 0.12f * k), ground), h * 0.07f, Color(0xFF8A7A6A), f * seg(p, 0.3f + 0.05f * k, 0.45f + 0.05f * k), lean = -0.3f)
            qIcon("chain", Offset(w * 0.5f, h * 0.36f), w * 0.07f, Qn.Ember, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("tznuh3", 0, 0), Offset(w * 0.25f, h * 0.24f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tznuh3", 0, 1), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tz-jinn ═════════════
internal fun DrawScope.tzjinnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzjinn", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { nightSky(a.t, S97.Night, S97.NightLow, ground, 1f); dunes97(ground, Color(0xFF3A2E24), Color(0xFF2E241C)) },
        { p, f ->
            openBook21(Offset(w * 0.5f, h * 0.62f), w * 0.2f, T104.Page, T104.Rule, f, 0.6f)
            for (k in 0 until 4) drawArc(Qn.GoldLight, 200f, 140f, false, Offset(w * 0.5f - w * 0.05f * (k + 2), h * 0.55f - h * 0.04f * (k + 2)), Size(w * 0.1f * (k + 2), h * 0.08f * (k + 2)), alpha = 0.4f * f * seg(p, 0.15f + 0.08f * k, 0.3f + 0.08f * k), style = Stroke(1.3f * density))
            qIcon("ear", Offset(w * 0.82f, h * 0.3f), w * 0.07f, Color(0xFFB8C0E0), f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("tzjinn0", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("tzjinn0", 0, 1), Offset(w * 0.82f, h * 0.42f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f, 0.3f)
            qIcon("ear", Offset(w * 0.3f, h * 0.42f), w * 0.08f, Color(0xFFB8C0E0), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("star", Offset(w * 0.7f, h * 0.36f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.35f, 0.5f), a.t)
            softGlow(Offset(w * 0.7f, h * 0.36f), w * 0.15f, Qn.GoldLight, 0.5f * f * seg(p, 0.4f, 0.7f))
            tagAt(a.tag("tzjinn1", 0, 0), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzjinn1", 0, 1), Offset(w * 0.7f, h * 0.52f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 7) person99(Offset(w * (0.15f + 0.1f * k), ground), h * 0.09f, Fig105, f * seg(p, 0.05f + 0.03f * k, 0.2f + 0.03f * k))
            qIcon("scroll", Offset(w * 0.45f, h * 0.36f), w * 0.09f, T104.Page, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("tzjinn2", 0, 0), Offset(w * 0.45f, ground - h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzjinn2", 0, 1), Offset(w * 0.45f, h * 0.22f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f, 0.4f)
            drawPath(Path().apply { moveTo(0f, ground); lineTo(w * 0.3f, h * 0.4f); lineTo(w * 0.45f, ground); close() }, Color(0xFF3A3040), alpha = f)
            drawPath(Path().apply { moveTo(w * 0.55f, ground); lineTo(w * 0.7f, h * 0.38f); lineTo(w, ground); close() }, Color(0xFF3A3040), alpha = f)
            person99(Offset(w * 0.5f, ground), h * 0.09f, Fig105, f * seg(p, 0.1f, 0.25f))
            qIcon("flame", Offset(w * 0.5f, h * 0.4f), w * 0.07f, Qn.Ember, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("tzjinn3", 0, 0), Offset(w * 0.5f, ground + h * 0.06f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzjinn3", 0, 1), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tz-muzzammil ═════════════
internal fun DrawScope.tzmuzzammilScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tzmuzzammil", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { study104(a) },
        { p, f ->
            qIcon("lips", Offset(w * 0.3f, h * 0.38f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 8) drawCircle(Qn.GoldLight, w * 0.012f, Offset(w * (0.42f + 0.05f * k), h * 0.38f), alpha = f * seg(p, 0.25f + 0.06f * k, 0.32f + 0.06f * k))
            tagAt(a.tag("tzmuzzammil0", 0, 0), Offset(w * 0.3f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmuzzammil0", 0, 1), Offset(w * 0.6f, h * 0.5f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f)
            val press = easeOut(seg(p, 0.2f, 0.5f))
            openBook21(Offset(w * 0.5f, h * 0.3f + h * 0.1f * press), w * 0.18f, T104.Page, T104.Rule, f, 0.5f)
            qIcon("heart", Offset(w * 0.5f, h * 0.66f), w * 0.09f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("tzmuzzammil1", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmuzzammil1", 0, 1), Offset(w * 0.5f, h * 0.8f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f, 0.3f)
            qIcon("crescent", Offset(w * 0.8f, h * 0.3f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("heart", Offset(w * 0.4f, h * 0.36f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            softGlow(Offset(w * 0.4f, h * 0.36f), w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.35f, 0.6f))
            tagAt(a.tag("tzmuzzammil2", 0, 0), Offset(w * 0.8f, h * 0.68f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmuzzammil2", 0, 1), Offset(w * 0.4f, h * 0.22f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmuzzammil2", 0, 2), Offset(w * 0.4f, h * 0.5f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f)
            pray12(Offset(w * 0.5f, h * 0.84f), h * 0.12f, Fig105, f * seg(p, 0.05f, 0.25f))
            softGlow(Offset(w * 0.5f, h * 0.2f), w * 0.2f, Qn.GoldLight, 0.5f * f * seg(p, 0.35f, 0.7f))
            drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.68f), Offset(w * 0.5f, h * 0.3f), 1.5f * density, alpha = 0.5f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("tzmuzzammil3", 0, 1), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmuzzammil3", 0, 0), Offset(w * 0.75f, h * 0.6f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tz-muddaththir ═════════════
internal fun DrawScope.tzmuddaththirScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzmuddaththir", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { nightSky(a.t, S97.Dusk, S97.DuskLow, ground, 0.6f); dunes97(ground, S97.SandDark, Color(0xFF6A5236)) },
        { p, f ->
            person99(Offset(w * 0.4f, ground), h * 0.13f, Bad105, f, lean = 0.1f * sin(a.t * 1.5f))
            for (k in 0 until 5) drawCircle(Qn.Ember, w * 0.008f, Offset(w * 0.4f + cos(a.t + k * 1.2f) * w * 0.08f, ground - h * 0.2f + sin(a.t + k * 1.2f) * h * 0.04f), alpha = f * seg(p, 0.2f, 0.4f))
            tagAt(a.tag("tzmuddaththir0", 0, 0), Offset(w * 0.4f, h * 0.36f), f * seg(p, 0.35f, 0.5f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            openBook21(Offset(w * 0.4f, h * 0.45f), w * 0.2f, T104.Page, T104.Rule, f, 0.6f)
            val probe = seg(p, 0.3f, 0.7f)
            drawCircle(Qn.Ember, w * 0.04f, Offset(mix(w * 0.85f, w * 0.55f, probe), h * 0.4f), alpha = f * (1f - 0.7f * seg(p, 0.7f, 0.85f)), style = Stroke(2f * density))
            tagAt(a.tag("tzmuddaththir1", 0, 0), Offset(w * 0.4f, h * 0.24f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmuddaththir1", 0, 1), Offset(w * 0.8f, h * 0.6f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.4f, ground), h * 0.13f, Bad105, f, arms = 0.6f)
            qIcon("lips", Offset(w * 0.55f, h * 0.48f), w * 0.07f, Qn.Ember, f * seg(p, 0.2f, 0.35f), a.t)
            drawLine(Qn.Ember, Offset(w * 0.48f, h * 0.41f), Offset(w * 0.62f, h * 0.55f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.55f, 0.7f))
            tagAt(a.tag("tzmuddaththir2", 0, 0), Offset(w * 0.6f, h * 0.32f), f * seg(p, 0.25f, 0.4f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f, 0.7f)
            qIcon("flame", Offset(w * 0.5f, h * 0.76f), w * 0.1f, Qn.Ember, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 19) {
                val ang = PI.toFloat() * (1.05f + 0.9f * k / 18f)
                angel98(Offset(w * 0.5f + cos(ang) * w * 0.38f, h * 0.62f + sin(ang) * h * 0.36f), w * 0.015f, f * seg(p, 0.2f + 0.02f * k, 0.3f + 0.02f * k))
            }
            tagAt(a.tag("tzmuddaththir3", 0, 2), Offset(w * 0.5f, h * 0.4f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmuddaththir3", 0, 1), Offset(w * 0.5f, h * 0.52f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC20283A), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmuddaththir3", 0, 0), Offset(w * 0.5f, h * 0.9f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tz-qiyama ═════════════
internal fun DrawScope.tzqiyamaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzqiyama", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { drawRect(Brush.verticalGradient(listOf(Color(0xFF1A1830), Color(0xFF3A3040)))); drawRect(Color(0xFF2A2420), Offset(0f, ground), Size(w, h - ground)) },
        { p, f ->
            val scatter = easeInOut(seg(p, 0.1f, 0.6f))
            for (k in 0 until 30) {
                val home = Offset(w * 0.5f + cos(k * 0.7f) * w * 0.05f, h * 0.55f + sin(k * 1.1f) * h * 0.1f)
                val far = Offset(w * (0.05f + 0.03f * k), ground - h * 0.01f * (k % 3))
                drawCircle(Color(0xFFB8B0A0), w * 0.005f, Offset(mix(home.x, far.x, scatter), mix(home.y, far.y, scatter)), alpha = f)
            }
            tagAt(a.tag("tzqiyama0", 0, 0), Offset(w * 0.3f, h * 0.3f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzqiyama0", 0, 1), Offset(w * 0.7f, ground - h * 0.08f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzqiyama0", 0, 2), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            qIcon("hands", Offset(w * 0.5f, h * 0.48f), w * 0.14f, Color(0xFFE8DCC8), f * seg(p, 0.05f, 0.25f), a.t)
            softGlow(Offset(w * 0.5f, h * 0.48f), w * 0.15f, Qn.GoldLight, 0.4f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("tzqiyama1", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzqiyama1", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f, 0.3f)
            val c = Offset(w * 0.5f, h * 0.48f)
            for (k in 0 until 9) {
                val r = w * (0.03f + 0.022f * k)
                drawArc(Color(0xFFE8DCC8), 200f + 6f * k, 300f - 8f * k, false, Offset(c.x - r, c.y - r * 1.25f), Size(r * 2f, r * 2.5f), alpha = f * seg(p, 0.1f + 0.06f * k, 0.2f + 0.06f * k), style = Stroke(1.6f * density))
            }
            tagAt(a.tag("tzqiyama2", 0, 0), Offset(w * 0.2f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzqiyama2", 0, 1), Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f)
            val run = seg(p, 0.1f, 0.9f)
            qIcon("flame", Offset(w * (0.2f + 0.2f * run), h * 0.45f), w * 0.07f, Qn.Ember, f, a.t)
            qIcon("hourglass", Offset(w * (0.8f - 0.05f * run), h * 0.45f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("tzqiyama3", 0, 0), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tzqiyama3", 0, 1), Offset(w * 0.78f, h * 0.6f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tz-insan ═════════════
internal fun DrawScope.tzinsanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tzinsan", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { town105(a, ground) },
        { p, f ->
            veil105(f, 0.45f)
            for (k in 0 until 5) person99(Offset(w * (0.2f + 0.08f * k), ground), h * 0.1f, Good105, f, lean = 0.08f)
            balance12(Offset(w * 0.72f, h * 0.4f), w * 0.1f, 0.15f * sin(a.t), Qn.GoldLight, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("tzinsan0", 0, 0), Offset(w * 0.36f, ground - h * 0.2f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("tzinsan0", 0, 1), Offset(w * 0.72f, h * 0.2f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            val give = easeInOut(seg(p, 0.2f, 0.7f))
            person99(Offset(w * 0.3f, ground), h * 0.12f, Good105, f, arms = 0.8f * give)
            for (k in 0 until 3) person99(Offset(w * (0.62f + 0.08f * k), ground), h * (if (k == 1) 0.07f else 0.1f), Color(0xFF9A8A80), f)
            qIcon("cup", Offset(mix(w * 0.36f, w * 0.62f, give), ground - h * 0.12f), w * 0.05f, Qn.GoldLight, f, a.t)
            tagAt(a.tag("tzinsan1", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil105(f)
            fqCoins(Offset(w * 0.3f, h * 0.5f), w * 0.022f, 4, f * seg(p, 0.05f, 0.2f))
            qIcon("lips", Offset(w * 0.7f, h * 0.45f), w * 0.08f, Color(0xFF8A8494), f * seg(p, 0.3f, 0.45f), a.t)
            for (k in 0 until 2) drawLine(Qn.Ember, Offset(w * (0.22f + 0.4f * k), h * 0.38f), Offset(w * (0.38f + 0.4f * k), h * 0.54f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.2f + 0.3f * k, 0.32f + 0.3f * k))
            tagAt(a.tag("tzinsan2", 0, 0), Offset(w * 0.3f, h * 0.66f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tzinsan2", 0, 1), Offset(w * 0.7f, h * 0.66f), f * seg(p, 0.4f, 0.55f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            daySky12(Color(0xFFB8D8E8), Color(0xFFE8F0D0), ground, Color(0xFF6A8A4A))
            for (k in 0 until 5) tree104(Offset(w * (0.1f + 0.2f * k), ground), h * 0.28f, Color(0xFF4A9A5A), Qn.GoldLight, f, 1f)
            drawRect(Color(0x553A6A4A), Offset(0f, 0f), Size(w, ground), alpha = f * seg(p, 0.3f, 0.5f))
            qIcon("sun", Offset(w * 0.85f, h * 0.12f), w * 0.06f, Qn.Ember, f * seg(p, 0.45f, 0.6f) * (1f - 0.6f * seg(p, 0.6f, 0.8f)), a.t)
            tagAt(a.tag("tzinsan3", 0, 0), Offset(w * 0.4f, h * 0.12f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("tzinsan3", 0, 1), Offset(w * 0.8f, h * 0.24f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tz-mursalat ═════════════
internal fun DrawScope.tzmursalatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.6f
    film98(a, "tzmursalat", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { daySky12(Qn.DayTop, Color(0xFFF0E4C0), ground, Color(0xFF6A5040)) },
        { p, f ->
            for (k in 0 until 6) person99(Offset(w * (0.1f + 0.15f * k), ground), h * 0.09f, Fig105, f * seg(p, 0.05f, 0.2f))
            for (k in 0 until 5) drawRoundRect(Color(0xFF4A3A2E), Offset(w * (0.12f + 0.17f * k), ground + h * 0.18f), Size(w * 0.1f, h * 0.04f), CornerRadius(4f * density), alpha = f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("tzmursalat0", 0, 2), Offset(w * 0.5f, ground + h * 0.08f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmursalat0", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmursalat0", 0, 1), Offset(w * 0.5f, ground + h * 0.3f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            val split = easeInOut(seg(p, 0.2f, 0.6f))
            softGlow(Offset(w * (0.5f - 0.22f * split), h * 0.45f), w * 0.12f, Qn.GoldLight, 0.7f * f)
            qIcon("cloud", Offset(w * (0.5f + 0.22f * split), h * 0.45f), w * 0.1f, Color(0xFF5A4A50), f * seg(p, 0.2f, 0.4f), a.t)
            drawLine(Color(0xFFE8E0D0), Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.7f), 2f * density, alpha = f * split)
            tagAt(a.tag("tzmursalat1", 0, 2), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmursalat1", 0, 0), Offset(w * 0.28f, h * 0.62f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
            tagAt(a.tag("tzmursalat1", 0, 1), Offset(w * 0.72f, h * 0.62f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f, 0.7f)
            for (k in 0 until 3) {
                val rise = easeOut(seg(p, 0.2f + 0.1f * k, 0.6f + 0.1f * k))
                val x = w * 0.5f + (k - 1) * w * 0.12f * (0.3f + rise)
                for (j in 0 until 4) drawCircle(Color(0xFF4A3A3A), w * (0.03f + 0.01f * j), Offset(x + (k - 1) * w * 0.04f * j, h * 0.8f - h * 0.12f * j * rise), alpha = f * 0.8f)
            }
            tagAt(a.tag("tzmursalat2", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tzmursalat2", 0, 1), Offset(w * 0.5f, h * 0.9f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil105(f)
            openBook21(Offset(w * 0.5f, h * 0.46f), w * 0.22f, T104.Page, T104.Rule, f * seg(p, 0.05f, 0.25f), 0.7f)
            softGlow(Offset(w * 0.5f, h * 0.46f), w * 0.25f, Qn.GoldLight, 0.4f * f * seg(p, 0.3f, 0.7f))
            tagAt(a.tag("tzmursalat3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
    )
}
