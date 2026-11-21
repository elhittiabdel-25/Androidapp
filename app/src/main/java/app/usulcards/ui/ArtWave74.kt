package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 74 (2026-11-03): v1.3 fiqh, «المختصر في أحكام التراويح» (al-ʿAmmārī). The night prayer in rows under the lamps of the mosque;
 * the count of rakʿas as pairs of arches and one odd one; praying with the imam to the end; the witr; the qunut. Worshippers are
 * faceless figures facing left, towards the qibla; the imam is only one of them, in front.
 */

internal object HallC {
    val Wall = Color(0xFF2A2436)
    val Arch = Color(0xFF1C1828)
    val Column = Color(0xFF4A4258)
    val Carpet = Color(0xFF6A2A34)
    val Stripe = Color(0xFF8A3A44)
    val Lamp = Color(0xFFF2C860)
    val Person = Color(0xFFE8DCC8)
    val Gold = Color(0xFFF2C860)
    val Pair = Color(0xFFD8C8A0)
    val Night = Color(0xFF0E1430)
}

/** The prayer hall at night: a wall of arches, columns, a striped carpet from [floor] down and lamps hanging from the roof. */
internal fun DrawScope.hall74(floor: Float, t: Float, lamps: Float = 1f) {
    val w = size.width
    val h = size.height
    drawRect(HallC.Wall, Offset.Zero, Size(w, floor))
    for (k in 0 until 4) {
        val x = w * (0.125f + 0.25f * k)
        drawPath(archPath26(x, h * 0.18f, floor, w * 0.085f), HallC.Arch)
        drawRect(HallC.Column, Offset(x + w * 0.11f, h * 0.12f), Size(w * 0.03f, floor - h * 0.12f))
    }
    drawRect(HallC.Carpet, Offset(0f, floor), Size(w, h - floor))
    for (k in 0 until 4) drawLine(HallC.Stripe, Offset(0f, floor + (h - floor) * (0.22f + 0.24f * k)), Offset(w, floor + (h - floor) * (0.22f + 0.24f * k)), 2f * density)
    if (lamps > 0.01f) for (k in 0 until 4) {
        val x = w * (0.125f + 0.25f * k)
        val y = h * (0.1f + 0.015f * sin(t * 1.3f + k))
        drawLine(HallC.Column, Offset(x, 0f), Offset(x, y), 1.2f * density, alpha = lamps)
        softGlow(Offset(x, y + h * 0.015f), w * 0.05f, HallC.Lamp, 0.55f * lamps)
        drawCircle(HallC.Lamp, w * 0.012f, Offset(x, y + h * 0.015f), alpha = lamps)
    }
}

/** A row of [n] worshippers from [x0] to [x1] on the line [y]; [pose] 0 standing, 0.5 bowing, 1 prostrate. */
internal fun DrawScope.row74(y: Float, x0: Float, x1: Float, n: Int, tall: Float, color: Color, alpha: Float, pose: Float = 0f) {
    if (alpha <= 0.01f) return
    for (k in 0 until n) {
        val foot = Offset(mix(x0, x1, if (n == 1) 0f else k / (n - 1f)), y)
        if (pose <= 0.01f) pray12(foot, tall, color, alpha) else bow12(foot, tall, color, alpha, pose)
    }
}

/** A rakʿa as a small arch, its foot at [foot]; [lit] fills it with gold. */
internal fun DrawScope.rakah74(foot: Offset, s: Float, lit: Float, alpha: Float, odd: Boolean = false) {
    if (alpha <= 0.01f) return
    val col = if (odd) HallC.Gold else HallC.Pair
    val p = archPath26(foot.x, foot.y - s, foot.y, s * 0.32f)
    if (lit > 0f) drawPath(p, col, alpha = alpha * lit * 0.85f)
    drawPath(p, col, alpha = alpha, style = Stroke(s * 0.06f))
}

// ═════════════ 1. tar-hukm: the night prayer of Ramadan ═════════════

/**
 * Beats: nightrows (rows standing in the lit hall, the crescent in a window: «جَمَاعَةً فِي لَيَالِي رَمَضَانَ»), afterisha (a band of the night:
 * the ʿishāʾ first, then the long light of the tarawih after it: «بَعْدَ صَلَاةِ الْعِشَاءِ»), confirmedsunnah (many small lights come together
 * into one seal: «سُنَّةٌ مُؤَكَّدَةٌ», «بِالْإِجْمَاعِ»), firstnight (the new crescent appears and the first night's rows light: «إِذَا رُئِيَ
 * الْهِلَالُ»), noadhan (the minaret is silent, its call struck out: «وَلَا يُؤَذَّنُ لَهَا وَلَا يُقَامُ»).
 */
internal fun DrawScope.tarawihnightScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "nightrows" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "afterisha" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "confirmedsunnah" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "firstnight" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "noadhan" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { tarawihNightDraw(a) }
}

private fun DrawScope.tarawihNightDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.6f
    hall74(floor, t)
    val nr = fqIn(a, "nightrows", "afterisha")
    val ai = fqIn(a, "afterisha", "confirmedsunnah")
    val cs = fqIn(a, "confirmedsunnah", "firstnight")
    val fn = fqIn(a, "firstnight", "noadhan")
    val na = fqIn(a, "noadhan")
    val rows = maxOf(nr, fn)
    if (rows > 0f) {
        val lit = if (fn > 0f) seg(a.b("firstnight"), 0.3f, 0.6f) else 1f
        row74(floor + h * 0.16f, w * 0.12f, w * 0.88f, 6, h * 0.2f, HallC.Person, rows * lit)
        row74(floor + h * 0.34f, w * 0.12f, w * 0.88f, 6, h * 0.22f, HallC.Person, rows * lit)
    }
    if (nr > 0f) {
        val p = a.b("nightrows")
        drawRect(HallC.Night, Offset(w * 0.08f, h * 0.24f), Size(w * 0.09f, h * 0.16f), alpha = nr)
        crescent12(Offset(w * 0.125f, h * 0.31f), w * 0.022f, HallC.Gold, HallC.Night, nr)
        tagAt(a.tag("nightrows"), Offset(w * 0.55f, h * 0.36f), nr * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ai > 0f) {
        val p = a.b("afterisha")
        drawRect(HallC.Night, Offset.Zero, size, alpha = 0.75f * ai)
        val y = h * 0.5f
        val x0 = w * 0.9f
        val x1 = w * 0.1f
        drawLine(Color(0xFF3A4468), Offset(x0, y), Offset(x1, y), h * 0.05f, StrokeCap.Round, alpha = ai)
        val isha = seg(p, 0.05f, 0.25f)
        drawLine(Color(0xFF8A94B8), Offset(x0 - w * 0.05f, y), Offset(x0 - w * 0.05f - w * 0.1f * isha, y), h * 0.05f, StrokeCap.Round, alpha = ai)
        val tar = easeOut(seg(p, 0.35f, 0.8f))
        drawLine(HallC.Gold, Offset(x0 - w * 0.2f, y), Offset(x0 - w * 0.2f - w * 0.45f * tar, y), h * 0.05f, StrokeCap.Round, alpha = ai * 0.85f)
        crescent12(Offset(x0 - w * 0.4f, y - h * 0.12f), w * 0.025f, HallC.Gold, HallC.Night, ai)
        tagAt(a.tag("afterisha"), Offset(w * 0.5f, y + h * 0.12f), ai * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (cs > 0f) {
        val p = a.b("confirmedsunnah")
        drawRect(HallC.Night, Offset.Zero, size, alpha = 0.75f * cs)
        val c = Offset(w * 0.5f, h * 0.48f)
        val come = easeInOut(seg(p, 0.1f, 0.6f))
        for (k in 0 until 16) {
            val ang = 2f * PI.toFloat() * k / 16f
            val far = c + Offset(cos(ang) * w * 0.45f, sin(ang) * h * 0.38f)
            drawCircle(HallC.Gold, w * 0.01f, far + (c - far) * come, alpha = cs * (1f - seg(p, 0.6f, 0.7f)))
        }
        val seal = seg(p, 0.55f, 0.75f)
        softGlow(c, w * 0.16f, HallC.Gold, 0.6f * cs * seal)
        drawCircle(HallC.Gold, w * 0.1f, c, alpha = cs * seal, style = Stroke(3f * dp))
        drawPath(starPath(c, w * 0.05f), HallC.Gold, alpha = cs * seal)
        tagAt(a.tag("confirmedsunnah", 0, 0), Offset(w * 0.5f, h * 0.2f), cs * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("confirmedsunnah", 0, 1), Offset(w * 0.5f, h * 0.76f), cs * seal, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (fn > 0f) {
        val p = a.b("firstnight")
        drawRect(HallC.Night, Offset(w * 0.08f, h * 0.24f), Size(w * 0.09f, h * 0.16f), alpha = fn)
        val m = seg(p, 0.05f, 0.3f)
        softGlow(Offset(w * 0.125f, h * 0.31f), w * 0.05f, HallC.Gold, 0.6f * fn * m)
        crescent12(Offset(w * 0.125f, h * 0.31f), w * 0.022f, HallC.Gold, HallC.Night, fn * m)
        tagAt(a.tag("firstnight"), Offset(w * 0.55f, h * 0.36f), fn * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (na > 0f) {
        val p = a.b("noadhan")
        drawRect(HallC.Night, Offset.Zero, size, alpha = 0.8f * na)
        val base = Offset(w * 0.5f, h * 0.85f)
        drawRect(Color(0xFFD8CCB4), Offset(base.x - w * 0.04f, base.y - h * 0.55f), Size(w * 0.08f, h * 0.55f), alpha = na)
        drawArc(Color(0xFF4E7A6A), 180f, 180f, true, Offset(base.x - w * 0.06f, base.y - h * 0.62f), Size(w * 0.12f, h * 0.1f), alpha = na)
        val top = base + Offset(0f, -h * 0.5f)
        voice12(top + Offset(-w * 0.1f, 0f), w * 0.1f, t, Color(0xFF8AC4EA), na * (1f - seg(p, 0.35f, 0.55f)), -1f)
        voice12(top + Offset(w * 0.1f, 0f), w * 0.1f, t, Color(0xFF8AC4EA), na * (1f - seg(p, 0.35f, 0.55f)), 1f)
        cross14(top + Offset(-w * 0.18f, 0f), w * 0.05f, seg(p, 0.3f, 0.5f))
        cross14(top + Offset(w * 0.18f, 0f), w * 0.05f, seg(p, 0.35f, 0.55f))
        tagAt(a.tag("noadhan"), Offset(w * 0.5f, h * 0.14f), na * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 2. tar-adad: how many rakʿas ═════════════

/**
 * Beats: twentyrakat (twenty small arches in pairs: «20 رَكْعَةً»), elevenrakat (five pairs and one gold arch alone: «عِنْدَ الْمُؤَلِّفِ», «11
 * رَكْعَةً بِالْوِتْرِ»), twobytwo (pair after pair keeps coming: «مَثْنَى مَثْنَى»), imamcare (the one in front, and the row behind him, one of
 * them bent with age; a warm line joins them: «يُرَاعِي الْإِمَامُ حَالَ الْمَأْمُومِينَ»), heartbest (one praying alone with a light at his
 * heart: «مَا هُوَ أَصْلَحُ لِقَلْبِهِ»).
 */
internal fun DrawScope.rakatcountScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twentyrakat" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "elevenrakat" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "twobytwo" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "imamcare" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
        "heartbest" to Shot(Offset(w * 0.5f, h * 0.65f), 1.12f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { rakatCountDraw(a) }
}

private fun DrawScope.rakatCountDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val tw = fqIn(a, "twentyrakat", "elevenrakat")
    val el = fqIn(a, "elevenrakat", "twobytwo")
    val tb = fqIn(a, "twobytwo", "imamcare")
    val ic = fqIn(a, "imamcare", "heartbest")
    val hb = fqIn(a, "heartbest")
    val board = maxOf(tw, el, tb)
    if (board > 0f) drawRect(HallC.Night, Offset.Zero, size, alpha = 0.75f * board)
    if (tw > 0f) {
        val p = a.b("twentyrakat")
        for (i in 0 until 20) {
            val pair = i / 2
            val row = pair / 5
            val x = w * 0.88f - (pair % 5) * w * 0.17f - (i % 2) * w * 0.06f
            rakah74(Offset(x, h * (0.42f + 0.22f * row)), h * 0.12f, seg(p, 0.03f * i, 0.03f * i + 0.08f), tw)
        }
        tagAt(a.tag("twentyrakat"), Offset(w * 0.5f, h * 0.84f), tw * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (el > 0f) {
        val p = a.b("elevenrakat")
        for (i in 0 until 11) {
            val odd = i == 10
            val x = w * 0.9f - (i / 2) * w * 0.15f - (i % 2) * w * 0.05f
            rakah74(Offset(x, h * 0.55f), h * 0.14f, seg(p, 0.1f + 0.05f * i, 0.2f + 0.05f * i), el, odd)
            if (odd) softGlow(Offset(x, h * 0.48f), w * 0.07f, HallC.Gold, el * seg(p, 0.6f, 0.75f))
        }
        tagAt(a.tag("elevenrakat", 0, 0), Offset(w * 0.5f, h * 0.22f), el * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("elevenrakat", 0, 1), Offset(w * 0.5f, h * 0.72f), el * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tb > 0f) {
        val p = a.b("twobytwo")
        for (k in 0 until 4) {
            val u = fract(t * 0.25f + k / 4f)
            val x = w * (1.05f - 1.1f * u)
            for (j in 0 until 2) rakah74(Offset(x - j * w * 0.05f, h * 0.55f), h * 0.14f, 1f, tb * bump(u))
        }
        tagAt(a.tag("twobytwo"), Offset(w * 0.5f, h * 0.24f), tb * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ic > 0f) {
        val p = a.b("imamcare")
        val imam = Offset(w * 0.2f, floor + h * 0.14f)
        pray12(imam, h * 0.22f, HallC.Person, ic)
        for (k in 0 until 4) {
            val foot = Offset(w * (0.45f + 0.14f * k), floor + h * 0.3f)
            if (k == 2) figure(foot, h * 0.21f, HallC.Person, ic, lean = 0.25f)
            else pray12(foot, h * 0.22f, HallC.Person, ic)
        }
        val link = seg(p, 0.3f, 0.6f)
        drawLine(HallC.Gold, imam + Offset(0f, -h * 0.18f), imam + Offset(0f, -h * 0.18f) + (Offset(w * 0.73f, floor + h * 0.1f) - (imam + Offset(0f, -h * 0.18f))) * link, 2f * dp, StrokeCap.Round, alpha = ic * 0.7f)
        softGlow(Offset(w * 0.73f, floor + h * 0.12f), w * 0.06f, HallC.Gold, 0.5f * ic * seg(p, 0.55f, 0.7f))
        tagAt(a.tag("imamcare"), Offset(w * 0.5f, h * 0.38f), ic * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (hb > 0f) {
        val p = a.b("heartbest")
        drawRect(HallC.Night, Offset.Zero, size, alpha = 0.5f * hb)
        val foot = Offset(w * 0.5f, floor + h * 0.25f)
        pray12(foot, h * 0.3f, HallC.Person, hb)
        val heart = foot + Offset(-h * 0.02f, -h * 0.2f)
        softGlow(heart, w * 0.1f, HallC.Gold, hb * seg(p, 0.2f, 0.4f) * (0.7f + 0.3f * sin(t * 2f)))
        heart12(heart, w * 0.04f, HallC.Gold, hb * seg(p, 0.2f, 0.4f), fill = true)
        tagAt(a.tag("heartbest"), Offset(w * 0.5f, h * 0.3f), hb * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 3. tar-imam: with the imam until he leaves ═════════════

/**
 * Beats: firsttolast (the moon crosses the whole night above a row that stays from the first rakʿa to the last: «مِنْ أَوَّلِ الصَّلَاةِ حَتَّى
 * آخِرِهَا»), twoimams (the night in two halves under two imams; one leaves at the half and his moon stops half way: «لَمْ يَنَلْ ذَلِكَ
 * الْفَضْلَ», «لِأَنَّ الصَّلَاةَ وَاحِدَةٌ»), evenitout (a gold odd arch, then one more added beside it to make a pair: «أَوْتَرَ مَعَهُ», «بِرَكْعَةٍ
 * تُشْفِعُ صَلَاتَهُ»), rowsfill (the rows fill with more and more: «شُهُودُ الْخَيْرِ», «وَتَكْثِيرُ سَوَادِ الْمُسْلِمِينَ», «وَنَشَاطُ
 * النَّفْسِ»).
 */
internal fun DrawScope.withimamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "firsttolast" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "twoimams" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "evenitout" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "rowsfill" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { withImamDraw(a) }
}

private fun DrawScope.withImamDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val fl = fqIn(a, "firsttolast", "twoimams")
    val ti = fqIn(a, "twoimams", "evenitout")
    val ev = fqIn(a, "evenitout", "rowsfill")
    val rf = fqIn(a, "rowsfill")
    val nightArc = { prog: Float, alpha: Float, stopAt: Float ->
        val c = Offset(w * 0.5f, h * 0.5f)
        val r = w * 0.38f
        drawArc(Color(0xFF8A94B8), 180f, 180f, false, Offset(c.x - r, c.y - r * 0.8f), Size(r * 2f, r * 1.6f), alpha = alpha * 0.5f, style = Stroke(1.5f * dp, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp))))
        val u = minOf(prog, stopAt)
        drawArc(HallC.Gold, 180f, 180f * u, false, Offset(c.x - r, c.y - r * 0.8f), Size(r * 2f, r * 1.6f), alpha = alpha, style = Stroke(4f * dp, cap = StrokeCap.Round))
        val ang = PI.toFloat() * (1f + u)
        moon12(c + Offset(cos(ang) * r, sin(ang) * r * 0.8f), w * 0.03f, alpha)
    }
    if (fl > 0f) {
        val p = a.b("firsttolast")
        drawRect(HallC.Night, Offset(0f, 0f), Size(w, floor), alpha = 0.6f * fl)
        nightArc(easeInOut(seg(p, 0.1f, 0.85f)), fl, 1f)
        row74(floor + h * 0.22f, w * 0.15f, w * 0.85f, 6, h * 0.2f, HallC.Person, fl)
        tagAt(a.tag("firsttolast"), Offset(w * 0.5f, h * 0.9f), fl * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ti > 0f) {
        val p = a.b("twoimams")
        drawRect(HallC.Night, Offset(0f, 0f), Size(w, floor), alpha = 0.6f * ti)
        nightArc(easeInOut(seg(p, 0.05f, 0.8f)), ti, 0.5f)
        val leave = easeInOut(seg(p, 0.4f, 0.7f))
        for (k in 0 until 6) {
            val foot = Offset(w * (0.15f + 0.14f * k), floor + h * 0.22f)
            if (k == 3) figure(foot + Offset(w * 0.5f * leave, 0f), h * 0.2f, HallC.Person, ti * (1f - seg(p, 0.65f, 0.8f)), walk = if (leave > 0f && leave < 1f) t * 6f else 0f)
            else pray12(foot, h * 0.2f, HallC.Person, ti)
        }
        tagAt(a.tag("twoimams", 0, 0), Offset(w * 0.5f, h * 0.1f), ti * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Ember)
        tagAt(a.tag("twoimams", 0, 1), Offset(w * 0.5f, h * 0.9f), ti * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ev > 0f) {
        val p = a.b("evenitout")
        drawRect(HallC.Night, Offset.Zero, size, alpha = 0.75f * ev)
        val y = h * 0.6f
        for (k in 0 until 3) rakah74(Offset(w * (0.82f - 0.07f * k), y), h * 0.13f, 1f, ev * 0.6f, k == 2)
        val odd = Offset(w * 0.6f, y)
        rakah74(odd, h * 0.13f, seg(p, 0.05f, 0.2f), ev, true)
        tagAt(a.tag("evenitout", 0, 0), Offset(w * 0.7f, h * 0.3f), ev * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        val add = seg(p, 0.45f, 0.6f)
        rakah74(Offset(w * 0.53f, y), h * 0.13f, add, ev * add, false)
        if (add > 0f) drawLine(HallC.Pair, Offset(w * 0.53f, y + h * 0.03f), Offset(w * 0.6f, y + h * 0.03f), 2f * dp, alpha = ev * add)
        for (k in 0 until 2) rakah74(Offset(w * (0.36f - 0.07f * k), y), h * 0.13f, seg(p, 0.7f + 0.08f * k, 0.8f + 0.08f * k), ev * seg(p, 0.7f, 0.8f), false)
        tagAt(a.tag("evenitout", 0, 1), Offset(w * 0.45f, h * 0.8f), ev * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (rf > 0f) {
        val p = a.b("rowsfill")
        for (r in 0 until 3) {
            val y = floor + h * (0.1f + 0.13f * r)
            val n = (2 + (seg(p, 0.05f + 0.15f * r, 0.4f + 0.15f * r) * 6f).toInt()).coerceAtMost(8)
            row74(y, w * 0.1f, w * (0.1f + 0.1f * (n - 1)), n, h * (0.15f + 0.02f * r), HallC.Person, rf)
        }
        for (k in 0 until 3) tagAt(a.tag("rowsfill", 0, k), Offset(w * 0.5f, h * (0.12f + 0.11f * k)), rf * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. tar-witr: the witr at the close ═════════════

/**
 * Beats: onerakah (one gold arch alone: «بِرَكْعَةٍ وَاحِدَةٍ»), apartjoined (two arches and one apart, then the three drawn together: «فَمَا
 * قَبْلَهَا شَفْعٌ», «فَالْجَمِيعُ وِتْرٌ»), twoways (two rows: two and one with a seal between, or three joined: «بِتَشَهُّدَيْنِ وَتَسْلِيمَتَيْنِ»,
 * «ثَلَاثًا سَرْدًا بِتَشَهُّدٍ وَاحِدٍ»), threesurahs (a page over each of the three arches: «الْأَعْلَى», «وَالْكَافِرُونَ», «وَالْإِخْلَاصِ»),
 * subhanmalik (the words rise three times, the third the loudest: «سُبْحَانَ الْمَلِكِ الْقُدُّوسِ»).
 */
internal fun DrawScope.witrcloseScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "singlewitr" to Shot(Offset(w * 0.5f, h * 0.55f), 1.12f),
        "apartjoined" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "twowitrforms" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "witrsurahs" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "subhanmalik" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { witrCloseDraw(a) }
}

private fun DrawScope.witrCloseDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    nightSky(t, HallC.Night, Color(0xFF2E2A5A), h, starAlpha = 0.6f)
    val orr = fqIn(a, "singlewitr", "apartjoined")
    val aj = fqIn(a, "apartjoined", "twowitrforms")
    val tw = fqIn(a, "twowitrforms", "witrsurahs")
    val ts = fqIn(a, "witrsurahs", "subhanmalik")
    val sm = fqIn(a, "subhanmalik")
    val y = h * 0.62f
    val s = h * 0.2f
    if (orr > 0f) {
        val p = a.b("singlewitr")
        softGlow(Offset(w * 0.5f, y - s * 0.5f), w * 0.15f, HallC.Gold, 0.5f * orr * seg(p, 0.1f, 0.3f))
        rakah74(Offset(w * 0.5f, y), s, seg(p, 0.1f, 0.3f), orr, true)
        tagAt(a.tag("singlewitr"), Offset(w * 0.5f, y + h * 0.1f), orr * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (aj > 0f) {
        val p = a.b("apartjoined")
        val join = easeInOut(seg(p, 0.5f, 0.75f))
        val xs = listOf(0.72f, 0.58f, mix(0.3f, 0.44f, join))
        for (k in 0 until 3) rakah74(Offset(w * xs[k], y), s, 1f, aj, k == 2 || join > 0.5f)
        if (join > 0.9f) drawLine(HallC.Gold, Offset(w * 0.4f, y + h * 0.03f), Offset(w * 0.76f, y + h * 0.03f), 3f * dp, StrokeCap.Round, alpha = aj)
        tagAt(a.tag("apartjoined", 0, 0), Offset(w * 0.65f, h * 0.3f), aj * seg(p, 0.05f, 0.25f) * (1f - seg(p, 0.5f, 0.6f)), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("apartjoined", 0, 1), Offset(w * 0.55f, h * 0.3f), aj * seg(p, 0.75f, 0.9f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tw > 0f) {
        val p = a.b("twowitrforms")
        for (r in 0 until 2) {
            val g = seg(p, 0.05f + 0.4f * r, 0.25f + 0.4f * r)
            val yy = h * (0.42f + 0.3f * r)
            val xs = if (r == 0) listOf(0.75f, 0.63f, 0.4f) else listOf(0.7f, 0.58f, 0.46f)
            for (k in 0 until 3) rakah74(Offset(w * xs[k], yy), s * 0.75f, 1f, tw * g, k == 2)
            if (r == 0) {
                drawCircle(HallC.Gold, w * 0.018f, Offset(w * 0.515f, yy - s * 0.3f), alpha = tw * g, style = Stroke(2f * dp))
                drawCircle(HallC.Gold, w * 0.018f, Offset(w * 0.33f, yy - s * 0.3f), alpha = tw * g, style = Stroke(2f * dp))
            } else drawCircle(HallC.Gold, w * 0.018f, Offset(w * 0.39f, yy - s * 0.3f), alpha = tw * g, style = Stroke(2f * dp))
            tagAt(a.tag("twowitrforms", 0, r), Offset(w * 0.55f, yy + h * 0.07f), tw * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (ts > 0f) {
        val p = a.b("witrsurahs")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.25f * k, 0.25f + 0.25f * k)
            val x = w * (0.78f - 0.28f * k)
            rakah74(Offset(x, y), s, 0.4f + 0.6f * g, ts, k == 2)
            scroll14(Offset(x, y - s - h * 0.12f), w * 0.2f, h * 0.12f, g, ts * g)
            tagAt(a.tag("witrsurahs", 0, k), Offset(x, y - s - h * 0.12f), ts * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    if (sm > 0f) {
        val p = a.b("subhanmalik")
        for (k in 0 until 3) {
            val g = seg(p, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            val c = Offset(w * 0.5f, h * (0.7f - 0.2f * k))
            voice12(c + Offset(w * 0.15f, 0f), w * (0.05f + 0.04f * k), t, HallC.Gold, sm * g, 1f)
            voice12(c - Offset(w * 0.15f, 0f), w * (0.05f + 0.04f * k), t, HallC.Gold, sm * g, -1f)
            if (k == 2) softGlow(c, w * 0.2f, HallC.Gold, 0.4f * sm * g)
        }
        tagAt(a.tag("subhanmalik"), Offset(w * 0.5f, h * 0.3f), sm * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. tar-qunut: the qunut in the witr ═════════════

/**
 * Beats: qunutsunnah (two raised hands light up; then a tick, for the prayer stays sound without it: «سُنَّةٌ», «وَمَنْ تَرَكَهُ فَصَلَاتُهُ
 * صَحِيحَةٌ»), afterbow (one bows, rises, then lifts his hands: «بَعْدَ الرُّكُوعِ», «وَيَجُوزُ قَبْلَهُ»), wordsfree (light rises from the
 * hands in many shapes, and one page shines: «وَلَا يَتَعَيَّنُ فِيهِ لَفْظٌ مُعَيَّنٌ», «وَالْأَفْضَلُ الدُّعَاءُ بِالْوَارِدِ»), aminecho (the one in
 * front raises his voice and the rows answer: «لِأَجْلِ التَّأْمِينِ»), notoverlong (the hourglass is turned before the row tires: «لَا يُطِيلُ
 * عَلَى النَّاسِ»).
 */
internal fun DrawScope.qunuthandsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "qunutsunnah" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "afterbow" to Shot(Offset(w * 0.5f, h * 0.65f), 1.1f),
        "wordsfree" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "aminecho" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
        "notoverlong" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { qunutHandsDraw(a) }
}

private fun DrawScope.qunutHandsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val qs = fqIn(a, "qunutsunnah", "afterbow")
    val ab = fqIn(a, "afterbow", "wordsfree")
    val wf = fqIn(a, "wordsfree", "aminecho")
    val ae = fqIn(a, "aminecho", "notoverlong")
    val no = fqIn(a, "notoverlong")
    if (qs > 0f) {
        val p = a.b("qunutsunnah")
        drawRect(HallC.Night, Offset.Zero, size, alpha = 0.6f * qs)
        val c = Offset(w * 0.5f, h * 0.55f)
        softGlow(c + Offset(0f, -h * 0.1f), w * 0.2f, HallC.Gold, 0.45f * qs * seg(p, 0.1f, 0.3f))
        hands12(c, h * 0.26f, HallC.Person, qs)
        tagAt(a.tag("qunutsunnah", 0, 0), Offset(w * 0.5f, h * 0.2f), qs * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tick14(Offset(w * 0.82f, h * 0.45f), w * 0.05f, seg(p, 0.55f, 0.7f))
        tagAt(a.tag("qunutsunnah", 0, 1), Offset(w * 0.5f, h * 0.88f), qs * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (ab > 0f) {
        val p = a.b("afterbow")
        val foot = Offset(w * 0.5f, floor + h * 0.25f)
        val bowIn = seg(p, 0.05f, 0.2f)
        val bowOut = seg(p, 0.3f, 0.45f)
        val lift = seg(p, 0.5f, 0.65f)
        if (lift <= 0f) bow12(foot, h * 0.3f, HallC.Person, ab, 0.5f * bowIn * (1f - bowOut))
        else {
            pray12(foot, h * 0.3f, HallC.Person, ab * (1f - lift))
            figure(foot, h * 0.3f, HallC.Person, ab * lift)
            hands12(foot + Offset(0f, -h * 0.26f), h * 0.1f, HallC.Person, ab * lift)
            softGlow(foot + Offset(0f, -h * 0.3f), w * 0.07f, HallC.Gold, 0.5f * ab * lift)
        }
        tagAt(a.tag("afterbow", 0, 0), Offset(w * 0.5f, h * 0.18f), ab * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("afterbow", 0, 1), Offset(w * 0.5f, h * 0.3f), ab * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (wf > 0f) {
        val p = a.b("wordsfree")
        drawRect(HallC.Night, Offset.Zero, size, alpha = 0.6f * wf)
        val c = Offset(w * 0.5f, h * 0.7f)
        hands12(c, h * 0.2f, HallC.Person, wf)
        for (k in 0 until 9) {
            val u = fract(t * 0.3f + k / 9f)
            val x = c.x + sin(k * 2.1f + u * 4f) * w * 0.2f * u
            val pos = Offset(x, c.y - h * 0.12f - u * h * 0.45f)
            when (k % 3) {
                0 -> drawCircle(HallC.Gold, w * 0.01f, pos, alpha = wf * (1f - u))
                1 -> drawPath(starPath(pos, w * 0.012f), HallC.Gold, alpha = wf * (1f - u))
                else -> drawRect(HallC.Gold, Offset(pos.x - w * 0.008f, pos.y - w * 0.008f), Size(w * 0.016f, w * 0.016f), alpha = wf * (1f - u))
            }
        }
        tagAt(a.tag("wordsfree", 0, 0), Offset(w * 0.5f, h * 0.14f), wf * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        val g = seg(p, 0.5f, 0.7f)
        scroll14(Offset(w * 0.8f, h * 0.4f), w * 0.22f, h * 0.16f, g, wf * g)
        softGlow(Offset(w * 0.8f, h * 0.4f), w * 0.12f, HallC.Gold, 0.4f * wf * g)
        tagAt(a.tag("wordsfree", 0, 1), Offset(w * 0.5f, h * 0.26f), wf * g, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ae > 0f) {
        val p = a.b("aminecho")
        val imam = Offset(w * 0.15f, floor + h * 0.12f)
        figure(imam, h * 0.22f, HallC.Person, ae)
        hands12(imam + Offset(0f, -h * 0.19f), h * 0.08f, HallC.Person, ae)
        voice12(imam + Offset(w * 0.08f, -h * 0.2f), w * 0.08f, t, HallC.Gold, ae * seg(p, 0.1f, 0.25f), 1f)
        for (k in 0 until 5) {
            val foot = Offset(w * (0.38f + 0.12f * k), floor + h * 0.3f)
            figure(foot, h * 0.2f, HallC.Person, ae)
            voice12(foot + Offset(-w * 0.03f, -h * 0.2f), w * 0.03f, t + k, Color(0xFF8AC4EA), ae * seg(p, 0.4f + 0.05f * k, 0.5f + 0.05f * k), -1f)
        }
        tagAt(a.tag("aminecho"), Offset(w * 0.5f, h * 0.36f), ae * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (no > 0f) {
        val p = a.b("notoverlong")
        row74(floor + h * 0.28f, w * 0.38f, w * 0.88f, 5, h * 0.2f, HallC.Person, no)
        val glass = Offset(w * 0.17f, h * 0.45f)
        val run = seg(p, 0.05f, 0.55f)
        val turned = seg(p, 0.55f, 0.7f)
        hourglass73(glass, h * 0.2f, if (turned > 0.5f) 0f else 0.8f * run, Fq.Sand, no)
        tagAt(a.tag("notoverlong"), Offset(w * 0.55f, h * 0.36f), no * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
