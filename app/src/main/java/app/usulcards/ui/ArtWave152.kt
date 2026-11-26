package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 152 (2026-11-26): occasions of revelation from al-Wadi'i, Maryam to al-Nur: Khabbab's debt, Ibn al-Ziba'ra,
 * the duel of Badr, the first permission to fight, the famine of Makka, Marthad, and the two li'an cases. The Prophet
 * ﷺ is never drawn; Jibril is a cool white light; prophets are only named; Abu Bakr is his companion roundel, others
 * plain; people are faceless; the li'an cases show only figures apart, oaths as lights and a scroll, never a scene
 * of the accusation itself.
 */

private fun DrawScope.cave152(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawArc(Color(0xFF3A2E24), 180f, 180f, true, Offset(c.x - s, c.y - s * 0.8f), Size(s * 2f, s * 1.6f), alpha = alpha)
    drawArc(Color(0xFF0E0A08), 180f, 180f, true, Offset(c.x - s * 0.45f, c.y - s * 0.4f), Size(s * 0.9f, s * 0.8f), alpha = alpha)
}

// ═════════════ as-khabbab: Khabbab's debt ═════════════
internal fun DrawScope.askhabbabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askhabbab", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.7f, h * 0.3f), w * 0.08f, a, "askhabbab0", 0, f * seg(p, 0.02f, 0.18f))
            qIcon("flame", Offset(w * 0.32f, h * 0.5f), w * 0.08f, Qn.Ember, f * seg(p, 0.2f, 0.35f), a.t)
            qIcon("coin", Offset(w * 0.5f, h * 0.56f), w * 0.05f, Qn.Gold, f * seg(p, 0.6f, 0.75f), a.t)
            row131(a, "askhabbab0", 2, h * 0.78f, p, f, from = 0.25f, step = 0.35f, first = 1)
        },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.68f, h * 0.82f), h * 0.24f, Dim145, f)
            figure(Offset(w * 0.32f, h * 0.82f), h * 0.2f, Qn.Person, f)
            qIcon("cross", Offset(w * 0.32f, h * 0.48f), w * 0.06f, Qn.Rose, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("askhabbab1", 0, 0), Offset(w * 0.68f, h * 0.36f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ember)
            tagAt(a.tag("askhabbab1", 0, 1), Offset(w * 0.32f, h * 0.3f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("askhabbab2", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.55f, 0.7f))
            for (k in 0 until 4) qIcon("coin", Offset(w * (0.4f + 0.06f * k), h * 0.48f), w * 0.04f, Dim145, f * seg(p, 0.05f, 0.2f) * (1f - 0.6f * seg(p, 0.5f, 0.8f)), a.t)
            qIcon("child", Offset(w * 0.5f, h * 0.62f), w * 0.05f, Dim145, f * seg(p, 0.15f, 0.3f) * (1f - 0.6f * seg(p, 0.5f, 0.8f)), a.t)
            tagAt(a.tag("askhabbab2", 0, 0), Offset(w * 0.5f, h * 0.8f), f * seg(p, 0.1f, 0.25f))
        },
        { p, f ->
            veil131(f)
            drawRect(Color(0xFF6A6A72), Offset(w * 0.38f, h * 0.5f), Size(w * 0.24f, h * 0.06f), alpha = f)
            qIcon("flame", Offset(w * 0.5f, h * 0.42f), w * 0.07f, Qn.Ember, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("askhabbab3", 0, 0), Offset(w * 0.5f, h * 0.74f), f * seg(p, 0.2f, 0.35f))
        },
    )
}

// ═════════════ as-zibara: Ibn al-Ziba'ra ═════════════
internal fun DrawScope.aszibaraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "aszibara", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            crowd145(w * 0.5f, h * 0.82f, 5, h * 0.17f, f, lean = 0.1f * kotlin.math.sin(a.t * 3f), gap = 0.09f)
            row131(a, "aszibara0", 2, h * 0.3f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.5f, h * 0.82f), h * 0.24f, Dim145, f, arms = 0.6f)
            row131(a, "aszibara1", 2, h * 0.3f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            row131(a, "aszibara2", 2, h * 0.3f, p, f, from = 0.05f, step = 0.15f)
            softGlow(Offset(w * 0.5f, h * 0.3f), w * 0.3f, Qn.GoldLight, 0.3f * f * seg(p, 0.5f, 0.8f))
            qIcon("flame", Offset(w * 0.5f, h * 0.72f), w * 0.08f, Qn.Ember, f * seg(p, 0.5f, 0.6f) * 0.6f, a.t)
            tagAt(a.tag("aszibara2", 0, 2), Offset(w * 0.5f, h * 0.52f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) qIcon("chain", Offset(w * (0.7f - 0.2f * k), h * 0.42f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k), a.t)
            tagAt(a.tag("aszibara3", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-khasman: the two parties of Badr ═════════════
internal fun DrawScope.askhasmanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "askhasman", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.32f), w * 0.09f, a, "askhasman0", 0, f * seg(p, 0.02f, 0.18f))
            tagAt(a.tag("askhasman0", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) plain97(Offset(w * 0.78f, h * (0.26f + 0.2f * k)), w * 0.07f, a, "askhasman1", k, f * seg(p, 0.05f + 0.12f * k, 0.15f + 0.12f * k))
            for (k in 0 until 3) tagAt(a.tag("askhasman1", 0, 3 + k), Offset(w * 0.24f, h * (0.26f + 0.2f * k)), f * seg(p, 0.5f + 0.1f * k, 0.6f + 0.1f * k), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("askhasman2", 0, 2), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
            plain97(Offset(w * 0.5f, h * 0.44f), w * 0.09f, a, "askhasman2", 0, f * seg(p, 0.02f, 0.18f))
            tagAt(a.tag("askhasman2", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.2f, 0.35f))
        },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.5f, h * 0.4f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            row131(a, "askhasman3", 2, h * 0.68f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ as-idhn: the first verse on fighting ═════════════
internal fun DrawScope.asidhnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asidhn", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.25f, h * 0.5f), w * 0.09f, Dim145, f, a.t)
            close97(Offset(w * 0.66f, h * 0.36f), w * 0.09f, a, "asidhn0", 0, 0, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asidhn0", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.3f, 0.45f))
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.4f), w * 0.18f, Qn.GoldLight, 0.5f * f * seg(p, 0.05f, 0.3f))
            qIcon("scroll", Offset(w * 0.5f, h * 0.4f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            row131(a, "asidhn1", 2, h * 0.7f, p, f, from = 0.2f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.42f), w * 0.14f, Qn.Gold, f, a.t)
            drawCircle(Qn.GoldLight, w * 0.03f, Offset(w * 0.5f, h * 0.24f), alpha = f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("asidhn2", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 5) if (k != 3) drawCircle(Qn.GoldLight, w * 0.025f, Offset(w * 0.5f, h * (0.16f + 0.08f * k)), alpha = f * seg(p, 0.05f + 0.07f * k, 0.12f + 0.07f * k), style = Stroke(w * 0.005f))
            drawCircle(Qn.Rose, w * 0.025f, Offset(w * 0.5f, h * 0.4f), alpha = f * seg(p, 0.5f, 0.65f), style = Stroke(w * 0.004f))
            row131(a, "asidhn3", 2, h * 0.7f, p, f, from = 0.2f, step = 0.35f)
        },
    )
}

// ═════════════ as-ilhz: the famine of Makka ═════════════
internal fun DrawScope.asilhzScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asilhz", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asilhz0", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.3f, 0.45f))
            figure(Offset(w * 0.5f, h * 0.82f), h * 0.22f, Dim145, f, lean = 0.15f)
            tagAt(a.tag("asilhz0", 0, 0), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.05f, 0.2f))
        },
        { p, f ->
            veil131(f)
            drawArc(Color(0xFF8A7458), 0f, 180f, false, Offset(w * 0.38f, h * 0.36f), Size(w * 0.24f, h * 0.14f), alpha = f, style = Stroke(w * 0.01f))
            crowd145(w * 0.5f, h * 0.84f, 4, h * 0.13f * (1f - 0.2f * seg(p, 0.3f, 0.7f)), f, gap = 0.08f)
            row131(a, "asilhz1", 2, h * 0.24f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            crowd145(w * 0.5f, h * 0.82f, 5, h * 0.2f, f, gap = 0.09f)
            row131(a, "asilhz2", 2, h * 0.3f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) qIcon("book", Offset(w * (0.72f - 0.22f * k), h * 0.38f), w * 0.07f, Qn.Gold, f * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k), a.t)
            row131(a, "asilhz3", 3, h * 0.64f, p, f, from = 0.2f, step = 0.22f)
        },
    )
}

// ═════════════ as-marthad: 'Anaq, Allah has forbidden zina ═════════════
internal fun DrawScope.asmarthadScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asmarthad", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.72f, h * 0.28f), w * 0.08f, a, "asmarthad0", 0, f * seg(p, 0.02f, 0.18f))
            figure(Offset(mix(w * 0.2f, w * 0.6f, seg(p, 0.2f, 0.9f)), h * 0.82f), h * 0.2f, Qn.Person, f, walk = a.t * 3f, lean = 0.2f, face = 1f)
            tagAt(a.tag("asmarthad0", 0, 1), Offset(w * 0.4f, h * 0.5f), f * seg(p, 0.4f, 0.55f))
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asmarthad1", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.02f, 0.15f), ink = ArtInk.Gold)
            cave152(Offset(w * 0.3f, h * 0.7f), w * 0.14f, f * seg(p, 0.3f, 0.45f))
            for (k in 0 until 8) figure(Offset(mix(w * 1.1f, w * 0.55f, seg(p, 0.35f, 0.8f)) + w * 0.045f * k, h * 0.84f), h * 0.12f, Dim145, f * seg(p, 0.35f, 0.45f))
            tagAt(a.tag("asmarthad1", 0, 1), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("cross", Offset(w * 0.5f, h * 0.42f), w * 0.12f, Qn.Rose, f * seg(p, 0.3f, 0.45f), a.t)
            row131(a, "asmarthad2", 2, h * 0.7f, p, f, from = 0.35f, step = 0.3f)
        },
        { p, f ->
            veil131(f)
            qIcon("chain", Offset(w * 0.5f, h * 0.42f), w * 0.09f, Dim145, f * (1f - seg(p, 0.2f, 0.45f)), a.t)
            figure(Offset(mix(w * 0.2f, w * 0.8f, seg(p, 0.3f, 0.95f)), h * 0.82f), h * 0.22f, Qn.Person, f, walk = a.t * 2f, lean = 0.25f, face = 1f)
            row131(a, "asmarthad3", 2, h * 0.24f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ as-uwaymir: 'Uwaymir and 'Asim ═════════════
internal fun DrawScope.asuwaymirScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asuwaymir", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.7f, h * 0.4f), w * 0.09f, a, "asuwaymir0", 0, f * seg(p, 0.02f, 0.18f))
            plain97(Offset(w * 0.3f, h * 0.4f), w * 0.09f, a, "asuwaymir0", 1, f * seg(p, 0.3f, 0.45f))
        },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.42f), w * 0.1f, Dim145, f, a.t)
            row131(a, "asuwaymir1", 2, h * 0.68f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asuwaymir2", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            figure(Offset(w * 0.7f, h * 0.82f), h * 0.22f, Qn.Person, f)
            figure(Offset(w * 0.3f, h * 0.82f), h * 0.2f, Qn.Person, f)
            for (k in 0 until 5) drawCircle(Qn.GoldLight, w * 0.01f, Offset(w * (0.42f + 0.04f * k), h * 0.5f), alpha = f * seg(p, 0.4f + 0.08f * k, 0.48f + 0.08f * k))
            tagAt(a.tag("asuwaymir2", 0, 1), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.35f, 0.5f))
        },
        { p, f ->
            veil131(f)
            figure(Offset(mix(w * 0.6f, w * 0.8f, seg(p, 0.1f, 0.5f)), h * 0.82f), h * 0.22f, Qn.Person, f)
            figure(Offset(mix(w * 0.4f, w * 0.2f, seg(p, 0.1f, 0.5f)), h * 0.82f), h * 0.2f, Qn.Person, f)
            row131(a, "asuwaymir3", 2, h * 0.3f, p, f, from = 0.3f, step = 0.3f)
        },
    )
}

// ═════════════ as-hilal: evidence, or the hadd on your back ═════════════
internal fun DrawScope.ashilalScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ashilal", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.32f), w * 0.09f, a, "ashilal0", 0, f * seg(p, 0.02f, 0.18f))
            qIcon("scale", Offset(w * 0.5f, h * 0.58f), w * 0.1f, Qn.Gold, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("ashilal0", 0, 1), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            angel98(Offset(w * 0.5f, mix(h * 0.06f, h * 0.3f, seg(p, 0.5f, 0.8f))), w * 0.045f, f * seg(p, 0.5f, 0.6f))
            row131(a, "ashilal1", 2, h * 0.66f, p, f, from = 0.05f, step = 0.3f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 5) drawCircle(if (k == 4) Qn.Ember else Qn.GoldLight, w * 0.03f, Offset(w * (0.74f - 0.12f * k), h * 0.4f), alpha = f * seg(p, 0.05f + 0.08f * k, 0.12f + 0.08f * k) * (if (k == 4) 0.6f + 0.4f * kotlin.math.sin(a.t * 4f) else 1f))
            row131(a, "ashilal2", 3, h * 0.66f, p, f, from = 0.3f, step = 0.2f)
        },
        { p, f ->
            veil131(f)
            qIcon("hands", Offset(w * 0.5f, h * 0.42f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.4f, 0.55f), a.t)
            row131(a, "ashilal3", 2, h * 0.7f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}
