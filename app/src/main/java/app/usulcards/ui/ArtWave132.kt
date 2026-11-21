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
 * Wave 132 (2026-11-21): more transactions from al-Sharh al-Mumti' (abridged), with the kit of wave 131: the pledge
 * (the Prophet's ﷺ armour as a plain shape beside his named light), surety for the debt or for the person,
 * settlement, agency by word, deed and letter, hire of a benefit or of work (Ibn Urayqit a plain roundel), the loan
 * of use, and the lost property announced in the market, never inside the mosque. People are faceless.
 */

private fun DrawScope.armour132(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val p = Path().apply {
        moveTo(c.x - s * 0.35f, c.y - s * 0.6f); lineTo(c.x + s * 0.35f, c.y - s * 0.6f); lineTo(c.x + s * 0.6f, c.y - s * 0.35f)
        lineTo(c.x + s * 0.45f, c.y + s * 0.6f); lineTo(c.x - s * 0.45f, c.y + s * 0.6f); lineTo(c.x - s * 0.6f, c.y - s * 0.35f); close()
    }
    drawPath(p, Color(0xFF9AA3B2), alpha = alpha)
    for (k in 0 until 4) drawLine(Color(0xFF5E6676), Offset(c.x - s * 0.45f, c.y - s * 0.3f + s * 0.25f * k), Offset(c.x + s * 0.45f, c.y - s * 0.3f + s * 0.25f * k), s * 0.04f, alpha = alpha)
}

private fun DrawScope.mosque132(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(Fig131, Offset(c.x - s, c.y - s * 0.6f), Size(s * 2f, s * 0.6f), alpha = alpha * 0.8f)
    drawArc(Fig131, 180f, 180f, true, Offset(c.x - s * 0.55f, c.y - s * 1.15f), Size(s * 1.1f, s * 1.1f), alpha = alpha * 0.8f)
    drawRect(Fig131, Offset(c.x + s * 1.05f, c.y - s * 1.5f), Size(s * 0.22f, s * 1.5f), alpha = alpha * 0.8f)
    drawArc(Color(0xFF2A2140), 180f, 180f, true, Offset(c.x - s * 0.2f, c.y - s * 0.45f), Size(s * 0.4f, s * 0.4f), alpha = alpha)
    drawRect(Color(0xFF2A2140), Offset(c.x - s * 0.2f, c.y - s * 0.25f), Size(s * 0.4f, s * 0.25f), alpha = alpha)
}

private fun DrawScope.shop132(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(Color(0xFF6E5432), Offset(c.x - s, c.y - s), Size(s * 2f, s), alpha = alpha)
    for (k in 0 until 4) drawRect(if (k % 2 == 0) Qn.Rose else Fig131, Offset(c.x - s + s * 0.5f * k, c.y - s * 1.25f), Size(s * 0.5f, s * 0.25f), alpha = alpha)
}

// ═════════════ md-rahn ═════════════
internal fun DrawScope.mmrahnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmrahn", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.8f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.2f, g), h * 0.13f, Fig131, f)
            coin131(Offset(mix(w * 0.72f, w * 0.28f, easeInOut(seg(p, 0.05f, 0.3f))), g - h * 0.12f), w * 0.022f, f)
            box131(Offset(mix(w * 0.3f, w * 0.68f, easeInOut(seg(p, 0.4f, 0.7f))), g - h * 0.05f), w * 0.04f, Qn.Jade, f * seg(p, 0.35f, 0.45f))
            qIcon("lock", Offset(w * 0.68f, h * 0.48f), w * 0.05f, Qn.Gold, f * seg(p, 0.7f, 0.8f), a.t)
            tagAt(a.tag("mmrahn0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            val icons = listOf("eye", "lock", "coin", "people")
            for (i in 0 until 4) qIcon(icons[i], Offset(w * (0.5f + (1.5f - i) * 0.21f), h * 0.4f), w * 0.06f, Qn.Gold, f * seg(p, 0.1f + 0.18f * i, 0.22f + 0.18f * i), a.t)
            row131(a, "mmrahn1", 4, h * 0.58f, p, f, from = 0.1f, step = 0.18f)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.72f, h * 0.42f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.72f, h * 0.3f))
            armour132(Offset(w * 0.3f, h * 0.5f), w * 0.1f, f * seg(p, 0.3f, 0.45f))
            qIcon("lock", Offset(w * 0.3f, h * 0.5f), w * 0.04f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            tagAt(a.tag("mmrahn2", 0, 0), Offset(w * 0.3f, h * 0.7f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.7f, g - h * 0.1f), w * 0.16f, Fig131, f, a.t)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.4f + k / 3f) % 1f)
                coin131(Offset(mix(w * 0.6f, w * 0.32f, ph), g - h * 0.12f), w * 0.018f, f * (1f - ph) * seg(p, 0.2f, 0.35f))
            }
            drawRoundRect(Qn.Lapis, Offset(w * 0.2f, g - h * 0.16f), Size(w * 0.14f, h * 0.12f), CornerRadius(w * 0.02f), alpha = f)
            qIcon("lock", Offset(w * 0.27f, g - h * 0.1f), w * 0.035f, Qn.Gold, f, a.t)
            tagAt(a.tag("mmrahn3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-kafala ═════════════
internal fun DrawScope.mmkafalaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmkafala", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            coin131(Offset(w * 0.72f, g - h * 0.1f), w * 0.05f, f * seg(p, 0.05f, 0.2f))
            person99(Offset(w * 0.28f, g), h * 0.14f, Fig131, f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("mmkafala0", 0, 0), Offset(w * 0.72f, h * 0.36f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("mmkafala0", 0, 1), Offset(w * 0.28f, h * 0.36f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.42f), w * 0.18f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            row131(a, "mmkafala1", 2, h * 0.66f, p, f, from = 0.2f, step = 0.3f)
        },
        { p, f ->
            veil131(f)
            coin131(Offset(w * 0.72f, h * 0.42f), w * 0.05f, f * seg(p, 0.05f, 0.2f))
            qIcon("people", Offset(w * 0.28f, h * 0.42f), w * 0.08f, Fig131, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "mmkafala2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (i in 0 until 2) {
                val x = w * (0.71f - 0.42f * i)
                box131(Offset(x, h * 0.42f), w * 0.07f, Qn.Jade, f * seg(p, 0.1f + 0.35f * i, 0.22f + 0.35f * i))
                qIcon("lock", Offset(x, h * 0.42f), w * 0.04f, Qn.Gold, f * seg(p, 0.2f + 0.35f * i, 0.32f + 0.35f * i), a.t)
            }
            row131(a, "mmkafala3", 2, h * 0.62f, p, f, from = 0.1f, step = 0.35f)
        },
    )
}

// ═════════════ md-sulh ═════════════
internal fun DrawScope.mmsulhScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmsulh", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            val q = easeInOut(seg(p, 0.35f, 0.7f))
            person99(Offset(mix(w * 0.82f, w * 0.6f, q), g), h * 0.13f, Fig131, f)
            person99(Offset(mix(w * 0.18f, w * 0.4f, q), g), h * 0.13f, Fig131, f)
            val zig = Path().apply { moveTo(w * 0.5f, h * 0.36f); lineTo(w * 0.47f, h * 0.44f); lineTo(w * 0.53f, h * 0.5f); lineTo(w * 0.49f, h * 0.6f) }
            drawPath(zig, Qn.Rose, alpha = f * (1f - q), style = Stroke(w * 0.008f, cap = StrokeCap.Round))
            qIcon("hands", Offset(w * 0.5f, g - h * 0.22f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.7f, 0.82f), a.t)
            tagAt(a.tag("mmsulh0", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.7f, h * 0.44f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.7f, h * 0.32f))
            for (k in 0 until 3) person99(Offset(w * (0.18f + 0.1f * k), g), h * 0.1f, Dim131, f * seg(p, 0.2f, 0.35f))
            drawLine(Qn.GoldLight, Offset(w * 0.42f, g - h * 0.05f), Offset(w * 0.58f, g - h * 0.05f), w * 0.005f, alpha = f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("mmsulh1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.4f), w * 0.16f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("check", Offset(w * 0.68f, h * 0.48f), w * 0.04f, Qn.Jade, f * seg(p, 0.3f, 0.42f), a.t)
            qIcon("hands", Offset(w * 0.22f, h * 0.42f), w * 0.05f, Dim131, f * seg(p, 0.5f, 0.6f), a.t)
            qIcon("cross", Offset(w * 0.22f, h * 0.42f), w * 0.06f, Qn.Rose, f * seg(p, 0.6f, 0.7f), a.t)
            tagAt(a.tag("mmsulh2", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 4) {
                val ph = ((a.t * 0.3f + k / 4f) % 1f)
                qIcon("scroll", Offset(w * (0.2f + 0.2f * k), mix(h * 0.3f, h * 0.5f, ph)), w * 0.035f, Dim131, f * (1f - ph), a.t)
            }
            qIcon("hands", Offset(w * 0.5f, h * 0.6f), w * 0.06f, Dim131, f, a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.6f), w * 0.07f, Qn.Rose, f * seg(p, 0.45f, 0.58f), a.t)
            tagAt(a.tag("mmsulh3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ md-wakala ═════════════
internal fun DrawScope.mmwakalaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmwakala", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.78f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.22f, g), h * 0.13f, Fig131, f)
            arrow131(Offset(w * 0.7f, h * 0.5f), Offset(w * 0.3f, h * 0.5f), easeInOut(seg(p, 0.15f, 0.45f)), Qn.GoldLight, f)
            qIcon("hands", Offset(w * 0.22f, g - h * 0.25f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("mmwakala0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.82f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.5f, g), h * 0.13f, Fig131, f)
            car131(Offset(mix(w * 0.7f, w * 0.3f, easeInOut(seg(p, 0.4f, 0.75f))), g - h * 0.03f), w * 0.08f, Qn.Lapis, f)
            tagAt(a.tag("mmwakala1", 0, 0), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
        { p, f ->
            shop132(Offset(w * 0.3f, g), w * 0.12f, f)
            person99(Offset(w * 0.3f, g), h * 0.1f, Dim131, f)
            person99(Offset(mix(w * 0.85f, w * 0.52f, easeInOut(seg(p, 0.1f, 0.4f))), g), h * 0.13f, Fig131, f, walk = a.t * 3f)
            sack131(Offset(mix(w * 0.78f, w * 0.42f, easeInOut(seg(p, 0.1f, 0.55f))), g - h * 0.06f), w * 0.04f, Sack131, f)
            qIcon("check", Offset(w * 0.3f, h * 0.36f), w * 0.05f, Qn.Jade, f * seg(p, 0.6f, 0.72f), a.t)
            tagAt(a.tag("mmwakala2", 0, 0), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.8f, g - h * 0.1f), w * 0.12f, Fig131, f, a.t)
            qIcon("house", Offset(w * 0.2f, g - h * 0.1f), w * 0.12f, Dim131, f, a.t)
            val q = easeInOut(seg(p, 0.15f, 0.6f))
            qIcon("scroll", Offset(mix(w * 0.75f, w * 0.25f, q), mix(h * 0.5f, h * 0.5f, q) - h * 0.15f * kotlin.math.sin(q * 3.1416f)), w * 0.05f, Qn.GoldLight, f, a.t)
            tagAt(a.tag("mmwakala3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-ijara ═════════════
internal fun DrawScope.mmijaraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmijara", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.7f, h * 0.42f), w * 0.12f, Fig131, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("hands", Offset(w * 0.3f, h * 0.42f), w * 0.1f, Fig131, f * seg(p, 0.4f, 0.55f), a.t)
            row131(a, "mmijara0", 2, h * 0.64f, p, f, from = 0.1f, step = 0.35f, ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.74f, g - h * 0.1f), w * 0.15f, Fig131, f, a.t)
            softGlow(Offset(w * 0.74f, g - h * 0.1f), w * 0.08f, Qn.GoldLight, 0.5f * f * seg(p, 0.1f, 0.25f))
            for (k in 0 until 4) drawRect(Sack131, Offset(w * 0.18f + w * 0.07f * (k % 2), g - h * 0.05f * (k / 2 + 1)), Size(w * 0.065f, h * 0.045f), alpha = f * seg(p, 0.4f + 0.08f * k, 0.48f + 0.08f * k))
            person99(Offset(w * 0.38f, g), h * 0.12f, Fig131, f * seg(p, 0.35f, 0.45f))
            tagAt(a.tag("mmijara1", 0, 0), Offset(w * 0.74f, h * 0.3f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("mmijara1", 0, 1), Offset(w * 0.3f, h * 0.42f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            for (i in 0 until 3) {
                val y = h * (0.3f + 0.17f * i)
                qIcon("check", Offset(w * 0.84f, y), w * 0.04f, Qn.Jade, f * seg(p, 0.1f + 0.25f * i, 0.2f + 0.25f * i), a.t)
                tagAt(a.tag("mmijara2", 0, i), Offset(w * 0.5f, y), f * seg(p, 0.1f + 0.25f * i, 0.24f + 0.25f * i), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            plain97(Offset(w * 0.5f, h * 0.2f), w * 0.08f, a, "mmijara3", 0, f * seg(p, 0.05f, 0.2f))
            sirajMunir(Offset(w * 0.75f, g - h * 0.12f), w * 0.025f, f, a.t, named = false)
            person99(Offset(mix(w * 0.55f, w * 0.35f, easeInOut(seg(p, 0.3f, 0.9f))), g), h * 0.12f, Fig131, f, walk = a.t * 3f)
            drawLine(Qn.GoldLight, Offset(w * 0.1f, g + h * 0.02f), Offset(w * 0.9f, g + h * 0.02f), w * 0.004f, alpha = 0.6f * f)
            tagAt(a.tag("mmijara3", 0, 1), Offset(w * 0.5f, h * 0.4f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ md-ariya ═════════════
internal fun DrawScope.mmariyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmariya", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.8f, g), h * 0.13f, Fig131, f)
            person99(Offset(w * 0.2f, g), h * 0.13f, Fig131, f)
            val go = easeInOut(seg(p, 0.1f, 0.4f))
            val back = easeInOut(seg(p, 0.6f, 0.9f))
            qIcon("book", Offset(mix(mix(w * 0.72f, w * 0.28f, go), w * 0.72f, back), g - h * 0.14f), w * 0.045f, Qn.Gold, f, a.t)
            tagAt(a.tag("mmariya0", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            val icons = listOf("cup", "book", "pen")
            for (i in 0 until 3) qIcon(icons[i], Offset(w * (0.5f + (1.5f - i) * 0.21f), h * 0.42f), w * 0.06f, Qn.Gold, f * seg(p, 0.05f + 0.18f * i, 0.17f + 0.18f * i), a.t)
            car131(Offset(w * 0.185f, h * 0.44f), w * 0.06f, Qn.Lapis, f * seg(p, 0.6f, 0.72f))
            row131(a, "mmariya1", 4, h * 0.6f, p, f, from = 0.05f, step = 0.18f)
        },
        { p, f ->
            veil131(f)
            coin131(Offset(w * 0.5f, h * 0.42f), w * 0.06f, f * seg(p, 0.05f, 0.2f))
            qIcon("cross", Offset(w * 0.5f, h * 0.42f), w * 0.1f, Qn.Rose, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("mmariya2", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.5f, g), h * 0.13f, Fig131, f)
            qIcon("hands", Offset(w * 0.5f, g - h * 0.25f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("check", Offset(w * 0.66f, h * 0.36f), w * 0.05f, Qn.Jade, f * seg(p, 0.4f, 0.52f), a.t)
            tagAt(a.tag("mmariya3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ md-luqata ═════════════
internal fun DrawScope.mmluqataScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mmluqata", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            shop132(Offset(w * 0.78f, g), w * 0.1f, f)
            mosque132(Offset(w * 0.22f, g), w * 0.09f, f)
            for (k in 0 until 4) person99(Offset(w * (0.36f + 0.09f * k), g), h * 0.1f, Dim131, f * seg(p, 0.1f, 0.25f))
            person99(Offset(w * 0.5f, g), h * 0.13f, Fig131, f)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.6f + k / 3f) % 1f)
                drawCircle(Qn.GoldLight, w * (0.03f + 0.08f * ph), Offset(w * 0.5f, g - h * 0.18f), alpha = 0.5f * f * (1f - ph) * seg(p, 0.3f, 0.45f), style = Stroke(w * 0.004f))
            }
            tagAt(a.tag("mmluqata0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) coin131(Offset(w * (0.42f + 0.08f * k), h * 0.42f), w * 0.03f, f * seg(p, 0.05f, 0.2f))
            qIcon("lock", Offset(w * 0.75f, h * 0.42f), w * 0.05f, Qn.Gold, f * seg(p, 0.55f, 0.68f), a.t)
            tagAt(a.tag("mmluqata1", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            qIcon("hourglass", Offset(w * 0.5f, h * 0.42f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.42f), w * 0.12f, Qn.Rose, f * seg(p, 0.45f, 0.58f), a.t)
            tagAt(a.tag("mmluqata2", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            mosque132(Offset(w * 0.5f, g), w * 0.14f, f)
            qIcon("cross", Offset(w * 0.5f, h * 0.36f), w * 0.07f, Qn.Rose, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("mmluqata3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}
