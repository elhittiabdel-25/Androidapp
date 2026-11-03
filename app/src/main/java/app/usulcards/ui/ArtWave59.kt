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
 * Wave 59 (2026-11-03): «الأربعون النووية», hadiths 11–15. Leaving the doubtful for the clear; passing by what does not
 * concern you; loving for a brother what you love for yourself; what is sacred in a Muslim; good words or silence, the
 * neighbour and the guest. Allah is never drawn; the Prophet ﷺ is only light; Jibril is a cool white light; companions and
 * Ahl al-Bayt are names in roundels; others are generic figures or plain roundels.
 */

private val Back59 = Color(0xE6181420)

// ═════════════ 1. leave what makes you doubt (nw-11) ═════════════

private object DoubtC59 {
    val Night = Color(0xFF121A2A)
    val NightLow = Color(0xFF2A3A4E)
    val Ground = Color(0xFF1C2430)
    val Gold = Color(0xFFF2C860)
    val Leaf = Color(0xFF6ABA7A)
    val Murky = Color(0xFF6A5A3A)
    val Clear = Color(0xFF9AD4F0)
    val Cup = Color(0xFFD8CCB4)
    val Mud = Color(0xFF5A4A36)
    val Road = Color(0xFF8A7A62)
    val Person = Color(0xFFDCD4C4)
    val Shake = Color(0xFFD07A5A)
}

/**
 * Beats: rayhanatan (the light ﷺ between two roundels with their names, «الْحَسَنُ» and «وَالْحُسَيْنَ», and sweet basil leaves
 * drifting about them), murkyclear (two cups: the cloudy one is set down, «مَا يَشُكُّ فِي حِلِّهِ», and the clear one is taken,
 * «مَا لَا يَشُكُّ فِيهِ»), widermargin (a figure walks a road that bends wide around a patch of mud), steadyjitter (two lines from
 * a heart: one calm, «الصِّدْقَ طُمَأْنِينَةٌ», one shaking, «وَالْكَذِبَ رِيبَةٌ»).
 */
internal fun DrawScope.doubtfreeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rayhanatan" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "murkyclear" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "widermargin" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
        "steadyjitter" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { doubtNight(a) }
}

private fun DrawScope.doubtNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rh = a.b("rayhanatan")
    val mc = a.b("murkyclear")
    val wm = a.b("widermargin")
    val sj = a.b("steadyjitter")
    val ground = h * 0.84f
    nightSky(t, DoubtC59.Night, DoubtC59.NightLow, ground, starAlpha = 0.85f)
    drawRect(DoubtC59.Ground, Offset(0f, ground), Size(w, h - ground))
    if (rh > 0f) {
        val f = easeOut(seg(rh, 0f, 0.25f)) * (1f - seg(mc, 0f, 0.4f))
        val light = Offset(w * 0.5f, h * 0.28f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.11f))
        nameRoundel(Offset(w * 0.3f, h * 0.58f), w * 0.1f, a.big("rayhanatan", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(rh, 0.1f, 0.3f))
        nameRoundel(Offset(w * 0.7f, h * 0.58f), w * 0.1f, a.big("rayhanatan", 0, 1), Roundel.Fills[2], Roundel.Rim, f * seg(rh, 0.25f, 0.45f))
        for (k in 0 until 10) {
            val u = fract(t * 0.12f + k * 0.1f)
            val c = Offset(w * (0.15f + 0.7f * fract(k * 0.37f)) + sin(t + k) * w * 0.02f, mix(h * 0.8f, h * 0.15f, u))
            drawOval(DoubtC59.Leaf, Offset(c.x - w * 0.012f, c.y - h * 0.006f), Size(w * 0.024f, h * 0.012f), alpha = f * seg(rh, 0.4f, 0.6f) * sin(PI.toFloat() * u))
        }
    }
    if (mc > 0f) {
        val f = easeOut(seg(mc, 0f, 0.25f)) * (1f - seg(wm, 0f, 0.4f))
        val l = Offset(w * 0.3f, ground)
        val r = Offset(w * 0.7f, ground)
        val away = easeInOut(seg(mc, 0.3f, 0.5f))
        cup12(l + Offset(-w * 0.12f * away, 0f), w * 0.12f, DoubtC59.Cup, f * (1f - 0.5f * away), DoubtC59.Murky)
        val take = easeInOut(seg(mc, 0.6f, 0.85f))
        cup12(r + Offset(0f, -h * 0.2f * take), w * 0.12f, DoubtC59.Cup, f, DoubtC59.Clear)
        softGlow(r + Offset(0f, -h * 0.05f - h * 0.2f * take), w * 0.08f, DoubtC59.Clear, 0.4f * f * take)
        tagAt(a.tag("murkyclear", 0, 0), Offset(w * 0.3f, h * 0.35f), f * seg(mc, 0.1f, 0.3f), back = Back59)
        tagAt(a.tag("murkyclear", 0, 1), Offset(w * 0.7f, h * 0.25f), f * seg(mc, 0.5f, 0.7f), back = Back59, ink = ArtInk.Gold)
    }
    if (wm > 0f) {
        val f = easeOut(seg(wm, 0f, 0.25f)) * (1f - seg(sj, 0f, 0.4f))
        val mud = Offset(w * 0.5f, h * 0.7f)
        drawOval(DoubtC59.Mud, Offset(mud.x - w * 0.14f, mud.y - h * 0.05f), Size(w * 0.28f, h * 0.1f), alpha = f)
        val p0 = Offset(w * 0.95f, h * 0.72f)
        val p1 = Offset(w * 0.7f, h * 0.5f)
        val p2 = Offset(w * 0.3f, h * 0.5f)
        val p3 = Offset(w * 0.05f, h * 0.72f)
        drawPath(cubicPart(p0, p1, p2, p3, 0f, 1f), DoubtC59.Road, alpha = f, style = Stroke(h * 0.03f, cap = StrokeCap.Round))
        val u = easeInOut(seg(wm, 0.1f, 0.9f))
        figure(cubic(p0, p1, p2, p3, u) + Offset(0f, h * 0.01f), h * 0.2f, DoubtC59.Person, f, walk = t * 5f)
    }
    if (sj > 0f) {
        val f = easeOut(seg(sj, 0f, 0.25f))
        val heart = Offset(w * 0.85f, h * 0.5f)
        heart12(heart, w * 0.05f, DoubtC59.Gold, f, fill = true)
        for (k in 0 until 2) {
            val g = seg(sj, 0.1f + 0.4f * k, 0.4f + 0.4f * k)
            val y = h * (0.38f + 0.24f * k)
            val path = Path().apply {
                moveTo(heart.x - w * 0.06f, heart.y)
                lineTo(w * 0.72f, y)
                for (j in 0..24) {
                    val x = w * 0.72f - w * 0.62f * (j / 24f) * g
                    lineTo(x, y + if (k == 1) sin(j * 2.3f + t * 14f) * h * 0.03f else 0f)
                }
            }
            drawPath(path, if (k == 0) DoubtC59.Gold else DoubtC59.Shake, alpha = f * seg(sj, 0.1f + 0.4f * k, 0.15f + 0.4f * k), style = Stroke(3f * dp, cap = StrokeCap.Round))
            tagAt(a.tag("steadyjitter", 0, k), Offset(w * 0.4f, y - h * 0.08f), f * g, back = Back59, ink = if (k == 0) ArtInk.Gold else DoubtC59.Shake)
        }
    }
}

// ═════════════ 2. what does not concern him (nw-12) ═════════════

private object ConcernC59 {
    val Sky = Color(0xFFC6DCEA)
    val SkyLow = Color(0xFFF4E8D2)
    val Ground = Color(0xFFCCB692)
    val Wall = Color(0xFFE2D2B4)
    val Trim = Color(0xFF8A6A44)
    val Person = Color(0xFF3A3040)
    val Leaf = Color(0xFF4E8A54)
    val Thin = Color(0xFFA8A070)
    val Gold = Color(0xFFD89A2A)
    val Chat = Color(0xFF8A7A9A)
    val Thorn = Color(0xFF8A4A3A)
    val Basket = Color(0xFFB88A58)
}

/**
 * Beats: betterless (two plants from the same soil: one full, one thin), passby (a figure walks past windows full of chatter
 * to his own lit door: «مَا لَا يَعْنِيهِ»), enjoinstill (he stops to point one the right way: «وَالْأَمْرُ بِالْمَعْرُوفِ وَالنَّهْيُ
 * عَنِ الْمُنْكَرِ»), twobaskets (two baskets are kept, «دِينِهِ» and «وَدُنْيَاهُ النَّافِعَةِ», and the rest falls away:
 * «فُضُولٌ»), chatterdoor (idle sound waves from a mouth turn into thorns along a path).
 */
internal fun DrawScope.notmyconcernScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "betterless" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "passby" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
        "enjoinstill" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "twobaskets" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "chatterdoor" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { concernDay(a) }
}

private fun DrawScope.concernDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bl = a.b("betterless")
    val pb = a.b("passby")
    val es = a.b("enjoinstill")
    val tb = a.b("twobaskets")
    val cd = a.b("chatterdoor")
    val ground = h * 0.84f
    daySky12(ConcernC59.Sky, ConcernC59.SkyLow, ground, ConcernC59.Ground)
    if (bl > 0f) {
        val f = easeOut(seg(bl, 0f, 0.25f)) * (1f - seg(pb, 0f, 0.4f))
        for (k in 0 until 2) {
            val foot = Offset(w * (0.32f + 0.36f * k), ground)
            val g = easeOut(seg(bl, 0.1f, 0.7f))
            val full = k == 0
            val tall = h * (if (full) 0.38f else 0.26f) * g
            drawLine(ConcernC59.Trim, foot, foot + Offset(0f, -tall), w * (if (full) 0.014f else 0.008f), StrokeCap.Round, alpha = f)
            val n = if (full) 7 else 3
            for (j in 0 until n) {
                val p = foot + Offset((if (j % 2 == 0) 1f else -1f) * w * 0.04f, -tall * (0.3f + 0.65f * j / n.toFloat()))
                drawOval(if (full) ConcernC59.Leaf else ConcernC59.Thin, Offset(p.x - w * 0.035f, p.y - h * 0.015f), Size(w * 0.07f, h * 0.03f), alpha = f * g)
            }
            if (full) softGlow(foot + Offset(0f, -tall * 0.7f), w * 0.12f, ConcernC59.Gold, 0.3f * f * g)
        }
    }
    if (pb > 0f || es > 0f) {
        val f = easeOut(seg(pb, 0f, 0.25f)) * (1f - seg(tb, 0f, 0.4f))
        for (k in 0 until 3) {
            val foot = Offset(w * (0.75f - 0.22f * k), ground)
            house12(foot, w * 0.16f, ConcernC59.Wall, ConcernC59.Trim, f, 0.2f)
            if (es <= 0f) voice12(foot + Offset(0f, -h * 0.2f), w * 0.04f, t + k, ConcernC59.Chat, f * 0.7f, if (k % 2 == 0) 1f else -1f)
        }
        door12(Offset(w * 0.1f, ground), w * 0.08f, h * 0.18f, 0.6f, ConcernC59.Trim, ConcernC59.Wall, f, Color(0xFFFFE2A0))
        val walk = easeInOut(seg(pb, 0.1f, 0.9f))
        val stop = es > 0f
        val x = if (stop) w * 0.5f else mix(w * 0.95f, w * 0.15f, walk)
        figure(Offset(x, ground + h * 0.06f), h * 0.26f, ConcernC59.Person, f, walk = if (stop) 0f else t * 5f, arms = if (stop) -0.9f * seg(es, 0.2f, 0.4f) else 0f)
        tagAt(a.tag("passby"), Offset(w * 0.5f, h * 0.2f), f * seg(pb, 0.4f, 0.6f) * (1f - seg(es, 0f, 0.2f)), back = Back59, ink = ArtInk.Gold)
        if (es > 0f) {
            val ff = easeOut(seg(es, 0f, 0.25f)) * (1f - seg(tb, 0f, 0.4f))
            val other = Offset(w * 0.3f, ground + h * 0.06f)
            val turn = seg(es, 0.45f, 0.7f)
            figure(other, h * 0.26f, ConcernC59.Person, ff, walk = t * 4f * (1f - turn), lean = 0.1f - 0.2f * turn)
            softGlow(Offset(w * 0.15f, h * 0.55f), w * 0.06f, ConcernC59.Gold, ff * turn)
            tagAt(a.tag("enjoinstill"), Offset(w * 0.5f, h * 0.2f), ff * seg(es, 0.2f, 0.4f), back = Back59, ink = ArtInk.Gold)
        }
    }
    if (tb > 0f) {
        val f = easeOut(seg(tb, 0f, 0.25f)) * (1f - seg(cd, 0f, 0.4f))
        for (k in 0 until 2) {
            val c = Offset(w * (0.68f - 0.36f * k), ground - h * 0.06f)
            val g = seg(tb, 0.1f + 0.2f * k, 0.3f + 0.2f * k)
            drawArc(ConcernC59.Basket, 0f, 180f, true, Offset(c.x - w * 0.1f, c.y - h * 0.08f), Size(w * 0.2f, h * 0.16f), alpha = f * g)
            softGlow(c + Offset(0f, -h * 0.03f), w * 0.07f, ConcernC59.Gold, 0.5f * f * g)
            tagAt(a.tag("twobaskets", 0, k), c + Offset(0f, -h * 0.18f), f * g, back = Back59, ink = ArtInk.Gold)
        }
        val fall = seg(tb, 0.6f, 0.95f)
        for (k in 0 until 6) {
            val c = Offset(w * (0.2f + 0.12f * k), mix(h * 0.25f, ground + h * 0.1f, easeInOut((fall - 0.05f * k).coerceIn(0f, 1f))))
            drawCircle(ConcernC59.Chat, w * 0.012f, c, alpha = f * fall * 0.7f)
        }
        tagAt(a.tag("twobaskets", 0, 2), Offset(w * 0.5f, h * 0.18f), f * seg(tb, 0.65f, 0.8f), back = Back59)
    }
    if (cd > 0f) {
        val f = easeOut(seg(cd, 0f, 0.25f))
        val mouth = Offset(w * 0.82f, h * 0.45f)
        voice12(mouth, w * 0.06f, t, ConcernC59.Chat, f, -1f)
        val turn = seg(cd, 0.3f, 0.8f)
        drawLine(ConcernC59.Ground, Offset(w * 0.05f, ground - h * 0.02f), Offset(w * 0.75f, ground - h * 0.02f), h * 0.03f, alpha = f)
        for (k in 0 until 8) {
            val x = w * (0.7f - 0.08f * k)
            val g = seg(turn, k / 10f, k / 10f + 0.2f)
            drawPath(Path().apply { moveTo(x - w * 0.015f, ground - h * 0.02f); lineTo(x, ground - h * 0.02f - h * 0.05f * g); lineTo(x + w * 0.015f, ground - h * 0.02f); close() }, ConcernC59.Thorn, alpha = f * g)
        }
    }
}

// ═════════════ 3. love for your brother (nw-13) ═════════════

private object BrotherC59 {
    val Night = Color(0xFF16182C)
    val NightLow = Color(0xFF34304E)
    val Ground = Color(0xFF221E2C)
    val Gold = Color(0xFFF2C860)
    val Vessel = Color(0xFFD8CCB4)
    val Fill = Color(0xFF7ACB9A)
    val Dark = Color(0xFF3A3040)
    val Kin = Color(0xFF8A8A9A)
    val Road = Color(0xFF5A5270)
    val Red = Color(0xFFD07A5A)
}

/**
 * Beats: anasyears (a roundel with his name, «أَنَسُ بْنُ مَالِكٍ», and ten small lights going round it, at home and on the
 * road), baseremains (a vessel whose base stays full while its upper part is empty), mirrorgood (two plain roundels: each good
 * light that comes to one comes to the other, and a dark one is pushed from both), faiththread (a gold thread, «أُخُوَّةُ
 * الدِّينِ», holds while a grey one, «أُخُوَّةِ النَّسَبِ», slackens), warmnet (many roundels joined by lines of light; the
 * frayed ones are knotted again).
 */
internal fun DrawScope.likeforbrotherScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "anasyears" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "baseremains" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "mirrorgood" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "faiththread" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "warmnet" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { brotherNight(a) }
}

private fun DrawScope.brotherNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ay = a.b("anasyears")
    val br = a.b("baseremains")
    val mg = a.b("mirrorgood")
    val ft = a.b("faiththread")
    val wn = a.b("warmnet")
    val ground = h * 0.86f
    nightSky(t, BrotherC59.Night, BrotherC59.NightLow, ground, starAlpha = 0.8f)
    drawRect(BrotherC59.Ground, Offset(0f, ground), Size(w, h - ground))
    if (ay > 0f) {
        val f = easeOut(seg(ay, 0f, 0.25f)) * (1f - seg(br, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.5f)
        nameRoundel(c, w * 0.12f, a.big("anasyears"), Roundel.Fills[1], Roundel.Rim, f * seg(ay, 0.05f, 0.25f))
        for (k in 0 until 10) {
            val ang = -PI.toFloat() / 2f + k * PI.toFloat() / 5f + t * 0.2f
            softGlow(c + Offset(cos(ang) * w * 0.2f, sin(ang) * w * 0.2f), w * 0.015f, BrotherC59.Gold, f * seg(ay, 0.25f + 0.05f * k, 0.32f + 0.05f * k))
        }
        drawLine(BrotherC59.Road, Offset(w * 0.1f, ground - h * 0.02f), Offset(w * 0.9f, ground - h * 0.02f), h * 0.02f, StrokeCap.Round, alpha = f * seg(ay, 0.6f, 0.8f))
    }
    if (br > 0f) {
        val f = easeOut(seg(br, 0f, 0.25f)) * (1f - seg(mg, 0f, 0.4f))
        val foot = Offset(w * 0.5f, ground - h * 0.04f)
        val vw = w * 0.3f
        val vh = h * 0.5f
        val fill = mix(1f, 0.3f, easeInOut(seg(br, 0.2f, 0.6f)))
        drawRect(BrotherC59.Fill, Offset(foot.x - vw / 2f + 4f * dp, foot.y - vh * fill), Size(vw - 8f * dp, vh * fill), alpha = f * 0.8f)
        drawRoundRect(BrotherC59.Vessel, Offset(foot.x - vw / 2f, foot.y - vh), Size(vw, vh), CornerRadius(10f * dp), alpha = f, style = Stroke(4f * dp))
        drawLine(BrotherC59.Gold, Offset(foot.x - vw / 2f, foot.y - vh * 0.3f), Offset(foot.x + vw / 2f, foot.y - vh * 0.3f), 2f * dp, alpha = f * seg(br, 0.6f, 0.8f))
        softGlow(Offset(foot.x, foot.y - vh * 0.15f), w * 0.1f, BrotherC59.Gold, 0.4f * f * seg(br, 0.65f, 0.85f))
    }
    if (mg > 0f) {
        val f = easeOut(seg(mg, 0f, 0.25f)) * (1f - seg(ft, 0f, 0.4f))
        val l = Offset(w * 0.28f, h * 0.55f)
        val r = Offset(w * 0.72f, h * 0.55f)
        nameRoundel(l, w * 0.06f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(r, w * 0.06f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        for (k in 0 until 3) {
            val g = seg(mg, 0.1f + 0.15f * k, 0.25f + 0.15f * k)
            val ang = -PI.toFloat() / 2f + (k - 1) * 0.7f
            val off = Offset(cos(ang), sin(ang)) * (w * 0.1f)
            softGlow(l + off, w * 0.018f, BrotherC59.Gold, f * g)
            softGlow(r + off, w * 0.018f, BrotherC59.Gold, f * g)
        }
        val push = easeInOut(seg(mg, 0.6f, 0.9f))
        val dark = Offset(w * 0.5f, mix(h * 0.55f, h * 0.95f, push))
        drawCircle(BrotherC59.Dark, w * 0.03f, dark, alpha = f * (1f - push) + f * 0.2f)
    }
    if (ft > 0f) {
        val f = easeOut(seg(ft, 0f, 0.25f)) * (1f - seg(wn, 0f, 0.4f))
        val l = Offset(w * 0.2f, h * 0.5f)
        val r = Offset(w * 0.8f, h * 0.5f)
        nameRoundel(l, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(r, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val slack = seg(ft, 0.5f, 0.8f)
        drawPath(Path().apply { moveTo(l.x + w * 0.05f, l.y - h * 0.03f); quadraticTo(w * 0.5f, l.y - h * 0.08f, r.x - w * 0.05f, r.y - h * 0.03f) }, BrotherC59.Gold, alpha = f * seg(ft, 0.1f, 0.25f), style = Stroke(4f * dp))
        drawPath(Path().apply { moveTo(l.x + w * 0.05f, l.y + h * 0.03f); quadraticTo(w * 0.5f, l.y + h * (0.06f + 0.2f * slack), r.x - w * 0.05f, r.y + h * 0.03f) }, BrotherC59.Kin, alpha = f * (1f - 0.5f * slack), style = Stroke(3f * dp))
        tagAt(a.tag("faiththread", 0, 0), Offset(w * 0.5f, h * 0.32f), f * seg(ft, 0.1f, 0.3f), back = Back59, ink = ArtInk.Gold)
        tagAt(a.tag("faiththread", 0, 1), Offset(w * 0.5f, h * 0.76f), f * seg(ft, 0.3f, 0.5f), back = Back59)
    }
    if (wn > 0f) {
        val f = easeOut(seg(wn, 0f, 0.25f))
        val pts = List(8) { k -> val ang = k * PI.toFloat() / 4f + 0.3f; Offset(w * 0.5f + cos(ang) * w * 0.33f, h * 0.5f + sin(ang) * h * 0.28f) }
        val mend = seg(wn, 0.4f, 0.85f)
        for (k in 0 until 8) for (j in k + 1 until 8) {
            if ((k + j) % 3 != 0) continue
            val frayed = (k * j) % 2 == 1
            val g = if (frayed) mend else 1f
            drawLine(if (frayed && mend < 1f) BrotherC59.Red else BrotherC59.Gold, pts[k], pts[k] + (pts[j] - pts[k]) * (if (frayed) 0.4f + 0.6f * g else 1f), 2f * dp, alpha = f * seg(wn, 0.05f, 0.3f) * 0.8f)
        }
        for (p in pts) nameRoundel(p, w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f)
    }
}

// ═════════════ 4. what is sacred (nw-14) ═════════════

private object SacredC59 {
    val Sky = Color(0xFFC8DAE8)
    val SkyLow = Color(0xFFF2E6CC)
    val Ground = Color(0xFFCDB890)
    val Mount = Color(0xFFB09472)
    val Drop = Color(0xFFB04A3A)
    val Scroll = Color(0xFFF2E8D2)
    val Gold = Color(0xFFD89A2A)
    val Dim = Color(0xFF9A9A9A)
    val Voice = Color(0xFF5A6A9A)
    val Hand = Color(0xFF3A3040)
}

/**
 * Beats: threesacred (a drop, coins and a name scroll, each under a dome of light: «دَمُهُ», «وَمَالُهُ», «وَعِرْضُهُ»),
 * tonguehearthand (three lights in turn, on the tongue, in the heart and in the hands: «نُطْقًا», «وَاعْتِقَادًا», «وَعَمَلًا»),
 * farewellday (a great gathering of plain roundels below a mountain under the sun: «كَحُرْمَةِ يَوْمِكُمْ هَذَا»),
 * partmissing (the same three lights, one of them failing: «جَنَانُهُ», «لِسَانُهُ», «أَرْكَانُهُ»).
 */
internal fun DrawScope.inviolableScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threesacred" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "tonguehearthand" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "farewellday" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "partmissing" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { sacredDay(a) }
}

private fun DrawScope.threeLights59(a: ArtCtx, kind: String, p: Float, f: Float, failing: Int) {
    val w = size.width
    val h = size.height
    val t = a.t
    val spots = listOf(Offset(w * 0.78f, h * 0.45f), Offset(w * 0.5f, h * 0.45f), Offset(w * 0.22f, h * 0.45f))
    for (k in 0 until 3) {
        val g = seg(p, 0.1f + 0.22f * k, 0.3f + 0.22f * k)
        val c = spots[k]
        val fail = if (k == failing) seg(p, 0.75f, 0.9f) else 0f
        val col = lerpColor(SacredC59.Gold, SacredC59.Dim, fail)
        when (k) {
            0 -> voice12(c, w * 0.05f, t, lerpColor(SacredC59.Voice, SacredC59.Dim, fail), f * g, -1f)
            1 -> heart12(c, w * 0.05f, col, f * g, fill = true)
            else -> hands12(c + Offset(0f, h * 0.05f), h * 0.1f, lerpColor(SacredC59.Hand, SacredC59.Dim, fail), f * g)
        }
        softGlow(c, w * 0.1f, col, 0.4f * f * g * (1f - fail))
        tagAt(a.tag(kind, 0, k), c + Offset(0f, h * 0.17f), f * g, back = Back59, ink = if (fail > 0.5f) Color.Unspecified else ArtInk.Gold)
    }
}

private fun DrawScope.sacredDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ts = a.b("threesacred")
    val th = a.b("tonguehearthand")
    val fd = a.b("farewellday")
    val pm = a.b("partmissing")
    val ground = h * 0.84f
    daySky12(SacredC59.Sky, SacredC59.SkyLow, ground, SacredC59.Ground)
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, 0.25f)) * (1f - seg(th, 0f, 0.4f))
        for (k in 0 until 3) {
            val foot = Offset(w * (0.78f - 0.28f * k), ground - h * 0.05f)
            val g = seg(ts, 0.1f + 0.2f * k, 0.3f + 0.2f * k)
            when (k) {
                0 -> drawPath(Path().apply { val c = foot + Offset(0f, -h * 0.08f); moveTo(c.x, c.y - h * 0.06f); quadraticTo(c.x + w * 0.05f, c.y + h * 0.02f, c.x, c.y + h * 0.04f); quadraticTo(c.x - w * 0.05f, c.y + h * 0.02f, c.x, c.y - h * 0.06f) }, SacredC59.Drop, alpha = f * g)
                1 -> for (j in 0 until 5) coin12(foot + Offset(w * (0.025f * (j % 3) - 0.025f), -h * (0.03f + 0.025f * (j / 3))), w * 0.016f, f * g)
                else -> scroll14(foot + Offset(0f, -h * 0.07f), w * 0.12f, h * 0.1f, 1f, f * g)
            }
            guardDome12(foot, w * 0.11f, seg(ts, 0.2f + 0.2f * k, 0.45f + 0.2f * k), t, f)
            tagAt(a.tag("threesacred", 0, k), foot + Offset(0f, -h * 0.26f), f * g, back = Back59, ink = ArtInk.Gold)
        }
    }
    if (th > 0f) {
        val f = easeOut(seg(th, 0f, 0.25f)) * (1f - seg(fd, 0f, 0.4f))
        threeLights59(a, "tonguehearthand", th, f, -1)
    }
    if (fd > 0f) {
        val f = easeOut(seg(fd, 0f, 0.25f)) * (1f - seg(pm, 0f, 0.4f))
        sun12(Offset(w * 0.8f, h * 0.15f), w * 0.04f, f)
        drawPath(Path().apply { moveTo(w * 0.15f, ground); quadraticTo(w * 0.35f, ground - h * 0.35f, w * 0.55f, ground); close() }, SacredC59.Mount, alpha = f)
        for (k in 0 until 40) {
            val g = seg(fd, 0.05f + 0.01f * k, 0.2f + 0.01f * k)
            val c = Offset(w * (0.05f + 0.9f * fract(k * 0.618f)), ground - h * 0.01f + h * 0.12f * fract(k * 0.31f))
            drawCircle(SacredC59.Scroll, w * 0.012f, c, alpha = f * g)
            drawCircle(SacredC59.Mount, w * 0.012f, c, alpha = f * g, style = Stroke(1f * dp))
        }
        tagAt(a.tag("farewellday"), Offset(w * 0.5f, h * 0.3f), f * seg(fd, 0.4f, 0.6f), back = Back59, ink = ArtInk.Gold)
    }
    if (pm > 0f) {
        val f = easeOut(seg(pm, 0f, 0.25f))
        threeLights59(a, "partmissing", pm, f, 1)
    }
}

// ═════════════ 5. good words or silence, the neighbour, the guest (nw-15) ═════════════

private object SilentC59 {
    val Dusk = Color(0xFF2A2848)
    val DuskLow = Color(0xFFD89A72)
    val Ground = Color(0xFF3A3038)
    val Gold = Color(0xFFF2C860)
    val Wall = Color(0xFFC8B494)
    val Trim = Color(0xFF6A5040)
    val Person = Color(0xFFEAE2D2)
    val Voice = Color(0xFF8AC4EA)
    val Seal = Color(0xFFB8A27E)
    val Tray = Color(0xFFD8C4A0)
    val Food = Color(0xFFC0602A)
}

/**
 * Beats: speakorstill (good words come out as gold, «قَوْلُ الْخَيْرِ»; when there is none the mouth is shut by a soft seal:
 * «فَلَا يَتَكَلَّمُ»), rightscount (three houses with three, two and one lights over them: «ثَلَاثَةُ حُقُوقٍ», «حَقَّانِ», «حَقٌّ
 * وَاحِدٌ»), guestwelcome (a door opens; a guest is brought in and a tray set before him), neighborcounsel (a cool white light
 * comes again and again to the light ﷺ, and each time a neighbour's house grows brighter), threehostdays (three days pass
 * over a guest's house; the first day and night shine most, «يَوْمٌ وَلَيْلَةٌ», then three, «ثَلَاثَةُ أَيَّامٍ», then a coin:
 * «فَهُوَ صَدَقَةٌ»).
 */
internal fun DrawScope.goodorsilentScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "speakorstill" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "rightscount" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
        "guestwelcome" to Shot(Offset(w * 0.5f, h * 0.65f), 1.1f),
        "neighborcounsel" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "threehostdays" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { silentDusk(a) }
}

private fun DrawScope.silentDusk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ss = a.b("speakorstill")
    val rc = a.b("rightscount")
    val gw = a.b("guestwelcome")
    val nc = a.b("neighborcounsel")
    val td = a.b("threehostdays")
    val ground = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(SilentC59.Dusk, SilentC59.DuskLow), 0f, ground), Offset.Zero, Size(w, ground))
    drawRect(SilentC59.Ground, Offset(0f, ground), Size(w, h - ground))
    if (ss > 0f) {
        val f = easeOut(seg(ss, 0f, 0.25f)) * (1f - seg(rc, 0f, 0.4f))
        val mouth = Offset(w * 0.7f, h * 0.5f)
        val shut = seg(ss, 0.55f, 0.75f)
        drawArc(SilentC59.Person, 20f, 140f, false, Offset(mouth.x - w * 0.08f, mouth.y - h * 0.06f * (1f - shut)), Size(w * 0.16f, h * 0.12f * (1f - shut) + 2f * dp), alpha = f, style = Stroke(4f * dp, cap = StrokeCap.Round))
        if (shut < 0.5f) for (k in 0 until 4) {
            val u = fract(t * 0.6f + k / 4f)
            softGlow(mouth + Offset(-w * 0.5f * u, -h * 0.05f * sin(u * 6f)), w * 0.02f, SilentC59.Gold, f * (1f - u) * (1f - 2f * shut))
        }
        drawRoundRect(SilentC59.Seal, Offset(mouth.x - w * 0.09f, mouth.y - h * 0.015f), Size(w * 0.18f, h * 0.03f), CornerRadius(6f * dp), alpha = f * shut)
        tagAt(a.tag("speakorstill", 0, 0), Offset(w * 0.32f, h * 0.3f), f * seg(ss, 0.1f, 0.3f), back = Back59, ink = ArtInk.Gold)
        tagAt(a.tag("speakorstill", 0, 1), Offset(w * 0.7f, h * 0.7f), f * seg(ss, 0.6f, 0.8f), back = Back59)
    }
    if (rc > 0f) {
        val f = easeOut(seg(rc, 0f, 0.25f)) * (1f - seg(gw, 0f, 0.4f))
        for (k in 0 until 3) {
            val foot = Offset(w * (0.78f - 0.28f * k), ground)
            val g = seg(rc, 0.1f + 0.22f * k, 0.3f + 0.22f * k)
            house12(foot, w * 0.18f, SilentC59.Wall, SilentC59.Trim, f, 0.5f)
            for (j in 0 until 3 - k) softGlow(foot + Offset(w * (0.04f * j - 0.04f * (2 - k) / 2f), -h * 0.28f), w * 0.02f, SilentC59.Gold, f * g)
            tagAt(a.tag("rightscount", 0, k), foot + Offset(0f, -h * 0.36f), f * g, back = Back59, ink = ArtInk.Gold)
        }
    }
    if (gw > 0f) {
        val f = easeOut(seg(gw, 0f, 0.25f)) * (1f - seg(nc, 0f, 0.4f))
        val sill = Offset(w * 0.6f, ground)
        house12(sill, w * 0.4f, SilentC59.Wall, SilentC59.Trim, f, 0.7f)
        door12(sill, w * 0.09f, h * 0.2f, seg(gw, 0.1f, 0.3f), SilentC59.Trim, SilentC59.Wall, f)
        val come = easeInOut(seg(gw, 0.25f, 0.6f))
        figure(Offset(mix(w * 0.15f, w * 0.52f, come), ground + h * 0.05f), h * 0.24f, SilentC59.Person, f, walk = t * 5f * (1f - seg(gw, 0.58f, 0.62f)))
        figure(Offset(w * 0.68f, ground + h * 0.05f), h * 0.24f, SilentC59.Person, f, arms = -0.6f * seg(gw, 0.2f, 0.35f))
        val tray = seg(gw, 0.65f, 0.85f)
        drawOval(SilentC59.Tray, Offset(w * 0.53f, ground + h * 0.08f), Size(w * 0.14f, h * 0.03f), alpha = f * tray)
        for (k in 0 until 3) drawCircle(SilentC59.Food, w * 0.01f, Offset(w * (0.57f + 0.025f * k), ground + h * 0.085f), alpha = f * tray)
    }
    if (nc > 0f) {
        val f = easeOut(seg(nc, 0f, 0.25f)) * (1f - seg(td, 0f, 0.4f))
        val light = Offset(w * 0.3f, h * 0.35f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        for (k in 0 until 3) {
            val u = seg(nc, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            val from = Offset(w * 0.5f, h * 0.08f)
            angelLight26(from + (light + Offset(w * 0.06f, -h * 0.04f) - from) * u, w * 0.008f, t, f * sin(PI.toFloat() * u))
        }
        val foot = Offset(w * 0.75f, ground)
        house12(foot, w * 0.22f, SilentC59.Wall, SilentC59.Trim, f, 0.2f + 0.8f * seg(nc, 0.2f, 0.9f))
        softGlow(foot + Offset(0f, -h * 0.12f), w * 0.15f, SilentC59.Gold, 0.4f * f * seg(nc, 0.3f, 0.9f))
    }
    if (td > 0f) {
        val f = easeOut(seg(td, 0f, 0.25f))
        val foot = Offset(w * 0.5f, ground)
        house12(foot, w * 0.24f, SilentC59.Wall, SilentC59.Trim, f, 0.6f)
        for (k in 0 until 3) {
            val g = seg(td, 0.1f + 0.18f * k, 0.25f + 0.18f * k)
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.2f)
            sun12(c, w * (if (k == 0) 0.045f else 0.03f), f * g)
            if (k == 0) softGlow(c, w * 0.12f, SilentC59.Gold, 0.5f * f * g)
        }
        tagAt(a.tag("threehostdays", 0, 0), Offset(w * 0.8f, h * 0.34f), f * seg(td, 0.1f, 0.25f), back = Back59, ink = ArtInk.Gold)
        tagAt(a.tag("threehostdays", 0, 1), Offset(w * 0.5f, h * 0.34f), f * seg(td, 0.35f, 0.5f), back = Back59, ink = ArtInk.Gold)
        val coin = seg(td, 0.7f, 0.85f)
        coin12(Offset(w * 0.2f, h * 0.45f), w * 0.04f, f * coin)
        tagAt(a.tag("threehostdays", 0, 2), Offset(w * 0.2f, h * 0.58f), f * coin, back = Back59, ink = ArtInk.Gold)
    }
}
