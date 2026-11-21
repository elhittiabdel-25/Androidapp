package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 136 (2026-11-21): ila', zihar, li'an, the waiting period, suckling and maintenance (al-Sharh al-Mumti',
 * abridged). Months are moons, the waiting period an hourglass, suckling a cup, maintenance sacks and a house.
 * Husband and wife stay faceless and apart; Subay'a al-Aslamiyya only named in a plain roundel.
 */

private fun DrawScope.dot136(c: Offset, r: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(color, r, c, alpha = alpha)
    drawCircle(Qn.GoldLight, r, c, alpha = alpha, style = Stroke(r * 0.15f))
}

private fun DrawScope.bar136(x: Float, y: Float, len: Float, q: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val h = size.height * 0.03f
    drawRoundRect(Dim131, Offset(x - len / 2f, y - h / 2f), Size(len, h), CornerRadius(h / 2f), alpha = alpha * 0.5f)
    drawRoundRect(color, Offset(x - len / 2f, y - h / 2f), Size(len * q, h), CornerRadius(h / 2f), alpha = alpha)
}

private fun DrawScope.icons136(names: List<String>, y: Float, s: Float, colors: List<Color>, p: Float, f: Float, from: Float, step: Float, t: Float) {
    val n = names.size
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        qIcon(names[i], Offset(x, y), s, colors[i % colors.size], f * seg(p, from + step * i, from + step * i + 0.14f), t)
    }
}

// ═════════════ tl-ila ═════════════
internal fun DrawScope.tlilaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "tlila", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.78f, g), h * 0.14f, Fig131, f)
            woman12(Offset(w * 0.2f, g), h * 0.13f, Dim131, f)
            voice130(Offset(w * 0.78f, g - h * 0.2f), 0.5f, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("tlila0", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 4) qIcon("moon", Offset(w * (0.71f - 0.14f * k), h * 0.28f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k), a.t)
            row131(a, "tlila1", 3, h * 0.5f, p, f, from = 0.1f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) qIcon("moon", Offset(w * (0.62f - 0.12f * k), h * 0.32f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.05f + 0.08f * k, 0.18f + 0.08f * k), a.t)
            qIcon("hourglass", Offset(w * 0.3f, h * 0.32f), w * 0.07f, Fig131, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("tlila2", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("tlila3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            qIcon("scroll", Offset(w * 0.5f, h * 0.48f), w * 0.09f, Qn.Gold, f * seg(p, 0.25f, 0.4f), a.t)
        },
    )
}

// ═════════════ tl-zihar ═════════════
internal fun DrawScope.tlziharScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tlzihar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("lips", Offset(w * 0.5f, h * 0.38f), w * 0.12f, Fig131, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.38f), w * 0.16f, Qn.Rose, 0.85f * f * seg(p, 0.5f, 0.62f), a.t)
            tagAt(a.tag("tlzihar0", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Qn.Rose, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("check", Offset(w * 0.64f, h * 0.28f), w * 0.05f, Qn.Jade, f * seg(p, 0.55f, 0.68f), a.t)
            tagAt(a.tag("tlzihar1", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            icons136(listOf("chain", "moon", "cup"), h * 0.34f, w * 0.08f, listOf(Qn.GoldLight), p, f, 0.08f, 0.28f, a.t)
            for (i in 0 until 2) arrow131(Offset(w * (0.66f - 0.28f * i), h * 0.34f), Offset(w * (0.56f - 0.28f * i), h * 0.34f), seg(p, 0.25f + 0.28f * i, 0.33f + 0.28f * i), Dim131, f)
            row131(a, "tlzihar2", 3, h * 0.56f, p, f, from = 0.1f, step = 0.28f)
        },
        { p, f ->
            for (k in 0 until 4) woman12(Offset(w * (0.2f + 0.2f * k), h * 0.8f), h * 0.11f, Dim131, f * seg(p, 0.05f + 0.05f * k, 0.2f + 0.05f * k))
            dot136(Offset(w * 0.5f, h * 0.4f), w * 0.05f, Qn.Lapis, f * seg(p, 0.45f, 0.58f))
            tagAt(a.tag("tlzihar3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tl-lian ═════════════
internal fun DrawScope.tllianScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "tllian", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.8f, g), h * 0.14f, Fig131, f)
            woman12(Offset(w * 0.2f, g), h * 0.13f, Fig131, f)
            qIcon("scale", Offset(w * 0.5f, h * 0.5f), w * 0.12f, Qn.Gold, f * seg(p, 0.02f, 0.15f), a.t)
            row131(a, "tllian0", 2, h * 0.26f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            person99(Offset(w * 0.82f, g), h * 0.14f, Fig131, f)
            for (k in 0 until 4) {
                val x = w * (0.18f + 0.14f * k)
                person99(Offset(x, g), h * 0.11f, Dim131, f * seg(p, 0.3f + 0.06f * k, 0.42f + 0.06f * k))
                qIcon("eye", Offset(x, g - h * 0.2f), w * 0.035f, Qn.GoldLight, f * seg(p, 0.45f + 0.06f * k, 0.55f + 0.06f * k), a.t)
            }
            tagAt(a.tag("tllian1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("bed", Offset(w * 0.5f, h * 0.4f), w * 0.14f, Dim131, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("scale", Offset(w * 0.5f, h * 0.4f), w * 0.14f, Qn.Gold, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("tllian2", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig131, f)
            voice130(Offset(w * 0.72f, g - h * 0.2f), 0.5f, f * seg(p, 0.1f, 0.25f), a.t)
            person99(Offset(w * 0.28f, g), h * 0.15f, Dim131, f * seg(p, 0.45f, 0.58f))
            qIcon("scale", Offset(w * 0.28f, g - h * 0.24f), w * 0.06f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "tllian3", 2, h * 0.26f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ tl-idda ═════════════
internal fun DrawScope.tliddaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tlidda", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("hourglass", Offset(w * 0.5f, h * 0.38f), w * 0.14f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tlidda0", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            icons136(listOf("check", "point", "scale"), h * 0.34f, w * 0.08f, listOf(Qn.Jade, Qn.GoldLight, Qn.Gold), p, f, 0.15f, 0.25f, a.t)
            row131(a, "tlidda1", 3, h * 0.56f, p, f, from = 0.15f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 6) dot136(Offset(w * (0.22f + 0.112f * k), h * 0.4f), w * 0.035f, Qn.Lapis, f * seg(p, 0.15f + 0.06f * k, 0.25f + 0.06f * k))
            tagAt(a.tag("tlidda2", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
        },
        { p, f ->
            woman12(Offset(w * 0.62f, h * 0.8f), h * 0.13f, Fig131, f)
            qIcon("hourglass", Offset(w * 0.3f, h * 0.5f), w * 0.08f, Dim131, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("cross", Offset(w * 0.3f, h * 0.5f), w * 0.1f, Qn.Rose, f * seg(p, 0.4f, 0.52f), a.t)
            tagAt(a.tag("tlidda3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tl-hamil ═════════════
internal fun DrawScope.tlhamilScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tlhamil", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) dot136(Offset(w * (0.26f + 0.24f * k), h * 0.56f), w * 0.03f, Dim131, f * seg(p, 0.05f, 0.15f) * (1f - 0.7f * seg(p, 0.45f, 0.6f)))
            softGlow(Offset(w * 0.5f, h * 0.36f), w * 0.1f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f, 0.4f))
            dot136(Offset(w * 0.5f, h * 0.36f), w * 0.07f, Qn.Gold, f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("tlhamil0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.34f), w * 0.1f, a, "tlhamil1", 0, f * seg(p, 0.02f, 0.15f))
            child12(Offset(w * 0.3f, h * 0.7f), h * 0.07f, Fig131, f * seg(p, 0.3f, 0.42f))
            tagAt(a.tag("tlhamil1", 0, 1), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.65f, 0.78f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            bar136(w * 0.72f, h * 0.4f, w * 0.18f, seg(p, 0.1f, 0.3f), Qn.Jade, f)
            bar136(w * 0.28f, h * 0.4f, w * 0.38f, seg(p, 0.5f, 0.75f), Qn.Jade, f)
            row131(a, "tlhamil2", 2, h * 0.58f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            child12(Offset(w * 0.6f, h * 0.8f), h * 0.08f, Fig131, f * seg(p, 0.05f, 0.18f))
            child12(Offset(w * 0.4f, h * 0.8f), h * 0.08f, Fig131, f * seg(p, 0.55f, 0.68f))
            qIcon("hourglass", Offset(w * 0.5f, h * 0.5f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            row131(a, "tlhamil3", 2, h * 0.26f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ tl-rada ═════════════
internal fun DrawScope.tlradaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tlrada", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("cup", Offset(w * 0.72f, h * 0.38f), w * 0.1f, Fig131, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("tree", Offset(w * 0.28f, h * 0.38f), w * 0.12f, Qn.Jade, f * seg(p, 0.45f, 0.6f), a.t)
            for (k in 0 until 2) drawLine(Qn.GoldLight, Offset(w * 0.46f, h * (0.37f + 0.03f * k)), Offset(w * 0.54f, h * (0.37f + 0.03f * k)), w * 0.008f, alpha = f * seg(p, 0.6f, 0.7f))
            row131(a, "tlrada0", 2, h * 0.62f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            row131(a, "tlrada1", 3, h * 0.45f, p, f, from = 0.12f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 5) qIcon("cup", Offset(w * (0.8f - 0.15f * k), h * 0.4f), w * 0.07f, Fig131, f * seg(p, 0.1f + 0.1f * k, 0.2f + 0.1f * k), a.t)
            tagAt(a.tag("tlrada2", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            child12(Offset(w * 0.5f, h * 0.8f), h * 0.09f, Fig131, f)
            qIcon("cup", Offset(w * 0.68f, h * 0.5f), w * 0.07f, Fig131, f * (1f - 0.6f * seg(p, 0.4f, 0.55f)), a.t)
            qIcon("wheat", Offset(w * 0.32f, h * 0.5f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("tlrada3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tl-nafaqa ═════════════
internal fun DrawScope.tlnafaqaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "tlnafaqa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            icons136(listOf("heart", "tree", "key"), h * 0.34f, w * 0.08f, listOf(Qn.Rose, Qn.Jade, Qn.Gold), p, f, 0.1f, 0.25f, a.t)
            row131(a, "tlnafaqa0", 3, h * 0.56f, p, f, from = 0.1f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            icons136(listOf("wheat", "crown", "house"), h * 0.34f, w * 0.09f, listOf(Qn.GoldLight, Qn.Lapis, Fig131), p, f, 0.1f, 0.25f, a.t)
            row131(a, "tlnafaqa1", 3, h * 0.56f, p, f, from = 0.1f, step = 0.25f)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig131, f)
            sack131(Offset(w * 0.84f, g - h * 0.05f), w * 0.08f, Sack131, f * seg(p, 0.1f, 0.25f))
            person99(Offset(w * 0.28f, g), h * 0.14f, Fig131, f)
            sack131(Offset(w * 0.38f, g - h * 0.03f), w * 0.04f, Sack131, f * seg(p, 0.5f, 0.62f))
            row131(a, "tlnafaqa2", 2, h * 0.28f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.78f, g), h * 0.14f, Fig131, f)
            woman12(Offset(w * 0.22f, g), h * 0.13f, Fig131, f)
            sack131(Offset(mix(w * 0.66f, w * 0.4f, easeInOut(seg(p, 0.2f, 0.5f))), g - h * 0.05f), w * 0.05f, Sack131, f)
            qIcon("check", Offset(w * 0.5f, h * 0.44f), w * 0.05f, Qn.Jade, f * seg(p, 0.55f, 0.68f), a.t)
            tagAt(a.tag("tlnafaqa3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}
