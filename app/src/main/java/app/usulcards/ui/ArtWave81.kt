package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 81 (2026-11-03): v1.3 fiqh, prayer 2: the recitation, bowing and rising, prostration on seven limbs, the sitting between the
 * two prostrations, the tashahhud and the salam, and the pillars of prayer. One faceless figure facing left (the qibla) moves through
 * the postures; lights mark the limbs and the words.
 */

/** Where the seven limbs touch the ground for [bow12] at deep = 1: forehead, two hands, two knees, two sets of toes (pairs drawn as one). */
internal fun sevenPoints81(foot: Offset, tall: Float): List<Offset> {
    val hip = foot + Offset(tall * 0.12f, -tall * 0.12f)
    val ang = PI.toFloat() / 2f + PI.toFloat() / 4f
    val dir = Offset(-sin(ang), -cos(ang))
    val shoulder = hip + dir * (tall * 0.31f)
    val head = shoulder + dir * (tall * 0.14f)
    val forehead = Offset(head.x - tall * 0.05f, foot.y)
    val hand = Offset(head.x + tall * 0.06f, foot.y)
    val knee = Offset(foot.x - tall * 0.12f, foot.y - tall * 0.02f)
    val toe = foot + Offset(tall * 0.1f, 0f)
    return listOf(forehead, hand, knee, toe)
}

// ═════════════ 7. sal-fatiha: the recitation ═════════════

/**
 * Beats: fatihakey (an open book of seven lines lights, and only then does the arch of the prayer light: «فَلَا صَلَاةَ لِمَنْ لَمْ يَقْرَأْ بِهَا»),
 * aminloudlow (a word said aloud under the night sky, softly under the day's: «آمِينَ», «جَهْرًا فِي الْجَهْرِيَّةِ وَسِرًّا فِي السِّرِّيَّةِ»),
 * midlength (three bars of reading, the middle one lit: «مِنْ أَوْسَاطِ الْمُفَصَّلِ»), longshort (the long bar at dawn, the short bar at sunset:
 * «وَفِي الْفَجْرِ مِنْ طِوَالِهِ», «وَفِي الْمَغْرِبِ مِنْ قِصَارِهِ»), thirdfourth (four rakʿas; the last two carry only the small book of the Fātiḥa:
 * «يَقْرَأُ الْفَاتِحَةَ فَقَطْ»).
 */
internal fun DrawScope.recitationScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fatihakey" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "aminloudlow" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "midlength" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "longshort" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "thirdfourth" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { recitationDraw(a) }
}

private fun DrawScope.recitationDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    nightSky(t, SalC.Top, SalC.Low, h, starAlpha = 0.5f)
    val fk = fqIn(a, "fatihakey", "aminloudlow")
    val al = fqIn(a, "aminloudlow", "midlength")
    val ml = fqIn(a, "midlength", "longshort")
    val ls = fqIn(a, "longshort", "thirdfourth")
    val tf = fqIn(a, "thirdfourth")
    if (fk > 0f) {
        val p = a.b("fatihakey")
        val book = Offset(w * 0.68f, h * 0.55f)
        openBook21(book, w * 0.36f, Color(0xFFF2E8CE), Color(0xFFB8A888), fk, glow = seg(p, 0.2f, 0.5f))
        for (k in 0 until 7) drawLine(SalC.Gold, book + Offset(w * (0.03f + (k % 2) * 0.0f) - w * 0.15f * (k / 4), -h * 0.06f + (k % 4) * h * 0.035f), book + Offset(w * 0.12f - w * 0.15f * (k / 4), -h * 0.06f + (k % 4) * h * 0.035f), 2f * dp, StrokeCap.Round, alpha = fk * seg(p, 0.1f + 0.05f * k, 0.18f + 0.05f * k))
        val arch = seg(p, 0.5f, 0.7f)
        drawPath(archPath26(w * 0.22f, h * 0.25f, h * 0.8f, w * 0.1f), SalC.Gold, alpha = fk * (0.2f + 0.8f * arch), style = Stroke(3f * dp))
        softGlow(Offset(w * 0.22f, h * 0.5f), w * 0.12f, SalC.Gold, 0.5f * fk * arch)
        tagAt(a.tag("fatihakey"), Offset(w * 0.5f, h * 0.12f), fk * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (al > 0f) {
        val p = a.b("aminloudlow")
        tagAt(a.tag("aminloudlow", 0, 0), Offset(w * 0.5f, h * 0.12f), al * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(p, 0.35f, 0.55f)
        moon12(Offset(w * 0.78f, h * 0.32f), w * 0.03f, al * g)
        voice12(Offset(w * 0.72f, h * 0.55f), w * 0.14f, t, SalC.Gold, al * g, 1f)
        sun12(Offset(w * 0.28f, h * 0.32f), w * 0.03f, al * g)
        voice12(Offset(w * 0.24f, h * 0.55f), w * 0.05f, t, SalC.Rim, al * g, 1f)
        tagAt(a.tag("aminloudlow", 0, 1), Offset(w * 0.5f, h * 0.84f), al * g, back = Fq.Back, ink = ArtInk.Ivory)
    }
    val bars = { alpha: Float, lit: Int, prog: Float ->
        val lens = listOf(0.7f, 0.45f, 0.2f)
        for (k in 0 until 3) {
            val g = seg(prog, 0.05f + 0.1f * k, 0.2f + 0.1f * k)
            val y = h * (0.32f + 0.18f * k)
            drawLine(if (k == lit) SalC.Gold else SalC.Cell, Offset(w * 0.85f, y), Offset(w * 0.85f - w * lens[k] * g, y), h * 0.05f, StrokeCap.Round, alpha = alpha)
        }
    }
    if (ml > 0f) {
        val p = a.b("midlength")
        bars(ml, 1, p)
        tagAt(a.tag("midlength"), Offset(w * 0.5f, h * 0.86f), ml * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ls > 0f) {
        val p = a.b("longshort")
        bars(ls, if (p < 0.5f) 0 else 2, 1f)
        val dawn = 1f - seg(p, 0.45f, 0.55f)
        sun12(Offset(w * 0.08f, h * (if (dawn > 0.5f) 0.32f else 0.68f)), w * 0.025f, ls)
        tagAt(a.tag("longshort", 0, 0), Offset(w * 0.5f, h * 0.12f), ls * seg(p, 0.05f, 0.2f) * dawn, back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("longshort", 0, 1), Offset(w * 0.5f, h * 0.88f), ls * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tf > 0f) {
        val p = a.b("thirdfourth")
        for (k in 0 until 4) {
            val x = w * (0.8f - 0.2f * k)
            rakah74(Offset(x, h * 0.72f), h * 0.3f, seg(p, 0.05f + 0.1f * k, 0.15f + 0.1f * k), tf)
            val bw = if (k < 2) w * 0.12f else w * 0.06f
            openBook21(Offset(x, h * 0.3f), bw, Color(0xFFF2E8CE), Color(0xFFB8A888), tf * seg(p, 0.1f + 0.1f * k, 0.2f + 0.1f * k))
        }
        tagAt(a.tag("thirdfourth"), Offset(w * 0.3f, h * 0.88f), tf * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 8. sal-ruku: bowing and rising ═════════════

/**
 * Beats: flatback (one bows; a level line lies along his back and a calm glow settles: «جَاعِلًا رَأْسَهُ حِيَالَ ظَهْرِهِ», «وَيَطْمَئِنُّ فِي
 * رُكُوعِهِ»), threetasbih (three lights, one per glorification: «سُبْحَانَ رَبِّيَ الْعَظِيمِ», «ثَلَاثًا أَوْ أَكْثَرَ»), riseupsami (he rises to stand:
 * «سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ», «رَبَّنَا وَلَكَ الْحَمْدُ»), fillheavens (praise rises and fills the sky, then the earth: «مِلْءَ السَّمَاوَاتِ»,
 * «وَمِلْءَ الْأَرْضِ»), handsafterrise (standing again with hands on the chest: «يَضَعُ يَدَيْهِ عَلَى صَدْرِهِ بَعْدَ الرَّفْعِ»).
 */
internal fun DrawScope.bowingScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "flatback" to Shot(Offset(w * 0.5f, h * 0.65f), 1.1f),
        "threetasbih" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "riseupsami" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "fillheavens" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "handsafterrise" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { bowingDraw(a) }
}

private fun DrawScope.bowingDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val fb = fqIn(a, "flatback", "riseupsami")
    val tt = fqIn(a, "threetasbih", "riseupsami")
    val rs = fqIn(a, "riseupsami", "fillheavens")
    val fh = fqIn(a, "fillheavens", "handsafterrise")
    val hr = fqIn(a, "handsafterrise")
    val foot = Offset(w * 0.58f, floor + h * 0.3f)
    val tall = h * 0.5f
    val bowing = maxOf(fb, tt)
    if (bowing > 0f) {
        val p = a.b("flatback")
        bow12(foot, tall, SalC.Person, bowing, 0.5f * easeOut(seg(p, 0.05f, 0.3f)))
        val level = seg(p, 0.35f, 0.5f)
        val hip = foot - Offset(0f, tall * 0.45f)
        drawLine(SalC.Gold, hip + Offset(tall * 0.05f, -tall * 0.04f), hip + Offset(-tall * 0.5f, -tall * 0.04f), 2f * dp, StrokeCap.Round, alpha = fb * level)
        softGlow(hip + Offset(-tall * 0.2f, -tall * 0.05f), w * 0.1f, SalC.Gold, 0.4f * fb * seg(p, 0.6f, 0.8f))
        tagAt(a.tag("flatback", 0, 0), Offset(w * 0.5f, h * 0.12f), fb * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("flatback", 0, 1), Offset(w * 0.5f, h * 0.26f), fb * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tt > 0f) {
        val p = a.b("threetasbih")
        for (k in 0 until 3) {
            val g = seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k)
            val c = Offset(w * (0.3f - 0.08f * k), h * 0.3f)
            softGlow(c, w * 0.04f, SalC.Gold, tt * g)
            drawCircle(SalC.Gold, w * 0.014f, c, alpha = tt * g)
        }
        tagAt(a.tag("threetasbih", 0, 0), Offset(w * 0.5f, h * 0.12f), tt * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("threetasbih", 0, 1), Offset(w * 0.22f, h * 0.44f), tt * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (rs > 0f) {
        val p = a.b("riseupsami")
        val up = easeInOut(seg(p, 0.05f, 0.35f))
        if (up < 1f) bow12(foot, tall, SalC.Person, rs, 0.5f * (1f - up)) else takbir80(foot, tall, SalC.Person, rs, 0f)
        voice12(foot + Offset(-tall * 0.15f, -tall * 0.85f), w * 0.06f, t, SalC.Gold, rs * seg(p, 0.1f, 0.25f), -1f)
        tagAt(a.tag("riseupsami", 0, 0), Offset(w * 0.28f, h * 0.18f), rs * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("riseupsami", 0, 1), Offset(w * 0.28f, h * 0.34f), rs * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fh > 0f) {
        val p = a.b("fillheavens")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.6f * fh)
        takbir80(Offset(w * 0.5f, h * 0.9f), h * 0.3f, SalC.Person, fh, 0f)
        val sky = easeOut(seg(p, 0.05f, 0.45f))
        drawRect(SalC.Gold, Offset(0f, h * 0.4f * (1f - sky)), Size(w, h * 0.4f * sky), alpha = fh * 0.18f)
        for (k in 0 until 12) drawPath(starPath(Offset(w * fract(k * 0.618f), h * 0.4f * fract(k * 0.381f + 0.1f)), w * 0.008f), SalC.Gold, alpha = fh * sky)
        tagAt(a.tag("fillheavens", 0, 0), Offset(w * 0.5f, h * 0.2f), fh * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        val earth = easeOut(seg(p, 0.5f, 0.85f))
        drawRect(SalC.Gold, Offset(w * 0.5f - w * 0.5f * earth, h * 0.9f), Size(w * earth, h * 0.1f), alpha = fh * 0.25f)
        tagAt(a.tag("fillheavens", 0, 1), Offset(w * 0.5f, h * 0.55f), fh * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (hr > 0f) {
        val p = a.b("handsafterrise")
        pray12(foot, tall, SalC.Person, hr)
        softGlow(foot + Offset(0f, -tall * 0.68f), w * 0.06f, SalC.Gold, hr * seg(p, 0.2f, 0.4f))
        tagAt(a.tag("handsafterrise"), Offset(w * 0.4f, h * 0.12f), hr * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 9. sal-sujud: prostration ═════════════

/**
 * Beats: kneesfirst (he goes down, the knees touching first and lit, then the hands: «رُكْبَتَيْهِ قَبْلَ يَدَيْهِ», «فَإِنْ شَقَّ عَلَيْهِ قَدَّمَ
 * يَدَيْهِ»), sevenlimbs (in prostration, the forehead, the hands, the knees and the toes light one after another: «الْجَبْهَةُ مَعَ الْأَنْفِ»,
 * «وَالْيَدَيْنِ», «وَالرُّكْبَتَيْنِ», «وَبُطُونُ أَصَابِعِ الرِّجْلَيْنِ»), nearestbow (light comes down over him: «فَأَقْرَبُ مَا يَكُونُ الْعَبْدُ مِنْ رَبِّهِ
 * وَهُوَ سَاجِدٌ»), armsapart (the gap between arm and side, and the forearm lifted off the floor, both marked: «وَيُجَافِي عَضُدَيْهِ عَنْ جَنْبَيْهِ»,
 * «وَيَرْفَعُ ذِرَاعَيْهِ عَنِ الْأَرْضِ»), greatestpillar (among the pillars of prayer, one stands tallest: «أَعْظَمُ أَرْكَانِ الصَّلَاةِ»).
 */
internal fun DrawScope.prostrationScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "kneesfirst" to Shot(Offset(w * 0.5f, h * 0.7f), 1.1f),
        "sevenlimbs" to Shot(Offset(w * 0.5f, h * 0.75f), 1.2f),
        "nearestbow" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "armsapart" to Shot(Offset(w * 0.5f, h * 0.75f), 1.2f),
        "greatestpillar" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { prostrationDraw(a) }
}

private fun DrawScope.prostrationDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val kf = fqIn(a, "kneesfirst", "sevenlimbs")
    val sl = fqIn(a, "sevenlimbs", "nearestbow")
    val nb = fqIn(a, "nearestbow", "armsapart")
    val aa = fqIn(a, "armsapart", "greatestpillar")
    val gp = fqIn(a, "greatestpillar")
    val foot = Offset(w * 0.62f, floor + h * 0.3f)
    val tall = h * 0.5f
    val pts = sevenPoints81(foot, tall)
    if (kf > 0f) {
        val p = a.b("kneesfirst")
        val d = 0.5f + 0.5f * easeInOut(seg(p, 0.05f, 0.5f))
        bow12(foot, tall, SalC.Person, kf, d)
        softGlow(pts[2], w * 0.04f, SalC.Gold, kf * seg(p, 0.3f, 0.45f))
        softGlow(pts[1], w * 0.04f, SalC.Gold, kf * seg(p, 0.5f, 0.6f) * 0.7f)
        tagAt(a.tag("kneesfirst", 0, 0), Offset(w * 0.4f, h * 0.15f), kf * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("kneesfirst", 0, 1), Offset(w * 0.4f, h * 0.3f), kf * seg(p, 0.65f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (sl > 0f) {
        val p = a.b("sevenlimbs")
        bow12(foot, tall, SalC.Person, sl, 1f)
        val order = listOf(0, 1, 2, 3)
        for (k in order.indices) {
            val g = seg(p, 0.05f + 0.22f * k, 0.2f + 0.22f * k)
            softGlow(pts[order[k]], w * 0.035f, SalC.Gold, sl * g)
            drawCircle(SalC.Gold, w * 0.01f, pts[order[k]], alpha = sl * g)
            tagAt(a.tag("sevenlimbs", 0, k), Offset(w * (0.82f - 0.21f * k), h * 0.18f + (k % 2) * h * 0.1f), sl * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (nb > 0f) {
        val p = a.b("nearestbow")
        bow12(foot, tall, SalC.Person, nb, 1f)
        lightShafts(Offset(foot.x - tall * 0.15f, 0f), (PI / 2).toFloat(), 0.25f, floor + h * 0.3f, SalC.Gold, nb * seg(p, 0.1f, 0.4f) * 0.7f, t, rays = 5)
        softGlow(pts[0], w * 0.08f, SalC.Gold, nb * seg(p, 0.3f, 0.5f))
        tagAt(a.tag("nearestbow"), Offset(w * 0.5f, h * 0.15f), nb * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (aa > 0f) {
        val p = a.b("armsapart")
        bow12(foot, tall, SalC.Person, aa, 1f)
        val hip = foot + Offset(tall * 0.12f, -tall * 0.12f)
        drawArc(SalC.Gold, 200f, 80f, false, Offset(hip.x - tall * 0.3f, hip.y - tall * 0.05f), Size(tall * 0.3f, tall * 0.2f), alpha = aa * seg(p, 0.1f, 0.3f), style = Stroke(2f * dp))
        tagAt(a.tag("armsapart", 0, 0), Offset(w * 0.4f, h * 0.15f), aa * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val lift = seg(p, 0.5f, 0.65f)
        drawLine(SalC.Gold, pts[1] + Offset(-w * 0.01f, -h * 0.01f), pts[1] + Offset(w * 0.08f, -h * 0.01f), 2f * dp, alpha = aa * lift)
        drawLine(Color(0xFFC0503A), pts[1] + Offset(-w * 0.01f, h * 0.005f), pts[1] + Offset(w * 0.08f, h * 0.005f), 2f * dp, alpha = aa * lift * 0.6f)
        tagAt(a.tag("armsapart", 0, 1), Offset(w * 0.4f, h * 0.3f), aa * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (gp > 0f) {
        val p = a.b("greatestpillar")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.7f * gp)
        val base = h * 0.82f
        for (k in 0 until 7) {
            val big = k == 3
            val g = seg(p, 0.05f + 0.06f * k, 0.2f + 0.06f * k)
            val ht = h * (if (big) 0.6f else 0.35f) * g
            val x = w * (0.86f - 0.12f * k)
            drawRect(if (big) SalC.Gold else SalC.Mosque, Offset(x - w * 0.025f, base - ht), Size(w * 0.05f, ht), alpha = gp * (if (big) 1f else 0.5f))
            if (big) softGlow(Offset(x, base - ht), w * 0.08f, SalC.Gold, gp * seg(p, 0.5f, 0.7f))
        }
        tagAt(a.tag("greatestpillar"), Offset(w * 0.5f, h * 0.92f), gp * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 10. sal-julus: between the two prostrations ═════════════

/**
 * Beats: iftirash (seated on the left foot laid flat, the right foot upright, each marked: «فَيَفْرِشُ قَدَمَهُ الْيُسْرَى», «وَيَنْصِبُ الْيُمْنَى»),
 * rabbighfirli (a quiet word rises twice: «رَبِّ اغْفِرْ لِي»), vertebrae (a column of small bones settles back into line: «حَتَّى يَرْجِعَ كُلُّ فَقَارٍ
 * إِلَى مَكَانِهِ»), followimam (one in front bows; one behind who goes before him is struck out, one who follows after is ticked: «وَلَا يَجُوزُ
 * لِلْمَأْمُومِ مُسَابَقَةُ إِمَامِهِ», «وَالسُّنَّةُ أَنْ تَكُونَ أَفْعَالُهُ بَعْدَ إِمَامِهِ»), restsit (a short sit between rising from prostration and
 * standing: «جَلْسَةَ الِاسْتِرَاحَةِ», «وَإِنْ تَرَكَهَا فَلَا حَرَجَ»).
 */
internal fun DrawScope.betweensujudScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "iftirash" to Shot(Offset(w * 0.5f, h * 0.75f), 1.15f),
        "rabbighfirli" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "vertebrae" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "followimam" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
        "restsit" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { betweenSujudDraw(a) }
}

private fun DrawScope.betweenSujudDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val ift = fqIn(a, "iftirash", "vertebrae")
    val rg = fqIn(a, "rabbighfirli", "vertebrae")
    val vb = fqIn(a, "vertebrae", "followimam")
    val fi = fqIn(a, "followimam", "restsit")
    val rs = fqIn(a, "restsit")
    val seat = Offset(w * 0.56f, floor + h * 0.3f)
    val tall = h * 0.5f
    if (ift > 0f) {
        val p = a.b("iftirash")
        kneel80(seat, tall, SalC.Person, ift)
        val l = seg(p, 0.15f, 0.35f)
        drawLine(SalC.Gold, seat + Offset(-tall * 0.02f, h * 0.005f), seat + Offset(tall * 0.12f, h * 0.005f), 3f * dp, StrokeCap.Round, alpha = ift * l)
        val r = seg(p, 0.5f, 0.7f)
        drawLine(SalC.Gold, seat + Offset(tall * 0.14f, 0f), seat + Offset(tall * 0.14f, -tall * 0.1f), 3f * dp, StrokeCap.Round, alpha = ift * r)
        tagAt(a.tag("iftirash", 0, 0), Offset(w * 0.3f, h * 0.15f), ift * l, back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("iftirash", 0, 1), Offset(w * 0.3f, h * 0.3f), ift * r, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (rg > 0f) {
        val p = a.b("rabbighfirli")
        for (k in 0 until 2) {
            val u = seg(p, 0.1f + 0.4f * k, 0.5f + 0.4f * k)
            softGlow(Offset(seat.x - tall * 0.05f, mix(seat.y - tall * 0.55f, h * 0.05f, u)), w * 0.03f, SalC.Gold, rg * bump(u))
        }
        tagAt(a.tag("rabbighfirli"), Offset(w * 0.8f, h * 0.2f), rg * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (vb > 0f) {
        val p = a.b("vertebrae")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.7f * vb)
        val settle = easeOut(seg(p, 0.1f, 0.6f))
        for (k in 0 until 9) {
            val off = sin(k * 1.7f) * w * 0.04f * (1f - settle)
            val c = Offset(w * 0.5f + off, h * (0.2f + 0.07f * k))
            drawRoundRect(SalC.Mosque, Offset(c.x - w * 0.04f, c.y - h * 0.025f), Size(w * 0.08f, h * 0.05f), androidx.compose.ui.geometry.CornerRadius(h * 0.015f), alpha = vb)
        }
        drawLine(SalC.Gold, Offset(w * 0.5f, h * 0.16f), Offset(w * 0.5f, h * 0.82f), 1.5f * dp, alpha = vb * seg(p, 0.55f, 0.7f))
        tagAt(a.tag("vertebrae"), Offset(w * 0.5f, h * 0.92f), vb * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fi > 0f) {
        val p = a.b("followimam")
        val imamBow = 0.5f * easeInOut(seg(p, 0.1f, 0.3f))
        bow12(Offset(w * 0.2f, floor + h * 0.15f), h * 0.32f, SalC.Person, fi, imamBow)
        val early = 0.5f * easeInOut(seg(p, 0f, 0.15f))
        bow12(Offset(w * 0.5f, floor + h * 0.32f), h * 0.32f, SalC.Person, fi, early)
        cross14(Offset(w * 0.5f, floor - h * 0.05f), w * 0.035f, seg(p, 0.2f, 0.35f))
        val late = 0.5f * easeInOut(seg(p, 0.35f, 0.55f))
        bow12(Offset(w * 0.78f, floor + h * 0.32f), h * 0.32f, SalC.Person, fi, late)
        tick14(Offset(w * 0.78f, floor - h * 0.05f), w * 0.035f, seg(p, 0.55f, 0.7f))
        tagAt(a.tag("followimam", 0, 0), Offset(w * 0.5f, h * 0.12f), fi * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Ember)
        tagAt(a.tag("followimam", 0, 1), Offset(w * 0.5f, h * 0.26f), fi * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (rs > 0f) {
        val p = a.b("restsit")
        val stage = seg(p, 0f, 0.9f)
        when {
            stage < 0.3f -> bow12(seat, tall, SalC.Person, rs, 1f)
            stage < 0.6f -> kneel80(seat, tall, SalC.Person, rs)
            else -> takbir80(seat, tall, SalC.Person, rs, 0f)
        }
        softGlow(seat + Offset(0f, -tall * 0.3f), w * 0.08f, SalC.Gold, rs * bump(seg(stage, 0.3f, 0.6f)) * 0.6f)
        tagAt(a.tag("restsit", 0, 0), Offset(w * 0.3f, h * 0.15f), rs * seg(p, 0.25f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("restsit", 0, 1), Offset(w * 0.3f, h * 0.3f), rs * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 11. sal-tashahhud: the tashahhud and the salam ═════════════

/**
 * Beats: indexpoint (seated, the forefinger raised, a small light at its tip: «يُشِيرُ بِهَا إِلَى التَّوْحِيدِ»), tahiyyat (words of greeting rise as
 * light, then four shadows are held off: «التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ», «وَيَسْتَعِيذُ بِاللَّهِ مِنْ أَرْبَعٍ»), salamturn (the head
 * turns right, then left, and peace goes out each way: «السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ»), fourrefuge (four dark shapes, each stopped by a
 * shield of light: «عَذَابِ جَهَنَّمَ», «وَعَذَابِ الْقَبْرِ», «وَفِتْنَةِ الْمَحْيَا وَالْمَمَاتِ», «وَفِتْنَةِ الْمَسِيحِ الدَّجَّالِ»), tawarruk (in the last
 * sitting the hip rests on the ground: «وَيَجْلِسُ فِي الْأَخِيرِ مُتَوَرِّكًا»).
 */
internal fun DrawScope.tashahhudScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "indexpoint" to Shot(Offset(w * 0.5f, h * 0.7f), 1.15f),
        "tahiyyat" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "salamsides" to Shot(Offset(w * 0.5f, h * 0.65f), 1.1f),
        "fourrefuge" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "tawarruk" to Shot(Offset(w * 0.5f, h * 0.7f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { tashahhudDraw(a) }
}

private fun DrawScope.tashahhudDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    hall74(floor, t)
    val ip = fqIn(a, "indexpoint", "salamsides")
    val th = fqIn(a, "tahiyyat", "salamsides")
    val st = fqIn(a, "salamsides", "fourrefuge")
    val fr = fqIn(a, "fourrefuge", "tawarruk")
    val tw = fqIn(a, "tawarruk")
    val seat = Offset(w * 0.56f, floor + h * 0.3f)
    val tall = h * 0.5f
    val sitting = maxOf(ip, th, st)
    if (sitting > 0f) kneel80(seat, tall, SalC.Person, sitting, point = if (ip > 0f || th > 0f) 1f else 0f)
    if (ip > 0f) {
        val p = a.b("indexpoint")
        val tip = Offset(seat.x - tall * 0.26f, seat.y - tall * 0.16f)
        softGlow(tip, w * 0.04f, SalC.Gold, ip * seg(p, 0.3f, 0.5f) * (0.7f + 0.3f * sin(t * 3f)))
        tagAt(a.tag("indexpoint"), Offset(w * 0.3f, h * 0.15f), ip * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (th > 0f) {
        val p = a.b("tahiyyat")
        for (k in 0 until 3) {
            val u = seg(p, 0.05f + 0.12f * k, 0.4f + 0.12f * k)
            softGlow(Offset(seat.x - tall * 0.1f + k * w * 0.03f, mix(seat.y - tall * 0.6f, h * 0.05f, u)), w * 0.025f, SalC.Gold, th * bump(u))
        }
        tagAt(a.tag("tahiyyat", 0, 0), Offset(w * 0.5f, h * 0.12f), th * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("tahiyyat", 0, 1), Offset(w * 0.5f, h * 0.26f), th * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (st > 0f) {
        val p = a.b("salamsides")
        val head = seat - Offset(0f, tall * 0.1f + tall * 0.47f)
        val right = seg(p, 0.1f, 0.25f) * (1f - seg(p, 0.45f, 0.55f))
        val left = seg(p, 0.6f, 0.75f)
        drawCircle(SalC.Person, tall * 0.1f, head + Offset(tall * 0.06f * right - tall * 0.06f * left, 0f), alpha = st * 0.4f)
        voice12(head + Offset(w * 0.08f, 0f), w * 0.08f, t, SalC.Gold, st * right, 1f)
        voice12(head + Offset(-w * 0.08f, 0f), w * 0.08f, t, SalC.Gold, st * left, -1f)
        tagAt(a.tag("salamsides"), Offset(w * 0.5f, h * 0.12f), st * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fr > 0f) {
        val p = a.b("fourrefuge")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.8f * fr)
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.22f * k, 0.22f + 0.22f * k)
            val c = Offset(w * (0.82f - 0.21f * k), h * 0.45f)
            for (j in 0 until 5) drawCircle(Color(0xFF2A2430), w * (0.025f + 0.008f * (j % 3)), c + Offset(sin(j * 2.1f + t) * w * 0.025f, -h * 0.08f + cos(j * 1.7f + t) * h * 0.03f), alpha = fr * g * 0.9f)
            drawArc(SalC.Gold, 200f, 140f, false, Offset(c.x - w * 0.08f, c.y - h * 0.02f), Size(w * 0.16f, h * 0.12f), alpha = fr * seg(p, 0.12f + 0.22f * k, 0.25f + 0.22f * k), style = Stroke(4f * dp, cap = StrokeCap.Round))
            tagAt(a.tag("fourrefuge", 0, k), c + Offset(0f, h * 0.18f + (k % 2) * h * 0.08f), fr * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (tw > 0f) {
        val p = a.b("tawarruk")
        val sw = kotlin.math.max(1.7f * dp, tall * 0.065f)
        val hip = seat + Offset(tall * 0.04f, -tall * 0.05f)
        val knee = Offset(seat.x - tall * 0.26f, seat.y - tall * 0.03f)
        val shoulder = hip - Offset(tall * 0.02f, tall * 0.33f)
        drawLine(SalC.Person, hip, knee, sw, StrokeCap.Round, alpha = tw)
        drawLine(SalC.Person, knee, Offset(seat.x - tall * 0.05f, seat.y), sw, StrokeCap.Round, alpha = tw)
        drawLine(SalC.Person, Offset(seat.x - tall * 0.12f, seat.y), Offset(seat.x + tall * 0.2f, seat.y), sw, StrokeCap.Round, alpha = tw)
        drawLine(SalC.Person, hip, shoulder, sw, StrokeCap.Round, alpha = tw)
        drawCircle(SalC.Person, tall * 0.1f, shoulder - Offset(0f, tall * 0.14f), alpha = tw)
        drawLine(SalC.Person, shoulder, knee + Offset(tall * 0.06f, -tall * 0.08f), sw * 0.9f, StrokeCap.Round, alpha = tw)
        softGlow(hip + Offset(0f, tall * 0.04f), w * 0.05f, SalC.Gold, tw * seg(p, 0.2f, 0.4f))
        tagAt(a.tag("tawarruk"), Offset(w * 0.3f, h * 0.15f), tw * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 12. sal-arkan: the pillars of prayer ═════════════

/**
 * Beats: threetiers (pillars below, walls on them, ornaments on top: «أَرْكَانٌ», «وَوَاجِبَاتٌ», «وَسُنَنٌ»), fourteenpillars (fourteen pillars hold a
 * roof; one is taken away and the roof drops: «أَرْبَعَةَ عَشَرَ», «إِذَا تُرِكَ مِنْهَا شَيْءٌ بَطَلَتِ الصَّلَاةُ»), somepillars (standing, bowing,
 * prostrating and stillness, one after another: «الْقِيَامُ», «وَالرُّكُوعُ», «وَالسُّجُودُ», «وَالطُّمَأْنِينَةُ»), sittingside (one who cannot stand
 * prays seated, then lying on his side: «قَاعِدًا أَوْ عَلَى جَنْبٍ»), calmness (a hurried, shaking figure is struck out beside a still one that
 * glows: «وَمَنْ لَا يَطْمَئِنُّ فِي صَلَاتِهِ يُؤْمَرُ بِإِعَادَتِهَا»).
 */
internal fun DrawScope.pillarsofprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threetiers" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "fourteenpillars" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "somepillars" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
        "sittingside" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
        "calmness" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { pillarsOfPrayerDraw(a) }
}

private fun DrawScope.pillarsOfPrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.84f
    nightSky(t, SalC.Top, SalC.Low, ground, starAlpha = 0.5f)
    drawRect(SalC.Ground, Offset(0f, ground), Size(w, h - ground))
    val tt = fqIn(a, "threetiers", "fourteenpillars")
    val fp = fqIn(a, "fourteenpillars", "somepillars")
    val sp = fqIn(a, "somepillars", "sittingside")
    val ss = fqIn(a, "sittingside", "calmness")
    val cm = fqIn(a, "calmness")
    if (tt > 0f) {
        val p = a.b("threetiers")
        val g0 = seg(p, 0.05f, 0.25f)
        for (k in 0 until 6) drawRect(SalC.Gold, Offset(w * (0.2f + 0.12f * k), ground - h * 0.25f * g0), Size(w * 0.04f, h * 0.25f * g0), alpha = tt)
        tagAt(a.tag("threetiers", 0, 0), Offset(w * 0.9f, ground - h * 0.12f), tt * g0, back = Fq.Back, ink = ArtInk.Gold)
        val g1 = seg(p, 0.35f, 0.55f)
        drawRect(SalC.Mosque, Offset(w * 0.18f, ground - h * 0.25f - h * 0.18f * g1), Size(w * 0.64f, h * 0.18f * g1), alpha = tt * 0.8f)
        tagAt(a.tag("threetiers", 0, 1), Offset(w * 0.9f, ground - h * 0.34f), tt * g1, back = Fq.Back, ink = ArtInk.Ivory)
        val g2 = seg(p, 0.65f, 0.85f)
        for (k in 0 until 5) drawArc(SalC.Dome, 180f, 180f, true, Offset(w * (0.2f + 0.12f * k), ground - h * 0.48f), Size(w * 0.1f, h * 0.1f), alpha = tt * g2)
        tagAt(a.tag("threetiers", 0, 2), Offset(w * 0.9f, ground - h * 0.5f), tt * g2, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (fp > 0f) {
        val p = a.b("fourteenpillars")
        val out = seg(p, 0.5f, 0.6f)
        val drop = easeInOut(seg(p, 0.6f, 0.8f))
        for (k in 0 until 14) {
            val x = w * (0.1f + 0.0615f * k)
            val gone = k == 9
            drawRect(SalC.Gold, Offset(x - w * 0.012f, ground - h * 0.4f + (if (gone) -h * 0.5f * out else 0f)), Size(w * 0.024f, h * 0.4f), alpha = fp * (if (gone) 1f - out else 1f) * seg(p, 0.02f * k, 0.02f * k + 0.1f))
        }
        val roofY = ground - h * 0.44f
        drawLine(SalC.Mosque, Offset(w * 0.08f, roofY), Offset(w * 0.92f, roofY + h * 0.25f * drop), h * 0.04f, StrokeCap.Round, alpha = fp)
        tagAt(a.tag("fourteenpillars", 0, 0), Offset(w * 0.5f, h * 0.12f), fp * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("fourteenpillars", 0, 1), Offset(w * 0.5f, h * 0.26f), fp * seg(p, 0.65f, 0.85f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (sp > 0f) {
        val p = a.b("somepillars")
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.22f * k, 0.22f + 0.22f * k)
            val foot = Offset(w * (0.84f - 0.22f * k), ground)
            when (k) {
                0 -> takbir80(foot, h * 0.3f, SalC.Person, sp * g, 0f)
                1 -> bow12(foot, h * 0.3f, SalC.Person, sp * g, 0.5f)
                2 -> bow12(foot, h * 0.3f, SalC.Person, sp * g, 1f)
                else -> { kneel80(foot, h * 0.3f, SalC.Person, sp * g); softGlow(foot + Offset(0f, -h * 0.15f), w * 0.06f, SalC.Gold, 0.5f * sp * g) }
            }
            tagAt(a.tag("somepillars", 0, k), Offset(foot.x, h * (0.2f + 0.1f * (k % 2))), sp * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (ss > 0f) {
        val p = a.b("sittingside")
        val g0 = seg(p, 0.05f, 0.3f)
        sit12(Offset(w * 0.7f, ground), h * 0.32f, SalC.Person, ss * g0, -1f)
        val g1 = seg(p, 0.45f, 0.65f)
        drawRoundRect(Color(0xFF6A5A4A), Offset(w * 0.12f, ground - h * 0.05f), Size(w * 0.34f, h * 0.05f), androidx.compose.ui.geometry.CornerRadius(h * 0.01f), alpha = ss * g1)
        lying73(Offset(w * 0.16f, ground - h * 0.09f), w * 0.28f, SalC.Person, ss * g1)
        tagAt(a.tag("sittingside"), Offset(w * 0.5f, h * 0.15f), ss * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (cm > 0f) {
        val p = a.b("calmness")
        val jit = sin(t * 30f) * w * 0.01f
        bow12(Offset(w * 0.3f + jit, ground), h * 0.32f, SalC.Person, cm * 0.8f, 0.5f + 0.5f * fract(t * 2f))
        cross14(Offset(w * 0.3f, ground - h * 0.42f), w * 0.04f, seg(p, 0.2f, 0.35f))
        bow12(Offset(w * 0.7f, ground), h * 0.32f, SalC.Person, cm, 0.5f)
        softGlow(Offset(w * 0.68f, ground - h * 0.18f), w * 0.1f, SalC.Gold, cm * seg(p, 0.4f, 0.6f))
        tagAt(a.tag("calmness"), Offset(w * 0.5f, h * 0.12f), cm * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}
