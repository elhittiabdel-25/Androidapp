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
 * Wave 125 (2026-11-18): Aysar al-Tafasir on al-Dukhan, al-Jathiya and al-Ahqaf: the blessed night and the year's
 * decrees, the smoke of hunger, the tree of zaqqum, the straight shari'a, the dahriyya, the excuse of the disbelievers,
 * and «أذهبتم طيباتكم». The Prophet ﷺ is only his named light, 'Umar his roundel, people faceless.
 */

private val Fig125 = Color(0xFFEDE4D0)
private val Dim125 = Color(0xFF8A8296)
private val Mom125 = Color(0xFFD8C2E0)

private fun DrawScope.night125(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil125(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row125(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

private fun DrawScope.tree125(foot: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawLine(Color(0xFF2A1E18), foot, Offset(foot.x, foot.y - s), s * 0.08f, alpha = alpha, cap = StrokeCap.Round)
    for (k in 0 until 6) {
        val y = foot.y - s * (0.4f + 0.1f * k)
        val sx = if (k % 2 == 0) 1f else -1f
        val tip = Offset(foot.x + sx * s * (0.45f - 0.05f * k), y - s * 0.15f)
        drawLine(Color(0xFF2A1E18), Offset(foot.x, y), tip, s * 0.04f, alpha = alpha, cap = StrokeCap.Round)
        for (j in 0 until 3) drawLine(Color(0xFF3A2A20), Offset(mix(foot.x, tip.x, 0.3f + 0.25f * j), mix(y, tip.y, 0.3f + 0.25f * j)),
            Offset(mix(foot.x, tip.x, 0.3f + 0.25f * j), mix(y, tip.y, 0.3f + 0.25f * j) - s * 0.06f), s * 0.015f, alpha = alpha)
    }
}

// ═════════════ ay-qadr ═════════════
internal fun DrawScope.ayqadrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ayqadr", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night125(a) },
        { p, f ->
            qIcon("crescent", Offset(w * 0.78f, h * 0.16f), w * 0.07f, Qn.GoldLight, f, a.t)
            val down = easeInOut(seg(p, 0.1f, 0.6f))
            softGlow(Offset(w * 0.5f, mix(h * 0.1f, h * 0.48f, down)), w * 0.18f, Qn.GoldLight, 0.6f * f)
            qIcon("book", Offset(w * 0.5f, mix(h * 0.1f, h * 0.48f, down)), w * 0.12f, Qn.Gold, f, a.t)
            row125(a, "ayqadr0", 2, h * 0.72f, p, f, from = 0.4f, step = 0.25f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil125(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.32f), w * 0.22f, Qn.Parch, f, a.t)
            val out = easeInOut(seg(p, 0.25f, 0.75f))
            for (k in 0 until 5) qIcon("scroll", Offset(mix(w * 0.5f, w * (0.14f + 0.18f * k), out), mix(h * 0.32f, h * 0.6f, out)), w * 0.07f, Qn.ParchLow, f * seg(p, 0.2f, 0.3f), a.t)
            tagAt(a.tag("ayqadr1", 0, 0), Offset(w * 0.5f, h * 0.8f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil125(f)
            val icons = listOf("hourglass", "coin", "heart", "crown")
            for (i in 0 until 4) {
                val y = h * (0.2f + 0.17f * i)
                val q = seg(p, 0.05f + 0.2f * i, 0.2f + 0.2f * i)
                qIcon(icons[i], Offset(w * 0.8f, y), w * 0.07f, Qn.GoldLight, f * q, a.t)
                tagAt(a.tag("ayqadr2", 0, i), Offset(w * 0.45f, y), f * q, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            val g = h * 0.8f
            house12(Offset(w * 0.5f, g), w * 0.4f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.7f * (1f - seg(p, 0.5f, 0.85f)))
            qIcon("flame", Offset(w * 0.78f, g - h * 0.1f), w * 0.05f, Qn.Gold, f * (1f - seg(p, 0.5f, 0.85f)), a.t)
            tagAt(a.tag("ayqadr3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ay-dukhan ═════════════
internal fun DrawScope.aydukhanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "aydukhan", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night125(a) },
        { p, f ->
            val haze = seg(p, 0.1f, 0.5f)
            for (k in 0 until 8) drawCircle(Qn.Smoke, w * (0.1f + 0.02f * (k % 3)), Offset(w * (0.06f + 0.13f * k), h * (0.25f + 0.05f * (k % 2)) + sin(a.t + k) * h * 0.01f), alpha = 0.55f * f * haze)
            for (k in 0 until 5) person99(Offset(w * (0.16f + 0.17f * k), g), h * 0.1f, Dim125, f, lean = -0.1f)
            tagAt(a.tag("aydukhan0", 0, 0), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 8) drawCircle(Qn.Smoke, w * 0.11f, Offset(w * (0.06f + 0.13f * k), h * 0.25f), alpha = 0.5f * f)
            for (k in 0 until 5) person99(Offset(w * (0.16f + 0.17f * k), g), h * 0.1f, Dim125, f, arms = 0.9f * seg(p, 0.2f, 0.4f))
            tagAt(a.tag("aydukhan1", 0, 0), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.7f, h * 0.48f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.7f, h * 0.36f))
            for (k in 0 until 3) person99(Offset(w * (0.12f + 0.1f * k), g), h * 0.11f, Color(0xFF4A4458), f, arms = 0.5f)
            tagAt(a.tag("aydukhan2", 0, 0), Offset(w * 0.3f, h * 0.36f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            val dust = seg(p, 0.2f, 0.6f)
            for (k in 0 until 6) drawCircle(Color(0xFFC8B48A), w * 0.06f, Offset(w * (0.1f + 0.16f * k), g - h * 0.05f), alpha = 0.5f * f * dust)
            for (k in 0 until 4) person99(Offset(w * (0.62f + 0.08f * k), g), h * 0.1f, Fig125, f)
            for (k in 0 until 4) person99(Offset(w * (0.12f + 0.08f * k), g), h * 0.1f, Color(0xFF4A4458), f * (1f - 0.5f * dust))
            tagAt(a.tag("aydukhan3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ay-zaqqum ═════════════
internal fun DrawScope.ayzaqqumScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "ayzaqqum", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night125(a) },
        { p, f ->
            drawRect(Qn.Ember, Offset(0f, g), Size(w, h * 0.2f), alpha = 0.6f * f)
            tree125(Offset(w * 0.5f, g), h * 0.5f, f * seg(p, 0.05f, 0.3f))
            tagAt(a.tag("ayzaqqum0", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.4f, 0.55f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil125(f)
            drawRoundRect(Color(0xFF3A2A20), Offset(w * 0.3f, h * 0.45f), Size(w * 0.4f, h * 0.22f), CornerRadius(w * 0.06f), alpha = f)
            drawRect(Color(0xFF6B5A28), Offset(w * 0.32f, h * 0.46f), Size(w * 0.36f, h * 0.03f), alpha = f)
            for (k in 0 until 6) {
                val ph = ((a.t * 0.7f + k / 6f) % 1f)
                drawCircle(Color(0xFF8A7A38), w * (0.01f + 0.02f * ph), Offset(w * (0.36f + 0.05f * k), h * 0.46f - h * 0.05f * ph), alpha = f * (1f - ph))
            }
            drawRect(Qn.Ember, Offset(w * 0.3f, h * 0.7f), Size(w * 0.4f, h * 0.04f), alpha = 0.7f * f)
            tagAt(a.tag("ayzaqqum1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val tbl = Offset(w * 0.5f, g - h * 0.1f)
            drawRoundRect(Color(0xFF8A6A40), Offset(tbl.x - w * 0.2f, tbl.y), Size(w * 0.4f, h * 0.025f), CornerRadius(w * 0.01f), alpha = f)
            drawOval(Color(0xFFF6E6A8), Offset(tbl.x - w * 0.12f, tbl.y - h * 0.03f), Size(w * 0.08f, h * 0.03f), alpha = f)
            for (k in 0 until 4) drawOval(Color(0xFF6B3A20), Offset(tbl.x + w * (0.02f + 0.03f * k), tbl.y - h * 0.02f), Size(w * 0.025f, h * 0.018f), alpha = f)
            person99(Offset(w * 0.8f, g), h * 0.14f, Color(0xFF4A4458), f, arms = 0.7f)
            for (k in 0 until 3) child12(Offset(w * (0.2f + 0.08f * k), g), h * 0.1f, Dim125, f)
            tagAt(a.tag("ayzaqqum2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil125(f)
            person99(Offset(w * 0.5f, g), h * 0.14f, Color(0xFF4A4458), f, lean = 0.1f)
            for (k in 0 until 12) {
                val ph = ((a.t * 0.9f + k * 0.29f) % 1f)
                drawLine(Qn.Ember, Offset(w * (0.42f + 0.015f * k), h * 0.2f + ph * h * 0.35f), Offset(w * (0.42f + 0.015f * k), h * 0.23f + ph * h * 0.35f), w * 0.005f, alpha = f * seg(p, 0.15f, 0.3f))
            }
            tagAt(a.tag("ayzaqqum3", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ay-sharia ═════════════
private fun DrawScope.road125(alpha: Float) {
    val w = size.width
    val h = size.height
    drawPath(Path().apply { moveTo(w * 0.38f, h * 0.84f); lineTo(w * 0.62f, h * 0.84f); lineTo(w * 0.52f, h * 0.3f); lineTo(w * 0.48f, h * 0.3f); close() }, Color(0xFFC8B07A), alpha = alpha)
    softGlow(Offset(w * 0.5f, h * 0.28f), w * 0.15f, Qn.GoldLight, 0.7f * alpha)
}

internal fun DrawScope.ayshariaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "aysharia", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night125(a) },
        { p, f ->
            road125(f)
            val go = easeInOut(seg(p, 0.1f, 0.8f))
            person99(Offset(w * 0.5f, mix(h * 0.84f, h * 0.5f, go)), h * mix(0.13f, 0.06f, go), Fig125, f, walk = a.t * 2f)
            row125(a, "aysharia0", 2, h * 0.14f, p, f, from = 0.2f, step = 0.35f, ink = ArtInk.Gold)
        },
        { p, f ->
            road125(f)
            for (s in listOf(-1f, 1f)) {
                drawPath(Path().apply { moveTo(w * 0.5f, h * 0.7f); quadraticBezierTo(w * (0.5f + 0.2f * s), h * 0.65f, w * (0.5f + 0.42f * s), h * 0.55f) }, Color(0xFF6B5A48),
                    alpha = f * seg(p, 0.1f, 0.3f), style = Stroke(w * 0.03f))
                drawCircle(Qn.Smoke, w * 0.07f, Offset(w * (0.5f + 0.42f * s), h * 0.52f), alpha = 0.7f * f * seg(p, 0.25f, 0.45f))
            }
            tagAt(a.tag("aysharia1", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            road125(f * 0.6f)
            val off = easeInOut(seg(p, 0.1f, 0.5f))
            person99(Offset(mix(w * 0.5f, w * 0.85f, off), h * 0.72f), h * 0.1f, Dim125, f, walk = a.t * 2f)
            for (k in 0 until 3) drawCircle(Qn.Smoke, w * 0.05f, Offset(w * (0.8f + 0.04f * k), h * (0.6f - 0.03f * k)), alpha = 0.6f * f * seg(p, 0.4f, 0.6f))
            qIcon("cross", Offset(w * 0.85f, h * 0.45f), w * 0.05f, Qn.Rose, f * seg(p, 0.6f, 0.75f), a.t)
            tagAt(a.tag("aysharia2", 0, 0), Offset(w * 0.4f, h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil125(f)
            qIcon("book", Offset(w * 0.5f, h * 0.38f), w * 0.14f, Qn.Gold, f, a.t)
            for (k in 0 until 3) {
                val c = Offset(w * (0.3f + 0.2f * k), h * 0.66f)
                qIcon("heart", c, w * 0.07f, Qn.Rose, f, a.t)
                drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.45f), c, w * 0.004f, alpha = 0.6f * f * seg(p, 0.2f + 0.1f * k, 0.35f + 0.1f * k))
            }
            tagAt(a.tag("aysharia3", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ay-dahr ═════════════
internal fun DrawScope.aydahrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "aydahr", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night125(a) },
        { p, f ->
            qIcon("hourglass", Offset(w * 0.5f, h * 0.3f), w * 0.12f, Dim125, f, a.t)
            val gen = seg(p, 0.2f, 0.8f)
            person99(Offset(w * 0.65f, g), h * 0.13f, Dim125, f * (1f - gen))
            child12(Offset(w * 0.35f, g), h * mix(0.07f, 0.13f, gen), Fig125, f)
            tagAt(a.tag("aydahr0", 0, 0), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil125(f)
            tagAt(a.tag("aydahr1", 0, 0), Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil125(f)
            val tilt = 0.18f * easeInOut(seg(p, 0.35f, 0.65f))
            val c = Offset(w * 0.5f, h * 0.4f)
            val r = Offset(c.x + w * 0.28f * cos(tilt), c.y + w * 0.28f * sin(tilt))
            val l = Offset(c.x - w * 0.28f * cos(tilt), c.y - w * 0.28f * sin(tilt))
            drawLine(Color(0xFF8A6A40), c, Offset(c.x, g), w * 0.012f, alpha = f)
            drawLine(Color(0xFF8A6A40), l, r, w * 0.01f, alpha = f, cap = StrokeCap.Round)
            tagAt(a.tag("aydahr2", 0, 0), Offset(r.x, r.y + h * 0.08f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("aydahr2", 0, 1), Offset(l.x, l.y + h * 0.08f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil125(f)
            val rise = easeInOut(seg(p, 0.1f, 0.5f))
            for (k in 0 until 6) person99(Offset(w * (0.15f + 0.14f * k), g + h * 0.1f * (1f - rise)), h * 0.1f, Fig125, f * rise)
            row125(a, "aydahr3", 2, h * 0.24f, p, f, from = 0.3f, step = 0.3f, ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ay-sabaquna ═════════════
internal fun DrawScope.aysabaqunaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "aysabaquna", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night125(a) },
        { p, f ->
            for (k in 0 until 4) person99(Offset(w * (0.62f + 0.09f * k), g), h * 0.1f, Fig125, f)
            softGlow(Offset(w * 0.75f, g - h * 0.08f), w * 0.2f, Qn.GoldLight, 0.4f * f)
            for (k in 0 until 3) person99(Offset(w * (0.14f + 0.1f * k), g), h * 0.13f, Color(0xFF4A4458), f, arms = 0.6f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("aysabaquna0", 0, 0), Offset(w * 0.3f, h * 0.3f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil125(f)
            for (k in 0 until 5) qIcon("coin", Offset(w * (0.2f + 0.15f * k), h * 0.4f + h * 0.03f * sin(a.t * 2f + k)), w * 0.06f, Qn.Gold, f * seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k), a.t)
            tagAt(a.tag("aysabaquna1", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil125(f)
            qIcon("scroll", Offset(w * 0.72f, h * 0.36f), w * 0.14f, Qn.Parch, f, a.t)
            qIcon("book", Offset(w * 0.28f, h * 0.36f), w * 0.14f, Qn.Gold, f, a.t)
            drawLine(Qn.GoldLight, Offset(w * 0.62f, h * 0.36f), Offset(w * 0.38f, h * 0.36f), w * 0.008f, alpha = f * seg(p, 0.2f, 0.4f), cap = StrokeCap.Round)
            row125(a, "aysabaquna2", 3, h * 0.62f, p, f, from = 0.35f, step = 0.18f, ink = ArtInk.Gold)
        },
        { p, f ->
            door12(Offset(w * 0.5f, g), w * 0.2f, h * 0.32f, 1f - easeInOut(seg(p, 0.1f, 0.5f)), Qn.Gold, Color(0xFF6B4F35), f)
            person99(Offset(w * 0.22f, g), h * 0.12f, Color(0xFF4A4458), f)
            tagAt(a.tag("aysabaquna3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ay-tayyibat ═════════════
internal fun DrawScope.aytayyibatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "aytayyibat", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night125(a) },
        { p, f ->
            tagAt(a.tag("aytayyibat0", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            person99(Offset(w * 0.72f, g), h * 0.13f, Fig125, f, arms = 0.9f)
            woman12(Offset(w * 0.6f, g), h * 0.12f, Mom125, f)
            person99(Offset(w * 0.25f, g), h * 0.12f, Color(0xFF4A4458), f, lean = -0.2f * seg(p, 0.4f, 0.6f))
            for (k in 0 until 3) {
                val up = ((a.t * 0.5f + k / 3f) % 1f)
                drawCircle(Qn.GoldLight, w * 0.008f, Offset(w * 0.66f, g - h * 0.2f - up * h * 0.25f), alpha = f * (1f - up))
            }
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.13f, Fig125, f)
            woman12(Offset(w * 0.6f, g), h * 0.12f, Mom125, f)
            softGlow(Offset(w * 0.66f, g - h * 0.1f), w * 0.15f, Qn.Rose, 0.6f * f * seg(p, 0.2f, 0.45f))
            qIcon("heart", Offset(w * 0.66f, g - h * 0.25f), w * 0.06f, Qn.Rose, f * seg(p, 0.2f, 0.4f), a.t)
            person99(Offset(w * 0.25f, g), h * 0.12f, Color(0xFF4A4458), f)
            tagAt(a.tag("aytayyibat1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil125(f)
            val fade = seg(p, 0.35f, 0.8f)
            drawRoundRect(Color(0xFF8A6A40), Offset(w * 0.2f, h * 0.55f), Size(w * 0.6f, h * 0.03f), CornerRadius(w * 0.01f), alpha = f)
            for (k in 0 until 5) drawOval(listOf(Qn.Gold, Qn.Rose, Qn.Jade, Color(0xFFF6E6A8), Qn.Ember)[k], Offset(w * (0.24f + 0.11f * k), h * 0.5f), Size(w * 0.08f, h * 0.05f), alpha = f * (1f - fade))
            tagAt(a.tag("aytayyibat2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            close97(Offset(w * 0.68f, h * 0.42f), w * 0.1f, a, "aytayyibat3", 1, 1, f * seg(p, 0.05f, 0.2f))
            drawOval(Color(0xFFC8A87A), Offset(w * 0.24f, h * 0.48f), Size(w * 0.14f, h * 0.05f), alpha = f * seg(p, 0.3f, 0.45f))
            tagAt(a.tag("aytayyibat3", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}
