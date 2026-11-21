package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 79 (2026-11-03): v1.3 fiqh, «الفقه الميسر», zakat. What zakat is and its wisdom; its conditions; gold, silver and money; trade
 * goods; crops and buried treasure; livestock; and the eight who may receive it. Wealth is coins, sacks, heaps, shelves and herds;
 * shares are cells taken out of a bar; a year is a ring of twelve months. People are faceless figures, few and plain.
 */

private object ZakC {
    val Top = Color(0xFF14243A)
    val Low = Color(0xFF3A5068)
    val Ground = Color(0xFF242A30)
    val DayTop = Color(0xFF9CC8E6)
    val DayLow = Color(0xFFF2E6C8)
    val Field = Color(0xFFB8A868)
    val Grass = Color(0xFF7AA860)
    val Gold = Color(0xFFF2C45A)
    val Silver = Color(0xFFC8CCD4)
    val Cell = Color(0xFF3A4458)
    val Rim = Color(0xFF8A94B0)
    val Wheat = Color(0xFFE0B860)
    val Person = Color(0xFFE8DCC8)
    val Wood = Color(0xFF8A6A48)
    val Green = Color(0xFF6FBF8A)
}

private fun DrawScope.zakNight(t: Float, ground: Float) {
    nightSky(t, ZakC.Top, ZakC.Low, ground, starAlpha = 0.5f)
    drawRect(ZakC.Ground, Offset(0f, ground), Size(size.width, size.height - ground))
}

/** A ring of twelve months around [c]; [fill] 0..1 lights them in turn from the top, clockwise. */
private fun DrawScope.yearRing79(c: Offset, r: Float, fill: Float, alpha: Float, on: Color = ZakC.Gold) {
    if (alpha <= 0.01f) return
    for (m in 0 until 12) {
        val ang = -PI.toFloat() / 2f + 2f * PI.toFloat() * m / 12f
        val lit = seg(fill * 12f, m.toFloat(), m + 1f)
        val p = c + Offset(cos(ang), sin(ang)) * r
        drawCircle(ZakC.Rim, r * 0.12f, p, alpha = alpha * 0.4f, style = Stroke(1.2f * density))
        if (lit > 0f) drawCircle(on, r * 0.12f, p, alpha = alpha * lit)
    }
}

/** A bar of [n] cells (right to left); [out] of them, from the left end, lift away by [lift] in gold. */
private fun DrawScope.shareBar79(c: Offset, wd: Float, ht: Float, n: Int, out: Int, lift: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val cw = wd / n
    for (i in 0 until n) {
        val x = c.x + wd / 2f - cw * (i + 1)
        val isOut = i >= n - out
        val dy = if (isOut) -ht * 1.6f * lift else 0f
        drawRect(if (isOut) ZakC.Gold else ZakC.Silver, Offset(x + cw * 0.1f, c.y - ht / 2f + dy), Size(cw * 0.8f, ht), alpha = alpha * (if (isOut) 1f else 0.55f))
    }
}

/** A shop shelf with boxes, its base centred on [foot]. */
private fun DrawScope.shelf79(foot: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (r in 0 until 3) {
        val y = foot.y - s * 0.32f * r
        drawRect(ZakC.Wood, Offset(foot.x - s * 0.5f, y - s * 0.03f), Size(s, s * 0.03f), alpha = alpha)
        for (b in 0 until 4) {
            val col = listOf(Color(0xFFB07A48), Color(0xFF6A8AA8), Color(0xFFC8A060), Color(0xFF8A6A9A))[(r + b) % 4]
            drawRect(col, Offset(foot.x - s * 0.45f + b * s * 0.23f, y - s * 0.25f), Size(s * 0.18f, s * 0.22f), alpha = alpha)
        }
    }
}

/** Wheat stalks across [x0]..[x1], ripened by [ripe]. */
private fun DrawScope.wheat79(x0: Float, x1: Float, base: Float, ht: Float, ripe: Float, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    val col = lerpColor(ZakC.Green, ZakC.Wheat, ripe)
    var x = x0
    var k = 0
    while (x < x1) {
        val sway = sin(t * 1.5f + k) * ht * 0.04f
        drawLine(col, Offset(x, base), Offset(x + sway, base - ht), 1.5f * density, alpha = alpha)
        drawOval(col, Offset(x + sway - ht * 0.03f, base - ht * 1.15f), Size(ht * 0.06f, ht * 0.2f), alpha = alpha)
        x += ht * 0.12f
        k++
    }
}

// ═════════════ 1. zak-hukm: what zakat is ═════════════

/**
 * Beats: growsprout (a heap of coins from which a green shoot rises, and a share set apart: «النَّمَاءُ وَالزِّيَادَةُ», «حَقٌّ يَجِبُ فِي الْمَالِ الَّذِي
 * بَلَغَ نِصَابًا مُعَيَّنًا»), thirdpillar (five pillars, the second lit and the third brightest: «وَأَهَمُّهَا بَعْدَ الصَّلَاةِ»), threewisdoms (coins
 * washed and growing, a tight knot that loosens, a hand that gives: «تَطْهِيرُ الْمَالِ وَتَنْمِيَتُهُ», «وَتَطْهِيرُ الْمُزَكِّي مِنَ الشُّحِّ
 * وَالْبُخْلِ», «وَمُوَاسَاةُ الْفُقَرَاءِ»), bondnet (houses joined by warm lines: «الْأُلْفَةِ وَالْمَحَبَّةِ وَالتَّكَافُلِ»), twokinds (coins, and a
 * measure of food: «زَكَاةُ الْأَمْوَالِ», «وَزَكَاةُ الْأَبْدَانِ»).
 */
internal fun DrawScope.zakatgrowthScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "growsprout" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "thirdpillar" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "threewisdoms" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "bondnet" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "twokinds" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { zakatGrowthDraw(a) }
}

private fun DrawScope.zakatGrowthDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    zakNight(t, ground)
    val gs = fqIn(a, "growsprout", "thirdpillar")
    val tp = fqIn(a, "thirdpillar", "threewisdoms")
    val tw = fqIn(a, "threewisdoms", "bondnet")
    val bn = fqIn(a, "bondnet", "twokinds")
    val tk = fqIn(a, "twokinds")
    if (gs > 0f) {
        val p = a.b("growsprout")
        val c = Offset(w * 0.4f, ground)
        fqCoins(c, w * 0.08f, 6, gs)
        val grow = easeOut(seg(p, 0.1f, 0.45f))
        drawLine(ZakC.Green, c + Offset(0f, -h * 0.1f), c + Offset(0f, -h * 0.1f - h * 0.25f * grow), 3f * dp, StrokeCap.Round, alpha = gs)
        for (k in 0 until 2) drawOval(ZakC.Green, Offset(c.x + (if (k == 0) 0f else -w * 0.06f), c.y - h * 0.1f - h * 0.2f * grow - h * 0.03f), Size(w * 0.06f * grow, h * 0.03f), alpha = gs)
        tagAt(a.tag("growsprout", 0, 0), Offset(w * 0.4f, h * 0.14f), gs * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Jade)
        val part = seg(p, 0.55f, 0.8f)
        coin12(Offset(mix(c.x, w * 0.75f, part), mix(c.y - h * 0.12f, ground - h * 0.2f, part) - h * 0.08f * sin(PI.toFloat() * part)), w * 0.03f, gs * seg(p, 0.5f, 0.6f))
        tagAt(a.tag("growsprout", 0, 1), Offset(w * 0.5f, h * 0.9f), gs * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tp > 0f) {
        val p = a.b("thirdpillar")
        for (k in 0 until 5) {
            val g = easeOut(seg(p, 0.05f + 0.08f * k, 0.2f + 0.08f * k))
            val x = w * (0.82f - 0.16f * k)
            val top = ground - h * 0.4f * g
            val lit = when (k) { 1 -> 0.6f; 2 -> 1f; else -> 0.35f } * seg(p, 0.5f, 0.7f)
            drawRect(lerpColor(Color(0xFFD8CCB4), ZakC.Gold, lit), Offset(x - w * 0.035f, top), Size(w * 0.07f, ground - top), alpha = tp)
            if (k == 2) softGlow(Offset(x, top + h * 0.15f), w * 0.1f, ZakC.Gold, 0.5f * tp * lit)
        }
        tagAt(a.tag("thirdpillar"), Offset(w * 0.5f, h * 0.88f), tp * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tw > 0f) {
        val p = a.b("threewisdoms")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.5f)
            when (k) {
                0 -> {
                    fqCoins(c + Offset(0f, h * 0.12f), w * 0.05f, 3 + (g * 3f).toInt(), tw * g)
                    rain12(c.x - w * 0.06f, c.x + w * 0.06f, c.y - h * 0.15f, c.y + h * 0.08f, t, Fq.Water, tw * g * 0.7f, 10)
                }
                1 -> {
                    val loose = seg(p, 0.45f, 0.62f)
                    for (sg in listOf(-1f, 1f)) drawArc(ZakC.Rim, if (sg < 0f) 90f else -90f, 180f, false, Offset(c.x - w * 0.05f + sg * w * 0.025f * loose - w * 0.0f, c.y - h * 0.06f), Size(w * 0.1f, h * 0.12f), alpha = tw * g, style = Stroke(3f * dp))
                    softGlow(c, w * 0.06f, ZakC.Gold, 0.5f * tw * loose)
                }
                else -> {
                    figure(Offset(c.x - w * 0.06f, c.y + h * 0.2f), h * 0.26f, ZakC.Person, tw * g, arms = 0.7f)
                    figure(Offset(c.x + w * 0.07f, c.y + h * 0.2f), h * 0.22f, Color(0xFFB8B0A0), tw * g)
                    coin12(Offset(c.x, c.y - h * 0.02f), w * 0.018f, tw * g)
                }
            }
            tagAt(a.tag("threewisdoms", 0, k), Offset(c.x, h * (0.16f + 0.08f * (k % 2))), tw * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (bn > 0f) {
        val p = a.b("bondnet")
        val hs = listOf(Offset(w * 0.15f, ground), Offset(w * 0.38f, ground - h * 0.12f), Offset(w * 0.62f, ground - h * 0.05f), Offset(w * 0.85f, ground - h * 0.14f))
        for (k in hs.indices) house12(hs[k], w * 0.12f, Color(0xFFCDBFA6), Color(0xFF6A5A48), bn, lit = seg(p, 0.3f + 0.1f * k, 0.4f + 0.1f * k))
        for (i in hs.indices) for (j in i + 1 until hs.size) {
            val g = seg(p, 0.1f + 0.08f * (i + j), 0.3f + 0.08f * (i + j))
            val a0 = hs[i] + Offset(0f, -w * 0.09f)
            val b0 = hs[j] + Offset(0f, -w * 0.09f)
            drawLine(ZakC.Gold, a0, a0 + (b0 - a0) * g, 1.5f * dp, StrokeCap.Round, alpha = bn * 0.6f)
        }
        tagAt(a.tag("bondnet"), Offset(w * 0.5f, h * 0.16f), bn * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tk > 0f) {
        val p = a.b("twokinds")
        val g0 = seg(p, 0.05f, 0.25f)
        fqCoins(Offset(w * 0.7f, ground), w * 0.07f, 6, tk * g0)
        tagAt(a.tag("twokinds", 0, 0), Offset(w * 0.7f, h * 0.2f), tk * g0, back = Fq.Back, ink = ArtInk.Gold)
        val g1 = seg(p, 0.45f, 0.65f)
        saa73(Offset(w * 0.3f, ground), h * 0.3f, 1f, ZakC.Wheat, tk * g1)
        tagAt(a.tag("twokinds", 0, 1), Offset(w * 0.3f, h * 0.2f), tk * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 2. zak-shurut: the conditions ═════════════

/**
 * Beats: firstislam (an arch with the crescent over it: the first condition: «وَأَوَّلُهَا الْإِسْلَامُ»), nisabowned (a measure fills past the nisab
 * line, with the house, food and clothes set aside first: «مِلْكُ النِّصَابِ», «فَاضِلًا عَنِ الْحَاجَاتِ الضَّرُورِيَّةِ»), fullyear (twelve months light
 * round a heap of coins: «حَوَلَانُ الْحَوْلِ», «اثْنَا عَشَرَ شَهْرًا»), fourwealth (coins, grain, a sheep and a shelf of goods: «الذَّهَبُ
 * وَالْفِضَّةُ», «وَالْخَارِجُ مِنَ الْأَرْضِ», «وَبَهِيمَةُ الْأَنْعَامِ», «وَعُرُوضُ التِّجَارَةِ»), debtzakat (a loan stuck with one who cannot pay is
 * counted once when it comes back; one with an able man is counted every year: «زَكَّاهُ صَاحِبُهُ إِذَا قَبَضَهُ لِعَامٍ وَاحِدٍ», «وَإِنْ كَانَ عَلَى
 * مَلِيءٍ قَادِرٍ زَكَّاهُ لِكُلِّ عَامٍ»).
 */
internal fun DrawScope.zakatcondsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "firstislam" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "nisabowned" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "fullyear" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "fourwealth" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "debtzakat" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { zakatCondsDraw(a) }
}

private fun DrawScope.zakatCondsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    zakNight(t, ground)
    val fi = fqIn(a, "firstislam", "nisabowned")
    val no = fqIn(a, "nisabowned", "fullyear")
    val fy = fqIn(a, "fullyear", "fourwealth")
    val fw = fqIn(a, "fourwealth", "debtzakat")
    val dz = fqIn(a, "debtzakat")
    if (fi > 0f) {
        val p = a.b("firstislam")
        val c = Offset(w * 0.5f, ground)
        drawPath(archPath26(c.x, ground - h * 0.55f, ground, w * 0.12f), ZakC.Gold, alpha = fi * 0.25f)
        drawPath(archPath26(c.x, ground - h * 0.55f, ground, w * 0.12f), ZakC.Gold, alpha = fi, style = Stroke(3f * dp))
        crescent12(Offset(c.x, ground - h * 0.66f), w * 0.03f, ZakC.Gold, ZakC.Top, fi * seg(p, 0.2f, 0.4f))
        val u = seg(p, 0.3f, 0.8f)
        coin12(Offset(mix(w * 0.15f, w * 0.85f, u), ground - h * 0.2f), w * 0.025f, fi * bump(u))
        tagAt(a.tag("firstislam"), Offset(w * 0.5f, h * 0.9f), fi * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (no > 0f) {
        val p = a.b("nisabowned")
        val g0 = seg(p, 0.05f, 0.3f)
        house12(Offset(w * 0.18f, ground), w * 0.1f, Color(0xFFCDBFA6), Color(0xFF6A5A48), no * g0)
        dateBowl72(Offset(w * 0.3f, ground), w * 0.1f, no * g0)
        fqShirt(Offset(w * 0.42f, ground - h * 0.08f), w * 0.08f, Color(0xFF8A9AC8), no * g0)
        tagAt(a.tag("nisabowned", 0, 1), Offset(w * 0.3f, h * 0.5f), no * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
        val m = Offset(w * 0.75f, h * 0.48f)
        val fill = seg(p, 0.2f, 0.6f)
        drawRoundRect(ZakC.Cell, Offset(m.x - w * 0.05f, m.y - h * 0.3f), Size(w * 0.1f, h * 0.6f), CornerRadius(w * 0.02f), alpha = no)
        drawRoundRect(ZakC.Gold, Offset(m.x - w * 0.05f, m.y + h * 0.3f - h * 0.6f * fill), Size(w * 0.1f, h * 0.6f * fill), CornerRadius(w * 0.02f), alpha = no * 0.85f)
        drawLine(Color(0xFFC0503A), Offset(m.x - w * 0.08f, m.y - h * 0.1f), Offset(m.x + w * 0.08f, m.y - h * 0.1f), 2.5f * dp, alpha = no)
        tagAt(a.tag("nisabowned", 0, 0), Offset(m.x, h * 0.1f), no * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fy > 0f) {
        val p = a.b("fullyear")
        val c = Offset(w * 0.5f, h * 0.5f)
        yearRing79(c, h * 0.3f, seg(p, 0.05f, 0.85f), fy)
        fqCoins(c + Offset(0f, h * 0.06f), w * 0.05f, 5, fy)
        tagAt(a.tag("fullyear", 0, 0), Offset(w * 0.18f, h * 0.3f), fy * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("fullyear", 0, 1), Offset(w * 0.82f, h * 0.7f), fy * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (fw > 0f) {
        val p = a.b("fourwealth")
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.22f * k, 0.22f + 0.22f * k)
            val c = Offset(w * (0.85f - 0.233f * k), ground)
            when (k) {
                0 -> fqCoins(c, w * 0.05f, 5, fw * g)
                1 -> fqSack(c, h * 0.2f, ZakC.Wheat, fw * g)
                2 -> sheep26(c + Offset(0f, -h * 0.1f), h * 0.16f, Color(0xFFF0EAE0), fw * g)
                else -> shelf79(c, w * 0.16f, fw * g)
            }
            tagAt(a.tag("fourwealth", 0, k), Offset(c.x, h * (0.22f + 0.1f * (k % 2))), fw * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (dz > 0f) {
        val p = a.b("debtzakat")
        for (r in 0 until 2) {
            val g = seg(p, 0.05f + 0.45f * r, 0.25f + 0.45f * r)
            val y = h * (0.35f + 0.32f * r)
            fqCoins(Offset(w * 0.85f, y + h * 0.04f), w * 0.035f, 3, dz * g)
            for (yr in 0 until 4) {
                val c = Offset(w * (0.68f - 0.12f * yr), y)
                val lit = if (r == 0) (if (yr == 3) seg(p, 0.3f, 0.4f) else 0f) else seg(p, 0.6f + 0.06f * yr, 0.7f + 0.06f * yr)
                drawCircle(ZakC.Rim, w * 0.025f, c, alpha = dz * g * 0.5f, style = Stroke(1.5f * dp))
                if (lit > 0f) drawCircle(ZakC.Gold, w * 0.025f, c, alpha = dz * g * lit)
            }
            tagAt(a.tag("debtzakat", 0, r), Offset(w * 0.5f, y - h * 0.1f), dz * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
}

// ═════════════ 3. zak-naqd: gold, silver and money ═════════════

/**
 * Beats: quarterten (a bar of forty cells; one lifts out in gold: «رُبُعُ الْعُشْرِ»), twonisabs (a stack of gold and a stack of silver rise to
 * their lines: «نَحْوَ 85 غِرَامًا», «مِئَتَا دِرْهَمٍ»), nocombine (half a gold stack and half a silver one stay apart, a bar between them: «وَلَا
 * يُضَمُّ أَحَدُهُمَا إِلَى الْآخَرِ», «عِنْدَ مُؤَلِّفِي الْكِتَابِ»), fiveof200 (five coins step out of two hundred: «خَمْسَةُ دَرَاهِمَ», «نِصْفُ
 * مِثْقَالٍ»), jewelry (a bracelet and a necklace on a level balance of two views: «الصَّحِيحُ عِنْدَ مُؤَلِّفِي الْكِتَابِ وُجُوبُ الزَّكَاةِ فِيهِ»,
 * «خِلَافٌ مَشْهُورٌ»).
 */
internal fun DrawScope.goldnisabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "quarterten" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "twonisabs" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "nocombine" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "fiveof200" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "jewelry" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { goldNisabDraw(a) }
}

private fun DrawScope.goldNisabDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    zakNight(t, ground)
    val qt = fqIn(a, "quarterten", "twonisabs")
    val tn = fqIn(a, "twonisabs", "nocombine")
    val nc = fqIn(a, "nocombine", "fiveof200")
    val ff = fqIn(a, "fiveof200", "jewelry")
    val jw = fqIn(a, "jewelry")
    if (qt > 0f) {
        val p = a.b("quarterten")
        shareBar79(Offset(w * 0.5f, h * 0.55f), w * 0.84f, h * 0.1f, 40, 1, easeOut(seg(p, 0.35f, 0.6f)), qt * seg(p, 0f, 0.2f))
        tagAt(a.tag("quarterten"), Offset(w * 0.5f, h * 0.2f), qt * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val stack = { foot: Offset, n: Int, col: Color, alpha: Float -> fqCoins(foot, w * 0.06f, n, alpha, col) }
    if (tn > 0f) {
        val p = a.b("twonisabs")
        val ng = (seg(p, 0.05f, 0.4f) * 14f).toInt()
        stack(Offset(w * 0.7f, ground), ng, ZakC.Gold, tn)
        drawLine(ZakC.Gold, Offset(w * 0.6f, ground - w * 0.06f * 0.32f * 14f), Offset(w * 0.8f, ground - w * 0.06f * 0.32f * 14f), 2f * dp, alpha = tn * 0.8f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp)))
        tagAt(a.tag("twonisabs", 0, 0), Offset(w * 0.7f, h * 0.14f), tn * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        val ns = (seg(p, 0.5f, 0.85f) * 16f).toInt()
        stack(Offset(w * 0.3f, ground), ns, ZakC.Silver, tn)
        drawLine(ZakC.Silver, Offset(w * 0.2f, ground - w * 0.06f * 0.32f * 16f), Offset(w * 0.4f, ground - w * 0.06f * 0.32f * 16f), 2f * dp, alpha = tn * 0.8f * seg(p, 0.5f, 0.6f), pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp)))
        tagAt(a.tag("twonisabs", 0, 1), Offset(w * 0.3f, h * 0.14f), tn * seg(p, 0.75f, 0.9f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (nc > 0f) {
        val p = a.b("nocombine")
        stack(Offset(w * 0.68f, ground), 7, ZakC.Gold, nc)
        stack(Offset(w * 0.32f, ground), 8, ZakC.Silver, nc)
        drawLine(Color(0xFFC0503A), Offset(w * 0.5f, ground - h * 0.4f), Offset(w * 0.5f, ground), 4f * dp, StrokeCap.Round, alpha = nc * seg(p, 0.2f, 0.35f))
        tagAt(a.tag("nocombine", 0, 0), Offset(w * 0.5f, h * 0.14f), nc * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("nocombine", 0, 1), Offset(w * 0.5f, h * 0.9f), nc * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ff > 0f) {
        val p = a.b("fiveof200")
        for (i in 0 until 200) {
            val col = i % 25
            val row = i / 25
            val c = Offset(w * (0.9f - 0.03f * col), h * (0.3f + 0.045f * row))
            val out = i >= 195
            val lift = if (out) easeOut(seg(p, 0.3f, 0.55f)) else 0f
            drawCircle(if (out) ZakC.Gold else ZakC.Silver, w * 0.009f, c + Offset(0f, -h * 0.2f * lift), alpha = ff * (if (out) 1f else 0.6f))
        }
        tagAt(a.tag("fiveof200", 0, 0), Offset(w * 0.2f, h * 0.15f), ff * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("fiveof200", 0, 1), Offset(w * 0.5f, h * 0.88f), ff * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (jw > 0f) {
        val p = a.b("jewelry")
        balance12(Offset(w * 0.5f, ground), h * 0.5f, 0.05f * sin(t), Fq.Brass, jw)
        drawCircle(ZakC.Gold, w * 0.04f, Offset(w * 0.33f, h * 0.36f), alpha = jw * seg(p, 0.05f, 0.2f), style = Stroke(4f * dp))
        for (k in 0 until 7) drawCircle(ZakC.Gold, w * 0.008f, Offset(w * (0.62f + 0.02f * k), h * (0.36f + 0.03f * sin(k * 0.5f))), alpha = jw * seg(p, 0.1f, 0.25f))
        tagAt(a.tag("jewelry", 0, 0), Offset(w * 0.5f, h * 0.12f), jw * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("jewelry", 0, 1), Offset(w * 0.5f, h * 0.9f), jw * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 4. zak-urud: trade goods ═════════════

/**
 * Beats: shopshelf (a shelf of goods and a line of profit rising: «لِأَجْلِ الرِّبْحِ», «وَالزَّكَاةُ وَاجِبَةٌ فِيهَا»), threetradeconds (a hand that
 * buys, a heart that intends trade, a measure that reaches the nisab: «بِفِعْلِهِ», «بِنِيَّةِ التِّجَارَةِ», «وَأَنْ تَبْلُغَ قِيمَتُهَا نِصَابًا»),
 * valueatyearend (the year ring closes and the goods are weighed in coins; the old price tag is struck: «وَالْعِبْرَةُ بِقِيمَتِهَا وَقْتَ تَمَامِ
 * الْحَوْلِ», «لَا بِمَا اشْتُرِيَتْ بِهِ»), inheritedno (a box that came by a will is set apart from the shelf: «مَا مَلَكَهُ بِالْإِرْثِ»), rentincome (a
 * camel let out for hire, and the coins of its hire counted: «فِيمَا يَحْصُلُ مِنْ أُجْرَتِهَا»).
 */
internal fun DrawScope.tradegoodsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "shopshelf" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "threetradeconds" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "valueatyearend" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "inheritedno" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "rentincome" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { tradeGoodsDraw(a) }
}

private fun DrawScope.tradeGoodsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.82f
    zakNight(t, ground)
    val ss = fqIn(a, "shopshelf", "threetradeconds")
    val tc = fqIn(a, "threetradeconds", "valueatyearend")
    val vy = fqIn(a, "valueatyearend", "inheritedno")
    val ih = fqIn(a, "inheritedno", "rentincome")
    val ri = fqIn(a, "rentincome")
    if (ss > 0f) {
        val p = a.b("shopshelf")
        shelf79(Offset(w * 0.32f, ground), w * 0.36f, ss)
        val g = seg(p, 0.2f, 0.6f)
        val pts = listOf(0f, 0.15f, 0.1f, 0.3f, 0.25f, 0.45f)
        val path = Path().apply {
            for (k in pts.indices) {
                val x = w * (0.6f + 0.06f * k)
                val y = h * 0.6f - h * 0.4f * pts[k] * g
                if (k == 0) moveTo(x, y) else lineTo(x, y)
            }
        }
        drawPath(path, ZakC.Green, alpha = ss, style = Stroke(3f * dp, cap = StrokeCap.Round))
        tagAt(a.tag("shopshelf", 0, 0), Offset(w * 0.75f, h * 0.15f), ss * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Jade)
        tagAt(a.tag("shopshelf", 0, 1), Offset(w * 0.5f, h * 0.92f), ss * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tc > 0f) {
        val p = a.b("threetradeconds")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.5f)
            when (k) {
                0 -> { coin12(c + Offset(w * 0.04f, -h * 0.02f), w * 0.02f, tc * g); drawRect(Color(0xFFB07A48), Offset(c.x - w * 0.07f, c.y - h * 0.04f), Size(w * 0.07f, h * 0.08f), alpha = tc * g) }
                1 -> heart12(c, w * 0.07f, ZakC.Gold, tc * g, fill = true)
                else -> fqMeter(c, w * 0.18f, h * 0.05f, 6, g, ZakC.Gold, ZakC.Cell, tc)
            }
            tick14(c + Offset(0f, -h * 0.15f), w * 0.03f, seg(p, 0.15f + 0.28f * k, 0.3f + 0.28f * k))
            tagAt(a.tag("threetradeconds", 0, k), c + Offset(0f, h * 0.16f), tc * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (vy > 0f) {
        val p = a.b("valueatyearend")
        yearRing79(Offset(w * 0.2f, h * 0.4f), h * 0.18f, seg(p, 0.05f, 0.35f), vy)
        shelf79(Offset(w * 0.5f, ground), w * 0.24f, vy)
        val conv = seg(p, 0.35f, 0.65f)
        fqCoins(Offset(w * 0.8f, ground), w * 0.05f, (conv * 8f).toInt(), vy)
        tagAt(a.tag("valueatyearend", 0, 0), Offset(w * 0.6f, h * 0.14f), vy * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
        val tag = Offset(w * 0.5f, ground - h * 0.38f)
        drawRoundRect(Color(0xFFE8E2D4), Offset(tag.x - w * 0.05f, tag.y - h * 0.03f), Size(w * 0.1f, h * 0.06f), CornerRadius(h * 0.01f), alpha = vy)
        cross14(tag, w * 0.03f, seg(p, 0.65f, 0.8f))
        tagAt(a.tag("valueatyearend", 0, 1), Offset(w * 0.4f, h * 0.28f), vy * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ih > 0f) {
        val p = a.b("inheritedno")
        shelf79(Offset(w * 0.68f, ground), w * 0.28f, ih)
        scroll14(Offset(w * 0.22f, h * 0.3f), w * 0.14f, h * 0.1f, seg(p, 0.05f, 0.25f), ih)
        val u = easeInOut(seg(p, 0.2f, 0.5f))
        drawRect(Color(0xFF8A6A9A), Offset(w * 0.22f - w * 0.04f, mix(h * 0.38f, ground - h * 0.1f, u)), Size(w * 0.08f, h * 0.1f), alpha = ih)
        drawLine(ZakC.Rim, Offset(w * 0.4f, ground - h * 0.4f), Offset(w * 0.4f, ground), 2f * dp, alpha = ih * seg(p, 0.5f, 0.6f), pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        tagAt(a.tag("inheritedno"), Offset(w * 0.22f, h * 0.12f), ih * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ri > 0f) {
        val p = a.b("rentincome")
        camel26(Offset(w * 0.68f, ground), h * 0.4f, Color(0xFFC8A880), ri, t, walk = t * 3f)
        for (k in 0 until 4) {
            val u = seg(p, 0.2f + 0.12f * k, 0.35f + 0.12f * k)
            coin12(Offset(mix(w * 0.6f, w * 0.25f, u), mix(ground - h * 0.3f, ground - h * 0.03f - k * h * 0.02f, u)), w * 0.022f, ri * seg(u, 0f, 0.1f))
        }
        tagAt(a.tag("rentincome"), Offset(w * 0.3f, h * 0.2f), ri * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. zak-zuru: crops, fruits and buried treasure ═════════════

/**
 * Beats: storedgrains (sacks of grain and heaps of dates, ticked; a basket of fresh fruit and greens, not: «كَالْقَمْحِ وَالشَّعِيرِ وَالْأَرُزِّ
 * وَالتَّمْرِ وَالزَّبِيبِ», «وَلَا تَجِبُ فِي الْفَوَاكِهِ وَالْخَضْرَاوَاتِ»), tenthorhalf (a field under rain gives a tenth; a field watered by a
 * pump gives half of it: «خَمْسَةُ أَوْسُقٍ», «الْعُشْرُ فِيمَا سُقِيَ بِلَا كُلْفَةٍ», «وَنِصْفُ الْعُشْرِ فِيمَا سُقِيَ بِمُؤْنَةٍ»), ripenow (the wheat
 * turns gold and is due at once, with no year ring: «وَلَا يُشْتَرَطُ لَهَا الْحَوْلُ»), threehundredsaa (measures fill a grid: «ثَلَاثُمِئَةِ
 * صَاعٍ», «612 كِيلُوغْرَامًا»), buriedfifth (a chest comes up out of the ground and a fifth of it is set apart: «وَفِيهِ الْخُمُسُ فِي قَلِيلِهِ
 * وَكَثِيرِهِ»).
 */
internal fun DrawScope.harvestshareScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "storedgrains" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "tenthorhalf" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "ripenow" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "threehundredsaa" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "buriedfifth" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { harvestShareDraw(a) }
}

private fun DrawScope.harvestShareDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.74f
    daySky12(ZakC.DayTop, ZakC.DayLow, ground, ZakC.Field)
    val sg = fqIn(a, "storedgrains", "tenthorhalf")
    val th = fqIn(a, "tenthorhalf", "ripenow")
    val rn = fqIn(a, "ripenow", "threehundredsaa")
    val ts = fqIn(a, "threehundredsaa", "buriedfifth")
    val bf = fqIn(a, "buriedfifth")
    if (sg > 0f) {
        val p = a.b("storedgrains")
        val g0 = seg(p, 0.05f, 0.3f)
        for (k in 0 until 3) fqSack(Offset(w * (0.85f - 0.11f * k), ground + h * 0.12f), h * 0.18f, listOf(ZakC.Wheat, Color(0xFFD8C080), Color(0xFFF2EAD8))[k], sg * g0)
        heap73(Offset(w * 0.5f, ground + h * 0.1f), w * 0.12f, Color(0xFF6A3A22), sg * g0)
        tick14(Offset(w * 0.7f, ground - h * 0.14f), w * 0.04f, seg(p, 0.25f, 0.4f))
        tagAt(a.tag("storedgrains", 0, 0), Offset(w * 0.65f, h * 0.12f), sg * g0, back = Fq.Back, ink = ArtInk.Ivory)
        val g1 = seg(p, 0.5f, 0.7f)
        val bk = Offset(w * 0.2f, ground + h * 0.1f)
        drawArc(Color(0xFF9A7A50), 0f, 180f, true, Offset(bk.x - w * 0.1f, bk.y - h * 0.08f), Size(w * 0.2f, h * 0.14f), alpha = sg * g1)
        for (k in 0 until 4) drawCircle(listOf(Color(0xFFC0503A), Color(0xFF6FBF8A), Color(0xFFE0A040), Color(0xFFC0503A))[k], w * 0.02f, bk + Offset(w * (-0.06f + 0.04f * k), -h * 0.02f), alpha = sg * g1)
        cross14(Offset(w * 0.2f, ground - h * 0.08f), w * 0.035f, seg(p, 0.65f, 0.8f))
        tagAt(a.tag("storedgrains", 0, 1), Offset(w * 0.25f, h * 0.24f), sg * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (th > 0f) {
        val p = a.b("tenthorhalf")
        tagAt(a.tag("tenthorhalf", 0, 0), Offset(w * 0.5f, h * 0.08f), th * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        for (k in 0 until 2) {
            val g = seg(p, 0.2f + 0.35f * k, 0.35f + 0.35f * k)
            val x0 = if (k == 0) w * 0.55f else w * 0.05f
            wheat79(x0, x0 + w * 0.4f, ground + h * 0.15f, h * 0.14f, 0.6f, th * g, t)
            if (k == 0) {
                cloud38(Offset(w * 0.75f, h * 0.25f), w * 0.06f, Color(0xFFE8ECF2), th * g)
                rain12(w * 0.6f, w * 0.9f, h * 0.3f, ground, t, Fq.Water, th * g, 18)
            } else {
                drawCircle(ZakC.Wood, h * 0.08f, Offset(w * 0.12f, ground - h * 0.06f), alpha = th * g, style = Stroke(3f * dp))
                for (s in 0 until 6) {
                    val ang = t * 2f + s * PI.toFloat() / 3f
                    drawLine(ZakC.Wood, Offset(w * 0.12f, ground - h * 0.06f), Offset(w * 0.12f, ground - h * 0.06f) + Offset(cos(ang), sin(ang)) * h * 0.08f, 2f * dp, alpha = th * g)
                }
            }
            shareBar79(Offset(x0 + w * 0.2f, ground + h * 0.2f), w * 0.3f, h * 0.035f, if (k == 0) 10 else 20, 1, seg(p, 0.3f + 0.35f * k, 0.45f + 0.35f * k), th * g)
            tagAt(a.tag("tenthorhalf", 0, 1 + k), Offset(x0 + w * 0.2f, h * 0.2f), th * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (rn > 0f) {
        val p = a.b("ripenow")
        wheat79(w * 0.05f, w * 0.95f, ground + h * 0.2f, h * 0.22f, seg(p, 0.05f, 0.4f), rn, t)
        val ring = seg(p, 0.45f, 0.6f)
        yearRing79(Offset(w * 0.5f, h * 0.3f), h * 0.12f, 0.3f, rn * ring)
        cross14(Offset(w * 0.5f, h * 0.3f), w * 0.04f, seg(p, 0.55f, 0.7f))
        tagAt(a.tag("ripenow"), Offset(w * 0.5f, h * 0.1f), rn * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ts > 0f) {
        val p = a.b("threehundredsaa")
        drawRect(Color(0xFF2A2430), Offset.Zero, size, alpha = 0.7f * ts)
        val n = (seg(p, 0.05f, 0.6f) * 60f).toInt()
        for (i in 0 until 60) {
            val c = Offset(w * (0.9f - 0.054f * (i % 15)), h * (0.3f + 0.1f * (i / 15)))
            saa73(c + Offset(0f, h * 0.035f), h * 0.07f, if (i < n) 1f else 0f, ZakC.Wheat, ts * 0.9f)
        }
        tagAt(a.tag("threehundredsaa", 0, 0), Offset(w * 0.5f, h * 0.15f), ts * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("threehundredsaa", 0, 1), Offset(w * 0.5f, h * 0.8f), ts * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (bf > 0f) {
        val p = a.b("buriedfifth")
        val up = easeOut(seg(p, 0.05f, 0.4f))
        val chest = Offset(w * 0.4f, mix(ground + h * 0.2f, ground - h * 0.02f, up))
        drawRect(ZakC.Wood, Offset(chest.x - w * 0.12f, chest.y - h * 0.12f), Size(w * 0.24f, h * 0.12f), alpha = bf)
        drawRect(ZakC.Gold, Offset(chest.x - w * 0.12f, chest.y - h * 0.07f), Size(w * 0.24f, h * 0.015f), alpha = bf)
        val open = seg(p, 0.4f, 0.55f)
        if (open > 0f) for (k in 0 until 5) {
            val out = k == 4
            val lift = if (out) easeInOut(seg(p, 0.6f, 0.85f)) else 0f
            coin12(Offset(chest.x - w * 0.08f + w * 0.04f * k + w * 0.35f * lift, chest.y - h * 0.15f - h * 0.1f * sin(PI.toFloat() * lift)), w * 0.022f, bf * open)
        }
        tagAt(a.tag("buriedfifth"), Offset(w * 0.5f, h * 0.15f), bf * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 6. zak-anam: livestock ═════════════

/**
 * Beats: threeherds (a camel, a cow and a sheep come in: «الْإِبِلُ», «وَالْبَقَرُ», «وَالْغَنَمُ»), herdnisab (each beside its count in dots, five,
 * thirty, forty: «خَمْسًا مِنَ الْإِبِلِ», «وَثَلَاثِينَ مِنَ الْبَقَرِ», «وَأَرْبَعِينَ مِنَ الْغَنَمِ»), grazingfree (sheep grazing on open pasture,
 * ticked; an ox at the plough, struck out: «سَائِمَةً تَرْعَى الْكَلَأَ الْمُبَاحَ», «وَأَلَّا تَكُونَ عَامِلَةً»), mostlyfed (a year ring, the smaller
 * part green with grazing and the larger part fed from bales: «فَلَيْسَتْ سَائِمَةً»), onesheepdue (a flock of forty to a hundred and twenty and one
 * sheep that steps out of it: «أَرْبَعِينَ إِلَى مِئَةٍ وَعِشْرِينَ شَاةً»).
 */
internal fun DrawScope.livestockzakatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threeherds" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "herdnisab" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "grazingfree" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "mostlyfed" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "onesheepdue" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { livestockZakatDraw(a) }
}

private fun DrawScope.livestockZakatDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.74f
    daySky12(ZakC.DayTop, ZakC.DayLow, ground, ZakC.Grass)
    val th = fqIn(a, "threeherds", "herdnisab")
    val hn = fqIn(a, "herdnisab", "grazingfree")
    val gf = fqIn(a, "grazingfree", "mostlyfed")
    val mf = fqIn(a, "mostlyfed", "onesheepdue")
    val os = fqIn(a, "onesheepdue")
    val fleece = Color(0xFFF0EAE0)
    val camelC = Color(0xFFC8A880)
    val cowC = Color(0xFF8A6A50)
    if (th > 0f) {
        val p = a.b("threeherds")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val x = w * (0.8f - 0.3f * k)
            when (k) {
                0 -> camel26(Offset(x, ground + h * 0.1f), h * 0.36f, camelC, th * g, t)
                1 -> fqCow(Offset(x, ground + h * 0.1f), h * 0.3f, cowC, th * g)
                else -> sheep26(Offset(x, ground), h * 0.2f, fleece, th * g)
            }
            tagAt(a.tag("threeherds", 0, k), Offset(x, h * 0.16f), th * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (hn > 0f) {
        val p = a.b("herdnisab")
        drawRect(Color(0xFF2A2C38), Offset.Zero, size, alpha = 0.75f * hn)
        val counts = listOf(5, 30, 40)
        for (r in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * r, 0.25f + 0.28f * r)
            val y = h * (0.25f + 0.25f * r)
            when (r) {
                0 -> camel26(Offset(w * 0.9f, y + h * 0.06f), h * 0.14f, camelC, hn * g, t)
                1 -> fqCow(Offset(w * 0.9f, y + h * 0.06f), h * 0.12f, cowC, hn * g)
                else -> sheep26(Offset(w * 0.9f, y), h * 0.08f, fleece, hn * g)
            }
            val n = counts[r]
            for (i in 0 until n) {
                val c = Offset(w * (0.8f - 0.018f * (i % 20)), y - h * 0.02f + h * 0.04f * (i / 20))
                drawCircle(fleece, w * 0.006f, c, alpha = hn * seg(g * n, i.toFloat(), i + 1f))
            }
            tagAt(a.tag("herdnisab", 0, r), Offset(w * 0.25f, y), hn * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (gf > 0f) {
        val p = a.b("grazingfree")
        val g0 = seg(p, 0.05f, 0.25f)
        for (k in 0 until 3) sheep26(Offset(w * (0.62f + 0.12f * k), ground + h * 0.08f + (k % 2) * h * 0.04f), h * 0.12f, fleece, gf * g0)
        tick14(Offset(w * 0.74f, ground - h * 0.12f), w * 0.04f, seg(p, 0.2f, 0.35f))
        tagAt(a.tag("grazingfree", 0, 0), Offset(w * 0.74f, h * 0.12f), gf * g0, back = Fq.Back, ink = ArtInk.Jade)
        val g1 = seg(p, 0.5f, 0.7f)
        fqCow(Offset(w * 0.2f, ground + h * 0.12f), h * 0.22f, cowC, gf * g1)
        drawLine(ZakC.Wood, Offset(w * 0.3f, ground + h * 0.02f), Offset(w * 0.42f, ground + h * 0.12f), 3f * dp, StrokeCap.Round, alpha = gf * g1)
        drawLine(ZakC.Wood, Offset(w * 0.42f, ground + h * 0.12f), Offset(w * 0.46f, ground + h * 0.16f), 5f * dp, StrokeCap.Round, alpha = gf * g1)
        cross14(Offset(w * 0.28f, ground - h * 0.18f), w * 0.04f, seg(p, 0.65f, 0.8f))
        tagAt(a.tag("grazingfree", 0, 1), Offset(w * 0.28f, h * 0.24f), gf * g1, back = Fq.Back, ink = ArtInk.Ember)
    }
    if (mf > 0f) {
        val p = a.b("mostlyfed")
        drawRect(Color(0xFF2A2C38), Offset.Zero, size, alpha = 0.75f * mf)
        val c = Offset(w * 0.5f, h * 0.48f)
        val r = h * 0.28f
        yearRing79(c, r, seg(p, 0.05f, 0.25f) * (4f / 12f), mf, ZakC.Grass)
        for (m in 4 until 12) {
            val ang = -PI.toFloat() / 2f + 2f * PI.toFloat() * m / 12f
            val lit = seg(p, 0.25f + 0.03f * (m - 4), 0.3f + 0.03f * (m - 4))
            drawRect(ZakC.Wheat, Offset(c.x + cos(ang) * r - r * 0.1f, c.y + sin(ang) * r - r * 0.08f), Size(r * 0.2f, r * 0.16f), alpha = mf * lit)
        }
        sheep26(c, h * 0.12f, fleece, mf)
        cross14(c + Offset(0f, -h * 0.12f), w * 0.03f, seg(p, 0.6f, 0.75f))
        tagAt(a.tag("mostlyfed"), Offset(w * 0.5f, h * 0.9f), mf * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (os > 0f) {
        val p = a.b("onesheepdue")
        for (k in 0 until 14) {
            val c = Offset(w * (0.35f + 0.045f * (k % 7)), ground + h * (0.04f + 0.07f * (k / 7)))
            sheep26(c, h * 0.08f, fleece, os)
        }
        val u = easeInOut(seg(p, 0.35f, 0.7f))
        val one = Offset(mix(w * 0.35f, w * 0.15f, u), ground + h * 0.05f)
        sheep26(one, h * 0.1f, fleece, os)
        softGlow(one, w * 0.06f, ZakC.Gold, 0.5f * os * u)
        tagAt(a.tag("onesheepdue"), Offset(w * 0.5f, h * 0.14f), os * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 7. zak-ahl: the eight who receive it ═════════════

/**
 * Beats: firstfour (eight places in a ring; the first four light: an empty bowl, a half bowl, a ledger, a heart: «الْفُقَرَاءُ», «وَالْمَسَاكِينُ»,
 * «وَالْعَامِلُونَ عَلَيْهَا», «وَالْمُؤَلَّفَةُ قُلُوبُهُمْ»), lastfour (the other four: a broken chain, a torn debt, a shield, a road with a bundle:
 * «وَالرِّقَابُ», «وَالْغَارِمُونَ», «وَالْغُزَاةُ فِي سَبِيلِ اللَّهِ», «وَابْنُ السَّبِيلِ»), anyclass (a coin goes to one place alone and is ticked: «وَيُجْزِئُ
 * دَفْعُهَا إِلَى أَيِّ صِنْفٍ مِنْهُمْ»), poorneedy (two bowls, under half and over half, then a year's worth: «أَقَلَّ مِنْ نِصْفِ الْكِفَايَةِ», «يَجِدُ
 * نِصْفَهَا أَوْ أَكْثَرَهَا», «مَا يَكْفِيهِ سَنَةً»), notdependents (a coin turned back at one's own house: «كَالْآبَاءِ وَالْأَوْلَادِ وَالزَّوْجَةِ»).
 */
internal fun DrawScope.eightrecipientsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "firstfour" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "lastfour" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "anyclass" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "poorneedy" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "notdependents" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { eightRecipientsDraw(a) }
}

private fun DrawScope.eightIcon79(k: Int, c: Offset, s: Float, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    when (k) {
        0 -> drawArc(Color(0xFFB07A48), 0f, 180f, false, Offset(c.x - s * 0.5f, c.y - s * 0.3f), Size(s, s * 0.6f), alpha = alpha, style = Stroke(s * 0.1f))
        1 -> {
            drawArc(Color(0xFFB07A48), 0f, 180f, true, Offset(c.x - s * 0.5f, c.y - s * 0.3f), Size(s, s * 0.6f), alpha = alpha * 0.4f)
            drawArc(ZakC.Wheat, 0f, 180f, true, Offset(c.x - s * 0.35f, c.y - s * 0.05f), Size(s * 0.7f, s * 0.35f), alpha = alpha)
        }
        2 -> openBook21(c, s * 1.2f, Color(0xFFF2E8CE), Color(0xFFB8A888), alpha)
        3 -> heart12(c, s * 0.6f, ZakC.Gold, alpha, fill = true)
        4 -> {
            chain72(c + Offset(-s * 0.6f, 0f), c + Offset(-s * 0.08f, 0f), s * 0.12f, ZakC.Silver, alpha)
            chain72(c + Offset(s * 0.08f, 0f), c + Offset(s * 0.6f, 0f), s * 0.12f, ZakC.Silver, alpha)
        }
        5 -> {
            drawRect(Color(0xFFE8E2D4), Offset(c.x - s * 0.4f, c.y - s * 0.4f), Size(s * 0.35f, s * 0.8f), alpha = alpha)
            drawRect(Color(0xFFE8E2D4), Offset(c.x + s * 0.05f, c.y - s * 0.4f + s * 0.06f), Size(s * 0.35f, s * 0.8f), alpha = alpha)
        }
        6 -> drawPath(Path().apply {
            moveTo(c.x - s * 0.4f, c.y - s * 0.45f); lineTo(c.x + s * 0.4f, c.y - s * 0.45f); lineTo(c.x + s * 0.4f, c.y)
            quadraticTo(c.x + s * 0.35f, c.y + s * 0.35f, c.x, c.y + s * 0.5f); quadraticTo(c.x - s * 0.35f, c.y + s * 0.35f, c.x - s * 0.4f, c.y); close()
        }, Color(0xFF6A8AA8), alpha = alpha)
        else -> {
            drawLine(Color(0xFF8A7A6A), c + Offset(-s * 0.6f, s * 0.3f), c + Offset(s * 0.6f, s * 0.3f), s * 0.12f, StrokeCap.Round, alpha = alpha)
            fqSack(c + Offset(0f, s * 0.2f), s * 0.6f, Fq.SandDark, alpha)
        }
    }
}

private fun DrawScope.eightRecipientsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    zakNight(t, h * 0.9f)
    val ff = fqIn(a, "firstfour", "anyclass")
    val lf = fqIn(a, "lastfour", "anyclass")
    val ac = fqIn(a, "anyclass", "poorneedy")
    val pn = fqIn(a, "poorneedy", "notdependents")
    val nd = fqIn(a, "notdependents")
    val c = Offset(w * 0.5f, h * 0.5f)
    val spot = { k: Int -> val ang = -PI.toFloat() / 2f + 2f * PI.toFloat() * k / 8f; c + Offset(cos(ang) * w * 0.32f, sin(ang) * h * 0.34f) }
    val ring = maxOf(ff, lf, ac)
    if (ring > 0f) {
        for (k in 0 until 8) drawCircle(ZakC.Rim, w * 0.055f, spot(k), alpha = ring * 0.35f, style = Stroke(1.5f * dp))
        fqCoins(c + Offset(0f, h * 0.05f), w * 0.05f, 4, ring)
    }
    if (ff > 0f) {
        val p = a.b("firstfour")
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.22f * k, 0.22f + 0.22f * k)
            eightIcon79(k, spot(k), w * 0.07f, ff * g, t)
            tagAt(a.tag("firstfour", 0, k), spot(k) + Offset(0f, h * 0.1f * (if (k in 1..3) 1f else -1f)), ff * g * (1f - lf), back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (lf > 0f) {
        val p = a.b("lastfour")
        for (k in 4 until 8) {
            val g = seg(p, 0.05f + 0.22f * (k - 4), 0.22f + 0.22f * (k - 4))
            eightIcon79(k, spot(k), w * 0.07f, lf * g, t)
            tagAt(a.tag("lastfour", 0, k - 4), spot(k) + Offset(0f, h * 0.1f * (if (k in 5..7) -1f else 1f)), lf * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
        for (k in 0 until 4) eightIcon79(k, spot(k), w * 0.07f, lf, t)
    }
    if (ac > 0f) {
        val p = a.b("anyclass")
        for (k in 0 until 8) eightIcon79(k, spot(k), w * 0.07f, ac * 0.6f, t)
        val u = easeInOut(seg(p, 0.15f, 0.5f))
        coin12(c + (spot(1) - c) * u, w * 0.025f, ac)
        tick14(spot(1) + Offset(w * 0.06f, -h * 0.06f), w * 0.03f, seg(p, 0.5f, 0.65f))
        tagAt(a.tag("anyclass"), Offset(w * 0.5f, h * 0.06f), ac * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (pn > 0f) {
        val p = a.b("poorneedy")
        for (k in 0 until 2) {
            val g = seg(p, 0.05f + 0.3f * k, 0.25f + 0.3f * k)
            val b = Offset(w * (0.72f - 0.44f * k), h * 0.42f)
            drawArc(Color(0xFFB07A48), 0f, 180f, true, Offset(b.x - w * 0.12f, b.y - h * 0.1f), Size(w * 0.24f, h * 0.2f), alpha = pn * g * 0.5f)
            val f = if (k == 0) 0.3f else 0.7f
            drawArc(ZakC.Wheat, 0f, 180f, true, Offset(b.x - w * 0.12f * f, b.y - h * 0.1f * f + h * 0.1f * (1f - f)), Size(w * 0.24f * f, h * 0.2f * f), alpha = pn * g)
            tagAt(a.tag("poorneedy", 0, k), b + Offset(0f, h * 0.18f), pn * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
        val yr = seg(p, 0.65f, 0.9f)
        yearRing79(Offset(w * 0.5f, h * 0.42f), h * 0.1f, yr, pn * seg(p, 0.6f, 0.7f))
        tagAt(a.tag("poorneedy", 0, 2), Offset(w * 0.5f, h * 0.12f), pn * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nd > 0f) {
        val p = a.b("notdependents")
        val home = Offset(w * 0.3f, h * 0.8f)
        house12(home, w * 0.22f, Color(0xFFCDBFA6), Color(0xFF6A5A48), nd, lit = 0.6f)
        val go = seg(p, 0.1f, 0.4f)
        val back = seg(p, 0.45f, 0.75f)
        val pos = mix(w * 0.8f, w * 0.42f, go) + (w * 0.8f - w * 0.42f) * back
        coin12(Offset(pos, h * 0.55f - h * 0.08f * sin(PI.toFloat() * (go - back).coerceIn(0f, 1f))), w * 0.025f, nd)
        cross14(Offset(w * 0.42f, h * 0.4f), w * 0.035f, seg(p, 0.35f, 0.5f))
        tagAt(a.tag("notdependents"), Offset(w * 0.6f, h * 0.18f), nd * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}
