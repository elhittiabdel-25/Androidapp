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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Round 16 of the redraw (2026-10-27): «أركان الإيمان». Eight new scenes: six medallions joined in one rosette, from
 * which the whole of belief branches out; three courts one inside the other, with five, six and one pillar; a verse
 * on a page that no pen, eraser, question or likeness may touch; a glass jar of light that rises and falls, keeps its
 * light through sins, breaks only when a sin is made lawful, and the grain that is left; a seed known, written,
 * willed and created; a man whose heart, tongue and limbs all carry faith, and the two who keep only one part; five
 * people whose light is a sun, a star, a torch, a lamp and a weak lamp; and the angels in their ranks, three of them
 * bringing life to hearts, to the earth and to the dead, and two who write. Allah is never drawn: only His name, as
 * a word. Angels are cool white light; his ﷺ light always carries his name.
 */

private fun DrawScope.dash28(on: Float = 5f, off: Float = 4f): PathEffect = PathEffect.dashPathEffect(floatArrayOf(on * density, off * density))

/** A small closed book lying flat, its title as a tag beside it (a scholar is drawn as his book). */
private fun DrawScope.flatBook28(c: Offset, s: Float, cover: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Color(0xFFF2E8D0), Offset(c.x - s * 0.5f, c.y - s * 0.16f + s * 0.06f), Size(s, s * 0.32f), CornerRadius(s * 0.04f), alpha = alpha)
    drawRoundRect(cover, Offset(c.x - s * 0.52f, c.y - s * 0.2f), Size(s * 1.04f, s * 0.3f), CornerRadius(s * 0.05f), alpha = alpha)
    drawLine(Color(0xFFE2BC5A), Offset(c.x - s * 0.3f, c.y - s * 0.05f), Offset(c.x + s * 0.3f, c.y - s * 0.05f), s * 0.03f, alpha = alpha)
}

/** A small balance (the Mizan as a plain symbol). */
private fun DrawScope.balance28(c: Offset, s: Float, color: Color, tilt: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val sw = s * 0.06f
    drawLine(color, c + Offset(0f, -s * 0.45f), c + Offset(0f, s * 0.45f), sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, c + Offset(-s * 0.3f, s * 0.45f), c + Offset(s * 0.3f, s * 0.45f), sw, StrokeCap.Round, alpha = alpha)
    val l = c + Offset(-s * 0.5f, -s * 0.35f + s * 0.1f * tilt)
    val r = c + Offset(s * 0.5f, -s * 0.35f - s * 0.1f * tilt)
    drawLine(color, l, r, sw, StrokeCap.Round, alpha = alpha)
    for (p in listOf(l, r)) {
        drawLine(color, p, p + Offset(-s * 0.14f, s * 0.3f), sw * 0.5f, alpha = alpha)
        drawLine(color, p, p + Offset(s * 0.14f, s * 0.3f), sw * 0.5f, alpha = alpha)
        drawArc(color, 0f, 180f, true, Offset(p.x - s * 0.17f, p.y + s * 0.22f), Size(s * 0.34f, s * 0.16f), alpha = alpha)
    }
}

// ═════════════ 1. six medallions in one rosette, and all belief branching from them (iman-pillars) ═════════════

private object SixC28 {
    val Parch = Color(0xFFF4E9CF)
    val ParchLow = Color(0xFFE2CFA4)
    val Ink = Color(0xFF6A4A22)
    val Gold = Color(0xFFC8962E)
    val Well = Color(0xFF1E2A4A)
    val Rim = Color(0xFFD2A445)
    val Leaf = Color(0xFF7A8A3A)
    val Back = Color(0xE6201810)
}

private fun medal28(w: Float, h: Float, k: Int): Offset {
    val ang = (-90f + 60f * k) * PI.toFloat() / 180f
    return Offset(w * 0.5f + cos(ang) * w * 0.3f, h * 0.47f + sin(ang) * h * 0.33f)
}

/**
 * A page of geometric ornament. Beats: threeroots (three medallions are drawn and joined: the name «اللَّهُ» as a word,
 * a cool white light for the angels, a bound book for the books), threemore (three more: small warm lights for the
 * messengers, a balance for the Last Day, a pen over a ruled tablet for the decree; the six close into a rosette),
 * branchout (from every medallion fine gold stems branch out and leaf, filling the page: what is believed of Allah,
 * of the return and of the rest of the unseen).
 */
internal fun DrawScope.sixrootsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threeroots" to Shot(Offset(w * 0.62f, h * 0.38f), 1.22f),
        "threemore" to Shot(Offset(w * 0.38f, h * 0.62f), 1.22f),
        "branchout" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { sixrootsPage(a) }
}

private fun DrawScope.sixrootsPage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val r3 = a.b("threeroots")
    val m3 = a.b("threemore")
    val br = a.b("branchout")
    drawRect(Brush.radialGradient(listOf(SixC28.Parch, SixC28.ParchLow), Offset(w * 0.5f, h * 0.45f), w * 0.75f))
    drawRect(SixC28.Gold, Offset(w * 0.025f, h * 0.03f), Size(w * 0.95f, h * 0.94f), alpha = 0.5f, style = Stroke(1.5f * dp))
    val c = Offset(w * 0.5f, h * 0.47f)
    val r = w * 0.072f
    val shown = List(6) { k -> if (k < 3) easeOut(seg(r3, k * 0.25f, k * 0.25f + 0.35f)) else easeOut(seg(m3, (k - 3) * 0.25f, (k - 3) * 0.25f + 0.35f)) }
    // the branches: drawn under the medallions, from each one outward and inward
    if (br > 0f) {
        for (k in 0 until 6) {
            val p = medal28(w, h, k)
            val out = Offset(p.x - c.x, p.y - c.y).let { it / max(1f, it.getDistance()) }
            for (j in 0 until 4) {
                val f = easeOut(seg(br, 0.05f + j * 0.12f + k * 0.03f, 0.4f + j * 0.12f + k * 0.03f))
                if (f <= 0f) continue
                val side = if (j % 2 == 0) 1f else -1f
                val perp = Offset(-out.y, out.x) * side
                val len = w * (0.1f + 0.03f * j)
                val start = p + out * r * 0.9f
                val end = start + out * len * (if (j >= 2) -0.9f else 1f) + perp * len * 0.55f
                val ctrl = start + out * len * 0.5f * (if (j >= 2) -1f else 1f) + perp * len * 0.05f
                val stem = Path()
                for (m in 0..16) {
                    val u = m / 16f * f
                    val q = start * ((1 - u) * (1 - u)) + ctrl * (2 * u * (1 - u)) + end * (u * u)
                    if (m == 0) stem.moveTo(q.x, q.y) else stem.lineTo(q.x, q.y)
                }
                drawPath(stem, SixC28.Gold, alpha = 0.85f, style = Stroke(1.3f * dp, cap = StrokeCap.Round))
                if (f > 0.6f) {
                    val tip = start * ((1 - f) * (1 - f)) + ctrl * (2 * f * (1 - f)) + end * (f * f)
                    val lf = seg(f, 0.6f, 1f)
                    drawOval(SixC28.Leaf, Offset(tip.x - w * 0.009f * lf, tip.y - h * 0.006f * lf), Size(w * 0.018f * lf, h * 0.012f * lf), alpha = 0.9f)
                    drawCircle(SixC28.Gold, w * 0.004f * lf, tip + perp * w * 0.012f, alpha = 0.9f)
                }
            }
        }
        val o = seg(br, 0.5f, 0.75f)
        tagAt(a.tag("branchout", 0, 0), Offset(w * 0.8f, h * 0.09f), o, back = SixC28.Back, ink = ArtInk.BrassLight)
        tagAt(a.tag("branchout", 0, 1), Offset(w * 0.14f, h * 0.9f), seg(br, 0.6f, 0.85f), back = SixC28.Back, ink = ArtInk.BrassLight)
        tagAt(a.tag("branchout", 0, 2), Offset(w * 0.86f, h * 0.9f), seg(br, 0.7f, 0.95f), back = SixC28.Back, ink = ArtInk.BrassLight)
    }
    // the rosette: the ring and the two triangles, drawn as each medallion arrives
    for (k in 0 until 6) {
        val f = if (k < 5) min(shown[k], shown[k + 1]) else min(shown[5], shown[0])
        val p = medal28(w, h, k)
        val q = medal28(w, h, (k + 1) % 6)
        if (f > 0f) drawLine(SixC28.Ink, p, p + (q - p) * f, 1.4f * dp, alpha = 0.7f)
        val g = min(shown[k], shown[(k + 2) % 6])
        val q2 = medal28(w, h, (k + 2) % 6)
        if (g > 0f) drawLine(SixC28.Gold, p, p + (q2 - p) * g, 1f * dp, alpha = 0.6f)
    }
    // the centre: an eight-pointed star that lights when all six are in
    val all = easeOut(seg(m3, 0.75f, 1f)).coerceAtLeast(if (br > 0f) 1f else 0f)
    if (all > 0f) {
        for (k in 0 until 2) withTransform({ rotate(45f * k + 10f * sin(t * 0.2f), c) }) {
            drawRect(SixC28.Gold, Offset(c.x - w * 0.035f, c.y - w * 0.035f), Size(w * 0.07f, w * 0.07f), alpha = 0.7f * all, style = Stroke(1.4f * dp))
        }
        softGlow(c, w * 0.08f, ArtInk.Gold, 0.35f * all)
    }
    // the six medallions
    for (k in 0 until 6) {
        val f = shown[k]
        if (f <= 0f) continue
        val p = medal28(w, h, k)
        drawCircle(SixC28.Well, r * f, p)
        drawCircle(SixC28.Rim, r * f, p, style = Stroke(2.4f * dp))
        drawCircle(SixC28.Rim, r * f * 0.86f, p, alpha = 0.5f, style = Stroke(0.8f * dp))
        val o = seg(f, 0.5f, 1f)
        when (k) {
            0 -> fitBig26(a.big("threeroots", 0, 0), p, r * 1.35f, ArtInk.Gold, o, glow = ArtInk.Gold)
            1 -> angelLight26(p, r * 0.22f, t, o)
            2 -> {
                drawRoundRect(Color(0xFF7A2E2E), Offset(p.x - r * 0.38f, p.y - r * 0.48f), Size(r * 0.76f, r * 0.96f), CornerRadius(r * 0.08f), alpha = o)
                drawRect(Color(0xFFF2E8D0), Offset(p.x + r * 0.3f, p.y - r * 0.42f), Size(r * 0.08f, r * 0.84f), alpha = o)
                drawLine(ArtInk.Gold, Offset(p.x - r * 0.22f, p.y - r * 0.2f), Offset(p.x + r * 0.18f, p.y - r * 0.2f), r * 0.06f, alpha = o)
                drawLine(ArtInk.Gold, Offset(p.x - r * 0.22f, p.y + r * 0.2f), Offset(p.x + r * 0.18f, p.y + r * 0.2f), r * 0.06f, alpha = o)
            }
            3 -> for (m in 0 until 3) {
                val lc = p + Offset(r * (-0.42f + 0.42f * m), r * (if (m == 1) -0.12f else 0.12f))
                softGlow(lc, r * 0.35f, Color(0xFFFFE6B0), 0.7f * o)
                drawCircle(Color(0xFFFFF4DA), r * 0.1f, lc, alpha = o)
            }
            4 -> balance28(p + Offset(0f, r * 0.05f), r * 1.2f, ArtInk.BrassLight, 0.3f * sin(t * 0.8f), o)
            else -> {
                drawRoundRect(Color(0xFFE8DCC0), Offset(p.x - r * 0.42f, p.y - r * 0.5f), Size(r * 0.84f, r), CornerRadius(r * 0.06f), alpha = o)
                for (m in 0 until 4) drawLine(Color(0xFF9A8A6A), Offset(p.x - r * 0.3f, p.y - r * 0.3f + m * r * 0.2f), Offset(p.x + r * 0.3f, p.y - r * 0.3f + m * r * 0.2f), r * 0.04f, alpha = o)
                drawLine(ArtInk.Gold, p + Offset(r * 0.5f, -r * 0.6f), p + Offset(r * 0.1f, r * 0.2f), r * 0.07f, StrokeCap.Round, alpha = o)
            }
        }
        // its name below it (the first is the Name itself)
        if (k > 0) {
            val tl = if (k < 3) a.tag("threeroots", 0, k) else a.tag("threemore", 0, k - 3)
            tagAt(tl, p + Offset(0f, r + h * 0.04f), seg(f, 0.6f, 1f), back = SixC28.Back, ink = ArtInk.Ivory)
        }
    }
}

// ═════════════ 2. three courts, one inside the other: five, six and one pillar (deen-levels) ═════════════

private object CourtC28 {
    val Ground = Color(0xFFE9DFC8)
    val Outer = Color(0xFFF2E6CC)
    val Middle = Color(0xFFD9ECE4)
    val Inner = Color(0xFFFFF4D6)
    val Wall = Color(0xFF9A7A4A)
    val Pillar = Color(0xFFF8F2E4)
    val PillarRim = Color(0xFFB09060)
    val Ink = Color(0xFF2A2430)
    val Idol = Color(0xFF6A6058)
    val Back = Color(0xE6201810)
}

private fun courtRect28(w: Float, h: Float, level: Int): Rect {
    val hw = w * (0.4f - 0.14f * level)
    val hh = h * (0.4f - 0.135f * level)
    val c = Offset(w * 0.5f, h * 0.52f)
    return Rect(c.x - hw, c.y - hh, c.x + hw, c.y + hh)
}

/** A court's wall as a rounded outline with its gate open at the bottom; [p] draws it. */
private fun DrawScope.courtWall28(r: Rect, gate: Float, p: Float, alpha: Float) {
    if (p <= 0f || alpha <= 0.01f) return
    val path = Path().apply {
        moveTo(r.center.x - gate, r.bottom)
        lineTo(r.left + r.width * 0.04f, r.bottom)
        quadraticTo(r.left, r.bottom, r.left, r.bottom - r.height * 0.04f)
        lineTo(r.left, r.top + r.height * 0.04f)
        quadraticTo(r.left, r.top, r.left + r.width * 0.04f, r.top)
        lineTo(r.right - r.width * 0.04f, r.top)
        quadraticTo(r.right, r.top, r.right, r.top + r.height * 0.04f)
        lineTo(r.right, r.bottom - r.height * 0.04f)
        quadraticTo(r.right, r.bottom, r.right - r.width * 0.04f, r.bottom)
        lineTo(r.center.x + gate, r.bottom)
    }
    clipRect(0f, r.bottom - (r.height + density * 4f) * p, size.width, size.height) {
        drawPath(path, CourtC28.Wall, alpha = alpha, style = Stroke(3.2f * density, cap = StrokeCap.Round))
    }
}

private fun DrawScope.pillar28(c: Offset, s: Float, lit: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawOval(Color.Black, Offset(c.x - s * 0.2f, c.y + s * 0.42f), Size(s * 0.75f, s * 0.22f), alpha = 0.18f * alpha)
    drawRect(CourtC28.Pillar, Offset(c.x - s * 0.22f, c.y - s * 0.5f), Size(s * 0.44f, s), alpha = alpha)
    drawRect(CourtC28.PillarRim, Offset(c.x - s * 0.22f, c.y - s * 0.5f), Size(s * 0.44f, s), alpha = alpha, style = Stroke(1.2f * density))
    drawRect(CourtC28.PillarRim, Offset(c.x - s * 0.3f, c.y - s * 0.58f), Size(s * 0.6f, s * 0.1f), alpha = alpha)
    if (lit > 0f) softGlow(c + Offset(0f, -s * 0.2f), s * 0.8f, ArtInk.Gold, 0.45f * lit * alpha)
}

/** Pillars on a ring around the courts' centre, [n] of them, skipping the gate at the bottom. */
private fun courtPillars28(w: Float, h: Float, level: Int, n: Int): List<Offset> {
    val c = Offset(w * 0.5f, h * 0.52f)
    if (n == 1) return listOf(c)
    val rx = w * (0.33f - 0.14f * level)
    val ry = h * (0.335f - 0.135f * level)
    val start = if (n == 5) -90f else -60f
    return List(n) { k ->
        val ang = (start + 360f * k / n) * PI.toFloat() / 180f
        Offset(c.x + cos(ang) * rx, c.y + sin(ang) * ry)
    }
}

/**
 * Three courts seen from above, one inside the other. Beats: enter (a man comes in at the outer gate, turns away from a
 * stone idol by the road and bows: «التَّوْحِيدُ», «الطَّاعَةُ», «الْبَرَاءَةُ»), court5 (the outer court's five pillars rise
 * and light: «5»), court6 (he passes the second gate; its six pillars: «6»), court1 (he reaches the inmost court with
 * its one pillar, in light: «1»).
 */
internal fun DrawScope.courtsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "enter" to Shot(Offset(w * 0.5f, h * 0.86f), 1.3f),
        "court5" to Shot(Offset(w * 0.5f, h * 0.52f), 1f),
        "court6" to Shot(Offset(w * 0.5f, h * 0.55f), 1.25f),
        "court1" to Shot(Offset(w * 0.5f, h * 0.55f), 1.6f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { courtsPlan(a) }
}

private fun DrawScope.courtsPlan(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val en = a.b("enter")
    val c5 = a.b("court5")
    val c6 = a.b("court6")
    val c1 = a.b("court1")
    drawRect(CourtC28.Ground)
    for (k in 0 until 14) drawLine(Color(0xFFD8CCB0), Offset(0f, h * k / 14f), Offset(w, h * k / 14f), 0.8f * dp, alpha = 0.5f)
    val outer = courtRect28(w, h, 0)
    val middle = courtRect28(w, h, 1)
    val inner = courtRect28(w, h, 2)
    // the floors
    val o0 = easeOut(seg(en, 0f, 0.3f)).coerceAtLeast(if (c5 > 0f) 1f else 0f)
    val faint = easeOut(seg(c5, 0f, 0.2f)).coerceAtLeast(if (c6 > 0f) 1f else 0f)
    drawRoundRect(CourtC28.Outer, outer.topLeft, outer.size, CornerRadius(w * 0.02f), alpha = o0)
    drawRoundRect(CourtC28.Middle, middle.topLeft, middle.size, CornerRadius(w * 0.02f), alpha = 0.35f * faint + 0.65f * easeOut(seg(c6, 0f, 0.3f)).coerceAtLeast(if (c1 > 0f) 1f else 0f))
    drawRoundRect(CourtC28.Inner, inner.topLeft, inner.size, CornerRadius(w * 0.02f), alpha = 0.35f * faint + 0.65f * easeOut(seg(c1, 0f, 0.3f)))
    courtWall28(outer, w * 0.05f, easeOut(seg(en, 0f, 0.35f)).coerceAtLeast(if (c5 > 0f) 1f else 0f), 1f)
    courtWall28(middle, w * 0.04f, 1f, 0.35f * faint + 0.65f * easeOut(seg(c6, 0f, 0.3f)).coerceAtLeast(if (c1 > 0f) 1f else 0f))
    courtWall28(inner, w * 0.03f, 1f, 0.35f * faint + 0.65f * easeOut(seg(c1, 0f, 0.3f)))
    // the stone idol by the road outside, which he turns from
    val idolA = easeOut(seg(en, 0.05f, 0.3f)) * (1f - easeOut(seg(c5, 0f, 0.3f)))
    val idol = Offset(w * 0.22f, h * 0.985f)
    if (idolA > 0f) {
        drawRect(CourtC28.Idol, Offset(idol.x - w * 0.025f, idol.y - h * 0.02f), Size(w * 0.05f, h * 0.02f), alpha = idolA)
        drawRoundRect(CourtC28.Idol, Offset(idol.x - w * 0.016f, idol.y - h * 0.075f), Size(w * 0.032f, h * 0.058f), CornerRadius(w * 0.01f), alpha = idolA)
        drawCircle(CourtC28.Idol, w * 0.012f, idol + Offset(0f, -h * 0.09f), alpha = idolA)
    }
    // the five, six and one pillars
    val p5 = courtPillars28(w, h, 0, 5)
    val p6 = courtPillars28(w, h, 1, 6)
    val p1 = courtPillars28(w, h, 2, 1)
    for ((k, p) in p5.withIndex()) {
        val f = easeOut(seg(c5, 0.1f + k * 0.1f, 0.35f + k * 0.1f)).coerceAtLeast(if (c6 > 0f) 1f else 0f)
        pillar28(p, w * 0.05f * f.coerceAtLeast(0.01f), seg(c5, 0.7f, 1f) * (1f - 0.5f * seg(c6, 0f, 0.3f)), f)
    }
    for ((k, p) in p6.withIndex()) {
        val f = easeOut(seg(c6, 0.15f + k * 0.08f, 0.4f + k * 0.08f)).coerceAtLeast(if (c1 > 0f) 1f else 0f)
        pillar28(p, w * 0.045f * f.coerceAtLeast(0.01f), seg(c6, 0.7f, 1f) * (1f - 0.5f * seg(c1, 0f, 0.3f)), f)
    }
    val one = easeOut(seg(c1, 0.15f, 0.5f))
    if (one > 0f) {
        lightShafts(p1[0] + Offset(0f, -h * 0.03f), -PI.toFloat() / 2f, 1.2f, h * 0.2f, Color(0xFFFFE6A0), 0.4f * seg(c1, 0.4f, 1f), t, rays = 7)
        pillar28(p1[0], w * 0.06f * one, seg(c1, 0.4f, 1f), one)
    }
    // the man: in at the outer gate, then the second, then the inmost
    val walk1 = easeInOut(seg(en, 0.15f, 0.5f))
    val walk2 = easeInOut(seg(c6, 0.1f, 0.6f))
    val walk3 = easeInOut(seg(c1, 0.05f, 0.5f))
    val pts = listOf(Offset(w * 0.5f, h * 1.02f), Offset(w * 0.5f, outer.bottom - h * 0.04f), Offset(w * 0.5f, middle.bottom - h * 0.035f), Offset(w * 0.5f, inner.bottom - h * 0.03f))
    val pos = when {
        c1 > 0f -> pts[2] + (pts[3] - pts[2]) * walk3
        c6 > 0f -> pts[1] + (pts[2] - pts[1]) * walk2
        else -> pts[0] + (pts[1] - pts[0]) * walk1
    }
    val walking = (walk1 in 0.01f..0.99f && c6 <= 0f) || (walk2 in 0.01f..0.99f && c1 <= 0f) || walk3 in 0.01f..0.99f
    val tall = h * 0.1f
    val man = easeOut(seg(en, 0.1f, 0.25f)).coerceAtLeast(if (c5 > 0f) 1f else 0f)
    // he bows once he is inside, as submission
    val bow = if (c5 <= 0f) easeOut(seg(en, 0.6f, 0.75f)) * (1f - easeOut(seg(en, 0.9f, 1f))) else 0f
    if (bow > 0.05f) w8Worshipper(pos, tall, CourtC28.Ink, 0f, bow, man)
    else figure(pos, tall, CourtC28.Ink, man, walk = if (walking) t * 5f else 0f)
    if (c1 > 0.5f) softGlow(pos + Offset(0f, -tall * 0.6f), w * 0.03f, Color(0xFFFFF4D0), 0.6f * seg(c1, 0.5f, 1f))
    // the three words of the definition while he enters
    val words = 1f - easeOut(seg(c5, 0f, 0.25f))
    tagAt(a.tag("enter", 0, 0), Offset(w * 0.5f, outer.bottom - h * 0.16f), seg(en, 0.3f, 0.45f) * words, back = CourtC28.Back, ink = ArtInk.BrassLight)
    tagAt(a.tag("enter", 0, 1), Offset(w * 0.7f, outer.bottom - h * 0.08f), seg(en, 0.55f, 0.7f) * words, back = CourtC28.Back, ink = ArtInk.BrassLight)
    tagAt(a.tag("enter", 0, 2), idol + Offset(w * 0.12f, -h * 0.1f), seg(en, 0.75f, 0.9f) * words, back = CourtC28.Back, ink = ArtInk.Ember)
    if (idolA > 0f) cross21(idol + Offset(0f, -h * 0.06f), w * 0.03f, Color(0xFFC0463A), seg(en, 0.78f, 0.95f), 2.5f * dp)
    // the numbers of the pillars
    tagAt(a.tag("court5"), Offset(outer.right - w * 0.06f, outer.top + h * 0.05f), seg(c5, 0.6f, 0.8f), back = CourtC28.Back, ink = ArtInk.Gold)
    tagAt(a.tag("court6"), Offset(middle.right - w * 0.05f, middle.top + h * 0.045f), seg(c6, 0.6f, 0.8f), back = CourtC28.Back, ink = ArtInk.Gold)
    tagAt(a.tag("court1"), Offset(inner.center.x, inner.top + h * 0.035f), seg(c1, 0.6f, 0.8f), back = CourtC28.Back, ink = ArtInk.Gold)
}

// ═════════════ 3. the verse on its page, kept from every hand (asma-sifat) ═════════════

private object GuardC28 {
    val Desk = Color(0xFF4A3424)
    val DeskLow = Color(0xFF2E2016)
    val Page = Color(0xFFF8EFD8)
    val PageEdge = Color(0xFFD8C49A)
    val Gold = Color(0xFFD4A33E)
    val Ink = Color(0xFF2A2430)
    val Red = Color(0xFFC0463A)
    val Back = Color(0xE6201810)
}

/**
 * A page on a dark desk. Beats: affirmverse (the verse is written across the page, line by line, ﴿لَيْسَ كَمِثْلِهِ
 * شَيْءٌ﴾ then ﴿وَهُوَ السَّمِيعُ الْبَصِيرُ﴾, and a gold border closes round it), nobend (from the left a red pen comes
 * to bend its lines, from the right an eraser to wipe them: both stop at the border and are struck out, «تَحْرِيفٌ»,
 * «تَعْطِيلٌ»), nohow (a dashed box with a question mark, asking how, and a man held up beside it as a likeness: both
 * struck out, «تَكْيِيفٌ», «تَمْثِيلٌ»).
 */
internal fun DrawScope.verseguardScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "affirmverse" to Shot(Offset(w * 0.5f, h * 0.42f), 1.12f),
        "nobend" to Shot(Offset(w * 0.5f, h * 0.4f), 1.04f),
        "nohow" to Shot(Offset(w * 0.5f, h * 0.62f), 1.04f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { verseguardDesk(a) }
}

private fun DrawScope.verseguardDesk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val av = a.b("affirmverse")
    val nb = a.b("nobend")
    val nh = a.b("nohow")
    drawRect(Brush.verticalGradient(listOf(GuardC28.Desk, GuardC28.DeskLow)))
    for (k in 0 until 7) drawLine(Color(0xFF5A4030), Offset(0f, h * (0.1f + 0.14f * k)), Offset(w, h * (0.12f + 0.14f * k)), 1f * dp, alpha = 0.35f)
    val page = Rect(w * 0.23f, h * 0.14f, w * 0.77f, h * 0.7f)
    softGlow(page.center, w * 0.4f, Color(0xFFFFE6B0), 0.25f)
    drawRect(Color.Black, page.topLeft + Offset(w * 0.008f, h * 0.012f), page.size, alpha = 0.25f)
    drawRect(GuardC28.Page, page.topLeft, page.size)
    drawRect(GuardC28.PageEdge, page.topLeft, page.size, style = Stroke(1.2f * dp))
    // the verse, written in from the right, line by line
    val l1 = seg(av, 0.05f, 0.4f)
    val l2 = seg(av, 0.35f, 0.7f)
    for ((k, f) in listOf(l1, l2).withIndex()) {
        if (f <= 0f) continue
        val cy = page.top + page.height * (0.32f + 0.3f * k)
        clipRect(page.right - page.width * f - 2f, page.top, page.right, page.bottom) {
            fitBig26(a.big("affirmverse", 0, k), Offset(page.center.x, cy), page.width * 0.84f, GuardC28.Ink, 1f)
        }
    }
    // the gold border that keeps it
    val border = easeOut(seg(av, 0.7f, 1f))
    if (border > 0f) {
        val g = Rect(page.left - w * 0.02f, page.top - h * 0.025f, page.right + w * 0.02f, page.bottom + h * 0.025f)
        val per = 2f * (g.width + g.height)
        val pe = PathEffect.dashPathEffect(floatArrayOf(per * border, per))
        drawRect(GuardC28.Gold, g.topLeft, g.size, style = Stroke(2.6f * dp, pathEffect = pe))
        softGlow(page.center, w * 0.32f, ArtInk.Gold, 0.18f * border * (0.8f + 0.2f * sin(t * 1.5f)))
    }
    // 1. a red pen that would bend the lines
    if (nb > 0f) {
        val come = easeOut(seg(nb, 0f, 0.35f))
        val tip = Offset(mix(-w * 0.05f, page.left - w * 0.045f, come), page.top + page.height * 0.35f)
        withTransform({ rotate(-30f, tip) }) {
            drawRoundRect(GuardC28.Red, Offset(tip.x - w * 0.12f, tip.y - h * 0.012f), Size(w * 0.11f, h * 0.024f), CornerRadius(h * 0.01f))
            drawPath(Path().apply { moveTo(tip.x - w * 0.012f, tip.y - h * 0.012f); lineTo(tip.x, tip.y); lineTo(tip.x - w * 0.012f, tip.y + h * 0.012f); close() }, Color(0xFF2A2430))
        }
        // its wavy stroke breaks off at the border
        val wave = seg(nb, 0.2f, 0.45f)
        if (wave > 0f) {
            val p = Path().apply {
                moveTo(tip.x, tip.y)
                for (m in 1..10) lineTo(tip.x + w * 0.004f * m * wave, tip.y + sin(m * 1.4f) * h * 0.01f)
            }
            drawPath(p, GuardC28.Red, alpha = 0.8f * (1f - seg(nb, 0.5f, 0.7f)), style = Stroke(1.8f * dp, cap = StrokeCap.Round))
        }
        struckTag21(a.tag("nobend", 0, 0), Offset(w * 0.12f, h * 0.2f), seg(nb, 0.2f, 0.4f), seg(nb, 0.45f, 0.7f), GuardC28.Red, back = GuardC28.Back, ink = ArtInk.Ivory)
        // an eraser that would wipe the words
        val come2 = easeOut(seg(nb, 0.45f, 0.75f))
        val er = Offset(mix(w * 1.05f, page.right + w * 0.06f, come2), page.top + page.height * 0.62f)
        drawRoundRect(Color(0xFFE8B0B8), Offset(er.x - w * 0.045f, er.y - h * 0.03f), Size(w * 0.09f, h * 0.06f), CornerRadius(w * 0.01f), alpha = come2)
        drawRect(Color(0xFF6A8AB0), Offset(er.x - w * 0.045f, er.y - h * 0.03f), Size(w * 0.03f, h * 0.06f), alpha = come2)
        if (come2 > 0.95f) for (m in 0 until 3) {
            val f = fract(t * 2f + m / 3f)
            drawCircle(Color(0xFFD8C8C0), w * 0.004f, er + Offset(-w * 0.05f - w * 0.02f * f, h * (-0.02f + 0.02f * m)), alpha = 0.6f * (1f - f))
        }
        struckTag21(a.tag("nobend", 0, 1), Offset(w * 0.88f, h * 0.2f), seg(nb, 0.6f, 0.75f), seg(nb, 0.78f, 0.98f), GuardC28.Red, back = GuardC28.Back, ink = ArtInk.Ivory)
    }
    // 2. asking how, and likening to a creature
    if (nh > 0f) {
        val q = easeOut(seg(nh, 0f, 0.3f))
        val box = Offset(w * 0.13f, h * 0.82f)
        drawRect(Color(0xFFE8DCC0), Offset(box.x - w * 0.06f, box.y - h * 0.07f), Size(w * 0.12f, h * 0.14f), alpha = q, style = Stroke(1.6f * dp, pathEffect = dash28()))
        qmark21(box, h * 0.08f, Color(0xFFE8DCC0), q)
        cross21(box, w * 0.05f, GuardC28.Red, seg(nh, 0.3f, 0.5f), 3f * dp)
        struckTag21(a.tag("nohow", 0, 0), Offset(w * 0.17f, h * 0.62f), seg(nh, 0.15f, 0.3f), seg(nh, 0.3f, 0.5f), GuardC28.Red, back = GuardC28.Back, ink = ArtInk.Ivory)
        val m = easeOut(seg(nh, 0.5f, 0.75f))
        val man = Offset(w * 0.88f, h * 0.93f)
        figure(man, h * 0.2f, Color(0xFFE8DCC0), m)
        // the sign of likeness between him and the page, broken
        val eq = Offset(w * 0.8f, h * 0.8f)
        for (dy in listOf(-1f, 1f)) drawLine(Color(0xFFE8DCC0), eq + Offset(-w * 0.02f, dy * h * 0.012f), eq + Offset(w * 0.02f, dy * h * 0.012f), 3f * dp, StrokeCap.Round, alpha = m)
        val br = seg(nh, 0.75f, 0.9f)
        if (br > 0f) drawLine(GuardC28.Red, eq + Offset(w * 0.015f, -h * 0.03f), eq + Offset(w * 0.015f - w * 0.03f * br, -h * 0.03f + h * 0.06f * br), 3f * dp, StrokeCap.Round)
        struckTag21(a.tag("nohow", 0, 1), Offset(w * 0.84f, h * 0.62f), seg(nh, 0.6f, 0.75f), seg(nh, 0.78f, 0.98f), GuardC28.Red, back = GuardC28.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 4. a jar of light: rising, falling, kept through sins, broken only by making them lawful (iman-increase) ═════════════

private object JarC28 {
    val Wall = Color(0xFF2A2026)
    val WallLow = Color(0xFF16101A)
    val Shelf = Color(0xFF5A3E2A)
    val Glass = Color(0xFFCFE2F0)
    val Light = Color(0xFFFFD27A)
    val LightDeep = Color(0xFFE8A040)
    val Dark = Color(0xFF3A2E3A)
    val Stone = Color(0xFF4A3E48)
    val Red = Color(0xFFC0463A)
    val Back = Color(0xE6201810)
}

/** A glass jar, its base at [base], [s] tall, filled with light to [level] (0..1), [dim] darkening it. */
private fun DrawScope.jar28(base: Offset, s: Float, level: Float, dim: Float, t: Float, alpha: Float, crack: Float = 0f) {
    if (alpha <= 0.01f) return
    val hw = s * 0.32f
    val body = Path().apply {
        moveTo(base.x - hw * 0.8f, base.y)
        quadraticTo(base.x - hw * 1.1f, base.y - s * 0.4f, base.x - hw * 0.9f, base.y - s * 0.72f)
        lineTo(base.x - hw * 0.45f, base.y - s * 0.86f)
        lineTo(base.x - hw * 0.45f, base.y - s)
        lineTo(base.x + hw * 0.45f, base.y - s)
        lineTo(base.x + hw * 0.45f, base.y - s * 0.86f)
        lineTo(base.x + hw * 0.9f, base.y - s * 0.72f)
        quadraticTo(base.x + hw * 1.1f, base.y - s * 0.4f, base.x + hw * 0.8f, base.y)
        close()
    }
    drawPath(body, JarC28.Glass, alpha = 0.12f * alpha)
    // the light inside, its surface gently moving
    val lv = level.coerceIn(0f, 1f)
    if (lv > 0.005f) {
        val top = base.y - s * 0.84f * lv
        clipPath(body) {
            val surf = Path().apply {
                moveTo(base.x - hw * 1.2f, base.y + 2f)
                lineTo(base.x - hw * 1.2f, top)
                for (m in 0..12) lineTo(base.x - hw * 1.2f + hw * 2.4f * m / 12f, top + sin(t * 2f + m * 0.9f) * s * 0.008f)
                lineTo(base.x + hw * 1.2f, base.y + 2f)
                close()
            }
            drawPath(surf, Brush.verticalGradient(listOf(lerpColor(JarC28.Light, JarC28.Dark, dim), lerpColor(JarC28.LightDeep, JarC28.Dark, dim * 0.9f)), startY = top, endY = base.y), alpha = alpha)
        }
        softGlow(Offset(base.x, (top + base.y) / 2f), s * 0.55f * (0.4f + 0.6f * lv), JarC28.Light, 0.45f * alpha * (1f - dim))
    }
    drawPath(body, JarC28.Glass, alpha = 0.75f * alpha, style = Stroke(2f * density))
    drawLine(Color.White, Offset(base.x - hw * 0.7f, base.y - s * 0.62f), Offset(base.x - hw * 0.75f, base.y - s * 0.25f), 2f * density, StrokeCap.Round, alpha = 0.45f * alpha)
    if (crack > 0f) {
        val c = Path().apply {
            moveTo(base.x + hw * 0.6f, base.y - s * 0.7f)
            lineTo(base.x + hw * 0.3f, base.y - s * 0.55f)
            lineTo(base.x + hw * 0.55f, base.y - s * 0.42f)
            lineTo(base.x + hw * 0.15f, base.y - s * 0.25f)
            lineTo(base.x + hw * 0.4f, base.y - s * 0.1f)
        }
        clipRect(0f, base.y - s * 0.72f, size.width, base.y - s * 0.72f + s * 0.64f * crack) {
            drawPath(c, Color.White, alpha = alpha, style = Stroke(1.8f * density, cap = StrokeCap.Round))
        }
    }
}

/**
 * A shelf in a dark room. Beats: fillup (two streams of light fall into a glass jar, «قَوْلٌ» and «عَمَلٌ»: it rises),
 * drain (dark drops: it sinks and dims), stillfaith (four dark stones drop in one by one, «الزِّنَا», «السَّرِقَةُ»,
 * «الرِّبَا», «الْعُقُوقُ»: it sinks lower, but its light stays), crack (a second jar beside it: «الِاسْتِحْلَالُ», it
 * cracks and its light runs out), mustard (close on the first jar: at its bottom one grain of light still shines).
 */
internal fun DrawScope.heartjarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fillup" to Shot(Offset(w * 0.56f, h * 0.45f), 1.15f),
        "sinkdrop" to Shot(Offset(w * 0.56f, h * 0.5f), 1.15f),
        "stillfaith" to Shot(Offset(w * 0.42f, h * 0.55f), 1.05f),
        "lawfulcrack" to Shot(Offset(w * 0.86f, h * 0.68f), 1.55f),
        "mustard" to Shot(Offset(w * 0.56f, h * 0.8f), 1.8f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { heartjarShelf(a) }
}

private fun DrawScope.heartjarShelf(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fu = a.b("fillup")
    val dr = a.b("sinkdrop")
    val sf = a.b("stillfaith")
    val ck = a.b("lawfulcrack")
    val mu = a.b("mustard")
    drawRect(Brush.verticalGradient(listOf(JarC28.Wall, JarC28.WallLow)))
    val shelfY = h * 0.88f
    drawRect(JarC28.Shelf, Offset(0f, shelfY), Size(w, h * 0.03f))
    drawRect(Color(0xFF3A281C), Offset(0f, shelfY + h * 0.03f), Size(w, h * 0.09f))
    val base = Offset(w * 0.56f, shelfY)
    val s = h * 0.56f
    // the level: up with obedience, down with sin, lower with the great sins, never empty
    val level = 0.3f + 0.45f * easeInOut(seg(fu, 0.1f, 0.9f)) - 0.2f * easeInOut(seg(dr, 0.1f, 0.9f)) - 0.32f * easeInOut(seg(sf, 0.15f, 0.85f))
    val dim = 0.25f * easeOut(seg(dr, 0.1f, 0.9f)) + 0.25f * easeOut(seg(sf, 0.15f, 0.85f)) - 0.3f * easeOut(seg(mu, 0f, 0.5f))
    jar28(base, s, level, dim.coerceIn(0f, 0.6f), t, 1f)
    // two streams of light: saying and doing
    if (fu > 0f) {
        val pour = seg(fu, 0.05f, 0.95f)
        for (k in 0 until 2) {
            val x = base.x + (if (k == 0) w * 0.04f else -w * 0.04f)
            for (m in 0 until 7) {
                val f = fract(t * 1.3f + m / 7f + k * 0.5f)
                drawCircle(JarC28.Light, w * 0.006f, Offset(x, h * 0.06f + (base.y - s * 0.6f - h * 0.06f) * f), alpha = 0.9f * bump(pour).coerceAtLeast(if (pour in 0.2f..0.8f) 1f else 0f))
            }
        }
        val tg = seg(fu, 0.2f, 0.4f) * (1f - seg(dr, 0f, 0.3f))
        tagAt(a.tag("fillup", 0, 0), Offset(base.x + w * 0.13f, h * 0.12f), tg, back = JarC28.Back, ink = ArtInk.BrassLight)
        tagAt(a.tag("fillup", 0, 1), Offset(base.x - w * 0.13f, h * 0.12f), tg, back = JarC28.Back, ink = ArtInk.BrassLight)
    }
    // dark drops
    if (dr > 0f && dr < 1f) for (m in 0 until 6) {
        val f = fract(t * 1.1f + m / 6f)
        drawCircle(JarC28.Dark, w * 0.007f, Offset(base.x + w * (-0.03f + 0.012f * m), h * 0.08f + (base.y - s * 0.55f - h * 0.08f) * f), alpha = 0.9f * bump(seg(dr, 0f, 1f)))
    }
    // four great sins drop in, one by one; the light stays
    if (sf > 0f) {
        for (k in 0 until 4) {
            val appear = easeOut(seg(sf, 0.1f + k * 0.16f, 0.2f + k * 0.16f))
            val fall = easeInOut(seg(sf, 0.18f + k * 0.16f, 0.32f + k * 0.16f))
            val tagP = Offset(w * 0.18f, h * (0.22f + 0.11f * k))
            tagAt(a.tag("stillfaith", 0, k), tagP, appear * (1f - seg(ck, 0f, 0.3f)), back = JarC28.Back, ink = ArtInk.Ivory)
            if (appear > 0f && fall < 1f) {
                val from = tagP + Offset(w * 0.1f, 0f)
                val to = base + Offset(w * (-0.02f + 0.013f * k), -s * 0.3f)
                val p = from + (to - from) * fall
                drawOval(JarC28.Stone, Offset(p.x - w * 0.012f, p.y - h * 0.01f), Size(w * 0.024f, h * 0.02f), alpha = appear)
            }
        }
        // the light still shines through: a small check by the jar
        check21(base + Offset(w * 0.16f, -s * 0.35f), h * 0.03f, Color(0xFF7ED6A0), seg(sf, 0.82f, 1f) * (1f - seg(ck, 0f, 0.3f)), 3f * dp)
    }
    // the second jar: making it lawful breaks it
    val j2 = easeOut(seg(ck, 0f, 0.25f))
    if (j2 > 0f) {
        val b2 = Offset(w * 0.86f, shelfY)
        val crack = easeOut(seg(ck, 0.25f, 0.55f))
        val run = easeInOut(seg(ck, 0.45f, 1f))
        jar28(b2, h * 0.3f, 0.45f * (1f - run), 0.1f + 0.5f * run, t, j2, crack)
        if (run > 0f) {
            drawOval(JarC28.Light, Offset(b2.x - w * 0.02f, b2.y - h * 0.008f), Size(w * 0.08f * run, h * 0.016f), alpha = 0.5f * (1f - seg(ck, 0.8f, 1f)))
            for (m in 0 until 4) {
                val f = fract(t * 1.5f + m / 4f)
                drawCircle(JarC28.Light, w * 0.004f, b2 + Offset(w * 0.03f + w * 0.02f * f, -h * 0.12f + h * 0.11f * f), alpha = 0.7f * (1f - f) * (1f - run))
            }
        }
        tagAt(a.tag("lawfulcrack"), b2 + Offset(-w * 0.02f, -h * 0.4f), seg(ck, 0.15f, 0.35f) * (1f - seg(mu, 0f, 0.3f)), back = JarC28.Back, ink = ArtInk.Ember)
    }
    // the grain of mustard that still shines
    if (mu > 0f) {
        val seed = base + Offset(0f, -h * 0.03f)
        val o = easeOut(seg(mu, 0.15f, 0.5f))
        softGlow(seed, w * (0.04f + 0.01f * sin(t * 3f)), Color(0xFFFFF0B0), 0.9f * o)
        drawCircle(Color(0xFFFFF8E0), w * 0.007f, seed, alpha = o)
        glint(seed, w * 0.025f, Color.White, o * (0.6f + 0.4f * sin(t * 2.4f)))
        tagAt(a.tag("mustard"), seed + Offset(0f, -h * 0.12f), seg(mu, 0.4f, 0.65f), back = JarC28.Back, ink = ArtInk.BrassLight)
    }
}

// ═════════════ 5. a seed: known, written, willed, created (qadar) ═════════════

private object DecC28 {
    val SkyTop = Color(0xFF3A4A7A)
    val SkyLow = Color(0xFFF2C89A)
    val Grass = Color(0xFF7AA04A)
    val Soil = Color(0xFF7A5A3A)
    val SoilDeep = Color(0xFF4A3424)
    val Seed = Color(0xFFC89A5A)
    val Tablet = Color(0xFFF4ECD6)
    val TabletEdge = Color(0xFFC8A45A)
    val Rule = Color(0xFFB8A27A)
    val Known = Color(0xFFF2C45A)
    val Trunk = Color(0xFF6A4A2E)
    val Leaf = Color(0xFF4A7A3A)
    val Date = Color(0xFFB8542E)
    val Ink = Color(0xFF3A2A1A)
    val Back = Color(0xE6201810)
}

private fun DrawScope.bird28(c: Offset, s: Float, color: Color, flap: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val up = s * (0.3f + 0.25f * sin(flap))
    drawPath(Path().apply {
        moveTo(c.x - s, c.y - up); quadraticTo(c.x - s * 0.4f, c.y - up * 0.2f, c.x, c.y)
        quadraticTo(c.x + s * 0.4f, c.y - up * 0.2f, c.x + s, c.y - up)
    }, color, alpha = alpha, style = Stroke(s * 0.18f, cap = StrokeCap.Round))
}

/** The outline of a grown palm: its trunk and fronds, [tall] high from [base]. */
private fun palmOutline28(base: Offset, tall: Float, w: Float): Path = Path().apply {
    val top = base + Offset(w * 0.015f, -tall)
    moveTo(base.x, base.y)
    quadraticTo(base.x + w * 0.025f, base.y - tall * 0.5f, top.x, top.y)
    for (k in 0 until 6) {
        val ang = (-170f + 32f * k) * PI.toFloat() / 180f
        val tip = top + Offset(cos(ang) * tall * 0.38f, sin(ang) * tall * 0.2f + tall * 0.12f)
        moveTo(top.x, top.y)
        quadraticTo(top.x + cos(ang) * tall * 0.2f, top.y + sin(ang) * tall * 0.2f - tall * 0.05f, tip.x, tip.y)
    }
}

/**
 * A field at dawn, cut away to show the soil. Beats: known (a seed lies in the soil; over it, in fine gold dots, the
 * palm it will become, the birds that will come to it: all that will be is known), written (high above, a tablet
 * of light fills with ruled lines; under it the hadith «كَتَبَ اللَّهُ مَقَادِيرَ الْخَلَائِقِ …»), willed (the seed puts out a
 * root and a shoot; a second seed beside it stays as it is, the outline over it fading), created (the palm grows into
 * its outline, its dates hang, birds come, the field greens: all of it created).
 */
internal fun DrawScope.decreeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "allknown" to Shot(Offset(w * 0.64f, h * 0.6f), 1.2f),
        "written" to Shot(Offset(w * 0.22f, h * 0.3f), 1.35f),
        "willed" to Shot(Offset(w * 0.72f, h * 0.72f), 1.45f),
        "allcreated" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { decreeField(a) }
}

private fun DrawScope.decreeField(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val kn = a.b("allknown")
    val wr = a.b("written")
    val wl = a.b("willed")
    val cr = a.b("allcreated")
    val ground = h * 0.63f
    val dawn = easeOut(seg(kn, 0f, 0.6f)) * 0.5f + 0.5f * easeOut(seg(cr, 0f, 0.6f))
    drawRect(Brush.verticalGradient(listOf(lerpColor(Color(0xFF1E2848), DecC28.SkyTop, dawn), lerpColor(Color(0xFF6A5A7A), DecC28.SkyLow, dawn)), endY = ground), size = Size(w, ground))
    softGlow(Offset(w * 0.9f, ground), w * 0.3f, Color(0xFFFFD8A0), 0.35f * dawn)
    // the soil, cut away
    drawRect(Brush.verticalGradient(listOf(DecC28.Soil, DecC28.SoilDeep), startY = ground, endY = h), Offset(0f, ground), Size(w, h - ground))
    val green = easeOut(seg(cr, 0.3f, 1f))
    drawRect(lerpColor(Color(0xFF8A7A4A), DecC28.Grass, green), Offset(0f, ground - h * 0.012f), Size(w, h * 0.022f))
    for (k in 0 until 9) drawCircle(Color(0xFF5A4030), w * 0.006f, Offset(w * (0.05f + 0.11f * k), h * (0.75f + 0.08f * ((k * 7) % 3))), alpha = 0.6f)
    val s1 = Offset(w * 0.62f, h * 0.74f)
    val s2 = Offset(w * 0.84f, h * 0.76f)
    val base1 = Offset(s1.x, ground)
    val tallPalm = h * 0.42f
    // 1. known: the seed, and in gold dots all that it will be
    val seeds = easeOut(seg(kn, 0f, 0.25f)).coerceAtLeast(if (wr > 0f || wl > 0f || cr > 0f) 1f else 0f)
    for (s in listOf(s1, s2)) drawOval(DecC28.Seed, Offset(s.x - w * 0.014f, s.y - h * 0.01f), Size(w * 0.028f, h * 0.02f), alpha = seeds)
    val known = easeOut(seg(kn, 0.2f, 0.7f)) * (1f - easeOut(seg(cr, 0.4f, 0.9f)))
    if (known > 0f) {
        drawPath(palmOutline28(base1, tallPalm, w), DecC28.Known, alpha = 0.8f * known, style = Stroke(1.5f * dp, cap = StrokeCap.Round, pathEffect = dash28(2f, 4f)))
        for (k in 0 until 2) bird28(Offset(w * (0.75f + 0.08f * k), h * (0.2f + 0.04f * k)), w * 0.022f, DecC28.Known, 0.5f, 0.7f * known)
        softGlow(base1 + Offset(0f, -tallPalm * 0.6f), w * 0.15f, DecC28.Known, 0.15f * known)
    }
    // the outline over the second seed: what is not willed does not come to be
    val other = easeOut(seg(kn, 0.3f, 0.7f)) * (1f - easeOut(seg(wl, 0.45f, 0.9f)))
    if (other > 0f) drawPath(palmOutline28(Offset(s2.x, ground), tallPalm * 0.6f, w), DecC28.Known, alpha = 0.45f * other, style = Stroke(1.2f * dp, cap = StrokeCap.Round, pathEffect = dash28(2f, 4f)))
    // 2. written: a tablet of light, high above
    val tab = easeOut(seg(wr, 0f, 0.3f)).coerceAtLeast(if (wl > 0f || cr > 0f) 1f else 0f) * (1f - 0.5f * easeOut(seg(cr, 0f, 0.4f)))
    if (tab > 0f) {
        val r = Rect(w * 0.07f, h * 0.1f, w * 0.35f, h * 0.36f)
        softGlow(r.center, w * 0.2f, Color(0xFFFFF0C8), 0.45f * tab)
        drawRoundRect(DecC28.Tablet, r.topLeft, r.size, CornerRadius(w * 0.012f), alpha = tab)
        drawRoundRect(DecC28.TabletEdge, r.topLeft, r.size, CornerRadius(w * 0.012f), alpha = tab, style = Stroke(2f * dp))
        val lines = seg(wr, 0.15f, 0.9f).coerceAtLeast(if (wl > 0f || cr > 0f) 1f else 0f)
        for (k in 0 until 7) {
            val f = seg(lines, k / 7f, (k + 1) / 7f)
            if (f <= 0f) continue
            val y = r.top + r.height * (0.15f + 0.11f * k)
            val x0 = r.right - r.width * 0.1f
            drawLine(DecC28.Rule, Offset(x0, y), Offset(x0 - r.width * 0.8f * f, y), 1.6f * dp, StrokeCap.Round, alpha = tab)
        }
        val hd = seg(wr, 0.35f, 0.7f) * (1f - seg(wl, 0f, 0.3f))
        fitBig26(a.big("written", 0, 0), Offset(r.center.x, r.bottom + h * 0.07f), w * 0.4f, Color(0xFFFFF4DA), hd)
        fitBig26(a.big("written", 0, 1), Offset(r.center.x, r.bottom + h * 0.14f), w * 0.46f, Color(0xFFFFF4DA), hd)
    }
    // 3. willed: the first seed puts out root and shoot; the second stays still
    val sprout = easeOut(seg(wl, 0.1f, 0.7f))
    if (sprout > 0f && cr <= 0f) {
        drawLine(Color(0xFFE8D8B0), s1, s1 + Offset(-w * 0.01f, h * 0.06f * sprout), 1.6f * dp, StrokeCap.Round)
        drawLine(Color(0xFFE8D8B0), s1, s1 + Offset(w * 0.012f, h * 0.05f * sprout), 1.2f * dp, StrokeCap.Round)
        val tip = s1 + Offset(0f, -(s1.y - ground + h * 0.07f) * sprout)
        drawLine(Color(0xFF6AA04A), s1, tip, 2.2f * dp, StrokeCap.Round)
        if (sprout > 0.7f) {
            val lf = seg(sprout, 0.7f, 1f)
            drawOval(Color(0xFF6AA04A), Offset(tip.x - w * 0.022f * lf, tip.y - h * 0.008f), Size(w * 0.022f * lf, h * 0.014f))
            drawOval(Color(0xFF6AA04A), Offset(tip.x, tip.y - h * 0.012f), Size(w * 0.022f * lf, h * 0.014f))
        }
        softGlow(tip, w * 0.03f, Color(0xFFCFF0A0), 0.4f * sprout)
    }
    // 4. created: the palm grows into what was known, and the world fills
    if (cr > 0f) {
        val grow = easeOut(seg(cr, 0f, 0.7f))
        w8Palm(base1, tallPalm * (0.15f + 0.85f * grow), DecC28.Trunk, DecC28.Leaf, t)
        val dates = seg(cr, 0.55f, 0.85f)
        if (dates > 0f) for (k in 0 until 7) {
            val c = base1 + Offset(w * (0.006f + 0.01f * (k % 3) - 0.01f), -tallPalm * 0.93f + h * 0.012f * (k / 3))
            drawCircle(DecC28.Date, w * 0.006f * dates, c)
        }
        for (k in 0 until 3) {
            val come = easeOut(seg(cr, 0.4f + k * 0.12f, 0.8f + k * 0.12f))
            bird28(Offset(mix(w * 1.05f, w * (0.7f + 0.09f * k), come), h * (0.16f + 0.05f * k) + sin(t * 2f + k) * h * 0.01f), w * 0.02f, Color(0xFF2A2430), t * 8f + k, come)
        }
        for (k in 0 until 14) {
            val x = w * (0.04f + 0.07f * k)
            val f = seg(green, k / 20f, k / 20f + 0.4f)
            drawLine(DecC28.Grass, Offset(x, ground), Offset(x - w * 0.006f, ground - h * 0.03f * f), 1.6f * dp, StrokeCap.Round)
            drawLine(DecC28.Grass, Offset(x, ground), Offset(x + w * 0.008f, ground - h * 0.025f * f), 1.6f * dp, StrokeCap.Round)
        }
    }
}

// ═════════════ 6. heart, tongue and limbs; and the two who keep only one (iman-musamma) ═════════════

private object ManC28 {
    val Wall = Color(0xFFF0E4CC)
    val WallLow = Color(0xFFE0CDA8)
    val Floor = Color(0xFFC8B08A)
    val Ink = Color(0xFF2A2430)
    val Heart = Color(0xFFFFC86A)
    val Voice = Color(0xFFFFE6A8)
    val Deed = Color(0xFF9AD8B0)
    val Off = Color(0xFF8A8094)
    val Red = Color(0xFFC0463A)
    val Back = Color(0xE6201810)
}

/** A faceless man with the three seats of faith lit: heart [hl], tongue [tl], limbs [ll]. */
private fun DrawScope.faithMan28(foot: Offset, tall: Float, hl: Float, tl: Float, ll: Float, t: Float, alpha: Float, dark: Float = 0f) {
    if (alpha <= 0.01f) return
    figure(foot, tall, lerpColor(ManC28.Ink, ManC28.Off, dark), alpha)
    val chest = foot + Offset(tall * 0.02f, -tall * 0.64f)
    val head = foot + Offset(0f, -tall * 0.91f)
    if (hl > 0f) {
        softGlow(chest, tall * 0.16f, ManC28.Heart, 0.8f * hl * alpha)
        drawCircle(ManC28.Heart, tall * 0.03f, chest, alpha = hl * alpha)
    }
    if (tl > 0f) for (k in 0 until 3) {
        val f = fract(t * 0.8f + k / 3f)
        val r = tall * (0.06f + 0.1f * f)
        drawArc(ManC28.Voice, 140f, 80f, false, Offset(head.x - tall * 0.08f - r, head.y + tall * 0.02f - r), Size(r * 2f, r * 2f), alpha = tl * alpha * (1f - f), style = Stroke(tall * 0.012f, cap = StrokeCap.Round))
    }
    if (ll > 0f) {
        val shoulder = foot + Offset(0f, -tall * 0.76f)
        for (p in listOf(shoulder + Offset(-tall * 0.11f, tall * 0.28f), shoulder + Offset(tall * 0.11f, tall * 0.28f), foot + Offset(-tall * 0.075f, 0f), foot + Offset(tall * 0.075f, 0f))) {
            softGlow(p, tall * 0.07f, ManC28.Deed, 0.75f * ll * alpha)
            drawCircle(ManC28.Deed, tall * 0.014f, p, alpha = ll * alpha)
        }
    }
}

/**
 * A plain room by day. Beats: threeparts (a man: his heart lights, «الْقَلْبُ»; words go out from his tongue, «اللِّسَانُ»;
 * his hands and feet glow as they act, «الْجَوَارِحُ»; then all three brighten and dim, rising and falling), nodeeds (a
 * book, «الْعَقِيدَةُ الطَّحَاوِيَّةُ»: as its definition is read his limbs go dark; a second book, «تَعْلِيقُ ابْنِ بَازٍ», marks
 * the gap in red), tongueonly (a second man, only his tongue lit: struck out, «اللِّسَانُ وَحْدَهُ»), heartonly (a third,
 * only a faint knowing in his heart: struck out, «الْمَعْرِفَةُ وَحْدَهَا»).
 */
internal fun DrawScope.wholemanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threeparts" to Shot(Offset(w * 0.3f, h * 0.5f), 1.2f),
        "nodeeds" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "tongueonly" to Shot(Offset(w * 0.66f, h * 0.62f), 1.3f),
        "heartonly" to Shot(Offset(w * 0.82f, h * 0.62f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { wholemanRoom(a) }
}

private fun DrawScope.wholemanRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tp = a.b("threeparts")
    val nd = a.b("nodeeds")
    val to = a.b("tongueonly")
    val ho = a.b("heartonly")
    val floor = h * 0.9f
    drawRect(Brush.verticalGradient(listOf(ManC28.Wall, ManC28.WallLow), endY = floor), size = Size(w, floor))
    drawRect(ManC28.Floor, Offset(0f, floor), Size(w, h - floor))
    softGlow(Offset(w * 0.3f, h * 0.2f), w * 0.4f, Color(0xFFFFF4DA), 0.4f)
    // the whole man
    val foot = Offset(w * 0.3f, floor)
    val tall = h * 0.68f
    val pulse = if (tp > 0.8f && nd <= 0f) 0.8f + 0.2f * sin(t * 2.2f) else 1f
    val hl = easeOut(seg(tp, 0.05f, 0.3f)) * pulse
    val tl = easeOut(seg(tp, 0.3f, 0.5f)) * pulse
    val limbsOff = easeOut(seg(nd, 0.15f, 0.45f))
    val ll = easeOut(seg(tp, 0.5f, 0.7f)) * pulse * (1f - limbsOff)
    val mainA = easeOut(seg(tp, 0f, 0.15f)) * (1f - 0.6f * easeOut(seg(to, 0f, 0.3f)))
    faithMan28(foot, tall, hl, tl, ll, t, mainA)
    val parts = 1f - easeOut(seg(nd, 0f, 0.2f))
    tagAt(a.tag("threeparts", 0, 0), foot + Offset(w * 0.17f, -tall * 0.64f), seg(tp, 0.15f, 0.3f) * parts, back = ManC28.Back, ink = ArtInk.BrassLight)
    tagAt(a.tag("threeparts", 0, 1), foot + Offset(-w * 0.19f, -tall * 0.96f), seg(tp, 0.35f, 0.5f) * parts, back = ManC28.Back, ink = ArtInk.BrassLight)
    tagAt(a.tag("threeparts", 0, 2), foot + Offset(w * 0.17f, -tall * 0.3f), seg(tp, 0.55f, 0.7f) * parts, back = ManC28.Back, ink = ArtInk.BrassLight)
    // the definition in the book, and the comment on it
    val books = easeOut(seg(nd, 0f, 0.2f)) * (1f - easeOut(seg(to, 0f, 0.3f)))
    if (books > 0f) {
        val b1 = Offset(w * 0.7f, h * 0.17f)
        flatBook28(b1, w * 0.12f, Color(0xFF2E4A6A), books)
        tagAt(a.tag("nodeeds", 0, 0), b1 + Offset(0f, h * 0.07f), books, back = ManC28.Back, ink = ArtInk.Ivory)
        val b2a = easeOut(seg(nd, 0.5f, 0.7f)) * books
        val b2 = Offset(w * 0.7f, h * 0.4f)
        flatBook28(b2, w * 0.12f, Color(0xFF6A2E2E), b2a)
        tagAt(a.tag("nodeeds", 0, 1), b2 + Offset(0f, h * 0.07f), b2a, back = ManC28.Back, ink = ArtInk.Ivory)
        // the gap it leaves, marked in red round his hands
        val mark = easeOut(seg(nd, 0.7f, 0.9f)) * books
        if (mark > 0f) {
            val hands = foot + Offset(0f, -tall * 0.48f)
            drawOval(ManC28.Red, Offset(hands.x - tall * 0.2f, hands.y - tall * 0.08f), Size(tall * 0.4f, tall * 0.16f), alpha = mark, style = Stroke(2f * dp, pathEffect = dash28()))
            drawOval(ManC28.Red, Offset(foot.x - tall * 0.16f, foot.y - tall * 0.06f), Size(tall * 0.32f, tall * 0.1f), alpha = mark, style = Stroke(2f * dp, pathEffect = dash28()))
            drawLine(ManC28.Red, b2 + Offset(-w * 0.08f, -h * 0.02f), hands + Offset(tall * 0.22f, 0f), 1.4f * dp, alpha = 0.7f * mark, pathEffect = dash28(3f, 3f))
        }
    }
    // the man who has only his tongue
    val ta = easeOut(seg(to, 0.05f, 0.3f)).coerceAtLeast(if (ho > 0f) 1f else 0f)
    if (ta > 0f) {
        val f2 = Offset(w * 0.62f, floor)
        faithMan28(f2, h * 0.42f, 0f, ta, 0f, t, ta, dark = 0.6f)
        drawCircle(ManC28.Off, h * 0.42f * 0.03f, f2 + Offset(h * 0.42f * 0.02f, -h * 0.42f * 0.64f), alpha = ta, style = Stroke(1.2f * dp))
        struckTag21(a.tag("tongueonly"), Offset(w * 0.6f, h * 0.38f), seg(to, 0.2f, 0.4f), seg(to, 0.5f, 0.85f), ManC28.Red, back = ManC28.Back, ink = ArtInk.Ivory)
    }
    // the man who has only a knowing in his heart
    val ka = easeOut(seg(ho, 0.05f, 0.3f))
    if (ka > 0f) {
        val f3 = Offset(w * 0.86f, floor)
        faithMan28(f3, h * 0.42f, 0.35f * ka, 0f, 0f, t, ka, dark = 0.6f)
        struckTag21(a.tag("heartonly"), Offset(w * 0.85f, h * 0.46f), seg(ho, 0.2f, 0.4f), seg(ho, 0.5f, 0.85f), ManC28.Red, back = ManC28.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 7. five lights: a sun, a star, a torch, a lamp and a weak lamp (iman-tafadul) ═════════════

private object LitC28 {
    val Top = Color(0xFF0E1430)
    val Low = Color(0xFF2A2A52)
    val Ground = Color(0xFF14122A)
    val Ink = Color(0xFF080A16)
    val Warm = Color(0xFFFFE0A0)
    val Flame = Color(0xFFFFB04A)
    val Lamp = Color(0xFFB8864A)
    val Cloud = Color(0xFF2E2440)
    val CloudRim = Color(0xFF6A5A80)
    val Back = Color(0xE60E1018)
}

private val litX28 = listOf(0.84f, 0.67f, 0.5f, 0.33f, 0.16f)
private val litS28 = listOf(1f, 0.78f, 0.6f, 0.42f, 0.22f)

/** The light at a man's heart, of kind [k] (0 sun … 4 weak lamp), [f] formed, at strength [s]. */
private fun DrawScope.heartLight28(c: Offset, k: Int, f: Float, s: Float, t: Float, w: Float) {
    if (s <= 0.01f) return
    softGlow(c, w * (0.03f + 0.11f * s), LitC28.Warm, 0.75f * min(1f, 0.4f + s))
    if (f <= 0.01f) {
        drawCircle(LitC28.Warm, w * (0.005f + 0.008f * s), c)
        return
    }
    when (k) {
        0 -> {
            for (m in 0 until 10) {
                val ang = t * 0.3f + m * PI.toFloat() / 5f
                drawLine(LitC28.Warm, c + Offset(cos(ang), sin(ang)) * w * 0.032f, c + Offset(cos(ang), sin(ang)) * w * (0.032f + 0.018f * f), 1.6f * density, StrokeCap.Round, alpha = f)
            }
            drawCircle(Color(0xFFFFF0C0), w * 0.024f * f, c)
        }
        1 -> {
            glint(c, w * 0.045f * f, Color.White, f)
            drawCircle(Color(0xFFFFF8E8), w * 0.008f, c, alpha = f)
        }
        2 -> {
            drawLine(LitC28.Lamp, c + Offset(0f, w * 0.005f), c + Offset(-w * 0.004f, w * 0.05f), 2.4f * density, StrokeCap.Round, alpha = f)
            val fl = w * (0.026f + 0.004f * sin(t * 9f))
            drawPath(Path().apply { moveTo(c.x - fl * 0.5f, c.y); quadraticTo(c.x, c.y - fl * 2.2f * f, c.x + fl * 0.5f, c.y); close() }, LitC28.Flame, alpha = f)
        }
        else -> {
            // an oil lamp; the fifth one's flame small and wavering
            val weak = k == 4
            drawPath(Path().apply { moveTo(c.x - w * 0.022f, c.y + w * 0.01f); quadraticTo(c.x, c.y + w * 0.03f, c.x + w * 0.026f, c.y + w * 0.004f); lineTo(c.x - w * 0.022f, c.y + w * 0.01f); close() }, LitC28.Lamp, alpha = f)
            val fl = w * (if (weak) 0.008f + 0.004f * sin(t * 13f) else 0.016f + 0.002f * sin(t * 9f))
            val tip = c + Offset(w * 0.02f, w * 0.002f)
            drawPath(Path().apply { moveTo(tip.x - fl * 0.5f, tip.y); quadraticTo(tip.x, tip.y - fl * 2.2f * f, tip.x + fl * 0.5f, tip.y); close() }, LitC28.Flame, alpha = f * (if (weak) 0.7f + 0.3f * sin(t * 7f) else 1f))
        }
    }
}

/**
 * Night, five men in a row. Beats: samelight (each has the same small light at his heart; a book above,
 * «الْعَقِيدَةُ الطَّحَاوِيَّةُ»), unequal (a second book, «تَعْلِيقُ ابْنِ بَازٍ»: the lights draw apart, strong to weak),
 * brightthree (the first is a sun, the second a shining star, the third a great torch), dimtwo (the fourth a bright lamp,
 * the fifth a weak one), burnaway (dark clouds drift down, «الشُّبُهَاتُ», «الشَّهَوَاتُ»: the strongest lights burn them
 * far off, the weakest only close by).
 */
internal fun DrawScope.heartlightsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "samelight" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "unequal" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "brightthree" to Shot(Offset(w * 0.67f, h * 0.62f), 1.35f),
        "dimtwo" to Shot(Offset(w * 0.25f, h * 0.62f), 1.4f),
        "burnaway" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { heartlightsNight(a) }
}

private fun DrawScope.heartlightsNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val sl = a.b("samelight")
    val un = a.b("unequal")
    val b3 = a.b("brightthree")
    val d2 = a.b("dimtwo")
    val bu = a.b("burnaway")
    val ground = h * 0.86f
    deepSky(t, ground, LitC28.Top, LitC28.Low, starAlpha = 0.6f)
    drawRect(LitC28.Ground, Offset(0f, ground), Size(w, h - ground))
    // the two books
    val bk = 1f - easeOut(seg(bu, 0f, 0.3f))
    val b1 = easeOut(seg(sl, 0.2f, 0.5f)) * bk
    flatBook28(Offset(w * 0.8f, h * 0.1f), w * 0.1f, Color(0xFF2E4A6A), b1)
    tagAt(a.tag("samelight"), Offset(w * 0.8f, h * 0.18f), b1, back = LitC28.Back, ink = ArtInk.Ivory)
    val b2 = easeOut(seg(un, 0.1f, 0.4f)) * bk
    flatBook28(Offset(w * 0.2f, h * 0.1f), w * 0.1f, Color(0xFF6A2E2E), b2)
    tagAt(a.tag("unequal"), Offset(w * 0.2f, h * 0.18f), b2, back = LitC28.Back, ink = ArtInk.Ivory)
    // the men and their lights
    val tall = h * 0.32f
    val men = easeOut(seg(sl, 0f, 0.3f))
    val apart = easeInOut(seg(un, 0.3f, 0.9f))
    val formed = listOf(seg(b3, 0f, 0.3f), seg(b3, 0.3f, 0.6f), seg(b3, 0.6f, 0.9f), seg(d2, 0f, 0.4f), seg(d2, 0.5f, 0.9f)).map { easeOut(it) }
    for (k in 0 until 5) {
        val foot = Offset(w * litX28[k], ground)
        figure(foot, tall, LitC28.Ink, men)
        val s = mix(0.45f, litS28[k], apart) * men
        heartLight28(foot + Offset(tall * 0.02f, -tall * 0.64f), k, formed[k], s, t, w)
    }
    // the clouds of doubts and desires, burnt away at the distance each light can reach
    if (bu > 0f) {
        for (k in 0 until 5) {
            val chest = Offset(w * litX28[k], ground - tall * 0.64f)
            val stop = chest.y - h * (0.06f + 0.3f * litS28[k])
            val start = h * 0.04f
            val come = easeInOut(seg(bu, 0.05f + 0.04f * k, 0.6f + 0.04f * k))
            val y = mix(start, stop, come)
            val burn = seg(bu, 0.55f + 0.04f * k, 0.85f + 0.04f * k)
            val cs = w * 0.035f * (1f - 0.85f * burn)
            val c = Offset(chest.x + sin(t * 0.7f + k) * w * 0.01f, y)
            for (m in 0 until 3) {
                val p = c + Offset(w * (-0.025f + 0.025f * m), if (m == 1) -cs * 0.4f else 0f)
                drawCircle(LitC28.Cloud, cs * (if (m == 1) 1.1f else 0.85f), p, alpha = 0.95f * (1f - burn * 0.9f))
                drawCircle(LitC28.CloudRim, cs * (if (m == 1) 1.1f else 0.85f), p, alpha = 0.5f * (1f - burn), style = Stroke(1f * density))
            }
            if (burn in 0.01f..0.99f) for (m in 0 until 5) {
                val f = fract(t * 1.4f + m / 5f)
                drawCircle(LitC28.Flame, w * 0.004f, c + Offset(w * (-0.03f + 0.015f * m), cs * 0.6f + f * h * 0.03f), alpha = 0.8f * (1f - f))
            }
        }
        tagAt(a.tag("burnaway", 0, 0), Offset(w * 0.3f, h * 0.07f), seg(bu, 0.1f, 0.3f), back = LitC28.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("burnaway", 0, 1), Offset(w * 0.7f, h * 0.07f), seg(bu, 0.15f, 0.35f), back = LitC28.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 8. the angels in their ranks; life for hearts, for the earth, for the dead; and the two who write (malaika) ═════════════

private object LifeC28 {
    val Top = Color(0xFF141C3E)
    val Low = Color(0xFF8A6A8A)
    val Ground = Color(0xFF2A2430)
    val Mount = Color(0xFF3A2E3E)
    val Dry = Color(0xFF8A6A48)
    val Green = Color(0xFF4A8A4A)
    val Cloud = Color(0xFFCFD6E6)
    val Horn = Color(0xFFEDE3CC)
    val HornEdge = Color(0xFFA89878)
    val Mound = Color(0xFF4A3E44)
    val Ink = Color(0xFFD8D0C4)
    val Page = Color(0xFFF6EED8)
    val Back = Color(0xE60E1018)
}

/**
 * Twilight over a wide land. Beats: ranks (rows of cool white lights settle each into its own place; ﴿بَلْ عِبَادٌ
 * مُكْرَمُونَ﴾, then ﴿وَمَا مِنَّا إِلَّا لَهُ مَقَامٌ مَعْلُومٌ﴾), jibril (a larger white light comes down to the cave where his ﷺ
 * light shines, named; a gold thread goes from it to a man, whose heart lights), mikail (a white light over the clouds;
 * rain falls on cracked ground, which greens), israfil (a white light at the horn; rings of sound go over the graves
 * and the dead rise), scribes (a man walks, a small white light at each shoulder with its page: ﴿كِرَامًا كَاتِبِينَ﴾).
 */
internal fun DrawScope.threelivesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "ranks" to Shot(Offset(w * 0.5f, h * 0.4f), 1.05f),
        "jibril" to Shot(Offset(w * 0.74f, h * 0.6f), 1.3f),
        "mikail" to Shot(Offset(w * 0.24f, h * 0.6f), 1.35f),
        "israfil" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "scribes" to Shot(Offset(w * 0.5f, h * 0.7f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { threelivesLand(a) }
}

private fun DrawScope.threelivesLand(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rk = a.b("ranks")
    val jb = a.b("jibril")
    val mk = a.b("mikail")
    val isr = a.b("israfil")
    val sc = a.b("scribes")
    val ground = h * 0.7f
    deepSky(t, ground, LifeC28.Top, LifeC28.Low, starAlpha = 0.7f)
    drawRect(LifeC28.Ground, Offset(0f, ground), Size(w, h - ground))
    // the mountain and its cave (right)
    drawPath(Path().apply { moveTo(w * 0.62f, ground); lineTo(w * 0.8f, h * 0.45f); lineTo(w * 0.9f, h * 0.5f); lineTo(w, h * 0.47f); lineTo(w, ground); close() }, LifeC28.Mount)
    val cave = Offset(w * 0.82f, h * 0.56f)
    drawArc(Color(0xFF0E0A12), 180f, 180f, true, Offset(cave.x - w * 0.025f, cave.y - h * 0.035f), Size(w * 0.05f, h * 0.07f))
    // the dry ground (left), greening under the rain
    val green = easeOut(seg(mk, 0.35f, 0.95f))
    drawRect(lerpColor(LifeC28.Dry, LifeC28.Green, green), Offset(0f, ground), Size(w * 0.44f, h * 0.06f), alpha = 0.9f)
    if (green < 1f) for (k in 0 until 6) {
        val x = w * (0.04f + 0.07f * k)
        drawPath(Path().apply { moveTo(x, ground + h * 0.01f); lineTo(x + w * 0.012f, ground + h * 0.025f); lineTo(x + w * 0.004f, ground + h * 0.04f); lineTo(x + w * 0.018f, ground + h * 0.055f) }, Color(0xFF4A3424), alpha = 1f - green, style = Stroke(1.2f * dp))
    }
    if (green > 0f) for (k in 0 until 12) {
        val x = w * (0.03f + 0.035f * k)
        val f = seg(green, k / 16f, k / 16f + 0.4f)
        drawLine(Color(0xFF7AC86A), Offset(x, ground), Offset(x - w * 0.005f, ground - h * 0.025f * f), 1.5f * dp, StrokeCap.Round)
        drawLine(Color(0xFF7AC86A), Offset(x, ground), Offset(x + w * 0.006f, ground - h * 0.02f * f), 1.5f * dp, StrokeCap.Round)
    }
    // the ranks: each light settles into its place
    val later = 1f - 0.7f * easeOut(seg(jb, 0f, 0.3f))
    for (r in 0 until 3) for (k in 0 until 7) {
        val f = easeOut(seg(rk, 0.05f + r * 0.12f + k * 0.02f, 0.4f + r * 0.12f + k * 0.02f))
        if (f <= 0f) continue
        val home = Offset(w * (0.2f + 0.1f * k + (if (r % 2 == 1) 0.05f else 0f)), h * (0.12f + 0.08f * r))
        val from = home + Offset(sin(k * 3.1f + r) * w * 0.12f, -h * 0.15f)
        angelLight26(from + (home - from) * f, w * 0.007f, t + k + r, f * later)
    }
    val v1 = seg(rk, 0.15f, 0.35f) * (1f - seg(rk, 0.55f, 0.65f))
    val v2 = seg(rk, 0.62f, 0.8f) * (1f - easeOut(seg(jb, 0f, 0.2f)))
    fitBig26(a.big("ranks", 0, 0), Offset(w * 0.5f, h * 0.45f), w * 0.5f, Color(0xFFEAF2FF), v1, glow = Color(0xFFCFE4FF))
    fitBig26(a.big("ranks", 0, 1), Offset(w * 0.5f, h * 0.45f), w * 0.62f, Color(0xFFEAF2FF), v2, glow = Color(0xFFCFE4FF))
    // 1. revelation: life for hearts
    if (jb > 0f) {
        val down = easeInOut(seg(jb, 0.05f, 0.5f))
        val from = Offset(w * 0.6f, -h * 0.04f)
        val to = cave + Offset(-w * 0.03f, -h * 0.06f)
        val ctrl = Offset(w * 0.62f, h * 0.3f)
        val trail = Path()
        for (k in 0..20) {
            val u = k / 20f * down
            val p = from * ((1 - u) * (1 - u)) + ctrl * (2 * u * (1 - u)) + to * (u * u)
            if (k == 0) trail.moveTo(p.x, p.y) else trail.lineTo(p.x, p.y)
        }
        val fadeAll = 1f - easeOut(seg(mk, 0f, 0.3f))
        drawPath(trail, Color(0xFFDDEBFF), alpha = 0.5f * fadeAll, style = Stroke(1.8f * dp, cap = StrokeCap.Round))
        val u = down
        angelLight26(from * ((1 - u) * (1 - u)) + ctrl * (2 * u * (1 - u)) + to * (u * u), w * 0.016f, t, easeOut(seg(jb, 0f, 0.15f)) * fadeAll.coerceAtLeast(0.3f))
        sirajMunir(cave + Offset(0f, -h * 0.005f), w * 0.01f, easeOut(seg(jb, 0.4f, 0.7f)), t, nameAt = cave + Offset(-w * 0.15f, -h * 0.06f))
        // the thread to a man's heart
        val man = Offset(w * 0.6f, h * 0.88f)
        val th = easeOut(seg(jb, 0.6f, 0.85f))
        val chest = man + Offset(0f, -h * 0.13f)
        if (th > 0f) drawLine(ArtInk.Gold, cave + Offset(-w * 0.01f, h * 0.03f), cave + Offset(-w * 0.01f, h * 0.03f) + (chest - cave - Offset(-w * 0.01f, h * 0.03f)) * th, 1.5f * dp, StrokeCap.Round, alpha = 0.85f * fadeAll)
        figure(man, h * 0.2f, Color(0xFF0A0810), easeOut(seg(jb, 0.5f, 0.7f)))
        val lit = easeOut(seg(jb, 0.8f, 1f))
        if (lit > 0f) {
            softGlow(chest, w * 0.04f, Color(0xFFFFE6B0), 0.8f * lit)
            drawCircle(Color(0xFFFFF4DA), w * 0.006f, chest, alpha = lit)
        }
    }
    // 2. rain: life for the earth
    if (mk > 0f) {
        val o = easeOut(seg(mk, 0f, 0.25f)) * (1f - easeOut(seg(isr, 0f, 0.3f)) * 0.6f)
        val cl = Offset(w * 0.22f, h * 0.4f)
        for (k in 0 until 5) drawCircle(LifeC28.Cloud, w * (0.04f + 0.012f * (k % 2)), cl + Offset(w * (-0.1f + 0.05f * k), if (k % 2 == 0) 0f else -h * 0.025f), alpha = 0.85f * o)
        angelLight26(cl + Offset(0f, -h * 0.1f), w * 0.011f, t, o)
        val rain = seg(mk, 0.15f, 0.9f)
        if (rain > 0f && rain < 1f) for (k in 0 until 16) {
            val f = fract(t * 1.8f + k * 0.11f)
            val x = cl.x + w * (-0.13f + 0.017f * k)
            val y = cl.y + h * 0.04f + f * (ground - cl.y - h * 0.04f)
            drawLine(Color(0xFF9AC0E8), Offset(x, y), Offset(x - w * 0.004f, y + h * 0.025f), 1.3f * dp, alpha = 0.8f)
        }
    }
    // 3. the horn: life for the dead
    if (isr > 0f) {
        val o = easeOut(seg(isr, 0f, 0.2f)) * (1f - easeOut(seg(sc, 0f, 0.3f)))
        val n = Offset(w * 0.42f, h * 0.12f)
        val m = Offset(w * 0.56f, h * 0.24f)
        val dir = (m - n) / (m - n).getDistance()
        val perp = Offset(-dir.y, dir.x)
        val horn = Path().apply {
            moveTo(n.x + perp.x * w * 0.006f, n.y + perp.y * w * 0.006f)
            lineTo(m.x + perp.x * w * 0.03f, m.y + perp.y * w * 0.03f)
            lineTo(m.x - perp.x * w * 0.03f, m.y - perp.y * w * 0.03f)
            lineTo(n.x - perp.x * w * 0.006f, n.y - perp.y * w * 0.006f)
            close()
        }
        drawPath(horn, LifeC28.Horn, alpha = o)
        drawPath(horn, LifeC28.HornEdge, alpha = o, style = Stroke(1.2f * dp))
        angelLight26(n - dir * w * 0.02f, w * 0.012f, t, o)
        val blow = seg(isr, 0.2f, 0.9f)
        if (blow > 0f) for (k in 0 until 4) {
            val f = fract(t * 0.5f + k / 4f)
            val r = w * (0.05f + 0.35f * f)
            drawArc(Color.White, 40f, 100f, false, Offset(m.x - r, m.y - r), Size(r * 2f, r * 2f), alpha = 0.35f * (1f - f) * o, style = Stroke(1.4f * dp))
        }
        for (k in 0 until 3) {
            val g = Offset(w * (0.38f + 0.12f * k), h * 0.9f)
            drawOval(LifeC28.Mound, Offset(g.x - w * 0.045f, g.y - h * 0.012f), Size(w * 0.09f, h * 0.03f), alpha = o)
            val up = easeOut(seg(isr, 0.45f + k * 0.08f, 0.85f + k * 0.08f))
            if (up > 0f) clipRect(0f, 0f, w, g.y) {
                figure(g + Offset(0f, h * 0.15f * (1f - up)), h * 0.15f, LifeC28.Ink, min(1f, up * 2f) * o)
            }
        }
    }
    // 4. the two who write
    if (sc > 0f) {
        val o = easeOut(seg(sc, 0f, 0.25f))
        val go = easeInOut(seg(sc, 0.05f, 1f))
        val foot = Offset(mix(w * 0.62f, w * 0.42f, go), h * 0.9f)
        val tall = h * 0.22f
        figure(foot, tall, LifeC28.Ink, o, walk = if (go in 0.01f..0.99f) t * 5f else 0f)
        for (side in listOf(-1f, 1f)) {
            val c = foot + Offset(side * tall * 0.3f, -tall * 0.85f)
            angelLight26(c, w * 0.007f, t + side, o)
            val pg = c + Offset(side * w * 0.035f, -h * 0.01f)
            drawRect(LifeC28.Page, Offset(pg.x - w * 0.013f, pg.y - h * 0.018f), Size(w * 0.026f, h * 0.036f), alpha = 0.9f * o)
            val lines = seg(sc, 0.2f, 0.95f)
            for (m in 0 until 4) {
                val f = seg(lines, m / 4f, (m + 1) / 4f)
                if (f > 0f) drawLine(Color(0xFF9A8A6A), Offset(pg.x + w * 0.009f, pg.y - h * 0.01f + m * h * 0.007f), Offset(pg.x + w * 0.009f - w * 0.018f * f, pg.y - h * 0.01f + m * h * 0.007f), 0.9f * dp, alpha = o)
            }
        }
        fitBig26(a.big("scribes"), Offset(w * 0.5f, h * 0.48f), w * 0.4f, Color(0xFFEAF2FF), seg(sc, 0.3f, 0.6f), glow = Color(0xFFCFE4FF))
    }
}
