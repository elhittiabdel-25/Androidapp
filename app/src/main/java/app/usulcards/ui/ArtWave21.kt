package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
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
 * Wave 21 (2026-10-27): the books section opens with «بصائر», part 1, chapter «الإلحاد وطريق اليقين». A study wall
 * where ten padlocks open one key at a time and the eleventh only to the master key of method; two roads at dawn
 * (the long one over hills of books, the short one to the open Mushaf) shared by the card on certainty and the card
 * on sincerity; a plain at dusk where the four kinds of irreligion stand apart; and the river of the centuries.
 */

// ───────────── shared small drawings of the book scenes ─────────────

/** A plain question mark drawn as a stroke (no font), [h] tall, centred at [c]. */
internal fun DrawScope.qmark21(c: Offset, h: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val r = h * 0.24f
    val top = c + Offset(0f, -h * 0.22f)
    val p = Path().apply {
        arcTo(Rect(top.x - r, top.y - r, top.x + r, top.y + r), 200f, 250f, true)
        lineTo(c.x, c.y + h * 0.12f)
    }
    drawPath(p, color, alpha = alpha, style = Stroke(h * 0.11f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(color, h * 0.07f, c + Offset(0f, h * 0.36f), alpha = alpha)
}

/** A cross drawn over a point (something ruled out). [p] 0..1 draws its two strokes in turn. */
internal fun DrawScope.cross21(c: Offset, r: Float, color: Color, p: Float, width: Float) {
    if (p <= 0f) return
    val a = seg(p, 0f, 0.5f)
    val b = seg(p, 0.5f, 1f)
    drawLine(color, c + Offset(-r, -r), c + Offset(-r + 2 * r * a, -r + 2 * r * a), width, StrokeCap.Round)
    if (b > 0f) drawLine(color, c + Offset(r, -r), c + Offset(r - 2 * r * b, -r + 2 * r * b), width, StrokeCap.Round)
}

/** A check mark at [c]; [p] draws it. */
internal fun DrawScope.check21(c: Offset, r: Float, color: Color, p: Float, width: Float) {
    if (p <= 0f) return
    val a = c + Offset(-r, 0f)
    val m = c + Offset(-r * 0.25f, r * 0.7f)
    val e = c + Offset(r, -r * 0.8f)
    val f1 = seg(p, 0f, 0.4f)
    val f2 = seg(p, 0.4f, 1f)
    drawLine(color, a, a + (m - a) * f1, width, StrokeCap.Round)
    if (f2 > 0f) drawLine(color, m, m + (e - m) * f2, width, StrokeCap.Round)
}

/** A closed book seen from the spine, standing: [c] its centre, [w]×[h], [bands] gold bands. */
internal fun DrawScope.spine21(c: Offset, w: Float, h: Float, color: Color, bands: Int, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, Offset(c.x - w / 2, c.y - h / 2), Size(w, h), CornerRadius(w * 0.12f), alpha = alpha)
    drawRoundRect(Color.Black, Offset(c.x - w / 2, c.y - h / 2), Size(w * 0.18f, h), CornerRadius(w * 0.1f), alpha = 0.18f * alpha)
    for (k in 0 until bands) {
        val y = c.y - h * 0.32f + k * h * 0.09f
        drawLine(ArtInk.BrassLight, Offset(c.x - w * 0.36f, y), Offset(c.x + w * 0.36f, y), h * 0.022f, alpha = 0.9f * alpha)
    }
    drawLine(ArtInk.BrassLight, Offset(c.x - w * 0.36f, c.y + h * 0.36f), Offset(c.x + w * 0.36f, c.y + h * 0.36f), h * 0.02f, alpha = 0.7f * alpha)
}

/** An open book on a stand, [w] wide, its spine at [c]; ruled lines only (never imitation script). */
internal fun DrawScope.openBook21(c: Offset, w: Float, page: Color, rule: Color, alpha: Float, glow: Float = 0f) {
    if (alpha <= 0.01f) return
    if (glow > 0f) softGlow(c, w * 0.9f, ArtInk.Gold, 0.5f * glow * alpha)
    val h = w * 0.36f
    for (side in listOf(-1f, 1f)) {
        val p = Path().apply {
            moveTo(c.x, c.y)
            cubicTo(c.x + side * w * 0.18f, c.y - h * 0.22f, c.x + side * w * 0.36f, c.y - h * 0.2f, c.x + side * w * 0.5f, c.y - h * 0.12f)
            lineTo(c.x + side * w * 0.5f, c.y + h * 0.7f)
            cubicTo(c.x + side * w * 0.34f, c.y + h * 0.6f, c.x + side * w * 0.16f, c.y + h * 0.62f, c.x, c.y + h * 0.8f)
            close()
        }
        drawPath(p, page, alpha = alpha)
        drawPath(p, ArtInk.Brass, alpha = 0.7f * alpha, style = Stroke(w * 0.012f))
        for (r in 0 until 5) {
            val y = c.y + h * (0.02f + r * 0.13f)
            drawLine(rule, Offset(c.x + side * w * 0.08f, y), Offset(c.x + side * w * 0.42f, y - h * 0.04f), w * 0.009f, StrokeCap.Round, alpha = 0.55f * alpha)
        }
    }
}

/** A padlock, body centred at [c], [s] its body width; [open] lifts the shackle (0..1). */
internal fun DrawScope.padlock21(c: Offset, s: Float, body: Color, open: Float, alpha: Float = 1f, rot: Float = 0f) {
    if (alpha <= 0.01f) return
    withTransform({ rotate(rot, c) }) {
        val lift = s * 0.32f * open
        val sh = Path().apply {
            moveTo(c.x - s * 0.3f, c.y - s * 0.3f - lift * (if (open > 0f) 1f else 0f))
            lineTo(c.x - s * 0.3f, c.y - s * 0.62f - lift)
            arcTo(Rect(c.x - s * 0.3f, c.y - s * 0.92f - lift, c.x + s * 0.3f, c.y - s * 0.32f - lift), 180f, 180f, false)
            lineTo(c.x + s * 0.3f, c.y - s * 0.3f - lift * 0.2f)
        }
        drawPath(sh, Color(0xFF8D96A6), alpha = alpha, style = Stroke(s * 0.13f, cap = StrokeCap.Round))
        drawRoundRect(body, Offset(c.x - s / 2, c.y - s * 0.38f), Size(s, s * 0.78f), CornerRadius(s * 0.12f), alpha = alpha)
        drawRoundRect(Color.White, Offset(c.x - s / 2, c.y - s * 0.38f), Size(s, s * 0.2f), CornerRadius(s * 0.12f), alpha = 0.18f * alpha)
        drawCircle(Color(0xFF221A10), s * 0.09f, c + Offset(0f, s * 0.02f), alpha = alpha)
        drawLine(Color(0xFF221A10), c + Offset(0f, s * 0.05f), c + Offset(0f, s * 0.2f), s * 0.07f, StrokeCap.Round, alpha = alpha)
    }
}

/** A key pointing left, its bow at [c], [s] long. */
internal fun DrawScope.key21(c: Offset, s: Float, color: Color, alpha: Float = 1f, rot: Float = 0f) {
    if (alpha <= 0.01f) return
    withTransform({ rotate(rot, c) }) {
        drawCircle(color, s * 0.16f, c, alpha = alpha, style = Stroke(s * 0.07f))
        drawLine(color, c + Offset(-s * 0.15f, 0f), c + Offset(-s * 0.95f, 0f), s * 0.07f, StrokeCap.Round, alpha = alpha)
        drawLine(color, c + Offset(-s * 0.8f, 0f), c + Offset(-s * 0.8f, s * 0.16f), s * 0.07f, StrokeCap.Round, alpha = alpha)
        drawLine(color, c + Offset(-s * 0.66f, 0f), c + Offset(-s * 0.66f, s * 0.11f), s * 0.07f, StrokeCap.Round, alpha = alpha)
    }
}

// ═════════════ 1. ten locks, the eleventh, and the master key (bs-manhaj) ═════════════

private object LockC21 {
    val Wall = Color(0xFFE9DDC4)
    val WallLow = Color(0xFFD4C3A0)
    val Shelf = Color(0xFF8A6238)
    val Board = Color(0xFF6E4A2A)
    val Brick = Color(0xFFC08A5E)
    val BrickDark = Color(0xFF9A6A44)
    val Lock = Color(0xFFB98A3A)
    val Red = Color(0xFFB0473A)
    val Key = Color(0xFF7B8494)
    val Back = Color(0xE6201810)
    val Vol = listOf(Color(0xFF1F3A5E), Color(0xFF2E5A44), Color(0xFF6A2632))
}

private fun lockAt21(w: Float, h: Float, k: Int): Offset = Offset(w * (0.09f + (k % 5) * 0.085f), h * (0.5f + (k / 5) * 0.2f))

/**
 * A study wall by day. Beats: volumes (three volumes slide onto the shelf, one, two and three gold bands), arch
 * (loose bricks fly in and set into an arch, its keystone glowing), tenlocks (ten padlocks on a board each opened
 * by its own key, one after another), eleventh (an eleventh, larger lock appears; a key tries it, does not fit and
 * shakes; question marks), masterkey (the gold master key «الْمَنْهَجُ» turns in it: it opens, and all the locks glow).
 */
internal fun DrawScope.locksScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "volumes" to Shot(Offset(w * 0.5f, h * 0.24f), 1.5f),
        "arch" to Shot(Offset(w * 0.8f, h * 0.62f), 1.45f),
        "tenlocks" to Shot(Offset(w * 0.3f, h * 0.62f), 1.35f),
        "eleventh" to Shot(Offset(w * 0.6f, h * 0.62f), 1.5f),
        "masterkey" to Shot(Offset(w * 0.45f, h * 0.6f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { locksWall(a) }
}

private fun DrawScope.locksWall(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val vo = a.b("volumes")
    val ar = a.b("arch")
    val tl = a.b("tenlocks")
    val el = a.b("eleventh")
    val mk = a.b("masterkey")
    drawRect(Brush.verticalGradient(listOf(LockC21.Wall, LockC21.WallLow)))
    softGlow(Offset(w * 0.5f, h * 0.15f), w * 0.4f, Color(0xFFFFF2D0), 0.5f)
    // the shelf and its three volumes
    drawRect(LockC21.Shelf, Offset(w * 0.3f, h * 0.33f), Size(w * 0.4f, h * 0.025f))
    for (k in 0 until 3) {
        val f = easeOut(seg(vo, k * 0.15f, k * 0.15f + 0.45f))
        if (f <= 0f) continue
        val x = mix(w * 0.95f, w * (0.43f + k * 0.07f), f)
        spine21(Offset(x, h * 0.245f), w * 0.055f, h * 0.17f, LockC21.Vol[k], k + 1, f)
    }
    if (vo > 0.7f) glint(Offset(w * 0.57f, h * 0.17f), w * 0.03f, Color.White, bump(seg(vo, 0.7f, 1f)))
    // the arch: loose bricks that fly in and set
    if (ar > 0f) {
        val c = Offset(w * 0.8f, h * 0.82f)
        val r = w * 0.11f
        val n = 9
        for (k in 0 until n) {
            val ang = PI.toFloat() * (1f - k / (n - 1f))
            val f = easeOut(seg(ar, k * 0.05f, k * 0.05f + 0.45f))
            val home = c + Offset(cos(ang) * r, -sin(ang) * r)
            val from = Offset(w * (0.55f + 0.05f * (k % 4)), h * (0.98f - 0.04f * (k % 3)))
            val p = from + (home - from) * f
            withTransform({ rotate(-(ang * 180f / PI.toFloat()) + 90f + (1f - f) * 70f, p) }) {
                drawRoundRect(if (k == n / 2) ArtInk.Gold else LockC21.Brick, Offset(p.x - w * 0.018f, p.y - h * 0.03f), Size(w * 0.036f, h * 0.06f), CornerRadius(w * 0.004f))
                drawRoundRect(LockC21.BrickDark, Offset(p.x - w * 0.018f, p.y - h * 0.03f), Size(w * 0.036f, h * 0.06f), CornerRadius(w * 0.004f), style = Stroke(w * 0.003f))
            }
        }
        for (side in listOf(-1f, 1f)) {
            val f = easeOut(seg(ar, 0f, 0.3f))
            drawRect(LockC21.Brick, Offset(c.x + side * r - w * 0.018f, c.y), Size(w * 0.036f, h * 0.16f * f))
        }
        val key = easeOut(seg(ar, 0.6f, 1f))
        softGlow(c + Offset(0f, -r), w * 0.05f, ArtInk.Gold, 0.6f * key)
    }
    // the board of ten locks
    val board = easeOut(seg(tl, 0f, 0.15f)).coerceAtLeast(if (el > 0f || mk > 0f) 1f else 0f)
    if (board > 0f) {
        drawRoundRect(LockC21.Board, Offset(w * 0.04f, h * 0.4f), Size(w * 0.44f, h * 0.42f), CornerRadius(w * 0.01f), alpha = board)
        for (k in 0 until 10) {
            val c = lockAt21(w, h, k)
            val open = easeOut(seg(tl, 0.12f + k * 0.08f, 0.2f + k * 0.08f))
            // its own key slides in, turns, falls away
            val kin = seg(tl, 0.06f + k * 0.08f, 0.14f + k * 0.08f)
            if (kin > 0f && kin < 1f) key21(c + Offset(w * 0.06f * (1f - kin), h * 0.01f), w * 0.05f, LockC21.Key, 1f, rot = 90f * seg(kin, 0.6f, 1f))
            padlock21(c, w * 0.045f, LockC21.Lock, open, board)
            val all = easeOut(seg(mk, 0.55f, 0.9f))
            if (all > 0f) softGlow(c, w * 0.04f, ArtInk.Gold, 0.5f * all)
        }
    }
    // the eleventh lock: no key of the ten fits it
    if (el > 0f) {
        val c = Offset(w * 0.6f, h * 0.62f)
        val shake = if (mk <= 0f) sin(t * 40f) * 6f * bump(seg(el, 0.35f, 0.75f)) else 0f
        val open = easeOut(seg(mk, 0.35f, 0.6f))
        val body = lerpColor(LockC21.Red, LockC21.Lock, open)
        padlock21(c, w * 0.09f, body, open, easeOut(seg(el, 0f, 0.2f)), rot = shake)
        // a plain key tries it and bounces back
        val tryK = seg(el, 0.2f, 0.7f)
        if (tryK > 0f && mk <= 0f) {
            val dx = w * 0.12f * (1f - easeOut(seg(tryK, 0f, 0.4f))) + w * 0.05f * easeOut(seg(tryK, 0.5f, 1f))
            key21(c + Offset(w * 0.07f + dx, h * 0.01f), w * 0.08f, LockC21.Key, 1f - seg(tryK, 0.85f, 1f))
        }
        val q = seg(el, 0.55f, 0.8f) * (1f - seg(mk, 0f, 0.3f))
        for (k in 0 until 3) qmark21(c + Offset(w * (-0.05f + 0.05f * k), -h * (0.17f + 0.03f * (k % 2))), h * 0.07f, LockC21.Red, q)
        // the master key of method
        if (mk > 0f) {
            val come = easeOut(seg(mk, 0f, 0.35f))
            val kp = c + Offset(w * 0.08f + w * 0.2f * (1f - come), h * 0.01f)
            softGlow(kp, w * 0.06f, ArtInk.Gold, 0.55f * come)
            key21(kp, w * 0.1f, ArtInk.Gold, come, rot = 90f * seg(mk, 0.35f, 0.5f))
            tagAt(a.tag("masterkey"), kp + Offset(w * 0.02f, -h * 0.11f), seg(mk, 0.1f, 0.3f), back = LockC21.Back, ink = ArtInk.BrassLight)
        }
    }
}

// ═════════════ 2. two roads at dawn (bs-yaqin, bs-ikhlas) ═════════════

private object RoadC21 {
    val SkyTop = Color(0xFF6E86BE)
    val SkyMid = Color(0xFFE9B9A0)
    val SkyLow = Color(0xFFFFE6B8)
    val Land = Color(0xFFB9A57C)
    val LandLow = Color(0xFF8E7C58)
    val Road = Color(0xFFE8D6AE)
    val RoadEdge = Color(0xFF9C8458)
    val Ink = Color(0xFF2A2430)
    val Ghost = Color(0xFF6A6470)
    val Page = Color(0xFFFFF6DE)
    val Rule = Color(0xFF9A8A6A)
    val Back = Color(0xE6201810)
    val Books = listOf(Color(0xFF6A3A2E), Color(0xFF2E4A6A), Color(0xFF4A5A2E), Color(0xFF6A5A2E), Color(0xFF3A2E5A))
}

/** The short road: from the traveller's start (bottom right) straight to the open Mushaf at dawn. */
private fun shortRoad21(w: Float, h: Float, s: Float): Offset = Offset(mix(w * 0.88f, w * 0.5f, s), mix(h * 0.9f, h * 0.6f, s))

/** The long road: winding to the left over the hills of books before it comes back. */
private fun longRoad21(w: Float, h: Float, s: Float): Offset {
    val p0 = Offset(w * 0.88f, h * 0.9f)
    val p1 = Offset(w * 0.1f, h * 0.95f)
    val p2 = Offset(w * 0.05f, h * 0.35f)
    val p3 = Offset(w * 0.38f, h * 0.58f)
    return cubic(p0, p1, p2, p3, s)
}

/**
 * Two roads at dawn from one start. Certainty (yaqin) beats: tworoads (both roads draw in: a long one winding over
 * hills of stacked books, a short straight one to the open Mushaf on its stand in the dawn light), wird (the
 * traveller takes the short road and reads; a ribbon marks «نِصْفَ حِزْبٍ»), tafsir (a small book opens beside it,
 * a dotted line from a line of the page to it), nextyear (a calendar leaf turns and a second book opens), calmheart
 * (question marks around him fade, he sits, his heart lit). Sincerity (ikhlas) beats: race (two runners set off on
 * the road; the first keeps turning to a cheering crowd and drifts off), sincere (the steady one reaches the dawn),
 * steps3 (he climbs three steps, «الْإِخْلَاصُ», «التَّقْوَى», «الصَّبْرُ»; others follow behind him), satchel (four
 * books go into his bag, each title shown in turn).
 */
internal fun DrawScope.roadsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "tworoads" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "wird" to Shot(Offset(w * 0.52f, h * 0.55f), 1.5f),
        "tafsir" to Shot(Offset(w * 0.56f, h * 0.55f), 1.7f),
        "nextyear" to Shot(Offset(w * 0.6f, h * 0.55f), 1.6f),
        "calmheart" to Shot(Offset(w * 0.55f, h * 0.6f), 1.35f),
        "race" to Shot(Offset(w * 0.68f, h * 0.72f), 1.3f),
        "sincere" to Shot(Offset(w * 0.52f, h * 0.6f), 1.4f),
        "steps3" to Shot(Offset(w * 0.4f, h * 0.5f), 1.3f),
        "satchel" to Shot(Offset(w * 0.5f, h * 0.55f), 1.55f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.55f), 1f), dim = 0.12f) { roadsDawn(a) }
}

private fun DrawScope.roadsDawn(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val yaqin = a.count("wird") > 0
    val two = if (yaqin) a.b("tworoads") else 1f
    val horizon = h * 0.52f
    drawRect(Brush.verticalGradient(listOf(RoadC21.SkyTop, RoadC21.SkyMid, RoadC21.SkyLow), endY = horizon), size = Size(w, horizon))
    val dawn = Offset(w * 0.5f, horizon)
    softGlow(dawn, w * 0.3f, Color(0xFFFFE0A0), 0.75f)
    drawRect(Brush.verticalGradient(listOf(RoadC21.Land, RoadC21.LandLow), startY = horizon, endY = h), Offset(0f, horizon), Size(w, h - horizon))
    // hills of stacked books along the long road
    for (k in 0 until 4) {
        val base = Offset(w * (0.06f + 0.11f * k), h * (0.62f + 0.07f * (k % 2)))
        for (m in 0 until 5 - (k % 2)) {
            val bw = w * (0.1f - m * 0.012f)
            drawRoundRect(RoadC21.Books[(k + m) % 5], Offset(base.x - bw / 2, base.y - (m + 1) * h * 0.035f), Size(bw, h * 0.032f), CornerRadius(h * 0.004f), alpha = 0.85f * two)
        }
    }
    // the two roads
    val lr = easeOut(seg(two, 0f, 0.6f))
    val lpts = List(41) { longRoad21(w, h, it / 40f * lr) }
    drawPath(w8Poly(lpts), RoadC21.RoadEdge, alpha = 0.7f, style = Stroke(h * 0.035f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(w8Poly(lpts), RoadC21.Road, alpha = 0.8f, style = Stroke(h * 0.025f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    val sr = easeOut(seg(two, 0.3f, 0.9f))
    val a0 = shortRoad21(w, h, 0f)
    val a1 = shortRoad21(w, h, sr)
    drawLine(RoadC21.RoadEdge, a0, a1, h * 0.06f, StrokeCap.Round)
    drawLine(RoadC21.Road, a0, a1, h * 0.048f, StrokeCap.Round)
    if (yaqin) roadsYaqin(a, dawn) else roadsIkhlas(a, dawn)
    // a little dust of light in the dawn air
    for (k in 0 until 8) {
        val f = fract(t * 0.05f + k / 8f)
        drawCircle(Color.White, 1.2f * dp, Offset(w * (0.3f + 0.05f * k), horizon - h * 0.3f * f), alpha = 0.4f * sin(PI.toFloat() * f))
    }
}

private fun DrawScope.roadsYaqin(a: ArtCtx, dawn: Offset) {
    val w = size.width
    val h = size.height
    val t = a.t
    val two = a.b("tworoads")
    val wi = a.b("wird")
    val tf = a.b("tafsir")
    val ny = a.b("nextyear")
    val st = a.b("calmheart")
    val book = Offset(w * 0.5f, h * 0.53f)
    // the Mushaf on its stand
    val shown = easeOut(seg(two, 0.5f, 1f))
    drawLine(Color(0xFF6A4A2A), book + Offset(-w * 0.035f, h * 0.1f), book + Offset(w * 0.035f, h * 0.03f), w * 0.006f, alpha = shown)
    drawLine(Color(0xFF6A4A2A), book + Offset(w * 0.035f, h * 0.1f), book + Offset(-w * 0.035f, h * 0.03f), w * 0.006f, alpha = shown)
    openBook21(book, w * 0.12f, RoadC21.Page, RoadC21.Rule, shown, glow = 0.6f + 0.4f * easeOut(seg(wi, 0.4f, 1f)))
    // the ribbon: half a hizb
    val rib = easeOut(seg(wi, 0.5f, 0.75f))
    if (rib > 0f) {
        drawLine(Color(0xFF2E7A56), book + Offset(0f, h * 0.02f), book + Offset(w * 0.005f, h * 0.02f + h * 0.08f * rib), w * 0.006f, StrokeCap.Round)
        tagAt(a.tag("wird"), book + Offset(-w * 0.1f, h * 0.12f), seg(wi, 0.6f, 0.8f), back = RoadC21.Back, ink = ArtInk.BrassLight)
    }
    // the tafsir, then next year's tafsir
    for ((k, p) in listOf(tf, ny).withIndex()) {
        if (p <= 0f) continue
        val c = book + Offset(w * (0.11f + 0.11f * k), h * 0.08f)
        val o = easeOut(seg(p, 0f, 0.4f))
        openBook21(c, w * 0.08f, Color(0xFFF2ECDC), RoadC21.Rule, o)
        val dot = seg(p, 0.3f, 0.8f)
        for (m in 0 until 8) {
            val f = m / 8f
            if (f > dot) break
            drawCircle(ArtInk.Brass, w * 0.0025f, book + Offset(w * 0.03f, h * 0.025f) + (c - book - Offset(w * 0.03f, 0f)) * f)
        }
        tagAt(a.tag(if (k == 0) "tafsir" else "nextyear"), c + Offset(0f, h * 0.1f), seg(p, 0.35f, 0.6f), back = RoadC21.Back, ink = ArtInk.BrassLight)
    }
    if (ny > 0f) {
        // a calendar leaf turning over
        val cal = book + Offset(w * 0.27f, -h * 0.08f)
        drawRoundRect(Color.White, cal + Offset(-w * 0.025f, -h * 0.04f), Size(w * 0.05f, h * 0.07f), CornerRadius(w * 0.004f), alpha = easeOut(seg(ny, 0f, 0.2f)))
        drawRect(Color(0xFFC0463A), cal + Offset(-w * 0.025f, -h * 0.04f), Size(w * 0.05f, h * 0.014f), alpha = easeOut(seg(ny, 0f, 0.2f)))
        val flip = seg(ny, 0.15f, 0.45f)
        if (flip in 0.01f..0.99f) withTransform({ scale(1f, 1f - flip, pivot = cal + Offset(0f, -h * 0.04f)) }) {
            drawRect(Color(0xFFF2F2F2), cal + Offset(-w * 0.025f, -h * 0.026f), Size(w * 0.05f, h * 0.056f))
        }
    }
    // the traveller: takes the short road, reads, then sits at peace
    val walk = easeInOut(seg(wi, 0f, 0.5f))
    val foot = shortRoad21(w, h, 0.05f + 0.75f * walk) + Offset(0f, h * 0.01f)
    val tall = h * 0.19f
    val doubt = (1f - seg(st, 0.1f, 0.7f)) * (if (wi > 0f) 1f else 0f) * (if (st > 0f) 1f else 0.6f)
    if (st > 0.4f) {
        w8Seated(foot + Offset(0f, -h * 0.01f), tall * 0.9f, RoadC21.Ink, 1f, 1f)
    } else {
        figure(foot, tall, RoadC21.Ink, if (two > 0f) 1f else 0f, walk = if (wi in 0.01f..0.5f) t * 6f else 0f, arms = if (wi > 0.5f) 0.6f else 0f)
    }
    for (k in 0 until 4) {
        val ang = t * 0.6f + k * 1.6f
        val q = foot + Offset(cos(ang) * w * 0.06f, -tall * 0.8f + sin(ang) * h * 0.05f)
        qmark21(q, h * 0.045f, RoadC21.Ghost, 0.7f * doubt)
    }
    if (st > 0f) {
        val heart = foot + Offset(0f, -tall * 0.5f)
        softGlow(heart, tall * 0.25f, ArtInk.Gold, 0.8f * easeOut(seg(st, 0.3f, 0.9f)))
        drawPath(w8Heart(heart, tall * 0.06f), ArtInk.Gold.copy(alpha = easeOut(seg(st, 0.3f, 0.9f))))
    }
}

private fun DrawScope.roadsIkhlas(a: ArtCtx, dawn: Offset) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ra = a.b("race")
    val si = a.b("sincere")
    val s3 = a.b("steps3")
    val sa = a.b("satchel")
    val tall = h * 0.18f
    // the crowd on the left of the road, cheering the first runner
    val crowd = bump(seg(ra, 0.2f, 1.2f)).coerceAtLeast(if (ra > 0.6f && si <= 0f) 0.6f else 0f)
    for (k in 0 until 4) figure(Offset(w * (0.62f + 0.04f * k), h * 0.68f), tall * 0.6f, RoadC21.Ghost, 0.8f * crowd, arms = if (sin(t * 6f + k) > 0f) 1f else -0.4f)
    // runner A: ahead at first, keeps turning to the crowd and drifts off
    if (ra > 0f) {
        val s = 0.45f * easeOut(seg(ra, 0f, 0.4f))
        val drift = easeInOut(seg(ra, 0.45f, 1f))
        val pA = shortRoad21(w, h, s) + Offset(-w * 0.08f * drift, -h * 0.06f * drift)
        figure(pA, tall, RoadC21.Ghost, 1f - 0.4f * drift, walk = if (ra < 0.45f) t * 8f else 0f, arms = if (drift > 0.1f) 1f else 0f)
    }
    // runner B: steady, then up the steps, then packing his books
    val sB = when {
        si > 0f -> 0.3f + 0.6f * easeInOut(seg(si, 0f, 0.8f))
        else -> 0.3f * easeOut(seg(ra, 0f, 1f))
    }
    var foot = shortRoad21(w, h, sB)
    val stepBase = Offset(w * 0.45f, h * 0.62f)
    if (s3 > 0f) {
        // three steps rising to the left of the dawn
        for (k in 0 until 3) {
            val f = easeOut(seg(s3, k * 0.1f, k * 0.1f + 0.3f))
            val x = stepBase.x - k * w * 0.08f
            val y = stepBase.y - k * h * 0.07f
            drawRect(Color(0xFFE6D3A8), Offset(x - w * 0.08f, y), Size(w * 0.08f, (stepBase.y - y) + h * 0.07f), alpha = f)
            drawLine(ArtInk.Brass, Offset(x - w * 0.08f, y), Offset(x, y), 1.5f * density, alpha = f)
            tagAt(a.tag("steps3", 0, k), Offset(x - w * 0.04f, y + h * 0.035f), seg(s3, 0.2f + k * 0.2f, 0.35f + k * 0.2f), back = RoadC21.Back, ink = ArtInk.BrassLight)
        }
        val up = easeInOut(seg(s3, 0.25f, 0.95f))
        val k = min(2f, up * 3f)
        foot = Offset(stepBase.x - w * 0.04f - k * w * 0.08f, stepBase.y - k * h * 0.07f)
        // others follow behind him
        for (m in 0 until 3) {
            val f = seg(s3, 0.6f + m * 0.1f, 0.9f + m * 0.1f)
            figure(Offset(w * (0.6f + 0.06f * m), h * 0.66f), tall * 0.85f, RoadC21.Ghost, f, walk = t * 5f + m)
        }
    }
    if (ra > 0f || si > 0f || s3 > 0f || sa > 0f) {
        val moving = (ra in 0.01f..0.99f) || (si in 0.01f..0.8f) || (s3 in 0.25f..0.95f)
        figure(foot, tall, RoadC21.Ink, 1f, walk = if (moving) t * 6f else 0f)
        val heart = foot + Offset(0f, -tall * 0.62f)
        val lit = max(easeOut(seg(si, 0.5f, 1f)), if (s3 > 0f || sa > 0f) 1f else 0f)
        softGlow(heart, tall * 0.22f, ArtInk.Gold, 0.75f * lit)
        drawPath(w8Heart(heart, tall * 0.055f), ArtInk.Gold.copy(alpha = 0.4f + 0.6f * lit))
    }
    // the satchel and four books
    if (sa > 0f) {
        val bag = Offset(w * 0.5f, h * 0.84f)
        val o = easeOut(seg(sa, 0f, 0.15f))
        drawRoundRect(Color(0xFF7A5230), bag + Offset(-w * 0.07f, -h * 0.06f), Size(w * 0.14f, h * 0.11f), CornerRadius(w * 0.02f), alpha = o)
        drawLine(Color(0xFF5A3A20), bag + Offset(-w * 0.05f, -h * 0.06f), bag + Offset(w * 0.05f, -h * 0.06f), 2f * density, alpha = o)
        val cols = listOf(Color(0xFF1F3A5E), Color(0xFF2E5A44), Color(0xFF6A2632), Color(0xFF5A4A1E))
        var shown = -1
        for (k in 0 until 4) {
            val f = easeInOut(seg(sa, 0.12f + k * 0.2f, 0.3f + k * 0.2f))
            if (seg(sa, 0.12f + k * 0.2f, 0.32f + k * 0.2f) > 0f) shown = k
            val from = Offset(w * (0.25f + 0.17f * k), h * 0.3f)
            val to = bag + Offset(w * (-0.04f + 0.027f * k), -h * 0.06f)
            val p = from + (to - from) * f
            spine21(p, w * 0.03f, h * 0.1f, cols[k], 2, if (f >= 1f) 0.9f else 1f)
        }
        if (shown >= 0) tagAt(a.tag("satchel", 0, shown), Offset(w * 0.5f, h * 0.22f), seg(sa, 0.12f + shown * 0.2f, 0.2f + shown * 0.2f), back = RoadC21.Back, ink = ArtInk.BrassLight)
    }
}

// ═════════════ 3. the plain at dusk: the kinds of irreligion (bs-ilhad) ═════════════

private object ForkC21 {
    val SkyTop = Color(0xFF283A6A)
    val SkyLow = Color(0xFFC88A7A)
    val Land = Color(0xFF5A5040)
    val LandLow = Color(0xFF3A3228)
    val Road = Color(0xFFB8A27A)
    val Ink = Color(0xFF14121A)
    val Plaque = Color(0xFFE9DDBE)
    val Red = Color(0xFFD0503C)
    val Back = Color(0xE6141826)
}

private fun forkSpot21(w: Float, h: Float, k: Int): Offset = when (k) {
    0 -> Offset(w * 0.78f, h * 0.84f)
    1 -> Offset(w * 0.58f, h * 0.8f)
    2 -> Offset(w * 0.38f, h * 0.84f)
    else -> Offset(w * 0.18f, h * 0.82f)
}

/**
 * A plain at dusk under the first stars, a lit road running to the horizon. Beats: deny5 (a signpost of five
 * plaques — «الْخَالِقِ»، «الْغَيْبِ»، «النُّبُوَّاتِ»، «الْبَعْثِ»، «الْحِسَابِ» — each crossed out in red in turn),
 * atheist (a figure with his back to the signpost, arms folded), deist (a figure looks up at the stars, which
 * brighten, then walks past an open book on its stand without a glance), agnostic (a figure stands still at a fork,
 * a question mark over him), indifferent (a figure sits with his back to it all, lit by a small screen), allfour (a
 * thread runs round the four and their name is shown once). Each figure carries its name while it is spoken of.
 */
internal fun DrawScope.forkScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "deny5" to Shot(Offset(w * 0.5f, h * 0.45f), 1.25f),
        "atheist" to Shot(Offset(forkSpot21(w, h, 0).x - w * 0.05f, h * 0.65f), 1.6f),
        "deist" to Shot(Offset(forkSpot21(w, h, 1).x, h * 0.55f), 1.4f),
        "agnostic" to Shot(Offset(forkSpot21(w, h, 2).x, h * 0.66f), 1.6f),
        "indifferent" to Shot(Offset(forkSpot21(w, h, 3).x + w * 0.04f, h * 0.68f), 1.6f),
        "allfour" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.55f), 1f), dim = 0.16f) { forkPlain(a) }
}

private fun DrawScope.forkPlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val dn = a.b("deny5")
    val at = a.b("atheist")
    val de = a.b("deist")
    val ag = a.b("agnostic")
    val ind = a.b("indifferent")
    val all = a.b("allfour")
    val horizon = h * 0.6f
    drawRect(Brush.verticalGradient(listOf(ForkC21.SkyTop, ForkC21.SkyLow), endY = horizon), size = Size(w, horizon))
    val starBoost = easeOut(seg(de, 0.1f, 0.5f)) * (1f - 0.5f * seg(de, 0.6f, 1f))
    for (k in 0 until 40) {
        val x = fract(sin(k * 12.9898f) * 43758.5f)
        val y = fract(sin(k * 78.233f) * 12345.6f)
        drawCircle(Color.White, (0.8f + 1.4f * starBoost) * dp, Offset(w * x, horizon * 0.75f * y), alpha = (0.35f + 0.3f * sin(t * 2f + k)) * (0.6f + starBoost))
    }
    drawRect(Brush.verticalGradient(listOf(ForkC21.Land, ForkC21.LandLow), startY = horizon, endY = h), Offset(0f, horizon), Size(w, h - horizon))
    // the road to the horizon
    drawPath(Path().apply {
        moveTo(w * 0.47f, horizon); lineTo(w * 0.53f, horizon); lineTo(w * 0.62f, h); lineTo(w * 0.38f, h); close()
    }, Brush.verticalGradient(listOf(ForkC21.Road.copy(alpha = 0.5f), ForkC21.Road), startY = horizon, endY = h))
    softGlow(Offset(w * 0.5f, horizon), w * 0.12f, Color(0xFFFFD9A0), 0.5f)
    // the signpost with its five plaques
    val post = Offset(w * 0.5f, h * 0.18f)
    val ps = easeOut(seg(dn, 0f, 0.15f)).coerceAtLeast(if (at > 0f || de > 0f) 1f else 0f)
    if (ps > 0f) {
        drawLine(Color(0xFF6A4A2A), Offset(post.x, post.y), Offset(post.x, horizon + h * 0.02f), 3f * dp, alpha = ps)
        for (k in 0 until 5) {
            val y = post.y + k * h * 0.075f
            val side = if (k % 2 == 0) -1f else 1f
            val c = Offset(post.x + side * w * 0.07f, y)
            drawRoundRect(ForkC21.Plaque, c + Offset(-w * 0.065f, -h * 0.028f), Size(w * 0.13f, h * 0.056f), CornerRadius(h * 0.01f), alpha = ps)
            tagAt(a.tag("deny5", 0, k), c, ps, back = Color.Transparent, ink = Color(0xFF2A2018))
            val x = seg(dn, 0.2f + k * 0.14f, 0.3f + k * 0.14f)
            cross21(c, h * 0.025f, ForkC21.Red, x, 2.4f * dp)
        }
    }
    val tall = h * 0.2f
    // 1. the atheist: back to the signpost, arms folded
    if (at > 0f || all > 0f) {
        val p = forkSpot21(w, h, 0)
        withTransform({ scale(-1f, 1f, pivot = p) }) { figure(p, tall, ForkC21.Ink, easeOut(seg(at, 0f, 0.3f)).coerceAtLeast(if (all > 0f) 1f else 0f), arms = -0.8f) }
        tagAt(a.tag("atheist"), p + Offset(0f, -tall * 1.25f), seg(at, 0.2f, 0.5f) * (1f - seg(de, 0f, 0.2f)) + seg(all, 0.6f, 0.8f) * 0f, back = ForkC21.Back, ink = ArtInk.Ivory)
    }
    // 2. the deist: looks up at the stars, then walks past the book
    if (de > 0f || all > 0f) {
        val book = Offset(w * 0.66f, h * 0.73f)
        openBook21(book, w * 0.06f, Color(0xFFF6EED8), Color(0xFF9A8A6A), easeOut(seg(de, 0f, 0.3f)).coerceAtLeast(if (all > 0f) 1f else 0f))
        val walk = easeInOut(seg(de, 0.55f, 0.95f))
        val p = forkSpot21(w, h, 1) + Offset(w * 0.1f * walk, 0f)
        val look = bump(seg(de, 0.05f, 0.6f))
        figure(p, tall, ForkC21.Ink, easeOut(seg(de, 0f, 0.2f)).coerceAtLeast(if (all > 0f) 1f else 0f), walk = if (walk in 0.01f..0.99f) t * 5f else 0f, lean = -0.25f * look)
        if (look > 0f) glint(p + Offset(-w * 0.03f, -tall * 1.6f), w * 0.04f, Color.White, look)
        tagAt(a.tag("deist"), p + Offset(0f, -tall * 1.25f), seg(de, 0.15f, 0.4f) * (1f - seg(ag, 0f, 0.2f)), back = ForkC21.Back, ink = ArtInk.Ivory)
    }
    // 3. the agnostic: stands still where the paths part, a question over him
    if (ag > 0f || all > 0f) {
        val p = forkSpot21(w, h, 2)
        val al = easeOut(seg(ag, 0f, 0.2f)).coerceAtLeast(if (all > 0f) 1f else 0f)
        for (side in listOf(-1f, 1f)) drawLine(ForkC21.Road, p + Offset(0f, h * 0.01f), p + Offset(side * w * 0.09f, h * 0.12f), h * 0.02f, StrokeCap.Round, alpha = 0.7f * al)
        figure(p, tall, ForkC21.Ink, al, arms = -0.3f * sin(t * 2f))
        qmark21(p + Offset(0f, -tall * 1.25f), h * 0.07f, ArtInk.Ivory, seg(ag, 0.2f, 0.4f) * (1f - seg(ind, 0f, 0.3f)) + 0.4f * seg(all, 0f, 0.3f))
        tagAt(a.tag("agnostic"), p + Offset(0f, h * 0.08f), seg(ag, 0.2f, 0.4f) * (1f - seg(ind, 0f, 0.2f)), back = ForkC21.Back, ink = ArtInk.Ivory)
    }
    // 4. the indifferent: sits with his back to it all, lit by a small screen
    if (ind > 0f || all > 0f) {
        val p = forkSpot21(w, h, 3)
        val al = easeOut(seg(ind, 0f, 0.25f)).coerceAtLeast(if (all > 0f) 1f else 0f)
        withTransform({ scale(-1f, 1f, pivot = p) }) { w8Seated(p, tall * 0.9f, ForkC21.Ink, 1f, al) }
        val screen = p + Offset(-w * 0.04f, -tall * 0.5f)
        softGlow(screen, w * 0.04f, Color(0xFF8AC8FF), 0.6f * al * (0.85f + 0.15f * sin(t * 9f)))
        drawRoundRect(Color(0xFFBEE2FF), screen + Offset(-w * 0.008f, -h * 0.015f), Size(w * 0.016f, h * 0.03f), CornerRadius(w * 0.003f), alpha = al)
        tagAt(a.tag("indifferent"), p + Offset(0f, -tall * 1.15f), seg(ind, 0.2f, 0.4f) * (1f - seg(all, 0f, 0.2f)), back = ForkC21.Back, ink = ArtInk.Ivory)
    }
    // the thread round all four, and their common name
    if (all > 0f) {
        val f = easeOut(seg(all, 0f, 0.6f))
        val c = Offset(w * 0.48f, h * 0.76f)
        val rx = w * 0.4f
        val ry = h * 0.16f
        val path = Path().apply {
            val n = 48
            for (m in 0..(n * f).toInt()) {
                val ang = 2f * PI.toFloat() * m / n
                val q = c + Offset(cos(ang) * rx, sin(ang) * ry)
                if (m == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y)
            }
        }
        drawPath(path, ArtInk.BrassLight, alpha = 0.85f, style = Stroke(2f * dp, cap = StrokeCap.Round))
        tagAt(a.tag("allfour"), Offset(w * 0.5f, h * 0.5f), seg(all, 0.5f, 0.8f), back = ForkC21.Back, ink = ArtInk.BrassLight)
    }
}

// ═════════════ 4. the river of the centuries (bs-ilhad-tarikh) ═════════════

private object CentC21 {
    val Sky = Color(0xFFDCE6EE)
    val SkyLow = Color(0xFFF4EEDC)
    val Land = Color(0xFFCBBE98)
    val River = Color(0xFF7FA6C4)
    val RiverLight = Color(0xFFB8D4E6)
    val Dome = Color(0xFFEDE3C8)
    val DomeEdge = Color(0xFF9A8A6A)
    val Ink = Color(0xFF2A2430)
    val Ghost = Color(0xFF7A7480)
    val Smoke = Color(0xFF5A5660)
    val Back = Color(0xE6201810)
}

/** A point on the river of time, [s] 0 (the far past, right) .. 1 (now, left). */
private fun timeAt21(w: Float, h: Float, s: Float): Offset = Offset(mix(w * 0.96f, w * 0.04f, s), h * 0.62f + h * 0.05f * sin(s * 6f))

/**
 * The river of the centuries flowing from the right (the past) to the left (now), along its banks small domes
 * and figures at prayer, each with a small light. Beats: ages (the camera runs down the river past the lit shrines
 * of every age; a flag rises near the end with «الْقَرْنِ الثَّامِنَ عَشَرَ»), twist (beside the flag a scroll of
 * ruled lines is wrung by a grey gust into knots), trustlost (the figures by it turn and walk away, their lights going
 * out), thinkers (three books on a lectern, «مَارْكْس»، «دَارْوِين»، «دُورْكَايْم», smoke rising from them), newcoat
 * (a white lab coat drops onto a grey figure: «الْإِلْحَادِ الْعِلْمِيِّ»).
 */
internal fun DrawScope.centuriesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ag = a.b("ages")
    val pan = easeInOut(seg(ag, 0f, 0.8f))
    val shots = List(a.size) { i ->
        when (a.kindAt(i)) {
            "ages" -> Shot(Offset(mix(w * 0.75f, w * 0.3f, pan), h * 0.58f), 1.45f)
            "twist" -> Shot(Offset(w * 0.3f, h * 0.5f), 1.6f)
            "trustlost" -> Shot(Offset(w * 0.28f, h * 0.58f), 1.4f)
            "thinkers" -> Shot(Offset(w * 0.18f, h * 0.45f), 1.6f)
            "newcoat" -> Shot(Offset(w * 0.12f, h * 0.55f), 1.6f)
            else -> null
        }
    }
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.55f), 1f), dim = 0.12f) { centuriesRiver(a) }
}

private fun DrawScope.centuriesRiver(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ag = a.b("ages")
    val tw = a.b("twist")
    val tr = a.b("trustlost")
    val th = a.b("thinkers")
    val nc = a.b("newcoat")
    drawRect(Brush.verticalGradient(listOf(CentC21.Sky, CentC21.SkyLow), endY = h * 0.5f), size = Size(w, h * 0.5f))
    drawRect(CentC21.Land, Offset(0f, h * 0.5f), Size(w, h * 0.5f))
    // the river
    val pts = List(41) { timeAt21(w, h, it / 40f) }
    drawPath(w8Poly(pts), CentC21.River, style = Stroke(h * 0.07f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    for (k in 0 until 10) {
        val f = fract(k / 10f + t * 0.03f)
        val p = timeAt21(w, h, f)
        drawLine(CentC21.RiverLight, p + Offset(w * 0.01f, 0f), p + Offset(-w * 0.02f, 0f), 1.4f * dp, StrokeCap.Round, alpha = 0.7f)
    }
    // the shrines of the ages, lit; those after the eighteenth century lose their light
    val reveal = easeOut(seg(ag, 0f, 0.8f))
    val lost = easeInOut(seg(tr, 0.2f, 0.9f))
    for (k in 0 until 9) {
        val s = 0.04f + k * 0.085f
        if (s > reveal + 0.05f) continue
        val p = timeAt21(w, h, s) + Offset(0f, -h * 0.1f - h * 0.04f * (k % 2))
        drawRoundRect(CentC21.Dome, p + Offset(-w * 0.025f, -h * 0.02f), Size(w * 0.05f, h * 0.05f), CornerRadius(w * 0.004f))
        drawArc(CentC21.Dome, 180f, 180f, true, p + Offset(-w * 0.022f, -h * 0.05f), Size(w * 0.044f, h * 0.06f))
        drawArc(CentC21.DomeEdge, 180f, 180f, false, p + Offset(-w * 0.022f, -h * 0.05f), Size(w * 0.044f, h * 0.06f), style = Stroke(1f * dp))
        val late = k >= 7
        val light = if (late) 1f - lost else 1f
        softGlow(p + Offset(0f, -h * 0.01f), w * 0.03f, ArtInk.Gold, 0.55f * light)
        w8Worshipper(p + Offset(w * 0.04f, h * 0.035f), h * 0.07f, CentC21.Ink, 0f, 0.8f, 1f - (if (late) lost else 0f))
    }
    // the flag at the eighteenth century
    val flagS = 0.7f
    val fp = timeAt21(w, h, flagS) + Offset(0f, h * 0.06f)
    val up = easeOut(seg(ag, 0.7f, 0.95f)).coerceAtLeast(if (tw > 0f) 1f else 0f)
    if (up > 0f) {
        drawLine(Color(0xFF5A4030), fp, fp + Offset(0f, h * 0.14f * up), 1.6f * dp)
        tagAt(a.tag("ages"), fp + Offset(0f, h * 0.2f), seg(ag, 0.8f, 1f) * (1f - seg(tw, 0f, 0.3f)), back = CentC21.Back, ink = ArtInk.BrassLight)
    }
    // the scroll wrung into knots
    if (tw > 0f) {
        val c = Offset(w * 0.3f, h * 0.4f)
        val o = easeOut(seg(tw, 0f, 0.2f))
        val wring = easeInOut(seg(tw, 0.25f, 0.85f))
        drawRoundRect(Color(0xFFF6EED8), c + Offset(-w * 0.07f, -h * 0.1f), Size(w * 0.14f, h * 0.2f), CornerRadius(w * 0.006f), alpha = o)
        for (r in 0 until 6) {
            val y = c.y - h * 0.075f + r * h * 0.03f
            val p = Path().apply {
                moveTo(c.x - w * 0.055f, y)
                for (m in 1..12) {
                    val x = c.x - w * 0.055f + m * w * 0.11f / 12f
                    lineTo(x, y + sin(m * 1.3f + r + t * 3f) * h * 0.02f * wring)
                }
            }
            drawPath(p, Color(0xFF7A6A50), alpha = o, style = Stroke(1.3f * dp, cap = StrokeCap.Round))
        }
        // the grey gust
        for (k in 0 until 4) {
            val f = fract(t * 0.6f + k * 0.25f)
            drawArc(CentC21.Smoke, 200f, 120f, false, c + Offset(w * (0.06f - 0.15f * f), -h * 0.06f + k * h * 0.03f), Size(w * 0.08f, h * 0.06f), alpha = 0.5f * bump(f) * bump(seg(tw, 0.1f, 1.2f)).coerceAtLeast(if (tw > 0.5f) 0.6f else 0f), style = Stroke(2f * dp, cap = StrokeCap.Round))
        }
    }
    // the people turning away from the text
    if (tr > 0f) {
        for (k in 0 until 3) {
            val walk = easeInOut(seg(tr, k * 0.1f, 0.7f + k * 0.1f))
            val p = Offset(w * (0.36f + 0.05f * k) + w * 0.12f * walk, h * 0.62f + h * 0.02f * (k % 2))
            withTransform({ scale(-1f, 1f, pivot = p) }) { figure(p, h * 0.14f, CentC21.Ink, 1f - 0.5f * walk, walk = if (walk in 0.01f..0.99f) t * 5f + k else 0f) }
        }
    }
    // the three books of the nineteenth century, smoking
    if (th > 0f) {
        val lect = Offset(w * 0.18f, h * 0.5f)
        drawLine(Color(0xFF6A4A2A), lect + Offset(0f, h * 0.02f), lect + Offset(0f, h * 0.14f), 2.5f * dp, alpha = easeOut(seg(th, 0f, 0.2f)))
        val cols = listOf(Color(0xFF6A2632), Color(0xFF2E4A2E), Color(0xFF3A3A5A))
        for (k in 0 until 3) {
            val f = easeOut(seg(th, 0.1f + k * 0.2f, 0.3f + k * 0.2f))
            val c = lect + Offset(w * (-0.04f + 0.04f * k), -h * 0.06f)
            spine21(c, w * 0.032f, h * 0.13f, cols[k], 1, f)
            tagAt(a.tag("thinkers", 0, k), c + Offset(0f, -h * (0.13f + 0.045f * k)), seg(th, 0.15f + k * 0.2f, 0.3f + k * 0.2f), back = CentC21.Back, ink = ArtInk.Ivory)
            for (m in 0 until 3) {
                val ff = fract(t * 0.4f + m / 3f + k * 0.2f)
                drawCircle(CentC21.Smoke, w * (0.006f + 0.01f * ff), c + Offset(sin(ff * 6f + k) * w * 0.01f, -h * 0.08f - ff * h * 0.12f), alpha = 0.35f * f * (1f - ff))
            }
        }
    }
    // the same old idea in a new coat
    if (nc > 0f) {
        val p = Offset(w * 0.1f, h * 0.75f)
        figure(p, h * 0.2f, CentC21.Ghost, easeOut(seg(nc, 0f, 0.2f)))
        val drop = easeOut(seg(nc, 0.15f, 0.55f))
        val top = p + Offset(0f, -h * 0.17f - h * 0.2f * (1f - drop))
        val coat = Path().apply {
            moveTo(top.x - w * 0.025f, top.y)
            lineTo(top.x + w * 0.025f, top.y)
            lineTo(top.x + w * 0.035f, top.y + h * 0.12f)
            lineTo(top.x - w * 0.035f, top.y + h * 0.12f)
            close()
        }
        drawPath(coat, Color.White, alpha = 0.95f * drop)
        drawLine(Color(0xFFB0B4C0), top, top + Offset(0f, h * 0.12f), 1f * dp, alpha = drop)
        // a small flask on the pocket
        drawPath(Path().apply {
            moveTo(top.x + w * 0.012f, top.y + h * 0.05f); lineTo(top.x + w * 0.02f, top.y + h * 0.05f)
            lineTo(top.x + w * 0.028f, top.y + h * 0.075f); lineTo(top.x + w * 0.004f, top.y + h * 0.075f); close()
        }, Color(0xFF6AB8E0), alpha = drop)
        tagAt(a.tag("newcoat"), p + Offset(w * 0.06f, -h * 0.28f), seg(nc, 0.5f, 0.75f), back = CentC21.Back, ink = ArtInk.Ivory)
    }
}
