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
 * Wave 129 (2026-11-18): Adwa' al-Bayan on al-Fatiha: praise in its places and times, the worlds and the sign,
 * al-Rahman's wide mercy and al-Rahim's for the believers (light only, never a figure for Allah), the Day of
 * Recompense, the negation and affirmation of «Iyyaka na'budu», the straight path of those favoured (Abu Bakr his
 * companion roundel, Maryam a plain one), and the two paths that leave it. No verse is written in the scenes;
 * the Prophet ﷺ is only his named light, Musa a named light.
 */

private val Fig129 = Color(0xFFEDE4D0)
private val Dim129 = Color(0xFF8A8296)

private fun DrawScope.night129(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil129(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row129(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             first: Int = 0, ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, first + i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

private fun DrawScope.book129(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, Offset(c.x - s, c.y - s * 0.65f), Size(s * 2f, s * 1.3f), CornerRadius(s * 0.12f), alpha = alpha)
    drawLine(Color(0xFF2A2140), Offset(c.x, c.y - s * 0.6f), Offset(c.x, c.y + s * 0.6f), s * 0.08f, alpha = alpha)
}

private fun DrawScope.light129(c: Offset, r: Float, a: ArtCtx, kind: String, part: Int, alpha: Float, cool: Boolean = false) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 3f, if (cool) Color(0xFFCFE6F2) else Qn.GoldLight, 0.7f * alpha)
    drawCircle(Color(0xFFF8F4E8), r, c, alpha = alpha)
    if (part >= 0) tagAt(a.tag(kind, 0, part), Offset(c.x, c.y - r * 3.2f), alpha, ink = ArtInk.Ivory)
}

/** The straight path, from the near ground up to the horizon. */
private fun DrawScope.path129(alpha: Float, reveal: Float = 1f, color: Color = Qn.Gold) {
    val w = size.width
    val h = size.height
    val p = Path().apply {
        moveTo(w * 0.36f, h)
        lineTo(w * 0.64f, h)
        lineTo(w * mix(0.64f, 0.52f, reveal), h * mix(1f, 0.3f, reveal))
        lineTo(w * mix(0.36f, 0.48f, reveal), h * mix(1f, 0.3f, reveal))
        close()
    }
    drawPath(p, color, alpha = 0.55f * alpha)
}

// ═════════════ ad-hamd ═════════════
internal fun DrawScope.adhamdScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "adhamd", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night129(a) },
        { p, f ->
            veil129(f)
            book129(Offset(w * 0.5f, h * 0.28f), w * 0.06f, Qn.Gold, f * seg(p, 0.02f, 0.15f))
            for (i in 0 until 2) {
                val x = w * (0.7f - 0.4f * i)
                drawRoundRect(Dim129, Offset(x - w * 0.12f, h * 0.44f), Size(w * 0.24f, h * 0.14f), CornerRadius(w * 0.02f), alpha = f * seg(p, 0.3f + 0.2f * i, 0.45f + 0.2f * i), style = Stroke(w * 0.004f))
            }
            row129(a, "adhamd0", 2, h * 0.66f, p, f, from = 0.3f, step = 0.2f)
        },
        { p, f ->
            drawRect(Qn.GoldLight, Offset(0f, 0f), Size(w, h * 0.3f), alpha = 0.18f * f * seg(p, 0.35f, 0.55f))
            drawRect(Qn.GoldLight, Offset(0f, h * 0.8f), Size(w, h * 0.2f), alpha = 0.18f * f * seg(p, 0.6f, 0.8f))
            tagAt(a.tag("adhamd1", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("adhamd1", 0, 1), Offset(w * 0.5f, h * 0.9f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
            tagAt(a.tag("adhamd1", 0, 2), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil129(f)
            drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.3f), Offset(w * 0.5f, h * 0.7f), w * 0.004f, alpha = f * seg(p, 0.3f, 0.4f))
            drawCircle(Qn.Gold, w * 0.06f, Offset(w * 0.74f, h * 0.5f), alpha = f * seg(p, 0.35f, 0.5f))
            softGlow(Offset(w * 0.26f, h * 0.5f), w * 0.14f, Qn.GoldLight, 0.7f * f * seg(p, 0.6f, 0.75f))
            tagAt(a.tag("adhamd2", 0, 0), Offset(w * 0.74f, h * 0.7f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
            tagAt(a.tag("adhamd2", 0, 1), Offset(w * 0.26f, h * 0.7f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
            tagAt(a.tag("adhamd2", 0, 2), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil129(f)
            val gather = easeInOut(seg(p, 0.1f, 0.6f))
            for (k in 0 until 18) {
                val ang = k / 18f * 6.283f + a.t * 0.2f
                val r = w * mix(0.4f, 0.02f, gather)
                drawCircle(Qn.GoldLight, w * 0.008f, Offset(w * 0.5f + cos(ang) * r, h * 0.45f + sin(ang) * r * 0.8f), alpha = f * (1f - 0.7f * gather))
            }
            softGlow(Offset(w * 0.5f, h * 0.45f), w * 0.14f, Qn.GoldLight, 0.8f * f * gather)
            tagAt(a.tag("adhamd3", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ad-alamin ═════════════
internal fun DrawScope.adalaminScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "adalamin", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night129(a) },
        { p, f ->
            light129(Offset(w * 0.72f, h * 0.5f), w * 0.03f, a, "adalamin0", 0, f * seg(p, 0.02f, 0.15f), cool = true)
            plain97(Offset(w * 0.28f, h * 0.5f), w * 0.08f, a, "adalamin0", 1, f * seg(p, 0.15f, 0.3f))
        },
        { p, f ->
            drawRect(Qn.GoldLight, Offset(0f, 0f), Size(w, h * 0.28f), alpha = 0.18f * f * seg(p, 0.1f, 0.3f))
            drawRect(Qn.GoldLight, Offset(0f, h * 0.8f), Size(w, h * 0.2f), alpha = 0.18f * f * seg(p, 0.35f, 0.55f))
            drawRect(Qn.GoldLight, Offset(0f, h * 0.28f), Size(w, h * 0.52f), alpha = 0.08f * f * seg(p, 0.6f, 0.8f))
            tagAt(a.tag("adalamin1", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("adalamin1", 0, 1), Offset(w * 0.5f, h * 0.9f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("adalamin1", 0, 2), Offset(w * 0.5f, h * 0.52f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            for (k in 0 until 5) {
                val x = w * (0.82f - 0.15f * k)
                val q = seg(p, 0.1f + 0.1f * k, 0.25f + 0.1f * k)
                drawLine(Qn.GoldLight, Offset(x, g), Offset(x, g - h * 0.12f), w * 0.006f, alpha = f * q)
                val tip = Path().apply { moveTo(x - w * 0.03f, g - h * 0.12f); lineTo(x - w * 0.06f, g - h * 0.15f); lineTo(x - w * 0.03f, g - h * 0.18f); lineTo(x + w * 0.03f, g - h * 0.18f); lineTo(x + w * 0.03f, g - h * 0.12f); close() }
                drawPath(tip, Qn.Gold, alpha = f * q)
            }
            tagAt(a.tag("adalamin2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil129(f)
            tagAt(a.tag("adalamin3", 0, 0), Offset(w * 0.72f, h * 0.45f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            drawLine(Qn.GoldLight, Offset(w * 0.46f, h * 0.43f), Offset(w * 0.54f, h * 0.43f), w * 0.006f, alpha = f * seg(p, 0.25f, 0.35f))
            drawLine(Qn.GoldLight, Offset(w * 0.46f, h * 0.47f), Offset(w * 0.54f, h * 0.47f), w * 0.006f, alpha = f * seg(p, 0.25f, 0.35f))
            tagAt(a.tag("adalamin3", 0, 1), Offset(w * 0.28f, h * 0.45f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ad-rahman ═════════════
internal fun DrawScope.adrahmanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "adrahman", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night129(a) },
        { p, f ->
            veil129(f)
            softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.4f * seg(p, 0.1f, 0.4f), Qn.GoldLight, 0.45f * f)
            softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.14f * seg(p, 0.3f, 0.55f), Color(0xFFFFF2D0), 0.7f * f)
            row129(a, "adrahman0", 2, h * 0.16f, p, f, from = 0.1f, step = 0.25f, ink = ArtInk.Gold)
        },
        { p, f ->
            val spread = easeInOut(seg(p, 0.05f, 0.5f))
            drawArc(Qn.GoldLight, 180f, 180f, true, Offset(w * 0.5f - w * 0.5f * spread, g - h * 0.5f * spread), Size(w * spread, h * spread), alpha = 0.16f * f)
            for (k in 0 until 6) person99(Offset(w * (0.12f + 0.15f * k), g), h * 0.09f, Fig129, f * seg(p, 0.2f + 0.04f * k, 0.35f + 0.04f * k))
            tree104(Offset(w * 0.9f, g), h * 0.1f, Qn.Jade, Qn.Gold, f * seg(p, 0.3f, 0.45f))
            tagAt(a.tag("adrahman1", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 6) {
                val lit = k == 1 || k == 2 || k == 4
                val x = w * (0.12f + 0.15f * k)
                person99(Offset(x, g), h * 0.09f, if (lit) Fig129 else Dim129, f * (if (lit) 1f else 0.5f))
                if (lit) softGlow(Offset(x, g - h * 0.06f), w * 0.05f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f, 0.4f))
            }
            tagAt(a.tag("adrahman2", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("adrahman2", 0, 1), Offset(w * 0.5f, h * 0.4f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val spread = easeInOut(seg(p, 0.15f, 0.7f))
            drawArc(Qn.GoldLight, 180f, 180f, false, Offset(w * 0.5f - w * 0.55f * spread, h * 0.8f - h * 0.6f * spread), Size(w * 1.1f * spread, h * 1.2f * spread),
                alpha = 0.5f * f, style = Stroke(w * 0.005f))
            tree104(Offset(w * 0.2f, g), h * 0.1f, Qn.Jade, Qn.Gold, f)
            for (k in 0 until 3) person99(Offset(w * (0.45f + 0.15f * k), g), h * 0.09f, Fig129, f)
            tagAt(a.tag("adrahman3", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("adrahman3", 0, 1), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ad-din ═════════════
internal fun DrawScope.addinScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "addin", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night129(a) },
        { p, f ->
            veil129(f)
            book129(Offset(w * 0.5f, h * 0.45f), w * 0.07f, Qn.Gold, f * seg(p, 0.02f, 0.15f))
            tagAt(a.tag("addin0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("addin0", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 4) {
                val x = w * (0.17f + 0.22f * k)
                person99(Offset(x, g), h * 0.11f, Fig129, f * seg(p, 0.05f + 0.06f * k, 0.2f + 0.06f * k))
                if (k < 3) drawLine(Dim129, Offset(x + w * 0.11f, g), Offset(x + w * 0.11f, g - h * 0.2f), w * 0.003f, alpha = f * seg(p, 0.4f, 0.55f))
            }
            tagAt(a.tag("addin1", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil129(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.42f), w * 0.16f, Qn.Gold, f * seg(p, 0.1f, 0.3f), a.t)
            tagAt(a.tag("addin2", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.13f, Qn.Gold, f, a.t)
            for (k in 0 until 3) {
                val x = w * (0.3f + 0.2f * k)
                person99(Offset(x, g), h * 0.1f, Fig129, f)
                qIcon("scroll", Offset(x, g - h * 0.17f), w * 0.035f, Qn.GoldLight, f * seg(p, 0.2f + 0.1f * k, 0.35f + 0.1f * k), a.t)
            }
            tagAt(a.tag("addin3", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ad-iyyaka ═════════════
internal fun DrawScope.adiyyakaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "adiyyaka", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night129(a) },
        { p, f ->
            veil129(f)
            tagAt(a.tag("adiyyaka0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            qIcon("cross", Offset(w * 0.72f, h * 0.44f), w * 0.06f, Qn.Rose, f * seg(p, 0.55f, 0.68f), a.t)
            qIcon("check", Offset(w * 0.28f, h * 0.44f), w * 0.06f, Qn.Jade, f * seg(p, 0.7f, 0.82f), a.t)
            row129(a, "adiyyaka0", 2, h * 0.6f, p, f, from = 0.55f, step = 0.15f, first = 1)
        },
        { p, f ->
            val off = seg(p, 0.15f, 0.45f)
            for (k in 0 until 4) {
                val x = w * (0.08f + 0.09f * k)
                val tilt = off * (0.4f + 0.1f * k)
                drawRoundRect(Dim129, Offset(x - w * 0.025f, g - h * 0.12f + h * 0.06f * tilt), Size(w * 0.05f, h * 0.12f), CornerRadius(w * 0.01f), alpha = f * (1f - 0.6f * off))
            }
            qIcon("cross", Offset(w * 0.2f, g - h * 0.2f), w * 0.05f, Qn.Rose, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("adiyyaka1", 0, 1), Offset(w * 0.22f, h * 0.4f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            person99(Offset(w * 0.72f, g), h * 0.13f, Fig129, f * seg(p, 0.5f, 0.62f))
            qIcon("mat", Offset(w * 0.72f, g + h * 0.02f), w * 0.08f, Qn.Jade, f * seg(p, 0.5f, 0.62f), a.t)
            drawLine(Qn.GoldLight, Offset(w * 0.72f, g - h * 0.18f), Offset(w * 0.72f, h * 0.26f), w * 0.005f, alpha = f * seg(p, 0.6f, 0.75f), cap = StrokeCap.Round)
            tagAt(a.tag("adiyyaka1", 0, 0), Offset(w * 0.72f, h * 0.18f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil129(f)
            val swap = easeInOut(seg(p, 0.15f, 0.45f))
            tagAt(a.tag("adiyyaka2", 0, 0), Offset(w * mix(0.3f, 0.7f, swap), h * 0.42f), f, ink = ArtInk.Gold)
            tagAt(a.tag("adiyyaka2", 0, 1), Offset(w * mix(0.7f, 0.3f, swap), h * 0.42f), f, ink = ArtInk.Ivory)
            tagAt(a.tag("adiyyaka2", 0, 2), Offset(w * 0.7f, h * 0.6f), f * seg(p, 0.45f, 0.58f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("adiyyaka2", 0, 3), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.75f, 0.88f), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig129, f)
            qIcon("hands", Offset(w * 0.5f, g - h * 0.26f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            drawLine(Qn.GoldLight, Offset(w * 0.5f, g - h * 0.32f), Offset(w * 0.5f, h * 0.24f), w * 0.005f, alpha = f * seg(p, 0.3f, 0.45f), cap = StrokeCap.Round)
            tagAt(a.tag("adiyyaka3", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ad-anam ═════════════
internal fun DrawScope.adanamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "adanam", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night129(a) },
        { p, f ->
            path129(f, easeInOut(seg(p, 0.05f, 0.5f)))
            tagAt(a.tag("adanam0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            book129(Offset(w * 0.82f, h * 0.4f), w * 0.04f, Qn.Lapis, f * seg(p, 0.6f, 0.72f))
            tagAt(a.tag("adanam0", 0, 1), Offset(w * 0.82f, h * 0.52f), f * seg(p, 0.62f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            path129(f)
            for (i in 0 until 4) {
                val y = h * (0.86f - 0.14f * i)
                val c = Offset(w * 0.5f, y)
                val q = seg(p, 0.1f + 0.2f * i, 0.25f + 0.2f * i)
                if (i == 0) light129(c, w * 0.018f, a, "adanam1", -1, f * q) else drawCircle(Fig129, w * 0.016f, c, alpha = f * q)
                tagAt(a.tag("adanam1", 0, i), Offset(if (i % 2 == 0) w * 0.78f else w * 0.22f, y), f * q, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            path129(f)
            close97(Offset(w * 0.5f, h * 0.6f), w * 0.09f, a, "adanam2", 0, 0, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("adanam2", 0, 1), Offset(w * 0.5f, h * 0.38f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil129(f)
            plain97(Offset(w * 0.5f, h * 0.4f), w * 0.09f, a, "adanam3", 0, f * seg(p, 0.02f, 0.15f))
            tagAt(a.tag("adanam3", 0, 1), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ad-maghdub ═════════════
internal fun DrawScope.admaghdubScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "admaghdub", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night129(a) },
        { p, f ->
            path129(f)
            val q1 = easeInOut(seg(p, 0.15f, 0.4f))
            drawLine(Color(0xFF8A2A2A), Offset(w * 0.56f, h * 0.7f), Offset(mix(w * 0.56f, w * 0.88f, q1), mix(h * 0.7f, h * 0.48f, q1)), w * 0.03f, alpha = 0.8f * f, cap = StrokeCap.Round)
            val q2 = easeInOut(seg(p, 0.5f, 0.75f))
            drawLine(Dim129, Offset(w * 0.44f, h * 0.7f), Offset(mix(w * 0.44f, w * 0.12f, q2), mix(h * 0.7f, h * 0.48f, q2)), w * 0.03f, alpha = 0.8f * f, cap = StrokeCap.Round)
            tagAt(a.tag("admaghdub0", 0, 0), Offset(w * 0.8f, h * 0.36f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("admaghdub0", 0, 1), Offset(w * 0.2f, h * 0.36f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil129(f)
            sirajMunir(Offset(w * 0.72f, h * 0.45f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.72f, h * 0.33f))
            plain97(Offset(w * 0.28f, h * 0.45f), w * 0.08f, a, "admaghdub1", 0, f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("admaghdub1", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig129, f)
            val lamp = Offset(w * 0.6f, g - h * 0.12f)
            softGlow(lamp, w * 0.06f, Qn.GoldLight, 0.8f * f * seg(p, 0.1f, 0.25f))
            drawCircle(Qn.GoldLight, w * 0.015f, lamp, alpha = f * seg(p, 0.1f, 0.25f))
            val turn = seg(p, 0.45f, 0.6f)
            drawLine(Color(0xFF8A2A2A), Offset(w * 0.45f, g - h * 0.05f), Offset(w * 0.45f - w * 0.25f * turn, g - h * 0.05f), w * 0.02f, alpha = 0.8f * f * turn, cap = StrokeCap.Round)
            tagAt(a.tag("admaghdub2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFFB8BCC8), alpha = 0.22f * f * seg(p, 0.05f, 0.3f))
            person99(Offset(mix(w * 0.3f, w * 0.7f, 0.5f + 0.5f * sin(a.t * 0.8f)), g), h * 0.13f, Dim129, f, lean = 0.1f * sin(a.t * 1.6f))
            for (k in 0 until 4) {
                val ph = ((a.t * 0.2f + k / 4f) % 1f)
                drawOval(Color(0xFFD8DCE4), Offset(w * (ph * 1.2f - 0.3f), h * (0.45f + 0.08f * k)), Size(w * 0.4f, h * 0.06f), alpha = 0.25f * f)
            }
            tagAt(a.tag("admaghdub3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
    )
}
