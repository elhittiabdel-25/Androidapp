package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Round 18 of the redraw (2026-10-27): «مَنْهَجُ أَهْلِ السُّنَّةِ». Four new scenes: a believer who will not take the
 * cord of alliance from one who opposes Allah and His Messenger, even his own kin, and whose heart is tied by gold
 * threads to believers from far lands; the companions as named roundels in their order, the ten, and all the rest,
 * with a veil over what passed between them; a square where good is bidden and harm held back, the rows kept, the
 * Hajj, jihad, Fridays and Eids with those in charge, and people bound like one wall; and his ﷺ household in rings
 * around his named light. Companions are never drawn as figures, only as their names.
 */

private fun DrawScope.dash30(on: Float = 5f, off: Float = 4f): PathEffect = PathEffect.dashPathEffect(floatArrayOf(on * density, off * density))

// ═════════════ 1. the cord of alliance refused; the gold threads of love for Allah's sake (wala-bara) ═════════════

private object AllyC30 {
    val Sky = Color(0xFFC6DCEC)
    val SkyLow = Color(0xFFF2E8D2)
    val Ground = Color(0xFFD8C49C)
    val GroundLow = Color(0xFFC2AA7E)
    val Ink = Color(0xFF2A2430)
    val Foe = Color(0xFF5A3A3A)
    val Cord = Color(0xFF8A6A4A)
    val Gold = Color(0xFFE2B84A)
    val Kin = Color(0xFF8A8094)
    val Wall = Color(0xFFB8A27A)
    val Back = Color(0xE6201810)
    val Others = listOf(Color(0xFF3A2A20), Color(0xFF6A4A3A), Color(0xFF2A3A4A))
}

/**
 * A plain by day. Beats: believer (a man stands, a gold light at his heart), nobond (from the other side a man comes
 * holding out a cord to bind them; it falls short and drops; over them ﴿لَا تَجِدُ قَوْمًا يُؤْمِنُونَ بِاللَّهِ …﴾),
 * evenkin (a dotted line of kinship joins the two: it is his own; the cord still lies on the ground), loveandhate (gold
 * threads go from his heart to believers from far lands; a low wall stands between him and the one who opposes).
 */
internal fun DrawScope.allegianceScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "believer" to Shot(Offset(w * 0.5f, h * 0.68f), 1.25f),
        "nobond" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "evenkin" to Shot(Offset(w * 0.35f, h * 0.68f), 1.25f),
        "loveandhate" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { allegiancePlain(a) }
}

private fun DrawScope.allegiancePlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val be = a.b("believer")
    val nb = a.b("nobond")
    val ek = a.b("evenkin")
    val lh = a.b("loveandhate")
    val ground = h * 0.7f
    drawRect(Brush.verticalGradient(listOf(AllyC30.Sky, AllyC30.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(Brush.verticalGradient(listOf(AllyC30.Ground, AllyC30.GroundLow), startY = ground, endY = h), Offset(0f, ground), Size(w, h - ground))
    val tall = h * 0.26f
    val me = Offset(w * 0.5f, h * 0.88f)
    val heart = me + Offset(tall * 0.02f, -tall * 0.64f)
    val o = easeOut(seg(be, 0f, 0.3f))
    // the low wall between him and the one who opposes
    val wall = easeOut(seg(lh, 0.45f, 0.8f))
    if (wall > 0f) {
        for (k in 0 until 4) drawRect(AllyC30.Wall, Offset(w * 0.355f, h * 0.9f - (k + 1) * h * 0.035f * wall), Size(w * 0.035f, h * 0.033f * wall))
    }
    // the man
    figure(me, tall, AllyC30.Ink, o)
    softGlow(heart, w * 0.04f, AllyC30.Gold, 0.8f * easeOut(seg(be, 0.3f, 0.7f)))
    drawCircle(Color(0xFFFFF0C0), w * 0.006f, heart, alpha = easeOut(seg(be, 0.3f, 0.7f)))
    // the one who opposes, holding out a cord
    val come = easeInOut(seg(nb, 0f, 0.4f))
    if (nb > 0f) {
        val foe = Offset(mix(-w * 0.05f, w * 0.22f, come), h * 0.88f)
        withTransform({ scale(-1f, 1f, pivot = foe) }) {
            figure(foe, tall * 0.98f, AllyC30.Foe, 1f, walk = if (come in 0.01f..0.99f) t * 5f else 0f, arms = 0.8f)
        }
        val hand = foe + Offset(tall * 0.27f, -tall * 0.55f)
        val drop = easeIn30(seg(nb, 0.5f, 0.75f))
        val reach = easeOut(seg(nb, 0.3f, 0.5f))
        val end = hand + Offset(w * 0.16f * reach, h * 0.02f) + Offset(0f, (h * 0.88f - hand.y) * drop)
        val cord = Path().apply {
            moveTo(hand.x, hand.y)
            quadraticTo((hand.x + end.x) / 2f, max(hand.y, end.y) + h * 0.05f, end.x, end.y)
        }
        drawPath(cord, AllyC30.Cord, style = Stroke(2.2f * dp, cap = StrokeCap.Round))
        if (drop > 0.95f) drawOval(AllyC30.Cord, Offset(end.x - w * 0.02f, end.y - h * 0.008f), Size(w * 0.04f, h * 0.016f), alpha = 0.6f)
        val verse = seg(nb, 0.35f, 0.6f) * (1f - easeOut(seg(lh, 0f, 0.3f)))
        fitBig26(a.big("nobond", 0, 0), Offset(w * 0.5f, h * 0.14f), w * 0.86f, Color(0xFF4A2E1A), verse)
        fitBig26(a.big("nobond", 0, 1), Offset(w * 0.5f, h * 0.26f), w * 0.7f, Color(0xFF4A2E1A), verse)
        // the line of kinship: he is his own
        val kin = easeOut(seg(ek, 0.1f, 0.6f))
        if (kin > 0f) {
            val h1 = foe + Offset(0f, -tall * 0.95f)
            val h2 = me + Offset(0f, -tall * 0.95f)
            val arc = Path().apply { moveTo(h1.x, h1.y); quadraticTo((h1.x + h2.x) / 2f, h1.y - h * 0.16f * kin, h1.x + (h2.x - h1.x) * kin, h2.y) }
            drawPath(arc, AllyC30.Kin, style = Stroke(1.8f * dp, pathEffect = dash30(3f, 3f)))
            val mid = Offset((h1.x + h2.x) / 2f, h1.y - h * 0.08f)
            drawPath(w8Heart(mid, w * 0.014f), AllyC30.Kin, alpha = seg(ek, 0.5f, 0.8f))
        }
    }
    // gold threads to believers from far lands
    if (lh > 0f) {
        for (k in 0 until 3) {
            val f = easeOut(seg(lh, 0.05f + k * 0.12f, 0.35f + k * 0.12f))
            if (f <= 0f) continue
            val foot = Offset(w * (0.68f + 0.11f * k), h * (0.88f - 0.012f * k))
            figure(foot, tall * (0.92f + 0.04f * k), AllyC30.Others[k], f)
            val hk = foot + Offset(tall * 0.02f, -tall * 0.6f)
            softGlow(hk, w * 0.03f, AllyC30.Gold, 0.6f * f)
            val thread = Path().apply { moveTo(heart.x, heart.y); quadraticTo((heart.x + hk.x) / 2f, min(heart.y, hk.y) - h * 0.08f, heart.x + (hk.x - heart.x) * f, heart.y + (hk.y - heart.y) * f) }
            drawPath(thread, AllyC30.Gold, alpha = 0.9f, style = Stroke(1.6f * dp, cap = StrokeCap.Round))
        }
    }
}

private fun easeIn30(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ═════════════ 2. the companions in their order (sahaba) ═════════════

private object CompC30 {
    val Bg = Color(0xFFF2E6CC)
    val BgLow = Color(0xFFE0CCA4)
    val Star = Color(0xFFC8A45A)
    val Veil = Color(0xFF6A7A9A)
    val Gold = Color(0xFFE2B84A)
    val Back = Color(0xE6201810)
}

private val four30 = listOf(Offset(0.84f, 0.2f), Offset(0.66f, 0.28f), Offset(0.48f, 0.36f), Offset(0.3f, 0.44f))
private val fourR30 = listOf(0.07f, 0.064f, 0.059f, 0.055f)

/**
 * A wall of geometric ornament. Beats: fourfirst (four roundels in order, from the largest, each a name in calligraphy:
 * «أَبُو بَكْرٍ», «عُمَرُ», «عُثْمَانُ», «عَلِيٌّ»), theten (a row of six more names, the rest of the ten), allcompanions (rows
 * of plain roundels fill the foot of the wall), holdtongue (a soft veil comes down between the rows), tworewards (two
 * gold medals and one: «2», «1»), bestgens (three bands of light open out from the companions, each a little fainter).
 */
internal fun DrawScope.companionsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fourfirst" to Shot(Offset(w * 0.6f, h * 0.32f), 1.15f),
        "theten" to Shot(Offset(w * 0.5f, h * 0.62f), 1.15f),
        "allcompanions" to Shot(Offset(w * 0.5f, h * 0.78f), 1.15f),
        "holdtongue" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "tworewards" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "bestgens" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { companionsWall(a) }
}

private fun DrawScope.companionsWall(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ff = a.b("fourfirst")
    val tn = a.b("theten")
    val ac = a.b("allcompanions")
    val ht = a.b("holdtongue")
    val tr = a.b("tworewards")
    val bg = a.b("bestgens")
    drawRect(Brush.radialGradient(listOf(CompC30.Bg, CompC30.BgLow), Offset(w * 0.5f, h * 0.4f), w * 0.8f))
    // faint eight-pointed stars over the wall
    for (r in 0 until 5) for (k in 0 until 8) {
        val c = Offset(w * (0.06f + 0.125f * k + (if (r % 2 == 1) 0.0625f else 0f)), h * (0.1f + 0.2f * r))
        for (m in 0 until 2) withTransform({ rotate(45f * m, c) }) {
            drawRect(CompC30.Star, Offset(c.x - w * 0.018f, c.y - w * 0.018f), Size(w * 0.036f, w * 0.036f), alpha = 0.18f, style = Stroke(1f * dp))
        }
    }
    // the generations: three bands of light opening from the companions
    if (bg > 0f) {
        for (k in 0 until 3) {
            val f = easeOut(seg(bg, 0.1f + k * 0.25f, 0.45f + k * 0.25f))
            val rr = w * (0.25f + 0.2f * k) * f
            drawCircle(CompC30.Gold, rr, Offset(w * 0.6f, h * 0.3f), alpha = (0.5f - 0.13f * k) * f, style = Stroke(w * 0.04f * (1f - 0.25f * k)))
        }
    }
    // the four, in order
    for (k in 0 until 4) {
        val f = easeOut(seg(ff, 0.08f + k * 0.18f, 0.3f + k * 0.18f))
        val c = Offset(w * four30[k].x, h * four30[k].y)
        if (k > 0 && f > 0f) drawLine(CompC30.Star, Offset(w * four30[k - 1].x, h * four30[k - 1].y), c, 1.2f * dp, alpha = 0.6f * f, pathEffect = dash30(3f, 4f))
        if (f > 0.9f) softGlow(c, w * 0.08f, CompC30.Gold, 0.25f)
        nameRoundel(c, w * fourR30[k], a.big("fourfirst", 0, k), Roundel.Fills[k], Roundel.Rim, f)
    }
    // the rest of the ten
    for (k in 0 until 6) {
        val f = easeOut(seg(tn, k * 0.1f, k * 0.1f + 0.35f))
        val c = Offset(w * (0.88f - 0.152f * k), h * 0.63f)
        nameRoundel(c, w * 0.045f, a.big("theten", 0, k), lerpColor(Roundel.Fills[k % 4], Color(0xFF3A3F4B), 0.35f), Roundel.Rim, f)
    }
    // all the companions: plain roundels, no names
    for (r in 0 until 3) for (k in 0 until 16) {
        val f = easeOut(seg(ac, (r * 16 + k) / 60f, (r * 16 + k) / 60f + 0.25f))
        if (f <= 0f) continue
        val c = Offset(w * (0.04f + 0.06f * k + (if (r % 2 == 1) 0.03f else 0f)), h * (0.79f + 0.065f * r))
        drawCircle(Roundel.PlainFill, w * 0.017f * f, c)
        drawCircle(Roundel.PlainRim, w * 0.017f * f, c, alpha = 0.7f, style = Stroke(1f * dp))
    }
    // the veil over what passed between them
    val veil = easeInOut(seg(ht, 0.05f, 0.8f))
    if (veil > 0f) {
        val bottom = h * 0.74f
        val top = bottom - h * 0.2f * veil
        drawRect(Brush.verticalGradient(listOf(CompC30.Veil.copy(alpha = 0f), CompC30.Veil.copy(alpha = 0.55f), CompC30.Veil.copy(alpha = 0.55f), CompC30.Veil.copy(alpha = 0f)), startY = top - h * 0.03f, endY = bottom + h * 0.25f * veil), Offset(0f, top - h * 0.03f), Size(w, bottom - top + h * 0.03f + h * 0.25f * veil))
        for (k in 0 until 10) {
            val x = w * (0.05f + 0.1f * k) + sin(t * 0.6f + k) * w * 0.004f
            drawLine(Color.White, Offset(x, top), Offset(x, bottom + h * 0.2f * veil), 1f * dp, alpha = 0.12f * veil)
        }
    }
    // two rewards and one
    if (tr > 0f) {
        val medals = listOf(Offset(w * 0.66f, h * 0.84f) to 2, Offset(w * 0.34f, h * 0.84f) to 1)
        for ((k, pair) in medals.withIndex()) {
            val (c, n) = pair
            val f = easeOut(seg(tr, 0.1f + k * 0.3f, 0.4f + k * 0.3f))
            if (f <= 0f) continue
            for (m in 0 until n) {
                val mc = c + Offset((m - (n - 1) / 2f) * w * 0.06f, 0f)
                softGlow(mc, w * 0.05f, CompC30.Gold, 0.5f * f)
                drawCircle(CompC30.Gold, w * 0.026f * f, mc)
                drawCircle(Color(0xFFF6E2A0), w * 0.019f * f, mc, style = Stroke(1.2f * dp))
            }
            tagAt(a.tag("tworewards", 0, k), c + Offset(0f, -h * 0.09f), seg(f, 0.5f, 1f), back = CompC30.Back, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 3. the square: good bidden, harm held back, the rows kept, one wall (akhlaq-sunna) ═════════════

private object SqC30 {
    val Sky = Color(0xFFC8DCEA)
    val SkyLow = Color(0xFFF2E6CC)
    val Paving = Color(0xFFE2D2B0)
    val PavingLine = Color(0xFFCDB890)
    val Wall = Color(0xFFEDE3CC)
    val Dome = Color(0xFF4A8A6A)
    val Ink = Color(0xFF2A2430)
    val Brick = Color(0xFFD8B07A)
    val Gold = Color(0xFFE2B84A)
    val Back = Color(0xE6201810)
}

/**
 * A town square by day, a mosque on one side. Beats: goodword (one man lifts a stick over another; a third steps in and
 * holds his arm: the stick drops, and he points them both toward the mosque), rows (before the mosque the rows form
 * behind their imam), withleaders (above the rows, four signs come up: the Kaaba, a banner, a pulpit, the new moon of
 * the feast: «الْحَجُّ», «الْجِهَادُ», «الْجُمَعُ», «الْأَعْيَادُ»; a leader with a banner at the front), onewall (behind the
 * people a wall of bricks rises, course by course, as they stand shoulder to shoulder).
 */
internal fun DrawScope.jamaaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "bidgood" to Shot(Offset(w * 0.26f, h * 0.68f), 1.3f),
        "keeprows" to Shot(Offset(w * 0.66f, h * 0.68f), 1.2f),
        "withleaders" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "onewall" to Shot(Offset(w * 0.62f, h * 0.66f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { jamaaSquare(a) }
}

private fun DrawScope.jamaaSquare(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val gw = a.b("bidgood")
    val rw = a.b("keeprows")
    val wl = a.b("withleaders")
    val ow = a.b("onewall")
    val ground = h * 0.6f
    drawRect(Brush.verticalGradient(listOf(SqC30.Sky, SqC30.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(SqC30.Paving, Offset(0f, ground), Size(w, h - ground))
    for (k in 0 until 6) drawLine(SqC30.PavingLine, Offset(0f, ground + h * 0.07f * k), Offset(w, ground + h * 0.07f * k), 1f * dp, alpha = 0.6f)
    // the mosque, right
    val mx = w * 0.82f
    drawRect(SqC30.Wall, Offset(mx - w * 0.17f, ground - h * 0.2f), Size(w * 0.34f, h * 0.2f))
    drawArc(SqC30.Dome, 180f, 180f, true, Offset(mx - w * 0.08f, ground - h * 0.32f), Size(w * 0.16f, h * 0.24f))
    drawRect(SqC30.Wall, Offset(mx + w * 0.12f, ground - h * 0.42f), Size(w * 0.03f, h * 0.42f))
    drawArc(SqC30.Dome, 180f, 180f, true, Offset(mx + w * 0.12f, ground - h * 0.44f), Size(w * 0.03f, h * 0.04f))
    drawPath(archPath26(mx, ground - h * 0.12f, ground, w * 0.035f), Color(0xFF2A2430))
    val tall = h * 0.2f
    // 1. good bidden, harm held back
    val gA = 1f - easeOut(seg(rw, 0.2f, 0.6f))
    if (gw > 0f && gA > 0f) {
        val o = easeOut(seg(gw, 0f, 0.2f)) * gA
        val aggressor = Offset(w * 0.18f, h * 0.9f)
        val victim = Offset(w * 0.08f, h * 0.9f)
        val helper = Offset(w * mix(0.42f, 0.27f, easeInOut(seg(gw, 0.15f, 0.4f))), h * 0.9f)
        figure(victim, tall * 0.95f, SqC30.Ink, o, lean = 0.2f * (1f - seg(gw, 0.5f, 0.7f)))
        figure(aggressor, tall, SqC30.Ink, o)
        val drop = easeOut(seg(gw, 0.45f, 0.65f))
        val grip = aggressor + Offset(tall * 0.12f, -tall * (0.95f - 0.4f * drop))
        drawLine(Color(0xFF6A4A2A), grip, grip + Offset(-w * 0.03f * (1f - drop), -h * 0.06f * (1f - drop) + h * 0.12f * drop), 2.5f * dp, StrokeCap.Round, alpha = o)
        drawLine(SqC30.Ink, aggressor + Offset(0f, -tall * 0.76f), grip, tall * 0.065f, StrokeCap.Round, alpha = o)
        withTransform({ scale(-1f, 1f, pivot = helper) }) {
            figure(helper, tall, Color(0xFF3A4A3A), o, walk = if (seg(gw, 0.15f, 0.4f) in 0.01f..0.99f) t * 5f else 0f, arms = easeOut(seg(gw, 0.35f, 0.5f)))
        }
        // then he points them toward the mosque
        val point = easeOut(seg(gw, 0.7f, 0.85f))
        if (point > 0f) arrowHead(helper + Offset(w * 0.12f * point, -tall * 0.6f), Offset(1f, 0f), w * 0.02f, SqC30.Gold, 2f * dp, point * o)
    }
    // 2. the rows
    val rowsA = easeOut(seg(rw, 0.1f, 0.6f)).coerceAtLeast(if (wl > 0f || ow > 0f) 1f else 0f)
    val rowY = listOf(h * 0.8f, h * 0.92f)
    val xs = List(7) { k -> w * (0.66f - 0.07f * k) }
    if (rowsA > 0f) {
        // the imam / the leader in front
        figure(Offset(w * 0.74f, h * 0.76f), tall, SqC30.Ink, rowsA)
        for ((r, y) in rowY.withIndex()) for ((k, x) in xs.withIndex()) {
            val f = easeOut(seg(rw, 0.1f + (r * 7 + k) * 0.035f, 0.3f + (r * 7 + k) * 0.035f)).coerceAtLeast(if (wl > 0f || ow > 0f) 1f else 0f)
            figure(Offset(x - r * w * 0.02f, y), tall * 0.92f, SqC30.Ink, f)
        }
    }
    // 3. with those in charge: the four signs
    if (wl > 0f) {
        val leader = Offset(w * 0.74f, h * 0.76f)
        val ban = easeOut(seg(wl, 0.6f, 0.8f))
        drawLine(Color(0xFF6A4A2A), leader + Offset(w * 0.02f, -tall * 0.5f), leader + Offset(w * 0.02f, -tall * 1.5f * ban), 2f * dp, alpha = ban)
        drawRect(Color(0xFF2E6A4A), Offset(leader.x + w * 0.02f, leader.y - tall * 1.5f * ban), Size(w * 0.05f * ban, h * 0.04f), alpha = ban)
        val signs = List(4) { k -> Offset(w * (0.8f - 0.2f * k), h * 0.22f) }
        for ((k, c) in signs.withIndex()) {
            val f = easeOut(seg(wl, 0.05f + k * 0.12f, 0.25f + k * 0.12f))
            if (f <= 0f) continue
            drawCircle(Color(0xFFFBF4E0), w * 0.055f * f, c, alpha = 0.9f)
            drawCircle(SqC30.Gold, w * 0.055f * f, c, style = Stroke(1.6f * dp))
            when (k) {
                0 -> {
                    drawRect(Color(0xFF14121A), Offset(c.x - w * 0.022f, c.y - h * 0.03f), Size(w * 0.044f, h * 0.05f), alpha = f)
                    drawRect(SqC30.Gold, Offset(c.x - w * 0.022f, c.y - h * 0.02f), Size(w * 0.044f, h * 0.006f), alpha = f)
                }
                1 -> {
                    drawLine(Color(0xFF6A4A2A), c + Offset(-w * 0.012f, h * 0.035f), c + Offset(-w * 0.012f, -h * 0.035f), 2f * dp, alpha = f)
                    drawRect(Color(0xFF2E6A4A), Offset(c.x - w * 0.012f, c.y - h * 0.035f), Size(w * 0.032f, h * 0.022f), alpha = f)
                }
                2 -> {
                    for (s in 0 until 3) drawRect(Color(0xFF8A6A48), Offset(c.x - w * 0.02f + s * w * 0.013f, c.y + h * 0.025f - (s + 1) * h * 0.015f), Size(w * 0.013f, (s + 1) * h * 0.015f), alpha = f)
                    drawArc(Color(0xFF8A6A48), 180f, 180f, true, Offset(c.x + w * 0.006f, c.y - h * 0.04f), Size(w * 0.02f, h * 0.025f), alpha = f)
                }
                else -> {
                    drawCircle(Color(0xFFE2B84A), w * 0.022f, c + Offset(0f, -h * 0.005f), alpha = f)
                    drawCircle(Color(0xFFFBF4E0), w * 0.019f, c + Offset(w * 0.009f, -h * 0.01f), alpha = f)
                }
            }
            tagAt(a.tag("withleaders", 0, k), c + Offset(0f, h * 0.1f), seg(f, 0.5f, 1f) * (1f - seg(ow, 0f, 0.3f)), back = SqC30.Back, ink = ArtInk.Ivory)
        }
    }
    // 4. one wall: bricks rise behind the people, course by course
    if (ow > 0f) {
        val courses = 6
        for (c in 0 until courses) {
            val f = easeOut(seg(ow, 0.1f + c * 0.1f, 0.3f + c * 0.1f))
            if (f <= 0f) continue
            val y = h * 0.78f - (c + 1) * h * 0.04f
            var x = w * 0.2f - (if (c % 2 == 1) w * 0.035f else 0f)
            while (x < w * 0.72f) {
                drawRect(SqC30.Brick, Offset(max(x, w * 0.2f), y), Size(min(w * 0.07f, w * 0.72f - max(x, w * 0.2f)), h * 0.04f), alpha = 0.85f * f)
                drawRect(Color(0xFFF0E2C6), Offset(max(x, w * 0.2f), y), Size(min(w * 0.07f, w * 0.72f - max(x, w * 0.2f)), h * 0.04f), alpha = f, style = Stroke(1f * dp))
                x += w * 0.07f
            }
        }
        // shoulder to shoulder: a gold line joining the rows
        val join = easeOut(seg(ow, 0.6f, 0.9f))
        for (y in rowY) drawLine(SqC30.Gold, Offset(w * 0.24f, y - tall * 0.76f), Offset(w * 0.24f + (w * 0.44f) * join, y - tall * 0.76f), 2f * dp, StrokeCap.Round, alpha = 0.8f)
        if (join > 0.9f) softGlow(Offset(w * 0.46f, h * 0.65f), w * 0.3f, SqC30.Gold, 0.15f)
    }
}

// ═════════════ 4. his ﷺ household in rings round his named light (ahl-bayt) ═════════════

private object HouseC30 {
    val Bg = Color(0xFF0E2A2A)
    val BgLow = Color(0xFF071818)
    val Pattern = Color(0xFF2E5A50)
    val Ring = Color(0xFFE2C46A)
    val Mothers = Color(0xFF3A2A4A)
    val Out = Color(0xFF3A3A3A)
    val Red = Color(0xFFC0463A)
    val Back = Color(0xE60E1018)
}

private fun houseRing30(w: Float, h: Float, k: Int): Offset {
    val ang = (225f + 90f * k) * PI.toFloat() / 180f
    return Offset(w * 0.5f + cos(ang) * w * 0.17f, h * 0.42f + sin(ang) * h * 0.2f)
}

private fun motherRing30(w: Float, h: Float, k: Int): Offset {
    val ang = (60f * k) * PI.toFloat() / 180f
    return Offset(w * 0.5f + cos(ang) * w * 0.37f, h * 0.42f + sin(ang) * h * 0.3f)
}

/**
 * A dark green field of ornament. Beats: household (his ﷺ light in the middle, named; around it four roundels:
 * «عَلِيٌّ», «فَاطِمَةُ», «الْحَسَنُ», «الْحُسَيْنُ»; above, «أُذَكِّرُكُمُ اللَّهَ فِي أَهْلِ بَيْتِي»), mothers (a wider ring of six
 * roundels, the Mothers of the Believers), notlahab (far outside the rings, a grey roundel, «أَبُو لَهَبٍ»: the ring's
 * dashed edge passes it by), noexcess (a roundel is lifted above his light: it is struck down, «لَا غُلُوَّ»), nosaving
 * (Fatima's roundel, and his words to her: «لَا أُغْنِي عَنْكِ مِنَ اللَّهِ شَيْئًا»).
 */
internal fun DrawScope.householdScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "household" to Shot(Offset(w * 0.5f, h * 0.4f), 1.2f),
        "mothers" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "notlahab" to Shot(Offset(w * 0.78f, h * 0.76f), 1.3f),
        "noexcess" to Shot(Offset(w * 0.5f, h * 0.32f), 1.2f),
        "nosaving" to Shot(Offset(w * 0.5f, h * 0.45f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { householdField(a) }
}

private fun DrawScope.householdField(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ho = a.b("household")
    val mo = a.b("mothers")
    val nl = a.b("notlahab")
    val ne = a.b("noexcess")
    val ns = a.b("nosaving")
    drawRect(Brush.radialGradient(listOf(HouseC30.Bg, HouseC30.BgLow), Offset(w * 0.5f, h * 0.42f), w * 0.8f))
    for (r in 0 until 5) for (k in 0 until 8) {
        val c = Offset(w * (0.06f + 0.125f * k + (if (r % 2 == 1) 0.0625f else 0f)), h * (0.1f + 0.2f * r))
        for (m in 0 until 2) withTransform({ rotate(45f * m, c) }) {
            drawRect(HouseC30.Pattern, Offset(c.x - w * 0.02f, c.y - w * 0.02f), Size(w * 0.04f, w * 0.04f), alpha = 0.35f, style = Stroke(1f * dp))
        }
    }
    val center = Offset(w * 0.5f, h * 0.42f)
    // the household's edge: a dashed ring that leaves him outside
    val edge = easeOut(seg(nl, 0.2f, 0.6f))
    if (edge > 0f) drawOval(HouseC30.Ring, Offset(center.x - w * 0.45f, center.y - h * 0.38f), Size(w * 0.9f, h * 0.76f), alpha = 0.5f * edge, style = Stroke(1.4f * dp, pathEffect = dash30()))
    // his light, named
    val light = easeOut(seg(ho, 0f, 0.3f))
    sirajMunir(center, w * 0.014f, light, t, nameAt = center + Offset(0f, h * 0.09f))
    // the four of his house
    val four = easeOut(seg(ho, 0.25f, 0.6f))
    for (k in 0 until 4) {
        val f = easeOut(seg(ho, 0.25f + k * 0.08f, 0.45f + k * 0.08f))
        val c = houseRing30(w, h, k)
        if (f > 0f) drawLine(HouseC30.Ring, center, center + (c - center) * f, 1.2f * dp, alpha = 0.5f)
        val lift = if (k == 0) easeInOut(seg(ne, 0.1f, 0.45f)) * (1f - easeInOut(seg(ne, 0.6f, 0.85f))) else 0f
        val shine = if (k == 1) seg(ns, 0.1f, 0.4f) else 0f
        if (shine > 0f) softGlow(c, w * 0.08f, Color(0xFFFFE6B0), 0.5f * shine)
        nameRoundel(c + Offset(0f, -h * 0.3f * lift), w * 0.055f, a.big("household", 0, k), Roundel.Fills[k], Roundel.Rim, f)
    }
    tagAt(a.tag("household", 0, 4), Offset(w * 0.5f, h * 0.06f), seg(ho, 0.55f, 0.8f) * (1f - seg(ne, 0f, 0.2f)), back = HouseC30.Back, ink = ArtInk.BrassLight)
    // the Mothers of the Believers
    for (k in 0 until 6) {
        val f = easeOut(seg(mo, 0.05f + k * 0.1f, 0.3f + k * 0.1f))
        nameRoundel(motherRing30(w, h, k), w * 0.048f, a.big("mothers", 0, k), HouseC30.Mothers, Roundel.Rim, f)
    }
    // one of his kin who has no share in this
    val lah = easeOut(seg(nl, 0f, 0.3f))
    if (lah > 0f) {
        val c = Offset(w * 0.9f, h * 0.9f)
        nameRoundel(c, w * 0.045f, a.big("notlahab"), HouseC30.Out, Color(0xFF8A8A8A), lah, ink = Color(0xFFC8C8C8))
    }
    // excess: lifting one above him is struck down
    if (ne > 0f) {
        val c = houseRing30(w, h, 0) + Offset(0f, -h * 0.3f * easeInOut(seg(ne, 0.1f, 0.45f)))
        val o = seg(ne, 0.1f, 0.3f) * (1f - seg(ne, 0.85f, 1f))
        drawLine(Color(0xFFE2C46A), houseRing30(w, h, 0), c, 1.4f * dp, alpha = 0.6f * o, pathEffect = dash30(3f, 3f))
        cross21(c, w * 0.06f, HouseC30.Red, seg(ne, 0.4f, 0.6f), 3.5f * dp)
        tagAt(a.tag("noexcess"), Offset(w * 0.2f, h * 0.12f), seg(ne, 0.45f, 0.65f), back = HouseC30.Back, ink = Color(0xFFFFB0A0))
    }
    // his words to Fatima
    if (ns > 0f) {
        tagAt(a.tag("nosaving"), houseRing30(w, h, 1) + Offset(0f, -h * 0.13f), seg(ns, 0.2f, 0.45f), back = HouseC30.Back, ink = ArtInk.BrassLight)
    }
}
