package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
 * Wave 84 (2026-11-03): v1.3 fiqh, prayer 5: shortening and joining on a journey, the Friday prayer and its manners, and the two
 * eids. Rakʿas are small arches and the five prayers are lights on the day's line; worshippers are faceless figures; where a woman
 * is among those for whom Friday is not obligatory she is the plain covered silhouette, never more.
 */

/** A minbar of three steps with its seat at the top, its foot at [foot], rising to the right. */
private fun DrawScope.minbar84(foot: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val wood = Color(0xFF8A6A48)
    for (k in 0 until 3) drawRect(wood, Offset(foot.x + s * 0.15f * k, foot.y - s * 0.15f * (k + 1)), Size(s * 0.15f, s * 0.15f * (k + 1)), alpha = alpha)
    drawRect(wood, Offset(foot.x + s * 0.45f, foot.y - s * 0.7f), Size(s * 0.06f, s * 0.7f), alpha = alpha)
    drawPath(archPath26(foot.x + s * 0.48f, foot.y - s * 0.95f, foot.y - s * 0.7f, s * 0.08f), SalC.Dome, alpha = alpha)
}

/** The five prayers as lights on the day's line from dawn (right) to night (left); returns their centres. */
private fun prayerDots84(w: Float, y: Float): List<Offset> = listOf(0.88f, 0.66f, 0.5f, 0.3f, 0.14f).map { Offset(w * it, y) }

// ═════════════ 25. sal-qasr: shortening on a journey ═════════════

/**
 * Beats: fourtotwo (the four-rakʿa prayers fold to two; dawn and sunset keep theirs: «وَهِيَ الظُّهْرُ وَالْعَصْرُ وَالْعِشَاءُ», «وَلَا تُقْصَرُ الْفَجْرُ
 * وَلَا الْمَغْرِبُ إِجْمَاعًا»), takerukhsa (on the road the shortened prayer lights as the better way: «أَوْلَى وَأَفْضَلُ مِنْ تَرْكِهَا»), fourdaysrule (a
 * stay passes four days and the arches open back to four; behind a resident imam they are four too: «أَكْثَرَ مِنْ أَرْبَعَةِ أَيَّامٍ أَتَمَّ عِنْدَ
 * مُؤَلِّفِي الْكِتَابِ», «وَمَنْ صَلَّى خَلْفَ مُقِيمٍ أَتَمَّ»), charitygift (a gift opens with light: «فَهُوَ صَدَقَةٌ تَصَدَّقَ اللَّهُ بِهَا عَلَى
 * عِبَادِهِ»), homemissed (a prayer missed at home is carried on the road whole: «صَلَّاهَا تَامَّةً»).
 */
internal fun DrawScope.shortenedprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fourtotwo" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "takerukhsa" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "fourdaysrule" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "charitygift" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "homemissed" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { shortenedPrayerDraw(a) }
}

private fun DrawScope.shortenedPrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    nightSky(t, SalC.Top, Color(0xFF6A6A9A), ground, starAlpha = 0.5f)
    drawRect(Color(0xFF3A3026), Offset(0f, ground), Size(w, h - ground))
    val ft = fqIn(a, "fourtotwo", "takerukhsa")
    val tr = fqIn(a, "takerukhsa", "fourdaysrule")
    val fd = fqIn(a, "fourdaysrule", "charitygift")
    val cg = fqIn(a, "charitygift", "homemissed")
    val hm = fqIn(a, "homemissed")
    val group = { cx: Float, y: Float, n: Int, shown: Float, alpha: Float, s: Float ->
        for (j in 0 until n) {
            val keep = j < 2 || shown > 0.5f
            rakah74(Offset(cx + s * 0.35f * (n - 1) / 2f - s * 0.35f * j, y), s, 1f, alpha * (if (keep) 1f else 0f))
        }
    }
    if (ft > 0f) {
        val p = a.b("fourtotwo")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.6f * ft)
        val fold = 1f - seg(p, 0.25f, 0.45f)
        val counts = listOf(2, 4, 4, 3, 4)
        val xs = listOf(0.88f, 0.68f, 0.5f, 0.32f, 0.13f)
        for (k in 0 until 5) {
            val four = counts[k] == 4
            group(w * xs[k], h * 0.55f, counts[k], if (four) fold else 1f, ft, h * 0.14f)
            if (four) softGlow(Offset(w * xs[k], h * 0.5f), w * 0.05f, SalC.Gold, 0.4f * ft * (1f - fold))
        }
        tagAt(a.tag("fourtotwo", 0, 0), Offset(w * 0.5f, h * 0.2f), ft * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("fourtotwo", 0, 1), Offset(w * 0.5f, h * 0.82f), ft * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tr > 0f) {
        val p = a.b("takerukhsa")
        fqRoad(Offset(w * 1.05f, ground + h * 0.08f), Offset(-w * 0.05f, ground + h * 0.08f), h * 0.06f, Color(0xFF5A4E40), tr)
        camel26(Offset(mix(w * 0.85f, w * 0.6f, seg(p, 0f, 1f)), ground + h * 0.06f), h * 0.3f, Color(0xFFC8A880), tr, t, walk = t * 4f)
        for (j in 0 until 2) rakah74(Offset(w * (0.3f - 0.06f * j), h * 0.55f), h * 0.18f, seg(p, 0.3f, 0.5f), tr)
        softGlow(Offset(w * 0.27f, h * 0.48f), w * 0.1f, SalC.Gold, 0.5f * tr * seg(p, 0.35f, 0.55f))
        tagAt(a.tag("takerukhsa"), Offset(w * 0.3f, h * 0.2f), tr * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fd > 0f) {
        val p = a.b("fourdaysrule")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.6f * fd)
        val cell = w * 0.07f
        val days = (seg(p, 0.05f, 0.4f) * 5f).toInt()
        for (d in 0 until 5) {
            val c = Offset(w * (0.85f - 0.1f * d), h * 0.25f)
            drawRoundRect(if (d < days) (if (d < 4) SalC.Gold else Color(0xFFE0603A)) else SalC.Cell, Offset(c.x - cell / 2f, c.y - cell / 2f), Size(cell, cell), androidx.compose.ui.geometry.CornerRadius(cell * 0.2f), alpha = fd)
        }
        val open = seg(p, 0.4f, 0.55f)
        for (j in 0 until 4) rakah74(Offset(w * (0.72f - 0.07f * j), h * 0.62f), h * 0.16f, 1f, fd * (if (j < 2) 1f else open))
        tagAt(a.tag("fourdaysrule", 0, 0), Offset(w * 0.6f, h * 0.84f), fd * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(p, 0.65f, 0.8f)
        pray12(Offset(w * 0.2f, h * 0.7f), h * 0.22f, SalC.Person, fd * g)
        pray12(Offset(w * 0.32f, h * 0.74f), h * 0.2f, Color(0xFFB8B0A0), fd * g)
        tagAt(a.tag("fourdaysrule", 0, 1), Offset(w * 0.25f, h * 0.4f), fd * g, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (cg > 0f) {
        val p = a.b("charitygift")
        val c = Offset(w * 0.5f, h * 0.6f)
        val open = seg(p, 0.2f, 0.45f)
        drawRect(Color(0xFF6A3A5A), Offset(c.x - w * 0.1f, c.y - h * 0.12f), Size(w * 0.2f, h * 0.18f), alpha = cg)
        drawRect(SalC.Gold, Offset(c.x - w * 0.012f, c.y - h * 0.12f), Size(w * 0.024f, h * 0.18f), alpha = cg)
        drawRect(Color(0xFF7A4A6A), Offset(c.x - w * 0.11f, c.y - h * 0.16f - h * 0.08f * open), Size(w * 0.22f, h * 0.04f), alpha = cg)
        softGlow(c + Offset(0f, -h * 0.15f), w * 0.15f, SalC.Gold, cg * open)
        for (k in 0 until 6) {
            val u = fract(t * 0.5f + k / 6f)
            drawCircle(SalC.Gold, w * 0.007f, c + Offset(sin(k * 2f + u * 4f) * w * 0.08f, -h * 0.15f - u * h * 0.3f), alpha = cg * open * (1f - u))
        }
        tagAt(a.tag("charitygift"), Offset(w * 0.5f, h * 0.88f), cg * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (hm > 0f) {
        val p = a.b("homemissed")
        house12(Offset(w * 0.82f, ground), w * 0.16f, Color(0xFFCDBFA6), Color(0xFF6A5A48), hm, lit = 0.6f)
        val u = easeInOut(seg(p, 0.15f, 0.7f))
        for (j in 0 until 4) rakah74(Offset(mix(w * 0.82f, w * 0.35f, u) + w * (0.1f - 0.065f * j), h * 0.5f), h * 0.14f, 1f, hm)
        fqRoad(Offset(w * 0.75f, ground + h * 0.08f), Offset(-w * 0.05f, ground + h * 0.08f), h * 0.05f, Color(0xFF5A4E40), hm)
        tagAt(a.tag("homemissed"), Offset(w * 0.35f, h * 0.24f), hm * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 26. sal-jam: joining two prayers ═════════════

/**
 * Beats: twopairs (on the day's line, the noon and afternoon lights draw together, and the sunset and night ones: «بَيْنَ الظُّهْرِ وَالْعَصْرِ», «وَبَيْنَ
 * الْمَغْرِبِ وَالْعِشَاءِ»), movingorresting (a camel on the move and a tent pitched, each with a joined pair: «سَائِرًا أَمْ نَازِلًا»), rainmudwind (heavy rain,
 * mud and a cold wind each join the pair: «كَالْمَطَرِ الْغَزِيرِ الَّذِي يَبُلُّ الثِّيَابَ», «وَالْوَحْلِ», «وَالرِّيحِ الشَّدِيدَةِ الْبَارِدَةِ»),
 * delaytoasr (setting out before noon, the noon light moves later to the afternoon's: «أَخَّرَ الظُّهْرَ حَتَّى يَجْمَعَهَا إِلَى الْعَصْرِ»),
 * advancewithzuhr (setting out after noon, the afternoon light comes early to the noon's: «صَلَّى الظُّهْرَ وَالْعَصْرَ جَمِيعًا ثُمَّ سَارَ»), nofajrjoin (the
 * dawn light stays alone, and the afternoon and sunset are never joined: «وَلَا تُجْمَعُ الْفَجْرُ إِلَى غَيْرِهَا», «وَلَا الْعَصْرُ إِلَى الْمَغْرِبِ»).
 */
internal fun DrawScope.joiningprayersScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twopairs" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "movingorresting" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "rainmudwind" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "delaytoasr" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "advancewithzuhr" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "nofajrjoin" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { joiningPrayersDraw(a) }
}

private fun DrawScope.joiningPrayersDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    nightSky(t, SalC.Top, SalC.Low, h, starAlpha = 0.5f)
    val tp = fqIn(a, "twopairs", "movingorresting")
    val mr = fqIn(a, "movingorresting", "rainmudwind")
    val rm = fqIn(a, "rainmudwind", "delaytoasr")
    val da = fqIn(a, "delaytoasr", "advancewithzuhr")
    val aw = fqIn(a, "advancewithzuhr", "nofajrjoin")
    val nf = fqIn(a, "nofajrjoin")
    val y = h * 0.5f
    val dots = prayerDots84(w, y)
    val line = { alpha: Float -> drawLine(SalC.Rim, Offset(w * 0.95f, y), Offset(w * 0.05f, y), 2f * dp, alpha = alpha * 0.6f) }
    if (tp > 0f) {
        val p = a.b("twopairs")
        line(tp)
        val j1 = easeInOut(seg(p, 0.1f, 0.4f))
        val j2 = easeInOut(seg(p, 0.5f, 0.8f))
        for (k in 0 until 5) {
            val c = when (k) {
                1 -> dots[1] + (dots[2] - dots[1]) * (0.35f * j1)
                2 -> dots[2] + (dots[1] - dots[2]) * (0.35f * j1)
                3 -> dots[3] + (dots[4] - dots[3]) * (0.35f * j2)
                4 -> dots[4] + (dots[3] - dots[4]) * (0.35f * j2)
                else -> dots[k]
            }
            drawCircle(SalC.Gold, w * 0.025f, c, alpha = tp)
        }
        tagAt(a.tag("twopairs", 0, 0), Offset(w * 0.58f, h * 0.3f), tp * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("twopairs", 0, 1), Offset(w * 0.22f, h * 0.7f), tp * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val pairAt = { c: Offset, alpha: Float ->
        drawCircle(SalC.Gold, w * 0.018f, c + Offset(w * 0.02f, 0f), alpha = alpha)
        drawCircle(SalC.Gold, w * 0.018f, c - Offset(w * 0.02f, 0f), alpha = alpha)
    }
    if (mr > 0f) {
        val p = a.b("movingorresting")
        val ground = h * 0.82f
        drawRect(Color(0xFF3A3026), Offset(0f, ground), Size(w, h - ground), alpha = mr)
        val g0 = seg(p, 0.05f, 0.3f)
        camel26(Offset(w * 0.72f, ground), h * 0.32f, Color(0xFFC8A880), mr * g0, t, walk = t * 4f)
        pairAt(Offset(w * 0.72f, h * 0.3f), mr * g0)
        val g1 = seg(p, 0.45f, 0.65f)
        fqTent(Offset(w * 0.28f, ground), w * 0.2f, Color(0xFFE8E2D4), mr * g1)
        pairAt(Offset(w * 0.28f, h * 0.3f), mr * g1)
        tagAt(a.tag("movingorresting"), Offset(w * 0.5f, h * 0.15f), mr * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (rm > 0f) {
        val p = a.b("rainmudwind")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.5f)
            when (k) {
                0 -> { cloud38(c + Offset(0f, -h * 0.15f), w * 0.05f, Color(0xFFB8C4D8), rm * g); rain12(c.x - w * 0.08f, c.x + w * 0.08f, c.y - h * 0.1f, c.y + h * 0.15f, t, Fq.Water, rm * g, 18) }
                1 -> for (j in 0 until 3) drawOval(Color(0xFF5A4630), Offset(c.x - w * 0.08f + j * w * 0.05f, c.y + h * 0.08f - (j % 2) * h * 0.03f), Size(w * 0.06f, h * 0.03f), alpha = rm * g)
                else -> for (j in 0 until 4) drawArc(Color(0xFFCFE0F0), 200f, 120f, false, Offset(c.x - w * 0.08f + j * w * 0.01f, c.y - h * 0.08f + j * h * 0.05f), Size(w * 0.16f, h * 0.08f), alpha = rm * g, style = Stroke(2f * dp))
            }
            pairAt(c + Offset(0f, -h * 0.28f), rm * g)
            tagAt(a.tag("rainmudwind", 0, k), c + Offset(0f, h * 0.28f), rm * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (da > 0f) {
        val p = a.b("delaytoasr")
        line(da)
        for (k in listOf(0, 3, 4)) drawCircle(SalC.Gold, w * 0.02f, dots[k], alpha = da * 0.5f)
        val u = easeInOut(seg(p, 0.2f, 0.6f))
        drawCircle(SalC.Gold, w * 0.025f, dots[1] + (dots[2] - dots[1]) * (u * 0.8f), alpha = da)
        drawCircle(SalC.Gold, w * 0.025f, dots[2], alpha = da)
        camel26(Offset(w * 0.8f, h * 0.85f), h * 0.18f, Color(0xFFC8A880), da, t, walk = t * 4f)
        sun12(Offset(w * 0.75f, h * 0.25f), w * 0.025f, da)
        tagAt(a.tag("delaytoasr"), Offset(w * 0.5f, h * 0.7f), da * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (aw > 0f) {
        val p = a.b("advancewithzuhr")
        line(aw)
        for (k in listOf(0, 3, 4)) drawCircle(SalC.Gold, w * 0.02f, dots[k], alpha = aw * 0.5f)
        val u = easeInOut(seg(p, 0.2f, 0.6f))
        drawCircle(SalC.Gold, w * 0.025f, dots[2] + (dots[1] - dots[2]) * (u * 0.8f), alpha = aw)
        drawCircle(SalC.Gold, w * 0.025f, dots[1], alpha = aw)
        camel26(Offset(mix(w * 0.6f, w * 0.3f, seg(p, 0.6f, 1f)), h * 0.85f), h * 0.18f, Color(0xFFC8A880), aw, t, walk = t * 4f)
        sun12(Offset(w * 0.6f, h * 0.22f), w * 0.025f, aw)
        tagAt(a.tag("advancewithzuhr"), Offset(w * 0.5f, h * 0.7f), aw * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nf > 0f) {
        val p = a.b("nofajrjoin")
        line(nf)
        for (k in 0 until 5) drawCircle(SalC.Gold, w * 0.025f, dots[k], alpha = nf)
        val g0 = seg(p, 0.05f, 0.3f)
        drawLine(Color(0xFFC0503A), dots[0] + Offset(-w * 0.03f, 0f), dots[0] + Offset(-w * 0.15f, 0f), 2f * dp, alpha = nf * g0, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        cross14(dots[0] + Offset(-w * 0.09f, -h * 0.06f), w * 0.025f, seg(p, 0.2f, 0.35f))
        tagAt(a.tag("nofajrjoin", 0, 0), Offset(w * 0.78f, h * 0.3f), nf * g0, back = Fq.Back, ink = ArtInk.Ivory)
        val g1 = seg(p, 0.5f, 0.7f)
        drawLine(Color(0xFFC0503A), dots[2], dots[3], 2f * dp, alpha = nf * g1, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        cross14((dots[2] + dots[3]) / 2f + Offset(0f, -h * 0.06f), w * 0.025f, seg(p, 0.6f, 0.75f))
        tagAt(a.tag("nofajrjoin", 0, 1), Offset(w * 0.4f, h * 0.7f), nf * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 27. sal-jumua: the Friday prayer ═════════════

/**
 * Beats: fardayn (men gather to the Friday mosque from every side: «فَرْضُ عَيْنٍ»), notobligedbutvalid (a woman, a child, one ill and a traveller come
 * too; each is ticked: «فَإِنْ حَضَرُوهَا صَحَّتْ مِنْهُمْ وَأَجْزَأَتْهُمْ عَنِ الظُّهْرِ»), catchonerakah (one who catches a rakʿa with the imam completes the
 * Friday; one who does not prays four: «بِإِدْرَاكِ رَكْعَةٍ مَعَ الْإِمَامِ», «وَإِلَّا صَلَّاهَا ظُهْرًا»), twokhutbas (on the minbar the khatib stands, sits
 * a moment, stands again: «خُطْبَتَيْنِ قَائِمًا», «وَيُسَنُّ قَصْرُهُمَا»), nostepping (one who steps over the seated rows is struck out: «وَيَحْرُمُ تَخَطِّي
 * رِقَابِ النَّاسِ»).
 */
internal fun DrawScope.fridayprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fardayn" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "notobligedbutvalid" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "catchonerakah" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "twokhutbas" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "nostepping" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { fridayPrayerDraw(a) }
}

private fun DrawScope.fridayPrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val fa = fqIn(a, "fardayn", "notobligedbutvalid")
    val nv = fqIn(a, "notobligedbutvalid", "catchonerakah")
    val cr = fqIn(a, "catchonerakah", "twokhutbas")
    val tk = fqIn(a, "twokhutbas", "nostepping")
    val ns = fqIn(a, "nostepping")
    val ground = h * 0.84f
    if (fa > 0f || nv > 0f) {
        daySky12(SalC.DayTop, SalC.DayLow, ground, SalC.Sand)
        mosque12(Offset(w * 0.18f, ground), w * 0.32f, SalC.Mosque, SalC.Dome, 1f, 0.5f)
    } else hall74(h * 0.62f, t)
    if (fa > 0f) {
        val p = a.b("fardayn")
        for (k in 0 until 8) {
            val u = easeInOut(seg(p, 0.03f * k, 0.5f + 0.03f * k))
            val from = Offset(w * (1.05f + 0.06f * k), ground)
            figure(from + (Offset(w * 0.35f + w * 0.02f * k, ground) - from) * u, h * 0.24f, Color(0xFF5A4E44), fa * (1f - seg(u, 0.9f, 1f) * 0.7f), walk = t * 6f)
        }
        tagAt(a.tag("fardayn"), Offset(w * 0.6f, h * 0.15f), fa * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nv > 0f) {
        val p = a.b("notobligedbutvalid")
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k)
            val foot = Offset(w * (0.42f + 0.14f * k), ground)
            val col = Color(0xFF5A4E44)
            when (k) {
                0 -> woman12(foot, h * 0.26f, col, nv * g)
                1 -> child12(foot, h * 0.3f, col, nv * g)
                2 -> { figure(foot, h * 0.26f, col, nv * g, lean = 0.15f); drawLine(col, foot + Offset(-h * 0.06f, 0f), foot + Offset(-h * 0.05f, -h * 0.16f), 2.5f * density, StrokeCap.Round, alpha = nv * g) }
                else -> { figure(foot, h * 0.26f, col, nv * g); fqSack(foot + Offset(h * 0.05f, -h * 0.1f), h * 0.07f, Fq.SandDark, nv * g) }
            }
            tick14(foot + Offset(0f, -h * 0.36f), w * 0.025f, seg(p, 0.6f + 0.05f * k, 0.7f + 0.05f * k))
        }
        tagAt(a.tag("notobligedbutvalid"), Offset(w * 0.6f, h * 0.12f), nv * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (cr > 0f) {
        val p = a.b("catchonerakah")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.7f * cr)
        val g0 = seg(p, 0.05f, 0.3f)
        for (j in 0 until 2) rakah74(Offset(w * (0.78f - 0.08f * j), h * 0.5f), h * 0.18f, if (j == 1) 1f else 0.3f, cr * g0)
        tick14(Offset(w * 0.74f, h * 0.25f), w * 0.03f, seg(p, 0.25f, 0.4f))
        tagAt(a.tag("catchonerakah", 0, 0), Offset(w * 0.74f, h * 0.76f), cr * g0, back = Fq.Back, ink = ArtInk.Gold)
        val g1 = seg(p, 0.5f, 0.7f)
        for (j in 0 until 4) rakah74(Offset(w * (0.38f - 0.07f * j), h * 0.5f), h * 0.18f, 1f, cr * g1)
        tagAt(a.tag("catchonerakah", 0, 1), Offset(w * 0.28f, h * 0.76f), cr * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tk > 0f) {
        val p = a.b("twokhutbas")
        val floor = h * 0.62f
        val foot = Offset(w * 0.12f, floor + h * 0.15f)
        minbar84(foot, h * 0.5f, tk)
        val top = foot + Offset(h * 0.5f * 0.38f, -h * 0.45f)
        val sitting = seg(p, 0.4f, 0.45f) * (1f - seg(p, 0.55f, 0.6f))
        if (sitting > 0.5f) sit12(top + Offset(0f, h * 0.02f), h * 0.2f, SalC.Person, tk, 1f) else figure(top, h * 0.24f, SalC.Person, tk, arms = 0.3f)
        voice12(top + Offset(h * 0.1f, -h * 0.2f), w * 0.06f, t, SalC.Gold, tk * (1f - sitting) * seg(p, 0.05f, 0.15f), 1f)
        for (k in 0 until 5) sit12(Offset(w * (0.45f + 0.1f * k), floor + h * 0.32f), h * 0.2f, SalC.Person, tk, -1f)
        tagAt(a.tag("twokhutbas", 0, 0), Offset(w * 0.6f, h * 0.12f), tk * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("twokhutbas", 0, 1), Offset(w * 0.6f, h * 0.26f), tk * seg(p, 0.65f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ns > 0f) {
        val p = a.b("nostepping")
        val floor = h * 0.62f
        for (r in 0 until 2) for (k in 0 until 6) sit12(Offset(w * (0.2f + 0.12f * k), floor + h * (0.18f + 0.15f * r)), h * 0.18f, SalC.Person, ns, -1f)
        val u = seg(p, 0.1f, 0.6f)
        val x = mix(w * 0.9f, w * 0.45f, u)
        figure(Offset(x, floor + h * 0.28f - h * 0.06f * kotlin.math.abs(sin(u * PI.toFloat() * 4f))), h * 0.24f, Color(0xFFC8A890), ns, walk = t * 8f)
        cross14(Offset(x, floor - h * 0.06f), w * 0.035f, seg(p, 0.4f, 0.55f))
        tagAt(a.tag("nostepping"), Offset(w * 0.5f, h * 0.12f), ns * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Ember)
    }
}

// ═════════════ 28. sal-jumua-adab: the manners of Friday ═════════════

/**
 * Beats: earlyfriday (early in the day one walks to the mosque and sits reading until the khatib comes out: «التَّبْكِيرُ إِلَى الْمَسْجِدِ», «حَتَّى يَخْرُجَ
 * الْإِمَامُ»), silentlisten (the rows listen in silence; one who whispers even «be quiet» is struck out: «وَيَجِبُ الْإِنْصَاتُ لِلْخُطْبَةِ», «فَقَدْ لَغَا»),
 * tworakatenter (one who comes in during the khutba prays two light rakʿas, then sits: «حَتَّى يُصَلِّيَ رَكْعَتَيْنِ يَتَجَوَّزُ فِيهِمَا»), kahfread (an open
 * book with the arch of a cave glowing over it: «قِرَاءَةُ سُورَةِ الْكَهْفِ»), noreserve (a rug left in the front row by one who comes late is struck out,
 * and the place goes to the one who came first: «عَمَلٌ غَيْرُ سَائِغٍ», «لِأَنَّ الصَّفَّ الْأَوَّلَ لِلسَّابِقِ بِنَفْسِهِ»).
 */
internal fun DrawScope.fridaymannersScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "earlyfriday" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "silentlisten" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
        "tworakatenter" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
        "kahfread" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "noreserve" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { fridayMannersDraw(a) }
}

private fun DrawScope.fridayMannersDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val ef = fqIn(a, "earlyfriday", "silentlisten")
    val sl = fqIn(a, "silentlisten", "tworakatenter")
    val tr = fqIn(a, "tworakatenter", "kahfread")
    val kr = fqIn(a, "kahfread", "noreserve")
    val nr = fqIn(a, "noreserve")
    if (ef > 0f) {
        val p = a.b("earlyfriday")
        drawRect(Color(0xFF8AB8DA), Offset(w * 0.08f, h * 0.24f), Size(w * 0.09f, h * 0.16f), alpha = ef)
        sun12(Offset(w * 0.125f, h * 0.36f - h * 0.06f * seg(p, 0f, 0.5f)), w * 0.015f, ef)
        val u = easeInOut(seg(p, 0.05f, 0.45f))
        if (u < 1f) figure(Offset(mix(w * 0.95f, w * 0.45f, u), floor + h * 0.3f), h * 0.28f, SalC.Person, ef, walk = t * 6f)
        else {
            sit12(Offset(w * 0.45f, floor + h * 0.3f), h * 0.26f, SalC.Person, ef, -1f)
            openBook21(Offset(w * 0.38f, floor + h * 0.24f), w * 0.07f, Color(0xFFF2E8CE), Color(0xFFB8A888), ef)
        }
        tagAt(a.tag("earlyfriday", 0, 0), Offset(w * 0.6f, h * 0.12f), ef * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        minbar84(Offset(w * 0.12f, floor + h * 0.12f), h * 0.36f, ef * seg(p, 0.6f, 0.75f))
        tagAt(a.tag("earlyfriday", 0, 1), Offset(w * 0.6f, h * 0.26f), ef * seg(p, 0.65f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (sl > 0f) {
        val p = a.b("silentlisten")
        minbar84(Offset(w * 0.08f, floor + h * 0.12f), h * 0.36f, sl)
        voice12(Offset(w * 0.3f, floor - h * 0.12f), w * 0.06f, t, SalC.Gold, sl, 1f)
        for (k in 0 until 5) {
            val seat = Offset(w * (0.42f + 0.11f * k), floor + h * 0.32f)
            sit12(seat, h * 0.22f, SalC.Person, sl, -1f)
            ear73(seat + Offset(0f, -h * 0.3f), w * 0.025f, SalC.Gold, sl * seg(p, 0.1f, 0.3f))
        }
        tagAt(a.tag("silentlisten", 0, 0), Offset(w * 0.6f, h * 0.12f), sl * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(p, 0.5f, 0.65f)
        voice12(Offset(w * 0.73f, floor + h * 0.06f), w * 0.025f, t * 2f, Color(0xFFC0503A), sl * g, 1f)
        cross14(Offset(w * 0.75f, floor - h * 0.04f), w * 0.03f, seg(p, 0.6f, 0.75f))
        tagAt(a.tag("silentlisten", 0, 1), Offset(w * 0.75f, h * 0.28f), sl * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (tr > 0f) {
        val p = a.b("tworakatenter")
        minbar84(Offset(w * 0.08f, floor + h * 0.12f), h * 0.36f, tr * 0.7f)
        val foot = Offset(w * 0.62f, floor + h * 0.3f)
        val st = seg(p, 0f, 1f)
        when {
            st < 0.2f -> figure(Offset(mix(w * 0.95f, foot.x, st / 0.2f), foot.y), h * 0.3f, SalC.Person, tr, walk = t * 6f)
            st < 0.75f -> { val ph = fract((st - 0.2f) / 0.275f); if (ph < 0.5f) pray12(foot, h * 0.3f, SalC.Person, tr) else bow12(foot, h * 0.3f, SalC.Person, tr, if (ph < 0.75f) 0.5f else 1f) }
            else -> sit12(foot, h * 0.26f, SalC.Person, tr, -1f)
        }
        for (j in 0 until 2) rakah74(Offset(w * (0.82f - 0.05f * j), h * 0.25f), h * 0.1f, seg(st, 0.2f + 0.27f * j, 0.45f + 0.27f * j), tr)
        tagAt(a.tag("tworakatenter"), Offset(w * 0.45f, h * 0.12f), tr * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (kr > 0f) {
        val p = a.b("kahfread")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.75f * kr)
        val c = Offset(w * 0.5f, h * 0.65f)
        openBook21(c, w * 0.34f, Color(0xFFF2E8CE), Color(0xFFB8A888), kr, glow = seg(p, 0.2f, 0.5f))
        val cave = Path().apply {
            moveTo(c.x - w * 0.2f, c.y - h * 0.12f); quadraticTo(c.x - w * 0.18f, c.y - h * 0.5f, c.x, c.y - h * 0.48f)
            quadraticTo(c.x + w * 0.18f, c.y - h * 0.5f, c.x + w * 0.2f, c.y - h * 0.12f)
        }
        drawPath(cave, Fq.Stone, alpha = kr * seg(p, 0.15f, 0.35f), style = Stroke(w * 0.03f, cap = StrokeCap.Round))
        softGlow(Offset(c.x, c.y - h * 0.28f), w * 0.1f, SalC.Gold, kr * seg(p, 0.35f, 0.55f))
        tagAt(a.tag("kahfread"), Offset(w * 0.5f, h * 0.1f), kr * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nr > 0f) {
        val p = a.b("noreserve")
        val front = Offset(w * 0.3f, floor + h * 0.2f)
        fqRug(front, w * 0.08f, h * 0.06f, Color(0xFF6A2A34), SalC.Gold, nr * (1f - seg(p, 0.55f, 0.7f)))
        cross14(front + Offset(0f, -h * 0.1f), w * 0.03f, seg(p, 0.2f, 0.35f) * (1f - seg(p, 0.55f, 0.7f)))
        tagAt(a.tag("noreserve", 0, 0), Offset(w * 0.3f, h * 0.12f), nr * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Ember)
        val come = easeInOut(seg(p, 0.55f, 0.8f))
        if (come > 0f) {
            if (come < 1f) figure(Offset(mix(w * 0.7f, front.x, come), front.y + h * 0.04f), h * 0.24f, SalC.Person, nr, walk = t * 6f)
            else sit12(front + Offset(0f, h * 0.04f), h * 0.22f, SalC.Person, nr, -1f)
        }
        tagAt(a.tag("noreserve", 0, 1), Offset(w * 0.6f, h * 0.26f), nr * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 29. sal-eid: the prayer of the two eids ═════════════

/**
 * Beats: eidkifaya (an open prayer ground under the eid crescent, full of rows: «فَرْضُ كِفَايَةٍ عِنْدَ مُؤَلِّفِي الْكِتَابِ», «مِنْ شَعَائِرِ الْإِسْلَامِ
 * الظَّاهِرَةِ»), eidtime (the sun a spear's height up, and the prayer ground outside the town: «بَعْدَ ارْتِفَاعِ الشَّمْسِ قَدْرَ رُمْحٍ إِلَى الزَّوَالِ»,
 * «فِي مُصَلًّى بَارِزٍ خَارِجَ الْبَلَدِ»), sevenfive (seven lights over the first rakʿa, five over the second: «فِي الْأُولَى سَبْعًا», «وَفِي الثَّانِيَةِ
 * خَمْسًا»), datesbefore (dates eaten before going out on the eid of fitr; on the eid of sacrifice the plate waits: «تَمَرَاتٍ», «وَأَلَّا يَطْعَمَ يَوْمَ النَّحْرِ
 * حَتَّى يُصَلِّيَ»), tworoads (one road there and another back: «وَيُخَالِفُ الطَّرِيقَ»).
 */
internal fun DrawScope.eidprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "eidkifaya" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "eidtime" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "sevenfive" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "datesbefore" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "otherwayback" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { eidPrayerDraw(a) }
}

private fun DrawScope.eidPrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    daySky12(SalC.DayTop, SalC.DayLow, ground, SalC.Sand)
    val ek = fqIn(a, "eidkifaya", "eidtime")
    val et = fqIn(a, "eidtime", "sevenfive")
    val sf = fqIn(a, "sevenfive", "datesbefore")
    val db = fqIn(a, "datesbefore", "otherwayback")
    val tr = fqIn(a, "otherwayback")
    val ink = Color(0xFF5A4E44)
    if (ek > 0f) {
        val p = a.b("eidkifaya")
        fqEidMoon(Offset(w * 0.85f, h * 0.15f), w * 0.03f, SalC.DayTop, ek)
        for (r in 0 until 3) row74(ground - h * 0.02f + h * 0.06f * r, w * 0.1f, w * 0.9f, 9, h * (0.16f + 0.02f * r), ink, ek * seg(p, 0.1f + 0.15f * r, 0.3f + 0.15f * r))
        tagAt(a.tag("eidkifaya", 0, 0), Offset(w * 0.4f, h * 0.15f), ek * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("eidkifaya", 0, 1), Offset(w * 0.4f, h * 0.3f), ek * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (et > 0f) {
        val p = a.b("eidtime")
        val ang = PI.toFloat() * (1f - mix(0.05f, 0.12f, seg(p, 0f, 0.3f)))
        val sun = Offset(w * 0.5f + cos(ang) * w * 0.42f, ground - sin(ang) * h * 0.62f)
        sun12(sun, w * 0.035f, et)
        drawLine(Fq.SandDark, Offset(sun.x, ground), Offset(sun.x, sun.y + w * 0.04f), 2f * dp, alpha = et * seg(p, 0.2f, 0.35f))
        tagAt(a.tag("eidtime", 0, 0), Offset(w * 0.5f, h * 0.12f), et * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(p, 0.5f, 0.7f)
        for (k in 0 until 4) house12(Offset(w * (0.72f + 0.08f * k), ground), w * 0.08f, Color(0xFFCDBFA6), Color(0xFF6A5A48), et * g)
        drawRect(Color(0xFFE8D8B0), Offset(w * 0.15f, ground - h * 0.01f), Size(w * 0.4f, h * 0.02f), alpha = et * g)
        row74(ground, w * 0.2f, w * 0.5f, 5, h * 0.16f, ink, et * g)
        tagAt(a.tag("eidtime", 0, 1), Offset(w * 0.35f, h * 0.32f), et * g, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (sf > 0f) {
        val p = a.b("sevenfive")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.7f * sf)
        for (r in 0 until 2) {
            val n = if (r == 0) 7 else 5
            val cx = w * (0.7f - 0.4f * r)
            rakah74(Offset(cx, h * 0.75f), h * 0.22f, 1f, sf)
            for (k in 0 until n) {
                val g = seg(p, (if (r == 0) 0.05f else 0.5f) + 0.05f * k, (if (r == 0) 0.1f else 0.55f) + 0.05f * k)
                val ang = PI.toFloat() * (0.15f + 0.7f * k / (n - 1))
                drawCircle(SalC.Gold, w * 0.012f, Offset(cx - cos(ang) * w * 0.12f, h * 0.55f - sin(ang) * h * 0.16f), alpha = sf * g)
            }
            tagAt(a.tag("sevenfive", 0, r), Offset(cx, h * 0.18f), sf * seg(p, if (r == 0) 0.2f else 0.65f, if (r == 0) 0.4f else 0.85f), back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    if (db > 0f) {
        val p = a.b("datesbefore")
        val g0 = seg(p, 0.05f, 0.25f)
        fqEidMoon(Offset(w * 0.72f, h * 0.2f), w * 0.025f, SalC.DayTop, db * g0)
        dateBowl72(Offset(w * 0.72f, ground), w * 0.18f, db * g0)
        tick14(Offset(w * 0.85f, ground - h * 0.18f), w * 0.03f, seg(p, 0.2f, 0.35f))
        tagAt(a.tag("datesbefore", 0, 0), Offset(w * 0.72f, h * 0.36f), db * g0, back = Fq.Back, ink = ArtInk.Gold)
        val g1 = seg(p, 0.5f, 0.7f)
        fqRam(Offset(w * 0.25f, ground - h * 0.08f), h * 0.18f, Color(0xFFF0EAE0), db * g1)
        drawOval(Color(0xFFDCD8D0), Offset(w * 0.38f, ground - h * 0.03f), Size(w * 0.12f, h * 0.04f), alpha = db * g1)
        clock22(Offset(w * 0.44f, ground - h * 0.18f), w * 0.03f, SalC.Gold, 0.3f, db * g1)
        tagAt(a.tag("datesbefore", 0, 1), Offset(w * 0.3f, h * 0.36f), db * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tr > 0f) {
        val p = a.b("otherwayback")
        val home = Offset(w * 0.85f, ground)
        val musalla = Offset(w * 0.15f, ground)
        house12(home, w * 0.12f, Color(0xFFCDBFA6), Color(0xFF6A5A48), tr)
        drawRect(Color(0xFFE8D8B0), Offset(musalla.x - w * 0.1f, ground - h * 0.01f), Size(w * 0.2f, h * 0.02f), alpha = tr)
        val go = Path().apply { moveTo(home.x, home.y - h * 0.02f); quadraticTo(w * 0.5f, h * 0.45f, musalla.x, musalla.y - h * 0.02f) }
        val back = Path().apply { moveTo(musalla.x, musalla.y + h * 0.05f); quadraticTo(w * 0.5f, h * 0.98f, home.x, home.y + h * 0.05f) }
        drawPath(go, SalC.Gold, alpha = tr * seg(p, 0.05f, 0.3f), style = Stroke(3f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp))))
        drawPath(back, ArtInk.Jade, alpha = tr * seg(p, 0.45f, 0.7f), style = Stroke(3f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp))))
        tagAt(a.tag("otherwayback"), Offset(w * 0.5f, h * 0.15f), tr * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
