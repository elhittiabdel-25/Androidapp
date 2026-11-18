package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 119 (2026-11-18): al-Adab al-Mufrad (2): servants at the table, repaying a favour, the young and the old,
 * gentleness, cutting off a brother, oppression as darkness, visiting the sick. People are faceless, the Prophet ﷺ is
 * only his named light, angels are lights, the devil only dark smoke; tags with the Name of Allah stay at the top.
 */

private val Fig119 = Color(0xFFEDE4D0)
private val Dim119 = Color(0xFF8A8296)
private val Wall119 = Color(0xFFE2CCA0)
private val Trim119 = Color(0xFF8A6A40)

private fun DrawScope.day119(): Float {
    val g = size.height * 0.8f
    daySky12(Qn.DayTop, Qn.DayLow, g, Color(0xFFB09868))
    return g
}

private fun DrawScope.night119(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil119(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row119(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

private fun DrawScope.arcs119(c: Offset, s: Float, dir: Float, color: Color, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 3) {
        val ph = ((t * 0.8f + k / 3f) % 1f)
        val r = s * (0.3f + ph)
        drawArc(color, if (dir > 0) -40f else 140f, 80f, false, Offset(c.x - r, c.y - r), Size(2 * r, 2 * r),
            alpha = alpha * (1f - ph), style = Stroke(s * 0.12f, cap = StrokeCap.Round))
    }
}

private fun DrawScope.table119(c: Offset, w0: Float, alpha: Float) {
    drawRoundRect(Trim119, Offset(c.x - w0 / 2, c.y), Size(w0, w0 * 0.06f), CornerRadius(w0 * 0.02f), alpha = alpha)
    for (sx in listOf(-0.4f, 0.4f)) drawRect(Trim119, Offset(c.x + sx * w0 - w0 * 0.015f, c.y), Size(w0 * 0.03f, w0 * 0.22f), alpha = alpha)
}

// ═════════════ am-khadim ═════════════
internal fun DrawScope.amkhadimScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "amkhadim", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day119() },
        { p, f ->
            val g = h * 0.8f
            table119(Offset(w * 0.5f, g - h * 0.1f), w * 0.36f, f)
            for (k in 0 until 2) {
                person99(Offset(w * (0.7f - 0.4f * k), g), h * 0.14f, if (k == 0) Fig119 else Color(0xFFC8B89A), f)
                drawOval(Qn.Gold, Offset(w * (0.56f - 0.12f * k) - w * 0.04f, g - h * 0.115f), Size(w * 0.08f, h * 0.02f), alpha = f * seg(p, 0.2f, 0.35f))
            }
            row119(a, "amkhadim0", 2, h * 0.2f, p, f, from = 0.2f, step = 0.3f)
        },
        { p, f ->
            val g = h * 0.8f
            val lighten = easeInOut(seg(p, 0.4f, 0.75f))
            person99(Offset(w * 0.5f, g), h * 0.14f, Color(0xFFC8B89A), f, lean = 0.25f * (1f - lighten))
            val s = mix(w * 0.18f, w * 0.07f, lighten)
            drawRoundRect(Color(0xFF6B5A48), Offset(w * 0.5f - s / 2, g - h * 0.19f - s * 0.7f), Size(s, s * 0.7f), CornerRadius(s * 0.15f), alpha = f)
            tagAt(a.tag("amkhadim1", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            table119(Offset(w * 0.5f, g - h * 0.1f), w * 0.36f, f)
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig119, f, arms = 0.6f * seg(p, 0.3f, 0.5f))
            val come = easeInOut(seg(p, 0.05f, 0.35f))
            person99(Offset(mix(w * 0.05f, w * 0.28f, come), g), h * 0.14f, Color(0xFFC8B89A), f, walk = a.t * 2f * (1f - come))
            drawOval(Qn.Gold, Offset(w * 0.5f - w * 0.05f, g - h * 0.115f), Size(w * 0.1f, h * 0.025f), alpha = f * seg(p, 0.35f, 0.45f))
            tagAt(a.tag("amkhadim2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil119(f)
            val g = h * 0.8f
            person99(Offset(w * 0.5f, g), h * 0.16f, Color(0xFFC8B89A), f)
            softGlow(Offset(w * 0.5f, g - h * 0.2f), w * 0.08f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f, 0.4f))
            drawCircle(Qn.GoldLight, w * 0.06f, Offset(w * 0.5f, g - h * 0.21f), alpha = f * seg(p, 0.2f, 0.4f), style = Stroke(w * 0.008f))
            tagAt(a.tag("amkhadim3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ am-maruf ═════════════
internal fun DrawScope.ammarufScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "ammaruf", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night119(a) },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig119, f, arms = 0.5f)
            person99(Offset(w * 0.28f, g), h * 0.14f, Dim119, f)
            val go = easeInOut(seg(p, 0.05f, 0.35f))
            qIcon("coin", Offset(mix(w * 0.66f, w * 0.34f, go), g - h * 0.12f), w * 0.04f, Qn.Gold, f * (1f - seg(p, 0.4f, 0.5f)), a.t)
            val back = easeInOut(seg(p, 0.45f, 0.7f))
            qIcon("coin", Offset(mix(w * 0.34f, w * 0.66f, back), g - h * 0.16f), w * 0.04f, Qn.GoldLight, f * seg(p, 0.45f, 0.5f) * (1f - seg(p, 0.72f, 0.78f)), a.t)
            arcs119(Offset(w * 0.34f, g - h * 0.17f), w * 0.05f, 1f, Qn.GoldLight, f * seg(p, 0.8f, 0.9f), a.t)
            row119(a, "ammaruf0", 2, h * 0.24f, p, f, from = 0.4f, step = 0.35f)
        },
        { p, f ->
            veil119(f)
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig119, f)
            arcs119(Offset(w * 0.66f, g - h * 0.17f), w * 0.06f, -1f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            person99(Offset(w * 0.28f, g), h * 0.14f, Dim119, f)
            qIcon("lock", Offset(w * 0.34f, g - h * 0.22f), w * 0.05f, Dim119, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("ammaruf1", 0, 0), Offset(w * 0.72f, h * 0.36f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("ammaruf1", 0, 1), Offset(w * 0.28f, h * 0.36f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil119(f)
            tagAt(a.tag("ammaruf2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            for (k in 0 until 3) person99(Offset(w * (0.3f + 0.2f * k), g), h * 0.12f, Fig119, f * seg(p, 0.3f + 0.1f * k, 0.45f + 0.1f * k))
            softGlow(Offset(w * 0.5f, g - h * 0.1f), w * 0.3f, Qn.GoldLight, 0.4f * f * seg(p, 0.6f, 0.85f))
        },
        { p, f ->
            tagAt(a.tag("ammaruf3", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
            for (k in 0 until 3) person99(Offset(w * (0.62f + 0.1f * k), g), h * 0.12f, Fig119, f)
            for (k in 0 until 3) person99(Offset(w * (0.18f + 0.1f * k), g), h * 0.12f, Color(0xFFC8D8C0), f, arms = 0.7f * seg(p, 0.3f, 0.45f))
            for (k in 0 until 3) {
                val up = ((a.t * 0.5f + k / 3f) % 1f)
                drawCircle(Qn.GoldLight, w * 0.008f, Offset(w * (0.18f + 0.1f * k), g - h * 0.2f - up * h * 0.25f), alpha = f * (1f - up) * seg(p, 0.35f, 0.5f))
            }
        },
    )
}

// ═════════════ am-kabir ═════════════
internal fun DrawScope.amkabirScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "amkabir", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day119() },
        { p, f ->
            val g = h * 0.8f
            person99(Offset(w * 0.75f, g), h * 0.13f, Color(0xFFF5F2EA), f, lean = 0.12f)
            drawLine(Trim119, Offset(w * 0.79f, g), Offset(w * 0.8f, g - h * 0.12f), w * 0.008f, alpha = f)
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig119, f, lean = 0.15f * seg(p, 0.5f, 0.7f))
            child12(Offset(w * 0.26f, g), h * 0.12f, Color(0xFFD8C2E0), f)
            softGlow(Offset(w * 0.36f, g - h * 0.08f), w * 0.1f, Qn.Rose, 0.5f * f * seg(p, 0.15f, 0.35f))
            row119(a, "amkabir0", 2, h * 0.22f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            val g = h * 0.8f
            person99(Offset(w * 0.72f, g), h * 0.13f, Color(0xFFF5F2EA), f, lean = 0.12f)
            softGlow(Offset(w * 0.72f, g - h * 0.17f), w * 0.07f, Color(0xFFFFFFFF), 0.6f * f * seg(p, 0.1f, 0.3f))
            qIcon("crown", Offset(w * 0.28f, g - h * 0.24f), w * 0.07f, Qn.Gold, f * seg(p, 0.5f, 0.6f), a.t)
            qIcon("scale", Offset(w * 0.28f, g - h * 0.34f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.55f, 0.65f), a.t)
            person99(Offset(w * 0.28f, g), h * 0.14f, Fig119, f * seg(p, 0.45f, 0.6f))
            row119(a, "amkabir1", 2, h * 0.14f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            val g = h * 0.8f
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig119, f)
            qIcon("book", Offset(w * 0.5f, g - h * 0.27f), w * 0.09f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            for (sx in listOf(-1f, 1f)) qIcon("cross", Offset(w * 0.5f + sx * w * 0.28f, g - h * 0.2f), w * 0.04f, Qn.Rose, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("amkabir2", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("amkabir2", 0, 1), Offset(w * 0.5f, h * 0.88f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            person99(Offset(w * 0.72f, g), h * 0.13f, Color(0xFFF5F2EA), f, lean = 0.12f)
            arcs119(Offset(w * 0.66f, g - h * 0.15f), w * 0.05f, -1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 3) child12(Offset(w * (0.38f - 0.1f * k), g), h * (0.13f - 0.02f * k), Color(0xFFD8C2E0), f)
            val give = easeInOut(seg(p, 0.5f, 0.8f))
            drawCircle(Color(0xFF8A3A2A), w * 0.015f, Offset(mix(w * 0.6f, w * 0.18f, give), g - h * 0.12f), alpha = f * seg(p, 0.45f, 0.5f))
            row119(a, "amkabir3", 2, h * 0.22f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ am-rifq ═════════════
internal fun DrawScope.amrifqScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "amrifq", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night119(a) },
        { p, f ->
            tagAt(a.tag("amrifq0", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            person99(Offset(w * 0.62f, g), h * 0.14f, Fig119, f, arms = 0.4f)
            person99(Offset(w * 0.38f, g), h * 0.12f, Dim119, f, lean = 0.2f * (1f - seg(p, 0.4f, 0.7f)))
            softGlow(Offset(w * 0.5f, g - h * 0.12f), w * 0.15f, Qn.Jade, 0.5f * f * seg(p, 0.4f, 0.7f))
        },
        { p, f ->
            veil119(f)
            door12(Offset(w * 0.5f, g), w * 0.2f, h * 0.32f, 1f - easeInOut(seg(p, 0.2f, 0.6f)), Trim119, Color(0xFF6B4F35), f)
            person99(Offset(w * 0.24f, g), h * 0.13f, Dim119, f)
            tagAt(a.tag("amrifq1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            door12(Offset(w * 0.5f, g), w * 0.2f, h * 0.32f, easeInOut(seg(p, 0.1f, 0.5f)), Trim119, Color(0xFF6B4F35), f)
            person99(Offset(mix(w * 0.2f, w * 0.44f, easeInOut(seg(p, 0.3f, 0.7f))), g), h * 0.13f, Fig119, f, walk = a.t * 2f)
            softGlow(Offset(w * 0.5f, g - h * 0.15f), w * 0.2f, Qn.GoldLight, 0.6f * f * seg(p, 0.4f, 0.7f))
            tagAt(a.tag("amrifq2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil119(f)
            val tilt = 0.18f * easeInOut(seg(p, 0.3f, 0.6f))
            val c = Offset(w * 0.5f, h * 0.38f)
            val r = Offset(c.x + w * 0.26f * cos(tilt), c.y + w * 0.26f * sin(tilt))
            val l = Offset(c.x - w * 0.26f * cos(tilt), c.y - w * 0.26f * sin(tilt))
            drawLine(Trim119, c, Offset(c.x, g), w * 0.012f, alpha = f)
            drawLine(Trim119, l, r, w * 0.01f, alpha = f, cap = StrokeCap.Round)
            qIcon("heart", Offset(r.x, r.y + h * 0.06f), w * 0.08f, Qn.Rose, f, a.t)
            row119(a, "amrifq3", 2, h * 0.16f, p, f, from = 0.1f, step = 0.35f)
        },
    )
}

// ═════════════ am-hijra ═════════════
internal fun DrawScope.amhijraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "amhijra", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night119(a) },
        { p, f ->
            val apart = easeInOut(seg(p, 0.2f, 0.5f))
            person99(Offset(w * (0.56f + 0.14f * apart), g), h * 0.14f, Fig119, f, lean = -0.15f * apart)
            person99(Offset(w * (0.44f - 0.14f * apart), g), h * 0.14f, Dim119, f, lean = 0.15f * apart)
            for (k in 0 until 3) qIcon("moon", Offset(w * (0.68f - 0.18f * k), h * 0.2f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.45f + 0.12f * k, 0.55f + 0.12f * k), a.t)
            tagAt(a.tag("amhijra0", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.8f, 0.9f), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.7f, g), h * 0.14f, Fig119, f)
            person99(Offset(w * 0.3f, g), h * 0.14f, Dim119, f)
            arcs119(Offset(w * 0.64f, g - h * 0.17f), w * 0.06f, -1f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            softGlow(Offset(w * 0.7f, g - h * 0.1f), w * 0.15f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.55f))
            tagAt(a.tag("amhijra1", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil119(f)
            door12(Offset(w * 0.5f, g), w * 0.22f, h * 0.34f, 0f, Qn.Gold, Color(0xFF6B4F35), f)
            for (k in 0 until 2) person99(Offset(w * (0.72f - 0.44f * k), g), h * 0.12f, Dim119, f)
            softGlow(Offset(w * 0.72f, g - h * 0.1f), w * 0.12f, Qn.GoldLight, 0.6f * f * seg(p, 0.55f, 0.8f))
            row119(a, "amhijra2", 2, h * 0.2f, p, f, from = 0.15f, step = 0.45f)
        },
        { p, f ->
            person99(Offset(w * 0.7f, g), h * 0.14f, Fig119, f)
            person99(Offset(w * 0.3f, g), h * 0.14f, Dim119, f)
            angel98(Offset(w * 0.78f, h * 0.36f), w * 0.025f, f * seg(p, 0.15f, 0.35f))
            for (k in 0 until 3) drawCircle(Qn.Smoke, w * (0.04f + 0.01f * k), Offset(w * (0.18f + 0.03f * k), h * (0.42f - 0.04f * k)), alpha = 0.7f * f * seg(p, 0.45f, 0.65f))
            tagAt(a.tag("amhijra3", 0, 0), Offset(w * 0.72f, h * 0.22f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Gold)
            tagAt(a.tag("amhijra3", 0, 1), Offset(w * 0.28f, h * 0.22f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ am-zulm ═════════════
internal fun DrawScope.amzulmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "amzulm", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night119(a) },
        { p, f ->
            val dark = easeInOut(seg(p, 0.2f, 0.7f))
            for (k in 0 until 3) drawRect(Color(0xFF05040A), Offset(w * (0.5f - 0.18f * k) , 0f), Size(w, h), alpha = 0.3f * f * seg(p, 0.2f + 0.15f * k, 0.4f + 0.15f * k))
            person99(Offset(mix(w * 0.2f, w * 0.65f, dark), g), h * 0.13f, Dim119, f * (1f - 0.6f * dark), walk = a.t * 2f)
            tagAt(a.tag("amzulm0", 0, 0), Offset(w * 0.3f, h * 0.22f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil119(f)
            val grip = seg(p, 0.15f, 0.4f)
            for (k in 0 until 6) {
                val ang = k / 6f * 6.283f
                val d = mix(w * 0.18f, w * 0.06f, grip)
                qIcon("coin", Offset(w * 0.5f + d * cos(ang), h * 0.45f + d * sin(ang) * 0.6f), w * 0.04f, Qn.Gold, f * (1f - seg(p, 0.6f, 0.85f)), a.t)
            }
            drawCircle(Qn.Smoke, w * 0.12f * seg(p, 0.55f, 0.85f), Offset(w * 0.5f, h * 0.45f), alpha = 0.7f * f)
            tagAt(a.tag("amzulm1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.2f, 0.35f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil119(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.48f), w * 0.22f, Qn.Parch, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 6) drawLine(Qn.Ink, Offset(w * 0.42f, h * (0.41f + 0.028f * k)), Offset(w * (0.42f + 0.16f * seg(p, 0.2f + 0.1f * k, 0.3f + 0.1f * k)), h * (0.41f + 0.028f * k)), w * 0.005f, alpha = f)
            tagAt(a.tag("amzulm2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil119(f)
            val kneel = seg(p, 0.15f, 0.45f)
            person99(Offset(w * 0.5f, g), h * 0.14f * mix(1f, 0.7f, kneel), Fig119, f, lean = 0.2f * kneel)
            tagAt(a.tag("amzulm3", 0, 0), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ am-iyada ═════════════
private fun DrawScope.sickbed119(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Color(0xFFD8CCB4), Offset(c.x - s * 0.5f, c.y - s * 0.12f), Size(s, s * 0.14f), CornerRadius(s * 0.04f), alpha = alpha)
    drawRect(Trim119, Offset(c.x - s * 0.5f, c.y), Size(s, s * 0.05f), alpha = alpha)
    drawCircle(Color(0xFFC8B89A), s * 0.07f, Offset(c.x + s * 0.38f, c.y - s * 0.16f), alpha = alpha)
}

internal fun DrawScope.amiyadaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "amiyada", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night119(a) },
        { p, f ->
            sickbed119(Offset(w * 0.32f, g - h * 0.03f), w * 0.34f, f)
            val go = easeInOut(seg(p, 0.05f, 0.5f))
            person99(Offset(mix(w * 0.95f, w * 0.6f, go), g), h * 0.13f, Fig119, f, walk = a.t * 2f * (1f - go))
            for (k in 0 until 5) {
                val x = w * (0.1f + 0.2f * k)
                qIcon("tree", Offset(x, h * 0.36f), w * 0.08f, Qn.Leaf, f * seg(p, 0.5f + 0.06f * k, 0.6f + 0.06f * k), a.t)
                drawCircle(Qn.Rose, w * 0.01f, Offset(x + w * 0.015f, h * 0.35f), alpha = f * seg(p, 0.6f + 0.06f * k, 0.7f + 0.06f * k))
            }
            tagAt(a.tag("amiyada0", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            sickbed119(Offset(w * 0.32f, g - h * 0.03f), w * 0.34f, f)
            val rise = easeInOut(seg(p, 0.1f, 0.8f))
            drawRect(Qn.GoldLight, Offset(0f, g - h * 0.2f * rise), Size(w, h * 0.2f * rise + h * 0.02f), alpha = 0.25f * f)
            person99(Offset(w * 0.62f, g), h * 0.13f * mix(1f, 0.7f, seg(p, 0.5f, 0.7f)), Fig119, f)
            row119(a, "amiyada1", 2, h * 0.2f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            sickbed119(Offset(w * 0.32f, g - h * 0.03f), w * 0.3f, f)
            sirajMunir(Offset(w * 0.66f, h * 0.48f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.66f, h * 0.36f))
            arcs119(Offset(w * 0.56f, h * 0.55f), w * 0.05f, -1f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            softGlow(Offset(w * 0.45f, g - h * 0.1f), w * 0.1f, Qn.Jade, 0.6f * f * seg(p, 0.65f, 0.85f))
            tagAt(a.tag("amiyada2", 0, 0), Offset(w * 0.4f, h * 0.2f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            tagAt(a.tag("amiyada3", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            val go = easeInOut(seg(p, 0.05f, 0.6f))
            sirajMunir(Offset(mix(w * 0.6f, w * 0.85f, go), h * 0.5f), w * 0.035f, f, a.t, named = false)
            sickbed119(Offset(w * 0.32f, g - h * 0.03f), w * 0.3f, f)
            softGlow(Offset(w * 0.4f, g - h * 0.1f), w * 0.12f, Qn.Jade, 0.6f * f)
        },
    )
}
