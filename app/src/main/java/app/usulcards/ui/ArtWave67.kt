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
 * Wave 67 (2026-11-03): «مختصر الشمائل» (3), the last four cards. His recitation and his weeping; «I am only a servant»; his
 * character and modesty; and the day he died. The Prophet ﷺ is only ever his light with his name; companions, Ahl al-Bayt and the
 * Mothers of the Believers are names in roundels; no grave is drawn and there are no figures.
 */

private val Back67 = Color(0xE6181420)

// ═════════════ 1. his recitation (sira-qiraa) ═════════════

private object ReciteC67 {
    val Night = Color(0xFF0E1428)
    val NightLow = Color(0xFF283052)
    val Ground = Color(0xFF181C2C)
    val Gold = Color(0xFFF2C860)
    val Wave = Color(0xFF8AC4EA)
    val Tear = Color(0xFFDCEBF6)
    val Pot = Color(0xFF6A5A4A)
    val Steam = Color(0xFFD8DCE6)
    val Bed = Color(0xFF8A6A44)
    val House = Color(0xFF5A5068)
}

/**
 * Beats: drawnout (a roundel with his name, «أَنَسٌ», and a sound that stretches long and smooth: «كَانَ يَمُدُّ مَدًّا»), readtome (a
 * roundel, «ابْنِ مَسْعُودٍ», reads from a scroll, «سُورَةَ النِّسَاءِ», to the light ﷺ, and small drops of light fall from it),
 * simmering (a roundel, «عَبْدُ اللَّهِ بْنُ الشِّخِّيرِ», by the light at prayer, and a pot simmering beside: «كَأَزِيزِ الْمِرْجَلِ»),
 * loudorlow (a roundel, «عَائِشَةُ»; the recitation comes now loud, now low: «كُلُّ ذَلِكَ قَدْ كَانَ يَفْعَلُ»), heardatnight (a
 * roundel, «أُمُّ هَانِئٍ», on a raised bed at night, and the recitation reaching her over the houses).
 */
internal fun DrawScope.recitelightScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "drawnout" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "readtome" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "simmering" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "loudorlow" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "heardatnight" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { reciteNight(a) }
}

private fun DrawScope.reciteNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val dn = a.b("drawnout")
    val rt = a.b("readtome")
    val sm = a.b("simmering")
    val ll = a.b("loudorlow")
    val hn = a.b("heardatnight")
    val ground = h * 0.86f
    nightSky(t, ReciteC67.Night, ReciteC67.NightLow, ground, starAlpha = 0.9f)
    drawRect(ReciteC67.Ground, Offset(0f, ground), Size(w, h - ground))
    val light = Offset(w * 0.68f, h * 0.35f)
    val wave = { from: Offset, len: Float, amp: Float, alpha: Float ->
        val p = Path().apply { moveTo(from.x, from.y); for (j in 1..40) { val x = from.x - len * j / 40f; lineTo(x, from.y + sin(j * 0.5f - t * 4f) * amp) } }
        drawPath(p, ReciteC67.Wave, alpha = alpha, style = Stroke(3f * dp, cap = StrokeCap.Round))
    }
    if (dn > 0f) {
        val f = easeOut(seg(dn, 0f, BEAT_IN)) * (1f - seg(rt, 0f, BEAT_OUT))
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        wave(light + Offset(-w * 0.06f, h * 0.02f), w * 0.5f * seg(dn, 0.2f, 0.7f), h * 0.015f, f)
        nameRoundel(Offset(w * 0.22f, h * 0.65f), w * 0.08f, a.big("drawnout", 0, 0), Roundel.Fills[1], Roundel.Rim, f * seg(dn, 0.05f, 0.2f))
        tagAt(a.tag("drawnout", 0, 1), Offset(w * 0.6f, h * 0.7f), f * seg(dn, 0.5f, 0.7f), back = Back67, ink = ArtInk.Gold)
    }
    if (rt > 0f) {
        val f = easeOut(seg(rt, 0f, BEAT_IN)) * (1f - seg(sm, 0f, 0.4f))
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        nameRoundel(Offset(w * 0.25f, h * 0.4f), w * 0.08f, a.big("readtome", 0, 0), Roundel.Fills[2], Roundel.Rim, f * seg(rt, 0.05f, 0.2f))
        scroll14(Offset(w * 0.3f, h * 0.68f), w * 0.3f, h * 0.18f, easeOut(seg(rt, 0.1f, 0.35f)), f)
        tagAt(a.tag("readtome", 0, 1), Offset(w * 0.3f, h * 0.84f), f * seg(rt, 0.25f, 0.45f), back = Back67, ink = ArtInk.Gold)
        val tears = seg(rt, 0.6f, 0.75f)
        for (k in 0 until 4) {
            val u = fract(t * 0.5f + k * 0.25f)
            drawCircle(ReciteC67.Tear, w * 0.004f, light + Offset(w * 0.012f * (if (k % 2 == 0) -1f else 1f), h * 0.04f + h * 0.12f * u), alpha = f * tears * (1f - u))
        }
    }
    if (sm > 0f) {
        val f = easeOut(seg(sm, 0f, 0.25f)) * (1f - seg(ll, 0f, BEAT_OUT))
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        nameRoundel(Offset(w * 0.22f, h * 0.38f), w * 0.08f, a.big("simmering", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(sm, 0.05f, 0.2f))
        val pot = Offset(w * 0.4f, ground)
        drawArc(ReciteC67.Pot, 0f, 180f, true, Offset(pot.x - w * 0.1f, pot.y - h * 0.16f), Size(w * 0.2f, h * 0.2f), alpha = f)
        for (k in 0 until 6) {
            val u = fract(t * 0.7f + k / 6f)
            drawCircle(ReciteC67.Steam, w * 0.008f + w * 0.01f * u, pot + Offset(w * 0.05f * sin(k * 1.9f), -h * 0.08f - h * 0.15f * u), alpha = f * seg(sm, 0.3f, 0.5f) * (1f - u) * 0.7f)
        }
        tagAt(a.tag("simmering", 0, 1), Offset(w * 0.55f, h * 0.62f), f * seg(sm, 0.4f, 0.6f), back = Back67, ink = ArtInk.Gold)
    }
    if (ll > 0f) {
        val f = easeOut(seg(ll, 0f, BEAT_IN)) * (1f - seg(hn, 0f, BEAT_OUT))
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        val loud = 0.5f + 0.5f * sin(t * 1.3f)
        wave(light + Offset(-w * 0.06f, h * 0.02f), w * 0.5f, h * (0.005f + 0.03f * loud), f * seg(ll, 0.2f, 0.4f))
        nameRoundel(Offset(w * 0.22f, h * 0.65f), w * 0.08f, a.big("loudorlow", 0, 0), Roundel.Fills[2], Roundel.Rim, f * seg(ll, 0.05f, 0.2f))
        tagAt(a.tag("loudorlow", 0, 1), Offset(w * 0.6f, h * 0.75f), f * seg(ll, 0.45f, 0.65f), back = Back67, ink = ArtInk.Gold)
    }
    if (hn > 0f) {
        val f = easeOut(seg(hn, 0f, 0.25f))
        for (k in 0 until 5) drawRect(ReciteC67.House, Offset(w * (0.05f + 0.19f * k), ground - h * (0.12f + 0.04f * (k % 2))), Size(w * 0.16f, h * (0.12f + 0.04f * (k % 2))), alpha = f)
        val src = Offset(w * 0.85f, ground - h * 0.25f)
        sirajMunir(src, w * 0.01f, f, t, nameAt = src + Offset(0f, -h * 0.08f))
        for (k in 0 until 3) {
            val u = fract(t * 0.3f + k / 3f)
            drawArc(ReciteC67.Wave, 150f, 60f, false, Offset(src.x - w * (0.1f + 0.6f * u), src.y - w * (0.1f + 0.6f * u)), Size(w * (0.2f + 1.2f * u), w * (0.2f + 1.2f * u)), alpha = f * (1f - u) * 0.6f, style = Stroke(2f * dp))
        }
        val bed = Offset(w * 0.2f, ground - h * 0.28f)
        drawRect(ReciteC67.Bed, Offset(bed.x - w * 0.1f, bed.y), Size(w * 0.2f, h * 0.03f), alpha = f)
        for (k in 0 until 2) drawLine(ReciteC67.Bed, Offset(bed.x - w * 0.09f + w * 0.18f * k, bed.y), Offset(bed.x - w * 0.09f + w * 0.18f * k, ground - h * 0.16f), 3f * dp, alpha = f)
        nameRoundel(bed + Offset(0f, -h * 0.08f), w * 0.07f, a.big("heardatnight"), Roundel.Fills[3], Roundel.Rim, f * seg(hn, 0.1f, 0.3f))
    }
}

// ═════════════ 2. «I am only a servant» (sira-tawadu) ═════════════

private object ServantC67 {
    val Sky = Color(0xFFC6DCEA)
    val SkyLow = Color(0xFFF2E6CC)
    val Ground = Color(0xFFD2BC92)
    val Gold = Color(0xFFD89A2A)
    val Road = Color(0xFFE8DCC0)
    val Wall = Color(0xFFE2D2B4)
    val Trim = Color(0xFF8A6A44)
    val Cloth = Color(0xFFF2EEE4)
    val Thread = Color(0xFF8A3A3A)
    val Goat = Color(0xFF8A6A4A)
    val Pail = Color(0xFF9A9AA8)
    val Milk = Color(0xFFF6F2E8)
    val Dish = Color(0xFFD8C4A0)
    val Camel = Color(0xFFB88A58)
    val Worn = Color(0xFF9A8A70)
    val Coin = Color(0xFFC8C8D0)
    val Step = Color(0xFFB8A27E)
}

/**
 * Beats: servantmessenger (the light ﷺ and, written large beneath it: «عَبْدُ اللَّهِ وَرَسُولُهُ»), anystreet (a plain roundel and the
 * light meet at a corner of the town's streets: «فِي أَيِّ طَرِيقِ الْمَدِينَةِ شِئْتِ»), homechores (a garment mended, a goat milked, a
 * room swept: «يَفْلِي ثَوْبَهُ», «وَيَحْلُبُ شَاتَهُ», «وَيَخْدُمُ نَفْسَهُ»), smallgift (a small dish is offered and taken: «لَوْ أُهْدِيَ
 * إِلَيَّ كُرَاعٌ لَقَبِلْتُ»), plainsaddle (a camel with a worn saddle and a cheap cloth: «لَا تُسَاوِي 4 دَرَاهِمَ», «حَجًّا لَا رِيَاءَ
 * فِيهِ وَلَا سُمْعَةَ»), walkingvisit (footsteps go to a house where a roundel, «جَابِرًا», lies ill).
 */
internal fun DrawScope.onlyaservantScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "servantmessenger" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "anystreet" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "homechores" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "smallgift" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "plainsaddle" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "walkingvisit" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { servantDay(a) }
}

private fun DrawScope.servantDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sm = a.b("servantmessenger")
    val ast = a.b("anystreet")
    val hc = a.b("homechores")
    val sg = a.b("smallgift")
    val ps = a.b("plainsaddle")
    val wv = a.b("walkingvisit")
    val ground = h * 0.84f
    daySky12(ServantC67.Sky, ServantC67.SkyLow, ground, ServantC67.Ground)
    if (sm > 0f) {
        val f = easeOut(seg(sm, 0f, 0.25f)) * (1f - seg(ast, 0f, BEAT_OUT))
        val light = Offset(w * 0.5f, h * 0.3f)
        sirajMunir(light, w * 0.014f, f, t, nameAt = light + Offset(0f, -h * 0.12f))
        fitBig26(a.big("servantmessenger"), Offset(w * 0.5f, h * 0.62f), w * 0.8f, ServantC67.Trim, f * seg(sm, 0.3f, 0.55f))
    }
    if (ast > 0f) {
        val f = easeOut(seg(ast, 0f, BEAT_IN)) * (1f - seg(hc, 0f, BEAT_OUT))
        for (k in 0 until 3) drawLine(ServantC67.Road, Offset(0f, h * (0.45f + 0.18f * k)), Offset(w, h * (0.45f + 0.18f * k)), h * 0.035f, alpha = f)
        for (k in 0 until 3) drawLine(ServantC67.Road, Offset(w * (0.2f + 0.3f * k), h * 0.35f), Offset(w * (0.2f + 0.3f * k), ground), w * 0.035f, alpha = f)
        for (k in 0 until 6) house12(Offset(w * (0.08f + 0.17f * k), h * 0.43f), w * 0.1f, ServantC67.Wall, ServantC67.Trim, f, 0.3f)
        val meet = easeInOut(seg(ast, 0.2f, 0.6f))
        val corner = Offset(w * 0.5f, h * 0.63f)
        nameRoundel(Offset(mix(w * 0.1f, corner.x - w * 0.05f, meet), corner.y), w * 0.03f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        sirajMunir(Offset(mix(w * 0.9f, corner.x + w * 0.05f, meet), corner.y - h * 0.02f), w * 0.009f, f, t, named = false)
        softGlow(corner, w * 0.1f, ServantC67.Gold, 0.4f * f * seg(ast, 0.55f, 0.75f))
        tagAt(a.tag("anystreet"), Offset(w * 0.5f, h * 0.2f), f * seg(ast, 0.4f, 0.6f), back = Back67, ink = ArtInk.Gold)
    }
    if (hc > 0f) {
        val f = easeOut(seg(hc, 0f, BEAT_IN)) * (1f - seg(sg, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val g = seg(hc, 0.08f + 0.27f * k, 0.25f + 0.27f * k)
            val c = Offset(w * (0.8f - 0.3f * k), ground - h * 0.12f)
            when (k) {
                0 -> { drawRoundRect(ServantC67.Cloth, Offset(c.x - w * 0.08f, c.y - h * 0.08f), Size(w * 0.16f, h * 0.14f), CornerRadius(4f * dp), alpha = f * g); val st = seg(hc, 0.1f, 0.3f); for (j in 0 until 5) if (j / 5f < st) drawLine(ServantC67.Thread, Offset(c.x - w * 0.05f + w * 0.025f * j, c.y), Offset(c.x - w * 0.04f + w * 0.025f * j, c.y + h * 0.015f), 2f * dp, alpha = f) }
                1 -> { sheep26(c + Offset(-w * 0.03f, 0f), w * 0.1f, ServantC67.Goat, f * g); drawRect(ServantC67.Pail, Offset(c.x + w * 0.04f, c.y + h * 0.02f), Size(w * 0.05f, h * 0.06f), alpha = f * g); drawRect(ServantC67.Milk, Offset(c.x + w * 0.045f, c.y + h * 0.08f - h * 0.05f * seg(hc, 0.4f, 0.6f)), Size(w * 0.04f, h * 0.05f * seg(hc, 0.4f, 0.6f)), alpha = f * g) }
                else -> { drawLine(ServantC67.Trim, c + Offset(w * 0.04f, -h * 0.1f), c + Offset(-w * 0.02f, h * 0.06f), 3f * dp, StrokeCap.Round, alpha = f * g); for (j in 0 until 5) drawLine(ServantC67.Worn, c + Offset(-w * 0.02f, h * 0.06f), c + Offset(-w * 0.05f + w * 0.015f * j, h * 0.1f), 2f * dp, alpha = f * g) }
            }
            tagAt(a.tag("homechores", 0, k), c + Offset(0f, -h * 0.2f), f * g, back = Back67, ink = ArtInk.Gold)
        }
    }
    if (sg > 0f) {
        val f = easeOut(seg(sg, 0f, BEAT_IN)) * (1f - seg(ps, 0f, BEAT_OUT))
        val light = Offset(w * 0.7f, h * 0.4f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        val go = easeInOut(seg(sg, 0.15f, 0.55f))
        val dish = Offset(mix(w * 0.2f, w * 0.6f, go), h * 0.62f)
        drawOval(ServantC67.Dish, Offset(dish.x - w * 0.06f, dish.y - h * 0.015f), Size(w * 0.12f, h * 0.03f), alpha = f)
        drawOval(ServantC67.Trim, Offset(dish.x - w * 0.02f, dish.y - h * 0.025f), Size(w * 0.04f, h * 0.015f), alpha = f)
        softGlow(dish, w * 0.08f, ServantC67.Gold, 0.5f * f * seg(sg, 0.55f, 0.8f))
        tagAt(a.tag("smallgift"), Offset(w * 0.45f, h * 0.18f), f * seg(sg, 0.4f, 0.6f), back = Back67, ink = ArtInk.Gold)
    }
    if (ps > 0f) {
        val f = easeOut(seg(ps, 0f, BEAT_IN)) * (1f - seg(wv, 0f, BEAT_OUT))
        val foot = Offset(w * 0.6f, ground)
        camel26(foot, h * 0.4f, ServantC67.Camel, f, t)
        drawRect(ServantC67.Worn, Offset(foot.x - w * 0.08f, foot.y - h * 0.4f), Size(w * 0.16f, h * 0.05f), alpha = f)
        for (k in 0 until 4) coin12(Offset(w * (0.12f + 0.05f * k), h * 0.55f), w * 0.018f, f * seg(ps, 0.2f + 0.05f * k, 0.3f + 0.05f * k), ServantC67.Coin)
        tagAt(a.tag("plainsaddle", 0, 0), Offset(w * 0.2f, h * 0.45f), f * seg(ps, 0.2f, 0.4f), back = Back67, ink = ArtInk.Gold)
        tagAt(a.tag("plainsaddle", 0, 1), Offset(w * 0.5f, h * 0.15f), f * seg(ps, 0.55f, 0.75f), back = Back67, ink = ArtInk.Gold)
    }
    if (wv > 0f) {
        val f = easeOut(seg(wv, 0f, 0.25f))
        val house = Offset(w * 0.2f, ground)
        house12(house, w * 0.26f, ServantC67.Wall, ServantC67.Trim, f, 0.6f)
        nameRoundel(house + Offset(0f, -h * 0.38f), w * 0.07f, a.big("walkingvisit"), Roundel.Fills[0], Roundel.Rim, f * seg(wv, 0.05f, 0.2f))
        val walk = seg(wv, 0.15f, 0.85f)
        for (k in 0 until 12) {
            val u = k / 12f
            if (u > walk) break
            val p = Offset(mix(w * 0.95f, w * 0.38f, u), ground + h * 0.06f + (if (k % 2 == 0) -h * 0.01f else h * 0.01f))
            drawOval(ServantC67.Step, Offset(p.x - w * 0.012f, p.y - h * 0.006f), Size(w * 0.024f, h * 0.012f), alpha = f)
        }
        softGlow(Offset(mix(w * 0.95f, w * 0.38f, walk), ground), w * 0.04f, ServantC67.Gold, f * seg(wv, 0.15f, 0.25f))
    }
}

// ═════════════ 3. his character (sira-khuluq) ═════════════

private object MannersC67 {
    val Night = Color(0xFF161A2C)
    val NightLow = Color(0xFF34324E)
    val Ground = Color(0xFF221E2C)
    val Gold = Color(0xFFF2C860)
    val Curtain = Color(0xFF6A4A6A)
    val Dark = Color(0xFF3A3040)
    val Bloom = Color(0xFFF2B8C8)
    val Steep = Color(0xFF8A7A6A)
    val Easy = Color(0xFF6FCB9F)
    val Bad = Color(0xFFD06A5A)
    val Gift = Color(0xFFB04A3A)
    val Ribbon = Color(0xFFE8C46A)
}

/**
 * Beats: neveruff (a roundel with his name, «أَنَسٌ»; ten years go round it as rings, with not one dark word among them: «10 سِنِينَ»),
 * shyerthan (a curtained alcove with a soft lamp: «أَشَدَّ حَيَاءً مِنَ الْعَذْرَاءِ فِي خِدْرِهَا»), pardonsback (a dark dart comes and
 * is met with a flower of light: «وَلَكِنْ يَعْفُو وَيَصْفَحُ»), chooseseasier (a steep path and an easier one; the easier is taken:
 * «إِلَّا اخْتَارَ أَيْسَرَهُمَا»), windgiving (gifts go out like a steady wind, more under the crescent of Ramadan: «أَجْوَدَ النَّاسِ
 * بِالْخَيْرِ», «فِي شَهْرِ رَمَضَانَ»), giftreturned (a gift comes in and a gift goes back).
 */
internal fun DrawScope.noblemannersScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "neveruff" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "shyerthan" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "pardonsback" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "chooseseasier" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "windgiving" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "giftreturned" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { mannersNight(a) }
}

private fun DrawScope.mannersNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val nu = a.b("neveruff")
    val st = a.b("shyerthan")
    val pb = a.b("pardonsback")
    val ce = a.b("chooseseasier")
    val wg = a.b("windgiving")
    val gr = a.b("giftreturned")
    val ground = h * 0.86f
    nightSky(t, MannersC67.Night, MannersC67.NightLow, ground, starAlpha = 0.85f)
    drawRect(MannersC67.Ground, Offset(0f, ground), Size(w, h - ground))
    if (nu > 0f) {
        val f = easeOut(seg(nu, 0f, BEAT_IN)) * (1f - seg(st, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.5f)
        nameRoundel(c, w * 0.09f, a.big("neveruff", 0, 0), Roundel.Fills[1], Roundel.Rim, f * seg(nu, 0.05f, 0.2f))
        for (k in 0 until 10) {
            val g = seg(nu, 0.2f + 0.05f * k, 0.3f + 0.05f * k)
            drawCircle(MannersC67.Gold, w * (0.12f + 0.022f * k), c, alpha = f * g * 0.5f, style = Stroke(1.5f * dp))
        }
        tagAt(a.tag("neveruff", 0, 1), Offset(w * 0.5f, h * 0.88f), f * seg(nu, 0.4f, 0.6f), back = Back67, ink = ArtInk.Gold)
    }
    if (st > 0f) {
        val f = easeOut(seg(st, 0f, BEAT_IN)) * (1f - seg(pb, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.55f)
        drawArc(MannersC67.Curtain, 180f, 180f, true, Offset(c.x - w * 0.22f, c.y - h * 0.25f), Size(w * 0.44f, h * 0.3f), alpha = f * 0.8f)
        drawRect(MannersC67.Curtain, Offset(c.x - w * 0.22f, c.y - h * 0.1f), Size(w * 0.44f, h * 0.3f), alpha = f * 0.8f)
        val open = 0.15f + 0.05f * sin(t)
        drawRect(MannersC67.Ground, Offset(c.x - w * 0.22f * open, c.y - h * 0.1f), Size(w * 0.44f * open, h * 0.3f), alpha = f)
        softGlow(c, w * 0.08f, MannersC67.Gold, 0.6f * f * seg(st, 0.2f, 0.4f))
        tagAt(a.tag("shyerthan"), Offset(w * 0.5f, h * 0.18f), f * seg(st, 0.3f, 0.5f), back = Back67, ink = ArtInk.Gold)
    }
    if (pb > 0f) {
        val f = easeOut(seg(pb, 0f, BEAT_IN)) * (1f - seg(ce, 0f, BEAT_OUT))
        val me = Offset(w * 0.3f, h * 0.55f)
        nameRoundel(me, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val dart = easeIn67(seg(pb, 0.1f, 0.4f))
        val dp0 = Offset(mix(w * 0.95f, me.x + w * 0.12f, dart), h * 0.55f)
        drawLine(MannersC67.Dark, dp0, dp0 + Offset(w * 0.06f, 0f), 4f * dp, StrokeCap.Round, alpha = f * (1f - seg(pb, 0.4f, 0.5f)))
        val bloom = seg(pb, 0.45f, 0.75f)
        for (k in 0 until 6) {
            val ang = k * PI.toFloat() / 3f + t * 0.3f
            drawCircle(MannersC67.Bloom, w * 0.025f * bloom, me + Offset(w * 0.14f, 0f) + Offset(cos(ang), sin(ang)) * (w * 0.03f * bloom), alpha = f * bloom)
        }
        softGlow(me + Offset(w * 0.14f, 0f), w * 0.06f, MannersC67.Gold, f * bloom)
        tagAt(a.tag("pardonsback"), Offset(w * 0.5f, h * 0.22f), f * seg(pb, 0.5f, 0.7f), back = Back67, ink = ArtInk.Gold)
    }
    if (ce > 0f) {
        val f = easeOut(seg(ce, 0f, BEAT_IN)) * (1f - seg(wg, 0f, BEAT_OUT))
        val start = Offset(w * 0.5f, ground)
        drawPath(Path().apply { moveTo(start.x, start.y); lineTo(w * 0.7f, h * 0.6f); lineTo(w * 0.62f, h * 0.45f); lineTo(w * 0.85f, h * 0.25f) }, MannersC67.Steep, alpha = f, style = Stroke(h * 0.018f, cap = StrokeCap.Round))
        drawPath(Path().apply { moveTo(start.x, start.y); quadraticTo(w * 0.25f, h * 0.6f, w * 0.15f, h * 0.25f) }, MannersC67.Easy, alpha = f, style = Stroke(h * 0.018f, cap = StrokeCap.Round))
        val u = easeInOut(seg(ce, 0.3f, 0.85f))
        val p = Offset(mix(start.x, w * 0.15f, u) - w * 0.08f * sin(PI.toFloat() * u), mix(start.y, h * 0.25f, u))
        softGlow(p, w * 0.04f, MannersC67.Gold, f * seg(ce, 0.25f, 0.35f))
        tagAt(a.tag("chooseseasier"), Offset(w * 0.5f, h * 0.12f), f * seg(ce, 0.2f, 0.4f), back = Back67, ink = ArtInk.Gold)
    }
    if (wg > 0f) {
        val f = easeOut(seg(wg, 0f, BEAT_IN)) * (1f - seg(gr, 0f, BEAT_OUT))
        val src = Offset(w * 0.85f, h * 0.55f)
        softGlow(src, w * 0.08f, MannersC67.Gold, 0.7f * f)
        val ramadan = seg(wg, 0.5f, 0.7f)
        if (ramadan > 0f) crescent12(Offset(w * 0.8f, h * 0.18f), w * 0.04f, MannersC67.Gold, MannersC67.Night, f * ramadan)
        val n = 8 + (10 * ramadan).toInt()
        for (k in 0 until n) {
            val u = fract(t * (0.35f + 0.25f * ramadan) + k / n.toFloat())
            val c = src + Offset(-w * 0.75f * u, sin(u * 8f + k) * h * 0.05f + h * 0.12f * ((k % 3) - 1))
            drawRect(MannersC67.Gift, Offset(c.x - w * 0.01f, c.y - w * 0.01f), Size(w * 0.02f, w * 0.02f), alpha = f * (1f - u) * seg(wg, 0.1f, 0.25f))
        }
        tagAt(a.tag("windgiving", 0, 0), Offset(w * 0.4f, h * 0.3f), f * seg(wg, 0.15f, 0.35f), back = Back67, ink = ArtInk.Gold)
        tagAt(a.tag("windgiving", 0, 1), Offset(w * 0.4f, h * 0.8f), f * ramadan, back = Back67, ink = ArtInk.Gold)
    }
    if (gr > 0f) {
        val f = easeOut(seg(gr, 0f, 0.25f))
        val l = Offset(w * 0.2f, h * 0.55f)
        val r = Offset(w * 0.8f, h * 0.55f)
        nameRoundel(l, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        softGlow(r, w * 0.08f, MannersC67.Gold, 0.7f * f)
        for (k in 0 until 2) {
            val u = easeInOut(seg(gr, 0.1f + 0.4f * k, 0.45f + 0.4f * k))
            val from = if (k == 0) l else r
            val to = if (k == 0) r else l
            val c = from + (to - from) * u + Offset(0f, -h * 0.1f * sin(PI.toFloat() * u))
            drawRect(MannersC67.Gift, Offset(c.x - w * 0.03f, c.y - w * 0.03f), Size(w * 0.06f, w * 0.06f), alpha = f * seg(gr, 0.1f + 0.4f * k, 0.15f + 0.4f * k))
            drawLine(MannersC67.Ribbon, Offset(c.x, c.y - w * 0.03f), Offset(c.x, c.y + w * 0.03f), 2f * dp, alpha = f * seg(gr, 0.1f + 0.4f * k, 0.15f + 0.4f * k))
        }
    }
}

private fun easeIn67(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ═════════════ 4. the last day (sira-wafat) ═════════════

private object LastC67 {
    val Night = Color(0xFF0E1226)
    val NightLow = Color(0xFF2A2C4C)
    val Ground = Color(0xFF181A28)
    val Gold = Color(0xFFF2C860)
    val Curtain = Color(0xFF5A4A6A)
    val Page = Color(0xFFF6F0E1)
    val House = Color(0xFFC8B494)
    val Trim = Color(0xFF6A5040)
    val DawnTop = Color(0xFF6A7AA8)
    val DawnLow = Color(0xFFF2D8B0)
}

/**
 * Beats: curtainlift (a curtain is drawn aside and the light shines like a page of the Mushaf, «كَأَنَّهُ وَرَقَةُ مُصْحَفٍ», over rows of
 * roundels praying behind a roundel with his name, «أَبِي بَكْرٍ»), noagony (a roundel with her name, «فَاطِمَةُ», and the words: «لَا كَرْبَ
 * عَلَى أَبِيكَ بَعْدَ الْيَوْمِ»), mondayrest (a roundel with her name, «عَائِشَةُ», and beside it the light, which rises softly away:
 * «يَوْمَ الِاثْنَيْنِ»), zumarverse (a roundel with his name, «أَبُو بَكْرٍ», and a scroll; the roundels round him understand: «آيَةَ
 * الزُّمَرِ»), citylitdark (the town lights up from end to end as the light enters it, and on the day he died every light goes
 * dim: «أَضَاءَ مِنْهَا كُلُّ شَيْءٍ», «أَظْلَمَ مِنْهَا كُلُّ شَيْءٍ»).
 */
internal fun DrawScope.lastdayScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "curtainlift" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "noagony" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "mondayrest" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "zumarverse" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "citylitdark" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { lastNight(a) }
}

private fun DrawScope.lastNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cl = a.b("curtainlift")
    val na = a.b("noagony")
    val mr = a.b("mondayrest")
    val zv = a.b("zumarverse")
    val cd = a.b("citylitdark")
    val ground = h * 0.86f
    val dawn = seg(cl, 0f, 0.3f) * (1f - seg(na, 0f, BEAT_OUT))
    nightSky(t, lerpColor(LastC67.Night, LastC67.DawnTop, dawn * 0.6f), lerpColor(LastC67.NightLow, LastC67.DawnLow, dawn * 0.6f), ground, starAlpha = 0.6f * (1f - dawn))
    drawRect(LastC67.Ground, Offset(0f, ground), Size(w, h - ground))
    if (cl > 0f) {
        val f = easeOut(seg(cl, 0f, BEAT_IN)) * (1f - seg(na, 0f, BEAT_OUT))
        val win = Offset(w * 0.8f, h * 0.3f)
        val open = easeInOut(seg(cl, 0.15f, 0.4f))
        drawRect(LastC67.Page, Offset(win.x - w * 0.1f, win.y - h * 0.12f), Size(w * 0.2f, h * 0.24f), alpha = f * 0.25f * open)
        sirajMunir(win, w * 0.012f, f * open, t, nameAt = win + Offset(0f, -h * 0.17f))
        drawRect(LastC67.Curtain, Offset(win.x - w * 0.1f, win.y - h * 0.12f), Size(w * 0.2f * (1f - 0.8f * open), h * 0.24f), alpha = f)
        nameRoundel(Offset(w * 0.4f, h * 0.48f), w * 0.06f, a.big("curtainlift", 0, 1), Roundel.Fills[0], Roundel.Rim, f * seg(cl, 0.05f, 0.2f))
        for (r in 0 until 2) for (k in 0 until 8) nameRoundel(Offset(w * (0.1f + 0.1f * k), h * (0.66f + 0.1f * r)), w * 0.022f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        tagAt(a.tag("curtainlift", 0, 0), Offset(w * 0.5f, h * 0.12f), f * seg(cl, 0.45f, 0.65f), back = Back67, ink = ArtInk.Gold)
    }
    if (na > 0f) {
        val f = easeOut(seg(na, 0f, BEAT_IN)) * (1f - seg(mr, 0f, BEAT_OUT))
        val light = Offset(w * 0.7f, h * 0.38f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        nameRoundel(Offset(w * 0.3f, h * 0.45f), w * 0.09f, a.big("noagony", 0, 0), Roundel.Fills[2], Roundel.Rim, f * seg(na, 0.05f, 0.2f))
        tagAt(a.tag("noagony", 0, 1), Offset(w * 0.5f, h * 0.75f), f * seg(na, 0.4f, 0.6f), back = Back67, ink = ArtInk.Gold)
    }
    if (mr > 0f) {
        val f = easeOut(seg(mr, 0f, BEAT_IN)) * (1f - seg(zv, 0f, BEAT_OUT))
        nameRoundel(Offset(w * 0.35f, h * 0.6f), w * 0.09f, a.big("mondayrest", 0, 1), Roundel.Fills[1], Roundel.Rim, f * seg(mr, 0.05f, 0.2f))
        val rise = easeInOut(seg(mr, 0.35f, 0.9f))
        val light = Offset(w * 0.58f, mix(h * 0.55f, h * 0.12f, rise))
        sirajMunir(light, w * 0.012f, f * (1f - 0.3f * rise), t, nameAt = light + Offset(0f, -h * 0.1f))
        tagAt(a.tag("mondayrest", 0, 0), Offset(w * 0.5f, h * 0.86f), f * seg(mr, 0.15f, 0.35f), back = Back67, ink = ArtInk.Gold)
    }
    if (zv > 0f) {
        val f = easeOut(seg(zv, 0f, BEAT_IN)) * (1f - seg(cd, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.42f)
        nameRoundel(c, w * 0.09f, a.big("zumarverse", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(zv, 0.05f, 0.2f))
        scroll14(c + Offset(0f, h * 0.2f), w * 0.3f, h * 0.12f, easeOut(seg(zv, 0.15f, 0.4f)), f)
        tagAt(a.tag("zumarverse", 0, 1), c + Offset(0f, h * 0.33f), f * seg(zv, 0.3f, 0.5f), back = Back67, ink = ArtInk.Gold)
        for (k in 0 until 10) {
            val ang = PI.toFloat() * (0.05f + 0.9f * k / 9f)
            val p = c + Offset(cos(ang) * w * 0.4f, -sin(ang) * h * 0.25f + h * 0.05f)
            nameRoundel(p, w * 0.022f, null, Roundel.PlainFill, Roundel.PlainRim, f)
            softGlow(p, w * 0.03f, LastC67.Gold, 0.6f * f * seg(zv, 0.55f + 0.03f * k, 0.7f + 0.03f * k))
        }
    }
    if (cd > 0f) {
        val f = easeOut(seg(cd, 0f, 0.25f))
        val lit = seg(cd, 0.1f, 0.4f) * (1f - seg(cd, 0.55f, 0.85f))
        for (k in 0 until 8) {
            val foot = Offset(w * (0.06f + 0.125f * k), ground)
            house12(foot, w * (0.1f + 0.02f * (k % 3)), LastC67.House, LastC67.Trim, f, lit)
            softGlow(foot + Offset(0f, -h * 0.08f), w * 0.06f, LastC67.Gold, 0.5f * f * lit)
        }
        tagAt(a.tag("citylitdark", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(cd, 0.2f, 0.35f) * (1f - seg(cd, 0.5f, 0.6f)), back = Back67, ink = ArtInk.Gold)
        tagAt(a.tag("citylitdark", 0, 1), Offset(w * 0.5f, h * 0.3f), f * seg(cd, 0.6f, 0.75f), back = Back67)
    }
}
