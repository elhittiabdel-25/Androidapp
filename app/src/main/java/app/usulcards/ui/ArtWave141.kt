package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 141 (2026-11-21): judgeship, testimony and admission, the last books of al-Sharh al-Mumti' (abridged). The
 * judge is a scale, the mufti a book; anger a flame; a bribe coins under the table; a witness an eye then a mouth.
 * Faceless figures only; the last card closes on the word of tawhid as a tag at the top.
 */

private fun DrawScope.icons141(names: List<String>, y: Float, s: Float, colors: List<Color>, p: Float, f: Float, from: Float, step: Float, t: Float) {
    val n = names.size
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        qIcon(names[i], Offset(x, y), s, colors[i % colors.size], f * seg(p, from + step * i, from + step * i + 0.14f), t)
    }
}

// ═════════════ qd-qada ═════════════
internal fun DrawScope.qdqadaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "qdqada", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            icons141(listOf("book", "lock", "scale"), h * 0.34f, w * 0.08f, listOf(Qn.Gold, Qn.GoldLight, Qn.Gold), p, f, 0.1f, 0.25f, a.t)
            row131(a, "qdqada0", 3, h * 0.56f, p, f, from = 0.1f, step = 0.25f)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig131, f)
            qIcon("scale", Offset(w * 0.72f, g - h * 0.26f), w * 0.07f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            person99(Offset(w * 0.28f, g), h * 0.14f, Fig131, f)
            qIcon("book", Offset(w * 0.28f, g - h * 0.26f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("qdqada1", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 7) {
                val one = k == 3
                person99(Offset(w * (0.14f + 0.12f * k), g), h * (if (one) 0.14f else 0.1f), if (one) Fig131 else Dim131, f)
            }
            qIcon("scale", Offset(w * 0.5f, g - h * 0.26f), w * 0.06f, Qn.Gold, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("qdqada2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.15f, Fig131, f)
            voice130(Offset(w * 0.72f, g - h * 0.2f), 0.5f, f * seg(p, 0.05f, 0.2f), a.t)
            person99(Offset(w * 0.32f, g), h * 0.14f, Fig131, f)
            softGlow(Offset(w * 0.32f, g - h * 0.1f), w * 0.09f, Qn.GoldLight, 0.6f * f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("qdqada3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ qd-ghadab ═════════════
internal fun DrawScope.qdghadabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "qdghadab", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (i in 0 until 3) qIcon("flame", Offset(w * (0.78f - 0.28f * i), h * 0.36f), w * (0.05f + 0.03f * i), Qn.Rose, f * seg(p, 0.15f + 0.25f * i, 0.27f + 0.25f * i), a.t)
            row131(a, "qdghadab0", 3, h * 0.58f, p, f, from = 0.15f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            qIcon("flame", Offset(w * 0.72f, h * 0.34f), w * 0.05f, Qn.Rose, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("check", Offset(w * 0.72f, h * 0.48f), w * 0.05f, Qn.Jade, f * seg(p, 0.2f, 0.3f), a.t)
            qIcon("flame", Offset(w * 0.28f, h * 0.32f), w * 0.1f, Qn.Rose, f * seg(p, 0.5f, 0.62f), a.t)
            qIcon("cross", Offset(w * 0.28f, h * 0.48f), w * 0.05f, Qn.Rose, f * seg(p, 0.65f, 0.75f), a.t)
            row131(a, "qdghadab1", 2, h * 0.64f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("eye", Offset(w * 0.68f, h * 0.38f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            arrow131(Offset(w * 0.6f, h * 0.38f), Offset(w * 0.4f, h * 0.38f), seg(p, 0.25f, 0.45f), Qn.GoldLight, f)
            qIcon("scale", Offset(w * 0.3f, h * 0.38f), w * 0.1f, Qn.Gold, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("qdghadab2", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            val g = h * 0.8f
            for (k in 0 until 2) person99(Offset(w * mix(0.6f + 0.1f * k, 0.9f + 0.1f * k, seg(p, 0.2f, 0.6f)), g), h * 0.12f, Dim131, f, walk = a.t)
            for (k in 0 until 3) qIcon("sun", Offset(w * (0.2f + 0.12f * k), h * 0.4f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.45f + 0.08f * k, 0.55f + 0.08f * k), a.t)
            tagAt(a.tag("qdghadab3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ qd-rishwa ═════════════
internal fun DrawScope.qdrishwaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "qdrishwa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            drawRect(Color(0xFF6E5432), Offset(w * 0.3f, g - h * 0.1f), Size(w * 0.4f, h * 0.03f), alpha = f)
            person99(Offset(w * 0.78f, g), h * 0.14f, Fig131, f)
            person99(Offset(w * 0.22f, g), h * 0.14f, Dim131, f)
            coin131(Offset(mix(w * 0.7f, w * 0.3f, easeInOut(seg(p, 0.15f, 0.45f))), g - h * 0.04f), w * 0.02f, f)
            qIcon("cross", Offset(w * 0.5f, h * 0.44f), w * 0.08f, Qn.Rose, f * seg(p, 0.55f, 0.67f), a.t)
            tagAt(a.tag("qdrishwa0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) coin131(Offset(w * 0.72f, h * (0.44f - 0.035f * k)), w * 0.03f, f * seg(p, 0.05f + 0.05f * k, 0.15f + 0.05f * k))
            for (k in 0 until 4) coin131(Offset(w * 0.28f, h * (0.44f - 0.035f * k)), w * 0.03f, f * seg(p, 0.45f + 0.05f * k, 0.55f + 0.05f * k))
            row131(a, "qdrishwa1", 2, h * 0.62f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig131, f)
            person99(Offset(w * 0.28f, g), h * 0.14f, Dim131, f)
            qIcon("check", Offset(w * 0.72f, h * 0.44f), w * 0.05f, Qn.Jade, f * seg(p, 0.4f, 0.52f), a.t)
            qIcon("cross", Offset(w * 0.28f, h * 0.44f), w * 0.05f, Qn.Rose, f * seg(p, 0.2f, 0.32f), a.t)
            tagAt(a.tag("qdrishwa2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            box131(Offset(mix(w * 0.32f, w * 0.68f, easeInOut(seg(p, 0.3f, 0.6f))), h * 0.4f), w * 0.06f, Qn.Rose, f)
            arrow131(Offset(w * 0.3f, h * 0.54f), Offset(w * 0.7f, h * 0.54f), seg(p, 0.3f, 0.6f), Qn.GoldLight, f)
            tagAt(a.tag("qdrishwa3", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ qd-bayyina ═════════════
internal fun DrawScope.qdbayyinaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "qdbayyina", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.74f, g), h * 0.14f, Fig131, f)
            qIcon("scroll", Offset(w * 0.74f, g - h * 0.26f), w * 0.05f, Qn.Gold, f * seg(p, 0.1f, 0.22f), a.t)
            person99(Offset(w * 0.26f, g), h * 0.14f, Fig131, f)
            qIcon("lips", Offset(w * 0.26f, g - h * 0.26f), w * 0.05f, Fig131, f * seg(p, 0.55f, 0.67f), a.t)
            row131(a, "qdbayyina0", 2, h * 0.24f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            person99(Offset(w * 0.78f, g), h * 0.13f, Dim131, f)
            person99(Offset(w * 0.22f, g), h * 0.14f, Fig131, f)
            qIcon("scale", Offset(w * 0.5f, h * 0.5f), w * 0.14f, Qn.Gold, f, a.t)
            softGlow(Offset(w * 0.22f, g - h * 0.1f), w * 0.08f, Qn.GoldLight, 0.5f * f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("qdbayyina1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            icons141(listOf("eye", "house", "people"), h * 0.34f, w * 0.08f, listOf(Qn.GoldLight, Fig131, Fig131), p, f, 0.15f, 0.25f, a.t)
            row131(a, "qdbayyina2", 3, h * 0.56f, p, f, from = 0.15f, step = 0.25f)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig131, f)
            qIcon("scale", Offset(w * 0.72f, g - h * 0.26f), w * 0.06f, Qn.Gold, f, a.t)
            person99(Offset(w * 0.28f, g), h * 0.14f, Fig131, f * seg(p, 0.1f, 0.2f))
            arrow131(Offset(w * 0.6f, g - h * 0.12f), Offset(w * 0.4f, g - h * 0.12f), seg(p, 0.2f, 0.4f), Qn.GoldLight, f)
            row131(a, "qdbayyina3", 2, h * 0.24f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ sh-shahada ═════════════
internal fun DrawScope.shshahadaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "shshahada", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("eye", Offset(w * 0.64f, h * 0.38f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("lips", Offset(w * 0.36f, h * 0.38f), w * 0.08f, Fig131, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("shshahada0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("eye", Offset(w * 0.72f, h * 0.38f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("lips", Offset(w * 0.3f, h * 0.34f), w * 0.07f, Fig131, f * seg(p, 0.5f, 0.62f), a.t)
            qIcon("scale", Offset(w * 0.24f, h * 0.44f), w * 0.07f, Qn.Gold, f * seg(p, 0.55f, 0.68f), a.t)
            row131(a, "shshahada1", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("cloud", Offset(w * 0.72f, h * 0.38f), w * 0.1f, Dim131, f * seg(p, 0.1f, 0.22f), a.t)
            qIcon("hourglass", Offset(w * 0.28f, h * 0.38f), w * 0.08f, Dim131, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "shshahada2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("lips", Offset(w * 0.5f, h * 0.4f), w * 0.1f, Dim131, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.4f), w * 0.13f, Qn.Rose, 0.8f * f * seg(p, 0.35f, 0.48f), a.t)
            tagAt(a.tag("shshahada3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ sh-nisab ═════════════
internal fun DrawScope.shnisabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "shnisab", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            val q0 = f * seg(p, 0.1f, 0.22f)
            val q1 = f * seg(p, 0.35f, 0.47f)
            val q2 = f * seg(p, 0.6f, 0.72f)
            for (k in 0 until 2) person99(Offset(w * (0.74f + 0.06f * k), g), h * 0.1f, Fig131, q0)
            person99(Offset(w * 0.44f, g), h * 0.1f, Fig131, q1)
            for (k in 0 until 2) woman12(Offset(w * (0.5f + 0.06f * k), g), h * 0.09f, Fig131, q1)
            person99(Offset(w * 0.2f, g), h * 0.1f, Fig131, q2)
            qIcon("lips", Offset(w * 0.12f, g - h * 0.12f), w * 0.035f, Qn.GoldLight, q2, a.t)
            row131(a, "shnisab0", 3, h * 0.3f, p, f, from = 0.1f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) coin131(Offset(w * (0.46f + 0.04f * k), h * (0.4f - 0.03f * k)), w * 0.03f, f * seg(p, 0.05f, 0.18f))
            tagAt(a.tag("shnisab1", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.5f, g - h * 0.14f), w * 0.24f, Dim131, f, a.t)
            for (k in 0 until 2) person99(Offset(w * (0.42f + 0.16f * k), g), h * 0.12f, Fig131, f * seg(p, 0.2f + 0.1f * k, 0.32f + 0.1f * k))
            tagAt(a.tag("shnisab2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig131, f)
            person99(Offset(w * 0.32f, g), h * 0.14f, Fig131, f)
            voice130(Offset(w * 0.72f, g - h * 0.2f), 0.4f, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("heart", Offset(w * 0.5f, h * 0.5f), w * 0.05f, Qn.Rose, f * seg(p, 0.55f, 0.68f), a.t)
            tagAt(a.tag("shnisab3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ qr-iqrar ═════════════
internal fun DrawScope.qriqrarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "qriqrar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.62f, g), h * 0.14f, Fig131, f)
            voice130(Offset(w * 0.62f, g - h * 0.2f), 0.4f, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("scroll", Offset(w * 0.32f, h * 0.56f), w * 0.07f, Qn.Gold, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("qriqrar0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) coin131(Offset(w * 0.72f, h * (0.4f - 0.035f * k)), w * 0.03f, f * seg(p, 0.05f, 0.18f))
            qIcon("check", Offset(w * 0.72f, h * 0.24f), w * 0.04f, Qn.Jade, f * seg(p, 0.2f, 0.3f), a.t)
            for (k in 0 until 4) coin131(Offset(w * 0.28f, h * (0.4f - 0.035f * k)), w * 0.03f, f * seg(p, 0.45f, 0.58f))
            qIcon("cross", Offset(w * 0.28f, h * 0.2f), w * 0.04f, Qn.Rose, f * seg(p, 0.6f, 0.7f), a.t)
            row131(a, "qriqrar1", 2, h * 0.62f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("qriqrar2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.14f, Qn.GoldLight, 0.7f * f * seg(p, 0.4f, 0.65f))
            qIcon("book", Offset(w * 0.5f, h * 0.5f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
        },
        { p, f ->
            for (k in 0 until 5) qIcon("tree", Offset(w * (0.14f + 0.18f * k), g - h * 0.08f), w * 0.1f, Qn.Jade, f * seg(p, 0.05f + 0.06f * k, 0.18f + 0.06f * k), a.t)
            softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.2f, Qn.GoldLight, 0.4f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("qriqrar3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}
