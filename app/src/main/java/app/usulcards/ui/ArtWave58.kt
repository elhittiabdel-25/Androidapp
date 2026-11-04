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
 * Wave 58 (2026-11-03): «الأربعون النووية», hadiths 6–10. The fenced pasture and the three shepherds; sincere counsel to
 * five; three pillars shown and the rest left to Allah; as much as you can; only the good is accepted. Allah is never
 * drawn; the Prophet ﷺ is only light; companions are names in roundels; people are generic figures or plain roundels.
 */

private val Back58 = Color(0xE6181420)

// ═════════════ 1. the edge of the sanctuary (nw-06) ═════════════

private object HimaC58 {
    val Sky = Color(0xFFC0DAEA)
    val SkyLow = Color(0xFFF0E8D0)
    val Grass = Color(0xFF8AB46A)
    val Halal = Color(0xFF5AA06A)
    val Haram = Color(0xFFC0503A)
    val Grey = Color(0xFF9A9A8A)
    val Fence = Color(0xFF6A4A32)
    val Person = Color(0xFF3A3040)
    val Sheep = Color(0xFFF2EEE4)
    val Gold = Color(0xFFE8B84A)
}

/**
 * Beats: threezones (a field in three bands: green «حَلَالٌ بَيِّنٌ», red «وَحَرَامٌ بَيِّنٌ», and grey between, «وَمُشْتَبِهٌ»),
 * stepclear (a figure at the grey edge steps back into the green: «اسْتَبْرَأَ لِدِينِهِ وَعِرْضِهِ»), nearhima (a shepherd
 * grazes by a fence; one sheep wanders toward the gap), threeshepherds (three shepherds: one inside the fence, one at it, one
 * far from it), slopedown (a ball on a slope runs from grey into red), limbsfollow (a heart lights in a figure, and the light
 * spreads to hands and feet).
 */
internal fun DrawScope.himaedgeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threezones" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
        "stepclear" to Shot(Offset(w * 0.5f, h * 0.65f), 1.1f),
        "nearhima" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
        "threeshepherds" to Shot(Offset(w * 0.5f, h * 0.65f), 1.0f),
        "slopedown" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "limbsfollow" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { himaDay(a) }
}

private fun DrawScope.himaDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tz = a.b("threezones")
    val sc = a.b("stepclear")
    val nh = a.b("nearhima")
    val ts = a.b("threeshepherds")
    val sd = a.b("slopedown")
    val lf = a.b("limbsfollow")
    val ground = h * 0.62f
    daySky12(HimaC58.Sky, HimaC58.SkyLow, ground, HimaC58.Grass)
    if (tz > 0f || sc > 0f) {
        val f = easeOut(seg(tz, 0f, BEAT_IN)) * (1f - seg(nh, 0f, BEAT_OUT))
        val cols = listOf(HimaC58.Halal, HimaC58.Grey, HimaC58.Haram)
        val order = listOf(0, 2, 1)
        for (k in 0 until 3) {
            val x = w * (0.67f - 0.33f * k)
            val g = seg(tz, 0.05f + 0.15f * k, 0.2f + 0.15f * k)
            drawRect(cols[k], Offset(x, ground), Size(w * 0.33f, h - ground), alpha = f * g * 0.85f)
        }
        for (k in 0 until 3) {
            val zone = order[k]
            val x = w * (0.67f - 0.33f * zone) + w * 0.165f
            tagAt(a.tag("threezones", 0, k), Offset(x, ground + h * 0.08f), f * seg(tz, 0.15f + 0.2f * k, 0.3f + 0.2f * k), back = Back58)
        }
        if (sc > 0f) {
            val back = easeInOut(seg(sc, 0.15f, 0.55f))
            val foot = Offset(mix(w * 0.62f, w * 0.85f, back), h * 0.9f)
            figure(foot, h * 0.28f, HimaC58.Person, f * seg(sc, 0f, 0.15f), walk = t * 5f * (1f - seg(sc, 0.5f, 0.55f)))
            guardDome12(foot, w * 0.1f, seg(sc, 0.5f, 0.75f), t, f)
            tagAt(a.tag("stepclear"), Offset(w * 0.5f, h * 0.2f), f * seg(sc, 0.5f, 0.7f), back = Back58, ink = ArtInk.Gold)
        }
    }
    val fenceL = w * 0.12f
    val fenceR = w * 0.48f
    val fenceY = h * 0.78f
    val fence = { alpha: Float ->
        for (k in 0..8) {
            val x = mix(fenceL, fenceR, k / 8f)
            drawLine(HimaC58.Fence, Offset(x, fenceY), Offset(x, fenceY - h * 0.08f), 3f * dp, StrokeCap.Round, alpha = alpha)
        }
        drawLine(HimaC58.Fence, Offset(fenceL, fenceY - h * 0.06f), Offset(fenceR, fenceY - h * 0.06f), 2.5f * dp, alpha = alpha)
        drawLine(HimaC58.Fence, Offset(fenceL, fenceY - h * 0.02f), Offset(fenceR, fenceY - h * 0.02f), 2.5f * dp, alpha = alpha)
    }
    if (nh > 0f) {
        val f = easeOut(seg(nh, 0f, BEAT_IN)) * (1f - seg(ts, 0f, BEAT_OUT))
        drawRect(HimaC58.Halal, Offset(fenceL, fenceY - h * 0.15f), Size(fenceR - fenceL, h * 0.12f), alpha = f * 0.25f)
        fence(f)
        figure(Offset(w * 0.75f, h * 0.92f), h * 0.28f, HimaC58.Person, f)
        val drift = easeInOut(seg(nh, 0.2f, 0.9f))
        for (k in 0 until 4) {
            val c = Offset(w * (0.62f + 0.06f * k) - (if (k == 0) w * 0.1f * drift else 0f), h * (0.86f + 0.02f * (k % 2)) - (if (k == 0) h * 0.04f * drift else 0f))
            sheep26(c, w * 0.05f, HimaC58.Sheep, f)
        }
    }
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, BEAT_IN)) * (1f - seg(sd, 0f, BEAT_OUT))
        drawRect(HimaC58.Haram, Offset(fenceL, fenceY - h * 0.15f), Size(fenceR - fenceL, h * 0.12f), alpha = f * 0.25f)
        fence(f)
        val spots = listOf(Offset(w * 0.3f, fenceY - h * 0.04f), Offset(w * 0.55f, h * 0.88f), Offset(w * 0.88f, h * 0.92f))
        for (k in 0 until 3) {
            val g = seg(ts, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            figure(spots[k], h * (0.22f + 0.02f * k), HimaC58.Person, f * g)
            if (k == 2) softGlow(spots[k] + Offset(0f, -h * 0.12f), w * 0.07f, HimaC58.Gold, 0.5f * f * g)
        }
    }
    if (sd > 0f) {
        val f = easeOut(seg(sd, 0f, BEAT_IN)) * (1f - seg(lf, 0f, BEAT_OUT))
        val top = Offset(w * 0.85f, h * 0.35f)
        val mid = Offset(w * 0.5f, h * 0.6f)
        val bot = Offset(w * 0.12f, h * 0.85f)
        drawLine(HimaC58.Grey, top, mid, h * 0.025f, StrokeCap.Round, alpha = f)
        drawLine(HimaC58.Haram, mid, bot, h * 0.025f, StrokeCap.Round, alpha = f)
        val u = easeIn58(seg(sd, 0.2f, 0.85f))
        val p = if (u < 0.5f) top + (mid - top) * (u * 2f) else mid + (bot - mid) * ((u - 0.5f) * 2f)
        drawCircle(HimaC58.Person, w * 0.025f, p + Offset(0f, -h * 0.03f), alpha = f)
    }
    if (lf > 0f) {
        val f = easeOut(seg(lf, 0f, 0.25f))
        val foot = Offset(w * 0.5f, h * 0.9f)
        val tall = h * 0.55f
        figure(foot, tall, HimaC58.Person, f, arms = 0.3f)
        val heart = foot + Offset(0f, -tall * 0.66f)
        val g = seg(lf, 0.15f, 0.4f)
        heart12(heart, w * 0.035f, HimaC58.Gold, f * g, fill = true)
        softGlow(heart, w * 0.1f, HimaC58.Gold, 0.6f * f * g)
        val spread = seg(lf, 0.45f, 0.85f)
        for (p in listOf(Offset(-tall * 0.2f, -tall * 0.45f), Offset(tall * 0.2f, -tall * 0.45f), Offset(-tall * 0.07f, 0f), Offset(tall * 0.07f, 0f))) {
            softGlow(foot + p, w * 0.04f, HimaC58.Gold, 0.7f * f * spread)
        }
    }
}

// ═════════════ 2. sincere counsel (nw-07) ═════════════

private object CounselC58 {
    val Night = Color(0xFF141A2C)
    val NightLow = Color(0xFF2E3050)
    val Ground = Color(0xFF1E2030)
    val Gold = Color(0xFFF2C860)
    val Water = Color(0xFF8AC4EA)
    val Jar = Color(0xFFB88A58)
    val Book = Color(0xFF2E5A4A)
    val Page = Color(0xFFF2E8D2)
    val Paper = Color(0xFF9A9AA8)
    val Path = Color(0xFF4A5070)
    val Green = Color(0xFF6FCB9F)
}

/**
 * Beats: thriceword (the same words come three times, each larger: «الدِّينُ النَّصِيحَةُ»), cleanfull (clear water poured
 * into a jar until it is full: «السَّلَامَةُ مِنَ الْغِشِّ», «وَبَذْلُ الْمَجْهُودِ»), fiveto (one stem and five branches with
 * their words: «لِلَّهِ», «وَلِكِتَابِهِ», «وَلِرَسُولِهِ», «وَلِأَئِمَّةِ الْمُسْلِمِينَ», «وَعَامَّتِهِمْ»), bookfirst (a book
 * rises above a pile of papers), measuringline (a line of light from the light ﷺ; papers are held against it, and those
 * that lean off it fall away), pointgood (plain roundels at a fork; a light points them to the lit path).
 */
internal fun DrawScope.fivecounselScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "thriceword" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "cleanfull" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "fiveto" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "bookfirst" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "measuringline" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "pointgood" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { counselNight(a) }
}

private fun DrawScope.counselNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tw = a.b("thriceword")
    val cf = a.b("cleanfull")
    val ft = a.b("fiveto")
    val bf = a.b("bookfirst")
    val ml = a.b("measuringline")
    val pg = a.b("pointgood")
    val ground = h * 0.86f
    nightSky(t, CounselC58.Night, CounselC58.NightLow, ground, starAlpha = 0.8f)
    drawRect(CounselC58.Ground, Offset(0f, ground), Size(w, h - ground))
    if (tw > 0f) {
        val f = easeOut(seg(tw, 0f, BEAT_IN)) * (1f - seg(cf, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val g = seg(tw, 0.08f + 0.25f * k, 0.25f + 0.25f * k)
            val c = Offset(w * 0.5f, h * (0.3f + 0.18f * k))
            softGlow(c, w * (0.1f + 0.05f * k), CounselC58.Gold, 0.3f * f * g)
            tagAt(a.tag("thriceword"), c, f * g, back = Back58, ink = ArtInk.Gold)
        }
    }
    if (cf > 0f) {
        val f = easeOut(seg(cf, 0f, BEAT_IN)) * (1f - seg(ft, 0f, BEAT_OUT))
        val foot = Offset(w * 0.5f, ground)
        val fill = seg(cf, 0.2f, 0.85f)
        val jw = w * 0.22f
        val jh = h * 0.32f
        drawRect(CounselC58.Water, Offset(foot.x - jw / 2f + 4f * dp, foot.y - jh * fill), Size(jw - 8f * dp, jh * fill), alpha = f * 0.8f)
        drawRoundRect(CounselC58.Jar, Offset(foot.x - jw / 2f, foot.y - jh), Size(jw, jh), CornerRadius(14f * dp), alpha = f, style = Stroke(4f * dp))
        if (fill < 1f) drawLine(CounselC58.Water, Offset(foot.x, h * 0.2f), Offset(foot.x, foot.y - jh * fill), 4f * dp, alpha = f * seg(cf, 0.15f, 0.2f))
        tagAt(a.tag("cleanfull", 0, 0), Offset(w * 0.25f, h * 0.3f), f * seg(cf, 0.1f, 0.25f), back = Back58, ink = CounselC58.Water)
        tagAt(a.tag("cleanfull", 0, 1), Offset(w * 0.75f, h * 0.42f), f * seg(cf, 0.4f, 0.55f), back = Back58, ink = ArtInk.Gold)
    }
    if (ft > 0f) {
        val f = easeOut(seg(ft, 0f, BEAT_IN)) * (1f - seg(bf, 0f, BEAT_OUT))
        val root = Offset(w * 0.5f, ground)
        val fork = Offset(w * 0.5f, h * 0.62f)
        drawLine(CounselC58.Gold, root, fork, 4f * dp, StrokeCap.Round, alpha = f * seg(ft, 0f, 0.1f))
        for (k in 0 until 5) {
            val ang = PI.toFloat() * (1.1f + 0.8f * k / 4f)
            val end = fork + Offset(cos(ang) * w * 0.36f, sin(ang) * h * 0.4f)
            val g = seg(ft, 0.1f + 0.16f * k, 0.25f + 0.16f * k)
            drawLine(CounselC58.Gold, fork, fork + (end - fork) * g, 3f * dp, StrokeCap.Round, alpha = f)
            tagAt(a.tag("fiveto", 0, k), end, f * g, back = Back58, ink = ArtInk.Gold)
        }
    }
    if (bf > 0f) {
        val f = easeOut(seg(bf, 0f, BEAT_IN)) * (1f - seg(ml, 0f, BEAT_OUT))
        for (k in 0 until 6) drawRect(CounselC58.Paper, Offset(w * (0.25f + 0.02f * (k % 2)), ground - h * 0.03f * (k + 1)), Size(w * 0.5f, h * 0.025f), alpha = f * 0.8f)
        val rise = easeInOut(seg(bf, 0.2f, 0.6f))
        val c = Offset(w * 0.5f, mix(ground - h * 0.05f, h * 0.38f, rise))
        openBook21(c, w * 0.42f, CounselC58.Page, CounselC58.Book, f, seg(bf, 0.4f, 0.8f))
        softGlow(c, w * 0.2f, CounselC58.Gold, 0.4f * f * rise)
    }
    if (ml > 0f) {
        val f = easeOut(seg(ml, 0f, BEAT_IN)) * (1f - seg(pg, 0f, BEAT_OUT))
        val light = Offset(w * 0.5f, h * 0.16f)
        sirajMunir(light, w * 0.011f, f, t, nameAt = light + Offset(0f, -h * 0.09f))
        val drop = seg(ml, 0.1f, 0.35f)
        drawLine(CounselC58.Gold, light + Offset(0f, h * 0.05f), Offset(w * 0.5f, mix(h * 0.21f, ground, drop)), 3f * dp, alpha = f)
        for (k in 0 until 4) {
            val y = h * (0.35f + 0.12f * k)
            val off = k == 1 || k == 3
            val lean = if (off) 0.35f * seg(ml, 0.45f + 0.08f * k, 0.6f + 0.08f * k) else 0f
            val fall = if (off) easeIn58(seg(ml, 0.7f + 0.05f * k, 0.95f)) else 0f
            val c = Offset(w * (if (k % 2 == 0) 0.35f else 0.65f), y + (ground - y) * fall)
            val p = Path().apply {
                val dx = cos(lean) * w * 0.09f
                val dy = sin(lean) * w * 0.09f
                moveTo(c.x - dx, c.y - dy); lineTo(c.x + dx, c.y + dy)
            }
            drawPath(p, if (off) CounselC58.Paper else CounselC58.Page, alpha = f * seg(ml, 0.3f + 0.05f * k, 0.4f + 0.05f * k) * (1f - 0.7f * fall), style = Stroke(h * 0.03f))
        }
    }
    if (pg > 0f) {
        val f = easeOut(seg(pg, 0f, 0.25f))
        val fork = Offset(w * 0.5f, h * 0.62f)
        drawLine(CounselC58.Path, Offset(w * 0.5f, ground), fork, h * 0.02f, StrokeCap.Round, alpha = f)
        drawLine(CounselC58.Path, fork, Offset(w * 0.15f, h * 0.3f), h * 0.02f, StrokeCap.Round, alpha = f)
        drawLine(CounselC58.Green, fork, Offset(w * 0.85f, h * 0.3f), h * 0.02f, StrokeCap.Round, alpha = f)
        softGlow(Offset(w * 0.85f, h * 0.28f), w * 0.07f, CounselC58.Gold, f * seg(pg, 0.2f, 0.4f))
        val go = easeInOut(seg(pg, 0.35f, 0.9f))
        for (k in 0 until 3) {
            val u = (go - 0.12f * k).coerceIn(0f, 1f)
            val c = if (u <= 0f) Offset(w * (0.45f + 0.05f * k), ground - h * 0.04f) else fork + (Offset(w * 0.85f, h * 0.3f) - fork) * u
            nameRoundel(c, w * 0.025f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        }
    }
}

// ═════════════ 3. three shown, the rest with Allah (nw-08) ═════════════

private object ShownC58 {
    val Sky = Color(0xFF1A1E34)
    val SkyLow = Color(0xFF34365A)
    val Ground = Color(0xFF22222E)
    val Stone = Color(0xFFD8CCB4)
    val Dim = Color(0xFF6A6A78)
    val Gold = Color(0xFFF2C860)
    val Seal = Color(0xFFB04A3A)
    val Box = Color(0xFF5A4A3A)
    val Bead = Color(0xFF8AC4A8)
    val Rope = Color(0xFFB89A6A)
    val Veil = Color(0xFF3A3A50)
}

/**
 * Beats: threelit (five columns; three of them light up: «الشَّهَادَتَيْنِ», «وَالصَّلَاةِ», «وَالزَّكَاةِ»), sealedfast (a closed
 * box with a seal, its light kept inside: «فَسِرٌّ وَأَمَانَةٌ»), bloodwealth (a dome of light comes down over a house and its
 * coins: «عَصَمَ دَمَهُ وَمَالَهُ»), sevenconds (seven beads on one string, each with its word), iqal (a roundel with its
 * name, «أَبُو بَكْرٍ», and a hobbling cord beside it: «عِقَالًا»), outerjudged (a sphere whose outer shell is lit and whose
 * core stays veiled).
 */
internal fun DrawScope.threeshownScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threelit" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "sealedfast" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "bloodwealth" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "sevenconds" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "iqal" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "outerjudged" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { shownNight(a) }
}

private fun DrawScope.shownNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tl = a.b("threelit")
    val sf = a.b("sealedfast")
    val bw = a.b("bloodwealth")
    val sc = a.b("sevenconds")
    val iq = a.b("iqal")
    val oj = a.b("outerjudged")
    val ground = h * 0.84f
    nightSky(t, ShownC58.Sky, ShownC58.SkyLow, ground, starAlpha = 0.7f)
    drawRect(ShownC58.Ground, Offset(0f, ground), Size(w, h - ground))
    if (tl > 0f) {
        val f = easeOut(seg(tl, 0f, BEAT_IN)) * (1f - seg(sf, 0f, BEAT_OUT))
        val lit = listOf(0, 1, 2)
        for (k in 0 until 5) {
            val x = w * (0.82f - 0.16f * k)
            val i = lit.indexOf(k)
            val g = if (i >= 0) seg(tl, 0.15f + 0.22f * i, 0.3f + 0.22f * i) else 0f
            drawRect(lerpColor(ShownC58.Dim, ShownC58.Stone, g), Offset(x - w * 0.035f, ground - h * 0.4f), Size(w * 0.07f, h * 0.4f), alpha = f)
            if (g > 0f) {
                softGlow(Offset(x, ground - h * 0.2f), w * 0.07f, ShownC58.Gold, 0.5f * f * g)
                tagAt(a.tag("threelit", 0, i), Offset(x, ground - h * (0.48f + 0.06f * (i % 2))), f * g, back = Back58, ink = ArtInk.Gold)
            }
        }
    }
    if (sf > 0f) {
        val f = easeOut(seg(sf, 0f, BEAT_IN)) * (1f - seg(bw, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.6f)
        drawRoundRect(ShownC58.Box, Offset(c.x - w * 0.15f, c.y - h * 0.1f), Size(w * 0.3f, h * 0.2f), CornerRadius(6f * dp), alpha = f)
        val seal = seg(sf, 0.25f, 0.45f)
        drawCircle(ShownC58.Seal, w * 0.035f * easeOut(seal), c, alpha = f)
        softGlow(c, w * 0.06f, ShownC58.Gold, 0.25f * f * (0.7f + 0.3f * sin(t * 2f)))
        drawRoundRect(ShownC58.Gold, Offset(c.x - w * 0.15f, c.y - h * 0.1f), Size(w * 0.3f, h * 0.2f), CornerRadius(6f * dp), alpha = f * 0.25f * seal, style = Stroke(1.5f * dp))
        tagAt(a.tag("sealedfast"), Offset(w * 0.5f, h * 0.3f), f * seg(sf, 0.4f, 0.6f), back = Back58, ink = ArtInk.Gold)
    }
    if (bw > 0f) {
        val f = easeOut(seg(bw, 0f, BEAT_IN)) * (1f - seg(sc, 0f, 0.4f))
        val foot = Offset(w * 0.45f, ground)
        house12(foot, w * 0.26f, ShownC58.Stone, ShownC58.Box, f, 0.8f)
        for (k in 0 until 5) coin12(foot + Offset(w * (0.2f + 0.025f * (k % 3)), -h * (0.015f + 0.02f * (k / 3))), w * 0.014f, f)
        guardDome12(foot + Offset(w * 0.05f, 0f), w * 0.3f, seg(bw, 0.2f, 0.6f), t, f)
        tagAt(a.tag("bloodwealth"), Offset(w * 0.5f, h * 0.2f), f * seg(bw, 0.4f, 0.6f), back = Back58, ink = ArtInk.Gold)
    }
    if (sc > 0f) {
        val f = easeOut(seg(sc, 0f, 0.25f)) * (1f - seg(iq, 0f, BEAT_OUT))
        val p0 = Offset(w * 0.92f, h * 0.4f)
        val p3 = Offset(w * 0.08f, h * 0.4f)
        val p1 = Offset(w * 0.7f, h * 0.75f)
        val p2 = Offset(w * 0.3f, h * 0.75f)
        drawPath(cubicPart(p0, p1, p2, p3, 0f, seg(sc, 0.02f, 0.15f)), ShownC58.Rope, alpha = f, style = Stroke(2f * dp))
        for (k in 0 until 7) {
            val u = 0.08f + 0.14f * k
            val c = cubic(p0, p1, p2, p3, u)
            val g = seg(sc, 0.1f + 0.11f * k, 0.2f + 0.11f * k)
            drawCircle(ShownC58.Bead, w * 0.025f * easeOut(g), c, alpha = f)
            tagAt(a.tag("sevenconds", 0, k), c + Offset(0f, if (k % 2 == 0) -h * 0.08f else h * 0.08f), f * g, back = Back58, ink = ShownC58.Bead)
        }
    }
    if (iq > 0f) {
        val f = easeOut(seg(iq, 0f, BEAT_IN)) * (1f - seg(oj, 0f, BEAT_OUT))
        nameRoundel(Offset(w * 0.6f, h * 0.48f), w * 0.1f, a.big("iqal", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(iq, 0.05f, 0.25f))
        val g = seg(iq, 0.45f, 0.7f)
        val c = Offset(w * 0.25f, h * 0.5f)
        drawPath(Path().apply { moveTo(c.x - w * 0.08f, c.y); cubicTo(c.x - w * 0.04f, c.y - h * 0.08f, c.x + w * 0.04f, c.y + h * 0.08f, c.x + w * 0.08f, c.y) }, ShownC58.Rope, alpha = f * g, style = Stroke(5f * dp, cap = StrokeCap.Round))
        drawCircle(ShownC58.Rope, w * 0.03f, c + Offset(w * 0.08f, 0f), alpha = f * g, style = Stroke(4f * dp))
        tagAt(a.tag("iqal", 0, 1), c + Offset(0f, h * 0.12f), f * g, back = Back58, ink = ArtInk.Gold)
    }
    if (oj > 0f) {
        val f = easeOut(seg(oj, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.52f)
        val g = seg(oj, 0.15f, 0.45f)
        drawCircle(ShownC58.Gold, w * 0.24f, c, alpha = f * 0.25f * g)
        drawCircle(ShownC58.Gold, w * 0.24f, c, alpha = f * g, style = Stroke(4f * dp))
        val v = seg(oj, 0.4f, 0.7f)
        drawCircle(ShownC58.Veil, w * 0.14f, c, alpha = f * (0.4f + 0.55f * v))
        qMark14(c, w * 0.06f, ShownC58.Dim, f * v)
    }
}

// ═════════════ 4. as much as you can (nw-09) ═════════════

private object CanC58 {
    val Sky = Color(0xFFC8DCEA)
    val SkyLow = Color(0xFFF4EAD4)
    val Ground = Color(0xFFD2BC92)
    val Door = Color(0xFF8A4A3A)
    val Step = Color(0xFFE8DCC4)
    val Shade = Color(0xFFB8A888)
    val Person = Color(0xFF3A3040)
    val Gold = Color(0xFFD89A2A)
    val Bad = Color(0xFFB4492A)
    val Water = Color(0xFF5AA0D0)
    val Dust = Color(0xFFB89A72)
    val Ask = Color(0xFF6A5A7A)
}

/**
 * Beats: shutandclimb (on one side a door crossed shut, «تُجْتَنَبُ كُلُّهَا»; on the other, steps a figure climbs as high as
 * he can, «بِمَا يُسْتَطَاعُ»), sitpray (a figure who cannot stand prays sitting, and dust from the ground stands in for water:
 * «لَا وَاجِبَ مَعَ عَجْزٍ»), manyquestions (question marks pile up on a road until it is blocked), partwater (a little water
 * reaches some limbs; the rest is done with dust).
 */
internal fun DrawScope.canbearScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "shutandclimb" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "sitpray" to Shot(Offset(w * 0.5f, h * 0.65f), 1.15f),
        "manyquestions" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "partwater" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { canDay(a) }
}

private fun DrawScope.canDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sc = a.b("shutandclimb")
    val sp = a.b("sitpray")
    val mq = a.b("manyquestions")
    val pw = a.b("partwater")
    val ground = h * 0.84f
    daySky12(CanC58.Sky, CanC58.SkyLow, ground, CanC58.Ground)
    if (sc > 0f) {
        val f = easeOut(seg(sc, 0f, 0.25f)) * (1f - seg(sp, 0f, BEAT_OUT))
        val door = Offset(w * 0.8f, ground)
        drawRoundRect(CanC58.Door, Offset(door.x - w * 0.08f, door.y - h * 0.32f), Size(w * 0.16f, h * 0.32f), CornerRadius(6f * dp), alpha = f)
        crossMark(door + Offset(0f, -h * 0.16f), w * 0.09f, CanC58.Bad, 5f * dp, f * seg(sc, 0.1f, 0.25f))
        tagAt(a.tag("shutandclimb", 0, 0), door + Offset(0f, -h * 0.42f), f * seg(sc, 0.12f, 0.27f), back = Back58)
        for (k in 0 until 6) drawRect(CanC58.Step, Offset(w * (0.1f + 0.07f * k), ground - h * 0.06f * (k + 1)), Size(w * 0.07f, h * 0.06f * (k + 1)), alpha = f)
        val climb = easeInOut(seg(sc, 0.4f, 0.85f)) * 3.5f
        val step = climb.toInt().coerceAtMost(5)
        val fr = climb - step
        val foot = Offset(w * (0.135f + 0.07f * (step + fr)), ground - h * 0.06f * (step + 1) - h * 0.06f * fr)
        figure(foot, h * 0.22f, CanC58.Person, f, walk = t * 5f * (1f - seg(sc, 0.85f, 0.9f)))
        softGlow(foot + Offset(0f, -h * 0.12f), w * 0.06f, CanC58.Gold, 0.5f * f * seg(sc, 0.8f, 0.95f))
        tagAt(a.tag("shutandclimb", 0, 1), Offset(w * 0.3f, h * 0.2f), f * seg(sc, 0.45f, 0.6f), back = Back58, ink = ArtInk.Gold)
    }
    if (sp > 0f) {
        val f = easeOut(seg(sp, 0f, BEAT_IN)) * (1f - seg(mq, 0f, BEAT_OUT))
        sit12(Offset(w * 0.35f, ground), h * 0.32f, CanC58.Person, f, -1f)
        val g = seg(sp, 0.4f, 0.8f)
        for (k in 0 until 8) {
            val u = fract(t * 0.6f + k / 8f)
            drawCircle(CanC58.Dust, w * 0.005f, Offset(w * (0.7f + 0.02f * sin(k * 2f)), ground - h * 0.12f * u), alpha = f * g * (1f - u))
        }
        hands12(Offset(w * 0.7f, ground - h * 0.03f), h * 0.06f, CanC58.Person, f * g)
        tagAt(a.tag("sitpray"), Offset(w * 0.5f, h * 0.25f), f * seg(sp, 0.15f, 0.35f), back = Back58, ink = ArtInk.Gold)
    }
    if (mq > 0f) {
        val f = easeOut(seg(mq, 0f, BEAT_IN)) * (1f - seg(pw, 0f, BEAT_OUT))
        drawLine(CanC58.Shade, Offset(0f, ground - h * 0.02f), Offset(w, ground - h * 0.02f), h * 0.04f, alpha = f)
        for (k in 0 until 14) {
            val g = seg(mq, 0.05f + 0.05f * k, 0.15f + 0.05f * k)
            val c = Offset(w * (0.35f + 0.06f * (k % 5) + 0.02f * (k / 5)), ground - h * (0.07f + 0.09f * (k / 5)) - h * 0.2f * (1f - easeOut(g)))
            qMark14(c, w * 0.04f, CanC58.Ask, f * g)
        }
        figure(Offset(w * 0.15f, ground), h * 0.26f, CanC58.Person, f, walk = t * 4f * (1f - seg(mq, 0.6f, 0.7f)))
    }
    if (pw > 0f) {
        val f = easeOut(seg(pw, 0f, 0.25f))
        val foot = Offset(w * 0.5f, ground)
        figure(foot, h * 0.4f, CanC58.Person, f, arms = 0.4f)
        cup12(Offset(w * 0.25f, ground), w * 0.08f, CanC58.Step, f, CanC58.Water)
        val wet = seg(pw, 0.15f, 0.5f)
        for (p in listOf(Offset(-h * 0.13f, -h * 0.25f), Offset(h * 0.13f, -h * 0.25f), Offset(0f, -h * 0.36f))) softGlow(foot + p, w * 0.03f, CanC58.Water, f * wet)
        val dust = seg(pw, 0.55f, 0.9f)
        for (k in 0 until 10) {
            val u = fract(t * 0.5f + k / 10f)
            drawCircle(CanC58.Dust, w * 0.004f, foot + Offset(w * 0.04f * sin(k * 1.7f), -h * 0.2f * u), alpha = f * dust * (1f - u))
        }
    }
}

// ═════════════ 5. only the good (nw-10) ═════════════

private object GoodC58 {
    val Sky = Color(0xFF181A2E)
    val SkyLow = Color(0xFF3A3052)
    val Ground = Color(0xFF2A2430)
    val Gold = Color(0xFFF2C860)
    val Pure = Color(0xFFF4F0E4)
    val Dark = Color(0xFF3A3040)
    val Person = Color(0xFFDCD4C4)
    val Fruit = Color(0xFFC0602A)
    val Leaf = Color(0xFF6AAA6A)
    val Bad = Color(0xFFD06A4A)
}

/**
 * Beats: pureonly (white grains drift up through a gate of light; dark ones stop at it: «فَلَا يُتَقَرَّبُ إِلَيْهِ بِخَبِيثٍ»),
 * eatandwork (a tray of fruit; its strength goes into a figure who then bows: «مِنَ الطَّيِّبَاتِ»), blockeddua (a dusty
 * traveller raises his hands; dark threads from his food tie the light down: «فَأَنَّى يُسْتَجَابُ لِذَلِكَ»), fourcauses
 * (four lights rise, «السَّفَرُ», «وَالشَّعَثُ», «وَرَفْعُ الْيَدَيْنِ», «وَالْإِلْحَاحُ», and one dark bar stops them),
 * noghulul (two things crossed out: «بِغَيْرِ طُهُورٍ», «مِنْ غُلُولٍ»).
 */
internal fun DrawScope.goodonlyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "pureonly" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "eatandwork" to Shot(Offset(w * 0.5f, h * 0.65f), 1.1f),
        "blockeddua" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "fourcauses" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "noghulul" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { goodNight(a) }
}

private fun DrawScope.goodNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val po = a.b("pureonly")
    val ew = a.b("eatandwork")
    val bd = a.b("blockeddua")
    val fc = a.b("fourcauses")
    val ng = a.b("noghulul")
    val ground = h * 0.84f
    nightSky(t, GoodC58.Sky, GoodC58.SkyLow, ground, starAlpha = 0.8f)
    drawRect(GoodC58.Ground, Offset(0f, ground), Size(w, h - ground))
    if (po > 0f) {
        val f = easeOut(seg(po, 0f, BEAT_IN)) * (1f - seg(ew, 0f, BEAT_OUT))
        val gateY = h * 0.38f
        drawLine(GoodC58.Gold, Offset(w * 0.2f, gateY), Offset(w * 0.8f, gateY), 3f * dp, StrokeCap.Round, alpha = f * 0.8f)
        softGlow(Offset(w * 0.5f, gateY - h * 0.1f), w * 0.3f, GoodC58.Gold, 0.25f * f)
        for (k in 0 until 16) {
            val pure = k % 3 != 0
            val u = fract(t * 0.25f + k * 0.0625f)
            val x = w * (0.25f + 0.5f * fract(k * 0.37f))
            val y = if (pure) mix(ground, h * 0.1f, u) else mix(ground, gateY + h * 0.02f, minOf(u * 1.6f, 1f))
            drawCircle(if (pure) GoodC58.Pure else GoodC58.Dark, w * 0.008f, Offset(x, y), alpha = f * seg(po, 0.05f, 0.2f) * (if (pure) 1f - u else 1f))
        }
        tagAt(a.tag("pureonly"), Offset(w * 0.5f, h * 0.16f), f * seg(po, 0.5f, 0.7f), back = Back58, ink = ArtInk.Gold)
    }
    if (ew > 0f) {
        val f = easeOut(seg(ew, 0f, BEAT_IN)) * (1f - seg(bd, 0f, BEAT_OUT))
        drawOval(GoodC58.Pure, Offset(w * 0.12f, ground - h * 0.02f), Size(w * 0.3f, h * 0.05f), alpha = f * 0.6f)
        for (k in 0 until 5) drawCircle(if (k % 2 == 0) GoodC58.Fruit else GoodC58.Leaf, w * 0.018f, Offset(w * (0.18f + 0.045f * k), ground - h * 0.03f), alpha = f)
        val eat = seg(ew, 0.25f, 0.5f)
        softGlow(Offset(mix(w * 0.27f, w * 0.65f, eat), ground - h * 0.1f - h * 0.1f * eat), w * 0.03f, GoodC58.Gold, f * eat * (1f - seg(ew, 0.5f, 0.55f)))
        val deep = seg(ew, 0.55f, 0.8f) * 0.5f
        if (deep > 0.02f) bow12(Offset(w * 0.68f, ground), h * 0.34f, GoodC58.Person, f, deep) else figure(Offset(w * 0.68f, ground), h * 0.34f, GoodC58.Person, f)
        softGlow(Offset(w * 0.65f, ground - h * 0.25f), w * 0.08f, GoodC58.Gold, 0.4f * f * seg(ew, 0.6f, 0.9f))
        tagAt(a.tag("eatandwork"), Offset(w * 0.27f, h * 0.55f), f * seg(ew, 0.1f, 0.25f), back = Back58, ink = ArtInk.Gold)
    }
    if (bd > 0f) {
        val f = easeOut(seg(bd, 0f, BEAT_IN)) * (1f - seg(fc, 0f, BEAT_OUT))
        val foot = Offset(w * 0.5f, ground)
        figure(foot, h * 0.36f, GoodC58.Person, f, arms = 0.9f)
        for (k in 0 until 6) drawCircle(GoodC58.Dark, w * 0.01f, foot + Offset(w * (0.04f * sin(k * 2.1f + t)), -h * 0.36f * fract(k * 0.29f)), alpha = f * 0.6f)
        val rise = seg(bd, 0.2f, 0.5f)
        val tie = seg(bd, 0.5f, 0.75f)
        val glow = Offset(w * 0.5f, mix(ground - h * 0.45f, h * 0.15f, rise * (1f - 0.7f * tie)))
        softGlow(glow, w * 0.035f, GoodC58.Gold, f * rise)
        for (k in 0 until 3) {
            val from = foot + Offset(w * (0.08f * (k - 1)), 0f)
            drawLine(GoodC58.Dark, from, glow, 2f * dp, StrokeCap.Round, alpha = f * tie)
        }
        tagAt(a.tag("blockeddua"), Offset(w * 0.5f, h * 0.12f), f * seg(bd, 0.6f, 0.8f), back = Back58, ink = GoodC58.Bad)
    }
    if (fc > 0f) {
        val f = easeOut(seg(fc, 0f, BEAT_IN)) * (1f - seg(ng, 0f, BEAT_OUT))
        val bar = h * 0.3f
        for (k in 0 until 4) {
            val x = w * (0.8f - 0.2f * k)
            val g = seg(fc, 0.08f + 0.15f * k, 0.25f + 0.15f * k)
            val y = mix(ground - h * 0.05f, bar + h * 0.05f, easeOut(g))
            softGlow(Offset(x, y), w * 0.03f, GoodC58.Gold, f * g)
            tagAt(a.tag("fourcauses", 0, k), Offset(x, ground - h * 0.12f - h * 0.08f * (k % 2)), f * g, back = Back58, ink = ArtInk.Gold)
        }
        val b = seg(fc, 0.7f, 0.85f)
        drawRect(GoodC58.Dark, Offset(w * 0.08f, bar - h * 0.02f), Size(w * 0.84f * b, h * 0.04f), alpha = f)
    }
    if (ng > 0f) {
        val f = easeOut(seg(ng, 0f, 0.25f))
        for (k in 0 until 2) {
            val c = Offset(w * (0.7f - 0.4f * k), h * 0.5f)
            val g = seg(ng, 0.1f + 0.35f * k, 0.3f + 0.35f * k)
            if (k == 0) {
                drawRoundRect(GoodC58.Fruit, Offset(c.x - w * 0.06f, c.y - h * 0.1f), Size(w * 0.12f, h * 0.2f), CornerRadius(4f * dp), alpha = f * g * 0.7f)
                cup12(c + Offset(w * 0.1f, h * 0.1f), w * 0.05f, GoodC58.Pure, f * g)
            }
            else for (j in 0 until 4) coin12(c + Offset(w * (0.03f * (j % 2) - 0.015f), h * (0.08f - 0.03f * (j / 2))), w * 0.02f, f * g)
            crossMark(c, w * 0.11f, GoodC58.Bad, 5f * dp, f * seg(ng, 0.2f + 0.35f * k, 0.35f + 0.35f * k))
            struckTag21(a.tag("noghulul", 0, k), c + Offset(0f, -h * 0.22f), f * g, seg(ng, 0.25f + 0.35f * k, 0.4f + 0.35f * k), GoodC58.Bad, Back58, Color.White)
        }
    }
}

private fun easeIn58(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }
