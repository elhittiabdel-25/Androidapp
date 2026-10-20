package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Night scenes in black and white, with faceless figures drawn in a few strokes: a house the fog cannot enter,
 * a walk past a shouting crowd, a deed that is returned, a joke by the fire, three men and three gifts.
 * Never a prophet or a companion: the figures are anyone.
 */

private object Mono {
    val Top = Color(0xFF0A0B0F)
    val Low = Color(0xFF17191F)
    val Line = Color(0xFFEDEBE4)
    val Dim = Color(0xFF8A8D96)
    val Ground = Color(0xFF0D0E12)
    val Warm = Color(0xFFF2C46B)
    val Ember = Color(0xFFE8794A)
    val Back = Color(0xE60E1015)
}

/** Night sky with stars and a crescent, the ground at [ground] with a faint reflection band, optional rain. */
private fun DrawScope.monoNight(t: Float, ground: Float, rain: Float = 0f) {
    val w = size.width
    val h = size.height
    val dp = density
    drawRect(Brush.verticalGradient(listOf(Mono.Top, Mono.Low), endY = ground))
    for (s in TWINKLES) {
        if (s.y * h * 0.75f > ground - h * 0.05f) continue
        drawCircle(Mono.Line.copy(alpha = 0.3f * (0.55f + 0.45f * sin(t * 1.2f + s.p))), radius = s.r * 0.7f * dp, center = Offset(s.x * w, s.y * h * 0.75f))
    }
    // crescent
    val mc = Offset(w * 0.12f, h * 0.14f)
    val mr = h * 0.055f
    drawCircle(Mono.Line.copy(alpha = 0.9f), radius = mr, center = mc)
    drawCircle(Mono.Top, radius = mr * 0.95f, center = mc + Offset(mr * 0.42f, -mr * 0.18f))
    drawRect(Mono.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))
    drawLine(Mono.Line.copy(alpha = 0.55f), Offset(0f, ground), Offset(w, ground), 1.2f * dp)
    if (rain > 0f) {
        for (k in 0 until 46) {
            val x = fract(k * 0.618f) * w
            val f = fract(t * (0.9f + 0.3f * (k % 3)) + k * 0.37f)
            val y = f * ground
            drawLine(Mono.Line.copy(alpha = 0.18f * rain), Offset(x, y), Offset(x - 1.5f * dp, y + h * 0.05f), 0.8f * dp)
        }
    }
}

/** A stone arch in white bricks (the doorway of a mosque, a mihrab), its opening lit by [glow]. */
private fun DrawScope.stoneArch(cx: Float, floor: Float, width: Float, height: Float, alpha: Float, glow: Float) {
    if (alpha <= 0.01f) return
    val dp = density
    val band = width * 0.15f
    val r = (width - band) / 2f
    val spring = floor - (height - width / 2f)
    if (glow > 0.01f) {
        val c = Offset(cx, mix(spring, floor, 0.5f))
        drawCircle(Brush.radialGradient(listOf(Mono.Warm.copy(alpha = 0.32f * glow * alpha), Color.Transparent), center = c, radius = width * 1.1f), radius = width * 1.1f, center = c)
        val open = Path().apply {
            moveTo(cx - r + band / 2, floor)
            lineTo(cx - r + band / 2, spring)
            arcTo(Rect(cx - r + band / 2, spring - r + band / 2, cx + r - band / 2, spring + r - band / 2), 180f, 180f, false)
            lineTo(cx + r - band / 2, floor)
            close()
        }
        drawPath(open, Mono.Warm, alpha = 0.18f * glow * alpha)
    }
    val path = Path().apply {
        moveTo(cx - r, floor)
        lineTo(cx - r, spring)
        arcTo(Rect(cx - r, spring - r, cx + r, spring + r), 180f, 180f, false)
        lineTo(cx + r, floor)
    }
    drawPath(path, Mono.Line, alpha = alpha, style = Stroke(band))
    // brick joints
    val joint = Mono.Low
    var y = floor - band * 0.75f
    var row = 0
    while (y > spring) {
        drawLine(joint, Offset(cx - r - band / 2, y), Offset(cx - r + band / 2, y), 1.1f * dp, alpha = alpha)
        drawLine(joint, Offset(cx + r - band / 2, y), Offset(cx + r + band / 2, y), 1.1f * dp, alpha = alpha)
        val mid = y + band * 0.37f
        val off = if (row % 2 == 0) -band * 0.12f else band * 0.12f
        drawLine(joint, Offset(cx - r + off, mid - band * 0.37f), Offset(cx - r + off, mid + band * 0.37f), 1f * dp, alpha = alpha * 0.8f)
        drawLine(joint, Offset(cx + r - off, mid - band * 0.37f), Offset(cx + r - off, mid + band * 0.37f), 1f * dp, alpha = alpha * 0.8f)
        y -= band * 0.75f
        row++
    }
    for (k in 1 until 9) {
        val ang = PI.toFloat() * (1f + k / 9f)
        val u = Offset(cos(ang), sin(ang))
        drawLine(joint, Offset(cx, spring) + u * (r - band / 2), Offset(cx, spring) + u * (r + band / 2), 1.1f * dp, alpha = alpha)
    }
}

/** A tag on a dark backing with a small tail pointing at [to] — a speech bubble at night. */
private fun DrawScope.monoBubble(tl: TextLayoutResult?, c: Offset, to: Offset, alpha: Float, ink: Color = Mono.Line, edge: Color = Mono.Line) {
    if (tl == null || alpha <= 0.01f) return
    val dp = density
    val padX = 7f * dp
    val padY = 3.5f * dp
    val w = tl.size.width + 2 * padX
    val h = tl.size.height + 2 * padY
    val x = (c.x - w / 2).coerceIn(2f * dp, max(2f * dp, size.width - w - 2f * dp))
    val y = (c.y - h / 2).coerceIn(2f * dp, max(2f * dp, size.height - h - 2f * dp))
    val a = alpha.coerceIn(0f, 1f)
    val bx = (to.x).coerceIn(x + 8f * dp, x + w - 8f * dp)
    val tail = Path().apply {
        moveTo(bx - 5f * dp, y + h - 1f)
        lineTo(to.x, to.y)
        lineTo(bx + 5f * dp, y + h - 1f)
        close()
    }
    drawPath(tail, Mono.Back, alpha = a)
    drawPath(tail, edge, alpha = a * 0.8f, style = Stroke(1f * dp))
    drawRoundRect(Mono.Back, topLeft = Offset(x, y), size = Size(w, h), cornerRadius = CornerRadius(h / 2), alpha = a)
    drawRoundRect(edge, topLeft = Offset(x, y), size = Size(w, h), cornerRadius = CornerRadius(h / 2), alpha = a * 0.8f, style = Stroke(1f * dp))
    drawText(tl, color = ink, topLeft = Offset(x + padX, y + padY), alpha = a)
}

/** A figure sitting on the ground at [seat], knees toward [facing] (-1 left, 1 right); [bob] shakes it (laughing). */
private fun DrawScope.sitting(seat: Offset, tall: Float, color: Color, alpha: Float, facing: Float, bob: Float = 0f) {
    if (alpha <= 0.01f) return
    val sw = max(1.7f * density, tall * 0.065f)
    val hip = seat - Offset(0f, tall * 0.04f)
    val knee = hip + Offset(facing * tall * 0.24f, -tall * 0.13f)
    val foot = Offset(knee.x + facing * tall * 0.06f, seat.y)
    val shoulder = hip + Offset(-facing * tall * 0.03f, -tall * 0.33f + bob)
    val head = shoulder + Offset(-facing * tall * 0.01f, -tall * 0.15f + bob * 0.5f)
    drawLine(color, hip, knee, sw, cap = StrokeCap.Round, alpha = alpha)
    drawLine(color, knee, foot, sw, cap = StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, shoulder, sw, cap = StrokeCap.Round, alpha = alpha)
    drawCircle(color, radius = tall * 0.1f, center = head, alpha = alpha)
    drawLine(color, shoulder, knee + Offset(-facing * tall * 0.02f, -tall * 0.02f), sw * 0.9f, cap = StrokeCap.Round, alpha = alpha)
}

/** A figure standing in prayer, hands folded on the chest. */
private fun DrawScope.praying(foot: Offset, tall: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val sw = max(1.7f * density, tall * 0.065f)
    val hip = foot - Offset(0f, tall * 0.45f)
    val shoulder = hip - Offset(0f, tall * 0.31f)
    val head = shoulder - Offset(0f, tall * 0.15f)
    drawLine(color, hip, foot + Offset(-tall * 0.05f, 0f), sw, cap = StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, foot + Offset(tall * 0.05f, 0f), sw, cap = StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, shoulder, sw, cap = StrokeCap.Round, alpha = alpha)
    drawCircle(color, radius = tall * 0.1f, center = head, alpha = alpha)
    val chest = shoulder + Offset(0f, tall * 0.12f)
    drawLine(color, shoulder, chest + Offset(-tall * 0.09f, tall * 0.02f), sw * 0.9f, cap = StrokeCap.Round, alpha = alpha)
    drawLine(color, chest + Offset(-tall * 0.09f, tall * 0.02f), chest + Offset(tall * 0.05f, -tall * 0.01f), sw * 0.9f, cap = StrokeCap.Round, alpha = alpha)
    drawLine(color, shoulder, chest + Offset(tall * 0.09f, tall * 0.04f), sw * 0.9f, cap = StrokeCap.Round, alpha = alpha * 0.85f)
}

private fun bigWord(scope: DrawScope, tl: TextLayoutResult?, c: Offset, scale: Float, color: Color, alpha: Float) {
    if (tl == null || alpha <= 0.01f) return
    scope.withTransform({ scale(scale, scale, pivot = c) }) {
        drawText(tl, color = color, topLeft = c - Offset(tl.size.width / 2f, tl.size.height / 2f), alpha = alpha.coerceIn(0f, 1f))
    }
}

// ───────────── khawf-shirk: a lit house the fog of shirk cannot enter ─────────────

internal fun DrawScope.watchScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.84f
    monoNight(t, ground)

    val fog = a.b("fog")
    val ring = a.b("shield")
    val crack = a.b("wisp")
    val shut = a.b("close")

    // the house
    val cx = w * 0.5f
    val bw = w * 0.3f
    val bh = h * 0.27f
    val roof = h * 0.13f
    val top = ground - bh
    val body = Path().apply {
        moveTo(cx - bw / 2, ground)
        lineTo(cx - bw / 2, top)
        lineTo(cx, top - roof)
        lineTo(cx + bw / 2, top)
        lineTo(cx + bw / 2, ground)
    }
    val light = 0.85f + 0.15f * sin(t * 1.7f)
    drawCircle(Brush.radialGradient(listOf(Mono.Warm.copy(alpha = 0.16f * light), Color.Transparent), center = Offset(cx, ground - bh * 0.4f), radius = bw),
        radius = bw, center = Offset(cx, ground - bh * 0.4f))
    drawPath(body, Mono.Ground)
    drawPath(body, Mono.Line, style = Stroke(1.8f * dp))
    // door and two lit windows
    val dw = bw * 0.17f
    drawRect(Mono.Line, topLeft = Offset(cx - dw / 2, ground - bh * 0.5f), size = Size(dw, bh * 0.5f), style = Stroke(1.4f * dp))
    for (side in intArrayOf(-1, 1)) {
        val wx = cx + side * bw * 0.28f
        val wy = top + bh * 0.24f
        val ww = bw * 0.16f
        drawRect(Mono.Warm, topLeft = Offset(wx - ww / 2, wy), size = Size(ww, ww), alpha = 0.8f * light)
        drawRect(Mono.Line, topLeft = Offset(wx - ww / 2, wy), size = Size(ww, ww), style = Stroke(1.2f * dp))
        drawLine(Mono.Line, Offset(wx, wy), Offset(wx, wy + ww), 1f * dp)
    }
    // the small window under the roof, where riya slips in
    val sw0 = Offset(cx, top - roof * 0.38f)
    val sws = bw * 0.09f
    drawRect(Mono.Warm, topLeft = sw0 - Offset(sws / 2, sws / 2), size = Size(sws, sws), alpha = 0.7f * light)
    drawRect(Mono.Line, topLeft = sw0 - Offset(sws / 2, sws / 2), size = Size(sws, sws), style = Stroke(1.1f * dp))

    // the fog of shirk, closing in from both sides, then held off at the ring
    val creep = easeOut(seg(fog, 0f, 0.8f))
    val hold = easeOut(seg(ring, 0.1f, 0.8f))
    if (fog > 0f) {
        for (k in 0 until 18) {
            val side = if (k % 2 == 0) -1f else 1f
            val m = k / 2
            val reachX = mix(w * 0.62f, bw * 0.95f, hold)                 // how close it gets to the centre
            val dist = mix(w * 0.75f, reachX, creep) + m * w * 0.035f
            val drift = sin(t * 0.6f + k * 1.3f) * w * 0.012f
            val x = cx + side * (dist + drift)
            val y = ground - h * (0.03f + 0.045f * (m % 4)) - sin(t * 0.4f + k) * h * 0.01f
            val r = h * (0.09f + 0.025f * (k % 3))
            drawCircle(Brush.radialGradient(listOf(Mono.Dim.copy(alpha = 0.34f * seg(fog, 0f, 0.3f)), Color.Transparent), center = Offset(x, y), radius = r),
                radius = r, center = Offset(x, y))
        }
        tagAt(a.tag("fog"), Offset(w * 0.14f, ground - h * 0.24f), seg(fog, 0.4f, 1f), back = Mono.Back, ink = Mono.Line)
    }
    // the ring of light the du'a draws around the house
    if (ring > 0f) {
        val rr = easeOut(seg(ring, 0f, 0.7f))
        val oval = Rect(cx - bw * 0.95f * rr, ground - h * 0.06f, cx + bw * 0.95f * rr, ground + h * 0.06f)
        drawOval(Mono.Warm, topLeft = oval.topLeft, size = oval.size, alpha = 0.75f * seg(ring, 0f, 0.3f), style = Stroke(1.6f * dp))
        drawOval(Brush.radialGradient(listOf(Mono.Warm.copy(alpha = 0.18f), Color.Transparent), center = oval.center, radius = bw), topLeft = oval.topLeft, size = oval.size)
        tagAt(a.tag("shield"), Offset(cx, top - roof - h * 0.12f), seg(ring, 0.4f, 1f) * (1f - seg(crack, 0f, 0.3f)), back = Mono.Back, ink = Mono.Warm)
    }
    // riya: a thin wisp toward the small window — then the window is shut
    if (crack > 0f) {
        val cut = easeOut(seg(shut, 0f, 0.5f))
        val reach = easeInOut(seg(crack, 0.1f, 0.9f)) * (1f - cut * 0.6f)
        val from = Offset(w * 0.86f, top - roof * 0.9f)
        val pts = List(24) { m ->
            val u = m / 23f
            Offset(mix(from.x, sw0.x + sws * 0.6f, u), mix(from.y, sw0.y, u) + sin(u * 9f + t * 2f) * h * 0.02f * (1f - u))
        }
        val shown = pts.take(max(2, (pts.size * reach).toInt()))
        for (m in 1 until shown.size) {
            drawLine(Mono.Dim, shown[m - 1], shown[m], 2.4f * dp * (1f - m / 30f), cap = StrokeCap.Round, alpha = 0.8f * seg(crack, 0f, 0.2f) * (1f - cut))
        }
        tagAt(a.tag("wisp"), Offset(w * 0.84f, top - roof - h * 0.04f), seg(crack, 0.3f, 0.7f), back = Mono.Back, ink = Mono.Ember)
        if (shut > 0f) {
            // shutters close over the small window
            val f = easeOut(seg(shut, 0f, 0.6f))
            drawRect(Mono.Line, topLeft = sw0 - Offset(sws / 2, sws / 2), size = Size(sws * f, sws), alpha = 0.9f)
            tagAt(a.tag("close"), Offset(cx, top - roof - h * 0.12f), seg(shut, 0.4f, 1f), back = Mono.Back, ink = Mono.Warm)
        }
    }
}

// ───────────── khawf-tawakkul: he walks on to the arch while the crowd shouts ─────────────

internal fun DrawScope.crowdScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.84f
    monoNight(t, ground)

    val walk = a.b("march")
    val shout = a.b("shout")
    val arrive = a.b("arrive")
    val calm = a.b("quiet")

    val archX = w * 0.17f
    stoneArch(archX, ground, w * 0.2f, h * 0.5f, seg(walk, 0f, 0.3f), 0.35f + 0.65f * easeOut(arrive))
    tagAt(a.tag("march"), Offset(archX, ground - h * 0.6f), seg(walk, 0.3f, 0.8f), back = Mono.Back, ink = Mono.Warm)

    // the crowd, further back, shouting
    val crowd = seg(shout, 0f, 0.3f)
    if (crowd > 0f) {
        val back = ground - h * 0.05f
        for (k in 0 until 6) {
            val x = w * (0.6f + 0.065f * k)
            val tall = h * (0.2f + 0.02f * (k % 2))
            val jolt = sin(t * 9f + k * 1.7f) * (1f - calm) * h * 0.006f
            figure(Offset(x, back + jolt), tall, Mono.Dim, alpha = crowd * 0.85f, arms = if (k % 2 == 0) 0.6f * (1f - calm) else 0f)
            // shouting marks, then quiet
            val markY = back - tall * 1.32f
            val loud = (1f - easeOut(calm)) * crowd
            if (loud > 0.02f) {
                drawLine(Mono.Ember, Offset(x, markY - h * 0.045f), Offset(x, markY - h * 0.012f), 2f * dp, cap = StrokeCap.Round, alpha = loud)
                drawCircle(Mono.Ember, radius = 1.4f * dp, center = Offset(x, markY), alpha = loud)
            }
            if (calm > 0f) drawCircle(Mono.Warm, radius = 2f * dp, center = Offset(x, markY - h * 0.02f), alpha = easeOut(seg(calm, 0.3f, 0.8f)) * crowd)
        }
        tagAt(a.tag("shout"), Offset(w * 0.77f, ground - h * 0.48f), seg(shout, 0.3f, 0.7f) * (1f - seg(calm, 0f, 0.3f)), back = Mono.Back, ink = Mono.Ember)
        tagAt(a.tag("quiet"), Offset(w * 0.77f, ground - h * 0.48f), seg(calm, 0.4f, 1f), back = Mono.Back, ink = Mono.Warm)
    }

    // the believer: walks on, does not turn back
    if (walk > 0f) {
        val x = when {
            arrive > 0f -> mix(w * 0.43f, archX + w * 0.04f, easeInOut(arrive))
            shout > 0f -> mix(w * 0.6f, w * 0.43f, easeInOut(shout))
            else -> mix(w * 0.9f, w * 0.6f, easeInOut(seg(walk, 0.1f, 1f)))
        }
        val moving = (walk in 0.1f..0.99f) || (shout in 0.01f..0.99f) || (arrive in 0.01f..0.99f)
        figure(Offset(x, ground), h * 0.27f, Mono.Line, alpha = seg(walk, 0f, 0.2f), walk = if (moving) x / (w * 0.035f) else 0f)
        if (arrive > 0.6f) tagAt(a.tag("arrive"), Offset(archX + w * 0.2f, ground - h * 0.38f), seg(arrive, 0.6f, 1f), back = Mono.Back, ink = Mono.Warm)
    }
}

// ───────────── riya: the deed adorned for the eyes of people is returned ─────────────

internal fun DrawScope.deedsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.84f
    monoNight(t, ground)

    val pray = a.b("pray")
    val gaze = a.b("eyes")
    val khafi = a.b("khafi")
    val back = a.b("returned")
    val left = a.b("left")

    val archX = w * 0.66f
    stoneArch(archX, ground, w * 0.24f, h * 0.56f, seg(pray, 0f, 0.3f), 0.5f)
    val tall = h * 0.27f
    praying(Offset(archX, ground), tall, Mono.Line, seg(pray, 0.1f, 0.4f))

    // the onlookers
    val eyes = seg(gaze, 0f, 0.3f)
    if (eyes > 0f) {
        for (k in 0 until 3) {
            val x = w * (0.1f + 0.1f * k)
            val tallK = h * 0.22f
            figure(Offset(x, ground), tallK, Mono.Dim, alpha = eyes * 0.9f, arms = if (k == 1) 0.4f else 0f)
            val from = Offset(x - 0f, ground - tallK * 1.08f)
            val to = Offset(archX - w * 0.04f, ground - tall * 0.95f)
            val dash = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 4f * dp), -t * 12f * dp)
            drawLine(Mono.Dim, from, from + (to - from) * easeOut(seg(gaze, 0.2f, 0.7f)), 1f * dp, pathEffect = dash, alpha = eyes * 0.7f * (1f - seg(left, 0f, 0.5f)))
        }
        tagAt(a.tag("eyes"), Offset(w * 0.2f, ground - h * 0.42f), seg(gaze, 0.3f, 0.8f), back = Mono.Back, ink = Mono.Line)
    }

    // the deed: a tile of light rising from him; adorned when people look; then sent back down, and left
    if (pray > 0f) {
        val rise = easeOut(seg(pray, 0.3f, 1f))
        val fall = easeInOut(seg(back, 0.1f, 0.8f))
        val top = ground - tall * 1.3f - h * 0.28f * rise
        val y = mix(top, ground - h * 0.04f, fall)
        val c = Offset(archX + sin(t * 1.3f) * w * 0.004f, y)
        val s = h * 0.075f
        val fade = 1f - 0.85f * easeOut(left)
        val dim = 1f - 0.55f * fall
        drawCircle(Brush.radialGradient(listOf(Mono.Warm.copy(alpha = 0.4f * dim * fade), Color.Transparent), center = c, radius = s * 2.2f), radius = s * 2.2f, center = c)
        drawRoundRect(lerp2(Mono.Dim, Mono.Warm, dim), topLeft = c - Offset(s / 2, s / 2), size = Size(s, s), cornerRadius = CornerRadius(s * 0.18f), alpha = fade)
        // adorned for the eyes: gilt corners and glitter
        val adorn = easeOut(seg(gaze, 0.4f, 1f))
        if (adorn > 0f) {
            val pad = s * 0.18f * adorn
            drawRoundRect(Mono.Line, topLeft = c - Offset(s / 2 + pad, s / 2 + pad), size = Size(s + 2 * pad, s + 2 * pad),
                cornerRadius = CornerRadius(s * 0.24f), alpha = fade * adorn, style = Stroke(1.4f * dp))
            for (k in 0 until 6) {
                val f = fract(t * 0.8f + k / 6f)
                val p = c + Offset(cos(k * 1.05f + t) * s * (0.7f + 0.5f * f), sin(k * 1.05f + t) * s * (0.7f + 0.5f * f))
                drawCircle(Mono.Line, radius = 1.1f * dp, center = p, alpha = adorn * (1f - f) * fade)
            }
            tagAt(a.tag("eyes", 0, 1), c + Offset(w * 0.15f, -s * 0.2f), seg(gaze, 0.6f, 1f) * (1f - seg(back, 0f, 0.3f)), back = Mono.Back, ink = Mono.Line)
        }
        // the hidden thread from the deed to the onlookers
        if (khafi > 0f) {
            val to = Offset(w * 0.2f, ground - h * 0.3f)
            val k2 = easeOut(seg(khafi, 0f, 0.7f)) * (1f - seg(left, 0f, 0.4f))
            val mid = Offset((c.x + to.x) / 2f, min(c.y, to.y) - h * 0.06f)
            val pts = List(18) { m ->
                val u = m / 17f
                val q1 = c + (mid - c) * u
                val q2 = mid + (to - mid) * u
                q1 + (q2 - q1) * u
            }
            for (m in 1 until pts.size) {
                if (m / 17f > k2) break
                drawLine(Mono.Ember, pts[m - 1], pts[m], 1.2f * dp, alpha = 0.75f * k2)
            }
            tagAt(a.tag("khafi"), Offset(w * 0.4f, min(c.y, to.y) - h * 0.1f), seg(khafi, 0.3f, 0.8f) * (1f - seg(back, 0f, 0.4f)), back = Mono.Back, ink = Mono.Ember)
        }
        if (back > 0f) tagAt(a.tag("returned"), Offset(archX - w * 0.2f, ground - h * 0.1f), seg(back, 0.5f, 1f) * (1f - seg(left, 0f, 0.4f)), back = Mono.Back, ink = Mono.Ember)
        // His words in the hadith qudsi: high in the picture, never by the ground
        if (left > 0f) tagAt(a.tag("left"), Offset(w * 0.4f, h * 0.13f), seg(left, 0.3f, 0.8f), back = Mono.Back, ink = Mono.Ember)
    }
}

private fun lerp2(a: Color, b: Color, f: Float): Color = androidx.compose.ui.graphics.lerp(a, b, f.coerceIn(0f, 1f))

// ───────────── istihza: a joke by the fire on the road to Tabuk ─────────────

internal fun DrawScope.campScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.82f
    monoNight(t, ground)
    // dunes on the horizon
    val dunes = Path().apply {
        moveTo(0f, ground)
        for (k in 0..24) {
            val x = w * k / 24f
            lineTo(x, ground - h * (0.05f + 0.035f * sin(k * 0.9f) + 0.02f * sin(k * 2.3f)))
        }
        lineTo(w, ground)
        close()
    }
    drawPath(dunes, Color(0xFF111319))
    drawLine(Mono.Line.copy(alpha = 0.55f), Offset(0f, ground), Offset(w, ground), 1.2f * dp)

    val camp = a.b("camp")
    val kufr = a.b("kufr")
    val joke = a.b("joke")
    val tabuk = a.b("tabuk")

    // the road (to Tabuk), a dotted line into the distance
    if (tabuk > 0f) {
        val pts = List(30) { m ->
            val u = m / 29f
            Offset(mix(w * 0.95f, w * 0.62f, u), mix(h * 0.97f, ground - h * 0.06f, u))
        }
        for (m in pts.indices) {
            if (m / 29f > easeOut(seg(tabuk, 0f, 0.7f))) break
            drawCircle(Mono.Line, radius = 1.3f * dp * (1f - m / 45f), center = pts[m], alpha = 0.7f)
        }
        tagAt(a.tag("tabuk"), Offset(w * 0.8f, ground - h * 0.16f), seg(tabuk, 0.4f, 1f), back = Mono.Back, ink = Mono.Line)
    }

    // the fire
    val fx = w * 0.4f
    val fire = seg(camp, 0f, 0.4f)
    if (fire > 0f) {
        val fc = Offset(fx, ground - h * 0.03f)
        drawCircle(Brush.radialGradient(listOf(Mono.Warm.copy(alpha = 0.35f * fire), Color.Transparent), center = fc, radius = w * 0.22f), radius = w * 0.22f, center = fc)
        for (k in 0 until 3) {
            val fl = 0.75f + 0.25f * sin(t * (6f + k) + k)
            val flame = Path().apply {
                val bw = w * (0.035f - 0.008f * k)
                val fh = h * (0.12f - 0.025f * k) * fl
                moveTo(fx - bw, ground)
                quadraticBezierTo(fx - bw * 0.9f, ground - fh * 0.6f, fx + sin(t * 3f + k) * bw * 0.3f, ground - fh)
                quadraticBezierTo(fx + bw * 0.9f, ground - fh * 0.6f, fx + bw, ground)
                close()
            }
            drawPath(flame, if (k == 2) Mono.Line else Mono.Warm, alpha = fire * (0.55f + 0.2f * k))
        }
        // sparks, scattered when the word falls
        for (k in 0 until 8) {
            val f = fract(t * 0.5f + k * 0.13f)
            val spread = 1f + 2.5f * easeOut(seg(kufr, 0f, 0.5f)) * (1f - seg(kufr, 0.8f, 1f))
            drawCircle(Mono.Warm, radius = 1.2f * dp, center = Offset(fx + sin(k * 2.4f + t) * w * 0.02f * spread, ground - h * 0.06f - f * h * 0.25f),
                alpha = fire * (1f - f) * 0.9f)
        }
        // the travellers on either side of it (never behind it: that would read as a figure in the fire); the one on the right laughs
        val tall = h * 0.27f
        sitting(Offset(fx - w * 0.16f, ground), tall, Mono.Line, fire, 1f)
        val laugh = seg(camp, 0.5f, 1f) * (1f - seg(kufr, 0.2f, 0.5f))
        sitting(Offset(fx + w * 0.17f, ground), tall, Mono.Line, fire, -1f, bob = sin(t * 16f) * tall * 0.025f * laugh)
        sitting(Offset(fx - w * 0.29f, ground), tall * 0.9f, Mono.Dim, fire * 0.8f, 1f)
    }

    // «it was only a joke» — no excuse
    val jx = fx + w * 0.22f
    val jy = ground - h * 0.5f
    if (joke > 0f) {
        monoBubble(a.tag("joke"), Offset(jx, jy), Offset(fx + w * 0.17f, ground - h * 0.32f), seg(joke, 0f, 0.3f))
        val strike = easeOut(seg(joke, 0.4f, 0.8f))
        val tl = a.tag("joke")
        if (tl != null && strike > 0f) {
            val half = tl.size.width / 2f + 6f * dp
            drawLine(Mono.Ember, Offset(jx + half, jy - h * 0.03f), Offset(jx + half - 2 * half * strike, jy + h * 0.03f), 2.2f * dp, cap = StrokeCap.Round)
        }
        tagAt(a.tag("joke", 0, 1), Offset(jx, jy + h * 0.11f), seg(joke, 0.6f, 1f), back = Mono.Back, ink = Mono.Ember)
    }
    // the word that the joke does not undo
    if (kufr > 0f) {
        bigWord(this, a.tag("kufr"), Offset(w * 0.25f, h * 0.24f), 2.6f, Mono.Ember, easeOut(seg(kufr, 0f, 0.5f)))
    }
}

// ───────────── nisbat-niam: three men, three gifts; two deny the Giver, one thanks Him ─────────────

internal fun DrawScope.giftsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.86f
    monoNight(t, ground)

    val other = a.b("other")
    val self = a.b("self")
    val deny = a.b("deny")
    val thank = a.b("thank")
    val xs = floatArrayOf(w * 0.8f, w * 0.5f, w * 0.2f)
    val beats = floatArrayOf(other, self, thank)
    val tall = h * 0.3f
    for (k in 0 until 3) {
        val p = beats[k]
        if (p <= 0f) continue
        val al = seg(p, 0f, 0.25f)
        val foot = Offset(xs[k], ground)
        figure(foot, tall, Mono.Line, alpha = al, arms = if (k < 2) 0.35f else 0f)
        // the gift in his hands
        val g = foot - Offset(w * 0.06f, tall * 0.62f)
        val s = h * 0.085f
        val lost = if (k < 2) easeOut(seg(deny, 0.2f, 0.9f)) else 0f
        val kept = if (k == 2) easeOut(seg(thank, 0.5f, 1f)) else 0f
        val glow = (1f - lost) * (0.7f + 0.3f * kept) * (0.85f + 0.15f * sin(t * 2f + k))
        if (glow > 0.02f) {
            drawCircle(Brush.radialGradient(listOf(Mono.Warm.copy(alpha = (0.32f + 0.25f * kept) * glow * al), Color.Transparent), center = g, radius = s * (2f + kept)),
                radius = s * (2f + kept), center = g)
        }
        val col = lerp2(Mono.Dim, Mono.Warm, 1f - lost)
        drawRoundRect(col, topLeft = g - Offset(s / 2, s / 2) + Offset(0f, lost * h * 0.08f), size = Size(s, s * (1f - 0.35f * lost)),
            cornerRadius = CornerRadius(s * 0.12f), alpha = al * (1f - 0.45f * lost))
        drawLine(Mono.Ground, g + Offset(0f, -s / 2 + lost * h * 0.08f), g + Offset(0f, s / 2 + lost * h * 0.05f), 1.4f * dp, alpha = al)
        drawLine(Mono.Ground, g + Offset(-s / 2, lost * h * 0.07f), g + Offset(s / 2, lost * h * 0.07f), 1.4f * dp, alpha = al)
        if (lost > 0.3f) {
            for (m in 0 until 5) {
                val f = fract(t * 0.4f + m * 0.2f)
                drawCircle(Mono.Dim, radius = 1.3f * dp, center = g + Offset((m - 2) * s * 0.25f, s * 0.5f + f * h * 0.12f), alpha = lost * (1f - f) * al)
            }
        }
    }
    // what each one says (the first two sayings are examples of the two errors; they go before the story's two are named)
    monoBubble(a.tag("other"), Offset(xs[0] - w * 0.02f, ground - tall * 1.55f), Offset(xs[0], ground - tall * 1.2f), seg(other, 0.2f, 0.5f) * (1f - seg(deny, 0f, 0.3f)))
    monoBubble(a.tag("self"), Offset(xs[1], ground - tall * 1.55f), Offset(xs[1], ground - tall * 1.2f), seg(self, 0.2f, 0.5f) * (1f - seg(deny, 0f, 0.3f)))
    monoBubble(a.tag("thank", 0, 1), Offset(xs[2] + w * 0.05f, ground - tall * 1.55f), Offset(xs[2], ground - tall * 1.2f), seg(thank, 0.25f, 0.55f), ink = Mono.Warm, edge = Mono.Warm)
    // who they were
    for (k in 0 until 2) tagAt(a.tag("deny", 0, k), Offset(xs[k], ground + h * 0.065f), seg(deny, 0.2f, 0.6f), back = Mono.Back, ink = Mono.Dim)
    tagAt(a.tag("thank", 0, 0), Offset(xs[2], ground + h * 0.065f), seg(thank, 0.2f, 0.6f), back = Mono.Back, ink = Mono.Warm)
}
