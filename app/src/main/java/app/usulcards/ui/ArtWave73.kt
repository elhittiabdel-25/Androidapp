package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 73 (2026-11-03): v1.3 fiqh, «مجالس شهر رمضان» (Ibn ʿUthaymīn), the last six fasting cards. The manners of the fast; suhur and
 * iftar; what breaks the fast and what does not; the forgetful; the last ten nights; zakat al-fitr. The Prophet ﷺ is never drawn,
 * even where the text tells what he did: his house is shown by its lit windows. Other people are faceless figures.
 */

/** One lying down asleep: a body along the ground and a plain head, the head at [head]. */
internal fun DrawScope.lying73(head: Offset, len: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawLine(color, head + Offset(len * 0.12f, len * 0.02f), head + Offset(len, len * 0.04f), len * 0.12f, StrokeCap.Round, alpha = alpha)
    drawCircle(color, len * 0.09f, head, alpha = alpha)
}

/** A small syringe, needle to the left. */
internal fun DrawScope.syringe73(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(color, Offset(c.x - s * 0.3f, c.y - s * 0.08f), Size(s * 0.6f, s * 0.16f), alpha = alpha * 0.5f)
    drawRect(color, Offset(c.x - s * 0.3f, c.y - s * 0.08f), Size(s * 0.6f, s * 0.16f), alpha = alpha, style = Stroke(s * 0.025f))
    drawLine(color, c + Offset(s * 0.3f, 0f), c + Offset(s * 0.5f, 0f), s * 0.04f, alpha = alpha)
    drawLine(color, c + Offset(s * 0.5f, -s * 0.1f), c + Offset(s * 0.5f, s * 0.1f), s * 0.04f, StrokeCap.Round, alpha = alpha)
    drawLine(color, c + Offset(-s * 0.3f, 0f), c + Offset(-s * 0.55f, 0f), s * 0.015f, alpha = alpha)
}

/** A drip bag on its stand, the tube running down from it, drops falling with [t]. */
internal fun DrawScope.drip73(foot: Offset, s: Float, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    val pole = Color(0xFFB8BCC8)
    drawLine(pole, foot, foot + Offset(0f, -s), s * 0.025f, alpha = alpha)
    drawLine(pole, foot + Offset(-s * 0.12f, 0f), foot + Offset(s * 0.12f, 0f), s * 0.03f, StrokeCap.Round, alpha = alpha)
    val bag = foot + Offset(-s * 0.08f, -s * 0.92f)
    drawRoundRect(Color(0xFFDDEEF6), Offset(bag.x - s * 0.1f, bag.y), Size(s * 0.2f, s * 0.28f), CornerRadius(s * 0.04f), alpha = alpha)
    drawRect(Color(0xFFF2E6B0), Offset(bag.x - s * 0.08f, bag.y + s * 0.1f), Size(s * 0.16f, s * 0.16f), alpha = alpha * 0.7f)
    val tube = bag + Offset(0f, s * 0.28f)
    drawLine(Color(0xFFDDEEF6), tube, tube + Offset(-s * 0.15f, s * 0.5f), s * 0.012f, alpha = alpha)
    val u = fract(t * 1.2f)
    drawCircle(Color(0xFFF2E6B0), s * 0.015f, tube + Offset(0f, s * 0.08f * u + s * 0.02f), alpha = alpha * (1f - u))
}

/** An eye drawn alone (an almond and its pupil), for the senses kept in the fast. */
internal fun DrawScope.eye73(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val p = Path().apply {
        moveTo(c.x - s * 0.5f, c.y); quadraticTo(c.x, c.y - s * 0.4f, c.x + s * 0.5f, c.y); quadraticTo(c.x, c.y + s * 0.4f, c.x - s * 0.5f, c.y); close()
    }
    drawPath(p, color, alpha = alpha, style = Stroke(s * 0.06f))
    drawCircle(color, s * 0.12f, c, alpha = alpha)
}

/** An ear drawn alone. */
internal fun DrawScope.ear73(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawArc(color, -110f, 250f, false, Offset(c.x - s * 0.28f, c.y - s * 0.45f), Size(s * 0.56f, s * 0.75f), alpha = alpha, style = Stroke(s * 0.07f, cap = StrokeCap.Round))
    drawArc(color, -80f, 200f, false, Offset(c.x - s * 0.12f, c.y - s * 0.22f), Size(s * 0.26f, s * 0.36f), alpha = alpha, style = Stroke(s * 0.05f, cap = StrokeCap.Round))
}

/** Closed lips drawn alone. */
internal fun DrawScope.lips73(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawPath(Path().apply {
        moveTo(c.x - s * 0.5f, c.y); quadraticTo(c.x - s * 0.25f, c.y - s * 0.25f, c.x, c.y - s * 0.1f)
        quadraticTo(c.x + s * 0.25f, c.y - s * 0.25f, c.x + s * 0.5f, c.y); quadraticTo(c.x, c.y + s * 0.3f, c.x - s * 0.5f, c.y); close()
    }, color, alpha = alpha, style = Stroke(s * 0.06f))
    drawLine(color, c + Offset(-s * 0.45f, 0f), c + Offset(s * 0.45f, 0f), s * 0.04f, alpha = alpha)
}

/** A wooden measure (the ṣāʿ), its base at [foot], filled to [fill] with [grain]. */
internal fun DrawScope.saa73(foot: Offset, s: Float, fill: Float, grain: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val wood = Color(0xFF9A6A3E)
    val inner = Path().apply {
        moveTo(foot.x - s * 0.36f, foot.y - s * 0.62f); lineTo(foot.x + s * 0.36f, foot.y - s * 0.62f)
        lineTo(foot.x + s * 0.3f, foot.y - s * 0.06f); lineTo(foot.x - s * 0.3f, foot.y - s * 0.06f); close()
    }
    drawPath(inner, lerpColor(wood, Color.Black, 0.5f), alpha = alpha)
    val f = fill.coerceIn(0f, 1f)
    if (f > 0f) {
        val top = mix(foot.y - s * 0.06f, foot.y - s * 0.62f, f)
        val hw = mix(s * 0.3f, s * 0.36f, f)
        drawPath(Path().apply {
            moveTo(foot.x - hw, top); quadraticTo(foot.x, top - s * 0.1f * f, foot.x + hw, top)
            lineTo(foot.x + s * 0.3f, foot.y - s * 0.06f); lineTo(foot.x - s * 0.3f, foot.y - s * 0.06f); close()
        }, grain, alpha = alpha)
    }
    drawPath(inner, wood, alpha = alpha, style = Stroke(s * 0.06f))
    drawRect(wood, Offset(foot.x - s * 0.34f, foot.y - s * 0.07f), Size(s * 0.68f, s * 0.07f), alpha = alpha)
}

/** A little heap of food around [c]. */
internal fun DrawScope.heap73(c: Offset, s: Float, color: Color, alpha: Float, round: Boolean = true) {
    if (alpha <= 0.01f) return
    for (k in 0 until 7) {
        val p = c + Offset(s * (-0.3f + 0.2f * (k % 4)) + (if (k >= 4) s * 0.1f else 0f), -s * (if (k >= 4) 0.18f else 0f))
        if (round) drawOval(color, Offset(p.x - s * 0.11f, p.y - s * 0.07f), Size(s * 0.22f, s * 0.14f), alpha = alpha)
        else drawRect(color, Offset(p.x - s * 0.08f, p.y - s * 0.08f), Size(s * 0.16f, s * 0.16f), alpha = alpha)
    }
}

/** An hourglass whose sand has run [p] (0..1) into the lower bulb. */
internal fun DrawScope.hourglass73(c: Offset, ht: Float, p: Float, sand: Color, alpha: Float, frame: Color = Color(0xFFD8C8A0)) {
    if (alpha <= 0.01f) return
    val hw = ht * 0.3f
    val top = c.y - ht / 2f
    val bot = c.y + ht / 2f
    val glass = Path().apply {
        moveTo(c.x - hw, top); lineTo(c.x + hw, top); lineTo(c.x + ht * 0.03f, c.y)
        lineTo(c.x + hw, bot); lineTo(c.x - hw, bot); lineTo(c.x - ht * 0.03f, c.y); close()
    }
    drawPath(glass, Color.White, alpha = alpha * 0.12f)
    val pp = p.coerceIn(0f, 1f)
    val q = 1f - pp
    if (q > 0.01f) {
        val y0 = c.y - ht / 2f * q
        drawPath(Path().apply { moveTo(c.x - hw * q, y0); lineTo(c.x + hw * q, y0); lineTo(c.x, c.y); close() }, sand, alpha = alpha)
        drawLine(sand, c, Offset(c.x, bot - ht / 2f * pp), ht * 0.015f, alpha = alpha)
    }
    if (pp > 0.01f) {
        val y1 = bot - ht / 2f * pp
        drawPath(Path().apply { moveTo(c.x - hw, bot); lineTo(c.x + hw, bot); lineTo(c.x + hw * q, y1); lineTo(c.x - hw * q, y1); close() }, sand, alpha = alpha)
    }
    drawPath(glass, frame, alpha = alpha, style = Stroke(ht * 0.025f))
    for (y in listOf(top, bot)) drawLine(frame, Offset(c.x - hw * 1.15f, y), Offset(c.x + hw * 1.15f, y), ht * 0.05f, StrokeCap.Round, alpha = alpha)
}

// ═════════════ 7. ram-adab: the manners of the fast ═════════════

/**
 * Beats: twotiers (a firm base and fine arches on it: «آدَابٌ وَاجِبَةٌ», «وَآدَابٌ مُسْتَحَبَّةٌ»), prayontime (a row at prayer under a lamp,
 * the clock at its hour: «الصَّلَاةُ الْمَفْرُوضَةُ»), fourharms (four dark words, each struck out in turn: «الْكَذِبِ», «وَالْغِيبَةِ»,
 * «وَالنَّمِيمَةِ», «وَالْغِشِّ»), sleptprayer (one asleep while the sun crosses the prayer's time: «مِنْ أَعْظَمِ الْمُنْكَرَاتِ»),
 * earseyetongue (an ear, an eye and closed lips, each ringed in light: «سَمْعُكَ», «وَبَصَرُكَ», «وَلِسَانُكَ»).
 */
internal fun DrawScope.fastmannersScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twotiers" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "prayontime" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "fourharms" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sleptprayer" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "earseyetongue" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { fastMannersDraw(a) }
}

private fun DrawScope.fastMannersDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.82f
    ramNight72(t, ground, moon = 0.7f)
    val tt = fqIn(a, "twotiers", "prayontime")
    val pt = fqIn(a, "prayontime", "fourharms")
    val fh = fqIn(a, "fourharms", "sleptprayer")
    val sp = fqIn(a, "sleptprayer", "earseyetongue")
    val et = fqIn(a, "earseyetongue")
    if (tt > 0f) {
        val p = a.b("twotiers")
        val base = seg(p, 0.05f, 0.3f)
        drawRect(Color(0xFF8A7E6E), Offset(w * 0.12f, ground - h * 0.16f * base), Size(w * 0.76f, h * 0.16f * base), alpha = tt)
        for (k in 0 until 6) drawLine(Color(0xFF5E5448), Offset(w * (0.12f + 0.127f * k), ground - h * 0.16f * base), Offset(w * (0.12f + 0.127f * k), ground), 1.5f * dp, alpha = tt)
        tagAt(a.tag("twotiers", 0, 0), Offset(w * 0.5f, ground - h * 0.08f), tt * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Ivory)
        val top = ground - h * 0.16f
        for (k in 0 until 4) {
            val g = seg(p, 0.45f + 0.08f * k, 0.6f + 0.08f * k)
            val x = w * (0.78f - 0.187f * k)
            drawPath(archPath26(x, top - h * 0.22f * g, top, w * 0.07f), Color(0xFFE8D8B0), alpha = tt * g, style = Stroke(3f * dp))
            softGlow(Offset(x, top - h * 0.12f), w * 0.06f, RamC.Lit, 0.4f * tt * g)
        }
        tagAt(a.tag("twotiers", 0, 1), Offset(w * 0.5f, top - h * 0.32f), tt * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (pt > 0f) {
        val p = a.b("prayontime")
        drawRect(Color(0xFF2A2638), Offset(0f, h * 0.3f), Size(w, ground - h * 0.3f), alpha = pt * 0.7f)
        for (k in 0 until 5) pray12(Offset(w * (0.18f + 0.16f * k), ground), h * 0.26f, RamC.Person, pt * seg(p, 0.05f * k, 0.15f + 0.05f * k))
        clock22(Offset(w * 0.5f, h * 0.2f), w * 0.07f, RamC.Lit, seg(p, 0f, 0.5f), pt)
        tagAt(a.tag("prayontime"), Offset(w * 0.5f, h * 0.36f), pt * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fh > 0f) {
        val p = a.b("fourharms")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.6f * fh)
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.2f * k, 0.15f + 0.2f * k)
            val c = Offset(w * (if (k % 2 == 0) 0.7f else 0.3f), h * (0.32f + 0.26f * (k / 2)))
            drawRoundRect(RamC.Dark, Offset(c.x - w * 0.17f, c.y - h * 0.06f), Size(w * 0.34f, h * 0.12f), CornerRadius(h * 0.03f), alpha = fh * g)
            tagAt(a.tag("fourharms", 0, k), c, fh * g, back = Fq.Back, ink = ArtInk.Ivory)
            cross14(c + Offset(w * 0.15f, -h * 0.05f), w * 0.035f, seg(p, 0.12f + 0.2f * k, 0.22f + 0.2f * k))
        }
    }
    if (sp > 0f) {
        val p = a.b("sleptprayer")
        val sky = seg(p, 0f, 0.5f)
        drawRect(Color(0xFF6A9AC8), Offset.Zero, Size(w, ground), alpha = sp * 0.5f * sky)
        val arc = seg(p, 0.05f, 0.9f)
        val ang = PI.toFloat() * (1f - arc)
        sun12(Offset(w * 0.5f + cos(ang) * w * 0.38f, ground - sin(ang) * h * 0.55f), w * 0.04f, sp)
        drawRoundRect(Color(0xFF5A4A3E), Offset(w * 0.2f, ground - h * 0.06f), Size(w * 0.6f, h * 0.06f), CornerRadius(h * 0.01f), alpha = sp)
        lying73(Offset(w * 0.26f, ground - h * 0.1f), w * 0.5f, RamC.Person, sp)
        for (k in 0 until 3) {
            val u = fract(t * 0.4f + k / 3f)
            drawCircle(Color(0xFFB8C0D8), w * (0.008f + 0.008f * u), Offset(w * 0.24f + u * w * 0.06f, ground - h * 0.16f - u * h * 0.1f), alpha = sp * (1f - u))
        }
        tagAt(a.tag("sleptprayer"), Offset(w * 0.5f, h * 0.9f), sp * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (et > 0f) {
        val p = a.b("earseyetongue")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.6f * et)
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.25f * k, 0.25f + 0.25f * k)
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.48f)
            softGlow(c, w * 0.1f, RamC.Lit, 0.35f * et * g)
            drawCircle(RamC.Lit, w * 0.11f, c, alpha = et * g, style = Stroke(2.5f * dp))
            when (k) {
                0 -> ear73(c, w * 0.14f, RamC.Person, et * g)
                1 -> eye73(c, w * 0.15f, RamC.Person, et * g)
                else -> lips73(c, w * 0.15f, RamC.Person, et * g)
            }
            tagAt(a.tag("earseyetongue", 0, k), c + Offset(0f, h * 0.13f), et * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 8. ram-sahur: suhur and iftar ═════════════

/**
 * Beats: predawnmeal (in the last of the night one sits to a small meal: «السُّحُورُ»), nearfajr (the dawn line creeps up the horizon and the
 * meal ends just short of it: «تَأْخِيرُهُ», «مَا لَمْ يَخْشَ طُلُوعَ الْفَجْرِ»), sunsethaste (the sun goes down and a date is taken at once:
 * «تَعْجِيلُ الْفِطْرِ»), ratabtamrwater (fresh dates, then dry dates, then water, lit one after another: «رُطَبٍ», «فَتَمْرٍ», «فَمَاءٍ»),
 * thirstgone (a dry branching vein fills with water, then a gold seal: «ذَهَبَ الظَّمَأُ», «وَابْتَلَّتِ الْعُرُوقُ», «وَثَبَتَ الْأَجْرُ»).
 */
internal fun DrawScope.suhooriftarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "predawnmeal" to Shot(Offset(w * 0.5f, h * 0.62f), 1.1f),
        "nearfajr" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "sunsethaste" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "ratabtamrwater" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "thirstgone" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { suhoorIftarDraw(a) }
}

private fun DrawScope.suhoorIftarDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.74f
    val pm = fqIn(a, "predawnmeal", "sunsethaste")
    val nf = fqIn(a, "nearfajr", "sunsethaste")
    val sh = fqIn(a, "sunsethaste", "thirstgone")
    val rt = fqIn(a, "ratabtamrwater", "thirstgone")
    val tg = fqIn(a, "thirstgone")
    val dawn = seg(a.b("nearfajr"), 0.1f, 0.9f) * (1f - seg(a.b("sunsethaste"), 0f, BEAT_OUT))
    val dusk = seg(a.b("sunsethaste"), 0f, 0.4f)
    nightSky(t, RamC.Top, lerpColor(lerpColor(RamC.Low, Color(0xFFB8A0C8), dawn), Color(0xFFE8906A), dusk), ground, starAlpha = 0.8f * (1f - dawn * 0.6f))
    drawRect(RamC.Ground, Offset(0f, ground), Size(w, h - ground))
    if (dawn > 0f) drawRect(Color(0xFFF2D8B0), Offset(0f, ground - h * 0.025f * dawn), Size(w, h * 0.025f * dawn), alpha = dawn * nf)
    val table = Offset(w * 0.55f, ground + h * 0.1f)
    val meal = maxOf(pm, sh)
    if (meal > 0f) {
        drawRoundRect(Color(0xFF6A4A32), Offset(table.x - w * 0.22f, table.y - h * 0.02f), Size(w * 0.44f, h * 0.025f), CornerRadius(h * 0.008f), alpha = meal)
        dateBowl72(table + Offset(-w * 0.08f, -h * 0.02f), w * 0.14f, meal)
        cup12(table + Offset(w * 0.1f, -h * 0.02f), w * 0.055f, RamC.Rim, meal, RamC.Rayyan)
        sit12(table + Offset(w * 0.3f, h * 0.0f), h * 0.24f, RamC.Person, meal, -1f)
    }
    if (pm > 0f) {
        val p = a.b("predawnmeal")
        crescent12(Offset(w * 0.2f, h * 0.18f), w * 0.03f, RamC.Lit, RamC.Top, pm * (1f - dawn))
        tagAt(a.tag("predawnmeal"), Offset(w * 0.5f, h * 0.22f), pm * seg(p, 0.15f, 0.35f) * (1f - seg(a.b("nearfajr"), 0f, BEAT_OUT)), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nf > 0f) {
        val p = a.b("nearfajr")
        hourglass73(Offset(w * 0.16f, h * 0.45f), h * 0.16f, 0.2f + 0.75f * seg(p, 0.1f, 0.8f), Fq.Sand, nf)
        tagAt(a.tag("nearfajr", 0, 0), Offset(w * 0.5f, h * 0.16f), nf * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("nearfajr", 0, 1), Offset(w * 0.5f, h * 0.3f), nf * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (sh > 0f) {
        val p = a.b("sunsethaste")
        val sun = Offset(w * 0.18f, mix(ground - h * 0.12f, ground + h * 0.06f, easeInOut(seg(p, 0f, 0.4f))))
        sun12(sun, w * 0.05f, sh * (1f - seg(p, 0.35f, 0.45f)))
        drawRect(RamC.Ground, Offset(0f, ground), Size(w * 0.4f, h * 0.1f), alpha = sh)
        val lift = easeOut(seg(p, 0.45f, 0.7f))
        val d = table + Offset(-w * 0.08f, -h * 0.07f) + Offset(w * 0.25f * lift, -h * 0.12f * lift)
        drawOval(RamC.Date, Offset(d.x - w * 0.025f, d.y - h * 0.012f), Size(w * 0.05f, h * 0.024f), alpha = sh * seg(p, 0.45f, 0.5f))
        tagAt(a.tag("sunsethaste"), Offset(w * 0.5f, h * 0.2f), sh * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (rt > 0f) {
        val p = a.b("ratabtamrwater")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.65f * rt)
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.5f)
            softGlow(c, w * 0.1f, RamC.Lit, 0.4f * rt * g)
            when (k) {
                0 -> heap73(c, w * 0.18f, Color(0xFFC8862E), rt * (0.35f + 0.65f * g))
                1 -> heap73(c, w * 0.18f, RamC.Date, rt * (0.35f + 0.65f * g))
                else -> cup12(c + Offset(0f, h * 0.04f), w * 0.09f, RamC.Rim, rt * (0.35f + 0.65f * g), RamC.Rayyan)
            }
            tagAt(a.tag("ratabtamrwater", 0, k), c + Offset(0f, h * 0.12f), rt * g, back = Fq.Back, ink = ArtInk.Gold)
            if (k < 2) drawLine(RamC.Rim, c + Offset(-w * 0.1f, 0f), c + Offset(-w * 0.2f, 0f), 1.5f * dp, alpha = rt * g * 0.6f)
        }
    }
    if (tg > 0f) {
        val p = a.b("thirstgone")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.7f * tg)
        val root = Offset(w * 0.5f, h * 0.82f)
        val wet = seg(p, 0.3f, 0.65f)
        fun branch(from: Offset, ang: Float, len: Float, depth: Int, idx: Int) {
            val to = from + Offset(cos(ang), -sin(ang)) * len
            val f = seg(wet * 4f - (3 - depth), 0f, 1f)
            drawLine(lerpColor(Color(0xFF8A6A4A), RamC.Rayyan, f), from, to, (1f + depth) * 1.2f * dp, StrokeCap.Round, alpha = tg)
            if (depth > 0) {
                branch(to, ang + 0.45f, len * 0.7f, depth - 1, idx * 2)
                branch(to, ang - 0.45f, len * 0.7f, depth - 1, idx * 2 + 1)
            }
        }
        branch(root, PI.toFloat() / 2f, h * 0.18f, 3, 0)
        tagAt(a.tag("thirstgone", 0, 0), Offset(w * 0.5f, h * 0.12f), tg * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("thirstgone", 0, 1), Offset(w * 0.5f, h * 0.24f), tg * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Jade)
        val seal = seg(p, 0.65f, 0.8f)
        softGlow(Offset(w * 0.5f, h * 0.9f), w * 0.07f, RamC.Lit, tg * seal)
        drawPath(starPath(Offset(w * 0.5f, h * 0.9f), w * 0.035f), RamC.Lit, alpha = tg * seal)
        tagAt(a.tag("thirstgone", 0, 2), Offset(w * 0.5f, h * 0.36f), tg * seal, back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 9. ram-muftirat: what breaks the fast ═════════════

/**
 * Beats: eatdrink (under the day's sun a plate and a cup, struck out: «الْأَكْلُ وَالشُّرْبُ»), nutritivedrip (a drip bag that stands in for
 * food, struck out: «كَالْإِبَرِ الْمُغَذِّيَةِ»), plainshot (a plain syringe, ticked: «الْإِبَرُ غَيْرُ الْمُغَذِّيَةِ فَلَا تُفَطِّرُ»), scentok (a
 * phial of scent, ticked: «وَشَمُّ الرَّوَائِحِ لَا يُفَطِّرُ»), holdandrepay (the sun's path with the rest of the day held, and one day set
 * gold on the calendar after: «الْإِمْسَاكُ بَقِيَّةَ الْيَوْمِ», «وَالْقَضَاءُ»).
 */
internal fun DrawScope.fastbreakersScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "eatdrink" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "nutritivedrip" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "plainshot" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "scentok" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "holdandrepay" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { fastBreakersDraw(a) }
}

private fun DrawScope.fastBreakersDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    daySky12(Color(0xFF8AB8DA), Color(0xFFF2E2C2), ground, Color(0xFFB8A27E))
    val ed = fqIn(a, "eatdrink", "nutritivedrip")
    val nd = fqIn(a, "nutritivedrip", "scentok")
    val ps = fqIn(a, "plainshot", "scentok")
    val so = fqIn(a, "scentok", "holdandrepay")
    val hr = fqIn(a, "holdandrepay")
    sun12(Offset(w * 0.82f, h * 0.14f), w * 0.05f, 1f - hr)
    if (ed > 0f) {
        val p = a.b("eatdrink")
        dateBowl72(Offset(w * 0.4f, ground - h * 0.02f), w * 0.24f, ed)
        cup12(Offset(w * 0.64f, ground - h * 0.02f), w * 0.09f, Color(0xFF6A7A9A), ed, RamC.Rayyan)
        cross14(Offset(w * 0.52f, ground - h * 0.2f), w * 0.07f, seg(p, 0.3f, 0.5f))
        tagAt(a.tag("eatdrink"), Offset(w * 0.5f, h * 0.3f), ed * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (nd > 0f) {
        val p = a.b("nutritivedrip")
        drip73(Offset(w * 0.72f, ground), h * 0.5f, nd, t)
        cross14(Offset(w * 0.72f, ground - h * 0.58f), w * 0.05f, seg(p, 0.4f, 0.6f))
        tagAt(a.tag("nutritivedrip"), Offset(w * 0.72f, ground + h * 0.08f), nd * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (ps > 0f) {
        val p = a.b("plainshot")
        syringe73(Offset(w * 0.28f, ground - h * 0.25f), w * 0.3f, Color(0xFF5A6A8A), ps)
        tick14(Offset(w * 0.28f, ground - h * 0.38f), w * 0.05f, seg(p, 0.3f, 0.5f))
        tagAt(a.tag("plainshot"), Offset(w * 0.3f, ground + h * 0.08f), ps * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (so > 0f) {
        val p = a.b("scentok")
        fqPhial(Offset(w * 0.5f, ground - h * 0.02f), h * 0.3f, Color(0xFFB8A0D8), so, t)
        tick14(Offset(w * 0.7f, ground - h * 0.3f), w * 0.05f, seg(p, 0.3f, 0.5f))
        tagAt(a.tag("scentok"), Offset(w * 0.5f, h * 0.18f), so * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (hr > 0f) {
        val p = a.b("holdandrepay")
        val c = Offset(w * 0.5f, ground)
        val r = w * 0.38f
        drawArc(Color(0xFF6A7A9A), 180f, 180f, false, Offset(c.x - r, c.y - r * 0.9f), Size(r * 2f, r * 1.8f), alpha = hr, style = Stroke(2f * dp, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp))))
        val at = 0.35f
        val held = seg(p, 0.1f, 0.5f)
        drawArc(Color(0xFF3A3E58), 180f + 180f * at, 180f * (1f - at) * held, false, Offset(c.x - r, c.y - r * 0.9f), Size(r * 2f, r * 1.8f), alpha = hr, style = Stroke(6f * dp, cap = StrokeCap.Round))
        val ang = PI.toFloat() * (1f + at)
        sun12(c + Offset(cos(ang) * r, sin(ang) * r * 0.9f), w * 0.035f, hr)
        tagAt(a.tag("holdandrepay", 0, 0), Offset(w * 0.5f, h * 0.2f), hr * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
        val cell = w * 0.07f
        val g = seg(p, 0.6f, 0.8f)
        fqCalendar(Offset(w * 0.5f - cell * 1.15f * 3.5f, ground + h * 0.05f), 7, 7, cell, hr, Color(0xFF6A7A9A)) { i -> if (i == 5) RamC.Lit.copy(alpha = g) else null }
        tagAt(a.tag("holdandrepay", 0, 1), Offset(w * 0.5f, ground + h * 0.17f), hr * g, back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 10. ram-nasi: when the fast is not broken ═════════════

/**
 * Beats: threerings (three rings light, and only all three together break the fast: «عَالِمًا», «ذَاكِرًا», «مُخْتَارًا»), forgotate (one
 * drinks, forgetting, and a tick stays over him: «نَاسِيًا», «فَصِيَامُهُ صَحِيحٌ»), didntknow (the dawn already pale behind him, unseen,
 * and still the tick: «الْجَاهِلُ بِالْحُكْمِ أَوْ بِالْوَقْتِ»), notbreakers (a kohl pot, an eye-dropper, a siwak and a splash of water,
 * each ticked: «بِالْكُحْلِ», «وَقَطْرَةِ الْعَيْنِ», «بِالسِّوَاكِ», «بِالتَّبَرُّدِ بِالْمَاءِ»), rememberstop (he remembers and the cup comes
 * down: «أَمْسَكَ»).
 */
internal fun DrawScope.forgotfastScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threerings" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "forgotate" to Shot(Offset(w * 0.5f, h * 0.6f), 1.08f),
        "didntknow" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "notbreakers" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "rememberstop" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { forgotFastDraw(a) }
}

private fun DrawScope.forgotFastDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    val tr = fqIn(a, "threerings", "forgotate")
    val fa = fqIn(a, "forgotate", "notbreakers")
    val dk = fqIn(a, "didntknow", "notbreakers")
    val nb = fqIn(a, "notbreakers", "rememberstop")
    val rs = fqIn(a, "rememberstop")
    val dawn = seg(a.b("didntknow"), 0.1f, 0.5f) * (1f - seg(a.b("notbreakers"), 0f, BEAT_OUT))
    nightSky(t, RamC.Top, lerpColor(RamC.Low, Color(0xFFE8C8A8), dawn), ground, starAlpha = 0.7f * (1f - dawn))
    drawRect(RamC.Ground, Offset(0f, ground), Size(w, h - ground))
    if (tr > 0f) {
        val p = a.b("threerings")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.22f * k, 0.25f + 0.22f * k)
            val c = Offset(w * (0.72f - 0.22f * k), h * 0.45f)
            drawCircle(RamC.Lit, w * 0.12f, c, alpha = tr * (0.25f + 0.75f * g), style = Stroke(4f * dp))
            tagAt(a.tag("threerings", 0, k), c + Offset(0f, -h * 0.16f * (if (k == 1) -1f else 1f)), tr * g, back = Fq.Back, ink = ArtInk.Gold)
        }
        softGlow(Offset(w * 0.5f, h * 0.45f), w * 0.1f, RamC.Lit, 0.6f * tr * seg(p, 0.75f, 0.9f))
    }
    val seat = Offset(w * 0.52f, ground)
    val drink = maxOf(fa, dk, rs)
    if (drink > 0f) {
        val down = seg(a.b("rememberstop"), 0.2f, 0.5f)
        sit12(seat, h * 0.28f, RamC.Person, drink, -1f)
        val cupAt = seat + Offset(-h * 0.1f, mix(-h * 0.24f, -h * 0.08f, down))
        cup12(cupAt + Offset(0f, h * 0.035f), w * 0.05f, RamC.Rim, drink, RamC.Rayyan)
    }
    if (fa > 0f) {
        val p = a.b("forgotate")
        tick14(seat + Offset(0f, -h * 0.42f), w * 0.05f, seg(p, 0.35f, 0.55f))
        tagAt(a.tag("forgotate", 0, 0), Offset(w * 0.25f, h * 0.25f), fa * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("forgotate", 0, 1), Offset(w * 0.75f, h * 0.25f), fa * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (dk > 0f) {
        val p = a.b("didntknow")
        drawRect(Color(0xFFF2D8B0), Offset(0f, ground - h * 0.03f), Size(w, h * 0.03f), alpha = dk * dawn * 0.8f)
        tick14(seat + Offset(0f, -h * 0.42f), w * 0.05f, 1f)
        tagAt(a.tag("didntknow"), Offset(w * 0.5f, h * 0.16f), dk * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nb > 0f) {
        val p = a.b("notbreakers")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.7f * nb)
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.2f * k, 0.2f + 0.2f * k)
            val c = Offset(w * (0.8f - 0.2f * k), h * 0.48f)
            val s = w * 0.12f
            when (k) {
                0 -> {
                    drawRoundRect(Color(0xFF3A3A48), Offset(c.x - s * 0.3f, c.y - s * 0.2f), Size(s * 0.6f, s * 0.5f), CornerRadius(s * 0.1f), alpha = nb * g)
                    drawLine(Color(0xFFB8A078), c + Offset(0f, -s * 0.2f), c + Offset(s * 0.1f, -s * 0.6f), s * 0.05f, StrokeCap.Round, alpha = nb * g)
                }
                1 -> {
                    drawLine(Color(0xFFDDEEF6), c + Offset(0f, -s * 0.5f), c + Offset(0f, s * 0.05f), s * 0.08f, StrokeCap.Round, alpha = nb * g)
                    drawOval(Color(0xFFC0503A), Offset(c.x - s * 0.13f, c.y - s * 0.7f), Size(s * 0.26f, s * 0.22f), alpha = nb * g)
                    fqDrop(c + Offset(0f, s * 0.22f), s * 0.06f, Fq.Water, nb * g * bump(fract(t * 0.8f)))
                }
                2 -> {
                    drawLine(Color(0xFF9A7A50), c + Offset(-s * 0.1f, s * 0.35f), c + Offset(s * 0.1f, -s * 0.35f), s * 0.09f, StrokeCap.Round, alpha = nb * g)
                    for (f in 0 until 4) drawLine(Color(0xFFE8D8B0), c + Offset(s * 0.1f, -s * 0.35f), c + Offset(s * (0.06f + 0.03f * f), -s * 0.48f), 1.5f * dp, alpha = nb * g)
                }
                else -> for (d in 0 until 5) fqDrop(c + Offset(s * (-0.3f + 0.15f * d), -s * 0.1f * (d % 2)), s * 0.07f, Fq.Water, nb * g)
            }
            tick14(c + Offset(0f, -h * 0.13f), w * 0.03f, seg(p, 0.12f + 0.2f * k, 0.2f + 0.2f * k))
            tagAt(a.tag("notbreakers", 0, k), c + Offset(0f, h * 0.12f + (k % 2) * h * 0.07f), nb * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (rs > 0f) {
        val p = a.b("rememberstop")
        softGlow(seat + Offset(0f, -h * 0.3f), w * 0.08f, RamC.Lit, rs * bump(seg(p, 0f, 0.4f)))
        tagAt(a.tag("rememberstop"), Offset(w * 0.5f, h * 0.18f), rs * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 11. ram-ashr: the last ten nights ═════════════

/**
 * Beats: lasttennights (thirty nights in a row, the last ten brighter: «الْعَشْرِ الْأَوَاخِرِ»), wakefamily (a house at night whose windows
 * light one by one: «وَيُوقِظُ أَهْلَهُ»), oddnights (the ten nights; the odd ones glow, one of them most: «فِي الْأَوْتَارِ», «لَيْلَةُ سَبْعٍ
 * وَعِشْرِينَ»), movingnight (year after year the brightest night moves from one odd night to another: «بَلْ تَتَنَقَّلُ»), hiddenseek (the
 * glow is veiled, and lights rise from all ten nights alike: «لِيَكْثُرَ عَمَلُ الْعِبَادِ فِي طَلَبِهَا»).
 */
internal fun DrawScope.lasttenScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "lasttennights" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "wakefamily" to Shot(Offset(w * 0.5f, h * 0.6f), 1.08f),
        "oddnights" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "movingnight" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "hiddenseek" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { lastTenDraw(a) }
}

private fun DrawScope.lastTenDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.84f
    ramNight72(t, ground, moon = 0f)
    val lt = fqIn(a, "lasttennights", "wakefamily")
    val wf = fqIn(a, "wakefamily", "oddnights")
    val od = fqIn(a, "oddnights", "movingnight")
    val mv = fqIn(a, "movingnight", "hiddenseek")
    val hs = fqIn(a, "hiddenseek")
    if (lt > 0f) {
        val p = a.b("lasttennights")
        val cell = w * 0.025f
        for (i in 0 until 30) {
            val g = seg(p, i * 0.015f, i * 0.015f + 0.1f)
            val c = Offset(w * 0.93f - i * cell * 1.27f, h * 0.48f)
            val last = i >= 20
            val bright = if (last) seg(p, 0.5f, 0.7f) else 0f
            drawCircle(if (last) RamC.Lit else RamC.Rim, cell * 0.45f, c, alpha = lt * g * (0.4f + 0.6f * bright))
            if (last) softGlow(c, cell * 1.6f, RamC.Lit, 0.4f * lt * bright)
        }
        tagAt(a.tag("lasttennights"), Offset(w * 0.25f, h * 0.36f), lt * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (wf > 0f) {
        val p = a.b("wakefamily")
        val foot = Offset(w * 0.5f, ground)
        val s = w * 0.5f
        drawRect(Color(0xFF3A3448), Offset(foot.x - s * 0.5f, foot.y - s * 0.55f), Size(s, s * 0.55f), alpha = wf)
        drawPath(Path().apply { moveTo(foot.x - s * 0.56f, foot.y - s * 0.55f); lineTo(foot.x, foot.y - s * 0.8f); lineTo(foot.x + s * 0.56f, foot.y - s * 0.55f); close() }, Color(0xFF2A2436), alpha = wf)
        for (k in 0 until 4) {
            val lit = seg(p, 0.1f + 0.15f * k, 0.2f + 0.15f * k)
            val win = Offset(foot.x + s * (0.33f - 0.22f * k), foot.y - s * 0.32f)
            drawRect(lerpColor(Color(0xFF1A1826), RamC.Lit, lit), Offset(win.x - s * 0.06f, win.y - s * 0.08f), Size(s * 0.12f, s * 0.16f), alpha = wf)
            softGlow(win, s * 0.12f, RamC.Lit, 0.4f * wf * lit)
        }
        tagAt(a.tag("wakefamily"), Offset(w * 0.5f, h * 0.18f), wf * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val tenAt = { i: Int, row: Int -> Offset(w * 0.9f - i * w * 0.089f, h * (0.5f + 0.0f * row)) }
    if (od > 0f) {
        val p = a.b("oddnights")
        for (i in 0 until 10) {
            val odd = i % 2 == 0
            val c = tenAt(i, 0)
            val g = if (odd) seg(p, 0.1f + 0.05f * i, 0.25f + 0.05f * i) else 0f
            val best = if (i == 6) seg(p, 0.6f, 0.8f) else 0f
            drawCircle(if (odd) RamC.Lit else RamC.Rim, w * 0.028f, c, alpha = od * (0.35f + 0.5f * g))
            softGlow(c, w * (0.05f + 0.05f * best), RamC.Lit, od * (0.35f * g + 0.5f * best))
        }
        tagAt(a.tag("oddnights", 0, 0), Offset(w * 0.5f, h * 0.36f), od * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("oddnights", 0, 1), tenAt(6, 0) + Offset(0f, h * 0.12f), od * seg(p, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (mv > 0f) {
        val p = a.b("movingnight")
        val picks = listOf(6, 2, 8, 4, 6, 0)
        for (row in 0 until 3) {
            val y = h * (0.3f + 0.2f * row)
            val g = seg(p, 0.1f + 0.25f * row, 0.3f + 0.25f * row)
            for (i in 0 until 10) {
                val c = Offset(w * 0.9f - i * w * 0.089f, y)
                drawCircle(if (i % 2 == 0) RamC.Lit else RamC.Rim, w * 0.02f, c, alpha = mv * g * 0.45f)
                if (i == picks[row]) softGlow(c, w * 0.06f, RamC.Lit, mv * g * 0.9f)
            }
        }
        tagAt(a.tag("movingnight"), Offset(w * 0.5f, h * 0.88f), mv * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (hs > 0f) {
        val p = a.b("hiddenseek")
        for (i in 0 until 10) {
            val c = tenAt(i, 0)
            drawCircle(RamC.Lit, w * 0.028f, c, alpha = hs * 0.55f)
            for (k in 0 until 3) {
                val u = fract(t * 0.3f + k / 3f + i * 0.17f)
                drawCircle(RamC.Lit, w * 0.006f, c + Offset(sin(u * 7f + i) * w * 0.01f, -u * h * 0.3f), alpha = hs * seg(p, 0.3f, 0.5f) * (1f - u))
            }
        }
        drawRect(Color(0xFF2A2C48), Offset(w * 0.05f, h * 0.55f), Size(w * 0.9f, h * 0.05f), alpha = hs * 0.5f * seg(p, 0.05f, 0.25f))
        tagAt(a.tag("hiddenseek"), Offset(w * 0.5f, h * 0.72f), hs * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 12. ram-fitr: zakat al-fitr ═════════════

/**
 * Beats: everysoul (a household, old and young, man and woman, each with a small sack before them: «الْكَبِيرِ وَالصَّغِيرِ», «وَالذَّكَرِ
 * وَالْأُنْثَى»), onesaa (a wooden measure fills, and four heaps of food sit by it: «صَاعٌ», «كَالتَّمْرِ وَالشَّعِيرِ وَالزَّبِيبِ وَالْأَقِطِ»),
 * surplus (the food of the eid day and night set aside, and the measure goes out from what is left: «فَاضِلَةً عَنْ نَفَقَةِ يَوْمِ الْعِيدِ
 * وَلَيْلَتِهِ»), beforeeidprayer (two days before the eid, the eid morning bright, and a bar after the prayer: «صَبَاحَ الْعِيدِ قَبْلَ
 * الصَّلَاةِ», «قَبْلَ الْعِيدِ بِيَوْمٍ أَوْ يَوْمَيْنِ»), foodnotvalue (coins and cloth struck out, the measure of food ticked: «صَاعًا مِنْ
 * طَعَامٍ»).
 */
internal fun DrawScope.fitrsaaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "everysoul" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "onesaa" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "surplus" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "beforeeidprayer" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "foodnotvalue" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { fitrSaaDraw(a) }
}

private fun DrawScope.fitrSaaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    daySky12(Color(0xFF9CC8E6), Color(0xFFF4E6C8), ground, Color(0xFFC8B48E))
    fqEidMoon(Offset(w * 0.12f, h * 0.12f), w * 0.03f, Color(0xFF9CC8E6), 0.8f)
    val es = fqIn(a, "everysoul", "onesaa")
    val os = fqIn(a, "onesaa", "surplus")
    val sp = fqIn(a, "surplus", "beforeeidprayer")
    val be = fqIn(a, "beforeeidprayer", "foodnotvalue")
    val fv = fqIn(a, "foodnotvalue")
    val grain = Color(0xFFD8B070)
    val person = Color(0xFF5A4E44)
    if (es > 0f) {
        val p = a.b("everysoul")
        val xs = listOf(0.82f, 0.62f, 0.42f, 0.22f)
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k)
            val foot = Offset(w * xs[k], ground)
            when (k) {
                0 -> figure(foot, h * 0.32f, person, es * g, lean = 0.12f)
                1 -> child12(foot, h * 0.3f, person, es * g)
                2 -> figure(foot, h * 0.34f, person, es * g)
                else -> woman12(foot, h * 0.32f, person, es * g)
            }
            if (k == 0) drawLine(person, foot + Offset(-h * 0.07f, 0f), foot + Offset(-h * 0.06f, -h * 0.2f), 2.5f * dp, StrokeCap.Round, alpha = es * g)
            fqSack(foot + Offset(w * 0.07f, 0f), h * 0.08f, grain, es * seg(p, 0.6f + 0.06f * k, 0.7f + 0.06f * k))
        }
        tagAt(a.tag("everysoul", 0, 0), Offset(w * 0.72f, h * 0.2f), es * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("everysoul", 0, 1), Offset(w * 0.32f, h * 0.2f), es * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (os > 0f) {
        val p = a.b("onesaa")
        saa73(Offset(w * 0.5f, ground), h * 0.32f, seg(p, 0.05f, 0.4f), grain, os)
        tagAt(a.tag("onesaa", 0, 0), Offset(w * 0.5f, ground - h * 0.3f), os * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val cols = listOf(RamC.Date, Color(0xFFD8C080), Color(0xFF6A3A5A), Color(0xFFF2EAD8))
        for (k in 0 until 4) {
            val g = seg(p, 0.4f + 0.1f * k, 0.5f + 0.1f * k)
            val c = Offset(w * (if (k < 2) 0.82f - 0.14f * k else 0.32f - 0.14f * (k - 2)), ground - h * 0.03f)
            heap73(c, w * 0.12f, cols[k], os * g, round = k != 3)
        }
        tagAt(a.tag("onesaa", 0, 1), Offset(w * 0.5f, h * 0.18f), os * seg(p, 0.45f, 0.65f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (sp > 0f) {
        val p = a.b("surplus")
        val shelf = Offset(w * 0.25f, ground)
        drawRect(Color(0xFF8A6A48), Offset(shelf.x - w * 0.18f, shelf.y - h * 0.3f), Size(w * 0.36f, h * 0.3f), alpha = sp * 0.5f)
        dateBowl72(shelf + Offset(-w * 0.07f, -h * 0.02f), w * 0.12f, sp)
        cup12(shelf + Offset(w * 0.08f, -h * 0.02f), w * 0.05f, Color(0xFF6A7A9A), sp, RamC.Rayyan)
        heap73(shelf + Offset(0f, -h * 0.17f), w * 0.12f, Color(0xFFD8C080), sp)
        val out = easeInOut(seg(p, 0.4f, 0.8f))
        saa73(Offset(mix(w * 0.55f, w * 0.85f, out), ground), h * 0.22f, 1f, grain, sp)
        tagAt(a.tag("surplus"), Offset(w * 0.5f, h * 0.2f), sp * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (be > 0f) {
        val p = a.b("beforeeidprayer")
        val y = h * 0.5f
        val x0 = w * 0.88f
        val x1 = w * 0.1f
        drawLine(Color(0xFF6A7A9A), Offset(x0, y), Offset(x1, y), 2f * dp, alpha = be)
        for (k in 0 until 2) {
            val c = Offset(mix(x0, x1, 0.12f + 0.16f * k), y)
            drawCircle(RamC.Lit, w * 0.025f, c, alpha = be * 0.6f * seg(p, 0.5f, 0.65f))
        }
        val morning = Offset(mix(x0, x1, 0.55f), y)
        sun12(morning + Offset(0f, -h * 0.08f), w * 0.04f, be * seg(p, 0.1f, 0.3f))
        softGlow(morning, w * 0.06f, RamC.Lit, be * seg(p, 0.1f, 0.3f))
        mosque12(Offset(mix(x0, x1, 0.72f), y + h * 0.02f), w * 0.14f, Color(0xFFD8CCB4), Color(0xFF4E7A6A), be)
        drawLine(Color(0xFFC0503A), Offset(mix(x0, x1, 0.8f), y - h * 0.08f), Offset(mix(x0, x1, 0.8f), y + h * 0.06f), 4f * dp, StrokeCap.Round, alpha = be * seg(p, 0.75f, 0.9f))
        tagAt(a.tag("beforeeidprayer", 0, 0), morning + Offset(0f, h * 0.12f), be * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("beforeeidprayer", 0, 1), Offset(mix(x0, x1, 0.2f), y - h * 0.14f), be * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (fv > 0f) {
        val p = a.b("foodnotvalue")
        fqCoins(Offset(w * 0.22f, ground - h * 0.25f), w * 0.05f, 5, fv)
        fqCloth(Offset(w * 0.22f, ground - h * 0.08f), w * 0.18f, h * 0.1f, Color(0xFF8A9AC8), fv, t)
        cross14(Offset(w * 0.22f, ground - h * 0.42f), w * 0.05f, seg(p, 0.2f, 0.4f))
        saa73(Offset(w * 0.68f, ground), h * 0.3f, 1f, grain, fv)
        tick14(Offset(w * 0.68f, ground - h * 0.42f), w * 0.05f, seg(p, 0.5f, 0.7f))
        tagAt(a.tag("foodnotvalue"), Offset(w * 0.68f, h * 0.18f), fv * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
