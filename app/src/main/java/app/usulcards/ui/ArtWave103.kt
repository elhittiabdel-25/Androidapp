package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 103 (2026-11-17): the thirteen cards of «تعامله ﷺ مع الناس» redrawn in the illustrated style (boards in wave 96).
 * He ﷺ is only his named light, inside the scene (the house, the mosque, the street); never a figure beside it. His
 * daughters and grandsons are roundels in his family's colour, the Mothers in their own; other named people the plain
 * roundel; Jibril cool white light; ordinary people and children faceless figures.
 */

private val Warm103 = Color(0xFFE2CCA0)
private val Trim103 = Color(0xFF8A6A40)
private val Kid103 = Color(0xFFE8D8B8)

private fun DrawScope.veil103(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

private fun DrawScope.street103(a: ArtCtx, ground: Float) {
    val w = size.width
    val h = size.height
    daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB09868))
    for (k in 0 until 4) house12(Offset(w * (0.08f + 0.28f * k), ground), w * 0.11f, Warm103, Trim103, 0.55f)
    palm12(Offset(w * 0.94f, ground), h * 0.2f, Color(0xFF6A4A30), Qn.Leaf, 0.7f, a.t)
}

private fun DrawScope.room103(a: ArtCtx, ground: Float) {
    nightSky(a.t, S97.Night, S97.NightLow, ground, 0.7f)
    dunes97(ground, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.kid103(foot: Offset, alpha: Float, walk: Float = 0f) = person99(foot, size.height * 0.055f, Kid103, alpha, walk = walk)

/** Two tags facing each other across a line: the wrong way (crossed, left) and the right way (lit, right). */
private fun DrawScope.vs103(a: ArtCtx, kind: String, wrong: Int, right: Int, p: Float, f: Float, iconL: String, iconR: String) {
    val w = size.width
    val h = size.height
    veil103(f)
    drawLine(Color(0xFF8A8494), Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.72f), 1.5f * density, alpha = f)
    qIcon(iconL, Offset(w * 0.27f, h * 0.42f), w * 0.09f, Color(0xFF9A8A80), f * seg(p, 0.05f, 0.2f) * (1f - 0.5f * seg(p, 0.35f, 0.5f)), a.t)
    drawLine(Qn.Ember, Offset(w * 0.18f, h * 0.34f), Offset(w * 0.36f, h * 0.5f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.3f, 0.42f))
    tagAt(a.tag(kind, 0, wrong), Offset(w * 0.27f, h * 0.6f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
    qIcon(iconR, Offset(w * 0.73f, h * 0.42f), w * 0.1f, Qn.Jade, f * seg(p, 0.5f, 0.65f), a.t)
    softGlow(Offset(w * 0.73f, h * 0.42f), w * 0.12f, Qn.Jade, 0.35f * f * seg(p, 0.6f, 0.85f))
    tagAt(a.tag(kind, 0, right), Offset(w * 0.73f, h * 0.6f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
}

// ═════════════ tm-ahl ═════════════
internal fun DrawScope.tmahlScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    val home = Offset(w * 0.5f, ground)
    film98(a, "tmahl", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1.05f) },
        { room103(a, ground); house12(home, w * 0.34f, Warm103, Trim103, 1f, lit = 0.6f) },
        { p, f ->
            sirajMunir(home + Offset(0f, -h * 0.12f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.14f))
            qIcon("heart", Offset(w * 0.2f, h * 0.36f), w * 0.07f, Qn.Rose, f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("sun", Offset(w * 0.8f, h * 0.36f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("tmahl0", 0, 1), Offset(w * 0.2f, h * 0.48f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmahl0", 0, 2), Offset(w * 0.8f, h * 0.48f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            sirajMunir(home + Offset(-w * 0.06f, -h * 0.1f), w * 0.026f, f, a.t, named = false)
            qIcon("cup", home + Offset(w * 0.08f, -h * 0.08f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            val call = seg(p, 0.5f, 0.7f)
            for (k in 0 until 3) drawArc(Qn.GoldLight, -60f, 120f, false, Offset(w * 0.8f - w * 0.04f * (k + 1), h * 0.3f - h * 0.04f * (k + 1)), Size(w * 0.08f * (k + 1), h * 0.08f * (k + 1)), alpha = 0.5f * f * call, style = Stroke(1.4f * density))
            qIcon("mat", Offset(w * 0.8f, h * 0.42f), w * 0.07f, Qn.Jade, f * call, a.t)
            tagAt(a.tag("tmahl1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmahl1", 0, 1), Offset(w * 0.8f, h * 0.54f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil103(f)
            sirajMunir(Offset(w * 0.5f, h * 0.3f), w * 0.03f, f, a.t, named = false)
            val sew = (a.t * 1.5f) % 1f
            drawRoundRect(Color(0xFFD8D0C0), Offset(w * 0.18f, h * 0.48f), Size(w * 0.24f, h * 0.12f), CornerRadius(4f * density), alpha = f * seg(p, 0.05f, 0.2f))
            for (k in 0 until 6) drawLine(Color(0xFF6A5030), Offset(w * (0.2f + 0.035f * k), h * 0.54f), Offset(w * (0.215f + 0.035f * k), h * 0.54f), 1.6f * density, alpha = f * seg(p, 0.15f + 0.05f * k, 0.2f + 0.05f * k))
            drawLine(Color(0xFFC0C4CC), Offset(w * (0.2f + 0.2f * sew), h * 0.5f), Offset(w * (0.2f + 0.2f * sew) + w * 0.03f, h * 0.47f), 1.4f * density, alpha = f * seg(p, 0.1f, 0.2f))
            drawOval(Color(0xFF8A6A48), Offset(w * 0.6f, h * 0.5f), Size(w * 0.22f, h * 0.08f), alpha = f * seg(p, 0.45f, 0.6f))
            drawLine(Color(0xFF5A4030), Offset(w * 0.64f, h * 0.54f), Offset(w * 0.78f, h * 0.54f), 2f * density, alpha = f * seg(p, 0.6f, 0.75f))
            tagAt(a.tag("tmahl2", 0, 0), Offset(w * 0.3f, h * 0.68f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmahl2", 0, 1), Offset(w * 0.71f, h * 0.68f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil103(f)
            qIcon("hands", Offset(w * 0.5f, h * 0.42f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.25f), a.t)
            drawCircle(Qn.Ember, w * 0.11f, Offset(w * 0.5f, h * 0.42f), alpha = f * seg(p, 0.35f, 0.5f), style = Stroke(2.5f * density))
            drawLine(Qn.Ember, Offset(w * 0.42f, h * 0.34f), Offset(w * 0.58f, h * 0.5f), 2.5f * density, alpha = f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("tmahl3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tm-wafa ═════════════
internal fun DrawScope.tmwafaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    val khadija = Roundel.Fills[0]
    film98(a, "tmwafa", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room103(a, ground) },
        { p, f ->
            sirajMunir(Offset(w * 0.28f, h * 0.45f), w * 0.04f, f, a.t, named = false)
            nameRoundel(Offset(w * 0.7f, h * 0.42f), w * 0.09f, a.big("tmwafa0", 0, 1) ?: a.tag("tmwafa0", 0, 1), khadija, Roundel.Rim, f * seg(p, 0.2f, 0.4f))
            for (k in 0 until 4) qIcon("heart", Offset(mix(w * 0.34f, w * 0.62f, ((a.t * 0.3f + k / 4f) % 1f)), h * 0.42f - h * 0.04f * sin(k.toFloat())), w * 0.03f, Qn.Rose, f * seg(p, 0.3f, 0.5f), a.t)
            tagAt(a.tag("tmwafa0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            street103(a, ground)
            sirajMunir(Offset(w * 0.2f, ground - h * 0.16f), w * 0.026f, f, a.t, named = false)
            for (k in 0 until 3) {
                val go = easeInOut(seg(p, 0.15f + 0.15f * k, 0.5f + 0.15f * k))
                qIcon("cup", Offset(mix(w * 0.25f, w * (0.5f + 0.15f * k), go), ground - h * 0.14f), w * 0.035f, Qn.GoldLight, f * seg(p, 0.1f, 0.2f), a.t)
                person99(Offset(w * (0.5f + 0.15f * k), ground), h * 0.08f, Qn.Rose, f)
            }
            tagAt(a.tag("tmwafa1", 0, 0), Offset(w * 0.65f, h * 0.3f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil103(f)
            qIcon("heart", Offset(w * 0.3f, h * 0.42f), w * 0.1f, Qn.Rose, f * seg(p, 0.05f, 0.25f), a.t)
            mother102(Offset(w * 0.7f, h * 0.42f), w * 0.09f, a, "tmwafa2", 1, 1, f * seg(p, 0.35f, 0.55f))
            tagAt(a.tag("tmwafa2", 0, 0), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil103(f)
            nameRoundel(Offset(w * 0.5f, h * 0.42f), w * 0.1f, a.big("tmwafa3", 0, 0) ?: a.tag("tmwafa3", 0, 0), khadija, Roundel.Rim, f * seg(p, 0.05f, 0.25f))
            drawCircle(Qn.GoldLight, w * 0.15f, Offset(w * 0.5f, h * 0.42f), alpha = 0.6f * f * seg(p, 0.3f, 0.5f), style = Stroke(1.5f * density))
            qIcon("lock", Offset(w * 0.5f, h * 0.22f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("tmwafa3", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tm-banat ═════════════
internal fun DrawScope.tmbanatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmbanat", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room103(a, ground) },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.22f), w * 0.03f, f, a.t, named = false)
            for (k in 0 until 4) {
                val c = Offset(w * (0.14f + 0.24f * k), h * 0.52f)
                drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.26f), c, 1.2f * density, alpha = 0.4f * f * seg(p, 0.1f + 0.18f * k, 0.25f + 0.18f * k))
                close97(c, w * 0.075f, a, "tmbanat0", k, 3, f * seg(p, 0.1f + 0.18f * k, 0.25f + 0.18f * k))
            }
        },
        { p, f ->
            house12(Offset(w * 0.5f, ground), w * 0.3f, Warm103, Trim103, f, lit = 0.6f)
            sirajMunir(Offset(w * 0.7f, h * 0.36f), w * 0.04f, f, a.t, nameAt = Offset(w * 0.7f, h * 0.18f))
            val come = easeInOut(seg(p, 0.1f, 0.45f))
            close97(Offset(mix(w * 0.05f, w * 0.3f, come), h * 0.38f), w * 0.08f, a, "tmbanat1", 2, 3, f)
            tagAt(a.tag("tmbanat1", 0, 1), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            vs103(a, "tmbanat2", 1, 0, p, f, "people", "star")
        },
        { p, f ->
            veil103(f)
            qIcon("flame", Offset(w * 0.3f, h * 0.5f), w * 0.1f, Qn.Ember, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tmbanat3", 0, 0), Offset(w * 0.3f, h * 0.68f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            close97(Offset(w * 0.7f, h * 0.45f), w * 0.09f, a, "tmbanat3", 1, 3, f * seg(p, 0.35f, 0.55f))
            drawCircle(Qn.Jade, w * 0.13f, Offset(w * 0.7f, h * 0.45f), alpha = 0.6f * f * seg(p, 0.6f, 0.8f), style = Stroke(2f * density))
        },
    )
}

// ═════════════ tm-ahfad ═════════════
internal fun DrawScope.tmahfadScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmahfad", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room103(a, ground) },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.24f), w * 0.035f, f, a.t, named = false)
            close97(Offset(w * 0.3f, h * 0.52f), w * 0.08f, a, "tmahfad0", 0, 3, f * seg(p, 0.05f, 0.25f))
            close97(Offset(w * 0.7f, h * 0.52f), w * 0.08f, a, "tmahfad0", 1, 3, f * seg(p, 0.2f, 0.4f))
            for (k in 0 until 6) drawCircle(Roundel.Fills[3], w * 0.022f, Offset(w * (0.25f + 0.1f * k), h * 0.72f), alpha = f * seg(p, 0.45f + 0.04f * k, 0.55f + 0.04f * k))
            tagAt(a.tag("tmahfad0", 0, 2), Offset(w * 0.5f, h * 0.82f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil103(f)
            for (k in 0 until 3) {
                val on = seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k)
                qIcon(listOf("ear", "wheat", "pen")[k], Offset(w * (0.22f + 0.28f * k), h * 0.42f), w * 0.07f, Qn.GoldLight, f * on, a.t)
                tagAt(a.tag("tmahfad1", 0, k), Offset(w * (0.22f + 0.28f * k), h * 0.56f), f * on, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil103(f)
            kid103(Offset(w * 0.4f, h * 0.7f), f)
            qIcon("coin", Offset(w * 0.4f, h * 0.48f), w * 0.05f, Qn.Gold, f * (1f - seg(p, 0.5f, 0.7f)), a.t)
            sirajMunir(Offset(w * 0.68f, h * 0.42f), w * 0.035f, f, a.t, named = false)
            drawLine(Qn.Ember, Offset(w * 0.34f, h * 0.43f), Offset(w * 0.46f, h * 0.53f), 2.5f * density, alpha = f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("tmahfad2", 0, 0), Offset(w * 0.4f, h * 0.32f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tmahfad2", 0, 1), Offset(w * 0.68f, h * 0.6f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil103(f)
            val swap = easeInOut(seg(p, 0.35f, 0.7f))
            tagAt(a.tag("tmahfad3", 0, 0), Offset(w * 0.5f, mix(h * 0.42f, h * 0.26f, swap)), f * (1f - 0.7f * swap), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            if (swap > 0.2f) drawLine(Qn.Ember, Offset(w * 0.42f, mix(h * 0.42f, h * 0.26f, swap)), Offset(w * 0.58f, mix(h * 0.42f, h * 0.26f, swap)), 2.5f * density, alpha = f * (1f - 0.7f * swap))
            softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.12f, Qn.Jade, 0.4f * f * swap)
            tagAt(a.tag("tmahfad3", 0, 1), Offset(w * 0.5f, h * 0.55f), f * swap, back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tm-jar ═════════════
internal fun DrawScope.tmjarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmjar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB09868)) },
        { p, f ->
            house12(Offset(w * 0.5f, ground), w * 0.2f, Warm103, Trim103, f, lit = 0.5f)
            sirajMunir(Offset(w * 0.5f, ground - h * 0.08f), w * 0.024f, f, a.t, named = false)
            for (k in 0 until 2) house12(Offset(w * (0.15f + 0.7f * k), ground), w * 0.16f, Color(0xFFB8A088), Color(0xFF6A4A3A), f)
            for (k in 0 until 5) drawCircle(Color(0xFF6A5A48), w * 0.008f, Offset(w * (0.42f + 0.04f * k), ground - h * 0.02f), alpha = f * seg(p, 0.3f + 0.05f * k, 0.4f + 0.05f * k))
            tagAt(a.tag("tmjar0", 0, 0), Offset(w * 0.15f, ground - h * 0.26f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("tmjar0", 0, 1), Offset(w * 0.5f, ground - h * 0.3f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil103(f)
            angel98(Offset(w * 0.25f, h * 0.35f), w * 0.06f, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("tmjar1", 0, 0), Offset(w * 0.25f, h * 0.52f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC20283A), ink = ArtInk.Ivory)
            for (k in 0 until 3) house12(Offset(w * (0.55f + 0.14f * k), h * 0.7f), w * 0.1f, Warm103, Trim103, f, lit = seg(p, 0.35f + 0.12f * k, 0.5f + 0.12f * k))
            tagAt(a.tag("tmjar1", 0, 1), Offset(w * 0.69f, h * 0.4f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            for (k in 0 until 2) {
                val c = Offset(w * (0.28f + 0.44f * k), ground)
                house12(c, w * 0.18f, if (k == 0) Warm103 else Color(0xFFD0C0A8), Trim103, f, lit = seg(p, 0.1f + 0.4f * k, 0.3f + 0.4f * k))
                tagAt(a.tag("tmjar2", 0, k), c + Offset(0f, -h * 0.28f), f * seg(p, 0.1f + 0.4f * k, 0.25f + 0.4f * k), ink = ArtInk.Ivory)
            }
            drawArc(Qn.GoldLight, 200f, 140f, false, Offset(w * 0.2f, ground - h * 0.42f), Size(w * 0.6f, h * 0.3f), alpha = 0.6f * f * seg(p, 0.65f, 0.85f), style = Stroke(2f * density))
        },
        { p, f -> vs103(a, "tmjar3", 0, 1, p, f, "cross", "heart") },
    )
}

// ═════════════ tm-khadam ═════════════
internal fun DrawScope.tmkhadamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmkhadam", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1.05f) },
        { room103(a, ground); house12(Offset(w * 0.5f, ground), w * 0.34f, Warm103, Trim103, 1f, lit = 0.5f) },
        { p, f ->
            sirajMunir(Offset(w * 0.4f, ground - h * 0.14f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.14f))
            kid103(Offset(w * 0.6f, ground), f * seg(p, 0.15f, 0.3f))
            qIcon("heart", Offset(w * 0.5f, ground - h * 0.3f), w * 0.06f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("tmkhadam0", 0, 1), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil103(f)
            for (k in 0 until 2) {
                val c = Offset(w * (0.3f + 0.4f * k), h * 0.42f)
                qIcon(if (k == 0) "cup" else "scroll", c + Offset(-w * 0.05f, 0f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.1f + 0.4f * k, 0.25f + 0.4f * k), a.t)
                qIcon(if (k == 0) "cup" else "scroll", c + Offset(w * 0.05f, 0f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.2f + 0.4f * k, 0.35f + 0.4f * k), a.t)
                tagAt(a.tag("tmkhadam1", 0, k), c + Offset(0f, h * 0.14f), f * seg(p, 0.15f + 0.4f * k, 0.3f + 0.4f * k), ink = ArtInk.Ivory)
            }
            drawLine(Qn.GoldLight, Offset(w * 0.27f, h * 0.48f), Offset(w * 0.33f, h * 0.48f), 1.5f * density, alpha = f * seg(p, 0.25f, 0.35f))
        },
        { p, f ->
            sirajMunir(Offset(w * 0.4f, ground - h * 0.14f), w * 0.03f, f, a.t, named = false)
            kid103(Offset(w * 0.6f, ground), f)
            for (k in 0 until 3) drawArc(Qn.GoldLight, -40f, 80f, false, Offset(w * 0.42f - w * 0.03f * (k + 1), ground - h * 0.14f - h * 0.03f * (k + 1)), Size(w * 0.06f * (k + 1), h * 0.06f * (k + 1)), alpha = 0.5f * f * seg(p, 0.2f + 0.08f * k, 0.35f + 0.08f * k), style = Stroke(1.3f * density))
            tagAt(a.tag("tmkhadam2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            street103(a, ground)
            plain97(Offset(w * 0.3f, h * 0.36f), w * 0.07f, a, "tmkhadam3", 0, f * seg(p, 0.05f, 0.25f))
            kid103(Offset(mix(w * 0.3f, w * 0.6f, easeInOut(seg(p, 0.1f, 0.6f))), ground), f, walk = a.t * 3f)
            qIcon("sun", Offset(w * 0.72f, h * 0.3f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("tmkhadam3", 0, 1), Offset(w * 0.72f, h * 0.42f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tm-bala ═════════════
internal fun DrawScope.tmbalaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmbala", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { street103(a, ground) },
        { p, f ->
            sirajMunir(Offset(w * 0.32f, ground - h * 0.16f), w * 0.028f, f, a.t, named = false)
            person99(Offset(w * 0.6f, ground), h * 0.09f, Color(0xFF9A8A80), f, lean = 0.15f)
            qIcon("heart", Offset(w * 0.46f, ground - h * 0.28f), w * 0.06f, Qn.Rose, f * seg(p, 0.15f, 0.3f), a.t)
            tagAt(a.tag("tmbala0", 0, 0), Offset(w * 0.32f, h * 0.3f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmbala0", 0, 1), Offset(w * 0.68f, ground - h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f -> vs103(a, "tmbala1", 0, 1, p, f, "eye", "eye") },
        { p, f ->
            val m = Offset(w * 0.5f, ground)
            drawRect(Color(0xFF8A6A40), Offset(m.x - w * 0.3f, m.y - h * 0.22f), Size(w * 0.6f, h * 0.22f), alpha = 0.5f * f)
            qIcon("mat", Offset(w * 0.3f, ground - h * 0.06f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tmbala2", 0, 0), Offset(w * 0.3f, h * 0.36f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            for (r in 0 until 2) for (k in 0 until 5) pray12(Offset(w * (0.5f + 0.07f * k), ground - r * h * 0.06f), h * 0.07f, Color(0xFFF2EEE4), f * seg(p, 0.5f, 0.65f))
            sirajMunir(Offset(w * 0.64f, ground - h * 0.3f), w * 0.024f, f * seg(p, 0.45f, 0.6f), a.t, named = false)
            tagAt(a.tag("tmbala2", 0, 1), Offset(w * 0.64f, h * 0.24f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f -> vs103(a, "tmbala3", 1, 0, p, f, "people", "people") },
    )
}

// ═════════════ tm-kuffar ═════════════
internal fun DrawScope.tmkuffarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmkuffar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { room103(a, ground) },
        { p, f ->
            balance12(Offset(w * 0.5f, h * 0.68f), w * 0.16f, 0.02f * sin(a.t), Qn.GoldLight, f * seg(p, 0.05f, 0.25f))
            person99(Offset(w * 0.2f, ground), h * 0.09f, Color(0xFFC8E8D0), f)
            person99(Offset(w * 0.8f, ground), h * 0.09f, Color(0xFF9A8AB0), f)
            tagAt(a.tag("tmkuffar0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil103(f)
            qIcon("scroll", Offset(w * 0.3f, h * 0.42f), w * 0.09f, Color(0xFFEFE2C0), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("tree", Offset(w * 0.72f, h * 0.42f), w * 0.1f, Qn.Jade, f * seg(p, 0.35f, 0.5f) * (1f - 0.6f * seg(p, 0.6f, 0.8f)), a.t)
            drawLine(Qn.Ember, Offset(w * 0.62f, h * 0.32f), Offset(w * 0.82f, h * 0.52f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.6f, 0.72f))
            tagAt(a.tag("tmkuffar1", 0, 0), Offset(w * 0.3f, h * 0.58f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmkuffar1", 0, 1), Offset(w * 0.72f, h * 0.6f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            house12(Offset(w * 0.5f, ground), w * 0.32f, Warm103, Trim103, f, lit = 0.5f)
            drawRoundRect(Color(0xFFB8A88A), Offset(w * 0.52f, ground - h * 0.05f), Size(w * 0.18f, h * 0.04f), CornerRadius(3f * density), alpha = f)
            kid103(Offset(w * 0.6f, ground - h * 0.05f), f * (0.7f + 0.3f * seg(p, 0.6f, 0.8f)))
            sirajMunir(Offset(w * 0.38f, ground - h * 0.14f), w * 0.028f, f, a.t, named = false)
            qIcon("check", Offset(w * 0.6f, h * 0.36f), w * 0.07f, Qn.Jade, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("tmkuffar2", 0, 0), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmkuffar2", 0, 1), Offset(w * 0.6f, h * 0.48f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f -> vs103(a, "tmkuffar3", 0, 1, p, f, "lips", "heart") },
    )
}

// ═════════════ tm-jadid ═════════════
internal fun DrawScope.tmjadidScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmjadid", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { street103(a, ground) },
        { p, f ->
            veil103(f)
            sirajMunir(Offset(w * 0.5f, h * 0.36f), w * 0.04f, f, a.t, named = false)
            for (k in 0 until 10) {
                val ang = 2f * PI.toFloat() * k / 10f
                drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.36f) + Offset(cos(ang) * w * 0.07f, sin(ang) * w * 0.07f), Offset(w * 0.5f, h * 0.36f) + Offset(cos(ang) * w * 0.11f, sin(ang) * w * 0.11f), 1.5f * density, alpha = f * seg(p, 0.05f, 0.2f))
            }
            tagAt(a.tag("tmjadid0", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            plain97(Offset(w * 0.25f, h * 0.66f), w * 0.07f, a, "tmjadid0", 1, f * seg(p, 0.3f, 0.5f))
            plain97(Offset(w * 0.75f, h * 0.66f), w * 0.07f, a, "tmjadid0", 2, f * seg(p, 0.55f, 0.75f))
        },
        { p, f ->
            sirajMunir(Offset(w * 0.3f, ground - h * 0.16f), w * 0.028f, f, a.t, named = false)
            person99(Offset(w * 0.55f, ground), h * 0.1f, Color(0xFFC8E8D0), f)
            pray12(Offset(w * 0.75f, ground), h * 0.1f, Color(0xFFC8E8D0), f * seg(p, 0.3f, 0.5f))
            qIcon("hands", Offset(w * 0.6f, h * 0.36f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("tmjadid1", 0, 0), Offset(w * 0.75f, ground - h * 0.2f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("tmjadid1", 0, 1), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil103(f)
            fqCoins(Offset(w * 0.3f, h * 0.52f), w * 0.025f, 6, f * seg(p, 0.05f, 0.25f))
            qIcon("heart", Offset(w * 0.7f, h * 0.45f), w * 0.1f, Qn.Rose, f * seg(p, 0.4f, 0.6f), a.t)
            drawLine(Qn.GoldLight, Offset(w * 0.38f, h * 0.48f), Offset(w * 0.6f, h * 0.46f), 1.5f * density, alpha = 0.6f * f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("tmjadid2", 0, 0), Offset(w * 0.3f, h * 0.66f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("tmjadid2", 0, 1), Offset(w * 0.7f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            plain97(Offset(w * 0.25f, h * 0.3f), w * 0.07f, a, "tmjadid3", 0, f * seg(p, 0.05f, 0.25f))
            val go = easeInOut(seg(p, 0.3f, 0.85f))
            for (k in 0 until 5) person99(Offset(mix(w * 0.2f, w * 0.7f, go) - k * w * 0.04f, ground), h * 0.08f, Color(0xFFC8E8D0), f, walk = a.t * 2f)
            house12(Offset(w * 0.85f, ground), w * 0.12f, Warm103, Trim103, f, lit = seg(p, 0.7f, 0.9f))
            tagAt(a.tag("tmjadid3", 0, 1), Offset(w * 0.85f, ground - h * 0.22f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tm-usat ═════════════
internal fun DrawScope.tmusatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmusat", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { street103(a, ground) },
        { p, f ->
            veil103(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.4f), w * 0.12f, Qn.Rose, f * seg(p, 0.05f, 0.25f), a.t)
            softGlow(Offset(w * 0.5f, h * 0.4f), w * 0.16f, Qn.Rose, 0.3f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("tmusat0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val near = easeInOut(seg(p, 0.1f, 0.45f))
            sirajMunir(Offset(w * 0.32f, ground - h * 0.16f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.32f, h * 0.14f))
            person99(Offset(mix(w * 0.8f, w * 0.5f, near), ground), h * 0.1f, Color(0xFFB8B4C0), f, walk = a.t * 2f * (1f - near))
            for (k in 0 until 3) drawArc(Qn.GoldLight, -40f, 80f, false, Offset(w * 0.34f - w * 0.03f * (k + 1), ground - h * 0.16f - h * 0.03f * (k + 1)), Size(w * 0.06f * (k + 1), h * 0.06f * (k + 1)), alpha = 0.5f * f * seg(p, 0.5f + 0.06f * k, 0.65f + 0.06f * k), style = Stroke(1.3f * density))
            tagAt(a.tag("tmusat1", 0, 2), Offset(w * 0.6f, ground - h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmusat1", 0, 1), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.42f, ground - h * 0.16f), w * 0.03f, f, a.t, named = false)
            person99(Offset(w * 0.52f, ground), h * 0.1f, Color(0xFFB8B4C0), f)
            softGlow(Offset(w * 0.52f, ground - h * 0.08f), w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.2f, 0.5f))
            qIcon("hands", Offset(w * 0.5f, h * 0.36f), w * 0.08f, Qn.Jade, f * seg(p, 0.4f, 0.6f), a.t)
            tagAt(a.tag("tmusat2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            for (k in 0 until 14) person99(Offset(w * (0.08f + 0.065f * k), ground), h * 0.08f, Color(0xFFB8B4C0), f)
            sirajMunir(Offset(w * 0.5f, h * 0.3f), w * 0.035f, f, a.t, named = false)
            for (k in 0 until 3) drawArc(Qn.GoldLight, 30f, 120f, false, Offset(w * 0.5f - w * 0.1f * (k + 1), h * 0.3f - h * 0.05f * (k + 1)), Size(w * 0.2f * (k + 1), h * 0.1f * (k + 1)), alpha = 0.45f * f * seg(p, 0.3f + 0.08f * k, 0.45f + 0.08f * k), style = Stroke(1.4f * density))
            tagAt(a.tag("tmusat3", 0, 0), Offset(w * 0.5f, ground - h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmusat3", 0, 1), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tm-nisa ═════════════
internal fun DrawScope.tmnisaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmnisa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB8A070)) },
        { p, f ->
            kaaba29(Offset(w * 0.2f, ground), w * 0.07f, f)
            for (k in 0 until 16) person99(Offset(w * (0.32f + 0.04f * k), ground + h * 0.02f * (k % 2)), h * 0.05f, Color(0xFFF2EEE4), f)
            sirajMunir(Offset(w * 0.6f, h * 0.4f), w * 0.035f, f, a.t, named = false)
            tagAt(a.tag("tmnisa0", 0, 0), Offset(w * 0.2f, ground - h * 0.22f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("tmnisa0", 0, 1), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil103(f)
            for (k in 0 until 3) person99(Offset(w * (0.2f + 0.07f * k), h * 0.7f), h * 0.1f, Qn.Rose, f * seg(p, 0.05f, 0.25f))
            for (k in 0 until 3) person99(Offset(w * (0.66f + 0.07f * k), h * 0.7f), h * 0.1f, Color(0xFFB8C8E0), f * seg(p, 0.2f, 0.4f))
            balance12(Offset(w * 0.5f, h * 0.62f), w * 0.1f, 0f, Qn.GoldLight, f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("tmnisa1", 0, 0), Offset(w * 0.27f, h * 0.52f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmnisa1", 0, 1), Offset(w * 0.73f, h * 0.52f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmnisa1", 0, 2), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil103(f)
            for (k in 0 until 3) {
                val c = Offset(w * (0.3f + 0.2f * k), h * 0.5f)
                drawRoundRect(Color(0xFFBFE0F0), Offset(c.x - w * 0.04f, c.y - h * 0.08f), Size(w * 0.08f, h * 0.14f), CornerRadius(w * 0.03f), alpha = 0.5f * f * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k))
                drawRoundRect(Color(0xFFE8F4FF), Offset(c.x - w * 0.04f, c.y - h * 0.08f), Size(w * 0.08f, h * 0.14f), CornerRadius(w * 0.03f), alpha = f * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k), style = Stroke(1.5f * density))
            }
            softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.2f, Qn.GoldLight, 0.3f * f * seg(p, 0.45f, 0.7f))
            tagAt(a.tag("tmnisa2", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF8A6A40), Offset(w * 0.2f, ground - h * 0.24f), Size(w * 0.6f, h * 0.24f), alpha = 0.5f * f)
            for (k in 0 until 6) pray12(Offset(w * (0.3f + 0.07f * k), ground), h * 0.07f, Color(0xFFF2EEE4), f)
            sirajMunir(Offset(w * 0.25f, ground - h * 0.12f), w * 0.026f, f, a.t, named = false)
            qIcon("child", Offset(w * 0.86f, h * 0.5f), w * 0.07f, Kid103, f * seg(p, 0.1f, 0.25f), a.t)
            for (k in 0 until 2) drawArc(Kid103, 150f, 60f, false, Offset(w * 0.86f - w * 0.04f * (k + 1), h * 0.5f - h * 0.04f * (k + 1)), Size(w * 0.08f * (k + 1), h * 0.08f * (k + 1)), alpha = 0.6f * f * seg(p, 0.2f, 0.35f), style = Stroke(1.3f * density))
            tagAt(a.tag("tmnisa3", 0, 0), Offset(w * 0.82f, h * 0.36f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmnisa3", 0, 1), Offset(w * 0.45f, h * 0.36f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tm-kibar ═════════════
internal fun DrawScope.tmkibarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmkibar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { street103(a, ground) },
        { p, f ->
            kid103(Offset(w * 0.22f, ground), f * seg(p, 0.05f, 0.2f))
            person99(Offset(w * 0.5f, ground), h * 0.09f, Qn.Rose, f * seg(p, 0.25f, 0.4f))
            person99(Offset(w * 0.78f, ground), h * 0.085f, Color(0xFFD0CCC4), f * seg(p, 0.45f, 0.6f), lean = 0.2f)
            for (k in 0 until 3) tagAt(a.tag("tmkibar0", 0, k), Offset(w * (0.22f + 0.28f * k), ground - h * 0.2f), f * seg(p, 0.05f + 0.2f * k, 0.2f + 0.2f * k), ink = ArtInk.Ivory)
            drawArc(Qn.GoldLight, 200f, 140f, false, Offset(w * 0.12f, ground - h * 0.4f), Size(w * 0.76f, h * 0.3f), alpha = 0.6f * f * seg(p, 0.65f, 0.85f), style = Stroke(2f * density))
            tagAt(a.tag("tmkibar0", 0, 3), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil103(f)
            kid103(Offset(w * 0.3f, h * 0.7f), f * seg(p, 0.05f, 0.2f))
            person99(Offset(w * 0.7f, h * 0.7f), h * 0.11f, Color(0xFFD0CCC4), f * seg(p, 0.4f, 0.55f), lean = 0.2f)
            qIcon("heart", Offset(w * 0.3f, h * 0.38f), w * 0.07f, Qn.Rose, f * seg(p, 0.15f, 0.3f), a.t)
            qIcon("crown", Offset(w * 0.7f, h * 0.38f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("tmkibar1", 0, 0), Offset(w * 0.3f, h * 0.5f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmkibar1", 0, 1), Offset(w * 0.7f, h * 0.5f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 3) person99(Offset(w * (0.3f + 0.1f * k), ground), h * (0.08f + 0.01f * k), Color(0xFFB8B4C0), f * if (k == 2) 1f else 0.6f)
            sirajMunir(Offset(w * 0.75f, ground - h * 0.16f), w * 0.028f, f, a.t, named = false)
            for (k in 0 until 2) drawArc(Qn.GoldLight, 150f, 60f, false, Offset(w * 0.73f - w * 0.04f * (k + 1), ground - h * 0.16f - h * 0.04f * (k + 1)), Size(w * 0.08f * (k + 1), h * 0.08f * (k + 1)), alpha = 0.5f * f * seg(p, 0.2f, 0.4f), style = Stroke(1.3f * density))
            tagAt(a.tag("tmkibar2", 0, 0), Offset(w * 0.6f, h * 0.36f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil103(f)
            plain97(Offset(w * 0.32f, h * 0.45f), w * 0.08f, a, "tmkibar3", 0, f * seg(p, 0.05f, 0.25f))
            sirajMunir(Offset(w * 0.7f, h * 0.42f), w * 0.04f, f * seg(p, 0.2f, 0.4f), a.t, named = false)
            qIcon("check", Offset(w * 0.32f, h * 0.24f), w * 0.07f, Qn.Jade, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("tmkibar3", 0, 1), Offset(w * 0.32f, h * 0.66f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tm-sighar ═════════════
internal fun DrawScope.tmsigharScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "tmsighar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { street103(a, ground) },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, ground - h * 0.18f), w * 0.03f, f, a.t, named = false)
            for (k in 0 until 6) {
                val ang = PI.toFloat() * (0.1f + 0.16f * k)
                kid103(Offset(w * 0.5f - cos(ang) * w * 0.3f, ground), f * seg(p, 0.15f + 0.05f * k, 0.3f + 0.05f * k), walk = a.t * 2f)
            }
            qIcon("heart", Offset(w * 0.5f, h * 0.3f), w * 0.08f, Qn.Rose, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tmsighar0", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmsighar0", 0, 1), Offset(w * 0.8f, ground - h * 0.16f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.35f, ground - h * 0.18f), w * 0.03f, f, a.t, named = false)
            kid103(Offset(w * 0.6f, ground), f)
            tagAt(a.tag("tmsighar1", 0, 0), Offset(w * 0.6f, ground - h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmsighar1", 0, 1), Offset(w * 0.45f, h * 0.3f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, ground - h * 0.22f), w * 0.03f, f, a.t, named = false)
            for (k in 0 until 4) {
                val c = Offset(w * (0.26f + 0.16f * k), ground)
                kid103(c, f)
                softGlow(c + Offset(0f, -h * 0.06f), w * 0.04f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f + 0.1f * k, 0.45f + 0.1f * k))
            }
            tagAt(a.tag("tmsighar2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("tmsighar2", 0, 1), Offset(w * 0.5f, ground + h * 0.06f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 4) palm12(Offset(w * (0.1f + 0.2f * k), ground), h * 0.26f, Color(0xFF6A4A30), Qn.Leaf, f, a.t)
            kid103(Offset(w * 0.55f, ground), f)
            sirajMunir(Offset(w * 0.78f, ground - h * 0.16f), w * 0.028f, f * seg(p, 0.3f, 0.5f), a.t, named = false)
            qIcon("heart", Offset(w * 0.66f, h * 0.36f), w * 0.06f, Qn.Rose, f * seg(p, 0.6f, 0.75f), a.t)
            tagAt(a.tag("tmsighar3", 0, 0), Offset(w * 0.3f, h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmsighar3", 0, 1), Offset(w * 0.55f, ground - h * 0.12f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("tmsighar3", 0, 2), Offset(w * 0.66f, h * 0.48f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}
