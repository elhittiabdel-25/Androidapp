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
 * Wave 78 (2026-11-03): v1.3 fiqh, «رسالة في الدماء الطبيعية للنساء» (Ibn ʿUthaymīn). These rulings are drawn with days only: rows and
 * rings of days, a muted rose for the days of the excuse, a white sign of purity, gold for the prayers and fasts that return, and
 * plain objects (a rug, beads, a book, a cradle, the House). No woman and nothing of the body is ever drawn.
 */

private object DimaC {
    val Top = Color(0xFF1A1830)
    val Low = Color(0xFF3A3058)
    val Cell = Color(0xFF34385A)
    val Rim = Color(0xFF8A8CB0)
    val Rose = Color(0xFFB07A86)
    val Tint = Color(0xFFC8A870)
    val Pure = Color(0xFFF4F1E8)
    val Gold = Color(0xFFF2C860)
    val Grey = Color(0xFF5A5E78)
    val Leaf = Color(0xFF6FBF8A)
}

private fun DrawScope.dimaBack(t: Float) = nightSky(t, DimaC.Top, DimaC.Low, size.height, starAlpha = 0.5f)

/** A row of [n] day cells from the right edge at [right], [cell] wide, coloured by [fill]. */
private fun DrawScope.dayRow78(right: Offset, n: Int, cell: Float, alpha: Float, fill: (Int) -> Color?) {
    if (alpha <= 0.01f) return
    for (i in 0 until n) {
        val tl = Offset(right.x - (i + 1) * cell * 1.12f, right.y - cell / 2f)
        val f = fill(i)
        if (f != null) drawRoundRect(f, tl, Size(cell, cell), CornerRadius(cell * 0.2f), alpha = alpha)
        drawRoundRect(DimaC.Rim, tl, Size(cell, cell), CornerRadius(cell * 0.2f), alpha = alpha * 0.6f, style = Stroke(1.2f * density))
    }
}

/** The centre of day [i] in a row drawn by [dayRow78]. */
private fun dayAt78(right: Offset, i: Int, cell: Float): Offset = Offset(right.x - (i + 1) * cell * 1.12f + cell / 2f, right.y)

/** A white sign of purity. */
private fun DrawScope.pureSign78(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 2.5f, DimaC.Pure, 0.6f * alpha)
    drawCircle(DimaC.Pure, r, c, alpha = alpha)
}

/** A cradle with a sleeping bundle in it, its base at [foot]. */
private fun DrawScope.cradle78(foot: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawArc(Color(0xFFB08A6A), 0f, 180f, true, Offset(foot.x - s * 0.5f, foot.y - s * 0.55f), Size(s, s * 0.5f), alpha = alpha)
    drawRoundRect(Color(0xFFD8C4E8), Offset(foot.x - s * 0.35f, foot.y - s * 0.38f), Size(s * 0.5f, s * 0.14f), CornerRadius(s * 0.07f), alpha = alpha)
    drawCircle(Color(0xFFF0E4D0), s * 0.1f, Offset(foot.x + s * 0.24f, foot.y - s * 0.34f), alpha = alpha)
    for (sg in listOf(-1f, 1f)) drawLine(Color(0xFF8A6A4A), Offset(foot.x + sg * s * 0.35f, foot.y - s * 0.1f), Offset(foot.x + sg * s * 0.48f, foot.y + s * 0.08f), s * 0.04f, StrokeCap.Round, alpha = alpha)
}

// ═════════════ 1. dima-hayd: a natural rhythm ═════════════

/**
 * Beats: monthrhythm (a ring of a month's days turns, a few muted days recurring in it like the moon's phases: «دَمٌ طَبِيعِيٌّ», «لَيْسَ لَهُ سَبَبٌ
 * مِنْ مَرَضٍ أَوْ جُرْحٍ»), seednourish (a seed in the earth drawing nourishment up its roots: «وَمِنْ حِكْمَتِهِ»), nolimits (a ruler whose end
 * marks fade away: «لَا حَدَّ لِسِنِّ الْحَيْضِ وَلَا لِأَقَلِّهِ وَلَا لِأَكْثَرِهِ»), usualyears (a line of years with a band where it is most
 * usual, its edges soft: «اثْنَتَيْ عَشْرَةَ سَنَةً», «إِلَى خَمْسِينَ»), easerule (a gentle road that lights: «الْيُسْرُ وَالسُّهُولَةُ»).
 */
internal fun DrawScope.naturalcycleScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "monthrhythm" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "seednourish" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "nolimits" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "usualyears" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "easerule" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { naturalCycleDraw(a) }
}

private fun DrawScope.naturalCycleDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    dimaBack(t)
    val mr = fqIn(a, "monthrhythm", "seednourish")
    val sn = fqIn(a, "seednourish", "nolimits")
    val nl = fqIn(a, "nolimits", "usualyears")
    val uy = fqIn(a, "usualyears", "easerule")
    val er = fqIn(a, "easerule")
    if (mr > 0f) {
        val p = a.b("monthrhythm")
        val c = Offset(w * 0.5f, h * 0.5f)
        val r = h * 0.3f
        val turn = t * 0.15f
        for (i in 0 until 28) {
            val ang = 2f * PI.toFloat() * (i / 28f) - turn
            val pos = c + Offset(cos(ang), sin(ang)) * r
            val g = seg(p, i * 0.012f, i * 0.012f + 0.1f)
            drawCircle(if (i < 6) DimaC.Rose else DimaC.Rim, w * 0.012f, pos, alpha = mr * g * (if (i < 6) 0.9f else 0.5f))
        }
        moon12(c, w * 0.04f, mr * seg(p, 0.3f, 0.5f))
        tagAt(a.tag("monthrhythm", 0, 0), Offset(w * 0.5f, h * 0.1f), mr * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("monthrhythm", 0, 1), Offset(w * 0.5f, h * 0.9f), mr * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (sn > 0f) {
        val p = a.b("seednourish")
        val soil = h * 0.55f
        drawRect(Color(0xFF3A2E24), Offset(0f, soil), Size(w, h - soil), alpha = sn)
        val seed = Offset(w * 0.5f, soil + h * 0.12f)
        drawOval(Color(0xFFC8A870), Offset(seed.x - w * 0.03f, seed.y - h * 0.025f), Size(w * 0.06f, h * 0.05f), alpha = sn)
        val grow = seg(p, 0.2f, 0.7f)
        for (k in 0 until 5) {
            val ang = PI.toFloat() * (0.15f + 0.175f * k)
            drawLine(Color(0xFF8A6A4A), seed, seed + Offset(cos(ang), sin(ang)) * (h * 0.2f * grow), 2f * dp, StrokeCap.Round, alpha = sn)
            val u = fract(t * 0.5f + k / 5f)
            drawCircle(DimaC.Gold, w * 0.006f, seed + Offset(cos(ang), sin(ang)) * (h * 0.2f * grow * (1f - u)), alpha = sn * grow * 0.8f)
        }
        drawLine(DimaC.Leaf, seed, seed + Offset(0f, -h * 0.2f * grow), 3f * dp, StrokeCap.Round, alpha = sn)
        drawOval(DimaC.Leaf, Offset(seed.x, seed.y - h * 0.2f * grow - h * 0.02f), Size(w * 0.05f * grow, h * 0.03f), alpha = sn)
        tagAt(a.tag("seednourish"), Offset(w * 0.5f, h * 0.18f), sn * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nl > 0f) {
        val p = a.b("nolimits")
        val y = h * 0.5f
        drawLine(DimaC.Rim, Offset(w * 0.12f, y), Offset(w * 0.88f, y), 3f * dp, StrokeCap.Round, alpha = nl)
        for (k in 0 until 11) drawLine(DimaC.Rim, Offset(w * (0.12f + 0.076f * k), y - h * 0.02f), Offset(w * (0.12f + 0.076f * k), y + h * 0.02f), 1.5f * dp, alpha = nl * 0.6f)
        val fade = seg(p, 0.3f, 0.7f)
        for (x in listOf(0.3f, 0.7f)) {
            drawLine(Color(0xFFC0503A), Offset(w * x, y - h * 0.12f), Offset(w * x, y + h * 0.12f), 4f * dp, StrokeCap.Round, alpha = nl * (1f - fade))
            for (k in 0 until 4) drawCircle(Color(0xFFC0503A), w * 0.006f, Offset(w * x + (k - 1.5f) * w * 0.02f * fade * 3f, y - h * 0.12f * fade), alpha = nl * bump(fade))
        }
        tagAt(a.tag("nolimits"), Offset(w * 0.5f, h * 0.24f), nl * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (uy > 0f) {
        val p = a.b("usualyears")
        val y = h * 0.52f
        val x0 = w * 0.9f
        val x1 = w * 0.1f
        drawLine(DimaC.Rim, Offset(x0, y), Offset(x1, y), 2.5f * dp, StrokeCap.Round, alpha = uy)
        val a12 = mix(x0, x1, 12f / 70f)
        val a50 = mix(x0, x1, 50f / 70f)
        val g = seg(p, 0.1f, 0.4f)
        for (k in 0 until 3) drawRoundRect(DimaC.Rose, Offset(a50 - w * 0.02f * k, y - h * 0.035f), Size(a12 - a50 + w * 0.04f * k, h * 0.07f), CornerRadius(h * 0.03f), alpha = uy * g * (0.5f - 0.15f * k))
        tagAt(a.tag("usualyears", 0, 0), Offset(a12, y + h * 0.1f), uy * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("usualyears", 0, 1), Offset(a50, y + h * 0.1f), uy * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (er > 0f) {
        val p = a.b("easerule")
        val road = Path().apply { moveTo(w * 0.5f, h * 0.98f); cubicTo(w * 0.1f, h * 0.8f, w * 0.85f, h * 0.55f, w * 0.5f, h * 0.35f) }
        drawPath(road, Color(0xFF4A3E52), alpha = er, style = Stroke(w * 0.06f, cap = StrokeCap.Round))
        drawPath(road, DimaC.Gold, alpha = er * seg(p, 0.2f, 0.5f) * 0.7f, style = Stroke(w * 0.012f, cap = StrokeCap.Round))
        softGlow(Offset(w * 0.5f, h * 0.33f), w * 0.1f, DimaC.Gold, 0.5f * er * seg(p, 0.4f, 0.6f))
        tagAt(a.tag("easerule"), Offset(w * 0.5f, h * 0.18f), er * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. dima-tawari: when the habit changes ═════════════

/**
 * Beats: shiftingdays (a run of muted days slides earlier and later, grows and shrinks, and the rule holds: «مَتَى رَأَتِ الدَّمَ فَهِيَ حَائِضٌ»,
 * «وَمَتَى طَهُرَتْ مِنْهُ فَهِيَ طَاهِرٌ»), tintedcells (tinted days inside the run count with it; after the sign of purity they do not: «فِي زَمَنِ
 * الْحَيْضِ حَيْضٌ», «وَبَعْدَ الطُّهْرِ لَيْسَتْ بِحَيْضٍ»), whitesign (a white sign closes the run and the days after it turn gold: «الْقَصَّةُ
 * الْبَيْضَاءُ عَلَامَةُ الطُّهْرِ»), pregnantrule (a cradle over a row of days with no muted run in it: «وَالْغَالِبُ أَنَّ الْحَامِلَ لَا
 * تَحِيضُ»).
 */
internal fun DrawScope.cyclechangesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "shiftingdays" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "tintedcells" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "whitesign" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "pregnantrule" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { cycleChangesDraw(a) }
}

private fun DrawScope.cycleChangesDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    dimaBack(t)
    val sd = fqIn(a, "shiftingdays", "tintedcells")
    val tc = fqIn(a, "tintedcells", "whitesign")
    val ws = fqIn(a, "whitesign", "pregnantrule")
    val pr = fqIn(a, "pregnantrule")
    val cell = w * 0.055f
    val right = Offset(w * 0.5f + cell * 1.12f * 7.5f, h * 0.5f)
    if (sd > 0f) {
        val p = a.b("shiftingdays")
        val start = (3f + 2.5f * sin(t * 0.9f)).toInt()
        val len = (5f + 2f * sin(t * 0.6f + 1f)).toInt()
        dayRow78(right, 15, cell, sd) { i -> if (i in start until start + len) DimaC.Rose else DimaC.Cell }
        tagAt(a.tag("shiftingdays", 0, 0), Offset(w * 0.5f, h * 0.25f), sd * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("shiftingdays", 0, 1), Offset(w * 0.5f, h * 0.75f), sd * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (tc > 0f) {
        val p = a.b("tintedcells")
        dayRow78(right, 15, cell, tc) { i ->
            when (i) {
                in 2..5 -> DimaC.Rose
                6 -> DimaC.Tint
                in 9..10 -> DimaC.Tint.copy(alpha = 0.5f)
                else -> DimaC.Cell
            }
        }
        val g0 = seg(p, 0.1f, 0.3f)
        val run = dayAt78(right, 4, cell)
        drawRoundRect(DimaC.Rose, Offset(dayAt78(right, 6, cell).x - cell * 0.62f, run.y - cell * 0.7f), Size(cell * 1.12f * 5f + cell * 0.12f, cell * 1.4f), CornerRadius(cell * 0.3f), alpha = tc * g0, style = Stroke(2.5f * density))
        tagAt(a.tag("tintedcells", 0, 0), Offset(run.x, h * 0.28f), tc * g0, back = Fq.Back, ink = ArtInk.Ivory)
        pureSign78(dayAt78(right, 7, cell) + Offset(0f, -cell * 1.1f), cell * 0.18f, tc * seg(p, 0.4f, 0.55f))
        val g1 = seg(p, 0.55f, 0.75f)
        for (i in 9..10) cross14(dayAt78(right, i, cell) + Offset(0f, -cell * 1.1f), cell * 0.3f, g1)
        tagAt(a.tag("tintedcells", 0, 1), Offset(dayAt78(right, 10, cell).x, h * 0.74f), tc * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ws > 0f) {
        val p = a.b("whitesign")
        val close = seg(p, 0.2f, 0.4f)
        dayRow78(right, 15, cell, ws) { i -> if (i in 2..6) DimaC.Rose else if (i > 7 && seg(p, 0.45f + 0.03f * (i - 8), 0.55f + 0.03f * (i - 8)) > 0.5f) DimaC.Gold.copy(alpha = 0.75f) else DimaC.Cell }
        pureSign78(dayAt78(right, 7, cell), cell * 0.32f, ws * close)
        tagAt(a.tag("whitesign"), Offset(w * 0.5f, h * 0.25f), ws * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (pr > 0f) {
        val p = a.b("pregnantrule")
        cradle78(Offset(w * 0.5f, h * 0.38f), w * 0.2f, pr * seg(p, 0f, 0.2f))
        dayRow78(right + Offset(0f, h * 0.15f), 15, cell, pr) { DimaC.Cell }
        tagAt(a.tag("pregnantrule"), Offset(w * 0.5f, h * 0.86f), pr * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 3. dima-salah: prayer and remembrance ═════════════

/**
 * Beats: noprayermakeup (a prayer rug folded aside, the five prayer marks of those days dimmed and none of them carried later: «وَلَا تَقْضِيهَا
 * إِذَا طَهُرَتْ»), onerakahtime (a prayer's time as a bar; a rakʿa fits before its edge and the prayer lights: «مِقْدَارَ رَكْعَةٍ كَامِلَةٍ», «وَجَبَتْ
 * عَلَيْهَا تِلْكَ الصَّلَاةُ»), dhikrallowed (beads, raised hands and a listening ear, each ticked: «الذِّكْرُ وَالتَّسْبِيحُ», «وَالدُّعَاءُ»,
 * «وَاسْتِمَاعُ الْقُرْآنِ»), readbyheart (an open book and a heart that lights as the lines are followed: «فَتَقْرَأُ بِقَلْبِهَا»).
 */
internal fun DrawScope.excusedprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "noprayermakeup" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "onerakahtime" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "dhikrallowed" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "readbyheart" to Shot(Offset(w * 0.5f, h * 0.5f), 1.08f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { excusedPrayerDraw(a) }
}

private fun DrawScope.excusedPrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    dimaBack(t)
    val np = fqIn(a, "noprayermakeup", "onerakahtime")
    val ot = fqIn(a, "onerakahtime", "dhikrallowed")
    val da = fqIn(a, "dhikrallowed", "readbyheart")
    val rb = fqIn(a, "readbyheart")
    if (np > 0f) {
        val p = a.b("noprayermakeup")
        val fold = seg(p, 0.05f, 0.3f)
        fqRug(Offset(w * 0.18f, h * 0.55f), w * 0.12f, h * 0.36f * (1f - 0.5f * fold), Color(0xFF6A2A34), DimaC.Gold, np)
        for (d in 0 until 5) for (k in 0 until 5) {
            val c = Offset(w * (0.85f - 0.11f * d), h * (0.28f + 0.1f * k))
            drawCircle(if (d < 3) DimaC.Grey else DimaC.Gold, w * 0.012f, c, alpha = np * (if (d < 3) 0.5f else 0.85f * seg(p, 0.5f, 0.7f)))
        }
        for (d in 0 until 3) drawRoundRect(DimaC.Rose, Offset(w * (0.85f - 0.11f * d) - w * 0.03f, h * 0.2f), Size(w * 0.06f, h * 0.53f), CornerRadius(w * 0.02f), alpha = np * 0.25f)
        tagAt(a.tag("noprayermakeup"), Offset(w * 0.6f, h * 0.88f), np * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ot > 0f) {
        val p = a.b("onerakahtime")
        val y = h * 0.55f
        val x0 = w * 0.85f
        val x1 = w * 0.15f
        drawLine(DimaC.Cell, Offset(x0, y), Offset(x1, y), h * 0.08f, StrokeCap.Round, alpha = ot)
        val pure = mix(x0, x1, 0.82f)
        pureSign78(Offset(pure, y - h * 0.12f), w * 0.015f, ot * seg(p, 0.05f, 0.2f))
        drawLine(DimaC.Rose, Offset(x0, y), Offset(pure, y), h * 0.08f, StrokeCap.Round, alpha = ot * 0.5f)
        val fit = seg(p, 0.25f, 0.5f)
        rakah74(Offset(mix(pure, x1 + w * 0.03f, fit * 0.6f), y - h * 0.06f), h * 0.14f, fit, ot)
        tagAt(a.tag("onerakahtime", 0, 0), Offset(w * 0.5f, h * 0.24f), ot * seg(p, 0.25f, 0.45f), back = Fq.Back, ink = ArtInk.Ivory)
        val lit = seg(p, 0.6f, 0.75f)
        softGlow(Offset(w * 0.5f, y), w * 0.3f, DimaC.Gold, 0.3f * ot * lit)
        drawLine(DimaC.Gold, Offset(x0, y + h * 0.08f), Offset(x1, y + h * 0.08f), 3f * dp, StrokeCap.Round, alpha = ot * lit)
        tagAt(a.tag("onerakahtime", 0, 1), Offset(w * 0.5f, h * 0.84f), ot * lit, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (da > 0f) {
        val p = a.b("dhikrallowed")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.25f * k, 0.25f + 0.25f * k)
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.48f)
            when (k) {
                0 -> beads12(c, w * 0.07f, DimaC.Gold, da * g, t * 0.4f)
                1 -> hands12(c + Offset(0f, h * 0.04f), h * 0.2f, Color(0xFFDCD4C4), da * g)
                else -> {
                    ear73(c, w * 0.1f, Color(0xFFDCD4C4), da * g)
                    voice12(c + Offset(w * 0.08f, 0f), w * 0.05f, t, DimaC.Gold, da * g, -1f)
                }
            }
            tick14(c + Offset(0f, -h * 0.17f), w * 0.03f, seg(p, 0.15f + 0.25f * k, 0.3f + 0.25f * k))
            tagAt(a.tag("dhikrallowed", 0, k), c + Offset(0f, h * 0.18f), da * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (rb > 0f) {
        val p = a.b("readbyheart")
        val c = Offset(w * 0.5f, h * 0.62f)
        openBook21(c, w * 0.4f, Color(0xFFF2E8CE), Color(0xFFB8A888), rb, glow = seg(p, 0.3f, 0.6f))
        val follow = fract(t * 0.3f)
        drawCircle(DimaC.Gold, w * 0.008f, c + Offset(w * (0.15f - 0.3f * follow), -h * 0.04f), alpha = rb * 0.8f)
        val heart = Offset(w * 0.5f, h * 0.28f)
        softGlow(heart, w * 0.08f, DimaC.Gold, rb * seg(p, 0.3f, 0.5f))
        heart12(heart, w * 0.05f, DimaC.Gold, rb * seg(p, 0.3f, 0.5f), fill = true)
        tagAt(a.tag("readbyheart"), Offset(w * 0.5f, h * 0.1f), rb * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. dima-siyam: fasting, tawaf and the rites ═════════════

/**
 * Beats: fastmakeup (the crescent of a fasting day, set aside, and the same days lit later on: «وَيَجِبُ عَلَيْهَا قَضَاءُ الْفَرْضِ مِنْهُ»),
 * ritesbutawaf (the round of the House struck out; the hills, ʿArafa, the tents and the pillars ticked: «الطَّوَافُ بِالْبَيْتِ», «كَالسَّعْيِ
 * وَالْوُقُوفِ بِعَرَفَةَ وَالْمَبِيتِ وَرَمْيِ الْجِمَارِ»), purifiedwash (a white sign, then water poured from a jug: «وَجَبَ عَلَيْهَا أَنْ
 * تَغْتَسِلَ»), aftersunset (the sun goes down before the sign appears, and the day's fast stands: «فَصَوْمُهَا صَحِيحٌ عِنْدَ الْمُؤَلِّفِ»),
 * farewellwaived (the road out of Mecca; the farewell round is lifted from her, drawn faint and gold: «وَيَسْقُطُ عَنْهَا طَوَافُ الْوَدَاعِ»).
 */
internal fun DrawScope.excusedfastScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fastmakeup" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "ritesbutawaf" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "purifiedwash" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "aftersunset" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "farewellwaived" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { excusedFastDraw(a) }
}

private fun DrawScope.excusedFastDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    dimaBack(t)
    val fm = fqIn(a, "fastmakeup", "ritesbutawaf")
    val rt = fqIn(a, "ritesbutawaf", "purifiedwash")
    val pw = fqIn(a, "purifiedwash", "aftersunset")
    val asn = fqIn(a, "aftersunset", "farewellwaived")
    val fw = fqIn(a, "farewellwaived")
    if (fm > 0f) {
        val p = a.b("fastmakeup")
        val cell = w * 0.05f
        val rowA = Offset(w * 0.9f, h * 0.38f)
        val rowB = Offset(w * 0.9f, h * 0.62f)
        crescent12(Offset(w * 0.1f, h * 0.38f), w * 0.025f, DimaC.Gold, DimaC.Top, fm)
        dayRow78(rowA, 14, cell, fm) { i -> if (i in 4..8) DimaC.Rose.copy(alpha = 0.5f) else DimaC.Gold.copy(alpha = 0.55f) }
        val later = seg(p, 0.4f, 0.75f)
        dayRow78(rowB, 14, cell, fm * seg(p, 0.3f, 0.4f)) { i -> if (i < 5 && later * 5f > i) DimaC.Gold.copy(alpha = 0.85f) else null }
        tagAt(a.tag("fastmakeup"), Offset(w * 0.5f, h * 0.85f), fm * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (rt > 0f) {
        val p = a.b("ritesbutawaf")
        val k0 = Offset(w * 0.18f, h * 0.55f)
        fqCircling(k0, w * 0.1f, h * 0.04f, t, Color(0xFFDCD4C4), rt * (1f - 0.6f * seg(p, 0.15f, 0.3f)), 10)
        kaaba29(k0 + Offset(0f, h * 0.02f), w * 0.08f, rt)
        cross14(k0 + Offset(0f, -h * 0.2f), w * 0.035f, seg(p, 0.15f, 0.3f))
        tagAt(a.tag("ritesbutawaf", 0, 0), Offset(k0.x, h * 0.84f), rt * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Ember)
        for (k in 0 until 4) {
            val g = seg(p, 0.35f + 0.1f * k, 0.45f + 0.1f * k)
            val c = Offset(w * (0.88f - 0.15f * k), h * 0.55f)
            when (k) {
                0 -> { fqMound(c + Offset(-w * 0.03f, h * 0.04f), w * 0.05f, h * 0.05f, Fq.Stone, rt * g); fqMound(c + Offset(w * 0.03f, h * 0.04f), w * 0.05f, h * 0.05f, Fq.Stone, rt * g) }
                1 -> fqMound(c + Offset(0f, h * 0.04f), w * 0.1f, h * 0.1f, Color(0xFFA9824A), rt * g)
                2 -> fqTent(c + Offset(0f, h * 0.04f), w * 0.1f, Fq.White, rt * g)
                else -> fqPillar(c + Offset(0f, h * 0.04f), w * 0.02f, h * 0.12f, Fq.Stone, rt * g)
            }
            tick14(c + Offset(0f, -h * 0.13f), w * 0.025f, seg(p, 0.4f + 0.1f * k, 0.5f + 0.1f * k))
        }
        tagAt(a.tag("ritesbutawaf", 0, 1), Offset(w * 0.66f, h * 0.84f), rt * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (pw > 0f) {
        val p = a.b("purifiedwash")
        pureSign78(Offset(w * 0.3f, h * 0.45f), w * 0.03f, pw * seg(p, 0.05f, 0.25f))
        fqEwer(Offset(w * 0.65f, h * 0.7f), h * 0.32f, Color(0xFF6A8AA8), pw, pour = seg(p, 0.35f, 0.6f), t = t)
        tagAt(a.tag("purifiedwash"), Offset(w * 0.5f, h * 0.14f), pw * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (asn > 0f) {
        val p = a.b("aftersunset")
        val horizon = h * 0.6f
        drawRect(Color(0xFF2A2436), Offset(0f, horizon), Size(w, h - horizon), alpha = asn)
        val sun = Offset(w * 0.3f, mix(horizon - h * 0.15f, horizon + h * 0.06f, easeInOut(seg(p, 0f, 0.4f))))
        sun12(sun, w * 0.04f, asn * (1f - seg(p, 0.35f, 0.45f)))
        drawRect(Color(0xFF2A2436), Offset(0f, horizon), Size(w, h * 0.1f), alpha = asn)
        pureSign78(Offset(w * 0.65f, horizon + h * 0.12f), w * 0.012f, 0f)
        drawCircle(DimaC.Rose, w * 0.012f, Offset(w * 0.65f, horizon + h * 0.12f), alpha = asn * seg(p, 0.5f, 0.6f))
        crescent12(Offset(w * 0.75f, h * 0.2f), w * 0.03f, DimaC.Gold, DimaC.Top, asn * seg(p, 0.4f, 0.5f))
        tick14(Offset(w * 0.75f, h * 0.36f), w * 0.035f, seg(p, 0.6f, 0.75f))
        tagAt(a.tag("aftersunset"), Offset(w * 0.5f, h * 0.88f), asn * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (fw > 0f) {
        val p = a.b("farewellwaived")
        val k0 = Offset(w * 0.2f, h * 0.6f)
        kaaba29(k0, w * 0.08f, fw)
        drawOval(DimaC.Gold, Offset(k0.x - w * 0.1f, k0.y - h * 0.04f), Size(w * 0.2f, h * 0.08f), alpha = fw * 0.5f * (1f - 0.6f * seg(p, 0.3f, 0.6f)), style = Stroke(2f * dp))
        fqRoad(Offset(w * 0.3f, h * 0.7f), Offset(w * 0.95f, h * 0.7f), h * 0.05f, Color(0xFF4A3E52), fw)
        val u = easeInOut(seg(p, 0.2f, 0.9f))
        drawCircle(Color(0xFFDCD4C4), w * 0.012f, Offset(mix(w * 0.32f, w * 0.9f, u), h * 0.7f), alpha = fw)
        tagAt(a.tag("farewellwaived"), Offset(w * 0.55f, h * 0.3f), fw * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. dima-istihada: a flow that does not stop ═════════════

/**
 * Beats: unbrokenline (a row of days with a thin muted line running through all of them without a break: «اسْتِمْرَارُ الدَّمِ»), knownhabit (last
 * month's usual run is laid over this one; those days count, the rest are something else: «رَجَعَتْ إِلَيْهِ», «وَمَا عَدَاهُ اسْتِحَاضَةٌ»),
 * wudueachprayer (five prayer marks, each with its own drop of wuḍūʾ when its time comes in: «كَالطَّاهِرَاتِ فِي الْأَحْكَامِ», «الْوُضُوءُ لِكُلِّ
 * صَلَاةٍ بَعْدَ دُخُولِ وَقْتِهَا»), threecases (three branches: a known habit, a difference that can be told, and the habit of most women:
 * «حَيْضٌ مَعْلُومٌ», «تُمَيِّزُ دَمَ الْحَيْضِ بِصِفَتِهِ», «عَادَةِ غَالِبِ النِّسَاءِ»).
 */
internal fun DrawScope.continuousflowScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "unbrokenline" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "knownhabit" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "wudueachprayer" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "threecases" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { continuousFlowDraw(a) }
}

private fun DrawScope.continuousFlowDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    dimaBack(t)
    val ub = fqIn(a, "unbrokenline", "knownhabit")
    val kh = fqIn(a, "knownhabit", "wudueachprayer")
    val wp = fqIn(a, "wudueachprayer", "threecases")
    val tc = fqIn(a, "threecases")
    val cell = w * 0.052f
    val right = Offset(w * 0.5f + cell * 1.12f * 7.5f, h * 0.55f)
    if (ub > 0f) {
        val p = a.b("unbrokenline")
        dayRow78(right, 15, cell, ub) { DimaC.Cell }
        val run = seg(p, 0.05f, 0.6f)
        drawLine(DimaC.Rose, Offset(right.x - cell * 0.2f, right.y), Offset(right.x - cell * 0.2f - (15 * cell * 1.12f) * run, right.y), cell * 0.18f, StrokeCap.Round, alpha = ub)
        tagAt(a.tag("unbrokenline"), Offset(w * 0.5f, h * 0.28f), ub * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (kh > 0f) {
        val p = a.b("knownhabit")
        val above = Offset(right.x, h * 0.32f)
        dayRow78(above, 15, cell, kh * seg(p, 0.05f, 0.2f)) { i -> if (i < 6) DimaC.Rose else DimaC.Cell.copy(alpha = 0.5f) }
        val drop = seg(p, 0.25f, 0.5f)
        dayRow78(right, 15, cell, kh) { i -> if (i < 6 && drop > 0.9f) DimaC.Rose else if (i >= 6 && seg(p, 0.55f, 0.75f) > 0.5f) DimaC.Grey else DimaC.Cell }
        drawLine(DimaC.Rose, Offset(right.x - cell * 0.2f, right.y), Offset(right.x - cell * 0.2f - 15 * cell * 1.12f, right.y), cell * 0.12f, StrokeCap.Round, alpha = kh * 0.5f)
        tagAt(a.tag("knownhabit", 0, 0), Offset(dayAt78(right, 3, cell).x, h * 0.78f), kh * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("knownhabit", 0, 1), Offset(dayAt78(right, 11, cell).x, h * 0.78f), kh * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (wp > 0f) {
        val p = a.b("wudueachprayer")
        tagAt(a.tag("wudueachprayer", 0, 0), Offset(w * 0.5f, h * 0.15f), wp * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Jade)
        for (k in 0 until 5) {
            val g = seg(p, 0.3f + 0.1f * k, 0.4f + 0.1f * k)
            val c = Offset(w * (0.82f - 0.16f * k), h * 0.55f)
            drawCircle(DimaC.Gold, w * 0.025f, c, alpha = wp * (0.3f + 0.7f * g))
            fqDrop(c + Offset(0f, -h * 0.12f), w * 0.018f, Fq.Water, wp * g)
        }
        tagAt(a.tag("wudueachprayer", 0, 1), Offset(w * 0.5f, h * 0.84f), wp * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tc > 0f) {
        val p = a.b("threecases")
        val root = Offset(w * 0.5f, h * 0.18f)
        drawCircle(DimaC.Rose, w * 0.02f, root, alpha = tc)
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.3f * k, 0.25f + 0.3f * k)
            val end = Offset(w * (0.82f - 0.32f * k), h * 0.6f)
            drawLine(DimaC.Rim, root, root + (end - root) * easeOut(g), 2f * dp, StrokeCap.Round, alpha = tc)
            drawCircle(DimaC.Gold, w * 0.018f, end, alpha = tc * g)
            tagAt(a.tag("threecases", 0, k), end + Offset(0f, h * 0.12f), tc * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
}

// ═════════════ 6. dima-nifas: after a birth ═════════════

/**
 * Beats: cradlebegins (a cradle, and a row of forty days begins beside it: «بِسَبَبِ الْوِلَادَةِ»), pureearly (the sign of purity comes before
 * the fortieth day and the days after it turn gold: «سَوَاءً بِسَوَاءٍ», «وَلَوْ قَبْلَ الْأَرْبَعِينَ اغْتَسَلَتْ وَصَلَّتْ وَصَامَتْ»), fortymark (the
 * forty days with a soft edge at the end, and the days past it greyed: «لَا حَدَّ لِأَقَلِّهِ وَلَا لِأَكْثَرِهِ», «وَالْأَرْبَعُونَ مُنْتَهَى الْغَالِبِ»),
 * washatforty (at the fortieth day a drop of water, and the days after it lit: «اغْتَسَلَتْ عِنْدَ تَمَامِ الْأَرْبَعِينَ»).
 */
internal fun DrawScope.afterbirthScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "cradlebegins" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "pureearly" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "fortymark" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "washatforty" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { afterBirthDraw(a) }
}

private fun DrawScope.afterBirthDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    dimaBack(t)
    val cb = fqIn(a, "cradlebegins", "pureearly")
    val pe = fqIn(a, "pureearly", "fortymark")
    val fm = fqIn(a, "fortymark", "washatforty")
    val wf = fqIn(a, "washatforty")
    val cell = w * 0.034f
    // forty days and four more in two rows of twenty-two, right to left
    val rows = listOf(Offset(w * 0.5f + cell * 1.12f * 11f, h * 0.5f), Offset(w * 0.5f + cell * 1.12f * 11f, h * 0.5f + cell * 1.5f))
    val grid = { alpha: Float, fill: (Int) -> Color? ->
        for (r in 0 until 2) dayRow78(rows[r], 22, cell, alpha) { i -> fill(r * 22 + i) }
    }
    val at = { d: Int -> dayAt78(rows[d / 22], d % 22, cell) }
    if (cb > 0f) {
        val p = a.b("cradlebegins")
        cradle78(Offset(w * 0.5f, h * 0.36f), w * 0.2f, cb * seg(p, 0f, 0.2f))
        val n = (seg(p, 0.3f, 0.9f) * 40f).toInt()
        grid(cb) { d -> if (d < 40 && d < n) DimaC.Rose.copy(alpha = 0.6f) else if (d < 40) DimaC.Cell else null }
        tagAt(a.tag("cradlebegins"), Offset(w * 0.5f, h * 0.84f), cb * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (pe > 0f) {
        val p = a.b("pureearly")
        val pureDay = 24
        grid(pe) { d -> if (d < pureDay) DimaC.Rose.copy(alpha = 0.6f) else if (d < 40 && seg(p, 0.5f, 0.8f) * 16f > d - pureDay) DimaC.Gold.copy(alpha = 0.8f) else if (d < 40) DimaC.Cell else null }
        pureSign78(at(pureDay) + Offset(0f, -cell * 1.4f), cell * 0.3f, pe * seg(p, 0.3f, 0.45f))
        tagAt(a.tag("pureearly", 0, 0), Offset(w * 0.5f, h * 0.24f), pe * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("pureearly", 0, 1), Offset(w * 0.5f, h * 0.84f), pe * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fm > 0f) {
        val p = a.b("fortymark")
        grid(fm) { d -> if (d < 40) DimaC.Rose.copy(alpha = 0.6f) else DimaC.Grey }
        val edge = at(39) + Offset(-cell * 0.62f, 0f)
        drawLine(DimaC.Gold, edge + Offset(0f, -cell * 1.2f), edge + Offset(0f, cell * 2.6f), 3f * density, StrokeCap.Round, alpha = fm * seg(p, 0.4f, 0.6f))
        tagAt(a.tag("fortymark", 0, 0), Offset(w * 0.5f, h * 0.24f), fm * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("fortymark", 0, 1), Offset(w * 0.5f, h * 0.84f), fm * seg(p, 0.45f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (wf > 0f) {
        val p = a.b("washatforty")
        grid(wf) { d -> if (d < 40) DimaC.Rose.copy(alpha = 0.6f) else if (seg(p, 0.45f, 0.7f) > 0.5f) DimaC.Gold.copy(alpha = 0.8f) else DimaC.Cell }
        fqDrop(at(39) + Offset(0f, -cell * 1.6f), cell * 0.35f, Fq.Water, wf * seg(p, 0.15f, 0.35f))
        tagAt(a.tag("washatforty"), Offset(w * 0.5f, h * 0.84f), wf * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
