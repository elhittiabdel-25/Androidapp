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
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/*
 * Wave 47 (2026-10-28): «رياض الصالحين»، «حقوق وصلات» (2) and «مكارم الأخلاق» (1). Six rights round a rosette and a
 * body that feels its smallest part; a sick man's room where the visitor walks through an orchard and the angels
 * stay till evening; two quarrelling tents and the one who carries only good words; the balance that good character
 * outweighs; gentle rain and the vessel that gentleness adorns. Allah is never drawn; the Prophet ﷺ is only light.
 */

private val Back47 = Color(0xE6181420)

// ═════════════ 1. six rights round a rosette (riyad-huquq) ═════════════

private object RightsC47 {
    val Tile = Color(0xFF14322E)
    val TileLine = Color(0xFF2C5A50)
    val Gold = Color(0xFFE8C46A)
    val Ivory = Color(0xFFF4EAD4)
    val Body = Color(0xFFE6DCC8)
    val Pain = Color(0xFFE0584A)
    val Warm = Color(0xFFF2A85A)
    val Brick = Color(0xFFC8925A)
    val Mortar = Color(0xFF6A4A32)
    val Person = Color(0xFFE6DCC8)
    val Stick = Color(0xFF8A6A44)
}

/**
 * Beats: sixfirst (a six-pointed rosette on a tiled ground; three of its points light, each with a sign: two who
 * greet, a plate, a word), sixrest (the other three light: a sneeze answered, a sick bed, a bier followed), onebody (a
 * body of small lights; one turns red and the warmth runs through all the rest), building (bricks set one on another,
 * each holding the next), restrain (one raises a stick at another; a third holds his arm back).
 */
internal fun DrawScope.sixrightsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sixfirst" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "sixrest" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "onebody" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "building" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "restrain" to Shot(Offset(w * 0.5f, h * 0.6f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { rightsTiles(a) }
}

private fun DrawScope.rightsTiles(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val s1 = a.b("sixfirst")
    val s2 = a.b("sixrest")
    val ob = a.b("onebody")
    val bd = a.b("building")
    val rs = a.b("restrain")
    drawRect(RightsC47.Tile)
    // a faint tiled ground of eight-pointed stars
    val step = w * 0.12f
    var y = step * 0.5f
    while (y < h + step) {
        var x = step * 0.5f
        while (x < w + step) {
            drawPath(starPath(Offset(x, y), step * 0.32f), RightsC47.TileLine, alpha = 0.55f, style = Stroke(1f * dp))
            x += step
        }
        y += step
    }
    // the rosette of six rights
    if (s1 > 0f) {
        val f = easeOut(seg(s1, 0f, BEAT_IN)) * (1f - seg(ob, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.5f)
        val r = h * 0.3f
        val hex = Path()
        for (k in 0 until 6) {
            val ang = -PI.toFloat() / 2f + k * PI.toFloat() / 3f
            val p = c + Offset(cos(ang) * r, sin(ang) * r)
            if (k == 0) hex.moveTo(p.x, p.y) else hex.lineTo(p.x, p.y)
        }
        hex.close()
        drawPath(hex, RightsC47.Gold, alpha = f * 0.6f, style = Stroke(1.6f * dp))
        drawCircle(RightsC47.Gold, r * 0.35f, c, alpha = f * 0.5f, style = Stroke(1.2f * dp))
        for (k in 0 until 6) {
            val ang = -PI.toFloat() / 2f + k * PI.toFloat() / 3f
            val p = c + Offset(cos(ang) * r, sin(ang) * r)
            val g = if (k < 3) seg(s1, 0.15f + 0.2f * k, 0.35f + 0.2f * k) else seg(s2, 0.1f + 0.25f * (k - 3), 0.3f + 0.25f * (k - 3))
            drawCircle(RightsC47.Tile, w * 0.05f, p, alpha = f)
            drawCircle(RightsC47.Gold, w * 0.05f, p, alpha = f, style = Stroke(1.6f * dp))
            if (g > 0f) {
                softGlow(p, w * 0.06f, RightsC47.Gold, 0.5f * f * g)
                rightSign47(k, p, w * 0.03f, f * g, t)
            }
            drawLine(RightsC47.Gold, c, p, 1f * dp, alpha = f * 0.35f * g)
        }
        softGlow(c, w * 0.05f, RightsC47.Gold, 0.4f * f * seg(s2, 0.6f, 1f))
    }
    // one body
    if (ob > 0f) {
        val f = easeOut(seg(ob, 0f, BEAT_IN)) * (1f - seg(bd, 0f, BEAT_OUT))
        val foot = Offset(w * 0.5f, h * 0.9f)
        val tall = h * 0.72f
        val pts = listOf(
            Offset(0f, -0.92f), Offset(0f, -0.78f), Offset(-0.12f, -0.7f), Offset(0.12f, -0.7f), Offset(0f, -0.62f),
            Offset(-0.2f, -0.55f), Offset(0.2f, -0.55f), Offset(0f, -0.48f), Offset(-0.26f, -0.42f), Offset(0.26f, -0.42f),
            Offset(0f, -0.36f), Offset(-0.07f, -0.24f), Offset(0.07f, -0.24f), Offset(-0.08f, -0.12f), Offset(0.08f, -0.12f),
            Offset(-0.09f, -0.02f), Offset(0.09f, -0.02f),
        )
        val sick = 9
        val pain = seg(ob, 0.2f, 0.35f)
        val spread = seg(ob, 0.35f, 0.9f)
        for ((k, q) in pts.withIndex()) {
            val p = foot + Offset(q.x * tall, q.y * tall)
            val d = (q - pts[sick]).getDistance()
            val warm = seg(spread * 1.3f - d, 0f, 0.15f)
            val col = if (k == sick) lerpColor(RightsC47.Body, RightsC47.Pain, pain) else lerpColor(RightsC47.Body, RightsC47.Warm, warm)
            drawCircle(col, w * 0.012f, p, alpha = f)
            if (k == sick && pain > 0f) softGlow(p, w * 0.04f, RightsC47.Pain, 0.6f * f * pain)
            else if (warm > 0f) softGlow(p, w * 0.025f, RightsC47.Warm, 0.35f * f * warm * (0.7f + 0.3f * sin(t * 4f + k)))
        }
        drawCircle(RightsC47.Body, tall * 0.07f, foot + Offset(0f, -0.92f * tall), alpha = f * 0.5f, style = Stroke(1.2f * dp))
    }
    // bricks that hold each other
    if (bd > 0f) {
        val f = easeOut(seg(bd, 0f, BEAT_IN)) * (1f - seg(rs, 0f, BEAT_OUT))
        val bw = w * 0.12f
        val bh = h * 0.07f
        var n = 0
        for (row in 0 until 5) {
            val cols = 5 - row
            for (k in 0 until cols) {
                val g = easeOut(seg(bd, 0.05f + 0.045f * n, 0.2f + 0.045f * n))
                n++
                val x = w * 0.5f + (k - (cols - 1) / 2f) * bw - bw / 2f
                val yy = h * 0.86f - (row + 1) * bh - h * 0.12f * (1f - g)
                drawRoundRect(RightsC47.Brick, Offset(x + 1.5f * dp, yy), Size(bw - 3f * dp, bh - 3f * dp), CornerRadius(2f * dp), alpha = f * g)
                drawRect(RightsC47.Mortar, Offset(x + 1.5f * dp, yy + bh - 4f * dp), Size(bw - 3f * dp, 1.5f * dp), alpha = f * g * 0.6f)
            }
        }
        softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.2f, RightsC47.Gold, 0.25f * f * seg(bd, 0.75f, 1f))
    }
    // holding back the one who wrongs
    if (rs > 0f) {
        val f = easeOut(seg(rs, 0f, 0.3f))
        val ground = h * 0.86f
        drawRect(Color(0xFF0E2420), Offset(0f, ground), Size(w, h - ground), alpha = f)
        val raise = easeOut(seg(rs, 0.1f, 0.35f))
        val hold = easeInOut(seg(rs, 0.4f, 0.7f))
        val a0 = Offset(w * 0.42f, ground)
        figure(a0, h * 0.34f, RightsC47.Person, f, arms = -0.6f * raise * (1f - hold))
        val handUp = a0 + Offset(w * 0.04f, -h * 0.36f)
        val stickEnd = handUp + Offset(-w * 0.06f * (1f - hold), -h * 0.12f * raise * (1f - 0.6f * hold))
        drawLine(RightsC47.Stick, handUp, stickEnd, 3f * dp, StrokeCap.Round, alpha = f * raise)
        figure(Offset(w * 0.22f, ground), h * 0.32f, RightsC47.Person, f * 0.85f, lean = -0.1f * raise * (1f - hold))
        val helper = Offset(w * (0.72f - 0.16f * hold), ground)
        figure(helper, h * 0.34f, RightsC47.Gold, f, walk = t * 6f * (1f - hold) * seg(rs, 0.3f, 0.4f), arms = hold)
        if (hold > 0.9f) softGlow(handUp, w * 0.04f, RightsC47.Gold, 0.5f * f)
    }
}

/** The sign of each of the six rights: greet, answer the invitation, advise, bless the sneeze, visit, follow the bier. */
private fun DrawScope.rightSign47(k: Int, c: Offset, s: Float, alpha: Float, t: Float) {
    val dp = density
    val col = RightsC47.Ivory
    when (k) {
        0 -> { figure(c + Offset(-s * 0.4f, s * 0.7f), s * 1.3f, col, alpha, arms = 0.6f); figure(c + Offset(s * 0.4f, s * 0.7f), s * 1.3f, col, alpha, arms = -0.2f) }
        1 -> cup12(c + Offset(0f, s * 0.4f), s * 1.4f, col, alpha, RightsC47.Warm)
        2 -> {
            drawRoundRect(col, Offset(c.x - s * 0.7f, c.y - s * 0.5f), Size(s * 1.4f, s * 0.8f), CornerRadius(s * 0.3f), alpha = alpha, style = Stroke(1.4f * dp))
            drawPath(Path().apply { moveTo(c.x - s * 0.2f, c.y + s * 0.3f); lineTo(c.x - s * 0.4f, c.y + s * 0.65f); lineTo(c.x + s * 0.05f, c.y + s * 0.3f) }, col, alpha = alpha, style = Stroke(1.4f * dp))
        }
        3 -> for (j in 0 until 6) {
            val ang = j * PI.toFloat() / 3f + t * 0.5f
            drawLine(col, c + Offset(cos(ang), sin(ang)) * (s * 0.25f), c + Offset(cos(ang), sin(ang)) * (s * 0.7f), 1.4f * dp, StrokeCap.Round, alpha = alpha)
        }
        4 -> {
            drawRect(col, Offset(c.x - s * 0.8f, c.y + s * 0.1f), Size(s * 1.6f, s * 0.2f), alpha = alpha)
            drawCircle(col, s * 0.22f, c + Offset(-s * 0.55f, -s * 0.08f), alpha = alpha)
            drawLine(col, c + Offset(-s * 0.8f, s * 0.3f), c + Offset(-s * 0.8f, s * 0.6f), 1.4f * dp, alpha = alpha)
            drawLine(col, c + Offset(s * 0.8f, s * 0.3f), c + Offset(s * 0.8f, s * 0.6f), 1.4f * dp, alpha = alpha)
        }
        else -> {
            drawRoundRect(col, Offset(c.x - s * 0.8f, c.y - s * 0.2f), Size(s * 1.6f, s * 0.35f), CornerRadius(s * 0.15f), alpha = alpha)
            for (j in 0 until 3) drawLine(col, c + Offset(-s * 0.6f + s * 0.6f * j, s * 0.15f), c + Offset(-s * 0.6f + s * 0.6f * j, s * 0.7f), 1.4f * dp, alpha = alpha)
        }
    }
}

// ═════════════ 2. the sick man's room (riyad-marid) ═════════════

private object SickC47 {
    val Night = Color(0xFF151827)
    val Wall = Color(0xFF3A3040)
    val WallLit = Color(0xFF6A5040)
    val Mat = Color(0xFF8A6A50)
    val Sick = Color(0xFFCFC2B0)
    val Visitor = Color(0xFFF2E6CE)
    val Flame = Color(0xFFF2B04A)
    val Leaf = Color(0xFF3E6A44)
    val Fruit = Color(0xFFE8925A)
    val Path = Color(0xFF8A7860)
    val Chain = Color(0xFFB0B4C0)
    val Morning = Color(0xFF8AB4D8)
    val Evening = Color(0xFFD8805A)
}

/**
 * Beats: visit (a dim room, a man lying ill on a mat by a small clay lamp; a visitor comes in and sits by him; the room
 * grows warm), orchard (the visitor's way to the sick man runs between fruit trees and he gathers as he goes), threeacts
 * (a bed, a bowl, a chain that breaks: visit, feed, free), angels (over the house, from morning till evening, rows of
 * cool white lights: «70 أَلْفَ»).
 */
internal fun DrawScope.sickroomScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "visit" to Shot(Offset(w * 0.45f, h * 0.6f), 1.2f),
        "orchard" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "threeacts" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "angels" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.15f) { sickNight(a) }
}

private fun DrawScope.sickNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val vs = a.b("visit")
    val oc = a.b("orchard")
    val ta = a.b("threeacts")
    val an = a.b("angels")
    val floor = h * 0.82f
    drawRect(SickC47.Night)
    // the room
    if (vs > 0f) {
        val f = easeOut(seg(vs, 0f, BEAT_IN)) * (1f - seg(oc, 0f, BEAT_OUT))
        val warm = seg(vs, 0.45f, 0.95f)
        drawRect(lerpColor(SickC47.Wall, SickC47.WallLit, warm), Offset(0f, 0f), Size(w, floor), alpha = f)
        drawRect(Color(0xFF2A2026), Offset(0f, floor), Size(w, h - floor), alpha = f)
        val lamp = Offset(w * 0.2f, floor - h * 0.02f)
        drawOval(Color(0xFF8A5A3A), Offset(lamp.x - w * 0.03f, lamp.y - h * 0.02f), Size(w * 0.06f, h * 0.03f), alpha = f)
        val fl = 1f + 0.08f * sin(t * 9f)
        drawOval(SickC47.Flame, Offset(lamp.x - w * 0.006f, lamp.y - h * 0.06f * fl), Size(w * 0.012f, h * 0.045f * fl), alpha = f)
        softGlow(lamp + Offset(0f, -h * 0.04f), w * (0.12f + 0.1f * warm), SickC47.Flame, (0.35f + 0.3f * warm) * f)
        drawRect(SickC47.Mat, Offset(w * 0.3f, floor - h * 0.03f), Size(w * 0.36f, h * 0.03f), alpha = f)
        // the sick man lying down
        drawLine(SickC47.Sick, Offset(w * 0.36f, floor - h * 0.06f), Offset(w * 0.6f, floor - h * 0.05f), h * 0.035f, StrokeCap.Round, alpha = f)
        drawCircle(SickC47.Sick, h * 0.03f, Offset(w * 0.34f, floor - h * 0.075f), alpha = f)
        // the door, and the visitor who comes in and sits by him
        val door = Offset(w * 0.86f, floor)
        door12(door, w * 0.1f, h * 0.36f, seg(vs, 0.1f, 0.3f), Color(0xFF2A2026), Color(0xFF5A4030), f, Color(0xFF8AA0C8))
        val come = easeInOut(seg(vs, 0.25f, 0.6f))
        if (come < 1f) figure(Offset(w * (0.86f - 0.2f * come), floor), h * 0.3f, SickC47.Visitor, f, walk = t * 5f)
        else sit12(Offset(w * 0.66f, floor), h * 0.3f, SickC47.Visitor, f, -1f)
    }
    // the way through the orchard
    if (oc > 0f) {
        val f = easeOut(seg(oc, 0f, BEAT_IN)) * (1f - seg(ta, 0f, BEAT_OUT))
        drawRect(Brush.verticalGradient(listOf(Color(0xFF1C2440), Color(0xFF3A4060)), 0f, floor), Offset.Zero, Size(w, floor), alpha = f)
        drawRect(Color(0xFF26302A), Offset(0f, floor), Size(w, h - floor), alpha = f)
        drawPath(Path().apply { moveTo(w * 0.04f, h); lineTo(w * 0.42f, floor); lineTo(w * 0.58f, floor); lineTo(w * 0.96f, h); close() }, SickC47.Path, alpha = f * 0.6f)
        house12(Offset(w * 0.5f, floor), w * 0.14f, Color(0xFF5A4A4A), Color(0xFF3A2A2A), f, 0.7f)
        for (k in 0 until 6) {
            val side = if (k % 2 == 0) -1f else 1f
            val depth = (k / 2) / 3f
            val foot = Offset(w * 0.5f + side * w * (0.36f - 0.16f * depth), floor + h * (0.14f - 0.12f * depth))
            val s = h * (0.34f - 0.12f * depth)
            drawLine(Color(0xFF5A4030), foot, foot + Offset(0f, -s * 0.6f), s * 0.06f, StrokeCap.Round, alpha = f)
            drawCircle(SickC47.Leaf, s * 0.28f, foot + Offset(0f, -s * 0.7f), alpha = f)
            for (j in 0 until 4) drawCircle(SickC47.Fruit, s * 0.035f, foot + Offset(s * (0.12f * (j - 1.5f)), -s * (0.62f + 0.08f * (j % 2))), alpha = f * (0.6f + 0.4f * sin(t + j)))
        }
        val go = easeInOut(seg(oc, 0.1f, 0.95f))
        val p = Offset(w * 0.5f, h * 0.98f - (h * 0.98f - floor) * go)
        figure(p, h * (0.3f - 0.18f * go), SickC47.Visitor, f, walk = t * 5f, arms = 0.5f * sin(t * 2f).coerceAtLeast(0f))
    }
    // visit the sick, feed the hungry, free the captive
    if (ta > 0f) {
        val f = easeOut(seg(ta, 0f, BEAT_IN)) * (1f - seg(an, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val c = Offset(w * (0.22f + 0.28f * k), h * 0.5f)
            val g = easeOut(seg(ta, 0.1f + 0.25f * k, 0.3f + 0.25f * k))
            drawCircle(Color(0xFF2A2E44), w * 0.1f, c, alpha = f * g)
            softGlow(c, w * 0.1f, ArtInk.Gold, 0.25f * f * g)
            when (k) {
                0 -> {
                    drawRect(SickC47.Mat, Offset(c.x - w * 0.06f, c.y), Size(w * 0.12f, h * 0.025f), alpha = f * g)
                    drawLine(SickC47.Sick, c + Offset(-w * 0.04f, -h * 0.015f), c + Offset(w * 0.05f, -h * 0.01f), h * 0.025f, StrokeCap.Round, alpha = f * g)
                    drawCircle(SickC47.Sick, h * 0.02f, c + Offset(-w * 0.055f, -h * 0.025f), alpha = f * g)
                }
                1 -> {
                    cup12(c + Offset(0f, h * 0.04f), w * 0.08f, Color(0xFFD8B080), f * g, Color(0xFFE8A050))
                    for (j in 0 until 3) {
                        val u = fract(t * 0.3f + j / 3f)
                        drawCircle(Color.White, w * 0.01f * (0.5f + u), c + Offset(w * 0.01f * sin(t + j), -h * (0.02f + 0.08f * u)), alpha = f * g * 0.4f * (1f - u))
                    }
                }
                else -> {
                    val br = seg(ta, 0.8f, 0.95f)
                    for (j in 0 until 4) {
                        val gap = if (j >= 2) w * 0.03f * br else 0f
                        drawOval(SickC47.Chain, Offset(c.x - w * 0.07f + w * 0.035f * j + gap, c.y - h * 0.02f), Size(w * 0.045f, h * 0.04f), alpha = f * g, style = Stroke(2.2f * dp))
                    }
                }
            }
        }
    }
    // the angels from morning till evening
    if (an > 0f) {
        val f = easeOut(seg(an, 0f, 0.3f))
        val day = seg(an, 0.1f, 0.9f)
        drawRect(Brush.verticalGradient(listOf(lerpColor(SickC47.Morning, Color(0xFF3A3058), day), lerpColor(Color(0xFFF2E4C4), SickC47.Evening, day)), 0f, floor), Offset.Zero, Size(w, floor), alpha = f)
        drawRect(Color(0xFF4A4036), Offset(0f, floor), Size(w, h - floor), alpha = f)
        sun12(Offset(w * (0.15f + 0.7f * day), h * (0.3f - 0.18f * sin(PI.toFloat() * day))), w * 0.03f, f * (1f - seg(an, 0.85f, 1f)))
        house12(Offset(w * 0.5f, floor), w * 0.26f, Color(0xFF8A7A6A), Color(0xFF5A4A3A), f, 0.6f)
        for (row in 0 until 3) for (k in 0 until 9) {
            val g = seg(an, 0.1f + 0.05f * row + 0.02f * k, 0.3f + 0.05f * row + 0.02f * k)
            val c = Offset(w * (0.14f + 0.09f * k), h * (0.14f + 0.08f * row) + h * 0.008f * sin(t * 1.5f + k + row))
            angelLight26(c, w * 0.006f, t + k, f * g * 0.85f)
        }
        tagAt(a.tag("angels"), Offset(w * 0.5f, h * 0.44f), f * seg(an, 0.3f, 0.5f), back = Back47, ink = ArtInk.Ivory)
    }
}

// ═════════════ 3. between two tents (riyad-islah) ═════════════

private object MendC47 {
    val Sky = Color(0xFFB4D2E6)
    val SkyLow = Color(0xFFF4E4C4)
    val Sand = Color(0xFFD8C094)
    val TentA = Color(0xFF7A4A3A)
    val TentB = Color(0xFF3A5A6A)
    val Grey = Color(0xFF8A8A96)
    val Gold = Color(0xFFE8B84A)
    val Person = Color(0xFF3A3040)
    val Crack = Color(0xFF6A5236)
    val Cloud = Color(0xFF5A5A66)
    val Road = Color(0xFFC8AE84)
    val Green = Color(0xFF5AA06A)
}

/**
 * Beats: whispers (many small grey clouds of talk drift over a camp; one among them turns gold), justice (a crack in
 * the ground between two who turned their backs; one stands in the middle with a level balance and the crack closes),
 * mendword (one carries a green leaf from one tent to the other and back; the two tents turn their doors to each
 * other), village (a map: two quarters of a village with a dark cloud between; a light ﷺ comes along the road and the
 * cloud clears), debt (a pile of coins between two; half of it slides back to the one who owes).
 */
internal fun DrawScope.reconcileScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "whispers" to Shot(Offset(w * 0.5f, h * 0.4f), 1.1f),
        "justice" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "mendword" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "village" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "debt" to Shot(Offset(w * 0.5f, h * 0.6f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { twoTents(a) }
}

private fun DrawScope.tent47(foot: Offset, s: Float, color: Color, alpha: Float, facing: Float) {
    if (alpha <= 0.01f) return
    drawPath(Path().apply {
        moveTo(foot.x - s * 0.5f, foot.y); lineTo(foot.x - s * 0.1f, foot.y - s * 0.55f); lineTo(foot.x + s * 0.1f, foot.y - s * 0.6f)
        lineTo(foot.x + s * 0.5f, foot.y); close()
    }, color, alpha = alpha)
    val door = foot + Offset(facing * s * 0.22f, 0f)
    drawPath(Path().apply { moveTo(door.x - s * 0.08f, foot.y); lineTo(door.x, foot.y - s * 0.28f); lineTo(door.x + s * 0.08f, foot.y); close() }, Color(0xFF1E1A16), alpha = alpha)
}

private fun DrawScope.twoTents(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val wh = a.b("whispers")
    val js = a.b("justice")
    val gw = a.b("mendword")
    val vl = a.b("village")
    val db = a.b("debt")
    val ground = h * 0.8f
    daySky12(MendC47.Sky, MendC47.SkyLow, ground, MendC47.Sand)
    // talk in secret: grey whispers drift between the tents; one, the word that makes peace, turns gold
    if (wh > 0f) {
        val f = easeOut(seg(wh, 0f, BEAT_IN)) * (1f - seg(js, 0f, BEAT_OUT))
        tent47(Offset(w * 0.24f, ground), w * 0.2f, MendC47.TentA, f, 1f)
        tent47(Offset(w * 0.76f, ground), w * 0.2f, MendC47.TentB, f, -1f)
        var goldAt = Offset.Zero
        for (k in 0 until 7) {
            val r = fract(sin(k * 7.3f) * 4375.5f)
            val c = Offset(w * (0.3f + 0.067f * k) + w * 0.015f * sin(t * 0.6f + k), h * (0.42f + 0.16f * r))
            val gold = k == 3
            val col = if (gold) lerpColor(MendC47.Grey, MendC47.Gold, seg(wh, 0.5f, 0.8f)) else MendC47.Grey
            val al = f * seg(wh, 0.04f * k, 0.15f + 0.04f * k) * (if (gold) 1f else 0.85f - 0.35f * seg(wh, 0.6f, 0.9f))
            bubble47(c, w * 0.06f, h * 0.06f, col, al)
            if (!gold) for (d in 0 until 3) drawCircle(Color(0xFFF4F0E8), 1.6f * dp, c + Offset(w * 0.012f * (d - 1), 0f), alpha = al)
            if (gold) { goldAt = c; softGlow(c, w * 0.07f, MendC47.Gold, 0.6f * f * seg(wh, 0.5f, 0.8f)) }
        }
        tagAt(a.tag("whispers", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(wh, 0.25f, 0.45f), ink = Color(0xFFE0E0E8))
        tagAt(a.tag("whispers", 0, 1), goldAt + Offset(0f, h * 0.11f), f * seg(wh, 0.6f, 0.85f), ink = ArtInk.Gold)
    }
    // a just word between two: the crack closes; the balance rests in his hands
    if (js > 0f) {
        val f = easeOut(seg(js, 0f, BEAT_IN)) * (1f - seg(gw, 0f, BEAT_OUT))
        val close = easeInOut(seg(js, 0.4f, 0.85f))
        val gap = w * 0.02f * (1f - close)
        var x = w * 0.5f
        val crack = Path().apply {
            moveTo(w * 0.5f, ground)
            for (k in 1..6) { x += (if (k % 2 == 0) 1f else -1f) * gap; lineTo(x, ground + h * 0.03f * k) }
        }
        drawPath(crack, MendC47.Crack, alpha = f * (1f - close), style = Stroke(3f * dp))
        figure(Offset(w * 0.25f, ground), h * 0.3f, MendC47.Person, f, lean = 0.12f * (1f - close), arms = 0.6f * close)
        figure(Offset(w * 0.75f, ground), h * 0.3f, MendC47.Person, f, lean = -0.12f * (1f - close), arms = -0.4f * (1f - close))
        figure(Offset(w * 0.5f, ground), h * 0.3f, MendC47.Gold, f * seg(js, 0.15f, 0.35f), arms = 1f)
        balance12(Offset(w * 0.5f, ground - h * 0.3f), h * 0.12f, 0.4f * sin(t * 2f) * (1f - close), MendC47.Gold, f * seg(js, 0.25f, 0.45f))
        if (close > 0.3f) softGlow(Offset(w * 0.5f, ground - h * 0.18f), h * 0.2f, MendC47.Gold, 0.45f * f * close)
        tagAt(a.tag("justice"), Offset(w * 0.5f, ground - h * 0.5f), f * seg(js, 0.55f, 0.8f), ink = ArtInk.Gold)
    }
    // the one who carries only good: a glowing word goes from tent to tent, and each lights as it arrives
    if (gw > 0f) {
        val f = easeOut(seg(gw, 0f, BEAT_IN)) * (1f - seg(vl, 0f, BEAT_OUT))
        val turn = easeInOut(seg(gw, 0.6f, 0.9f))
        tent47(Offset(w * 0.24f, ground), w * 0.18f, MendC47.TentA, f, -1f + 2f * turn)
        tent47(Offset(w * 0.76f, ground), w * 0.18f, MendC47.TentB, f, 1f - 2f * turn)
        val trip = fract(gw * 1.6f)
        val leg = (gw * 1.6f).toInt()
        val dir = if (leg % 2 == 0) 1f else -1f
        val x0 = if (dir > 0f) mix(w * 0.34f, w * 0.66f, trip) else mix(w * 0.66f, w * 0.34f, trip)
        val x = if (gw > 0.75f) mix(x0, w * 0.5f, easeOut(seg(gw, 0.75f, 0.85f))) else x0
        val walker = f
        figure(Offset(x, ground), h * 0.26f, MendC47.Person, walker, walk = t * 6f, arms = 0.6f)
        val b = Offset(x + dir * w * 0.05f, ground - h * 0.33f)
        softGlow(b, w * 0.05f, MendC47.Gold, 0.5f * walker)
        bubble47(b, w * 0.07f, h * 0.06f, MendC47.Gold, walker)
        tagAt(a.tag("mendword"), b + Offset(0f, -h * 0.09f), walker * seg(gw, 0.1f, 0.25f), ink = ArtInk.Gold)
        for (k in 0 until 2) {
            val reached = if (k == 1) seg(gw, 0.3f, 0.4f) else seg(gw, 0.62f, 0.72f)
            softGlow(Offset(w * (0.24f + 0.52f * k), ground - h * 0.05f), w * 0.06f, MendC47.Gold, 0.55f * f * max(reached, turn))
        }
    }
    // he went himself to make peace: a light comes up to two groups at odds, and they draw together
    if (vl > 0f) {
        val f = easeOut(seg(vl, 0f, BEAT_IN)) * (1f - seg(db, 0f, BEAT_OUT))
        drawRect(Color(0xFFEAD9B4), alpha = f)
        val clear = easeInOut(seg(vl, 0.6f, 0.9f))
        for (side in listOf(-1f, 1f)) for (k in 0 until 4) {
            val c = Offset(w * (0.5f + side * (0.3f - 0.08f * clear) + side * 0.06f * (k % 2)), h * (0.42f + 0.12f * (k / 2)))
            nameRoundel(c, w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f)
            if (clear > 0f) softGlow(c, w * 0.05f, MendC47.Gold, 0.35f * f * clear)
        }
        for (k in 0 until 3) {
            val sx = w * (0.44f + 0.06f * k) + w * 0.03f * sin(t * 3f + k)
            drawCircle(Color(0xFF7A6A58), w * 0.008f, Offset(sx, h * (0.46f + 0.04f * sin(t * 4f + k * 2f))), alpha = f * (1f - clear))
        }
        cloud38(Offset(w * 0.5f, h * 0.3f), w * 0.09f, MendC47.Cloud, f * (1f - clear))
        val go = easeInOut(seg(vl, 0.15f, 0.6f))
        val c = Offset(w * 0.5f, h * (0.9f - 0.3f * go))
        sirajMunir(c, w * 0.012f, f * seg(vl, 0.1f, 0.25f), t, nameAt = c + Offset(w * 0.16f, h * 0.04f))
    }
    // the debt: he swore not to do the good; the oath is struck out and half the pile goes back to the debtor
    if (db > 0f) {
        val f = easeOut(seg(db, 0f, 0.3f))
        val owes = Offset(w * 0.28f, ground - h * 0.16f)
        val owed = Offset(w * 0.72f, ground - h * 0.16f)
        nameRoundel(owes, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(owed, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        tagAt(a.tag("debt", 0, 1), owes + Offset(0f, h * 0.13f), f, ink = Color(0xFFF0ECE4))
        tagAt(a.tag("debt", 0, 0), owed + Offset(0f, h * 0.13f), f, ink = Color(0xFFF0ECE4))
        val oath = owed + Offset(-w * 0.08f, -h * 0.2f)
        val oathOn = seg(db, 0.12f, 0.3f)
        bubble47(oath, w * 0.3f, h * 0.09f, Color(0xFF3A3040), f * oathOn)
        tagAt(a.tag("debt", 0, 2), oath, f * oathOn, back = Color(0x00000000), ink = Color(0xFFF0ECE4))
        cross14(oath + Offset(w * 0.15f, -h * 0.045f), h * 0.035f, seg(db, 0.5f, 0.62f))
        val back = easeInOut(seg(db, 0.6f, 0.92f))
        for (k in 0 until 8) {
            val half = k >= 4
            val base = Offset(w * 0.5f, ground - h * 0.03f - h * 0.025f * (k % 4))
            val c = if (half) base + Offset(-w * 0.13f * back, 0f) else base
            coin12(c, w * 0.022f, f)
        }
        sirajMunir(Offset(w * 0.5f, h * 0.2f), w * 0.01f, f * seg(db, 0.3f, 0.48f), t, nameAt = Offset(w * 0.5f, h * 0.08f))
    }
}

/** A rounded speech bubble with a small tail, centred at [c]. */
private fun DrawScope.bubble47(c: Offset, bw: Float, bh: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, topLeft = c - Offset(bw / 2f, bh / 2f), size = Size(bw, bh), cornerRadius = CornerRadius(bh * 0.45f), alpha = alpha)
    drawPath(Path().apply { moveTo(c.x - bw * 0.1f, c.y + bh * 0.45f); lineTo(c.x - bw * 0.2f, c.y + bh * 0.85f); lineTo(c.x + bw * 0.05f, c.y + bh * 0.45f); close() }, color, alpha = alpha)
}

// ═════════════ 4. the heaviest in the balance (riyad-khuluq) ═════════════

private object WeighC47 {
    val Sky = Color(0xFF14182C)
    val SkyLow = Color(0xFF332C50)
    val Beam = Color(0xFFD8B46A)
    val Gold = Color(0xFFF2C45A)
    val Grey = Color(0xFF7A7A8A)
    val Gate = Color(0xFF5A4A3A)
    val Person = Color(0xFFE6DCC8)
    val Garden = Color(0xFF2E4A34)
    val House = Color(0xFFD8C4A0)
}

/**
 * Beats: heaviest (a great balance under the stars: heavy things on one pan; a small glowing token, «حُسْنِ الْخُلُقِ»,
 * on the other brings it down), twokeys (a gate with two keyholes; two keys turn: «تَقْوَى اللَّهِ», «وَحُسْنُ
 * الْخُلُقِ»), threetraits (three signs light in turn: a warm glow round a head, a hand giving bread, a hand holding a
 * thorny branch away: «طَلَاقَةُ الْوَجْهِ», «وَبَذْلُ الْمَعْرُوفِ», «وَكَفُّ الْأَذَى»), fastrank (one of good manner rises to
 * stand level with one in prayer and a crescent of fasting), twohouses (a garden on a slope: a house at its top, a
 * house at its edge).
 */
internal fun DrawScope.heavyscaleScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "heaviest" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "twokeys" to Shot(Offset(w * 0.5f, h * 0.5f), 1.3f),
        "threetraits" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "fastrank" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "twohouses" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.15f) { weighNight(a) }
}

private fun DrawScope.weighNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val hv = a.b("heaviest")
    val tk = a.b("twokeys")
    val tt = a.b("threetraits")
    val rk = a.b("fastrank")
    val th = a.b("twohouses")
    val ground = h * 0.86f
    nightSky(t, WeighC47.Sky, WeighC47.SkyLow, ground, starAlpha = 0.8f)
    drawRect(Color(0xFF1C1A28), Offset(0f, ground), Size(w, h - ground))
    // the balance
    if (hv > 0f) {
        val f = easeOut(seg(hv, 0f, BEAT_IN)) * (1f - seg(tk, 0f, BEAT_OUT))
        val tilt = mix(0.7f, -0.8f, easeInOut(seg(hv, 0.45f, 0.85f)))
        val foot = Offset(w * 0.5f, ground)
        val s = h * 0.66f
        balance12(foot, s, tilt, WeighC47.Beam, f)
        val (lp, rp) = balancePans12(foot, s, tilt)
        for (k in 0 until 5) drawRoundRect(WeighC47.Grey, Offset(rp.x - w * 0.05f + w * 0.02f * (k % 3), rp.y - h * 0.035f - h * 0.03f * (k / 3)), Size(w * 0.035f, h * 0.03f), CornerRadius(2f * dp), alpha = f * seg(hv, 0.05f + 0.05f * k, 0.2f + 0.05f * k))
        val tok = seg(hv, 0.3f, 0.45f)
        softGlow(lp + Offset(0f, -h * 0.03f), w * 0.04f, WeighC47.Gold, 0.9f * f * tok)
        drawCircle(WeighC47.Gold, w * 0.012f, lp + Offset(0f, -h * 0.03f), alpha = f * tok)
        tagAt(a.tag("heaviest"), lp + Offset(0f, h * 0.12f), f * seg(hv, 0.5f, 0.7f), back = Back47, ink = ArtInk.Gold)
    }
    // the gate of two keys
    if (tk > 0f) {
        val f = easeOut(seg(tk, 0f, BEAT_IN)) * (1f - seg(tt, 0f, BEAT_OUT))
        val sill = Offset(w * 0.5f, ground)
        val open = easeInOut(seg(tk, 0.65f, 0.95f))
        door12(sill, w * 0.2f, h * 0.6f, open, WeighC47.Gate, Color(0xFF3A2A1E), f)
        for (k in 0 until 2) {
            val c = Offset(w * (0.42f + 0.16f * k), ground - h * 0.3f)
            val turn = seg(tk, 0.25f + 0.2f * k, 0.4f + 0.2f * k)
            key21(c + Offset((k * 2 - 1) * w * 0.06f * (1f - seg(tk, 0.1f + 0.2f * k, 0.25f + 0.2f * k)), 0f), w * 0.05f, WeighC47.Gold, f, 90f * turn)
            tagAt(a.tag("twokeys", 0, k), Offset(w * (0.24f + 0.52f * k), ground - h * 0.42f), f * seg(tk, 0.15f + 0.2f * k, 0.3f + 0.2f * k), back = Back47, ink = ArtInk.Gold)
        }
    }
    // three signs of good character
    if (tt > 0f) {
        val f = easeOut(seg(tt, 0f, BEAT_IN)) * (1f - seg(rk, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val c = Offset(w * (0.2f + 0.3f * k), h * 0.5f)
            val g = easeOut(seg(tt, 0.1f + 0.25f * k, 0.3f + 0.25f * k))
            when (k) {
                0 -> { softGlow(c, w * 0.08f, WeighC47.Gold, 0.7f * f * g); drawCircle(WeighC47.Person, w * 0.035f, c, alpha = f * g); drawLine(WeighC47.Person, c + Offset(0f, w * 0.035f), c + Offset(0f, h * 0.18f), w * 0.02f, StrokeCap.Round, alpha = f * g) }
                1 -> {
                    drawPath(Path().apply { moveTo(c.x - w * 0.08f, c.y + h * 0.06f); quadraticTo(c.x - w * 0.02f, c.y + h * 0.04f, c.x + w * 0.04f, c.y + h * 0.05f); lineTo(c.x + w * 0.04f, c.y + h * 0.09f); lineTo(c.x - w * 0.08f, c.y + h * 0.1f); close() }, WeighC47.Person, alpha = f * g)
                    drawOval(Color(0xFFD8A65A), Offset(c.x - w * 0.02f, c.y + h * 0.0f), Size(w * 0.07f, h * 0.05f), alpha = f * g)
                }
                else -> {
                    val away = seg(tt, 0.65f, 0.9f)
                    rotate(-40f * away, c + Offset(w * 0.04f, h * 0.08f)) {
                        drawLine(Color(0xFF6A5040), c + Offset(-w * 0.06f, h * 0.02f), c + Offset(w * 0.04f, h * 0.08f), 2.5f * dp, StrokeCap.Round, alpha = f * g)
                        for (j in 0 until 4) drawLine(Color(0xFF6A5040), c + Offset(-w * 0.04f + w * 0.022f * j, h * (0.03f + 0.012f * j)), c + Offset(-w * 0.05f + w * 0.022f * j, h * (0.0f + 0.012f * j)), 1.5f * dp, alpha = f * g)
                    }
                    drawCircle(WeighC47.Person, w * 0.02f, c + Offset(w * 0.05f, h * 0.09f), alpha = f * g)
                }
            }
            tagAt(a.tag("threetraits", 0, k), c + Offset(0f, h * 0.24f), f * g, back = Back47, ink = ArtInk.Gold)
        }
    }
    // the rank of the one who fasts and prays
    if (rk > 0f) {
        val f = easeOut(seg(rk, 0f, BEAT_IN)) * (1f - seg(th, 0f, BEAT_OUT))
        val lift = easeInOut(seg(rk, 0.2f, 0.8f))
        val step = ground - h * 0.3f
        drawRect(Color(0xFF3A3450), Offset(w * 0.55f, step), Size(w * 0.36f, ground - step), alpha = f)
        pray12(Offset(w * 0.66f, step), h * 0.3f, WeighC47.Person, f)
        crescent12(Offset(w * 0.82f, step - h * 0.2f), w * 0.03f, WeighC47.Gold, WeighC47.Sky, f)
        val y = ground - (ground - step) * lift
        drawRect(Color(0xFF3A3450), Offset(w * 0.18f, y), Size(w * 0.2f, ground - y), alpha = f)
        figure(Offset(w * 0.28f, y), h * 0.3f, WeighC47.Gold, f, arms = 0.4f)
        if (lift > 0.95f) drawLine(WeighC47.Gold, Offset(w * 0.18f, step), Offset(w * 0.91f, step), 1.5f * dp, alpha = f * 0.6f)
    }
    // a house at the top, a house at the edge
    if (th > 0f) {
        val f = easeOut(seg(th, 0f, 0.3f))
        drawPath(Path().apply { moveTo(0f, ground); quadraticTo(w * 0.5f, h * 0.1f, w, ground); close() }, WeighC47.Garden, alpha = f)
        for (k in 0 until 7) palm12(Offset(w * (0.2f + 0.1f * k), ground - h * (0.18f + 0.3f * sin(PI.toFloat() * (0.2f + 0.1f * k)))), h * 0.14f, Color(0xFF6A4A32), Color(0xFF4A7A4A), f * 0.8f, t + k)
        val g1 = easeOut(seg(th, 0.2f, 0.45f))
        house12(Offset(w * 0.5f, h * 0.36f), w * 0.1f, WeighC47.House, Color(0xFF8A6A44), f * g1, 1f)
        softGlow(Offset(w * 0.5f, h * 0.3f), w * 0.1f, WeighC47.Gold, 0.5f * f * g1)
        val g2 = easeOut(seg(th, 0.55f, 0.8f))
        house12(Offset(w * 0.12f, ground), w * 0.09f, WeighC47.House, Color(0xFF8A6A44), f * g2, 1f)
    }
}

// ═════════════ 5. gentle rain (riyad-rifq) ═════════════

private object GentleC47 {
    val Sky = Color(0xFFBFD8E6)
    val SkyLow = Color(0xFFEFE8D4)
    val Ground = Color(0xFF8A7454)
    val Grass = Color(0xFF5A9A5A)
    val Leaf = Color(0xFF3E7A46)
    val Trunk = Color(0xFF6A4A32)
    val Rain = Color(0xFF6A9AC0)
    val Flood = Color(0xFF6A5A48)
    val Floor = Color(0xFFD8C8A8)
    val Person = Color(0xFF3A3040)
    val Water = Color(0xFF8AC0E0)
    val Vessel = Color(0xFFC89A6A)
    val Pattern = Color(0xFF2E6A8A)
    val Sign = Color(0xFF8A6A44)
}

/**
 * Beats: twotraits (two young trees grow slowly, side by side: «الْحِلْمُ», «وَالْأَنَاةُ»), rainvsflood (on the left a
 * soft rain and a field that greens; on the right a torrent that carries the soil away), bucket (a mosque floor; plain roundels
 * of the companions rush forward and stop; a bucket of water is poured over the spot), signposts (two signposts at a crossroads:
 * «يَسِّرُوا», «وَبَشِّرُوا»), zanahu (a plain jar: as gentleness touches it a pattern blooms over it; taken away, it
 * cracks).
 */
internal fun DrawScope.gentlerainScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twotraits" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "rainvsflood" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "bucket" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "signposts" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "zanahu" to Shot(Offset(w * 0.5f, h * 0.5f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { gentleDay(a) }
}

private fun DrawScope.gentleDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tw = a.b("twotraits")
    val rf = a.b("rainvsflood")
    val bk = a.b("bucket")
    val sp = a.b("signposts")
    val ad = a.b("zanahu")
    val ground = h * 0.82f
    daySky12(GentleC47.Sky, GentleC47.SkyLow, ground, GentleC47.Ground)
    // two calm trees
    if (tw > 0f) {
        val f = easeOut(seg(tw, 0f, BEAT_IN)) * (1f - seg(rf, 0f, BEAT_OUT))
        drawRect(GentleC47.Grass, Offset(0f, ground), Size(w, h * 0.03f), alpha = f)
        for (k in 0 until 2) {
            val base = Offset(w * (0.36f + 0.28f * k), ground)
            val g = easeOut(seg(tw, 0.1f + 0.2f * k, 0.8f))
            drawLine(GentleC47.Trunk, base, base + Offset(0f, -h * 0.3f * g), w * 0.018f, StrokeCap.Round, alpha = f)
            drawCircle(GentleC47.Leaf, w * 0.1f * g, base + Offset(w * 0.005f * sin(t), -h * 0.38f * g), alpha = f)
            tagAt(a.tag("twotraits", 0, k), base + Offset(0f, h * 0.08f), f * seg(tw, 0.2f + 0.2f * k, 0.4f + 0.2f * k), back = Back47, ink = ArtInk.Gold)
        }
    }
    // a soft rain, a hard torrent
    if (rf > 0f) {
        val f = easeOut(seg(rf, 0f, BEAT_IN)) * (1f - seg(bk, 0f, BEAT_OUT))
        cloud38(Offset(w * 0.25f, h * 0.18f), w * 0.1f, Color(0xFFE4E8F0), f)
        rain12(w * 0.1f, w * 0.42f, h * 0.24f, ground, t * 0.6f, GentleC47.Rain, f * 0.7f, 18)
        val green = seg(rf, 0.3f, 0.9f)
        for (k in 0 until 10) {
            val x = w * (0.08f + 0.035f * k)
            drawLine(GentleC47.Grass, Offset(x, ground), Offset(x + w * 0.004f * sin(t + k), ground - h * 0.08f * green * (0.6f + 0.4f * ((k * 7) % 3) / 2f)), 2f * dp, StrokeCap.Round, alpha = f)
        }
        cloud38(Offset(w * 0.75f, h * 0.16f), w * 0.12f, Color(0xFF6A6A7A), f)
        rain12(w * 0.55f, w * 0.95f, h * 0.22f, ground, t * 1.6f, Color(0xFF4A5A70), f, 40)
        val wash = seg(rf, 0.3f, 0.9f)
        drawPath(Path().apply {
            moveTo(w * 0.52f, ground); lineTo(w, ground - h * 0.02f * wash); lineTo(w, ground + h * 0.08f); lineTo(w * 0.52f, ground + h * 0.06f * wash); close()
        }, GentleC47.Flood, alpha = f * wash)
        drawLine(Color(0xFF2E3A40), Offset(w * 0.5f, h * 0.2f), Offset(w * 0.5f, ground), 1.5f * dp, alpha = f * 0.6f)
    }
    // a bucket of water in the mosque
    if (bk > 0f) {
        val f = easeOut(seg(bk, 0f, BEAT_IN)) * (1f - seg(sp, 0f, BEAT_OUT))
        drawRect(Color(0xFFEFE2C4), Offset(0f, 0f), Size(w, ground), alpha = f)
        for (k in 0 until 4) drawPath(archPath26(w * (0.15f + 0.23f * k), h * 0.12f, ground - h * 0.3f, w * 0.08f), Color(0xFFD8C49C), alpha = f)
        drawRect(GentleC47.Floor, Offset(0f, ground), Size(w, h - ground), alpha = f)
        val stop = seg(bk, 0.25f, 0.4f)
        for (k in 0 until 3) {
            val x = w * (0.72f + 0.08f * k) - w * 0.1f * seg(bk, 0.05f, 0.25f)
            nameRoundel(Offset(x, ground - h * 0.08f), w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        }
        val spot = Offset(w * 0.4f, ground + h * 0.05f)
        drawOval(Color(0xFFC0AC88), Offset(spot.x - w * 0.06f, spot.y - h * 0.015f), Size(w * 0.12f, h * 0.03f), alpha = f * (1f - seg(bk, 0.7f, 0.95f)))
        val pour = seg(bk, 0.45f, 0.8f)
        val bucket = spot + Offset(-w * 0.04f, -h * 0.28f)
        rotate(70f * pour, bucket) { cup12(bucket, w * 0.06f, Color(0xFF8A6A44), f) }
        if (pour > 0f) for (k in 0 until 10) {
            val u = fract(t * 2f + k / 10f)
            drawCircle(GentleC47.Water, 2f * dp, bucket + (spot - bucket) * u + Offset(w * 0.01f * sin(k * 1.3f), 0f), alpha = f * bump(pour))
        }
    }
    // make things easy, give good tidings
    if (sp > 0f) {
        val f = easeOut(seg(sp, 0f, BEAT_IN)) * (1f - seg(ad, 0f, BEAT_OUT))
        drawRect(Color(0xFFCDB890), Offset(w * 0.47f, ground - h * 0.5f), Size(w * 0.06f, h * 0.5f), alpha = 0f)
        for (k in 0 until 2) {
            val pole = Offset(w * (0.36f + 0.28f * k), ground)
            val g = easeOut(seg(sp, 0.1f + 0.3f * k, 0.35f + 0.3f * k))
            drawLine(GentleC47.Sign, pole, pole + Offset(0f, -h * 0.42f * g), 4f * dp, StrokeCap.Round, alpha = f)
            drawPath(Path().apply {
                val y = ground - h * 0.38f * g
                val dir = if (k == 0) -1f else 1f
                moveTo(pole.x, y - h * 0.04f); lineTo(pole.x + dir * w * 0.14f, y - h * 0.04f); lineTo(pole.x + dir * w * 0.17f, y); lineTo(pole.x + dir * w * 0.14f, y + h * 0.04f); lineTo(pole.x, y + h * 0.04f); close()
            }, Color(0xFFE8D8B0), alpha = f * g)
            tagAt(a.tag("signposts", 0, k), Offset(pole.x + (if (k == 0) -1f else 1f) * w * 0.08f, ground - h * 0.38f * g), f * g, back = Back47, ink = ArtInk.Gold)
        }
    }
    // the jar that gentleness adorns
    if (ad > 0f) {
        val f = easeOut(seg(ad, 0f, 0.3f))
        val c = Offset(w * 0.5f, h * 0.52f)
        val jar = Path().apply {
            moveTo(c.x - w * 0.04f, c.y - h * 0.26f); lineTo(c.x + w * 0.04f, c.y - h * 0.26f)
            quadraticTo(c.x + w * 0.03f, c.y - h * 0.18f, c.x + w * 0.1f, c.y - h * 0.08f)
            quadraticTo(c.x + w * 0.14f, c.y + h * 0.14f, c.x, c.y + h * 0.22f)
            quadraticTo(c.x - w * 0.14f, c.y + h * 0.14f, c.x - w * 0.1f, c.y - h * 0.08f)
            quadraticTo(c.x - w * 0.03f, c.y - h * 0.18f, c.x - w * 0.04f, c.y - h * 0.26f); close()
        }
        drawPath(jar, GentleC47.Vessel, alpha = f)
        val bloomP = easeOut(seg(ad, 0.15f, 0.5f)) * (1f - seg(ad, 0.6f, 0.75f))
        for (k in 0 until 5) drawPath(starPath(c + Offset(w * 0.045f * (k - 2), h * 0.02f * (k % 2)), w * 0.018f), GentleC47.Pattern, alpha = f * bloomP)
        drawLine(GentleC47.Pattern, c + Offset(-w * 0.11f, -h * 0.05f), c + Offset(w * 0.11f, -h * 0.05f), 2f * dp, alpha = f * bloomP)
        drawLine(GentleC47.Pattern, c + Offset(-w * 0.12f, h * 0.1f), c + Offset(w * 0.12f, h * 0.1f), 2f * dp, alpha = f * bloomP)
        if (bloomP > 0.2f) softGlow(c, w * 0.12f, ArtInk.Gold, 0.3f * f * bloomP)
        val crack = seg(ad, 0.7f, 0.9f)
        if (crack > 0f) drawPath(Path().apply { moveTo(c.x + w * 0.02f, c.y - h * 0.2f); lineTo(c.x - w * 0.01f, c.y - h * 0.08f); lineTo(c.x + w * 0.03f, c.y + h * 0.02f); lineTo(c.x, c.y + h * 0.1f * crack) }, Color(0xFF3A2A1E), alpha = f * crack, style = Stroke(2f * dp))
    }
}
