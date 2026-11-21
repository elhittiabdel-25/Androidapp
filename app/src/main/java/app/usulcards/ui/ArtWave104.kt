package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 104 (2026-11-17): the fifteen tafsir cards of batch 18 (tt-*) redrawn in the illustrated style (boards in wave 94).
 * The world is a lamp-lit study at night: an arched window, the open book on its stand. Meanings are pictured, not
 * listed: streams joining one river for agreeing readings, a fork for opposites, the sky, the dawn, the trees and the
 * lands the verses swear by. Scholars are roundels (Ibn Abbas and Umar in colour, the rest plain); no figure for them.
 */

internal object T104 {
    val Wall = Color(0xFF1C1A2E)
    val WallLow = Color(0xFF2E2638)
    val Page = Color(0xFFF2E6C8)
    val Rule = Color(0xFF8A7050)
    val Water = Color(0xFF7AB0D0)
}

/** The study: dark wall, an arched window on the night with a crescent, a lamp's warm pool, the book on its stand. */
internal fun DrawScope.study104(a: ArtCtx, book: Float = 1f) {
    val w = size.width
    val h = size.height
    drawRect(Brush.verticalGradient(listOf(T104.Wall, T104.WallLow)))
    val win = Path().apply {
        moveTo(w * 0.7f, h * 0.62f); lineTo(w * 0.7f, h * 0.28f)
        quadraticBezierTo(w * 0.8f, h * 0.1f, w * 0.9f, h * 0.28f); lineTo(w * 0.9f, h * 0.62f); close()
    }
    drawPath(win, Color(0xFF0E1530))
    qIcon("crescent", Offset(w * 0.82f, h * 0.3f), w * 0.05f, Qn.GoldLight, 0.8f, a.t)
    drawPath(win, Color(0xFF6A5A48), style = Stroke(3f * density))
    drawRect(Color(0xFF3A2C22), Offset(0f, h * 0.86f), Size(w, h * 0.14f))
    softGlow(Offset(w * 0.18f, h * 0.6f), w * 0.3f, Color(0xFFFFC870), 0.25f + 0.04f * sin(a.t * 3f))
    drawLine(Color(0xFF8A6A40), Offset(w * 0.18f, h * 0.86f), Offset(w * 0.18f, h * 0.66f), 3f * density)
    drawCircle(Color(0xFFFFE0A0), w * 0.016f, Offset(w * 0.18f, h * 0.64f))
    if (book > 0.01f) {
        drawLine(Color(0xFF6A4A30), Offset(w * 0.42f, h * 0.86f), Offset(w * 0.58f, h * 0.76f), 3f * density, alpha = book)
        drawLine(Color(0xFF6A4A30), Offset(w * 0.58f, h * 0.86f), Offset(w * 0.42f, h * 0.76f), 3f * density, alpha = book)
        openBook21(Offset(w * 0.5f, h * 0.74f), w * 0.22f, T104.Page, T104.Rule, book, 0.3f)
    }
}

internal fun DrawScope.dim104(alpha: Float, k: Float = 0.5f) = drawRect(Color(0xFF0E0C1A), alpha = k * alpha)

/** Several streams from the top that join one river at [join]. */
internal fun DrawScope.streams104(n: Int, join: Offset, u: Float, alpha: Float, color: Color = T104.Water) {
    val w = size.width
    val h = size.height
    for (k in 0 until n) {
        val x0 = w * (0.15f + 0.7f * k / (n - 1).coerceAtLeast(1))
        val path = Path().apply { moveTo(x0, h * 0.12f); quadraticBezierTo(x0, join.y - h * 0.1f, join.x, join.y) }
        drawPath(path, color, alpha = alpha * u, style = Stroke(3f * density, cap = StrokeCap.Round))
    }
    drawLine(color, join, Offset(join.x, h * 0.92f), 6f * density, StrokeCap.Round, alpha = alpha * seg(u, 0.6f, 1f))
}

/** A fork: one road that splits; the [open] branch lit, the other blocked. */
internal fun DrawScope.fork104(c: Offset, open: Int, u: Float, alpha: Float) {
    val w = size.width
    val h = size.height
    drawLine(Color(0xFFB8A888), Offset(c.x, h * 0.9f), c, 4f * density, StrokeCap.Round, alpha = alpha)
    for (s in 0 until 2) {
        val end = c + Offset((if (s == 0) -1f else 1f) * w * 0.25f, -h * 0.3f)
        drawLine(if (s == open) Qn.Jade else Color(0xFF6A6474), c, Offset(mix(c.x, end.x, u), mix(c.y, end.y, u)), 4f * density, StrokeCap.Round, alpha = alpha)
        if (s != open && u > 0.9f) {
            val m = c + (end - c) * 0.6f
            drawLine(Qn.Ember, m + Offset(-w * 0.03f, -h * 0.02f), m + Offset(w * 0.03f, h * 0.02f), 3f * density, StrokeCap.Round, alpha = alpha)
            drawLine(Qn.Ember, m + Offset(-w * 0.03f, h * 0.02f), m + Offset(w * 0.03f, -h * 0.02f), 3f * density, StrokeCap.Round, alpha = alpha)
        }
    }
}

internal fun DrawScope.hills104(ground: Float, color: Color, alpha: Float = 1f) {
    val w = size.width
    val h = size.height
    drawPath(Path().apply {
        moveTo(0f, ground); quadraticBezierTo(w * 0.25f, ground - h * 0.12f, w * 0.5f, ground - h * 0.03f)
        quadraticBezierTo(w * 0.75f, ground - h * 0.14f, w, ground - h * 0.02f); lineTo(w, h); lineTo(0f, h); close()
    }, color, alpha = alpha)
}

internal fun DrawScope.tree104(foot: Offset, s: Float, leaf: Color, fruit: Color, alpha: Float, grow: Float = 1f) {
    if (alpha <= 0.01f) return
    drawLine(Color(0xFF6A4A30), foot, foot + Offset(0f, -s * grow), s * 0.1f, StrokeCap.Round, alpha = alpha)
    drawCircle(leaf, s * 0.45f * grow, foot + Offset(0f, -s * grow), alpha = alpha)
    for (k in 0 until 5) {
        val ang = k * 1.3f
        drawCircle(fruit, s * 0.07f, foot + Offset(cos(ang) * s * 0.28f, -s * grow + sin(ang) * s * 0.25f), alpha = alpha * seg(grow, 0.7f, 1f))
    }
}

internal fun DrawScope.dawn104(u: Float, ground: Float) {
    val w = size.width
    val h = size.height
    drawRect(Brush.verticalGradient(listOf(lerpColor(Color(0xFF0E1530), Color(0xFF6A8AC0), u), lerpColor(Color(0xFF2A3560), Color(0xFFF2C08A), u)), endY = ground))
    softGlow(Offset(w * 0.5f, ground), w * 0.5f * (0.4f + u), Color(0xFFFFD890), 0.6f * u)
    drawRect(Color(0xFF2E241C), Offset(0f, ground), Size(w, h - ground))
}

// ═════════════ tt-mafhum ═════════════
internal fun DrawScope.ttmafhumScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ttmafhum", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { study104(a) },
        { p, f ->
            dim104(f, 0.3f)
            val c = Offset(w * 0.5f, h * 0.36f)
            drawRect(Color(0xFF3A3448), Offset(c.x - w * 0.18f, c.y - h * 0.1f), Size(w * 0.36f * (1f - easeInOut(seg(p, 0.3f, 0.6f))), h * 0.2f), alpha = f)
            softGlow(c, w * 0.18f, Qn.GoldLight, 0.6f * f * seg(p, 0.35f, 0.7f))
            for (k in 0 until 3) tagAt(a.tag("ttmafhum0", 0, k), Offset(w * (0.22f + 0.28f * k), h * 0.56f), f * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k), ink = if (k == 1) ArtInk.Gold else ArtInk.Ivory)
        },
        { p, f ->
            val core = Offset(w * 0.5f, h * 0.4f)
            drawCircle(Qn.GoldLight, w * 0.05f, core, alpha = f * seg(p, 0.3f, 0.5f))
            softGlow(core, w * 0.12f, Qn.GoldLight, 0.5f * f * seg(p, 0.3f, 0.6f))
            drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.7f), core, 2f * density, alpha = 0.6f * f * seg(p, 0.15f, 0.35f))
            tagAt(a.tag("ttmafhum1", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttmafhum1", 0, 1), core + Offset(0f, -h * 0.12f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            qIcon("key", Offset(w * 0.75f, h * 0.36f), w * 0.07f, Qn.Jade, f * seg(p, 0.6f, 0.75f), a.t)
            tagAt(a.tag("ttmafhum1", 0, 2), Offset(w * 0.75f, h * 0.48f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            dim104(f)
            val c = Offset(w * 0.5f, h * 0.45f)
            drawCircle(Qn.Gold, w * 0.08f, c, alpha = f * seg(p, 0.05f, 0.25f))
            qIcon("key", c, w * 0.08f, Color(0xFF1C1A2E), f * seg(p, 0.1f, 0.3f), a.t)
            drawCircle(Color(0xFF8A8494), w * 0.2f, c, alpha = 0.7f * f * seg(p, 0.45f, 0.65f), style = Stroke(w * 0.03f))
            tagAt(a.tag("ttmafhum2", 0, 0), c + Offset(0f, h * 0.02f), f * seg(p, 0.2f, 0.35f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("ttmafhum2", 0, 1), c + Offset(0f, h * 0.26f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val ground = h * 0.86f
            drawRect(Brush.verticalGradient(listOf(Color(0xFF5A6A80), Color(0xFFB8C0C8)), endY = ground), alpha = f)
            drawRect(Color(0xFF6A7A4A), Offset(0f, ground), Size(w, h - ground), alpha = f)
            for (k in 0 until 3) drawCircle(Color(0xFFE8ECF2), w * 0.1f, Offset(w * (0.3f + 0.2f * k), h * 0.2f), alpha = f)
            val press = seg(p, 0.35f, 1f)
            for (k in 0 until 18) {
                val x = w * (0.18f + 0.04f * k)
                val y = h * 0.28f + ((a.t * 0.6f + k * 0.17f) % 1f) * (ground - h * 0.28f)
                drawLine(T104.Water, Offset(x, y), Offset(x, y + h * 0.04f), 1.6f * density, alpha = f * press)
            }
            tagAt(a.tag("ttmafhum3", 0, 2), Offset(w * 0.5f, h * 0.08f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("ttmafhum3", 0, 0), Offset(w * 0.82f, h * 0.2f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttmafhum3", 0, 1), Offset(w * 0.5f, ground + h * 0.06f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tt-tanawwu ═════════════
internal fun DrawScope.tttanawwuScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tttanawwu", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study104(a) },
        { p, f ->
            dim104(f)
            for (k in 0 until 5) drawCircle(Qn.Jade, w * 0.03f, Offset(w * (0.12f + 0.065f * k), h * 0.45f), alpha = f * seg(p, 0.05f, 0.25f))
            drawLine(Qn.Jade, Offset(w * 0.1f, h * 0.52f), Offset(w * 0.4f, h * 0.52f), 2f * density, alpha = f * seg(p, 0.2f, 0.35f))
            balance12(Offset(w * 0.72f, h * 0.62f), w * 0.12f, 0.2f * sin(a.t * 1.2f), Qn.GoldLight, f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("tttanawwu0", 0, 0), Offset(w * 0.25f, h * 0.32f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("tttanawwu0", 0, 1), Offset(w * 0.72f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f, 0.6f)
            streams104(3, Offset(w * 0.5f, h * 0.62f), easeInOut(seg(p, 0.1f, 0.7f)), f)
            softGlow(Offset(w * 0.5f, h * 0.62f), w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.6f, 0.85f))
            tagAt(a.tag("tttanawwu1", 0, 1), Offset(w * 0.5f, h * 0.08f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("tttanawwu1", 0, 0), Offset(w * 0.72f, h * 0.7f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 3) drawLine(T104.Water, Offset(w * 0.5f, h * 0.68f), Offset(w * (0.3f + 0.2f * k), h * 0.3f), 2f * density, StrokeCap.Round, alpha = 0.7f * f * seg(p, 0.15f + 0.1f * k, 0.3f + 0.1f * k))
            qIcon("check", Offset(w * 0.5f, h * 0.22f), w * 0.07f, Qn.Jade, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("tttanawwu2", 0, 2), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("tttanawwu2", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("tttanawwu2", 0, 1), Offset(w * 0.5f, h * 0.34f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            dim104(f)
            val c = Offset(w * 0.5f, h * 0.36f)
            qIcon("crown", c, w * 0.09f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 3) {
                val at = Offset(w * (0.25f + 0.25f * k), h * 0.66f)
                drawLine(Qn.GoldLight, c + Offset(0f, h * 0.05f), at, 1.4f * density, alpha = 0.6f * f * seg(p, 0.25f + 0.15f * k, 0.4f + 0.15f * k))
                qIcon("star", at, w * 0.05f, Qn.GoldLight, f * seg(p, 0.3f + 0.15f * k, 0.45f + 0.15f * k), a.t)
            }
            tagAt(a.tag("tttanawwu3", 0, 0), c + Offset(0f, -h * 0.12f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("tttanawwu3", 0, 1), Offset(w * 0.5f, h * 0.82f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tt-tadadd ═════════════
internal fun DrawScope.tttadaddScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tttadadd", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study104(a) },
        { p, f ->
            tagAt(a.tag("tttadadd0", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            balance12(Offset(w * 0.5f, h * 0.44f), w * 0.12f, 0.15f * sin(a.t), Qn.GoldLight, f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("tttadadd0", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.6f)
            streams104(4, Offset(w * 0.5f, h * 0.6f), easeInOut(seg(p, 0.1f, 0.7f)), f)
            tagAt(a.tag("tttadadd1", 0, 0), Offset(w * 0.5f, h * 0.08f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("tttadadd1", 0, 1), Offset(w * 0.72f, h * 0.7f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            dim104(f, 0.6f)
            fork104(Offset(w * 0.5f, h * 0.62f), 0, easeInOut(seg(p, 0.1f, 0.6f)), f)
            tagAt(a.tag("tttadadd2", 0, 2), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("tttadadd2", 0, 0), Offset(w * 0.25f, h * 0.26f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("tttadadd2", 0, 1), Offset(w * 0.75f, h * 0.26f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            val ground = h * 0.86f
            val u = seg(p, 0f, 1f)
            drawRect(Brush.horizontalGradient(listOf(Color(0xFF3A3060), Color(0xFF0E1530), Color(0xFF4A5A90))), alpha = f)
            drawRect(Color(0xFF2E241C), Offset(0f, ground), Size(w, h - ground), alpha = f)
            qIcon("crescent", Offset(w * 0.2f, h * 0.3f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.1f, 0.3f), a.t)
            qIcon("star", Offset(w * 0.8f, h * 0.3f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.45f, 0.65f), a.t)
            tagAt(a.tag("tttadadd3", 0, 2), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0f, 0.15f) * (0.5f + 0.5f * u), ink = ArtInk.Gold)
            tagAt(a.tag("tttadadd3", 0, 0), Offset(w * 0.2f, h * 0.46f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("tttadadd3", 0, 1), Offset(w * 0.8f, h * 0.46f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tt-asbab ═════════════
internal fun DrawScope.ttasbabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ttasbab", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study104(a) },
        { p, f ->
            dim104(f, 0.6f)
            for (k in 0 until 3) {
                val come = easeInOut(seg(p, 0.1f + 0.1f * k, 0.6f + 0.1f * k))
                qIcon("scroll", Offset(mix(w * (0.2f + 0.3f * k), w * 0.5f, come), mix(h * 0.25f, h * 0.45f, come)), w * 0.07f, T104.Page, f * (1f - 0.5f * come), a.t)
            }
            softGlow(Offset(w * 0.5f, h * 0.45f), w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.6f, 0.85f))
            tagAt(a.tag("ttasbab0", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
            tagAt(a.tag("ttasbab0", 0, 1), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0.75f, 0.9f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f, 0.6f)
            val c = Offset(w * 0.5f, h * 0.62f)
            tagAt(a.tag("ttasbab1", 0, 0), c, f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            for (k in 0 until 3) {
                val to = Offset(w * (0.2f + 0.3f * k), h * 0.3f)
                drawLine(Qn.GoldLight, c + Offset(0f, -h * 0.04f), Offset(mix(c.x, to.x, seg(p, 0.25f, 0.6f)), mix(c.y, to.y, seg(p, 0.25f, 0.6f))), 1.6f * density, alpha = 0.7f * f)
                drawCircle(Qn.GoldLight, w * 0.02f, to, alpha = f * seg(p, 0.55f + 0.05f * k, 0.65f + 0.05f * k))
            }
            tagAt(a.tag("ttasbab1", 0, 2), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("ttasbab1", 0, 1), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f, 0.6f)
            for (k in 0 until 3) {
                val c = Offset(w * (0.2f + 0.3f * k), h * 0.42f)
                drawCircle(Color(0xFF3A3448), w * 0.1f, c, alpha = f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k))
                tagAt(a.tag("ttasbab2", 0, k), c, f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k), ink = ArtInk.Ivory)
            }
            drawLine(Qn.GoldLight, Offset(w * 0.1f, h * 0.6f), Offset(w * 0.9f, h * 0.6f), 2f * density, alpha = f * seg(p, 0.7f, 0.85f))
            tagAt(a.tag("ttasbab2", 0, 3), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f)
            tagAt(a.tag("ttasbab3", 0, 2), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.52f), Offset(w * 0.28f, h * 0.24f), 1.5f * density, alpha = 0.6f * f * seg(p, 0.15f, 0.3f))
            drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.52f), Offset(w * 0.72f, h * 0.24f), 1.5f * density, alpha = 0.6f * f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("ttasbab3", 0, 0), Offset(w * 0.28f, h * 0.18f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttasbab3", 0, 1), Offset(w * 0.72f, h * 0.18f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ tt-lafz ═════════════
internal fun DrawScope.ttlafzScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ttlafz", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study104(a) },
        { p, f ->
            dim104(f, 0.4f)
            val icons = listOf("lips", "light", "scale")
            for (k in 0 until 3) {
                val c = Offset(w * (0.22f + 0.28f * k), h * 0.36f)
                qIcon(icons[k], c, w * 0.08f, Qn.GoldLight, f * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k), a.t)
                drawLine(Qn.GoldLight, c + Offset(0f, h * 0.06f), Offset(w * 0.5f, h * 0.66f), 1.2f * density, alpha = 0.5f * f * seg(p, 0.2f + 0.25f * k, 0.35f + 0.25f * k))
                tagAt(a.tag("ttlafz0", 0, k), c + Offset(0f, -h * 0.1f), f * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            dim104(f, 0.6f)
            tagAt(a.tag("ttlafz1", 0, 0), Offset(w * 0.3f, h * 0.4f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            for (k in 0 until 6) {
                val y = h * (0.24f + 0.06f * k)
                drawLine(T104.Rule, Offset(w * 0.56f, y), Offset(w * (0.56f + 0.3f * seg(p, 0.3f + 0.05f * k, 0.45f + 0.05f * k)), y), 2f * density, alpha = f)
            }
            drawRoundRect(T104.Page, Offset(w * 0.52f, h * 0.18f), Size(w * 0.38f, h * 0.42f), CornerRadius(6f * density), alpha = 0.2f * f)
            drawLine(Qn.GoldLight, Offset(w * 0.4f, h * 0.4f), Offset(w * 0.52f, h * 0.4f), 2f * density, alpha = f * seg(p, 0.6f, 0.75f))
            tagAt(a.tag("ttlafz1", 0, 1), Offset(w * 0.71f, h * 0.68f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f, 0.4f)
            qIcon("point", Offset(w * 0.3f, h * 0.36f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            val run = easeInOut(seg(p, 0.4f, 0.8f))
            drawLine(Qn.GoldLight, Offset(w * 0.36f, h * 0.38f), Offset(mix(w * 0.36f, w * 0.48f, run), mix(h * 0.38f, h * 0.68f, run)), 2f * density, StrokeCap.Round, alpha = f)
            tagAt(a.tag("ttlafz2", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttlafz2", 0, 1), Offset(w * 0.3f, h * 0.22f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f)
            close97(Offset(w * 0.24f, h * 0.36f), w * 0.08f, a, "ttlafz3", 2, 3, f * seg(p, 0.05f, 0.25f))
            qIcon("scroll", Offset(w * 0.55f, h * 0.36f), w * 0.09f, T104.Page, f * seg(p, 0.25f, 0.4f), a.t)
            qIcon("hourglass", Offset(w * 0.8f, h * 0.36f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("ttlafz3", 0, 0), Offset(w * 0.55f, h * 0.52f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttlafz3", 0, 1), Offset(w * 0.8f, h * 0.52f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tt-tabaqat ═════════════
internal fun DrawScope.tttabaqatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tttabaqat", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study104(a, 0f) },
        { p, f ->
            dim104(f, 0.4f)
            val tones = listOf(Qn.GoldLight, Qn.Jade, Color(0xFFB8C0E0))
            for (k in 0 until 3) {
                val y = h * (0.28f + 0.2f * k)
                val on = seg(p, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
                for (j in 0 until (3 + k * 2)) drawCircle(tones[k], w * 0.018f, Offset(w * 0.5f + (j - (2 + k)) * w * 0.06f, y), alpha = f * on)
                tagAt(a.tag("tttabaqat0", 0, k), Offset(w * 0.5f, y - h * 0.07f), f * on, ink = ArtInk.Ivory)
                if (k > 0) drawLine(tones[k - 1], Offset(w * 0.5f, y - h * 0.17f), Offset(w * 0.5f, y - h * 0.1f), 1.5f * density, alpha = f * on)
            }
        },
        { p, f ->
            dim104(f)
            close97(Offset(w * 0.5f, h * 0.4f), w * 0.11f, a, "tttabaqat1", 2, 3, f * seg(p, 0.05f, 0.25f))
            qIcon("star", Offset(w * 0.22f, h * 0.62f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("book", Offset(w * 0.78f, h * 0.62f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("tttabaqat1", 0, 0), Offset(w * 0.22f, h * 0.74f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("tttabaqat1", 0, 1), Offset(w * 0.78f, h * 0.74f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f)
            val c = Offset(w * 0.5f, h * 0.22f)
            drawCircle(Roundel.Fills[3], w * 0.05f, c, alpha = f)
            drawCircle(Roundel.Rim, w * 0.05f, c, alpha = f, style = Stroke(1.5f * density))
            for (k in 0 until 4) {
                val at = Offset(w * (0.2f + 0.2f * k), h * 0.55f + (k % 2) * h * 0.1f)
                drawLine(Qn.GoldLight, c + Offset(0f, w * 0.05f), at, 1.2f * density, alpha = 0.5f * f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k))
                plain97(at, w * 0.055f, a, "tttabaqat2", k, f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k))
            }
        },
        { p, f ->
            plain97(Offset(w * 0.24f, h * 0.3f), w * 0.075f, a, "tttabaqat3", 2, f * seg(p, 0.05f, 0.25f))
            for (k in 0 until 4) qIcon("scroll", Offset(w * (0.5f + 0.1f * k), h * 0.24f), w * 0.05f, T104.Page, f * seg(p, 0.2f + 0.05f * k, 0.35f + 0.05f * k), a.t)
            balance12(Offset(w * 0.65f, h * 0.6f), w * 0.12f, 0.15f * (1f - seg(p, 0.5f, 0.8f)), Qn.GoldLight, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("tttabaqat3", 0, 0), Offset(w * 0.65f, h * 0.14f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("tttabaqat3", 0, 1), Offset(w * 0.65f, h * 0.7f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tt-hushirat ═════════════
internal fun DrawScope.tthushiratScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tthushirat", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study104(a) },
        { p, f ->
            dim104(f, 0.4f)
            for (k in 0 until 7) drawCircle(Qn.GoldLight, w * 0.02f, Offset(w * (0.2f + 0.1f * k), h * 0.4f), alpha = f * seg(p, 0.05f + 0.03f * k, 0.2f + 0.03f * k))
            for (k in 0 until 3) drawArc(Qn.GoldLight, -60f, 120f, false, Offset(w * 0.75f - w * 0.04f * (k + 1), h * 0.4f - h * 0.04f * (k + 1)), Size(w * 0.08f * (k + 1), h * 0.08f * (k + 1)), alpha = 0.5f * f * seg(p, 0.4f + 0.08f * k, 0.55f + 0.08f * k), style = Stroke(1.4f * density))
            tagAt(a.tag("tthushirat0", 0, 0), Offset(w * 0.4f, h * 0.28f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tthushirat0", 0, 1), Offset(w * 0.8f, h * 0.56f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.6f)
            qIcon("scroll", Offset(w * 0.3f, h * 0.4f), w * 0.1f, T104.Page, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("book", Offset(w * 0.7f, h * 0.4f), w * 0.1f, Color(0xFF8A8494), f * seg(p, 0.35f, 0.5f), a.t)
            drawLine(Qn.Ember, Offset(w * 0.62f, h * 0.32f), Offset(w * 0.78f, h * 0.48f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.55f, 0.7f))
            tagAt(a.tag("tthushirat1", 0, 0), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("tthushirat1", 0, 1), Offset(w * 0.7f, h * 0.56f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val ground = h * 0.86f
            daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB09868))
            plain97(Offset(w * 0.2f, h * 0.24f), w * 0.07f, a, "tthushirat2", 2, f * seg(p, 0.05f, 0.25f))
            val mixu = easeInOut(seg(p, 0.3f, 0.8f))
            for (k in 0 until 14) {
                val home = Offset(w * (0.1f + 0.06f * k), ground)
                val swirl = Offset(w * 0.5f + cos(k * 0.9f + a.t) * w * 0.25f, ground)
                person99(Offset(mix(home.x, swirl.x, mixu), ground), h * 0.08f, if (k % 2 == 0) Color(0xFFD8C8E8) else Color(0xFFC8E8D0), f, walk = a.t * 2f * mixu)
            }
            tagAt(a.tag("tthushirat2", 0, 0), Offset(w * 0.55f, h * 0.36f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("tthushirat2", 0, 1), Offset(w * 0.55f, h * 0.24f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f)
            for (k in 0 until 8) drawCircle(Color(0xFFC8E8D0), w * 0.018f, Offset(w * 0.3f + cos(k * 0.8f) * w * 0.08f, h * 0.45f + sin(k * 0.8f) * h * 0.06f), alpha = f * seg(p, 0.05f, 0.2f))
            qIcon("chain", Offset(w * 0.55f, h * 0.45f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("people", Offset(w * 0.78f, h * 0.45f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("tthushirat3", 0, 0), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tthushirat3", 0, 1), Offset(w * 0.55f, h * 0.6f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
            tagAt(a.tag("tthushirat3", 0, 2), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tt-naba ═════════════
internal fun DrawScope.ttnabaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ttnaba", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study104(a) },
        { p, f ->
            val ground = h * 0.86f
            nightSky(a.t, S97.Dusk, S97.DuskLow, ground, 0.6f)
            dunes97(ground, S97.SandDark, Color(0xFF6A5236))
            kaaba29(Offset(w * 0.8f, ground), w * 0.06f, f)
            for (k in 0 until 6) person99(Offset(w * (0.12f + 0.08f * k), ground), h * 0.08f, Color(0xFF8A7A6A), f, lean = 0.1f * sin(a.t * 2f + k))
            qIcon("scroll", Offset(w * 0.36f, h * 0.4f), w * 0.08f, T104.Page, f * seg(p, 0.3f, 0.45f), a.t)
            for (k in 0 until 4) drawCircle(Qn.GoldLight, w * 0.006f, Offset(w * (0.2f + 0.08f * k), h * 0.56f - h * 0.03f * sin(a.t * 3f + k)), alpha = f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("ttnaba0", 0, 0), Offset(w * 0.32f, ground - h * 0.18f), f * seg(p, 0.05f, 0.2f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("ttnaba0", 0, 1), Offset(w * 0.36f, h * 0.26f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.3f)
            plain97(Offset(w * 0.25f, h * 0.3f), w * 0.07f, a, "ttnaba1", 1, f * seg(p, 0.05f, 0.25f))
            softGlow(Offset(w * 0.5f, h * 0.72f), w * 0.2f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("ttnaba1", 0, 0), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f)
            plain97(Offset(w * 0.25f, h * 0.3f), w * 0.07f, a, "ttnaba2", 1, f * seg(p, 0.05f, 0.25f))
            val rise = easeOut(seg(p, 0.3f, 0.8f))
            drawRect(Color(0xFF3A2C22), Offset(0f, h * 0.78f), Size(w, h * 0.22f), alpha = f)
            for (k in 0 until 7) person99(Offset(w * (0.4f + 0.08f * k), h * 0.78f + h * 0.08f * (1f - rise)), h * 0.07f, Color(0xFFE8E4DA), f * rise)
            qIcon("hourglass", Offset(w * 0.65f, h * 0.36f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("ttnaba2", 0, 0), Offset(w * 0.65f, h * 0.24f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.6f)
            qIcon("book", Offset(w * 0.25f, h * 0.24f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("hourglass", Offset(w * 0.75f, h * 0.24f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            streams104(2, Offset(w * 0.5f, h * 0.6f), easeInOut(seg(p, 0.2f, 0.7f)), f)
            tagAt(a.tag("ttnaba3", 0, 1), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttnaba3", 0, 0), Offset(w * 0.72f, h * 0.7f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tt-zuwwijat ═════════════
internal fun DrawScope.ttzuwwijatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ttzuwwijat", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study104(a) },
        { p, f ->
            balance12(Offset(w * 0.5f, h * 0.5f), w * 0.12f, 0.1f * sin(a.t), Qn.GoldLight, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("ttzuwwijat0", 0, 1), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("ttzuwwijat0", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f)
            close97(Offset(w * 0.5f, h * 0.2f), w * 0.07f, a, "ttzuwwijat1", 2, 1, f * seg(p, 0.05f, 0.2f))
            val pair = easeInOut(seg(p, 0.3f, 0.7f))
            val cols = listOf(Qn.Jade, Qn.GoldLight, Qn.Rose)
            for (k in 0 until 3) for (j in 0 until 2) {
                val home = Offset(w * (0.15f + 0.35f * j + 0.12f * k), h * (0.45f + 0.12f * k))
                val to = Offset(w * (0.25f + 0.25f * k) + (j - 0.5f) * w * 0.06f, h * 0.55f)
                drawCircle(cols[k], w * 0.025f, Offset(mix(home.x, to.x, pair), mix(home.y, to.y, pair)), alpha = f * seg(p, 0.15f, 0.3f))
            }
            tagAt(a.tag("ttzuwwijat1", 0, 0), Offset(w * 0.3f, h * 0.72f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("ttzuwwijat1", 0, 1), Offset(w * 0.7f, h * 0.72f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            dim104(f)
            plain97(Offset(w * 0.5f, h * 0.18f), w * 0.07f, a, "ttzuwwijat2", 2, f * seg(p, 0.05f, 0.2f))
            val down = easeInOut(seg(p, 0.3f, 0.75f))
            for (k in 0 until 5) {
                val x = w * (0.2f + 0.15f * k)
                person99(Offset(x, h * 0.82f), h * 0.1f, Color(0xFFB8B4C0), f * seg(p, 0.15f, 0.3f))
                drawCircle(Color(0xFFE8F0FF), w * 0.015f, Offset(x, mix(h * 0.32f, h * 0.7f, down)), alpha = f * (1f - 0.7f * seg(p, 0.75f, 0.9f)))
            }
            tagAt(a.tag("ttzuwwijat2", 0, 0), Offset(w * 0.85f, h * 0.34f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttzuwwijat2", 0, 1), Offset(w * 0.5f, h * 0.92f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f)
            drawLine(Color(0xFF8A8494), Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.85f), 1.5f * density, alpha = f)
            val gather = easeInOut(seg(p, 0.15f, 0.6f))
            for (k in 0 until 6) {
                person99(Offset(mix(w * (0.05f + 0.15f * k), w * (0.15f + 0.05f * k), gather), h * 0.8f), h * 0.08f, Qn.Ember, f * if (k < 3) 1f else 0f)
                person99(Offset(mix(w * (0.95f - 0.15f * (k - 3)), w * (0.85f - 0.05f * (k - 3)), gather), h * 0.8f), h * 0.08f, Qn.Jade, f * if (k >= 3) 1f else 0f)
            }
            tagAt(a.tag("ttzuwwijat3", 0, 0), Offset(w * 0.25f, h * 0.5f), f * seg(p, 0.2f, 0.35f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("ttzuwwijat3", 0, 1), Offset(w * 0.75f, h * 0.5f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tt-fajr ═════════════
internal fun DrawScope.ttfajrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.8f
    film98(a, "ttfajr", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { dawn104(0f, ground) },
        { p, f ->
            dawn104(easeInOut(seg(p, 0f, 0.8f)) * 0.6f, ground)
            for (k in 0 until 5) pray12(Offset(w * (0.3f + 0.1f * k), ground + h * 0.08f), h * 0.07f, Color(0xFFF2EEE4), f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("ttfajr0", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ttfajr0", 0, 1), Offset(w * 0.5f, h * 0.94f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dawn104(0.6f + 0.4f * easeInOut(seg(p, 0f, 0.7f)), ground)
            drawCircle(Color(0xFFFFE8A0), w * 0.07f, Offset(w * 0.5f, ground - h * 0.06f * seg(p, 0.2f, 0.8f)), alpha = f)
            tagAt(a.tag("ttfajr1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            dawn104(0.15f, ground)
            for (k in 0 until 10) qIcon("star", Offset(w * (0.08f + 0.093f * k), h * (0.2f + 0.05f * (k % 3))), w * 0.035f, Qn.GoldLight, f * seg(p, 0.05f + 0.04f * k, 0.15f + 0.04f * k), a.t)
            kaaba29(Offset(w * 0.5f, ground), w * 0.1f, f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("ttfajr2", 0, 0), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttfajr2", 0, 1), Offset(w * 0.5f, ground + h * 0.08f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.7f)
            balance12(Offset(w * 0.5f, h * 0.62f), w * 0.15f, 0.15f * sin(a.t), Qn.GoldLight, f * seg(p, 0.2f, 0.4f))
            tagAt(a.tag("ttfajr3", 0, 1), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttfajr3", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tt-shaf ═════════════
internal fun DrawScope.ttshafScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.8f
    film98(a, "ttshaf", List(3) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { dawn104(0.3f, ground) },
        { p, f ->
            dim104(f, 0.5f)
            for (k in 0 until 2) drawCircle(Qn.GoldLight, w * 0.03f, Offset(w * (0.22f + 0.1f * k), h * 0.42f), alpha = f * seg(p, 0.1f, 0.25f))
            drawCircle(Qn.GoldLight, w * 0.04f, Offset(w * 0.72f, h * 0.42f), alpha = f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("ttshaf0", 0, 0), Offset(w * 0.27f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttshaf0", 0, 1), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("sun", Offset(w * 0.2f, h * 0.36f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            drawPath(Path().apply { moveTo(w * 0.35f, ground); quadraticBezierTo(w * 0.5f, ground - h * 0.3f, w * 0.65f, ground); close() }, Color(0xFF6A5A48), alpha = f * seg(p, 0.3f, 0.45f))
            for (r in 0 until 1) for (k in 0 until 4) pray12(Offset(w * (0.72f + 0.06f * k), ground + h * 0.08f), h * 0.06f, Color(0xFFF2EEE4), f * seg(p, 0.6f, 0.75f))
            tagAt(a.tag("ttshaf1", 0, 0), Offset(w * 0.2f, h * 0.5f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("ttshaf1", 0, 1), Offset(w * 0.5f, ground - h * 0.36f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttshaf1", 0, 2), Offset(w * 0.8f, ground - h * 0.06f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f, 0.6f)
            plain97(Offset(w * 0.2f, h * 0.22f), w * 0.065f, a, "ttshaf2", 1, f * seg(p, 0.05f, 0.25f))
            val c = Offset(w * 0.55f, h * 0.5f)
            drawCircle(Qn.GoldLight, w * 0.25f, c, alpha = 0.6f * f * seg(p, 0.35f, 0.6f), style = Stroke(2f * density))
            for ((k, ic) in listOf("sun", "mountain", "mat").withIndex()) {
                val ang = 2f * PI.toFloat() * k / 3f - PI.toFloat() / 2f
                qIcon(ic, c + Offset(cos(ang) * w * 0.12f, sin(ang) * w * 0.12f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.1f + 0.1f * k, 0.25f + 0.1f * k), a.t)
            }
            tagAt(a.tag("ttshaf2", 0, 0), c + Offset(0f, w * 0.3f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ tt-quran ═════════════
internal fun DrawScope.ttquranScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ttquran", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study104(a) },
        { p, f ->
            dim104(f, 0.5f)
            qIcon("lips", Offset(w * 0.28f, h * 0.36f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            val arc = seg(p, 0.3f, 0.7f)
            drawArc(Qn.GoldLight, 180f, 180f * arc, false, Offset(w * 0.28f, h * 0.2f), Size(w * 0.44f, h * 0.3f), alpha = 0.7f * f, style = Stroke(2f * density))
            softGlow(Offset(w * 0.72f, h * 0.36f), w * 0.08f, Qn.GoldLight, 0.7f * f * seg(p, 0.65f, 0.85f))
            tagAt(a.tag("ttquran0", 0, 0), Offset(w * 0.28f, h * 0.5f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttquran0", 0, 1), Offset(w * 0.72f, h * 0.5f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f)
            balance12(Offset(w * 0.5f, h * 0.6f), w * 0.15f, 0.12f * sin(a.t), Qn.GoldLight, f * seg(p, 0.2f, 0.4f))
            tagAt(a.tag("ttquran1", 0, 1), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttquran1", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.4f)
            openBook21(Offset(w * 0.3f, h * 0.36f), w * 0.18f, T104.Page, T104.Rule, f * seg(p, 0.05f, 0.2f), 0.3f)
            openBook21(Offset(w * 0.7f, h * 0.36f), w * 0.18f, T104.Page, T104.Rule, f * seg(p, 0.2f, 0.35f), 0.3f)
            drawLine(Qn.GoldLight, Offset(w * 0.42f, h * 0.36f), Offset(w * 0.58f, h * 0.36f), 2f * density, alpha = f * seg(p, 0.4f, 0.55f))
            qIcon("check", Offset(w * 0.5f, h * 0.18f), w * 0.07f, Qn.Jade, f * seg(p, 0.6f, 0.75f), a.t)
            tagAt(a.tag("ttquran2", 0, 0), Offset(w * 0.3f, h * 0.5f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttquran2", 0, 1), Offset(w * 0.7f, h * 0.5f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttquran2", 0, 2), Offset(w * 0.5f, h * 0.08f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            nightSky(a.t, S97.Night, S97.NightLow, h, 1f)
            val c = Offset(w * 0.5f, h * 0.36f)
            for (k in 0 until 8) {
                val ang = 2f * PI.toFloat() * k / 8f + a.t * 0.2f
                drawLine(Color(0xFFFFF4D0), c, c + Offset(cos(ang), sin(ang)) * (w * 0.1f * seg(p, 0.3f, 0.6f)), 1.6f * density, alpha = f)
            }
            drawCircle(Color(0xFFFFF4D0), w * 0.025f, c, alpha = f * seg(p, 0.25f, 0.4f))
            softGlow(c, w * 0.12f, Color(0xFFFFF4D0), 0.6f * f * seg(p, 0.3f, 0.6f))
            for (k in 0 until 3) drawArc(Qn.GoldLight, -60f, 120f, false, Offset(w * 0.2f - w * 0.04f * (k + 1), h * 0.62f - h * 0.04f * (k + 1)), Size(w * 0.08f * (k + 1), h * 0.08f * (k + 1)), alpha = 0.5f * f * seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k), style = Stroke(1.4f * density))
            tagAt(a.tag("ttquran3", 0, 0), Offset(w * 0.2f, h * 0.76f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttquran3", 0, 1), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tt-tin ═════════════
internal fun DrawScope.tttinScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "tttin", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { daySky12(Qn.DayTop, Color(0xFFF0E4C0), ground, Color(0xFF9A8A5E)) },
        { p, f ->
            tree104(Offset(w * 0.3f, ground), h * 0.3f, Color(0xFF5A8A4A), Color(0xFF7A3A5A), f, easeOut(seg(p, 0.05f, 0.4f)))
            tree104(Offset(w * 0.7f, ground), h * 0.3f, Color(0xFF8A9A6A), Color(0xFF3A4A2A), f, easeOut(seg(p, 0.35f, 0.7f)))
            tagAt(a.tag("tttin0", 0, 0), Offset(w * 0.3f, h * 0.18f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("tttin0", 0, 1), Offset(w * 0.7f, h * 0.18f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            hills104(ground, Color(0xFF7A8A5A), f)
            for (k in 0 until 7) tree104(Offset(w * (0.08f + 0.14f * k), ground - h * 0.06f * sin(k * 0.9f).coerceAtLeast(0f)), h * 0.12f, if (k % 2 == 0) Color(0xFF5A8A4A) else Color(0xFF8A9A6A), Color(0xFF7A3A5A), f * seg(p, 0.1f + 0.06f * k, 0.25f + 0.06f * k))
            tagAt(a.tag("tttin1", 0, 0), Offset(w * 0.3f, h * 0.3f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tttin1", 0, 1), Offset(w * 0.7f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            hills104(ground, Color(0xFF6A7A58), f)
            for (k in 0 until 5) {
                val c = Offset(w * (0.15f + 0.18f * k), h * (0.3f + 0.05f * (k % 2)))
                drawCircle(Qn.GoldLight, w * 0.012f, c, alpha = f * seg(p, 0.3f + 0.08f * k, 0.4f + 0.08f * k))
                softGlow(c, w * 0.04f, Qn.GoldLight, 0.5f * f * seg(p, 0.3f + 0.08f * k, 0.4f + 0.08f * k))
            }
            tagAt(a.tag("tttin2", 0, 0), Offset(w * 0.5f, ground - h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("tttin2", 0, 1), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            hills104(ground, Color(0xFF8A7A5A), f)
            drawRoundRect(Color(0xFFD8C8A0), Offset(w * 0.14f, ground - h * 0.16f), Size(w * 0.14f, h * 0.12f), CornerRadius(3f * density), alpha = f * seg(p, 0.05f, 0.2f))
            drawArc(Color(0xFFC8A048), 180f, 180f, true, Offset(w * 0.17f, ground - h * 0.21f), Size(w * 0.08f, h * 0.1f), alpha = f * seg(p, 0.05f, 0.2f))
            drawPath(Path().apply { moveTo(w * 0.38f, ground); lineTo(w * 0.5f, ground - h * 0.3f); lineTo(w * 0.62f, ground); close() }, Color(0xFF7A6050), alpha = f * seg(p, 0.35f, 0.5f))
            kaaba29(Offset(w * 0.82f, ground), w * 0.06f, f * seg(p, 0.65f, 0.8f))
            tagAt(a.tag("tttin3", 0, 0), Offset(w * 0.21f, ground - h * 0.3f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("tttin3", 0, 1), Offset(w * 0.5f, ground - h * 0.38f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
            tagAt(a.tag("tttin3", 0, 2), Offset(w * 0.82f, ground - h * 0.22f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tt-qadr ═════════════
internal fun DrawScope.ttqadrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.88f
    film98(a, "ttqadr", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { nightSky(a.t, S97.Night, S97.NightLow, ground, 1f); drawRect(Color(0xFF1A1610), Offset(0f, ground), Size(w, h - ground)) },
        { p, f ->
            qIcon("scroll", Offset(w * 0.5f, h * 0.14f), w * 0.1f, T104.Page, f * seg(p, 0.05f, 0.2f), a.t)
            val down = easeInOut(seg(p, 0.3f, 0.75f))
            softGlow(Offset(w * 0.5f, mix(h * 0.14f, h * 0.5f, down)), w * 0.1f, Qn.GoldLight, 0.7f * f * seg(p, 0.25f, 0.4f))
            drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.2f), Offset(w * 0.5f, mix(h * 0.2f, h * 0.5f, down)), 2f * density, alpha = 0.6f * f)
            drawArc(Color(0xFF6A7AB0), 180f, 180f, false, Offset(w * 0.05f, h * 0.48f), Size(w * 0.9f, h * 0.4f), alpha = f * seg(p, 0.4f, 0.6f), style = Stroke(2f * density))
            tagAt(a.tag("ttqadr0", 0, 2), Offset(w * 0.5f, h * 0.8f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("ttqadr0", 0, 0), Offset(w * 0.78f, h * 0.14f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttqadr0", 0, 1), Offset(w * 0.78f, h * 0.5f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 9) {
                val u = seg(p, 0.08f * k, 0.25f + 0.08f * k)
                val x = w * (0.1f + 0.1f * k)
                drawLine(Qn.GoldLight, Offset(x, h * 0.2f), Offset(x, mix(h * 0.2f, ground - h * 0.04f, u)), 1.4f * density, alpha = 0.6f * f * u)
                drawCircle(Qn.GoldLight, w * 0.008f, Offset(x, mix(h * 0.2f, ground - h * 0.04f, u)), alpha = f * u)
            }
            tagAt(a.tag("ttqadr1", 0, 0), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("scroll", Offset(w * 0.5f, h * 0.4f), w * 0.14f, T104.Page, f * seg(p, 0.05f, 0.25f), a.t)
            qIcon("pen", Offset(w * 0.68f, h * 0.36f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            for (k in 0 until 12) drawCircle(Qn.GoldLight, w * 0.008f, Offset(w * (0.2f + 0.055f * k), h * 0.66f), alpha = f * seg(p, 0.4f + 0.03f * k, 0.5f + 0.03f * k))
            tagAt(a.tag("ttqadr2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttqadr2", 0, 1), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.3f)
            qIcon("crescent", Offset(w * 0.22f, h * 0.36f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 40) drawCircle(Color(0xFF8A8494), w * 0.008f, Offset(w * (0.5f + 0.045f * (k % 10)), h * (0.26f + 0.05f * (k / 10))), alpha = f * seg(p, 0.3f + 0.008f * k, 0.4f + 0.008f * k))
            tagAt(a.tag("ttqadr3", 0, 0), Offset(w * 0.36f, h * 0.56f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttqadr3", 0, 1), Offset(w * 0.7f, h * 0.56f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tt-falaq ═════════════
internal fun DrawScope.ttfalaqScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.8f
    film98(a, "ttfalaq", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study104(a) },
        { p, f ->
            dim104(f, 0.4f)
            val brk = easeOut(seg(p, 0.4f, 0.7f))
            for (s in listOf(-1f, 1f)) qIcon("chain", Offset(w * 0.5f + s * w * (0.12f + 0.15f * brk), h * 0.5f), w * 0.07f, Qn.Ember, f * (1f - 0.6f * brk), a.t)
            softGlow(Offset(w * 0.5f, h * 0.74f), w * 0.2f, Qn.GoldLight, 0.6f * f * seg(p, 0.5f, 0.8f))
            tagAt(a.tag("ttfalaq0", 0, 1), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("ttfalaq0", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            dawn104(easeInOut(seg(p, 0.1f, 0.8f)), ground)
            val split = seg(p, 0.1f, 0.6f)
            drawLine(Color(0xFFFFE8B0), Offset(w * 0.5f * (1f - split), ground), Offset(w * 0.5f * (1f + split), ground), 3f * density, alpha = f)
            tagAt(a.tag("ttfalaq1", 0, 1), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("ttfalaq1", 0, 0), Offset(w * 0.5f, h * 0.4f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.7f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.42f), w * 0.1f, Color(0xFF8A8494), f * seg(p, 0.1f, 0.25f), a.t)
            drawLine(Qn.Ember, Offset(w * 0.4f, h * 0.32f), Offset(w * 0.6f, h * 0.52f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("ttfalaq2", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0f, 0.15f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("ttfalaq2", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.4f, 0.55f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            dim104(f, 0.4f)
            val icons = listOf("mat", "sun", "bed")
            for (k in 0 until 3) {
                val c = Offset(w * (0.22f + 0.28f * k), h * 0.38f)
                qIcon(icons[k], c, w * 0.08f, Qn.GoldLight, f * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k), a.t)
                softGlow(c, w * 0.08f, Qn.Jade, 0.35f * f * seg(p, 0.15f + 0.25f * k, 0.3f + 0.25f * k))
                tagAt(a.tag("ttfalaq3", 0, k), c + Offset(0f, h * 0.14f), f * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k), ink = ArtInk.Ivory)
            }
        },
    )
}
