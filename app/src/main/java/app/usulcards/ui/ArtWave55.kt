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
 * Wave 55 (2026-11-03): «حصن المسلم»، the end of «أذكار الأحوال» and «الذكر والدعاء» (1). A mount and a road kept by
 * remembrance; an iftar at sunset when the roots drink; a sneeze and its three answers in the notebook; two rings joined
 * and the names of the prophets' sons; a living tree beside a dead one. Allah is never drawn; prophets are lights with
 * their names; angels are cool white lights; companions and Ahl al-Bayt are names in roundels.
 */

private val Back55 = Color(0xE6181420)

// ═════════════ 1. the journey (hisn-safar) ═════════════

private object RideC55 {
    val Sky = Color(0xFFB4D0E4)
    val SkyLow = Color(0xFFF4E2BE)
    val Sand = Color(0xFFD8BC8A)
    val Dune = Color(0xFFC8A472)
    val Camel = Color(0xFFB88A58)
    val Gold = Color(0xFFE8B84A)
    val Wall = Color(0xFFDCC6A0)
    val Trim = Color(0xFF8A6A44)
    val Person = Color(0xFF3A3040)
    val Road = Color(0xFFC8B088)
}

/**
 * Beats: mounted (a camel kneels and rises with the light ﷺ over its saddle; three glows of takbir), piety (a long road
 * ahead, and over it: «الْبِرَّ وَالتَّقْوَى»), homecoming (camels come back toward the town and four words come with them:
 * «آيِبُونَ», «تَائِبُونَ», «عَابِدُونَ», «لِرَبِّنَا حَامِدُونَ»), keptboth (a traveller far on his road and his house far
 * behind, each under its own dome of light), updown (plain roundels go over hills: rising, a glow «كَبَّرُوا»; going
 * down, a glow «سَبَّحُوا»).
 */
internal fun DrawScope.journeyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "mounted" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "piety" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "homecoming" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "keptboth" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "updown" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { rideDesert(a) }
}

private fun DrawScope.rideDesert(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val mo = a.b("mounted")
    val pi = a.b("piety")
    val hc = a.b("homecoming")
    val kb = a.b("keptboth")
    val ud = a.b("updown")
    val ground = h * 0.8f
    daySky12(RideC55.Sky, RideC55.SkyLow, ground, RideC55.Sand)
    if (mo > 0f) {
        val f = easeOut(seg(mo, 0f, 0.3f)) * (1f - seg(pi, 0f, BEAT_OUT))
        val rise = easeInOut(seg(mo, 0.1f, 0.4f))
        val foot = Offset(w * 0.45f, ground)
        camel26(foot + Offset(0f, h * 0.08f * (1f - rise)), h * 0.34f, RideC55.Camel, f, t)
        val saddle = foot + Offset(w * 0.0f, -h * 0.3f - h * 0.0f + h * 0.08f * (1f - rise))
        sirajMunir(saddle + Offset(0f, -h * 0.06f), w * 0.012f, f * seg(mo, 0.15f, 0.35f), t, nameAt = saddle + Offset(0f, -h * 0.18f))
        for (k in 0 until 3) softGlow(Offset(w * (0.65f + 0.08f * k), h * 0.3f), w * 0.025f, RideC55.Gold, 0.8f * f * seg(mo, 0.45f + 0.12f * k, 0.55f + 0.12f * k))
    }
    if (pi > 0f) {
        val f = easeOut(seg(pi, 0f, BEAT_IN)) * (1f - seg(hc, 0f, BEAT_OUT))
        val run = easeInOut(seg(pi, 0.05f, 0.6f))
        drawPath(Path().apply { moveTo(w * 0.38f, h); lineTo(w * (0.5f - 0.02f * run), ground - h * 0.2f * run); lineTo(w * (0.5f + 0.02f * run), ground - h * 0.2f * run); lineTo(w * 0.62f, h); close() }, RideC55.Road, alpha = f)
        for (k in 0 until 4) drawPath(Path().apply { val x = w * (0.1f + 0.25f * k); moveTo(x - w * 0.15f, ground); quadraticTo(x, ground - h * 0.12f, x + w * 0.15f, ground); close() }, RideC55.Dune, alpha = f * 0.8f)
        tagAt(a.tag("piety"), Offset(w * 0.5f, h * 0.36f), f * seg(pi, 0.4f, 0.6f), back = Back55, ink = ArtInk.Gold)
    }
    if (hc > 0f) {
        val f = easeOut(seg(hc, 0f, BEAT_IN)) * (1f - seg(kb, 0f, BEAT_OUT))
        for (k in 0 until 4) house12(Offset(w * (0.08f + 0.1f * k), ground), w * 0.09f, RideC55.Wall, RideC55.Trim, f, 0.5f)
        val come = easeInOut(seg(hc, 0.05f, 0.7f))
        for (k in 0 until 3) camel26(Offset(w * (0.9f - 0.35f * come + 0.13f * k), ground), h * 0.24f, RideC55.Camel, f, t, t * 3f * (1f - come))
        for (k in 0 until 4) tagAt(a.tag("homecoming", 0, k), Offset(w * (0.2f + 0.2f * k), h * (0.2f + 0.06f * (k % 2))), f * seg(hc, 0.2f + 0.17f * k, 0.35f + 0.17f * k), back = Back55, ink = ArtInk.Gold)
    }
    if (kb > 0f) {
        val f = easeOut(seg(kb, 0f, BEAT_IN)) * (1f - seg(ud, 0f, BEAT_OUT))
        val house = Offset(w * 0.18f, ground)
        house12(house, w * 0.16f, RideC55.Wall, RideC55.Trim, f, 0.6f)
        guardDome12(house, w * 0.14f, seg(kb, 0.4f, 0.7f), t, f)
        drawLine(RideC55.Road, Offset(w * 0.25f, ground + h * 0.03f), Offset(w * 0.95f, ground + h * 0.03f), h * 0.03f, alpha = f)
        val go = easeInOut(seg(kb, 0.05f, 0.5f))
        val p = Offset(w * (0.35f + 0.42f * go), ground)
        figure(p, h * 0.26f, RideC55.Person, f, walk = t * 5f * (1f - seg(kb, 0.45f, 0.5f)))
        guardDome12(p, w * 0.1f, seg(kb, 0.15f, 0.45f), t, f)
    }
    if (ud > 0f) {
        val f = easeOut(seg(ud, 0f, 0.3f))
        val hill = Path().apply { moveTo(0f, ground); quadraticTo(w * 0.25f, ground - h * 0.5f, w * 0.5f, ground - h * 0.1f); quadraticTo(w * 0.75f, ground + h * 0.2f, w, ground) ; lineTo(w, h); lineTo(0f, h); close() }
        drawPath(hill, RideC55.Dune, alpha = f)
        val u = seg(ud, 0.05f, 0.95f)
        for (k in 0 until 3) {
            val uu = (u - 0.06f * k).coerceIn(0f, 1f)
            val x = w * uu
            val y = if (uu < 0.5f) ground - h * 0.5f * (1f - (1f - 2f * uu) * (1f - 2f * uu)) * 0.62f else ground - h * 0.1f + h * 0.1f * (uu - 0.5f) * 2f
            nameRoundel(Offset(x, y - h * 0.06f), w * 0.025f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        }
        tagAt(a.tag("updown", 0, 0), Offset(w * 0.22f, h * 0.18f), f * seg(ud, 0.15f, 0.3f) * (1f - seg(ud, 0.5f, 0.6f)), back = Back55, ink = ArtInk.Gold)
        tagAt(a.tag("updown", 0, 1), Offset(w * 0.72f, h * 0.45f), f * seg(ud, 0.55f, 0.7f), back = Back55, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. iftar at sunset (hisn-taam) ═════════════

private object IftarC55 {
    val Sky = Color(0xFF3A3058)
    val SkyLow = Color(0xFFE8906A)
    val Ground = Color(0xFF5A4636)
    val Cloth = Color(0xFFB04A3A)
    val Person = Color(0xFFE6DCC8)
    val Gold = Color(0xFFF2C45A)
    val Water = Color(0xFF7AB8E0)
    val Crack = Color(0xFF3A2A20)
    val Root = Color(0xFFC8A070)
    val Wall = Color(0xFF8A7464)
    val Trim = Color(0xFF5A4636)
    val Spike = Color(0xFF8A5A5A)
    val Palm = Color(0xFF3E6A44)
    val Date = Color(0xFFC0602A)
}

/**
 * Beats: guestdua (a guest at a host's cloth raises his hands: «بَارِكْ لَهُمْ», «وَاغْفِرْ لَهُمْ», «وَارْحَمْهُمْ»), rootsdrink
 * (sunset; water is poured and the dry, cracked earth closes as roots drink: «وَثَبَتَ الْأَجْرُ»), angelsover (a house where
 * fasting guests break their fast; cool white lights gather over it), calmfast (sharp shapes are thrown at one; he stays
 * calm behind a word: «إِنِّي صَائِمٌ»), firstdates (the first dates of the season glow on a palm: «بَارِكْ لَنَا فِي ثَمَرِنَا»).
 */
internal fun DrawScope.iftarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "guestdua" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "rootsdrink" to Shot(Offset(w * 0.5f, h * 0.7f), 1.15f),
        "angelsover" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "calmfast" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "firstdates" to Shot(Offset(w * 0.5f, h * 0.45f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { iftarDusk(a) }
}

private fun DrawScope.iftarDusk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val gd = a.b("guestdua")
    val rd = a.b("rootsdrink")
    val ao = a.b("angelsover")
    val cf = a.b("calmfast")
    val fd = a.b("firstdates")
    val ground = h * 0.78f
    drawRect(Brush.verticalGradient(listOf(IftarC55.Sky, IftarC55.SkyLow), 0f, ground), Offset.Zero, Size(w, ground))
    drawRect(IftarC55.Ground, Offset(0f, ground), Size(w, h - ground))
    if (gd > 0f) {
        val f = easeOut(seg(gd, 0f, BEAT_IN)) * (1f - seg(rd, 0f, BEAT_OUT))
        drawOval(IftarC55.Cloth, Offset(w * 0.28f, ground - h * 0.02f), Size(w * 0.44f, h * 0.08f), alpha = f)
        for (k in 0 until 3) cup12(Offset(w * (0.4f + 0.1f * k), ground + h * 0.01f), w * 0.05f, Color(0xFFD8C4A0), f, IftarC55.Date)
        sit12(Offset(w * 0.24f, ground + h * 0.02f), h * 0.28f, IftarC55.Person, f, 1f)
        sit12(Offset(w * 0.76f, ground + h * 0.02f), h * 0.28f, IftarC55.Person, f, -1f)
        hands12(Offset(w * 0.26f, ground - h * 0.3f), h * 0.07f, IftarC55.Person, f * seg(gd, 0.15f, 0.3f))
        for (k in 0 until 3) tagAt(a.tag("guestdua", 0, k), Offset(w * (0.3f + 0.2f * k), h * (0.22f + 0.06f * (k % 2))), f * seg(gd, 0.25f + 0.2f * k, 0.4f + 0.2f * k), back = Back55, ink = ArtInk.Gold)
    }
    if (rd > 0f) {
        val f = easeOut(seg(rd, 0f, BEAT_IN)) * (1f - seg(ao, 0f, BEAT_OUT))
        val soak = seg(rd, 0.25f, 0.8f)
        for (k in 0 until 7) {
            val x = w * (0.15f + 0.12f * k)
            drawLine(IftarC55.Crack, Offset(x, ground + h * 0.02f), Offset(x + w * 0.03f, ground + h * 0.12f), 2f * dp * (1f - soak), alpha = f)
        }
        drawRect(IftarC55.Water, Offset(0f, ground), Size(w, h * 0.22f * soak), alpha = f * 0.25f)
        for (k in 0 until 5) {
            val x = w * (0.2f + 0.15f * k)
            val path = Path().apply { moveTo(x, ground); quadraticTo(x - w * 0.02f, ground + h * 0.08f, x + w * 0.01f, ground + h * 0.18f) }
            drawPath(path, lerpColor(IftarC55.Root, IftarC55.Water, soak), alpha = f, style = Stroke(2f * dp))
            drawLine(Color(0xFF5A9A5A), Offset(x, ground), Offset(x, ground - h * 0.06f * (0.3f + soak)), 2.5f * dp, StrokeCap.Round, alpha = f)
        }
        val pour = Offset(w * 0.5f, h * 0.35f)
        cup12(pour, w * 0.07f, Color(0xFFD8C4A0), f * (1f - seg(rd, 0.4f, 0.5f)), IftarC55.Water)
        tagAt(a.tag("rootsdrink"), Offset(w * 0.5f, h * 0.2f), f * seg(rd, 0.6f, 0.8f), back = Back55, ink = ArtInk.Gold)
    }
    if (ao > 0f) {
        val f = easeOut(seg(ao, 0f, BEAT_IN)) * (1f - seg(cf, 0f, BEAT_OUT))
        house12(Offset(w * 0.5f, ground), w * 0.3f, IftarC55.Wall, IftarC55.Trim, f, 0.9f)
        for (k in 0 until 9) {
            val ang = PI.toFloat() * (1.1f + 0.8f * k / 8f)
            val c = Offset(w * 0.5f + cos(ang) * w * 0.26f, ground - h * 0.2f + sin(ang) * h * 0.3f)
            angelLight26(c, w * 0.007f, t + k, f * seg(ao, 0.1f + 0.06f * k, 0.3f + 0.06f * k))
        }
    }
    if (cf > 0f) {
        val f = easeOut(seg(cf, 0f, BEAT_IN)) * (1f - seg(fd, 0f, BEAT_OUT))
        val p = Offset(w * 0.4f, ground)
        figure(p, h * 0.32f, IftarC55.Person, f)
        figure(Offset(w * 0.82f, ground), h * 0.3f, IftarC55.Person, f, arms = -0.7f)
        for (k in 0 until 5) {
            val u = fract(t * 0.7f + k / 5f) * seg(cf, 0.1f, 0.2f)
            val start = Offset(w * 0.78f, ground - h * 0.24f)
            val stop = Offset(w * 0.52f, ground - h * 0.2f)
            val q = start + (stop - start) * u
            for (j in 0 until 4) drawLine(IftarC55.Spike, q, q + Offset(cos(j * 1.6f + k), sin(j * 1.6f + k)) * (w * 0.01f), 1.5f * dp, alpha = f * (1f - u))
        }
        drawLine(IftarC55.Gold, Offset(w * 0.5f, ground - h * 0.38f), Offset(w * 0.5f, ground), 2.5f * dp, alpha = f * seg(cf, 0.3f, 0.5f))
        tagAt(a.tag("calmfast"), Offset(w * 0.4f, h * 0.22f), f * seg(cf, 0.35f, 0.55f), back = Back55, ink = ArtInk.Gold)
    }
    if (fd > 0f) {
        val f = easeOut(seg(fd, 0f, 0.3f))
        val foot = Offset(w * 0.5f, ground)
        palm12(foot, h * 0.6f, Color(0xFF6A4A32), IftarC55.Palm, f, t)
        val top = foot + Offset(h * 0.6f * 0.06f, -h * 0.6f)
        for (k in 0 until 8) {
            val g = seg(fd, 0.2f + 0.05f * k, 0.35f + 0.05f * k)
            val p = top + Offset(w * (0.02f * (k % 4) - 0.03f), h * (0.05f + 0.025f * (k / 4)))
            drawCircle(IftarC55.Date, w * 0.009f, p, alpha = f * g)
            softGlow(p, w * 0.02f, IftarC55.Gold, 0.3f * f * g)
        }
        tagAt(a.tag("firstdates"), Offset(w * 0.5f, h * 0.12f), f * seg(fd, 0.5f, 0.7f), back = Back55, ink = ArtInk.Gold)
    }
}

// ═════════════ 3. the sneeze in the notebook (hisn-atas) ═════════════

/**
 * Beats: threeanswers (three boxes linked by arrows: «الْحَمْدُ لِلَّهِ», «يَرْحَمُكَ اللَّهُ», «يَهْدِيكُمُ اللَّهُ وَيُصْلِحُ
 * بَالَكُمْ»), nopraise (without the first box the chain is empty — struck out), coolword (two quarrel; a red flame over one;
 * a word comes, «أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ», and the flame goes out), twopraises (two cards for two
 * kinds of day: «بِنِعْمَتِهِ تَتِمُّ الصَّالِحَاتُ», «عَلَى كُلِّ حَالٍ»), blessit (something admired is ringed by a
 * word: «بِالْبَرَكَةِ»).
 */
internal fun DrawScope.sneezebookScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threeanswers" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "nopraise" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "coolword" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "twopraises" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "blessit" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0f) { sneezePage(a) }
}

private fun DrawScope.sneezePage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pp = Paper(a.dark)
    paperSheet(pp)
    val ta = a.b("threeanswers")
    val np = a.b("nopraise")
    val cw = a.b("coolword")
    val tp = a.b("twopraises")
    val bl = a.b("blessit")
    val pen = 2.2f * dp
    if (ta > 0f) {
        val f = easeOut(seg(ta, 0f, BEAT_IN)) * (1f - seg(np, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val c = Offset(w * (0.8f - 0.3f * k), h * (0.35f + 0.15f * k))
            val g = seg(ta, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            note(a.tag("threeanswers", 0, k), c, f * g, pp, if (k == 1) pp.good else pp.amber, boxed = true)
            if (k > 0) {
                val prev = Offset(w * (0.8f - 0.3f * (k - 1)), h * (0.35f + 0.15f * (k - 1)))
                penLine(listOf(prev + Offset(-w * 0.08f, h * 0.03f), c + Offset(w * 0.08f, -h * 0.03f)), g, pp.ink, pen, f)
                arrowHead(c + Offset(w * 0.08f, -h * 0.03f), c - prev, w * 0.015f, pp.ink, pen, f * g)
            }
        }
    }
    if (np > 0f) {
        val f = easeOut(seg(np, 0f, BEAT_IN)) * (1f - seg(cw, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val c = Offset(w * (0.8f - 0.3f * k), h * (0.35f + 0.15f * k))
            penLine(listOf(c + Offset(-w * 0.09f, -h * 0.05f), c + Offset(w * 0.09f, -h * 0.05f), c + Offset(w * 0.09f, h * 0.05f), c + Offset(-w * 0.09f, h * 0.05f), c + Offset(-w * 0.09f, -h * 0.05f)), seg(np, 0.05f + 0.1f * k, 0.2f + 0.1f * k), pp.faint, pen, f, dash = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp)))
        }
        note(a.tag("nopraise", 0, 0), Offset(w * 0.8f, h * 0.35f), f * seg(np, 0.15f, 0.3f), pp, pp.bad)
        note(a.tag("nopraise", 0, 1), Offset(w * 0.5f, h * 0.5f), f * seg(np, 0.3f, 0.45f), pp, pp.ink)
        crossMark(Offset(w * 0.5f, h * 0.5f), w * 0.08f, pp.bad, pen * 1.6f, f * seg(np, 0.5f, 0.7f))
    }
    if (cw > 0f) {
        val f = easeOut(seg(cw, 0f, BEAT_IN)) * (1f - seg(tp, 0f, BEAT_OUT))
        val ground = h * 0.82f
        figure(Offset(w * 0.32f, ground), h * 0.34f, pp.ink, f, arms = 0.6f)
        figure(Offset(w * 0.62f, ground), h * 0.34f, pp.ink, f, arms = -0.6f)
        val out = seg(cw, 0.55f, 0.8f)
        val head = Offset(w * 0.32f, ground - h * 0.36f)
        val fl = (1f - out) * (1f + 0.15f * sin(t * 10f))
        drawOval(pp.bad, Offset(head.x - w * 0.015f * fl, head.y - h * 0.08f * fl), Size(w * 0.03f * fl, h * 0.08f * fl), alpha = f)
        val head2 = Offset(w * 0.62f, ground - h * 0.36f)
        drawOval(pp.bad, Offset(head2.x - w * 0.015f, head2.y - h * 0.08f), Size(w * 0.03f, h * 0.08f), alpha = f)
        // angry words between them
        for (k in 0 until 3) drawLine(pp.bad, Offset(w * (0.4f + 0.05f * k), ground - h * 0.3f), Offset(w * (0.43f + 0.05f * k), ground - h * (0.27f + 0.03f * (k % 2))), pen, StrokeCap.Round, alpha = f * (1f - out))
        note(a.tag("coolword", 0, 1), Offset(w * 0.47f, ground - h * 0.42f), f * seg(cw, 0.1f, 0.25f) * (1f - out), pp, pp.bad)
        note(a.tag("coolword"), Offset(w * 0.5f, h * 0.2f), f * seg(cw, 0.35f, 0.55f), pp, pp.good, boxed = true)
    }
    if (tp > 0f) {
        val f = easeOut(seg(tp, 0f, BEAT_IN)) * (1f - seg(bl, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val c = Offset(w * (0.3f + 0.4f * k), h * 0.5f)
            val g = seg(tp, 0.1f + 0.35f * k, 0.3f + 0.35f * k)
            penLine(listOf(c + Offset(-w * 0.15f, -h * 0.2f), c + Offset(w * 0.15f, -h * 0.2f), c + Offset(w * 0.15f, h * 0.2f), c + Offset(-w * 0.15f, h * 0.2f), c + Offset(-w * 0.15f, -h * 0.2f)), g, pp.ink, pen, f)
            if (k == 0) drawCircle(pp.amber, w * 0.03f, c + Offset(0f, -h * 0.08f), alpha = f * g)
            else cloud38(c + Offset(0f, -h * 0.08f), w * 0.04f, pp.faint, f * g)
            note(a.tag("twopraises", 0, k), c + Offset(0f, h * 0.1f), f * g, pp, pp.amber)
        }
    }
    if (bl > 0f) {
        val f = easeOut(seg(bl, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.55f)
        for (k in 0 until 5) palm12(c + Offset(w * (0.08f * k - 0.16f), h * 0.2f), h * 0.3f, pp.ink, pp.good, f, t + k)
        drawOval(pp.amber, Offset(c.x - w * 0.28f, c.y - h * 0.22f), Size(w * 0.56f, h * 0.5f), alpha = f * seg(bl, 0.3f, 0.6f), style = Stroke(pen))
        note(a.tag("blessit"), Offset(w * 0.5f, h * 0.18f), f * seg(bl, 0.4f, 0.6f), pp, pp.amber)
    }
}

// ═════════════ 4. two rings joined (hisn-zawaj) ═════════════

private object RingsC55 {
    val Night = Color(0xFF181A30)
    val NightLow = Color(0xFF3A2E54)
    val Ground = Color(0xFF221E2C)
    val Gold = Color(0xFFE8C46A)
    val Silver = Color(0xFFD8DCE6)
    val Hasan = Color(0xFF1E5A46)
    val Husain = Color(0xFF5A1E2B)
    val Snake = Color(0xFF6A7A4A)
    val Gift = Color(0xFFB04A3A)
    val Ribbon = Color(0xFFE8C46A)
}

/**
 * Beats: tworings (two rings, gold and silver, come together and join under three words: «بَارَكَ اللَّهُ لَكَ», «وَبَارَكَ
 * عَلَيْكَ», «وَجَمَعَ بَيْنَكُمَا فِي خَيْرٍ»), hasanayn (the light ﷺ and two roundels, «الْحَسَنَ», «وَالْحُسَيْنَ», under a
 * ring of protection), fatherofprophets (a light with his name, «إِبْرَاهِيمُ», and two smaller lights with theirs:
 * «إِسْمَاعِيلَ», «وَإِسْحَاقَ»), venomback (a snake comes toward a ring and turns away), thanksgift (a wrapped gift passes
 * and a word goes back with it: «جَزَاكَ اللَّهُ خَيْرًا»).
 */
internal fun DrawScope.weddinglightsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "tworings" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "hasanayn" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "fatherofprophets" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "venomback" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "thanksgift" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { ringsNight(a) }
}

private fun DrawScope.ringsNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tr = a.b("tworings")
    val hn = a.b("hasanayn")
    val fp = a.b("fatherofprophets")
    val vb = a.b("venomback")
    val tg = a.b("thanksgift")
    val ground = h * 0.84f
    nightSky(t, RingsC55.Night, RingsC55.NightLow, ground, starAlpha = 0.8f)
    drawRect(RingsC55.Ground, Offset(0f, ground), Size(w, h - ground))
    if (tr > 0f) {
        val f = easeOut(seg(tr, 0f, BEAT_IN)) * (1f - seg(hn, 0f, BEAT_OUT))
        val join = easeInOut(seg(tr, 0.1f, 0.45f))
        val r = w * 0.08f
        drawCircle(RingsC55.Gold, r, Offset(w * (0.3f + 0.15f * join), h * 0.55f), alpha = f, style = Stroke(6f * dp))
        drawCircle(RingsC55.Silver, r, Offset(w * (0.7f - 0.15f * join), h * 0.55f), alpha = f, style = Stroke(6f * dp))
        softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.1f, RingsC55.Gold, 0.5f * f * seg(tr, 0.4f, 0.6f))
        for (k in 0 until 3) tagAt(a.tag("tworings", 0, k), Offset(w * (0.25f + 0.25f * k), h * (0.2f + 0.05f * (k % 2))), f * seg(tr, 0.3f + 0.2f * k, 0.45f + 0.2f * k), back = Back55, ink = ArtInk.Gold)
    }
    if (hn > 0f) {
        val f = easeOut(seg(hn, 0f, BEAT_IN)) * (1f - seg(fp, 0f, BEAT_OUT))
        val light = Offset(w * 0.5f, h * 0.3f)
        sirajMunir(light, w * 0.013f, f, t, nameAt = light + Offset(0f, -h * 0.12f))
        nameRoundel(Offset(w * 0.38f, h * 0.6f), w * 0.055f, a.big("hasanayn", 0, 0), RingsC55.Hasan, Roundel.Rim, f * seg(hn, 0.1f, 0.25f))
        nameRoundel(Offset(w * 0.62f, h * 0.6f), w * 0.055f, a.big("hasanayn", 0, 1), RingsC55.Husain, Roundel.Rim, f * seg(hn, 0.2f, 0.35f))
        guardDome12(Offset(w * 0.5f, h * 0.72f), w * 0.24f, seg(hn, 0.4f, 0.7f), t, f)
    }
    if (fp > 0f) {
        val f = easeOut(seg(fp, 0f, BEAT_IN)) * (1f - seg(vb, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.32f)
        nabiLight14(c, w * 0.012f, f * seg(fp, 0.05f, 0.25f), t, a.tag("fatherofprophets", 0, 0), c + Offset(0f, -h * 0.12f))
        for (k in 0 until 2) {
            val p = Offset(w * (0.3f + 0.4f * k), h * 0.62f)
            nabiLight14(p, w * 0.008f, f * seg(fp, 0.3f + 0.2f * k, 0.5f + 0.2f * k), t, a.tag("fatherofprophets", 0, k + 1), p + Offset(0f, h * 0.11f))
            guardDome12(p + Offset(0f, h * 0.04f), w * 0.08f, seg(fp, 0.6f, 0.9f), t, f)
        }
    }
    if (vb > 0f) {
        val f = easeOut(seg(vb, 0f, BEAT_IN)) * (1f - seg(tg, 0f, BEAT_OUT))
        val c = Offset(w * 0.35f, ground)
        guardDome12(c, w * 0.14f, 1f, t, f)
        val come = seg(vb, 0.1f, 0.5f)
        val back = easeInOut(seg(vb, 0.55f, 0.95f))
        val x = w * (0.85f - 0.3f * come + 0.35f * back)
        val body = Path().apply {
            moveTo(x, ground - h * 0.02f)
            for (k in 1..8) lineTo(x + w * 0.02f * k, ground - h * 0.02f + h * 0.015f * sin(k * 1.2f + t * 5f))
        }
        drawPath(body, RingsC55.Snake, alpha = f, style = Stroke(5f * dp, cap = StrokeCap.Round))
        drawCircle(RingsC55.Snake, w * 0.012f, Offset(x, ground - h * 0.02f), alpha = f)
    }
    if (tg > 0f) {
        val f = easeOut(seg(tg, 0f, 0.3f))
        val go = easeInOut(seg(tg, 0.1f, 0.5f))
        val c = Offset(w * (0.25f + 0.45f * go), h * 0.55f)
        drawRoundRect(RingsC55.Gift, Offset(c.x - w * 0.05f, c.y - h * 0.06f), Size(w * 0.1f, h * 0.12f), CornerRadius(4f * dp), alpha = f)
        drawLine(RingsC55.Ribbon, Offset(c.x, c.y - h * 0.06f), Offset(c.x, c.y + h * 0.06f), 3f * dp, alpha = f)
        drawLine(RingsC55.Ribbon, Offset(c.x - w * 0.05f, c.y), Offset(c.x + w * 0.05f, c.y), 3f * dp, alpha = f)
        val back = seg(tg, 0.55f, 0.9f)
        softGlow(Offset(w * (0.7f - 0.45f * back), h * 0.35f), w * 0.04f, RingsC55.Gold, 0.7f * f * back)
        tagAt(a.tag("thanksgift"), Offset(w * 0.5f, h * 0.22f), f * seg(tg, 0.55f, 0.75f), back = Back55, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. the living and the dead (hisn-fadl) ═════════════

private object AliveC55 {
    val Sky = Color(0xFFC4DAE8)
    val SkyLow = Color(0xFFF2E8D2)
    val Green = Color(0xFF4E8A54)
    val Dry = Color(0xFFB8A27E)
    val Trunk = Color(0xFF6A4A32)
    val Bare = Color(0xFF8A7A6A)
    val Gold = Color(0xFFE8B84A)
    val Silver = Color(0xFFD8DCE6)
    val Beam = Color(0xFF6A4A2E)
    val Letter = Color(0xFF2E5A4A)
}

/**
 * Beats: twotrees (two trees side by side: one green and full of life with a soft glow, the other bare and grey),
 * outweighs (gold and silver coins heaped on one pan; a small light of remembrance on the other brings it down), moist (a
 * dry branch turns green and supple again), tenletters (three letters, «أَلِفٌ», «وَلَامٌ», «وَمِيمٌ», each with ten small
 * lights about it).
 */
internal fun DrawScope.alivetreeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twotrees" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "outweighs" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "moist" to Shot(Offset(w * 0.5f, h * 0.5f), 1.25f),
        "tenletters" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { aliveDay(a) }
}

private fun DrawScope.aliveDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tt = a.b("twotrees")
    val ow = a.b("outweighs")
    val mo = a.b("moist")
    val tl = a.b("tenletters")
    val ground = h * 0.84f
    daySky12(AliveC55.Sky, AliveC55.SkyLow, ground, Color(0xFFCDB890))
    if (tt > 0f) {
        val f = easeOut(seg(tt, 0f, BEAT_IN)) * (1f - seg(ow, 0f, BEAT_OUT))
        val l = Offset(w * 0.3f, ground)
        drawLine(AliveC55.Trunk, l, l + Offset(0f, -h * 0.34f), w * 0.025f, StrokeCap.Round, alpha = f)
        for (k in 0 until 5) drawCircle(AliveC55.Green, w * (0.07f + 0.01f * (k % 2)), l + Offset(w * 0.05f * cos(k * 1.3f), -h * 0.42f + h * 0.06f * sin(k * 1.3f) + h * 0.008f * sin(t + k)), alpha = f)
        softGlow(l + Offset(0f, -h * 0.42f), w * 0.15f, AliveC55.Gold, 0.35f * f * seg(tt, 0.3f, 0.6f))
        val r = Offset(w * 0.7f, ground)
        drawLine(AliveC55.Bare, r, r + Offset(0f, -h * 0.34f), w * 0.022f, StrokeCap.Round, alpha = f)
        for (k in 0 until 4) {
            val s = r + Offset(0f, -h * (0.18f + 0.06f * k))
            drawLine(AliveC55.Bare, s, s + Offset((if (k % 2 == 0) 1f else -1f) * w * 0.07f, -h * 0.06f), 2.5f * dp, StrokeCap.Round, alpha = f)
        }
    }
    if (ow > 0f) {
        val f = easeOut(seg(ow, 0f, BEAT_IN)) * (1f - seg(mo, 0f, 0.4f))
        val tilt = mix(0.7f, -0.7f, easeInOut(seg(ow, 0.45f, 0.85f)))
        val foot = Offset(w * 0.5f, ground)
        val s = h * 0.6f
        balance12(foot, s, tilt, AliveC55.Beam, f)
        val (lp, rp) = balancePans12(foot, s, tilt)
        for (k in 0 until 8) coin12(rp + Offset(w * (0.018f * (k % 4) - 0.027f), -h * (0.03f + 0.025f * (k / 4))), w * 0.012f, f * seg(ow, 0.05f + 0.03f * k, 0.2f + 0.03f * k), if (k % 2 == 0) AliveC55.Gold else AliveC55.Silver)
        softGlow(lp + Offset(0f, -h * 0.03f), w * 0.04f, AliveC55.Gold, 0.9f * f * seg(ow, 0.3f, 0.45f))
    }
    if (mo > 0f) {
        val f = easeOut(seg(mo, 0f, 0.3f)) * (1f - seg(tl, 0f, BEAT_OUT))
        val wet = easeInOut(seg(mo, 0.2f, 0.8f))
        val c = Offset(w * 0.5f, h * 0.55f)
        val path = Path().apply { moveTo(c.x - w * 0.2f, c.y + h * 0.1f); quadraticTo(c.x, c.y - h * (0.05f + 0.15f * wet), c.x + w * 0.2f, c.y - h * 0.1f) }
        drawPath(path, lerpColor(AliveC55.Dry, AliveC55.Green, wet), alpha = f, style = Stroke(6f * dp, cap = StrokeCap.Round))
        for (k in 0 until 6) {
            val p = cubic(Offset(c.x - w * 0.2f, c.y + h * 0.1f), Offset(c.x - w * 0.1f, c.y - h * 0.1f * wet), Offset(c.x + w * 0.1f, c.y - h * 0.1f * wet), Offset(c.x + w * 0.2f, c.y - h * 0.1f), 0.15f + 0.14f * k)
            drawOval(AliveC55.Green, Offset(p.x - w * 0.015f, p.y - h * 0.04f), Size(w * 0.03f, h * 0.035f), alpha = f * seg(mo, 0.4f + 0.06f * k, 0.55f + 0.06f * k))
        }
    }
    if (tl > 0f) {
        val f = easeOut(seg(tl, 0f, 0.3f))
        for (k in 0 until 3) {
            val c = Offset(w * (0.25f + 0.25f * k), h * 0.5f)
            val g = seg(tl, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            fitBig26(a.big("tenletters", 0, k), c, w * 0.14f, AliveC55.Letter, f * g)
            for (j in 0 until 10) {
                val ang = j * PI.toFloat() / 5f + t * 0.3f
                softGlow(c + Offset(cos(ang) * w * 0.09f, sin(ang) * h * 0.14f), w * 0.01f, AliveC55.Gold, 0.9f * f * seg(tl, 0.2f + 0.25f * k + 0.01f * j, 0.3f + 0.25f * k + 0.01f * j))
            }
        }
    }
}
