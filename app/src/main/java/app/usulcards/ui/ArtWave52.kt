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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 52 (2026-11-03): «حصن المسلم»، «أذكار اليوم والليلة» (2). After the prayer: the hundred beads and the foam they
 * wash away; the prayer of seeking the good between two doors; the day's two ends and the guard of the morning; the
 * master of seeking forgiveness written as a covenant in the notebook; the words after which nothing harms. Allah is
 * never drawn, only His names as words; jinn are pale blue flames; the Prophet ﷺ is only light.
 */

private val Back52 = Color(0xE6181420)

// ═════════════ 1. after the prayer (hisn-salah) ═════════════

private object AfterC52 {
    val Wall = Color(0xFFEDE2C8)
    val Arch = Color(0xFFD8C49C)
    val Floor = Color(0xFF8A3A3A)
    val Person = Color(0xFF3A3040)
    val Gold = Color(0xFFE8B84A)
    val Bead = Color(0xFF8A5A2E)
    val Sea = Color(0xFF6AA6C4)
    val Foam = Color(0xFFF4F8FA)
    val Garden = Color(0xFF5A9A5A)
    val Bag = Color(0xFF8A6A44)
    val Deed = Color(0xFFF2C45A)
}

/**
 * Beats: salamturn (the light ﷺ after the prayer: a glow turns to the right and the left; three small rings of asking
 * forgiveness rise), hundred (three rows of 33 beads fill, one more makes a hundred; a heap of sea foam beside them
 * dissolves: «33»), nobarrier (from the prayer niche a clear path runs to a garden gate with nothing across it but the
 * end of a life), threeasks (three steps light: «ذِكْرِكَ», «وَشُكْرِكَ», «وَحُسْنِ عِبَادَتِكَ»), nojadd (a full money bag
 * on one pan does not move the balance; a small shining deed does).
 */
internal fun DrawScope.afterprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "salamturn" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "hundred" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "nobarrier" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "threeasks" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "nojadd" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { afterHall(a) }
}

private fun DrawScope.afterHall(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val st = a.b("salamturn")
    val hd = a.b("hundred")
    val nb = a.b("nobarrier")
    val ta = a.b("threeasks")
    val nj = a.b("nojadd")
    val floor = h * 0.8f
    drawRect(AfterC52.Wall)
    for (k in 0 until 4) drawPath(archPath26(w * (0.14f + 0.24f * k), h * 0.1f, floor - h * 0.2f, w * 0.08f), AfterC52.Arch)
    drawRect(AfterC52.Floor, Offset(0f, floor), Size(w, h - floor))
    if (st > 0f) {
        val f = easeOut(seg(st, 0f, BEAT_IN)) * (1f - seg(hd, 0f, BEAT_OUT))
        val seat = Offset(w * 0.5f, floor)
        val turn = sin(PI.toFloat() * 2f * seg(st, 0.05f, 0.35f))
        val light = seat + Offset(0f, -h * 0.26f)
        sirajMunir(light, w * 0.015f, f, t, nameAt = light + Offset(0f, h * 0.14f))
        softGlow(light + Offset(w * 0.08f * turn, 0f), w * 0.03f, AfterC52.Gold, 0.5f * f * kotlin.math.abs(turn))
        for (k in 0 until 3) {
            val u = seg(st, 0.4f + 0.15f * k, 0.7f + 0.15f * k)
            if (u <= 0f || u >= 1f) continue
            drawCircle(AfterC52.Gold, w * (0.02f + 0.04f * u), seat + Offset(0f, -h * (0.4f + 0.2f * u)), alpha = f * (1f - u), style = Stroke(2f * dp))
        }
    }
    if (hd > 0f) {
        val f = easeOut(seg(hd, 0f, BEAT_IN)) * (1f - seg(nb, 0f, BEAT_OUT))
        for (row in 0 until 3) {
            val fill = seg(hd, 0.05f + 0.2f * row, 0.25f + 0.2f * row)
            for (k in 0 until 33) {
                val c = Offset(w * (0.08f + 0.017f * k), h * (0.25f + 0.13f * row))
                drawCircle(if (k < (fill * 33f).toInt()) AfterC52.Bead else AfterC52.Arch, w * 0.007f, c, alpha = f)
            }
            tagAt(a.tag("hundred"), Offset(w * 0.7f, h * (0.25f + 0.13f * row)), f * fill, back = Back52, ink = ArtInk.Gold)
        }
        drawCircle(AfterC52.Gold, w * 0.012f, Offset(w * 0.4f, h * 0.66f), alpha = f * seg(hd, 0.7f, 0.8f))
        val gone = seg(hd, 0.75f, 1f)
        drawRect(AfterC52.Sea, Offset(w * 0.78f, floor - h * 0.12f), Size(w * 0.2f, h * 0.12f), alpha = f)
        for (k in 0 until 12) drawCircle(AfterC52.Foam, w * 0.012f, Offset(w * (0.8f + 0.015f * k), floor - h * (0.13f + 0.02f * (k % 3))), alpha = f * (1f - gone))
    }
    if (nb > 0f) {
        val f = easeOut(seg(nb, 0f, BEAT_IN)) * (1f - seg(ta, 0f, BEAT_OUT))
        drawPath(archPath26(w * 0.15f, h * 0.3f, floor, w * 0.07f), Color(0xFF6A5A44), alpha = f)
        val run = easeInOut(seg(nb, 0.1f, 0.6f))
        drawLine(AfterC52.Gold, Offset(w * 0.2f, floor - h * 0.02f), Offset(w * (0.2f + 0.56f * run), floor - h * 0.02f), 4f * dp, StrokeCap.Round, alpha = f)
        drawRect(AfterC52.Garden, Offset(w * 0.72f, h * 0.3f), Size(w * 0.24f, floor - h * 0.3f), alpha = f * seg(nb, 0.4f, 0.7f))
        door12(Offset(w * 0.8f, floor), w * 0.1f, h * 0.4f, seg(nb, 0.5f, 0.8f), Color(0xFF6A5A44), Color(0xFF3A2A1E), f, Color(0xFFDFF2B0))
        drawLine(Color(0xFF8A8494), Offset(w * 0.75f, h * 0.45f), Offset(w * 0.75f, floor), 1f * dp, alpha = f * 0.7f)
    }
    if (ta > 0f) {
        val f = easeOut(seg(ta, 0f, BEAT_IN)) * (1f - seg(nj, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val g = easeOut(seg(ta, 0.1f + 0.25f * k, 0.3f + 0.25f * k))
            val top = floor - h * (0.1f + 0.1f * k)
            drawRect(Color(0xFFD8C49C), Offset(w * (0.2f + 0.2f * k), top), Size(w * 0.2f, floor - top), alpha = f * g)
            softGlow(Offset(w * (0.3f + 0.2f * k), top - h * 0.04f), w * 0.05f, AfterC52.Gold, 0.5f * f * g)
            tagAt(a.tag("threeasks", 0, k), Offset(w * (0.3f + 0.2f * k), top - h * 0.12f), f * g, back = Back52, ink = ArtInk.Gold)
        }
    }
    if (nj > 0f) {
        val f = easeOut(seg(nj, 0f, 0.3f))
        val tilt = -0.7f * easeInOut(seg(nj, 0.5f, 0.85f))
        val foot = Offset(w * 0.5f, floor)
        val s = h * 0.56f
        balance12(foot, s, tilt, Color(0xFF6A4A2E), f)
        val (lp, rp) = balancePans12(foot, s, tilt)
        drawOval(AfterC52.Bag, Offset(rp.x - w * 0.05f, rp.y - h * 0.1f), Size(w * 0.1f, h * 0.1f), alpha = f * seg(nj, 0.05f, 0.2f))
        for (k in 0 until 3) coin12(rp + Offset(w * (0.02f * k - 0.02f), -h * 0.12f), w * 0.01f, f * seg(nj, 0.1f, 0.25f))
        val d = seg(nj, 0.35f, 0.5f)
        softGlow(lp + Offset(0f, -h * 0.03f), w * 0.04f, AfterC52.Deed, 0.9f * f * d)
        drawCircle(AfterC52.Deed, w * 0.01f, lp + Offset(0f, -h * 0.03f), alpha = f * d)
    }
}

// ═════════════ 2. between two doors (hisn-istikhara) ═════════════

private object ChooseC52 {
    val Sky = Color(0xFFBCD6E8)
    val SkyLow = Color(0xFFF2E6CA)
    val Ground = Color(0xFFCFB88E)
    val Person = Color(0xFF3A3040)
    val Gold = Color(0xFFE8B84A)
    val Door = Color(0xFF7A5A3A)
    val Frame = Color(0xFFB89A6A)
    val Good = Color(0xFFDFF2B0)
    val Shut = Color(0xFF6A6470)
    val Page = Color(0xFFFBF7EC)
    val Road = Color(0xFFBFA77E)
}

/**
 * Beats: likesurah (the light ﷺ beside an open book; plain roundels of his companions gather to learn, as a surah is learned), tworakah (one
 * bows and prostrates twice, then raises his hands), consulted (he sits with two who give counsel, then walks on with a
 * steady step), gooddoor (of two doors at a fork, the one that is good opens and its road is made smooth), closedoor
 * (the other closes, he is turned from it, and a warm glow of content settles on him).
 */
internal fun DrawScope.twodoorsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "likesurah" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "tworakah" to Shot(Offset(w * 0.5f, h * 0.6f), 1.25f),
        "consulted" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "gooddoor" to Shot(Offset(w * 0.4f, h * 0.55f), 1.1f),
        "closedoor" to Shot(Offset(w * 0.6f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { chooseDay(a) }
}

private fun DrawScope.chooseDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ls = a.b("likesurah")
    val tr = a.b("tworakah")
    val cs = a.b("consulted")
    val od = a.b("gooddoor")
    val cd = a.b("closedoor")
    val ground = h * 0.84f
    daySky12(ChooseC52.Sky, ChooseC52.SkyLow, ground, ChooseC52.Ground)
    if (ls > 0f) {
        val f = easeOut(seg(ls, 0f, BEAT_IN)) * (1f - seg(tr, 0f, BEAT_OUT))
        val light = Offset(w * 0.3f, h * 0.38f)
        sirajMunir(light, w * 0.014f, f, t, nameAt = light + Offset(0f, -h * 0.13f))
        openBook21(Offset(w * 0.55f, h * 0.4f), w * 0.2f, ChooseC52.Page, Color(0xFF9A9080), f * seg(ls, 0.1f, 0.3f), glow = 0.3f)
        for (k in 0 until 4) nameRoundel(Offset(w * (0.3f + 0.13f * k), ground - h * 0.08f), w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f * seg(ls, 0.2f + 0.08f * k, 0.35f + 0.08f * k))
    }
    if (tr > 0f) {
        val f = easeOut(seg(tr, 0f, BEAT_IN)) * (1f - seg(cs, 0f, BEAT_OUT))
        val p = Offset(w * 0.5f, ground)
        val ph = seg(tr, 0.05f, 0.8f) * 2f
        val inCycle = fract(ph)
        if (tr < 0.8f) {
            val deep = when {
                inCycle < 0.3f -> 0f
                inCycle < 0.55f -> 0.5f
                else -> 1f
            }
            bow12(p, h * 0.32f, ChooseC52.Person, f, deep)
        } else {
            sit12(p, h * 0.32f, ChooseC52.Person, f, -1f)
            hands12(p + Offset(-w * 0.02f, -h * 0.34f), h * 0.08f, ChooseC52.Person, f)
        }
        tagAt(a.tag("tworakah", 0, minOf(1, ph.toInt())), Offset(w * 0.5f, h * 0.24f), f * seg(tr, 0.1f, 0.2f) * (1f - seg(tr, 0.75f, 0.8f)), back = Back52, ink = ArtInk.Gold)
    }
    if (cs > 0f) {
        val f = easeOut(seg(cs, 0f, BEAT_IN)) * (1f - seg(od, 0f, BEAT_OUT))
        val go = easeInOut(seg(cs, 0.55f, 0.95f))
        sit12(Offset(w * 0.24f, ground), h * 0.26f, ChooseC52.Person, f * (1f - go * 0.5f), 1f)
        sit12(Offset(w * 0.4f, ground), h * 0.26f, ChooseC52.Person, f * (1f - go * 0.5f), -1f)
        voice12(Offset(w * 0.3f, ground - h * 0.3f), w * 0.03f, t, ChooseC52.Gold, f * (1f - go))
        if (go <= 0f) sit12(Offset(w * 0.32f, ground + h * 0.02f), h * 0.26f, ChooseC52.Gold, f, 1f)
        else figure(Offset(w * (0.36f + 0.5f * go), ground), h * 0.3f, ChooseC52.Gold, f, walk = t * 5f)
    }
    if (od > 0f || cd > 0f) {
        val f = easeOut(seg(od, 0f, 0.3f))
        val fork = Offset(w * 0.5f, h * 0.98f)
        drawLine(ChooseC52.Road, fork, Offset(w * 0.3f, ground - h * 0.05f), w * 0.04f, StrokeCap.Round, alpha = f)
        drawLine(ChooseC52.Road, fork, Offset(w * 0.7f, ground - h * 0.05f), w * 0.04f, StrokeCap.Round, alpha = f)
        val open = easeInOut(seg(od, 0.2f, 0.6f))
        door12(Offset(w * 0.3f, ground - h * 0.05f), w * 0.12f, h * 0.4f, open, ChooseC52.Frame, ChooseC52.Door, f, ChooseC52.Good)
        val shut = seg(cd, 0.15f, 0.45f)
        door12(Offset(w * 0.7f, ground - h * 0.05f), w * 0.12f, h * 0.4f, 0.4f * (1f - shut), ChooseC52.Frame, ChooseC52.Door, f, Color(0xFFE0B0A0))
        if (shut > 0.9f) crossMark(Offset(w * 0.7f, ground - h * 0.25f), w * 0.025f, ChooseC52.Shut, 3f * dp, f)
        val walkIn = easeInOut(seg(od, 0.5f, 1f))
        val p = if (cd > 0f) Offset(w * (0.5f - 0.14f * easeInOut(seg(cd, 0.4f, 0.9f))), ground + h * 0.06f) else Offset(w * (0.5f - 0.1f * walkIn), ground + h * 0.08f - h * 0.04f * walkIn)
        figure(p, h * 0.28f, ChooseC52.Person, f, walk = t * 5f, lean = if (cd > 0f) 0.1f * (1f - seg(cd, 0.4f, 0.9f)) else 0f)
        if (cd > 0f) softGlow(p + Offset(0f, -h * 0.26f), w * 0.06f, ChooseC52.Gold, 0.6f * f * seg(cd, 0.7f, 1f))
        if (open > 0.5f) softGlow(Offset(w * 0.3f, ground - h * 0.2f), w * 0.12f, ChooseC52.Gold, 0.4f * f * open * (1f - 0.5f * seg(cd, 0f, 0.4f)))
    }
}

// ═════════════ 3. the two ends of the day (hisn-sabah) ═════════════

private object DayC52 {
    val Dawn = Color(0xFFF2B07A)
    val Noon = Color(0xFF9CC8E8)
    val Dusk = Color(0xFFC0607A)
    val Night = Color(0xFF14182C)
    val Ground = Color(0xFF5A6A44)
    val Wall = Color(0xFFDCC6A0)
    val Trim = Color(0xFF8A6A44)
    val Gold = Color(0xFFE8B84A)
    val Person = Color(0xFF3A3040)
    val Chain = Color(0xFFB0B4C0)
}

/**
 * Beats: bookends (the sun's arc over a day, and a mark of light at its two ends: «بُكْرَةً», «وَعَشِيًّا»), dawnguard (a
 * house at dawn under a dome of light; pale blue flames stay outside it until evening), threeshields (three small
 * scrolls, each read three times, stand round as a shield), mulk (day turns to evening over the land and the words
 * stand over it all: «الْمُلْكُ لِلَّهِ»), tillsunrise (a circle sits round the light ﷺ from the dawn prayer until the sun is up; four
 * chains break: «4»).
 */
internal fun DrawScope.morningeveningScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "bookends" to Shot(Offset(w * 0.5f, h * 0.45f), 1.0f),
        "dawnguard" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "threeshields" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "mulk" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "tillsunrise" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { dayCycle(a) }
}

private fun DrawScope.dayCycle(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val be = a.b("bookends")
    val dg = a.b("dawnguard")
    val ts = a.b("threeshields")
    val mk = a.b("mulk")
    val tsr = a.b("tillsunrise")
    val ground = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(DayC52.Noon, Color(0xFFF2E6CC)), 0f, ground), Offset.Zero, Size(w, ground))
    drawRect(DayC52.Ground, Offset(0f, ground), Size(w, h - ground))
    if (be > 0f) {
        val f = easeOut(seg(be, 0f, BEAT_IN)) * (1f - seg(dg, 0f, BEAT_OUT))
        val u = seg(be, 0.1f, 0.9f)
        val c = Offset(w * 0.5f, ground)
        drawArc(DayC52.Gold, 180f, 180f, false, Offset(c.x - w * 0.4f, c.y - h * 0.6f), Size(w * 0.8f, h * 1.2f), alpha = f * 0.4f, style = Stroke(1.5f * dp, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp))))
        val ang = PI.toFloat() * (1f + u)
        sun12(c + Offset(cos(ang) * w * 0.4f, sin(ang) * h * 0.6f), w * 0.03f, f)
        for (k in 0 until 2) {
            val p = Offset(w * (0.1f + 0.8f * k), ground - h * 0.02f)
            val g = seg(be, 0.15f + 0.6f * k, 0.3f + 0.6f * k)
            softGlow(p, w * 0.05f, if (k == 0) DayC52.Dawn else DayC52.Dusk, 0.9f * f * g)
            tagAt(a.tag("bookends", 0, k), p + Offset(0f, -h * 0.12f), f * g, back = Back52, ink = ArtInk.Gold)
        }
    }
    if (dg > 0f) {
        val f = easeOut(seg(dg, 0f, BEAT_IN)) * (1f - seg(ts, 0f, BEAT_OUT))
        drawRect(Brush.verticalGradient(listOf(Color(0xFF6A88B8), DayC52.Dawn), 0f, ground), Offset.Zero, Size(w, ground), alpha = f)
        val c = Offset(w * 0.5f, ground)
        house12(c, w * 0.22f, DayC52.Wall, DayC52.Trim, f, 0.5f)
        guardDome12(c, w * 0.2f, seg(dg, 0.1f, 0.4f), t, f)
        for (k in 0 until 4) jinnFlame20(Offset(w * (0.1f + 0.08f * k) + (if (k > 1) w * 0.56f else 0f), ground), h * 0.12f, t, k, f * 0.8f)
    }
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, BEAT_IN)) * (1f - seg(mk, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.55f)
        drawCircle(DayC52.Person, h * 0.05f, c + Offset(0f, -h * 0.05f), alpha = f)
        drawLine(DayC52.Person, c, c + Offset(0f, h * 0.18f), h * 0.03f, StrokeCap.Round, alpha = f)
        for (k in 0 until 3) {
            val ang = -PI.toFloat() / 2f + (k - 1) * 0.9f
            val p = c + Offset(cos(ang) * w * 0.22f, sin(ang) * h * 0.3f + h * 0.12f)
            for (j in 0 until 3) {
                val g = seg(ts, 0.1f + 0.25f * k + 0.07f * j, 0.2f + 0.25f * k + 0.07f * j)
                scroll14(p + Offset(0f, -h * 0.025f * j), w * 0.06f, h * 0.05f, 1f, f * g)
            }
            softGlow(p, w * 0.06f, DayC52.Gold, 0.4f * f * seg(ts, 0.3f + 0.25f * k, 0.5f + 0.25f * k))
        }
    }
    if (mk > 0f) {
        val f = easeOut(seg(mk, 0f, BEAT_IN)) * (1f - seg(tsr, 0f, BEAT_OUT))
        val dusk = seg(mk, 0.1f, 0.8f)
        drawRect(Brush.verticalGradient(listOf(lerpColor(DayC52.Noon, DayC52.Night, dusk), lerpColor(Color(0xFFF2E6CC), DayC52.Dusk, dusk)), 0f, ground), Offset.Zero, Size(w, ground), alpha = f)
        for (k in 0 until 5) house12(Offset(w * (0.1f + 0.2f * k), ground), w * 0.12f, DayC52.Wall, DayC52.Trim, f, dusk)
        fitBig26(a.big("mulk"), Offset(w * 0.5f, h * 0.3f), w * 0.5f, ArtInk.Gold, f * seg(mk, 0.3f, 0.6f), glow = ArtInk.Gold)
    }
    if (tsr > 0f) {
        val f = easeOut(seg(tsr, 0f, 0.3f))
        val rise = seg(tsr, 0.1f, 0.8f)
        drawRect(Brush.verticalGradient(listOf(lerpColor(DayC52.Night, DayC52.Noon, rise), lerpColor(Color(0xFF3A3058), DayC52.Dawn, rise)), 0f, ground), Offset.Zero, Size(w, ground), alpha = f)
        sun12(Offset(w * 0.5f, ground + h * 0.06f - h * 0.2f * rise), w * 0.04f, f * rise)
        for (k in 0 until 6) {
            val ang = PI.toFloat() * (0.1f + 0.8f * k / 5f)
            val p = Offset(w * 0.5f - cos(ang) * w * 0.3f, ground + h * 0.08f - sin(ang) * h * 0.04f)
            nameRoundel(p + Offset(0f, -h * 0.05f), w * 0.026f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        }
        val lc = Offset(w * 0.5f, ground - h * 0.08f)
        sirajMunir(lc, w * 0.01f, f, t, nameAt = lc + Offset(0f, -h * 0.1f))
        for (k in 0 until 4) {
            val c = Offset(w * (0.2f + 0.2f * k), h * 0.25f)
            val br = seg(tsr, 0.55f + 0.08f * k, 0.7f + 0.08f * k)
            drawOval(DayC52.Chain, Offset(c.x - w * 0.035f - w * 0.01f * br, c.y - h * 0.02f), Size(w * 0.035f, h * 0.04f), alpha = f, style = Stroke(2f * dp))
            drawOval(DayC52.Chain, Offset(c.x + w * 0.01f * br, c.y - h * 0.02f), Size(w * 0.035f, h * 0.04f), alpha = f, style = Stroke(2f * dp))
        }
        tagAt(a.tag("tillsunrise"), Offset(w * 0.5f, h * 0.12f), f * seg(tsr, 0.6f, 0.8f), back = Back52, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. the covenant in the notebook (hisn-sayyid) ═════════════

/**
 * Beats: fourparts (four boxes written in turn: «التَّوْحِيدَ», «وَالْعَهْدَ», «وَالِاعْتِرَافَ بِالنِّعْمَةِ», «وَبِالذَّنْبِ»),
 * dayornight (a page split into day and night; said with certainty in either, a garden gate is drawn open at the
 * end), abu (the word «أَبُوءُ» with two arrows to «أُقِرُّ» and «وَأَعْتَرِفُ»), fivesteps (a stair of five steps drawn in
 * ink, the last lit), dayshukr (small signs of the day's blessings all point to one word: «فَمِنْكَ وَحْدَكَ»).
 */
internal fun DrawScope.covenantScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fourparts" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "dayornight" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "abu" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "fivesteps" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "dayshukr" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0f) { covenantPage(a) }
}

private fun DrawScope.covenantPage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val pp = Paper(a.dark)
    paperSheet(pp)
    val fp = a.b("fourparts")
    val dn = a.b("dayornight")
    val ab = a.b("abu")
    val fs = a.b("fivesteps")
    val ds = a.b("dayshukr")
    val pen = 2.2f * dp
    if (fp > 0f) {
        val f = easeOut(seg(fp, 0f, BEAT_IN)) * (1f - seg(dn, 0f, BEAT_OUT))
        for (k in 0 until 4) {
            val c = Offset(w * (0.2f + 0.2f * k), h * 0.5f + h * 0.08f * (k % 2))
            val g = seg(fp, 0.1f + 0.2f * k, 0.3f + 0.2f * k)
            penLine(listOf(c + Offset(-w * 0.08f, -h * 0.09f), c + Offset(w * 0.08f, -h * 0.09f), c + Offset(w * 0.08f, h * 0.09f), c + Offset(-w * 0.08f, h * 0.09f), c + Offset(-w * 0.08f, -h * 0.09f)), g, pp.ink, pen, f)
            note(a.tag("fourparts", 0, k), c, f * g, pp, pp.amber)
        }
    }
    if (dn > 0f) {
        val f = easeOut(seg(dn, 0f, BEAT_IN)) * (1f - seg(ab, 0f, BEAT_OUT))
        penLine(listOf(Offset(w * 0.5f, h * 0.15f), Offset(w * 0.5f, h * 0.85f)), seg(dn, 0.05f, 0.25f), pp.ink, pen, f)
        val sunC = Offset(w * 0.28f, h * 0.3f)
        for (k in 0 until 8) {
            val ang = k * PI.toFloat() / 4f
            penLine(listOf(sunC + Offset(cos(ang), sin(ang)) * (w * 0.04f), sunC + Offset(cos(ang), sin(ang)) * (w * 0.06f)), seg(dn, 0.15f, 0.35f), pp.amber, pen, f)
        }
        drawCircle(pp.amber, w * 0.03f, sunC, alpha = f * seg(dn, 0.1f, 0.25f), style = Stroke(pen))
        val moonC = Offset(w * 0.72f, h * 0.3f)
        drawArc(pp.ink, 60f, 240f, false, Offset(moonC.x - w * 0.03f, moonC.y - w * 0.03f), Size(w * 0.06f, w * 0.06f), alpha = f * seg(dn, 0.25f, 0.4f), style = Stroke(pen))
        for (side in 0 until 2) {
            val c = Offset(w * (0.28f + 0.44f * side), h * 0.68f)
            val g = seg(dn, 0.45f + 0.2f * side, 0.65f + 0.2f * side)
            penLine(listOf(c + Offset(-w * 0.05f, h * 0.1f), c + Offset(-w * 0.05f, -h * 0.06f), c + Offset(0f, -h * 0.12f), c + Offset(w * 0.05f, -h * 0.06f), c + Offset(w * 0.05f, h * 0.1f)), g, pp.good, pen, f)
        }
    }
    if (ab > 0f) {
        val f = easeOut(seg(ab, 0f, BEAT_IN)) * (1f - seg(fs, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.32f)
        note(a.tag("abu", 0, 0), c, f * seg(ab, 0.05f, 0.2f), pp, pp.amber, boxed = true)
        for (k in 0 until 2) {
            val end = Offset(w * (0.3f + 0.4f * k), h * 0.7f)
            penLine(listOf(c + Offset(0f, h * 0.06f), end + Offset(0f, -h * 0.07f)), seg(ab, 0.25f + 0.2f * k, 0.45f + 0.2f * k), pp.ink, pen, f)
            arrowHead(end + Offset(0f, -h * 0.07f), end - c, w * 0.02f, pp.ink, pen, f * seg(ab, 0.4f + 0.2f * k, 0.5f + 0.2f * k))
            note(a.tag("abu", 0, k + 1), end, f * seg(ab, 0.45f + 0.2f * k, 0.6f + 0.2f * k), pp)
        }
    }
    if (fs > 0f) {
        val f = easeOut(seg(fs, 0f, BEAT_IN)) * (1f - seg(ds, 0f, BEAT_OUT))
        val pts = mutableListOf(Offset(w * 0.15f, h * 0.82f))
        for (k in 0 until 5) {
            val x = w * (0.15f + 0.14f * k)
            val y = h * (0.82f - 0.12f * (k + 1))
            pts.add(Offset(x, y)); pts.add(Offset(x + w * 0.14f, y))
        }
        penLine(pts, seg(fs, 0.05f, 0.8f), pp.ink, pen, f)
        softGlow(Offset(w * 0.78f, h * 0.18f), w * 0.05f, pp.amber, 0.7f * f * seg(fs, 0.8f, 1f))
    }
    if (ds > 0f) {
        val f = easeOut(seg(ds, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.5f)
        note(a.tag("dayshukr"), c, f * seg(ds, 0.5f, 0.7f), pp, pp.amber, boxed = true)
        for (k in 0 until 8) {
            val ang = k * PI.toFloat() / 4f
            val p = c + Offset(cos(ang) * w * 0.32f, sin(ang) * h * 0.34f)
            val g = seg(ds, 0.05f + 0.05f * k, 0.2f + 0.05f * k)
            drawCircle(pp.good, w * 0.015f, p, alpha = f * g)
            penLine(listOf(p, p + (c - p) * 0.62f), seg(ds, 0.3f + 0.03f * k, 0.5f + 0.03f * k), pp.faint, pen * 0.8f, f)
        }
    }
}

// ═════════════ 5. nothing harms with His name (hisn-noharm) ═════════════

private object HarmC52 {
    val Sky = Color(0xFF151A30)
    val SkyLow = Color(0xFF34365A)
    val Ground = Color(0xFF24222E)
    val Wall = Color(0xFF6E6070)
    val Roof = Color(0xFF4A3E4E)
    val Gold = Color(0xFFE8C46A)
    val Dart = Color(0xFF8A6A7A)
    val Scorp = Color(0xFF6A4A3A)
    val Sapling = Color(0xFF5A9A5A)
    val Storm = Color(0xFF4A4A5A)
}

/**
 * Beats: bounce (a house under a dome; small darts fly at it and glance off, morning and evening), scorpion (a scorpion
 * crawls up to the door at night and turns away), threecontent (three phrases rise one after another: «رَضِيتُ بِاللَّهِ
 * رَبًّا», «وَبِالْإِسْلَامِ دِينًا», «وَبِمُحَمَّدٍ ﷺ نَبِيًّا»), heldup (a young plant that would fall is held upright by a thin
 * line of gold), sevenrings (seven rings close round a house and a storm cloud over it thins away).
 */
internal fun DrawScope.noharmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "bounce" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "scorpion" to Shot(Offset(w * 0.5f, h * 0.65f), 1.3f),
        "threecontent" to Shot(Offset(w * 0.5f, h * 0.45f), 1.1f),
        "heldup" to Shot(Offset(w * 0.5f, h * 0.55f), 1.3f),
        "sevenrings" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { harmNight(a) }
}

private fun DrawScope.harmNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bo = a.b("bounce")
    val sc = a.b("scorpion")
    val tc = a.b("threecontent")
    val hu = a.b("heldup")
    val sr = a.b("sevenrings")
    val ground = h * 0.82f
    nightSky(t, HarmC52.Sky, HarmC52.SkyLow, ground, starAlpha = 0.7f)
    drawRect(HarmC52.Ground, Offset(0f, ground), Size(w, h - ground))
    if (bo > 0f) {
        val f = easeOut(seg(bo, 0f, BEAT_IN)) * (1f - seg(sc, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, ground)
        house12(c, w * 0.2f, HarmC52.Wall, HarmC52.Roof, f, 0.7f)
        guardDome12(c, w * 0.2f, seg(bo, 0.05f, 0.3f), t, f)
        for (k in 0 until 6) {
            val u = fract(t * 0.6f + k / 6f)
            val side = if (k % 2 == 0) -1f else 1f
            val start = Offset(w * 0.5f + side * w * 0.48f, h * (0.25f + 0.08f * (k % 3)))
            val hit = Offset(w * 0.5f + side * w * 0.17f, ground - h * 0.18f)
            val p = if (u < 0.6f) start + (hit - start) * (u / 0.6f) else hit + Offset(side * w * 0.1f * (u - 0.6f) / 0.4f, -h * 0.15f * (u - 0.6f) / 0.4f)
            drawLine(HarmC52.Dart, p, p + Offset(side * w * 0.02f, 0f), 2f * dp, StrokeCap.Round, alpha = f * seg(bo, 0.2f, 0.35f) * (1f - u * 0.5f))
        }
    }
    if (sc > 0f) {
        val f = easeOut(seg(sc, 0f, BEAT_IN)) * (1f - seg(tc, 0f, BEAT_OUT))
        door12(Offset(w * 0.3f, ground), w * 0.12f, h * 0.38f, 0f, HarmC52.Wall, Color(0xFF3A2A2A), f)
        guardDome12(Offset(w * 0.3f, ground), w * 0.12f, 1f, t, f * seg(sc, 0.05f, 0.2f))
        val come = seg(sc, 0.1f, 0.5f)
        val back = easeInOut(seg(sc, 0.55f, 0.95f))
        val x = w * (0.85f - 0.4f * come + 0.4f * back)
        val y = ground - h * 0.02f
        val dir = if (back > 0f) 1f else -1f
        drawOval(HarmC52.Scorp, Offset(x - w * 0.03f, y - h * 0.015f), Size(w * 0.06f, h * 0.03f), alpha = f)
        for (k in 0 until 3) {
            drawLine(HarmC52.Scorp, Offset(x - w * 0.015f + w * 0.015f * k, y), Offset(x - w * 0.02f + w * 0.015f * k, y + h * 0.02f), 1.5f * dp, alpha = f)
        }
        val tail = Path().apply { moveTo(x - dir * w * 0.03f, y); quadraticTo(x - dir * w * 0.07f, y - h * 0.02f, x - dir * w * 0.055f, y - h * 0.06f); lineTo(x - dir * w * 0.045f, y - h * 0.05f) }
        drawPath(tail, HarmC52.Scorp, alpha = f, style = Stroke(2.5f * dp, cap = StrokeCap.Round))
        drawLine(HarmC52.Scorp, Offset(x + dir * w * 0.03f, y), Offset(x + dir * w * 0.05f, y - h * 0.015f), 2f * dp, StrokeCap.Round, alpha = f)
    }
    if (tc > 0f) {
        val f = easeOut(seg(tc, 0f, BEAT_IN)) * (1f - seg(hu, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val g = seg(tc, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            val y = h * (0.7f - 0.2f * k) - h * 0.05f * g
            softGlow(Offset(w * 0.5f, y), w * 0.08f, HarmC52.Gold, 0.3f * f * g)
            tagAt(a.tag("threecontent", 0, k), Offset(w * 0.5f, y), f * g, back = Back52, ink = ArtInk.Gold)
        }
    }
    if (hu > 0f) {
        val f = easeOut(seg(hu, 0f, BEAT_IN)) * (1f - seg(sr, 0f, BEAT_OUT))
        val base = Offset(w * 0.5f, ground)
        val lean = 0.5f * (1f - easeInOut(seg(hu, 0.3f, 0.6f)))
        val top = base + Offset(sin(lean) * h * 0.3f, -cos(lean) * h * 0.3f)
        drawLine(HarmC52.Sapling, base, top, 3f * dp, StrokeCap.Round, alpha = f)
        for (k in 0 until 4) drawOval(HarmC52.Sapling, Offset(top.x - w * 0.02f + w * 0.02f * (k % 2), top.y - h * 0.03f + h * 0.04f * (k / 2)), Size(w * 0.03f, h * 0.02f), alpha = f)
        drawLine(HarmC52.Gold, top, Offset(top.x, h * 0.04f), 1.2f * dp, alpha = f * seg(hu, 0.2f, 0.4f))
    }
    if (sr > 0f) {
        val f = easeOut(seg(sr, 0f, 0.3f))
        val c = Offset(w * 0.5f, ground)
        house12(c, w * 0.18f, HarmC52.Wall, HarmC52.Roof, f, 0.7f)
        for (k in 0 until 7) {
            val g = seg(sr, 0.05f + 0.08f * k, 0.2f + 0.08f * k)
            val rr = w * (0.12f + 0.03f * k)
            drawArc(HarmC52.Gold, 180f, 180f, false, Offset(c.x - rr, c.y - rr * 0.9f), Size(rr * 2f, rr * 1.8f), alpha = f * g * 0.6f, style = Stroke(1.4f * dp))
        }
        cloud38(Offset(w * 0.5f, h * 0.2f), w * 0.14f, HarmC52.Storm, f * (1f - seg(sr, 0.6f, 0.95f)))
    }
}
