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
 * Wave 107 (2026-11-17): «من هديه ﷺ», his guidance in hair, dress, scent, sleep, fasting and weeping (Zad level 2).
 * He ﷺ is only his named light; his body is never drawn, so hair, beard and garments are shown as gauges, strands and
 * objects. Fatima and Ali are roundels in his family's colour, Abu Bakr, Umar and Uthman in theirs, Umm Salama and
 * Aisha in the Mothers' colours, every other named person the plain roundel.
 */

private val Hair107 = Color(0xFF3A2A1E)
private val White107 = Color(0xFFF2EEE4)
private val Silver107 = Color(0xFFC8CCD4)
private val Saffron107 = Color(0xFFE8A83A)
private val Henna107 = Color(0xFFB4532A)
private val Leather107 = Color(0xFF8A5A34)
private val Cloth107 = Color(0xFFE2D6BC)

private fun DrawScope.night107(a: ArtCtx, ground: Float) {
    nightSky(a.t, S97.Night, S97.NightLow, ground, 0.7f)
    dunes97(ground, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.day107(a: ArtCtx, ground: Float) {
    val w = size.width
    daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB09868))
    for (k in 0 until 3) house12(Offset(w * (0.12f + 0.38f * k), ground), w * 0.12f, Color(0xFFE2CCA0), Color(0xFF8A6A40), 0.55f)
    palm12(Offset(w * 0.93f, ground), size.height * 0.2f, Color(0xFF6A4A30), Qn.Leaf, 0.7f, a.t)
}

private fun DrawScope.veil107(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.6f * alpha)

private fun DrawScope.cross107(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawLine(Qn.Ember, c + Offset(-r, -r), c + Offset(r, r), 3f * density, StrokeCap.Round, alpha = alpha)
    drawLine(Qn.Ember, c + Offset(r, -r), c + Offset(-r, r), 3f * density, StrokeCap.Round, alpha = alpha)
}

/** A sandal seen from above: sole and two thongs (قبالان). [right] mirrors it. */
private fun DrawScope.sandal107(c: Offset, s: Float, alpha: Float, right: Boolean = true, straps: Float = 1f) {
    if (alpha <= 0.01f) return
    val m = if (right) 1f else -1f
    drawOval(Leather107, c + Offset(-s * 0.32f, -s), Size(s * 0.64f, s * 2f), alpha = alpha)
    drawOval(Color(0xFF6A4226), c + Offset(-s * 0.32f, -s), Size(s * 0.64f, s * 2f), alpha = alpha, style = Stroke(1.5f * density))
    val toe = c + Offset(m * s * 0.05f, -s * 0.62f)
    for (k in 0 until 2) {
        val side = c + Offset(m * s * (if (k == 0) 0.3f else -0.3f), -s * 0.05f)
        drawLine(Color(0xFF4A2E18), toe, side, 2.2f * density, StrokeCap.Round, alpha = alpha * straps)
    }
    drawCircle(Color(0xFF4A2E18), s * 0.06f, toe, alpha = alpha * straps)
}

/** A row of short hair strands; [white] of them (from the left) are white. */
private fun DrawScope.strands107(from: Offset, to: Offset, n: Int, len: Float, white: Int, alpha: Float, sway: Float) {
    for (k in 0 until n) {
        val f = k / (n - 1f)
        val root = Offset(mix(from.x, to.x, f), mix(from.y, to.y, f))
        val bend = sin(sway * 2f * PI.toFloat() + k) * len * 0.12f
        drawLine(if (k < white) White107 else Hair107, root, root + Offset(bend, len), 1.6f * density, StrokeCap.Round, alpha = alpha)
    }
}

/** A small glass bottle (قارورة), [fill] 0..1 of it filled with a golden drop. */
private fun DrawScope.bottle107(c: Offset, s: Float, fill: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Color(0xFF8AB0C0), c + Offset(-s * 0.35f, -s * 0.2f), Size(s * 0.7f, s * 0.9f), CornerRadius(s * 0.25f), alpha = 0.35f * alpha)
    val f = fill.coerceIn(0f, 1f)
    drawRoundRect(Qn.GoldLight, c + Offset(-s * 0.3f, s * 0.65f - s * 0.8f * f), Size(s * 0.6f, s * 0.8f * f), CornerRadius(s * 0.2f), alpha = 0.8f * alpha)
    drawRoundRect(Color(0xFFCFE4EC), c + Offset(-s * 0.35f, -s * 0.2f), Size(s * 0.7f, s * 0.9f), CornerRadius(s * 0.25f), alpha = alpha, style = Stroke(1.5f * density))
    drawRect(Color(0xFFCFE4EC), c + Offset(-s * 0.12f, -s * 0.42f), Size(s * 0.24f, s * 0.22f), alpha = alpha, style = Stroke(1.5f * density))
}

/** Fragrance: three rising wisps at [c]. */
private fun DrawScope.scent107(c: Offset, s: Float, t: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 3) {
        val ph = (t * 0.35f + k / 3f) % 1f
        val p = Path()
        val x0 = c.x + (k - 1) * s * 0.35f
        p.moveTo(x0, c.y)
        for (i in 1..8) {
            val y = c.y - s * 1.4f * i / 8f
            p.lineTo(x0 + sin(i * 0.9f + t * 2f + k) * s * 0.15f, y)
        }
        drawPath(p, Qn.GoldLight, alpha = alpha * (1f - ph) * 0.7f, style = Stroke(1.4f * density, cap = StrokeCap.Round))
    }
}

/** A falling tear-drop at [c], cycling with [t]. */
private fun DrawScope.tears107(c: Offset, s: Float, t: Float, alpha: Float, n: Int = 3) {
    for (k in 0 until n) {
        val ph = (t * 0.5f + k / n.toFloat()) % 1f
        qIcon("drop", c + Offset((k - (n - 1) / 2f) * s * 1.6f, s * 3f * ph), s, Color(0xFF9CC8E8), alpha * (1f - ph), t)
    }
}

// ═════════════ hd-shaar ═════════════
internal fun DrawScope.hdshaarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdshaar", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night107(a, ground) },
        { p, f ->
            veil107(f)
            // a gauge: the ear lobe line and the shoulder line, the hair's length lit between them
            val ear = h * 0.32f
            val shoulder = h * 0.6f
            drawLine(Silver107, Offset(w * 0.2f, ear), Offset(w * 0.8f, ear), 1.5f * density, alpha = f * seg(p, 0.05f, 0.2f))
            drawLine(Silver107, Offset(w * 0.2f, shoulder), Offset(w * 0.8f, shoulder), 1.5f * density, alpha = f * seg(p, 0.25f, 0.4f))
            val grow = easeInOut(seg(p, 0.45f, 0.75f))
            strands107(Offset(w * 0.38f, h * 0.2f), Offset(w * 0.62f, h * 0.2f), 14, mix(h * 0.08f, (ear + shoulder) / 2f - h * 0.2f, grow), 0, f * seg(p, 0.4f, 0.5f), a.t * 0.3f)
            softGlow(Offset(w * 0.5f, (ear + shoulder) / 2f), w * 0.1f, Qn.Gold, 0.3f * f * seg(p, 0.7f, 0.85f))
            tagAt(a.tag("hdshaar0", 0, 0), Offset(w * 0.5f, ear - h * 0.05f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdshaar0", 0, 1), Offset(w * 0.5f, shoulder + h * 0.06f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            val spots = listOf(Offset(w * 0.2f, h * 0.42f), Offset(w * 0.5f, h * 0.3f), Offset(w * 0.8f, h * 0.42f))
            spots.forEachIndexed { k, c ->
                val on = f * seg(p, 0.3f + 0.15f * k, 0.45f + 0.15f * k)
                strands107(c + Offset(-w * 0.07f, 0f), c + Offset(w * 0.07f, 0f), 9, h * 0.06f, 2 + k % 2, on, a.t * 0.3f)
                tagAt(a.tag("hdshaar1", 0, k + 1), c + Offset(0f, h * 0.12f), on, ink = ArtInk.Ivory)
            }
            tagAt(a.tag("hdshaar1", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            val part = easeInOut(seg(p, 0.45f, 0.75f))
            for (k in 0 until 16) {
                val x0 = w * (0.3f + 0.4f * k / 15f)
                val side = if (k < 8) -1f else 1f
                val top = Offset(x0, h * 0.28f)
                drawLine(Hair107, top, top + Offset(side * w * 0.07f * part, h * 0.24f), 1.8f * density, StrokeCap.Round, alpha = f * seg(p, 0.05f, 0.2f))
            }
            drawLine(Silver107, Offset(w * 0.5f, h * 0.24f), Offset(w * 0.5f, h * 0.3f), 2f * density, alpha = f * part)
            tagAt(a.tag("hdshaar2", 0, 0), Offset(w * 0.22f, h * 0.2f), f * seg(p, 0.1f, 0.25f) * (1f - 0.6f * part), ink = ArtInk.Ivory)
            tagAt(a.tag("hdshaar2", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
            tagAt(a.tag("hdshaar2", 0, 2), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil107(f)
            val c = Offset(w * 0.5f, h * 0.4f)
            drawCircle(Color(0xFFD8C4A8), w * 0.15f, c, alpha = f * seg(p, 0.05f, 0.2f), style = Stroke(2f * density))
            for (k in 0 until 3) drawCircle(Hair107, w * 0.05f, c + Offset(w * 0.07f * cos(k * 2.1f), w * 0.07f * sin(k * 2.1f)), alpha = f * seg(p, 0.15f, 0.3f))
            cross107(c, w * 0.1f, f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("hdshaar3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("hdshaar3", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ hd-lihya ═════════════
internal fun DrawScope.hdlihyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdlihya", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night107(a, ground) },
        { p, f ->
            veil107(f)
            // trimmed (short strands) beside left full (long strands)
            strands107(Offset(w * 0.18f, h * 0.36f), Offset(w * 0.38f, h * 0.36f), 10, h * 0.03f, 0, f * seg(p, 0.1f, 0.25f), a.t * 0.3f)
            strands107(Offset(w * 0.6f, h * 0.3f), Offset(w * 0.82f, h * 0.3f), 12, mix(h * 0.03f, h * 0.2f, easeInOut(seg(p, 0.5f, 0.8f))), 0, f * seg(p, 0.45f, 0.55f), a.t * 0.3f)
            tagAt(a.tag("hdlihya0", 0, 0), Offset(w * 0.28f, h * 0.6f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdlihya0", 0, 1), Offset(w * 0.71f, h * 0.6f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil107(f)
            sirajMunir(Offset(w * 0.5f, h * 0.24f), w * 0.03f, f, a.t, named = false)
            drawCircle(Saffron107, w * 0.07f, Offset(w * 0.3f, h * 0.46f), alpha = f * seg(p, 0.15f, 0.3f))
            qIcon("sprout", Offset(w * 0.7f, h * 0.46f), w * 0.08f, Color(0xFFD86A2A), f * seg(p, 0.45f, 0.6f), a.t)
            strands107(Offset(w * 0.42f, h * 0.36f), Offset(w * 0.58f, h * 0.36f), 9, h * 0.1f, 0, f * seg(p, 0.6f, 0.7f), a.t * 0.3f)
            drawRect(Saffron107, Offset(w * 0.42f, h * 0.36f), Size(w * 0.16f, h * 0.1f), alpha = 0.35f * f * seg(p, 0.7f, 0.9f))
            tagAt(a.tag("hdlihya1", 0, 0), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("hdlihya1", 0, 1), Offset(w * 0.7f, h * 0.6f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            drawLine(Color(0xFF8A8494), Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.72f), 1.5f * density, alpha = f)
            drawCircle(Color(0xFF101010), w * 0.07f, Offset(w * 0.27f, h * 0.42f), alpha = f * seg(p, 0.05f, 0.2f))
            drawCircle(Silver107, w * 0.07f, Offset(w * 0.27f, h * 0.42f), alpha = f * seg(p, 0.05f, 0.2f), style = Stroke(1.2f * density))
            cross107(Offset(w * 0.27f, h * 0.42f), w * 0.08f, f * seg(p, 0.3f, 0.42f))
            tagAt(a.tag("hdlihya2", 0, 0), Offset(w * 0.27f, h * 0.6f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            drawCircle(Henna107, w * 0.07f, Offset(w * 0.73f, h * 0.42f), alpha = f * seg(p, 0.5f, 0.65f))
            softGlow(Offset(w * 0.73f, h * 0.42f), w * 0.12f, Qn.Jade, 0.35f * f * seg(p, 0.6f, 0.85f))
            tagAt(a.tag("hdlihya2", 0, 1), Offset(w * 0.73f, h * 0.6f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil107(f)
            mother102(Offset(w * 0.32f, h * 0.42f), w * 0.09f, a, "hdlihya3", 0, 4, f * seg(p, 0.05f, 0.25f))
            val open = seg(p, 0.35f, 0.6f)
            drawRoundRect(Cloth107, Offset(w * 0.56f, h * 0.38f), Size(w * 0.26f, h * 0.1f), CornerRadius(6f * density), alpha = f * open)
            for (k in 0 until 3) drawLine(Henna107, Offset(w * (0.6f + 0.06f * k), h * 0.41f), Offset(w * (0.64f + 0.06f * k), h * 0.45f), 2f * density, StrokeCap.Round, alpha = f * seg(p, 0.5f + 0.05f * k, 0.6f + 0.05f * k))
            tagAt(a.tag("hdlihya3", 0, 1), Offset(w * 0.69f, h * 0.58f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ hd-khatam ═════════════
internal fun DrawScope.hdkhatamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdkhatam", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night107(a, ground) },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.38f), w * 0.05f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.14f))
            val egg = f * seg(p, 0.35f, 0.55f)
            softGlow(Offset(w * 0.5f, h * 0.38f), w * 0.1f, Qn.GoldLight, 0.4f * egg)
            drawOval(Qn.GoldLight, Offset(w * 0.46f, h * 0.35f), Size(w * 0.08f, h * 0.06f), alpha = egg, style = Stroke(1.8f * density))
            tagAt(a.tag("hdkhatam0", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("hdkhatam0", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.3f, h * 0.45f), w * 0.035f, f, a.t, named = false)
            plain97(Offset(w * 0.7f, h * 0.42f), w * 0.09f, a, "hdkhatam1", 0, f * seg(p, 0.15f, 0.35f))
            tagAt(a.tag("hdkhatam1", 0, 1), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            val c = Offset(w * 0.5f, h * 0.42f)
            val r = w * 0.16f
            drawCircle(Silver107, r, c, alpha = f * seg(p, 0.05f, 0.2f), style = Stroke(5f * density))
            drawCircle(Color(0xFF2A2E3A), r * 0.8f, c, alpha = f * seg(p, 0.1f, 0.25f))
            // the seal reads from top: الله، رسول، محمد (the Name on top)
            for (k in 0 until 3) tagAt(a.tag("hdkhatam2", 0, k), c + Offset(0f, r * (k - 1) * 0.5f), f * seg(p, 0.3f + 0.12f * k, 0.42f + 0.12f * k), back = Color(0x00000000), ink = ArtInk.Ivory)
            tagAt(a.tag("hdkhatam2", 0, 3), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            val pts = listOf(Offset(w * 0.2f, h * 0.36f), Offset(w * 0.42f, h * 0.36f), Offset(w * 0.64f, h * 0.36f))
            pts.forEachIndexed { k, c -> close97(c, w * 0.075f, a, "hdkhatam3", k, k, f * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k)) }
            val well = Offset(w * 0.84f, h * 0.62f)
            drawRoundRect(Color(0xFF8A7A66), well + Offset(-w * 0.08f, -h * 0.03f), Size(w * 0.16f, h * 0.08f), CornerRadius(4f * density), alpha = f * seg(p, 0.5f, 0.6f))
            drawOval(Color(0xFF0A0E18), well + Offset(-w * 0.065f, -h * 0.045f), Size(w * 0.13f, h * 0.03f), alpha = f * seg(p, 0.5f, 0.6f))
            val move = seg(p, 0.1f, 0.7f) * 3f
            val i = move.toInt().coerceAtMost(2)
            val from = pts[i]
            val to = if (i < 2) pts[i + 1] else well
            val ring = Offset(mix(from.x, to.x, easeInOut(move - i)), mix(from.y, to.y, easeInOut(move - i)) - h * 0.1f * sin(PI.toFloat() * (move - i)))
            drawCircle(Silver107, w * 0.025f, ring + Offset(0f, -h * 0.1f), alpha = f * (1f - seg(p, 0.78f, 0.88f)), style = Stroke(2.5f * density))
            tagAt(a.tag("hdkhatam3", 0, 3), well + Offset(0f, h * 0.1f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ hd-naal ═════════════
internal fun DrawScope.hdnaalScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdnaal", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night107(a, ground) },
        { p, f ->
            veil107(f)
            sandal107(Offset(w * 0.5f, h * 0.42f), h * 0.14f, f * seg(p, 0.05f, 0.25f), straps = seg(p, 0.3f, 0.5f))
            tagAt(a.tag("hdnaal0", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            sandal107(Offset(w * 0.36f, h * 0.4f), h * 0.1f, f * seg(p, 0.25f, 0.4f), right = false)
            sandal107(Offset(w * 0.64f, h * 0.4f), h * 0.1f, f * seg(p, 0.05f, 0.2f))
            softGlow(Offset(w * 0.64f, h * 0.4f), w * 0.1f, Qn.Jade, 0.35f * f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("hdnaal1", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("hdnaal1", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            sandal107(Offset(w * 0.25f, h * 0.42f), h * 0.09f, f * seg(p, 0.05f, 0.2f))
            cross107(Offset(w * 0.25f, h * 0.42f), w * 0.07f, f * seg(p, 0.2f, 0.3f))
            tagAt(a.tag("hdnaal2", 0, 0), Offset(w * 0.25f, h * 0.62f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            sandal107(Offset(w * 0.62f, h * 0.36f), h * 0.07f, f * seg(p, 0.4f, 0.55f), right = false)
            sandal107(Offset(w * 0.76f, h * 0.36f), h * 0.07f, f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("hdnaal2", 0, 1), Offset(w * 0.69f, h * 0.54f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("hdnaal2", 0, 2), Offset(w * 0.69f, h * 0.64f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil107(f)
            sirajMunir(Offset(w * 0.25f, h * 0.4f), w * 0.035f, f, a.t, named = false)
            plain97(Offset(w * 0.68f, h * 0.36f), w * 0.09f, a, "hdnaal3", 0, f * seg(p, 0.05f, 0.25f))
            sandal107(Offset(w * 0.62f, h * 0.58f), h * 0.05f, f * seg(p, 0.35f, 0.5f), right = false)
            sandal107(Offset(w * 0.74f, h * 0.58f), h * 0.05f, f * seg(p, 0.35f, 0.5f))
        },
    )
}

// ═════════════ hd-isbal ═════════════
internal fun DrawScope.hdisbalScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    val knee = h * 0.3f
    val mid = h * 0.47f
    val ankle = h * 0.64f
    val x = w * 0.42f
    fun DrawScope.gauge(f: Float) {
        drawLine(Color(0xFFD8C4A8), Offset(x, h * 0.18f), Offset(x, ankle), 6f * density, StrokeCap.Round, alpha = f)
        drawCircle(Color(0xFFD8C4A8), 5f * density, Offset(x + 4f * density, ankle), alpha = f)
        for (y in listOf(knee, mid, ankle)) drawLine(Silver107, Offset(x - w * 0.08f, y), Offset(x + w * 0.08f, y), 1.2f * density, alpha = 0.7f * f)
    }
    fun DrawScope.hem(to: Float, f: Float, color: Color) {
        drawRect(color, Offset(x - w * 0.06f, h * 0.16f), Size(w * 0.12f, to - h * 0.16f), alpha = 0.8f * f)
    }
    film98(a, "hdisbal", List(4) { Shot(Offset(w * 0.5f, h * 0.45f), 1f) },
        { night107(a, ground) },
        { p, f ->
            veil107(f)
            gauge(f * seg(p, 0.05f, 0.2f))
            hem(mix(knee, mid, easeInOut(seg(p, 0.15f, 0.45f))), f * seg(p, 0.15f, 0.25f), Cloth107)
            softGlow(Offset(x, mid), w * 0.08f, Qn.Jade, 0.4f * f * seg(p, 0.4f, 0.55f))
            drawRect(Qn.Jade, Offset(x + w * 0.1f, mid), Size(w * 0.02f, ankle - mid), alpha = 0.6f * f * seg(p, 0.55f, 0.7f))
            tagAt(a.tag("hdisbal0", 0, 0), Offset(w * 0.72f, mid - h * 0.04f), f * seg(p, 0.35f, 0.5f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("hdisbal0", 0, 1), Offset(w * 0.72f, (mid + ankle) / 2f + h * 0.02f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdisbal0", 0, 2), Offset(w * 0.72f, ankle + h * 0.05f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            gauge(f)
            hem(mix(mid, ankle + h * 0.08f, easeInOut(seg(p, 0.05f, 0.4f))), f, Cloth107)
            qIcon("flame", Offset(x, ankle + h * 0.13f), w * 0.07f, Qn.Ember, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("hdisbal1", 0, 0), Offset(w * 0.74f, ankle + h * 0.04f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("crown", Offset(w * 0.5f, h * 0.18f), w * 0.07f, Qn.Ember, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("hdisbal2", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("hdisbal2", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            for (k in 0 until 3) {
                val c = Offset(w * (0.2f + 0.3f * k), h * 0.45f)
                tagAt(a.tag("hdisbal3", 0, k), c, f * seg(p, 0.15f + 0.2f * k, 0.3f + 0.2f * k), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            }
            drawLine(Color(0xFF8A8494), Offset(w * 0.1f, h * 0.3f), Offset(w * 0.9f, h * 0.3f), 1.2f * density, alpha = f * seg(p, 0.05f, 0.2f))
        },
    )
}

// ═════════════ hd-tib ═════════════
internal fun DrawScope.hdtibScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdtib", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night107(a, ground) },
        { p, f ->
            veil107(f)
            sirajMunir(Offset(w * 0.5f, h * 0.36f), w * 0.04f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.14f))
            scent107(Offset(w * 0.5f, h * 0.3f), w * 0.06f, a.t, f * seg(p, 0.6f, 0.75f))
            for (k in 0 until 2) {
                val c = Offset(w * (0.18f + 0.64f * k), h * 0.48f)
                drawRoundRect(if (k == 0) Color(0xFFD8B8D0) else Color(0xFFC8A060), c + Offset(-w * 0.08f, -h * 0.03f), Size(w * 0.16f, h * 0.06f), CornerRadius(8f * density), alpha = f * seg(p, 0.1f + 0.15f * k, 0.25f + 0.15f * k) * (1f - 0.5f * seg(p, 0.55f, 0.7f)))
                tagAt(a.tag("hdtib0", 0, k), c + Offset(0f, h * 0.08f), f * seg(p, 0.1f + 0.15f * k, 0.25f + 0.15f * k), ink = ArtInk.Ivory)
            }
            tagAt(a.tag("hdtib0", 0, 2), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            sirajMunir(Offset(w * 0.25f, h * 0.42f), w * 0.03f, f, a.t, named = false)
            qIcon("bed", Offset(w * 0.25f, h * 0.52f), w * 0.1f, Qn.Lapis, f * seg(p, 0.05f, 0.2f), a.t)
            plain97(Offset(w * 0.72f, h * 0.3f), w * 0.075f, a, "hdtib1", 0, f * seg(p, 0.15f, 0.35f))
            bottle107(Offset(w * 0.72f, h * 0.52f), w * 0.07f, seg(p, 0.35f, 0.8f), f * seg(p, 0.3f, 0.4f))
            scent107(Offset(w * 0.72f, h * 0.48f), w * 0.04f, a.t, f * seg(p, 0.7f, 0.85f))
            tagAt(a.tag("hdtib1", 0, 1), Offset(w * 0.72f, h * 0.68f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            day107(a, ground)
            val go = easeInOut(seg(p, 0.05f, 0.55f))
            val pos = Offset(mix(w * 0.12f, w * 0.85f, go), ground - h * 0.12f)
            sirajMunir(pos, w * 0.026f, f * (1f - seg(p, 0.6f, 0.75f)), a.t, named = false)
            for (k in 0 until 5) {
                val x = w * (0.15f + 0.15f * k)
                if (x < pos.x) scent107(Offset(x, ground - h * 0.06f), w * 0.03f, a.t + k, f * 0.8f)
            }
            tagAt(a.tag("hdtib2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            for (k in 0 until 2) {
                val c = Offset(w * (0.32f + 0.36f * k), h * 0.42f)
                drawOval(if (k == 0) White107 else Color(0xFFB07AA0), c + Offset(-w * 0.06f, -h * 0.035f), Size(w * 0.12f, h * 0.07f), alpha = f * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k))
                cross107(c, w * 0.07f, f * seg(p, 0.45f, 0.55f))
                tagAt(a.tag("hdtib3", 0, k), c + Offset(0f, h * 0.12f), f * seg(p, 0.1f + 0.15f * k, 0.25f + 0.15f * k), ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ hd-nawm ═════════════
internal fun DrawScope.hdnawmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdnawm", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night107(a, ground); moon12(Offset(w * 0.82f, h * 0.14f), w * 0.04f, 0.8f) },
        { p, f ->
            qIcon("bed", Offset(w * 0.5f, h * 0.5f), w * 0.2f, Qn.Lapis, f * seg(p, 0.05f, 0.2f), a.t)
            sirajMunir(Offset(w * 0.62f, h * 0.44f), w * 0.03f, f, a.t, named = false)
            drawLine(Qn.Jade, Offset(w * 0.3f, h * 0.62f), Offset(w * 0.7f, h * 0.62f), 2f * density, StrokeCap.Round, alpha = f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("hdnawm0", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("hdnawm0", 0, 1), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            qIcon("hands", Offset(w * 0.5f, h * 0.42f), w * 0.13f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 3) {
                val on = seg(p, 0.25f + 0.15f * k, 0.35f + 0.15f * k)
                drawCircle(Qn.GoldLight, w * 0.015f, Offset(w * (0.42f + 0.08f * k), h * 0.3f), alpha = f * on)
            }
            tagAt(a.tag("hdnawm1", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("hdnawm1", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            val icons = listOf("drop", "bed", "cross")
            for (k in 0 until 3) {
                val c = Offset(w * (0.2f + 0.3f * k), h * 0.42f)
                val on = f * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k)
                val shake = if (k == 1) sin(a.t * 18f) * w * 0.006f * seg(p, 0.4f, 0.6f) else 0f
                qIcon(icons[k], c + Offset(shake, 0f), w * 0.08f, if (k == 2) Qn.Ember else Qn.Jade, on, a.t)
                tagAt(a.tag("hdnawm2", 0, k), c + Offset(0f, h * 0.14f), on, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil107(f)
            // the dhikr above, the two roundels below it (the Name is never placed low)
            for (k in 0 until 3) tagAt(a.tag("hdnawm3", 0, 2 + k), Offset(w * 0.5f, h * (0.2f + 0.1f * k)), f * seg(p, 0.35f + 0.15f * k, 0.5f + 0.15f * k), ink = ArtInk.Gold)
            close97(Offset(w * 0.33f, h * 0.64f), w * 0.07f, a, "hdnawm3", 0, 3, f * seg(p, 0.05f, 0.2f))
            close97(Offset(w * 0.67f, h * 0.64f), w * 0.07f, a, "hdnawm3", 1, 3, f * seg(p, 0.1f, 0.25f))
        },
    )
}

// ═════════════ hd-yaqza ═════════════
internal fun DrawScope.hdyaqzaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdyaqza", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { day107(a, ground) },
        { p, f ->
            val rise = easeInOut(seg(p, 0.1f, 0.6f))
            drawRect(Color(0xFF14102A), alpha = 0.6f * f * (1f - rise))
            qIcon("sun", Offset(w * 0.5f, mix(ground, h * 0.32f, rise)), w * 0.1f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("hdyaqza0", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("hdyaqza0", 0, 1), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            sirajMunir(Offset(w * 0.5f, h * 0.3f), w * 0.03f, f, a.t, named = false)
            val brush = sin(a.t * 6f) * w * 0.02f
            drawLine(Color(0xFFB08A5A), Offset(w * 0.3f + brush, h * 0.5f), Offset(w * 0.5f + brush, h * 0.5f), 5f * density, StrokeCap.Round, alpha = f * seg(p, 0.05f, 0.2f))
            drawLine(Color(0xFFE8DCC0), Offset(w * 0.3f + brush, h * 0.5f), Offset(w * 0.33f + brush, h * 0.5f), 6f * density, StrokeCap.Round, alpha = f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("hdyaqza1", 0, 0), Offset(w * 0.4f, h * 0.6f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("hdyaqza1", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            qIcon("hands", Offset(w * 0.5f, h * 0.44f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tears107(Offset(w * 0.5f, h * 0.28f), w * 0.025f, a.t, f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("hdyaqza2", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("hdyaqza2", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            drawLine(Color(0xFFB09868), Offset(w * 0.1f, h * 0.3f), Offset(w * 0.9f, h * 0.3f), 3f * density, alpha = f * seg(p, 0.05f, 0.15f))
            for (k in 0 until 3) {
                val c = Offset(w * (0.25f + 0.25f * k), h * 0.3f)
                val undo = seg(p, 0.2f + 0.22f * k, 0.35f + 0.22f * k)
                drawCircle(Color(0xFF6A4A30), w * 0.035f * (1f - undo), c, alpha = f)
                softGlow(c, w * 0.05f, Qn.Jade, 0.4f * f * undo)
                tagAt(a.tag("hdyaqza3", 0, k), c + Offset(0f, h * 0.12f), f * undo, back = Color(0xCC1A3020), ink = ArtInk.Jade)
            }
        },
    )
}

// ═════════════ hd-sawm ═════════════
internal fun DrawScope.hdsawmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdsawm", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night107(a, ground) },
        { p, f ->
            veil107(f)
            for (m in 0 until 12) {
                val c = Offset(w * (0.1f + 0.072f * m), h * 0.4f)
                val ram = m == 8
                val fill = if (ram) seg(p, 0.3f, 0.6f) else 0.3f
                drawRoundRect(if (ram) Qn.Gold else Color(0xFF4A5068), c + Offset(-w * 0.03f, -h * 0.08f), Size(w * 0.06f, h * 0.16f * fill), CornerRadius(3f * density), alpha = f * seg(p, 0.05f + 0.01f * m, 0.15f + 0.01f * m))
            }
            qIcon("crescent", Offset(w * 0.1f + w * 0.072f * 8, h * 0.24f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("hdsawm0", 0, 0), Offset(w * 0.1f + w * 0.072f * 8, h * 0.62f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            for (d in 0 until 15) {
                val c = Offset(w * (0.12f + 0.054f * d), h * 0.42f)
                val fast = (d / 4) % 2 == 0
                drawCircle(if (fast) Qn.GoldLight else Color(0xFF4A5068), w * 0.018f, c, alpha = f * seg(p, 0.05f + 0.035f * d, 0.12f + 0.035f * d))
            }
            tagAt(a.tag("hdsawm1", 0, 0), Offset(w * 0.3f, h * 0.58f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("hdsawm1", 0, 1), Offset(w * 0.7f, h * 0.58f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            plain97(Offset(w * 0.5f, h * 0.22f), w * 0.075f, a, "hdsawm2", 0, f * seg(p, 0.05f, 0.2f))
            val icons = listOf("crescent", "sun", "moon")
            for (k in 0 until 3) {
                val c = Offset(w * (0.2f + 0.3f * k), h * 0.48f)
                val on = f * seg(p, 0.25f + 0.2f * k, 0.4f + 0.2f * k)
                qIcon(icons[k], c, w * 0.07f, Qn.GoldLight, on, a.t)
                tagAt(a.tag("hdsawm2", 0, 1 + k), c + Offset(0f, h * 0.13f), on, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil107(f)
            for (k in 0 until 6) {
                val row = k / 3
                val c = Offset(w * (0.22f + 0.28f * (k % 3)), h * (0.34f + 0.2f * row))
                tagAt(a.tag("hdsawm3", 0, k), c, f * seg(p, 0.05f + 0.12f * k, 0.15f + 0.12f * k), back = if (row == 0) Color(0xCC3A2A10) else Color(0xCC1A2A40), ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ hd-bika ═════════════
internal fun DrawScope.hdbikaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdbika", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night107(a, ground) },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.32f), w * 0.04f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.12f))
            tears107(Offset(w * 0.5f, h * 0.42f), w * 0.02f, a.t, f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("hdbika0", 0, 0), Offset(w * 0.3f, h * 0.66f), f * seg(p, 0.4f, 0.55f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("hdbika0", 0, 1), Offset(w * 0.7f, h * 0.66f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            sirajMunir(Offset(w * 0.3f, h * 0.38f), w * 0.035f, f, a.t, named = false)
            tears107(Offset(w * 0.3f, h * 0.46f), w * 0.018f, a.t, f, n = 2)
            plain97(Offset(w * 0.7f, h * 0.38f), w * 0.08f, a, "hdbika1", 0, f * seg(p, 0.1f, 0.3f))
            for (k in 0 until 3) drawArc(Qn.Ember, -50f, 100f, false, Offset(w * 0.78f + w * 0.03f * k, h * 0.32f - h * 0.02f * k), Size(w * 0.06f, h * 0.12f + h * 0.04f * k), alpha = 0.6f * f * seg(p, 0.25f, 0.4f) * (1f - seg(p, 0.5f, 0.65f)), style = Stroke(1.5f * density))
            tagAt(a.tag("hdbika1", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            sirajMunir(Offset(w * 0.32f, h * 0.38f), w * 0.035f, f, a.t, named = false)
            tears107(Offset(w * 0.32f, h * 0.46f), w * 0.018f, a.t, f * seg(p, 0.4f, 0.55f), n = 2)
            plain97(Offset(w * 0.68f, h * 0.4f), w * 0.085f, a, "hdbika2", 0, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("hdbika2", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val mound = Offset(w * 0.5f, ground)
            drawArc(Color(0xFF6A5640), 180f, 180f, true, mound + Offset(-w * 0.18f, -h * 0.07f), Size(w * 0.36f, h * 0.14f), alpha = f * seg(p, 0.05f, 0.2f))
            sirajMunir(Offset(w * 0.5f, h * 0.48f), w * 0.03f, f, a.t, named = false)
            tears107(Offset(w * 0.5f, h * 0.56f), w * 0.018f, a.t, f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("hdbika3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ hd-mirath ═════════════
internal fun DrawScope.hdmirathScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdmirath", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night107(a, ground) },
        { p, f ->
            veil107(f)
            close97(Offset(w * 0.3f, h * 0.4f), w * 0.085f, a, "hdmirath0", 0, 3, f * seg(p, 0.05f, 0.25f))
            close97(Offset(w * 0.7f, h * 0.4f), w * 0.085f, a, "hdmirath0", 1, 0, f * seg(p, 0.25f, 0.45f))
            drawLine(Qn.GoldLight, Offset(w * 0.4f, h * 0.4f), Offset(w * 0.6f, h * 0.4f), 1.5f * density, alpha = 0.6f * f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("hdmirath0", 0, 2), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            sirajMunir(Offset(w * 0.5f, h * 0.24f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.1f))
            for (k in 0 until 5) {
                val go = easeInOut(seg(p, 0.35f + 0.06f * k, 0.7f + 0.06f * k))
                val to = Offset(w * (0.14f + 0.18f * k), h * 0.6f)
                qIcon("coin", Offset(mix(w * 0.5f, to.x, go), mix(h * 0.34f, to.y, go)), w * 0.035f, Qn.Gold, f * seg(p, 0.3f, 0.4f), a.t)
                qIcon("people", to + Offset(0f, h * 0.08f), w * 0.05f, Qn.Person, f * seg(p, 0.6f, 0.75f), a.t)
            }
            tagAt(a.tag("hdmirath1", 0, 0), Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("hdmirath1", 0, 1), Offset(w * 0.5f, h * 0.8f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil107(f)
            close97(Offset(w * 0.5f, h * 0.28f), w * 0.085f, a, "hdmirath2", 0, 0, f * seg(p, 0.05f, 0.25f))
            for (k in 0 until 3) {
                val c = Offset(w * (0.22f + 0.28f * k), h * 0.6f)
                val on = f * seg(p, 0.3f + 0.12f * k, 0.45f + 0.12f * k)
                drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.36f), c + Offset(0f, -h * 0.06f), 1.2f * density, alpha = 0.6f * on)
                qIcon("house", c, w * 0.06f, Color(0xFFE2CCA0), on, a.t)
            }
            tagAt(a.tag("hdmirath2", 0, 1), Offset(w * 0.3f, h * 0.76f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("hdmirath2", 0, 2), Offset(w * 0.7f, h * 0.76f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil107(f)
            for (k in 0 until 7) {
                val row = if (k < 4) 0 else 1
                val col = if (row == 0) k else k - 4
                val n = if (row == 0) 4 else 3
                val c = Offset(w * (0.5f + (col - (n - 1) / 2f) * 0.22f), h * (0.34f + 0.24f * row))
                val g = f * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k)
                when (k) {
                    0, 1 -> close97(c, w * 0.07f, a, "hdmirath3", k, k + 1, g)
                    6 -> mother102(c, w * 0.07f, a, "hdmirath3", k, 1, g)
                    else -> plain97(c, w * 0.07f, a, "hdmirath3", k, g)
                }
            }
        },
    )
}
