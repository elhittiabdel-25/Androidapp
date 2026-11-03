package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 82 (2026-11-03): v1.3 fiqh, prayer 3: the obligatory acts and sunnahs, what is disliked in prayer, the prostration of
 * forgetfulness, the remembrance after prayer, the regular sunnahs and the witr. Rakʿas are small arches, words are lights and
 * beads; the one praying is a faceless figure; Shaytan is dark smoke; the angels who witness the night are pale lights.
 */

/** Dark smoke that sinks and thins with [down]. */
private fun DrawScope.smokeDown82(c: Offset, r: Float, down: Float, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 8) {
        val ang = k * 0.8f + t * 0.5f
        drawCircle(Color(0xFF2A2430), r * (0.5f + 0.25f * (k % 3)) * (1f - 0.5f * down), c + Offset(sin(ang) * r * 0.8f, cos(ang * 1.3f) * r * 0.4f + r * 2f * down), alpha = alpha * (1f - 0.7f * down))
    }
}

// ═════════════ 13. sal-wajibat: the obligatory acts and the sunnahs ═════════════

/**
 * Beats: eightduties (eight rings; one left on purpose cracks the prayer, one forgotten is mended by a small prostration: «مَنْ تَرَكَ مِنْهَا
 * شَيْئًا عَمْدًا بَطَلَتْ صَلَاتُهُ», «وَمَنْ تَرَكَهُ سَهْوًا أَوْ جَهْلًا سَجَدَ لِلسَّهْوِ»), transitions (the postures follow one another with a
 * small light between each, and the first sitting is marked: «تَكْبِيرَاتُ الِانْتِقَالِ», «وَالتَّشَهُّدُ الْأَوَّلُ وَجُلُوسُهُ»), extrareward (fine
 * arches set on a firm building, each with a star: «لَا تَبْطُلُ الصَّلَاةُ بِتَرْكِهَا», «مَنْ فَعَلَهَا فَلَهُ زِيَادَةُ أَجْرٍ»), threetoten (a bar of
 * ten glorifications: three lit, then all ten: «ثَلَاثٍ وَهِيَ أَدْنَى الْكَمَالِ», «وَإِلَى عَشْرٍ وَهِيَ أَعْلَاهُ»), followerpraise (the one in front
 * calls and the rows answer with praise: «وَإِنَّمَا يَقُولُ: رَبَّنَا وَلَكَ الْحَمْدُ»).
 */
internal fun DrawScope.wajibatprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "eightduties" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "transitions" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
        "extrareward" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "threetoten" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "followerpraise" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { wajibatPrayerDraw(a) }
}

private fun DrawScope.wajibatPrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val ed = fqIn(a, "eightduties", "transitions")
    val tr = fqIn(a, "transitions", "extrareward")
    val er = fqIn(a, "extrareward", "threetoten")
    val tt = fqIn(a, "threetoten", "followerpraise")
    val fp = fqIn(a, "followerpraise")
    if (ed > 0f) {
        val p = a.b("eightduties")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.75f * ed)
        for (k in 0 until 8) {
            val c = Offset(w * (0.88f - 0.108f * k), h * 0.42f)
            val g = seg(p, 0.02f * k, 0.02f * k + 0.1f)
            val bad = k == 2
            val forgot = k == 5
            drawCircle(SalC.Gold, w * 0.035f, c, alpha = ed * g * (if (bad) 1f - 0.7f * seg(p, 0.25f, 0.4f) else 1f), style = Stroke(3f * dp))
            if (bad) cross14(c, w * 0.03f, seg(p, 0.3f, 0.45f))
            if (forgot) {
                val mend = seg(p, 0.6f, 0.8f)
                bow12(c + Offset(w * 0.03f, h * 0.15f), h * 0.12f, SalC.Person, ed * mend, 1f)
                drawCircle(Fq.Water, w * 0.035f, c, alpha = ed * mend, style = Stroke(3f * dp))
            }
        }
        tagAt(a.tag("eightduties", 0, 0), Offset(w * 0.5f, h * 0.18f), ed * seg(p, 0.25f, 0.45f), back = Fq.Back, ink = ArtInk.Ember)
        tagAt(a.tag("eightduties", 0, 1), Offset(w * 0.5f, h * 0.78f), ed * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (tr > 0f) {
        val p = a.b("transitions")
        val y = floor + h * 0.3f
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.12f * k, 0.15f + 0.12f * k)
            val foot = Offset(w * (0.84f - 0.22f * k), y)
            when (k) {
                0 -> takbir80(foot, h * 0.3f, SalC.Person, tr * g, 0f)
                1 -> bow12(foot, h * 0.3f, SalC.Person, tr * g, 0.5f)
                2 -> bow12(foot, h * 0.3f, SalC.Person, tr * g, 1f)
                else -> kneel80(foot, h * 0.3f, SalC.Person, tr * g)
            }
            if (k < 3) {
                val c = Offset(w * (0.73f - 0.22f * k), y - h * 0.3f)
                softGlow(c, w * 0.03f, SalC.Gold, tr * seg(p, 0.12f + 0.12f * k, 0.2f + 0.12f * k))
            }
        }
        tagAt(a.tag("transitions", 0, 0), Offset(w * 0.5f, h * 0.12f), tr * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        softGlow(Offset(w * 0.18f, y - h * 0.12f), w * 0.08f, SalC.Gold, 0.6f * tr * seg(p, 0.6f, 0.75f))
        tagAt(a.tag("transitions", 0, 1), Offset(w * 0.25f, h * 0.26f), tr * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (er > 0f) {
        val p = a.b("extrareward")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.75f * er)
        val ground = h * 0.82f
        drawRect(SalC.Mosque, Offset(w * 0.2f, ground - h * 0.3f), Size(w * 0.6f, h * 0.3f), alpha = er)
        for (k in 0 until 4) {
            val g = seg(p, 0.3f + 0.1f * k, 0.4f + 0.1f * k)
            val x = w * (0.28f + 0.147f * k)
            drawArc(SalC.Dome, 180f, 180f, true, Offset(x - w * 0.05f, ground - h * 0.38f), Size(w * 0.1f, h * 0.16f), alpha = er * g)
            drawPath(starPath(Offset(x, ground - h * 0.45f), w * 0.015f), SalC.Gold, alpha = er * g)
        }
        tagAt(a.tag("extrareward", 0, 0), Offset(w * 0.5f, h * 0.12f), er * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("extrareward", 0, 1), Offset(w * 0.5f, h * 0.92f), er * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tt > 0f) {
        val p = a.b("threetoten")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.75f * tt)
        val n = when {
            p < 0.15f -> 1
            p < 0.5f -> 1 + ((p - 0.15f) / 0.35f * 2f).toInt().coerceAtMost(2)
            else -> 3 + ((p - 0.5f) / 0.35f * 7f).toInt().coerceAtMost(7)
        }
        fqMeter(Offset(w * 0.5f, h * 0.5f), w * 0.8f, h * 0.08f, 10, n / 10f, SalC.Gold, SalC.Cell, tt)
        tagAt(a.tag("threetoten", 0, 0), Offset(w * 0.72f, h * 0.3f), tt * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("threetoten", 0, 1), Offset(w * 0.3f, h * 0.7f), tt * seg(p, 0.75f, 0.9f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fp > 0f) {
        val p = a.b("followerpraise")
        val imam = Offset(w * 0.18f, floor + h * 0.14f)
        takbir80(imam, h * 0.26f, SalC.Person, fp, 0f)
        voice12(imam + Offset(w * 0.06f, -h * 0.22f), w * 0.07f, t, SalC.Call, fp * seg(p, 0.05f, 0.2f), 1f)
        row74(floor + h * 0.32f, w * 0.4f, w * 0.88f, 5, h * 0.24f, SalC.Person, fp)
        for (k in 0 until 5) voice12(Offset(w * (0.4f + 0.12f * k) - w * 0.03f, floor + h * 0.1f), w * 0.03f, t + k, SalC.Gold, fp * seg(p, 0.4f + 0.04f * k, 0.5f + 0.04f * k), -1f)
        tagAt(a.tag("followerpraise"), Offset(w * 0.6f, h * 0.18f), fp * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 14. sal-makruh: what is disliked in prayer ═════════════

/**
 * Beats: gazedown (an eye raised to the sky is struck out; it comes down to the place of prostration: «رَفْعُ الْبَصَرِ إِلَى السَّمَاءِ», «إِلَى
 * مَوْضِعِ سُجُودِهِ»), fidgets (a fidgeting hand, forearms laid flat in prostration, a hand on the hip, each struck out: «الْعَبَثُ», «وَافْتِرَاشُ
 * الذِّرَاعَيْنِ فِي السُّجُودِ», «وَوَضْعُ الْيَدِ عَلَى الْخَاصِرَةِ»), distractions (one about to pray with a meal waiting behind him; his heart dims:
 * «بِحَضْرَةِ طَعَامٍ يَشْتَهِيهِ», «لِأَنَّ ذَلِكَ يَمْنَعُ الْخُشُوعَ»), ornatewall (an ornamented wall pulls the gaze in loops: «الْجُدْرَانِ وَالنُّقُوشِ
 * وَالْكِتَابَاتِ»), presentheart (one at prayer, still, his heart lit: «بِحُضُورِ الْقَلْبِ وَالْخُشُوعِ»).
 */
internal fun DrawScope.dislikedactsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "gazedown" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "fidgets" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "distractions" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "ornatewall" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "presentheart" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { dislikedActsDraw(a) }
}

private fun DrawScope.dislikedActsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val gd = fqIn(a, "gazedown", "fidgets")
    val fg = fqIn(a, "fidgets", "distractions")
    val ds = fqIn(a, "distractions", "ornatewall")
    val ow = fqIn(a, "ornatewall", "presentheart")
    val ph = fqIn(a, "presentheart")
    if (gd > 0f) {
        val p = a.b("gazedown")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.6f * gd)
        val eye = Offset(w * 0.5f, h * 0.45f)
        eye73(eye, w * 0.16f, SalC.Person, gd)
        val up = 1f - seg(p, 0.4f, 0.6f)
        drawLine(SalC.Rim, eye, eye + Offset(0f, -h * 0.3f), 2f * dp, alpha = gd * up, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        cross14(eye + Offset(0f, -h * 0.32f), w * 0.035f, seg(p, 0.2f, 0.35f) * up)
        val down = seg(p, 0.55f, 0.75f)
        drawLine(SalC.Gold, eye, eye + Offset(-w * 0.1f, h * 0.32f) * down, 2f * dp, alpha = gd)
        fqRug(Offset(w * 0.4f, h * 0.84f), w * 0.1f, h * 0.06f, Color(0xFF6A2A34), SalC.Gold, gd * down)
        tagAt(a.tag("gazedown", 0, 0), Offset(w * 0.78f, h * 0.15f), gd * seg(p, 0.1f, 0.3f) * up, back = Fq.Back, ink = ArtInk.Ember)
        tagAt(a.tag("gazedown", 0, 1), Offset(w * 0.75f, h * 0.84f), gd * down, back = Fq.Back, ink = ArtInk.Jade)
    }
    if (fg > 0f) {
        val p = a.b("fidgets")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val foot = Offset(w * (0.8f - 0.3f * k), floor + h * 0.3f)
            when (k) {
                0 -> {
                    pray12(foot, h * 0.3f, SalC.Person, fg * g)
                    for (j in 0 until 3) drawArc(SalC.Rim, -30f + 20f * sin(t * 8f), 60f, false, Offset(foot.x + h * 0.02f + j * h * 0.015f, foot.y - h * 0.22f), Size(h * 0.06f, h * 0.06f), alpha = fg * g, style = Stroke(1.5f * dp))
                }
                1 -> {
                    bow12(foot, h * 0.3f, SalC.Person, fg * g, 1f)
                    drawLine(SalC.Person, foot + Offset(-h * 0.25f, 0f), foot + Offset(-h * 0.12f, 0f), h * 0.02f, StrokeCap.Round, alpha = fg * g)
                }
                else -> {
                    figure(foot, h * 0.3f, SalC.Person, fg * g)
                    drawLine(SalC.Person, foot + Offset(-h * 0.05f, -h * 0.22f), foot + Offset(-h * 0.07f, -h * 0.14f), h * 0.02f, StrokeCap.Round, alpha = fg * g)
                }
            }
            cross14(Offset(foot.x, floor - h * 0.06f), w * 0.03f, seg(p, 0.15f + 0.28f * k, 0.3f + 0.28f * k))
            tagAt(a.tag("fidgets", 0, k), Offset(foot.x, h * (0.14f + 0.1f * (k % 2))), fg * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (ds > 0f) {
        val p = a.b("distractions")
        val foot = Offset(w * 0.4f, floor + h * 0.3f)
        pray12(foot, h * 0.34f, SalC.Person, ds)
        dateBowl72(Offset(w * 0.75f, floor + h * 0.3f), w * 0.16f, ds * seg(p, 0.05f, 0.2f))
        cup12(Offset(w * 0.88f, floor + h * 0.3f), w * 0.05f, SalC.Rim, ds * seg(p, 0.05f, 0.2f), Fq.Water)
        for (k in 0 until 3) cloud38(foot + Offset(w * (0.06f + 0.04f * k), -h * (0.36f + 0.03f * k)), w * 0.02f, Color(0xFF9A92A8), ds * seg(p, 0.2f + 0.08f * k, 0.3f + 0.08f * k))
        val dim = seg(p, 0.45f, 0.65f)
        heart12(foot + Offset(-h * 0.02f, -h * 0.22f), w * 0.03f, lerpColor(SalC.Gold, Color(0xFF5A5468), dim), ds, fill = true)
        tagAt(a.tag("distractions", 0, 0), Offset(w * 0.78f, h * 0.2f), ds * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("distractions", 0, 1), Offset(w * 0.3f, h * 0.12f), ds * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (ow > 0f) {
        val p = a.b("ornatewall")
        drawRect(Color(0xFF3A3248), Offset(w * 0.1f, h * 0.15f), Size(w * 0.8f, h * 0.45f), alpha = ow)
        for (r in 0 until 3) for (c in 0 until 8) {
            val cc = Offset(w * (0.15f + 0.1f * c), h * (0.22f + 0.13f * r))
            drawPath(starPath(cc, w * 0.025f), SalC.Gold, alpha = ow * 0.6f, style = Stroke(1.5f * dp))
        }
        val eye = Offset(w * 0.5f, h * 0.82f)
        eye73(eye, w * 0.1f, SalC.Person, ow)
        for (k in 0 until 3) {
            val u = fract(t * 0.4f + k / 3f)
            val target = Offset(w * (0.2f + 0.6f * fract(k * 0.618f + u * 0.3f)), h * (0.2f + 0.3f * fract(k * 0.381f + u)))
            drawLine(SalC.Rim, eye, target, 1f * dp, alpha = ow * (1f - u) * 0.6f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp)))
        }
        tagAt(a.tag("ornatewall"), Offset(w * 0.5f, h * 0.68f), ow * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ph > 0f) {
        val p = a.b("presentheart")
        val foot = Offset(w * 0.5f, floor + h * 0.3f)
        pray12(foot, h * 0.4f, SalC.Person, ph)
        val heart = foot + Offset(-h * 0.03f, -h * 0.27f)
        softGlow(heart, w * 0.1f, SalC.Gold, ph * seg(p, 0.1f, 0.4f) * (0.8f + 0.2f * sin(t * 1.5f)))
        heart12(heart, w * 0.04f, SalC.Gold, ph * seg(p, 0.1f, 0.4f), fill = true)
        tagAt(a.tag("presentheart"), Offset(w * 0.5f, h * 0.12f), ph * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 15. sal-sahw: the prostration of forgetfulness ═════════════

/**
 * Beats: threeaims (dark smoke pushed down, a crack in an arch filled, a light rising: «إِرْغَامًا لِلشَّيْطَانِ», «وَجَبْرًا لِلنُّقْصَانِ», «وَإِرْضَاءً
 * لِلرَّحْمَنِ»), threecauses (a row of rakʿas: one too many, one missing, one in doubt: «الزِّيَادَةُ», «أَوِ النَّقْصُ», «أَوِ الشَّكُّ»), buildonless
 * (three or four? the doubtful fourth fades and the count builds on three: «بَنَى عَلَى الْأَقَلِّ»), returnifnotup (one rising from the first sitting,
 * not yet upright, goes back down: «رَجَعَ إِلَيْهِ مَا لَمْ يَسْتَتِمَّ قَائِمًا»), alertimam (the one in front stands for an extra rakʿa and the rows
 * behind him glorify to warn him: «فَيُسَبِّحُ الرِّجَالُ»).
 */
internal fun DrawScope.forgetfulnessScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threeaims" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sahwcauses" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "buildonless" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "returnifnotup" to Shot(Offset(w * 0.5f, h * 0.7f), 1.1f),
        "alertimam" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { forgetfulnessDraw(a) }
}

private fun DrawScope.forgetfulnessDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val ta = fqIn(a, "threeaims", "sahwcauses")
    val tc = fqIn(a, "sahwcauses", "buildonless")
    val bl = fqIn(a, "buildonless", "returnifnotup")
    val rn = fqIn(a, "returnifnotup", "alertimam")
    val ai = fqIn(a, "alertimam")
    val board = maxOf(ta, tc, bl)
    if (board > 0f) drawRect(SalC.Top, Offset.Zero, size, alpha = 0.75f * board)
    if (ta > 0f) {
        val p = a.b("threeaims")
        val g0 = seg(p, 0.05f, 0.25f)
        smokeDown82(Offset(w * 0.8f, h * 0.35f), w * 0.05f, seg(p, 0.1f, 0.35f), ta * g0, t)
        tagAt(a.tag("threeaims", 0, 0), Offset(w * 0.8f, h * 0.8f), ta * g0, back = Fq.Back, ink = ArtInk.Ivory)
        val g1 = seg(p, 0.35f, 0.55f)
        rakah74(Offset(w * 0.5f, h * 0.6f), h * 0.3f, 1f, ta * g1)
        drawLine(Color(0xFF2A2430), Offset(w * 0.5f, h * 0.3f), Offset(w * 0.49f, h * 0.45f), 3f * dp, alpha = ta * g1 * (1f - seg(p, 0.45f, 0.6f)))
        tagAt(a.tag("threeaims", 0, 1), Offset(w * 0.5f, h * 0.8f), ta * g1, back = Fq.Back, ink = ArtInk.Ivory)
        val g2 = seg(p, 0.65f, 0.85f)
        softGlow(Offset(w * 0.2f, mix(h * 0.6f, h * 0.3f, g2)), w * 0.06f, SalC.Gold, ta * g2)
        tagAt(a.tag("threeaims", 0, 2), Offset(w * 0.2f, h * 0.8f), ta * g2, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tc > 0f) {
        val p = a.b("sahwcauses")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val cx = w * (0.8f - 0.3f * k)
            for (j in 0 until 4) {
                val x = cx + w * (0.075f - 0.05f * j)
                val y = h * 0.55f
                when {
                    k == 0 -> rakah74(Offset(x, y), h * 0.12f, 1f, tc * g)
                    k == 1 && j == 2 -> drawPath(archPath26(x, y - h * 0.12f, y, h * 0.04f), SalC.Rim, alpha = tc * g * 0.4f, style = Stroke(1f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
                    k == 2 && j == 3 -> drawPath(archPath26(x, y - h * 0.12f, y, h * 0.04f), SalC.Gold, alpha = tc * g * (0.4f + 0.4f * sin(t * 4f)), style = Stroke(2f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
                    else -> rakah74(Offset(x, y), h * 0.12f, 1f, tc * g)
                }
            }
            if (k == 0) rakah74(Offset(cx - w * 0.125f, h * 0.55f), h * 0.12f, 1f, tc * g, true)
            tagAt(a.tag("sahwcauses", 0, k), Offset(cx, h * 0.72f), tc * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (bl > 0f) {
        val p = a.b("buildonless")
        for (j in 0 until 4) {
            val fourth = j == 3
            val x = w * (0.7f - 0.12f * j)
            rakah74(Offset(x, h * 0.55f), h * 0.2f, if (fourth) 0f else 1f, bl * (if (fourth) 1f - seg(p, 0.3f, 0.5f) else 1f))
        }
        val add = seg(p, 0.6f, 0.75f)
        rakah74(Offset(w * 0.34f, h * 0.55f), h * 0.2f, add, bl * add, true)
        tagAt(a.tag("buildonless"), Offset(w * 0.5f, h * 0.2f), bl * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (rn > 0f) {
        val p = a.b("returnifnotup")
        val seat = Offset(w * 0.55f, floor + h * 0.3f)
        val up = bump(seg(p, 0.1f, 0.7f))
        if (up < 0.5f) kneel80(seat, h * 0.45f, SalC.Person, rn)
        else bow12(seat, h * 0.45f, SalC.Person, rn, 0.3f)
        drawLine(SalC.Rim, Offset(w * 0.2f, seat.y - h * 0.45f * 0.92f), Offset(w * 0.85f, seat.y - h * 0.45f * 0.92f), 1.5f * dp, alpha = rn * 0.6f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp)))
        tagAt(a.tag("returnifnotup"), Offset(w * 0.4f, h * 0.12f), rn * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ai > 0f) {
        val p = a.b("alertimam")
        takbir80(Offset(w * 0.18f, floor + h * 0.14f), h * 0.26f, SalC.Person, ai, 0f)
        row74(floor + h * 0.32f, w * 0.4f, w * 0.88f, 5, h * 0.24f, SalC.Person, ai)
        for (k in 0 until 5) voice12(Offset(w * (0.4f + 0.12f * k) - w * 0.03f, floor + h * 0.1f), w * 0.03f, t + k, SalC.Call, ai * seg(p, 0.3f + 0.04f * k, 0.4f + 0.04f * k), -1f)
        tagAt(a.tag("alertimam"), Offset(w * 0.6f, h * 0.18f), ai * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 16. sal-adhkar: remembrance after the prayer ═════════════

/**
 * Beats: istighfarthree (three soft lights, then a wide calm glow of peace: «اسْتَغْفَرَ اللَّهَ ثَلَاثًا», «اللَّهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ
 * السَّلَامُ»), thirtythree (three strings of beads fill in turn, and one more completes the hundred: «ثَلَاثًا وَثَلَاثِينَ», «تَمَامَ الْمِئَةِ»),
 * kursiyshield (an open book under a dome of light; at dawn and sunset three marks each: «آيَةَ الْكُرْسِيِّ وَالْمُعَوِّذَاتِ», «ثَلَاثًا بَعْدَ الْفَجْرِ
 * وَالْمَغْرِبِ»), tentimes (ten lights: «عَشْرَ مَرَّاتٍ»), imamturns (the one in front turns from the qibla to face the rows: «انْصَرَفَ إِلَى
 * النَّاسِ وَقَابَلَهُمْ بِوَجْهِهِ»).
 */
internal fun DrawScope.postprayerdhikrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "istighfarthree" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "thirtythree" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "kursiyshield" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "tentimes" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "imamturns" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { postPrayerDhikrDraw(a) }
}

private fun DrawScope.postPrayerDhikrDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t, lamps = 0.7f)
    val it3 = fqIn(a, "istighfarthree", "thirtythree")
    val tt = fqIn(a, "thirtythree", "kursiyshield")
    val ks = fqIn(a, "kursiyshield", "tentimes")
    val tn = fqIn(a, "tentimes", "imamturns")
    val im = fqIn(a, "imamturns")
    val board = maxOf(it3, tt, ks, tn)
    if (board > 0f) drawRect(SalC.Top, Offset.Zero, size, alpha = 0.7f * board)
    if (it3 > 0f) {
        val p = a.b("istighfarthree")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.12f * k, 0.15f + 0.12f * k)
            softGlow(Offset(w * (0.62f - 0.12f * k), h * 0.4f), w * 0.04f, SalC.Gold, it3 * g)
        }
        tagAt(a.tag("istighfarthree", 0, 0), Offset(w * 0.5f, h * 0.15f), it3 * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        val peace = seg(p, 0.5f, 0.8f)
        softGlow(Offset(w * 0.5f, h * 0.65f), w * 0.3f * (0.5f + 0.5f * peace), Color(0xFFDDEEFF), 0.5f * it3 * peace)
        tagAt(a.tag("istighfarthree", 0, 1), Offset(w * 0.5f, h * 0.84f), it3 * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tt > 0f) {
        val p = a.b("thirtythree")
        for (r in 0 until 3) {
            val c = Offset(w * (0.78f - 0.28f * r), h * 0.45f)
            val fill = seg(p, 0.05f + 0.25f * r, 0.3f + 0.25f * r)
            for (k in 0 until 33) {
                val ang = -PI.toFloat() / 2f + 2f * PI.toFloat() * k / 33f
                val on = fill * 33f > k
                drawCircle(if (on) SalC.Gold else SalC.Rim, w * 0.007f, c + Offset(cos(ang), sin(ang)) * (w * 0.1f), alpha = tt * (if (on) 1f else 0.35f))
            }
        }
        tagAt(a.tag("thirtythree", 0, 0), Offset(w * 0.5f, h * 0.12f), tt * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        val done = seg(p, 0.85f, 0.95f)
        softGlow(Offset(w * 0.5f, h * 0.8f), w * 0.05f, SalC.Gold, tt * done)
        tagAt(a.tag("thirtythree", 0, 1), Offset(w * 0.5f, h * 0.9f), tt * done, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ks > 0f) {
        val p = a.b("kursiyshield")
        val c = Offset(w * 0.5f, h * 0.55f)
        openBook21(c, w * 0.26f, Color(0xFFF2E8CE), Color(0xFFB8A888), ks, glow = 0.5f)
        guardDome12(c + Offset(0f, h * 0.08f), w * 0.2f, seg(p, 0.1f, 0.4f), t, ks)
        tagAt(a.tag("kursiyshield", 0, 0), Offset(w * 0.5f, h * 0.12f), ks * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(p, 0.55f, 0.75f)
        sun12(Offset(w * 0.15f, h * 0.35f), w * 0.025f, ks * g)
        moon12(Offset(w * 0.85f, h * 0.35f), w * 0.022f, ks * g)
        for (k in 0 until 3) {
            drawCircle(SalC.Gold, w * 0.008f, Offset(w * (0.11f + 0.04f * k), h * 0.45f), alpha = ks * g)
            drawCircle(SalC.Gold, w * 0.008f, Offset(w * (0.81f + 0.04f * k), h * 0.45f), alpha = ks * g)
        }
        tagAt(a.tag("kursiyshield", 0, 1), Offset(w * 0.5f, h * 0.9f), ks * g, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tn > 0f) {
        val p = a.b("tentimes")
        for (k in 0 until 10) {
            val g = seg(p, 0.05f + 0.07f * k, 0.12f + 0.07f * k)
            val c = Offset(w * (0.88f - 0.084f * k), h * 0.5f)
            softGlow(c, w * 0.03f, SalC.Gold, tn * g)
            drawCircle(SalC.Gold, w * 0.012f, c, alpha = tn * g)
        }
        tagAt(a.tag("tentimes"), Offset(w * 0.5f, h * 0.7f), tn * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (im > 0f) {
        val p = a.b("imamturns")
        val turn = seg(p, 0.2f, 0.45f)
        val seat = Offset(w * 0.2f, floor + h * 0.16f)
        sit12(seat, h * 0.24f, SalC.Person, im * (1f - turn), -1f)
        sit12(seat, h * 0.24f, SalC.Person, im * turn, 1f)
        for (k in 0 until 5) sit12(Offset(w * (0.42f + 0.11f * k), floor + h * 0.32f), h * 0.22f, SalC.Person, im, -1f)
        tagAt(a.tag("imamturns"), Offset(w * 0.55f, h * 0.15f), im * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 17. sal-rawatib: the regular sunnahs ═════════════

/**
 * Beats: twelvearches (twelve small arches appear: «اثْنَتَيْ عَشْرَةَ رَكْعَةً», «وَتُسَمَّى الرَّوَاتِبَ»), aroundprayers (along the day, the arches
 * settle before and after the five prayers: «أَرْبَعٌ قَبْلَ الظُّهْرِ», «وَثِنْتَانِ بَعْدَهُ», «وَثِنْتَانِ بَعْدَ الْمَغْرِبِ», «وَثِنْتَانِ بَعْدَ
 * الْعِشَاءِ», «وَثِنْتَانِ قَبْلَ صَلَاةِ الْفَجْرِ»), travelkeeps (on a journey, only the dawn sunnah and the witr stay lit: «وَيُحَافِظُ عَلَى سُنَّةِ
 * الْفَجْرِ وَالْوِتْرِ»), houseinjannah (the twelve arches gather into a house of light in a garden: «بُنِيَ لَهُ بِهِنَّ بَيْتٌ فِي الْجَنَّةِ»).
 */
internal fun DrawScope.twelverakatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twelvearches" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "aroundprayers" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "travelkeeps" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "houseinjannah" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { twelveRakatDraw(a) }
}

private fun DrawScope.twelveRakatDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    nightSky(t, SalC.Top, SalC.Low, h, starAlpha = 0.5f)
    val ta = fqIn(a, "twelvearches", "aroundprayers")
    val ap = fqIn(a, "aroundprayers", "travelkeeps")
    val tk = fqIn(a, "travelkeeps", "houseinjannah")
    val hj = fqIn(a, "houseinjannah")
    if (ta > 0f) {
        val p = a.b("twelvearches")
        for (k in 0 until 12) rakah74(Offset(w * (0.9f - 0.07f * k), h * 0.55f), h * 0.16f, seg(p, 0.04f * k, 0.04f * k + 0.1f), ta * seg(p, 0.04f * k, 0.04f * k + 0.05f))
        tagAt(a.tag("twelvearches", 0, 0), Offset(w * 0.5f, h * 0.2f), ta * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("twelvearches", 0, 1), Offset(w * 0.5f, h * 0.78f), ta * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    // a day from dawn (right) to night (left): fajr, zuhr, asr, maghrib, isha
    val fard = listOf(0.86f, 0.64f, 0.5f, 0.32f, 0.16f)
    val placeOf = listOf(Pair(1, -4), Pair(1, 2), Pair(3, 2), Pair(4, 2), Pair(0, -2))
    if (ap > 0f || tk > 0f) {
        val f = maxOf(ap, tk)
        val y = h * 0.55f
        drawLine(SalC.Rim, Offset(w * 0.95f, y), Offset(w * 0.05f, y), 2f * dp, alpha = f * 0.6f)
        for (k in 0 until 5) drawCircle(SalC.Gold, w * 0.018f, Offset(w * fard[k], y), alpha = f)
    }
    if (ap > 0f) {
        val p = a.b("aroundprayers")
        for ((i, pl) in placeOf.withIndex()) {
            val g = seg(p, 0.05f + 0.18f * i, 0.2f + 0.18f * i)
            val n = kotlin.math.abs(pl.second)
            val before = pl.second < 0
            for (j in 0 until n) {
                val x = w * fard[pl.first] + (if (before) 1f else -1f) * w * (0.035f + 0.025f * j)
                rakah74(Offset(x, h * 0.53f), h * 0.08f, 1f, ap * g)
            }
            tagAt(a.tag("aroundprayers", 0, i), Offset(w * fard[pl.first], h * (if (i % 2 == 0) 0.32f else 0.75f)), ap * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (tk > 0f) {
        val p = a.b("travelkeeps")
        camel26(Offset(w * 0.5f, h * 0.92f), h * 0.22f, Color(0xFFC8A880), tk, t, walk = t * 3f)
        for ((i, pl) in placeOf.withIndex()) {
            val keep = i == 4
            val n = kotlin.math.abs(pl.second)
            val before = pl.second < 0
            for (j in 0 until n) {
                val x = w * fard[pl.first] + (if (before) 1f else -1f) * w * (0.035f + 0.025f * j)
                rakah74(Offset(x, h * 0.53f), h * 0.08f, if (keep) 1f else 0f, tk * (if (keep) 1f else 0.25f))
            }
        }
        rakah74(Offset(w * 0.08f, h * 0.53f), h * 0.1f, 1f, tk * seg(p, 0.3f, 0.45f), true)
        tagAt(a.tag("travelkeeps"), Offset(w * 0.5f, h * 0.2f), tk * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (hj > 0f) {
        val p = a.b("houseinjannah")
        val ground = h * 0.8f
        drawRect(Color(0xFF2E5A40), Offset(0f, ground), Size(w, h - ground), alpha = hj)
        val gather = easeInOut(seg(p, 0.05f, 0.5f))
        for (k in 0 until 12) {
            val from = Offset(w * (0.9f - 0.07f * k), h * 0.3f)
            val to = Offset(w * (0.38f + 0.08f * (k % 4)), ground - h * (0.06f + 0.12f * (k / 4)))
            rakah74(from + (to - from) * gather, h * 0.1f, 1f, hj * (1f - seg(p, 0.55f, 0.7f)))
        }
        val lit = seg(p, 0.5f, 0.7f)
        house12(Offset(w * 0.5f, ground), w * 0.34f, Color(0xFFF2E6C0), SalC.Gold, hj * lit, lit = 1f)
        softGlow(Offset(w * 0.5f, ground - h * 0.2f), w * 0.25f, SalC.Gold, 0.5f * hj * lit)
        palm12(Offset(w * 0.15f, ground), h * 0.3f, Fq.SandDark, Color(0xFF7AD0A0), hj * lit, t)
        palm12(Offset(w * 0.85f, ground), h * 0.26f, Fq.SandDark, Color(0xFF7AD0A0), hj * lit, t)
        tagAt(a.tag("houseinjannah"), Offset(w * 0.5f, h * 0.12f), hj * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Jade)
    }
}

// ═════════════ 18. sal-witr: the witr ═════════════

/**
 * Beats: strongestnafl (among silver arches one gold arch shines: «آكَدُ التَّطَوُّعِ», «فَلَا يَنْبَغِي تَرْكُهُ»), witrwindow (the night from the
 * ʿishāʾ to the dawn; a mark early for one who fears he will not wake, a mark late for one who trusts he will: «مِنْ بَعْدِ صَلَاةِ الْعِشَاءِ إِلَى
 * طُلُوعِ الْفَجْرِ», «أَوْتَرَ أَوَّلَهُ», «أَوْتَرَ آخِرَهُ»), oneto13 (one arch alone, then eleven or thirteen in pairs closed by one: «وَأَقَلُّهُ
 * رَكْعَةٌ», «وَأَكْثَرُهُ إِحْدَى عَشْرَةَ أَوْ ثَلَاثَ عَشْرَةَ رَكْعَةً»), ninejoined (nine joined arches with a short sitting after the eighth: «يَسْرُدُ
 * تِسْعًا», «فَيَجْلِسُ بَعْدَ الثَّامِنَةِ»), lastnightwitnessed (in the last part of the night pale lights come down to the reading: «فَإِنَّ قِرَاءَةَ
 * آخِرِ اللَّيْلِ مَحْضُورَةٌ»).
 */
internal fun DrawScope.witrnightScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "strongestnafl" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "witrwindow" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "oneto13" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "ninejoined" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "lastnightwitnessed" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { witrNightDraw(a) }
}

private fun DrawScope.witrNightDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    nightSky(t, Color(0xFF0A1028), Color(0xFF2A2A50), h, starAlpha = 0.9f)
    val sn = fqIn(a, "strongestnafl", "witrwindow")
    val ww = fqIn(a, "witrwindow", "oneto13")
    val ot = fqIn(a, "oneto13", "ninejoined")
    val nj = fqIn(a, "ninejoined", "lastnightwitnessed")
    val ln = fqIn(a, "lastnightwitnessed")
    val y = h * 0.62f
    if (sn > 0f) {
        val p = a.b("strongestnafl")
        for (k in 0 until 7) {
            val gold = k == 3
            val x = w * (0.86f - 0.12f * k)
            rakah74(Offset(x, y), h * (if (gold) 0.3f else 0.18f), if (gold) seg(p, 0.2f, 0.4f) else 0.3f, sn * seg(p, 0.03f * k, 0.03f * k + 0.1f), gold)
            if (gold) softGlow(Offset(x, y - h * 0.15f), w * 0.1f, SalC.Gold, sn * seg(p, 0.3f, 0.5f))
        }
        tagAt(a.tag("strongestnafl", 0, 0), Offset(w * 0.5f, h * 0.15f), sn * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("strongestnafl", 0, 1), Offset(w * 0.5f, h * 0.84f), sn * seg(p, 0.65f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ww > 0f) {
        val p = a.b("witrwindow")
        val x0 = w * 0.88f
        val x1 = w * 0.12f
        val by = h * 0.5f
        val run = easeOut(seg(p, 0.05f, 0.3f))
        drawLine(SalC.Cell, Offset(x0, by), Offset(mix(x0, x1, run), by), h * 0.06f, StrokeCap.Round, alpha = ww)
        crescent12(Offset(x0, by - h * 0.12f), w * 0.025f, SalC.Gold, SalC.Top, ww)
        drawRect(Color(0xFFF2D8B0), Offset(x1 - w * 0.04f, by - h * 0.012f), Size(w * 0.08f, h * 0.024f), alpha = ww * run)
        tagAt(a.tag("witrwindow", 0, 0), Offset(w * 0.5f, h * 0.15f), ww * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        val e = seg(p, 0.4f, 0.55f)
        rakah74(Offset(mix(x0, x1, 0.2f), by - h * 0.04f), h * 0.12f, 1f, ww * e, true)
        tagAt(a.tag("witrwindow", 0, 1), Offset(mix(x0, x1, 0.2f), h * 0.72f), ww * e, back = Fq.Back, ink = ArtInk.Gold)
        val l = seg(p, 0.65f, 0.8f)
        rakah74(Offset(mix(x0, x1, 0.85f), by - h * 0.04f), h * 0.12f, 1f, ww * l, true)
        tagAt(a.tag("witrwindow", 0, 2), Offset(mix(x0, x1, 0.85f), h * 0.72f), ww * l, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ot > 0f) {
        val p = a.b("oneto13")
        val g0 = seg(p, 0.05f, 0.25f) * (1f - seg(p, 0.4f, 0.5f))
        rakah74(Offset(w * 0.5f, y), h * 0.3f, 1f, ot * g0, true)
        tagAt(a.tag("oneto13", 0, 0), Offset(w * 0.5f, h * 0.15f), ot * g0, back = Fq.Back, ink = ArtInk.Gold)
        val g1 = seg(p, 0.45f, 0.65f)
        for (k in 0 until 13) {
            val odd = k == 12
            val x = w * 0.92f - (k / 2) * w * 0.135f - (k % 2) * w * 0.05f
            rakah74(Offset(x, y), h * 0.16f, 1f, ot * g1 * (if (k >= 11 && !odd) 0.5f else 1f), odd)
        }
        tagAt(a.tag("oneto13", 0, 1), Offset(w * 0.5f, h * 0.15f), ot * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (nj > 0f) {
        val p = a.b("ninejoined")
        for (k in 0 until 9) {
            val x = w * (0.9f - 0.095f * k)
            rakah74(Offset(x, y), h * 0.18f, seg(p, 0.03f * k, 0.03f * k + 0.1f), nj, k == 8)
        }
        drawLine(SalC.Gold, Offset(w * 0.9f, y + h * 0.03f), Offset(w * 0.14f, y + h * 0.03f), 2f * dp, alpha = nj * seg(p, 0.3f, 0.45f))
        tagAt(a.tag("ninejoined", 0, 0), Offset(w * 0.5f, h * 0.15f), nj * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        val seatAt = Offset(w * (0.9f - 0.095f * 7.5f), y + h * 0.1f)
        drawCircle(SalC.Gold, w * 0.015f, seatAt, alpha = nj * seg(p, 0.55f, 0.7f))
        softGlow(seatAt, w * 0.04f, SalC.Gold, nj * seg(p, 0.55f, 0.7f))
        tagAt(a.tag("ninejoined", 0, 1), Offset(seatAt.x, h * 0.86f), nj * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ln > 0f) {
        val p = a.b("lastnightwitnessed")
        drawRect(Color(0xFF3A3A6A), Offset(0f, h * 0.85f), Size(w, h * 0.15f), alpha = ln * 0.4f)
        openBook21(Offset(w * 0.5f, h * 0.75f), w * 0.24f, Color(0xFFF2E8CE), Color(0xFFB8A888), ln, glow = 0.6f)
        for (k in 0 until 4) {
            val u = seg(p, 0.1f + 0.12f * k, 0.5f + 0.12f * k)
            angelLight26(Offset(w * (0.2f + 0.2f * k), mix(-h * 0.05f, h * (0.35f + 0.05f * (k % 2)), easeOut(u))), w * 0.012f, t, ln * seg(u, 0f, 0.2f))
        }
        tagAt(a.tag("lastnightwitnessed"), Offset(w * 0.5f, h * 0.12f), ln * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
