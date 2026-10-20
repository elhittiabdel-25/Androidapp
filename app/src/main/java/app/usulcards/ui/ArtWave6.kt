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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Still lifes on a lit table (the charms cut down, the knots untied), lights in the night (the five lights of
 * faith, the six pillars, the order of the Companions written in gold), names lit on a wall, a flat picture
 * with robed silhouettes (an oath), and a map on parchment (whom one loves and disowns).
 */

/** A word drawn large, from its pre-measured tag. */
internal fun DrawScope.bigText(tl: TextLayoutResult?, c: Offset, sc: Float, color: Color, alpha: Float) {
    if (tl == null || alpha <= 0.01f) return
    withTransform({ scale(sc, sc, pivot = c) }) {
        drawText(tl, color = color, topLeft = c - Offset(tl.size.width / 2f, tl.size.height / 2f), alpha = alpha.coerceIn(0f, 1f))
    }
}

private fun lerpC(a: Color, b: Color, f: Float): Color = lerp(a, b, f.coerceIn(0f, 1f))

private fun easeIn2(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ───────────── still life: a dark wall, a wooden table, one warm light ─────────────

private object Still {
    val Wall = Color(0xFF120E0C)
    val WallLow = Color(0xFF1F1813)
    val Wood = Color(0xFF3B2719)
    val WoodDark = Color(0xFF241810)
    val Edge = Color(0xFF5A3D27)
    val Thread = Color(0xFFD8C7A6)
    val Light = Color(0xFFFFE2A8)
    val Leather = Color(0xFF6B4226)
    val Back = Color(0xE6140F0B)
    val Ok = Color(0xFF86D6AE)
    val Bad = Color(0xFFE8794A)
}

private fun DrawScope.stillLife(table: Float, spot: Offset, light: Float) {
    val w = size.width
    val h = size.height
    val dp = density
    drawRect(Brush.verticalGradient(listOf(Still.Wall, Still.WallLow), endY = table))
    drawRect(Brush.verticalGradient(listOf(Still.Wood, Still.WoodDark), startY = table, endY = h), topLeft = Offset(0f, table), size = Size(w, h - table))
    for (k in 0 until 6) {
        val y = table + (h - table) * (0.15f + k * 0.15f)
        drawLine(Still.WoodDark, Offset(0f, y), Offset(w, y + sin(k * 1.7f) * 2f * dp), 0.8f * dp, alpha = 0.6f)
    }
    drawLine(Still.Edge, Offset(0f, table), Offset(w, table), 1.4f * dp)
    val r = w * 0.45f
    softGlow(spot, r, Still.Light, 0.22f * light)
    // the pool of light on the table
    val pool = Offset(spot.x, table + (h - table) * 0.35f)
    drawOval(Brush.radialGradient(listOf(Still.Light.copy(alpha = 0.18f * light), Color.Transparent), center = pool, radius = w * 0.3f),
        topLeft = Offset(pool.x - w * 0.3f, pool.y - h * 0.09f), size = Size(w * 0.6f, h * 0.18f))
}

/** An open book on the table, its lines written as [p] goes (0..1), lit by [glow]. */
private fun DrawScope.openBook(c: Offset, bw: Float, p: Float, glow: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val dp = density
    val bh = bw * 0.42f
    if (glow > 0.01f) {
        softGlow(c, bw * 1.1f, Still.Light, 0.35f * glow * alpha)
    }
    for (side in intArrayOf(-1, 1)) {
        val page = Path().apply {
            moveTo(c.x, c.y)
            lineTo(c.x + side * bw * 0.5f, c.y - bh * 0.12f)
            lineTo(c.x + side * bw * 0.5f, c.y + bh * 0.62f)
            lineTo(c.x, c.y + bh * 0.72f)
            close()
        }
        drawPath(page, Color(0xFFF3E7CC), alpha = alpha)
        drawPath(page, Color(0xFF8A6A43), alpha = alpha, style = Stroke(1f * dp))
        // the lines, right page first, each written from its right end
        for (k in 0 until 5) {
            val f = ((p * 10f) - (if (side > 0) 0 else 5) - k).coerceIn(0f, 1f)
            if (f <= 0f) continue
            val y = c.y + bh * (0.08f + k * 0.11f)
            val outer = c.x + side * bw * 0.44f
            val inner = c.x + side * bw * 0.06f
            val from = if (side > 0) outer else inner
            val to = if (side > 0) inner else outer
            drawLine(Color(0xFF3D2A18), Offset(from, y), Offset(mix(from, to, f), y), 1.1f * dp, alpha = alpha * 0.8f)
        }
    }
}

// tamaim-ruqa: the ring, the thread and the amulet hang in the light — cut down; then a page of ruqya lights up

internal fun DrawScope.amuletScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val table = h * 0.74f
    val hang = a.b("hang")
    val snip = a.b("snip")
    val page = a.b("page")
    val pure = a.b("pure")
    val toBook = easeInOut(seg(page, 0f, 0.6f))
    stillLife(table, Offset(mix(w * 0.7f, w * 0.3f, toBook), h * 0.3f), 0.6f + 0.4f * max(seg(hang, 0f, 0.4f), seg(page, 0f, 0.4f)))

    // the rod and the three charms
    val barY = h * 0.1f
    drawLine(ArtInk.Brass, Offset(w * 0.4f, barY), Offset(w * 0.97f, barY), 3f * dp, cap = StrokeCap.Round,
        alpha = seg(hang, 0f, 0.3f) * (1f - 0.6f * seg(page, 0f, 0.5f)))
    val xs = floatArrayOf(0.88f, 0.69f, 0.5f)
    val fall = easeIn2(seg(snip, 0.25f, 0.9f))
    val drop = Offset(0f, fall * h * 0.9f)
    for (k in 0 until 3) {
        val appear = seg(hang, k * 0.2f, k * 0.2f + 0.35f)
        if (appear <= 0f) continue
        val x = w * xs[k]
        val len = h * (0.28f + 0.04f * k)
        val sway = sin(t * 1.4f + k * 1.1f) * 0.06f * (1f - fall)
        val top = Offset(x, barY)
        val rest = top + Offset(sin(sway) * len, cos(sway) * len)
        val knot = top + (rest - top) * 0.45f
        // the thread; once cut, its lower part falls with the charm
        if (fall <= 0f) {
            drawLine(Still.Thread, top, rest, 1.1f * dp, alpha = appear)
        } else {
            drawLine(Still.Thread, top, knot, 1.1f * dp, alpha = appear)
            drawLine(Still.Thread, knot + drop, rest + drop, 1.1f * dp, alpha = appear * (1f - fall))
        }
        val tip = rest + drop
        val al = appear * (1f - fall)
        when (k) {
            0 -> drawCircle(ArtInk.Brass, radius = h * 0.05f, center = tip + Offset(0f, h * 0.05f), alpha = al, style = Stroke(3.5f * dp))
            1 -> {
                val pts = List(10) { m -> tip + Offset(sin(m * 1.3f + t * 2f) * 3f * dp, m * h * 0.012f) }
                for (m in 1 until pts.size) drawLine(Color(0xFFB0402B), pts[m - 1], pts[m], 2.4f * dp, cap = StrokeCap.Round, alpha = al)
                for (m in intArrayOf(2, 5, 8)) drawCircle(Color(0xFFB0402B), radius = 3.2f * dp, center = pts[m], alpha = al)
            }
            else -> {
                val pc = tip + Offset(0f, h * 0.06f)
                val pw = h * 0.085f
                drawRoundRect(Still.Leather, topLeft = pc - Offset(pw / 2, pw * 0.6f), size = Size(pw, pw * 1.2f), cornerRadius = CornerRadius(pw * 0.2f), alpha = al)
                drawRoundRect(Still.Thread, topLeft = pc - Offset(pw / 2 - 2f * dp, pw * 0.6f - 2f * dp), size = Size(pw - 4f * dp, pw * 1.2f - 4f * dp),
                    cornerRadius = CornerRadius(pw * 0.15f), alpha = al * 0.7f,
                    style = Stroke(0.8f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f * dp, 2f * dp), 0f)))
            }
        }
        tagAt(a.tag("hang", 0, k), Offset(x, rest.y + h * 0.15f), seg(hang, 0.4f + k * 0.15f, 0.7f + k * 0.1f) * (1f - seg(snip, 0f, 0.3f)),
            back = Still.Back, ink = Still.Thread)
    }
    // the cut: one bright stroke through the threads, right to left
    if (snip > 0f) {
        val s0 = Offset(w * 0.97f, h * 0.2175f)
        val s1 = Offset(w * 0.42f, h * 0.27f)
        drawLine(Still.Light, s0, s0 + (s1 - s0) * easeOut(seg(snip, 0f, 0.3f)), 2.2f * dp, cap = StrokeCap.Round, alpha = 1f - seg(snip, 0.4f, 0.7f))
        val gone = 1f - seg(page, 0f, 0.3f)
        tagAt(a.tag("snip", 0, 0), Offset(w * 0.69f, h * 0.52f), seg(snip, 0.3f, 0.7f) * gone, back = Still.Back, ink = Still.Bad)
        tagAt(a.tag("snip", 0, 1), Offset(w * 0.5f, h * 0.36f), seg(snip, 0.5f, 1f) * gone, back = Still.Back, ink = Still.Thread)
    }
    // the page of ruqya
    if (page > 0f) {
        val c = Offset(w * 0.3f, table - h * 0.02f)
        openBook(c, w * 0.34f, seg(page, 0.2f, 1f), seg(page, 0.3f, 1f), seg(page, 0f, 0.3f))
        for (m in 0 until 6) {
            val f = fract(t * 0.3f + m / 6f)
            drawCircle(Still.Light, radius = 1.3f * dp, center = c + Offset((m - 2.5f) * w * 0.035f, -f * h * 0.4f), alpha = seg(page, 0.4f, 1f) * (1f - f) * 0.8f)
        }
        tagAt(a.tag("page"), Offset(w * 0.3f, h * 0.18f), seg(page, 0.3f, 0.8f), back = Still.Back, ink = Still.Light)
    }
    if (pure > 0f) {
        checkMark(Offset(w * 0.53f, table - h * 0.12f), h * 0.04f, Still.Ok, 2.4f * dp, seg(pure, 0f, 0.4f))
        tagAt(a.tag("pure"), Offset(w * 0.3f, h * 0.3f), seg(pure, 0.3f, 0.8f), back = Still.Back, ink = Still.Ok)
    }
}

// sihr-nushra: seven heavy stones of ruin, two of them paired; then a knotted rope — no cure by more magic; ruqya and medicine untie it

internal fun DrawScope.knotsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val table = h * 0.8f
    val stones = a.b("stones")
    val pair = a.b("pair")
    val rope = a.b("knotted")
    val fake = a.b("fake")
    val cure = a.b("cure")
    stillLife(table, Offset(w * 0.5f, h * 0.25f), 0.7f)

    // act 1: the seven, resting on the table; the first two glow like embers when they are named together
    val act1 = seg(stones, 0f, 0.3f) * (1f - seg(rope, 0f, 0.35f))
    if (act1 > 0.01f) {
        val tops = ArrayList<Offset>()
        for (k in 0 until 7) {
            val on = seg(stones, k * 0.08f, k * 0.08f + 0.3f)
            val r = h * (0.05f + 0.01f * ((k * 5) % 3))
            val c = Offset(w * (0.88f - 0.127f * k), table - r * 0.8f)
            tops.add(c - Offset(0f, r * 0.8f))
            val hot = if (k < 2) easeOut(seg(pair, 0f, 0.5f)) else 0f
            if (hot > 0f) {
                softGlow(c, r * 2.6f, Still.Bad, 0.4f * hot * act1)
            }
            val stone = Path().apply {
                for (m in 0 until 9) {
                    val ang = m / 9f * 2f * PI.toFloat()
                    val rr = r * (0.84f + 0.16f * sin(m * 2.3f + k * 1.7f))
                    val q = c + Offset(cos(ang) * rr * 1.15f, sin(ang) * rr * 0.8f)
                    if (m == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y)
                }
                close()
            }
            drawPath(stone, lerpC(Color(0xFF2E2621), Color(0xFF7A2A1A), hot), alpha = act1 * on)
            drawPath(stone, lerpC(Color(0xFF54473C), Still.Bad, hot), alpha = act1 * on, style = Stroke(1.2f * dp))
            drawCircle(Color.White, radius = r * 0.12f, center = c - Offset(r * 0.35f, r * 0.3f), alpha = act1 * on * 0.18f)
        }
        if (pair > 0f) {
            val arc = Path().apply {
                moveTo(tops[0].x, tops[0].y - h * 0.02f)
                quadraticBezierTo((tops[0].x + tops[1].x) / 2f, tops[0].y - h * 0.12f, tops[1].x, tops[1].y - h * 0.02f)
            }
            drawPath(arc, Still.Bad, alpha = act1 * seg(pair, 0.2f, 0.6f), style = Stroke(1.6f * dp, cap = StrokeCap.Round))
            tagAt(a.tag("pair", 0, 0), Offset(tops[0].x, tops[0].y - h * 0.2f), act1 * seg(pair, 0.3f, 0.7f), back = Still.Back, ink = Still.Bad)
            tagAt(a.tag("pair", 0, 1), Offset(tops[1].x, tops[1].y - h * 0.28f), act1 * seg(pair, 0.4f, 0.8f), back = Still.Back, ink = Still.Bad)
        }
        tagAt(a.tag("stones"), Offset(w * 0.4f, h * 0.14f), act1 * seg(stones, 0.4f, 0.9f), back = Still.Back, ink = Still.Thread)
    }

    // act 2: the knotted rope
    val act2 = seg(rope, 0.2f, 0.5f)
    if (act2 > 0.01f) {
        val y = h * 0.55f
        val loose = easeInOut(seg(cure, 0.15f, 0.95f))
        val line = Path()
        for (m in 0..60) {
            val u = m / 60f
            val x = mix(w * 0.95f, w * 0.05f, u)
            val yy = y + sin(u * 26f + t * 0.8f) * (1f - loose) * h * 0.012f + sin(u * 5f) * h * 0.01f * loose
            if (m == 0) line.moveTo(x, yy) else line.lineTo(x, yy)
        }
        drawPath(line, Still.Thread, alpha = act2, style = Stroke(3f * dp, cap = StrokeCap.Round))
        val n = 4
        val hot = easeOut(seg(fake, 0.2f, 0.6f)) * (1f - seg(cure, 0f, 0.3f))
        for (k in 0 until n) {
            val x = mix(w * 0.95f, w * 0.05f, (k + 1f) / (n + 1f))
            // the light reaches each knot in turn, from the right
            val r = h * 0.04f * (1f - seg(loose, k / n.toFloat(), (k + 1f) / n))
            if (r > 0.5f) knot(Offset(x, y), r, lerpC(Still.Thread, Still.Bad, hot), act2)
        }
        tagAt(a.tag("knotted"), Offset(w * 0.5f, h * 0.14f), act2 * seg(rope, 0.4f, 0.9f) * (1f - seg(fake, 0f, 0.3f)), back = Still.Back, ink = Still.Thread)
        // the false cure: more magic — smoke round the knots, which only burn hotter; rejected
        if (fake > 0f) {
            val fa = act2 * seg(fake, 0f, 0.3f) * (1f - 0.75f * seg(cure, 0f, 0.3f))
            for (k in 0 until 7) {
                val f = fract(t * 0.25f + k / 7f)
                val c = Offset(w * (0.2f + 0.06f * sin(k * 2f)), y - h * 0.06f - f * h * 0.22f)
                drawCircle(Brush.radialGradient(listOf(Color(0xFF6A6470).copy(alpha = 0.4f * fa * (1f - f)), Color.Transparent), center = c, radius = h * 0.08f),
                    radius = h * 0.08f, center = c)
            }
            crossMark(Offset(w * 0.12f, y - h * 0.17f), h * 0.04f, Still.Bad, 2.4f * dp, fa * seg(fake, 0.4f, 0.7f))
            tagAt(a.tag("fake", 0, 0), Offset(w * 0.25f, y + h * 0.13f), fa * seg(fake, 0.2f, 0.6f), back = Still.Back, ink = Still.Bad)
            tagAt(a.tag("fake", 0, 1), Offset(w * 0.25f, h * 0.14f), fa * seg(fake, 0.5f, 0.9f), back = Still.Back, ink = Still.Bad)
        }
        // the cure: warm light sweeping the rope from the right
        if (cure > 0f) {
            val sx = mix(w * 0.98f, w * 0.05f, easeInOut(seg(cure, 0.1f, 0.95f)))
            drawCircle(Brush.radialGradient(listOf(Still.Light.copy(alpha = 0.45f), Color.Transparent), center = Offset(sx, y), radius = h * 0.16f),
                radius = h * 0.16f, center = Offset(sx, y), alpha = 1f - 0.6f * seg(cure, 0.9f, 1f))
            checkMark(Offset(w * 0.86f, y - h * 0.17f), h * 0.04f, Still.Ok, 2.4f * dp, seg(cure, 0.6f, 0.9f))
            tagAt(a.tag("cure", 0, 0), Offset(w * 0.68f, y + h * 0.13f), seg(cure, 0.2f, 0.6f), back = Still.Back, ink = Still.Light)
            tagAt(a.tag("cure", 0, 1), Offset(w * 0.72f, h * 0.14f), seg(cure, 0.6f, 1f), back = Still.Back, ink = Still.Ok)
        }
    }
}

/** A knot on the rope: a bulge of coiled turns (no ring with a slash: that reads as a "no" sign). */
private fun DrawScope.knot(c: Offset, r: Float, edge: Color, alpha: Float) {
    val dp = density
    drawOval(Color(0xFF8C7A5E), topLeft = c - Offset(r * 1.1f, r * 0.75f), size = Size(r * 2.2f, r * 1.5f), alpha = alpha)
    drawOval(Color(0xFF2A1A12), topLeft = c - Offset(r * 1.1f, r * 0.75f), size = Size(r * 2.2f, r * 1.5f), alpha = alpha * 0.8f, style = Stroke(1f * dp))
    for (m in -1..1) {
        val x0 = c.x + m * r * 0.6f + r * 0.15f
        val turn = Path().apply {
            moveTo(x0, c.y - r * 0.72f)
            quadraticBezierTo(x0 + r * 0.35f, c.y, x0 - r * 0.3f, c.y + r * 0.72f)
        }
        drawPath(turn, edge, alpha = alpha, style = Stroke(1.8f * dp, cap = StrokeCap.Round))
    }
}

// ───────────── lights in the night (navy and gold) ─────────────

private object Sky {
    val Top = Color(0xFF070B1C)
    val Low = Color(0xFF141B36)
    val Gold = ArtInk.Gold
    val Pale = Color(0xFFF7EBC8)
    val Haze = Color(0xFF6E7391)
    val Back = Color(0xD90B1024)
}

private fun DrawScope.navySky(t: Float, stars: Float) {
    val w = size.width
    val h = size.height
    val dp = density
    drawRect(Brush.verticalGradient(listOf(Sky.Top, Sky.Low)))
    for (s in TWINKLES) {
        drawCircle(Sky.Pale.copy(alpha = 0.32f * stars * (0.5f + 0.5f * sin(t * 1.1f + s.p))), radius = s.r * 0.7f * dp, center = Offset(s.x * w, s.y * h))
    }
}

// iman-tafadul: equal at the root, then five lights as the card names them — each burns doubts and desires by its strength

private val STRENGTH = floatArrayOf(1f, 0.78f, 0.58f, 0.4f, 0.22f)

internal fun DrawScope.lightsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    navySky(t, 0.7f)
    val same = a.b("same")
    val unlike = a.b("unlike")
    val burn = a.b("burn")
    val xs = floatArrayOf(0.87f, 0.68f, 0.5f, 0.32f, 0.13f)
    val cy = h * 0.44f
    val grow = easeInOut(unlike)

    // doubts and desires: grey motes around each light, burnt within its reach
    if (burn > 0f) {
        for (k in 0 until 5) {
            val c = Offset(w * xs[k], cy)
            val reach = w * (0.03f + 0.075f * STRENGTH[k]) * easeOut(seg(burn, 0.25f, 0.9f))
            for (m in 0 until 22) {
                val ang = m * 2.39996f + k
                val rad = w * (0.025f + 0.075f * fract(m * 0.618f + k * 0.2f))
                val p = c + Offset(cos(ang + t * 0.05f) * rad, sin(ang + t * 0.05f) * rad * 0.9f)
                val gone = rad < reach
                drawCircle(if (gone) Sky.Gold else Sky.Haze, radius = (if (gone) 1f else 1.6f) * dp, center = p,
                    alpha = seg(burn, 0f, 0.25f) * (if (gone) 0.5f * (1f - seg(burn, 0.6f, 1f)) else 0.7f))
            }
        }
        tagAt(a.tag("burn"), Offset(w * 0.5f, h * 0.1f), seg(burn, 0.3f, 0.8f), back = Sky.Back, ink = Sky.Pale)
    }

    for (k in 0 until 5) {
        val c = Offset(w * xs[k], cy)
        val appear = seg(same, k * 0.1f, k * 0.1f + 0.4f)
        if (appear <= 0f) continue
        val i = a.nth("lit", k)
        val beat = if (i >= 0) a.beatAt(i) else 0f
        val lit = easeOut(beat)
        val s = mix(0.5f, STRENGTH[k], grow)
        // the plain point it starts as
        val dotR = h * 0.022f * (0.7f + 0.6f * s)
        softGlow(c, dotR * 4f, Sky.Gold, 0.4f * appear * s)
        drawCircle(Sky.Pale, radius = dotR * (1f - lit), center = c, alpha = appear)
        if (lit > 0f) lightOf(k, c, h * 0.07f, lit, t)
        tagAt(if (i >= 0) a.tagOf(i) else null, Offset(c.x, h * (if (k % 2 == 0) 0.74f else 0.86f)), seg(beat, 0.3f, 0.8f), back = Sky.Back, ink = Sky.Pale)
    }
    tagAt(a.tag("same"), Offset(w * 0.5f, h * 0.8f), seg(same, 0.4f, 0.9f) * (1f - seg(unlike, 0f, 0.3f)), back = Sky.Back, ink = Sky.Pale)
    tagAt(a.tag("unlike"), Offset(w * 0.5f, h * 0.8f), seg(unlike, 0.3f, 0.8f) * (1f - seg(a.b("lit"), 0f, 0.3f)), back = Sky.Back, ink = Sky.Gold)
}

/** The five lights: the sun, the shining star, the great torch, the bright lamp, the weak lamp. */
private fun DrawScope.lightOf(kind: Int, c: Offset, u: Float, p: Float, t: Float) {
    val dp = density
    val gold = Sky.Gold
    when (kind) {
        0 -> {
            val r = u * 0.75f * p
            softGlow(c, r * 3.2f, gold, 0.6f * p)
            for (m in 0 until 12) {
                val ang = m * PI.toFloat() / 6f + t * 0.15f
                val dir = Offset(cos(ang), sin(ang))
                drawLine(gold, c + dir * (r * 1.25f), c + dir * (r * (1.6f + 0.15f * sin(t * 2f + m))), 1.6f * dp, cap = StrokeCap.Round, alpha = p)
            }
            drawCircle(Sky.Pale, radius = r, center = c, alpha = p)
        }
        1 -> {
            val r = u * 0.65f * p
            softGlow(c, r * 2.6f, Sky.Pale, 0.5f * p)
            val star = Path().apply {
                for (m in 0 until 8) {
                    val ang = m * PI.toFloat() / 4f - PI.toFloat() / 2f
                    val rr = if (m % 2 == 0) r else r * 0.28f
                    val q = c + Offset(cos(ang), sin(ang)) * rr
                    if (m == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y)
                }
                close()
            }
            drawPath(star, Sky.Pale, alpha = p)
        }
        2 -> {
            val fl = 0.85f + 0.15f * sin(t * 7f)
            drawLine(Color(0xFF8A6A43), c + Offset(0f, u * 0.1f), c + Offset(u * 0.12f, u * 1.1f), 3f * dp, cap = StrokeCap.Round, alpha = p)
            softGlow(c, u * 1.8f, gold, 0.45f * p)
            flame(c, u * 0.45f * p, u * 1.1f * p * fl, gold, p)
        }
        else -> {
            val weak = kind == 4
            val v = u * 1.35f
            val flick = if (weak) 0.6f + 0.4f * sin(t * 11f) * sin(t * 3.7f) else 0.92f + 0.08f * sin(t * 5f)
            val dish = Path().apply {
                moveTo(c.x - v * 0.55f, c.y + v * 0.35f)
                quadraticBezierTo(c.x, c.y + v * 0.75f, c.x + v * 0.55f, c.y + v * 0.35f)
                lineTo(c.x + v * 0.75f, c.y + v * 0.3f)
                close()
            }
            drawPath(dish, Color(0xFFB98A55), alpha = p)
            val base = c + Offset(v * 0.45f, v * 0.25f)
            val tall = v * (if (weak) 0.35f else 0.6f) * flick
            val halo = v * (if (weak) 0.6f else 1.2f)
            val mid = base - Offset(0f, tall * 0.5f)
            drawCircle(Brush.radialGradient(listOf(gold.copy(alpha = (if (weak) 0.18f else 0.38f) * p * flick), Color.Transparent), center = mid, radius = halo),
                radius = halo, center = mid)
            flame(base, v * (if (weak) 0.1f else 0.17f), tall, gold, p)
        }
    }
}

private fun DrawScope.flame(base: Offset, halfW: Float, tall: Float, color: Color, alpha: Float) {
    val path = Path().apply {
        moveTo(base.x - halfW, base.y)
        quadraticBezierTo(base.x - halfW, base.y - tall * 0.55f, base.x, base.y - tall)
        quadraticBezierTo(base.x + halfW, base.y - tall * 0.55f, base.x + halfW, base.y)
        close()
    }
    drawPath(path, color, alpha = alpha)
    drawCircle(Sky.Pale, radius = halfW * 0.45f, center = base - Offset(0f, tall * 0.25f), alpha = alpha * 0.8f)
}

// sahaba: the order of the best of them, written in gold, step by step; what happened between them, behind a curtain

internal fun DrawScope.ranksScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    navySky(t, 0.6f)
    val n = min(6, a.count("rank"))
    for (k in 0 until n) {
        val i = a.nth("rank", k)
        val p = a.beatAt(i)
        if (p <= 0f) continue
        val y = h * (0.12f + 0.135f * k)
        val x = w * (0.84f - 0.055f * k)
        val al = easeOut(seg(p, 0f, 0.5f))
        // a step of light under each name
        val stepW = w * (0.24f - 0.015f * k)
        drawLine(Sky.Gold, Offset(x - stepW / 2, y + h * 0.045f), Offset(x + stepW / 2, y + h * 0.045f), 1.6f * dp, cap = StrokeCap.Round, alpha = al * (0.95f - 0.1f * k))
        drawCircle(Brush.radialGradient(listOf(Sky.Gold.copy(alpha = 0.22f * al * (1f - 0.12f * k)), Color.Transparent), center = Offset(x, y), radius = stepW * 0.6f),
            radius = stepW * 0.6f, center = Offset(x, y))
        bigText(a.tagOf(i), Offset(x, y), 1.6f - 0.12f * k, lerpC(Sky.Pale, Sky.Gold, 1f - k / 6f), al)
        // «then» between the steps
        if (k > 0) drawCircle(Sky.Gold, radius = 1.6f * dp, center = Offset(x + w * 0.03f, y - h * 0.07f), alpha = al * 0.7f)
    }

    // what happened between them: held back, behind a curtain
    val veil = a.b("veil")
    val reward = a.b("reward")
    if (veil > 0f) {
        val cx = w * 0.2f
        val floor = h * 0.86f
        val aw = w * 0.26f
        val top = h * 0.2f
        val al = seg(veil, 0f, 0.3f)
        val arch = Path().apply {
            moveTo(cx - aw / 2, floor)
            lineTo(cx - aw / 2, top + aw / 2)
            arcTo(Rect(cx - aw / 2, top, cx + aw / 2, top + aw), 180f, 180f, false)
            lineTo(cx + aw / 2, floor)
            close()
        }
        drawPath(arch, Color(0xFF0B0F22), alpha = al)
        // the curtain, drawn closed from both sides
        val shut = easeInOut(seg(veil, 0.2f, 0.9f))
        for (side in intArrayOf(-1, 1)) {
            val inner = cx + side * (aw / 2) * (1f - shut)
            val outer = cx + side * aw / 2
            val curtain = Path().apply {
                moveTo(outer, top + aw * 0.25f)
                lineTo(inner, top + aw * 0.25f)
                for (m in 0..10) {
                    val yy = mix(top + aw * 0.25f, floor, m / 10f)
                    lineTo(inner + side * sin(m * 1.3f + t * 0.6f) * w * 0.006f, yy)
                }
                lineTo(outer, floor)
                close()
            }
            drawPath(curtain, Color(0xFF5E2533), alpha = al)
            for (m in 1 until 4) {
                val fx = mix(outer, inner, m / 4f)
                drawLine(Color(0xFF3E1520), Offset(fx, top + aw * 0.3f), Offset(fx, floor), 1f * dp, alpha = al * 0.8f)
            }
        }
        drawPath(arch, Sky.Gold, alpha = al * 0.8f, style = Stroke(1.6f * dp))
        tagAt(a.tag("veil"), Offset(cx, h * 0.1f), seg(veil, 0.4f, 0.9f), back = Sky.Back, ink = Sky.Pale)
        if (reward > 0f) {
            for (m in 0 until 2) {
                tagAt(a.tag("reward", 0, m), Offset(cx, h * (0.52f + 0.17f * m)), seg(reward, 0.2f + 0.3f * m, 0.5f + 0.3f * m), back = Sky.Back, ink = Sky.Gold)
            }
        }
    }
}

// iman-pillars: six lights on a ring, joined as each is named; all the rest of belief branches out from them

internal fun DrawScope.pillarsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    navySky(t, 0.4f)
    val c = Offset(w * 0.5f, h * 0.48f)
    val r = h * 0.3f
    val n = min(6, a.count("node"))
    // counter-clockwise from the top: the ring runs the way the line is read
    fun at(k: Int): Offset {
        val ang = (-90f - 60f * k) * PI.toFloat() / 180f
        return c + Offset(cos(ang) * r * 1.25f, sin(ang) * r)
    }
    val branch = a.b("branch")
    // branches: fine lines out from each light, ending in small points
    if (branch > 0f) {
        for (k in 0 until 6) {
            val p0 = at(k)
            val dir = (p0 - c) / (p0 - c).getDistance()
            for (m in 0 until 5) {
                val ang = (m - 2) * 0.32f
                val d = Offset(dir.x * cos(ang) - dir.y * sin(ang), dir.x * sin(ang) + dir.y * cos(ang))
                val len = h * (0.1f + 0.05f * ((m + k) % 3)) * easeOut(seg(branch, m * 0.08f, 0.6f + m * 0.08f))
                val end = p0 + d * len
                drawLine(Sky.Gold, p0, end, 0.9f * dp, alpha = 0.5f)
                drawCircle(Sky.Pale, radius = 1.4f * dp, center = end, alpha = 0.8f * seg(branch, 0.3f + m * 0.08f, 0.6f + m * 0.08f))
            }
        }
    }
    // the ring that joins them
    for (k in 0 until n) {
        val p = a.beatAt(a.nth("node", k))
        if (p <= 0f) continue
        if (k > 0) drawLine(Sky.Gold, at(k - 1), at(k - 1) + (at(k) - at(k - 1)) * easeOut(seg(p, 0f, 0.5f)), 1.2f * dp, alpha = 0.7f)
        if (k == 5) drawLine(Sky.Gold, at(5), at(5) + (at(0) - at(5)) * easeOut(seg(p, 0.4f, 0.9f)), 1.2f * dp, alpha = 0.7f)
    }
    val all = if (n == 6) easeOut(seg(a.beatAt(a.nth("node", 5)), 0.5f, 1f)) else 0f
    if (all > 0f) {
        softGlow(c, r * 0.9f, Sky.Gold, 0.3f * all)
    }
    for (k in 0 until n) {
        val i = a.nth("node", k)
        val p = a.beatAt(i)
        if (p <= 0f) continue
        val q = at(k)
        val al = easeOut(seg(p, 0f, 0.4f))
        softGlow(q, h * 0.06f, Sky.Gold, 0.5f * al)
        drawCircle(Sky.Pale, radius = h * 0.016f, center = q, alpha = al)
        val out = (q - c) / (q - c).getDistance()
        tagAt(a.tagOf(i), q + Offset(out.x * w * 0.09f, out.y * h * 0.1f), seg(p, 0.3f, 0.8f), back = Sky.Back, ink = Sky.Pale)
    }
    tagAt(a.tag("branch"), c, seg(branch, 0.4f, 0.9f), back = Sky.Back, ink = Sky.Gold)
}

// ───────────── names lit on a brick wall ─────────────

internal fun DrawScope.neonScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    drawRect(Brush.verticalGradient(listOf(Color(0xFF16141A), Color(0xFF0E0D11))))
    // bricks
    val bh = h * 0.07f
    var row = 0
    var y = 0f
    while (y < h) {
        drawLine(Color(0xFF1F1C24), Offset(0f, y), Offset(w, y), 1f * dp)
        var x = if (row % 2 == 0) 0f else w * 0.06f
        while (x < w) {
            drawLine(Color(0xFF1F1C24), Offset(x, y), Offset(x, y + bh), 1f * dp)
            x += w * 0.12f
        }
        y += bh
        row++
    }
    val ijma = a.b("ijma")
    val banned = a.b("banned")
    val slaves = a.b("slaves")
    val allowed = a.b("allowed")
    val red = Color(0xFFFF4D5E)
    val warm = Color(0xFFFFE0A3)
    val back = Color(0xE6101015)
    val scale = 1.9f

    tagAt(a.tag("ijma"), Offset(w * 0.5f, h * 0.07f), seg(ijma, 0.2f, 0.7f), back = back, ink = ArtInk.Ivory)
    // names given in worship to other than Allah: they flicker on, then go dark — marked beside the name, never across it
    for (k in 0 until 2) {
        val tl = a.tag("banned", 0, k) ?: continue
        val c = Offset(w * (if (k == 0) 0.72f else 0.28f), h * 0.74f)
        val on = seg(banned, k * 0.15f, k * 0.15f + 0.25f)
        if (on <= 0f) continue
        val flick = if (banned < 0.6f) (if (sin(t * 23f + k * 3f) > -0.6f) 1f else 0.45f) else 1f
        neonWord(tl, c, scale, red, on * flick * (1f - 0.75f * seg(banned, 0.7f, 1f)))
        val mark = Offset(c.x - tl.size.width * scale / 2f - w * 0.05f, c.y)
        crossMark(mark, h * 0.025f, red, 2.2f * dp, seg(banned, 0.55f + 0.1f * k, 0.7f + 0.1f * k))
    }
    tagAt(a.tag("banned", 0, 2), Offset(w * 0.5f, h * 0.9f), seg(banned, 0.6f, 0.9f), back = back, ink = red)
    tagAt(a.tag("slaves"), Offset(w * 0.5f, h * 0.5f), seg(slaves, 0.2f, 0.7f), back = back, ink = warm)
    // names given in worship to Allah: steady, warm, high on the wall
    for (k in 0 until 2) {
        val tl = a.tag("allowed", 0, k) ?: continue
        val c = Offset(w * (if (k == 0) 0.72f else 0.28f), h * 0.27f)
        val on = easeOut(seg(allowed, k * 0.25f, k * 0.25f + 0.4f))
        if (on <= 0f) continue
        neonWord(tl, c, scale, warm, on * (0.95f + 0.05f * sin(t * 2f + k)))
        val mark = Offset(c.x - tl.size.width * scale / 2f - w * 0.05f, c.y)
        checkMark(mark, h * 0.025f, Color(0xFF86D6AE), 2.2f * dp, seg(allowed, 0.3f + 0.25f * k, 0.5f + 0.25f * k))
    }
}

private fun DrawScope.neonWord(tl: TextLayoutResult, c: Offset, scale: Float, color: Color, glow: Float) {
    if (glow <= 0.01f) return
    val rw = tl.size.width * scale * 0.62f
    drawOval(Brush.radialGradient(listOf(color.copy(alpha = 0.28f * glow), Color.Transparent), center = c, radius = rw),
        topLeft = c - Offset(rw, rw * 0.45f), size = Size(rw * 2f, rw * 0.9f))
    // the glass tube: a soft, wider copy under the bright line
    bigText(tl, c, scale * 1.04f, color, glow * 0.4f)
    bigText(tl, c, scale, lerpC(color, Color.White, 0.35f), glow)
}

// ───────────── a flat picture with robed silhouettes: the oath ─────────────

/** A robed, faceless silhouette: a disc for the head, a long robe to the feet. */
private fun DrawScope.robed(foot: Offset, tall: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val shoulder = foot.y - tall * 0.72f
    val robe = Path().apply {
        moveTo(foot.x - tall * 0.12f, shoulder + tall * 0.04f)
        quadraticBezierTo(foot.x, shoulder - tall * 0.05f, foot.x + tall * 0.12f, shoulder + tall * 0.04f)
        lineTo(foot.x + tall * 0.17f, foot.y)
        lineTo(foot.x - tall * 0.17f, foot.y)
        close()
    }
    drawPath(robe, color, alpha = alpha)
    drawCircle(color, radius = tall * 0.1f, center = Offset(foot.x, shoulder - tall * 0.13f), alpha = alpha)
}

internal fun DrawScope.oathScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val dark = a.dark
    val pp = Paper(dark)
    val ground = h * 0.86f
    drawRect(Brush.verticalGradient(if (dark) listOf(Color(0xFF1B1F2A), Color(0xFF151821)) else listOf(Color(0xFFEDE3D1), Color(0xFFDCCDB3))))
    drawRect(if (dark) Color(0xFF0F1117) else Color(0xFFCDBB9C), topLeft = Offset(0f, ground), size = Size(w, h - ground))
    val ink = pp.ink
    val block = if (dark) Color(0xFFB8893F) else Color(0xFFC9A15E)

    val swear = a.b("swear")
    val pardon = a.b("pardon")
    val topple = a.b("topple")
    val plea = a.b("plea")
    val tasbih = a.b("tasbih")
    val act1 = seg(swear, 0f, 0.3f) * (1f - seg(plea, 0f, 0.3f))

    if (act1 > 0.01f) {
        val swearer = Offset(w * 0.62f, ground)
        val other = Offset(w * 0.25f, ground)
        val tall = h * 0.42f
        // the worshipper's deeds, stacked behind him — they fall when his deeds are annulled
        val fall = easeIn2(seg(topple, 0.1f, 0.8f))
        for (k in 0 until 7) {
            val bw = w * 0.07f
            val bh = h * 0.06f
            val base = Offset(w * 0.84f, ground - bh * (k + 0.5f))
            val drop = Offset((k - 3) * w * 0.02f * fall + sin(k * 2.1f) * w * 0.03f * fall, (ground - bh * 0.5f - base.y) * fall)
            rotate(degrees = fall * (k - 3) * 9f, pivot = base + drop) {
                drawRect(block, topLeft = base + drop - Offset(bw / 2, bh / 2), size = Size(bw, bh * 0.92f), alpha = act1)
                drawRect(ink, topLeft = base + drop - Offset(bw / 2, bh / 2), size = Size(bw, bh * 0.92f), alpha = act1 * 0.5f, style = Stroke(1f * dp))
            }
        }
        robed(swearer, tall, ink, act1)
        // the other one: dim at first; light falls on him when he is forgiven
        val lit = easeOut(seg(pardon, 0.2f, 0.8f))
        if (lit > 0f) {
            val beam = Path().apply {
                moveTo(other.x - w * 0.03f, 0f)
                lineTo(other.x + w * 0.03f, 0f)
                lineTo(other.x + w * 0.13f, ground)
                lineTo(other.x - w * 0.13f, ground)
                close()
            }
            val ray = if (dark) Color(0xFFFFE7A8) else Color(0xFFFFF7DF)
            drawPath(beam, Brush.verticalGradient(listOf(ray.copy(alpha = 0f), ray.copy(alpha = if (dark) 0.35f else 0.75f))), alpha = lit * act1)
        }
        robed(other, tall * 0.92f, lerpC(pp.faint, ink, lit), act1)
        note(a.tag("swear"), Offset(w * 0.55f, h * 0.16f), act1 * seg(swear, 0.2f, 0.5f), pp, ink = ink, boxed = true)
        note(a.tag("pardon"), Offset(other.x, h * 0.36f), act1 * seg(pardon, 0.4f, 0.9f), pp, ink = pp.good)
        note(a.tag("topple"), Offset(w * 0.82f, h * 0.34f), act1 * seg(topple, 0.5f, 1f), pp, ink = pp.bad)
    }
    // the plea «we seek Allah's intercession with you» — rejected: «سبحان الله»
    val act2 = seg(plea, 0.1f, 0.4f)
    if (act2 > 0.01f) {
        note(a.tag("plea"), Offset(w * 0.5f, h * 0.52f), act2 * (1f - 0.55f * seg(tasbih, 0.3f, 0.8f)), pp, ink = ink, boxed = true)
        if (tasbih > 0f) {
            val glow = easeOut(seg(tasbih, 0f, 0.6f))
            val tc = Offset(w * 0.5f, h * 0.24f)
            drawCircle(Brush.radialGradient(listOf(pp.amber.copy(alpha = 0.3f), Color.Transparent), center = tc, radius = w * 0.3f), radius = w * 0.3f, center = tc, alpha = glow)
            bigText(a.tag("tasbih", 0, 0), tc, 2.4f, ink, glow)
            note(a.tag("tasbih", 0, 1), Offset(w * 0.5f, h * 0.66f), seg(tasbih, 0.5f, 0.9f), pp, ink = pp.bad)
        }
    }
}

// ───────────── a map on parchment: whom the one who worships Allah alone loves and disowns ─────────────

internal fun DrawScope.kinScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val pp = Paper(a.dark)
    drawRect(pp.bg)
    // parchment: soft fibres and a vignette instead of a grid
    for (k in 0 until 40) {
        val x = fract(k * 0.7548777f + 0.13f) * w
        val y = fract(k * 0.5698403f + 0.29f) * h
        drawLine(pp.faint, Offset(x, y), Offset(x + w * 0.06f, y + h * 0.005f), 0.6f * dp, alpha = 0.18f)
    }
    drawRect(Brush.radialGradient(listOf(Color.Transparent, pp.faint.copy(alpha = 0.25f)), center = Offset(w / 2, h / 2), radius = w * 0.75f))

    val self = a.b("self")
    val foe = a.b("foe")
    val kin = a.b("kin")
    val love = a.b("love")
    val hate = a.b("hate")
    val centre = Offset(w * 0.52f, h * 0.58f)
    val foeAt = Offset(w * 0.2f, h * 0.2f)
    val friends = listOf(Offset(w * 0.84f, h * 0.22f), Offset(w * 0.88f, h * 0.62f), Offset(w * 0.68f, h * 0.9f), Offset(w * 0.2f, h * 0.84f))

    // threads of love for Allah's sake
    if (love > 0f) {
        for ((k, f) in friends.withIndex()) {
            val p = easeOut(seg(love, k * 0.12f, k * 0.12f + 0.5f))
            if (p <= 0f) continue
            penLine(listOf(centre, centre + (f - centre) * p), 1f, pp.amber, 2f * dp)
            drawOval(pp.card, topLeft = f - Offset(w * 0.06f, h * 0.045f), size = Size(w * 0.12f, h * 0.09f), alpha = p)
            drawOval(pp.amber, topLeft = f - Offset(w * 0.06f, h * 0.045f), size = Size(w * 0.12f, h * 0.09f), alpha = p, style = Stroke(1.4f * dp))
            drawCircle(pp.amber, radius = 3f * dp, center = f, alpha = p)
        }
        note(a.tag("love"), Offset(w * 0.8f, h * 0.42f), seg(love, 0.4f, 0.9f), pp, ink = pp.amber)
    }
    // the thread to the one who opposes Allah and His Messenger — cut, however close he is
    if (foe > 0f) {
        val p = easeOut(seg(foe, 0f, 0.5f))
        val cut = easeOut(seg(kin, 0.3f, 0.8f))
        val near = easeInOut(seg(kin, 0f, 0.4f))
        val at = foeAt + (Offset(w * 0.3f, h * 0.36f) - foeAt) * (near * 0.4f)
        if (cut <= 0f) {
            penLine(listOf(centre, centre + (at - centre) * p), 1f, pp.ink, 1.6f * dp)
        } else {
            val mid = centre + (at - centre) * 0.5f
            val gap = (at - centre) / (at - centre).getDistance() * (w * 0.03f * cut)
            val sag = Offset(0f, h * 0.05f * cut)
            val col = lerpC(pp.ink, pp.bad, easeOut(hate))
            penLine(listOf(centre, mid - gap + sag), 1f, col, 1.6f * dp)
            penLine(listOf(at, mid + gap + sag), 1f, col, 1.6f * dp)
        }
        drawOval(pp.card, topLeft = at - Offset(w * 0.07f, h * 0.05f), size = Size(w * 0.14f, h * 0.1f), alpha = p)
        drawOval(lerpC(pp.ink, pp.bad, cut), topLeft = at - Offset(w * 0.07f, h * 0.05f), size = Size(w * 0.14f, h * 0.1f), alpha = p, style = Stroke(1.4f * dp))
        note(a.tag("foe"), Offset(at.x + w * 0.06f, at.y - h * 0.1f), seg(foe, 0.3f, 0.8f), pp, ink = pp.ink)
        note(a.tag("kin"), Offset(at.x + w * 0.05f, at.y + h * 0.11f), seg(kin, 0.3f, 0.8f) * (1f - seg(love, 0f, 0.3f)), pp, ink = pp.bad)
        if (hate > 0f) note(a.tag("hate"), centre + (at - centre) * 0.5f + Offset(w * 0.12f, h * 0.02f), seg(hate, 0.3f, 0.8f), pp, ink = pp.bad)
    }
    // the believer at the centre
    if (self > 0f) {
        val al = seg(self, 0f, 0.4f)
        drawOval(pp.card, topLeft = centre - Offset(w * 0.1f, h * 0.07f), size = Size(w * 0.2f, h * 0.14f), alpha = al)
        drawOval(pp.ink, topLeft = centre - Offset(w * 0.1f, h * 0.07f), size = Size(w * 0.2f, h * 0.14f), alpha = al, style = Stroke(1.8f * dp))
        note(a.tag("self"), centre, seg(self, 0.2f, 0.7f), pp, ink = pp.ink)
    }
}
