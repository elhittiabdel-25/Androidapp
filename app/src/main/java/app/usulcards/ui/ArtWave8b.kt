package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/* Wave 8, round 1 (continued from ArtWave8.kt): a drafting desk, a mosque by day, a parchment in two halves, a
 * field through the seasons and a street at night. */

// ═════════════ 6. ruler: the straightedge and the hands that draw by it (manhaj-masum) ═════════════

private object DeskC8 {
    val Wood = Color(0xFF4E3220)
    val WoodLight = Color(0xFF6E4A30)
    val Paper = Color(0xFFF5EDDA)
    val PaperShade = Color(0xFFE3D5B6)
    val Grid = Color(0xFFD2C4A2)
    val Brass = Color(0xFFCFA54C)
    val BrassDark = Color(0xFF8E6A26)
    val Ink = Color(0xFF26201A)
    val Pens = listOf(Color(0xFF2F4E8C), Color(0xFF1F6E5A), Color(0xFF9C3A2F))
    val Red = Color(0xFFB8342A)
    val Green = Color(0xFF2E7D4F)
    val Gold = Color(0xFFE2B044)
    val Slip = Color(0xFFFFFBF0)
    val Back = Color(0xE6302418)
}

/**
 * A drafting desk with a brass straightedge, his ﷺ light at its end. Beats: sortout (people's sayings come as slips
 * of paper laid against the edge: on each, the line that runs true by it is kept with a check, the line that leaves
 * it is struck out; then the straightedge glows whole), trace (three pens draw along it, each line wobbling, while
 * the line of them together runs exactly on the edge, in gold), veer (one pen drifts away, and a red bracket
 * measures how far, wider and wider).
 */
internal fun DrawScope.rulerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sortout" to Shot(Offset(w * 0.5f, h * 0.5f), 1.12f),
        "trace" to Shot(Offset(w * 0.64f, h * 0.44f), 1.3f),
        "veer" to Shot(Offset(w * 0.34f, h * 0.56f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { rulerDesk(a) }
}

private fun DrawScope.pen8(tip: Offset, s: Float, color: Color, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    val back = tip + Offset(s * 0.55f, -s)
    drawLine(Color.Black, tip + Offset(2f * density, 2f * density), back + Offset(4f * density, 4f * density), s * 0.14f, StrokeCap.Round, alpha = 0.15f * alpha)
    drawLine(color, tip + Offset(s * 0.08f, -s * 0.15f), back, s * 0.14f, StrokeCap.Round, alpha = alpha)
    drawLine(DeskC8.BrassDark, tip, tip + Offset(s * 0.1f, -s * 0.18f), s * 0.07f, StrokeCap.Round, alpha = alpha)
}

private fun DrawScope.rulerDesk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val so = a.b("sortout")
    val tr = a.b("trace")
    val vr = a.b("veer")
    drawRect(Brush.linearGradient(listOf(DeskC8.WoodLight, DeskC8.Wood), start = Offset(0f, 0f), end = Offset(w, h)))
    for (k in 0 until 14) {
        val y = h * k / 14f
        drawLine(DeskC8.Wood, Offset(0f, y), Offset(w, y + h * 0.02f * sin(k * 1.3f)), 0.8f * dp, alpha = 0.5f)
    }
    // the sheet, with its faint grid
    withTransform({ rotate(-1.2f, Offset(w * 0.5f, h * 0.5f)) }) {
        drawRect(Color.Black, topLeft = Offset(w * 0.055f, h * 0.09f), size = Size(w * 0.9f, h * 0.86f), alpha = 0.25f)
        drawRect(Brush.verticalGradient(listOf(DeskC8.Paper, DeskC8.PaperShade)), topLeft = Offset(w * 0.045f, h * 0.07f), size = Size(w * 0.9f, h * 0.86f))
        var gx = w * 0.07f
        while (gx < w * 0.94f) { drawLine(DeskC8.Grid, Offset(gx, h * 0.08f), Offset(gx, h * 0.92f), 0.5f * dp, alpha = 0.6f); gx += w * 0.04f }
        var gy = h * 0.1f
        while (gy < h * 0.92f) { drawLine(DeskC8.Grid, Offset(w * 0.05f, gy), Offset(w * 0.94f, gy), 0.5f * dp, alpha = 0.6f); gy += w * 0.04f }
    }
    // the straightedge: brass, with its ticks; the working edge is its lower side
    val edge = h * 0.38f
    val rl = w * 0.07f
    val rr = w * 0.9f
    drawRect(Color.Black, topLeft = Offset(rl + 2f * dp, edge - h * 0.055f + 3f * dp), size = Size(rr - rl, h * 0.055f), alpha = 0.25f)
    drawRect(Brush.verticalGradient(listOf(Color(0xFFF0D58A), DeskC8.Brass, DeskC8.BrassDark), startY = edge - h * 0.055f, endY = edge), topLeft = Offset(rl, edge - h * 0.055f), size = Size(rr - rl, h * 0.055f))
    var tx = rl + w * 0.01f
    var n = 0
    while (tx < rr - w * 0.005f) {
        drawLine(DeskC8.BrassDark, Offset(tx, edge), Offset(tx, edge - h * (if (n % 5 == 0) 0.022f else 0.011f)), 0.8f * dp)
        tx += w * 0.0125f
        n++
    }
    val whole = easeOut(seg(so, 0.78f, 1f))
    if (whole > 0f) drawLine(DeskC8.Gold, Offset(rl, edge), Offset(rr, edge), 2.4f * dp, alpha = 0.9f * whole)
    sirajMunir(Offset(rr + w * 0.035f, edge - h * 0.028f), 3.8f * dp, easeOut(seg(so, 0f, 0.2f)), t)

    // sortout: three slips; from each, the line that runs true is kept and the one that leaves the edge struck out
    val slipFade = 1f - 0.8f * easeOut(seg(tr, 0f, 0.25f))
    for (k in 0 until 3) {
        val come = easeOut(seg(so, 0.04f + 0.16f * k, 0.2f + 0.16f * k))
        if (come <= 0f) continue
        val judge = easeOut(seg(so, 0.24f + 0.16f * k, 0.36f + 0.16f * k))
        val cx = w * (0.75f - 0.25f * k)
        val c = Offset(cx, mix(h * 1.1f, h * 0.53f, come))
        val sw = w * 0.2f
        val sh = h * 0.16f
        withTransform({ rotate((1f - come) * (8f - 6f * k), c) }) {
            drawRect(Color.Black, topLeft = c - Offset(sw / 2 - 2f * dp, sh / 2 - 3f * dp), size = Size(sw, sh), alpha = 0.18f * slipFade)
            drawRect(DeskC8.Slip, topLeft = c - Offset(sw / 2, sh / 2), size = Size(sw, sh), alpha = slipFade)
            // the true line and the stray one (which is which differs from one slip to the next)
            val trueTop = k != 1
            val y1 = c.y - sh * 0.18f
            val y2 = c.y + sh * 0.2f
            val straightY = if (trueTop) y1 else y2
            val strayY = if (trueTop) y2 else y1
            val skew = if (k == 0) sh * 0.25f else -sh * 0.22f
            drawLine(DeskC8.Ink, Offset(c.x - sw * 0.4f, straightY), Offset(c.x + sw * 0.32f, straightY), 1.4f * dp, StrokeCap.Round, alpha = slipFade)
            drawLine(DeskC8.Ink, Offset(c.x - sw * 0.4f, strayY), Offset(c.x + sw * 0.32f, strayY + skew), 1.4f * dp, StrokeCap.Round, alpha = slipFade * (1f - 0.55f * judge))
            if (judge > 0f) {
                checkMark(Offset(c.x + sw * 0.42f, straightY), sh * 0.08f, DeskC8.Green, 1.8f * dp, judge * slipFade)
                drawLine(DeskC8.Red, Offset(c.x - sw * 0.42f, strayY + skew * 0.5f - sh * 0.06f), Offset(c.x - sw * 0.42f + sw * 0.76f * judge, strayY + skew * 0.5f + sh * 0.06f), 1.8f * dp, StrokeCap.Round, alpha = slipFade)
                crossMark(Offset(c.x + sw * 0.42f, strayY + skew), sh * 0.06f, DeskC8.Red, 1.8f * dp, judge * slipFade)
            }
        }
    }

    // trace: three pens draw along the edge; their lines wobble, the line of them together runs true
    fun lineY(k: Int, x: Float): Float {
        val off = (k - 1) * h * 0.012f
        val wob = h * 0.011f * sin(x / dp * (0.07f + 0.02f * k) + k * 2.1f) + h * 0.006f * sin(x / dp * 0.19f + k)
        return edge + h * 0.03f + off + wob
    }
    val runA = easeInOut(seg(tr, 0.05f, 1f))
    val runB = easeInOut(seg(vr, 0f, 0.85f))
    val headX = if (vr > 0f) mix(w * 0.5f, w * 0.12f, runB) else mix(w * 0.88f, w * 0.5f, runA)
    if (tr > 0f) {
        val veerFrom = w * 0.5f
        for (k in 0 until 3) {
            val pts = ArrayList<Offset>()
            var x = w * 0.88f
            while (x >= headX) {
                var y = lineY(k, x)
                if (k == 2 && x < veerFrom) y += h * 0.42f * ((veerFrom - x) / (veerFrom - w * 0.12f)).let { it * it }
                pts.add(Offset(x, y))
                x -= 2f * dp
            }
            if (pts.size > 1) {
                val col = if (k == 2 && vr > 0f) lerpColor(DeskC8.Pens[k], Color(0xFF9A948A), seg(vr, 0.3f, 1f)) else DeskC8.Pens[k]
                drawPath(w8Poly(pts), col, style = Stroke(1.5f * dp, cap = StrokeCap.Round, join = StrokeJoin.Round))
                pen8(pts.last(), h * 0.13f, DeskC8.Pens[k])
            }
        }
        // together they run true: their middle line lies on the edge itself
        drawLine(DeskC8.Gold, Offset(w * 0.88f, edge + h * 0.003f), Offset(max(headX, if (vr > 0f) w * 0.12f else headX), edge + h * 0.003f), 3f * dp, StrokeCap.Round, alpha = 0.95f)
        softGlow(Offset(headX, edge), 8f * dp, DeskC8.Gold, 0.6f)
    }
    // veer: how far he has gone, measured
    if (vr > 0.15f) {
        val x = headX + w * 0.02f
        val y2 = lineY(2, x) + h * 0.42f * ((w * 0.5f - x) / (w * 0.38f)).coerceIn(0f, 1f).let { it * it }
        val top = edge + h * 0.005f
        val al = seg(vr, 0.15f, 0.3f)
        drawLine(DeskC8.Red, Offset(x - w * 0.03f, top), Offset(x - w * 0.03f, y2), 1.6f * dp, alpha = al)
        drawLine(DeskC8.Red, Offset(x - w * 0.04f, top), Offset(x - w * 0.02f, top), 1.6f * dp, alpha = al)
        drawLine(DeskC8.Red, Offset(x - w * 0.04f, y2), Offset(x - w * 0.02f, y2), 1.6f * dp, alpha = al)
        tagAt(a.tag("veer"), Offset(x - w * 0.15f, (top + y2) / 2f), seg(vr, 0.35f, 0.6f), back = DeskC8.Back, ink = Color(0xFFFFB8A8))
    }
}

// ═════════════ 7. mosque: two who differ pray one behind the other (ikhtilaf-ijtihad) ═════════════

private object MasjidC8 {
    val Wall = Color(0xFFF0E5CD)
    val WallShade = Color(0xFFD8C7A4)
    val Arch = Color(0xFFC9AE7C)
    val Mihrab = Color(0xFF2F6B66)
    val MihrabGold = Color(0xFFD8B05A)
    val Carpet = Color(0xFF8E2F33)
    val CarpetLine = Color(0xFFD9A75A)
    val Window = Color(0xFFFFF1C8)
    val Beam = Color(0xFFFFE7A8)
    val Wood = Color(0xFF6A4428)
    val Ink = Color(0xFF2A2026)
    val Outside = Color(0xFFF6DCA8)
    val Crack = Color(0xFF5A2A20)
    val Bubble = Color(0xFFFFFBF0)
    val Glow = Color(0xFFFFD77A)
}

/**
 * Inside a mosque by day, the mihrab on the left. Beats: differ (two men sit with their books and each holds his
 * view: one raises his hands at the takbir, the other does not, shown in their two thought bubbles), clasp (they
 * stand and clasp hands, warmly), behind (they pray, one leading and the other behind him, each praying as he
 * holds, and others join the row), leave (out through the door: two others turn their backs and a wall goes up
 * between them).
 */
internal fun DrawScope.mosqueScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "differ" to Shot(Offset(w * 0.5f, h * 0.7f), 1.35f),
        "clasp" to Shot(Offset(w * 0.5f, h * 0.68f), 1.35f),
        "behind" to Shot(Offset(w * 0.34f, h * 0.68f), 1.2f),
        "leave" to Shot(Offset(w * 0.87f, h * 0.6f), 1.8f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.16f) { mosqueHall(a) }
}

/** A thought bubble with a small praying figure in it: [raise] shows the hands raised at the takbir. */
private fun DrawScope.viewBubble(c: Offset, r: Float, from: Offset, raise: Boolean, alpha: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 2) {
        val p = from + (c - from) * (0.35f + 0.25f * k)
        drawCircle(MasjidC8.Bubble, r * (0.12f + 0.08f * k), p, alpha = alpha)
    }
    drawCircle(Color.Black, r * 1.02f, c + Offset(0f, r * 0.08f), alpha = 0.12f * alpha)
    drawCircle(MasjidC8.Bubble, r, c, alpha = alpha)
    drawCircle(MasjidC8.CarpetLine, r, c, alpha = alpha * 0.8f, style = Stroke(1.2f * density))
    w8Worshipper(c + Offset(0f, r * 0.62f), r * 1.15f, MasjidC8.Ink, if (raise) 1f else 0f, 0f, alpha)
}

private fun DrawScope.mosqueHall(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val df = a.b("differ")
    val cl = a.b("clasp")
    val bh = a.b("behind")
    val lv = a.b("leave")
    val floorY = h * 0.66f
    drawRect(Brush.verticalGradient(listOf(MasjidC8.Wall, MasjidC8.WallShade), endY = floorY))
    // an arcade on the back wall, two windows with light falling in
    for (k in 0 until 4) {
        val x = w * (0.3f + 0.16f * k)
        drawPath(horseshoe(x, h * 0.3f, w * 0.05f, w * 0.042f, floorY), MasjidC8.WallShade)
        drawPath(horseshoe(x, h * 0.3f, w * 0.05f, w * 0.042f, floorY), MasjidC8.Arch, style = Stroke(1.5f * dp))
    }
    for (x in listOf(0.38f, 0.62f)) {
        drawPath(horseshoe(w * x, h * 0.16f, w * 0.022f, w * 0.018f, h * 0.27f), MasjidC8.Window)
        lightShafts(Offset(w * x, h * 0.2f), PI.toFloat() * 0.62f, 0.25f, h * 0.7f, MasjidC8.Beam, 0.8f, t, 3)
    }
    // the qibla wall on the left, with the mihrab
    drawRect(MasjidC8.WallShade, topLeft = Offset(0f, 0f), size = Size(w * 0.16f, floorY))
    drawPath(horseshoe(w * 0.09f, h * 0.3f, w * 0.055f, w * 0.045f, floorY), MasjidC8.Mihrab)
    drawPath(horseshoe(w * 0.09f, h * 0.3f, w * 0.055f, w * 0.045f, floorY), MasjidC8.MihrabGold, style = Stroke(2.2f * dp))
    drawPath(horseshoe(w * 0.09f, h * 0.3f, w * 0.07f, w * 0.06f, floorY), MasjidC8.MihrabGold, alpha = 0.6f, style = Stroke(1f * dp))
    drawPath(starPath(Offset(w * 0.09f, h * 0.26f), w * 0.02f), MasjidC8.MihrabGold)
    // the door on the right, open on the bright street
    val door = horseshoe(w * 0.88f, h * 0.36f, w * 0.05f, w * 0.042f, floorY)
    drawPath(door, MasjidC8.Outside)
    drawPath(door, MasjidC8.Wood, style = Stroke(3f * dp))
    // the carpet with its rows
    drawRect(MasjidC8.Carpet, topLeft = Offset(0f, floorY), size = Size(w, h - floorY))
    for (k in 0 until 4) {
        val y = floorY + (h - floorY) * (0.12f + 0.27f * k)
        drawLine(MasjidC8.CarpetLine, Offset(0f, y), Offset(w, y), 1f * dp, alpha = 0.6f)
        var x = w * 0.02f
        while (x < w) {
            drawPath(horseshoe(x + w * 0.02f, y - h * 0.035f, w * 0.012f, w * 0.01f, y), MasjidC8.CarpetLine, alpha = 0.25f, style = Stroke(0.8f * dp))
            x += w * 0.05f
        }
    }

    val tall = h * 0.3f
    val seatL = Offset(w * 0.4f, h * 0.9f)
    val seatR = Offset(w * 0.58f, h * 0.9f)
    val stand = easeInOut(seg(cl, 0f, 0.3f))
    val goPray = easeInOut(seg(bh, 0f, 0.3f))
    // A holds that the hands are raised; B (who leads) holds that they are not
    val aRaises = true
    if (goPray <= 0f) {
        if (stand < 0.5f) {
            // sitting with their books
            val sit = 1f - stand * 2f
            for ((k, s) in listOf(seatL, seatR).withIndex()) {
                val book = s + Offset(if (k == 0) w * 0.06f else -w * 0.06f, -h * 0.02f)
                drawLine(MasjidC8.Wood, book + Offset(-w * 0.02f, h * 0.02f), book + Offset(w * 0.02f, -h * 0.03f), 2f * dp, alpha = sit)
                drawLine(MasjidC8.Wood, book + Offset(w * 0.02f, h * 0.02f), book + Offset(-w * 0.02f, -h * 0.03f), 2f * dp, alpha = sit)
                drawPath(Path().apply { moveTo(book.x - w * 0.025f, book.y - h * 0.035f); lineTo(book.x, book.y - h * 0.025f); lineTo(book.x + w * 0.025f, book.y - h * 0.035f); lineTo(book.x + w * 0.025f, book.y - h * 0.05f); lineTo(book.x, book.y - h * 0.04f); lineTo(book.x - w * 0.025f, book.y - h * 0.05f); close() }, MasjidC8.Bubble, alpha = sit)
                val speak = if (k == 0) bump(seg(df, 0.1f, 0.5f)) else bump(seg(df, 0.4f, 0.8f))
                w8Seated(s, tall * 0.95f, MasjidC8.Ink, if (k == 0) 1f else -1f, sit, speak)
            }
        } else {
            // standing, then reaching out and clasping hands
            val reach = easeOut(seg(cl, 0.3f, 0.6f))
            val l = Offset(mix(seatL.x, w * 0.44f, reach), h * 0.92f)
            val r = Offset(mix(seatR.x, w * 0.54f, reach), h * 0.92f)
            withTransform({ scale(-1f, 1f, pivot = l) }) { figure(l, tall, MasjidC8.Ink, arms = 0.2f + 0.8f * reach, walk = if (reach in 0.01f..0.99f) t * 6f else 0f) }
            figure(r, tall, MasjidC8.Ink, arms = 0.2f + 0.8f * reach, walk = if (reach in 0.01f..0.99f) t * 6f else 0f)
            val hands = Offset((l.x + r.x) / 2f, l.y - tall * 0.5f)
            val warm = easeOut(seg(cl, 0.55f, 0.8f))
            softGlow(hands, w * 0.1f, MasjidC8.Glow, 0.8f * warm)
            if (warm > 0f) drawPath(w8Heart(hands + Offset(0f, -tall * 0.55f), w * 0.022f * warm), Color(0xFFE0604A), alpha = warm * (1f - seg(cl, 0.9f, 1f) * 0.3f))
        }
        // their two views, still held
        val bubbles = seg(df, 0.15f, 0.35f) * (1f - 0.55f * seg(cl, 0.5f, 0.9f))
        viewBubble(Offset(w * 0.3f, h * 0.48f), w * 0.055f, seatL + Offset(0f, -tall * 0.6f), aRaises, bubbles)
        viewBubble(Offset(w * 0.7f, h * 0.48f), w * 0.055f, seatR + Offset(0f, -tall * 0.6f), !aRaises, seg(df, 0.35f, 0.55f) * (1f - 0.55f * seg(cl, 0.5f, 0.9f)))
    } else {
        // behind: B leads before the mihrab, A prays behind him; each as he holds
        val imam = Offset(mix(w * 0.54f, w * 0.24f, goPray), h * 0.9f)
        val follower = Offset(mix(w * 0.44f, w * 0.38f, goPray), h * 0.93f)
        val p = seg(bh, 0.3f, 1f)
        val takbir = bump(seg(p, 0.02f, 0.3f))
        val bow = bump(seg(p, 0.38f, 0.85f)).let { min(1f, it * 1.6f) }
        if (goPray < 1f) {
            figure(imam, tall, MasjidC8.Ink, walk = t * 6f)
            figure(follower, tall * 1.04f, MasjidC8.Ink, walk = t * 6f + 1f)
        } else {
            w8Worshipper(imam, tall, MasjidC8.Ink, 0f, bow)
            w8Worshipper(follower, tall * 1.04f, MasjidC8.Ink, if (aRaises) takbir else 0f, bow)
            // others join the row behind
            for (k in 0 until 3) {
                val j = easeOut(seg(p, 0.2f + 0.12f * k, 0.35f + 0.12f * k))
                if (j <= 0f) continue
                w8Worshipper(Offset(w * (0.52f + 0.13f * k), h * 0.95f), tall * 1.06f, MasjidC8.Ink.copy(alpha = 0.85f), 0f, bow, j)
            }
            softGlow(Offset(w * 0.09f, h * 0.45f), w * 0.12f, MasjidC8.Glow, 0.45f * seg(p, 0f, 0.3f))
        }
    }
    // leave: out in the street, two others turn their backs and a wall goes up between them
    if (lv > 0f) {
        val dc = Offset(w * 0.88f, floorY)
        val ft = h * 0.13f
        val turn = easeOut(seg(lv, 0.15f, 0.45f))
        val wall = easeOut(seg(lv, 0.45f, 0.85f))
        clipRect(w * 0.83f, h * 0.2f, w * 0.93f, floorY) {
            drawRect(Color(0xFFE9C98C), topLeft = Offset(w * 0.83f, floorY - h * 0.03f), size = Size(w * 0.1f, h * 0.03f))
            val l = dc + Offset(-w * 0.025f - w * 0.012f * turn, -h * 0.01f)
            val r = dc + Offset(w * 0.025f + w * 0.012f * turn, -h * 0.01f)
            withTransform({ scale(if (turn < 0.5f) -1f else 1f, 1f, pivot = l) }) { figure(l, ft, MasjidC8.Ink, arms = if (turn < 0.5f) 0.8f else -0.4f) }
            withTransform({ scale(if (turn < 0.5f) 1f else -1f, 1f, pivot = r) }) { figure(r, ft, MasjidC8.Ink, arms = if (turn < 0.5f) 0.8f else -0.4f) }
            if (wall > 0f) {
                drawRect(MasjidC8.Crack, topLeft = Offset(dc.x - w * 0.005f, dc.y - h * 0.01f - ft * 1.2f * wall), size = Size(w * 0.01f, ft * 1.2f * wall))
                for (k in 0 until (5 * wall).toInt()) drawLine(Color(0xFF3A1A12), Offset(dc.x - w * 0.005f, dc.y - h * 0.01f - k * ft * 0.22f), Offset(dc.x + w * 0.005f, dc.y - h * 0.01f - k * ft * 0.22f), 0.8f * dp)
            }
        }
    }
}

// ═════════════ 8. maze: the cool of certainty and the stairs that go nowhere (bard-yaqin) ═════════════

private object ParchC8 {
    val Paper = Color(0xFFEBDDBD)
    val PaperLow = Color(0xFFD8C39A)
    val Ink = Color(0xFF3A2A1A)
    val Sepia = Color(0xFF7A5A3A)
    val Cool = Color(0xFF6FA8C8)
    val CoolLight = Color(0xFFD5ECF5)
    val Palm = Color(0xFF4E6B3A)
    val Trunk = Color(0xFF6B4E30)
    val Gold = Color(0xFFB4852B)
    val Dusk = Color(0xFF5A4A5A)
    val Stone = Color(0xFFB8A27A)
    val Books = listOf(Color(0xFF7A2E2A), Color(0xFF2E4A6E), Color(0xFF4E6A3A), Color(0xFF6E4A2A))
    val Back = Color(0xE63A2A1A)
}

/**
 * A parchment drawn in two halves. Beats: coolwell (on the right, an ordinary man draws water at a well under a
 * palm, drinks and sits in the shade, cool and at rest), climb (on the left, a tower of stairs built of books; a
 * man climbs it flight after flight, fast, and at the top finds nothing: he turns with empty hands in the wind),
 * regret (the two books at the top, named, close; he turns toward the cool side).
 */
internal fun DrawScope.mazeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "coolwell" to Shot(Offset(w * 0.76f, h * 0.66f), 1.35f),
        "climb" to Shot(Offset(w * 0.26f, h * 0.5f), 1.1f),
        "regret" to Shot(Offset(w * 0.3f, h * 0.2f), 1.45f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.16f) { mazePage(a) }
}

private fun DrawScope.mazePage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cw = a.b("coolwell")
    val cb = a.b("climb")
    val rg = a.b("regret")
    drawRect(Brush.verticalGradient(listOf(ParchC8.Paper, ParchC8.PaperLow)))
    for (k in 0 until 50) {
        val p = Offset(w8Rnd(k, 12.9f) * w, w8Rnd(k, 78.2f) * h)
        drawLine(ParchC8.Sepia, p, p + Offset((4f + 8f * w8Rnd(k, 3.7f)) * dp, 0f), 0.5f * dp, alpha = 0.2f)
    }
    val m = w * 0.03f
    drawRect(ParchC8.Gold, topLeft = Offset(m, m), size = Size(w - 2 * m, h - 2 * m), style = Stroke(1.4f * dp))
    // the two halves: dusk on the left, cool morning on the right
    drawRect(ParchC8.Dusk, topLeft = Offset(m, m), size = Size(w * 0.5f - m, h - 2 * m), alpha = 0.18f + 0.1f * seg(cb, 0f, 0.3f))
    drawRect(ParchC8.Cool, topLeft = Offset(w * 0.5f, m), size = Size(w * 0.5f - m, h - 2 * m), alpha = 0.08f + 0.12f * seg(cw, 0f, 0.4f))
    drawLine(ParchC8.Gold, Offset(w * 0.5f, m), Offset(w * 0.5f, h - m), 1.6f * dp)
    val ground = h * 0.88f
    drawLine(ParchC8.Ink, Offset(m, ground), Offset(w - m, ground), 1.2f * dp, alpha = 0.7f)

    // the right half: a palm, a well, a man at rest
    val palm = Offset(w * 0.88f, ground)
    w8Palm(palm, h * 0.5f, ParchC8.Trunk, ParchC8.Palm, t)
    val well = Offset(w * 0.66f, ground)
    drawRect(ParchC8.Stone, topLeft = Offset(well.x - w * 0.05f, ground - h * 0.1f), size = Size(w * 0.1f, h * 0.1f))
    for (k in 0 until 3) drawLine(ParchC8.Sepia, Offset(well.x - w * 0.05f, ground - h * 0.033f * (k + 1)), Offset(well.x + w * 0.05f, ground - h * 0.033f * (k + 1)), 0.8f * dp, alpha = 0.6f)
    drawLine(ParchC8.Trunk, Offset(well.x - w * 0.045f, ground - h * 0.1f), Offset(well.x - w * 0.045f, ground - h * 0.24f), 2f * dp)
    drawLine(ParchC8.Trunk, Offset(well.x + w * 0.045f, ground - h * 0.1f), Offset(well.x + w * 0.045f, ground - h * 0.24f), 2f * dp)
    drawLine(ParchC8.Trunk, Offset(well.x - w * 0.055f, ground - h * 0.24f), Offset(well.x + w * 0.055f, ground - h * 0.24f), 2.2f * dp)
    val pull = bump(seg(cw, 0.08f, 0.5f))
    val bucketY = ground - h * 0.12f - h * 0.0f + h * 0.12f * (1f - pull) - h * 0.04f * pull
    drawLine(ParchC8.Sepia, Offset(well.x, ground - h * 0.24f), Offset(well.x, bucketY), 0.8f * dp)
    if (cw > 0f) drawRect(ParchC8.Trunk, topLeft = Offset(well.x - w * 0.012f, bucketY), size = Size(w * 0.024f, h * 0.03f), alpha = if (bucketY > ground - h * 0.1f) 0.2f else 1f)
    val tall = h * 0.3f
    val sitDown = easeInOut(seg(cw, 0.6f, 0.85f))
    if (cw > 0f) {
        val stand = Offset(well.x + w * 0.07f, ground)
        if (sitDown < 0.5f) {
            val drink = bump(seg(cw, 0.45f, 0.65f))
            figure(stand, tall, ParchC8.Ink, alpha = seg(cw, 0f, 0.1f), arms = 0.5f + 0.5f * pull + 0.3f * drink, lean = 0.12f * pull)
        } else {
            val seat = Offset(w * 0.8f, ground)
            w8Seated(seat, tall, ParchC8.Ink, -1f, 1f, 0f)
            // the cool of certainty: rings of cool air around him, a breeze in the palm
            val cool = easeOut(seg(cw, 0.75f, 1f))
            for (k in 0 until 3) {
                val f = fract(t * 0.35f + k / 3f)
                drawCircle(ParchC8.Cool, tall * (0.3f + 0.5f * f), seat + Offset(0f, -tall * 0.3f), alpha = 0.45f * cool * (1f - f), style = Stroke(1.2f * dp))
            }
            softGlow(seat + Offset(0f, -tall * 0.3f), tall * 0.5f, ParchC8.CoolLight, 0.5f * cool)
        }
    }

    // the left half: a tower of stairs built of books
    val flights = listOf(
        Offset(w * 0.08f, ground) to Offset(w * 0.4f, h * 0.74f),
        Offset(w * 0.4f, h * 0.74f) to Offset(w * 0.1f, h * 0.58f),
        Offset(w * 0.1f, h * 0.58f) to Offset(w * 0.4f, h * 0.43f),
        Offset(w * 0.4f, h * 0.43f) to Offset(w * 0.12f, h * 0.29f),
        Offset(w * 0.12f, h * 0.29f) to Offset(w * 0.34f, h * 0.17f),
    )
    val built = easeOut(seg(cb, 0f, 0.3f)) * flights.size
    for ((k, f) in flights.withIndex()) {
        val b = (built - k).coerceIn(0f, 1f)
        if (b <= 0f) continue
        val (p0, p1) = f
        val steps = 7
        for (s in 0 until steps) {
            val u = s / steps.toFloat()
            if (u > b) break
            val q = p0 + (p1 - p0) * u
            val bw = kotlin.math.abs(p1.x - p0.x) / steps
            val bh = kotlin.math.abs(p1.y - p0.y) / steps + h * 0.004f
            drawRect(ParchC8.Books[(k + s) % 4], topLeft = Offset(min(q.x, q.x + (p1.x - p0.x) / steps), q.y - bh), size = Size(bw, bh), alpha = 0.9f)
            drawLine(ParchC8.Paper, Offset(min(q.x, q.x + (p1.x - p0.x) / steps) + 1.5f * dp, q.y - bh * 0.5f), Offset(min(q.x, q.x + (p1.x - p0.x) / steps) + bw - 1.5f * dp, q.y - bh * 0.5f), 0.6f * dp, alpha = 0.6f)
        }
        drawLine(ParchC8.Ink, p0, p1, 0.9f * dp, alpha = 0.5f * b)
    }
    // the platform at the top: nothing there, only wind
    val topP = Offset(w * 0.34f, h * 0.17f)
    if (built >= flights.size) drawRect(ParchC8.Ink, topLeft = Offset(topP.x - w * 0.06f, topP.y), size = Size(w * 0.12f, h * 0.012f), alpha = 0.7f)
    val climbU = easeInOut(seg(cb, 0.15f, 0.78f))
    if (cb > 0.1f) {
        val path = flights.flatMap { listOf(it.first, it.second) }
        val foot = w8Along(path, climbU)
        val atTop = seg(cb, 0.78f, 1f)
        val wind = easeOut(atTop)
        for (k in 0 until 4) {
            val y = topP.y - h * (0.03f + 0.03f * k)
            val x = mix(w * 0.05f, w * 0.45f, fract(t * 0.5f + k * 0.27f))
            drawLine(ParchC8.Sepia, Offset(x, y), Offset(x + w * 0.06f, y), 0.8f * dp, StrokeCap.Round, alpha = 0.6f * wind)
        }
        val ct = h * 0.17f
        val regretTurn = easeOut(seg(rg, 0.5f, 0.8f))
        if (regretTurn > 0.5f) withTransform({ scale(-1f, 1f, pivot = foot) }) {
            figure(foot, ct, ParchC8.Ink, arms = 0.7f, lean = 0.1f)
        } else {
            figure(foot, ct, ParchC8.Ink, walk = if (climbU in 0.01f..0.99f) t * 9f else 0f, arms = -0.8f * wind, lean = -0.08f * wind)
        }
        if (wind > 0.3f && rg <= 0f) for (sd in listOf(-1f, 1f)) {      // empty hands, open
            drawCircle(ParchC8.Ink, 1.4f * dp, foot + Offset(sd * ct * 0.22f, -ct * 0.5f), alpha = 0.6f)
        }
    }
    // regret: the two books at the top, named, close
    if (rg > 0f) {
        for (k in 0 until 2) {
            val bc = topP + Offset(-w * 0.03f + w * 0.06f * k, -h * 0.03f)
            val shut = easeInOut(seg(rg, 0.15f + 0.1f * k, 0.45f + 0.1f * k))
            val half = w * 0.022f
            val col = ParchC8.Books[k + 1]
            drawRect(col, topLeft = Offset(bc.x - half, bc.y - h * 0.008f), size = Size(half, h * 0.016f))
            withTransform({ scale(1f - 2f * shut, 1f, pivot = Offset(bc.x, bc.y)) }) {
                drawRect(if (shut < 0.5f) ParchC8.Paper else col, topLeft = Offset(bc.x, bc.y - h * 0.008f), size = Size(half, h * 0.016f))
            }
            tagAt(a.tag("regret", 0, k), bc + Offset(0f, -h * 0.06f - h * 0.05f * k), seg(rg, 0.05f + 0.1f * k, 0.25f + 0.1f * k), back = ParchC8.Back, ink = Color(0xFFF2DDB0))
        }
        val spill = easeOut(seg(rg, 0.6f, 1f))
        drawRect(Brush.horizontalGradient(listOf(Color.Transparent, ParchC8.Cool.copy(alpha = 0.25f * spill)), startX = w * 0.3f, endX = w * 0.5f), topLeft = Offset(w * 0.3f, m), size = Size(w * 0.2f, h - 2 * m))
    }
}

// ═════════════ 9. field: sow, trust, harvest, winnow (tawakkul-asbab) ═════════════

private object FieldC8 {
    val SkyTop = Color(0xFF6FA3D6)
    val SkyLow = Color(0xFFDCEBF2)
    val Grey = Color(0xFF8E98A8)
    val Cloud = Color(0xFFF4F6F8)
    val RainCloud = Color(0xFF6E7888)
    val Rain = Color(0xFFB8C8DA)
    val Hill = Color(0xFF8FAF6A)
    val HillFar = Color(0xFFA9C08A)
    val Soil = Color(0xFF8A6244)
    val SoilDark = Color(0xFF5E4030)
    val Sprout = Color(0xFF6DAA4A)
    val Wheat = Color(0xFFE2B85A)
    val WheatDark = Color(0xFFB88A36)
    val Ink = Color(0xFF2E2620)
    val Heart = Color(0xFFFFE6A8)
    val Chaff = Color(0xFFF2E2B0)
    val Sun = Color(0xFFFFF3C8)
    val Back = Color(0xE6203040)
}

/**
 * A field in the hills. Beats: sow (a farmer walks the furrow throwing seed, then stops and raises his hands to the
 * sky; clouds come, rain falls, shoots come up), harvest (the field turns to wheat; he holds a sheaf in his hand
 * while his heart, a small light, hangs by a thread from the sky and not from the sheaf), winnow (he throws the
 * grain up and the wind carries the chaff away; the grain falls back into a heap).
 */
internal fun DrawScope.fieldScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sow" to Shot(Offset(w * 0.55f, h * 0.58f), 1.06f),
        "harvest" to Shot(Offset(w * 0.5f, h * 0.62f), 1.35f),
        "winnow" to Shot(Offset(w * 0.46f, h * 0.68f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { fieldLand(a) }
}

private fun DrawScope.fieldLand(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sw = a.b("sow")
    val hv = a.b("harvest")
    val wn = a.b("winnow")
    val horizon = h * 0.46f
    val clouds = easeOut(seg(sw, 0.5f, 0.72f)) * (1f - easeOut(seg(hv, 0f, 0.4f)))
    val rain = seg(sw, 0.62f, 0.7f) * (1f - seg(hv, 0f, 0.15f))
    drawRect(Brush.verticalGradient(listOf(lerpColor(FieldC8.SkyTop, FieldC8.Grey, clouds), lerpColor(FieldC8.SkyLow, FieldC8.Grey, 0.6f * clouds)), endY = horizon), size = Size(w, horizon))
    softGlow(Offset(w * 0.82f, h * 0.12f), w * 0.07f, FieldC8.Sun, 0.9f * (1f - 0.8f * clouds))
    drawCircle(FieldC8.Sun, w * 0.03f, Offset(w * 0.82f, h * 0.12f), alpha = 1f - 0.8f * clouds)
    // white clouds drift; grey ones gather to rain
    for (k in 0 until 3) {
        val c = Offset(fract(0.2f + k * 0.33f + t * 0.006f) * w * 1.2f - w * 0.1f, h * (0.09f + 0.05f * k))
        for (o in listOf(Offset(-w * 0.03f, 0f), Offset(0f, -h * 0.02f), Offset(w * 0.03f, 0f))) drawCircle(FieldC8.Cloud, w * 0.03f, c + o, alpha = 0.8f * (1f - clouds))
    }
    if (clouds > 0f) for (k in 0 until 5) {
        val c = Offset(mix(-w * 0.2f + w * 0.25f * k, w * (0.15f + 0.17f * k), clouds), h * (0.1f + 0.03f * (k % 2)))
        for (o in listOf(Offset(-w * 0.04f, 0f), Offset(0f, -h * 0.025f), Offset(w * 0.04f, 0f), Offset(0f, h * 0.012f))) drawCircle(FieldC8.RainCloud, w * 0.045f, c + o, alpha = 0.92f * clouds)
    }
    hills(FieldC8.HillFar, horizon - h * 0.04f, h * 0.03f, 0.015f, 0.04f, 0.7f)
    hills(FieldC8.Hill, horizon, h * 0.02f, 0.02f, 0.05f, 2.1f)
    // the field: furrows running to the hills
    val grow = easeOut(seg(hv, 0f, 0.45f))
    drawRect(Brush.verticalGradient(listOf(lerpColor(FieldC8.Soil, FieldC8.WheatDark, grow), lerpColor(FieldC8.SoilDark, FieldC8.WheatDark, 0.6f * grow)), startY = horizon + h * 0.02f, endY = h), topLeft = Offset(0f, horizon + h * 0.02f), size = Size(w, h - horizon))
    val vp = Offset(w * 0.5f, horizon - h * 0.1f)
    for (k in -8..8) {
        val bottom = Offset(w * 0.5f + k * w * 0.12f, h)
        val top = vp + (bottom - vp) * ((horizon + h * 0.02f - vp.y) / (h - vp.y))
        drawLine(FieldC8.SoilDark, top, bottom, 1f * dp, alpha = 0.5f * (1f - grow))
    }
    // shoots after the rain, then wheat
    val shoots = easeOut(seg(sw, 0.78f, 1f))
    if (shoots > 0f || grow > 0f) for (k in 0 until 70) {
        val y = mix(horizon + h * 0.04f, h * 0.98f, w8Rnd(k, 3.7f).let { it * it })
        val x = w8Rnd(k, 9.1f) * w
        val depth = (y - horizon) / (h - horizon)
        val s = h * 0.05f * depth * (0.4f + 0.6f * w8Rnd(k, 1.3f))
        val sway = sin(t * 1.6f + x / dp * 0.05f) * s * 0.15f
        val tallness = mix(0.35f * shoots, 1.6f, grow)
        val top = Offset(x + sway * tallness, y - s * tallness)
        drawLine(lerpColor(FieldC8.Sprout, FieldC8.WheatDark, grow), Offset(x, y), top, max(0.8f * dp, 1.4f * dp * depth), StrokeCap.Round)
        if (grow > 0.5f) drawOval(FieldC8.Wheat, topLeft = top - Offset(s * 0.08f, s * 0.25f), size = Size(s * 0.16f, s * 0.4f), alpha = seg(grow, 0.5f, 1f))
    }
    if (rain > 0f) for (k in 0 until 60) {
        val x = w8Rnd(k, 5.5f) * w
        val f = fract(w8Rnd(k, 2.2f) + t * 1.4f)
        val y = mix(h * 0.14f, h, f)
        drawLine(FieldC8.Rain, Offset(x, y), Offset(x - w * 0.006f, y + h * 0.04f), 1f * dp, alpha = 0.7f * rain)
    }

    // the farmer
    val tall = h * 0.3f
    val walkS = easeInOut(seg(sw, 0f, 0.45f))
    val foot = Offset(mix(w * 0.8f, w * 0.48f, walkS), h * 0.95f)
    val pray = easeOut(seg(sw, 0.45f, 0.58f)) * (1f - easeOut(seg(hv, 0f, 0.2f)))
    val throwArm = if (sw in 0.01f..0.45f) 0.5f + 0.5f * sin(t * 6f) else 0f
    if (wn <= 0f) {
        drawOval(Color.Black, topLeft = foot - Offset(tall * 0.2f, tall * 0.03f), size = Size(tall * 0.4f, tall * 0.06f), alpha = 0.2f)
        figure(foot, tall, FieldC8.Ink, walk = if (sw in 0.01f..0.45f) t * 5f else 0f, arms = if (pray > 0f) -0.1f else 0.3f + 0.6f * throwArm, lean = -0.05f * pray)
        if (pray > 0.01f) {        // hands raised to the sky
            for (sd in listOf(-1f, 1f)) {
                val sh = foot + Offset(0f, -tall * 0.76f)
                drawLine(FieldC8.Ink, sh, sh + Offset(sd * tall * 0.12f, -tall * 0.2f * pray), max(1.6f * dp, tall * 0.06f), StrokeCap.Round)
            }
        }
        // seed, thrown into the furrow
        if (sw in 0.01f..0.45f) for (k in 0 until 5) {
            val f = fract(t * 1.5f + k / 5f)
            val from = foot + Offset(-tall * 0.25f, -tall * 0.55f)
            drawCircle(FieldC8.WheatDark, 1.5f * dp, from + Offset(-tall * 0.5f * f, tall * 0.55f * f * f), alpha = 1f - f)
        }
    }
    // harvest: the sheaf in his hand; his heart hangs from the sky, not from the sheaf
    if (hv > 0f && wn <= 0f) {
        val hold = easeOut(seg(hv, 0.3f, 0.55f))
        val hand = foot + Offset(-tall * 0.3f, -tall * 0.5f)
        if (hold > 0f) {
            for (k in 0 until 7) drawLine(FieldC8.WheatDark, hand + Offset(0f, tall * 0.08f), hand + Offset((k - 3) * tall * 0.03f, -tall * 0.25f), 1.3f * dp, alpha = hold)
            for (k in 0 until 7) drawOval(FieldC8.Wheat, topLeft = hand + Offset((k - 3) * tall * 0.03f - tall * 0.015f, -tall * 0.3f), size = Size(tall * 0.03f, tall * 0.07f), alpha = hold)
        }
        val heartIn = easeOut(seg(hv, 0.5f, 0.72f))
        if (heartIn > 0f) {
            val hc = foot + Offset(tall * 0.05f, -tall * 0.62f)
            drawLine(FieldC8.Heart, hc + Offset(0f, -tall * 0.05f), Offset(hc.x, -h * 0.05f), 1f * dp, alpha = 0.8f * heartIn)
            softGlow(hc, tall * 0.18f, FieldC8.Heart, 0.8f * heartIn)
            drawPath(w8Heart(hc, tall * 0.07f * heartIn), Color(0xFFFFF6DC))
            tagAt(a.tag("harvest", 0, 0), hand + Offset(-tall * 0.25f, -tall * 0.35f), seg(hv, 0.6f, 0.78f), back = FieldC8.Back, ink = FieldC8.Wheat)
            tagAt(a.tag("harvest", 0, 1), hc + Offset(tall * 0.45f, -tall * 0.15f), seg(hv, 0.72f, 0.9f), back = FieldC8.Back, ink = FieldC8.Heart)
        }
    }
    // winnow: grain up, chaff away on the wind, grain back into the heap
    if (wn > 0f) {
        val f2 = Offset(w * 0.48f, h * 0.95f)
        val heap = f2 + Offset(-tall * 0.45f, 0f)
        val heapS = tall * (0.12f + 0.12f * seg(wn, 0.2f, 1f))
        drawArc(FieldC8.WheatDark, 180f, 180f, true, topLeft = heap - Offset(heapS, heapS * 0.6f), size = Size(heapS * 2, heapS * 1.2f))
        val toss = bump(fract(seg(wn, 0f, 1f) * 3f))
        figure(f2, tall, FieldC8.Ink, arms = 0.4f + 0.6f * toss, lean = 0.1f * toss)
        val fork = f2 + Offset(-tall * 0.35f, -tall * (0.45f + 0.3f * toss))
        drawLine(FieldC8.SoilDark, f2 + Offset(-tall * 0.15f, -tall * 0.35f), fork, 1.8f * dp, StrokeCap.Round)
        for (k in 0 until 14) {
            val f = fract(t * 0.9f + k / 14f)
            val grain = heap + Offset((w8Rnd(k, 3.1f) - 0.5f) * tall * 0.3f, -tall * 0.7f * bump(f))
            drawCircle(FieldC8.WheatDark, 1.5f * dp, grain, alpha = 0.9f * seg(wn, 0f, 0.1f))
            val chaff = fork + Offset(-w * 0.5f * f, -h * 0.15f * f + h * 0.03f * sin(f * 9f + k))
            drawOval(FieldC8.Chaff, topLeft = chaff, size = Size(3f * dp, 1.6f * dp), alpha = (1f - f) * 0.95f)
        }
        for (k in 0 until 5) {      // the wind
            val y = h * (0.55f + 0.06f * k)
            val x = fract(t * 0.6f + k * 0.21f) * w * 1.2f
            drawLine(Color.White, Offset(w - x, y), Offset(w - x - w * 0.1f, y), 1f * dp, StrokeCap.Round, alpha = 0.6f)
        }
    }
}

// ═════════════ 10. street: the stranger who mends (ghuraba) ═════════════

private object StreetC8 {
    val SkyTop = Color(0xFF070B1C)
    val SkyLow = Color(0xFF1C2040)
    val House = Color(0xFF2B2739)
    val HouseLight = Color(0xFF3B354D)
    val Door = Color(0xFF17141F)
    val Window = Color(0xFFFFC870)
    val Glare = Color(0xFFFF5A3A)
    val GlareSoft = Color(0xFFFFA05A)
    val Ground = Color(0xFF26223A)
    val Crowd = Color(0xFF8C82A2)
    val Stranger = Color(0xFFEDEBE4)
    val Rim = Color(0xFF9FC6FF)
    val Jar = Color(0xFFB0703E)
    val Water = Color(0xFF8FD0F0)
    val Plant = Color(0xFF5E9A5A)
    val Minaret = Color(0xFF3B3753)
    val Shutter = Color(0xFF4A3A30)
}

/**
 * A street of an old town at night: a small mosque at the left end, a glare of lights at the right. Beats: against
 * (the crowd streams to the right toward the glare; one man walks the other way, toward the mosque), mend (the
 * crowd knocks over a jar and breaks a sapling; he kneels, and the pieces come back together), lonewindow (the
 * crowd is gone; along the street the windows close one after another; his is the one still lit, in his own
 * street, a stranger there).
 */
internal fun DrawScope.streetScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "against" to Shot(Offset(w * 0.55f, h * 0.7f), 1.15f),
        "mend" to Shot(Offset(w * 0.44f, h * 0.8f), 1.5f),
        "lonewindow" to Shot(Offset(w * 0.36f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.24f) { streetNight(a) }
}

private fun DrawScope.streetNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ag = a.b("against")
    val md = a.b("mend")
    val lw = a.b("lonewindow")
    val groundY = h * 0.74f
    drawRect(Brush.verticalGradient(listOf(StreetC8.SkyTop, StreetC8.SkyLow), endY = groundY), size = Size(w, groundY))
    for (k in 0 until 30) {
        val p = Offset(w8Rnd(k, 11.7f) * w, w8Rnd(k, 33.3f) * h * 0.3f)
        drawCircle(Color.White, (0.5f + 0.8f * w8Rnd(k, 2.9f)) * dp, p, alpha = 0.3f + 0.3f * sin(t * 1.2f + k))
    }
    // the glare at the right end, where the crowd is going
    softGlow(Offset(w * 1.02f, h * 0.55f), w * 0.35f, StreetC8.Glare, 0.6f + 0.1f * sin(t * 3f))
    for (k in 0 until 9) drawCircle(if (k % 2 == 0) StreetC8.GlareSoft else StreetC8.Glare, 2f * dp, Offset(w * (0.88f + 0.015f * k), h * (0.38f + 0.02f * sin(k * 1.1f))), alpha = 0.6f + 0.4f * sin(t * 6f + k))
    // the small mosque at the left end
    val mx = w * 0.07f
    drawRect(StreetC8.Minaret, topLeft = Offset(mx - w * 0.015f, h * 0.18f), size = Size(w * 0.03f, groundY - h * 0.18f))
    drawPath(Path().apply { moveTo(mx - w * 0.018f, h * 0.18f); lineTo(mx, h * 0.12f); lineTo(mx + w * 0.018f, h * 0.18f); close() }, StreetC8.Minaret)
    drawArc(StreetC8.Minaret, 180f, 180f, true, topLeft = Offset(mx + w * 0.01f, h * 0.36f), size = Size(w * 0.1f, h * 0.1f))
    drawRect(StreetC8.Minaret, topLeft = Offset(mx + w * 0.01f, h * 0.41f), size = Size(w * 0.1f, groundY - h * 0.41f))
    softGlow(Offset(mx, h * 0.2f), w * 0.04f, StreetC8.Rim, 0.35f + 0.25f * seg(lw, 0.5f, 1f))
    // the houses, their windows lit at first and closing one by one at the end
    val houses = listOf(0.2f, 0.32f, 0.44f, 0.56f, 0.68f, 0.8f)
    val home = 1
    for ((k, x) in houses.withIndex()) {
        val hw = w * 0.11f
        val top = h * (0.3f + 0.04f * ((k * 5) % 3))
        drawRect(if (k % 2 == 0) StreetC8.House else StreetC8.HouseLight, topLeft = Offset(w * x - hw / 2, top), size = Size(hw, groundY - top))
        drawPath(horseshoe(w * x, groundY - h * 0.12f, w * 0.022f, w * 0.018f, groundY), StreetC8.Door)
        for (c in 0 until 2) {
            val wc = Offset(w * x + (c - 0.5f) * hw * 0.45f, top + h * 0.07f)
            val closeAt = 0.25f + 0.08f * ((k * 2 + c) % 7)
            val lit = if (k == home) 1f else 1f - seg(lw, closeAt, closeAt + 0.08f)
            drawRect(StreetC8.Window, topLeft = wc - Offset(w * 0.012f, h * 0.025f), size = Size(w * 0.024f, h * 0.05f), alpha = 0.15f + 0.8f * lit)
            if (lit > 0.5f) softGlow(wc, w * 0.03f, StreetC8.Window, 0.35f * lit * (if (k == home) 1f + seg(lw, 0.6f, 1f) else 1f))
            if (lit < 1f) drawRect(StreetC8.Shutter, topLeft = wc - Offset(w * 0.013f, h * 0.026f), size = Size(w * 0.026f, h * 0.052f * (1f - lit)))
        }
    }
    drawRect(StreetC8.Ground, topLeft = Offset(0f, groundY), size = Size(w, h - groundY))
    for (k in 0 until 18) drawLine(Color(0xFF34304A), Offset(w * k / 18f, groundY + h * 0.06f), Offset(w * k / 18f + w * 0.03f, groundY + h * 0.06f), 0.8f * dp)

    // the jar and the sapling the crowd knocks over
    val jarP = Offset(w * 0.44f, h * 0.86f)
    val plantP = Offset(w * 0.36f, h * 0.86f)
    val knock = easeOut(seg(md, 0.02f, 0.2f))
    val mendJar = easeInOut(seg(md, 0.4f, 0.72f))
    val mendPlant = easeInOut(seg(md, 0.6f, 0.88f))
    val broken = knock * (1f - mendJar)
    for (k in 0 until 5) {
        val ang = -PI.toFloat() * 0.5f + (k - 2) * 0.6f
        val off = Offset(cos(ang) * w * 0.04f, -sin(ang) * h * 0.02f + h * 0.03f) * broken
        withTransform({ rotate(broken * (k - 2) * 40f, jarP + off) }) {
            drawPath(Path().apply {
                val c = jarP + off
                moveTo(c.x - w * 0.012f + k * w * 0.005f, c.y - h * 0.05f + k * h * 0.012f)
                lineTo(c.x - w * 0.007f + k * w * 0.005f, c.y - h * 0.05f + k * h * 0.012f)
                lineTo(c.x - w * 0.006f + k * w * 0.005f, c.y - h * 0.035f + k * h * 0.012f)
                lineTo(c.x - w * 0.014f + k * w * 0.005f, c.y - h * 0.035f + k * h * 0.012f)
                close()
            }, StreetC8.Jar)
        }
    }
    if (broken < 0.5f) {
        drawOval(StreetC8.Jar, topLeft = jarP - Offset(w * 0.018f, h * 0.06f), size = Size(w * 0.036f, h * 0.06f))
        drawRect(StreetC8.Jar, topLeft = jarP - Offset(w * 0.007f, h * 0.075f), size = Size(w * 0.014f, h * 0.02f))
        if (mendJar > 0.9f) glint(jarP + Offset(0f, -h * 0.075f), 6f * dp, StreetC8.Water, 0.9f)
    }
    if (knock > 0f && mendJar < 1f) drawOval(StreetC8.Water, topLeft = jarP + Offset(-w * 0.04f, -h * 0.005f), size = Size(w * 0.08f * knock * (1f - mendJar), h * 0.012f), alpha = 0.6f)
    val bend = easeOut(seg(md, 0.08f, 0.25f)) * (1f - mendPlant)
    withTransform({ rotate(-70f * bend, plantP) }) {
        drawLine(StreetC8.Plant, plantP, plantP + Offset(0f, -h * 0.08f), 1.6f * dp, StrokeCap.Round)
        for (k in 0 until 3) drawOval(StreetC8.Plant, topLeft = plantP + Offset(if (k % 2 == 0) -w * 0.012f else 0f, -h * (0.04f + 0.018f * k)), size = Size(w * 0.012f, h * 0.012f))
    }
    if (mendJar > 0f && mendJar < 1f) for (k in 0 until 5) glint(jarP + Offset((k - 2) * w * 0.012f, -h * 0.04f), 5f * dp, StreetC8.Window, 0.6f * bump(mendJar))

    // the crowd streams right toward the glare
    val crowdIn = easeOut(seg(ag, 0f, 0.3f))
    val crowdOut = easeOut(seg(lw, 0f, 0.4f))
    val tall = h * 0.22f
    if (crowdIn > 0f && crowdOut < 1f) for (k in 0 until 11) {
        val speed = 0.07f + 0.012f * (k % 3)
        val x = (fract(w8Rnd(k, 3.9f) + t * speed) * 1.25f - 0.15f) * w + crowdOut * w * 0.6f
        if (x > w * 1.05f) continue
        val y = groundY + h * (0.05f + 0.07f * (k % 3))
        val s = tall * (0.82f + 0.08f * (k % 3))
        withTransform({ scale(-1f, 1f, pivot = Offset(x, y)) }) {
            figure(Offset(x, y), s, StreetC8.Crowd, alpha = crowdIn * (1f - crowdOut) * 0.85f, walk = t * 7f + k, arms = 0.15f)
        }
    }
    // the stranger, walking the other way; then he mends; then home
    if (ag > 0f) {
        val come = easeInOut(seg(ag, 0.05f, 1f))
        val toJar = easeInOut(seg(md, 0.25f, 0.4f))
        val toHome = easeInOut(seg(lw, 0.3f, 0.9f))
        val x = when {
            lw > 0f -> mix(w * 0.47f, w * 0.32f, toHome)
            md > 0f -> mix(w * 0.55f, w * 0.47f, toJar)
            else -> mix(w * 0.85f, w * 0.55f, come)
        }
        val foot = Offset(x, groundY + h * 0.16f)
        val kneel = easeOut(seg(md, 0.38f, 0.45f)) * (1f - easeOut(seg(md, 0.9f, 1f)))
        val moving = (ag in 0.06f..0.99f && md <= 0f) || (md in 0.25f..0.4f) || (lw in 0.3f..0.9f)
        softGlow(foot + Offset(0f, -tall * 0.5f), tall * 0.5f, StreetC8.Rim, 0.35f)
        figure(foot, tall * (1f - 0.25f * kneel), StreetC8.Stranger, walk = if (moving) t * 5f else 0f, lean = 0.5f * kneel, arms = 0.2f + 0.7f * kneel)
    }
}
