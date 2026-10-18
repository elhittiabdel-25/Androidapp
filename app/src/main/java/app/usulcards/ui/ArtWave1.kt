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
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.drawText
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Scene templates shared by several cards: lanterns in the wind, a trail with its forks and footprints, a ridge
 * between two ravines, a tree, and streams to one basin. Which elements appear, and when, comes from the
 * card's beats (kind, target `side`, strength `value`); see tools/previews/ART_PLAN.md.
 */

internal class Twinkle(val x: Float, val y: Float, val r: Float, val p: Float)

internal val TWINKLES: List<Twinkle> = run {
    var seed = 11L
    fun rnd(): Float {
        seed = (seed * 16807L) % 2147483647L
        return seed / 2147483647f
    }
    List(60) { Twinkle(rnd(), rnd(), 0.5f + rnd() * 1.1f, rnd() * 6.3f) }
}

/** Night sky with twinkling stars down to [starsTo] of the height. */
internal fun DrawScope.nightSky(t: Float, top: Color, bottom: Color, starsTo: Float, starAlpha: Float = 1f) {
    drawRect(Brush.verticalGradient(listOf(top, bottom)))
    for (s in TWINKLES) {
        val a = (0.3f + 0.7f * (0.5f + 0.5f * sin(t * 1.5f + s.p))) * starAlpha
        drawCircle(ArtInk.Ivory.copy(alpha = a.coerceIn(0f, 1f)), radius = s.r * density, center = Offset(s.x * size.width, s.y * size.height * starsTo))
    }
}

/** Half the drawn width of a tag, with its padding (0 when there is none). */
private fun DrawScope.halfTagW(tl: androidx.compose.ui.text.TextLayoutResult?): Float = if (tl == null) 0f else tl.size.width / 2f + 6f * density

/** Small rounded marks of light: footprints, seen from the side. */
private fun DrawScope.footprint(c: Offset, color: Color, alpha: Float, scale: Float = 1f) {
    if (alpha <= 0.01f) return
    val w = 5f * density * scale
    val h = 2.6f * density * scale
    drawOval(color.copy(alpha = alpha.coerceIn(0f, 1f)), topLeft = Offset(c.x - w / 2, c.y - h / 2), size = Size(w, h))
}

/** A cross drawn over a tag or a thing that is refused. */
private fun DrawScope.crossAt(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val col = ArtInk.Ember.copy(alpha = alpha.coerceIn(0f, 1f))
    drawLine(col, c + Offset(-r, -r), c + Offset(r, r), 2f * density, StrokeCap.Round)
    drawLine(col, c + Offset(r, -r), c + Offset(-r, r), 2f * density, StrokeCap.Round)
}

// ───────────── lanterns in the wind ─────────────

private object LampInk {
    val SkyTop = Color(0xFF111833)
    val SkyLow = Color(0xFF2C3766)
    val Sill = Color(0xFF1A1D33)
    val SillEdge = Color(0xFF454B75)
    val GustOut = Color(0xFFC2694C)
    val GustDim = Color(0xFF97A3C8)
    val GustSoft = Color(0xFFD4DBF2)
}

/**
 * One to five lanterns on a sill at night. Beats: lamp (a lantern; value 1 = under glass, 2 = dim), gust
 * (wind on the `side` lantern or all with -1; value 0 shakes it, 1 dims it, 2 puts it out unless under glass),
 * glass, dome (light of Allah's will over it), grow, dim, ember (only a spark stays), out, shatter, say (a tag).
 */
internal fun DrawScope.lanternScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    nightSky(t, LampInk.SkyTop, LampInk.SkyLow, 0.75f)
    val sill = h * 0.8f
    drawRect(LampInk.Sill, topLeft = Offset(0f, sill), size = Size(w, h - sill))
    drawLine(LampInk.SillEdge, Offset(0f, sill), Offset(w, sill), 1.5f * dp)
    var zx = w * 0.04f
    while (zx < w) {
        drawPath(starPath(Offset(zx, sill + (h - sill) * 0.5f), (h - sill) * 0.18f), ArtInk.Brass.copy(alpha = 0.22f))
        zx += w * 0.08f
    }

    val n = max(1, a.count("lamp"))
    val s = when (n) {
        1 -> w * 0.05f
        2 -> w * 0.045f
        3 -> w * 0.04f
        else -> w * 0.032f
    }
    val xs = FloatArray(n) { k -> w - w * (k + 1f) / (n + 1f) }      // first lantern on the right
    val ringY = sill - 5.35f * s
    val bodyY = ringY + 2.9f * s

    // each lantern's state, beat by beat in time order
    val appear = FloatArray(n)
    val flame = FloatArray(n)
    val glass = FloatArray(n)
    val dome = FloatArray(n)
    val shatter = FloatArray(n)
    val shake = FloatArray(n)
    val gaze = FloatArray(n)
    val hidden = FloatArray(n)
    val ret = FloatArray(n)
    val charm = FloatArray(n)
    val bind = FloatArray(n)
    val untieBad = FloatArray(n)
    val untieGood = FloatArray(n)
    var crowd = 0f
    val topTag = IntArray(n) { -1 }
    var centerTag = -1
    var lampNo = 0
    for (i in 0 until a.size) {
        val p = a.beatAt(i)
        val kind = a.kindAt(i)
        if (kind == "lamp") {
            val k = lampNo++
            if (k < n) {
                appear[k] = easeOut(p)
                val v = a.valueAt(i)
                flame[k] = when {
                    v >= 3f -> v / 10f          // its own brightness, from 0.3 to 1.4
                    v >= 2f -> 0.35f
                    else -> 0.85f
                }
                if (v in 0.5f..1.5f) glass[k] = easeOut(p)
            }
            continue
        }
        if (p <= 0f) continue
        val tg = a.sideAt(i)
        for (k in 0 until n) {
            if (tg != k && tg >= 0) continue
            val guarded = glass[k] > 0.5f
            when (kind) {
                "glass" -> glass[k] = max(glass[k], easeOut(p))
                "dome" -> dome[k] = max(dome[k], easeOut(p))
                "grow" -> flame[k] += 0.45f * easeOut(p)
                "dim" -> if (!guarded) flame[k] -= 0.4f * easeOut(p)
                "ember" -> flame[k] = mix(flame[k], 0.16f, easeOut(p))
                "out" -> if (!guarded || tg == k) flame[k] *= 1f - easeOut(p)
                "gaze" -> {
                    gaze[k] = easeOut(p)
                    flame[k] += 0.25f * easeOut(p)
                }
                "hidden" -> hidden[k] = easeOut(p)
                "return" -> {
                    ret[k] = p
                    flame[k] -= 0.3f * easeOut(seg(p, 0.6f, 1f))
                }
                "charm" -> charm[k] = p
                "bind" -> {
                    bind[k] = easeOut(p)
                    flame[k] = mix(flame[k], 0.3f, easeOut(p))
                }
                "untie" -> if (a.valueAt(i) >= 1f) {
                    untieGood[k] = p
                    flame[k] = mix(flame[k], 0.95f, easeOut(seg(p, 0.4f, 1f)))
                } else untieBad[k] = p
                "many" -> crowd = max(crowd, p)
                "shatter" -> {
                    shatter[k] = p
                    flame[k] *= 1f - easeOut(seg(p, 0.15f, 0.6f))
                    glass[k] = 0f
                }
                "gust" -> {
                    val v = a.valueAt(i)
                    shake[k] += bump(p) * (0.5f + 0.35f * v)
                    if (!guarded) {
                        if (v >= 2f) flame[k] *= 1f - easeOut(seg(p, 0.3f, 0.85f))
                        else if (v >= 1f) flame[k] -= 0.4f * easeOut(seg(p, 0.3f, 0.85f))
                    }
                }
            }
        }
        if ((kind != "gust" || a.valueAt(i) >= 2f) && kind != "bond" && kind != "sever" && a.parts(i) > 0) {
            if (tg in 0 until n) topTag[tg] = i else centerTag = i
        }
    }

    // bonds of light between lamps (side: from, value: to); a severed bond breaks and its halves fall away
    class Bond(val from: Int, val to: Int) {
        var p = 0f
        var cut = 0f
        var tag = -1
    }
    val bonds = ArrayList<Bond>()
    for (i in 0 until a.size) {
        val kind = a.kindAt(i)
        if (kind != "bond" && kind != "sever") continue
        val f = a.sideAt(i).coerceIn(0, n - 1)
        val to = a.valueAt(i).toInt().coerceIn(0, n - 1)
        val b = bonds.firstOrNull { it.from == f && it.to == to } ?: Bond(f, to).also { bonds += it }
        val p = a.beatAt(i)
        if (kind == "bond") b.p = max(b.p, p) else {
            b.p = max(b.p, seg(p, 0f, 0.4f))
            b.cut = max(b.cut, seg(p, 0.45f, 1f))
        }
        if (p > 0f && a.parts(i) > 0) b.tag = i
    }
    for (b in bonds) {
        if (b.p <= 0f || appear[b.from] <= 0f) continue
        val p0 = Offset(xs[b.from], bodyY)
        val p2 = Offset(xs[b.to], bodyY)
        val span = kotlin.math.abs(b.to - b.from) / max(1f, n - 1f)
        val ctrl = Offset((p0.x + p2.x) / 2, bodyY - h * (0.2f + 0.35f * span))
        fun q(u: Float) = Offset((1 - u) * (1 - u) * p0.x + 2 * (1 - u) * u * ctrl.x + u * u * p2.x, (1 - u) * (1 - u) * p0.y + 2 * (1 - u) * u * ctrl.y + u * u * p2.y)
        if (b.cut <= 0f) {
            val reveal = easeOut(b.p)
            val path = Path()
            for (k in 0..24) {
                val c = q(reveal * k / 24f)
                if (k == 0) path.moveTo(c.x, c.y) else path.lineTo(c.x, c.y)
            }
            drawPath(path, ArtInk.Gold.copy(alpha = 0.25f), style = Stroke(6f * dp, cap = StrokeCap.Round))
            drawPath(path, ArtInk.Gold, style = Stroke(1.6f * dp, cap = StrokeCap.Round))
        } else {
            for (half in 0..1) {
                val path = Path()
                for (k in 0..12) {
                    val u = if (half == 0) 0.5f * k / 12f else 1f - 0.5f * k / 12f
                    val c = q(u)
                    val drop = (0.5f - kotlin.math.abs(u - 0.5f)) * 2f * b.cut * h * 0.3f
                    if (k == 0) path.moveTo(c.x, c.y + drop) else path.lineTo(c.x, c.y + drop)
                }
                drawPath(path, ArtInk.Ember.copy(alpha = 1f - 0.65f * b.cut), style = Stroke(1.6f * dp, cap = StrokeCap.Round))
            }
        }
        if (b.tag >= 0) tagAt(a.tagOf(b.tag), q(0.5f) + Offset(0f, if (b.cut > 0f) -h * 0.03f else -h * 0.15f), seg(a.beatAt(b.tag), 0.3f, 1f), back = Color(0xE6101426), ink = if (b.cut > 0f) ArtInk.Ember else ArtInk.Gold)
    }

    for (k in 0 until n) {
        if (appear[k] <= 0f) continue
        val x = xs[k] + sin(t * 17f + k) * 2.2f * dp * min(1f, shake[k])
        // the light of Allah's will over it
        if (dome[k] > 0f) {
            val r = 3.9f * s
            clipRect(bottom = sill) {
                drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.32f * dome[k]), Color.Transparent), center = Offset(xs[k], sill), radius = r * 1.3f), radius = r * 1.3f, center = Offset(xs[k], sill))
                drawCircle(ArtInk.Gold.copy(alpha = 0.6f * dome[k]), radius = r, center = Offset(xs[k], sill), style = Stroke(1.4f * dp))
            }
        }
        val fl = (0.07f * sin(t * 9f + k) + 0.05f * sin(t * 13.3f + 2f * k)) + shake[k] * 0.35f * sin(t * 23f)
        val sc = 0.55f + 0.45f * appear[k]
        withTransform({ scale(sc, sc, pivot = Offset(xs[k], sill)) }) {
            lantern(Offset(x, ringY), s, (flame[k] * appear[k]).coerceIn(0f, 1.4f), fl)
        }
        val lc = Offset(xs[k], bodyY)
        // eyes on it: beams from both sides, and it decks itself out for them
        if (gaze[k] > 0f) {
            for (sd in listOf(-1f, 1f)) {
                val src = Offset(xs[k] + sd * w * 0.42f, h * 0.02f)
                val beam = Path().apply {
                    moveTo(src.x, src.y)
                    lineTo(lc.x + sd * 1.6f * s, lc.y - 1.2f * s)
                    lineTo(lc.x + sd * 0.6f * s, lc.y + 1.4f * s)
                    close()
                }
                drawPath(beam, ArtInk.Ivory.copy(alpha = 0.12f * gaze[k]))
            }
            for (m in 0 until 6) {
                val ang = (m * 60f + t * 40f) * PI.toFloat() / 180f
                val c = lc + Offset(cos(ang) * 1.5f * s, sin(ang) * 1.9f * s)
                drawPath(starPath(c, 2.2f * dp * (0.7f + 0.3f * sin(t * 6f + m))), lerp(ArtInk.Gold, ArtInk.Ember, 0.4f).copy(alpha = gaze[k]))
            }
        }
        // a hidden ember inside the flame
        if (hidden[k] > 0f) {
            val pulse = 0.6f + 0.4f * sin(t * 5f)
            drawCircle(Brush.radialGradient(listOf(ArtInk.Ember.copy(alpha = 0.9f * hidden[k] * pulse), Color.Transparent), center = lc + Offset(0f, 0.25f * s), radius = 0.9f * s), radius = 0.9f * s, center = lc + Offset(0f, 0.25f * s))
        }
        // its light rises, and is given back
        if (ret[k] > 0f && ret[k] < 1f) {
            val q = ret[k]
            val y = if (q < 0.55f) mix(bodyY, h * 0.08f, easeOut(q / 0.55f)) else mix(h * 0.08f, bodyY, easeInOut((q - 0.55f) / 0.45f))
            val col = if (q < 0.55f) ArtInk.Gold else ArtInk.Ember
            val c = Offset(xs[k], y)
            drawCircle(Brush.radialGradient(listOf(col.copy(alpha = 0.7f), Color.Transparent), center = c, radius = 14f * dp), radius = 14f * dp, center = c)
            drawCircle(ArtInk.Ivory, radius = 3f * dp, center = c)
        }
        // amulets hung on it: struck out
        if (charm[k] > 0f) {
            val q = easeOut(seg(charm[k], 0f, 0.5f))
            val top = Offset(xs[k] + 1.3f * s, ringY + 1.5f * s)
            drawLine(ArtInk.Ivory.copy(alpha = 0.7f * q), top, top + Offset(0f, 2.2f * s * q), 1f * dp)
            drawCircle(ArtInk.Brass.copy(alpha = q), radius = 0.32f * s, center = top + Offset(0f, 2.4f * s * q), style = Stroke(1.4f * dp))
            drawRect(lerp(ArtInk.Brass, ArtInk.Ember, 0.4f).copy(alpha = q), topLeft = top + Offset(-0.22f * s, 2.8f * s * q), size = Size(0.44f * s, 0.6f * s))
            val x = seg(charm[k], 0.5f, 0.9f)
            if (x > 0f) {
                val c = top + Offset(0f, 2.8f * s)
                val r = 0.9f * s
                drawLine(ArtInk.Ember, c + Offset(-r, -r), c + Offset(-r + 2 * r * seg(x, 0f, 0.5f), -r + 2 * r * seg(x, 0f, 0.5f)), 2.2f * dp, StrokeCap.Round)
                if (x > 0.5f) drawLine(ArtInk.Ember, c + Offset(r, -r), c + Offset(r - 2 * r * seg(x, 0.5f, 1f), -r + 2 * r * seg(x, 0.5f, 1f)), 2.2f * dp, StrokeCap.Round)
            }
        }
        // a knotted cord binding it; undone by light, or crossed with another knot
        if (bind[k] > 0f) {
            val fallAway = easeOut(seg(untieGood[k], 0.3f, 1f))
            val dy = fallAway * (sill - bodyY) * 0.8f
            val cordA = bind[k] * (1f - fallAway * 0.9f)
            val col = lerp(Color(0xFF6A4A6E), ArtInk.Jade, easeOut(seg(untieGood[k], 0f, 0.4f)))
            for (m in 0 until 3) {
                val y = bodyY - 0.9f * s + m * 0.9f * s + dy
                drawLine(col.copy(alpha = cordA), Offset(xs[k] - 1.25f * s, y), Offset(xs[k] + 1.25f * s, y + 0.25f * s), 2f * dp, StrokeCap.Round)
                drawCircle(col.copy(alpha = cordA), radius = 0.22f * s, center = Offset(xs[k] + (m - 1) * 0.5f * s, y + 0.12f * s))
            }
            if (untieBad[k] > 0f) {
                val q = easeOut(seg(untieBad[k], 0f, 0.5f)) * (1f - fallAway * 0.9f)
                for (m in 0 until 2) {
                    val y = bodyY - 0.45f * s + m * 0.9f * s + dy
                    drawLine(Color(0xFF2A1A2E).copy(alpha = q), Offset(xs[k] - 1.3f * s, y + 0.2f * s), Offset(xs[k] + 1.3f * s, y - 0.1f * s), 2.4f * dp, StrokeCap.Round)
                }
                crossAt(lc + Offset(2.4f * s, -1.6f * s), 0.7f * s, seg(untieBad[k], 0.5f, 0.9f) * (1f - fallAway))
            }
        }
        // glass bell
        if (glass[k] > 0f) {
            val gw = 1.95f * s
            val gTop = ringY - 0.7f * s - (1f - glass[k]) * h * 0.25f
            val bell = Path().apply {
                moveTo(xs[k] - gw, sill)
                lineTo(xs[k] - gw, gTop + gw)
                cubicTo(xs[k] - gw, gTop - 0.15f * gw, xs[k] + gw, gTop - 0.15f * gw, xs[k] + gw, gTop + gw)
                lineTo(xs[k] + gw, sill)
            }
            drawPath(bell, ArtInk.Ivory.copy(alpha = 0.08f * glass[k]))
            drawPath(bell, ArtInk.Ivory.copy(alpha = 0.6f * glass[k]), style = Stroke(1.2f * dp))
            drawLine(ArtInk.Ivory.copy(alpha = 0.45f * glass[k]), Offset(xs[k] - gw * 0.7f, gTop + gw * 1.1f), Offset(xs[k] - gw * 0.7f, sill - gw), 1.6f * dp, StrokeCap.Round)
        }
        // shattered glass
        if (shatter[k] > 0f) {
            val q = easeOut(shatter[k])
            for (m in 0 until 10) {
                val ang = (m * 36f + 13f) * PI.toFloat() / 180f
                val d = q * w * (0.08f + 0.04f * (m % 3))
                val c = Offset(xs[k], bodyY) + Offset(cos(ang) * d, sin(ang) * d * 0.7f)
                val tri = Path().apply {
                    moveTo(c.x, c.y - 3f * dp)
                    lineTo(c.x + 2.6f * dp, c.y + 2f * dp)
                    lineTo(c.x - 2f * dp, c.y + 1.5f * dp)
                    close()
                }
                drawPath(tri, ArtInk.Ivory.copy(alpha = 0.8f * (1f - seg(shatter[k], 0.5f, 1f))))
            }
            // the cracks stay, and a few shards lie on the sill
            val cr = seg(shatter[k], 0.2f, 0.5f)
            val crack = Path().apply {
                moveTo(xs[k] - 0.55f * s, bodyY - 0.95f * s)
                lineTo(xs[k] - 0.05f * s, bodyY - 0.35f * s)
                lineTo(xs[k] - 0.35f * s, bodyY + 0.15f * s)
                lineTo(xs[k] + 0.4f * s, bodyY + 0.85f * s)
                moveTo(xs[k] - 0.05f * s, bodyY - 0.35f * s)
                lineTo(xs[k] + 0.5f * s, bodyY - 0.6f * s)
            }
            drawPath(crack, ArtInk.Ivory.copy(alpha = 0.85f * cr), style = Stroke(1.3f * dp, cap = StrokeCap.Round))
            for (m in 0 until 5) {
                val c = Offset(xs[k] + (m - 2f) * 0.75f * s + (m % 2) * 0.2f * s, sill - 1.2f * dp)
                val tri = Path().apply {
                    moveTo(c.x - 2.4f * dp, c.y)
                    lineTo(c.x + (m % 3 - 1) * 1.2f * dp, c.y - 2.6f * dp)
                    lineTo(c.x + 2.2f * dp, c.y)
                    close()
                }
                drawPath(tri, ArtInk.Ivory.copy(alpha = 0.55f * seg(shatter[k], 0.55f, 0.9f)))
            }
            val f = bump(seg(shatter[k], 0f, 0.35f))
            drawCircle(Brush.radialGradient(listOf(ArtInk.Ivory.copy(alpha = 0.8f * f), Color.Transparent), center = Offset(xs[k], bodyY), radius = 3f * s), radius = 3f * s, center = Offset(xs[k], bodyY))
        }
    }

    // many more lights, small, along the sill
    if (crowd > 0f) {
        for (m in 0 until 18) {
            val q = easeOut(seg(crowd, m * 0.03f, m * 0.03f + 0.4f))
            if (q <= 0f) continue
            val x = w * (0.05f + 0.9f * fract(m * 0.618f + 0.11f))
            val y = sill - h * (0.035f + 0.02f * (m % 3))
            val c = Offset(x, y)
            drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.5f * q), Color.Transparent), center = c, radius = 8f * dp), radius = 8f * dp, center = c)
            drawCircle(ArtInk.Ivory.copy(alpha = q * (0.75f + 0.25f * sin(t * 3f + m))), radius = 1.8f * dp, center = c)
        }
    }

    // gusts: streams of wind from the sides, their names stacked by the edge they come from
    var g = 0
    val many = a.count("gust") > 2
    for (i in 0 until a.size) {
        if (a.kindAt(i) != "gust") continue
        val p = a.beatAt(i)
        val fromLeft = many && g % 2 == 1
        val row = if (many) g / 2 else g
        g++
        if (p <= 0f) continue
        val tg = a.sideAt(i)
        val target = if (tg in 0 until n) Offset(xs[tg], bodyY) else Offset(xs.average().toFloat(), bodyY)
        val edge = if (fromLeft) -6f * dp else w + 6f * dp
        val v = a.valueAt(i)
        val col = when {
            v >= 2f -> LampInk.GustOut
            v >= 1f -> LampInk.GustDim
            else -> LampInk.GustSoft
        }
        val reveal = easeOut(seg(p, 0f, 0.5f))
        val fade = 1f - seg(p, 0.65f, 1f)
        if (fade > 0f) {
            for (j in 0 until 3) {
                val y0 = target.y + (j - 1) * h * 0.07f - h * 0.05f
                val p0 = Offset(edge, y0)
                val p3 = target + Offset(0f, (j - 1) * h * 0.02f)
                val p1 = Offset(mix(edge, target.x, 0.35f), y0 - h * 0.08f)
                val p2 = Offset(mix(edge, target.x, 0.7f), p3.y + h * 0.05f)
                drawPath(
                    cubicPart(p0, p1, p2, p3, 0f, reveal),
                    col.copy(alpha = 0.75f * fade),
                    style = Stroke(1.6f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(7f * dp, 5f * dp), -t * 40f * dp)),
                )
            }
        }
        val tx = if (fromLeft) w * 0.19f else w * 0.81f
        val ty = if (many) h * (0.09f + 0.125f * row) else h * (0.12f + 0.12f * row)
        if (v < 2f) tagAt(a.tagOf(i), Offset(tx, ty), seg(p, 0f, 0.3f), back = Color(0xD9101426), ink = if (v >= 1f) ArtInk.Ember else ArtInk.Ivory)
    }

    // tags: names on the sill (or above the lamp when `side` is 1), the latest word over each lantern
    lampNo = 0
    var namesAbove = false
    for (i in 0 until a.size) {
        if (a.kindAt(i) != "lamp") continue
        val k = lampNo++
        if (k >= n) continue
        if (a.sideAt(i) == 1) {
            namesAbove = true
            val lift = if (n >= 5 && k % 2 == 1) h * 0.09f else 0f
            tagAt(a.tagOf(i), Offset(xs[k], ringY - h * 0.08f - lift), seg(a.beatAt(i), 0.2f, 0.8f), back = Color(0xE6101426), ink = ArtInk.Gold)
        } else {
            tagAt(a.tagOf(i), Offset(xs[k], sill + (h - sill) * 0.5f), seg(a.beatAt(i), 0.2f, 0.8f), back = Color(0xE6101426))
        }
    }
    val wordY = ringY - h * (if (namesAbove) (if (n >= 5) 0.27f else 0.18f) else 0.1f)
    for (k in 0 until n) {
        val i = topTag[k]
        if (i >= 0) tagAt(a.tagOf(i), Offset(xs[k], wordY), seg(a.beatAt(i), 0.2f, 0.8f), back = Color(0xE6101426), ink = toneInk(a.kindAt(i), a.valueAt(i)))
    }
    if (centerTag >= 0) {
        tagAt(a.tagOf(centerTag), Offset(xs.average().toFloat(), max(h * 0.07f, wordY - h * (if (topTag.any { it >= 0 } || namesAbove) 0.12f else 0.02f))), seg(a.beatAt(centerTag), 0.2f, 0.8f), back = Color(0xE6101426), ink = toneInk(a.kindAt(centerTag), a.valueAt(centerTag)))
    }
}

/** Ink of a tag by what its beat does: warnings in ember, protection and favour in gold or jade. */
private fun toneInk(kind: String, value: Float): Color = when (kind) {
    "out", "shatter", "dim", "gust", "gaze", "hidden", "return", "charm", "bind" -> ArtInk.Ember
    "glass", "dome" -> ArtInk.Gold
    "grow", "ember" -> ArtInk.Jade
    else -> when (value.toInt()) {
        1 -> ArtInk.Ember
        2 -> ArtInk.Gold
        3 -> ArtInk.Jade
        else -> ArtInk.Ivory
    }
}

// ───────────── the trail ─────────────

private object TrailInk {
    val SkyTop = Color(0xFF233062)
    val SkyLow = Color(0xFFE6A673)
    val DayTop = Color(0xFF6FA6D8)
    val DayLow = Color(0xFFF3DDB0)
    val NightTop = Color(0xFF0E1430)
    val NightLow = Color(0xFF26305A)
    val Hills = Color(0xFF3E3460)
    val Land = Color(0xFF2E2A47)
    val LandDay = Color(0xFF6E8A4E)
    val LandNight = Color(0xFF1C1B30)
    val Path = Color(0xFFD9C08E)
    val Post = Color(0xFF2A1C12)
    val Stone = Color(0xFF221E33)
}

/**
 * A trail seen from the side, walked from right to left (the way Arabic reads). Beats: origin, up (a beam to
 * the sky), lamp (a lamp post lighting the way on), mark ×n (signposts), good / bad (the fork's two ends),
 * follow / follow2 (one set of steps, then another stepping exactly into it), hole, stray, idols, keep (a
 * lantern carried to the end on a lit road), omen, turn (steps turn back), word (a good word lights the way),
 * goal (the place it leads), lands (from a dark land to a lit one), open, stuck (one who cannot leave: excused).
 */
internal fun DrawScope.trailScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val lands = easeOut(a.b("lands"))
    val hasLands = a.count("lands") > 0
    val horizon = h * 0.6f

    // sky: dusk, or night on the right and day on the left when the trail crosses two lands
    if (hasLands) {
        drawRect(Brush.verticalGradient(listOf(TrailInk.NightTop, TrailInk.NightLow), endY = horizon), size = Size(w, horizon))
        val dayW = w * 0.52f * lands
        drawRect(Brush.verticalGradient(listOf(TrailInk.DayTop, TrailInk.DayLow), endY = horizon), size = Size(dayW, horizon))
        drawRect(Brush.horizontalGradient(listOf(TrailInk.DayLow.copy(alpha = 0.5f * lands), Color.Transparent), startX = dayW, endX = dayW + 26f * dp), topLeft = Offset(dayW, 0f), size = Size(26f * dp, horizon))
        for (s in TWINKLES) {
            if (s.x < 0.55f) continue
            drawCircle(ArtInk.Ivory.copy(alpha = 0.6f * (0.5f + 0.5f * sin(t * 1.5f + s.p))), radius = s.r * dp, center = Offset(s.x * w, s.y * horizon * 0.9f))
        }
        val sun = Offset(w * 0.16f, h * 0.18f)
        drawCircle(Brush.radialGradient(listOf(Color(0xFFFFF0C2).copy(alpha = 0.8f * lands), Color.Transparent), center = sun, radius = w * 0.14f), radius = w * 0.14f, center = sun)
        drawCircle(Color(0xFFFFE9A8).copy(alpha = lands), radius = w * 0.04f, center = sun)
    } else {
        drawRect(Brush.verticalGradient(listOf(TrailInk.SkyTop, TrailInk.SkyLow), endY = horizon), size = Size(w, horizon))
    }
    hills(TrailInk.Hills, horizon - h * 0.02f, h * 0.03f, 0.013f, 0.031f, 0.8f)
    if (hasLands) {
        drawRect(TrailInk.LandNight, topLeft = Offset(0f, horizon), size = Size(w, h - horizon))
        drawRect(lerp(TrailInk.LandNight, TrailInk.LandDay, lands), topLeft = Offset(0f, horizon), size = Size(w * 0.5f, h - horizon))
    } else {
        drawRect(TrailInk.Land, topLeft = Offset(0f, horizon), size = Size(w, h - horizon))
    }

    // the trail: u = 0 at the right, 1 at the left
    val pathY = h * 0.76f
    fun pt(u: Float) = Offset(mix(w * 0.95f, w * 0.05f, u), pathY + h * 0.012f * sin(u * 6.3f))
    val road = Path().apply {
        for (k in 0..40) {
            val q = pt(k / 40f)
            if (k == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y)
        }
    }
    drawPath(road, TrailInk.Path.copy(alpha = 0.35f), style = Stroke(9f * dp, cap = StrokeCap.Round))
    drawPath(road, TrailInk.Path.copy(alpha = 0.55f), style = Stroke(1.2f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f * dp, 8f * dp))))

    // the fork: up to a garden, down to a fire
    val hasFork = a.count("good") > 0 || a.count("bad") > 0
    val forkU = 0.5f
    val forkP = pt(forkU)
    val gardenP = Offset(w * 0.12f, h * 0.5f)
    val fireP = Offset(w * 0.13f, h * 0.93f)
    val up1 = forkP + Offset(-w * 0.12f, -h * 0.02f)
    val up2 = gardenP + Offset(w * 0.12f, h * 0.1f)
    val dn1 = forkP + Offset(-w * 0.1f, h * 0.02f)
    val dn2 = fireP + Offset(w * 0.12f, -h * 0.04f)
    if (hasFork) {
        drawPath(cubicPart(forkP, up1, up2, gardenP, 0f, 1f), TrailInk.Path.copy(alpha = 0.3f), style = Stroke(7f * dp, cap = StrokeCap.Round))
        drawPath(cubicPart(forkP, dn1, dn2, fireP, 0f, 1f), TrailInk.Path.copy(alpha = 0.22f), style = Stroke(7f * dp, cap = StrokeCap.Round))
        val good = easeOut(a.b("good"))
        val bad = easeOut(a.b("bad"))
        // garden gate
        val gr = h * 0.12f
        drawCircle(Brush.radialGradient(listOf(ArtInk.Jade.copy(alpha = 0.25f + 0.45f * good), Color.Transparent), center = gardenP, radius = gr * 2.2f), radius = gr * 2.2f, center = gardenP)
        drawPath(horseshoe(gardenP.x, gardenP.y - gr * 0.55f, gr * 0.5f, gr * 0.42f, gardenP.y + gr * 0.2f), lerp(Color(0xFF2E4A3C), ArtInk.Jade, 0.3f + 0.6f * good))
        // fire gate
        drawCircle(Brush.radialGradient(listOf(ArtInk.Ember.copy(alpha = 0.15f + 0.45f * bad), Color.Transparent), center = fireP, radius = gr * 2f), radius = gr * 2f, center = fireP)
        drawPath(horseshoe(fireP.x, fireP.y - gr * 0.55f, gr * 0.5f, gr * 0.42f, fireP.y + gr * 0.2f), lerp(Color(0xFF3A1E18), ArtInk.Ember, 0.25f + 0.55f * bad))
        if (good > 0f) steps(forkP, up1, up2, gardenP, good, ArtInk.Ivory, t)
        if (bad > 0f) steps(forkP, dn1, dn2, fireP, bad, ArtInk.Ember, t)
        tagAt(a.tag("good"), gardenP + Offset(0f, -gr * 1.05f), seg(a.b("good"), 0.4f, 1f), back = Color(0xE6101426), ink = ArtInk.Jade)
        tagAt(a.tag("bad"), fireP + Offset(w * 0.12f, -gr * 0.2f), seg(a.b("bad"), 0.4f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
    }

    // origin: the first light, and a beam to the sky (what it was made for)
    val origin = a.b("origin")
    if (origin > 0f) {
        val o = pt(0f)
        drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.7f * origin), Color.Transparent), center = o, radius = 16f * dp), radius = 16f * dp, center = o)
        drawCircle(ArtInk.Ivory.copy(alpha = origin), radius = 3f * dp, center = o)
        tagAt(a.tag("origin"), o + Offset(-6f * dp, h * 0.11f), seg(origin, 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }
    val up = easeOut(a.b("up"))
    if (up > 0f) {
        val o = pt(0f)
        val top = Offset(o.x, mix(o.y, -4f, up))
        drawLine(ArtInk.Gold.copy(alpha = 0.25f), o, top, 6f * dp, StrokeCap.Round)
        drawLine(ArtInk.Gold.copy(alpha = 0.9f), o, top, 1.8f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp), t * 22f * dp))
        tagAt(a.tag("up"), Offset(o.x - w * 0.14f, h * 0.18f), seg(a.b("up"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }

    // the lamp post: guidance lighting the way ahead
    val lamp = easeOut(a.b("lamp"))
    val lampU = if (hasFork) forkU - 0.06f else 0.06f
    if (lamp > 0f) {
        val base = pt(lampU)
        val top = base + Offset(0f, -h * 0.26f)
        drawLine(TrailInk.Post, base, top, 2.6f * dp)
        val cone = Path().apply {
            moveTo(top.x, top.y)
            lineTo(top.x - w * 0.34f, base.y + 6f * dp)
            lineTo(top.x + w * 0.04f, base.y + 6f * dp)
            close()
        }
        drawPath(cone, Brush.verticalGradient(listOf(ArtInk.Gold.copy(alpha = 0.35f * lamp), Color.Transparent), startY = top.y, endY = base.y + 6f * dp))
        lantern(top + Offset(0f, -0.4f * w * 0.022f), w * 0.022f, lamp, 0.05f * sin(t * 9f))
        tagAt(a.tag("lamp"), top + Offset(0f, -h * 0.09f), seg(a.b("lamp"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }

    // signposts along the way, and the walker reaching each
    val marks = a.count("mark")
    for (m in 0 until marks) {
        val p = easeOut(a.b("mark", m))
        if (p <= 0f) continue
        val u = 0.22f + 0.2f * m
        val base = pt(u)
        val above = m % 2 == 0
        val postTop = base + Offset(0f, -h * (0.13f + 0.03f * p))
        drawLine(TrailInk.Post.copy(alpha = p), base, postTop, 2f * dp)
        // when the first two signs are swapped, each plate crosses over to the other post
        val sw = if (m < 2) easeInOut(a.b("swap")) else 0f
        val other = pt(0.22f + 0.2f * (1 - m))
        val plate = Offset(mix(postTop.x, other.x, sw), postTop.y - bump(sw) * h * (if (m == 0) 0.06f else -0.04f))
        drawRoundRect(lerp(TrailInk.Path, ArtInk.Ember, sw).copy(alpha = 0.85f * p), topLeft = plate + Offset(-6f * dp, -5f * dp), size = Size(12f * dp, 6f * dp), cornerRadius = CornerRadius(1.5f * dp, 1.5f * dp))
        tagAt(a.tag("mark", m), plate + Offset(0f, if (above) -h * 0.08f else -h * 0.17f), seg(a.b("mark", m), 0.3f, 1f), back = Color(0xE6101426), ink = if (sw > 0.5f) ArtInk.Ember else ArtInk.Gold)
    }
    if (marks > 0) {
        var reach = 0f
        for (m in 0 until marks) {
            val p = a.b("mark", m)
            if (p > 0f) reach = max(reach, mix(if (m == 0) 0f else 0.22f + 0.2f * (m - 1), 0.22f + 0.2f * m, easeOut(seg(p, 0f, 0.8f))))
        }
        walkSteps({ u -> pt(u) }, 0f, reach, ArtInk.Ivory, 1f, t)
    }

    // plain walking (with the two lands, or toward a goal)
    val walk = a.b("walk")
    val lands0 = if (hasLands) a.b("lands") else 0f
    val turn = easeOut(a.b("turn"))
    val word = easeOut(a.b("word"))
    if (hasLands) walkSteps({ u -> pt(u) }, 0f, 0.85f * easeOut(seg(lands0, 0.2f, 1f)), ArtInk.Ivory, 1f, t)
    if (walk > 0f) walkSteps({ u -> pt(u) }, 0f, 0.9f * easeOut(walk), ArtInk.Ivory, 1f, t)

    // goal at the far left
    val hasDoor = a.count("door") > 0
    val goalA = !hasDoor && (a.count("goal") > 0 || a.count("word") > 0 || a.count("turn") > 0)
    if (goalA) {
        val g = pt(0.97f)
        val lit = max(easeOut(a.b("goal")), word * 0.6f)
        drawRoundRect(Color(0xFF3A2A1E), topLeft = g + Offset(-12f * dp, -26f * dp), size = Size(24f * dp, 26f * dp), cornerRadius = CornerRadius(3f * dp, 3f * dp))
        drawPath(Path().apply { moveTo(g.x - 15f * dp, g.y - 25f * dp); lineTo(g.x, g.y - 36f * dp); lineTo(g.x + 15f * dp, g.y - 25f * dp); close() }, Color(0xFF4A3424))
        drawRoundRect(lerp(Color(0xFF1C140E), ArtInk.Gold, 0.3f + 0.7f * lit), topLeft = g + Offset(-4f * dp, -15f * dp), size = Size(8f * dp, 15f * dp), cornerRadius = CornerRadius(4f * dp, 4f * dp))
        if (lit > 0f) drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.4f * lit), Color.Transparent), center = g + Offset(0f, -10f * dp), radius = 30f * dp), radius = 30f * dp, center = g + Offset(0f, -10f * dp))
        tagAt(a.tag("goal"), g + Offset(w * 0.06f, -h * 0.25f), seg(a.b("goal"), 0.2f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }

    if (a.count("swap") > 0) tagAt(a.tag("swap"), pt(0.32f) + Offset(0f, -h * 0.36f), seg(a.b("swap"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)

    // the way shown by the lamp, all the way to the door
    val show = easeOut(a.b("show"))
    if (show > 0f) {
        val glowPath = Path().apply {
            for (k in 0..30) {
                val q = pt(0.06f + 0.9f * show * k / 30f)
                if (k == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y)
            }
        }
        drawPath(glowPath, ArtInk.Gold.copy(alpha = 0.35f), style = Stroke(10f * dp, cap = StrokeCap.Round))
        tagAt(a.tag("show"), pt(0.55f) + Offset(0f, -h * 0.1f), seg(a.b("show"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }
    // a door at the end that only light from above opens
    if (hasDoor) {
        val door = a.b("door")
        val g = pt(0.95f)
        val dj = w * 0.032f
        val dr = dj * 1.18f
        val dcy = g.y - h * 0.16f
        val beam = easeOut(seg(door, 0f, 0.4f))
        if (beam > 0f) {
            val ray = Path().apply {
                moveTo(g.x - w * 0.012f, 0f)
                lineTo(g.x + w * 0.012f, 0f)
                lineTo(g.x + dj * 1.3f, g.y)
                lineTo(g.x - dj * 1.3f, g.y)
                close()
            }
            drawPath(ray, Brush.verticalGradient(listOf(ArtInk.Gold.copy(alpha = 0.08f * beam), ArtInk.Gold.copy(alpha = 0.32f * beam)), startY = 0f, endY = g.y))
        }
        val open = easeOut(seg(door, 0.4f, 0.8f))
        drawPath(horseshoe(g.x, dcy, dr + 3f * dp, dj + 3f * dp, g.y), Color(0xFF6E5532))
        drawPath(horseshoe(g.x, dcy, dr, dj, g.y), lerp(Color(0xFF14101C), Color(0xFFFFF1C8), open))
        val leaf = dj * (1f - 0.8f * open)
        drawRect(Color(0xFF5A3D24), topLeft = Offset(g.x + dj - leaf, dcy - dr), size = Size(leaf, g.y - dcy + dr))
        drawRect(Color(0xFF5A3D24), topLeft = Offset(g.x - dj, dcy - dr), size = Size(leaf, g.y - dcy + dr))
        tagAt(a.tag("door"), Offset(g.x + w * 0.06f, dcy - dr - h * 0.1f), seg(door, 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }

    // an omen: a dark cloud and a sound, then the steps turn back
    val omen = easeOut(a.b("omen"))
    if (omen > 0f) {
        val c = Offset(w * 0.5f, h * 0.3f)
        for ((dx, dy, r) in listOf(Triple(-0.05f, 0.01f, 0.05f), Triple(0f, -0.02f, 0.065f), Triple(0.055f, 0.01f, 0.05f))) {
            drawCircle(Color(0xFF1E1A2C).copy(alpha = 0.9f * omen), radius = w * r, center = c + Offset(w * dx, h * dy))
        }
        for (k in 0 until 3) {
            val q = fract(t * 0.8f + k / 3f)
            drawCircle(ArtInk.Ivory.copy(alpha = 0.4f * omen * (1f - q)), radius = (6f + 26f * q) * dp, center = c + Offset(0f, h * 0.18f), style = Stroke(1.2f * dp))
        }
        walkSteps({ u -> pt(u) }, 0f, 0.42f * easeOut(seg(a.b("omen"), 0f, 0.9f)), ArtInk.Ivory, 1f, t)
        tagAt(a.tag("omen"), c + Offset(0f, -h * 0.13f), seg(a.b("omen"), 0.3f, 1f), back = Color(0xE6101426))
    }
    if (turn > 0f && a.count("omen") == 0) walkSteps({ u -> pt(u) }, 0f, 0.42f * easeOut(seg(a.b("turn"), 0f, 0.4f)), ArtInk.Ivory, 1f, t)
    if (turn > 0f) {
        // back toward where it started, in ember
        val from = pt(0.42f)
        val back1 = from + Offset(-w * 0.04f, h * 0.07f)
        val back2 = pt(0.2f) + Offset(0f, h * 0.08f)
        steps(from, back1, back1 + Offset(w * 0.1f, h * 0.02f), back2, turn, ArtInk.Ember, t)
        tagAt(a.tag("turn"), back2 + Offset(-w * 0.08f, h * 0.09f), seg(a.b("turn"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
    }
    if (word > 0f) {
        val b = pt(0.5f) + Offset(0f, -h * 0.3f)
        val glowPath = Path().apply {
            for (k in 0..30) {
                val q = pt(0.42f + 0.55f * k / 30f)
                if (k == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y)
            }
        }
        drawPath(glowPath, ArtInk.Gold.copy(alpha = 0.35f * word), style = Stroke(10f * dp, cap = StrokeCap.Round))
        walkSteps({ u -> pt(u) }, 0.42f, 0.42f + 0.52f * easeOut(seg(a.b("word"), 0.3f, 1f)), ArtInk.Ivory, 1f, t)
        val tl = a.tag("word")
        if (tl != null) {
            // a speech bubble around the good word
            val bw = tl.size.width + 18f * dp
            val bh = tl.size.height + 10f * dp
            val r = Rect(b.x - bw / 2, b.y - bh / 2, b.x + bw / 2, b.y + bh / 2)
            drawRoundRect(ArtInk.Ivory.copy(alpha = 0.95f * word), topLeft = r.topLeft, size = r.size, cornerRadius = CornerRadius(bh / 2, bh / 2))
            drawPath(Path().apply { moveTo(b.x - 4f * dp, r.bottom - 1f); lineTo(b.x + 6f * dp, r.bottom - 1f); lineTo(b.x - 2f * dp, r.bottom + 8f * dp); close() }, ArtInk.Ivory.copy(alpha = 0.95f * word))
            drawText(tl, color = Color(0xFF2A2A2A), topLeft = Offset(b.x - tl.size.width / 2f, b.y - tl.size.height / 2f), alpha = word)
        }
    }

    // following the ones before, step for step, even into a hole
    val f1 = a.b("follow")
    val f2 = a.b("follow2")
    if (f1 > 0f || f2 > 0f) {
        val holeP = Offset(w * 0.18f, h * 0.9f)
        val c1 = pt(0.35f) + Offset(0f, h * 0.02f)
        val c2 = holeP + Offset(w * 0.18f, -h * 0.06f)
        val s0 = pt(0f)
        drawOval(Color(0xFF07060E), topLeft = holeP + Offset(-16f * dp, -6f * dp), size = Size(32f * dp, 12f * dp))
        drawOval(Color(0xFF3A3050), topLeft = holeP + Offset(-16f * dp, -6f * dp), size = Size(32f * dp, 12f * dp), style = Stroke(1f * dp))
        if (f1 > 0f) steps(s0, c1, c2, holeP, easeOut(f1), ArtInk.Ember, t, offset = 1.5f * dp)
        if (f2 > 0f) steps(s0, c1, c2, holeP, easeOut(f2), ArtInk.Ivory, t, offset = 1.5f * dp, scale = 0.8f)
        tagAt(a.tag("follow"), pt(0.12f) + Offset(0f, h * 0.12f), seg(f1, 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
        tagAt(a.tag("follow2"), pt(0.3f) + Offset(0f, -h * 0.1f), seg(f2, 0.3f, 1f), back = Color(0xE6101426))
        tagAt(a.tag("hole") ?: a.tag("follow2", 0, 1), holeP + Offset(-w * 0.02f, h * 0.06f), seg(max(f2, a.b("hole")), 0.5f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
    }
    // straying to the idols
    val stray = easeOut(a.b("stray"))
    val idols = easeOut(a.b("idols"))
    if (stray > 0f || idols > 0f) {
        val st = Offset(w * 0.5f, h * 0.95f)
        for (k in 0 until 3) {
            val sp = st + Offset((k - 1) * w * 0.05f, 0f)
            val sh = h * (0.08f + 0.01f * k)
            if (idols > 0f) drawCircle(Brush.radialGradient(listOf(ArtInk.Ember.copy(alpha = 0.5f * idols), Color.Transparent), center = sp - Offset(0f, sh), radius = 14f * dp), radius = 14f * dp, center = sp - Offset(0f, sh))
            drawRoundRect(TrailInk.Stone, topLeft = Offset(sp.x - 5f * dp, sp.y - sh), size = Size(10f * dp, sh), cornerRadius = CornerRadius(5f * dp, 5f * dp))
        }
        if (stray > 0f) steps(pt(0.45f), pt(0.5f) + Offset(0f, h * 0.06f), st + Offset(w * 0.06f, -h * 0.12f), st + Offset(0f, -h * 0.03f), stray, ArtInk.Ember, t)
        tagAt(a.tag("stray"), st + Offset(w * 0.15f, h * 0.015f), seg(a.b("stray"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
        tagAt(a.tag("idols"), st + Offset(-w * 0.18f, -h * 0.1f), seg(a.b("idols"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
    }
    // a lantern carried on to the end of a lit road
    val keep = a.b("keep")
    if (keep > 0f) {
        val glowPath = Path().apply {
            for (k in 0..30) {
                val q = pt(0.3f + 0.67f * k / 30f) + Offset(0f, -h * 0.005f)
                if (k == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y)
            }
        }
        drawPath(glowPath, ArtInk.Gold.copy(alpha = 0.35f * easeOut(keep)), style = Stroke(9f * dp, cap = StrokeCap.Round))
        val at = pt(0.3f + 0.62f * easeInOut(keep))
        val ls = w * 0.02f
        lantern(at + Offset(0f, -5.6f * ls), ls, 1f, 0.06f * sin(t * 9f))
        tagAt(a.tag("keep"), at + Offset(0f, -h * 0.22f), seg(keep, 0.2f, 0.8f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }

    // the two lands, the open way, and the one excused
    if (hasLands) {
        tagAt(a.tag("lands", 0, 0), Offset(w * 0.8f, h * 0.88f), seg(lands0, 0f, 0.4f), back = Color(0xE6101426), ink = ArtInk.Ember)
        tagAt(a.tag("lands", 0, 1), Offset(w * 0.22f, h * 0.88f), seg(lands0, 0.5f, 1f), back = Color(0xE6101426), ink = ArtInk.Jade)
    }
    val open = easeOut(a.b("open"))
    if (open > 0f) {
        drawPath(road, ArtInk.Gold.copy(alpha = 0.3f * open), style = Stroke(10f * dp, cap = StrokeCap.Round))
        tagAt(a.tag("open"), Offset(w * 0.5f, h * 0.56f), seg(a.b("open"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }
    val stuck = easeOut(a.b("stuck"))
    if (stuck > 0f) {
        val c = Offset(w * 0.84f, h * 0.7f)
        val ls = w * 0.018f
        lantern(c + Offset(0f, -5.3f * ls), ls, 0.7f, 0.05f * sin(t * 8f))
        drawLine(Color(0xFF8E8AA8).copy(alpha = stuck), c + Offset(4f * dp, -2f * dp), c + Offset(16f * dp, 8f * dp), 1.4f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(2.5f * dp, 2f * dp)))
        drawCircle(Color(0xFF55516E).copy(alpha = stuck), radius = 4f * dp, center = c + Offset(18f * dp, 9f * dp))
        tagAt(a.tag("stuck"), c + Offset(0f, -h * 0.22f), seg(a.b("stuck"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Jade)
    }
}

/** Footprints along a cubic from p0 to p3, up to [upTo] of the way. */
private fun DrawScope.steps(p0: Offset, p1: Offset, p2: Offset, p3: Offset, upTo: Float, color: Color, t: Float, offset: Float = 0f, scale: Float = 1f) {
    val n = 18
    val last = (n * upTo).toInt()
    for (k in 0..last) {
        val q = k / n.toFloat()
        val c = cubic(p0, p1, p2, p3, q)
        val side = if (k % 2 == 0) -1f else 1f
        val a = if (k == last) fract(upTo * n).coerceAtLeast(0.3f) else 0.85f
        footprint(c + Offset(0f, side * 2.2f * density + offset), color, a, scale)
    }
}

/** Footprints along the trail from [from] to [to] (trail positions 0..1). */
private fun DrawScope.walkSteps(pt: (Float) -> Offset, from: Float, to: Float, color: Color, alpha: Float, t: Float) {
    if (to <= from) return
    val stepU = 0.035f
    var u = from
    var k = 0
    while (u <= to) {
        val side = if (k % 2 == 0) -1f else 1f
        footprint(pt(u) + Offset(0f, side * 2.2f * density), color, 0.8f * alpha)
        u += stepU
        k++
    }
}

// ───────────── the ridge between two ravines ─────────────

private object RidgeInk {
    val SkyTop = Color(0xFF1C2350)
    val SkyLow = Color(0xFFCF8E6A)
    val Ridge = Color(0xFF6E6A86)
    val RidgeLight = Color(0xFFB9B0C9)
    val Deep = Color(0xFF05040C)
    val Wall = Color(0xFF16142A)
}

/**
 * A narrow path along a ridge into the distance, with a ravine on each side (the two extremes). Beats: stone ×n
 * (lit milestones on the path), pit (side 0 right, 1 left: a crumbling edge named for the error), lean (a side
 * trail leaving the path toward the `side` ravine), walk (a lantern goes safely along the middle).
 */
internal fun DrawScope.ridgeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val horizon = h * 0.34f
    drawRect(Brush.verticalGradient(listOf(RidgeInk.SkyTop, RidgeInk.SkyLow), endY = horizon), size = Size(w, horizon))
    hills(Color(0xFF3A2F58), horizon, h * 0.025f, 0.012f, 0.03f, 1.1f)
    // the ravines fill the ground; the ridge rises in the middle
    drawRect(Brush.verticalGradient(listOf(RidgeInk.Wall, RidgeInk.Deep), startY = horizon, endY = h), topLeft = Offset(0f, horizon), size = Size(w, h - horizon))
    val vx = w * 0.5f
    val vy = horizon + h * 0.02f
    fun edge(u: Float, side: Float): Offset {      // u: 0 near (bottom) .. 1 far; side -1 left, +1 right
        val y = mix(h, vy, 1f - (1f - u) * (1f - u))
        val half = mix(w * 0.16f, w * 0.008f, u)
        return Offset(vx + side * half, y)
    }
    val ridge = Path().apply {
        moveTo(edge(0f, -1f).x, h)
        for (k in 0..24) {
            val q = edge(k / 24f, -1f)
            lineTo(q.x, q.y)
        }
        for (k in 24 downTo 0) {
            val q = edge(k / 24f, 1f)
            lineTo(q.x, q.y)
        }
        close()
    }
    drawPath(ridge, Brush.verticalGradient(listOf(RidgeInk.Ridge, RidgeInk.RidgeLight), startY = vy, endY = h))
    // ravine walls falling away from the ridge
    for (side in listOf(-1f, 1f)) {
        val wall = Path().apply {
            moveTo(edge(0f, side).x, h)
            for (k in 0..24) {
                val q = edge(k / 24f, side)
                lineTo(q.x, q.y)
            }
            lineTo(edge(1f, side).x + side * w * 0.02f, vy + h * 0.05f)
            lineTo(edge(0f, side).x + side * w * 0.12f, h)
            close()
        }
        drawPath(wall, Color(0xFF2B2843))
    }

    // milestones of light along the middle of the path
    val stones = a.count("stone")
    for (m in 0 until stones) {
        val p = easeOut(a.b("stone", m))
        if (p <= 0f) continue
        for (k in 0 until 4) {
            val u = 0.1f + 0.2f * k + 0.05f * m
            val c = Offset(vx, edge(u, 0f).y)
            val r = mix(4f, 1.2f, u) * dp
            drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.6f * p), Color.Transparent), center = c, radius = r * 4f), radius = r * 4f, center = c)
            drawCircle(ArtInk.Gold.copy(alpha = p), radius = r, center = c)
        }
    }

    // pitfalls on each side: the edge crumbles where the error lies
    var rightN = 0
    var leftN = 0
    for (i in 0 until a.size) {
        val kind = a.kindAt(i)
        if (kind != "pit" && kind != "lean") continue
        val p = a.beatAt(i)
        val side = if (a.sideAt(i) == 1) -1f else 1f
        val row = if (side > 0) rightN++ else leftN++
        if (p <= 0f) continue
        val u = 0.12f + 0.2f * row
        val e = edge(u, side)
        if (kind == "pit") {
            // falling stones
            for (k in 0 until 4) {
                val q = fract(t * 0.6f + k * 0.27f + row * 0.13f)
                val c = e + Offset(side * (4f + 6f * k) * dp, q * h * 0.25f)
                drawCircle(Color(0xFF8A84A0).copy(alpha = 0.7f * easeOut(p) * (1f - q)), radius = 1.6f * dp, center = c)
            }
            val q = easeOut(p)
            val a0 = edge(u - 0.06f, side)
            val a1 = edge(u + 0.06f, side)
            val inner = Offset(mix(e.x, vx, 0.3f * q), e.y + 2f * dp)
            val rim = Path().apply {
                moveTo(a0.x, a0.y)
                quadraticBezierTo(mix(a0.x, inner.x, 0.8f), mix(a0.y, inner.y, 0.4f), inner.x, inner.y)
                quadraticBezierTo(mix(a1.x, inner.x, 0.8f), mix(a1.y, inner.y, 0.4f), a1.x, a1.y)
            }
            val bite = Path().apply {
                moveTo(a0.x, a0.y)
                quadraticBezierTo(mix(a0.x, inner.x, 0.8f), mix(a0.y, inner.y, 0.4f), inner.x, inner.y)
                quadraticBezierTo(mix(a1.x, inner.x, 0.8f), mix(a1.y, inner.y, 0.4f), a1.x, a1.y)
                close()
            }
            drawPath(bite, Color(0xFF15131F).copy(alpha = q))
            drawPath(rim, ArtInk.Ember.copy(alpha = 0.85f * q), style = Stroke(1.6f * dp, cap = StrokeCap.Round))
        } else {
            // a side trail leaving the path, with steps going over the edge
            val start = Offset(vx, edge(u, 0f).y)
            val end = Offset(e.x + side * w * 0.2f, e.y + h * 0.06f)
            val q = easeOut(seg(p, 0f, 0.8f))
            drawLine(ArtInk.Ember.copy(alpha = 0.7f * q), start, Offset(mix(start.x, end.x, q), mix(start.y, end.y, q)), 1.6f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp), -t * 16f * dp))
        }
        val tx = if (side > 0) w * 0.8f else w * 0.2f
        val ty = mix(h * 0.86f, h * 0.48f, row / 2.5f)
        tagAt(a.tagOf(i), Offset(tx, ty), seg(p, 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
    }

    for (m in 0 until stones) {
        tagAt(a.tag("stone", m), Offset(vx, h * (0.16f + 0.08f * m)), seg(a.b("stone", m), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }

    // the traveller keeps to the middle
    val walk = if (a.count("walk") > 0) a.b("walk") else (0 until a.size).sumOf { a.beatAt(it).toDouble() }.toFloat() / max(1, a.size)
    val wu = 0.05f + 0.7f * easeInOut(walk)
    val ls = mix(w * 0.024f, w * 0.008f, wu)
    val lp = Offset(vx, edge(wu, 0f).y)
    lantern(lp + Offset(0f, -5.4f * ls), ls, 0.9f, 0.06f * sin(t * 9f))
    tagAt(a.tag("walk"), Offset(vx, h * 0.42f), seg(walk, 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    // titles of the two sides
    a.extras.getOrNull(0)?.let { tagAt(it, Offset(w * 0.86f, h * 0.06f), 1f, back = Color(0x99101426)) }
    a.extras.getOrNull(1)?.let { tagAt(it, Offset(w * 0.14f, h * 0.06f), 1f, back = Color(0x99101426)) }
}

// ───────────── the tree ─────────────

private object TreeInk {
    val SkyTop = Color(0xFF2B4C7E)
    val SkyLow = Color(0xFFF0C890)
    val StormTop = Color(0xFF151B28)
    val StormLow = Color(0xFF3D4656)
    val Hill = Color(0xFF3F6B45)
    val HillStorm = Color(0xFF223828)
    val Soil = Color(0xFF3B2A1E)
    val SoilDeep = Color(0xFF221710)
    val Bark = Color(0xFF5A3B24)
    val Leaf = Color(0xFF4E9A5E)
    val LeafLight = Color(0xFF7FC48A)
    val Dry = Color(0xFFC9A04E)
    val Root = Color(0xFFD9B98A)
}

/**
 * A tree on a hill, its roots shown in the soil below. Beats: root ×n (a root grows, named at its tip), trunk,
 * crown (the canopy spreads), fruit ×n, storm (wind and rain bend it), hold (its roots light up: it stands),
 * calm (the sky clears), shed (dry leaves fall away), bad ×n (refused acts, crossed out).
 */
internal fun DrawScope.treeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val hasRoots = a.count("root") > 0
    val storm = easeInOut(seg(a.b("storm"), 0f, 0.7f)) * (1f - easeInOut(a.b("calm")))
    val ground = if (hasRoots) h * 0.58f else h * 0.78f
    drawRect(Brush.verticalGradient(listOf(lerp(TreeInk.SkyTop, TreeInk.StormTop, storm), lerp(TreeInk.SkyLow, TreeInk.StormLow, storm)), endY = ground), size = Size(w, ground))
    if (storm < 1f) {
        val sun = Offset(w * 0.82f, h * 0.18f)
        drawCircle(Brush.radialGradient(listOf(Color(0xFFFFEFC0).copy(alpha = 0.7f * (1f - storm)), Color.Transparent), center = sun, radius = w * 0.14f), radius = w * 0.14f, center = sun)
        drawCircle(Color(0xFFFFE9A8).copy(alpha = 1f - storm), radius = w * 0.035f, center = sun)
    }
    // hill and soil
    val hillTop = ground - h * 0.03f
    drawPath(Path().apply {
        moveTo(0f, ground + h * 0.02f)
        cubicTo(w * 0.3f, hillTop - h * 0.02f, w * 0.7f, hillTop - h * 0.02f, w, ground + h * 0.02f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }, lerp(TreeInk.Hill, TreeInk.HillStorm, storm))
    val base = Offset(w * 0.5f, hillTop + h * 0.005f)
    if (hasRoots) {
        drawRect(Brush.verticalGradient(listOf(TreeInk.Soil, TreeInk.SoilDeep), startY = ground + h * 0.02f, endY = h), topLeft = Offset(0f, ground + h * 0.02f), size = Size(w, h - ground))
        drawLine(Color(0xFF55402E), Offset(0f, ground + h * 0.02f), Offset(w, ground + h * 0.02f), 1.2f * dp)
    }

    // roots, each with its name
    val roots = a.count("root")
    val hold = easeOut(a.b("hold"))
    for (k in 0 until roots) {
        val p = easeOut(seg(a.b("root", k), 0f, 0.7f))
        if (p <= 0f) continue
        val frac = if (roots == 1) 0.5f else k / (roots - 1f)
        val end = Offset(mix(w * 0.88f, w * 0.12f, frac), mix(h * 0.8f, h * 0.95f, 1f - abs1(frac - 0.5f) * 2f))
        val c1 = base + Offset(0f, h * 0.08f)
        val c2 = Offset(mix(base.x, end.x, 0.6f), end.y - h * 0.08f)
        val col = lerp(TreeInk.Root, ArtInk.Gold, hold)
        drawPath(cubicPart(base, c1, c2, end, 0f, p), col, style = Stroke(3f * dp, cap = StrokeCap.Round))
        if (p > 0.9f) drawCircle(col, radius = 2.2f * dp, center = end)
        tagAt(a.tag("root", k), end + Offset(0f, if (k % 2 == 0) -h * 0.07f else h * 0.0f), seg(a.b("root", k), 0.4f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }

    // trunk and branches, bending in the storm
    val trunkIn = if (a.count("trunk") > 0) easeOut(a.b("trunk")) else 1f
    val crown = if (a.count("crown") > 0) easeOut(a.b("crown")) else 1f
    val bend = storm * (0.08f + 0.03f * sin(t * 3.1f)) * w
    val trunkH = (if (hasRoots) h * 0.3f else h * 0.42f) * (0.35f + 0.65f * trunkIn)
    val top = base + Offset(bend, -trunkH)
    val girth = 0.45f + 0.55f * crown
    val trunk = Path().apply {
        moveTo(base.x - 8f * dp * girth, base.y)
        cubicTo(base.x - 5f * dp * girth, base.y - trunkH * 0.5f, top.x - 4f * dp * girth - bend * 0.3f, top.y + trunkH * 0.3f, top.x - 2.5f * dp * girth, top.y)
        lineTo(top.x + 2.5f * dp * girth, top.y)
        cubicTo(top.x + 4f * dp * girth - bend * 0.3f, top.y + trunkH * 0.3f, base.x + 5f * dp * girth, base.y - trunkH * 0.5f, base.x + 8f * dp * girth, base.y)
        close()
    }
    drawPath(trunk, TreeInk.Bark)
    if (crown < 1f) {
        val leafA = 1f - crown
        for (sd in listOf(-1f, 1f)) {
            val c = top + Offset(sd * 7f * dp, -3f * dp)
            withTransform({ rotate(sd * -28f, c) }) {
                drawOval(TreeInk.Leaf.copy(alpha = leafA), topLeft = c - Offset(8f * dp, 3.6f * dp), size = Size(16f * dp, 7.2f * dp))
            }
        }
    }
    if (crown > 0f) {
        val r = trunkH * 0.62f * (0.3f + 0.7f * crown)
        val blobs = listOf(Offset(0f, -0.15f) to 1f, Offset(-0.55f, 0.15f) to 0.75f, Offset(0.55f, 0.12f) to 0.78f, Offset(-0.25f, -0.55f) to 0.7f, Offset(0.3f, -0.5f) to 0.72f)
        for ((o, sz) in blobs) {
            val c = top + Offset(o.x * r + bend * 0.4f, o.y * r)
            val sway = sin(t * 2f + o.x * 3f) * storm * 3f * dp
            drawCircle(lerp(TreeInk.Leaf, Color(0xFF2E5A3A), storm * 0.5f).copy(alpha = crown), radius = r * 0.62f * sz, center = c + Offset(sway, 0f))
            drawCircle(TreeInk.LeafLight.copy(alpha = 0.35f * crown * (1f - storm)), radius = r * 0.3f * sz, center = c + Offset(-r * 0.15f * sz + sway, -r * 0.15f * sz))
        }
        tagAt(a.tag("crown"), top + Offset(0f, -r * 1.05f), seg(a.b("crown"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Jade)
    }
    tagAt(a.tag("trunk"), base + Offset(w * 0.16f, -trunkH * 0.4f), seg(a.b("trunk"), 0.3f, 1f), back = Color(0xE6101426))

    // fruit; with three or more, their names stand in a column on the right, each with a thread to its fruit
    val fruits = a.count("fruit")
    for (k in 0 until fruits) {
        val p = easeOut(a.b("fruit", k))
        if (p <= 0f) continue
        val ang = (200f + (if (fruits >= 3) 40f else 70f) * k) * PI.toFloat() / 180f
        val c = top + Offset(cos(ang) * trunkH * 0.45f, sin(ang) * trunkH * 0.3f + trunkH * 0.12f)
        drawCircle(ArtInk.Gold.copy(alpha = p), radius = 4f * dp * p, center = c)
        if (fruits >= 3) {
            val tl = a.tag("fruit", k)
            val tc = Offset(w * 0.84f, h * (0.16f + 0.1f * k))
            val al = seg(a.b("fruit", k), 0.3f, 1f)
            if (tl != null && al > 0f) drawLine(ArtInk.Gold.copy(alpha = 0.55f * al), c, Offset(tc.x - tl.size.width / 2f - 6f * dp, tc.y), 1f * dp)
            tagAt(tl, tc, al, back = Color(0xE6101426), ink = ArtInk.Gold)
        } else {
            tagAt(a.tag("fruit", k), c + Offset(0f, -h * 0.07f), seg(a.b("fruit", k), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
        }
    }

    // storm: wind streaks and rain; the roots hold
    if (storm > 0f) {
        for (m in 0 until (40 * storm).toInt()) {
            val x0 = fract(m * 0.618f) * w * 1.1f
            val y0 = fract(t * 1.1f + m * 0.13f) * ground
            drawLine(Color(0xFFBFD0F5).copy(alpha = 0.45f * storm), Offset(x0, y0), Offset(x0 - 6f * dp, y0 + 10f * dp), 1f * dp, StrokeCap.Round)
        }
        for (k in 0 until 3) {
            val y = h * (0.15f + 0.12f * k)
            val q = fract(t * 0.9f + k * 0.3f)
            drawLine(ArtInk.Ivory.copy(alpha = 0.35f * storm * bump(q)), Offset(-w * 0.1f + q * w * 1.2f, y), Offset(-w * 0.1f + q * w * 1.2f + w * 0.15f, y + 3f * dp), 1.4f * dp, StrokeCap.Round)
        }
        tagAt(a.tag("storm"), Offset(w * 0.2f, h * 0.1f), seg(a.b("storm"), 0.2f, 0.9f), back = Color(0xE6101426), ink = ArtInk.Ember)
    }
    if (hold > 0f) {
        drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.4f * hold), Color.Transparent), center = base, radius = w * 0.14f), radius = w * 0.14f, center = base)
        tagAt(a.tag("hold"), Offset(w * 0.8f, h * 0.44f), seg(a.b("hold"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }
    val calm = easeOut(a.b("calm"))
    if (calm > 0f) tagAt(a.tag("calm"), Offset(w * 0.8f, h * 0.32f), seg(a.b("calm"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Jade)
    // dry leaves falling away
    val shed = a.b("shed")
    if (shed > 0f) {
        for (m in 0 until 14) {
            val q = (shed * 1.4f - m * 0.05f).coerceIn(0f, 1f)
            if (q <= 0f) continue
            val sx = top.x + (fract(m * 0.37f) - 0.5f) * trunkH * 0.9f
            val sy = top.y + (fract(m * 0.61f) - 0.3f) * trunkH * 0.4f
            val c = Offset(sx + q * w * 0.25f + sin(t * 3f + m) * 4f * dp, sy + q * (ground - sy))
            drawOval(TreeInk.Dry.copy(alpha = 1f - seg(q, 0.85f, 1f)), topLeft = c - Offset(3f * dp, 1.6f * dp), size = Size(6f * dp, 3.2f * dp))
        }
        tagAt(a.tag("shed"), Offset(w * 0.82f, ground - h * 0.24f), seg(shed, 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
    }
    // refused acts, listed and crossed out on the left
    for (k in 0 until a.count("bad")) {
        val p = a.b("bad", k)
        if (p <= 0f) continue
        val c = Offset(w * 0.16f, h * (0.34f + 0.13f * k))
        tagAt(a.tag("bad", k), c, seg(p, 0f, 0.4f), back = Color(0xE6101426), ink = ArtInk.Ember)
        val tl = a.tag("bad", k)
        if (tl != null) {
            val half = tl.size.width / 2f + 4f * dp
            drawLine(ArtInk.Ember.copy(alpha = seg(p, 0.4f, 0.9f)), c + Offset(-half, 0f), c + Offset(-half + 2f * half * seg(p, 0.4f, 0.9f), 0f), 1.6f * dp, StrokeCap.Round)
        }
    }
}

private fun abs1(x: Float): Float = if (x < 0f) -x else x

// ───────────── streams to one basin ─────────────

private object StreamInk {
    val SkyTop = Color(0xFF26355F)
    val SkyLow = Color(0xFF8C7AA2)
    val Mount = Color(0xFF3B3A5E)
    val MountLight = Color(0xFF5B5980)
    val Ground = Color(0xFF2A2B45)
    val Water = Color(0xFF8FD3F0)
    val Dark = Color(0xFF4A2A30)
}

/**
 * Streams of light rise from the land and go up out of sight, to Allah alone (nothing is drawn where they go).
 * Beats: basin (the sky above them opens in light), stream (one stream per tag part, "a|b|c"), divert (the
 * `side`-th stream, 0 = the last one so far, bends back down into a dark pool on the ground), curse (a mark over
 * the dark pool), block (a gate shuts a branch that would turn aside), near (help asked of someone alive and
 * present, in what he can do: a short channel between two wells on the land).
 */
internal fun DrawScope.streamsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val land = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(StreamInk.SkyTop, StreamInk.SkyLow), endY = land), size = Size(w, land))
    for (s in TWINKLES) drawCircle(ArtInk.Ivory.copy(alpha = 0.4f * (0.5f + 0.5f * sin(t * 1.4f + s.p))), radius = s.r * 0.8f * dp, center = Offset(s.x * w, s.y * land * 0.7f))
    hills(StreamInk.Mount, land - h * 0.03f, h * 0.02f, 0.012f, 0.029f, 0.4f)
    drawRect(StreamInk.Ground, topLeft = Offset(0f, land), size = Size(w, h - land))

    // every stream, in order: (start x on the land, appear progress, item index, tag part)
    data class Stream(val x: Float, val p: Float, val item: Int, val part: Int)
    val streams = ArrayList<Stream>()
    for (i in 0 until a.size) {
        if (a.kindAt(i) != "stream") continue
        for (part in 0 until max(1, a.parts(i))) streams += Stream(0f, a.beatAt(i), i, part)
    }
    val count = streams.size
    val placed = streams.mapIndexed { k, s -> s.copy(x = w * (0.86f - 0.72f * (if (count == 1) 0.5f else k / (count - 1f)))) }
    // which streams are turned aside (side = how many from the newest: 0 = the newest when it happens)
    val diverted = FloatArray(count)
    var turnedX = 0f
    var turnedN = 0
    var seen = 0
    for (i in 0 until a.size) {
        if (a.kindAt(i) == "stream") seen += max(1, a.parts(i))
        if (a.kindAt(i) == "divert" && count > 0) {
            val target = (seen - 1 - max(0, a.sideAt(i))).coerceIn(0, count - 1)
            diverted[target] = max(diverted[target], easeOut(a.beatAt(i)))
            turnedX += placed[target].x
            turnedN++
        }
    }
    // the dark pool lies on the side of the streams that are turned aside
    val pool = if (turnedN > 0 && turnedX / turnedN > w * 0.5f) Offset(w * 0.9f, h * 0.92f) else Offset(w * 0.1f, h * 0.92f)
    val anyDivert = (0 until a.count("divert")).maxOfOrNull { a.b("divert", it) } ?: 0f
    if (anyDivert > 0f) {
        drawOval(StreamInk.Dark.copy(alpha = easeOut(anyDivert)), topLeft = pool + Offset(-w * 0.08f, -h * 0.035f), size = Size(w * 0.16f, h * 0.07f))
    }
    // the sky opening in light above them
    val bIn = easeOut(a.b("basin"))
    if (bIn > 0f) {
        drawRect(Brush.verticalGradient(listOf(ArtInk.Gold.copy(alpha = 0.5f * bIn), Color.Transparent), startY = 0f, endY = h * 0.3f), size = Size(w, h * 0.3f))
        tagAt(a.tag("basin"), Offset(w * 0.5f, h * 0.07f), seg(a.b("basin"), 0.2f, 0.9f), back = Color(0xE6101426), ink = ArtInk.Gold)
    }

    var branchFrom = Offset(w * 0.5f, h * 0.6f)
    for ((k, s) in placed.withIndex()) {
        val p = easeOut(seg(s.p, 0f, 0.8f))
        if (p <= 0f) continue
        val start = Offset(s.x, land + h * 0.03f)
        val dv = diverted[k]
        val inward = if (pool.x > w * 0.5f) -1f else 1f
        val upEnd = Offset(w * 0.5f + (s.x - w * 0.5f) * 0.8f, h * 0.16f)
        val end = Offset(mix(upEnd.x, pool.x + inward * w * 0.03f, dv), mix(upEnd.y, pool.y - h * 0.02f, dv))
        val c1 = Offset(mix(start.x, start.x + inward * w * 0.18f, dv), mix(start.y - h * 0.3f, h * 0.42f, dv))
        val c2 = Offset(mix(upEnd.x, pool.x + inward * w * 0.24f, dv), mix(upEnd.y + h * 0.2f, h * 0.42f, dv))
        if (dv < 0.5f) branchFrom = cubic(start, c1, c2, end, 0.45f)
        val col = lerp(lerp(StreamInk.Water, ArtInk.Gold, 0.35f), StreamInk.Dark, dv)
        // a rising stream thins into the light before it reaches the top: where it goes is not drawn
        val fadeTop = if (dv < 0.5f) Brush.verticalGradient(listOf(col.copy(alpha = 0f), col), startY = upEnd.y, endY = upEnd.y + h * 0.22f) else null
        if (fadeTop != null) {
            drawPath(cubicPart(start, c1, c2, end, 0f, p), fadeTop, alpha = 0.3f, style = Stroke(5f * dp, cap = StrokeCap.Round))
            drawPath(cubicPart(start, c1, c2, end, 0f, p), fadeTop, style = Stroke(1.8f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp), t * 26f * dp)))
        } else {
            drawPath(cubicPart(start, c1, c2, end, 0f, p), col.copy(alpha = 0.3f), style = Stroke(5f * dp, cap = StrokeCap.Round))
            drawPath(cubicPart(start, c1, c2, end, 0f, p), col, style = Stroke(1.8f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp), t * 26f * dp)))
        }
        drawCircle(ArtInk.Ivory.copy(alpha = 0.8f * p), radius = 2.4f * dp, center = start)
        tagAt(a.tagOf(s.item, s.part), start + Offset(0f, h * (0.045f + 0.06f * (k % 2))), seg(s.p, 0.3f, 1f), back = Color(0xE6101426), ink = if (dv > 0.5f) ArtInk.Ember else ArtInk.Ivory)
    }
    for (k in 0 until a.count("divert")) {
        val tl = a.tag("divert", k)
        val tx = if (pool.x > w * 0.5f) min(pool.x, w - halfTagW(tl)) else max(pool.x, halfTagW(tl))
        tagAt(tl, Offset(tx, pool.y - h * 0.2f - k * h * 0.09f), seg(a.b("divert", k), 0.4f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
    }
    val curse = easeOut(a.b("curse"))
    if (curse > 0f) {
        crossAt(pool, 9f * dp, curse)
        val tl = a.tag("curse")
        val half = (tl?.size?.width ?: 0) / 2f + 8f * dp
        val cx = if (pool.x > w * 0.5f) min(w * 0.75f, w - half) else max(w * 0.25f, half)
        tagAt(tl, Offset(cx, h * 0.3f), seg(a.b("curse"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
    }
    // help asked of someone alive and present, in what he can do: a short channel between two wells
    val near = easeOut(a.b("near"))
    if (near > 0f) {
        val w1 = Offset(w * 0.95f, h * 0.89f)
        val w2 = Offset(w * 0.83f, h * 0.95f)
        for (c in listOf(w1, w2)) {
            drawOval(StreamInk.Water.copy(alpha = 0.25f * near), topLeft = c + Offset(-w * 0.035f, -h * 0.022f), size = Size(w * 0.07f, h * 0.044f))
            drawOval(ArtInk.Jade.copy(alpha = near), topLeft = c + Offset(-w * 0.035f, -h * 0.022f), size = Size(w * 0.07f, h * 0.044f), style = Stroke(1.2f * dp))
        }
        drawLine(ArtInk.Jade.copy(alpha = near), w1, Offset(mix(w1.x, w2.x, near), mix(w1.y, w2.y, near)), 1.8f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp), -t * 26f * dp))
        tagAt(a.tag("near"), Offset(w * 0.86f, h * 0.76f), seg(a.b("near"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Jade)
    }
    val block = easeOut(a.b("block"))
    if (block > 0f) {
        // a short branch toward the pool, shut by a gate
        val from = branchFrom
        val to = Offset(mix(branchFrom.x, pool.x, 0.6f), mix(branchFrom.y, pool.y, 0.6f))
        drawLine(StreamInk.Water.copy(alpha = 0.6f), from, Offset(mix(from.x, to.x, 0.5f), mix(from.y, to.y, 0.5f)), 1.8f * dp, StrokeCap.Round)
        val g = Offset(mix(from.x, to.x, 0.55f), mix(from.y, to.y, 0.55f))
        drawLine(ArtInk.Brass.copy(alpha = block), g + Offset(-8f * dp, -6f * dp), g + Offset(8f * dp, 6f * dp), 3f * dp, StrokeCap.Round)
        val tl = a.tag("block")
        tagAt(tl, g + Offset(0f, -h * 0.08f), seg(a.b("block"), 0.3f, 1f), back = Color(0xE6101426), ink = ArtInk.Ember)
    }
}
