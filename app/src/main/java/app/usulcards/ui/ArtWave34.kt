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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 34 (2026-10-27): «بصائر»، الجزء الثاني، «بشارات الكتب السابقة» (2). The statue of the king's dream and the stone
 * cut without hands; the sceptre passed down one line until it leaves it; three voices that knew the time, over a line
 * of twelve centuries; and an open Book above old scrolls, setting their lines right.
 */

private val Back34 = Color(0xE6181420)

// ═════════════ 1. the statue and the stone (bs-danyal) ═════════════

private object StatueC34 {
    val Night = Color(0xFF141634)
    val NightLow = Color(0xFF34305A)
    val Ground = Color(0xFF2A2638)
    val Gold = Color(0xFFE8BE4A)
    val Silver = Color(0xFFC8CCD6)
    val Bronze = Color(0xFFB8743A)
    val Iron = Color(0xFF6A6E78)
    val Clay = Color(0xFF9A6A4A)
    val Stone = Color(0xFFD8CCB0)
    val Mount = Color(0xFF8A7A5A)
}

/** The statue's five parts, top to bottom: (centre, width, height, colour). */
private fun statueParts34(w: Float, h: Float): List<Triple<Offset, Size, Color>> = listOf(
    Triple(Offset(w * 0.5f, h * 0.2f), Size(w * 0.06f, h * 0.09f), StatueC34.Gold),
    Triple(Offset(w * 0.5f, h * 0.32f), Size(w * 0.14f, h * 0.13f), StatueC34.Silver),
    Triple(Offset(w * 0.5f, h * 0.45f), Size(w * 0.11f, h * 0.12f), StatueC34.Bronze),
    Triple(Offset(w * 0.5f, h * 0.6f), Size(w * 0.09f, h * 0.17f), StatueC34.Iron),
    Triple(Offset(w * 0.5f, h * 0.715f), Size(w * 0.11f, h * 0.05f), StatueC34.Clay),
)

/**
 * Beats: statue (in a night of dream a great statue builds from the head down: gold, silver, bronze, iron, iron mixed
 * with clay — «ذَهَبٌ»…«خَزَفٌ»), handless (a stone breaks from a far mountain, cut by no hand, strikes the feet; the
 * whole statue falls to chaff the wind carries off), fillearth (the stone grows into a mountain that fills the land),
 * kingdoms (the statue's outline again, faint, its parts named — «بَابِلُ», «فَارِسُ», «الْإِسْكَنْدَرُ», «الرُّومُ» —
 * and his light over the mountain).
 */
internal fun DrawScope.statueScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "statue" to Shot(Offset(w * 0.5f, h * 0.45f), 1.25f),
        "handless" to Shot(Offset(w * 0.58f, h * 0.55f), 1.1f),
        "fillearth" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "kingdoms" to Shot(Offset(w * 0.5f, h * 0.45f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { statueDream(a) }
}

private fun DrawScope.statueDream(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val st = a.b("statue")
    val hl = a.b("handless")
    val fe = a.b("fillearth")
    val kg = a.b("kingdoms")
    val ground = h * 0.74f
    nightSky(t, StatueC34.Night, StatueC34.NightLow, ground, starAlpha = 0.8f)
    // the far mountain the stone is cut from
    drawPath(Path().apply { moveTo(w * 0.72f, ground); lineTo(w * 0.86f, h * 0.36f); lineTo(w * 1.02f, ground); close() }, Color(0xFF3A3650))
    drawRect(StatueC34.Ground, Offset(0f, ground), Size(w, h - ground))
    val parts = statueParts34(w, h)
    val strike = easeOut(seg(hl, 0.2f, 0.45f))
    val crumble = seg(hl, 0.4f, 1f)
    // the statue, built from the head down, then falling to chaff
    for ((k, p) in parts.withIndex()) {
        val f = easeOut(seg(st, k * 0.12f, 0.25f + k * 0.12f)) * (1f - crumble)
        if (f > 0f) {
            val (c, sz, col) = p
            drawRoundRect(col, Offset(c.x - sz.width / 2f, c.y - sz.height / 2f), sz, CornerRadius(w * 0.01f), alpha = f)
            if (k == 1) for (s in listOf(-1f, 1f)) drawRoundRect(col, Offset(c.x + s * sz.width * 0.5f - (if (s < 0) w * 0.03f else 0f), c.y - sz.height * 0.45f), Size(w * 0.03f, sz.height * 1.1f), CornerRadius(w * 0.01f), alpha = f)
            if (k == 3) drawLine(StatueC34.Night, Offset(c.x, c.y - sz.height * 0.4f), Offset(c.x, c.y + sz.height / 2f), 2f * dp, alpha = f)
            if (k == 4) for (j in 0 until 4) drawRect(if (j % 2 == 0) StatueC34.Iron else StatueC34.Clay, Offset(c.x - sz.width / 2f + j * sz.width / 4f, c.y - sz.height / 2f), Size(sz.width / 4f, sz.height), alpha = f)
            softGlow(c, sz.width, col, 0.15f * f)
        }
        val tagF = seg(st, 0.2f + k * 0.12f, 0.4f + k * 0.12f) * (1f - seg(hl, 0f, 0.2f))
        tagAt(a.tag("statue", 0, k), Offset(w * 0.28f, parts[k].first.y), tagF, back = Back34, ink = ArtInk.Ivory)
        if (tagF > 0f) drawLine(Color(0xFF8A86A8), Offset(w * 0.33f, parts[k].first.y), Offset(parts[k].first.x - parts[k].second.width / 2f, parts[k].first.y), 1f * dp, alpha = 0.6f * tagF,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp)))
    }
    // the chaff the wind carries
    if (crumble > 0f) {
        for (k in 0 until 40) {
            val from = Offset(w * (0.46f + 0.08f * w8Rnd(k, 3.1f)), h * (0.16f + 0.58f * w8Rnd(k, 7.7f)))
            val p = from + Offset(w * 0.55f * crumble * (0.6f + 0.4f * w8Rnd(k, 1.3f)), -h * 0.1f * crumble * w8Rnd(k, 5.5f) + h * 0.05f * sin(t * 2f + k) * crumble)
            val col = parts[(k % 5)].third
            drawCircle(col, w * 0.004f, p, alpha = 0.8f * (1f - crumble))
        }
    }
    // the stone cut without hands
    val stoneFrom = Offset(w * 0.86f, h * 0.38f)
    val feet = Offset(w * 0.5f, h * 0.72f)
    if (hl > 0f) {
        val fly = easeOut(seg(hl, 0f, 0.4f))
        val p = stoneFrom + (feet + Offset(w * 0.05f, 0f) - stoneFrom) * fly - Offset(0f, sin(PI.toFloat() * fly) * h * 0.12f)
        val grow = easeOut(seg(fe, 0f, 0.8f))
        if (grow <= 0f) {
            drawCircle(StatueC34.Stone, w * 0.022f, p)
            if (fly < 1f) drawLine(Color(0xFFF2E8D0), p, p + (stoneFrom - p) * 0.15f, 2f * dp, StrokeCap.Round, alpha = 0.5f)
            if (strike in 0.01f..0.9f) softGlow(feet, w * 0.06f * strike, Color.White, 0.6f * (1f - strike))
        }
    }
    // the mountain that fills the land
    if (fe > 0f) {
        val grow = easeOut(seg(fe, 0f, 0.8f))
        val peak = Offset(w * 0.5f, mix(ground - h * 0.02f, h * 0.3f, grow))
        drawPath(Path().apply { moveTo(w * 0.5f - w * 0.6f * grow, ground + h * 0.01f); quadraticTo(w * 0.5f - w * 0.15f * grow, peak.y, peak.x, peak.y); quadraticTo(w * 0.5f + w * 0.15f * grow, peak.y, w * 0.5f + w * 0.6f * grow, ground + h * 0.01f); close() },
            Brush.verticalGradient(listOf(StatueC34.Stone, StatueC34.Mount), startY = peak.y, endY = ground))
        drawRect(StatueC34.Mount, Offset(0f, ground), Size(w, (h - ground) * grow), alpha = 0.8f)
        softGlow(peak, w * 0.1f, ArtInk.Gold, 0.3f * grow)
    }
    // the kingdoms named on its faint outline, and his light over the mountain
    if (kg > 0f) {
        val f = easeOut(seg(kg, 0f, 0.3f))
        for (k in 0 until 4) {
            val (c, sz, _) = parts[k]
            drawRoundRect(Color(0xFFE8E0FF), Offset(c.x - sz.width / 2f - w * 0.28f, c.y - sz.height / 2f), sz, CornerRadius(w * 0.01f), alpha = 0.5f * f, style = Stroke(1.2f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
            tagAt(a.tag("kingdoms", 0, k), Offset(c.x - w * 0.12f, c.y), seg(kg, 0.1f + k * 0.12f, 0.3f + k * 0.12f), back = Back34, ink = Color(0xFFD8D0F0))
        }
        val top = Offset(w * 0.5f, h * 0.22f)
        sirajMunir(top, w * 0.012f, easeOut(seg(kg, 0.6f, 0.9f)), t, nameAt = top + Offset(w * 0.15f, 0f))
    }
}

// ═════════════ 2. the sceptre (bs-shilon) ═════════════

private object RodC34 {
    val Sky = Color(0xFF2E2440)
    val SkyLow = Color(0xFFB07A5A)
    val Ground = Color(0xFF3A2E2A)
    val Line = Color(0xFFD8C098)
    val Node = Color(0xFFE8D4A8)
    val Rod = Color(0xFFF2C45A)
    val Iron = Color(0xFF8A8E9A)
}

/**
 * Beats: sceptre (a line of generations, «يَهُوذَا» at its head; a golden rod passes from each to the next),
 * handover (at the line's end the rod rises and goes over to a new light: «شِيلُونُ»), linedark (the line behind it
 * goes dark from its end: no prophet after), unshackle (a pair of shackles opens and falls away: «الْأَغْلَالُ»).
 */
internal fun DrawScope.sceptreScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sceptre" to Shot(Offset(w * 0.42f, h * 0.62f), 1.2f),
        "handover" to Shot(Offset(w * 0.62f, h * 0.45f), 1.15f),
        "linedark" to Shot(Offset(w * 0.45f, h * 0.62f), 1.15f),
        "unshackle" to Shot(Offset(w * 0.75f, h * 0.7f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { sceptreLine(a) }
}

private fun DrawScope.sceptreLine(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sc = a.b("sceptre")
    val ho = a.b("handover")
    val ld = a.b("linedark")
    val us = a.b("unshackle")
    val ground = h * 0.82f
    nightSky(t, RodC34.Sky, RodC34.SkyLow, ground, starAlpha = 0.5f)
    drawRect(RodC34.Ground, Offset(0f, ground), Size(w, h - ground))
    val y = h * 0.62f
    val nodes = (0 until 8).map { Offset(w * (0.08f + 0.085f * it), y + h * 0.02f * sin(it * 1.3f)) }
    val drawn = easeOut(seg(sc, 0f, 0.3f))
    val dark = easeOut(seg(ld, 0.1f, 0.9f))
    for (k in nodes.indices) {
        // darkness spreads back from the end of the line
        val dimK = seg(dark * 8f - (7 - k), 0f, 1f)
        val col = lerpColor(RodC34.Node, Color(0xFF3A3440), dimK)
        if (k > 0) drawLine(lerpColor(RodC34.Line, Color(0xFF3A3440), dimK), nodes[k - 1], nodes[k], 2f * dp, alpha = drawn)
        drawCircle(col, w * 0.012f, nodes[k], alpha = drawn)
    }
    tagAt(a.tag("sceptre"), nodes[0] + Offset(0f, h * 0.1f), seg(sc, 0.2f, 0.45f), back = Back34, ink = ArtInk.Ivory)
    // the rod passing down the line, then leaving it
    if (sc > 0f) {
        val pass = easeInOut(seg(sc, 0.25f, 1f)) * 7f
        val i = pass.toInt().coerceAtMost(6)
        val along = nodes[i] + (nodes[(i + 1).coerceAtMost(7)] - nodes[i]) * (pass - i)
        val goal = Offset(w * 0.82f, h * 0.3f)
        val lift = easeInOut(seg(ho, 0f, 0.7f))
        val rodAt = along + (goal + Offset(0f, h * 0.08f) - along) * lift - Offset(0f, sin(PI.toFloat() * lift) * h * 0.1f)
        val tilt = 0.4f * (1f - lift)
        val tip = rodAt + Offset(sin(tilt) * h * 0.06f, -cos(tilt) * h * 0.06f)
        val base = rodAt - Offset(sin(tilt) * h * 0.04f, -cos(tilt) * h * 0.04f)
        drawLine(RodC34.Rod, base, tip, 3f * dp, StrokeCap.Round)
        drawCircle(RodC34.Rod, w * 0.008f, tip)
        softGlow(tip, w * 0.03f, RodC34.Rod, 0.5f)
    }
    if (ho > 0f) {
        val goal = Offset(w * 0.82f, h * 0.3f)
        sirajMunir(goal, w * 0.012f, easeOut(seg(ho, 0.4f, 0.8f)), t, nameAt = goal + Offset(0f, -h * 0.12f))
        tagAt(a.tag("handover"), goal + Offset(-w * 0.14f, h * 0.08f), seg(ho, 0.6f, 0.85f), back = Back34, ink = ArtInk.Gold)
    }
    // the shackles fall away
    if (us > 0f) {
        val f = easeOut(seg(us, 0f, 0.2f))
        val open = easeOut(seg(us, 0.3f, 0.6f))
        val fall = w8EaseIn(seg(us, 0.5f, 1f))
        val c = Offset(w * 0.75f, h * 0.72f + h * 0.1f * fall)
        for (s in listOf(-1f, 1f)) {
            val ring = c + Offset(s * w * (0.035f + 0.03f * open), 0f)
            drawArc(RodC34.Iron, if (s < 0) 30f + 60f * open else 210f + 60f * open, 300f, false, Offset(ring.x - w * 0.025f, ring.y - w * 0.025f), Size(w * 0.05f, w * 0.05f), alpha = f * (1f - 0.6f * fall), style = Stroke(3f * dp))
        }
        for (k in 0 until 4) {
            val u = k / 3f
            drawCircle(RodC34.Iron, w * 0.006f, c + Offset(w * (-0.02f + 0.04f * u) * (1f - open), h * 0.01f * sin(PI.toFloat() * u) + h * 0.03f * open * (if (k % 2 == 0) 1f else -1f)), alpha = f * (1f - 0.6f * fall), style = Stroke(1.5f * dp))
        }
        tagAt(a.tag("unshackle"), Offset(w * 0.75f, h * 0.55f), seg(us, 0.3f, 0.55f), back = Back34, ink = Color(0xFFD8E8F0))
    }
}

// ═════════════ 3. those who knew the time (bs-zaman) ═════════════

private object WaitC34 {
    val Sky = Color(0xFF1E2238)
    val SkyLow = Color(0xFF4A3E5A)
    val Ground = Color(0xFF2A2430)
    val Trunk = Color(0xFF6A5040)
    val Leaf = Color(0xFF3A6A48)
    val Wall = Color(0xFF5A5060)
    val Window = Color(0xFFFFC870)
    val Crown = Color(0xFFE8BE4A)
    val Cushion = Color(0xFF7A2A3A)
    val Ruin = Color(0xFF8A8070)
    val Tick = Color(0xFFE8D8B0)
}

/**
 * Beats: yathrib (palms and houses of Medina; what they used to say: «قَدْ تَقَارَبَ زَمَانُ نَبِيٍّ»), cell (a monk's
 * cell, a lamp and a book at the window: «قَدْ أَظَلَّكَ زَمَانُ نَبِيٍّ»), throne (a crown on a cushion: «قَدْ كُنْتُ
 * أَعْلَمُ أَنَّهُ خَارِجٌ»), twelve (below, a line from a ruined temple: twelve marks light up one after another, and at
 * the twelfth his light).
 */
internal fun DrawScope.awaitedScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "yathrib" to Shot(Offset(w * 0.2f, h * 0.45f), 1.45f),
        "cell" to Shot(Offset(w * 0.5f, h * 0.45f), 1.45f),
        "throne" to Shot(Offset(w * 0.8f, h * 0.45f), 1.45f),
        "twelve" to Shot(Offset(w * 0.5f, h * 0.7f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { awaitedNight(a) }
}

private fun DrawScope.awaitedNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val yt = a.b("yathrib")
    val ce = a.b("cell")
    val th = a.b("throne")
    val tw = a.b("twelve")
    val ground = h * 0.6f
    nightSky(t, WaitC34.Sky, WaitC34.SkyLow, ground, starAlpha = 0.7f)
    drawRect(WaitC34.Ground, Offset(0f, ground), Size(w, h - ground))
    // Medina's palms and houses
    val yf = easeOut(seg(yt, 0f, 0.4f))
    if (yf > 0f) {
        for (k in 0 until 3) w8Palm(Offset(w * (0.08f + 0.1f * k), ground), h * (0.2f + 0.03f * (k % 2)), WaitC34.Trunk, WaitC34.Leaf, t, yf)
        for (k in 0 until 3) drawRect(Color(0xFF8A7A60), Offset(w * (0.12f + 0.08f * k), ground - h * 0.07f), Size(w * 0.06f, h * 0.07f), alpha = yf)
    }
    tagAt(a.tag("yathrib"), Offset(w * 0.2f, h * 0.22f), seg(yt, 0.3f, 0.55f) * (1f - seg(ce, 0f, 0.25f)), back = Back34, ink = ArtInk.Ivory)
    // the monk's cell
    val cf = easeOut(seg(ce, 0f, 0.4f))
    if (cf > 0f) {
        drawRect(WaitC34.Wall, Offset(w * 0.43f, ground - h * 0.22f), Size(w * 0.14f, h * 0.22f), alpha = cf)
        drawPath(archPath26(w * 0.5f, ground - h * 0.18f, ground - h * 0.06f, w * 0.03f), WaitC34.Window, alpha = cf * (0.85f + 0.15f * sin(t * 3f)))
        softGlow(Offset(w * 0.5f, ground - h * 0.12f), w * 0.05f, WaitC34.Window, 0.5f * cf)
        openBook21(Offset(w * 0.5f, ground - h * 0.08f), w * 0.04f, Color(0xFFF2E8D2), Color(0xFF8A7A5A), cf)
    }
    tagAt(a.tag("cell"), Offset(w * 0.5f, h * 0.22f), seg(ce, 0.3f, 0.55f) * (1f - seg(th, 0f, 0.25f)), back = Back34, ink = ArtInk.Ivory)
    // the crown on its cushion
    val tf = easeOut(seg(th, 0f, 0.4f))
    if (tf > 0f) {
        val c = Offset(w * 0.8f, ground - h * 0.06f)
        drawOval(WaitC34.Cushion, Offset(c.x - w * 0.06f, c.y - h * 0.02f), Size(w * 0.12f, h * 0.05f), alpha = tf)
        val crown = Path().apply {
            moveTo(c.x - w * 0.04f, c.y - h * 0.02f); lineTo(c.x - w * 0.045f, c.y - h * 0.09f); lineTo(c.x - w * 0.02f, c.y - h * 0.05f); lineTo(c.x, c.y - h * 0.1f)
            lineTo(c.x + w * 0.02f, c.y - h * 0.05f); lineTo(c.x + w * 0.045f, c.y - h * 0.09f); lineTo(c.x + w * 0.04f, c.y - h * 0.02f); close()
        }
        drawPath(crown, WaitC34.Crown, alpha = tf)
        glint(Offset(c.x, c.y - h * 0.1f), w * 0.006f, Color.White, tf)
    }
    tagAt(a.tag("throne"), Offset(w * 0.8f, h * 0.22f), seg(th, 0.3f, 0.55f) * (1f - seg(tw, 0f, 0.25f)), back = Back34, ink = ArtInk.Ivory)
    // twelve centuries from the ruined temple
    if (tw > 0f) {
        val f = easeOut(seg(tw, 0f, 0.2f))
        val y = h * 0.8f
        val x0 = w * 0.14f
        val x1 = w * 0.84f
        // the ruin
        for (k in 0 until 3) {
            val ht = h * (0.08f - 0.02f * k)
            drawRect(WaitC34.Ruin, Offset(x0 - w * 0.07f + k * w * 0.025f, y - ht), Size(w * 0.014f, ht), alpha = f)
        }
        drawRect(WaitC34.Ruin, Offset(x0 - w * 0.08f, y), Size(w * 0.09f, h * 0.012f), alpha = f)
        drawLine(WaitC34.Tick, Offset(x0, y), Offset(x1, y), 1.5f * dp, alpha = f)
        for (k in 1..12) {
            val x = x0 + (x1 - x0) * k / 12f
            val lit = seg(tw, 0.1f + k * 0.05f, 0.15f + k * 0.05f)
            drawLine(lerpColor(Color(0xFF6A6478), ArtInk.Gold, lit), Offset(x, y - h * 0.02f), Offset(x, y + h * 0.02f), 2f * dp, alpha = f)
        }
        sirajMunir(Offset(x1, y - h * 0.1f), w * 0.011f, easeOut(seg(tw, 0.75f, 0.95f)), t, nameAt = Offset(x1 - w * 0.12f, y - h * 0.18f))
        tagAt(a.tag("twelve"), Offset((x0 + x1) / 2f, y + h * 0.09f), seg(tw, 0.4f, 0.65f), back = Back34, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. the Book over the scrolls (bs-muhaymin) ═════════════

private object OverC34 {
    val Wall = Color(0xFF241E2E)
    val WallLow = Color(0xFF3E3044)
    val Shelf = Color(0xFF5A4232)
    val Ray = Color(0xFFFFE6A8)
    val Red = Color(0xFFD23C30)
    val Ruin = Color(0xFF8A7A66)
    val House = Color(0xFFD8C4A0)
    val Wallc = Color(0xFFB8A07A)
}

/**
 * Beats: overbook (an open Book, lit, above three old scrolls), strikeone (a ray to the first scroll strikes a line:
 * «كُفْرُ سُلَيْمَانَ»), striketwo (the second: «اسْتَرَاحَ»), strikethree (the third: «أُهِينَ» struck, and in its place
 * «وَجِيهًا»), ezra (below, a ruined empty town fills with houses and a wall over a hundred years: «عَزْرَا»).
 */
internal fun DrawScope.overseerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "overbook" to Shot(Offset(w * 0.5f, h * 0.35f), 1.2f),
        "strikeone" to Shot(Offset(w * 0.25f, h * 0.5f), 1.3f),
        "striketwo" to Shot(Offset(w * 0.5f, h * 0.5f), 1.3f),
        "strikethree" to Shot(Offset(w * 0.75f, h * 0.5f), 1.3f),
        "ezra" to Shot(Offset(w * 0.5f, h * 0.8f), 1.5f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { overseerHall(a) }
}

private fun DrawScope.overseerHall(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ob = a.b("overbook")
    val s1 = a.b("strikeone")
    val s2 = a.b("striketwo")
    val s3 = a.b("strikethree")
    val ez = a.b("ezra")
    drawRect(Brush.verticalGradient(listOf(OverC34.Wall, OverC34.WallLow)), size = Size(w, h))
    drawRect(OverC34.Shelf, Offset(w * 0.08f, h * 0.66f), Size(w * 0.84f, h * 0.025f))
    val book = Offset(w * 0.5f, h * 0.14f)
    val bf = easeOut(seg(ob, 0f, 0.4f))
    if (bf > 0f) {
        lightShafts(book, PI.toFloat() / 2f, 1.6f, h * 0.55f, OverC34.Ray, 0.35f * bf, t, rays = 9)
        openBook21(book, w * 0.16f, Color(0xFFF6EED8), Color(0xFF8A6A3A), bf, glow = bf)
    }
    val scrolls = listOf(Offset(w * 0.22f, h * 0.52f), Offset(w * 0.5f, h * 0.52f), Offset(w * 0.78f, h * 0.52f))
    for ((k, c) in scrolls.withIndex()) {
        val f = easeOut(seg(ob, 0.2f + k * 0.12f, 0.5f + k * 0.12f))
        scroll14(c, w * 0.12f, h * 0.24f, 1f, f)
    }
    val strikes = listOf(s1, s2, s3)
    val kinds = listOf("strikeone", "striketwo", "strikethree")
    for (k in 0 until 3) {
        val b = strikes[k]
        if (b <= 0f) continue
        val c = scrolls[k]
        val ray = easeOut(seg(b, 0f, 0.3f))
        drawLine(OverC34.Ray, book + Offset(0f, h * 0.05f), book + Offset(0f, h * 0.05f) + (c + Offset(0f, -h * 0.12f) - book - Offset(0f, h * 0.05f)) * ray, 2f * dp, StrokeCap.Round, alpha = 0.7f * (1f - seg(b, 0.8f, 1f) * 0.5f))
        struckTag21(a.tag(kinds[k], 0, 0), c + Offset(0f, h * 0.17f), seg(b, 0.15f, 0.4f), seg(b, 0.35f, 0.75f), OverC34.Red, Back34, ArtInk.Ivory)
        if (k == 2) tagAt(a.tag("strikethree", 0, 1), c + Offset(0f, h * 0.26f), seg(b, 0.7f, 0.95f), back = Back34, ink = ArtInk.Gold)
    }
    // Ezra's town: empty, then full a hundred years on
    if (ez > 0f) {
        val f = easeOut(seg(ez, 0f, 0.2f))
        val fill = easeInOut(seg(ez, 0.25f, 0.85f))
        val y = h * 0.92f
        for (k in 0 until 5) {
            val x = w * (0.3f + 0.1f * k)
            // ruins first
            drawRect(OverC34.Ruin, Offset(x - w * 0.03f, y - h * 0.04f), Size(w * 0.012f, h * 0.04f), alpha = f * (1f - fill))
            // then houses with lit windows
            val hf = seg(fill, k * 0.12f, 0.4f + k * 0.12f)
            drawRect(OverC34.House, Offset(x - w * 0.035f, y - h * 0.08f * hf), Size(w * 0.06f, h * 0.08f * hf), alpha = f)
            if (hf > 0.9f) drawRect(Color(0xFFFFC870), Offset(x - w * 0.01f, y - h * 0.055f), Size(w * 0.016f, h * 0.02f), alpha = f)
        }
        val wall = seg(fill, 0.5f, 1f)
        drawLine(OverC34.Wallc, Offset(w * 0.24f, y + h * 0.005f), Offset(w * 0.24f + w * 0.54f * wall, y + h * 0.005f), 4f * dp, alpha = f)
        // a hundred years: a sun crossing many times above the town
        val u = fract(seg(ez, 0.25f, 0.85f) * 5f)
        if (ez in 0.25f..0.85f) drawCircle(Color(0xFFFFD27A), w * 0.008f, Offset(w * (0.25f + 0.5f * u), h * 0.76f - sin(PI.toFloat() * u) * h * 0.04f))
        tagAt(a.tag("ezra"), Offset(w * 0.5f, h * 0.73f), seg(ez, 0.3f, 0.55f), back = Back34, ink = ArtInk.Ivory)
    }
}
