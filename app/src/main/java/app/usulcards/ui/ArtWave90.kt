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
 * Wave 90 (2026-11-04): v1.4, the Quran 5: al-Duha, al-Layl, al-Shams, then from al-Balad: al-Balad, al-Fajr. The soul is
 * a moving light inside a faceless figure; without it the figure is a grey statue.
 */

// ═════════════ 21. tf-duha: your Lord has not left you ═════════════

/**
 * Beats: notleft (morning rises; two words, «تَرَكَ» and «أَبْغَضَهُ», are struck out and the light grows: «ثُبُوتِ ضِدِّهِ»),
 * perfectstate (his ﷺ named light climbs a stair of gold, step after step: «مَحَبَّةُ اللَّهِ لَهُ وَاسْتِمْرَارُهَا», «وَتَرْقِيَتُهُ
 * فِي دَرَجِ الْكَمَالِ»), twoaskers (two come to an open door, one asking for money, one for knowledge, and the light takes
 * both in: «السَّائِلُ لِلْمَالِ», «وَالسَّائِلُ لِلْعِلْمِ»), teacherkind (a teacher and a student sit over a book; a warm heart
 * between them: «بِحُسْنِ الْخُلُقِ مَعَ الْمُتَعَلِّمِ», «بِالْإِكْرَامِ وَالتَّحَنُّنِ عَلَيْهِ»).
 */
internal fun DrawScope.duhamorningScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "notleft" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "perfectstate" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "twoaskers" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "teacherkind" to Shot(Offset(w * 0.5f, h * 0.58f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { duhaDraw(a) }
}

private fun DrawScope.duhaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val nl = fqIn(a, "notleft", "perfectstate")
    val ps = fqIn(a, "perfectstate", "twoaskers")
    val ta = fqIn(a, "twoaskers", "teacherkind")
    val tk = fqIn(a, "teacherkind")
    val ground = h * 0.84f
    val morning = seg(a.b("notleft"), 0.3f, 0.9f).coerceAtLeast(if (ps + ta + tk > 0f) 1f else 0f)
    daySky12(lerpColor(Color(0xFF3A4A7A), Color(0xFF8FC1E3), morning), lerpColor(Color(0xFFD89A7A), Color(0xFFF6E6C4), morning), ground, Color(0xFFB8A27A))
    sun12(Offset(w * 0.82f, mix(ground, h * 0.2f, morning)), w * 0.035f, 1f)
    if (nl > 0f) {
        val p = a.b("notleft")
        for (k in 0 until 2) struckTag21(a.tag("notleft", 0, k), Offset(w * (0.62f - 0.24f * k), h * 0.3f), nl * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k), seg(p, 0.3f + 0.1f * k, 0.5f + 0.1f * k), Color(0xFFC0463A), back = Fq.Back, ink = ArtInk.Ivory)
        softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.3f * morning, Qn.GoldLight, 0.5f * nl)
        tagAt(a.tag("notleft", 0, 2), Offset(w * 0.5f, h * 0.56f), nl * seg(p, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ps > 0f) {
        val p = a.b("perfectstate")
        // a stair of gold, and his named light climbing it
        for (k in 0 until 5) {
            val f = seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k)
            drawRect(Qn.Gold, Offset(w * (0.2f + 0.12f * k), ground - h * (0.1f + 0.11f * k)), Size(w * 0.12f, h * 0.025f), alpha = ps * f)
        }
        val climb = easeInOut(seg(p, 0.3f, 0.9f))
        val step = climb * 4f
        val c = Offset(w * (0.26f + 0.12f * step), ground - h * (0.18f + 0.11f * step))
        sirajMunir(c, w * 0.018f, ps * seg(p, 0.2f, 0.3f), t, nameAt = c + Offset(-w * 0.14f, -h * 0.02f))
        tagAt(a.tag("perfectstate", 0, 0), Offset(w * 0.72f, h * 0.72f), ps * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("perfectstate", 0, 1), Offset(w * 0.24f, h * 0.2f), ps * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ta > 0f) {
        val p = a.b("twoaskers")
        drawRect(Color(0xFFF6E6C4), alpha = 0.5f * ta)
        val door = Offset(w * 0.5f, ground)
        door12(door, w * 0.16f, h * 0.5f, 1f, Color(0xFF8A6A40), Color(0xFF4A3420), ta)
        val icons = listOf("coin", "book")
        for (k in 0 until 2) {
            val side = if (k == 0) 1f else -1f
            val come = easeInOut(seg(p, 0.1f + 0.2f * k, 0.5f + 0.2f * k))
            val foot = Offset(door.x + side * w * mix(0.42f, 0.12f, come), ground)
            figure(foot, h * 0.24f, Color(0xFF3A3040), ta, walk = if (come in 0.01f..0.99f) t * 6f + k else 0f)
            glyphCard(Offset(door.x + side * w * 0.32f, h * 0.3f), w * 0.12f, a.tag("twoaskers", 0, k), ta * seg(p, 0.15f + 0.25f * k, 0.3f + 0.25f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
        }
        softGlow(door + Offset(0f, -h * 0.2f), w * 0.15f, Qn.GoldLight, 0.5f * ta * seg(p, 0.5f, 0.8f))
    }
    if (tk > 0f) {
        val p = a.b("teacherkind")
        drawRect(Color(0xFF3A2E28), alpha = 0.8f * tk)
        val book = Offset(w * 0.5f, ground - h * 0.12f)
        sit12(Offset(w * 0.32f, ground), h * 0.3f, Qn.Person, tk, 1f)
        sit12(Offset(w * 0.68f, ground), h * 0.26f, Color(0xFFC8BCA8), tk, -1f)
        openBook21(book, w * 0.14f, Qn.Parch, Qn.Ink, tk)
        val warm = seg(p, 0.2f, 0.5f)
        heart12(Offset(w * 0.5f, h * 0.42f), w * 0.06f, Qn.Rose, tk * warm, fill = true)
        softGlow(Offset(w * 0.5f, h * 0.42f), w * 0.12f, Qn.GoldLight, 0.5f * warm * tk)
        tagAt(a.tag("teacherkind", 0, 0), Offset(w * 0.5f, h * 0.14f), tk * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("teacherkind", 0, 1), Offset(w * 0.5f, h * 0.26f), tk * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 22. tf-layl: whoever gives and fears ═════════════

/**
 * Beats: gaveguarded (three cards, giving, guarding, believing, and a smooth road of light opens ahead: «لِلْيُسْرَى»),
 * husnameaning (the words «لَا إِلَهَ إِلَّا اللَّهُ» high and shining), easeall (on the road, doors of good open on their own and
 * dark stones roll aside: «كُلَّ خَيْرٍ», «تَرْكَ كُلِّ شَرٍّ»), forhisface (one gives money to another; the line of a favour owed
 * back is crossed out, and the gift goes up as light: «لَا لِنِعْمَةٍ لِأَحَدٍ عِنْدَهُ», «ابْتِغَاءَ وَجْهِ رَبِّهِ الْأَعْلَى»).
 */
internal fun DrawScope.layleasyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "gaveguarded" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "husnameaning" to Shot(Offset(w * 0.5f, h * 0.4f), 1.05f),
        "easeall" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "forhisface" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { laylDraw(a) }
}

private fun DrawScope.laylDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val gg = fqIn(a, "gaveguarded", "husnameaning")
    val hm = fqIn(a, "husnameaning", "easeall")
    val ea = fqIn(a, "easeall", "forhisface")
    val fh = fqIn(a, "forhisface")
    deepSky(t, h * 0.8f, Color(0xFF14183A), Color(0xFF3A3A6A), glow = Color(0xFF8A7AAA))
    val ground = h * 0.84f
    drawRect(Color(0xFF2A2638), Offset(0f, ground), Size(w, h - ground))
    // the road of ease: from the near right to the far left, smooth and lit
    val roadA = (gg * seg(a.b("gaveguarded"), 0.6f, 0.9f)) + ea
    if (roadA > 0f) {
        val r = roadA.coerceAtMost(1f)
        drawPath(Path().apply { moveTo(w * 0.62f, h); lineTo(w * 0.98f, h); lineTo(w * 0.22f, ground); lineTo(w * 0.16f, ground); close() }, Qn.ParchLow, alpha = r)
        softGlow(Offset(w * 0.19f, ground), w * 0.12f, Qn.GoldLight, 0.8f * r)
    }
    if (gg > 0f) {
        val p = a.b("gaveguarded")
        val icons = listOf("coin", "lock", "heart")
        for (k in 0 until 3) glyphCard(Offset(w * (0.78f - 0.24f * k), h * 0.3f), w * 0.12f, a.tag("gaveguarded", 0, k), gg * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k)) { at, s, al ->
            if (k == 1) { // a shield: guarding oneself
                drawPath(Path().apply { moveTo(at.x, at.y - s * 0.45f); lineTo(at.x + s * 0.36f, at.y - s * 0.3f); lineTo(at.x + s * 0.3f, at.y + s * 0.15f); quadraticTo(at.x + s * 0.2f, at.y + s * 0.38f, at.x, at.y + s * 0.48f); quadraticTo(at.x - s * 0.2f, at.y + s * 0.38f, at.x - s * 0.3f, at.y + s * 0.15f); lineTo(at.x - s * 0.36f, at.y - s * 0.3f); close() }, Qn.GoldLight, alpha = al, style = Stroke(2f * density))
            } else qIcon(icons[k], at, s, Qn.GoldLight, al)
        }
        tagAt(a.tag("gaveguarded", 0, 3), Offset(w * 0.24f, h * 0.7f), gg * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (hm > 0f) {
        val p = a.b("husnameaning")
        drawRect(Color(0xFF0E1030), alpha = 0.6f * hm)
        qFit(a.big("husnameaning", 0, 0), Offset(w * 0.5f, h * 0.3f), w * 0.7f, h * 0.18f, Color(0xFFFFF4D2), hm * seg(p, 0.05f, 0.3f), glow = Qn.Gold)
        tagAt(a.tag("husnameaning", 0, 1), Offset(w * 0.5f, h * 0.5f), hm * seg(p, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ea > 0f) {
        val p = a.b("easeall")
        val walk = easeInOut(seg(p, 0.05f, 0.95f))
        val foot = Offset(mix(w * 0.78f, w * 0.3f, walk), mix(h * 0.96f, ground + h * 0.02f, walk))
        figure(foot, h * mix(0.26f, 0.14f, walk), Qn.Person, ea, walk = t * 6f)
        // doors of good open by themselves as he comes
        for (k in 0 until 3) {
            val x = w * (0.7f - 0.17f * k)
            val open = seg(walk, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            door12(Offset(x, ground - h * 0.02f), w * 0.07f, h * 0.2f, open, Color(0xFF8A6A40), Color(0xFF4A3420), ea)
        }
        // and dark stones roll off the road
        for (k in 0 until 3) {
            val off = seg(walk, 0.15f + 0.25f * k, 0.35f + 0.25f * k)
            val c = Offset(w * (0.62f - 0.15f * k) + w * 0.2f * off, ground + h * (0.08f - 0.02f * k) + h * 0.06f * off)
            drawCircle(Color(0xFF1A1620), h * 0.03f, c, alpha = ea * (1f - 0.6f * off))
        }
        tagAt(a.tag("easeall", 0, 0), Offset(w * 0.5f, h * 0.4f), ea * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("easeall", 0, 1), Offset(w * 0.82f, h * 0.7f), ea * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (fh > 0f) {
        val p = a.b("forhisface")
        drawRect(Color(0xFF14183A), alpha = 0.7f * fh)
        val giver = Offset(w * 0.62f, ground)
        val taker = Offset(w * 0.36f, ground)
        figure(giver, h * 0.3f, Qn.Person, fh, arms = 0.6f)
        figure(taker, h * 0.28f, Color(0xFF9A90A8), fh, arms = 0.5f)
        val give = easeInOut(seg(p, 0.05f, 0.3f))
        coin12(Offset(mix(giver.x - w * 0.04f, taker.x + w * 0.05f, give), ground - h * 0.18f), w * 0.018f, fh * (1f - seg(p, 0.6f, 0.7f)))
        // no favour owed back: the line from him back to the giver is crossed out
        val back = seg(p, 0.3f, 0.45f)
        drawLine(Color(0xFF9A90A8), taker + Offset(0f, -h * 0.32f), taker + Offset(w * 0.26f * back, -h * 0.32f), 1.5f * density, alpha = fh * back, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(4f * density, 4f * density)))
        if (back >= 1f) cross14(Offset(w * 0.49f, h * 0.52f), h * 0.05f, seg(p, 0.45f, 0.55f))
        tagAt(a.tag("forhisface", 0, 0), Offset(w * 0.49f, h * 0.42f), fh * seg(p, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
        // the gift goes up as light
        val up = easeOut(seg(p, 0.6f, 0.9f))
        qBeam(giver + Offset(0f, -h * 0.3f), Offset(w * 0.62f, h * 0.12f), up, Qn.GoldLight, fh, 2f * density)
        tagAt(a.tag("forhisface", 0, 1), Offset(w * 0.5f, h * 0.1f), fh * seg(p, 0.75f, 0.9f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 23. tf-shams: the soul, a great sign ═════════════

/**
 * Beats: subtlesoul (inside a faceless figure a light that never keeps still, shifting and changing colour: «فِي غَايَةِ
 * اللُّطْفِ وَالْخِفَّةِ», «سَرِيعَةُ التَّنَقُّلِ وَالتَّغَيُّرِ»), innerlife (five cards round him, what is in the soul; then the light
 * goes out and he is a grey statue: «مُجَرَّدَ تِمْثَالٍ»), nafsroads (a fork: a road of light going up and a dark road going down:
 * «طَرِيقَ الْخَيْرِ», «وَطَرِيقَ الشَّرِّ»), purifiedfail (two hearts: one polished bright, one buried under dark layers:
 * «أَفْلَحَ مَنْ زَكَّاهَا بِالطَّاعَةِ», «وَخَابَ مَنْ أَخْفَاهَا بِالْمَعَاصِي»).
 */
internal fun DrawScope.nafsmirrorScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "subtlesoul" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "innerlife" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "nafsroads" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "purifiedfail" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { nafsDraw(a) }
}

private fun DrawScope.nafsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ss = fqIn(a, "subtlesoul", "innerlife")
    val il = fqIn(a, "innerlife", "nafsroads")
    val tw = fqIn(a, "nafsroads", "purifiedfail")
    val pf = fqIn(a, "purifiedfail")
    drawRect(Brush.radialGradient(listOf(Color(0xFF2A2A4A), Color(0xFF0E0E20)), Offset(w * 0.5f, h * 0.45f), w * 0.7f))
    val foot = Offset(w * 0.5f, h * 0.9f)
    val tall = h * 0.62f
    val body = ss + il
    if (body > 0f) {
        val b = body.coerceAtMost(1f)
        val stone = seg(a.b("innerlife"), 0.65f, 0.85f)
        figure(foot, tall, lerpColor(Color(0xFF6A6488), Color(0xFF9A968E), stone), b)
        // the soul: a light that never keeps still
        val chest = foot + Offset(0f, -tall * 0.62f)
        val live = (1f - stone) * b
        for (k in 0 until 3) {
            val an = t * (2.2f + k) + k * 2f
            val c = chest + Offset(cos(an) * tall * 0.05f, sin(an * 1.3f) * tall * 0.06f)
            val col = lerpColor(Qn.GoldLight, if (k == 1) Qn.Rose else Fq.Water, 0.5f + 0.5f * sin(t * 1.7f + k))
            softGlow(c, tall * 0.08f, col, 0.7f * live)
            drawCircle(col, tall * 0.02f, c, alpha = live)
        }
    }
    if (ss > 0f) {
        val p = a.b("subtlesoul")
        tagAt(a.tag("subtlesoul", 0, 0), Offset(w * 0.22f, h * 0.3f), ss * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("subtlesoul", 0, 1), Offset(w * 0.78f, h * 0.5f), ss * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (il > 0f) {
        val p = a.b("innerlife")
        val icons = listOf("star", "key", "pen", "heart", "cross")
        val spots = listOf(Offset(0.84f, 0.2f), Offset(0.86f, 0.52f), Offset(0.5f, 0.12f), Offset(0.14f, 0.52f), Offset(0.16f, 0.2f))
        for (k in 0 until 5) glyphCard(Offset(w * spots[k].x, h * spots[k].y), w * 0.1f, a.tag("innerlife", 0, k), il * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k) * (1f - 0.6f * seg(p, 0.65f, 0.85f))) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
        tagAt(a.tag("innerlife", 0, 5), Offset(w * 0.5f, h * 0.36f), il * seg(p, 0.75f, 0.9f), back = Color(0xE64A4440), ink = ArtInk.Ivory)
    }
    if (tw > 0f) {
        val p = a.b("nafsroads")
        val base = Offset(w * 0.5f, h * 0.92f)
        figure(base, h * 0.2f, Qn.Person, tw)
        val up = easeOut(seg(p, 0.05f, 0.4f))
        val down = easeOut(seg(p, 0.4f, 0.75f))
        drawLine(Qn.GoldLight, base + Offset(0f, -h * 0.04f), base + Offset(-w * 0.35f * up, -h * 0.7f * up), w * 0.03f, StrokeCap.Round, alpha = tw * 0.9f)
        softGlow(base + Offset(-w * 0.35f, -h * 0.7f), w * 0.1f, Qn.GoldLight, tw * up)
        drawLine(Color(0xFF2A1E28), base + Offset(0f, -h * 0.04f), base + Offset(w * 0.4f * down, h * 0.06f * down - h * 0.04f), w * 0.03f, StrokeCap.Round, alpha = tw)
        tagAt(a.tag("nafsroads", 0, 0), Offset(w * 0.22f, h * 0.12f), tw * seg(p, 0.25f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("nafsroads", 0, 1), Offset(w * 0.84f, h * 0.76f), tw * seg(p, 0.6f, 0.75f), back = Color(0xE6401818), ink = ArtInk.Ivory)
    }
    if (pf > 0f) {
        val p = a.b("purifiedfail")
        drawRect(Color(0xFF0E0E20), alpha = 0.7f * pf)
        val good = Offset(w * 0.72f, h * 0.48f)
        val bad = Offset(w * 0.28f, h * 0.48f)
        val shine = seg(p, 0.1f, 0.4f)
        softGlow(good, w * 0.14f, Qn.GoldLight, pf * shine)
        heart12(good, w * 0.16f, lerpColor(Color(0xFF8A6A70), Color(0xFFE0566A), shine), pf, fill = true)
        glint(good + Offset(-w * 0.03f, -h * 0.04f), w * 0.04f, Color.White, pf * bump(shine))
        heart12(bad, w * 0.16f, Color(0xFF8A6A70), pf * seg(p, 0.4f, 0.5f), fill = true)
        val cover = seg(p, 0.5f, 0.85f)
        for (k in 0 until 4) drawOval(Color(0xFF1A141C), Offset(bad.x - w * 0.09f + k * w * 0.02f, bad.y - h * 0.08f + k * h * 0.03f), Size(w * 0.12f, h * 0.06f), alpha = pf * seg(cover, k * 0.2f, k * 0.2f + 0.3f))
        tagAt(a.tag("purifiedfail", 0, 0), good + Offset(0f, h * 0.22f), pf * seg(p, 0.25f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("purifiedfail", 0, 1), bad + Offset(0f, h * 0.22f), pf * seg(p, 0.65f, 0.8f), back = Color(0xE6401818), ink = ArtInk.Ivory)
    }
}

// ═════════════ 24. tf-balad: man made to toil ═════════════

/**
 * Beats: toil (one climbs a steep, rough road with a load on his back, past three stations: this world, the barzakh, the
 * day the witnesses stand), restwork (his load turns to light and he reaches shade and light at the top: «يُرِيحُهُ مِنْ هَذِهِ
 * الشَّدَائِدِ», «الْفَرَحَ وَالسُّرُورَ الدَّائِمَ»), wasted (coins thrown into a dark pit go out like sparks: «إِهْلَاكًا», «لَا يَنْتَفِعُ
 * بِمَا أَنْفَقَ»), tradedgod (a coin is planted and grows into a plant hung with coins, many times more: «تَاجَرَ مَعَ اللَّهِ»,
 * «وَرَبِحَ أَضْعَافَ أَضْعَافِ مَا أَنْفَقَ»).
 */
internal fun DrawScope.kabadScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "toil" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "restwork" to Shot(Offset(w * 0.4f, h * 0.4f), 1.1f),
        "wasted" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "tradedgod" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { kabadDraw(a) }
}

private fun DrawScope.kabadDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val to = fqIn(a, "toil", "restwork")
    val rw = fqIn(a, "restwork", "wasted")
    val ws = fqIn(a, "wasted", "tradedgod")
    val tg = fqIn(a, "tradedgod")
    daySky12(Color(0xFFC8A880), Color(0xFFF0D8B0), h * 0.9f, Color(0xFF8A6A4A))
    // a steep rough road from the bottom right up to the top left
    val pts = listOf(Offset(0.95f, 0.92f), Offset(0.7f, 0.8f), Offset(0.78f, 0.62f), Offset(0.48f, 0.52f), Offset(0.56f, 0.34f), Offset(0.24f, 0.24f))
    val climbA = to + rw
    if (climbA > 0f) {
        val c = climbA.coerceAtMost(1f)
        drawPath(Path().apply { moveTo(w * 0.3f, h); lineTo(w * 0.16f, h * 0.24f); lineTo(w * 0.32f, h * 0.2f); lineTo(w, h * 0.7f); lineTo(w, h); close() }, Color(0xFF9A7A5A), alpha = c)
        for (k in 0 until pts.size - 1) drawLine(Color(0xFF6A4A30), Offset(w * pts[k].x, h * pts[k].y), Offset(w * pts[k + 1].x, h * pts[k + 1].y), 4f * density, StrokeCap.Round, alpha = c)
        for (k in 0 until 8) drawCircle(Color(0xFF6A5040), w * 0.012f, Offset(w * (0.35f + 0.08f * k), h * (0.3f + 0.08f * (k % 4))), alpha = c * 0.7f)
    }
    fun along(u: Float): Offset {
        val x = u * (pts.size - 1)
        val i = x.toInt().coerceAtMost(pts.size - 2)
        val f = x - i
        return Offset(w * mix(pts[i].x, pts[i + 1].x, f), h * mix(pts[i].y, pts[i + 1].y, f))
    }
    if (to > 0f) {
        val p = a.b("toil")
        val u = easeInOut(seg(p, 0.05f, 0.9f)) * 0.7f
        val foot = along(u)
        figure(foot, h * 0.16f, Color(0xFF3A3040), to, walk = t * 4f, lean = -0.3f)
        drawRoundRect(Color(0xFF6A5040), Offset(foot.x + w * 0.005f, foot.y - h * 0.15f), Size(w * 0.04f, h * 0.06f), CornerRadius(3f * density), alpha = to)
        val icons = listOf("sun", "tree", "scale")
        val stations = listOf(0.1f, 0.4f, 0.7f)
        for (k in 0 until 3) {
            val st = along(stations[k])
            glyphCard(st + Offset(w * 0.12f, -h * 0.1f), w * 0.1f, a.tag("toil", 0, k), to * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k)) { at, s, al ->
                if (k == 1) fqMound(at + Offset(0f, s * 0.3f), s * 0.8f, s * 0.3f, Color(0xFF8A7A62), al) else qIcon(icons[k], at, s, Qn.GoldLight, al)
            }
        }
    }
    if (rw > 0f) {
        val p = a.b("restwork")
        val top = along(1f)
        val u = 0.7f + 0.3f * easeInOut(seg(p, 0.05f, 0.5f))
        val foot = along(u)
        figure(foot, h * 0.16f, Qn.Person, rw, walk = if (u < 1f) t * 4f else 0f)
        val lit = seg(p, 0.3f, 0.6f)
        softGlow(foot + Offset(w * 0.02f, -h * 0.12f), w * 0.05f, Qn.GoldLight, rw * lit)
        palm12(top + Offset(-w * 0.08f, 0f), h * 0.3f, Color(0xFF6A4A30), Qn.Leaf, rw, t)
        softGlow(top + Offset(0f, -h * 0.1f), w * 0.2f, Qn.GoldLight, 0.6f * rw * seg(p, 0.5f, 0.8f))
        tagAt(a.tag("restwork", 0, 0), Offset(w * 0.62f, h * 0.12f), rw * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("restwork", 0, 1), Offset(w * 0.62f, h * 0.24f), rw * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ws > 0f) {
        val p = a.b("wasted")
        drawRect(Color(0xFF2A1E1A), alpha = 0.85f * ws)
        val pit = Offset(w * 0.4f, h * 0.8f)
        drawOval(Color(0xFF0A0606), Offset(pit.x - w * 0.14f, pit.y - h * 0.05f), Size(w * 0.28f, h * 0.1f), alpha = ws)
        for (k in 0 until 6) {
            val f = seg(p, 0.1f + 0.08f * k, 0.35f + 0.08f * k)
            val c = Offset(mix(w * 0.8f, pit.x + (k - 2.5f) * w * 0.03f, f), mix(h * 0.4f, pit.y, f) - h * 0.15f * bump(f))
            coin12(c, w * 0.016f, ws * (1f - seg(f, 0.85f, 1f)))
            if (f >= 1f) glint(pit + Offset((k - 2.5f) * w * 0.03f, -h * 0.02f), w * 0.015f, Qn.Ember, ws * 0.5f * (1f - seg(p, 0.8f, 1f)))
        }
        tagAt(a.tag("wasted", 0, 0), Offset(w * 0.4f, h * 0.6f), ws * seg(p, 0.35f, 0.5f), back = Color(0xE6401818), ink = ArtInk.Ivory)
        tagAt(a.tag("wasted", 0, 1), Offset(w * 0.5f, h * 0.16f), ws * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tg > 0f) {
        val p = a.b("tradedgod")
        drawRect(Color(0xFF2A3A2A), alpha = 0.85f * tg)
        val base = Offset(w * 0.5f, h * 0.86f)
        val sow = seg(p, 0.05f, 0.2f)
        coin12(Offset(base.x, mix(h * 0.5f, base.y, sow)), w * 0.018f, tg * (1f - seg(p, 0.2f, 0.25f)))
        val grow = easeOut(seg(p, 0.25f, 0.7f))
        qPlant(base, h * 0.55f, grow, tg, t)
        for (k in 0 until 10) {
            val f = seg(p, 0.5f + 0.03f * k, 0.6f + 0.03f * k)
            val an = -PI.toFloat() / 2f + (k - 4.5f) * 0.3f
            coin12(base + Offset(cos(an) * w * 0.16f, -h * 0.35f + sin(an) * h * 0.18f), w * 0.015f, tg * f)
        }
        softGlow(base + Offset(0f, -h * 0.4f), w * 0.2f, Qn.GoldLight, 0.4f * tg * seg(p, 0.6f, 0.9f))
        tagAt(a.tag("tradedgod", 0, 0), Offset(w * 0.5f, h * 0.1f), tg * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("tradedgod", 0, 1), Offset(w * 0.5f, h * 0.22f), tg * seg(p, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 25. tf-fajr: the dawn and ten nights ═════════════

/**
 * Beats: dawnoath (the night draws off to one side and day comes in from the other, over a small mosque at the dawn
 * prayer: «بِالْفَجْرِ», «إِدْبَارِ اللَّيْلِ وَإِقْبَالِ النَّهَارِ», «صَلَاةٌ فَاضِلَةٌ»), tennights (ten nights in a row, and the two
 * readings beside them: the last ten of Ramadan, the first ten of Dhu al-Hijja), lifeafter (a short bar of this life and,
 * beside it, a bar that runs on into light: «﴿يَا لَيْتَنِي قَدَّمْتُ لِحَيَاتِي﴾», «حَيَاةُ الْآخِرَةِ»), besttwo (two cards light up:
 * the Night of Decree, the standing at 'Arafa).
 */
internal fun DrawScope.fajrtenScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "dawnoath" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "tennights" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "lifeafter" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "besttwo" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { fajrDraw(a) }
}

private fun DrawScope.fajrDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dn = fqIn(a, "dawnoath", "tennights")
    val tn = fqIn(a, "tennights", "lifeafter")
    val la = fqIn(a, "lifeafter", "besttwo")
    val bt = fqIn(a, "besttwo")
    val ground = h * 0.82f
    // the night withdraws to the left as the day comes in from the right
    val day = easeInOut(seg(a.b("dawnoath"), 0.1f, 0.8f)).coerceAtLeast(if (tn + la + bt > 0f) 1f else 0f)
    drawRect(Brush.horizontalGradient(listOf(Color(0xFF0E1430), Color(0xFF0E1430), lerpColor(Color(0xFF0E1430), Color(0xFFF2B27A), day)), 0f, w))
    drawRect(Brush.horizontalGradient(listOf(Color(0x000E1430), Color(0x000E1430), lerpColor(Color(0x00F6D2A0), Color(0xCCF6D2A0), day)), w * (1f - day), w))
    drawRect(Color(0xFF1E1A24), Offset(0f, ground), Size(w, h - ground))
    if (dn > 0f) {
        val p = a.b("dawnoath")
        drawLine(Color(0xFFFFE2B0), Offset(w * (1f - 0.9f * day), ground), Offset(w, ground), 2.5f * density, alpha = dn)
        mosque12(Offset(w * 0.62f, ground), w * 0.16f, Color(0xFFE8DCC0), Qn.Gold, dn, lit = seg(p, 0.5f, 0.7f))
        for (k in 0 until 3) figure(Offset(w * (0.74f + 0.04f * k), ground), h * 0.1f, Color(0xFF2A2430), dn * seg(p, 0.55f + 0.05f * k, 0.65f + 0.05f * k), walk = t * 5f + k)
        qFit(a.big("dawnoath", 0, 0), Offset(w * 0.75f, h * 0.18f), w * 0.3f, h * 0.13f, Color(0xFFFFF4D2), dn * seg(p, 0.05f, 0.2f), glow = Qn.Gold)
        tagAt(a.tag("dawnoath", 0, 1), Offset(w * 0.3f, h * 0.4f), dn * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("dawnoath", 0, 2), Offset(w * 0.62f, h * 0.48f), dn * seg(p, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tn > 0f) {
        val p = a.b("tennights")
        drawRect(Color(0xFF0E1430), alpha = 0.8f * tn)
        tagAt(a.tag("tennights", 0, 0), Offset(w * 0.5f, h * 0.12f), tn * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        for (k in 0 until 10) {
            val f = seg(p, 0.1f + 0.04f * k, 0.18f + 0.04f * k)
            val c = Offset(w * (0.91f - 0.091f * k), h * 0.36f)
            drawRoundRect(Color(0xFF1C2A4A), Offset(c.x - w * 0.038f, c.y - h * 0.07f), Size(w * 0.076f, h * 0.14f), CornerRadius(4f * density), alpha = tn * f)
            crescent12(c, w * 0.016f, Qn.GoldLight, Color(0xFF1C2A4A), tn * f)
        }
        val icons = listOf("crescent", "kaaba")
        for (k in 0 until 2) glyphCard(Offset(w * (0.7f - 0.4f * k), h * 0.7f), w * 0.15f, a.tag("tennights", 0, k + 1), tn * seg(p, 0.55f + 0.15f * k, 0.7f + 0.15f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
        tagAt(a.tag("tennights", 0, 3), Offset(w * 0.5f, h * 0.7f), tn * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (la > 0f) {
        val p = a.b("lifeafter")
        drawRect(Color(0xFF14182A), alpha = 0.9f * la)
        qFit(a.big("lifeafter", 0, 0), Offset(w * 0.5f, h * 0.2f), w * 0.7f, h * 0.14f, Color(0xFFFFF4D2), la * seg(p, 0.05f, 0.2f))
        // this life: a short bar; the next: one that runs on into light
        val y1 = h * 0.5f
        val y2 = h * 0.7f
        val r1 = easeOut(seg(p, 0.3f, 0.45f))
        drawLine(Color(0xFF8A8494), Offset(w * 0.9f, y1), Offset(w * mix(0.9f, 0.75f, r1), y1), h * 0.04f, StrokeCap.Round, alpha = la)
        val r2 = easeOut(seg(p, 0.45f, 0.9f))
        drawLine(Qn.GoldLight, Offset(w * 0.9f, y2), Offset(w * mix(0.9f, -0.05f, r2), y2), h * 0.04f, StrokeCap.Round, alpha = la)
        softGlow(Offset(w * 0.05f, y2), w * 0.15f, Qn.GoldLight, la * r2)
        tagAt(a.tag("lifeafter", 0, 1), Offset(w * 0.45f, y2 + h * 0.1f), la * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (bt > 0f) {
        val p = a.b("besttwo")
        drawRect(Color(0xFF0E1430), alpha = 0.8f * bt)
        val icons = listOf("star", "mountain")
        for (k in 0 until 2) glyphCard(Offset(w * (0.68f - 0.36f * k), h * 0.5f), w * 0.18f, a.tag("besttwo", 0, k), bt * seg(p, 0.1f + 0.35f * k, 0.3f + 0.35f * k), lit = seg(p, 0.3f + 0.35f * k, 0.5f + 0.35f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
    }
}
