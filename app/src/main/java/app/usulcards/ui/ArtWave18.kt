package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 18, round 11 of the redraw (2026-10-21): «السحر والكهانة والتنجيم». A fortune-teller's booth at dusk, where
 * believing him snaps the thread to the revealed book, and asking him costs forty nights of prayer; and a road in
 * the morning, where a crow turns a man back and a good word sends him on.
 */

/** A black bird in flight at [c], [s] across, its wings beating with [t]. */
internal fun DrawScope.crow18(c: Offset, s: Float, t: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val flap = sin(t * 12f) * s * 0.35f
    val dp = density
    drawPath(Path().apply {
        moveTo(c.x - s, c.y - flap)
        quadraticTo(c.x - s * 0.45f, c.y - s * 0.25f, c.x, c.y)
        quadraticTo(c.x + s * 0.45f, c.y - s * 0.25f, c.x + s, c.y - flap)
    }, color.copy(alpha = alpha), style = Stroke(max(2f * dp, s * 0.18f), cap = StrokeCap.Round))
    drawOval(color.copy(alpha = alpha), topLeft = c - Offset(s * 0.28f, s * 0.12f), size = Size(s * 0.56f, s * 0.24f))
    drawCircle(color, s * 0.13f, c + Offset(-s * 0.3f, -s * 0.06f), alpha = alpha)
    drawPath(Path().apply { moveTo(c.x - s * 0.4f, c.y - s * 0.08f); lineTo(c.x - s * 0.55f, c.y - s * 0.02f); lineTo(c.x - s * 0.4f, c.y - s * 0.0f); close() }, Color(0xFF4A4038).copy(alpha = alpha))
}

// ═════════════ 1. booth: believing the fortune-teller, and asking him (kahana) ═════════════

private object BoothC18 {
    val SkyTop = Color(0xFF3A3A6E)
    val SkyLow = Color(0xFFE89A6A)
    val Night = Color(0xFF0E1230)
    val Wall = Color(0xFFB89A7A)
    val WallDark = Color(0xFF8A6E54)
    val Ground = Color(0xFF6A5444)
    val Tent = Color(0xFF4A2A4E)
    val TentEdge = Color(0xFFC8A04A)
    val Table = Color(0xFF5A3A24)
    val Shell = Color(0xFFF0E6D0)
    val Smoke = Color(0xFF8A8090)
    val Ink = Color(0xFF1E1822)
    val Teller = Color(0xFF2A1E30)
    val Page = Color(0xFFFBF2DC)
    val Thread = Color(0xFFF2C45A)
    val Rug = Color(0xFF2E5A4A)
}

/**
 * A market lane at dusk: a fortune-teller under a dark awning on the right; on the left a book shining on a stand,
 * the revelation. Beats: fortune (a man comes and sits before him; he moves his hands over shells and smoke, and
 * the man nods along, believing, a gold thread still running from his heart to the book), cutoff (the thread snaps
 * and its ends fall; his heart goes grey; the book shines on), askonly (another comes and only asks: a question mark
 * over his raised hand), unaccepted (night falls; that one prays on a rug, and the lights of his prayer rise and
 * sink back before they get far; the moon crosses over and over, and the nights are counted off in strokes, forty).
 */
internal fun DrawScope.boothScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fortune" to Shot(Offset(w * 0.62f, h * 0.58f), 1.25f),
        "cutoff" to Shot(Offset(w * 0.4f, h * 0.55f), 1.1f),
        "askonly" to Shot(Offset(w * 0.55f, h * 0.58f), 1.25f),
        "unaccepted" to Shot(Offset(w * 0.4f, h * 0.48f), 1.08f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.16f) { boothLane(a) }
}

private fun DrawScope.boothLane(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fo = a.b("fortune")
    val cu = a.b("cutoff")
    val ak = a.b("askonly")
    val un = a.b("unaccepted")
    val night = easeOut(seg(un, 0f, 0.25f))
    val ground = h * 0.8f
    drawRect(Brush.verticalGradient(listOf(lerpColor(BoothC18.SkyTop, BoothC18.Night, night), lerpColor(BoothC18.SkyLow, Color(0xFF2A2A50), night)), endY = ground), size = Size(w, ground))
    if (night > 0f) for (k in 0 until 24) drawCircle(Color.White, (0.6f + 0.6f * w8Rnd(k, 3.1f)) * dp, Offset(w * w8Rnd(k, 9.7f), h * 0.4f * w8Rnd(k, 4.3f)), alpha = 0.7f * night)
    // the lane's walls
    drawRect(lerpColor(BoothC18.Wall, Color(0xFF3A3040), night), topLeft = Offset(0f, h * 0.36f), size = Size(w, ground - h * 0.36f))
    drawRect(lerpColor(BoothC18.WallDark, Color(0xFF2A2230), night), topLeft = Offset(0f, h * 0.36f), size = Size(w, h * 0.02f))
    drawRect(BoothC18.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))

    // the book on its stand, the revelation, shining
    val bk = Offset(w * 0.14f, ground)
    drawLine(Color(0xFF6A4A2A), bk, bk + Offset(-w * 0.02f, -h * 0.18f), 2.4f * dp, StrokeCap.Round)
    drawLine(Color(0xFF6A4A2A), bk, bk + Offset(w * 0.02f, -h * 0.18f), 2.4f * dp, StrokeCap.Round)
    val page = bk + Offset(0f, -h * 0.2f)
    softGlow(page, w * 0.1f, Color(0xFFFFF0C0), 0.85f)
    for (sd in listOf(-1f, 1f)) {
        drawPath(Path().apply {
            moveTo(page.x, page.y + h * 0.025f)
            lineTo(page.x + sd * w * 0.05f, page.y + h * 0.008f)
            lineTo(page.x + sd * w * 0.05f, page.y - h * 0.05f)
            lineTo(page.x, page.y - h * 0.032f)
            close()
        }, BoothC18.Page)
        for (k in 0 until 3) drawLine(Color(0xFF9A8060), Offset(page.x + sd * w * 0.01f, page.y - h * (0.02f - 0.014f * k)), Offset(page.x + sd * w * 0.04f, page.y - h * (0.032f - 0.014f * k)), 0.8f * dp)
    }

    // the booth: an awning, the teller, his table of shells and smoke
    val bx = w * 0.76f
    val fade = 1f - 0.65f * night
    drawPath(Path().apply {
        moveTo(bx - w * 0.16f, h * 0.42f); lineTo(bx + w * 0.16f, h * 0.42f); lineTo(bx + w * 0.19f, h * 0.5f); lineTo(bx - w * 0.19f, h * 0.5f); close()
    }, BoothC18.Tent.copy(alpha = fade))
    drawLine(BoothC18.TentEdge, Offset(bx - w * 0.19f, h * 0.5f), Offset(bx + w * 0.19f, h * 0.5f), 1.6f * dp, alpha = fade)
    for (x in listOf(-0.17f, 0.17f)) drawLine(BoothC18.Tent, Offset(bx + w * x, h * 0.5f), Offset(bx + w * x, ground), 2.4f * dp, alpha = fade)
    val tall = h * 0.34f
    w8Seated(Offset(bx + w * 0.04f, ground), tall, BoothC18.Teller, -1f, alpha = fade, speak = if (fo > 0f && cu < 1f) 0.5f + 0.5f * sin(t * 3f) else 0f)
    drawRect(BoothC18.Table, topLeft = Offset(bx - w * 0.08f, ground - h * 0.06f), size = Size(w * 0.09f, h * 0.06f), alpha = fade)
    for (k in 0 until 4) drawOval(BoothC18.Shell, topLeft = Offset(bx - w * (0.075f - 0.02f * k), ground - h * 0.07f), size = Size(w * 0.014f, h * 0.012f), alpha = fade)
    for (k in 0 until 5) {
        val f = fract(t * 0.4f + k / 5f)
        drawCircle(BoothC18.Smoke, w * (0.01f + 0.02f * f), Offset(bx - w * 0.04f + sin(f * 5f + k) * w * 0.02f, ground - h * 0.08f - f * h * 0.25f), alpha = 0.4f * (1f - f) * fade)
    }

    // the first visitor: comes, sits, nods along
    if (fo > 0f) {
        val come = easeInOut(seg(fo, 0f, 0.3f))
        val vx = mix(w * 0.3f, w * 0.52f, come)
        val al = fade
        val heart: Offset
        if (come < 1f) {
            withTransform({ scale(-1f, 1f, pivot = Offset(vx, ground)) }) { figure(Offset(vx, ground), tall, BoothC18.Ink, al, walk = t * 6f) }
            heart = Offset(vx, ground - tall * 0.62f)
        } else {
            val nod = if (cu < 0.3f) 0.5f + 0.5f * sin(t * 5f) else 0f
            val seat = Offset(vx, ground)
            withTransform({ rotate(6f * nod, seat) }) { w8Seated(seat, tall, BoothC18.Ink, 1f, alpha = al) }
            heart = seat + Offset(0f, -tall * 0.27f)
        }
        val grey = easeOut(seg(cu, 0.3f, 0.6f))
        val hc = lerpColor(Color(0xFFFFD27A), Color(0xFF7A7480), grey)
        softGlow(heart, tall * 0.14f, hc, 0.6f * (1f - grey) * al)
        drawPath(w8Heart(heart, tall * 0.045f), hc.copy(alpha = al))
        // the thread to the book, and its snapping
        val snap = easeOut(seg(cu, 0.1f, 0.35f))
        val mid = (heart + page) / 2f + Offset(0f, h * 0.06f)
        if (snap <= 0f) {
            drawPath(Path().apply { moveTo(heart.x, heart.y); quadraticTo(mid.x, mid.y, page.x, page.y) }, BoothC18.Thread.copy(alpha = 0.9f * al), style = Stroke(2f * dp, cap = StrokeCap.Round))
        } else {
            val drop = h * 0.12f * easeOut(seg(cu, 0.2f, 0.6f))
            drawPath(Path().apply { moveTo(heart.x, heart.y); quadraticTo(heart.x - w * 0.04f, heart.y + drop * 0.8f, (heart.x + mid.x) / 2f, mid.y + drop) }, BoothC18.Thread.copy(alpha = 0.7f * al), style = Stroke(2f * dp, cap = StrokeCap.Round))
            drawPath(Path().apply { moveTo(page.x, page.y); quadraticTo(page.x + w * 0.04f, page.y + drop, (page.x + mid.x) / 2f, mid.y + drop) }, BoothC18.Thread.copy(alpha = 0.7f * al), style = Stroke(2f * dp, cap = StrokeCap.Round))
            val sp = mid
            glint(sp, h * 0.08f, Color.White, bump(seg(cu, 0.1f, 0.4f)))
        }
        // what he believes: a made-up picture in a bubble over the teller
        val claim = seg(fo, 0.35f, 0.6f) * (1f - seg(cu, 0.4f, 0.8f))
        if (claim > 0f) {
            val bc = Offset(bx - w * 0.02f, h * 0.28f)
            drawCircle(Color.White, h * 0.07f, bc, alpha = 0.85f * claim * fade)
            for (k in 0 until 3) drawCircle(Color.White, h * (0.012f - 0.003f * k), bc + Offset(w * (0.02f + 0.015f * k), h * (0.08f + 0.03f * k)), alpha = 0.85f * claim * fade)
            qMark14(bc, h * 0.08f, BoothC18.Teller, claim * fade)
        }
    }
    // the second: comes and only asks
    if (ak > 0f) {
        val come = easeInOut(seg(ak, 0f, 0.35f))
        if (un <= 0.2f) {
            val ax = mix(w * 0.2f, w * 0.4f, come)
            val al = 1f - seg(un, 0f, 0.2f)
            withTransform({ scale(-1f, 1f, pivot = Offset(ax, ground + h * 0.04f)) }) {
                figure(Offset(ax, ground + h * 0.04f), tall, Color(0xFF3A2E40), al, walk = if (come < 1f) t * 6f else 0f, arms = if (come >= 1f) 0.8f else 0.1f)
            }
            qMark14(Offset(ax + tall * 0.25f, ground + h * 0.04f - tall * 1.15f), tall * 0.2f, Color(0xFF3A2E40), seg(ak, 0.4f, 0.6f) * al)
        }
    }
    // forty nights: he prays on his rug and the lights of his prayer sink back
    if (un > 0f) {
        val al = easeOut(seg(un, 0.15f, 0.35f))
        val px = w * 0.38f
        val foot = Offset(px, ground + h * 0.06f)
        drawRect(BoothC18.Rug, topLeft = Offset(px - w * 0.08f, ground + h * 0.05f), size = Size(w * 0.11f, h * 0.025f), alpha = al)
        val cyc = fract(t * 0.25f)
        val bow = if (cyc < 0.5f) bump(cyc * 2f) else 0f
        w8Worshipper(foot, tall, Color(0xFF3A2E40), if (cyc >= 0.5f) 1f else 0f, bow, al)
        for (k in 0 until 4) {
            val f = fract(t * 0.5f + k / 4f)
            val up = if (f < 0.6f) f / 0.6f else 1f - (f - 0.6f) / 0.4f
            val c = foot + Offset(-tall * 0.1f + (k - 1.5f) * tall * 0.06f, -tall * (1.05f + 0.45f * up))
            softGlow(c, 6f * dp, Color(0xFFFFE6A8), 0.6f * al * (1f - seg(f, 0.85f, 1f)))
            drawCircle(Color(0xFFFFF4D0), 1.8f * dp, c, alpha = al * (1f - seg(f, 0.85f, 1f)))
        }
        // the moon crossing night after night
        val moonU = fract(t * 0.3f)
        val mc = Offset(mix(w * 0.95f, w * 0.05f, moonU), h * (0.28f - 0.18f * sin(PI.toFloat() * moonU)))
        drawCircle(Color(0xFFF4EED8), h * 0.03f, mc, alpha = al)
        drawCircle(lerpColor(BoothC18.SkyTop, BoothC18.Night, night), h * 0.026f, mc + Offset(h * 0.014f, -h * 0.006f), alpha = al)
        // the nights, struck off in fives
        val count = (seg(un, 0.2f, 1f) * 40f).toInt()
        for (n in 0 until count) {
            val g = n / 5
            val i = n % 5
            val x0 = w * 0.06f + g * w * 0.06f
            val y0 = h * 0.08f
            if (i < 4) drawLine(Color(0xFFF4EED8), Offset(x0 + i * w * 0.009f, y0), Offset(x0 + i * w * 0.009f, y0 + h * 0.05f), 1.4f * dp, StrokeCap.Round, alpha = al)
            else drawLine(Color(0xFFF4EED8), Offset(x0 - w * 0.004f, y0 + h * 0.045f), Offset(x0 + w * 0.032f, y0 + h * 0.005f), 1.4f * dp, StrokeCap.Round, alpha = al)
        }
    }
}

// ═════════════ 2. omenroad: the crow that turns him back, the good word that sends him on (tatayyur) ═════════════

private object RoadC18 {
    val SkyTop = Color(0xFF8CC4EC)
    val SkyLow = Color(0xFFF4F0DC)
    val Field = Color(0xFFA8C47A)
    val FieldFar = Color(0xFFC4D69A)
    val Road = Color(0xFFD8C090)
    val RoadEdge = Color(0xFFB8A070)
    val House = Color(0xFFE8D8B8)
    val Roof = Color(0xFFA0583A)
    val Town = Color(0xFFE2D0AC)
    val Ink = Color(0xFF2A2430)
    val Crow = Color(0xFF16121A)
    val Bag = Color(0xFF8A6A3A)
    val Blot = Color(0xFF2A2230)
}

/**
 * A road through the fields in the morning, from a house on the left to a market town on a hill on the right.
 * Beats: crowfly (a man sets out with his bag; a crow flies across his way and caws, and he stops), turnhome (a dark
 * blot comes on his heart; he turns round and goes back home, and a red sign comes over him), goodword (he sets out
 * again and a passer-by calls a good word to him: the blot lifts, his head comes up), liked (his ﷺ light with his name
 * over the road, and the man walks on to the town).
 */
internal fun DrawScope.omenroadScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "crowfly" to Shot(Offset(w * 0.4f, h * 0.55f), 1.25f),
        "turnhome" to Shot(Offset(w * 0.32f, h * 0.58f), 1.2f),
        "goodword" to Shot(Offset(w * 0.45f, h * 0.52f), 1.15f),
        "liked" to Shot(Offset(w * 0.6f, h * 0.48f), 1.02f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { omenroadFields(a) }
}

private fun DrawScope.omenroadFields(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cf = a.b("crowfly")
    val th = a.b("turnhome")
    val gw = a.b("goodword")
    val lk = a.b("liked")
    val horizon = h * 0.5f
    drawRect(Brush.verticalGradient(listOf(RoadC18.SkyTop, RoadC18.SkyLow), endY = horizon), size = Size(w, horizon))
    softGlow(Offset(w * 0.15f, h * 0.12f), w * 0.1f, Color(0xFFFFF2C0), 0.8f)
    // the town on its hill
    drawPath(Path().apply { moveTo(w * 0.62f, horizon); quadraticTo(w * 0.82f, h * 0.26f, w * 1.02f, horizon); close() }, RoadC18.FieldFar)
    for (k in 0 until 4) {
        val x = w * (0.74f + 0.05f * k)
        val y = h * (0.36f + 0.02f * (k % 2))
        drawRect(RoadC18.Town, topLeft = Offset(x - w * 0.02f, y - h * 0.05f), size = Size(w * 0.04f, h * 0.05f))
        if (k == 1) drawArc(RoadC18.Town, 180f, 180f, true, topLeft = Offset(x - w * 0.02f, y - h * 0.08f), size = Size(w * 0.04f, h * 0.06f))
    }
    drawRect(RoadC18.Field, topLeft = Offset(0f, horizon), size = Size(w, h - horizon))
    // the road from his house to the town
    val road = Path().apply {
        moveTo(0f, h * 0.84f); lineTo(w * 0.7f, h * 0.52f); lineTo(w * 0.76f, h * 0.52f); lineTo(w * 0.2f, h); lineTo(0f, h); close()
    }
    drawPath(road, RoadC18.Road)
    fun at(u: Float): Offset = Offset(mix(w * 0.08f, w * 0.7f, u), mix(h * 0.9f, h * 0.55f, u))
    // his house
    val hc = Offset(w * 0.08f, h * 0.72f)
    drawRect(RoadC18.House, topLeft = Offset(hc.x - w * 0.07f, hc.y - h * 0.12f), size = Size(w * 0.14f, h * 0.12f))
    drawPath(Path().apply { moveTo(hc.x - w * 0.08f, hc.y - h * 0.12f); lineTo(hc.x, hc.y - h * 0.2f); lineTo(hc.x + w * 0.08f, hc.y - h * 0.12f); close() }, RoadC18.Roof)
    drawRect(RoadC18.Ink, topLeft = Offset(hc.x - w * 0.012f, hc.y - h * 0.06f), size = Size(w * 0.024f, h * 0.06f))

    // where he is on the road
    val out1 = easeInOut(seg(cf, 0.05f, 0.5f)) * 0.42f
    val back = easeInOut(seg(th, 0.25f, 0.85f))
    val out2 = easeInOut(seg(gw, 0.05f, 0.6f)) * 0.55f + easeInOut(seg(lk, 0.1f, 0.9f)) * 0.4f
    val u: Float
    val facing: Float
    val moving: Boolean
    when {
        gw > 0f -> { u = 0.08f + out2; facing = 1f; moving = (gw in 0.05f..0.6f) || (lk in 0.1f..0.9f) }
        th > 0f -> { u = mix(0.42f, 0.08f, back); facing = if (th > 0.2f) -1f else 1f; moving = th in 0.25f..0.85f }
        else -> { u = 0.08f + out1; facing = 1f; moving = cf in 0.05f..0.5f }
    }
    val tall = h * mix(0.36f, 0.22f, u)
    val foot = at(u)
    if (cf > 0f) {
        val headUp = easeOut(seg(gw, 0.3f, 0.6f))
        withTransform({ if (facing > 0f) scale(-1f, 1f, pivot = foot) }) {
            figure(foot, tall, RoadC18.Ink, seg(cf, 0f, 0.08f), walk = if (moving) t * 6f else 0f, lean = if (th > 0.2f && th < 0.9f) 0.08f else -0.04f * headUp)
            drawOval(RoadC18.Bag, topLeft = foot + Offset(tall * 0.04f, -tall * 0.66f), size = Size(tall * 0.16f, tall * 0.2f))
        }
        // the blot on his heart, and its lifting
        val heart = foot + Offset(0f, -tall * 0.62f)
        val blot = easeOut(seg(th, 0.05f, 0.3f)) * (1f - easeOut(seg(gw, 0.25f, 0.55f)))
        if (blot > 0f) {
            drawCircle(RoadC18.Blot, tall * 0.07f * blot, heart)
            drawCircle(RoadC18.Blot, tall * 0.04f * blot, heart + Offset(tall * 0.05f, -tall * 0.04f))
        }
        val lift = easeOut(seg(gw, 0.35f, 0.65f))
        if (lift > 0f) {
            softGlow(heart, tall * 0.18f, Color(0xFFFFD27A), 0.7f * lift)
            drawPath(w8Heart(heart, tall * 0.05f), Color(0xFFFFD27A).copy(alpha = lift))
        }
        // turning back for it: crossed out
        noSign11pub(foot + Offset(0f, -tall * 0.55f), tall * 0.42f, easeOut(seg(th, 0.6f, 0.85f)) * (1f - seg(gw, 0f, 0.2f)))
    }
    // the crow across his way, and its caw
    if (cf > 0.3f && th < 0.6f) {
        val f = seg(cf, 0.35f, 1f) + 0.4f * seg(th, 0f, 0.6f)
        val c = Offset(mix(w * 0.85f, w * 0.05f, min(1f, f)), mix(h * 0.18f, h * 0.42f, min(1f, f)) + h * 0.02f * sin(t * 3f))
        crow18(c, h * 0.06f, t, RoadC18.Crow, 1f - seg(th, 0.4f, 0.6f))
        val caw = seg(cf, 0.5f, 0.9f)
        for (k in 0 until 3) {
            val q = fract(t * 1.5f + k / 3f)
            val rr = h * (0.03f + 0.05f * q)
            drawArc(RoadC18.Crow, 150f, 60f, false, topLeft = c - Offset(rr + h * 0.04f, rr), size = Size(2 * rr, 2 * rr), alpha = caw * (1f - q) * (1f - seg(th, 0.4f, 0.6f)), style = Stroke(1.4f * dp, cap = StrokeCap.Round))
        }
    }
    // the passer-by and his good word
    if (gw > 0f) {
        val pf = Offset(w * 0.52f, h * 0.7f)
        val al = easeOut(seg(gw, 0f, 0.2f))
        figure(pf, h * 0.27f, Color(0xFF3A4A5A), al, arms = 0.6f)
        callout(pf + Offset(w * 0.02f, -h * 0.38f), a.big("goodword"), w * 0.4f, seg(gw, 0.15f, 0.35f) * (1f - 0.6f * seg(lk, 0.3f, 0.7f)), ArtInk.Shade, Color(0xFF2E8A5A))
    }
    // his ﷺ light over the road
    if (lk > 0f) {
        val lc = Offset(w * 0.56f, h * 0.17f)
        sirajMunir(lc, 4.4f * dp, easeOut(seg(lk, 0f, 0.3f)), t, nameAt = lc + Offset(w * 0.18f, 0f))
    }
}
