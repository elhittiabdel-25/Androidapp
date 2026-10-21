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
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 8, round 1 of the redraw after the scene review (2026-10-21): every card of «أهل السنة وخصائصهم» gets a
 * picture of its own, and no two neighbours share a register — footprints on a road from dawn to now, a river
 * seen from above, a loom of 72 threads on four knots, a tiled public fountain, a shore at dusk (this file); a
 * drafting desk, a mosque by day, a parchment in two halves, a field through the seasons and a street at night
 * (ArtWave8b.kt). People are faceless figures, companions plain roundels, the Prophet ﷺ only light.
 */

// ───────────── helpers shared by the wave-8 scenes ─────────────

internal fun w8EaseIn(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

internal fun w8Rnd(k: Int, salt: Float): Float = fract(sin(k * salt) * 43758.547f)

internal fun w8Poly(pts: List<Offset>): Path = Path().apply {
    pts.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
}

/** A smooth curve through [ctrl] (Catmull-Rom), sampled into a polyline. */
internal fun w8Smooth(ctrl: List<Offset>, per: Int = 10): List<Offset> {
    if (ctrl.size < 3) return ctrl
    val out = ArrayList<Offset>()
    for (i in 0 until ctrl.size - 1) {
        val p0 = ctrl[max(0, i - 1)]
        val p1 = ctrl[i]
        val p2 = ctrl[i + 1]
        val p3 = ctrl[min(ctrl.size - 1, i + 2)]
        for (k in 0 until per) {
            val t = k / per.toFloat()
            val t2 = t * t
            val t3 = t2 * t
            out.add((p1 * 2f + (p2 - p0) * t + (p0 * 2f - p1 * 5f + p2 * 4f - p3) * t2 + (p1 * 3f - p0 - p2 * 3f + p3) * t3) * 0.5f)
        }
    }
    out.add(ctrl.last())
    return out
}

/** The point at fraction [u] of a polyline's length. */
internal fun w8Along(pts: List<Offset>, u: Float): Offset {
    var total = 0f
    for (i in 1 until pts.size) total += (pts[i] - pts[i - 1]).getDistance()
    var left = total * u.coerceIn(0f, 1f)
    for (i in 1 until pts.size) {
        val s = (pts[i] - pts[i - 1]).getDistance()
        if (s <= 0f) continue
        if (left <= s) return pts[i - 1] + (pts[i] - pts[i - 1]) * (left / s)
        left -= s
    }
    return pts.last()
}

/** The first [p] (0..1) of a polyline, by points. */
internal fun w8Head(pts: List<Offset>, p: Float): List<Offset> {
    if (p >= 1f || pts.size < 2) return pts
    val n = (pts.size - 1) * p.coerceIn(0f, 1f)
    val i = n.toInt()
    val out = ArrayList(pts.subList(0, i + 1))
    if (i + 1 < pts.size) out.add(pts[i] + (pts[i + 1] - pts[i]) * (n - i))
    return out
}

/** A band (a road, a river) along [pts], [half] wide on each side at each fraction of its points. */
internal fun w8Band(pts: List<Offset>, half: (Float) -> Float): Path {
    val left = ArrayList<Offset>()
    val right = ArrayList<Offset>()
    for (i in pts.indices) {
        val a = pts[max(0, i - 1)]
        val b = pts[min(pts.size - 1, i + 1)]
        val d = b - a
        val len = max(0.001f, d.getDistance())
        val nrm = Offset(-d.y / len, d.x / len)
        val hw = half(if (pts.size > 1) i / (pts.size - 1f) else 0f)
        left.add(pts[i] + nrm * hw)
        right.add(pts[i] - nrm * hw)
    }
    return Path().apply {
        left.forEachIndexed { i, q -> if (i == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y) }
        for (q in right.asReversed()) lineTo(q.x, q.y)
        close()
    }
}

/** A companion who is not named: a plain roundel (the codex). [grow] pops it in. */
internal fun DrawScope.w8Roundel(c: Offset, r: Float, grow: Float) {
    if (grow <= 0.01f) return
    val g = grow.coerceIn(0f, 1f)
    val rr = r * (0.6f + 0.4f * easeOut(g))
    drawCircle(Color.Black, rr * 1.05f, c + Offset(0f, rr * 0.15f), alpha = 0.2f * g)
    drawCircle(Color(0xFF3A3F4B), rr, c, alpha = g)
    drawCircle(Color(0xFFE7C77A), rr, c, alpha = g * 0.9f, style = Stroke(rr * 0.14f))
    drawCircle(Color(0xFFE7C77A), rr * 0.45f, c, alpha = g * 0.45f, style = Stroke(rr * 0.09f))
}

/** A heart, point down, centred at [c]. */
internal fun w8Heart(c: Offset, r: Float): Path = Path().apply {
    moveTo(c.x, c.y + r * 0.9f)
    cubicTo(c.x - r * 1.35f, c.y + r * 0.05f, c.x - r * 0.8f, c.y - r * 1.0f, c.x, c.y - r * 0.38f)
    cubicTo(c.x + r * 0.8f, c.y - r * 1.0f, c.x + r * 1.35f, c.y + r * 0.05f, c.x, c.y + r * 0.9f)
    close()
}

/** A figure sitting cross-legged at [seat], facing [facing] (-1 left, 1 right); [speak] lifts the near arm. */
internal fun DrawScope.w8Seated(seat: Offset, tall: Float, color: Color, facing: Float, alpha: Float = 1f, speak: Float = 0f) {
    if (alpha <= 0.01f) return
    val sw = max(1.7f * density, tall * 0.065f)
    val hip = seat - Offset(0f, tall * 0.05f)
    val knee = hip + Offset(facing * tall * 0.22f, -tall * 0.02f)
    val shoulder = hip + Offset(-facing * tall * 0.02f, -tall * 0.33f)
    val head = shoulder + Offset(0f, -tall * 0.15f)
    drawLine(color, hip, knee, sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, knee, Offset(hip.x + facing * tall * 0.04f, seat.y), sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, shoulder, sw, StrokeCap.Round, alpha = alpha)
    drawCircle(color, tall * 0.1f, head, alpha = alpha)
    val rest = knee + Offset(-facing * tall * 0.04f, -tall * 0.05f)
    val lifted = shoulder + Offset(facing * tall * 0.24f, -tall * 0.02f)
    drawLine(color, shoulder, rest + (lifted - rest) * speak.coerceIn(0f, 1f), sw * 0.9f, StrokeCap.Round, alpha = alpha)
    drawLine(color, shoulder, knee + Offset(-facing * tall * 0.1f, -tall * 0.02f), sw * 0.9f, StrokeCap.Round, alpha = alpha * 0.85f)
}

/**
 * A figure in prayer, facing left: [raise] lifts the hands beside the head (the takbir), otherwise they are folded
 * on the chest; [bow] bends the back to the ruku' with the hands on the knees.
 */
internal fun DrawScope.w8Worshipper(foot: Offset, tall: Float, color: Color, raise: Float, bow: Float, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    val sw = max(1.7f * density, tall * 0.065f)
    val hip = foot - Offset(0f, tall * 0.45f)
    val ang = bow.coerceIn(0f, 1f) * PI.toFloat() * 0.47f
    val up = Offset(-sin(ang), -cos(ang))
    val shoulder = hip + up * (tall * 0.31f)
    val head = shoulder + up * (tall * 0.15f)
    drawLine(color, hip, foot + Offset(-tall * 0.04f, 0f), sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, foot + Offset(tall * 0.04f, 0f), sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, shoulder, sw, StrokeCap.Round, alpha = alpha)
    drawCircle(color, tall * 0.1f, head, alpha = alpha)
    val folded = shoulder + Offset(-tall * 0.07f, tall * 0.11f)
    val raised = shoulder + Offset(-tall * 0.05f, -tall * 0.16f)
    val knees = hip + Offset(-tall * 0.06f, tall * 0.2f)
    val r = raise.coerceIn(0f, 1f) * (1f - bow.coerceIn(0f, 1f))
    val hand = (folded + (raised - folded) * r).let { it + (knees - it) * bow.coerceIn(0f, 1f) }
    val elbow = shoulder + (hand - shoulder) * 0.5f + Offset(tall * 0.05f * (1f - r), tall * 0.03f)
    drawLine(color, shoulder, elbow, sw * 0.9f, StrokeCap.Round, alpha = alpha)
    drawLine(color, elbow, hand, sw * 0.9f, StrokeCap.Round, alpha = alpha)
}

internal fun DrawScope.w8Palm(base: Offset, tall: Float, trunk: Color, leaf: Color, t: Float, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    val dp = density
    val top = base + Offset(-tall * 0.08f, -tall)
    drawPath(cubicPart(base, base + Offset(0f, -tall * 0.4f), top + Offset(tall * 0.06f, tall * 0.3f), top, 0f, 1f), trunk, alpha = alpha, style = Stroke(max(2f * dp, tall * 0.05f), cap = StrokeCap.Round))
    for (k in 0 until 8) {
        val ang = -PI.toFloat() + k * PI.toFloat() / 7f + 0.05f * sin(t * 0.8f + k)
        val tip = top + Offset(cos(ang) * tall * 0.42f, sin(ang) * tall * 0.2f + tall * 0.22f)
        drawPath(cubicPart(top, top + Offset(cos(ang) * tall * 0.24f, -tall * 0.14f), tip, tip, 0f, 1f), leaf, alpha = alpha, style = Stroke(max(1.6f * dp, tall * 0.035f), cap = StrokeCap.Round))
    }
}

// ═════════════ 1. athar: the footprints, from dawn to now (ahl-sunna) ═════════════

private object AtharC8 {
    val SkyTop = Color(0xFF27305A)
    val SkyMid = Color(0xFFA9708C)
    val SkyLow = Color(0xFFF4BE86)
    val Sun = Color(0xFFFFE3A8)
    val FarHill = Color(0xFF9A7486)
    val Ground = Color(0xFF7C5B47)
    val GroundLow = Color(0xFF4A372F)
    val Road = Color(0xFFB48D68)
    val RoadEdge = Color(0xFF8E6C52)
    val Print = Color(0xFFFFD46E)
    val Grey = Color(0xFF8E8494)
    val Fog = Color(0xFF241F2A)
    val Ink = Color(0xFF241A24)
    val Tent = Color(0xFF3A2A27)
    val Palm = Color(0xFF3E3A2C)
    val City = Color(0xFF6A4A5C)
    val CityLow = Color(0xFF55394A)
    val Tower = Color(0xFF4E4260)
    val Window = Color(0xFFFFD98A)
}

/**
 * A road across the land at dawn. Beats: dawnlight (his ﷺ light rises over the start of the road, his companions
 * around him as plain roundels, and their first footprints), prints (golden footprints run on along the road and a
 * walker sets his feet exactly in them), sidepath (a crooked grey track turns off into fog; he stops, looks, pulls
 * back and keeps to the prints), eras (the skyline rises in three ages, tents, domes, towers, and walkers of each
 * age keep to the same footprints).
 */
internal fun DrawScope.atharScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "dawnlight" to Shot(Offset(w * 0.8f, h * 0.5f), 1.3f),
        "prints" to Shot(Offset(w * 0.68f, h * 0.74f), 1.3f),
        "sidepath" to Shot(Offset(w * 0.44f, h * 0.74f), 1.35f),
        "eras" to Shot(Offset(w * 0.5f, h * 0.56f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.16f) { atharLand(a) }
}

private fun DrawScope.tent8(base: Offset, s: Float, color: Color) {
    val p = Path().apply {
        moveTo(base.x - s, base.y)
        lineTo(base.x - s * 0.72f, base.y - s * 0.32f)
        lineTo(base.x - s * 0.22f, base.y - s * 0.44f)
        lineTo(base.x + s * 0.08f, base.y - s * 0.3f)
        lineTo(base.x + s * 0.5f, base.y - s * 0.46f)
        lineTo(base.x + s, base.y)
        close()
    }
    drawPath(p, color)
    drawRect(Color(0xFF1A1210), topLeft = Offset(base.x - s * 0.18f, base.y - s * 0.24f), size = Size(s * 0.26f, s * 0.24f))
}

private fun DrawScope.atharLand(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val dl = a.b("dawnlight")
    val pr = a.b("prints")
    val sp = a.b("sidepath")
    val er = a.b("eras")
    val horizon = h * 0.62f
    // dawn: the sky warms from the right, where the sun is about to rise; the last stars on the left
    drawRect(Brush.verticalGradient(0f to AtharC8.SkyTop, 0.55f to AtharC8.SkyMid, 1f to AtharC8.SkyLow, endY = horizon), size = Size(w, horizon))
    drawRect(Brush.radialGradient(listOf(AtharC8.Sun.copy(alpha = 0.55f), Color.Transparent), center = Offset(w * 0.92f, horizon), radius = w * 0.6f), size = Size(w, horizon))
    for (k in 0 until 24) {
        val p = Offset(w8Rnd(k, 12.99f) * w * 0.62f, w8Rnd(k, 78.23f) * horizon * 0.5f)
        drawCircle(Color.White, (0.6f + 0.8f * w8Rnd(k, 3.1f)) * dp, p, alpha = 0.2f + 0.3f * (0.5f + 0.5f * sin(t * 1.3f + k)))
    }
    drawCircle(AtharC8.Sun, w * 0.05f, Offset(w * 0.93f, horizon + w * 0.014f))
    hills(AtharC8.FarHill, horizon - h * 0.02f, h * 0.022f, 0.02f, 0.05f, 1.3f)

    // the skyline in three ages: his age on the right, then the domes of the middle centuries, then the towers of now
    val middle = easeOut(seg(er, 0.05f, 0.42f))
    val now = easeOut(seg(er, 0.32f, 0.7f))
    clipRect(0f, 0f, w, horizon + 1f) {
        for ((k, x) in listOf(0.7f, 0.8f, 0.98f).withIndex()) tent8(Offset(w * x, horizon), w * (0.06f - 0.007f * k), AtharC8.Tent)
        for (x in listOf(0.75f, 0.87f)) w8Palm(Offset(w * x, horizon), h * 0.15f, AtharC8.Palm, AtharC8.Palm, t)
        if (middle > 0f) withTransform({ translate(0f, (1f - middle) * h * 0.3f) }) {
            for ((k, x) in listOf(0.42f, 0.5f, 0.58f).withIndex()) {
                val r = w * (0.035f + 0.012f * (1 - k % 2))
                drawRect(AtharC8.CityLow, topLeft = Offset(w * x - r, horizon - h * 0.07f), size = Size(2 * r, h * 0.07f))
                drawArc(AtharC8.City, 180f, 180f, true, topLeft = Offset(w * x - r, horizon - h * 0.07f - r), size = Size(2 * r, 2 * r))
                drawLine(AtharC8.City, Offset(w * x, horizon - h * 0.07f - r), Offset(w * x, horizon - h * 0.07f - r - h * 0.02f), 1.2f * dp)
            }
            for (x in listOf(0.37f, 0.63f)) {
                drawRect(AtharC8.City, topLeft = Offset(w * x - w * 0.009f, horizon - h * 0.2f), size = Size(w * 0.018f, h * 0.2f))
                drawRect(AtharC8.CityLow, topLeft = Offset(w * x - w * 0.014f, horizon - h * 0.16f), size = Size(w * 0.028f, h * 0.012f))
                drawPath(Path().apply { moveTo(w * x - w * 0.01f, horizon - h * 0.2f); lineTo(w * x, horizon - h * 0.24f); lineTo(w * x + w * 0.01f, horizon - h * 0.2f); close() }, AtharC8.City)
            }
        }
        if (now > 0f) withTransform({ translate(0f, (1f - now) * h * 0.42f) }) {
            val towers = listOf(0.04f to 0.3f, 0.1f to 0.38f, 0.17f to 0.26f, 0.23f to 0.34f, 0.29f to 0.22f)
            for ((k, tw) in towers.withIndex()) {
                val (x, th) = tw
                val bw = w * 0.048f
                drawRect(AtharC8.Tower, topLeft = Offset(w * x - bw / 2, horizon - h * th), size = Size(bw, h * th))
                var y = horizon - h * th + h * 0.02f
                var row = 0
                while (y < horizon - h * 0.02f) {
                    for (c in 0 until 3) if (w8Rnd(k * 31 + row * 7 + c, 9.3f) > 0.45f) {
                        drawRect(AtharC8.Window, topLeft = Offset(w * x - bw / 2 + bw * (0.15f + 0.28f * c), y), size = Size(bw * 0.14f, h * 0.012f), alpha = 0.75f)
                    }
                    y += h * 0.03f
                    row++
                }
            }
            // a crane over the newest tower
            drawLine(AtharC8.Tower, Offset(w * 0.33f, horizon), Offset(w * 0.33f, horizon - h * 0.36f), 1.6f * dp)
            drawLine(AtharC8.Tower, Offset(w * 0.27f, horizon - h * 0.35f), Offset(w * 0.4f, horizon - h * 0.35f), 1.6f * dp)
        }
    }

    // the ground and the road, right to left
    drawRect(Brush.verticalGradient(listOf(AtharC8.Ground, AtharC8.GroundLow), startY = horizon, endY = h), topLeft = Offset(0f, horizon), size = Size(w, h - horizon))
    for (k in 0 until 46) {
        val p = Offset(w8Rnd(k, 4.7f) * w, horizon + h * 0.03f + w8Rnd(k, 8.9f) * (h - horizon))
        drawCircle(AtharC8.GroundLow, (0.8f + 1.6f * w8Rnd(k, 2.2f)) * dp, p, alpha = 0.6f)
    }
    val roadY = h * 0.8f
    fun roadAt(x: Float): Float = roadY + h * 0.008f * sin(((w * 1.02f - x) / (w * 1.04f)) * 7f)
    val road = List(41) { i -> val x = mix(w * 1.02f, -w * 0.02f, i / 40f); Offset(x, roadAt(x)) }

    // the crooked side track, into fog
    val junction = Offset(w * 0.5f, roadAt(w * 0.5f))
    val side = w8Smooth(listOf(junction, Offset(w * 0.46f, h * 0.755f), Offset(w * 0.4f, h * 0.735f), Offset(w * 0.35f, h * 0.7f), Offset(w * 0.25f, h * 0.675f)), 8)
    val sideShow = easeOut(seg(sp, 0f, 0.4f))
    val refuse = easeOut(seg(sp, 0.55f, 0.85f))
    if (sideShow > 0f) {
        val end = side.last()
        drawCircle(Brush.radialGradient(listOf(AtharC8.Fog.copy(alpha = (0.85f + 0.15f * refuse) * sideShow), Color.Transparent), end, w * 0.16f), w * 0.16f, end)
        drawPath(w8Poly(w8Head(side, sideShow)), AtharC8.Grey, alpha = 0.6f * (1f - 0.4f * refuse), style = Stroke(h * 0.03f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        for (k in 0 until 9) {
            val u = 0.06f + k * 0.1f
            if (u > sideShow) break
            val p = w8Along(side, u) + Offset(if (k % 2 == 0) -3.5f * dp else 3.5f * dp, 0f)
            drawOval(AtharC8.Grey, topLeft = p - Offset(2.6f * dp, 1.2f * dp), size = Size(5.2f * dp, 2.4f * dp), alpha = 0.75f * (1f - 0.6f * refuse))
        }
        for (k in 0 until 4) {         // the fog curls up
            val c = end + Offset(-w * 0.04f + k * w * 0.028f, -h * 0.025f * k - h * 0.015f * sin(t * 0.9f + k))
            drawCircle(AtharC8.Fog, w * (0.04f - 0.006f * k), c, alpha = 0.6f * sideShow)
        }
    }
    drawPath(w8Band(road) { h * 0.05f }, AtharC8.RoadEdge)
    drawPath(w8Band(road) { h * 0.042f }, AtharC8.Road)
    for (k in 0 until 30) {
        val x = w * (k / 30f) + w * 0.01f
        drawLine(AtharC8.RoadEdge, Offset(x, roadAt(x) + h * 0.03f), Offset(x + w * 0.012f, roadAt(x) + h * 0.03f), 0.8f * dp, alpha = 0.5f)
    }

    // his light over the start of the road, his companions around him
    val lightC = Offset(w * 0.84f, h * 0.33f)
    val lg = easeOut(seg(dl, 0f, 0.35f))
    if (lg > 0f) {
        lightShafts(lightC, PI.toFloat() * 0.62f, 0.9f, h * 0.5f, Color(0xFFFFE6B0), 0.8f * lg, t, 6)
        sirajMunir(lightC, 4.8f * dp, lg, t, nameAt = lightC + Offset(-w * 0.27f, 0f))
    }
    for (k in 0 until 7) {
        val ang = 2f * PI.toFloat() * k / 7f - PI.toFloat() / 2f + 0.25f
        val c = lightC + Offset(cos(ang) * w * 0.1f, sin(ang) * h * 0.115f)
        w8Roundel(c, w * 0.022f, seg(dl, 0.3f + 0.06f * k, 0.5f + 0.06f * k))
    }

    // the walkers: each sets his feet in the footprints (they flash as he does)
    val walkerX = ArrayList<Float>()
    val w1x = when {
        er > 0f -> mix(0.4f, 0.13f, easeInOut(seg(er, 0f, 0.9f)))
        sp > 0f -> when {
            sp < 0.35f -> mix(0.62f, 0.52f, easeInOut(seg(sp, 0f, 0.35f)))
            sp < 0.65f -> 0.52f
            else -> mix(0.52f, 0.4f, easeInOut(seg(sp, 0.65f, 1f)))
        }
        else -> mix(0.84f, 0.62f, easeInOut(seg(pr, 0.05f, 1f)))
    } * w
    val w1Moving = (sp <= 0f && pr > 0.05f && pr < 0.99f) || (er <= 0f && (sp in 0.01f..0.35f || sp in 0.65f..0.99f)) || (er > 0.01f && er < 0.9f)
    val w2x = w * mix(0.74f, 0.44f, easeInOut(seg(er, 0.15f, 1f)))
    val w3x = w * mix(0.95f, 0.7f, easeInOut(seg(er, 0.38f, 1f)))
    if (pr > 0f) walkerX.add(w1x)
    if (er > 0.15f) walkerX.add(w2x)
    if (er > 0.38f) walkerX.add(w3x)

    val n = 28
    val shownPrints = if (pr > 0f) 3 + ((n - 3) * easeOut(seg(pr, 0f, 0.85f))).toInt() else (4f * seg(dl, 0.6f, 1f)).toInt()
    for (k in 0 until min(n, shownPrints)) {
        val x = w * (0.84f - 0.0295f * k)
        val p = Offset(x, roadAt(x) + if (k % 2 == 0) -2.2f * dp else 2.2f * dp)
        var flash = 0f
        for (wx in walkerX) flash = max(flash, 1f - (kotlin.math.abs(wx - x) / (w * 0.03f)).coerceIn(0f, 1f))
        softGlow(p, (5f + 4f * flash) * dp, AtharC8.Print, 0.35f + 0.5f * flash)
        drawOval(AtharC8.Print, topLeft = p - Offset(2.7f * dp, 1.25f * dp), size = Size(5.4f * dp, 2.5f * dp), alpha = 0.9f)
    }
    val tall = h * 0.17f
    fun walker(x: Float, appear: Float, moving: Boolean, phase: Float, lean: Float = 0f, arms: Float = 0f, kit: Int = 0) {
        if (appear <= 0f) return
        val foot = Offset(x, roadAt(x))
        drawOval(Color.Black, topLeft = foot - Offset(tall * 0.2f, tall * 0.03f), size = Size(tall * 0.4f, tall * 0.06f), alpha = 0.22f * appear)
        figure(foot, tall, AtharC8.Ink, alpha = appear, walk = if (moving) t * 6f + phase else 0f, lean = lean, arms = arms)
        if (kit == 1) drawLine(AtharC8.Ink, foot + Offset(-tall * 0.2f, 0f), foot + Offset(-tall * 0.14f, -tall * 0.95f), 1.6f * dp, StrokeCap.Round, alpha = appear)
        if (kit == 2) drawRoundRect(AtharC8.Ink, topLeft = foot + Offset(tall * 0.03f, -tall * 0.78f), size = Size(tall * 0.13f, tall * 0.2f), cornerRadius = CornerRadius(2f * dp), alpha = appear)
    }
    val glance = bump(seg(sp, 0.35f, 0.65f))
    walker(w1x, seg(pr, 0f, 0.15f), w1Moving, 0f, lean = -0.12f * glance, arms = -0.6f * glance)
    walker(w2x, seg(er, 0.15f, 0.3f), er in 0.16f..0.99f, 1.7f, kit = 1)
    walker(w3x, seg(er, 0.38f, 0.52f), er in 0.39f..0.99f, 3.1f, kit = 2)
}

// ═════════════ 2. delta: one river, then many channels (iftiraq) ═════════════

private object DeltaC8 {
    val Land = Color(0xFFCDB98E)
    val LandDark = Color(0xFFA9956C)
    val Contour = Color(0xFF8E7A55)
    val Scrub = Color(0xFF6E8B4E)
    val Water = Color(0xFF5FA8CF)
    val WaterLight = Color(0xFFCDEBF6)
    val Mud = Color(0xFF7A5A36)
    val Bank = Color(0xFF6B5838)
    val Stone = Color(0xFF6A665E)
    val Scroll = Color(0xFFEDE0C0)
    val ScrollEdge = Color(0xFFB59A68)
    val Smoke = Color(0xFF3A2E2E)
    val Ember = Color(0xFFE8683A)
    val Garden = Color(0xFF3F7D47)
    val GardenLight = Color(0xFF7DB36A)
    val Gold = Color(0xFFF4C85A)
    val Back = Color(0xE62A2418)
}

/**
 * A river seen from above. Beats: oneriver (from his ﷺ light at the source one wide river runs down; stones thrown
 * in from the banks splash and sink, and it flows on whole), fork (a thin channel slips away; banks are built up
 * along the river and it stays wide), fray (a scroll pours muddy water in from the side and below it the river
 * frays into many channels), onekept (the channels run into smoke and fire; one stays clear to a garden).
 */
internal fun DrawScope.deltaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "oneriver" to Shot(Offset(w * 0.64f, h * 0.34f), 1.2f),
        "fork" to Shot(Offset(w * 0.58f, h * 0.56f), 1.3f),
        "fray" to Shot(Offset(w * 0.36f, h * 0.64f), 1.15f),
        "onekept" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { deltaLand(a) }
}

private fun DrawScope.river8(pts: List<Offset>, half: Float, p: Float, color: Color, alpha: Float, t: Float, speed: Float = 0.07f) {
    if (p <= 0f || alpha <= 0.01f || pts.size < 2) return
    val part = w8Head(pts, p)
    if (part.size < 2) return
    drawPath(w8Band(part) { half * 1.25f }, DeltaC8.Bank, alpha = 0.35f * alpha)
    drawPath(w8Band(part) { half }, color, alpha = alpha)
    // the current: short glints carried downstream
    for (k in 0 until 14) {
        val u = fract(k / 14f + t * speed)
        if (u > p) continue
        val q = w8Along(pts, u)
        val q2 = w8Along(pts, min(p, u + 0.02f))
        drawLine(DeltaC8.WaterLight, q, q2, 1.2f * density, StrokeCap.Round, alpha = 0.6f * alpha)
    }
}

private fun DrawScope.deltaLand(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ob = a.b("oneriver")
    val fb = a.b("fork")
    val rb = a.b("fray")
    val kb = a.b("onekept")
    drawRect(Brush.linearGradient(listOf(DeltaC8.Land, DeltaC8.LandDark), start = Offset(w, 0f), end = Offset(0f, h)))
    // contour lines of the low hills, and scrub
    for (k in 0 until 4) {
        val c = Offset(w * (0.16f + 0.22f * k), h * (0.22f + 0.2f * ((k * 7) % 3)))
        for (r in 1..3) drawOval(DeltaC8.Contour, topLeft = c - Offset(w * 0.035f * r, h * 0.03f * r), size = Size(w * 0.07f * r, h * 0.06f * r), alpha = 0.22f, style = Stroke(0.8f * dp))
    }
    for (k in 0 until 70) {
        val p = Offset(w8Rnd(k, 5.31f) * w, w8Rnd(k, 9.77f) * h)
        drawCircle(DeltaC8.Scrub, (1.2f + 1.6f * w8Rnd(k, 2.7f)) * dp, p, alpha = 0.45f)
    }
    // a compass rose, as on a map
    val cr = Offset(w * 0.08f, h * 0.12f)
    drawCircle(DeltaC8.Contour, w * 0.03f, cr, alpha = 0.5f, style = Stroke(0.8f * dp))
    drawPath(Path().apply { moveTo(cr.x, cr.y - w * 0.04f); lineTo(cr.x + w * 0.008f, cr.y); lineTo(cr.x, cr.y + w * 0.04f); lineTo(cr.x - w * 0.008f, cr.y); close() }, DeltaC8.Contour, alpha = 0.6f)

    val main = w8Smooth(listOf(Offset(w * 0.9f, -h * 0.03f), Offset(w * 0.8f, h * 0.12f), Offset(w * 0.66f, h * 0.28f), Offset(w * 0.56f, h * 0.44f), Offset(w * 0.47f, h * 0.58f), Offset(w * 0.42f, h * 0.66f)), 10)
    val lower = w8Smooth(listOf(Offset(w * 0.42f, h * 0.66f), Offset(w * 0.36f, h * 0.73f), Offset(w * 0.28f, h * 0.79f), Offset(w * 0.2f, h * 0.84f)), 10)
    val flow = easeInOut(seg(ob, 0f, 0.45f))
    val mudIn = easeOut(seg(rb, 0f, 0.35f))
    val mudMix = easeOut(seg(rb, 0.25f, 0.6f))
    val fan = easeOut(seg(rb, 0.35f, 0.9f))
    val fire = easeOut(seg(kb, 0f, 0.45f))
    val keep = easeOut(seg(kb, 0.15f, 0.6f))
    val murky = lerpColor(DeltaC8.Water, DeltaC8.Mud, 0.55f * mudMix)

    // fire and smoke where the channels end, and the garden where the one ends
    if (fire > 0f) {
        val base = h * 0.95f
        drawRect(Brush.verticalGradient(listOf(Color.Transparent, DeltaC8.Smoke.copy(alpha = 0.85f * fire)), startY = h * 0.8f, endY = h), topLeft = Offset(w * 0.3f, h * 0.8f), size = Size(w * 0.7f, h * 0.2f))
        for (k in 0 until 11) {
            val x = w * (0.32f + 0.065f * k)
            val c = Offset(x, h * (0.88f - 0.02f * sin(t * 1.3f + k)))
            drawCircle(Brush.radialGradient(listOf(DeltaC8.Smoke.copy(alpha = 0.8f * fire), Color.Transparent), c, w * 0.08f), w * 0.08f, c)
            val fl = h * 0.09f * (0.7f + 0.3f * sin(t * 7f + k * 1.9f)) * fire
            drawPath(Path().apply { moveTo(x - w * 0.022f, base); quadraticBezierTo(x - w * 0.01f, base - fl * 0.6f, x, base - fl); quadraticBezierTo(x + w * 0.01f, base - fl * 0.6f, x + w * 0.022f, base); close() }, DeltaC8.Ember, alpha = 0.9f * fire)
            drawPath(Path().apply { moveTo(x - w * 0.01f, base); quadraticBezierTo(x - w * 0.004f, base - fl * 0.35f, x, base - fl * 0.55f); quadraticBezierTo(x + w * 0.004f, base - fl * 0.35f, x + w * 0.01f, base); close() }, DeltaC8.Gold, alpha = 0.8f * fire)
        }
    }
    if (keep > 0f) {
        val g = Offset(w * 0.17f, h * 0.89f)
        softGlow(g, w * 0.16f * keep, DeltaC8.Gold, 0.35f * keep)
        drawOval(DeltaC8.Garden, topLeft = g - Offset(w * 0.12f * keep, h * 0.08f * keep), size = Size(w * 0.24f * keep, h * 0.16f * keep))
        for (k in 0 until 14) {          // trees seen from above
            val c = g + Offset((w8Rnd(k, 3.3f) - 0.5f) * w * 0.19f * keep, (w8Rnd(k, 7.1f) - 0.5f) * h * 0.12f * keep)
            drawCircle(Color(0xFF2C5E36), (5f + 2.5f * w8Rnd(k, 1.9f)) * dp * keep, c + Offset(1.5f * dp, 1.5f * dp))
            drawCircle(DeltaC8.GardenLight, (4.5f + 2.5f * w8Rnd(k, 1.9f)) * dp * keep, c)
        }
    }

    // the river from the source
    river8(main, w * 0.028f, seg(flow, 0f, 0.72f), DeltaC8.Water, 1f, t)
    val lowerColor = lerpColor(murky, DeltaC8.Water, keep)
    river8(lower, w * mix(0.028f, 0.016f, fan), seg(flow, 0.72f, 1f), lowerColor, 1f, t)
    if (keep > 0f) for (k in 0 until 4) {           // light runs down the one that stays clear
        val q = w8Along(lower, fract(t * 0.22f + k * 0.25f))
        softGlow(q, 6f * dp, DeltaC8.Gold, 0.8f * keep)
    }
    // stones thrown in to split it: they splash and sink, and it flows on
    val throws = listOf(Triple(Offset(w * 0.3f, h * 0.3f), Offset(w * 0.62f, h * 0.36f), 0.5f), Triple(Offset(w * 0.86f, h * 0.48f), Offset(w * 0.55f, h * 0.47f), 0.62f), Triple(Offset(w * 0.3f, h * 0.56f), Offset(w * 0.48f, h * 0.57f), 0.74f))
    for ((from, to, st) in throws) {
        val f = seg(ob, st, st + 0.1f)
        if (f <= 0f) continue
        if (f < 1f) drawCircle(DeltaC8.Stone, 3.2f * dp, from + (to - from) * f + Offset(0f, -h * 0.12f * bump(f)))
        val s = seg(ob, st + 0.1f, st + 0.32f)
        for (r in 0 until 2) {
            val q = seg(s, r * 0.25f, 1f)
            if (q > 0f && q < 1f) drawCircle(DeltaC8.WaterLight, (3f + 16f * q) * dp, to, alpha = 0.8f * (1f - q), style = Stroke(1.3f * dp))
        }
    }
    // fork: a thin channel slips away; the banks are built up and the river stays whole
    val fk = easeOut(seg(fb, 0f, 0.5f))
    if (fk > 0f) {
        val thin = w8Smooth(listOf(Offset(w * 0.5f, h * 0.53f), Offset(w * 0.6f, h * 0.6f), Offset(w * 0.73f, h * 0.65f), Offset(w * 0.86f, h * 0.75f)), 8)
        river8(thin, w * 0.008f, fk, lerpColor(DeltaC8.Water, DeltaC8.Mud, 0.3f), 1f, t, 0.05f)
        val end = thin.last()
        if (fk >= 1f) drawOval(lerpColor(DeltaC8.Water, DeltaC8.Mud, 0.45f), topLeft = end - Offset(w * 0.025f, h * 0.02f), size = Size(w * 0.05f, h * 0.04f), alpha = 0.8f)
        tagAt(a.tag("fork"), Offset(w * 0.82f, h * 0.36f), seg(fb, 0.5f, 1f), back = DeltaC8.Back, ink = ArtInk.Gold)
    }
    val banks = easeOut(seg(fb, 0.35f, 0.9f))
    if (banks > 0f) for (k in 0 until 12) {
        val u = 0.3f + 0.05f * k
        if ((k + 1) / 12f > banks) break
        val q = w8Along(main, u)
        val q2 = w8Along(main, u + 0.02f)
        val d = q2 - q
        val len = max(0.001f, d.getDistance())
        val nrm = Offset(-d.y / len, d.x / len) * (w * 0.036f)
        for (sd in listOf(1f, -1f)) drawLine(DeltaC8.Bank, q + nrm * sd, q2 + nrm * sd, 3f * dp, StrokeCap.Round)
    }
    // fray: a scroll of foreign learning pours muddy water in, and the river frays into channels
    if (mudIn > 0f) {
        val sc = Offset(w * 0.17f, h * 0.37f)
        withTransform({ rotate(-18f, sc) }) {
            drawRect(DeltaC8.Scroll, topLeft = sc - Offset(w * 0.07f, h * 0.05f), size = Size(w * 0.14f, h * 0.1f), alpha = mudIn)
            for (sd in listOf(-1f, 1f)) drawRoundRect(DeltaC8.ScrollEdge, topLeft = Offset(sc.x + sd * w * 0.07f - w * 0.008f, sc.y - h * 0.065f), size = Size(w * 0.016f, h * 0.13f), cornerRadius = CornerRadius(4f * dp), alpha = mudIn)
            for (r in 0 until 4) drawLine(DeltaC8.Mud, Offset(sc.x - w * 0.05f, sc.y - h * 0.03f + r * h * 0.02f), Offset(sc.x + w * 0.05f, sc.y - h * 0.03f + r * h * 0.02f), 0.9f * dp, alpha = 0.55f * mudIn)
        }
        val mud = w8Smooth(listOf(Offset(w * 0.2f, h * 0.42f), Offset(w * 0.27f, h * 0.5f), Offset(w * 0.36f, h * 0.57f), Offset(w * 0.45f, h * 0.6f)), 8)
        river8(mud, w * 0.012f, mudIn, DeltaC8.Mud, 1f, t, 0.09f)
    }
    if (fan > 0f) {
        val s = Offset(w * 0.42f, h * 0.66f)
        val ends = listOf(0.36f, 0.48f, 0.6f, 0.72f, 0.84f, 0.95f)
        for ((k, ex) in ends.withIndex()) {
            val e = Offset(w * ex, h * 0.9f)
            val wig = Offset((w8Rnd(k, 4.4f) - 0.5f) * w * 0.08f, 0f)
            val ch = w8Smooth(listOf(s, s + (e - s) * 0.35f + wig, s + (e - s) * 0.7f - wig, e), 8)
            val c = lerpColor(murky, DeltaC8.Smoke, 0.5f * fire)
            river8(ch, w * 0.009f, fan, c, 1f - 0.5f * fire, t, 0.06f)
        }
    }
    // his light at the source
    val src = Offset(w * 0.87f, h * 0.05f)
    sirajMunir(src, 4.4f * dp, easeOut(seg(ob, 0f, 0.25f)), t)
}

// ═════════════ 3. loom: 72 threads on four knots (usul-bida) ═════════════

private object LoomC8 {
    val Wall = Color(0xFFE8D8BA)
    val WallLow = Color(0xFFCBB590)
    val Wood = Color(0xFF7A4E2C)
    val WoodDark = Color(0xFF4E301A)
    val WoodLight = Color(0xFFA36F43)
    val Floor = Color(0xFF6B4A30)
    val Threads = listOf(Color(0xFF9E3B32), Color(0xFF3F6E8C), Color(0xFF6E7F3A), Color(0xFF7E4E92))
    val Glass = Color(0xFFDDEEF2)
    val Light = Color(0xFFFFCF5A)
    val Leak = Color(0xFF5A3A2A)
    val Up = Color(0xFF2E7D4F)
    val Down = Color(0xFFB8342A)
    val Back = Color(0xE63A2A1A)
}

/**
 * A loom in a workshop. Beats: knots72 (72 threads are strung one after another from the top beam, gathering on
 * four knots, which are then named), caliphs4 (four name roundels set themselves in a row in order, and the third
 * knot comes undone: its threads drop away), ebb (a glass of light rises with deeds, falls, rises again, and the
 * second knot comes undone too).
 */
internal fun DrawScope.loomScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "knots72" to Shot(Offset(w * 0.5f, h * 0.52f), 1.04f),
        "caliphs4" to Shot(Offset(w * 0.4f, h * 0.56f), 1.3f),
        "ebb" to Shot(Offset(w * 0.6f, h * 0.48f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.18f) { loomRoom(a) }
}

private fun DrawScope.loomRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val kb = a.b("knots72")
    val cb = a.b("caliphs4")
    val eb = a.b("ebb")
    drawRect(Brush.verticalGradient(listOf(LoomC8.Wall, LoomC8.WallLow), endY = h * 0.9f))
    lightShafts(Offset(-w * 0.05f, -h * 0.1f), PI.toFloat() * 0.25f, 0.5f, h * 1.2f, Color(0xFFFFF1C8), 0.7f, t, 5)
    drawRect(LoomC8.Floor, topLeft = Offset(0f, h * 0.9f), size = Size(w, h * 0.1f))
    for (k in 0 until 8) drawLine(LoomC8.WoodDark, Offset(w * k / 8f, h * 0.9f), Offset(w * k / 8f - w * 0.03f, h), 0.8f * dp, alpha = 0.4f)
    // the frame
    val topY = h * 0.14f
    val barY = h * 0.7f
    for (x in listOf(0.05f, 0.95f)) {
        drawRect(LoomC8.WoodDark, topLeft = Offset(w * x - w * 0.014f, h * 0.08f), size = Size(w * 0.028f, h * 0.84f))
        drawRect(LoomC8.WoodLight, topLeft = Offset(w * x - w * 0.014f, h * 0.08f), size = Size(w * 0.006f, h * 0.84f), alpha = 0.6f)
    }
    drawRoundRect(LoomC8.Wood, topLeft = Offset(w * 0.03f, topY - h * 0.03f), size = Size(w * 0.94f, h * 0.045f), cornerRadius = CornerRadius(4f * dp))
    drawRect(LoomC8.WoodLight, topLeft = Offset(w * 0.03f, topY - h * 0.03f), size = Size(w * 0.94f, h * 0.008f), alpha = 0.6f)
    drawRoundRect(LoomC8.WoodDark, topLeft = Offset(w * 0.03f, barY + h * 0.03f), size = Size(w * 0.94f, h * 0.03f), cornerRadius = CornerRadius(3f * dp))

    fun knotAt(k: Int) = Offset(w * (0.8f - 0.2f * k), barY)
    // each knot comes undone: 2 (the Shi'a) after the caliphs, 1 (the Murji'a) after the ebb
    fun undone(k: Int): Float = when (k) {
        2 -> w8EaseIn(seg(cb, 0.72f, 0.96f))
        1 -> w8EaseIn(seg(eb, 0.72f, 0.96f))
        else -> 0f
    }
    val strung = 72f * easeOut(seg(kb, 0f, 0.42f))
    for (j in 0 until 72) {
        if (j >= strung) break
        val k = j / 18
        val u = undone(k)
        if (u >= 1f) continue
        val top = Offset(w * (0.92f - 0.84f * (j + 0.5f) / 72f), topY)
        val knot = knotAt(k)
        val drop = Offset(sin(j * 1.7f + t * 3f) * w * 0.02f * u, h * 0.75f * u)
        val tp = top + drop
        val kp = knot + Offset(0f, h * 0.3f * u)
        val ctrl = Offset((tp.x + kp.x) / 2f, (tp.y + kp.y) / 2f + h * 0.035f)
        val col = LoomC8.Threads[k]
        drawPath(Path().apply { moveTo(tp.x, tp.y); quadraticBezierTo(ctrl.x, ctrl.y, kp.x, kp.y) }, lerpColor(col, Color.White, 0.12f * (j % 3)), alpha = (1f - u) * (if (j.toFloat() > strung - 1f) strung - j else 1f), style = Stroke(1f * dp))
    }
    // the knots, and their names under them
    for (k in 0 until 4) {
        val got = seg(strung, k * 18f, k * 18f + 6f)
        val u = undone(k)
        if (got <= 0f || u >= 1f) continue
        val c = knotAt(k) + Offset(0f, h * 0.3f * u)
        val r = w * 0.032f * (0.5f + 0.5f * got) * (1f - 0.6f * u)
        drawCircle(Color.Black, r * 1.05f, c + Offset(0f, r * 0.2f), alpha = 0.25f * (1f - u))
        drawCircle(LoomC8.Threads[k], r, c, alpha = 1f - u)
        for (m in 0 until 5) drawArc(lerpColor(LoomC8.Threads[k], Color.Black, 0.35f), 200f + m * 31f, 140f, false, topLeft = c - Offset(r * 0.8f, r * 0.8f), size = Size(r * 1.6f, r * 1.6f), alpha = 0.7f * (1f - u), style = Stroke(0.9f * dp))
        tagAt(a.tag("knots72", 0, k), Offset(c.x, h * 0.82f), seg(kb, 0.45f + 0.12f * k, 0.58f + 0.12f * k) * (1f - u), back = LoomC8.Back, ink = ArtInk.Ivory)
    }
    // the count, once all are strung
    tagAt(a.tag("knots72", 0, 4), Offset(w * 0.5f, h * 0.06f), seg(kb, 0.38f, 0.5f), back = LoomC8.Back, ink = ArtInk.Gold)

    // caliphs4: their names in roundels, set in a row in order
    if (cb > 0f) for (m in 0 until 4) {
        val g = easeOut(seg(cb, 0.04f + 0.14f * m, 0.24f + 0.14f * m))
        if (g <= 0f) continue
        val target = Offset(w * (0.4f + (1.5f - m) * 0.09f), h * 0.47f)
        val c = Offset(mix(w * 1.12f, target.x, g), target.y - h * 0.08f * bump(g))
        nameRoundel(c, w * 0.043f, a.big("caliphs4", 0, m), Roundel.Fills[m], Roundel.Rim, g)
    }
    // ebb: a glass of light that rises and falls with deeds
    val show = seg(eb, 0f, 0.08f)
    if (show > 0f) {
        val gx = w * 0.67f
        val top = h * 0.24f
        val bottom = h * 0.6f
        val gw = w * 0.07f
        val lv = when {
            eb < 0.3f -> mix(0.12f, 0.85f, easeInOut(seg(eb, 0.02f, 0.3f)))
            eb < 0.5f -> mix(0.85f, 0.32f, easeInOut(seg(eb, 0.3f, 0.5f)))
            else -> mix(0.32f, 0.7f, easeInOut(seg(eb, 0.5f, 0.68f)))
        }
        val al = show * (1f - 0.3f * seg(eb, 0.9f, 1f))
        val surf = bottom - (bottom - top) * lv
        softGlow(Offset(gx, surf), gw * 0.9f, LoomC8.Light, 0.5f * al)
        drawRect(LoomC8.Light, topLeft = Offset(gx - gw / 2, surf), size = Size(gw, bottom - surf), alpha = 0.85f * al)
        drawLine(Color.White, Offset(gx - gw / 2, surf), Offset(gx + gw / 2, surf), 1.2f * dp, alpha = 0.8f * al)
        drawRoundRect(LoomC8.Glass, topLeft = Offset(gx - gw / 2, top), size = Size(gw, bottom - top), cornerRadius = CornerRadius(4f * dp), alpha = 0.9f * al, style = Stroke(1.6f * dp))
        drawLine(Color.White, Offset(gx - gw * 0.3f, top + h * 0.02f), Offset(gx - gw * 0.3f, bottom - h * 0.02f), 1.2f * dp, alpha = 0.5f * al)
        val rising = (eb in 0.02f..0.3f) || (eb in 0.5f..0.68f)
        if (rising) for (k in 0 until 3) {
            val f = fract(t * 1.4f + k / 3f)
            drawCircle(LoomC8.Light, 2.4f * dp, Offset(gx + (k - 1) * gw * 0.2f, mix(top - h * 0.1f, surf, f)), alpha = al)
        }
        val arrowP = Offset(gx + gw * 0.95f, (surf + bottom) / 2f)
        if (rising) arrowHead(arrowP + Offset(0f, -h * 0.03f), Offset(0f, -1f), 7f * dp, LoomC8.Up, 2f * dp, al)
        if (eb in 0.3f..0.5f) {
            arrowHead(arrowP + Offset(0f, h * 0.03f), Offset(0f, 1f), 7f * dp, LoomC8.Down, 2f * dp, al)
            for (k in 0 until 3) {      // a leak: deeds lost
                val f = fract(t * 1.8f + k / 3f)
                drawCircle(LoomC8.Leak, 2f * dp, Offset(gx + gw * 0.1f, bottom + f * h * 0.08f), alpha = al * (1f - f))
            }
        }
    }
}

// ═════════════ 4. sabil: a public fountain of pure water (khasais-aqida) ═════════════

private object SabilC8 {
    val Wall = Color(0xFFEDE1C7)
    val WallShade = Color(0xFFD2C09C)
    val Tiles = listOf(Color(0xFF1F6E6A), Color(0xFF2F4E8C), Color(0xFFF2EAD6))
    val TileGold = Color(0xFFC99A3C)
    val Recess = Color(0xFF9C8A6A)
    val Stone = Color(0xFFD8CBB0)
    val StoneDark = Color(0xFFA99877)
    val Bronze = Color(0xFFA8742E)
    val Water = Color(0xFF7FC4E0)
    val WaterDeep = Color(0xFF3E8FB5)
    val Mud = Color(0xFF6A5638)
    val Cloud = Color(0xFF4A4550)
    val Awning = Color(0xFF7A4A2A)
    val AwningTile = Color(0xFF2E6B4E)
    val Chain = Color(0xFFE8C26A)
    val Ink = Color(0xFF2B2420)
    val Plaque = Color(0xF2F6EFDD)
    val Floor = Color(0xFFBFAE8A)
    val Back = Color(0xE6302820)
}

/**
 * A tiled public fountain in a wall, under a little roof. Beats: spout (clear water runs from three spouts, each
 * under its plaque: the Book, the Sunna, the consensus; a dark cloud's muddy rain runs off the roof and away, never
 * into the basin), pipe (the wall opens and the water's pipe is a chain, link by link, up to his ﷺ light), drink
 * (one man with a book and one without come and drink from the same clear, still basin).
 */
internal fun DrawScope.sabilScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "spout" to Shot(Offset(w * 0.5f, h * 0.47f), 1.1f),
        "pipe" to Shot(Offset(w * 0.5f, h * 0.3f), 1.15f),
        "drink" to Shot(Offset(w * 0.5f, h * 0.7f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.16f) { sabilWall(a) }
}

private fun DrawScope.sabilWall(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sb = a.b("spout")
    val pb = a.b("pipe")
    val db = a.b("drink")
    val floorY = h * 0.9f
    drawRect(Brush.verticalGradient(listOf(SabilC8.Wall, SabilC8.WallShade), endY = floorY))
    drawRect(SabilC8.Floor, topLeft = Offset(0f, floorY), size = Size(w, h - floorY))
    for (k in 0 until 10) drawLine(SabilC8.StoneDark, Offset(w * k / 10f, floorY), Offset(w * k / 10f, h), 0.8f * dp, alpha = 0.4f)
    // the zellige panel around the niche
    val pl = w * 0.25f
    val pr = w * 0.75f
    val pt = h * 0.2f
    val pbm = floorY
    drawRect(SabilC8.Tiles[2], topLeft = Offset(pl, pt), size = Size(pr - pl, pbm - pt))
    clipRect(pl, pt, pr, pbm) {
        val s = w * 0.046f
        var i = 0
        var y = pt + s / 2
        while (y < pbm + s) {
            var x = pl + s / 2 + (if (i % 2 == 0) 0f else s / 2)
            var j = 0
            while (x < pr + s) {
                drawPath(starPath(Offset(x, y), s * 0.42f), SabilC8.Tiles[(i + j) % 2])
                drawPath(starPath(Offset(x, y), s * 0.18f), SabilC8.TileGold)
                x += s
                j++
            }
            y += s * 0.86f
            i++
        }
    }
    drawRect(SabilC8.TileGold, topLeft = Offset(pl, pt), size = Size(pr - pl, pbm - pt), style = Stroke(2f * dp))
    // the niche
    val cx = w * 0.5f
    val niche = horseshoe(cx, h * 0.42f, w * 0.15f, w * 0.13f, pbm)
    drawPath(niche, SabilC8.Recess)
    drawPath(niche, Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.25f), Color.Transparent), startY = h * 0.27f, endY = h * 0.6f))
    drawPath(niche, SabilC8.TileGold, style = Stroke(2.2f * dp))
    drawPath(horseshoe(cx, h * 0.42f, w * 0.162f, w * 0.142f, pbm), SabilC8.Stone, style = Stroke(2.4f * dp))
    // pipe: the wall opens and the chain goes up to his light
    val open = easeOut(seg(pb, 0f, 0.2f))
    if (open > 0f) {
        drawRect(Color(0xFF5A4A36), topLeft = Offset(cx - w * 0.018f, h * 0.07f), size = Size(w * 0.036f, h * 0.48f), alpha = 0.75f * open)
        drawRect(SabilC8.StoneDark, topLeft = Offset(cx - w * 0.018f, h * 0.07f), size = Size(w * 0.036f, h * 0.48f), alpha = open, style = Stroke(1.2f * dp))
    }
    // the roof over it
    val roof = Path().apply {
        moveTo(w * 0.2f, h * 0.2f); lineTo(w * 0.26f, h * 0.1f); lineTo(w * 0.74f, h * 0.1f); lineTo(w * 0.8f, h * 0.2f); close()
    }
    drawPath(roof, SabilC8.AwningTile)
    for (k in 0 until 14) {
        val x = w * (0.22f + 0.04f * k)
        drawLine(Color(0xFF1E4A36), Offset(x + w * 0.02f, h * 0.105f), Offset(x, h * 0.198f), 1.4f * dp)
    }
    drawRect(SabilC8.Awning, topLeft = Offset(w * 0.19f, h * 0.195f), size = Size(w * 0.62f, h * 0.018f))
    for (k in 0 until 7) drawRect(SabilC8.Awning, topLeft = Offset(w * (0.22f + 0.095f * k), h * 0.213f), size = Size(w * 0.012f, h * 0.02f))

    // the chain, link by link, and his light at the top
    if (pb > 0f) {
        val links = 12
        val shown = links * easeOut(seg(pb, 0.05f, 0.7f))
        val y0 = h * 0.53f
        val y1 = h * 0.09f
        for (k in 0 until links) {
            if (k >= shown) break
            val y = mix(y0, y1, k / (links - 1f))
            val lw = if (k % 2 == 0) w * 0.016f else w * 0.006f
            drawOval(SabilC8.Chain, topLeft = Offset(cx - lw, y - h * 0.022f), size = Size(lw * 2, h * 0.044f), style = Stroke(1.8f * dp))
        }
        val glintY = mix(y0, y1, fract(t * 0.6f))
        if (pb > 0.7f) glint(Offset(cx, glintY), 7f * dp, Color.White, 0.8f)
        sirajMunir(Offset(cx, h * 0.07f), 4.6f * dp, easeOut(seg(pb, 0.55f, 0.85f)), t, nameAt = Offset(cx + w * 0.15f, h * 0.06f))
    }

    // the basin
    val bl = w * 0.3f
    val br = w * 0.7f
    val bt = h * 0.72f
    val bb = h * 0.86f
    val fill = easeOut(seg(sb, 0.15f, 0.7f))
    val waterTop = mix(bb - h * 0.01f, bt + h * 0.022f, fill)
    if (fill > 0f) {
        drawRect(Brush.verticalGradient(listOf(SabilC8.Water, SabilC8.WaterDeep), startY = waterTop, endY = bb), topLeft = Offset(bl + w * 0.01f, waterTop), size = Size(br - bl - w * 0.02f, bb - waterTop))
        val calm = 1f - easeOut(seg(db, 0.6f, 1f))
        for (k in 0 until 3) {
            val x = mix(bl, br, 0.25f + 0.25f * k)
            val r = (4f + 8f * fract(t * 0.8f + k * 0.33f)) * dp
            drawOval(Color.White, topLeft = Offset(x - r * 2f, waterTop + h * 0.006f - r * 0.3f), size = Size(r * 4f, r * 0.6f), alpha = 0.5f * calm * (1f - fract(t * 0.8f + k * 0.33f)), style = Stroke(0.9f * dp))
        }
        if (db > 0.6f) glint(Offset(mix(bl, br, 0.62f), waterTop + h * 0.01f), 8f * dp, Color.White, seg(db, 0.6f, 0.8f))
    }
    drawRoundRect(SabilC8.Stone, topLeft = Offset(bl, bt), size = Size(br - bl, bb - bt), cornerRadius = CornerRadius(5f * dp), style = Stroke(w * 0.012f))
    drawRect(SabilC8.StoneDark, topLeft = Offset(bl, bb - h * 0.01f), size = Size(br - bl, h * 0.04f))
    drawRect(SabilC8.Stone, topLeft = Offset(bl - w * 0.015f, bt - h * 0.012f), size = Size(br - bl + w * 0.03f, h * 0.016f))

    // the three spouts, each under its plaque, and the water from them
    for (k in 0 until 3) {
        val x = w * (0.65f - 0.15f * k)
        val mouth = Offset(x, h * 0.57f)
        drawRoundRect(SabilC8.Bronze, topLeft = Offset(x - w * 0.012f, h * 0.54f), size = Size(w * 0.024f, h * 0.035f), cornerRadius = CornerRadius(3f * dp))
        val run = easeOut(seg(sb, 0.04f + 0.1f * k, 0.18f + 0.1f * k))
        if (run > 0f) {
            val end = Offset(x - w * 0.006f, mix(mouth.y, waterTop, run))
            drawLine(SabilC8.Water, mouth, end, 2.6f * dp, StrokeCap.Round, alpha = 0.95f)
            drawLine(Color.White, mouth + Offset(-0.6f * dp, 0f), end + Offset(-0.6f * dp, 0f), 0.8f * dp, StrokeCap.Round, alpha = 0.6f)
        }
        val lit = seg(sb, 0.1f + 0.12f * k, 0.3f + 0.12f * k)
        if (lit > 0f) softGlow(Offset(x, h * 0.49f), w * 0.05f, Color(0xFFFFF4D0), 0.6f * lit)
        tagAt(a.tag("spout", 0, k), Offset(x, h * 0.485f), lit, back = SabilC8.Plaque, ink = SabilC8.Ink)
    }

    // the cloud of desires: its muddy rain runs off the roof and away
    val cloud = easeOut(seg(sb, 0.5f, 0.66f))
    if (cloud > 0f) {
        val cc = Offset(w * 0.2f, h * 0.075f)
        for ((k, o) in listOf(Offset(-w * 0.05f, h * 0.01f), Offset(0f, -h * 0.01f), Offset(w * 0.05f, h * 0.012f), Offset(-w * 0.01f, h * 0.025f)).withIndex()) {
            drawCircle(SabilC8.Cloud, w * (0.04f + 0.006f * (k % 2)), cc + o, alpha = 0.92f * cloud)
        }
        for (k in 0 until 12) {
            val ph = fract(t * 0.7f + k / 12f)
            val x0 = w * (0.2f + 0.008f * k)
            val roofY = h * 0.2f - (x0 - w * 0.2f) / (w * 0.06f) * h * 0.1f
            val p = when {
                ph < 0.4f -> Offset(x0, mix(h * 0.1f, roofY.coerceAtLeast(h * 0.1f), ph / 0.4f))
                ph < 0.7f -> Offset(mix(x0, w * 0.2f, (ph - 0.4f) / 0.3f), mix(roofY.coerceAtLeast(h * 0.1f), h * 0.2f, (ph - 0.4f) / 0.3f))
                else -> Offset(w * 0.19f, mix(h * 0.21f, floorY + h * 0.02f, (ph - 0.7f) / 0.3f))
            }
            drawCircle(SabilC8.Mud, 1.8f * dp, p, alpha = 0.9f * cloud)
        }
        drawLine(SabilC8.Mud, Offset(w * 0.19f, floorY + h * 0.03f), Offset(-w * 0.01f, floorY + h * 0.05f), 2.5f * dp, StrokeCap.Round, alpha = 0.7f * cloud)
        tagAt(a.tag("spout", 0, 3), Offset(w * 0.1f, h * 0.15f), seg(sb, 0.62f, 0.8f), back = SabilC8.Back, ink = Color(0xFFD9B98A))
    }

    // drink: a man with a book and a man without drink from the same basin
    if (db > 0f) {
        val tall = h * 0.3f
        val come = easeInOut(seg(db, 0f, 0.42f))
        val sip = bump(fract(seg(db, 0.45f, 1f) * 2f)) * seg(db, 0.45f, 0.5f)
        val bend = easeInOut(seg(db, 0.4f, 0.55f))
        val walking = db in 0.01f..0.42f
        val l = Offset(mix(-w * 0.06f, w * 0.25f, come), floorY)
        withTransform({ scale(-1f, 1f, pivot = l) }) {
            figure(l, tall, SabilC8.Ink, walk = if (walking) t * 6f else 0f, lean = 0.55f * bend, arms = 0.4f + 0.5f * bend + 0.3f * sip)
            drawRoundRect(Color(0xFF7A2E2A), topLeft = l + Offset(tall * 0.02f, -tall * 0.6f), size = Size(tall * 0.14f, tall * 0.18f), cornerRadius = CornerRadius(1.5f * dp))
        }
        val r = Offset(mix(w * 1.06f, w * 0.75f, come), floorY)
        figure(r, tall, SabilC8.Ink, walk = if (walking) t * 6f + 1.3f else 0f, lean = 0.55f * bend, arms = 0.4f + 0.5f * bend + 0.3f * sip)
    }
}

// ═════════════ 5. shore: a small cup before the sea (mahar-muhal) ═════════════

private object ShoreC8 {
    val SkyTop = Color(0xFF1B224E)
    val SkyMid = Color(0xFF6B4B7F)
    val SkyLow = Color(0xFFE99C6E)
    val Sea = Color(0xFF263867)
    val SeaFar = Color(0xFF5E5A8C)
    val Foam = Color(0xFFEDEBF4)
    val Sand = Color(0xFFC9A479)
    val SandDark = Color(0xFF8C6C50)
    val Ink = Color(0xFF1E1628)
    val Mound = Color(0xFF3B3046)
    val Bliss = Color(0xFFBFE6C8)
    val Ember = Color(0xFFFF8A4A)
    val Pool = Color(0xFFD7EEFF)
    val Cup = Color(0xFFD9C08A)
    val Back = Color(0xE6181A30)
}

/**
 * A shore at dusk. Beats: horizon (far on the horizon, larger than anything near, three labelled symbols appear in
 * turn: the grave with a light in it, the hair-thin bridge over embers, the basin ringed with cups like stars),
 * scoop (a man walks to the water with a bowl and scoops the sea; the bowl overflows at once, and he stands with
 * a hand on his head, question marks rising over it), yield (he sets the bowl down and sits on the sand, a hand on
 * his chest; the sea calms to a mirror and the stars come out).
 */
internal fun DrawScope.shoreScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "horizon" to Shot(Offset(w * 0.5f, h * 0.38f), 1.14f),
        "scoop" to Shot(Offset(w * 0.62f, h * 0.76f), 1.5f),
        "yield" to Shot(Offset(w * 0.55f, h * 0.6f), 1.08f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.18f) { shoreView(a) }
}

private fun DrawScope.shoreView(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val hb = a.b("horizon")
    val sb = a.b("scoop")
    val yb = a.b("yield")
    val horizon = h * 0.42f
    val calm = easeOut(seg(yb, 0.2f, 0.8f))
    drawRect(Brush.verticalGradient(0f to ShoreC8.SkyTop, 0.6f to ShoreC8.SkyMid, 1f to ShoreC8.SkyLow, endY = horizon), size = Size(w, horizon))
    for (k in 0 until 40) {
        val p = Offset(w8Rnd(k, 17.3f) * w, w8Rnd(k, 41.9f) * horizon * 0.7f)
        val tw = 0.5f + 0.5f * sin(t * 1.4f + k * 2.1f)
        drawCircle(Color.White, (0.6f + 0.9f * w8Rnd(k, 5.5f)) * dp, p, alpha = (0.15f + 0.55f * calm) * tw)
    }
    drawRect(Brush.radialGradient(listOf(ShoreC8.SkyLow.copy(alpha = 0.6f), Color.Transparent), center = Offset(w * 0.4f, horizon), radius = w * 0.5f), size = Size(w, horizon))
    // the sea
    val shore = h * 0.75f
    drawRect(Brush.verticalGradient(listOf(ShoreC8.SeaFar, ShoreC8.Sea), startY = horizon, endY = shore), topLeft = Offset(0f, horizon), size = Size(w, shore - horizon))
    val rough = (0.5f + 1.1f * easeOut(seg(sb, 0.2f, 0.6f))) * (1f - calm)
    for (k in 0 until 16) {
        val y = mix(horizon + h * 0.01f, shore - h * 0.02f, (k / 15f).let { it * it })
        val len = w * (0.05f + 0.1f * w8Rnd(k, 3.3f))
        val x = fract(w8Rnd(k, 8.1f) + t * 0.02f * (1 + k % 3)) * w
        drawLine(ShoreC8.Foam, Offset(x, y), Offset(x + len, y), 0.9f * dp, alpha = 0.15f + 0.25f * rough * (k / 15f))
    }
    // sand, and the wet line of the waves
    val reach = h * 0.03f * rough * (0.5f + 0.5f * sin(t * 1.6f))
    val waveLine = List(31) { i -> val x = w * i / 30f; Offset(x, shore + reach + h * 0.008f * sin(x / dp * 0.05f + t * 1.5f)) }
    drawRect(Brush.verticalGradient(listOf(ShoreC8.Sand, ShoreC8.SandDark), startY = shore, endY = h), topLeft = Offset(0f, shore - h * 0.01f), size = Size(w, h - shore + h * 0.01f))
    drawPath(Path().apply { moveTo(0f, shore - h * 0.02f); for (p in waveLine) lineTo(p.x, p.y); lineTo(w, shore - h * 0.02f); close() }, ShoreC8.Sea)
    drawPath(w8Poly(waveLine), ShoreC8.Foam, alpha = 0.8f, style = Stroke(1.6f * dp))

    // the three symbols far on the horizon
    val glow = 0.7f + 0.3f * calm
    val grave = seg(hb, 0.48f, 0.62f)
    if (grave > 0f) {
        val c = Offset(w * 0.8f, horizon)
        drawArc(ShoreC8.Mound, 180f, 180f, true, topLeft = c - Offset(w * 0.08f, h * 0.06f), size = Size(w * 0.16f, h * 0.12f), alpha = grave)
        drawPath(horseshoe(c.x, c.y - h * 0.025f, w * 0.018f, w * 0.015f, c.y), ShoreC8.Bliss, alpha = grave * glow)
        softGlow(c + Offset(0f, -h * 0.02f), w * 0.05f, ShoreC8.Bliss, 0.5f * grave * glow)
        tagAt(a.tag("horizon", 0, 0), c + Offset(0f, -h * 0.11f), grave, back = ShoreC8.Back)
    }
    val bridge = seg(hb, 0.72f, 0.86f)
    if (bridge > 0f) {
        val l = Offset(w * 0.36f, horizon - h * 0.01f)
        val r = Offset(w * 0.64f, horizon - h * 0.01f)
        drawOval(Brush.radialGradient(listOf(ShoreC8.Ember.copy(alpha = 0.7f * bridge * glow), Color.Transparent), Offset(w * 0.5f, horizon), w * 0.16f), topLeft = Offset(w * 0.34f, horizon - h * 0.05f), size = Size(w * 0.32f, h * 0.1f))
        drawPath(cubicPart(l, l + Offset(w * 0.08f, -h * 0.07f), r + Offset(-w * 0.08f, -h * 0.07f), r, 0f, bridge), Color.White, alpha = 0.9f, style = Stroke(0.7f * dp))
        tagAt(a.tag("horizon", 0, 1), Offset(w * 0.5f, horizon - h * 0.12f), bridge, back = ShoreC8.Back)
    }
    val basin = seg(hb, 0.88f, 1f)
    if (basin > 0f) {
        val c = Offset(w * 0.2f, horizon - h * 0.005f)
        softGlow(c, w * 0.07f, ShoreC8.Pool, 0.55f * basin * glow)
        drawOval(ShoreC8.Pool, topLeft = c - Offset(w * 0.06f, h * 0.016f), size = Size(w * 0.12f, h * 0.032f), alpha = 0.9f * basin)
        for (k in 0 until 10) {
            val ang = k * PI.toFloat() / 5f + t * 0.1f
            glint(c + Offset(cos(ang) * w * 0.075f, sin(ang) * h * 0.03f - h * 0.01f), 4f * dp, Color.White, 0.7f * basin)
        }
        tagAt(a.tag("horizon", 0, 2), c + Offset(0f, -h * 0.08f), basin, back = ShoreC8.Back)
    }

    // the man and his bowl: he scoops the sea, the bowl overflows at once, and he stands puzzled before the horizon;
    // then he sets the bowl down and sits at peace with what he cannot measure
    val tall = h * 0.26f
    val ink = ShoreC8.Ink
    val sw = max(1.7f * dp, tall * 0.065f)
    val go = easeInOut(seg(sb, 0f, 0.2f))
    val foot = Offset(mix(w * 0.72f, w * 0.62f, go), mix(h * 0.95f, shore + h * 0.06f, go))
    val dip = easeInOut(seg(sb, 0.2f, 0.34f))
    val lifted = easeInOut(seg(sb, 0.4f, 0.54f))
    val puzzled = easeInOut(seg(sb, 0.62f, 0.72f))
    val sit = easeInOut(seg(yb, 0.15f, 0.45f))
    drawOval(Color.Black, topLeft = foot - Offset(tall * 0.2f, tall * 0.03f), size = Size(tall * 0.4f, tall * 0.06f), alpha = 0.2f)
    // where his forward hand is, as the figure draws it
    val lean = 1.15f * dip * (1f - lifted)
    val arms = lifted
    val hip = foot - Offset(0f, tall * 0.45f)
    val sh = hip + Offset(-sin(lean), -cos(lean)) * (tall * 0.31f)
    val armLen = tall * 0.28f
    val hand = if (sb <= 0f) foot + Offset(-tall * 0.11f, -tall * 0.48f) else sh + Offset(-armLen * (0.35f + 0.65f * arms), armLen * (0.85f - 0.75f * arms))
    val side = foot + Offset(tall * 0.16f, -tall * 0.2f)          // the bowl lowered at his side
    val sand = foot + Offset(-tall * 0.34f, tall * 0.02f)          // the bowl set down on the sand
    val bowl = (hand + (side - hand) * puzzled).let { it + (sand - it) * sit }
    if (puzzled < 0.5f) {
        figure(foot, tall, ink, alpha = 1f - sit, walk = if (sb in 0.01f..0.2f) t * 6f else 0f, lean = lean, arms = arms)
    } else if (sit < 1f) {
        // puzzled: one hand on his head, the bowl hanging at his side, his head tilted
        val a = 1f - sit
        val shoulder = hip + Offset(0f, -tall * 0.31f)
        val head = shoulder + Offset(-tall * 0.03f, -tall * 0.15f)
        drawLine(ink, hip, foot + Offset(-tall * 0.075f, 0f), sw, StrokeCap.Round, alpha = a)
        drawLine(ink, hip, foot + Offset(tall * 0.075f, 0f), sw, StrokeCap.Round, alpha = a)
        drawLine(ink, hip, shoulder, sw, StrokeCap.Round, alpha = a)
        drawCircle(ink, tall * 0.1f, head, alpha = a)
        val elbow = shoulder + Offset(-tall * 0.2f, -tall * 0.04f)
        drawLine(ink, shoulder, elbow, sw * 0.9f, StrokeCap.Round, alpha = a)
        drawLine(ink, elbow, head + Offset(-tall * 0.07f, -tall * 0.06f), sw * 0.9f, StrokeCap.Round, alpha = a)
        drawLine(ink, shoulder, side + Offset(0f, -tall * 0.03f), sw * 0.9f, StrokeCap.Round, alpha = a)
        // question marks rise over his head, one after another
        for (k in 0 until 3) {
            val q = seg(sb, 0.7f + 0.08f * k, 0.86f + 0.08f * k) * a
            if (q <= 0f) continue
            val qc = head + Offset((k - 1) * tall * 0.2f, -tall * (0.3f + 0.06f * k) - tall * 0.05f * sin(t * 2f + k))
            val qr = tall * 0.055f
            drawArc(Color(0xFFF4E3B0), 190f, 260f, false, topLeft = qc - Offset(qr, qr), size = Size(2 * qr, 2 * qr), alpha = q, style = Stroke(sw * 0.75f, cap = StrokeCap.Round))
            drawLine(Color(0xFFF4E3B0), qc + Offset(0f, qr), qc + Offset(0f, qr * 1.6f), sw * 0.75f, StrokeCap.Round, alpha = q)
            drawCircle(Color(0xFFF4E3B0), sw * 0.45f, qc + Offset(0f, qr * 2.4f), alpha = q)
        }
    }
    if (sit > 0f) {
        w8Seated(foot, tall * 0.95f, ink, facing = -1f, alpha = sit)
        val chest = foot + Offset(tall * 0.03f, -tall * 0.36f)        // a hand on his chest
        drawLine(ink, chest + Offset(tall * 0.02f, -tall * 0.06f), chest + Offset(-tall * 0.08f, tall * 0.02f), sw, StrokeCap.Round, alpha = sit)
        softGlow(chest, tall * 0.18f, ShoreC8.Bliss, 0.35f * sit * (0.8f + 0.2f * sin(t * 2f)))
    }
    // the bowl: full from the moment it touches the sea, and spilling over its lips
    val bw = tall * 0.13f
    val bh = tall * 0.075f
    val bc = bowl + Offset(0f, bh * 0.4f)
    val full = seg(dip, 0.85f, 1f)
    drawArc(ShoreC8.Cup, 0f, 180f, true, topLeft = Offset(bc.x - bw, bc.y - bh), size = Size(2 * bw, 2 * bh))
    drawArc(Color(0xFF9A7A48), 0f, 180f, false, topLeft = Offset(bc.x - bw, bc.y - bh), size = Size(2 * bw, 2 * bh), style = Stroke(1.2f * dp))
    if (full > 0f) {
        drawOval(ShoreC8.Pool, topLeft = Offset(bc.x - bw, bc.y - bh * 0.25f), size = Size(2 * bw, bh * 0.5f), alpha = full)
        val spill = full * (1f - sit) * (if (puzzled > 0f) 1f - 0.6f * puzzled else 1f)
        if (spill > 0.05f && lifted > 0.2f) for (sd in listOf(-1f, 1f)) for (k in 0 until 7) {
            val f = fract(t * 1.8f + k / 7f + (if (sd > 0f) 0.5f else 0f))
            val lip = bc + Offset(sd * bw * 0.95f, 0f)
            val drop = lip + Offset(sd * bw * 0.3f * f, (foot.y - lip.y) * f * f)
            drawCircle(ShoreC8.Pool, (1.4f + 0.8f * (1f - f)) * dp, drop, alpha = spill * (1f - 0.5f * f))
        }
        if (spill > 0.05f && lifted > 0.2f) {          // where it lands, a splash on the sand
            val r = (3f + 5f * fract(t * 1.8f)) * dp
            drawOval(ShoreC8.Pool, topLeft = Offset(bc.x - r * 2f, foot.y - r * 0.3f), size = Size(r * 4f, r * 0.6f), alpha = 0.5f * spill * (1f - fract(t * 1.8f)), style = Stroke(1f * dp))
        }
    }
    if (calm > 0f) {      // the still sea mirrors the light of the horizon
        drawRect(Brush.verticalGradient(listOf(ShoreC8.SkyLow.copy(alpha = 0.22f * calm), Color.Transparent), startY = horizon, endY = shore), topLeft = Offset(0f, horizon), size = Size(w, shore - horizon))
    }
}
