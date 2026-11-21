package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 142 (2026-11-21): al-Itqan, second part: the first and last revelation, occasions of revelation, the general
 * wording, 'Umar's agreements, and how the Quran came down. The cave of Hira is a mountain with a dark mouth (no
 * figure and no angel drawn; Jibril only named); companions are roundels; a surah is a small book.
 */

private fun DrawScope.icons142(names: List<String>, y: Float, s: Float, colors: List<Color>, p: Float, f: Float, from: Float, step: Float, t: Float) {
    val n = names.size
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        qIcon(names[i], Offset(x, y), s, colors[i % colors.size], f * seg(p, from + step * i, from + step * i + 0.14f), t)
    }
}

private fun DrawScope.cave142(c: Offset, s: Float, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    qIcon("mountain", c, s, Color(0xFF6A5A48), alpha, t)
    drawOval(Color(0xFF0E0A1A), Offset(c.x - s * 0.12f, c.y + s * 0.02f), Size(s * 0.24f, s * 0.14f), alpha = alpha)
}

// ═════════════ iq-awwal ═════════════
internal fun DrawScope.iqawwalScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqawwal", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            tagAt(a.tag("iqawwal0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.46f), w * 0.12f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.5f))
            qIcon("book", Offset(w * 0.5f, h * 0.46f), w * 0.14f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
        },
        { p, f ->
            veil131(f)
            qIcon("moon", Offset(w * 0.72f, h * 0.36f), w * 0.09f, Fig131, f * seg(p, 0.1f, 0.22f), a.t)
            cave142(Offset(w * 0.28f, h * 0.4f), w * 0.22f, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "iqawwal1", 2, h * 0.64f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            cave142(Offset(w * 0.5f, h * 0.5f), w * 0.4f, f, a.t)
            row131(a, "iqawwal2", 2, h * 0.22f, p, f, from = 0.15f, step = 0.35f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.3f), w * 0.08f, a, "iqawwal3", 0, f * seg(p, 0.02f, 0.15f))
            for (k in 0 until 8) {
                val ang = k / 8f * 6.2832f
                drawCircle(Dim131, w * 0.02f, Offset(w * 0.5f + w * 0.16f * kotlin.math.cos(ang), h * 0.52f + h * 0.06f * kotlin.math.sin(ang)), alpha = f * seg(p, 0.15f + 0.03f * k, 0.25f + 0.03f * k))
            }
            tagAt(a.tag("iqawwal3", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ iq-akhir ═════════════
internal fun DrawScope.iqakhirScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqakhir", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            icons142(listOf("book", "book"), h * 0.36f, w * 0.09f, listOf(Qn.Gold, Qn.GoldLight), p, f, 0.1f, 0.45f, a.t)
            row131(a, "iqakhir0", 2, h * 0.58f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) coin131(Offset(w * (0.46f + 0.04f * k), h * (0.4f - 0.03f * k)), w * 0.03f, f * seg(p, 0.05f, 0.18f))
            qIcon("cross", Offset(w * 0.5f, h * 0.38f), w * 0.12f, Qn.Rose, 0.7f * f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("iqakhir1", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            icons142(listOf("book", "book"), h * 0.36f, w * 0.09f, listOf(Qn.GoldLight, Qn.Gold), p, f, 0.1f, 0.45f, a.t)
            row131(a, "iqakhir2", 2, h * 0.58f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 6) qIcon("book", Offset(w * (0.82f - 0.12f * k), h * 0.4f), w * 0.05f, if (k == 5) Qn.Gold else Dim131, f * seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k), a.t)
            tagAt(a.tag("iqakhir3", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ iq-sabab ═════════════
internal fun DrawScope.iqsababScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqsabab", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.68f, h * 0.38f), w * 0.08f, Dim131, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("key", Offset(w * 0.32f, h * 0.38f), w * 0.09f, Qn.Gold, f * seg(p, 0.45f, 0.58f), a.t)
            tagAt(a.tag("iqsabab0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            icons142(listOf("key", "eye"), h * 0.36f, w * 0.09f, listOf(Qn.Gold, Qn.GoldLight), p, f, 0.05f, 0.45f, a.t)
            row131(a, "iqsabab1", 2, h * 0.58f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            row131(a, "iqsabab2", 2, h * 0.42f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
            arrow131(Offset(w * 0.62f, h * 0.56f), Offset(w * 0.38f, h * 0.56f), seg(p, 0.35f, 0.55f), Qn.GoldLight, f)
        },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.68f, h * 0.4f), w * 0.08f, Fig131, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("book", Offset(w * 0.32f, h * 0.4f), w * 0.1f, Qn.Gold, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("iqsabab3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ iq-umum ═════════════
internal fun DrawScope.iqumumScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "iqumum", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            val r = w * 0.12f * easeInOut(seg(p, 0.1f, 0.35f))
            drawCircle(Qn.GoldLight, r, Offset(w * 0.72f, h * 0.38f), alpha = f, style = Stroke(w * 0.008f))
            qIcon("check", Offset(w * 0.72f, h * 0.38f), w * 0.05f, Qn.Jade, f * seg(p, 0.3f, 0.42f), a.t)
            drawCircle(Dim131, w * 0.02f, Offset(w * 0.28f, h * 0.38f), alpha = f * seg(p, 0.5f, 0.6f))
            row131(a, "iqumum0", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            for (k in 0 until 3) drawCircle(Qn.GoldLight, w * (0.05f + 0.12f * ((a.t * 0.3f + k / 3f) % 1f)), Offset(w * 0.5f, g - h * 0.12f), alpha = 0.5f * f * (1f - ((a.t * 0.3f + k / 3f) % 1f)), style = Stroke(w * 0.005f))
            for (k in 0 until 6) person99(Offset(w * (0.12f + 0.15f * k), g), h * 0.09f, Dim131, f * seg(p, 0.3f + 0.04f * k, 0.42f + 0.04f * k))
            row131(a, "iqumum1", 2, h * 0.28f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            drawCircle(Qn.Rose, w * 0.025f, Offset(w * 0.72f, h * 0.38f), alpha = f * seg(p, 0.05f, 0.18f))
            drawCircle(Qn.Rose, w * 0.12f, Offset(w * 0.28f, h * 0.38f), alpha = f * seg(p, 0.5f, 0.62f), style = Stroke(w * 0.008f))
            row131(a, "iqumum2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.36f), w * 0.08f, a, "iqumum3", 0, f * seg(p, 0.05f, 0.18f))
            plain97(Offset(w * 0.32f, h * 0.36f), w * 0.08f, a, "iqumum3", 1, f * seg(p, 0.4f, 0.52f))
            qIcon("scroll", Offset(w * 0.5f, h * 0.6f), w * 0.07f, Qn.Gold, f * seg(p, 0.6f, 0.72f), a.t)
        },
    )
}

// ═════════════ iq-umar ═════════════
internal fun DrawScope.iqumarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqumar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.68f, h * 0.36f), w * 0.09f, a, "iqumar0", 0, 1, f * seg(p, 0.02f, 0.15f))
            softGlow(Offset(w * 0.32f, h * 0.36f), w * 0.09f, Qn.GoldLight, 0.6f * f * seg(p, 0.45f, 0.65f))
            qIcon("book", Offset(w * 0.32f, h * 0.36f), w * 0.1f, Qn.Gold, f * seg(p, 0.45f, 0.58f), a.t)
            tagAt(a.tag("iqumar0", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            icons142(listOf("kaaba", "house", "heart"), h * 0.34f, w * 0.08f, listOf(Qn.Gold, Fig131, Qn.Rose), p, f, 0.15f, 0.25f, a.t)
            row131(a, "iqumar1", 3, h * 0.56f, p, f, from = 0.15f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            qIcon("lips", Offset(w * 0.68f, h * 0.38f), w * 0.08f, Fig131, f * seg(p, 0.05f, 0.18f), a.t)
            arrow131(Offset(w * 0.58f, h * 0.38f), Offset(w * 0.42f, h * 0.38f), seg(p, 0.25f, 0.45f), Qn.GoldLight, f)
            qIcon("book", Offset(w * 0.32f, h * 0.38f), w * 0.09f, Qn.Gold, f * seg(p, 0.45f, 0.58f), a.t)
            row131(a, "iqumar2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            for (k in 0 until 5) person99(Offset(w * (0.14f + 0.18f * k), h * 0.8f), h * 0.1f, Dim131, f)
            qIcon("book", Offset(w * 0.5f, h * 0.44f), w * 0.09f, Qn.Gold, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("iqumar3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ iq-inzal ═════════════
internal fun DrawScope.iqinzalScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqinzal", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            val d = easeInOut(seg(p, 0.05f, 0.35f))
            qIcon("book", Offset(w * 0.72f, mix(h * 0.08f, h * 0.36f, d)), w * 0.12f, Qn.Gold, f, a.t)
            for (k in 0 until 8) {
                val q = seg(p, 0.45f + 0.05f * k, 0.6f + 0.05f * k)
                drawRoundRect(Qn.GoldLight, Offset(w * (0.18f + 0.03f * (k % 4)), mix(h * 0.1f, h * (0.3f + 0.04f * (k / 4)), q)), Size(w * 0.03f, h * 0.02f), alpha = f * q)
            }
            row131(a, "iqinzal0", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            for (k in 0 until 20) drawCircle(Color.White, w * 0.004f, Offset(w * ((k * 0.173f) % 1f), h * ((k * 0.111f) % 0.5f)), alpha = 0.6f * f)
            softGlow(Offset(w * 0.68f, h * 0.24f), w * 0.1f, Qn.GoldLight, 0.7f * f * seg(p, 0.05f, 0.25f))
            qIcon("house", Offset(w * 0.68f, h * 0.24f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            road142(Offset(w * 0.6f, h * 0.3f), Offset(w * 0.3f, h * 0.62f), seg(p, 0.5f, 0.75f), f)
            row131(a, "iqinzal1", 2, h * 0.72f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("iqinzal2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
            voice130(Offset(w * 0.7f, h * 0.48f), 0.5f, f * seg(p, 0.05f, 0.2f), a.t)
            arrow131(Offset(w * 0.6f, h * 0.48f), Offset(w * 0.4f, h * 0.48f), seg(p, 0.3f, 0.5f), Qn.GoldLight, f)
            qIcon("book", Offset(w * 0.3f, h * 0.48f), w * 0.08f, Qn.Gold, f * seg(p, 0.45f, 0.58f), a.t)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.4f), w * 0.12f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f, 0.45f))
            qIcon("heart", Offset(w * 0.5f, h * 0.4f), w * 0.12f, Qn.Rose, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("iqinzal3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

private fun DrawScope.road142(from: Offset, to: Offset, q: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 12) {
        val d = k / 11f
        if (d > q) break
        drawCircle(Qn.GoldLight, size.width * 0.006f, Offset(mix(from.x, to.x, d), mix(from.y, to.y, d)), alpha = alpha)
    }
}
