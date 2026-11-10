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
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 60 (2026-11-03): «الأربعون النووية», hadiths 16–20. The ember of anger and the water that cools it; excellence written
 * on everything, down to the last moment of an animal; taqwa wherever you are; guard Allah's limits and He guards you;
 * modesty. Allah is never drawn; the Prophet ﷺ is only light; Shaytan is dark smoke; companions are names in roundels.
 */

private val Back60 = Color(0xE6181420)

// ═════════════ 1. the ember of anger (nw-16) ═════════════

private object AngerC60 {
    val Night = Color(0xFF1A1424)
    val NightLow = Color(0xFF3A2A3E)
    val Ground = Color(0xFF241C26)
    val Ember = Color(0xFFE0603A)
    val Gold = Color(0xFFF2C860)
    val Water = Color(0xFF8AC4EA)
    val Steam = Color(0xFFD8DCE6)
    val Person = Color(0xFFE2DACA)
    val Path = Color(0xFF6A5A6A)
    val Smoke = Color(0xFF2A2430)
}

/**
 * Beats: repeatnoanger (the same short words come again and again: «لَا تَغْضَبْ»), pulledoff (a red glow pulls a figure off a lit
 * path), emberquench (an ember in the chest; water falls on it and it goes out in steam: «جَمْرَةٌ فِي الْقَلْبِ»), sitthenlie
 * (standing, then sitting, then lying down, the red flame smaller each time), smokeandwater (dark smoke breathes fire; water
 * puts the fire out), trulystrong (a figure throws another down; a third holds his own red flame down with both hands and it
 * fades: «يَمْلِكُ نَفْسَهُ عِنْدَ الْغَضَبِ»).
 */
internal fun DrawScope.angeremberScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "repeatnoanger" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "pulledoff" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
        "emberquench" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "sitthenlie" to Shot(Offset(w * 0.5f, h * 0.7f), 1.1f),
        "smokeandwater" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "trulystrong" to Shot(Offset(w * 0.5f, h * 0.65f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { angerNight(a) }
}

private fun DrawScope.flame60(c: Offset, s: Float, t: Float, alpha: Float, color: Color = AngerC60.Ember) {
    if (alpha <= 0.01f || s <= 0f) return
    val fl = 1f + 0.12f * sin(t * 11f)
    drawPath(Path().apply {
        moveTo(c.x, c.y - s * 1.6f * fl)
        quadraticTo(c.x + s * 0.9f, c.y - s * 0.4f, c.x, c.y + s * 0.5f)
        quadraticTo(c.x - s * 0.9f, c.y - s * 0.4f, c.x, c.y - s * 1.6f * fl)
    }, color, alpha = alpha)
    softGlow(c, s * 2.2f, color, 0.4f * alpha)
}

private fun DrawScope.angerNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rn = a.b("repeatnoanger")
    val po = a.b("pulledoff")
    val eq = a.b("emberquench")
    val sl = a.b("sitthenlie")
    val sw = a.b("smokeandwater")
    val ts = a.b("trulystrong")
    val ground = h * 0.84f
    nightSky(t, AngerC60.Night, AngerC60.NightLow, ground, starAlpha = 0.6f)
    drawRect(AngerC60.Ground, Offset(0f, ground), Size(w, h - ground))
    // a man asks for advice; the light ﷺ answers, and answers again: «لَا تَغْضَبْ»
    if (rn > 0f) {
        val f = easeOut(seg(rn, 0f, BEAT_IN)) * (1f - seg(po, 0f, BEAT_OUT))
        val asker = Offset(w * 0.2f, ground)
        figure(asker, h * 0.3f, AngerC60.Person, f, arms = 0.5f)
        val light = Offset(w * 0.8f, h * 0.42f)
        sirajMunir(light, w * 0.014f, f, t, nameAt = light + Offset(0f, h * 0.13f))
        for (k in 0 until 3) {
            val ask = seg(rn, 0.04f + 0.3f * k, 0.12f + 0.3f * k) * (1f - seg(rn, 0.16f + 0.3f * k, 0.22f + 0.3f * k))
            qmark60(asker + Offset(0f, -h * 0.4f), h * 0.05f, AngerC60.Person, f * ask)
            val g = seg(rn, 0.14f + 0.28f * k, 0.28f + 0.28f * k)
            tagAt(a.tag("repeatnoanger"), Offset(w * (0.62f - 0.12f * k), h * (0.28f + 0.13f * k)), f * g, back = Back60, ink = AngerC60.Water)
        }
    }
    // anger is evil in the religion: a man on the lit way, then the flame in his chest turns him onto a dark, thorny one
    if (po > 0f) {
        val f = easeOut(seg(po, 0f, BEAT_IN)) * (1f - seg(eq, 0f, BEAT_OUT))
        val y = ground - h * 0.01f
        drawLine(AngerC60.Gold, Offset(w * 0.5f, y), Offset(w * 0.98f, y), h * 0.022f, StrokeCap.Round, alpha = f * 0.75f)
        softGlow(Offset(w * 0.95f, y), w * 0.08f, AngerC60.Gold, 0.5f * f)
        drawLine(AngerC60.Path, Offset(w * 0.5f, y), Offset(w * 0.02f, y), h * 0.022f, StrokeCap.Round, alpha = f)
        for (k in 0 until 8) {
            val x = w * (0.08f + 0.05f * k)
            drawPath(Path().apply { moveTo(x - w * 0.008f, y - h * 0.01f); lineTo(x, y - h * 0.05f - h * 0.015f * (k % 2)); lineTo(x + w * 0.008f, y - h * 0.01f); close() }, AngerC60.Path, alpha = f)
        }
        val turn = easeInOut(seg(po, 0.35f, 0.5f))
        val x = if (po < 0.35f) mix(w * 0.42f, w * 0.6f, seg(po, 0f, 0.35f)) else mix(w * 0.6f, w * 0.24f, easeInOut(seg(po, 0.45f, 0.95f)))
        val foot = Offset(x, y)
        val tall = h * 0.3f
        withTransform({ scale(if (turn > 0.5f) 1f else -1f, 1f, pivot = foot) }) { figure(foot, tall, AngerC60.Person, f, walk = t * 5f, lean = 0.1f * turn) }
        flame60(foot + Offset(0f, -tall * 0.55f), w * 0.016f * (0.4f + 1.2f * seg(po, 0.15f, 0.4f)), t, f * seg(po, 0.1f, 0.3f))
        tagAt(a.tag("pulledoff"), Offset(w * 0.22f, h * 0.48f), f * seg(po, 0.55f, 0.8f), back = Back60, ink = AngerC60.Ember)
    }
    // an ember in the heart: a glowing coal, the heat rising off it
    if (eq > 0f) {
        val f = easeOut(seg(eq, 0f, BEAT_IN)) * (1f - seg(sl, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.48f)
        val s = h * 0.36f * easeOut(seg(eq, 0f, 0.3f))
        heart12(c, s, Color(0xFF8A3A3A), f, fill = true)
        heart12(c, s, AngerC60.Ember, f * 0.9f)
        val pulse = 0.75f + 0.25f * sin(t * 6f)
        softGlow(c + Offset(0f, h * 0.03f), w * 0.1f, AngerC60.Ember, f * pulse * seg(eq, 0.15f, 0.35f))
        val coal = c + Offset(0f, h * 0.04f)
        drawOval(Color(0xFF3A1A14), topLeft = coal - Offset(w * 0.045f, h * 0.03f), size = Size(w * 0.09f, h * 0.06f), alpha = f * seg(eq, 0.15f, 0.35f))
        drawOval(Color(0xFFFFA040), topLeft = coal - Offset(w * 0.03f, h * 0.018f), size = Size(w * 0.06f, h * 0.036f), alpha = f * pulse * seg(eq, 0.2f, 0.4f))
        for (k in 0 until 3) {
            val u = fract(t * 0.7f + k / 3f)
            val x0 = coal.x + w * 0.03f * (k - 1)
            drawPath(Path().apply {
                moveTo(x0, coal.y - h * 0.05f - h * 0.12f * u)
                quadraticTo(x0 + w * 0.012f, coal.y - h * 0.08f - h * 0.12f * u, x0, coal.y - h * 0.11f - h * 0.12f * u)
            }, Color(0xFFFFC080), alpha = f * (1f - u) * seg(eq, 0.3f, 0.5f), style = Stroke(1.6f * dp, cap = StrokeCap.Round))
        }
        tagAt(a.tag("emberquench"), Offset(w * 0.5f, h * 0.8f), f * seg(eq, 0.1f, 0.3f), back = Back60, ink = AngerC60.Ember)
    }
    // standing, then sitting, then lying: the flame in his chest smaller each time
    if (sl > 0f) {
        val f = easeOut(seg(sl, 0f, BEAT_IN)) * (1f - seg(sw, 0f, 0.4f))
        val foot = Offset(w * 0.5f, ground)
        val tall = h * 0.36f
        val stage = if (sl < 0.35f) 0 else if (sl < 0.65f) 1 else 2
        val chest = when (stage) {
            0 -> { figure(foot, tall, AngerC60.Person, f); foot + Offset(0f, -tall * 0.62f) }
            1 -> {
                drawRoundRect(Color(0xFF5A4A50), topLeft = Offset(w * 0.47f, ground - tall * 0.2f), size = Size(w * 0.08f, tall * 0.2f), cornerRadius = CornerRadius(tall * 0.03f), alpha = f)
                val seat = Offset(w * 0.5f, ground - tall * 0.2f)
                sit12(seat, tall, AngerC60.Person, f, 1f)
                seat + Offset(0f, -tall * 0.38f)
            }
            else -> {
                val pivot = Offset(w * 0.5f, ground - h * 0.02f)
                withTransform({ rotate(-90f, pivot) }) { figure(pivot + Offset(tall * 0.5f, 0f), tall, AngerC60.Person, f) }
                Offset(w * 0.5f - tall * 0.12f, ground - h * 0.06f)
            }
        }
        val s = w * 0.022f * (1f - 0.35f * stage) * (1f - seg(sl, 0.85f, 1f))
        flame60(chest, s, t, f)
    }
    // the one who drives him to it is Shaytan, made of fire; water puts the fire out
    if (sw > 0f) {
        val f = easeOut(seg(sw, 0f, 0.25f)) * (1f - seg(ts, 0f, BEAT_OUT))
        val me = Offset(w * 0.62f, ground)
        val tall = h * 0.32f
        val wet = seg(sw, 0.5f, 0.85f)
        figure(me, tall, AngerC60.Person, f, arms = 0.4f * (1f - wet))
        val base = Offset(w * 0.24f, ground)
        flame60(base + Offset(0f, -h * 0.02f), w * 0.03f, t, f * seg(sw, 0.05f, 0.2f) * (1f - wet))
        smoke12(base, me + Offset(-w * 0.04f, -tall * 0.7f), seg(sw, 0.05f, 0.35f), t, f * (1f - 0.7f * wet), Color(0xFF7A6A82))
        flame60(me + Offset(0f, -tall * 0.62f), w * 0.02f * (1f - wet), t, f * seg(sw, 0.2f, 0.35f))
        rain12(w * 0.5f, w * 0.74f, h * 0.12f, h * 0.7f, t, AngerC60.Water, f * seg(sw, 0.45f, 0.55f), 20)
        for (k in 0 until 4) {
            val u = fract(t * 0.5f + k * 0.25f)
            drawCircle(AngerC60.Steam, w * 0.018f * (0.5f + u), me + Offset(w * 0.02f * sin(k * 2f), -tall * 0.7f - h * 0.12f * u), alpha = f * wet * (1f - u) * 0.6f)
        }
        tagAt(a.tag("smokeandwater", 0, 0), Offset(w * 0.24f, h * 0.36f), f * seg(sw, 0.2f, 0.4f) * (1f - wet), back = Back60, ink = Color(0xFFC8B8D0))
        tagAt(a.tag("smokeandwater", 0, 1), base + Offset(0f, -h * 0.12f), f * seg(sw, 0.25f, 0.45f) * (1f - wet), back = Back60, ink = AngerC60.Ember)
    }
    // not the wrestler who throws others down: the strong one holds his own flame down until it goes out
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, 0.25f))
        val throwP = easeInOut(seg(ts, 0.1f, 0.35f))
        val tall = h * 0.28f
        figure(Offset(w * 0.22f, ground), tall, AngerC60.Person, f, arms = 0.8f)
        if (throwP < 0.9f) figure(Offset(w * 0.32f, mix(ground, ground - h * 0.02f, throwP)), tall, AngerC60.Person, f * 0.7f, lean = 1.2f * throwP)
        else {
            val pivot = Offset(w * 0.3f, ground - h * 0.015f)
            withTransform({ rotate(-90f, pivot) }) { figure(pivot + Offset(tall * 0.5f, 0f), tall, AngerC60.Person, f * 0.7f) }
        }
        val calm = seg(ts, 0.45f, 0.9f)
        val me = Offset(w * 0.75f, ground)
        figure(me, h * 0.34f, AngerC60.Person, f * seg(ts, 0.35f, 0.5f))
        hands12(me + Offset(0f, -h * 0.22f), h * 0.06f, AngerC60.Person, f * seg(ts, 0.4f, 0.55f))
        flame60(me + Offset(0f, -h * 0.24f), w * 0.025f * (1f - calm), t, f * seg(ts, 0.35f, 0.5f))
        softGlow(me + Offset(0f, -h * 0.2f), w * 0.1f, AngerC60.Gold, 0.5f * f * calm)
        tagAt(a.tag("trulystrong"), Offset(w * 0.5f, h * 0.2f), f * seg(ts, 0.55f, 0.75f), back = Back60, ink = ArtInk.Gold)
    }
}

/** A drawn question mark, centred at [c]. */
private fun DrawScope.qmark60(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawArc(color, 180f, 270f, false, topLeft = c - Offset(s * 0.5f, s * 0.9f), size = Size(s, s), alpha = alpha, style = Stroke(s * 0.18f, cap = StrokeCap.Round))
    drawLine(color, c + Offset(0f, s * 0.1f), c + Offset(0f, s * 0.3f), s * 0.18f, StrokeCap.Round, alpha = alpha)
    drawCircle(color, s * 0.1f, c + Offset(0f, s * 0.58f), alpha = alpha)
}

// ═════════════ 2. excellence written on everything (nw-17) ═════════════

private object IhsanC60 {
    val Sky = Color(0xFFC8DCE6)
    val SkyLow = Color(0xFFF0E8D4)
    val Ground = Color(0xFFB8C890)
    val Scroll = Color(0xFFF2E8D2)
    val Gold = Color(0xFFD89A2A)
    val Ink = Color(0xFF2E3A4A)
    val Stone = Color(0xFF8A8A8A)
    val Blade = Color(0xFFD8DCE6)
    val Spark = Color(0xFFF2C860)
    val Sheep = Color(0xFFF2EEE4)
    val Water = Color(0xFF5AA0D0)
    val Tree = Color(0xFF4E8A54)
    val Bad = Color(0xFFB4492A)
    val Weight = Color(0xFF6A5A4A)
}

/**
 * Beats: twowritings (two scrolls side by side: «كَوْنِيَّةٌ قَدَرِيَّةٌ», «وَشَرْعِيَّةٌ دِينِيَّةٌ»; the second one lights),
 * sharpenrest (a blade is drawn along a whetstone with sparks while a sheep rests in shade with water: «وَلْيُحِدَّ أَحَدُكُمْ
 * شَفْرَتَهُ», «وَلْيُرِحْ ذَبِيحَتَهُ»), bluntno (a dull, notched blade is crossed out), outweighharm (a scale: a small dark
 * weight on one pan, a gold one heavier on the other), reachesbeast (a warm light spreads from roundels to trees and to a
 * resting animal).
 */
internal fun DrawScope.ihsaneverythingScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twowritings" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "sharpenrest" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "bluntno" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "outweighharm" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "reachesbeast" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { ihsanDay(a) }
}

private fun DrawScope.blade60(c: Offset, s: Float, color: Color, alpha: Float, notched: Boolean = false) {
    if (alpha <= 0.01f) return
    val p = Path().apply {
        moveTo(c.x - s, c.y)
        quadraticTo(c.x, c.y + s * 0.35f, c.x + s, c.y - s * 0.05f)
        if (notched) for (k in 0 until 5) { val x = c.x + s - k * s * 0.4f; lineTo(x - s * 0.1f, c.y - s * 0.12f); lineTo(x - s * 0.2f, c.y - s * 0.05f) }
        lineTo(c.x - s, c.y - s * 0.15f)
        close()
    }
    drawPath(p, color, alpha = alpha)
    drawRoundRect(Color(0xFF6A4A32), Offset(c.x - s * 1.5f, c.y - s * 0.16f), Size(s * 0.5f, s * 0.2f), CornerRadius(s * 0.05f), alpha = alpha)
}

private fun DrawScope.ihsanDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tw = a.b("twowritings")
    val sr = a.b("sharpenrest")
    val bn = a.b("bluntno")
    val oh = a.b("outweighharm")
    val rb = a.b("reachesbeast")
    val ground = h * 0.84f
    daySky12(IhsanC60.Sky, IhsanC60.SkyLow, ground, IhsanC60.Ground)
    if (tw > 0f) {
        val f = easeOut(seg(tw, 0f, BEAT_IN)) * (1f - seg(sr, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val c = Offset(w * (0.72f - 0.44f * k), h * 0.5f)
            val g = seg(tw, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            scroll14(c, w * 0.3f, h * 0.36f, easeOut(g), f)
            if (k == 1) softGlow(c, w * 0.22f, IhsanC60.Gold, 0.4f * f * seg(tw, 0.6f, 0.85f))
            tagAt(a.tag("twowritings", 0, k), c + Offset(0f, -h * 0.26f), f * g, back = Back60, ink = if (k == 1) ArtInk.Gold else Color.Unspecified)
        }
    }
    if (sr > 0f) {
        val f = easeOut(seg(sr, 0f, BEAT_IN)) * (1f - seg(bn, 0f, BEAT_OUT))
        val stone = Offset(w * 0.72f, ground - h * 0.06f)
        drawRoundRect(IhsanC60.Stone, Offset(stone.x - w * 0.12f, stone.y - h * 0.025f), Size(w * 0.24f, h * 0.05f), CornerRadius(6f * dp), alpha = f)
        val stroke = sin(t * 5f) * w * 0.06f
        blade60(stone + Offset(stroke, -h * 0.05f), w * 0.07f, IhsanC60.Blade, f)
        for (k in 0 until 4) {
            val u = fract(t * 2f + k * 0.25f)
            drawCircle(IhsanC60.Spark, w * 0.004f, stone + Offset(stroke + w * 0.06f + w * 0.05f * u, -h * 0.03f - h * 0.05f * u * (1f - u) * 4f), alpha = f * (1f - u))
        }
        palm12(Offset(w * 0.22f, ground), h * 0.5f, Color(0xFF6A4A32), IhsanC60.Tree, f, t)
        drawOval(IhsanC60.Ink, Offset(w * 0.08f, ground - h * 0.01f), Size(w * 0.3f, h * 0.03f), alpha = f * 0.15f)
        sheep26(Offset(w * 0.24f, ground - h * 0.04f), w * 0.09f, IhsanC60.Sheep, f)
        cup12(Offset(w * 0.38f, ground), w * 0.06f, IhsanC60.Stone, f, IhsanC60.Water)
        tagAt(a.tag("sharpenrest", 0, 0), Offset(w * 0.72f, h * 0.45f), f * seg(sr, 0.1f, 0.3f), back = Back60, ink = ArtInk.Gold)
        tagAt(a.tag("sharpenrest", 0, 1), Offset(w * 0.28f, h * 0.2f), f * seg(sr, 0.5f, 0.7f), back = Back60, ink = ArtInk.Gold)
    }
    if (bn > 0f) {
        val f = easeOut(seg(bn, 0f, BEAT_IN)) * (1f - seg(oh, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.5f)
        blade60(c, w * 0.16f, IhsanC60.Stone, f, notched = true)
        crossMark(c, w * 0.18f, IhsanC60.Bad, 6f * dp, f * seg(bn, 0.35f, 0.55f))
    }
    if (oh > 0f) {
        val f = easeOut(seg(oh, 0f, BEAT_IN)) * (1f - seg(rb, 0f, 0.4f))
        val tilt = mix(-0.4f, 0.6f, easeInOut(seg(oh, 0.4f, 0.8f)))
        val foot = Offset(w * 0.5f, ground)
        val s = h * 0.6f
        balance12(foot, s, tilt, IhsanC60.Weight, f)
        val (lp, rp) = balancePans12(foot, s, tilt)
        drawRect(IhsanC60.Ink, Offset(rp.x - w * 0.025f, rp.y - h * 0.04f), Size(w * 0.05f, h * 0.035f), alpha = f * seg(oh, 0.1f, 0.25f))
        drawRect(IhsanC60.Gold, Offset(lp.x - w * 0.04f, lp.y - h * 0.06f), Size(w * 0.08f, h * 0.055f), alpha = f * seg(oh, 0.3f, 0.45f))
        softGlow(lp + Offset(0f, -h * 0.03f), w * 0.07f, IhsanC60.Gold, 0.5f * f * seg(oh, 0.3f, 0.6f))
    }
    if (rb > 0f) {
        val f = easeOut(seg(rb, 0f, 0.25f))
        val spread = seg(rb, 0.1f, 0.8f)
        val src = Offset(w * 0.85f, ground - h * 0.08f)
        softGlow(src, w * 0.06f + w * 0.6f * spread, IhsanC60.Gold, 0.25f * f)
        for (k in 0 until 3) nameRoundel(Offset(w * (0.78f + 0.06f * k), ground - h * (0.06f + 0.05f * (k % 2))), w * 0.025f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        for (k in 0 until 2) palm12(Offset(w * (0.45f + 0.12f * k), ground), h * 0.32f, Color(0xFF6A4A32), IhsanC60.Tree, f * seg(rb, 0.2f + 0.1f * k, 0.4f + 0.1f * k), t + k)
        sheep26(Offset(w * 0.18f, ground - h * 0.04f), w * 0.09f, IhsanC60.Sheep, f * seg(rb, 0.45f, 0.6f))
        softGlow(Offset(w * 0.18f, ground - h * 0.06f), w * 0.1f, IhsanC60.Gold, 0.5f * f * seg(rb, 0.6f, 0.9f))
    }
}

// ═════════════ 3. taqwa wherever you are (nw-18) ═════════════

private object TaqwaC60 {
    val Night = Color(0xFF121A2E)
    val Day = Color(0xFF8AB4D8)
    val Ground = Color(0xFF1E2232)
    val Gold = Color(0xFFF2C860)
    val Shield = Color(0xFFBFE6FF)
    val Storm = Color(0xFF3A3448)
    val Cloth = Color(0xFF5A6A8A)
    val Tear = Color(0xFF2A2A38)
    val Step = Color(0xFFD8CCB4)
    val Gift = Color(0xFFB04A3A)
}

/**
 * Beats: muadhabudharr (two roundels with their names, «مُعَاذًا» and «وَأَبَا ذَرٍّ», and between them two lights: what is
 * Allah's due and what is people's), shieldsecret (a roundel with a shield of light between it and a storm, by night and by
 * day: «فِي السِّرِّ وَالْعَلَانِيَةِ»), patchtear (a torn cloth is mended with a gold stitch: «أَتْبِعِ السَّيِّئَةَ الْحَسَنَةَ
 * تَمْحُهَا»), tworanks (two steps, the second higher: «الْأَبْرَارِ», «السَّابِقِينَ»), fourmanners (roundels meet: a bright
 * face, a greeting, a gift, a pardon: «بِانْطِلَاقِ الْوَجْهِ», «وَإِفْشَاءِ السَّلَامِ», «وَالْهَدِيَّةِ», «وَالْمُسَامَحَةِ»).
 */
internal fun DrawScope.taqwaeverywhereScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "muadhabudharr" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "shieldsecret" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "patchtear" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "tworanks" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "fourmanners" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { taqwaNight(a) }
}

private fun DrawScope.taqwaNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val mb = a.b("muadhabudharr")
    val ss = a.b("shieldsecret")
    val pt = a.b("patchtear")
    val tr = a.b("tworanks")
    val fm = a.b("fourmanners")
    val ground = h * 0.86f
    nightSky(t, TaqwaC60.Night, Color(0xFF2E3456), ground, starAlpha = 0.8f)
    drawRect(TaqwaC60.Ground, Offset(0f, ground), Size(w, h - ground))
    if (mb > 0f) {
        val f = easeOut(seg(mb, 0f, BEAT_IN)) * (1f - seg(ss, 0f, BEAT_OUT))
        nameRoundel(Offset(w * 0.75f, h * 0.55f), w * 0.1f, a.big("muadhabudharr", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(mb, 0.05f, 0.25f))
        nameRoundel(Offset(w * 0.25f, h * 0.55f), w * 0.1f, a.big("muadhabudharr", 0, 1), Roundel.Fills[1], Roundel.Rim, f * seg(mb, 0.2f, 0.4f))
        softGlow(Offset(w * 0.5f, h * 0.32f), w * 0.06f, TaqwaC60.Gold, f * seg(mb, 0.45f, 0.65f))
        softGlow(Offset(w * 0.5f, h * 0.72f), w * 0.06f, TaqwaC60.Shield, f * seg(mb, 0.6f, 0.8f))
    }
    if (ss > 0f) {
        val f = easeOut(seg(ss, 0f, BEAT_IN)) * (1f - seg(pt, 0f, BEAT_OUT))
        drawRect(TaqwaC60.Day, Offset(w * 0.5f, 0f), Size(w * 0.5f, ground), alpha = f * 0.5f * seg(ss, 0.4f, 0.6f))
        for (k in 0 until 2) {
            val c = Offset(w * (0.27f + 0.5f * k), h * 0.6f)
            nameRoundel(c, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f * seg(ss, 0.05f + 0.35f * k, 0.2f + 0.35f * k))
            drawArc(TaqwaC60.Shield, 200f, 140f, false, Offset(c.x - w * 0.12f, c.y - h * 0.14f), Size(w * 0.24f, h * 0.24f), alpha = f * seg(ss, 0.15f + 0.35f * k, 0.3f + 0.35f * k), style = Stroke(5f * dp, cap = StrokeCap.Round))
            for (j in 0 until 6) {
                val u = fract(t * 1.3f + j / 6f)
                drawCircle(TaqwaC60.Storm, w * 0.006f, Offset(c.x - w * 0.12f + w * 0.24f * fract(j * 0.37f), mix(h * 0.15f, c.y - h * 0.14f, u)), alpha = f * 0.8f)
            }
        }
        tagAt(a.tag("shieldsecret"), Offset(w * 0.5f, h * 0.88f), f * seg(ss, 0.6f, 0.8f), back = Back60, ink = ArtInk.Gold)
    }
    if (pt > 0f) {
        val f = easeOut(seg(pt, 0f, BEAT_IN)) * (1f - seg(tr, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.5f)
        drawRoundRect(TaqwaC60.Cloth, Offset(c.x - w * 0.25f, c.y - h * 0.18f), Size(w * 0.5f, h * 0.36f), CornerRadius(8f * dp), alpha = f)
        val mend = seg(pt, 0.35f, 0.8f)
        val tear = Path().apply { moveTo(c.x - w * 0.12f, c.y - h * 0.1f); lineTo(c.x - w * 0.02f, c.y); lineTo(c.x - w * 0.08f, c.y + h * 0.04f); lineTo(c.x + w * 0.1f, c.y + h * 0.12f) }
        drawPath(tear, TaqwaC60.Tear, alpha = f * (1f - mend), style = Stroke(6f * dp, cap = StrokeCap.Round))
        for (k in 0 until 8) {
            val u = k / 7f
            if (u > mend) continue
            val p = Offset(mix(c.x - w * 0.12f, c.x + w * 0.1f, u), mix(c.y - h * 0.1f, c.y + h * 0.12f, u))
            drawLine(TaqwaC60.Gold, p + Offset(-w * 0.015f, -h * 0.01f), p + Offset(w * 0.015f, h * 0.01f), 2.5f * dp, StrokeCap.Round, alpha = f)
        }
        softGlow(c, w * 0.15f, TaqwaC60.Gold, 0.3f * f * mend)
        tagAt(a.tag("patchtear"), Offset(w * 0.5f, h * 0.2f), f * seg(pt, 0.2f, 0.4f), back = Back60, ink = ArtInk.Gold)
    }
    if (tr > 0f) {
        val f = easeOut(seg(tr, 0f, BEAT_IN)) * (1f - seg(fm, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val g = seg(tr, 0.1f + 0.35f * k, 0.3f + 0.35f * k)
            val x = w * (0.62f - 0.3f * k)
            val top = ground - h * (0.18f + 0.18f * k)
            drawRect(TaqwaC60.Step, Offset(x - w * 0.14f, top), Size(w * 0.28f, ground - top), alpha = f * g)
            nameRoundel(Offset(x, top - h * 0.06f), w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f * g)
            if (k == 1) softGlow(Offset(x, top - h * 0.06f), w * 0.1f, TaqwaC60.Gold, 0.5f * f * g)
            tagAt(a.tag("tworanks", 0, k), Offset(x, top - h * 0.16f), f * g, back = Back60, ink = ArtInk.Gold)
        }
    }
    if (fm > 0f) {
        val f = easeOut(seg(fm, 0f, 0.25f))
        for (k in 0 until 4) {
            val g = seg(fm, 0.08f + 0.2f * k, 0.25f + 0.2f * k)
            val y = h * (0.25f + 0.17f * k)
            val l = Offset(w * 0.22f, y)
            val r = Offset(w * 0.78f, y)
            nameRoundel(l, w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f * g)
            nameRoundel(r, w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f * g)
            when (k) {
                0 -> softGlow(r, w * 0.07f, TaqwaC60.Gold, 0.7f * f * g)
                1 -> voice12(r + Offset(-w * 0.07f, 0f), w * 0.035f, t, TaqwaC60.Shield, f * g, -1f)
                2 -> drawRect(TaqwaC60.Gift, Offset(mix(r.x, l.x, g) - w * 0.02f, y - h * 0.02f), Size(w * 0.04f, h * 0.04f), alpha = f * g)
                else -> heart12(Offset(w * 0.5f, y), w * 0.025f, TaqwaC60.Gold, f * g, fill = true)
            }
            tagAt(a.tag("fourmanners", 0, k), Offset(w * 0.5f, y - h * 0.06f), f * g, back = Back60, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 4. guard Allah and He guards you (nw-19) ═════════════

private object GuardC60 {
    val Sky = Color(0xFFC0D8EA)
    val SkyLow = Color(0xFFF2E6CC)
    val Ground = Color(0xFFD2BC92)
    val Camel = Color(0xFFB88A58)
    val Post = Color(0xFF6A4A32)
    val Person = Color(0xFF3A3040)
    val Gold = Color(0xFFD89A2A)
    val Storm = Color(0xFF6A6A7A)
    val Drop = Color(0xFF5AA0D0)
    val Whip = Color(0xFF5A3A22)
}

/**
 * Beats: ridebehind (a camel with the light ﷺ on it and a small roundel behind him: «ابْنُ عَبَّاسٍ»), keepbounds (a figure
 * keeps within a ring of posts; a dome of light forms over him: «يَحْفَظْكَ فِي دِينِكَ وَدُنْيَاكَ»), crowdcannot (a crowd of
 * plain roundels presses toward one but stops at a written line: «قَدْ كَتَبَهُ اللَّهُ»), ownwhip (a rider's whip falls; he
 * gets down and picks it up himself), easethenhard (a figure bows on a clear day; then a storm comes and a dome holds over
 * him: «فِي الرَّخَاءِ», «فِي الشِّدَّةِ»), missandhit (drops fall: one passes by a cup, one lands in it).
 */
internal fun DrawScope.guardhimScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "ridebehind" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "keepbounds" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
        "crowdcannot" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "ownwhip" to Shot(Offset(w * 0.5f, h * 0.65f), 1.1f),
        "easethenhard" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "missandhit" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { guardDay(a) }
}

private fun DrawScope.guardDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rb = a.b("ridebehind")
    val kb = a.b("keepbounds")
    val cc = a.b("crowdcannot")
    val ow = a.b("ownwhip")
    val eh = a.b("easethenhard")
    val mh = a.b("missandhit")
    val ground = h * 0.84f
    val storm = seg(eh, 0.45f, 0.6f) * (1f - seg(mh, 0f, BEAT_OUT))
    daySky12(lerpColor(GuardC60.Sky, GuardC60.Storm, storm), lerpColor(GuardC60.SkyLow, GuardC60.Storm, storm * 0.6f), ground, GuardC60.Ground)
    if (rb > 0f) {
        val f = easeOut(seg(rb, 0f, 0.25f)) * (1f - seg(kb, 0f, BEAT_OUT))
        val foot = Offset(w * 0.5f, ground)
        camel26(foot, h * 0.36f, GuardC60.Camel, f, t, t * 2f)
        val saddle = foot + Offset(0f, -h * 0.34f)
        sirajMunir(saddle + Offset(w * 0.02f, -h * 0.04f), w * 0.011f, f, t, nameAt = saddle + Offset(w * 0.02f, -h * 0.15f))
        nameRoundel(saddle + Offset(w * 0.12f, -h * 0.02f), w * 0.045f, a.big("ridebehind"), Roundel.Fills[3], Roundel.Rim, f * seg(rb, 0.2f, 0.4f))
    }
    if (kb > 0f) {
        val f = easeOut(seg(kb, 0f, BEAT_IN)) * (1f - seg(cc, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, ground)
        for (k in 0 until 10) {
            val ang = PI.toFloat() * k / 9f
            val p = c + Offset(cos(ang) * w * 0.32f, -sin(ang) * h * 0.04f + h * 0.04f)
            drawLine(GuardC60.Post, p, p + Offset(0f, -h * 0.07f), 3f * dp, StrokeCap.Round, alpha = f)
        }
        figure(c, h * 0.3f, GuardC60.Person, f, walk = t * 2f * (1f - seg(kb, 0.4f, 0.5f)))
        guardDome12(c, w * 0.26f, seg(kb, 0.35f, 0.7f), t, f)
        tagAt(a.tag("keepbounds"), Offset(w * 0.5f, h * 0.18f), f * seg(kb, 0.5f, 0.7f), back = Back60, ink = ArtInk.Gold)
    }
    if (cc > 0f) {
        val f = easeOut(seg(cc, 0f, BEAT_IN)) * (1f - seg(ow, 0f, BEAT_OUT))
        val me = Offset(w * 0.18f, h * 0.55f)
        nameRoundel(me, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val line = w * 0.34f
        drawLine(GuardC60.Gold, Offset(line, h * 0.25f), Offset(line, h * 0.85f), 3f * dp, StrokeCap.Round, alpha = f * seg(cc, 0.3f, 0.45f))
        val push = easeOut(seg(cc, 0.05f, 0.4f))
        for (k in 0 until 18) {
            val x = mix(w * (0.65f + 0.3f * fract(k * 0.41f)), maxOf(line + w * 0.05f, w * (0.4f + 0.3f * fract(k * 0.41f))), push)
            val y = h * (0.3f + 0.5f * fract(k * 0.618f))
            nameRoundel(Offset(x, y), w * 0.025f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        }
        tagAt(a.tag("crowdcannot"), Offset(line, h * 0.18f), f * seg(cc, 0.4f, 0.6f), back = Back60, ink = ArtInk.Gold)
    }
    if (ow > 0f) {
        val f = easeOut(seg(ow, 0f, BEAT_IN)) * (1f - seg(eh, 0f, BEAT_OUT))
        val camel = Offset(w * 0.55f, ground)
        camel26(camel, h * 0.34f, GuardC60.Camel, f, t)
        val fall = easeIn60(seg(ow, 0.1f, 0.3f))
        val down = easeInOut(seg(ow, 0.35f, 0.6f))
        val pick = seg(ow, 0.65f, 0.85f)
        val whipY = mix(ground - h * 0.3f, ground - h * 0.01f, fall) - h * 0.2f * pick
        drawLine(GuardC60.Whip, Offset(w * 0.4f, whipY), Offset(w * 0.46f, whipY - h * 0.01f), 3f * dp, StrokeCap.Round, alpha = f)
        if (down < 0.5f) sit12(camel + Offset(0f, -h * 0.28f), h * 0.2f, GuardC60.Person, f * (1f - 2f * down), 1f)
        else figure(Offset(w * 0.42f, ground), h * 0.26f, GuardC60.Person, f * (2f * down - 1f), arms = 0.4f * pick)
    }
    if (eh > 0f) {
        val f = easeOut(seg(eh, 0f, BEAT_IN)) * (1f - seg(mh, 0f, BEAT_OUT))
        val foot = Offset(w * 0.5f, ground)
        val cyc = fract(eh * 3f)
        bow12(foot, h * 0.32f, GuardC60.Person, f, if (cyc < 0.5f) 0.5f else 1f)
        if (storm > 0f) rain12(0f, w, 0f, ground, t, GuardC60.Drop, f * storm, 30)
        guardDome12(foot, w * 0.18f, storm, t, f)
        tagAt(a.tag("easethenhard", 0, 0), Offset(w * 0.25f, h * 0.2f), f * seg(eh, 0.1f, 0.25f) * (1f - storm * 0.5f), back = Back60, ink = ArtInk.Gold)
        tagAt(a.tag("easethenhard", 0, 1), Offset(w * 0.75f, h * 0.2f), f * seg(eh, 0.55f, 0.7f), back = Back60, ink = ArtInk.Gold)
    }
    if (mh > 0f) {
        val f = easeOut(seg(mh, 0f, 0.25f))
        val cup = Offset(w * 0.5f, ground)
        cup12(cup, w * 0.12f, Color(0xFFD8CCB4), f)
        for (k in 0 until 2) {
            val u = easeIn60(seg(mh, 0.15f + 0.35f * k, 0.45f + 0.35f * k))
            val x = if (k == 0) mix(w * 0.42f, w * 0.3f, u) else w * 0.5f
            val y = mix(h * 0.12f, if (k == 0) ground + h * 0.05f else ground - h * 0.08f, u)
            drawCircle(GuardC60.Drop, w * 0.012f, Offset(x, y), alpha = f * seg(mh, 0.1f + 0.35f * k, 0.15f + 0.35f * k))
            if (k == 1 && u >= 1f) softGlow(cup + Offset(0f, -h * 0.06f), w * 0.06f, GuardC60.Drop, 0.6f * f)
        }
    }
}

private fun easeIn60(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ═════════════ 5. modesty (nw-20) ═════════════

private object HayaC60 {
    val Dusk = Color(0xFF221E36)
    val DuskLow = Color(0xFF5A4660)
    val Ground = Color(0xFF2A2430)
    val Gold = Color(0xFFF2C860)
    val Lamp = Color(0xFFB8925A)
    val Red = Color(0xFFD06A5A)
    val Person = Color(0xFFE2DACA)
    val Dark = Color(0xFF3A3040)
    val Leaf = Color(0xFF6AAA6A)
    val Trunk = Color(0xFF6A4A32)
    val Wall = Color(0xFFC8B494)
    val Water = Color(0xFF8AC4EA)
}

/**
 * Beats: oldprophecy (an old scroll passes along a row of lamps from long ago to now: «إِذَا لَمْ تَسْتَحِ فَاصْنَعْ مَا شِئْتَ»),
 * sternword (the same command glows red, a warning), hayagauge (deeds are held up to a gauge; those that fit glow, the others
 * dim), pullsback (a hand reaches toward a dark thing and pulls back; another reaches without care), placebadr (a roundel with
 * his name, «أَبُو مَسْعُودٍ الْبَدْرِيُّ», by a house and palms at a well), faithbranch (a tree of many branches; one of them
 * lights: «شُعْبَةٌ مِنَ الْإِيمَانِ»).
 */
internal fun DrawScope.modestyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "oldprophecy" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "sternword" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "hayagauge" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "pullsback" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "placebadr" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "faithbranch" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { hayaDusk(a) }
}

private fun DrawScope.hayaDusk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val op = a.b("oldprophecy")
    val sw = a.b("sternword")
    val hg = a.b("hayagauge")
    val pb = a.b("pullsback")
    val bd = a.b("placebadr")
    val fb = a.b("faithbranch")
    val ground = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(HayaC60.Dusk, HayaC60.DuskLow), 0f, ground), Offset.Zero, Size(w, ground))
    drawRect(HayaC60.Ground, Offset(0f, ground), Size(w, h - ground))
    if (op > 0f) {
        val f = easeOut(seg(op, 0f, BEAT_IN)) * (1f - seg(sw, 0f, 0.4f))
        for (k in 0 until 6) {
            val x = w * (0.08f + 0.168f * k)
            drawLine(HayaC60.Lamp, Offset(x, ground), Offset(x, ground - h * 0.12f), 2f * dp, alpha = f)
            softGlow(Offset(x, ground - h * 0.15f), w * 0.03f, HayaC60.Gold, f * (0.3f + 0.1f * k))
        }
        val u = easeInOut(seg(op, 0.1f, 0.7f))
        scroll14(Offset(mix(w * 0.08f, w * 0.92f, u), ground - h * 0.28f), w * 0.12f, h * 0.08f, 0.6f, f)
        tagAt(a.tag("oldprophecy"), Offset(w * 0.5f, h * 0.25f), f * seg(op, 0.5f, 0.7f), back = Back60, ink = ArtInk.Gold)
    }
    if (sw > 0f) {
        val f = easeOut(seg(sw, 0f, 0.25f)) * (1f - seg(hg, 0f, BEAT_OUT))
        softGlow(Offset(w * 0.5f, h * 0.45f), w * 0.3f, HayaC60.Red, 0.3f * f * (0.8f + 0.2f * sin(t * 4f)))
        tagAt(a.tag("oldprophecy"), Offset(w * 0.5f, h * 0.45f), f, back = Back60, ink = HayaC60.Red)
        crossMark(Offset(w * 0.5f, h * 0.65f), w * 0.05f, HayaC60.Red, 4f * dp, f * seg(sw, 0.4f, 0.6f))
    }
    if (hg > 0f) {
        val f = easeOut(seg(hg, 0f, BEAT_IN)) * (1f - seg(pb, 0f, BEAT_OUT))
        val gx = w * 0.5f
        drawRoundRect(HayaC60.Gold, Offset(gx - w * 0.1f, h * 0.25f), Size(w * 0.2f, h * 0.5f), CornerRadius(8f * dp), alpha = f * seg(hg, 0.05f, 0.2f), style = Stroke(3f * dp))
        for (k in 0 until 4) {
            val fits = k % 2 == 0
            val u = easeInOut(seg(hg, 0.15f + 0.18f * k, 0.35f + 0.18f * k))
            val c = Offset(mix(w * 0.9f, gx, u), h * (0.32f + 0.12f * k))
            val sz = if (fits) w * 0.14f else w * 0.26f
            drawRoundRect(if (fits && u >= 1f) HayaC60.Gold else HayaC60.Wall, Offset(c.x - sz / 2f, c.y - h * 0.03f), Size(sz, h * 0.06f), CornerRadius(4f * dp), alpha = f * (if (!fits && u >= 1f) 0.35f else 0.9f))
        }
    }
    if (pb > 0f) {
        val f = easeOut(seg(pb, 0f, BEAT_IN)) * (1f - seg(bd, 0f, BEAT_OUT))
        val dark = Offset(w * 0.5f, ground - h * 0.1f)
        drawCircle(HayaC60.Dark, w * 0.05f, dark, alpha = f)
        smoke12(dark, dark + Offset(0f, -h * 0.15f), 0.6f, t, f * 0.7f)
        val reach = sin(PI.toFloat() * seg(pb, 0.1f, 0.6f))
        figure(Offset(w * 0.25f, ground), h * 0.32f, HayaC60.Person, f, arms = 0.7f * reach, lean = 0.08f * reach)
        softGlow(Offset(w * 0.25f, ground - h * 0.22f), w * 0.08f, HayaC60.Gold, 0.5f * f * seg(pb, 0.45f, 0.65f))
        figure(Offset(w * 0.75f, ground), h * 0.32f, HayaC60.Person, f * 0.6f * seg(pb, 0.5f, 0.65f), arms = -0.8f, lean = -0.1f)
    }
    if (bd > 0f) {
        val f = easeOut(seg(bd, 0f, BEAT_IN)) * (1f - seg(fb, 0f, BEAT_OUT))
        house12(Offset(w * 0.72f, ground), w * 0.2f, HayaC60.Wall, HayaC60.Trunk, f, 0.6f)
        for (k in 0 until 2) palm12(Offset(w * (0.88f + 0.08f * k), ground), h * 0.4f, HayaC60.Trunk, HayaC60.Leaf, f, t + k)
        drawOval(HayaC60.Wall, Offset(w * 0.42f, ground - h * 0.03f), Size(w * 0.1f, h * 0.05f), alpha = f)
        drawOval(HayaC60.Water, Offset(w * 0.435f, ground - h * 0.02f), Size(w * 0.07f, h * 0.025f), alpha = f)
        nameRoundel(Offset(w * 0.3f, h * 0.45f), w * 0.1f, a.big("placebadr"), Roundel.Fills[2], Roundel.Rim, f * seg(bd, 0.1f, 0.3f))
    }
    if (fb > 0f) {
        val f = easeOut(seg(fb, 0f, 0.25f))
        val foot = Offset(w * 0.5f, ground)
        drawLine(HayaC60.Trunk, foot, foot + Offset(0f, -h * 0.35f), w * 0.03f, StrokeCap.Round, alpha = f)
        for (k in 0 until 8) {
            val ang = PI.toFloat() * (1.1f + 0.8f * k / 7f)
            val from = foot + Offset(0f, -h * (0.2f + 0.02f * k))
            val to = from + Offset(cos(ang) * w * 0.3f, sin(ang) * h * 0.25f)
            val lit = k == 5
            val g = seg(fb, 0.05f + 0.05f * k, 0.2f + 0.05f * k)
            drawLine(if (lit) HayaC60.Gold else HayaC60.Trunk, from, from + (to - from) * g, 3f * dp, StrokeCap.Round, alpha = f)
            drawCircle(if (lit) HayaC60.Gold else HayaC60.Leaf, w * 0.03f, to, alpha = f * g)
            if (lit) {
                softGlow(to, w * 0.08f, HayaC60.Gold, f * seg(fb, 0.5f, 0.7f))
                tagAt(a.tag("faithbranch"), to + Offset(0f, -h * 0.08f), f * seg(fb, 0.55f, 0.75f), back = Back60, ink = ArtInk.Gold)
            }
        }
    }
}
