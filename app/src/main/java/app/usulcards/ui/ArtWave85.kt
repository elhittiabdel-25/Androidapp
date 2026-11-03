package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 85 (2026-11-03): v1.3 fiqh, prayer 6, the last five cards: the eclipse prayer, the prayer for rain, washing and shrouding
 * the dead, the funeral prayer, and burial and condolence. The one who has died is only a white shrouded shape, never a body; graves
 * are plain; the living are faceless figures.
 */

/** A body wrapped in white, lying along [len] from [head] to the right, with three ties. */
internal fun DrawScope.shroud85(head: Offset, len: Float, alpha: Float, layers: Int = 3) {
    if (alpha <= 0.01f) return
    val th = len * 0.22f
    for (k in 0 until layers) drawRoundRect(lerpColor(Color(0xFFF4F1E8), Color(0xFFD8D2C2), k * 0.3f), Offset(head.x - len * 0.02f * k, head.y - th / 2f - len * 0.01f * k), Size(len + len * 0.04f * k, th + len * 0.02f * k), CornerRadius(th * 0.5f), alpha = alpha * (if (k == 0) 1f else 0.6f))
    drawRoundRect(Color(0xFFF4F1E8), Offset(head.x, head.y - th / 2f), Size(len, th), CornerRadius(th * 0.5f), alpha = alpha)
    for (k in 0 until 3) {
        val x = head.x + len * (0.18f + 0.32f * k)
        drawLine(Color(0xFFB8B0A0), Offset(x, head.y - th / 2f), Offset(x, head.y + th / 2f), len * 0.02f, alpha = alpha)
    }
}

/** A bier: a plank on four short legs, its top centred on [top]. */
private fun DrawScope.bier85(top: Offset, wd: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val wood = Color(0xFF6A5A4A)
    drawRect(wood, Offset(top.x - wd / 2f, top.y), Size(wd, wd * 0.05f), alpha = alpha)
    for (k in 0 until 2) drawLine(wood, Offset(top.x + (if (k == 0) -1f else 1f) * wd * 0.42f, top.y), Offset(top.x + (if (k == 0) -1f else 1f) * wd * 0.42f, top.y + wd * 0.14f), wd * 0.025f, alpha = alpha)
}

// ═════════════ 30. sal-kusuf: the eclipse prayer ═════════════

/**
 * Beats: sundarkens (a dark disc slides over the sun and the sky dims: «انْحِجَابُ ضَوْءِ الشَّمْسِ أَوِ الْقَمَرِ», «يُخَوِّفُ اللَّهُ بِهِ عِبَادَهُ حَتَّى
 * يَرْجِعُوا إِلَيْهِ»), eclipsewindow (a band from the start of the eclipse to its end: «مِنِ ابْتِدَاءِ الْكُسُوفِ إِلَى ذَهَابِهِ»), tworukuperrakah (one
 * stands, bows, stands again, bows again, then prostrates, the marks counting two standings and two bows in a rakʿa: «قِيَامَانِ وَرُكُوعَانِ
 * وَسَجْدَتَانِ», «وَيَجْهَرُ بِالْقِرَاءَةِ»), longthenshorter (bars of reading and bowing, each shorter than the one before: «وَسُورَةً دُونَ الْأُولَى»),
 * secondless (the second rakʿa's bars are all shorter than the first's: «لَكِنْ دُونَهَا فِي كُلِّ مَا يَفْعَلُ»).
 */
internal fun DrawScope.eclipseprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sundarkens" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "eclipsewindow" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "tworukuperrakah" to Shot(Offset(w * 0.5f, h * 0.65f), 1f),
        "longthenshorter" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "secondless" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { eclipsePrayerDraw(a) }
}

private fun DrawScope.eclipsePrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sd = fqIn(a, "sundarkens", "eclipsewindow")
    val ew = fqIn(a, "eclipsewindow", "tworukuperrakah")
    val tr = fqIn(a, "tworukuperrakah", "longthenshorter")
    val ls = fqIn(a, "longthenshorter", "secondless")
    val sl = fqIn(a, "secondless")
    val ground = h * 0.84f
    val cover = seg(a.b("sundarkens"), 0.1f, 0.6f)
    daySky12(lerpColor(SalC.DayTop, Color(0xFF2A3050), cover), lerpColor(SalC.DayLow, Color(0xFF4A4A6A), cover), ground, lerpColor(SalC.Sand, Color(0xFF3A3448), cover))
    if (sd > 0f) {
        val p = a.b("sundarkens")
        val sun = Offset(w * 0.5f, h * 0.32f)
        val r = w * 0.08f
        softGlow(sun, r * 2f, Color(0xFFFFE0A0), sd * (1f - 0.7f * cover))
        drawCircle(Color(0xFFFFF2C0), r, sun, alpha = sd)
        drawCircle(Color(0xFF1A1826), r * 1.02f, sun + Offset(mix(r * 2.2f, r * 0.15f, cover), 0f), alpha = sd)
        tagAt(a.tag("sundarkens", 0, 0), Offset(w * 0.5f, h * 0.62f), sd * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("sundarkens", 0, 1), Offset(w * 0.5f, h * 0.76f), sd * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ew > 0f) {
        val p = a.b("eclipsewindow")
        drawRect(Color(0xFF1A1826), Offset.Zero, size, alpha = 0.6f * ew)
        val y = h * 0.5f
        for (k in 0 until 5) {
            val c = Offset(w * (0.85f - 0.175f * k), y - h * 0.15f)
            val f = if (k <= 2) k / 2f else (4 - k) / 2f
            drawCircle(Color(0xFFFFF2C0), w * 0.03f, c, alpha = ew)
            drawCircle(Color(0xFF1A1826), w * 0.031f, c + Offset(w * 0.06f * (1f - f), 0f), alpha = ew)
        }
        val run = easeOut(seg(p, 0.1f, 0.6f))
        drawLine(SalC.Gold, Offset(w * 0.85f, y), Offset(mix(w * 0.85f, w * 0.15f, run), y), h * 0.04f, StrokeCap.Round, alpha = ew * 0.8f)
        tagAt(a.tag("eclipsewindow"), Offset(w * 0.5f, h * 0.7f), ew * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tr > 0f) {
        val p = a.b("tworukuperrakah")
        drawRect(Color(0xFF1A1826), Offset.Zero, size, alpha = 0.5f * tr)
        val foot = Offset(w * 0.55f, ground)
        val ph = seg(p, 0.05f, 0.85f) * 5f
        val stage = ph.toInt().coerceAtMost(4)
        when (stage) {
            0, 2 -> takbir80(foot, h * 0.36f, SalC.Person, tr, 0f)
            1, 3 -> bow12(foot, h * 0.36f, SalC.Person, tr, 0.5f)
            else -> bow12(foot, h * 0.36f, SalC.Person, tr, 1f)
        }
        val labels = listOf(0, 1, 0, 1, 2)
        for (k in 0 until 5) {
            val c = Offset(w * (0.88f - 0.04f * k), h * 0.2f)
            drawCircle(if (labels[k] == 2) Fq.Water else SalC.Gold, w * 0.012f, c, alpha = tr * (if (k <= stage) 1f else 0.25f))
        }
        voice12(foot + Offset(-h * 0.1f, -h * 0.4f), w * 0.05f, t, SalC.Gold, tr * (if (stage == 0 || stage == 2) 1f else 0f), -1f)
        tagAt(a.tag("tworukuperrakah", 0, 0), Offset(w * 0.4f, h * 0.12f), tr * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("tworukuperrakah", 0, 1), Offset(w * 0.25f, h * 0.3f), tr * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val barsAt = { x0: Float, scale: Float, alpha: Float, prog: Float ->
        val lens = listOf(1f, 0.85f, 0.7f, 0.6f)
        for (k in 0 until 4) {
            val g = seg(prog, 0.05f + 0.15f * k, 0.2f + 0.15f * k)
            val y = h * (0.3f + 0.12f * k)
            drawLine(if (k % 2 == 0) SalC.Gold else SalC.Rim, Offset(x0, y), Offset(x0 - w * 0.35f * lens[k] * scale * g, y), h * 0.04f, StrokeCap.Round, alpha = alpha)
        }
    }
    if (ls > 0f) {
        val p = a.b("longthenshorter")
        drawRect(Color(0xFF1A1826), Offset.Zero, size, alpha = 0.7f * ls)
        barsAt(w * 0.85f, 1.6f, ls, p)
        tagAt(a.tag("longthenshorter"), Offset(w * 0.5f, h * 0.85f), ls * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sl > 0f) {
        val p = a.b("secondless")
        drawRect(Color(0xFF1A1826), Offset.Zero, size, alpha = 0.7f * sl)
        barsAt(w * 0.92f, 1f, sl * 0.6f, 1f)
        barsAt(w * 0.45f, 0.75f, sl, p)
        tagAt(a.tag("secondless"), Offset(w * 0.5f, h * 0.85f), sl * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 31. sal-istisqa: the prayer for rain ═════════════

/**
 * Beats: drought (cracked earth and a withered plant under a hard sun: «إِذَا أَجْدَبَتِ الْأَرْضُ وَقَحَطَ الْمَطَرُ»), tworakatground (rows at prayer in the
 * open, then raised hands alone: «رَكْعَتَيْنِ فِي الْمُصَلَّى», «وَيَجُوزُ الِاسْتِسْقَاءُ بِالدُّعَاءِ عَلَى أَيِّ صِفَةٍ»), repentfirst (dark specks lift from a
 * gathered crowd and the first clouds come: «وَيَأْمُرُهُمْ بِالتَّوْبَةِ وَالْخُرُوجِ مِنَ الْمَظَالِمِ», «لِأَنَّ الْمَعَاصِيَ سَبَبُ الْقَحْطِ»), cloakturn
 * (a cloak turns over, inside to outside: «وَيُحَوِّلُ رِدَاءَهُ تَفَاؤُلًا بِتَحَوُّلِ الْحَالِ»), rainarrives (rain falls and the field greens: «اللَّهُمَّ
 * صَيِّبًا نَافِعًا», «اللَّهُمَّ حَوَالَيْنَا وَلَا عَلَيْنَا»).
 */
internal fun DrawScope.rainprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "drought" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "tworakatground" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "repentfirst" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "cloakturn" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "rainarrives" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { rainPrayerDraw(a) }
}

private fun DrawScope.rainPrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    val green = seg(a.b("rainarrives"), 0.2f, 0.8f)
    val clouds = maxOf(seg(a.b("repentfirst"), 0.4f, 0.9f), if (a.b("rainarrives") > 0f) 1f else 0f)
    daySky12(lerpColor(Color(0xFFC8B08A), Color(0xFF6A7A8A), clouds), lerpColor(Color(0xFFF2D8A8), Color(0xFF9AA8B0), clouds), ground, lerpColor(Color(0xFFB8986A), Color(0xFF6A9A5A), green))
    val dr = fqIn(a, "drought", "tworakatground")
    val tg = fqIn(a, "tworakatground", "repentfirst")
    val rf = fqIn(a, "repentfirst", "cloakturn")
    val ct = fqIn(a, "cloakturn", "rainarrives")
    val ra = fqIn(a, "rainarrives")
    if (dr > 0f) {
        val p = a.b("drought")
        sun12(Offset(w * 0.8f, h * 0.18f), w * 0.05f, dr)
        for (k in 0 until 9) {
            val x = w * (0.08f + 0.1f * k)
            drawPath(Path().apply { moveTo(x, ground + h * 0.03f); lineTo(x + w * 0.02f, ground + h * 0.08f); lineTo(x - w * 0.01f, ground + h * 0.14f) }, Color(0xFF6A5030), alpha = dr * seg(p, 0.05f * k, 0.05f * k + 0.2f), style = Stroke(1.5f * dp))
        }
        val plant = Offset(w * 0.4f, ground)
        drawLine(Color(0xFF8A7A4A), plant, plant + Offset(-w * 0.02f, -h * 0.12f), 2f * dp, alpha = dr)
        drawLine(Color(0xFF8A7A4A), plant + Offset(-w * 0.01f, -h * 0.06f), plant + Offset(w * 0.03f, -h * 0.05f), 2f * dp, alpha = dr)
        tagAt(a.tag("drought"), Offset(w * 0.5f, h * 0.12f), dr * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (tg > 0f) {
        val p = a.b("tworakatground")
        val g0 = 1f - seg(p, 0.45f, 0.55f)
        row74(ground, w * 0.15f, w * 0.85f, 7, h * 0.24f, Color(0xFF5A4E44), tg * g0)
        tagAt(a.tag("tworakatground", 0, 0), Offset(w * 0.5f, h * 0.15f), tg * seg(p, 0.05f, 0.25f) * g0, back = Fq.Back, ink = ArtInk.Gold)
        val g1 = seg(p, 0.5f, 0.65f)
        hands12(Offset(w * 0.5f, ground - h * 0.12f), h * 0.24f, Color(0xFF5A4E44), tg * g1)
        tagAt(a.tag("tworakatground", 0, 1), Offset(w * 0.5f, h * 0.15f), tg * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (rf > 0f) {
        val p = a.b("repentfirst")
        for (k in 0 until 7) figure(Offset(w * (0.2f + 0.1f * k), ground), h * 0.22f, Color(0xFF5A4E44), rf)
        for (k in 0 until 10) {
            val u = fract(t * 0.3f + k / 10f)
            drawCircle(Color(0xFF2A2430), w * 0.01f, Offset(w * (0.2f + 0.065f * k), ground - h * 0.25f - u * h * 0.4f), alpha = rf * (1f - u) * seg(p, 0.1f, 0.3f) * (1f - seg(p, 0.6f, 0.9f)))
        }
        for (k in 0 until 3) cloud38(Offset(w * (0.25f + 0.25f * k), h * 0.15f), w * 0.06f, Color(0xFFD8DCE4), rf * seg(p, 0.5f + 0.1f * k, 0.7f + 0.1f * k))
        tagAt(a.tag("repentfirst", 0, 0), Offset(w * 0.5f, h * 0.35f), rf * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("repentfirst", 0, 1), Offset(w * 0.5f, h * 0.5f), rf * seg(p, 0.45f, 0.65f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ct > 0f) {
        val p = a.b("cloakturn")
        val c = Offset(w * 0.5f, h * 0.5f)
        val flip = easeInOut(seg(p, 0.2f, 0.6f))
        val sx = cos(PI.toFloat() * flip)
        val col = if (flip < 0.5f) Color(0xFF8A6A48) else Color(0xFFD8C8A0)
        drawRoundRect(col, Offset(c.x - w * 0.18f * kotlin.math.abs(sx), c.y - h * 0.22f), Size(w * 0.36f * kotlin.math.abs(sx) + 1f, h * 0.44f), CornerRadius(w * 0.02f), alpha = ct)
        for (k in 0 until 4) drawLine(lerpColor(col, Color.Black, 0.2f), Offset(c.x - w * 0.18f * kotlin.math.abs(sx) + w * 0.09f * kotlin.math.abs(sx) * k, c.y - h * 0.22f), Offset(c.x - w * 0.18f * kotlin.math.abs(sx) + w * 0.09f * kotlin.math.abs(sx) * k, c.y + h * 0.22f), 1.2f * dp, alpha = ct * 0.5f)
        softGlow(c, w * 0.2f, SalC.Gold, 0.4f * ct * seg(p, 0.6f, 0.8f))
        tagAt(a.tag("cloakturn"), Offset(w * 0.5f, h * 0.86f), ct * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ra > 0f) {
        val p = a.b("rainarrives")
        for (k in 0 until 3) cloud38(Offset(w * (0.25f + 0.25f * k), h * 0.15f), w * 0.07f, Color(0xFFB8C4D0), ra)
        rain12(w * 0.1f, w * 0.9f, h * 0.2f, ground, t, Fq.Water, ra * seg(p, 0.05f, 0.2f), 40)
        for (k in 0 until 8) {
            val x = w * (0.1f + 0.11f * k)
            val gg = seg(p, 0.3f + 0.04f * k, 0.5f + 0.04f * k)
            drawLine(Color(0xFF6FBF8A), Offset(x, ground), Offset(x, ground - h * 0.08f * gg), 2.5f * dp, StrokeCap.Round, alpha = ra)
            drawOval(Color(0xFF6FBF8A), Offset(x, ground - h * 0.08f * gg - h * 0.015f), Size(w * 0.025f * gg, h * 0.02f), alpha = ra)
        }
        tagAt(a.tag("rainarrives", 0, 0), Offset(w * 0.5f, h * 0.35f), ra * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Jade)
        tagAt(a.tag("rainarrives", 0, 1), Offset(w * 0.5f, h * 0.5f), ra * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 32. sal-ghusl-mayyit: washing and shrouding ═════════════

/**
 * Beats: visitremind (a visitor by a sickbed; a quiet light between them: «لُقِّنَ لَا إِلَهَ إِلَّا اللَّهُ», «وَأُسْرِعَ بِتَجْهِيزِهِ»), washdeceased (a
 * covered white shape on a board, water poured gently over the cover: «وَغَسْلُ الْمَيِّتِ وَتَكْفِينُهُ وَاجِبَانِ», «الثِّقَةُ الْعَارِفُ بِأَحْكَامِهِ»),
 * threewhite (three white sheets fold over it and are tied: «ثَلَاثَةِ أَثْوَابٍ بِيضٍ», «وَالْوَاجِبُ سَتْرُ جَمِيعِ الْبَدَنِ»), dustinstead (when there is no
 * water, hands strike clean earth instead: «يُمِّمَ بِالتُّرَابِ»).
 */
internal fun DrawScope.preparingdeceasedScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "visitremind" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "washdeceased" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "threewhite" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "dustinstead" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { preparingDeceasedDraw(a) }
}

private fun DrawScope.preparingDeceasedDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ground = h * 0.84f
    nightSky(t, Color(0xFF14182A), Color(0xFF34385A), ground, starAlpha = 0.4f)
    drawRect(Color(0xFF242836), Offset(0f, ground), Size(w, h - ground))
    val vr = fqIn(a, "visitremind", "washdeceased")
    val wd = fqIn(a, "washdeceased", "threewhite")
    val tw = fqIn(a, "threewhite", "dustinstead")
    val di = fqIn(a, "dustinstead")
    if (vr > 0f) {
        val p = a.b("visitremind")
        val bed = Offset(w * 0.62f, ground)
        drawRoundRect(Color(0xFF6A5A4A), Offset(bed.x - w * 0.2f, bed.y - h * 0.1f), Size(w * 0.4f, h * 0.1f), CornerRadius(h * 0.01f), alpha = vr)
        drawRoundRect(Color(0xFFDAD2C2), Offset(bed.x - w * 0.16f, bed.y - h * 0.15f), Size(w * 0.3f, h * 0.06f), CornerRadius(h * 0.03f), alpha = vr)
        drawCircle(SalC.Person, h * 0.035f, Offset(bed.x + w * 0.17f, bed.y - h * 0.14f), alpha = vr)
        sit12(Offset(w * 0.28f, ground), h * 0.3f, SalC.Person, vr, 1f)
        softGlow(Offset(w * 0.5f, ground - h * 0.25f), w * 0.08f, SalC.Gold, vr * seg(p, 0.2f, 0.4f))
        tagAt(a.tag("visitremind", 0, 0), Offset(w * 0.5f, h * 0.15f), vr * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("visitremind", 0, 1), Offset(w * 0.5f, h * 0.3f), vr * seg(p, 0.65f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val board = Offset(w * 0.3f, ground - h * 0.12f)
    if (wd > 0f) {
        val p = a.b("washdeceased")
        drawRect(Color(0xFF6A5A4A), Offset(board.x - w * 0.02f, board.y + h * 0.05f), Size(w * 0.48f, h * 0.025f), alpha = wd)
        shroud85(board, w * 0.42f, wd, layers = 1)
        fqEwer(Offset(w * 0.55f, board.y - h * 0.12f), h * 0.18f, Color(0xFF6A8AA8), wd, pour = 0.6f * seg(p, 0.2f, 0.35f), t = t)
        tagAt(a.tag("washdeceased", 0, 0), Offset(w * 0.5f, h * 0.15f), wd * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("washdeceased", 0, 1), Offset(w * 0.5f, h * 0.3f), wd * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tw > 0f) {
        val p = a.b("threewhite")
        drawRect(Color(0xFF6A5A4A), Offset(board.x - w * 0.02f, board.y + h * 0.05f), Size(w * 0.48f, h * 0.025f), alpha = tw)
        val n = 1 + (seg(p, 0.05f, 0.5f) * 2.99f).toInt()
        shroud85(board, w * 0.42f, tw, layers = n)
        tagAt(a.tag("threewhite", 0, 0), Offset(w * 0.5f, h * 0.15f), tw * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("threewhite", 0, 1), Offset(w * 0.5f, h * 0.3f), tw * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (di > 0f) {
        val p = a.b("dustinstead")
        val strike = seg(p, 0.1f, 0.25f)
        for (sg in listOf(-1f, 1f)) drawRoundRect(Color(0xFFD8B898), Offset(w * 0.5f + sg * w * 0.07f - w * 0.04f, mix(h * 0.45f, ground - h * 0.06f, strike) - h * 0.04f), Size(w * 0.08f, h * 0.08f), CornerRadius(w * 0.02f), alpha = di)
        val puff = seg(p, 0.25f, 0.5f)
        for (k in 0 until 10) {
            val ang = PI.toFloat() * (k / 9f)
            drawCircle(Color(0xFFE8D8B0), w * 0.01f, Offset(w * 0.5f, ground) + Offset(-cos(ang), -sin(ang)) * (w * 0.18f * puff), alpha = di * bump(puff))
        }
        tagAt(a.tag("dustinstead"), Offset(w * 0.5f, h * 0.15f), di * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 33. sal-janaza: the funeral prayer ═════════════

/**
 * Beats: fourtakbir (a bier before the rows, four lights above it in turn: «فَرْضُ كِفَايَةٍ», «وَيُكَبِّرُ أَرْبَعًا»), foursteps (after each takbir a
 * sign: a book read softly, a light, raised hands for the dead, then the salam: «بَعْدَ الْأُولَى الْفَاتِحَةَ سِرًّا», «وَبَعْدَ الثَّالِثَةِ يَدْعُو
 * لِلْمَيِّتِ»), latejoin (one who comes late joins the row and makes up the lights he missed; a distant bier glows too: «دَخَلَ مَعَ الْإِمَامِ وَقَضَى مَا
 * فَاتَهُ», «وَيُصَلَّى عَلَى الْغَائِبِ»), duaall (light rises over houses and graves alike: «اللَّهُمَّ اغْفِرْ لِحَيِّنَا وَمَيِّتِنَا»), qirats (following
 * the bier earns one mound of light, staying to the burial a second: «فَلَهُ قِيرَاطٌ», «فَلَهُ قِيرَاطَانِ»).
 */
internal fun DrawScope.funeralprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fourtakbir" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "foursteps" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "latejoin" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "duaall" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "qirats" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { funeralPrayerDraw(a) }
}

private fun DrawScope.funeralPrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.84f
    nightSky(t, Color(0xFF14182A), Color(0xFF34385A), ground, starAlpha = 0.5f)
    drawRect(Color(0xFF242836), Offset(0f, ground), Size(w, h - ground))
    val ft = fqIn(a, "fourtakbir", "foursteps")
    val fs = fqIn(a, "foursteps", "latejoin")
    val lj = fqIn(a, "latejoin", "duaall")
    val da = fqIn(a, "duaall", "qirats")
    val qr = fqIn(a, "qirats")
    val bierTop = Offset(w * 0.18f, ground - h * 0.14f)
    if (ft > 0f || lj > 0f) {
        val f = maxOf(ft, lj)
        bier85(bierTop, w * 0.26f, f)
        shroud85(bierTop + Offset(-w * 0.12f, -h * 0.03f), w * 0.24f, f, layers = 1)
        takbir80(Offset(w * 0.4f, ground), h * 0.28f, SalC.Person, f, 0f)
        row74(ground, w * 0.55f, w * 0.9f, 4, h * 0.26f, SalC.Person, f)
    }
    if (ft > 0f) {
        val p = a.b("fourtakbir")
        for (k in 0 until 4) {
            val g = seg(p, 0.3f + 0.12f * k, 0.38f + 0.12f * k)
            softGlow(Offset(w * (0.5f + 0.1f * k), h * 0.25f), w * 0.03f, SalC.Gold, ft * g)
            drawCircle(SalC.Gold, w * 0.012f, Offset(w * (0.5f + 0.1f * k), h * 0.25f), alpha = ft * g)
        }
        tagAt(a.tag("fourtakbir", 0, 0), Offset(w * 0.3f, h * 0.12f), ft * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("fourtakbir", 0, 1), Offset(w * 0.65f, h * 0.4f), ft * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fs > 0f) {
        val p = a.b("foursteps")
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.22f * k, 0.22f + 0.22f * k)
            val c = Offset(w * (0.82f - 0.21f * k), h * 0.5f)
            drawCircle(SalC.Gold, w * 0.012f, c + Offset(0f, -h * 0.2f), alpha = fs)
            when (k) {
                0 -> openBook21(c, w * 0.1f, Color(0xFFF2E8CE), Color(0xFFB8A888), fs * g)
                1 -> softGlow(c, w * 0.05f, SalC.Gold, fs * g)
                2 -> hands12(c + Offset(0f, h * 0.04f), h * 0.14f, SalC.Person, fs * g)
                else -> { voice12(c + Offset(w * 0.04f, 0f), w * 0.04f, t, SalC.Gold, fs * g, 1f) }
            }
            if (k == 0) tagAt(a.tag("foursteps", 0, 0), c + Offset(0f, h * 0.2f), fs * g, back = Fq.Back, ink = ArtInk.Ivory)
            if (k == 2) tagAt(a.tag("foursteps", 0, 1), c + Offset(0f, h * 0.2f), fs * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    if (lj > 0f) {
        val p = a.b("latejoin")
        val u = easeInOut(seg(p, 0.05f, 0.4f))
        if (u < 1f) figure(Offset(mix(w * 1.02f, w * 0.95f, u), ground), h * 0.26f, Color(0xFFC8A890), lj, walk = t * 6f)
        else takbir80(Offset(w * 0.95f, ground), h * 0.26f, Color(0xFFC8A890), lj, 0f)
        for (k in 0 until 2) drawCircle(SalC.Gold, w * 0.012f, Offset(w * (0.88f + 0.04f * k), h * 0.3f), alpha = lj * seg(p, 0.4f + 0.1f * k, 0.5f + 0.1f * k))
        tagAt(a.tag("latejoin", 0, 0), Offset(w * 0.6f, h * 0.15f), lj * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(p, 0.65f, 0.8f)
        softGlow(Offset(w * 0.12f, h * 0.2f), w * 0.05f, Color(0xFFDDEEFF), lj * g)
        tagAt(a.tag("latejoin", 0, 1), Offset(w * 0.3f, h * 0.3f), lj * g, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (da > 0f) {
        val p = a.b("duaall")
        for (k in 0 until 3) house12(Offset(w * (0.62f + 0.12f * k), ground), w * 0.1f, Color(0xFFCDBFA6), Color(0xFF6A5A48), da, lit = 0.6f)
        for (k in 0 until 3) {
            val g = Offset(w * (0.12f + 0.12f * k), ground)
            drawArc(Color(0xFF4A4E5E), 180f, 180f, true, Offset(g.x - w * 0.05f, g.y - h * 0.04f), Size(w * 0.1f, h * 0.08f), alpha = da)
        }
        for (k in 0 until 8) {
            val u = fract(t * 0.25f + k / 8f)
            val x = w * (0.1f + 0.11f * k)
            softGlow(Offset(x, ground - h * 0.1f - u * h * 0.5f), w * 0.02f, SalC.Gold, da * seg(p, 0.2f, 0.4f) * (1f - u))
        }
        tagAt(a.tag("duaall"), Offset(w * 0.5f, h * 0.15f), da * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (qr > 0f) {
        val p = a.b("qirats")
        val g0 = seg(p, 0.1f, 0.35f)
        val g1 = seg(p, 0.55f, 0.8f)
        for (k in 0 until 2) {
            val g = if (k == 0) g0 else g1
            val c = Offset(w * (0.68f - 0.36f * k), ground)
            drawPath(Path().apply { moveTo(c.x - w * 0.14f, c.y); quadraticTo(c.x, c.y - h * 0.5f * g, c.x + w * 0.14f, c.y); close() }, SalC.Gold, alpha = qr * 0.4f * g)
            softGlow(c + Offset(0f, -h * 0.2f * g), w * 0.1f, SalC.Gold, qr * g * 0.5f)
            tagAt(a.tag("qirats", 0, k), c + Offset(0f, -h * 0.55f), qr * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 34. sal-dafn: burial and condolence ═════════════

/**
 * Beats: hastenbier (four carry the bier at a brisk walk: «يُسَنُّ الْإِسْرَاعُ بِالْجِنَازَةِ»), lahdniche (a deep grave seen in section, a niche cut into its
 * qibla side; the shrouded one laid on the right side facing it: «وَيُوَسَّعُ وَيُلْحَدُ», «عَلَى شِقِّهِ الْأَيْمَنِ مُسْتَقْبِلَ الْقِبْلَةِ»), spanmound
 * (the grave raised a hand-span; a foot on it struck out: «قَدْرَ شِبْرٍ», «وَتَحْرُمُ إِهَانَةُ الْقُبُورِ بِالْمَشْيِ عَلَيْهَا»), standdua (standing by
 * the grave with raised hands: «وَسَلُوا لَهُ التَّثْبِيتَ»), taziya (one lays a hand on another's shoulder: «إِنَّ لِلَّهِ مَا أَخَذَ، وَلَهُ مَا أَعْطَى»,
 * «فَلْتَصْبِرْ وَلْتَحْتَسِبْ»).
 */
internal fun DrawScope.burialScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "hastenbier" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "lahdniche" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "spanmound" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
        "standdua" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "taziya" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { burialDraw(a) }
}

private fun DrawScope.burialDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.6f
    nightSky(t, Color(0xFF14182A), Color(0xFF4A4A6A), ground, starAlpha = 0.4f)
    drawRect(Color(0xFF3A3026), Offset(0f, ground), Size(w, h - ground))
    val hb = fqIn(a, "hastenbier", "lahdniche")
    val ln = fqIn(a, "lahdniche", "spanmound")
    val sm = fqIn(a, "spanmound", "standdua")
    val sd = fqIn(a, "standdua", "taziya")
    val tz = fqIn(a, "taziya")
    val person = Color(0xFFE8DCC8)
    if (hb > 0f) {
        val p = a.b("hastenbier")
        val x = mix(w * 0.95f, w * 0.35f, easeInOut(seg(p, 0f, 1f)))
        for (k in 0 until 4) figure(Offset(x + w * (0.1f * (k % 2) - 0.05f) + (k / 2) * w * 0.04f, ground + h * 0.12f + (k / 2) * h * 0.04f), h * 0.26f, person, hb, walk = t * 9f, arms = 0.2f)
        bier85(Offset(x, ground - h * 0.06f), w * 0.26f, hb)
        shroud85(Offset(x - w * 0.12f, ground - h * 0.09f), w * 0.24f, hb, layers = 1)
        tagAt(a.tag("hastenbier"), Offset(w * 0.5f, h * 0.12f), hb * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ln > 0f) {
        val p = a.b("lahdniche")
        val pit = Offset(w * 0.55f, ground)
        val dw = w * 0.22f
        val dh = h * 0.32f * easeOut(seg(p, 0.05f, 0.3f))
        drawRect(Color(0xFF1A140E), Offset(pit.x - dw / 2f, pit.y), Size(dw, dh), alpha = ln)
        val niche = seg(p, 0.3f, 0.5f)
        drawRect(Color(0xFF1A140E), Offset(pit.x - dw / 2f - w * 0.14f * niche, pit.y + dh - h * 0.09f), Size(w * 0.14f * niche, h * 0.09f), alpha = ln)
        kaaba29(Offset(w * 0.12f, ground - h * 0.02f), w * 0.05f, ln * niche)
        drawLine(SalC.Gold, Offset(w * 0.18f, ground - h * 0.04f), Offset(w * 0.3f, ground - h * 0.04f), 2f * dp, alpha = ln * niche, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        val lay = seg(p, 0.55f, 0.75f)
        shroud85(Offset(pit.x - dw / 2f - w * 0.13f, pit.y + dh - h * 0.045f), w * 0.2f, ln * lay, layers = 1)
        tagAt(a.tag("lahdniche", 0, 0), Offset(w * 0.55f, h * 0.15f), ln * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("lahdniche", 0, 1), Offset(w * 0.55f, h * 0.3f), ln * seg(p, 0.65f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val mound = { alpha: Float ->
        drawArc(Color(0xFF6A5A48), 180f, 180f, true, Offset(w * 0.35f, ground - h * 0.06f), Size(w * 0.3f, h * 0.12f), alpha = alpha)
        drawRect(Color(0xFF8A8478), Offset(w * 0.355f, ground - h * 0.1f), Size(w * 0.015f, h * 0.06f), alpha = alpha)
    }
    if (sm > 0f) {
        val p = a.b("spanmound")
        mound(sm)
        val g = seg(p, 0.1f, 0.3f)
        drawLine(SalC.Gold, Offset(w * 0.7f, ground), Offset(w * 0.7f, ground - h * 0.06f * g), 3f * dp, StrokeCap.Round, alpha = sm)
        hand76Shim(Offset(w * 0.74f, ground - h * 0.03f), h * 0.07f, sm * g)
        tagAt(a.tag("spanmound", 0, 0), Offset(w * 0.75f, h * 0.36f), sm * g, back = Fq.Back, ink = ArtInk.Gold)
        val step = seg(p, 0.5f, 0.65f)
        foot76(Offset(w * 0.5f, ground - h * 0.08f), h * 0.07f, Color(0xFFC0503A), sm * step, right = true)
        cross14(Offset(w * 0.5f, ground - h * 0.2f), w * 0.03f, seg(p, 0.6f, 0.75f))
        tagAt(a.tag("spanmound", 0, 1), Offset(w * 0.5f, h * 0.15f), sm * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (sd > 0f) {
        val p = a.b("standdua")
        mound(sd)
        for (k in 0 until 3) {
            val foot = Offset(w * (0.72f + 0.09f * k), ground + h * 0.06f)
            figure(foot, h * 0.26f, person, sd)
            hands12(foot + Offset(0f, -h * 0.22f), h * 0.07f, person, sd * seg(p, 0.1f, 0.25f))
        }
        val u = seg(p, 0.3f, 0.9f)
        softGlow(Offset(w * 0.5f, mix(ground - h * 0.08f, h * 0.05f, u)), w * 0.03f, SalC.Gold, sd * bump(u))
        tagAt(a.tag("standdua"), Offset(w * 0.45f, h * 0.15f), sd * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tz > 0f) {
        val p = a.b("taziya")
        val a0 = Offset(w * 0.42f, ground + h * 0.2f)
        val b0 = Offset(w * 0.58f, ground + h * 0.2f)
        figure(a0, h * 0.3f, person, tz, arms = 0.9f * seg(p, 0.1f, 0.3f))
        figure(b0, h * 0.3f, Color(0xFFB8B0A0), tz, lean = -0.1f)
        softGlow(Offset(w * 0.5f, ground - h * 0.02f), w * 0.1f, SalC.Gold, 0.5f * tz * seg(p, 0.3f, 0.5f))
        tagAt(a.tag("taziya", 0, 0), Offset(w * 0.5f, h * 0.12f), tz * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("taziya", 0, 1), Offset(w * 0.5f, h * 0.26f), tz * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

/** A flat hand held level, measuring a span (shim for the mound's height). */
private fun DrawScope.hand76Shim(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Color(0xFFD8B898), Offset(c.x - s * 0.5f, c.y - s * 0.12f), Size(s, s * 0.24f), CornerRadius(s * 0.1f), alpha = alpha)
}
