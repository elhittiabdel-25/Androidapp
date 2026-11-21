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
 * Wave 123 (2026-11-18): Tafsir Ibn Kathir, the virtues of the Quran: 'Uthman's one harf, beautifying the voice,
 * keeping the Quran, «نُسِّيَ», the drawn-out reading, how long to finish it, hearts together, and the refuge. The
 * Prophet ﷺ is only his named light, Jibril a cool light, 'Uthman his roundel, Abu Musa a plain roundel; the devil is
 * never drawn (only smoke). No instrument is drawn; tags with the Name of Allah stay at the top.
 */

private val Fig123 = Color(0xFFEDE4D0)
private val Dim123 = Color(0xFF8A8296)

private fun DrawScope.night123(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil123(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row123(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

private fun DrawScope.arcs123(c: Offset, s: Float, dir: Float, color: Color, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 3) {
        val ph = ((t * 0.8f + k / 3f) % 1f)
        val r = s * (0.3f + ph)
        drawArc(color, if (dir > 0) -40f else 140f, 80f, false, Offset(c.x - r, c.y - r), Size(2 * r, 2 * r),
            alpha = alpha * (1f - ph), style = Stroke(s * 0.12f, cap = StrokeCap.Round))
    }
}

/** A reader: a faceless figure holding an open book. */
private fun DrawScope.reader123(foot: Offset, tall: Float, alpha: Float, a: ArtCtx, color: Color = Fig123) {
    person99(foot, tall, color, alpha, arms = 0.4f)
    qIcon("book", Offset(foot.x + tall * 0.12f, foot.y - tall * 0.95f), tall * 0.35f, Qn.Gold, alpha, a.t)
}

/** Calendar cells: `n` of `total` lit, right to left. */
private fun DrawScope.days123(n: Int, total: Int, p: Float, f: Float) {
    val w = size.width
    val h = size.height
    val cols = 10
    for (i in 0 until total) {
        val c = Offset(w * (0.8f - 0.067f * (i % cols)), h * (0.36f + 0.09f * (i / cols)))
        val lit = i < n
        drawRoundRect(if (lit) Qn.Gold else Color(0x33FFFFFF), Offset(c.x - w * 0.025f, c.y - h * 0.03f), Size(w * 0.05f, h * 0.06f), CornerRadius(w * 0.008f),
            alpha = f * if (lit) seg(p, 0.05f + 0.5f * i / total, 0.12f + 0.5f * i / total) else 1f)
    }
}

// ═════════════ ik-harf ═════════════
internal fun DrawScope.ikharfScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "ikharf", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night123(a) },
        { p, f ->
            val apart = easeInOut(seg(p, 0.15f, 0.6f))
            for (k in 0 until 3) person99(Offset(w * (0.62f + 0.08f * k + 0.08f * apart), g), h * 0.1f, Dim123, f)
            for (k in 0 until 3) person99(Offset(w * (0.38f - 0.08f * k - 0.08f * apart), g), h * 0.1f, Dim123, f)
            for (k in 0 until 3) drawCircle(Qn.Smoke, w * (0.03f + 0.01f * k), Offset(w * 0.5f, g - h * (0.06f + 0.06f * k)), alpha = 0.7f * f * apart)
            tagAt(a.tag("ikharf0", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            qIcon("crescent", Offset(w * 0.5f, h * 0.14f), w * 0.07f, Qn.GoldLight, f, a.t)
            sirajMunir(Offset(w * 0.36f, h * 0.44f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.36f, h * 0.32f))
            angel98(Offset(w * 0.64f, h * 0.44f), w * 0.03f, f * seg(p, 0.05f, 0.2f))
            arcs123(Offset(w * 0.56f, h * 0.44f), w * 0.05f, -1f, Color(0xFFCFE6F2), f * seg(p, 0.2f, 0.35f), a.t)
            arcs123(Offset(w * 0.44f, h * 0.44f), w * 0.05f, 1f, Qn.GoldLight, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("ikharf1", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            close97(Offset(w * 0.5f, h * 0.45f), w * 0.09f, a, "ikharf2", 1, 2, f * seg(p, 0.05f, 0.2f))
            val out = easeInOut(seg(p, 0.25f, 0.75f))
            for (k in 0 until 4) {
                val ang = k / 4f * 6.283f + 0.78f
                qIcon("book", Offset(w * 0.5f + w * 0.34f * out * cos(ang), h * 0.45f + h * 0.22f * out * sin(ang)), w * 0.06f, Qn.Gold, f * seg(p, 0.2f, 0.3f), a.t)
            }
            tagAt(a.tag("ikharf2", 0, 0), Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil123(f)
            for (k in 0 until 7) {
                val y0 = h * (0.3f + 0.05f * k)
                val q = seg(p, 0.1f, 0.6f)
                drawLine(Qn.GoldLight, Offset(w * 0.85f, y0), Offset(mix(w * 0.85f, w * 0.4f, q), mix(y0, h * 0.45f, q)), w * 0.005f, alpha = 0.7f * f, cap = StrokeCap.Round)
            }
            drawLine(Qn.Gold, Offset(w * 0.4f, h * 0.45f), Offset(w * 0.12f, h * 0.45f), w * 0.012f, alpha = f * seg(p, 0.55f, 0.7f), cap = StrokeCap.Round)
            tagAt(a.tag("ikharf3", 0, 0), Offset(w * 0.72f, h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ikharf3", 0, 1), Offset(w * 0.26f, h * 0.6f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ik-taghanni ═════════════
internal fun DrawScope.iktaghanniScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "iktaghanni", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night123(a) },
        { p, f ->
            reader123(Offset(w * 0.68f, g), h * 0.15f, f, a)
            arcs123(Offset(w * 0.6f, g - h * 0.17f), w * 0.07f, -1f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            row123(a, "iktaghanni0", 2, h * 0.22f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            reader123(Offset(w * 0.72f, g), h * 0.14f, f, a)
            arcs123(Offset(w * 0.64f, g - h * 0.16f), w * 0.06f, -1f, Qn.GoldLight, f, a.t)
            person99(Offset(w * 0.3f, g), h * 0.13f, Fig123, f)
            softGlow(Offset(w * 0.3f, g - h * 0.12f), w * 0.1f, Qn.Rose, 0.6f * f * seg(p, 0.3f, 0.6f))
            qIcon("heart", Offset(w * 0.3f, g - h * 0.24f), w * 0.05f, Qn.Rose, f * seg(p, 0.3f, 0.5f), a.t)
            tagAt(a.tag("iktaghanni1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil123(f)
            val path = Path()
            for (i in 0..24) {
                val x = w * (0.18f + 0.64f * i / 24f)
                val y = h * 0.45f + h * 0.06f * (if (i % 2 == 0) 1f else -1f)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path, Dim123, alpha = f * seg(p, 0.05f, 0.2f), style = Stroke(w * 0.008f))
            qIcon("cross", Offset(w * 0.5f, h * 0.45f), w * 0.09f, Qn.Rose, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("iktaghanni2", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil123(f)
            tagAt(a.tag("iktaghanni3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            reader123(Offset(w * 0.5f, g), h * 0.15f, f, a)
            arcs123(Offset(w * 0.42f, g - h * 0.17f), w * 0.07f, -1f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
        },
    )
}

// ═════════════ ik-taahud ═════════════
internal fun DrawScope.iktaahudScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "iktaahud", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night123(a) },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.46f)
            softGlow(c, w * 0.18f, Qn.GoldLight, 0.5f * f)
            qIcon("book", c, w * 0.16f, Qn.Gold, f, a.t)
            val loose = seg(p, 0.3f, 0.7f)
            for (k in 0 until 2) {
                val s = if (k == 0) 1f else -1f
                drawLine(Color(0xFFC8A87A), Offset(c.x + s * w * 0.1f, c.y), Offset(c.x + s * (w * 0.1f + w * 0.2f * loose), c.y + h * 0.1f * loose), w * 0.012f, alpha = f, cap = StrokeCap.Round)
            }
            tagAt(a.tag("iktaahud0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil123(f)
            val c = Offset(w * 0.5f, h * 0.5f)
            qIcon("heart", c, w * 0.16f, Qn.Rose, f, a.t)
            for (k in 0 until 6) {
                val q = seg(p, 0.15f + 0.08f * k, 0.5f + 0.08f * k)
                val ang = k / 6f * 6.283f
                drawLine(Qn.GoldLight, Offset(c.x + w * (0.05f + 0.35f * q) * cos(ang), c.y + h * (0.03f + 0.25f * q) * sin(ang)),
                    Offset(c.x + w * (0.1f + 0.35f * q) * cos(ang), c.y + h * (0.03f + 0.25f * q) * sin(ang)), w * 0.008f, alpha = f * (1f - q), cap = StrokeCap.Round)
            }
            tagAt(a.tag("iktaahud1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            reader123(Offset(w * 0.5f, g), h * 0.15f, f, a)
            val ph = (a.t * 0.2f) % 1f
            qIcon(if (ph < 0.5f) "sun" else "moon", Offset(w * (0.15f + 0.7f * ((ph * 2f) % 1f)), h * 0.18f), w * 0.07f, Qn.GoldLight, f, a.t)
            softGlow(Offset(w * 0.5f, g - h * 0.12f), w * 0.15f, Qn.GoldLight, 0.5f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("iktaahud2", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil123(f)
            qIcon("book", Offset(w * 0.5f, h * 0.48f), w * 0.16f, Dim123, f, a.t)
            for (k in 0 until 10) drawCircle(Color(0xFF8A8070), w * 0.006f, Offset(w * (0.4f + 0.02f * k), h * (0.44f + 0.01f * (k % 3))), alpha = f * seg(p, 0.1f + 0.03f * k, 0.2f + 0.03f * k))
            tagAt(a.tag("iktaahud3", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ik-nusiya ═════════════
internal fun DrawScope.iknusiyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "iknusiya", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night123(a) },
        { p, f ->
            reader123(Offset(w * 0.28f, g), h * 0.13f, f, a)
            arcs123(Offset(w * 0.36f, g - h * 0.15f), w * 0.06f, 1f, Qn.GoldLight, f, a.t)
            sirajMunir(Offset(w * 0.72f, h * 0.5f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.72f, h * 0.38f))
            softGlow(Offset(w * 0.72f, h * 0.5f), w * 0.15f, Qn.GoldLight, 0.5f * f * seg(p, 0.5f, 0.75f))
            tagAt(a.tag("iknusiya0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil123(f)
            tagAt(a.tag("iknusiya1", 0, 0), Offset(w * 0.68f, h * 0.45f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            qIcon("cross", Offset(w * 0.68f, h * 0.58f), w * 0.06f, Qn.Rose, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("iknusiya1", 0, 1), Offset(w * 0.32f, h * 0.45f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
            qIcon("check", Offset(w * 0.32f, h * 0.58f), w * 0.06f, Qn.Jade, f * seg(p, 0.7f, 0.85f), a.t)
        },
        { p, f ->
            reader123(Offset(w * 0.5f, g), h * 0.14f, f, a)
            qIcon("flame", Offset(w * 0.72f, g - h * 0.1f), w * 0.05f, Qn.Gold, f, a.t)
            softGlow(Offset(w * 0.5f, g - h * 0.1f), w * 0.15f, Qn.GoldLight, 0.4f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("iknusiya2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil123(f)
            tagAt(a.tag("iknusiya3", 0, 0), Offset(w * 0.5f, h * 0.45f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ik-madd ═════════════
internal fun DrawScope.ikmaddScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ikmadd", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night123(a) },
        { p, f ->
            veil123(f)
            val q = easeInOut(seg(p, 0.15f, 0.7f))
            val path = Path()
            for (i in 0..40) {
                val x = w * (0.85f - 0.7f * q * i / 40f)
                val y = h * 0.5f + h * 0.03f * sin(i * 0.5f + a.t * 2f)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path, Qn.GoldLight, alpha = f, style = Stroke(w * 0.01f, cap = StrokeCap.Round))
            tagAt(a.tag("ikmadd0", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil123(f)
            row123(a, "ikmadd1", 3, h * 0.2f, p, f, from = 0.05f, step = 0.25f, ink = ArtInk.Gold)
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                val q = seg(p, 0.12f + 0.25f * i, 0.32f + 0.25f * i)
                drawLine(Qn.GoldLight, Offset(x + w * 0.1f, h * 0.36f), Offset(x + w * 0.1f - w * 0.2f * q, h * 0.36f), w * 0.01f, alpha = f, cap = StrokeCap.Round)
            }
        },
        { p, f ->
            val sway = sin(a.t * 2f) * w * 0.01f
            sirajMunir(Offset(w * 0.5f + sway, h * 0.48f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.36f))
            arcs123(Offset(w * 0.42f + sway, h * 0.48f), w * 0.06f, -1f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            row123(a, "ikmadd2", 2, h * 0.7f, p, f, from = 0.2f, step = 0.35f)
        },
        { p, f ->
            plain97(Offset(w * 0.3f, h * 0.45f), w * 0.09f, a, "ikmadd3", 1, f * seg(p, 0.05f, 0.2f))
            sirajMunir(Offset(w * 0.72f, h * 0.45f), w * 0.03f, f, a.t, named = false)
            arcs123(Offset(w * 0.38f, h * 0.45f), w * 0.06f, 1f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("ikmadd3", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ik-khatm ═════════════
internal fun DrawScope.ikkhatmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "ikkhatm", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night123(a) },
        { p, f ->
            veil123(f)
            days123(30, 30, p, f)
            tagAt(a.tag("ikkhatm0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil123(f)
            days123(7, 30, p, f)
            tagAt(a.tag("ikkhatm1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil123(f)
            days123(3, 30, p, f)
            tagAt(a.tag("ikkhatm2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            reader123(Offset(w * 0.5f, g), h * 0.15f, f, a)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.4f + k / 3f) % 1f)
                drawCircle(Qn.GoldLight, w * 0.008f, Offset(w * 0.5f, g - h * 0.24f - ph * h * 0.12f), alpha = f * (1f - ph))
            }
            tagAt(a.tag("ikkhatm3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ik-itlaf ═════════════
internal fun DrawScope.ikitlafScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val c = Offset(w * 0.5f, h * 0.5f)
    film98(a, "ikitlaf", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night123(a) },
        { p, f ->
            for (k in 0 until 6) {
                val ang = k / 6f * 6.283f
                qIcon("heart", Offset(c.x + w * 0.24f * cos(ang), c.y + h * 0.16f * sin(ang)), w * 0.06f, Qn.Rose, f * seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k), a.t)
            }
            qIcon("book", c, w * 0.1f, Qn.Gold, f, a.t)
            softGlow(c, w * 0.3f, Qn.GoldLight, 0.4f * f * seg(p, 0.5f, 0.8f))
            tagAt(a.tag("ikitlaf0", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 6) {
                val ang = k / 6f * 6.283f
                val q = easeInOut(seg(p, 0.1f, 0.5f))
                qIcon("heart", Offset(c.x + w * mix(0.24f, 0.16f, q) * cos(ang), c.y + h * mix(0.16f, 0.1f, q) * sin(ang)), w * 0.06f, Qn.Rose, f, a.t)
            }
            qIcon("book", c, w * 0.1f, Qn.Gold, f, a.t)
            softGlow(c, w * 0.25f, Qn.GoldLight, 0.6f * f)
            tagAt(a.tag("ikitlaf1", 0, 0), Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val dim = seg(p, 0.15f, 0.45f)
            for (k in 0 until 6) {
                val ang = k / 6f * 6.283f
                qIcon("heart", Offset(c.x + w * 0.24f * cos(ang) * (1f + 0.3f * dim * (k % 2)), c.y + h * 0.16f * sin(ang)), w * 0.06f, lerpC123(Qn.Rose, Dim123, dim), f, a.t)
            }
            qIcon("book", c, w * 0.1f, Qn.Gold, f * (1f - 0.4f * dim), a.t)
            tagAt(a.tag("ikitlaf2", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil123(f)
            qIcon("flame", Offset(w * 0.5f, h * 0.48f), w * 0.1f, Qn.Gold, f, a.t)
            for (k in 0 until 7) drawCircle(Qn.Gold, w * 0.012f, Offset(w * (0.8f - 0.1f * k), h * 0.66f), alpha = f * seg(p, 0.1f + 0.08f * k, 0.2f + 0.08f * k))
            tagAt(a.tag("ikitlaf3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

private fun lerpC123(a: Color, b: Color, t: Float) = Color(a.red + (b.red - a.red) * t, a.green + (b.green - a.green) * t, a.blue + (b.blue - a.blue) * t, 1f)

// ═════════════ ik-istiadha ═════════════
internal fun DrawScope.ikistiadhaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "ikistiadha", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night123(a) },
        { p, f ->
            reader123(Offset(w * 0.5f, g), h * 0.15f, f, a)
            val shield = seg(p, 0.3f, 0.5f)
            drawArc(Qn.GoldLight, 180f, 180f, false, Offset(w * 0.28f, g - h * 0.32f), Size(w * 0.44f, h * 0.5f), alpha = 0.8f * f * shield, style = Stroke(w * 0.01f))
            for (k in 0 until 2) {
                val x = mix(w * (if (k == 0) 0.05f else 0.95f), w * (if (k == 0) 0.22f else 0.78f), seg(p, 0.05f, 0.35f))
                drawCircle(Qn.Smoke, w * 0.06f, Offset(x, g - h * 0.15f), alpha = 0.7f * f * (1f - seg(p, 0.55f, 0.8f)))
            }
            tagAt(a.tag("ikistiadha0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil123(f)
            qIcon("book", Offset(w * 0.5f, h * 0.48f), w * 0.14f, Qn.Gold, f * seg(p, 0.4f, 0.6f), a.t)
            qIcon("hands", Offset(w * 0.5f, h * 0.48f), w * 0.14f, Fig123, f * (1f - seg(p, 0.4f, 0.6f)), a.t)
            tagAt(a.tag("ikistiadha1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.5f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.38f))
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.3f)
                drawCircle(Qn.Smoke, w * 0.06f, Offset(x, h * 0.72f), alpha = 0.7f * f * (1f - seg(p, 0.45f + 0.12f * i, 0.65f + 0.12f * i)))
            }
            row123(a, "ikistiadha2", 3, h * 0.86f, p, f, from = 0.1f, step = 0.15f)
        },
        { p, f ->
            veil123(f)
            row123(a, "ikistiadha3", 3, h * 0.45f, p, f, from = 0.1f, step = 0.25f)
        },
    )
}
