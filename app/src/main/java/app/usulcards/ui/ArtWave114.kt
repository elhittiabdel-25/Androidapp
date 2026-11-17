package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 114 (2026-11-17): «مصطلح الحديث» (Hafiz al-Hakami). A scholar's desk: the isnad as a chain of rings that
 * light up link by link, books on a shelf, a sieve-like balance between accepted and rejected. He ﷺ is only his
 * named light at the head of the chain; narrators are plain discs, never figures.
 */

private val Good114 = Color(0xCC1A3020)
private val Bad114 = Color(0xCC401A20)
private val Spine114 = listOf(Color(0xFF1E5A3E), Color(0xFF2E4E6A), Color(0xFF6A3E2A), Color(0xFF4A5A2E), Color(0xFF6A2E4A), Color(0xFF5A4A2A))

private fun DrawScope.desk114(a: ArtCtx) {
    drawRect(Color(0xFF1A1420))
    drawRect(Color(0xFF3A2A1E), Offset(0f, size.height * 0.82f), Size(size.width, size.height * 0.18f))
    for (k in 0 until 14) drawRect(Spine114[k % 6], Offset(size.width * (0.03f + 0.07f * k), size.height * (0.1f + 0.02f * (k % 3))), Size(size.width * 0.05f, size.height * (0.14f - 0.02f * (k % 3))), alpha = 0.25f)
}

private fun DrawScope.veil114(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.6f * alpha)

private fun DrawScope.row114(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.14f,
                             back: Color = Color(0xB3101426), ink: Color = ArtInk.Ivory, first: Int = 0) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, first + i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), back = back, ink = ink)
    }
}

/** The isnad: rings from the right (the Prophet's ﷺ light) to the left, lit up to [lit]. */
private fun DrawScope.isnad114(a: ArtCtx, y: Float, n: Int, alpha: Float, lit: Float, broken: Int = -1) {
    val w = size.width
    sirajMunir(Offset(w * 0.88f, y), w * 0.03f, alpha, a.t, named = false)
    for (k in 0 until n) {
        if (k == broken) continue
        val c = Offset(w * (0.76f - 0.62f * k / (n - 1f)), y)
        drawCircle(if (k / (n - 1f) <= lit) Qn.GoldLight else Color(0xFF6A6474), w * 0.032f, c, alpha = alpha, style = Stroke(3f * density))
    }
}

private fun DrawScope.book114(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, c + Offset(-s * 0.4f, -s * 0.55f), Size(s * 0.8f, s * 1.1f), CornerRadius(s * 0.08f), alpha = alpha)
    drawRect(Qn.GoldLight, c + Offset(-s * 0.4f, -s * 0.55f), Size(s * 0.1f, s * 1.1f), alpha = 0.6f * alpha)
}

// ═════════════ mh-ilm ═════════════
internal fun DrawScope.mhilmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mhilm", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { desk114(a) },
        { p, f ->
            veil114(f)
            isnad114(a, h * 0.42f, 5, f * seg(p, 0.05f, 0.2f), seg(p, 0.2f, 0.8f))
            tagAt(a.tag("mhilm0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("mhilm0", 0, 1), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil114(f)
            isnad114(a, h * 0.4f, 5, f, 1f)
            qIcon("scroll", Offset(w * 0.12f, h * 0.4f), w * 0.07f, Color(0xFFD8C4A8), f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("mhilm1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("mhilm1", 0, 1), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
            tagAt(a.tag("mhilm1", 0, 2), Offset(w * 0.12f, h * 0.56f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil114(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.14f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("mhilm2", 0, 0), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.25f, 0.4f), back = Good114, ink = ArtInk.Jade)
            tagAt(a.tag("mhilm2", 0, 1), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.5f, 0.65f), back = Bad114, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil114(f)
            for (k in 0 until 20) drawCircle(Spine114[k % 6], w * 0.02f, Offset(w * (0.2f + 0.065f * (k % 10)), h * (0.34f + 0.1f * (k / 10))), alpha = f * seg(p, 0.05f + 0.02f * k, 0.12f + 0.02f * k))
            tagAt(a.tag("mhilm3", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mh-kutub ═════════════
internal fun DrawScope.mhkutubScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mhkutub", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { desk114(a) },
        { p, f ->
            veil114(f)
            for (k in 0 until 2) {
                val c = Offset(w * (0.66f - 0.32f * k), h * 0.42f)
                val on = f * seg(p, 0.1f + 0.3f * k, 0.25f + 0.3f * k)
                book114(c, w * 0.12f, Spine114[k], on)
                tagAt(a.tag("mhkutub0", 0, k), c + Offset(0f, h * 0.17f), on, ink = ArtInk.Gold)
            }
        },
        { p, f ->
            veil114(f)
            qIcon("book", Offset(w * 0.5f, h * 0.24f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 2) book114(Offset(w * (0.6f - 0.2f * k), h * 0.52f), w * 0.09f, Spine114[k], f)
            tagAt(a.tag("mhkutub1", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.3f, 0.45f), back = Good114, ink = ArtInk.Jade)
        },
        { p, f ->
            veil114(f)
            for (k in 0 until 4) {
                val c = Offset(w * (0.8f - 0.2f * k), h * 0.42f)
                val on = f * seg(p, 0.1f + 0.16f * k, 0.25f + 0.16f * k)
                book114(c, w * 0.09f, Spine114[k + 2], on)
                tagAt(a.tag("mhkutub2", 0, k), c + Offset(0f, h * 0.14f), on, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil114(f)
            for (k in 0 until 6) book114(Offset(w * (0.82f - 0.128f * k), h * 0.42f), w * 0.08f, Spine114[k], f * seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k))
            tagAt(a.tag("mhkutub3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mh-mutawatir ═════════════
internal fun DrawScope.mhmutawatirScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mhmutawatir", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { desk114(a) },
        { p, f ->
            veil114(f)
            sirajMunir(Offset(w * 0.88f, h * 0.4f), w * 0.03f, f, a.t, named = false)
            for (r in 0 until 5) for (k in 0 until 4) {
                val c = Offset(w * (0.74f - 0.15f * k), h * (0.26f + 0.07f * r))
                drawCircle(Roundel.PlainFill, w * 0.018f, c, alpha = f * seg(p, 0.05f + 0.04f * k, 0.15f + 0.04f * k))
                if (k > 0) drawLine(Qn.GoldLight, c, c + Offset(w * 0.15f, 0f), 1f * density, alpha = 0.5f * f * seg(p, 0.3f, 0.5f))
            }
            tagAt(a.tag("mhmutawatir0", 0, 0), Offset(w * 0.4f, h * 0.68f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("mhmutawatir0", 0, 1), Offset(w * 0.4f, h * 0.78f), f * seg(p, 0.55f, 0.7f), back = Good114, ink = ArtInk.Jade)
        },
        { p, f ->
            veil114(f)
            row114(a, "mhmutawatir1", 2, h * 0.34f, p, f, ink = ArtInk.Gold)
            row114(a, "mhmutawatir1", 2, h * 0.5f, p, f, from = 0.45f, ink = ArtInk.Gold, first = 2)
        },
        { p, f ->
            veil114(f)
            for (k in 0 until 3) {
                val x = w * (0.75f - 0.25f * k)
                val n = 3 - k
                for (r in 0 until n) drawCircle(Roundel.PlainFill, w * 0.02f, Offset(x - w * 0.04f * (n - 1) / 2f + w * 0.04f * r, h * 0.38f), alpha = f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k))
            }
            row114(a, "mhmutawatir2", 3, h * 0.52f, p, f, step = 0.2f)
        },
        { p, f ->
            veil114(f)
            row114(a, "mhmutawatir3", 3, h * 0.42f, p, f, step = 0.2f)
        },
    )
}

// ═════════════ mh-sahih ═════════════
internal fun DrawScope.mhsahihScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mhsahih", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { desk114(a) },
        { p, f ->
            veil114(f)
            isnad114(a, h * 0.26f, 5, f, seg(p, 0.1f, 0.9f))
            row114(a, "mhsahih0", 3, h * 0.48f, p, f, step = 0.12f, back = Good114, ink = ArtInk.Jade)
            row114(a, "mhsahih0", 2, h * 0.6f, p, f, from = 0.5f, step = 0.12f, back = Good114, ink = ArtInk.Jade, first = 3)
        },
        { p, f ->
            veil114(f)
            isnad114(a, h * 0.32f, 5, f, 1f)
            drawCircle(Qn.Gold, w * 0.045f, Offset(w * 0.45f, h * 0.32f), alpha = f * seg(p, 0.3f, 0.45f), style = Stroke(1.5f * density))
            tagAt(a.tag("mhsahih1", 0, 0), Offset(w * 0.45f, h * 0.5f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil114(f)
            for (k in 0 until 4) {
                val y = h * (0.22f + 0.12f * k)
                tagAt(a.tag("mhsahih2", 0, k), Offset(w * 0.5f, y), f * seg(p, 0.1f + 0.15f * k, 0.25f + 0.15f * k), back = if (k % 2 == 0) Good114 else Color(0xB3101426), ink = if (k % 2 == 0) ArtInk.Jade else ArtInk.Ivory)
            }
        },
        { p, f ->
            veil114(f)
            drawLine(Color(0xFF8A8494), Offset(w * 0.75f, h * 0.38f), Offset(w * 0.5f, h * 0.5f), 2f * density, StrokeCap.Round, alpha = f * seg(p, 0.05f, 0.2f))
            drawLine(Color(0xFF8A8494), Offset(w * 0.25f, h * 0.38f), Offset(w * 0.5f, h * 0.5f), 2f * density, StrokeCap.Round, alpha = f * seg(p, 0.3f, 0.45f))
            softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.1f, Qn.Jade, 0.4f * f * seg(p, 0.55f, 0.75f))
            tagAt(a.tag("mhsahih3", 0, 0), Offset(w * 0.75f, h * 0.3f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("mhsahih3", 0, 1), Offset(w * 0.25f, h * 0.3f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("mhsahih3", 0, 2), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.6f, 0.75f), back = Good114, ink = ArtInk.Jade)
        },
    )
}

// ═════════════ mh-mursal ═════════════
internal fun DrawScope.mhmursalScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mhmursal", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { desk114(a) },
        { p, f ->
            veil114(f)
            isnad114(a, h * 0.4f, 5, f, 1f, broken = if (p > 0.35f) 0 else -1)
            drawCircle(Qn.Ember, w * 0.04f, Offset(w * 0.76f, h * 0.4f), alpha = f * seg(p, 0.35f, 0.5f), style = Stroke(1.5f * density))
            tagAt(a.tag("mhmursal0", 0, 0), Offset(w * 0.6f, h * 0.58f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("mhmursal0", 0, 1), Offset(w * 0.76f, h * 0.24f), f * seg(p, 0.4f, 0.55f), back = Bad114, ink = ArtInk.Ivory)
            tagAt(a.tag("mhmursal0", 0, 2), Offset(w * 0.88f, h * 0.58f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil114(f)
            plain97(Offset(w * 0.5f, h * 0.3f), w * 0.08f, a, "mhmursal1", 0, 0f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.13f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("mhmursal1", 0, 0), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("mhmursal1", 0, 1), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.5f, 0.65f), back = Good114, ink = ArtInk.Jade)
        },
        { p, f ->
            veil114(f)
            isnad114(a, h * 0.4f, 5, f, 1f, broken = 2)
            drawCircle(Color(0xFF6A6474), w * 0.032f, Offset(w * 0.45f, h * 0.4f), alpha = 0.6f * f * seg(p, 0.3f, 0.45f), style = Stroke(1.5f * density, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(6f, 6f))))
            tagAt(a.tag("mhmursal2", 0, 0), Offset(w * 0.7f, h * 0.6f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("mhmursal2", 0, 1), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.5f, 0.65f), back = Bad114, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil114(f)
            for (k in 0 until 2) drawCircle(Roundel.PlainFill, w * 0.05f, Offset(w * (0.62f - 0.24f * k), h * 0.38f), alpha = f * seg(p, 0.05f, 0.2f))
            drawLine(Color(0xFF8A8494), Offset(w * 0.56f, h * 0.38f), Offset(w * 0.44f, h * 0.38f), 2f * density, alpha = f * seg(p, 0.3f, 0.45f), pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(8f, 8f)))
            tagAt(a.tag("mhmursal3", 0, 0), Offset(w * 0.7f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("mhmursal3", 0, 1), Offset(w * 0.3f, h * 0.58f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ mh-mawdu ═════════════
internal fun DrawScope.mhmawduScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mhmawdu", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { desk114(a) },
        { p, f ->
            veil114(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.36f), w * 0.12f, Color(0xFF6A5A4A), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.36f), w * 0.12f, Qn.Ember, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("mhmawdu0", 0, 0), Offset(w * 0.72f, h * 0.6f), f * seg(p, 0.15f, 0.3f), back = Bad114, ink = ArtInk.Ivory)
            tagAt(a.tag("mhmawdu0", 0, 1), Offset(w * 0.28f, h * 0.6f), f * seg(p, 0.5f, 0.65f), back = Bad114, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil114(f)
            row114(a, "mhmawdu1", 3, h * 0.34f, p, f, step = 0.12f)
            row114(a, "mhmawdu1", 2, h * 0.5f, p, f, from = 0.46f, step = 0.12f, first = 3)
        },
        { p, f ->
            veil114(f)
            qIcon("lock", Offset(w * 0.5f, h * 0.36f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("mhmawdu2", 0, 0), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.15f, 0.3f), back = Bad114, ink = ArtInk.Ivory)
            tagAt(a.tag("mhmawdu2", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFFF2E2B8), Offset(w * 0.5f, 0f), Size(w * 0.5f, h), alpha = 0.25f * f)
            drawRect(Color(0xFF05070E), Offset(0f, 0f), Size(w * 0.5f, h), alpha = 0.55f * f)
            qIcon("sun", Offset(w * 0.75f, h * 0.36f), w * 0.09f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("moon", Offset(w * 0.25f, h * 0.36f), w * 0.08f, Color(0xFF6A6474), f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("mhmawdu3", 0, 0), Offset(w * 0.75f, h * 0.58f), f * seg(p, 0.15f, 0.3f), back = Good114, ink = ArtInk.Jade)
            tagAt(a.tag("mhmawdu3", 0, 1), Offset(w * 0.25f, h * 0.58f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ mh-sahabi ═════════════
internal fun DrawScope.mhsahabiScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mhsahabi", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { desk114(a) },
        { p, f ->
            veil114(f)
            sirajMunir(Offset(w * 0.5f, h * 0.26f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.1f))
            row114(a, "mhsahabi0", 3, h * 0.52f, p, f, step = 0.2f, back = Good114, ink = ArtInk.Jade)
        },
        { p, f ->
            veil114(f)
            for (k in 0 until 2) {
                val c = Offset(w * (0.7f - 0.4f * k), h * 0.38f)
                drawCircle(Roundel.PlainFill, w * 0.05f, c, alpha = f * seg(p, 0.1f + 0.35f * k, 0.25f + 0.35f * k))
                qIcon("cross", c, w * 0.06f, Qn.Ember, f * seg(p, 0.25f + 0.35f * k, 0.35f + 0.35f * k), a.t)
                tagAt(a.tag("mhsahabi1", 0, k), c + Offset(0f, h * 0.14f), f * seg(p, 0.15f + 0.35f * k, 0.3f + 0.35f * k), back = Bad114, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil114(f)
            row114(a, "mhsahabi2", 2, h * 0.36f, p, f, step = 0.18f)
            row114(a, "mhsahabi2", 2, h * 0.52f, p, f, from = 0.46f, step = 0.18f, first = 2)
        },
        { p, f ->
            veil114(f)
            for (k in 0 until 9) drawCircle(Roundel.PlainFill, w * 0.03f, Offset(w * (0.18f + 0.08f * k), h * 0.38f), alpha = f * seg(p, 0.05f + 0.03f * k, 0.15f + 0.03f * k))
            softGlow(Offset(w * 0.5f, h * 0.38f), w * 0.4f, Qn.Jade, 0.25f * f * seg(p, 0.4f, 0.7f))
            tagAt(a.tag("mhsahabi3", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.4f, 0.55f), back = Good114, ink = ArtInk.Jade)
        },
    )
}
