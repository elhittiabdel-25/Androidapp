package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 138 (2026-11-21): the Makki and the Madani from al-Itqan. Mecca is the Ka'ba, Medina a house among palms, the
 * hijra a dotted road between them; a surah is a small book. Ibn Mas'ud a plain roundel, 'Umar a companion roundel.
 */

private fun DrawScope.medina138(c: Offset, s: Float, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    qIcon("house", c, s, Fig131, alpha, t)
    qIcon("tree", Offset(c.x - s * 0.6f, c.y + s * 0.05f), s * 0.6f, Qn.Jade, alpha, t)
    qIcon("tree", Offset(c.x + s * 0.6f, c.y + s * 0.05f), s * 0.6f, Qn.Jade, alpha, t)
}

private fun DrawScope.road138(from: Offset, to: Offset, q: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val n = 14
    for (k in 0 until n) {
        val d = k / (n - 1f)
        if (d > q) break
        drawCircle(Qn.GoldLight, size.width * 0.006f, Offset(mix(from.x, to.x, d), mix(from.y, to.y, d) - size.height * 0.04f * kotlin.math.sin(d * 3.1416f)), alpha = alpha)
    }
}

// ═════════════ iq-makki ═════════════
internal fun DrawScope.iqmakkiScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqmakki", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.76f, h * 0.4f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            road138(Offset(w * 0.66f, h * 0.42f), Offset(w * 0.34f, h * 0.42f), seg(p, 0.3f, 0.55f), f)
            medina138(Offset(w * 0.24f, h * 0.4f), w * 0.1f, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "iqmakki0", 2, h * 0.64f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.5f, h * 0.36f), w * 0.14f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("book", Offset(w * 0.5f, h * 0.18f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.2f, 0.32f), a.t)
            row131(a, "iqmakki1", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.72f, h * 0.38f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("people", Offset(w * 0.28f, h * 0.38f), w * 0.12f, Fig131, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "iqmakki2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            qIcon("kaaba", Offset(w * 0.86f, h * 0.62f), w * 0.08f, Dim131, f, a.t)
            medina138(Offset(w * 0.14f, h * 0.62f), w * 0.06f, f * 0.6f, a.t)
            road138(Offset(w * 0.8f, h * 0.66f), Offset(w * 0.2f, h * 0.66f), 1f, f * 0.6f)
            person99(Offset(mix(w * 0.7f, w * 0.4f, seg(p, 0.1f, 0.7f)), h * 0.8f), h * 0.1f, Fig131, f, walk = a.t)
            qIcon("book", Offset(w * 0.5f, h * 0.42f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("iqmakki3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ iq-faida ═════════════
internal fun DrawScope.iqfaidaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "iqfaida", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.68f, h * 0.38f), w * 0.1f, Dim131, f * seg(p, 0.05f, 0.18f), a.t)
            softGlow(Offset(w * 0.32f, h * 0.38f), w * 0.09f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.5f))
            qIcon("book", Offset(w * 0.32f, h * 0.38f), w * 0.1f, Qn.Gold, f * seg(p, 0.25f, 0.4f), a.t)
            arrow131(Offset(w * 0.58f, h * 0.38f), Offset(w * 0.42f, h * 0.38f), seg(p, 0.2f, 0.35f), Qn.GoldLight, f)
            row131(a, "iqfaida0", 2, h * 0.62f, p, f, from = 0.3f, step = 0.3f)
        },
        { p, f ->
            for (k in 0 until 5) person99(Offset(w * (0.14f + 0.18f * k), g), h * 0.11f, Dim131, f * seg(p, 0.05f + 0.05f * k, 0.18f + 0.05f * k))
            qIcon("book", Offset(w * 0.5f, h * 0.42f), w * 0.08f, Qn.Gold, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("iqfaida1", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.32f), w * 0.09f, a, "iqfaida2", 0, f * seg(p, 0.02f, 0.15f))
            tagAt(a.tag("iqfaida2", 0, 1), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 25) drawCircle(Qn.GoldLight, w * 0.016f, Offset(w * (0.26f + 0.12f * (k % 5)), h * (0.22f + 0.07f * (k / 5))), alpha = f * seg(p, 0.05f + 0.015f * k, 0.12f + 0.015f * k))
            tagAt(a.tag("iqfaida3", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ iq-dawabit ═════════════
internal fun DrawScope.iqdawabitScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqdawabit", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            medina138(Offset(w * 0.72f, h * 0.56f), w * 0.08f, f * seg(p, 0.2f, 0.32f), a.t)
            qIcon("kaaba", Offset(w * 0.28f, h * 0.56f), w * 0.1f, Qn.Gold, f * seg(p, 0.6f, 0.72f), a.t)
            row131(a, "iqdawabit0", 2, h * 0.34f, p, f, from = 0.08f, step = 0.42f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 7) qIcon("check", Offset(w * (0.2f + 0.1f * k), h * 0.38f), w * 0.035f, Qn.Jade, f * seg(p, 0.05f + 0.04f * k, 0.15f + 0.04f * k), a.t)
            qIcon("cross", Offset(w * 0.86f, h * 0.38f), w * 0.04f, Qn.Rose, f * seg(p, 0.45f, 0.55f), a.t)
            tagAt(a.tag("iqdawabit1", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("people", Offset(w * 0.72f, h * 0.38f), w * 0.12f, Fig131, f * seg(p, 0.1f, 0.22f), a.t)
            qIcon("scale", Offset(w * 0.28f, h * 0.38f), w * 0.11f, Qn.Gold, f * seg(p, 0.55f, 0.68f), a.t)
            row131(a, "iqdawabit2", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("ear", Offset(w * 0.72f, h * 0.38f), w * 0.09f, Fig131, f * seg(p, 0.1f, 0.22f), a.t)
            qIcon("scale", Offset(w * 0.28f, h * 0.38f), w * 0.1f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "iqdawabit3", 2, h * 0.62f, p, f, from = 0.1f, step = 0.42f)
        },
    )
}

// ═════════════ iq-suar ═════════════
internal fun DrawScope.iqsuarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "iqsuar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 20) drawRoundRect(Qn.GoldLight, Offset(w * (0.82f - 0.07f * (k % 10)) - w * 0.02f, h * (0.26f + 0.1f * (k / 10))), Size(w * 0.04f, h * 0.06f), androidx.compose.ui.geometry.CornerRadius(w * 0.005f), alpha = f * seg(p, 0.05f + 0.015f * k, 0.12f + 0.015f * k))
            row131(a, "iqsuar0", 2, h * 0.62f, p, f, from = 0.15f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (i in 0 until 4) {
                val x = if (i % 2 == 0) w * 0.72f else w * 0.28f
                tagAt(a.tag("iqsuar1", 0, i), Offset(x, h * 0.36f + h * 0.2f * (i / 2)), f * seg(p, 0.1f + 0.2f * i, 0.22f + 0.2f * i), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.5f, h * 0.42f), w * 0.14f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            for (k in 0 until 3) qIcon("book", Offset(w * (0.3f + 0.2f * k), h * 0.22f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.3f + 0.08f * k, 0.42f + 0.08f * k), a.t)
            tagAt(a.tag("iqsuar2", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.72f, h * 0.38f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("kaaba", Offset(w * 0.28f, h * 0.38f), w * 0.11f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "iqsuar3", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ iq-safari ═════════════
internal fun DrawScope.iqsafariScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "iqsafari", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            qIcon("house", Offset(w * 0.74f, g - h * 0.1f), w * 0.18f, Fig131, f * seg(p, 0.05f, 0.18f), a.t)
            road138(Offset(w * 0.5f, g - h * 0.02f), Offset(w * 0.08f, g - h * 0.02f), 1f, f * seg(p, 0.45f, 0.55f))
            person99(Offset(mix(w * 0.42f, w * 0.2f, seg(p, 0.5f, 0.95f)), g), h * 0.1f, Fig131, f * seg(p, 0.45f, 0.55f), walk = a.t)
            row131(a, "iqsafari0", 2, h * 0.3f, p, f, from = 0.1f, step = 0.42f)
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.86f, h * 0.5f), w * 0.09f, Qn.Gold, f, a.t)
            medina138(Offset(w * 0.14f, h * 0.5f), w * 0.06f, f, a.t)
            road138(Offset(w * 0.8f, h * 0.54f), Offset(w * 0.2f, h * 0.54f), seg(p, 0.05f, 0.5f), f)
            softGlow(Offset(w * 0.62f, h * 0.48f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f))
            qIcon("book", Offset(w * 0.62f, h * 0.36f), w * 0.05f, Qn.Gold, f * seg(p, 0.35f, 0.48f), a.t)
            row131(a, "iqsafari1", 2, h * 0.24f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.72f, h * 0.36f), w * 0.09f, a, "iqsafari2", 0, 1, f * seg(p, 0.02f, 0.15f))
            qIcon("kaaba", Offset(w * 0.3f, h * 0.38f), w * 0.12f, Qn.Gold, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("iqsafari2", 0, 1), Offset(w * 0.3f, h * 0.62f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.5f, h * 0.38f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("iqsafari3", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ iq-layli ═════════════
internal fun DrawScope.iqlayliScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "iqlayli", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("sun", Offset(w * 0.72f, h * 0.38f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("moon", Offset(w * 0.28f, h * 0.38f), w * 0.1f, Fig131, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "iqlayli0", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            for (r in 0 until 2) for (k in 0 until 4) person99(Offset(w * (0.2f + 0.14f * k), g - h * 0.07f * r), h * (0.1f - 0.01f * r), Dim131, f)
            person99(Offset(mix(w * 1.02f, w * 0.84f, easeInOut(seg(p, 0.2f, 0.5f))), g), h * 0.12f, Fig131, f, walk = a.t)
            voice130(Offset(w * 0.84f, g - h * 0.18f), 0.5f, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "iqlayli1", 2, h * 0.26f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            medina138(Offset(w * 0.72f, h * 0.48f), w * 0.07f, f, a.t)
            qIcon("sun", Offset(w * 0.6f, h * 0.24f), w * 0.06f, Color(0xFFE0A060), f * seg(p, 0.1f, 0.22f), a.t)
            qIcon("house", Offset(w * 0.28f, h * 0.5f), w * 0.1f, Dim131, f, a.t)
            qIcon("sun", Offset(w * 0.18f, h * 0.3f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.55f, 0.68f), a.t)
            row131(a, "iqlayli2", 2, h * 0.68f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            val turn = easeInOut(seg(p, 0.2f, 0.6f))
            qIcon("kaaba", Offset(w * mix(0.86f, 0.14f, turn), h * 0.42f), w * 0.08f, Qn.Gold, f, a.t)
            for (k in 0 until 4) person99(Offset(w * (0.32f + 0.12f * k), g), h * 0.1f, Fig131, f)
            drawArc(Qn.GoldLight, 200f, 140f * turn, false, Offset(w * 0.3f, h * 0.48f), Size(w * 0.4f, h * 0.2f), alpha = f, style = Stroke(w * 0.006f))
            tagAt(a.tag("iqlayli3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}
