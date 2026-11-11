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
 * Wave 66 (2026-11-03): «مختصر الشمائل» (2). His names and his years; how simply his household lived; how he spoke and smiled;
 * his jesting that was always true; his night prayer. The Prophet ﷺ is only ever his light with his name; companions and the
 * Mothers of the Believers are names in roundels; there are no figures in these scenes.
 */

private val Back66 = Color(0xE6181420)

// ═════════════ 1. his names and his years (sira-asma) ═════════════

private object NamesC66 {
    val Night = Color(0xFF0E1428)
    val NightLow = Color(0xFF2A3256)
    val Ground = Color(0xFF181C2C)
    val Gold = Color(0xFFF2C860)
    val Line = Color(0xFFD8CCB4)
    val Makka = Color(0xFFE8B84A)
    val Madina = Color(0xFF6FCB9F)
}

/**
 * Beats: threenames (the light ﷺ, and names come round it one by one: «مُحَمَّدٌ», «أَحْمَدُ», «الْمَاحِي»), lastnames (two more join
 * the ring: «الْحَاشِرُ», «الْعَاقِبُ»), yearsline (a line of years: a stretch for Makkah and one for Madinah, and the whole span:
 * «بِمَكَّةَ 13 سَنَةً», «وَبِالْمَدِينَةِ 10», «ابْنُ 63»), mercynames (three more names glow in the ring: «نَبِيُّ الرَّحْمَةِ», «وَنَبِيُّ
 * التَّوْبَةِ», «الْمُقَفِّي»), atforty (on the line of years a mark lights at the start of the mission: «40 سَنَةً»).
 */
internal fun DrawScope.namesringScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threenames" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "lastnames" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "yearsline" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "mercynames" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "atforty" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { namesNight(a) }
}

private fun DrawScope.namesNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tn = a.b("threenames")
    val ln = a.b("lastnames")
    val yl = a.b("yearsline")
    val mn = a.b("mercynames")
    val af = a.b("atforty")
    val ground = h * 0.88f
    nightSky(t, NamesC66.Night, NamesC66.NightLow, ground, starAlpha = 0.9f)
    drawRect(NamesC66.Ground, Offset(0f, ground), Size(w, h - ground))
    val center = Offset(w * 0.5f, h * 0.48f)
    val ringAt = { k: Int, n: Int -> val ang = -PI.toFloat() / 2f + 2f * PI.toFloat() * k / n; center + Offset(cos(ang) * w * 0.34f, sin(ang) * h * 0.3f) }
    if (tn > 0f || ln > 0f) {
        val f = easeOut(seg(tn, 0f, 0.25f)) * (1f - seg(yl, 0f, BEAT_OUT))
        sirajMunir(center, w * 0.014f, f, t, named = false)
        for (k in 0 until 3) tagAt(a.tag("threenames", 0, k), ringAt(k, 5), f * seg(tn, 0.1f + 0.25f * k, 0.3f + 0.25f * k), back = Back66, ink = ArtInk.Gold)
        for (k in 0 until 2) tagAt(a.tag("lastnames", 0, k), ringAt(3 + k, 5), f * seg(ln, 0.1f + 0.35f * k, 0.3f + 0.35f * k), back = Back66, ink = ArtInk.Gold)
    }
    val line = { f: Float, p: Float ->
        val y = h * 0.6f
        drawLine(NamesC66.Line, Offset(w * 0.92f, y), Offset(w * 0.92f - w * 0.84f * p, y), 3f * dp, StrokeCap.Round, alpha = f)
        y
    }
    if (yl > 0f || af > 0f) {
        val f = (easeOut(seg(yl, 0f, BEAT_IN)) * (1f - seg(mn, 0f, BEAT_OUT)) + easeOut(seg(af, 0f, 0.25f))).coerceIn(0f, 1f)
        val y = line(f, if (af > 0f) 1f else seg(yl, 0.05f, 0.25f))
        val x0 = w * 0.92f
        val span = w * 0.84f
        val x40 = x0 - span * 40f / 63f
        val x53 = x0 - span * 53f / 63f
        val g1 = if (af > 0f) 1f else seg(yl, 0.2f, 0.45f)
        val g2 = if (af > 0f) 1f else seg(yl, 0.45f, 0.7f)
        drawLine(NamesC66.Makka, Offset(x40, y), Offset(x40 + (x53 - x40) * g1, y), 8f * dp, StrokeCap.Round, alpha = f)
        drawLine(NamesC66.Madina, Offset(x53, y), Offset(x53 + (w * 0.08f - x53) * g2, y), 8f * dp, StrokeCap.Round, alpha = f)
        if (yl > 0f && af <= 0f) {
            tagAt(a.tag("yearsline", 0, 0), Offset((x40 + x53) / 2f, y - h * 0.1f), f * g1, back = Back66, ink = NamesC66.Makka)
            tagAt(a.tag("yearsline", 0, 1), Offset((x53 + w * 0.08f) / 2f, y + h * 0.1f), f * g2, back = Back66, ink = NamesC66.Madina)
            // time runs right to left: an arrowhead at the end, and «ابن 63» on the marker where the line ends
            val end = Offset(w * 0.08f, y)
            val m = f * seg(yl, 0.7f, 0.85f)
            drawPath(Path().apply { moveTo(end.x - w * 0.02f, y); lineTo(end.x + w * 0.005f, y - h * 0.025f); lineTo(end.x + w * 0.005f, y + h * 0.025f); close() }, NamesC66.Line, alpha = f * seg(yl, 0.2f, 0.3f))
            drawLine(NamesC66.Gold, end + Offset(w * 0.012f, -h * 0.07f), end + Offset(w * 0.012f, h * 0.05f), 3f * dp, StrokeCap.Round, alpha = m)
            softGlow(end, w * 0.05f, NamesC66.Gold, 0.5f * m)
            tagAt(a.tag("yearsline", 0, 2), end + Offset(w * 0.03f, -h * 0.22f), m, back = Back66, ink = ArtInk.Gold)
        }
        if (af > 0f) {
            val g = seg(af, 0.15f, 0.4f)
            softGlow(Offset(x40, y), w * 0.06f, NamesC66.Gold, f * g)
            drawLine(NamesC66.Gold, Offset(x40, y - h * 0.06f), Offset(x40, y + h * 0.06f), 3f * dp, StrokeCap.Round, alpha = f * g)
            tagAt(a.tag("atforty"), Offset(x40, y - h * 0.14f), f * seg(af, 0.3f, 0.5f), back = Back66, ink = ArtInk.Gold)
        }
    }
    if (mn > 0f) {
        val f = easeOut(seg(mn, 0f, BEAT_IN)) * (1f - seg(af, 0f, BEAT_OUT))
        sirajMunir(center, w * 0.014f, f, t, named = false)
        for (k in 0 until 3) tagAt(a.tag("mercynames", 0, k), ringAt(k, 3), f * seg(mn, 0.1f + 0.25f * k, 0.3f + 0.25f * k), back = Back66, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. how simply they lived (sira-aysh) ═════════════

private object LivingC66 {
    val Wall = Color(0xFF3A3038)
    val WallLow = Color(0xFF5A4640)
    val Floor = Color(0xFF2E2620)
    val Ash = Color(0xFF6A6A6A)
    val Stone = Color(0xFF8A7A6A)
    val Date = Color(0xFF8A3A1A)
    val Water = Color(0xFF8AC4EA)
    val Bread = Color(0xFFC8A070)
    val Plate = Color(0xFFD8C4A0)
    val Gold = Color(0xFFF2C860)
    val Palm = Color(0xFF3E6A44)
    val Trunk = Color(0xFF6A4A32)
    val Milk = Color(0xFFF6F2E8)
    val House = Color(0xFFC8B494)
    val Chaff = Color(0xFFE8D8A8)
}

/**
 * Beats: coldhearth (a hearth with no fire while the moon goes through a month; only dates and water: «التَّمْرُ وَالْمَاءُ»),
 * barleynights (night after night an empty plate; then a loaf of barley: «خُبْزَ الشَّعِيرِ»), coolshade (the light ﷺ and two plain
 * roundels in the shade of a palm with fresh dates and cool water, and a roundel with his name, «أَبِي الْهَيْثَمِ»: «هَذَا مِنَ
 * النَّعِيمِ الَّذِي تُسْأَلُونَ عَنْهُ»), milkfromneighbors (cups of milk come from the neighbours' houses), chaffblown (barley is
 * blown on and the husks fly off).
 */
internal fun DrawScope.simplelivingScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "coldhearth" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "barleynights" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "coolshade" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "milkfromneighbors" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
        "chaffblown" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { livingRoom(a) }
}

private fun DrawScope.livingRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ch = a.b("coldhearth")
    val bn = a.b("barleynights")
    val cs = a.b("coolshade")
    val mf = a.b("milkfromneighbors")
    val cb = a.b("chaffblown")
    val floor = h * 0.8f
    val outdoor = cs > 0f && mf <= 0f
    if (outdoor) daySky12(Color(0xFFC6DCEA), Color(0xFFF2E6CC), floor, Color(0xFFD2BC92))
    else {
        drawRect(Brush.verticalGradient(listOf(LivingC66.Wall, LivingC66.WallLow), 0f, floor), Offset.Zero, Size(w, floor))
        drawRect(LivingC66.Floor, Offset(0f, floor), Size(w, h - floor))
    }
    if (ch > 0f) {
        val f = easeOut(seg(ch, 0f, BEAT_IN)) * (1f - seg(bn, 0f, BEAT_OUT))
        for (k in 0 until 3) drawCircle(LivingC66.Stone, w * 0.03f, Offset(w * (0.4f + 0.06f * k), floor + h * 0.02f), alpha = f)
        drawOval(LivingC66.Ash, Offset(w * 0.42f, floor - h * 0.01f), Size(w * 0.12f, h * 0.03f), alpha = f * 0.6f)
        val cyc = seg(ch, 0.05f, 0.9f)
        moon12(Offset(w * (0.15f + 0.7f * cyc), h * 0.18f), w * 0.035f * (0.3f + 0.7f * sin(PI.toFloat() * cyc)), f)
        for (k in 0 until 5) drawOval(LivingC66.Date, Offset(w * (0.2f + 0.025f * k), floor + h * 0.06f), Size(w * 0.02f, h * 0.03f), alpha = f)
        cup12(Offset(w * 0.75f, floor + h * 0.1f), w * 0.07f, LivingC66.Plate, f, LivingC66.Water)
        tagAt(a.tag("coldhearth"), Offset(w * 0.5f, h * 0.4f), f * seg(ch, 0.4f, 0.6f), back = Back66, ink = ArtInk.Gold)
    }
    if (bn > 0f) {
        val f = easeOut(seg(bn, 0f, BEAT_IN)) * (1f - seg(cs, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val g = seg(bn, 0.05f + 0.15f * k, 0.15f + 0.15f * k)
            moon12(Offset(w * (0.8f - 0.25f * k), h * 0.18f), w * 0.025f, f * g * (1f - seg(bn, 0.2f + 0.15f * k, 0.3f + 0.15f * k)))
        }
        drawOval(LivingC66.Plate, Offset(w * 0.38f, floor + h * 0.03f), Size(w * 0.24f, h * 0.05f), alpha = f)
        val bread = seg(bn, 0.6f, 0.8f)
        drawOval(LivingC66.Bread, Offset(w * 0.43f, floor + h * 0.015f), Size(w * 0.14f, h * 0.045f), alpha = f * bread)
        for (k in 0 until 8) drawCircle(LivingC66.Floor, w * 0.003f, Offset(w * (0.45f + 0.012f * k), floor + h * (0.03f + 0.008f * (k % 2))), alpha = f * bread)
        tagAt(a.tag("barleynights"), Offset(w * 0.5f, h * 0.45f), f * seg(bn, 0.6f, 0.8f), back = Back66, ink = ArtInk.Gold)
    }
    if (cs > 0f) {
        val f = easeOut(seg(cs, 0f, BEAT_IN)) * (1f - seg(mf, 0f, BEAT_OUT))
        palm12(Offset(w * 0.5f, floor), h * 0.62f, LivingC66.Trunk, LivingC66.Palm, f, t)
        drawOval(Color(0x33000000), Offset(w * 0.2f, floor - h * 0.01f), Size(w * 0.6f, h * 0.06f), alpha = f)
        val light = Offset(w * 0.5f, floor - h * 0.12f)
        sirajMunir(light, w * 0.01f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        nameRoundel(Offset(w * 0.32f, floor - h * 0.06f), w * 0.028f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(Offset(w * 0.68f, floor - h * 0.06f), w * 0.028f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(Offset(w * 0.85f, floor - h * 0.2f), w * 0.07f, a.big("coolshade", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(cs, 0.05f, 0.25f))
        for (k in 0 until 5) drawOval(Color(0xFFD0A040), Offset(w * (0.42f + 0.03f * k), floor + h * 0.03f), Size(w * 0.02f, h * 0.025f), alpha = f)
        cup12(Offset(w * 0.6f, floor + h * 0.08f), w * 0.06f, LivingC66.Plate, f, LivingC66.Water)
        tagAt(a.tag("coolshade", 0, 1), Offset(w * 0.5f, h * 0.92f), f * seg(cs, 0.5f, 0.7f), back = Back66, ink = ArtInk.Gold)
    }
    if (mf > 0f) {
        val f = easeOut(seg(mf, 0f, BEAT_IN)) * (1f - seg(cb, 0f, BEAT_OUT))
        house12(Offset(w * 0.5f, floor), w * 0.22f, LivingC66.House, LivingC66.Trunk, f, 0.6f)
        for (k in 0 until 2) {
            val side = if (k == 0) -1f else 1f
            val n = Offset(w * 0.5f + side * w * 0.36f, floor)
            house12(n, w * 0.16f, LivingC66.House, LivingC66.Trunk, f, 0.4f)
            val u = easeInOut(seg(mf, 0.2f + 0.3f * k, 0.5f + 0.3f * k))
            cup12(Offset(mix(n.x, w * 0.5f + side * w * 0.08f, u), floor - h * 0.05f - h * 0.08f * sin(PI.toFloat() * u)), w * 0.05f, LivingC66.Plate, f, LivingC66.Milk)
        }
    }
    if (cb > 0f) {
        val f = easeOut(seg(cb, 0f, 0.25f))
        val c = Offset(w * 0.5f, floor + h * 0.02f)
        drawOval(LivingC66.Plate, Offset(c.x - w * 0.14f, c.y - h * 0.03f), Size(w * 0.28f, h * 0.06f), alpha = f)
        for (k in 0 until 14) drawOval(LivingC66.Bread, Offset(c.x - w * 0.1f + w * 0.015f * k, c.y - h * 0.02f), Size(w * 0.012f, h * 0.008f), alpha = f)
        val blow = seg(cb, 0.2f, 0.9f)
        for (k in 0 until 3) drawLine(LivingC66.Milk, Offset(w * 0.9f, c.y - h * 0.08f - h * 0.02f * k), Offset(w * 0.75f, c.y - h * 0.06f - h * 0.02f * k), 1.5f * dp, StrokeCap.Round, alpha = f * 0.5f * blow)
        for (k in 0 until 12) {
            val u = fract(t * 0.6f + k / 12f) * blow
            drawOval(LivingC66.Chaff, Offset(c.x - w * 0.05f - w * 0.4f * u + w * 0.08f * sin(k * 1.3f), c.y - h * 0.05f - h * 0.3f * u * (1f - u) * 2f), Size(w * 0.012f, h * 0.005f), alpha = f * (1f - u))
        }
    }
}

// ═════════════ 3. his speech and his smile (sira-kalam) ═════════════

private object SpeechC66 {
    val Night = Color(0xFF14182C)
    val NightLow = Color(0xFF34304E)
    val Ground = Color(0xFF1E1E2C)
    val Gold = Color(0xFFF2C860)
    val Warm = Color(0xFFF4D890)
    val Door = Color(0xFF8A5A3A)
    val Frame = Color(0xFF5A3A22)
    val Camel = Color(0xFFB88A58)
}

/**
 * Beats: spacedwords (the light ﷺ speaks; words come one at a time with space between, and roundels around keep each one),
 * threetimesword (the same word comes three times, each softer glow kept), warmsmile (warm rings of light open out around the light ﷺ, and a
 * roundel with his name: «عَبْدُ اللَّهِ بْنُ الْحَارِثِ», «أَكْثَرَ تَبَسُّمًا»), neverbarred (a door that is never closed to a roundel
 * with his name, «جَرِيرُ بْنُ عَبْدِ اللَّهِ», and warm light each time), aliriding (a roundel with his name, «عَلِيٌّ», on a mount; a
 * light of remembrance and then a warm glow).
 */
internal fun DrawScope.clearspeechScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "spacedwords" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "threetimesword" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "warmsmile" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "neverbarred" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "aliriding" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { speechNight(a) }
}

private fun DrawScope.speechNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sw = a.b("spacedwords")
    val tt = a.b("threetimesword")
    val ws = a.b("warmsmile")
    val nb = a.b("neverbarred")
    val ar = a.b("aliriding")
    val ground = h * 0.86f
    nightSky(t, SpeechC66.Night, SpeechC66.NightLow, ground, starAlpha = 0.85f)
    drawRect(SpeechC66.Ground, Offset(0f, ground), Size(w, h - ground))
    val light = Offset(w * 0.5f, h * 0.3f)
    if (sw > 0f || tt > 0f) {
        val f = (easeOut(seg(sw, 0f, BEAT_IN)) * (1f - seg(tt, 0f, BEAT_OUT)) + easeOut(seg(tt, 0f, BEAT_IN)) * (1f - seg(ws, 0f, BEAT_OUT))).coerceIn(0f, 1f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        if (tt <= 0f) {
            for (k in 0 until 5) {
                val g = seg(sw, 0.1f + 0.16f * k, 0.2f + 0.16f * k)
                val c = Offset(w * (0.82f - 0.16f * k), h * 0.55f)
                drawRoundRect(SpeechC66.Gold, Offset(c.x - w * 0.05f, c.y - h * 0.02f), Size(w * 0.1f, h * 0.04f), CornerRadius(h * 0.02f), alpha = f * g * 0.7f)
                nameRoundel(c + Offset(0f, h * 0.15f), w * 0.025f, null, Roundel.PlainFill, Roundel.PlainRim, f)
                softGlow(c + Offset(0f, h * 0.15f), w * 0.04f, SpeechC66.Gold, 0.5f * f * g)
            }
            // each word on its own, and those sitting with him keep it
            tagAt(a.tag("spacedwords", 0, 0), Offset(w * 0.5f, h * 0.42f), f * seg(sw, 0.3f, 0.5f), back = Back66, ink = ArtInk.Gold)
            tagAt(a.tag("spacedwords", 0, 1), Offset(w * 0.5f, h * 0.84f), f * seg(sw, 0.6f, 0.8f), back = Back66, ink = Color(0xFFF0ECE4))
        } else {
            for (k in 0 until 3) {
                val g = seg(tt, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
                val c = Offset(w * 0.5f, h * (0.5f + 0.1f * k))
                drawRoundRect(SpeechC66.Gold, Offset(c.x - w * 0.1f, c.y - h * 0.025f), Size(w * 0.2f, h * 0.05f), CornerRadius(h * 0.025f), alpha = f * g * (0.9f - 0.2f * k))
            }
        }
    }
    if (ws > 0f) {
        val f = easeOut(seg(ws, 0f, BEAT_IN)) * (1f - seg(nb, 0f, BEAT_OUT))
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        warmRings66(light, w * 0.06f, w * 0.2f, f * seg(ws, 0.15f, 0.35f), t)
        nameRoundel(Offset(w * 0.5f, h * 0.68f), w * 0.09f, a.big("warmsmile", 0, 0), Roundel.Fills[1], Roundel.Rim, f * seg(ws, 0.05f, 0.25f))
        tagAt(a.tag("warmsmile", 0, 1), Offset(w * 0.5f, h * 0.9f), f * seg(ws, 0.45f, 0.65f), back = Back66, ink = ArtInk.Gold)
    }
    if (nb > 0f) {
        val f = easeOut(seg(nb, 0f, BEAT_IN)) * (1f - seg(ar, 0f, BEAT_OUT))
        val sill = Offset(w * 0.65f, ground)
        door12(sill, w * 0.16f, h * 0.36f, 0.85f, SpeechC66.Frame, SpeechC66.Door, f)
        softGlow(sill + Offset(0f, -h * 0.18f), w * 0.12f, SpeechC66.Warm, 0.6f * f * (0.7f + 0.3f * sin(t * 2f)))
        val come = easeInOut(seg(nb, 0.1f, 0.5f))
        nameRoundel(Offset(mix(w * 0.15f, w * 0.38f, come), h * 0.6f), w * 0.08f, a.big("neverbarred"), Roundel.Fills[2], Roundel.Rim, f)
        warmRings66(Offset(w * 0.38f, h * 0.6f), w * 0.09f, w * 0.2f, f * seg(nb, 0.5f, 0.7f), t)
        tagAt(a.tag("neverbarred", 0, 1), sill + Offset(0f, -h * 0.44f), f * seg(nb, 0.3f, 0.5f), back = Back66, ink = ArtInk.Gold)
        tagAt(a.tag("neverbarred", 0, 2), Offset(w * 0.38f, h * 0.82f), f * seg(nb, 0.55f, 0.75f), back = Back66, ink = ArtInk.Gold)
    }
    if (ar > 0f) {
        val f = easeOut(seg(ar, 0f, 0.25f))
        val foot = Offset(w * 0.5f, ground)
        camel26(foot, h * 0.36f, SpeechC66.Camel, f, t)
        nameRoundel(foot + Offset(0f, -h * 0.42f), w * 0.07f, a.big("aliriding"), Roundel.Fills[0], Roundel.Rim, f * seg(ar, 0.05f, 0.2f))
        softGlow(foot + Offset(0f, -h * 0.42f), w * 0.12f, SpeechC66.Gold, 0.5f * f * seg(ar, 0.3f, 0.5f))
        warmRings66(foot + Offset(0f, -h * 0.42f), w * 0.08f, w * 0.2f, f * seg(ar, 0.6f, 0.8f), t)
    }
}

// ═════════════ 4. his jesting, always true (sira-mizah) ═════════════

private object JestC66 {
    val Sky = Color(0xFFC6DCEA)
    val SkyLow = Color(0xFFF4E8D2)
    val Ground = Color(0xFFD2BC92)
    val Gold = Color(0xFFD89A2A)
    val Spark = Color(0xFFE8A85A)
    val Bird = Color(0xFFC0603A)
    val Camel = Color(0xFFB88A58)
    val Calf = Color(0xFFD0A878)
    val Stall = Color(0xFFB04A3A)
    val Pole = Color(0xFF6A4A32)
    val Coin = Color(0xFFE8C25A)
    val Green = Color(0xFF4E8A54)
}

/**
 * Beats: truejoke (playful sparkles dance around a straight gold line that does not bend: «لَا أَقُولُ إِلَّا حَقًّا»), littlebird
 * (a small roundel and a little bird hopping beside it: «يَا أَبَا عُمَيْرٍ، مَا فَعَلَ النُّغَيْرُ»), everycamel (a great camel walks
 * up beside its mother: «وَهَلْ تَلِدُ الْإِبِلُ إِلَّا النُّوقَ»), twoears (a roundel and soft sound waves coming to it from both sides: «يَا ذَا
 * الْأُذُنَيْنِ»), notcheap (in a market, a roundel with his name, «زَاهِرٌ», is set on a scale and outweighs the coins: «لَكِنْ عِنْدَ
 * اللَّهِ لَسْتَ بِكَاسِدٍ»), youngagain (an old roundel grows bright and young).
 */
internal fun DrawScope.truejestScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "truejoke" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "littlebird" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "everycamel" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
        "twoears" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "notcheap" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "youngagain" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { jestDay(a) }
}

private fun DrawScope.jestDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tj = a.b("truejoke")
    val lb = a.b("littlebird")
    val ec = a.b("everycamel")
    val te = a.b("twoears")
    val nc = a.b("notcheap")
    val ya = a.b("youngagain")
    val ground = h * 0.84f
    daySky12(JestC66.Sky, JestC66.SkyLow, ground, JestC66.Ground)
    if (tj > 0f) {
        val f = easeOut(seg(tj, 0f, BEAT_IN)) * (1f - seg(lb, 0f, BEAT_OUT))
        drawLine(JestC66.Gold, Offset(w * 0.1f, h * 0.5f), Offset(w * 0.1f + w * 0.8f * seg(tj, 0.05f, 0.35f), h * 0.5f), 4f * dp, StrokeCap.Round, alpha = f)
        for (k in 0 until 14) {
            val x = w * (0.12f + 0.055f * k)
            val y = h * 0.5f + sin(t * 4f + k * 0.9f) * h * 0.08f
            starPath(Offset(x, y), w * 0.01f).let { drawPath(it, JestC66.Spark, alpha = f * seg(tj, 0.2f + 0.02f * k, 0.3f + 0.02f * k)) }
        }
        tagAt(a.tag("truejoke"), Offset(w * 0.5f, h * 0.25f), f * seg(tj, 0.45f, 0.65f), back = Back66, ink = ArtInk.Gold)
        tagAt(a.tag("truejoke", 0, 1), Offset(w * 0.5f, h * 0.5f), f * seg(tj, 0.3f, 0.5f), back = Back66, ink = ArtInk.Gold)
        tagAt(a.tag("truejoke", 0, 2), Offset(w * 0.5f, h * 0.72f), f * seg(tj, 0.15f, 0.35f), back = Back66, ink = Color(0xFFF0ECE4))
    }
    if (lb > 0f) {
        val f = easeOut(seg(lb, 0f, BEAT_IN)) * (1f - seg(ec, 0f, BEAT_OUT))
        val kid = Offset(w * 0.6f, ground - h * 0.06f)
        nameRoundel(kid, w * 0.07f, a.tag("littlebird", 0, 1), Roundel.PlainFill, Roundel.PlainRim, f)
        sirajMunir(Offset(w * 0.84f, h * 0.4f), w * 0.012f, f, t, nameAt = Offset(w * 0.8f, h * 0.53f))
        val hop = kotlin.math.abs(sin(t * 4f)) * h * 0.03f
        val b = Offset(w * 0.4f, ground - h * 0.03f - hop)
        drawOval(JestC66.Bird, Offset(b.x - w * 0.025f, b.y - h * 0.015f), Size(w * 0.05f, h * 0.03f), alpha = f)
        drawCircle(JestC66.Bird, w * 0.012f, b + Offset(-w * 0.025f, -h * 0.015f), alpha = f)
        drawPath(Path().apply { moveTo(b.x - w * 0.037f, b.y - h * 0.017f); lineTo(b.x - w * 0.05f, b.y - h * 0.012f); lineTo(b.x - w * 0.037f, b.y - h * 0.01f); close() }, JestC66.Gold, alpha = f)
        drawLine(JestC66.Bird, b + Offset(w * 0.02f, 0f), b + Offset(w * 0.04f, -h * 0.015f), 3f * dp, StrokeCap.Round, alpha = f)
        tagAt(a.tag("littlebird"), Offset(w * 0.5f, h * 0.3f), f * seg(lb, 0.3f, 0.5f), back = Back66, ink = ArtInk.Gold)
    }
    if (ec > 0f) {
        val f = easeOut(seg(ec, 0f, BEAT_IN)) * (1f - seg(te, 0f, BEAT_OUT))
        val walk = easeInOut(seg(ec, 0.05f, 0.6f))
        camel26(Offset(mix(w * 0.95f, w * 0.65f, walk), ground), h * 0.42f, JestC66.Camel, f, t, t * 3f * (1f - walk))
        camel26(Offset(mix(w * 1.15f, w * 0.35f, walk), ground), h * 0.42f, JestC66.Calf, f, t, t * 3f * (1f - walk))
        tagAt(a.tag("everycamel"), Offset(w * 0.5f, h * 0.18f), f * seg(ec, 0.5f, 0.7f), back = Back66, ink = ArtInk.Gold)
    }
    if (te > 0f) {
        val f = easeOut(seg(te, 0f, BEAT_IN)) * (1f - seg(nc, 0f, BEAT_OUT))
        val c = Offset(w * 0.4f, h * 0.55f)
        nameRoundel(c, w * 0.09f, a.tag("twoears", 0, 1), Roundel.PlainFill, Roundel.PlainRim, f)
        val light = Offset(w * 0.82f, h * 0.4f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(-w * 0.02f, h * 0.13f))
        // two ears on the roundel: the jest is simply true
        for (sd in listOf(-1f, 1f)) drawArc(Roundel.PlainRim, if (sd < 0) 90f else -90f, 180f, false, Offset(c.x + sd * w * 0.09f - w * 0.02f, c.y - h * 0.035f), Size(w * 0.04f, h * 0.07f), alpha = f * seg(te, 0.25f, 0.45f), style = Stroke(3f * dp, cap = StrokeCap.Round))
        val g = seg(te, 0.2f, 0.45f)
        for (side in listOf(-1f, 1f)) for (k in 0 until 3) {
            val u = fract(t * 0.6f + k / 3f)
            val r = w * (0.22f - 0.08f * u)
            drawArc(JestC66.Spark, if (side < 0) 150f else -30f, 60f, false, Offset(c.x - r, c.y - r), Size(r * 2f, r * 2f), alpha = f * g * u, style = Stroke(3f * dp, cap = StrokeCap.Round))
        }
        tagAt(a.tag("twoears"), Offset(w * 0.62f, h * 0.24f), f * seg(te, 0.3f, 0.5f), back = Back66, ink = ArtInk.Gold)
    }
    if (nc > 0f) {
        val f = easeOut(seg(nc, 0f, BEAT_IN)) * (1f - seg(ya, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val x = w * (0.15f + 0.35f * k)
            drawLine(JestC66.Pole, Offset(x, ground), Offset(x, ground - h * 0.3f), 3f * dp, alpha = f * 0.8f)
            drawPath(Path().apply { moveTo(x - w * 0.12f, ground - h * 0.3f); lineTo(x + w * 0.12f, ground - h * 0.3f); lineTo(x + w * 0.1f, ground - h * 0.38f); lineTo(x - w * 0.1f, ground - h * 0.38f); close() }, if (k % 2 == 0) JestC66.Stall else JestC66.Green, alpha = f * 0.8f)
        }
        val tilt = mix(-0.3f, 0.5f, easeInOut(seg(nc, 0.4f, 0.75f)))
        val foot = Offset(w * 0.5f, ground)
        balance12(foot, h * 0.5f, tilt, JestC66.Pole, f)
        val (lp, rp) = balancePans12(foot, h * 0.5f, tilt)
        nameRoundel(lp + Offset(0f, -h * 0.06f), w * 0.06f, a.big("notcheap", 0, 0), Roundel.Fills[3], Roundel.Rim, f * seg(nc, 0.1f, 0.25f))
        for (k in 0 until 6) coin12(rp + Offset(w * (0.02f * (k % 3) - 0.02f), -h * (0.02f + 0.02f * (k / 3))), w * 0.014f, f, JestC66.Coin)
        tagAt(a.tag("notcheap", 0, 1), Offset(w * 0.5f, h * 0.12f), f * seg(nc, 0.6f, 0.8f), back = Back66, ink = ArtInk.Gold)
    }
    if (ya > 0f) {
        val f = easeOut(seg(ya, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.5f)
        val young = easeInOut(seg(ya, 0.35f, 0.75f))
        nameRoundel(c, w * (0.07f + 0.02f * young), null, Roundel.PlainFill, lerpColor(Color(0xFF9A9A9A), Roundel.Rim, young), f)
        softGlow(c, w * 0.2f, JestC66.Gold, 0.6f * f * young)
        for (k in 0 until 8) {
            val ang = k * PI.toFloat() / 4f + t * 0.3f
            starPath(c + Offset(cos(ang), sin(ang)) * (w * 0.16f), w * 0.012f).let { drawPath(it, JestC66.Spark, alpha = f * young) }
        }
        tagAt(a.tag("youngagain", 0, 0), c + Offset(0f, h * 0.2f), f * (1f - young), back = Back66, ink = Color(0xFFC8C8C8))
        tagAt(a.tag("youngagain", 0, 1), c + Offset(0f, h * 0.2f), f * young, back = Back66, ink = ArtInk.Gold)
        tagAt(a.tag("youngagain", 0, 2), Offset(w * 0.5f, h * 0.14f), f * seg(ya, 0.1f, 0.3f) * (1f - seg(ya, 0.4f, 0.55f)), back = Back66, ink = Color(0xFFF0ECE4))
        tagAt(a.tag("youngagain", 0, 3), Offset(w * 0.5f, h * 0.14f), f * seg(ya, 0.55f, 0.75f), back = Back66, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. a grateful servant (sira-ibada) ═════════════

private object NightPrayC66 {
    val Night = Color(0xFF0C1022)
    val NightLow = Color(0xFF242C4C)
    val Ground = Color(0xFF161A28)
    val Gold = Color(0xFFF2C860)
    val Scroll = Color(0xFFF2E8D2)
    val Lamp = Color(0xFFB8925A)
    val Dim = Color(0xFF5A5A6A)
    val Water = Color(0xFF8AC4EA)
    val Jar = Color(0xFFB88A58)
    val Line = Color(0xFF4A5070)
}

/**
 * Beats: thankfulnight (the light ﷺ stands through a whole night as the stars wheel over: «أَفَلَا أَكُونُ عَبْدًا شَكُورًا»),
 * threesuras (a roundel with his name, «حُذَيْفَةُ», beside the light; three long scrolls unroll in turn: «الْبَقَرَةَ», «وَآلَ
 * عِمْرَانَ», «وَالنِّسَاءَ»), longstanding (a roundel with his name, «ابْنُ مَسْعُودٍ», sways with tiredness while the light stands
 * steady), nightplan (a band of night: sleep in its first part, then standing, then witr at its end), heartawake (two small lamps are
 * dimmed for sleep while one in the middle, a heart, stays lit: «إِنَّ عَيْنَيَّ تَنَامَانِ وَلَا يَنَامُ قَلْبِي»), steadydrops (a jar
 * fills drop by drop: «مَا دِيمَ عَلَيْهِ وَإِنْ قَلَّ»).
 */
internal fun DrawScope.gratefulservantScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "thankfulnight" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "threesuras" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "longstanding" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "nightplan" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "heartawake" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "steadydrops" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { nightPray(a) }
}

private fun DrawScope.nightPray(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tn = a.b("thankfulnight")
    val ts = a.b("threesuras")
    val ls = a.b("longstanding")
    val np = a.b("nightplan")
    val ha = a.b("heartawake")
    val sd = a.b("steadydrops")
    val ground = h * 0.86f
    nightSky(t, NightPrayC66.Night, NightPrayC66.NightLow, ground, starAlpha = 1f)
    drawRect(NightPrayC66.Ground, Offset(0f, ground), Size(w, h - ground))
    if (tn > 0f) {
        val f = easeOut(seg(tn, 0f, 0.25f)) * (1f - seg(ts, 0f, BEAT_OUT))
        val light = Offset(w * 0.5f, ground - h * 0.3f)
        sirajMunir(light, w * 0.013f, f, t, nameAt = light + Offset(0f, -h * 0.12f))
        val sweep = seg(tn, 0.05f, 0.9f)
        // the prayer mat under the light: he stands the night long while the moon crosses the sky
        drawRoundRect(Color(0xFF2E5A4A), Offset(light.x - w * 0.09f, ground - h * 0.015f), Size(w * 0.18f, h * 0.03f), CornerRadius(3f * dp), alpha = f)
        tagAt(a.tag("thankfulnight", 0, 1), Offset(w * 0.2f, h * 0.5f), f * seg(tn, 0.15f, 0.35f), back = Back66, ink = Color(0xFFDCE8F0))
        for (k in 0 until 12) {
            val ang = PI.toFloat() * (1.05f + 0.9f * fract(k * 0.137f + sweep * 0.5f))
            val r = w * (0.4f + 0.04f * (k % 3))
            drawCircle(NightPrayC66.Scroll, w * 0.004f, Offset(w * 0.5f + cos(ang) * r, ground + sin(ang) * r), alpha = f * 0.8f)
        }
        moon12(Offset(w * (0.85f - 0.7f * sweep), h * 0.2f + h * 0.1f * (1f - sin(PI.toFloat() * sweep))), w * 0.03f, f)
        tagAt(a.tag("thankfulnight"), Offset(w * 0.5f, ground + h * 0.06f), f * seg(tn, 0.5f, 0.7f), back = Back66, ink = ArtInk.Gold)
    }
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, BEAT_IN)) * (1f - seg(ls, 0f, BEAT_OUT))
        val light = Offset(w * 0.68f, h * 0.3f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        nameRoundel(Offset(w * 0.3f, h * 0.3f), w * 0.08f, a.big("threesuras", 0, 0), Roundel.Fills[1], Roundel.Rim, f * seg(ts, 0.05f, 0.2f))
        for (k in 0 until 3) {
            val g = seg(ts, 0.2f + 0.22f * k, 0.4f + 0.22f * k)
            val c = Offset(w * (0.78f - 0.28f * k), h * 0.66f)
            scroll14(c, w * 0.24f, h * 0.3f * (1f + 0.15f * k), easeOut(g), f)
            tagAt(a.tag("threesuras", 0, k + 1), c + Offset(0f, h * 0.22f), f * g, back = Back66, ink = ArtInk.Gold)
        }
    }
    if (ls > 0f) {
        val f = easeOut(seg(ls, 0f, BEAT_IN)) * (1f - seg(np, 0f, BEAT_OUT))
        val light = Offset(w * 0.65f, ground - h * 0.3f)
        sirajMunir(light, w * 0.013f, f, t, nameAt = light + Offset(0f, -h * 0.12f))
        val tired = seg(ls, 0.3f, 0.8f)
        val sway = sin(t * 2f) * w * 0.02f * tired
        nameRoundel(Offset(w * 0.3f + sway, h * 0.5f + h * 0.14f * tired), w * 0.08f, a.big("longstanding"), Roundel.Fills[2], Roundel.Rim, f * seg(ls, 0.05f, 0.2f))
        drawRoundRect(Color(0xFF2E5A4A), Offset(light.x - w * 0.09f, ground - h * 0.015f), Size(w * 0.18f, h * 0.03f), CornerRadius(3f * dp), alpha = f)
        // the standing goes on and on: a bar of time that keeps growing
        drawLine(NightPrayC66.Line, Offset(w * 0.9f, h * 0.14f), Offset(w * 0.1f, h * 0.14f), h * 0.02f, StrokeCap.Round, alpha = f * 0.5f)
        drawLine(NightPrayC66.Gold, Offset(w * 0.9f, h * 0.14f), Offset(mix(w * 0.9f, w * 0.1f, seg(ls, 0.1f, 0.95f)), h * 0.14f), h * 0.02f, StrokeCap.Round, alpha = f)
        tagAt(a.tag("longstanding", 0, 1), Offset(w * 0.5f, h * 0.22f), f * seg(ls, 0.25f, 0.45f), back = Back66, ink = ArtInk.Gold)
        tagAt(a.tag("longstanding", 0, 2), Offset(w * 0.3f, h * 0.8f), f * seg(ls, 0.6f, 0.8f), back = Back66, ink = Color(0xFFF0ECE4))
    }
    if (np > 0f) {
        val f = easeOut(seg(np, 0f, BEAT_IN)) * (1f - seg(ha, 0f, BEAT_OUT))
        val y = h * 0.5f
        val x0 = w * 0.9f
        val x1 = w * 0.1f
        val g = seg(np, 0.05f, 0.25f)
        drawLine(NightPrayC66.Line, Offset(x0, y), Offset(mix(x0, x1, g), y), h * 0.06f, StrokeCap.Round, alpha = f)
        val p1 = mix(x0, x1, 0.45f)
        val p2 = mix(x0, x1, 0.88f)
        drawLine(NightPrayC66.Dim, Offset(x0, y), Offset(mix(x0, p1, seg(np, 0.25f, 0.45f)), y), h * 0.06f, StrokeCap.Round, alpha = f)
        drawLine(NightPrayC66.Gold, Offset(p1, y), Offset(mix(p1, p2, seg(np, 0.45f, 0.7f)), y), h * 0.06f, StrokeCap.Round, alpha = f * 0.7f)
        softGlow(Offset(mix(p2, x1, 0.5f), y), w * 0.06f, NightPrayC66.Gold, f * seg(np, 0.75f, 0.9f))
        moon12(Offset(w * 0.75f, h * 0.25f), w * 0.03f, f)
        tagAt(a.tag("nightplan", 0, 0), Offset((x0 + p1) / 2f, y + h * 0.1f), f * seg(np, 0.3f, 0.45f), back = Back66, ink = Color(0xFFC8C8D8))
        tagAt(a.tag("nightplan", 0, 1), Offset((p1 + p2) / 2f, y - h * 0.1f), f * seg(np, 0.5f, 0.7f), back = Back66, ink = ArtInk.Gold)
        tagAt(a.tag("nightplan", 0, 2), Offset(mix(p2, x1, 0.5f) + w * 0.03f, y + h * 0.1f), f * seg(np, 0.75f, 0.9f), back = Back66, ink = ArtInk.Gold)
    }
    if (ha > 0f) {
        val f = easeOut(seg(ha, 0f, BEAT_IN)) * (1f - seg(sd, 0f, BEAT_OUT))
        val dimmed = seg(ha, 0.25f, 0.5f)
        for (side in listOf(-1f, 1f)) {
            val c = Offset(w * 0.5f + side * w * 0.25f, h * 0.48f)
            drawLine(NightPrayC66.Lamp, c + Offset(0f, h * 0.04f), c + Offset(0f, h * 0.15f), 2f * dp, alpha = f)
            drawOval(NightPrayC66.Lamp, Offset(c.x - w * 0.025f, c.y - h * 0.025f), Size(w * 0.05f, h * 0.06f), alpha = f)
            softGlow(c, w * 0.06f, NightPrayC66.Gold, f * (1f - 0.9f * dimmed))
        }
        val heart = Offset(w * 0.5f, h * 0.48f)
        heart12(heart, w * 0.06f, NightPrayC66.Gold, f, fill = true)
        softGlow(heart, w * 0.14f, NightPrayC66.Gold, 0.6f * f * (0.85f + 0.15f * sin(t * 2f)))
        tagAt(a.tag("heartawake", 0, 1), Offset(w * 0.75f, h * 0.36f), f * seg(ha, 0.3f, 0.5f), back = Back66, ink = Color(0xFFC8C8D8))
        tagAt(a.tag("heartawake", 0, 2), heart + Offset(0f, -h * 0.14f), f * seg(ha, 0.45f, 0.65f), back = Back66, ink = ArtInk.Gold)
        tagAt(a.tag("heartawake"), Offset(w * 0.5f, h * 0.8f), f * seg(ha, 0.3f, 0.5f), back = Back66, ink = ArtInk.Gold)
    }
    if (sd > 0f) {
        val f = easeOut(seg(sd, 0f, 0.25f))
        val c = Offset(w * 0.5f, ground)
        val fill = seg(sd, 0.1f, 0.95f)
        drawRect(NightPrayC66.Water, Offset(c.x - w * 0.09f, c.y - h * 0.3f * fill), Size(w * 0.18f, h * 0.3f * fill), alpha = f * 0.8f)
        drawRoundRect(NightPrayC66.Jar, Offset(c.x - w * 0.1f, c.y - h * 0.32f), Size(w * 0.2f, h * 0.32f), CornerRadius(12f * dp), alpha = f, style = Stroke(4f * dp))
        val u = fract(t * 1.5f)
        drawCircle(NightPrayC66.Water, w * 0.008f, Offset(c.x, mix(h * 0.2f, c.y - h * 0.3f * fill, u)), alpha = f)
        // one small drop a day; the days tick by beside the jar as it fills
        for (k in 0 until 7) {
            val on = fill > (k + 1) / 8f
            val y = c.y - h * 0.3f * (k + 1) / 8f
            drawLine(if (on) NightPrayC66.Gold else NightPrayC66.Line, Offset(c.x + w * 0.12f, y), Offset(c.x + w * 0.16f, y), 2f * dp, StrokeCap.Round, alpha = f)
        }
        tagAt(a.tag("steadydrops", 0, 1), Offset(c.x + w * 0.27f, c.y - h * 0.15f), f * seg(sd, 0.3f, 0.5f), back = Back66, ink = Color(0xFFF0ECE4))
        tagAt(a.tag("steadydrops"), Offset(w * 0.5f, h * 0.12f), f * seg(sd, 0.2f, 0.4f), back = Back66, ink = ArtInk.Gold)
    }
}

/** Warm rings that open out from [c] and fade: warmth and welcome, never a mouth or a face. */
private fun DrawScope.warmRings66(c: Offset, r0: Float, r1: Float, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 3) {
        val u = fract(t * 0.35f + k / 3f)
        drawCircle(SpeechC66.Warm, mix(r0, r1, u), c, alpha = alpha * (1f - u) * 0.8f, style = Stroke(2.5f * density))
    }
    softGlow(c, r1 * 0.7f, SpeechC66.Warm, 0.35f * alpha)
}
