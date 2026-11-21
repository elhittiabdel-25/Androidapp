package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 135 (2026-11-21): divorce from al-Sharh al-Mumti' (abridged). The bond is a chain that opens, the three
 * divorces three tokens, anger a flame of three sizes, a word counted or not a scroll lit or dimmed. Husband and wife
 * are faceless figures kept apart; 'Umar a companion roundel; al-Zubayr only named.
 */

private fun DrawScope.token135(c: Offset, r: Float, on: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(Dim131, r, c, alpha = alpha * 0.6f)
    drawCircle(Qn.Lapis, r, c, alpha = alpha * on)
    drawCircle(Qn.GoldLight, r, c, alpha = alpha, style = Stroke(r * 0.15f))
}

private fun DrawScope.cut135(c: Offset, s: Float, q: Float, alpha: Float) {
    if (alpha <= 0.01f || q <= 0f) return
    drawLine(Qn.Rose, Offset(c.x - s, c.y - s), Offset(c.x - s + 2f * s * q, c.y - s + 2f * s * q), size.width * 0.007f, alpha = alpha, cap = StrokeCap.Round)
}

private fun DrawScope.apart135(g: Float, h: Float, f: Float, gap: Float = 0.28f) {
    person99(Offset(size.width * (0.5f + gap), g), h * 0.14f, Fig131, f)
    woman12(Offset(size.width * (0.5f - gap), g), h * 0.13f, Fig131, f)
}

// ═════════════ tl-hukm ═════════════
internal fun DrawScope.tlhukmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "tlhukm", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("chain", Offset(w * 0.72f, h * 0.4f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            cut135(Offset(w * 0.72f, h * 0.4f), w * 0.08f, seg(p, 0.2f, 0.35f), f)
            qIcon("chain", Offset(w * 0.28f, h * 0.4f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.45f, 0.6f), a.t)
            cut135(Offset(w * 0.28f, h * 0.4f), w * 0.04f, seg(p, 0.6f, 0.72f), f)
            row131(a, "tlhukm0", 2, h * 0.62f, p, f, from = 0.15f, step = 0.45f)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.5f, g - h * 0.14f), w * 0.24f, Dim131, f, a.t)
            qIcon("heart", Offset(w * 0.5f, h * 0.34f), w * 0.08f, Qn.Rose, f * seg(p, 0.1f, 0.25f), a.t)
            drawLine(Color(0xFF2A2140), Offset(w * 0.5f, h * 0.3f), Offset(w * 0.49f, h * 0.38f), w * 0.008f, alpha = f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("tlhukm1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            apart135(g, h, f)
            voice130(Offset(w * 0.68f, g - h * 0.2f), 0.4f, f * seg(p, 0.3f, 0.45f), a.t)
            voice130(Offset(w * 0.32f, g - h * 0.2f), 0.4f, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("tlhukm2", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) {
                val gone = if (k == 0) seg(p, 0.4f, 0.5f) else if (k == 1) seg(p, 0.65f, 0.75f) else 0f
                token135(Offset(w * (0.7f - 0.2f * k), h * 0.42f), w * 0.06f, 1f - gone, f * seg(p, 0.05f + 0.05f * k, 0.2f + 0.05f * k))
            }
            tagAt(a.tag("tlhukm3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tl-bida ═════════════
internal fun DrawScope.tlbidaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tlbida", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("hourglass", Offset(w * 0.72f, h * 0.4f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            for (k in 0 until 3) token135(Offset(w * (0.2f + 0.08f * k), h * 0.4f), w * 0.03f, 1f, f * seg(p, 0.5f, 0.62f))
            qIcon("cross", Offset(w * 0.5f, h * 0.24f), w * 0.06f, Qn.Rose, f * seg(p, 0.02f, 0.12f), a.t)
            row131(a, "tlbida0", 2, h * 0.62f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            qIcon("moon", Offset(w * 0.72f, h * 0.4f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("hourglass", Offset(w * 0.28f, h * 0.4f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.5f, 0.62f), a.t)
            for (i in 0 until 2) qIcon("cross", Offset(w * (0.72f - 0.44f * i), h * 0.4f), w * 0.11f, Qn.Rose, 0.8f * f * seg(p, 0.3f + 0.4f * i, 0.4f + 0.4f * i), a.t)
            row131(a, "tlbida1", 2, h * 0.62f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) {
                val gone = if (k == 0) 0f else seg(p, 0.55f, 0.7f)
                token135(Offset(w * (0.62f - 0.12f * k), h * 0.38f), w * 0.05f, 1f, f * seg(p, 0.05f + 0.05f * k, 0.18f + 0.05f * k) * (1f - 0.8f * gone))
            }
            row131(a, "tlbida2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.5f, ink = ArtInk.Gold)
        },
        { p, f ->
            woman12(Offset(w * 0.5f, h * 0.8f), h * 0.14f, Fig131, f)
            softGlow(Offset(w * 0.5f, h * 0.68f), w * 0.07f, Qn.GoldLight, 0.5f * f * seg(p, 0.1f, 0.3f))
            qIcon("check", Offset(w * 0.5f, h * 0.36f), w * 0.06f, Qn.Jade, f * seg(p, 0.4f, 0.52f), a.t)
            tagAt(a.tag("tlbida3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tl-ikrah ═════════════
internal fun DrawScope.tlikrahScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "tlikrah", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.62f, g), h * 0.14f, Fig131, f)
            qIcon("chain", Offset(w * 0.62f, g - h * 0.08f), w * 0.07f, Dim131, f * seg(p, 0.05f, 0.2f), a.t)
            person99(Offset(w * 0.3f, g), h * 0.17f, Dim131, f, lean = -0.1f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.36f), w * 0.06f, Dim131, f * seg(p, 0.4f, 0.5f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.36f), w * 0.08f, Qn.Rose, f * seg(p, 0.55f, 0.65f), a.t)
            tagAt(a.tag("tlikrah0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("mountain", Offset(w * 0.62f, h * 0.5f), w * 0.4f, Color(0xFF5A4A3A), f, a.t)
            val top = Offset(w * 0.62f, h * 0.28f)
            val down = h * 0.62f
            drawLine(Sack131, top, Offset(w * 0.62f, down), w * 0.004f, alpha = f * seg(p, 0.02f, 0.12f))
            person99(Offset(w * 0.62f, down + h * 0.1f), h * 0.08f, Fig131, f * seg(p, 0.05f, 0.15f))
            woman12(Offset(w * 0.72f, h * 0.28f), h * 0.07f, Dim131, f * seg(p, 0.1f, 0.2f))
            close97(Offset(w * 0.22f, h * 0.36f), w * 0.09f, a, "tlikrah1", 0, 1, f * seg(p, 0.5f, 0.62f))
            tagAt(a.tag("tlikrah1", 0, 1), Offset(w * 0.24f, h * 0.6f), f * seg(p, 0.7f, 0.82f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("hands", Offset(w * 0.72f, h * 0.4f), w * 0.1f, Fig131, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("eye", Offset(w * 0.28f, h * 0.4f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "tlikrah2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            for (k in 0 until 14) drawCircle(Color.White, w * 0.005f, Offset(w * ((k * 0.137f + a.t * 0.03f) % 1f), h * (0.1f + (k * 0.071f + a.t * 0.05f) % 0.6f)), alpha = 0.6f * f)
            person99(Offset(w * 0.62f, g), h * 0.14f, Fig131, f)
            drawRoundRect(Qn.Lapis, Offset(w * 0.57f, g - h * 0.12f), androidx.compose.ui.geometry.Size(w * 0.1f, h * 0.1f), androidx.compose.ui.geometry.CornerRadius(w * 0.02f), alpha = 0.85f * f)
            for (k in 0 until 2) coin131(Offset(w * (0.3f + 0.06f * k), g - h * 0.06f), w * 0.022f, f * seg(p, 0.15f, 0.3f))
            row131(a, "tlikrah3", 2, h * 0.26f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ tl-ghadab ═════════════
internal fun DrawScope.tlghadabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tlghadab", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) qIcon("flame", Offset(w * (0.75f - 0.25f * k), h * 0.44f), w * (0.12f - 0.03f * k), Qn.Rose, f * seg(p, 0.1f + 0.15f * k, 0.25f + 0.15f * k), a.t)
            tagAt(a.tag("tlghadab0", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("flame", Offset(w * 0.72f, h * 0.3f), w * 0.13f, Qn.Rose, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("scroll", Offset(w * 0.72f, h * 0.48f), w * 0.06f, Dim131, f * seg(p, 0.2f, 0.32f), a.t)
            qIcon("flame", Offset(w * 0.28f, h * 0.32f), w * 0.06f, Qn.Rose, f * seg(p, 0.5f, 0.62f), a.t)
            qIcon("scroll", Offset(w * 0.28f, h * 0.48f), w * 0.06f, Qn.Gold, f * seg(p, 0.62f, 0.74f), a.t)
            row131(a, "tlghadab1", 2, h * 0.66f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            person99(Offset(w * 0.5f, h * 0.8f), h * 0.15f, Fig131, f)
            qIcon("flame", Offset(w * 0.5f, h * 0.46f), w * 0.1f, Qn.Rose, f * seg(p, 0.1f, 0.3f), a.t)
            qIcon("chain", Offset(w * 0.5f, h * 0.7f), w * 0.08f, Dim131, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("tlghadab2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.38f), w * 0.14f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 4) qIcon("drop", Offset(w * (0.36f + 0.09f * k), h * (0.54f + 0.02f * (k % 2))), w * 0.03f, Qn.Lapis, f * seg(p, 0.2f + 0.05f * k, 0.32f + 0.05f * k), a.t)
            tagAt(a.tag("tlghadab3", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tl-sarih ═════════════
internal fun DrawScope.tlsarihScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tlsarih", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            arrow131(Offset(w * 0.62f, h * 0.42f), Offset(w * 0.86f, h * 0.42f), seg(p, 0.1f, 0.3f), Qn.Gold, f)
            arrow131(Offset(w * 0.14f, h * 0.42f), Offset(w * 0.38f, h * 0.3f), seg(p, 0.5f, 0.68f), Fig131, f)
            arrow131(Offset(w * 0.14f, h * 0.42f), Offset(w * 0.38f, h * 0.54f), seg(p, 0.5f, 0.68f), Fig131, f)
            row131(a, "tlsarih0", 2, h * 0.66f, p, f, from = 0.12f, step = 0.42f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("lips", Offset(w * 0.5f, h * 0.32f), w * 0.1f, Fig131, f * seg(p, 0.02f, 0.15f), a.t)
            row131(a, "tlsarih1", 3, h * 0.54f, p, f, from = 0.12f, step = 0.22f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            val w0 = size.width
            for (i in 0 until 4) {
                val x = if (i % 2 == 0) w0 * 0.72f else w0 * 0.28f
                tagAt(a.tag("tlsarih2", 0, i), Offset(x, h * 0.36f + h * 0.2f * (i / 2)), f * seg(p, 0.08f + 0.2f * i, 0.2f + 0.2f * i), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.42f), w * 0.1f, Qn.GoldLight, 0.8f * f * seg(p, 0.05f, 0.2f))
            val drape = seg(p, 0.25f, 0.55f)
            val cloak = Path().apply {
                val cy = mix(h * 0.18f, h * 0.36f, easeInOut(drape))
                moveTo(w * 0.42f, cy); lineTo(w * 0.58f, cy); lineTo(w * 0.62f, cy + h * 0.16f); lineTo(w * 0.38f, cy + h * 0.16f); close()
            }
            drawPath(cloak, Qn.Lapis, alpha = 0.75f * f * seg(p, 0.2f, 0.3f))
            tagAt(a.tag("tlsarih3", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tl-hazl ═════════════
internal fun DrawScope.tlhazlScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "tlhazl", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig131, f)
            person99(Offset(w * 0.28f, g), h * 0.14f, Fig131, f, lean = 0.08f)
            for (i in 0 until 2) qIcon("check", Offset(w * (0.72f - 0.44f * i), h * 0.44f), w * 0.05f, Qn.Jade, f * seg(p, 0.3f + 0.35f * i, 0.42f + 0.35f * i), a.t)
            row131(a, "tlhazl0", 2, h * 0.28f, p, f, from = 0.1f, step = 0.4f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("lips", Offset(w * 0.78f, h * 0.38f), w * 0.07f, Fig131, f * seg(p, 0.1f, 0.22f), a.t)
            qIcon("scale", Offset(w * 0.64f, h * 0.38f), w * 0.07f, Qn.Gold, f * seg(p, 0.15f, 0.28f), a.t)
            qIcon("lips", Offset(w * 0.32f, h * 0.38f), w * 0.07f, Fig131, f * seg(p, 0.5f, 0.62f), a.t)
            qIcon("scale", Offset(w * 0.18f, h * 0.38f), w * 0.07f, Dim131, f * seg(p, 0.55f, 0.68f) * 0.5f, a.t)
            row131(a, "tlhazl1", 2, h * 0.6f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) qIcon("sun", Offset(w * (0.7f - 0.2f * k), h * 0.36f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.1f + 0.1f * k, 0.22f + 0.1f * k), a.t)
            qIcon("scroll", Offset(w * 0.5f, h * 0.52f), w * 0.06f, Dim131, f * seg(p, 0.45f, 0.55f), a.t)
            tagAt(a.tag("tlhazl2", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("chain", Offset(w * 0.72f, h * 0.4f), w * 0.1f, Fig131, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("scale", Offset(w * 0.28f, h * 0.4f), w * 0.1f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "tlhazl3", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ tl-raja ═════════════
internal fun DrawScope.tlrajaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "tlraja", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            val back = easeInOut(seg(p, 0.35f, 0.75f))
            person99(Offset(w * 0.75f, g), h * 0.14f, Fig131, f)
            woman12(Offset(w * mix(0.2f, 0.45f, back), g), h * 0.13f, Fig131, f, walk = a.t * back * (1f - back))
            qIcon("hourglass", Offset(w * 0.18f, h * 0.36f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            voice130(Offset(w * 0.75f, g - h * 0.2f), 0.5f, f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("tlraja0", 0, 0), Offset(w * 0.6f, h * 0.26f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            row131(a, "tlraja1", 5, h * 0.45f, p, f, from = 0.1f, step = 0.15f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) coin131(Offset(w * (0.68f + 0.04f * k), h * (0.42f - 0.03f * k)), w * 0.03f, f * seg(p, 0.1f, 0.25f))
            qIcon("scroll", Offset(w * 0.3f, h * 0.4f), w * 0.07f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "tlraja2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            apart135(g, h, f, gap = 0.18f)
            qIcon("chain", Offset(w * 0.5f, g - h * 0.08f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("tlraja3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
    )
}
