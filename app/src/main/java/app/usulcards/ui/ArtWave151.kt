package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 151 (2026-11-26): occasions of revelation from al-Wadi'i, al-Ra'd to Maryam: the thunderbolt, the firm word in
 * the grave, 'Uthman and his freedman, the dead of Uhud, the jinn who were worshipped, the question about the soul,
 * the voice of recitation and Jibril's visits. The Prophet ﷺ is never drawn; Jibril is a cool white light; the jinn
 * are only faint smoke-coloured lights; 'Uthman is his companion roundel, others plain; people are faceless; the
 * martyrs of Uhud are only dots of light, never wounds.
 */

private val Smoke151 = Color(0xFFB59A86)

private fun DrawScope.bolt151(top: Offset, bottom: Offset, alpha: Float) {
    if (alpha <= 0.01f) return
    val dx = (bottom.x - top.x)
    val dy = (bottom.y - top.y)
    val p = Path().apply {
        moveTo(top.x, top.y)
        lineTo(top.x + dx * 0.3f + size.width * 0.03f, top.y + dy * 0.35f)
        lineTo(top.x + dx * 0.45f - size.width * 0.02f, top.y + dy * 0.5f)
        lineTo(bottom.x, bottom.y)
    }
    drawPath(p, Color(0xFFFFF2B0), alpha = alpha, style = Stroke(size.width * 0.008f))
}

private fun DrawScope.waves151(c: Offset, n: Int, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (k in 1..n) drawCircle(Qn.GoldLight, s * k, c, alpha = alpha * (1f - 0.2f * k), style = Stroke(size.width * 0.004f))
}

// ═════════════ as-saiqa: the thunderbolt ═════════════
internal fun DrawScope.assaiqaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "assaiqa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.68f, h * 0.82f), h * 0.24f, Dim145, f)
            qIcon("crown", Offset(w * 0.68f, h * 0.5f), w * 0.05f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            figure(Offset(mix(w * 0.1f, w * 0.38f, seg(p, 0.05f, 0.4f)), h * 0.82f), h * 0.2f, Qn.Person, f, walk = a.t * 3f)
            row131(a, "assaiqa0", 2, h * 0.26f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            qIcon("cloud", Offset(w * 0.6f, h * 0.2f), w * 0.16f, Dim145, f * seg(p, 0.1f, 0.3f), a.t)
            bolt151(Offset(w * 0.6f, h * 0.26f), Offset(w * 0.66f, h * 0.7f), f * seg(p, 0.55f, 0.6f) * (1f - seg(p, 0.75f, 0.9f)))
            drawRect(Color(0xFFFFF7D0), Offset.Zero, size, alpha = 0.25f * f * seg(p, 0.55f, 0.6f) * (1f - seg(p, 0.6f, 0.75f)))
            figure(Offset(w * 0.66f, h * 0.82f), h * 0.22f, Dim145, f * (1f - seg(p, 0.6f, 0.8f)))
            row131(a, "assaiqa1", 2, h * 0.9f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) {
                qIcon("cloud", Offset(w * (0.25f + 0.25f * k), h * 0.2f), w * 0.12f, Dim145, f, a.t)
                bolt151(Offset(w * (0.25f + 0.25f * k), h * 0.24f), Offset(w * (0.28f + 0.25f * k), h * 0.55f), f * seg(p, 0.2f + 0.15f * k, 0.25f + 0.15f * k) * (1f - seg(p, 0.3f + 0.15f * k, 0.4f + 0.15f * k)))
            }
            row131(a, "assaiqa2", 2, h * 0.74f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) qIcon("book", Offset(w * (0.62f - 0.24f * k), h * 0.38f), w * 0.08f, Qn.Gold, f * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k), a.t)
            row131(a, "assaiqa3", 3, h * 0.64f, p, f, from = 0.2f, step = 0.22f)
        },
    )
}

// ═════════════ as-tathbit: firm in the grave ═════════════
internal fun DrawScope.astathbitScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "astathbit", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.28f), w * 0.09f, a, "astathbit0", 0, f * seg(p, 0.02f, 0.18f))
            drawOval(Color(0xFF5A4632), Offset(w * 0.32f, h * 0.7f), Size(w * 0.36f, h * 0.1f), alpha = f * seg(p, 0.3f, 0.45f))
            tagAt(a.tag("astathbit0", 0, 1), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.45f, 0.6f))
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("astathbit1", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            drawOval(Color(0xFF5A4632), Offset(w * 0.32f, h * 0.7f), Size(w * 0.36f, h * 0.1f), alpha = f)
            softGlow(Offset(w * 0.5f, h * 0.72f), w * 0.15f, Qn.GoldLight, 0.6f * f * seg(p, 0.45f, 0.7f))
            tagAt(a.tag("astathbit1", 0, 0), Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("astathbit1", 0, 2), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("astathbit2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            lightShafts(Offset(w * 0.5f, h * 0.02f), 1.5708f, 0.4f, h * 0.7f, Qn.GoldLight, 0.3f * f * seg(p, 0.1f, 0.4f), a.t)
            figure(Offset(w * 0.5f, h * 0.82f), h * 0.2f, Qn.Person, f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("astathbit3", 0, 1), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            qIcon("light", Offset(w * 0.5f, h * 0.48f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("astathbit3", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.1f, 0.25f))
        },
    )
}

// ═════════════ as-abkam: 'Uthman and his freedman ═════════════
internal fun DrawScope.asabkamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asabkam", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.5f, h * 0.34f), w * 0.1f, a, "asabkam0", 0, 2, f * seg(p, 0.02f, 0.18f))
            qIcon("scale", Offset(w * 0.5f, h * 0.62f), w * 0.1f, Qn.Gold, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("asabkam0", 0, 1), Offset(w * 0.5f, h * 0.8f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.75f, h * 0.36f), w * 0.07f, a, "asabkam0", 0, 2, f)
            figure(Offset(w * 0.35f, h * 0.8f), h * 0.2f, Dim145, f, lean = 0.2f)
            for (k in 0 until 3) qIcon("coin", Offset(mix(w * 0.7f, w * 0.38f, seg(p, 0.5f + 0.1f * k, 0.7f + 0.1f * k)), h * 0.5f), w * 0.035f, Qn.Gold, f * seg(p, 0.5f, 0.55f), a.t)
            row131(a, "asabkam1", 3, h * 0.2f, p, f, from = 0.05f, step = 0.2f)
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.75f, h * 0.36f), w * 0.07f, a, "asabkam0", 0, 2, f)
            figure(Offset(w * 0.35f, h * 0.8f), h * 0.2f, Dim145, f, arms = 0.6f)
            qIcon("cross", Offset(w * 0.35f, h * 0.44f), w * 0.07f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            row131(a, "asabkam2", 2, h * 0.2f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.65f, h * 0.8f), h * 0.22f, Qn.Person, f)
            figure(Offset(w * 0.35f, h * 0.8f), h * 0.18f, Dim145, f * seg(p, 0.3f, 0.45f))
            row131(a, "asabkam3", 2, h * 0.3f, p, f, from = 0.05f, step = 0.4f)
        },
    )
}

// ═════════════ as-muthla: we will be patient ═════════════
internal fun DrawScope.asmuthlaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asmuthla", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.24f), w * 0.08f, a, "asmuthla0", 0, f * seg(p, 0.02f, 0.15f))
            qIcon("mountain", Offset(w * 0.5f, h * 0.5f), w * 0.2f, Dim145, f, a.t)
            dots145(70, w * 0.5f, h * 0.7f, 14, Qn.GoldLight, p, f, from = 0.2f)
            row131(a, "asmuthla0", 2, h * 0.86f, p, f, from = 0.5f, step = 0.2f, first = 1)
        },
        { p, f ->
            veil131(f)
            val tilt = seg(p, 0.2f, 0.6f)
            val c = Offset(w * 0.5f, h * 0.46f)
            drawLine(Qn.Gold, Offset(c.x - w * 0.24f, c.y - h * 0.05f * tilt), Offset(c.x + w * 0.24f, c.y + h * 0.05f * tilt), w * 0.008f, alpha = f)
            tagAt(a.tag("asmuthla1", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            qIcon("kaaba", Offset(w * 0.5f, h * 0.4f), w * 0.11f, Qn.Gold, f * seg(p, 0.02f, 0.15f), a.t)
            qIcon("hands", Offset(w * 0.5f, h * 0.6f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            row131(a, "asmuthla2", 2, h * 0.8f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            row131(a, "asmuthla3", 2, h * 0.46f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ as-wasila: they seek a means to their Lord ═════════════
internal fun DrawScope.aswasilaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "aswasila", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            crowd145(w * 0.3f, h * 0.82f, 3, h * 0.18f, f, lean = -0.2f, gap = 0.06f)
            for (k in 0 until 3) softGlow(Offset(w * (0.62f + 0.1f * k), h * (0.5f + 0.03f * kotlin.math.sin(a.t * 2f + k))), w * 0.04f, Smoke151, f * seg(p, 0.3f + 0.1f * k, 0.45f + 0.1f * k))
            row131(a, "aswasila0", 2, h * 0.24f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            crowd145(w * 0.3f, h * 0.82f, 3, h * 0.18f, f, lean = -0.2f, gap = 0.06f)
            val rise = seg(p, 0.2f, 0.8f)
            softGlow(Offset(w * 0.75f, h * 0.12f), w * 0.1f, Qn.GoldLight, 0.6f * f)
            for (k in 0 until 3) softGlow(Offset(mix(w * (0.62f + 0.1f * k), w * 0.75f, rise * 0.6f), mix(h * 0.5f, h * 0.24f, rise)), w * 0.04f, Smoke151, f)
            row131(a, "aswasila1", 2, h * 0.62f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.3f), w * 0.14f, Qn.GoldLight, 0.5f * f * seg(p, 0.05f, 0.3f))
            figure(Offset(w * 0.5f, h * 0.82f), h * 0.2f, Qn.Person, f, lean = 0.2f)
            row131(a, "aswasila2", 2, h * 0.56f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 4) drawRect(Qn.GoldLight, Offset(w * (0.62f - 0.1f * k), h * (0.66f - 0.08f * k)), Size(w * 0.1f, h * 0.015f), alpha = f * seg(p, 0.1f + 0.15f * k, 0.2f + 0.15f * k))
            row131(a, "aswasila3", 2, h * 0.8f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ as-ruh: ask him about the soul ═════════════
internal fun DrawScope.asruhScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asruh", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.32f), w * 0.09f, a, "asruh0", 0, f * seg(p, 0.02f, 0.18f))
            drawLine(Color(0xFF9A7A50), Offset(w * 0.34f, h * 0.5f), Offset(w * 0.3f, h * 0.8f), w * 0.008f, alpha = f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("asruh0", 0, 1), Offset(w * 0.32f, h * 0.4f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            crowd145(w * 0.6f, h * 0.82f, 4, h * 0.18f, f, gap = 0.07f)
            row131(a, "asruh1", 2, h * 0.26f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asruh2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.46f), w * 0.2f, Qn.GoldLight, 0.4f * f * seg(p, 0.05f, 0.3f))
            dots145(5, w * 0.5f, h * 0.62f, 5, Qn.GoldLight, p, f, from = 0.4f)
            tagAt(a.tag("asruh2", 0, 1), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            crowd145(w * 0.75f, h * 0.82f, 2, h * 0.18f, f, gap = 0.06f)
            crowd145(w * 0.25f, h * 0.82f, 2, h * 0.18f, f * seg(p, 0.3f, 0.45f), gap = 0.06f)
            row131(a, "asruh3", 3, h * 0.3f, p, f, from = 0.05f, step = 0.25f)
        },
    )
}

// ═════════════ as-jahr: neither aloud nor in a whisper ═════════════
internal fun DrawScope.asjahrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asjahr", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.5f, h * 0.56f), w * 0.1f, Dim145, f, a.t)
            waves151(Offset(w * 0.5f, h * 0.54f), 4, w * 0.06f * (0.5f + seg(p, 0.4f, 0.8f)), f * seg(p, 0.4f, 0.55f))
            row131(a, "asjahr0", 2, h * 0.22f, p, f, from = 0.05f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.35f, h * 0.44f), w * 0.09f, Qn.Gold, f, a.t)
            crowd145(w * 0.72f, h * 0.82f, 3, h * 0.18f, f, lean = -0.15f, gap = 0.06f)
            qIcon("cross", Offset(w * 0.72f, h * 0.46f), w * 0.06f, Qn.Rose, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("asjahr1", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            waves151(Offset(w * 0.78f, h * 0.44f), 4, w * 0.04f, f * seg(p, 0.05f, 0.2f) * 0.5f)
            waves151(Offset(w * 0.22f, h * 0.44f), 1, w * 0.02f, f * seg(p, 0.3f, 0.45f) * 0.5f)
            waves151(Offset(w * 0.5f, h * 0.44f), 2, w * 0.04f, f * seg(p, 0.6f, 0.75f))
            row131(a, "asjahr2", 3, h * 0.72f, p, f, from = 0.05f, step = 0.27f)
        },
        { p, f ->
            veil131(f)
            qIcon("hands", Offset(w * 0.5f, h * 0.44f), w * 0.11f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            row131(a, "asjahr3", 2, h * 0.7f, p, f, from = 0.1f, step = 0.45f)
        },
    )
}

// ═════════════ as-ziyara: why do you not visit us more? ═════════════
internal fun DrawScope.asziyaraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asziyara", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            angel98(Offset(w * 0.5f, h * 0.3f), w * 0.05f, f * seg(p, 0.02f, 0.2f))
            row131(a, "asziyara0", 2, h * 0.66f, p, f, from = 0.1f, step = 0.45f)
        },
        { p, f ->
            veil131(f)
            angel98(Offset(w * 0.5f, mix(h * 0.08f, h * 0.36f, seg(p, 0.1f, 0.6f))), w * 0.04f, f)
            row131(a, "asziyara1", 2, h * 0.7f, p, f, from = 0.2f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asziyara2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            lightShafts(Offset(w * 0.5f, h * 0.02f), 1.5708f, 0.4f, h * 0.7f, Color(0xFFDDEBFF), 0.35f * f * seg(p, 0.1f, 0.4f), a.t)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) qIcon("book", Offset(w * (0.62f - 0.24f * k), h * 0.4f), w * 0.08f, Qn.Gold, f * seg(p, 0.05f + 0.25f * k, 0.2f + 0.25f * k), a.t)
            row131(a, "asziyara3", 3, h * 0.66f, p, f, from = 0.1f, step = 0.25f)
        },
    )
}
