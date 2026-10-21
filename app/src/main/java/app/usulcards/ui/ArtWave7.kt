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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextLayoutResult
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 7, after the visual codex (tools/cards/CODEX.md): new registers so that neighbouring cards never look
 * alike — a page of calligraphy, a reading desk with a lens, a zellige panel, a desert at dawn, an old map, a
 * courtyard in the morning, a caravan at night. Companions appear only as their names in roundels; people are
 * faceless figures who stand for anyone; animals are silhouettes.
 */

private fun rnd(k: Int, salt: Float): Float = fract(sin(k * salt) * 43758.547f)

/** Point at fraction [u] of a polyline's length. */
private fun alongPoly(pts: List<Offset>, u: Float): Offset {
    var total = 0f
    for (i in 1 until pts.size) total += (pts[i] - pts[i - 1]).getDistance()
    var left = total * u.coerceIn(0f, 1f)
    for (i in 1 until pts.size) {
        val s = (pts[i] - pts[i - 1]).getDistance()
        if (s <= 0f) continue
        if (left <= s) return pts[i - 1] + (pts[i] - pts[i - 1]) * (left / s)
        left -= s
    }
    return pts.last()
}

/** A smooth curve through [ctrl] points (Catmull-Rom), sampled into a polyline. */
private fun smooth(ctrl: List<Offset>, per: Int = 10): List<Offset> {
    if (ctrl.size < 3) return ctrl
    val out = ArrayList<Offset>()
    for (i in 0 until ctrl.size - 1) {
        val p0 = ctrl[max(0, i - 1)]
        val p1 = ctrl[i]
        val p2 = ctrl[i + 1]
        val p3 = ctrl[min(ctrl.size - 1, i + 2)]
        for (k in 0 until per) {
            val t = k / per.toFloat()
            val t2 = t * t
            val t3 = t2 * t
            out.add(
                (p1 * 2f + (p2 - p0) * t + (p0 * 2f - p1 * 5f + p2 * 4f - p3) * t2 + (p1 * 3f - p0 - p2 * 3f + p3) * t3) * 0.5f,
            )
        }
    }
    out.add(ctrl.last())
    return out
}

private fun polyPathOf(pts: List<Offset>): Path = Path().apply {
    pts.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
}

/** The colours of the companions' roundels: each of the closest companions has his own (the codex). */
internal object Roundel {
    val Fills = listOf(Color(0xFF0E3B2E), Color(0xFF1B2A5C), Color(0xFF5A1E2B), Color(0xFF3D2A10))
    val Rim = Color(0xFFE7C77A)
    val Name = Color(0xFFF6E7BE)
    val PlainFill = Color(0xFF3A3F4B)
    val PlainRim = Color(0xFFB4B8C2)
}

/** A companion as his name in calligraphy inside a roundel (never a figure). [grow] 0..1 pops it in. */
internal fun DrawScope.nameRoundel(c: Offset, r: Float, name: TextLayoutResult?, fill: Color, rim: Color, grow: Float, ink: Color = Roundel.Name) {
    if (grow <= 0.01f) return
    val g = grow.coerceIn(0f, 1f)
    val rr = r * (0.6f + 0.4f * easeOut(g))
    drawCircle(Color.Black.copy(alpha = 0.22f * g), rr * 1.04f, c + Offset(0f, rr * 0.12f))
    drawCircle(fill, rr, c, alpha = g)
    drawCircle(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.16f * g), Color.Transparent), c - Offset(rr * 0.35f, rr * 0.4f), rr), rr, c)
    drawCircle(rim, rr, c, alpha = g, style = Stroke(rr * 0.1f))
    drawCircle(rim, rr * 0.8f, c, alpha = g * 0.55f, style = Stroke(rr * 0.035f))
    // small beads around the rim, as on the old medallions
    for (k in 0 until 16) {
        val ang = k * PI.toFloat() / 8f
        drawCircle(rim, rr * 0.035f, c + Offset(cos(ang) * rr * 0.9f, sin(ang) * rr * 0.9f), alpha = g * 0.8f)
    }
    if (name != null) {
        val sc = min(rr * 1.35f / max(1f, name.size.width.toFloat()), rr * 0.95f / max(1f, name.size.height.toFloat()))
        bigText(name, c, sc, ink, g)
    }
}

/** A plain roundel for the other companions: one muted colour, no name. */
private fun DrawScope.plainRoundel(c: Offset, r: Float, grow: Float) {
    if (grow <= 0.01f) return
    val g = grow.coerceIn(0f, 1f)
    val rr = r * (0.6f + 0.4f * easeOut(g))
    drawCircle(Roundel.PlainFill, rr, c, alpha = g)
    drawCircle(Roundel.PlainRim, rr, c, alpha = g * 0.9f, style = Stroke(rr * 0.12f))
    drawCircle(Roundel.PlainRim, rr * 0.45f, c, alpha = g * 0.5f, style = Stroke(rr * 0.08f))
}

/** A camel in silhouette walking to the left, feet at [foot], [s] tall (animals are silhouettes only). */
private fun DrawScope.camel(foot: Offset, s: Float, color: Color, walk: Float, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    val bx = foot.x
    val by = foot.y - s * 0.6f
    val body = Path().apply {
        moveTo(bx + s * 0.42f, by + s * 0.12f)
        cubicTo(bx + s * 0.48f, by - s * 0.14f, bx + s * 0.2f, by - s * 0.42f, bx + s * 0.02f, by - s * 0.2f)
        cubicTo(bx - s * 0.1f, by - s * 0.1f, bx - s * 0.2f, by - s * 0.08f, bx - s * 0.28f, by - s * 0.02f)
        cubicTo(bx - s * 0.4f, by - s * 0.08f, bx - s * 0.44f, by - s * 0.32f, bx - s * 0.5f, by - s * 0.38f)
        cubicTo(bx - s * 0.55f, by - s * 0.42f, bx - s * 0.63f, by - s * 0.4f, bx - s * 0.66f, by - s * 0.34f)
        lineTo(bx - s * 0.64f, by - s * 0.29f)
        lineTo(bx - s * 0.54f, by - s * 0.28f)
        cubicTo(bx - s * 0.46f, by - s * 0.1f, bx - s * 0.38f, by + s * 0.1f, bx - s * 0.26f, by + s * 0.18f)
        lineTo(bx + s * 0.34f, by + s * 0.22f)
        close()
    }
    drawPath(body, color, alpha = alpha)
    // a saddle cloth over the hump
    drawPath(Path().apply {
        moveTo(bx - s * 0.06f, by - s * 0.18f)
        lineTo(bx + s * 0.22f, by - s * 0.26f)
        lineTo(bx + s * 0.26f, by + s * 0.02f)
        lineTo(bx - s * 0.04f, by + s * 0.06f)
        close()
    }, Color(0xFF6B2E2A), alpha = alpha * 0.9f)
    val sw = s * 0.06f
    for ((k, hx) in listOf(bx - s * 0.2f, bx + s * 0.28f).withIndex()) {
        val ph = walk + k * PI.toFloat()
        val sw1 = sin(ph) * s * 0.1f
        val hip = Offset(hx, by + s * 0.16f)
        drawLine(color, hip, Offset(hx + sw1, foot.y), sw, StrokeCap.Round, alpha = alpha)
        drawLine(color, hip + Offset(s * 0.07f, 0f), Offset(hx + s * 0.07f - sw1, foot.y), sw, StrokeCap.Round, alpha = alpha)
    }
}

/** A row of handwriting from [right] to [left] at [y]: words of joined strokes with dots and tall letters; [p] writes it from the right. */
private fun DrawScope.inkRow(right: Float, left: Float, y: Float, seed: Int, color: Color, width: Float, p: Float = 1f, alpha: Float = 1f) {
    if (p <= 0f || alpha <= 0.01f) return
    val end = right - (right - left) * p.coerceIn(0f, 1f)
    val stroke = Stroke(width, cap = StrokeCap.Round, join = StrokeJoin.Round)
    var x = right
    var k = 0
    while (x > end + width) {
        val len = (right - left) * (0.08f + 0.12f * rnd(seed * 31 + k, 12.9898f))
        val x2 = max(end, x - len)
        val word = Path().apply {
            moveTo(x, y)
            var xx = x
            var j = 0
            while (xx > x2) {
                val nx = max(x2, xx - width * (2.6f + 1.6f * rnd(seed * 7 + k * 13 + j, 4.1f)))
                val up = width * (0.6f + 1.4f * rnd(seed * 5 + k * 11 + j, 9.7f))
                cubicTo(xx - (xx - nx) * 0.25f, y - up, xx - (xx - nx) * 0.75f, y - up, nx, y)
                xx = nx
                j++
            }
        }
        drawPath(word, color, alpha = alpha, style = stroke)
        // a tall letter at the word's start, a tail at its end, the letters' dots
        if (rnd(seed * 3 + k, 2.3f) > 0.45f) drawLine(color, Offset(x - width * 0.6f, y), Offset(x - width * 0.2f, y - width * 5f), width * 0.9f, StrokeCap.Round, alpha = alpha)
        if (rnd(seed * 9 + k, 6.1f) > 0.5f) drawPath(Path().apply { moveTo(x2, y); cubicTo(x2 - width * 2f, y + width * 2.5f, x2 - width * 4f, y + width * 1.5f, x2 - width * 4.5f, y - width * 0.5f) }, color, alpha = alpha, style = stroke)
        val dots = (rnd(seed * 17 + k, 78.233f) * 3f).toInt()
        for (d in 0 until dots) drawCircle(color, width * 0.6f, Offset(x - (x - x2) * (0.3f + 0.25f * d), y + (if (d % 2 == 0) -width * 2.6f else width * 2.2f)), alpha = alpha)
        x = x2 - (right - left) * 0.035f
        k++
    }
}

// ═════════════ 1. ink: a page of calligraphy (islam-sunna) ═════════════

private object InkPage {
    val Paper = Color(0xFFF1E5C8)
    val PaperLow = Color(0xFFE0CB9F)
    val Fiber = Color(0xFFC6AE80)
    val Fox = Color(0xFFB98F5A)
    val Ink = Color(0xFF231708)
    val Gold = Color(0xFFB4852B)
    val GoldLight = Color(0xFFE5BF63)
    val Lapis = Color(0xFF27407A)
    val Back = Color(0xF2F1E5C8)
}

/**
 * A calligrapher's page. Beats: inscribe (each part of its tag is written large with a reed pen, right to left),
 * lean (a gold arch rises from both words and meets at a keystone; one word fades and the other tilts, then both
 * stand again: neither stands without the other), base (a foundation of roundels: the first parts of the tag are
 * its caption, then the named companions, then plain roundels for the others).
 */
internal fun DrawScope.inkScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    drawRect(Brush.verticalGradient(listOf(InkPage.Paper, InkPage.PaperLow)))
    drawRect(Brush.radialGradient(listOf(Color(0x40FFF8E2), Color.Transparent), center = Offset(w * 0.22f, h * 0.02f), radius = w * 0.9f))
    for (k in 0 until 70) {
        val p = Offset(rnd(k, 12.9898f) * w, rnd(k, 78.233f) * h)
        val len = (4f + 12f * rnd(k, 37.719f)) * dp
        val ang = (rnd(k, 5.31f) - 0.5f) * 0.8f
        drawLine(InkPage.Fiber, p, p + Offset(cos(ang) * len, sin(ang) * len), 0.6f * dp, alpha = 0.32f)
    }
    for (k in 0 until 9) {     // foxing: the old page's brown spots
        val c = Offset(rnd(k, 91.7f) * w, rnd(k, 47.3f) * h)
        drawCircle(Brush.radialGradient(listOf(InkPage.Fox.copy(alpha = 0.16f), Color.Transparent), c, (6f + 14f * rnd(k, 3.7f)) * dp), (6f + 14f * rnd(k, 3.7f)) * dp, c)
    }
    // the illuminated frame: lapis band between two gold rules, beads, rosettes in the corners
    val m = w * 0.03f
    drawRect(InkPage.Gold, topLeft = Offset(m, m), size = Size(w - 2 * m, h - 2 * m), style = Stroke(1.6f * dp), alpha = 0.85f)
    drawRect(InkPage.Lapis, topLeft = Offset(m + 2.5f * dp, m + 2.5f * dp), size = Size(w - 2 * m - 5f * dp, h - 2 * m - 5f * dp), style = Stroke(2.2f * dp), alpha = 0.55f)
    drawRect(InkPage.Gold, topLeft = Offset(m + 5f * dp, m + 5f * dp), size = Size(w - 2 * m - 10f * dp, h - 2 * m - 10f * dp), style = Stroke(0.7f * dp), alpha = 0.7f)
    var bx = m + 12f * dp
    while (bx < w - m - 10f * dp) {
        drawCircle(InkPage.Gold, 0.8f * dp, Offset(bx, m + 2.5f * dp), alpha = 0.8f)
        drawCircle(InkPage.Gold, 0.8f * dp, Offset(bx, h - m - 2.5f * dp), alpha = 0.8f)
        bx += 7f * dp
    }
    for (c in listOf(Offset(m + 2.5f * dp, m + 2.5f * dp), Offset(w - m - 2.5f * dp, m + 2.5f * dp), Offset(m + 2.5f * dp, h - m - 2.5f * dp), Offset(w - m - 2.5f * dp, h - m - 2.5f * dp))) {
        drawPath(starPath(c, 6.5f * dp), InkPage.Gold)
        drawCircle(InkPage.Lapis, 2.2f * dp, c)
    }
    // the inkwell and a resting reed pen, top right
    val well = Offset(w * 0.88f, h * 0.17f)
    drawOval(Color.Black.copy(alpha = 0.18f), topLeft = well + Offset(-w * 0.04f, h * 0.035f), size = Size(w * 0.085f, h * 0.035f))
    drawRoundRect(Color(0xFF2B2420), topLeft = well + Offset(-w * 0.032f, -h * 0.02f), size = Size(w * 0.064f, h * 0.07f), cornerRadius = CornerRadius(5f * dp))
    drawRoundRect(Color(0xFFB58A3E), topLeft = well + Offset(-w * 0.036f, -h * 0.035f), size = Size(w * 0.072f, h * 0.02f), cornerRadius = CornerRadius(3f * dp))
    drawOval(Color(0xFF0E0A06), topLeft = well + Offset(-w * 0.022f, -h * 0.033f), size = Size(w * 0.044f, h * 0.012f))
    drawLine(Color(0xFF9C7A45), well + Offset(-w * 0.005f, -h * 0.03f), well + Offset(-w * 0.07f, -h * 0.13f), 2.4f * dp, StrokeCap.Round)

    val wi = a.nth("inscribe")
    val n = if (wi >= 0) max(1, a.parts(wi)) else 0
    val wordY = h * 0.47f
    val wb = a.b("inscribe")
    val lb = a.b("lean")
    val test = bump(seg(lb, 0.62f, 0.97f))
    val tilt = 8f * test
    val fade = 1f - 0.8f * test
    val settle = easeOut(seg(a.b("base"), 0.8f, 1f))
    data class Word(val c: Offset, val tw: Float, val th: Float, val sc: Float, val tl: TextLayoutResult)
    val words = ArrayList<Word>()
    for (k in 0 until n) {
        val tl = a.big("inscribe", 0, k) ?: continue
        val sc = min(1.5f, w * 0.3f / max(1f, tl.size.width.toFloat()))
        val cx = if (n == 1) w * 0.5f else mix(w * 0.72f, w * 0.28f, k / (n - 1f))
        words.add(Word(Offset(cx, wordY), tl.size.width * sc, tl.size.height * sc, sc, tl))
    }
    fun pivotOf(k: Int): Offset = words[k].let { Offset(it.c.x + it.tw / 2f, it.c.y + it.th * 0.4f) }
    // the guide lines a calligrapher rules before writing
    for (wd in words) {
        drawLine(InkPage.Gold, Offset(wd.c.x - wd.tw * 0.6f, wd.c.y + wd.th * 0.28f), Offset(wd.c.x + wd.tw * 0.6f, wd.c.y + wd.th * 0.28f), 0.6f * dp, alpha = 0.35f)
    }
    // the arch the two words hold up together
    val q = easeInOut(seg(lb, 0f, 0.55f))
    val apex = Offset(w * 0.5f, h * 0.11f)
    if (q > 0f && words.size == 2) {
        for (k in 0 until 2) {
            val wd = words[k]
            val side = if (k == 0) 1f else -1f
            val foot = Offset(wd.c.x, wd.c.y - wd.th * 0.55f)
            val path = cubicPart(foot, foot + Offset(0f, -h * 0.17f), apex + Offset(side * w * 0.07f, h * 0.1f), apex, 0f, q)
            val inner = cubicPart(foot + Offset(-side * w * 0.025f, 0f), foot + Offset(-side * w * 0.025f, -h * 0.15f), apex + Offset(side * w * 0.05f, h * 0.13f), apex + Offset(0f, h * 0.045f), 0f, q)
            val alpha = if (k == 1) fade else 1f
            withTransform({ rotate(if (k == 0) tilt else 0f, pivotOf(k)) }) {
                drawPath(path, InkPage.Gold, alpha = 0.25f * alpha, style = Stroke(7f * dp, cap = StrokeCap.Round))
                drawPath(path, InkPage.Lapis, alpha = 0.5f * alpha, style = Stroke(3f * dp, cap = StrokeCap.Round))
                drawPath(path, InkPage.GoldLight, alpha = alpha, style = Stroke(1.4f * dp, cap = StrokeCap.Round))
                drawPath(inner, InkPage.Gold, alpha = 0.7f * alpha, style = Stroke(0.9f * dp, cap = StrokeCap.Round))
            }
        }
        val key = easeOut(seg(lb, 0.48f, 0.62f))
        if (key > 0f) {
            softGlow(apex, w * 0.06f, InkPage.GoldLight, 0.5f * key)
            drawPath(starPath(apex, w * 0.028f * key), InkPage.Gold)
            drawCircle(InkPage.Lapis, w * 0.009f * key, apex)
        }
        tagAt(a.tag("lean"), Offset(w * 0.5f, h * 0.27f), seg(lb, 0.3f, 1f), back = InkPage.Back, ink = InkPage.Ink)
    }
    // the words, written by the reed pen from the right
    for ((k, wd) in words.withIndex()) {
        val p = seg(wb, k / n.toFloat(), (k + 1) / n.toFloat())
        if (p <= 0f) continue
        if (settle > 0f) softGlow(wd.c, wd.tw * 0.75f, InkPage.GoldLight, 0.35f * settle)
        val right = wd.c.x + wd.tw / 2f + 3f * dp
        val left = wd.c.x - wd.tw / 2f - 3f * dp
        val edge = right - (right - left) * easeInOut(p)
        val rot = if (words.size == 2 && k == 0) tilt else 0f
        val alpha = if (words.size == 2 && k == 1) fade else 1f
        withTransform({ rotate(rot, pivotOf(k)) }) {
            clipRect(left = edge, top = wd.c.y - wd.th, right = right + 2f * dp, bottom = wd.c.y + wd.th) {
                bigText(wd.tl, wd.c + Offset(0.8f * dp, 1f * dp), wd.sc, InkPage.Fiber, 0.5f * alpha)
                bigText(wd.tl, wd.c, wd.sc, InkPage.Ink, alpha)
            }
        }
        if (p < 1f) {
            val nib = Offset(edge, wd.c.y + wd.th * 0.12f * sin(t * 13f + k))
            drawLine(Color(0xFF9C7A45), nib + Offset(1.5f * dp, -2f * dp), nib + Offset(w * 0.07f, -h * 0.2f), 3f * dp, StrokeCap.Round)
            drawPath(Path().apply {
                moveTo(nib.x, nib.y)
                lineTo(nib.x + 4f * dp, nib.y - 9f * dp)
                lineTo(nib.x + 7.5f * dp, nib.y - 7.5f * dp)
                close()
            }, Color(0xFF4A3214))
            drawCircle(InkPage.Ink, 1.4f * dp, nib)
        }
    }
    if (words.size == 2) {
        val sep = easeOut(seg(wb, 0.92f, 1f))
        if (sep > 0f) {
            drawPath(starPath(Offset(w * 0.5f, wordY), w * 0.02f * sep), InkPage.Gold)
            drawCircle(InkPage.Lapis, w * 0.006f * sep, Offset(w * 0.5f, wordY))
        }
    }
    // the foundation: the companions, the closest by name, the others in plain roundels
    val bb = a.b("base")
    if (bb > 0f) {
        val bi = a.nth("base")
        val names = max(0, a.parts(bi) - 1)
        val y = h * 0.75f
        val line = easeOut(seg(bb, 0f, 0.25f))
        drawLine(InkPage.Gold, Offset(w * 0.5f - w * 0.42f * line, y), Offset(w * 0.5f + w * 0.42f * line, y), 1.4f * dp, alpha = 0.85f)
        drawLine(InkPage.Lapis, Offset(w * 0.5f - w * 0.42f * line, y + 3f * dp), Offset(w * 0.5f + w * 0.42f * line, y + 3f * dp), 0.8f * dp, alpha = 0.5f)
        val r = w * 0.058f
        val gap = w * 0.135f
        for (i in 0 until names) {
            val x = w * 0.5f + gap * ((names - 1) / 2f - i)
            nameRoundel(Offset(x, y), r, a.big("base", 0, i + 1), Roundel.Fills[i % Roundel.Fills.size], Roundel.Rim, seg(bb, 0.18f + i * 0.09f, 0.42f + i * 0.09f))
        }
        val outer = gap * (names - 1) / 2f + r
        for (s in 0 until 2) for (side in intArrayOf(1, -1)) {
            val x = w * 0.5f + side * (outer + w * 0.045f + s * w * 0.065f)
            plainRoundel(Offset(x, y), w * 0.025f, seg(bb, 0.55f + s * 0.1f, 0.75f + s * 0.1f))
        }
        tagAt(a.tag("base"), Offset(w * 0.5f, h * 0.9f), seg(bb, 0.45f, 1f), back = InkPage.Back, ink = InkPage.Ink)
    }
}

// ═════════════ 2. lens: the reading desk (sunna-tufassir) ═════════════

private object Desk {
    val Top = Color(0xFF1A3A2C)
    val Low = Color(0xFF0B1913)
    val Tool = Color(0xFFC09848)
    val Page = Color(0xFFF3E8CE)
    val PageEdge = Color(0xFFCDB78C)
    val Ink = Color(0xFF2E2213)
    val Red = Color(0xFFA2321F)
    val Gold = Color(0xFFD7A443)
    val Wood = Color(0xFF6A4325)
    val WoodDark = Color(0xFF3E2614)
    val Brass = Color(0xFFCFA04A)
    val Back = Color(0xF2F3E8CE)
}

/** An open book: two pages around [c], [bw] wide; [frame] draws the gilt borders of a mushaf. */
private fun DrawScope.openPages(c: Offset, bw: Float, bh: Float, frame: Boolean, alpha: Float = 1f) {
    val dp = density
    drawRoundRect(Color.Black.copy(alpha = 0.3f * alpha), topLeft = Offset(c.x - bw / 2f + 3f * dp, c.y - bh / 2f + 5f * dp), size = Size(bw, bh), cornerRadius = CornerRadius(4f * dp))
    for (side in intArrayOf(-1, 1)) {
        val pg = Path().apply {
            moveTo(c.x, c.y - bh / 2f + 3f * dp)
            cubicTo(c.x + side * bw * 0.2f, c.y - bh / 2f - 3f * dp, c.x + side * bw * 0.35f, c.y - bh / 2f, c.x + side * bw / 2f, c.y - bh / 2f + 1f * dp)
            lineTo(c.x + side * bw / 2f, c.y + bh / 2f)
            cubicTo(c.x + side * bw * 0.35f, c.y + bh / 2f - 1f * dp, c.x + side * bw * 0.2f, c.y + bh / 2f - 3f * dp, c.x, c.y + bh / 2f + 2f * dp)
            close()
        }
        drawPath(pg, Desk.Page, alpha = alpha)
        drawPath(pg, Brush.horizontalGradient(listOf(Color(0x33000000), Color.Transparent), startX = c.x, endX = c.x + side * bw * 0.12f), alpha = alpha)
        drawPath(pg, Desk.PageEdge, alpha = alpha, style = Stroke(0.9f * dp))
        if (frame) {
            val l = if (side > 0) c.x + bw * 0.06f else c.x - bw * 0.46f
            drawRect(Desk.Gold, topLeft = Offset(l, c.y - bh * 0.4f), size = Size(bw * 0.4f, bh * 0.8f), alpha = alpha * 0.9f, style = Stroke(1.6f * dp))
            drawRect(Desk.Red, topLeft = Offset(l + 2.5f * dp, c.y - bh * 0.4f + 2.5f * dp), size = Size(bw * 0.4f - 5f * dp, bh * 0.8f - 5f * dp), alpha = alpha * 0.4f, style = Stroke(0.7f * dp))
        }
    }
    drawLine(Desk.PageEdge, Offset(c.x, c.y - bh / 2f + 3f * dp), Offset(c.x, c.y + bh / 2f + 2f * dp), 1.2f * dp, alpha = alpha)
}

/**
 * A reading desk at night under a lamp: an open mushaf on its folding stand, and below it a book of hadith.
 * Beats: sheet (the book of hadith slides into view), match (a line in each lights up and a gold thread joins
 * them: the Sunna says what the Qur'an says), lens (a magnifier passes over a passage that is blurred and shows it
 * clearly, then points to the hadith that explains it), add (a new line is written in the hadith book alone).
 * The first extra title names the mushaf.
 */
internal fun DrawScope.lensScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    drawRect(Brush.verticalGradient(listOf(Desk.Top, Desk.Low)))
    // the leather's tooled border and its grain
    drawRect(Desk.Tool, topLeft = Offset(w * 0.025f, h * 0.035f), size = Size(w * 0.95f, h * 0.93f), alpha = 0.45f, style = Stroke(1f * dp))
    drawRect(Desk.Tool, topLeft = Offset(w * 0.035f, h * 0.05f), size = Size(w * 0.93f, h * 0.9f), alpha = 0.25f, style = Stroke(0.6f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f * dp, 3f * dp))))
    for (k in 0 until 40) {
        val p = Offset(rnd(k, 12.9898f) * w, rnd(k, 78.233f) * h)
        drawCircle(Color.Black, (0.6f + rnd(k, 3.3f)) * dp, p, alpha = 0.12f)
    }
    // the lamp's warm pool from the upper right
    drawRect(Brush.radialGradient(listOf(Color(0x55FFD9A0), Color(0x14FFD9A0), Color.Transparent), center = Offset(w * 0.82f, h * 0.02f), radius = w * 0.85f))

    // the rahl: the folding wooden stand of the mushaf
    val mc = Offset(w * 0.5f, h * 0.3f)
    val bw = w * 0.6f
    val bh = h * 0.32f
    for (side in intArrayOf(-1, 1)) {
        drawLine(Desk.WoodDark, Offset(mc.x + side * bw * 0.36f, mc.y + bh * 0.42f), Offset(mc.x - side * bw * 0.12f, mc.y + bh * 0.75f), 5f * dp, StrokeCap.Round)
        drawLine(Desk.Wood, Offset(mc.x + side * bw * 0.36f, mc.y + bh * 0.42f), Offset(mc.x - side * bw * 0.12f, mc.y + bh * 0.75f), 3f * dp, StrokeCap.Round)
    }
    openPages(mc, bw, bh, frame = true)
    // the mushaf's lines; on the left page a passage left blurred (a general wording the Sunna explains)
    for (k in 0 until 5) {
        val y = mc.y - bh * 0.28f + k * bh * 0.14f
        inkRow(mc.x + bw * 0.43f, mc.x + bw * 0.09f, y, 10 + k, Desk.Ink, 1.3f * dp)
        if (k == 2) {
            drawLine(Desk.Ink, Offset(mc.x - bw * 0.09f, y), Offset(mc.x - bw * 0.43f, y), 7f * dp, StrokeCap.Round, alpha = 0.12f)
            drawLine(Desk.Ink, Offset(mc.x - bw * 0.11f, y), Offset(mc.x - bw * 0.41f, y), 3.5f * dp, StrokeCap.Round, alpha = 0.16f)
        } else {
            inkRow(mc.x - bw * 0.09f, mc.x - bw * 0.43f, y, 20 + k, Desk.Ink, 1.3f * dp)
        }
    }
    // a small gilt rosette at the head of the right page
    drawPath(starPath(Offset(mc.x + bw * 0.26f, mc.y - bh * 0.44f), 3.5f * dp), Desk.Gold)
    tagAt(a.extras.getOrNull(0), Offset(mc.x, h * 0.075f), 1f, back = Color(0xE60B1913), ink = Desk.Gold)

    // the book of hadith slides up onto the desk
    val sbRaw = a.b("sheet")
    val sb = easeOut(sbRaw)
    val hc = Offset(w * 0.5f, h * 0.73f + (1f - sb) * h * 0.5f)
    val hw = w * 0.7f
    val hh = h * 0.34f
    val rows = 4
    fun hrow(side: Int, k: Int): Float = hc.y - hh * 0.3f + k * hh * 0.17f
    if (sb > 0f) {
        openPages(hc, hw, hh, frame = false)
        // a ribbon marker
        drawLine(Desk.Red, Offset(hc.x + hw * 0.1f, hc.y - hh / 2f), Offset(hc.x + hw * 0.13f, hc.y + hh * 0.62f), 2.4f * dp, StrokeCap.Square)
        for (side in intArrayOf(1, -1)) for (k in 0 until rows) {
            if (side == 1 && k == rows - 1) continue       // the last line of the right page is written by `add`
            val y = hrow(side, k)
            val r0 = if (side > 0) hc.x + hw * 0.44f else hc.x - hw * 0.06f
            val l0 = if (side > 0) hc.x + hw * 0.06f else hc.x - hw * 0.44f
            // each narration opens with a word in red ink
            drawLine(Desk.Red, Offset(r0, y), Offset(r0 - hw * 0.06f, y), 1.5f * dp, StrokeCap.Round)
            inkRow(r0 - hw * 0.08f, l0, y, 40 + k * 3 + side, Desk.Ink, 1.2f * dp)
        }
        tagAt(a.tag("sheet"), Offset(hc.x + hw * 0.22f, hc.y - hh * 0.62f), seg(sbRaw, 0.3f, 1f), back = Desk.Back, ink = Desk.Ink)
    }

    // match: the same ruling in both, joined by a gold thread
    val mb = a.b("match")
    if (mb > 0f && sb > 0.5f) {
        val e = easeOut(mb)
        val y1 = mc.y - bh * 0.28f
        val y2 = hrow(1, 0)
        drawRect(Desk.Gold, topLeft = Offset(mc.x + bw * 0.08f, y1 - 4f * dp), size = Size(bw * 0.36f, 8f * dp), alpha = 0.3f * e)
        drawRect(Desk.Gold, topLeft = Offset(hc.x + hw * 0.05f, y2 - 4f * dp), size = Size(hw * 0.4f, 8f * dp), alpha = 0.3f * e)
        val from = Offset(mc.x + bw * 0.45f, y1)
        val to = Offset(hc.x + hw * 0.46f, y2)
        val thread = cubicPart(from, from + Offset(w * 0.1f, 0f), to + Offset(w * 0.1f, 0f), to, 0f, easeInOut(seg(mb, 0.15f, 0.8f)))
        drawPath(thread, Desk.Gold, alpha = 0.3f, style = Stroke(5f * dp, cap = StrokeCap.Round))
        drawPath(thread, Desk.Gold, style = Stroke(1.4f * dp, cap = StrokeCap.Round))
        tagAt(a.tag("match"), Offset(w * 0.86f, (y1 + y2) / 2f), seg(mb, 0.5f, 1f), back = Desk.Back, ink = Desk.Ink)
    }

    // lens: the magnifier shows the blurred passage clearly, and the hadith it comes from
    val lb = a.b("lens")
    if (lb > 0f) {
        val move = easeInOut(seg(lb, 0f, 0.5f))
        val target = Offset(mc.x - bw * 0.26f, mc.y - bh * 0.28f + 2 * bh * 0.14f)
        val c = Offset(mix(w * 1.12f, target.x, move), mix(h * 0.8f, target.y, move))
        val r = w * 0.085f
        val hand = Offset(0.62f, 0.78f)
        drawLine(Desk.WoodDark, c + hand * (r * 1.05f), c + hand * (r * 2.5f), 7f * dp, StrokeCap.Round)
        drawLine(Desk.Wood, c + hand * (r * 1.05f), c + hand * (r * 2.5f), 4.5f * dp, StrokeCap.Round)
        val disc = Path().apply { addOval(Rect(c.x - r, c.y - r, c.x + r, c.y + r)) }
        clipPath(disc) {
            drawRect(Desk.Page, topLeft = Offset(c.x - r, c.y - r), size = Size(2 * r, 2 * r))
            for (k in -1..1) inkRow(c.x + r * 0.95f, c.x - r * 0.95f, c.y + k * r * 0.5f, 70 + k, Desk.Ink, 2.2f * dp)
            drawRect(Brush.radialGradient(listOf(Color(0x22FFFFFF), Color(0x10A8D8FF)), c, r), topLeft = Offset(c.x - r, c.y - r), size = Size(2 * r, 2 * r))
        }
        drawCircle(Color.White, r * 0.28f, c - Offset(r * 0.4f, r * 0.42f), alpha = 0.18f)
        drawCircle(Desk.Brass, r, c, style = Stroke(3.2f * dp))
        drawCircle(Color(0xFF7A5A22), r + 1.6f * dp, c, style = Stroke(0.8f * dp))
        val link = easeOut(seg(lb, 0.55f, 0.95f))
        if (link > 0f && sb > 0.5f) {
            val to = Offset(hc.x - hw * 0.25f, hrow(-1, 1))
            val from = c + Offset(0f, r)
            drawLine(Desk.Gold, from, from + (to - from) * link, 1.4f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp), t * 12f * dp))
            if (link >= 1f) drawRect(Desk.Gold, topLeft = Offset(hc.x - hw * 0.44f, to.y - 4f * dp), size = Size(hw * 0.38f, 8f * dp), alpha = 0.28f)
        }
        tagAt(a.tag("lens"), Offset(c.x - r * 0.2f, c.y + r * 1.55f), seg(lb, 0.45f, 1f), back = Desk.Back, ink = Desk.Ink)
    }

    // add: a ruling written in the hadith book alone
    val ab = a.b("add")
    if (ab > 0f && sb > 0.5f) {
        val y = hrow(1, rows - 1)
        val r0 = hc.x + hw * 0.44f
        drawPath(starPath(Offset(r0 + 1f * dp, y), 4f * dp * easeOut(seg(ab, 0f, 0.3f))), Desk.Gold)
        inkRow(r0 - hw * 0.03f, hc.x + hw * 0.06f, y, 99, Desk.Ink, 1.3f * dp, p = easeInOut(seg(ab, 0.15f, 0.85f)))
        softGlow(Offset(hc.x + hw * 0.25f, y), hw * 0.22f, Desk.Gold, 0.3f * bump(seg(ab, 0.5f, 1f)) + 0.12f * seg(ab, 0.8f, 1f))
        tagAt(a.tag("add"), Offset(hc.x + hw * 0.25f, h * 0.96f), seg(ab, 0.4f, 1f), back = Desk.Back, ink = Desk.Ink)
    }
}

// ═════════════ 3. zellige: a tile panel in a courtyard (la-qiyas) ═════════════

private object Tile {
    val Wall = Color(0xFFEADCC2)
    val WallLow = Color(0xFFD7C4A0)
    val Stucco = Color(0xFFDCCBAA)
    val StuccoShade = Color(0xFFBFA77E)
    val Grout = Color(0xFF123A3E)
    val Gold = Color(0xFFD9A445)
    val Green = Color(0xFF2C7A5C)
    val White = Color(0xFFEFE8D8)
    val Blue = Color(0xFF2C4E8C)
    val Clay = Color(0xFFB4532C)
    val Frame = Color(0xFF6B4527)
    val FloorA = Color(0xFFB5643C)
    val FloorB = Color(0xFFE9DFCB)
    val Pencil = Color(0xFF6E6252)
    val Iron = Color(0xFF4A4A50)
    val Bad = Color(0xFFB4492A)
    val Good = Color(0xFF2E7D57)
    val Back = Color(0xF2EADCC2)
    val Ink = Color(0xFF2A2116)
}

/** A polygon piece of [sides] around [c]. */
private fun polyPiece(c: Offset, r: Float, sides: Int, turn: Float = 0f): Path = Path().apply {
    for (v in 0 until sides) {
        val ang = v * 2f * PI.toFloat() / sides + turn
        val q = c + Offset(cos(ang), sin(ang)) * r
        if (v == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y)
    }
    close()
}

/**
 * A zellige panel on a courtyard wall in the sun, its centre piece missing; a tiled floor, the craftsman's tools.
 * Beats: liken ×n (on the right, a piece from another pattern is held up as "like it" and crossed out), fit (the
 * true piece drops in and the whole pattern lights), freehand (under the panel, a piece cut by guesswork is pushed
 * into a new cell: it does not fit, crossed out), extend (the panel's own lines run on into a new cell and a
 * piece cut along them fits: ticked).
 */
internal fun DrawScope.zelligeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    drawRect(Brush.verticalGradient(listOf(Tile.Wall, Tile.WallLow)))
    drawRect(Brush.linearGradient(listOf(Color(0x46FFF6DA), Color.Transparent), start = Offset(0f, 0f), end = Offset(w * 0.8f, h)))
    // the carved stucco band at the top: a row of small arches
    val band = h * 0.07f
    drawRect(Tile.Stucco, size = Size(w, band))
    var x0 = w * 0.01f
    while (x0 < w) {
        drawPath(horseshoe(x0 + w * 0.022f, band * 0.45f, w * 0.016f, w * 0.012f, band * 0.92f), Tile.StuccoShade)
        x0 += w * 0.045f
    }
    drawLine(Tile.StuccoShade, Offset(0f, band), Offset(w, band), 1.2f * dp)
    // the tiled floor, seen at a slant
    val floorY = h * 0.84f
    drawRect(Tile.FloorB, topLeft = Offset(0f, floorY), size = Size(w, h - floorY))
    for (row in 0 until 3) {
        val y0 = floorY + (h - floorY) * row / 3f
        val y1 = floorY + (h - floorY) * (row + 1) / 3f
        val n = 10 + row * 2
        for (k in 0 until n) {
            if ((k + row) % 2 != 0) continue
            val xa0 = w * k / n.toFloat()
            val xa1 = w * (k + 1) / n.toFloat()
            drawPath(Path().apply { moveTo(xa0, y0); lineTo(xa1, y0); lineTo(xa1, y1); lineTo(xa0, y1); close() }, Tile.FloorA, alpha = 0.85f)
        }
    }
    drawRect(Brush.verticalGradient(listOf(Color(0x33000000), Color.Transparent), startY = floorY, endY = floorY + h * 0.05f), topLeft = Offset(0f, floorY), size = Size(w, h * 0.05f))

    val cols = 5
    val rowsN = 3
    val s = w * 0.11f
    val left = w * 0.05f
    val top = h * 0.11f
    val pw = s * cols
    val ph = s * rowsN
    fun cell(i: Int, j: Int) = Offset(left + s * (i + 0.5f), top + s * (j + 0.5f))
    val slotI = 2
    val slotJ = 1
    val slot = cell(slotI, slotJ)
    val fitRaw = a.b("fit")
    val lit = easeOut(seg(fitRaw, 0.55f, 1f))
    drawRect(Color.Black.copy(alpha = 0.22f), topLeft = Offset(left - 2f * dp, top + 2f * dp), size = Size(pw + 12f * dp, ph + 12f * dp))
    drawRect(Tile.Frame, topLeft = Offset(left - 6f * dp, top - 6f * dp), size = Size(pw + 12f * dp, ph + 12f * dp))
    drawRect(Tile.Gold, topLeft = Offset(left - 2.5f * dp, top - 2.5f * dp), size = Size(pw + 5f * dp, ph + 5f * dp), style = Stroke(0.8f * dp), alpha = 0.8f)
    drawRect(Tile.Grout, topLeft = Offset(left, top), size = Size(pw, ph))
    val colors = listOf(Tile.Gold, Tile.Green, Tile.White, Tile.Blue)
    for (i in 0 until cols) for (j in 0 until rowsN) {
        if (i == slotI && j == slotJ) continue
        val c = cell(i, j)
        val col = colors[(i + 2 * j) % colors.size]
        drawPath(starPath(c, s * 0.46f), col)
        drawPath(starPath(c, s * 0.22f), if (col == Tile.White) Tile.Clay else Tile.White, alpha = 0.85f)
        drawPath(starPath(c, s * 0.46f), Color.White, alpha = 0.18f + 0.5f * lit * (0.5f + 0.5f * sin(t * 3f + i + j)), style = Stroke(0.9f * dp))
    }
    for (i in 1 until cols) for (j in 1 until rowsN) {
        drawPath(polyPiece(Offset(left + s * i, top + s * j), s * 0.17f, 4, PI.toFloat() / 4f), Tile.Clay)
    }
    clipRect(left, top, left + pw, top + ph) {
        val sx = left + fract(t * 0.07f) * pw * 2f - pw * 0.5f
        drawRect(Brush.linearGradient(listOf(Color.Transparent, Color(0x30FFFFFF), Color.Transparent), start = Offset(sx - s, top), end = Offset(sx + s, top + ph)), topLeft = Offset(left, top), size = Size(pw, ph))
    }
    val drop = easeOut(seg(fitRaw, 0f, 0.55f))
    if (drop < 1f) {
        drawPath(starPath(slot, s * 0.46f), Tile.White, alpha = 0.7f, style = Stroke(1.2f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
    }
    if (fitRaw > 0f) {
        val p = slot + Offset(0f, -(1f - drop) * h * 0.4f)
        val sc = 1f + 0.18f * (1f - drop)
        if (drop < 1f) drawPath(starPath(p + Offset(3f * dp, 5f * dp), s * 0.46f * sc), Color.Black, alpha = 0.25f)
        drawPath(starPath(p, s * 0.46f * sc), Tile.Gold)
        drawPath(starPath(p, s * 0.3f * sc), Tile.Green)
        drawPath(starPath(p, s * 0.14f * sc), Tile.White)
        if (lit > 0f) {
            softGlow(slot, s * 1.6f, Color(0xFFFFE6A8), 0.5f * lit)
            drawCircle(Tile.Gold, s * (0.5f + 1.6f * seg(fitRaw, 0.55f, 1f)), slot, alpha = 0.6f * (1f - seg(fitRaw, 0.55f, 1f)), style = Stroke(1.5f * dp))
        }
        tagAt(a.tag("fit"), Offset(left + pw / 2f, band * 0.5f), seg(fitRaw, 0.5f, 1f), back = Tile.Back, ink = Tile.Ink)
    }
    // liken: on the right, pieces from other patterns held up as "like it", and crossed out
    for (k in 0 until a.count("liken")) {
        val lb = a.b("liken", k)
        if (lb <= 0f) continue
        val home = Offset(w * 0.82f, h * (0.22f + 0.27f * k))
        val inn = easeOut(seg(lb, 0f, 0.35f))
        val c = home + Offset((1f - inn) * w * 0.22f, 0f)
        val fadeOut = 1f - 0.5f * seg(lb, 0.75f, 1f)
        val piece = if (k % 2 == 0) polyPiece(c, s * 0.42f, 6) else polyPiece(c, s * 0.4f, 5, -PI.toFloat() / 2f)
        drawPath(piece, Color.Black, alpha = 0.18f * fadeOut)
        withTransform({ translate(-2f * dp, -2f * dp) }) {
            drawPath(piece, if (k % 2 == 0) Tile.Blue else Tile.Clay, alpha = fadeOut)
            drawPath(piece, Tile.White, alpha = 0.6f * fadeOut, style = Stroke(1f * dp))
        }
        val cmp = seg(lb, 0.25f, 0.5f)
        if (cmp > 0f) {
            val to = Offset(left + pw + 8f * dp, slot.y)
            drawLine(Tile.Pencil, c + (to - c) * 0.25f, c + (to - c) * (0.25f + 0.65f * cmp), 1f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp)), alpha = 0.8f * fadeOut)
        }
        val x = easeOut(seg(lb, 0.5f, 0.68f))
        if (x > 0f) crossMark(c + Offset(-2f * dp, -2f * dp), s * 0.38f * x, Tile.Bad, 3f * dp)
        tagAt(a.tag("liken", k), home + Offset(0f, s * 0.72f), seg(lb, 0.35f, 1f), back = Tile.Back, ink = Tile.Ink)
    }
    // under the panel: two new cells where the pattern would go on
    val newY = top + ph + s * 0.62f
    val freeC = Offset(left + s * 4.5f, newY)
    val extC = Offset(left + s * 0.5f, newY)
    if (a.b("freehand") > 0f || a.b("extend") > 0f) {
        for (c in listOf(freeC, extC)) {
            drawRect(Tile.Grout, topLeft = Offset(c.x - s / 2f, c.y - s / 2f), size = Size(s, s), alpha = 0.9f)
            drawRect(Tile.Frame, topLeft = Offset(c.x - s / 2f, c.y - s / 2f), size = Size(s, s), alpha = 0.7f, style = Stroke(1.4f * dp))
        }
    }
    val fb = a.b("freehand")
    if (fb > 0f) {
        val inn = easeOut(seg(fb, 0f, 0.4f))
        val shake = sin(t * 38f) * 2.2f * dp * bump(seg(fb, 0.4f, 0.7f))
        val c = freeC + Offset(shake + (1f - inn) * w * 0.25f, -s * 0.12f * (1f - inn))
        val blob = Path().apply {
            for (v in 0..24) {
                val ang = v * 2f * PI.toFloat() / 24f
                val rr = s * (0.44f + 0.11f * sin(ang * 3f + 1.3f) + 0.06f * sin(ang * 5f))
                val q = c + Offset(cos(ang), sin(ang)) * rr
                if (v == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y)
            }
            close()
        }
        val grey = seg(fb, 0.55f, 0.8f)
        drawPath(blob, lerp(Tile.Green, Color(0xFF8C8A82), grey))
        drawPath(blob, Tile.Bad, alpha = 0.8f * grey, style = Stroke(1.4f * dp))
        val x = easeOut(seg(fb, 0.7f, 0.85f))
        if (x > 0f) crossMark(c, s * 0.34f * x, Tile.Bad, 3f * dp)
        tagAt(a.tag("freehand"), Offset(freeC.x + s * 0.2f, h * 0.92f), seg(fb, 0.4f, 1f), back = Tile.Back, ink = Tile.Ink)
    }
    val eb = a.b("extend")
    if (eb > 0f) {
        val guide = seg(eb, 0f, 0.45f)
        val dash = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 2.5f * dp))
        for (dx in listOf(-0.5f, 0f, 0.5f)) {
            val from = Offset(extC.x + dx * s, top + ph)
            drawLine(Tile.Pencil, from, from + Offset(0f, (newY + s / 2f - from.y) * guide), 0.9f * dp, StrokeCap.Round, dash)
        }
        drawPath(starPath(extC, s * 0.46f), Tile.Pencil, alpha = guide, style = Stroke(1f * dp, pathEffect = dash))
        val fill = easeOut(seg(eb, 0.45f, 0.8f))
        if (fill > 0f) {
            drawPath(starPath(extC, s * 0.46f), Tile.Green, alpha = fill)
            drawPath(starPath(extC, s * 0.22f), Tile.White, alpha = fill)
            softGlow(extC, s, Color(0xFFFFE6A8), 0.35f * fill)
        }
        val tick = easeOut(seg(eb, 0.8f, 0.95f))
        if (tick > 0f) checkMark(extC + Offset(s * 0.7f, -s * 0.25f), s * 0.2f * tick, Tile.Good, 2.8f * dp)
        tagAt(a.tag("extend"), Offset(extC.x + s * 0.3f, h * 0.92f), seg(eb, 0.4f, 1f), back = Tile.Back, ink = Tile.Ink)
    }
    // the craftsman's hammer and a few cut pieces, by the wall on the right
    val tool = Offset(w * 0.86f, floorY - 2f * dp)
    drawLine(Color(0xFF8A6238), tool, tool + Offset(-w * 0.07f, -h * 0.02f), 2.6f * dp, StrokeCap.Round)
    drawRoundRect(Tile.Iron, topLeft = tool + Offset(-w * 0.085f, -h * 0.045f), size = Size(w * 0.02f, h * 0.05f), cornerRadius = CornerRadius(1.5f * dp))
    for (k in 0 until 3) drawPath(polyPiece(tool + Offset(w * (0.03f + 0.025f * k), -3f * dp), s * 0.1f, 4, k * 0.5f), colors[k])
}

// ═════════════ 4. canyon: a desert at dawn (bida-sighar) ═════════════

private object Dune {
    val SkyTop = Color(0xFF283969)
    val SkyMid = Color(0xFFB9707A)
    val SkyLow = Color(0xFFF4C48A)
    val Sun = Color(0xFFFFE6AE)
    val Cloud = Color(0xFFF2A88E)
    val Mesa = Color(0xFF9A5E4A)
    val MesaShade = Color(0xFF7A4638)
    val Far = Color(0xFFC88A5C)
    val Sand = Color(0xFFD7A66C)
    val SandLow = Color(0xFFB07D4F)
    val SandLight = Color(0xFFE8BF85)
    val Ripple = Color(0xFFB98552)
    val Road = Color(0xFFF0DCB0)
    val Stone = Color(0xFF8E7356)
    val Face = Color(0xFFC77B4A)
    val FaceDark = Color(0xFF8A4A2C)
    val Strata = Color(0xFF7A3E24)
    val Gap = Color(0xFF2E1A14)
    val Floor = Color(0xFF9E6640)
    val Lip = Color(0xFFF0C894)
    val Ink = Color(0xFF2B2230)
    val Post = Color(0xFF5A3A24)
    val Banner = Color(0xFF9C3B22)
    val Back = Color(0xE62A1E26)
}

/**
 * A desert road at dawn, and a faint track leaving it. Beats: side (a thin track branches off), deepen (it cuts
 * down into a canyon that widens and deepens as it goes), mimic (a signpost just like the road's own stands at
 * its mouth), trapped (a walker goes down into it and its walls rise until there is no way up), pave (its floor is
 * paved and hung with banners: a religion people follow), bury (sand drifts over the same length of the true road).
 */
internal fun DrawScope.canyonScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val horizon = h * 0.42f
    drawRect(Brush.verticalGradient(0f to Dune.SkyTop, 0.55f to Dune.SkyMid, 1f to Dune.SkyLow, endY = horizon), size = Size(w, horizon))
    for (s in TWINKLES) {
        if (s.x > 0.45f || s.y > 0.4f) continue
        drawCircle(ArtInk.Ivory, s.r * 0.8f * dp, Offset(s.x * w, s.y * horizon), alpha = 0.35f * (0.5f + 0.5f * sin(t + s.p)))
    }
    val sun = Offset(w * 0.82f, horizon)
    softGlow(sun, w * 0.35f, Dune.Sun, 0.75f)
    lightShafts(sun, -PI.toFloat() / 2f, PI.toFloat() * 0.8f, h * 0.6f, Dune.Sun, 0.6f, t, 9)
    drawCircle(Dune.Sun, w * 0.055f, sun)
    for (k in 0 until 4) {
        val cx = w * (0.15f + 0.22f * k) + w * 0.01f * sin(t * 0.1f + k)
        val cy = h * (0.1f + 0.05f * (k % 2))
        drawOval(Dune.Cloud, topLeft = Offset(cx - w * 0.07f, cy), size = Size(w * 0.14f, h * 0.022f), alpha = 0.55f)
        drawOval(Dune.Cloud, topLeft = Offset(cx - w * 0.04f, cy - h * 0.012f), size = Size(w * 0.08f, h * 0.02f), alpha = 0.45f)
    }
    for (k in 0 until 2) {
        val b = Offset(w * (0.36f + 0.06f * k) - fract(t * 0.01f) * w * 0.1f, h * (0.2f + 0.03f * k))
        val f = 2.5f * dp * (1f + 0.4f * sin(t * 6f + k))
        drawLine(Dune.Ink, b, b + Offset(-4f * dp, -f), 0.9f * dp, StrokeCap.Round, alpha = 0.7f)
        drawLine(Dune.Ink, b, b + Offset(4f * dp, -f), 0.9f * dp, StrokeCap.Round, alpha = 0.7f)
    }
    for ((k, mx) in listOf(0.12f, 0.3f, 0.62f).withIndex()) {
        val mw = w * (0.12f + 0.04f * k)
        val mh = h * (0.06f + 0.02f * (k % 2))
        drawPath(Path().apply {
            moveTo(w * mx - mw / 2f, horizon); lineTo(w * mx - mw * 0.35f, horizon - mh); lineTo(w * mx + mw * 0.3f, horizon - mh); lineTo(w * mx + mw / 2f, horizon); close()
        }, Dune.Mesa)
        drawRect(Dune.MesaShade, topLeft = Offset(w * mx + mw * 0.1f, horizon - mh), size = Size(mw * 0.2f, mh))
    }
    hills(Dune.Far, horizon + h * 0.01f, h * 0.025f, 0.02f, 0.045f, 0.7f)
    val groundBrush = Brush.verticalGradient(listOf(Dune.Sand, Dune.SandLow), startY = horizon + h * 0.03f, endY = h)
    drawRect(groundBrush, topLeft = Offset(0f, horizon + h * 0.03f), size = Size(w, h - horizon))
    hills(Dune.Sand, horizon + h * 0.05f, h * 0.02f, 0.03f, 0.07f, 2.1f)
    for (k in 0 until 16) {
        val y = horizon + h * (0.1f + 0.036f * k)
        val x = rnd(k, 12.9898f) * w
        drawLine(Dune.Ripple, Offset(x - w * 0.12f, y), Offset(x + w * 0.12f, y + h * 0.006f), 0.8f * dp, StrokeCap.Round, alpha = 0.45f)
    }

    // the true road, right to left, edged with stones
    fun road(u: Float) = Offset(mix(w * 1.02f, -w * 0.02f, u), mix(h * 0.565f, h * 0.505f, u) + h * 0.01f * sin(u * 5f))
    val roadPath = polyPathOf(List(41) { road(it / 40f) })
    drawPath(roadPath, Dune.Stone, alpha = 0.5f, style = Stroke(13f * dp, cap = StrokeCap.Round))
    drawPath(roadPath, Dune.Road, style = Stroke(10f * dp, cap = StrokeCap.Round))
    drawPath(roadPath, Dune.Stone, alpha = 0.35f, style = Stroke(1f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 7f * dp))))
    // bury: a dune drifts over the same length of the true road
    val buRaw = a.b("bury")
    val bu = easeOut(buRaw)
    if (bu > 0f) {
        val from = 0.33f
        val to = from + 0.42f * bu
        val top = ArrayList<Offset>()
        val bot = ArrayList<Offset>()
        for (k in 0..28) {
            val f = k / 28f
            val c = road(mix(from, to, f))
            val env = min(1f, bump(f) * 1.6f)
            top.add(c + Offset(0f, -(h * 0.05f * (0.75f + 0.25f * sin(k * 1.1f)) * env) - 6f * dp * env))
            bot.add(c + Offset(0f, 9f * dp))
        }
        val mound = Path().apply {
            top.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
            for (p in bot.asReversed()) lineTo(p.x, p.y)
            close()
        }
        drawPath(mound, Brush.verticalGradient(listOf(Dune.SandLight, Dune.Sand), startY = road(from).y - h * 0.06f, endY = road(from).y + 9f * dp))
        for (r in 1..3) drawPath(polyPathOf(top.map { it + Offset(0f, r * 3.5f * dp) }), Dune.Ripple, alpha = 0.5f, style = Stroke(0.8f * dp))
        drawPath(polyPathOf(top), Dune.SandLow, style = Stroke(1f * dp))
        for (k in 0 until 7) {
            val y = road(from).y - h * (0.03f + 0.018f * k)
            val x = fract(t * 0.3f + k * 0.17f) * w
            drawLine(Dune.Road, Offset(x, y), Offset(x - w * 0.08f, y + h * 0.004f), 0.8f * dp, StrokeCap.Round, alpha = 0.55f * bu)
        }
        tagAt(a.tag("bury"), road(from + 0.21f) + Offset(0f, -h * 0.13f), seg(buRaw, 0.4f, 1f), back = Dune.Back)
    }

    // the side track, and the canyon it becomes
    val f0 = road(0.27f)
    val c1 = Offset(w * 0.56f, h * 0.6f)
    val c2 = Offset(w * 0.7f, h * 0.84f)
    val end = Offset(w * 0.2f, h * 1.06f)
    fun mid(u: Float) = cubic(f0, c1, c2, end, u)
    fun nrm(u: Float): Offset {
        val d = mid(min(1f, u + 0.01f)) - mid(max(0f, u - 0.01f))
        val l = max(0.001f, d.getDistance())
        return Offset(-d.y / l, d.x / l)
    }
    val sideB = a.b("side")
    val deep = easeOut(a.b("deepen"))
    val trap = a.b("trapped")
    val rise = easeOut(seg(trap, 0.35f, 0.8f))
    fun half(u: Float) = mix(1.5f * dp, w * 0.105f, u) * deep
    fun depth(u: Float) = (h * 0.13f * deep + h * 0.1f * rise) * u
    fun farTop(u: Float) = mid(u) + nrm(u) * half(u)
    fun nearTop(u: Float) = mid(u) - nrm(u) * half(u)
    fun farFloor(u: Float) = mid(u) + nrm(u) * (half(u) * 0.35f) + Offset(0f, depth(u))
    fun floorMid(u: Float) = (farFloor(u) + nearTop(u)) * 0.5f
    val sp = easeOut(seg(sideB, 0f, 0.8f))
    if (sideB > 0f) {
        if (deep > 0f) {
            val us = List(31) { it / 30f * sp }
            // the gap's darkness, then the far wall's sunlit face with its layers, then the floor
            val gap = Path().apply {
                us.forEachIndexed { i, u -> val p = farTop(u); if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
                for (u in us.asReversed()) { val p = nearTop(u); lineTo(p.x, p.y) }
                close()
            }
            drawPath(gap, Dune.Gap)
            val face = Path().apply {
                us.forEachIndexed { i, u -> val p = farTop(u); if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
                for (u in us.asReversed()) { val p = farFloor(u); lineTo(p.x, p.y) }
                close()
            }
            drawPath(face, Brush.verticalGradient(listOf(Dune.Face, Dune.FaceDark), startY = h * 0.55f, endY = h))
            for (k in 1..4) {
                drawPath(polyPathOf(us.map { u -> lerp2(farTop(u), farFloor(u), k / 5f) + Offset(0f, 1.5f * dp * sin(u * 30f + k)) }), Dune.Strata, alpha = 0.45f, style = Stroke(0.8f * dp))
            }
            val floor = Path().apply {
                us.forEachIndexed { i, u -> val p = farFloor(u); if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
                for (u in us.asReversed()) { val p = nearTop(u); lineTo(p.x, p.y) }
                close()
            }
            drawPath(floor, Brush.verticalGradient(listOf(Dune.Floor, Dune.Gap), startY = h * 0.6f, endY = h * 1.05f))
            // the sunlit lips of both rims
            drawPath(polyPathOf(us.map { farTop(it) }), Dune.Lip, style = Stroke(1.6f * dp, cap = StrokeCap.Round))
            drawPath(polyPathOf(us.map { nearTop(it) }), Dune.SandLow, style = Stroke((1.5f + 2.5f * deep) * dp, cap = StrokeCap.Round))
            tagAt(a.tag("deepen"), mid(0.62f) + Offset(w * 0.25f, h * 0.02f), seg(a.b("deepen"), 0.4f, 1f), back = Dune.Back)
        } else {
            drawPath(cubicPart(f0, c1, c2, end, 0f, sp), Dune.Road, alpha = 0.9f, style = Stroke(1.6f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp))))
        }
        tagAt(a.tag("side"), Offset(w * 0.86f, h * 0.66f), seg(sideB, 0.35f, 1f), back = Dune.Back)
    }
    // the road's own signpost, and its twin at the mouth of the track
    fun post(foot: Offset, alpha: Float) {
        if (alpha <= 0.01f) return
        val top = foot + Offset(0f, -h * 0.11f)
        drawLine(Dune.Post, foot, top, 2.2f * dp, StrokeCap.Round, alpha = alpha)
        drawPath(Path().apply {
            moveTo(top.x + 1f * dp, top.y + 2f * dp); lineTo(top.x - w * 0.045f, top.y + 2f * dp); lineTo(top.x - w * 0.055f, top.y + h * 0.02f); lineTo(top.x - w * 0.045f, top.y + h * 0.038f); lineTo(top.x + 1f * dp, top.y + h * 0.038f); close()
        }, Dune.Post, alpha = alpha)
        drawLine(Dune.Road, top + Offset(-w * 0.038f, h * 0.02f), top + Offset(-w * 0.008f, h * 0.02f), 1f * dp, alpha = alpha * 0.8f)
    }
    post(road(0.16f) + Offset(0f, -6f * dp), 1f)
    val mimRaw = a.b("mimic")
    val mim = easeOut(mimRaw)
    if (mim > 0f) {
        val foot = nearTop(0.07f) + Offset(w * 0.025f, 2f * dp)
        post(foot, mim)
        val g = bump(seg(mimRaw, 0.3f, 1f))
        glint(foot + Offset(-w * 0.03f, -h * 0.1f), 8f * dp * g, Dune.Sun, g)
        tagAt(a.tag("mimic"), Offset(w * 0.58f, h * 0.44f), seg(mimRaw, 0.35f, 1f), back = Dune.Back)
    }
    // pave: the canyon's floor made into a road people follow, hung with banners
    val pv = a.b("pave")
    if (pv > 0f && deep > 0f) {
        for (k in 0 until 9) {
            val u = 0.15f + k * 0.095f
            if (u > sp) break
            val p = easeOut(seg(pv, k * 0.06f, k * 0.06f + 0.4f))
            if (p <= 0f) continue
            val f = farFloor(u)
            val n = nearTop(u)
            drawLine(Dune.Road, f + (n - f) * 0.15f, f + (n - f) * 0.85f, 1.6f * dp, alpha = 0.75f * p)
        }
        for ((k, u) in listOf(0.42f, 0.64f, 0.86f).withIndex()) {
            val p = easeOut(seg(pv, 0.3f + k * 0.15f, 0.6f + k * 0.15f))
            if (p <= 0f) continue
            val c = floorMid(u)
            val tall = h * mix(0.1f, 0.2f, u) * p
            drawLine(Dune.Post, c, c + Offset(0f, -tall), 1.8f * dp, StrokeCap.Round)
            val wave = sin(t * 4f + k) * 2f * dp
            drawPath(Path().apply {
                moveTo(c.x, c.y - tall); lineTo(c.x - w * 0.05f, c.y - tall + h * 0.015f + wave); lineTo(c.x, c.y - tall + h * 0.035f); close()
            }, Dune.Banner, alpha = p)
        }
        tagAt(a.tag("pave"), Offset(w * 0.18f, h * 0.83f), seg(pv, 0.4f, 1f), back = Dune.Back)
    }
    // trapped: a walker goes down into the canyon and cannot climb out
    if (trap > 0f) {
        val u = mix(0.12f, 0.56f, easeInOut(seg(trap, 0f, 0.6f)))
        val c = floorMid(u)
        val tall = h * mix(0.1f, 0.2f, u)
        val walking = trap < 0.6f
        drawLine(Color.Black, c, c + Offset(-tall * 1.1f, tall * 0.05f), tall * 0.07f, StrokeCap.Round, alpha = 0.2f)
        figure(c, tall, Dune.Ink, walk = if (walking) t * 6f else 0f, lean = if (walking) 0.08f else -0.24f * seg(trap, 0.65f, 0.85f), arms = if (walking) 0f else 0.9f * seg(trap, 0.7f, 0.9f))
        tagAt(a.tag("trapped"), Offset(w * 0.3f, h * 0.7f), seg(trap, 0.5f, 1f), back = Dune.Back)
    }
    for ((k, sx) in listOf(0.9f, 0.08f).withIndex()) {
        val b = Offset(w * sx, h * (0.95f - 0.25f * k))
        for (j in 0 until 5) {
            val ang = -PI.toFloat() / 2f + (j - 2) * 0.35f
            drawLine(Color(0xFF5E5A34), b, b + Offset(cos(ang), sin(ang)) * (h * 0.05f), 1.4f * dp, StrokeCap.Round)
        }
    }
}

private fun lerp2(p: Offset, q: Offset, f: Float): Offset = p + (q - p) * f

// ═════════════ 5. map: an old map of the way (atiq) ═════════════

private object MapInk {
    val Paper = Color(0xFFE9DCB9)
    val PaperLow = Color(0xFFD5BF8E)
    val Burn = Color(0xFF8A5A2B)
    val Sea = Color(0xFF8FA9A6)
    val SeaLine = Color(0xFF5E7E7B)
    val Land = Color(0xFFD9C79A)
    val Mount = Color(0xFF8E7148)
    val MountLight = Color(0xFFC4A770)
    val River = Color(0xFF5E86A0)
    val Ink = Color(0xFF4A3520)
    val Red = Color(0xFF9C3B22)
    val Gold = Color(0xFFBE8721)
    val GoldLight = Color(0xFFEBC466)
    val Back = Color(0xF2E9DCB9)
}

/**
 * An old map: sea and coast, mountains, rivers, towns, a compass rose and a title cartouche. Beats: spiral
 * (side tracks in red ink wind off into a spiral and a maze: going too deep, making it hard), old (the old road
 * is inked in gold from its first marker, and its name is written in the cartouche), span (a bracket marks its
 * stretch from one pin to another), caliphs (roundels stand along the road: the first parts of the tag are the
 * caption, then the names), wide (a traveller's token goes the whole road, and the road is wide enough for him).
 */
internal fun DrawScope.mapScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    drawRect(MapInk.Burn)
    // the sheet, its edges a little torn and burnt
    val sheet = Path().apply {
        val n = 40
        for (k in 0..n) {
            val x = w * (0.015f + 0.97f * k / n)
            val y = h * 0.02f + rnd(k, 3.1f) * 3f * dp
            if (k == 0) moveTo(x, y) else lineTo(x, y)
        }
        for (k in 0..n) lineTo(w * 0.985f - rnd(k, 5.7f) * 3f * dp, h * (0.02f + 0.96f * k / n))
        for (k in 0..n) lineTo(w * (0.985f - 0.97f * k / n), h * 0.98f - rnd(k, 7.3f) * 3f * dp)
        for (k in 0..n) lineTo(w * 0.015f + rnd(k, 9.1f) * 3f * dp, h * (0.98f - 0.96f * k / n))
        close()
    }
    drawPath(sheet, Brush.radialGradient(listOf(MapInk.Paper, MapInk.PaperLow), center = Offset(w * 0.5f, h * 0.45f), radius = w * 0.7f))
    clipPath(sheet) {
        // the sea in the lower left, with its hatched coast
        val coast = smooth(listOf(Offset(0f, h * 0.62f), Offset(w * 0.14f, h * 0.7f), Offset(w * 0.2f, h * 0.84f), Offset(w * 0.34f, h * 0.9f), Offset(w * 0.4f, h * 1.02f)))
        val seaPath = Path().apply {
            coast.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
            lineTo(0f, h)
            close()
        }
        drawPath(seaPath, MapInk.Sea, alpha = 0.55f)
        for (k in 1..3) drawPath(polyPathOf(coast.map { it + Offset(-k * 4f * dp, k * 3f * dp) }), MapInk.SeaLine, alpha = 0.45f / k, style = Stroke(0.7f * dp))
        drawPath(polyPathOf(coast), MapInk.Ink, style = Stroke(1.1f * dp))
        for (k in 0 until 7) {      // little waves
            val c = Offset(w * (0.03f + 0.04f * k), h * (0.8f + 0.025f * (k % 3)))
            drawArc(MapInk.SeaLine, 200f, 140f, false, topLeft = c - Offset(4f * dp, 2f * dp), size = Size(8f * dp, 5f * dp), style = Stroke(0.8f * dp))
        }
        // latitude and longitude, faint
        for (k in 1..5) drawLine(MapInk.Ink, Offset(w * k / 6f, 0f), Offset(w * k / 6f, h), 0.4f * dp, alpha = 0.18f)
        for (k in 1..3) drawLine(MapInk.Ink, Offset(0f, h * k / 4f), Offset(w, h * k / 4f), 0.4f * dp, alpha = 0.18f)
        // mountains, each a small ridge shaded on one side
        val peaks = listOf(0.66f to 0.2f, 0.74f to 0.17f, 0.82f to 0.22f, 0.9f to 0.18f, 0.16f to 0.2f, 0.25f to 0.16f, 0.33f to 0.22f, 0.58f to 0.82f, 0.68f to 0.86f)
        for ((px, py) in peaks) {
            val b = Offset(w * px, h * py)
            val mw = w * 0.035f
            val mh = h * 0.06f
            drawPath(Path().apply { moveTo(b.x - mw, b.y); lineTo(b.x, b.y - mh); lineTo(b.x + mw, b.y); close() }, MapInk.MountLight)
            drawPath(Path().apply { moveTo(b.x, b.y - mh); lineTo(b.x + mw, b.y); lineTo(b.x + mw * 0.15f, b.y); close() }, MapInk.Mount)
            drawPath(Path().apply { moveTo(b.x - mw, b.y); lineTo(b.x, b.y - mh); lineTo(b.x + mw, b.y) }, MapInk.Ink, style = Stroke(0.7f * dp))
        }
        // a river down to the sea
        val river = smooth(listOf(Offset(w * 0.46f, h * 0.3f), Offset(w * 0.42f, h * 0.46f), Offset(w * 0.3f, h * 0.68f), Offset(w * 0.2f, h * 0.78f)))
        drawPath(polyPathOf(river), MapInk.River, style = Stroke(1.3f * dp, cap = StrokeCap.Round))
        // compass rose, lower right
        val rose = Offset(w * 0.9f, h * 0.84f)
        drawCircle(MapInk.Ink, w * 0.045f, rose, alpha = 0.6f, style = Stroke(0.7f * dp))
        for (k in 0 until 8) {
            val ang = k * PI.toFloat() / 4f - PI.toFloat() / 2f
            val len = if (k % 2 == 0) w * 0.055f else w * 0.032f
            val tip = rose + Offset(cos(ang), sin(ang)) * len
            val sideA = rose + Offset(cos(ang + 0.5f), sin(ang + 0.5f)) * (w * 0.009f)
            val sideB = rose + Offset(cos(ang - 0.5f), sin(ang - 0.5f)) * (w * 0.009f)
            drawPath(Path().apply { moveTo(tip.x, tip.y); lineTo(sideA.x, sideA.y); lineTo(rose.x, rose.y); close() }, MapInk.Ink)
            drawPath(Path().apply { moveTo(tip.x, tip.y); lineTo(sideB.x, sideB.y); lineTo(rose.x, rose.y); close() }, if (k == 0) MapInk.Red else MapInk.MountLight)
        }
    }
    drawPath(sheet, MapInk.Ink, alpha = 0.5f, style = Stroke(1f * dp))
    drawRect(MapInk.Ink, topLeft = Offset(w * 0.035f, h * 0.045f), size = Size(w * 0.93f, h * 0.91f), alpha = 0.55f, style = Stroke(0.8f * dp))

    // the old road, faint until it is inked
    val route = smooth(listOf(Offset(w * 0.88f, h * 0.6f), Offset(w * 0.72f, h * 0.52f), Offset(w * 0.55f, h * 0.6f), Offset(w * 0.38f, h * 0.53f), Offset(w * 0.22f, h * 0.57f), Offset(w * 0.1f, h * 0.48f)))
    drawPath(polyPathOf(route), MapInk.Ink, alpha = 0.35f, style = Stroke(1f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))))
    // towns along the way: a dome and a minaret
    for ((k, u) in listOf(0.0f, 0.36f, 0.64f, 1f).withIndex()) {
        val c = alongPoly(route, u) + Offset(0f, -h * 0.05f)
        val s = 1f + 0.25f * (k % 2)
        // a walled town: houses, a dome, a minaret
        drawRect(MapInk.MountLight, topLeft = c + Offset(-9f * dp * s, -1f * dp), size = Size(18f * dp * s, 7f * dp))
        drawRect(MapInk.Ink, topLeft = c + Offset(-9f * dp * s, -1f * dp), size = Size(18f * dp * s, 7f * dp), style = Stroke(0.8f * dp))
        for (b in 0 until 4) drawRect(MapInk.Ink, topLeft = c + Offset((-9f + b * 5f) * dp * s, -3f * dp), size = Size(2.2f * dp, 2f * dp))
        drawArc(MapInk.Gold, 180f, 180f, true, topLeft = c + Offset(-4f * dp, -6f * dp), size = Size(8f * dp, 8f * dp))
        drawArc(MapInk.Ink, 180f, 180f, false, topLeft = c + Offset(-4f * dp, -6f * dp), size = Size(8f * dp, 8f * dp), style = Stroke(0.8f * dp))
        drawRect(MapInk.Ink, topLeft = c + Offset(5f * dp * s, -11f * dp), size = Size(2.4f * dp, 11f * dp))
        drawPath(Path().apply { moveTo(c.x + 4.6f * dp * s, c.y - 11f * dp); lineTo(c.x + 6.2f * dp * s, c.y - 14f * dp); lineTo(c.x + 7.8f * dp * s, c.y - 11f * dp); close() }, MapInk.Ink)
    }
    // spiral: the red side tracks of going too deep and making it hard
    val spB = a.b("spiral")
    if (spB > 0f) {
        val sp = easeInOut(seg(spB, 0f, 0.9f))
        val dash = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 2.5f * dp))
        val s0 = alongPoly(route, 0.15f)
        val sc = Offset(w * 0.8f, h * 0.29f)
        val spiral = ArrayList<Offset>()
        spiral.addAll(smooth(listOf(s0, s0 + Offset(-w * 0.02f, -h * 0.1f), sc + Offset(w * 0.07f, h * 0.02f)), 8))
        for (k in 0..60) {
            val f = k / 60f
            val ang = f * 5f * PI.toFloat()
            val rr = w * 0.065f * (1f - f)
            spiral.add(sc + Offset(cos(ang) * rr, sin(ang) * rr * 0.7f))
        }
        penLine(spiral, sp, MapInk.Red, 1.4f * dp, dash = dash)
        val m0 = alongPoly(route, 0.7f)
        val mc = Offset(w * 0.24f, h * 0.29f)
        val maze = ArrayList<Offset>()
        maze.addAll(smooth(listOf(m0, m0 + Offset(w * 0.02f, -h * 0.1f), mc + Offset(w * 0.07f, h * 0.06f)), 8))
        for (k in 0 until 7) {
            val y = mc.y + h * 0.06f - k * h * 0.02f
            maze.add(Offset(if (k % 2 == 0) mc.x + w * 0.07f else mc.x - w * 0.07f, y))
            maze.add(Offset(if (k % 2 == 0) mc.x - w * 0.07f else mc.x + w * 0.07f, y))
        }
        penLine(maze, sp, MapInk.Red, 1.4f * dp, dash = dash)
        tagAt(a.tag("spiral"), Offset(w * 0.5f, h * 0.28f), seg(spB, 0.4f, 1f), back = MapInk.Back, ink = MapInk.Red)
    }
    // the cartouche at the top, where the road's name is written
    val ob = a.b("old")
    val cart = Rect(w * 0.33f, h * 0.06f, w * 0.67f, h * 0.18f)
    drawRoundRect(MapInk.Paper, topLeft = cart.topLeft, size = cart.size, cornerRadius = CornerRadius(6f * dp))
    drawRoundRect(MapInk.Ink, topLeft = cart.topLeft, size = cart.size, cornerRadius = CornerRadius(6f * dp), style = Stroke(1.2f * dp))
    for (side in intArrayOf(-1, 1)) {      // the scroll's curled ends
        val ex = if (side < 0) cart.left else cart.right
        drawCircle(MapInk.Ink, 4f * dp, Offset(ex + side * 3f * dp, cart.top + 4f * dp), style = Stroke(1f * dp))
        drawCircle(MapInk.Ink, 4f * dp, Offset(ex + side * 3f * dp, cart.bottom - 4f * dp), style = Stroke(1f * dp))
    }
    if (ob > 0f) {
        val o = easeInOut(seg(ob, 0f, 0.75f))
        penLine(route, o, MapInk.GoldLight, 7f * dp, alpha = 0.45f)
        penLine(route, o, MapInk.Gold, 2.6f * dp)
        val start = route.first()
        softGlow(start, w * 0.05f, MapInk.GoldLight, 0.6f * easeOut(seg(ob, 0f, 0.3f)))
        drawPath(starPath(start, w * 0.018f * easeOut(seg(ob, 0f, 0.3f))), MapInk.Gold)
        val name = a.big("old")
        if (name != null) {
            val sc = min(cart.height * 0.75f / max(1f, name.size.height.toFloat()), cart.width * 0.8f / max(1f, name.size.width.toFloat()))
            val wr = seg(ob, 0.2f, 0.8f)
            val half = name.size.width * sc / 2f
            clipRect(cart.center.x + half - 2f * half * wr - 2f * dp, cart.top, cart.right, cart.bottom) {
                bigText(name, cart.center, sc, MapInk.Gold, 1f)
            }
        }
    } else {
        // until then the cartouche holds a flourish
        drawLine(MapInk.Ink, Offset(cart.left + cart.width * 0.2f, cart.center.y), Offset(cart.right - cart.width * 0.2f, cart.center.y), 0.8f * dp, alpha = 0.4f)
    }
    // span: from the first pin to the second, bracketed
    val spanB = a.b("span")
    if (spanB > 0f) {
        val p0 = route.first()
        val p1 = alongPoly(route, 0.8f)
        val br = easeInOut(seg(spanB, 0.2f, 0.8f))
        val bracket = cubicPart(p0 + Offset(0f, -h * 0.05f), p0 + Offset(0f, -h * 0.17f), p1 + Offset(0f, -h * 0.17f), p1 + Offset(0f, -h * 0.05f), 0f, br)
        drawPath(bracket, MapInk.Ink, style = Stroke(1f * dp, cap = StrokeCap.Round))
        for (pp in listOf(p0, p1)) {
            val pin = easeOut(seg(spanB, 0f, 0.3f))
            drawLine(MapInk.Ink, pp, pp + Offset(0f, -h * 0.05f * pin), 1.2f * dp)
            drawCircle(MapInk.Red, 3.2f * dp * pin, pp + Offset(0f, -h * 0.05f * pin))
        }
        tagAt(a.tag("span"), cubic(p0 + Offset(0f, -h * 0.05f), p0 + Offset(0f, -h * 0.17f), p1 + Offset(0f, -h * 0.17f), p1 + Offset(0f, -h * 0.05f), 0.5f) + Offset(0f, -h * 0.03f), seg(spanB, 0.45f, 1f), back = MapInk.Back, ink = MapInk.Ink)
    }
    // the caliphs' roundels along the road
    val cb = a.b("caliphs")
    if (cb > 0f) {
        val ci = a.nth("caliphs")
        val names = max(0, a.parts(ci) - 1)
        for (i in 0 until names) {
            val u = if (names == 1) 0.5f else mix(0.18f, 0.78f, i / (names - 1f))
            val on = alongPoly(route, u)
            val c = on + Offset(0f, h * 0.15f)
            val g = seg(cb, 0.1f + i * 0.18f, 0.4f + i * 0.18f)
            if (g > 0f) drawLine(MapInk.Ink, on, on + (c - on) * easeOut(g), 1f * dp, alpha = 0.7f)
            nameRoundel(c, w * 0.052f, a.big("caliphs", 0, i + 1), Roundel.Fills[i % Roundel.Fills.size], Roundel.Rim, g)
        }
        tagAt(a.tag("caliphs"), Offset(w * 0.58f, h * 0.9f), seg(cb, 0.5f, 1f), back = MapInk.Back, ink = MapInk.Ink)
    }
    // wide: a traveller's token goes the whole way, on a road wide enough for him
    val wb = a.b("wide")
    if (wb > 0f) {
        val band = easeOut(seg(wb, 0.2f, 1f))
        penLine(route, 1f, MapInk.GoldLight, (7f + 14f * band) * dp, alpha = 0.22f * band)
        val c = alongPoly(route, easeInOut(seg(wb, 0f, 0.85f)))
        softGlow(c, 14f * dp, MapInk.GoldLight, 0.6f)
        drawCircle(Color(0xFF7A5A22), 5.5f * dp, c)
        drawCircle(MapInk.GoldLight, 4f * dp, c)
        drawCircle(MapInk.Ink, 1.5f * dp, c)
        tagAt(a.tag("wide"), route.last() + Offset(w * 0.06f, -h * 0.1f), seg(wb, 0.5f, 1f), back = MapInk.Back, ink = MapInk.Ink)
    }
}

// ═════════════ 6. books: a courtyard in the morning (ilm-ittiba) ═════════════

private object Court {
    val SkyTop = Color(0xFFAFCDE2)
    val SkyLow = Color(0xFFF5EAD3)
    val Stone = Color(0xFFE6D6B8)
    val StoneShade = Color(0xFFC6AF86)
    val Arch = Color(0xFF7D6446)
    val Dado = Color(0xFF2E6E5A)
    val Ground = Color(0xFFDCC9A3)
    val GroundLow = Color(0xFFC3AB80)
    val Way = Color(0xFFF4EAD0)
    val WayEdge = Color(0xFFCDB68C)
    val Ink = Color(0xFF2B303E)
    val Light = Color(0xFFFFF0C4)
    val Rock = Color(0xFF8A7058)
    val RockDark = Color(0xFF5C4838)
    val Pit = Color(0xFF231E1A)
    val Palm = Color(0xFF3E6B4A)
    val Trunk = Color(0xFF7A5B3A)
    val Back = Color(0xE62B303E)
}

private val BOOK_COLOURS = listOf(Color(0xFF8C2F2A), Color(0xFF2F5E8C), Color(0xFF3E7A4E), Color(0xFF8C6A2F), Color(0xFF5E3E7A))

/** A way drawn in perspective: wide at its first point, narrow at its last. */
private fun DrawScope.wayBand(pts: List<Offset>, near: Float, far: Float, fill: Color, edge: Color) {
    val l = ArrayList<Offset>()
    val r = ArrayList<Offset>()
    for (i in pts.indices) {
        val d = pts[min(pts.size - 1, i + 1)] - pts[max(0, i - 1)]
        val len = max(0.001f, d.getDistance())
        val n = Offset(-d.y / len, d.x / len)
        val half = mix(near, far, i / (pts.size - 1f)) / 2f
        l.add(pts[i] + n * half)
        r.add(pts[i] - n * half)
    }
    val band = Path().apply {
        l.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
        for (p in r.asReversed()) lineTo(p.x, p.y)
        close()
    }
    drawPath(band, fill)
    drawPath(polyPathOf(l), edge, style = Stroke(1f * density))
    drawPath(polyPathOf(r), edge, style = Stroke(1f * density))
}

/**
 * A madrasa courtyard in the morning: an arcade, a minaret, a palm, a lit doorway on the right and a broken edge
 * over a drop on the left. Beats: pile (a man's stack of books grows and grows), single (another, with one small
 * book, walks toward the lit door on the footprints before him), stack (the first walks off toward the edge, his
 * stack teetering, a book falling into the drop), learner (a child where the ways meet looks at both, then
 * follows the footprints).
 */
internal fun DrawScope.booksScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val horizon = h * 0.44f
    drawRect(Brush.verticalGradient(listOf(Court.SkyTop, Court.SkyLow), endY = horizon), size = Size(w, horizon))
    lightShafts(Offset(w * 0.95f, -h * 0.05f), PI.toFloat() * 0.7f, 0.5f, h * 1.1f, Court.Light, 0.5f, t, 5)
    // a minaret beyond the arcade
    val mx = w * 0.3f
    drawRect(Court.StoneShade, topLeft = Offset(mx - w * 0.022f, horizon - h * 0.4f), size = Size(w * 0.044f, h * 0.4f))
    drawRect(Court.Stone, topLeft = Offset(mx - w * 0.03f, horizon - h * 0.41f), size = Size(w * 0.06f, h * 0.022f))
    drawPath(horseshoe(mx, horizon - h * 0.355f, w * 0.009f, w * 0.007f, horizon - h * 0.33f), Court.Arch)
    drawRect(Court.StoneShade, topLeft = Offset(mx - w * 0.014f, horizon - h * 0.47f), size = Size(w * 0.028f, h * 0.06f))
    drawPath(Path().apply { moveTo(mx - w * 0.016f, horizon - h * 0.47f); lineTo(mx, horizon - h * 0.51f); lineTo(mx + w * 0.016f, horizon - h * 0.47f); close() }, Court.Dado)
    // the arcade along the back of the courtyard, with a band of zellige under it
    val arcTop = horizon - h * 0.25f
    drawRect(Court.Stone, topLeft = Offset(0f, arcTop), size = Size(w, h * 0.25f))
    drawLine(Court.StoneShade, Offset(0f, arcTop + h * 0.02f), Offset(w, arcTop + h * 0.02f), 1f * dp)
    var ax = w * 0.06f
    while (ax < w * 0.78f) {
        drawPath(horseshoe(ax, horizon - h * 0.14f, w * 0.045f, w * 0.036f, horizon - h * 0.04f), Court.StoneShade)
        drawPath(horseshoe(ax, horizon - h * 0.14f, w * 0.045f, w * 0.036f, horizon - h * 0.04f), Court.Arch, style = Stroke(1f * dp))
        ax += w * 0.115f
    }
    drawRect(Court.Dado, topLeft = Offset(0f, horizon - h * 0.04f), size = Size(w, h * 0.04f))
    var zx = w * 0.01f
    while (zx < w) {
        drawPath(starPath(Offset(zx, horizon - h * 0.02f), h * 0.013f), Court.Stone, alpha = 0.85f)
        zx += w * 0.04f
    }
    // the lit doorway on the right
    val door = Offset(w * 0.87f, horizon - h * 0.04f)
    val single = easeOut(a.b("single"))
    softGlow(door + Offset(0f, -h * 0.08f), w * 0.2f, Court.Light, 0.55f + 0.3f * single)
    drawPath(horseshoe(door.x, door.y - h * 0.15f, w * 0.06f, w * 0.048f, door.y), Court.Light)
    drawPath(horseshoe(door.x, door.y - h * 0.15f, w * 0.06f, w * 0.048f, door.y), Court.Arch, style = Stroke(2f * dp))
    drawPath(horseshoe(door.x, door.y - h * 0.15f, w * 0.072f, w * 0.06f, door.y), Court.Dado, style = Stroke(1.2f * dp))
    // the courtyard's paving, in perspective
    drawRect(Brush.verticalGradient(listOf(Court.Ground, Court.GroundLow), startY = horizon, endY = h), topLeft = Offset(0f, horizon), size = Size(w, h - horizon))
    val vp = Offset(w * 0.5f, horizon - h * 0.2f)
    for (k in -6..6) {
        val bottom = Offset(w * 0.5f + k * w * 0.16f, h)
        val topP = vp + (bottom - vp) * ((horizon - vp.y) / (h - vp.y))
        drawLine(Court.StoneShade, topP, bottom, 0.6f * dp, alpha = 0.55f)
    }
    for (k in 1..6) {
        val y = horizon + (h - horizon) * (k / 6f).let { it * it }
        drawLine(Court.StoneShade, Offset(0f, y), Offset(w, y), 0.6f * dp, alpha = 0.55f)
    }
    // the broken edge on the left, and the drop beneath it
    val edge = listOf(Offset(0f, horizon + h * 0.08f), Offset(w * 0.1f, horizon + h * 0.11f), Offset(w * 0.16f, horizon + h * 0.2f), Offset(w * 0.12f, horizon + h * 0.32f), Offset(w * 0.17f, h * 1.01f))
    val drop = Path().apply {
        edge.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
        lineTo(0f, h)
        close()
    }
    drawPath(drop, Brush.horizontalGradient(listOf(Court.Pit, Color(0xFF3A3028)), startX = 0f, endX = w * 0.17f))
    drawPath(polyPathOf(edge.map { it + Offset(-w * 0.018f, h * 0.012f) }), Court.RockDark, style = Stroke(5f * dp, join = StrokeJoin.Round))
    drawPath(polyPathOf(edge), Court.Rock, style = Stroke(2.5f * dp, join = StrokeJoin.Round))
    for (k in 0 until 5) drawCircle(Court.Rock, (2f + 1.5f * k) * dp * 0.7f, Offset(w * (0.04f + 0.02f * k), h * (0.62f + 0.07f * k)), alpha = 0.7f)
    // a palm by the door
    val palm = Offset(w * 0.7f, horizon + h * 0.02f)
    drawLine(Court.Trunk, palm, palm + Offset(-w * 0.01f, -h * 0.32f), 3.8f * dp, StrokeCap.Round)
    for (k in 1..5) drawLine(Color(0xFF5E4428), palm + Offset(-w * 0.002f * k, -h * 0.055f * k), palm + Offset(-w * 0.002f * k + 3f * dp, -h * 0.055f * k - 2f * dp), 1f * dp)
    val crown = palm + Offset(-w * 0.01f, -h * 0.32f)
    for (k in 0 until 8) {
        val ang = -PI.toFloat() + k * PI.toFloat() / 7f + 0.04f * sin(t * 0.8f + k)
        val tip = crown + Offset(cos(ang) * w * 0.08f, sin(ang) * h * 0.05f + h * 0.05f)
        drawPath(cubicPart(crown, crown + Offset(cos(ang) * w * 0.045f, -h * 0.035f), tip, tip, 0f, 1f), Court.Palm, style = Stroke(2.8f * dp, cap = StrokeCap.Round))
    }

    // the two ways from where the child stands
    val j = Offset(w * 0.5f, h * 1.02f)
    val rightWay = smooth(listOf(j, Offset(w * 0.62f, h * 0.8f), Offset(w * 0.78f, h * 0.6f), door), 12)
    val leftWay = smooth(listOf(j, Offset(w * 0.38f, h * 0.8f), Offset(w * 0.24f, h * 0.66f), Offset(w * 0.15f, horizon + h * 0.19f)), 12)
    wayBand(leftWay, w * 0.2f, w * 0.05f, Court.Way, Court.WayEdge)
    wayBand(rightWay, w * 0.2f, w * 0.05f, Court.Way, Court.WayEdge)
    fun tallOn(foot: Offset) = h * mix(0.1f, 0.36f, ((foot.y - horizon) / (h - horizon)).coerceIn(0f, 1f))

    // single: the footprints of those before, and a man with one small book
    val sbRaw = a.b("single")
    if (sbRaw > 0f) {
        val prints = easeOut(seg(sbRaw, 0f, 0.6f))
        for (k in 0 until 16) {
            val u = 0.24f + k * 0.045f
            if (u > 0.24f + 0.72f * prints) break
            val c = alongPoly(rightWay, u)
            val s = tallOn(c) * 0.03f
            drawOval(Court.Arch, topLeft = c + Offset((if (k % 2 == 0) -1.5f else 0.5f) * s - s, -s * 0.5f), size = Size(s * 2f, s), alpha = 0.5f)
        }
        val go = easeInOut(seg(sbRaw, 0.25f, 1f))
        val foot = alongPoly(rightWay, mix(0.2f, 0.42f, go))
        val tall = tallOn(foot)
        val appear = seg(sbRaw, 0f, 0.15f)
        drawLine(Color.Black, foot, foot + Offset(-tall * 0.8f, tall * 0.05f), tall * 0.06f, StrokeCap.Round, alpha = 0.15f * appear)
        figure(foot, tall, Court.Ink, alpha = appear, walk = if (go in 0.01f..0.99f) t * 6f else 0f, arms = 0.4f)
        val bk = foot + Offset(-tall * 0.2f, -tall * 0.56f)
        softGlow(bk, tall * 0.3f, Court.Light, 0.8f * appear)
        drawRect(Court.Dado, topLeft = bk - Offset(tall * 0.08f, tall * 0.05f), size = Size(tall * 0.16f, tall * 0.1f), alpha = appear)
        drawRect(Color(0xFFD9A445), topLeft = bk - Offset(tall * 0.08f, tall * 0.05f), size = Size(tall * 0.16f, tall * 0.1f), alpha = appear, style = Stroke(0.9f * dp))
        tagAt(a.tag("single"), Offset(foot.x + w * 0.06f, foot.y - tall * 1.25f), seg(sbRaw, 0.3f, 1f), back = Court.Back)
    }
    // pile: a man with more and more books; stack: he walks off toward the edge
    val pb = a.b("pile")
    val kb = a.b("stack")
    if (pb > 0f) {
        val walkOff = easeInOut(seg(kb, 0f, 0.75f))
        val foot = alongPoly(leftWay, mix(0.2f, 0.78f, walkOff))
        val tall = tallOn(foot)
        val appear = seg(pb, 0f, 0.15f)
        val walking = kb > 0f && kb < 0.75f
        drawLine(Color.Black, foot, foot + Offset(-tall * 0.8f, tall * 0.05f), tall * 0.06f, StrokeCap.Round, alpha = 0.15f * appear)
        figure(foot, tall, Court.Ink, alpha = appear, walk = if (walking) t * 6f else 0f, arms = 0.55f)
        val books = (easeOut(seg(pb, 0.1f, 0.9f)) * 11f).toInt()
        val sway = sin(t * 3.2f) * 8f * seg(kb, 0.1f, 0.6f)
        val baseB = foot + Offset(-tall * 0.22f, -tall * 0.52f)
        val bh = tall * 0.06f
        withTransform({ rotate(sway, baseB) }) {
            for (k in 0 until books) {
                if (kb > 0.55f && k == books - 1) continue
                val bw = tall * (0.32f + 0.08f * rnd(k, 3.3f))
                val off = (rnd(k, 7.7f) - 0.5f) * tall * 0.07f
                drawRect(BOOK_COLOURS[k % BOOK_COLOURS.size], topLeft = Offset(baseB.x - bw / 2f + off, baseB.y - (k + 1) * bh), size = Size(bw, bh * 0.92f), alpha = appear)
                drawLine(Court.Way, Offset(baseB.x - bw / 2f + off + 2f * dp, baseB.y - (k + 0.5f) * bh), Offset(baseB.x + bw / 2f + off - 2f * dp, baseB.y - (k + 0.5f) * bh), 0.7f * dp, alpha = 0.6f * appear)
            }
        }
        if (kb > 0.55f && books > 0) {
            val f = easeInOut(seg(kb, 0.55f, 0.95f))
            val from = baseB + Offset(0f, -books * bh)
            val to = Offset(w * 0.06f, h * 0.95f)
            val p = from + (to - from) * f + Offset(0f, -h * 0.08f * bump(f))
            withTransform({ rotate(f * 240f, p) }) {
                drawRect(BOOK_COLOURS[(books - 1) % BOOK_COLOURS.size], topLeft = p - Offset(tall * 0.16f, tall * 0.03f), size = Size(tall * 0.32f, tall * 0.06f), alpha = 1f - 0.6f * f)
            }
        }
        val topY = baseB.y - books * bh
        tagAt(a.tag("pile"), Offset(foot.x - w * 0.02f, max(h * 0.07f, topY - h * 0.06f)), seg(pb, 0.3f, 1f), back = Court.Back)
        if (kb > 0f) tagAt(a.tag("stack"), Offset(foot.x + w * 0.16f, foot.y - tall * 0.3f), seg(kb, 0.3f, 1f), back = Court.Back)
    }
    // learner: a child where the ways meet
    val lb = a.b("learner")
    if (lb > 0f) {
        val look = 0.2f * easeInOut(seg(lb, 0.1f, 0.3f)) * (1f - easeInOut(seg(lb, 0.35f, 0.55f))) - 0.2f * easeInOut(seg(lb, 0.35f, 0.55f)) * (1f - easeInOut(seg(lb, 0.6f, 0.7f)))
        val go = easeInOut(seg(lb, 0.65f, 1f))
        val foot = alongPoly(rightWay, 0.02f + 0.12f * go)
        val tall = tallOn(foot) * 0.62f
        figure(foot, tall, Court.Ink, alpha = seg(lb, 0f, 0.12f), walk = if (go in 0.01f..0.99f) t * 7f else 0f, lean = look, stride = 0.8f)
        tagAt(a.tag("learner"), foot + Offset(0f, -tall * 1.45f), seg(lb, 0.3f, 1f), back = Court.Back)
    }
}

// ═════════════ 7. caravan: a caravan at night (zallat-tariq) ═════════════

private object Night {
    val Top = Color(0xFF060A1A)
    val Low = Color(0xFF1E2244)
    val DuneFar = Color(0xFF161830)
    val Dune = Color(0xFF1E2036)
    val DuneNear = Color(0xFF14152A)
    val Rim = Color(0xFF9AA2C8)
    val Road = Color(0xFF4E4964)
    val RoadEdge = Color(0xFF6A6584)
    val Line = Color(0xFFEDEBE4)
    val Dim = Color(0xFFA6A8BC)
    val Camel = Color(0xFF3A3652)
    val Cold = Color(0xFF5A6AA8)
    val Hollow = Color(0xFF07070C)
    val Warn = Color(0xFFF2B451)
    val Back = Color(0xE60E1015)
}

/**
 * A caravan crossing the desert at night under a guiding star, walking to the left with its camel. Beats: fork
 * (the two ways of leaving the road are marked), slip (one walker slips into a hollow beside the road, meaning
 * well), around (those behind do not follow him in: their steps go round it, and one reaches out to him), defy
 * (the one at the front turns off into the dark on purpose, beckoning), warn (two start to drift after him; the
 * last raises a warning and they come back).
 */
internal fun DrawScope.caravanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    nightSky(t, Night.Top, Night.Low, 0.62f)
    val moon = Offset(w * 0.86f, h * 0.14f)
    softGlow(moon, w * 0.14f, Color(0xFFE9E2C8), 0.4f)
    drawCircle(Color(0xFFF2EAD0), w * 0.036f, moon)
    drawCircle(Night.Top, w * 0.031f, moon + Offset(w * 0.015f, -w * 0.006f))
    val star = Offset(w * 0.08f, h * 0.11f)
    glint(star, 16f * dp * (0.9f + 0.1f * sin(t * 2f)), Color(0xFFEAF2FF), 0.95f)
    drawCircle(Color.White, 2.2f * dp, star)
    // dunes, their crests catching the moon
    hills(Night.DuneFar, h * 0.55f, h * 0.035f, 0.018f, 0.04f, 1.1f)
    hills(Night.Dune, h * 0.63f, h * 0.03f, 0.025f, 0.06f, 2.7f)
    drawRect(Night.Dune, topLeft = Offset(0f, h * 0.68f), size = Size(w, h * 0.32f))
    for (k in 0 until 3) {
        val y = h * (0.555f + 0.08f * k)
        val crest = polyPathOf(List(30) { i -> val x = w * i / 29f; Offset(x, y + h * (0.03f - 0.005f * k) * (sin((x / dp) * (0.018f + 0.007f * k) + 1.1f + k * 1.6f) * 0.6f + sin((x / dp) * (0.04f + 0.02f * k) + 2.3f * (1.1f + k * 1.6f)) * 0.4f)) })
        drawPath(crest, Night.Rim, alpha = 0.18f - 0.04f * k, style = Stroke(1f * dp))
    }
    // the road, right to left, and its cairns
    fun road(u: Float) = Offset(mix(w * 1.02f, -w * 0.02f, u), h * 0.75f + h * 0.012f * sin(u * 4f))
    val roadPath = polyPathOf(List(41) { road(it / 40f) })
    drawPath(roadPath, Night.RoadEdge, alpha = 0.6f, style = Stroke(15f * dp, cap = StrokeCap.Round))
    drawPath(roadPath, Night.Road, style = Stroke(12f * dp, cap = StrokeCap.Round))
    for (u in listOf(0.06f, 0.3f, 0.95f)) {
        val c = road(u) + Offset(0f, -10f * dp)
        drawCircle(Night.Dim, 3.4f * dp, c + Offset(0f, 2.5f * dp), alpha = 0.55f)
        drawCircle(Night.Dim, 2.5f * dp, c + Offset(0.6f * dp, -2.5f * dp), alpha = 0.55f)
        drawCircle(Night.Dim, 1.7f * dp, c + Offset(0f, -6f * dp), alpha = 0.55f)
    }
    // the hollow beside the road
    val hollowC = road(0.54f) + Offset(0f, h * 0.13f)
    val hw = w * 0.085f
    val hh = h * 0.045f
    drawOval(Brush.radialGradient(listOf(Night.Hollow, Color(0xFF15142A)), hollowC, hw), topLeft = hollowC - Offset(hw, hh), size = Size(2 * hw, 2 * hh))
    drawArc(Night.Rim, 190f, 160f, false, topLeft = hollowC - Offset(hw, hh), size = Size(2 * hw, 2 * hh), alpha = 0.35f, style = Stroke(1.2f * dp))
    for (k in 0 until 6) {
        val ang = PI.toFloat() * (0.1f + 0.16f * k)
        drawCircle(Night.Dim, (1.5f + rnd(k, 3.7f)) * dp, hollowC + Offset(cos(ang) * hw * 1.02f, sin(ang) * hh * 1.05f), alpha = 0.45f)
    }

    val fb = a.b("fork")
    val sl = a.b("slip")
    val ar = a.b("around")
    val df = a.b("defy")
    val wn = a.b("warn")
    val dash = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))
    val darkEnd = Offset(w * 0.04f, h * 0.53f)
    val leave = road(0.8f)
    fun offRoad(u: Float) = cubic(leave, leave + Offset(-w * 0.02f, -h * 0.07f), darkEnd + Offset(w * 0.07f, h * 0.03f), darkEnd, u)
    if (fb > 0f) {
        val f = easeOut(seg(fb, 0f, 0.7f))
        drawPath(cubicPart(road(0.52f), road(0.52f) + Offset(0f, h * 0.05f), hollowC + Offset(w * 0.03f, -h * 0.05f), hollowC, 0f, f), Night.Dim, style = Stroke(1.3f * dp, pathEffect = dash))
        drawPath(cubicPart(leave, leave + Offset(-w * 0.02f, -h * 0.07f), darkEnd + Offset(w * 0.07f, h * 0.03f), darkEnd, 0f, f), Night.Dim, style = Stroke(1.3f * dp, pathEffect = dash))
        tagAt(a.tag("fork"), Offset(w * 0.42f, h * 0.38f), seg(fb, 0.35f, 1f), back = Night.Back)
    }
    val walkPh = t * 5f
    val tallN = h * 0.19f
    // the warner at the back, and the one before him
    val drift = easeOut(seg(wn, 0f, 0.35f)) * (1f - easeOut(seg(wn, 0.7f, 1f)))
    val raise = easeOut(seg(wn, 0.35f, 0.6f))
    val f4 = road(0.12f)
    val f3 = road(0.25f)
    figure(f3, tallN, Night.Line, walk = if (wn <= 0f) walkPh + 2f else 0f, lean = 0.32f * drift)
    figure(f4, tallN, Night.Line, walk = if (wn <= 0f) walkPh + 3f else 0f, lean = 0.22f * drift * (1f - raise), arms = raise)
    if (raise > 0f) {
        val sign = f4 + Offset(-w * 0.05f, -tallN * 1.35f)
        val pulse = 0.75f + 0.25f * sin(t * 6f)
        softGlow(sign, 14f * dp, Night.Warn, 0.55f * raise * pulse)
        drawPath(Path().apply { moveTo(sign.x, sign.y - 8f * dp); lineTo(sign.x + 8f * dp, sign.y + 6f * dp); lineTo(sign.x - 8f * dp, sign.y + 6f * dp); close() }, Night.Warn, alpha = raise, style = Stroke(1.7f * dp, join = StrokeJoin.Round))
        drawLine(Night.Warn, sign + Offset(0f, -3f * dp), sign + Offset(0f, 1.8f * dp), 1.6f * dp, StrokeCap.Round, alpha = raise)
        drawCircle(Night.Warn, 1f * dp, sign + Offset(0f, 4f * dp), alpha = raise)
    }
    // the helper: his steps go round the hollow, and he reaches out
    val reach = easeOut(seg(ar, 0.4f, 0.8f))
    val helper = road(0.42f)
    if (ar > 0f) {
        val g = easeOut(seg(ar, 0f, 0.6f))
        drawPath(cubicPart(road(0.43f), road(0.5f) + Offset(0f, -h * 0.06f), road(0.6f) + Offset(0f, -h * 0.06f), road(0.67f), 0f, g), Night.Line, alpha = 0.6f, style = Stroke(1.3f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f * dp, 4f * dp))))
        tagAt(a.tag("around"), Offset(w * 0.68f, h * 0.48f), seg(ar, 0.35f, 1f), back = Night.Back)
    }
    figure(helper, tallN, Night.Line, walk = if (ar <= 0f) walkPh + 1f else 0f, lean = 0.18f * reach, arms = reach)
    // the one who slips into the hollow
    val slipF = easeInOut(seg(sl, 0f, 0.6f))
    val start = road(0.5f)
    val slipFoot = start + (hollowC - start) * slipF + Offset(0f, -h * 0.04f * bump(slipF))
    figure(slipFoot, tallN * mix(1f, 0.7f, slipF), Night.Line, walk = if (sl <= 0f) walkPh else 0f, lean = 0.55f * slipF, arms = -0.5f * slipF)
    if (sl > 0f) tagAt(a.tag("slip"), Offset(w * 0.64f, h * 0.94f), seg(sl, 0.35f, 1f), back = Night.Back)
    // the camel, led on toward the front
    val camelFoot = road(0.65f) + Offset(0f, -2f * dp)
    camel(camelFoot, h * 0.24f, Night.Camel, walkPh, 1f)
    camel(camelFoot + Offset(-0.8f * dp, -0.8f * dp), h * 0.24f, Night.Rim, walkPh, 0.14f)
    // the one at the front who turns off into the dark, beckoning
    val go = easeInOut(seg(df, 0f, 0.8f))
    val front = if (df <= 0f) leave else offRoad(go)
    if (df > 0f) {
        softGlow(front + Offset(0f, -tallN * 0.5f), w * 0.1f, Night.Cold, 0.4f * go)
        tagAt(a.tag("defy"), Offset(w * 0.2f, h * 0.27f), seg(df, 0.35f, 1f), back = Night.Back)
    }
    val beckon = if (df > 0f) (0.5f + 0.5f * sin(t * 3f)) * seg(df, 0.6f, 1f) else 0f
    figure(front, tallN * mix(1f, 0.8f, go), Night.Line, walk = if (df <= 0f || go < 1f) walkPh + 0.5f else 0f, lean = -0.12f * beckon, arms = if (df > 0f) -0.2f - 0.8f * beckon else 0f)
    if (wn > 0f) tagAt(a.tag("warn"), Offset(w * 0.24f, h * 0.62f), seg(wn, 0.3f, 1f), back = Night.Back, ink = Night.Warn)
}
