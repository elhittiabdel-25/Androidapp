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
    val roll = easeInOut(a.b("roll"))
    val rollY = roll * ground                       // the sky above this line is rolled away
    val adorn = easeOut(a.b("adorn"))
    deepSky(t, ground, NightInk.Top, NightInk.Low, shine = adorn)
    if (adorn > 0f) for (s in TWINKLES) {
        if (s.r <= 1.2f) continue
        val c = Offset(s.x * w, s.y * ground * 0.8f)
        if (c.y < rollY) continue
        val tw = 0.5f + 0.5f * sin(t * 2.6f + s.p)
        bloom(c, s.r * 1.6f * dp, ArtInk.Gold, 0.5f * adorn * tw)
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
        tagAt(a.tag("layers"), Offset(w * 0.5f, h * 0.07f), seg(a.b("layers"), 0.3f, 1f), back = NightBack, ink = ArtInk.Ivory)
    }
    val truth = easeOut(a.b("truth"))
    if (truth > 0f) {
        val end = mix(0f, ground, truth)
        drawLine(ArtInk.Gold.copy(alpha = 0.3f), Offset(w * 0.5f, 0f), Offset(w * 0.5f, end), 7f * dp, StrokeCap.Round)
        drawLine(ArtInk.Gold, Offset(w * 0.5f, 0f), Offset(w * 0.5f, end), 1.6f * dp, StrokeCap.Round)
        bloom(Offset(w * 0.5f, end), 5f * dp, ArtInk.Gold, truth)
        tagAt(a.tag("truth"), Offset(w * 0.5f, h * 0.28f), seg(a.b("truth"), 0.4f, 1f), back = NightBack, ink = ArtInk.Gold)
    }

    // the book of revelation, and the cloud that covers it
    val book = easeOut(a.b("book"))
    val cloud = a.b("cloud")
    val cover = easeInOut(seg(cloud, 0f, 0.7f))
    val bc = Offset(w * 0.5f, h * 0.22f)
    if (book > 0f) {
        val lit = book * (1f - 0.85f * cover)
        lightShafts(bc, -PI.toFloat() / 2f, PI.toFloat() * 1.25f, w * 0.34f, ArtInk.Gold, lit, t, 11)
        bloom(bc, w * 0.045f, ArtInk.Gold, lit * 0.9f)
        for (sd in listOf(-1f, 1f)) {
            val page = Path().apply {
                moveTo(bc.x, bc.y + 8f * dp)
                lineTo(bc.x + sd * 28f * dp, bc.y + 3f * dp)
                lineTo(bc.x + sd * 28f * dp, bc.y - 15f * dp)
                lineTo(bc.x, bc.y - 10f * dp)
                close()
            }
            drawPath(page, lerp(Color(0xFF55506A), NightInk.Page, 0.3f + 0.7f * lit).copy(alpha = book))
            // lines written on the pages: with the book, or later with its own beat
            val written = if (a.count("write") > 0) easeOut(a.b("write")) else easeOut(seg(a.b("book"), 0.3f, 1f))
            for (k in 0 until 3) {
                val f = (written * 3f - k).coerceIn(0f, 1f)
                if (f <= 0f) continue
                val x0 = bc.x + sd * 6f * dp
                val x1 = bc.x + sd * 23f * dp
                drawLine(lerp(Color(0xFF8C7A5A), ArtInk.Gold, 0.4f).copy(alpha = 0.8f * book), Offset(x0, bc.y - 6f * dp + k * 4.5f * dp), Offset(mix(x0, x1, f), bc.y - 6f * dp + k * 4.5f * dp - 3f * dp * f), 0.9f * dp)
            }
        }
        tagAt(a.tag("book"), bc + Offset(0f, h * 0.13f), seg(a.b("book"), 0.3f, 1f) * (1f - cover), back = NightBack, ink = ArtInk.Gold)
    }
    // sent down: light comes down onto the book from above
    val descend = easeOut(a.b("descend"))
    if (descend > 0f) {
        val end = mix(0f, bc.y - 12f * dp, descend)
        val beam = Path().apply {
            moveTo(bc.x - 4f * dp, 0f)
            lineTo(bc.x + 4f * dp, 0f)
            lineTo(bc.x + 22f * dp, end)
            lineTo(bc.x - 22f * dp, end)
            close()
        }
        lightShafts(Offset(bc.x, -h * 0.08f), PI.toFloat() / 2f, 0.32f, end + h * 0.12f, ArtInk.Gold, descend, t, 5)
        drawPath(beam, Brush.verticalGradient(listOf(ArtInk.Gold.copy(alpha = 0.1f), ArtInk.Gold.copy(alpha = 0.45f)), startY = 0f, endY = max(1f, end)))
        tagAt(a.tag("descend"), Offset(w * 0.78f, h * 0.1f), seg(a.b("descend"), 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    }
    // recited: the sound of it spreads
    val recite = easeOut(a.b("recite"))
    if (recite > 0f) {
        for (k in 0 until 4) {
            val q = fract(t * 0.5f + k / 4f)
            drawArc(ArtInk.Ivory.copy(alpha = 0.5f * recite * (1f - q)), 150f, 60f, false, topLeft = bc + Offset(-(30f + 90f * q) * dp, -(30f + 90f * q) * dp), size = Size((60f + 180f * q) * dp, (60f + 180f * q) * dp), style = Stroke(1.4f * dp))
            drawArc(ArtInk.Ivory.copy(alpha = 0.5f * recite * (1f - q)), -30f, 60f, false, topLeft = bc + Offset(-(30f + 90f * q) * dp, -(30f + 90f * q) * dp), size = Size((60f + 180f * q) * dp, (60f + 180f * q) * dp), style = Stroke(1.4f * dp))
        }
        tagAt(a.tag("recite"), Offset(w * 0.18f, h * 0.22f), seg(a.b("recite"), 0.3f, 1f), back = NightBack, ink = ArtInk.Ivory)
    }
    // kept: small lights leave the book and stay, glowing, along the land
    val keep = a.b("keep")
    if (keep > 0f) {
        for (k in 0 until 9) {
            val dest = Offset(w * (0.1f + 0.1f * k), ground - h * 0.06f)
            val q = easeInOut(seg(keep, k * 0.05f, k * 0.05f + 0.55f))
            if (q <= 0f) continue
            val c = Offset(mix(bc.x, dest.x, q), mix(bc.y, dest.y, q) - bump(q) * h * 0.08f)
            softGlow(c, 9f * dp, ArtInk.Gold, 0.6f)
            drawCircle(ArtInk.Ivory, radius = 2f * dp, center = c)
        }
        tagAt(a.tag("keep"), Offset(w * 0.5f, ground - h * 0.17f), seg(keep, 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    }
    if (a.count("write") > 0) tagAt(a.tag("write"), Offset(w * 0.8f, h * 0.24f), seg(a.b("write"), 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    // what He wills comes to be, what He does not will does not
    val will = a.b("will")
    if (will > 0f) {
        val yes = Offset(w * 0.7f, h * 0.55f)
        val no = Offset(w * 0.3f, h * 0.55f)
        val q = easeOut(seg(will, 0f, 0.3f))
        val become = easeOut(seg(will, 0.35f, 0.9f))
        drawCircle(ArtInk.Ivory.copy(alpha = 0.6f * q * (1f - become)), radius = 14f * dp, center = yes, style = Stroke(1.3f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
        if (become > 0f) {
            bloom(yes, 9f * dp, ArtInk.Gold, become)
            drawPath(starPath(yes, 12f * dp * become), ArtInk.Gold)
        }
        drawCircle(ArtInk.Ivory.copy(alpha = 0.6f * q * (1f - become)), radius = 14f * dp, center = no, style = Stroke(1.3f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
        tagAt(a.tag("will", 0, 0), yes + Offset(0f, h * 0.1f), seg(will, 0.4f, 1f), back = NightBack, ink = ArtInk.Gold)
        tagAt(a.tag("will", 0, 1), no + Offset(0f, h * 0.1f), seg(will, 0.6f, 1f), back = NightBack, ink = ArtInk.Ivory)
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
        bloom(gs, 6f * dp, ArtInk.Gold, guide)
        glint(gs, 22f * dp * guide, ArtInk.BrassLight, guide * (0.7f + 0.3f * sin(t * 2f)))
        drawPath(starPath(gs, 8f * dp * guide), ArtInk.Gold)
        drawLine(ArtInk.Gold.copy(alpha = 0.5f * guide), gs, Offset(w * 0.68f, ground), 1.2f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(4f * dp, 6f * dp), -t * 18f * dp))
        tagAt(a.tag("guide"), gs + Offset(-w * 0.05f, h * 0.12f), seg(a.b("guide"), 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    }

    // ground: a far range in the haze, then the near hills
    farHills(NightInk.Hills, NightInk.Low, ground - h * 0.075f, h * 0.035f, 1.9f)
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
        tagAt(a.tag("nocall"), Offset(w * 0.5f, h * 0.46f), seg(nocall, 0.3f, 1f), back = NightBack, ink = ArtInk.Ember)
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
        tagAt(a.tag("rain"), Offset(w * 0.62f, h * 0.5f), seg(a.b("rain"), 0.3f, 1f), back = NightBack, ink = ArtInk.Jade)
    }
    // creation: new stars, and life along the land
    val create = a.b("create")
    if (create > 0f) {
        for (m in 0 until 12) {
            val q = easeOut(seg(create, m * 0.04f, m * 0.04f + 0.4f))
            if (q <= 0f) continue
            val c = Offset(w * fract(m * 0.381f + 0.07f), h * (0.08f + 0.5f * fract(m * 0.613f)))
            drawPath(starPath(c, 4f * dp * q), ArtInk.Ivory.copy(alpha = 0.9f))
        }
        for (k in 0 until 16) {
            val x = w * (0.03f + 0.062f * k)
            val q = seg(create, 0.3f + 0.025f * k, 0.9f)
            if (q <= 0f) continue
            drawLine(ArtInk.Jade, Offset(x, ground), Offset(x - 3f * dp, ground - 10f * dp * q), 1.6f * dp, StrokeCap.Round)
            drawLine(ArtInk.Jade, Offset(x, ground), Offset(x + 3f * dp, ground - 8f * dp * q), 1.6f * dp, StrokeCap.Round)
            if (k % 3 == 0 && q > 0.8f) drawCircle(ArtInk.Gold, radius = 2f * dp, center = Offset(x - 3f * dp, ground - 10f * dp * q))
        }
        tagAt(a.tag("create"), Offset(w * 0.5f, ground - h * 0.13f), seg(create, 0.3f, 1f), back = NightBack, ink = ArtInk.Jade)
    }
    if (a.count("adorn") > 0) tagAt(a.tag("adorn"), Offset(w * 0.5f, h * 0.08f), seg(a.b("adorn"), 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    nightMore(a, ground)
    for (k in 0 until a.count("note")) {
        tagAt(a.tag("note", k), Offset(w * 0.5f, h * (0.2f + 0.08f * k)), seg(a.b("note", k), 0.3f, 1f), back = NightBack, ink = ArtInk.Ember)
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
    drawRect(Brush.verticalGradient(0f to GrainInk.SkyTop, 0.7f to lerp(GrainInk.SkyTop, GrainInk.SkyLow, 0.6f), 1f to GrainInk.SkyLow, endY = ground), size = Size(w, ground))
    // morning light from behind the real grain
    val dawn = Offset(w * 0.78f, ground)
    softGlow(dawn, w * 0.35f, Color(0xFFFFE0A8), 0.45f)
    lightShafts(dawn, -PI.toFloat() / 2f, PI.toFloat() * 0.9f, h * 0.7f, Color(0xFFFFE6B8), 0.5f, t, 8)
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
            softGlow(top, w * 0.12f, ArtInk.Gold, 0.25f * grow)
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

// ───────────── vessels of love, in their order ─────────────

private object VesselInk {
    val BgTop = Color(0xFF1B1E3C)
    val BgLow = Color(0xFF453652)
    val Shelf = Color(0xFF5A3E2A)
    val ShelfLight = Color(0xFF7A5638)
    val Glass = Color(0xFFE8F0F8)
}

/**
 * Glass vessels on a shelf, filled with light to the measure of a love. Beats: cup ×n (`value` 0..1: how full,
 * the first on the right), equal (`side`: that vessel is filled up to equal the first: struck out), first (the
 * first vessel shines above all).
 */
internal fun DrawScope.vesselsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    deepSky(t, h * 0.8f, VesselInk.BgTop, VesselInk.BgLow, starAlpha = 0.6f)
    val shelf = h * 0.8f
    drawRect(VesselInk.Shelf, topLeft = Offset(0f, shelf), size = Size(w, h * 0.035f))
    drawLine(VesselInk.ShelfLight, Offset(0f, shelf), Offset(w, shelf), 1.4f * dp)

    val cups = (0 until a.size).filter { a.kindAt(it) == "cup" }
    val n = max(1, cups.size)
    val cw = w * 0.085f
    val ch = h * 0.42f
    val first = easeOut(a.b("first"))
    for ((k, i) in cups.withIndex()) {
        val p = easeOut(a.beatAt(i))
        if (p <= 0f) continue
        val x = if (n == 1) w * 0.5f else w * (0.82f - 0.64f * k / (n - 1f))
        var level = a.valueAt(i).coerceIn(0.05f, 1f) * easeOut(seg(a.beatAt(i), 0.2f, 1f))
        var tone = ArtInk.Gold
        var strike = 0f
        var drain = 0f
        var sealed = 0f
        for (e in 0 until a.size) {
            if (a.sideAt(e) != k) continue
            if (a.kindAt(e) == "drain") drain = max(drain, a.beatAt(e))
            if (a.kindAt(e) == "seal") sealed = max(sealed, easeOut(a.beatAt(e)))
        }
        if (drain > 0f) {
            level = mix(level, 0.06f, easeInOut(drain))
            tone = lerp(ArtInk.Gold, Color(0xFF7C7A86), easeOut(drain))
        }
        for (e in 0 until a.size) {
            if (a.kindAt(e) != "equal" || a.sideAt(e) != k) continue
            val q = a.beatAt(e)
            level = mix(level, cups.firstOrNull()?.let { a.valueAt(it) } ?: 1f, easeInOut(seg(q, 0f, 0.5f)))
            tone = lerp(ArtInk.Gold, ArtInk.Ember, seg(q, 0f, 0.5f))
            strike = seg(q, 0.5f, 0.9f)
        }
        val body = Rect(x - cw / 2, shelf - ch, x + cw / 2, shelf)
        if (k == 0 && first > 0f) {
            softGlow(body.center, cw * 2.4f, ArtInk.Gold, 0.45f * first)
        }
        val jar = Path().apply { addRoundRect(androidx.compose.ui.geometry.RoundRect(body, CornerRadius(cw * 0.35f, cw * 0.35f))) }
        clipPath(jar) {
            val top = shelf - ch * level * p
            drawRect(Brush.verticalGradient(listOf(tone.copy(alpha = 0.95f), lerp(tone, Color(0xFF8A5A20), 0.4f)), startY = top, endY = shelf), topLeft = Offset(body.left, top), size = Size(cw, shelf - top))
            drawLine(ArtInk.Ivory.copy(alpha = 0.7f), Offset(body.left, top), Offset(body.right, top + 2f * dp * sin(t * 2f + k)), 1.2f * dp)
        }
        drawPath(jar, VesselInk.Glass.copy(alpha = 0.08f * p))
        drawPath(jar, VesselInk.Glass.copy(alpha = 0.65f * p), style = Stroke(1.3f * dp))
        drawLine(VesselInk.Glass.copy(alpha = 0.35f * p), Offset(body.left + cw * 0.2f, body.top + ch * 0.1f), Offset(body.left + cw * 0.2f, body.bottom - ch * 0.15f), 2f * dp, StrokeCap.Round)
        if (strike > 0f) nightCross(body.center + Offset(0f, -ch * 0.2f), cw * 0.45f, strike)
        // what leaks away
        if (drain > 0f && drain < 1f) {
            for (m in 0 until 3) {
                val q = fract(t * 1.2f + m / 3f)
                drawCircle(Color(0xFF9A98A6).copy(alpha = 0.8f * (1f - q)), radius = 1.8f * dp, center = Offset(x + (m - 1) * 4f * dp, shelf + q * h * 0.12f))
            }
        }
        // a sealed vessel: a gold lid with a star, kept safe
        if (sealed > 0f) {
            softGlow(body.center, cw * 1.8f, ArtInk.Gold, 0.35f * sealed)
            val lidY = body.top - (1f - sealed) * h * 0.08f
            drawRoundRect(ArtInk.Brass.copy(alpha = sealed), topLeft = Offset(body.left - 3f * dp, lidY - 7f * dp), size = Size(cw + 6f * dp, 9f * dp), cornerRadius = CornerRadius(3f * dp, 3f * dp))
            drawPath(starPath(Offset(x, lidY - 2.5f * dp), 5f * dp), ArtInk.Gold.copy(alpha = sealed))
        }
        tagAt(a.tagOf(i), Offset(x, shelf - ch - h * 0.07f), seg(a.beatAt(i), 0.3f, 1f), back = NightBack, ink = if (k == 0) ArtInk.Gold else ArtInk.Ivory)
        for (e in 0 until a.size) {
            val kind = a.kindAt(e)
            if ((kind == "equal" || kind == "drain" || kind == "seal") && a.sideAt(e) == k) {
                tagAt(a.tagOf(e), Offset(x, shelf - ch - h * 0.155f - (if (k % 2 == 1) h * 0.075f else 0f)), seg(a.beatAt(e), 0.3f, 1f), back = NightBack, ink = if (kind == "seal") ArtInk.Gold else ArtInk.Ember)
            }
        }
        if (k == 0) tagAt(a.tag("first"), Offset(x, shelf - ch - h * 0.155f), seg(a.b("first"), 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    }
}

// ───────────── a cave the sun turns away from ─────────────

private object CaveInk {
    val SkyTop = Color(0xFF5E93C8)
    val SkyLow = Color(0xFFF2DDB2)
    val Ground = Color(0xFF6E5A44)
    val Rock = Color(0xFF7A6650)
    val RockDark = Color(0xFF5A4A3A)
    val Mouth = Color(0xFF1C1612)
    val Sun = Color(0xFFFFE9A8)
}

/**
 * Day over a hill with a cave. Beats: cave (the cave, quiet inside), sun (the sun crosses the sky and its rays
 * turn away from the cave: what Allah does for His friends beyond the usual), follow (lit steps lead into the
 * cave behind a lamp: those who follow their prophets' guidance), false (a showy spark over a crooked path,
 * struck out: no honour for one who breaks the law).
 */
internal fun DrawScope.caveScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(CaveInk.SkyTop, CaveInk.SkyLow), endY = ground), size = Size(w, ground))
    val sun = a.b("sun")
    val mouth = Offset(w * 0.76f, ground - h * 0.02f)
    // the sun's way across the sky
    val su = 0.15f + 0.7f * easeInOut(sun)
    val sp = cubic(Offset(w * 0.02f, h * 0.55f), Offset(w * 0.25f, h * 0.02f), Offset(w * 0.75f, h * 0.02f), Offset(w * 0.98f, h * 0.5f), su)
    softGlow(sp, w * 0.1f, CaveInk.Sun, 0.8f)
    lightShafts(sp, PI.toFloat() / 2f, PI.toFloat() * 1.6f, w * 0.5f, CaveInk.Sun, 0.55f, t, 10)
    bloom(sp, w * 0.03f, CaveInk.Sun, 0.9f)
    // the hill and its cave
    val hill = Path().apply {
        moveTo(w * 0.48f, ground)
        cubicTo(w * 0.56f, ground - h * 0.42f, w * 0.9f, ground - h * 0.5f, w * 1.05f, ground - h * 0.3f)
        lineTo(w * 1.05f, ground)
        close()
    }
    drawPath(hill, CaveInk.Rock)
    drawPath(hill, CaveInk.RockDark, style = Stroke(1.4f * dp))
    val caveA = if (a.count("cave") > 0) easeOut(a.b("cave")) else 1f
    val calm = 0.25f + 0.5f * seg(sun, 0.4f, 1f)
    drawPath(horseshoe(mouth.x, mouth.y - h * 0.12f, w * 0.05f, w * 0.042f, mouth.y), CaveInk.Mouth.copy(alpha = caveA))
    softGlow(mouth + Offset(0f, -h * 0.08f), w * 0.05f, Color(0xFF9FD2E8), calm * caveA * 0.5f)
    // rays: those that would enter the cave bend away from it
    if (sun > 0f) {
        val ra = easeOut(seg(sun, 0f, 0.3f))
        for (k in 0 until 8) {
            val target = Offset(w * (0.1f + 0.12f * k), ground - (if (k >= 5) h * 0.18f else 0f))
            val dx = target.x - mouth.x
            val near = (1f - kotlin.math.abs(dx) / (w * 0.22f)).coerceIn(0f, 1f)
            val ctrl = Offset(mix(sp.x, target.x, 0.5f) + (if (dx >= 0f) 1f else -1f) * near * w * 0.25f, mix(sp.y, target.y, 0.5f))
            val end = Offset(target.x + (if (dx >= 0f) 1f else -1f) * near * w * 0.12f, target.y - near * h * 0.12f)
            val path = Path().apply {
                moveTo(sp.x, sp.y)
                quadraticBezierTo(ctrl.x, ctrl.y, end.x, end.y)
            }
            drawPath(path, CaveInk.Sun.copy(alpha = 0.35f * ra), style = Stroke(2f * dp, cap = StrokeCap.Round))
        }
        tagAt(a.tag("sun"), mouth + Offset(0f, -h * 0.3f), seg(sun, 0.4f, 1f), back = NightBack, ink = ArtInk.Gold)
    }
    drawRect(CaveInk.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))
    tagAt(a.tag("cave"), mouth + Offset(-w * 0.02f, h * 0.1f), seg(a.b("cave"), 0.3f, 1f), back = NightBack, ink = ArtInk.Ivory)
    // lit steps behind a lamp, into the cave
    val follow = easeOut(a.b("follow"))
    if (follow > 0f) {
        val lampP = Offset(w * 0.3f, ground + h * 0.02f)
        drawLine(Color(0xFF3A2A1E), lampP, lampP + Offset(0f, -h * 0.2f), 2.4f * dp)
        lantern(lampP + Offset(0f, -h * 0.2f - 0.4f * w * 0.02f), w * 0.02f, follow, 0.05f * sin(t * 9f))
        for (k in 0 until 10) {
            val f = k / 9f
            if (f > follow) continue
            val c = Offset(mix(w * 0.3f, mouth.x - w * 0.02f, f), ground + h * 0.06f - f * h * 0.05f + (if (k % 2 == 0) 2.5f else -2.5f) * dp)
            drawOval(ArtInk.Ivory.copy(alpha = 0.85f), topLeft = c - Offset(3.5f * dp, 1.6f * dp), size = Size(7f * dp, 3.2f * dp))
        }
        tagAt(a.tag("follow"), Offset(w * 0.42f, ground - h * 0.3f), seg(a.b("follow"), 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    }
    // a showy spark over a crooked path, struck out
    val fake = a.b("false")
    if (fake > 0f) {
        val q = easeOut(seg(fake, 0f, 0.4f))
        val c = Offset(w * 0.12f, ground - h * 0.32f)
        val path = Path().apply {
            moveTo(w * 0.02f, ground + h * 0.12f)
            lineTo(w * 0.08f, ground + h * 0.04f)
            lineTo(w * 0.04f, ground - h * 0.04f)
            lineTo(w * 0.12f, ground - h * 0.12f)
        }
        drawPath(path, ArtInk.Ember.copy(alpha = 0.6f * q), style = Stroke(1.4f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))))
        for (m in 0 until 8) {
            val ang = (m * 45f + t * 90f) * PI.toFloat() / 180f
            drawLine(lerp(ArtInk.Ember, ArtInk.Gold, 0.4f).copy(alpha = q), c, c + Offset(cos(ang), sin(ang)) * (10f + 6f * sin(t * 8f + m)) * dp, 1.6f * dp, StrokeCap.Round)
        }
        nightCross(c, 14f * dp, seg(fake, 0.5f, 0.9f))
        tagAt(a.tag("false"), c + Offset(w * 0.06f, -h * 0.12f), seg(fake, 0.3f, 1f), back = NightBack, ink = ArtInk.Ember)
    }
}

/**
 * More of the night sky. Beats: trumpet (rings spread through the sky), ring ×n (spans of time, one inside the
 * other), name ×n (a word rises from the book into the sky), reject ×n (a word set aside, struck out), smoke
 * (smoke covers the land), sunwest (the sun rises in the west), sink (the land sinks in three places), fire (a
 * fire comes from one side and drives forward), journey (a trail of light by night between two places), ascend
 * (the light rises up through the heavens), fifty (fifty small lights become five).
 */
private fun DrawScope.nightMore(a: ArtCtx, ground: Float) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    // a sound spreading through the sky
    val trumpet = easeOut(a.b("trumpet"))
    if (trumpet > 0f) {
        clipRect(bottom = ground) {
            for (k in 0 until 4) {
                val q = fract(t * 0.3f + k / 4f)
                drawCircle(ArtInk.Ivory.copy(alpha = 0.4f * trumpet * (1f - q)), radius = q * w * 1.1f, center = Offset(w * 1.02f, -h * 0.05f), style = Stroke(1.4f * dp))
            }
        }
        tagAt(a.tag("trumpet"), Offset(w * 0.8f, h * 0.17f), seg(a.b("trumpet"), 0.3f, 1f), back = NightBack, ink = ArtInk.Ivory)
    }
    // spans of time, one inside the other
    val rings = a.count("ring")
    if (rings > 0) {
        val c = Offset(w * 0.5f, h * 0.55f)
        for (k in 0 until rings) {
            val p = easeOut(a.b("ring", k))
            if (p <= 0f) continue
            val r = h * (0.4f - 0.065f * k) * (0.85f + 0.15f * p)
            drawCircle(ArtInk.Gold.copy(alpha = 0.12f * p), radius = r, center = c)
            drawCircle(lerp(ArtInk.Ivory, ArtInk.Gold, k / max(1f, rings - 1f)).copy(alpha = 0.8f * p), radius = r, center = c, style = Stroke(1.6f * dp))
            val ang = (-30f - 22f * k) * PI.toFloat() / 180f
            val at = c + Offset(cos(ang) * r, sin(ang) * r)
            drawCircle(ArtInk.Gold.copy(alpha = p), radius = 2.6f * dp, center = at)
            tagAt(a.tag("ring", k), Offset(if (k % 2 == 0) w * 0.84f else w * 0.16f, h * (0.12f + 0.085f * k)), seg(a.b("ring", k), 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
        }
    }
    // words that rise from the book into the sky, and words set aside
    for (k in 0 until a.count("name")) {
        val p = a.b("name", k)
        if (p <= 0f) continue
        val q = easeOut(p)
        val from = Offset(w * 0.5f, h * 0.22f)
        val to = Offset(w * (0.2f + 0.6f * k), h * 0.17f)
        tagAt(a.tag("name", k), Offset(mix(from.x, to.x, q), mix(from.y, to.y, q)), seg(p, 0f, 0.4f), back = NightBack, ink = ArtInk.Gold)
    }
    for (k in 0 until a.count("reject")) {
        val p = a.b("reject", k)
        if (p <= 0f) continue
        val tl = a.tag("reject", k)
        val c = Offset(if (k % 2 == 0) w * 0.8f else w * 0.2f, h * (0.5f + 0.1f * (k / 2)))
        tagAt(tl, c, seg(p, 0f, 0.4f), back = NightBack, ink = ArtInk.Ember)
        if (tl != null) {
            val half = tl.size.width / 2f + 3f * dp
            val st = seg(p, 0.4f, 0.9f)
            drawLine(ArtInk.Ember, Offset(c.x - half, c.y), Offset(c.x - half + 2 * half * st, c.y), 1.5f * dp, StrokeCap.Round)
        }
    }
    // smoke over the land
    val smoke = easeOut(a.b("smoke"))
    if (smoke > 0f) {
        for (m in 0 until 12) {
            val x = fract(m * 0.137f + t * 0.015f * (1 + m % 3)) * w * 1.3f - w * 0.15f
            val y = ground - h * (0.04f + 0.1f * (m % 4)) * smoke
            val r = w * (0.12f + 0.03f * (m % 3))
            softGlow(Offset(x, y), r, Color(0xFF8A8494), 0.32f * smoke)
        }
        tagAt(a.tag("smoke"), Offset(w * 0.2f, ground - h * 0.32f), seg(a.b("smoke"), 0.3f, 1f), back = NightBack, ink = ArtInk.Ivory)
    }
    // the sun rising in the west (on the left, where it always set)
    val sw = easeOut(a.b("sunwest"))
    if (sw > 0f) {
        val c = Offset(w * 0.12f, mix(ground + h * 0.05f, ground - h * 0.12f, sw))
        clipRect(bottom = ground) {
            softGlow(c, w * 0.18f, Color(0xFFFFD27A), 0.7f * sw)
            drawCircle(Color(0xFFFFE3A0), radius = w * 0.045f, center = c)
        }
        tagAt(a.tag("sunwest"), Offset(w * 0.24f, ground - h * 0.24f), seg(a.b("sunwest"), 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    }
    // the land sinking in three places
    val sink = easeOut(a.b("sink"))
    if (sink > 0f) {
        for ((k, fx) in listOf(0.8f, 0.2f, 0.5f).withIndex()) {
            val q = easeOut(seg(a.b("sink"), k * 0.2f, k * 0.2f + 0.6f))
            if (q <= 0f) continue
            drawOval(Color(0xFF05040A), topLeft = Offset(w * fx - w * 0.06f * q, ground - h * 0.01f), size = Size(w * 0.12f * q, h * 0.08f * q))
            drawOval(ArtInk.Ember.copy(alpha = 0.5f * q), topLeft = Offset(w * fx - w * 0.06f * q, ground - h * 0.01f), size = Size(w * 0.12f * q, h * 0.08f * q), style = Stroke(1.2f * dp))
        }
        tagAt(a.tag("sink"), Offset(w * 0.5f, ground - h * 0.12f), seg(a.b("sink"), 0.4f, 1f), back = NightBack, ink = ArtInk.Ember)
    }
    // a fire coming from one side and driving on
    val fire = a.b("fire")
    if (fire > 0f) {
        val front = mix(w * 1.05f, w * 0.35f, easeInOut(fire))
        for (m in 0 until 14) {
            val x = front + (m % 7) * w * 0.1f
            val fh = h * (0.08f + 0.05f * sin(t * 6f + m))
            val path = Path().apply {
                moveTo(x - w * 0.03f, ground)
                quadraticBezierTo(x - w * 0.02f, ground - fh * 0.6f, x, ground - fh)
                quadraticBezierTo(x + w * 0.02f, ground - fh * 0.6f, x + w * 0.03f, ground)
                close()
            }
            drawPath(path, lerp(ArtInk.Ember, Color(0xFFFFC46A), (m % 3) / 3f).copy(alpha = 0.85f))
        }
        for (k in 0 until 6) {
            val x = front - w * (0.06f + 0.05f * k) - fract(t * 0.4f + k * 0.17f) * w * 0.08f
            if (x < 0f) continue
            drawCircle(ArtInk.Ivory.copy(alpha = 0.8f), radius = 2f * dp, center = Offset(x, ground - h * 0.03f))
        }
        tagAt(a.tag("fire"), Offset(w * 0.62f, ground - h * 0.22f), seg(fire, 0.3f, 1f), back = NightBack, ink = ArtInk.Ember)
    }
    // a trail of light by night between two places
    val jr = a.b("journey")
    if (jr > 0f) {
        val from = Offset(w * 0.88f, ground - h * 0.02f)
        val to = Offset(w * 0.14f, ground - h * 0.02f)
        val mid = Offset(w * 0.51f, ground - h * 0.3f)
        // the two places: a cube and a dome, as small dark shapes
        drawRect(Color(0xFF0A0A12), topLeft = Offset(from.x - w * 0.025f, from.y - h * 0.06f), size = Size(w * 0.05f, h * 0.06f))
        drawRect(Color(0xFFC9A54A).copy(alpha = 0.7f), topLeft = Offset(from.x - w * 0.025f, from.y - h * 0.045f), size = Size(w * 0.05f, h * 0.008f))
        drawArc(Color(0xFFB89A4A), 180f, 180f, true, topLeft = Offset(to.x - w * 0.03f, to.y - h * 0.09f), size = Size(w * 0.06f, h * 0.06f))
        drawRect(Color(0xFF2A2440), topLeft = Offset(to.x - w * 0.035f, to.y - h * 0.06f), size = Size(w * 0.07f, h * 0.06f))
        val q = easeInOut(seg(jr, 0f, 0.9f))
        val path = Path()
        for (k in 0..30) {
            val u = q * k / 30f
            val pt = Offset((1 - u) * (1 - u) * from.x + 2 * (1 - u) * u * mid.x + u * u * to.x, (1 - u) * (1 - u) * (from.y - h * 0.06f) + 2 * (1 - u) * u * mid.y + u * u * (to.y - h * 0.09f))
            if (k == 0) path.moveTo(pt.x, pt.y) else path.lineTo(pt.x, pt.y)
        }
        drawPath(path, ArtInk.Gold.copy(alpha = 0.3f), style = Stroke(7f * dp, cap = StrokeCap.Round))
        drawPath(path, ArtInk.Gold, style = Stroke(1.6f * dp, cap = StrokeCap.Round))
        // the Prophet ﷺ is shown only as a lamp spreading light, at the head of the trail
        if (a.b("ascend") <= 0f) {
            val u = q
            val head = Offset((1 - u) * (1 - u) * from.x + 2 * (1 - u) * u * mid.x + u * u * to.x, (1 - u) * (1 - u) * (from.y - h * 0.06f) + 2 * (1 - u) * u * mid.y + u * u * (to.y - h * 0.09f))
            sirajMunir(head, 3.2f * dp, easeOut(seg(jr, 0f, 0.15f)), t)
        }
        tagAt(a.tag("journey"), Offset(w * 0.51f, ground - h * 0.4f), seg(jr, 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    }
    // the light rises up through the heavens, layer after layer
    val asc = a.b("ascend")
    if (asc > 0f) {
        val base = Offset(w * 0.14f, ground - h * 0.12f)
        val top = mix(base.y, h * 0.04f, easeInOut(asc))
        drawLine(ArtInk.Gold.copy(alpha = 0.3f), base, Offset(base.x, top), 7f * dp, StrokeCap.Round)
        drawLine(ArtInk.Gold, base, Offset(base.x, top), 1.6f * dp, StrokeCap.Round)
        for (k in 0 until 7) {
            val y = mix(base.y - h * 0.08f, h * 0.08f, k / 6f)
            if (top > y) continue
            // each heaven lights up as the light passes through it
            drawLine(ArtInk.Gold.copy(alpha = 0.18f), Offset(base.x - w * 0.07f, y), Offset(base.x + w * 0.07f, y), 5f * dp, StrokeCap.Round)
            drawLine(ArtInk.Ivory.copy(alpha = 0.7f), Offset(base.x - w * 0.06f, y), Offset(base.x + w * 0.06f, y), 1.2f * dp, StrokeCap.Round)
        }
        sirajMunir(Offset(base.x, max(top, h * 0.1f)), 3.2f * dp, 1f, t)
        tagAt(a.tag("ascend"), Offset(w * 0.32f, h * 0.2f), seg(asc, 0.3f, 1f), back = NightBack, ink = ArtInk.Gold)
    }
    // fifty become five
    val ff = a.b("fifty")
    if (ff > 0f) {
        val appear = easeOut(seg(ff, 0f, 0.3f))
        val keep = easeInOut(seg(ff, 0.4f, 1f))
        for (k in 0 until 50) {
            val col = k % 10
            val row = k / 10
            val stays = k in listOf(12, 15, 22, 25, 28)
            val c0 = Offset(w * (0.48f + 0.042f * col), h * (0.14f + 0.055f * row))
            val c1 = Offset(w * (0.52f + 0.08f * listOf(12, 15, 22, 25, 28).indexOf(k)), h * 0.26f)
            val alpha = appear * (if (stays) 1f else 1f - keep)
            if (alpha <= 0.01f) continue
            val c = if (stays) Offset(mix(c0.x, c1.x, keep), mix(c0.y, c1.y, keep)) else c0
            val r = (2f + (if (stays) 3f * keep else 0f)) * dp
            if (stays && keep > 0f) softGlow(c, r * 4f, ArtInk.Gold, 0.6f * keep)
            drawCircle(ArtInk.Ivory.copy(alpha = alpha), radius = r, center = c)
        }
        tagAt(a.tag("fifty", 0, 0), Offset(w * 0.78f, h * 0.42f), seg(ff, 0.1f, 0.4f) * (1f - seg(ff, 0.6f, 0.8f)), back = NightBack, ink = ArtInk.Ivory)
        tagAt(a.tag("fifty", 0, 1), Offset(w * 0.78f, h * 0.42f), seg(ff, 0.7f, 1f), back = NightBack, ink = ArtInk.Gold)
    }
}
