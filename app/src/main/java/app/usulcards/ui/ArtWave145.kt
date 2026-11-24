package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 145 (2026-11-24): occasions of revelation from al-Wadi'i, second part: silence in prayer, no compulsion in
 * religion, "we hear and obey", the false oath, the two clans at Uhud, the Fajr supplication, the captives of Badr
 * and the souls of the martyrs. The Prophet ﷺ is never drawn (his place is an empty prayer mat or a named light);
 * companions are roundels (Abu Bakr and 'Umar in their own colours, others plain); people are faceless; the green
 * birds of the hadith are only green lights, never birds; the Throne is not drawn.
 */

internal val Dim145 = Color(0xFF8A8296)

internal fun DrawScope.crowd145(cx: Float, foot: Float, n: Int, tall: Float, alpha: Float, lean: Float = 0f, gap: Float = 0.07f) {
    if (alpha <= 0.01f) return
    for (k in 0 until n) figure(Offset(cx + size.width * gap * (k - (n - 1) / 2f), foot), tall, Qn.Person, alpha, lean = lean)
}

internal fun DrawScope.dots145(n: Int, cx: Float, cy: Float, cols: Int, color: Color, p: Float, f: Float, from: Float = 0.1f) {
    val rows = (n + cols - 1) / cols
    for (k in 0 until n) {
        val c = k % cols
        val r = k / cols
        val x = cx + size.width * 0.03f * ((cols - 1) / 2f - c)
        val y = cy + size.height * 0.03f * (r - (rows - 1) / 2f)
        drawCircle(color, size.width * 0.009f, Offset(x, y), alpha = f * seg(p, from + 0.5f * k / n, from + 0.08f + 0.5f * k / n))
    }
}

private fun DrawScope.well145(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawOval(Color(0xFF8A7458), Offset(c.x - s, c.y - s * 0.3f), Size(s * 2f, s * 0.6f), alpha = alpha, style = Stroke(s * 0.12f))
    drawOval(Color(0xFF2E5A7A), Offset(c.x - s * 0.85f, c.y - s * 0.22f), Size(s * 1.7f, s * 0.44f), alpha = alpha * 0.8f)
    drawRect(Color(0xFF8A7458), Offset(c.x - s, c.y), Size(s * 2f, s * 0.7f), alpha = alpha)
}

// ═════════════ as-sukut: we used to talk in prayer ═════════════
internal fun DrawScope.assukutScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "assukut", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.26f), w * 0.09f, a, "assukut0", 0, f * seg(p, 0.02f, 0.18f))
            crowd145(w * 0.5f, h * 0.78f, 4, h * 0.2f, f * seg(p, 0.2f, 0.35f))
            for (k in 0 until 3) qIcon("lips", Offset(w * (0.37f + 0.13f * k), h * 0.5f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.3f + 0.08f * k, 0.4f + 0.08f * k), a.t)
            tagAt(a.tag("assukut0", 0, 1), Offset(w * 0.5f, h * 0.88f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            crowd145(w * 0.5f, h * 0.78f, 4, h * 0.2f, f)
            for (k in 0 until 3) {
                val c = Offset(w * (0.37f + 0.13f * k), h * 0.5f)
                qIcon("lips", c, w * 0.05f, Qn.GoldLight, f * (1f - seg(p, 0.2f, 0.4f)), a.t)
                qIcon("hands", c, w * 0.05f, Qn.Gold, f * seg(p, 0.3f, 0.5f), a.t)
            }
            tagAt(a.tag("assukut1", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.36f), w * 0.09f, a, "assukut2", 0, f * seg(p, 0.02f, 0.18f))
            qIcon("mat", Offset(w * 0.3f, h * 0.42f), w * 0.11f, Qn.Gold, f * seg(p, 0.2f, 0.35f), a.t)
            qIcon("cross", Offset(w * 0.49f, h * 0.38f), w * 0.04f, Qn.Rose, f * seg(p, 0.5f, 0.6f), a.t)
            tagAt(a.tag("assukut2", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.55f, 0.7f))
        },
        { p, f ->
            veil131(f)
            qIcon("sun", Offset(w * 0.68f, h * 0.36f), w * 0.11f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("assukut3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("assukut3", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-ikrah: the sons of the Ansar among Banu al-Nadir ═════════════
internal fun DrawScope.asikrahScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asikrah", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.58f, h * 0.76f), h * 0.24f, Qn.Person, f * seg(p, 0.02f, 0.18f))
            qIcon("child", Offset(w * 0.4f, h * 0.68f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.35f, 0.5f), a.t)
            row131(a, "asikrah0", 2, h * 0.3f, p, f, from = 0.15f, step = 0.3f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) qIcon("house", Offset(w * (0.3f + 0.2f * k) - w * 0.15f * seg(p, 0.25f, 0.8f), h * 0.56f), w * 0.08f, Dim145, f * (1f - 0.5f * seg(p, 0.25f, 0.8f)), a.t)
            for (k in 0 until 2) qIcon("child", Offset(w * (0.62f + 0.12f * k), h * 0.72f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            crowd145(w * 0.75f, h * 0.84f, 2, h * 0.16f, f * seg(p, 0.35f, 0.5f))
            row131(a, "asikrah1", 2, h * 0.26f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.45f), w * 0.22f, Qn.GoldLight, 0.5f * f * seg(p, 0.05f, 0.3f))
            qIcon("hands", Offset(w * 0.5f, h * 0.45f), w * 0.12f, Qn.Gold, f * seg(p, 0.1f, 0.3f), a.t)
            tagAt(a.tag("asikrah2", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 5) drawCircle(Qn.GoldLight, w * 0.025f, Offset(w * 0.5f, h * (0.2f + 0.08f * k)), alpha = f * seg(p, 0.05f + 0.07f * k, 0.12f + 0.07f * k), style = Stroke(w * 0.006f))
            tagAt(a.tag("asikrah3", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.4f, 0.55f))
            row131(a, "asikrah3", 2, h * 0.78f, p, f, from = 0.55f, step = 0.15f, first = 1)
        },
    )
}

// ═════════════ as-samina: we hear and obey ═════════════
internal fun DrawScope.assaminaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "assamina", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            crowd145(w * 0.5f, h * 0.8f, 5, h * 0.2f * (1f - 0.3f * seg(p, 0.4f, 0.6f)), f, lean = 0.2f * seg(p, 0.4f, 0.6f))
            row131(a, "assamina0", 2, h * 0.28f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            val icons = listOf("mat", "moon", "flame", "coin")
            for (k in 0 until 4) qIcon(icons[k], Offset(w * (0.5f + (1.5f - k) * 0.21f), h * 0.42f), w * 0.07f, Qn.Gold, f * seg(p, 0.1f + 0.15f * k, 0.22f + 0.15f * k), a.t)
            row131(a, "assamina1", 4, h * 0.58f, p, f, from = 0.12f, step = 0.15f)
        },
        { p, f ->
            veil131(f)
            qIcon("ear", Offset(w * 0.68f, h * 0.42f), w * 0.09f, Dim145, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.68f, h * 0.42f), w * 0.11f, Qn.Rose, f * seg(p, 0.2f, 0.3f), a.t)
            qIcon("ear", Offset(w * 0.32f, h * 0.42f), w * 0.09f, Qn.Gold, f * seg(p, 0.45f, 0.6f), a.t)
            qIcon("check", Offset(w * 0.32f, h * 0.42f), w * 0.11f, Qn.Jade, f * seg(p, 0.6f, 0.7f), a.t)
            tagAt(a.tag("assamina2", 0, 0), Offset(w * 0.68f, h * 0.62f), f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("assamina2", 0, 1), Offset(w * 0.32f, h * 0.62f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            val lift = seg(p, 0.25f, 0.6f)
            qIcon("scale", Offset(w * 0.5f, h * 0.48f - h * 0.04f * lift), w * 0.16f, Qn.Gold, f, a.t)
            tagAt(a.tag("assamina3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("assamina3", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.55f, 0.7f))
        },
    )
}

// ═════════════ as-yamin: the oath that cuts off a man's wealth ═════════════
internal fun DrawScope.asyaminScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asyamin", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asyamin0", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
            qIcon("coin", Offset(mix(w * 0.65f, w * 0.38f, seg(p, 0.2f, 0.5f)), h * 0.46f), w * 0.07f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("hands", Offset(w * 0.32f, h * 0.5f), w * 0.09f, Dim145, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("asyamin0", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.2f, 0.35f))
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.32f), w * 0.09f, a, "asyamin1", 0, f * seg(p, 0.02f, 0.18f))
            well145(Offset(w * 0.32f, h * 0.5f), w * 0.09f, f * seg(p, 0.2f, 0.35f))
            row131(a, "asyamin1", 2, h * 0.74f, p, f, from = 0.35f, step = 0.25f, first = 1)
        },
        { p, f ->
            veil131(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.42f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("check", Offset(w * 0.6f, h * 0.36f), w * 0.05f, Qn.Jade, f * seg(p, 0.6f, 0.7f), a.t)
            row131(a, "asyamin2", 2, h * 0.68f, p, f, from = 0.1f, step = 0.5f)
        },
        { p, f ->
            veil131(f)
            qIcon("scale", Offset(w * 0.3f, h * 0.4f), w * 0.1f, Dim145, f * seg(p, 0.05f, 0.2f), a.t)
            well145(Offset(w * 0.7f, h * 0.42f), w * 0.07f, f * seg(p, 0.05f, 0.2f))
            for (s in listOf(-1f, 1f)) drawLine(Qn.GoldLight, Offset(w * (0.5f + 0.2f * s), h * 0.5f), Offset(w * 0.5f, mix(h * 0.5f, h * 0.6f, seg(p, 0.4f, 0.6f))), w * 0.006f, alpha = f * seg(p, 0.4f, 0.5f))
            tagAt(a.tag("asyamin3", 0, 0), Offset(w * 0.3f, h * 0.24f), f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("asyamin3", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-taifatan: the two clans at Uhud ═════════════
internal fun DrawScope.astaifatanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "astaifatan", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.32f), w * 0.1f, a, "astaifatan0", 0, f * seg(p, 0.02f, 0.18f))
            tagAt(a.tag("astaifatan0", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.35f, 0.5f))
        },
        { p, f ->
            veil131(f)
            qIcon("mountain", Offset(w * 0.5f, h * 0.38f), w * 0.2f, Dim145, f * seg(p, 0.02f, 0.15f), a.t)
            val sway = 0.15f * kotlin.math.sin(a.t * 2f) * seg(p, 0.3f, 0.5f)
            crowd145(w * 0.7f, h * 0.8f, 3, h * 0.16f, f * seg(p, 0.1f, 0.25f), lean = sway, gap = 0.05f)
            crowd145(w * 0.3f, h * 0.8f, 3, h * 0.16f, f * seg(p, 0.3f, 0.45f), lean = -sway, gap = 0.05f)
            row131(a, "astaifatan1", 2, h * 0.6f, p, f, from = 0.15f, step = 0.25f)
        },
        { p, f ->
            veil131(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.42f), w * 0.11f, Qn.Rose, f * seg(p, 0.1f, 0.3f), a.t)
            tagAt(a.tag("astaifatan2", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.3f, 0.45f))
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("astaifatan3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.45f), w * 0.3f, Qn.GoldLight, 0.45f * f * seg(p, 0.1f, 0.4f))
            crowd145(w * 0.68f, h * 0.8f, 3, h * 0.16f, f, gap = 0.05f)
            crowd145(w * 0.32f, h * 0.8f, 3, h * 0.16f, f, gap = 0.05f)
        },
    )
}

// ═════════════ as-laysa: the matter is all Allah's ═════════════
internal fun DrawScope.aslaysaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "aslaysa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            drawRect(Qn.Ember, Offset(0f, h * 0.62f), Size(w, h * 0.18f), alpha = 0.18f * f * seg(p, 0.05f, 0.4f))
            plain97(Offset(w * 0.72f, h * 0.3f), w * 0.08f, a, "aslaysa0", 0, f * seg(p, 0.02f, 0.18f))
            qIcon("mat", Offset(w * 0.36f, h * 0.5f), w * 0.13f, Qn.Gold, f * seg(p, 0.15f, 0.3f), a.t)
            tagAt(a.tag("aslaysa0", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.4f, 0.55f))
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("aslaysa1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            lightShafts(Offset(w * 0.5f, h * 0.05f), 1.5708f, 0.5f, h * 0.7f, Qn.GoldLight, 0.4f * f * seg(p, 0.2f, 0.5f), a.t)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) plain97(Offset(w * (0.75f - 0.25f * k), h * 0.4f), w * 0.08f, a, "aslaysa2", k, f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k))
        },
        { p, f ->
            veil131(f)
            qIcon("mountain", Offset(w * 0.5f, h * 0.4f), w * 0.2f, Dim145, f * seg(p, 0.5f, 0.65f), a.t)
            row131(a, "aslaysa3", 3, h * 0.66f, p, f, from = 0.05f, step = 0.25f)
        },
    )
}

// ═════════════ as-fidya: the captives of Badr ═════════════
internal fun DrawScope.asfidyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asfidya", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("mat", Offset(w * 0.5f, h * 0.56f), w * 0.13f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            lightShafts(Offset(w * 0.5f, h * 0.05f), 1.5708f, 0.3f, h * 0.5f, Qn.GoldLight, 0.35f * f * seg(p, 0.3f, 0.6f), a.t)
            row131(a, "asfidya0", 2, h * 0.8f, p, f, from = 0.05f, step = 0.35f)
        },
        { p, f ->
            veil131(f)
            dots145(70, w * 0.7f, h * 0.42f, 10, Dim145, p, f, from = 0.05f)
            dots145(70, w * 0.3f, h * 0.42f, 10, Qn.GoldLight, p, f, from = 0.4f)
            row131(a, "asfidya1", 2, h * 0.7f, p, f, from = 0.15f, step = 0.4f)
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.7f, h * 0.36f), w * 0.1f, a, "asfidya2", 0, 0, f * seg(p, 0.02f, 0.18f))
            close97(Offset(w * 0.3f, h * 0.36f), w * 0.1f, a, "asfidya2", 1, 1, f * seg(p, 0.4f, 0.55f))
            qIcon("coin", Offset(w * 0.7f, h * 0.58f), w * 0.06f, Qn.Gold, f * seg(p, 0.2f, 0.3f), a.t)
            tagAt(a.tag("asfidya2", 0, 2), Offset(w * 0.7f, h * 0.72f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("mountain", Offset(w * 0.5f, h * 0.34f), w * 0.22f, Dim145, f * seg(p, 0.02f, 0.15f), a.t)
            dots145(70, w * 0.5f, h * 0.62f, 14, Qn.Rose, p, f, from = 0.3f)
            row131(a, "asfidya3", 2, h * 0.84f, p, f, from = 0.1f, step = 0.4f)
        },
    )
}

// ═════════════ as-shuhada: the souls of the martyrs of Uhud ═════════════
internal fun DrawScope.asshuhadaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asshuhada", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) drawLine(Color(0xFF4F8FB8), Offset(w * 0.1f, h * (0.6f + 0.04f * k)), Offset(w * 0.9f, h * (0.58f + 0.04f * k)), w * 0.012f, alpha = 0.7f * f * seg(p, 0.3f, 0.5f))
            for (k in 0 until 3) qIcon("tree", Offset(w * (0.25f + 0.25f * k), h * 0.46f), w * 0.08f, Qn.Leaf, f * seg(p, 0.35f + 0.08f * k, 0.5f + 0.08f * k), a.t)
            for (k in 0 until 7) {
                val x = w * (0.15f + 0.11f * k)
                val y = h * (0.3f + 0.04f * kotlin.math.sin(a.t * 1.5f + k))
                softGlow(Offset(x, y), w * 0.03f, Qn.Jade, f * seg(p, 0.05f + 0.04f * k, 0.15f + 0.04f * k))
            }
            row131(a, "asshuhada0", 2, h * 0.78f, p, f, from = 0.2f, step = 0.3f)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 3) lantern(Offset(w * (0.3f + 0.2f * k), h * 0.28f), w * 0.06f, f * seg(p, 0.1f + 0.1f * k, 0.25f + 0.1f * k), 0.5f + 0.5f * kotlin.math.sin(a.t * 3f + k))
            tagAt(a.tag("asshuhada1", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.36f), w * 0.1f, a, "asshuhada2", 0, f * seg(p, 0.02f, 0.18f))
            tagAt(a.tag("asshuhada2", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            softGlow(Offset(w * 0.5f, h * 0.42f), w * 0.2f, Qn.Jade, 0.5f * f * seg(p, 0.1f, 0.4f))
            tagAt(a.tag("asshuhada3", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.2f, 0.35f))
        },
    )
}
