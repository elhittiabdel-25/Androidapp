package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 149 (2026-11-26): occasions of revelation from al-Wadi'i, al-Anfal and al-Tawba: Abu Jahl's prayer, the
 * spoils made lawful, the boast of water for the pilgrims, Abu Dharr at al-Rabadha, the mockers of Tabuk, the man with
 * a devil's eyes, the mockers of alms and Ibn Ubayy's shirt. The Prophet ﷺ is never drawn: where he is present he is
 * only a soft named light; 'Umar and 'Uthman are their companion roundels, others plain; people are faceless; no
 * animals (the camel of Tabuk is only named).
 */

private fun DrawScope.pile149(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (r in 0 until 3) for (k in 0..(2 - r)) qIcon("coin", Offset(c.x + s * (k - (2 - r) / 2f) * 0.5f, c.y - s * 0.35f * r), s * 0.45f, Qn.Gold, alpha)
}

private fun DrawScope.shirt149(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(Color(0xFFEDE6D6), Offset(c.x - s * 0.35f, c.y - s * 0.4f), Size(s * 0.7f, s * 0.9f), alpha = alpha)
    drawRect(Color(0xFFEDE6D6), Offset(c.x - s * 0.7f, c.y - s * 0.4f), Size(s * 1.4f, s * 0.25f), alpha = alpha)
}

// ═════════════ as-abujahl: Abu Jahl's prayer ═════════════
internal fun DrawScope.asabujahlScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asabujahl", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            drawRect(Color(0xFF2A1A20), Offset(0f, 0f), Size(w, h * 0.4f), alpha = 0.5f * f * seg(p, 0.2f, 0.6f))
            figure(Offset(w * 0.5f, h * 0.82f), h * 0.22f, Dim145, f, arms = 1f)
            row131(a, "asabujahl0", 2, h * 0.3f, p, f, from = 0.05f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.3f, Qn.GoldLight, 0.5f * f * seg(p, 0.02f, 0.3f))
            crowd145(w * 0.5f, h * 0.84f, 6, h * 0.14f, f, gap = 0.12f)
            row131(a, "asabujahl1", 2, h * 0.24f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.5f, h * 0.48f), w * 0.11f, Dim145, f, a.t)
            for (k in 0 until 10) {
                val ang = k / 10f * 6.2832f + a.t * 0.4f
                drawCircle(Dim145, w * 0.01f, Offset(w * 0.5f + w * 0.26f * kotlin.math.cos(ang), h * 0.5f + h * 0.12f * kotlin.math.sin(ang)), alpha = f)
            }
            tagAt(a.tag("asabujahl2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("asabujahl2", 0, 1), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            val join = seg(p, 0.3f, 0.7f)
            for (s in listOf(-1f, 1f)) qIcon("scroll", Offset(w * (0.5f + 0.2f * s * (1f - join)), h * 0.42f), w * 0.09f, Qn.GoldLight, f, a.t)
            row131(a, "asabujahl3", 2, h * 0.7f, p, f, from = 0.2f, step = 0.4f)
        },
    )
}

// ═════════════ as-ghanima: spoils made lawful ═════════════
internal fun DrawScope.asghanimaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asghanima", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            pile149(Offset(w * 0.3f, h * 0.7f), w * 0.1f, f)
            crowd145(mix(w * 0.8f, w * 0.55f, seg(p, 0.2f, 0.8f)), h * 0.82f, 3, h * 0.16f, f, lean = -0.15f, gap = 0.06f)
            row131(a, "asghanima0", 2, h * 0.26f, p, f, from = 0.05f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            pile149(Offset(w * 0.5f, h * 0.56f), w * 0.12f, f)
            softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.16f, Qn.Jade, 0.6f * f * seg(p, 0.4f, 0.7f))
            row131(a, "asghanima1", 2, h * 0.24f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            pile149(Offset(w * 0.5f, h * 0.62f), w * 0.12f, f * (1f - seg(p, 0.55f, 0.85f)))
            qIcon("flame", Offset(w * 0.5f, mix(h * 0.15f, h * 0.55f, seg(p, 0.35f, 0.6f))), w * 0.1f, Qn.Ember, f * seg(p, 0.35f, 0.45f) * (1f - seg(p, 0.85f, 1f)), a.t)
            row131(a, "asghanima2", 2, h * 0.84f, p, f, from = 0.05f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asghanima3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            pile149(Offset(w * 0.5f, h * 0.62f), w * 0.12f, f)
            softGlow(Offset(w * 0.5f, h * 0.56f), w * 0.15f, Qn.GoldLight, 0.4f * f * seg(p, 0.2f, 0.5f))
        },
    )
}

// ═════════════ as-siqaya: giving water to the pilgrims ═════════════
internal fun DrawScope.assiqayaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "assiqaya", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.72f, h * 0.3f), w * 0.08f, a, "assiqaya0", 0, f * seg(p, 0.02f, 0.18f))
            qIcon("cup", Offset(w * 0.32f, h * 0.42f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("assiqaya0", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.45f, 0.6f))
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.7f, h * 0.42f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("light", Offset(w * 0.3f, h * 0.42f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("assiqaya1", 0, 0), Offset(w * 0.7f, h * 0.66f), f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("assiqaya1", 0, 1), Offset(w * 0.3f, h * 0.66f), f * seg(p, 0.55f, 0.7f))
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.5f, h * 0.3f), w * 0.09f, a, "assiqaya2", 0, 1, f * seg(p, 0.02f, 0.18f))
            row131(a, "assiqaya2", 2, h * 0.62f, p, f, from = 0.25f, step = 0.35f, first = 1)
        },
        { p, f ->
            veil131(f)
            val lv = seg(p, 0.3f, 0.7f)
            val c = Offset(w * 0.5f, h * 0.46f)
            drawLine(Qn.Gold, Offset(c.x - w * 0.24f, c.y + h * 0.05f * lv), Offset(c.x + w * 0.24f, c.y - h * 0.05f * lv), w * 0.008f, alpha = f)
            qIcon("light", Offset(c.x - w * 0.24f, c.y + h * 0.05f * lv - h * 0.06f), w * 0.06f, Qn.GoldLight, f, a.t)
            qIcon("cup", Offset(c.x + w * 0.24f, c.y - h * 0.05f * lv - h * 0.06f), w * 0.05f, Dim145, f, a.t)
            row131(a, "assiqaya3", 3, h * 0.74f, p, f, from = 0.1f, step = 0.25f)
        },
    )
}

// ═════════════ as-kanz: Abu Dharr at al-Rabadha ═════════════
internal fun DrawScope.askanzScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askanz", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.3f, h * 0.6f), w * 0.09f, Dim145, f, a.t)
            plain97(Offset(w * 0.66f, h * 0.36f), w * 0.09f, a, "askanz0", 0, f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("askanz0", 0, 1), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.3f, 0.45f))
        },
        { p, f ->
            veil131(f)
            pile149(Offset(w * 0.5f, h * 0.5f), w * 0.1f, f)
            qIcon("lock", Offset(w * 0.5f, h * 0.4f), w * 0.06f, Dim145, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("askanz1", 0, 0), Offset(w * 0.75f, h * 0.22f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("askanz1", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.3f, 0.45f))
            tagAt(a.tag("askanz1", 0, 2), Offset(w * 0.5f, h * 0.82f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.5f, h * 0.32f), w * 0.09f, a, "askanz2", 0, 2, f * seg(p, 0.02f, 0.18f))
            crowd145(w * 0.5f, h * 0.84f, 5, h * 0.13f, f * seg(p, 0.3f, 0.5f), gap = 0.07f)
            tagAt(a.tag("askanz2", 0, 1), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.6f, 0.75f))
        },
        { p, f ->
            veil131(f)
            qIcon("hands", Offset(w * 0.5f, h * 0.42f), w * 0.11f, Qn.Gold, f * seg(p, 0.05f, 0.25f), a.t)
            tagAt(a.tag("askanz3", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-khawd: we were only chatting and playing ═════════════
internal fun DrawScope.askhawdScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askhawd", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            crowd145(w * 0.5f, h * 0.82f, 5, h * 0.15f, f, gap = 0.1f)
            row131(a, "askhawd0", 2, h * 0.3f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            figure(Offset(mix(w * 0.3f, w * 0.7f, seg(p, 0.05f, 0.9f)), h * 0.82f), h * 0.2f, Dim145, f, walk = a.t * 5f, lean = 0.15f, face = 1f)
            for (k in 0 until 6) drawCircle(Color(0xFF8A7458), w * 0.008f, Offset(w * (0.2f + 0.11f * k), h * (0.86f + 0.01f * (k % 2))), alpha = f * seg(p, 0.2f + 0.08f * k, 0.3f + 0.08f * k))
            row131(a, "askhawd1", 2, h * 0.3f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("askhawd2", 0, 1), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            qIcon("scroll", Offset(w * 0.5f, h * 0.46f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("askhawd2", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.4f, 0.55f))
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) qIcon("book", Offset(w * (0.62f - 0.24f * k), h * 0.4f), w * 0.08f, Qn.Gold, f * seg(p, 0.05f + 0.2f * k, 0.2f + 0.2f * k), a.t)
            row131(a, "askhawd3", 3, h * 0.66f, p, f, from = 0.1f, step = 0.25f)
        },
    )
}

// ═════════════ as-shaytan: with a devil's eyes ═════════════
internal fun DrawScope.asshaytanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asshaytan", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("tree", Offset(w * 0.5f, h * 0.5f), w * 0.2f, Qn.Leaf, f * seg(p, 0.02f, 0.15f), a.t)
            crowd145(w * 0.5f, h * 0.84f, 4, h * 0.12f, f, gap = 0.1f)
            row131(a, "asshaytan0", 2, h * 0.2f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            figure(Offset(mix(w * 0.95f, w * 0.65f, seg(p, 0.02f, 0.3f)), h * 0.82f), h * 0.22f, Color(0xFF8FA8D8), f, walk = a.t * 3f)
            row131(a, "asshaytan1", 2, h * 0.3f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asshaytan2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f))
            crowd145(w * 0.5f, h * 0.82f, 4, h * 0.2f, f, gap = 0.08f)
            qIcon("cross", Offset(w * 0.5f, h * 0.44f), w * 0.08f, Qn.Rose, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("asshaytan2", 0, 1), Offset(w * 0.5f, h * 0.32f), f * seg(p, 0.35f, 0.5f))
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) qIcon("book", Offset(w * (0.62f - 0.24f * k), h * 0.42f), w * 0.08f, Qn.Gold, f * seg(p, 0.05f + 0.2f * k, 0.2f + 0.2f * k), a.t)
            row131(a, "asshaytan3", 2, h * 0.68f, p, f, from = 0.2f, step = 0.35f)
        },
    )
}

// ═════════════ as-lamz: we used to carry loads ═════════════
internal fun DrawScope.aslamzScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "aslamz", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) {
                val x = mix(w * (0.1f + 0.15f * k), w * (0.5f + 0.15f * k), seg(p, 0.1f, 0.9f))
                figure(Offset(x, h * 0.82f), h * 0.2f, Qn.Person, f, walk = a.t * 3f, lean = 0.2f)
                drawRect(Color(0xFFC9A46A), Offset(x - w * 0.04f, h * 0.58f), Size(w * 0.06f, h * 0.06f), alpha = f)
            }
            row131(a, "aslamz0", 2, h * 0.26f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            pile149(Offset(w * 0.5f, h * 0.56f), w * 0.12f, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("aslamz1", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("aslamz1", 0, 1), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("aslamz2", 0, 1), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ember)
            qIcon("cup", Offset(w * 0.5f, h * 0.5f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("aslamz2", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.1f, 0.25f))
        },
        { p, f ->
            veil131(f)
            qIcon("cup", Offset(w * 0.5f, h * 0.46f), w * 0.08f, Qn.GoldLight, f, a.t)
            softGlow(Offset(w * 0.5f, h * 0.46f), w * 0.15f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.6f))
            row131(a, "aslamz3", 2, h * 0.72f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ as-qamis: Ibn Ubayy's shirt ═════════════
internal fun DrawScope.asqamisScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asqamis", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.3f, h * 0.82f), h * 0.22f, Qn.Person, f)
            shirt149(Offset(mix(w * 0.65f, w * 0.42f, seg(p, 0.4f, 0.8f)), h * 0.5f), w * 0.09f, f * seg(p, 0.2f, 0.35f))
            row131(a, "asqamis0", 2, h * 0.22f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.5f, h * 0.32f), w * 0.09f, a, "asqamis1", 0, 1, f * seg(p, 0.02f, 0.18f))
            tagAt(a.tag("asqamis1", 0, 1), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("mat", Offset(w * 0.5f, h * 0.42f), w * 0.12f, Dim145, f, a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.42f), w * 0.14f, Qn.Rose, f * seg(p, 0.3f, 0.45f), a.t)
            row131(a, "asqamis2", 2, h * 0.7f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            for (s in listOf(-1f, 1f)) drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.7f), Offset(w * (0.5f + 0.22f * s), h * 0.4f), w * 0.006f, alpha = f * seg(p, 0.05f, 0.3f))
            tagAt(a.tag("asqamis3", 0, 0), Offset(w * 0.72f, h * 0.32f), f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("asqamis3", 0, 1), Offset(w * 0.28f, h * 0.32f), f * seg(p, 0.45f, 0.6f))
        },
    )
}
