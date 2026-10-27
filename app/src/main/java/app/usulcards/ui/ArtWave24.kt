package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.drawText
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 24 (2026-10-27): «بصائر»، «الحياة والمعلومة». Inside a cell: the double helix opening into a ribbon of letters
 * that are read into a living shape, information that no scale can weigh, and Venter's work done inside a cell
 * that was already alive; a bridge across a gorge, its gaps stopped with question marks, and the solid ground of
 * what we know; a door marked «الْمُسْتَقْبَلُ» and a laboratory that makes nothing living; and a screen where
 * letters lock into a sentence chosen beforehand.
 */

private object CellC24 {
    val Plasma = Color(0xFF2A1430)
    val PlasmaLight = Color(0xFF4A2450)
    val Membrane = Color(0xFFE8C8F0)
    val Nucleus = Color(0xFF6A3A8A)
    val Mito = Color(0xFFE88A5A)
    val Strand1 = Color(0xFF7AC8FF)
    val Strand2 = Color(0xFFFFA0C8)
    val Bead = Color(0xFFFFD27A)
    val Glass = Color(0xFFBFD8F0)
    val Green = Color(0xFF7ED6A0)
    val Red = Color(0xFFE0604A)
    val Back = Color(0xE6140A1A)
}

/**
 * Inside a cell. Information (malumat): helix (a double helix turns, then opens into a straight ribbon on which the
 * letters A T G C run by in order), readout (the ribbon runs through a reader which strings beads that fold into a
 * shape — the code becomes the body), nomatter (a balance: «مَادَّةً» on one pan, «طَاقَةً» on the other; the letters
 * float above it and cannot be weighed), whence (a question mark glows over the letters). Venter (venter): livecell
 * (under a microscope's round view a cell already alive: its membrane breathes, organelles move), inject (a fine
 * needle puts a made strand into it, and the cell copies it again and again), insideonly (beside it a flask of
 * lifeless chemicals that does nothing, crossed out; the living cell ticked), notmade (a ghost of a new cell tries to
 * rise from the flask and is struck out).
 */
internal fun DrawScope.cellScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "helix" to Shot(Offset(w * 0.5f, h * 0.45f), 1.1f),
        "readout" to Shot(Offset(w * 0.6f, h * 0.55f), 1.2f),
        "nomatter" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "whence" to Shot(Offset(w * 0.5f, h * 0.4f), 1.35f),
        "livecell" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "inject" to Shot(Offset(w * 0.55f, h * 0.45f), 1.4f),
        "insideonly" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "notmade" to Shot(Offset(w * 0.27f, h * 0.5f), 1.5f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) {
        drawRect(Brush.radialGradient(listOf(CellC24.PlasmaLight, CellC24.Plasma), Offset(w * 0.5f, h * 0.5f), w * 0.75f))
        if (a.count("helix") > 0) cellCode(a) else cellVenter(a)
    }
}

private fun DrawScope.cellCode(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val hx = a.b("helix")
    val ro = a.b("readout")
    val nm = a.b("nomatter")
    val wh = a.b("whence")
    val open = easeInOut(seg(hx, 0.3f, 0.8f))
    val y = h * 0.3f
    val x0 = w * 0.06f
    val x1 = w * 0.94f
    // the helix, turning, then laid open as a ribbon
    for (k in 0 until 40) {
        val f = k / 39f
        val x = mix(x0, x1, f)
        val ph = f * 12f + t * 1.2f
        val amp = h * 0.08f * (1f - open)
        val a1 = Offset(x, y + sin(ph) * amp - h * 0.04f * open)
        val a2 = Offset(x, y - sin(ph) * amp + h * 0.04f * open)
        val o = easeOut(seg(hx, 0f, 0.2f))
        if (k % 2 == 0) drawLine(Color(0xFFB8A8D8), a1, a2, 1.2f * dp, alpha = 0.5f * o)
        drawCircle(CellC24.Strand1, 2.2f * dp, a1, alpha = o)
        drawCircle(CellC24.Strand2, 2.2f * dp, a2, alpha = o)
    }
    // the letters on the opened ribbon, running by
    val lt = easeOut(seg(hx, 0.6f, 0.9f))
    if (lt > 0f) {
        val run = t * 0.5f
        for (k in 0 until 16) {
            val f = fract(k / 16f - run * 0.05f)
            val x = mix(x0, x1, f)
            val tl = a.tag("helix", 0, (k * 5 + 1) % 4)
            if (tl != null) drawText(tl, color = Color(0xFFF2EAFF), topLeft = Offset(x - tl.size.width / 2f, y - tl.size.height / 2f), alpha = lt * bump(f).coerceAtLeast(0.3f))
        }
    }
    // read into a shape: beads strung and folded
    if (ro > 0f) {
        val reader = Offset(w * 0.5f, y + h * 0.1f)
        val o = easeOut(seg(ro, 0f, 0.2f))
        drawRoundRect(Color(0xFF8A6AB0), reader + Offset(-w * 0.05f, -h * 0.04f), Size(w * 0.1f, h * 0.08f), CornerRadius(h * 0.03f), alpha = o)
        val n = 18
        val made = seg(ro, 0.15f, 0.8f)
        val fold = easeInOut(seg(ro, 0.6f, 1f))
        val heart = Offset(w * 0.62f, h * 0.68f)
        for (k in 0 until n) {
            val f = k / (n - 1f)
            if (f > made) break
            val chain = reader + Offset(w * 0.05f + f * w * 0.35f, h * 0.06f + sin(f * 9f) * h * 0.03f)
            val ang = f * 2f * PI.toFloat()
            val hx2 = 16f * sin(ang).let { it * it * it }
            val hy2 = -(13f * cos(ang) - 5f * cos(2 * ang) - 2f * cos(3 * ang) - cos(4 * ang))
            val onHeart = heart + Offset(hx2, hy2) * (w * 0.006f)
            val p = chain + (onHeart - chain) * fold
            drawCircle(CellC24.Bead, w * 0.008f, p, alpha = o)
        }
        if (fold > 0.9f) softGlow(heart, w * 0.12f, Color(0xFFFF7A9A), 0.5f * seg(fold, 0.9f, 1f))
    }
    // information is neither matter nor energy
    if (nm > 0f) {
        val pivot = Offset(w * 0.5f, h * 0.55f)
        val o = easeOut(seg(nm, 0f, 0.25f)) * (1f - 0.5f * easeOut(seg(wh, 0f, 0.4f)))
        drawLine(ArtInk.Brass, pivot, pivot + Offset(0f, h * 0.33f), 3f * dp, alpha = o)
        drawLine(ArtInk.Brass, pivot + Offset(-w * 0.22f, 0f), pivot + Offset(w * 0.22f, 0f), 3f * dp, StrokeCap.Round, alpha = o)
        for (side in listOf(-1f, 1f)) {
            val pan = pivot + Offset(side * w * 0.22f, h * 0.2f)
            drawLine(ArtInk.Brass, pivot + Offset(side * w * 0.22f, 0f), pan, 1.2f * dp, alpha = o)
            drawArc(ArtInk.Brass, 0f, 180f, true, pan + Offset(-w * 0.07f, -h * 0.025f), Size(w * 0.14f, h * 0.05f), alpha = o)
            if (side < 0f) drawOval(Color(0xFF8A8478), Offset(pan.x - w * 0.035f, pan.y - h * 0.06f), Size(w * 0.07f, h * 0.05f), alpha = o)
            else {
                val fl = Path().apply {
                    moveTo(pan.x, pan.y - h * (0.09f + 0.01f * sin(t * 9f))); quadraticTo(pan.x + w * 0.03f, pan.y - h * 0.03f, pan.x, pan.y - h * 0.01f)
                    quadraticTo(pan.x - w * 0.03f, pan.y - h * 0.03f, pan.x, pan.y - h * (0.09f + 0.01f * sin(t * 9f)))
                }
                drawPath(fl, Color(0xFFFF9A4A), alpha = o)
            }
            tagAt(a.tag("nomatter", 0, if (side < 0f) 0 else 1), pan + Offset(0f, h * 0.08f), seg(nm, 0.2f + (side + 1f) * 0.1f, 0.4f + (side + 1f) * 0.1f), back = CellC24.Back, ink = ArtInk.Ivory)
        }
        // the letters hover above, weightless
        softGlow(Offset(w * 0.5f, y), w * 0.2f, Color(0xFFF2EAFF), 0.3f * o)
    }
    if (wh > 0f) {
        qmark21(Offset(w * 0.5f, h * 0.14f), h * 0.12f, ArtInk.Gold, easeOut(seg(wh, 0f, 0.4f)))
        softGlow(Offset(w * 0.5f, h * 0.14f), w * 0.08f, ArtInk.Gold, 0.4f * easeOut(seg(wh, 0f, 0.4f)))
    }
}

/** A living cell at [c], [r] across, pulsing; [alpha] fades it. */
private fun DrawScope.livingCell24(c: Offset, r: Float, t: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val breathe = 1f + 0.02f * sin(t * 2f)
    val path = Path().apply {
        for (k in 0..36) {
            val ang = k * 2f * PI.toFloat() / 36f
            val rr = r * breathe * (1f + 0.04f * sin(ang * 3f + t))
            val p = c + Offset(cos(ang) * rr, sin(ang) * rr * 0.8f)
            if (k == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
        }
        close()
    }
    drawPath(path, Color(0xFF6A3A7A), alpha = 0.5f * alpha)
    drawPath(path, CellC24.Membrane, alpha = alpha, style = Stroke(3f * density))
    drawCircle(CellC24.Nucleus, r * 0.3f, c + Offset(-r * 0.15f, 0f), alpha = alpha)
    for (k in 0 until 4) {
        val ang = t * 0.4f + k * 1.6f
        val p = c + Offset(cos(ang) * r * 0.55f, sin(ang) * r * 0.4f)
        withTransform({ rotate(ang * 57f, p) }) {
            drawOval(CellC24.Mito, Offset(p.x - r * 0.1f, p.y - r * 0.045f), Size(r * 0.2f, r * 0.09f), alpha = alpha)
        }
    }
}

private fun DrawScope.cellVenter(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val lc = a.b("livecell")
    val ij = a.b("inject")
    val io = a.b("insideonly")
    val nm = a.b("notmade")
    val split = easeInOut(seg(io, 0f, 0.4f))
    val cellC = Offset(mix(w * 0.5f, w * 0.7f, split), h * 0.5f)
    val r = mix(w * 0.26f, w * 0.18f, split)
    livingCell24(cellC, r, t, easeOut(seg(lc, 0f, 0.3f)))
    // the microscope's round view
    val view = 1f - split
    if (view > 0f) drawCircle(Color.Black, w * 0.75f, Offset(w * 0.5f, h * 0.5f), alpha = 0.85f * view, style = Stroke(w * 0.5f))
    // the needle and the made strand, copied over and over
    if (ij > 0f) {
        val tip = cellC + Offset(r * 0.2f, -r * 0.3f)
        val inn = easeOut(seg(ij, 0f, 0.3f))
        val out = easeOut(seg(ij, 0.45f, 0.7f))
        val back = Offset(w * 0.9f, h * 0.08f)
        val needleTip = back + (tip - back) * (inn * (1f - out * 0.6f))
        drawLine(Color(0xFFD8DCE8), back, needleTip, 2.5f * dp, StrokeCap.Round, alpha = 1f - split)
        val copies = (seg(ij, 0.4f, 1f) * 5).toInt() + if (io > 0f) 5 else 0
        for (k in 0 until min(copies + 1, 6)) {
            val p = cellC + Offset(r * (0.05f + 0.1f * (k % 3)), -r * 0.2f + r * 0.12f * (k / 3))
            drawPath(Path().apply { moveTo(p.x, p.y); for (m in 1..6) lineTo(p.x + m * r * 0.03f, p.y + sin(m * 1.5f + t * 3f) * r * 0.02f) }, CellC24.Strand1, style = Stroke(1.8f * dp, cap = StrokeCap.Round))
        }
    }
    // a flask of lifeless chemicals: nothing living comes of it
    if (io > 0f) {
        val f = Offset(w * 0.27f, h * 0.6f)
        val o = easeOut(seg(io, 0.2f, 0.5f))
        drawPath(Path().apply {
            moveTo(f.x - w * 0.03f, f.y - h * 0.2f); lineTo(f.x + w * 0.03f, f.y - h * 0.2f); lineTo(f.x + w * 0.03f, f.y - h * 0.1f)
            lineTo(f.x + w * 0.1f, f.y + h * 0.12f); lineTo(f.x - w * 0.1f, f.y + h * 0.12f); lineTo(f.x - w * 0.03f, f.y - h * 0.1f); close()
        }, CellC24.Glass, alpha = 0.2f * o)
        drawPath(Path().apply {
            moveTo(f.x - w * 0.03f, f.y - h * 0.2f); lineTo(f.x - w * 0.03f, f.y - h * 0.1f); lineTo(f.x - w * 0.1f, f.y + h * 0.12f)
            lineTo(f.x + w * 0.1f, f.y + h * 0.12f); lineTo(f.x + w * 0.03f, f.y - h * 0.1f); lineTo(f.x + w * 0.03f, f.y - h * 0.2f)
        }, CellC24.Glass, alpha = o, style = Stroke(2f * dp))
        drawRect(Color(0xFF7AA06A), Offset(f.x - w * 0.085f, f.y + h * 0.03f), Size(w * 0.17f, h * 0.085f), alpha = 0.6f * o)
        for (k in 0 until 4) {
            val ff = fract(t * 0.4f + k * 0.25f)
            drawCircle(Color.White, w * 0.005f, f + Offset(w * (-0.04f + 0.025f * k), h * 0.1f - ff * h * 0.06f), alpha = 0.5f * o * (1f - ff))
        }
        cross21(f + Offset(0f, -h * 0.27f), h * 0.035f, CellC24.Red, seg(io, 0.5f, 0.8f), 3f * dp)
        check21(cellC + Offset(0f, -r * 1.05f), h * 0.035f, CellC24.Green, seg(io, 0.6f, 0.9f), 3f * dp)
    }
    if (nm > 0f) {
        val c = Offset(w * 0.27f, h * 0.3f)
        val ghost = bump(seg(nm, 0f, 0.6f)) * (0.6f + 0.4f * sin(t * 20f))
        drawCircle(CellC24.Membrane, w * 0.05f, c, alpha = 0.4f * ghost, style = Stroke(1.6f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp))))
        cross21(c, w * 0.045f, CellC24.Red, seg(nm, 0.4f, 0.8f), 3f * dp)
    }
}

// ═════════════ 2. the bridge of gaps and the ground of what we know (bs-hayat) ═════════════

private object PlankC24 {
    val Sky = Color(0xFFB8CCE0)
    val SkyLow = Color(0xFFE8E0D0)
    val Rock = Color(0xFF7A6A5A)
    val RockDark = Color(0xFF4A3E34)
    val Plank = Color(0xFF9A6A3A)
    val Rope = Color(0xFF6A4A2A)
    val Ink = Color(0xFF2A2430)
    val Ghost = Color(0xFF8A8490)
    val Back = Color(0xE6201810)
}

/**
 * A gorge. Beats: chasm (two cliffs and the deep gorge between them; a man looks across), gapsbridge (a rope bridge
 * with planks missing: every gap stopped with a pale plank marked by a question mark, swaying), solid (on the near
 * side the man stands on firm ground of squared stones), infoblocks (the stones light up from inside, each lined with
 * a ribbon of code: we argue from what we know).
 */
internal fun DrawScope.planksScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "chasm" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "gapsbridge" to Shot(Offset(w * 0.5f, h * 0.48f), 1.3f),
        "solid" to Shot(Offset(w * 0.8f, h * 0.6f), 1.5f),
        "infoblocks" to Shot(Offset(w * 0.8f, h * 0.65f), 1.6f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { planksGorge(a) }
}

private fun DrawScope.planksGorge(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ch = a.b("chasm")
    val gb = a.b("gapsbridge")
    val so = a.b("solid")
    val ib = a.b("infoblocks")
    drawRect(Brush.verticalGradient(listOf(PlankC24.Sky, PlankC24.SkyLow)))
    drawRect(Brush.verticalGradient(listOf(Color(0xFF3A3028), Color(0xFF0E0A08)), startY = h * 0.45f, endY = h), Offset(w * 0.3f, h * 0.45f), Size(w * 0.4f, h * 0.55f))
    // the two cliffs
    for (side in listOf(-1f, 1f)) {
        val x = if (side < 0f) 0f else w * 0.68f
        drawPath(Path().apply {
            moveTo(x, h * 0.45f); lineTo(x + w * 0.32f, h * 0.45f)
            // the far face of each cliff slopes down into the gorge
            if (side < 0f) { lineTo(x + w * 0.29f, h); lineTo(x, h) } else { lineTo(x + w * 0.32f, h); lineTo(x + w * 0.04f, h) }
            close()
        }, Brush.verticalGradient(listOf(PlankC24.Rock, PlankC24.RockDark), startY = h * 0.45f, endY = h))
    }
    // the man on the near (right) edge
    val man = Offset(w * 0.8f, h * 0.45f)
    figure(man, h * 0.2f, PlankC24.Ink, easeOut(seg(ch, 0f, 0.3f)).coerceAtLeast(if (gb > 0f) 1f else 0f))
    // the bridge of gaps
    if (gb > 0f) {
        val o = easeOut(seg(gb, 0f, 0.3f))
        val l = Offset(w * 0.32f, h * 0.45f)
        val r = Offset(w * 0.68f, h * 0.45f)
        val sag = h * 0.06f
        val rope = Path().apply { moveTo(l.x, l.y - h * 0.05f); quadraticTo(w * 0.5f, l.y - h * 0.05f + sag * 2f, r.x, r.y - h * 0.05f) }
        drawPath(rope, PlankC24.Rope, alpha = o, style = Stroke(1.6f * dp))
        for (k in 0 until 9) {
            val f = (k + 0.5f) / 9f
            val p = Offset(mix(l.x, r.x, f), l.y + sag * 4f * f * (1f - f))
            val real = k % 3 == 0
            val sway = if (real) 0f else sin(t * 3f + k) * 6f
            withTransform({ rotate(sway, p) }) {
                drawRect(if (real) PlankC24.Plank else Color(0xFFDCD8E0), Offset(p.x - w * 0.016f, p.y - h * 0.008f), Size(w * 0.032f, h * 0.016f), alpha = o * (if (real) 1f else 0.6f))
            }
            if (!real) qmark21(p + Offset(0f, -h * 0.05f), h * 0.035f, PlankC24.Ghost, o * seg(gb, 0.2f + k * 0.05f, 0.35f + k * 0.05f))
        }
    }
    // the firm ground of squared stones, and the code that lights them
    if (so > 0f) {
        val o = easeOut(seg(so, 0f, 0.4f))
        for (r in 0 until 3) for (k in 0 until 4) {
            val x = w * (0.7f + 0.075f * k) + (if (r % 2 == 1) w * 0.035f else 0f)
            val y = h * (0.47f + 0.075f * r)
            drawRect(Color(0xFFB8A890), Offset(x, y), Size(w * 0.07f, h * 0.07f), alpha = o)
            drawRect(Color(0xFF6A5A48), Offset(x, y), Size(w * 0.07f, h * 0.07f), alpha = o, style = Stroke(1.2f * dp))
            val lit = easeOut(seg(ib, (r * 4 + k) / 16f, (r * 4 + k) / 16f + 0.3f))
            if (lit > 0f) {
                softGlow(Offset(x + w * 0.035f, y + h * 0.035f), w * 0.04f, ArtInk.Gold, 0.4f * lit)
                for (m in 0 until 3) drawLine(Color(0xFF7AC8FF), Offset(x + w * 0.01f, y + h * (0.018f + 0.017f * m)), Offset(x + w * 0.06f, y + h * (0.018f + 0.017f * m)), 1.4f * dp, alpha = lit, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 2f * dp)))
            }
        }
        check21(man + Offset(-w * 0.08f, -h * 0.17f), h * 0.03f, Color(0xFF3A9A6A), seg(so, 0.4f, 0.8f), 3f * dp)
    }
}

// ═════════════ 3. the door of the future and the laboratory (bs-mustaqbal) ═════════════

private object LabC24 {
    val Wall = Color(0xFFE6EAEE)
    val Floor = Color(0xFFB8C0C8)
    val Door = Color(0xFF6A4A2A)
    val Bench = Color(0xFF4A5A6A)
    val Glass = Color(0xFF9ACBEA)
    val Ink = Color(0xFF2A2430)
    val Gold = Color(0xFFE2B84A)
    val Red = Color(0xFFD0503C)
    val Back = Color(0xE6201810)
}

/**
 * Beats: waitdoor (a man waits before a closed door, «الْمُسْتَقْبَلُ», while calendar leaves fly off one after another),
 * labfail (a laboratory: flasks bubble, an arm stirs, lights blink — and the dish in the middle stays empty, crossed
 * out), prize (a trophy on a lit pedestal and a card with the sum, beside a strand of code with a question mark).
 */
internal fun DrawScope.labScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "waitdoor" to Shot(Offset(w * 0.3f, h * 0.55f), 1.35f),
        "labfail" to Shot(Offset(w * 0.6f, h * 0.6f), 1.25f),
        "prize" to Shot(Offset(w * 0.5f, h * 0.5f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { labRoom(a) }
}

private fun DrawScope.labRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val wd = a.b("waitdoor")
    val lf = a.b("labfail")
    val pz = a.b("prize")
    drawRect(LabC24.Wall)
    drawRect(LabC24.Floor, Offset(0f, h * 0.8f), Size(w, h * 0.2f))
    // the door of the future and the man who waits
    val door = Offset(w * 0.3f, h * 0.8f)
    drawRect(LabC24.Door, Offset(door.x - w * 0.08f, door.y - h * 0.5f), Size(w * 0.16f, h * 0.5f))
    drawCircle(LabC24.Gold, w * 0.007f, door + Offset(w * 0.055f, -h * 0.24f))
    tagAt(a.tag("waitdoor"), door + Offset(0f, -h * 0.56f), seg(wd, 0.1f, 0.3f) * (1f - seg(lf, 0f, 0.3f)), back = LabC24.Back, ink = ArtInk.BrassLight)
    val man = Offset(w * 0.15f, h * 0.88f)
    figure(man, h * 0.24f, LabC24.Ink, easeOut(seg(wd, 0f, 0.3f)).coerceAtLeast(if (lf > 0f) 1f else 0f), arms = -0.2f * sin(t))
    if (wd > 0f) {
        for (k in 0 until 6) {
            val f = seg(wd, 0.2f + k * 0.12f, 0.45f + k * 0.12f)
            if (f <= 0f || f >= 1f) continue
            val p = Offset(w * 0.45f + w * 0.2f * f, h * 0.2f + h * 0.1f * f - h * 0.15f * sin(PI.toFloat() * f))
            withTransform({ rotate(f * 200f, p) }) {
                drawRect(Color.White, Offset(p.x - w * 0.02f, p.y - h * 0.025f), Size(w * 0.04f, h * 0.05f), alpha = 1f - f)
                drawRect(Color(0xFFC0463A), Offset(p.x - w * 0.02f, p.y - h * 0.025f), Size(w * 0.04f, h * 0.01f), alpha = 1f - f)
            }
        }
    }
    // the laboratory that makes nothing living
    if (lf > 0f) {
        val o = easeOut(seg(lf, 0f, 0.3f))
        val bench = Offset(w * 0.62f, h * 0.68f)
        drawRect(LabC24.Bench, Offset(bench.x - w * 0.25f, bench.y), Size(w * 0.5f, h * 0.03f), alpha = o)
        for (k in 0 until 4) {
            val c = bench + Offset(w * (-0.2f + 0.13f * k), 0f)
            val col = listOf(Color(0xFF7AC8A0), Color(0xFFE8A05A), Color(0xFF9A8AE0), Color(0xFF6AB0E0))[k]
            drawPath(Path().apply {
                moveTo(c.x - w * 0.012f, c.y - h * 0.14f); lineTo(c.x + w * 0.012f, c.y - h * 0.14f); lineTo(c.x + w * 0.012f, c.y - h * 0.08f)
                lineTo(c.x + w * 0.04f, c.y); lineTo(c.x - w * 0.04f, c.y); lineTo(c.x - w * 0.012f, c.y - h * 0.08f); close()
            }, LabC24.Glass, alpha = 0.35f * o)
            drawRect(col, Offset(c.x - w * 0.033f, c.y - h * 0.035f), Size(w * 0.066f, h * 0.033f), alpha = 0.8f * o)
            for (m in 0 until 3) {
                val f = fract(t * 0.8f + m / 3f + k * 0.2f)
                drawCircle(Color.White, w * 0.004f, c + Offset(w * (-0.01f + 0.01f * m), -h * 0.04f - f * h * 0.12f), alpha = 0.6f * o * (1f - f))
            }
        }
        // the robot arm, stirring
        val pivot = bench + Offset(w * 0.24f, -h * 0.35f)
        val ang = 0.6f + 0.25f * sin(t * 2f)
        val elbow = pivot + Offset(-cos(ang) * w * 0.1f, sin(ang) * h * 0.12f)
        drawLine(Color(0xFF8A94A4), pivot, elbow, 4f * dp, StrokeCap.Round, alpha = o)
        drawLine(Color(0xFF8A94A4), elbow, elbow + Offset(-w * 0.04f, h * 0.12f), 3f * dp, StrokeCap.Round, alpha = o)
        // the dish that stays empty
        val dish = bench + Offset(0f, -h * 0.02f)
        drawOval(Color(0xFFF2F6F8), Offset(dish.x - w * 0.05f, dish.y - h * 0.02f), Size(w * 0.1f, h * 0.03f), alpha = o)
        drawOval(LabC24.Glass, Offset(dish.x - w * 0.05f, dish.y - h * 0.02f), Size(w * 0.1f, h * 0.03f), alpha = o, style = Stroke(1.4f * dp))
        cross21(dish + Offset(0f, -h * 0.08f), h * 0.035f, LabC24.Red, seg(lf, 0.6f, 0.9f), 3f * dp)
    }
    // the prize nobody has won
    if (pz > 0f) {
        val o = easeOut(seg(pz, 0f, 0.3f))
        val ped = Offset(w * 0.5f, h * 0.55f)
        drawRect(Color.Black, Offset(0f, 0f), Size(w, h), alpha = 0.45f * o)
        lightShafts(Offset(w * 0.5f, 0f), PI.toFloat() / 2, 0.5f, h * 0.7f, Color.White, o, t, rays = 5)
        drawRect(Color(0xFF3A3A48), Offset(ped.x - w * 0.06f, ped.y + h * 0.05f), Size(w * 0.12f, h * 0.2f), alpha = o)
        drawPath(Path().apply {
            moveTo(ped.x - w * 0.045f, ped.y - h * 0.12f); lineTo(ped.x + w * 0.045f, ped.y - h * 0.12f)
            quadraticTo(ped.x + w * 0.04f, ped.y - h * 0.02f, ped.x + w * 0.008f, ped.y); lineTo(ped.x + w * 0.008f, ped.y + h * 0.03f)
            lineTo(ped.x + w * 0.025f, ped.y + h * 0.05f); lineTo(ped.x - w * 0.025f, ped.y + h * 0.05f); lineTo(ped.x - w * 0.008f, ped.y + h * 0.03f)
            lineTo(ped.x - w * 0.008f, ped.y); quadraticTo(ped.x - w * 0.04f, ped.y - h * 0.02f, ped.x - w * 0.045f, ped.y - h * 0.12f); close()
        }, LabC24.Gold, alpha = o)
        glint(ped + Offset(-w * 0.02f, -h * 0.09f), w * 0.02f, Color.White, o * (0.5f + 0.5f * sin(t * 3f)))
        tagAt(a.tag("prize"), ped + Offset(0f, h * 0.32f), seg(pz, 0.3f, 0.5f), back = LabC24.Back, ink = ArtInk.BrassLight)
        // the strand whose origin is asked for
        val s = Offset(w * 0.78f, h * 0.45f)
        for (k in 0 until 14) {
            val y = s.y - h * 0.15f + k * h * 0.022f
            val dx = sin(k * 0.8f + t) * w * 0.03f
            drawCircle(Color(0xFF7AC8FF), 2f * dp, Offset(s.x + dx, y), alpha = o)
            drawCircle(Color(0xFFFFA0C8), 2f * dp, Offset(s.x - dx, y), alpha = o)
        }
        qmark21(s + Offset(w * 0.07f, -h * 0.15f), h * 0.06f, ArtInk.Ivory, seg(pz, 0.5f, 0.7f))
    }
}

// ═════════════ 4. the screen: letters locking into a chosen sentence (bs-dawkins) ═════════════

private object ScrC24 {
    val Room = Color(0xFF1A1E28)
    val Monitor = Color(0xFF2A2E38)
    val Screen = Color(0xFF0A1410)
    val Tile = Color(0xFF2E4A3A)
    val Lock = Color(0xFF6AE0A0)
    val Card = Color(0xFFF6EEDC)
    val Ink = Color(0xFFDCD4E8)
    val Back = Color(0xE6101418)
}

/**
 * Beats: scramble (on a screen a row of tiles flickers through random letters; one by one they stop on the right
 * letter and turn green), target (above the screen a card is pinned with the sentence it is aiming at; a line runs
 * from each tile to its letter on the card, keeping the right ones and throwing back the wrong), designer (the camera
 * draws back: a man at the keyboard, the cable to the wall, a lamp lit over his head — the aim was set before it ran).
 */
internal fun DrawScope.screenScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "scramble" to Shot(Offset(w * 0.5f, h * 0.5f), 1.35f),
        "target" to Shot(Offset(w * 0.5f, h * 0.38f), 1.25f),
        "designer" to Shot(Offset(w * 0.5f, h * 0.55f), 0.95f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { screenDesk(a) }
}

private fun DrawScope.screenDesk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sc = a.b("scramble")
    val tg = a.b("target")
    val de = a.b("designer")
    drawRect(ScrC24.Room)
    val mon = Offset(w * 0.5f, h * 0.5f)
    val mw = w * 0.56f
    val mh = h * 0.34f
    drawRoundRect(ScrC24.Monitor, Offset(mon.x - mw / 2 - w * 0.015f, mon.y - mh / 2 - h * 0.02f), Size(mw + w * 0.03f, mh + h * 0.04f), CornerRadius(w * 0.012f))
    drawRect(ScrC24.Screen, Offset(mon.x - mw / 2, mon.y - mh / 2), Size(mw, mh))
    drawRect(ScrC24.Monitor, Offset(mon.x - w * 0.02f, mon.y + mh / 2 + h * 0.02f), Size(w * 0.04f, h * 0.06f))
    // the row of tiles: random, then locked one by one
    val n = 28
    val tileW = mw * 0.9f / n
    for (k in 0 until n) {
        val x = mon.x - mw * 0.45f + k * tileW
        val lockAt = 0.15f + 0.8f * fract(k * 0.618f)
        val locked = sc >= lockAt
        val space = k == 8 || k == 11 || k == 14 || k == 19 || k == 21
        val col = when {
            locked && !space -> ScrC24.Lock
            locked && space -> ScrC24.Screen
            else -> lerpColor(ScrC24.Tile, Color(0xFF6A8A7A), 0.5f + 0.5f * sin(t * 20f + k * 3f))
        }
        val o = easeOut(seg(sc, 0f, 0.15f))
        drawRect(col, Offset(x + tileW * 0.1f, mon.y - mh * 0.06f), Size(tileW * 0.8f, mh * 0.12f), alpha = o)
    }
    // the sentence chosen beforehand, pinned above
    if (tg > 0f) {
        val card = Offset(w * 0.5f, h * 0.17f)
        val o = easeOut(seg(tg, 0f, 0.3f))
        val tl = a.tag("target")
        val cw = (tl?.size?.width?.toFloat() ?: w * 0.4f) + w * 0.06f
        drawRoundRect(ScrC24.Card, Offset(card.x - cw / 2, card.y - h * 0.05f), Size(cw, h * 0.1f), CornerRadius(w * 0.006f), alpha = o)
        drawCircle(Color(0xFFD0503C), w * 0.008f, card + Offset(0f, -h * 0.05f), alpha = o)
        if (tl != null) drawText(tl, color = Color(0xFF2A2430), topLeft = Offset(card.x - tl.size.width / 2f, card.y - tl.size.height / 2f), alpha = o)
        val lines = seg(tg, 0.3f, 0.9f)
        for (k in 0 until n step 3) {
            val f = k / (n - 1f)
            if (f > lines) break
            val from = Offset(card.x - cw * 0.45f + f * cw * 0.9f, card.y + h * 0.05f)
            val to = Offset(mon.x - mw * 0.45f + k * tileW + tileW / 2, mon.y - mh * 0.06f)
            drawLine(ScrC24.Lock, from, to, 0.8f * dp, alpha = 0.5f * o, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp)))
        }
        softGlow(card, cw * 0.6f, ArtInk.Gold, 0.3f * bump(seg(tg, 0.2f, 1f)))
    }
    // the man who set the aim
    if (de > 0f) {
        val o = easeOut(seg(de, 0f, 0.4f))
        val seat = Offset(w * 0.86f, h * 0.88f)
        w8Seated(seat, h * 0.3f, ScrC24.Ink, 1f, o)
        drawRoundRect(Color(0xFF3A3E48), Offset(w * 0.55f, h * 0.8f), Size(w * 0.18f, h * 0.03f), CornerRadius(h * 0.01f), alpha = o)
        drawPath(Path().apply { moveTo(mon.x - mw / 2, mon.y + mh * 0.3f); cubicTo(w * 0.1f, h * 0.8f, w * 0.05f, h * 0.95f, 0f, h * 0.9f) }, Color(0xFF4A4E58), alpha = o, style = Stroke(2.5f * dp))
        val lamp = seat + Offset(-w * 0.02f, -h * 0.4f)
        bloom(lamp, w * 0.015f, ArtInk.Gold, o * easeOut(seg(de, 0.4f, 0.8f)))
    }
}
