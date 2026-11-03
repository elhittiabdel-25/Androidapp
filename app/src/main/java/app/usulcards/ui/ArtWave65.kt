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
 * Wave 65 (2026-11-03): the last two of «الأربعون النووية» (41–42) and the first three cards of «مختصر الشمائل». A needle that
 * turns to follow the revelation; forgiveness wider than the sky; and in the Shama'il, the Prophet ﷺ is only ever his light with
 * his name: never a body, a face or a hand. What is described is shown through what the companions compared it to (the moon,
 * silver), and through the things around him: cloth, perfume, a cup, a table. Companions are names in roundels.
 */

private val Back65 = Color(0xE6181420)

// ═════════════ 1. desire that follows (nw-41) ═════════════

private object FollowC65 {
    val Night = Color(0xFF121A2C)
    val NightLow = Color(0xFF2C3654)
    val Ground = Color(0xFF1C2232)
    val Gold = Color(0xFFF2C860)
    val Page = Color(0xFFF2E8D2)
    val Book = Color(0xFF2E5A4A)
    val Needle = Color(0xFFD06A5A)
    val Good = Color(0xFF6FCB9F)
    val Dim = Color(0xFF6A6A7A)
    val Stone = Color(0xFF8A5A3A)
    val Lens = Color(0xFFD8DCE6)
}

/**
 * Beats: needlealigns (a needle in a heart swings back and forth and then settles on the line of light from an open book: «تَبَعًا
 * لِمَا جَاءَ بِهِ»), lovesdoes (a heart sends one hand to take a good thing and turns the other from a dark one: «مَحَبَّةً
 * تَقْتَضِي فِعْلَهُ», «كَرَاهَةً تَقْتَضِي تَرْكَهُ»), smallandlarge (small and large matters alike are brought to the light and
 * ticked: «فِي الْقَلِيلِ وَالْكَثِيرِ», «وَالتَّسْلِيمُ لَهُ تَسْلِيمًا تَامًّا»), datestone (a date stone under a lens: the tiny
 * groove on its back and the thin skin over it: «النَّقِيرِ», «وَالْقِطْمِيرِ»).
 */
internal fun DrawScope.followsrevealedScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "needlealigns" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "lovesdoes" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "smallandlarge" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "datestone" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { followNight(a) }
}

private fun DrawScope.followNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val na = a.b("needlealigns")
    val ld = a.b("lovesdoes")
    val sl = a.b("smallandlarge")
    val ds = a.b("datestone")
    val ground = h * 0.86f
    nightSky(t, FollowC65.Night, FollowC65.NightLow, ground, starAlpha = 0.85f)
    drawRect(FollowC65.Ground, Offset(0f, ground), Size(w, h - ground))
    if (na > 0f) {
        val f = easeOut(seg(na, 0f, 0.25f)) * (1f - seg(ld, 0f, 0.4f))
        val book = Offset(w * 0.5f, h * 0.22f)
        openBook21(book, w * 0.3f, FollowC65.Page, FollowC65.Book, f, seg(na, 0.1f, 0.3f))
        val heart = Offset(w * 0.5f, h * 0.65f)
        heart12(heart, w * 0.12f, FollowC65.Dim, f, fill = false)
        drawLine(FollowC65.Gold, book + Offset(0f, h * 0.06f), heart, 2f * dp, alpha = f * 0.5f * seg(na, 0.2f, 0.4f))
        val settle = easeInOut(seg(na, 0.45f, 0.8f))
        val ang = -PI.toFloat() / 2f + (1f - settle) * 0.9f * sin(t * 3f)
        drawLine(FollowC65.Needle, heart, heart + Offset(cos(ang), sin(ang)) * (w * 0.1f), 4f * dp, StrokeCap.Round, alpha = f)
        drawCircle(FollowC65.Needle, w * 0.012f, heart, alpha = f)
        softGlow(heart, w * 0.12f, FollowC65.Gold, 0.4f * f * settle)
        tagAt(a.tag("needlealigns"), Offset(w * 0.5f, h * 0.9f), f * seg(na, 0.5f, 0.7f), back = Back65, ink = ArtInk.Gold)
    }
    if (ld > 0f) {
        val f = easeOut(seg(ld, 0f, 0.25f)) * (1f - seg(sl, 0f, 0.4f))
        val heart = Offset(w * 0.5f, h * 0.3f)
        heart12(heart, w * 0.06f, FollowC65.Gold, f, fill = true)
        for (k in 0 until 2) {
            val g = seg(ld, 0.15f + 0.35f * k, 0.35f + 0.35f * k)
            val x = w * (0.75f - 0.5f * k)
            val hand = Offset(x + (if (k == 1) -w * 0.08f * g else 0f), h * 0.66f)
            hands12(hand, h * 0.09f, FollowC65.Page, f * g)
            if (k == 0) drawCircle(FollowC65.Good, w * 0.025f, hand + Offset(0f, -h * 0.08f), alpha = f * g)
            else drawCircle(FollowC65.Ground, w * 0.03f, Offset(x + w * 0.05f, h * 0.6f), alpha = f * g)
            drawLine(FollowC65.Gold, heart, hand + Offset(0f, -h * 0.05f), 1.5f * dp, alpha = f * g * 0.6f)
            tagAt(a.tag("lovesdoes", 0, k), Offset(x, h * 0.84f), f * g, back = Back65, ink = if (k == 0) ArtInk.Gold else Color.Unspecified)
        }
    }
    if (sl > 0f) {
        val f = easeOut(seg(sl, 0f, 0.25f)) * (1f - seg(ds, 0f, 0.4f))
        val light = Offset(w * 0.5f, h * 0.3f)
        softGlow(light, w * 0.1f, FollowC65.Gold, 0.8f * f)
        for (k in 0 until 6) {
            val big = k % 2 == 0
            val u = easeInOut(seg(sl, 0.1f + 0.08f * k, 0.4f + 0.08f * k))
            val from = Offset(w * (0.1f + 0.16f * k), ground - h * 0.05f)
            val c = from + (light + Offset(w * 0.06f * (k - 2.5f), h * 0.2f) - from) * u
            drawRoundRect(FollowC65.Page, Offset(c.x - w * (if (big) 0.035f else 0.015f), c.y - h * (if (big) 0.03f else 0.012f)), Size(w * (if (big) 0.07f else 0.03f), h * (if (big) 0.06f else 0.024f)), CornerRadius(3f * dp), alpha = f * 0.9f)
            if (u >= 1f) checkMark(c + Offset(0f, h * 0.06f), w * 0.015f, FollowC65.Good, 2f * dp, f)
        }
        tagAt(a.tag("smallandlarge", 0, 0), Offset(w * 0.5f, h * 0.12f), f * seg(sl, 0.1f, 0.3f), back = Back65, ink = ArtInk.Gold)
        tagAt(a.tag("smallandlarge", 0, 1), Offset(w * 0.5f, h * 0.9f), f * seg(sl, 0.6f, 0.8f), back = Back65, ink = ArtInk.Gold)
    }
    if (ds > 0f) {
        val f = easeOut(seg(ds, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.5f)
        drawOval(FollowC65.Stone, Offset(c.x - w * 0.18f, c.y - h * 0.08f), Size(w * 0.36f, h * 0.16f), alpha = f)
        val g1 = seg(ds, 0.15f, 0.4f)
        drawLine(FollowC65.Ground, Offset(c.x - w * 0.12f, c.y), Offset(c.x - w * 0.12f + w * 0.24f * g1, c.y), 3f * dp, StrokeCap.Round, alpha = f)
        drawCircle(FollowC65.Ground, w * 0.012f, c + Offset(0f, -h * 0.02f), alpha = f * g1)
        val g2 = seg(ds, 0.5f, 0.75f)
        drawOval(FollowC65.Lens, Offset(c.x - w * 0.19f, c.y - h * 0.09f), Size(w * 0.38f, h * 0.18f), alpha = f * g2 * 0.6f, style = Stroke(1.5f * dp))
        drawCircle(FollowC65.Lens, w * 0.1f, c + Offset(w * 0.15f, -h * 0.12f), alpha = f, style = Stroke(3f * dp))
        drawLine(FollowC65.Lens, c + Offset(w * 0.22f, -h * 0.06f), c + Offset(w * 0.3f, 0f), 4f * dp, StrokeCap.Round, alpha = f)
        tagAt(a.tag("datestone", 0, 0), Offset(w * 0.3f, h * 0.72f), f * g1, back = Back65, ink = ArtInk.Gold)
        tagAt(a.tag("datestone", 0, 1), Offset(w * 0.7f, h * 0.72f), f * g2, back = Back65, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. forgiveness wider than the sky (nw-42) ═════════════

private object SkyC65 {
    val Night = Color(0xFF0E1426)
    val NightLow = Color(0xFF2A2C50)
    val Ground = Color(0xFF1A1C2C)
    val Gold = Color(0xFFF2C860)
    val Cloud = Color(0xFF3A3448)
    val Earth = Color(0xFF3A4A3A)
    val Mount = Color(0xFF2A2430)
    val Page = Color(0xFFF2E8D2)
    val Book = Color(0xFF5A2E3A)
    val Hands = Color(0xFFDCD4C4)
}

/**
 * Beats: threecauses (three lights rise in turn: «الدُّعَاءُ مَعَ الرَّجَاءِ», «وَالِاسْتِغْفَارُ», «وَالتَّوْحِيدُ»), cloudstosky
 * (dark clouds pile up to the top of the sky; asking forgiveness clears them: «وَلَوْ بَلَغَتِ الذُّنُوبُ عَنَانَ السَّمَاءِ»),
 * earthfulpardon (a dark heap as wide as the earth is met by as much light: «بِقُرَابِ الْأَرْضِ مَغْفِرَةً»), elixirdrop (one drop
 * of gold falls on dark mountains and they melt: «بِالْإِكْسِيرِ»), firstlastpage (the book's first page lights, «بِالنِّيَّةِ», and its
 * last, «بِسَعَةِ الْمَغْفِرَةِ»).
 */
internal fun DrawScope.skyhighforgiveScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threecauses" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "cloudstosky" to Shot(Offset(w * 0.5f, h * 0.45f), 1.0f),
        "earthfulpardon" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
        "elixirdrop" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "firstlastpage" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { skyNight(a) }
}

private fun DrawScope.skyNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tc = a.b("threecauses")
    val cs = a.b("cloudstosky")
    val ep = a.b("earthfulpardon")
    val ed = a.b("elixirdrop")
    val fl = a.b("firstlastpage")
    val ground = h * 0.86f
    nightSky(t, SkyC65.Night, SkyC65.NightLow, ground, starAlpha = 0.9f)
    drawRect(SkyC65.Ground, Offset(0f, ground), Size(w, h - ground))
    if (tc > 0f) {
        val f = easeOut(seg(tc, 0f, 0.25f)) * (1f - seg(cs, 0f, 0.4f))
        for (k in 0 until 3) {
            val g = seg(tc, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            val x = w * (0.78f - 0.28f * k)
            val y = mix(ground, h * 0.35f, easeOut(g))
            softGlow(Offset(x, y), w * (0.04f + 0.015f * k), SkyC65.Gold, f * g)
            tagAt(a.tag("threecauses", 0, k), Offset(x, y + h * 0.1f), f * g, back = Back65, ink = ArtInk.Gold)
        }
    }
    if (cs > 0f) {
        val f = easeOut(seg(cs, 0f, 0.25f)) * (1f - seg(ep, 0f, 0.4f))
        val pile = seg(cs, 0.05f, 0.45f)
        val clear = seg(cs, 0.55f, 0.9f)
        for (k in 0 until 14) {
            val row = k / 3
            if (row / 5f > pile) continue
            val c = Offset(w * (0.25f + 0.25f * (k % 3)) + sin(t * 0.5f + k) * w * 0.02f, ground - h * 0.08f - h * 0.15f * row)
            val gone = seg(clear, k / 18f, k / 18f + 0.3f)
            cloud38(c, w * 0.09f * (1f - 0.5f * gone), SkyC65.Cloud, f * (1f - gone))
        }
        hands12(Offset(w * 0.5f, ground - h * 0.02f), h * 0.08f, SkyC65.Hands, f * seg(cs, 0.45f, 0.55f))
        lightShafts(Offset(w * 0.5f, ground - h * 0.1f), -PI.toFloat() / 2f, 0.5f, h * 0.8f * clear, SkyC65.Gold, 0.5f * f * clear, t)
        tagAt(a.tag("cloudstosky"), Offset(w * 0.5f, h * 0.1f), f * seg(cs, 0.2f, 0.4f), back = Back65, ink = ArtInk.Gold)
    }
    if (ep > 0f) {
        val f = easeOut(seg(ep, 0f, 0.25f)) * (1f - seg(ed, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 1.25f)
        drawCircle(SkyC65.Earth, w * 0.75f, c, alpha = f)
        val heap = seg(ep, 0.05f, 0.35f)
        val light = seg(ep, 0.45f, 0.85f)
        drawArc(lerpColor(SkyC65.Cloud, SkyC65.Gold, light), 200f, 140f, true, Offset(c.x - w * 0.8f, c.y - w * 0.8f - h * 0.12f * heap), Size(w * 1.6f, w * 1.6f), alpha = f * 0.7f)
        softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.5f, SkyC65.Gold, 0.35f * f * light)
        tagAt(a.tag("earthfulpardon"), Offset(w * 0.5f, h * 0.2f), f * seg(ep, 0.5f, 0.7f), back = Back65, ink = ArtInk.Gold)
    }
    if (ed > 0f) {
        val f = easeOut(seg(ed, 0f, 0.25f)) * (1f - seg(fl, 0f, 0.4f))
        val melt = seg(ed, 0.45f, 0.9f)
        for (k in 0 until 3) {
            val x = w * (0.25f + 0.25f * k)
            val tall = h * (0.35f + 0.08f * (k % 2)) * (1f - 0.85f * melt)
            drawPath(Path().apply { moveTo(x - w * 0.16f, ground); lineTo(x, ground - tall); lineTo(x + w * 0.16f, ground); close() }, lerpColor(SkyC65.Mount, SkyC65.Gold, melt * 0.6f), alpha = f)
        }
        val drop = seg(ed, 0.1f, 0.45f)
        softGlow(Offset(w * 0.5f, mix(h * 0.08f, ground - h * 0.4f, drop)), w * 0.02f, SkyC65.Gold, f * (1f - melt))
        drawCircle(SkyC65.Gold, w * 0.008f, Offset(w * 0.5f, mix(h * 0.08f, ground - h * 0.4f, drop)), alpha = f * (1f - melt))
        tagAt(a.tag("elixirdrop"), Offset(w * 0.5f, h * 0.15f), f * seg(ed, 0.05f, 0.25f), back = Back65, ink = ArtInk.Gold)
    }
    if (fl > 0f) {
        val f = easeOut(seg(fl, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.52f)
        openBook21(c, w * 0.7f, SkyC65.Page, SkyC65.Book, f, 0f)
        softGlow(c + Offset(w * 0.17f, 0f), w * 0.15f, SkyC65.Gold, 0.6f * f * seg(fl, 0.1f, 0.35f))
        softGlow(c + Offset(-w * 0.17f, 0f), w * 0.15f, SkyC65.Gold, 0.6f * f * seg(fl, 0.5f, 0.75f))
        tagAt(a.tag("firstlastpage", 0, 0), c + Offset(w * 0.17f, -h * 0.2f), f * seg(fl, 0.1f, 0.3f), back = Back65, ink = ArtInk.Gold)
        tagAt(a.tag("firstlastpage", 0, 1), c + Offset(-w * 0.17f, -h * 0.2f), f * seg(fl, 0.5f, 0.7f), back = Back65, ink = ArtInk.Gold)
    }
}

// ═════════════ 3. brighter than the moon (sira-khalq) ═════════════

private object MoonC65 {
    val Night = Color(0xFF0E1428)
    val NightLow = Color(0xFF283252)
    val Ground = Color(0xFF181C2C)
    val Gold = Color(0xFFF2C860)
    val Bar = Color(0xFF3A4060)
    val Silver = Color(0xFFDCE2EC)
    val Page = Color(0xFFF2E8D2)
    val Book = Color(0xFF2E4A5A)
    val Good = Color(0xFF6FCB9F)
    val Bad = Color(0xFFD06A5A)
}

/**
 * Beats: neithertall (a tall bar and a short one; the light ﷺ shines between them at the middle), moonnotsword (a roundel with his
 * name, «الْبَرَاءَ بْنَ عَازِبٍ»; a full moon rises beside the light: «مِثْلَ الْقَمَرِ»), brighterthanmoon (on a moonlit night a
 * roundel, «جَابِرُ بْنُ سَمُرَةَ», looks from the moon to the light and the light shines the more: «أَحْسَنُ مِنَ الْقَمَرِ»),
 * silvercast (a roundel, «أَبُو هُرَيْرَةَ», and a silver shimmer about the light: «كَأَنَّمَا صِيغَ مِنْ فِضَّةٍ»), lastwhosaw
 * (roundels fade one by one until one is left: «أَبُو الطُّفَيْلِ»), gradedmarks (a book with ticks beside its reports and one cross:
 * «ضَعِيفٌ جِدًّا»).
 */
internal fun DrawScope.moonfaceScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "neithertall" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "moonnotsword" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "brighterthanmoon" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "silvercast" to Shot(Offset(w * 0.5f, h * 0.45f), 1.1f),
        "lastwhosaw" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "gradedmarks" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { moonNight(a) }
}

private fun DrawScope.moonNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val nt = a.b("neithertall")
    val ms = a.b("moonnotsword")
    val bm = a.b("brighterthanmoon")
    val sc = a.b("silvercast")
    val lw = a.b("lastwhosaw")
    val gm = a.b("gradedmarks")
    val ground = h * 0.86f
    nightSky(t, MoonC65.Night, MoonC65.NightLow, ground, starAlpha = 0.9f)
    drawRect(MoonC65.Ground, Offset(0f, ground), Size(w, h - ground))
    if (nt > 0f) {
        val f = easeOut(seg(nt, 0f, 0.25f)) * (1f - seg(ms, 0f, 0.4f))
        drawRect(MoonC65.Bar, Offset(w * 0.2f, ground - h * 0.6f * seg(nt, 0.05f, 0.25f)), Size(w * 0.06f, h * 0.6f * seg(nt, 0.05f, 0.25f)), alpha = f)
        drawRect(MoonC65.Bar, Offset(w * 0.74f, ground - h * 0.2f * seg(nt, 0.15f, 0.35f)), Size(w * 0.06f, h * 0.2f * seg(nt, 0.15f, 0.35f)), alpha = f)
        val light = Offset(w * 0.5f, ground - h * 0.4f)
        sirajMunir(light, w * 0.013f, f * seg(nt, 0.35f, 0.6f), t, nameAt = light + Offset(0f, -h * 0.13f))
    }
    if (ms > 0f || bm > 0f || sc > 0f) {
        val f = (easeOut(seg(ms, 0f, 0.25f)) + easeOut(seg(bm, 0f, 0.25f)) + easeOut(seg(sc, 0f, 0.25f))).coerceIn(0f, 1f) * (1f - seg(lw, 0f, 0.4f))
        val light = Offset(w * 0.62f, h * 0.38f)
        val moon = Offset(w * 0.25f, h * 0.22f)
        val bright = 1f + 0.5f * seg(bm, 0.5f, 0.8f)
        sirajMunir(light, w * 0.013f * bright, f, t, nameAt = light + Offset(0f, -h * 0.13f))
        moon12(moon, w * 0.06f, f * (if (ms > 0f && bm <= 0f) seg(ms, 0.4f, 0.6f) else 1f))
        val who = when { sc > 0f -> 2; bm > 0f -> 1; else -> 0 }
        val kind = listOf("moonnotsword", "brighterthanmoon", "silvercast")[who]
        val p = listOf(ms, bm, sc)[who]
        nameRoundel(Offset(w * 0.3f, h * 0.68f), w * 0.09f, a.big(kind, 0, 0), Roundel.Fills[who % 4], Roundel.Rim, f * seg(p, 0.05f, 0.2f))
        if (who == 1) {
            val look = sin(t * 1.2f)
            drawLine(MoonC65.Silver, Offset(w * 0.3f, h * 0.6f), if (look > 0f) moon else light, 1f * dp, alpha = f * 0.3f * seg(bm, 0.2f, 0.4f))
        }
        if (who == 2) for (k in 0 until 10) {
            val ang = k * PI.toFloat() / 5f + t * 0.4f
            drawCircle(MoonC65.Silver, w * 0.004f, light + Offset(cos(ang), sin(ang)) * (w * (0.09f + 0.01f * sin(t * 3f + k))), alpha = f * seg(sc, 0.3f, 0.5f))
        }
        tagAt(a.tag(kind, 0, 1), Offset(w * 0.62f, h * 0.62f), f * seg(p, 0.45f, 0.65f), back = Back65, ink = ArtInk.Gold)
    }
    if (lw > 0f) {
        val f = easeOut(seg(lw, 0f, 0.25f)) * (1f - seg(gm, 0f, 0.4f))
        for (k in 0 until 12) {
            val c = Offset(w * (0.12f + 0.076f * k), h * (0.55f + 0.06f * (k % 2)))
            val last = k == 7
            val fade = if (last) 0f else seg(lw, 0.1f + 0.04f * k, 0.25f + 0.04f * k)
            nameRoundel(c, w * 0.026f, null, Roundel.PlainFill, Roundel.PlainRim, f * (1f - fade))
        }
        val c = Offset(w * (0.12f + 0.076f * 7), h * 0.61f)
        nameRoundel(c, w * 0.026f + w * 0.05f * seg(lw, 0.65f, 0.85f), a.big("lastwhosaw"), Roundel.Fills[3], Roundel.Rim, f)
    }
    if (gm > 0f) {
        val f = easeOut(seg(gm, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.5f)
        openBook21(c, w * 0.7f, MoonC65.Page, MoonC65.Book, f, 0f)
        for (k in 0 until 5) {
            val y = c.y - h * 0.08f + h * 0.04f * k
            drawLine(MoonC65.Bar, Offset(w * 0.55f, y), Offset(w * 0.78f, y), 2f * dp, alpha = f * 0.6f)
            if (k != 3) checkMark(Offset(w * 0.52f, y), w * 0.012f, MoonC65.Good, 2f * dp, f * seg(gm, 0.1f + 0.1f * k, 0.2f + 0.1f * k))
            else crossMark(Offset(w * 0.52f, y), w * 0.012f, MoonC65.Bad, 2f * dp, f * seg(gm, 0.5f, 0.6f))
        }
        tagAt(a.tag("gradedmarks"), Offset(w * 0.5f, h * 0.22f), f * seg(gm, 0.55f, 0.75f), back = Back65, ink = MoonC65.Bad)
    }
}

// ═════════════ 4. his garments and perfume (sira-libas) ═════════════

private object ClothC65 {
    val Sky = Color(0xFFC6DCEA)
    val SkyLow = Color(0xFFF4EAD4)
    val Ground = Color(0xFFD2BC92)
    val White = Color(0xFFF8F6F0)
    val Stripe = Color(0xFF8A3A3A)
    val Stripe2 = Color(0xFF3A5A7A)
    val Line = Color(0xFF6A4A32)
    val Glass = Color(0xFFB8D8E8)
    val Scent = Color(0xFFE8C46A)
    val Gold = Color(0xFFD89A2A)
    val Cushion = Color(0xFF8A5A8A)
    val Oil = Color(0xFFD8B04A)
    val Milk = Color(0xFFF6F2E8)
    val Cup = Color(0xFFB88A58)
}

/**
 * Beats: shirtstripes (a long shirt on a peg and a striped cloth that unrolls: «الْقَمِيصُ», «الْحِبَرَةَ»), whitecloths (white
 * garments hang in the sun: «عَلَيْكُمْ بِالْبَيَاضِ مِنَ الثِّيَابِ»), perfumekept (a flask of perfume is brought to the light ﷺ
 * and kept: «لَا يَرُدُّ الطِّيبَ»), namednew (a new garment, and three names: «عِمَامَةً», «قَمِيصًا», «رِدَاءً»), notreturned3 (a
 * cushion, a little oil and a cup of milk: «الْوَسَائِدُ», «وَالدُّهْنُ», «وَاللَّبَنُ»).
 */
internal fun DrawScope.garmentsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "shirtstripes" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "whitecloths" to Shot(Offset(w * 0.5f, h * 0.45f), 1.0f),
        "perfumekept" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "namednew" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "notreturned3" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { clothDay(a) }
}

private fun DrawScope.shirt65(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawPath(Path().apply {
        moveTo(c.x - s * 0.18f, c.y - s * 0.5f)
        lineTo(c.x - s * 0.45f, c.y - s * 0.35f)
        lineTo(c.x - s * 0.38f, c.y - s * 0.2f)
        lineTo(c.x - s * 0.22f, c.y - s * 0.28f)
        lineTo(c.x - s * 0.24f, c.y + s * 0.5f)
        lineTo(c.x + s * 0.24f, c.y + s * 0.5f)
        lineTo(c.x + s * 0.22f, c.y - s * 0.28f)
        lineTo(c.x + s * 0.38f, c.y - s * 0.2f)
        lineTo(c.x + s * 0.45f, c.y - s * 0.35f)
        lineTo(c.x + s * 0.18f, c.y - s * 0.5f)
        close()
    }, color, alpha = alpha)
}

private fun DrawScope.clothDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ss = a.b("shirtstripes")
    val wc = a.b("whitecloths")
    val pk = a.b("perfumekept")
    val nn = a.b("namednew")
    val nr = a.b("notreturned3")
    val ground = h * 0.84f
    daySky12(ClothC65.Sky, ClothC65.SkyLow, ground, ClothC65.Ground)
    if (ss > 0f) {
        val f = easeOut(seg(ss, 0f, 0.25f)) * (1f - seg(wc, 0f, 0.4f))
        val peg = Offset(w * 0.7f, h * 0.2f)
        drawCircle(ClothC65.Line, w * 0.012f, peg, alpha = f)
        shirt65(peg + Offset(0f, h * 0.27f), h * 0.45f, ClothC65.White, f * seg(ss, 0.05f, 0.25f))
        val roll = easeInOut(seg(ss, 0.4f, 0.8f))
        val top = h * 0.3f
        for (k in 0 until 8) {
            val y = top + h * 0.05f * k
            if (y > top + h * 0.4f * roll) break
            drawRect(if (k % 2 == 0) ClothC65.Stripe else ClothC65.White, Offset(w * 0.12f, y), Size(w * 0.26f, h * 0.05f), alpha = f)
            drawLine(ClothC65.Stripe2, Offset(w * 0.12f, y + h * 0.025f), Offset(w * 0.38f, y + h * 0.025f), 1.5f * dp, alpha = f * 0.7f)
        }
        drawRoundRect(ClothC65.Stripe, Offset(w * 0.11f, top + h * 0.4f * roll - h * 0.02f), Size(w * 0.28f, h * 0.04f), CornerRadius(h * 0.02f), alpha = f)
        tagAt(a.tag("shirtstripes", 0, 0), Offset(w * 0.7f, h * 0.84f), f * seg(ss, 0.1f, 0.3f), back = Back65, ink = ArtInk.Gold)
        tagAt(a.tag("shirtstripes", 0, 1), Offset(w * 0.25f, h * 0.84f), f * seg(ss, 0.45f, 0.65f), back = Back65, ink = ArtInk.Gold)
    }
    if (wc > 0f) {
        val f = easeOut(seg(wc, 0f, 0.25f)) * (1f - seg(pk, 0f, 0.4f))
        sun12(Offset(w * 0.85f, h * 0.12f), w * 0.04f, f)
        drawLine(ClothC65.Line, Offset(w * 0.05f, h * 0.3f), Offset(w * 0.95f, h * 0.3f), 2f * dp, alpha = f)
        for (k in 0 until 4) {
            val g = seg(wc, 0.1f + 0.12f * k, 0.25f + 0.12f * k)
            val sway = sin(t * 2f + k) * w * 0.005f
            shirt65(Offset(w * (0.17f + 0.22f * k) + sway, h * 0.45f), h * 0.3f, ClothC65.White, f * g)
        }
        tagAt(a.tag("whitecloths"), Offset(w * 0.5f, h * 0.72f), f * seg(wc, 0.3f, 0.5f), back = Back65, ink = ArtInk.Gold)
    }
    if (pk > 0f) {
        val f = easeOut(seg(pk, 0f, 0.25f)) * (1f - seg(nn, 0f, 0.4f))
        val light = Offset(w * 0.68f, h * 0.38f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.11f))
        val go = easeInOut(seg(pk, 0.15f, 0.55f))
        val flask = Offset(mix(w * 0.15f, w * 0.55f, go), h * 0.6f)
        drawRoundRect(ClothC65.Glass, Offset(flask.x - w * 0.03f, flask.y - h * 0.06f), Size(w * 0.06f, h * 0.08f), CornerRadius(8f * dp), alpha = f)
        drawRect(ClothC65.Gold, Offset(flask.x - w * 0.012f, flask.y - h * 0.09f), Size(w * 0.024f, h * 0.03f), alpha = f)
        for (k in 0 until 4) {
            val u = fract(t * 0.4f + k * 0.25f)
            drawPath(Path().apply { val s = flask + Offset(0f, -h * 0.1f - h * 0.15f * u); moveTo(s.x, s.y); quadraticTo(s.x + w * 0.03f * sin(u * 6f + k), s.y - h * 0.03f, s.x, s.y - h * 0.06f) }, ClothC65.Scent, alpha = f * (1f - u) * 0.7f, style = Stroke(2f * dp, cap = StrokeCap.Round))
        }
        softGlow(light, w * 0.12f, ClothC65.Scent, 0.4f * f * seg(pk, 0.55f, 0.8f))
        tagAt(a.tag("perfumekept"), Offset(w * 0.4f, h * 0.18f), f * seg(pk, 0.5f, 0.7f), back = Back65, ink = ArtInk.Gold)
    }
    if (nn > 0f) {
        val f = easeOut(seg(nn, 0f, 0.25f)) * (1f - seg(nr, 0f, 0.4f))
        for (k in 0 until 3) {
            val g = seg(nn, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            val c = Offset(w * (0.78f - 0.28f * k), h * 0.5f)
            when (k) {
                0 -> { drawOval(ClothC65.White, Offset(c.x - w * 0.08f, c.y - h * 0.05f), Size(w * 0.16f, h * 0.1f), alpha = f * g); drawArc(ClothC65.Line, 200f, 140f, false, Offset(c.x - w * 0.07f, c.y - h * 0.045f), Size(w * 0.14f, h * 0.09f), alpha = f * g * 0.4f, style = Stroke(1.5f * dp)) }
                1 -> shirt65(c, h * 0.25f, ClothC65.White, f * g)
                else -> drawRoundRect(ClothC65.White, Offset(c.x - w * 0.1f, c.y - h * 0.08f), Size(w * 0.2f, h * 0.16f), CornerRadius(4f * dp), alpha = f * g)
            }
            tagAt(a.tag("namednew", 0, k), c + Offset(0f, h * 0.17f), f * g, back = Back65, ink = ArtInk.Gold)
        }
    }
    if (nr > 0f) {
        val f = easeOut(seg(nr, 0f, 0.25f))
        for (k in 0 until 3) {
            val g = seg(nr, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            val c = Offset(w * (0.78f - 0.28f * k), ground - h * 0.06f)
            when (k) {
                0 -> drawRoundRect(ClothC65.Cushion, Offset(c.x - w * 0.1f, c.y - h * 0.05f), Size(w * 0.2f, h * 0.1f), CornerRadius(h * 0.04f), alpha = f * g)
                1 -> { drawRoundRect(ClothC65.Glass, Offset(c.x - w * 0.025f, c.y - h * 0.06f), Size(w * 0.05f, h * 0.1f), CornerRadius(6f * dp), alpha = f * g); drawRect(ClothC65.Oil, Offset(c.x - w * 0.02f, c.y - h * 0.02f), Size(w * 0.04f, h * 0.055f), alpha = f * g) }
                else -> cup12(c + Offset(0f, h * 0.05f), w * 0.09f, ClothC65.Cup, f * g, ClothC65.Milk)
            }
            softGlow(c, w * 0.08f, ClothC65.Gold, 0.3f * f * g)
            tagAt(a.tag("notreturned3", 0, k), c + Offset(0f, -h * 0.18f), f * g, back = Back65, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 5. the humble table (sira-akl) ═════════════

private object TableC65 {
    val Wall = Color(0xFF2E2630)
    val WallLow = Color(0xFF4A3A42)
    val Floor = Color(0xFF3A2E2A)
    val Cloth = Color(0xFFB04A3A)
    val Bowl = Color(0xFFD8C4A0)
    val Food = Color(0xFFC0802A)
    val Gold = Color(0xFFF2C860)
    val Cushion = Color(0xFF6A4A6A)
    val Bad = Color(0xFFD06A5A)
    val Milk = Color(0xFFF6F2E8)
    val Cup = Color(0xFFB88A58)
}

/**
 * Beats: norecline (a leaning cushion is set aside, and a plain place is laid low at the cloth: «فَلَا آكُلُ مُتَّكِئًا»),
 * tablelifted (the cloth is lifted and praise rises from it: «حَمْدًا كَثِيرًا طَيِّبًا مُبَارَكًا فِيهِ»), basmalabless (a bowl
 * enough for six roundels; a seventh comes without the name of Allah and it is gone in two bites: «لَوْ سَمَّى لَكَفَاكُمْ»),
 * firstandlast (a meal begun without the name; the name is said, and it lights the beginning and the end: «بِسْمِ اللَّهِ أَوَّلَهُ
 * وَآخِرَهُ»), threesips (a cup; a drink taken in three breaths, three soft rings: «أَمْرَأُ وَأَرْوَى»), milkbless (a cup of milk that
 * glows: «اللَّهُمَّ بَارِكْ لَنَا فِيهِ وَزِدْنَا مِنْهُ»).
 */
internal fun DrawScope.humbletableScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "norecline" to Shot(Offset(w * 0.5f, h * 0.65f), 1.1f),
        "tablelifted" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "basmalabless" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "firstandlast" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "threesips" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "milkbless" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { tableRoom(a) }
}

private fun DrawScope.tableRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val nr = a.b("norecline")
    val tl = a.b("tablelifted")
    val bb = a.b("basmalabless")
    val fl = a.b("firstandlast")
    val ts = a.b("threesips")
    val mb = a.b("milkbless")
    val floor = h * 0.8f
    drawRect(Brush.verticalGradient(listOf(TableC65.Wall, TableC65.WallLow), 0f, floor), Offset.Zero, Size(w, floor))
    drawRect(TableC65.Floor, Offset(0f, floor), Size(w, h - floor))
    if (nr > 0f) {
        val f = easeOut(seg(nr, 0f, 0.25f)) * (1f - seg(tl, 0f, 0.4f))
        drawOval(TableC65.Cloth, Offset(w * 0.25f, floor + h * 0.02f), Size(w * 0.5f, h * 0.08f), alpha = f)
        drawOval(TableC65.Bowl, Offset(w * 0.44f, floor + h * 0.025f), Size(w * 0.12f, h * 0.04f), alpha = f)
        val away = easeInOut(seg(nr, 0.2f, 0.5f))
        drawRoundRect(TableC65.Cushion, Offset(mix(w * 0.72f, w * 0.9f, away), floor - h * 0.1f + h * 0.05f * away), Size(w * 0.1f, h * 0.12f - h * 0.05f * away), CornerRadius(h * 0.03f), alpha = f * (1f - 0.4f * away))
        softGlow(Offset(w * 0.5f, floor - h * 0.08f), w * 0.1f, TableC65.Gold, 0.4f * f * seg(nr, 0.5f, 0.7f))
        tagAt(a.tag("norecline"), Offset(w * 0.5f, h * 0.3f), f * seg(nr, 0.4f, 0.6f), back = Back65, ink = ArtInk.Gold)
    }
    if (tl > 0f) {
        val f = easeOut(seg(tl, 0f, 0.25f)) * (1f - seg(bb, 0f, 0.4f))
        val lift = easeInOut(seg(tl, 0.15f, 0.45f))
        drawOval(TableC65.Cloth, Offset(w * 0.25f, floor + h * 0.02f - h * 0.3f * lift), Size(w * 0.5f, h * 0.08f * (1f - 0.5f * lift)), alpha = f)
        for (k in 0 until 6) {
            val u = fract(t * 0.35f + k / 6f)
            softGlow(Offset(w * (0.35f + 0.06f * k), floor - h * 0.3f * lift - h * 0.4f * u), w * 0.015f, TableC65.Gold, f * seg(tl, 0.4f, 0.6f) * (1f - u))
        }
        tagAt(a.tag("tablelifted"), Offset(w * 0.5f, h * 0.15f), f * seg(tl, 0.4f, 0.6f), back = Back65, ink = ArtInk.Gold)
    }
    if (bb > 0f) {
        val f = easeOut(seg(bb, 0f, 0.25f)) * (1f - seg(fl, 0f, 0.4f))
        val bowl = Offset(w * 0.5f, floor + h * 0.03f)
        val eaten = seg(bb, 0.4f, 0.6f)
        drawOval(TableC65.Bowl, Offset(bowl.x - w * 0.1f, bowl.y - h * 0.03f), Size(w * 0.2f, h * 0.06f), alpha = f)
        drawOval(TableC65.Food, Offset(bowl.x - w * 0.08f, bowl.y - h * 0.035f), Size(w * 0.16f * (1f - eaten), h * 0.03f), alpha = f * (1f - eaten))
        for (k in 0 until 6) {
            val ang = PI.toFloat() * (1.1f + 0.8f * k / 5f)
            nameRoundel(bowl + Offset(cos(ang) * w * 0.25f, sin(ang) * h * 0.18f), w * 0.028f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        }
        val come = easeInOut(seg(bb, 0.15f, 0.35f))
        nameRoundel(Offset(mix(w * 0.95f, w * 0.68f, come), floor - h * 0.02f), w * 0.028f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        tagAt(a.tag("basmalabless"), Offset(w * 0.5f, h * 0.18f), f * seg(bb, 0.6f, 0.8f), back = Back65, ink = ArtInk.Gold)
    }
    if (fl > 0f) {
        val f = easeOut(seg(fl, 0f, 0.25f)) * (1f - seg(ts, 0f, 0.4f))
        val y = floor - h * 0.15f
        drawLine(TableC65.Bowl, Offset(w * 0.15f, y), Offset(w * 0.85f, y), 4f * dp, StrokeCap.Round, alpha = f * 0.6f)
        val said = seg(fl, 0.35f, 0.55f)
        softGlow(Offset(w * 0.85f, y), w * 0.06f, TableC65.Gold, f * said)
        softGlow(Offset(w * 0.15f, y), w * 0.06f, TableC65.Gold, f * said)
        drawLine(TableC65.Gold, Offset(w * 0.15f, y), Offset(w * 0.85f, y), 3f * dp, StrokeCap.Round, alpha = f * seg(fl, 0.5f, 0.8f))
        tagAt(a.tag("firstandlast"), Offset(w * 0.5f, h * 0.3f), f * seg(fl, 0.35f, 0.55f), back = Back65, ink = ArtInk.Gold)
    }
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, 0.25f)) * (1f - seg(mb, 0f, 0.4f))
        val c = Offset(w * 0.5f, floor)
        cup12(c, w * 0.16f, TableC65.Cup, f, Color(0xFF8AC4EA))
        for (k in 0 until 3) {
            val g = seg(ts, 0.15f + 0.25f * k, 0.3f + 0.25f * k)
            drawCircle(TableC65.Bowl, w * (0.04f + 0.06f * g), c + Offset(0f, -h * 0.25f - h * 0.08f * k), alpha = f * g * (1f - g) * 3f, style = Stroke(2f * dp))
        }
        tagAt(a.tag("threesips"), Offset(w * 0.5f, h * 0.15f), f * seg(ts, 0.7f, 0.9f), back = Back65, ink = ArtInk.Gold)
    }
    if (mb > 0f) {
        val f = easeOut(seg(mb, 0f, 0.25f))
        val c = Offset(w * 0.5f, floor)
        cup12(c, w * 0.18f, TableC65.Cup, f, TableC65.Milk)
        softGlow(c + Offset(0f, -h * 0.12f), w * 0.15f, TableC65.Gold, 0.5f * f * seg(mb, 0.3f, 0.6f))
        tagAt(a.tag("milkbless"), Offset(w * 0.5f, h * 0.22f), f * seg(mb, 0.2f, 0.4f), back = Back65, ink = ArtInk.Gold)
    }
}
