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
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.lerp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 3 of the illustrated scenes: the night sky (revelation and the soothsayer, the stars and the rain, the
 * heavens rolled up, the heavens trembling at revelation) and the grain no maker of images can create.
 * Same rules as Art.kt.
 */

private val NightBack = Color(0xE6101426)

private object NightInk {
    val Top = Color(0xFF0A0F28)
    val Low = Color(0xFF2C2A58)
    val Hills = Color(0xFF1C1A34)
    val Ground = Color(0xFF151327)
    val Cloud = Color(0xFF1B1726)
    val Page = Color(0xFFF1E6C8)
    val Tower = Color(0xFF2A2440)
    val TowerEdge = Color(0xFF4A4068)
    val Rain = Color(0xFF9FC8F0)
}

/** An ember cross drawn over [c] once [p] runs (0..1). */
private fun DrawScope.nightCross(c: Offset, r: Float, p: Float) {
    if (p <= 0f) return
    val a1 = seg(p, 0f, 0.5f)
    val a2 = seg(p, 0.5f, 1f)
    drawLine(ArtInk.Ember, c + Offset(-r, -r), c + Offset(-r + 2 * r * a1, -r + 2 * r * a1), 2.4f * density, StrokeCap.Round)
    if (a2 > 0f) drawLine(ArtInk.Ember, c + Offset(r, -r), c + Offset(r - 2 * r * a2, -r + 2 * r * a2), 2.4f * density, StrokeCap.Round)
}

/**
 * The night sky. Beats: book (an open book of light: revelation), cloud (a dark cloud covers it), nights (forty
 * small moons go dark one by one), towers (towers of the proud on the ground), roll (the sky is rolled up like a
 * scroll), shrink (the towers sink to nothing), note (a word in the sky), layers (the heavens as layers), quake
 * (they tremble), calm (they are still), truth (a thread of light comes down through them), nocall (calls sent up
 * from the ground fall short, struck out), adorn (the stars shine: an adornment), shoot (shooting stars), guide (a
 * star the way is found by), credit (rain credited to a star: struck out), rain (rain falls by Allah's favour).
 */
internal fun DrawScope.nightScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.86f
    drawRect(Brush.verticalGradient(listOf(NightInk.Top, NightInk.Low), endY = ground), size = Size(w, ground))

    val roll = easeInOut(a.b("roll"))
    val rollY = roll * ground                       // the sky above this line is rolled away
    val adorn = easeOut(a.b("adorn"))
    for (s in TWINKLES) {
        val y = s.y * ground * 0.8f
        if (y < rollY) continue
        val tw = 0.5f + 0.5f * sin(t * (1.4f + 1.6f * adorn) + s.p)
        drawCircle(ArtInk.Ivory.copy(alpha = (0.35f + 0.45f * tw) * (0.7f + 0.3f * adorn)), radius = s.r * dp * (1f + 0.5f * adorn), center = Offset(s.x * w, y))
        if (adorn > 0f && s.r > 1.2f) drawPath(starPath(Offset(s.x * w, y), s.r * 3f * dp * adorn * tw), ArtInk.Gold.copy(alpha = 0.35f * adorn))
    }
    if (roll > 0f && roll < 1f) {
        // the rolled edge of the sky
        drawRect(Brush.verticalGradient(listOf(Color(0xFF05070F), NightInk.Top), startY = rollY - h * 0.05f, endY = rollY + h * 0.01f), topLeft = Offset(0f, 0f), size = Size(w, rollY + h * 0.01f))
        drawRoundRect(Brush.verticalGradient(listOf(Color(0xFF3A3870), Color(0xFF151530)), startY = rollY - h * 0.03f, endY = rollY + h * 0.03f), topLeft = Offset(-4f * dp, rollY - h * 0.03f), size = Size(w + 8f * dp, h * 0.06f), cornerRadius = CornerRadius(h * 0.03f, h * 0.03f))
    } else if (roll >= 1f) {
        drawRect(Color(0xFF05070F), size = Size(w, ground))
    }
    if (a.count("roll") > 0) tagAt(a.tag("roll"), Offset(w * 0.5f, h * 0.1f), seg(a.b("roll"), 0.1f, 0.5f), back = NightBack, ink = ArtInk.Ivory)

    // the heavens as layers, trembling at revelation, and a thread of light coming down
    val layersA = easeOut(a.b("layers"))
    val quake = a.b("quake")
    val calm = easeOut(a.b("calm"))
    val shaking = easeOut(seg(quake, 0f, 0.3f)) * (1f - calm)
    if (layersA > 0f) {
        val centre = Offset(w * 0.5f, h * 1.3f)
        for (k in 0 until 7) {
            val r = h * (0.62f + 0.1f * k)
            val jitter = shaking * 2.5f * dp * sin(t * 40f + k * 1.3f)
            val flash = shaking * (0.5f + 0.5f * sin(t * 18f + k))
            drawArc(lerp(Color(0xFF6A6AAE), ArtInk.Ivory, flash * 0.6f).copy(alpha = layersA * (0.6f - 0.05f * k)), 195f, 150f, false, topLeft = Offset(centre.x - r, centre.y - r + jitter), size = Size(2 * r, 2 * r), style = Stroke((1.6f + shaking) * dp))
        }
        if (a.count("quake") > 0) tagAt(a.tag("quake"), Offset(w * 0.8f, h * 0.42f), seg(quake, 0.3f, 1f) * (1f - calm), back = NightBack, ink = ArtInk.Ivory)
    }
    val truth = easeOut(a.b("truth"))
    if (truth > 0f) {
        val end = mix(0f, ground, truth)
        drawLine(ArtInk.Gold.copy(alpha = 0.3f), Offset(w * 0.5f, 0f), Offset(w * 0.5f, end), 7f * dp, StrokeCap.Round)
        drawLine(ArtInk.Gold, Offset(w * 0.5f, 0f), Offset(w * 0.5f, end), 1.6f * dp, StrokeCap.Round)
        tagAt(a.tag("truth"), Offset(w * 0.5f, h * 0.28f), seg(a.b("truth"), 0.4f, 1f), back = NightBack, ink = ArtInk.Gold)
    }

    // the book of revelation, and the cloud that covers it
    val book = easeOut(a.b("book"))
    val cloud = a.b("cloud")
    val cover = easeInOut(seg(cloud, 0f, 0.7f))
    val bc = Offset(w * 0.5f, h * 0.22f)
    if (book > 0f) {
        val lit = book * (1f - 0.85f * cover)
        for (m in 0 until 12) {
            val ang = (m * 30f + t * 5f) * PI.toFloat() / 180f
            val r0 = w * 0.06f
            val r1 = r0 + w * 0.08f * lit
            drawLine(ArtInk.Gold.copy(alpha = 0.3f * lit), bc + Offset(cos(ang) * r0, sin(ang) * r0 * 0.7f), bc + Offset(cos(ang) * r1, sin(ang) * r1 * 0.7f), 2f * dp, StrokeCap.Round)
        }
        drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.45f * lit), Color.Transparent), center = bc, radius = w * 0.12f), radius = w * 0.12f, center = bc)
        for (sd in listOf(-1f, 1f)) {
            val page = Path().apply {
                moveTo(bc.x, bc.y + 8f * dp)
                lineTo(bc.x + sd * 28f * dp, bc.y + 3f * dp)
                lineTo(bc.x + sd * 28f * dp, bc.y - 15f * dp)
                lineTo(bc.x, bc.y - 10f * dp)
                close()
            }
            drawPath(page, lerp(Color(0xFF55506A), NightInk.Page, 0.3f + 0.7f * lit).copy(alpha = book))
            for (k in 0 until 3) drawLine(Color(0xFF8C7A5A).copy(alpha = 0.6f * book), Offset(bc.x + sd * 6f * dp, bc.y - 6f * dp + k * 4.5f * dp), Offset(bc.x + sd * 23f * dp, bc.y - 9f * dp + k * 4.5f * dp), 0.8f * dp)
        }
        tagAt(a.tag("book"), bc + Offset(0f, h * 0.13f), seg(a.b("book"), 0.3f, 1f) * (1f - cover), back = NightBack, ink = ArtInk.Gold)
    }
    if (cloud > 0f) {
        val cx = mix(-w * 0.2f, bc.x, cover)
        for ((dx, dy, r) in listOf(Triple(-0.07f, 0.01f, 0.07f), Triple(0f, -0.025f, 0.09f), Triple(0.075f, 0.01f, 0.07f), Triple(0.03f, 0.035f, 0.06f))) {
            drawCircle(NightInk.Cloud.copy(alpha = 0.95f), radius = w * r, center = Offset(cx + w * dx, bc.y + h * dy))
        }
        tagAt(a.tag("cloud"), Offset(bc.x, bc.y + h * 0.17f), seg(cloud, 0.4f, 1f), back = NightBack, ink = ArtInk.Ember)
    }

    // forty nights going dark one by one
    val nights = a.b("nights")
    if (nights > 0f) {
        val appear = easeOut(seg(nights, 0f, 0.25f))
        for (k in 0 until 40) {
            val col = k % 10
            val row = k / 10
            val c = Offset(w * (0.2f + 0.6f * col / 9f), h * (0.5f + 0.06f * row))
            val dark = seg(nights, 0.25f + 0.017f * k, 0.3f + 0.017f * k)
            val moon = lerp(ArtInk.Ivory, Color(0xFF3A3656), dark).copy(alpha = appear)
            drawCircle(moon, radius = 4.5f * dp, center = c)
            drawCircle(lerp(NightInk.Low, NightInk.Top, 0.5f).copy(alpha = appear), radius = 4f * dp, center = c + Offset(2.2f * dp, -1.2f * dp))
        }
        tagAt(a.tag("nights"), Offset(w * 0.5f, h * 0.79f), seg(nights, 0.2f, 0.6f), back = NightBack, ink = ArtInk.Ember)
    }

    // shooting stars
    val shoot = easeOut(a.b("shoot"))
    if (shoot > 0f) {
        for (m in 0 until 3) {
            val q = fract(t * 0.5f + m / 3f)
            val s0 = Offset(w * (0.25f + 0.3f * m), h * (0.06f + 0.05f * m))
            val c = s0 + Offset(-w * 0.25f * q, h * 0.2f * q)
            drawLine(ArtInk.Ivory.copy(alpha = shoot * (1f - q)), c, c + Offset(w * 0.06f, -h * 0.05f), 1.6f * dp, StrokeCap.Round)
            drawCircle(ArtInk.Ivory.copy(alpha = shoot * (1f - q)), radius = 2f * dp, center = c)
        }
        tagAt(a.tag("shoot"), Offset(w * 0.3f, h * 0.38f), seg(a.b("shoot"), 0.3f, 1f), back = NightBack, ink = ArtInk.Ivory)
    }
    // a guiding star, and a road taken by it
    val guide = easeOut(a.b("guide"))
    val gs = Offset(w * 0.82f, h * 0.14f)
    if (guide > 0f) {
        drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.6f * guide), Color.Transparent), center = gs, radius = w * 0.06f), radius = w * 0.06f, center = gs)
        drawPath(starPath(gs, 8f * dp * guide), ArtInk.Gold)
        drawLine(ArtInk.Gold.copy(alpha = 0.5f * guide), gs, Offset(w * 0.68f, ground), 1.2f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(4f * dp, 6f * dp), -t * 18f * dp))
        tagAt(a.tag("guide"), gs + Offset(-w * 0.05f, h * 0.12f), seg(a.b("guide"), 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    }

    // ground
    hills(NightInk.Hills, ground - h * 0.03f, h * 0.02f, 0.012f, 0.03f, 0.6f)
    drawRect(NightInk.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))

    // towers of the proud, sinking to nothing
    if (a.count("towers") > 0) {
        val tw = easeOut(a.b("towers"))
        val shrink = easeInOut(a.b("shrink"))
        for (k in 0 until 5) {
            val x = w * (0.12f + 0.19f * k)
            val th = h * (0.3f + 0.08f * ((k * 7) % 3)) * tw * (1f - 0.92f * shrink)
            val bw = w * (0.05f + 0.01f * (k % 2))
            if (th <= 1f) continue
            drawRect(NightInk.Tower, topLeft = Offset(x - bw / 2, ground - th), size = Size(bw, th))
            drawRect(NightInk.TowerEdge, topLeft = Offset(x - bw / 2, ground - th), size = Size(bw, th), style = Stroke(1f * dp))
            for (m in 0 until 3) drawRect(NightInk.TowerEdge, topLeft = Offset(x - bw / 2 + m * bw / 3f + 1f * dp, ground - th - 4f * dp), size = Size(bw / 3f - 2f * dp, 4f * dp))
        }
        tagAt(a.tag("shrink"), Offset(w * 0.5f, ground - h * 0.12f), seg(a.b("shrink"), 0.4f, 1f), back = NightBack, ink = ArtInk.Ember)
    }
    // calls sent up from the ground that fall short
    val nocall = a.b("nocall")
    if (nocall > 0f) {
        for (k in 0 until 3) {
            val base = Offset(w * (0.25f + 0.25f * k), ground)
            drawRoundRect(Color(0xFF4A4460), topLeft = base + Offset(-w * 0.02f, -h * 0.07f), size = Size(w * 0.04f, h * 0.07f), cornerRadius = CornerRadius(w * 0.02f, w * 0.02f))
            val up = easeOut(seg(nocall, 0f, 0.5f))
            val top = base + Offset(0f, -h * 0.07f - h * 0.25f * up)
            drawLine(ArtInk.Ivory.copy(alpha = 0.6f * (1f - seg(nocall, 0.6f, 1f) * 0.6f)), base + Offset(0f, -h * 0.07f), top, 1.2f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(3f * dp, 4f * dp)))
            nightCross(top, 6f * dp, seg(nocall, 0.5f, 0.9f))
        }
        tagAt(a.tag("nocall"), Offset(w * 0.5f, ground - h * 0.42f), seg(nocall, 0.3f, 1f), back = NightBack, ink = ArtInk.Ember)
    }
    // rain credited to a star (struck out), and rain by Allah's favour
    val credit = a.b("credit")
    if (credit > 0f) {
        val star = Offset(w * 0.2f, h * 0.16f)
        drawPath(starPath(star, 7f * dp), ArtInk.Ivory.copy(alpha = easeOut(seg(credit, 0f, 0.3f))))
        drawLine(ArtInk.Ember.copy(alpha = 0.7f * easeOut(seg(credit, 0.1f, 0.5f))), star, Offset(w * 0.3f, h * 0.45f), 1.2f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(3f * dp, 4f * dp)))
        nightCross(star, 10f * dp, seg(credit, 0.5f, 0.9f))
        tagAt(a.tag("credit"), star + Offset(w * 0.08f, h * 0.1f), seg(credit, 0.3f, 1f), back = NightBack, ink = ArtInk.Ember)
    }
    val rain = easeOut(a.b("rain"))
    if (rain > 0f) {
        for (m in 0 until (50 * rain).toInt()) {
            val x0 = fract(m * 0.618f + 0.05f) * w
            val y0 = fract(t * 0.9f + m * 0.137f) * ground
            drawLine(NightInk.Rain.copy(alpha = 0.55f * rain), Offset(x0, y0), Offset(x0 - 2f * dp, y0 + 9f * dp), 1f * dp, StrokeCap.Round)
        }
        // the land drinks: green shoots along the ground
        for (k in 0 until 14) {
            val x = w * (0.04f + 0.07f * k)
            val q = seg(a.b("rain"), 0.4f + 0.02f * k, 0.9f)
            if (q <= 0f) continue
            drawLine(ArtInk.Jade.copy(alpha = 0.9f), Offset(x, ground), Offset(x - 3f * dp, ground - 9f * dp * q), 1.6f * dp, StrokeCap.Round)
            drawLine(ArtInk.Jade.copy(alpha = 0.9f), Offset(x, ground), Offset(x + 3f * dp, ground - 7f * dp * q), 1.6f * dp, StrokeCap.Round)
        }
        tagAt(a.tag("rain"), Offset(w * 0.62f, ground - h * 0.2f), seg(a.b("rain"), 0.3f, 1f), back = NightBack, ink = ArtInk.Jade)
    }
    if (a.count("adorn") > 0) tagAt(a.tag("adorn"), Offset(w * 0.5f, h * 0.08f), seg(a.b("adorn"), 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    for (k in 0 until a.count("note")) {
        tagAt(a.tag("note", k), Offset(w * 0.5f, h * (0.22f + 0.09f * k)), seg(a.b("note", k), 0.3f, 1f), back = NightBack, ink = ArtInk.Ember)
    }
}

// ───────────── a grain: what no maker of images can create ─────────────

private object GrainInk {
    val SkyTop = Color(0xFF2C3E6E)
    val SkyLow = Color(0xFFE7B98C)
    val Soil = Color(0xFF4A3426)
    val SoilDark = Color(0xFF2E2018)
    val Seed = Color(0xFFC9A66B)
    val Stem = Color(0xFF5FA86A)
    val Leaf = Color(0xFF7CC48A)
    val Easel = Color(0xFF7A5634)
    val Canvas = Color(0xFFEDE3C8)
}

/**
 * Two grains side by side. Beats: grain (a real grain in the soil sprouts and grows: Allah's making), challenge (a
 * grain drawn on a canvas beside it: it stays a drawing, struck out), breath (wind blown on the drawing: nothing
 * stirs, struck out).
 */
internal fun DrawScope.grainScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.72f
    drawRect(Brush.verticalGradient(listOf(GrainInk.SkyTop, GrainInk.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(Brush.verticalGradient(listOf(GrainInk.Soil, GrainInk.SoilDark), startY = ground, endY = h), topLeft = Offset(0f, ground), size = Size(w, h - ground))
    drawLine(Color(0xFF6A4E38), Offset(0f, ground), Offset(w, ground), 1.2f * dp)

    // the real grain, on the right, sprouting
    val grain = a.b("grain")
    val gx = w * 0.72f
    val seed = Offset(gx, ground + h * 0.08f)
    if (a.count("grain") > 0) {
        drawOval(GrainInk.Seed, topLeft = seed + Offset(-7f * dp, -4.5f * dp), size = Size(14f * dp, 9f * dp))
        val root = easeOut(seg(grain, 0f, 0.4f))
        if (root > 0f) {
            for (sd in listOf(-1f, 0f, 1f)) drawLine(Color(0xFFD9C19A).copy(alpha = 0.9f), seed + Offset(0f, 3f * dp), seed + Offset(sd * 12f * dp * root, (10f + 8f * (1f - sd * sd)) * dp * root), 1.2f * dp, StrokeCap.Round)
        }
        val grow = easeOut(seg(grain, 0.2f, 1f))
        if (grow > 0f) {
            val sway = sin(t * 1.6f) * 3f * dp * grow
            val top = Offset(gx + sway, ground - h * 0.32f * grow)
            val stem = Path().apply {
                moveTo(seed.x, seed.y - 3f * dp)
                cubicTo(seed.x, ground, gx + sway * 0.3f, ground - h * 0.12f * grow, top.x, top.y)
            }
            drawPath(stem, GrainInk.Stem, style = Stroke(2.6f * dp, cap = StrokeCap.Round))
            for (k in 0 until 4) {
                val f = 0.3f + 0.17f * k
                if (grow < f) continue
                val c = Offset(mix(seed.x, top.x, f), mix(ground, top.y, f))
                val sd = if (k % 2 == 0) 1f else -1f
                val leaf = Path().apply {
                    moveTo(c.x, c.y)
                    quadraticBezierTo(c.x + sd * 14f * dp, c.y - 12f * dp, c.x + sd * 24f * dp, c.y - 4f * dp)
                    quadraticBezierTo(c.x + sd * 12f * dp, c.y + 2f * dp, c.x, c.y)
                    close()
                }
                drawPath(leaf, GrainInk.Leaf.copy(alpha = seg(grow, f, f + 0.15f)))
            }
            if (grow > 0.85f) {
                val q = seg(grow, 0.85f, 1f)
                for (k in 0 until 5) drawOval(GrainInk.Seed.copy(alpha = q), topLeft = top + Offset(-3f * dp + (k % 2) * 3f * dp, -6f * dp - k * 5f * dp), size = Size(5f * dp, 7f * dp))
            }
            drawCircle(Brush.radialGradient(listOf(ArtInk.Gold.copy(alpha = 0.25f * grow), Color.Transparent), center = top, radius = w * 0.12f), radius = w * 0.12f, center = top)
        }
        tagAt(a.tag("grain"), Offset(gx, h * 0.12f), seg(grain, 0.3f, 1f), back = NightBack, ink = ArtInk.Jade)
    }

    // the drawn grain, on the left: a canvas on an easel
    val ch = a.b("challenge")
    val ex = w * 0.3f
    if (ch > 0f) {
        val q = easeOut(seg(ch, 0f, 0.4f))
        drawLine(GrainInk.Easel.copy(alpha = q), Offset(ex - w * 0.08f, ground), Offset(ex - w * 0.02f, ground - h * 0.48f), 3f * dp, StrokeCap.Round)
        drawLine(GrainInk.Easel.copy(alpha = q), Offset(ex + w * 0.08f, ground), Offset(ex + w * 0.02f, ground - h * 0.48f), 3f * dp, StrokeCap.Round)
        val canvas = Rect(ex - w * 0.1f, ground - h * 0.44f, ex + w * 0.1f, ground - h * 0.16f)
        drawRect(GrainInk.Canvas.copy(alpha = q), canvas.topLeft, canvas.size)
        drawRect(GrainInk.Easel.copy(alpha = q), canvas.topLeft, canvas.size, style = Stroke(2f * dp))
        // a flat, outlined grain: a picture only
        val gc = canvas.center
        drawOval(Color(0xFF8C7A5A).copy(alpha = q), topLeft = gc + Offset(-9f * dp, -6f * dp), size = Size(18f * dp, 12f * dp), style = Stroke(1.4f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
        nightCross(gc, 16f * dp, seg(ch, 0.55f, 0.95f))
        tagAt(a.tag("challenge"), Offset(ex, h * 0.12f), seg(ch, 0.3f, 1f), back = NightBack, ink = ArtInk.Ember)
    }
    val breath = a.b("breath")
    if (breath > 0f) {
        val c = Offset(ex, ground - h * 0.3f)
        for (k in 0 until 3) {
            val q = fract(t * 0.8f + k / 3f)
            val y = c.y + (k - 1) * h * 0.05f
            drawLine(ArtInk.Ivory.copy(alpha = 0.5f * (1f - q) * (1f - seg(breath, 0.7f, 1f))), Offset(w * 0.02f + q * w * 0.12f, y), Offset(w * 0.08f + q * w * 0.12f, y), 1.4f * dp, StrokeCap.Round)
        }
        tagAt(a.tag("breath"), Offset(ex, ground + h * 0.15f), seg(breath, 0.3f, 1f), back = NightBack, ink = ArtInk.Ember)
    }
}
