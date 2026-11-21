package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 143 (2026-11-21): occasions of revelation from al-Wadi'i's al-Sahih al-Musnad. Each occasion is a small,
 * plain scene: a chain of narrators, two hills for al-Safa and al-Marwa, a house with a front door and a back way,
 * the white and black threads of dawn. Companions are plain roundels; the Prophet ﷺ is not drawn.
 */

private fun DrawScope.chain143(x: Float, y0: Float, y1: Float, gap: Int, q: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val n = 5
    for (k in 0 until n) {
        if (k == gap) continue
        val d = k / (n - 1f)
        if (d > q) break
        drawOval(Qn.GoldLight, Offset(x - size.width * 0.025f, mix(y1, y0, d) - size.height * 0.025f), Size(size.width * 0.05f, size.height * 0.05f), alpha = alpha, style = Stroke(size.width * 0.007f))
    }
}

private fun DrawScope.hills143(cx: Float, y: Float, s: Float, color: Color, alpha: Float, t: Float) {
    qIcon("mountain", Offset(cx + s * 0.9f, y), s, color, alpha, t)
    qIcon("mountain", Offset(cx - s * 0.9f, y), s, color, alpha, t)
}

// ═════════════ as-tarif ═════════════
internal fun DrawScope.astarifScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "astarif", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("people", Offset(w * 0.72f, h * 0.36f), w * 0.11f, Fig131, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("ear", Offset(w * 0.28f, h * 0.36f), w * 0.08f, Fig131, f * seg(p, 0.45f, 0.58f), a.t)
            row131(a, "astarif0", 2, h * 0.6f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            for (i in 0 until 2) qIcon("book", Offset(w * (0.72f - 0.44f * i), h * 0.36f), w * 0.09f, if (i == 0) Qn.Gold else Qn.GoldLight, f * seg(p, 0.1f + 0.4f * i, 0.22f + 0.4f * i), a.t)
            row131(a, "astarif1", 2, h * 0.58f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            chain143(w * 0.5f, h * 0.18f, h * 0.56f, -1, seg(p, 0.1f, 0.5f), f)
            softGlow(Offset(w * 0.5f, h * 0.16f), w * 0.07f, Qn.GoldLight, 0.6f * f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("astarif2", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            chain143(w * 0.5f, h * 0.18f, h * 0.56f, 3, seg(p, 0.1f, 0.5f), f)
            qIcon("cross", Offset(w * 0.5f, mix(h * 0.56f, h * 0.18f, 0.75f)), w * 0.05f, Qn.Rose, f * seg(p, 0.5f, 0.6f), a.t)
            tagAt(a.tag("astarif3", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ as-umum ═════════════
internal fun DrawScope.asumumScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "asumum", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            drawCircle(Qn.GoldLight, w * 0.12f * easeInOut(seg(p, 0.1f, 0.35f)), Offset(w * 0.72f, h * 0.38f), alpha = f, style = Stroke(w * 0.008f))
            drawCircle(Dim131, w * 0.02f, Offset(w * 0.28f, h * 0.38f), alpha = f * seg(p, 0.5f, 0.6f))
            row131(a, "asumum0", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            person99(Offset(w * 0.8f, g), h * 0.14f, Fig131, f)
            for (k in 0 until 6) person99(Offset(w * (0.1f + 0.1f * k), g), h * 0.1f, Dim131, f * seg(p, 0.5f + 0.04f * k, 0.62f + 0.04f * k))
            row131(a, "asumum1", 2, h * 0.28f, p, f, from = 0.05f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            drawCircle(Qn.GoldLight, w * 0.16f, Offset(w * 0.5f, h * 0.4f), alpha = f * seg(p, 0.05f, 0.18f), style = Stroke(w * 0.008f))
            drawCircle(Qn.Rose, w * 0.025f, Offset(w * 0.5f, h * 0.4f), alpha = f * seg(p, 0.2f, 0.32f))
            qIcon("lock", Offset(w * 0.6f, h * 0.48f), w * 0.04f, Qn.Gold, f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "asumum2", 2, h * 0.68f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.5f, h * 0.38f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("asumum3", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ as-faida ═════════════
internal fun DrawScope.asfaidaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asfaida", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.4f, h * 0.38f), w * 0.11f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("key", Offset(w * 0.62f, h * 0.38f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.3f, 0.42f), a.t)
            tagAt(a.tag("asfaida0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            val clear = seg(p, 0.5f, 0.85f)
            qIcon("sun", Offset(w * 0.5f, h * 0.34f), w * 0.12f, Qn.GoldLight, f * clear, a.t)
            for (k in 0 until 3) qIcon("cloud", Offset(w * (0.3f + 0.2f * k) + w * 0.3f * clear * (k - 1), h * 0.34f), w * 0.12f, Dim131, f * (1f - clear), a.t)
            row131(a, "asfaida1", 2, h * 0.62f, p, f, from = 0.1f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.36f), w * 0.08f, a, "asfaida2", 0, f * seg(p, 0.02f, 0.15f))
            qIcon("book", Offset(w * 0.3f, h * 0.38f), w * 0.09f, Qn.Gold, f * seg(p, 0.55f, 0.68f), a.t)
            tagAt(a.tag("asfaida2", 0, 1), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.65f, 0.78f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 5) drawRect(Qn.GoldLight, Offset(w * (0.2f + 0.12f * k), h * (0.6f - 0.07f * k)), Size(w * 0.12f, h * 0.02f), alpha = f * seg(p, 0.1f + 0.1f * k, 0.2f + 0.1f * k))
            person99(Offset(w * mix(0.24f, 0.74f, seg(p, 0.2f, 0.7f)), h * mix(0.6f, 0.32f, seg(p, 0.2f, 0.7f))), h * 0.08f, Fig131, f, walk = a.t)
            tagAt(a.tag("asfaida3", 0, 0), Offset(w * 0.5f, h * 0.74f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-safa ═════════════
internal fun DrawScope.assafaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "assafa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            hills143(w * 0.5f, h * 0.62f, w * 0.14f, Dim131, f, a.t)
            plain97(Offset(w * 0.5f, h * 0.28f), w * 0.08f, a, "assafa0", 0, f * seg(p, 0.02f, 0.15f))
            tagAt(a.tag("assafa0", 0, 1), Offset(w * 0.5f, h * 0.46f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            hills143(w * 0.5f, h * 0.62f, w * 0.14f, Color(0xFF8A7A62), f, a.t)
            softGlow(Offset(w * 0.5f, h * 0.6f), w * 0.2f, Qn.GoldLight, 0.4f * f * seg(p, 0.2f, 0.4f))
            person99(Offset(w * mix(0.66f, 0.34f, (a.t * 0.12f) % 1f), h * 0.74f), h * 0.08f, Fig131, f, walk = a.t)
            row131(a, "assafa1", 2, h * 0.28f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("kaaba", Offset(w * 0.72f, h * 0.52f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.18f), a.t)
            qIcon("mountain", Offset(w * 0.28f, h * 0.56f), w * 0.12f, Color(0xFF8A7A62), f * seg(p, 0.5f, 0.62f), a.t)
            row131(a, "assafa2", 2, h * 0.26f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            for (i in 0 until 2) qIcon("book", Offset(w * (0.6f - 0.2f * i), h * 0.38f), w * 0.09f, Qn.Gold, f * seg(p, 0.05f + 0.1f * i, 0.18f + 0.1f * i), a.t)
            tagAt(a.tag("assafa3", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-qays ═════════════
internal fun DrawScope.asqaysScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "asqays", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            qIcon("moon", Offset(w * 0.72f, h * 0.26f), w * 0.07f, Fig131, f * (1f - seg(p, 0.5f, 0.65f)), a.t)
            qIcon("sun", Offset(w * 0.28f, h * 0.26f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            qIcon("bed", Offset(w * 0.5f, g - h * 0.06f), w * 0.16f, Dim131, f, a.t)
            qIcon("cup", Offset(w * 0.78f, g - h * 0.04f), w * 0.05f, Fig131, 0.6f * f, a.t)
            tagAt(a.tag("asqays0", 0, 0), Offset(w * 0.5f, h * 0.46f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
        { p, f ->
            plain97(Offset(w * 0.72f, h * 0.3f), w * 0.08f, a, "asqays1", 0, f * seg(p, 0.02f, 0.15f))
            person99(Offset(w * 0.72f, g), h * 0.13f, Fig131, f * (1f - seg(p, 0.55f, 0.7f)))
            qIcon("bed", Offset(w * 0.72f, g - h * 0.05f), w * 0.14f, Fig131, f * seg(p, 0.55f, 0.7f), a.t)
            woman12(Offset(w * mix(0.6f, 0.2f, seg(p, 0.2f, 0.5f)), g), h * 0.12f, Dim131, f, walk = a.t)
            tagAt(a.tag("asqays1", 0, 1), Offset(w * 0.35f, h * 0.36f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            qIcon("moon", Offset(w * 0.5f, h * 0.2f), w * 0.07f, Fig131, f, a.t)
            for (k in 0 until 4) person99(Offset(w * (0.2f + 0.2f * k), g), h * 0.11f, Fig131, f)
            for (k in 0 until 4) qIcon("cup", Offset(w * (0.2f + 0.2f * k), g - h * 0.22f), w * 0.035f, Qn.GoldLight, f * seg(p, 0.3f + 0.05f * k, 0.42f + 0.05f * k), a.t)
            tagAt(a.tag("asqays2", 0, 0), Offset(w * 0.5f, h * 0.38f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF0E0A1A), Offset(0f, h * 0.5f), Size(w, h * 0.5f), alpha = f)
            val q = seg(p, 0.1f, 0.5f)
            drawLine(Color(0xFFF3EEDF), Offset(w * 0.1f, h * 0.5f), Offset(w * (0.1f + 0.8f * q), h * 0.5f), w * 0.006f, alpha = f)
            drawLine(Color(0xFF2A2440), Offset(w * 0.1f, h * 0.53f), Offset(w * (0.1f + 0.8f * q), h * 0.53f), w * 0.006f, alpha = f)
            tagAt(a.tag("asqays3", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-buyut ═════════════
internal fun DrawScope.asbuyutScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "asbuyut", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            qIcon("house", Offset(w * 0.5f, g - h * 0.14f), w * 0.3f, Fig131, f, a.t)
            arrow131(Offset(w * 0.86f, g - h * 0.05f), Offset(w * 0.6f, g - h * 0.05f), seg(p, 0.1f, 0.3f), Qn.Jade, f)
            arrow131(Offset(w * 0.14f, g - h * 0.36f), Offset(w * 0.38f, g - h * 0.24f), seg(p, 0.5f, 0.7f), Qn.Rose, f)
            row131(a, "asbuyut0", 2, h * 0.2f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.6f, g - h * 0.14f), w * 0.26f, Fig131, f, a.t)
            person99(Offset(w * mix(0.86f, 0.64f, seg(p, 0.05f, 0.35f)), g), h * 0.12f, Fig131, f, walk = a.t)
            softGlow(Offset(w * 0.3f, h * 0.44f), w * 0.08f, Qn.GoldLight, 0.6f * f * seg(p, 0.5f, 0.7f))
            qIcon("heart", Offset(w * 0.3f, h * 0.44f), w * 0.05f, Qn.Rose, f * seg(p, 0.55f, 0.68f), a.t)
            row131(a, "asbuyut1", 2, h * 0.22f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            for (k in 0 until 3) person99(Offset(w * (0.62f + 0.1f * k), g), h * 0.11f, Fig131, f * seg(p, 0.05f, 0.18f))
            for (k in 0 until 3) person99(Offset(w * (0.12f + 0.1f * k), g), h * 0.11f, Dim131, f * seg(p, 0.5f, 0.62f))
            row131(a, "asbuyut2", 2, h * 0.3f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.36f), w * 0.08f, a, "asbuyut3", 0, f * seg(p, 0.02f, 0.15f))
            qIcon("house", Offset(w * 0.3f, h * 0.4f), w * 0.14f, Fig131, f * seg(p, 0.2f, 0.32f), a.t)
            tagAt(a.tag("asbuyut3", 0, 1), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ as-maqil ═════════════
internal fun DrawScope.asmaqilScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "asmaqil", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.34f), w * 0.08f, a, "asmaqil0", 0, f * seg(p, 0.02f, 0.15f))
            qIcon("hourglass", Offset(w * 0.3f, h * 0.36f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.45f, 0.58f), a.t)
            tagAt(a.tag("asmaqil0", 0, 1), Offset(w * 0.3f, h * 0.58f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            qIcon("house", Offset(w * 0.5f, g - h * 0.14f), w * 0.26f, Dim131, f, a.t)
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig131, f)
            person99(Offset(w * 0.84f, g), h * 0.13f, Dim131, f)
            qIcon("cross", Offset(w * 0.5f, h * 0.36f), w * 0.07f, Qn.Rose, f * seg(p, 0.15f, 0.28f), a.t)
            row131(a, "asmaqil1", 2, h * 0.2f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("key", Offset(w * 0.5f, h * 0.38f), w * 0.11f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("asmaqil2", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            softGlow(Offset(w * 0.5f, h * 0.18f), w * 0.14f, Color(0xFFFFD27A), 0.7f * f)
            qIcon("sun", Offset(w * 0.5f, h * 0.18f), w * 0.12f, Qn.GoldLight, f, a.t)
            for (k in 0 until 2) person99(Offset(w * (0.42f + 0.16f * k), g), h * 0.11f, Fig131, f)
            tagAt(a.tag("asmaqil3", 0, 0), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
    )
}
