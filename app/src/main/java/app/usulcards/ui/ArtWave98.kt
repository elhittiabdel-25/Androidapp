package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 98 (2026-11-17): the eleven cards of «الدعوة في مكة» redrawn in the illustrated style (boards in wave 95). Same
 * beats and tags. Jibril and the angel are a cool white light, never a figure; the Prophet ﷺ is his named light;
 * companions are calligraphy roundels (the closest in their own colour, others plain); crowds are faceless figures.
 */

/** One card as four shots: [shots] per beat, [world] drawn under every shot, then each beat's own drawing. */
internal fun DrawScope.film98(
    a: ArtCtx, lay: String, shots: List<Shot>,
    world: DrawScope.() -> Unit,
    vararg beats: DrawScope.(p: Float, f: Float) -> Unit,
) {
    val w = size.width
    val h = size.height
    val kinds = beats.indices.map { "$lay$it" }
    followBeats(a, shotsFor(a, kinds.indices.associate { kinds[it] to shots[it] }), Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) {
        world()
        beats.forEachIndexed { i, draw ->
            val f = fqIn(a, kinds[i], kinds.getOrNull(i + 1))
            if (f > 0f) draw(a.b(kinds[i]), f)
        }
    }
}

/** Jibril (or an angel): a cool white light, larger for Jibril; never wings, never a figure. */
internal fun DrawScope.angel98(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 2.6f, Color(0xFFDDEBFF), 0.75f * alpha)
    drawCircle(Color(0xFFF2F8FF), r, c, alpha = alpha)
}

/** A rocky mountain with a cave mouth near its top. */
private fun DrawScope.cave98(foot: Offset, s: Float, alpha: Float, lit: Float = 0f) {
    drawPath(Path().apply {
        moveTo(foot.x - s, foot.y); lineTo(foot.x - s * 0.35f, foot.y - s * 0.9f); lineTo(foot.x - s * 0.1f, foot.y - s * 0.8f)
        lineTo(foot.x + s * 0.2f, foot.y - s * 1.05f); lineTo(foot.x + s, foot.y); close()
    }, Color(0xFF4A3A30), alpha = alpha)
    val mouth = Offset(foot.x + s * 0.05f, foot.y - s * 0.62f)
    drawOval(Color(0xFF0E0A08), Offset(mouth.x - s * 0.12f, mouth.y - s * 0.08f), Size(s * 0.24f, s * 0.16f), alpha = alpha)
    if (lit > 0f) softGlow(mouth, s * 0.3f, Qn.GoldLight, 0.7f * lit * alpha)
}

private fun DrawScope.scroll98(c: Offset, s: Float, ink: Color, alpha: Float) {
    drawRoundRect(Color(0xFFEFE2C0), Offset(c.x - s * 0.5f, c.y - s * 0.32f), Size(s, s * 0.64f), CornerRadius(s * 0.06f), alpha = alpha)
    for (k in 0 until 4) drawLine(ink, Offset(c.x - s * 0.38f, c.y - s * 0.18f + k * s * 0.12f), Offset(c.x + s * (0.38f - 0.1f * (k % 2)), c.y - s * 0.18f + k * s * 0.12f), s * 0.025f, StrokeCap.Round, alpha = alpha * 0.8f)
}

private fun tag98(back: Color = Color(0xCC1A1630)) = back

// ═════════════ md-hira ═════════════
internal fun DrawScope.mdhiraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.88f
    film98(a, "mdhira", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.55f, h * 0.4f), 1.2f), Shot(Offset(w * 0.5f, h * 0.45f), 1.1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { nightSky(a.t, S97.Night, S97.NightLow, ground); cave98(Offset(w * 0.55f, ground), w * 0.42f, 1f, lit = 0.6f) },
        { p, f ->
            drawCircle(Color(0xFFF6E7BE), w * 0.03f, Offset(w * 0.18f, h * 0.18f), alpha = f)
            drawCircle(S97.Night, w * 0.028f, Offset(w * 0.193f, h * 0.17f), alpha = f)
            sirajMunir(Offset(w * 0.57f, ground - w * 0.26f), w * 0.022f, f * seg(p, 0.1f, 0.3f), a.t, named = false)
            tagAt(a.tag("mdhira0", 0, 0), Offset(w * 0.78f, h * 0.3f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdhira0", 0, 1), Offset(w * 0.18f, h * 0.3f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
            tagAt(a.tag("mdhira0", 0, 2), Offset(w * 0.5f, h * 0.08f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF0E0A08), alpha = 0.6f * f)
            angel98(Offset(w * 0.3f, h * 0.38f), w * 0.04f, f * seg(p, 0f, 0.2f))
            sirajMunir(Offset(w * 0.68f, h * 0.48f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.68f, h * 0.64f))
            tagAt(a.tag("mdhira1", 0, 0), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.1f, 0.25f), ink = Color(0xFFDDEBFF))
            tagAt(a.tag("mdhira1", 0, 2), Offset(w * 0.3f, h * 0.18f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdhira1", 0, 1), Offset(w * 0.68f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF0E0A08), alpha = 0.65f * f)
            for (k in 0 until 3) {
                val on = seg(p, 0.1f + 0.22f * k, 0.25f + 0.22f * k)
                drawCircle(Color(0xFFDDEBFF), w * (0.12f + 0.07f * k), Offset(w * 0.5f, h * 0.5f), alpha = 0.6f * f * on * (1f - seg(p, 0.3f + 0.22f * k, 0.5f + 0.22f * k)), style = Stroke(2f * density))
            }
            scroll98(Offset(w * 0.5f, h * 0.5f), w * 0.22f, Color(0xFF6A5030), f * seg(p, 0.6f, 0.8f))
            softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.2f, Qn.GoldLight, 0.4f * f * seg(p, 0.65f, 0.9f))
            tagAt(a.tag("mdhira2", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdhira2", 0, 0), Offset(w * 0.5f, h * 0.8f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.8f * f)
            val l = Offset(w * 0.3f, h * 0.5f)
            val r = Offset(w * 0.7f, h * 0.5f)
            drawRoundRect(Color(0xFF1E5A4A), Offset(l.x - w * 0.1f, l.y - h * 0.18f), Size(w * 0.2f, h * 0.36f), CornerRadius(6f * density), alpha = f)
            drawRoundRect(Qn.Gold, Offset(l.x - w * 0.08f, l.y - h * 0.15f), Size(w * 0.16f, h * 0.3f), CornerRadius(4f * density), alpha = f, style = Stroke(2f * density))
            scroll98(r, w * 0.2f, Color(0xFF8A7A6A), f * (1f - 0.6f * seg(p, 0.5f, 0.8f)))
            drawLine(Qn.Ember, r + Offset(-w * 0.12f, -h * 0.1f), r + Offset(w * 0.12f, h * 0.1f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.5f, 0.7f))
            tagAt(a.tag("mdhira3", 0, 0), l + Offset(0f, h * 0.26f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("mdhira3", 0, 1), r + Offset(0f, h * 0.26f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ md-muddaththir ═════════════
internal fun DrawScope.mdmuddScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mdmudd", listOf(Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.45f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1.08f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { nightSky(a.t, Color(0xFF0A1028), Color(0xFF2A3560), ground); dunes97(ground, Color(0xFF3A2E22), Color(0xFF2A2218)) },
        { p, f ->
            // the silence: the light waits, an hourglass runs, longing glows
            sirajMunir(Offset(w * 0.5f, h * 0.55f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.7f))
            qIcon("hourglass", Offset(w * 0.22f, h * 0.4f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("heart", Offset(w * 0.78f, h * 0.4f), w * 0.09f, Qn.Rose, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("mdmudd0", 0, 0), Offset(w * 0.22f, h * 0.22f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdmudd0", 0, 1), Offset(w * 0.78f, h * 0.22f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF0A0E20), alpha = 0.5f * f)
            angel98(Offset(w * 0.5f, h * 0.2f), w * 0.05f, f * seg(p, 0f, 0.2f))
            drawLine(Color(0xFFDDEBFF), Offset(0f, h * 0.2f), Offset(w, h * 0.2f), 1f * density, alpha = 0.4f * f)
            val run = easeInOut(seg(p, 0.3f, 0.7f))
            house12(Offset(w * 0.8f, ground), w * 0.2f, Color(0xFFD8C0A0), Color(0xFF8A6A40), f, lit = 0.6f)
            sirajMunir(Offset(mix(w * 0.3f, w * 0.72f, run), ground - h * 0.12f), w * 0.025f, f, a.t, named = false)
            tagAt(a.tag("mdmudd1", 0, 0), Offset(w * 0.5f, h * 0.08f), f * seg(p, 0.1f, 0.25f), ink = Color(0xFFDDEBFF))
            close97(Offset(w * 0.8f, h * 0.42f), w * 0.07f, a, "mdmudd1", 1, 0, f * seg(p, 0.55f, 0.75f))
            tagAt(a.tag("mdmudd1", 0, 2), Offset(w * 0.5f, h * 0.45f), f * seg(p, 0.75f, 0.9f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF0A0E20), alpha = 0.6f * f)
            scroll98(Offset(w * 0.5f, h * 0.45f), w * 0.26f, Color(0xFF6A5030), f * seg(p, 0f, 0.2f))
            for (k in 0 until 8) {
                val ang = k * PI.toFloat() / 4f + a.t * 0.2f
                val on = seg(p, 0.4f + 0.05f * k, 0.55f + 0.05f * k)
                drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.45f) + Offset(cos(ang), sin(ang)) * (w * 0.17f), Offset(w * 0.5f, h * 0.45f) + Offset(cos(ang), sin(ang)) * (w * 0.24f), 2f * density, StrokeCap.Round, alpha = f * on)
            }
            tagAt(a.tag("mdmudd2", 0, 0), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("mdmudd2", 0, 1), Offset(w * 0.5f, h * 0.82f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.7f * f)
            val c = Offset(w * 0.5f, h * 0.55f)
            sirajMunir(c, w * 0.04f, f, a.t, named = false)
            for (k in 0 until 3) {
                val ang = PI.toFloat() * (0.85f - 0.35f * k)
                val at = c + Offset(cos(ang) * w * 0.3f, -sin(ang) * h * 0.32f)
                val on = seg(p, 0.15f + 0.22f * k, 0.3f + 0.22f * k)
                drawLine(Qn.GoldLight, c, at, 1.5f * density, alpha = 0.6f * f * on)
                tagAt(a.tag("mdmudd3", 0, k), at, f * on, ink = ArtInk.Gold)
            }
            tagAt(a.tag("mdmudd3", 0, 3), Offset(w * 0.5f, h * 0.9f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ md-maratib ═════════════
internal fun DrawScope.mdmaratibScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mdmaratib", listOf(Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1.08f), Shot(Offset(w * 0.5f, h * 0.35f), 1f)),
        { nightSky(a.t, S97.Night, S97.NightLow, ground); dunes97(ground, S97.SandDark, Color(0xFF5A4630)) },
        { p, f ->
            // a true dream: a night that turns into the clear light of dawn
            val dawn = easeInOut(seg(p, 0.3f, 0.9f))
            drawRect(Color(0xFFF2D6A0), Offset(0f, ground - h * 0.2f), Size(w, h * 0.2f), alpha = 0.6f * f * dawn)
            sirajMunir(Offset(w * 0.5f, h * 0.5f), w * 0.03f, f, a.t, named = false)
            drawCircle(Color(0xFFF2F8FF), w * 0.12f * dawn, Offset(w * 0.5f, h * 0.5f), alpha = 0.4f * f, style = Stroke(1.5f * density))
            tagAt(a.tag("mdmaratib0", 0, 1), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdmaratib0", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF0A0E20), alpha = 0.6f * f)
            val c = Offset(w * 0.32f, h * 0.5f)
            qIcon("heart", c, w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            angel98(c + Offset(0f, -h * 0.22f), w * 0.02f, f * seg(p, 0.15f, 0.3f))
            for (k in 0 until 4) drawCircle(Color(0xFFDDEBFF), w * 0.006f, Offset(c.x, mix(c.y - h * 0.2f, c.y - h * 0.04f, ((a.t * 0.8f + k * 0.25f) % 1f))), alpha = f * seg(p, 0.2f, 0.35f))
            angel98(Offset(w * 0.72f, h * 0.48f), w * 0.04f, f * seg(p, 0.5f, 0.7f))   // the angel as a man: still only light
            tagAt(a.tag("mdmaratib1", 0, 0), c + Offset(0f, h * 0.18f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("mdmaratib1", 0, 1), Offset(w * 0.72f, h * 0.22f), f * seg(p, 0.6f, 0.75f), ink = Color(0xFFDDEBFF))
        },
        { p, f ->
            drawRect(Color(0xFF0A0E20), alpha = 0.7f * f)
            val c = Offset(w * 0.5f, h * 0.48f)
            for (k in 0 until 5) {
                val r = w * (0.06f + 0.06f * k + 0.04f * ((a.t * 1.6f) % 1f))
                drawCircle(Color(0xFFDDEBFF), r, c, alpha = 0.5f * f * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k) * (1f - k / 6f), style = Stroke(2f * density))
            }
            sirajMunir(c, w * 0.03f, f, a.t, named = false)
            tagAt(a.tag("mdmaratib2", 0, 0), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0.05f, 0.2f), ink = Color(0xFFDDEBFF))
            tagAt(a.tag("mdmaratib2", 0, 1), Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF0A0E20), alpha = 0.6f * f)
            for (k in 0 until 7) {
                val y = h * (0.12f + 0.07f * k)
                drawLine(Color(0xFF8AA0D0), Offset(w * 0.1f, y), Offset(w * 0.9f, y), 1.2f * density, alpha = 0.5f * f * (1f - k / 8f))
            }
            val up = easeInOut(seg(p, 0.05f, 0.6f))
            sirajMunir(Offset(w * 0.5f, mix(h * 0.8f, h * 0.1f, up)), w * 0.03f, f, a.t, named = false)
            drawRoundRect(Color(0xFF2A5A4A), Offset(w * 0.38f, h * 0.84f), Size(w * 0.24f, h * 0.05f), CornerRadius(4f * density), alpha = f * seg(p, 0.6f, 0.8f))
            tagAt(a.tag("mdmaratib3", 0, 2), Offset(w * 0.2f, h * 0.12f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdmaratib3", 0, 0), Offset(w * 0.78f, h * 0.18f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
            tagAt(a.tag("mdmaratib3", 0, 1), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ md-sirr ═════════════
internal fun DrawScope.mdsirrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mdsirr", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1.1f), Shot(Offset(w * 0.5f, h * 0.6f), 1.05f)),
        { nightSky(a.t, S97.Night, S97.NightLow, ground); for (k in 0 until 5) house12(Offset(w * (0.08f + 0.21f * k), ground), w * 0.12f, Color(0xFF5A4A3A), Color(0xFF3A2E24), 1f) },
        { p, f ->
            kaaba29(Offset(w * 0.5f, ground), w * 0.08f, f)
            qIcon("lock", Offset(w * 0.2f, h * 0.3f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("mdsirr0", 0, 0), Offset(w * 0.2f, h * 0.45f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("mdsirr0", 0, 1), Offset(w * 0.5f, ground - h * 0.25f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.6f * f)
            val c = Offset(w * 0.5f, h * 0.62f)
            sirajMunir(c, w * 0.035f, f, a.t, nameAt = c + Offset(0f, h * 0.15f))
            val xs = listOf(0.2f, 0.5f, 0.8f)
            for (k in 0 until 3) {
                val at = Offset(w * xs[k], h * (if (k == 1) 0.24f else 0.34f))
                val on = seg(p, 0.15f + 0.2f * k, 0.3f + 0.2f * k)
                drawLine(Qn.GoldLight, c, at, 1.4f * density, alpha = 0.6f * f * on)
                if (k == 2) plain97(at, w * 0.07f, a, "mdsirr1", k, f * on) else close97(at, w * 0.07f, a, "mdsirr1", k, k, f * on)
            }
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.65f * f)
            drawRoundRect(Color(0xFF2A5A4A), Offset(w * 0.3f, h * 0.62f), Size(w * 0.4f, h * 0.08f), CornerRadius(6f * density), alpha = f)
            for (k in 0 until 3) pray12(Offset(w * (0.38f + 0.12f * k), h * 0.66f), h * 0.18f, Qn.Person, f * seg(p, 0.15f + 0.15f * k, 0.3f + 0.15f * k))
            tagAt(a.tag("mdsirr2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.6f * f)
            house12(Offset(w * 0.5f, ground), w * 0.36f, Color(0xFFD8C0A0), Color(0xFF8A6A40), f, lit = seg(p, 0.2f, 0.5f))
            val gather = easeInOut(seg(p, 0.2f, 0.7f))
            for (k in 0 until 6) {
                val side = if (k % 2 == 0) -1f else 1f
                figure(Offset(w * 0.5f + side * mix(w * 0.45f, w * (0.04f + 0.03f * (k / 2)), gather), ground), h * 0.1f, Qn.Person, f * (1f - 0.7f * seg(gather, 0.85f, 1f)), walk = a.t * 2f)
            }
            tagAt(a.tag("mdsirr3", 0, 0), Offset(w * 0.5f, ground - h * 0.42f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdsirr3", 0, 1), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-jahr ═════════════
internal fun DrawScope.mdjahrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mdjahr", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.4f, h * 0.45f), 1.05f), Shot(Offset(w * 0.5f, h * 0.6f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { daySky12(Qn.DayTop, Color(0xFFF2D6A0), ground, Color(0xFF9A8060)) },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.45f)
            sirajMunir(c, w * 0.035f, f, a.t, nameAt = c + Offset(0f, h * 0.14f))
            for (k in 0 until 8) figure(Offset(w * (0.12f + 0.11f * k), ground), h * 0.11f, Qn.Person, f * seg(p, 0.2f + 0.05f * k, 0.35f + 0.05f * k))
            tagAt(a.tag("mdjahr0", 0, 1), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("mdjahr0", 0, 0), Offset(w * 0.5f, ground - h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawPath(Path().apply { moveTo(w * 0.1f, ground); lineTo(w * 0.32f, h * 0.48f); lineTo(w * 0.52f, ground); close() }, Color(0xFF7A6A58), alpha = f)
            sirajMunir(Offset(w * 0.32f, h * 0.4f), w * 0.03f, f, a.t, named = false)
            for (k in 0 until 3) drawArc(Qn.GoldLight, -40f, 80f, false, Offset(w * 0.32f - w * (0.08f + 0.06f * k), h * 0.4f - w * (0.08f + 0.06f * k)), Size(w * (0.16f + 0.12f * k), w * (0.16f + 0.12f * k)), alpha = f * seg(p, 0.3f + 0.1f * k, 0.45f + 0.1f * k), style = Stroke(2f * density))
            val come = easeInOut(seg(p, 0.4f, 0.9f))
            for (k in 0 until 6) figure(Offset(mix(w * 1.05f, w * (0.6f + 0.06f * k), come), ground), h * 0.11f, Qn.Person, f, walk = a.t * 2f * (1f - come))
            tagAt(a.tag("mdjahr1", 0, 0), Offset(w * 0.18f, h * 0.38f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdjahr1", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            tagAt(a.tag("mdjahr1", 0, 2), Offset(w * 0.76f, ground - h * 0.2f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.5f * f)
            for (k in 0 until 7) figure(Offset(w * (0.14f + 0.12f * k), ground), h * 0.12f, Qn.Person, f)
            qIcon("check", Offset(w * 0.5f, h * 0.4f), w * 0.1f, Qn.Jade, f * seg(p, 0.4f, 0.6f), a.t)
            tagAt(a.tag("mdjahr2", 0, 0), Offset(w * 0.5f, ground - h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdjahr2", 0, 1), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.65f * f)
            close97(Offset(w * 0.3f, h * 0.5f), w * 0.1f, a, "mdjahr3", 1, 2, f * seg(p, 0.05f, 0.25f))
            fqCoins(Offset(w * 0.7f, h * 0.62f), w * 0.03f, 5, f * seg(p, 0.3f, 0.5f))
            drawLine(ArtInk.Gold, Offset(w * 0.43f, h * 0.5f), Offset(w * 0.6f, h * 0.55f), 1.5f * density, alpha = 0.6f * f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("mdjahr3", 0, 0), Offset(w * 0.7f, h * 0.4f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-adha ═════════════
internal fun DrawScope.mdadhaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mdadha", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.45f, h * 0.6f), 1.1f), Shot(Offset(w * 0.45f, h * 0.6f), 1.1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { daySky12(Color(0xFF6A7A9A), Color(0xFFE8C8A0), ground, Color(0xFF9A8060)); kaaba29(Offset(w * 0.5f, ground), w * 0.1f, 1f) },
        { p, f ->
            drawRect(Color(0xFF3A1E1E), alpha = 0.35f * f * seg(p, 0.1f, 0.4f))
            for (k in 0 until 4) figure(Offset(w * (0.1f + 0.07f * k), ground), h * 0.12f, Qn.Person, f, lean = 0.2f)
            for (k in 0 until 3) figure(Offset(w * (0.72f + 0.07f * k), ground), h * 0.12f, lerpColor(Qn.Person, Color(0xFFFFF0C8), 0.4f), f)
            qIcon("flame", Offset(w * 0.25f, h * 0.36f), w * 0.08f, Qn.Ember, f * seg(p, 0.15f, 0.3f), a.t)
            tagAt(a.tag("mdadha0", 0, 0), Offset(w * 0.25f, h * 0.2f), f * seg(p, 0.2f, 0.35f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("mdadha0", 0, 1), Offset(w * 0.79f, h * 0.48f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val c = Offset(w * 0.32f, ground - h * 0.06f)
            sirajMunir(c, w * 0.03f, f, a.t, nameAt = c + Offset(0f, -h * 0.14f))
            val drop = easeInOut(seg(p, 0.35f, 0.6f))
            drawRoundRect(Color(0xFF4A3A2A), Offset(c.x - w * 0.05f, mix(h * 0.3f, c.y - h * 0.04f, drop)), Size(w * 0.1f, h * 0.04f), CornerRadius(6f * density), alpha = f * seg(p, 0.25f, 0.35f))
            tagAt(a.tag("mdadha1", 0, 1), Offset(w * 0.2f, h * 0.16f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("mdadha1", 0, 0), Offset(w * 0.5f, ground - h * 0.3f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val c = Offset(w * 0.32f, ground - h * 0.06f)
            sirajMunir(c, w * 0.03f, f, a.t, named = false)
            val off = easeInOut(seg(p, 0.4f, 0.7f))
            drawRoundRect(Color(0xFF4A3A2A), Offset(c.x - w * 0.05f + w * 0.15f * off, c.y - h * 0.04f + h * 0.05f * off), Size(w * 0.1f, h * 0.04f), CornerRadius(6f * density), alpha = f * (1f - off))
            close97(Offset(w * 0.68f, h * 0.36f), w * 0.08f, a, "mdadha2", 0, 2, f * seg(p, 0.05f, 0.3f))
            qIcon("check", Offset(w * 0.45f, h * 0.3f), w * 0.06f, Qn.Jade, f * seg(p, 0.65f, 0.8f), a.t)
            tagAt(a.tag("mdadha2", 0, 1), Offset(w * 0.45f, h * 0.18f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            val sun = easeInOut(seg(p, 0f, 0.4f))
            softGlow(Offset(w * 0.8f, h * 0.18f), w * 0.15f, Color(0xFFFFE9A8), 0.8f * f * sun)
            drawCircle(Color(0xFFFFF0C8), w * 0.04f, Offset(w * 0.8f, h * 0.18f), alpha = f * sun)
            drawRoundRect(Color(0xFF2A5A4A), Offset(w * 0.2f, ground - h * 0.03f), Size(w * 0.2f, h * 0.03f), CornerRadius(4f * density), alpha = f * seg(p, 0.2f, 0.4f))
            sirajMunir(Offset(w * 0.3f, ground - h * 0.28f), w * 0.025f, f * seg(p, 0.2f, 0.4f), a.t, named = false)
            tagAt(a.tag("mdadha3", 0, 0), Offset(w * 0.8f, h * 0.32f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            tagAt(a.tag("mdadha3", 0, 1), Offset(w * 0.5f, ground - h * 0.3f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ md-habasha ═════════════
internal fun DrawScope.mdhabashaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "mdhabasha", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1.05f), Shot(Offset(w * 0.5f, h * 0.6f), 1f), Shot(Offset(w * 0.6f, h * 0.5f), 1.05f)),
        {
            daySky12(Color(0xFF5A8AB0), Color(0xFFE8D8B0), ground, S97.Sand)
            drawRect(Color(0xFF2A6A8A), Offset(w * 0.42f, ground - h * 0.03f), Size(w * 0.2f, h - ground + h * 0.03f))   // the Red Sea
        },
        { p, f ->
            kaaba29(Offset(w * 0.18f, ground), w * 0.07f, f)
            for (k in 0 until 3) palm12(Offset(w * (0.74f + 0.08f * k), ground), h * 0.2f, Color(0xFF6A4A30), Qn.Leaf, f, a.t)
            val c = Offset(w * 0.82f, h * 0.32f)
            softGlow(c, w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.4f, 0.6f))
            qIcon("crown", c, w * 0.09f, Qn.GoldLight, f * seg(p, 0.4f, 0.6f), a.t)
            val arc = easeInOut(seg(p, 0.1f, 0.6f))
            for (k in 0 until 9) {
                val u = k / 8f
                if (u > arc) break
                drawCircle(Qn.GoldLight, w * 0.005f, Offset(mix(w * 0.22f, w * 0.78f, u), ground - h * 0.12f - sin(u * PI.toFloat()) * h * 0.25f), alpha = f)
            }
            tagAt(a.tag("mdhabasha0", 0, 2), Offset(w * 0.82f, h * 0.12f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdhabasha0", 0, 1), c + Offset(0f, h * 0.14f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.5f * f)
            close97(Offset(w * 0.38f, h * 0.45f), w * 0.09f, a, "mdhabasha1", 0, 0, f * seg(p, 0.1f, 0.3f))
            close97(Offset(w * 0.62f, h * 0.45f), w * 0.09f, a, "mdhabasha1", 1, 2, f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("mdhabasha1", 0, 2), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
        },
        { p, f ->
            val go = easeInOut(seg(p, 0.05f, 0.8f))
            for (k in 0 until 10) figure(Offset(mix(w * 0.05f, w * 0.66f, go) + (k % 5) * w * 0.035f, ground - (k / 5) * h * 0.02f), h * 0.1f, Qn.Person, f, walk = a.t * 2f)
            for (k in 0 until 4) figure(Offset(mix(w * 0.02f, w * 0.63f, go) + k * w * 0.035f, ground), h * 0.09f, Color(0xFFB8A0C8), f, walk = a.t * 2f)
            tagAt(a.tag("mdhabasha2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdhabasha2", 0, 1), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.55f * f)
            val c = Offset(w * 0.72f, h * 0.42f)
            qIcon("crown", c, w * 0.12f, Qn.GoldLight, f, a.t)
            plain97(Offset(w * 0.25f, h * 0.45f), w * 0.08f, a, "mdhabasha3", 0, f * seg(p, 0.05f, 0.25f) * (1f - 0.6f * seg(p, 0.55f, 0.8f)))
            for (k in 0 until 3) qIcon("coin", Offset(w * (0.34f + 0.05f * k), h * 0.62f), w * 0.04f, Qn.Gold, f * seg(p, 0.2f, 0.35f) * (1f - seg(p, 0.5f, 0.7f)), a.t)
            val shield = easeInOut(seg(p, 0.5f, 0.85f))
            drawArc(Qn.Jade, 200f, 140f, false, Offset(c.x - w * 0.22f, c.y - h * 0.3f), Size(w * 0.44f, h * 0.6f), alpha = f * shield, style = Stroke(3f * density))
            tagAt(a.tag("mdhabasha3", 0, 1), c + Offset(0f, h * 0.18f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("mdhabasha3", 0, 2), Offset(w * 0.5f, h * 0.88f), f * seg(p, 0.75f, 0.9f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ md-shib ═════════════
internal fun DrawScope.mdshibScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mdshib", listOf(Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.6f), 1.05f), Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { nightSky(a.t, S97.Night, S97.NightLow, ground); dunes97(ground, Color(0xFF3A2E22), Color(0xFF2A2218)) },
        { p, f ->
            for (k in 0 until 5) figure(Offset(w * (0.08f + 0.07f * k), ground), h * 0.12f, Color(0xFF6A5A6A), f)
            for (k in 0 until 4) figure(Offset(w * (0.68f + 0.07f * k), ground), h * 0.12f, lerpColor(Qn.Person, Color(0xFFFFF0C8), 0.3f), f)
            val wall = easeInOut(seg(p, 0.3f, 0.7f))
            drawLine(Qn.Ember, Offset(w * 0.52f, ground), Offset(w * 0.52f, ground - h * 0.5f * wall), 4f * density, StrokeCap.Round, alpha = f)
            tagAt(a.tag("mdshib0", 0, 0), Offset(w * 0.22f, ground - h * 0.22f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("mdshib0", 0, 1), Offset(w * 0.78f, ground - h * 0.22f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawPath(Path().apply { moveTo(0f, ground); lineTo(w * 0.25f, h * 0.3f); lineTo(w * 0.4f, ground); close() }, Color(0xFF3A2E24), alpha = f)
            drawPath(Path().apply { moveTo(w * 0.6f, ground); lineTo(w * 0.75f, h * 0.28f); lineTo(w, ground); close() }, Color(0xFF3A2E24), alpha = f)
            for (k in 0 until 5) figure(Offset(w * (0.42f + 0.04f * k), ground), h * 0.08f, Qn.Person, f * 0.8f)
            kaaba29(Offset(w * 0.86f, h * 0.36f), w * 0.05f, f * seg(p, 0.2f, 0.4f))
            scroll98(Offset(w * 0.86f, h * 0.18f), w * 0.1f, Color(0xFF6A3020), f * seg(p, 0.35f, 0.55f))
            tagAt(a.tag("mdshib1", 0, 0), Offset(w * 0.86f, h * 0.06f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdshib1", 0, 1), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.5f * f)
            for (k in 0 until 3) {
                val on = seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k)
                drawCircle(Color(0xFFF6E7BE), w * 0.025f, Offset(w * (0.25f + 0.25f * k), h * 0.25f), alpha = f * on)
                drawCircle(S97.Night, w * 0.023f, Offset(w * (0.25f + 0.25f * k) + w * 0.01f, h * 0.24f), alpha = f * on)
            }
            val tear = seg(p, 0.55f, 0.85f)
            scroll98(Offset(w * 0.5f - w * 0.05f * tear, h * 0.58f), w * 0.2f, Color(0xFF6A3020), f * (1f - 0.7f * tear))
            drawLine(Qn.Ember, Offset(w * 0.5f, h * 0.45f), Offset(w * 0.5f, h * 0.72f), 3f * density, alpha = f * tear)
            tagAt(a.tag("mdshib2", 0, 0), Offset(w * 0.5f, h * 0.38f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            tagAt(a.tag("mdshib2", 0, 1), Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.7f * f)
            scroll98(Offset(w * 0.5f, h * 0.48f), w * 0.3f, Color(0xFF8A7A6A), f)
            tagAt(a.tag("mdshib3", 0, 0), Offset(w * 0.5f, h * 0.46f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            drawLine(Qn.Ember, Offset(w * 0.33f, h * 0.36f), Offset(w * 0.67f, h * 0.6f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("mdshib3", 0, 1), Offset(w * 0.5f, h * 0.8f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-taif ═════════════
internal fun DrawScope.mdtaifScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mdtaif", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.6f, h * 0.6f), 1.1f), Shot(Offset(w * 0.5f, h * 0.45f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1f)),
        {
            daySky12(Color(0xFF6A7A9A), Color(0xFFE8C8A0), ground, Color(0xFF8A7458))
            for (k in 0 until 3) drawPath(Path().apply { val x = w * (0.55f + 0.15f * k); moveTo(x, ground); lineTo(x + w * 0.12f, ground - h * (0.32f + 0.06f * k)); lineTo(x + w * 0.26f, ground); close() }, Color(0xFF5A6A4A), alpha = 0.8f)
        },
        { p, f ->
            kaaba29(Offset(w * 0.12f, ground), w * 0.06f, f)
            val go = easeInOut(seg(p, 0.05f, 0.85f))
            sirajMunir(Offset(mix(w * 0.18f, w * 0.6f, go), ground - h * 0.12f), w * 0.025f, f, a.t, named = false)
            tagAt(a.tag("mdtaif0", 0, 2), Offset(mix(w * 0.18f, w * 0.6f, go), ground - h * 0.24f), f * seg(p, 0.2f, 0.4f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdtaif0", 0, 0), Offset(w * 0.12f, ground - h * 0.2f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdtaif0", 0, 1), Offset(w * 0.8f, h * 0.2f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 6) figure(Offset(w * (0.62f + 0.06f * k), ground), h * 0.11f, Color(0xFF6A5A6A), f, arms = 0.8f)
            for (k in 0 until 6) {
                val u = ((a.t * 1.2f + k / 6f) % 1f)
                drawCircle(Color(0xFF8A7A6A), w * 0.008f, Offset(mix(w * 0.62f, w * 0.32f, u), ground - h * 0.18f - sin(u * PI.toFloat()) * h * 0.1f), alpha = f * seg(p, 0.2f, 0.3f))
            }
            sirajMunir(Offset(w * 0.3f, ground - h * 0.12f), w * 0.025f, f, a.t, named = false)
            for (k in 0 until 3) drawCircle(Qn.Rose, w * 0.006f, Offset(w * (0.28f + 0.02f * k), ground - h * 0.005f), alpha = f * seg(p, 0.5f, 0.7f))
            tagAt(a.tag("mdtaif1", 0, 0), Offset(w * 0.78f, ground - h * 0.22f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("mdtaif1", 0, 1), Offset(w * 0.3f, h * 0.88f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.5f * f)
            angel98(Offset(w * 0.5f, h * 0.18f), w * 0.03f, f * seg(p, 0f, 0.2f))
            val close = 0.3f * seg(p, 0.15f, 0.35f) * (1f - easeInOut(seg(p, 0.5f, 0.8f)))
            drawPath(Path().apply { moveTo(0f, ground); lineTo(w * (0.18f + close), h * 0.3f); lineTo(w * (0.36f + close), ground); close() }, Color(0xFF4A3A30), alpha = f)
            drawPath(Path().apply { moveTo(w, ground); lineTo(w * (0.82f - close), h * 0.3f); lineTo(w * (0.64f - close), ground); close() }, Color(0xFF4A3A30), alpha = f)
            sirajMunir(Offset(w * 0.5f, ground - h * 0.15f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.5f, ground - h * 0.02f))
            softGlow(Offset(w * 0.5f, ground - h * 0.15f), w * 0.2f, Qn.Jade, 0.4f * f * seg(p, 0.55f, 0.85f))
            tagAt(a.tag("mdtaif2", 0, 0), Offset(w * 0.2f, h * 0.2f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("mdtaif2", 0, 1), Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.55f * f)
            for (k in 0 until 3) {
                val c = Offset(w * (0.2f + 0.3f * k), ground)
                drawPath(Path().apply { moveTo(c.x - w * 0.08f, c.y); lineTo(c.x, c.y - h * 0.16f); lineTo(c.x + w * 0.08f, c.y); close() }, Color(0xFF6A4A30), alpha = f)
                for (j in 0 until 3) figure(Offset(c.x - w * 0.06f + j * w * 0.06f, ground), h * 0.08f, Qn.Person, f * seg(p, 0.2f + 0.2f * k, 0.35f + 0.2f * k))
            }
            val go = easeInOut(seg(p, 0.15f, 0.85f))
            sirajMunir(Offset(mix(w * 0.2f, w * 0.8f, go), ground - h * 0.26f), w * 0.025f, f, a.t, named = false)
            tagAt(a.tag("mdtaif3", 0, 0), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-aqaba ═════════════
internal fun DrawScope.mdaqabaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mdaqaba", listOf(Shot(Offset(w * 0.5f, h * 0.6f), 1.05f), Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.6f), 1.05f), Shot(Offset(w * 0.5f, h * 0.55f), 1f)),
        { nightSky(a.t, S97.Night, S97.NightLow, ground); drawPath(Path().apply { moveTo(0f, ground); lineTo(w * 0.3f, h * 0.55f); lineTo(w * 0.62f, ground); close() }, Color(0xFF3A2E24)) },
        { p, f ->
            sirajMunir(Offset(w * 0.3f, h * 0.48f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.3f, h * 0.36f))
            for (k in 0 until 6) figure(Offset(w * (0.5f + 0.07f * k), ground), h * 0.11f, lerpColor(Qn.Person, Color(0xFFFFF0C8), 0.6f * seg(p, 0.3f, 0.7f)), f)
            tagAt(a.tag("mdaqaba0", 0, 0), Offset(w * 0.68f, ground - h * 0.22f), f * seg(p, 0.4f, 0.55f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.3f, h * 0.48f), w * 0.03f, f, a.t, named = false)
            for (k in 0 until 12) figure(Offset(w * (0.45f + 0.04f * (k % 6)), ground - (k / 6) * h * 0.025f), h * 0.1f, Color(0xFFE8D8B8), f)
            plain97(Offset(w * 0.72f, h * 0.34f), w * 0.07f, a, "mdaqaba1", 1, f * seg(p, 0.45f, 0.65f))
            val go = easeInOut(seg(p, 0.65f, 0.95f))
            drawLine(Qn.GoldLight, Offset(w * 0.72f, h * 0.42f), Offset(mix(w * 0.72f, w * 0.98f, go), mix(h * 0.42f, h * 0.18f, go)), 1.5f * density, alpha = f)
            tagAt(a.tag("mdaqaba1", 0, 2), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("mdaqaba1", 0, 0), Offset(w * 0.58f, ground - h * 0.22f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(S97.Night, alpha = 0.4f * f)
            sirajMunir(Offset(w * 0.3f, h * 0.48f), w * 0.03f, f, a.t, named = false)
            for (k in 0 until 24) figure(Offset(w * (0.42f + 0.045f * (k % 12)), ground - (k / 12) * h * 0.025f), h * 0.09f, Color(0xFFE8D8B8), f * seg(p, 0.05f, 0.3f))
            palm12(Offset(w * 0.82f, h * 0.5f), h * 0.14f, Color(0xFF6A4A30), Qn.Leaf, f * seg(p, 0.55f, 0.75f), a.t)
            softGlow(Offset(w * 0.82f, h * 0.42f), w * 0.1f, Qn.Jade, 0.5f * f * seg(p, 0.6f, 0.8f))
            tagAt(a.tag("mdaqaba2", 0, 2), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("mdaqaba2", 0, 0), Offset(w * 0.6f, ground - h * 0.2f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdaqaba2", 0, 1), Offset(w * 0.82f, h * 0.24f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(S97.Night, alpha = 0.55f * f)
            for (k in 0 until 7) house12(Offset(w * (0.06f + 0.14f * k), ground), w * 0.12f, Color(0xFFD8C0A0), Color(0xFF8A6A40), f, lit = seg(p, 0.1f + 0.1f * k, 0.25f + 0.1f * k))
            tagAt(a.tag("mdaqaba3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ md-hijra ═════════════
internal fun DrawScope.mdhijraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mdhijra", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.6f), 1.05f)),
        { nightSky(a.t, S97.Night, S97.NightLow, ground); dunes97(ground, Color(0xFF3A2E22), Color(0xFF2A2218)) },
        { p, f ->
            house12(Offset(w * 0.5f, ground), w * 0.4f, Color(0xFF5A4A3A), Color(0xFF3A2E24), f, lit = 0.4f)
            for (k in 0 until 8) figure(Offset(w * (0.12f + 0.11f * k), ground), h * 0.11f, Color(0xFF6A5A6A), f * seg(p, 0.2f + 0.04f * k, 0.35f + 0.04f * k))
            tagAt(a.tag("mdhijra0", 0, 0), Offset(w * 0.5f, ground - h * 0.42f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdhijra0", 0, 1), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            house12(Offset(w * 0.5f, ground), w * 0.34f, Color(0xFF5A4A3A), Color(0xFF3A2E24), f, lit = 0.3f)
            tagAt(a.tag("mdhijra1", 0, 0), Offset(w * 0.5f, ground - h * 0.3f), f * seg(p, 0.05f, 0.25f), ink = ArtInk.Ivory)
            for (k in 0 until 6) figure(Offset(w * (0.08f + 0.05f * k) + (if (k > 2) w * 0.6f else 0f), ground), h * 0.11f, Color(0xFF6A5A6A), f * (1f - 0.5f * seg(p, 0.4f, 0.6f)))
            val go = easeInOut(seg(p, 0.4f, 0.9f))
            sirajMunir(Offset(mix(w * 0.5f, w * 0.85f, go), mix(ground - h * 0.25f, h * 0.3f, go)), w * 0.025f, f, a.t, named = false)
            tagAt(a.tag("mdhijra1", 0, 1), Offset(w * 0.2f, h * 0.2f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawPath(Path().apply { moveTo(w * 0.05f, ground); lineTo(w * 0.25f, h * 0.42f); lineTo(w * 0.45f, ground); close() }, Color(0xFF4A3A30), alpha = f)
            drawOval(Color(0xFF0E0A08), Offset(w * 0.21f, h * 0.6f), Size(w * 0.08f, h * 0.06f), alpha = f)
            sirajMunir(Offset(w * 0.25f, h * 0.63f), w * 0.02f, f * (1f - seg(p, 0.55f, 0.65f)), a.t, named = false)
            for (k in 0 until 3) {
                drawCircle(Color(0xFFF6E7BE), w * 0.02f, Offset(w * (0.55f + 0.1f * k), h * 0.18f), alpha = f * seg(p, 0.1f + 0.12f * k, 0.22f + 0.12f * k))
            }
            val go = easeInOut(seg(p, 0.6f, 0.95f))
            sirajMunir(Offset(mix(w * 0.3f, w * 0.85f, go), ground - h * 0.1f), w * 0.022f, f * seg(p, 0.6f, 0.65f), a.t, named = false)
            house12(Offset(w * 0.88f, ground), w * 0.12f, Color(0xFFD8C0A0), Color(0xFF8A6A40), f * seg(p, 0.7f, 0.85f), lit = 0.6f)
            tagAt(a.tag("mdhijra2", 0, 0), Offset(w * 0.25f, h * 0.32f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdhijra2", 0, 1), Offset(w * 0.65f, h * 0.3f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            tagAt(a.tag("mdhijra2", 0, 2), Offset(w * 0.88f, ground - h * 0.2f), f * seg(p, 0.8f, 0.92f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val dawn = easeInOut(seg(p, 0f, 0.4f))
            drawRect(Color(0xFFF2D6A0), alpha = 0.5f * f * dawn)
            for (k in 0 until 4) palm12(Offset(w * (0.08f + 0.26f * k), ground), h * 0.2f, Color(0xFF6A4A30), Qn.Leaf, f, a.t)
            for (k in 0 until 10) figure(Offset(w * (0.15f + 0.07f * k), ground), h * 0.1f, Color(0xFFE8D8B8), f * seg(p, 0.1f, 0.3f), arms = 0.9f)
            camel26(Offset(w * 0.45f, ground), h * 0.16f, Color(0xFF7A5A3A), f * seg(p, 0.3f, 0.5f), a.t)
            drawRoundRect(Color(0xFF2A5A4A), Offset(w * 0.39f, ground - h * 0.02f), Size(w * 0.12f, h * 0.02f), CornerRadius(3f * density), alpha = f * seg(p, 0.45f, 0.6f))
            sirajMunir(Offset(w * 0.45f, ground - h * 0.26f), w * 0.025f, f, a.t, named = false)
            house12(Offset(w * 0.78f, ground), w * 0.16f, Color(0xFFD8C0A0), Color(0xFF8A6A40), f * seg(p, 0.6f, 0.75f), lit = 0.6f)
            tagAt(a.tag("mdhijra3", 0, 0), Offset(w * 0.45f, ground + h * 0.06f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
            tagAt(a.tag("mdhijra3", 0, 1), Offset(w * 0.78f, ground - h * 0.24f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
    )
}
