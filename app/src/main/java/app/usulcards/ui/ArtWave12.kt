package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/*
 * Wave 12, round 5 of the redraw (2026-10-21): «الحكم على أهل القبلة». A young plant in its pot on a potting
 * bench (the sinner's faith wilted but rooted, between the axe and the paint brush), and a road at dusk to the
 * door of repentance (turning back, dropping the sack, regret and resolve; then the sun rising in the west and the
 * door closing).
 */

// ═════════════ 1. sapling: wilted but rooted, between the axe and the brush (ahl-qibla) ═════════════

private object SaplingC12 {
    val Wall = Color(0xFFE8E2CE)
    val WallLow = Color(0xFFD2C9AE)
    val Glass = Color(0xFFCFE6EE)
    val Bench = Color(0xFF8A6440)
    val BenchDark = Color(0xFF5A3E26)
    val Pot = Color(0xFFB86A44)
    val PotDark = Color(0xFF8A4A2E)
    val Soil = Color(0xFF5A4030)
    val Root = Color(0xFFE2C46A)
    val Stem = Color(0xFF5A7A3A)
    val Leaf = Color(0xFF6AA24A)
    val Wilted = Color(0xFFB8A24A)
    val Dead = Color(0xFF8A7A5A)
    val Sin = Color(0xFF3A3038)
    val Ink = Color(0xFF2A2420)
    val Paint = Color(0xFF3ED86A)
    val Axe = Color(0xFF6A6A72)
}

/**
 * A potting bench under a glasshouse window. Beats: rooted (a young plant in a clay pot, its roots glowing in the
 * soil; at the far end of the bench lies a dry one whose root has been cut through), wilt (dark drops fall on it and
 * some of its leaves yellow and droop, but its root still glows), cloche (the gardener sets a glass bell over it and
 * steps back, his hands open: it is kept, not his to judge), axe (one comes with an axe raised to cut it down as dead
 * wood; he is stopped), paint (another paints its yellow leaves green; the paint runs off and the yellow shows):
 * the plant stays as it is, weakened, alive, between the two.
 */
internal fun DrawScope.saplingScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rooted" to Shot(Offset(w * 0.5f, h * 0.55f), 1.25f),
        "wilt" to Shot(Offset(w * 0.5f, h * 0.5f), 1.5f),
        "cloche" to Shot(Offset(w * 0.55f, h * 0.55f), 1.3f),
        "axe" to Shot(Offset(w * 0.4f, h * 0.55f), 1.3f),
        "paint" to Shot(Offset(w * 0.55f, h * 0.55f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { saplingBench(a) }
}

private fun DrawScope.saplingBench(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rt = a.b("rooted")
    val wl = a.b("wilt")
    val cl = a.b("cloche")
    val ax = a.b("axe")
    val pa = a.b("paint")
    drawRect(Brush.verticalGradient(listOf(SaplingC12.Wall, SaplingC12.WallLow)))
    // the glasshouse window, in panes
    val wl0 = w * 0.25f
    val wr0 = w * 0.75f
    drawRect(SaplingC12.Glass, topLeft = Offset(wl0, h * 0.06f), size = Size(wr0 - wl0, h * 0.42f))
    for (k in 1 until 5) drawLine(Color.White, Offset(mix(wl0, wr0, k / 5f), h * 0.06f), Offset(mix(wl0, wr0, k / 5f), h * 0.48f), 2f * dp)
    drawLine(Color.White, Offset(wl0, h * 0.27f), Offset(wr0, h * 0.27f), 2f * dp)
    lightShafts(Offset(w * 0.5f, h * 0.1f), PI.toFloat() * 0.5f, 0.6f, h * 0.7f, Color(0xFFFFF6D8), 0.5f, t, 5)
    // the bench
    val top = h * 0.72f
    drawRect(SaplingC12.Bench, topLeft = Offset(0f, top), size = Size(w, h * 0.04f))
    drawRect(SaplingC12.BenchDark, topLeft = Offset(0f, top + h * 0.04f), size = Size(w, h * 0.012f))
    for (x in listOf(0.08f, 0.92f)) drawRect(SaplingC12.BenchDark, topLeft = Offset(w * x - w * 0.012f, top + h * 0.05f), size = Size(w * 0.024f, h - top))
    // the dry one at the end, its root cut through
    val dry = Offset(w * 0.12f, top)
    drawLine(SaplingC12.Dead, dry, dry + Offset(-w * 0.02f, -h * 0.12f), 1.6f * dp, StrokeCap.Round, alpha = 0.8f)
    for (k in 0 until 3) drawLine(SaplingC12.Dead, dry + Offset(-w * 0.01f * k, -h * (0.05f + 0.03f * k)), dry + Offset(-w * (0.04f + 0.01f * k), -h * (0.06f + 0.035f * k)), 1.2f * dp, StrokeCap.Round, alpha = 0.8f)
    drawLine(SaplingC12.Dead, dry, dry + Offset(w * 0.03f, h * 0.012f), 1.2f * dp, alpha = 0.8f)
    drawLine(Color(0xFFB03A2A), dry + Offset(w * 0.008f, -h * 0.01f), dry + Offset(w * 0.016f, h * 0.02f), 1.4f * dp, alpha = 0.8f * seg(rt, 0.3f, 0.6f))

    // the pot, cut away to show the soil and the roots
    val pc = Offset(w * 0.5f, top)
    val potW = w * 0.14f
    val potH = h * 0.16f
    val pot = Path().apply {
        moveTo(pc.x - potW / 2, pc.y - potH); lineTo(pc.x + potW / 2, pc.y - potH); lineTo(pc.x + potW * 0.38f, pc.y); lineTo(pc.x - potW * 0.38f, pc.y); close()
    }
    drawPath(pot, SaplingC12.Pot)
    drawRect(SaplingC12.PotDark, topLeft = Offset(pc.x - potW * 0.55f, pc.y - potH - h * 0.015f), size = Size(potW * 1.1f, h * 0.025f))
    // the cut-away window into the soil
    val soilTl = Offset(pc.x - potW * 0.32f, pc.y - potH * 0.8f)
    val soilSz = Size(potW * 0.64f, potH * 0.7f)
    drawRect(SaplingC12.Soil, topLeft = soilTl, size = soilSz)
    val rootGlow = 0.6f + 0.4f * sin(t * 2f)
    clipRect(soilTl.x, soilTl.y, soilTl.x + soilSz.width, soilTl.y + soilSz.height) {
        for (k in 0 until 6) {
            val ang = PI.toFloat() * (0.25f + 0.1f * k)
            val from = Offset(pc.x, soilTl.y)
            val to = from + Offset(cos(ang) * soilSz.width * 0.6f, sin(ang) * soilSz.height)
            drawLine(SaplingC12.Root, from, to, 1.6f * dp, StrokeCap.Round)
        }
        softGlow(Offset(pc.x, soilTl.y + soilSz.height * 0.4f), soilSz.width * 0.6f, SaplingC12.Root, 0.5f * rootGlow)
    }
    drawPath(pot, SaplingC12.PotDark, style = Stroke(1.4f * dp))
    // the plant: its leaves, some of which wilt and droop when the dark drops fall
    val stemTop = Offset(pc.x, pc.y - potH - h * 0.26f)
    drawLine(SaplingC12.Stem, Offset(pc.x, pc.y - potH), stemTop, 3f * dp, StrokeCap.Round)
    val wilt = easeOut(seg(wl, 0.3f, 0.8f))
    val painted = easeOut(seg(pa, 0.2f, 0.5f)) * (1f - easeOut(seg(pa, 0.6f, 0.95f)))
    for (k in 0 until 8) {
        val u = 0.2f + 0.1f * k
        val at = Offset(pc.x, mix(pc.y - potH, stemTop.y, u))
        val sd = if (k % 2 == 0) -1f else 1f
        val sick = k % 3 != 1
        val droop = if (sick) wilt else 0f
        val ang = sd * (40f + 70f * droop)
        val col = if (sick) lerpColor(lerpColor(SaplingC12.Leaf, SaplingC12.Wilted, wilt), SaplingC12.Paint, painted) else SaplingC12.Leaf
        withTransform({ rotate(ang, at) }) {
            drawOval(col, topLeft = at + Offset(-w * 0.012f, -h * 0.07f), size = Size(w * 0.024f, h * 0.07f))
        }
        if (painted > 0.2f && sick) for (d in 0 until 2) {
            val f = fract(t * 0.8f + k * 0.3f + d * 0.5f)
            drawCircle(SaplingC12.Paint, 1.6f * dp, at + Offset(sd * w * 0.02f, h * 0.04f * f), alpha = painted * (1f - f))
        }
    }
    // the dark drops that fall on it
    if (wl > 0f && wl < 1f) for (k in 0 until 8) {
        val f = fract(t * 0.9f + k / 8f)
        val x = pc.x + (w8Rnd(k, 3.1f) - 0.5f) * w * 0.1f
        drawCircle(SaplingC12.Sin, 2.6f * dp, Offset(x, mix(h * 0.08f, stemTop.y + h * 0.15f, f)), alpha = seg(wl, 0f, 0.2f) * (1f - f * 0.3f))
    }
    // the glass bell over it
    val bell = easeOut(seg(cl, 0.2f, 0.6f))
    if (bell > 0f) {
        val bTop = mix(h * -0.2f, stemTop.y - h * 0.05f, bell)
        val bPath = Path().apply {
            moveTo(pc.x - potW * 0.6f, bTop + h * 0.38f + h * 0.02f)
            lineTo(pc.x - potW * 0.6f, bTop + h * 0.1f)
            quadraticBezierTo(pc.x - potW * 0.6f, bTop, pc.x, bTop)
            quadraticBezierTo(pc.x + potW * 0.6f, bTop, pc.x + potW * 0.6f, bTop + h * 0.1f)
            lineTo(pc.x + potW * 0.6f, bTop + h * 0.38f + h * 0.02f)
        }
        drawPath(bPath, Color.White, alpha = 0.18f * bell)
        drawPath(bPath, Color.White, alpha = 0.8f * bell, style = Stroke(1.4f * dp))
        drawCircle(Color.White, w * 0.008f, Offset(pc.x, bTop - h * 0.01f), alpha = bell)
    }

    val tall = h * 0.4f
    // the gardener: he waters it, sets the bell, steps back with open hands; then stands between the two
    val gFoot = Offset(mix(w * 0.66f, w * 0.7f, easeOut(seg(cl, 0.6f, 0.9f))) - w * 0.1f * easeInOut(seg(ax, 0f, 0.3f)) * (1f - easeInOut(seg(pa, 0f, 0.3f))) + w * 0.15f * easeInOut(seg(pa, 0f, 0.3f)), h * 0.98f)
    val open = easeOut(seg(cl, 0.6f, 0.9f))
    figure(gFoot, tall, SaplingC12.Ink, arms = if (cl in 0.1f..0.6f) 0.9f else 0.2f)
    if (open > 0f) for (sd in listOf(-1f, 1f)) drawLine(SaplingC12.Ink, gFoot + Offset(0f, -tall * 0.76f), gFoot + Offset(sd * tall * 0.2f, -tall * 0.62f), max(1.6f * dp, tall * 0.06f), StrokeCap.Round, alpha = open * (1f - seg(ax, 0f, 0.2f)))
    if (rt > 0.3f && cl <= 0f) {
        // watering it
        val can = gFoot + Offset(-tall * 0.35f, -tall * 0.62f)
        drawRoundRect(Color(0xFF6A7A8A), topLeft = can - Offset(w * 0.02f, h * 0.025f), size = Size(w * 0.04f, h * 0.04f), cornerRadius = CornerRadius(3f * dp))
        drawLine(Color(0xFF6A7A8A), can + Offset(-w * 0.02f, 0f), can + Offset(-w * 0.05f, -h * 0.03f), 2f * dp, StrokeCap.Round)
        for (k in 0 until 5) {
            val f = fract(t * 1.5f + k / 5f)
            drawCircle(Color(0xFF9ED8F2), 1.4f * dp, can + Offset(-w * 0.05f - w * 0.02f * f, -h * 0.03f + h * 0.18f * f), alpha = seg(rt, 0.3f, 0.5f) * (1f - f))
        }
    }
    // the one with the axe, from the left: stopped
    if (ax > 0f) {
        val come = easeInOut(seg(ax, 0f, 0.4f))
        val foot = Offset(mix(-w * 0.08f, w * 0.3f, come), h * 0.98f)
        val fade = 1f - seg(pa, 0.6f, 1f) * 0.6f
        withTransform({ scale(-1f, 1f, pivot = foot) }) { figure(foot, tall, SaplingC12.Ink, alpha = fade, walk = if (come < 1f) t * 6f else 0f, arms = 0.9f, lean = -0.05f) }
        val grip = foot + Offset(tall * 0.28f, -tall * 0.75f)
        val raised = 1f - easeOut(seg(ax, 0.45f, 0.6f)) * 0.3f
        val head = grip + Offset(tall * 0.1f, -tall * 0.35f * raised)
        drawLine(Color(0xFF6A4A2A), grip, head, 2f * dp, StrokeCap.Round, alpha = fade)
        drawPath(Path().apply { moveTo(head.x, head.y); lineTo(head.x + w * 0.03f, head.y - h * 0.02f); lineTo(head.x + w * 0.03f, head.y + h * 0.04f); close() }, SaplingC12.Axe, alpha = fade)
        noSign11pub(head + Offset(w * 0.02f, h * 0.01f), w * 0.04f, easeOut(seg(ax, 0.5f, 0.7f)) * fade)
    }
    // the one with the brush, from the right
    if (pa > 0f) {
        val come = easeInOut(seg(pa, 0f, 0.25f))
        val foot = Offset(mix(w * 1.08f, w * 0.68f, come), h * 0.98f)
        figure(foot, tall, SaplingC12.Ink, walk = if (come < 1f) t * 6f else 0f, arms = 0.9f)
        val brush = foot + Offset(-tall * 0.3f, -tall * 0.75f)
        drawLine(Color(0xFF6A4A2A), brush, brush + Offset(-w * 0.04f, -h * 0.02f), 1.8f * dp, StrokeCap.Round)
        drawCircle(SaplingC12.Paint, 3f * dp, brush + Offset(-w * 0.045f, -h * 0.022f))
        noSign11pub(brush + Offset(-w * 0.03f, -h * 0.01f), w * 0.04f, easeOut(seg(pa, 0.65f, 0.85f)))
        drawRect(Color(0xFF7A7A82), topLeft = foot + Offset(-tall * 0.12f, -tall * 0.25f), size = Size(w * 0.03f, h * 0.04f))
    }
}

/** The red "not this" sign (shared). */
internal fun DrawScope.noSign11pub(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(Color(0xFFD23C30), r, c, alpha = alpha, style = Stroke(r * 0.18f))
    drawLine(Color(0xFFD23C30), c + Offset(-r * 0.7f, -r * 0.7f), c + Offset(r * 0.7f, r * 0.7f), r * 0.18f, alpha = alpha)
}

// ═════════════ 2. return: the door of repentance, open until it closes (tawba) ═════════════

private object ReturnC12 {
    val SkyTop = Color(0xFF2A2A5A)
    val SkyLow = Color(0xFFE8A06A)
    val West = Color(0xFFE85A3A)
    val Ground = Color(0xFF4A3A3A)
    val Road = Color(0xFF7A6458)
    val Frame = Color(0xFFC9A46A)
    val FrameDark = Color(0xFF7A5A34)
    val Door = Color(0xFF5A3D24)
    val Light = Color(0xFFFFF1C8)
    val Ink = Color(0xFF1E1620)
    val Sack = Color(0xFF2A2026)
    val Tear = Color(0xFF9ED8F2)
    val Heart = Color(0xFFFFE6A8)
    val Back = Color(0xE6140E12)
}

/**
 * A road at dusk; at its right end the door of repentance stands open, its light on the road. Beats: opendoor (a
 * man walks away from it to the left with a dark sack on his back), turnquit (he stops and turns toward the door,
 * a warm light in his heart, and lets the sack drop; it melts into smoke), regretresolve (he kneels, head bowed, and
 * tears fall; then he gets up and walks to the door with firm steps), westsun (in the west, where he came from, the
 * sun begins to rise; the door starts to close; he goes in; another, far behind with his sack, starts to run),
 * doorshut (the door shuts; the sky darkens; the latecomer reaches it closed).
 */
internal fun DrawScope.returnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "opendoor" to Shot(Offset(w * 0.55f, h * 0.55f), 1.1f),
        "turnquit" to Shot(Offset(w * 0.35f, h * 0.6f), 1.45f),
        "regretresolve" to Shot(Offset(w * 0.5f, h * 0.6f), 1.25f),
        "westsun" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "doorshut" to Shot(Offset(w * 0.72f, h * 0.55f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { returnRoad(a) }
}

private fun DrawScope.returnRoad(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val od = a.b("opendoor")
    val tq = a.b("turnquit")
    val rr = a.b("regretresolve")
    val ws = a.b("westsun")
    val ds = a.b("doorshut")
    val sunUp = easeOut(seg(ws, 0.1f, 0.9f))
    val dark = easeOut(seg(ds, 0.2f, 1f))
    drawRect(Brush.verticalGradient(listOf(lerpColor(ReturnC12.SkyTop, Color(0xFF3A1010), dark), lerpColor(ReturnC12.SkyLow, Color(0xFF8A2A1A), 0.5f * sunUp + 0.4f * dark)), endY = h * 0.7f), size = Size(w, h * 0.7f))
    for (k in 0 until 20) drawCircle(Color.White, (0.5f + 0.7f * w8Rnd(k, 2.9f)) * dp, Offset(w8Rnd(k, 11.7f) * w, w8Rnd(k, 33.3f) * h * 0.35f), alpha = 0.35f * (1f - sunUp))
    // the sun, rising in the west (on the left, where the road comes from)
    if (ws > 0f) {
        val sun = Offset(w * 0.12f, mix(h * 0.74f, h * 0.5f, sunUp))
        softGlow(sun, w * 0.16f, ReturnC12.West, 0.8f * seg(ws, 0f, 0.3f))
        drawCircle(ReturnC12.West, w * 0.05f, sun, alpha = seg(ws, 0f, 0.3f))
    }
    hills(Color(0xFF3A2E3A), h * 0.7f, h * 0.03f, 0.02f, 0.05f, 2.3f)
    drawRect(ReturnC12.Ground, topLeft = Offset(0f, h * 0.7f), size = Size(w, h * 0.3f))
    // the road, from the west to the door
    val roadY = h * 0.86f
    drawRect(ReturnC12.Road, topLeft = Offset(0f, roadY - h * 0.04f), size = Size(w, h * 0.08f))
    // the door of repentance on the right, labelled
    val gx = w * 0.84f
    val ground = roadY - h * 0.04f
    val j = w * 0.06f
    val r = j * 1.2f
    val cy = ground - h * 0.3f
    val open = (1f - easeInOut(seg(ws, 0.4f, 1f)) * 0.8f) * (1f - easeOut(seg(ds, 0f, 0.4f))).let { max(it, 0f) }
    val openNow = if (ds > 0.4f) 0f else open
    val frame = horseshoe(gx, cy, r + w * 0.016f, j + w * 0.016f, ground)
    val opening = horseshoe(gx, cy, r, j, ground)
    softGlow(Offset(gx, cy + r * 0.5f), w * 0.2f, ReturnC12.Light, 0.6f * openNow)
    lightShafts(Offset(gx, cy + r * 0.4f), PI.toFloat() * 0.92f, 0.5f, w * 0.6f, ReturnC12.Light, 0.7f * openNow, t, 6)
    drawPath(frame, ReturnC12.Frame)
    drawPath(frame, ReturnC12.FrameDark, style = Stroke(1.4f * dp))
    drawPath(opening, lerpColor(Color(0xFF14101C), ReturnC12.Light, openNow))
    clipRect(gx - j, 0f, gx + j, ground) {
        val leaf = j * (1f - 0.9f * openNow)
        for (sd in listOf(-1f, 1f)) {
            val hinge = gx + sd * j
            val inner = hinge - sd * leaf
            drawRect(ReturnC12.Door, topLeft = Offset(minOf(hinge, inner), cy - r * 1.3f), size = Size(leaf, ground - cy + r * 1.3f))
        }
    }
    tagAt(a.tag("opendoor"), Offset(gx, cy - r - h * 0.07f), seg(od, 0.1f, 0.3f), back = ReturnC12.Back, ink = ArtInk.Gold)

    val tall = h * 0.3f
    // the man: away with his sack; turns and drops it; kneels and weeps; gets up and walks to the door; goes in
    val away = easeInOut(seg(od, 0.1f, 0.8f))
    val turned = tq > 0.1f
    val drop = easeIn12(seg(tq, 0.45f, 0.7f))
    val kneel = easeOut(seg(rr, 0f, 0.2f)) * (1f - easeOut(seg(rr, 0.45f, 0.6f)))
    val toDoor = easeInOut(seg(rr, 0.55f, 1f))
    val enter = easeInOut(seg(ws, 0.1f, 0.4f))
    val x = when {
        ws > 0f -> mix(w * 0.72f, gx, enter)
        rr > 0f -> mix(w * 0.3f, w * 0.72f, toDoor)
        else -> mix(w * 0.68f, w * 0.3f, away)
    }
    val foot = Offset(x, roadY)
    val inside = enter >= 1f
    if (!inside) {
        val alpha = 1f - seg(ws, 0.3f, 0.4f)
        if (kneel > 0.5f) {
            w8Seated(foot, tall * 0.95f, ReturnC12.Ink, facing = 1f, alpha = alpha)
            for (k in 0 until 3) {
                val f = fract(t * 0.9f + k / 3f)
                drawCircle(ReturnC12.Tear, 1.6f * dp, foot + Offset(tall * 0.09f, -tall * 0.6f + tall * 0.4f * f), alpha = (1f - f) * alpha)
            }
        } else {
            val moving = (od in 0.11f..0.79f) || (rr in 0.56f..0.99f) || (ws in 0.11f..0.39f)
            withTransform({ scale(if (turned) -1f else 1f, 1f, pivot = foot) }) {
                figure(foot, tall, ReturnC12.Ink, alpha = alpha, walk = if (moving) t * 6f else 0f, lean = if (!turned) 0.18f else 0f, arms = if (!turned) -0.4f else 0.2f)
            }
        }
        // the heart that turns to the door
        if (turned) {
            val hc = foot + Offset(0f, -tall * (if (kneel > 0.5f) 0.42f else 0.62f))
            softGlow(hc, tall * 0.12f, ReturnC12.Heart, 0.8f * seg(tq, 0.1f, 0.4f) * alpha)
            drawPath(w8Heart(hc, tall * 0.05f), ReturnC12.Heart, alpha = seg(tq, 0.1f, 0.4f) * alpha)
        }
    }
    // the sack: on his back, then dropped, melting into smoke
    if (od > 0f) {
        val onBack = 1f - drop
        val sackC = if (turned) foot + Offset(-tall * 0.25f - w * 0.04f * drop, -tall * (0.62f - 0.58f * drop)) else foot + Offset(tall * 0.12f, -tall * 0.68f)
        val melt = seg(tq, 0.7f, 1f)
        if (melt < 1f && !(ws > 0f) ) {
            drawOval(ReturnC12.Sack, topLeft = sackC - Offset(tall * 0.16f, tall * 0.13f), size = Size(tall * 0.32f, tall * 0.26f), alpha = 1f - melt)
            drawLine(ReturnC12.Sack, sackC + Offset(0f, -tall * 0.13f), sackC + Offset(0f, -tall * 0.2f), 2f * dp, alpha = (1f - melt) * onBack.coerceAtLeast(0.3f))
        }
        if (melt > 0f) for (k in 0 until 6) {
            val f = fract(t * 0.5f + k / 6f)
            drawCircle(Color(0xFF4A4250), tall * (0.05f + 0.08f * f), Offset(w * 0.26f + (k - 2.5f) * w * 0.01f, roadY - tall * 0.1f - tall * 0.6f * f), alpha = 0.5f * (1f - f) * (1f - seg(rr, 0.3f, 0.6f)))
        }
    }
    // the one who waited too long: far back, with his sack, then running; the door shut before him
    if (ws > 0.3f) {
        val run = easeIn12(seg(ws, 0.4f, 1f)) * 0.7f + easeOut(seg(ds, 0f, 0.6f)) * 0.3f
        val lf = Offset(mix(w * 0.04f, gx - j * 1.6f, run), roadY)
        withTransform({ scale(-1f, 1f, pivot = lf) }) { figure(lf, tall * 0.95f, ReturnC12.Ink, walk = if (run < 1f) t * 9f else 0f, arms = if (run >= 1f) 0.9f else 0.3f, stride = 1.3f) }
        drawOval(ReturnC12.Sack, topLeft = lf + Offset(-tall * 0.28f, -tall * 0.8f), size = Size(tall * 0.28f, tall * 0.24f))
    }
}

private fun easeIn12(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }
