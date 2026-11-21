package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 144 (2026-11-21): al-Itqan, third part: the seven ahruf, the names of the Quran, its collection under Abu
 * Bakr and 'Uthman, the order of its verses and their count. Narrators are dots that light one by one, a sheet of
 * the first collection a pale leaf, a copy a small book. Abu Bakr, 'Umar and 'Uthman are companion roundels; Zayd and
 * Hudhayfa plain roundels; Hafsa only named.
 */

private fun DrawScope.dots144(n: Int, cols: Int, y0: Float, p: Float, f: Float, color: Color) {
    val w = size.width
    val rows = (n + cols - 1) / cols
    for (k in 0 until n) {
        val c = k % cols
        val r = k / cols
        val x = w * (0.5f + ((cols - 1) / 2f - c) * 0.08f)
        val y = y0 + size.height * 0.07f * (r - (rows - 1) / 2f)
        drawCircle(color, w * 0.016f, Offset(x, y), alpha = f * seg(p, 0.05f + 0.6f * k / n, 0.15f + 0.6f * k / n))
    }
}

private fun DrawScope.leaf144(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Color(0xFFE9DFC4), Offset(c.x - s * 0.6f, c.y - s * 0.8f), Size(s * 1.2f, s * 1.6f), CornerRadius(s * 0.1f), alpha = alpha)
    for (k in 0 until 4) drawLine(Color(0xFF7A6A50), Offset(c.x - s * 0.4f, c.y - s * 0.5f + s * 0.3f * k), Offset(c.x + s * 0.4f, c.y - s * 0.5f + s * 0.3f * k), s * 0.05f, alpha = alpha * 0.7f)
}

// ═════════════ iq-ahruf ═════════════
internal fun DrawScope.iqahrufScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqahruf", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 7) softGlow(Offset(w * (0.2f + 0.1f * k), h * 0.4f), w * 0.04f, Qn.GoldLight, 0.8f * f * seg(p, 0.1f + 0.06f * k, 0.2f + 0.06f * k))
            qIcon("book", Offset(w * 0.5f, h * 0.24f), w * 0.08f, Qn.Gold, f * seg(p, 0.02f, 0.15f), a.t)
            tagAt(a.tag("iqahruf0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            dots144(21, 7, h * 0.36f, p, f, Qn.GoldLight)
            row131(a, "iqahruf1", 2, h * 0.64f, p, f, from = 0.6f, step = 0.2f)
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.68f, h * 0.34f), w * 0.09f, a, "iqahruf2", 0, 2, f * seg(p, 0.02f, 0.15f))
            qIcon("book", Offset(w * 0.32f, h * 0.36f), w * 0.09f, Qn.Gold, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("iqahruf2", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            dots144(27, 9, h * 0.36f, p, f, Qn.GoldLight)
            tagAt(a.tag("iqahruf3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ iq-asma ═════════════
internal fun DrawScope.iqasmaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqasma", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 55) {
                val ang = k / 55f * 6.2832f
                drawCircle(Qn.GoldLight, w * 0.008f, Offset(w * 0.5f + w * 0.24f * kotlin.math.cos(ang), h * 0.38f + h * 0.16f * kotlin.math.sin(ang)), alpha = f * seg(p, 0.1f + 0.01f * k, 0.15f + 0.01f * k))
            }
            qIcon("book", Offset(w * 0.5f, h * 0.38f), w * 0.12f, Qn.Gold, f * seg(p, 0.02f, 0.15f), a.t)
            tagAt(a.tag("iqasma0", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (i in 0 until 4) {
                val x = if (i % 2 == 0) w * 0.72f else w * 0.28f
                tagAt(a.tag("iqasma1", 0, i), Offset(x, h * 0.36f + h * 0.2f * (i / 2)), f * seg(p, 0.08f + 0.2f * i, 0.2f + 0.2f * i), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.72f, h * 0.38f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            for (k in 0 until 4) drawLine(Fig131, Offset(w * 0.18f, h * (0.3f + 0.05f * k)), Offset(w * 0.38f, h * (0.3f + 0.05f * k)), w * 0.006f, alpha = f * seg(p, 0.4f + 0.05f * k, 0.5f + 0.05f * k))
            row131(a, "iqasma2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.5f, h * 0.36f), w * 0.11f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            row131(a, "iqasma3", 2, h * 0.62f, p, f, from = 0.15f, step = 0.4f, ink = ArtInk.Gold)
        },
    )
}

// ═════════════ iq-jam1 ═════════════
internal fun DrawScope.iqjam1Scene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqjam1", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.68f, h * 0.34f), w * 0.09f, a, "iqjam10", 0, 0, f * seg(p, 0.02f, 0.15f))
            close97(Offset(w * 0.32f, h * 0.34f), w * 0.09f, a, "iqjam10", 1, 1, f * seg(p, 0.2f, 0.32f))
            tagAt(a.tag("iqjam10", 0, 2), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.36f), w * 0.08f, a, "iqjam11", 0, f * seg(p, 0.05f, 0.18f))
            qIcon("pen", Offset(w * 0.32f, h * 0.36f), w * 0.07f, Fig131, f * seg(p, 0.5f, 0.62f), a.t)
            tagAt(a.tag("iqjam11", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            qIcon("mountain", Offset(w * 0.72f, h * 0.4f), w * 0.18f, Color(0xFF6A5A48), f * seg(p, 0.05f, 0.18f), a.t)
            for (k in 0 until 3) leaf144(Offset(w * (0.2f + 0.08f * k), h * (0.38f + 0.02f * k)), w * 0.04f, f * seg(p, 0.45f + 0.08f * k, 0.55f + 0.08f * k))
            row131(a, "iqjam12", 2, h * 0.64f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            box131(Offset(w * 0.5f, h * 0.42f), w * 0.1f, Color(0xFF6E5432), f * seg(p, 0.05f, 0.18f))
            for (k in 0 until 3) leaf144(Offset(w * (0.44f + 0.06f * k), h * mix(0.2f, 0.36f, seg(p, 0.2f + 0.1f * k, 0.4f + 0.1f * k))), w * 0.035f, f * seg(p, 0.2f + 0.1f * k, 0.3f + 0.1f * k))
            tagAt(a.tag("iqjam13", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ iq-jam2 ═════════════
internal fun DrawScope.iqjam2Scene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqjam2", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.34f), w * 0.08f, a, "iqjam20", 0, f * seg(p, 0.02f, 0.15f))
            for (k in 0 until 3) qIcon("lips", Offset(w * (0.2f + 0.1f * k), h * (0.34f + 0.05f * (k % 2))), w * 0.04f, Dim131, f * seg(p, 0.25f + 0.05f * k, 0.35f + 0.05f * k), a.t)
            tagAt(a.tag("iqjam20", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) leaf144(Offset(w * (0.74f + 0.04f * k), h * 0.38f), w * 0.035f, f * seg(p, 0.05f, 0.18f))
            for (k in 0 until 4) qIcon("book", Offset(w * (0.18f + 0.1f * k), h * 0.38f), w * 0.05f, Qn.Gold, f * seg(p, 0.4f + 0.07f * k, 0.5f + 0.07f * k), a.t)
            arrow131(Offset(w * 0.68f, h * 0.38f), Offset(w * 0.54f, h * 0.38f), seg(p, 0.25f, 0.4f), Qn.GoldLight, f)
            row131(a, "iqjam21", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("lips", Offset(w * 0.66f, h * 0.38f), w * 0.08f, Fig131, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("book", Offset(w * 0.34f, h * 0.38f), w * 0.1f, Qn.Gold, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("iqjam22", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 6) {
                val ang = k / 6f * 6.2832f
                val q = easeInOut(seg(p, 0.1f, 0.5f))
                qIcon("book", Offset(w * 0.5f + w * 0.3f * q * kotlin.math.cos(ang), h * 0.38f + h * 0.16f * q * kotlin.math.sin(ang)), w * 0.05f, Qn.Gold, f, a.t)
            }
            tagAt(a.tag("iqjam23", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ iq-tartib ═════════════
internal fun DrawScope.iqtartibScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqtartib", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 6) drawRoundRect(Qn.GoldLight, Offset(w * 0.32f, h * (0.24f + 0.05f * k)), Size(w * 0.36f, h * 0.03f), CornerRadius(w * 0.01f), alpha = f * seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k))
            qIcon("lock", Offset(w * 0.76f, h * 0.36f), w * 0.06f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            tagAt(a.tag("iqtartib0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 6) person99(Offset(w * (0.12f + 0.15f * k), h * 0.8f), h * 0.1f, Dim131, f)
            qIcon("book", Offset(w * 0.5f, h * 0.44f), w * 0.1f, Qn.Gold, f * seg(p, 0.1f, 0.22f), a.t)
            tagAt(a.tag("iqtartib1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 5) {
                val q = easeInOut(seg(p, 0.1f + 0.06f * k, 0.4f + 0.06f * k))
                leaf144(Offset(mix(w * (0.12f + 0.19f * k), w * 0.5f, q), mix(h * (0.25f + 0.1f * (k % 2)), h * 0.4f, q)), w * 0.035f, f)
            }
            tagAt(a.tag("iqtartib2", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 5) if (k != 2) drawCircle(Qn.GoldLight, w * 0.022f, Offset(w * (0.3f + 0.1f * k), h * 0.38f), alpha = f * seg(p, 0.05f + 0.05f * k, 0.15f + 0.05f * k))
            qIcon("cross", Offset(w * 0.5f, h * 0.38f), w * 0.05f, Qn.Rose, f * seg(p, 0.35f, 0.45f), a.t)
            row131(a, "iqtartib3", 2, h * 0.62f, p, f, from = 0.2f, step = 0.4f)
        },
    )
}

// ═════════════ iq-adad ═════════════
internal fun DrawScope.iqadadScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqadad", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 60) drawCircle(Qn.GoldLight, w * 0.007f, Offset(w * (0.14f + 0.072f * (k % 10)), h * (0.2f + 0.06f * (k / 10))), alpha = f * seg(p, 0.05f + 0.008f * k, 0.1f + 0.008f * k))
            tagAt(a.tag("iqadad0", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            row131(a, "iqadad1", 3, h * 0.44f, p, f, from = 0.1f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("iqadad2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            qIcon("ear", Offset(w * 0.64f, h * 0.48f), w * 0.08f, Fig131, f * seg(p, 0.3f, 0.42f), a.t)
            qIcon("book", Offset(w * 0.36f, h * 0.48f), w * 0.09f, Qn.Gold, f * seg(p, 0.45f, 0.58f), a.t)
        },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.72f, h * 0.38f), w * 0.09f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("cross", Offset(w * 0.28f, h * 0.38f), w * 0.06f, Dim131, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "iqadad3", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}
