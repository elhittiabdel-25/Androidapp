package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 139 (2026-11-21): crimes, blood money and hadd from al-Sharh al-Mumti' (abridged), kept symbolic: no violence
 * is drawn. Justice is a scale, a right a small lamp that stays lit or goes out, the blood money gold and silver (the
 * camels only named), the penalty a sealed scroll. Faceless figures only.
 */

private fun DrawScope.icons139(names: List<String>, y: Float, s: Float, colors: List<Color>, p: Float, f: Float, from: Float, step: Float, t: Float) {
    val n = names.size
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        qIcon(names[i], Offset(x, y), s, colors[i % colors.size], f * seg(p, from + step * i, from + step * i + 0.14f), t)
    }
}

private fun DrawScope.grid139(a: ArtCtx, kind: String, p: Float, f: Float, y0: Float, dy: Float) {
    for (i in 0 until 4) {
        val x = if (i % 2 == 0) size.width * 0.72f else size.width * 0.28f
        tagAt(a.tag(kind, 0, i), Offset(x, y0 + dy * (i / 2)), f * seg(p, 0.08f + 0.2f * i, 0.2f + 0.2f * i), ink = ArtInk.Ivory)
    }
}

// ═════════════ jn-huquq ═════════════
internal fun DrawScope.jnhuquqScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jnhuquq", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            row131(a, "jnhuquq0", 3, h * 0.24f, p, f, from = 0.05f, step = 0.28f)
            softGlow(Offset(w * 0.78f, h * 0.48f), w * 0.07f, Qn.GoldLight, 0.7f * f * seg(p, 0.1f, 0.25f))
            qIcon("people", Offset(w * 0.5f, h * 0.48f), w * 0.11f, Fig131, f * seg(p, 0.38f, 0.5f), a.t)
            drawCircle(Dim131, w * 0.05f, Offset(w * 0.22f, h * 0.48f), alpha = 0.6f * f * seg(p, 0.65f, 0.78f), style = androidx.compose.ui.graphics.drawscope.Stroke(w * 0.006f))
        },
        { p, f ->
            veil131(f)
            icons139(listOf("check", "check", "cross"), h * 0.36f, w * 0.06f, listOf(Qn.Jade, Qn.Jade, Qn.Rose), p, f, 0.2f, 0.28f, a.t)
            row131(a, "jnhuquq1", 3, h * 0.56f, p, f, from = 0.1f, step = 0.28f)
        },
        { p, f ->
            person99(Offset(w * 0.5f, h * 0.8f), h * 0.13f, Fig131, f, lean = 0.15f)
            softGlow(Offset(w * 0.5f, h * mix(0.2f, 0.6f, seg(p, 0.3f, 0.7f))), w * 0.08f, Qn.GoldLight, 0.7f * f * seg(p, 0.25f, 0.4f))
            tagAt(a.tag("jnhuquq2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.38f), w * 0.14f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Qn.Rose, 0.6f * f * seg(p, 0.4f, 0.52f), a.t)
            tagAt(a.tag("jnhuquq3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ jn-maratib ═════════════
internal fun DrawScope.jnmaratibScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jnmaratib", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.38f), w * 0.16f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("jnmaratib0", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            icons139(listOf("scale", "coin", "heart"), h * 0.36f, w * 0.08f, listOf(Qn.Gold, Qn.GoldLight, Qn.Rose), p, f, 0.15f, 0.22f, a.t)
            row131(a, "jnmaratib1", 3, h * 0.56f, p, f, from = 0.15f, step = 0.22f)
        },
        { p, f ->
            veil131(f)
            qIcon("eye", Offset(w * 0.72f, h * 0.38f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.1f, 0.22f), a.t)
            qIcon("heart", Offset(w * 0.28f, h * 0.38f), w * 0.08f, Dim131, f * seg(p, 0.45f, 0.58f), a.t)
            row131(a, "jnmaratib2", 2, h * 0.6f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.14f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            row131(a, "jnmaratib3", 2, h * 0.6f, p, f, from = 0.3f, step = 0.3f, ink = ArtInk.Gold)
        },
    )
}

// ═════════════ jn-hayat ═════════════
internal fun DrawScope.jnhayatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "jnhayat", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            val grow = easeInOut(seg(p, 0.15f, 0.6f))
            qIcon("sprout", Offset(w * 0.5f, g - h * 0.06f * grow), w * (0.06f + 0.1f * grow), Qn.Jade, f, a.t)
            qIcon("scale", Offset(w * 0.5f, h * 0.34f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            tagAt(a.tag("jnhayat0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("jnhayat1", 0, 0), Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            qIcon("cross", Offset(w * 0.5f, h * 0.42f), w * 0.14f, Qn.Rose, 0.7f * f * seg(p, 0.55f, 0.68f), a.t)
        },
        { p, f ->
            veil131(f)
            row131(a, "jnhayat2", 5, h * 0.45f, p, f, from = 0.12f, step = 0.15f)
        },
        { p, f ->
            person99(Offset(w * 0.6f, g), h * 0.15f, Fig131, f)
            child12(Offset(w * 0.42f, g), h * 0.08f, Fig131, f)
            softGlow(Offset(w * 0.5f, g - h * 0.1f), w * 0.12f, Qn.GoldLight, 0.5f * f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("jnhayat3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ jn-sabab ═════════════
internal fun DrawScope.jnsababScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "jnsabab", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            drawRect(Color(0xFF3A3448), Offset(0f, g - h * 0.03f), Size(w, h * 0.06f), alpha = f)
            drawOval(Color(0xFF0E0A1A), Offset(w * 0.4f, g - h * 0.025f), Size(w * 0.2f, h * 0.05f), alpha = f * seg(p, 0.1f, 0.25f))
            person99(Offset(w * 0.24f, g), h * 0.13f, Fig131, f * seg(p, 0.05f, 0.18f))
            row131(a, "jnsabab0", 2, h * 0.3f, p, f, from = 0.1f, step = 0.3f)
        },
        { p, f ->
            person99(Offset(w * 0.74f, g), h * 0.14f, Dim131, f)
            person99(Offset(w * 0.32f, g), h * 0.14f, Fig131, f)
            val q = easeInOut(seg(p, 0.1f, 0.35f))
            drawLine(Fig131, Offset(mix(w * 0.66f, w * 0.4f, q), g - h * 0.1f), Offset(mix(w * 0.66f, w * 0.4f, q) - w * 0.04f, g - h * 0.1f), w * 0.006f, alpha = f)
            qIcon("scale", Offset(w * 0.32f, h * 0.42f), w * 0.07f, Qn.Gold, f * seg(p, 0.55f, 0.68f), a.t)
            tagAt(a.tag("jnsabab1", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 3) person99(Offset(w * (0.62f + 0.1f * k), g), h * 0.11f, Dim131, f)
            qIcon("scroll", Offset(w * 0.72f, h * 0.44f), w * 0.06f, Fig131, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("scale", Offset(w * 0.28f, h * 0.5f), w * 0.1f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            arrow131(Offset(w * 0.4f, h * 0.5f), Offset(w * 0.58f, h * 0.5f), seg(p, 0.65f, 0.8f), Qn.Rose, f)
            row131(a, "jnsabab2", 2, h * 0.24f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            person99(Offset(w * 0.68f, g), h * 0.15f, Fig131, f)
            child12(Offset(w * 0.36f, g), h * 0.08f, Fig131, f)
            voice130(Offset(w * 0.68f, g - h * 0.2f), 0.5f, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("scale", Offset(w * 0.68f, h * 0.38f), w * 0.07f, Qn.Gold, f * seg(p, 0.45f, 0.58f), a.t)
            tagAt(a.tag("jnsabab3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ jn-diya ═════════════
internal fun DrawScope.jndiyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jndiya", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            icons139(listOf("people", "hands", "eye"), h * 0.36f, w * 0.08f, listOf(Fig131, Fig131, Qn.GoldLight), p, f, 0.15f, 0.22f, a.t)
            row131(a, "jndiya0", 3, h * 0.56f, p, f, from = 0.15f, step = 0.22f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) coin131(Offset(w * (0.42f + 0.04f * k), h * (0.38f - 0.03f * k)), w * 0.03f, f * seg(p, 0.35f, 0.5f))
            for (k in 0 until 3) {
                val c = Offset(w * (0.12f + 0.04f * k), h * (0.38f - 0.03f * k))
                drawCircle(Color(0xFFC8CCD4), w * 0.03f, c, alpha = f * seg(p, 0.65f, 0.78f))
            }
            row131(a, "jndiya1", 3, h * 0.58f, p, f, from = 0.1f, step = 0.28f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("check", Offset(w * 0.72f, h * 0.38f), w * 0.07f, Qn.Jade, f * seg(p, 0.1f, 0.22f), a.t)
            qIcon("scale", Offset(w * 0.28f, h * 0.38f), w * 0.09f, Dim131, f * seg(p, 0.5f, 0.6f), a.t)
            qIcon("cross", Offset(w * 0.28f, h * 0.38f), w * 0.1f, Qn.Rose, 0.7f * f * seg(p, 0.6f, 0.7f), a.t)
            row131(a, "jndiya2", 2, h * 0.6f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            person99(Offset(w * 0.72f, h * 0.8f), h * 0.14f, Fig131, f)
            person99(Offset(w * 0.28f, h * 0.8f), h * 0.14f, Fig131, f)
            qIcon("hands", Offset(w * 0.5f, h * 0.6f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("jndiya3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ hd-hadd ═════════════
internal fun DrawScope.hdhaddScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "hdhadd", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.36f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("lock", Offset(w * 0.5f, h * 0.48f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("hdhadd0", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("lock", Offset(w * 0.72f, h * 0.38f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.1f, 0.22f), a.t)
            qIcon("drop", Offset(w * 0.28f, h * 0.38f), w * 0.08f, Qn.Lapis, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "hdhadd1", 2, h * 0.6f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.72f, h * 0.38f), w * 0.08f, Fig131, f * seg(p, 0.1f, 0.22f), a.t)
            qIcon("scale", Offset(w * 0.28f, h * 0.38f), w * 0.1f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "hdhadd2", 2, h * 0.6f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Qn.Rose, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("hdhadd3", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ hd-shurut ═════════════
internal fun DrawScope.hdshurutScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "hdshurut", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            grid139(a, "hdshurut0", p, f, h * 0.34f, h * 0.22f)
        },
        { p, f ->
            for (k in 0 until 4) person99(Offset(w * (0.2f + 0.2f * k), g), h * 0.12f, Fig131, f)
            qIcon("crown", Offset(w * 0.8f, g - h * 0.27f), w * 0.04f, Qn.Gold, f * seg(p, 0.1f, 0.22f), a.t)
            sack131(Offset(w * 0.4f, g - h * 0.03f), w * 0.04f, Sack131, f * seg(p, 0.5f, 0.62f))
            qIcon("scale", Offset(w * 0.5f, h * 0.44f), w * 0.1f, Qn.Gold, f * seg(p, 0.7f, 0.82f), a.t)
            row131(a, "hdshurut1", 2, h * 0.22f, p, f, from = 0.1f, step = 0.42f)
        },
        { p, f ->
            child12(Offset(w * 0.5f, g), h * 0.09f, Fig131, f)
            tagAt(a.tag("hdshurut2", 0, 0), Offset(w * 0.72f, h * 0.36f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdshurut2", 0, 1), Offset(w * 0.28f, h * 0.36f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("hdshurut3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            qIcon("hands", Offset(w * 0.5f, h * 0.48f), w * 0.1f, Fig131, f * seg(p, 0.3f, 0.45f), a.t)
        },
    )
}
