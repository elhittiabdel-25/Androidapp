package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/*
 * Shared pieces for the v1.2 scenes (waves 46 on: Riyad al-Salihin, Hisn al-Muslim, the Forty Nawawi, the Shama'il):
 * people in a few strokes (sitting, praying, bowing, a woman's modest silhouette, a child), a date palm, a house, a
 * door that opens on light, a mosque, the crescent and the sun, raised hands, prayer beads, a cup, a balance and a
 * dome of light that guards. Faces are never drawn; the Prophet ﷺ is only light (sirajMunir); Allah is never drawn.
 */

/** A figure sitting on the ground at [seat], knees toward [facing] (-1 left, 1 right); [bob] shakes it a little. */
internal fun DrawScope.sit12(seat: Offset, tall: Float, color: Color, alpha: Float, facing: Float, bob: Float = 0f) {
    if (alpha <= 0.01f) return
    val sw = max(1.7f * density, tall * 0.065f)
    val hip = seat - Offset(0f, tall * 0.04f)
    val knee = hip + Offset(facing * tall * 0.24f, -tall * 0.13f)
    val foot = Offset(knee.x + facing * tall * 0.06f, seat.y)
    val shoulder = hip + Offset(-facing * tall * 0.03f, -tall * 0.33f + bob)
    val head = shoulder + Offset(-facing * tall * 0.01f, -tall * 0.15f + bob * 0.5f)
    drawLine(color, hip, knee, sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, knee, foot, sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, shoulder, sw, StrokeCap.Round, alpha = alpha)
    drawCircle(color, tall * 0.1f, head, alpha = alpha)
    drawLine(color, shoulder, knee + Offset(-facing * tall * 0.02f, -tall * 0.02f), sw * 0.9f, StrokeCap.Round, alpha = alpha)
}

/** A figure standing in prayer, hands folded on the chest. */
internal fun DrawScope.pray12(foot: Offset, tall: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val sw = max(1.7f * density, tall * 0.065f)
    val hip = foot - Offset(0f, tall * 0.45f)
    val shoulder = hip - Offset(0f, tall * 0.31f)
    val head = shoulder - Offset(0f, tall * 0.15f)
    drawLine(color, hip, foot + Offset(-tall * 0.05f, 0f), sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, foot + Offset(tall * 0.05f, 0f), sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, shoulder, sw, StrokeCap.Round, alpha = alpha)
    drawCircle(color, tall * 0.1f, head, alpha = alpha)
    val chest = shoulder + Offset(0f, tall * 0.12f)
    drawLine(color, shoulder, chest + Offset(-tall * 0.09f, tall * 0.02f), sw * 0.9f, StrokeCap.Round, alpha = alpha)
    drawLine(color, chest + Offset(-tall * 0.09f, tall * 0.02f), chest + Offset(tall * 0.05f, -tall * 0.01f), sw * 0.9f, StrokeCap.Round, alpha = alpha)
}

/**
 * A figure bowing, facing left: [deep] 0 stands, 0.5 is the bow (rukuʿ), 1 is prostration (sujud), feet at [foot].
 */
internal fun DrawScope.bow12(foot: Offset, tall: Float, color: Color, alpha: Float, deep: Float) {
    if (alpha <= 0.01f) return
    val sw = max(1.7f * density, tall * 0.065f)
    val d = deep.coerceIn(0f, 1f)
    val bowF = (d / 0.5f).coerceIn(0f, 1f)
    val downF = ((d - 0.5f) / 0.5f).coerceIn(0f, 1f)
    // the hip comes down to the heels in prostration
    val hip = foot + Offset(tall * 0.12f * downF, -tall * (0.45f - 0.33f * downF))
    val ang = (PI.toFloat() / 2f) * bowF + (PI.toFloat() / 4f) * downF          // body angle from upright, leaning left
    val dir = Offset(-sin(ang), -cos(ang))
    val shoulder = hip + dir * (tall * 0.31f)
    val head = shoulder + dir * (tall * 0.14f)
    val knee = if (downF > 0f) Offset(foot.x - tall * 0.12f * downF, foot.y - tall * 0.02f) else foot
    drawLine(color, hip, if (downF > 0f) knee else foot + Offset(-tall * 0.04f, 0f), sw, StrokeCap.Round, alpha = alpha)
    if (downF > 0f) drawLine(color, knee, foot + Offset(tall * 0.1f, 0f), sw, StrokeCap.Round, alpha = alpha)
    else drawLine(color, hip, foot + Offset(tall * 0.04f, 0f), sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, shoulder, sw, StrokeCap.Round, alpha = alpha)
    drawCircle(color, tall * 0.1f, head, alpha = alpha)
    val hand = if (downF > 0f) Offset(head.x + tall * 0.06f, foot.y) else hip + Offset(-tall * 0.12f * bowF, tall * 0.12f * bowF)
    drawLine(color, shoulder, hand, sw * 0.9f, StrokeCap.Round, alpha = alpha)
}

/** A woman as a modest silhouette: a long robe and a covering over head and shoulders. */
internal fun DrawScope.woman12(foot: Offset, tall: Float, color: Color, alpha: Float, walk: Float = 0f) {
    if (alpha <= 0.01f) return
    val sway = sin(walk) * tall * 0.02f
    val shoulder = foot.y - tall * 0.74f
    drawPath(Path().apply {
        moveTo(foot.x - tall * 0.1f, shoulder)
        lineTo(foot.x + tall * 0.1f, shoulder)
        lineTo(foot.x + tall * 0.17f + sway, foot.y)
        lineTo(foot.x - tall * 0.17f + sway, foot.y)
        close()
    }, color, alpha = alpha)
    val head = Offset(foot.x, shoulder - tall * 0.11f)
    drawPath(Path().apply {
        moveTo(head.x - tall * 0.13f, shoulder + tall * 0.08f)
        quadraticTo(head.x - tall * 0.13f, head.y - tall * 0.14f, head.x, head.y - tall * 0.13f)
        quadraticTo(head.x + tall * 0.13f, head.y - tall * 0.14f, head.x + tall * 0.13f, shoulder + tall * 0.08f)
        close()
    }, color, alpha = alpha)
}

/** A woman sitting (a rounded robe on the ground), facing [facing]. */
internal fun DrawScope.womanSit12(seat: Offset, tall: Float, color: Color, alpha: Float, facing: Float = 1f) {
    if (alpha <= 0.01f) return
    drawPath(Path().apply {
        moveTo(seat.x - tall * 0.22f, seat.y)
        quadraticTo(seat.x - tall * 0.2f, seat.y - tall * 0.42f, seat.x - facing * tall * 0.02f, seat.y - tall * 0.46f)
        quadraticTo(seat.x + tall * 0.2f, seat.y - tall * 0.3f, seat.x + tall * 0.26f, seat.y)
        close()
    }, color, alpha = alpha)
    drawCircle(color, tall * 0.11f, Offset(seat.x - facing * tall * 0.02f, seat.y - tall * 0.52f), alpha = alpha)
}

/** A date palm, its foot at [foot], [s] high; the fronds sway with [t]. */
internal fun DrawScope.palm12(foot: Offset, s: Float, trunk: Color, leaf: Color, alpha: Float, t: Float = 0f) {
    if (alpha <= 0.01f) return
    val top = foot + Offset(s * 0.06f, -s)
    drawPath(Path().apply {
        moveTo(foot.x - s * 0.035f, foot.y)
        quadraticTo(foot.x + s * 0.06f, foot.y - s * 0.5f, top.x - s * 0.018f, top.y)
        lineTo(top.x + s * 0.018f, top.y)
        quadraticTo(foot.x + s * 0.1f, foot.y - s * 0.5f, foot.x + s * 0.035f, foot.y)
        close()
    }, trunk, alpha = alpha)
    val sway = 0.06f * sin(t * 0.9f)
    for (k in 0 until 7) {
        val a = -PI.toFloat() * (0.06f + 0.88f * k / 6f) + sway
        val len = s * (0.36f + 0.06f * (k % 2))
        val tip = top + Offset(cos(a) * len, sin(a) * len * 0.6f + len * 0.25f)
        val mid = top + Offset(cos(a) * len * 0.5f, sin(a) * len * 0.5f - len * 0.08f)
        drawPath(Path().apply { moveTo(top.x, top.y); quadraticTo(mid.x, mid.y, tip.x, tip.y) }, leaf, alpha = alpha, style = Stroke(s * 0.035f, cap = StrokeCap.Round))
    }
    for (k in 0 until 3) drawCircle(Color(0xFF9A5A2A), s * 0.025f, top + Offset(s * (0.02f * k - 0.02f), s * 0.05f), alpha = alpha)
}

/** A flat-roofed house of the old towns, its foot at [foot], [s] wide; [lit] lights its window and door. */
internal fun DrawScope.house12(foot: Offset, s: Float, wall: Color, trim: Color, alpha: Float, lit: Float = 0f) {
    if (alpha <= 0.01f) return
    val h = s * 0.62f
    drawRect(wall, Offset(foot.x - s / 2f, foot.y - h), Size(s, h), alpha = alpha)
    drawRect(trim, Offset(foot.x - s * 0.53f, foot.y - h - s * 0.05f), Size(s * 1.06f, s * 0.06f), alpha = alpha)
    val glow = Color(0xFFFFD98A)
    val door = Offset(foot.x - s * 0.12f, foot.y)
    drawPath(archPath26(door.x, door.y - h * 0.62f, door.y, s * 0.1f), lerpColor(trim, glow, lit), alpha = alpha)
    drawRect(lerpColor(trim, glow, lit), Offset(foot.x + s * 0.14f, foot.y - h * 0.72f), Size(s * 0.16f, s * 0.14f), alpha = alpha)
    if (lit > 0.02f) softGlow(Offset(foot.x + s * 0.22f, foot.y - h * 0.65f), s * 0.3f, glow, 0.35f * lit * alpha)
}

/** An arched door seen from the front, its sill at [sill]; [open] 0..1 swings it and lets light out. */
internal fun DrawScope.door12(sill: Offset, wd: Float, ht: Float, open: Float, frame: Color, leaf: Color, alpha: Float, light: Color = Color(0xFFFFE2A0)) {
    if (alpha <= 0.01f) return
    val o = open.coerceIn(0f, 1f)
    val hw = wd / 2f
    drawPath(archPath26(sill.x, sill.y - ht, sill.y, hw * 1.18f), frame, alpha = alpha)
    drawPath(archPath26(sill.x, sill.y - ht * 0.94f, sill.y, hw), lerpColor(Color(0xFF1A1410), light, o), alpha = alpha)
    if (o > 0.02f) softGlow(Offset(sill.x, sill.y - ht * 0.4f), wd * 1.2f, light, 0.45f * o * alpha)
    // the leaf turns on its hinge (left side): it narrows as it opens
    val lw = wd * (1f - 0.85f * o)
    drawRect(leaf, Offset(sill.x - hw, sill.y - ht * 0.78f), Size(lw, ht * 0.78f), alpha = alpha)
    drawCircle(Color(0xFFE2C070), wd * 0.04f, Offset(sill.x - hw + lw * 0.85f, sill.y - ht * 0.4f), alpha = alpha * (1f - o))
}

/** A mosque: a dome on a cube and a minaret, its foot at [foot], [s] wide. */
internal fun DrawScope.mosque12(foot: Offset, s: Float, body: Color, dome: Color, alpha: Float, lit: Float = 0f) {
    if (alpha <= 0.01f) return
    val h = s * 0.42f
    drawRect(body, Offset(foot.x - s * 0.36f, foot.y - h), Size(s * 0.72f, h), alpha = alpha)
    drawArc(dome, 180f, 180f, true, Offset(foot.x - s * 0.24f, foot.y - h - s * 0.24f), Size(s * 0.48f, s * 0.48f), alpha = alpha)
    drawLine(dome, Offset(foot.x, foot.y - h - s * 0.24f), Offset(foot.x, foot.y - h - s * 0.32f), s * 0.015f, StrokeCap.Round, alpha = alpha)
    val mx = foot.x + s * 0.44f
    drawRect(body, Offset(mx - s * 0.04f, foot.y - s * 0.95f), Size(s * 0.08f, s * 0.95f), alpha = alpha)
    drawRect(dome, Offset(mx - s * 0.06f, foot.y - s * 0.78f), Size(s * 0.12f, s * 0.03f), alpha = alpha)
    drawPath(Path().apply { moveTo(mx - s * 0.05f, foot.y - s * 0.95f); lineTo(mx, foot.y - s * 1.07f); lineTo(mx + s * 0.05f, foot.y - s * 0.95f); close() }, dome, alpha = alpha)
    val glow = Color(0xFFFFD98A)
    drawPath(archPath26(foot.x, foot.y - h * 0.6f, foot.y, s * 0.07f), lerpColor(Color(0xFF2A2018), glow, lit), alpha = alpha)
    if (lit > 0.02f) softGlow(Offset(foot.x, foot.y - h * 0.3f), s * 0.25f, glow, 0.4f * lit * alpha)
}

/** A crescent moon: a disc with a disc of the sky's colour cut from it. */
internal fun DrawScope.crescent12(c: Offset, r: Float, color: Color, sky: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 2.6f, color, 0.3f * alpha)
    drawCircle(color, r, c, alpha = alpha)
    drawCircle(sky, r * 0.86f, c + Offset(r * 0.42f, -r * 0.18f), alpha = alpha)
}

/** A full moon with a soft halo. */
internal fun DrawScope.moon12(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 3f, Color(0xFFEFF2FF), 0.35f * alpha)
    drawCircle(Color(0xFFF4F1E4), r, c, alpha = alpha)
    drawCircle(Color(0xFFDCD6C2), r * 0.18f, c + Offset(-r * 0.3f, -r * 0.2f), alpha = alpha * 0.6f)
    drawCircle(Color(0xFFDCD6C2), r * 0.12f, c + Offset(r * 0.25f, r * 0.3f), alpha = alpha * 0.5f)
}

/** The sun of a daylight scene. */
internal fun DrawScope.sun12(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 4f, Color(0xFFFFE7A8), 0.5f * alpha)
    drawCircle(Color(0xFFFFF4D2), r, c, alpha = alpha)
}

/** A daylight sky from [top] to [low] down to [horizon], with the ground below in [ground]. */
internal fun DrawScope.daySky12(top: Color, low: Color, horizon: Float, ground: Color) {
    drawRect(Brush.verticalGradient(listOf(top, low), 0f, horizon))
    drawRect(ground, Offset(0f, horizon), Size(size.width, size.height - horizon))
}

/** Two hands raised in supplication, palms up, around [c]; [s] is their height. */
internal fun DrawScope.hands12(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    for (side in listOf(-1f, 1f)) {
        val base = c + Offset(side * s * 0.2f, s * 0.45f)
        drawPath(Path().apply {
            moveTo(base.x - s * 0.08f, base.y)
            quadraticTo(base.x - side * s * 0.02f - s * 0.1f, base.y - s * 0.5f, base.x + side * s * 0.02f, base.y - s * 0.78f)
            quadraticTo(base.x + side * s * 0.16f, base.y - s * 0.8f, base.x + side * s * 0.14f, base.y - s * 0.55f)
            quadraticTo(base.x + side * s * 0.12f, base.y - s * 0.2f, base.x + s * 0.08f, base.y)
            close()
        }, color, alpha = alpha)
    }
}

/** A ring of prayer beads with a short tassel; [turn] slides the beads round. */
internal fun DrawScope.beads12(c: Offset, r: Float, color: Color, alpha: Float, turn: Float = 0f) {
    if (alpha <= 0.01f) return
    val n = 33
    for (k in 0 until n) {
        val a = PI.toFloat() * 0.5f + 2f * PI.toFloat() * (k + turn) / (n + 3)
        drawCircle(color, r * 0.075f, c + Offset(cos(a) * r, sin(a) * r * 0.85f), alpha = alpha)
    }
    val bottom = c + Offset(0f, r * 0.85f)
    drawCircle(color, r * 0.12f, bottom + Offset(0f, r * 0.1f), alpha = alpha)
    drawLine(color, bottom + Offset(0f, r * 0.2f), bottom + Offset(0f, r * 0.6f), r * 0.05f, StrokeCap.Round, alpha = alpha)
}

/** A cup or bowl seen from the side, its foot at [foot], [s] wide. */
internal fun DrawScope.cup12(foot: Offset, s: Float, color: Color, alpha: Float, fill: Color? = null) {
    if (alpha <= 0.01f) return
    drawPath(Path().apply {
        moveTo(foot.x - s * 0.5f, foot.y - s * 0.55f)
        quadraticTo(foot.x - s * 0.45f, foot.y, foot.x, foot.y)
        quadraticTo(foot.x + s * 0.45f, foot.y, foot.x + s * 0.5f, foot.y - s * 0.55f)
        close()
    }, color, alpha = alpha)
    if (fill != null) drawOval(fill, Offset(foot.x - s * 0.46f, foot.y - s * 0.6f), Size(s * 0.92f, s * 0.12f), alpha = alpha)
}

/** A balance: a beam that tilts by [tilt] (-1..1, positive lowers the right pan), its stand at [foot]. */
internal fun DrawScope.balance12(foot: Offset, s: Float, tilt: Float, color: Color, alpha: Float, left: Color = color, right: Color = color) {
    if (alpha <= 0.01f) return
    val top = foot + Offset(0f, -s)
    drawLine(color, foot, top, s * 0.04f, StrokeCap.Round, alpha = alpha)
    drawLine(color, foot + Offset(-s * 0.18f, 0f), foot + Offset(s * 0.18f, 0f), s * 0.05f, StrokeCap.Round, alpha = alpha)
    val a = 0.32f * tilt
    val arm = Offset(cos(a), sin(a)) * (s * 0.55f)
    val l = top - arm
    val r = top + arm
    drawLine(color, l, r, s * 0.035f, StrokeCap.Round, alpha = alpha)
    for ((end, pan) in listOf(l to left, r to right)) {
        val p = end + Offset(0f, s * 0.38f)
        drawLine(color, end, p + Offset(-s * 0.14f, 0f), s * 0.012f, alpha = alpha)
        drawLine(color, end, p + Offset(s * 0.14f, 0f), s * 0.012f, alpha = alpha)
        drawArc(pan, 0f, 180f, true, Offset(p.x - s * 0.16f, p.y - s * 0.07f), Size(s * 0.32f, s * 0.14f), alpha = alpha)
    }
    drawCircle(color, s * 0.04f, top, alpha = alpha)
}

/** The pans of [balance12]: where the left and right pans hang for a [tilt]. */
internal fun balancePans12(foot: Offset, s: Float, tilt: Float): Pair<Offset, Offset> {
    val top = foot + Offset(0f, -s)
    val a = 0.32f * tilt
    val arm = Offset(cos(a), sin(a)) * (s * 0.55f)
    return Pair(top - arm + Offset(0f, s * 0.36f), top + arm + Offset(0f, s * 0.36f))
}

/** A dome of light over [c] (a guard that remembrance keeps): [r] its radius, [rise] 0..1 raises it. */
internal fun DrawScope.guardDome12(c: Offset, r: Float, rise: Float, t: Float, alpha: Float, color: Color = Color(0xFFBFE6FF)) {
    if (alpha <= 0.01f || rise <= 0.01f) return
    val rr = r * easeOut(rise)
    drawArc(Brush.radialGradient(listOf(Color.Transparent, color.copy(alpha = 0.22f * alpha)), c, rr), 180f, 180f, true, Offset(c.x - rr, c.y - rr), Size(rr * 2f, rr * 2f))
    drawArc(color, 180f, 180f, false, Offset(c.x - rr, c.y - rr), Size(rr * 2f, rr * 2f), alpha = 0.8f * alpha, style = Stroke(2.2f * density))
    for (k in 0 until 5) {
        val a = PI.toFloat() * (1f + (k + 0.5f) / 5f) + 0.05f * sin(t + k)
        drawCircle(Color.White, 1.6f * density, c + Offset(cos(a) * rr, sin(a) * rr), alpha = 0.7f * alpha)
    }
}

/** A heart outline (the inner life), [s] wide. */
internal fun DrawScope.heart12(c: Offset, s: Float, color: Color, alpha: Float, fill: Boolean = false) {
    if (alpha <= 0.01f) return
    val p = Path().apply {
        moveTo(c.x, c.y + s * 0.42f)
        cubicTo(c.x - s * 0.55f, c.y + s * 0.05f, c.x - s * 0.5f, c.y - s * 0.42f, c.x - s * 0.22f, c.y - s * 0.4f)
        cubicTo(c.x - s * 0.08f, c.y - s * 0.4f, c.x, c.y - s * 0.3f, c.x, c.y - s * 0.2f)
        cubicTo(c.x, c.y - s * 0.3f, c.x + s * 0.08f, c.y - s * 0.4f, c.x + s * 0.22f, c.y - s * 0.4f)
        cubicTo(c.x + s * 0.5f, c.y - s * 0.42f, c.x + s * 0.55f, c.y + s * 0.05f, c.x, c.y + s * 0.42f)
        close()
    }
    if (fill) drawPath(p, color, alpha = alpha) else drawPath(p, color, alpha = alpha, style = Stroke(max(1.6f * density, s * 0.06f)))
}

/** A coin (a dinar or dirham), seen face on. */
internal fun DrawScope.coin12(c: Offset, r: Float, alpha: Float, color: Color = Color(0xFFE8C25A)) {
    if (alpha <= 0.01f) return
    drawCircle(color, r, c, alpha = alpha)
    drawCircle(Color(0xFF9A7224), r * 0.78f, c, alpha = alpha * 0.8f, style = Stroke(r * 0.1f))
    drawCircle(Color.White, r * 0.22f, c + Offset(-r * 0.35f, -r * 0.35f), alpha = alpha * 0.35f)
}

/** A child: a small faceless figure. */
internal fun DrawScope.child12(foot: Offset, tall: Float, color: Color, alpha: Float, walk: Float = 0f, arms: Float = 0f) {
    figure(foot, tall * 0.62f, color, alpha, walk = walk, arms = arms)
}

/** A short line of rain or light falling over [x0]..[x1] from [top] to [bottom], moving with [t]. */
internal fun DrawScope.rain12(x0: Float, x1: Float, top: Float, bottom: Float, t: Float, color: Color, alpha: Float, n: Int = 28) {
    if (alpha <= 0.01f) return
    for (k in 0 until n) {
        val r = fract(sin(k * 12.9898f) * 43758.547f)
        val x = x0 + (x1 - x0) * r
        val y = top + (bottom - top) * fract(r * 3.1f + t * (0.9f + 0.4f * r))
        drawLine(color, Offset(x, y), Offset(x - 1.5f * density, y + 9f * density), 1.2f * density, StrokeCap.Round, alpha = alpha * 0.8f)
    }
}
