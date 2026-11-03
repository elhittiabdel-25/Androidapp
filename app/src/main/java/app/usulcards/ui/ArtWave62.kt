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
 * Wave 62 (2026-11-03): «الأربعون النووية», hadiths 26–30. A charity for every joint each day the sun rises; ask your heart;
 * hold fast to the Sunnah; the head of the matter, its pillar and its peak; four kinds of ruling, set out in the notebook.
 * Allah is never drawn; the Prophet ﷺ is only light; companions are names in roundels; others are generic figures.
 */

private val Back62 = Color(0xE6181420)

// ═════════════ 1. a charity for every joint (nw-26) ═════════════

private object JointC62 {
    val Sky = Color(0xFFBED6E8)
    val SkyLow = Color(0xFFF4E6C8)
    val Ground = Color(0xFFCDB890)
    val Person = Color(0xFF3A3040)
    val Gold = Color(0xFFD89A2A)
    val Basket = Color(0xFFB88A58)
    val Camel = Color(0xFFB88A58)
    val Voice = Color(0xFF5A7AA8)
    val Stone = Color(0xFF8A7A6A)
    val Thorn = Color(0xFF6A4A3A)
    val Mosque = Color(0xFFE8DCC4)
    val Dome = Color(0xFF6A8A6A)
    val Step = Color(0xFFB8A27E)
}

/**
 * Beats: jointslight (a figure; small lights come on at every joint), dutyorextra (two baskets: «وَاجِبَةً», «مَنْدُوبَةً»),
 * fivesadaqa (five small deeds glow in turn: «فَتَعْدِلُ بَيْنَ اثْنَيْنِ», «وَتُعِينُ الرَّجُلَ فِي دَابَّتِهِ», «وَالْكَلِمَةُ
 * الطَّيِّبَةُ», «وَكُلُّ خُطْوَةٍ إِلَى الصَّلَاةِ», «وَإِمَاطَةُ الْأَذَى عَنِ الطَّرِيقِ»), duhatwo (morning sun; a figure stands,
 * bows and prostrates and every joint lights: «رَكْعَتَانِ مِنَ الضُّحَى»), clearroad (a thorn, dirt and a stone are lifted off a
 * path), eachsunrise (the sun rises again and again, and each time the lights come on afresh).
 */
internal fun DrawScope.everyjointScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "jointslight" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "dutyorextra" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "fivesadaqa" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "duhatwo" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "clearroad" to Shot(Offset(w * 0.5f, h * 0.7f), 1.15f),
        "eachsunrise" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { jointDay(a) }
}

private fun DrawScope.jointDots62(foot: Offset, tall: Float, alpha: Float, p: Float) {
    val pts = listOf(
        Offset(0f, -0.88f), Offset(0f, -0.74f), Offset(-0.12f, -0.72f), Offset(0.12f, -0.72f), Offset(-0.18f, -0.58f), Offset(0.18f, -0.58f),
        Offset(-0.2f, -0.45f), Offset(0.2f, -0.45f), Offset(0f, -0.6f), Offset(0f, -0.5f), Offset(0f, -0.45f), Offset(-0.06f, -0.25f),
        Offset(0.06f, -0.25f), Offset(-0.07f, -0.02f), Offset(0.07f, -0.02f),
    )
    for ((k, q) in pts.withIndex()) {
        val g = seg(p, k / 20f, k / 20f + 0.25f)
        softGlow(foot + Offset(q.x * tall, q.y * tall), tall * 0.035f, JointC62.Gold, alpha * g)
    }
}

private fun DrawScope.jointDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val jl = a.b("jointslight")
    val de = a.b("dutyorextra")
    val fs = a.b("fivesadaqa")
    val dt = a.b("duhatwo")
    val cr = a.b("clearroad")
    val es = a.b("eachsunrise")
    val ground = h * 0.84f
    daySky12(JointC62.Sky, JointC62.SkyLow, ground, JointC62.Ground)
    if (jl > 0f) {
        val f = easeOut(seg(jl, 0f, 0.25f)) * (1f - seg(de, 0f, 0.4f))
        val foot = Offset(w * 0.5f, ground)
        val tall = h * 0.6f
        figure(foot, tall, JointC62.Person, f, arms = 0.25f)
        jointDots62(foot, tall, f, seg(jl, 0.15f, 0.9f))
    }
    if (de > 0f) {
        val f = easeOut(seg(de, 0f, 0.25f)) * (1f - seg(fs, 0f, 0.4f))
        for (k in 0 until 2) {
            val c = Offset(w * (0.7f - 0.4f * k), ground - h * 0.08f)
            val g = seg(de, 0.1f + 0.35f * k, 0.3f + 0.35f * k)
            drawArc(JointC62.Basket, 0f, 180f, true, Offset(c.x - w * 0.13f, c.y - h * 0.1f), Size(w * 0.26f, h * 0.2f), alpha = f * g)
            for (j in 0 until (if (k == 0) 5 else 3)) softGlow(c + Offset(w * 0.04f * (j - 2), -h * 0.02f), w * 0.02f, JointC62.Gold, f * g)
            tagAt(a.tag("dutyorextra", 0, k), c + Offset(0f, -h * 0.2f), f * g, back = Back62, ink = ArtInk.Gold)
        }
    }
    if (fs > 0f) {
        val f = easeOut(seg(fs, 0f, 0.25f)) * (1f - seg(dt, 0f, 0.4f))
        for (k in 0 until 5) {
            val g = seg(fs, 0.06f + 0.18f * k, 0.2f + 0.18f * k)
            val x = w * (0.88f - 0.19f * k)
            val foot = Offset(x, ground - h * 0.06f)
            when (k) {
                0 -> { nameRoundel(foot + Offset(-w * 0.04f, -h * 0.05f), w * 0.022f, null, Roundel.PlainFill, Roundel.PlainRim, f * g); nameRoundel(foot + Offset(w * 0.04f, -h * 0.05f), w * 0.022f, null, Roundel.PlainFill, Roundel.PlainRim, f * g); drawLine(JointC62.Gold, foot + Offset(-w * 0.02f, -h * 0.05f), foot + Offset(w * 0.02f, -h * 0.05f), 2f * dp, alpha = f * g) }
                1 -> camel26(foot, h * 0.16f, JointC62.Camel, f * g, t)
                2 -> voice12(foot + Offset(0f, -h * 0.06f), w * 0.03f, t, JointC62.Voice, f * g)
                3 -> { mosque12(foot + Offset(-w * 0.03f, 0f), w * 0.06f, JointC62.Mosque, JointC62.Dome, f * g); for (j in 0 until 3) drawCircle(JointC62.Step, w * 0.005f, foot + Offset(w * (0.02f + 0.02f * j), h * 0.02f), alpha = f * g) }
                else -> { val lift = seg(fs, 0.85f, 0.98f); drawCircle(JointC62.Stone, w * 0.02f, foot + Offset(0f, -h * 0.02f - h * 0.08f * lift), alpha = f * g) }
            }
            softGlow(foot + Offset(0f, -h * 0.06f), w * 0.06f, JointC62.Gold, 0.4f * f * g)
            tagAt(a.tag("fivesadaqa", 0, k), Offset(w * 0.5f, h * 0.28f), f * g * (if (k < 4) 1f - seg(fs, 0.06f + 0.18f * (k + 1), 0.12f + 0.18f * (k + 1)) else 1f), back = Back62, ink = ArtInk.Gold)
        }
    }
    if (dt > 0f) {
        val f = easeOut(seg(dt, 0f, 0.25f)) * (1f - seg(cr, 0f, 0.4f))
        sun12(Offset(w * 0.82f, h * 0.35f), w * 0.04f, f)
        val foot = Offset(w * 0.45f, ground)
        val tall = h * 0.4f
        val cyc = fract(seg(dt, 0.1f, 0.9f) * 2f)
        val deep = if (cyc < 0.3f) 0f else if (cyc < 0.5f) 0.5f else if (cyc < 0.85f) 1f else 0f
        if (deep == 0f) pray12(foot, tall, JointC62.Person, f) else bow12(foot, tall, JointC62.Person, f, deep)
        softGlow(foot + Offset(0f, -tall * 0.4f), w * 0.15f, JointC62.Gold, 0.4f * f * seg(dt, 0.3f, 0.6f))
        tagAt(a.tag("duhatwo"), Offset(w * 0.45f, h * 0.2f), f * seg(dt, 0.2f, 0.4f), back = Back62, ink = ArtInk.Gold)
    }
    if (cr > 0f) {
        val f = easeOut(seg(cr, 0f, 0.25f)) * (1f - seg(es, 0f, 0.4f))
        drawRect(JointC62.Step, Offset(0f, ground - h * 0.02f), Size(w, h * 0.06f), alpha = f)
        for (k in 0 until 3) {
            val x = w * (0.7f - 0.2f * k)
            val lift = easeInOut(seg(cr, 0.15f + 0.25f * k, 0.35f + 0.25f * k))
            val c = Offset(mix(x, w * 0.95f, lift), ground - h * 0.02f - h * 0.15f * sin(PI.toFloat() * lift))
            when (k) {
                0 -> for (j in 0 until 3) drawLine(JointC62.Thorn, c, c + Offset(cos(j * 2.1f) * w * 0.025f, sin(j * 2.1f) * w * 0.025f - w * 0.01f), 2f * dp, StrokeCap.Round, alpha = f * (1f - lift))
                1 -> drawOval(JointC62.Thorn, Offset(c.x - w * 0.03f, c.y - h * 0.01f), Size(w * 0.06f, h * 0.02f), alpha = f * 0.7f * (1f - lift))
                else -> drawCircle(JointC62.Stone, w * 0.025f, c + Offset(0f, -w * 0.015f), alpha = f * (1f - lift))
            }
        }
        hands12(Offset(w * 0.5f, ground - h * 0.12f), h * 0.07f, JointC62.Person, f)
    }
    if (es > 0f) {
        val f = easeOut(seg(es, 0f, 0.25f))
        val cyc = fract(seg(es, 0.05f, 0.95f) * 3f)
        val sunY = mix(ground, h * 0.2f, sin(PI.toFloat() * cyc))
        sun12(Offset(w * (0.15f + 0.7f * cyc), sunY), w * 0.04f, f * sin(PI.toFloat() * cyc).coerceAtLeast(0.2f))
        val foot = Offset(w * 0.5f, ground)
        figure(foot, h * 0.4f, JointC62.Person, f * 0.9f)
        jointDots62(foot, h * 0.4f, f, seg(cyc, 0.1f, 0.6f))
    }
}

// ═════════════ 2. ask your heart (nw-27) ═════════════

private object HeartC62 {
    val Night = Color(0xFF141A2C)
    val NightLow = Color(0xFF2E3454)
    val Ground = Color(0xFF1E2232)
    val Gold = Color(0xFFF2C860)
    val Knot = Color(0xFF6A3A4A)
    val Person = Color(0xFFDCD4C4)
    val Shake = Color(0xFFD07A5A)
    val Page = Color(0xFFF2E8D2)
    val Book = Color(0xFF2E5A4A)
}

/**
 * Beats: birrring (a wide ring, «الْبِرُّ», and inside it at its heart, «بِحُسْنِ الْخُلُقِ»), hiddenknot (a figure turns a dark
 * knot away from others' eyes: «مَا حَاكَ فِي نَفْسِكَ»), consultheart (two hearts: one rests steady, one flutters: «اسْتَفْتِ
 * قَلْبَكَ»), answeredfirst (a roundel with his name, «وَابِصَةُ», comes to the light ﷺ; a question mark over him fades before
 * he speaks), revealedlit (an open book lights a heart).
 */
internal fun DrawScope.askyourheartScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "birrring" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "hiddenknot" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "consultheart" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "answeredfirst" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "revealedlit" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { heartNight(a) }
}

private fun DrawScope.heartNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val br = a.b("birrring")
    val hk = a.b("hiddenknot")
    val ch = a.b("consultheart")
    val af = a.b("answeredfirst")
    val rl = a.b("revealedlit")
    val ground = h * 0.86f
    nightSky(t, HeartC62.Night, HeartC62.NightLow, ground, starAlpha = 0.85f)
    drawRect(HeartC62.Ground, Offset(0f, ground), Size(w, h - ground))
    if (br > 0f) {
        val f = easeOut(seg(br, 0f, 0.25f)) * (1f - seg(hk, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.52f)
        val g1 = seg(br, 0.05f, 0.35f)
        drawCircle(HeartC62.Gold, w * 0.38f * easeOut(g1), c, alpha = f * 0.12f)
        drawCircle(HeartC62.Gold, w * 0.38f * easeOut(g1), c, alpha = f, style = Stroke(3f * dp))
        tagAt(a.tag("birrring", 0, 0), c + Offset(0f, -w * 0.3f), f * g1, back = Back62, ink = ArtInk.Gold)
        val g2 = seg(br, 0.45f, 0.75f)
        softGlow(c, w * 0.15f, HeartC62.Gold, 0.5f * f * g2)
        tagAt(a.tag("birrring", 0, 1), c, f * g2, back = Back62, ink = ArtInk.Gold)
    }
    if (hk > 0f) {
        val f = easeOut(seg(hk, 0f, 0.25f)) * (1f - seg(ch, 0f, 0.4f))
        val me = Offset(w * 0.3f, ground)
        val turn = seg(hk, 0.3f, 0.5f)
        figure(me, h * 0.34f, HeartC62.Person, f, lean = -0.1f * turn)
        val knot = me + Offset(-w * 0.05f * turn, -h * 0.22f)
        drawCircle(HeartC62.Knot, w * 0.03f, knot, alpha = f)
        for (j in 0 until 3) drawArc(HeartC62.Knot, 60f * j, 200f, false, Offset(knot.x - w * 0.04f, knot.y - w * 0.04f), Size(w * 0.08f, w * 0.08f), alpha = f * 0.7f, style = Stroke(2f * dp))
        for (k in 0 until 2) figure(Offset(w * (0.68f + 0.14f * k), ground), h * 0.3f, HeartC62.Person, f * 0.5f)
        tagAt(a.tag("hiddenknot"), Offset(w * 0.5f, h * 0.2f), f * seg(hk, 0.2f, 0.4f), back = Back62, ink = HeartC62.Shake)
    }
    if (ch > 0f) {
        val f = easeOut(seg(ch, 0f, 0.25f)) * (1f - seg(af, 0f, 0.4f))
        val l = Offset(w * 0.7f, h * 0.55f)
        val r = Offset(w * 0.3f, h * 0.55f + sin(t * 18f) * h * 0.01f)
        heart12(l, w * 0.08f, HeartC62.Gold, f * seg(ch, 0.1f, 0.3f), fill = true)
        softGlow(l, w * 0.15f, HeartC62.Gold, 0.4f * f * seg(ch, 0.2f, 0.4f))
        heart12(r + Offset(sin(t * 23f) * w * 0.008f, 0f), w * 0.08f, HeartC62.Shake, f * seg(ch, 0.35f, 0.55f), fill = true)
        tagAt(a.tag("consultheart"), Offset(w * 0.5f, h * 0.22f), f * seg(ch, 0.05f, 0.25f), back = Back62, ink = ArtInk.Gold)
    }
    if (af > 0f) {
        val f = easeOut(seg(af, 0f, 0.25f)) * (1f - seg(rl, 0f, 0.4f))
        val light = Offset(w * 0.72f, h * 0.38f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        val come = easeInOut(seg(af, 0.05f, 0.4f))
        val c = Offset(mix(w * 0.05f, w * 0.35f, come), h * 0.55f)
        nameRoundel(c, w * 0.09f, a.big("answeredfirst"), Roundel.Fills[3], Roundel.Rim, f)
        qMark14(c + Offset(0f, -h * 0.15f), w * 0.04f, HeartC62.Gold, f * seg(af, 0.3f, 0.4f) * (1f - seg(af, 0.55f, 0.75f)))
        voice12(light + Offset(-w * 0.08f, h * 0.02f), w * 0.04f, t, HeartC62.Gold, f * seg(af, 0.5f, 0.6f), -1f)
    }
    if (rl > 0f) {
        val f = easeOut(seg(rl, 0f, 0.25f))
        val book = Offset(w * 0.5f, h * 0.32f)
        openBook21(book, w * 0.36f, HeartC62.Page, HeartC62.Book, f, seg(rl, 0.1f, 0.4f))
        lightShafts(book + Offset(0f, h * 0.05f), PI.toFloat() / 2f, 0.4f, h * 0.3f, HeartC62.Gold, 0.5f * f * seg(rl, 0.25f, 0.5f), t, rays = 5)
        val heart = Offset(w * 0.5f, h * 0.68f)
        heart12(heart, w * 0.07f, lerpColor(HeartC62.Knot, HeartC62.Gold, seg(rl, 0.4f, 0.8f)), f, fill = true)
        softGlow(heart, w * 0.14f, HeartC62.Gold, 0.5f * f * seg(rl, 0.5f, 0.85f))
    }
}

// ═════════════ 3. hold fast to my Sunnah (nw-28) ═════════════

private object SunnahC62 {
    val Night = Color(0xFF101828)
    val NightLow = Color(0xFF283450)
    val Ground = Color(0xFF1A2232)
    val Gold = Color(0xFFF2C860)
    val Tear = Color(0xFF8AC4EA)
    val Ring = Color(0xFFD8C8A8)
    val Path = Color(0xFF4A5070)
    val Rope = Color(0xFFC8A070)
    val Hand = Color(0xFFDCD4C4)
    val Bad = Color(0xFFD06A4A)
}

/**
 * Beats: movingsermon (the light ﷺ speaks; plain roundels tremble and tears fall), linkedrings (a chain of rings, each holding
 * the next: «بِجَمَاعَةٍ», «بِإِمَارَةٍ», «بِسَمْعٍ وَطَاعَةٍ»), onelitway (many roads split off in the dark; one stays lit: «بِسُنَّتِهِ»,
 * «وَسُنَّةِ الْخُلَفَاءِ الرَّاشِدِينَ الْمَهْدِيِّينَ»), tightgrip (a rope slips through hands; then it is gripped hard and holds),
 * notbeforeus (one lit line runs back through the first generations; a new branch that is not on it is struck: «وَلَوْ كَانَ
 * خَيْرًا لَسَبَقُونَا إِلَيْهِ»).
 */
internal fun DrawScope.holdfastScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "movingsermon" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "linkedrings" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "onelitway" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "tightgrip" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "notbeforeus" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { sunnahNight(a) }
}

private fun DrawScope.sunnahNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ms = a.b("movingsermon")
    val lr = a.b("linkedrings")
    val ow = a.b("onelitway")
    val tg = a.b("tightgrip")
    val nb = a.b("notbeforeus")
    val ground = h * 0.86f
    nightSky(t, SunnahC62.Night, SunnahC62.NightLow, ground, starAlpha = 0.85f)
    drawRect(SunnahC62.Ground, Offset(0f, ground), Size(w, h - ground))
    if (ms > 0f) {
        val f = easeOut(seg(ms, 0f, 0.25f)) * (1f - seg(lr, 0f, 0.4f))
        val light = Offset(w * 0.5f, h * 0.25f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        voice12(light + Offset(0f, h * 0.08f), w * 0.05f, t, SunnahC62.Gold, f * seg(ms, 0.1f, 0.3f), 1f)
        for (k in 0 until 7) {
            val c = Offset(w * (0.12f + 0.126f * k), h * (0.62f + 0.06f * (k % 2))) + Offset(sin(t * 14f + k) * w * 0.002f * seg(ms, 0.3f, 0.5f), 0f)
            nameRoundel(c, w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f)
            val u = fract(t * 0.6f + k * 0.17f)
            drawCircle(SunnahC62.Tear, w * 0.005f, c + Offset(w * 0.015f, h * 0.02f + h * 0.06f * u), alpha = f * seg(ms, 0.45f, 0.65f) * (1f - u))
        }
    }
    if (lr > 0f) {
        val f = easeOut(seg(lr, 0f, 0.25f)) * (1f - seg(ow, 0f, 0.4f))
        for (k in 0 until 4) {
            val g = seg(lr, 0.05f + 0.2f * k, 0.22f + 0.2f * k)
            val c = Offset(w * (0.8f - 0.2f * k), h * 0.5f)
            drawOval(if (k == 0) SunnahC62.Gold else SunnahC62.Ring, Offset(c.x - w * 0.11f, c.y - h * 0.06f), Size(w * 0.22f, h * 0.12f), alpha = f * g, style = Stroke(5f * dp))
            if (k > 0) tagAt(a.tag("linkedrings", 0, k - 1), c + Offset(0f, h * 0.12f), f * g, back = Back62, ink = ArtInk.Gold)
        }
        softGlow(Offset(w * 0.8f, h * 0.5f), w * 0.1f, SunnahC62.Gold, 0.5f * f * seg(lr, 0.75f, 0.95f))
    }
    if (ow > 0f) {
        val f = easeOut(seg(ow, 0f, 0.25f)) * (1f - seg(tg, 0f, 0.4f))
        val root = Offset(w * 0.5f, ground)
        for (k in 0 until 7) {
            val ang = PI.toFloat() * (1.1f + 0.8f * k / 6f)
            val lit = k == 3
            val g = seg(ow, 0.05f + 0.05f * k, 0.3f + 0.05f * k)
            val end = root + Offset(cos(ang) * w * 0.45f, sin(ang) * h * 0.65f) * g
            drawLine(if (lit) SunnahC62.Gold else SunnahC62.Path, root, end, (if (lit) 5f else 2.5f) * dp, StrokeCap.Round, alpha = f * (if (lit) 1f else 0.6f * (1f - 0.5f * seg(ow, 0.5f, 0.8f))))
        }
        softGlow(Offset(w * 0.5f, h * 0.24f), w * 0.07f, SunnahC62.Gold, f * seg(ow, 0.4f, 0.6f))
        for (k in 0 until 4) nameRoundel(Offset(w * 0.5f + w * 0.035f * (if (k % 2 == 0) -1f else 1f), mix(ground - h * 0.1f, h * 0.32f, (k + 1) / 5f)), w * 0.022f, null, Roundel.PlainFill, Roundel.PlainRim, f * seg(ow, 0.55f + 0.06f * k, 0.65f + 0.06f * k))
        tagAt(a.tag("onelitway", 0, 0), Offset(w * 0.5f, h * 0.12f), f * seg(ow, 0.35f, 0.5f), back = Back62, ink = ArtInk.Gold)
        tagAt(a.tag("onelitway", 0, 1), Offset(w * 0.5f, h * 0.93f), f * seg(ow, 0.55f, 0.7f), back = Back62, ink = ArtInk.Gold)
    }
    if (tg > 0f) {
        val f = easeOut(seg(tg, 0f, 0.25f)) * (1f - seg(nb, 0f, 0.4f))
        val grip = seg(tg, 0.45f, 0.6f)
        val slip = (1f - grip) * fract(t * 0.6f) * h * 0.1f
        drawLine(SunnahC62.Rope, Offset(w * 0.5f, h * 0.1f + slip), Offset(w * 0.5f, h * 0.95f + slip), 6f * dp, StrokeCap.Round, alpha = f)
        for (k in 0 until 2) {
            val c = Offset(w * (0.44f + 0.12f * k), h * (0.45f + 0.12f * k))
            drawRoundRect(SunnahC62.Hand, Offset(c.x - w * 0.05f, c.y - h * 0.03f), Size(w * 0.1f, h * 0.06f), CornerRadius(10f * dp), alpha = f * (0.6f + 0.4f * grip))
        }
        softGlow(Offset(w * 0.5f, h * 0.51f), w * 0.12f, SunnahC62.Gold, 0.5f * f * grip)
    }
    if (nb > 0f) {
        val f = easeOut(seg(nb, 0f, 0.25f))
        val y = h * 0.45f
        drawLine(SunnahC62.Gold, Offset(w * 0.92f, y), Offset(w * 0.08f, y), 4f * dp, StrokeCap.Round, alpha = f * seg(nb, 0.05f, 0.25f))
        for (k in 0 until 4) nameRoundel(Offset(w * (0.85f - 0.14f * k), y), w * 0.028f, null, Roundel.PlainFill, Roundel.PlainRim, f * seg(nb, 0.1f + 0.06f * k, 0.2f + 0.06f * k))
        val br = seg(nb, 0.4f, 0.6f)
        val from = Offset(w * 0.3f, y)
        val to = Offset(w * 0.12f, y + h * 0.25f)
        drawLine(SunnahC62.Bad, from, from + (to - from) * br, 3f * dp, StrokeCap.Round, alpha = f)
        crossMark(from + (to - from) * 0.6f, w * 0.04f, SunnahC62.Bad, 4f * dp, f * seg(nb, 0.6f, 0.75f))
        tagAt(a.tag("notbeforeus"), Offset(w * 0.5f, h * 0.2f), f * seg(nb, 0.65f, 0.85f), back = Back62, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. the head of the matter (nw-29) ═════════════

private object TentC62 {
    val Sky = Color(0xFF1C1A30)
    val SkyLow = Color(0xFF4A3A58)
    val Ground = Color(0xFF2A2430)
    val Gold = Color(0xFFF2C860)
    val Cloth = Color(0xFFD8C8A8)
    val Pole = Color(0xFF8A6A44)
    val Shield = Color(0xFFBFE6FF)
    val Water = Color(0xFF8AC4EA)
    val Fire = Color(0xFFD0603A)
    val Seal = Color(0xFFB8A27E)
    val Mouth = Color(0xFFE2DACA)
    val Garden = Color(0xFF3E7A54)
}

/**
 * Beats: greatyeteasy (a roundel with his name, «مُعَاذٌ», asks; a garden gate glows near and a fire far off, and the answer
 * comes: «وَإِنَّهُ لَيَسِيرٌ عَلَى مَنْ يَسَّرَهُ اللَّهُ عَلَيْهِ»), tentparts (a tent: its head, its pole and its peak each light
 * with their words: «رَأْسُ الْأَمْرِ الْإِسْلَامُ», «وَعَمُودُهُ الصَّلَاةُ», «وَذُرْوَةُ سَنَامِهِ الْجِهَادُ»), shieldandwater (a
 * shield stands before a fire, and water poured on another puts it out: «الصَّوْمُ جُنَّةٌ», «وَالصَّدَقَةُ تُطْفِئُ الْخَطِيئَةَ»),
 * polefalls (take the pole out and the tent falls), sealtongue (a mouth is held shut by a soft seal: «كُفَّ عَلَيْكَ هَذَا»).
 */
internal fun DrawScope.headofmatterScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "greatyeteasy" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "tentparts" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "shieldandwater" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "polefalls" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "sealtongue" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { tentDusk(a) }
}

private fun DrawScope.tent62(foot: Offset, s: Float, alpha: Float, fall: Float, glowPole: Float, glowPeak: Float, glowBase: Float, t: Float) {
    if (alpha <= 0.01f) return
    val peakY = foot.y - s * (1f - 0.75f * fall)
    val peak = Offset(foot.x, peakY)
    drawPath(Path().apply { moveTo(foot.x - s * 0.9f, foot.y); lineTo(peak.x, peak.y); lineTo(foot.x + s * 0.9f, foot.y); close() }, TentC62.Cloth, alpha = alpha)
    drawLine(TentC62.Pole, foot, peak, s * 0.04f, StrokeCap.Round, alpha = alpha * (1f - fall))
    softGlow(Offset(foot.x, (foot.y + peakY) / 2f), s * 0.3f, TentC62.Gold, 0.6f * alpha * glowPole)
    softGlow(peak, s * 0.2f, TentC62.Gold, 0.8f * alpha * glowPeak)
    drawLine(TentC62.Gold, foot + Offset(-s * 0.9f, 0f), foot + Offset(s * 0.9f, 0f), s * 0.03f, StrokeCap.Round, alpha = alpha * glowBase)
}

private fun DrawScope.tentDusk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ge = a.b("greatyeteasy")
    val tp = a.b("tentparts")
    val sw = a.b("shieldandwater")
    val pf = a.b("polefalls")
    val st = a.b("sealtongue")
    val ground = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(TentC62.Sky, TentC62.SkyLow), 0f, ground), Offset.Zero, Size(w, ground))
    drawRect(TentC62.Ground, Offset(0f, ground), Size(w, h - ground))
    if (ge > 0f) {
        val f = easeOut(seg(ge, 0f, 0.25f)) * (1f - seg(tp, 0f, 0.4f))
        nameRoundel(Offset(w * 0.5f, h * 0.62f), w * 0.09f, a.big("greatyeteasy", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(ge, 0.05f, 0.2f))
        val gate = Offset(w * 0.8f, ground)
        drawRect(TentC62.Garden, Offset(gate.x - w * 0.07f, gate.y - h * 0.2f), Size(w * 0.14f, h * 0.2f), alpha = f * seg(ge, 0.2f, 0.4f))
        softGlow(gate + Offset(0f, -h * 0.1f), w * 0.1f, TentC62.Gold, 0.6f * f * seg(ge, 0.25f, 0.45f))
        for (k in 0 until 4) drawOval(TentC62.Fire, Offset(w * (0.06f + 0.03f * k), ground - h * 0.05f * (0.6f + 0.4f * sin(t * 8f + k))), Size(w * 0.025f, h * 0.05f), alpha = f * 0.6f * seg(ge, 0.25f, 0.45f))
        tagAt(a.tag("greatyeteasy", 0, 1), Offset(w * 0.5f, h * 0.25f), f * seg(ge, 0.55f, 0.75f), back = Back62, ink = ArtInk.Gold)
    }
    if (tp > 0f || pf > 0f) {
        val f = easeOut(seg(tp, 0f, 0.25f)) * (1f - seg(sw, 0f, 0.4f)) + easeOut(seg(pf, 0f, 0.25f)) * (1f - seg(st, 0f, 0.4f))
        val fall = easeInOut(seg(pf, 0.3f, 0.7f))
        val gBase = if (pf > 0f) 0f else seg(tp, 0.1f, 0.3f)
        val gPole = if (pf > 0f) 0f else seg(tp, 0.35f, 0.55f)
        val gPeak = if (pf > 0f) 0f else seg(tp, 0.65f, 0.85f)
        tent62(Offset(w * 0.5f, ground), h * 0.5f, f.coerceIn(0f, 1f), fall, gPole, gPeak, gBase, t)
        if (pf <= 0f) for (k in 0 until 3) {
            val g = listOf(gBase, gPole, gPeak)[k]
            tagAt(a.tag("tentparts", 0, k), Offset(w * (0.82f - 0.32f * (k % 2)), h * (0.78f - 0.24f * k)), f * g, back = Back62, ink = ArtInk.Gold)
        }
        if (pf > 0f) {
            val out = easeInOut(seg(pf, 0.1f, 0.35f))
            drawLine(TentC62.Pole, Offset(w * (0.5f + 0.35f * out), ground), Offset(w * (0.5f + 0.35f * out), ground - h * 0.5f), h * 0.02f, StrokeCap.Round, alpha = f.coerceIn(0f, 1f) * out)
        }
    }
    if (sw > 0f) {
        val f = easeOut(seg(sw, 0f, 0.25f)) * (1f - seg(pf, 0f, 0.4f))
        val sh = Offset(w * 0.72f, h * 0.55f)
        drawArc(TentC62.Shield, 120f, 120f, false, Offset(sh.x - w * 0.12f, sh.y - h * 0.15f), Size(w * 0.24f, h * 0.3f), alpha = f * seg(sw, 0.1f, 0.3f), style = Stroke(6f * dp, cap = StrokeCap.Round))
        for (k in 0 until 3) drawOval(TentC62.Fire, Offset(w * (0.82f + 0.04f * k), ground - h * 0.08f * (0.6f + 0.4f * sin(t * 8f + k))), Size(w * 0.03f, h * 0.08f), alpha = f * 0.8f)
        val wet = seg(sw, 0.55f, 0.85f)
        for (k in 0 until 3) drawOval(TentC62.Fire, Offset(w * (0.2f + 0.05f * k), ground - h * 0.08f * (1f - wet) * (0.6f + 0.4f * sin(t * 8f + k))), Size(w * 0.03f, h * 0.08f * (1f - wet) + 1f), alpha = f * 0.8f)
        rain12(w * 0.15f, w * 0.4f, h * 0.3f, ground, t, TentC62.Water, f * seg(sw, 0.5f, 0.6f), 16)
        tagAt(a.tag("shieldandwater", 0, 0), Offset(w * 0.72f, h * 0.22f), f * seg(sw, 0.1f, 0.3f), back = Back62, ink = ArtInk.Gold)
        tagAt(a.tag("shieldandwater", 0, 1), Offset(w * 0.28f, h * 0.22f), f * seg(sw, 0.5f, 0.7f), back = Back62, ink = ArtInk.Gold)
    }
    if (st > 0f) {
        val f = easeOut(seg(st, 0f, 0.25f))
        val mouth = Offset(w * 0.5f, h * 0.5f)
        val shut = seg(st, 0.3f, 0.55f)
        drawArc(TentC62.Mouth, 20f, 140f, false, Offset(mouth.x - w * 0.12f, mouth.y - h * 0.08f * (1f - shut)), Size(w * 0.24f, h * 0.16f * (1f - shut) + 2f * dp), alpha = f, style = Stroke(5f * dp, cap = StrokeCap.Round))
        drawRoundRect(TentC62.Seal, Offset(mouth.x - w * 0.14f, mouth.y - h * 0.02f), Size(w * 0.28f, h * 0.04f), CornerRadius(8f * dp), alpha = f * shut)
        softGlow(mouth, w * 0.18f, TentC62.Gold, 0.3f * f * shut)
        tagAt(a.tag("sealtongue"), Offset(w * 0.5f, h * 0.25f), f * seg(st, 0.15f, 0.35f), back = Back62, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. four kinds of ruling (nw-30, notebook) ═════════════

/**
 * Beats: keepduties (items in a box are kept safe and ticked: «فَلَا تُضَيِّعُوهَا»), boundsfences (a dashed boundary, «فَلَا
 * تَعْتَدُوهَا», and a closed square inside it, «فَلَا تَنْتَهِكُوهَا»), silentmercy (an open, blank space where items float freely:
 * «فَهُوَ عَفْوٌ»), fourquads (the page in four parts: «وَاجِبَاتٍ تُفْعَلُ», «وَحُدُودًا يُوقَفُ عِنْدَهَا», «وَمُحَرَّمَاتٍ
 * تُجْتَنَبُ», «وَمَسْكُوتًا عَنْهُ يُعْفَى عَنْهُ»), knowingsilence (the blank part is circled and a word is written in it: «عَنْ
 * عِلْمٍ وَحِكْمَةٍ»).
 */
internal fun DrawScope.fourrulingsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "keepduties" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "boundsfences" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "silentmercy" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "fourquads" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "knowingsilence" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0f) { rulingsPage(a) }
}

private fun DrawScope.rulingsPage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pp = Paper(a.dark)
    paperSheet(pp)
    val kd = a.b("keepduties")
    val bf = a.b("boundsfences")
    val sm = a.b("silentmercy")
    val fq = a.b("fourquads")
    val ks = a.b("knowingsilence")
    val pen = 2.2f * dp
    val dash = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp))
    if (kd > 0f) {
        val f = easeOut(seg(kd, 0f, 0.25f)) * (1f - seg(bf, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.55f)
        penLine(listOf(c + Offset(-w * 0.25f, -h * 0.15f), c + Offset(w * 0.25f, -h * 0.15f), c + Offset(w * 0.25f, h * 0.15f), c + Offset(-w * 0.25f, h * 0.15f), c + Offset(-w * 0.25f, -h * 0.15f)), seg(kd, 0.05f, 0.25f), pp.ink, pen, f)
        for (k in 0 until 4) checkMark(c + Offset(w * (0.18f - 0.12f * k), 0f), w * 0.025f, pp.good, pen, f * seg(kd, 0.3f + 0.1f * k, 0.4f + 0.1f * k))
        note(a.tag("keepduties"), Offset(w * 0.5f, h * 0.25f), f * seg(kd, 0.5f, 0.7f), pp, pp.good)
    }
    if (bf > 0f) {
        val f = easeOut(seg(bf, 0f, 0.25f)) * (1f - seg(sm, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.55f)
        penLine(listOf(c + Offset(-w * 0.35f, -h * 0.25f), c + Offset(w * 0.35f, -h * 0.25f), c + Offset(w * 0.35f, h * 0.25f), c + Offset(-w * 0.35f, h * 0.25f), c + Offset(-w * 0.35f, -h * 0.25f)), seg(bf, 0.05f, 0.3f), pp.amber, pen, f, dash)
        val g = seg(bf, 0.45f, 0.7f)
        drawRect(pp.bad, Offset(c.x - w * 0.12f, c.y - h * 0.1f), Size(w * 0.24f, h * 0.2f), alpha = f * g * 0.25f)
        penLine(listOf(c + Offset(-w * 0.12f, -h * 0.1f), c + Offset(w * 0.12f, -h * 0.1f), c + Offset(w * 0.12f, h * 0.1f), c + Offset(-w * 0.12f, h * 0.1f), c + Offset(-w * 0.12f, -h * 0.1f)), g, pp.bad, pen, f)
        note(a.tag("boundsfences", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(bf, 0.2f, 0.4f), pp, pp.amber)
        note(a.tag("boundsfences", 0, 1), c, f * g, pp, pp.bad)
    }
    if (sm > 0f) {
        val f = easeOut(seg(sm, 0f, 0.25f)) * (1f - seg(fq, 0f, 0.4f))
        for (k in 0 until 8) {
            val c = Offset(w * (0.2f + 0.6f * fract(k * 0.37f)) + sin(t * 0.8f + k) * w * 0.03f, h * (0.35f + 0.4f * fract(k * 0.61f)) + cos(t * 0.7f + k) * h * 0.02f)
            drawCircle(pp.faint, w * 0.012f, c, alpha = f * seg(sm, 0.05f + 0.04f * k, 0.15f + 0.04f * k), style = Stroke(pen * 0.8f))
        }
        softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.3f, pp.good, 0.15f * f * seg(sm, 0.3f, 0.6f))
        note(a.tag("silentmercy"), Offset(w * 0.5f, h * 0.22f), f * seg(sm, 0.4f, 0.6f), pp, pp.good, boxed = true)
    }
    if (fq > 0f || ks > 0f) {
        val f = easeOut(seg(fq, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.55f)
        penLine(listOf(Offset(c.x, h * 0.22f), Offset(c.x, h * 0.88f)), seg(fq, 0.02f, 0.15f), pp.ink, pen, f)
        penLine(listOf(Offset(w * 0.08f, c.y), Offset(w * 0.92f, c.y)), seg(fq, 0.08f, 0.2f), pp.ink, pen, f)
        val spots = listOf(Offset(w * 0.72f, h * 0.38f), Offset(w * 0.28f, h * 0.38f), Offset(w * 0.72f, h * 0.72f), Offset(w * 0.28f, h * 0.72f))
        val cols = listOf(pp.good, pp.amber, pp.bad, pp.faint)
        for (k in 0 until 4) note(a.tag("fourquads", 0, k), spots[k], f * seg(fq, 0.15f + 0.18f * k, 0.3f + 0.18f * k), pp, cols[k])
        if (ks > 0f) {
            val g = seg(ks, 0.1f, 0.4f)
            drawOval(pp.good, Offset(spots[3].x - w * 0.2f, spots[3].y - h * 0.12f), Size(w * 0.4f, h * 0.24f), alpha = f * g, style = Stroke(pen * 1.3f))
            note(a.tag("knowingsilence"), spots[3] + Offset(0f, h * 0.08f), f * seg(ks, 0.35f, 0.55f), pp, pp.good)
        }
    }
}
