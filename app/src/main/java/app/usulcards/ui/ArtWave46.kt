package app.usulcards.ui

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
import kotlin.math.sin

/*
 * Wave 46 (2026-10-28): «رياض الصالحين»، «حقوق وصلات» (1). The middle gate of the garden that a parent keeps open;
 * threads of kinship that one hand ties again when the other cuts them; an alley of neighbours and the broth with
 * more water; the sponsor of an orphan beside the light ﷺ; the home kept by good character, drawn in the notebook.
 * The Prophet ﷺ is only light with his name; Jibril is a cool white light; Allah is never drawn.
 */

private val Back46 = Color(0xE6181420)

// ═════════════ 1. the middle gate (riyad-walidayn) ═════════════

private object GateC46 {
    val Sky = Color(0xFF8FBFE6)
    val SkyLow = Color(0xFFF6E3BE)
    val Ground = Color(0xFFD9C29A)
    val Wall = Color(0xFFC9A877)
    val WallDark = Color(0xFF8E6E46)
    val Leaf = Color(0xFF3E5A3A)
    val Step = Color(0xFFE8D6B0)
    val Glow = Color(0xFFFFE2A0)
    val Person = Color(0xFF3A3040)
    val Elder = Color(0xFF5A4A5A)
    val Heart = Color(0xFFE0645A)
    val Banner = Color(0xFF7A3A3A)
    val Road = Color(0xFFBFA77E)
}

/**
 * Beats: bestdeeds (three steps light up one after another: «الصَّلَاةَ عَلَى وَقْتِهَا», «بِرَّ الْوَالِدَيْنِ», «الْجِهَادَ
 * فِي سَبِيلِ اللَّهِ»), mother3 (a mother and a father sitting; three hearts rise over her, then one over him), rughim
 * (an open gate beside two old parents; a figure walks off and the gate dims), fihima (a fork: a pennant on a hill one
 * way, a house with two parents the other; the figure turns home and the house lights), midgate (a wall of five gates:
 * the middle one opens on light).
 */
internal fun DrawScope.parentsgateScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "bestdeeds" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "mother3" to Shot(Offset(w * 0.5f, h * 0.55f), 1.3f),
        "rughim" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "fihima" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "midgate" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { gardenGates(a) }
}

private fun DrawScope.gardenGates(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bd = a.b("bestdeeds")
    val m3 = a.b("mother3")
    val rg = a.b("rughim")
    val fh = a.b("fihima")
    val mg = a.b("midgate")
    val ground = h * 0.82f
    daySky12(GateC46.Sky, GateC46.SkyLow, ground, GateC46.Ground)
    sun12(Offset(w * 0.86f, h * 0.14f), w * 0.035f, 1f)
    for (k in 0 until 4) palm12(Offset(w * (0.04f + 0.3f * k), ground), h * 0.32f, Color(0xFF7A5A3A), GateC46.Leaf, 0.35f, t + k)
    // three deeds, three steps
    if (bd > 0f) {
        val f = easeOut(seg(bd, 0f, BEAT_IN)) * (1f - seg(m3, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val g = easeOut(seg(bd, 0.1f + 0.22f * k, 0.3f + 0.22f * k))
            val x = w * (0.2f + 0.24f * k)
            val top = ground - h * (0.12f + 0.12f * k)
            drawRect(GateC46.Step, Offset(x, top), Size(w * 0.24f, ground - top), alpha = f * g)
            drawRect(GateC46.WallDark, Offset(x, top), Size(w * 0.24f, 2f * dp), alpha = f * g)
            softGlow(Offset(x + w * 0.12f, top - h * 0.05f), w * 0.06f, GateC46.Glow, 0.5f * f * g)
            tagAt(a.tag("bestdeeds", 0, k), Offset(x + w * 0.12f, top - h * 0.1f), f * g, back = Back46, ink = ArtInk.Gold)
        }
        // the deeds are climbed in this order
        val climb = easeInOut(seg(bd, 0.15f, 0.85f))
        val fx = w * (0.26f + 0.48f * climb)
        val fy = ground - h * (0.12f + 0.24f * climb)
        figure(Offset(fx, fy), h * 0.14f, GateC46.Person, f, walk = t * 5f * (1f - seg(bd, 0.85f, 0.9f)))
    }
    // the mother three times, then the father
    if (m3 > 0f) {
        val f = easeOut(seg(m3, 0f, BEAT_IN)) * (1f - seg(rg, 0f, BEAT_OUT))
        val mom = Offset(w * 0.4f, ground)
        val dad = Offset(w * 0.62f, ground)
        womanSit12(mom, h * 0.24f, GateC46.Elder, f, 1f)
        sit12(dad, h * 0.26f, GateC46.Elder, f, -1f)
        for (k in 0 until 3) {
            val g = easeOut(seg(m3, 0.15f + 0.18f * k, 0.3f + 0.18f * k))
            heart12(mom + Offset(0f, -h * (0.34f + 0.08f * k)), w * 0.045f * g, GateC46.Heart, f * g, fill = true)
        }
        val g = easeOut(seg(m3, 0.72f, 0.88f))
        heart12(dad + Offset(0f, -h * 0.36f), w * 0.045f * g, GateC46.Heart, f * g, fill = true)
        tagAt(a.tag("mother3", 0, 0), mom + Offset(0f, h * 0.08f), f * seg(m3, 0.15f, 0.3f), back = Back46, ink = ArtInk.Ivory)
        tagAt(a.tag("mother3", 0, 1), dad + Offset(0f, h * 0.08f), f * g, back = Back46, ink = ArtInk.Ivory)
    }
    // the gate beside the parents, and one who walks away from it
    if (rg > 0f) {
        val f = easeOut(seg(rg, 0f, BEAT_IN)) * (1f - seg(fh, 0f, BEAT_OUT))
        val dim = seg(rg, 0.45f, 0.9f)
        val sill = Offset(w * 0.32f, ground)
        drawRect(GateC46.Wall, Offset(w * 0.12f, ground - h * 0.42f), Size(w * 0.4f, h * 0.42f), alpha = f)
        door12(sill, w * 0.11f, h * 0.34f, 1f - 0.8f * dim, GateC46.WallDark, Color(0xFF6A4A2A), f, GateC46.Glow)
        womanSit12(Offset(w * 0.48f, ground), h * 0.2f, GateC46.Elder, f, -1f)
        sit12(Offset(w * 0.56f, ground), h * 0.22f, GateC46.Elder, f, -1f)
        val walk = easeInOut(seg(rg, 0.2f, 0.95f))
        figure(Offset(w * (0.62f + 0.3f * walk), ground), h * 0.2f, GateC46.Person, f, walk = t * 6f)
    }
    // two roads: the pennant on the hill, or home to the parents
    if (fh > 0f) {
        val f = easeOut(seg(fh, 0f, BEAT_IN)) * (1f - seg(mg, 0f, BEAT_OUT))
        val fork = Offset(w * 0.5f, ground - h * 0.02f)
        val start = Offset(w * 0.5f, h * 0.98f)
        drawLine(GateC46.Road, start, fork, w * 0.05f, StrokeCap.Round, alpha = f)
        drawLine(GateC46.Road, fork, Offset(w * 0.24f, ground - h * 0.16f), w * 0.035f, StrokeCap.Round, alpha = f)
        drawLine(GateC46.Road, fork, Offset(w * 0.76f, ground - h * 0.08f), w * 0.035f, StrokeCap.Round, alpha = f)
        drawPath(Path().apply { moveTo(w * 0.08f, ground); quadraticTo(w * 0.22f, ground - h * 0.32f, w * 0.38f, ground); close() }, Color(0xFFB8A07A), alpha = f)
        val pole = Offset(w * 0.22f, ground - h * 0.2f)
        drawLine(GateC46.WallDark, pole, pole + Offset(0f, -h * 0.22f), 2.5f * dp, StrokeCap.Round, alpha = f)
        val flap = sin(t * 3f) * h * 0.01f
        drawPath(Path().apply { moveTo(pole.x, pole.y - h * 0.22f); lineTo(pole.x + w * 0.08f, pole.y - h * 0.19f + flap); lineTo(pole.x, pole.y - h * 0.15f); close() }, GateC46.Banner, alpha = f)
        val lit = seg(fh, 0.55f, 0.9f)
        house12(Offset(w * 0.8f, ground - h * 0.04f), w * 0.2f, GateC46.Wall, GateC46.WallDark, f, lit)
        womanSit12(Offset(w * 0.7f, ground), h * 0.14f, GateC46.Elder, f * lit, 1f)
        sit12(Offset(w * 0.92f, ground), h * 0.15f, GateC46.Elder, f * lit, -1f)
        // he stands at the fork, then goes home
        val go = easeInOut(seg(fh, 0.3f, 0.85f))
        val p = fork + (Offset(w * 0.72f, ground - h * 0.07f) - fork) * go
        figure(p, h * 0.18f, GateC46.Person, f, walk = t * 6f * go, lean = -0.1f * go)
    }
    // the middle gate
    if (mg > 0f) {
        val f = easeOut(seg(mg, 0f, 0.3f))
        drawRect(GateC46.Wall, Offset(0f, ground - h * 0.4f), Size(w, h * 0.4f), alpha = f)
        drawRect(GateC46.WallDark, Offset(0f, ground - h * 0.42f), Size(w, h * 0.025f), alpha = f)
        for (k in 0 until 5) {
            val x = w * (0.1f + 0.2f * k)
            val mid = k == 2
            val open = if (mid) easeInOut(seg(mg, 0.3f, 0.75f)) else 0f
            door12(Offset(x, ground), w * (if (mid) 0.11f else 0.08f), h * (if (mid) 0.33f else 0.26f), open, GateC46.WallDark, Color(0xFF6A4A2A), f, GateC46.Glow)
        }
        if (mg > 0.5f) lightShafts(Offset(w * 0.5f, ground - h * 0.15f), -PI.toFloat() / 2f, 0.9f, h * 0.5f, GateC46.Glow, 0.4f * seg(mg, 0.5f, 0.9f), t, 7)
    }
}

// ═════════════ 2. threads of kinship (riyad-rahim) ═════════════

private object KinC46 {
    val Sky = Color(0xFF151A30)
    val SkyLow = Color(0xFF2E3058)
    val Ground = Color(0xFF242436)
    val Wall = Color(0xFF6E6070)
    val Roof = Color(0xFF4A3E4E)
    val Thread = Color(0xFFF2C46A)
    val Cut = Color(0xFF8A7E90)
    val Basket = Color(0xFFB08048)
    val Date = Color(0xFF8A3A2A)
    val Road = Color(0xFF8E8070)
    val Coin = Color(0xFFE8C25A)
}

private val kinHouses46 = listOf(Offset(0.18f, 0.74f), Offset(0.36f, 0.66f), Offset(0.52f, 0.76f), Offset(0.68f, 0.64f), Offset(0.84f, 0.74f))

/**
 * Beats: hangs (five houses in a valley at night; threads of gold join them and rise from each into the light above),
 * widens (the threads of one house thicken: a basket beside it fills with dates and its road runs on further), reconnect
 * (one house cuts its thread to another; the other ties it again and the knot glows), twofold (a coin to a stranger
 * lights once, «صَدَقَةٌ»; a coin to a kinsman lights twice, «اثْنَتَانِ»).
 */
internal fun DrawScope.kinthreadsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "hangs" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "widens" to Shot(Offset(w * 0.36f, h * 0.62f), 1.35f),
        "reconnect" to Shot(Offset(w * 0.6f, h * 0.68f), 1.4f),
        "twofold" to Shot(Offset(w * 0.5f, h * 0.45f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { kinValley(a) }
}

private fun DrawScope.kinValley(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val hg = a.b("hangs")
    val wd = a.b("widens")
    val rc = a.b("reconnect")
    val tf = a.b("twofold")
    val ground = h * 0.8f
    nightSky(t, KinC46.Sky, KinC46.SkyLow, ground, starAlpha = 0.7f)
    drawPath(Path().apply {
        moveTo(0f, h * 0.62f); quadraticTo(w * 0.5f, h * 0.9f, w, h * 0.6f); lineTo(w, h); lineTo(0f, h); close()
    }, KinC46.Ground)
    val pts = kinHouses46.map { Offset(w * it.x, h * it.y) }
    val show = easeOut(seg(hg, 0f, BEAT_IN)) * (1f - seg(tf, 0f, BEAT_OUT))
    for ((k, p) in pts.withIndex()) house12(p, w * 0.1f, KinC46.Wall, KinC46.Roof, show, 0.6f)
    // the threads between the houses, and from each up into the light
    if (hg > 0f) {
        val f = show
        val join = easeInOut(seg(hg, 0.15f, 0.55f))
        for (k in 0 until pts.size - 1) {
            if (k == 2 && rc > 0f) continue                      // the thread that is cut and tied again is drawn below
            val p0 = pts[k] + Offset(0f, -h * 0.07f)
            val p1 = pts[k + 1] + Offset(0f, -h * 0.07f)
            val mid = (p0 + p1) / 2f + Offset(0f, h * 0.05f)
            drawPath(cubicPart(p0, mid, mid, p1, 0f, join), KinC46.Thread, alpha = f * 0.85f, style = Stroke(2f * dp, cap = StrokeCap.Round))
        }
        val up = easeInOut(seg(hg, 0.45f, 0.95f))
        for ((k, p) in pts.withIndex()) {
            val top = Offset(w * 0.5f + (p.x - w * 0.5f) * 0.25f, h * 0.06f)
            val from = p + Offset(0f, -h * 0.08f)
            val tip = from + (top - from) * up
            drawLine(KinC46.Thread, from, tip, 1.4f * dp, StrokeCap.Round, alpha = f * 0.5f * (0.7f + 0.3f * sin(t * 2f + k)))
        }
        softGlow(Offset(w * 0.5f, h * 0.04f), w * 0.2f, KinC46.Thread, 0.35f * up * f)
    }
    // the one who keeps the ties: provision and a longer road
    if (wd > 0f) {
        val f = easeOut(seg(wd, 0f, BEAT_IN)) * (1f - seg(tf, 0f, BEAT_OUT))
        val p = pts[1]
        softGlow(p + Offset(0f, -h * 0.05f), w * 0.08f, KinC46.Thread, 0.5f * f)
        val basket = p + Offset(-w * 0.07f, 0f)
        cup12(basket, w * 0.06f, KinC46.Basket, f)
        val fill = seg(wd, 0.2f, 0.6f)
        for (k in 0 until (fill * 9f).toInt()) drawCircle(KinC46.Date, w * 0.007f, basket + Offset(w * (0.012f * (k % 4) - 0.018f), -h * (0.035f + 0.012f * (k / 4))), alpha = f)
        val road = easeInOut(seg(wd, 0.4f, 0.95f))
        drawLine(KinC46.Road, p + Offset(w * 0.03f, h * 0.02f), p + Offset(w * (0.03f + 0.26f * road), h * 0.12f), 3f * dp, StrokeCap.Round, alpha = f)
    }
    // cut, and tied again
    if (rc > 0f) {
        val f = easeOut(seg(rc, 0f, BEAT_IN)) * (1f - seg(tf, 0f, BEAT_OUT))
        val p0 = pts[2] + Offset(0f, -h * 0.07f)
        val p1 = pts[3] + Offset(0f, -h * 0.07f)
        val mid = (p0 + p1) / 2f + Offset(0f, h * 0.05f)
        val cut = seg(rc, 0.15f, 0.3f)
        val tie = easeInOut(seg(rc, 0.45f, 0.8f))
        val gap = w * 0.03f * cut * (1f - tie)
        val m = cubic(p0, mid, mid, p1, 0.5f)
        drawLine(if (tie > 0.5f) KinC46.Thread else KinC46.Cut, p0, m + Offset(-gap, gap * 0.6f), 2f * dp, StrokeCap.Round, alpha = f)
        drawLine(if (tie > 0.5f) KinC46.Thread else KinC46.Cut, m + Offset(gap, gap * 0.6f), p1, 2f * dp, StrokeCap.Round, alpha = f)
        if (tie > 0.6f) {
            drawCircle(KinC46.Thread, w * 0.008f, m, alpha = f)
            softGlow(m, w * 0.04f, KinC46.Thread, 0.6f * f * seg(rc, 0.75f, 0.95f))
        }
        // the one who cut stays dark; the one who ties is lit
        house12(pts[3], w * 0.1f, KinC46.Wall, KinC46.Roof, f, 0.6f * (1f - cut))
        house12(pts[2], w * 0.1f, KinC46.Wall, KinC46.Roof, f, 0.6f + 0.4f * tie)
    }
    // a coin to a stranger, a coin to a kinsman
    if (tf > 0f) {
        val f = easeOut(seg(tf, 0f, 0.3f))
        for (k in 0 until 2) {
            val c = Offset(w * (0.3f + 0.4f * k), h * 0.45f)
            val drop = easeOut(seg(tf, 0.1f + 0.3f * k, 0.3f + 0.3f * k))
            coin12(c + Offset(0f, -h * 0.15f * (1f - drop)), w * 0.035f, f * drop, KinC46.Coin)
            val lights = if (k == 0) 1 else 2
            for (j in 0 until lights) {
                val g = seg(tf, 0.3f + 0.3f * k + 0.1f * j, 0.45f + 0.3f * k + 0.1f * j)
                softGlow(c + Offset(w * 0.05f * (j * 2 - (lights - 1)), -h * 0.13f), w * 0.04f, KinC46.Thread, 0.8f * f * g)
            }
            tagAt(a.tag("twofold", 0, k), c + Offset(0f, h * 0.13f), f * seg(tf, 0.25f + 0.3f * k, 0.4f + 0.3f * k), back = Back46, ink = ArtInk.Gold)
        }
        house12(Offset(w * 0.7f, h * 0.78f), w * 0.1f, KinC46.Wall, KinC46.Roof, f, 0.8f)
        drawLine(KinC46.Thread, Offset(w * 0.7f, h * 0.7f), Offset(w * 0.7f, h * 0.52f), 1.5f * dp, StrokeCap.Round, alpha = f * 0.6f)
    }
}

// ═════════════ 3. an alley of neighbours (riyad-jar) ═════════════

private object AlleyC46 {
    val Sky = Color(0xFFA9CBE3)
    val SkyLow = Color(0xFFF2E6CC)
    val Ground = Color(0xFFCBB48C)
    val Wall = Color(0xFFD8C29A)
    val WallB = Color(0xFFC4A97E)
    val Trim = Color(0xFF8A6A44)
    val Thorn = Color(0xFF5A4030)
    val Pot = Color(0xFF6A4030)
    val Broth = Color(0xFFD89A4A)
    val Steam = Color(0xFFF4F0E8)
    val Beam = Color(0xFF8A5A2E)
    val Person = Color(0xFF3A3040)
    val Plate = Color(0xFFF2EADA)
}

/**
 * Beats: counsel (a cool white light comes down beside the warm light ﷺ again and again; the houses of the alley
 * glow), harm (thorns are thrown over a shared wall; the house next door goes dark), nearest (three doors; a plate
 * travels to the nearest), broth (a pot on the fire, water poured in, bowls carried to the doors), beam (a beam of
 * wood is set into the neighbour's wall and both roofs share it).
 */
internal fun DrawScope.neighborsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "counsel" to Shot(Offset(w * 0.5f, h * 0.4f), 1.05f),
        "harm" to Shot(Offset(w * 0.5f, h * 0.55f), 1.3f),
        "nearest" to Shot(Offset(w * 0.45f, h * 0.6f), 1.25f),
        "broth" to Shot(Offset(w * 0.4f, h * 0.6f), 1.3f),
        "beam" to Shot(Offset(w * 0.5f, h * 0.45f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { alley(a) }
}

private fun DrawScope.alley(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cs = a.b("counsel")
    val hm = a.b("harm")
    val nr = a.b("nearest")
    val br = a.b("broth")
    val bm = a.b("beam")
    val ground = h * 0.84f
    daySky12(AlleyC46.Sky, AlleyC46.SkyLow, ground, AlleyC46.Ground)
    // the counsel, repeated: a cool light comes to the warm light, and the alley's houses glow
    if (cs > 0f) {
        val f = easeOut(seg(cs, 0f, BEAT_IN)) * (1f - seg(hm, 0f, BEAT_OUT))
        val light = Offset(w * 0.62f, h * 0.22f)
        sirajMunir(light, w * 0.016f, f, t, nameAt = light + Offset(0f, -h * 0.12f))
        for (k in 0 until 3) {
            val u = fract(cs * 2.2f + k / 3f)
            val c = Offset(w * (0.25f + 0.3f * u), h * (0.1f + 0.08f * u))
            angelLight26(c, w * 0.012f, t, f * bump(u) * seg(cs, 0.1f, 0.25f))
        }
        val glow = seg(cs, 0.4f, 0.9f)
        for (k in 0 until 5) house12(Offset(w * (0.12f + 0.19f * k), ground), w * 0.15f, if (k % 2 == 0) AlleyC46.Wall else AlleyC46.WallB, AlleyC46.Trim, f, glow * seg(cs, 0.4f + 0.1f * k, 0.6f + 0.1f * k))
    }
    // thorns over the wall
    if (hm > 0f) {
        val f = easeOut(seg(hm, 0f, BEAT_IN)) * (1f - seg(nr, 0f, BEAT_OUT))
        house12(Offset(w * 0.3f, ground), w * 0.26f, AlleyC46.Wall, AlleyC46.Trim, f, 0.2f)
        val dark = seg(hm, 0.55f, 0.9f)
        house12(Offset(w * 0.7f, ground), w * 0.26f, lerpColor(AlleyC46.WallB, Color(0xFF6A5A4A), dark), AlleyC46.Trim, f, 0.8f * (1f - dark))
        drawRect(AlleyC46.Trim, Offset(w * 0.49f, ground - h * 0.3f), Size(w * 0.02f, h * 0.3f), alpha = f)
        for (k in 0 until 5) {
            val u = seg(hm, 0.15f + 0.08f * k, 0.5f + 0.08f * k)
            if (u <= 0f) continue
            val p = Offset(w * (0.42f + 0.22f * u), ground - h * (0.32f + 0.1f * sin(PI.toFloat() * u)) + h * 0.3f * u * u)
            rotate(360f * u, p) {
                for (j in 0 until 5) drawLine(AlleyC46.Thorn, p, p + Offset(cos(j * 1.26f), sin(j * 1.26f)) * (w * 0.03f), 2.6f * dp, StrokeCap.Round, alpha = f)
            }
        }
        nameRoundel(Offset(w * 0.82f, ground - h * 0.06f + h * 0.008f * sin(t * 14f) * dark), w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        tagAt(a.tag("harm", 0, 0), Offset(w * 0.54f, ground - h * 0.5f), f * seg(hm, 0.2f, 0.4f), back = Color(0xE6201810), ink = Color(0xFFF0A090))
        tagAt(a.tag("harm", 0, 1), Offset(w * 0.3f, ground - h * 0.56f), f * seg(hm, 0.6f, 0.8f), back = Color(0xE6201810), ink = Color(0xFFF0A090))
        tagAt(a.tag("harm", 0, 2), Offset(w * 0.82f, ground + h * 0.07f), f * seg(hm, 0.5f, 0.7f), back = Color(0xE6201810), ink = Color(0xFFF0ECE4))
    }
    // three doors; the gift goes to the nearest
    if (nr > 0f) {
        val f = easeOut(seg(nr, 0f, BEAT_IN)) * (1f - seg(br, 0f, 0.4f))
        house12(Offset(w * 0.12f, ground), w * 0.2f, AlleyC46.Wall, AlleyC46.Trim, f, 0.5f)
        for (k in 0 until 3) {
            val x = w * (0.4f + 0.2f * k)
            house12(Offset(x, ground), w * 0.16f, if (k % 2 == 0) AlleyC46.WallB else AlleyC46.Wall, AlleyC46.Trim, f, if (k == 0) seg(nr, 0.65f, 0.9f) else 0.1f)
        }
        val go = easeInOut(seg(nr, 0.2f, 0.65f))
        val from = Offset(w * 0.16f, ground - h * 0.02f)
        val to = Offset(w * 0.35f, ground - h * 0.02f)
        val p = from + (to - from) * go
        figure(p, h * 0.2f, AlleyC46.Person, f, walk = t * 6f * (1f - seg(nr, 0.6f, 0.65f)), arms = 0.6f)
        drawOval(AlleyC46.Plate, Offset(p.x + w * 0.02f, p.y - h * 0.15f), Size(w * 0.07f, h * 0.03f), alpha = f)
        softGlow(Offset(p.x + w * 0.055f, p.y - h * 0.15f), w * 0.03f, ArtInk.Gold, 0.5f * f)
        softGlow(Offset(w * 0.4f, ground - h * 0.05f), w * 0.07f, ArtInk.Gold, 0.6f * f * seg(nr, 0.5f, 0.8f))
        tagAt(a.tag("nearest", 0, 0), Offset(w * 0.4f, ground - h * 0.36f), f * seg(nr, 0.5f, 0.75f), back = Color(0xE6201810), ink = ArtInk.Gold)
        tagAt(a.tag("nearest", 0, 1), p + Offset(w * 0.05f, -h * 0.24f), f * seg(nr, 0.1f, 0.3f), back = Color(0xE6201810), ink = Color(0xFFF0ECE4))
    }
    // the broth with more water
    if (br > 0f) {
        val f = easeOut(seg(br, 0f, 0.3f)) * (1f - seg(bm, 0f, BEAT_OUT))
        val pot = Offset(w * 0.3f, ground - h * 0.06f)
        for (k in 0 until 4) {
            val u = fract(t * 1.3f + k / 4f)
            drawCircle(AlleyC46.Broth.copy(red = 1f), w * 0.01f * (1f - u), Offset(pot.x + w * 0.02f * (k - 1.5f), ground - h * 0.01f - h * 0.02f * u), alpha = f * 0.8f)
        }
        drawPath(Path().apply {
            moveTo(pot.x - w * 0.08f, pot.y - h * 0.1f); lineTo(pot.x + w * 0.08f, pot.y - h * 0.1f)
            quadraticTo(pot.x + w * 0.09f, pot.y + h * 0.04f, pot.x, pot.y + h * 0.04f)
            quadraticTo(pot.x - w * 0.09f, pot.y + h * 0.04f, pot.x - w * 0.08f, pot.y - h * 0.1f); close()
        }, AlleyC46.Pot, alpha = f)
        val level = 0.3f + 0.5f * seg(br, 0.15f, 0.45f)
        drawOval(AlleyC46.Broth, Offset(pot.x - w * 0.075f, pot.y - h * 0.1f - h * 0.01f * level), Size(w * 0.15f, h * 0.025f), alpha = f)
        // water poured in
        if (br in 0.12f..0.5f) for (k in 0 until 6) drawCircle(Color(0xFFBFE0F0), 1.6f * dp, Offset(pot.x + w * 0.02f, pot.y - h * 0.25f + h * 0.15f * fract(t * 2f + k / 6f)), alpha = f)
        for (k in 0 until 3) {
            val u = fract(t * 0.15f + k * 0.33f)
            drawCircle(AlleyC46.Steam, w * 0.02f * (0.5f + u), pot + Offset(w * 0.02f * sin(t + k), -h * (0.14f + 0.12f * u)), alpha = f * 0.35f * (1f - u))
        }
        for (k in 0 until 3) {
            val x = w * (0.55f + 0.16f * k)
            house12(Offset(x, ground), w * 0.13f, AlleyC46.Wall, AlleyC46.Trim, f, seg(br, 0.6f + 0.1f * k, 0.8f + 0.1f * k))
            val g = easeOut(seg(br, 0.5f + 0.1f * k, 0.65f + 0.1f * k))
            cup12(Offset(x - w * 0.04f, ground - h * 0.005f), w * 0.035f, AlleyC46.Plate, f * g, AlleyC46.Broth)
        }
    }
    // a beam set in the neighbour's wall
    if (bm > 0f) {
        val f = easeOut(seg(bm, 0f, 0.3f))
        house12(Offset(w * 0.3f, ground), w * 0.32f, AlleyC46.Wall, AlleyC46.Trim, f, 0.4f)
        house12(Offset(w * 0.7f, ground), w * 0.32f, AlleyC46.WallB, AlleyC46.Trim, f, 0.4f)
        val slide = easeInOut(seg(bm, 0.2f, 0.7f))
        val y = ground - h * 0.24f
        drawRect(AlleyC46.Beam, Offset(w * (0.18f + 0.2f * slide), y), Size(w * 0.24f, h * 0.035f), alpha = f)
        if (slide > 0.95f) softGlow(Offset(w * 0.6f, y + h * 0.017f), w * 0.04f, ArtInk.Gold, 0.5f * f * seg(bm, 0.7f, 0.9f))
        figure(Offset(w * 0.2f, ground), h * 0.2f, AlleyC46.Person, f, arms = -0.4f)
        figure(Offset(w * 0.82f, ground), h * 0.2f, AlleyC46.Person, f, arms = 0.5f * seg(bm, 0.6f, 0.9f))
    }
}

// ═════════════ 4. the sponsor of the orphan (riyad-yatim) ═════════════

private object OrphanC46 {
    val Sky = Color(0xFF1E2A44)
    val SkyLow = Color(0xFFE2A88A)
    val Ground = Color(0xFF34402E)
    val Leaf = Color(0xFF2E4A30)
    val River = Color(0xFF6AA6C4)
    val Person = Color(0xFFE6DCC8)
    val Child = Color(0xFFF2E8D2)
    val Wall = Color(0xFF8A7A6A)
    val Trim = Color(0xFF5A4A3A)
    val Root = Color(0xFFF2C46A)
    val Trunk = Color(0xFF6A4A32)
    val Fruit = Color(0xFFE88A4A)
    val Bread = Color(0xFFD8A65A)
}

/**
 * Beats: together (a garden at dawn: the light ﷺ, and close beside it a figure holding a child by the hand),
 * threeranks (a figure carries a basket to a widow's door; three tags light: «كَالْمُجَاهِدِ فِي سَبِيلِ اللَّهِ»,
 * «وَكَالْقَائِمِ الَّذِي لَا يَفْتُرُ», «وَكَالصَّائِمِ الَّذِي لَا يُفْطِرُ»), weakroots (a tree whose roots are small
 * lights: «بِضُعَفَائِهَا»; its crown fills with fruit), twodaughters (two girls grow up beside a father; then two lights
 * stand close together), quietpoor (a line at a bread stall, and apart from it one who sits at home unseen; a soft
 * light finds him).
 */
internal fun DrawScope.orphangardenScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "together" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "threeranks" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "weakroots" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "twodaughters" to Shot(Offset(w * 0.45f, h * 0.55f), 1.25f),
        "quietpoor" to Shot(Offset(w * 0.6f, h * 0.6f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { orphanDawn(a) }
}

private fun DrawScope.orphanDawn(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tg = a.b("together")
    val tr = a.b("threeranks")
    val wr = a.b("weakroots")
    val td = a.b("twodaughters")
    val qp = a.b("quietpoor")
    val ground = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(OrphanC46.Sky, OrphanC46.SkyLow), 0f, ground))
    drawRect(OrphanC46.Ground, Offset(0f, ground), Size(w, h - ground))
    // a garden with a river beneath
    if (tg > 0f) {
        val f = easeOut(seg(tg, 0f, BEAT_IN)) * (1f - seg(tr, 0f, BEAT_OUT))
        for (k in 0 until 5) palm12(Offset(w * (0.06f + 0.22f * k), ground), h * (0.36f + 0.06f * (k % 2)), OrphanC46.Trunk, OrphanC46.Leaf, f, t + k)
        val y = ground + h * 0.08f
        drawRect(OrphanC46.River, Offset(0f, y), Size(w, h * 0.05f), alpha = f * 0.8f)
        for (k in 0 until 6) drawLine(Color.White, Offset(w * fract(k / 6f + t * 0.03f), y + h * 0.02f), Offset(w * fract(k / 6f + t * 0.03f) + w * 0.04f, y + h * 0.02f), 1.2f * dp, alpha = f * 0.4f)
        val light = Offset(w * 0.42f, h * 0.36f)
        sirajMunir(light, w * 0.016f, f * seg(tg, 0.1f, 0.4f), t, nameAt = light + Offset(0f, -h * 0.13f))
        val near = easeInOut(seg(tg, 0.35f, 0.8f))
        val p = Offset(w * (0.8f - 0.24f * near), ground)
        figure(p, h * 0.24f, OrphanC46.Person, f, walk = t * 5f * (1f - seg(tg, 0.75f, 0.8f)), arms = -0.3f)
        child12(p + Offset(w * 0.06f, 0f), h * 0.24f, OrphanC46.Child, f, walk = t * 5f * (1f - seg(tg, 0.75f, 0.8f)), arms = 0.6f)
    }
    // the one who looks after the widow and the poor
    if (tr > 0f) {
        val f = easeOut(seg(tr, 0f, BEAT_IN)) * (1f - seg(wr, 0f, BEAT_OUT))
        house12(Offset(w * 0.78f, ground), w * 0.2f, OrphanC46.Wall, OrphanC46.Trim, f, seg(tr, 0.4f, 0.6f))
        woman12(Offset(w * 0.64f, ground), h * 0.2f, OrphanC46.Child, f * seg(tr, 0.35f, 0.5f))
        val go = easeInOut(seg(tr, 0.05f, 0.4f))
        val p = Offset(w * (0.12f + 0.4f * go), ground)
        figure(p, h * 0.22f, OrphanC46.Person, f, walk = t * 6f * (1f - seg(tr, 0.38f, 0.42f)), arms = 0.6f)
        cup12(p + Offset(-w * 0.05f, -h * 0.1f), w * 0.05f, OrphanC46.Bread, f)
        for (k in 0 until 3) tagAt(a.tag("threeranks", 0, k), Offset(w * (0.22f + 0.28f * k), h * (0.2f + 0.06f * (k % 2))), f * seg(tr, 0.45f + 0.15f * k, 0.6f + 0.15f * k), back = Back46, ink = ArtInk.Gold)
    }
    // a tree fed by small lights
    if (wr > 0f) {
        val f = easeOut(seg(wr, 0f, BEAT_IN)) * (1f - seg(td, 0f, BEAT_OUT))
        val base = Offset(w * 0.5f, ground)
        for (k in 0 until 9) {
            val x = w * (0.3f + 0.05f * k)
            val y = ground + h * (0.05f + 0.03f * (k % 3))
            val g = seg(wr, 0.05f + 0.04f * k, 0.2f + 0.04f * k)
            drawLine(OrphanC46.Root, Offset(x, y), base, 1.3f * dp, StrokeCap.Round, alpha = f * g * 0.6f)
            softGlow(Offset(x, y), w * 0.015f, OrphanC46.Root, 0.9f * f * g)
        }
        val grow = easeOut(seg(wr, 0.3f, 0.7f))
        drawLine(OrphanC46.Trunk, base, base + Offset(0f, -h * 0.32f * grow), w * 0.03f, StrokeCap.Round, alpha = f)
        drawCircle(OrphanC46.Leaf, w * 0.16f * grow, base + Offset(0f, -h * 0.42f * grow), alpha = f)
        for (k in 0 until 8) {
            val g = seg(wr, 0.6f + 0.04f * k, 0.75f + 0.04f * k)
            val ang = k * 0.8f
            drawCircle(OrphanC46.Fruit, w * 0.012f, base + Offset(cos(ang) * w * 0.1f, -h * 0.42f + sin(ang) * h * 0.12f), alpha = f * g)
        }
        tagAt(a.tag("weakroots"), Offset(w * 0.5f, h * 0.95f), f * seg(wr, 0.2f, 0.4f), back = Back46, ink = ArtInk.Gold)
    }
    // two daughters raised, then two lights side by side
    if (td > 0f) {
        val f = easeOut(seg(td, 0f, BEAT_IN)) * (1f - seg(qp, 0f, BEAT_OUT))
        val grow = easeInOut(seg(td, 0.1f, 0.6f))
        figure(Offset(w * 0.3f, ground), h * 0.26f, OrphanC46.Person, f * (1f - seg(td, 0.65f, 0.8f)))
        for (k in 0 until 2) woman12(Offset(w * (0.42f + 0.1f * k), ground), h * (0.1f + 0.1f * grow + 0.02f * k), OrphanC46.Child, f * (1f - seg(td, 0.65f, 0.8f)))
        val two = seg(td, 0.7f, 0.95f)
        val c = Offset(w * 0.5f, h * 0.38f)
        sirajMunir(c, w * 0.016f, f * two, t, nameAt = c + Offset(0f, -h * 0.13f))
        bloom(c + Offset(w * 0.06f, h * 0.01f), w * 0.012f, ArtInk.Ivory, f * two)
    }
    // the poor who does not ask
    if (qp > 0f) {
        val f = easeOut(seg(qp, 0f, 0.3f))
        drawRect(OrphanC46.Trim, Offset(w * 0.08f, ground - h * 0.18f), Size(w * 0.16f, h * 0.04f), alpha = f)
        for (k in 0 until 3) drawCircle(OrphanC46.Bread, w * 0.015f, Offset(w * (0.11f + 0.05f * k), ground - h * 0.2f), alpha = f)
        for (k in 0 until 4) figure(Offset(w * (0.3f + 0.07f * k), ground), h * 0.22f, OrphanC46.Person, f * 0.8f, arms = if (k == 0) 0.8f else 0f)
        house12(Offset(w * 0.8f, ground), w * 0.2f, OrphanC46.Wall, OrphanC46.Trim, f, 0f)
        sit12(Offset(w * 0.8f, ground - h * 0.01f), h * 0.12f, OrphanC46.Child, f * seg(qp, 0.4f, 0.6f), -1f)
        softGlow(Offset(w * 0.8f, ground - h * 0.08f), w * 0.1f, ArtInk.Gold, 0.55f * f * seg(qp, 0.5f, 0.85f))
    }
}

// ═════════════ 5. the home in the notebook (riyad-ahl) ═════════════

/**
 * Beats: bestrank (a ladder of ranks drawn in ink; at its top a house, and a star beside it), sharetable (two plates
 * and two robes: an equals sign between his and hers), weighflaw (a balance: one pan holds a small grey trait, the
 * other fills with good ones and comes down), homelight (a warm light ﷺ inside a small house; the call comes and the
 * light goes out to the mosque), firstdinar (four coins in a row; the one spent on the family grows brightest:
 * «عَلَى أَهْلِكَ»).
 */
internal fun DrawScope.homeledgerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "bestrank" to Shot(Offset(w * 0.5f, h * 0.45f), 1.15f),
        "sharetable" to Shot(Offset(w * 0.5f, h * 0.5f), 1.25f),
        "weighflaw" to Shot(Offset(w * 0.5f, h * 0.5f), 1.25f),
        "homelight" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "firstdinar" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0f) { homePage(a) }
}

private fun DrawScope.homePage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pp = Paper(a.dark)
    paperSheet(pp)
    val br = a.b("bestrank")
    val st = a.b("sharetable")
    val wf = a.b("weighflaw")
    val hl = a.b("homelight")
    val fd = a.b("firstdinar")
    val pen = 2.2f * dp
    // a ladder of ranks; the house at the top
    if (br > 0f) {
        val f = easeOut(seg(br, 0f, 0.25f)) * (1f - seg(st, 0f, BEAT_OUT))
        val x0 = w * 0.38f
        val x1 = w * 0.5f
        penLine(listOf(Offset(x0, h * 0.86f), Offset(x0, h * 0.2f)), seg(br, 0.05f, 0.4f), pp.ink, pen, f)
        penLine(listOf(Offset(x1, h * 0.86f), Offset(x1, h * 0.2f)), seg(br, 0.1f, 0.45f), pp.ink, pen, f)
        for (k in 0 until 6) {
            val y = h * (0.8f - 0.11f * k)
            penLine(listOf(Offset(x0, y), Offset(x1, y)), seg(br, 0.2f + 0.05f * k, 0.3f + 0.05f * k), pp.ink, pen * 0.8f, f)
        }
        val top = Offset(w * 0.62f, h * 0.24f)
        val g = seg(br, 0.55f, 0.85f)
        penLine(listOf(top + Offset(-w * 0.06f, h * 0.06f), top + Offset(-w * 0.06f, -h * 0.02f), top + Offset(0f, -h * 0.08f), top + Offset(w * 0.06f, -h * 0.02f), top + Offset(w * 0.06f, h * 0.06f), top + Offset(-w * 0.06f, h * 0.06f)), g, pp.amber, pen, f)
        penLine(listOf(top + Offset(-w * 0.012f, h * 0.06f), top + Offset(-w * 0.012f, h * 0.02f), top + Offset(w * 0.012f, h * 0.02f), top + Offset(w * 0.012f, h * 0.06f)), seg(br, 0.75f, 0.9f), pp.amber, pen, f)
        drawPath(starPath(top + Offset(w * 0.11f, -h * 0.06f), w * 0.02f), pp.amber, alpha = f * seg(br, 0.85f, 1f))
        arrowHead(Offset(x1 + w * 0.02f, h * 0.24f), Offset(1f, 0f), w * 0.015f, pp.amber, pen, f * g)
        note(a.tag("bestrank", 0, 0), Offset(w * 0.24f, h * 0.55f), f * seg(br, 0.3f, 0.5f), pp, pp.ink)
        note(a.tag("bestrank", 0, 1), Offset(w * 0.44f, h * 0.13f), f * seg(br, 0.55f, 0.75f), pp, pp.good)
    }
    // his plate and hers, his robe and hers
    if (st > 0f) {
        val f = easeOut(seg(st, 0f, BEAT_IN)) * (1f - seg(wf, 0f, BEAT_OUT))
        for (row in 0 until 2) {
            val y = h * (0.36f + 0.3f * row)
            for (side in 0 until 2) {
                val c = Offset(w * (0.32f + 0.36f * side), y)
                val g = seg(st, 0.1f + 0.3f * row + 0.1f * side, 0.3f + 0.3f * row + 0.1f * side)
                if (row == 0) {
                    penLine(listOf(c + Offset(-w * 0.07f, 0f), c + Offset(-w * 0.04f, h * 0.05f), c + Offset(w * 0.04f, h * 0.05f), c + Offset(w * 0.07f, 0f)), g, pp.ink, pen, f)
                    penLine(listOf(c + Offset(-w * 0.075f, 0f), c + Offset(w * 0.075f, 0f)), g, pp.ink, pen, f)
                } else {
                    penLine(listOf(c + Offset(-w * 0.03f, -h * 0.08f), c + Offset(-w * 0.06f, -h * 0.04f), c + Offset(-w * 0.04f, h * 0.1f), c + Offset(w * 0.04f, h * 0.1f), c + Offset(w * 0.06f, -h * 0.04f), c + Offset(w * 0.03f, -h * 0.08f), c + Offset(-w * 0.03f, -h * 0.08f)), g, pp.ink, pen, f)
                }
            }
            val e = seg(st, 0.3f + 0.3f * row, 0.45f + 0.3f * row)
            penLine(listOf(Offset(w * 0.46f, y - h * 0.015f), Offset(w * 0.54f, y - h * 0.015f)), e, pp.amber, pen * 1.2f, f)
            penLine(listOf(Offset(w * 0.46f, y + h * 0.015f), Offset(w * 0.54f, y + h * 0.015f)), e, pp.amber, pen * 1.2f, f)
        }
    }
    // a small flaw in one pan, many good traits in the other
    if (wf > 0f) {
        val f = easeOut(seg(wf, 0f, BEAT_IN)) * (1f - seg(hl, 0f, BEAT_OUT))
        val tilt = -0.9f * easeInOut(seg(wf, 0.35f, 0.8f))
        val foot = Offset(w * 0.5f, h * 0.88f)
        val s = h * 0.62f
        balance12(foot, s, tilt, pp.ink, f, pp.faint, pp.good)
        val (lp, rp) = balancePans12(foot, s, tilt)
        drawCircle(pp.faint, w * 0.016f, rp + Offset(0f, -h * 0.03f), alpha = f * seg(wf, 0.1f, 0.25f))
        for (k in 0 until 4) drawCircle(pp.good, w * 0.014f, lp + Offset(w * (0.022f * k - 0.033f), -h * 0.03f - h * 0.02f * (k % 2)), alpha = f * seg(wf, 0.25f + 0.08f * k, 0.4f + 0.08f * k))
        tagAt(a.tag("weighflaw", 0, 0), rp + Offset(0f, h * 0.1f), f * seg(wf, 0.15f, 0.3f), back = pp.back, ink = pp.bad)
        tagAt(a.tag("weighflaw", 0, 1), lp + Offset(0f, h * 0.1f), f * seg(wf, 0.5f, 0.7f), back = pp.back, ink = pp.good)
    }
    // at home in the service of his family, then out to the prayer
    if (hl > 0f) {
        val f = easeOut(seg(hl, 0f, BEAT_IN)) * (1f - seg(fd, 0f, BEAT_OUT))
        val house = Offset(w * 0.32f, h * 0.7f)
        penLine(listOf(house + Offset(-w * 0.12f, 0f), house + Offset(-w * 0.12f, -h * 0.26f), house + Offset(w * 0.12f, -h * 0.26f), house + Offset(w * 0.12f, 0f), house + Offset(-w * 0.12f, 0f)), seg(hl, 0.05f, 0.3f), pp.ink, pen, f)
        val m = Offset(w * 0.74f, h * 0.7f)
        penLine(listOf(m + Offset(-w * 0.1f, 0f), m + Offset(-w * 0.1f, -h * 0.2f), m + Offset(w * 0.1f, -h * 0.2f), m + Offset(w * 0.1f, 0f)), seg(hl, 0.15f, 0.4f), pp.ink, pen, f)
        drawArc(pp.ink, 180f, 180f, false, Offset(m.x - w * 0.07f, m.y - h * 0.32f), Size(w * 0.14f, h * 0.24f), alpha = f * seg(hl, 0.3f, 0.45f), style = Stroke(pen))
        val out = easeInOut(seg(hl, 0.55f, 0.9f))
        val c = house + (m - house) * out + Offset(0f, -h * 0.12f)
        sirajMunir(c, w * 0.012f, f * seg(hl, 0.1f, 0.3f), t, nameAt = Offset(c.x, h * 0.2f))
        // the call to prayer: rings from the minaret
        for (k in 0 until 3) {
            val u = fract(t * 0.6f + k / 3f)
            drawCircle(pp.amber, w * 0.03f + w * 0.08f * u, m + Offset(0f, -h * 0.3f), alpha = f * seg(hl, 0.4f, 0.55f) * (1f - u) * 0.5f, style = Stroke(1.2f * dp))
        }
        note(a.tag("homelight"), house + Offset(0f, -h * 0.34f), f * seg(hl, 0.3f, 0.5f), pp, pp.good)
    }
    // the dinar spent on the family
    if (fd > 0f) {
        val f = easeOut(seg(fd, 0f, 0.25f))
        for (k in 0 until 4) {
            val c = Offset(w * (0.2f + 0.2f * k), h * 0.5f)
            val best = k == 3
            val g = seg(fd, 0.1f + 0.1f * k, 0.25f + 0.1f * k)
            val r = w * (if (best) 0.035f + 0.025f * seg(fd, 0.55f, 0.85f) else 0.035f)
            if (best) softGlow(c, r * 2.2f, pp.amber, 0.5f * f * seg(fd, 0.55f, 0.85f))
            coin12(c, r, f * g, if (best) Color(0xFFF2C45A) else pp.faint)
        }
        tagAt(a.tag("firstdinar"), Offset(w * 0.8f, h * 0.68f), f * seg(fd, 0.6f, 0.8f), back = pp.back, ink = pp.amber)
    }
}
