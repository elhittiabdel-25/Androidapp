package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Notebook scenes: the idea drawn as a diagram in a study notebook — graph paper, ink and an amber pen — the way
 * an explainer sketches it: a chart (faith rises and falls), a circuit (ten breakers), a verdict with faceless
 * figures, an evidence board. Cream paper in the light theme, a dark page in the dark one.
 */

/** The notebook's colours, for the app's light or dark theme. */
internal class Paper(dark: Boolean) {
    val bg = if (dark) Color(0xFF13161E) else Color(0xFFF6F0E1)
    val grid = if (dark) Color(0xFF222936) else Color(0xFFDCE3EC)
    val ink = if (dark) Color(0xFFEAE2CC) else Color(0xFF1F2B48)
    val faint = if (dark) Color(0xFF7C8394) else Color(0xFF8E97A9)
    val amber = if (dark) Color(0xFFF2B451) else Color(0xFFC07A12)
    val good = if (dark) Color(0xFF6FCB9F) else Color(0xFF2E7D57)
    val bad = if (dark) Color(0xFFEE8158) else Color(0xFFB4492A)
    val card = if (dark) Color(0xFF1C212C) else Color(0xFFFFFBF1)
    val back = if (dark) Color(0xF213161E) else Color(0xF2F6F0E1)
}

/** Graph paper with the notebook's margin line on the right, where an Arabic page starts. */
internal fun DrawScope.paperSheet(pp: Paper) {
    drawRect(pp.bg)
    val step = 13f * density
    val sw = 0.6f * density
    var x = (size.width % step) / 2f
    while (x < size.width) {
        drawLine(pp.grid, Offset(x, 0f), Offset(x, size.height), sw)
        x += step
    }
    var y = (size.height % step) / 2f
    while (y < size.height) {
        drawLine(pp.grid, Offset(0f, y), Offset(size.width, y), sw)
        y += step
    }
    drawLine(pp.amber.copy(alpha = 0.3f), Offset(size.width * 0.975f, 0f), Offset(size.width * 0.975f, size.height), 0.8f * density)
}

/** A label written on the page: a small paper patch over the grid, ink text; [boxed] draws a frame (a speech bubble). */
internal fun DrawScope.note(tl: TextLayoutResult?, c: Offset, alpha: Float, pp: Paper, ink: Color = pp.ink, boxed: Boolean = false) {
    if (tl == null || alpha <= 0.01f) return
    val padX = 5f * density
    val padY = 2f * density
    val w = tl.size.width + 2 * padX
    val h = tl.size.height + 2 * padY
    val x = (c.x - w / 2).coerceIn(2f * density, max(2f * density, size.width - w - 2f * density))
    val y = (c.y - h / 2).coerceIn(2f * density, max(2f * density, size.height - h - 2f * density))
    val a = alpha.coerceIn(0f, 1f)
    drawRoundRect(if (boxed) pp.card else pp.back, topLeft = Offset(x, y), size = Size(w, h), cornerRadius = CornerRadius(5f * density), alpha = a)
    if (boxed) {
        drawRoundRect(ink, topLeft = Offset(x, y), size = Size(w, h), cornerRadius = CornerRadius(5f * density), alpha = a * 0.85f, style = Stroke(1.1f * density))
    }
    drawText(tl, color = ink, topLeft = Offset(x + padX, y + padY), alpha = a)
}

/** Length of a polyline. */
private fun polyLength(pts: List<Offset>): Float {
    var total = 0f
    for (i in 1 until pts.size) total += (pts[i] - pts[i - 1]).getDistance()
    return total
}

/** The point at distance [d] along a polyline. */
private fun polyAt(pts: List<Offset>, d: Float): Offset {
    var left = d
    for (i in 1 until pts.size) {
        val seg = (pts[i] - pts[i - 1]).getDistance()
        if (seg <= 0f) continue
        if (left <= seg) return pts[i - 1] + (pts[i] - pts[i - 1]) * (left / seg)
        left -= seg
    }
    return pts.last()
}

/** The first [d] of a polyline as a path. */
private fun polyPath(pts: List<Offset>, d: Float): Path {
    val path = Path()
    if (pts.isEmpty()) return path
    path.moveTo(pts[0].x, pts[0].y)
    var left = d
    for (i in 1 until pts.size) {
        val seg = (pts[i] - pts[i - 1]).getDistance()
        if (seg <= 0f) continue
        if (left >= seg) {
            path.lineTo(pts[i].x, pts[i].y)
            left -= seg
        } else {
            val q = pts[i - 1] + (pts[i] - pts[i - 1]) * (max(0f, left) / seg)
            path.lineTo(q.x, q.y)
            break
        }
    }
    return path
}

/** The first [p] (0..1) of a polyline, drawn by a pen. */
internal fun DrawScope.penLine(pts: List<Offset>, p: Float, color: Color, width: Float, alpha: Float = 1f, dash: PathEffect? = null) {
    if (p <= 0f || pts.size < 2 || alpha <= 0.01f) return
    val path = polyPath(pts, polyLength(pts) * p.coerceIn(0f, 1f))
    drawPath(path, color, alpha = alpha.coerceIn(0f, 1f), style = Stroke(width, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = dash))
}

/** A small arrow head at [tip] pointing along [dir] (a unit-ish direction). */
private fun DrawScope.arrowHead(tip: Offset, dir: Offset, len: Float, color: Color, width: Float, alpha: Float = 1f) {
    val d = dir.getDistance().takeIf { it > 0f } ?: return
    val u = dir / d
    val n = Offset(-u.y, u.x)
    drawLine(color, tip, tip - u * len + n * (len * 0.55f), width, cap = StrokeCap.Round, alpha = alpha)
    drawLine(color, tip, tip - u * len - n * (len * 0.55f), width, cap = StrokeCap.Round, alpha = alpha)
}

/** A cross mark (✗). */
private fun DrawScope.crossMark(c: Offset, r: Float, color: Color, width: Float, alpha: Float = 1f) {
    drawLine(color, c + Offset(-r, -r), c + Offset(r, r), width, cap = StrokeCap.Round, alpha = alpha)
    drawLine(color, c + Offset(-r, r), c + Offset(r, -r), width, cap = StrokeCap.Round, alpha = alpha)
}

/** A check mark (✓). */
private fun DrawScope.checkMark(c: Offset, r: Float, color: Color, width: Float, alpha: Float = 1f) {
    val p = Path().apply {
        moveTo(c.x - r, c.y)
        lineTo(c.x - r * 0.3f, c.y + r * 0.7f)
        lineTo(c.x + r, c.y - r * 0.75f)
    }
    drawPath(p, color, alpha = alpha, style = Stroke(width, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

/**
 * A faceless figure in a few strokes, feet at [foot], [tall] high. [walk] is the stride phase (radians), [lean] tilts
 * the body (positive leans to the left), [arms] 0 hangs them, 1 reaches forward (to the left), -1 pulls back.
 */
internal fun DrawScope.figure(foot: Offset, tall: Float, color: Color, alpha: Float = 1f, walk: Float = 0f, lean: Float = 0f, arms: Float = 0f, stride: Float = 1f) {
    if (alpha <= 0.01f) return
    val sw = max(1.7f * density, tall * 0.065f)
    val hip = foot - Offset(0f, tall * 0.45f)
    val up = Offset(-sin(lean), -cos(lean))
    val shoulder = hip + up * (tall * 0.31f)
    val head = shoulder + up * (tall * 0.15f)
    val swing = sin(walk) * tall * 0.14f * stride
    val stance = tall * 0.075f
    // legs: apart when standing, swinging when walking
    drawLine(color, hip, foot + Offset(-stance + swing, 0f), sw, cap = StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, foot + Offset(stance - swing, 0f), sw, cap = StrokeCap.Round, alpha = alpha)
    // body and head (a plain disc: no face)
    drawLine(color, hip, shoulder, sw, cap = StrokeCap.Round, alpha = alpha)
    drawCircle(color, radius = tall * 0.1f, center = head, alpha = alpha)
    // arms
    val reach = tall * 0.28f
    val hand1: Offset
    val hand2: Offset
    when {
        arms > 0f -> {           // reaching forward (to the left), as when speaking to someone
            hand1 = shoulder + Offset(-reach * (0.35f + 0.65f * arms), reach * (0.85f - 0.75f * arms))
            hand2 = shoulder + Offset(tall * 0.1f, reach * 0.95f)
        }
        arms < 0f -> {           // pulled back, resisting
            hand1 = shoulder + Offset(reach * 0.75f * -arms, reach * 0.55f)
            hand2 = shoulder + Offset(reach * 0.55f * -arms, reach * 0.8f)
        }
        else -> {
            hand1 = shoulder + Offset(-tall * 0.11f - swing * 0.7f, reach)
            hand2 = shoulder + Offset(tall * 0.11f + swing * 0.7f, reach)
        }
    }
    drawLine(color, shoulder, hand1, sw * 0.9f, cap = StrokeCap.Round, alpha = alpha)
    drawLine(color, shoulder, hand2, sw * 0.9f, cap = StrokeCap.Round, alpha = alpha)
}

/** A glowing dot (a badge of light), dimmed by [lit] (0..1). */
private fun DrawScope.badge(c: Offset, r: Float, pp: Paper, lit: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    if (lit > 0.02f) {
        drawCircle(Brush.radialGradient(listOf(pp.amber.copy(alpha = 0.45f * lit * alpha), Color.Transparent), center = c, radius = r * 3.2f), radius = r * 3.2f, center = c)
    }
    drawCircle(lerpColor(pp.faint, pp.amber, lit), radius = r, center = c, alpha = alpha)
}

private fun lerpColor(a: Color, b: Color, f: Float): Color = androidx.compose.ui.graphics.lerp(a, b, f.coerceIn(0f, 1f))

// ───────────── the chart: faith rises with obedience and falls with sins ─────────────

/** (time, level) pairs; time runs from the right (0) to the left (1), level 0 is the line nothing but kufr crosses. */
private val CHART = floatArrayOf(
    0f, 0.30f, 0.06f, 0.40f, 0.12f, 0.55f, 0.18f, 0.70f,             // gain: points 0..3
    0.26f, 0.50f, 0.30f, 0.46f,                                       // loss: 4..5
    0.36f, 0.60f, 0.42f, 0.28f, 0.48f, 0.50f, 0.54f, 0.22f,           // sins: 6..14
    0.60f, 0.42f, 0.66f, 0.18f, 0.72f, 0.38f, 0.78f, 0.15f, 0.84f, 0.30f,
)
private val CHART_DIPS = intArrayOf(7, 9, 11, 13)

internal fun DrawScope.chartScene(a: ArtCtx) {
    val pp = Paper(a.dark)
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    paperSheet(pp)
    fun px(u: Float) = mix(w * 0.86f, w * 0.06f, u)
    fun py(level: Float) = h * 0.68f - level * h * 0.54f
    val axisX = w * 0.9f
    val axisY = py(-0.33f)

    val axis = a.b("axis")
    val gain = a.b("gain")
    val loss = a.b("loss")
    val floorB = a.b("floor")
    val sins = a.b("sins")
    val cross = a.b("cross")
    val seed = a.b("seed")

    // the zone below the line: kufr
    val zone = 0.05f * easeOut(floorB) + 0.06f * easeOut(cross)
    if (zone > 0f) drawRect(pp.bad, topLeft = Offset(w * 0.04f, py(0f)), size = Size(axisX - w * 0.04f, axisY - py(0f)), alpha = zone)

    // axes: faith up the right side, time along the bottom to the left
    val ax = easeOut(seg(axis, 0f, 0.6f))
    penLine(listOf(Offset(axisX, axisY), Offset(axisX, h * 0.1f)), ax, pp.ink, 1.3f * dp)
    penLine(listOf(Offset(axisX, axisY), Offset(w * 0.03f, axisY)), easeOut(seg(axis, 0.3f, 1f)), pp.ink, 1.3f * dp)
    if (ax > 0.95f) arrowHead(Offset(axisX, h * 0.1f), Offset(0f, -1f), 5f * dp, pp.ink, 1.3f * dp)
    if (axis > 0.95f) arrowHead(Offset(w * 0.03f, axisY), Offset(-1f, 0f), 5f * dp, pp.ink, 1.3f * dp)
    note(a.tag("axis"), Offset(w * 0.7f, h * 0.08f), seg(axis, 0.4f, 1f), pp)

    // the line not to cross
    if (floorB > 0f) {
        val dash = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 4f * dp), 0f)
        penLine(listOf(Offset(axisX, py(0f)), Offset(w * 0.04f, py(0f))), easeOut(floorB), pp.bad, 1.4f * dp, dash = dash)
        note(a.tag("floor"), Offset(w * 0.7f, py(0f) + h * 0.06f), seg(floorB, 0.4f, 1f), pp, ink = pp.bad)
    }

    // the line of faith, drawn as the voice goes
    val n = CHART.size / 2
    val pts = List(n) { Offset(px(CHART[2 * it]), py(CHART[2 * it + 1])) }
    val upto = when {
        sins > 0f -> 5f + 9f * easeInOut(sins)
        loss > 0f -> 3f + 2f * easeInOut(loss)
        else -> 3f * easeInOut(gain)
    }
    if (upto > 0f) {
        val k = floor(upto).toInt().coerceIn(0, n - 1)
        val drawn = ArrayList<Offset>()
        for (i in 0..k) drawn += pts[i]
        if (k < n - 1) drawn += pts[k] + (pts[k + 1] - pts[k]) * (upto - k)
        penLine(drawn, 1f, pp.ink, 2f * dp)
        if ((gain in 0.01f..0.99f) || (loss in 0.01f..0.99f) || (sins in 0.01f..0.99f)) {
            drawCircle(pp.amber, radius = 2.6f * dp, center = drawn.last())
        }
        // deeds (rises) and sins (dips)
        for (i in intArrayOf(1, 2, 3, 6, 8, 10, 12)) {
            if (upto >= i) drawCircle(pp.good, radius = 2.3f * dp, center = pts[i], alpha = seg(upto, i.toFloat(), i + 0.3f))
        }
        for (i in intArrayOf(4) + CHART_DIPS) {
            if (upto >= i) crossMark(pts[i] + Offset(0f, 5f * dp), 2.6f * dp, pp.bad, 1.4f * dp, seg(upto, i.toFloat(), i + 0.3f))
        }
    }
    note(a.tag("gain"), Offset(px(0.12f), py(0.55f) - h * 0.1f), seg(gain, 0.5f, 1f), pp, ink = pp.good)
    note(a.tag("loss"), Offset(px(0.3f), py(0.46f) + h * 0.11f), seg(loss, 0.4f, 1f), pp, ink = pp.bad)
    // each sin named as the pen reaches it, then gone (the cross marks stay)
    for ((m, i) in CHART_DIPS.withIndex()) {
        val on = seg(upto, i - 0.4f, i.toFloat())
        val off = if (m < CHART_DIPS.size - 1) seg(upto, i + 1.4f, i + 2f) else seg(cross, 0f, 0.3f)
        note(a.tag("sins", 0, m), pts[i] + Offset(0f, h * 0.075f), on * (1f - off), pp, ink = pp.bad)
    }

    // deeming it lawful: the line plunges through
    if (cross > 0f) {
        val from = pts[n - 1]
        val to = Offset(px(0.92f), py(-0.25f))
        val dash = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 3.5f * dp), 0f)
        val dim = 1f - 0.65f * seg(seed, 0f, 0.3f)
        penLine(listOf(from, to), easeIn(seg(cross, 0f, 0.6f)), pp.bad, 2f * dp, alpha = dim, dash = dash)
        if (cross > 0.6f) crossMark(to, 4f * dp, pp.bad, 2f * dp, seg(cross, 0.6f, 0.8f) * dim)
        note(a.tag("cross"), Offset(to.x + w * 0.15f, to.y - h * 0.02f), seg(cross, 0.55f, 1f) * (1f - seg(seed, 0f, 0.3f)), pp, ink = pp.bad)
    }

    // a mustard seed of faith, still above the line, and lifted
    if (seed > 0f) {
        val c = Offset(px(0.975f), py(0f) - 3f * dp)
        val glow = easeOut(seg(seed, 0f, 0.5f)) * (0.8f + 0.2f * sin(t * 2.6f))
        drawCircle(Brush.radialGradient(listOf(pp.amber.copy(alpha = 0.6f * glow), Color.Transparent), center = c, radius = 14f * dp), radius = 14f * dp, center = c)
        drawOval(pp.amber, topLeft = c - Offset(2.6f * dp, 2f * dp), size = Size(5.2f * dp, 4f * dp), alpha = min(1f, glow * 1.4f))
        for (m in 0 until 4) {
            val f = fract(t * 0.35f + m * 0.25f)
            drawCircle(pp.amber, radius = 1.2f * dp, center = c + Offset(sin(m * 2.1f + t) * 4f * dp, -f * h * 0.22f), alpha = glow * (1f - f) * 0.8f)
        }
        note(a.tag("seed"), Offset(c.x + w * 0.2f, c.y + h * 0.1f), seg(seed, 0.3f, 0.9f), pp, ink = pp.amber)
    }
}

private fun easeIn(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ───────────── the circuit: ten breakers in a row, any one cuts the light ─────────────

internal fun DrawScope.circuitScene(a: ArtCtx) {
    val pp = Paper(a.dark)
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    paperSheet(pp)

    val power = a.b("power")
    val nB = min(10, a.count("breaker"))
    val trip = a.b("trip")
    val guard = a.b("guard")
    val tripAt = a.nth("trip").let { if (it < 0) 0 else a.sideAt(it).coerceIn(0, 9) }
    val open = easeOut(seg(trip, 0f, 0.35f)) * (1f - easeOut(seg(guard, 0f, 0.5f)))

    val row1 = h * 0.2f
    val row2 = h * 0.47f
    val word = Offset(w * 0.5f, h * 0.77f)
    val boxW = w * 0.34f
    val boxH = h * 0.17f
    val wire = listOf(
        Offset(w * 0.92f, row1), Offset(w * 0.06f, row1), Offset(w * 0.06f, row2), Offset(w * 0.92f, row2),
        Offset(w * 0.92f, word.y), Offset(word.x + boxW / 2, word.y),
    )
    val total = polyLength(wire)
    fun bx(k: Int) = if (k < 5) w * (0.8f - 0.165f * k) else w * (0.14f + 0.165f * (k - 5))
    fun by(k: Int) = if (k < 5) row1 else row2
    // distance along the wire of each breaker
    fun bd(k: Int): Float = if (k < 5) w * 0.92f - bx(k) else (w * 0.86f) + (row2 - row1) + (bx(k) - w * 0.06f)

    // the wire, then the current (moving dashes) as far as the first open breaker
    val wireIn = easeOut(seg(power, 0f, 0.6f))
    penLine(wire, wireIn, pp.ink, 1.4f * dp)
    drawCircle(pp.ink, radius = 3.4f * dp, center = wire[0], alpha = wireIn)
    drawCircle(pp.amber, radius = 1.8f * dp, center = wire[0], alpha = wireIn)
    val reach = if (open > 0.3f && tripAt < nB) bd(tripAt) - w * 0.025f else total
    val flow = seg(power, 0.4f, 1f)
    if (flow > 0f) {
        val dash = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 7f * dp), -t * 30f * dp)
        drawPath(polyPath(wire, min(reach, total * easeOut(flow))), pp.amber, alpha = 0.95f,
            style = Stroke(2.2f * dp, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = dash))
    }

    // the breakers, each where its name is read
    val gap = w * 0.06f
    for (k in 0 until nB) {
        val p = a.b("breaker", k)
        if (p <= 0f) continue
        val c = Offset(bx(k), by(k))
        val ap = seg(p, 0f, 0.3f)
        // its housing, over the wire
        val hw = gap * 1.55f
        val hh = h * 0.1f
        val housing = Offset(c.x - hw / 2, c.y - hh * 0.62f)
        drawRoundRect(pp.card, topLeft = housing, size = Size(hw, hh), cornerRadius = CornerRadius(4f * dp), alpha = ap)
        val guarded = easeOut(seg(guard, 0.3f, 0.9f))
        drawRoundRect(lerpColor(pp.ink, pp.good, guarded), topLeft = housing, size = Size(hw, hh), cornerRadius = CornerRadius(4f * dp),
            alpha = ap * (0.7f + 0.3f * guarded), style = Stroke((1.1f + 0.6f * guarded) * dp))
        // the current comes from the right on the first row, from the left on the second
        val hinge = if (k < 5) c + Offset(gap / 2, 0f) else c - Offset(gap / 2, 0f)
        val far = if (k < 5) c - Offset(gap / 2, 0f) else c + Offset(gap / 2, 0f)
        val lift = if (k == tripAt) open else 0f
        val ang = (9f + 36f * lift) * PI.toFloat() / 180f
        val dir = if (k < 5) -1f else 1f
        val lever = hinge + Offset(dir * gap * cos(ang), -gap * sin(ang))
        val col = if (lift > 0.3f) pp.bad else pp.ink
        drawLine(pp.ink, Offset(housing.x, c.y), Offset(far.x, c.y), 1.4f * dp, alpha = ap)
        drawLine(pp.ink, Offset(hinge.x, c.y), Offset(housing.x + hw, c.y), 1.4f * dp, alpha = ap)
        drawCircle(pp.ink, radius = 2.6f * dp, center = hinge, alpha = ap)
        drawCircle(pp.ink, radius = 2.6f * dp, center = far, alpha = ap)
        drawLine(col, hinge, lever, 2.2f * dp, cap = StrokeCap.Round, alpha = ap)
        // its name while it is new
        val next = if (k + 1 < nB) a.b("breaker", k + 1) else seg(trip, 0f, 0.2f)
        note(a.tag("breaker", k), c + Offset(0f, h * 0.1f), seg(p, 0.2f, 0.5f) * (1f - seg(next, 0f, 0.35f)), pp)
    }
    // the tripped one, named again
    if (trip > 0f && tripAt < nB) {
        val c = Offset(bx(tripAt), by(tripAt))
        note(a.tag("trip"), c + Offset(0f, h * 0.1f), seg(trip, 0.2f, 0.5f) * (1f - seg(guard, 0f, 0.3f)), pp, ink = pp.bad)
    }

    // the word the current lights
    val lit = easeOut(seg(power, 0.5f, 1f)) * (1f - open * 0.85f)
    val tl = a.tag("power")
    val boxA = seg(power, 0.2f, 0.6f)
    if (lit > 0.02f) {
        drawCircle(Brush.radialGradient(listOf(pp.amber.copy(alpha = 0.35f * lit), Color.Transparent), center = word, radius = boxW * 0.8f), radius = boxW * 0.8f, center = word)
    }
    drawRoundRect(pp.card, topLeft = word - Offset(boxW / 2, boxH / 2), size = Size(boxW, boxH), cornerRadius = CornerRadius(8f * dp), alpha = boxA)
    drawRoundRect(lerpColor(pp.ink, pp.amber, lit), topLeft = word - Offset(boxW / 2, boxH / 2), size = Size(boxW, boxH), cornerRadius = CornerRadius(8f * dp), alpha = boxA, style = Stroke(1.4f * dp))
    if (tl != null && boxA > 0f) {
        val sc = min(2.2f, boxW * 0.82f / tl.size.width.toFloat().coerceAtLeast(1f))
        withTransform({ scale(sc, sc, pivot = word) }) {
            drawText(tl, color = lerpColor(pp.faint, pp.amber, lit), topLeft = word - Offset(tl.size.width / 2f, tl.size.height / 2f), alpha = boxA)
        }
    }
    note(a.tag("guard"), Offset(w * 0.5f, h * 0.93f), seg(guard, 0.4f, 1f), pp, ink = pp.good)
}

// ───────────── the verdict: jesting, serious or afraid, the same — except the one forced ─────────────

internal fun DrawScope.verdictScene(a: ArtCtx) {
    val pp = Paper(a.dark)
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    paperSheet(pp)
    val ground = h * 0.8f
    val tall = h * 0.27f

    val voidB = a.b("void")
    val less = a.b("less")
    val will = a.b("will")
    val echo = a.b("echo")
    val act1 = 1f - seg(voidB, 0f, 0.3f)
    val act2 = seg(voidB, 0f, 0.3f) * (1f - seg(echo, 0f, 0.25f))
    val act3 = seg(echo, 0.1f, 0.3f)

    // act 1: four figures cross the line one by one
    if (act1 > 0.01f) {
        drawLine(pp.faint, Offset(w * 0.03f, ground), Offset(w * 0.97f, ground), 1.2f * dp, alpha = act1)
        val dash = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp), 0f)
        val anyone = a.b("walker") > 0f
        drawLine(pp.bad, Offset(w * 0.5f, h * 0.16f), Offset(w * 0.5f, ground), 1.4f * dp, pathEffect = dash, alpha = act1 * (if (anyone) 1f else 0.5f))
        tagLine(a, pp, act1)
        val finals = floatArrayOf(0.1f, 0.21f, 0.32f, 0.43f)
        for (k in 0 until 4) {
            val forced = k == 3
            val p = if (forced) a.b("forced") else a.b("walker", k)
            if (p <= 0f) continue
            val go = easeInOut(seg(p, 0.1f, 0.85f))
            val x = mix(w * 0.9f, w * finals[k], go)
            val stepPhase = go * 14f
            val walking = p in 0.1f..0.85f
            val foot = Offset(x, ground)
            figure(foot, tall, pp.ink, alpha = act1 * seg(p, 0f, 0.12f), walk = if (walking) stepPhase else 0f,
                lean = if (forced) -0.28f * seg(p, 0.05f, 0.3f) else 0f, arms = if (forced) -1f else 0f)
            // the force that pushes the one forced: a heavy block arrow at his back, with motion lines
            if (forced) {
                val al = act1 * seg(p, 0f, 0.15f) * (1f - 0.6f * seg(p, 0.85f, 1f))
                val bx = x + tall * 0.42f
                val by = ground - tall * 0.5f
                val arrow = Path().apply {
                    moveTo(bx, by)
                    lineTo(bx + tall * 0.28f, by - tall * 0.22f)
                    lineTo(bx + tall * 0.28f, by - tall * 0.1f)
                    lineTo(bx + tall * 0.7f, by - tall * 0.1f)
                    lineTo(bx + tall * 0.7f, by + tall * 0.1f)
                    lineTo(bx + tall * 0.28f, by + tall * 0.1f)
                    lineTo(bx + tall * 0.28f, by + tall * 0.22f)
                    close()
                }
                drawPath(arrow, pp.bad, alpha = al * 0.85f)
                for (m in 0 until 3) {
                    val yy = by + (m - 1) * tall * 0.16f
                    drawLine(pp.bad, Offset(bx + tall * 0.8f, yy), Offset(bx + tall * (1.05f + 0.1f * m), yy), 1.2f * dp, cap = StrokeCap.Round, alpha = al * 0.7f)
                }
            }
            // the light of Islam over each one: out for those who cross by choice, kept for the one forced
            val crossed = seg(p, 0.6f, 0.85f)
            val lit = if (forced) 1f else 1f - crossed
            val bc = foot - Offset(0f, tall * 1.18f)
            badge(bc, 3.2f * dp, pp, lit, act1 * seg(p, 0f, 0.15f))
            if (!forced && crossed > 0.5f) crossMark(bc + Offset(7f * dp, -5f * dp), 2.4f * dp, pp.bad, 1.3f * dp, act1 * seg(crossed, 0.5f, 1f))
            if (forced && p > 0.85f) checkMark(bc + Offset(8f * dp, -5f * dp), 3f * dp, pp.good, 1.4f * dp, act1 * seg(p, 0.85f, 1f))
            note(if (forced) a.tag("forced") else a.tag("walker", k), Offset(x, ground + h * 0.085f), act1 * seg(p, 0.2f, 0.5f), pp,
                ink = if (forced) pp.good else pp.ink)
        }
    }

    // act 2: two gauges — what voids Islam, and the sin that only lessens faith
    if (act2 > 0.01f) {
        val bw = w * 0.085f
        val top = h * 0.26f
        val bot = h * 0.7f
        for (k in 0 until 2) {
            val cx = if (k == 0) w * 0.68f else w * 0.32f
            val p = if (k == 0) voidB else less
            val appear = seg(p, 0f, 0.25f)
            if (appear <= 0f) continue
            val drop = easeInOut(seg(p, 0.3f, 0.85f))
            val fill = if (k == 0) 0.82f * (1f - drop) else 0.82f - 0.4f * drop
            val al = act2 * appear
            drawRoundRect(pp.card, topLeft = Offset(cx - bw / 2, top), size = Size(bw, bot - top), cornerRadius = CornerRadius(6f * dp), alpha = al)
            drawRoundRect(if (k == 0) pp.amber else pp.amber, topLeft = Offset(cx - bw / 2, bot - (bot - top) * fill), size = Size(bw, (bot - top) * fill),
                cornerRadius = CornerRadius(6f * dp), alpha = al * 0.85f)
            drawRoundRect(pp.ink, topLeft = Offset(cx - bw / 2, top), size = Size(bw, bot - top), cornerRadius = CornerRadius(6f * dp), alpha = al, style = Stroke(1.3f * dp))
            if (k == 0 && drop > 0.6f) {
                // a crack through the empty gauge
                val crack = listOf(Offset(cx - bw * 0.7f, top + (bot - top) * 0.35f), Offset(cx - bw * 0.1f, top + (bot - top) * 0.45f),
                    Offset(cx - bw * 0.3f, top + (bot - top) * 0.55f), Offset(cx + bw * 0.7f, top + (bot - top) * 0.62f))
                penLine(crack, seg(drop, 0.6f, 1f), pp.bad, 1.6f * dp, alpha = al)
            }
            note(if (k == 0) a.tag("void") else a.tag("less"), Offset(cx, bot + h * 0.08f), al * seg(p, 0.2f, 0.5f), pp, ink = if (k == 0) pp.bad else pp.ink)
        }
        // under Allah's will: a bracket over the gauge that lessened, and two ways from it
        if (will > 0f) {
            val cx = w * 0.32f
            val al = act2 * seg(will, 0f, 0.3f)
            val arc = Path().apply {
                moveTo(cx - w * 0.1f, top - h * 0.02f)
                quadraticBezierTo(cx, top - h * 0.13f, cx + w * 0.1f, top - h * 0.02f)
            }
            drawPath(arc, pp.amber, alpha = al, style = Stroke(1.5f * dp, cap = StrokeCap.Round))
            val fork = easeOut(seg(will, 0.3f, 0.8f))
            penLine(listOf(Offset(cx, top - h * 0.075f), Offset(cx - w * 0.12f, top - h * 0.03f)), fork, pp.faint, 1.2f * dp, alpha = al)
            penLine(listOf(Offset(cx, top - h * 0.075f), Offset(cx + w * 0.12f, top - h * 0.03f)), fork, pp.faint, 1.2f * dp, alpha = al)
            note(a.tag("will"), Offset(cx, top - h * 0.16f), al * seg(will, 0.3f, 0.7f), pp, ink = pp.amber)
        }
    }

    // act 3: «يا كافر» — said to a brother, and come back to the one who said it
    if (act3 > 0.01f) {
        drawLine(pp.faint, Offset(w * 0.03f, ground), Offset(w * 0.97f, ground), 1.2f * dp, alpha = act3)
        val speaker = Offset(w * 0.74f, ground)
        val other = Offset(w * 0.26f, ground)
        figure(speaker, tall, pp.ink, alpha = act3, arms = seg(echo, 0.1f, 0.3f) * (1f - seg(echo, 0.6f, 0.75f)))
        figure(other, tall, pp.ink, alpha = act3)
        val hit = seg(echo, 0.8f, 0.92f)
        badge(speaker - Offset(0f, tall * 1.18f), 3.2f * dp, pp, 1f - 0.85f * hit, act3)
        badge(other - Offset(0f, tall * 1.18f), 3.2f * dp, pp, 1f, act3)
        val go = easeInOut(seg(echo, 0.15f, 0.5f))
        val back = easeInOut(seg(echo, 0.6f, 0.85f))
        val y0 = ground - tall * 0.95f
        val x = mix(mix(speaker.x - w * 0.1f, other.x + w * 0.1f, go), speaker.x - w * 0.02f, back)
        val arcY = y0 - sin(PI.toFloat() * (go + back) / 2f) * h * 0.1f
        val bubbleA = act3 * seg(echo, 0.12f, 0.2f) * (1f - seg(echo, 0.88f, 0.95f))
        note(a.tag("echo", 0, 0), Offset(x, arcY), bubbleA, pp, boxed = true)
        note(a.tag("echo", 0, 1), Offset(speaker.x, ground + h * 0.085f), act3 * seg(echo, 0.86f, 1f), pp, ink = pp.bad)
    }
}

/** The scene's side title (if any) as the name of the line that is crossed. */
private fun DrawScope.tagLine(a: ArtCtx, pp: Paper, alpha: Float) {
    val tl = a.extras.getOrNull(0) ?: return
    note(tl, Offset(size.width * 0.5f, size.height * 0.1f), alpha * seg(a.b("walker"), 0f, 0.3f), pp, ink = pp.bad)
}

// ───────────── the evidence board: four proofs, one conclusion ─────────────

internal fun DrawScope.boardScene(a: ArtCtx) {
    val pp = Paper(a.dark)
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    paperSheet(pp)

    val core = a.b("core")
    val n = min(4, a.count("pin"))
    val centre = Offset(w * 0.5f, h * 0.5f)
    val cw = w * 0.3f
    val ch = h * 0.17f
    val spots = listOf(Offset(w * 0.83f, h * 0.25f), Offset(w * 0.17f, h * 0.25f), Offset(w * 0.83f, h * 0.76f), Offset(w * 0.17f, h * 0.76f))
    val tilt = floatArrayOf(-3f, 2.5f, 2f, -2.5f)
    val nw = w * 0.24f
    val nh = h * 0.34f

    var linked = 0f
    for (k in 0 until n) linked += seg(a.beatAt(a.nth("pin", k)), 0.45f, 1f)

    // the conclusion in the middle, brighter with each proof tied to it
    if (core > 0f) {
        val glow = (linked / 4f).coerceIn(0f, 1f)
        if (glow > 0.02f) {
            drawCircle(Brush.radialGradient(listOf(pp.amber.copy(alpha = 0.35f * glow * (0.85f + 0.15f * sin(t * 2f))), Color.Transparent), center = centre, radius = cw * 0.9f),
                radius = cw * 0.9f, center = centre)
        }
        val ca = seg(core, 0f, 0.5f)
        drawRoundRect(pp.card, topLeft = centre - Offset(cw / 2, ch / 2), size = Size(cw, ch), cornerRadius = CornerRadius(6f * dp), alpha = ca)
        drawRoundRect(lerpColor(pp.ink, pp.amber, glow), topLeft = centre - Offset(cw / 2, ch / 2), size = Size(cw, ch), cornerRadius = CornerRadius(6f * dp),
            alpha = ca, style = Stroke((1.2f + 0.8f * glow) * dp))
        val tl = a.tag("core")
        if (tl != null) {
            val sc = min(1.35f, cw * 0.86f / tl.size.width.toFloat().coerceAtLeast(1f))
            withTransform({ scale(sc, sc, pivot = centre) }) {
                drawText(tl, color = lerpColor(pp.ink, pp.amber, glow), topLeft = centre - Offset(tl.size.width / 2f, tl.size.height / 2f), alpha = ca)
            }
        }
    }

    // the four notes, pinned one by one
    for (k in 0 until n) {
        val i = a.nth("pin", k)
        val p = a.beatAt(i)
        if (p <= 0f) continue
        val s = a.sideAt(i).coerceIn(0, 3)
        val drop = (1f - easeOut(seg(p, 0f, 0.35f))) * h * 0.12f
        val c = spots[s] - Offset(0f, drop)
        val al = seg(p, 0f, 0.2f)
        rotate(degrees = tilt[s], pivot = c) {
            drawRoundRect(pp.card, topLeft = c - Offset(nw / 2, nh / 2), size = Size(nw, nh), cornerRadius = CornerRadius(3f * dp), alpha = al)
            drawRoundRect(pp.faint, topLeft = c - Offset(nw / 2, nh / 2), size = Size(nw, nh), cornerRadius = CornerRadius(3f * dp), alpha = al * 0.6f, style = Stroke(0.8f * dp))
            proofIcon(s, c - Offset(0f, nh * 0.1f), nh * 0.24f, pp, al, t)
            note(a.tagOf(i), c + Offset(0f, nh * 0.3f), al * seg(p, 0.25f, 0.55f), pp)
        }
        val pinAt = c - Offset(0f, nh * 0.42f)
        drawCircle(pp.bad, radius = 3.4f * dp, center = pinAt, alpha = al)
        drawCircle(Color.White, radius = 1.1f * dp, center = pinAt - Offset(1f * dp, 1f * dp), alpha = al * 0.7f)
    }
    // red threads from each note's pin to the conclusion's pins, on top of everything
    for (k in 0 until n) {
        val i = a.nth("pin", k)
        val p = a.beatAt(i)
        val thread = seg(p, 0.45f, 1f)
        if (thread <= 0f) continue
        val s2 = a.sideAt(i).coerceIn(0, 3)
        val from = spots[s2] - Offset(0f, nh * 0.42f)
        val upper = spots[s2].y < centre.y
        val to = centre + Offset(0f, if (upper) -ch * 0.5f else ch * 0.5f)
        val mid = Offset((from.x + to.x) / 2f, (from.y + to.y) / 2f + h * 0.06f)
        val pts = List(16) { m ->
            val u = m / 15f
            val q1 = from + (mid - from) * u
            val q2 = mid + (to - mid) * u
            q1 + (q2 - q1) * u
        }
        penLine(pts, easeOut(thread), pp.bad, 1.4f * dp, alpha = seg(p, 0.45f, 0.6f))
    }
    if (core > 0f) {
        for (yy in floatArrayOf(-ch * 0.5f, ch * 0.5f)) {
            if (linked > 0f) drawCircle(pp.bad, radius = 3f * dp, center = centre + Offset(0f, yy), alpha = min(1f, linked))
        }
    }
}


/** The small drawing on each proof's note: coins pushed away, a seal, a route that came true, a page with a line marked. */
private fun DrawScope.proofIcon(kind: Int, c: Offset, r: Float, pp: Paper, alpha: Float, t: Float) {
    val dp = density
    val sw = 1.3f * dp
    when (kind) {
        0 -> {
            for (m in 0 until 3) {
                val y = c.y + r * 0.45f - m * r * 0.32f
                drawOval(pp.amber, topLeft = Offset(c.x - r * 0.15f - r * 0.5f, y - r * 0.16f), size = Size(r, r * 0.32f), alpha = alpha * 0.9f)
                drawOval(pp.ink, topLeft = Offset(c.x - r * 0.15f - r * 0.5f, y - r * 0.16f), size = Size(r, r * 0.32f), alpha = alpha, style = Stroke(sw * 0.8f))
            }
            val arrow = listOf(Offset(c.x + r * 0.25f, c.y), Offset(c.x + r * 0.95f, c.y - r * 0.2f))
            penLine(arrow, 1f, pp.ink, sw, alpha)
            arrowHead(arrow[1], arrow[1] - arrow[0], r * 0.25f, pp.ink, sw, alpha)
        }
        1 -> {
            drawCircle(pp.ink, radius = r * 0.62f, center = c, alpha = alpha, style = Stroke(sw))
            drawCircle(pp.ink, radius = r * 0.45f, center = c, alpha = alpha * 0.6f, style = Stroke(sw * 0.7f))
            checkMark(c, r * 0.26f, pp.good, sw * 1.2f, alpha)
        }
        2 -> {
            val from = c + Offset(r * 0.8f, r * 0.35f)
            val to = c + Offset(-r * 0.8f, -r * 0.25f)
            val pts = List(14) { m ->
                val u = m / 13f
                Offset(mix(from.x, to.x, u), mix(from.y, to.y, u) - sin(PI.toFloat() * u) * r * 0.45f)
            }
            penLine(pts, 1f, pp.ink, sw, alpha, PathEffect.dashPathEffect(floatArrayOf(3f * dp, 2.5f * dp), 0f))
            drawCircle(pp.ink, radius = 2.2f * dp, center = from, alpha = alpha)
            drawLine(pp.ink, to, to - Offset(0f, r * 0.55f), sw, alpha = alpha)
            val flag = Path().apply {
                moveTo(to.x, to.y - r * 0.55f)
                lineTo(to.x - r * 0.35f, to.y - r * 0.42f)
                lineTo(to.x, to.y - r * 0.3f)
                close()
            }
            drawPath(flag, pp.amber, alpha = alpha)
        }
        else -> {
            val left = Path().apply {
                moveTo(c.x, c.y - r * 0.5f)
                lineTo(c.x - r * 0.95f, c.y - r * 0.62f)
                lineTo(c.x - r * 0.95f, c.y + r * 0.5f)
                lineTo(c.x, c.y + r * 0.62f)
                close()
            }
            val right = Path().apply {
                moveTo(c.x, c.y - r * 0.5f)
                lineTo(c.x + r * 0.95f, c.y - r * 0.62f)
                lineTo(c.x + r * 0.95f, c.y + r * 0.5f)
                lineTo(c.x, c.y + r * 0.62f)
                close()
            }
            drawPath(left, pp.card, alpha = alpha)
            drawPath(right, pp.card, alpha = alpha)
            drawPath(left, pp.ink, alpha = alpha, style = Stroke(sw))
            drawPath(right, pp.ink, alpha = alpha, style = Stroke(sw))
            for (m in 0 until 4) {
                val y = c.y - r * 0.32f + m * r * 0.24f
                val mark = m == 2
                drawLine(if (mark) pp.amber else pp.faint, Offset(c.x + r * 0.15f, y), Offset(c.x + r * 0.8f, y), if (mark) sw * 2.2f else sw * 0.8f, alpha = alpha)
                drawLine(pp.faint, Offset(c.x - r * 0.8f, y), Offset(c.x - r * 0.15f, y), sw * 0.8f, alpha = alpha)
            }
        }
    }
}
