package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 116 (2026-11-17): al-Bukhari's al-Adab al-Mufrad: soft words to a mother, what no child repays, a parent's
 * prayer (Jurayj), the womb that seeks refuge, daughters, mercy, the neighbour. People are faceless; named companions
 * are roundels (Fatima: family roundel, Aisha: her Mother-of-the-Believers roundel, others plain); the Prophet ﷺ is
 * only his named light.
 */

private val Fig116 = Color(0xFFEDE4D0)
private val Mom116 = Color(0xFFD8C2E0)
private val Wall116 = Color(0xFFE2CCA0)
private val Trim116 = Color(0xFF8A6A40)

private fun DrawScope.day116(a: ArtCtx): Float {
    val g = size.height * 0.8f
    daySky12(Qn.DayTop, Qn.DayLow, g, Color(0xFFB09868))
    return g
}

private fun DrawScope.night116(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil116(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row116(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ArtInk.Ivory)
    }
}

/** Soft speech: three arcs opening from a mouth point. */
private fun DrawScope.speech116(c: Offset, s: Float, dir: Float, color: Color, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 3) {
        val ph = ((t * 0.8f + k / 3f) % 1f)
        val r = s * (0.3f + ph)
        drawArc(color, if (dir > 0) -40f else 140f, 80f, false, Offset(c.x - r, c.y - r), Size(2 * r, 2 * r),
            alpha = alpha * (1f - ph), style = Stroke(s * 0.12f, cap = StrokeCap.Round))
    }
}

// ═════════════ am-layyin ═════════════
internal fun DrawScope.amlayyinScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "amlayyin", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day116(a) },
        { p, f ->
            plain97(Offset(w * 0.7f, h * 0.36f), w * 0.09f, a, "amlayyin0", 0, f * seg(p, 0.05f, 0.25f))
            person99(Offset(mix(w * 0.05f, w * 0.38f, easeInOut(seg(p, 0.1f, 0.6f))), g), h * 0.13f, Fig116, f, walk = a.t * 2f, lean = 0.15f)
            for (k in 0 until 3) drawCircle(Color(0xFF3A2E40), w * (0.02f + 0.006f * k), Offset(w * (0.24f + 0.05f * k), h * 0.5f), alpha = 0.5f * f * seg(p, 0.2f, 0.4f))
            tagAt(a.tag("amlayyin0", 0, 1), Offset(w * 0.3f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            house12(Offset(w * 0.5f, g), w * 0.5f, Wall116, Trim116, 0.45f * f, lit = 0.5f)
            woman12(Offset(w * 0.64f, g), h * 0.15f, Mom116, f)
            person99(Offset(w * 0.38f, g), h * 0.14f, Fig116, f, lean = 0.12f)
            speech116(Offset(w * 0.44f, g - h * 0.17f), w * 0.05f, 1f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("cup", Offset(w * 0.51f, g - h * 0.09f), w * 0.06f, Qn.Gold, f * seg(p, 0.35f, 0.5f), a.t)
            row116(a, "amlayyin1", 2, h * 0.2f, p, f, from = 0.15f, step = 0.2f)
            softGlow(Offset(w * 0.5f, h * 0.08f), w * 0.3f, Qn.GoldLight, 0.6f * f * seg(p, 0.65f, 0.85f))
            tagAt(a.tag("amlayyin1", 0, 2), Offset(w * 0.5f, h * 0.31f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil116(f)
            woman12(Offset(w * 0.5f, g), h * 0.17f, Mom116, f)
            for (k in 0 until 4) {
                val ph = ((a.t * 0.5f + k / 4f) % 1f)
                qIcon("drop", Offset(w * (0.47f + 0.06f * (k % 2)), g - h * 0.19f + ph * h * 0.12f), w * 0.025f, Color(0xFF8FC1E3), f * (1f - ph) * seg(p, 0.15f, 0.3f), a.t)
            }
            tagAt(a.tag("amlayyin2", 0, 0), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil116(f)
            for (i in 0 until 4) {
                val y = h * (0.22f + 0.13f * i)
                tagAt(a.tag("amlayyin3", 0, i), Offset(w * 0.5f, y), f * seg(p, 0.08f + 0.18f * i, 0.22f + 0.18f * i), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ am-jaza ═════════════
internal fun DrawScope.amjazaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "amjaza", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day116(a) },
        { p, f ->
            val tilt = 0.18f * easeInOut(seg(p, 0.2f, 0.6f))
            val c = Offset(w * 0.5f, h * 0.36f)
            drawLine(Trim116, Offset(c.x, c.y), Offset(c.x, g), w * 0.012f, alpha = f)
            val l = Offset(c.x - w * 0.28f * cos(tilt), c.y - w * 0.28f * sin(tilt))
            val r = Offset(c.x + w * 0.28f * cos(tilt), c.y + w * 0.28f * sin(tilt))
            drawLine(Trim116, l, r, w * 0.01f, alpha = f, cap = StrokeCap.Round)
            woman12(Offset(r.x, r.y + h * 0.16f), h * 0.12f, Mom116, f)
            person99(Offset(l.x, l.y + h * 0.16f), h * 0.1f, Fig116, f)
            softGlow(Offset(r.x, r.y + h * 0.08f), w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.5f, 0.7f))
            tagAt(a.tag("amjaza0", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            kaaba29(Offset(w * 0.5f, g - h * 0.02f), w * 0.22f, f)
            val ang = a.t * 0.5f
            val x = w * 0.5f + w * 0.32f * cos(ang)
            val y = g + h * 0.04f * sin(ang)
            person99(Offset(x, y), h * 0.12f, Fig116, f, walk = a.t * 2f, lean = 0.25f)
            woman12(Offset(x - w * 0.02f, y - h * 0.1f), h * 0.08f, Mom116, f)
            row116(a, "amjaza1", 2, h * 0.2f, p, f, from = 0.2f, step = 0.25f)
        },
        { p, f ->
            veil116(f)
            val path = Path()
            val n = 40
            for (i in 0..n) {
                val x = w * (0.15f + 0.7f * i / n)
                val amp = h * 0.06f * sin(i / n.toFloat() * 3.1416f)
                val y = h * 0.5f + amp * sin(i * 0.9f + a.t * 3f)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path, Qn.GoldLight, alpha = f * seg(p, 0.1f, 0.3f), style = Stroke(w * 0.008f, cap = StrokeCap.Round))
            tagAt(a.tag("amjaza2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("amjaza2", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            house12(Offset(w * 0.3f, g), w * 0.34f, Wall116, Trim116, f, lit = 0.6f)
            for (k in 0 until 2) person99(Offset(w * (0.24f + 0.12f * k), g), h * 0.11f, if (k == 0) Fig116 else Mom116, f)
            val back = easeInOut(seg(p, 0.1f, 0.55f))
            person99(Offset(mix(w * 0.88f, w * 0.52f, back), g), h * 0.12f, Fig116, f, walk = a.t * 2f * (1f - back))
            for (k in 0 until 2) {
                val c = Offset(w * (0.24f + 0.12f * k), g - h * 0.17f)
                qIcon("drop", c, w * 0.025f, Color(0xFF8FC1E3), f * (1f - seg(p, 0.5f, 0.65f)), a.t)
                qIcon("sun", c, w * 0.04f, Qn.Gold, f * seg(p, 0.6f, 0.75f), a.t)
            }
            tagAt(a.tag("amjaza3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ am-dawa ═════════════
private fun DrawScope.tower116(foot: Offset, s: Float, alpha: Float, fall: Float = 0f) {
    if (alpha <= 0.01f) return
    for (k in 0 until 6) {
        val drop = fall * (k + 1) * s * 0.25f
        val dx = fall * (k - 2.5f) * s * 0.18f
        val top = foot.y - s * 0.32f * (k + 1) + drop
        drawRoundRect(if (k % 2 == 0) Color(0xFFCDB58A) else Color(0xFFB89E72), Offset(foot.x - s * 0.22f + dx, top), Size(s * 0.44f, s * 0.3f),
            CornerRadius(s * 0.03f), alpha = alpha * (1f - 0.4f * fall))
    }
    if (fall < 0.05f) drawRoundRect(Color(0xFFFFE2A0), Offset(foot.x - s * 0.06f, foot.y - s * 1.75f), Size(s * 0.12f, s * 0.18f), CornerRadius(s * 0.06f), alpha = alpha)
}

internal fun DrawScope.amdawaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "amdawa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night116(a) },
        { p, f ->
            for (i in 0 until 3) {
                val x = w * (0.78f - 0.28f * i)
                if (i == 2) woman12(Offset(x, g), h * 0.12f, Mom116, f) else person99(Offset(x, g), h * 0.12f, Fig116, f, arms = 0.8f)
                if (i == 1) drawLine(Trim116, Offset(x + w * 0.04f, g), Offset(x + w * 0.05f, g - h * 0.15f), w * 0.008f, alpha = f)
                val up = seg(p, 0.15f + 0.15f * i, 0.5f + 0.15f * i)
                drawLine(Qn.GoldLight, Offset(x, g - h * 0.17f), Offset(x, mix(g - h * 0.17f, h * 0.06f, up)), w * 0.006f, alpha = 0.7f * f, cap = StrokeCap.Round)
                tagAt(a.tag("amdawa0", 0, i), Offset(x, g + h * 0.08f), f * seg(p, 0.1f + 0.15f * i, 0.25f + 0.15f * i), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            tower116(Offset(w * 0.62f, g), h * 0.3f, f)
            woman12(Offset(w * 0.3f, g), h * 0.13f, Mom116, f)
            for (k in 0 until 3) {
                speech116(Offset(w * 0.35f, g - h * 0.15f), w * 0.04f, 1f, Qn.GoldLight, f * seg(p, 0.1f + 0.22f * k, 0.2f + 0.22f * k) * (1f - seg(p, 0.3f + 0.22f * k, 0.4f + 0.22f * k)), a.t)
                drawCircle(Qn.Gold, w * 0.012f, Offset(w * (0.24f + 0.05f * k), h * 0.3f), alpha = f * seg(p, 0.15f + 0.22f * k, 0.25f + 0.22f * k))
            }
            tagAt(a.tag("amdawa1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            tower116(Offset(w * 0.62f, g), h * 0.3f, f, fall = easeInOut(seg(p, 0.25f, 0.6f)))
            person99(Offset(w * 0.3f, g), h * 0.12f, Fig116, f, lean = -0.1f)
            for (k in 0 until 3) person99(Offset(w * (0.82f + 0.06f * k), g), h * 0.1f, Color(0xFFB8A890), f * seg(p, 0.05f, 0.2f), arms = 0.6f)
            tagAt(a.tag("amdawa2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil116(f)
            val c = Offset(w * 0.5f, h * 0.55f)
            drawOval(Color(0xFFF3E6C4), Offset(c.x - w * 0.1f, c.y - h * 0.035f), Size(w * 0.2f, h * 0.07f), alpha = f)
            drawCircle(Color(0xFFF3E6C4), w * 0.035f, Offset(c.x + w * 0.11f, c.y - h * 0.01f), alpha = f)
            speech116(Offset(c.x + w * 0.15f, c.y - h * 0.02f), w * 0.05f, 1f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            tagAt(a.tag("amdawa3", 0, 0), Offset(w * 0.5f, h * 0.75f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            softGlow(c, w * 0.25f, Qn.GoldLight, 0.4f * f * seg(p, 0.55f, 0.75f))
            tagAt(a.tag("amdawa3", 0, 1), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ am-rahim ═════════════
internal fun DrawScope.amrahimScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "amrahim", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night116(a) },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.42f)
            softGlow(c, w * 0.22f, Qn.Rose, 0.5f * f * seg(p, 0.05f, 0.3f))
            qIcon("chain", c, w * 0.2f, Qn.GoldLight, f * seg(p, 0.1f, 0.3f), a.t)
            tagAt(a.tag("amrahim0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("amrahim0", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val cut = seg(p, 0.5f, 0.65f)
            for (side in 0 until 2) {
                val x = w * (0.75f - 0.5f * side)
                person99(Offset(x, g), h * 0.12f, Fig116, f * (if (side == 1) 1f - 0.4f * cut else 1f))
                val mid = Offset(w * 0.5f, g - h * 0.12f)
                val end = Offset(x, g - h * 0.12f)
                if (side == 0 || cut < 0.5f) drawLine(Qn.Rose, mid, end, w * 0.008f, alpha = f * seg(p, 0.05f, 0.2f), cap = StrokeCap.Round)
                else drawLine(Qn.Rose, mid, Offset(mix(mid.x, end.x, 0.4f), mid.y + h * 0.04f * cut), w * 0.008f, alpha = f * 0.5f, cap = StrokeCap.Round)
            }
            qIcon("chain", Offset(w * 0.5f, g - h * 0.12f), w * 0.08f, Qn.GoldLight, f, a.t)
            softGlow(Offset(w * 0.75f, g - h * 0.2f), w * 0.1f, Qn.GoldLight, 0.6f * f * seg(p, 0.25f, 0.4f))
            row116(a, "amrahim1", 2, h * 0.24f, p, f, from = 0.2f, step = 0.35f)
        },
        { p, f ->
            veil116(f)
            for (k in 0 until 3) drawCircle(Qn.Smoke, w * (0.12f + 0.03f * k), Offset(w * (0.38f + 0.12f * k), h * 0.24f), alpha = 0.8f * f * seg(p, 0.1f, 0.3f))
            val bolt = seg(p, 0.45f, 0.5f) * (1f - seg(p, 0.6f, 0.7f))
            drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.3f), Offset(w * 0.46f, h * 0.45f), w * 0.01f, alpha = f * bolt)
            drawLine(Qn.GoldLight, Offset(w * 0.46f, h * 0.45f), Offset(w * 0.52f, h * 0.5f), w * 0.01f, alpha = f * bolt)
            person99(Offset(w * 0.5f, g), h * 0.12f, Fig116, f, lean = -0.1f)
            row116(a, "amrahim2", 2, h * 0.66f, p, f, from = 0.2f, step = 0.2f)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.3f), w * 0.04f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.12f))
            drawPath(Path().apply { moveTo(w * 0.2f, g); quadraticBezierTo(w * 0.5f, h * 0.36f, w * 0.8f, g); close() }, Color(0xFF4A3A2A), alpha = f)
            for (k in 0 until 5) person99(Offset(w * (0.1f + 0.2f * k), g + h * 0.08f), h * 0.08f, Fig116, f * seg(p, 0.1f + 0.06f * k, 0.25f + 0.06f * k))
            tagAt(a.tag("amrahim3", 0, 0), Offset(w * 0.5f, h * 0.52f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            close97(Offset(w * 0.8f, h * 0.42f), w * 0.08f, a, "amrahim3", 1, 3, f * seg(p, 0.55f, 0.75f))
        },
    )
}

// ═════════════ am-banat ═════════════
internal fun DrawScope.ambanatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "ambanat", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day116(a) },
        { p, f ->
            person99(Offset(w * 0.78f, g), h * 0.14f, Fig116, f, arms = 0.5f)
            for (k in 0 until 3) woman12(Offset(w * (0.6f - 0.14f * k), g), h * 0.09f, listOf(Qn.Rose, Mom116, Qn.GoldLight)[k], f)
            val shield = seg(p, 0.45f, 0.7f)
            drawArc(Qn.GoldLight, 200f, 140f, false, Offset(w * 0.12f, g - h * 0.32f), Size(w * 0.76f, h * 0.5f), alpha = 0.8f * f * shield, style = Stroke(w * 0.015f, cap = StrokeCap.Round))
            row116(a, "ambanat0", 3, h * 0.14f, p, f, from = 0.1f, step = 0.22f)
        },
        { p, f ->
            house12(Offset(w * 0.5f, g), w * 0.55f, Wall116, Trim116, f, lit = 0.7f)
            person99(Offset(w * 0.62f, g), h * 0.13f, Fig116, f, arms = 0.4f)
            for (k in 0 until 2) woman12(Offset(w * (0.46f - 0.1f * k), g), h * 0.08f, if (k == 0) Qn.Rose else Mom116, f)
            row116(a, "ambanat1", 3, h * 0.16f, p, f, from = 0.1f, step = 0.15f)
            tagAt(a.tag("ambanat1", 0, 3), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            plain97(Offset(w * 0.72f, h * 0.36f), w * 0.09f, a, "ambanat2", 0, f * seg(p, 0.05f, 0.25f))
            for (k in 0 until 6) {
                val ph = ((a.t * 0.3f + k / 6f) % 1f)
                qIcon("wheat", Offset(w * (0.18f + 0.06f * k), mix(h * 0.08f, g - h * 0.05f, ph)), w * 0.04f, Qn.Gold, f * seg(p, 0.4f, 0.55f) * (1f - ph), a.t)
            }
            for (k in 0 until 3) woman12(Offset(w * (0.2f + 0.12f * k), g), h * 0.08f, Mom116, f)
            tagAt(a.tag("ambanat2", 0, 1), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil116(f)
            val close = easeInOut(seg(p, 0.2f, 0.6f))
            for (s in 0 until 2) {
                val x0 = if (s == 0) 0f else w
                val x1 = w * 0.5f
                drawRect(Color(0xFFC9A86A), Offset(if (s == 0) x0 else mix(x0, x1, close), h * 0.15f), Size(mix(0f, w * 0.5f, close), h * 0.55f), alpha = 0.85f * f)
            }
            drawRect(Color(0xFFE8794A), Offset(0f, h * 0.72f), Size(w, h * 0.08f), alpha = 0.5f * f * (1f - close))
            tagAt(a.tag("ambanat3", 0, 0), Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ am-rahma ═════════════
internal fun DrawScope.amrahmaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "amrahma", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day116(a) },
        { p, f ->
            val up = easeInOut(seg(p, 0.2f, 0.55f))
            person99(Offset(w * 0.62f, g), h * 0.13f, Fig116, f, lean = 0.3f * (1f - up), arms = 0.7f)
            person99(Offset(w * 0.42f, g), h * 0.13f * mix(0.45f, 1f, up), Color(0xFFB8A890), f)
            softGlow(Offset(w * 0.52f, g - h * 0.12f), w * 0.12f, Qn.Rose, 0.5f * f * seg(p, 0.4f, 0.6f))
            qIcon("heart", Offset(w * 0.52f, g - h * 0.26f), w * 0.06f, Qn.Rose, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("amrahma0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            house12(Offset(w * 0.5f, g), w * 0.5f, Wall116, Trim116, 0.4f * f, lit = 0.4f)
            person99(Offset(w * 0.58f, g), h * 0.14f, Fig116, f, lean = 0.2f * seg(p, 0.2f, 0.4f))
            child12(Offset(w * 0.46f, g), h * 0.11f, Mom116, f)
            qIcon("heart", Offset(w * 0.5f, g - h * 0.19f), w * 0.04f, Qn.Rose, f * seg(p, 0.35f, 0.5f), a.t)
            person99(Offset(w * 0.18f, g), h * 0.13f, Color(0xFFB8A890), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("amrahma1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil116(f)
            val fallP = easeInOut(seg(p, 0.45f, 0.8f))
            for (i in 0 until 100) {
                val col = i % 10
                val row = i / 10
                var c = Offset(w * (0.27f + 0.05f * col), h * (0.12f + 0.035f * row))
                if (i == 99) c = Offset(c.x, mix(c.y, g - h * 0.02f, fallP))
                drawCircle(if (i == 99) Qn.GoldLight else Qn.Gold, w * 0.011f, c, alpha = f * seg(p, 0.02f + 0.002f * i, 0.1f + 0.002f * i))
            }
            if (fallP > 0.95f) softGlow(Offset(w * 0.72f, g), w * 0.2f, Qn.GoldLight, 0.5f * f)
            tagAt(a.tag("amrahma2", 0, 0), Offset(w * 0.12f, h * 0.2f), f * seg(p, 0.2f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("amrahma2", 0, 1), Offset(w * 0.12f, h * 0.34f), f * seg(p, 0.35f, 0.45f), ink = ArtInk.Gold)
            tagAt(a.tag("amrahma2", 0, 2), Offset(w * 0.5f, g + h * 0.08f), f * seg(p, 0.8f, 0.9f), ink = ArtInk.Gold)
        },
        { p, f ->
            softGlow(Offset(w * 0.5f, h * 0.45f), w * 0.4f, Qn.GoldLight, 0.35f * f)
            qIcon("nest", Offset(w * 0.32f, h * 0.48f), w * 0.14f, Qn.Soil, f * seg(p, 0.05f, 0.2f), a.t)
            woman12(Offset(w * 0.62f, g), h * 0.13f, Mom116, f * seg(p, 0.2f, 0.35f))
            child12(Offset(w * 0.7f, g), h * 0.09f, Fig116, f * seg(p, 0.25f, 0.4f))
            tagAt(a.tag("amrahma3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ am-jar ═════════════
internal fun DrawScope.amjarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "amjar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day116(a) },
        { p, f ->
            house12(Offset(w * 0.72f, g), w * 0.36f, Wall116, Trim116, f, lit = 0.8f)
            house12(Offset(w * 0.28f, g), w * 0.32f, Color(0xFFC8B48A), Trim116, f, lit = 0.1f)
            person99(Offset(w * 0.28f, g), h * 0.1f, Color(0xFFB8A890), f, lean = 0.1f)
            qIcon("cup", Offset(w * 0.34f, g - h * 0.05f), w * 0.04f, Trim116, f, a.t)
            val give = easeInOut(seg(p, 0.45f, 0.8f))
            qIcon("wheat", Offset(mix(w * 0.66f, w * 0.34f, give), g - h * 0.14f - h * 0.08f * sin(give * 3.1416f)), w * 0.05f, Qn.Gold, f * seg(p, 0.4f, 0.5f), a.t)
            tagAt(a.tag("amjar0", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val pot = Offset(w * 0.5f, h * 0.5f)
            drawRoundRect(Color(0xFF6B4F35), Offset(pot.x - w * 0.12f, pot.y), Size(w * 0.24f, h * 0.12f), CornerRadius(w * 0.05f), alpha = f)
            val lvl = easeInOut(seg(p, 0.1f, 0.4f))
            drawRect(Color(0xFFC0884A), Offset(pot.x - w * 0.11f, pot.y + h * 0.04f - h * 0.03f * lvl), Size(w * 0.22f, h * 0.02f), alpha = f)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.4f + k / 3f) % 1f)
                drawCircle(Color(0xFFF3E6C4), w * (0.01f + 0.02f * ph), Offset(pot.x + w * (-0.05f + 0.05f * k), pot.y - h * 0.04f * ph), alpha = 0.6f * f * (1f - ph))
            }
            for (k in 0 until 3) {
                val x = w * (0.82f - 0.32f * k)
                house12(Offset(x, g), w * 0.2f, Wall116, Trim116, f * seg(p, 0.45f + 0.1f * k, 0.55f + 0.1f * k), lit = 0.5f)
                qIcon("cup", Offset(x, g - h * 0.18f), w * 0.035f, Qn.Gold, f * seg(p, 0.55f + 0.1f * k, 0.65f + 0.1f * k), a.t)
            }
            row116(a, "amjar1", 2, h * 0.2f, p, f, from = 0.15f, step = 0.35f)
        },
        { p, f ->
            for (s in 0 until 2) {
                val x = w * (0.72f - 0.44f * s)
                door12(Offset(x, g), w * 0.18f, h * 0.3f, 0.8f, Trim116, Color(0xFF6B4F35), f)
                woman12(Offset(x + (if (s == 0) -1f else 1f) * w * 0.1f, g), h * 0.12f, if (s == 0) Mom116 else Qn.Rose, f)
            }
            val give = easeInOut(seg(p, 0.3f, 0.7f))
            qIcon("heart", Offset(mix(w * 0.6f, w * 0.4f, give), g - h * 0.13f), w * 0.035f, Qn.Rose, f * seg(p, 0.25f, 0.35f), a.t)
            tagAt(a.tag("amjar2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            mother102(Offset(w * 0.5f, h * 0.18f), w * 0.08f, a, "amjar3", 0, 1, f * seg(p, 0.05f, 0.25f))
            house12(Offset(w * 0.5f, g), w * 0.28f, Wall116, Trim116, f, lit = 0.5f)
            for (s in 0 until 2) {
                val x = w * (if (s == 0) 0.22f else 0.92f)
                house12(Offset(x, g), w * 0.22f, Color(0xFFC8B48A), Trim116, f)
            }
            softGlow(Offset(w * 0.22f, g - h * 0.08f), w * 0.12f, Qn.GoldLight, 0.7f * f * seg(p, 0.45f, 0.65f))
            tagAt(a.tag("amjar3", 0, 1), Offset(w * 0.3f, h * 0.48f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}
