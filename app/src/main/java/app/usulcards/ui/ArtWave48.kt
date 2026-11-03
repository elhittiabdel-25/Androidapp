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
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 48 (2026-10-28): «رياض الصالحين»، «مكارم الأخلاق» (2). In the notebook: what charity, pardon and humility do,
 * the strong man who holds his anger, the two mountains that stayed apart. In a town square: the speck of pride, the
 * levelled pedestals, the one who struts and sinks. Two roads from one crossroads; rain on the merciful one's field;
 * a night of hospitality with the lamp put out. Allah is never drawn; the Prophet ﷺ is only light; angels are cool
 * white lights; companions and Ahl al-Bayt are names in roundels.
 */

private val Back48 = Color(0xE6181420)

// ═════════════ 1. the notebook of pardon (riyad-afw) ═════════════

/**
 * Beats: threenever (three sketches with arrows: a jar of coins whose level does not drop as one is given, «صَدَقَةٌ»;
 * a figure who pardons and rises on a step, «بِعَفْوٍ»; one who bows and is lifted, «تَوَاضَعَ»), strongman (a wrestler
 * throws a man down — struck out; one with a flame in his chest closes his hand on it — ticked), mountains (two
 * mountains lean over a town; a cool white light; they stay apart and small lights appear in the town).
 */
internal fun DrawScope.pardonbookScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threenever" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "strongman" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "mountains" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0f) { pardonPage(a) }
}

private fun DrawScope.pardonPage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pp = Paper(a.dark)
    paperSheet(pp)
    val tn = a.b("threenever")
    val sm = a.b("strongman")
    val mt = a.b("mountains")
    val pen = 2.2f * dp
    if (tn > 0f) {
        val f = easeOut(seg(tn, 0f, 0.25f)) * (1f - seg(sm, 0f, 0.4f))
        for (k in 0 until 3) {
            val c = Offset(w * (0.2f + 0.3f * k), h * 0.56f)
            val g = seg(tn, 0.08f + 0.28f * k, 0.32f + 0.28f * k)
            when (k) {
                0 -> {
                    penLine(listOf(c + Offset(-w * 0.06f, -h * 0.16f), c + Offset(-w * 0.07f, h * 0.12f), c + Offset(w * 0.07f, h * 0.12f), c + Offset(w * 0.06f, -h * 0.16f)), g, pp.ink, pen, f)
                    penLine(listOf(c + Offset(-w * 0.065f, -h * 0.02f), c + Offset(w * 0.065f, -h * 0.02f)), g, pp.amber, pen, f)
                    val out = seg(tn, 0.2f, 0.32f)
                    coin12(c + Offset(w * 0.1f * out, -h * 0.2f - h * 0.05f * out), w * 0.018f, f * g, pp.amber)
                }
                1 -> {
                    val up = easeOut(seg(tn, 0.45f, 0.6f))
                    penLine(listOf(c + Offset(-w * 0.07f, h * 0.12f), c + Offset(w * 0.07f, h * 0.12f)), g, pp.ink, pen, f)
                    drawRect(pp.faint, Offset(c.x - w * 0.04f, c.y + h * 0.12f - h * 0.08f * up), Size(w * 0.08f, h * 0.08f * up), alpha = f * 0.5f)
                    figure(c + Offset(0f, h * 0.12f - h * 0.08f * up), h * 0.2f, pp.ink, f * g, arms = 0.4f)
                    arrowHead(c + Offset(w * 0.08f, -h * 0.12f), Offset(0f, -1f), w * 0.02f, pp.amber, pen, f * up)
                    penLine(listOf(c + Offset(w * 0.08f, h * 0.05f), c + Offset(w * 0.08f, -h * 0.12f)), up, pp.amber, pen, f)
                }
                else -> {
                    val up = easeOut(seg(tn, 0.78f, 0.95f))
                    bow12(c + Offset(0f, h * 0.12f - h * 0.08f * up), h * 0.2f, pp.ink, f * g, 0.5f * (1f - up))
                    penLine(listOf(c + Offset(w * 0.08f, h * 0.05f), c + Offset(w * 0.08f, -h * 0.12f)), up, pp.amber, pen, f)
                    arrowHead(c + Offset(w * 0.08f, -h * 0.12f), Offset(0f, -1f), w * 0.02f, pp.amber, pen, f * up)
                }
            }
            note(a.tag("threenever", 0, k), c + Offset(0f, -h * 0.3f), f * g, pp, pp.amber)
        }
    }
    // not the one who throws men down: the one who holds himself
    if (sm > 0f) {
        val f = easeOut(seg(sm, 0f, 0.25f)) * (1f - seg(mt, 0f, 0.4f))
        val ground = h * 0.78f
        penLine(listOf(Offset(w * 0.05f, ground), Offset(w * 0.95f, ground)), seg(sm, 0f, 0.2f), pp.faint, pen * 0.7f, f)
        val throwP = easeInOut(seg(sm, 0.1f, 0.35f))
        figure(Offset(w * 0.24f, ground), h * 0.3f, pp.ink, f, arms = 0.8f)
        // the thrown man falls
        val fallen = Offset(w * 0.34f, ground)
        figure(fallen, h * 0.3f, pp.faint, f, lean = -1.3f * throwP)
        crossMark(Offset(w * 0.28f, h * 0.3f), w * 0.04f, pp.bad, pen * 1.3f, f * seg(sm, 0.4f, 0.5f))
        val c = Offset(w * 0.72f, ground)
        figure(c, h * 0.3f, pp.ink, f)
        val hold = seg(sm, 0.6f, 0.85f)
        val chest = c + Offset(0f, -h * 0.2f)
        val fl = (1f - hold) * (1f + 0.15f * sin(t * 10f))
        drawOval(pp.bad, Offset(chest.x - w * 0.012f * fl, chest.y - h * 0.05f * fl), Size(w * 0.024f * fl, h * 0.06f * fl), alpha = f * seg(sm, 0.5f, 0.6f))
        checkMark(Offset(w * 0.72f, h * 0.3f), w * 0.04f, pp.good, pen * 1.3f, f * seg(sm, 0.8f, 0.95f))
    }
    // the mountains that were not closed over them
    if (mt > 0f) {
        val f = easeOut(seg(mt, 0f, 0.25f))
        val ground = h * 0.8f
        val lean = easeInOut(seg(mt, 0.1f, 0.4f)) * (1f - easeInOut(seg(mt, 0.55f, 0.75f)))
        for (side in listOf(-1f, 1f)) {
            val base = Offset(w * 0.5f + side * w * 0.3f, ground)
            val peak = Offset(w * 0.5f + side * w * (0.24f - 0.12f * lean), h * 0.25f)
            penLine(listOf(base + Offset(side * w * 0.14f, 0f), peak, base + Offset(-side * w * 0.12f, 0f)), seg(mt, 0f, 0.2f), pp.ink, pen, f)
        }
        for (k in 0 until 5) {
            val p = Offset(w * (0.4f + 0.05f * k), ground)
            penLine(listOf(p, p + Offset(0f, -h * 0.06f), p + Offset(w * 0.035f, -h * 0.06f), p + Offset(w * 0.035f, 0f)), seg(mt, 0.05f + 0.03f * k, 0.2f + 0.03f * k), pp.ink, pen * 0.8f, f)
        }
        angelLight26(Offset(w * 0.5f, h * 0.14f), w * 0.012f, t, f * seg(mt, 0.1f, 0.25f) * (1f - seg(mt, 0.6f, 0.8f)))
        for (k in 0 until 7) {
            val g = seg(mt, 0.7f + 0.03f * k, 0.85f + 0.03f * k)
            softGlow(Offset(w * (0.4f + 0.03f * k), ground - h * (0.1f + 0.03f * (k % 3))), w * 0.015f, pp.amber, 0.9f * f * g)
        }
    }
}

// ═════════════ 2. the town square (riyad-tawadu) ═════════════

private object SquareC48 {
    val Sky = Color(0xFFAFCFE4)
    val SkyLow = Color(0xFFF4E6C8)
    val Ground = Color(0xFFD4BC92)
    val Wall = Color(0xFFDCC8A0)
    val Trim = Color(0xFF8A6A44)
    val Person = Color(0xFF3A3040)
    val Robe = Color(0xFF8A4A6A)
    val Gold = Color(0xFFE8B84A)
    val Heart = Color(0xFFE88A7A)
    val Speck = Color(0xFF2A2026)
    val Stone = Color(0xFFB8A27E)
    val Child = Color(0xFF5A4A5A)
}

/**
 * Beats: speck (a heart with a speck of dark in it; a garden gate before it stays shut), beautyok (a fine robe on a
 * stand, ticked; then one who pushes away a scroll and looks down on a small figure — the two tags: «بَطَرُ الْحَقِّ»,
 * «وَغَمْطُ النَّاسِ»), levelout (figures on pedestals of different heights; the pedestals sink level), childrenlead (the
 * light ﷺ greets children; a small girl leads it by the hand down the street), sinkdown (a man in a fine robe struts and
 * the ground takes him down).
 */
internal fun DrawScope.townsquareScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "speck" to Shot(Offset(w * 0.5f, h * 0.5f), 1.25f),
        "beautyok" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "levelout" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "childrenlead" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "sinkdown" to Shot(Offset(w * 0.5f, h * 0.65f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { squareDay(a) }
}

private fun DrawScope.squareDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sk = a.b("speck")
    val bo = a.b("beautyok")
    val lv = a.b("levelout")
    val cl = a.b("childrenlead")
    val sn = a.b("sinkdown")
    val ground = h * 0.84f
    daySky12(SquareC48.Sky, SquareC48.SkyLow, ground, SquareC48.Ground)
    for (k in 0 until 4) house12(Offset(w * (0.12f + 0.25f * k), ground - h * 0.02f), w * 0.2f, SquareC48.Wall, SquareC48.Trim, 0.35f)
    // a speck of pride, and the gate that stays shut
    if (sk > 0f) {
        val f = easeOut(seg(sk, 0f, 0.3f)) * (1f - seg(bo, 0f, 0.4f))
        door12(Offset(w * 0.72f, ground), w * 0.14f, h * 0.5f, 0.35f * (1f - seg(sk, 0.5f, 0.8f)), SquareC48.Trim, Color(0xFF5A3A24), f, Color(0xFFBFE4A0))
        val c = Offset(w * 0.34f, h * 0.48f)
        heart12(c, w * 0.18f, SquareC48.Heart, f, fill = true)
        val sp = seg(sk, 0.25f, 0.45f)
        drawCircle(SquareC48.Speck, w * 0.006f + w * 0.01f * bump(seg(sk, 0.25f, 0.6f)), c + Offset(w * 0.02f, h * 0.02f), alpha = f * sp)
    }
    // beauty is not pride; pride is pushing truth away and looking down on people
    if (bo > 0f) {
        val f = easeOut(seg(bo, 0f, 0.3f)) * (1f - seg(lv, 0f, 0.4f))
        val stand = Offset(w * 0.22f, ground)
        drawLine(SquareC48.Trim, stand, stand + Offset(0f, -h * 0.4f), 3f * dp, StrokeCap.Round, alpha = f)
        drawPath(Path().apply {
            val top = stand + Offset(0f, -h * 0.38f)
            moveTo(top.x - w * 0.06f, top.y); lineTo(top.x + w * 0.06f, top.y); lineTo(top.x + w * 0.08f, top.y + h * 0.3f); lineTo(top.x - w * 0.08f, top.y + h * 0.3f); close()
        }, SquareC48.Robe, alpha = f * seg(bo, 0.05f, 0.2f))
        tick14(stand + Offset(w * 0.08f, -h * 0.46f), w * 0.03f, seg(bo, 0.2f, 0.35f) * f)
        val c = Offset(w * 0.64f, ground)
        val push = seg(bo, 0.45f, 0.65f)
        figure(c, h * 0.34f, SquareC48.Person, f * seg(bo, 0.35f, 0.45f), lean = -0.15f, arms = -0.8f * push)
        scroll14(c + Offset(w * 0.08f + w * 0.08f * push, -h * 0.2f), w * 0.07f, h * 0.06f, 1f, f * seg(bo, 0.4f, 0.5f) * (1f - 0.4f * push))
        figure(Offset(w * 0.5f, ground), h * 0.16f, SquareC48.Child, f * seg(bo, 0.6f, 0.75f))
        tagAt(a.tag("beautyok", 0, 0), c + Offset(w * 0.12f, -h * 0.46f), f * seg(bo, 0.55f, 0.7f), back = Back48, ink = ArtInk.Ivory)
        tagAt(a.tag("beautyok", 0, 1), Offset(w * 0.5f, ground - h * 0.24f), f * seg(bo, 0.7f, 0.85f), back = Back48, ink = ArtInk.Ivory)
    }
    // pedestals sink level
    if (lv > 0f) {
        val f = easeOut(seg(lv, 0f, 0.3f)) * (1f - seg(cl, 0f, 0.4f))
        val down = easeInOut(seg(lv, 0.35f, 0.85f))
        val heights = listOf(0.28f, 0.1f, 0.2f, 0.04f, 0.16f)
        for ((k, ht) in heights.withIndex()) {
            val hh = h * (0.03f + ht * (1f - down))
            val x = w * (0.16f + 0.17f * k)
            drawRect(SquareC48.Stone, Offset(x - w * 0.05f, ground - hh), Size(w * 0.1f, hh), alpha = f)
            figure(Offset(x, ground - hh), h * 0.24f, SquareC48.Person, f, lean = (if (ht > 0.15f) -0.08f else 0f) * (1f - down))
        }
        if (down > 0.95f) drawLine(SquareC48.Gold, Offset(w * 0.08f, ground - h * 0.03f), Offset(w * 0.92f, ground - h * 0.03f), 2f * dp, alpha = f)
    }
    // he greets the children, and a little girl leads him where she wishes
    if (cl > 0f) {
        val f = easeOut(seg(cl, 0f, 0.3f)) * (1f - seg(sn, 0f, 0.4f))
        val go = easeInOut(seg(cl, 0.45f, 0.95f))
        val light = Offset(w * (0.36f - 0.16f * go), h * 0.46f)
        sirajMunir(light, w * 0.014f, f, t, nameAt = light + Offset(0f, -h * 0.13f))
        for (k in 0 until 3) {
            val g = seg(cl, 0.1f + 0.08f * k, 0.25f + 0.08f * k) * (1f - seg(cl, 0.45f, 0.6f))
            nameRoundel(Offset(w * (0.56f + 0.08f * k), ground - h * 0.06f - h * 0.01f * sin(t * 3f + k).coerceAtLeast(0f)), w * 0.022f, null, Roundel.PlainFill, Roundel.PlainRim, f * g)
        }
        nameRoundel(Offset(light.x - w * 0.07f, ground - h * 0.06f), w * 0.022f, null, Roundel.PlainFill, Roundel.PlainRim, f * seg(cl, 0.4f, 0.5f))
    }
    // the one who struts in his robe
    if (sn > 0f) {
        val f = easeOut(seg(sn, 0f, 0.3f))
        val sink = easeInOut(seg(sn, 0.45f, 0.9f))
        val p = Offset(w * (0.3f + 0.2f * seg(sn, 0.05f, 0.45f)), ground + h * 0.3f * sink)
        clipRect(0f, 0f, w, ground) {
            figure(p, h * 0.34f, SquareC48.Robe, f, walk = t * 4f * (1f - sink), lean = 0.1f)
        }
        drawOval(Color(0xFF7A6448), Offset(p.x - w * 0.06f, ground - h * 0.01f), Size(w * 0.12f, h * 0.03f), alpha = f * seg(sn, 0.45f, 0.6f))
    }
}

// ═════════════ 3. two roads from one crossroads (riyad-sidq) ═════════════

private object RoadsC48 {
    val Map = Color(0xFFEFE2C2)
    val MapDark = Color(0xFFCDB88E)
    val Bright = Color(0xFFE8B84A)
    val Dark = Color(0xFF5A4A5A)
    val Garden = Color(0xFF4E8A54)
    val Fire = Color(0xFFD8582E)
    val Smoke = Color(0xFF6A6266)
    val Ink = Color(0xFF3A2E2A)
    val Basket = Color(0xFFB08048)
    val Fruit = Color(0xFFE08A4A)
}

/**
 * Beats: toberr (a map: from a crossroads, the bright road runs through «الْبِرِّ» to a garden, and a walker on it),
 * tofujur (the dark road runs through «الْفُجُورِ» to smoke and fire), fourcalls (four signs of one message stand in a
 * row: «بِالصَّلَاةِ», «وَالصِّدْقِ», «وَالْعَفَافِ», «وَالصِّلَةِ»), market (two traders: when they speak true and show
 * the fault, their basket fills; when they hide it, the goods thin away), bedrank (a man dies on his bed; a small
 * light rises from it to the high rank where the martyrs' lights are).
 */
internal fun DrawScope.truthroadScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "toberr" to Shot(Offset(w * 0.35f, h * 0.45f), 1.1f),
        "tofujur" to Shot(Offset(w * 0.65f, h * 0.6f), 1.1f),
        "fourcalls" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "market" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "bedrank" to Shot(Offset(w * 0.5f, h * 0.45f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { roadsMap(a) }
}

private fun DrawScope.roadsMap(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tb = a.b("toberr")
    val tf = a.b("tofujur")
    val fc = a.b("fourcalls")
    val mk = a.b("market")
    val br = a.b("bedrank")
    drawRect(RoadsC48.Map)
    val step = w * 0.06f
    var x = 0f
    while (x < w) { drawLine(RoadsC48.MapDark, Offset(x, 0f), Offset(x, h), 0.6f * dp, alpha = 0.4f); x += step }
    val cross = Offset(w * 0.5f, h * 0.88f)
    val away = 1f - seg(fc, 0f, 0.4f)
    // the bright road
    if (tb > 0f) {
        val f = easeOut(seg(tb, 0f, 0.3f)) * away
        val p0 = cross; val p1 = Offset(w * 0.42f, h * 0.6f); val p2 = Offset(w * 0.2f, h * 0.5f); val p3 = Offset(w * 0.22f, h * 0.2f)
        val run = easeInOut(seg(tb, 0.05f, 0.7f))
        drawPath(cubicPart(p0, p1, p2, p3, 0f, run), RoadsC48.Bright, alpha = f, style = Stroke(5f * dp, cap = StrokeCap.Round))
        val mid = cubic(p0, p1, p2, p3, 0.5f)
        drawCircle(RoadsC48.Bright, w * 0.02f, mid, alpha = f * seg(tb, 0.35f, 0.45f))
        tagAt(a.tag("toberr", 0, 0), mid + Offset(-w * 0.1f, 0f), f * seg(tb, 0.35f, 0.5f), back = Back48, ink = ArtInk.Gold)
        val g = seg(tb, 0.6f, 0.85f)
        for (k in 0 until 5) palm12(p3 + Offset(w * (0.05f * k - 0.1f), h * 0.04f), h * 0.12f, Color(0xFF6A4A32), RoadsC48.Garden, f * g, t + k)
        tagAt(a.tag("toberr", 0, 1), p3 + Offset(0f, -h * 0.14f), f * g, back = Back48, ink = ArtInk.Gold)
        val walker = cubic(p0, p1, p2, p3, run * 0.95f)
        figure(walker, h * 0.1f, RoadsC48.Ink, f, walk = t * 6f)
    }
    // the dark road
    if (tf > 0f) {
        val f = easeOut(seg(tf, 0f, 0.3f)) * away
        val p0 = cross; val p1 = Offset(w * 0.58f, h * 0.6f); val p2 = Offset(w * 0.82f, h * 0.55f); val p3 = Offset(w * 0.8f, h * 0.28f)
        val run = easeInOut(seg(tf, 0.05f, 0.7f))
        drawPath(cubicPart(p0, p1, p2, p3, 0f, run), RoadsC48.Dark, alpha = f, style = Stroke(5f * dp, cap = StrokeCap.Round))
        val mid = cubic(p0, p1, p2, p3, 0.5f)
        tagAt(a.tag("tofujur", 0, 0), mid + Offset(w * 0.1f, 0f), f * seg(tf, 0.35f, 0.5f), back = Back48, ink = ArtInk.Ivory)
        val g = seg(tf, 0.6f, 0.85f)
        for (k in 0 until 5) {
            val u = fract(t * 0.4f + k / 5f)
            drawCircle(RoadsC48.Smoke, w * 0.02f * (0.6f + u), p3 + Offset(w * 0.02f * sin(t + k), -h * 0.15f * u), alpha = f * g * 0.5f * (1f - u))
        }
        for (k in 0 until 4) drawOval(RoadsC48.Fire, Offset(p3.x - w * 0.04f + w * 0.02f * k, p3.y - h * (0.04f + 0.02f * sin(t * 6f + k))), Size(w * 0.016f, h * 0.06f), alpha = f * g * 0.8f)
        tagAt(a.tag("tofujur", 0, 1), p3 + Offset(0f, -h * 0.2f), f * g, back = Back48, ink = ArtInk.Ivory)
    }
    // four signs of one message
    if (fc > 0f) {
        val f = easeOut(seg(fc, 0f, 0.3f)) * (1f - seg(mk, 0f, 0.4f))
        for (k in 0 until 4) {
            val c = Offset(w * (0.2f + 0.2f * k), h * 0.55f)
            val g = easeOut(seg(fc, 0.1f + 0.18f * k, 0.3f + 0.18f * k))
            drawRoundRect(RoadsC48.MapDark, Offset(c.x - w * 0.07f, c.y - h * 0.22f * g), Size(w * 0.14f, h * 0.3f * g), CornerRadius(4f * dp), alpha = f)
            drawPath(archPath26(c.x, c.y - h * 0.2f * g, c.y + h * 0.04f, w * 0.05f), RoadsC48.Map, alpha = f * g)
            tagAt(a.tag("fourcalls", 0, k), c + Offset(0f, h * 0.16f), f * g, back = Back48, ink = ArtInk.Gold)
        }
    }
    // a true sale and a false one
    if (mk > 0f) {
        val f = easeOut(seg(mk, 0f, 0.3f)) * (1f - seg(br, 0f, 0.4f))
        val ground = h * 0.8f
        drawLine(RoadsC48.MapDark, Offset(w * 0.5f, h * 0.15f), Offset(w * 0.5f, h * 0.9f), 1.5f * dp, alpha = f)
        for (side in 0 until 2) {
            val cx = w * (0.25f + 0.5f * side)
            figure(Offset(cx - w * 0.1f, ground), h * 0.26f, RoadsC48.Ink, f, arms = 0.5f)
            figure(Offset(cx + w * 0.1f, ground), h * 0.26f, RoadsC48.Ink, f, arms = -0.3f)
            cup12(Offset(cx, ground - h * 0.02f), w * 0.1f, RoadsC48.Basket, f)
            val n = if (side == 0) 4 + (6 * seg(mk, 0.3f, 0.8f)).toInt() else (6 * (1f - seg(mk, 0.4f, 0.9f))).toInt()
            for (k in 0 until n) drawCircle(RoadsC48.Fruit, w * 0.012f, Offset(cx - w * 0.035f + w * 0.023f * (k % 4), ground - h * (0.07f + 0.025f * (k / 4))), alpha = f)
            if (side == 0) checkMark(Offset(cx, h * 0.25f), w * 0.03f, Color(0xFF2E8A55), 3f * dp, f * seg(mk, 0.5f, 0.7f))
            else crossMark(Offset(cx, h * 0.25f), w * 0.03f, Color(0xFFC0482E), 3f * dp, f * seg(mk, 0.6f, 0.8f))
        }
    }
    // the bed, and the rank of the martyrs
    if (br > 0f) {
        val f = easeOut(seg(br, 0f, 0.3f))
        val bed = Offset(w * 0.5f, h * 0.84f)
        drawRoundRect(RoadsC48.Basket, Offset(bed.x - w * 0.16f, bed.y - h * 0.04f), Size(w * 0.32f, h * 0.05f), CornerRadius(4f * dp), alpha = f)
        drawLine(RoadsC48.Ink, bed + Offset(-w * 0.1f, -h * 0.06f), bed + Offset(w * 0.12f, -h * 0.055f), h * 0.03f, StrokeCap.Round, alpha = f)
        drawCircle(RoadsC48.Ink, h * 0.025f, bed + Offset(-w * 0.13f, -h * 0.07f), alpha = f)
        val tier = h * 0.2f
        drawRoundRect(RoadsC48.MapDark, Offset(w * 0.2f, tier), Size(w * 0.6f, h * 0.04f), CornerRadius(4f * dp), alpha = f)
        for (k in 0 until 6) softGlow(Offset(w * (0.26f + 0.09f * k), tier - h * 0.04f), w * 0.02f, RoadsC48.Bright, 0.8f * f * seg(br, 0.1f, 0.3f))
        val rise = easeInOut(seg(br, 0.35f, 0.85f))
        val c = Offset(w * 0.5f, bed.y - h * 0.1f - (bed.y - h * 0.1f - tier + h * 0.04f) * rise)
        softGlow(c, w * 0.03f, RoadsC48.Bright, 0.9f * f * seg(br, 0.3f, 0.4f))
    }
}

// ═════════════ 4. rain on the merciful one's field (riyad-rahma) ═════════════

private object MercyC48 {
    val Sky = Color(0xFFC4DCE8)
    val SkyLow = Color(0xFFF2E8D2)
    val Ground = Color(0xFFB89A6E)
    val Dry = Color(0xFFC8A878)
    val Green = Color(0xFF5A9A5A)
    val Rain = Color(0xFF6A9AC0)
    val Person = Color(0xFF3A3040)
    val Hasan = Color(0xFF1E5A46)
    val Arch = Color(0xFFE6D6B4)
    val Floor = Color(0xFFD8C8A6)
    val Wave = Color(0xFFE88A7A)
    val Stone = Color(0xFF8A8890)
    val Heart = Color(0xFFE88A7A)
}

/**
 * Beats: kiss (the light ﷺ bends to a roundel, «الْحَسَنَ», and its warmth wraps it; beside them a plain roundel,
 * «الْأَقْرَعُ بْنُ حَابِسٍ», with ten small rounds about it, none of them lit), mirrorrain (a jug tips and waters a dry
 * plant, and rain falls on that field; the jug that stays upright keeps its field dry), babycry (rows of prayer rugs;
 * a cradle at the back cries; the long arc of the prayer is cut short), stoneheart (hearts of stone in a row; one warm heart beats
 * among them).
 */
internal fun DrawScope.mercyrainScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "kiss" to Shot(Offset(w * 0.5f, h * 0.45f), 1.2f),
        "mirrorrain" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "babycry" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "stoneheart" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { mercyDay(a) }
}

private fun DrawScope.mercyDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ks = a.b("kiss")
    val mr = a.b("mirrorrain")
    val bc = a.b("babycry")
    val sh = a.b("stoneheart")
    val ground = h * 0.84f
    daySky12(MercyC48.Sky, MercyC48.SkyLow, ground, MercyC48.Ground)
    if (ks > 0f) {
        val f = easeOut(seg(ks, 0f, 0.3f)) * (1f - seg(mr, 0f, 0.4f))
        val light = Offset(w * 0.34f, h * 0.36f)
        sirajMunir(light, w * 0.015f, f, t, nameAt = light + Offset(0f, -h * 0.13f))
        val near = easeInOut(seg(ks, 0.15f, 0.45f))
        val c = Offset(w * (0.5f - 0.08f * near), h * 0.5f)
        nameRoundel(c, w * 0.05f, a.big("kiss", 0, 0), MercyC48.Hasan, Roundel.Rim, f * seg(ks, 0.05f, 0.2f))
        softGlow(c, w * 0.07f, ArtInk.Gold, 0.5f * f * near)
        val q = Offset(w * 0.74f, h * 0.5f)
        nameRoundel(q, w * 0.05f, a.big("kiss", 0, 1), Roundel.PlainFill, Roundel.PlainRim, f * seg(ks, 0.45f, 0.6f))
        for (k in 0 until 10) {
            val ang = k * PI.toFloat() / 5f
            drawCircle(Color(0xFF8A8E9A), w * 0.01f, q + Offset(cos(ang) * w * 0.09f, sin(ang) * h * 0.14f), alpha = f * seg(ks, 0.55f + 0.02f * k, 0.65f + 0.02f * k))
        }
    }
    // the merciful are shown mercy
    if (mr > 0f) {
        val f = easeOut(seg(mr, 0f, 0.3f)) * (1f - seg(bc, 0f, 0.4f))
        drawLine(Color(0xFF6A5A4A), Offset(w * 0.5f, h * 0.2f), Offset(w * 0.5f, ground), 1.5f * dp, alpha = f * 0.5f)
        drawRect(MercyC48.Dry, Offset(w * 0.52f, ground), Size(w * 0.48f, h * 0.04f), alpha = f)
        // a jug that stays upright over a dry plant
        cup12(Offset(w * 0.76f, ground - h * 0.18f), w * 0.05f, Color(0xFF8A6A44), f)
        drawLine(Color(0xFF8A7A5A), Offset(w * 0.8f, ground), Offset(w * 0.79f, ground - h * 0.05f), 2f * dp, StrokeCap.Round, alpha = f)
        // a jug that tips and waters a dry plant
        val pour = seg(mr, 0.1f, 0.3f)
        val jug = Offset(w * 0.16f, ground - h * 0.2f)
        rotate(60f * pour, jug) { cup12(jug, w * 0.05f, Color(0xFF8A6A44), f) }
        if (pour > 0.5f) for (k in 0 until 5) drawCircle(MercyC48.Rain, 1.6f * dp, Offset(w * 0.19f, ground - h * 0.16f + h * 0.14f * fract(t * 2f + k / 5f)), alpha = f * (1f - seg(mr, 0.4f, 0.5f)))
        val rainP = seg(mr, 0.4f, 0.6f)
        cloud38(Offset(w * 0.24f, h * 0.2f), w * 0.1f, Color(0xFFE8EEF4), f * rainP)
        rain12(w * 0.08f, w * 0.42f, h * 0.26f, ground, t * 0.7f, MercyC48.Rain, f * rainP * 0.8f, 20)
        val green = seg(mr, 0.5f, 0.95f)
        for (k in 0 until 9) drawLine(MercyC48.Green, Offset(w * (0.06f + 0.045f * k), ground), Offset(w * (0.06f + 0.045f * k), ground - h * 0.07f * (0.3f + green) * (0.6f + 0.4f * (k % 3) / 2f)), 2f * dp, StrokeCap.Round, alpha = f)
    }
    // the prayer cut short for a crying child
    if (bc > 0f) {
        val f = easeOut(seg(bc, 0f, 0.3f)) * (1f - seg(sh, 0f, 0.4f))
        drawRect(Color(0xFFF2E8D0), Offset.Zero, Size(w, ground), alpha = f)
        for (k in 0 until 4) drawPath(archPath26(w * (0.15f + 0.23f * k), h * 0.1f, ground - h * 0.32f, w * 0.08f), MercyC48.Arch, alpha = f)
        drawRect(MercyC48.Floor, Offset(0f, ground), Size(w, h - ground), alpha = f)
        // rows of prayer rugs
        for (row in 0 until 2) for (k in 0 until 5) {
            val x = w * (0.1f + 0.1f * k) + w * 0.02f * row
            val y = ground + h * (0.02f + 0.06f * row)
            drawRoundRect(Color(0xFF8A3A3A), Offset(x, y), Size(w * 0.07f, h * 0.04f), CornerRadius(2f * dp), alpha = f)
            drawPath(archPath26(x + w * 0.035f, y, y + h * 0.02f, w * 0.02f), Color(0xFFE8C46A), alpha = f * 0.7f)
        }
        // a cradle at the back, and its crying
        val cr = Offset(w * 0.82f, ground + h * 0.06f)
        drawArc(Color(0xFF8A6A44), 0f, 180f, true, Offset(cr.x - w * 0.06f, cr.y - h * 0.05f), Size(w * 0.12f, h * 0.08f), alpha = f)
        for (k in 0 until 3) {
            val u = fract(t * 0.9f + k / 3f)
            drawArc(MercyC48.Wave, -150f, 120f, false, Offset(cr.x - w * 0.08f * u, cr.y - h * 0.1f - h * 0.1f * u), Size(w * 0.16f * u, h * 0.14f * u), alpha = f * seg(bc, 0.2f, 0.3f) * (1f - u), style = Stroke(1.6f * dp))
        }
        val long = 1f - 0.6f * easeInOut(seg(bc, 0.4f, 0.75f))
        drawArc(ArtInk.Gold, 200f, 140f * long, false, Offset(w * 0.08f, h * 0.18f), Size(w * 0.44f, h * 0.36f), alpha = f, style = Stroke(3f * dp, cap = StrokeCap.Round))
    }
    // hearts of stone, and one that is warm
    if (sh > 0f) {
        val f = easeOut(seg(sh, 0f, 0.3f))
        for (k in 0 until 5) {
            val c = Offset(w * (0.16f + 0.17f * k), h * 0.5f)
            val warm = k == 2
            val g = seg(sh, 0.05f + 0.07f * k, 0.25f + 0.07f * k)
            if (warm) {
                val beat = 1f + 0.08f * sin(t * 6f)
                softGlow(c, w * 0.08f, MercyC48.Heart, 0.5f * f * g)
                heart12(c, w * 0.11f * beat, MercyC48.Heart, f * g, fill = true)
            } else heart12(c, w * 0.1f, MercyC48.Stone, f * g, fill = true)
        }
    }
}

// ═════════════ 5. a night of hospitality (riyad-karam) ═════════════

private object GuestC48 {
    val Night = Color(0xFF141826)
    val NightLow = Color(0xFF2C2848)
    val Ground = Color(0xFF221E2A)
    val Well = Color(0xFF8A7A6A)
    val Water = Color(0xFF7AB4D8)
    val Wall = Color(0xFF5A4A50)
    val Room = Color(0xFF3A2E30)
    val Flame = Color(0xFFF2B04A)
    val Person = Color(0xFFE6DCC8)
    val Cloth = Color(0xFFB04A3A)
    val Food = Color(0xFFD8A65A)
    val Chest = Color(0xFF6A4A2A)
    val Sheep = Color(0xFFEDE6DA)
    val Green = Color(0xFF6AC08A)
}

/**
 * Beats: basinfill (a basin gives water to many cups and fills again as it gives), twolights (at dawn two cool white
 * lights come down: one over a giver's open door, «بِالْخَلَفِ»; one over a locked chest that crumbles, «بِالتَّلَفِ»),
 * nevernot (the light ﷺ, and those who come to it each leave with something), lampout (a small room: the guest eats by a
 * lamp; the host puts the lamp out and the hosts sit with empty hands — the companions as plain roundels; the children
 * sleep), onecloth (small heaps of food poured on one cloth and shared out in equal heaps among plain roundels), shoulder (a sheep given away all but its shoulder: what was
 * given glows; the shoulder kept fades).
 */
internal fun DrawScope.guestnightScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "basinfill" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "twolights" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "nevernot" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "lampout" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "onecloth" to Shot(Offset(w * 0.5f, h * 0.6f), 1.25f),
        "shoulder" to Shot(Offset(w * 0.5f, h * 0.55f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.15f) { guestNight(a) }
}

private fun DrawScope.guestNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sp = a.b("basinfill")
    val tl = a.b("twolights")
    val nn = a.b("nevernot")
    val lo = a.b("lampout")
    val oc = a.b("onecloth")
    val sd = a.b("shoulder")
    val ground = h * 0.84f
    nightSky(t, GuestC48.Night, GuestC48.NightLow, ground, starAlpha = 0.8f)
    drawRect(GuestC48.Ground, Offset(0f, ground), Size(w, h - ground))
    // the basin that fills as it gives
    if (sp > 0f) {
        val f = easeOut(seg(sp, 0f, 0.3f)) * (1f - seg(tl, 0f, 0.4f))
        val c = Offset(w * 0.5f, ground - h * 0.12f)
        drawRoundRect(GuestC48.Well, Offset(c.x - w * 0.12f, c.y - h * 0.08f), Size(w * 0.24f, h * 0.2f), CornerRadius(6f * dp), alpha = f)
        val level = 0.7f + 0.2f * sin(t * 1.5f)
        drawRect(GuestC48.Water, Offset(c.x - w * 0.11f, c.y - h * 0.08f + h * 0.18f * (1f - level)), Size(w * 0.22f, h * 0.02f), alpha = f)
        for (k in 0 until 6) {
            val u = fract(t * 0.35f + k / 6f)
            val side = if (k % 2 == 0) -1f else 1f
            val p = c + Offset(side * w * (0.14f + 0.28f * u), -h * 0.05f + h * 0.12f * u)
            cup12(p, w * 0.035f, Color(0xFFD8C4A0), f * seg(sp, 0.1f, 0.3f) * (1f - u * 0.3f), GuestC48.Water)
        }
        for (k in 0 until 4) {
            val u = fract(t * 0.8f + k / 4f)
            drawCircle(GuestC48.Water, 2f * dp, c + Offset(w * 0.02f * (k - 1.5f), -h * 0.2f + h * 0.12f * u), alpha = f * 0.8f)
        }
    }
    // two angels at dawn
    if (tl > 0f) {
        val f = easeOut(seg(tl, 0f, 0.3f)) * (1f - seg(nn, 0f, 0.4f))
        drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFFE8A07A).copy(alpha = 0.4f)), h * 0.3f, ground), Offset(0f, h * 0.3f), Size(w, ground - h * 0.3f), alpha = f)
        val left = Offset(w * 0.28f, ground)
        house12(left, w * 0.2f, GuestC48.Wall, Color(0xFF3A2E30), f, 0.8f)
        for (k in 0 until 3) coin12(left + Offset(w * (0.06f * k - 0.06f), h * 0.06f), w * 0.015f, f * seg(tl, 0.5f + 0.1f * k, 0.6f + 0.1f * k))
        val right = Offset(w * 0.72f, ground - h * 0.06f)
        val crumble = seg(tl, 0.55f, 0.9f)
        drawRect(GuestC48.Chest, Offset(right.x - w * 0.08f, right.y - h * 0.1f * (1f - 0.6f * crumble)), Size(w * 0.16f, h * 0.1f * (1f - 0.6f * crumble)), alpha = f)
        padlock21(right + Offset(0f, -h * 0.05f), w * 0.03f, Color(0xFFB0A070), 0f, f * (1f - crumble))
        for (k in 0 until 6) drawCircle(Color(0xFF6A5A4A), w * 0.006f, right + Offset(w * (0.03f * (k - 2.5f)), h * 0.03f * crumble * (k % 3)), alpha = f * crumble)
        for (k in 0 until 2) {
            val g = seg(tl, 0.1f + 0.15f * k, 0.35f + 0.15f * k)
            val c = Offset(if (k == 0) left.x else right.x, h * (0.12f + 0.2f * g))
            angelLight26(c, w * 0.012f, t, f * g)
            tagAt(a.tag("twolights", 0, k), c + Offset(0f, -h * 0.08f), f * seg(tl, 0.4f + 0.15f * k, 0.55f + 0.15f * k), back = Back48, ink = ArtInk.Ivory)
        }
    }
    // never did he say no
    if (nn > 0f) {
        val f = easeOut(seg(nn, 0f, 0.3f)) * (1f - seg(lo, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.4f)
        sirajMunir(c, w * 0.016f, f, t, nameAt = c + Offset(0f, -h * 0.14f))
        for (k in 0 until 4) {
            val side = if (k % 2 == 0) -1f else 1f
            val u = seg(nn, 0.1f + 0.18f * k, 0.5f + 0.18f * k)
            val come = bump(u)
            val p = Offset(w * 0.5f + side * w * (0.42f - 0.24f * come), ground)
            nameRoundel(p + Offset(0f, -h * 0.08f), w * 0.03f, null, Roundel.PlainFill, Roundel.PlainRim, f * seg(nn, 0.05f + 0.18f * k, 0.12f + 0.18f * k))
            if (u > 0.5f) softGlow(p + Offset(0f, -h * 0.14f), w * 0.02f, ArtInk.Gold, 0.9f * f * (1f - seg(u, 0.9f, 1f)))
        }
    }
    // the lamp put out for the guest
    if (lo > 0f) {
        val f = easeOut(seg(lo, 0f, 0.3f)) * (1f - seg(oc, 0f, 0.4f))
        val dark = seg(lo, 0.35f, 0.5f)
        drawRect(lerpColor(GuestC48.Room, Color(0xFF0E0C12), dark * 0.7f), Offset(w * 0.08f, h * 0.2f), Size(w * 0.84f, ground - h * 0.2f), alpha = f)
        val lamp = Offset(w * 0.5f, ground - h * 0.04f)
        val fl = (1f - dark) * (1f + 0.08f * sin(t * 9f))
        drawOval(GuestC48.Flame, Offset(lamp.x - w * 0.006f, lamp.y - h * 0.05f * fl), Size(w * 0.012f, h * 0.04f * fl), alpha = f * (1f - dark))
        softGlow(lamp, w * 0.2f, GuestC48.Flame, 0.4f * f * (1f - dark) + 0.08f * f)
        drawOval(Color(0xFF8A5A3A), Offset(lamp.x - w * 0.025f, lamp.y - h * 0.01f), Size(w * 0.05f, h * 0.02f), alpha = f)
        nameRoundel(Offset(w * 0.3f, ground - h * 0.08f), w * 0.04f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        cup12(Offset(w * 0.38f, ground), w * 0.06f, Color(0xFFC8A87A), f, GuestC48.Food)
        nameRoundel(Offset(w * 0.64f, ground - h * 0.08f + h * 0.01f * sin(t * 3f) * dark), w * 0.04f, null, Roundel.PlainFill, Roundel.PlainRim, f * 0.8f)
        nameRoundel(Offset(w * 0.75f, ground - h * 0.08f), w * 0.04f, null, Roundel.PlainFill, Roundel.PlainRim, f * 0.8f)
        for (k in 0 until 2) drawOval(GuestC48.Person, Offset(w * (0.8f + 0.05f * k), ground - h * 0.03f), Size(w * 0.05f, h * 0.025f), alpha = f * 0.6f)
    }
    // one cloth, equal shares
    if (oc > 0f) {
        val f = easeOut(seg(oc, 0f, 0.3f)) * (1f - seg(sd, 0f, 0.4f))
        drawOval(GuestC48.Cloth, Offset(w * 0.25f, ground - h * 0.06f), Size(w * 0.5f, h * 0.1f), alpha = f)
        val gather = easeInOut(seg(oc, 0.1f, 0.45f))
        val share = easeInOut(seg(oc, 0.55f, 0.9f))
        val sizes = listOf(0.04f, 0.015f, 0.03f, 0.01f, 0.025f)
        for (k in 0 until 5) {
            val start = Offset(w * (0.12f + 0.19f * k), ground - h * 0.16f)
            val mid = Offset(w * 0.5f, ground - h * 0.02f)
            val end = Offset(w * (0.18f + 0.16f * k), ground - h * 0.14f)
            val p = if (share > 0f) mid + (end - mid) * share else start + (mid - start) * gather
            val r = if (share > 0f) mix(0f, 0.024f, share) else sizes[k] * (1f - gather)
            drawCircle(GuestC48.Food, w * r + 0.5f, p, alpha = f)
            nameRoundel(Offset(w * (0.18f + 0.16f * k), ground + h * 0.06f), w * 0.025f, null, Roundel.PlainFill, Roundel.PlainRim, f * share)
        }
        if (share <= 0f) drawCircle(GuestC48.Food, w * 0.06f * gather, Offset(w * 0.5f, ground - h * 0.03f), alpha = f)
    }
    // what is given stays
    if (sd > 0f) {
        val f = easeOut(seg(sd, 0f, 0.3f))
        val c = Offset(w * 0.5f, h * 0.55f)
        val given = seg(sd, 0.3f, 0.7f)
        sheep26(c, h * 0.3f, GuestC48.Sheep, f)
        softGlow(c, w * 0.18f, ArtInk.Gold, 0.5f * f * given)
        val shoulder = c + Offset(w * 0.06f, -h * 0.02f)
        drawCircle(Color(0xFF6A6470), w * 0.03f, shoulder, alpha = f * seg(sd, 0.35f, 0.5f) * (1f - 0.7f * seg(sd, 0.7f, 0.95f)))
        for (k in 0 until 6) {
            val ang = k * PI.toFloat() / 3f + t * 0.3f
            drawCircle(GuestC48.Green, w * 0.008f, c + Offset(cos(ang) * w * 0.2f, sin(ang) * h * 0.2f), alpha = f * given)
        }
    }
}
