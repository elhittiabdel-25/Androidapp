package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 83 (2026-11-03): v1.3 fiqh, prayer 4: the forenoon prayer, the prostration of recitation, the times when prayer is forbidden,
 * the congregation, who leads it, and the prayer of the sick. Worshippers are faceless figures facing left; the sun's path tells the
 * times; ranks of who should lead are objects (a book, a scroll, a road, a crescent).
 */

/** One sitting on the ground who leans forward by [lean] (0 upright .. 1 deep), facing left. */
private fun DrawScope.seatedNod83(seat: Offset, tall: Float, color: Color, alpha: Float, lean: Float) {
    if (alpha <= 0.01f) return
    val hip = seat - Offset(0f, tall * 0.04f)
    val ang = 0.9f * lean
    val neck = hip + Offset(-sin(ang), -cos(ang)) * (tall * 0.33f)
    val knee = hip + Offset(-tall * 0.24f, -tall * 0.1f)
    val foot = Offset(knee.x - tall * 0.06f, seat.y)
    posed14(hip, neck, knee + Offset(tall * 0.02f, -tall * 0.02f), knee + Offset(tall * 0.05f, -tall * 0.01f), knee, foot, tall, color, alpha)
}

// ═════════════ 19. sal-duha: the forenoon prayer ═════════════

/**
 * Beats: duhanafl (the morning sun climbs and two arches light: «مِنَ التَّطَوُّعِ»), twotoeight (two arches, then eight: «وَأَقَلُّهَا رَكْعَتَانِ»,
 * «وَأَكْثَرُهَا عِنْدَ الْمُؤَلِّفِ ثَمَانِي رَكَعَاتٍ»), duhawindow (the stretch of the sun's path from well risen to the heat of the forenoon: «مِنِ ارْتِفَاعِ
 * الشَّمْسِ بَعْدَ طُلُوعِهَا», «إِذَا اشْتَدَّ الْحَرُّ»), threecounsels (three days marked in a month, two arches in the sun, one arch under the moon
 * before sleep: «صِيَامُ ثَلَاثَةِ أَيَّامٍ مِنْ كُلِّ شَهْرٍ», «وَرَكْعَتَيِ الضُّحَى», «وَأَنْ يُوتِرَ قَبْلَ أَنْ يَرْقُدَ»), hotsand (young camels kneel on
 * sand that shimmers with heat: «أَنْ تَحْمَى الرَّمْضَاءُ»).
 */
internal fun DrawScope.forenoonScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "duhanafl" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "twotoeight" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "duhawindow" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "threecounsels" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "hotsand" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { forenoonDraw(a) }
}

private fun DrawScope.forenoonDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    daySky12(SalC.DayTop, SalC.DayLow, ground, SalC.Sand)
    val dn = fqIn(a, "duhanafl", "twotoeight")
    val te = fqIn(a, "twotoeight", "duhawindow")
    val dw = fqIn(a, "duhawindow", "threecounsels")
    val tc = fqIn(a, "threecounsels", "hotsand")
    val hs = fqIn(a, "hotsand")
    val sunAt = { u: Float -> val ang = PI.toFloat() * (1f - u); Offset(w * 0.5f + cos(ang) * w * 0.42f, ground - sin(ang) * h * 0.62f) }
    if (dn > 0f) {
        val p = a.b("duhanafl")
        sun12(sunAt(mix(0.05f, 0.25f, easeOut(seg(p, 0f, 0.6f)))), w * 0.04f, dn)
        for (k in 0 until 2) rakah74(Offset(w * (0.42f + 0.16f * k), ground), h * 0.28f, seg(p, 0.4f + 0.1f * k, 0.55f + 0.1f * k), dn)
        tagAt(a.tag("duhanafl"), Offset(w * 0.5f, h * 0.12f), dn * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (te > 0f) {
        val p = a.b("twotoeight")
        val many = seg(p, 0.45f, 0.6f)
        for (k in 0 until 8) {
            val show = if (k < 2) 1f else many
            val x = w * 0.88f - (k / 2) * w * 0.2f - (k % 2) * w * 0.07f
            rakah74(Offset(x, ground), h * 0.24f, show, te * show)
        }
        tagAt(a.tag("twotoeight", 0, 0), Offset(w * 0.5f, h * 0.12f), te * seg(p, 0.05f, 0.25f) * (1f - many), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("twotoeight", 0, 1), Offset(w * 0.5f, h * 0.12f), te * many, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (dw > 0f) {
        val p = a.b("duhawindow")
        val c = Offset(w * 0.5f, ground)
        val r = w * 0.42f
        drawArc(Color(0xFF6A7A9A), 180f, 180f, false, Offset(c.x - r, c.y - h * 0.62f), Size(r * 2f, h * 1.24f), alpha = dw * 0.6f, style = Stroke(2f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp))))
        val g = seg(p, 0.1f, 0.4f)
        drawArc(SalC.Gold, 180f + 180f * 0.12f, 180f * 0.3f * g, false, Offset(c.x - r, c.y - h * 0.62f), Size(r * 2f, h * 1.24f), alpha = dw, style = Stroke(6f * dp, cap = StrokeCap.Round))
        sun12(sunAt(mix(0.12f, 0.42f, easeInOut(seg(p, 0.1f, 0.8f)))), w * 0.04f, dw)
        tagAt(a.tag("duhawindow", 0, 0), Offset(w * 0.24f, h * 0.2f), dw * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val heat = seg(p, 0.55f, 0.75f)
        for (k in 0 until 5) drawLine(Color(0xFFF2A050), Offset(w * (0.2f + 0.15f * k), ground - h * 0.02f), Offset(w * (0.22f + 0.15f * k), ground - h * 0.08f - h * 0.02f * sin(t * 4f + k)), 2f * dp, StrokeCap.Round, alpha = dw * heat)
        tagAt(a.tag("duhawindow", 0, 1), Offset(w * 0.6f, h * 0.32f), dw * heat, back = Fq.Back, ink = ArtInk.Ember)
    }
    if (tc > 0f) {
        val p = a.b("threecounsels")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.75f * tc)
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.5f)
            when (k) {
                0 -> fqCalendar(c + Offset(-w * 0.1f, -h * 0.12f), 6, 30, w * 0.028f, tc * g, SalC.Rim) { i -> if (i in 12..14) SalC.Gold else null }
                1 -> { sun12(c + Offset(0f, -h * 0.15f), w * 0.025f, tc * g); for (j in 0 until 2) rakah74(Offset(c.x + w * (0.03f - 0.06f * j), c.y + h * 0.12f), h * 0.18f, 1f, tc * g) }
                else -> { moon12(c + Offset(0f, -h * 0.15f), w * 0.022f, tc * g); rakah74(Offset(c.x, c.y + h * 0.12f), h * 0.18f, 1f, tc * g, true) }
            }
            tagAt(a.tag("threecounsels", 0, k), Offset(c.x, h * (0.82f + 0.0f)), tc * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (hs > 0f) {
        val p = a.b("hotsand")
        drawRect(Color(0xFFF2C888), Offset(0f, ground), Size(w, h - ground), alpha = hs * 0.5f)
        for (k in 0 until 3) camel26(Offset(w * (0.3f + 0.22f * k), ground + h * 0.12f), h * (0.2f - 0.02f * k), Color(0xFFC8A880), hs, t)
        for (k in 0 until 8) {
            val x = w * (0.08f + 0.12f * k)
            drawLine(Color(0xFFF2A050), Offset(x, ground + h * 0.02f), Offset(x + w * 0.01f, ground - h * 0.06f - h * 0.02f * sin(t * 5f + k)), 2f * dp, StrokeCap.Round, alpha = hs * seg(p, 0.1f, 0.3f))
        }
        sun12(sunAt(0.42f), w * 0.05f, hs)
        tagAt(a.tag("hotsand"), Offset(w * 0.5f, h * 0.12f), hs * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ember)
    }
}

// ═════════════ 20. sal-tilawa: the prostration of recitation ═════════════

/**
 * Beats: versebow (a marked verse in an open book glows and the reader goes down in prostration: «عِنْدَ تِلَاوَةِ آيَاتِ السُّجُودِ
 * وَاسْتِمَاعِهَا», «تَقَرُّبًا إِلَيْهِ وَخُضُوعًا لِعَظَمَتِهِ»), listenerkinds (the reader and the one who sits to listen both prostrate; one only
 * passing by walks on: «لِلْقَارِئِ وَالْمُسْتَمِعِ», «أَمَّا السَّامِعُ الَّذِي لَمْ يَقْصِدْهُ فَلَا يُشْرَعُ فِي حَقِّهِ»), bowsubhan (in prostration a
 * light rises: «سُبْحَانَ رَبِّيَ الْأَعْلَى»), sajadawajhi (an ear and an eye light by the one prostrating: «سَجَدَ وَجْهِي لِلَّذِي خَلَقَهُ وَصَوَّرَهُ»),
 * fromstanding (from standing to prostration along a gold path, from sitting along a plain one: «عَنْ قِيَامٍ أَفْضَلُ مِنَ الْإِتْيَانِ بِهِ عَنْ قُعُودٍ»).
 */
internal fun DrawScope.recitationbowScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "versebow" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "listenerkinds" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
        "bowsubhan" to Shot(Offset(w * 0.5f, h * 0.65f), 1.1f),
        "sajadawajhi" to Shot(Offset(w * 0.5f, h * 0.65f), 1.1f),
        "fromstanding" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { recitationBowDraw(a) }
}

private fun DrawScope.recitationBowDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t, lamps = 0.6f)
    val vb = fqIn(a, "versebow", "listenerkinds")
    val lk = fqIn(a, "listenerkinds", "bowsubhan")
    val bs = fqIn(a, "bowsubhan", "sajadawajhi")
    val sw = fqIn(a, "sajadawajhi", "fromstanding")
    val fs = fqIn(a, "fromstanding")
    val y = floor + h * 0.3f
    if (vb > 0f) {
        val p = a.b("versebow")
        openBook21(Offset(w * 0.25f, y - h * 0.04f), w * 0.2f, Color(0xFFF2E8CE), Color(0xFFB8A888), vb, glow = seg(p, 0.1f, 0.3f))
        drawLine(SalC.Gold, Offset(w * 0.17f, y - h * 0.07f), Offset(w * 0.23f, y - h * 0.07f), 3f * dp, StrokeCap.Round, alpha = vb * seg(p, 0.1f, 0.3f))
        val down = easeInOut(seg(p, 0.35f, 0.7f))
        if (down < 0.5f) sit12(Offset(w * 0.6f, y), h * 0.32f, SalC.Person, vb, -1f) else bow12(Offset(w * 0.6f, y), h * 0.36f, SalC.Person, vb, 1f)
        tagAt(a.tag("versebow", 0, 0), Offset(w * 0.5f, h * 0.12f), vb * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("versebow", 0, 1), Offset(w * 0.5f, h * 0.26f), vb * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (lk > 0f) {
        val p = a.b("listenerkinds")
        val down = seg(p, 0.15f, 0.35f)
        for (k in 0 until 2) {
            val foot = Offset(w * (0.7f - 0.22f * k), y)
            if (down < 0.5f) sit12(foot, h * 0.3f, SalC.Person, lk, -1f) else bow12(foot, h * 0.32f, SalC.Person, lk, 1f)
            if (k == 1) ear73(foot + Offset(0f, -h * 0.4f), w * 0.05f, SalC.Gold, lk * seg(p, 0.05f, 0.2f))
        }
        tagAt(a.tag("listenerkinds", 0, 0), Offset(w * 0.6f, h * 0.12f), lk * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val pass = seg(p, 0.5f, 1f)
        figure(Offset(mix(w * 0.05f, w * 0.3f, pass), y), h * 0.32f, Color(0xFFB8B0A0), lk * seg(p, 0.45f, 0.55f), walk = t * 6f)
        tagAt(a.tag("listenerkinds", 0, 1), Offset(w * 0.3f, h * 0.26f), lk * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val bowAt = Offset(w * 0.6f, y)
    if (bs > 0f) {
        val p = a.b("bowsubhan")
        bow12(bowAt, h * 0.4f, SalC.Person, bs, 1f)
        val u = seg(p, 0.2f, 0.9f)
        softGlow(Offset(bowAt.x - h * 0.15f, mix(y - h * 0.1f, h * 0.05f, u)), w * 0.04f, SalC.Gold, bs * bump(u))
        tagAt(a.tag("bowsubhan"), Offset(w * 0.4f, h * 0.15f), bs * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sw > 0f) {
        val p = a.b("sajadawajhi")
        bow12(bowAt, h * 0.4f, SalC.Person, sw, 1f)
        ear73(Offset(w * 0.3f, h * 0.35f), w * 0.07f, SalC.Gold, sw * seg(p, 0.3f, 0.5f))
        eye73(Offset(w * 0.15f, h * 0.35f), w * 0.08f, SalC.Gold, sw * seg(p, 0.4f, 0.6f))
        tagAt(a.tag("sajadawajhi"), Offset(w * 0.5f, h * 0.12f), sw * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fs > 0f) {
        val p = a.b("fromstanding")
        val g0 = seg(p, 0.05f, 0.4f)
        val a0 = Offset(w * 0.78f, y)
        if (g0 < 0.6f) takbir80(a0, h * 0.34f, SalC.Person, fs, 0f) else bow12(a0, h * 0.34f, SalC.Person, fs, 1f)
        drawArc(SalC.Gold, 270f, 90f, false, Offset(a0.x - h * 0.3f, a0.y - h * 0.4f), Size(h * 0.3f, h * 0.4f), alpha = fs * g0, style = Stroke(3f * dp))
        val g1 = seg(p, 0.4f, 0.75f)
        val a1 = Offset(w * 0.32f, y)
        if (g1 < 0.6f) sit12(a1, h * 0.3f, SalC.Person, fs * seg(p, 0.35f, 0.45f), -1f) else bow12(a1, h * 0.34f, SalC.Person, fs, 1f)
        drawArc(SalC.Rim, 270f, 90f, false, Offset(a1.x - h * 0.2f, a1.y - h * 0.22f), Size(h * 0.2f, h * 0.22f), alpha = fs * g1, style = Stroke(2f * dp))
        tagAt(a.tag("fromstanding"), Offset(w * 0.5f, h * 0.12f), fs * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 21. sal-nahy: when prayer is forbidden ═════════════

/**
 * Beats: twodawnbans (on the sun's path, the stretch from dawn to sunrise and from sunrise until a spear's height are shaded: «مِنْ طُلُوعِ
 * الْفَجْرِ الثَّانِي إِلَى طُلُوعِ الشَّمْسِ», «وَمِنْ طُلُوعِهَا حَتَّى تَرْتَفِعَ قَدْرَ رُمْحٍ»), threedaybans (then the noon standstill, from ʿaṣr to
 * sunset, and the setting itself: «وَعِنْدَ قِيَامِ الشَّمْسِ فِي كَبِدِ السَّمَاءِ», «وَمِنْ صَلَاةِ الْعَصْرِ إِلَى غُرُوبِ الشَّمْسِ», «وَإِذَا شَرَعَتْ
 * فِي الْغُرُوبِ حَتَّى تَغِيبَ»), allowedwithcause (inside the shaded times, three prayers with a reason are still ticked: «قَضَاءُ الْفَرَائِضِ
 * الْفَائِتَةِ», «وَرَكْعَتَا الطَّوَافِ», «وَذَوَاتُ الْأَسْبَابِ»), shadowstops (a stick's shadow shrinks, stands still, then turns east: «بِوُقُوفِ
 * الظِّلِّ»).
 */
internal fun DrawScope.forbiddentimesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twodawnbans" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "threedaybans" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "allowedwithcause" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "shadowstops" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { forbiddenTimesDraw(a) }
}

private fun DrawScope.forbiddenTimesDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    daySky12(SalC.DayTop, SalC.DayLow, ground, SalC.Sand)
    val db = fqIn(a, "twodawnbans", "allowedwithcause")
    val tb = fqIn(a, "threedaybans", "allowedwithcause")
    val ac = fqIn(a, "allowedwithcause", "shadowstops")
    val ss = fqIn(a, "shadowstops")
    val c = Offset(w * 0.5f, ground)
    val r = w * 0.42f
    val box = Offset(c.x - r, c.y - h * 0.62f)
    val sz = Size(r * 2f, h * 1.24f)
    // the night side (before sunrise) is the stretch just below the horizon on the right
    val zone = { from: Float, to: Float, alpha: Float -> drawArc(Color(0xFFC0503A), 180f + 180f * from, 180f * (to - from), false, box, sz, alpha = alpha, style = Stroke(8f * dp, cap = StrokeCap.Butt)) }
    val arcs = maxOf(db, tb, ac)
    if (arcs > 0f) drawArc(Color(0xFF6A7A9A), 180f, 180f, false, box, sz, alpha = arcs * 0.6f, style = Stroke(2f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp))))
    if (db > 0f) {
        val p = a.b("twodawnbans")
        val g0 = seg(p, 0.05f, 0.3f)
        drawLine(Color(0xFFC0503A), Offset(c.x - r - w * 0.04f * g0, ground + h * 0.01f), Offset(c.x - r, ground + h * 0.01f), 8f * dp, alpha = db)
        drawRect(Color(0xFFF2D8B0), Offset(0f, ground - h * 0.02f), Size(w * 0.12f, h * 0.02f), alpha = db * g0)
        tagAt(a.tag("twodawnbans", 0, 0), Offset(w * 0.25f, h * 0.88f), db * g0, back = Fq.Back, ink = ArtInk.Ivory)
        val g1 = seg(p, 0.5f, 0.75f)
        zone(0f, 0.08f * g1, db)
        sun12(Offset(c.x - r * cos(PI.toFloat() * 0.08f * g1), ground - h * 0.62f * sin(PI.toFloat() * 0.08f * g1)), w * 0.03f, db * g1)
        tagAt(a.tag("twodawnbans", 0, 1), Offset(w * 0.28f, h * 0.2f), db * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tb > 0f) {
        val p = a.b("threedaybans")
        zone(0f, 0.08f, tb)
        val g0 = seg(p, 0.05f, 0.25f)
        zone(0.48f, 0.48f + 0.04f * g0, tb)
        sun12(Offset(c.x, ground - h * 0.62f), w * 0.03f, tb * g0)
        tagAt(a.tag("threedaybans", 0, 0), Offset(w * 0.5f, h * 0.06f), tb * g0, back = Fq.Back, ink = ArtInk.Ivory)
        val g1 = seg(p, 0.35f, 0.55f)
        zone(0.7f, 0.7f + 0.26f * g1, tb)
        tagAt(a.tag("threedaybans", 0, 1), Offset(w * 0.78f, h * 0.24f), tb * g1, back = Fq.Back, ink = ArtInk.Ivory)
        val g2 = seg(p, 0.7f, 0.85f)
        zone(0.96f, 0.96f + 0.04f * g2, tb)
        sun12(Offset(c.x + r * 0.99f, ground - h * 0.04f), w * 0.03f, tb * g2)
        tagAt(a.tag("threedaybans", 0, 2), Offset(w * 0.75f, h * 0.88f), tb * g2, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ac > 0f) {
        val p = a.b("allowedwithcause")
        zone(0f, 0.08f, ac * 0.6f)
        zone(0.48f, 0.52f, ac * 0.6f)
        zone(0.7f, 1f, ac * 0.6f)
        drawRect(SalC.Top, Offset(0f, h * 0.3f), Size(w, h * 0.42f), alpha = 0.7f * ac)
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val cc = Offset(w * (0.8f - 0.3f * k), h * 0.5f)
            when (k) {
                0 -> { rakah74(cc + Offset(0f, h * 0.08f), h * 0.18f, 1f, ac * g); clock22(cc + Offset(w * 0.06f, -h * 0.08f), w * 0.025f, SalC.Gold, 0.7f, ac * g) }
                1 -> { kaaba29(cc + Offset(0f, h * 0.08f), w * 0.07f, ac * g); fqCircling(cc + Offset(0f, h * 0.06f), w * 0.08f, h * 0.03f, t, SalC.Person, ac * g, 8) }
                else -> mosque12(cc + Offset(0f, h * 0.1f), w * 0.14f, SalC.Mosque, SalC.Dome, ac * g)
            }
            tick14(cc + Offset(0f, -h * 0.14f), w * 0.03f, seg(p, 0.15f + 0.28f * k, 0.3f + 0.28f * k))
            tagAt(a.tag("allowedwithcause", 0, k), cc + Offset(0f, h * 0.3f), ac * g, back = Fq.Back, ink = ArtInk.Jade)
        }
    }
    if (ss > 0f) {
        val p = a.b("shadowstops")
        val u = when {
            p < 0.35f -> mix(0.35f, 0.5f, easeOut(seg(p, 0f, 0.35f)))
            p < 0.65f -> 0.5f
            else -> mix(0.5f, 0.58f, easeIn65(seg(p, 0.65f, 1f)))
        }
        val ang = PI.toFloat() * (1f - u)
        val sun = Offset(c.x + cos(ang) * r, ground - sin(ang) * h * 0.62f)
        sun12(sun, w * 0.035f, ss)
        val foot = Offset(w * 0.5f, ground)
        val ht = h * 0.22f
        val dir = foot - sun
        val len = if (dir.y > 1f) (dir.x * ht / dir.y).coerceIn(-ht * 2f, ht * 2f) else 0f
        drawLine(Color(0x99201C28), foot, foot + Offset(len, 0f), ht * 0.08f, StrokeCap.Round, alpha = ss)
        drawLine(Color(0xFF6A4A2E), foot, foot - Offset(0f, ht), ht * 0.06f, StrokeCap.Round, alpha = ss)
        tagAt(a.tag("shadowstops"), Offset(w * 0.5f, h * 0.9f), ss * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

private fun easeIn65(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ═════════════ 22. sal-jamaa: the congregation ═════════════

/**
 * Beats: rowsduty (men walk in from every side and fall into rows: «مِنْ أَهَمِّ الْوَاجِبَاتِ عَلَى الرِّجَالِ»), evenwar (a row at prayer beside shields
 * and tents; then a calm town: «فَكَيْفَ بِحَالِ السِّلْمِ»), benefits4 (four small scenes: two who meet, two who carry a load together, one who
 * counsels another, one who teaches from a book: «التَّعَارُفُ», «وَالتَّعَاوُنُ عَلَى الْبِرِّ», «وَالتَّوَاصِي بِالْحَقِّ», «وَتَعْلِيمُ الْجَاهِلِ»),
 * stepreward (each step to the mosque lifts a star and lets a dark speck fall: «بِكُلِّ خُطْوَةٍ حَسَنَةً», «وَرَفَعَهُ بِهَا دَرَجَةً», «وَحَطَّ
 * عَنْهُ بِهَا سَيِّئَةً»), familyneighbors (from the houses a father and his son and the neighbours walk out together: «مَعَ الْأَبْنَاءِ وَأَهْلِ
 * الْبَيْتِ وَالْجِيرَانِ»).
 */
internal fun DrawScope.congregationScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rowsduty" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "evenwar" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "benefits4" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "stepreward" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "familyneighbors" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { congregationDraw(a) }
}

private fun DrawScope.congregationDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.84f
    nightSky(t, SalC.Top, Color(0xFF6A6A9A), ground, starAlpha = 0.5f)
    drawRect(SalC.Ground, Offset(0f, ground), Size(w, h - ground))
    val rd = fqIn(a, "rowsduty", "evenwar")
    val ew = fqIn(a, "evenwar", "benefits4")
    val b4 = fqIn(a, "benefits4", "stepreward")
    val sr = fqIn(a, "stepreward", "familyneighbors")
    val fn = fqIn(a, "familyneighbors")
    if (rd > 0f) {
        val p = a.b("rowsduty")
        mosque12(Offset(w * 0.15f, ground), w * 0.26f, SalC.Mosque, SalC.Dome, rd, 0.7f)
        for (k in 0 until 7) {
            val u = easeInOut(seg(p, 0.05f + 0.05f * k, 0.5f + 0.05f * k))
            val from = Offset(w * (1.05f + 0.05f * k), ground)
            val to = Offset(w * (0.38f + 0.085f * k), ground)
            val at = from + (to - from) * u
            if (u < 1f) figure(at, h * 0.24f, SalC.Person, rd, walk = t * 6f) else pray12(at, h * 0.24f, SalC.Person, rd)
        }
        tagAt(a.tag("rowsduty"), Offset(w * 0.55f, h * 0.18f), rd * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ew > 0f) {
        val p = a.b("evenwar")
        val calm = seg(p, 0.5f, 0.7f)
        for (k in 0 until 3) fqTent(Offset(w * (0.12f + 0.12f * k), ground), w * 0.12f, Color(0xFFB8A888), ew * (1f - calm))
        for (k in 0 until 5) {
            val foot = Offset(w * (0.5f + 0.09f * k), ground)
            pray12(foot, h * 0.24f, SalC.Person, ew)
            if (k % 2 == 0) drawArc(Color(0xFF8A94B8), 90f, 180f, true, Offset(foot.x + w * 0.015f, foot.y - h * 0.12f), Size(w * 0.04f, h * 0.08f), alpha = ew * (1f - calm))
        }
        for (k in 0 until 4) house12(Offset(w * (0.08f + 0.1f * k), ground), w * 0.09f, Color(0xFFCDBFA6), Color(0xFF6A5A48), ew * calm, lit = 0.6f)
        tagAt(a.tag("evenwar"), Offset(w * 0.5f, h * 0.18f), ew * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (b4 > 0f) {
        val p = a.b("benefits4")
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.22f * k, 0.22f + 0.22f * k)
            val cx = w * (0.86f - 0.24f * k)
            val foot = Offset(cx, ground)
            when (k) {
                0 -> { figure(foot + Offset(-w * 0.03f, 0f), h * 0.24f, SalC.Person, b4 * g, arms = 0.6f); figure(foot + Offset(w * 0.03f, 0f), h * 0.24f, Color(0xFFB8B0A0), b4 * g) }
                1 -> {
                    figure(foot + Offset(-w * 0.04f, 0f), h * 0.24f, SalC.Person, b4 * g)
                    figure(foot + Offset(w * 0.04f, 0f), h * 0.24f, Color(0xFFB8B0A0), b4 * g)
                    drawLine(Fq.SandDark, foot + Offset(-w * 0.06f, -h * 0.2f), foot + Offset(w * 0.06f, -h * 0.2f), h * 0.02f, StrokeCap.Round, alpha = b4 * g)
                }
                2 -> { figure(foot + Offset(-w * 0.03f, 0f), h * 0.24f, SalC.Person, b4 * g, arms = 0.8f); figure(foot + Offset(w * 0.03f, 0f), h * 0.24f, Color(0xFFB8B0A0), b4 * g); voice12(foot + Offset(-w * 0.01f, -h * 0.22f), w * 0.02f, t, SalC.Gold, b4 * g, 1f) }
                else -> { sit12(foot + Offset(-w * 0.04f, 0f), h * 0.22f, SalC.Person, b4 * g, 1f); child12(foot + Offset(w * 0.05f, 0f), h * 0.3f, Color(0xFFB8B0A0), b4 * g); openBook21(foot + Offset(0f, -h * 0.12f), w * 0.05f, Color(0xFFF2E8CE), Color(0xFFB8A888), b4 * g) }
            }
            tagAt(a.tag("benefits4", 0, k), Offset(cx, h * (0.2f + 0.12f * (k % 2))), b4 * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (sr > 0f) {
        val p = a.b("stepreward")
        mosque12(Offset(w * 0.12f, ground), w * 0.22f, SalC.Mosque, SalC.Dome, sr, 0.7f)
        val u = easeInOut(seg(p, 0.05f, 0.95f))
        figure(Offset(mix(w * 0.92f, w * 0.3f, u), ground), h * 0.26f, SalC.Person, sr, walk = t * 7f)
        for (k in 0 until 7) {
            val sx = w * (0.88f - 0.08f * k)
            val passed = mix(w * 0.92f, w * 0.3f, u) < sx
            if (!passed) continue
            val age = fract(t * 0.5f + k * 0.13f)
            drawPath(starPath(Offset(sx, ground - h * 0.3f - age * h * 0.2f), w * 0.01f), SalC.Gold, alpha = sr * (1f - age))
            drawRect(SalC.Gold, Offset(sx - w * 0.015f, ground - h * 0.02f - h * 0.01f * k), Size(w * 0.03f, h * 0.008f), alpha = sr * 0.6f)
            drawCircle(Color(0xFF2A2430), w * 0.008f, Offset(sx, ground + age * h * 0.1f), alpha = sr * (1f - age))
        }
        for (k in 0 until 3) tagAt(a.tag("stepreward", 0, k), Offset(w * (0.75f - 0.25f * k), h * (0.15f + 0.08f * (k % 2))), sr * seg(p, 0.15f + 0.2f * k, 0.3f + 0.2f * k), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (fn > 0f) {
        val p = a.b("familyneighbors")
        for (k in 0 until 3) house12(Offset(w * (0.72f + 0.12f * k), ground), w * 0.11f, Color(0xFFCDBFA6), Color(0xFF6A5A48), fn, lit = 0.7f)
        mosque12(Offset(w * 0.12f, ground), w * 0.22f, SalC.Mosque, SalC.Dome, fn, 0.8f)
        val u = easeInOut(seg(p, 0.1f, 0.9f))
        val x = mix(w * 0.72f, w * 0.32f, u)
        figure(Offset(x, ground), h * 0.26f, SalC.Person, fn, walk = t * 6f)
        child12(Offset(x + w * 0.05f, ground), h * 0.26f, SalC.Person, fn, walk = t * 7f)
        figure(Offset(x + w * 0.12f, ground), h * 0.25f, Color(0xFFB8B0A0), fn, walk = t * 6f + 1f)
        tagAt(a.tag("familyneighbors"), Offset(w * 0.5f, h * 0.18f), fn * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 23. sal-imama: who should lead ═════════════

/**
 * Beats: rankorder (five steps light from the bottom: a book, a scroll of the sunna, a road of hijra, a crescent, the years: «أَقْرَؤُهُمْ لِكِتَابِ
 * اللَّهِ», «فَأَعْلَمُهُمْ بِالسُّنَّةِ», «فَأَقْدَمُهُمْ هِجْرَةً», «ثُمَّ أَقْدَمُهُمْ إِسْلَامًا», «ثُمَّ أَكْبَرُهُمْ سِنًّا»), readerfaqih (of two who read,
 * the one who also knows the fiqh steps forward: «قُدِّمَ الْقَارِئُ الْأَفْقَهُ»), hostfirst (in his own house the host stands in front of his guest;
 * the regular imam in his mosque: «وَصَاحِبُ الْبَيْتِ أَحَقُّ بِالْإِمَامَةِ مِنْ ضَيْفِهِ», «وَإِمَامُ الْمَسْجِدِ الرَّاتِبُ»), drawlots (three lots in a
 * cup; one rises and shines: «أُقْرِعَ بَيْنَهُمْ»), unknownfault (the one in front unknowingly not in wuḍūʾ; each in the rows behind is still ticked:
 * «فَصَلَاتُهُمْ صَحِيحَةٌ»).
 */
internal fun DrawScope.wholeadsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rankorder" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "readerfaqih" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "hostfirst" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "drawlots" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "unknownfault" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { whoLeadsDraw(a) }
}

private fun DrawScope.whoLeadsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val ro = fqIn(a, "rankorder", "readerfaqih")
    val rf = fqIn(a, "readerfaqih", "hostfirst")
    val hf = fqIn(a, "hostfirst", "drawlots")
    val dl = fqIn(a, "drawlots", "unknownfault")
    val uf = fqIn(a, "unknownfault")
    if (ro > 0f) {
        val p = a.b("rankorder")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.8f * ro)
        for (k in 0 until 5) {
            val g = seg(p, 0.05f + 0.18f * k, 0.2f + 0.18f * k)
            val y = h * (0.82f - 0.15f * k)
            val x = w * (0.62f + 0.04f * k)
            drawRect(SalC.Cell, Offset(x - w * 0.12f, y - h * 0.05f), Size(w * 0.24f, h * 0.1f), alpha = ro)
            drawRect(SalC.Gold, Offset(x - w * 0.12f, y - h * 0.05f), Size(w * 0.24f, h * 0.1f), alpha = ro * g * 0.35f)
            val ic = Offset(x + w * 0.08f, y)
            when (k) {
                0 -> openBook21(ic, w * 0.06f, Color(0xFFF2E8CE), Color(0xFFB8A888), ro * g)
                1 -> scroll14(ic, w * 0.05f, h * 0.06f, 1f, ro * g)
                2 -> drawLine(Fq.SandDark, ic + Offset(-w * 0.025f, h * 0.02f), ic + Offset(w * 0.025f, -h * 0.02f), 3f * dp, StrokeCap.Round, alpha = ro * g)
                3 -> crescent12(ic, w * 0.015f, SalC.Gold, SalC.Cell, ro * g)
                else -> hourglass73(ic, h * 0.07f, 0.8f, Fq.Sand, ro * g)
            }
            tagAt(a.tag("rankorder", 0, k), Offset(w * 0.25f, y), ro * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (rf > 0f) {
        val p = a.b("readerfaqih")
        val step = easeInOut(seg(p, 0.4f, 0.7f))
        for (k in 0 until 2) {
            val fwd = if (k == 1) step else 0f
            val foot = Offset(w * (0.6f - 0.25f * fwd + 0.2f * k), floor + h * 0.3f)
            figure(foot, h * 0.3f, SalC.Person, rf)
            openBook21(foot + Offset(0f, -h * 0.4f), w * 0.06f, Color(0xFFF2E8CE), Color(0xFFB8A888), rf)
            if (k == 1) scroll14(foot + Offset(w * 0.06f, -h * 0.44f), w * 0.04f, h * 0.05f, 1f, rf * seg(p, 0.1f, 0.3f))
        }
        tagAt(a.tag("readerfaqih"), Offset(w * 0.4f, h * 0.15f), rf * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (hf > 0f) {
        val p = a.b("hostfirst")
        drawRect(Color(0xFF3A3040), Offset.Zero, size, alpha = 0.7f * hf * (1f - seg(p, 0.55f, 0.65f)))
        val g0 = 1f - seg(p, 0.55f, 0.65f)
        pray12(Offset(w * 0.4f, floor + h * 0.2f), h * 0.32f, SalC.Person, hf * g0)
        pray12(Offset(w * 0.65f, floor + h * 0.32f), h * 0.3f, Color(0xFFB8B0A0), hf * g0)
        house12(Offset(w * 0.15f, floor + h * 0.3f), w * 0.14f, Color(0xFFCDBFA6), Color(0xFF6A5A48), hf * g0, lit = 0.6f)
        tagAt(a.tag("hostfirst", 0, 0), Offset(w * 0.5f, h * 0.15f), hf * seg(p, 0.1f, 0.3f) * g0, back = Fq.Back, ink = ArtInk.Gold)
        val g1 = seg(p, 0.6f, 0.75f)
        pray12(Offset(w * 0.2f, floor + h * 0.16f), h * 0.28f, SalC.Person, hf * g1)
        row74(floor + h * 0.32f, w * 0.42f, w * 0.88f, 5, h * 0.24f, SalC.Person, hf * g1)
        tagAt(a.tag("hostfirst", 0, 1), Offset(w * 0.5f, h * 0.15f), hf * g1, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (dl > 0f) {
        val p = a.b("drawlots")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.75f * dl)
        cup12(Offset(w * 0.5f, h * 0.75f), w * 0.12f, Fq.SandDark, dl)
        for (k in 0 until 3) {
            val pick = k == 1
            val up = if (pick) easeOut(seg(p, 0.3f, 0.6f)) else 0f
            val base = Offset(w * (0.47f + 0.03f * k), h * 0.62f - h * 0.25f * up)
            drawLine(if (pick) SalC.Gold else Color(0xFFB8A888), base, base + Offset(0f, -h * 0.14f), 4f * dp, StrokeCap.Round, alpha = dl)
            if (pick) softGlow(base + Offset(0f, -h * 0.07f), w * 0.05f, SalC.Gold, dl * up)
        }
        tagAt(a.tag("drawlots"), Offset(w * 0.5f, h * 0.12f), dl * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (uf > 0f) {
        val p = a.b("unknownfault")
        val imam = Offset(w * 0.18f, floor + h * 0.14f)
        pray12(imam, h * 0.26f, SalC.Person, uf)
        drawCircle(Color(0xFF6A5A6A), w * 0.012f, imam + Offset(0f, -h * 0.1f), alpha = uf * 0.8f)
        row74(floor + h * 0.32f, w * 0.42f, w * 0.88f, 5, h * 0.24f, SalC.Person, uf)
        for (k in 0 until 5) tick14(Offset(w * (0.42f + 0.115f * k), floor + h * 0.02f), w * 0.022f, seg(p, 0.3f + 0.06f * k, 0.4f + 0.06f * k))
        tagAt(a.tag("unknownfault"), Offset(w * 0.6f, h * 0.15f), uf * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Jade)
    }
}

// ═════════════ 24. sal-marid: the prayer of the sick ═════════════

/**
 * Beats: hardshipease (a heavy weight on one pan, and ease set on the other until they balance: «فَكُلَّمَا وُجِدَتِ الْمَشَقَّةُ وُجِدَ التَّيْسِيرُ»),
 * standsitside (standing with a stick, then seated, then lying on the side: «قَائِمًا», «فَقَاعِدًا», «فَعَلَى جَنْبٍ»), nodlower (seated, he leans a
 * little for the bow and lower for the prostration: «وَيَجْعَلُ سُجُودَهُ أَخْفَضَ مِنْ رُكُوعِهِ»), bentstand (one whose back will not straighten stands
 * bowed: «صَلَّى قَائِمًا عَلَى هَيْئَةِ الرَّاكِعِ»), sickjoin (two prayer marks drawn together into one time: «أَنْ يَجْمَعَ بَيْنَ الصَّلَاتَيْنِ»).
 */
internal fun DrawScope.sickprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "hardshipease" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "standsitside" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
        "nodlower" to Shot(Offset(w * 0.5f, h * 0.7f), 1.1f),
        "bentstand" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
        "sickjoin" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { sickPrayerDraw(a) }
}

private fun DrawScope.sickPrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t, lamps = 0.7f)
    val he = fqIn(a, "hardshipease", "standsitside")
    val ss = fqIn(a, "standsitside", "nodlower")
    val nl = fqIn(a, "nodlower", "bentstand")
    val bs = fqIn(a, "bentstand", "sickjoin")
    val sj = fqIn(a, "sickjoin")
    val y = floor + h * 0.3f
    if (he > 0f) {
        val p = a.b("hardshipease")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.7f * he)
        val tilt = mix(0.35f, 0f, easeInOut(seg(p, 0.35f, 0.7f)))
        balance12(Offset(w * 0.5f, h * 0.85f), h * 0.55f, tilt, Fq.Brass, he)
        drawRect(Color(0xFF5A5268), Offset(w * 0.24f, h * 0.5f + h * 0.1f * tilt), Size(w * 0.1f, h * 0.08f), alpha = he)
        softGlow(Offset(w * 0.71f, h * 0.48f - h * 0.1f * tilt), w * 0.06f, SalC.Gold, he * seg(p, 0.3f, 0.5f))
        tagAt(a.tag("hardshipease"), Offset(w * 0.5f, h * 0.12f), he * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ss > 0f) {
        val p = a.b("standsitside")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val foot = Offset(w * (0.8f - 0.3f * k), y)
            when (k) {
                0 -> { bow12(foot, h * 0.32f, SalC.Person, ss * g, 0.12f); drawLine(Fq.SandDark, foot + Offset(-h * 0.08f, 0f), foot + Offset(-h * 0.06f, -h * 0.18f), 2.5f * dp, StrokeCap.Round, alpha = ss * g) }
                1 -> sit12(foot, h * 0.3f, SalC.Person, ss * g, -1f)
                else -> { drawRect(Color(0xFF6A5A4A), Offset(foot.x - w * 0.12f, y - h * 0.04f), Size(w * 0.24f, h * 0.04f), alpha = ss * g); lying73(Offset(foot.x - w * 0.1f, y - h * 0.07f), w * 0.2f, SalC.Person, ss * g) }
            }
            tagAt(a.tag("standsitside", 0, k), Offset(foot.x, h * 0.15f + (k % 2) * h * 0.1f), ss * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (nl > 0f) {
        val p = a.b("nodlower")
        val seat = Offset(w * 0.55f, y)
        val lean = when {
            p < 0.15f -> 0f
            p < 0.4f -> 0.4f * easeInOut(seg(p, 0.15f, 0.3f))
            p < 0.5f -> 0.4f * (1f - easeInOut(seg(p, 0.4f, 0.5f)))
            else -> 0.8f * easeInOut(seg(p, 0.55f, 0.7f))
        }
        seatedNod83(seat, h * 0.4f, SalC.Person, nl, lean)
        drawLine(SalC.Gold, Offset(w * 0.25f, seat.y - h * 0.4f * 0.3f), Offset(w * 0.4f, seat.y - h * 0.4f * 0.3f), 1.5f * dp, alpha = nl * seg(p, 0.2f, 0.35f), pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        drawLine(SalC.Gold, Offset(w * 0.25f, seat.y - h * 0.4f * 0.15f), Offset(w * 0.4f, seat.y - h * 0.4f * 0.15f), 2.5f * dp, alpha = nl * seg(p, 0.6f, 0.75f))
        tagAt(a.tag("nodlower"), Offset(w * 0.5f, h * 0.12f), nl * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (bs > 0f) {
        val p = a.b("bentstand")
        bow12(Offset(w * 0.55f, y), h * 0.42f, SalC.Person, bs, 0.38f)
        softGlow(Offset(w * 0.5f, y - h * 0.25f), w * 0.1f, SalC.Gold, 0.4f * bs * seg(p, 0.3f, 0.5f))
        tagAt(a.tag("bentstand"), Offset(w * 0.5f, h * 0.12f), bs * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sj > 0f) {
        val p = a.b("sickjoin")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.75f * sj)
        val join = easeInOut(seg(p, 0.2f, 0.6f))
        val a0 = Offset(mix(w * 0.65f, w * 0.53f, join), h * 0.5f)
        val b0 = Offset(mix(w * 0.35f, w * 0.47f, join), h * 0.5f)
        drawCircle(SalC.Gold, w * 0.04f, a0, alpha = sj)
        drawCircle(SalC.Gold, w * 0.04f, b0, alpha = sj)
        drawRoundRect(SalC.Gold, Offset(w * 0.4f, h * 0.42f), Size(w * 0.2f, h * 0.16f), androidx.compose.ui.geometry.CornerRadius(h * 0.08f), alpha = sj * seg(p, 0.55f, 0.7f), style = Stroke(2.5f * dp))
        tagAt(a.tag("sickjoin"), Offset(w * 0.5f, h * 0.75f), sj * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
