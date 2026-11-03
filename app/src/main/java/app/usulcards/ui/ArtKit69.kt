package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Kit 69 (2026-11-03): the objects of the fiqh cards (v1.3): water and its vessels, the white cloths of ihram, the circling
 * round the House, the two hills, the pillars of the stoning, tents, rugs, calendars, sacks of grain and the like. People are
 * the faceless figures of ArtWave4; no one named is ever drawn.
 */

internal object Fq {
    val Water = Color(0xFF7CC4E8)
    val WaterDeep = Color(0xFF3A86B8)
    val White = Color(0xFFF4F1E8)
    val Sand = Color(0xFFD9B98A)
    val SandDark = Color(0xFF9C7A4E)
    val Stone = Color(0xFFB8AFA0)
    val Brass = Color(0xFFD29C45)
    val Gold = Color(0xFFF2C45A)
    val Rose = Color(0xFFB46A78)
    val Green = Color(0xFF3E8A5E)
    val Ink = Color(0xFF2A2430)
    val Night = Color(0xFF101830)
    val Dusk = Color(0xFF5A5A8A)
    val Back = Color(0xE6181420)
}

/** How present a beat is: it rises over the first [rise] of its own beat and fades out as [next] comes in. */
internal fun fqIn(a: ArtCtx, kind: String, next: String? = null, rise: Float = 0.25f): Float =
    easeOut(seg(a.b(kind), 0f, rise)) * (if (next == null) 1f else 1f - seg(a.b(next), 0f, 0.4f))

/** A water drop, its round end at [c]. */
internal fun DrawScope.fqDrop(c: Offset, r: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val p = Path().apply {
        moveTo(c.x, c.y - r * 2.1f)
        cubicTo(c.x + r * 0.4f, c.y - r * 1.2f, c.x + r, c.y - r * 0.6f, c.x + r, c.y)
        cubicTo(c.x + r, c.y + r * 0.6f, c.x + r * 0.55f, c.y + r, c.x, c.y + r)
        cubicTo(c.x - r * 0.55f, c.y + r, c.x - r, c.y + r * 0.6f, c.x - r, c.y)
        cubicTo(c.x - r, c.y - r * 0.6f, c.x - r * 0.4f, c.y - r * 1.2f, c.x, c.y - r * 2.1f)
        close()
    }
    drawPath(p, color, alpha = alpha)
    drawCircle(Color.White, r * 0.22f, c + Offset(-r * 0.35f, -r * 0.2f), alpha = alpha * 0.6f)
}

/** A ewer, its base at [foot], spout to the left; [pour] 0..1 tips it and lets water run. */
internal fun DrawScope.fqEwer(foot: Offset, s: Float, color: Color, alpha: Float, pour: Float = 0f, t: Float = 0f) {
    if (alpha <= 0.01f) return
    rotate(-35f * pour, foot) {
        drawOval(color, Offset(foot.x - s * 0.3f, foot.y - s * 0.62f), Size(s * 0.6f, s * 0.6f), alpha = alpha)
        drawRect(color, Offset(foot.x - s * 0.1f, foot.y - s * 0.85f), Size(s * 0.2f, s * 0.28f), alpha = alpha)
        drawRect(lerpColor(color, Color.White, 0.25f), Offset(foot.x - s * 0.15f, foot.y - s * 0.9f), Size(s * 0.3f, s * 0.06f), alpha = alpha)
        drawLine(color, Offset(foot.x - s * 0.24f, foot.y - s * 0.4f), Offset(foot.x - s * 0.5f, foot.y - s * 0.72f), s * 0.07f, StrokeCap.Round, alpha = alpha)
        drawArc(color, -90f, 180f, false, Offset(foot.x + s * 0.14f, foot.y - s * 0.75f), Size(s * 0.3f, s * 0.36f), alpha = alpha, style = Stroke(s * 0.05f))
        drawRect(lerpColor(color, Color.Black, 0.2f), Offset(foot.x - s * 0.18f, foot.y - s * 0.05f), Size(s * 0.36f, s * 0.05f), alpha = alpha)
    }
    if (pour > 0.3f) {
        val ang = (-35f * pour) * PI.toFloat() / 180f
        val sx = foot.x + (-s * 0.5f) * cos(ang) - (-s * 0.72f) * sin(ang)
        val sy = foot.y + (-s * 0.5f) * sin(ang) + (-s * 0.72f) * cos(ang)
        val k = seg(pour, 0.3f, 0.6f)
        for (i in 0 until 6) {
            val u = fract(t * 1.6f + i / 6f)
            fqDrop(Offset(sx - s * 0.04f, sy + s * 0.9f * u), s * 0.035f, Fq.Water, alpha * k * (1f - u * 0.6f))
        }
    }
}

/** A cloth hanging from its top edge around [c]; [open] 0..1 unrolls it downwards. */
internal fun DrawScope.fqCloth(c: Offset, wd: Float, ht: Float, color: Color, alpha: Float, t: Float, open: Float = 1f) {
    if (alpha <= 0.01f || open <= 0f) return
    val top = c.y - ht * 0.5f
    val h = ht * easeOut(open)
    val p = Path().apply {
        moveTo(c.x - wd * 0.5f, top)
        lineTo(c.x + wd * 0.5f, top)
        lineTo(c.x + wd * 0.5f, top + h)
        for (i in 1..8) {
            val x = c.x + wd * 0.5f - wd * i / 8f
            lineTo(x, top + h + sin(t * 4f + i) * ht * 0.025f)
        }
        close()
    }
    drawPath(p, color, alpha = alpha)
    for (i in 1..3) drawLine(lerpColor(color, Color.Black, 0.12f), Offset(c.x - wd * 0.5f + wd * i / 4f, top), Offset(c.x - wd * 0.5f + wd * i / 4f, top + h * 0.95f), 1.2f * density, alpha = alpha * 0.5f)
}

/** A pilgrim in the two white cloths: a faceless figure in white with the upper cloth across one shoulder. */
internal fun DrawScope.fqPilgrim(foot: Offset, tall: Float, alpha: Float, walk: Float = 0f, arms: Float = 0f) {
    if (alpha <= 0.01f) return
    figure(foot, tall, Fq.White, alpha, walk = walk, arms = arms)
    val hip = foot - Offset(0f, tall * 0.45f)
    drawRect(Fq.White, Offset(hip.x - tall * 0.13f, hip.y - tall * 0.04f), Size(tall * 0.26f, tall * 0.26f), alpha = alpha)
    drawLine(Fq.White, hip + Offset(-tall * 0.1f, -tall * 0.02f), hip + Offset(tall * 0.07f, -tall * 0.3f), tall * 0.1f, StrokeCap.Round, alpha = alpha)
}

/** People circling round [c] (an ellipse rx by ry), counter-clockwise seen from above, the House on their left. */
internal fun DrawScope.fqCircling(c: Offset, rx: Float, ry: Float, t: Float, color: Color, alpha: Float, n: Int = 14, speed: Float = 0.12f) {
    if (alpha <= 0.01f) return
    drawOval(color, Offset(c.x - rx, c.y - ry), Size(rx * 2f, ry * 2f), alpha = alpha * 0.25f, style = Stroke(1.5f * density))
    for (i in 0 until n) {
        val a = 2f * PI.toFloat() * (i / n.toFloat() + t * speed)
        val p = Offset(c.x + rx * cos(a), c.y - ry * sin(a))
        val front = sin(a) < 0f
        drawCircle(color, rx * 0.045f, p + Offset(0f, -rx * 0.05f), alpha = alpha * (if (front) 1f else 0.55f))
        drawLine(color, p, p + Offset(0f, -rx * 0.05f), rx * 0.05f, StrokeCap.Round, alpha = alpha * (if (front) 1f else 0.55f))
    }
}

/** A low rounded hill (al-Ṣafā, al-Marwa), its foot centred on [foot]. */
internal fun DrawScope.fqMound(foot: Offset, wd: Float, ht: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawArc(color, 180f, 180f, true, Offset(foot.x - wd * 0.5f, foot.y - ht), Size(wd, ht * 2f), alpha = alpha)
}

/** A pillar (of the stoning, or of a building), its base at [foot]. */
internal fun DrawScope.fqPillar(foot: Offset, wd: Float, ht: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(color, Offset(foot.x - wd * 0.5f, foot.y - ht), Size(wd, ht), alpha = alpha)
    drawRect(lerpColor(color, Color.White, 0.2f), Offset(foot.x - wd * 0.7f, foot.y - ht - wd * 0.25f), Size(wd * 1.4f, wd * 0.25f), alpha = alpha)
    drawRect(lerpColor(color, Color.Black, 0.2f), Offset(foot.x - wd * 0.7f, foot.y - wd * 0.2f), Size(wd * 1.4f, wd * 0.2f), alpha = alpha)
}

/** A small pebble flying from [from] to [to] along an arc, [p] 0..1. */
internal fun DrawScope.fqPebble(from: Offset, to: Offset, p: Float, r: Float, alpha: Float) {
    if (alpha <= 0.01f || p <= 0f || p >= 1f) return
    val x = mix(from.x, to.x, p)
    val y = mix(from.y, to.y, p) - sin(PI.toFloat() * p) * (to - from).getDistance() * 0.3f
    drawCircle(Fq.Stone, r, Offset(x, y), alpha = alpha)
}

/** A white tent (Minā), its base centred on [foot]. */
internal fun DrawScope.fqTent(foot: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawPath(Path().apply {
        moveTo(foot.x - s * 0.5f, foot.y); lineTo(foot.x - s * 0.32f, foot.y - s * 0.45f); lineTo(foot.x, foot.y - s * 0.62f)
        lineTo(foot.x + s * 0.32f, foot.y - s * 0.45f); lineTo(foot.x + s * 0.5f, foot.y); close()
    }, color, alpha = alpha)
    drawPath(Path().apply { moveTo(foot.x - s * 0.08f, foot.y); lineTo(foot.x, foot.y - s * 0.3f); lineTo(foot.x + s * 0.08f, foot.y); close() }, lerpColor(color, Color.Black, 0.35f), alpha = alpha)
}

/** A prayer rug lying flat, seen from above, around [c], its arch at the top (towards the qibla). */
internal fun DrawScope.fqRug(c: Offset, wd: Float, ht: Float, color: Color, trim: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, Offset(c.x - wd * 0.5f, c.y - ht * 0.5f), Size(wd, ht), CornerRadius(wd * 0.05f), alpha = alpha)
    drawRoundRect(trim, Offset(c.x - wd * 0.42f, c.y - ht * 0.44f), Size(wd * 0.84f, ht * 0.88f), CornerRadius(wd * 0.04f), alpha = alpha, style = Stroke(wd * 0.035f))
    drawPath(archPath26(c.x, c.y - ht * 0.38f, c.y + ht * 0.05f, wd * 0.26f), trim, alpha = alpha * 0.8f, style = Stroke(wd * 0.03f))
}

/** A grid of days, [cols] wide, its top-left at [tl]; [fill] gives each day's colour (null: an empty day). */
internal fun DrawScope.fqCalendar(tl: Offset, cols: Int, count: Int, cell: Float, alpha: Float, rim: Color, fill: (Int) -> Color?) {
    if (alpha <= 0.01f) return
    for (i in 0 until count) {
        val p = tl + Offset((cols - 1 - i % cols) * cell * 1.15f, (i / cols) * cell * 1.15f)     // right to left, as Arabic reads
        val f = fill(i)
        if (f != null) drawRoundRect(f, p, Size(cell, cell), CornerRadius(cell * 0.2f), alpha = alpha)
        drawRoundRect(rim, p, Size(cell, cell), CornerRadius(cell * 0.2f), alpha = alpha * 0.7f, style = Stroke(1.3f * density))
    }
}

/** A sack of grain tied at the neck, its base at [foot]. */
internal fun DrawScope.fqSack(foot: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawPath(Path().apply {
        moveTo(foot.x - s * 0.18f, foot.y - s * 0.78f)
        cubicTo(foot.x - s * 0.5f, foot.y - s * 0.5f, foot.x - s * 0.5f, foot.y, foot.x - s * 0.3f, foot.y)
        lineTo(foot.x + s * 0.3f, foot.y)
        cubicTo(foot.x + s * 0.5f, foot.y, foot.x + s * 0.5f, foot.y - s * 0.5f, foot.x + s * 0.18f, foot.y - s * 0.78f)
        close()
    }, color, alpha = alpha)
    drawLine(lerpColor(color, Color.Black, 0.35f), Offset(foot.x - s * 0.17f, foot.y - s * 0.76f), Offset(foot.x + s * 0.17f, foot.y - s * 0.76f), s * 0.05f, StrokeCap.Round, alpha = alpha)
    drawPath(Path().apply { moveTo(foot.x - s * 0.15f, foot.y - s * 0.78f); lineTo(foot.x - s * 0.22f, foot.y - s * 0.95f); lineTo(foot.x + s * 0.22f, foot.y - s * 0.95f); lineTo(foot.x + s * 0.15f, foot.y - s * 0.78f); close() }, lerpColor(color, Color.White, 0.12f), alpha = alpha)
}

/** A pair of scissors around [c]; [open] 0..1 opens the blades. */
internal fun DrawScope.fqScissors(c: Offset, s: Float, color: Color, alpha: Float, open: Float = 0.5f) {
    if (alpha <= 0.01f) return
    val a = 0.12f + 0.3f * open
    for (sg in listOf(-1f, 1f)) {
        val tip = c + Offset(-s * cos(a * sg), -s * sin(a * sg) * 1f)
        val ring = c + Offset(s * 0.45f * cos(a * sg), s * 0.45f * sin(a * sg))
        drawLine(color, ring, tip, s * 0.08f, StrokeCap.Round, alpha = alpha)
        drawCircle(color, s * 0.13f, ring + Offset(s * 0.1f * cos(a * sg), s * 0.1f * sin(a * sg)), alpha = alpha, style = Stroke(s * 0.05f))
    }
    drawCircle(Color.White, s * 0.03f, c, alpha = alpha)
}

/** A small glass phial of scent, its base at [foot], with a curl of scent above it. */
internal fun DrawScope.fqPhial(foot: Offset, s: Float, color: Color, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    drawOval(color, Offset(foot.x - s * 0.22f, foot.y - s * 0.45f), Size(s * 0.44f, s * 0.45f), alpha = alpha)
    drawRect(color, Offset(foot.x - s * 0.06f, foot.y - s * 0.62f), Size(s * 0.12f, s * 0.2f), alpha = alpha)
    drawRect(Fq.Gold, Offset(foot.x - s * 0.08f, foot.y - s * 0.7f), Size(s * 0.16f, s * 0.08f), alpha = alpha)
    for (k in 0 until 3) {
        val u = fract(t * 0.4f + k / 3f)
        drawArc(Fq.Gold, 200f, 140f, false, Offset(foot.x - s * 0.15f + sin(u * 6f) * s * 0.08f, foot.y - s * (0.8f + 0.6f * u)), Size(s * 0.3f, s * 0.2f), alpha = alpha * (1f - u) * 0.8f, style = Stroke(1.5f * density))
    }
}

/** A road of [wd] width from [from] to [to]. */
internal fun DrawScope.fqRoad(from: Offset, to: Offset, wd: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawLine(color, from, to, wd, StrokeCap.Round, alpha = alpha)
    drawLine(lerpColor(color, Color.White, 0.4f), from, to, wd * 0.08f, alpha = alpha * 0.5f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(wd * 0.6f, wd * 0.6f)))
}

/** A stack of [n] coins, its base at [foot]. */
internal fun DrawScope.fqCoins(foot: Offset, r: Float, n: Int, alpha: Float, color: Color = Fq.Gold) {
    if (alpha <= 0.01f) return
    for (i in 0 until n) {
        val y = foot.y - i * r * 0.32f
        drawOval(lerpColor(color, Color.Black, 0.25f), Offset(foot.x - r, y - r * 0.32f), Size(r * 2f, r * 0.5f), alpha = alpha)
        drawOval(color, Offset(foot.x - r, y - r * 0.42f), Size(r * 2f, r * 0.5f), alpha = alpha)
    }
}

/** A bar of [n] cells filling from the right, [p] 0..1 how many are lit; a measure of a nisab or a share. */
internal fun DrawScope.fqMeter(c: Offset, wd: Float, ht: Float, n: Int, p: Float, on: Color, off: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val cw = wd / n
    for (i in 0 until n) {
        val x = c.x + wd * 0.5f - cw * (i + 1)
        val lit = seg(p * n, i.toFloat(), i + 1f)
        drawRoundRect(off, Offset(x + cw * 0.08f, c.y - ht * 0.5f), Size(cw * 0.84f, ht), CornerRadius(ht * 0.2f), alpha = alpha * 0.5f)
        if (lit > 0f) drawRoundRect(on, Offset(x + cw * 0.08f, c.y - ht * 0.5f), Size(cw * 0.84f, ht), CornerRadius(ht * 0.2f), alpha = alpha * lit)
    }
}

/** A cow or bull standing, facing left, its feet at [foot]. */
internal fun DrawScope.fqCow(foot: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val body = foot + Offset(0f, -s * 0.55f)
    drawRoundRect(color, Offset(body.x - s * 0.45f, body.y - s * 0.2f), Size(s * 0.9f, s * 0.4f), CornerRadius(s * 0.15f), alpha = alpha)
    drawOval(color, Offset(body.x - s * 0.68f, body.y - s * 0.3f), Size(s * 0.3f, s * 0.26f), alpha = alpha)
    drawArc(lerpColor(color, Color.White, 0.5f), 180f, 120f, false, Offset(body.x - s * 0.66f, body.y - s * 0.42f), Size(s * 0.26f, s * 0.2f), alpha = alpha, style = Stroke(s * 0.035f, cap = StrokeCap.Round))
    for (k in 0 until 4) drawLine(color, Offset(body.x - s * 0.35f + k * s * 0.22f, body.y + s * 0.15f), Offset(body.x - s * 0.35f + k * s * 0.22f, foot.y), s * 0.07f, StrokeCap.Round, alpha = alpha)
    drawLine(color, body + Offset(s * 0.44f, -s * 0.1f), body + Offset(s * 0.52f, s * 0.2f), s * 0.03f, StrokeCap.Round, alpha = alpha)
}

/** A bird in flight around [c], wings beating with [t]. */
internal fun DrawScope.fqBird(c: Offset, s: Float, color: Color, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    val f = sin(t * 9f) * s * 0.3f
    drawPath(Path().apply {
        moveTo(c.x - s, c.y - f); quadraticBezierTo(c.x - s * 0.4f, c.y - s * 0.3f, c.x, c.y)
        quadraticBezierTo(c.x + s * 0.4f, c.y - s * 0.3f, c.x + s, c.y - f)
    }, color, alpha = alpha, style = Stroke(s * 0.14f, cap = StrokeCap.Round))
}

/** A sewn shirt, front view, around [c]. */
internal fun DrawScope.fqShirt(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawPath(Path().apply {
        moveTo(c.x - s * 0.18f, c.y - s * 0.5f); lineTo(c.x - s * 0.5f, c.y - s * 0.35f); lineTo(c.x - s * 0.42f, c.y - s * 0.12f)
        lineTo(c.x - s * 0.3f, c.y - s * 0.18f); lineTo(c.x - s * 0.3f, c.y + s * 0.5f); lineTo(c.x + s * 0.3f, c.y + s * 0.5f)
        lineTo(c.x + s * 0.3f, c.y - s * 0.18f); lineTo(c.x + s * 0.42f, c.y - s * 0.12f); lineTo(c.x + s * 0.5f, c.y - s * 0.35f)
        lineTo(c.x + s * 0.18f, c.y - s * 0.5f); quadraticBezierTo(c.x, c.y - s * 0.36f, c.x - s * 0.18f, c.y - s * 0.5f); close()
    }, color, alpha = alpha)
    for (k in 0 until 3) drawCircle(lerpColor(color, Color.Black, 0.3f), s * 0.025f, Offset(c.x, c.y - s * 0.25f + k * s * 0.18f), alpha = alpha)
}

/** A sunshade on its pole, its foot at [foot]. */
internal fun DrawScope.fqShade(foot: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawLine(Color(0xFF5A4A3A), foot, foot + Offset(0f, -s), s * 0.04f, alpha = alpha)
    drawArc(color, 180f, 180f, true, Offset(foot.x - s * 0.5f, foot.y - s * 1.2f), Size(s, s * 0.45f), alpha = alpha)
    drawRect(color.copy(alpha = 0.18f), Offset(foot.x - s * 0.5f, foot.y - s * 0.02f), Size(s, s * 0.06f), alpha = alpha)
}
