package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 16, round 9 of the redraw (2026-10-21): «فضل التوحيد وتحقيقه». The earth filled with sins and the same
 * measure turned to light by forgiveness; and the plain of the reckoning, where a long queue waits at the Balance
 * while those who made their tawhid pure walk straight past it to the gate.
 */

// ═════════════ 1. globe: an earthful of sins, an earthful of forgiveness (tawhid-fadl) ═════════════

private object GlobeC16 {
    val SkyTop = Color(0xFF6A7AB8)
    val SkyLow = Color(0xFFF6D6B0)
    val Hill = Color(0xFF7A8A5A)
    val HillDark = Color(0xFF5A6A42)
    val Sea = Color(0xFF3A6AA8)
    val Land = Color(0xFF6A9A5A)
    val Glass = Color(0xFFE8F4FF)
    val Sin = Color(0xFF2A2230)
    val SinEdge = Color(0xFF4A3E52)
    val Light = Color(0xFFFFF4D4)
    val Snow = Color(0xFFFFFFFF)
    val Ink = Color(0xFF2A2430)
    val Heart = Color(0xFFFFD27A)
    val Idol = Color(0xFF8A7A6A)
    val Dark = Color(0xFF1E1826)
}

/**
 * Dawn: a hill on the left, the earth hanging like a great glass globe on the right. Beats: pureheart (a man walks
 * up the hill, his heart one clear light; the two idols by the path crack and fall as he passes without a glance),
 * earthful (dark stones pour into the globe until it is nearly full), forgiven (white snow falls into it, and stone
 * by stone the same measure turns to light), unmixed (a dark wisp creeps toward his heart and comes apart at its
 * edge: his faith is not mixed), safeway (a dome of light closes over him, the dark things outside draw back, and a
 * path of light opens up the hill ahead of him).
 */
internal fun DrawScope.globeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "pureheart" to Shot(Offset(w * 0.28f, h * 0.58f), 1.25f),
        "earthful" to Shot(Offset(w * 0.64f, h * 0.44f), 1.25f),
        "forgiven" to Shot(Offset(w * 0.64f, h * 0.44f), 1.3f),
        "unmixed" to Shot(Offset(w * 0.3f, h * 0.55f), 1.4f),
        "safeway" to Shot(Offset(w * 0.4f, h * 0.52f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { globeDawn(a) }
}

private fun DrawScope.globeDawn(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ph = a.b("pureheart")
    val ef = a.b("earthful")
    val fg = a.b("forgiven")
    val um = a.b("unmixed")
    val sw = a.b("safeway")
    drawRect(Brush.verticalGradient(listOf(GlobeC16.SkyTop, GlobeC16.SkyLow), endY = h))
    for (k in 0 until 20) drawCircle(Color.White, (0.6f + 0.6f * w8Rnd(k, 3.3f)) * dp, Offset(w * w8Rnd(k, 11.1f), h * 0.35f * w8Rnd(k, 5.7f)), alpha = 0.5f)
    // the hill and its path
    val hill = Path().apply {
        moveTo(0f, h * 0.62f)
        cubicTo(w * 0.2f, h * 0.6f, w * 0.38f, h * 0.7f, w * 0.52f, h * 0.86f)
        lineTo(w * 0.52f, h)
        lineTo(0f, h)
        close()
    }
    drawPath(hill, Brush.verticalGradient(listOf(GlobeC16.Hill, GlobeC16.HillDark), startY = h * 0.6f, endY = h))
    drawRect(GlobeC16.HillDark, topLeft = Offset(0f, h * 0.94f), size = Size(w, h * 0.06f))
    fun pathAt(u: Float): Offset {
        val p0 = Offset(w * 0.5f, h * 0.92f)
        val p3 = Offset(w * 0.04f, h * 0.64f)
        return cubic(p0, Offset(w * 0.4f, h * 0.8f), Offset(w * 0.2f, h * 0.64f), p3, u)
    }

    // the earth as a glass globe, on the right
    val gc = Offset(w * 0.66f, h * 0.44f)
    val gr = h * 0.31f
    softGlow(gc, gr * 1.4f, GlobeC16.Light, 0.3f + 0.6f * easeOut(seg(fg, 0.5f, 1f)))
    drawCircle(GlobeC16.Sea, gr, gc, alpha = 0.55f)
    val globe = Path().apply { addOval(androidx.compose.ui.geometry.Rect(gc.x - gr, gc.y - gr, gc.x + gr, gc.y + gr)) }
    clipPath(globe) {
        for ((dx, dy, r) in listOf(Triple(-0.35f, -0.3f, 0.4f), Triple(0.3f, 0.1f, 0.35f), Triple(-0.1f, 0.45f, 0.3f), Triple(0.5f, -0.45f, 0.22f))) {
            drawOval(GlobeC16.Land, topLeft = gc + Offset((dx - r) * gr, (dy - r * 0.7f) * gr), size = Size(2 * r * gr, 1.4f * r * gr), alpha = 0.6f)
        }
        // the stones of sin, filling it from the bottom up; turned to light by forgiveness
        val fill = easeOut(seg(ef, 0.05f, 0.95f))
        val turn = seg(fg, 0.15f, 0.9f)
        val rows = 11
        for (j in 0 until rows) {
            val y = gc.y + gr - (j + 0.5f) * (2 * gr / rows)
            val rowFill = seg(fill * rows - j, 0f, 1f)
            if (rowFill <= 0f) continue
            val half = kotlin.math.sqrt(max(0f, gr * gr - (y - gc.y) * (y - gc.y)))
            val n = max(1, (2 * half / (gr * 0.17f)).toInt())
            for (i in 0 until n) {
                val x = gc.x - half + (i + 0.5f) * (2 * half / n) + (if (j % 2 == 0) 0f else gr * 0.04f)
                val drop = (1f - rowFill) * gr * 0.6f
                val order = w8Rnd(j * 31 + i, 4.7f)
                val lit = seg(turn * 1.25f - order * 0.25f, 0f, 1f)
                val c = Offset(x, y - drop)
                val r = gr * 0.085f
                if (lit < 1f) drawCircle(GlobeC16.Sin, r, c, alpha = rowFill * (1f - lit))
                if (lit < 1f) drawCircle(GlobeC16.SinEdge, r, c, alpha = rowFill * (1f - lit), style = Stroke(0.8f * dp))
                if (lit > 0f) {
                    drawCircle(GlobeC16.Light, r, c, alpha = rowFill * lit)
                    drawCircle(Color.White, r * 0.4f, c, alpha = rowFill * lit)
                }
            }
        }
    }
    drawCircle(GlobeC16.Glass, gr, gc, alpha = 0.18f)
    drawCircle(Color.White, gr, gc, alpha = 0.7f, style = Stroke(1.6f * dp))
    drawArc(Color.White, 200f, 60f, false, topLeft = gc - Offset(gr * 0.85f, gr * 0.85f), size = Size(gr * 1.7f, gr * 1.7f), alpha = 0.7f, style = Stroke(3f * dp, cap = StrokeCap.Round))
    // the stones pouring in from above
    if (ef > 0f && ef < 1f) {
        for (k in 0 until 10) {
            val f = fract(t * 1.6f + k / 10f)
            val x = gc.x + gr * (w8Rnd(k, 8.1f) - 0.5f) * 0.6f
            drawCircle(GlobeC16.Sin, gr * 0.07f, Offset(x, mix(-h * 0.05f, gc.y - gr * 0.2f, f)), alpha = (1f - seg(ef, 0.85f, 1f)))
        }
    }
    // the snow of forgiveness falling into it
    if (fg > 0f) {
        val snow = easeOut(seg(fg, 0f, 0.2f)) * (1f - 0.6f * seg(fg, 0.85f, 1f))
        for (k in 0 until 28) {
            val f = fract(t * 0.5f + w8Rnd(k, 2.3f))
            val x = gc.x + gr * 1.2f * (w8Rnd(k, 6.6f) - 0.5f) * 2f + sin(t * 1.5f + k) * gr * 0.05f
            val y = mix(-h * 0.05f, gc.y + gr * 0.9f, f)
            drawCircle(GlobeC16.Snow, (1.3f + 1.2f * w8Rnd(k, 1.9f)) * dp, Offset(x, y), alpha = snow * (1f - seg(f, 0.85f, 1f)))
        }
    }

    // the idols by the path: they fall as he goes by
    val tall = h * 0.3f
    val walk = easeInOut(seg(ph, 0.05f, 0.85f))
    val u = mix(0.98f, 0.45f, walk)
    for ((k, iu) in listOf(0.78f, 0.6f).withIndex()) {
        val base = pathAt(iu) + Offset(0f, -h * 0.005f)
        val passed = u < iu
        val fall = if (passed) easeOut(seg(walk, if (k == 0) 0.38f else 0.72f, if (k == 0) 0.6f else 0.95f)) else 0f
        withTransform({ rotate(80f * fall * (if (k == 0) -1f else 1f), base) }) {
            drawRect(GlobeC16.Idol, topLeft = base + Offset(-w * 0.018f, -h * 0.03f), size = Size(w * 0.036f, h * 0.03f))
            drawRect(GlobeC16.Idol, topLeft = base + Offset(-w * 0.012f, -h * 0.1f), size = Size(w * 0.024f, h * 0.07f))
            drawCircle(GlobeC16.Idol, w * 0.014f, base + Offset(0f, -h * 0.115f))
            if (fall > 0.2f) drawLine(GlobeC16.Dark, base + Offset(-w * 0.008f, -h * 0.09f), base + Offset(w * 0.006f, -h * 0.05f), 1f * dp)
        }
    }
    // the man, his heart one clear light
    val foot = pathAt(u)
    val moving = ph > 0.05f && ph < 0.85f
    if (ph > 0f) {
        withTransform({ scale(-1f, 1f, pivot = foot) }) {
            figure(foot, tall, GlobeC16.Ink, seg(ph, 0f, 0.1f), walk = if (moving) t * 6f else 0f)
        }
        val heart = foot + Offset(0f, -tall * 0.62f)
        softGlow(heart, tall * 0.25f, GlobeC16.Heart, 0.8f)
        drawPath(w8Heart(heart, tall * 0.055f), GlobeC16.Heart)

        // unmixed: a dark wisp comes at his heart and comes apart at its edge
        if (um > 0f) {
            val reach = easeInOut(seg(um, 0.1f, 0.55f))
            val gone = easeOut(seg(um, 0.55f, 0.85f))
            val from = heart + Offset(-tall * 0.9f, -tall * 0.5f)
            val n = 14
            for (m in 0 until n) {
                val q = m / (n - 1f)
                if (q > reach) break
                val p = from + (heart - from) * (q * 0.88f) + Offset(0f, sin(q * 8f + t * 3f) * tall * 0.05f)
                val scatter = Offset((w8Rnd(m, 7.3f) - 0.5f) * tall * 0.6f, (w8Rnd(m, 2.1f) - 0.5f) * tall * 0.6f) * gone
                drawCircle(GlobeC16.Dark, tall * (0.04f + 0.02f * (1f - q)), p + scatter, alpha = 0.75f * (1f - gone))
            }
            drawCircle(GlobeC16.Heart, tall * 0.14f, heart, alpha = 0.7f * bump(seg(um, 0.45f, 0.8f)), style = Stroke(2f * dp))
        }
        // safeway: the dome of safety and the path of guidance
        if (sw > 0f) {
            val dome = easeOut(seg(sw, 0f, 0.4f))
            val dc = foot + Offset(0f, -tall * 0.45f)
            drawCircle(GlobeC16.Light, tall * 0.75f * dome, dc, alpha = 0.18f * dome)
            drawCircle(GlobeC16.Light, tall * 0.75f * dome, dc, alpha = 0.7f * dome, style = Stroke(1.6f * dp))
            // dark things outside it, drawing back
            for (k in 0 until 6) {
                val ang = PI.toFloat() * (0.9f + 0.2f * k)
                val r = tall * (0.95f + 0.35f * dome)
                val p = dc + Offset(cos(ang) * r, sin(ang) * r * 0.7f)
                drawCircle(Color(0xFFFFE070), 1.4f * dp, p + Offset(-2.5f * dp, 0f), alpha = 0.8f * (1f - 0.7f * dome))
                drawCircle(Color(0xFFFFE070), 1.4f * dp, p + Offset(2.5f * dp, 0f), alpha = 0.8f * (1f - 0.7f * dome))
            }
            val ahead = easeOut(seg(sw, 0.2f, 0.9f))
            for (k in 0 until 8) {
                val q = 0.45f - 0.05f * (k + 1)
                if ((k + 1) / 8f > ahead) break
                val p = pathAt(q.coerceAtLeast(0f))
                softGlow(p, 7f * dp, ArtInk.Gold, 0.7f)
                drawOval(ArtInk.Gold, topLeft = p - Offset(4f * dp, 1.6f * dp), size = Size(8f * dp, 3.2f * dp))
            }
        }
    }
}

// ═════════════ 2. plain: straight past the Balance to the gate (tahqiq-tawhid) ═════════════

private object PlainC16 {
    val SkyTop = Color(0xFFB8A6C8)
    val SkyLow = Color(0xFFF8E2C0)
    val Ground = Color(0xFFD8C4A0)
    val GroundFar = Color(0xFFC8B08A)
    val Queue = Color(0xFF6A6070)
    val Ink = Color(0xFF2A2430)
    val Lane = Color(0xFFF2C45A)
    val Gate = Color(0xFFE8D0A0)
    val GateDark = Color(0xFF8A6A42)
    val GateLight = Color(0xFFFFF4D0)
    val Scale = Color(0xFF7A6A52)
    val Heart = Color(0xFFFFD27A)
    val Speck = Color(0xFF2A2230)
    val Iron = Color(0xFFFF7A3A)
    val Crow = Color(0xFF1A1620)
    val Back = Color(0xE6201810)
}

/**
 * The plain at dawn: on the right a great Balance with a long queue winding to it; on the left the gate. Beats:
 * refine (close on one man: dark specks in his heart are drawn out one by one and blow away until it is pure gold),
 * straightin (the view opens: he and others like him, hearts of gold, walk a golden lane straight past the queue and
 * the Balance to the gate, which opens and takes them in, while the queue waits), noruqya (beside their lane one holds
 * out his hands to recite over them; they walk on, and it is crossed out), nocautery (a hot iron, glowing: crossed
 * out), noomen (a black bird flies across their way; they do not stop: crossed out), rely (their hearts shine
 * brighter as they go in).
 */
internal fun DrawScope.plainScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "refine" to Shot(Offset(w * 0.52f, h * 0.62f), 1.55f),
        "straightin" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "noruqya" to Shot(Offset(w * 0.4f, h * 0.62f), 1.25f),
        "nocautery" to Shot(Offset(w * 0.4f, h * 0.62f), 1.25f),
        "noomen" to Shot(Offset(w * 0.4f, h * 0.6f), 1.25f),
        "rely" to Shot(Offset(w * 0.3f, h * 0.6f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { plainDawn(a) }
}

private fun DrawScope.plainDawn(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rf = a.b("refine")
    val si = a.b("straightin")
    val nr = a.b("noruqya")
    val nc = a.b("nocautery")
    val no = a.b("noomen")
    val rl = a.b("rely")
    val horizon = h * 0.46f
    drawRect(Brush.verticalGradient(listOf(PlainC16.SkyTop, PlainC16.SkyLow), endY = horizon), size = Size(w, horizon))
    softGlow(Offset(w * 0.45f, horizon), w * 0.3f, Color(0xFFFFE8C0), 0.6f)
    drawRect(Brush.verticalGradient(listOf(PlainC16.GroundFar, PlainC16.Ground), startY = horizon, endY = h), topLeft = Offset(0f, horizon), size = Size(w, h - horizon))

    // the Balance and the queue that winds to it
    val sc = Offset(w * 0.8f, h * 0.3f)
    val post = h * 0.24f
    drawLine(PlainC16.Scale, sc, sc + Offset(0f, post), 3f * dp)
    drawRect(PlainC16.Scale, topLeft = sc + Offset(-w * 0.04f, post), size = Size(w * 0.08f, h * 0.015f))
    val tilt = 0.08f * sin(t * 0.7f)
    val arm = w * 0.1f
    val l = sc + Offset(-cos(tilt) * arm, -sin(tilt) * arm)
    val r = sc + Offset(cos(tilt) * arm, sin(tilt) * arm)
    drawLine(PlainC16.Scale, l, r, 2.6f * dp, StrokeCap.Round)
    for (e in listOf(l, r)) {
        val pan = e + Offset(0f, h * 0.12f)
        drawLine(PlainC16.Scale, e, pan + Offset(-w * 0.03f, 0f), 1f * dp)
        drawLine(PlainC16.Scale, e, pan + Offset(w * 0.03f, 0f), 1f * dp)
        drawArc(PlainC16.Scale, 0f, 180f, true, topLeft = pan - Offset(w * 0.035f, h * 0.02f), size = Size(w * 0.07f, h * 0.04f))
    }
    tagAt(a.tag("straightin"), sc + Offset(0f, -h * 0.06f), seg(si, 0.2f, 0.5f), back = PlainC16.Back, ink = ArtInk.Gold)
    val queue = w8Smooth(listOf(Offset(w * 0.98f, h * 0.98f), Offset(w * 0.7f, h * 0.86f), Offset(w * 0.95f, h * 0.72f), Offset(w * 0.74f, h * 0.62f), Offset(w * 0.8f, h * 0.56f)), 8)
    for (k in 0 until 18) {
        val p = w8Along(queue, k / 17f)
        val tl = h * (0.1f + 0.06f * (1f - k / 17f))
        figure(p, tl, PlainC16.Queue, 0.85f, walk = 0f, lean = 0.03f * sin(t * 0.8f + k))
    }

    // the gate on the left
    val gx = w * 0.13f
    val gy = h * 0.78f
    val open = easeOut(seg(si, 0.35f, 0.65f))
    softGlow(Offset(gx, gy - h * 0.14f), w * 0.15f, PlainC16.GateLight, 0.4f + 0.5f * open)
    lightShafts(Offset(gx, gy - h * 0.1f), -0.2f, 1.1f, w * 0.5f, PlainC16.GateLight, 0.7f * open, t, 7)
    drawPath(horseshoe(gx, gy - h * 0.2f, w * 0.075f, w * 0.065f, gy), PlainC16.Gate)
    drawPath(horseshoe(gx, gy - h * 0.2f, w * 0.06f, w * 0.05f, gy), lerpColor(Color(0xFF5A4030), PlainC16.GateLight, open))
    drawPath(horseshoe(gx, gy - h * 0.2f, w * 0.075f, w * 0.065f, gy), PlainC16.GateDark, style = Stroke(1.4f * dp))

    // the golden lane from the front of the plain to the gate
    val lane = w8Smooth(listOf(Offset(w * 0.56f, h * 1.02f), Offset(w * 0.5f, h * 0.86f), Offset(w * 0.34f, h * 0.8f), Offset(w * 0.22f, h * 0.8f), Offset(gx + w * 0.02f, gy - h * 0.01f)), 10)
    val laneA = easeOut(seg(si, 0f, 0.3f))
    if (laneA > 0f) {
        drawPath(w8Poly(lane), PlainC16.Lane.copy(alpha = 0.3f * laneA), style = Stroke(10f * dp, cap = StrokeCap.Round))
        drawPath(w8Poly(lane), PlainC16.Lane.copy(alpha = 0.85f * laneA), style = Stroke(1.6f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp), -t * 20f * dp)))
    }

    // the first man: close, his heart refined
    val tall = h * 0.4f
    val glow = 1f + 0.6f * easeOut(seg(rl, 0f, 0.5f))
    if (si <= 0f) {
        val foot = Offset(w * 0.52f, h * 0.98f)
        figure(foot, tall, PlainC16.Ink, seg(rf, 0f, 0.1f))
        val heart = foot + Offset(0f, -tall * 0.62f)
        val pure = easeOut(seg(rf, 0.65f, 0.9f))
        softGlow(heart, tall * 0.25f, PlainC16.Heart, (0.3f + 0.6f * pure) * seg(rf, 0f, 0.1f))
        drawPath(w8Heart(heart, tall * 0.06f), lerpColor(Color(0xFFB8A070), PlainC16.Heart, pure).copy(alpha = seg(rf, 0f, 0.1f)))
        // the specks drawn out one by one and blown away
        for (k in 0 until 5) {
            val out = easeOut(seg(rf, 0.12f + 0.1f * k, 0.32f + 0.1f * k))
            val home = heart + Offset((k - 2) * tall * 0.022f, (if (k % 2 == 0) -1f else 1f) * tall * 0.015f)
            val away = home + Offset(tall * (0.5f + 0.15f * k), -tall * (0.3f + 0.1f * k))
            val p = home + (away - home) * out
            drawCircle(PlainC16.Speck, tall * 0.014f * (1f + out), p, alpha = (1f - seg(out, 0.6f, 1f)) * seg(rf, 0f, 0.1f))
        }
        if (pure > 0f) glint(heart, tall * 0.2f, Color.White, bump(pure))
    }
    // the stream of those with pure hearts, along the lane and in at the gate
    if (si > 0f) {
        val n = 8
        for (k in 0 until n) {
            val start = k / n.toFloat()
            val go = seg(si, 0.1f, 1f)
            val u = if (k == 0) easeInOut(seg(si, 0.05f, 0.8f)) else fract(t * 0.06f + start)
            if (k > 0 && go < start * 0.6f) continue
            val p = w8Along(lane, u)
            val tl = tall * mix(0.85f, 0.4f, u)
            val inGate = seg(u, 0.88f, 1f)
            val alpha = (1f - inGate) * seg(si, 0f, 0.1f)
            if (alpha <= 0.01f) continue
            figure(p, tl, PlainC16.Ink, alpha, walk = t * 6f + k)
            val heart = p + Offset(0f, -tl * 0.62f)
            softGlow(heart, tl * 0.2f * glow, PlainC16.Heart, 0.7f * alpha)
            drawPath(w8Heart(heart, tl * 0.055f), PlainC16.Heart.copy(alpha = alpha))
        }
    }
    // what they do not seek, beside their lane, each crossed out as they walk on
    fun refused(b: Float, c: Offset, draw: DrawScope.(Float) -> Unit) {
        if (b <= 0f) return
        val show = easeOut(seg(b, 0f, 0.25f))
        val keep = 1f - 0.5f * seg(rl, 0f, 0.4f)
        draw(show * keep)
        cross14(c + Offset(0f, -h * 0.02f), h * 0.045f, seg(b, 0.35f, 0.8f) * keep)
    }
    refused(nr, Offset(w * 0.3f, h * 0.62f)) { al ->
        // one holding out his hands to recite over them
        val f = Offset(w * 0.3f, h * 0.72f)
        figure(f, h * 0.2f, PlainC16.Queue, al, arms = 0.8f)
        for (k in 0 until 3) {
            val q = fract(t * 1.4f + k / 3f)
            drawArc(PlainC16.Queue, 140f, 80f, false, topLeft = f + Offset(-h * 0.2f - h * 0.04f * q, -h * 0.22f), size = Size(h * 0.08f, h * 0.08f), alpha = al * (1f - q), style = Stroke(1.2f * dp))
        }
    }
    refused(nc, Offset(w * 0.44f, h * 0.66f)) { al ->
        val c = Offset(w * 0.44f, h * 0.7f)
        drawLine(Color(0xFF6A4A2A), c + Offset(w * 0.04f, h * 0.03f), c, 2.4f * dp, StrokeCap.Round, alpha = al)
        drawLine(PlainC16.Iron, c, c + Offset(-w * 0.035f, -h * 0.025f), 3f * dp, StrokeCap.Round, alpha = al)
        softGlow(c + Offset(-w * 0.035f, -h * 0.025f), h * 0.05f, PlainC16.Iron, 0.8f * al)
    }
    refused(no, Offset(w * 0.36f, h * 0.5f)) { al ->
        val fly = seg(no, 0f, 1f)
        val c = Offset(mix(w * 0.55f, w * 0.2f, fly), h * 0.5f + h * 0.02f * sin(t * 4f))
        val flap = sin(t * 12f) * h * 0.025f
        drawPath(Path().apply {
            moveTo(c.x - h * 0.05f, c.y - flap)
            quadraticTo(c.x - h * 0.02f, c.y - h * 0.01f, c.x, c.y)
            quadraticTo(c.x + h * 0.02f, c.y - h * 0.01f, c.x + h * 0.05f, c.y - flap)
        }, PlainC16.Crow.copy(alpha = al), style = Stroke(2.4f * dp, cap = StrokeCap.Round))
        drawCircle(PlainC16.Crow, h * 0.012f, c, alpha = al)
    }
}
