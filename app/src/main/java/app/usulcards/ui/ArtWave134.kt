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
 * Wave 134 (2026-11-21): marriage from al-Sharh al-Mumti' (abridged), kept modest: a man and a woman as faceless
 * figures always apart, the contract a scroll between them, the guardian and the witnesses at its side, the dower as
 * coins, the feast as a table of dishes (the sheep only named), khul' as a cloak taken off. 'Uthman ibn Maz'un a plain
 * roundel; the Mothers of the Believers only named in a tag.
 */

private fun DrawScope.couple134(gx: Float, g: Float, gap: Float, tall: Float, alpha: Float) {
    person99(Offset(size.width * (gx + gap), g), tall, Fig131, alpha)
    woman12(Offset(size.width * (gx - gap), g), tall * 0.95f, Fig131, alpha)
}

private fun DrawScope.table134(c: Offset, s: Float, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Color(0xFF6E5432), Offset(c.x - s, c.y - s * 0.12f), Size(s * 2f, s * 0.24f), CornerRadius(s * 0.05f), alpha = alpha)
    for (k in listOf(-0.6f, 0f, 0.6f)) qIcon("cup", Offset(c.x + s * k, c.y - s * 0.3f), s * 0.35f, Qn.GoldLight, alpha, t)
}

private fun DrawScope.grid4_134(a: ArtCtx, kind: String, p: Float, f: Float, y0: Float, dy: Float) {
    val w = size.width
    for (i in 0 until 4) {
        val x = if (i % 2 == 0) w * 0.72f else w * 0.28f
        val y = y0 + dy * (i / 2)
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, 0.08f + 0.2f * i, 0.2f + 0.2f * i), ink = ArtInk.Ivory)
    }
}

// ═════════════ nk-hukm ═════════════
internal fun DrawScope.nkhukmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "nkhukm", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            couple134(0.5f, g, 0.22f, h * 0.14f, f)
            qIcon("scroll", Offset(w * 0.5f, g - h * 0.14f), w * 0.06f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            child12(Offset(w * 0.5f, g), h * 0.08f, Fig131, f * seg(p, 0.6f, 0.75f))
            tagAt(a.tag("nkhukm0", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            row131(a, "nkhukm1", 5, h * 0.45f, p, f, from = 0.1f, step = 0.15f)
        },
        { p, f ->
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig131, f)
            qIcon("check", Offset(w * 0.5f, h * 0.36f), w * 0.06f, Qn.Jade, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("nkhukm2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.34f), w * 0.09f, a, "nkhukm3", 0, f * seg(p, 0.02f, 0.15f))
            tagAt(a.tag("nkhukm3", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ nk-arkan ═════════════
internal fun DrawScope.nkarkanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "nkarkan", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            couple134(0.72f, g, 0.08f, h * 0.12f, f * seg(p, 0.05f, 0.2f))
            qIcon("scroll", Offset(w * 0.28f, g - h * 0.1f), w * 0.07f, Qn.Gold, f * seg(p, 0.4f, 0.55f), a.t)
            row131(a, "nkarkan0", 2, h * 0.3f, p, f, from = 0.1f, step = 0.35f, ink = ArtInk.Gold)
        },
        { p, f ->
            woman12(Offset(w * 0.6f, g), h * 0.13f, Fig131, f)
            qIcon("hourglass", Offset(w * 0.35f, h * 0.5f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            qIcon("scroll", Offset(w * 0.5f, h * 0.3f), w * 0.06f, Dim131, f * seg(p, 0.45f, 0.55f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.3f), w * 0.08f, Qn.Rose, f * seg(p, 0.6f, 0.72f), a.t)
            tagAt(a.tag("nkarkan1", 0, 0), Offset(w * 0.35f, h * 0.64f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.75f, g), h * 0.14f, Fig131, f)
            person99(Offset(w * 0.25f, g), h * 0.13f, Fig131, f)
            voice130(Offset(w * 0.75f, g - h * 0.2f), 0.6f, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("nkarkan2", 0, 0), Offset(w * 0.75f, h * 0.3f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("nkarkan2", 0, 1), Offset(w * 0.4f, h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.3f, g), h * 0.13f, Dim131, f * (1f - 0.5f * seg(p, 0.1f, 0.3f)))
            qIcon("scroll", Offset(w * 0.65f, h * 0.42f), w * 0.06f, Dim131, f * (1f - seg(p, 0.35f, 0.5f)), a.t)
            qIcon("scroll", Offset(w * 0.65f, h * 0.42f), w * 0.06f, Qn.Gold, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("nkarkan3", 0, 0), Offset(w * 0.65f, h * 0.6f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ nk-shurut ═════════════
internal fun DrawScope.nkshurutScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "nkshurut", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("lock", Offset(w * 0.72f, h * 0.38f), w * 0.07f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("pen", Offset(w * 0.28f, h * 0.38f), w * 0.07f, Fig131, f * seg(p, 0.5f, 0.65f), a.t)
            row131(a, "nkshurut0", 2, h * 0.6f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            couple134(0.5f, g, 0.12f, h * 0.12f, f)
            for (k in 0 until 2) {
                val x = w * (0.85f - 0.7f * k)
                person99(Offset(x, g), h * 0.11f, Dim131, f * seg(p, 0.35f, 0.5f))
                qIcon("eye", Offset(x, g - h * 0.22f), w * 0.04f, Qn.GoldLight, f * seg(p, 0.45f, 0.6f), a.t)
            }
            tagAt(a.tag("nkshurut1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            couple134(0.62f, g, 0.1f, h * 0.12f, f)
            qIcon("heart", Offset(w * 0.62f, h * 0.44f), w * 0.05f, Qn.Rose, f * seg(p, 0.1f, 0.25f), a.t)
            person99(Offset(w * 0.22f, g), h * 0.14f, Fig131, f * seg(p, 0.5f, 0.62f))
            qIcon("key", Offset(w * 0.22f, h * 0.44f), w * 0.05f, Qn.Gold, f * seg(p, 0.55f, 0.68f), a.t)
            row131(a, "nkshurut2", 2, h * 0.24f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            box131(Offset(w * 0.72f, h * 0.42f), w * 0.07f, Qn.Jade, f * seg(p, 0.05f, 0.2f))
            qIcon("check", Offset(w * 0.72f, h * 0.26f), w * 0.04f, Qn.Jade, f * seg(p, 0.15f, 0.28f), a.t)
            qIcon("house", Offset(w * 0.28f, h * 0.42f), w * 0.12f, Fig131, f * seg(p, 0.45f, 0.6f), a.t)
            qIcon("heart", Offset(w * 0.28f, h * 0.26f), w * 0.04f, Qn.Rose, f * seg(p, 0.55f, 0.68f), a.t)
            row131(a, "nkshurut3", 2, h * 0.64f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ nk-muharramat ═════════════
internal fun DrawScope.nkmuharramatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "nkmuharramat", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 18) {
                val x = w * (0.14f + 0.08f * (k % 10))
                val y = h * (0.3f + 0.12f * (k / 10))
                val bad = k % 6 == 2
                drawCircle(if (bad) Dim131 else Qn.Jade, w * 0.018f, Offset(x, y), alpha = f * seg(p, 0.05f + 0.02f * k, 0.2f + 0.02f * k))
                if (bad) qIcon("cross", Offset(x, y), w * 0.03f, Qn.Rose, f * seg(p, 0.5f, 0.6f), a.t)
            }
            tagAt(a.tag("nkmuharramat0", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            drawCircle(Qn.Gold, w * 0.05f, Offset(w * 0.66f, h * 0.42f), alpha = f * seg(p, 0.1f, 0.25f), style = Stroke(w * 0.008f))
            drawCircle(Qn.Gold, w * 0.05f, Offset(w * 0.76f, h * 0.42f), alpha = f * seg(p, 0.1f, 0.25f), style = Stroke(w * 0.008f))
            qIcon("hourglass", Offset(w * 0.29f, h * 0.42f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            row131(a, "nkmuharramat1", 2, h * 0.62f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            row131(a, "nkmuharramat2", 5, h * 0.45f, p, f, from = 0.1f, step = 0.15f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 5) softGlow(Offset(w * (0.18f + 0.16f * k), h * 0.48f), w * 0.04f, Qn.GoldLight, 0.6f * f * seg(p, 0.1f + 0.08f * k, 0.25f + 0.08f * k))
            tagAt(a.tag("nkmuharramat3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("nkmuharramat3", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ nk-sadaq ═════════════
internal fun DrawScope.nksadaqScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "nksadaq", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            couple134(0.5f, g, 0.25f, h * 0.13f, f)
            for (k in 0 until 3) coin131(Offset(mix(w * 0.68f, w * 0.32f, easeInOut(seg(p, 0.15f + 0.05f * k, 0.45f + 0.05f * k))), g - h * (0.12f + 0.03f * k)), w * 0.02f, f)
            tagAt(a.tag("nksadaq0", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("heart", Offset(w * 0.6f, h * 0.42f), w * 0.1f, Qn.Rose, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 3) coin131(Offset(w * (0.36f - 0.04f * k), h * (0.46f - 0.04f * k)), w * 0.025f, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("nksadaq1", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig131, f)
            person99(Offset(w * 0.3f, g), h * 0.12f, Dim131, f, lean = 0.1f)
            coin131(Offset(mix(w * 0.64f, w * 0.36f, easeInOut(seg(p, 0.2f, 0.5f))), g - h * 0.12f), w * 0.022f, f)
            softGlow(Offset(w * 0.72f, g - h * 0.1f), w * 0.07f, Qn.GoldLight, 0.6f * f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("nksadaq2", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.4f), w * 0.14f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("nksadaq3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ nk-walima ═════════════
internal fun DrawScope.nkwalimaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "nkwalima", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            table134(Offset(w * 0.5f, g - h * 0.08f), w * 0.2f, f * seg(p, 0.1f, 0.25f), a.t)
            for (k in 0 until 4) person99(Offset(w * (0.14f + 0.24f * k + (if (k > 1) 0.0f else 0f)), g), h * 0.11f, Dim131, f * seg(p, 0.2f + 0.06f * k, 0.35f + 0.06f * k))
            tagAt(a.tag("nkwalima0", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            grid4_134(a, "nkwalima1", p, f, h * 0.36f, h * 0.2f)
        },
        { p, f ->
            person99(Offset(w * 0.5f, g), h * 0.15f, Fig131, f)
            softGlow(Offset(w * 0.5f, g - h * 0.1f), w * 0.1f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f, 0.4f))
            tagAt(a.tag("nkwalima2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.5f, g - h * 0.12f), w * 0.2f, Dim131, f, a.t)
            table134(Offset(w * 0.5f, g + h * 0.02f), w * 0.14f, f * 0.6f, a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.34f), w * 0.07f, Qn.Rose, f * seg(p, 0.45f, 0.58f), a.t)
            tagAt(a.tag("nkwalima3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ nk-khul ═════════════
internal fun DrawScope.nkkhulScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "nkkhul", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            person99(Offset(w * 0.5f, g), h * 0.16f, Fig131, f)
            val off = easeInOut(seg(p, 0.3f, 0.7f))
            val cloak = Path().apply {
                val cx = w * (0.5f + 0.25f * off)
                val cy = g - h * 0.12f + h * 0.06f * off
                moveTo(cx - w * 0.08f, cy - h * 0.1f); lineTo(cx + w * 0.08f, cy - h * 0.1f); lineTo(cx + w * 0.11f, cy + h * 0.1f); lineTo(cx - w * 0.11f, cy + h * 0.1f); close()
            }
            drawPath(cloak, Qn.Lapis, alpha = 0.85f * f)
            tagAt(a.tag("nkkhul0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.68f, h * 0.42f), w * 0.07f, Fig131, f * seg(p, 0.05f, 0.2f), a.t)
            coin131(Offset(w * 0.32f, h * 0.42f), w * 0.05f, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("nkkhul1", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.7f, h * 0.42f), w * 0.07f, Dim131, f * seg(p, 0.05f, 0.2f), a.t)
            drawLine(Qn.Rose, Offset(w * 0.64f, h * 0.36f), Offset(w * 0.76f, h * 0.48f), w * 0.006f, alpha = f * seg(p, 0.2f, 0.3f), cap = StrokeCap.Round)
            qIcon("scroll", Offset(w * 0.3f, h * 0.42f), w * 0.07f, Qn.Gold, f * seg(p, 0.5f, 0.65f), a.t)
            row131(a, "nkkhul2", 2, h * 0.62f, p, f, from = 0.15f, step = 0.4f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("pen", Offset(w * 0.5f, h * 0.3f), w * 0.07f, Fig131, f * seg(p, 0.05f, 0.2f), a.t)
            for (i in 0 until 2) qIcon(if (i == 0) "check" else "cross", Offset(w * (0.71f - 0.42f * i), h * 0.5f), w * 0.05f, if (i == 0) Qn.Jade else Qn.Rose, f * seg(p, 0.2f + 0.35f * i, 0.32f + 0.35f * i), a.t)
            row131(a, "nkkhul3", 2, h * 0.64f, p, f, from = 0.15f, step = 0.35f)
        },
    )
}
