package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 150 (2026-11-26): occasions of revelation from al-Wadi'i, al-Tawba to Yusuf: Abu Talib's death, Ka'b ibn
 * Malik in four cards, those who bent their chests, and "if only you told us stories". The Prophet ﷺ is never drawn
 * (his joy is only a soft light like a piece of the moon); companions are plain roundels; people are faceless; no
 * animals.
 */

private fun DrawScope.wall150(x: Float, top: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(Color(0xFF7A6A55), Offset(x, top), Size(size.width * 0.06f, size.height * 0.8f - top), alpha = alpha)
}

// ═════════════ as-talib: Abu Talib's death ═════════════
internal fun DrawScope.astalibScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "astalib", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("bed", Offset(w * 0.5f, h * 0.6f), w * 0.16f, Dim145, f * seg(p, 0.02f, 0.15f), a.t)
            crowd145(w * 0.78f, h * 0.82f, 2, h * 0.2f, f * seg(p, 0.4f, 0.55f), gap = 0.07f)
            row131(a, "astalib0", 2, h * 0.26f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("astalib1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.4f), w * 0.12f, Qn.GoldLight, 0.6f * f * seg(p, 0.05f, 0.3f))
            qIcon("bed", Offset(w * 0.5f, h * 0.62f), w * 0.14f, Dim145, f, a.t)
            tagAt(a.tag("astalib1", 0, 1), Offset(w * 0.5f, h * 0.82f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            qIcon("bed", Offset(w * 0.5f, h * 0.58f), w * 0.14f, Dim145, f * (1f - 0.5f * seg(p, 0.3f, 0.6f)), a.t)
            softGlow(Offset(w * 0.5f, h * 0.36f), w * 0.1f, Qn.GoldLight, 0.4f * f * seg(p, 0.5f, 0.8f))
            row131(a, "astalib2", 2, h * 0.24f, p, f, from = 0.05f, step = 0.5f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("astalib3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            lightShafts(Offset(w * 0.5f, h * 0.02f), 1.5708f, 0.4f, h * 0.6f, Qn.GoldLight, 0.35f * f * seg(p, 0.2f, 0.5f), a.t)
            tagAt(a.tag("astalib3", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.5f, 0.65f))
        },
    )
}

// ═════════════ as-kab1: putting it off ═════════════
internal fun DrawScope.askab1Scene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askab1", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.34f), w * 0.1f, a, "askab10", 0, f * seg(p, 0.02f, 0.18f))
            tagAt(a.tag("askab10", 0, 1), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("sun", Offset(w * 0.72f, h * 0.22f), w * 0.1f, Qn.Ember, f * seg(p, 0.02f, 0.15f), a.t)
            drawLine(Qn.GoldLight, Offset(w * 0.85f, h * 0.7f), Offset(mix(w * 0.85f, w * 0.1f, seg(p, 0.2f, 0.6f)), h * 0.7f), w * 0.004f, alpha = f)
            qIcon("tree", Offset(w * 0.25f, h * 0.48f), w * 0.1f, Qn.Leaf, f * seg(p, 0.6f, 0.75f), a.t)
            row131(a, "askab11", 3, h * 0.84f, p, f, from = 0.05f, step = 0.27f)
        },
        { p, f ->
            veil131(f)
            val step = kotlin.math.sin(a.t * 1.6f)
            figure(Offset(w * (0.75f - 0.06f * step), h * 0.8f), h * 0.2f, Qn.Person, f, walk = a.t * 2f)
            crowd145(mix(w * 0.4f, w * -0.1f, seg(p, 0.2f, 0.9f)), h * 0.82f, 4, h * 0.14f, f * (1f - seg(p, 0.75f, 0.95f)), gap = 0.06f)
            row131(a, "askab12", 2, h * 0.26f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("hourglass", Offset(w * 0.5f, h * 0.42f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            row131(a, "askab13", 2, h * 0.7f, p, f, from = 0.15f, step = 0.45f)
        },
    )
}

// ═════════════ as-kab2: the truth ═════════════
internal fun DrawScope.askab2Scene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askab2", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            dots145(83, w * 0.5f, h * 0.5f, 14, Dim145, p, f, from = 0.1f)
            row131(a, "askab20", 2, h * 0.24f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("askab21", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.1f, 0.25f))
            qIcon("scale", Offset(w * 0.5f, h * 0.56f), w * 0.1f, Dim145, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("askab21", 0, 1), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("askab22", 0, 1), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f))
            softGlow(Offset(w * 0.5f, h * 0.46f), w * 0.15f, Qn.GoldLight, 0.5f * f * seg(p, 0.05f, 0.3f))
            qIcon("check", Offset(w * 0.5f, h * 0.46f), w * 0.1f, Qn.Jade, f * seg(p, 0.1f, 0.3f), a.t)
            tagAt(a.tag("askab22", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.4f), w * 0.09f, a, "askab23", 0, f * seg(p, 0.05f, 0.2f))
            plain97(Offset(w * 0.32f, h * 0.4f), w * 0.09f, a, "askab23", 1, f * seg(p, 0.4f, 0.55f))
        },
    )
}

// ═════════════ as-kab3: fifty nights ═════════════
internal fun DrawScope.askab3Scene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askab3", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            dots145(50, w * 0.5f, h * 0.46f, 10, Qn.GoldLight, p, f, from = 0.1f)
            figure(Offset(w * 0.5f, h * 0.84f), h * 0.14f, Qn.Person, f)
            row131(a, "askab30", 2, h * 0.22f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            wall150(w * 0.5f, h * 0.48f, f)
            figure(Offset(w * 0.42f, mix(h * 0.8f, h * 0.5f, seg(p, 0.1f, 0.4f))), h * 0.16f, Qn.Person, f)
            plain97(Offset(w * 0.74f, h * 0.3f), w * 0.08f, a, "askab31", 0, f * seg(p, 0.3f, 0.45f))
            for (k in 0 until 3) qIcon("drop", Offset(w * (0.4f + 0.02f * k), h * (0.42f + 0.03f * k)), w * 0.02f, Color(0xFF8FC7E8), f * seg(p, 0.75f + 0.05f * k, 0.85f + 0.05f * k), a.t)
            tagAt(a.tag("askab31", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.55f, 0.7f))
        },
        { p, f ->
            veil131(f)
            val burn = seg(p, 0.5f, 0.85f)
            qIcon("scroll", Offset(mix(w * 0.3f, w * 0.5f, burn), mix(h * 0.4f, h * 0.55f, burn)), w * 0.09f, Qn.GoldLight, f * (1f - burn), a.t)
            qIcon("flame", Offset(w * 0.5f, h * 0.58f), w * 0.1f, Qn.Ember, f * seg(p, 0.4f, 0.55f), a.t)
            row131(a, "askab32", 2, h * 0.24f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.3f, h * 0.56f), w * 0.1f, Dim145, f, a.t)
            figure(Offset(mix(w * 0.55f, w * 0.85f, seg(p, 0.4f, 0.9f)), h * 0.82f), h * 0.18f, Qn.Person, f * 0.8f, walk = a.t * 3f)
            row131(a, "askab33", 2, h * 0.24f, p, f, from = 0.05f, step = 0.45f)
        },
    )
}

// ═════════════ as-kab4: the best day ═════════════
internal fun DrawScope.askab4Scene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askab4", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            tagAt(a.tag("askab40", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.5f, 0.65f))
            crowd145(mix(w * 0.9f, w * 0.6f, seg(p, 0.05f, 0.4f)), h * 0.82f, 3, h * 0.16f, f, gap = 0.06f)
            crowd145(mix(w * 0.1f, w * 0.4f, seg(p, 0.15f, 0.5f)), h * 0.82f, 3, h * 0.16f, f, gap = 0.06f)
            tagAt(a.tag("askab40", 0, 0), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.36f), w * 0.2f, Color(0xFFF4F0DC), 0.7f * f * seg(p, 0.02f, 0.3f))
            drawCircle(Color(0xFFF7F2E0), w * 0.06f, Offset(w * 0.5f, h * 0.36f), alpha = 0.8f * f * seg(p, 0.05f, 0.25f))
            row131(a, "askab41", 2, h * 0.7f, p, f, from = 0.2f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            qIcon("check", Offset(w * 0.5f, h * 0.4f), w * 0.1f, Qn.Jade, f * seg(p, 0.05f, 0.2f), a.t)
            softGlow(Offset(w * 0.5f, h * 0.4f), w * 0.14f, Qn.GoldLight, 0.4f * f * seg(p, 0.2f, 0.5f))
            row131(a, "askab42", 2, h * 0.7f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            pile149x(Offset(w * 0.5f, h * 0.56f), w * 0.1f, f, seg(p, 0.3f, 0.7f))
            tagAt(a.tag("askab43", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
    )
}

/** A pile of coins of which [half] (0..1) slides to the side: the part given away. */
private fun DrawScope.pile149x(c: Offset, s: Float, alpha: Float, half: Float) {
    for (k in 0 until 6) {
        val give = k >= 3
        val x = c.x + s * 0.4f * (k % 3 - 1) + (if (give) s * 1.4f * half else 0f)
        val y = c.y - s * 0.35f * (k / 3)
        qIcon("coin", Offset(x, y), s * 0.45f, Qn.Gold, alpha * (if (give) 1f - 0.5f * half else 1f))
    }
}

// ═════════════ as-yathnun: they bent their chests ═════════════
internal fun DrawScope.asyathnunScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asyathnun", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 12) drawCircle(Qn.GoldLight, w * 0.005f, Offset(w * (0.1f + 0.075f * k), h * (0.12f + 0.06f * ((k * 5) % 4))), alpha = f * seg(p, 0.05f + 0.03f * k, 0.15f + 0.03f * k))
            figure(Offset(w * 0.5f, h * 0.82f), h * 0.2f, Qn.Person, f)
            row131(a, "asyathnun0", 2, h * 0.5f, p, f, from = 0.2f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.5f, h * 0.82f), h * 0.2f * (1f - 0.25f * seg(p, 0.1f, 0.4f)), Qn.Person, f, lean = 0.4f * seg(p, 0.1f, 0.4f))
            row131(a, "asyathnun1", 2, h * 0.3f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            lightShafts(Offset(w * 0.5f, h * 0.02f), 1.5708f, 0.6f, h * 0.8f, Qn.GoldLight, 0.3f * f * seg(p, 0.05f, 0.4f), a.t)
            figure(Offset(w * 0.5f, h * 0.82f), h * 0.15f, Qn.Person, f, lean = 0.4f)
            tagAt(a.tag("asyathnun2", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            row131(a, "asyathnun3", 2, h * 0.46f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ as-qasas: if only you told us stories ═════════════
internal fun DrawScope.asqasasScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asqasas", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.5f, h * 0.36f), w * 0.1f, Qn.Gold, f * seg(p, 0.02f, 0.15f), a.t)
            crowd145(w * 0.5f, h * 0.84f, 5, h * 0.14f, f, gap = 0.1f)
            row131(a, "asqasas0", 2, h * 0.6f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.4f), w * 0.2f, Qn.GoldLight, 0.6f * f * seg(p, 0.05f, 0.3f))
            qIcon("book", Offset(w * 0.5f, h * 0.4f), w * 0.12f, Qn.Gold, f, a.t)
            row131(a, "asqasas1", 2, h * 0.7f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) qIcon("book", Offset(w * (0.62f - 0.24f * k), h * 0.4f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f + 0.35f * k, 0.2f + 0.35f * k), a.t)
            row131(a, "asqasas2", 2, h * 0.7f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            dots145(5, w * 0.5f, h * 0.34f, 5, Qn.GoldLight, p, f, from = 0.05f)
            row131(a, "asqasas3", 3, h * 0.6f, p, f, from = 0.2f, step = 0.22f)
        },
    )
}
