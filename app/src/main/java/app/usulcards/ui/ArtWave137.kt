package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 137 (2026-11-21): endowment and bequest (al-Sharh al-Mumti', abridged). The endowment is a tree whose trunk is
 * locked while its fruit goes out; shares are discs; the estate a bar that is paid out right to left. 'Umar and Abu
 * Bakr are companion roundels, Abu Talha a plain roundel; the old donkey is only named.
 */

private fun DrawScope.grid137(a: ArtCtx, kind: String, p: Float, f: Float, y0: Float, dy: Float) {
    for (i in 0 until 4) {
        val x = if (i % 2 == 0) size.width * 0.72f else size.width * 0.28f
        tagAt(a.tag(kind, 0, i), Offset(x, y0 + dy * (i / 2)), f * seg(p, 0.08f + 0.2f * i, 0.2f + 0.2f * i), ink = ArtInk.Ivory)
    }
}

private fun DrawScope.fruit137(tree: Offset, s: Float, q: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 4) {
        val d = ((q + k * 0.25f) % 1f)
        drawCircle(Qn.Rose, s * 0.06f, Offset(tree.x - s * 0.2f - s * 0.8f * d, tree.y - s * 0.2f + s * 0.6f * d), alpha = alpha * (1f - d))
    }
}

private fun DrawScope.estate137(y: Float, parts: List<Pair<Float, Color>>, p: Float, f: Float, from: Float, step: Float) {
    val w = size.width
    val x0 = w * 0.9f
    val len = w * 0.8f
    val h = size.height * 0.05f
    drawRoundRect(Dim131, Offset(w * 0.1f, y - h / 2f), Size(len, h), CornerRadius(h / 3f), alpha = 0.5f * f)
    var x = x0
    for ((i, part) in parts.withIndex()) {
        val q = seg(p, from + step * i, from + step * i + 0.12f)
        val wd = len * part.first
        drawRoundRect(part.second, Offset(x - wd * q, y - h / 2f), Size(wd * q, h), CornerRadius(h / 3f), alpha = f)
        x -= wd
    }
}

// ═════════════ wq-waqf ═════════════
internal fun DrawScope.wqwaqfScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wqwaqf", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            val tree = Offset(w * 0.72f, g - h * 0.16f)
            qIcon("tree", tree, w * 0.22f, Qn.Jade, f, a.t)
            qIcon("lock", Offset(w * 0.72f, g - h * 0.04f), w * 0.05f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            fruit137(tree, w * 0.3f, (a.t * 0.4f) % 1f, f * seg(p, 0.45f, 0.6f))
            person99(Offset(w * 0.28f, g), h * 0.12f, Fig131, f * seg(p, 0.5f, 0.62f))
            row131(a, "wqwaqf0", 2, h * 0.24f, p, f, from = 0.12f, step = 0.42f, ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 5) qIcon("tree", Offset(w * (0.5f + 0.09f * k), g - h * 0.06f), w * 0.08f, Qn.Jade, f, a.t)
            close97(Offset(w * 0.26f, h * 0.34f), w * 0.09f, a, "wqwaqf1", 0, 1, f * seg(p, 0.02f, 0.15f))
            qIcon("lock", Offset(w * 0.68f, h * 0.44f), w * 0.05f, Qn.Gold, f * seg(p, 0.6f, 0.72f), a.t)
            tagAt(a.tag("wqwaqf1", 0, 1), Offset(w * 0.68f, h * 0.3f), f * seg(p, 0.65f, 0.78f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.38f), w * 0.12f, Qn.GoldLight, 0.7f * f * seg(p, 0.1f, 0.3f))
            qIcon("tree", Offset(w * 0.5f, h * 0.38f), w * 0.14f, Qn.Jade, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("wqwaqf2", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 3) qIcon("tree", Offset(w * (0.62f + 0.1f * k), g - h * 0.08f), w * 0.1f, Qn.Jade, f, a.t)
            qIcon("drop", Offset(w * 0.72f, g + h * 0.02f), w * 0.04f, Qn.Lapis, f, a.t)
            plain97(Offset(w * 0.26f, h * 0.32f), w * 0.09f, a, "wqwaqf3", 0, f * seg(p, 0.02f, 0.15f))
            for (k in 0 until 3) person99(Offset(w * (0.14f + 0.1f * k), g), h * 0.1f, Dim131, f * seg(p, 0.6f, 0.72f))
            tagAt(a.tag("wqwaqf3", 0, 1), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.6f, 0.74f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ wq-fil ═════════════
internal fun DrawScope.wqfilScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wqfil", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig131, f)
            voice130(Offset(w * 0.72f, g - h * 0.2f), 0.5f, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("house", Offset(w * 0.3f, g - h * 0.1f), w * 0.2f, Fig131, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("wqfil0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.72f, g - h * 0.1f), w * 0.2f, Fig131, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("crescent", Offset(w * 0.72f, g - h * 0.28f), w * 0.05f, Qn.Gold, f * seg(p, 0.15f, 0.28f), a.t)
            drawRect(Dim131, Offset(w * 0.14f, g - h * 0.05f), Size(w * 0.3f, h * 0.012f), alpha = f * seg(p, 0.5f, 0.62f))
            for (k in 0 until 3) drawRoundRect(Dim131, Offset(w * (0.18f + 0.09f * k), g - h * 0.12f), Size(w * 0.03f, h * 0.06f), CornerRadius(w * 0.015f), alpha = f * seg(p, 0.55f + 0.04f * k, 0.65f + 0.04f * k))
            row131(a, "wqfil1", 2, h * 0.3f, p, f, from = 0.12f, step = 0.45f)
        },
        { p, f ->
            for (y in listOf(0.55f, 0.85f)) drawRect(Color(0xFF3A3448), Offset(0f, h * y - h * 0.03f), Size(w, h * 0.06f), alpha = f)
            drawRect(Color(0xFF4A3A2A), Offset(w * 0.3f, h * 0.58f), Size(w * 0.4f, h * 0.24f), alpha = f)
            for (k in 0 until 3) person99(Offset(w * mix(0.35f, 0.65f, (a.t * 0.2f + k * 0.33f) % 1f), h * 0.74f), h * 0.08f, Dim131, f, walk = a.t)
            person99(Offset(w * 0.86f, h * 0.5f), h * 0.12f, Fig131, f)
            qIcon("cross", Offset(w * 0.5f, h * 0.36f), w * 0.06f, Qn.Rose, f * seg(p, 0.5f, 0.62f), a.t)
            tagAt(a.tag("wqfil2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 3) qIcon("tree", Offset(w * (0.62f + 0.12f * k), g - h * 0.08f), w * 0.1f, Qn.Jade, f, a.t)
            qIcon("mat", Offset(w * 0.32f, g - h * 0.02f), w * 0.12f, Qn.Lapis, f * seg(p, 0.1f, 0.25f), a.t)
            for (k in 0 until 2) person99(Offset(w * (0.24f + 0.14f * k), g), h * 0.1f, Dim131, f * seg(p, 0.25f, 0.4f))
            tagAt(a.tag("wqfil3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ wq-shurut ═════════════
internal fun DrawScope.wqshurutScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "wqshurut", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            grid137(a, "wqshurut0", p, f, h * 0.34f, h * 0.22f)
        },
        { p, f ->
            veil131(f)
            qIcon("cup", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Dim131, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.38f), w * 0.13f, Qn.Rose, 0.85f * f * seg(p, 0.55f, 0.68f), a.t)
            row131(a, "wqshurut1", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.68f, h * 0.38f), w * 0.16f, Fig131, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("hourglass", Offset(w * 0.34f, h * 0.38f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            qIcon("cross", Offset(w * 0.68f, h * 0.38f), w * 0.12f, Qn.Rose, 0.8f * f * seg(p, 0.65f, 0.78f), a.t)
            row131(a, "wqshurut2", 2, h * 0.62f, p, f, from = 0.15f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.38f), w * 0.16f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            row131(a, "wqshurut3", 2, h * 0.62f, p, f, from = 0.12f, step = 0.45f)
        },
    )
}

// ═════════════ wq-shart ═════════════
internal fun DrawScope.wqshartScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wqshart", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            qIcon("scroll", Offset(w * 0.7f, h * 0.42f), w * 0.09f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            person99(Offset(w * 0.32f, g), h * 0.14f, Fig131, f)
            arrow131(Offset(w * 0.62f, h * 0.48f), Offset(w * 0.4f, h * 0.6f), seg(p, 0.3f, 0.5f), Qn.GoldLight, f)
            tagAt(a.tag("wqshart0", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 6) {
                val x = w * (0.85f - 0.14f * k)
                if (k < 3) person99(Offset(x, g), h * 0.11f, Fig131, f * seg(p, 0.05f + 0.04f * k, 0.15f + 0.04f * k))
                else child12(Offset(x, g), h * 0.08f, Fig131, f * seg(p, 0.05f + 0.04f * k, 0.15f + 0.04f * k))
                coin131(Offset(x, g - h * 0.2f), w * 0.018f, f * seg(p, 0.45f + 0.04f * k, 0.55f + 0.04f * k))
            }
            tagAt(a.tag("wqshart1", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.68f, h * 0.48f), w * 0.07f, Dim131, f * seg(p, 0.05f, 0.18f), a.t)
            arrow131(Offset(w * 0.62f, h * 0.44f), Offset(w * 0.4f, h * 0.32f), seg(p, 0.25f, 0.45f), Qn.GoldLight, f)
            softGlow(Offset(w * 0.34f, h * 0.3f), w * 0.07f, Qn.GoldLight, 0.7f * f * seg(p, 0.45f, 0.6f))
            qIcon("scroll", Offset(w * 0.34f, h * 0.3f), w * 0.07f, Qn.Gold, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("wqshart2", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.7f, h * 0.4f), w * 0.14f, Dim131, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("kaaba", Offset(w * 0.3f, h * 0.4f), w * 0.12f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "wqshart3", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ ws-hukm ═════════════
internal fun DrawScope.wshukmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wshukm", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) coin131(Offset(w * (0.68f + 0.04f * k), h * (0.4f - 0.03f * k)), w * 0.03f, f * seg(p, 0.1f, 0.25f))
            qIcon("scroll", Offset(w * 0.3f, h * 0.38f), w * 0.08f, Fig131, f * seg(p, 0.5f, 0.62f), a.t)
            qIcon("pen", Offset(w * 0.36f, h * 0.34f), w * 0.05f, Qn.Gold, f * seg(p, 0.55f, 0.68f), a.t)
            row131(a, "wshukm0", 2, h * 0.62f, p, f, from = 0.12f, step = 0.42f)
        },
        { p, f ->
            for (k in 0 until 3) coin131(Offset(w * (0.74f + 0.04f * k), g - h * (0.04f + 0.03f * k)), w * 0.025f, f * seg(p, 0.1f, 0.25f))
            person99(Offset(w * 0.36f, g), h * 0.14f, Fig131, f * seg(p, 0.45f, 0.58f))
            child12(Offset(w * 0.24f, g), h * 0.07f, Fig131, f * seg(p, 0.5f, 0.62f))
            child12(Offset(w * 0.16f, g), h * 0.06f, Fig131, f * seg(p, 0.55f, 0.66f))
            row131(a, "wshukm1", 2, h * 0.28f, p, f, from = 0.12f, step = 0.42f)
        },
        { p, f ->
            veil131(f)
            val fr = listOf(1f / 3f, 0.25f, 0.2f)
            for (i in 0 until 3) pie131(Offset(w * (0.78f - 0.28f * i), h * 0.38f), w * 0.07f, fr[i], f * seg(p, 0.1f + 0.25f * i, 0.22f + 0.25f * i))
            softGlow(Offset(w * 0.22f, h * 0.38f), w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.7f, 0.85f))
            row131(a, "wshukm2", 3, h * 0.6f, p, f, from = 0.12f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.68f, h * 0.36f), w * 0.09f, a, "wshukm3", 0, 0, f * seg(p, 0.02f, 0.15f))
            pie131(Offset(w * 0.3f, h * 0.36f), w * 0.07f, 0.2f, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("wshukm3", 0, 1), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ws-waris ═════════════
internal fun DrawScope.wswarisScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wswaris", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            person99(Offset(w * 0.72f, h * 0.56f), h * 0.12f, Fig131, f * seg(p, 0.05f, 0.18f))
            pie131(Offset(w * 0.3f, h * 0.44f), w * 0.07f, 0.5f, f * seg(p, 0.45f, 0.58f))
            for (i in 0 until 2) qIcon("cross", Offset(w * (0.72f - 0.42f * i), h * 0.44f), w * 0.1f, Qn.Rose, 0.85f * f * seg(p, 0.3f + 0.4f * i, 0.4f + 0.4f * i), a.t)
            row131(a, "wswaris0", 2, h * 0.68f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            for (k in 0 until 4) {
                val x = w * (0.2f + 0.2f * k)
                person99(Offset(x, g), h * 0.11f, Dim131, f)
                qIcon("check", Offset(x, g - h * 0.2f), w * 0.035f, Qn.Jade, f * seg(p, 0.35f + 0.08f * k, 0.45f + 0.08f * k), a.t)
            }
            tagAt(a.tag("wswaris1", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            coin131(Offset(w * 0.5f, h * 0.44f), w * 0.025f, f * seg(p, 0.05f, 0.18f))
            for (k in 0 until 3) child12(Offset(w * (0.3f + 0.2f * k), g), h * 0.08f, Dim131, f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("wswaris2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.78f, g), h * 0.14f, Fig131, f)
            for (k in 0 until 2) coin131(Offset(mix(w * 0.7f, w * (0.4f - 0.12f * k), easeInOut(seg(p, 0.3f + 0.1f * k, 0.55f + 0.1f * k))), g - h * 0.12f), w * 0.02f, f)
            person99(Offset(w * 0.36f, g), h * 0.13f, Dim131, f)
            woman12(Offset(w * 0.22f, g), h * 0.12f, Dim131, f)
            tagAt(a.tag("wswaris3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ws-huquq ═════════════
internal fun DrawScope.wshuquqScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "wshuquq", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            estate137(h * 0.3f, listOf(0.2f to Fig131, 0.2f to Qn.Rose, 0.2f to Color(0xFFB0506A), 0.15f to Qn.Lapis, 0.25f to Qn.Jade), p, f, 0.1f, 0.15f)
            row131(a, "wshuquq0", 5, h * 0.5f, p, f, from = 0.1f, step = 0.15f)
        },
        { p, f ->
            veil131(f)
            row131(a, "wshuquq1", 3, h * 0.42f, p, f, from = 0.1f, step = 0.3f)
            for (i in 0 until 2) arrow131(Offset(w * (0.66f - 0.28f * i), h * 0.56f), Offset(w * (0.56f - 0.28f * i), h * 0.56f), seg(p, 0.3f + 0.3f * i, 0.4f + 0.3f * i), Qn.GoldLight, f)
        },
        { p, f ->
            veil131(f)
            estate137(h * 0.34f, listOf((1f / 3f) to Qn.Lapis, (2f / 3f) to Qn.Jade), p, f, 0.35f, 0.3f)
            row131(a, "wshuquq2", 3, h * 0.56f, p, f, from = 0.1f, step = 0.28f)
        },
        { p, f ->
            person99(Offset(w * 0.5f, h * 0.8f), h * 0.15f, Dim131, f)
            val cloak = Path().apply {
                moveTo(w * 0.44f, h * 0.6f); lineTo(w * 0.56f, h * 0.6f); lineTo(w * 0.59f, h * 0.74f); lineTo(w * 0.41f, h * 0.74f); close()
            }
            drawPath(cloak, Qn.Lapis, alpha = 0.85f * f * seg(p, 0.1f, 0.25f))
            qIcon("cup", Offset(w * 0.72f, h * 0.68f), w * 0.06f, Fig131, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("wshuquq3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}
