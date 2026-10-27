package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Round 15 of the redraw (2026-10-27): «الشرك ووسائله». Seven new scenes: the four rules as four framed panels under
 * their book; the lote tree hung with weapons and the travellers who asked for one like it; the offering that goes
 * one way or the other, and the vow kept or broken; the storm at sea, the call to Allah alone, the call to a tomb,
 * and the living man who throws a rope; the gate with two seals; the plain grave that is kept plain; and the
 * footprints followed print for print. Allah is never drawn; his ﷺ light always carries his name.
 */

private val Dash27 = floatArrayOf(5f, 4f)

private fun DrawScope.dash27(): PathEffect = PathEffect.dashPathEffect(floatArrayOf(Dash27[0] * density, Dash27[1] * density))

/** A stone idol on a low base. */
private fun DrawScope.idol27(foot: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(color, Offset(foot.x - s * 0.5f, foot.y - s * 0.2f), Size(s, s * 0.2f), alpha = alpha)
    drawRoundRect(color, Offset(foot.x - s * 0.32f, foot.y - s * 0.95f), Size(s * 0.64f, s * 0.78f), CornerRadius(s * 0.25f), alpha = alpha)
    drawCircle(color, s * 0.24f, foot + Offset(0f, -s * 1.12f), alpha = alpha)
}

/** A domed tomb. */
private fun DrawScope.tomb27(foot: Offset, s: Float, body: Color, dome: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(body, Offset(foot.x - s * 0.5f, foot.y - s * 0.55f), Size(s, s * 0.55f), alpha = alpha)
    drawArc(dome, 180f, 180f, true, Offset(foot.x - s * 0.42f, foot.y - s * 0.95f), Size(s * 0.84f, s * 0.8f), alpha = alpha)
    drawArc(Color(0xFF241C18), 180f, 180f, true, Offset(foot.x - s * 0.12f, foot.y - s * 0.32f), Size(s * 0.24f, s * 0.5f), alpha = alpha)
}

/** A tree with a round crown. */
private fun DrawScope.tree27(foot: Offset, s: Float, trunk: Color, leaf: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawLine(trunk, foot, foot + Offset(0f, -s * 0.6f), s * 0.12f, StrokeCap.Round, alpha = alpha)
    for ((dx, dy, r) in listOf(Triple(0f, -0.85f, 0.38f), Triple(-0.28f, -0.72f, 0.28f), Triple(0.28f, -0.72f, 0.28f))) {
        drawCircle(leaf, s * r, foot + Offset(s * dx, s * dy), alpha = alpha)
    }
}

/** Arms raised in supplication: a standing figure facing left. */
private fun DrawScope.dua27(foot: Offset, tall: Float, color: Color, alpha: Float, lift: Float = 1f) {
    val hip = foot - Offset(0f, tall * 0.45f)
    val neck = hip + Offset(0f, -tall * 0.31f)
    val up = lift.coerceIn(0f, 1f)
    val handA = neck + Offset(-tall * (0.12f + 0.04f * up), -tall * (0.05f + 0.2f * up))
    val handB = neck + Offset(tall * (0.02f + 0.06f * up), -tall * (0.05f + 0.2f * up))
    posed14(hip, neck, handA, handB, foot + Offset(-tall * 0.07f, 0f), foot + Offset(tall * 0.07f, 0f), tall, color, alpha)
}

/** A figure pointing up and to the left (asking, showing). */
private fun DrawScope.pointer27(foot: Offset, tall: Float, color: Color, alpha: Float, up: Float) {
    val hip = foot - Offset(0f, tall * 0.45f)
    val neck = hip + Offset(0f, -tall * 0.31f)
    val handA = neck + Offset(-tall * 0.3f, -tall * 0.18f * up + tall * 0.12f * (1f - up))
    val handB = neck + Offset(tall * 0.08f, tall * 0.25f)
    posed14(hip, neck, handA, handB, foot + Offset(-tall * 0.07f, 0f), foot + Offset(tall * 0.07f, 0f), tall, color, alpha)
}

// ═════════════ 1. four rules, four panels (qawaid-arba) ═════════════

private object RulC27 {
    val Desk = Color(0xFFE6D6B4)
    val DeskLow = Color(0xFFCDB88E)
    val Frame = Color(0xFF8A6238)
    val Sky = Color(0xFFD9E6EE)
    val Sand = Color(0xFFDCC69A)
    val Ink = Color(0xFF2A2430)
    val Stone = Color(0xFF7A6E66)
    val Door = Color(0xFF6A4A2E)
    val Storm = Color(0xFF5A6070)
    val Sun = Color(0xFFF2B43A)
    val Red = Color(0xFFC0463A)
    val Back = Color(0xE6201810)
    val Cover = Color(0xFF2E4A3A)
}

private fun panel27(w: Float, h: Float, k: Int): Rect {
    val cx = if (k % 2 == 0) w * 0.735f else w * 0.265f
    val cy = if (k < 2) h * 0.4f else h * 0.77f
    return Rect(cx - w * 0.215f, cy - h * 0.165f, cx + w * 0.215f, cy + h * 0.165f)
}

/**
 * A desk by day. Beats: rulebook (the book «الْقَوَاعِدُ الْأَرْبَعُ» opens; four empty frames, one to four), ruleone (in
 * the first a man before a locked door says ﴿فَسَيَقُولُونَ اللَّهُ﴾: the door stays shut), ruletwo (in the second a man
 * bows to a stone, a dashed line from it upward: ﴿مَا نَعْبُدُهُمْ إِلَّا لِيُقَرِّبُونَا إِلَى اللَّهِ زُلْفَى﴾), rulethree (in
 * the third a tomb, an angel's light, a stone and a tree, «صَالِحًا»، «مَلَكًا»، «حَجَرًا»، «شَجَرًا», joined by red equals
 * signs), rulefour (in the fourth the same man bows to his stone in the sun and in the storm).
 */
internal fun DrawScope.fourrulesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rulebook" to Shot(Offset(w * 0.5f, h * 0.16f), 1.25f),
        "ruleone" to Shot(panel27(w, h, 0).center, 1.15f),
        "ruletwo" to Shot(panel27(w, h, 1).center, 1.15f),
        "rulethree" to Shot(panel27(w, h, 2).center, 1.15f),
        "rulefour" to Shot(panel27(w, h, 3).center, 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { fourrulesDesk(a) }
}

private fun DrawScope.fourrulesDesk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bk = a.b("rulebook")
    val beats = listOf(a.b("ruleone"), a.b("ruletwo"), a.b("rulethree"), a.b("rulefour"))
    drawRect(Brush.verticalGradient(listOf(RulC27.Desk, RulC27.DeskLow)))
    // the book
    val book = Offset(w * 0.5f, h * 0.11f)
    val open = easeOut(seg(bk, 0f, 0.35f))
    if (open < 0.5f) {
        drawRoundRect(RulC27.Cover, Offset(book.x - w * 0.045f, book.y - h * 0.06f), Size(w * 0.09f, h * 0.12f), CornerRadius(w * 0.006f))
        drawRect(ArtInk.Gold, Offset(book.x - w * 0.045f, book.y - h * 0.04f), Size(w * 0.09f, h * 0.008f))
    } else openBook21(book, w * 0.16f, Color(0xFFFBF4E0), Color(0xFFB8A27A), 1f, glow = 0.3f)
    tagAt(a.tag("rulebook"), book + Offset(w * 0.17f, -h * 0.01f), seg(bk, 0.25f, 0.5f), back = RulC27.Back, ink = ArtInk.BrassLight)
    // four frames
    for (k in 0 until 4) {
        val r = panel27(w, h, k)
        val f = easeOut(seg(bk, 0.3f + k * 0.12f, 0.55f + k * 0.12f)).coerceAtLeast(if (beats[k] > 0f) 1f else 0f)
        if (f <= 0f) continue
        val fill = easeOut(seg(beats[k], 0f, 0.25f))
        drawRoundRect(Color(0xFFF6EEDC), r.topLeft, r.size, CornerRadius(w * 0.01f), alpha = f)
        if (fill > 0f) clipRect(r.left, r.top, r.right, r.bottom) {
            when (k) {
                0 -> ruleDoor27(a, r, beats[0], fill, t)
                1 -> ruleCloser27(a, r, beats[1], fill, t)
                2 -> ruleSame27(a, r, beats[2], fill, t)
                else -> ruleAlways27(a, r, beats[3], fill, t)
            }
        }
        drawRoundRect(RulC27.Frame, r.topLeft, r.size, CornerRadius(w * 0.01f), alpha = f, style = Stroke(2.5f * dp))
        // its number, as pips
        for (m in 0..k) drawCircle(RulC27.Frame, w * 0.006f, Offset(r.right - w * 0.018f - m * w * 0.016f, r.top + h * 0.025f), alpha = f)
    }
}

private fun DrawScope.ruleDoor27(a: ArtCtx, r: Rect, p: Float, o: Float, t: Float) {
    val dp = density
    val ground = r.bottom - r.height * 0.15f
    drawRect(RulC27.Sky, r.topLeft, Size(r.width, ground - r.top), alpha = o)
    drawRect(RulC27.Sand, Offset(r.left, ground), Size(r.width, r.bottom - ground), alpha = o)
    // the door of Islam, locked
    val dl = r.left + r.width * 0.12f
    val dw = r.width * 0.26f
    val dt = r.top + r.height * 0.28f
    val push = if (p in 0.55f..0.85f) sin(t * 30f) * 2f * dp else 0f
    drawRect(Color(0xFFC9B48A), Offset(dl - r.width * 0.03f, dt - r.height * 0.04f), Size(dw + r.width * 0.06f, ground - dt + r.height * 0.04f), alpha = o)
    drawRect(RulC27.Door, Offset(dl + push, dt), Size(dw, ground - dt), alpha = o)
    padlock21(Offset(dl + dw * 0.5f + push, dt + (ground - dt) * 0.5f), r.width * 0.07f, Color(0xFF8A7A5A), 0f, o)
    // the man who says «Allah» is the Creator
    val foot = Offset(r.left + r.width * 0.68f, ground)
    figure(foot, r.height * 0.42f, RulC27.Ink, o, arms = 0.6f * easeOut(seg(p, 0.4f, 0.6f)))
    fitBig26(a.big("ruleone"), Offset(r.left + r.width * 0.68f, r.top + r.height * 0.17f), r.width * 0.5f, Color(0xFF6A4418), seg(p, 0.2f, 0.45f))
}

private fun DrawScope.ruleCloser27(a: ArtCtx, r: Rect, p: Float, o: Float, t: Float) {
    val dp = density
    val ground = r.bottom - r.height * 0.12f
    drawRect(RulC27.Sky, r.topLeft, Size(r.width, ground - r.top), alpha = o)
    drawRect(RulC27.Sand, Offset(r.left, ground), Size(r.width, r.bottom - ground), alpha = o)
    val stone = Offset(r.left + r.width * 0.3f, ground)
    idol27(stone, r.height * 0.3f, RulC27.Stone, o)
    w8Worshipper(Offset(r.left + r.width * 0.62f, ground), r.height * 0.4f, RulC27.Ink, 0f, easeOut(seg(p, 0.15f, 0.4f)), o)
    // a dashed line from the stone upward: he takes it as a go-between
    val up = easeOut(seg(p, 0.35f, 0.7f))
    if (up > 0f) {
        val from = stone + Offset(0f, -r.height * 0.42f)
        drawLine(RulC27.Red, from, from + Offset(r.width * 0.05f, -r.height * 0.3f * up), 1.6f * dp, StrokeCap.Round, pathEffect = dash27())
    }
    fitBig26(a.big("ruletwo", 0, 0), Offset(r.center.x, r.top + r.height * 0.13f), r.width * 0.78f, Color(0xFF6A4418), seg(p, 0.4f, 0.65f))
    fitBig26(a.big("ruletwo", 0, 1), Offset(r.center.x, r.top + r.height * 0.27f), r.width * 0.5f, Color(0xFF6A4418), seg(p, 0.5f, 0.75f))
}

private fun DrawScope.ruleSame27(a: ArtCtx, r: Rect, p: Float, o: Float, t: Float) {
    val dp = density
    drawRect(Color(0xFFEFE6D2), r.topLeft, r.size, alpha = o)
    val cells = listOf(
        Offset(r.left + r.width * 0.78f, r.top + r.height * 0.36f), Offset(r.left + r.width * 0.3f, r.top + r.height * 0.36f),
        Offset(r.left + r.width * 0.78f, r.top + r.height * 0.8f), Offset(r.left + r.width * 0.3f, r.top + r.height * 0.8f),
    )
    val s = r.height * 0.28f
    for ((k, c) in cells.withIndex()) {
        val f = easeOut(seg(p, k * 0.12f, k * 0.12f + 0.25f))
        if (f <= 0f) continue
        when (k) {
            0 -> tomb27(c, s, Color(0xFFD8CBB0), Color(0xFF6A8A6A), f)
            1 -> angelLight26(c + Offset(0f, -s * 0.45f), s * 0.14f, t, f)
            2 -> idol27(c, s * 0.8f, RulC27.Stone, f)
            else -> tree27(c, s, Color(0xFF6A4A2E), Color(0xFF4A8A4A), f)
        }
        tagAt(a.tag("rulethree", 0, k), c + Offset(0f, s * 0.3f), seg(f, 0.5f, 1f), back = RulC27.Back, ink = ArtInk.Ivory)
    }
    // one judgment for all four: red equals signs between them
    val eq = easeOut(seg(p, 0.6f, 0.85f))
    if (eq > 0f) {
        for ((m, pair) in listOf(cells[0] to cells[1], cells[2] to cells[3], cells[0] to cells[2], cells[1] to cells[3]).withIndex()) {
            val mid = (pair.first + pair.second) / 2f + Offset(0f, if (m < 2) -s * 0.4f else 0f)
            val vertical = m >= 2
            for (d in listOf(-1f, 1f)) {
                val off = if (vertical) Offset(d * r.width * 0.012f, 0f) else Offset(0f, d * r.height * 0.02f)
                val half = if (vertical) Offset(0f, r.height * 0.04f) else Offset(r.width * 0.035f, 0f)
                drawLine(RulC27.Red, mid + off - half, mid + off + half, 2.5f * dp, StrokeCap.Round, alpha = eq)
            }
        }
    }
}

private fun DrawScope.ruleAlways27(a: ArtCtx, r: Rect, p: Float, o: Float, t: Float) {
    val dp = density
    val mid = r.center.x
    val ground = r.bottom - r.height * 0.14f
    // ease (right) and hardship (left)
    drawRect(Color(0xFFDDEBF4), Offset(mid, r.top), Size(r.right - mid, ground - r.top), alpha = o)
    drawRect(RulC27.Storm, Offset(r.left, r.top), Size(mid - r.left, ground - r.top), alpha = o)
    drawRect(RulC27.Sand, Offset(r.left, ground), Size(r.width, r.bottom - ground), alpha = o)
    softGlow(Offset(r.right - r.width * 0.12f, r.top + r.height * 0.25f), r.width * 0.12f, RulC27.Sun, 0.6f * o)
    drawCircle(RulC27.Sun, r.width * 0.045f, Offset(r.right - r.width * 0.12f, r.top + r.height * 0.25f), alpha = o)
    for (k in 0 until 10) {
        val f = fract(t * 1.5f + k * 0.13f)
        val x = r.left + r.width * (0.04f + 0.045f * k)
        drawLine(Color(0xFFB8C8E0), Offset(x, r.top + f * (ground - r.top)), Offset(x - r.width * 0.01f, r.top + f * (ground - r.top) + r.height * 0.05f), 1f * dp, alpha = 0.7f * o)
    }
    if (fract(t * 0.4f) < 0.05f) drawLine(Color.White, Offset(r.left + r.width * 0.15f, r.top), Offset(r.left + r.width * 0.2f, r.top + r.height * 0.3f), 2f * dp, alpha = o)
    for (side in listOf(1f, -1f)) {
        val x = mid + side * r.width * 0.25f
        idol27(Offset(x - r.width * 0.08f, ground), r.height * 0.22f, RulC27.Stone, o)
        w8Worshipper(Offset(x + r.width * 0.08f, ground), r.height * 0.34f, RulC27.Ink, 0f, easeOut(seg(p, 0.2f, 0.45f)), o)
    }
    tagAt(a.tag("rulefour", 0, 0), Offset(mid + r.width * 0.25f, r.top + r.height * 0.12f), seg(p, 0.3f, 0.5f), back = RulC27.Back, ink = ArtInk.Ivory)
    tagAt(a.tag("rulefour", 0, 1), Offset(mid - r.width * 0.25f, r.top + r.height * 0.12f), seg(p, 0.45f, 0.65f), back = RulC27.Back, ink = ArtInk.Ivory)
}

// ═════════════ 2. the lote tree hung with weapons (tabarruk) ═════════════

private object AnwC27 {
    val Sky = Color(0xFFB8D6EA)
    val SkyLow = Color(0xFFF2E2BC)
    val Sand = Color(0xFFE2C890)
    val SandLow = Color(0xFFC8A870)
    val Road = Color(0xFFD2B47A)
    val Trunk = Color(0xFF5A4030)
    val Leaf = Color(0xFF5A8A4A)
    val LeafDark = Color(0xFF3E6A3A)
    val Blade = Color(0xFFB8C0CC)
    val Cloth = listOf(Color(0xFFC0463A), Color(0xFF3A6AB0), Color(0xFFE8B83A))
    val Ink = Color(0xFF2A2430)
    val Pagan = Color(0xFF4A3A3A)
    val Red = Color(0xFFC0463A)
    val Back = Color(0xE6201810)
}

/**
 * A desert road by day. Beats: lotetree (a great lote tree by the road; men hang swords and strips of cloth on it, and
 * a stone at its foot is wrapped: «ذَاتُ أَنْوَاطٍ»), sunanpath (old footprints lead to it from those before; it is marked
 * with a red sign: «سُنَنُ الْمُشْرِكِينَ»), askanwat (a column of travellers comes along the road; two stop and point:
 * «اجْعَلْ لَنَا ذَاتَ أَنْوَاطٍ»), takbirsunan (his ﷺ light at the head of the column, named; «اللَّهُ أَكْبَرُ، إِنَّهَا السُّنَنُ»;
 * the two lower their hands and walk on).
 */
internal fun DrawScope.anwatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "lotetree" to Shot(Offset(w * 0.38f, h * 0.5f), 1.3f),
        "sunanpath" to Shot(Offset(w * 0.3f, h * 0.6f), 1.15f),
        "askanwat" to Shot(Offset(w * 0.66f, h * 0.6f), 1.3f),
        "takbirsunan" to Shot(Offset(w * 0.6f, h * 0.5f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { anwatRoad(a) }
}

private fun DrawScope.anwatRoad(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val lt = a.b("lotetree")
    val sp = a.b("sunanpath")
    val ak = a.b("askanwat")
    val tk = a.b("takbirsunan")
    val horizon = h * 0.55f
    drawRect(Brush.verticalGradient(listOf(AnwC27.Sky, AnwC27.SkyLow), endY = horizon), size = Size(w, horizon))
    farHills(Color(0xFFC8A880), AnwC27.SkyLow, horizon, h * 0.03f, 0.5f)
    drawRect(Brush.verticalGradient(listOf(AnwC27.Sand, AnwC27.SandLow), startY = horizon, endY = h), Offset(0f, horizon), Size(w, h - horizon))
    drawPath(Path().apply { moveTo(w, h * 0.78f); quadraticTo(w * 0.6f, h * 0.72f, 0f, h * 0.8f); lineTo(0f, h * 0.86f); quadraticTo(w * 0.6f, h * 0.79f, w, h * 0.85f); close() }, AnwC27.Road)
    // the lote tree
    val base = Offset(w * 0.38f, h * 0.7f)
    drawLine(AnwC27.Trunk, base, base + Offset(0f, -h * 0.22f), w * 0.02f, StrokeCap.Round)
    for ((dx, dy, r) in listOf(Triple(0f, -0.36f, 0.12f), Triple(-0.1f, -0.3f, 0.09f), Triple(0.1f, -0.3f, 0.09f), Triple(-0.05f, -0.42f, 0.08f), Triple(0.06f, -0.42f, 0.08f))) {
        drawCircle(if (dx == 0f) AnwC27.Leaf else AnwC27.LeafDark, w * r, base + Offset(w * dx, h * dy))
    }
    // what is hung on it
    val hung = easeOut(seg(lt, 0.2f, 0.8f)).coerceAtLeast(if (sp > 0f) 1f else 0f)
    for (k in 0 until 6) {
        val f = seg(hung, k / 7f, k / 7f + 0.3f)
        if (f <= 0f) continue
        val x = base.x + w * (-0.13f + 0.052f * k)
        val y = base.y - h * (0.2f + 0.03f * (k % 2))
        val sway = sin(t * 1.3f + k) * 3f
        withTransform({ rotate(sway, Offset(x, y)) }) {
            if (k % 2 == 0) {
                drawLine(AnwC27.Blade, Offset(x, y), Offset(x, y + h * 0.09f * f), 2f * dp, StrokeCap.Round)
                drawLine(Color(0xFF6A4A2E), Offset(x - w * 0.01f, y + h * 0.015f), Offset(x + w * 0.01f, y + h * 0.015f), 2f * dp, alpha = f)
            } else {
                drawLine(AnwC27.Cloth[k / 2 % 3], Offset(x, y), Offset(x + w * 0.003f, y + h * 0.06f * f), 3f * dp, StrokeCap.Round)
            }
        }
    }
    val stone = base + Offset(w * 0.09f, h * 0.02f)
    drawOval(Color(0xFF8A7E72), Offset(stone.x - w * 0.035f, stone.y - h * 0.05f), Size(w * 0.07f, h * 0.06f))
    drawLine(AnwC27.Cloth[0], stone + Offset(-w * 0.03f, -h * 0.025f), stone + Offset(w * 0.03f, -h * 0.03f), 2.5f * dp, alpha = hung)
    // the men who hang them
    val men = easeOut(seg(lt, 0f, 0.25f)) * (1f - easeOut(seg(ak, 0f, 0.4f)))
    for (k in 0 until 2) pointer27(base + Offset(w * (-0.07f + 0.15f * k), h * 0.03f), h * 0.17f, AnwC27.Pagan, men, 0.6f + 0.3f * sin(t * 2f + k))
    tagAt(a.tag("lotetree"), base + Offset(0f, -h * 0.5f), seg(lt, 0.4f, 0.7f), back = AnwC27.Back, ink = ArtInk.Ivory)
    // the old footprints of those before, and the red sign
    if (sp > 0f) {
        val f = seg(sp, 0f, 0.6f)
        for (k in 0 until 12) {
            if (k / 12f > f) break
            val x = w * (0.02f + 0.024f * k)
            val y = h * (0.9f - 0.012f * k) + (if (k % 2 == 0) -h * 0.008f else h * 0.008f)
            drawOval(Color(0xFF8A6A4A), Offset(x, y), Size(w * 0.012f, h * 0.01f), alpha = 0.7f)
        }
        tagAt(a.tag("sunanpath"), Offset(w * 0.14f, h * 0.95f), seg(sp, 0.3f, 0.6f), back = AnwC27.Back, ink = Color(0xFFFFB0A0))
        noSign11pub(base + Offset(w * 0.17f, -h * 0.4f), w * 0.045f, easeOut(seg(sp, 0.5f, 0.8f)))
    }
    // the column comes along the road; two stop and point
    if (ak > 0f) {
        val come = easeInOut(seg(ak, 0f, 0.5f))
        val head = mix(w * 1.05f, w * 0.66f, come)
        val walking = come in 0.01f..0.99f
        for (k in 0 until 6) {
            val x = head + k * w * 0.055f
            val y = h * (0.79f - 0.004f * k)
            val askers = k == 1 || k == 2
            val point = if (askers) easeOut(seg(ak, 0.55f, 0.75f)) * (1f - easeOut(seg(tk, 0.5f, 0.8f))) else 0f
            if (point > 0.02f) pointer27(Offset(x, y), h * 0.16f, AnwC27.Ink, 1f, point)
            else figure(Offset(x, y), h * 0.16f, AnwC27.Ink, 1f, walk = if (walking) t * 5f + k else 0f)
            if (k == 3) camel26(Offset(x + w * 0.04f, y), h * 0.15f, Color(0xFF6A4A30), 1f, t, if (walking) t * 4f else 0f)
        }
        tagAt(a.tag("askanwat"), Offset(head + w * 0.08f, h * 0.5f), seg(ak, 0.6f, 0.8f) * (1f - seg(tk, 0f, 0.2f)), back = AnwC27.Back, ink = ArtInk.Ivory)
    }
    // his ﷺ light at the head of the column
    if (tk > 0f) {
        val c = Offset(w * 0.6f, h * 0.62f)
        sirajMunir(c, w * 0.011f, easeOut(seg(tk, 0f, 0.3f)), t, nameAt = c + Offset(0f, -h * 0.12f))
        tagAt(a.tag("takbirsunan"), Offset(w * 0.66f, h * 0.36f), seg(tk, 0.3f, 0.55f), back = AnwC27.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 3. the offering, one way or the other; the vow kept or broken (dhabh-nadhr) ═════════════

private object OffC27 {
    val Wall = Color(0xFFEADBB8)
    val WallLow = Color(0xFFD8C49A)
    val Yard = Color(0xFFCDB488)
    val Mihrab = Color(0xFF2E5A4A)
    val Arch = Color(0xFFB88A52)
    val Tomb = Color(0xFF8A7A6A)
    val Dome = Color(0xFF6A8A6A)
    val Ink = Color(0xFF2A2430)
    val Sheep = Color(0xFFF2EEE4)
    val Paper = Color(0xFFFBF5E4)
    val Bread = Color(0xFFD8A060)
    val Red = Color(0xFFC0463A)
    val Green = Color(0xFF2E8A5A)
    val Back = Color(0xE6201810)
}

/**
 * A courtyard by day. Beats: twopaths (a man leads his sheep; to the right a prayer niche, «لِلَّهِ»; to the left a tomb,
 * «لِغَيْرِ اللَّهِ»: he turns toward the tomb and a red sign comes up), cursed (the curse, «لَعَنَ اللَّهُ مَنْ ذَبَحَ لِغَيْرِ
 * اللَّهِ»: the way to the tomb darkens and he turns back toward the niche), vowkeep (in front, a man holds a slip,
 * «نَذْرُ طَاعَةٍ»; it becomes bread for a poor man: kept), vowbreak (another slip, «نَذْرُ مَعْصِيَةٍ», is torn and dropped).
 */
internal fun DrawScope.offeringScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twopaths" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "cursed" to Shot(Offset(w * 0.3f, h * 0.5f), 1.25f),
        "vowkeep" to Shot(Offset(w * 0.6f, h * 0.8f), 1.4f),
        "vowbreak" to Shot(Offset(w * 0.4f, h * 0.8f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { offeringYard(a) }
}

private fun DrawScope.offeringYard(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tp = a.b("twopaths")
    val cu = a.b("cursed")
    val vk = a.b("vowkeep")
    val vb = a.b("vowbreak")
    val ground = h * 0.62f
    drawRect(Brush.verticalGradient(listOf(OffC27.Wall, OffC27.WallLow), endY = ground), size = Size(w, ground))
    drawRect(OffC27.Yard, Offset(0f, ground), Size(w, h - ground))
    // the prayer niche (right) and the tomb (left)
    val niche = Offset(w * 0.84f, ground)
    val arch = archPath26(niche.x, ground - h * 0.4f, ground, w * 0.08f)
    drawPath(arch, OffC27.Mihrab)
    drawPath(arch, OffC27.Arch, style = Stroke(3f * dp))
    tomb27(Offset(w * 0.15f, ground), w * 0.17f, OffC27.Tomb, OffC27.Dome, 1f)
    val dark = easeOut(seg(cu, 0.3f, 0.8f))
    drawRect(Brush.horizontalGradient(listOf(Color.Black.copy(alpha = 0.32f * dark), Color.Transparent), startX = 0f, endX = w * 0.42f), size = Size(w * 0.42f, h))
    tagAt(a.tag("twopaths", 0, 0), niche + Offset(0f, -h * 0.47f), seg(tp, 0.1f, 0.3f) * (1f - seg(vk, 0f, 0.25f)), back = OffC27.Back, ink = ArtInk.BrassLight)
    tagAt(a.tag("twopaths", 0, 1), Offset(w * 0.15f, ground - h * 0.25f), seg(tp, 0.2f, 0.4f) * (1f - seg(vk, 0f, 0.25f)), back = OffC27.Back, ink = Color(0xFFFFB0A0))
    // the two paths from the middle
    val mid = Offset(w * 0.5f, h * 0.7f)
    drawLine(Color(0xFFE6D2A6), mid, niche + Offset(0f, h * 0.04f), w * 0.03f, StrokeCap.Round, alpha = 0.8f)
    drawLine(Color(0xFFE6D2A6), mid, Offset(w * 0.15f, ground + h * 0.04f), w * 0.03f, StrokeCap.Round, alpha = 0.8f * (1f - 0.6f * dark))
    // the man and his sheep: toward the tomb, then back toward the niche
    val toTomb = easeInOut(seg(tp, 0.2f, 0.7f))
    val back = easeInOut(seg(cu, 0.45f, 1f))
    val x = mix(mix(w * 0.5f, w * 0.34f, toTomb), w * 0.64f, back)
    val y = mix(mix(h * 0.7f, h * 0.66f, toTomb), h * 0.67f, back)
    val moving = (toTomb in 0.01f..0.99f) || (back in 0.01f..0.99f)
    val facingRight = back > 0.01f
    withTransform({ if (facingRight) scale(-1f, 1f, pivot = Offset(x, y)) }) {
        figure(Offset(x, y), h * 0.17f, OffC27.Ink, 1f, walk = if (moving) t * 5f else 0f, arms = 0.4f)
        sheep26(Offset(x - w * 0.06f, y - h * 0.02f), w * 0.045f, OffC27.Sheep, 1f)
        drawLine(Color(0xFF8A6A4A), Offset(x - w * 0.02f, y - h * 0.08f), Offset(x - w * 0.04f, y - h * 0.035f), 1.2f * dp)
    }
    noSign11pub(Offset(w * 0.24f, ground - h * 0.12f), w * 0.05f, easeOut(seg(tp, 0.6f, 0.85f)))
    tagAt(a.tag("cursed"), Offset(w * 0.32f, h * 0.14f), seg(cu, 0.1f, 0.35f) * (1f - seg(vk, 0f, 0.25f)), back = OffC27.Back, ink = Color(0xFFFFB0A0))
    // the vows, in front
    val front = h * 0.92f
    if (vk > 0f || vb > 0f) {
        drawRect(Color(0xFFBFA676), Offset(0f, h * 0.84f), Size(w, h * 0.16f), alpha = easeOut(seg(vk, 0f, 0.2f)).coerceAtLeast(if (vb > 0f) 1f else 0f))
        val man = Offset(w * 0.5f, front)
        figure(man, h * 0.2f, OffC27.Ink, 1f, arms = 0.7f)
        // kept: the slip becomes bread for the poor man
        val poor = Offset(w * 0.7f, front)
        w8Seated(poor, h * 0.17f, Color(0xFF4A3E4A), -1f, 1f)
        val give = easeInOut(seg(vk, 0.3f, 0.75f))
        val from = man + Offset(-w * 0.05f, -h * 0.12f)
        val to = poor + Offset(-w * 0.04f, -h * 0.08f)
        val p = from + (to - from) * give
        if (give < 0.9f) {
            drawRect(OffC27.Paper, Offset(p.x - w * 0.018f, p.y - h * 0.018f), Size(w * 0.036f, h * 0.036f), alpha = easeOut(seg(vk, 0f, 0.2f)))
            tagAt(a.tag("vowkeep"), p + Offset(0f, -h * 0.07f), seg(vk, 0.1f, 0.3f) * (1f - seg(vk, 0.6f, 0.8f)), back = OffC27.Back, ink = ArtInk.BrassLight)
        } else {
            for (m in 0 until 2) drawOval(OffC27.Bread, Offset(to.x - w * 0.02f + m * w * 0.02f, to.y - h * 0.01f), Size(w * 0.02f, h * 0.015f))
            check21(poor + Offset(-w * 0.02f, -h * 0.25f), h * 0.03f, OffC27.Green, seg(vk, 0.8f, 1f), 3f * dp)
        }
        // broken: the slip is torn and dropped
        if (vb > 0f) {
            val o = easeOut(seg(vb, 0f, 0.2f))
            val c = man + Offset(-w * 0.08f, -h * 0.14f)
            val tear = easeOut(seg(vb, 0.35f, 0.6f))
            val fall = seg(vb, 0.6f, 1f)
            for (side in listOf(-1f, 1f)) {
                val q = c + Offset(side * w * 0.02f * tear, h * 0.12f * fall * fall)
                withTransform({ rotate(side * 25f * tear + side * 60f * fall, q) }) {
                    drawRect(OffC27.Paper, Offset(q.x - w * 0.009f, q.y - h * 0.018f), Size(w * 0.018f, h * 0.036f), alpha = o * (1f - 0.5f * fall))
                }
            }
            tagAt(a.tag("vowbreak"), c + Offset(-w * 0.08f, -h * 0.05f), seg(vb, 0.1f, 0.3f), back = OffC27.Back, ink = Color(0xFFFFB0A0))
            noSign11pub(c + Offset(0f, -h * 0.02f), w * 0.03f, easeOut(seg(vb, 0.4f, 0.65f)) * (1f - fall))
        }
    }
}

// ═════════════ 4. the storm: to Allah alone, not to a tomb; and the living man's rope (istiadha-istighatha) ═════════════

private object StoC27 {
    val SkyStorm = Color(0xFF2A3040)
    val SkyCalm = Color(0xFF9AB8D0)
    val Sea = Color(0xFF2A4A62)
    val SeaCalm = Color(0xFF4A7A9A)
    val Foam = Color(0xFFDDE8F0)
    val Shore = Color(0xFFC8B088)
    val Boat = Color(0xFF6A4A2E)
    val Ink = Color(0xFF14121A)
    val Tomb = Color(0xFF8A7E72)
    val Gold = Color(0xFFF2D27A)
    val Red = Color(0xFFE0584A)
    val Green = Color(0xFF3ACB7A)
    val Back = Color(0xE60E1018)
}

/**
 * A sea in a storm. Beats: duasea (a boat tossed by the waves; one of the two men lifts his hands), refugeonly (his call
 * goes up, and over the sky the verse ﴿أَمَّنْ يُجِيبُ الْمُضْطَرَّ إِذَا دَعَاهُ﴾), shrinecall (the other turns to a tomb on
 * the shore and calls to it: a dashed line, a red cross, «لِغَيْرِهِ: شِرْكٌ»), livinghelp (the storm calms; by the shore a
 * man in the water, and a living man on the shore throws him a rope: «مِنْ حَيٍّ حَاضِرٍ: جَائِزٌ»).
 */
internal fun DrawScope.stormScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "duasea" to Shot(Offset(w * 0.62f, h * 0.62f), 1.3f),
        "refugeonly" to Shot(Offset(w * 0.6f, h * 0.38f), 1.15f),
        "shrinecall" to Shot(Offset(w * 0.32f, h * 0.6f), 1.2f),
        "livinghelp" to Shot(Offset(w * 0.3f, h * 0.7f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { stormSea(a) }
}

private fun DrawScope.stormSea(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val du = a.b("duasea")
    val ro = a.b("refugeonly")
    val sc = a.b("shrinecall")
    val lh = a.b("livinghelp")
    val calm = easeInOut(seg(lh, 0f, 0.5f))
    val storm = 1f - calm
    val sea = h * 0.6f
    drawRect(Brush.verticalGradient(listOf(lerpColor(StoC27.SkyStorm, StoC27.SkyCalm, calm), lerpColor(Color(0xFF4A5468), Color(0xFFE6D8C0), calm)), endY = sea), size = Size(w, sea))
    // clouds and lightning
    for (k in 0 until 5) drawOval(lerpColor(Color(0xFF1A1E28), Color(0xFFE6EAF0), calm), Offset(w * (-0.05f + 0.22f * k) + sin(t * 0.3f + k) * w * 0.02f, h * (0.02f + 0.03f * (k % 2))), Size(w * 0.3f, h * 0.12f), alpha = 0.8f)
    if (storm > 0.5f && fract(t * 0.35f) < 0.04f) {
        drawPath(Path().apply { moveTo(w * 0.8f, h * 0.08f); lineTo(w * 0.76f, h * 0.22f); lineTo(w * 0.79f, h * 0.22f); lineTo(w * 0.74f, h * 0.38f) }, Color.White, style = Stroke(2f * dp))
    }
    // rain
    if (storm > 0.05f) for (k in 0 until 24) {
        val f = fract(t * 1.8f + k * 0.073f)
        val x = w * fract(k * 0.618f) + f * w * 0.04f
        drawLine(Color(0xFFB8C8E0), Offset(x, f * h), Offset(x - w * 0.012f, f * h + h * 0.05f), 1f * dp, alpha = 0.45f * storm)
    }
    // the sea
    drawRect(lerpColor(StoC27.Sea, StoC27.SeaCalm, calm), Offset(0f, sea), Size(w, h - sea))
    for (row in 0 until 4) {
        val y = sea + h * (0.05f + 0.09f * row)
        val path = Path()
        var x = 0f
        path.moveTo(0f, y)
        while (x <= w) {
            path.lineTo(x, y + sin(x / w * 12f + t * 2f + row) * h * (0.006f + 0.02f * storm))
            x += w * 0.02f
        }
        drawPath(path, StoC27.Foam, alpha = 0.35f, style = Stroke(1.4f * dp))
    }
    // the shore and its tomb (left)
    drawPath(Path().apply { moveTo(0f, sea - h * 0.04f); lineTo(w * 0.24f, sea - h * 0.02f); quadraticTo(w * 0.3f, sea + h * 0.06f, w * 0.26f, h); lineTo(0f, h); close() }, StoC27.Shore)
    tomb27(Offset(w * 0.1f, sea - h * 0.03f), w * 0.12f, StoC27.Tomb, Color(0xFF6A7A6A), 1f)
    // the boat, tossed
    val bc = Offset(w * 0.64f, sea + h * 0.1f)
    val toss = sin(t * 2.2f) * 10f * storm
    withTransform({ rotate(toss, bc) }) {
        drawPath(Path().apply { moveTo(bc.x - w * 0.12f, bc.y - h * 0.02f); lineTo(bc.x + w * 0.12f, bc.y - h * 0.02f); lineTo(bc.x + w * 0.08f, bc.y + h * 0.05f); lineTo(bc.x - w * 0.09f, bc.y + h * 0.05f); close() }, StoC27.Boat)
        drawLine(StoC27.Boat, bc + Offset(w * 0.02f, -h * 0.02f), bc + Offset(w * 0.02f, -h * 0.28f), 2.5f * dp)
        val lift = easeOut(seg(du, 0.2f, 0.5f))
        dua27(bc + Offset(w * 0.07f, -h * 0.02f), h * 0.15f, StoC27.Ink, 1f, lift)
        // the other man: turned toward the tomb, reaching
        val turn = easeOut(seg(sc, 0.05f, 0.3f))
        if (turn > 0f) pointer27(bc + Offset(-w * 0.06f, -h * 0.02f), h * 0.15f, StoC27.Ink, 1f, 0.7f)
        else figure(bc + Offset(-w * 0.06f, -h * 0.02f), h * 0.15f, StoC27.Ink, 1f, lean = 0.2f * sin(t * 2f))
    }
    // the call that rises, and the verse
    if (ro > 0f) {
        val f = easeOut(seg(ro, 0f, 0.5f))
        val from = bc + Offset(w * 0.07f, -h * 0.22f)
        for (k in 0 until 6) {
            val u = fract(t * 0.6f + k / 6f) * f
            drawCircle(StoC27.Gold, w * 0.004f, from + Offset(sin(u * 8f) * w * 0.01f, -h * 0.3f * u), alpha = (1f - u) * f)
        }
        fitBig26(a.big("refugeonly"), Offset(w * 0.56f, h * 0.2f), w * 0.8f, StoC27.Gold, seg(ro, 0.3f, 0.65f), glow = StoC27.Gold)
    }
    // the call to the tomb: crossed out
    if (sc > 0f) {
        val f = easeOut(seg(sc, 0.2f, 0.6f))
        val from = bc + Offset(-w * 0.1f, -h * 0.13f)
        val to = Offset(w * 0.14f, sea - h * 0.12f)
        drawLine(StoC27.Red, from, from + (to - from) * f, 1.8f * dp, StrokeCap.Round, pathEffect = dash27())
        val mid = (from + to) / 2f
        cross21(mid, h * 0.035f, StoC27.Red, seg(sc, 0.55f, 0.8f), 3f * dp)
        tagAt(a.tag("shrinecall"), mid + Offset(0f, -h * 0.1f), seg(sc, 0.5f, 0.75f) * (1f - seg(lh, 0f, 0.3f)), back = StoC27.Back, ink = Color(0xFFFFB0A0))
    }
    // the living man on the shore throws a rope
    if (lh > 0f) {
        val o = easeOut(seg(lh, 0.3f, 0.55f))
        val rescuer = Offset(w * 0.22f, sea + h * 0.02f)
        val swimmer = Offset(w * 0.4f, sea + h * 0.12f + sin(t * 2f) * h * 0.005f)
        figure(rescuer, h * 0.17f, StoC27.Ink, o, arms = 0.9f)
        drawCircle(StoC27.Ink, h * 0.017f, swimmer, alpha = o)
        drawLine(StoC27.Ink, swimmer + Offset(-w * 0.01f, 0f), swimmer + Offset(-w * 0.03f, -h * 0.04f), 2f * dp, StrokeCap.Round, alpha = o)
        val throwF = easeOut(seg(lh, 0.5f, 0.8f))
        val hand = rescuer + Offset(-h * 0.17f * 0.28f + w * 0.03f, -h * 0.13f)
        val end = hand + (swimmer + Offset(-w * 0.03f, -h * 0.04f) - hand) * throwF
        drawPath(Path().apply { moveTo(hand.x, hand.y); quadraticTo((hand.x + end.x) / 2f, min(hand.y, end.y) - h * 0.05f, end.x, end.y) }, Color(0xFFC8A060), alpha = o, style = Stroke(1.8f * dp))
        check21(swimmer + Offset(w * 0.04f, -h * 0.12f), h * 0.03f, StoC27.Green, seg(lh, 0.8f, 1f), 3f * dp)
        tagAt(a.tag("livinghelp"), Offset(w * 0.3f, sea - h * 0.2f), seg(lh, 0.7f, 0.9f), back = StoC27.Back, ink = Color(0xFFA8F0C8))
    }
}

// ═════════════ 5. the gate with two seals (shafaa) ═════════════

private object SeaC27 {
    val Sky = Color(0xFFF2EAD8)
    val SkyLow = Color(0xFFE2D2B0)
    val Plain = Color(0xFFD6C29A)
    val Stone = Color(0xFFB8A27A)
    val StoneDark = Color(0xFF8A7458)
    val Leaf = Color(0xFF6A5236)
    val Seal = Color(0xFFB8402E)
    val SealLit = Color(0xFFF2C45A)
    val Garden = Color(0xFF5A9A6A)
    val Ink = Color(0xFF2A2430)
    val Idol = Color(0xFF7A6E66)
    val Back = Color(0xE6201810)
}

/**
 * A pale plain and a great gate. Beats: sealedgate (the gate stands shut; over it ﴿قُلْ لِلَّهِ الشَّفَاعَةُ جَمِيعًا﴾),
 * permit (the first seal on it glows: «إِذْنُ اللَّهِ لِلشَّافِعِ»), pleased (the second: «رِضَاهُ عَنِ الْمَشْفُوعِ لَهُ»), refused (men
 * bring the stones they hoped would intercede: for them the seals stay dark and the stones fall, «مَنْفِيَّةٌ»), ikhlasopen
 * (his ﷺ light beside the gate, named; a man whose heart is lit comes; both seals glow and the gate opens on a garden:
 * «أَهْلُ الْإِخْلَاصِ»).
 */
internal fun DrawScope.twosealsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sealedgate" to Shot(Offset(w * 0.62f, h * 0.3f), 1.15f),
        "permit" to Shot(Offset(w * 0.66f, h * 0.48f), 1.35f),
        "pleased" to Shot(Offset(w * 0.58f, h * 0.48f), 1.35f),
        "refused" to Shot(Offset(w * 0.28f, h * 0.62f), 1.3f),
        "ikhlasopen" to Shot(Offset(w * 0.66f, h * 0.55f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { twosealsPlain(a) }
}

private fun DrawScope.twosealsPlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sg = a.b("sealedgate")
    val pm = a.b("permit")
    val pl = a.b("pleased")
    val rf = a.b("refused")
    val io = a.b("ikhlasopen")
    val ground = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(SeaC27.Sky, SeaC27.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(SeaC27.Plain, Offset(0f, ground), Size(w, h - ground))
    // the gate
    val gx = w * 0.62f
    val gl = gx - w * 0.14f
    val gr = gx + w * 0.14f
    val gt = h * 0.28f
    val opening = easeInOut(seg(io, 0.45f, 0.9f))
    drawRect(SeaC27.StoneDark, Offset(gl - w * 0.05f, gt - h * 0.08f), Size(gr - gl + w * 0.1f, ground - gt + h * 0.08f))
    drawRect(SeaC27.Stone, Offset(gl - w * 0.04f, gt - h * 0.07f), Size(gr - gl + w * 0.08f, h * 0.05f))
    // behind the leaves: a garden, seen only when they open
    drawRect(SeaC27.Garden, Offset(gl, gt), Size(gr - gl, ground - gt))
    for (k in 0 until 5) w8Palm(Offset(gl + (gr - gl) * (0.1f + 0.2f * k), ground), h * 0.3f, Color(0xFF6A4A2E), Color(0xFF3E7A4A), t + k)
    val leafW = (gr - gl) / 2f * (1f - 0.85f * opening)
    for (side in listOf(-1f, 1f)) {
        val x0 = if (side < 0f) gl else gr - leafW
        drawRect(SeaC27.Leaf, Offset(x0, gt), Size(leafW, ground - gt))
        for (k in 0 until 4) drawLine(Color(0xFF4A3622), Offset(x0, gt + (ground - gt) * (0.2f + 0.2f * k)), Offset(x0 + leafW, gt + (ground - gt) * (0.2f + 0.2f * k)), 2f * dp)
    }
    // the two seals on the leaves
    val seals = listOf(Offset(gx + w * 0.07f * (1f - 0.85f * opening) + (if (opening > 0f) (gr - gl) * 0.43f * opening else 0f), h * 0.5f), Offset(gx - w * 0.07f * (1f - 0.85f * opening) - (if (opening > 0f) (gr - gl) * 0.43f * opening else 0f), h * 0.5f))
    val darkForThem = easeOut(seg(rf, 0.3f, 0.5f)) * (1f - easeOut(seg(io, 0.2f, 0.5f)))
    for ((k, c) in seals.withIndex()) {
        val lit = easeOut(seg(if (k == 0) pm else pl, 0.1f, 0.5f)) * (1f - darkForThem)
        softGlow(c, w * 0.05f, SeaC27.SealLit, 0.7f * lit)
        drawCircle(lerpColor(SeaC27.Seal, SeaC27.SealLit, lit), w * 0.026f, c)
        drawCircle(Color(0xFF6A2018), w * 0.026f, c, alpha = 0.6f, style = Stroke(1.5f * dp))
    }
    tagAt(a.tag("permit"), seals[0] + Offset(w * 0.0f, -h * 0.1f), seg(pm, 0.3f, 0.55f) * (1f - seg(rf, 0f, 0.3f)), back = SeaC27.Back, ink = ArtInk.BrassLight)
    tagAt(a.tag("pleased"), seals[1] + Offset(0f, h * 0.1f), seg(pl, 0.3f, 0.55f) * (1f - seg(rf, 0f, 0.3f)), back = SeaC27.Back, ink = ArtInk.BrassLight)
    // the verse over the gate
    fitBig26(a.big("sealedgate"), Offset(gx, h * 0.12f), w * 0.5f, Color(0xFF5A3A10), easeOut(seg(sg, 0.2f, 0.6f)) * (1f - seg(pm, 0f, 0.2f) + seg(a.b("ikhlasopen"), 0f, 0.3f)).coerceIn(0f, 1f))
    // those who bring their stones
    if (rf > 0f) {
        val o = easeOut(seg(rf, 0f, 0.2f)) * (1f - easeOut(seg(io, 0f, 0.3f)))
        for (k in 0 until 3) {
            val foot = Offset(w * (0.16f + 0.1f * k), ground + h * 0.02f)
            figure(foot, h * 0.18f, SeaC27.Ink, o, arms = 0.5f)
            val drop = easeIn27(seg(rf, 0.5f + k * 0.08f, 0.8f + k * 0.08f))
            idol27(foot + Offset(-w * 0.05f, -h * 0.08f + h * 0.1f * drop), h * 0.05f, SeaC27.Idol, o)
        }
        noSign11pub(Offset(w * 0.26f, ground - h * 0.32f), w * 0.04f, easeOut(seg(rf, 0.55f, 0.8f)) * o)
        tagAt(a.tag("refused"), Offset(w * 0.26f, ground - h * 0.45f), seg(rf, 0.6f, 0.85f) * o, back = SeaC27.Back, ink = Color(0xFFFFB0A0))
    }
    // his ﷺ light by the gate, and the man of sincerity
    if (io > 0f) {
        val c = Offset(gr + w * 0.12f, h * 0.42f)
        sirajMunir(c, w * 0.011f, easeOut(seg(io, 0f, 0.3f)), t, nameAt = c + Offset(0f, -h * 0.12f))
        val walk = easeInOut(seg(io, 0.15f, 0.6f))
        val foot = Offset(mix(w * 0.96f, gr + w * 0.03f, walk), ground + h * 0.02f)
        figure(foot, h * 0.18f, SeaC27.Ink, easeOut(seg(io, 0.05f, 0.25f)), walk = if (walk in 0.01f..0.99f) t * 5f else 0f)
        softGlow(foot + Offset(0f, -h * 0.11f), w * 0.025f, SeaC27.SealLit, 0.8f * easeOut(seg(io, 0.2f, 0.5f)))
        tagAt(a.tag("ikhlasopen"), foot + Offset(-w * 0.02f, -h * 0.28f), seg(io, 0.6f, 0.85f), back = SeaC27.Back, ink = ArtInk.BrassLight)
    }
}

private fun easeIn27(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ═════════════ 6. the plain grave, kept plain (qubur) ═════════════

private object GraC27 {
    val Sky = Color(0xFFCFE0EE)
    val SkyLow = Color(0xFFF2E6CC)
    val Wall = Color(0xFFC8A878)
    val WallTop = Color(0xFFB0905E)
    val Ground = Color(0xFFD8C094)
    val Mound = Color(0xFFB89A6A)
    val Stone = Color(0xFF8A8076)
    val Ghost = Color(0xFF5A4A3A)
    val Red = Color(0xFFC0463A)
    val Back = Color(0xE6201810)
}

/**
 * A walled burial ground by day, its graves low and plain. Beats: nodome (a dashed outline of a domed shrine and a prayer
 * hall rises over the middle grave; it is struck and fades: «مَسَاجِدَ»), nofeast (dashed banners and a ring of people
 * going round it as at a festival: struck: «عِيدًا»), noidol (a dashed figure bowing to it: struck: «وَثَنًا يُعْبَدُ»). Each
 * time the grave is left as it was.
 */
internal fun DrawScope.plaingraveScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "nodome" to Shot(Offset(w * 0.5f, h * 0.45f), 1.15f),
        "nofeast" to Shot(Offset(w * 0.5f, h * 0.62f), 1.2f),
        "noidol" to Shot(Offset(w * 0.5f, h * 0.68f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { plaingraveYard(a) }
}

private fun DrawScope.plaingraveYard(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val nd = a.b("nodome")
    val nf = a.b("nofeast")
    val ni = a.b("noidol")
    val ground = h * 0.6f
    drawRect(Brush.verticalGradient(listOf(GraC27.Sky, GraC27.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(GraC27.Ground, Offset(0f, ground), Size(w, h - ground))
    // the low mud wall
    drawRect(GraC27.Wall, Offset(0f, ground - h * 0.08f), Size(w, h * 0.08f))
    drawRect(GraC27.WallTop, Offset(0f, ground - h * 0.085f), Size(w, h * 0.012f))
    w8Palm(Offset(w * 0.9f, ground - h * 0.06f), h * 0.36f, Color(0xFF6A4A2E), Color(0xFF4A7A3A), t)
    // plain graves: a low mound and a small upright stone
    val graves = listOf(Offset(w * 0.18f, h * 0.72f), Offset(w * 0.82f, h * 0.74f), Offset(w * 0.3f, h * 0.9f), Offset(w * 0.7f, h * 0.9f), Offset(w * 0.5f, h * 0.8f))
    for ((k, g) in graves.withIndex()) {
        val s = if (k == 4) 1.25f else 1f
        drawOval(GraC27.Mound, Offset(g.x - w * 0.07f * s, g.y - h * 0.02f * s), Size(w * 0.14f * s, h * 0.04f * s))
        drawRoundRect(GraC27.Stone, Offset(g.x + w * 0.065f * s, g.y - h * 0.05f * s), Size(w * 0.012f * s, h * 0.05f * s), CornerRadius(w * 0.004f))
    }
    val g = graves[4]
    val dash = dash27()
    // 1. a shrine raised over it: struck
    val d1 = easeOut(seg(nd, 0f, 0.45f)) * (1f - 0.65f * easeOut(seg(nd, 0.8f, 1f))) * (1f - seg(nf, 0f, 0.2f))
    if (d1 > 0f) {
        val ghost = Path().apply {
            moveTo(g.x - w * 0.14f, g.y); lineTo(g.x - w * 0.14f, g.y - h * 0.22f)
            quadraticTo(g.x - w * 0.14f, g.y - h * 0.42f, g.x, g.y - h * 0.44f)
            quadraticTo(g.x + w * 0.14f, g.y - h * 0.42f, g.x + w * 0.14f, g.y - h * 0.22f)
            lineTo(g.x + w * 0.14f, g.y)
            moveTo(g.x + w * 0.2f, g.y); lineTo(g.x + w * 0.2f, g.y - h * 0.5f); lineTo(g.x + w * 0.23f, g.y - h * 0.5f); lineTo(g.x + w * 0.23f, g.y)
        }
        drawPath(ghost, GraC27.Ghost, alpha = d1, style = Stroke(1.6f * dp, pathEffect = dash))
    }
    val c1 = seg(nd, 0.45f, 0.75f)
    if (c1 > 0f && d1 > 0f) cross21(g + Offset(0f, -h * 0.25f), h * 0.1f, GraC27.Red, c1, 4f * dp)
    // 2. a festival around it: struck
    val d2 = easeOut(seg(nf, 0f, 0.45f)) * (1f - 0.65f * easeOut(seg(nf, 0.8f, 1f))) * (1f - seg(ni, 0f, 0.2f))
    if (d2 > 0f) {
        for (k in 0 until 8) {
            val ang = t * 0.8f + k * PI.toFloat() / 4f
            val p = g + Offset(cos(ang) * w * 0.2f, sin(ang) * h * 0.07f)
            figure(p, h * 0.12f, GraC27.Ghost, 0.55f * d2, walk = t * 5f + k)
        }
        for (k in 0 until 7) {
            val x = g.x - w * 0.24f + k * w * 0.08f
            drawLine(GraC27.Ghost, Offset(x, g.y - h * 0.3f), Offset(x + w * 0.08f, g.y - h * 0.3f + h * 0.03f * (if (k % 2 == 0) 1f else -1f)), 1.2f * dp, alpha = d2, pathEffect = dash)
            drawPath(Path().apply { moveTo(x, g.y - h * 0.3f); lineTo(x + w * 0.012f, g.y - h * 0.26f); lineTo(x + w * 0.024f, g.y - h * 0.3f) }, GraC27.Ghost, alpha = d2, style = Stroke(1.2f * dp, pathEffect = dash))
        }
    }
    val c2 = seg(nf, 0.45f, 0.75f)
    if (c2 > 0f && d2 > 0f) cross21(g + Offset(0f, -h * 0.12f), h * 0.1f, GraC27.Red, c2, 4f * dp)
    // 3. a man bowing to it: struck
    val d3 = easeOut(seg(ni, 0f, 0.4f)) * (1f - 0.65f * easeOut(seg(ni, 0.85f, 1f)))
    if (d3 > 0f) {
        withTransform({ scale(-1f, 1f, pivot = g + Offset(w * 0.12f, 0f)) }) {
            w8Worshipper(g + Offset(w * 0.12f, 0f), h * 0.18f, GraC27.Ghost, 0f, easeOut(seg(ni, 0.1f, 0.4f)), 0.7f * d3)
        }
        // seen side-on: the figure faces the grave (left); drawn mirrored to face it
    }
    val c3 = seg(ni, 0.45f, 0.75f)
    if (c3 > 0f && d3 > 0f) cross21(g + Offset(-w * 0.12f, -h * 0.1f), h * 0.08f, GraC27.Red, c3, 4f * dp)
    // the three words struck out, kept at the top
    val tagsAt = listOf(Offset(w * 0.5f, h * 0.27f), Offset(w * 0.5f, h * 0.42f), Offset(w * 0.76f, h * 0.58f))
    for ((k, p) in listOf(nd, nf, ni).withIndex()) {
        val kind = when (k) { 0 -> "nodome"; 1 -> "nofeast"; else -> "noidol" }
        struckTag21(a.tag(kind), tagsAt[k], seg(p, 0.15f, 0.4f), seg(p, 0.45f, 0.8f), GraC27.Red, back = GraC27.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 7. footprints followed print for print (sunan-umam) ═════════════

private object FooC27 {
    val Sky = Color(0xFFF0C890)
    val SkyTop = Color(0xFF7A8AB8)
    val Sand = Color(0xFFDDB880)
    val SandLow = Color(0xFFC09A62)
    val Road = Color(0xFFE6CC98)
    val Old = Color(0xFF8A7A6A)
    val New = Color(0xFFE8B030)
    val Rock = Color(0xFF8A6A4A)
    val Hole = Color(0xFF2A1A12)
    val Ink = Color(0xFF2A2430)
    val Idol = Color(0xFF6A5E56)
    val Shaft = Color(0xFF8A6A3A)
    val Feather = Color(0xFFF2EEE4)
    val Back = Color(0xE6201810)
}

private fun printAt27(w: Float, h: Float, k: Int, n: Int): Offset {
    val u = k / (n - 1f)
    return Offset(w * (0.92f - 0.72f * u), h * (0.7f + 0.11f * u + 0.025f * sin(u * 9f)) + (if (k % 2 == 0) -h * 0.01f else h * 0.01f))
}

/**
 * A desert in the late afternoon. Beats: oldprints (old grey footprints wind across the sand to a lizard's hole, «مَنْ كَانَ
 * قَبْلَهَا»; new gold prints fall on them one by one), featherpair (above, two arrows side by side, their feathers cut
 * alike; the new prints reach the hole: «جُحْرُ ضَبٍّ»), joinpagans (a group leaves the road for a camp of stone idols on
 * the hill), bowidols (they bow to the stones), firmgroup (on the road a group walks on, untouched by the shadows that
 * turn away from them: «لَا يَضُرُّهُمْ مَنْ خَذَلَهُمْ»).
 */
internal fun DrawScope.footstepsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "oldprints" to Shot(Offset(w * 0.55f, h * 0.76f), 1.15f),
        "featherpair" to Shot(Offset(w * 0.45f, h * 0.35f), 1.2f),
        "joinpagans" to Shot(Offset(w * 0.3f, h * 0.5f), 1.25f),
        "bowidols" to Shot(Offset(w * 0.22f, h * 0.45f), 1.45f),
        "firmgroup" to Shot(Offset(w * 0.68f, h * 0.58f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { footstepsDesert(a) }
}

private fun DrawScope.footstepsDesert(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val op = a.b("oldprints")
    val fp = a.b("featherpair")
    val jp = a.b("joinpagans")
    val bi = a.b("bowidols")
    val fg = a.b("firmgroup")
    val horizon = h * 0.46f
    drawRect(Brush.verticalGradient(listOf(FooC27.SkyTop, FooC27.Sky), endY = horizon), size = Size(w, horizon))
    softGlow(Offset(w * 0.85f, horizon), w * 0.25f, Color(0xFFFFD08A), 0.5f)
    drawRect(Brush.verticalGradient(listOf(FooC27.Sand, FooC27.SandLow), startY = horizon, endY = h), Offset(0f, horizon), Size(w, h - horizon))
    // the hill of the idols (left) and the road
    drawPath(Path().apply { moveTo(0f, horizon + h * 0.02f); quadraticTo(w * 0.18f, horizon - h * 0.08f, w * 0.38f, horizon + h * 0.03f); close() }, Color(0xFFC8A070))
    drawPath(Path().apply { moveTo(w, h * 0.6f); quadraticTo(w * 0.5f, h * 0.56f, 0f, h * 0.62f); lineTo(0f, h * 0.66f); quadraticTo(w * 0.5f, h * 0.6f, w, h * 0.65f); close() }, FooC27.Road)
    // the lizard's hole in a low rock
    val hole = Offset(w * 0.17f, h * 0.84f)
    drawArc(FooC27.Rock, 180f, 180f, true, Offset(hole.x - w * 0.08f, hole.y - h * 0.08f), Size(w * 0.16f, h * 0.14f))
    drawOval(FooC27.Hole, Offset(hole.x - w * 0.025f, hole.y - h * 0.035f), Size(w * 0.05f, h * 0.04f))
    // the prints: old ones, then new ones on them
    val n = 14
    val oldF = seg(op, 0f, 0.4f).coerceAtLeast(if (fp > 0f) 1f else 0f)
    val newF = (seg(op, 0.35f, 1f) * 0.7f + 0.3f * seg(fp, 0.3f, 1f)).coerceAtLeast(if (jp > 0f) 1f else 0f)
    for (k in 0 until n) {
        val p = printAt27(w, h, k, n)
        if (k / (n - 1f) <= oldF) drawOval(FooC27.Old, Offset(p.x - w * 0.01f, p.y - h * 0.006f), Size(w * 0.02f, h * 0.012f), alpha = 0.8f)
        if (k / (n - 1f) <= newF) drawOval(FooC27.New, Offset(p.x - w * 0.007f, p.y - h * 0.0045f), Size(w * 0.014f, h * 0.009f))
    }
    tagAt(a.tag("oldprints"), printAt27(w, h, 0, n) + Offset(-w * 0.03f, h * 0.07f), seg(op, 0.2f, 0.45f), back = FooC27.Back, ink = ArtInk.Ivory)
    // two arrows, feather matched to feather
    if (fp > 0f) {
        val o = easeOut(seg(fp, 0f, 0.3f))
        for (k in 0 until 2) {
            val y = h * (0.28f + 0.08f * k)
            val slide = w * 0.04f * sin(t * 1.2f)
            val tail = Offset(w * 0.68f + slide, y)
            val tip = Offset(w * 0.24f + slide, y)
            drawLine(FooC27.Shaft, tail, tip, 2.5f * dp, alpha = o)
            arrowHead(tip, Offset(-1f, 0f), w * 0.025f, Color(0xFF5A5A62), 2.5f * dp, o)
            for (m in 0 until 3) {
                val fx = tail.x - m * w * 0.018f
                drawLine(FooC27.Feather, Offset(fx, y), Offset(fx + w * 0.02f, y - h * 0.03f), 2f * dp, alpha = o)
                drawLine(FooC27.Feather, Offset(fx, y), Offset(fx + w * 0.02f, y + h * 0.03f), 2f * dp, alpha = o)
            }
        }
        tagAt(a.tag("featherpair"), hole + Offset(w * 0.1f, -h * 0.1f), seg(fp, 0.6f, 0.85f), back = FooC27.Back, ink = ArtInk.Ivory)
    }
    // the camp of idols on the hill
    val camp = easeOut(seg(jp, 0.2f, 0.5f)).coerceAtLeast(if (bi > 0f) 1f else 0f)
    for (k in 0 until 3) idol27(Offset(w * (0.08f + 0.08f * k), horizon + h * 0.0f - h * 0.02f * (k % 2)), h * 0.07f, FooC27.Idol, camp)
    // some leave the road for it, then bow
    val leave = easeInOut(seg(jp, 0.1f, 0.8f))
    if (jp > 0f) for (k in 0 until 3) {
        val from = Offset(w * (0.5f + 0.05f * k), h * 0.62f)
        val to = Offset(w * (0.1f + 0.08f * k) + w * 0.04f, horizon + h * 0.07f)
        val p = from + (to - from) * leave
        val bow = easeOut(seg(bi, 0.1f + k * 0.1f, 0.4f + k * 0.1f))
        if (bow > 0f) w8Worshipper(p, h * 0.12f, FooC27.Ink, 0f, bow) else figure(p, h * 0.12f, FooC27.Ink, 1f, walk = if (leave in 0.01f..0.99f) t * 5f + k else 0f)
    }
    // the group that stays on the road
    if (fg > 0f) {
        val o = easeOut(seg(fg, 0f, 0.25f))
        val go = easeInOut(seg(fg, 0.1f, 1f))
        for (k in 0 until 4) {
            val p = Offset(w * (0.78f + 0.05f * k) - w * 0.12f * go, h * (0.62f - 0.004f * k))
            figure(p, h * 0.15f, FooC27.Ink, o, walk = if (go in 0.01f..0.99f) t * 5f + k else 0f)
        }
        // shadows that turn away from them
        for (k in 0 until 3) {
            val p = Offset(w * (0.62f + 0.12f * k), h * (0.8f + 0.03f * (k % 2)))
            val turn = easeOut(seg(fg, 0.3f + 0.1f * k, 0.6f + 0.1f * k))
            withTransform({ scale(-1f, 1f, pivot = p) }) { figure(p + Offset(-w * 0.04f * turn, 0f), h * 0.13f, Color(0xFF6A5A62), 0.45f * o) }
        }
        softGlow(Offset(w * 0.75f - w * 0.12f * go, h * 0.55f), w * 0.12f, Color(0xFFFFE6A0), 0.35f * o)
        tagAt(a.tag("firmgroup"), Offset(w * 0.7f, h * 0.38f), seg(fg, 0.5f, 0.75f), back = FooC27.Back, ink = ArtInk.BrassLight)
    }
}
