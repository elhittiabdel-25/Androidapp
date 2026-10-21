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
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 15, round 8 of the redraw (2026-10-21): «أعمال القلوب». The threads of a man's heart, their thickness its
 * love; a carpet whose back is a tangle and whose face is a pattern (thinking well of Allah); and Ibn al-Qayyim's
 * bird, whose head is love and whose wings are fear and hope.
 */

// ═════════════ 1. threads: what the heart loves, and how much (mahabba) ═════════════

private object ThreadC15 {
    val SkyTop = Color(0xFF8EC0E4)
    val SkyLow = Color(0xFFF6E6C4)
    val Hill = Color(0xFFC8B07A)
    val Ground = Color(0xFFD8C08E)
    val Ink = Color(0xFF2A2430)
    val Other = Color(0xFF6A6078)
    val Stone = Color(0xFFB0A898)
    val StoneDark = Color(0xFF7A7266)
    val Heart = Color(0xFFFFD27A)
    val Thread = Color(0xFFF2C45A)
    val House = Color(0xFFE6D6B4)
    val Roof = Color(0xFFA0583A)
}

/** A thread of love from [from] to [to], [wd] thick, sagging a little, with sparks running along it. */
private fun DrawScope.loveThread(from: Offset, to: Offset, wd: Float, alpha: Float, t: Float, sag: Float = 0.08f) {
    if (alpha <= 0.01f || wd <= 0.1f) return
    val dp = density
    val mid = (from + to) / 2f + Offset(0f, (to - from).getDistance() * sag)
    val p = Path().apply { moveTo(from.x, from.y); quadraticTo(mid.x, mid.y, to.x, to.y) }
    drawPath(p, ThreadC15.Thread.copy(alpha = 0.25f * alpha), style = Stroke(wd * 3f, cap = StrokeCap.Round))
    drawPath(p, ThreadC15.Thread.copy(alpha = alpha), style = Stroke(wd, cap = StrokeCap.Round))
    val n = 3
    for (k in 0 until n) {
        val u = fract(t * 0.5f + k / n.toFloat())
        val a = (1 - u) * (1 - u)
        val b = 2 * (1 - u) * u
        val c = u * u
        val q = Offset(from.x * a + mid.x * b + to.x * c, from.y * a + mid.y * b + to.y * c)
        drawCircle(Color.White, max(1.2f * dp, wd * 0.6f), q, alpha = alpha * 0.8f)
    }
}

/**
 * An open plain in the afternoon; a man with a glowing heart. Beats: pedestal (on his right a pedestal rises with
 * one of the creatures on it; a thread from his heart to it swells as thick as it can, he bows to it, and a red sign
 * comes over it: a rival), putfirst (the pedestal sinks and the other stands on the ground like anyone; that thread
 * thins; he stands and lifts his hands, and his thickest thread runs straight up out of the picture, his house and
 * his goods held by thin ones), beloved (his ﷺ light comes with his name, and a thick thread runs to it), family
 * (his child at his side, his father with a stick, people behind: each held by a thread, all thinner than that one).
 */
internal fun DrawScope.threadsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "pedestal" to Shot(Offset(w * 0.58f, h * 0.55f), 1.2f),
        "putfirst" to Shot(Offset(w * 0.45f, h * 0.45f), 1.1f),
        "beloved" to Shot(Offset(w * 0.6f, h * 0.45f), 1.1f),
        "family" to Shot(Offset(w * 0.45f, h * 0.52f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { threadsPlain(a) }
}

private fun DrawScope.threadsPlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pd = a.b("pedestal")
    val pf = a.b("putfirst")
    val bl = a.b("beloved")
    val fm = a.b("family")
    val ground = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(ThreadC15.SkyTop, ThreadC15.SkyLow), endY = ground), size = Size(w, ground))
    hills(ThreadC15.Hill, ground - h * 0.03f, h * 0.03f, 2.3f, 5.1f, 0.4f)
    drawRect(ThreadC15.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))

    val tall = h * 0.36f
    val mx = w * 0.44f
    val foot = Offset(mx, ground + h * 0.04f)
    // his house and his goods on the left, held by thin threads once he has put his love in order
    val goods = easeOut(seg(pf, 0.3f, 0.6f))
    val house = Offset(w * 0.13f, ground + h * 0.01f)
    if (goods > 0f) {
        drawRect(ThreadC15.House, topLeft = Offset(house.x - w * 0.06f, house.y - h * 0.14f), size = Size(w * 0.12f, h * 0.14f), alpha = goods)
        drawPath(Path().apply { moveTo(house.x - w * 0.07f, house.y - h * 0.14f); lineTo(house.x, house.y - h * 0.22f); lineTo(house.x + w * 0.07f, house.y - h * 0.14f); close() }, ThreadC15.Roof.copy(alpha = goods))
        drawRect(ThreadC15.Ink, topLeft = Offset(house.x - w * 0.012f, house.y - h * 0.07f), size = Size(w * 0.024f, h * 0.07f), alpha = goods)
        drawOval(Color(0xFFB08A50), topLeft = Offset(house.x + w * 0.08f, house.y - h * 0.06f), size = Size(w * 0.06f, h * 0.07f), alpha = goods)
    }

    // the pedestal on his right and the one set up on it
    val rise = easeOut(seg(pd, 0f, 0.3f)) * (1f - easeInOut(seg(pf, 0f, 0.4f)))
    val px = w * 0.74f
    val pedH = h * 0.18f * rise
    if (pd > 0f) {
        if (pedH > 1f) {
            drawRect(ThreadC15.Stone, topLeft = Offset(px - w * 0.06f, ground + h * 0.03f - pedH), size = Size(w * 0.12f, pedH))
            drawRect(ThreadC15.StoneDark, topLeft = Offset(px - w * 0.065f, ground + h * 0.03f - pedH), size = Size(w * 0.13f, h * 0.015f))
        }
        figure(Offset(px, ground + h * 0.03f - pedH), tall * 0.9f, ThreadC15.Other, seg(pd, 0f, 0.15f))
    }
    val other = Offset(px, ground + h * 0.03f - pedH - tall * 0.55f)

    // the man: bows to it, then stands and lifts his hands
    val bow = easeOut(seg(pd, 0.3f, 0.55f)) * (1f - easeOut(seg(pf, 0f, 0.25f)))
    val lift = easeOut(seg(pf, 0.15f, 0.4f)) * (1f - easeOut(seg(bl, 0.2f, 0.5f)) * 0.7f)
    val heart: Offset
    if (bow > 0.3f) {
        withTransform({ scale(-1f, 1f, pivot = foot) }) { w8Worshipper(foot, tall, ThreadC15.Ink, 0f, min(1f, bow)) }
        heart = foot + Offset(tall * 0.14f * bow, -tall * (0.66f - 0.12f * bow))
    } else {
        figure(foot, tall, ThreadC15.Ink, arms = 0.15f)
        if (lift > 0f) for (sd in listOf(-1f, 1f)) {
            drawLine(ThreadC15.Ink, foot + Offset(0f, -tall * 0.76f), foot + Offset(sd * tall * 0.2f, -tall * (0.76f + 0.22f * lift)), max(1.7f * dp, tall * 0.06f), StrokeCap.Round, alpha = lift)
        }
        heart = foot + Offset(0f, -tall * 0.62f)
    }
    softGlow(heart, tall * 0.2f, ThreadC15.Heart, 0.8f)
    drawPath(w8Heart(heart, tall * 0.055f), ThreadC15.Heart)

    // the threads, their thickness the love
    val base = 1.2f * dp
    val toOther = if (pd > 0f) mix(base, 7f * dp, easeOut(seg(pd, 0.1f, 0.45f))) * (1f - 0.8f * easeOut(seg(pf, 0f, 0.4f))) else 0f
    loveThread(heart, other, toOther, seg(pd, 0.05f, 0.2f), t)
    if (pd > 0f) noSign11pub(other + Offset(0f, -tall * 0.05f), h * 0.11f, easeOut(seg(pd, 0.6f, 0.85f)) * (1f - easeOut(seg(pf, 0f, 0.3f))))
    // the first love: straight up, out of the picture, to nothing drawn
    val up = easeOut(seg(pf, 0.2f, 0.6f))
    if (up > 0f) {
        val top = Offset(heart.x, mix(heart.y, -h * 0.1f, up))
        drawLine(ThreadC15.Thread, heart, top, 16f * dp, StrokeCap.Round, alpha = 0.2f)
        drawLine(ThreadC15.Thread, heart, top, 6f * dp, StrokeCap.Round)
        for (k in 0 until 4) {
            val f = fract(t * 0.7f + k / 4f)
            drawCircle(Color.White, 2.6f * dp, Offset(heart.x, mix(heart.y, top.y, f)), alpha = 0.9f * (1f - f))
        }
    }
    loveThread(heart, house + Offset(0f, -h * 0.1f), base, goods, t, 0.15f)
    loveThread(heart, house + Offset(w * 0.11f, -h * 0.03f), base, goods, t, 0.15f)

    // his ﷺ light, with a thick thread
    if (bl > 0f) {
        val lc = Offset(w * 0.78f, h * 0.3f)
        sirajMunir(lc, 4.6f * dp, easeOut(seg(bl, 0f, 0.3f)), t, nameAt = lc + Offset(0f, h * 0.12f))
        loveThread(heart, lc, 4.6f * dp * easeOut(seg(bl, 0.2f, 0.6f)), seg(bl, 0.2f, 0.4f), t, 0.1f)
    }
    // his child, his father, and the people: each held, all by thinner threads
    if (fm > 0f) {
        val come = easeOut(seg(fm, 0f, 0.35f))
        val child = Offset(mx - w * 0.08f, foot.y)
        figure(child, tall * 0.55f, ThreadC15.Ink, come)
        val father = Offset(mx - w * 0.17f, foot.y)
        figure(father, tall * 0.92f, ThreadC15.Ink, come, lean = 0.12f)
        drawLine(Color(0xFF6A4A2A), father + Offset(-tall * 0.2f, 0f), father + Offset(-tall * 0.15f, -tall * 0.55f), 1.8f * dp, StrokeCap.Round, alpha = come)
        for (k in 0 until 4) figure(Offset(w * (0.04f + 0.05f * k), foot.y + h * 0.04f), tall * 0.75f, ThreadC15.Other, come * 0.9f)
        val th = 2.2f * dp * easeOut(seg(fm, 0.2f, 0.6f))
        loveThread(heart, child + Offset(0f, -tall * 0.38f), th, come, t, 0.2f)
        loveThread(heart, father + Offset(0f, -tall * 0.6f), th, come, t, 0.2f)
        loveThread(heart, Offset(w * 0.12f, foot.y + h * 0.04f - tall * 0.5f), th * 0.8f, come, t, 0.2f)
    }
}

// ═════════════ 2. carpet: its back a tangle, its face a pattern (husn-zann) ═════════════

private object CarpetC15 {
    val Wall = Color(0xFFEAD9B6)
    val WallLow = Color(0xFFD4BE92)
    val Floor = Color(0xFFBE9C6E)
    val Pole = Color(0xFF6A4A2A)
    val Rope = Color(0xFF9A7A4A)
    val Field = Color(0xFF8A1E2A)
    val Border = Color(0xFF1E2A5A)
    val Gold = Color(0xFFE8B84A)
    val Ivory = Color(0xFFF4E8CC)
    val Jade = Color(0xFF2E7A6A)
    val Back = Color(0xFF8A6A5A)
    val Knot = Color(0xFF4A3A34)
    val Ink = Color(0xFF2A2430)
    val Cloud = Color(0xFF4A4452)
}

/** The carpet's face: a border, a field, a medallion, corner stars. */
private fun DrawScope.carpetFace(l: Float, tp: Float, cw: Float, ch: Float, glow: Float) {
    val dp = density
    drawRect(CarpetC15.Border, topLeft = Offset(l, tp), size = Size(cw, ch))
    val inset = cw * 0.08f
    drawRect(CarpetC15.Field, topLeft = Offset(l + inset, tp + inset), size = Size(cw - 2 * inset, ch - 2 * inset))
    drawRect(CarpetC15.Gold, topLeft = Offset(l + inset * 0.5f, tp + inset * 0.5f), size = Size(cw - inset, ch - inset), style = Stroke(1.2f * dp))
    // little diamonds along the border
    val n = 8
    for (k in 0 until n) {
        for (y in listOf(tp + inset * 0.25f, tp + ch - inset * 0.75f)) {
            val x = l + inset + (cw - 2 * inset) * (k + 0.5f) / n
            drawPath(Path().apply { moveTo(x, y); lineTo(x + inset * 0.25f, y + inset * 0.25f); lineTo(x, y + inset * 0.5f); lineTo(x - inset * 0.25f, y + inset * 0.25f); close() }, CarpetC15.Ivory)
        }
    }
    val c = Offset(l + cw / 2, tp + ch / 2)
    if (glow > 0f) softGlow(c, cw * 0.45f, CarpetC15.Gold, 0.5f * glow)
    drawPath(starPath(c, cw * 0.2f), CarpetC15.Gold)
    drawPath(starPath(c, cw * 0.13f), CarpetC15.Border)
    drawPath(starPath(c, cw * 0.07f), CarpetC15.Jade)
    for (sx in listOf(-1f, 1f)) for (sy in listOf(-1f, 1f)) {
        val p = c + Offset(sx * (cw / 2 - inset * 2.2f), sy * (ch / 2 - inset * 2.2f))
        drawPath(starPath(p, cw * 0.06f), CarpetC15.Gold)
    }
    for (sx in listOf(-1f, 1f)) {
        val p = c + Offset(sx * cw * 0.27f, 0f)
        drawPath(Path().apply { moveTo(p.x, p.y - ch * 0.1f); lineTo(p.x + cw * 0.05f, p.y); lineTo(p.x, p.y + ch * 0.1f); lineTo(p.x - cw * 0.05f, p.y); close() }, CarpetC15.Ivory)
    }
}

/** The carpet's back: the same shapes dull and blurred, crossed by knots and loose ends. */
private fun DrawScope.carpetBack(l: Float, tp: Float, cw: Float, ch: Float, t: Float) {
    val dp = density
    drawRect(CarpetC15.Back, topLeft = Offset(l, tp), size = Size(cw, ch))
    val c = Offset(l + cw / 2, tp + ch / 2)
    drawPath(starPath(c, cw * 0.2f), Color(0xFF9A7A60))
    for (k in 0 until 26) {
        val p0 = Offset(l + cw * w8Rnd(k, 3.1f), tp + ch * w8Rnd(k, 7.9f))
        val p1 = p0 + Offset(cw * (w8Rnd(k, 1.7f) - 0.5f) * 0.4f, ch * (w8Rnd(k, 5.3f) - 0.5f) * 0.3f)
        val p2 = p1 + Offset(cw * (w8Rnd(k, 2.9f) - 0.5f) * 0.3f, ch * (w8Rnd(k, 9.1f) - 0.5f) * 0.3f)
        val col = listOf(CarpetC15.Knot, Color(0xFFB0404A), Color(0xFF3A4A7A), Color(0xFFC8A050))[k % 4]
        drawPath(Path().apply { moveTo(p0.x, p0.y); quadraticTo(p1.x, p1.y, p2.x, p2.y) }, col.copy(alpha = 0.85f), style = Stroke(1.4f * dp, cap = StrokeCap.Round))
        drawCircle(CarpetC15.Knot, 1.8f * dp, p0)
    }
    // loose ends hanging from the bottom
    for (k in 0 until 9) {
        val x = l + cw * (0.06f + 0.11f * k)
        val len = ch * (0.06f + 0.08f * w8Rnd(k, 4.4f))
        drawLine(listOf(Color(0xFFB0404A), Color(0xFF3A4A7A), CarpetC15.Knot)[k % 3], Offset(x, tp + ch), Offset(x + 2f * dp * sin(t * 1.3f + k), tp + ch + len), 1.2f * dp, StrokeCap.Round)
    }
}

/**
 * A courtyard by day: a carpet hangs on a line, its back to us, a man before it. Beats: backside (he comes up and
 * looks at it: a tangle of knots and loose ends; he tilts his head: what is this?), despair (he sits down, his head in
 * his hand, a dark cloud over him), turnover (the carpet turns round on its line: its face is a pattern, every
 * knot in its place; the cloud breaks up), hamd (he stands and lifts his hands in praise; the medallion shines).
 */
internal fun DrawScope.carpetScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "backside" to Shot(Offset(w * 0.5f, h * 0.48f), 1.15f),
        "despair" to Shot(Offset(w * 0.3f, h * 0.58f), 1.35f),
        "turnover" to Shot(Offset(w * 0.6f, h * 0.45f), 1.15f),
        "hamd" to Shot(Offset(w * 0.5f, h * 0.48f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { carpetYard(a) }
}

private fun DrawScope.carpetYard(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bs = a.b("backside")
    val ds = a.b("despair")
    val to = a.b("turnover")
    val hm = a.b("hamd")
    val ground = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(CarpetC15.Wall, CarpetC15.WallLow), endY = ground), size = Size(w, ground))
    drawRect(CarpetC15.Floor, topLeft = Offset(0f, ground), size = Size(w, h - ground))
    for (k in 0 until 7) drawLine(Color(0xFFA8865A), Offset(w * k / 6f, ground), Offset(w * (k / 6f - 0.5f) * 1.3f + w * 0.5f, h), 0.8f * dp)
    // the line and its two poles
    val lineY = h * 0.14f
    for (x in listOf(0.38f, 0.94f)) drawLine(CarpetC15.Pole, Offset(w * x, lineY - h * 0.02f), Offset(w * x, ground), 3f * dp, StrokeCap.Round)
    drawLine(CarpetC15.Rope, Offset(w * 0.38f, lineY), Offset(w * 0.94f, lineY), 1.4f * dp)
    // the carpet, turning round on its line
    val l0 = w * 0.44f
    val cw = w * 0.44f
    val ch = h * 0.56f
    val turn = easeInOut(seg(to, 0.05f, 0.65f))
    val sx = abs(cos(PI.toFloat() * turn))
    val front = turn > 0.5f
    val cx = l0 + cw / 2
    val sway = sin(t * 0.8f) * 0.6f
    withTransform({ rotate(sway, Offset(cx, lineY)); scale(max(0.02f, sx), 1f, pivot = Offset(cx, lineY)) }) {
        drawRect(Color.Black, topLeft = Offset(l0 + 3f * dp, lineY + 4f * dp), size = Size(cw, ch), alpha = 0.18f)
        if (front) carpetFace(l0, lineY, cw, ch, easeOut(seg(hm, 0.2f, 0.7f))) else carpetBack(l0, lineY, cw, ch, t)
        // shade as it turns edge-on
        drawRect(Color.Black, topLeft = Offset(l0, lineY), size = Size(cw, ch), alpha = 0.35f * (1f - sx))
    }
    for (k in 0 until 4) drawCircle(CarpetC15.Pole, 2f * dp, Offset(l0 + cw * (0.1f + 0.27f * k), lineY))

    // the man
    val tall = h * 0.36f
    val come = easeInOut(seg(bs, 0f, 0.45f))
    val x = mix(w * 0.02f, w * 0.27f, come)
    val foot = Offset(x, ground + h * 0.04f)
    val sit = easeOut(seg(ds, 0.1f, 0.3f)) * (1f - easeOut(seg(to, 0.5f, 0.8f)))
    val stand = 1f - sit
    val praise = easeOut(seg(hm, 0.05f, 0.35f))
    if (sit > 0.5f) {
        val seat = foot + Offset(0f, -h * 0.01f)
        w8Seated(seat, tall, CarpetC15.Ink, 1f)
        // his head sunk in his hand
        val head = seat + Offset(-tall * 0.02f, -tall * 0.53f)
        drawLine(CarpetC15.Ink, seat + Offset(tall * 0.2f, -tall * 0.1f), head + Offset(tall * 0.08f, tall * 0.06f), max(1.7f * dp, tall * 0.06f), StrokeCap.Round)
    } else {
        figure(foot, tall, CarpetC15.Ink, stand, walk = if (come in 0.01f..0.99f) t * 6f else 0f, lean = if (bs > 0.5f && to <= 0f) -0.1f else 0f)
        if (praise > 0f) for (sd in listOf(-1f, 1f)) {
            drawLine(CarpetC15.Ink, foot + Offset(0f, -tall * 0.76f), foot + Offset(sd * tall * 0.2f, -tall * (0.76f + 0.24f * praise)), max(1.7f * dp, tall * 0.06f), StrokeCap.Round, alpha = praise)
        }
    }
    // what is this?
    val puzzled = seg(bs, 0.5f, 0.7f) * (1f - seg(ds, 0f, 0.2f))
    qMark14(foot + Offset(tall * 0.12f, -tall * 1.25f), tall * 0.2f, CarpetC15.Ink, puzzled)
    // the dark cloud of his thought, breaking up when the face is seen
    val cloud = easeOut(seg(ds, 0.15f, 0.5f)) * (1f - easeOut(seg(to, 0.4f, 0.8f)))
    if (cloud > 0f) {
        val cc = foot + Offset(0f, -tall * 1.15f)
        for (k in 0 until 5) {
            val ang = k * 1.256f + t * 0.4f
            val spread = 1f + 0.8f * seg(to, 0.4f, 0.8f)
            drawCircle(CarpetC15.Cloud, tall * (0.1f + 0.03f * (k % 2)), cc + Offset(cos(ang) * tall * 0.13f * spread, sin(ang) * tall * 0.05f * spread), alpha = 0.85f * cloud)
        }
        for (m in 0 until 6) {
            val f = fract(t * 1.4f + m / 6f)
            val x0 = cc.x + (m - 2.5f) * tall * 0.05f
            drawLine(Color(0xFF6A7A9A), Offset(x0, cc.y + tall * 0.08f + f * tall * 0.3f), Offset(x0 - 1.5f * dp, cc.y + tall * 0.12f + f * tall * 0.3f), 1.1f * dp, alpha = cloud * (1f - f))
        }
    }
    // praise: sparks of light round him
    if (praise > 0f) {
        for (k in 0 until 6) {
            val ang = k * 1.047f + t * 0.6f
            val p = foot + Offset(cos(ang) * tall * 0.32f, -tall * 0.75f + sin(ang) * tall * 0.3f)
            glint(p, 6f * dp, CarpetC15.Gold, praise * (0.5f + 0.5f * sin(t * 3f + k)))
        }
    }
}

// ═════════════ 3. bird: love its head, fear and hope its wings (khawf-raja) ═════════════

private object BirdC15 {
    val SkyTop = Color(0xFF5A9AD6)
    val SkyLow = Color(0xFFE6F0F4)
    val Cloud = Color(0xFFFFFFFF)
    val Body = Color(0xFFF4F0E6)
    val BodyEdge = Color(0xFF8A8478)
    val Fear = Color(0xFF2E4A8A)
    val Hope = Color(0xFF2E8A5A)
    val Love = Color(0xFFD8584A)
    val Beak = Color(0xFFE8B04A)
    val Ghost = Color(0xFF6A7A8A)
    val Back = Color(0xE6141826)
    val Hill = Color(0xFF8AAE7A)
}

/** One wing, spread from [root] to the side [sd] (-1 left, 1 right), flapped by [flap] (-1..1). */
private fun DrawScope.wing15(root: Offset, span: Float, sd: Float, flap: Float, color: Color, alpha: Float, ghost: Boolean) {
    if (alpha <= 0.01f) return
    val dp = density
    val lift = -flap * span * 0.35f
    val tip = root + Offset(sd * span, lift - span * 0.15f)
    val p = Path().apply {
        moveTo(root.x, root.y - span * 0.08f)
        quadraticTo(root.x + sd * span * 0.5f, root.y + lift * 0.5f - span * 0.35f, tip.x, tip.y)
        // the feathers along its trailing edge
        for (k in 1..4) {
            val u = 1f - k / 4f
            val e = root + Offset(sd * span * u, lift * u - span * 0.15f * u + span * 0.2f)
            lineTo(e.x + sd * span * 0.06f, e.y - span * 0.06f)
            lineTo(e.x, e.y)
        }
        lineTo(root.x, root.y + span * 0.1f)
        close()
    }
    if (ghost) {
        drawPath(p, BirdC15.Ghost.copy(alpha = 0.8f * alpha), style = Stroke(1.4f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))))
    } else {
        drawPath(p, color.copy(alpha = alpha))
        drawPath(p, Color.White.copy(alpha = 0.35f * alpha), style = Stroke(1f * dp))
    }
}

/** The bird at [c], [s] its size; [fear], [hope], [love] 0..1 for each part present (0 shows its outline only). */
private fun DrawScope.bird15(c: Offset, s: Float, flap: Float, fear: Float, hope: Float, love: Float, show: Float, tilt: Float) {
    if (show <= 0.01f) return
    val dp = density
    withTransform({ rotate(tilt, c) }) {
        // fear on its left, hope on its right (as we see it)
        wing15(c + Offset(-s * 0.12f, -s * 0.05f), s * 0.9f * max(0.6f, fear), -1f, flap, BirdC15.Fear, show * (if (fear > 0f) fear else 1f), fear <= 0f)
        wing15(c + Offset(s * 0.12f, -s * 0.05f), s * 0.9f * max(0.6f, hope), 1f, flap, BirdC15.Hope, show * (if (hope > 0f) hope else 1f), hope <= 0f)
        // the body and its tail
        drawPath(Path().apply { moveTo(c.x - s * 0.08f, c.y + s * 0.3f); lineTo(c.x, c.y + s * 0.5f); lineTo(c.x + s * 0.08f, c.y + s * 0.3f); close() }, BirdC15.Body.copy(alpha = show))
        drawOval(BirdC15.Body.copy(alpha = show), topLeft = c - Offset(s * 0.14f, s * 0.2f), size = Size(s * 0.28f, s * 0.55f))
        drawOval(BirdC15.BodyEdge.copy(alpha = 0.6f * show), topLeft = c - Offset(s * 0.14f, s * 0.2f), size = Size(s * 0.28f, s * 0.55f), style = Stroke(1f * dp))
        // its head: love
        val hc = c + Offset(0f, -s * 0.3f)
        if (love > 0f) {
            softGlow(hc, s * 0.3f, BirdC15.Love, 0.6f * love * show)
            drawCircle(BirdC15.Love, s * 0.13f * (0.6f + 0.4f * love), hc, alpha = show * love)
            drawPath(Path().apply { moveTo(hc.x - s * 0.04f, hc.y - s * 0.02f); lineTo(hc.x, hc.y - s * 0.2f); lineTo(hc.x + s * 0.04f, hc.y - s * 0.02f); close() }, BirdC15.Beak.copy(alpha = show * love))
            drawCircle(Color.White, s * 0.022f, hc + Offset(-s * 0.05f, -s * 0.02f), alpha = show * love)
            drawCircle(Color.White, s * 0.022f, hc + Offset(s * 0.05f, -s * 0.02f), alpha = show * love)
        } else {
            drawCircle(BirdC15.Ghost, s * 0.13f, hc, alpha = 0.8f * show, style = Stroke(1.4f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))))
        }
    }
}

/**
 * A wide sky by day over far hills. Beats: wingfear (a bird's left wing spreads: fear), winghope (its right wing:
 * hope), headlove (its head: love; it beats its wings and rises), loveonly (it is made again with its head alone,
 * its wings only outlines, and it drops), fearonly (with one wing only, fear: it spins down), hopeonly (with the
 * other only, hope: it spins down the other way), soar (all three together: it climbs high into the light).
 */
internal fun DrawScope.birdScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "wingfear" to Shot(Offset(w * 0.4f, h * 0.48f), 1.3f),
        "winghope" to Shot(Offset(w * 0.6f, h * 0.48f), 1.3f),
        "headlove" to Shot(Offset(w * 0.5f, h * 0.42f), 1.15f),
        "loveonly" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "fearonly" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "hopeonly" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "soar" to Shot(Offset(w * 0.5f, h * 0.4f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { birdSky(a) }
}

private fun DrawScope.birdSky(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val wf = a.b("wingfear")
    val wh = a.b("winghope")
    val hl = a.b("headlove")
    val lo = a.b("loveonly")
    val fo = a.b("fearonly")
    val ho = a.b("hopeonly")
    val so = a.b("soar")
    drawRect(Brush.verticalGradient(listOf(BirdC15.SkyTop, BirdC15.SkyLow), endY = h))
    val sun = easeOut(seg(so, 0.2f, 0.8f))
    if (sun > 0f) {
        lightShafts(Offset(w * 0.5f, -h * 0.05f), PI.toFloat() / 2f, 1.2f, h * 0.9f, Color(0xFFFFF4D0), sun, t, 8)
    }
    for (k in 0 until 4) {
        val cx = w * fract(0.1f + 0.3f * k + t * 0.01f * (1 + k % 2)) * 1.3f - w * 0.15f
        val cy = h * (0.15f + 0.18f * k)
        for ((dx, r) in listOf(-0.04f to 0.03f, 0f to 0.045f, 0.04f to 0.03f)) drawCircle(BirdC15.Cloud, w * r, Offset(cx + w * dx, cy), alpha = 0.7f)
    }
    hills(BirdC15.Hill, h * 0.95f, h * 0.04f, 2.1f, 4.4f, 0.7f)

    val s = h * 0.36f
    val home = Offset(w * 0.5f, h * 0.48f)
    // which state the bird is in
    var fear = easeOut(seg(wf, 0f, 0.6f))
    var hope = easeOut(seg(wh, 0f, 0.6f))
    var love = easeOut(seg(hl, 0f, 0.4f))
    var pos = home
    var tilt = 0f
    var show = if (wf > 0f) 1f else 0f
    var flap = if (hl > 0.3f) sin(t * 6f) else 0f
    val fly = easeOut(seg(hl, 0.35f, 1f))
    pos += Offset(0f, -h * 0.06f * fly + h * 0.01f * sin(t * 6f) * fly)
    fun fall(b: Float, spin: Float): Float {
        show = easeOut(seg(b, 0f, 0.12f))
        val f = w8EaseIn(seg(b, 0.25f, 0.95f))
        pos = home + Offset(spin * w * 0.08f * f, h * 0.42f * f)
        tilt = spin * 300f * f + (if (spin == 0f) 25f * f else 0f)
        flap = if (spin != 0f) sin(t * 9f) * (1f - f) else 0f
        return f
    }
    when {
        so > 0f -> {
            fear = 1f; hope = 1f; love = 1f
            show = easeOut(seg(so, 0f, 0.12f))
            val up = easeInOut(seg(so, 0.15f, 1f))
            pos = home + Offset(w * 0.04f * sin(up * 3f), -h * 0.2f * up)
            flap = sin(t * 6f)
            tilt = 0f
        }
        ho > 0f -> { fear = 0f; hope = 1f; love = 0f; fall(ho, -1f) }
        fo > 0f -> { fear = 1f; hope = 0f; love = 0f; fall(fo, 1f) }
        lo > 0f -> { fear = 0f; hope = 0f; love = 1f; fall(lo, 0f) }
    }
    if (show > 0f && (lo > 0f || fo > 0f || ho > 0f) && so <= 0f) {
        // where it falls to
        drawOval(Color.Black, topLeft = Offset(pos.x - s * 0.3f, h * 0.93f), size = Size(s * 0.6f, h * 0.03f), alpha = 0.12f * show)
    }
    bird15(pos, s, flap, fear, hope, love, show, tilt)
    // the names of its parts, while it is whole or being made
    val whole = so <= 0f && lo <= 0f && fo <= 0f && ho <= 0f
    if (whole || so > 0f) {
        val tagA = if (so > 0f) 1f - seg(so, 0.5f, 0.9f) else 1f
        tagAt(a.tag("wingfear"), pos + Offset(-s * 0.75f, s * 0.22f), seg(wf, 0.1f, 0.6f) * tagA, back = BirdC15.Back, ink = Color(0xFFB8C8F0))
        tagAt(a.tag("winghope"), pos + Offset(s * 0.75f, s * 0.22f), seg(wh, 0.1f, 0.6f) * tagA, back = BirdC15.Back, ink = Color(0xFFA8E8C0))
        tagAt(a.tag("headlove"), pos + Offset(0f, -s * 0.62f), seg(hl, 0.1f, 0.4f) * tagA, back = BirdC15.Back, ink = Color(0xFFF8B8A8))
    }
    // soar: it climbs into the light
    if (so > 0.3f) {
        for (k in 0 until 5) {
            val f = fract(t * 0.8f + k / 5f)
            drawLine(Color.White, pos + Offset((k - 2) * s * 0.12f, s * (0.5f + 0.5f * f)), pos + Offset((k - 2) * s * 0.12f, s * (0.62f + 0.5f * f)), 1.4f * dp, StrokeCap.Round, alpha = 0.7f * (1f - f) * seg(so, 0.3f, 0.5f))
        }
    }
}
