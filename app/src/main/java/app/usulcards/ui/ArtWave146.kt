package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 146 (2026-11-24): occasions of revelation from al-Wadi'i, al-Nisa': the commander's fire, al-Zubayr's water,
 * the man who feared not to see the Prophet ﷺ in Paradise, the order to hold back, 'Umar and the rumour of the
 * divorce, the two parties at Uhud, the greeting of peace and those excused from fighting. The Prophet ﷺ is never
 * drawn; 'Umar is his companion roundel, others plain roundels; people are faceless; no animals (the man's flock is
 * only named); Jibril is a cool white light.
 */

private val Water146 = Color(0xFF4F8FB8)

// ═════════════ as-ulilamr: obedience is in what is right ═════════════
internal fun DrawScope.asulilamrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asulilamr", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.28f), w * 0.1f, a, "asulilamr0", 0, f * seg(p, 0.02f, 0.18f))
            crowd145(w * 0.5f, h * 0.82f, 5, h * 0.17f, f * seg(p, 0.3f, 0.45f), gap = 0.08f)
            tagAt(a.tag("asulilamr0", 0, 1), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.45f, 0.6f))
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 5) drawLine(Color(0xFF8A6A40), Offset(w * (0.4f + 0.05f * k), h * 0.62f), Offset(w * (0.45f + 0.03f * k), h * 0.54f), w * 0.012f, alpha = f * seg(p, 0.05f + 0.05f * k, 0.12f + 0.05f * k))
            qIcon("flame", Offset(w * 0.5f, h * 0.5f), w * (0.06f + 0.06f * seg(p, 0.35f, 0.6f)), Qn.Ember, f * seg(p, 0.35f, 0.45f), a.t)
            crowd145(w * 0.5f, h * 0.84f, 5, h * 0.15f, f, gap = 0.09f)
            row131(a, "asulilamr1", 3, h * 0.22f, p, f, from = 0.1f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            val dim = 1f - seg(p, 0.4f, 0.8f)
            qIcon("flame", Offset(w * 0.5f, h * 0.5f), w * (0.04f + 0.08f * dim), Qn.Ember, f * (0.2f + 0.8f * dim), a.t)
            crowd145(w * 0.5f, h * 0.84f, 5, h * 0.15f, f, lean = -0.15f, gap = 0.06f)
            row131(a, "asulilamr2", 2, h * 0.22f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.42f), w * 0.2f, Qn.GoldLight, 0.5f * f * seg(p, 0.05f, 0.3f))
            qIcon("check", Offset(w * 0.5f, h * 0.42f), w * 0.1f, Qn.Jade, f * seg(p, 0.1f, 0.3f), a.t)
            tagAt(a.tag("asulilamr3", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-zubayr: water your garden, al-Zubayr ═════════════
internal fun DrawScope.aszubayrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "aszubayr", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.72f, h * 0.26f), w * 0.08f, a, "aszubayr0", 0, f * seg(p, 0.02f, 0.18f))
            val run = seg(p, 0.25f, 0.8f)
            drawLine(Water146, Offset(w * 0.9f, h * 0.6f), Offset(mix(w * 0.9f, w * 0.1f, run), h * 0.6f), w * 0.02f, alpha = f)
            for (k in 0 until 3) qIcon("sprout", Offset(w * (0.75f - 0.08f * k), h * 0.52f), w * 0.05f, Qn.Leaf, f * seg(p, 0.3f, 0.45f), a.t)
            for (k in 0 until 3) qIcon("sprout", Offset(w * (0.3f - 0.08f * k), h * 0.52f), w * 0.05f, Qn.Leaf, f * seg(p, 0.6f, 0.75f), a.t)
            tagAt(a.tag("aszubayr0", 0, 1), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.15f, 0.3f))
        },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.35f, h * 0.78f), h * 0.24f, Qn.Person, f * seg(p, 0.02f, 0.18f), lean = 0.1f)
            tagAt(a.tag("aszubayr1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.2f, 0.35f))
        },
        { p, f ->
            veil131(f)
            drawRect(Color(0xFF8A7458), Offset(w * 0.6f, h * 0.36f), Size(w * 0.03f, h * 0.28f), alpha = f)
            val rise = seg(p, 0.2f, 0.75f)
            drawRect(Water146, Offset(w * 0.3f, h * (0.64f - 0.24f * rise)), Size(w * 0.3f, h * 0.24f * rise), alpha = 0.7f * f)
            qIcon("tree", Offset(w * 0.45f, h * 0.44f), w * 0.07f, Qn.Leaf, f, a.t)
            row131(a, "aszubayr2", 2, h * 0.8f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.36f), w * 0.08f, a, "aszubayr0", 0, f * seg(p, 0.02f, 0.15f))
            qIcon("scroll", Offset(w * 0.32f, h * 0.38f), w * 0.11f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            row131(a, "aszubayr3", 2, h * 0.66f, p, f, from = 0.3f, step = 0.3f)
        },
    )
}

// ═════════════ as-maiyya: I feared I would not see you ═════════════
internal fun DrawScope.asmaiyyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asmaiyya", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.36f), w * (0.08f + 0.04f * seg(p, 0.2f, 0.9f)), Qn.Rose, f * seg(p, 0.02f, 0.15f), a.t)
            row131(a, "asmaiyya0", 4, h * 0.66f, p, f, from = 0.15f, step = 0.18f)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.14f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.25f))
            figure(Offset(w * 0.5f, h * 0.84f), h * 0.2f, Qn.Person, f, lean = -0.1f)
            for (k in 0 until 5) drawCircle(Qn.GoldLight, w * 0.006f, Offset(w * 0.5f, h * (0.6f - 0.09f * k)), alpha = f * seg(p, 0.3f + 0.05f * k, 0.38f + 0.05f * k) * (1f - 0.18f * k))
            row131(a, "asmaiyya1", 2, h * 0.36f, p, f, from = 0.2f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            angel98(Offset(w * 0.5f, h * 0.3f), w * 0.05f, f * seg(p, 0.02f, 0.2f))
            tagAt(a.tag("asmaiyya2", 0, 0), Offset(w * 0.5f, h * 0.48f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("asmaiyya2", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.75f, h * 0.56f), w * 0.1f, Dim145, f, a.t)
            figure(Offset(mix(w * 0.7f, w * 0.4f, seg(p, 0.2f, 0.8f)), h * 0.8f), h * 0.18f, Qn.Person, f, walk = a.t * 3f)
            softGlow(Offset(w * 0.25f, h * 0.5f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.4f, 0.7f))
            row131(a, "asmaiyya3", 2, h * 0.24f, p, f, from = 0.1f, step = 0.4f)
        },
    )
}

// ═════════════ as-kuffu: I was commanded to pardon ═════════════
internal fun DrawScope.askuffuScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askuffu", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.26f), w * 0.1f, a, "askuffu0", 0, f * seg(p, 0.02f, 0.18f))
            crowd145(w * 0.5f, h * 0.84f, 4, h * 0.16f * (1f - 0.25f * seg(p, 0.55f, 0.8f)), f)
            row131(a, "askuffu0", 2, h * 0.56f, p, f, from = 0.25f, step = 0.3f, first = 1)
        },
        { p, f ->
            veil131(f)
            qIcon("hands", Offset(w * 0.5f, h * 0.42f), w * 0.12f, Qn.Gold, f * seg(p, 0.05f, 0.25f), a.t)
            row131(a, "askuffu1", 2, h * 0.68f, p, f, from = 0.2f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.8f, h * 0.5f), w * 0.08f, Dim145, f, a.t)
            for (k in 0 until 3) qIcon("house", Offset(w * (0.15f + 0.1f * k), h * 0.5f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            drawLine(Qn.GoldLight, Offset(w * 0.72f, h * 0.5f), Offset(mix(w * 0.72f, w * 0.4f, seg(p, 0.05f, 0.3f)), h * 0.5f), w * 0.006f, alpha = f)
            row131(a, "askuffu2", 3, h * 0.74f, p, f, from = 0.2f, step = 0.2f)
        },
        { p, f ->
            veil131(f)
            dots145(5, w * 0.5f, h * 0.34f, 5, Qn.GoldLight, p, f, from = 0.05f)
            tagAt(a.tag("askuffu3", 0, 0), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.15f, 0.3f))
            row131(a, "askuffu3", 2, h * 0.68f, p, f, from = 0.4f, step = 0.25f, first = 1)
        },
    )
}

// ═════════════ as-istinbat: he has not divorced his wives ═════════════
internal fun DrawScope.asistinbatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asistinbat", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            crowd145(w * 0.5f, h * 0.8f, 5, h * 0.12f, f, lean = 0.25f, gap = 0.12f)
            for (k in 0 until 9) drawCircle(Dim145, w * 0.006f, Offset(w * (0.25f + 0.06f * k), h * 0.82f), alpha = f * seg(p, 0.1f + 0.03f * k, 0.15f + 0.03f * k))
            close97(Offset(w * 0.82f, h * 0.3f), w * 0.08f, a, "asistinbat0", 2, 1, f * seg(p, 0.02f, 0.15f))
            row131(a, "asistinbat0", 2, h * 0.5f, p, f, from = 0.2f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            qIcon("mat", Offset(w * 0.5f, h * 0.38f), w * 0.16f, Color(0xFFB89A6A), f * seg(p, 0.02f, 0.2f), a.t)
            qIcon("wheat", Offset(w * 0.78f, h * 0.36f), w * 0.06f, Qn.Gold, f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("drop", Offset(w * 0.22f, h * 0.36f), w * 0.05f, Water146, f * seg(p, 0.65f, 0.8f), a.t)
            row131(a, "asistinbat1", 3, h * 0.66f, p, f, from = 0.1f, step = 0.27f)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.7f, h * 0.38f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.1f, 0.3f))
            for (k in 0 until 3) qIcon("coin", Offset(w * (0.24f + 0.06f * k), h * 0.42f), w * 0.04f, Dim145, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("asistinbat2", 0, 0), Offset(w * 0.7f, h * 0.62f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("asistinbat2", 0, 1), Offset(w * 0.3f, h * 0.62f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asistinbat3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            close97(Offset(w * 0.5f, h * 0.5f), w * 0.09f, a, "asistinbat0", 2, 1, f * seg(p, 0.02f, 0.15f))
            for (k in 1..3) drawCircle(Qn.GoldLight, w * (0.1f + 0.05f * k), Offset(w * 0.5f, h * 0.5f), alpha = 0.5f * f * seg(p, 0.2f + 0.1f * k, 0.3f + 0.1f * k) * (1f - seg(p, 0.6f + 0.1f * k, 0.9f)), style = androidx.compose.ui.graphics.drawscope.Stroke(w * 0.004f))
        },
    )
}

// ═════════════ as-fiatayn: two parties on the day of Uhud ═════════════
internal fun DrawScope.asfiataynScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asfiatayn", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("mountain", Offset(w * 0.25f, h * 0.4f), w * 0.18f, Dim145, f, a.t)
            crowd145(w * 0.5f, h * 0.82f, 4, h * 0.15f, f, gap = 0.06f)
            for (k in 0 until 2) figure(Offset(mix(w * 0.62f, w * 0.88f, seg(p, 0.3f, 0.9f)) + Offset(w * 0.05f * k, 0f).x, h * 0.82f), h * 0.15f, Qn.Person, f * 0.6f, walk = a.t * 3f)
            row131(a, "asfiatayn0", 2, h * 0.2f, p, f, from = 0.05f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            crowd145(w * 0.72f, h * 0.8f, 3, h * 0.16f, f * seg(p, 0.05f, 0.2f), gap = 0.05f)
            crowd145(w * 0.28f, h * 0.8f, 3, h * 0.16f, f * seg(p, 0.4f, 0.55f), gap = 0.05f)
            tagAt(a.tag("asfiatayn1", 0, 0), Offset(w * 0.72f, h * 0.44f), f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("asfiatayn1", 0, 1), Offset(w * 0.28f, h * 0.44f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            val clean = seg(p, 0.3f, 0.8f)
            drawRect(Color(0xFF6A5A4A), Offset(w * 0.35f, h * 0.44f), Size(w * 0.3f, h * 0.05f), alpha = f * (1f - clean))
            drawRect(Color(0xFFD8D8E0), Offset(w * 0.35f, h * 0.44f), Size(w * 0.3f, h * 0.05f), alpha = f * clean)
            qIcon("flame", Offset(w * 0.5f, h * 0.56f), w * 0.07f, Qn.Ember, f * seg(p, 0.1f, 0.25f), a.t)
            row131(a, "asfiatayn2", 3, h * 0.24f, p, f, from = 0.05f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) qIcon("book", Offset(w * (0.65f - 0.3f * k), h * 0.4f), w * 0.09f, Qn.Gold, f * seg(p, 0.05f + 0.2f * k, 0.2f + 0.2f * k), a.t)
            row131(a, "asfiatayn3", 3, h * 0.66f, p, f, from = 0.1f, step = 0.25f)
        },
    )
}

// ═════════════ as-salam: peace be upon you ═════════════
internal fun DrawScope.assalamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "assalam", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.3f, h * 0.8f), h * 0.22f, Qn.Person, f)
            crowd145(w * 0.72f, h * 0.8f, 3, h * 0.2f, f * seg(p, 0.1f, 0.3f), gap = 0.06f)
            softGlow(Offset(w * 0.3f, h * 0.48f), w * 0.06f, Qn.Jade, f * seg(p, 0.5f, 0.65f))
            row131(a, "assalam0", 2, h * 0.24f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 4) qIcon("coin", Offset(w * (0.38f + 0.08f * k), h * 0.44f), w * 0.04f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.44f), w * 0.16f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            row131(a, "assalam1", 2, h * 0.7f, p, f, from = 0.2f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 4) qIcon("coin", Offset(w * (0.38f + 0.08f * k), h * 0.44f - h * 0.08f * seg(p, 0.3f, 0.8f)), w * 0.04f, Qn.Gold, f * (1f - seg(p, 0.3f, 0.8f)), a.t)
            row131(a, "assalam2", 2, h * 0.7f, p, f, from = 0.1f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            for (s in listOf(-1f, 1f)) qIcon("scroll", Offset(w * (0.5f + 0.18f * s * (1f - seg(p, 0.5f, 0.8f))), h * 0.4f), w * 0.09f, Qn.GoldLight, f, a.t)
            row131(a, "assalam3", 2, h * 0.22f, p, f, from = 0.05f, step = 0.25f)
            tagAt(a.tag("assalam3", 0, 2), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-darar: if I could fight, I would ═════════════
internal fun DrawScope.asdararScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asdarar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.72f, h * 0.3f), w * 0.09f, a, "asdarar0", 0, f * seg(p, 0.02f, 0.18f))
            qIcon("scroll", Offset(w * 0.32f, h * 0.36f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            qIcon("pen", Offset(w * 0.38f, h * 0.32f), w * 0.05f, Qn.Gold, f * seg(p, 0.25f, 0.35f), a.t)
            row131(a, "asdarar0", 2, h * 0.66f, p, f, from = 0.45f, step = 0.2f, first = 1)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.3f), w * 0.09f, a, "asdarar1", 0, f * seg(p, 0.02f, 0.18f))
            figure(Offset(w * 0.5f, h * 0.84f), h * 0.18f, Qn.Person, f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("asdarar1", 0, 1), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.35f, 0.5f))
        },
        { p, f ->
            veil131(f)
            angel98(Offset(w * 0.5f, h * 0.2f), w * 0.04f, f * seg(p, 0.02f, 0.2f) * (1f - 0.6f * seg(p, 0.55f, 0.8f)))
            qIcon("scroll", Offset(w * 0.5f, h * 0.44f), w * 0.12f, Qn.GoldLight, f, a.t)
            softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.6f, 0.8f))
            row131(a, "asdarar2", 3, h * 0.7f, p, f, from = 0.1f, step = 0.27f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asdarar3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            figure(Offset(w * 0.5f, h * 0.84f), h * 0.22f, Qn.Person, f)
        },
    )
}
