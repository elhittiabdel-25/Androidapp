package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 71 (2026-11-03): v1.3 fiqh, «الأضحية والذكاة» (Ibn ʿUthaymīn). What the offering is; its conditions; the best of them and the
 * disliked; whom one sheep covers; eating, giving and the thirds; what the one who offers keeps from. The animals are always shown
 * alive and whole: no blade, no blood. People are faceless figures.
 */

private object UdhC71 {
    val Sky = Color(0xFF14183A)
    val SkyLow = Color(0xFF4A3E6A)
    val Ground = Color(0xFF2A2430)
    val DayTop = Color(0xFF9CC8E6)
    val DayLow = Color(0xFFF4E6C8)
    val Grass = Color(0xFF8AA868)
    val Fleece = Color(0xFFF0EAE0)
    val Goat = Color(0xFF6A5A4A)
    val Camel = Color(0xFFC8A880)
    val Cow = Color(0xFF8A6A50)
    val Lit = Color(0xFFF2C860)
    val Red = Color(0xFFC0503A)
    val Amber = Color(0xFFE0A040)
    val Person = Color(0xFFE8DCC4)
    val Meat = Color(0xFFC88A7A)
}

private fun DrawScope.udhDay(ground: Float) = daySky12(UdhC71.DayTop, UdhC71.DayLow, ground, UdhC71.Grass)

// ═════════════ 13. udh-hukm: what the offering is ═════════════

/**
 * Beats: adhadef (camel, cow and sheep on the eid morning, and a light rising from them: «تَقَرُّبًا إِلَى اللَّهِ»), twokabsh (two
 * horned rams, white flecked with black: «بِكَبْشَيْنِ أَمْلَحَيْنِ أَقْرَنَيْنِ»), sunnahor (two views side by side on a level balance:
 * «سُنَّةٌ مُؤَكَّدَةٌ», «وُجُوبِهَا»), betterthanprice (its price in coins beside the ram, and the ram shines brighter: «مِنْ شَعَائِرِ
 * اللَّهِ»), familyall (one offering, its light reaching a lit house and the stars above it: «الْأَحْيَاءَ وَالْأَمْوَاتَ»).
 */
internal fun DrawScope.eidofferingScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "adhadef" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "twokabsh" to Shot(Offset(w * 0.5f, h * 0.58f), 1.15f),
        "sunnahor" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "betterthanprice" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "familyall" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { eidOfferingDraw(a) }
}

private fun DrawScope.eidOfferingDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ground = h * 0.78f
    val fam = a.b("familyall")
    val night = seg(fam, 0f, 0.4f)
    udhDay(ground)
    if (night > 0f) {
        drawRect(UdhC71.Sky, Offset.Zero, Size(w, ground), alpha = night)
        nightSky(t, UdhC71.Sky, UdhC71.SkyLow, ground, starAlpha = night)
        drawRect(UdhC71.Ground, Offset(0f, ground), Size(w, h - ground), alpha = night)
    }
    val p1 = fqIn(a, "adhadef", "twokabsh")
    if (p1 > 0f) {
        val pb = a.b("adhadef")
        camel26(Offset(w * 0.78f, ground + h * 0.04f), h * 0.36f, UdhC71.Camel, p1 * seg(pb, 0.05f, 0.2f), t)
        fqCow(Offset(w * 0.5f, ground + h * 0.06f), h * 0.3f, UdhC71.Cow, p1 * seg(pb, 0.12f, 0.27f))
        sheep26(Offset(w * 0.24f, ground - h * 0.02f), h * 0.18f, UdhC71.Fleece, p1 * seg(pb, 0.2f, 0.35f))
        fqEidMoon(Offset(w * 0.15f, h * 0.15f), w * 0.035f, UdhC71.DayTop, p1 * 0.9f)
        val up = seg(pb, 0.45f, 0.95f)
        softGlow(Offset(w * 0.5f, mix(ground - h * 0.2f, h * 0.08f, easeOut(up))), w * 0.05f, UdhC71.Lit, p1 * bump(up))
        tagAt(a.tag("adhadef"), Offset(w * 0.55f, h * 0.18f), p1 * seg(pb, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p2 = fqIn(a, "twokabsh", "sunnahor")
    if (p2 > 0f) {
        val pb = a.b("twokabsh")
        for (k in 0 until 2) {
            val g = seg(pb, 0.1f + 0.2f * k, 0.3f + 0.2f * k)
            fqRam(Offset(w * (0.66f - 0.3f * k), ground - h * 0.06f), h * 0.3f, UdhC71.Fleece, p2 * g, amlah = true)
        }
        tagAt(a.tag("twokabsh"), Offset(w * 0.5f, h * 0.18f), p2 * seg(pb, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p3 = fqIn(a, "sunnahor", "betterthanprice")
    if (p3 > 0f) {
        val pb = a.b("sunnahor")
        balance12(Offset(w * 0.5f, ground + h * 0.02f), h * 0.5f, 0f, Fq.Brass, p3)
        tagAt(a.tag("sunnahor", 0, 0), Offset(w * 0.73f, h * 0.2f), p3 * seg(pb, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Jade)
        tagAt(a.tag("sunnahor", 0, 1), Offset(w * 0.27f, h * 0.2f), p3 * seg(pb, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p4 = fqIn(a, "betterthanprice", "familyall")
    if (p4 > 0f) {
        val pb = a.b("betterthanprice")
        fqCoins(Offset(w * 0.25f, ground), w * 0.05f, 6, p4)
        val shine = seg(pb, 0.3f, 0.6f)
        softGlow(Offset(w * 0.65f, ground - h * 0.12f), w * (0.12f + 0.08f * shine), UdhC71.Lit, p4 * (0.25f + 0.45f * shine))
        fqRam(Offset(w * 0.65f, ground - h * 0.08f), h * 0.32f, UdhC71.Fleece, p4)
        drawPath(Path().apply { moveTo(w * 0.42f, h * 0.5f); lineTo(w * 0.46f, h * 0.46f); lineTo(w * 0.46f, h * 0.54f); close() }, UdhC71.Lit, alpha = p4 * shine)
        tagAt(a.tag("betterthanprice"), Offset(w * 0.5f, h * 0.15f), p4 * seg(pb, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p5 = fqIn(a, "familyall")
    if (p5 > 0f) {
        val pb = a.b("familyall")
        val house = Offset(w * 0.3f, ground)
        house12(house, w * 0.22f, Color(0xFFD8C4A0), Color(0xFF8A6A48), p5, lit = seg(pb, 0.4f, 0.6f))
        fqRam(Offset(w * 0.72f, ground - h * 0.07f), h * 0.28f, UdhC71.Fleece, p5)
        val arc = seg(pb, 0.3f, 0.6f)
        for (k in 0 until 3) {
            val target = if (k == 0) house + Offset(0f, -h * 0.2f) else Offset(w * (0.2f + 0.2f * k), h * (0.15f + 0.05f * k))
            val from = Offset(w * 0.72f, ground - h * 0.2f)
            val u = arc
            val pnt = Offset(mix(from.x, target.x, u), mix(from.y, target.y, u) - sin(PI.toFloat() * u) * h * 0.12f)
            softGlow(pnt, w * 0.02f, UdhC71.Lit, p5 * bump(u) + p5 * (if (u >= 1f) 0.6f else 0f))
            if (k > 0) drawPath(starPath(target, w * 0.012f), UdhC71.Lit, alpha = p5 * seg(pb, 0.55f, 0.7f))
        }
        tagAt(a.tag("familyall"), Offset(w * 0.5f, h * 0.88f), p5 * seg(pb, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 14. udh-shurut: the conditions ═════════════

/**
 * Beats: anam (the three kinds of livestock come in: «الْإِبِلُ», «وَالْبَقَرُ», «وَالْغَنَمُ»), agecheck (each with its count of years in
 * pips, five, two, one, a half: «السِّنَّ الْمَحْدُودَةَ شَرْعًا»), fourdefects (four marks, each crossed: «الْعَوَرُ الْبَيِّنُ», «وَالْمَرَضُ
 * الْبَيِّنُ», «وَالْعَرَجُ الْبَيِّنُ», «وَالْهُزَالُ»), timewindow (four days on a strip, opening after the eid prayer and closing at the
 * last sunset: «بَعْدِ صَلَاةِ الْعِيدِ», «غُرُوبِ الشَّمْسِ مِنْ آخِرِ أَيَّامِ التَّشْرِيقِ»), owned (his own sheep in its pen with a tick, and
 * one led off by a shadow, crossed: «مِلْكًا لِلْمُضَحِّي», «فَلَا تَصِحُّ بِالْمَغْصُوبِ وَالْمَسْرُوقِ»).
 */
internal fun DrawScope.fourconditionsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "anam" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "agecheck" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "fourdefects" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "timewindow" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "owned" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { fourConditionsDraw(a) }
}

private fun DrawScope.fourConditionsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val ground = h * 0.78f
    udhDay(ground)
    val an = a.b("anam")
    val ag = a.b("agecheck")
    val p1 = fqIn(a, "anam", "fourdefects")
    if (p1 > 0f) {
        val xs = listOf(0.8f, 0.52f, 0.22f)
        camel26(Offset(w * xs[0], ground + h * 0.04f), h * 0.34f, UdhC71.Camel, p1 * seg(an, 0.05f, 0.2f), t)
        fqCow(Offset(w * xs[1], ground + h * 0.05f), h * 0.28f, UdhC71.Cow, p1 * seg(an, 0.3f, 0.45f))
        sheep26(Offset(w * (xs[2] + 0.06f), ground - h * 0.03f), h * 0.15f, UdhC71.Fleece, p1 * seg(an, 0.55f, 0.7f))
        sheep26(Offset(w * (xs[2] - 0.07f), ground - h * 0.02f), h * 0.14f, UdhC71.Goat, p1 * seg(an, 0.6f, 0.75f))
        for (k in 0 until 3) tagAt(a.tag("anam", 0, k), Offset(w * xs[k], h * 0.88f), p1 * seg(an, 0.05f + 0.25f * k, 0.2f + 0.25f * k), back = Fq.Back, ink = ArtInk.Ivory)
        if (ag > 0f) {
            val years = listOf(5f, 2f, 1f, 0.5f)
            val at = listOf(w * 0.8f, w * 0.52f, w * 0.28f, w * 0.15f)
            for (k in 0 until 4) {
                val g = seg(ag, 0.1f + 0.15f * k, 0.25f + 0.15f * k)
                val n = kotlin.math.ceil(years[k]).toInt()
                for (i in 0 until n) {
                    val c = Offset(at[k] + (i - (n - 1) * 0.5f) * w * 0.028f, h * 0.3f)
                    if (years[k] < 1f) drawArc(UdhC71.Lit, 90f, 180f, true, c - Offset(w * 0.01f, w * 0.01f), Size(w * 0.02f, w * 0.02f), alpha = p1 * g)
                    else drawCircle(UdhC71.Lit, w * 0.01f, c, alpha = p1 * g)
                    drawCircle(UdhC71.Lit, w * 0.01f, c, alpha = p1 * g * 0.6f, style = Stroke(1.2f * dp))
                }
            }
            tagAt(a.tag("agecheck"), Offset(w * 0.5f, h * 0.15f), p1 * seg(ag, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    val p3 = fqIn(a, "fourdefects", "timewindow")
    if (p3 > 0f) {
        val pb = a.b("fourdefects")
        for (k in 0 until 4) {
            val g = seg(pb, 0.05f + 0.2f * k, 0.18f + 0.2f * k)
            val c = Offset(w * (0.83f - 0.22f * k), h * 0.45f)
            drawCircle(Color.White, w * 0.08f, c, alpha = p3 * g * 0.5f)
            when (k) {
                0 -> { drawOval(Color(0xFF3A3040), Offset(c.x - w * 0.045f, c.y - h * 0.03f), Size(w * 0.09f, h * 0.06f), alpha = p3 * g, style = Stroke(3f * dp)); drawCircle(Color(0xFF3A3040), w * 0.012f, c, alpha = p3 * g) }
                1 -> for (i in 0 until 3) drawLine(Color(0xFF3A3040), c + Offset(-w * 0.04f + i * w * 0.03f, -h * 0.03f), c + Offset(-w * 0.025f + i * w * 0.03f, h * 0.03f), 3f * dp, StrokeCap.Round, alpha = p3 * g)
                2 -> { drawLine(Color(0xFF3A3040), c + Offset(0f, -h * 0.05f), c + Offset(w * 0.01f, 0f), 4f * dp, StrokeCap.Round, alpha = p3 * g); drawLine(Color(0xFF3A3040), c + Offset(w * 0.01f, 0f), c + Offset(-w * 0.02f, h * 0.05f), 4f * dp, StrokeCap.Round, alpha = p3 * g) }
                else -> drawOval(Color(0xFF3A3040), Offset(c.x - w * 0.05f, c.y - h * 0.02f), Size(w * 0.1f, h * 0.04f), alpha = p3 * g, style = Stroke(2f * dp))
            }
            cross14(c, w * 0.07f, seg(pb, 0.12f + 0.2f * k, 0.22f + 0.2f * k))
            tagAt(a.tag("fourdefects", 0, k), c + Offset(0f, h * 0.17f), p3 * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    val p4 = fqIn(a, "timewindow", "owned")
    if (p4 > 0f) {
        val pb = a.b("timewindow")
        val y = h * 0.48f
        val x0 = w * 0.85f
        val x1 = w * 0.15f
        domed29(Offset(x0 + w * 0.04f, y - h * 0.06f), w * 0.07f, Color(0xFFD8CCB4), Color(0xFF4E7A6A), p4 * seg(pb, 0.05f, 0.15f))
        val fill = easeOut(seg(pb, 0.15f, 0.75f))
        for (d in 0 until 4) {
            val l = x0 - (x0 - x1) * d / 4f
            val r = x0 - (x0 - x1) * (d + 1) / 4f
            drawRoundRect(UdhC71.Lit, Offset(r + w * 0.005f, y), Size(l - r - w * 0.01f, h * 0.06f), CornerRadius(h * 0.02f), alpha = p4 * (0.25f + 0.75f * seg(fill * 4f, d.toFloat(), d + 1f)))
            sun12(Offset((l + r) * 0.5f, y + h * 0.15f), w * 0.02f, p4 * 0.8f)
        }
        sun12(Offset(x1 - w * 0.01f, y + h * 0.04f), w * 0.03f, p4 * seg(pb, 0.75f, 0.85f))
        drawRect(UdhC71.Red, Offset(x1 - w * 0.06f, y - h * 0.02f), Size(w * 0.01f, h * 0.1f), alpha = p4 * seg(pb, 0.8f, 0.9f))
        tagAt(a.tag("timewindow", 0, 0), Offset(w * 0.75f, h * 0.25f), p4 * seg(pb, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Jade)
        tagAt(a.tag("timewindow", 0, 1), Offset(w * 0.32f, h * 0.75f), p4 * seg(pb, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ember)
    }
    val p5 = fqIn(a, "owned")
    if (p5 > 0f) {
        val pb = a.b("owned")
        val pen = Offset(w * 0.72f, ground)
        for (k in 0 until 5) drawLine(Color(0xFF8A6A48), pen + Offset(-w * 0.13f + k * w * 0.065f, 0f), pen + Offset(-w * 0.13f + k * w * 0.065f, -h * 0.12f), 3f * dp, alpha = p5)
        drawLine(Color(0xFF8A6A48), pen + Offset(-w * 0.13f, -h * 0.09f), pen + Offset(w * 0.13f, -h * 0.09f), 3f * dp, alpha = p5)
        sheep26(pen + Offset(0f, -h * 0.07f), h * 0.15f, UdhC71.Fleece, p5)
        tick14(pen + Offset(0f, -h * 0.25f), w * 0.04f, seg(pb, 0.15f, 0.3f))
        tagAt(a.tag("owned", 0, 0), pen + Offset(0f, -h * 0.38f), p5 * seg(pb, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Jade)
        val g = seg(pb, 0.45f, 0.6f)
        val lead = easeInOut(seg(pb, 0.5f, 0.9f))
        val sh = Offset(w * 0.3f - w * 0.1f * lead, ground - h * 0.06f)
        sheep26(sh, h * 0.15f, UdhC71.Fleece, p5 * g)
        figure(sh + Offset(-w * 0.12f, h * 0.06f), h * 0.3f, Color(0xFF2A2430), p5 * g * 0.85f, walk = t * 8f, arms = -0.4f)
        drawLine(Color(0xFF5A4A3A), sh + Offset(-h * 0.1f, -h * 0.02f), sh + Offset(-w * 0.1f, -h * 0.08f), 1.5f * dp, alpha = p5 * g)
        cross14(sh + Offset(-w * 0.05f, -h * 0.2f), w * 0.06f, seg(pb, 0.6f, 0.8f))
        tagAt(a.tag("owned", 0, 1), Offset(w * 0.28f, h * 0.2f), p5 * seg(pb, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Ember)
    }
}

// ═════════════ 15. udh-afdal: the best, and the disliked ═════════════

/**
 * Beats: rankkinds (four steps, highest first: «الْإِبِلُ», «ثُمَّ الْبَقَرُ», «ثُمَّ الضَّأْنُ», «ثُمَّ الْمَعْزُ»), bestshape (a ram fills
 * out and shines: «الْأَسْمَنُ», «الْأَكْمَلُ خِلْقَةً», «الْأَحْسَنُ مَنْظَرًا»), amlah (close on a fleece white mixed with black, horns
 * curling: «أَقْرَنَيْنِ», «أَمْلَحَيْنِ»), disliked (an ear notched, ringed in amber, not crossed: «الْعَضْبَاءُ», «وَمَشْقُوقَةُ الْأُذُنِ»),
 * fourteen (fourteen marks, four red and ten amber: «أَرْبَعَ عَشْرَةَ»).
 */
internal fun DrawScope.bestofferingScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rankkinds" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "bestshape" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "amlah" to Shot(Offset(w * 0.5f, h * 0.55f), 1.3f),
        "disliked" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "fourteen" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { bestOfferingDraw(a) }
}

private fun DrawScope.bestOfferingDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val ground = h * 0.8f
    udhDay(ground)
    val p1 = fqIn(a, "rankkinds", "bestshape")
    if (p1 > 0f) {
        val pb = a.b("rankkinds")
        for (k in 0 until 4) {
            val g = seg(pb, 0.05f + 0.2f * k, 0.2f + 0.2f * k)
            val top = ground - h * (0.32f - 0.07f * k)
            val x = w * (0.8f - 0.2f * k)
            drawRect(Color(0xFFD8CCB4), Offset(x - w * 0.09f, top), Size(w * 0.18f, ground - top), alpha = p1 * g)
            when (k) {
                0 -> camel26(Offset(x, top), h * 0.26f, UdhC71.Camel, p1 * g, t)
                1 -> fqCow(Offset(x, top), h * 0.22f, UdhC71.Cow, p1 * g)
                2 -> sheep26(Offset(x, top - h * 0.06f), h * 0.14f, UdhC71.Fleece, p1 * g)
                else -> sheep26(Offset(x, top - h * 0.06f), h * 0.13f, UdhC71.Goat, p1 * g)
            }
            tagAt(a.tag("rankkinds", 0, k), Offset(x, ground + h * 0.08f), p1 * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    val p2 = fqIn(a, "bestshape", "amlah")
    if (p2 > 0f) {
        val pb = a.b("bestshape")
        val grow = easeOut(seg(pb, 0.05f, 0.4f))
        softGlow(Offset(w * 0.5f, ground - h * 0.15f), w * 0.2f, UdhC71.Lit, p2 * seg(pb, 0.6f, 0.85f) * 0.5f)
        fqRam(Offset(w * 0.5f, ground - h * 0.12f), h * (0.3f + 0.12f * grow), UdhC71.Fleece, p2)
        for (k in 0 until 3) tagAt(a.tag("bestshape", 0, k), Offset(w * (0.8f - 0.3f * k), h * (0.18f + 0.08f * (k % 2))), p2 * seg(pb, 0.1f + 0.25f * k, 0.25f + 0.25f * k), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p3 = fqIn(a, "amlah", "disliked")
    if (p3 > 0f) {
        val pb = a.b("amlah")
        for (k in 0 until 2) fqRam(Offset(w * (0.68f - 0.32f * k), ground - h * 0.1f), h * 0.34f, UdhC71.Fleece, p3 * seg(pb, 0.05f + 0.15f * k, 0.2f + 0.15f * k), amlah = true)
        tagAt(a.tag("amlah", 0, 0), Offset(w * 0.68f, h * 0.2f), p3 * seg(pb, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("amlah", 0, 1), Offset(w * 0.32f, h * 0.2f), p3 * seg(pb, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p4 = fqIn(a, "disliked", "fourteen")
    if (p4 > 0f) {
        val pb = a.b("disliked")
        for (k in 0 until 2) {
            val c = Offset(w * (0.72f - 0.42f * k), h * 0.48f)
            val g = seg(pb, 0.05f + 0.35f * k, 0.2f + 0.35f * k)
            drawOval(Color(0xFFE8C8B0), Offset(c.x - w * 0.09f, c.y - h * 0.05f), Size(w * 0.18f, h * 0.1f), alpha = p4 * g)
            if (k == 0) drawPath(Path().apply { moveTo(c.x - w * 0.09f, c.y - h * 0.05f); lineTo(c.x - w * 0.01f, c.y); lineTo(c.x - w * 0.09f, c.y + h * 0.05f); close() }, UdhC71.DayLow, alpha = p4 * g)
            else drawLine(UdhC71.DayLow, c + Offset(-w * 0.09f, 0f), c + Offset(-w * 0.02f, 0f), 4f * dp, alpha = p4 * g)
            drawCircle(UdhC71.Amber, w * 0.12f, c, alpha = p4 * seg(pb, 0.15f + 0.35f * k, 0.3f + 0.35f * k), style = Stroke(4f * dp))
            tagAt(a.tag("disliked", 0, k), c + Offset(0f, h * 0.22f), p4 * g, back = Fq.Back, ink = ArtInk.Ember)
        }
    }
    val p5 = fqIn(a, "fourteen")
    if (p5 > 0f) {
        val pb = a.b("fourteen")
        for (k in 0 until 14) {
            val g = seg(pb, 0.03f + 0.04f * k, 0.1f + 0.04f * k)
            val c = Offset(w * (0.83f - 0.11f * (k % 7)), h * (0.36f + 0.18f * (k / 7)))
            val col = if (k < 4) UdhC71.Red else UdhC71.Amber
            drawCircle(col, w * 0.035f, c, alpha = p5 * g)
            sheep26(c + Offset(w * 0.005f, 0f), w * 0.04f, Color.White, p5 * g)
        }
        tagAt(a.tag("fourteen"), Offset(w * 0.5f, h * 0.16f), p5 * seg(pb, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 16. udh-tujzi: whom one offering covers ═════════════

/**
 * Beats: onesheep (one sheep, and its light spreads over a whole house: «عَنِ الرَّجُلِ وَأَهْلِ بَيْتِهِ»), seventh (a camel and a cow cut
 * into seven shares, each share matching one sheep below: «سُبْعُ الْبَعِيرِ», «سُبْعُ الْبَقَرَةِ»), notshared (two people holding one
 * sheep between them, crossed: «لِأَنَّ الْعِبَادَاتِ تَوْقِيفِيَّةٌ»), notmorethan7 (seven roundels round a camel, an eighth crossed:
 * «ثَمَانِيَةٌ فَأَكْثَرُ»).
 */
internal fun DrawScope.whocoversScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "onesheep" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "seventh" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "notshared" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "notmorethan7" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { whoCoversDraw(a) }
}

private fun DrawScope.whoCoversDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val ground = h * 0.78f
    udhDay(ground)
    val p1 = fqIn(a, "onesheep", "seventh")
    if (p1 > 0f) {
        val pb = a.b("onesheep")
        val house = Offset(w * 0.32f, ground)
        house12(house, w * 0.3f, Color(0xFFD8C4A0), Color(0xFF8A6A48), p1, lit = seg(pb, 0.35f, 0.6f))
        sheep26(Offset(w * 0.75f, ground - h * 0.06f), h * 0.2f, UdhC71.Fleece, p1)
        val spread = easeOut(seg(pb, 0.2f, 0.6f))
        drawArc(UdhC71.Lit, 180f, 180f, false, Offset(mix(w * 0.75f, w * 0.1f, spread), ground - h * 0.5f), Size(mix(0f, w * 0.75f, spread), h * 0.9f), alpha = p1 * 0.5f, style = Stroke(3f * dp))
        softGlow(house + Offset(0f, -h * 0.15f), w * 0.2f, UdhC71.Lit, p1 * seg(pb, 0.4f, 0.7f) * 0.4f)
        tagAt(a.tag("onesheep"), Offset(w * 0.5f, h * 0.12f), p1 * seg(pb, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p2 = fqIn(a, "seventh", "notshared")
    if (p2 > 0f) {
        val pb = a.b("seventh")
        for (k in 0 until 2) {
            val g = seg(pb, 0.05f + 0.4f * k, 0.2f + 0.4f * k)
            val x = w * (0.72f - 0.42f * k)
            val foot = Offset(x, ground - h * 0.22f)
            if (k == 0) camel26(foot, h * 0.32f, UdhC71.Camel, p2 * g, t) else fqCow(foot, h * 0.28f, UdhC71.Cow, p2 * g)
            for (i in 1 until 7) {
                val xc = x - h * 0.14f + i * h * 0.28f / 7f
                drawLine(Color.White, Offset(xc, foot.y - h * 0.24f), Offset(xc, foot.y - h * 0.12f), 1.5f * dp, alpha = p2 * g * seg(pb, 0.1f + 0.4f * k, 0.25f + 0.4f * k))
            }
            for (i in 0 until 7) sheep26(Offset(x - w * 0.15f + i * w * 0.05f, ground + h * 0.08f), w * 0.035f, UdhC71.Fleece, p2 * seg(pb, 0.2f + 0.4f * k + 0.02f * i, 0.3f + 0.4f * k + 0.02f * i))
            tagAt(a.tag("seventh", 0, k), Offset(x, h * 0.12f), p2 * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    val p3 = fqIn(a, "notshared", "notmorethan7")
    if (p3 > 0f) {
        val pb = a.b("notshared")
        val c = Offset(w * 0.5f, ground - h * 0.06f)
        sheep26(c, h * 0.18f, UdhC71.Fleece, p3)
        figure(Offset(w * 0.78f, ground), h * 0.32f, UdhC71.Person, p3, arms = 0.5f)
        figure(Offset(w * 0.22f, ground), h * 0.32f, Color(0xFFB8C0D0), p3, arms = -0.5f)
        drawLine(Color(0xFF8A6A48), Offset(w * 0.72f, ground - h * 0.17f), c + Offset(h * 0.05f, -h * 0.03f), 1.5f * dp, alpha = p3)
        drawLine(Color(0xFF8A6A48), Offset(w * 0.28f, ground - h * 0.17f), c + Offset(-h * 0.1f, -h * 0.03f), 1.5f * dp, alpha = p3)
        cross14(c + Offset(0f, -h * 0.2f), w * 0.07f, seg(pb, 0.25f, 0.45f))
        tagAt(a.tag("notshared"), Offset(w * 0.5f, h * 0.14f), p3 * seg(pb, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p4 = fqIn(a, "notmorethan7")
    if (p4 > 0f) {
        val pb = a.b("notmorethan7")
        val c = Offset(w * 0.5f, h * 0.5f)
        camel26(c + Offset(0f, h * 0.15f), h * 0.3f, UdhC71.Camel, p4, t)
        for (k in 0 until 8) {
            val ang = PI.toFloat() * (1.1f + 0.8f * k / 7f)
            val pos = c + Offset(w * 0.32f * cos(ang), h * 0.3f * sin(ang) + h * 0.05f)
            val g = seg(pb, 0.05f + 0.07f * k, 0.15f + 0.07f * k)
            nameRoundel(pos, w * 0.03f, null, Roundel.PlainFill, Roundel.PlainRim, p4 * g)
            if (k == 7) cross14(pos, w * 0.04f, seg(pb, 0.65f, 0.8f))
        }
        tagAt(a.tag("notmorethan7"), Offset(w * 0.5f, h * 0.88f), p4 * seg(pb, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ember)
    }
}

// ═════════════ 17. udh-akl: eating, giving and the thirds ═════════════

/**
 * Beats: eatgivegive (three wrapped parcels go three ways: the home table, a neighbour's door, open hands: «يَأْكُلَ», «وَيُهْدِيَ»,
 * «وَيَتَصَدَّقَ»), thirds (a round tray parted in three equal shares: «وَالْمُخْتَارُ عِنْدَ الْمُؤَلِّفِ», «يَأْكُلَ ثُلُثًا», «وَيُهْدِيَ ثُلُثًا»,
 * «وَيَتَصَدَّقَ بِثُلُثٍ»), nosale (a hide with a price on it, crossed; a parcel offered as the worker's wage, crossed: «حَتَّى الْجِلْدَ»,
 * «فِي مُقَابَلَةِ أُجْرَتِهِ»), designated (a ribbon on a sheep, and a lock: «هَذِهِ أُضْحِيَّةٌ»).
 */
internal fun DrawScope.threethirdsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "eatgivegive" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "thirds" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "nosale" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "designated" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { threeThirdsDraw(a) }
}

private fun DrawScope.parcel71(c: Offset, s: Float, alpha: Float) {
    drawRoundRect(Color(0xFFF2E8D8), c - Offset(s * 0.5f, s * 0.35f), Size(s, s * 0.7f), CornerRadius(s * 0.1f), alpha = alpha)
    drawLine(Color(0xFFB08850), c + Offset(0f, -s * 0.35f), c + Offset(0f, s * 0.35f), s * 0.08f, alpha = alpha)
    drawLine(Color(0xFFB08850), c + Offset(-s * 0.5f, 0f), c + Offset(s * 0.5f, 0f), s * 0.08f, alpha = alpha)
}

private fun DrawScope.threeThirdsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val ground = h * 0.8f
    udhDay(ground)
    val p1 = fqIn(a, "eatgivegive", "thirds")
    if (p1 > 0f) {
        val pb = a.b("eatgivegive")
        val src = Offset(w * 0.5f, h * 0.3f)
        val ends = listOf(Offset(w * 0.8f, ground - h * 0.1f), Offset(w * 0.5f, ground - h * 0.08f), Offset(w * 0.2f, ground - h * 0.1f))
        house12(Offset(w * 0.8f, ground), w * 0.16f, Color(0xFFD8C4A0), Color(0xFF8A6A48), p1, lit = 0.6f)
        door12(Offset(w * 0.5f, ground), w * 0.08f, h * 0.2f, seg(pb, 0.45f, 0.6f), Color(0xFF8A6A48), Color(0xFF6A4A30), p1)
        hands12(Offset(w * 0.2f, ground - h * 0.04f), h * 0.14f, UdhC71.Person, p1)
        for (k in 0 until 3) {
            val u = easeInOut(seg(pb, 0.1f + 0.25f * k, 0.35f + 0.25f * k))
            parcel71(Offset(mix(src.x, ends[k].x, u), mix(src.y, ends[k].y, u) - sin(PI.toFloat() * u) * h * 0.1f), w * 0.05f, p1 * seg(pb, 0.05f, 0.1f))
            tagAt(a.tag("eatgivegive", 0, k), Offset(ends[k].x, h * 0.88f), p1 * seg(pb, 0.25f + 0.25f * k, 0.35f + 0.25f * k), back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    val p2 = fqIn(a, "thirds", "nosale")
    if (p2 > 0f) {
        val pb = a.b("thirds")
        val c = Offset(w * 0.5f, h * 0.5f)
        val r = h * 0.2f
        drawCircle(Fq.Brass, r * 1.08f, c, alpha = p2)
        for (k in 0 until 3) {
            val g = seg(pb, 0.2f + 0.2f * k, 0.35f + 0.2f * k)
            drawArc(listOf(UdhC71.Meat, Color(0xFFD8A090), Color(0xFFB87A6A))[k], -90f + 120f * k, 118f * g, true, c - Offset(r, r), Size(r * 2f, r * 2f), alpha = p2)
            val mid = (-90f + 120f * k + 60f) * PI.toFloat() / 180f
            tagAt(a.tag("thirds", 0, k + 1), c + Offset(r * 1.7f * cos(mid), r * 1.5f * sin(mid)), p2 * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
        tagAt(a.tag("thirds", 0, 0), Offset(w * 0.5f, h * 0.1f), p2 * seg(pb, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p3 = fqIn(a, "nosale", "designated")
    if (p3 > 0f) {
        val pb = a.b("nosale")
        val hide = Offset(w * 0.7f, h * 0.45f)
        drawPath(Path().apply {
            moveTo(hide.x - w * 0.1f, hide.y - h * 0.1f); quadraticBezierTo(hide.x, hide.y - h * 0.14f, hide.x + w * 0.1f, hide.y - h * 0.1f)
            lineTo(hide.x + w * 0.12f, hide.y + h * 0.1f); quadraticBezierTo(hide.x, hide.y + h * 0.15f, hide.x - w * 0.12f, hide.y + h * 0.1f); close()
        }, Color(0xFFC8A070), alpha = p3 * seg(pb, 0.05f, 0.15f))
        coin12(hide + Offset(w * 0.1f, -h * 0.12f), w * 0.025f, p3 * seg(pb, 0.1f, 0.2f))
        cross14(hide, w * 0.09f, seg(pb, 0.2f, 0.4f))
        tagAt(a.tag("nosale", 0, 0), hide + Offset(0f, h * 0.24f), p3 * seg(pb, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        val worker = Offset(w * 0.18f, ground)
        figure(worker, h * 0.34f, Color(0xFFB8C0D0), p3 * seg(pb, 0.45f, 0.6f))
        parcel71(worker + Offset(w * 0.12f, -h * 0.2f), w * 0.05f, p3 * seg(pb, 0.5f, 0.6f))
        cross14(worker + Offset(w * 0.12f, -h * 0.2f), w * 0.06f, seg(pb, 0.6f, 0.8f))
        tagAt(a.tag("nosale", 0, 1), Offset(w * 0.3f, h * 0.15f), p3 * seg(pb, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p4 = fqIn(a, "designated")
    if (p4 > 0f) {
        val pb = a.b("designated")
        val c = Offset(w * 0.55f, ground - h * 0.1f)
        sheep26(c, h * 0.28f, UdhC71.Fleece, p4)
        val rib = seg(pb, 0.1f, 0.3f)
        drawLine(UdhC71.Lit, c + Offset(-h * 0.13f, -h * 0.05f), c + Offset(-h * 0.13f, h * 0.05f), 5f * dp, alpha = p4 * rib)
        drawPath(Path().apply { val q = c + Offset(-h * 0.13f, h * 0.05f); moveTo(q.x, q.y); lineTo(q.x - w * 0.02f, q.y + h * 0.08f); lineTo(q.x + w * 0.02f, q.y + h * 0.08f); close() }, UdhC71.Lit, alpha = p4 * rib)
        padlock21(Offset(w * 0.25f, h * 0.45f), w * 0.07f, Fq.Brass, 0f, p4 * seg(pb, 0.45f, 0.6f))
        tagAt(a.tag("designated"), Offset(w * 0.55f, h * 0.2f), p4 * seg(pb, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 18. udh-imsak: what the one who offers keeps from ═════════════

/**
 * Beats: noclip (the new moon of Dhu al-Ḥijja, then scissors, a nail and skin, crossed, while the days run to the tenth: «شَعْرِهِ»,
 * «أَظْفَارِهِ», «جِلْدِهِ»), onlyhim (in a household only one is ringed: «خَاصٌّ بِمَنْ يُضَحِّي»), tawbanokaffara (a light of repentance;
 * no coin of expiation; the sheep still ticked: «تَابَ وَلَا كَفَّارَةَ عَلَيْهِ»), likepilgrim (a pilgrim in white far off and the man at
 * home, joined by an arc of light: «شَارَكَ الْحَاجَّ»), dhakatconds (three conditions written on a sheet and ticked: «عَاقِلًا مُمَيِّزًا»,
 * «مُسْلِمًا أَوْ كِتَابِيًّا», «وَأَنْ يَذْكُرَ اسْمَ اللَّهِ عَلَيْهَا»).
 */
internal fun DrawScope.dhulhijjanailsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "noclip" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "onlyhim" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "tawbanokaffara" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "likepilgrim" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "dhakatconds" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { dhulhijjaNailsDraw(a) }
}

private fun DrawScope.dhulhijjaNailsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val ground = h * 0.8f
    nightSky(t, UdhC71.Sky, UdhC71.SkyLow, ground, starAlpha = 0.8f)
    drawRect(UdhC71.Ground, Offset(0f, ground), Size(w, h - ground))
    val p1 = fqIn(a, "noclip", "onlyhim")
    if (p1 > 0f) {
        val pb = a.b("noclip")
        crescent12(Offset(w * 0.85f, h * 0.14f), w * 0.03f, Fq.Gold, UdhC71.Sky, p1 * seg(pb, 0f, 0.15f))
        for (k in 0 until 3) {
            val c = Offset(w * (0.75f - 0.25f * k), h * 0.42f)
            val g = seg(pb, 0.1f + 0.2f * k, 0.25f + 0.2f * k)
            when (k) {
                0 -> fqScissors(c, w * 0.07f, Color(0xFFB8B8C8), p1 * g, open = 0.6f)
                1 -> drawRoundRect(Color(0xFFE8C8B0), c - Offset(w * 0.03f, h * 0.05f), Size(w * 0.06f, h * 0.1f), CornerRadius(w * 0.025f), alpha = p1 * g)
                else -> drawOval(Color(0xFFD8B098), Offset(c.x - w * 0.06f, c.y - h * 0.04f), Size(w * 0.12f, h * 0.08f), alpha = p1 * g)
            }
            cross14(c, w * 0.07f, seg(pb, 0.18f + 0.2f * k, 0.3f + 0.2f * k))
            tagAt(a.tag("noclip", 0, k), c + Offset(0f, h * 0.16f), p1 * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
        val run = seg(pb, 0.2f, 0.95f)
        fqCalendar(Offset(w * 0.2f, h * 0.7f), 10, 10, w * 0.05f, p1, Color(0xFFB8B0C8)) { i -> if (i < run * 10f) Fq.Gold.copy(alpha = if (i == 9) 1f else 0.55f) else null }
    }
    val p2 = fqIn(a, "onlyhim", "tawbanokaffara")
    if (p2 > 0f) {
        val pb = a.b("onlyhim")
        house12(Offset(w * 0.5f, ground), w * 0.5f, Color(0xFF4A4050), Color(0xFF2A2430), p2 * 0.6f, lit = 0.4f)
        for (k in 0 until 4) {
            val foot = Offset(w * (0.68f - 0.12f * k), ground)
            val tall = if (k == 3) h * 0.2f else h * 0.3f
            if (k == 0) drawCircle(UdhC71.Lit, h * 0.12f, foot + Offset(0f, -tall * 0.6f), alpha = p2 * seg(pb, 0.3f, 0.45f), style = Stroke(3f * dp))
            if (k == 3) child12(foot, tall, UdhC71.Person, p2) else figure(foot, tall, UdhC71.Person, p2)
        }
        tagAt(a.tag("onlyhim"), Offset(w * 0.5f, h * 0.15f), p2 * seg(pb, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p3 = fqIn(a, "tawbanokaffara", "likepilgrim")
    if (p3 > 0f) {
        val pb = a.b("tawbanokaffara")
        val foot = Offset(w * 0.62f, ground)
        softGlow(foot + Offset(0f, -h * 0.25f), w * 0.15f, UdhC71.Lit, p3 * seg(pb, 0.1f, 0.35f) * 0.6f)
        figure(foot, h * 0.38f, UdhC71.Person, p3)
        coin12(Offset(w * 0.3f, h * 0.4f), w * 0.04f, p3 * seg(pb, 0.3f, 0.45f))
        cross14(Offset(w * 0.3f, h * 0.4f), w * 0.06f, seg(pb, 0.4f, 0.55f))
        sheep26(Offset(w * 0.25f, ground - h * 0.06f), h * 0.16f, UdhC71.Fleece, p3 * seg(pb, 0.55f, 0.7f))
        tick14(Offset(w * 0.25f, ground - h * 0.22f), w * 0.035f, seg(pb, 0.65f, 0.85f))
        tagAt(a.tag("tawbanokaffara"), Offset(w * 0.5f, h * 0.14f), p3 * seg(pb, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p4 = fqIn(a, "likepilgrim", "dhakatconds")
    if (p4 > 0f) {
        val pb = a.b("likepilgrim")
        kaaba29(Offset(w * 0.82f, ground - h * 0.02f), w * 0.07f, p4)
        fqPilgrim(Offset(w * 0.7f, ground), h * 0.26f, p4)
        house12(Offset(w * 0.2f, ground), w * 0.16f, Color(0xFF4A4050), Color(0xFF2A2430), p4, lit = 0.5f)
        figure(Offset(w * 0.33f, ground), h * 0.26f, UdhC71.Person, p4)
        val u = easeInOut(seg(pb, 0.2f, 0.6f))
        val path = Path().apply { moveTo(w * 0.7f, ground - h * 0.3f); quadraticBezierTo(w * 0.52f, h * 0.15f, mix(w * 0.7f, w * 0.33f, u), ground - h * 0.3f - sin(PI.toFloat() * u) * h * 0.1f) }
        drawPath(path, UdhC71.Lit, alpha = p4 * seg(pb, 0.2f, 0.3f), style = Stroke(2.5f * dp, cap = StrokeCap.Round))
        tagAt(a.tag("likepilgrim"), Offset(w * 0.52f, h * 0.12f), p4 * seg(pb, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p5 = fqIn(a, "dhakatconds")
    if (p5 > 0f) {
        val pb = a.b("dhakatconds")
        val c = Offset(w * 0.5f, h * 0.5f)
        drawRect(Color.Black, Offset(c.x - w * 0.3f + 3f * dp, c.y - h * 0.32f + 4f * dp), Size(w * 0.6f, h * 0.64f), alpha = p5 * 0.3f)
        drawRect(Color(0xFFF6EBCF), Offset(c.x - w * 0.3f, c.y - h * 0.32f), Size(w * 0.6f, h * 0.64f), alpha = p5)
        for (k in 0 until 3) {
            val g = seg(pb, 0.05f + 0.28f * k, 0.2f + 0.28f * k)
            val y = c.y - h * 0.18f + k * h * 0.18f
            tick14(Offset(c.x + w * 0.24f, y), w * 0.025f, g)
            tagAt(a.tag("dhakatconds", 0, k), Offset(c.x - w * 0.02f, y), p5 * g, back = Color.Transparent, ink = Color(0xFF3A2A1A))
        }
    }
}
