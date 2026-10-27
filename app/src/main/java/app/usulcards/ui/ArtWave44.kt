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
 * Wave 44 (2026-10-27): «بصائر»، الجزء الثالث، «تشريع ورحمة». A court that turns the confessor away and a wall that guards
 * the town; a house left almost empty and four laden camels given away; rooms beside the mosque from which the Sunnah
 * shines out; a disc lit three quarters; a shield over a house and a lock that only she holds; a ledger, a cradle and a
 * share that flows out to others; two houses kept level. No figures: these pages are about people's rights, so they are
 * drawn as homes, purses, scales and lights. He ﷺ is only light; the Mothers of the Believers and the companions are
 * their names in roundels.
 */

private val Back44 = Color(0xE6181420)

/** A small house with a pitched roof, foot at [foot], [s] wide. */
private fun DrawScope.house44(foot: Offset, s: Float, wall: Color, roof: Color, alpha: Float, lit: Float = 0f) {
    if (alpha <= 0.01f) return
    drawRect(wall, Offset(foot.x - s / 2f, foot.y - s * 0.7f), Size(s, s * 0.7f), alpha = alpha)
    drawPath(Path().apply { moveTo(foot.x - s * 0.6f, foot.y - s * 0.7f); lineTo(foot.x, foot.y - s * 1.05f); lineTo(foot.x + s * 0.6f, foot.y - s * 0.7f); close() }, roof, alpha = alpha)
    drawRect(lerpColor(Color(0xFF3A2E28), Color(0xFFFFE0A0), lit), Offset(foot.x - s * 0.12f, foot.y - s * 0.45f), Size(s * 0.24f, s * 0.2f), alpha = alpha)
    if (lit > 0f) softGlow(Offset(foot.x, foot.y - s * 0.35f), s * 0.5f, ArtInk.Gold, 0.3f * lit * alpha)
}

// ═════════════ 1. a court that turns the confessor away (bs-hudud) ═════════════

private object CourtC44 {
    val Sky = Color(0xFF1A1C30)
    val SkyLow = Color(0xFF36304A)
    val Ground = Color(0xFF3A3236)
    val Wall = Color(0xFF8A7A66)
    val Wall2 = Color(0xFFD8C8A8)
    val Roof = Color(0xFF8A5A3A)
    val Fruit = Color(0xFFE8A04A)
    val Leaf = Color(0xFF4A7A4A)
    val Coin = Color(0xFFD8B04A)
    val Eye = Color(0xFFF2EEE4)
    val Veil = Color(0xFFFFF0D8)
}

/**
 * Beats: exemptions (a fruit tree and three coins, each with a tick: no hand cut here — «الْخَائِنِ», «الْمُنْتَهِبِ»,
 * «الْمُخْتَلِسِ»), turnaway (a court gate; one comes to it to confess, and is gently turned back), safewall (a wall round the
 * sleeping town, its windows lit: the aim is the people's safety), fourwitness (four open eyes in a row, all needed:
 * near impossible), veil (a soft veil of light falls over a house: cover yourself with Allah's cover and repent).
 */
internal fun DrawScope.courtgateScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "exemptions" to Shot(Offset(w * 0.3f, h * 0.5f), 1.2f),
        "turnaway" to Shot(Offset(w * 0.68f, h * 0.55f), 1.25f),
        "safewall" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
        "fourwitness" to Shot(Offset(w * 0.5f, h * 0.25f), 1.3f),
        "coverveil" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { courtNight(a) }
}

private fun DrawScope.courtNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ex = a.b("exemptions")
    val ta = a.b("turnaway")
    val sw = a.b("safewall")
    val fw = a.b("fourwitness")
    val ve = a.b("coverveil")
    val ground = h * 0.8f
    nightSky(t, CourtC44.Sky, CourtC44.SkyLow, ground, starAlpha = 0.6f)
    drawRect(CourtC44.Ground, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - 0.7f * seg(sw, 0f, 0.4f)
    // what is not cut for
    if (ex > 0f) {
        val f = easeOut(seg(ex, 0f, 0.3f)) * away
        val tree = Offset(w * 0.16f, ground)
        drawLine(Color(0xFF5A4030), tree, tree + Offset(0f, -h * 0.2f), w * 0.02f, alpha = f)
        drawCircle(CourtC44.Leaf, w * 0.08f, tree + Offset(0f, -h * 0.26f), alpha = f)
        for (k in 0 until 5) drawCircle(CourtC44.Fruit, w * 0.012f, tree + Offset(w * 0.05f * cos(k * 1.3f), -h * 0.26f + h * 0.05f * sin(k * 1.7f)), alpha = f)
        check21(tree + Offset(w * 0.1f, -h * 0.34f), w * 0.02f, ArtInk.Jade, seg(ex, 0.2f, 0.4f) * f, 3f * dp)
        for (k in 0 until 3) {
            val c = Offset(w * (0.3f + 0.14f * k), ground - h * (0.12f + 0.06f * (k % 2)))
            val g = seg(ex, 0.3f + 0.15f * k, 0.5f + 0.15f * k)
            drawCircle(CourtC44.Coin, w * 0.025f, c, alpha = f * g)
            check21(c + Offset(w * 0.035f, -h * 0.03f), w * 0.012f, ArtInk.Jade, g * f, 2.5f * dp)
            tagAt(a.tag("exemptions", 0, k), c + Offset(0f, -h * (0.07f + 0.08f * (k % 2))), g * away * (1f - seg(ta, 0f, 0.3f)), back = Back44, ink = ArtInk.Ivory)
        }
    }
    // the court gate turns the confessor back
    if (ta > 0f) {
        val f = easeOut(seg(ta, 0f, 0.3f)) * away
        val gate = Offset(w * 0.75f, ground)
        drawRect(CourtC44.Wall, Offset(gate.x - w * 0.12f, gate.y - h * 0.3f), Size(w * 0.24f, h * 0.3f), alpha = f)
        drawPath(archPath26(gate.x, gate.y - h * 0.18f, gate.y, w * 0.05f), Color(0xFF2A2228), alpha = f)
        // a balance over the gate
        val bc = gate + Offset(0f, -h * 0.36f)
        drawLine(ArtInk.Brass, bc + Offset(-w * 0.06f, 0f), bc + Offset(w * 0.06f, 0f), 2f * dp, alpha = f)
        for (s in listOf(-1f, 1f)) drawArc(ArtInk.Brass, 0f, 180f, true, Offset(bc.x + s * w * 0.06f - w * 0.02f, bc.y + h * 0.01f), Size(w * 0.04f, h * 0.02f), alpha = f)
        val come = easeInOut(seg(ta, 0.1f, 0.5f))
        val back = easeInOut(seg(ta, 0.55f, 0.95f))
        val x = w * (0.45f + 0.23f * come - 0.25f * back)
        val p = Offset(x, ground - h * 0.05f - h * 0.06f * bump(back))
        drawCircle(Color(0xFFE8DCC8), w * 0.015f, p, alpha = f)
        if (back > 0f) {
            drawArc(ArtInk.BrassLight, 200f, 140f, false, Offset(w * 0.5f, ground - h * 0.18f), Size(w * 0.2f, h * 0.14f), alpha = f * 0.7f, style = Stroke(1.5f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))))
        }
    }
    // the wall round the sleeping town
    if (sw > 0f) {
        val f = easeOut(seg(sw, 0f, 0.35f)) * (1f - 0.5f * seg(fw, 0f, 0.4f))
        for (k in 0 until 5) house44(Offset(w * (0.2f + 0.15f * k), ground), w * 0.1f, CourtC44.Wall2, CourtC44.Roof, f, lit = seg(sw, 0.3f + 0.1f * k, 0.5f + 0.1f * k))
        val run = easeInOut(seg(sw, 0.1f, 0.7f))
        drawLine(CourtC44.Wall, Offset(w * 0.08f, ground - h * 0.02f), Offset(w * (0.08f + 0.84f * run), ground - h * 0.02f), h * 0.04f, alpha = f)
        for (k in 0 until 12) {
            val x = w * (0.08f + 0.075f * k)
            if (x <= w * (0.08f + 0.84f * run)) drawRect(CourtC44.Wall, Offset(x, ground - h * 0.07f), Size(w * 0.035f, h * 0.03f), alpha = f)
        }
    }
    // four eyes that all must see
    if (fw > 0f) {
        val f = easeOut(seg(fw, 0f, 0.3f)) * (1f - 0.5f * seg(ve, 0f, 0.4f))
        for (k in 0 until 4) {
            val c = Offset(w * (0.26f + 0.16f * k), h * 0.24f)
            val open = easeInOut(seg(fw, 0.1f + 0.12f * k, 0.35f + 0.12f * k))
            drawOval(CourtC44.Eye, Offset(c.x - w * 0.045f, c.y - h * 0.025f * open), Size(w * 0.09f, h * 0.05f * open + 0.5f), alpha = f)
            drawCircle(Color(0xFF3A2A20), h * 0.016f * open, c, alpha = f)
        }
        drawRoundRect(ArtInk.BrassLight, Offset(w * 0.18f, h * 0.17f), Size(w * 0.64f, h * 0.14f), CornerRadius(10f * dp), alpha = f * seg(fw, 0.65f, 0.9f), style = Stroke(1.5f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp))))
    }
    // the veil: cover and repent
    if (ve > 0f) {
        val f = easeOut(seg(ve, 0f, 0.4f))
        val c = Offset(w * 0.5f, ground)
        house44(c, w * 0.16f, CourtC44.Wall2, CourtC44.Roof, f, lit = 0.6f * f)
        val fall = easeInOut(seg(ve, 0.2f, 0.8f))
        val top = h * 0.3f
        val p = Path().apply {
            moveTo(c.x - w * 0.16f, top)
            quadraticTo(c.x, top - h * 0.05f, c.x + w * 0.16f, top)
            lineTo(c.x + w * 0.16f, top + (ground - top) * fall)
            quadraticTo(c.x, top + (ground - top) * fall + h * 0.03f * sin(t * 1.5f), c.x - w * 0.16f, top + (ground - top) * fall)
            close()
        }
        drawPath(p, CourtC44.Veil, alpha = 0.3f * f)
        softGlow(Offset(c.x, (top + ground) / 2f), w * 0.2f, CourtC44.Veil, 0.25f * f * fall)
    }
}

// ═════════════ 2. a house left almost empty (bs-zuhd-nabi) ═════════════

private object EmptyC44 {
    val Wall = Color(0xFFE6DCC6)
    val WallLow = Color(0xFFCFC2A6)
    val Floor = Color(0xFF8A7458)
    val Mule = Color(0xFFE8E4DC)
    val Steel = Color(0xFF9AA4B0)
    val Land = Color(0xFF6A9A5A)
    val Cloth = Color(0xFF8AA0C0)
    val Debt = Color(0xFF6A6070)
    val Camel = Color(0xFFC8A070)
    val Load = Color(0xFF8A5A3A)
    val Coin = Color(0xFFD8B04A)
}

/**
 * Beats: leftlittle (an empty room; only three things appear in it: a white mule, a sword, and a plot of land marked
 * «صَدَقَةٌ»), bilaldebt (a roundel, «بِلَالٌ», hands clothes to the poor and a grey cloud of debt gathers over him),
 * fourcamels (four laden camels kneel; their loads go out one by one, and his light waits at the door until the last is
 * gone), goldaway (coins flow in and straight out again as light).
 */
internal fun DrawScope.emptyhouseScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "leftlittle" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "bilaldebt" to Shot(Offset(w * 0.3f, h * 0.4f), 1.3f),
        "fourcamels" to Shot(Offset(w * 0.55f, h * 0.65f), 1.1f),
        "goldaway" to Shot(Offset(w * 0.5f, h * 0.35f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { emptyRoom(a) }
}

private fun DrawScope.emptyRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ll = a.b("leftlittle")
    val bd = a.b("bilaldebt")
    val fc = a.b("fourcamels")
    val ga = a.b("goldaway")
    val floor = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(EmptyC44.Wall, EmptyC44.WallLow), endY = floor), size = Size(w, floor))
    drawRect(EmptyC44.Floor, Offset(0f, floor), Size(w, h - floor))
    val away = 1f - 0.7f * seg(fc, 0f, 0.4f)
    // three things only
    if (ll > 0f) {
        val f = easeOut(seg(ll, 0f, 0.3f)) * away
        val mule = Offset(w * 0.25f, floor)
        val m = seg(ll, 0.15f, 0.35f) * f
        drawOval(EmptyC44.Mule, Offset(mule.x - w * 0.07f, mule.y - h * 0.14f), Size(w * 0.14f, h * 0.07f), alpha = m)
        for (k in 0 until 4) drawLine(EmptyC44.Mule, Offset(mule.x - w * 0.05f + k * w * 0.033f, mule.y - h * 0.09f), Offset(mule.x - w * 0.05f + k * w * 0.033f, mule.y), w * 0.01f, alpha = m)
        drawLine(EmptyC44.Mule, mule + Offset(-w * 0.06f, -h * 0.12f), mule + Offset(-w * 0.09f, -h * 0.17f), w * 0.022f, StrokeCap.Round, alpha = m)
        drawOval(EmptyC44.Mule, Offset(mule.x - w * 0.125f, mule.y - h * 0.19f), Size(w * 0.055f, h * 0.035f), alpha = m)
        for (e in 0 until 2) drawLine(EmptyC44.Mule, mule + Offset(-w * (0.085f + 0.012f * e), -h * 0.185f), mule + Offset(-w * (0.08f + 0.012f * e), -h * 0.225f), w * 0.008f, StrokeCap.Round, alpha = m)
        drawCircle(Color(0xFF3A3028), w * 0.004f, mule + Offset(-w * 0.11f, -h * 0.175f), alpha = m)
        drawLine(EmptyC44.Mule, mule + Offset(w * 0.07f, -h * 0.12f), mule + Offset(w * 0.085f, -h * 0.07f), w * 0.006f, StrokeCap.Round, alpha = m)
        val sword = Offset(w * 0.5f, floor - h * 0.3f)
        val s = seg(ll, 0.3f, 0.5f) * f
        for (pg in listOf(-1f, 1f)) drawCircle(Color(0xFF6A4A32), w * 0.008f, sword + Offset(pg * w * 0.07f, h * 0.012f), alpha = s)
        drawPath(Path().apply {
            moveTo(sword.x - w * 0.06f, sword.y - h * 0.006f)
            quadraticTo(sword.x + w * 0.06f, sword.y - h * 0.012f, sword.x + w * 0.13f, sword.y - h * 0.03f)
            quadraticTo(sword.x + w * 0.07f, sword.y + h * 0.006f, sword.x - w * 0.06f, sword.y + h * 0.006f)
            close()
        }, EmptyC44.Steel, alpha = s)
        drawLine(ArtInk.Brass, sword + Offset(-w * 0.06f, -h * 0.018f), sword + Offset(-w * 0.06f, h * 0.018f), w * 0.008f, StrokeCap.Round, alpha = s)
        drawLine(Color(0xFF6A4A32), sword + Offset(-w * 0.065f, 0f), sword + Offset(-w * 0.11f, 0f), w * 0.012f, StrokeCap.Round, alpha = s)
        val land = Offset(w * 0.75f, floor - h * 0.03f)
        val lf = seg(ll, 0.45f, 0.65f) * f
        drawRect(EmptyC44.Land, Offset(land.x - w * 0.1f, land.y - h * 0.03f), Size(w * 0.2f, h * 0.05f), alpha = lf)
        tagAt(a.tag("leftlittle"), land + Offset(0f, -h * 0.1f), seg(ll, 0.6f, 0.85f) * away * (1f - seg(bd, 0f, 0.3f)), back = Back44, ink = ArtInk.Gold)
    }
    // Bilal clothes the poor on borrowed money
    if (bd > 0f) {
        val f = easeOut(seg(bd, 0f, 0.3f)) * (1f - seg(fc, 0f, 0.35f))
        val b = Offset(w * 0.3f, h * 0.38f)
        nameRoundel(b, w * 0.05f, a.tag("bilaldebt"), Roundel.Fills[0], Roundel.Rim, f)
        for (k in 0 until 3) {
            val u = easeInOut(seg(bd, 0.2f + 0.15f * k, 0.45f + 0.15f * k))
            val to = Offset(w * (0.55f + 0.1f * k), h * 0.5f)
            val p = b + (to - b) * u
            drawRect(EmptyC44.Cloth, Offset(p.x - w * 0.02f, p.y - h * 0.012f), Size(w * 0.04f, h * 0.024f), alpha = f * seg(bd, 0.2f + 0.15f * k, 0.3f + 0.15f * k))
            drawCircle(Color(0xFF5A4A40), w * 0.012f, to + Offset(0f, h * 0.04f), alpha = f * 0.7f)
        }
        cloud38(b + Offset(0f, -h * 0.12f), w * 0.07f * easeOut(seg(bd, 0.5f, 0.9f)), EmptyC44.Debt, 0.8f * f)
    }
    // four laden camels, given away before he goes in
    if (fc > 0f) {
        val f = easeOut(seg(fc, 0f, 0.3f)) * (1f - 0.5f * seg(ga, 0f, 0.4f))
        val door = Offset(w * 0.12f, floor)
        drawRect(Color(0xFF6A4A32), Offset(door.x - w * 0.05f, door.y - h * 0.22f), Size(w * 0.1f, h * 0.22f), alpha = f)
        for (k in 0 until 4) {
            val foot = Offset(w * (0.36f + 0.16f * k), floor + h * 0.05f)
            camel26(foot, h * 0.14f, EmptyC44.Camel, f, t, 0f)
            val gone = easeInOut(seg(fc, 0.25f + 0.12f * k, 0.45f + 0.12f * k))
            val load = foot + Offset(0f, -h * 0.17f) + Offset(w * 0.3f * gone, -h * 0.3f * gone)
            drawRect(EmptyC44.Load, Offset(load.x - w * 0.03f, load.y - h * 0.02f), Size(w * 0.06f, h * 0.04f), alpha = f * (1f - gone))
            if (gone > 0f) drawCircle(ArtInk.Gold, w * 0.008f, load, alpha = f * bump(gone))
        }
        val inside = easeInOut(seg(fc, 0.85f, 1f))
        val light = Offset(door.x + w * 0.1f * (1f - inside), floor - h * 0.3f)
        sirajMunir(light, w * 0.011f, f, t, nameAt = light + Offset(w * 0.08f, -h * 0.06f))
    }
    // in, and straight out
    if (ga > 0f) {
        val f = easeOut(seg(ga, 0f, 0.3f))
        val c = Offset(w * 0.5f, h * 0.36f)
        softGlow(c, w * 0.07f, ArtInk.Gold, 0.4f * f)
        for (k in 0 until 8) {
            val u = fract(t * 0.4f + k / 8f)
            val inP = Offset(w * 0.1f, h * 0.3f) + (c - Offset(w * 0.1f, h * 0.3f)) * seg(u, 0f, 0.5f)
            val outP = c + (Offset(w * (0.75f + 0.03f * (k % 3)), h * (0.15f + 0.05f * (k % 4))) - c) * seg(u, 0.5f, 1f)
            val p = if (u < 0.5f) inP else outP
            if (u < 0.5f) drawCircle(EmptyC44.Coin, w * 0.01f, p, alpha = f)
            else drawCircle(Color(0xFFFFF0C8), w * 0.007f, p, alpha = f * (1f - u))
        }
    }
}

// ═════════════ 3. rooms by the mosque, the Sunnah shining out (bs-zawjat) ═════════════

private object RoomsC44 {
    val Sky = Color(0xFF1A1C30)
    val SkyLow = Color(0xFF3A3450)
    val Ground = Color(0xFF4A3E36)
    val Mosque = Color(0xFFD8C8A8)
    val Room = Color(0xFFC8B898)
    val Dot = Color(0xFFE8D8B0)
    val Sea = Color(0xFF2E5A80)
    val Chain = Color(0xFF8A8A96)
}

/**
 * Beats: onlyone (his light and one roundel beside it through the years of his youth: «خَدِيجَةُ»), wisdoms (three
 * roundels, each with a reason: a roof over the widow, a sea for the one widowed far away, broken chains for a freed
 * people — «سَوْدَةُ», «أُمُّ حَبِيبَةَ», «جُوَيْرِيَةَ»), rooms (small rooms beside the mosque, their windows lit; from them
 * light goes out to many).
 */
internal fun DrawScope.lamphousesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "onlyone" to Shot(Offset(w * 0.3f, h * 0.35f), 1.35f),
        "wisdoms" to Shot(Offset(w * 0.6f, h * 0.38f), 1.1f),
        "rooms" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { roomsNight(a) }
}

private fun DrawScope.roomsNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val oo = a.b("onlyone")
    val wi = a.b("wisdoms")
    val ro = a.b("rooms")
    val ground = h * 0.84f
    nightSky(t, RoomsC44.Sky, RoomsC44.SkyLow, ground, starAlpha = 0.6f)
    drawRect(RoomsC44.Ground, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - 0.7f * seg(ro, 0f, 0.4f)
    // his youth with one wife
    if (oo > 0f) {
        val f = easeOut(seg(oo, 0f, 0.3f)) * away
        val light = Offset(w * 0.22f, h * 0.32f)
        sirajMunir(light, w * 0.011f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        nameRoundel(light + Offset(w * 0.13f, h * 0.02f), w * 0.05f, a.tag("onlyone"), Roundel.Fills[0], Roundel.Rim, f * seg(oo, 0.2f, 0.45f))
        for (k in 0 until 10) {
            val g = seg(oo, 0.4f + 0.05f * k, 0.5f + 0.05f * k)
            drawLine(ArtInk.BrassLight, Offset(w * (0.14f + 0.03f * k), h * 0.46f), Offset(w * (0.14f + 0.03f * k), h * 0.49f), 1.5f * dp, alpha = f * g)
        }
    }
    // three reasons
    if (wi > 0f) {
        val f = easeOut(seg(wi, 0f, 0.3f)) * away
        for (k in 0 until 3) {
            val c = Offset(w * (0.5f + 0.16f * k), h * (0.3f + 0.05f * (k % 2)))
            val g = easeOut(seg(wi, 0.1f + 0.2f * k, 0.35f + 0.2f * k))
            nameRoundel(c, w * 0.045f, a.tag("wisdoms", 0, k), Roundel.Fills[(k + 1) % 4], Roundel.Rim, f * g)
            val icon = c + Offset(0f, h * 0.1f)
            when (k) {
                0 -> drawPath(Path().apply { moveTo(icon.x - w * 0.04f, icon.y); lineTo(icon.x, icon.y - h * 0.035f); lineTo(icon.x + w * 0.04f, icon.y); close() }, ArtInk.BrassLight, alpha = f * g)
                1 -> { for (j in 0 until 3) drawArc(RoomsC44.Sea, 0f, 180f, false, Offset(icon.x - w * 0.045f + j * w * 0.03f, icon.y - h * 0.01f), Size(w * 0.03f, h * 0.02f), alpha = f * g, style = Stroke(2f * dp)) }
                else -> {
                    val br = seg(wi, 0.6f, 0.85f)
                    drawCircle(RoomsC44.Chain, w * 0.012f, icon + Offset(-w * 0.015f - w * 0.015f * br, 0f), alpha = f * g, style = Stroke(2f * dp))
                    drawCircle(RoomsC44.Chain, w * 0.012f, icon + Offset(w * 0.015f + w * 0.015f * br, 0f), alpha = f * g, style = Stroke(2f * dp))
                }
            }
        }
    }
    // rooms by the mosque; the Sunnah goes out from them
    if (ro > 0f) {
        val f = easeOut(seg(ro, 0f, 0.35f))
        val m = Offset(w * 0.3f, ground)
        drawRect(RoomsC44.Mosque, Offset(m.x - w * 0.18f, m.y - h * 0.2f), Size(w * 0.36f, h * 0.2f), alpha = f)
        drawArc(Color(0xFF3A8A5A), 180f, 180f, true, Offset(m.x - w * 0.05f, m.y - h * 0.27f), Size(w * 0.1f, h * 0.14f), alpha = f)
        for (k in 0 until 5) {
            val x = w * (0.54f + 0.085f * k)
            val lit = seg(ro, 0.2f + 0.1f * k, 0.4f + 0.1f * k)
            drawRect(RoomsC44.Room, Offset(x - w * 0.035f, ground - h * 0.12f), Size(w * 0.07f, h * 0.12f), alpha = f)
            drawRect(lerpColor(Color(0xFF3A2E28), Color(0xFFFFE0A0), lit), Offset(x - w * 0.012f, ground - h * 0.09f), Size(w * 0.024f, h * 0.03f), alpha = f)
            for (j in 0 until 3) {
                val u = fract(t * 0.3f + j / 3f + k * 0.13f)
                val an = -PI.toFloat() / 2f + (j - 1) * 0.6f
                drawCircle(RoomsC44.Dot, w * 0.005f, Offset(x, ground - h * 0.12f) + Offset(cos(an), sin(an)) * (h * 0.4f * u), alpha = f * lit * (1f - u))
            }
        }
    }
}

// ═════════════ 4. a disc lit three quarters (bs-ikram) ═════════════

private object HonorC44 {
    val Sky = Color(0xFF1C1A30)
    val SkyLow = Color(0xFF3E3050)
    val Disc = Color(0xFF4A4458)
    val Warm = Color(0xFFF2C45A)
    val Cool = Color(0xFF8AA8D0)
    val Dark = Color(0xFF0E0C14)
    val Heart = Color(0xFFE88A8A)
    val Purse = Color(0xFF8A5A3A)
    val Coin = Color(0xFFD8B04A)
}

/**
 * Beats: quarters (a disc of four quarters; three light up warm and one cool: «ثَلَاثَةُ أَرْبَاعِ الِاهْتِمَامِ»),
 * jahilidark (a dark land with a shut box, then light comes down over it), lifestages (five hearts from small to large,
 * each with a ring of protection round it), ownpurse (a purse of her own, coins dropping in: «ذِمَّةً مَالِيَّةً
 * مُسْتَقِلَّةً»), shaqaiq (two halves of one disc come together, equal).
 */
internal fun DrawScope.threepartsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "quarters" to Shot(Offset(w * 0.28f, h * 0.32f), 1.35f),
        "jahilidark" to Shot(Offset(w * 0.7f, h * 0.35f), 1.25f),
        "lifestages" to Shot(Offset(w * 0.5f, h * 0.68f), 1.15f),
        "ownpurse" to Shot(Offset(w * 0.78f, h * 0.7f), 1.35f),
        "shaqaiq" to Shot(Offset(w * 0.5f, h * 0.4f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { honorNight(a) }
}

private fun DrawScope.honorNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val qu = a.b("quarters")
    val jd = a.b("jahilidark")
    val ls = a.b("lifestages")
    val op = a.b("ownpurse")
    val sq = a.b("shaqaiq")
    nightSky(t, HonorC44.Sky, HonorC44.SkyLow, h, starAlpha = 0.5f)
    val away = 1f - seg(sq, 0f, 0.4f)
    // three quarters
    if (qu > 0f) {
        val f = easeOut(seg(qu, 0f, 0.3f)) * away
        val c = Offset(w * 0.28f, h * 0.3f)
        val r = w * 0.12f
        drawCircle(HonorC44.Disc, r, c, alpha = f)
        for (k in 0 until 4) {
            val g = easeOut(seg(qu, 0.15f + 0.15f * k, 0.35f + 0.15f * k))
            drawArc(if (k < 3) HonorC44.Warm else HonorC44.Cool, -90f + 90f * k, 88f, true, Offset(c.x - r, c.y - r), Size(r * 2f, r * 2f), alpha = f * g)
        }
        tagAt(a.tag("quarters"), c + Offset(0f, r + h * 0.05f), seg(qu, 0.75f, 0.95f) * away * (1f - seg(jd, 0f, 0.3f)), back = Back44, ink = ArtInk.Gold)
    }
    // darkness, then light came down
    if (jd > 0f) {
        val f = easeOut(seg(jd, 0f, 0.3f)) * away * (1f - 0.5f * seg(ls, 0f, 0.4f))
        val c = Offset(w * 0.72f, h * 0.4f)
        drawRect(HonorC44.Dark, Offset(c.x - w * 0.2f, c.y - h * 0.05f), Size(w * 0.4f, h * 0.15f), alpha = f)
        drawRect(HonorC44.Purse, Offset(c.x - w * 0.04f, c.y - h * 0.02f), Size(w * 0.08f, h * 0.05f), alpha = f * (1f - seg(jd, 0.6f, 0.9f)))
        val light = easeOut(seg(jd, 0.45f, 0.9f))
        lightShafts(Offset(c.x, h * 0.05f), PI.toFloat() / 2f, 0.7f, h * 0.4f, Color(0xFFFFF0C8), 0.5f * light * f, t, rays = 6)
        softGlow(c, w * 0.12f * light, ArtInk.Gold, 0.5f * light * f)
        drawPath(w8Heart(c, w * 0.025f * light), HonorC44.Heart, alpha = f * light)
    }
    // protected at every stage of life
    if (ls > 0f) {
        val f = easeOut(seg(ls, 0f, 0.3f)) * away
        for (k in 0 until 5) {
            val c = Offset(w * (0.14f + 0.13f * k), h * 0.68f)
            val g = easeOut(seg(ls, 0.1f + 0.12f * k, 0.3f + 0.12f * k))
            val r = w * (0.015f + 0.006f * k)
            drawPath(w8Heart(c, r * g), HonorC44.Heart, alpha = f)
            drawCircle(ArtInk.BrassLight, r * 1.7f * g, c, alpha = f * 0.8f, style = Stroke(2f * dp))
        }
    }
    // a purse of her own
    if (op > 0f) {
        val f = easeOut(seg(op, 0f, 0.3f)) * away
        val c = Offset(w * 0.82f, h * 0.72f)
        drawOval(HonorC44.Purse, Offset(c.x - w * 0.06f, c.y - h * 0.04f), Size(w * 0.12f, h * 0.09f), alpha = f)
        drawLine(ArtInk.Brass, c + Offset(-w * 0.03f, -h * 0.04f), c + Offset(w * 0.03f, -h * 0.04f), 2f * dp, alpha = f)
        for (k in 0 until 4) {
            val u = seg(op, 0.2f + 0.12f * k, 0.4f + 0.12f * k)
            drawCircle(HonorC44.Coin, w * 0.01f, c + Offset(w * 0.01f * (k - 1.5f), -h * 0.15f + h * 0.11f * u), alpha = f * (1f - seg(u, 0.9f, 1f)))
        }
        tagAt(a.tag("ownpurse"), c + Offset(0f, -h * 0.2f), seg(op, 0.5f, 0.8f) * away, back = Back44, ink = ArtInk.Gold)
    }
    // two halves, one whole
    if (sq > 0f) {
        val f = easeOut(seg(sq, 0f, 0.35f))
        val c = Offset(w * 0.5f, h * 0.4f)
        val meet = easeInOut(seg(sq, 0.2f, 0.7f))
        val r = w * 0.1f
        drawArc(HonorC44.Cool, 90f, 180f, true, Offset(c.x - r - w * 0.12f * (1f - meet), c.y - r), Size(r * 2f, r * 2f), alpha = f)
        drawArc(HonorC44.Warm, -90f, 180f, true, Offset(c.x - r + w * 0.12f * (1f - meet), c.y - r), Size(r * 2f, r * 2f), alpha = f)
        softGlow(c, r * 1.6f, ArtInk.Gold, 0.4f * f * seg(sq, 0.7f, 1f))
    }
}

// ═════════════ 5. a shield over the house, a lock she holds (bs-qiwama) ═════════════

private object ShieldC44 {
    val Sky = Color(0xFF9AB8D8)
    val SkyLow = Color(0xFFF0E2C8)
    val Ground = Color(0xFFB8986A)
    val Wall = Color(0xFFE8DCC4)
    val Roof = Color(0xFF8A5A3A)
    val Shield = Color(0xFF6A8AB0)
    val Coin = Color(0xFFD8B04A)
    val Chest = Color(0xFF7A5A3A)
    val Red = Color(0xFFE0584A)
    val Pillar = Color(0xFF8A7A6A)
}

/**
 * Beats: standingfor (a pillar carries a heavy beam: a duty; a crown beside it is struck out — «تَكْلِيفٌ», «تَشْرِيفٌ»),
 * shieldspend (a shield arches over a house and coins go in at its door), ownbox (her chest with her own lock; three
 * reaching hands stop short of it: «زَوْجٍ», «أَبٍ», «أَخٍ»), consent (a contract that waits for her own seal, then takes
 * it), harmony (two rings linked and glowing: «الْمَوَدَّةِ», «الرَّحْمَةِ»).
 */
internal fun DrawScope.shieldtentScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "standingfor" to Shot(Offset(w * 0.25f, h * 0.45f), 1.3f),
        "shieldspend" to Shot(Offset(w * 0.6f, h * 0.55f), 1.2f),
        "ownbox" to Shot(Offset(w * 0.5f, h * 0.7f), 1.25f),
        "consent" to Shot(Offset(w * 0.75f, h * 0.3f), 1.35f),
        "harmony" to Shot(Offset(w * 0.5f, h * 0.35f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { shieldDay(a) }
}

private fun DrawScope.shieldDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sf = a.b("standingfor")
    val ss = a.b("shieldspend")
    val ob = a.b("ownbox")
    val co = a.b("consent")
    val ha = a.b("harmony")
    val ground = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(ShieldC44.Sky, ShieldC44.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(ShieldC44.Ground, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - 0.8f * seg(ha, 0f, 0.4f)
    // a duty, not a crown
    if (sf > 0f) {
        val f = easeOut(seg(sf, 0f, 0.3f)) * away * (1f - 0.5f * seg(ss, 0f, 0.4f))
        val base = Offset(w * 0.2f, ground)
        drawRect(ShieldC44.Pillar, Offset(base.x - w * 0.025f, base.y - h * 0.32f), Size(w * 0.05f, h * 0.32f), alpha = f)
        val load = easeOut(seg(sf, 0.2f, 0.5f))
        drawRect(Color(0xFF5A4A3A), Offset(base.x - w * 0.12f, base.y - h * 0.36f - h * 0.1f * (1f - load)), Size(w * 0.24f, h * 0.04f), alpha = f * load)
        tagAt(a.tag("standingfor", 0, 0), base + Offset(0f, -h * 0.44f), seg(sf, 0.4f, 0.65f) * away * (1f - seg(ss, 0f, 0.3f)), back = Back44, ink = ArtInk.Gold)
        val crown = Offset(w * 0.38f, ground - h * 0.12f)
        drawRect(ArtInk.Gold, Offset(crown.x - w * 0.04f, crown.y), Size(w * 0.08f, h * 0.03f), alpha = f * seg(sf, 0.5f, 0.65f))
        for (k in 0 until 3) drawPath(Path().apply { val x = crown.x - w * 0.03f + k * w * 0.03f; moveTo(x - w * 0.01f, crown.y); lineTo(x, crown.y - h * 0.03f); lineTo(x + w * 0.01f, crown.y); close() }, ArtInk.Gold, alpha = f * seg(sf, 0.5f, 0.65f))
        cross21(crown + Offset(0f, h * 0.01f), w * 0.05f, ShieldC44.Red, seg(sf, 0.7f, 0.9f) * f, 3f * dp)
        tagAt(a.tag("standingfor", 0, 1), crown + Offset(0f, -h * 0.08f), seg(sf, 0.75f, 0.95f) * away * (1f - seg(ss, 0f, 0.3f)), back = Back44, ink = Color(0xFFFFD8D0))
    }
    // shield and spending
    val home = Offset(w * 0.62f, ground)
    if (ss > 0f) {
        val f = easeOut(seg(ss, 0f, 0.3f)) * away
        house44(home, w * 0.2f, ShieldC44.Wall, ShieldC44.Roof, f, lit = 0.5f)
        val arc = easeOut(seg(ss, 0.15f, 0.5f))
        drawArc(ShieldC44.Shield, 180f, 180f * arc, false, Offset(home.x - w * 0.18f, home.y - h * 0.34f), Size(w * 0.36f, h * 0.5f), alpha = f, style = Stroke(5f * dp, cap = StrokeCap.Round))
        for (k in 0 until 3) {
            val u = seg(ss, 0.5f + 0.12f * k, 0.7f + 0.12f * k)
            val from = Offset(w * 0.92f, ground - h * 0.05f)
            drawCircle(ShieldC44.Coin, w * 0.011f, from + (home + Offset(0f, -h * 0.05f) - from) * u, alpha = f * (1f - seg(u, 0.9f, 1f)))
        }
    }
    // her chest, her lock
    if (ob > 0f) {
        val f = easeOut(seg(ob, 0f, 0.3f)) * away
        val c = Offset(w * 0.5f, h * 0.74f)
        drawRect(ShieldC44.Chest, Offset(c.x - w * 0.07f, c.y - h * 0.05f), Size(w * 0.14f, h * 0.1f), alpha = f)
        padlock21(c + Offset(0f, h * 0.01f), w * 0.03f, ArtInk.Brass, 0f, f)
        for (k in 0 until 3) {
            val an = PI.toFloat() * (1.15f + 0.35f * k)
            val reach = easeOut(seg(ob, 0.2f + 0.12f * k, 0.5f + 0.12f * k))
            val from = c + Offset(cos(an), sin(an)) * (w * 0.26f)
            val to = c + Offset(cos(an), sin(an)) * (w * (0.26f - 0.1f * reach))
            drawLine(Color(0xFF8A7A6A), from, to, 3f * dp, StrokeCap.Round, alpha = f)
            drawLine(ShieldC44.Red, to + Offset(-sin(an), cos(an)) * (w * 0.02f), to - Offset(-sin(an), cos(an)) * (w * 0.02f), 3f * dp, StrokeCap.Round, alpha = f * seg(ob, 0.5f + 0.12f * k, 0.6f + 0.12f * k))
            tagAt(a.tag("ownbox", 0, k), from + Offset(cos(an), sin(an)) * (w * 0.05f), seg(ob, 0.4f + 0.12f * k, 0.6f + 0.12f * k) * away, back = Back44, ink = ArtInk.Ivory)
        }
    }
    // a contract waiting for her seal
    if (co > 0f) {
        val f = easeOut(seg(co, 0f, 0.3f)) * away
        val c = Offset(w * 0.76f, h * 0.28f)
        drawRect(Color(0xFFFBF7EC), Offset(c.x - w * 0.09f, c.y - h * 0.12f), Size(w * 0.18f, h * 0.24f), alpha = f)
        for (r in 0 until 5) drawLine(Color(0xFF9A9080), Offset(c.x - w * 0.06f, c.y - h * 0.08f + r * h * 0.03f), Offset(c.x + w * 0.06f, c.y - h * 0.08f + r * h * 0.03f), 1.2f * dp, alpha = f)
        val stamp = easeOut(seg(co, 0.55f, 0.75f))
        drawCircle(Color(0xFFC8384A), w * 0.025f * (1.6f - 0.6f * stamp), c + Offset(w * 0.04f, h * 0.08f), alpha = f * stamp)
        drawCircle(Color(0xFFC8384A), w * 0.025f, c + Offset(w * 0.04f, h * 0.08f), alpha = f * (1f - stamp), style = Stroke(1.5f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
    }
    // two rings, linked
    if (ha > 0f) {
        val f = easeOut(seg(ha, 0f, 0.35f))
        val c = Offset(w * 0.5f, h * 0.35f)
        val meet = easeInOut(seg(ha, 0.2f, 0.6f))
        for (k in 0 until 2) {
            val s = if (k == 0) -1f else 1f
            val rc = c + Offset(s * w * (0.12f - 0.065f * meet), 0f)
            drawCircle(if (k == 0) Color(0xFFE8A8A8) else ArtInk.Gold, w * 0.08f, rc, alpha = f, style = Stroke(6f * dp))
            tagAt(a.tag("harmony", 0, k), rc + Offset(s * w * 0.04f, h * 0.15f), seg(ha, 0.5f + 0.15f * k, 0.75f + 0.15f * k), back = Back44, ink = ArtInk.Gold)
        }
        softGlow(c, w * 0.15f, ArtInk.Gold, 0.4f * f * seg(ha, 0.6f, 1f))
    }
}

// ═════════════ 6. a ledger, a cradle, a share that flows out (bs-shahada) ═════════════

private object LedgerC44 {
    val Wall = Color(0xFFE6DCC6)
    val WallLow = Color(0xFFCFC2A6)
    val Floor = Color(0xFF8A7458)
    val Book = Color(0xFF6A4A3A)
    val Page = Color(0xFFFBF7EC)
    val Seal = Color(0xFFC8384A)
    val Cradle = Color(0xFF9A7A58)
    val Coin = Color(0xFFD8B04A)
    val Sign = Color(0xFFE0584A)
}

/**
 * Beats: tradeledger (a merchant's ledger with two seals beside it; when one fades the other lights it again),
 * cradle (a cradle, and beside it a single seal that is enough), obligations (a son's two coins flow out at once: to a
 * ring, to a house, to small hearts; a daughter's one coin stays in her purse), roadsign (a round road sign showing a
 * ball and a paw: a warning that likens no one to anyone).
 */
internal fun DrawScope.ledgerscaleScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "tradeledger" to Shot(Offset(w * 0.3f, h * 0.4f), 1.3f),
        "cradle" to Shot(Offset(w * 0.72f, h * 0.6f), 1.35f),
        "obligations" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "roadsign" to Shot(Offset(w * 0.5f, h * 0.35f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { ledgerRoom(a) }
}

private fun DrawScope.ledgerRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tl = a.b("tradeledger")
    val cr = a.b("cradle")
    val obl = a.b("obligations")
    val rsn = a.b("roadsign")
    val floor = h * 0.86f
    drawRect(Brush.verticalGradient(listOf(LedgerC44.Wall, LedgerC44.WallLow), endY = floor), size = Size(w, floor))
    drawRect(LedgerC44.Floor, Offset(0f, floor), Size(w, h - floor))
    val away = 1f - seg(obl, 0f, 0.4f)
    // a trade ledger and two seals that remind each other
    if (tl > 0f) {
        val f = easeOut(seg(tl, 0f, 0.3f)) * away
        val c = Offset(w * 0.28f, h * 0.42f)
        drawRect(LedgerC44.Book, Offset(c.x - w * 0.15f, c.y - h * 0.12f), Size(w * 0.3f, h * 0.24f), alpha = f)
        drawRect(LedgerC44.Page, Offset(c.x - w * 0.14f, c.y - h * 0.11f), Size(w * 0.28f, h * 0.22f), alpha = f)
        for (r in 0 until 5) {
            drawLine(Color(0xFF9A9080), Offset(c.x - w * 0.12f, c.y - h * 0.07f + r * h * 0.035f), Offset(c.x + w * 0.05f, c.y - h * 0.07f + r * h * 0.035f), 1.2f * dp, alpha = f)
            drawCircle(LedgerC44.Coin, w * 0.008f, Offset(c.x + w * 0.09f, c.y - h * 0.07f + r * h * 0.035f), alpha = f)
        }
        for (k in 0 until 2) {
            val s = c + Offset(w * (-0.05f + 0.1f * k), h * 0.18f)
            val dim = if (k == 0) bump(seg(tl, 0.4f, 0.8f)) else 0f
            drawCircle(LedgerC44.Seal, w * 0.025f, s, alpha = f * (1f - 0.7f * dim))
            if (k == 1) softGlow(s, w * 0.04f, ArtInk.Gold, 0.5f * f * dim)
        }
    }
    // a cradle: her word alone suffices
    if (cr > 0f) {
        val f = easeOut(seg(cr, 0f, 0.3f)) * away
        val c = Offset(w * 0.72f, floor)
        drawArc(LedgerC44.Cradle, 0f, 180f, true, Offset(c.x - w * 0.1f, c.y - h * 0.16f), Size(w * 0.2f, h * 0.14f), alpha = f)
        drawLine(LedgerC44.Cradle, c + Offset(-w * 0.09f, -h * 0.1f), c + Offset(-w * 0.07f, 0f), 3f * dp, alpha = f)
        drawLine(LedgerC44.Cradle, c + Offset(w * 0.09f, -h * 0.1f), c + Offset(w * 0.07f, 0f), 3f * dp, alpha = f)
        val s = c + Offset(0f, -h * 0.26f)
        drawCircle(LedgerC44.Seal, w * 0.025f, s, alpha = f * seg(cr, 0.4f, 0.6f))
        check21(s + Offset(w * 0.05f, 0f), w * 0.015f, ArtInk.Jade, seg(cr, 0.6f, 0.8f) * f, 2.5f * dp)
    }
    // the son's share flows out to others; hers stays hers
    if (obl > 0f) {
        val f = easeOut(seg(obl, 0f, 0.3f)) * (1f - 0.6f * seg(rsn, 0f, 0.4f))
        val son = Offset(w * 0.3f, h * 0.5f)
        val dau = Offset(w * 0.7f, h * 0.5f)
        drawOval(Color(0xFF8A5A3A), Offset(son.x - w * 0.05f, son.y - h * 0.035f), Size(w * 0.1f, h * 0.07f), alpha = f)
        drawOval(Color(0xFF8A5A3A), Offset(dau.x - w * 0.04f, dau.y - h * 0.03f), Size(w * 0.08f, h * 0.06f), alpha = f)
        for (k in 0 until 2) drawCircle(LedgerC44.Coin, w * 0.015f, son + Offset(w * 0.02f * (k * 2 - 1), -h * 0.06f), alpha = f * seg(obl, 0.1f, 0.25f))
        drawCircle(LedgerC44.Coin, w * 0.015f, dau + Offset(0f, -h * 0.06f), alpha = f * seg(obl, 0.15f, 0.3f))
        val targets = listOf(Offset(w * 0.15f, h * 0.75f), Offset(w * 0.3f, h * 0.8f), Offset(w * 0.45f, h * 0.75f))
        for ((k, tgt) in targets.withIndex()) {
            val u = easeInOut(seg(obl, 0.35f + 0.12f * k, 0.6f + 0.12f * k))
            drawCircle(LedgerC44.Coin, w * 0.01f, son + (tgt - son) * u, alpha = f * seg(obl, 0.35f + 0.12f * k, 0.4f + 0.12f * k))
            val g = seg(obl, 0.55f + 0.12f * k, 0.7f + 0.12f * k) * f
            when (k) {
                0 -> drawCircle(ArtInk.Gold, w * 0.018f, tgt + Offset(0f, h * 0.03f), alpha = g, style = Stroke(3f * dp))
                1 -> house44(tgt + Offset(0f, h * 0.05f), w * 0.06f, Color(0xFFE8DCC4), Color(0xFF8A5A3A), g)
                else -> { for (j in 0 until 2) drawPath(w8Heart(tgt + Offset(w * 0.02f * (j * 2 - 1), h * 0.03f), w * 0.012f), Color(0xFFE88A8A), alpha = g) }
            }
        }
        softGlow(dau, w * 0.06f, ArtInk.Gold, 0.4f * f * seg(obl, 0.6f, 1f))
    }
    // a road sign
    if (rsn > 0f) {
        val f = easeOut(seg(rsn, 0f, 0.35f))
        val c = Offset(w * 0.5f, h * 0.32f)
        drawLine(Color(0xFF6A6A70), c + Offset(0f, w * 0.1f), Offset(c.x, floor), 4f * dp, alpha = f)
        drawCircle(Color.White, w * 0.1f, c, alpha = f)
        drawCircle(LedgerC44.Sign, w * 0.1f, c, alpha = f, style = Stroke(6f * dp))
        drawCircle(Color(0xFF3A3A40), w * 0.022f, c + Offset(-w * 0.035f, h * 0.01f), alpha = f)
        for (k in 0 until 4) drawCircle(Color(0xFF3A3A40), w * 0.008f, c + Offset(w * (0.02f + 0.018f * k), -h * 0.012f * (if (k % 3 == 0) 0f else 1f)), alpha = f)
        drawCircle(Color(0xFF3A3A40), w * 0.016f, c + Offset(w * 0.045f, h * 0.018f), alpha = f)
    }
}

// ═════════════ 7. two houses kept level (bs-taaddud) ═════════════

private object FairC44 {
    val Sky = Color(0xFF1C1E34)
    val SkyLow = Color(0xFF3A3450)
    val Ground = Color(0xFF3A3236)
    val Wall = Color(0xFFD8C8A8)
    val Roof = Color(0xFF8A5A3A)
    val Rope = Color(0xFFD8B04A)
    val Thread = Color(0xFF8A8A96)
    val Brass = Color(0xFFD8B04A)
    val Crack = Color(0xFFE0584A)
    val Moon = Color(0xFFF2EAD0)
}

/**
 * Beats: bondvs (on one side a house bound by a strong gold cord, «بِرِبَاطٍ شَرْعِيٍّ مَسْؤُولٍ»; on the other loose
 * threads that blow away), justicecond (a balance between two houses held level: «الْعَدْلِ»), tilted (the balance tips to
 * one side and its beam cracks), equalnights (above each house the same moons and the same coins), khadijaonly (his light
 * and one roundel for many years: «خَدِيجَةَ»).
 */
internal fun DrawScope.fairhousesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "bondvs" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "justicecond" to Shot(Offset(w * 0.5f, h * 0.4f), 1.15f),
        "tilted" to Shot(Offset(w * 0.5f, h * 0.35f), 1.3f),
        "equalnights" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "khadijaonly" to Shot(Offset(w * 0.5f, h * 0.3f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { fairNight(a) }
}

private fun DrawScope.fairNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bv = a.b("bondvs")
    val jc = a.b("justicecond")
    val ti = a.b("tilted")
    val en = a.b("equalnights")
    val ko = a.b("khadijaonly")
    val ground = h * 0.82f
    nightSky(t, FairC44.Sky, FairC44.SkyLow, ground, starAlpha = 0.6f)
    drawRect(FairC44.Ground, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - 0.8f * seg(ko, 0f, 0.4f)
    // a bond, or loose threads
    if (bv > 0f) {
        val f = easeOut(seg(bv, 0f, 0.3f)) * away * (1f - seg(jc, 0f, 0.4f))
        val h1 = Offset(w * 0.28f, ground)
        house44(h1, w * 0.18f, FairC44.Wall, FairC44.Roof, f, lit = 0.6f)
        val tie = easeOut(seg(bv, 0.2f, 0.6f))
        drawArc(FairC44.Rope, 0f, 360f * tie, false, Offset(h1.x - w * 0.13f, h1.y - h * 0.24f), Size(w * 0.26f, h * 0.26f), alpha = f, style = Stroke(3f * dp))
        tagAt(a.tag("bondvs"), h1 + Offset(0f, -h * 0.3f), seg(bv, 0.4f, 0.65f) * away * (1f - seg(jc, 0f, 0.3f)), back = Back44, ink = ArtInk.Gold)
        val blow = easeOut(seg(bv, 0.5f, 1f))
        for (k in 0 until 5) {
            val p = Offset(w * (0.62f + 0.06f * k) + w * 0.1f * blow, ground - h * (0.08f + 0.04f * (k % 3)) - h * 0.1f * blow)
            drawLine(FairC44.Thread, p, p + Offset(w * 0.04f, -h * 0.02f + h * 0.01f * sin(t * 4f + k)), 1.2f * dp, alpha = f * (1f - blow) * 0.9f)
        }
    }
    // the balance: level, then tipped
    val houses = listOf(Offset(w * 0.22f, ground), Offset(w * 0.78f, ground))
    if (jc > 0f) {
        val f = easeOut(seg(jc, 0f, 0.3f)) * away * (1f - 0.6f * seg(en, 0.5f, 1f))
        for (p in houses) house44(p, w * 0.16f, FairC44.Wall, FairC44.Roof, f, lit = 0.4f)
        val c = Offset(w * 0.5f, h * 0.3f)
        val tilt = 0.3f * easeInOut(seg(ti, 0.1f, 0.6f)) * (1f - 0.9f * seg(en, 0f, 0.4f))
        drawLine(FairC44.Brass, c, Offset(c.x, ground), 3f * dp, alpha = f)
        val l = c + Offset(-w * 0.2f * cos(tilt), w * 0.2f * sin(tilt))
        val r = c + Offset(w * 0.2f * cos(tilt), -w * 0.2f * sin(tilt))
        drawLine(FairC44.Brass, l, r, 4f * dp, StrokeCap.Round, alpha = f)
        for (p in listOf(l, r)) drawArc(FairC44.Brass, 0f, 180f, true, Offset(p.x - w * 0.05f, p.y + h * 0.04f), Size(w * 0.1f, h * 0.04f), alpha = f)
        tagAt(a.tag("justicecond"), c + Offset(0f, -h * 0.08f), seg(jc, 0.4f, 0.7f) * away * (1f - seg(ti, 0f, 0.3f)), back = Back44, ink = ArtInk.Gold)
        val crack = seg(ti, 0.55f, 0.85f) * (1f - seg(en, 0f, 0.4f))
        if (crack > 0f) drawPath(Path().apply { moveTo(c.x + w * 0.05f, c.y - h * 0.01f); lineTo(c.x + w * 0.07f, c.y + h * 0.01f); lineTo(c.x + w * 0.065f, c.y + h * 0.03f); lineTo(c.x + w * 0.085f, c.y + h * 0.05f) }, FairC44.Crack, alpha = f * crack, style = Stroke(2.5f * dp))
    }
    // the same nights and the same spending for each
    if (en > 0f) {
        val f = easeOut(seg(en, 0f, 0.3f)) * away
        for ((k, p) in houses.withIndex()) {
            for (j in 0 until 3) {
                val g = seg(en, 0.2f + 0.08f * j + 0.04f * k, 0.4f + 0.08f * j + 0.04f * k)
                val m = p + Offset(w * (-0.06f + 0.06f * j), -h * 0.3f)
                drawCircle(FairC44.Moon, w * 0.015f, m, alpha = f * g)
                drawCircle(FairC44.SkyLow, w * 0.013f, m + Offset(w * 0.007f, -h * 0.003f), alpha = f * g)
                drawCircle(ArtInk.Gold, w * 0.01f, p + Offset(w * (-0.06f + 0.06f * j), -h * 0.22f), alpha = f * g)
            }
        }
    }
    // one wife for all those years
    if (ko > 0f) {
        val f = easeOut(seg(ko, 0f, 0.35f))
        val light = Offset(w * 0.42f, h * 0.3f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        nameRoundel(light + Offset(w * 0.14f, h * 0.02f), w * 0.05f, a.tag("khadijaonly"), Roundel.Fills[0], Roundel.Rim, f * seg(ko, 0.2f, 0.45f))
        for (k in 0 until 12) drawLine(ArtInk.BrassLight, Offset(w * (0.3f + 0.035f * k), h * 0.45f), Offset(w * (0.3f + 0.035f * k), h * 0.48f), 1.5f * dp, alpha = f * seg(ko, 0.4f + 0.04f * k, 0.5f + 0.04f * k))
    }
}
