package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 42 (2026-10-27): «بصائر»، الجزء الثالث، «نصوص وشبهات». The sun on its unbroken course under the layered heavens;
 * a boulder that moves a little with each prayer and answers that come late; a son who serves another house and a
 * student who never studied; a balance true to a grain; statues of kings and the empty plinth of the prophets; two
 * meters, civilisation and culture. People are faceless figures; prophets are small lights; he ﷺ is only light.
 */

private val Back42 = Color(0xE6181420)

// ═════════════ 1. the sun on its course (bs-sujud) ═════════════

private object SunC42 {
    val Space = Color(0xFF0C1024)
    val SpaceLow = Color(0xFF1E2240)
    val Sun = Color(0xFFFFD27A)
    val Orbit = Color(0xFF6A7AB0)
    val Layer = Color(0xFFBFC8F0)
    val Hill = Color(0xFF3A3040)
    val Tree = Color(0xFF3A5A40)
    val Figure = Color(0xFFE8DCC8)
    val Dusk = Color(0xFFE8906A)
}

/**
 * Beats: sunorbit (the sun runs its orbit and never stops), ceilingall (layer upon layer of heavens arch above all of
 * it: wherever the sun is, it is beneath them), allbow (stars, a mountain and a tree each pulse softly: all that is in
 * the heavens and earth bows), behindhill (on a hill an onlooker watches the sun go down behind a far ridge: what the eye
 * sees).
 */
internal fun DrawScope.sunbowScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sunorbit" to Shot(Offset(w * 0.5f, h * 0.48f), 1.15f),
        "ceilingall" to Shot(Offset(w * 0.5f, h * 0.4f), 1.0f),
        "allbow" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "behindhill" to Shot(Offset(w * 0.3f, h * 0.75f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { sunCourse(a) }
}

private fun DrawScope.sunCourse(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val so = a.b("sunorbit")
    val ca = a.b("ceilingall")
    val ab = a.b("allbow")
    val bh = a.b("behindhill")
    nightSky(t, SunC42.Space, SunC42.SpaceLow, h, starAlpha = 0.8f)
    val c = Offset(w * 0.5f, h * 0.48f)
    val rx = w * 0.3f
    val ry = h * 0.12f
    val of = easeOut(seg(so, 0f, 0.3f)) * (1f - 0.6f * seg(bh, 0f, 0.4f))
    if (of > 0f) {
        drawOval(SunC42.Orbit, Offset(c.x - rx, c.y - ry), Size(rx * 2f, ry * 2f), alpha = of * 0.7f, style = Stroke(1.5f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))))
        val an = t * 0.5f
        val sun = c + Offset(cos(an) * rx, sin(an) * ry)
        softGlow(sun, w * 0.07f, SunC42.Sun, 0.7f * of)
        drawCircle(SunC42.Sun, w * 0.03f, sun, alpha = of)
        // its trail: never a stop
        for (k in 1 until 8) {
            val b = an - k * 0.12f
            drawCircle(SunC42.Sun, w * 0.008f, c + Offset(cos(b) * rx, sin(b) * ry), alpha = of * 0.5f * (1f - k / 8f))
        }
    }
    // the heavens above everything
    if (ca > 0f) {
        val f = easeOut(seg(ca, 0f, 0.5f)) * (1f - 0.5f * seg(bh, 0f, 0.4f))
        for (k in 0 until 7) {
            val r = w * (0.48f + 0.06f * k)
            val g = seg(ca, 0.05f * k, 0.35f + 0.05f * k)
            drawArc(SunC42.Layer, 200f, 140f, false, Offset(c.x - r, c.y + h * 0.1f - r), Size(r * 2f, r * 2f), alpha = f * g * (0.5f - 0.05f * k), style = Stroke(2f * dp))
        }
        softGlow(Offset(w * 0.5f, 0f), w * 0.5f, Color(0xFFFFF4D8), 0.25f * f)
    }
    // everything bows
    if (ab > 0f) {
        val f = easeOut(seg(ab, 0f, 0.3f)) * (1f - 0.5f * seg(bh, 0f, 0.4f))
        val base = h * 0.86f
        drawPath(Path().apply { moveTo(w * 0.55f, base); lineTo(w * 0.72f, h * 0.68f); lineTo(w * 0.9f, base); close() }, SunC42.Hill, alpha = f)
        val tree = Offset(w * 0.35f, base)
        drawLine(Color(0xFF5A4030), tree, tree + Offset(0f, -h * 0.1f), w * 0.012f, alpha = f)
        drawCircle(SunC42.Tree, w * 0.045f, tree + Offset(w * 0.01f * sin(t), -h * 0.13f), alpha = f)
        drawRect(Color(0xFF241E2A), Offset(0f, base), Size(w, h - base), alpha = f)
        val pulse = 0.5f + 0.5f * sin(t * 2f)
        for ((k, p) in listOf(Offset(w * 0.72f, h * 0.68f), tree + Offset(0f, -h * 0.13f), Offset(w * 0.2f, h * 0.2f), Offset(w * 0.82f, h * 0.15f)).withIndex()) {
            val g = seg(ab, 0.2f + 0.12f * k, 0.45f + 0.12f * k)
            softGlow(p, w * (0.03f + 0.02f * pulse), ArtInk.Gold, 0.45f * f * g)
        }
    }
    // what the eye sees: the sun goes down behind the ridge
    if (bh > 0f) {
        val f = easeOut(seg(bh, 0f, 0.3f))
        val sky = h * 0.72f
        drawRect(Brush.verticalGradient(listOf(Color(0xFF3A3A6A), SunC42.Dusk), startY = h * 0.4f, endY = sky), Offset(0f, h * 0.4f), Size(w, sky - h * 0.4f), alpha = f)
        val down = easeInOut(seg(bh, 0.2f, 0.9f))
        val sun = Offset(w * 0.72f, h * 0.55f + h * 0.15f * down)
        softGlow(sun, w * 0.08f, SunC42.Sun, 0.7f * f)
        drawCircle(SunC42.Sun, w * 0.04f, sun, alpha = f)
        drawPath(Path().apply { moveTo(w * 0.45f, sky); quadraticTo(w * 0.72f, h * 0.6f, w, h * 0.66f); lineTo(w, sky); close() }, SunC42.Hill, alpha = f)
        drawRect(Color(0xFF2A2230), Offset(0f, sky), Size(w, h - sky), alpha = f)
        drawPath(Path().apply { moveTo(0f, h * 0.86f); quadraticTo(w * 0.2f, h * 0.78f, w * 0.42f, h * 0.86f); lineTo(w * 0.42f, h); lineTo(0f, h); close() }, Color(0xFF1E1822), alpha = f)
        figure(Offset(w * 0.22f, h * 0.81f), h * 0.12f, SunC42.Figure, f, lean = -0.1f)
    }
}

// ═════════════ 2. the boulder that moved with each prayer (bs-dua) ═════════════

private object CaveC42 {
    val Sky = Color(0xFF1C1E34)
    val SkyLow = Color(0xFF3A3450)
    val Rock = Color(0xFF5A4E48)
    val Boulder = Color(0xFF7A6A5E)
    val Inside = Color(0xFF120E14)
    val Prayer = Color(0xFFFFE8B0)
    val Sea = Color(0xFF2E5A80)
    val Tent = Color(0xFF4A3A3A)
    val Dome = Color(0xFFD8B87A)
}

/**
 * Beats: boulder (a cave shut by a boulder; three small lights rise from inside, and the stone slides a little with
 * each), delayed (a long arc from a prayer to its answer, the spark travelling slowly: «أُجِيبَتْ دَعْوَتُكُمَا»),
 * centuryanswer (Ibrahim's light at one end of a line of centuries, his ﷺ light at the other: «إِبْرَاهِيمُ»),
 * tatarmosques (dark tents on a plain; a century later they are domes).
 */
internal fun DrawScope.caveboulderScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "boulder" to Shot(Offset(w * 0.28f, h * 0.55f), 1.35f),
        "delayed" to Shot(Offset(w * 0.6f, h * 0.3f), 1.15f),
        "centuryanswer" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "tatarmosques" to Shot(Offset(w * 0.65f, h * 0.78f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { caveNight(a) }
}

private fun DrawScope.caveNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bo = a.b("boulder")
    val de = a.b("delayed")
    val ca = a.b("centuryanswer")
    val tm = a.b("tatarmosques")
    val ground = h * 0.86f
    nightSky(t, CaveC42.Sky, CaveC42.SkyLow, ground, starAlpha = 0.6f)
    drawRect(Color(0xFF2A2428), Offset(0f, ground), Size(w, h - ground))
    // the cave and the boulder
    val cf = easeOut(seg(bo, 0f, 0.3f)) * (1f - 0.6f * seg(ca, 0f, 0.4f))
    if (cf > 0f) {
        val mouth = Offset(w * 0.28f, h * 0.66f)
        drawPath(Path().apply { moveTo(w * 0.02f, ground); lineTo(w * 0.1f, h * 0.42f); lineTo(w * 0.36f, h * 0.38f); lineTo(w * 0.52f, ground); close() }, CaveC42.Rock, alpha = cf)
        drawOval(CaveC42.Inside, Offset(mouth.x - w * 0.09f, mouth.y - h * 0.1f), Size(w * 0.18f, h * 0.2f), alpha = cf)
        var slide = 0f
        for (k in 0 until 3) {
            val rise = seg(bo, 0.15f + 0.25f * k, 0.35f + 0.25f * k)
            val p = mouth + Offset(w * (-0.03f + 0.03f * k), -h * 0.2f * rise)
            drawCircle(CaveC42.Prayer, w * 0.008f, p, alpha = cf * bump(rise))
            slide += easeOut(seg(bo, 0.3f + 0.25f * k, 0.45f + 0.25f * k)) / 3f
        }
        softGlow(mouth, w * 0.08f * slide, CaveC42.Prayer, 0.5f * slide * cf)
        drawCircle(CaveC42.Boulder, w * 0.1f, mouth + Offset(w * 0.17f * slide, h * 0.01f), alpha = cf)
    }
    // answered, but later
    if (de > 0f) {
        val f = easeOut(seg(de, 0f, 0.3f)) * (1f - 0.6f * seg(ca, 0f, 0.4f))
        val from = Offset(w * 0.4f, h * 0.36f)
        val to = Offset(w * 0.86f, h * 0.36f)
        val arc = Path().apply { moveTo(from.x, from.y); quadraticTo(w * 0.63f, h * 0.08f, to.x, to.y) }
        drawPath(arc, ArtInk.BrassLight, alpha = f * 0.6f, style = Stroke(1.5f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp))))
        val u = easeInOut(seg(de, 0.2f, 0.95f))
        val p = Offset(mix(from.x, to.x, u), from.y - 4f * u * (1f - u) * (from.y - h * 0.08f) * 0.5f)
        drawCircle(CaveC42.Prayer, w * 0.01f, p, alpha = f)
        softGlow(p, w * 0.03f, CaveC42.Prayer, 0.5f * f)
        if (u > 0.97f) softGlow(to, w * 0.08f, ArtInk.Gold, 0.7f * f)
        tagAt(a.tag("delayed"), Offset(w * 0.63f, h * 0.27f), seg(de, 0.3f, 0.55f) * (1f - seg(ca, 0f, 0.3f)), back = Back42, ink = ArtInk.Gold)
    }
    // Ibrahim's prayer, answered after centuries
    if (ca > 0f) {
        val f = easeOut(seg(ca, 0f, 0.3f)) * (1f - 0.6f * seg(tm, 0f, 0.4f))
        val left = Offset(w * 0.14f, h * 0.45f)
        val right = Offset(w * 0.86f, h * 0.45f)
        nabiLight14(left, w * 0.01f, f, t, a.tag("centuryanswer"), left + Offset(0f, -h * 0.1f))
        val run = easeInOut(seg(ca, 0.2f, 0.85f))
        drawLine(ArtInk.BrassLight, left + Offset(w * 0.04f, 0f), left + Offset(w * 0.04f, 0f) + (right - left - Offset(w * 0.08f, 0f)) * run, 2f * dp, alpha = f)
        for (k in 0 until 20) {
            val x = left.x + w * 0.04f + (right.x - left.x - w * 0.08f) * k / 19f
            if (k / 19f <= run) drawLine(ArtInk.BrassLight, Offset(x, left.y - h * 0.01f), Offset(x, left.y + h * 0.01f), 1.2f * dp, alpha = f * 0.8f)
        }
        sirajMunir(right, w * 0.012f, f * seg(ca, 0.8f, 1f), t, nameAt = right + Offset(0f, -h * 0.1f))
    }
    // the Tatars' tents, and a century later, domes
    if (tm > 0f) {
        val f = easeOut(seg(tm, 0f, 0.3f))
        val turn = easeInOut(seg(tm, 0.4f, 0.85f))
        for (k in 0 until 6) {
            val x = w * (0.4f + 0.1f * k)
            val base = ground
            drawPath(Path().apply { moveTo(x - w * 0.035f, base); lineTo(x, base - h * 0.08f); lineTo(x + w * 0.035f, base); close() }, CaveC42.Tent, alpha = f * (1f - turn))
            domed29(Offset(x, base), w * 0.045f, Color(0xFFE8D8B8), CaveC42.Dome, f * turn)
        }
        softGlow(Offset(w * 0.65f, ground - h * 0.06f), w * 0.2f, ArtInk.Gold, 0.3f * f * turn)
    }
}

// ═════════════ 3. whom did you work for? (bs-salihat) ═════════════

private object WageC42 {
    val Sky = Color(0xFFB8D0E8)
    val SkyLow = Color(0xFFF0E4CC)
    val Ground = Color(0xFFC8A87A)
    val HouseA = Color(0xFFD8C8A8)
    val HouseB = Color(0xFF9AA8B8)
    val Roof = Color(0xFF8A5A3A)
    val Figure = Color(0xFF2E2620)
    val Coin = Color(0xFFD8B04A)
    val Card = Color(0xFFFBF7EC)
    val Red = Color(0xFFE0584A)
    val Star = Color(0xFFE8B030)
    val Brick = Color(0xFFB0805A)
}

/**
 * Beats: serveother (a son leaves his family's house and works at another; his pay comes from that house),
 * failgrade (a school report: good conduct all year, starred, and the exam left empty — «رَسَبَ»), dustworks (a wall of
 * bricks with no foundation blows away as dust).
 */
internal fun DrawScope.wagesroomScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "serveother" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "failgrade" to Shot(Offset(w * 0.5f, h * 0.35f), 1.3f),
        "dustworks" to Shot(Offset(w * 0.5f, h * 0.72f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { wagesDay(a) }
}

private fun DrawScope.wagesDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val so = a.b("serveother")
    val fg = a.b("failgrade")
    val dw = a.b("dustworks")
    val ground = h * 0.8f
    drawRect(Brush.verticalGradient(listOf(WageC42.Sky, WageC42.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(WageC42.Ground, Offset(0f, ground), Size(w, h - ground))
    // two houses: the one that raised him, the one he served
    val hf = easeOut(seg(so, 0f, 0.3f)) * (1f - 0.6f * seg(fg, 0f, 0.4f))
    if (hf > 0f) {
        for ((k, x) in listOf(w * 0.2f, w * 0.8f).withIndex()) {
            drawRect(if (k == 0) WageC42.HouseA else WageC42.HouseB, Offset(x - w * 0.1f, ground - h * 0.2f), Size(w * 0.2f, h * 0.2f), alpha = hf)
            drawPath(Path().apply { moveTo(x - w * 0.12f, ground - h * 0.2f); lineTo(x, ground - h * 0.3f); lineTo(x + w * 0.12f, ground - h * 0.2f); close() }, WageC42.Roof, alpha = hf)
            drawRect(Color(0xFF5A4030), Offset(x - w * 0.025f, ground - h * 0.1f), Size(w * 0.05f, h * 0.1f), alpha = hf)
        }
        val go = easeInOut(seg(so, 0.15f, 0.55f))
        val foot = Offset(w * (0.28f + 0.42f * go), ground)
        figure(foot, h * 0.16f, WageC42.Figure, hf, walk = if (so in 0.15f..0.55f) t * 6f else 0f, arms = if (so > 0.55f) 0.5f + 0.3f * sin(t * 4f) else 0f)
        for (k in 0 until 3) {
            val u = easeInOut(seg(so, 0.6f + 0.1f * k, 0.8f + 0.1f * k))
            val from = Offset(w * 0.8f, ground - h * 0.12f)
            val p = from + (foot + Offset(0f, -h * 0.1f) - from) * u
            drawCircle(WageC42.Coin, w * 0.012f, p, alpha = hf * seg(so, 0.6f + 0.1f * k, 0.65f + 0.1f * k))
        }
    }
    // the report: good conduct, no study
    if (fg > 0f) {
        val f = easeOut(seg(fg, 0f, 0.3f)) * (1f - 0.6f * seg(dw, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.35f)
        drawRect(WageC42.Card, Offset(c.x - w * 0.2f, c.y - h * 0.18f), Size(w * 0.4f, h * 0.36f), alpha = f)
        drawRect(Color(0xFF8A7A5A), Offset(c.x - w * 0.2f, c.y - h * 0.18f), Size(w * 0.4f, h * 0.36f), alpha = f, style = Stroke(2f * dp))
        for (k in 0 until 3) {
            val s = seg(fg, 0.15f + 0.08f * k, 0.3f + 0.08f * k)
            drawPath(starPath(Offset(c.x + w * (0.1f - 0.06f * k), c.y - h * 0.1f), w * 0.02f * s), WageC42.Star, alpha = f)
        }
        drawLine(Color(0xFF9A9080), Offset(c.x - w * 0.15f, c.y - h * 0.1f), Offset(c.x - w * 0.05f, c.y - h * 0.1f), 1.5f * dp, alpha = f)
        drawLine(Color(0xFF9A9080), Offset(c.x - w * 0.15f, c.y), Offset(c.x - w * 0.05f, c.y), 1.5f * dp, alpha = f)
        drawRect(Color(0xFFE8E0D0), Offset(c.x + w * 0.0f, c.y - h * 0.025f), Size(w * 0.12f, h * 0.05f), alpha = f)
        cross21(Offset(c.x + w * 0.06f, c.y), w * 0.025f, WageC42.Red, seg(fg, 0.5f, 0.7f) * f, 3f * dp)
        tagAt(a.tag("failgrade"), c + Offset(0f, h * 0.11f), seg(fg, 0.65f, 0.9f) * (1f - seg(dw, 0f, 0.4f)), back = Color(0xE6501818), ink = Color(0xFFFFE0D8))
    }
    // works with no foundation, blown away
    if (dw > 0f) {
        val f = easeOut(seg(dw, 0f, 0.3f))
        val blow = easeInOut(seg(dw, 0.35f, 1f))
        for (r in 0 until 3) for (k in 0 until 5) {
            val i = r * 5 + k
            val home = Offset(w * (0.35f + 0.065f * k) + (if (r % 2 == 1) w * 0.03f else 0f), ground - h * 0.045f * (r + 1))
            val drift = Offset(w * (0.3f + 0.05f * (i % 4)) * blow, -h * (0.1f + 0.03f * (i % 3)) * blow + h * 0.02f * sin(t * 3f + i) * blow)
            drawRect(WageC42.Brick, home + drift, Size(w * 0.06f * (1f - 0.7f * blow), h * 0.04f * (1f - 0.7f * blow)), alpha = f * (1f - blow))
            for (j in 0 until 3) drawCircle(WageC42.Brick, w * 0.003f, home + drift + Offset(w * 0.02f * j, h * 0.01f * j), alpha = f * blow * (1f - blow) * 2f)
        }
    }
}

// ═════════════ 4. a balance true to a grain (bs-jahannam) ═════════════

private object ScaleC42 {
    val Sky = Color(0xFF14162A)
    val SkyLow = Color(0xFF2A2640)
    val Brass = Color(0xFFD8B04A)
    val Grain = Color(0xFFF6E8A8)
    val Heart = Color(0xFFF2C45A)
    val Land = Color(0xFF3A3236)
    val House = Color(0xFFB8A080)
}

/**
 * Beats: exactscale (a great balance settles level: the reward fits the deed, «وِفَاقٌ»), grain (a single small grain on
 * one pan, and the beam answers it), praiseheart (a heart fills with light: he will praise His justice), messengersfirst
 * (before any balance, small lights come down over a town: no punishment without a messenger).
 */
internal fun DrawScope.justscaleScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "exactscale" to Shot(Offset(w * 0.5f, h * 0.42f), 1.15f),
        "mustardgrain" to Shot(Offset(w * 0.32f, h * 0.5f), 1.5f),
        "praiseheart" to Shot(Offset(w * 0.5f, h * 0.45f), 1.2f),
        "messengersfirst" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { scaleNight(a) }
}

private fun DrawScope.scaleNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val es = a.b("exactscale")
    val gr = a.b("mustardgrain")
    val ph = a.b("praiseheart")
    val mf = a.b("messengersfirst")
    nightSky(t, ScaleC42.Sky, ScaleC42.SkyLow, h, starAlpha = 0.6f)
    val sf = easeOut(seg(es, 0f, 0.35f)) * (1f - 0.6f * seg(ph, 0f, 0.4f)) * (1f - 0.8f * seg(mf, 0f, 0.3f))
    if (sf > 0f) {
        val c = Offset(w * 0.5f, h * 0.3f)
        val wobble = (1f - easeOut(seg(es, 0.2f, 0.8f))) * 0.2f * sin(t * 3f)
        val tilt = wobble + 0.06f * easeOut(seg(gr, 0.4f, 0.7f))
        drawLine(ScaleC42.Brass, c, Offset(c.x, h * 0.75f), 4f * dp, alpha = sf)
        drawRect(ScaleC42.Brass, Offset(c.x - w * 0.1f, h * 0.75f), Size(w * 0.2f, h * 0.02f), alpha = sf)
        val l = c + Offset(-w * 0.22f * cos(tilt), w * 0.22f * sin(tilt))
        val r = c + Offset(w * 0.22f * cos(tilt), -w * 0.22f * sin(tilt))
        drawLine(ScaleC42.Brass, l, r, 4f * dp, StrokeCap.Round, alpha = sf)
        for (p in listOf(l, r)) {
            drawLine(ScaleC42.Brass, p, p + Offset(-w * 0.06f, h * 0.16f), 1.2f * dp, alpha = sf)
            drawLine(ScaleC42.Brass, p, p + Offset(w * 0.06f, h * 0.16f), 1.2f * dp, alpha = sf)
            drawArc(ScaleC42.Brass, 0f, 180f, true, Offset(p.x - w * 0.08f, p.y + h * 0.14f), Size(w * 0.16f, h * 0.05f), alpha = sf)
        }
        tagAt(a.tag("exactscale"), c + Offset(0f, -h * 0.08f), seg(es, 0.5f, 0.8f) * (1f - seg(gr, 0f, 0.3f)), back = Back42, ink = ArtInk.Gold)
        if (gr > 0f) {
            val g = easeOut(seg(gr, 0f, 0.35f))
            val p = l + Offset(0f, h * 0.155f)
            softGlow(p, w * 0.03f, ScaleC42.Grain, 0.8f * g * sf)
            drawCircle(ScaleC42.Grain, w * 0.006f, p, alpha = g * sf)
        }
    }
    // a heart full of praise for His justice
    if (ph > 0f) {
        val f = easeOut(seg(ph, 0f, 0.4f)) * (1f - 0.7f * seg(mf, 0f, 0.3f))
        val c = Offset(w * 0.5f, h * 0.45f)
        val fill = easeOut(seg(ph, 0.2f, 0.9f))
        drawPath(w8Heart(c, w * 0.09f), ScaleC42.Heart, alpha = f * 0.2f)
        drawPath(w8Heart(c, w * 0.09f * fill), ScaleC42.Heart, alpha = f)
        softGlow(c, w * 0.18f * fill, ScaleC42.Heart, 0.5f * f)
    }
    // messengers first
    if (mf > 0f) {
        val f = easeOut(seg(mf, 0f, 0.35f))
        val ground = h * 0.84f
        drawRect(ScaleC42.Land, Offset(0f, ground), Size(w, h - ground), alpha = f)
        for (k in 0 until 5) {
            val x = w * (0.18f + 0.16f * k)
            drawRect(ScaleC42.House, Offset(x - w * 0.05f, ground - h * 0.09f), Size(w * 0.1f, h * 0.09f), alpha = f)
            drawPath(Path().apply { moveTo(x - w * 0.06f, ground - h * 0.09f); lineTo(x, ground - h * 0.14f); lineTo(x + w * 0.06f, ground - h * 0.09f); close() }, Color(0xFF8A5A3A), alpha = f)
        }
        for (k in 0 until 3) {
            val down = easeOut(seg(mf, 0.2f + 0.15f * k, 0.55f + 0.15f * k))
            val x = w * (0.26f + 0.24f * k)
            nabiLight14(Offset(x, h * 0.1f + (ground - h * 0.3f - h * 0.1f) * down), w * 0.008f, f * down, t, null, Offset(x, ground))
        }
    }
}

// ═════════════ 5. statues of kings, the plinth of the prophets (bs-athar) ═════════════

private object TempleC42 {
    val Sky = Color(0xFFF2D8A8)
    val SkyHigh = Color(0xFF8AB0D0)
    val Sand = Color(0xFFD8B884)
    val Stone = Color(0xFFB89A70)
    val StoneDark = Color(0xFF8A6A48)
    val Stele = Color(0xFF8A8478)
    val Dome = Color(0xFF3A8A5A)
    val Mosque = Color(0xFFE8DCC4)
    val Hill = Color(0xFF9A7A58)
    val Grave = Color(0xFF8A7A6A)
}

/**
 * Beats: statues (colossal statues and a temple front: the record that kings left), nostatue (an empty plinth with a soft
 * light above it: the prophets made no images), steles (a carved stele and a broken stone, each with a word on it:
 * «إِسْرَائِيلَ», «بَيْتُ دَاوُدَ»), medinamarks (a city of known places: «الْمَسْجِدُ النَّبَوِيُّ», «الْبَقِيعُ», «جَبَلُ
 * أُحُدٍ»).
 */
internal fun DrawScope.templesandsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "statues" to Shot(Offset(w * 0.3f, h * 0.5f), 1.15f),
        "nostatue" to Shot(Offset(w * 0.72f, h * 0.55f), 1.35f),
        "steles" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "medinamarks" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { templeSands(a) }
}

private fun DrawScope.templeSands(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val st = a.b("statues")
    val ns = a.b("nostatue")
    val sl = a.b("steles")
    val mm = a.b("medinamarks")
    val ground = h * 0.74f
    drawRect(Brush.verticalGradient(listOf(TempleC42.SkyHigh, TempleC42.Sky), endY = ground), size = Size(w, ground))
    drawRect(TempleC42.Sand, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - 0.85f * seg(mm, 0f, 0.4f)
    // the kings' statues and temple
    if (st > 0f) {
        val f = easeOut(seg(st, 0f, 0.4f)) * away
        drawRect(TempleC42.Stone, Offset(w * 0.1f, ground - h * 0.32f), Size(w * 0.4f, h * 0.32f), alpha = f)
        for (k in 0 until 4) drawRect(TempleC42.StoneDark, Offset(w * (0.13f + 0.09f * k), ground - h * 0.3f), Size(w * 0.04f, h * 0.3f), alpha = f * 0.6f)
        for (s in listOf(0.06f, 0.54f)) {
            val x = w * s
            drawRect(TempleC42.Stone, Offset(x - w * 0.03f, ground - h * 0.34f), Size(w * 0.06f, h * 0.34f), alpha = f)
            drawCircle(TempleC42.Stone, w * 0.035f, Offset(x, ground - h * 0.37f), alpha = f)
            drawRect(TempleC42.StoneDark, Offset(x - w * 0.04f, ground - h * 0.41f), Size(w * 0.08f, h * 0.025f), alpha = f)
        }
    }
    // the empty plinth of the prophets
    if (ns > 0f) {
        val f = easeOut(seg(ns, 0f, 0.35f)) * away
        val c = Offset(w * 0.75f, ground)
        drawRect(TempleC42.StoneDark, Offset(c.x - w * 0.06f, c.y - h * 0.08f), Size(w * 0.12f, h * 0.08f), alpha = f)
        val g = easeOut(seg(ns, 0.3f, 0.8f))
        softGlow(c + Offset(0f, -h * 0.2f), w * 0.07f, Color(0xFFFFF0C8), 0.6f * g * f)
        lightShafts(c + Offset(0f, -h * 0.3f), PI.toFloat() / 2f, 0.6f, h * 0.2f, Color(0xFFFFF0C8), 0.4f * g * f, t, rays = 5)
    }
    // the stele and the broken stone
    if (sl > 0f) {
        val f = easeOut(seg(sl, 0f, 0.3f)) * away
        val s1 = Offset(w * 0.36f, ground + h * 0.12f)
        drawRoundRect(TempleC42.Stele, Offset(s1.x - w * 0.07f, s1.y - h * 0.2f), Size(w * 0.14f, h * 0.2f), CornerRadius(w * 0.07f, w * 0.07f), alpha = f)
        for (r in 0 until 5) drawLine(Color(0xFF5A544A), Offset(s1.x - w * 0.05f, s1.y - h * 0.15f + r * h * 0.03f), Offset(s1.x + w * 0.05f, s1.y - h * 0.15f + r * h * 0.03f), 1f * dp, alpha = f * 0.6f)
        tagAt(a.tag("steles", 0, 0), s1 + Offset(0f, -h * 0.25f), seg(sl, 0.3f, 0.55f) * away, back = Back42, ink = ArtInk.Ivory)
        val s2 = Offset(w * 0.64f, ground + h * 0.12f)
        drawPath(Path().apply { moveTo(s2.x - w * 0.07f, s2.y); lineTo(s2.x - w * 0.06f, s2.y - h * 0.12f); lineTo(s2.x + w * 0.02f, s2.y - h * 0.15f); lineTo(s2.x + w * 0.07f, s2.y - h * 0.06f); lineTo(s2.x + w * 0.06f, s2.y); close() }, TempleC42.Stele, alpha = f * seg(sl, 0.4f, 0.6f))
        tagAt(a.tag("steles", 0, 1), s2 + Offset(0f, -h * 0.21f), seg(sl, 0.6f, 0.85f) * away, back = Back42, ink = ArtInk.Ivory)
    }
    // a city whose places everyone knows
    if (mm > 0f) {
        val f = easeOut(seg(mm, 0f, 0.4f))
        drawPath(Path().apply { moveTo(w * 0.62f, ground); quadraticTo(w * 0.8f, h * 0.36f, w * 1.02f, ground); close() }, TempleC42.Hill, alpha = f)
        domed29(Offset(w * 0.3f, ground), w * 0.12f, TempleC42.Mosque, TempleC42.Dome, f)
        for (k in 0 until 7) drawRect(TempleC42.Grave, Offset(w * (0.44f + 0.025f * k), ground - h * 0.02f), Size(w * 0.012f, h * 0.02f), alpha = f * seg(mm, 0.3f + 0.03f * k, 0.5f + 0.03f * k))
        val tags = listOf(Offset(w * 0.3f, ground - h * 0.28f), Offset(w * 0.52f, ground + h * 0.06f), Offset(w * 0.82f, h * 0.3f))
        for ((k, p) in tags.withIndex()) tagAt(a.tag("medinamarks", 0, k), p, seg(mm, 0.3f + 0.15f * k, 0.55f + 0.15f * k), back = Back42, ink = ArtInk.Gold)
    }
}

// ═════════════ 6. two meters: civilisation and culture (bs-takhalluf) ═════════════

private object MeterC42 {
    val Wall = Color(0xFF1A1E30)
    val WallLow = Color(0xFF2E3046)
    val Floor = Color(0xFF2A2430)
    val Coin = Color(0xFFD8B04A)
    val Beam = Color(0xFF9A8A6A)
    val Civ = Color(0xFF6AA0D8)
    val Cul = Color(0xFFE8B04A)
    val Band = Color(0xFFF2C45A)
    val Arch = Color(0xFFE8DCC4)
    val Hill = Color(0xFF6A5444)
    val Dot = Color(0xFFE8E0D0)
}

/**
 * Beats: materialscale (a balance heaped with coins: the beam snaps, a measure that fails), civculture (two meters: one
 * climbs, one keeps its level: «حَضَارَةٌ», «ثَقَافَةٌ»), longestciv (a long band of light across the ages), qarawiyyin (a
 * horseshoe arch with a lamp: «الْقَرَوِيِّينَ»), uhudlesson (a mountain, and on the hill before it the small marks leave
 * their place).
 */
internal fun DrawScope.twoladdersScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "materialscale" to Shot(Offset(w * 0.25f, h * 0.45f), 1.35f),
        "civculture" to Shot(Offset(w * 0.6f, h * 0.45f), 1.2f),
        "longestciv" to Shot(Offset(w * 0.5f, h * 0.18f), 1.15f),
        "qarawiyyin" to Shot(Offset(w * 0.3f, h * 0.6f), 1.3f),
        "uhudlesson" to Shot(Offset(w * 0.72f, h * 0.7f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { metersRoom(a) }
}

private fun DrawScope.metersRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ms = a.b("materialscale")
    val cc = a.b("civculture")
    val lc = a.b("longestciv")
    val qa = a.b("qarawiyyin")
    val ul = a.b("uhudlesson")
    val floor = h * 0.86f
    drawRect(Brush.verticalGradient(listOf(MeterC42.Wall, MeterC42.WallLow), endY = floor), size = Size(w, floor))
    drawRect(MeterC42.Floor, Offset(0f, floor), Size(w, h - floor))
    // a balance of money: it breaks
    if (ms > 0f) {
        val f = easeOut(seg(ms, 0f, 0.3f)) * (1f - 0.7f * seg(cc, 0f, 0.4f))
        val c = Offset(w * 0.25f, h * 0.36f)
        val snap = easeOut(seg(ms, 0.6f, 0.85f))
        drawLine(MeterC42.Beam, c, Offset(c.x, h * 0.6f), 3f * dp, alpha = f)
        drawLine(MeterC42.Beam, c + Offset(-w * 0.14f, h * 0.08f * snap), c + Offset(-w * 0.01f, 0f), 3f * dp, alpha = f)
        drawLine(MeterC42.Beam, c + Offset(w * 0.01f, 0f), c + Offset(w * 0.14f, -h * 0.02f), 3f * dp, alpha = f)
        for (k in 0 until 6) drawCircle(MeterC42.Coin, w * 0.012f, c + Offset(-w * 0.14f + w * 0.015f * (k % 3), h * (0.03f + 0.08f * snap) - h * 0.012f * (k / 3)), alpha = f * seg(ms, 0.1f + 0.05f * k, 0.3f + 0.05f * k))
    }
    // two meters
    if (cc > 0f) {
        val f = easeOut(seg(cc, 0f, 0.3f)) * (1f - 0.6f * seg(lc, 0f, 0.4f))
        for (k in 0 until 2) {
            val x = w * (0.52f + 0.2f * k)
            drawRect(Color(0xFF3A3E56), Offset(x - w * 0.04f, h * 0.2f), Size(w * 0.08f, h * 0.5f), alpha = f)
            val level = if (k == 0) 0.2f + 0.75f * easeOut(seg(cc, 0.2f, 0.8f)) else 0.55f
            drawRect(if (k == 0) MeterC42.Civ else MeterC42.Cul, Offset(x - w * 0.035f, h * 0.7f - h * 0.5f * level), Size(w * 0.07f, h * 0.5f * level), alpha = f)
            tagAt(a.tag("civculture", 0, k), Offset(x, h * 0.76f), seg(cc, 0.3f + 0.2f * k, 0.55f + 0.2f * k) * (1f - seg(lc, 0f, 0.4f)), back = Back42, ink = if (k == 0) Color(0xFFCFE4FF) else ArtInk.Gold)
        }
    }
    // the longest civilisation
    if (lc > 0f) {
        val f = easeOut(seg(lc, 0f, 0.3f)) * (1f - 0.5f * seg(qa, 0f, 0.4f))
        val y = h * 0.16f
        val run = easeInOut(seg(lc, 0.1f, 0.8f))
        drawLine(Color(0xFF3A3E56), Offset(w * 0.06f, y), Offset(w * 0.94f, y), 2f * dp, alpha = f)
        drawLine(MeterC42.Band, Offset(w * 0.12f, y), Offset(w * (0.12f + 0.7f * run), y), h * 0.025f, StrokeCap.Round, alpha = f)
        softGlow(Offset(w * (0.12f + 0.7f * run), y), w * 0.05f, MeterC42.Band, 0.5f * f)
    }
    // the oldest university still standing
    if (qa > 0f) {
        val f = easeOut(seg(qa, 0f, 0.35f)) * (1f - 0.5f * seg(ul, 0f, 0.4f))
        val c = Offset(w * 0.3f, floor)
        drawRect(MeterC42.Arch, Offset(c.x - w * 0.14f, c.y - h * 0.3f), Size(w * 0.28f, h * 0.3f), alpha = f)
        drawPath(horseshoe(c.x, c.y - h * 0.17f, w * 0.07f, w * 0.055f, c.y), Color(0xFF3A2E28), alpha = f)
        val lamp = Offset(c.x, c.y - h * 0.17f)
        softGlow(lamp, w * 0.05f, ArtInk.Gold, 0.7f * f)
        drawCircle(ArtInk.Gold, w * 0.01f, lamp, alpha = f)
        tagAt(a.tag("qarawiyyin"), c + Offset(0f, -h * 0.36f), seg(qa, 0.4f, 0.7f) * (1f - seg(ul, 0f, 0.4f)), back = Back42, ink = ArtInk.Gold)
    }
    // the lesson of Uhud: the post left
    if (ul > 0f) {
        val f = easeOut(seg(ul, 0f, 0.3f))
        drawPath(Path().apply { moveTo(w * 0.5f, floor); quadraticTo(w * 0.78f, h * 0.38f, w * 1.04f, floor); close() }, MeterC42.Hill, alpha = f)
        val post = Offset(w * 0.6f, floor - h * 0.06f)
        drawOval(Color(0xFF8A7058), Offset(post.x - w * 0.08f, post.y - h * 0.02f), Size(w * 0.16f, h * 0.05f), alpha = f)
        for (k in 0 until 8) {
            val leave = easeInOut(seg(ul, 0.3f + 0.05f * k, 0.7f + 0.05f * k))
            val home = post + Offset(w * (-0.06f + 0.017f * k), -h * 0.01f)
            val p = home + Offset(-w * 0.2f * leave, h * 0.06f * leave)
            drawCircle(MeterC42.Dot, w * 0.006f, p, alpha = f)
        }
    }
}
