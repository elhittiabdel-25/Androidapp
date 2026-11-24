package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 147 (2026-11-24): occasions of revelation from al-Wadi'i, end of al-Nisa' and start of al-Ma'ida: the
 * reconciliation of spouses, the kalala, 'A'isha's necklace and dry ablution, the stoning the Jews had dropped, equal
 * blood for Qurayza and al-Nadir, and "who will protect you from me?". The Prophet ﷺ is never drawn; Abu Bakr is his
 * companion roundel, Sawda a Mother of the Believers' roundel, others plain; people are faceless; no animals (the
 * camel of the necklace is only named).
 */

private fun DrawScope.beads147(c: Offset, r: Float, spread: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 12) {
        val ang = k / 12f * 6.2832f
        val d = r * (1f + spread * (0.4f + 0.6f * ((k * 7) % 5) / 4f))
        drawCircle(Color(0xFFF3E2B0), size.width * 0.009f, Offset(c.x + d * kotlin.math.cos(ang), c.y + d * 0.6f * kotlin.math.sin(ang)), alpha = alpha)
    }
}

// ═════════════ as-sulh: reconciliation between spouses ═════════════
internal fun DrawScope.assulhScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "assulh", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            val apart = seg(p, 0.3f, 0.8f)
            figure(Offset(w * (0.6f + 0.15f * apart), h * 0.8f), h * 0.22f, Qn.Person, f)
            figure(Offset(w * (0.4f - 0.15f * apart), h * 0.8f), h * 0.2f, Qn.Person, f)
            row131(a, "assulh0", 2, h * 0.28f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            val back = seg(p, 0.3f, 0.8f)
            figure(Offset(w * (0.75f - 0.13f * back), h * 0.8f), h * 0.22f, Qn.Person, f)
            figure(Offset(w * (0.25f + 0.13f * back), h * 0.8f), h * 0.2f, Qn.Person, f)
            qIcon("heart", Offset(w * 0.5f, h * 0.48f), w * 0.07f, Qn.Rose, f * seg(p, 0.65f, 0.8f), a.t)
            row131(a, "assulh1", 2, h * 0.24f, p, f, from = 0.1f, step = 0.5f)
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.68f, h * 0.36f), w * 0.09f, a, "assulh2", 0, 3, f * seg(p, 0.02f, 0.18f))
            qIcon("sun", Offset(mix(w * 0.68f, w * 0.32f, seg(p, 0.45f, 0.8f)), h * 0.58f), w * 0.05f, Qn.Gold, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("assulh2", 0, 1), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.4f, 0.55f))
        },
        { p, f ->
            veil131(f)
            val join = seg(p, 0.3f, 0.7f)
            for (k in 0 until 3) qIcon("scroll", Offset(mix(w * (0.25f + 0.25f * k), w * 0.5f, join), h * 0.4f), w * 0.08f, Qn.GoldLight, f * (1f - 0.6f * join * (if (k == 1) 0f else 1f)), a.t)
            softGlow(Offset(w * 0.5f, h * 0.4f), w * 0.12f, Qn.GoldLight, 0.5f * f * join)
            row131(a, "assulh3", 2, h * 0.68f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ as-kalala: the verse of the kalala ═════════════
internal fun DrawScope.askalalaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askalala", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.5f), w * 0.09f, a, "askalala0", 0, f * seg(p, 0.02f, 0.18f))
            close97(Offset(w * 0.76f, h * 0.3f), w * 0.07f, a, "askalala0", 1, 0, f * seg(p, 0.15f, 0.3f))
            for (k in 0 until 4) qIcon("drop", Offset(w * (0.44f + 0.04f * k), h * (0.3f + 0.04f * (k % 2))), w * 0.03f, Color(0xFF8FC7E8), f * seg(p, 0.5f + 0.05f * k, 0.6f + 0.05f * k), a.t)
            tagAt(a.tag("askalala0", 0, 2), Offset(w * 0.5f, h * 0.74f), f * seg(p, 0.75f, 0.88f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) qIcon("coin", Offset(w * (0.62f + 0.06f * k), h * 0.42f), w * 0.04f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("scroll", Offset(w * 0.32f, h * 0.42f), w * 0.11f, Qn.GoldLight, f * seg(p, 0.45f, 0.6f), a.t)
            row131(a, "askalala1", 2, h * 0.68f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            val c = Offset(w * 0.5f, h * 0.46f)
            drawCircle(Qn.GoldLight, w * 0.04f, c, alpha = f)
            for (s in listOf(-1f, 1f)) drawCircle(Qn.GoldLight, w * 0.03f, Offset(c.x + s * w * 0.18f, c.y), alpha = f * seg(p, 0.5f, 0.65f))
            for (s in listOf(-1f, 1f)) drawCircle(Dim145, w * 0.035f, Offset(c.x, c.y + s * h * 0.15f), alpha = f * seg(p, 0.2f, 0.35f), style = Stroke(w * 0.005f))
            tagAt(a.tag("askalala2", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.02f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("askalala2", 0, 1), Offset(w * 0.5f, h * 0.73f), f * seg(p, 0.3f, 0.45f))
            tagAt(a.tag("askalala2", 0, 2), Offset(w * 0.5f, h * 0.86f), f * seg(p, 0.4f, 0.55f))
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) qIcon("scroll", Offset(w * (0.62f - 0.24f * k), h * 0.42f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k), a.t)
            row131(a, "askalala3", 2, h * 0.68f, p, f, from = 0.3f, step = 0.3f)
        },
    )
}

// ═════════════ as-tayammum: the lost necklace ═════════════
internal fun DrawScope.astayammumScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "astayammum", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            beads147(Offset(w * 0.5f, h * 0.5f), w * 0.06f, 2f * seg(p, 0.45f, 0.75f), f * seg(p, 0.1f, 0.25f))
            row131(a, "astayammum0", 2, h * 0.24f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            crowd145(w * 0.5f, h * 0.84f, 5, h * 0.14f, f, gap = 0.1f)
            qIcon("drop", Offset(w * 0.3f, h * 0.42f), w * 0.06f, Color(0xFF8FC7E8), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.3f, h * 0.42f), w * 0.08f, Qn.Rose, f * seg(p, 0.2f, 0.3f), a.t)
            close97(Offset(w * 0.7f, h * 0.38f), w * 0.08f, a, "astayammum1", 1, 0, f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("astayammum1", 0, 0), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.15f, 0.3f))
        },
        { p, f ->
            veil131(f)
            qIcon("hands", Offset(w * 0.5f, h * 0.44f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 10) drawCircle(Color(0xFFC9A46A), w * 0.005f, Offset(w * (0.42f + 0.016f * k), h * (0.5f + 0.02f * ((k * 3) % 4))), alpha = f * seg(p, 0.15f + 0.02f * k, 0.25f + 0.02f * k))
            softGlow(Offset(w * 0.5f, h * 0.44f), w * 0.15f, Qn.GoldLight, 0.4f * f * seg(p, 0.5f, 0.8f))
            row131(a, "astayammum2", 2, h * 0.72f, p, f, from = 0.15f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            drawRect(Color(0xFF4A3A2A), Offset(0f, h * 0.62f), Size(w, h * 0.06f), alpha = f)
            beads147(Offset(w * 0.5f, h * 0.6f), w * 0.05f, 0f, f * seg(p, 0.2f, 0.4f))
            softGlow(Offset(w * 0.5f, h * 0.6f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("astayammum3", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-rajm: the ruling they had let die ═════════════
internal fun DrawScope.asrajmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asrajm", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.4f), w * 0.13f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            figure(Offset(w * 0.25f, h * 0.84f), h * 0.18f, Qn.Person, f * seg(p, 0.1f, 0.25f))
            row131(a, "asrajm0", 2, h * 0.68f, p, f, from = 0.3f, step = 0.3f)
        },
        { p, f ->
            veil131(f)
            val tilt = seg(p, 0.3f, 0.7f)
            val c = Offset(w * 0.5f, h * 0.42f)
            drawLine(Qn.Gold, Offset(c.x - w * 0.22f, c.y + h * 0.05f * tilt), Offset(c.x + w * 0.22f, c.y - h * 0.05f * tilt), w * 0.008f, alpha = f)
            qIcon("crown", Offset(c.x + w * 0.22f, c.y - h * 0.05f * tilt - h * 0.06f), w * 0.06f, Qn.Gold, f, a.t)
            figure(Offset(c.x - w * 0.22f, c.y + h * 0.05f * tilt), h * 0.1f, Qn.Person, f)
            row131(a, "asrajm1", 2, h * 0.7f, p, f, from = 0.25f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.44f), w * 0.13f, Qn.GoldLight, f, a.t)
            qIcon("light", Offset(w * 0.5f, h * 0.26f), w * 0.08f, Qn.Gold, f * seg(p, 0.3f, 0.55f), a.t)
            row131(a, "asrajm2", 2, h * 0.72f, p, f, from = 0.15f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            val swap = seg(p, 0.35f, 0.75f)
            tagAt(a.tag("asrajm3", 0, 0), Offset(w * 0.5f, mix(h * 0.62f, h * 0.4f, swap)), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asrajm3", 0, 1), Offset(w * 0.5f, mix(h * 0.4f, h * 0.62f, swap)), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-qist: a life for a life ═════════════
internal fun DrawScope.asqistScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asqist", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            crowd145(w * 0.72f, h * 0.8f, 3, h * 0.15f, f * seg(p, 0.05f, 0.2f), gap = 0.05f)
            crowd145(w * 0.28f, h * 0.8f, 3, h * 0.2f, f * seg(p, 0.3f, 0.45f), gap = 0.05f)
            tagAt(a.tag("asqist0", 0, 0), Offset(w * 0.72f, h * 0.44f), f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("asqist0", 0, 1), Offset(w * 0.28f, h * 0.4f), f * seg(p, 0.35f, 0.5f))
        },
        { p, f ->
            veil131(f)
            val c = Offset(w * 0.5f, h * 0.44f)
            drawLine(Qn.Gold, Offset(c.x - w * 0.24f, c.y - h * 0.05f), Offset(c.x + w * 0.24f, c.y + h * 0.05f), w * 0.008f, alpha = f)
            qIcon("wheat", Offset(c.x - w * 0.24f, c.y - h * 0.11f), w * 0.07f, Qn.Gold, f * seg(p, 0.5f, 0.65f), a.t)
            figure(Offset(c.x + w * 0.24f, c.y + h * 0.05f), h * 0.1f, Qn.Person, f * seg(p, 0.1f, 0.25f))
            row131(a, "asqist1", 2, h * 0.72f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            val c = Offset(w * 0.5f, h * 0.44f)
            val lv = 1f - seg(p, 0.1f, 0.45f)
            drawLine(Qn.Gold, Offset(c.x - w * 0.24f, c.y - h * 0.05f * lv), Offset(c.x + w * 0.24f, c.y + h * 0.05f * lv), w * 0.008f, alpha = f)
            for (s in listOf(-1f, 1f)) figure(Offset(c.x + s * w * 0.24f, c.y + s * h * 0.05f * lv), h * 0.1f, Qn.Person, f)
            softGlow(c, w * 0.12f, Qn.GoldLight, 0.5f * f * seg(p, 0.45f, 0.7f))
            row131(a, "asqist2", 2, h * 0.72f, p, f, from = 0.3f, step = 0.3f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) qIcon("book", Offset(w * (0.75f - 0.25f * k), h * 0.42f), w * 0.08f, Qn.Gold, f * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k), a.t)
            row131(a, "asqist3", 3, h * 0.64f, p, f, from = 0.1f, step = 0.2f)
        },
    )
}

// ═════════════ as-ismah: who will protect you from me? ═════════════
internal fun DrawScope.asismahScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asismah", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("tree", Offset(w * 0.5f, h * 0.48f), w * (0.16f + 0.06f * seg(p, 0.1f, 0.6f)), Qn.Leaf, f * seg(p, 0.02f, 0.2f), a.t)
            crowd145(w * 0.5f, h * 0.86f, 4, h * 0.1f, f * seg(p, 0.4f, 0.55f), gap = 0.12f)
            tagAt(a.tag("asismah0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.3f, 0.45f))
        },
        { p, f ->
            veil131(f)
            qIcon("tree", Offset(w * 0.45f, h * 0.44f), w * 0.2f, Qn.Leaf, f, a.t)
            val take = seg(p, 0.3f, 0.6f)
            drawLine(Color(0xFFC8CCD6), Offset(mix(w * 0.4f, w * 0.68f, take), h * 0.44f), Offset(mix(w * 0.4f, w * 0.68f, take), h * 0.58f), w * 0.008f, alpha = f)
            figure(Offset(mix(w * 0.88f, w * 0.72f, seg(p, 0.05f, 0.3f)), h * 0.82f), h * 0.22f, Qn.Person, f)
            row131(a, "asismah1", 2, h * 0.18f, p, f, from = 0.1f, step = 0.5f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asismah2", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.02f, 0.15f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.4f), w * 0.25f, Qn.GoldLight, 0.5f * f * seg(p, 0.05f, 0.3f))
            val fall = seg(p, 0.15f, 0.4f)
            drawLine(Color(0xFFC8CCD6), Offset(w * 0.68f, mix(h * 0.44f, h * 0.74f, fall)), Offset(w * (0.68f + 0.14f * fall), mix(h * 0.58f, h * 0.76f, fall)), w * 0.008f, alpha = f)
            tagAt(a.tag("asismah2", 0, 1), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 5) drawCircle(Qn.GoldLight, w * 0.022f, Offset(w * 0.5f, h * (0.2f + 0.08f * k)), alpha = f * seg(p, 0.05f + 0.07f * k, 0.12f + 0.07f * k), style = Stroke(w * 0.005f))
            tagAt(a.tag("asismah3", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}
