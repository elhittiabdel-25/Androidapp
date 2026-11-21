package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 140 (2026-11-21): foods, oaths and vows from al-Sharh al-Mumti' (abridged). Food is grain, fruit and cups;
 * beasts and donkeys are only named; an oath is a spoken word sealed or left loose. Abu Bakr a companion roundel,
 * al-Sa'b ibn Jaththama a plain roundel.
 */

private fun DrawScope.icons140(names: List<String>, y: Float, s: Float, colors: List<Color>, p: Float, f: Float, from: Float, step: Float, t: Float) {
    val n = names.size
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        val q = f * seg(p, from + step * i, from + step * i + 0.14f)
        if (names[i] == "cloth") blanket140(Offset(x, y), s * 0.8f, q) else qIcon(names[i], Offset(x, y), s, colors[i % colors.size], q, t)
    }
}

private fun DrawScope.blanket140(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Qn.Lapis, Offset(c.x - s, c.y - s * 0.6f), Size(s * 2f, s * 1.2f), CornerRadius(s * 0.15f), alpha = alpha)
    for (k in 1 until 4) drawLine(Qn.GoldLight, Offset(c.x - s, c.y - s * 0.6f + s * 0.3f * k), Offset(c.x + s, c.y - s * 0.6f + s * 0.3f * k), s * 0.04f, alpha = alpha * 0.6f)
}

// ═════════════ tm-asl ═════════════
internal fun DrawScope.tmaslScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tmasl", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            icons140(listOf("wheat", "cup", "tree", "drop"), h * 0.36f, w * 0.08f, listOf(Qn.GoldLight, Fig131, Qn.Jade, Qn.Lapis), p, f, 0.05f, 0.1f, a.t)
            qIcon("check", Offset(w * 0.5f, h * 0.2f), w * 0.05f, Qn.Jade, f * seg(p, 0.5f, 0.62f), a.t)
            tagAt(a.tag("tmasl0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            person99(Offset(w * 0.7f, h * 0.78f), h * 0.13f, Dim131, f)
            qIcon("scroll", Offset(w * 0.34f, h * 0.42f), w * 0.09f, Qn.Gold, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("tmasl1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.6f, h * 0.8f), h * 0.15f, Fig131, f)
            qIcon("cup", Offset(w * 0.4f, h * 0.64f), w * 0.06f, Fig131, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("tmasl2", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            icons140(listOf("wheat", "tree", "sprout"), h * 0.38f, w * 0.09f, listOf(Qn.GoldLight, Qn.Jade, Qn.Jade), p, f, 0.05f, 0.12f, a.t)
            tagAt(a.tag("tmasl3", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tm-sibaa ═════════════
internal fun DrawScope.tmsibaaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tmsibaa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            tagAt(a.tag("tmsibaa0", 0, 0), Offset(w * 0.5f, h * 0.4f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            qIcon("cross", Offset(w * 0.5f, h * 0.62f), w * 0.08f, Qn.Rose, f * seg(p, 0.4f, 0.52f), a.t)
        },
        { p, f ->
            person99(Offset(w * 0.4f, h * 0.8f), h * 0.15f, Fig131, f)
            qIcon("cup", Offset(w * 0.68f, h * 0.6f), w * 0.07f, Fig131, f * seg(p, 0.05f, 0.18f), a.t)
            arrow131(Offset(w * 0.62f, h * 0.6f), Offset(w * 0.46f, h * 0.66f), seg(p, 0.25f, 0.45f), Qn.GoldLight, f)
            softGlow(Offset(w * 0.4f, h * 0.7f), w * 0.08f, Qn.GoldLight, 0.5f * f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("tmsibaa1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.72f, h * 0.38f), w * 0.12f, Fig131, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("cross", Offset(w * 0.72f, h * 0.24f), w * 0.05f, Qn.Rose, f * seg(p, 0.2f, 0.3f), a.t)
            qIcon("mountain", Offset(w * 0.28f, h * 0.38f), w * 0.14f, Color(0xFF6A5A48), f * seg(p, 0.5f, 0.62f), a.t)
            qIcon("check", Offset(w * 0.28f, h * 0.24f), w * 0.05f, Qn.Jade, f * seg(p, 0.65f, 0.75f), a.t)
            row131(a, "tmsibaa2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.7f, h * 0.36f), w * 0.09f, a, "tmsibaa3", 0, f * seg(p, 0.02f, 0.15f))
            qIcon("kaaba", Offset(w * 0.3f, h * 0.38f), w * 0.1f, Qn.Gold, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("tmsibaa3", 0, 1), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tm-idtirar ═════════════
internal fun DrawScope.tmidtirarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "tmidtirar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.64f, g), h * 0.14f, Fig131, f, lean = 0.1f)
            qIcon("wheat", Offset(w * 0.36f, g - h * 0.1f), w * 0.08f, Qn.GoldLight, f * (1f - 0.8f * seg(p, 0.5f, 0.8f)), a.t)
            tagAt(a.tag("tmidtirar0", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("wheat", Offset(w * 0.72f, h * 0.38f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.05f, 0.18f) * (1f - 0.8f * seg(p, 0.3f, 0.45f)), a.t)
            blanket140(Offset(w * 0.28f, h * 0.38f), w * 0.08f, f * seg(p, 0.5f, 0.62f))
            row131(a, "tmidtirar1", 2, h * 0.6f, p, f, from = 0.2f, step = 0.45f)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig131, f)
            person99(Offset(w * 0.3f, g), h * 0.13f, Dim131, f)
            qIcon("cup", Offset(w * 0.6f, g - h * 0.1f), w * 0.05f, Fig131, f, a.t)
            row131(a, "tmidtirar2", 2, h * 0.28f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("book", Offset(w * 0.5f, h * 0.36f), w * 0.09f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 4) person99(Offset(w * (0.2f + 0.2f * k), g), h * 0.1f, Dim131, f * seg(p, 0.2f + 0.05f * k, 0.32f + 0.05f * k))
            tagAt(a.tag("tmidtirar3", 0, 0), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ym-hukm ═════════════
internal fun DrawScope.ymhukmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ymhukm", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("lips", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Fig131, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("lock", Offset(w * 0.62f, h * 0.46f), w * 0.05f, Qn.Gold, f * seg(p, 0.35f, 0.48f), a.t)
            tagAt(a.tag("ymhukm0", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            row131(a, "ymhukm1", 3, h * 0.42f, p, f, from = 0.1f, step = 0.2f, ink = ArtInk.Gold)
            for (i in 0 until 3) qIcon("point", Offset(w * (0.5f + (1f - i) * 0.28f), h * 0.58f), w * 0.035f, Qn.GoldLight, f * seg(p, 0.12f + 0.2f * i, 0.24f + 0.2f * i), a.t)
        },
        { p, f ->
            person99(Offset(w * 0.5f, h * 0.8f), h * 0.15f, Fig131, f)
            qIcon("lips", Offset(w * 0.5f, h * 0.5f), w * 0.06f, Fig131, f * seg(p, 0.1f, 0.22f), a.t)
            softGlow(Offset(w * 0.5f, h * 0.66f), w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("ymhukm2", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.14f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            row131(a, "ymhukm3", 2, h * 0.62f, p, f, from = 0.15f, step = 0.4f)
        },
    )
}

// ═════════════ ym-ikram ═════════════
internal fun DrawScope.ymikramScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "ymikram", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            qIcon("house", Offset(w * 0.5f, g - h * 0.14f), w * 0.26f, Dim131, f, a.t)
            person99(Offset(w * 0.68f, g), h * 0.14f, Fig131, f)
            person99(Offset(w * 0.32f, g), h * 0.14f, Fig131, f)
            voice130(Offset(w * 0.68f, g - h * 0.2f), 0.4f, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("heart", Offset(w * 0.5f, h * 0.34f), w * 0.05f, Qn.Rose, f * seg(p, 0.5f, 0.62f), a.t)
            tagAt(a.tag("ymikram0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            close97(Offset(w * 0.5f, h * 0.28f), w * 0.09f, a, "ymikram1", 0, 0, f * seg(p, 0.02f, 0.15f))
            for (k in 0 until 3) person99(Offset(w * (0.3f + 0.2f * k), g), h * 0.1f, Dim131, f)
            for (k in 0 until 3) qIcon("cup", Offset(w * (0.36f + 0.14f * k), h * 0.62f), w * 0.04f, Fig131, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("ymikram1", 0, 1), Offset(w * 0.5f, h * 0.48f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            drawOval(Color(0xFF0E0A1A), Offset(w * 0.3f, h * 0.36f), Size(w * 0.4f, h * 0.12f), alpha = f * seg(p, 0.05f, 0.2f))
            qIcon("lips", Offset(w * 0.5f, h * mix(0.3f, 0.42f, seg(p, 0.4f, 0.7f))), w * 0.06f, Dim131, f * (1f - seg(p, 0.6f, 0.75f)), a.t)
            row131(a, "ymikram2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            for (k in 0 until 3) coin131(Offset(w * (0.3f + 0.04f * k), h * (0.44f - 0.03f * k)), w * 0.022f, f * seg(p, 0.3f, 0.42f))
            qIcon("cross", Offset(w * 0.34f, h * 0.4f), w * 0.08f, Qn.Rose, 0.8f * f * seg(p, 0.55f, 0.65f), a.t)
            tagAt(a.tag("ymikram3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ym-laghw ═════════════
internal fun DrawScope.ymlaghwScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ymlaghw", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.5f, h * 0.8f), h * 0.15f, Fig131, f)
            for (k in 0 until 3) qIcon("lips", Offset(w * (0.36f + 0.14f * k), h * (0.44f - 0.04f * (k % 2))), w * 0.04f, Fig131, 0.7f * f * seg(p, 0.1f + 0.1f * k, 0.2f + 0.1f * k), a.t)
            tagAt(a.tag("ymlaghw0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            icons140(listOf("cup", "cloth", "chain"), h * 0.36f, w * 0.08f, listOf(Fig131, Qn.Lapis, Qn.GoldLight), p, f, 0.15f, 0.22f, a.t)
            row131(a, "ymlaghw1", 3, h * 0.56f, p, f, from = 0.15f, step = 0.22f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) qIcon("sun", Offset(w * (0.7f - 0.2f * k), h * 0.36f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.1f + 0.1f * k, 0.22f + 0.1f * k), a.t)
            for (k in 0 until 2) drawLine(Qn.GoldLight, Offset(w * (0.65f - 0.2f * k), h * 0.36f), Offset(w * (0.55f - 0.2f * k), h * 0.36f), w * 0.006f, alpha = f * seg(p, 0.45f, 0.55f))
            row131(a, "ymlaghw2", 2, h * 0.6f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 10) {
                val ang = k / 10f * 6.2832f
                drawCircle(Fig131, w * 0.02f, Offset(w * 0.5f + w * 0.2f * kotlin.math.cos(ang), h * 0.42f + h * 0.12f * kotlin.math.sin(ang)), alpha = f * seg(p, 0.1f + 0.03f * k, 0.2f + 0.03f * k))
            }
            qIcon("cup", Offset(w * 0.5f, h * 0.42f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.18f), a.t)
            tagAt(a.tag("ymlaghw3", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ym-nadhr ═════════════
internal fun DrawScope.ymnadhrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ymnadhr", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.5f, h * 0.8f), h * 0.15f, Fig131, f)
            qIcon("chain", Offset(w * 0.5f, h * 0.7f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.25f, 0.4f), a.t)
            tagAt(a.tag("ymnadhr0", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("hourglass", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Dim131, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.38f), w * 0.12f, Qn.Rose, 0.7f * f * seg(p, 0.5f, 0.62f), a.t)
            tagAt(a.tag("ymnadhr1", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.72f, h * 0.38f), w * 0.08f, Dim131, f * seg(p, 0.05f, 0.18f), a.t)
            softGlow(Offset(w * 0.28f, h * 0.38f), w * 0.08f, Qn.GoldLight, 0.6f * f * seg(p, 0.5f, 0.65f))
            qIcon("check", Offset(w * 0.28f, h * 0.38f), w * 0.07f, Qn.Jade, f * seg(p, 0.55f, 0.68f), a.t)
            row131(a, "ymnadhr2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("hourglass", Offset(w * 0.5f, h * 0.38f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ymnadhr3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}
