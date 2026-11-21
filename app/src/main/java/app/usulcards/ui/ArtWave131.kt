package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 131 (2026-11-21): transactions from al-Sharh al-Mumti' (abridged), each drawn from the book's own example:
 * the medicine with its price and the jar for the money, the seven conditions, two sacks for three (riba), the
 * farmer paid now for dates in a year (salam), the loan with the lender in the borrower's house, the debt moved
 * from one man to another (hawala), the money and the work split by a share (mudaraba), and the price a defect
 * takes away (1000 down to 800). People are faceless; the Prophet ﷺ only his named light.
 */

private val Fig131 = Color(0xFFEDE4D0)
private val Dim131 = Color(0xFF8A8296)
private val Sack131 = Color(0xFFC9A46A)

private fun DrawScope.night131(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil131(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row131(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             first: Int = 0, ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, first + i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

private fun DrawScope.sack131(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val p = Path().apply {
        moveTo(c.x - s * 0.35f, c.y - s * 0.55f)
        lineTo(c.x + s * 0.35f, c.y - s * 0.55f)
        quadraticBezierTo(c.x + s * 0.75f, c.y + s * 0.1f, c.x + s * 0.5f, c.y + s * 0.5f)
        lineTo(c.x - s * 0.5f, c.y + s * 0.5f)
        quadraticBezierTo(c.x - s * 0.75f, c.y + s * 0.1f, c.x - s * 0.35f, c.y - s * 0.55f)
        close()
    }
    drawPath(p, color, alpha = alpha)
    drawLine(Color(0xFF6E5432), Offset(c.x - s * 0.35f, c.y - s * 0.42f), Offset(c.x + s * 0.35f, c.y - s * 0.42f), s * 0.08f, alpha = alpha)
}

private fun DrawScope.coin131(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(Qn.Gold, r, c, alpha = alpha)
    drawCircle(Qn.GoldLight, r * 0.7f, c, alpha = alpha, style = Stroke(r * 0.15f))
}

private fun DrawScope.car131(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, Offset(c.x - s, c.y - s * 0.35f), Size(s * 2f, s * 0.5f), CornerRadius(s * 0.15f), alpha = alpha)
    drawRoundRect(color, Offset(c.x - s * 0.55f, c.y - s * 0.75f), Size(s * 1.1f, s * 0.45f), CornerRadius(s * 0.2f), alpha = alpha)
    for (k in listOf(-0.55f, 0.55f)) drawCircle(Color(0xFF2A2140), s * 0.2f, Offset(c.x + s * k, c.y + s * 0.15f), alpha = alpha)
}

private fun DrawScope.box131(c: Offset, s: Float, color: Color, alpha: Float, crack: Float = 0f) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, Offset(c.x - s, c.y - s * 0.7f), Size(s * 2f, s * 1.4f), CornerRadius(s * 0.1f), alpha = alpha)
    drawLine(Color(0xFF2A2140), Offset(c.x - s, c.y - s * 0.3f), Offset(c.x + s, c.y - s * 0.3f), s * 0.05f, alpha = alpha * 0.6f)
    if (crack > 0f) {
        val p = Path().apply { moveTo(c.x - s * 0.2f, c.y - s * 0.7f); lineTo(c.x + s * 0.05f, c.y - s * 0.2f); lineTo(c.x - s * 0.1f, c.y + s * 0.1f); lineTo(c.x + s * 0.2f, c.y + s * 0.6f) }
        drawPath(p, Color(0xFF2A2140), alpha = alpha * crack, style = Stroke(s * 0.07f, cap = StrokeCap.Round))
    }
}

/** A share of a whole: `frac` of a gold disc, the rest dim. */
private fun DrawScope.pie131(c: Offset, r: Float, frac: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(Dim131, r, c, alpha = alpha * 0.7f)
    drawArc(Qn.Gold, -90f, 360f * frac, true, Offset(c.x - r, c.y - r), Size(r * 2f, r * 2f), alpha = alpha)
}

private fun DrawScope.arrow131(from: Offset, to: Offset, q: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f || q <= 0f) return
    val e = Offset(mix(from.x, to.x, q), mix(from.y, to.y, q))
    drawLine(color, from, e, size.width * 0.006f, alpha = alpha, cap = StrokeCap.Round)
    drawCircle(color, size.width * 0.012f, e, alpha = alpha)
}

// ═════════════ md-sigha ═════════════
internal fun DrawScope.mmsighaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmsigha", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.78f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.22f, g), h * 0.13f, Fig131, f)
            row131(a, "mmsigha0", 3, h * 0.32f, p, f, from = 0.1f, step = 0.15f)
        },
        { p, f ->
            veil131(f)
            row131(a, "mmsigha1", 2, h * 0.36f, p, f, from = 0.2f, step = 0.35f, ink = ArtInk.Gold)
            for (k in 0 until 2) {
                val x = w * (0.71f - 0.42f * k)
                qIcon(if (k == 0) "lips" else "hands", Offset(x, h * 0.56f), w * 0.07f, Fig131, f * seg(p, 0.25f + 0.35f * k, 0.38f + 0.35f * k), a.t)
            }
        },
        { p, f ->
            box131(Offset(w * 0.68f, g - h * 0.06f), w * 0.06f, Qn.Jade, f * (1f - 0.8f * seg(p, 0.7f, 0.85f)))
            tagAt(a.tag("mmsigha2", 0, 1), Offset(w * 0.68f, g - h * 0.17f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            drawRoundRect(Dim131, Offset(w * 0.4f, g - h * 0.1f), Size(w * 0.12f, h * 0.1f), CornerRadius(w * 0.02f), alpha = f, style = Stroke(w * 0.006f))
            coin131(Offset(w * 0.46f, mix(h * 0.4f, g - h * 0.06f, easeInOut(seg(p, 0.35f, 0.55f)))), w * 0.02f, f * seg(p, 0.3f, 0.4f))
            person99(Offset(w * 0.22f, g), h * 0.13f, Fig131, f)
            tagAt(a.tag("mmsigha2", 0, 0), Offset(w * 0.46f, h * 0.24f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.75f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.25f, g), h * 0.13f, Fig131, f)
            val q = easeInOut(seg(p, 0.1f, 0.4f))
            qIcon("book", Offset(mix(w * 0.66f, w * 0.34f, q), g - h * 0.14f), w * 0.04f, Qn.Gold, f, a.t)
            coin131(Offset(mix(w * 0.34f, w * 0.66f, easeInOut(seg(p, 0.4f, 0.7f))), g - h * 0.1f), w * 0.02f, f * seg(p, 0.4f, 0.5f))
            qIcon("lips", Offset(w * 0.25f, h * 0.36f), w * 0.05f, Dim131, f * seg(p, 0.6f, 0.7f), a.t)
            qIcon("cross", Offset(w * 0.25f, h * 0.36f), w * 0.06f, Qn.Rose, f * seg(p, 0.65f, 0.75f), a.t)
            tagAt(a.tag("mmsigha3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ md-shurut ═════════════
internal fun DrawScope.mmshurutScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mmshurut", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (i in 0 until 3) {
                val y = h * (0.28f + 0.17f * i)
                qIcon("check", Offset(w * 0.84f, y), w * 0.04f, Qn.Jade, f * seg(p, 0.1f + 0.25f * i, 0.2f + 0.25f * i), a.t)
                tagAt(a.tag("mmshurut0", 0, i), Offset(w * 0.48f, y), f * seg(p, 0.1f + 0.25f * i, 0.24f + 0.25f * i), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil131(f)
            for (i in 0 until 2) {
                val y = h * (0.34f + 0.2f * i)
                qIcon("check", Offset(w * 0.84f, y), w * 0.04f, Qn.Jade, f * seg(p, 0.1f + 0.4f * i, 0.2f + 0.4f * i), a.t)
                tagAt(a.tag("mmshurut1", 0, i), Offset(w * 0.48f, y), f * seg(p, 0.1f + 0.4f * i, 0.24f + 0.4f * i), ink = ArtInk.Ivory)
            }
            qIcon("key", Offset(w * 0.16f, h * 0.34f), w * 0.05f, Qn.Gold, f * seg(p, 0.15f, 0.3f), a.t)
            qIcon("hands", Offset(w * 0.16f, h * 0.54f), w * 0.05f, Qn.Gold, f * seg(p, 0.55f, 0.7f), a.t)
        },
        { p, f ->
            veil131(f)
            box131(Offset(w * 0.72f, h * 0.42f), w * 0.07f, Qn.Jade, f * seg(p, 0.05f, 0.2f))
            qIcon("eye", Offset(w * 0.72f, h * 0.26f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            coin131(Offset(w * 0.28f, h * 0.42f), w * 0.05f, f * seg(p, 0.4f, 0.55f))
            qIcon("eye", Offset(w * 0.28f, h * 0.26f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            row131(a, "mmshurut2", 2, h * 0.64f, p, f, from = 0.1f, step = 0.4f, ink = ArtInk.Gold)
        },
        { p, f ->
            val g = h * 0.8f
            qIcon("house", Offset(w * 0.68f, g - h * 0.1f), w * 0.14f, Fig131, f, a.t)
            person99(Offset(w * 0.32f, g), h * 0.13f, Dim131, f)
            qIcon("scale", Offset(w * 0.5f, h * 0.3f), w * 0.1f, Qn.Gold, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("mmshurut3", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ md-riba ═════════════
internal fun DrawScope.mmribaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.78f
    film98(a, "mmriba", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            for (k in 0 until 2) sack131(Offset(w * (0.68f + 0.12f * k), g), w * 0.08f, Sack131, f)
            for (k in 0 until 3) sack131(Offset(w * (0.14f + 0.12f * k), g), w * 0.08f, Sack131, f * seg(p, 0.15f + 0.08f * k, 0.25f + 0.08f * k))
            qIcon("cross", Offset(w * 0.5f, g - h * 0.05f), w * 0.06f, Qn.Rose, f * seg(p, 0.5f, 0.62f), a.t)
            tagAt(a.tag("mmriba0", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            sack131(Offset(w * 0.74f, g), w * 0.08f, Sack131, f)
            sack131(Offset(w * 0.26f, g), w * 0.08f, Color(0xFFB89A7A), f * seg(p, 0.5f, 0.65f))
            qIcon("hourglass", Offset(w * 0.5f, g - h * 0.06f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.48f), w * 0.05f, Qn.Rose, f * seg(p, 0.65f, 0.75f), a.t)
            tagAt(a.tag("mmriba1", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            car131(Offset(w * 0.74f, g - h * 0.04f), w * 0.08f, Qn.Lapis, f)
            for (k in 0 until 2) car131(Offset(w * (0.18f + 0.2f * k), g - h * 0.04f), w * 0.07f, Qn.Lapis, f * seg(p, 0.2f + 0.1f * k, 0.3f + 0.1f * k))
            qIcon("check", Offset(w * 0.53f, h * 0.48f), w * 0.05f, Qn.Jade, f * seg(p, 0.55f, 0.68f), a.t)
            tagAt(a.tag("mmriba2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.68f, h * 0.42f), w * 0.12f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("hands", Offset(w * 0.32f, h * 0.42f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.45f, 0.6f), a.t)
            row131(a, "mmriba3", 2, h * 0.64f, p, f, from = 0.1f, step = 0.35f, ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-salam ═════════════
internal fun DrawScope.mmsalamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmsalam", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.8f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.2f, g), h * 0.13f, Fig131, f)
            coin131(Offset(mix(w * 0.72f, w * 0.28f, easeInOut(seg(p, 0.1f, 0.35f))), g - h * 0.12f), w * 0.022f, f)
            qIcon("hourglass", Offset(w * 0.5f, h * 0.4f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.4f, 0.55f), a.t)
            sack131(Offset(mix(w * 0.28f, w * 0.72f, easeInOut(seg(p, 0.65f, 0.9f))), g - h * 0.04f), w * 0.05f, Sack131, f * seg(p, 0.6f, 0.68f))
            tagAt(a.tag("mmsalam0", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            palm12(Offset(w * 0.14f, g), h * 0.28f, Color(0xFF7A5A3A), Qn.Jade, f, a.t)
            person99(Offset(w * 0.32f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.76f, g), h * 0.13f, Fig131, f)
            for (k in 0 until 3) coin131(Offset(mix(w * 0.68f, w * 0.4f, easeInOut(seg(p, 0.1f + 0.05f * k, 0.35f + 0.05f * k))), g - h * (0.1f + 0.03f * k)), w * 0.02f, f)
            tagAt(a.tag("mmsalam1", 0, 0), Offset(w * 0.62f, h * 0.36f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("mmsalam1", 0, 1), Offset(w * 0.3f, h * 0.24f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.75f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.25f, g), h * 0.13f, Fig131, f)
            softGlow(Offset(w * 0.75f, g - h * 0.08f), w * 0.08f, Qn.GoldLight, 0.6f * f * seg(p, 0.1f, 0.25f))
            softGlow(Offset(w * 0.25f, g - h * 0.08f), w * 0.08f, Qn.GoldLight, 0.6f * f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("mmsalam2", 0, 0), Offset(w * 0.72f, h * 0.34f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("mmsalam2", 0, 1), Offset(w * 0.28f, h * 0.46f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            for (i in 0 until 4) {
                val x = w * (0.82f - 0.213f * i)
                val bad = i == 1
                drawRoundRect(if (bad) Color(0xFF6A3A44) else Color(0xFF2E4A3A), Offset(x - w * 0.09f, h * 0.36f), Size(w * 0.18f, h * 0.16f), CornerRadius(w * 0.02f), alpha = f * seg(p, 0.05f + 0.15f * i, 0.15f + 0.15f * i))
                qIcon(if (bad) "cross" else "check", Offset(x, h * 0.44f), w * 0.04f, if (bad) Qn.Rose else Qn.Jade, f * seg(p, 0.1f + 0.15f * i, 0.2f + 0.15f * i), a.t)
            }
            tagAt(a.tag("mmsalam3", 0, 0), Offset(w * 0.78f, h * 0.62f), f * seg(p, 0.1f, 0.24f), ink = ArtInk.Ivory)
            tagAt(a.tag("mmsalam3", 0, 1), Offset(w * 0.6f, h * 0.74f), f * seg(p, 0.25f, 0.39f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ md-qard ═════════════
internal fun DrawScope.mmqardScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmqard", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.78f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.22f, g), h * 0.13f, Fig131, f)
            coin131(Offset(mix(w * 0.7f, w * 0.3f, easeInOut(seg(p, 0.1f, 0.4f))), g - h * 0.12f), w * 0.025f, f)
            coin131(Offset(mix(w * 0.3f, w * 0.7f, easeInOut(seg(p, 0.55f, 0.85f))), g - h * 0.18f), w * 0.025f, f * seg(p, 0.5f, 0.6f))
            qIcon("heart", Offset(w * 0.5f, h * 0.36f), w * 0.06f, Qn.Rose, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("mmqard0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            coin131(Offset(w * 0.6f, h * 0.44f), w * 0.05f, f)
            drawLine(Qn.Rose, Offset(w * 0.55f, h * 0.44f), Offset(w * 0.42f, h * 0.44f), w * 0.006f, alpha = f * seg(p, 0.2f, 0.35f))
            qIcon("house", Offset(w * 0.34f, h * 0.44f), w * 0.1f, Fig131, f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("cross", Offset(w * 0.47f, h * 0.3f), w * 0.05f, Qn.Rose, f * seg(p, 0.55f, 0.68f), a.t)
            tagAt(a.tag("mmqard1", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.28f, g - h * 0.1f), w * 0.16f, Fig131, f, a.t)
            person99(Offset(mix(w * 0.8f, w * 0.38f, easeInOut(seg(p, 0.45f, 0.75f))), g), h * 0.12f, Dim131, f, walk = a.t * 3f)
            for (k in 0 until 3) coin131(Offset(w * 0.78f, g - h * (0.18f + 0.04f * k)), w * 0.02f, f * seg(p, 0.05f + 0.05f * k, 0.15f + 0.05f * k))
            qIcon("cross", Offset(w * 0.28f, h * 0.36f), w * 0.06f, Qn.Rose, f * seg(p, 0.78f, 0.9f), a.t)
            tagAt(a.tag("mmqard2", 0, 0), Offset(w * 0.74f, h * 0.3f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("mmqard2", 0, 1), Offset(w * 0.32f, h * 0.22f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.72f, h * 0.42f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.72f, h * 0.3f))
            coin131(Offset(w * 0.3f, h * 0.5f), w * mix(0.03f, 0.05f, seg(p, 0.3f, 0.5f)), f)
            qIcon("check", Offset(w * 0.3f, h * 0.36f), w * 0.05f, Qn.Jade, f * seg(p, 0.5f, 0.62f), a.t)
            tagAt(a.tag("mmqard3", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-hawala ═════════════
internal fun DrawScope.mmhawalaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    val xs = listOf(0.8f, 0.5f, 0.2f)
    film98(a, "mmhawala", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            for (k in 0 until 3) person99(Offset(w * xs[k], g), h * 0.12f, if (k == 1) Fig131 else Dim131, f)
            arrow131(Offset(w * 0.78f, h * 0.48f), Offset(w * 0.52f, h * 0.48f), seg(p, 0.05f, 0.25f), Qn.GoldLight, f)
            arrow131(Offset(w * 0.48f, h * 0.48f), Offset(w * 0.22f, h * 0.48f), seg(p, 0.3f, 0.5f), Qn.GoldLight, f)
            arrow131(Offset(w * 0.78f, h * 0.36f), Offset(w * 0.22f, h * 0.36f), easeInOut(seg(p, 0.6f, 0.85f)), Qn.Gold, f)
            tagAt(a.tag("mmhawala0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 3) person99(Offset(w * xs[k], g), h * 0.12f, if (k == 1) Fig131 else Dim131, f)
            val q = easeInOut(seg(p, 0.3f, 0.7f))
            coin131(Offset(w * 0.5f, mix(h * 0.5f, h * 0.36f, q)), w * 0.025f, f * (1f - q))
            coin131(Offset(w * 0.2f, mix(h * 0.36f, h * 0.5f, q)), w * 0.025f, f * q)
            tagAt(a.tag("mmhawala1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("mmhawala1", 0, 1), Offset(w * 0.2f, h * 0.24f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            qIcon("lock", Offset(w * 0.72f, h * 0.42f), w * 0.07f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("scale", Offset(w * 0.28f, h * 0.42f), w * 0.09f, Qn.Gold, f * seg(p, 0.45f, 0.6f), a.t)
            row131(a, "mmhawala2", 2, h * 0.64f, p, f, from = 0.1f, step = 0.35f)
        },
        { p, f ->
            sack131(Offset(w * 0.72f, g - h * 0.02f), w * 0.09f, Sack131, f)
            sack131(Offset(w * 0.28f, g - h * 0.02f), w * 0.09f, Color(0xFFB89A7A), f)
            qIcon("cross", Offset(w * 0.5f, h * 0.5f), w * 0.06f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            row131(a, "mmhawala3", 2, h * 0.4f, p, f, from = 0.1f, step = 0.15f)
            tagAt(a.tag("mmhawala3", 0, 2), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-mudaraba ═════════════
internal fun DrawScope.mmmudarabaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmmudaraba", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.78f, g), h * 0.13f, Fig131, f)
            for (k in 0 until 3) coin131(Offset(w * 0.78f, g - h * (0.2f + 0.04f * k)), w * 0.02f, f * seg(p, 0.05f, 0.2f))
            person99(Offset(w * 0.22f, g), h * 0.13f, Fig131, f)
            qIcon("hands", Offset(w * 0.22f, g - h * 0.24f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.25f, 0.4f), a.t)
            row131(a, "mmmudaraba0", 2, h * 0.3f, p, f, from = 0.1f, step = 0.3f)
        },
        { p, f ->
            person99(Offset(w * 0.78f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.22f, g), h * 0.13f, Fig131, f)
            for (k in 0 until 3) coin131(Offset(mix(w * 0.72f, w * 0.28f, easeInOut(seg(p, 0.05f + 0.05f * k, 0.3f + 0.05f * k))), g - h * (0.12f + 0.03f * k)), w * 0.02f, f)
            pie131(Offset(w * 0.5f, h * 0.36f), w * 0.07f, 0.5f * seg(p, 0.5f, 0.7f), f * seg(p, 0.45f, 0.55f))
            tagAt(a.tag("mmmudaraba1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            val fr = listOf(0.5f, 0.25f, 0.75f)
            for (i in 0 until 3) pie131(Offset(w * (0.5f + (1 - i) * 0.28f), h * 0.42f), w * 0.08f, fr[i], f * seg(p, 0.1f + 0.2f * i, 0.22f + 0.2f * i))
            row131(a, "mmmudaraba2", 3, h * 0.62f, p, f, from = 0.1f, step = 0.2f)
        },
        { p, f ->
            veil131(f)
            pie131(Offset(w * 0.5f, h * 0.42f), w * 0.12f, 0.5f, f * seg(p, 0.05f, 0.2f))
            drawLine(Fig131, Offset(w * 0.5f, h * 0.42f - w * 0.13f), Offset(w * 0.5f, h * 0.42f + w * 0.13f), w * 0.006f, alpha = f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("mmmudaraba3", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-ayb ═════════════
internal fun DrawScope.mmaybScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mmayb", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            box131(Offset(w * 0.5f, h * 0.42f), w * 0.1f, Qn.Jade, f, crack = seg(p, 0.3f, 0.5f))
            tagAt(a.tag("mmayb0", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            val drop = easeInOut(seg(p, 0.45f, 0.75f))
            drawRoundRect(Qn.Gold, Offset(w * 0.62f, h * 0.7f - h * 0.4f), Size(w * 0.14f, h * 0.4f), CornerRadius(w * 0.02f), alpha = f * seg(p, 0.05f, 0.2f))
            drawRoundRect(Qn.Gold, Offset(w * 0.26f, h * 0.7f - h * 0.32f), Size(w * 0.14f, h * 0.32f), CornerRadius(w * 0.02f), alpha = f * drop)
            drawRoundRect(Qn.Rose, Offset(w * 0.26f, h * 0.7f - h * 0.4f), Size(w * 0.14f, h * 0.08f), CornerRadius(w * 0.02f), alpha = 0.5f * f * drop, style = Stroke(w * 0.005f))
            tagAt(a.tag("mmayb1", 0, 0), Offset(w * 0.69f, h * 0.22f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("mmayb1", 0, 1), Offset(w * 0.33f, h * 0.22f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            box131(Offset(w * 0.72f, h * 0.42f), w * 0.08f, Qn.Jade, f, crack = 1f)
            box131(Offset(w * 0.28f, h * 0.42f), w * 0.08f, Qn.Jade, f * seg(p, 0.45f, 0.6f))
            softGlow(Offset(w * 0.28f, h * 0.36f), w * 0.1f, Qn.GoldLight, 0.6f * f * seg(p, 0.55f, 0.7f))
            row131(a, "mmayb2", 2, h * 0.64f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            qIcon("drop", Offset(w * 0.72f, h * 0.42f), w * 0.07f, Color(0xFFCFE6F2), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.28f, h * 0.42f), w * 0.06f, Qn.Rose, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "mmayb3", 2, h * 0.62f, p, f, from = 0.1f, step = 0.4f)
        },
    )
}
