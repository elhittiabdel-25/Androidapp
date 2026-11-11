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
 * Wave 57 (2026-11-03): «الأربعون النووية» with Al al-Shaykh's commentary, hadiths 1–5. Intention as the lamp inside the
 * deed; the questioner who came to teach; five pillars under one roof; four words written; a new thing sent back. Allah is
 * never drawn; the Prophet ﷺ is only light; Jibril and the angels are cool white lights; scholars and the Mothers of the
 * Believers are names on books and in roundels.
 */

private val Back57 = Color(0xE6181420)

// ═════════════ 1. the lamp inside the deed (nw-01) ═════════════

private object LampC57 {
    val Wall = Color(0xFF2A2230)
    val WallLow = Color(0xFF3E3040)
    val Floor = Color(0xFF221A22)
    val Gold = Color(0xFFF2C45A)
    val BookA = Color(0xFF2E5A4A)
    val BookB = Color(0xFF5A2E3A)
    val Page = Color(0xFFF2E8D2)
    val Pillar = Color(0xFFD8C8A8)
    val Slab = Color(0xFFB8A27E)
    val Box = Color(0xFF6A5A4A)
    val Water = Color(0xFF8AC4EA)
    val Basin = Color(0xFF9A8A7A)
    val Road = Color(0xFF6A5A48)
    val Coin = Color(0xFFE8C25A)
}

/**
 * Beats: openwith (two books open on the same first line; names over them: «النَّوَوِيُّ», «الْبُخَارِيُّ»), twopillarsof (a slab
 * held on two pillars: «الْإِخْلَاصُ لِلَّهِ», «وَمُوَافَقَةُ سُنَّةِ رَسُولِهِ»; take one away and it falls), innerouter (a box
 * with a lamp inside it; one lens looks inside, «فِي بَاطِنِهِ», the other at its outside, «فِي ظَاهِرِهِ»), samewash (two basins
 * and the same washing; only one carries a glow: «الْعَادَةَ», «الْعِبَادَةِ»), hijraroads (one gate and two roads: one to a
 * light, «لِوَجْهِ اللَّهِ», one to a heap of coins, «لِغَرَضٍ دُنْيَوِيٍّ»).
 */
internal fun DrawScope.intentlampScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "openwith" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "twopillarsof" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "innerouter" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "samewash" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "hijraroads" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { lampRoom(a) }
}

private fun DrawScope.lampRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ow = a.b("openwith")
    val tc = a.b("twopillarsof")
    val io = a.b("innerouter")
    val sw = a.b("samewash")
    val hr = a.b("hijraroads")
    val floor = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(LampC57.Wall, LampC57.WallLow), 0f, floor), Offset.Zero, Size(w, floor))
    drawRect(LampC57.Floor, Offset(0f, floor), Size(w, h - floor))
    if (ow > 0f) {
        val f = easeOut(seg(ow, 0f, BEAT_IN)) * (1f - seg(tc, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val c = Offset(w * (0.3f + 0.4f * k), h * 0.6f)
            val open = easeInOut(seg(ow, 0.1f + 0.25f * k, 0.35f + 0.25f * k))
            if (open < 0.5f) drawRoundRect(if (k == 0) LampC57.BookA else LampC57.BookB, Offset(c.x - w * 0.09f, c.y - h * 0.12f), Size(w * 0.18f, h * 0.24f), CornerRadius(4f * dp), alpha = f)
            else openBook21(c, w * 0.32f, LampC57.Page, if (k == 0) LampC57.BookA else LampC57.BookB, f, seg(ow, 0.5f, 0.8f))
            tagAt(a.tag("openwith", 0, k), c + Offset(0f, -h * 0.22f), f * seg(ow, 0.15f + 0.25f * k, 0.3f + 0.25f * k), back = Back57, ink = ArtInk.Gold)
        }
        drawLine(LampC57.Gold, Offset(w * 0.2f, h * 0.6f), Offset(w * 0.8f, h * 0.6f), 2f * dp, StrokeCap.Round, alpha = f * 0.6f * seg(ow, 0.7f, 0.9f))
    }
    if (tc > 0f) {
        val f = easeOut(seg(tc, 0f, BEAT_IN)) * (1f - seg(io, 0f, BEAT_OUT))
        val pull = easeInOut(seg(tc, 0.7f, 0.85f)) * (1f - seg(tc, 0.9f, 1f))
        val tilt = 0.25f * pull
        for (k in 0 until 2) {
            val x = w * (0.3f + 0.4f * k)
            val gone = if (k == 1) pull else 0f
            drawRect(LampC57.Pillar, Offset(x - w * 0.04f, h * 0.42f), Size(w * 0.08f, floor - h * 0.42f), alpha = f * (1f - gone))
            tagAt(a.tag("twopillarsof", 0, k), Offset(x, h * 0.62f), f * seg(tc, 0.15f + 0.25f * k, 0.3f + 0.25f * k) * (1f - gone), back = Back57, ink = ArtInk.Gold)
        }
        val left = Offset(w * 0.22f, h * 0.38f)
        val right = Offset(w * 0.78f, h * 0.38f + h * 0.3f * tilt)
        drawLine(LampC57.Slab, left, right, h * 0.05f, StrokeCap.Butt, alpha = f)
    }
    if (io > 0f) {
        val f = easeOut(seg(io, 0f, BEAT_IN)) * (1f - seg(sw, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.6f)
        drawRoundRect(LampC57.Box, Offset(c.x - w * 0.15f, c.y - h * 0.12f), Size(w * 0.3f, h * 0.24f), CornerRadius(6f * dp), alpha = f)
        val see = seg(io, 0.15f, 0.4f)
        drawRoundRect(LampC57.Wall, Offset(c.x - w * 0.11f, c.y - h * 0.08f), Size(w * 0.22f, h * 0.16f), CornerRadius(4f * dp), alpha = f * see)
        softGlow(c, w * 0.08f, LampC57.Gold, f * see * (0.8f + 0.1f * sin(t * 3f)))
        drawCircle(LampC57.Gold, w * 0.015f, c, alpha = f * see)
        val lens = { p: Offset, g: Float -> drawCircle(LampC57.Pillar, w * 0.05f, p, alpha = f * g, style = Stroke(3f * dp)); drawLine(LampC57.Pillar, p + Offset(w * 0.035f, h * 0.025f), p + Offset(w * 0.07f, h * 0.05f), 3f * dp, StrokeCap.Round, alpha = f * g) }
        lens(c + Offset(-w * 0.02f, -h * 0.01f), see)
        val outer = seg(io, 0.55f, 0.75f)
        lens(c + Offset(w * 0.2f, -h * 0.1f), outer)
        drawRoundRect(LampC57.Gold, Offset(c.x - w * 0.15f, c.y - h * 0.12f), Size(w * 0.3f, h * 0.24f), CornerRadius(6f * dp), alpha = f * outer * 0.7f, style = Stroke(2f * dp))
        tagAt(a.tag("innerouter", 0, 0), Offset(w * 0.3f, h * 0.32f), f * see, back = Back57, ink = ArtInk.Gold)
        tagAt(a.tag("innerouter", 0, 1), Offset(w * 0.72f, h * 0.32f), f * outer, back = Back57, ink = ArtInk.Gold)
    }
    if (sw > 0f) {
        val f = easeOut(seg(sw, 0f, BEAT_IN)) * (1f - seg(hr, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val c = Offset(w * (0.3f + 0.4f * k), floor - h * 0.06f)
            drawArc(LampC57.Basin, 0f, 180f, true, Offset(c.x - w * 0.1f, c.y - h * 0.06f), Size(w * 0.2f, h * 0.12f), alpha = f)
            for (j in 0 until 5) {
                val u = fract(t * 1.2f + j / 5f)
                drawCircle(LampC57.Water, w * 0.005f, c + Offset(w * 0.01f * (j - 2), -h * 0.25f + h * 0.22f * u), alpha = f * (1f - u))
            }
            if (k == 1) softGlow(c + Offset(0f, -h * 0.12f), w * 0.12f, LampC57.Gold, 0.6f * f * seg(sw, 0.5f, 0.75f))
            tagAt(a.tag("samewash", 0, k), c + Offset(0f, -h * 0.35f), f * seg(sw, 0.15f + 0.3f * k, 0.3f + 0.3f * k), back = Back57, ink = if (k == 1) ArtInk.Gold else Color.Unspecified)
        }
    }
    if (hr > 0f) {
        val f = easeOut(seg(hr, 0f, 0.25f))
        val gate = Offset(w * 0.5f, floor)
        drawPath(archPath26(gate.x, gate.y - h * 0.22f, gate.y, w * 0.07f), LampC57.Pillar, alpha = f, style = Stroke(4f * dp))
        val l = Path().apply { moveTo(gate.x, gate.y); quadraticTo(w * 0.35f, h * 0.6f, w * 0.2f, h * 0.35f) }
        val r = Path().apply { moveTo(gate.x, gate.y); quadraticTo(w * 0.65f, h * 0.6f, w * 0.8f, h * 0.35f) }
        drawPath(l, LampC57.Road, alpha = f, style = Stroke(h * 0.02f, cap = StrokeCap.Round))
        drawPath(r, LampC57.Road, alpha = f, style = Stroke(h * 0.02f, cap = StrokeCap.Round))
        softGlow(Offset(w * 0.2f, h * 0.32f), w * 0.08f, LampC57.Gold, f * seg(hr, 0.15f, 0.35f))
        for (k in 0 until 6) coin12(Offset(w * (0.77f + 0.02f * (k % 3)), h * (0.33f - 0.02f * (k / 3))), w * 0.015f, f * seg(hr, 0.45f + 0.03f * k, 0.55f + 0.03f * k))
        tagAt(a.tag("hijraroads", 0, 0), Offset(w * 0.22f, h * 0.18f), f * seg(hr, 0.2f, 0.4f), back = Back57, ink = ArtInk.Gold)
        tagAt(a.tag("hijraroads", 0, 1), Offset(w * 0.76f, h * 0.18f), f * seg(hr, 0.5f, 0.7f), back = Back57)
    }
}

// ═════════════ 2. the questioner (nw-02) ═════════════

private object AskC57 {
    val Night = Color(0xFF101830)
    val NightLow = Color(0xFF2A3458)
    val Ground = Color(0xFF1C2232)
    val Gold = Color(0xFFF2CC6A)
    val Ring1 = Color(0xFF6FA8D8)
    val Ring2 = Color(0xFF7ACB9A)
    val Ring3 = Color(0xFFF2C45A)
    val Person = Color(0xFFDCD4C4)
    val Lock = Color(0xFF8A8A9A)
    val Tower = Color(0xFF5A5468)
    val Sheep = Color(0xFFE8E2D6)
}

/**
 * Beats: fourquestions (a cool white light and the light ﷺ face each other; four questions pass between them: «الْإِسْلَامِ»,
 * «وَالْإِيمَانِ», «وَالْإِحْسَانِ», «وَالسَّاعَةِ»), nestedranks (three rings one inside the other: «مُحْسِنٍ», «مُؤْمِنٌ»,
 * «مُسْلِمٌ»), pairsplit (two words side by side, «الْفَقِيرِ» and «وَالْمَسْكِينِ», each with its own colour; then one alone
 * spreads to cover both), tworungs (a figure in prayer inside two rings of light: «كَأَنَّكَ تَرَاهُ», then the wider «أَنَّهُ
 * يَرَاكَ»), fivelocked (five padlocks that stay shut), toweringup (shepherds with sheep; towers rise and rise beside them).
 */
internal fun DrawScope.questionerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fourquestions" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "nestedranks" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "pairsplit" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "tworungs" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "fivelocked" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "toweringup" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { askNight(a) }
}

private fun DrawScope.askNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fq = a.b("fourquestions")
    val nr = a.b("nestedranks")
    val ps = a.b("pairsplit")
    val tr = a.b("tworungs")
    val fl = a.b("fivelocked")
    val tu = a.b("toweringup")
    val ground = h * 0.84f
    nightSky(t, AskC57.Night, AskC57.NightLow, ground, starAlpha = 0.85f)
    drawRect(AskC57.Ground, Offset(0f, ground), Size(w, h - ground))
    if (fq > 0f) {
        val f = easeOut(seg(fq, 0f, BEAT_IN)) * (1f - seg(nr, 0f, BEAT_OUT))
        val ang = Offset(w * 0.22f, h * 0.45f)
        val nabi = Offset(w * 0.78f, h * 0.45f)
        angelLight26(ang, w * 0.012f, t, f * seg(fq, 0.02f, 0.15f))
        sirajMunir(nabi, w * 0.012f, f, t, nameAt = nabi + Offset(0f, -h * 0.12f))
        for (k in 0 until 4) {
            val g = seg(fq, 0.12f + 0.2f * k, 0.3f + 0.2f * k)
            val c = Offset(w * 0.5f, h * (0.25f + 0.13f * k))
            drawLine(AskC57.Ring1, ang + Offset(w * 0.04f, 0f), c + Offset(-w * 0.1f, 0f), 1.5f * dp, alpha = f * g * 0.5f)
            drawLine(AskC57.Ring3, c + Offset(w * 0.1f, 0f), nabi - Offset(w * 0.04f, 0f), 1.5f * dp, alpha = f * g * 0.5f)
            tagAt(a.tag("fourquestions", 0, k), c, f * g, back = Back57, ink = ArtInk.Gold)
        }
    }
    if (nr > 0f) {
        val f = easeOut(seg(nr, 0f, BEAT_IN)) * (1f - seg(ps, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.55f)
        val cols = listOf(AskC57.Ring3, AskC57.Ring2, AskC57.Ring1)
        for (k in 0 until 3) {
            val g = seg(nr, 0.1f + 0.22f * k, 0.3f + 0.22f * k)
            val r = w * (0.12f + 0.11f * k) * easeOut(g)
            drawCircle(cols[k], r, c, alpha = f * 0.15f)
            drawCircle(cols[k], r, c, alpha = f, style = Stroke(3f * dp))
            tagAt(a.tag("nestedranks", 0, k), c + Offset(0f, -w * (0.06f + 0.11f * k)), f * g, back = Back57, ink = cols[k])
        }
    }
    if (ps > 0f) {
        val f = easeOut(seg(ps, 0f, BEAT_IN)) * (1f - seg(tr, 0f, BEAT_OUT))
        val alone = easeInOut(seg(ps, 0.55f, 0.85f))
        drawRoundRect(AskC57.Ring2, Offset(w * 0.12f, h * 0.38f), Size(w * 0.36f, h * 0.24f), CornerRadius(12f * dp), alpha = f * 0.3f * (1f - alone))
        drawRoundRect(AskC57.Ring1, Offset(mix(w * 0.52f, w * 0.12f, alone), h * 0.38f), Size(mix(w * 0.36f, w * 0.76f, alone), h * 0.24f), CornerRadius(12f * dp), alpha = f * 0.3f)
        tagAt(a.tag("pairsplit", 0, 0), Offset(w * 0.3f, h * 0.5f), f * seg(ps, 0.1f, 0.25f) * (1f - alone), back = Back57, ink = AskC57.Ring2)
        tagAt(a.tag("pairsplit", 0, 1), Offset(mix(w * 0.7f, w * 0.5f, alone), h * 0.5f), f * seg(ps, 0.25f, 0.4f), back = Back57, ink = AskC57.Ring1)
    }
    if (tr > 0f) {
        val f = easeOut(seg(tr, 0f, BEAT_IN)) * (1f - seg(fl, 0f, BEAT_OUT))
        val foot = Offset(w * 0.5f, ground)
        pray12(foot, h * 0.32f, AskC57.Person, f)
        val c = foot + Offset(0f, -h * 0.16f)
        val inner = seg(tr, 0.15f, 0.4f)
        val outer = seg(tr, 0.55f, 0.8f)
        drawOval(AskC57.Ring3, Offset(c.x - w * 0.16f, c.y - h * 0.2f), Size(w * 0.32f, h * 0.4f), alpha = f * inner, style = Stroke(4f * dp))
        drawOval(AskC57.Ring1, Offset(c.x - w * 0.3f, c.y - h * 0.3f), Size(w * 0.6f, h * 0.6f), alpha = f * outer * 0.8f, style = Stroke(3f * dp))
        tagAt(a.tag("tworungs", 0, 0), Offset(w * 0.5f, h * 0.2f), f * inner * (1f - 0.5f * outer), back = Back57, ink = ArtInk.Gold)
        tagAt(a.tag("tworungs", 0, 1), Offset(w * 0.5f, h * 0.12f), f * outer, back = Back57, ink = AskC57.Ring1)
    }
    if (fl > 0f) {
        val f = easeOut(seg(fl, 0f, BEAT_IN)) * (1f - seg(tu, 0f, BEAT_OUT))
        for (k in 0 until 5) {
            val c = Offset(w * (0.14f + 0.18f * k), h * (0.5f + 0.04f * (k % 2)))
            padlock21(c, w * 0.08f, AskC57.Lock, 0.1f * sin(t * 4f + k).coerceAtLeast(0f), f * seg(fl, 0.08f + 0.1f * k, 0.2f + 0.1f * k))
        }
    }
    if (tu > 0f) {
        val f = easeOut(seg(tu, 0f, 0.25f))
        for (k in 0 until 5) {
            val rise = easeOut(seg(tu, 0.15f + 0.1f * k, 0.6f + 0.08f * k))
            val x = w * (0.42f + 0.11f * k)
            val tall = h * (0.2f + 0.08f * k) * rise
            drawRect(AskC57.Tower, Offset(x, ground - tall), Size(w * 0.09f, tall), alpha = f)
            for (j in 0 until (tall / (h * 0.06f)).toInt()) drawRect(AskC57.Gold, Offset(x + w * 0.03f, ground - tall + h * 0.02f + h * 0.06f * j), Size(w * 0.025f, h * 0.025f), alpha = f * 0.5f)
        }
        for (k in 0 until 2) figure(Offset(w * (0.12f + 0.12f * k), ground), h * 0.26f, AskC57.Person, f, walk = t * 3f + k)
        for (k in 0 until 3) sheep26(Offset(w * (0.1f + 0.1f * k), ground - h * 0.03f), w * 0.05f, AskC57.Sheep, f)
    }
}

// ═════════════ 3. five pillars under one roof (nw-03) ═════════════

private object PillarC57 {
    val Sky = Color(0xFFBFD8EA)
    val SkyLow = Color(0xFFF2E6CE)
    val Ground = Color(0xFFCDB890)
    val Stone = Color(0xFFE8DCC4)
    val Shade = Color(0xFFB8A888)
    val Base = Color(0xFF8A6A44)
    val Gold = Color(0xFFD89A2A)
    val Ink = Color(0xFF2E3A4A)
    val Hollow = Color(0xFF8A8A9A)
    val Tree = Color(0xFF4E8A54)
}

/**
 * Beats: fivebuild (five columns rise and a roof comes down on them; one column slips and the roof leans), basefirst (the
 * foundation under them glows: «الشَّهَادَتَانِ»), saidanddone (a word from the tongue alone is a hollow ring; the same word with
 * deeds under it is solid), twoservitudes (a wide circle round sun, trees and birds, «عَامَّةٌ», and a smaller ring of people
 * in prayer inside it, «وَخَاصَّةٌ»), orderofduty (a line of years with four marks: «الصَّلَاةُ», «وَالزَّكَاةُ», «وَالصَّوْمُ»,
 * «وَالْحَجُّ»).
 */
internal fun DrawScope.fivepillarsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fivebuild" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "basefirst" to Shot(Offset(w * 0.5f, h * 0.7f), 1.15f),
        "saidanddone" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "twoservitudes" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "orderofduty" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { pillarsDay(a) }
}

private fun DrawScope.pillarsDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fb = a.b("fivebuild")
    val bf = a.b("basefirst")
    val sd = a.b("saidanddone")
    val ts = a.b("twoservitudes")
    val od = a.b("orderofduty")
    val ground = h * 0.84f
    daySky12(PillarC57.Sky, PillarC57.SkyLow, ground, PillarC57.Ground)
    if (fb > 0f || bf > 0f) {
        val f = easeOut(seg(fb, 0f, BEAT_IN)) * (1f - seg(sd, 0f, BEAT_OUT))
        val slip = easeInOut(seg(fb, 0.75f, 0.9f)) * (1f - seg(bf, 0f, BEAT_OUT))
        val base = ground - h * 0.04f
        drawRect(PillarC57.Base, Offset(w * 0.1f, base), Size(w * 0.8f, h * 0.04f), alpha = f)
        for (k in 0 until 5) {
            val x = w * (0.18f + 0.16f * k)
            val rise = easeOut(seg(fb, 0.05f + 0.08f * k, 0.3f + 0.08f * k))
            val top = base - h * 0.36f * rise
            val dx = if (k == 4) w * 0.12f * slip else 0f
            val col = if (k == 2) lerpColor(PillarC57.Stone, PillarC57.Gold, 0.5f * seg(bf, 0.2f, 0.5f)) else PillarC57.Stone
            drawRect(col, Offset(x - w * 0.035f + dx, top), Size(w * 0.07f, base - top), alpha = f * (1f - slip * if (k == 4) 0.7f else 0f))
        }
        val roof = seg(fb, 0.5f, 0.7f)
        val y0 = mix(h * 0.1f, base - h * 0.36f, easeOut(roof))
        val path = Path().apply {
            moveTo(w * 0.1f, y0); lineTo(w * 0.9f, y0 + h * 0.08f * slip); lineTo(w * 0.5f, y0 - h * 0.12f + h * 0.04f * slip); close()
        }
        drawPath(path, PillarC57.Shade, alpha = f * roof)
        if (bf > 0f) {
            val g = seg(bf, 0.15f, 0.45f)
            drawRect(PillarC57.Gold, Offset(w * 0.1f, base), Size(w * 0.8f, h * 0.04f), alpha = f * g * 0.7f)
            softGlow(Offset(w * 0.5f, base), w * 0.2f, PillarC57.Gold, 0.4f * f * g)
            tagAt(a.tag("basefirst"), Offset(w * 0.5f, ground + h * 0.08f), f * g, back = Back57, ink = ArtInk.Gold)
        }
    }
    if (sd > 0f) {
        val f = easeOut(seg(sd, 0f, BEAT_IN)) * (1f - seg(ts, 0f, BEAT_OUT))
        val l = Offset(w * 0.3f, h * 0.45f)
        val r = Offset(w * 0.7f, h * 0.45f)
        drawCircle(PillarC57.Hollow, w * 0.1f, l, alpha = f * seg(sd, 0.1f, 0.3f), style = Stroke(3f * dp, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp))))
        val g = seg(sd, 0.45f, 0.7f)
        drawCircle(PillarC57.Gold, w * 0.1f, r, alpha = f * g * 0.8f)
        softGlow(r, w * 0.14f, PillarC57.Gold, 0.4f * f * g)
        for (k in 0 until 3) drawRect(PillarC57.Base, Offset(r.x - w * 0.09f + w * 0.065f * k, r.y + w * 0.12f), Size(w * 0.05f, h * 0.15f * seg(sd, 0.6f + 0.08f * k, 0.8f + 0.08f * k)), alpha = f)
        crossMark(l + Offset(0f, w * 0.16f), w * 0.03f, Color(0xFFB4492A), 3f * dp, f * seg(sd, 0.3f, 0.45f))
        tagAt(a.tag("saidanddone", 0, 0), l, f * seg(sd, 0.12f, 0.3f), back = Back57, ink = Color(0xFFF0A090))
        tagAt(a.tag("saidanddone", 0, 1), r, f * g, back = Back57, ink = ArtInk.Gold)
        tagAt(a.tag("saidanddone", 0, 2), r + Offset(0f, w * 0.12f + h * 0.19f), f * seg(sd, 0.75f, 0.95f), back = Back57, ink = Color(0xFFF0ECE4))
    }
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, BEAT_IN)) * (1f - seg(od, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.52f)
        val g1 = seg(ts, 0.05f, 0.3f)
        drawCircle(PillarC57.Ink, w * 0.42f * easeOut(g1), c, alpha = f * 0.6f, style = Stroke(2.5f * dp))
        sun12(c + Offset(-w * 0.28f, -h * 0.18f), w * 0.04f, f * g1)
        for (k in 0 until 3) palm12(c + Offset(w * (0.22f + 0.07f * k), h * 0.2f), h * 0.16f, PillarC57.Base, PillarC57.Tree, f * g1, t + k)
        for (k in 0 until 3) {
            val b = c + Offset(w * (-0.25f + 0.06f * k), -h * 0.05f + sin(t * 2f + k) * h * 0.01f)
            drawLine(PillarC57.Ink, b, b + Offset(-w * 0.015f, -h * 0.01f), 1.5f * dp, alpha = f * g1)
            drawLine(PillarC57.Ink, b, b + Offset(w * 0.015f, -h * 0.01f), 1.5f * dp, alpha = f * g1)
        }
        val g2 = seg(ts, 0.45f, 0.7f)
        drawCircle(PillarC57.Gold, w * 0.17f * easeOut(g2), c + Offset(0f, h * 0.06f), alpha = f, style = Stroke(3f * dp))
        for (k in 0 until 3) bow12(c + Offset(w * (0.08f * k - 0.07f), h * 0.16f), h * 0.16f, PillarC57.Ink, f * g2, 1f)
        tagAt(a.tag("twoservitudes", 0, 0), c + Offset(0f, -w * 0.46f), f * g1, back = Back57)
        tagAt(a.tag("twoservitudes", 0, 1), c + Offset(0f, -h * 0.06f), f * g2, back = Back57, ink = ArtInk.Gold)
    }
    if (od > 0f) {
        val f = easeOut(seg(od, 0f, 0.25f))
        val y = h * 0.55f
        penLineDay(Offset(w * 0.08f, y), Offset(w * 0.92f, y), seg(od, 0.05f, 0.3f), PillarC57.Ink, 3f * dp, f)
        // the prayer came first, in Makkah
        kaaba29(Offset(w * 0.85f, y - h * 0.2f), w * 0.05f, f * seg(od, 0.1f, 0.25f))
        tagAt(a.tag("orderofduty", 0, 4), Offset(w * 0.85f, y - h * 0.36f), f * seg(od, 0.12f, 0.28f), back = Back57, ink = Color(0xFFF0ECE4))
        for (k in 0 until 4) {
            val x = w * (0.85f - 0.24f * k)
            val g = seg(od, 0.15f + 0.2f * k, 0.3f + 0.2f * k)
            drawCircle(PillarC57.Gold, w * 0.018f, Offset(x, y), alpha = f * g)
            tagAt(a.tag("orderofduty", 0, k), Offset(x, y + (if (k % 2 == 0) 1f else -1f) * h * 0.1f), f * g, back = Back57, ink = ArtInk.Gold)
        }
    }
}

private fun DrawScope.penLineDay(from: Offset, to: Offset, p: Float, color: Color, width: Float, alpha: Float) {
    if (p <= 0f) return
    drawLine(color, to, to + (from - to) * p, width, StrokeCap.Round, alpha = alpha)
}

// ═════════════ 4. four words written (nw-04) ═════════════

private object WritC57 {
    val Deep = Color(0xFF14102A)
    val DeepLow = Color(0xFF2E2448)
    val Gold = Color(0xFFF2C860)
    val Rose = Color(0xFFD8A0A8)
    val Road = Color(0xFF4A4060)
    val Scroll = Color(0xFFF2E8D2)
    val Line = Color(0xFF8A7A9A)
}

/**
 * Beats: threestages (a line of 120 days in three equal parts, each with its word: «نُطْفَةً», «عَلَقَةً», «مُضْغَةً»), fourwrit
 * (a cool white light writes four lines: «رِزْقَهُ», «وَأَجَلَهُ», «وَعَمَلَهُ», «وَشَقِيٌّ أَوْ سَعِيدٌ»), endings (a winding road
 * and a lamp at its very end that decides the colour of all of it), nesteddecree (three scrolls, each inside a bigger one:
 * «الْعُمُرِيُّ», «الْحَوْلِيُّ», «وَالْيَوْمِيُّ»).
 */
internal fun DrawScope.fourwritScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threestages" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "fourwrit" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "endings" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "nesteddecree" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { writDeep(a) }
}

private fun DrawScope.writDeep(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val st = a.b("threestages")
    val fw = a.b("fourwrit")
    val en = a.b("endings")
    val nd = a.b("nesteddecree")
    drawRect(Brush.radialGradient(listOf(WritC57.DeepLow, WritC57.Deep), Offset(w * 0.5f, h * 0.5f), w * 0.9f), Offset.Zero, size)
    if (st > 0f) {
        val f = easeOut(seg(st, 0f, BEAT_IN)) * (1f - seg(fw, 0f, BEAT_OUT))
        val y = h * 0.6f
        val x0 = w * 0.9f
        val x1 = w * 0.1f
        drawLine(WritC57.Line, Offset(x0, y), Offset(x1, y), 2f * dp, alpha = f)
        for (k in 0 until 3) {
            val g = seg(st, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            val a0 = mix(x0, x1, k / 3f)
            val a1 = mix(x0, x1, (k + 1) / 3f)
            drawLine(WritC57.Rose, Offset(a0, y), Offset(mix(a0, a1, g), y), 5f * dp, StrokeCap.Round, alpha = f)
            drawLine(WritC57.Line, Offset(a1, y - h * 0.03f), Offset(a1, y + h * 0.03f), 2f * dp, alpha = f)
            val c = Offset((a0 + a1) / 2f, y - h * 0.1f)
            val r = w * (0.012f + 0.01f * k) * g
            if (k == 0) drawCircle(WritC57.Rose, r, c, alpha = f)
            if (k == 1) drawPath(Path().apply { moveTo(c.x, c.y - r * 1.4f); quadraticTo(c.x + r * 1.6f, c.y, c.x, c.y + r * 1.4f); quadraticTo(c.x - r * 0.6f, c.y, c.x, c.y - r * 1.4f) }, WritC57.Rose, alpha = f)
            if (k == 2) drawOval(WritC57.Rose, Offset(c.x - r * 1.3f, c.y - r), Size(r * 2.6f, r * 2f), alpha = f)
            tagAt(a.tag("threestages", 0, k), Offset((a0 + a1) / 2f, y + h * 0.1f), f * g, back = Back57, ink = WritC57.Rose)
        }
    }
    if (fw > 0f) {
        val f = easeOut(seg(fw, 0f, BEAT_IN)) * (1f - seg(en, 0f, BEAT_OUT))
        val ang = Offset(w * 0.5f, h * 0.18f)
        angelLight26(ang, w * 0.012f, t, f)
        drawRoundRect(WritC57.Scroll, Offset(w * 0.2f, h * 0.3f), Size(w * 0.6f, h * 0.55f), CornerRadius(6f * dp), alpha = f * 0.12f)
        for (k in 0 until 4) {
            val g = seg(fw, 0.1f + 0.2f * k, 0.28f + 0.2f * k)
            tagAt(a.tag("fourwrit", 0, k), Offset(w * 0.5f, h * (0.38f + 0.12f * k)), f * g, back = Color.Transparent, ink = ArtInk.Gold)
        }
    }
    if (en > 0f) {
        val f = easeOut(seg(en, 0f, BEAT_IN)) * (1f - seg(nd, 0f, BEAT_OUT))
        val p0 = Offset(w * 0.9f, h * 0.85f)
        val p1 = Offset(w * 0.0f, h * 0.65f)
        val p2 = Offset(w * 1.0f, h * 0.4f)
        val p3 = Offset(w * 0.2f, h * 0.22f)
        drawPath(cubicPart(p0, p1, p2, p3, 0f, 1f), WritC57.Road, alpha = f, style = Stroke(h * 0.025f, cap = StrokeCap.Round))
        val walk = seg(en, 0.1f, 0.7f)
        softGlow(cubic(p0, p1, p2, p3, walk), w * 0.015f, WritC57.Scroll, f)
        val lit = seg(en, 0.65f, 0.9f)
        softGlow(p3, w * 0.07f, WritC57.Gold, f * (0.3f + 0.7f * lit))
        drawPath(cubicPart(p0, p1, p2, p3, 0f, lit), WritC57.Gold, alpha = f * 0.6f, style = Stroke(h * 0.008f, cap = StrokeCap.Round))
    }
    if (nd > 0f) {
        val f = easeOut(seg(nd, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.55f)
        for (k in 0 until 3) {
            val g = seg(nd, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            val s = 1f - 0.28f * k
            drawRoundRect(WritC57.Scroll, Offset(c.x - w * 0.36f * s, c.y - h * 0.3f * s), Size(w * 0.72f * s, h * 0.6f * s), CornerRadius(8f * dp), alpha = f * g * (0.15f + 0.1f * k))
            drawRoundRect(WritC57.Gold, Offset(c.x - w * 0.36f * s, c.y - h * 0.3f * s), Size(w * 0.72f * s, h * 0.6f * s), CornerRadius(8f * dp), alpha = f * g, style = Stroke(2f * dp))
            tagAt(a.tag("nesteddecree", 0, k), Offset(c.x, c.y - h * 0.3f * s + h * 0.04f), f * g, back = Back57, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 5. sent back (nw-05) ═════════════

/**
 * Beats: bounceback (a new box is pushed through a frame; a stamp comes down and it slides back: «فَهُوَ رَدٌّ»), twogatesmeet
 * (two boxes linked to the two halves of the testimony: «الْإِخْلَاصُ», «وَالْمُتَابَعَةُ»), bothreturned (two columns,
 * «الْعِبَادَاتِ» and «وَالْعُقُودَ»; an unlawful item in each is crossed and sent back), twomothers (two roundels side by side
 * with their names: «عَائِشَةُ», «خَدِيجَةُ»).
 */
internal fun DrawScope.raddScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "bounceback" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "twogatesmeet" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "bothreturned" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "twomothers" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0f) { raddPage(a) }
}

private fun DrawScope.raddPage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val pp = Paper(a.dark)
    paperSheet(pp)
    val bb = a.b("bounceback")
    val tg = a.b("twogatesmeet")
    val br = a.b("bothreturned")
    val tm = a.b("twomothers")
    val pen = 2.2f * dp
    if (bb > 0f) {
        val f = easeOut(seg(bb, 0f, BEAT_IN)) * (1f - seg(tg, 0f, BEAT_OUT))
        val frame = w * 0.45f
        penLine(listOf(Offset(frame, h * 0.35f), Offset(frame, h * 0.7f)), seg(bb, 0.02f, 0.15f), pp.ink, pen * 1.5f, f)
        val inn = easeInOut(seg(bb, 0.1f, 0.4f))
        val out = easeInOut(seg(bb, 0.6f, 0.85f))
        val x = w * 0.8f - w * 0.3f * inn + w * 0.35f * out
        penLine(listOf(Offset(x - w * 0.06f, h * 0.48f), Offset(x + w * 0.06f, h * 0.48f), Offset(x + w * 0.06f, h * 0.6f), Offset(x - w * 0.06f, h * 0.6f), Offset(x - w * 0.06f, h * 0.48f)), 1f, pp.ink, pen, f)
        val stamp = seg(bb, 0.4f, 0.55f)
        crossMark(Offset(x, h * 0.54f), w * 0.04f, pp.bad, pen * 1.5f, f * stamp)
        note(a.tag("bounceback"), Offset(w * 0.5f, h * 0.22f), f * seg(bb, 0.45f, 0.6f), pp, pp.bad, boxed = true)
    }
    if (tg > 0f) {
        val f = easeOut(seg(tg, 0f, BEAT_IN)) * (1f - seg(br, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val c = Offset(w * (0.7f - 0.4f * k), h * 0.45f)
            val g = seg(tg, 0.1f + 0.3f * k, 0.3f + 0.3f * k)
            note(a.tag("twogatesmeet", 0, k), c, f * g, pp, if (k == 0) pp.amber else pp.good, boxed = true)
            penLine(listOf(c + Offset(0f, h * 0.06f), Offset(w * 0.5f, h * 0.7f)), g, pp.faint, pen, f)
        }
        checkMark(Offset(w * 0.5f, h * 0.76f), w * 0.04f, pp.good, pen * 1.5f, f * seg(tg, 0.75f, 0.9f))
    }
    if (br > 0f) {
        val f = easeOut(seg(br, 0f, BEAT_IN)) * (1f - seg(tm, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val x = w * (0.7f - 0.4f * k)
            note(a.tag("bothreturned", 0, k), Offset(x, h * 0.25f), f * seg(br, 0.05f + 0.15f * k, 0.2f + 0.15f * k), pp, pp.amber)
            for (j in 0 until 3) {
                val y = h * (0.4f + 0.12f * j)
                val bad = j == 1
                val back = if (bad) easeInOut(seg(br, 0.6f + 0.1f * k, 0.85f + 0.05f * k)) else 0f
                val dx = (if (k == 0) 1f else -1f) * w * 0.12f * back
                penLine(listOf(Offset(x - w * 0.12f + dx, y), Offset(x + w * 0.12f + dx, y)), seg(br, 0.2f + 0.05f * j, 0.35f + 0.05f * j), if (bad) pp.bad else pp.ink, pen, f * (1f - 0.6f * back))
                if (bad) crossMark(Offset(x + dx, y), w * 0.025f, pp.bad, pen, f * seg(br, 0.45f + 0.1f * k, 0.55f + 0.1f * k))
            }
        }
    }
    if (tm > 0f) {
        val f = easeOut(seg(tm, 0f, 0.25f))
        nameRoundel(Offset(w * 0.68f, h * 0.5f), w * 0.11f, a.big("twomothers", 0, 0), Roundel.Fills[3 % Roundel.Fills.size], Roundel.Rim, f * seg(tm, 0.05f, 0.25f))
        nameRoundel(Offset(w * 0.32f, h * 0.5f), w * 0.11f, a.big("twomothers", 0, 1), Roundel.Fills[5 % Roundel.Fills.size], Roundel.Rim, f * seg(tm, 0.35f, 0.55f))
    }
}
