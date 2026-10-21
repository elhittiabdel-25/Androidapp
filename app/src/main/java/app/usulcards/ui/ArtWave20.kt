package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 20, round 13 of the redraw (2026-10-21): «أساسيات التوحيد». Jinn and men on the earth at dawn, made to
 * worship, the messenger sent to them and the two ends of the way; a man who follows his ﷺ light in four ways; and
 * three arched windows, one for each kind of tawhid, each with its opposite crossed out.
 */

/** One of the jinn: a smokeless flame, flickering, at [base], [s] tall; [bow] bends it forward. */
internal fun DrawScope.jinnFlame20(base: Offset, s: Float, t: Float, k: Int, alpha: Float, bow: Float = 0f) {
    if (alpha <= 0.01f) return
    val fl = 0.08f * sin(t * 7f + k * 1.7f) + 0.05f * sin(t * 11f + k)
    val tip = base + Offset(-s * 0.55f * bow + s * fl, -s * (1f - 0.35f * bow))
    val p = Path().apply {
        moveTo(base.x - s * 0.22f, base.y)
        quadraticTo(base.x - s * 0.3f, base.y - s * 0.45f, tip.x, tip.y)
        quadraticTo(base.x + s * 0.3f, base.y - s * 0.45f, base.x + s * 0.22f, base.y)
        close()
    }
    softGlow(base + Offset(0f, -s * 0.4f), s * 0.6f, Color(0xFFFF9A50), 0.5f * alpha)
    drawPath(p, Brush.verticalGradient(listOf(Color(0xFFFFE08A), Color(0xFFFF7A3A), Color(0xFF5A8AE0)), startY = tip.y, endY = base.y), alpha = alpha)
}

// ═════════════ 1. mankind: made to worship, the messenger sent, the two ends (purpose) ═════════════

private object ManC20 {
    val SkyTop = Color(0xFF6A7AB0)
    val SkyLow = Color(0xFFF8D8B0)
    val Ground = Color(0xFFC8B088)
    val GroundLow = Color(0xFFA8906A)
    val Ink = Color(0xFF2A2430)
    val Garden = Color(0xFF6ABF7A)
    val GardenDark = Color(0xFF3E8A52)
    val Gate = Color(0xFFF2DFA8)
    val Fire = Color(0xFFE85A2A)
    val Pit = Color(0xFF2A1410)
    val Back = Color(0xE6141826)
}

/**
 * The earth at dawn. Beats: created (on the left the jinn as smokeless flames, on the right men, each named once),
 * worshiponly (all bow together, the flames bending with them), sent (his ﷺ light comes over the horizon and stands
 * over the middle with his name; on the right a garden gate opens, on the left a pit glows red), obeyed (half of them,
 * men and jinn, follow his light to the garden and go in), disobeyed (the rest turn away to the pit and go down into it).
 */
internal fun DrawScope.mankindScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "created" to Shot(Offset(w * 0.5f, h * 0.62f), 1.1f),
        "worshiponly" to Shot(Offset(w * 0.5f, h * 0.66f), 1.15f),
        "sent" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "obeyed" to Shot(Offset(w * 0.66f, h * 0.6f), 1.15f),
        "disobeyed" to Shot(Offset(w * 0.34f, h * 0.62f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.55f), 1f), dim = 0.14f) { mankindEarth(a) }
}

private fun DrawScope.mankindEarth(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cr = a.b("created")
    val wo = a.b("worshiponly")
    val se = a.b("sent")
    val ob = a.b("obeyed")
    val di = a.b("disobeyed")
    val horizon = h * 0.55f
    drawRect(Brush.verticalGradient(listOf(ManC20.SkyTop, ManC20.SkyLow), endY = horizon), size = Size(w, horizon))
    softGlow(Offset(w * 0.5f, horizon), w * 0.35f, Color(0xFFFFE0B0), 0.6f)
    drawRect(Brush.verticalGradient(listOf(ManC20.Ground, ManC20.GroundLow), startY = horizon, endY = h), topLeft = Offset(0f, horizon), size = Size(w, h - horizon))

    // the two ends of the way, once the messenger has come
    val ends = easeOut(seg(se, 0.35f, 0.7f))
    val gate = Offset(w * 0.92f, h * 0.66f)
    val pit = Offset(w * 0.08f, h * 0.72f)
    if (ends > 0f) {
        softGlow(gate + Offset(0f, -h * 0.08f), w * 0.12f, ManC20.Garden, 0.7f * ends)
        drawOval(ManC20.GardenDark, topLeft = gate + Offset(-w * 0.1f, -h * 0.02f), size = Size(w * 0.2f, h * 0.05f), alpha = ends)
        for (k in 0 until 3) drawCircle(ManC20.Garden, w * 0.025f, gate + Offset(w * (-0.07f + 0.07f * k), -h * 0.07f), alpha = ends)
        drawPath(horseshoe(gate.x, gate.y - h * 0.09f, w * 0.03f, w * 0.025f, gate.y), ManC20.Gate.copy(alpha = ends))
        softGlow(pit, w * 0.12f, ManC20.Fire, 0.8f * ends)
        drawOval(ManC20.Pit, topLeft = pit + Offset(-w * 0.09f, -h * 0.035f), size = Size(w * 0.18f, h * 0.07f), alpha = ends)
        for (k in 0 until 5) {
            val f = fract(t * 1.3f + k / 5f)
            drawCircle(ManC20.Fire, (1.5f + 2f * (1f - f)) * dp, pit + Offset((k - 2) * w * 0.025f, -h * 0.02f - f * h * 0.08f), alpha = ends * (1f - f))
        }
    }
    // his ﷺ light over the middle of the plain
    val lc = Offset(w * 0.5f, mix(horizon + h * 0.02f, h * 0.36f, easeOut(seg(se, 0f, 0.4f))))
    if (se > 0f) {
        sirajMunir(lc, 4.6f * dp, easeOut(seg(se, 0f, 0.3f)), t, nameAt = lc + Offset(0f, -h * 0.1f))
    }

    // the jinn on the left, men on the right; even ones obey, odd ones turn away
    val n = 10
    val appear = easeOut(seg(cr, 0f, 0.5f))
    val bow = easeOut(seg(wo, 0f, 0.4f)) * (1f - easeOut(seg(se, 0f, 0.3f)))
    for (k in 0 until n) {
        val jinn = k < 4
        val home = if (jinn) Offset(w * (0.2f + 0.07f * k), h * (0.76f + 0.05f * (k % 2))) else Offset(w * (0.5f + 0.065f * (k - 4)), h * (0.78f + 0.05f * (k % 2)))
        val obeys = k % 2 == 0
        val go = if (obeys) easeInOut(seg(ob, 0.05f, 0.9f)) else easeInOut(seg(di, 0.05f, 0.9f))
        val to = if (obeys) gate else pit
        val p = home + (to - home) * go
        val inside = seg(go, 0.85f, 1f)
        val al = appear * (1f - inside) * seg(cr, k * 0.04f, k * 0.04f + 0.3f).coerceAtLeast(if (cr >= 1f) 1f else 0f)
        if (al <= 0.01f) continue
        val moving = go in 0.01f..0.99f
        val tall = h * 0.17f
        if (jinn) {
            jinnFlame20(p, tall * 0.7f, t, k, al, bow)
        } else {
            val faceRight = moving && obeys
            if (bow > 0.3f && !moving) {
                w8Worshipper(p, tall, ManC20.Ink, 0f, min(1f, bow), al)
            } else {
                withTransform({ if (faceRight) scale(-1f, 1f, pivot = p) }) { figure(p, tall, ManC20.Ink, al, walk = if (moving) t * 6f + k else 0f) }
            }
        }
        // the light-motes they come out of
        if (cr in 0.01f..0.6f) glint(p + Offset(0f, -tall * 0.5f), tall * 0.3f, Color.White, bump(seg(cr, k * 0.04f, k * 0.04f + 0.3f)))
    }
    tagAt(a.tag("created", 0, 0), Offset(w * 0.3f, h * 0.62f), seg(cr, 0.3f, 0.7f) * (1f - seg(se, 0f, 0.3f)), back = ManC20.Back, ink = Color(0xFFFFC890))
    tagAt(a.tag("created", 0, 1), Offset(w * 0.68f, h * 0.62f), seg(cr, 0.4f, 0.8f) * (1f - seg(se, 0f, 0.3f)), back = ManC20.Back, ink = ArtInk.Ivory)
}

// ═════════════ 2. obey4: four ways of following him ﷺ (rasul-meaning) ═════════════

private object ObeyC20 {
    val SkyTop = Color(0xFF9CC8E8)
    val SkyLow = Color(0xFFF2EEDC)
    val Wall = Color(0xFFE8D8B4)
    val Ground = Color(0xFFD8C49A)
    val Ink = Color(0xFF2A2430)
    val Poor = Color(0xFF5A4E60)
    val Coin = Color(0xFFF2C45A)
    val Jug = Color(0xFF6A1E2A)
    val Rug = Color(0xFF2E5A4A)
    val Back = Color(0xE6201810)
}

/**
 * A courtyard by day; his ﷺ light up on the right with his name, a man below it. Beats: guide4 (the light comes;
 * the man turns to it), obeycmd (a ray from it falls on a poor man sitting by the wall; the man goes and puts a coin
 * in his hand), believe (it tells him of what he cannot see: a bubble shows a garden beyond the wall; he lays his
 * hand on his heart, and a tick), avoid (it marks a jar of wine on his way in red; he steps back from it, and it is
 * crossed out), asshown (he prays as he was shown, behind the light, and beside him one who whirls in a worship of
 * his own making is crossed out).
 */
internal fun DrawScope.obey4Scene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "guide4" to Shot(Offset(w * 0.6f, h * 0.48f), 1.05f),
        "obeycmd" to Shot(Offset(w * 0.36f, h * 0.6f), 1.3f),
        "believe" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "avoid" to Shot(Offset(w * 0.6f, h * 0.62f), 1.3f),
        "asshown" to Shot(Offset(w * 0.45f, h * 0.58f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { obey4Yard(a) }
}

private fun DrawScope.obey4Yard(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val g4 = a.b("guide4")
    val oc = a.b("obeycmd")
    val be = a.b("believe")
    val av = a.b("avoid")
    val asb = a.b("asshown")
    val ground = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(ObeyC20.SkyTop, ObeyC20.SkyLow), endY = h * 0.36f), size = Size(w, h * 0.36f))
    drawRect(ObeyC20.Wall, topLeft = Offset(0f, h * 0.36f), size = Size(w, ground - h * 0.36f))
    drawRect(Color(0xFFCDB88E), topLeft = Offset(0f, h * 0.36f), size = Size(w, h * 0.02f))
    drawRect(ObeyC20.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))

    val lc = Offset(w * 0.78f, h * 0.22f)
    sirajMunir(lc, 4.6f * dp, easeOut(seg(g4, 0f, 0.5f)).coerceAtLeast(if (oc > 0f) 1f else 0f), t, nameAt = lc + Offset(-w * 0.16f, 0f))
    val tall = h * 0.34f
    // where the man is
    val give = easeInOut(seg(oc, 0.15f, 0.55f)) * (1f - easeInOut(seg(be, 0f, 0.4f)))
    val back = easeOut(seg(av, 0.35f, 0.6f))
    val toJar = easeInOut(seg(av, 0.05f, 0.35f))
    var mx = mix(w * 0.5f, w * 0.38f, give) + w * 0.08f * toJar - w * 0.06f * back
    if (asb > 0f) mx = mix(mx, w * 0.5f, easeInOut(seg(asb, 0f, 0.25f)))
    val foot = Offset(mx, ground + h * 0.04f)
    val faceRight = give <= 0.01f || toJar > 0f
    // the poor man by the wall
    val poor = Offset(w * 0.27f, ground)
    if (oc > 0f || be > 0f) {
        w8Seated(poor, tall * 0.9f, ObeyC20.Poor, 1f, alpha = 1f - seg(asb, 0f, 0.3f), speak = 0.6f)
        val ray = seg(oc, 0f, 0.25f) * (1f - seg(oc, 0.6f, 0.9f))
        if (ray > 0f) {
            drawLine(ArtInk.Gold, lc, poor + Offset(0f, -tall * 0.5f), 2f * dp, StrokeCap.Round, alpha = 0.6f * ray)
            softGlow(poor + Offset(0f, -tall * 0.3f), tall * 0.4f, ArtInk.Gold, 0.5f * ray)
        }
        val coin = easeOut(seg(oc, 0.5f, 0.7f))
        if (coin > 0f) {
            val c = poor + Offset(tall * 0.24f, -tall * 0.36f)
            softGlow(c, 8f * dp, ObeyC20.Coin, 0.7f * coin)
            drawCircle(ObeyC20.Coin, 3f * dp, c, alpha = coin * (1f - seg(asb, 0f, 0.3f)))
        }
    }
    // the jar of wine on his way
    val jar = Offset(w * 0.66f, ground + h * 0.02f)
    if (av > 0f) {
        val mark = easeOut(seg(av, 0.05f, 0.3f))
        softGlow(jar + Offset(0f, -h * 0.04f), h * 0.1f, Color(0xFFD23C30), 0.6f * mark * (1f - seg(asb, 0f, 0.3f)))
        drawOval(ObeyC20.Jug, topLeft = jar + Offset(-w * 0.025f, -h * 0.09f), size = Size(w * 0.05f, h * 0.08f), alpha = 1f - seg(asb, 0f, 0.3f))
        drawRect(ObeyC20.Jug, topLeft = jar + Offset(-w * 0.008f, -h * 0.11f), size = Size(w * 0.016f, h * 0.025f), alpha = 1f - seg(asb, 0f, 0.3f))
        noSign11pub(jar + Offset(0f, -h * 0.06f), h * 0.07f, easeOut(seg(av, 0.5f, 0.75f)) * (1f - seg(asb, 0f, 0.3f)))
    }
    // the man himself
    if (asb > 0.25f) {
        // prays as he was shown: standing, then bowing, behind the light
        val cyc = fract(t * 0.22f)
        val bow = if (cyc > 0.5f) bump((cyc - 0.5f) * 2f) else 0f
        drawRect(ObeyC20.Rug, topLeft = foot + Offset(-w * 0.05f, -h * 0.005f), size = Size(w * 0.1f, h * 0.02f))
        withTransform({ scale(-1f, 1f, pivot = foot) }) { w8Worshipper(foot, tall, ObeyC20.Ink, if (cyc < 0.15f) 1f else 0f, bow) }
        // one whirling in a worship of his own making
        val wf = Offset(w * 0.2f, ground + h * 0.04f)
        val spin = sin(t * 6f)
        withTransform({ scale(if (spin > 0f) 1f else -1f, 1f, pivot = wf) }) {
            figure(wf, tall * 0.92f, Color(0xFF5A4A5A), seg(asb, 0.25f, 0.45f), lean = 0.1f * spin)
        }
        for (sd in listOf(-1f, 1f)) drawLine(Color(0xFF5A4A5A), wf + Offset(0f, -tall * 0.7f), wf + Offset(sd * tall * 0.25f, -tall * (0.95f + 0.05f * spin * sd)), max(1.6f * dp, tall * 0.055f), StrokeCap.Round, alpha = seg(asb, 0.25f, 0.45f))
        cross14(wf + Offset(0f, -tall * 1.15f), h * 0.04f, seg(asb, 0.5f, 0.8f))
    } else {
        val walking = (oc in 0.15f..0.55f) || (be in 0.01f..0.4f) || (av in 0.05f..0.6f)
        withTransform({ if (faceRight) scale(-1f, 1f, pivot = foot) }) {
            figure(foot, tall, ObeyC20.Ink, easeOut(seg(g4, 0f, 0.4f)).coerceAtLeast(if (oc > 0f) 1f else 0f), walk = if (walking) t * 6f else 0f, arms = if (give > 0.9f && oc < 0.75f) 0.8f else 0f, lean = -0.12f * back)
        }
        // believe: hand on his heart; the news of what he cannot see
        if (be > 0f) {
            val heart = foot + Offset(0f, -tall * 0.6f)
            val hand = easeOut(seg(be, 0.4f, 0.6f)) * (1f - seg(av, 0f, 0.2f))
            if (hand > 0f) drawLine(ObeyC20.Ink, foot + Offset(0f, -tall * 0.76f), heart, max(1.7f * dp, tall * 0.06f), StrokeCap.Round, alpha = hand)
            for (k in 0 until 3) {
                val f = fract(t * 1.3f + k / 3f)
                val c = lc + (heart - lc) * (0.2f + 0.6f * f)
                drawCircle(ArtInk.Gold, 2f * dp, c, alpha = seg(be, 0f, 0.2f) * (1f - seg(be, 0.5f, 0.7f)) * (1f - f))
            }
            val bub = seg(be, 0.15f, 0.35f) * (1f - seg(av, 0f, 0.2f))
            if (bub > 0f) {
                val bc = Offset(w * 0.36f, h * 0.22f)
                drawCircle(Color.White, h * 0.11f, bc, alpha = 0.9f * bub)
                for (k in 0 until 2) drawCircle(Color.White, h * (0.02f - 0.008f * k), bc + Offset(w * (0.05f + 0.03f * k), h * (0.12f + 0.05f * k)), alpha = 0.9f * bub)
                // a garden beyond what he can see
                drawOval(Color(0xFF6ABF7A), topLeft = bc + Offset(-h * 0.08f, h * 0.0f), size = Size(h * 0.16f, h * 0.05f), alpha = bub)
                for (k in 0 until 3) drawCircle(Color(0xFF4E9A5A), h * 0.025f, bc + Offset(h * (-0.05f + 0.05f * k), -h * 0.015f), alpha = bub)
                drawPath(horseshoe(bc.x, bc.y - h * 0.03f, h * 0.022f, h * 0.018f, bc.y + h * 0.015f), Color(0xFFF2DFA8).copy(alpha = bub))
            }
            tick14(foot + Offset(tall * 0.3f, -tall * 1.1f), h * 0.035f, seg(be, 0.6f, 0.85f) * (1f - seg(av, 0f, 0.2f)))
        }
    }
}

// ═════════════ 3. triptych: three windows, three kinds of tawhid (tawhid-anwa) ═════════════

private object TriC20 {
    val Wall = Color(0xFFE6D6B4)
    val WallLow = Color(0xFFCDB88E)
    val Frame = Color(0xFF9A7A4A)
    val FrameLight = Color(0xFFD8B878)
    val Sky = Color(0xFF8CC4EC)
    val SkyLow = Color(0xFFF2EAD0)
    val Ground = Color(0xFF9AB070)
    val Ink = Color(0xFF2A2430)
    val Idol = Color(0xFF8A7A6A)
    val Plaque = Color(0xFFFBF2DC)
    val Smear = Color(0xFF3A3038)
    val Floor = Color(0xFFC8AE84)
}

/**
 * A wall with three arched windows, read from the right. Beats: rububiyya (in the right window the sun climbs and
 * sets and the moon follows, a cloud rains and a plant grows: all of it ordered), rubopp (a man on a hill in it pulls
 * at a rope tied to the sun as if he steered it: crossed out), uluhiyya (in the middle window two rows of people bow
 * together), ulopp (one of them turns and bows to an idol: crossed out), asma (in the left window a plaque, the verse
 * written on it), asmopp (one comes to smear it over with a brush; the smear will not hold, and he is crossed out).
 */
internal fun DrawScope.triptychScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rububiyya" to Shot(Offset(w * 0.82f, h * 0.5f), 1.55f),
        "rubopp" to Shot(Offset(w * 0.82f, h * 0.52f), 1.6f),
        "uluhiyya" to Shot(Offset(w * 0.5f, h * 0.52f), 1.55f),
        "ulopp" to Shot(Offset(w * 0.5f, h * 0.54f), 1.6f),
        "asma" to Shot(Offset(w * 0.18f, h * 0.5f), 1.6f),
        "asmopp" to Shot(Offset(w * 0.18f, h * 0.52f), 1.6f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { triptychWall(a) }
}

private fun DrawScope.triptychWall(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rb = a.b("rububiyya")
    val ro = a.b("rubopp")
    val ul = a.b("uluhiyya")
    val uo = a.b("ulopp")
    val asm = a.b("asma")
    val ao = a.b("asmopp")
    drawRect(Brush.verticalGradient(listOf(TriC20.Wall, TriC20.WallLow)))
    drawRect(TriC20.Floor, topLeft = Offset(0f, h * 0.9f), size = Size(w, h * 0.1f))
    val cxs = listOf(w * 0.82f, w * 0.5f, w * 0.18f)
    val hw = w * 0.14f
    val top = h * 0.12f
    val bottom = h * 0.86f
    for ((k, cx) in cxs.withIndex()) {
        val arch = horseshoe(cx, top + hw * 0.9f, hw, hw * 0.92f, bottom)
        val lit = when (k) { 0 -> rb; 1 -> ul; else -> asm }
        drawPath(arch, Brush.verticalGradient(listOf(TriC20.Sky, TriC20.SkyLow), startY = top, endY = bottom))
        clipPath(arch) {
            drawRect(TriC20.Ground, topLeft = Offset(cx - hw * 1.2f, h * 0.7f), size = Size(hw * 2.4f, bottom - h * 0.7f))
            when (k) {
                0 -> rububiyyaPane(cx, hw, rb, ro, t)
                1 -> uluhiyyaPane(cx, hw, ul, uo, t)
                else -> asmaPane(a, cx, hw, asm, ao, t)
            }
            if (lit <= 0f) drawRect(Color.Black, topLeft = Offset(cx - hw * 1.2f, top), size = Size(hw * 2.4f, bottom - top), alpha = 0.25f)
        }
        drawPath(arch, TriC20.Frame, style = Stroke(5f * dp))
        drawPath(arch, TriC20.FrameLight, style = Stroke(1.4f * dp))
    }
}

private fun DrawScope.rububiyyaPane(cx: Float, hw: Float, rb: Float, ro: Float, t: Float) {
    val h = size.height
    val dp = density
    if (rb <= 0f) return
    val on = easeOut(seg(rb, 0f, 0.2f))
    // the sun and the moon on their arcs, the cloud and its rain, the plant growing
    val u = fract(t * 0.12f)
    val ang = PI.toFloat() * (1f - u)
    val sun = Offset(cx + cos(ang) * hw * 0.75f, h * 0.6f - sin(ang) * h * 0.36f)
    softGlow(sun, hw * 0.35f, Color(0xFFFFE08A), 0.8f * on)
    drawCircle(Color(0xFFFFC64C), hw * 0.12f, sun, alpha = on)
    val mAng = ang + PI.toFloat()
    val moon = Offset(cx + cos(mAng) * hw * 0.75f, h * 0.6f - sin(mAng) * h * 0.36f)
    if (moon.y < h * 0.62f) {
        drawCircle(Color(0xFFF4EED8), hw * 0.09f, moon, alpha = on)
        drawCircle(Color(0xFFBFD8EC), hw * 0.08f, moon + Offset(hw * 0.04f, -hw * 0.01f), alpha = on)
    }
    val cl = Offset(cx - hw * 0.35f, h * 0.38f)
    for ((dx, r) in listOf(-0.15f to 0.12f, 0f to 0.16f, 0.15f to 0.12f)) drawCircle(Color.White, hw * r, cl + Offset(hw * dx, 0f), alpha = on)
    for (m in 0 until 6) {
        val f = fract(t * 1.4f + m / 6f)
        val x = cl.x + (m - 2.5f) * hw * 0.07f
        drawLine(Color(0xFF6A9AD0), Offset(x, cl.y + hw * 0.12f + f * h * 0.22f), Offset(x - 1.5f * dp, cl.y + hw * 0.18f + f * h * 0.22f), 1.1f * dp, alpha = on)
    }
    val grow = easeOut(seg(rb, 0.3f, 0.9f))
    val pb = Offset(cl.x, h * 0.72f)
    drawLine(Color(0xFF3E7A3A), pb, pb + Offset(0f, -h * 0.08f * grow), 1.8f * dp, StrokeCap.Round)
    for (sd in listOf(-1f, 1f)) drawOval(Color(0xFF4E9A4A), topLeft = pb + Offset(if (sd < 0) -hw * 0.14f else 0f, -h * 0.06f * grow), size = Size(hw * 0.14f * grow, h * 0.02f * grow))
    // the one who claims to steer it
    if (ro > 0f) {
        val al = easeOut(seg(ro, 0f, 0.2f))
        val foot = Offset(cx + hw * 0.45f, h * 0.74f)
        val tall = h * 0.16f
        val tug = sin(t * 4f) * 0.5f + 0.5f
        withTransform({ scale(-1f, 1f, pivot = foot) }) { figure(foot, tall, TriC20.Ink, al, arms = 0.6f + 0.4f * tug, lean = -0.15f * tug) }
        val hand = foot + Offset(-tall * 0.25f, -tall * 0.62f)
        drawPath(Path().apply { moveTo(hand.x, hand.y); quadraticTo((hand.x + sun.x) / 2f, (hand.y + sun.y) / 2f + h * 0.05f, sun.x, sun.y) }, Color(0xFF8A6A3A).copy(alpha = al), style = Stroke(1.2f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp))))
        noSign11pub(foot + Offset(0f, -tall * 0.55f), tall * 0.5f, easeOut(seg(ro, 0.35f, 0.6f)))
    }
}

private fun DrawScope.uluhiyyaPane(cx: Float, hw: Float, ul: Float, uo: Float, t: Float) {
    val h = size.height
    if (ul <= 0f) return
    val on = easeOut(seg(ul, 0f, 0.2f))
    val cyc = fract(t * 0.25f)
    val bow = if (cyc > 0.4f) bump((cyc - 0.4f) / 0.6f) else 0f
    val tall = h * 0.13f
    for (row in 0 until 2) for (i in 0 until 4) {
        val foot = Offset(cx - hw * 0.6f + i * hw * 0.36f, h * (0.66f + 0.13f * row))
        val turned = row == 1 && i == 3 && uo > 0f
        if (turned) continue
        w8Worshipper(foot, tall, TriC20.Ink, if (cyc < 0.2f) 1f else 0f, bow, on)
    }
    // one turns to an idol and bows to it
    if (uo > 0f) {
        val al = easeOut(seg(uo, 0f, 0.25f))
        val idol = Offset(cx + hw * 0.85f, h * 0.79f)
        drawRect(TriC20.Idol, topLeft = idol + Offset(-hw * 0.08f, -h * 0.03f), size = Size(hw * 0.16f, h * 0.03f), alpha = al)
        drawRect(TriC20.Idol, topLeft = idol + Offset(-hw * 0.05f, -h * 0.1f), size = Size(hw * 0.1f, h * 0.07f), alpha = al)
        drawCircle(TriC20.Idol, hw * 0.06f, idol + Offset(0f, -h * 0.12f), alpha = al)
        val foot = Offset(cx - hw * 0.6f + 3 * hw * 0.36f, h * 0.79f)
        withTransform({ scale(-1f, 1f, pivot = foot) }) { w8Worshipper(foot, tall, Color(0xFF5A4A5A), 0f, easeOut(seg(uo, 0.15f, 0.45f)), 1f) }
        noSign11pub(foot + Offset(hw * 0.2f, -tall * 0.5f), tall * 0.55f, easeOut(seg(uo, 0.4f, 0.7f)))
    }
}

private fun DrawScope.asmaPane(a: ArtCtx, cx: Float, hw: Float, asm: Float, ao: Float, t: Float) {
    val h = size.height
    val dp = density
    if (asm <= 0f) return
    val on = easeOut(seg(asm, 0f, 0.25f))
    // the plaque with the verse
    val pc = Offset(cx, h * 0.5f)
    softGlow(pc, hw * 0.9f, ArtInk.Gold, 0.4f * on)
    drawRect(TriC20.Frame, topLeft = pc - Offset(hw * 0.88f, h * 0.11f), size = Size(hw * 1.76f, h * 0.22f), alpha = on)
    drawRect(TriC20.Plaque, topLeft = pc - Offset(hw * 0.82f, h * 0.095f), size = Size(hw * 1.64f, h * 0.19f), alpha = on)
    val tl = a.big("asma")
    if (tl != null) {
        val sc = min(0.62f, hw * 1.55f / max(1f, tl.size.width.toFloat()))
        bigText(tl, pc, sc, ArtInk.Shade, on)
    }
    // ilhad: a brush tries to smear it over; the smear does not hold
    if (ao > 0f) {
        val al = easeOut(seg(ao, 0f, 0.2f))
        val foot = Offset(cx + hw * 0.6f, h * 0.84f)
        val tall = h * 0.2f
        figure(foot, tall, Color(0xFF5A4A5A), al, arms = 1f, lean = 0.1f)
        val brush = foot + Offset(-tall * 0.28f, -tall * 0.76f)
        drawLine(Color(0xFF6A4A2A), brush, brush + Offset(-hw * 0.2f, -h * 0.04f), 1.8f * dp, StrokeCap.Round, alpha = al)
        val smear = easeOut(seg(ao, 0.1f, 0.45f)) * (1f - easeOut(seg(ao, 0.55f, 0.9f)))
        if (smear > 0f) drawRect(TriC20.Smear, topLeft = pc + Offset(hw * 0.82f - hw * 1.64f * smear, -h * 0.03f), size = Size(hw * 1.64f * smear, h * 0.06f), alpha = 0.8f)
        noSign11pub(foot + Offset(0f, -tall * 0.55f), tall * 0.45f, easeOut(seg(ao, 0.5f, 0.8f)))
    }
}
