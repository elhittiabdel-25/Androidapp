package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 70 (2026-11-03): v1.3 fiqh, «الحج والعمرة» 7–12 (Ibn Bāz). What the state of ihram forbids; the circling and the going between
 * the hills; Minā, ʿArafa and Muzdalifa; the day of sacrifice and the days of tashrīq; the offering and the ten days of fasting in its
 * place; the last days at Makkah and the farewell. Pilgrims are faceless figures; the offering is shown alive, never slaughtered.
 */

private object HajjC70 {
    val Sky = Color(0xFF0E1630)
    val SkyLow = Color(0xFF3A3A6A)
    val Ground = Color(0xFF1E1A24)
    val DayTop = Color(0xFF8EC2E6)
    val DayLow = Color(0xFFF2E2C0)
    val Dusk = Color(0xFFE09A70)
    val Lit = Color(0xFFF2C860)
    val Dark = Color(0xFF3A3040)
    val Hill = Color(0xFFB09068)
    val Floor = Color(0xFFE8E2D6)
    val Person = Color(0xFFE8DCC4)
    val Beast = Color(0xFFC8A880)
    val Green = Color(0xFF3E8A5E)
}

// ═════════════ 7. hajj-mahzurat: what ihram forbids ═════════════

/**
 * Beats: nocutscent (scissors, a nail, a phial, each crossed out: «شَعْرِهِ», «أَظْفَارِهِ», «يَتَطَيَّبَ»), nosewn (a sewn shirt and a
 * turban crossed out, then the two white cloths: «الْمَخِيطَ», «وَلَا يُغَطِّي رَأْسَهُ»), nohunt (a bird flies free past a crossed
 * arrow, and two rings stay apart: «قَتْلُ الصَّيْدِ», «وَعَقْدُ النِّكَاحِ»), norafath (three dark speech-clouds dissolve: «الرَّفَثِ»,
 * «وَالْفُسُوقِ», «وَالْجِدَالِ»), mayshade (what stays allowed, with ticks: water, and a shade: «يَغْتَسِلَ», «يَسْتَظِلَّ»).
 */
internal fun DrawScope.ihramlimitsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "nocutscent" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "nosewn" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "nohunt" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "norafath" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "mayshade" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { ihramLimitsDraw(a) }
}

private fun DrawScope.ihramLimitsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val ground = h * 0.8f
    daySky12(HajjC70.DayTop, HajjC70.DayLow, ground, Fq.Sand)
    val p1 = fqIn(a, "nocutscent", "nosewn")
    if (p1 > 0f) {
        val pb = a.b("nocutscent")
        val xs = listOf(0.78f, 0.5f, 0.22f)
        for (k in 0 until 3) {
            val g = seg(pb, 0.05f + 0.28f * k, 0.18f + 0.28f * k)
            val c = Offset(w * xs[k], h * 0.48f)
            when (k) {
                0 -> fqScissors(c, w * 0.08f, Color(0xFF6A6A78), p1 * g, open = 0.6f)
                1 -> {
                    drawRoundRect(Color(0xFFE8C8B0), c - Offset(w * 0.035f, h * 0.06f), Size(w * 0.07f, h * 0.12f), CornerRadius(w * 0.03f), alpha = p1 * g)
                    drawRoundRect(Color(0xFFF6EAE0), c - Offset(w * 0.025f, h * 0.055f), Size(w * 0.05f, h * 0.04f), CornerRadius(w * 0.02f), alpha = p1 * g)
                }
                else -> fqPhial(c + Offset(0f, h * 0.08f), h * 0.18f, Color(0xFFC8A0D0), p1 * g, t)
            }
            cross14(c, w * 0.08f, seg(pb, 0.12f + 0.28f * k, 0.25f + 0.28f * k))
            tagAt(a.tag("nocutscent", 0, k), c + Offset(0f, h * 0.2f), p1 * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    val p2 = fqIn(a, "nosewn", "nohunt")
    if (p2 > 0f) {
        val pb = a.b("nosewn")
        val shirt = Offset(w * 0.75f, h * 0.45f)
        fqShirt(shirt, h * 0.3f, Color(0xFF7A9AB8), p2 * seg(pb, 0.05f, 0.2f))
        cross14(shirt, w * 0.1f, seg(pb, 0.15f, 0.3f))
        tagAt(a.tag("nosewn", 0, 0), shirt + Offset(0f, h * 0.24f), p2 * seg(pb, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        val cap = Offset(w * 0.47f, h * 0.45f)
        val g = seg(pb, 0.4f, 0.55f)
        for (k in 0 until 3) drawOval(Color(0xFFE8E0D0), Offset(cap.x - w * 0.08f + k * w * 0.008f, cap.y - h * 0.06f + k * h * 0.035f), Size(w * 0.16f - k * w * 0.016f, h * 0.06f), alpha = p2 * g, style = Stroke(5f * dp))
        cross14(cap, w * 0.09f, seg(pb, 0.5f, 0.65f))
        tagAt(a.tag("nosewn", 0, 1), cap + Offset(0f, h * 0.24f), p2 * seg(pb, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
        fqPilgrim(Offset(w * 0.18f, ground), h * 0.38f, p2 * seg(pb, 0.75f, 0.9f))
    }
    val p3 = fqIn(a, "nohunt", "norafath")
    if (p3 > 0f) {
        val pb = a.b("nohunt")
        val fly = fract(t * 0.15f)
        fqBird(Offset(mix(w * 0.95f, w * 0.05f, fly), h * 0.3f + sin(t * 2f) * h * 0.03f), w * 0.04f, Color(0xFF3A3A48), p3, t)
        val bow = Offset(w * 0.72f, h * 0.55f)
        drawLine(Color(0xFF6A5040), bow, bow + Offset(-w * 0.14f, -h * 0.08f), 3f * dp, StrokeCap.Round, alpha = p3 * seg(pb, 0.05f, 0.15f))
        cross14(bow + Offset(-w * 0.07f, -h * 0.04f), w * 0.07f, seg(pb, 0.15f, 0.3f))
        tagAt(a.tag("nohunt", 0, 0), bow + Offset(-w * 0.07f, h * 0.14f), p3 * seg(pb, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        val rings = Offset(w * 0.28f, h * 0.55f)
        val g = seg(pb, 0.45f, 0.6f)
        drawCircle(Fq.Gold, w * 0.03f, rings + Offset(w * 0.025f, 0f), alpha = p3 * g, style = Stroke(4f * dp))
        drawCircle(Fq.Gold, w * 0.03f, rings + Offset(-w * 0.025f, 0f), alpha = p3 * g, style = Stroke(4f * dp))
        cross14(rings, w * 0.07f, seg(pb, 0.55f, 0.7f))
        tagAt(a.tag("nohunt", 0, 1), rings + Offset(0f, h * 0.14f), p3 * seg(pb, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p4 = fqIn(a, "norafath", "mayshade")
    if (p4 > 0f) {
        val pb = a.b("norafath")
        for (k in 0 until 3) {
            val g = seg(pb, 0.05f + 0.2f * k, 0.2f + 0.2f * k)
            val gone = seg(pb, 0.7f, 0.95f)
            val c = Offset(w * (0.78f - 0.28f * k), h * (0.42f + 0.06f * (k % 2)))
            val r = w * 0.09f * (1f + 0.3f * gone)
            drawRoundRect(HajjC70.Dark, c - Offset(r, r * 0.55f), Size(r * 2f, r * 1.1f), CornerRadius(r * 0.5f), alpha = p4 * g * (1f - gone))
            drawPath(Path().apply { moveTo(c.x + r * 0.2f, c.y + r * 0.5f); lineTo(c.x + r * 0.5f, c.y + r * 0.9f); lineTo(c.x + r * 0.55f, c.y + r * 0.4f); close() }, HajjC70.Dark, alpha = p4 * g * (1f - gone))
            tagAt(a.tag("norafath", 0, k), c, p4 * g * (1f - gone * 0.7f), back = Color.Transparent, ink = ArtInk.Ivory)
        }
    }
    val p5 = fqIn(a, "mayshade")
    if (p5 > 0f) {
        val pb = a.b("mayshade")
        val g1 = seg(pb, 0.05f, 0.2f)
        for (k in 0 until 5) {
            val u = fract(t * 0.8f + k / 5f)
            fqDrop(Offset(w * (0.68f + 0.04f * (k - 2)), mix(h * 0.25f, h * 0.6f, u)), w * 0.012f, Fq.Water, p5 * g1 * (1f - u))
        }
        tick14(Offset(w * 0.68f, h * 0.68f), w * 0.05f, seg(pb, 0.15f, 0.3f))
        tagAt(a.tag("mayshade", 0, 0), Offset(w * 0.68f, h * 0.16f), p5 * g1, back = Fq.Back, ink = Fq.Water)
        val g2 = seg(pb, 0.45f, 0.6f)
        fqShade(Offset(w * 0.3f, ground), h * 0.45f, Color(0xFFE8E0D0), p5 * g2)
        fqPilgrim(Offset(w * 0.3f, ground), h * 0.3f, p5 * g2)
        tick14(Offset(w * 0.45f, h * 0.68f), w * 0.05f, seg(pb, 0.55f, 0.7f))
        tagAt(a.tag("mayshade", 0, 1), Offset(w * 0.3f, h * 0.16f), p5 * g2, back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 8. hajj-tawaf: round the House, and between the hills ═════════════

/**
 * Beats: blackstone (the House from above; at its corner the stone glints and a hand is raised to it: «الْحَجَرِ الْأَسْوَدِ», «مُكَبِّرًا»),
 * sevencircuits (people go round it counter-clockwise, the House on their left, while seven pips fill: «7», «عَنْ يَسَارِهِ»),
 * behindmaqam (two rakʿas behind the Station: «رَكْعَتَيْنِ خَلْفَ الْمَقَامِ»), safamarwa (between two hills seven times: «الصَّفَا»,
 * «وَالْمَرْوَةِ»), umrahdone (the scissors, then a tick: «ثُمَّ يَحْلِقُ رَأْسَهُ أَوْ يُقَصِّرُ»).
 */
internal fun DrawScope.sevenroundsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val c = Offset(w * 0.5f, h * 0.5f)
    val shots = shotsFor(a, mapOf(
        "blackstone" to Shot(c + Offset(w * 0.06f, h * 0.06f), 1.4f),
        "sevencircuits" to Shot(c, 1f),
        "behindmaqam" to Shot(c + Offset(0f, h * 0.15f), 1.2f),
        "safamarwa" to Shot(c, 1f),
        "umrahdone" to Shot(c, 1.05f),
    ))
    followBeats(a, shots, Shot(c, 1f), dim = 0.08f) { sevenRoundsDraw(a) }
}

private fun DrawScope.sevenRoundsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val c = Offset(w * 0.5f, h * 0.5f)
    val sm = a.b("safamarwa")
    val court = 1f - seg(sm, 0f, 0.4f)
    drawRect(lerpColor(HajjC70.Floor, Color(0xFF2A2A38), seg(sm, 0f, 0.4f)), Offset.Zero, size)
    if (court > 0f) {
        val s = h * 0.2f
        for (k in 1..4) drawCircle(Color(0xFFD0C8B8), s * (0.9f + 0.55f * k), c, alpha = court * 0.5f, style = Stroke(1f * dp))
        rotate(-12f, c) {
            drawRect(Color(0xFF14121A), c - Offset(s * 0.5f, s * 0.45f), Size(s, s * 0.9f), alpha = court)
            drawRect(Color(0xFFD8A83A), c - Offset(s * 0.5f, s * 0.45f), Size(s, s * 0.9f), alpha = court, style = Stroke(s * 0.06f))
        }
        val stone = c + Offset(s * 0.42f, s * 0.5f)
        val bs = a.b("blackstone")
        glint(stone, w * 0.02f, Color.White, court * seg(bs, 0.1f, 0.3f))
        drawCircle(Color(0xFF2A2024), w * 0.01f, stone, alpha = court)
        val ci = a.b("sevencircuits")
        fqCircling(c, s * 1.6f, s * 1.6f, t, Fq.White, court * seg(ci, 0f, 0.2f), n = 22, speed = 0.06f)
        val ps = fqIn(a, "blackstone", "sevencircuits")
        if (ps > 0f) {
            fqPilgrim(stone + Offset(w * 0.05f, h * 0.22f), h * 0.18f, ps, arms = 0.7f)
            tagAt(a.tag("blackstone", 0, 0), stone + Offset(w * 0.2f, -h * 0.05f), ps * seg(bs, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
            tagAt(a.tag("blackstone", 0, 1), stone + Offset(w * 0.2f, h * 0.08f), ps * seg(bs, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
        }
        val pc = fqIn(a, "sevencircuits", "behindmaqam")
        if (pc > 0f) {
            for (k in 0 until 7) {
                val on = seg(ci, 0.1f + 0.11f * k, 0.18f + 0.11f * k)
                val pip = Offset(w * (0.9f - 0.035f * k), h * 0.1f)
                drawCircle(HajjC70.Lit, w * 0.012f, pip, alpha = pc * (0.25f + 0.75f * on))
            }
            tagAt(a.tag("sevencircuits", 0, 0), Offset(w * 0.79f, h * 0.2f), pc * seg(ci, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
            tagAt(a.tag("sevencircuits", 0, 1), Offset(w * 0.18f, h * 0.15f), pc * seg(ci, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Ivory)
        }
        val pm = fqIn(a, "behindmaqam", "safamarwa")
        if (pm > 0f) {
            val mb = a.b("behindmaqam")
            val mq = c + Offset(-s * 0.15f, s * 1.0f)
            drawRoundRect(HajjC70.Lit, mq - Offset(w * 0.02f, w * 0.03f), Size(w * 0.04f, w * 0.05f), CornerRadius(w * 0.01f), alpha = pm)
            softGlow(mq, w * 0.04f, HajjC70.Lit, pm * 0.5f)
            val bow = (sin(t * 1.4f) * 0.5f + 0.5f) * seg(mb, 0.2f, 0.3f)
            w8Worshipper(mq + Offset(0f, h * 0.24f), h * 0.16f, Fq.White, 0f, bow, pm * seg(mb, 0.1f, 0.25f))
            tagAt(a.tag("behindmaqam"), mq + Offset(w * 0.22f, h * 0.15f), pm * seg(mb, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    val p4 = fqIn(a, "safamarwa", "umrahdone")
    if (p4 > 0f) {
        val y = h * 0.62f
        val safa = Offset(w * 0.8f, y)
        val marwa = Offset(w * 0.2f, y)
        fqMound(safa, w * 0.2f, h * 0.1f, HajjC70.Hill, p4)
        fqMound(marwa, w * 0.2f, h * 0.1f, HajjC70.Hill, p4)
        drawLine(Color(0xFF5A5468), safa + Offset(-w * 0.1f, h * 0.02f), marwa + Offset(w * 0.1f, h * 0.02f), h * 0.05f, alpha = p4)
        val run = seg(sm, 0.15f, 1f) * 7f
        val leg = run.toInt().coerceAtMost(6)
        val u = easeInOut(run - leg)
        val from = if (leg % 2 == 0) safa else marwa
        val to = if (leg % 2 == 0) marwa else safa
        fqPilgrim(Offset(mix(from.x, to.x, u), y + h * 0.02f), h * 0.16f, p4, walk = t * 10f)
        for (k in 0 until 7) drawCircle(HajjC70.Lit, w * 0.011f, Offset(w * (0.62f - 0.04f * k), h * 0.86f), alpha = p4 * (if (run > k + 0.98f) 1f else 0.25f))
        tagAt(a.tag("safamarwa", 0, 0), safa + Offset(0f, -h * 0.2f), p4 * seg(sm, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("safamarwa", 0, 1), marwa + Offset(0f, -h * 0.2f), p4 * seg(sm, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p5 = fqIn(a, "umrahdone")
    if (p5 > 0f) {
        val ub = a.b("umrahdone")
        fqScissors(Offset(w * 0.5f, h * 0.42f), w * 0.1f, Color(0xFFB8B8C8), p5, open = 0.5f + 0.5f * sin(t * 6f) * (1f - seg(ub, 0.5f, 0.6f)))
        tick14(Offset(w * 0.5f, h * 0.68f), w * 0.07f, seg(ub, 0.55f, 0.8f))
        softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.2f, HajjC70.Lit, p5 * seg(ub, 0.6f, 0.9f) * 0.4f)
        tagAt(a.tag("umrahdone"), Offset(w * 0.5f, h * 0.15f), p5 * seg(ub, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 9. hajj-arafa: Minā, ʿArafa, Muzdalifa ═════════════

/**
 * Beats: tarwiya (white tents at Minā on the eighth: «الثَّامِنَ مِنْ ذِي الْحِجَّةِ», «مِنًى»), arafastand (the plain under the hill, hands
 * raised while the sun crosses the sky down to its setting: «عَرَفَةَ», «فِي الذِّكْرِ وَالدُّعَاءِ»), muzdalifanight (night, the crowd
 * moving calmly then resting under the stars: «مُزْدَلِفَةَ», «بِسَكِينَةٍ وَوَقَارٍ»), mashardawn (dawn whitening the sky while they
 * stay, then setting off before the sun: «الْمَشْعَرِ الْحَرَامِ», «قَبْلَ طُلُوعِ الشَّمْسِ»).
 */
internal fun DrawScope.arafatdayScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "tarwiya" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "arafastand" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "muzdalifanight" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "mashardawn" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { arafatDayDraw(a) }
}

private fun DrawScope.arafatDayDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ground = h * 0.74f
    val tw = a.b("tarwiya")
    val af = a.b("arafastand")
    val mz = a.b("muzdalifanight")
    val md = a.b("mashardawn")
    // the light of the day: day at Minā and ʿArafa, dusk at the end of ʿArafa, night at Muzdalifa, dawn at the Mashʿar
    val dusk = seg(af, 0.6f, 1f)
    val night = seg(mz, 0f, 0.3f) * (1f - seg(md, 0.2f, 0.9f))
    val dawn = seg(md, 0.2f, 0.9f)
    val top = lerpColor(lerpColor(lerpColor(HajjC70.DayTop, Color(0xFF4A4A7A), dusk), HajjC70.Sky, night), Color(0xFF6A7AAA), dawn)
    val low = lerpColor(lerpColor(lerpColor(HajjC70.DayLow, HajjC70.Dusk, dusk), HajjC70.SkyLow, night), Color(0xFFF0C8A0), dawn)
    drawRect(top, Offset.Zero, Size(w, ground))
    drawRect(low, Offset(0f, ground * 0.6f), Size(w, ground * 0.4f), alpha = 0.6f)
    if (night > 0f) nightSky(t, HajjC70.Sky, HajjC70.SkyLow, ground, starAlpha = night)
    drawRect(lerpColor(Fq.Sand, HajjC70.Ground, night * 0.8f), Offset(0f, ground), Size(w, h - ground))
    val p1 = fqIn(a, "tarwiya", "arafastand")
    if (p1 > 0f) {
        sun12(Offset(w * 0.8f, h * 0.18f), w * 0.045f, p1)
        for (r in 0 until 3) for (k in 0 until 7) {
            val g = seg(tw, 0.05f + 0.03f * (k + r), 0.2f + 0.03f * (k + r))
            fqTent(Offset(w * (0.1f + 0.13f * k + 0.06f * (r % 2)), ground + h * (0.06f + 0.08f * r)), w * 0.1f, Fq.White, p1 * g)
        }
        tagAt(a.tag("tarwiya", 0, 0), Offset(w * 0.5f, h * 0.18f), p1 * seg(tw, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("tarwiya", 0, 1), Offset(w * 0.3f, h * 0.38f), p1 * seg(tw, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p2 = fqIn(a, "arafastand", "muzdalifanight")
    if (p2 > 0f) {
        val sunU = seg(af, 0f, 1f)
        val sx = mix(w * 0.85f, w * 0.1f, sunU)
        val sy = ground - h * 0.55f * sin(PI.toFloat() * mix(0.35f, 1f, sunU)).coerceAtLeast(0f)
        sun12(Offset(sx, sy), w * 0.045f, p2 * (1f - seg(af, 0.95f, 1f)))
        fqMound(Offset(w * 0.62f, ground), w * 0.32f, h * 0.18f, HajjC70.Hill, p2)
        for (k in 0 until 26) {
            val x = w * (0.04f + 0.92f * fract(k * 0.618f))
            val y = ground + h * (0.03f + 0.18f * fract(k * 0.37f + 0.1f))
            drawCircle(Fq.White, w * 0.008f, Offset(x, y), alpha = p2)
        }
        hands12(Offset(w * 0.3f, ground + h * 0.1f), h * 0.12f, HajjC70.Person, p2 * seg(af, 0.2f, 0.35f))
        tagAt(a.tag("arafastand", 0, 0), Offset(w * 0.62f, ground - h * 0.25f), p2 * seg(af, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("arafastand", 0, 1), Offset(w * 0.3f, h * 0.2f), p2 * seg(af, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p3 = fqIn(a, "muzdalifanight", "mashardawn")
    if (p3 > 0f) {
        val go = seg(mz, 0.05f, 0.6f)
        for (k in 0 until 24) {
            val x0 = w * (0.6f + 0.4f * fract(k * 0.618f))
            val x = mix(x0, x0 - w * 0.5f, go)
            val y = ground + h * (0.04f + 0.16f * fract(k * 0.37f + 0.1f))
            drawCircle(Fq.White, w * 0.007f, Offset(x, y), alpha = p3 * 0.9f)
        }
        moon12(Offset(w * 0.2f, h * 0.2f), w * 0.035f, p3)
        tagAt(a.tag("muzdalifanight", 0, 0), Offset(w * 0.5f, h * 0.2f), p3 * seg(mz, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("muzdalifanight", 0, 1), Offset(w * 0.5f, h * 0.4f), p3 * seg(mz, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p4 = fqIn(a, "mashardawn")
    if (p4 > 0f) {
        softGlow(Offset(w * 0.85f, ground), w * 0.25f * dawn, Color(0xFFF8D8A8), p4 * dawn * 0.7f)
        domed29(Offset(w * 0.5f, ground + h * 0.02f), w * 0.16f, Color(0xFFD8CCB4), Color(0xFF8A9AAA), p4)
        val leave = seg(md, 0.75f, 1f)
        for (k in 0 until 20) {
            val x0 = w * (0.1f + 0.6f * fract(k * 0.618f))
            val y = ground + h * (0.06f + 0.14f * fract(k * 0.37f + 0.3f))
            drawCircle(Fq.White, w * 0.007f, Offset(mix(x0, x0 - w * 0.4f, leave), y), alpha = p4)
        }
        tagAt(a.tag("mashardawn", 0, 0), Offset(w * 0.5f, h * 0.3f), p4 * seg(md, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("mashardawn", 0, 1), Offset(w * 0.75f, h * 0.15f), p4 * seg(md, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 10. hajj-nahr: the day of sacrifice and the days of tashrīq ═════════════

/**
 * Beats: fourrites (four stations in a row, lit in turn: the pillar, the offering, the scissors, the House: «جَمْرَةَ الْعَقَبَةِ», «ثُمَّ
 * يَنْحَرُ هَدْيَهُ», «ثُمَّ يَحْلِقُ أَوْ يُقَصِّرُ», «ثُمَّ يَطُوفُ لِلْإِفَاضَةِ»), noharaj (the four change places and a tick still comes:
 * «افْعَلْ وَلَا حَرَجَ»), tashriqthree (three pillars, each struck after noon on three days: «أَيَّامَ التَّشْرِيقِ», «بَعْدَ الزَّوَالِ»),
 * deputythrow (one who cannot throw sits while another throws for him: «لِمَرَضٍ أَوْ كِبَرِ سِنٍّ أَوْ حَمْلٍ»).
 */
internal fun DrawScope.nahrdayScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fourrites" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "noharaj" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "tashriqthree" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "deputythrow" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { nahrDayDraw(a) }
}

private fun DrawScope.nahrRite(k: Int, c: Offset, s: Float, alpha: Float, t: Float) {
    when (k) {
        0 -> {
            fqPillar(c + Offset(0f, s * 0.5f), s * 0.25f, s, Fq.Stone, alpha)
            for (i in 0 until 3) fqPebble(c + Offset(s * 0.8f, s * 0.2f), c + Offset(0f, -s * 0.1f), fract(t * 0.7f + i / 3f), s * 0.04f, alpha)
        }
        1 -> sheep26(c + Offset(0f, s * 0.1f), s * 0.8f, Color(0xFFEDE6D8), alpha)
        2 -> fqScissors(c, s * 0.45f, Color(0xFF8A8A98), alpha, open = 0.5f + 0.5f * sin(t * 6f))
        else -> {
            fqCircling(c + Offset(0f, s * 0.3f), s * 0.6f, s * 0.22f, t, Fq.White, alpha, n = 10)
            kaaba29(c + Offset(0f, s * 0.35f), s * 0.5f, alpha)
        }
    }
}

private fun DrawScope.nahrDayDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ground = h * 0.78f
    daySky12(HajjC70.DayTop, HajjC70.DayLow, ground, Fq.Sand)
    val fr = a.b("fourrites")
    val nh = a.b("noharaj")
    val p1 = fqIn(a, "fourrites", "tashriqthree")
    if (p1 > 0f) {
        val slots = List(4) { Offset(w * (0.84f - 0.225f * it), h * 0.45f) }
        val order = listOf(2, 0, 3, 1)
        val mixU = easeInOut(seg(nh, 0.1f, 0.5f))
        for (k in 0 until 4) {
            val g = seg(fr, 0.05f + 0.22f * k, 0.2f + 0.22f * k)
            val home = slots[k]
            val away = slots[order[k]] + Offset(0f, h * 0.06f * sin(PI.toFloat() * mixU) * (if (k % 2 == 0) -1f else 1f))
            val pos = Offset(mix(home.x, away.x, mixU), mix(home.y, away.y, mixU))
            drawCircle(Color.White, w * 0.1f, pos, alpha = p1 * g * 0.35f)
            nahrRite(k, pos, h * 0.2f, p1 * g, t)
            tagAt(a.tag("fourrites", 0, k), pos + Offset(0f, h * 0.2f), p1 * g * (1f - 0.7f * seg(nh, 0.05f, 0.15f)), back = Fq.Back, ink = ArtInk.Ivory)
        }
        if (nh > 0f) {
            tick14(Offset(w * 0.5f, h * 0.78f), w * 0.06f, seg(nh, 0.5f, 0.7f))
            tagAt(a.tag("noharaj"), Offset(w * 0.5f, h * 0.15f), p1 * seg(nh, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    val p3 = fqIn(a, "tashriqthree", "deputythrow")
    if (p3 > 0f) {
        val tb = a.b("tashriqthree")
        for (k in 0 until 3) {
            val base = Offset(w * (0.78f - 0.28f * k), ground)
            fqPillar(base, w * 0.045f, h * (0.34f - 0.03f * k), Fq.Stone, p3)
            for (d in 0 until 3) {
                val q = seg(tb, 0.15f + 0.25f * d + 0.06f * k, 0.25f + 0.25f * d + 0.06f * k)
                fqPebble(base + Offset(w * 0.12f, -h * 0.05f), base + Offset(0f, -h * 0.2f), q, w * 0.007f, p3)
            }
        }
        for (d in 0 until 3) {
            val on = seg(tb, 0.12f + 0.25f * d, 0.2f + 0.25f * d)
            val c = Offset(w * (0.6f - 0.1f * d), h * 0.14f)
            sun12(c, w * 0.02f, p3 * (0.3f + 0.7f * on))
        }
        tagAt(a.tag("tashriqthree", 0, 0), Offset(w * 0.5f, h * 0.27f), p3 * seg(tb, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("tashriqthree", 0, 1), Offset(w * 0.18f, h * 0.14f), p3 * seg(tb, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p4 = fqIn(a, "deputythrow")
    if (p4 > 0f) {
        val db = a.b("deputythrow")
        val pil = Offset(w * 0.18f, ground)
        fqPillar(pil, w * 0.05f, h * 0.38f, Fq.Stone, p4)
        sit12(Offset(w * 0.8f, ground), h * 0.3f, HajjC70.Person, p4, facing = -1f)
        val man = Offset(w * 0.55f, ground)
        fqPilgrim(man, h * 0.36f, p4 * seg(db, 0.1f, 0.25f), arms = 0.8f)
        for (k in 0 until 3) {
            val q = fract(seg(db, 0.3f, 0.95f) * 3f - k) * (if (seg(db, 0.3f, 0.95f) * 3f >= k) 1f else 0f)
            fqPebble(man + Offset(-h * 0.1f, -h * 0.28f), pil + Offset(0f, -h * 0.22f), q, w * 0.008f, p4)
        }
        tagAt(a.tag("deputythrow"), Offset(w * 0.6f, h * 0.15f), p4 * seg(db, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 11. hajj-hady: the offering, or ten days of fasting ═════════════

/**
 * Beats: hadyshare (a sheep whole, a camel and a cow each marked in seven shares: «شَاةٌ», «أَوْ سُبْعُ بَدَنَةٍ», «أَوْ سُبْعُ بَقَرَةٍ»),
 * pureearning (coins sifted and only the clean ones go to the offering: «لَا يَقْبَلُ إِلَّا طَيِّبًا»), tenfast (ten days, three by the
 * House and seven at home: «قَبْلَ يَوْمِ النَّحْرِ», «بَعْدَ رُجُوعِهِ إِلَى أَهْلِهِ»), arafaopen (the three set before the day of ʿArafa,
 * which stays a day of eating, with hands raised: «يَوْمِ عَرَفَةَ»), fourdays (four days lit for the sacrifice: «يَوْمُ النَّحْرِ وَثَلَاثَةُ
 * أَيَّامٍ بَعْدَهُ»).
 */
internal fun DrawScope.hadyofferScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "hadyshare" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "pureearning" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "tenfast" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "arafaopen" to Shot(Offset(w * 0.62f, h * 0.45f), 1.2f),
        "fourdays" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { hadyOfferDraw(a) }
}

private fun DrawScope.hadyOfferDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val ground = h * 0.8f
    daySky12(HajjC70.DayTop, HajjC70.DayLow, ground, Fq.Sand)
    val p1 = fqIn(a, "hadyshare", "pureearning")
    if (p1 > 0f) {
        val pb = a.b("hadyshare")
        val g0 = seg(pb, 0.05f, 0.2f)
        sheep26(Offset(w * 0.82f, ground - h * 0.1f), h * 0.2f, Color(0xFFEDE6D8), p1 * g0)
        tagAt(a.tag("hadyshare", 0, 0), Offset(w * 0.82f, h * 0.4f), p1 * g0, back = Fq.Back, ink = ArtInk.Ivory)
        val g1 = seg(pb, 0.35f, 0.5f)
        camel26(Offset(w * 0.52f, ground), h * 0.36f, HajjC70.Beast, p1 * g1, t)
        val g2 = seg(pb, 0.65f, 0.8f)
        fqCow(Offset(w * 0.2f, ground), h * 0.32f, Color(0xFF8A6A50), p1 * g2)
        for (k in 1 until 7) {
            val xc = w * 0.52f - h * 0.15f + k * h * 0.3f / 7f
            drawLine(Color.White, Offset(xc, ground - h * 0.27f), Offset(xc, ground - h * 0.17f), 1.5f * dp, alpha = p1 * g1 * seg(pb, 0.45f, 0.55f))
            val xw = w * 0.2f - h * 0.14f + k * h * 0.29f / 7f
            drawLine(Color.White, Offset(xw, ground - h * 0.23f), Offset(xw, ground - h * 0.12f), 1.5f * dp, alpha = p1 * g2 * seg(pb, 0.75f, 0.85f))
        }
        tagAt(a.tag("hadyshare", 0, 1), Offset(w * 0.52f, h * 0.24f), p1 * g1, back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("hadyshare", 0, 2), Offset(w * 0.2f, h * 0.4f), p1 * g2, back = Fq.Back, ink = ArtInk.Gold)
    }
    val p2 = fqIn(a, "pureearning", "tenfast")
    if (p2 > 0f) {
        val pb = a.b("pureearning")
        for (k in 0 until 6) {
            val bad = k % 3 == 1
            val drop = easeInOut(seg(pb, 0.15f + 0.08f * k, 0.45f + 0.08f * k))
            val x = w * (0.75f - 0.06f * k)
            val y = mix(h * 0.25f, if (bad) h * 0.95f else h * 0.6f, drop)
            val xx = mix(x, if (bad) x else w * 0.35f, drop)
            coin12(Offset(xx, y), w * 0.022f, p2 * (if (bad) 1f - drop else 1f), if (bad) Color(0xFF6A6058) else Fq.Gold)
        }
        sheep26(Offset(w * 0.3f, ground - h * 0.1f), h * 0.2f, Color(0xFFEDE6D8), p2)
        softGlow(Offset(w * 0.3f, ground - h * 0.12f), w * 0.12f, HajjC70.Lit, p2 * seg(pb, 0.7f, 0.9f) * 0.5f)
        tagAt(a.tag("pureearning"), Offset(w * 0.5f, h * 0.12f), p2 * seg(pb, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val tf = a.b("tenfast")
    val ao = a.b("arafaopen")
    val p3 = fqIn(a, "tenfast", "fourdays")
    if (p3 > 0f) {
        val cell = w * 0.055f
        kaaba29(Offset(w * 0.8f, h * 0.32f), w * 0.07f, p3)
        house12(Offset(w * 0.22f, h * 0.36f), w * 0.09f, Color(0xFFD8C4A0), Color(0xFF8A6A48), p3, lit = 0.5f)
        val move = easeInOut(seg(ao, 0.1f, 0.4f))
        for (k in 0 until 3) {
            val g = seg(tf, 0.05f + 0.1f * k, 0.15f + 0.1f * k)
            val c = Offset(w * (0.88f - 0.075f * k) - move * w * 0.0f, h * 0.48f)
            drawRoundRect(HajjC70.Lit, c - Offset(cell * 0.5f, cell * 0.5f), Size(cell, cell), CornerRadius(cell * 0.2f), alpha = p3 * g)
        }
        if (ao > 0f) {
            val c = Offset(w * 0.65f, h * 0.48f)
            drawRoundRect(Fq.Green, c - Offset(cell * 0.5f, cell * 0.5f), Size(cell, cell), CornerRadius(cell * 0.2f), alpha = p3 * seg(ao, 0.2f, 0.35f), style = Stroke(3f * dp))
            hands12(c + Offset(0f, h * 0.16f), h * 0.1f, HajjC70.Person, p3 * seg(ao, 0.3f, 0.45f))
            cup12(c + Offset(0f, cell * 0.3f), cell * 0.6f, Fq.Water, p3 * seg(ao, 0.4f, 0.55f))
            tagAt(a.tag("arafaopen"), c + Offset(0f, -h * 0.12f), p3 * seg(ao, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Jade)
        }
        for (k in 0 until 7) {
            val g = seg(tf, 0.5f + 0.05f * k, 0.6f + 0.05f * k)
            val c = Offset(w * (0.42f - 0.06f * k), h * 0.58f + (k % 2) * cell * 0.0f)
            drawRoundRect(HajjC70.Lit, c - Offset(cell * 0.4f, cell * 0.4f), Size(cell * 0.8f, cell * 0.8f), CornerRadius(cell * 0.2f), alpha = p3 * g)
        }
        tagAt(a.tag("tenfast", 0, 0), Offset(w * 0.8f, h * 0.66f), p3 * seg(tf, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("tenfast", 0, 1), Offset(w * 0.25f, h * 0.74f), p3 * seg(tf, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p5 = fqIn(a, "fourdays")
    if (p5 > 0f) {
        val pb = a.b("fourdays")
        val cell = w * 0.12f
        for (k in 0 until 4) {
            val g = seg(pb, 0.1f + 0.15f * k, 0.25f + 0.15f * k)
            val c = Offset(w * (0.77f - 0.18f * k), h * 0.45f)
            drawRoundRect(if (k == 0) HajjC70.Lit else lerpColor(HajjC70.Lit, Color.White, 0.3f), c - Offset(cell * 0.5f, cell * 0.5f), Size(cell, cell), CornerRadius(cell * 0.15f), alpha = p5 * g)
            sun12(c + Offset(0f, -cell * 0.05f), cell * 0.18f, p5 * g)
        }
        tagAt(a.tag("fourdays"), Offset(w * 0.5f, h * 0.72f), p5 * seg(pb, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 12. hajj-tazawwud: the last days, and the farewell ═════════════

/**
 * Beats: stayworship (the House at night with people circling and a worshipper bowing: «ذِكْرَ اللَّهِ», «الصَّلَاةِ وَالطَّوَافِ»),
 * doubledhere (one good deed becomes many sparks; a dark speck weighs heavy: «مُضَاعَفَةٌ», «عَظِيمَةٌ شَدِيدَةٌ»), farewelltawaf (one
 * last round, then the road home: «طَوَافُ الْوَدَاعِ», «آخِرَ عَهْدِهِمْ بِالْبَيْتِ»), walkonward (he walks off facing his way: «مَضَى
 * عَلَى وَجْهِهِ»), madinahmosque (the green dome and a thousand small lights: «خَيْرٌ مِنْ أَلْفِ صَلَاةٍ»).
 */
internal fun DrawScope.farewellhouseScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "stayworship" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "doubledhere" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "farewelltawaf" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "walkonward" to Shot(Offset(w * 0.4f, h * 0.6f), 1.1f),
        "madinahmosque" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { farewellHouseDraw(a) }
}

private fun DrawScope.farewellHouseDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ground = h * 0.78f
    nightSky(t, HajjC70.Sky, HajjC70.SkyLow, ground, starAlpha = 0.7f)
    drawRect(Color(0xFF2A2A36), Offset(0f, ground), Size(w, h - ground))
    val p1 = fqIn(a, "stayworship", "doubledhere")
    if (p1 > 0f) {
        val pb = a.b("stayworship")
        val c = Offset(w * 0.55f, ground + h * 0.02f)
        softGlow(c + Offset(0f, -h * 0.1f), w * 0.2f, HajjC70.Lit, p1 * 0.3f)
        fqCircling(c + Offset(0f, -h * 0.03f), w * 0.26f, h * 0.08f, t, Fq.White, p1, n = 18, speed = 0.06f)
        kaaba29(c, w * 0.12f, p1)
        val bow = (sin(t * 1.3f) * 0.5f + 0.5f) * seg(pb, 0.4f, 0.5f)
        w8Worshipper(Offset(w * 0.15f, ground + h * 0.12f), h * 0.22f, Fq.White, 0f, bow, p1 * seg(pb, 0.3f, 0.45f))
        tagAt(a.tag("stayworship", 0, 0), Offset(w * 0.5f, h * 0.18f), p1 * seg(pb, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("stayworship", 0, 1), Offset(w * 0.18f, h * 0.4f), p1 * seg(pb, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p2 = fqIn(a, "doubledhere", "farewelltawaf")
    if (p2 > 0f) {
        val pb = a.b("doubledhere")
        val src = Offset(w * 0.72f, h * 0.55f)
        val n = (1 + 20 * seg(pb, 0.1f, 0.45f)).toInt()
        for (k in 0 until n) {
            val ang = k * 2.4f
            val r = w * 0.03f * kotlin.math.sqrt(k.toFloat())
            softGlow(src + Offset(r * cos(ang), r * sin(ang) * 0.8f), w * 0.012f, HajjC70.Lit, p2)
        }
        tagAt(a.tag("doubledhere", 0, 0), src + Offset(0f, -h * 0.25f), p2 * seg(pb, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(pb, 0.55f, 0.7f)
        val sp = Offset(w * 0.25f, mix(h * 0.4f, h * 0.62f, easeOut(seg(pb, 0.6f, 0.85f))))
        drawCircle(Color(0xFF0A0810), w * 0.04f, sp, alpha = p2 * g)
        drawCircle(Color(0xFF6A4050), w * 0.04f, sp, alpha = p2 * g, style = Stroke(2f * density))
        tagAt(a.tag("doubledhere", 0, 1), Offset(w * 0.25f, h * 0.2f), p2 * seg(pb, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ember)
    }
    val p3 = fqIn(a, "farewelltawaf", "madinahmosque")
    val wo = a.b("walkonward")
    if (p3 > 0f) {
        val pb = a.b("farewelltawaf")
        val c = Offset(w * 0.75f, ground + h * 0.02f)
        softGlow(c + Offset(0f, -h * 0.08f), w * 0.15f, HajjC70.Lit, p3 * 0.35f)
        kaaba29(c, w * 0.09f, p3)
        val circ = seg(pb, 0.05f, 0.5f)
        val ang = 2f * PI.toFloat() * circ - PI.toFloat() * 0.5f
        val round = Offset(c.x + w * 0.12f * cos(ang), c.y - h * 0.04f * sin(ang) + h * 0.04f)
        val away = seg(pb, 0.5f, 1f) * 0.4f + seg(wo, 0f, 1f) * 0.6f
        val pos = if (circ < 1f) round else Offset(mix(round.x, w * 0.1f, away), mix(round.y, ground + h * 0.12f, away))
        fqRoad(Offset(c.x - w * 0.05f, ground + h * 0.1f), Offset(-w * 0.05f, ground + h * 0.14f), h * 0.05f, Color(0xFF4A4038), p3 * seg(pb, 0.4f, 0.55f))
        fqPilgrim(pos, h * 0.2f, p3, walk = t * 9f)
        tagAt(a.tag("farewelltawaf", 0, 0), Offset(w * 0.72f, h * 0.2f), p3 * seg(pb, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("farewelltawaf", 0, 1), Offset(w * 0.35f, h * 0.35f), p3 * seg(pb, 0.4f, 0.55f) * (1f - seg(wo, 0f, 0.2f)), back = Fq.Back, ink = ArtInk.Ivory)
        if (wo > 0f) {
            tick14(Offset(w * 0.3f, h * 0.55f), w * 0.05f, seg(wo, 0.3f, 0.5f))
            tagAt(a.tag("walkonward"), Offset(w * 0.35f, h * 0.35f), p3 * seg(wo, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Jade)
        }
    }
    val p5 = fqIn(a, "madinahmosque")
    if (p5 > 0f) {
        val pb = a.b("madinahmosque")
        val foot = Offset(w * 0.5f, ground)
        softGlow(foot + Offset(0f, -h * 0.25f), w * 0.25f, HajjC70.Green, p5 * 0.35f)
        domed29(foot, w * 0.34f, Color(0xFFE6DCC6), HajjC70.Green, p5)
        for (k in 0 until 2) drawRect(Color(0xFFE6DCC6), Offset(w * (0.27f + 0.43f * k), ground - h * 0.42f), Size(w * 0.03f, h * 0.42f), alpha = p5)
        val n = (60 * seg(pb, 0.2f, 0.8f)).toInt()
        for (k in 0 until n) {
            val x = w * (0.05f + 0.9f * fract(k * 0.618f))
            val y = h * (0.06f + 0.3f * fract(k * 0.381f + 0.2f))
            drawCircle(HajjC70.Lit, w * 0.005f, Offset(x, y), alpha = p5 * (0.5f + 0.5f * sin(t * 2f + k)))
        }
        tagAt(a.tag("madinahmosque"), Offset(w * 0.5f, h * 0.88f), p5 * seg(pb, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
