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
 * Wave 39 (2026-10-27): «بصائر»، الجزء الثالث، «أسئلة الوجود». A road of hurdles, a fish that never feels wet and a
 * thread that runs on past the edge; three thermometers summed into one; four gates to knowing Him; a sealed exam and a
 * train; a swimmer who saves a child and walks away; the workbench of «how» under the sky of «why». People are faceless
 * figures; the scholars are books; Allah is never drawn — light from above is only light.
 */

private val Back39 = Color(0xE6181420)

// ═════════════ 1. a road of trials (bs-sharr) ═════════════

private object TrialC39 {
    val Sky = Color(0xFF6A8AB0)
    val SkyLow = Color(0xFFE8D2B0)
    val Ground = Color(0xFFB8986A)
    val Walker = Color(0xFF2E2620)
    val Hurdle = Color(0xFF8A5A3A)
    val Water = Color(0xFF4A8AB0)
    val Fish = Color(0xFFE8A04A)
    val Drop = Color(0xFFCFE8F6)
    val Light = Color(0xFFFFF0C8)
    val Dark = Color(0xFF3A3040)
    val Syringe = Color(0xFFDCE8F0)
    val Heart = Color(0xFFE8705A)
}

/**
 * Beats: trialworld (a walker on a road of hurdles: a world built for testing), fishdry (in a pond a fish swims, drops on
 * its back, and nothing: it was made for water), freechoice (the road forks, one way lit and one dark; he stands free
 * to choose), syringe (a syringe and a heart beside it: the loving father allows the painful shot), threadlong (a short
 * bright piece of thread, and the thread running on past the edge of the picture).
 */
internal fun DrawScope.trialroadScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "trialworld" to Shot(Offset(w * 0.35f, h * 0.62f), 1.15f),
        "fishdry" to Shot(Offset(w * 0.75f, h * 0.75f), 1.4f),
        "freechoice" to Shot(Offset(w * 0.55f, h * 0.55f), 1.15f),
        "syringe" to Shot(Offset(w * 0.25f, h * 0.3f), 1.4f),
        "threadlong" to Shot(Offset(w * 0.5f, h * 0.2f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { trialRoad(a) }
}

private fun DrawScope.trialRoad(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tw = a.b("trialworld")
    val fd = a.b("fishdry")
    val fc = a.b("freechoice")
    val sy = a.b("syringe")
    val tl = a.b("threadlong")
    val ground = h * 0.6f
    drawRect(Brush.verticalGradient(listOf(TrialC39.Sky, TrialC39.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(TrialC39.Ground, Offset(0f, ground), Size(w, h - ground))
    // the road of hurdles
    val road = easeOut(seg(tw, 0f, 0.3f))
    for (k in 0 until 4) {
        val x = w * (0.18f + 0.13f * k)
        val y = h * 0.8f - h * 0.03f * k
        val g = road * seg(tw, 0.1f + 0.1f * k, 0.3f + 0.1f * k)
        drawLine(TrialC39.Hurdle, Offset(x - w * 0.03f, y), Offset(x - w * 0.03f, y - h * 0.05f), 2f * dp, alpha = g)
        drawLine(TrialC39.Hurdle, Offset(x + w * 0.03f, y), Offset(x + w * 0.03f, y - h * 0.05f), 2f * dp, alpha = g)
        drawLine(TrialC39.Hurdle, Offset(x - w * 0.035f, y - h * 0.05f), Offset(x + w * 0.035f, y - h * 0.05f), 4f * dp, StrokeCap.Round, alpha = g)
    }
    val go = easeInOut(seg(tw, 0.2f, 1f)) * 0.42f + easeInOut(seg(fc, 0f, 0.4f)) * 0.08f
    val foot = Offset(w * (0.1f + go), h * (0.83f - 0.08f * go))
    val hop = bump(fract(go * 7.7f)) * h * 0.04f * (if (tw in 0.2f..0.99f) 1f else 0f)
    figure(foot + Offset(0f, -hop), h * 0.18f, TrialC39.Walker, road, walk = if (tw in 0.2f..0.99f) t * 7f else 0f)
    // the fish that never feels wet
    if (fd > 0f) {
        val f = easeOut(seg(fd, 0f, 0.3f))
        val pond = Offset(w * 0.76f, h * 0.78f)
        drawOval(TrialC39.Water, Offset(pond.x - w * 0.18f, pond.y - h * 0.07f), Size(w * 0.36f, h * 0.14f), alpha = f)
        val fx = pond.x + w * 0.08f * sin(t * 0.8f)
        val fish = Offset(fx, pond.y)
        drawOval(TrialC39.Fish, Offset(fish.x - w * 0.04f, fish.y - h * 0.018f), Size(w * 0.08f, h * 0.036f), alpha = f)
        drawPath(Path().apply { moveTo(fish.x + w * 0.035f, fish.y); lineTo(fish.x + w * 0.06f, fish.y - h * 0.02f); lineTo(fish.x + w * 0.06f, fish.y + h * 0.02f); close() }, TrialC39.Fish, alpha = f)
        for (k in 0 until 4) {
            val u = fract(t * 0.6f + k * 0.25f)
            drawCircle(TrialC39.Drop, w * 0.004f, fish + Offset(w * (-0.03f + 0.02f * k), -h * 0.12f * (1f - u)), alpha = f * seg(fd, 0.3f, 0.6f) * (1f - u))
        }
    }
    // the fork: free to choose
    if (fc > 0f) {
        val f = easeOut(seg(fc, 0.1f, 0.5f))
        val fork = Offset(w * 0.6f, h * 0.68f)
        drawLine(TrialC39.Light, fork, Offset(w * 0.86f, h * 0.48f), w * 0.03f, StrokeCap.Round, alpha = f * 0.9f)
        drawLine(TrialC39.Dark, fork, Offset(w * 0.86f, h * 0.66f), w * 0.03f, StrokeCap.Round, alpha = f * 0.9f)
        softGlow(Offset(w * 0.86f, h * 0.48f), w * 0.06f, ArtInk.Gold, 0.5f * f)
    }
    // the syringe and the father's love
    if (sy > 0f) {
        val f = easeOut(seg(sy, 0f, 0.35f)) * (1f - 0.6f * seg(tl, 0f, 0.4f))
        val c = Offset(w * 0.2f, h * 0.3f)
        drawRoundRect(TrialC39.Syringe, Offset(c.x - w * 0.08f, c.y - h * 0.02f), Size(w * 0.13f, h * 0.04f), CornerRadius(4f * dp), alpha = f)
        drawRect(Color(0xFF8AC0E0), Offset(c.x - w * 0.07f, c.y - h * 0.012f), Size(w * 0.08f * (1f - seg(sy, 0.4f, 0.8f)), h * 0.024f), alpha = f)
        drawLine(Color(0xFF9AA4B0), c + Offset(w * 0.05f, 0f), c + Offset(w * 0.1f, 0f), 1.5f * dp, alpha = f)
        drawLine(Color(0xFF6A7480), c + Offset(-w * 0.08f, 0f), c + Offset(-w * 0.11f, 0f), 4f * dp, alpha = f)
        val heart = c + Offset(w * 0.17f, -h * 0.04f)
        drawPath(w8Heart(heart, w * 0.03f * easeOut(seg(sy, 0.3f, 0.6f))), TrialC39.Heart, alpha = f)
        softGlow(heart, w * 0.05f, TrialC39.Heart, 0.4f * f * seg(sy, 0.4f, 0.8f))
    }
    // a short life, the start of an endless thread
    if (tl > 0f) {
        val f = easeOut(seg(tl, 0f, 0.3f))
        val y = h * 0.16f
        drawLine(ArtInk.Gold, Offset(w * 0.08f, y), Offset(w * 0.2f, y), 5f * dp, StrokeCap.Round, alpha = f)
        softGlow(Offset(w * 0.14f, y), w * 0.05f, ArtInk.Gold, 0.5f * f)
        val run = easeInOut(seg(tl, 0.2f, 1f))
        drawLine(ArtInk.BrassLight, Offset(w * 0.2f, y), Offset(w * (0.2f + 0.85f * run), y), 2f * dp, alpha = f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f * dp, 6f * dp), -t * 30f))
    }
}

// ═════════════ 2. three thermometers summed into one (bs-harara) ═════════════

private object ThermoC39 {
    val Sky = Color(0xFF1C2238)
    val SkyLow = Color(0xFF3A3450)
    val Glass = Color(0xFFDCE6EE)
    val Red = Color(0xFFE0584A)
    val Screen = Color(0xFF2A3A50)
    val Cloud = Color(0xFF7A7A88)
    val Walker = Color(0xFFE8E0D0)
    val House = Color(0xFFD8C098)
    val Gift = Color(0xFFF2C45A)
}

/** A thermometer standing on [foot], [tall] high, filled to [level] (0..1). */
private fun DrawScope.thermo39(foot: Offset, tall: Float, level: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val wd = tall * 0.12f
    drawRoundRect(ThermoC39.Glass, Offset(foot.x - wd / 2f, foot.y - tall), Size(wd, tall), CornerRadius(wd / 2f, wd / 2f), alpha = alpha)
    drawCircle(ThermoC39.Red, wd * 0.9f, foot, alpha = alpha)
    val top = foot.y - tall * 0.92f * level.coerceIn(0f, 1.3f)
    drawRect(ThermoC39.Red, Offset(foot.x - wd * 0.22f, top), Size(wd * 0.44f, foot.y - top), alpha = alpha)
}

/**
 * Beats: threethermo (three thermometers, three countries, each at a bearable heat), sumthermo (their red columns pour
 * into one giant thermometer that bursts past its top: «how can a man bear this?»), mediapile (a screen pulls the
 * clouds of every country onto one small figure), dividedblessings (the clouds go back, one to each house, and every
 * house is ringed with countless small gifts), needdua (a row of three doors opening one after another: «الْحَاجَةُ»,
 * «الدُّعَاءُ», «الْإِيمَانُ»).
 */
internal fun DrawScope.thermoScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threethermo" to Shot(Offset(w * 0.3f, h * 0.55f), 1.25f),
        "sumthermo" to Shot(Offset(w * 0.55f, h * 0.45f), 1.1f),
        "mediapile" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "dividedblessings" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "needdua" to Shot(Offset(w * 0.5f, h * 0.4f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { thermoNight(a) }
}

private fun DrawScope.thermoNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val th = a.b("threethermo")
    val su = a.b("sumthermo")
    val mp = a.b("mediapile")
    val db = a.b("dividedblessings")
    val nd = a.b("needdua")
    val ground = h * 0.84f
    nightSky(t, ThermoC39.Sky, ThermoC39.SkyLow, ground, starAlpha = 0.5f)
    drawRect(Color(0xFF2A2630), Offset(0f, ground), Size(w, h - ground))
    val phase1 = 1f - easeOut(seg(mp, 0f, 0.3f))
    // three thermometers
    val pour = easeInOut(seg(su, 0.1f, 0.6f))
    for (k in 0 until 3) {
        val foot = Offset(w * (0.14f + 0.14f * k), ground - h * 0.04f)
        val f = easeOut(seg(th, 0.05f + 0.15f * k, 0.35f + 0.15f * k)) * phase1
        thermo39(foot, h * 0.3f, (0.45f + 0.08f * k) * (1f - pour), f)
    }
    // summed into one that bursts
    if (su > 0f) {
        val f = easeOut(seg(su, 0f, 0.3f)) * phase1
        val foot = Offset(w * 0.68f, ground - h * 0.04f)
        val lvl = 1.3f * easeOut(seg(su, 0.3f, 0.85f))
        thermo39(foot, h * 0.5f, lvl, f)
        if (lvl > 1f) for (k in 0 until 6) {
            val an = -PI.toFloat() / 2f + (k - 2.5f) * 0.35f
            val r = h * 0.05f * seg(su, 0.75f, 1f)
            drawCircle(ThermoC39.Red, w * 0.007f, Offset(foot.x, foot.y - h * 0.5f) + Offset(cos(an) * r, sin(an) * r), alpha = f)
        }
        qmark21(Offset(foot.x + w * 0.12f, foot.y - h * 0.42f), h * 0.07f, ArtInk.Ivory, seg(su, 0.8f, 1f) * phase1)
    }
    // the screen piles every cloud on one man
    val houses = List(5) { Offset(w * (0.12f + 0.19f * it), ground) }
    if (mp > 0f) {
        val f = easeOut(seg(mp, 0f, 0.3f)) * (1f - 0.8f * seg(db, 0.3f, 0.6f))
        val scr = Offset(w * 0.5f, h * 0.22f)
        drawRoundRect(ThermoC39.Screen, Offset(scr.x - w * 0.16f, scr.y - h * 0.09f), Size(w * 0.32f, h * 0.18f), CornerRadius(8f * dp), alpha = f)
        drawRoundRect(Color(0xFF8AA0C0), Offset(scr.x - w * 0.16f, scr.y - h * 0.09f), Size(w * 0.32f, h * 0.18f), CornerRadius(8f * dp), alpha = f, style = Stroke(2f * dp))
        val man = Offset(w * 0.5f, ground)
        figure(man, h * 0.16f, ThermoC39.Walker, f * (1f - seg(db, 0f, 0.3f)), lean = 0.2f * seg(mp, 0.4f, 0.9f))
        for (k in 0 until 5) {
            val gather = easeInOut(seg(mp, 0.1f + 0.08f * k, 0.6f + 0.08f * k))
            val back = easeInOut(seg(db, 0.05f + 0.06f * k, 0.5f + 0.06f * k))
            val from = houses[k] + Offset(0f, -h * 0.3f)
            val pile = man + Offset(w * 0.02f * (k - 2), -h * (0.2f + 0.035f * k))
            val home = houses[k] + Offset(0f, -h * 0.17f)
            val p = if (back > 0f) pile + (home - pile) * back else from + (pile - from) * gather
            cloud38(p, w * 0.045f, ThermoC39.Cloud, easeOut(seg(mp, 0.1f, 0.3f)) * (1f - 0.5f * seg(nd, 0f, 0.4f)))
        }
    }
    // shared out: one cloud to each house, and countless gifts round every one
    if (db > 0f) {
        val f = easeOut(seg(db, 0.2f, 0.5f)) * (1f - 0.6f * seg(nd, 0f, 0.4f))
        for ((k, c) in houses.withIndex()) {
            drawRect(ThermoC39.House, Offset(c.x - w * 0.04f, c.y - h * 0.08f), Size(w * 0.08f, h * 0.08f), alpha = f)
            drawPath(Path().apply { moveTo(c.x - w * 0.05f, c.y - h * 0.08f); lineTo(c.x, c.y - h * 0.12f); lineTo(c.x + w * 0.05f, c.y - h * 0.08f); close() }, Color(0xFF8A5A3A), alpha = f)
            for (j in 0 until 14) {
                val an = j * 0.45f + k
                val r = w * (0.06f + 0.012f * (j % 3))
                val g = seg(db, 0.4f + 0.02f * j, 0.6f + 0.02f * j)
                drawCircle(ThermoC39.Gift, w * 0.0045f, c + Offset(cos(an) * r, -h * 0.05f + sin(an) * r * 0.7f), alpha = f * g * (0.7f + 0.3f * sin(t * 3f + j)))
            }
        }
    }
    // need opens the door to supplication, and supplication the doors of faith
    if (nd > 0f) {
        for (k in 0 until 3) {
            val c = Offset(w * (0.26f + 0.24f * k), h * 0.36f)
            val f = easeOut(seg(nd, 0.05f + 0.2f * k, 0.3f + 0.2f * k))
            val open = easeInOut(seg(nd, 0.2f + 0.2f * k, 0.45f + 0.2f * k))
            softGlow(c, w * 0.07f * open, ArtInk.Gold, 0.5f * open * f)
            drawRect(Color(0xFFFFF0C8), Offset(c.x - w * 0.05f, c.y - h * 0.1f), Size(w * 0.1f, h * 0.2f), alpha = f * open)
            drawRect(Color(0xFF6A4A3A), Offset(c.x - w * 0.05f, c.y - h * 0.1f), Size(w * 0.1f * (1f - 0.8f * open), h * 0.2f), alpha = f)
            drawRect(ArtInk.BrassDark, Offset(c.x - w * 0.05f, c.y - h * 0.1f), Size(w * 0.1f, h * 0.2f), alpha = f, style = Stroke(2f * dp))
            tagAt(a.tag("needdua", 0, k), c + Offset(0f, h * 0.15f), seg(nd, 0.15f + 0.2f * k, 0.4f + 0.2f * k), back = Back39, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 3. four gates to knowing Him (bs-maarifa) ═════════════

private object GatesC39 {
    val Sky = Color(0xFF243048)
    val SkyLow = Color(0xFFC89A7A)
    val Ground = Color(0xFF6A5444)
    val Gate = Color(0xFF8A7A6A)
    val Trench = Color(0xFF3A2E28)
    val Walker = Color(0xFFE8DCC8)
    val Womb = Color(0xFFD89A8A)
    val Lung = Color(0xFFF0B8A8)
    val Spark = Color(0xFFF0A040)
}

/**
 * Beats: fourways (four arches on a hill road, each named: «الْفِطْرَةُ السَّلِيمَةُ», «الْعَقْلُ», «النَّظَرُ فِي
 * مَخْلُوقَاتِهِ», «الرُّسُلُ»), foxhole (below, a man crouched in a trench under falling sparks looks up), greatestway (the
 * fourth arch fills with light, the widest road of all), readybefore (a womb, and in it two small lungs and two eyes
 * already made, glowing before they are needed).
 */
internal fun DrawScope.fourgatesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fourways" to Shot(Offset(w * 0.5f, h * 0.42f), 1.05f),
        "foxhole" to Shot(Offset(w * 0.25f, h * 0.78f), 1.4f),
        "greatestway" to Shot(Offset(w * 0.78f, h * 0.4f), 1.3f),
        "readybefore" to Shot(Offset(w * 0.68f, h * 0.78f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { fourGates(a) }
}

private fun DrawScope.fourGates(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fw = a.b("fourways")
    val fx = a.b("foxhole")
    val gw = a.b("greatestway")
    val rb = a.b("readybefore")
    val ground = h * 0.62f
    drawRect(Brush.verticalGradient(listOf(GatesC39.Sky, GatesC39.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(GatesC39.Ground, Offset(0f, ground), Size(w, h - ground))
    // four arches
    for (k in 0 until 4) {
        val x = w * (0.16f + 0.22f * k)
        val top = h * (0.3f - 0.03f * k)
        val f = easeOut(seg(fw, 0.05f + 0.15f * k, 0.35f + 0.15f * k))
        val great = if (k == 3) easeOut(seg(gw, 0f, 0.6f)) else 0f
        val hw = w * (0.06f + 0.03f * great)
        if (great > 0f) {
            softGlow(Offset(x, (top + ground) / 2f), w * 0.16f * great, ArtInk.Gold, 0.55f * great)
            lightShafts(Offset(x, top), PI.toFloat() / 2f, 1f, h * 0.5f, Color(0xFFFFF0C8), 0.45f * great, t, rays = 7)
        }
        drawPath(archPath26(x, top, ground, hw), lerpColor(Color(0xFF2A2A3A), Color(0xFFFFF0C8), great), alpha = f * 0.85f)
        drawPath(archPath26(x, top, ground, hw), GatesC39.Gate, alpha = f, style = Stroke(4f * dp))
        val dimOthers = if (k < 3) 1f - 0.5f * seg(gw, 0f, 0.5f) else 1f
        tagAt(a.tag("fourways", 0, k), Offset(x, top - h * 0.06f - h * 0.04f * (k % 2)), seg(fw, 0.2f + 0.15f * k, 0.45f + 0.15f * k) * dimOthers, back = Back39, ink = if (k == 3) ArtInk.Gold else ArtInk.Ivory)
    }
    // no atheists in the trenches
    if (fx > 0f) {
        val f = easeOut(seg(fx, 0f, 0.3f)) * (1f - 0.7f * seg(rb, 0f, 0.4f))
        val tr = Offset(w * 0.25f, h * 0.84f)
        drawRect(GatesC39.Trench, Offset(tr.x - w * 0.16f, tr.y - h * 0.06f), Size(w * 0.32f, h * 0.12f), alpha = f)
        figure(tr + Offset(0f, h * 0.04f), h * 0.12f, GatesC39.Walker, f, lean = -0.35f * seg(fx, 0.3f, 0.7f), arms = 0.8f * seg(fx, 0.4f, 0.8f))
        for (k in 0 until 6) {
            val u = fract(t * 0.7f + k / 6f)
            drawCircle(GatesC39.Spark, w * 0.005f, Offset(w * (0.12f + 0.05f * k), h * (0.66f + 0.14f * u)), alpha = f * (1f - u))
        }
        val look = seg(fx, 0.5f, 0.9f)
        lightShafts(Offset(w * 0.25f, h * 0.64f), PI.toFloat() / 2f, 0.4f, h * 0.18f, Color(0xFFFFF0C8), 0.4f * look * f, t, rays = 3)
    }
    // made in the womb before they are needed
    if (rb > 0f) {
        val f = easeOut(seg(rb, 0f, 0.35f))
        val c = Offset(w * 0.68f, h * 0.8f)
        drawOval(GatesC39.Womb, Offset(c.x - w * 0.13f, c.y - h * 0.09f), Size(w * 0.26f, h * 0.18f), alpha = f * 0.5f)
        drawOval(GatesC39.Womb, Offset(c.x - w * 0.13f, c.y - h * 0.09f), Size(w * 0.26f, h * 0.18f), alpha = f, style = Stroke(2f * dp))
        val grow = easeOut(seg(rb, 0.25f, 0.75f))
        for (s in listOf(-1f, 1f)) {
            val lung = c + Offset(s * w * 0.035f, h * 0.02f)
            drawOval(GatesC39.Lung, Offset(lung.x - w * 0.025f * grow, lung.y - h * 0.035f * grow), Size(w * 0.05f * grow, h * 0.07f * grow), alpha = f)
            val eye = c + Offset(s * w * 0.03f, -h * 0.05f)
            drawCircle(Color.White, w * 0.01f * grow, eye, alpha = f)
            drawCircle(Color(0xFF3A2A20), w * 0.005f * grow, eye, alpha = f)
        }
        softGlow(c, w * 0.1f, ArtInk.Gold, 0.35f * f * seg(rb, 0.6f, 1f))
    }
}

// ═════════════ 4. a sealed exam and a train (bs-taklif) ═════════════

private object ExamC39 {
    val Sky = Color(0xFF8AA8C8)
    val SkyLow = Color(0xFFE8D8B8)
    val Ground = Color(0xFF9A8466)
    val Figure = Color(0xFF2E2620)
    val Chest = Color(0xFF8A5A2A)
    val Desk = Color(0xFF6A4A32)
    val Paper = Color(0xFFFBF7EC)
    val Train = Color(0xFF5A6A80)
    val Window = Color(0xFFFFE8B0)
    val Post = Color(0xFF7A5A3A)
}

/**
 * Beats: amanah (a glowing chest settles into a man's arms: «الْأَمَانَةُ»), nomodel (an exam on a desk; beside it a model
 * answer appears, and is struck out: with it there would be no test), trainride (a train crosses the plain, one window
 * lit), threequestions (three signposts at a crossroads: «مِنْ أَيْنَ جِئْتُ», «لِمَاذَا أَنَا هُنَا», «إِلَى أَيْنَ أَذْهَبُ»).
 */
internal fun DrawScope.examtrainScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "amanah" to Shot(Offset(w * 0.22f, h * 0.55f), 1.35f),
        "nomodel" to Shot(Offset(w * 0.6f, h * 0.55f), 1.3f),
        "trainride" to Shot(Offset(w * 0.5f, h * 0.38f), 1.1f),
        "threequestions" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { examPlain(a) }
}

private fun DrawScope.examPlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val am = a.b("amanah")
    val nm = a.b("nomodel")
    val tr = a.b("trainride")
    val tq = a.b("threequestions")
    val ground = h * 0.5f
    drawRect(Brush.verticalGradient(listOf(ExamC39.Sky, ExamC39.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(ExamC39.Ground, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - 0.7f * seg(tq, 0f, 0.4f)
    // the trust
    if (am > 0f) {
        val f = easeOut(seg(am, 0f, 0.3f)) * away
        val foot = Offset(w * 0.22f, h * 0.8f)
        figure(foot, h * 0.24f, ExamC39.Figure, f, arms = 0.7f * seg(am, 0.3f, 0.6f))
        val drop = easeInOut(seg(am, 0.1f, 0.6f))
        val c = Offset(w * 0.17f, h * 0.2f + (h * 0.42f) * drop)
        softGlow(c, w * 0.06f, ArtInk.Gold, 0.6f * f)
        drawRect(ExamC39.Chest, Offset(c.x - w * 0.035f, c.y - h * 0.025f), Size(w * 0.07f, h * 0.05f), alpha = f)
        drawRect(ArtInk.Gold, Offset(c.x - w * 0.035f, c.y - h * 0.008f), Size(w * 0.07f, h * 0.006f), alpha = f)
        tagAt(a.tag("amanah"), c + Offset(0f, -h * 0.08f), seg(am, 0.5f, 0.8f) * away, back = Back39, ink = ArtInk.Gold)
    }
    // the exam, and no model answer
    if (nm > 0f) {
        val f = easeOut(seg(nm, 0f, 0.3f)) * away
        val d = Offset(w * 0.6f, h * 0.72f)
        drawRect(ExamC39.Desk, Offset(d.x - w * 0.16f, d.y), Size(w * 0.32f, h * 0.03f), alpha = f)
        drawLine(ExamC39.Desk, Offset(d.x - w * 0.14f, d.y), Offset(d.x - w * 0.14f, d.y + h * 0.12f), 3f * dp, alpha = f)
        drawLine(ExamC39.Desk, Offset(d.x + w * 0.14f, d.y), Offset(d.x + w * 0.14f, d.y + h * 0.12f), 3f * dp, alpha = f)
        drawRect(ExamC39.Paper, Offset(d.x - w * 0.12f, d.y - h * 0.11f), Size(w * 0.1f, h * 0.11f), alpha = f)
        for (r in 0 until 4) drawLine(Color(0xFF9A9080), Offset(d.x - w * 0.11f, d.y - h * 0.09f + r * h * 0.022f), Offset(d.x - w * 0.03f, d.y - h * 0.09f + r * h * 0.022f), 1.2f * dp, alpha = f)
        val model = easeOut(seg(nm, 0.3f, 0.55f))
        val mc = Offset(d.x + w * 0.07f, d.y - h * 0.055f)
        drawRect(Color(0xFFE8F4E8), Offset(mc.x - w * 0.05f, mc.y - h * 0.055f), Size(w * 0.1f, h * 0.11f), alpha = f * model)
        for (r in 0 until 4) check21(Offset(mc.x, mc.y - h * 0.035f + r * h * 0.022f), w * 0.008f, Color(0xFF2E9A55), model, 1.5f * dp)
        cross21(mc, w * 0.05f, Color(0xFFE0584A), seg(nm, 0.65f, 0.9f) * f, 4f * dp)
    }
    // the train of life
    if (tr > 0f) {
        val f = easeOut(seg(tr, 0f, 0.3f)) * away
        val y = h * 0.44f
        drawLine(Color(0xFF5A4A3A), Offset(0f, y + h * 0.04f), Offset(w, y + h * 0.04f), 2f * dp, alpha = f)
        val x0 = -w * 0.6f + w * 1.1f * easeInOut(seg(tr, 0f, 0.9f))
        for (k in 0 until 4) {
            val cx = x0 + k * w * 0.15f
            drawRoundRect(ExamC39.Train, Offset(cx, y - h * 0.06f), Size(w * 0.14f, h * 0.09f), CornerRadius(6f * dp), alpha = f)
            for (j in 0 until 3) {
                val lit = k == 2 && j == 1
                drawRect(if (lit) ExamC39.Window else Color(0xFF3A4A5A), Offset(cx + w * (0.015f + 0.04f * j), y - h * 0.045f), Size(w * 0.03f, h * 0.03f), alpha = f)
                if (lit) softGlow(Offset(cx + w * 0.07f, y - h * 0.03f), w * 0.04f, ArtInk.Gold, 0.5f * f)
            }
            drawCircle(Color(0xFF2A2A30), w * 0.012f, Offset(cx + w * 0.03f, y + h * 0.035f), alpha = f)
            drawCircle(Color(0xFF2A2A30), w * 0.012f, Offset(cx + w * 0.11f, y + h * 0.035f), alpha = f)
        }
    }
    // three signposts
    if (tq > 0f) {
        val c = Offset(w * 0.5f, h * 0.9f)
        val f = easeOut(seg(tq, 0f, 0.3f))
        drawLine(ExamC39.Post, c, c + Offset(0f, -h * 0.4f), 5f * dp, alpha = f)
        for (k in 0 until 3) {
            val y = c.y - h * (0.33f - 0.1f * k)
            val dir = if (k % 2 == 0) -1f else 1f
            val g = easeOut(seg(tq, 0.15f + 0.2f * k, 0.4f + 0.2f * k))
            drawPath(Path().apply {
                moveTo(c.x, y - h * 0.025f); lineTo(c.x + dir * w * 0.22f, y - h * 0.025f); lineTo(c.x + dir * w * 0.26f, y)
                lineTo(c.x + dir * w * 0.22f, y + h * 0.025f); lineTo(c.x, y + h * 0.025f); close()
            }, ExamC39.Post, alpha = g)
            tagAt(a.tag("threequestions", 0, k), Offset(c.x + dir * w * 0.12f, y), g, back = Back39, ink = ArtInk.Ivory)
        }
    }
}

// ═════════════ 5. a swimmer who walks away (bs-ghina) ═════════════

private object SeaC39 {
    val Sky = Color(0xFF9AC8E8)
    val SkyLow = Color(0xFFF2E6CC)
    val Sea = Color(0xFF3A7AA8)
    val SeaLow = Color(0xFF2A5A80)
    val Sand = Color(0xFFE8D2A0)
    val Figure = Color(0xFF2E2620)
    val Child = Color(0xFF4A3A30)
    val Bag = Color(0xFF8A3A3A)
    val Coin = Color(0xFFD8B04A)
    val Tree = Color(0xFF6A5A3A)
    val Leaf = Color(0xFF6A9A5A)
    val Fruit = Color(0xFFE8A04A)
}

/**
 * Beats: doctorfree (a doctor's bag, and a coin struck out: he treats for nothing), rescue (out at sea a small one
 * struggles; a swimmer brings him to the sand and walks off without waiting for thanks), forus (a worshipper bows, and
 * the light of his prayer comes back into him), fruitprayer (a tree drops its fruit into his hands: the fruit of prayer
 * is his).
 */
internal fun DrawScope.lifeguardScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "doctorfree" to Shot(Offset(w * 0.2f, h * 0.35f), 1.35f),
        "rescue" to Shot(Offset(w * 0.6f, h * 0.62f), 1.15f),
        "forus" to Shot(Offset(w * 0.3f, h * 0.7f), 1.3f),
        "fruitprayer" to Shot(Offset(w * 0.32f, h * 0.55f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { seaShore(a) }
}

private fun DrawScope.seaShore(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val dfr = a.b("doctorfree")
    val rs = a.b("rescue")
    val fu = a.b("forus")
    val fp = a.b("fruitprayer")
    val horizon = h * 0.46f
    drawRect(Brush.verticalGradient(listOf(SeaC39.Sky, SeaC39.SkyLow), endY = horizon), size = Size(w, horizon))
    drawRect(Brush.verticalGradient(listOf(SeaC39.Sea, SeaC39.SeaLow), startY = horizon, endY = h), Offset(w * 0.45f, horizon), Size(w * 0.55f, h - horizon))
    drawPath(Path().apply { moveTo(0f, horizon); lineTo(w * 0.5f, horizon); quadraticTo(w * 0.42f, h * 0.7f, w * 0.56f, h); lineTo(0f, h); close() }, SeaC39.Sand)
    // the doctor who asks no fee
    if (dfr > 0f) {
        val f = easeOut(seg(dfr, 0f, 0.3f)) * (1f - 0.7f * seg(rs, 0f, 0.4f))
        val c = Offset(w * 0.2f, h * 0.36f)
        drawRoundRect(SeaC39.Bag, Offset(c.x - w * 0.07f, c.y - h * 0.04f), Size(w * 0.14f, h * 0.09f), CornerRadius(6f * dp), alpha = f)
        drawArc(SeaC39.Bag, 180f, 180f, false, Offset(c.x - w * 0.03f, c.y - h * 0.07f), Size(w * 0.06f, h * 0.06f), alpha = f, style = Stroke(3f * dp))
        drawRect(Color.White, Offset(c.x - w * 0.006f, c.y - h * 0.025f), Size(w * 0.012f, h * 0.05f), alpha = f)
        drawRect(Color.White, Offset(c.x - w * 0.02f, c.y - h * 0.006f), Size(w * 0.04f, h * 0.012f), alpha = f)
        val coin = c + Offset(w * 0.13f, -h * 0.02f)
        drawCircle(SeaC39.Coin, w * 0.025f, coin, alpha = f * seg(dfr, 0.3f, 0.5f))
        cross21(coin, w * 0.03f, Color(0xFFE0584A), seg(dfr, 0.5f, 0.8f) * f, 3f * dp)
    }
    // the rescue at sea
    if (rs > 0f) {
        val f = easeOut(seg(rs, 0f, 0.2f)) * (1f - 0.6f * seg(fu, 0f, 0.4f))
        val far = Offset(w * 0.82f, h * 0.62f)
        val shore = Offset(w * 0.46f, h * 0.7f)
        val bring = easeInOut(seg(rs, 0.25f, 0.65f))
        val child = far + (shore - far) * bring
        val bob = h * 0.008f * sin(t * 5f) * (1f - bring)
        drawCircle(SeaC39.Child, w * 0.016f, child + Offset(0f, -h * 0.02f + bob), alpha = f)
        if (bring < 0.05f) for (k in 0 until 2) drawLine(SeaC39.Child, child + Offset(0f, -h * 0.01f), child + Offset(w * 0.025f * (if (k == 0) -1f else 1f), -h * 0.05f + h * 0.01f * sin(t * 8f + k)), 2f * dp, alpha = f)
        val swim = far + Offset(-w * 0.12f, 0f) + (child + Offset(w * 0.04f, 0f) - far - Offset(-w * 0.12f, 0f)) * seg(rs, 0.1f, 0.3f)
        val walkAway = easeInOut(seg(rs, 0.7f, 1f))
        if (bring < 1f) drawCircle(SeaC39.Figure, w * 0.018f, swim + Offset(0f, -h * 0.02f), alpha = f)
        else figure(Offset(shore.x - w * 0.05f - w * 0.25f * walkAway, shore.y + h * 0.02f), h * 0.16f, SeaC39.Figure, f, walk = if (walkAway in 0.01f..0.99f) t * 6f else 0f)
        if (bring >= 1f) figure(shore + Offset(w * 0.02f, h * 0.02f), h * 0.09f, SeaC39.Child, f)
    }
    // worship is for us: the light comes back to the worshipper
    if (fu > 0f) {
        val f = easeOut(seg(fu, 0f, 0.3f))
        val foot = Offset(w * 0.3f, h * 0.8f)
        w8Worshipper(foot, h * 0.18f, SeaC39.Figure, 0f, easeInOut(seg(fu, 0.2f, 0.6f)), f)
        for (k in 0 until 6) {
            val u = fract(t * 0.3f + k / 6f)
            val an = k * 1.05f
            val p = foot + Offset(cos(an) * w * 0.15f * (1f - u), -h * 0.1f + sin(an) * h * 0.1f * (1f - u))
            drawCircle(ArtInk.Gold, w * 0.005f, p, alpha = f * u)
        }
        softGlow(foot + Offset(0f, -h * 0.08f), w * 0.08f, ArtInk.Gold, 0.4f * f * seg(fu, 0.5f, 1f))
    }
    // the fruit of prayer falls to him
    if (fp > 0f) {
        val f = easeOut(seg(fp, 0f, 0.3f))
        val trunk = Offset(w * 0.14f, h * 0.8f)
        drawLine(SeaC39.Tree, trunk, trunk + Offset(0f, -h * 0.32f), w * 0.025f, alpha = f)
        for (k in 0 until 5) drawCircle(SeaC39.Leaf, w * 0.06f, trunk + Offset(w * 0.06f * cos(k * 1.3f), -h * 0.36f + h * 0.04f * sin(k * 1.7f)), alpha = f * 0.9f)
        for (k in 0 until 3) {
            val fall = easeIn39(seg(fp, 0.3f + 0.15f * k, 0.55f + 0.15f * k))
            val from = trunk + Offset(w * (0.04f + 0.03f * k), -h * 0.32f)
            val to = Offset(w * 0.27f + w * 0.015f * k, h * 0.66f)
            drawCircle(SeaC39.Fruit, w * 0.013f, from + (to - from) * fall, alpha = f)
        }
    }
}

private fun easeIn39(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ═════════════ 6. the workbench of «how» under the sky of «why» (bs-ilm-kayfa) ═════════════

private object HowC39 {
    val Sky = Color(0xFF101628)
    val SkyLow = Color(0xFF242A44)
    val Bench = Color(0xFF6A4A32)
    val Orbit = Color(0xFF6A8AC0)
    val Planet = Color(0xFFE8C88A)
    val Icon = Color(0xFFDCE6EE)
    val Glass = Color(0xFFCFE4EE)
    val Liquid = Color(0xFF7AC8A0)
}

/**
 * Beats: orderlyworld (orbits drawn neat and steady round a bright centre: a world of laws, expected and found),
 * howtools (on the bench three tools of «how»: a bowl, a wheel, a flask — «كَيْفَ»), whysilent (the tools go dim and a
 * great question mark rises into the dark sky), onlygod (light comes down from above into the scene), jabirflask (an
 * open book with an alembic beside it: «جَابِرُ بْنُ حَيَّانَ»).
 */
internal fun DrawScope.howwhyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "orderlyworld" to Shot(Offset(w * 0.5f, h * 0.3f), 1.2f),
        "howtools" to Shot(Offset(w * 0.5f, h * 0.75f), 1.3f),
        "whysilent" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "onlygod" to Shot(Offset(w * 0.5f, h * 0.4f), 1.05f),
        "jabirflask" to Shot(Offset(w * 0.25f, h * 0.68f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { howBench(a) }
}

private fun DrawScope.howBench(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ow = a.b("orderlyworld")
    val ht = a.b("howtools")
    val ws = a.b("whysilent")
    val og = a.b("onlygod")
    val jf = a.b("jabirflask")
    val bench = h * 0.82f
    nightSky(t, HowC39.Sky, HowC39.SkyLow, bench, starAlpha = 0.7f)
    drawRect(HowC39.Bench, Offset(0f, bench), Size(w, h - bench))
    // a world of laws
    if (ow > 0f) {
        val f = easeOut(seg(ow, 0f, 0.4f)) * (1f - 0.6f * seg(ws, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.3f)
        drawCircle(HowC39.Planet, w * 0.025f, c, alpha = f)
        softGlow(c, w * 0.06f, HowC39.Planet, 0.5f * f)
        for (k in 0 until 3) {
            val rx = w * (0.1f + 0.07f * k)
            val ry = h * (0.05f + 0.035f * k)
            val draw = seg(ow, 0.1f + 0.15f * k, 0.5f + 0.15f * k)
            drawArc(HowC39.Orbit, 0f, 360f * draw, false, Offset(c.x - rx, c.y - ry), Size(rx * 2f, ry * 2f), alpha = f * 0.8f, style = Stroke(1.5f * dp))
            val an = t * (0.8f - 0.2f * k) + k
            if (draw >= 1f) drawCircle(HowC39.Icon, w * (0.008f + 0.003f * k), c + Offset(cos(an) * rx, sin(an) * ry), alpha = f)
        }
    }
    // the tools of how
    val dimTools = 1f - 0.6f * seg(ws, 0.2f, 0.6f)
    if (ht > 0f) {
        val f = easeOut(seg(ht, 0f, 0.3f)) * dimTools * (1f - 0.5f * seg(jf, 0f, 0.4f))
        val y = bench - h * 0.05f
        // a bowl
        drawArc(HowC39.Icon, 0f, 180f, true, Offset(w * 0.32f, y - h * 0.04f), Size(w * 0.1f, h * 0.08f), alpha = f * seg(ht, 0.1f, 0.3f))
        // a wheel
        val wc = Offset(w * 0.52f, y - h * 0.01f)
        drawCircle(HowC39.Icon, w * 0.04f, wc, alpha = f * seg(ht, 0.25f, 0.45f), style = Stroke(3f * dp))
        for (k in 0 until 4) {
            val an = t + k * PI.toFloat() / 4f
            drawLine(HowC39.Icon, wc - Offset(cos(an), sin(an)) * (w * 0.04f), wc + Offset(cos(an), sin(an)) * (w * 0.04f), 1.5f * dp, alpha = f * seg(ht, 0.25f, 0.45f))
        }
        // a flask
        val fl = Offset(w * 0.7f, y)
        drawPath(Path().apply { moveTo(fl.x - w * 0.012f, fl.y - h * 0.09f); lineTo(fl.x + w * 0.012f, fl.y - h * 0.09f); lineTo(fl.x + w * 0.04f, fl.y + h * 0.02f); lineTo(fl.x - w * 0.04f, fl.y + h * 0.02f); close() }, HowC39.Glass, alpha = f * seg(ht, 0.4f, 0.6f) * 0.8f)
        tagAt(a.tag("howtools"), Offset(w * 0.52f, bench - h * 0.17f), seg(ht, 0.6f, 0.85f) * dimTools, back = Back39, ink = HowC39.Icon)
    }
    // why: a question science cannot answer
    if (ws > 0f) {
        val f = easeOut(seg(ws, 0.2f, 0.6f)) * (1f - 0.5f * seg(og, 0.3f, 0.8f))
        qmark21(Offset(w * 0.5f, h * 0.42f), h * 0.2f * f, Color(0xFFB8C0E0), f)
    }
    // light from above
    if (og > 0f) {
        val f = easeOut(seg(og, 0.1f, 0.6f))
        lightShafts(Offset(w * 0.5f, 0f), PI.toFloat() / 2f, 0.9f, h * 0.85f, Color(0xFFFFF0C8), 0.5f * f, t, rays = 9)
        softGlow(Offset(w * 0.5f, h * 0.45f), w * 0.2f, ArtInk.Gold, 0.3f * f)
    }
    // Jabir, a book and an alembic
    if (jf > 0f) {
        val f = easeOut(seg(jf, 0f, 0.35f))
        openBook21(Offset(w * 0.2f, bench - h * 0.04f), w * 0.14f, Color(0xFFF2E8D2), Color(0xFF8A7A5A), f, glow = 0.5f * f)
        val al = Offset(w * 0.36f, bench - h * 0.08f)
        drawCircle(HowC39.Glass, w * 0.035f, al, alpha = f * 0.8f)
        drawCircle(HowC39.Liquid, w * 0.025f, al + Offset(0f, h * 0.01f), alpha = f * 0.8f)
        drawLine(HowC39.Glass, al + Offset(w * 0.02f, -h * 0.03f), al + Offset(w * 0.09f, -h * 0.06f), 3f * dp, StrokeCap.Round, alpha = f)
        val drip = fract(t * 0.8f)
        drawCircle(HowC39.Liquid, w * 0.005f, al + Offset(w * 0.09f, -h * 0.05f + h * 0.06f * drip), alpha = f * (1f - drip))
        tagAt(a.tag("jabirflask"), Offset(w * 0.26f, bench - h * 0.2f), seg(jf, 0.4f, 0.7f), back = Back39, ink = ArtInk.Gold)
    }
}
