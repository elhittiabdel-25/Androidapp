package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 148 (2026-11-26): occasions of revelation from al-Wadi'i, al-Ma'ida to al-Anfal: the wine poured in the lanes,
 * questions that bring hardship, the silver cup of the man of Sahm, the poor companions, the dead meat, dress at every
 * mosque and the spoils of Badr. The Prophet ﷺ is never drawn; companions are plain roundels; people are faceless; no
 * animals; the tawaf of the Jahiliyya is only the Ka'ba and a ring of lights.
 */

private val Wine148 = Color(0xFF7A2E3A)
private val Silver148 = Color(0xFFC8CCD6)

private fun DrawScope.cup148(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(Silver148, Offset(c.x - s * 0.4f, c.y - s * 0.5f), Size(s * 0.8f, s * 0.7f), alpha = alpha)
    for (k in 0 until 3) drawRect(Qn.Gold, Offset(c.x - s * 0.3f + s * 0.25f * k, c.y - s * 0.45f), Size(s * 0.08f, s * 0.6f), alpha = alpha)
    drawRect(Silver148, Offset(c.x - s * 0.08f, c.y + s * 0.2f), Size(s * 0.16f, s * 0.25f), alpha = alpha)
    drawRect(Silver148, Offset(c.x - s * 0.3f, c.y + s * 0.42f), Size(s * 0.6f, s * 0.08f), alpha = alpha)
}

// ═════════════ as-khamr: it ran in the lanes of Madina ═════════════
internal fun DrawScope.askhamrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askhamr", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 1..3) drawCircle(Qn.GoldLight, w * (0.05f + 0.06f * k), Offset(w * 0.5f, h * 0.36f), alpha = 0.5f * f * seg(p, 0.05f * k, 0.1f + 0.1f * k), style = Stroke(w * 0.004f))
            tagAt(a.tag("askhamr0", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            plain97(Offset(w * 0.5f, h * 0.66f), w * 0.08f, a, "askhamr0", 1, f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 4) qIcon("house", Offset(w * (0.2f + 0.2f * k), h * 0.48f), w * 0.08f, Dim145, f, a.t)
            val run = seg(p, 0.1f, 0.8f)
            drawLine(Wine148, Offset(w * 0.9f, h * 0.62f), Offset(mix(w * 0.9f, w * 0.05f, run), h * 0.62f), w * 0.02f, alpha = f)
            drawLine(Wine148, Offset(w * 0.5f, h * 0.62f), Offset(w * 0.5f, mix(h * 0.62f, h * 0.8f, seg(p, 0.4f, 0.8f))), w * 0.014f, alpha = f * seg(p, 0.4f, 0.45f))
            tagAt(a.tag("askhamr1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.3f, 0.45f))
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.4f), w * 0.18f, Qn.GoldLight, 0.5f * f * seg(p, 0.05f, 0.3f))
            qIcon("check", Offset(w * 0.5f, h * 0.4f), w * 0.1f, Qn.Jade, f * seg(p, 0.1f, 0.3f), a.t)
            row131(a, "askhamr2", 2, h * 0.68f, p, f, from = 0.2f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.62f, h * 0.8f), h * 0.2f, Qn.Person, f, lean = 0.15f * kotlin.math.sin(a.t * 3f) * (1f - seg(p, 0.4f, 0.6f)))
            figure(Offset(w * 0.38f, h * 0.8f), h * 0.2f, Qn.Person, f, lean = -0.15f * kotlin.math.sin(a.t * 3f) * (1f - seg(p, 0.4f, 0.6f)))
            qIcon("heart", Offset(w * 0.5f, h * 0.46f), w * 0.07f, Dim145, f * seg(p, 0.5f, 0.65f), a.t)
            row131(a, "askhamr3", 2, h * 0.24f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ as-tasalu: leave me as long as I leave you ═════════════
internal fun DrawScope.astasaluScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "astasalu", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            crowd145(w * 0.5f, h * 0.82f, 6, h * 0.16f, f, lean = 0.25f * seg(p, 0.4f, 0.7f), gap = 0.11f)
            row131(a, "astasalu0", 2, h * 0.3f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.5f, h * 0.82f), h * 0.22f, Qn.Person, f)
            tagAt(a.tag("astasalu1", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("astasalu1", 0, 1), Offset(w * 0.5f, h * 0.52f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.5f, h * 0.42f), w * 0.11f, Qn.Gold, f * seg(p, 0.02f, 0.15f), a.t)
            row131(a, "astasalu2", 3, h * 0.72f, p, f, from = 0.1f, step = 0.27f)
        },
        { p, f ->
            veil131(f)
            val spread = seg(p, 0.2f, 0.8f)
            for (k in 0 until 9) {
                val ang = k / 9f * 6.2832f
                drawCircle(Qn.GoldLight, w * 0.012f, Offset(w * 0.5f + w * 0.3f * spread * kotlin.math.cos(ang), h * 0.42f + h * 0.18f * spread * kotlin.math.sin(ang)), alpha = f)
            }
            row131(a, "astasalu3", 2, h * 0.76f, p, f, from = 0.1f, step = 0.4f)
        },
    )
}

// ═════════════ as-jam: the silver cup ═════════════
internal fun DrawScope.asjamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asjam", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            crowd145(w * 0.5f, h * 0.8f, 3, h * 0.2f, f, gap = 0.22f)
            figure(Offset(w * 0.5f, h * 0.8f), h * 0.2f, Dim145, f * (1f - 0.7f * seg(p, 0.6f, 0.85f)))
            row131(a, "asjam0", 3, h * 0.3f, p, f, from = 0.1f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            cup148(Offset(w * 0.5f, h * 0.42f), w * 0.14f, f * seg(p, 0.05f, 0.25f) * (1f - 0.75f * seg(p, 0.6f, 0.85f)))
            row131(a, "asjam1", 2, h * 0.7f, p, f, from = 0.15f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.7f, h * 0.44f), w * 0.1f, Qn.Gold, f, a.t)
            cup148(Offset(w * 0.36f, h * 0.44f), w * 0.1f, f * seg(p, 0.15f, 0.3f))
            row131(a, "asjam2", 3, h * 0.72f, p, f, from = 0.2f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            cup148(Offset(w * 0.5f, h * 0.4f), w * 0.2f, f)
            row131(a, "asjam3", 2, h * 0.74f, p, f, from = 0.15f, step = 0.4f)
        },
    )
}

// ═════════════ as-tard: send them away ═════════════
internal fun DrawScope.astardScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "astard", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.28f), w * 0.09f, a, "astard0", 0, f * seg(p, 0.02f, 0.18f))
            for (k in 0 until 6) figure(Offset(w * (0.2f + 0.12f * k), h * 0.82f), h * 0.15f, Qn.Person, f * seg(p, 0.3f + 0.05f * k, 0.4f + 0.05f * k))
            tagAt(a.tag("astard0", 0, 1), Offset(w * 0.5f, h * 0.54f), f * seg(p, 0.55f, 0.7f))
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) {
                figure(Offset(w * (0.7f + 0.08f * k), h * 0.8f), h * 0.22f, Dim145, f)
                qIcon("crown", Offset(w * (0.7f + 0.08f * k), h * 0.52f), w * 0.04f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            }
            crowd145(w * 0.32f, h * 0.82f, 3, h * 0.15f, f, gap = 0.08f)
            row131(a, "astard1", 2, h * 0.26f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) plain97(Offset(w * (0.75f - 0.25f * k), h * 0.42f), w * 0.08f, a, "astard2", k, f * seg(p, 0.05f + 0.2f * k, 0.2f + 0.2f * k))
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.6f), w * 0.3f, Qn.GoldLight, 0.45f * f * seg(p, 0.1f, 0.5f))
            for (k in 0 until 6) figure(Offset(w * (0.2f + 0.12f * k), h * 0.82f), h * 0.15f, Qn.Person, f)
            tagAt(a.tag("astard3", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-dhabaih: what Allah killed and what you killed ═════════════
internal fun DrawScope.asdhabaihScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asdhabaih", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            crowd145(w * 0.72f, h * 0.8f, 3, h * 0.18f, f, lean = -0.1f, gap = 0.06f)
            crowd145(w * 0.28f, h * 0.8f, 3, h * 0.18f, f * seg(p, 0.2f, 0.35f), gap = 0.06f)
            row131(a, "asdhabaih0", 2, h * 0.34f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asdhabaih1", 0, 0), Offset(w * 0.68f, h * 0.2f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asdhabaih1", 0, 1), Offset(w * 0.32f, h * 0.2f), f * seg(p, 0.3f, 0.45f))
            qIcon("cross", Offset(w * 0.5f, h * 0.5f), w * 0.12f, Qn.Rose, f * seg(p, 0.65f, 0.8f), a.t)
        },
        { p, f ->
            veil131(f)
            qIcon("cross", Offset(w * 0.5f, h * 0.42f), w * 0.12f, Qn.Rose, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("asdhabaih2", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 4) drawRect(Qn.GoldLight, Offset(w * (0.62f - 0.1f * k), h * (0.62f - 0.07f * k)), Size(w * 0.1f, h * 0.015f), alpha = f * seg(p, 0.1f + 0.15f * k, 0.2f + 0.15f * k))
            row131(a, "asdhabaih3", 3, h * 0.78f, p, f, from = 0.1f, step = 0.25f)
        },
    )
}

// ═════════════ as-zina: dress at every mosque ═════════════
internal fun DrawScope.aszinaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "aszina", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.5f, h * 0.46f), w * 0.12f, Dim145, f, a.t)
            for (k in 0 until 10) {
                val ang = k / 10f * 6.2832f + a.t * 0.4f
                drawCircle(Dim145, w * 0.01f, Offset(w * 0.5f + w * 0.26f * kotlin.math.cos(ang), h * 0.48f + h * 0.12f * kotlin.math.sin(ang)), alpha = f * seg(p, 0.2f, 0.4f))
            }
            row131(a, "aszina0", 2, h * 0.2f, p, f, from = 0.05f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.5f, h * 0.46f), w * 0.12f, Dim145, f, a.t)
            tagAt(a.tag("aszina1", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.1f, 0.25f))
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.5f, h * 0.46f), w * 0.12f, Qn.Gold, f, a.t)
            softGlow(Offset(w * 0.5f, h * 0.46f), w * 0.25f, Qn.GoldLight, 0.4f * f * seg(p, 0.1f, 0.4f))
            for (k in 0 until 10) {
                val ang = k / 10f * 6.2832f + a.t * 0.4f
                drawCircle(Qn.GoldLight, w * 0.012f, Offset(w * 0.5f + w * 0.26f * kotlin.math.cos(ang), h * 0.48f + h * 0.12f * kotlin.math.sin(ang)), alpha = f * seg(p, 0.15f, 0.35f))
            }
            row131(a, "aszina2", 3, h * 0.78f, p, f, from = 0.1f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) qIcon("scroll", Offset(w * (0.62f - 0.24f * k), h * 0.42f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k), a.t)
            row131(a, "aszina3", 2, h * 0.68f, p, f, from = 0.3f, step = 0.3f)
        },
    )
}

// ═════════════ as-anfal: the spoils of Badr ═════════════
internal fun DrawScope.asanfalScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asanfal", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.72f, h * 0.3f), w * 0.08f, a, "asanfal0", 0, f * seg(p, 0.02f, 0.18f))
            drawLine(Silver148, Offset(w * 0.4f, h * 0.36f), Offset(w * 0.52f, h * 0.5f), w * 0.01f, alpha = f * seg(p, 0.15f, 0.3f))
            row131(a, "asanfal0", 2, h * 0.7f, p, f, from = 0.25f, step = 0.35f, first = 1)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.72f, h * 0.36f), w * 0.08f, a, "asanfal0", 0, f)
            val give = seg(p, 0.45f, 0.8f)
            val x = mix(w * 0.35f, w * 0.65f, give)
            drawLine(Silver148, Offset(x - w * 0.06f, h * 0.5f), Offset(x + w * 0.06f, h * 0.6f), w * 0.01f, alpha = f)
            row131(a, "asanfal1", 2, h * 0.76f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asanfal2", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.44f), w * 0.15f, Qn.GoldLight, 0.4f * f * seg(p, 0.05f, 0.3f))
            for (k in 0 until 5) qIcon("coin", Offset(w * (0.38f + 0.06f * k), h * 0.46f), w * 0.04f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("asanfal2", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.05f, 0.2f))
        },
        { p, f ->
            veil131(f)
            val share = seg(p, 0.6f, 0.9f)
            for (k in 0 until 3) {
                val gx = w * (0.75f - 0.25f * k)
                crowd145(gx, h * 0.82f, 2, h * 0.14f, f, gap = 0.04f)
                qIcon("coin", Offset(mix(w * 0.5f, gx, share), mix(h * 0.5f, h * 0.6f, share)), w * 0.04f, Qn.Gold, f, a.t)
            }
            row131(a, "asanfal3", 3, h * 0.3f, p, f, from = 0.05f, step = 0.18f)
        },
    )
}
