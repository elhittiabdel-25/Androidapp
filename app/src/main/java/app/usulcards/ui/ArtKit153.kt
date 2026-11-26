package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin

/*
 * Kit 153 (2026-11-26): the visual vocabulary the owner chose (tools/cards/CODEX.md, "Visual vocabulary"): speech
 * as sound waves, an oath as one raised right hand, a lie as a cracking tag, revelation as a beam from above, report
 * grades as seals, du'a as open palms with rising lights, a grave mound, the garden light of Paradise, flames, the
 * glows of emotion, real goods, far animal silhouettes that face their way, and a battle map.
 */

internal object V153 {
    val Wave = Color(0xFFF6D88A)
    val Sad = Color(0xFF6FA8DC)
    val Joy = Color(0xFFF2C45A)
    val Fear = Color(0xFF8A2E3A)
    val Mercy = Color(0xFFFFF3C8)
    val Parch = Color(0xFFE9DCB8)
    val ParchInk = Color(0xFF6B5638)
    val Muslim = Color(0xFF3F9A5C)
    val Enemy = Color(0xFFB8403A)
    val Sahih = Color(0xFFE8B84A)
    val Hasan = Color(0xFFC8CCD6)
    val Weak = Color(0xFF7A7A82)
}

/** Speech: sound-wave arcs flowing from [c] toward [dir] (-1 left, +1 right), pulsing with [t]. */
internal fun DrawScope.speech153(c: Offset, s: Float, color: Color, alpha: Float, t: Float = 0f, dir: Float = -1f) {
    if (alpha <= 0.01f) return
    val sw = s * 0.1f
    for (k in 0 until 3) {
        val ph = ((t * 0.9f + k / 3f) % 1f)
        val r = s * (0.3f + 0.75f * ph)
        val start = if (dir < 0) 180f - 40f else -40f
        drawArc(color, start, 80f, false, Offset(c.x - r, c.y - r), Size(r * 2f, r * 2f), alpha = alpha * (1f - 0.7f * ph), style = Stroke(sw, cap = StrokeCap.Round))
    }
}

/** An oath: one raised right hand, palm forward, fingers together and thumb out. */
internal fun DrawScope.oathHand153(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val palm = Path().apply {
        moveTo(c.x - s * 0.18f, c.y + s * 0.45f)
        lineTo(c.x - s * 0.2f, c.y - s * 0.05f)
        // four fingers
        for (k in 0 until 4) {
            val x0 = c.x - s * 0.2f + s * 0.1f * k
            val top = c.y - s * (0.42f + if (k == 1 || k == 2) 0.06f else 0f)
            lineTo(x0, top + s * 0.04f)
            quadraticTo(x0 + s * 0.05f, top - s * 0.03f, x0 + s * 0.1f, top + s * 0.04f)
        }
        lineTo(c.x + s * 0.2f, c.y - s * 0.02f)
        // thumb, to the right for a right hand seen from the front
        quadraticTo(c.x + s * 0.34f, c.y - s * 0.12f, c.x + s * 0.36f, c.y - s * 0.02f)
        quadraticTo(c.x + s * 0.32f, c.y + s * 0.12f, c.x + s * 0.18f, c.y + s * 0.2f)
        lineTo(c.x + s * 0.16f, c.y + s * 0.45f)
        close()
    }
    drawPath(palm, color, alpha = alpha)
    drawRect(color, Offset(c.x - s * 0.17f, c.y + s * 0.45f), Size(s * 0.33f, s * 0.22f), alpha = alpha * 0.8f)
}

/** A lie: cracks running across a tag at [c] of width [wid], drawn after the tag; [q] 0..1 grows them. */
internal fun DrawScope.crack153(c: Offset, wid: Float, q: Float, alpha: Float) {
    if (alpha <= 0.01f || q <= 0f) return
    drawRect(Color(0xFF2A2A30), Offset(c.x - wid / 2f, c.y - wid * 0.09f), Size(wid, wid * 0.18f), alpha = 0.55f * alpha * q)
    val p = Path().apply {
        moveTo(c.x - wid * 0.45f, c.y - wid * 0.02f)
        lineTo(c.x - wid * 0.2f, c.y + wid * 0.04f)
        lineTo(c.x - wid * 0.05f, c.y - wid * 0.05f)
        lineTo(c.x + wid * 0.12f, c.y + wid * 0.05f)
        lineTo(c.x + wid * 0.45f * q, c.y - wid * 0.03f)
    }
    drawPath(p, Color(0xFFDADADF), alpha = alpha * q, style = Stroke(size.width * 0.004f))
}

/** «فنزلت الآية»: a beam of light coming down from the top onto [to]. */
internal fun DrawScope.revelation153(to: Offset, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    lightShafts(Offset(to.x, 0f), 1.5708f, 0.22f, to.y, Qn.GoldLight, 0.5f * alpha, t, rays = 5)
    softGlow(to, size.width * 0.08f, Qn.GoldLight, 0.6f * alpha)
}

/** The grade of a report as a seal: [grade] 0 sahih (gold), 1 hasan (silver), 2 weak or mursal (grey). */
internal fun DrawScope.seal153(c: Offset, r: Float, grade: Int, alpha: Float) {
    if (alpha <= 0.01f) return
    val col = when (grade) { 0 -> V153.Sahih; 1 -> V153.Hasan; else -> V153.Weak }
    for (k in 0 until 16) {
        val a = k / 16f * 6.2832f
        drawCircle(col, r * 0.18f, Offset(c.x + r * cos(a), c.y + r * sin(a)), alpha = alpha)
    }
    drawCircle(col, r, c, alpha = alpha)
    drawCircle(lerpColor(col, Color.Black, 0.35f), r * 0.72f, c, alpha = alpha, style = Stroke(r * 0.08f))
}

/** Du'a: two open palms with small lights rising from them. */
internal fun DrawScope.dua153(c: Offset, s: Float, color: Color, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    hands12(c, s, color, alpha)
    for (k in 0 until 4) {
        val ph = (t * 0.35f + k / 4f) % 1f
        drawCircle(Qn.GoldLight, s * 0.04f, Offset(c.x + s * 0.12f * sin(k * 1.7f + t), c.y - s * 0.5f - s * 1.6f * ph), alpha = alpha * (1f - ph))
    }
}

/** Death: a simple grave mound with a small upright stone. */
internal fun DrawScope.grave153(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawArc(Color(0xFF6A5238), 180f, 180f, true, Offset(c.x - s, c.y - s * 0.35f), Size(s * 2f, s * 0.7f), alpha = alpha)
    drawRect(Color(0xFF8A7A66), Offset(c.x + s * 0.55f, c.y - s * 0.55f), Size(s * 0.12f, s * 0.4f), alpha = alpha)
}

/** Paradise suggested: soft green-gold light over trees and a flowing river. */
internal fun DrawScope.garden153(alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    val w = size.width
    val h = size.height
    softGlow(Offset(w * 0.5f, h * 0.35f), w * 0.5f, Color(0xFFCFE8A0), 0.4f * alpha)
    for (k in 0 until 4) qIcon("tree", Offset(w * (0.15f + 0.23f * k), h * 0.5f), w * 0.1f, Qn.Leaf, alpha, t)
    for (k in 0 until 2) drawLine(Color(0xFF6FB8D8), Offset(0f, h * (0.66f + 0.03f * k)), Offset(w, h * (0.64f + 0.03f * k)), w * 0.012f, alpha = 0.8f * alpha)
}

/** Hellfire or punishment: rising flames along the bottom of the scene (never on a person). */
internal fun DrawScope.flames153(alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    val w = size.width
    val h = size.height
    drawRect(Qn.Ember, Offset(0f, h * 0.78f), Size(w, h * 0.22f), alpha = 0.25f * alpha)
    for (k in 0 until 7) qFlame(Offset(w * (0.07f + 0.143f * k), h * 0.98f), w * (0.12f + 0.03f * sin(t * 3f + k)), Qn.Ember, alpha, t + k)
}

/** Emotion around a person at [c]: 0 sadness (cool blue), 1 joy (gold), 2 fear (dark red); also used for tears. */
internal fun DrawScope.mood153(c: Offset, r: Float, mood: Int, alpha: Float) {
    if (alpha <= 0.01f) return
    softGlow(c, r, when (mood) { 0 -> V153.Sad; 1 -> V153.Joy; else -> V153.Fear }, 0.6f * alpha)
}

/** A figure with the posture of its mood: 0 sad (head bowed), 1 joyful (upright), 2 afraid (crouched), with its glow. */
internal fun DrawScope.moodFigure153(foot: Offset, tall: Float, mood: Int, alpha: Float) {
    if (alpha <= 0.01f) return
    mood153(foot - Offset(0f, tall * 0.6f), tall * 0.8f, mood, alpha)
    when (mood) {
        0 -> figure(foot, tall * 0.95f, Qn.Person, alpha, lean = 0.3f)
        1 -> figure(foot, tall, Qn.Person, alpha)
        else -> figure(foot, tall * 0.7f, Qn.Person, alpha, lean = 0.5f)
    }
}

/** Mercy, forgiveness, accepted repentance: a soft beam of light falling on the person at [on]. */
internal fun DrawScope.mercy153(on: Offset, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    lightShafts(Offset(on.x, 0f), 1.5708f, 0.18f, on.y, V153.Mercy, 0.4f * alpha, t, rays = 4)
}

/** Goods: a grain sack. */
internal fun DrawScope.sack153(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Color(0xFFC9A46A), Offset(c.x - s * 0.35f, c.y - s * 0.3f), Size(s * 0.7f, s * 0.75f), androidx.compose.ui.geometry.CornerRadius(s * 0.2f), alpha = alpha)
    drawLine(Color(0xFF7A5A30), Offset(c.x - s * 0.2f, c.y - s * 0.22f), Offset(c.x + s * 0.2f, c.y - s * 0.22f), s * 0.05f, alpha = alpha)
}

/** Goods: a small heap of dates. */
internal fun DrawScope.dates153(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (r in 0 until 3) for (k in 0..(2 - r)) drawOval(Color(0xFF7A3E1E), Offset(c.x + s * (0.22f * (k - (2 - r) / 2f)) - s * 0.1f, c.y - s * 0.14f * r - s * 0.07f), Size(s * 0.2f, s * 0.13f), alpha = alpha)
}

/** An animal far away: a tiny dark camel silhouette facing [dir] (-1 left, +1 right), legs moving with [walk]. */
internal fun DrawScope.camelFar153(foot: Offset, s: Float, dir: Float, alpha: Float, walk: Float = 0f) {
    if (alpha <= 0.01f) return
    val col = Color(0xFF2A2018)
    val d = if (dir < 0) -1f else 1f
    drawOval(col, Offset(foot.x - s * 0.4f, foot.y - s * 0.75f), Size(s * 0.8f, s * 0.32f), alpha = alpha)
    drawCircle(col, s * 0.15f, Offset(foot.x - s * 0.05f, foot.y - s * 0.78f), alpha = alpha)
    val neck = Offset(foot.x + d * s * 0.38f, foot.y - s * 0.62f)
    val head = Offset(foot.x + d * s * 0.6f, foot.y - s * 1.0f)
    drawLine(col, neck, head, s * 0.09f, StrokeCap.Round, alpha = alpha)
    drawOval(col, Offset(head.x - s * 0.06f + d * s * 0.04f, head.y - s * 0.05f), Size(s * 0.16f, s * 0.09f), alpha = alpha)
    val sw = sin(walk) * s * 0.08f
    for ((x, ph) in listOf(-0.3f to 1f, -0.18f to -1f, 0.2f to -1f, 0.32f to 1f)) {
        val top = Offset(foot.x + x * s, foot.y - s * 0.5f)
        drawLine(col, top, Offset(top.x + ph * sw * d, foot.y), s * 0.05f, StrokeCap.Round, alpha = alpha)
    }
}

/** A walking person who faces [dir]: leans slightly toward the way he goes, never away from it. */
internal fun DrawScope.walker153(foot: Offset, tall: Float, color: Color, alpha: Float, walk: Float, dir: Float) {
    figure(foot, tall, color, alpha, walk = walk, lean = -0.08f * (if (dir < 0) -1f else 1f))
}

/** Battle map: a parchment top-down map filling the scene. */
internal fun DrawScope.battleMap153(alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(V153.Parch, Offset.Zero, size, alpha = alpha)
    drawRect(V153.ParchInk, Offset(size.width * 0.03f, size.height * 0.03f), Size(size.width * 0.94f, size.height * 0.94f), alpha = 0.5f * alpha, style = Stroke(size.width * 0.004f))
}

/** An army block on the battle map at [c]: [muslim] green or the enemy red, with a banner. */
internal fun DrawScope.army153(c: Offset, wid: Float, muslim: Boolean, alpha: Float) {
    if (alpha <= 0.01f) return
    val col = if (muslim) V153.Muslim else V153.Enemy
    drawRect(col, Offset(c.x - wid / 2f, c.y - wid * 0.18f), Size(wid, wid * 0.36f), alpha = alpha)
    drawRect(lerpColor(col, Color.Black, 0.4f), Offset(c.x - wid / 2f, c.y - wid * 0.18f), Size(wid, wid * 0.36f), alpha = alpha, style = Stroke(wid * 0.04f))
    drawLine(V153.ParchInk, Offset(c.x, c.y - wid * 0.18f), Offset(c.x, c.y - wid * 0.5f), wid * 0.03f, alpha = alpha)
    drawRect(col, Offset(c.x, c.y - wid * 0.5f), Size(wid * 0.22f, wid * 0.13f), alpha = alpha)
}

/** A movement arrow on the battle map from [a] to [b], drawn up to [q]. */
internal fun DrawScope.arrow153(a: Offset, b: Offset, muslim: Boolean, q: Float, alpha: Float) {
    if (alpha <= 0.01f || q <= 0f) return
    val col = if (muslim) V153.Muslim else V153.Enemy
    val e = Offset(a.x + (b.x - a.x) * q, a.y + (b.y - a.y) * q)
    drawLine(col, a, e, size.width * 0.012f, StrokeCap.Round, alpha = alpha)
    val dx = e.x - a.x
    val dy = e.y - a.y
    val len = kotlin.math.sqrt(dx * dx + dy * dy).coerceAtLeast(1f)
    val ux = dx / len
    val uy = dy / len
    val hs = size.width * 0.035f
    drawPath(Path().apply {
        moveTo(e.x + ux * hs * 0.4f, e.y + uy * hs * 0.4f)
        lineTo(e.x - ux * hs - uy * hs * 0.6f, e.y - uy * hs + ux * hs * 0.6f)
        lineTo(e.x - ux * hs + uy * hs * 0.6f, e.y - uy * hs - ux * hs * 0.6f)
        close()
    }, col, alpha = alpha)
}

/** A law book: an open volume, for hadd punishments (the act is never drawn). */
internal fun DrawScope.lawBook153(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    qIcon("book", c, s, Qn.Gold, alpha)
    qIcon("scale", c + Offset(0f, -s * 0.75f), s * 0.5f, Qn.GoldLight, alpha)
}
