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
import androidx.compose.ui.text.TextLayoutResult
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 93 (2026-11-04): v1.4, the virtues of the Quran and the etiquette of reciting it (eight cards). No figure appears
 * in a scene that speaks of the Prophet ﷺ (only his named light); companions are plain roundels; the prostrations use the
 * corrected pose (forehead on the ground). The fifteen prostrations are written in Western digits.
 */

/** A closed mushaf, standing or lying, centred at [c], [s] tall. */
private fun DrawScope.mushaf93(c: Offset, s: Float, alpha: Float, glow: Float = 0f) {
    if (alpha <= 0.01f) return
    if (glow > 0f) softGlow(c, s * 1.2f, Qn.GoldLight, glow * alpha)
    drawRoundRect(Color(0xFF1E5A4A), Offset(c.x - s * 0.36f, c.y - s * 0.5f), Size(s * 0.72f, s), CornerRadius(s * 0.05f), alpha = alpha)
    drawRoundRect(Qn.Gold, Offset(c.x - s * 0.3f, c.y - s * 0.42f), Size(s * 0.6f, s * 0.84f), CornerRadius(s * 0.04f), alpha = alpha, style = Stroke(s * 0.03f))
    drawPath(starPath(c, s * 0.16f), Qn.Gold, alpha = alpha)
}

/** A plain roundel with a name in it. */
private fun DrawScope.roundel93(c: Offset, r: Float, tl: TextLayoutResult?, alpha: Float, fill: Color = Color(0xFF5A6A7A)) {
    if (alpha <= 0.01f) return
    drawCircle(fill, r, c, alpha = alpha)
    drawCircle(Color(0xFFE8DCC0), r, c, alpha = alpha, style = Stroke(1.5f * density))
    qFit(tl, c, r * 1.7f, r * 1.1f, ArtInk.Ivory, alpha)
}

// ═════════════ 37. fq-shafi: an intercessor for its people ═════════════

/**
 * Beats: manyhadith (an open mushaf in light and scrolls of narrations fanning out from it: «فِي فَضْلِ تِلَاوَةِ الْقُرْآنِ
 * وَالْعِنَايَةِ بِهِ»), intercedes (one who held the book in his life; on the Day it stands before him as a shield of light:
 * «شَفِيعًا لِأَصْحَابِهِ», «الَّذِينَ كَانُوا يَعْمَلُونَ بِهِ»), learnteach (a book passes from a teacher to a student and on to another:
 * «تَعَلَّمَ الْقُرْآنَ», «وَعَلَّمَهُ»), honorall (three cards under a crown of light: learning, teaching, acting on it).
 */
internal fun DrawScope.shafibookScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "manyhadith" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "intercedes" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "learnteach" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "honorall" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { shafiDraw(a) }
}

private fun DrawScope.shafiDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val mh = fqIn(a, "manyhadith", "intercedes")
    val ic = fqIn(a, "intercedes", "learnteach")
    val lt = fqIn(a, "learnteach", "honorall")
    val ho = fqIn(a, "honorall")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF12283A), Color(0xFF1E3A44))))
    val ground = h * 0.86f
    if (mh > 0f) {
        val p = a.b("manyhadith")
        val c = Offset(w * 0.5f, h * 0.55f)
        openBook21(c, w * 0.3f, Qn.Parch, Qn.Ink, mh, glow = 0.7f)
        for (k in 0 until 5) {
            val an = PI.toFloat() * (1.15f + 0.175f * k)
            val f = easeOut(seg(p, 0.15f + 0.1f * k, 0.4f + 0.1f * k))
            scroll14(c + Offset(cos(an) * w * 0.3f * f, sin(an) * h * 0.4f * f), w * 0.07f, h * 0.06f, 1f, mh * f)
        }
        tagAt(a.tag("manyhadith", 0, 0), Offset(w * 0.5f, h * 0.88f), mh * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ic > 0f) {
        val p = a.b("intercedes")
        drawRect(Color(0xFF2A2430), Offset(0f, ground), Size(w, h - ground), alpha = ic)
        val foot = Offset(w * 0.35f, ground)
        figure(foot, h * 0.32f, Qn.Person, ic)
        val rise = easeInOut(seg(p, 0.15f, 0.55f))
        val bk = Offset(mix(foot.x + w * 0.05f, w * 0.58f, rise), mix(ground - h * 0.18f, h * 0.5f, rise))
        mushaf93(bk, mix(h * 0.08f, h * 0.3f, rise), ic, glow = rise)
        if (rise > 0.9f) guardDome12(foot + Offset(w * 0.05f, 0f), w * 0.2f, seg(p, 0.55f, 0.75f), t, ic, Qn.GoldLight)
        tagAt(a.tag("intercedes", 0, 0), Offset(w * 0.6f, h * 0.12f), ic * seg(p, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("intercedes", 0, 1), Offset(w * 0.3f, h * 0.24f), ic * seg(p, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (lt > 0f) {
        val p = a.b("learnteach")
        drawRect(Color(0xFF2A2430), Offset(0f, ground), Size(w, h - ground), alpha = lt)
        val seats = listOf(0.8f, 0.5f, 0.2f)
        for ((k, x) in seats.withIndex()) sit12(Offset(w * x, ground), h * 0.26f, if (k == 0) Qn.Person else Color(0xFFC8BCA8), lt * seg(p, 0.05f * k, 0.1f + 0.05f * k), -1f)
        for (k in 0 until 2) {
            val f = easeInOut(seg(p, 0.15f + 0.4f * k, 0.45f + 0.4f * k))
            val from = Offset(w * seats[k] - w * 0.04f, ground - h * 0.2f)
            val to = Offset(w * seats[k + 1] + w * 0.04f, ground - h * 0.2f)
            mushaf93(Offset(mix(from.x, to.x, f), mix(from.y, to.y, f) - h * 0.08f * bump(f)), h * 0.08f, lt, glow = 0.5f)
        }
        tagAt(a.tag("learnteach", 0, 0), Offset(w * 0.65f, h * 0.2f), lt * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("learnteach", 0, 1), Offset(w * 0.35f, h * 0.34f), lt * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ho > 0f) {
        val p = a.b("honorall")
        qIcon("crown", Offset(w * 0.5f, h * 0.18f), w * 0.14f, Qn.Gold, ho * seg(p, 0.6f, 0.75f))
        softGlow(Offset(w * 0.5f, h * 0.18f), w * 0.1f, Qn.GoldLight, ho * seg(p, 0.6f, 0.8f))
        val icons = listOf("book", "people", "hands")
        for (k in 0 until 3) glyphCard(Offset(w * (0.76f - 0.26f * k), h * 0.56f), w * 0.15f, a.tag("honorall", 0, k), ho * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k), lit = seg(p, 0.6f, 0.8f)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
    }
}

// ═════════════ 38. fq-harf: a good deed for every letter ═════════════

/**
 * Beats: tenfold (a letter read becomes one gold star, and the star becomes ten: «فَلَهُ بِهِ حَسَنَةٌ», «بِعَشْرِ أَمْثَالِهَا»), eachletter
 * (three letter names apart, right to left, «أَلِفٌ», «لَامٌ», «مِيمٌ», each with its own ten stars), harfclimb (a stair of verse rosettes
 * going up; one climbs while reciting, and his place is at the last he reads: «اقْرَأْ وَارْتَقِ وَرَتِّلْ», «فَإِنَّ مَنْزِلَتَكَ عِنْدَ
 * آخِرِ آيَةٍ تَقْرَؤُهَا»).
 */
internal fun DrawScope.harfgoodScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "tenfold" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "eachletter" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "harfclimb" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { harfDraw(a) }
}

private fun DrawScope.harfDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val tf = fqIn(a, "tenfold", "eachletter")
    val el = fqIn(a, "eachletter", "harfclimb")
    val asn = fqIn(a, "harfclimb")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF0E2426), Color(0xFF1E3A3C))))
    if (tf > 0f) {
        val p = a.b("tenfold")
        val one = Offset(w * 0.5f, h * 0.3f)
        val f1 = seg(p, 0.1f, 0.3f)
        drawPath(starPath(one, w * 0.04f), Qn.GoldLight, alpha = tf * f1)
        softGlow(one, w * 0.06f, Qn.GoldLight, tf * f1)
        tagAt(a.tag("tenfold", 0, 0), one + Offset(0f, -h * 0.14f), tf * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        for (k in 0 until 10) {
            val f = easeOut(seg(p, 0.4f + 0.03f * k, 0.6f + 0.03f * k))
            val target = Offset(w * (0.82f - 0.16f * (k % 5)), h * (0.6f + 0.16f * (k / 5)))
            drawPath(starPath(Offset(mix(one.x, target.x, f), mix(one.y, target.y, f)), w * 0.03f), Qn.GoldLight, alpha = tf * f)
        }
        tagAt(a.tag("tenfold", 0, 1), Offset(w * 0.5f, h * 0.48f), tf * seg(p, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (el > 0f) {
        val p = a.b("eachletter")
        for (k in 0 until 3) {
            val x = w * (0.8f - 0.3f * k)     // right to left: alif, lam, mim
            val f = seg(p, 0.05f + 0.25f * k, 0.25f + 0.25f * k)
            qFit(a.big("eachletter", 0, k), Offset(x, h * 0.26f), w * 0.24f, h * 0.16f, Color(0xFFFFF4D2), el * f, glow = Qn.Gold)
            for (m in 0 until 10) drawPath(starPath(Offset(x + w * (0.08f - 0.04f * (m % 5)), h * (0.5f + 0.1f * (m / 5))), w * 0.014f), Qn.GoldLight, alpha = el * seg(p, 0.15f + 0.25f * k + 0.01f * m, 0.2f + 0.25f * k + 0.01f * m))
        }
        tagAt(a.tag("eachletter", 0, 3), Offset(w * 0.5f, h * 0.86f), el * seg(p, 0.8f, 0.95f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (asn > 0f) {
        val p = a.b("harfclimb")
        val steps = 7
        for (k in 0 until steps) {
            val c = Offset(w * (0.82f - 0.1f * k), h * (0.86f - 0.1f * k))
            drawRect(Color(0xFF2E6B66), Offset(c.x - w * 0.05f, c.y), Size(w * 0.1f, h * 0.02f), alpha = asn)
            qRosette(c + Offset(0f, -h * 0.03f), w * 0.016f, asn * seg(p, 0.05f + 0.05f * k, 0.12f + 0.05f * k))
        }
        val climb = easeInOut(seg(p, 0.15f, 0.8f)) * (steps - 1)
        val k = climb.toInt()
        val foot = Offset(w * (0.82f - 0.1f * climb), h * (0.86f - 0.1f * k))
        figure(foot, h * 0.14f, Qn.Person, asn)
        voice12(foot + Offset(-h * 0.02f, -h * 0.14f), w * 0.03f, t, Qn.GoldLight, asn, -1f)
        val last = Offset(w * (0.82f - 0.1f * k), h * (0.86f - 0.1f * k) - h * 0.03f)
        softGlow(last, w * 0.05f, Qn.GoldLight, asn * seg(p, 0.7f, 0.9f))
        tagAt(a.tag("harfclimb", 0, 0), Offset(w * 0.7f, h * 0.14f), asn * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("harfclimb", 0, 1), Offset(w * 0.35f, h * 0.9f), asn * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 39. fq-bayt: the Quran raises people ═════════════

/**
 * Beats: ruined (two houses: one fallen in, dark, nothing inside, «كَالْبَيْتِ الْخَرِبِ»; the other whole and lit with a mushaf in its
 * window), raiselower (a mushaf in the middle; a platform of people lifts into light on one side, another sinks on the other:
 * «يَرْفَعُ بِهَذَا الْكِتَابِ أَقْوَامًا», «وَيَضَعُ بِهِ آخَرِينَ»), mawla (a small roundel, «مَوْلًى», rises to a seat of honour over Makkah,
 * with two cards: a reciter of the Quran, learned in inheritance), notlineage (a balance: the mushaf outweighs a family tree and
 * coins: «فَرِفْعَةُ الْمَرْءِ بِالْقُرْآنِ», «لَا بِنَسَبِهِ وَلَا بِمَالِهِ»).
 */
internal fun DrawScope.ruinhouseScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "ruined" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "raiselower" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "mawla" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "notlineage" to Shot(Offset(w * 0.5f, h * 0.52f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { ruinDraw(a) }
}

private fun DrawScope.ruinDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ru = fqIn(a, "ruined", "raiselower")
    val rl = fqIn(a, "raiselower", "mawla")
    val mw = fqIn(a, "mawla", "notlineage")
    val nl = fqIn(a, "notlineage")
    val ground = h * 0.84f
    daySky12(Color(0xFF6A7A9A), Color(0xFFD8C8A8), ground, Color(0xFF8A7458))
    if (ru > 0f) {
        val p = a.b("ruined")
        // the ruin: broken walls, no roof, dark inside
        val rx = w * 0.28f
        drawPath(Path().apply { moveTo(rx - w * 0.14f, ground); lineTo(rx - w * 0.14f, ground - h * 0.3f); lineTo(rx - w * 0.08f, ground - h * 0.22f); lineTo(rx - w * 0.02f, ground - h * 0.32f); lineTo(rx + w * 0.04f, ground - h * 0.18f); lineTo(rx + w * 0.14f, ground - h * 0.24f); lineTo(rx + w * 0.14f, ground); close() }, Color(0xFF6A5A4A), alpha = ru)
        drawRect(Color(0xFF1A1410), Offset(rx - w * 0.04f, ground - h * 0.14f), Size(w * 0.08f, h * 0.14f), alpha = ru)
        for (k in 0 until 4) drawRect(Color(0xFF5A4A3A), Offset(rx + w * (0.16f + 0.03f * k), ground - h * 0.02f * (k % 2 + 1)), Size(w * 0.025f, h * 0.02f), alpha = ru)
        tagAt(a.tag("ruined", 0, 0), Offset(rx, h * 0.38f), ru * seg(p, 0.2f, 0.35f), back = Color(0xE6403040), ink = ArtInk.Ivory)
        // the house that has the Quran: whole and lit
        val hx = w * 0.72f
        house12(Offset(hx, ground), w * 0.26f, Color(0xFFE2CCA0), Color(0xFF8A6A40), ru * seg(p, 0.45f, 0.6f), lit = seg(p, 0.55f, 0.75f))
        mushaf93(Offset(hx, ground - h * 0.12f), h * 0.07f, ru * seg(p, 0.6f, 0.75f), glow = 0.6f)
    }
    if (rl > 0f) {
        val p = a.b("raiselower")
        drawRect(Color(0xFF1E2234), alpha = 0.6f * rl)
        val c = Offset(w * 0.5f, h * 0.5f)
        mushaf93(c, h * 0.22f, rl, glow = 0.8f)
        val up = easeInOut(seg(p, 0.2f, 0.7f))
        val down = easeInOut(seg(p, 0.45f, 0.9f))
        val r = Offset(w * 0.8f, mix(h * 0.7f, h * 0.36f, up))
        val l = Offset(w * 0.2f, mix(h * 0.7f, h * 0.9f, down))
        for ((pl, lit) in listOf(r to up, l to 0f)) {
            drawRect(Qn.Soil, Offset(pl.x - w * 0.12f, pl.y), Size(w * 0.24f, h * 0.02f), alpha = rl)
            for (k in 0 until 3) figure(pl + Offset((k - 1) * w * 0.06f, 0f), h * 0.14f, lerpColor(Qn.Person, Color(0xFFFFF0C8), lit), rl * (if (pl == l) 1f - 0.5f * down else 1f))
            if (lit > 0f) softGlow(pl + Offset(0f, -h * 0.06f), w * 0.12f, Qn.GoldLight, 0.6f * lit * rl)
        }
        tagAt(a.tag("raiselower", 0, 0), Offset(w * 0.78f, h * 0.12f), rl * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("raiselower", 0, 1), Offset(w * 0.22f, h * 0.5f), rl * seg(p, 0.6f, 0.75f), back = Color(0xE6403040), ink = ArtInk.Ivory)
    }
    if (mw > 0f) {
        val p = a.b("mawla")
        drawRect(Color(0xFF1E2234), alpha = 0.7f * mw)
        kaaba29(Offset(w * 0.5f, ground), w * 0.08f, mw)
        // a seat of honour, and the small roundel that rises to it
        drawRoundRect(Qn.Gold, Offset(w * 0.4f, h * 0.36f), Size(w * 0.2f, h * 0.04f), CornerRadius(4f * density), alpha = mw * seg(p, 0.05f, 0.2f))
        val rise = easeInOut(seg(p, 0.15f, 0.5f))
        roundel93(Offset(w * 0.5f, mix(h * 0.7f, h * 0.28f, rise)), w * 0.06f, a.big("mawla", 0, 0), mw)
        val icons = listOf("book", "scale")
        for (k in 0 until 2) glyphCard(Offset(w * (0.82f - 0.64f * k), h * 0.3f), w * 0.13f, a.tag("mawla", 0, k + 1), mw * seg(p, 0.5f + 0.15f * k, 0.65f + 0.15f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
    }
    if (nl > 0f) {
        val p = a.b("notlineage")
        drawRect(Color(0xFF1E2234), alpha = 0.8f * nl)
        val foot = Offset(w * 0.5f, h * 0.88f)
        val sz = h * 0.55f
        val tilt = -0.7f * easeOut(seg(p, 0.35f, 0.65f))
        balance12(foot, sz, tilt, Qn.GoldLight, nl)
        val (l, r) = balancePans12(foot, sz, tilt)
        mushaf93(l + Offset(0f, -h * 0.06f), h * 0.1f, nl * seg(p, 0.05f, 0.2f), glow = 0.6f)
        // a family tree (three linked roundels) and coins on the light pan
        for (k in 0 until 3) drawCircle(Color(0xFF8A8494), w * 0.012f, r + Offset((k - 1) * w * 0.025f, -h * (0.03f + 0.03f * (k % 2))), alpha = nl * seg(p, 0.15f, 0.3f))
        drawLine(Color(0xFF8A8494), r + Offset(-w * 0.025f, -h * 0.03f), r + Offset(w * 0.025f, -h * 0.03f), 1.2f * density, alpha = nl * seg(p, 0.15f, 0.3f))
        fqCoins(r + Offset(w * 0.04f, -h * 0.005f), w * 0.012f, 2, nl * seg(p, 0.2f, 0.35f))
        tagAt(a.tag("notlineage", 0, 0), l + Offset(0f, -h * 0.2f), nl * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("notlineage", 0, 1), r + Offset(0f, -h * 0.16f), nl * seg(p, 0.55f, 0.7f), back = Color(0xE6403040), ink = ArtInk.Ivory)
    }
}

// ═════════════ 40. fq-nabi: how he ﷺ recited ═════════════

/**
 * Beats: allhours (his ﷺ named light over an open book while the sky turns from night to day and back: «آنَاءَ اللَّيْلِ وَأَطْرَافَ
 * النَّهَارِ»), slowrecite (the band of «سُورَةُ الْبَقَرَةِ» and its verses passing one by one, unhurried: «مُتَرَسِّلًا غَيْرَ مُسْتَعْجِلٍ»),
 * respond (three cards: at a verse of glorification he glorified, at a question he asked, at a refuge he sought refuge), khatm
 * (three rows of nights, three, two and one, each ending in a finished mushaf).
 */
internal fun DrawScope.nightreciteScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "allhours" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "slowrecite" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "respond" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "khatm" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { nightreciteDraw(a) }
}

private fun DrawScope.nightreciteDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dn = fqIn(a, "allhours", "slowrecite")
    val sr = fqIn(a, "slowrecite", "respond")
    val rp = fqIn(a, "respond", "khatm")
    val kh = fqIn(a, "khatm")
    val cyc = if (dn > 0f) 0.5f + 0.5f * cos(seg(a.b("allhours"), 0f, 1f) * 4f * PI.toFloat()) else 1f
    drawRect(Brush.verticalGradient(listOf(lerpColor(Color(0xFF8AB0D8), Color(0xFF0A1028), cyc), lerpColor(Color(0xFFF2E2C0), Color(0xFF1E2A44), cyc))))
    if (dn > 0f) {
        val p = a.b("allhours")
        val c = Offset(w * 0.5f, h * 0.4f)
        sirajMunir(c, w * 0.022f, dn, t, nameAt = c + Offset(0f, -h * 0.14f))
        openBook21(Offset(w * 0.5f, h * 0.6f), w * 0.26f, Qn.Parch, Qn.Ink, dn, glow = 0.5f)
        tagAt(a.tag("allhours", 0, 0), Offset(w * 0.5f, h * 0.9f), dn * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sr > 0f) {
        val p = a.b("slowrecite")
        qPlate(Offset(w * 0.5f, h * 0.22f), w * 0.5f, a.big("slowrecite", 0, 0), sr * seg(p, 0.05f, 0.2f))
        // verses go by one at a time, slowly
        for (k in 0 until 6) {
            val u = seg(p, 0.2f + 0.1f * k, 0.32f + 0.1f * k)
            qRosette(Offset(w * (0.88f - 0.15f * k), h * 0.55f), w * 0.03f, sr * u)
            if (u in 0.01f..0.99f) softGlow(Offset(w * (0.88f - 0.15f * k), h * 0.55f), w * 0.05f, Qn.GoldLight, sr)
        }
        tagAt(a.tag("slowrecite", 0, 1), Offset(w * 0.5f, h * 0.8f), sr * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (rp > 0f) {
        val p = a.b("respond")
        for (k in 0 until 3) glyphCard(Offset(w * (0.76f - 0.26f * k), h * 0.5f), w * 0.16f, a.tag("respond", 0, k), rp * seg(p, 0.05f + 0.25f * k, 0.25f + 0.25f * k)) { at, s, al ->
            when (k) {
                0 -> beads12(at, s * 0.35f, Qn.GoldLight, al, turn = t * 0.4f)
                1 -> qIcon("hands", at, s, Qn.GoldLight, al)
                else -> guardDome12(at + Offset(0f, s * 0.3f), s * 0.5f, 1f, t, al, Qn.GoldLight)
            }
        }
    }
    if (kh > 0f) {
        val p = a.b("khatm")
        drawRect(Color(0xFF0A1028), alpha = 0.6f * kh)
        for (r in 0 until 3) {
            val n = 3 - r
            val y = h * (0.26f + 0.24f * r)
            val f = seg(p, 0.05f + 0.25f * r, 0.3f + 0.25f * r)
            for (k in 0 until n) crescent12(Offset(w * (0.88f - 0.1f * k), y), w * 0.022f, Qn.GoldLight, Color(0xFF0A1028), kh * seg(f, k / (n + 1f), (k + 1f) / (n + 1f)))
            mushaf93(Offset(w * (0.88f - 0.1f * n), y), h * 0.12f, kh * f, glow = f)
            tagAt(a.tag("khatm", 0, r), Offset(w * 0.3f, y), kh * f, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 41. fq-mushaf: touching the mushaf, reading from a phone ═════════════

/**
 * Beats: withbarrier (a bare hand stops short of the mushaf; then a cloth goes between and the hand holds it: «يَمَسُّهُ
 * بِحَائِلٍ», «كَقُفَّازٍ أَوْ قُمَاشٍ»), tafsir (a page with the verses in the middle and commentary all round: «الْمُشْتَمِلَةِ عَلَى
 * التَّفْسِيرِ»), devices (two cards, a phone and a computer, each showing lines of the mushaf, with a green tick), junub (water pours
 * first, then the book opens: «حَتَّى يَغْتَسِلَ»).
 */
internal fun DrawScope.mushaftouchScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "withbarrier" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "tafsirpage" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "devices" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "junub" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { mushafDraw(a) }
}

private fun DrawScope.mushafDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val wb = fqIn(a, "withbarrier", "tafsirpage")
    val tp = fqIn(a, "tafsirpage", "devices")
    val dv = fqIn(a, "devices", "junub")
    val jb = fqIn(a, "junub")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF2A2420), Color(0xFF3E3428))))
    if (wb > 0f) {
        val p = a.b("withbarrier")
        val m = Offset(w * 0.42f, h * 0.5f)
        mushaf93(m, h * 0.4f, wb, glow = 0.3f)
        // the hand: a rounded palm and fingers coming from the right
        val reach = easeOut(seg(p, 0.05f, 0.3f))
        val hand = Offset(mix(w * 0.95f, w * 0.62f, reach), h * 0.5f)
        drawRoundRect(Color(0xFFD8B898), Offset(hand.x, hand.y - h * 0.06f), Size(w * 0.12f, h * 0.12f), CornerRadius(w * 0.03f), alpha = wb)
        for (k in 0 until 4) drawLine(Color(0xFFD8B898), Offset(hand.x, hand.y - h * 0.045f + k * h * 0.03f), Offset(hand.x - w * 0.05f, hand.y - h * 0.045f + k * h * 0.03f), h * 0.022f, StrokeCap.Round, alpha = wb)
        // first it stops short; then a cloth goes between, and it holds the mushaf
        val cloth = seg(p, 0.45f, 0.7f)
        if (cloth > 0f) fqCloth(Offset(w * 0.59f, h * 0.5f), w * 0.08f * cloth, h * 0.26f, Color(0xFF8AA0C0), wb, t)
        tagAt(a.tag("withbarrier", 0, 0), Offset(w * 0.5f, h * 0.1f), wb * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("withbarrier", 0, 1), Offset(w * 0.75f, h * 0.84f), wb * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tp > 0f) {
        val p = a.b("tafsirpage")
        val c = Offset(w * 0.5f, h * 0.5f)
        qPage(c, w * 0.44f, h * 0.76f, tp, lines = 12, frame = false)
        val band = seg(p, 0.1f, 0.35f)
        drawRoundRect(Color(0xFFE8F0E0), Offset(c.x - w * 0.16f, c.y - h * 0.1f), Size(w * 0.32f, h * 0.2f), CornerRadius(4f * density), alpha = tp * band)
        drawRoundRect(Qn.Gold, Offset(c.x - w * 0.16f, c.y - h * 0.1f), Size(w * 0.32f, h * 0.2f), CornerRadius(4f * density), alpha = tp * band, style = Stroke(1.5f * density))
        for (k in 0 until 3) drawLine(Color(0xFF1E5A4A), Offset(c.x - w * 0.13f, c.y - h * 0.05f + k * h * 0.05f), Offset(c.x + w * 0.13f, c.y - h * 0.05f + k * h * 0.05f), 2f * density, StrokeCap.Round, alpha = tp * band)
        val ok = seg(p, 0.55f, 0.75f)
        if (ok > 0f) checkMark(Offset(w * 0.84f, h * 0.2f), w * 0.04f, Qn.Jade, 4f * density, tp * ok)
        tagAt(a.tag("tafsirpage", 0, 0), Offset(w * 0.5f, h * 0.06f), tp * seg(p, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (dv > 0f) {
        val p = a.b("devices")
        for (k in 0 until 2) glyphCard(Offset(w * (0.68f - 0.36f * k), h * 0.48f), w * 0.2f, a.tag("devices", 0, k), dv * seg(p, 0.05f + 0.25f * k, 0.25f + 0.25f * k), rim = Qn.Jade) { at, s, al ->
            val sw = if (k == 0) s * 0.42f else s * 0.8f
            val sh = if (k == 0) s * 0.75f else s * 0.5f
            drawRoundRect(Color(0xFF2A2A30), Offset(at.x - sw / 2f, at.y - sh / 2f), Size(sw, sh), CornerRadius(s * 0.05f), alpha = al)
            drawRect(Qn.Parch, Offset(at.x - sw * 0.42f, at.y - sh * 0.4f), Size(sw * 0.84f, sh * 0.8f), alpha = al)
            for (m in 0 until 4) drawLine(Qn.Ink, Offset(at.x - sw * 0.32f, at.y - sh * 0.25f + m * sh * 0.16f), Offset(at.x + sw * 0.32f, at.y - sh * 0.25f + m * sh * 0.16f), 1.2f * density, alpha = al * 0.6f)
            if (k == 1) drawRect(Color(0xFF2A2A30), Offset(at.x - s * 0.5f, at.y + sh / 2f), Size(s, s * 0.06f), alpha = al)
        }
        val ok = seg(p, 0.6f, 0.8f)
        if (ok > 0f) checkMark(Offset(w * 0.5f, h * 0.84f), w * 0.04f, Qn.Jade, 4f * density, dv * ok)
    }
    if (jb > 0f) {
        val p = a.b("junub")
        val pour = seg(p, 0.05f, 0.5f)
        for (k in 0 until 12) {
            val r = fract(sin(k * 3.3f) * 17.9f)
            val y = h * (0.1f + 0.6f * fract(r + t * 0.8f))
            fqDrop(Offset(w * (0.2f + 0.2f * r), y), w * 0.01f, Fq.Water, jb * pour * (1f - seg(p, 0.5f, 0.6f)))
        }
        val open = seg(p, 0.55f, 0.8f)
        if (open < 0.5f) mushaf93(Offset(w * 0.66f, h * 0.5f), h * 0.36f, jb)
        else openBook21(Offset(w * 0.66f, h * 0.44f), w * 0.3f, Qn.Parch, Qn.Ink, jb, glow = open)
        tagAt(a.tag("junub", 0, 0), Offset(w * 0.3f, h * 0.84f), jb * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 42. fq-adab: the etiquette of reciting ═════════════

/**
 * Beats: istiadha (an open book under a dome of light; dark smoke pulls back from it: «يَسْتَعِيذُ», «قَبْلَ أَنْ يَقْرَأَ»), basmala
 * (a row of surah bands, each with a gold line of basmala over it, except one, «بَرَاءَةَ»), tadabbur (a verse rosette sends out
 * ring after ring, coming back again, and a heart lights: «الْخُشُوعِ», «وَالتَّدَبُّرِ»), oneverse (night passes over one rosette that
 * keeps shining while the moon crosses to dawn: «بِآيَةٍ وَاحِدَةٍ يُرَدِّدُهَا»).
 */
internal fun DrawScope.recitemannersScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "istiadha" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "basmala" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "tadabbur" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "oneverse" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { adabDraw(a) }
}

private fun DrawScope.adabDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ist = fqIn(a, "istiadha", "basmala")
    val bs = fqIn(a, "basmala", "tadabbur")
    val td = fqIn(a, "tadabbur", "oneverse")
    val ov = fqIn(a, "oneverse")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF0E2426), Color(0xFF1E3A3C))))
    if (ist > 0f) {
        val p = a.b("istiadha")
        val c = Offset(w * 0.5f, h * 0.6f)
        openBook21(c, w * 0.28f, Qn.Parch, Qn.Ink, ist, glow = 0.4f)
        guardDome12(c + Offset(0f, h * 0.2f), w * 0.24f, seg(p, 0.15f, 0.4f), t, ist, Qn.GoldLight)
        val back = 1f - easeInOut(seg(p, 0.35f, 0.8f))
        qSmoke(Offset(w * 0.02f, h * 0.15f), Offset(w * 0.02f, h * 0.15f) + (c - Offset(w * 0.02f, h * 0.15f)) * 0.55f, back, t, ist, w * 0.022f)
        tagAt(a.tag("istiadha", 0, 0), Offset(w * 0.5f, h * 0.1f), ist * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("istiadha", 0, 1), Offset(w * 0.8f, h * 0.3f), ist * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (bs > 0f) {
        val p = a.b("basmala")
        for (k in 0 until 4) {
            val c = Offset(w * 0.5f, h * (0.16f + 0.22f * k))
            val f = seg(p, 0.05f + 0.12f * k, 0.2f + 0.12f * k)
            val plain = k == 2
            qPlate(c, w * 0.46f, if (plain) a.big("basmala", 0, 1) else null, bs * f)
            if (!plain) drawLine(Qn.GoldLight, Offset(w * 0.4f, c.y - h * 0.075f), Offset(w * 0.6f, c.y - h * 0.075f), 2.5f * density, StrokeCap.Round, alpha = bs * seg(p, 0.15f + 0.12f * k, 0.3f + 0.12f * k))
        }
        tagAt(a.tag("basmala", 0, 0), Offset(w * 0.86f, h * 0.16f), bs * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (td > 0f) {
        val p = a.b("tadabbur")
        val c = Offset(w * 0.5f, h * 0.38f)
        qRosette(c, w * 0.05f, td)
        for (k in 0 until 3) {
            val r = w * 0.3f * fract(seg(p, 0.05f, 1f) * 2f + k * 0.33f)
            drawCircle(Qn.GoldLight, r, c, alpha = td * 0.5f * (1f - r / (w * 0.3f)), style = Stroke(2f * density))
        }
        val lit = seg(p, 0.35f, 0.6f)
        heart12(Offset(w * 0.5f, h * 0.76f), w * 0.08f, lerpColor(Color(0xFF6A5A6A), Qn.Rose, lit), td, fill = true)
        softGlow(Offset(w * 0.5f, h * 0.76f), w * 0.1f, Qn.GoldLight, td * lit)
        tagAt(a.tag("tadabbur", 0, 0), Offset(w * 0.78f, h * 0.6f), td * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("tadabbur", 0, 1), Offset(w * 0.22f, h * 0.6f), td * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ov > 0f) {
        val p = a.b("oneverse")
        val night = seg(p, 0f, 1f)
        drawRect(Brush.verticalGradient(listOf(Color(0xFF060A1C), lerpColor(Color(0xFF14183A), Color(0xFFE8A070), seg(night, 0.7f, 1f)))), alpha = ov)
        val an = PI.toFloat() * (1.1f + 0.8f * night)
        moon12(Offset(w * 0.5f + cos(an) * w * 0.4f, h * 0.7f + sin(an) * h * 0.55f), w * 0.025f, ov * (1f - seg(night, 0.85f, 1f)))
        val c = Offset(w * 0.5f, h * 0.62f)
        qRosette(c, w * 0.05f, ov)
        softGlow(c, w * 0.08f * (1f + 0.2f * sin(t * 3f)), Qn.GoldLight, ov)
        tagAt(a.tag("oneverse", 0, 0), Offset(w * 0.5f, h * 0.84f), ov * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 43. fq-sujud: the prostration of recitation ═════════════

/**
 * Beats: shaytanweeps (one goes down in prostration, forehead on the ground, while dark smoke draws off into a corner:
 * «فَسَجَدَ», «اعْتَزَلَ الشَّيْطَانُ يَبْكِي»), sunnahnot (two cards: «سُنَّةٌ» ticked, «لَا وَاجِبٌ»), imamled (one in front and one
 * behind: when the front one prostrates the one behind does, and when he does not, he does not), fifteen (the edge of a
 * mushaf with fifteen prostration marks along it: «15»).
 */
internal fun DrawScope.tilawasujudScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "shaytanweeps" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "sunnahnot" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "imamled" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "fifteen" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { tilawaDraw(a) }
}

private fun DrawScope.tilawaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val sw = fqIn(a, "shaytanweeps", "sunnahnot")
    val sn = fqIn(a, "sunnahnot", "imamled")
    val fi = fqIn(a, "imamled", "fifteen")
    val ff = fqIn(a, "fifteen")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF1A2236), Color(0xFF2A2E44))))
    val ground = h * 0.84f
    if (sw > 0f) {
        val p = a.b("shaytanweeps")
        drawRect(Color(0xFF3A3040), Offset(0f, ground), Size(w, h - ground), alpha = sw)
        drawRoundRect(Qn.Rose, Offset(w * 0.32f, ground - h * 0.015f), Size(w * 0.3f, h * 0.04f), CornerRadius(3f * density), alpha = sw)
        bow12(Offset(w * 0.56f, ground - h * 0.01f), h * 0.34f, Qn.Person, sw, easeInOut(seg(p, 0.05f, 0.35f)))
        val away = easeInOut(seg(p, 0.3f, 0.8f))
        qSmoke(Offset(w * 0.98f, h * 0.06f), Offset(mix(w * 0.7f, w * 0.95f, away), mix(h * 0.5f, h * 0.1f, away)), 1f - 0.7f * away, t, sw, w * 0.02f)
        tagAt(a.tag("shaytanweeps", 0, 0), Offset(w * 0.4f, h * 0.4f), sw * seg(p, 0.25f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("shaytanweeps", 0, 1), Offset(w * 0.78f, h * 0.24f), sw * seg(p, 0.55f, 0.7f), back = Color(0xE6403040), ink = ArtInk.Ivory)
    }
    if (sn > 0f) {
        val p = a.b("sunnahnot")
        for (k in 0 until 2) glyphCard(Offset(w * (0.68f - 0.36f * k), h * 0.48f), w * 0.2f, a.tag("sunnahnot", 0, k), sn * seg(p, 0.05f + 0.3f * k, 0.25f + 0.3f * k), rim = if (k == 0) Qn.Jade else Qn.Rose) { at, s, al ->
            if (k == 0) checkMark(at, s * 0.3f, Qn.Jade, 4f * density, al) else crossMark(at, s * 0.25f, Qn.Rose, 4f * density, al)
        }
    }
    if (fi > 0f) {
        val p = a.b("imamled")
        drawRect(Color(0xFF3A3040), Offset(0f, ground), Size(w, h - ground), alpha = fi)
        // first half: the front one prostrates and so does the one behind; second half: neither does
        val down = bump(seg(p, 0.05f, 0.5f))
        for (k in 0 until 2) {
            val foot = Offset(w * (0.36f + 0.32f * k), ground - h * 0.01f)
            drawRoundRect(Qn.Rose, Offset(foot.x - w * 0.14f, ground - h * 0.015f), Size(w * 0.22f, h * 0.04f), CornerRadius(3f * density), alpha = fi)
            val d = seg(down, 0.1f * k, 0.8f + 0.1f * k)
            if (d > 0.02f) bow12(foot, h * 0.3f, Qn.Person, fi, d) else takbir80(foot, h * 0.3f, Qn.Person, fi, 0f)
        }
        tagAt(a.tag("imamled", 0, 0), Offset(w * 0.5f, h * 0.12f), fi * seg(p, 0.1f, 0.25f) * (1f - seg(p, 0.5f, 0.55f)), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("imamled", 0, 1), Offset(w * 0.5f, h * 0.12f), fi * seg(p, 0.6f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ff > 0f) {
        val p = a.b("fifteen")
        mushaf93(Offset(w * 0.5f, h * 0.42f), h * 0.5f, ff, glow = 0.4f)
        for (k in 0 until 15) {
            val f = seg(p, 0.1f + 0.035f * k, 0.15f + 0.035f * k)
            qRosette(Offset(w * (0.92f - 0.06f * k), h * 0.82f), w * 0.016f, ff * f)
        }
        qFit(a.big("fifteen", 0, 0), Offset(w * 0.5f, h * 0.42f), w * 0.15f, h * 0.15f, ArtInk.Ivory, ff * seg(p, 0.65f, 0.8f))
    }
}

// ═════════════ 44. fq-sujud-sifa: what to say in it ═════════════

/**
 * Beats: sameaswalat (one in prostration, and the words rising: «سُبْحَانَ رَبِّيَ الْأَعْلَى»), wajhisajad (the words high, «سَجَدَ وَجْهِيَ
 * لِلَّذِي خَلَقَهُ وَصَوَّرَهُ», and two cards, hearing and sight), nocondition (two cards dimmed, purity and facing the qibla, under
 * «لَا تُشْتَرَطُ», and «اخْتِيَارُ ابْنِ بَازٍ»), takbiroutside (outside the prayer: going down with a tag of takbir; rising with none).
 */
internal fun DrawScope.sujuddhikrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sameaswalat" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "wajhisajad" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "nocondition" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "takbiroutside" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { sujudDhikrDraw(a) }
}

private fun DrawScope.sujudDhikrDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val sa = fqIn(a, "sameaswalat", "wajhisajad")
    val sj = fqIn(a, "wajhisajad", "nocondition")
    val nc = fqIn(a, "nocondition", "takbiroutside")
    val tb = fqIn(a, "takbiroutside")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF14183A), Color(0xFF2A2E50))))
    val ground = h * 0.84f
    if (sa > 0f) {
        val p = a.b("sameaswalat")
        drawRect(Color(0xFF3A3040), Offset(0f, ground), Size(w, h - ground), alpha = sa)
        drawRoundRect(Qn.Rose, Offset(w * 0.34f, ground - h * 0.015f), Size(w * 0.3f, h * 0.04f), CornerRadius(3f * density), alpha = sa)
        bow12(Offset(w * 0.58f, ground - h * 0.01f), h * 0.34f, Qn.Person, sa, easeInOut(seg(p, 0.05f, 0.3f)))
        qFit(a.big("sameaswalat", 0, 0), Offset(w * 0.5f, mix(h * 0.5f, h * 0.2f, easeOut(seg(p, 0.35f, 0.7f)))), w * 0.6f, h * 0.14f, Color(0xFFFFF4D2), sa * seg(p, 0.35f, 0.5f), glow = Qn.Gold)
    }
    if (sj > 0f) {
        val p = a.b("wajhisajad")
        qFit(a.big("wajhisajad", 0, 0), Offset(w * 0.5f, h * 0.2f), w * 0.86f, h * 0.15f, Color(0xFFFFF4D2), sj * seg(p, 0.05f, 0.25f), glow = Qn.Gold)
        val icons = listOf("ear", "eye")
        for (k in 0 until 2) glyphCard(Offset(w * (0.66f - 0.32f * k), h * 0.6f), w * 0.16f, a.tag("wajhisajad", 0, k + 1), sj * seg(p, 0.4f + 0.2f * k, 0.55f + 0.2f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
    }
    if (nc > 0f) {
        val p = a.b("nocondition")
        tagAt(a.tag("nocondition", 0, 2), Offset(w * 0.5f, h * 0.14f), nc * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        val icons = listOf("drop", "kaaba")
        for (k in 0 until 2) glyphCard(Offset(w * (0.68f - 0.36f * k), h * 0.48f), w * 0.18f, a.tag("nocondition", 0, k), nc * seg(p, 0.15f + 0.2f * k, 0.3f + 0.2f * k), rim = Color(0xFF8A8494)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al * 0.7f) }
        tagAt(a.tag("nocondition", 0, 3), Offset(w * 0.5f, h * 0.84f), nc * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tb > 0f) {
        val p = a.b("takbiroutside")
        drawRect(Color(0xFF3A3040), Offset(0f, ground), Size(w, h - ground), alpha = tb)
        drawRoundRect(Qn.Rose, Offset(w * 0.34f, ground - h * 0.015f), Size(w * 0.3f, h * 0.04f), CornerRadius(3f * density), alpha = tb)
        val down = bump(seg(p, 0.05f, 0.9f))
        val going = seg(p, 0.05f, 0.45f) > 0f && seg(p, 0.05f, 0.9f) < 0.5f
        if (down > 0.02f) bow12(Offset(w * 0.58f, ground - h * 0.01f), h * 0.34f, Qn.Person, tb, down) else takbir80(Offset(w * 0.58f, ground - h * 0.01f), h * 0.34f, Qn.Person, tb, 0f)
        tagAt(a.tag("takbiroutside", 0, 0), Offset(w * 0.5f, h * 0.14f), tb * (if (going) seg(p, 0.1f, 0.2f) else 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("takbiroutside", 0, 1), Offset(w * 0.5f, h * 0.28f), tb * seg(p, 0.6f, 0.7f), back = Color(0xE6403040), ink = ArtInk.Ivory)
    }
}
