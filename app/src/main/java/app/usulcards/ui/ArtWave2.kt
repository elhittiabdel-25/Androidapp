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
 * Wave 2 of the illustrated scenes: a building raised on its foundation, the gate and the stations of the Last
 * Day, graves and relics whose roads to shirk were closed, and the hourglass of al-'Asr. Same rules as Art.kt:
 * drawn in code, no people, animals, angels, the Throne, or depiction of Allah.
 */

private val TagBack = Color(0xE6101426)

/** A stable pseudo-random number in 0..1 for [seed]. */
private fun rnd(seed: Int): Float = fract(sin(seed * 12.9898f + 1.7f) * 43758.547f)

/** Half the drawn width of a tag (with its padding), 0 when there is none. */
private fun DrawScope.halfTag(tl: TextLayoutResult?): Float = if (tl == null) 0f else tl.size.width / 2f + 6f * density

/** A tag struck through once [strike] (0..1) has run. */
private fun DrawScope.struckTag(tl: TextLayoutResult?, c: Offset, alpha: Float, strike: Float, ink: Color = ArtInk.Ember) {
    tagAt(tl, c, alpha, back = TagBack, ink = ink)
    if (tl == null || strike <= 0f || alpha <= 0.01f) return
    val half = tl.size.width / 2f + 3f * density
    val x0 = (c.x - half).coerceAtLeast(6f * density)
    drawLine(ink.copy(alpha = alpha), Offset(x0, c.y), Offset(x0 + 2f * half * strike, c.y), 1.5f * density, StrokeCap.Round)
}

// ───────────── the building: foundation first ─────────────

private object BuildInk {
    val SkyTop = Color(0xFF243766)
    val SkyLow = Color(0xFFE2AE82)
    val Hills = Color(0xFF4A3F66)
    val Ground = Color(0xFF34304C)
    val GroundEdge = Color(0xFF4C4568)
    val Stone = Color(0xFFCBB898)
    val StoneLight = Color(0xFFE8DCC2)
    val StoneDark = Color(0xFF9A8666)
    val Brick = Color(0xFFC48C5E)
    val BrickLight = Color(0xFFDDA779)
    val Opening = Color(0xFF1E1712)
    val Rival = Color(0xFF3B2533)
}

/** One building's state, gathered beat by beat. */
private class Site(val cx: Float, val bw: Float) {
    var base = 0f
    var glow = 0f
    var pillarsN = 0
    var pillars = 0f
    val courses = ArrayList<Float>()
    val courseItems = ArrayList<Int>()
    var roof = 0f
    var light = 0f
    var house = 0f
    var rival = 0f
    var lower = 0f
    var wall = 0f
    var raise = 0f
    var palace = 0f
    var brick = 0f
    var word = 0f
    var level = 0f
    var fall = 0f
    var lock = 0f
    val cracks = ArrayList<Int>()
    val names = ArrayList<Int>()
}

/**
 * A building raised on its foundation (one, or two side by side when beats name a `side`: 0 right, 1 left).
 * Beats: base (the foundation is laid), pillars (`value` columns rise), course ×n (a row of bricks), roof, glow
 * (the foundation shines: it holds everything), light (a lamp lit on top: the call goes out), house (a whole
 * house goes up), rival (an equal set up beside it), fall (it collapses entirely), crack ×n (it stands, cracked),
 * lock (the bricks bind into one: each holds the others).
 */
internal fun DrawScope.buildingScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    // the camera goes to each part as it is laid
    val shots = shotsFor(a, mapOf(
        "base" to Shot(Offset(w * 0.5f, h * 0.74f), 1.2f),
        "pillars" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "roof" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "glow" to Shot(Offset(w * 0.5f, h * 0.72f), 1.25f),
        "siraj" to Shot(Offset(w * 0.6f, h * 0.48f), 1.02f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.16f) {
        buildingBody(a)
        val sj = a.b("siraj")
        if (sj > 0f) {
            // his ﷺ way: his light, and those who follow him on it, seeing where they go
            val dp = density
            val t = a.t
            val lc = Offset(w * 0.84f, h * 0.26f)
            sirajMunir(lc, 4.4f * dp, easeOut(seg(sj, 0f, 0.25f)), t, nameAt = lc + Offset(0f, -h * 0.12f))
            val p0 = lc + Offset(0f, h * 0.05f)
            val p3 = Offset(w * 0.6f, h * 0.84f)
            val c1 = p0 + Offset(-w * 0.02f, h * 0.25f)
            val c2 = p3 + Offset(w * 0.12f, -h * 0.05f)
            val way = easeOut(seg(sj, 0.15f, 0.5f))
            drawPath(cubicPart(p0, c1, c2, p3, 0f, way), ArtInk.Gold.copy(alpha = 0.3f), style = Stroke(7f * dp, cap = StrokeCap.Round))
            drawPath(cubicPart(p0, c1, c2, p3, 0f, way), ArtInk.Gold.copy(alpha = 0.85f), style = Stroke(1.4f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp), -t * 18f * dp)))
            val walk = seg(sj, 0.35f, 1f)
            for (k in 0 until 4) {
                val u = (walk * 0.9f - k * 0.12f).coerceIn(0f, 0.95f)
                if (walk * 0.9f - k * 0.12f <= 0f) continue
                val p = cubic(p0, c1, c2, p3, u)
                val tl = h * mix(0.08f, 0.18f, u)
                figure(p, tl, Color(0xFF1E1A2A), seg(walk * 0.9f - k * 0.12f, 0f, 0.1f), walk = t * 6f + k)
            }
        }
    }
}

private fun DrawScope.buildingBody(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.84f
    // dawn behind the building: the first stars fading above, the light rising behind it
    drawRect(Brush.verticalGradient(0f to Color(0xFF15204A), 0.45f to BuildInk.SkyTop, 0.8f to Color(0xFF9A6E86), 1f to BuildInk.SkyLow, endY = ground), size = Size(w, ground))
    for (st in TWINKLES) {
        if (st.y > 0.45f) continue
        drawCircle(ArtInk.Ivory.copy(alpha = 0.35f * (0.5f + 0.5f * sin(t * 1.2f + st.p)) * (1f - st.y * 2f)), st.r * 0.8f * dp, Offset(st.x * w, st.y * ground))
    }
    val dawn = Offset(w * 0.5f, ground - h * 0.03f)
    lightShafts(dawn, -PI.toFloat() / 2f, PI.toFloat() * 1.1f, h * 0.95f, Color(0xFFFFE0B0), 0.75f, t, 9)
    softGlow(dawn, w * 0.32f, Color(0xFFFFC98C), 0.5f)
    farHills(BuildInk.Hills, Color(0xFF9A6E86), ground - h * 0.1f, h * 0.03f, 0.9f)
    hills(BuildInk.Hills, ground - h * 0.05f, h * 0.025f, 0.011f, 0.027f, 1.7f)
    drawRect(Brush.verticalGradient(listOf(BuildInk.Ground, Color(0xFF1E1B30)), startY = ground, endY = h), topLeft = Offset(0f, ground), size = Size(w, h - ground))
    drawLine(Color(0xFFFFD9A8).copy(alpha = 0.35f), Offset(0f, ground), Offset(w, ground), 1.2f * dp)

    val two = (0 until a.size).any { a.sideAt(it) >= 0 }
    val sites = if (two) listOf(Site(w * 0.69f, w * 0.27f), Site(w * 0.29f, w * 0.27f)) else listOf(Site(w * 0.5f, w * 0.38f))
    for (i in 0 until a.size) {
        val p = a.beatAt(i)
        val targets = if (!two || a.sideAt(i) < 0) sites else listOfNotNull(sites.getOrNull(a.sideAt(i)))
        for (s in targets) when (a.kindAt(i)) {
            "base" -> s.base = max(s.base, p)
            "glow" -> s.glow = max(s.glow, p)
            "pillars" -> {
                s.pillars = max(s.pillars, p)
                s.pillarsN = a.valueAt(i).toInt().let { if (it > 0) it else 5 }
            }
            "course" -> {
                s.courses += p
                s.courseItems += i
            }
            "roof" -> s.roof = max(s.roof, p)
            "light" -> s.light = max(s.light, p)
            "house" -> s.house = max(s.house, p)
            "rival" -> s.rival = max(s.rival, p)
            "lower" -> s.lower = max(s.lower, p)
            "raise" -> s.raise = max(s.raise, p)
            "palace" -> s.palace = max(s.palace, p)
            "brick" -> s.brick = max(s.brick, p)
            "word" -> s.word = max(s.word, p)
            "level" -> s.level = max(s.level, p)
            "wall" -> s.wall = max(s.wall, p)
            "name" -> s.names += i
            "fall" -> s.fall = max(s.fall, p)
            "lock" -> s.lock = max(s.lock, p)
            "crack" -> s.cracks += i
        }
    }

    val fH = h * 0.05f
    val floor = ground - fH
    val bh = h * 0.05f
    val ph = h * 0.3f
    for ((si, s) in sites.withIndex()) {
        val cx = s.cx
        val bw = s.bw
        val fall = s.fall
        val baseP = max(s.base, seg(s.house, 0f, 0.3f))
        val rows: List<Float> = when {
            s.palace > 0f -> List(6) { k -> seg(s.palace, 0.1f + 0.09f * k, 0.35f + 0.09f * k) }
            s.house > 0f -> List(5) { k -> seg(s.house, 0.15f + 0.1f * k, 0.4f + 0.1f * k) }
            s.wall > 0f -> List(4) { k -> seg(s.wall, 0.15f * k, 0.15f * k + 0.55f) }
            else -> s.courses
        }
        val roofP = max(max(s.roof, seg(s.house, 0.7f, 1f)), seg(s.palace, 0.75f, 1f))
        val baseP2 = max(baseP, seg(s.palace, 0f, 0.25f))
        val wallTop = if (s.pillarsN > 0) floor - ph else floor - rows.size * bh
        val roofTop = wallTop - h * 0.06f

        /** A block of the building: drawn in place, or tumbling down once the building falls. */
        fun block(r: Rect, c: Color, seed: Int, alpha: Float = 1f, shade: Boolean = false) {
            if (alpha <= 0f) return
            val q0 = rnd(seed)
            val q = easeOut(seg(fall, q0 * 0.3f, q0 * 0.3f + 0.6f))
            val target = ground - rnd(seed + 7) * h * 0.05f
            val dy = (target - r.bottom) * q * q
            val dx = (r.center.x - cx) * 0.6f * q + (q0 - 0.5f) * w * 0.05f * q
            val rot = (rnd(seed + 3) - 0.5f) * 80f * q
            withTransform({
                translate(dx, dy)
                rotate(rot, r.center)
            }) {
                drawRect(c.copy(alpha = c.alpha * alpha), r.topLeft, r.size)
                if (shade) drawRect(Color.Black.copy(alpha = 0.18f * alpha), Offset(r.left + r.width * 0.62f, r.top), Size(r.width * 0.38f, r.height))
            }
        }

        // the foundation shining under everything
        if (s.glow > 0f) {
            val g = easeOut(s.glow) * (1f - fall)
            val c = Offset(cx, ground - fH / 2)
            softGlow(c, bw * 0.75f, ArtInk.Gold, 0.55f * g)
        }
        // something set after it and below it: lower, and lit in jade
        if (s.lower > 0f) {
            val q = easeOut(s.lower)
            val lx = cx + (if (si == 1) -1f else 1f) * (bw / 2 + w * 0.07f)
            val lTop = mix(ground, mix(floor, roofTop, 0.45f), q)
            drawRect(Color(0xFF3A4A48), topLeft = Offset(lx - w * 0.022f, lTop), size = Size(w * 0.044f, ground - lTop))
            drawRect(ArtInk.Jade.copy(alpha = 0.8f), topLeft = Offset(lx - w * 0.022f, lTop), size = Size(w * 0.044f, ground - lTop), style = Stroke(1.2f * dp))
        }
        // an equal set up beside it
        if (s.rival > 0f) {
            val q = easeOut(s.rival)
            val rx = cx + (if (si == 1) -1f else 1f) * (bw / 2 + w * 0.07f)
            val rTop = mix(ground, roofTop, q)
            drawRect(BuildInk.Rival, topLeft = Offset(rx - w * 0.022f, rTop), size = Size(w * 0.044f, ground - rTop))
            drawRect(ArtInk.Ember.copy(alpha = 0.7f), topLeft = Offset(rx - w * 0.022f, rTop), size = Size(w * 0.044f, ground - rTop), style = Stroke(1.2f * dp))
            if (q > 0.6f) {
                val la = seg(q, 0.6f, 1f) * (1f - fall)
                drawLine(ArtInk.Ember.copy(alpha = 0.8f * la), Offset(rx, roofTop), Offset(cx, roofTop), 1.2f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
            }
        }
        // foundation: three blocks
        if (baseP2 > 0f) {
            val q = easeOut(baseP2)
            val fw = bw + w * 0.05f
            val top = ground - fH * q
            for (k in 0 until 3) {
                val r = Rect(cx - fw / 2 + fw * k / 3f + 0.6f * dp, top, cx - fw / 2 + fw * (k + 1) / 3f - 0.6f * dp, ground)
                block(r, BuildInk.Stone, 31 + 17 * k + 101 * si, shade = true)
            }
            if (fall <= 0f) drawLine(BuildInk.StoneLight, Offset(cx - fw / 2, top + 0.8f * dp), Offset(cx + fw / 2, top + 0.8f * dp), 1.2f * dp)
            if (s.glow > 0f) drawRect(ArtInk.Gold.copy(alpha = easeOut(s.glow) * (1f - fall)), topLeft = Offset(cx - fw / 2, top), size = Size(fw, ground - top), style = Stroke(1.6f * dp))
        }
        // pillars, the first on the right
        val n = s.pillarsN
        for (k in 0 until n) {
            val pk = easeOut(seg(s.pillars, k * 0.12f, k * 0.12f + 0.45f))
            if (pk <= 0f) continue
            val x = cx + bw * (0.5f - (k + 0.5f) / n)
            val pw = min(w * 0.03f, bw / n * 0.45f)
            val topY = floor - ph * pk
            block(Rect(x - pw / 2, topY, x + pw / 2, floor), BuildInk.StoneLight, 211 + 13 * k + 101 * si, shade = true)
            if (pk > 0.9f) block(Rect(x - pw * 0.8f, topY - h * 0.014f, x + pw * 0.8f, topY + 1f), BuildInk.Stone, 307 + 13 * k + 101 * si, alpha = seg(pk, 0.9f, 1f))
        }
        // rows of bricks, laid from right to left
        val nb = 6
        val brickW = bw / nb
        for ((r, pr) in rows.withIndex()) {
            if (pr <= 0f) continue
            val y1 = floor - r * bh
            val y0 = y1 - bh
            val off = if (r % 2 == 1) brickW / 2 else 0f
            val count = if (off > 0f) nb + 1 else nb
            for (j in 0 until count) {
                val idx = count - 1 - j
                val pj = easeOut(seg(pr, idx * 0.6f / count, idx * 0.6f / count + 0.4f))
                if (pj <= 0f) continue
                val left = max(cx - bw / 2, cx - bw / 2 - off + j * brickW)
                val right = min(cx + bw / 2, cx - bw / 2 - off + (j + 1) * brickW)
                if (right - left < brickW * 0.2f) continue
                val drop = (1f - pj) * 8f * dp
                val col = lerp(BuildInk.Brick, BuildInk.BrickLight, rnd(r * 31 + j) * 0.6f)
                // a palace's one missing brick: an empty place, until the last brick is set in it
                if (s.palace > 0f && r == 2 && j == count / 2) {
                    val hole = Rect(left + 0.7f * dp, y0 + 0.7f * dp, right - 0.7f * dp, y1 - 0.7f * dp)
                    drawRect(BuildInk.Opening.copy(alpha = pj), hole.topLeft, hole.size)
                    if (s.brick > 0f) {
                        val q = easeOut(s.brick)
                        val from = Offset(hole.left, h * 0.05f)
                        val tl = Offset(mix(from.x, hole.left, q), mix(from.y, hole.top, q))
                        softGlow(Offset(tl.x + hole.width / 2, tl.y + hole.height / 2), hole.width * 1.6f, ArtInk.Gold, 0.6f * q)
                        drawRect(ArtInk.Gold, tl, hole.size)
                        // the last brick, the seal of the prophets ﷺ: once set, it spreads its light
                        sirajMunir(Offset(tl.x + hole.width / 2, tl.y + hole.height / 2), hole.height * 0.35f, seg(s.brick, 0.75f, 1f), t)
                    }
                    continue
                }
                block(Rect(left + 0.7f * dp, y0 + 0.7f * dp - drop, right - 0.7f * dp, y1 - 0.7f * dp - drop), col, 401 + r * 29 + j * 7 + 101 * si, alpha = pj)
            }
        }
        // the bricks bound into one
        if (s.lock > 0f && rows.isNotEmpty()) {
            val q = easeOut(s.lock) * (1f - fall)
            val top = floor - rows.size * bh
            drawRect(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.28f * q), Color.Transparent), center = Offset(cx, (top + floor) / 2), radius = bw * 0.8f), topLeft = Offset(cx - bw, top - bh), size = Size(bw * 2f, floor - top + 2 * bh))
            for (r in rows.indices) {
                val y = floor - r * bh
                drawLine(ArtInk.Gold.copy(alpha = 0.85f * q), Offset(cx - bw / 2, y), Offset(cx + bw / 2, y), 1.2f * dp)
            }
            drawLine(ArtInk.Gold.copy(alpha = 0.85f * q), Offset(cx - bw / 2, top), Offset(cx + bw / 2, top), 1.2f * dp)
            drawLine(ArtInk.Gold.copy(alpha = 0.85f * q), Offset(cx - bw / 2, top), Offset(cx - bw / 2, floor), 1.2f * dp)
            drawLine(ArtInk.Gold.copy(alpha = 0.85f * q), Offset(cx + bw / 2, top), Offset(cx + bw / 2, floor), 1.2f * dp)
        }
        // door and windows of a house
        if (s.house > 0f) {
            val q = seg(s.house, 0.55f, 0.9f) * (1f - seg(fall, 0f, 0.2f))
            if (q > 0f) {
                drawPath(horseshoe(cx, floor - bh * 1.55f, bw * 0.09f, bw * 0.075f, floor), BuildInk.Opening.copy(alpha = q))
                for (sd in listOf(-1f, 1f)) {
                    drawPath(horseshoe(cx + sd * bw * 0.3f, floor - bh * 3.35f, bw * 0.05f, bw * 0.042f, floor - bh * 2.6f), BuildInk.Opening.copy(alpha = q))
                }
            }
        }
        // roof: a lintel with stepped merlons
        if (roofP > 0f) {
            val q = easeOut(roofP)
            val dy = -(1f - q) * h * 0.06f
            block(Rect(cx - bw / 2 - w * 0.015f, wallTop - h * 0.035f + dy, cx + bw / 2 + w * 0.015f, wallTop + dy), BuildInk.Stone, 701 + 101 * si, alpha = q, shade = false)
            val mw = bw / 13f
            for (m in 0 until 7) {
                val mx = cx - bw / 2 + mw * (0.5f + 2f * m)
                block(Rect(mx, wallTop - h * 0.06f + dy, mx + mw, wallTop - h * 0.035f + dy), BuildInk.StoneLight, 733 + m * 11 + 101 * si, alpha = q)
            }
        }
        // cracks: it still stands
        for ((k, i) in s.cracks.withIndex()) {
            val p = easeOut(a.beatAt(i))
            if (p <= 0f) continue
            val xs = listOf(0.28f, -0.22f, 0.05f, -0.38f, 0.38f)
            val x0 = cx + bw * xs[k % xs.size]
            val level = a.valueAt(i).toInt()          // 0 the walls, 1 the foundation, 2 the roof
            val y0 = when (level) {
                1 -> floor + fH * 0.1f
                2 -> wallTop - h * 0.055f
                else -> wallTop + bh * 0.2f
            }
            val len = when (level) {
                1 -> fH * 0.85f * p
                2 -> h * 0.05f * p
                else -> (floor - y0) * (0.55f + 0.1f * (k % 3)) * p
            }
            val crack = Path().apply {
                moveTo(x0, y0)
                var y = y0
                var x = x0
                var m = 0
                while (y < y0 + len) {
                    y = min(y0 + len, y + min(bh * 0.45f, len / 2f + 0.5f))
                    x += (if (m % 2 == 0) 1f else -1f) * bw * 0.035f
                    lineTo(x, y)
                    m++
                }
            }
            drawPath(crack, Color(0xFF241812), style = Stroke(2.4f * dp, cap = StrokeCap.Round))
            drawPath(crack, ArtInk.Ember.copy(alpha = 0.9f), style = Stroke(1f * dp, cap = StrokeCap.Round))
        }
        // praise that would raise it above its place: a dashed storey, struck out
        if (s.raise > 0f) {
            val q = easeOut(s.raise)
            val gh = h * 0.16f * q
            val gw = bw * 0.8f
            val ghost = Rect(cx - gw / 2, roofTop - gh, cx + gw / 2, roofTop)
            drawRect(ArtInk.Ivory.copy(alpha = 0.06f * q), ghost.topLeft, ghost.size)
            drawRect(ArtInk.Ivory.copy(alpha = 0.7f * q * (1f - 0.6f * s.word)), ghost.topLeft, ghost.size, style = Stroke(1.3f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp))))
            if (s.word > 0f) {
                val c = ghost.center
                val r = min(gw, h * 0.16f) * 0.35f
                val a1 = seg(s.word, 0.2f, 0.6f)
                val a2 = seg(s.word, 0.6f, 1f)
                drawLine(ArtInk.Ember, c + Offset(-r, -r), c + Offset(-r + 2 * r * a1, -r + 2 * r * a1), 2.4f * dp, StrokeCap.Round)
                if (a2 > 0f) drawLine(ArtInk.Ember, c + Offset(r, -r), c + Offset(r - 2 * r * a2, -r + 2 * r * a2), 2.4f * dp, StrokeCap.Round)
            }
            tagAt(a.tag("raise"), Offset(cx + gw / 2 + w * 0.03f + halfTag(a.tag("raise")), roofTop - h * 0.1f), seg(s.raise, 0.3f, 1f), back = TagBack, ink = ArtInk.Ember)
        }
        if (s.word > 0f) tagAt(a.tag("word"), Offset(cx, h * 0.08f), seg(s.word, 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
        if (s.palace > 0f) tagAt(a.tag("palace"), Offset(cx, ground + (h - ground) / 2), seg(s.palace, 0.4f, 1f), back = TagBack, ink = ArtInk.Ivory)
        if (s.brick > 0f) tagAt(a.tag("brick"), Offset(cx + bw / 2 + w * 0.03f + halfTag(a.tag("brick")), floor - 2.5f * bh), seg(s.brick, 0.4f, 1f), back = TagBack, ink = ArtInk.Gold)
        // the station it was given: a line of gold at its top
        if (s.level > 0f) {
            val q = easeOut(s.level)
            val y = roofTop
            drawLine(ArtInk.Gold.copy(alpha = q), Offset(cx - (bw / 2 + w * 0.08f) * q, y), Offset(cx + (bw / 2 + w * 0.08f) * q, y), 2f * dp, StrokeCap.Round)
            for (sd in listOf(-1f, 1f)) drawLine(ArtInk.Gold.copy(alpha = q), Offset(cx + sd * (bw / 2 + w * 0.08f) * q, y - 5f * dp), Offset(cx + sd * (bw / 2 + w * 0.08f) * q, y + 5f * dp), 2f * dp, StrokeCap.Round)
            val tl = a.tag("level")
            tagAt(tl, Offset(cx - bw / 2 - w * 0.03f - halfTag(tl), y - h * 0.07f), seg(s.level, 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
        }
        // dust as it falls
        if (fall > 0f && fall < 1f) {
            for (m in 0 until 7) {
                val q = seg(fall, 0.25f + 0.05f * m, 0.95f)
                if (q <= 0f) continue
                val c = Offset(cx + (m - 3f) * bw * 0.18f, ground - h * 0.02f - q * h * 0.05f)
                drawCircle(Color(0xFFB9A890).copy(alpha = 0.35f * bump(q)), radius = (8f + 18f * q) * dp, center = c)
            }
        }
        // a lamp lit on top: the call goes out
        if (s.light > 0f) {
            val q = easeOut(s.light) * (1f - fall)
            val ls = w * 0.016f
            val ring = Offset(cx, roofTop - 5.3f * ls)
            val c = ring + Offset(0f, 2.9f * ls)
            // the call goes out: light carried far in every direction
            lightShafts(c, 0f, 2f * PI.toFloat(), w * 0.4f * (0.4f + 0.6f * seg(q, 0.2f, 1f)), ArtInk.Gold, q, t, 14)
            bloom(c, 4f * ls, ArtInk.Gold, 0.7f * q)
            lantern(ring, ls, q, 0.05f * sin(t * 9f))
            tagAt(a.tag("light"), ring + Offset(0f, -h * 0.07f), seg(s.light, 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
        }

        // names
        val stripY = ground + (h - ground) / 2
        if (si == 0 || two) {
            tagAt(a.tag("base"), Offset(cx, stripY), seg(s.base, 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
            tagAt(a.tag("house"), Offset(cx, stripY), seg(s.house, 0.4f, 1f) * (1f - seg(fall, 0f, 0.3f)), back = TagBack, ink = ArtInk.Gold)
        }
        val gTl = a.tag("glow")
        if (s.glow > 0f) tagAt(gTl, Offset(cx + bw / 2 + w * 0.03f + halfTag(gTl), floor), seg(s.glow, 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
        val pTl = a.tag("pillars")
        if (n > 0) tagAt(pTl, Offset(cx - bw / 2 - w * 0.02f - halfTag(pTl), floor - ph * 0.55f), seg(s.pillars, 0.4f, 1f), back = TagBack, ink = ArtInk.Ivory)
        val rTl = a.tag("roof")
        if (s.roof > 0f) tagAt(rTl, Offset(cx + bw / 2 + w * 0.03f + halfTag(rTl), wallTop - h * 0.04f), seg(s.roof, 0.3f, 1f), back = TagBack, ink = ArtInk.Ivory)
        for ((r, i) in s.courseItems.withIndex()) {
            val tl = a.tagOf(i)
            val right = r % 2 == 0
            val x = if (right) cx + bw / 2 + w * 0.025f + halfTag(tl) else cx - bw / 2 - w * 0.025f - halfTag(tl)
            tagAt(tl, Offset(x, floor - (r + 0.5f) * bh), seg(a.beatAt(i), 0.3f, 1f), back = TagBack, ink = ArtInk.Ivory)
        }
        if (s.lock > 0f) tagAt(a.tag("lock"), Offset(cx, floor - rows.size * bh - h * 0.13f), seg(s.lock, 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
        if (s.rival > 0f) {
            val rx = cx + (if (si == 1) -1f else 1f) * (bw / 2 + w * 0.07f)
            tagAt(a.tag("rival"), Offset(rx, roofTop - h * 0.07f - s.cracks.size * h * 0.085f), seg(s.rival, 0.3f, 1f), back = TagBack, ink = ArtInk.Ember)
        }
        for ((k, i) in s.cracks.withIndex()) {
            if (k == 0) {
                tagAt(a.tagOf(i), Offset(cx, roofTop - h * 0.07f), seg(a.beatAt(i), 0.3f, 1f), back = TagBack, ink = ArtInk.Ember)
            } else {
                // each further crack is named on the crack itself, while it is told
                val next = s.cracks.getOrNull(k + 1)?.let { a.beatAt(it) } ?: 0f
                val xs = listOf(0.28f, -0.22f, 0.05f, -0.38f, 0.38f)
                val x0 = cx + bw * xs[k % xs.size]
                tagAt(a.tagOf(i), Offset(x0, wallTop + bh * 0.2f + h * 0.03f), seg(a.beatAt(i), 0.2f, 0.5f) * (1f - seg(next, 0f, 0.3f)), back = TagBack, ink = ArtInk.Ember)
            }
        }
        if (fall > 0f) tagAt(a.tag("fall"), Offset(cx, ground - h * 0.2f), seg(fall, 0.5f, 1f), back = TagBack, ink = ArtInk.Ember)
        for ((k, i) in s.names.withIndex()) {
            tagAt(a.tagOf(i), Offset(cx, ground - h * 0.29f - k * h * 0.085f), seg(a.beatAt(i), 0.2f, 0.8f), back = TagBack, ink = ArtInk.Ivory)
        }
        if (s.lower > 0f) tagAt(a.tag("lower"), Offset(cx, roofTop - h * 0.07f - s.cracks.size * h * 0.085f), seg(s.lower, 0.3f, 1f), back = TagBack, ink = ArtInk.Jade)
        val wTl = a.tag("wall")
        if (s.wall > 0f) tagAt(wTl, Offset(cx - bw / 2 - w * 0.025f - halfTag(wTl), floor - 2f * bh), seg(s.wall, 0.4f, 1f), back = TagBack, ink = ArtInk.Ivory)
    }
}

// ───────────── the gate, and the stations on the way to it ─────────────

private object GateInk {
    val SkyTop = Color(0xFF0D1330)
    val SkyMid = Color(0xFF2E2E5E)
    val Dawn = Color(0xFF9A7488)
    val Ground = Color(0xFF1F1D36)
    val GroundEdge = Color(0xFF3A3658)
    val Mound = Color(0xFF2E2A46)
    val Chasm = Color(0xFF060509)
    val Frame = Color(0xFFBFA36A)
    val FrameDark = Color(0xFF6E5532)
    val Door = Color(0xFF5A3D24)
    val DoorLine = Color(0xFF3A2614)
    val Light = Color(0xFFFFF1C8)
    val Paper = Color(0xFFF3E6C4)
}

/**
 * The way to a gate, read from right to left. Beats: trumpet (rings spread through the sky), rise (lights rise
 * from the graves), lights (a procession of lights sets out), filter (it passes through a sieve: what spoils it
 * falls away), scrolls (two scrolls: one given forward, one behind; tag parts "a|b"), scale (the balance),
 * bridge (a narrow bridge over a chasm: most cross, some fall), pass (bright lights go over the reckoning straight
 * to the gate), trait ×n (names along that way), gate (the gate appears), seal ×n (its seals light one by one),
 * shut (a side gate that never opens), open (the gate opens and the lights go in).
 */
internal fun DrawScope.gateScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    val pathY = ground - h * 0.035f
    deepSky(t, ground, GateInk.SkyTop, GateInk.Dawn, glow = Color(0xFFB08AA0), twinkles = false)
    val trumpet = easeOut(a.b("trumpet"))
    for (s in TWINKLES) {
        val shake = trumpet * 1.5f * dp * sin(t * 30f + s.p * 5f)
        drawCircle(ArtInk.Ivory.copy(alpha = 0.55f * (0.5f + 0.5f * sin(t * 1.4f + s.p))), radius = s.r * dp, center = Offset(s.x * w + shake, s.y * ground * 0.75f))
    }

    val hasBridge = a.count("bridge") > 0
    val passing = a.count("pass") > 0
    val hasGate = a.count("gate") + a.count("seal") + a.count("open") + a.count("pass") > 0
    val gateX = if (hasBridge) w * 0.09f else w * 0.17f
    val scaleX = w * 0.5f
    val filterX = w * 0.72f
    val shutX = w * 0.82f
    val bridgeL = w * 0.2f
    val bridgeR = w * 0.42f
    val bridge = a.b("bridge")
    val chasm = easeOut(seg(bridge, 0f, 0.4f))

    // ground, and the chasm the bridge crosses
    drawRect(GateInk.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))
    drawLine(GateInk.GroundEdge, Offset(0f, ground), Offset(w, ground), 1.2f * dp)
    if (chasm > 0f) {
        val mid = (bridgeL + bridgeR) / 2
        val half = (bridgeR - bridgeL) / 2 * chasm
        drawRect(GateInk.Chasm, topLeft = Offset(mid - half, ground - 1f), size = Size(2 * half, h - ground + 1f))
        softGlow(Offset(mid, h * 1.02f), half * 1.2f, ArtInk.Ember, 0.45f * chasm)
    }
    fun bridgeY(x: Float): Float {
        val f = ((x - bridgeL) / (bridgeR - bridgeL)).coerceIn(0f, 1f)
        return pathY + h * 0.025f * 4f * f * (1f - f)
    }
    if (bridge > 0f) {
        val q = seg(bridge, 0.3f, 0.6f)
        val line = Path().apply {
            moveTo(bridgeR, pathY)
            for (k in 1..20) {
                val x = mix(bridgeR, bridgeL, k / 20f)
                lineTo(x, bridgeY(x))
            }
        }
        drawPath(line, ArtInk.Ivory.copy(alpha = 0.85f * q), style = Stroke(1.4f * dp, cap = StrokeCap.Round))
        drawPath(line, ArtInk.Gold.copy(alpha = 0.25f * q), style = Stroke(5f * dp, cap = StrokeCap.Round))
    }

    // the sound that spreads through the sky
    if (trumpet > 0f) {
        val keep = 0.35f + 0.65f * (1f - seg(a.b("trumpet"), 0.7f, 1f))
        clipRect(bottom = ground) {
            for (k in 0 until 4) {
                val q = fract(t * 0.3f + k / 4f)
                drawCircle(ArtInk.Ivory.copy(alpha = 0.45f * trumpet * keep * (1f - q)), radius = q * w * 1.15f, center = Offset(w * 1.02f, -h * 0.05f), style = Stroke(1.5f * dp))
            }
        }
        tagAt(a.tag("trumpet"), Offset(w * 0.8f, h * 0.09f), seg(a.b("trumpet"), 0.2f, 0.8f), back = TagBack, ink = ArtInk.Ivory)
    }

    // graves on the right, lights rising out of them
    val rise = a.b("rise")
    if (a.count("rise") > 0) {
        for (m in 0 until 3) {
            val mx = w * (0.83f + 0.065f * m)
            drawOval(GateInk.Mound, topLeft = Offset(mx - w * 0.028f, ground - h * 0.028f), size = Size(w * 0.056f, h * 0.05f))
            val q = easeOut(seg(rise, m * 0.12f, m * 0.12f + 0.5f))
            if (q > 0f && rise < 1f) {
                val c = Offset(mx, mix(ground, pathY - h * 0.03f, q))
                drawLine(ArtInk.Gold.copy(alpha = 0.35f * (1f - seg(rise, 0.7f, 1f))), Offset(mx, ground), c, 2f * dp, StrokeCap.Round)
                softGlow(c, 9f * dp, ArtInk.Gold, 0.7f)
                drawCircle(ArtInk.Ivory, radius = 2.2f * dp, center = c)
            }
        }
        tagAt(a.tag("rise"), Offset(w * 0.89f, ground + (h - ground) / 2), seg(rise, 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
    }

    // the balance in the middle (faint when the lights only pass it by)
    val noPrice = a.count("noprice") > 0
    val np = a.b("noprice")
    val scaleA = if (a.count("scale") > 0) easeOut(a.b("scale")) else if (noPrice) easeOut(seg(np, 0f, 0.3f)) else if (passing) 0.45f else 0f
    if (scaleA > 0f) {
        val postTop = ground - h * 0.36f
        val col = GateInk.Frame.copy(alpha = scaleA)
        drawLine(col, Offset(scaleX, ground), Offset(scaleX, postTop), 2.4f * dp)
        drawRect(col, topLeft = Offset(scaleX - w * 0.03f, ground - h * 0.012f), size = Size(w * 0.06f, h * 0.012f))
        // not a price: the deeds in one pan cannot outweigh the garden in the other
        val tilt = if (noPrice) 16f * easeInOut(seg(np, 0.15f, 0.5f)) + 1.5f * sin(t * 0.8f) else 7f * sin(t * 0.8f)
        val ang = tilt * PI.toFloat() / 180f
        val arm = w * 0.085f
        val l = Offset(scaleX - cos(ang) * arm, postTop - sin(ang) * arm)
        val r = Offset(scaleX + cos(ang) * arm, postTop + sin(ang) * arm)
        drawLine(col, l, r, 2.2f * dp, StrokeCap.Round)
        drawCircle(col, radius = 3f * dp, center = Offset(scaleX, postTop))
        for (e in listOf(l, r)) {
            val pan = e + Offset(0f, h * 0.15f)
            drawLine(col, e, pan + Offset(-w * 0.03f, 0f), 1f * dp)
            drawLine(col, e, pan + Offset(w * 0.03f, 0f), 1f * dp)
            drawArc(col, 0f, 180f, true, topLeft = pan + Offset(-w * 0.032f, -h * 0.03f), size = Size(w * 0.064f, h * 0.06f))
            drawCircle(ArtInk.Gold.copy(alpha = 0.8f * scaleA), radius = 2.4f * dp, center = pan + Offset(0f, h * 0.006f))
        }
        if (noPrice) {
            val inL = seg(np, 0.05f, 0.3f)
            val lp = l + Offset(0f, h * 0.15f - h * 0.02f)
            for (m in 0 until 3) {
                val c = lp + Offset((m - 1) * w * 0.014f, -h * 0.004f * (m % 2))
                softGlow(c, 6f * dp, ArtInk.Gold, 0.6f * inL)
                drawCircle(ArtInk.Ivory.copy(alpha = inL), radius = 1.8f * dp, center = c)
            }
            val rp = r + Offset(0f, h * 0.15f - h * 0.005f)
            softGlow(rp + Offset(0f, -h * 0.03f), w * 0.05f, GateInk.Light, 0.7f * inL)
            drawPath(horseshoe(rp.x, rp.y - h * 0.05f, w * 0.016f, w * 0.013f, rp.y), GateInk.Light.copy(alpha = inL))
            drawPath(horseshoe(rp.x, rp.y - h * 0.05f, w * 0.016f, w * 0.013f, rp.y), GateInk.Frame.copy(alpha = inL), style = Stroke(1.2f * dp))
            crossAtW2(Offset(scaleX, postTop + h * 0.1f), w * 0.1f, seg(np, 0.55f, 0.9f))
        }
        tagAt(a.tag("scale"), Offset(scaleX, postTop - h * 0.07f), seg(a.b("scale"), 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
    }

    // the sieve: what spoils it falls away
    val fl = easeOut(a.b("filter"))
    if (fl > 0f) {
        val c = Offset(filterX, pathY - h * 0.06f)
        val rw = w * 0.026f
        val rh = h * 0.085f
        clipPath(Path().apply { addOval(Rect(c.x - rw, c.y - rh, c.x + rw, c.y + rh)) }) {
            for (k in -2..2) drawLine(ArtInk.Gold.copy(alpha = 0.3f * fl), Offset(c.x - rw, c.y + k * rh * 0.35f), Offset(c.x + rw, c.y + k * rh * 0.35f), 0.8f * dp)
        }
        drawOval(ArtInk.Gold.copy(alpha = fl), topLeft = Offset(c.x - rw, c.y - rh), size = Size(2 * rw, 2 * rh), style = Stroke(2f * dp))
        for (m in 0 until 6) {
            val q = fract(t * 0.45f + m / 6f)
            val sp = Offset(c.x + (m - 2.5f) * 2.5f * dp, c.y + rh * 0.6f + q * (ground - c.y))
            drawCircle(Color(0xFF6A6478).copy(alpha = 0.9f * fl * (1f - q)), radius = 1.8f * dp, center = sp)
        }
        tagAt(a.tag("filter"), Offset(c.x, c.y - rh - h * 0.06f), seg(a.b("filter"), 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
    }

    // the gate
    val gateA = if (a.count("gate") > 0) easeOut(a.b("gate")) else if (hasGate) 1f else 0f
    val ps = a.b("pass")
    val open = max(easeOut(a.b("open")), 0.35f * easeOut(seg(ps, 0.55f, 1f)))
    val j = w * 0.062f
    val r = j * 1.18f
    val cy = ground - h * 0.31f
    if (gateA > 0f) {
        val frame = horseshoe(gateX, cy, r + w * 0.014f, j + w * 0.014f, ground)
        val opening = horseshoe(gateX, cy, r, j, ground)
        softGlow(Offset(gateX, cy + r * 0.4f), w * 0.2f, GateInk.Light, 0.55f * open * gateA)
        // light pours out of the open gate across the land
        lightShafts(Offset(gateX, cy + r * 0.2f), 0.12f, 1.1f, w * 0.7f, GateInk.Light, open * gateA, t, 8)
        drawPath(frame, GateInk.Frame.copy(alpha = gateA))
        drawPath(frame, GateInk.FrameDark.copy(alpha = gateA), style = Stroke(1.2f * dp))
        drawPath(opening, lerp(Color(0xFF14101C), GateInk.Light, open).copy(alpha = gateA))
        clipPath(opening) {
            val leaf = j * (1f - 0.85f * open)
            for (sd in listOf(-1f, 1f)) {
                val hinge = gateX + sd * j
                val inner = hinge - sd * leaf
                val left = min(hinge, inner)
                drawRect(GateInk.Door.copy(alpha = gateA), topLeft = Offset(left, cy - r * 1.2f), size = Size(leaf, ground - cy + r * 1.2f))
                for (m in 1..3) {
                    val x = left + leaf * m / 4f
                    drawLine(GateInk.DoorLine.copy(alpha = gateA), Offset(x, cy - r * 1.2f), Offset(x, ground), 1f * dp)
                }
            }
            if (open < 0.05f) drawLine(GateInk.DoorLine.copy(alpha = gateA), Offset(gateX, cy - r * 1.2f), Offset(gateX, ground), 1.4f * dp)
        }
        // seals, one on each leaf: lit one by one
        val seals = a.count("seal")
        for (k in 0 until seals) {
            val p = easeOut(a.b("seal", k))
            val c = Offset(gateX + (if (k % 2 == 0) 1f else -1f) * j * 0.5f * (1f - 0.85f * open), cy + r * 0.25f + (k / 2) * h * 0.06f)
            drawCircle(GateInk.FrameDark.copy(alpha = gateA), radius = 5.5f * dp, center = c, style = Stroke(1.4f * dp))
            if (p > 0f) {
                softGlow(c, 16f * dp, ArtInk.Gold, 0.7f * p)
                drawPath(starPath(c, 5f * dp * p), ArtInk.Gold.copy(alpha = p))
            }
            val tl = a.tag("seal", k)
            val tc = Offset(gateX + j + w * 0.04f + halfTag(tl), cy - r * 0.2f + k * h * 0.085f)
            if (p > 0f) drawLine(ArtInk.Gold.copy(alpha = 0.6f * seg(a.b("seal", k), 0.3f, 1f)), c, Offset(tc.x - halfTag(tl), tc.y), 1f * dp)
            tagAt(tl, tc, seg(a.b("seal", k), 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
        }
        tagAt(a.tag("gate"), Offset(max(gateX, halfTag(a.tag("gate")) + 4f * dp), h * 0.1f), seg(a.b("gate"), 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
        tagAt(a.tag("open"), Offset(max(gateX, halfTag(a.tag("open")) + 4f * dp), cy - r - h * 0.07f), seg(a.b("open"), 0.3f, 1f), back = TagBack, ink = ArtInk.Jade)
    }

    // a side gate that never opens
    val sh = easeOut(a.b("shut"))
    val sj = w * 0.034f
    val sr = sj * 1.18f
    val scy = ground - h * 0.17f
    if (sh > 0f) {
        drawPath(horseshoe(shutX, scy, sr + 4f * dp, sj + 4f * dp, ground), GateInk.FrameDark.copy(alpha = sh))
        drawPath(horseshoe(shutX, scy, sr, sj, ground), Color(0xFF15111E).copy(alpha = sh))
        crossAtW2(Offset(shutX, scy + sr * 0.3f), sj * 1.1f, seg(a.b("shut"), 0.45f, 0.9f))
        tagAt(a.tag("shut"), Offset(shutX, scy - sr - h * 0.08f), seg(a.b("shut"), 0.3f, 1f), back = TagBack, ink = ArtInk.Ember)
    }

    // the procession of lights, from right to left
    val flowA = maxOf(easeOut(a.b("lights")), easeOut(seg(rise, 0.45f, 1f)), easeOut(a.b("open")))
    val mainStop = when {
        passing -> scaleX + w * 0.06f
        gateA > 0f -> gateX + j * (1f - open) * 1.4f
        else -> -w * 0.04f
    }
    val shutStop = shutX + sj + 6f * dp
    val nLights = 15
    for (k in 0 until nLights) {
        val toShut = sh > 0f && k % 3 == 0
        val alpha0 = if (toShut) sh else flowA
        if (alpha0 <= 0f) continue
        val stop = if (toShut) shutStop else mainStop
        val u = fract(t * 0.045f + k / nLights.toFloat())
        val x = mix(w * 0.98f, stop, u)
        var y = pathY - 4f * dp + sin(t * 2f + k) * 1.2f * dp
        var col = ArtInk.Gold
        var alpha = alpha0 * seg(u, 0f, 0.08f) * (1f - seg(u, 0.85f, 1f))
        if (bridge > 0.4f && x < bridgeR && x > bridgeL) {
            y = bridgeY(x) - 4f * dp
            val mid = (bridgeL + bridgeR) / 2
            if (k % 4 == 1 && x < mid) {
                val f = (mid - x) / (mid - bridgeL)
                y += f * f * h * 0.3f
                col = ArtInk.Ember
                alpha *= 1f - f
            }
        } else if (bridge > 0.4f && x <= bridgeL && k % 4 == 1) {
            alpha = 0f
        }
        if (alpha <= 0.01f) continue
        val c = Offset(x, y)
        softGlow(c, 9f * dp, col, 0.6f * alpha)
        drawCircle((if (col == ArtInk.Ember) ArtInk.Ember else ArtInk.Ivory).copy(alpha = alpha), radius = 2.1f * dp, center = c)
    }
    if (a.count("lights") > 0) tagAt(a.tag("lights"), Offset(w * 0.86f, ground + (h - ground) / 2), seg(a.b("lights"), 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
    if (hasBridge) tagAt(a.tag("bridge"), Offset((bridgeL + bridgeR) / 2, pathY - h * 0.13f), seg(bridge, 0.4f, 1f), back = TagBack, ink = ArtInk.Ivory)

    // two scrolls: one given forward, one behind
    val sc = a.b("scrolls")
    if (sc > 0f) {
        val from = Offset(w * 0.7f, pathY - h * 0.05f)
        val toA = Offset(w * 0.63f, h * 0.38f)
        val toB = Offset(w * 0.87f, h * 0.46f)
        val qa = easeOut(seg(sc, 0f, 0.7f))
        val qb = easeOut(seg(sc, 0.2f, 0.9f))
        scrollAt(Offset(mix(from.x, toA.x, qa), mix(from.y, toA.y, qa)), -10f * qa, ArtInk.Gold, qa)
        scrollAt(Offset(mix(from.x, toB.x, qb), mix(from.y, toB.y, qb)), 14f * qb, ArtInk.Ember, qb)
        tagAt(a.tag("scrolls", 0, 0), toA + Offset(0f, -h * 0.09f), seg(sc, 0.5f, 1f), back = TagBack, ink = ArtInk.Gold)
        tagAt(a.tag("scrolls", 0, 1), toB + Offset(0f, -h * 0.09f), seg(sc, 0.6f, 1f), back = TagBack, ink = ArtInk.Ember)
    }

    // straight to the gate, over the reckoning
    if (ps > 0f) {
        val p0 = Offset(w * 0.86f, pathY - h * 0.02f)
        val p1 = Offset(w * 0.7f, h * 0.12f)
        val p2 = Offset(w * 0.32f, h * 0.12f)
        val p3 = Offset(gateX + w * 0.02f, ground - h * 0.2f)
        val reveal = easeOut(seg(ps, 0f, 0.6f))
        drawPath(cubicPart(p0, p1, p2, p3, 0f, reveal), ArtInk.Gold.copy(alpha = 0.22f), style = Stroke(7f * dp, cap = StrokeCap.Round))
        drawPath(cubicPart(p0, p1, p2, p3, 0f, reveal), ArtInk.Gold.copy(alpha = 0.8f), style = Stroke(1.3f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp), -t * 20f * dp)))
        for (m in 0 until 8) {
            val s = fract(t * 0.1f + m / 8f)
            if (s > reveal) continue
            val c = cubic(p0, p1, p2, p3, s)
            softGlow(c, 11f * dp, ArtInk.Gold, 0.75f)
            drawCircle(ArtInk.Ivory, radius = 2.4f * dp, center = c)
        }
        tagAt(a.tag("pass"), cubic(p0, p1, p2, p3, 0.5f) + Offset(0f, -h * 0.1f), seg(ps, 0.4f, 1f), back = TagBack, ink = ArtInk.Gold)
        for (k in 0 until a.count("trait")) {
            val tl = a.tag("trait", k)
            tagAt(tl, Offset(max(gateX, halfTag(tl) + 4f * dp), h * 0.07f + k * h * 0.075f), seg(a.b("trait", k), 0.2f, 0.8f), back = TagBack, ink = ArtInk.Jade)
        }
    }
}

/** A scroll, slightly turned, glowing in [col]. */
private fun DrawScope.scrollAt(c: Offset, deg: Float, col: Color, alpha: Float) {
    if (alpha <= 0f) return
    val dp = density
    withTransform({ rotate(deg, c) }) {
        softGlow(c, 26f * dp, col, 0.5f * alpha)
        drawRect(GateInk.Paper.copy(alpha = alpha), topLeft = c + Offset(-14f * dp, -8f * dp), size = Size(28f * dp, 16f * dp))
        for (k in 0 until 3) drawLine(Color(0xFF8C7A5A).copy(alpha = 0.7f * alpha), c + Offset(-9f * dp, (-4f + 4f * k) * dp), c + Offset(9f * dp, (-4f + 4f * k) * dp), 0.8f * dp)
        for (sd in listOf(-1f, 1f)) drawRoundRect(GateInk.Frame.copy(alpha = alpha), topLeft = c + Offset(sd * 15f * dp - 2.5f * dp, -10f * dp), size = Size(5f * dp, 20f * dp), cornerRadius = CornerRadius(2.5f * dp, 2.5f * dp))
    }
}

/** An ember cross that draws itself (0..1). */
private fun DrawScope.crossAtW2(c: Offset, r: Float, p: Float) {
    if (p <= 0f) return
    val a1 = seg(p, 0f, 0.5f)
    val a2 = seg(p, 0.5f, 1f)
    drawLine(ArtInk.Ember, c + Offset(-r, -r), c + Offset(-r + 2 * r * a1, -r + 2 * r * a1), 2.4f * density, StrokeCap.Round)
    if (a2 > 0f) drawLine(ArtInk.Ember, c + Offset(r, -r), c + Offset(r - 2 * r * a2, -r + 2 * r * a2), 2.4f * density, StrokeCap.Round)
}

// ───────────── shrines: lamps that became stones, a grave guarded, a tree of relics ─────────────

private object ShrineInk {
    val SkyTop = Color(0xFF121834)
    val SkyLow = Color(0xFF3A3560)
    val Ground = Color(0xFF25213A)
    val GroundEdge = Color(0xFF3E3858)
    val Stone = Color(0xFF6E6A82)
    val StoneLight = Color(0xFF908BA6)
    val Pedestal = Color(0xFF4E4766)
    val Mound = Color(0xFF4B3F37)
    val MoundLight = Color(0xFF625247)
    val Bark = Color(0xFF4A3628)
    val Leaf = Color(0xFF3E6A4A)
    val LeafLight = Color(0xFF5A8A62)
    val Steel = Color(0xFFC3CAD6)
    val Page = Color(0xFFEDE3C8)
}

/**
 * Night scenes of how shirk crept in and how its roads were shut. Three pictures, chosen by the beats:
 * lamps (lamp: `value` lamps of the righteous; excess: their light pushed beyond measure; names: tag parts under
 * each; out: they go out; stone: standing stones raised in their place; idol: the stones are worshipped; forget:
 * the book of knowledge dims), a grave (dome: a place of worship over it, struck out; feast: festival lights
 * round it, struck out; idol: it starts to be worshipped, and light from above guards it), or a tree (tree: a
 * lote tree hung with swords; rock; shirk: both seen as sources of blessing, struck out; steps: footprints of
 * those before; copy: "make us one like it", a dashed copy; takbir: «Allahu akbar» strikes the copy out).
 */
internal fun DrawScope.shrineScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    nightSky(t, ShrineInk.SkyTop, ShrineInk.SkyLow, 0.7f, 0.8f)
    drawRect(ShrineInk.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))
    drawLine(ShrineInk.GroundEdge, Offset(0f, ground), Offset(w, ground), 1.2f * dp)
    val strip = ground + (h - ground) / 2
    when {
        a.count("garden") + a.count("pit") + a.count("ask") > 0 -> shrineGraves(a)
        a.count("lamp") > 0 -> shrineLamps(a, ground, strip)
        a.count("tree") > 0 || a.count("copy") > 0 -> shrineTree(a, ground, strip)
        else -> shrineGrave(a, ground)
    }
}

private fun DrawScope.shrineLamps(a: ArtCtx, ground: Float, strip: Float) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val li = a.nth("lamp")
    val n = a.valueAt(li).toInt().let { if (it > 1) it else 5 }
    val lampA = easeOut(a.b("lamp"))
    val excess = easeOut(a.b("excess"))
    val out = easeOut(a.b("out"))
    val stone = easeOut(a.b("stone"))
    val idol = easeOut(a.b("idol"))
    val forget = easeOut(a.b("forget"))
    val s = w * 0.017f
    val pedH = h * 0.05f
    val xs = FloatArray(n) { k -> w * (0.82f - 0.6f * k / (n - 1f)) }

    // the book of knowledge, top left: it dims when knowledge is forgotten
    if (a.count("forget") > 0) {
        val c = Offset(w * 0.12f, h * 0.24f)
        val lit = 1f - 0.85f * forget
        softGlow(c, w * 0.09f, ArtInk.Gold, 0.5f * lit)
        for (sd in listOf(-1f, 1f)) {
            val page = Path().apply {
                moveTo(c.x, c.y + 6f * dp)
                lineTo(c.x + sd * 22f * dp, c.y + 2f * dp)
                lineTo(c.x + sd * 22f * dp, c.y - 12f * dp)
                lineTo(c.x, c.y - 8f * dp)
                close()
            }
            drawPath(page, lerp(Color(0xFF5A5468), ShrineInk.Page, lit))
            for (k in 0 until 3) drawLine(Color(0xFF8C7A5A).copy(alpha = 0.6f * lit), Offset(c.x + sd * 5f * dp, c.y - 5f * dp + k * 4f * dp), Offset(c.x + sd * 18f * dp, c.y - 7f * dp + k * 4f * dp), 0.8f * dp)
        }
        tagAt(a.tag("forget"), c + Offset(w * 0.02f, h * 0.1f), seg(a.b("forget"), 0.3f, 1f), back = TagBack, ink = ArtInk.Ember)
    }
    for (k in 0 until n) {
        val x = xs[k]
        val pk = easeOut(seg(a.b("lamp"), k * 0.08f, k * 0.08f + 0.6f))
        if (pk <= 0f) continue
        drawRect(ShrineInk.Pedestal, topLeft = Offset(x - w * 0.03f, ground - pedH), size = Size(w * 0.06f, pedH))
        drawRect(ShrineInk.StoneLight.copy(alpha = 0.5f), topLeft = Offset(x - w * 0.033f, ground - pedH), size = Size(w * 0.066f, 2f * dp))
        // a standing stone raised where the lamp stood
        if (stone > 0f) {
            val sh = h * 0.2f * stone
            drawRoundRect(lerp(ShrineInk.Stone, Color(0xFF7A5048), idol * 0.6f), topLeft = Offset(x - w * 0.022f, ground - pedH - sh), size = Size(w * 0.044f, sh), cornerRadius = CornerRadius(w * 0.022f, w * 0.022f))
            drawRoundRect(ShrineInk.StoneLight.copy(alpha = 0.5f), topLeft = Offset(x - w * 0.022f, ground - pedH - sh), size = Size(w * 0.012f, sh), cornerRadius = CornerRadius(w * 0.006f, w * 0.006f))
        }
        // worship around the stones
        if (idol > 0f) {
            val c = Offset(x, ground - pedH - h * 0.1f)
            softGlow(c, w * 0.06f, ArtInk.Ember, 0.45f * idol)
            for (m in 0 until 2) {
                val ang = t * 1.6f + m * PI.toFloat() + k
                val o = c + Offset(cos(ang) * w * 0.04f, sin(ang) * h * 0.025f + h * 0.06f)
                drawCircle(ArtInk.Ember.copy(alpha = idol), radius = 1.8f * dp, center = o)
            }
        }
        // the lamp of a righteous man, until he dies
        val lampAlpha = pk * (1f - stone)
        if (lampAlpha > 0f) {
            val glow = (0.9f + 0.4f * excess) * (1f - out)
            val ring = Offset(x, ground - pedH - 5.3f * s)
            if (excess > 0f && out < 1f) {
                val c = ring + Offset(0f, 2.9f * s)
                val rr = s * (7f + 5f * excess)
                softGlow(c, rr, lerp(ArtInk.Gold, ArtInk.Ember, 0.5f), 0.4f * excess * (1f - out))
            }
            drawContextAlpha(lampAlpha) { lantern(ring, s, glow, 0.06f * sin(t * 9f + k)) }
        }
        val names = a.nth("names")
        if (names >= 0) tagAt(a.tagOf(names, k), Offset(x, strip), seg(a.beatAt(names), 0.1f + 0.1f * k, 0.5f + 0.1f * k), back = TagBack, ink = ArtInk.Ivory)
    }
    val early = 1f - out
    tagAt(a.tag("lamp"), Offset(w * 0.5f, h * 0.36f), seg(a.b("lamp"), 0.3f, 1f) * early, back = TagBack, ink = ArtInk.Gold)
    tagAt(a.tag("excess"), Offset(w * 0.5f, h * 0.26f), seg(a.b("excess"), 0.3f, 1f) * early, back = TagBack, ink = ArtInk.Ember)
    tagAt(a.tag("out"), Offset(w * 0.5f, h * 0.36f), seg(a.b("out"), 0.3f, 1f) * (1f - stone), back = TagBack, ink = ArtInk.Ivory)
    tagAt(a.tag("stone"), Offset(w * 0.5f, h * 0.36f), seg(a.b("stone"), 0.3f, 1f), back = TagBack, ink = ArtInk.Ivory)
    tagAt(a.tag("idol"), Offset(w * 0.5f, h * 0.26f), seg(a.b("idol"), 0.3f, 1f), back = TagBack, ink = ArtInk.Ember)
}

/** Draws [block] at [alpha] by fading its colours through a layer. */
private inline fun DrawScope.drawContextAlpha(alpha: Float, block: DrawScope.() -> Unit) {
    if (alpha >= 0.99f) {
        block()
        return
    }
    drawContext.canvas.saveLayer(Rect(0f, 0f, size.width, size.height), androidx.compose.ui.graphics.Paint().apply { this.alpha = alpha })
    block()
    drawContext.canvas.restore()
}

private fun DrawScope.shrineGrave(a: ArtCtx, ground: Float) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cx = w * 0.5f
    val dome = a.b("dome")
    val feast = a.b("feast")
    val idol = a.b("idol")
    // what each closed road looks like: appear, struck out, a faint memory
    fun life(p: Float) = easeOut(seg(p, 0f, 0.45f)) * (1f - 0.8f * seg(p, 0.75f, 1f))
    fun strike(p: Float) = seg(p, 0.45f, 0.75f)

    // a place of worship built over the grave
    if (dome > 0f) {
        val q = life(dome)
        val top = ground - h * 0.42f * easeOut(seg(dome, 0f, 0.45f))
        clipRect(top = top) {
            val wall = Rect(cx - w * 0.2f, ground - h * 0.26f, cx + w * 0.2f, ground)
            drawRect(ArtInk.Ivory.copy(alpha = 0.08f * q), wall.topLeft, wall.size)
            drawRect(ArtInk.Ivory.copy(alpha = 0.7f * q), wall.topLeft, wall.size, style = Stroke(1.3f * dp))
            drawArc(ArtInk.Ivory.copy(alpha = 0.7f * q), 180f, 180f, false, topLeft = Offset(cx - w * 0.11f, ground - h * 0.26f - w * 0.11f), size = Size(w * 0.22f, w * 0.22f), style = Stroke(1.3f * dp))
            drawRect(ArtInk.Ivory.copy(alpha = 0.7f * q), topLeft = Offset(cx + w * 0.23f, ground - h * 0.42f), size = Size(w * 0.03f, h * 0.42f), style = Stroke(1.3f * dp))
        }
        val st = strike(dome)
        if (st > 0f) {
            drawLine(ArtInk.Gold, Offset(cx - w * 0.24f, ground - h * 0.02f), Offset(mix(cx - w * 0.24f, cx + w * 0.26f, st), mix(ground - h * 0.02f, ground - h * 0.44f, st)), 2.6f * dp, StrokeCap.Round)
        }
        struckTag(a.tag("dome"), Offset(cx, h * 0.12f), seg(dome, 0.2f, 0.6f), strike(dome))
    }
    // festival lights strung round it
    if (feast > 0f) {
        val q = life(feast)
        val l = Offset(cx - w * 0.3f, ground - h * 0.3f)
        val r = Offset(cx + w * 0.3f, ground - h * 0.3f)
        val sag = h * 0.1f
        val reveal = easeOut(seg(feast, 0f, 0.4f))
        val cord = Path().apply {
            for (k in 0..24) {
                val f = k / 24f * reveal
                val x = mix(r.x, l.x, f)
                val y = r.y + sag * 4f * f * (1f - f)
                if (k == 0) moveTo(x, y) else lineTo(x, y)
            }
        }
        drawPath(cord, ArtInk.Ivory.copy(alpha = 0.5f * q), style = Stroke(1f * dp))
        val cols = listOf(ArtInk.Gold, ArtInk.Ember, ArtInk.Jade)
        for (k in 0 until 11) {
            val f = (k + 0.5f) / 11f
            if (f > reveal) continue
            val c = Offset(mix(r.x, l.x, f), r.y + sag * 4f * f * (1f - f) + 4f * dp)
            val tw = 0.6f + 0.4f * sin(t * 4f + k * 1.7f)
            drawCircle(cols[k % 3].copy(alpha = q * tw), radius = 2.6f * dp, center = c)
        }
        crossAtW2(Offset(cx, ground - h * 0.25f), w * 0.05f, strike(feast))
        struckTag(a.tag("feast"), Offset(w * 0.86f, ground - h * 0.38f), seg(feast, 0.2f, 0.6f), strike(feast))
    }
    // the grave: a low mound and its headstone
    val stoneX = cx + w * 0.09f
    val idolGlow = easeOut(seg(idol, 0f, 0.4f)) * (1f - easeOut(seg(idol, 0.5f, 0.9f)))
    if (idolGlow > 0f) {
        val c = Offset(stoneX, ground - h * 0.08f)
        softGlow(c, w * 0.09f, ArtInk.Ember, 0.55f * idolGlow)
    }
    drawArc(ShrineInk.Mound, 180f, 180f, true, topLeft = Offset(cx - w * 0.12f, ground - h * 0.06f), size = Size(w * 0.24f, h * 0.12f))
    drawArc(ShrineInk.MoundLight, 200f, 60f, false, topLeft = Offset(cx - w * 0.12f, ground - h * 0.06f), size = Size(w * 0.24f, h * 0.12f), style = Stroke(1.2f * dp))
    drawRoundRect(ShrineInk.Stone, topLeft = Offset(stoneX - w * 0.016f, ground - h * 0.13f), size = Size(w * 0.032f, h * 0.13f), cornerRadius = CornerRadius(w * 0.016f, w * 0.016f))
    // worship starting round it, then guarded by light from above
    if (idol > 0f) {
        if (idolGlow > 0f) {
            for (m in 0 until 3) {
                val ang = t * 1.5f + m * 2.1f
                val o = Offset(stoneX + cos(ang) * w * 0.05f, ground - h * 0.07f + sin(ang) * h * 0.025f)
                drawCircle(ArtInk.Ember.copy(alpha = idolGlow), radius = 2f * dp, center = o)
            }
        }
        val guard = easeOut(seg(idol, 0.45f, 0.9f))
        if (guard > 0f) {
            val top = Offset(cx, -h * 0.05f)
            val beam = Path().apply {
                moveTo(top.x - w * 0.03f, 0f)
                lineTo(top.x + w * 0.03f, 0f)
                lineTo(cx + w * 0.17f, ground)
                lineTo(cx - w * 0.17f, ground)
                close()
            }
            drawPath(beam, Brush.verticalGradient(listOf(ArtInk.Gold.copy(alpha = 0.05f * guard), ArtInk.Gold.copy(alpha = 0.3f * guard)), startY = 0f, endY = ground))
            drawArc(ArtInk.Gold.copy(alpha = 0.8f * guard), 180f, 180f, false, topLeft = Offset(cx - w * 0.17f, ground - h * 0.2f), size = Size(w * 0.34f, h * 0.4f), style = Stroke(1.6f * dp))
        }
        struckTag(a.tag("idol"), Offset(w * 0.15f, ground - h * 0.38f), seg(idol, 0.15f, 0.5f), seg(idol, 0.45f, 0.8f))
    }
}

/** A lote tree hung with swords, [alpha] its strength; [dashed] for a copy only asked for. */
private fun DrawScope.loteTree(base: Offset, scaleK: Float, alpha: Float, dashed: Boolean, t: Float) {
    if (alpha <= 0f || scaleK <= 0f) return
    val w = size.width
    val h = size.height
    val dp = density
    val th = h * 0.26f * scaleK
    val cw = w * 0.15f * scaleK
    val top = base + Offset(0f, -th)
    val stroke = if (dashed) Stroke(1.4f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp))) else null
    val trunk = Path().apply {
        moveTo(base.x - 6f * dp * scaleK, base.y)
        cubicTo(base.x - 4f * dp, base.y - th * 0.4f, top.x - 9f * dp * scaleK, top.y + th * 0.3f, top.x - 4f * dp, top.y + 4f * dp)
        lineTo(top.x + 4f * dp, top.y + 4f * dp)
        cubicTo(top.x + 9f * dp * scaleK, top.y + th * 0.3f, base.x + 4f * dp, base.y - th * 0.4f, base.x + 6f * dp * scaleK, base.y)
        close()
    }
    val canopy = Path().apply {
        addOval(Rect(top.x - cw, top.y - th * 0.3f, top.x + cw, top.y + th * 0.12f))
        addOval(Rect(top.x - cw * 0.7f, top.y - th * 0.48f, top.x + cw * 0.55f, top.y - th * 0.02f))
    }
    if (stroke == null) {
        drawPath(trunk, ShrineInk.Bark.copy(alpha = alpha))
        drawPath(canopy, ShrineInk.Leaf.copy(alpha = alpha))
        drawOval(ShrineInk.LeafLight.copy(alpha = 0.4f * alpha), topLeft = Offset(top.x - cw * 0.5f, top.y - th * 0.4f), size = Size(cw * 0.7f, th * 0.18f))
    } else {
        drawPath(trunk, ArtInk.Ivory.copy(alpha = 0.7f * alpha), style = stroke)
        drawPath(canopy, ArtInk.Ivory.copy(alpha = 0.7f * alpha), style = stroke)
    }
    // curved swords hung on the branches by their hilts
    for (k in 0 until 4) {
        val hx = top.x + cw * (-0.7f + 0.45f * k)
        val hy = top.y + th * 0.06f + (if (k % 2 == 0) 0f else th * 0.03f)
        val sway = sin(t * 1.3f + k) * 1.5f * dp
        val col = if (dashed) ArtInk.Ivory.copy(alpha = 0.7f * alpha) else ShrineInk.Steel.copy(alpha = alpha)
        val hilt = Offset(hx + sway * 0.5f, hy + h * 0.025f * scaleK)
        drawLine(col.copy(alpha = col.alpha * 0.6f), Offset(hx, hy), hilt, 0.8f * dp)
        drawLine(lerp(col, ShrineInk.Bark, 0.5f), hilt, hilt + Offset(2f * dp, 6f * dp * scaleK), 2.6f * dp, StrokeCap.Round)
        val b0 = hilt + Offset(2f * dp, 6f * dp * scaleK)
        val blade = Path().apply {
            moveTo(b0.x, b0.y)
            quadraticBezierTo(b0.x + 10f * dp * scaleK + sway, b0.y + h * 0.05f * scaleK, b0.x + 3f * dp + sway, b0.y + h * 0.1f * scaleK)
            quadraticBezierTo(b0.x + 6f * dp * scaleK + sway, b0.y + h * 0.05f * scaleK, b0.x - 1.5f * dp, b0.y + 1f * dp)
            close()
        }
        drawPath(blade, col)
    }
}

private fun DrawScope.shrineTree(a: ArtCtx, ground: Float, strip: Float) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tree = easeOut(a.b("tree"))
    val rock = easeOut(a.b("rock"))
    val shirk = a.b("shirk")
    val stepsP = easeOut(a.b("steps"))
    val copy = a.b("copy")
    val takbir = a.b("takbir")
    val treeBase = Offset(w * 0.24f, ground)
    val rockC = Offset(w * 0.42f, ground)
    // sought for blessing: an ember aura
    val aura = easeOut(seg(shirk, 0f, 0.5f))
    if (aura > 0f) {
        for (c in listOf(treeBase + Offset(0f, -h * 0.2f), rockC + Offset(0f, -h * 0.04f))) {
            softGlow(c, w * 0.14f, ArtInk.Ember, 0.4f * aura)
        }
    }
    // footprints of those before, coming to it
    if (stepsP > 0f) {
        for (k in 0 until 9) {
            val f = k / 8f
            if (f > stepsP) continue
            val c = Offset(mix(w * 0.02f, treeBase.x - w * 0.05f, f), ground + h * 0.04f + (if (k % 2 == 0) 3f else -3f) * dp)
            drawOval(ArtInk.Ember.copy(alpha = 0.75f), topLeft = c - Offset(3.5f * dp, 1.8f * dp), size = Size(7f * dp, 3.6f * dp))
        }
        tagAt(a.tag("steps"), Offset(w * 0.14f, strip + h * 0.03f), seg(a.b("steps"), 0.3f, 1f), back = TagBack, ink = ArtInk.Ember)
    }
    loteTree(treeBase, 0.4f + 0.6f * tree, tree, false, t)
    if (rock > 0f) {
        drawOval(ShrineInk.Stone.copy(alpha = rock), topLeft = rockC + Offset(-w * 0.05f, -h * 0.09f), size = Size(w * 0.1f, h * 0.1f))
        drawOval(ShrineInk.StoneLight.copy(alpha = 0.5f * rock), topLeft = rockC + Offset(-w * 0.035f, -h * 0.08f), size = Size(w * 0.04f, h * 0.03f))
    }
    tagAt(a.tag("tree"), treeBase + Offset(0f, -h * 0.42f), seg(a.b("tree"), 0.3f, 1f), back = TagBack, ink = ArtInk.Ivory)
    tagAt(a.tag("rock"), rockC + Offset(0f, -h * 0.16f), seg(a.b("rock"), 0.3f, 1f), back = TagBack, ink = ArtInk.Ivory)
    if (shirk > 0f) {
        crossAtW2(treeBase + Offset(0f, -h * 0.17f), w * 0.05f, seg(shirk, 0.4f, 0.8f))
        crossAtW2(rockC + Offset(0f, -h * 0.05f), w * 0.03f, seg(shirk, 0.5f, 0.9f))
        tagAt(a.tag("shirk"), Offset(w * 0.34f, ground - h * 0.5f), seg(shirk, 0.3f, 1f), back = TagBack, ink = ArtInk.Ember)
    }
    // "make us one like theirs": a dashed copy on this side, struck out by «Allahu akbar»
    val copyBase = Offset(w * 0.76f, ground)
    if (copy > 0f) {
        val gone = 1f - 0.75f * easeOut(seg(takbir, 0.5f, 1f))
        loteTree(copyBase, 0.5f + 0.4f * easeOut(copy), easeOut(copy) * gone, true, t)
        tagAt(a.tag("copy"), copyBase + Offset(0f, -h * 0.42f), seg(copy, 0.3f, 1f) * (1f - seg(takbir, 0f, 0.3f)), back = TagBack, ink = ArtInk.Ivory)
    }
    if (takbir > 0f) {
        val q = easeOut(seg(takbir, 0f, 0.5f))
        val c = copyBase + Offset(0f, -h * 0.2f)
        for (m in 0 until 12) {
            val ang = (m * 30f) * PI.toFloat() / 180f
            val r0 = w * 0.03f
            val r1 = r0 + w * 0.16f * q
            drawLine(ArtInk.Gold.copy(alpha = 0.5f * (1f - seg(takbir, 0.6f, 1f) * 0.6f)), c + Offset(cos(ang) * r0, sin(ang) * r0), c + Offset(cos(ang) * r1, sin(ang) * r1), 2f * dp, StrokeCap.Round)
        }
        crossAtW2(c, w * 0.08f, seg(takbir, 0.3f, 0.7f))
        tagAt(a.tag("takbir"), copyBase + Offset(0f, -h * 0.42f), seg(takbir, 0.3f, 1f), back = TagBack, ink = ArtInk.Gold)
    }
}

// ───────────── the hourglass of al-'Asr ─────────────

private object GlassInk {
    val BgTop = Color(0xFF17213F)
    val BgLow = Color(0xFF3A2F4E)
    val Sill = Color(0xFF2A2236)
    val Frame = Color(0xFF9C7440)
    val FrameLight = Color(0xFFD2A866)
    val Glass = Color(0xFFE8F0F8)
    val Sand = Color(0xFF8E8678)
}

/**
 * Time running out through an hourglass, lost unless it is spent well. Beats: glass (the sand starts to run,
 * grey: time lost), gold ×n (each good deed turns the next layer of fallen sand to gold, named at its side).
 */
internal fun DrawScope.hourglassScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    deepSky(t, h * 0.9f, GlassInk.BgTop, GlassInk.BgLow, starAlpha = 0.8f)
    val sill = h * 0.9f
    drawRect(GlassInk.Sill, topLeft = Offset(0f, sill), size = Size(w, h - sill))

    val cx = w * 0.4f
    val topY = h * 0.1f
    val botY = h * 0.84f
    val neckY = (topY + botY) / 2
    val bw = w * 0.12f
    val neck = w * 0.007f
    fun bulb(top: Float, bottom: Float, down: Boolean): Path = Path().apply {
        // down: wide at [top], narrowing to the neck at [bottom]; else the mirror
        val wide = if (down) top else bottom
        val narrow = if (down) bottom else top
        moveTo(cx - bw, wide)
        cubicTo(cx - bw * 1.08f, mix(wide, narrow, 0.55f), cx - bw * 0.15f, mix(wide, narrow, 0.85f), cx - neck, narrow)
        lineTo(cx + neck, narrow)
        cubicTo(cx + bw * 0.15f, mix(wide, narrow, 0.85f), cx + bw * 1.08f, mix(wide, narrow, 0.55f), cx + bw, wide)
        close()
    }
    val upper = bulb(topY, neckY, true)
    val lower = bulb(neckY, botY, false)

    val glassP = if (a.count("glass") > 0) easeOut(a.b("glass")) else 1f
    val n = a.count("gold")
    val grey = 0.16f
    val bandFull = if (n > 0) (1f - grey) / n else 0f
    var fallen = grey * glassP
    for (k in 0 until n) fallen += bandFull * a.b("gold", k)
    val capH = (neckY - topY) * 0.82f

    // sand left above
    clipPath(upper) {
        val surface = neckY - capH * (1f - fallen)
        drawRect(GlassInk.Sand, topLeft = Offset(cx - bw * 1.2f, surface), size = Size(bw * 2.4f, neckY - surface))
    }
    // sand fallen below, layer by layer: grey first, then a band of gold for each deed
    var level = botY
    val bands = ArrayList<Pair<Float, Float>>()      // final centre of each band, its colour progress
    clipPath(lower) {
        val g = capH * grey * glassP
        drawRect(GlassInk.Sand, topLeft = Offset(cx - bw * 1.2f, level - g), size = Size(bw * 2.4f, g))
        level -= g
        for (k in 0 until n) {
            val p = a.b("gold", k)
            val hk = capH * bandFull * p
            val col = lerp(GlassInk.Sand, ArtInk.Gold, easeOut(seg(p, 0.15f, 0.8f)))
            drawRect(col, topLeft = Offset(cx - bw * 1.2f, level - hk), size = Size(bw * 2.4f, hk))
            if (p > 0.5f) {
                for (m in 0 until 4) {
                    val tw = 0.5f + 0.5f * sin(t * 3f + m * 1.9f + k)
                    drawCircle(ArtInk.Ivory.copy(alpha = 0.7f * tw * seg(p, 0.5f, 1f)), radius = 1.2f * dp, center = Offset(cx + (m - 1.5f) * bw * 0.35f, level - hk * 0.5f))
                }
            }
            level -= hk
        }
    }
    var lvl = botY - capH * grey
    for (k in 0 until n) {
        bands += (lvl - capH * bandFull / 2) to a.b("gold", k)
        lvl -= capH * bandFull
    }
    // the falling thread, coloured by what is falling now
    val running = (0 until a.size).firstOrNull { a.beatAt(it) > 0f && a.beatAt(it) < 1f }
    if (running != null) {
        val col = if (a.kindAt(running) == "gold") ArtInk.Gold else GlassInk.Sand
        drawLine(col.copy(alpha = 0.8f), Offset(cx, neckY), Offset(cx, level), 1.4f * dp)
        for (m in 0 until 5) {
            val q = fract(t * 1.6f + m / 5f)
            drawCircle(col, radius = 1.3f * dp, center = Offset(cx + sin(m * 2f) * 1f * dp, mix(neckY, level, q)))
        }
    }
    // glass and frame
    for (b in listOf(upper, lower)) {
        drawPath(b, GlassInk.Glass.copy(alpha = 0.07f))
        drawPath(b, GlassInk.Glass.copy(alpha = 0.6f), style = Stroke(1.3f * dp))
    }
    drawLine(GlassInk.Glass.copy(alpha = 0.35f), Offset(cx - bw * 0.7f, topY + h * 0.04f), Offset(cx - bw * 0.55f, neckY - h * 0.12f), 2f * dp, StrokeCap.Round)
    for (y in listOf(topY - h * 0.035f, botY)) {
        drawRoundRect(GlassInk.Frame, topLeft = Offset(cx - bw * 1.3f, y), size = Size(bw * 2.6f, h * 0.035f), cornerRadius = CornerRadius(3f * dp, 3f * dp))
        drawLine(GlassInk.FrameLight, Offset(cx - bw * 1.25f, y + 1.2f * dp), Offset(cx + bw * 1.25f, y + 1.2f * dp), 1f * dp)
    }
    for (sd in listOf(-1f, 1f)) drawLine(GlassInk.Frame, Offset(cx + sd * bw * 1.18f, topY), Offset(cx + sd * bw * 1.18f, botY), 3f * dp)

    // names of the gold layers, with a thread to each
    for (k in 0 until n) {
        val (yb, p) = bands[k]
        val tl = a.tag("gold", k)
        val ty = botY - h * 0.06f - k * h * 0.09f
        val tx = cx + bw * 1.5f + w * 0.03f + halfTag(tl)
        val al = seg(p, 0.3f, 1f)
        if (al > 0f) drawLine(ArtInk.Gold.copy(alpha = 0.6f * al), Offset(cx + bw * 0.6f, yb), Offset(tx - halfTag(tl), ty), 1f * dp)
        tagAt(tl, Offset(tx, ty), al, back = TagBack, ink = ArtInk.Gold)
    }
    tagAt(a.tag("glass"), Offset(cx + bw * 1.5f + w * 0.03f + halfTag(a.tag("glass")), topY + h * 0.08f), seg(a.b("glass"), 0.3f, 1f), back = TagBack, ink = ArtInk.Ivory)
}

/**
 * Two graves seen in the earth. Beats: ask ×n (the questions, written in the sky), garden (the first grave
 * widens and fills with the light of a garden), pit (the second narrows into a pit of fire).
 */
private fun DrawScope.shrineGraves(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val surface = h * 0.46f
    drawRect(Brush.verticalGradient(listOf(Color(0xFF3B2A1E), Color(0xFF1E140E)), startY = surface, endY = h), topLeft = Offset(0f, surface), size = Size(w, h - surface))
    drawLine(Color(0xFF5A4636), Offset(0f, surface), Offset(w, surface), 1.4f * dp)
    for ((k, cx) in listOf(w * 0.7f, w * 0.3f).withIndex()) {
        drawArc(ShrineInk.Mound, 180f, 180f, true, topLeft = Offset(cx - w * 0.09f, surface - h * 0.045f), size = Size(w * 0.18f, h * 0.09f))
        drawRoundRect(ShrineInk.Stone, topLeft = Offset(cx + w * 0.06f, surface - h * 0.1f), size = Size(w * 0.022f, h * 0.1f), cornerRadius = CornerRadius(w * 0.011f, w * 0.011f))
        val p = if (k == 0) a.b("garden") else a.b("pit")
        val q = easeOut(p)
        val chamberW = w * (0.16f + (if (k == 0) 0.16f * q else -0.07f * q))
        val chamberH = h * (0.18f + (if (k == 0) 0.12f * q else -0.05f * q))
        val top = surface + h * 0.08f
        val r = Rect(cx - chamberW / 2, top, cx + chamberW / 2, top + chamberH)
        val col = if (k == 0) lerp(Color(0xFF120C08), Color(0xFF2E5A3A), q) else lerp(Color(0xFF120C08), Color(0xFF4A1A10), q)
        drawRoundRect(col, r.topLeft, r.size, CornerRadius(chamberW * 0.2f, chamberW * 0.2f))
        if (k == 0 && q > 0f) {
            softGlow(r.center, chamberW, Color(0xFF9FE0B0), 0.55f * q)
            for (m in 0 until 7) {
                val fx = r.left + r.width * (0.1f + 0.13f * m)
                val fy = r.bottom - h * 0.02f
                drawLine(ArtInk.Jade.copy(alpha = q), Offset(fx, fy), Offset(fx, fy - h * 0.03f * q), 1.4f * dp, StrokeCap.Round)
                drawCircle(listOf(ArtInk.Gold, ArtInk.Ivory, ArtInk.Jade)[m % 3].copy(alpha = q), radius = 2.2f * dp, center = Offset(fx, fy - h * 0.035f * q))
            }
            tagAt(a.tag("garden"), Offset(cx, surface - h * 0.17f), seg(a.b("garden"), 0.3f, 1f), back = TagBack, ink = ArtInk.Jade)
        }
        if (k == 1 && q > 0f) {
            for (m in 0 until 6) {
                val fx = r.left + r.width * (0.12f + 0.16f * m)
                val fh = h * 0.05f * (0.6f + 0.4f * sin(t * 7f + m))
                drawLine(ArtInk.Ember.copy(alpha = q), Offset(fx, r.bottom), Offset(fx, r.bottom - fh * q), 3f * dp, StrokeCap.Round)
            }
            tagAt(a.tag("pit"), Offset(cx, surface - h * 0.17f), seg(a.b("pit"), 0.3f, 1f), back = TagBack, ink = ArtInk.Ember)
        }
    }
    for (k in 0 until a.count("ask")) {
        tagAt(a.tag("ask", k), Offset(w * 0.5f, h * (0.07f + 0.075f * k)), seg(a.b("ask", k), 0.2f, 0.8f), back = TagBack, ink = ArtInk.Ivory)
    }
}
