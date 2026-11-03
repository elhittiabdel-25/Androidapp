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
 * Wave 61 (2026-11-03): «الأربعون النووية», hadiths 21–25. Say «I believe», then go straight; the man who asked if his duties
 * were enough; purity is half of faith, written up in the notebook; injustice forbidden, and the needs of every servant; charity
 * that is not only money. In the qudsi hadith only words stand for what Allah says of Himself; Allah is never drawn.
 */

private val Back61 = Color(0xE6181420)

// ═════════════ 1. then go straight (nw-21) ═════════════

private object StraightC61 {
    val Sky = Color(0xFFC4DAEA)
    val SkyLow = Color(0xFFF2E8D2)
    val Ground = Color(0xFFCDB890)
    val Road = Color(0xFFE8DCC0)
    val Edge = Color(0xFF8A6A44)
    val Gold = Color(0xFFD89A2A)
    val Person = Color(0xFF3A3040)
    val Garden = Color(0xFF4E8A54)
    val Good = Color(0xFF2E7D57)
    val Bad = Color(0xFFB4492A)
}

/**
 * Beats: sufyanasks (a roundel with his name, «سُفْيَانُ بْنُ عَبْدِ اللَّهِ», asks; two answers come down one after the other:
 * «قُلْ آمَنْتُ بِاللَّهِ», «ثُمَّ اسْتَقِمْ»), fouralong (four marks along one straight road: «مَعْرِفَةُ ذَلِكَ», «وَاعْتِقَادُهُ»,
 * «وَالْعَمَلُ بِهِ», «وَالِاسْتِقَامَةُ عَلَيْهِ»), safeend (a figure keeps to the road to its end, where a garden lies under a dome
 * of light), bothsides (along the road, ticks on one side and crosses on the other: «فِعْلُ الْوَاجِبَاتِ», «وَتَرْكُ
 * الْمُحَرَّمَاتِ»).
 */
internal fun DrawScope.straightpathScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sufyanasks" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "fouralong" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "safeend" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "bothsides" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { straightDay(a) }
}

private fun DrawScope.straightDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sa = a.b("sufyanasks")
    val fa = a.b("fouralong")
    val se = a.b("safeend")
    val bs = a.b("bothsides")
    val ground = h * 0.45f
    daySky12(StraightC61.Sky, StraightC61.SkyLow, ground, StraightC61.Ground)
    val road = { alpha: Float, p: Float ->
        drawPath(Path().apply { moveTo(w * 0.4f, h); lineTo(w * 0.5f - w * 0.012f, ground + (h - ground) * (1f - p)); lineTo(w * 0.5f + w * 0.012f, ground + (h - ground) * (1f - p)); lineTo(w * 0.6f, h); close() }, StraightC61.Road, alpha = alpha)
    }
    if (sa > 0f) {
        val f = easeOut(seg(sa, 0f, 0.25f)) * (1f - seg(fa, 0f, 0.4f))
        nameRoundel(Offset(w * 0.5f, h * 0.7f), w * 0.1f, a.big("sufyanasks", 0, 0), Roundel.Fills[1], Roundel.Rim, f * seg(sa, 0.05f, 0.25f))
        for (k in 0 until 2) {
            val g = seg(sa, 0.35f + 0.25f * k, 0.5f + 0.25f * k)
            val c = Offset(w * (0.68f - 0.36f * k), h * 0.25f)
            softGlow(c, w * 0.12f, StraightC61.Gold, 0.3f * f * g)
            tagAt(a.tag("sufyanasks", 0, k + 1), c, f * g, back = Back61, ink = ArtInk.Gold)
        }
        drawLine(StraightC61.Gold, Offset(w * 0.5f, h * 0.58f), Offset(w * 0.5f, h * 0.32f), 3f * dp, StrokeCap.Round, alpha = f * seg(sa, 0.75f, 0.9f))
    }
    if (fa > 0f) {
        val f = easeOut(seg(fa, 0f, 0.25f)) * (1f - seg(se, 0f, 0.4f))
        road(f, seg(fa, 0f, 0.2f))
        for (k in 0 until 4) {
            val g = seg(fa, 0.12f + 0.2f * k, 0.28f + 0.2f * k)
            val y = mix(h * 0.92f, ground + h * 0.08f, k / 3f)
            val half = mix(w * 0.1f, w * 0.015f, k / 3f) + w * 0.01f
            drawLine(StraightC61.Gold, Offset(w * 0.5f - half, y), Offset(w * 0.5f + half, y), 4f * dp, StrokeCap.Round, alpha = f * g)
            tagAt(a.tag("fouralong", 0, k), Offset(w * (if (k % 2 == 0) 0.78f else 0.22f), y), f * g, back = Back61, ink = ArtInk.Gold)
        }
    }
    if (se > 0f) {
        val f = easeOut(seg(se, 0f, 0.25f)) * (1f - seg(bs, 0f, 0.4f))
        road(f, 1f)
        val end = Offset(w * 0.5f, ground)
        for (k in 0 until 5) palm12(end + Offset(w * (0.06f * k - 0.12f), h * 0.01f), h * 0.12f, StraightC61.Edge, StraightC61.Garden, f * seg(se, 0.5f, 0.7f), t + k)
        guardDome12(end, w * 0.18f, seg(se, 0.6f, 0.85f), t, f)
        val u = easeInOut(seg(se, 0.05f, 0.8f))
        val foot = Offset(w * 0.5f, mix(h * 0.96f, ground + h * 0.02f, u))
        figure(foot, h * mix(0.3f, 0.06f, u), StraightC61.Person, f, walk = t * 5f)
    }
    if (bs > 0f) {
        val f = easeOut(seg(bs, 0f, 0.25f))
        road(f, 1f)
        for (k in 0 until 4) {
            val y = mix(h * 0.9f, ground + h * 0.1f, k / 3f)
            val s = mix(w * 0.04f, w * 0.015f, k / 3f)
            checkMark(Offset(w * (0.68f - 0.04f * k), y), s, StraightC61.Good, 3f * dp, f * seg(bs, 0.1f + 0.08f * k, 0.2f + 0.08f * k))
            crossMark(Offset(w * (0.32f + 0.04f * k), y), s, StraightC61.Bad, 3f * dp, f * seg(bs, 0.45f + 0.08f * k, 0.55f + 0.08f * k))
        }
        tagAt(a.tag("bothsides", 0, 0), Offset(w * 0.8f, h * 0.3f), f * seg(bs, 0.1f, 0.25f), back = Back61, ink = ArtInk.Gold)
        tagAt(a.tag("bothsides", 0, 1), Offset(w * 0.2f, h * 0.3f), f * seg(bs, 0.45f, 0.6f), back = Back61)
    }
}

// ═════════════ 2. will I enter Paradise (nw-22) ═════════════

private object JannaC61 {
    val Night = Color(0xFF121C2C)
    val NightLow = Color(0xFF2C3A54)
    val Ground = Color(0xFF1C2432)
    val Gold = Color(0xFFF2C860)
    val Gate = Color(0xFFD8C8A8)
    val Good = Color(0xFF6FCB9F)
    val Dim = Color(0xFF6A6A78)
    val Dark = Color(0xFF3A3040)
    val Lamp = Color(0xFFB8925A)
    val Garden = Color(0xFF3E7A54)
}

/**
 * Beats: fourticks (four lines are ticked, «صَلَّيْتُ الْمَكْتُوبَاتِ», «وَصُمْتُ رَمَضَانَ», «وَأَحْلَلْتُ الْحَلَالَ», «وَحَرَّمْتُ
 * الْحَرَامَ», and the answer comes: «نَعَمْ»), heldbelief (a hand takes the good with a light in the heart, and draws back from
 * the dark), mercygate (a gate opens on a garden: «بِفَضْلِ اللَّهِ وَرَحْمَتِهِ»), notlisted (coins and a far house of
 * pilgrimage stay faint, not on the list), neverdrops (a lamp of prayer stays lit through day and night going by).
 */
internal fun DrawScope.intojannaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fourticks" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "heldbelief" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "mercygate" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "notlisted" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "neverdrops" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { jannaNight(a) }
}

private fun DrawScope.jannaNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ft = a.b("fourticks")
    val hb = a.b("heldbelief")
    val mg = a.b("mercygate")
    val nl = a.b("notlisted")
    val nd = a.b("neverdrops")
    val ground = h * 0.86f
    nightSky(t, JannaC61.Night, JannaC61.NightLow, ground, starAlpha = 0.8f)
    drawRect(JannaC61.Ground, Offset(0f, ground), Size(w, h - ground))
    if (ft > 0f) {
        val f = easeOut(seg(ft, 0f, 0.25f)) * (1f - seg(hb, 0f, 0.4f))
        for (k in 0 until 4) {
            val g = seg(ft, 0.08f + 0.16f * k, 0.22f + 0.16f * k)
            val y = h * (0.22f + 0.13f * k)
            tagAt(a.tag("fourticks", 0, k), Offset(w * 0.45f, y), f * g, back = Back61)
            checkMark(Offset(w * 0.82f, y), w * 0.03f, JannaC61.Good, 3f * dp, f * seg(ft, 0.15f + 0.16f * k, 0.25f + 0.16f * k))
        }
        val yes = seg(ft, 0.78f, 0.92f)
        softGlow(Offset(w * 0.5f, h * 0.8f), w * 0.12f, JannaC61.Gold, 0.5f * f * yes)
        tagAt(a.tag("fourticks", 0, 4), Offset(w * 0.5f, h * 0.8f), f * yes, back = Back61, ink = ArtInk.Gold)
    }
    if (hb > 0f) {
        val f = easeOut(seg(hb, 0f, 0.25f)) * (1f - seg(mg, 0f, 0.4f))
        val heart = Offset(w * 0.5f, h * 0.3f)
        heart12(heart, w * 0.05f, JannaC61.Gold, f * seg(hb, 0.05f, 0.2f), fill = true)
        softGlow(heart, w * 0.1f, JannaC61.Gold, 0.4f * f)
        val take = seg(hb, 0.2f, 0.45f)
        hands12(Offset(w * 0.3f, h * 0.6f - h * 0.05f * take), h * 0.1f, JannaC61.Gate, f)
        drawCircle(JannaC61.Good, w * 0.03f, Offset(w * 0.3f, h * 0.5f - h * 0.05f * take), alpha = f * take)
        val pull = seg(hb, 0.55f, 0.8f)
        hands12(Offset(w * 0.7f + w * 0.08f * pull, h * 0.6f), h * 0.1f, JannaC61.Gate, f)
        drawCircle(JannaC61.Dark, w * 0.035f, Offset(w * 0.62f, h * 0.55f), alpha = f * seg(hb, 0.45f, 0.55f))
        drawLine(JannaC61.Gold, heart, Offset(w * 0.3f, h * 0.48f), 1.5f * dp, alpha = f * 0.5f * take)
        drawLine(JannaC61.Gold, heart, Offset(w * 0.74f, h * 0.52f), 1.5f * dp, alpha = f * 0.5f * pull)
    }
    if (mg > 0f) {
        val f = easeOut(seg(mg, 0f, 0.25f)) * (1f - seg(nl, 0f, 0.4f))
        val foot = Offset(w * 0.5f, ground)
        val open = easeInOut(seg(mg, 0.2f, 0.6f))
        drawRect(JannaC61.Garden, Offset(foot.x - w * 0.18f, foot.y - h * 0.42f), Size(w * 0.36f, h * 0.42f), alpha = f * open)
        lightShafts(foot + Offset(0f, -h * 0.2f), -PI.toFloat() / 2f, 0.8f, h * 0.4f, JannaC61.Gold, 0.4f * f * open, t)
        for (k in 0 until 2) {
            val dir = if (k == 0) -1f else 1f
            val wd = w * 0.18f * (1f - open)
            val x = if (k == 0) foot.x - w * 0.18f else foot.x + w * 0.18f - wd
            drawRect(JannaC61.Gate, Offset(x, foot.y - h * 0.42f), Size(wd, h * 0.42f), alpha = f)
            drawLine(JannaC61.Lamp, Offset(foot.x + dir * w * 0.2f, foot.y), Offset(foot.x + dir * w * 0.2f, foot.y - h * 0.46f), 6f * dp, alpha = f)
        }
        tagAt(a.tag("mercygate"), Offset(w * 0.5f, h * 0.2f), f * seg(mg, 0.5f, 0.7f), back = Back61, ink = ArtInk.Gold)
    }
    if (nl > 0f) {
        val f = easeOut(seg(nl, 0f, 0.25f)) * (1f - seg(nd, 0f, 0.4f))
        for (k in 0 until 5) coin12(Offset(w * (0.25f + 0.03f * (k % 3)), ground - h * (0.03f + 0.03f * (k / 3))), w * 0.02f, f * 0.35f * seg(nl, 0.1f, 0.3f), JannaC61.Dim)
        kaaba29(Offset(w * 0.7f, ground), w * 0.16f, f * 0.35f * seg(nl, 0.45f, 0.65f))
        drawLine(JannaC61.Dim, Offset(w * 0.1f, h * 0.35f), Offset(w * 0.9f, h * 0.35f), 2f * dp, alpha = f * seg(nl, 0.6f, 0.8f))
        drawCircle(JannaC61.Gold, w * 0.012f, Offset(w * 0.75f, h * 0.35f), alpha = f * seg(nl, 0.7f, 0.85f))
    }
    if (nd > 0f) {
        val f = easeOut(seg(nd, 0f, 0.25f))
        val cyc = fract(nd * 2f)
        val day = sin(PI.toFloat() * cyc)
        drawRect(Color(0xFF8AB4D8), Offset.Zero, Size(w, ground), alpha = f * 0.5f * day)
        val c = Offset(w * 0.5f, ground - h * 0.2f)
        drawLine(JannaC61.Lamp, c + Offset(0f, h * 0.05f), Offset(c.x, ground), 3f * dp, alpha = f)
        drawOval(JannaC61.Lamp, Offset(c.x - w * 0.04f, c.y - h * 0.03f), Size(w * 0.08f, h * 0.09f), alpha = f)
        softGlow(c, w * 0.12f, JannaC61.Gold, f * (0.8f + 0.1f * sin(t * 3f)))
        sun12(Offset(w * (0.1f + 0.8f * cyc), h * 0.2f - h * 0.1f * day), w * 0.03f, f * day)
    }
}

// ═════════════ 3. half of faith (nw-23, notebook) ═════════════

/**
 * Beats: halffilled (a circle in the notebook; half of it is shaded in: «شَطْرُ الْإِيمَانِ»), praisefills (a scale drawn in ink
 * whose pan fills: «الْحَمْدُ لِلَّهِ تَمْلَأُ الْمِيزَانَ»), lightproofglow (three marks: a lamp, a seal and a sun, «نُورٌ»,
 * «بُرْهَانٌ», «ضِيَاءٌ»), patiencethree (three boxes: «عَلَى الطَّاعَاتِ», «وَعَنِ الْمَعَاصِي», «وَعَلَى أَقْدَارِ اللَّهِ
 * الْمُؤْلِمَةِ»), proofforagainst (a book with two arrows, one to each side: «حُجَّةٌ لَكَ», «وَعَلَيْكَ»).
 */
internal fun DrawScope.halfofimanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "halffilled" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "praisefills" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "lightproofglow" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "patiencethree" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "proofforagainst" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0f) { halfPage(a) }
}

private fun DrawScope.halfPage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pp = Paper(a.dark)
    paperSheet(pp)
    val hf = a.b("halffilled")
    val pf = a.b("praisefills")
    val lp = a.b("lightproofglow")
    val pt = a.b("patiencethree")
    val pa = a.b("proofforagainst")
    val pen = 2.2f * dp
    if (hf > 0f) {
        val f = easeOut(seg(hf, 0f, 0.25f)) * (1f - seg(pf, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.55f)
        val r = w * 0.2f
        val shade = seg(hf, 0.3f, 0.7f)
        drawArc(pp.good, 90f, 180f * shade, true, Offset(c.x - r, c.y - r), Size(r * 2f, r * 2f), alpha = f * 0.4f)
        drawCircle(pp.ink, r, c, alpha = f * seg(hf, 0.05f, 0.25f), style = Stroke(pen))
        penLine(listOf(Offset(c.x, c.y - r), Offset(c.x, c.y + r)), seg(hf, 0.2f, 0.35f), pp.ink, pen, f)
        note(a.tag("halffilled"), Offset(w * 0.5f, h * 0.2f), f * seg(hf, 0.4f, 0.6f), pp, pp.good)
    }
    if (pf > 0f) {
        val f = easeOut(seg(pf, 0f, 0.25f)) * (1f - seg(lp, 0f, 0.4f))
        val top = Offset(w * 0.5f, h * 0.35f)
        val tilt = 0.3f * easeInOut(seg(pf, 0.45f, 0.8f))
        penLine(listOf(top, Offset(w * 0.5f, h * 0.8f)), seg(pf, 0.05f, 0.2f), pp.ink, pen, f)
        val l = top + Offset(-w * 0.25f * cos(tilt), w * 0.25f * sin(tilt))
        val r = top + Offset(w * 0.25f * cos(tilt), -w * 0.25f * sin(tilt))
        penLine(listOf(l, r), seg(pf, 0.1f, 0.25f), pp.ink, pen, f)
        for (p in listOf(l, r)) penLine(listOf(p + Offset(-w * 0.08f, h * 0.12f), p, p + Offset(w * 0.08f, h * 0.12f)), seg(pf, 0.2f, 0.35f), pp.ink, pen, f)
        val fill = seg(pf, 0.35f, 0.75f)
        drawArc(pp.amber, 0f, 180f, true, Offset(l.x - w * 0.08f, l.y + h * 0.12f - h * 0.05f * fill), Size(w * 0.16f, h * 0.1f * fill + 1f), alpha = f * 0.6f)
        note(a.tag("praisefills"), Offset(w * 0.5f, h * 0.18f), f * seg(pf, 0.3f, 0.5f), pp, pp.amber)
    }
    if (lp > 0f) {
        val f = easeOut(seg(lp, 0f, 0.25f)) * (1f - seg(pt, 0f, 0.4f))
        for (k in 0 until 3) {
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.45f)
            val g = seg(lp, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            when (k) {
                0 -> { penLine(listOf(c + Offset(0f, h * 0.06f), c + Offset(0f, h * 0.12f)), g, pp.ink, pen, f); drawOval(pp.amber, Offset(c.x - w * 0.03f, c.y - h * 0.03f), Size(w * 0.06f, h * 0.08f), alpha = f * g, style = Stroke(pen)); softGlow(c, w * 0.05f, pp.amber, 0.5f * f * g) }
                1 -> { drawCircle(pp.bad, w * 0.045f, c, alpha = f * g, style = Stroke(pen)); checkMark(c, w * 0.025f, pp.bad, pen, f * g) }
                else -> { drawCircle(pp.amber, w * 0.04f, c, alpha = f * g); for (j in 0 until 8) { val ang = j * PI.toFloat() / 4f; penLine(listOf(c + Offset(cos(ang), sin(ang)) * (w * 0.055f), c + Offset(cos(ang), sin(ang)) * (w * 0.075f)), g, pp.amber, pen, f) } }
            }
            note(a.tag("lightproofglow", 0, k), c + Offset(0f, h * 0.18f), f * g, pp, if (k == 2) pp.amber else pp.ink)
        }
    }
    if (pt > 0f) {
        val f = easeOut(seg(pt, 0f, 0.25f)) * (1f - seg(pa, 0f, 0.4f))
        for (k in 0 until 3) {
            val g = seg(pt, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            note(a.tag("patiencethree", 0, k), Offset(w * 0.5f, h * (0.28f + 0.2f * k)), f * g, pp, if (k == 0) pp.good else pp.ink, boxed = true)
        }
    }
    if (pa > 0f) {
        val f = easeOut(seg(pa, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.45f)
        penLine(listOf(c + Offset(-w * 0.12f, -h * 0.08f), c + Offset(0f, -h * 0.05f), c + Offset(w * 0.12f, -h * 0.08f), c + Offset(w * 0.12f, h * 0.08f), c + Offset(0f, h * 0.11f), c + Offset(-w * 0.12f, h * 0.08f), c + Offset(-w * 0.12f, -h * 0.08f)), seg(pa, 0.05f, 0.25f), pp.ink, pen, f)
        penLine(listOf(c + Offset(0f, -h * 0.05f), c + Offset(0f, h * 0.11f)), seg(pa, 0.2f, 0.3f), pp.ink, pen, f)
        for (k in 0 until 2) {
            val dir = if (k == 0) 1f else -1f
            val g = seg(pa, 0.35f + 0.25f * k, 0.5f + 0.25f * k)
            val from = c + Offset(dir * w * 0.15f, 0f)
            val to = c + Offset(dir * w * 0.32f, h * 0.15f)
            penLine(listOf(from, to), g, if (k == 0) pp.good else pp.bad, pen, f)
            arrowHead(to, to - from, w * 0.02f, if (k == 0) pp.good else pp.bad, pen, f * g)
            note(a.tag("proofforagainst", 0, k), to + Offset(0f, h * 0.08f), f * g, pp, if (k == 0) pp.good else pp.bad)
        }
    }
}

// ═════════════ 4. injustice forbidden; the needs of every servant (nw-24) ═════════════

private object JustC61 {
    val Night = Color(0xFF14142A)
    val NightLow = Color(0xFF30284A)
    val Ground = Color(0xFF201C2C)
    val Gold = Color(0xFFF2C860)
    val Peg = Color(0xFFD8CCB4)
    val Hole = Color(0xFF3A3448)
    val Bread = Color(0xFFC89A5A)
    val Robe = Color(0xFF5A7AA8)
    val Water = Color(0xFF8AC4EA)
    val Spot = Color(0xFF3A3040)
}

/**
 * Beats: forbadeself (words written large in light on an open scroll, «حَرَّمَ الظُّلْمَ عَلَى نَفْسِهِ», over a perfectly level
 * line), wrongplace (a square block is forced at a round hole; it is taken back to the square one where it fits: «وَضْعُ
 * الشَّيْءِ فِي غَيْرِ مَوْضِعِهِ»), fourneeds (plain roundels each receive what they need: «الْهِدَايَةِ», «وَالطَّعَامِ»,
 * «وَالْكِسْوَةِ», «وَالْمَغْفِرَةِ»), curedone (two roundels with dark spots: one washes his with light and it clears, the other
 * leaves it), nothingadded (rows without end of roundels; a word above them does not change: «فَهُوَ الْغَنِيُّ بِذَاتِهِ»).
 */
internal fun DrawScope.noinjusticeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "forbadeself" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "wrongplace" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "fourneeds" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "curedone" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "nothingadded" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { justNight(a) }
}

private fun DrawScope.justNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fs = a.b("forbadeself")
    val wp = a.b("wrongplace")
    val fn = a.b("fourneeds")
    val cd = a.b("curedone")
    val na = a.b("nothingadded")
    val ground = h * 0.86f
    nightSky(t, JustC61.Night, JustC61.NightLow, ground, starAlpha = 0.9f)
    drawRect(JustC61.Ground, Offset(0f, ground), Size(w, h - ground))
    if (fs > 0f) {
        val f = easeOut(seg(fs, 0f, 0.25f)) * (1f - seg(wp, 0f, 0.4f))
        scroll14(Offset(w * 0.5f, h * 0.48f), w * 0.8f, h * 0.36f, easeOut(seg(fs, 0.05f, 0.3f)), f)
        fitBig26(a.big("forbadeself"), Offset(w * 0.5f, h * 0.39f), w * 0.6f, Color(0xFF2E3A4A), f * seg(fs, 0.25f, 0.45f))
        drawLine(JustC61.Gold, Offset(w * 0.2f, h * 0.58f), Offset(w * 0.8f, h * 0.58f), 3f * dp, StrokeCap.Round, alpha = f * seg(fs, 0.5f, 0.7f))
        softGlow(Offset(w * 0.5f, h * 0.58f), w * 0.2f, JustC61.Gold, 0.25f * f * seg(fs, 0.5f, 0.8f))
    }
    if (wp > 0f) {
        val f = easeOut(seg(wp, 0f, 0.25f)) * (1f - seg(fn, 0f, 0.4f))
        val round = Offset(w * 0.3f, h * 0.62f)
        val square = Offset(w * 0.7f, h * 0.62f)
        drawCircle(JustC61.Hole, w * 0.06f, round, alpha = f)
        drawRect(JustC61.Hole, Offset(square.x - w * 0.06f, square.y - w * 0.06f), Size(w * 0.12f, w * 0.12f), alpha = f)
        val press = sin(PI.toFloat() * seg(wp, 0.1f, 0.45f))
        val move = easeInOut(seg(wp, 0.5f, 0.8f))
        val c = Offset(mix(round.x, square.x, move), mix(round.y - h * 0.1f + h * 0.03f * press, square.y, easeOut(seg(wp, 0.75f, 0.9f))) - (if (move > 0f && move < 1f) h * 0.1f * sin(PI.toFloat() * move) else 0f))
        drawRect(JustC61.Peg, Offset(c.x - w * 0.055f, c.y - w * 0.055f), Size(w * 0.11f, w * 0.11f), alpha = f)
        if (wp > 0.85f) softGlow(square, w * 0.1f, JustC61.Gold, 0.6f * f)
        tagAt(a.tag("wrongplace"), Offset(w * 0.5f, h * 0.25f), f * seg(wp, 0.1f, 0.3f), back = Back61, ink = ArtInk.Gold)
    }
    if (fn > 0f) {
        val f = easeOut(seg(fn, 0f, 0.25f)) * (1f - seg(cd, 0f, 0.4f))
        for (k in 0 until 4) {
            val c = Offset(w * (0.82f - 0.21f * k), h * 0.6f)
            val g = seg(fn, 0.08f + 0.2f * k, 0.25f + 0.2f * k)
            nameRoundel(c, w * 0.045f, null, Roundel.PlainFill, Roundel.PlainRim, f)
            val gift = c + Offset(0f, -h * 0.13f)
            when (k) {
                0 -> softGlow(gift, w * 0.04f, JustC61.Gold, f * g)
                1 -> drawOval(JustC61.Bread, Offset(gift.x - w * 0.035f, gift.y - h * 0.015f), Size(w * 0.07f, h * 0.03f), alpha = f * g)
                2 -> drawPath(Path().apply { moveTo(gift.x - w * 0.03f, gift.y - h * 0.03f); lineTo(gift.x + w * 0.03f, gift.y - h * 0.03f); lineTo(gift.x + w * 0.04f, gift.y + h * 0.03f); lineTo(gift.x - w * 0.04f, gift.y + h * 0.03f); close() }, JustC61.Robe, alpha = f * g)
                else -> for (j in 0 until 4) drawCircle(JustC61.Water, w * 0.006f, gift + Offset(w * 0.012f * (j - 1.5f), h * 0.01f * (j % 2)), alpha = f * g)
            }
            tagAt(a.tag("fourneeds", 0, k), c + Offset(0f, h * 0.12f), f * g, back = Back61, ink = ArtInk.Gold)
        }
    }
    if (cd > 0f) {
        val f = easeOut(seg(cd, 0f, 0.25f)) * (1f - seg(na, 0f, 0.4f))
        for (k in 0 until 2) {
            val c = Offset(w * (0.7f - 0.4f * k), h * 0.52f)
            nameRoundel(c, w * 0.09f, null, Roundel.PlainFill, Roundel.PlainRim, f)
            val clear = if (k == 0) seg(cd, 0.35f, 0.75f) else 0f
            for (j in 0 until 3) drawCircle(JustC61.Spot, w * 0.018f, c + Offset(w * 0.03f * (j - 1), h * 0.02f * (j % 2)), alpha = f * (1f - clear))
            if (k == 0) {
                for (j in 0 until 6) {
                    val u = fract(t * 1.1f + j / 6f)
                    drawCircle(JustC61.Water, w * 0.005f, c + Offset(w * 0.06f * sin(j * 1.3f), -h * 0.15f + h * 0.12f * u), alpha = f * seg(cd, 0.2f, 0.35f) * (1f - u))
                }
                softGlow(c, w * 0.12f, JustC61.Gold, 0.5f * f * clear)
            }
        }
    }
    if (na > 0f) {
        val f = easeOut(seg(na, 0f, 0.25f))
        val rows = (seg(na, 0.05f, 0.6f) * 6f).toInt() + 1
        for (r in 0 until rows) for (k in 0 until 12) {
            val c = Offset(w * (0.06f + 0.08f * k + 0.04f * (r % 2)), h * (0.88f - 0.07f * r))
            drawCircle(JustC61.Peg, w * 0.012f, c, alpha = f * (1f - 0.1f * r))
        }
        tagAt(a.tag("nothingadded"), Offset(w * 0.5f, h * 0.18f), f * seg(na, 0.3f, 0.5f), back = Back61, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. not only money (nw-25) ═════════════

private object WealthC61 {
    val Sky = Color(0xFFC6DAE8)
    val SkyLow = Color(0xFFF4EAD4)
    val Ground = Color(0xFFCDB890)
    val Gold = Color(0xFFD89A2A)
    val Bag = Color(0xFF8A6A44)
    val Ink = Color(0xFF2E3A4A)
    val Step = Color(0xFFE8DCC4)
}

/**
 * Beats: richpoor (two rows of roundels pray alike; only one row has bags of coins beside it), dhikrcoins (four words, each
 * turning into a coin of light: «تَسْبِيحَةٍ», «تَكْبِيرَةٍ», «تَحْمِيدَةٍ», «تَهْلِيلَةٍ»), spreadsgood (light passes from one
 * roundel to the next and on to many), selfcharity (a roundel's own light goes out and comes back to itself), maruftiers (three
 * steps: «التَّوْحِيدُ» at the top, then «الْوَاجِبَةُ», then «الْمُسْتَحَبَّاتُ»).
 */
internal fun DrawScope.notonlywealthScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "richpoor" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "dhikrcoins" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "spreadsgood" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "selfcharity" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "maruftiers" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { wealthDay(a) }
}

private fun DrawScope.wealthDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rp = a.b("richpoor")
    val dc = a.b("dhikrcoins")
    val sg = a.b("spreadsgood")
    val sc = a.b("selfcharity")
    val mt = a.b("maruftiers")
    val ground = h * 0.86f
    daySky12(WealthC61.Sky, WealthC61.SkyLow, ground, WealthC61.Ground)
    if (rp > 0f) {
        val f = easeOut(seg(rp, 0f, 0.25f)) * (1f - seg(dc, 0f, 0.4f))
        for (r in 0 until 2) for (k in 0 until 5) {
            val c = Offset(w * (0.2f + 0.15f * k), h * (0.42f + 0.25f * r))
            nameRoundel(c, w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f)
            softGlow(c + Offset(0f, -h * 0.06f), w * 0.02f, WealthC61.Gold, 0.6f * f * seg(rp, 0.2f, 0.4f))
            if (r == 0) {
                val g = seg(rp, 0.5f + 0.05f * k, 0.6f + 0.05f * k)
                drawOval(WealthC61.Bag, Offset(c.x + w * 0.04f, c.y - h * 0.01f), Size(w * 0.035f, h * 0.045f), alpha = f * g)
            }
        }
    }
    if (dc > 0f) {
        val f = easeOut(seg(dc, 0f, 0.25f)) * (1f - seg(sg, 0f, 0.4f))
        for (k in 0 until 4) {
            val g = seg(dc, 0.08f + 0.2f * k, 0.25f + 0.2f * k)
            val turn = seg(dc, 0.18f + 0.2f * k, 0.3f + 0.2f * k)
            val x = w * (0.82f - 0.21f * k)
            tagAt(a.tag("dhikrcoins", 0, k), Offset(x, h * 0.35f), f * g, back = Back61, ink = ArtInk.Gold)
            coin12(Offset(x, h * 0.35f + h * 0.2f * turn), w * 0.035f, f * turn)
            softGlow(Offset(x, h * 0.55f), w * 0.05f, WealthC61.Gold, 0.5f * f * turn)
        }
    }
    if (sg > 0f) {
        val f = easeOut(seg(sg, 0f, 0.25f)) * (1f - seg(sc, 0f, 0.4f))
        val src = Offset(w * 0.5f, h * 0.72f)
        nameRoundel(src, w * 0.045f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        softGlow(src, w * 0.08f, WealthC61.Gold, 0.6f * f)
        for (ring in 0 until 2) for (k in 0 until 3 + 3 * ring) {
            val ang = PI.toFloat() * (1.1f + 0.8f * k / (2f + 3f * ring))
            val c = src + Offset(cos(ang) * w * (0.18f + 0.18f * ring), sin(ang) * h * (0.18f + 0.18f * ring))
            val g = seg(sg, 0.2f + 0.3f * ring + 0.03f * k, 0.35f + 0.3f * ring + 0.03f * k)
            drawLine(WealthC61.Gold, src, c, 1.5f * dp, alpha = f * g * 0.6f)
            nameRoundel(c, w * 0.03f, null, Roundel.PlainFill, Roundel.PlainRim, f)
            softGlow(c, w * 0.05f, WealthC61.Gold, 0.5f * f * g)
        }
    }
    if (sc > 0f) {
        val f = easeOut(seg(sc, 0f, 0.25f)) * (1f - seg(mt, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.55f)
        nameRoundel(c, w * 0.08f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val u = seg(sc, 0.15f, 0.85f)
        val ang = -PI.toFloat() / 2f + 2f * PI.toFloat() * u
        softGlow(c + Offset(cos(ang) * w * 0.2f, sin(ang) * w * 0.2f), w * 0.03f, WealthC61.Gold, f * sin(PI.toFloat() * u))
        drawArc(WealthC61.Gold, -90f, 360f * u, false, Offset(c.x - w * 0.2f, c.y - w * 0.2f), Size(w * 0.4f, w * 0.4f), alpha = f * 0.5f, style = Stroke(2f * dp))
        softGlow(c, w * 0.12f, WealthC61.Gold, 0.5f * f * seg(sc, 0.8f, 1f))
    }
    if (mt > 0f) {
        val f = easeOut(seg(mt, 0f, 0.25f))
        for (k in 0 until 3) {
            val g = seg(mt, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            val top = ground - h * (0.5f - 0.15f * k)
            val x = w * 0.5f
            val half = w * (0.12f + 0.1f * k)
            drawRect(WealthC61.Step, Offset(x - half, top), Size(half * 2f, h * 0.15f), alpha = f * g)
            drawRect(WealthC61.Bag, Offset(x - half, top), Size(half * 2f, h * 0.15f), alpha = f * g, style = Stroke(1.5f * dp))
            if (k == 0) softGlow(Offset(x, top + h * 0.07f), w * 0.15f, WealthC61.Gold, 0.4f * f * g)
            tagAt(a.tag("maruftiers", 0, k), Offset(x, top + h * 0.075f), f * g, back = Color.Transparent, ink = if (k == 0) WealthC61.Gold else WealthC61.Ink)
        }
    }
}
