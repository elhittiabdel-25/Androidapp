package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 133 (2026-11-21): inheritance from al-Sharh al-Mumti' (abridged), drawn as the book counts it: the estate is a
 * bar of equal shares, each heir's share a run of blocks in his colour (gold, jade, rose, lapis), heirs as small
 * faceless figures or name tags, blocked heirs dimmed and crossed. 'Umar a companion roundel. Numbers are spoken
 * and shown as words in the tags, never as Arabic-Indic digits.
 */

private val Heir1 = Qn.Gold
private val Heir2 = Qn.Jade
private val Heir3 = Qn.Rose
private val Heir4 = Qn.Lapis

/** The estate as [frame] equal blocks; [units] colours the first blocks (more than [frame] spills past the frame). */
private fun DrawScope.bar133(y: Float, frame: Int, units: List<Color>, reveal: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val w = size.width
    val n = maxOf(frame, units.size)
    val bw = w * 0.8f / n
    val x0 = w * 0.9f
    drawRoundRect(Fig131, Offset(x0 - bw * frame - w * 0.006f, y - bw * 0.55f), Size(bw * frame + w * 0.012f, bw * 1.1f), CornerRadius(w * 0.01f), alpha = 0.6f * alpha, style = Stroke(w * 0.004f))
    val shown = (units.size * reveal).toInt()
    for (i in 0 until shown) {
        val x = x0 - bw * (i + 1)
        drawRoundRect(units[i], Offset(x + bw * 0.08f, y - bw * 0.42f), Size(bw * 0.84f, bw * 0.84f), CornerRadius(bw * 0.12f), alpha = alpha)
    }
}

/** An heir: a small faceless figure, dimmed and crossed when blocked. */
private fun DrawScope.heir133(c: Offset, tall: Float, alpha: Float, blocked: Float = 0f, t: Float = 0f) {
    person99(c, tall, if (blocked > 0f) Dim131 else Fig131, alpha * (1f - 0.4f * blocked))
    if (blocked > 0f) qIcon("cross", Offset(c.x, c.y - tall * 1.2f), tall * 0.5f, Qn.Rose, alpha * blocked, t)
}

// ═════════════ fr-ilm ═════════════
internal fun DrawScope.frilmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "frilm", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.71f, h * 0.4f), w * 0.08f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("scale", Offset(w * 0.29f, h * 0.4f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.45f, 0.6f), a.t)
            row131(a, "frilm0", 2, h * 0.6f, p, f, from = 0.1f, step = 0.35f, ink = ArtInk.Gold)
        },
        { p, f ->
            drawCircle(Dim131, w * 0.04f, Offset(w * 0.5f, h * 0.24f), alpha = f)
            drawLine(Dim131, Offset(w * 0.5f, h * 0.28f), Offset(w * 0.3f, g - h * 0.18f), w * 0.004f, alpha = f * seg(p, 0.1f, 0.2f))
            drawLine(Dim131, Offset(w * 0.5f, h * 0.28f), Offset(w * 0.7f, g - h * 0.18f), w * 0.004f, alpha = f * seg(p, 0.1f, 0.2f))
            woman12(Offset(w * 0.7f, g), h * 0.12f, Fig131, f * seg(p, 0.15f, 0.3f))
            woman12(Offset(w * 0.3f, g), h * 0.12f, Fig131, f * seg(p, 0.25f, 0.4f))
            tagAt(a.tag("frilm1", 0, 0), Offset(w * 0.7f, h * 0.48f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
            tagAt(a.tag("frilm1", 0, 1), Offset(w * 0.3f, h * 0.48f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            bar133(h * 0.42f, 2, listOf(Heir1, Heir2), seg(p, 0.2f, 0.7f), f)
            row131(a, "frilm2", 2, h * 0.6f, p, f, from = 0.3f, step = 0.3f)
        },
        { p, f ->
            veil131(f)
            val icons = listOf("chain", "heart", "key")
            for (i in 0 until 3) qIcon(icons[i], Offset(w * (0.5f + (1 - i) * 0.28f), h * 0.4f), w * 0.07f, Qn.Gold, f * seg(p, 0.1f + 0.25f * i, 0.22f + 0.25f * i), a.t)
            row131(a, "frilm3", 3, h * 0.58f, p, f, from = 0.1f, step = 0.25f)
        },
    )
}

// ═════════════ fr-asaba ═════════════
internal fun DrawScope.frasabaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "frasaba", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            val rest = seg(p, 0.4f, 0.8f)
            bar133(h * 0.42f, 6, List(3) { Heir2 } + List((3 * rest).toInt()) { Heir1 }, 1f, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("frasaba0", 0, 0), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            row131(a, "frasaba1", 3, h * 0.45f, p, f, from = 0.15f, step = 0.22f, ink = ArtInk.Gold)
        },
        { p, f ->
            heir133(Offset(w * 0.62f, g), h * 0.13f, f)
            woman12(Offset(w * 0.38f, g), h * 0.12f, Fig131, f * seg(p, 0.2f, 0.35f))
            drawLine(Qn.GoldLight, Offset(w * 0.45f, g - h * 0.08f), Offset(w * 0.56f, g - h * 0.08f), w * 0.006f, alpha = f * seg(p, 0.4f, 0.55f), cap = StrokeCap.Round)
            tagAt(a.tag("frasaba2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            heir133(Offset(w * 0.7f, g), h * 0.14f, f)
            softGlow(Offset(w * 0.7f, g - h * 0.08f), w * 0.08f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.45f))
            heir133(Offset(w * 0.3f, g), h * 0.13f, f, blocked = seg(p, 0.45f, 0.6f), t = a.t)
            row131(a, "frasaba3", 2, h * 0.3f, p, f, from = 0.1f, step = 0.35f)
        },
    )
}

// ═════════════ fr-ikhwa ═════════════
internal fun DrawScope.frikhwaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "frikhwa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            bar133(h * 0.32f, 6, listOf(Heir3), 1f, f * seg(p, 0.05f, 0.2f))
            bar133(h * 0.56f, 6, listOf(Heir3, Heir3), 1f, f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("frikhwa0", 0, 0), Offset(w * 0.3f, h * 0.32f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("frikhwa0", 0, 1), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            heir133(Offset(w * 0.65f, g), h * 0.13f, f)
            woman12(Offset(w * 0.35f, g), h * 0.13f, Fig131, f)
            for (k in 0 until 2) drawLine(Qn.GoldLight, Offset(w * 0.46f, g - h * (0.1f + 0.03f * k)), Offset(w * 0.54f, g - h * (0.1f + 0.03f * k)), w * 0.006f, alpha = f * seg(p, 0.3f, 0.45f))
            tagAt(a.tag("frikhwa1", 0, 0), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (i in 0 until 2) {
                val x = w * (0.71f - 0.42f * i)
                qIcon(if (i == 0) "child" else "people", Offset(x, h * 0.4f), w * 0.07f, Dim131, f * seg(p, 0.1f + 0.4f * i, 0.22f + 0.4f * i), a.t)
                qIcon("cross", Offset(x, h * 0.4f), w * 0.09f, Qn.Rose, f * seg(p, 0.2f + 0.4f * i, 0.32f + 0.4f * i), a.t)
            }
            row131(a, "frikhwa2", 2, h * 0.6f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            heir133(Offset(w * 0.7f, g), h * 0.14f, f)
            softGlow(Offset(w * 0.7f, g - h * 0.08f), w * 0.08f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.45f))
            heir133(Offset(w * 0.3f, g), h * 0.13f, f, blocked = seg(p, 0.45f, 0.6f), t = a.t)
            tagAt(a.tag("frikhwa3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ fr-hajb ═════════════
internal fun DrawScope.frhajbScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "frhajb", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.5f, h * 0.4f), w * 0.09f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("lock", Offset(w * 0.5f, h * 0.4f), w * 0.05f, Dim131, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("frhajb0", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            for (i in 0 until 3) {
                val c = Offset(w * (0.78f - 0.28f * i), h * 0.42f)
                drawCircle(if (i == 2) Dim131 else Qn.Gold, w * 0.04f, c, alpha = f * seg(p, 0.1f + 0.15f * i, 0.22f + 0.15f * i))
                if (i > 0) drawLine(Fig131, Offset(c.x + w * 0.24f, c.y), Offset(c.x + w * 0.05f, c.y), w * 0.004f, alpha = f * seg(p, 0.1f + 0.15f * i, 0.22f + 0.15f * i))
            }
            drawRect(Qn.GoldLight, Offset(w * 0.37f, h * 0.32f), Size(w * 0.012f, h * 0.2f), alpha = f * seg(p, 0.55f, 0.68f))
            tagAt(a.tag("frhajb1", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            woman12(Offset(w * 0.75f, g), h * 0.14f, Fig131, f)
            softGlow(Offset(w * 0.75f, g - h * 0.08f), w * 0.08f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f, 0.35f))
            for (k in 0 until 2) {
                val x = w * (0.42f - 0.2f * k)
                woman12(Offset(x, g), h * 0.12f, Dim131, f * 0.7f)
                qIcon("cross", Offset(x, g - h * 0.18f), w * 0.04f, Qn.Rose, f * seg(p, 0.5f + 0.1f * k, 0.6f + 0.1f * k), a.t)
            }
            tagAt(a.tag("frhajb2", 0, 0), Offset(w * 0.75f, h * 0.36f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("frhajb2", 0, 1), Offset(w * 0.32f, h * 0.36f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (i in 0 until 3) heir133(Offset(w * (0.82f - 0.18f * i), g), h * 0.12f, f * seg(p, 0.1f + 0.15f * i, 0.22f + 0.15f * i))
            heir133(Offset(w * 0.2f, g), h * 0.12f, f, blocked = seg(p, 0.6f, 0.75f), t = a.t)
            row131(a, "frhajb3", 3, h * 0.3f, p, f, from = 0.1f, step = 0.15f, ink = ArtInk.Gold)
        },
    )
}

// ═════════════ fr-umariyya ═════════════
internal fun DrawScope.frumariyyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "frumariyya", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.5f, h * 0.36f), w * 0.1f, a, "frumariyya0", 0, 1, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("frumariyya0", 0, 1), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            bar133(h * 0.42f, 6, listOf(Heir1, Heir1, Heir1, Heir3, Heir2, Heir2), seg(p, 0.15f, 0.85f), f)
            row131(a, "frumariyya1", 3, h * 0.62f, p, f, from = 0.15f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            bar133(h * 0.42f, 4, listOf(Heir4, Heir3, Heir2, Heir2), seg(p, 0.15f, 0.85f), f)
            row131(a, "frumariyya2", 3, h * 0.62f, p, f, from = 0.15f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.5f, h * 0.36f), w * 0.1f, a, "frumariyya0", 0, 1, f)
            tagAt(a.tag("frumariyya3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ fr-awl ═════════════
internal fun DrawScope.frawlScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "frawl", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            bar133(h * 0.26f, 6, List(4) { Heir2 }, 1f, f * seg(p, 0.05f, 0.18f))
            bar133(h * 0.44f, 6, List(6) { Heir1 }, 1f, f * seg(p, 0.3f, 0.43f))
            bar133(h * 0.62f, 6, List(8) { Heir3 }, 1f, f * seg(p, 0.55f, 0.68f))
            for (i in 0 until 3) tagAt(a.tag("frawl0", 0, i), Offset(w * 0.5f, h * (0.18f + 0.18f * i)), f * seg(p, 0.05f + 0.25f * i, 0.18f + 0.25f * i), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            bar133(h * 0.42f, 6, listOf(Heir1, Heir1, Heir1, Heir3, Heir2, Heir2), seg(p, 0.15f, 0.85f), f)
            row131(a, "frawl1", 3, h * 0.62f, p, f, from = 0.15f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            bar133(h * 0.42f, 6, listOf(Heir2, Heir2, Heir2, Heir2, Heir3, Heir1, Heir1, Heir1), seg(p, 0.1f, 0.7f), f)
            tagAt(a.tag("frawl2", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            bar133(h * 0.42f, 6, listOf(Heir1, Heir1, Heir1, Heir3, Heir4, Heir4), seg(p, 0.1f, 0.75f), f)
            tagAt(a.tag("frawl3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.75f, 0.88f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ fr-radd ═════════════
internal fun DrawScope.frraddScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "frradd", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            bar133(h * 0.32f, 6, List(8) { Heir3 }, 1f, f * seg(p, 0.05f, 0.2f))
            bar133(h * 0.56f, 6, List(4) { Heir2 }, 1f, f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("frradd0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("frradd0", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            heir133(Offset(w * 0.78f, g), h * 0.13f, f)
            woman12(Offset(w * 0.5f, g), h * 0.13f, Fig131, f)
            heir133(Offset(w * 0.22f, g), h * 0.13f, f)
            softGlow(Offset(w * 0.78f, g - h * 0.08f), w * 0.07f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.45f))
            softGlow(Offset(w * 0.5f, g - h * 0.08f), w * 0.07f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.45f))
            qIcon("cross", Offset(w * 0.22f, g - h * 0.2f), w * 0.05f, Qn.Rose, f * seg(p, 0.6f, 0.72f), a.t)
            tagAt(a.tag("frradd1", 0, 0), Offset(w * 0.3f, h * 0.3f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            woman12(Offset(w * 0.7f, g), h * 0.14f, Fig131, f)
            softGlow(Offset(w * 0.7f, g - h * 0.08f), w * 0.09f, Qn.GoldLight, 0.7f * f * seg(p, 0.3f, 0.5f))
            heir133(Offset(w * 0.3f, g), h * 0.13f, f, blocked = seg(p, 0.45f, 0.6f), t = a.t)
            tagAt(a.tag("frradd2", 0, 0), Offset(w * 0.6f, h * 0.28f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.5f, g - h * 0.12f), w * 0.18f, Fig131, f, a.t)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.4f + k / 3f) % 1f)
                coin131(Offset(mix(w * 0.9f, w * 0.56f, ph), g - h * 0.12f), w * 0.02f, f * (1f - ph) * seg(p, 0.2f, 0.35f))
            }
            tagAt(a.tag("frradd3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}
