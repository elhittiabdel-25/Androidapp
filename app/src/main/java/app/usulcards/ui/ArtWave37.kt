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
 * Wave 37 (2026-10-27): «بصائر»، الجزء الثاني، «الإخبار بالمغيبات». What he foretold and how it came: Fatima, the first
 * fleet, the mountain that shook; Ammar, the two camps, the hadith they were sure of; the lands opened one by one;
 * the shepherds' towers, the near markets, the foam on the flood; and Arabia green again over its buried rivers.
 * Companions are only their names in roundels; he ﷺ is only light.
 */

private val Back37 = Color(0xE6181420)

// ═════════════ 1. foretold, and it came (bs-akhbar) ═════════════

private object ForeC37 {
    val Sky = Color(0xFF1A1E36)
    val SkyLow = Color(0xFF3A3452)
    val Sea = Color(0xFF1E3A5A)
    val SeaLow = Color(0xFF122638)
    val Island = Color(0xFF8A7A5A)
    val Ship = Color(0xFF8A5A3A)
    val Sail = Color(0xFFF2EAD8)
    val Hira = Color(0xFF5A4A44)
    val Tear = Color(0xFF8AC8F0)
    val Martyr = Color(0xFFC8384A)
}

/**
 * Beats: secret (his light and Fatima's roundel; a tear, then a warm glow — «فَاطِمَةُ»), sixmonths (six small marks
 * pass from his light to her, and her roundel joins his light), fleet (below, the first fleet sails for an island,
 * «أُمُّ حَرَامٍ» on the leading ship), hira (on the right, Hira trembles: his light on it, and six roundels round
 * it), martyrs (five of the six take a crimson ring).
 */
internal fun DrawScope.foretoldScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "secret" to Shot(Offset(w * 0.22f, h * 0.32f), 1.5f),
        "sixmonths" to Shot(Offset(w * 0.22f, h * 0.32f), 1.4f),
        "fleet" to Shot(Offset(w * 0.3f, h * 0.78f), 1.4f),
        "hira" to Shot(Offset(w * 0.72f, h * 0.42f), 1.3f),
        "martyrs" to Shot(Offset(w * 0.72f, h * 0.42f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { foretoldNight(a) }
}

private fun DrawScope.foretoldNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val se = a.b("secret")
    val sm = a.b("sixmonths")
    val fl = a.b("fleet")
    val hi = a.b("hira")
    val ma = a.b("martyrs")
    val seaTop = h * 0.66f
    nightSky(t, ForeC37.Sky, ForeC37.SkyLow, seaTop, starAlpha = 0.6f)
    drawRect(Brush.verticalGradient(listOf(ForeC37.Sea, ForeC37.SeaLow), startY = seaTop, endY = h), Offset(0f, seaTop), Size(w, h - seaTop))
    // his light and Fatima
    val light = Offset(w * 0.14f, h * 0.3f)
    val showLight = easeOut(seg(se, 0f, 0.3f)) * (1f - easeOut(seg(fl, 0f, 0.3f)))
    sirajMunir(light, w * 0.011f, showLight, t, nameAt = light + Offset(0f, -h * 0.12f))
    if (se > 0f) {
        val join = easeInOut(seg(sm, 0.5f, 1f))
        val her = Offset(w * 0.3f, h * 0.32f) + (light + Offset(w * 0.05f, h * 0.02f) - Offset(w * 0.3f, h * 0.32f)) * join
        nameRoundel(her, w * 0.045f, a.tag("secret"), Roundel.Fills[1], Roundel.Rim, easeOut(seg(se, 0.1f, 0.35f)) * (1f - seg(fl, 0f, 0.3f)))
        // a tear, then a warm glow
        val tear = seg(se, 0.35f, 0.65f)
        if (tear in 0.01f..0.99f) drawPath(Path().apply {
            val p = her + Offset(w * 0.05f, -h * 0.02f + h * 0.06f * tear)
            moveTo(p.x, p.y - h * 0.02f); quadraticTo(p.x + w * 0.008f, p.y, p.x, p.y + h * 0.008f); quadraticTo(p.x - w * 0.008f, p.y, p.x, p.y - h * 0.02f)
        }, ForeC37.Tear, alpha = 1f - tear)
        val warm = seg(se, 0.65f, 0.95f) * (1f - seg(fl, 0f, 0.3f))
        if (warm > 0f) softGlow(her, w * 0.07f, ArtInk.Gold, 0.6f * warm)
    }
    // six marks: six months
    if (sm > 0f) {
        for (k in 0 until 6) {
            val u = seg(sm, k * 0.07f, 0.5f)
            val p = light + (Offset(w * 0.3f, h * 0.32f) - light) * u + Offset(0f, -h * 0.08f * sin(PI.toFloat() * u))
            drawCircle(ArtInk.BrassLight, w * 0.005f, p, alpha = (1f - seg(sm, 0.55f, 0.7f)) * (1f - seg(fl, 0f, 0.3f)))
        }
    }
    // the first fleet
    if (fl > 0f) {
        val f = easeOut(seg(fl, 0f, 0.3f))
        val island = Offset(w * 0.52f, seaTop + h * 0.13f)
        drawOval(ForeC37.Island, Offset(island.x - w * 0.08f, island.y - h * 0.03f), Size(w * 0.16f, h * 0.06f), alpha = f)
        val sail = easeInOut(seg(fl, 0.1f, 0.95f))
        for (k in 0 until 4) {
            val p = Offset(w * (0.06f - 0.07f * k) + w * 0.3f * sail, seaTop + h * (0.1f + 0.05f * (k % 2)) + h * 0.006f * sin(t * 2f + k))
            drawPath(Path().apply { moveTo(p.x - w * 0.035f, p.y); lineTo(p.x + w * 0.035f, p.y); lineTo(p.x + w * 0.025f, p.y + h * 0.03f); lineTo(p.x - w * 0.025f, p.y + h * 0.03f); close() }, ForeC37.Ship, alpha = f)
            drawLine(ForeC37.Ship, p, p + Offset(0f, -h * 0.07f), 1.5f * dp, alpha = f)
            drawPath(Path().apply { moveTo(p.x + w * 0.002f, p.y - h * 0.07f); lineTo(p.x + w * 0.03f, p.y - h * 0.015f); lineTo(p.x + w * 0.002f, p.y - h * 0.015f); close() }, ForeC37.Sail, alpha = f)
            if (k == 0) nameRoundel(p + Offset(0f, -h * 0.13f), w * 0.04f, a.tag("fleet"), Roundel.Fills[2], Roundel.Rim, seg(fl, 0.3f, 0.55f) * (1f - seg(hi, 0f, 0.3f)))
        }
    }
    // Hira trembles
    val hc = Offset(w * 0.72f, h * 0.44f)
    if (hi > 0f) {
        val f = easeOut(seg(hi, 0f, 0.25f))
        val shake = bump(seg(hi, 0.2f, 0.5f)) * w * 0.006f * sin(t * 40f)
        drawPath(Path().apply { moveTo(hc.x - w * 0.2f + shake, seaTop); lineTo(hc.x - w * 0.03f + shake, hc.y - h * 0.06f); lineTo(hc.x + w * 0.04f + shake, hc.y - h * 0.08f); lineTo(hc.x + w * 0.22f + shake, seaTop); close() }, ForeC37.Hira, alpha = f)
        sirajMunir(hc + Offset(shake, -h * 0.13f), w * 0.011f, f, t, nameAt = hc + Offset(0f, -h * 0.26f))
        for (k in 0 until 6) {
            val ang = PI.toFloat() * (1.05f + 0.18f * k)
            val p = hc + Offset(cos(ang) * w * 0.15f + shake, -h * 0.02f + sin(ang) * h * 0.12f + h * 0.16f)
            val g = easeOut(seg(hi, 0.3f + k * 0.07f, 0.55f + k * 0.07f))
            if (k >= 1 && ma > 0f) {
                val ring = easeOut(seg(ma, 0.1f + (k - 1) * 0.12f, 0.35f + (k - 1) * 0.12f))
                drawCircle(ForeC37.Martyr, w * 0.04f, p, alpha = 0.9f * ring, style = Stroke(3f * dp))
                softGlow(p, w * 0.05f, ForeC37.Martyr, 0.3f * ring)
            }
            nameRoundel(p, w * 0.032f, a.tag("hira", 0, k), Roundel.Fills[k % 4], Roundel.Rim, g)
        }
    }
}

// ═════════════ 2. Ammar and the rebel party (bs-ammar) ═════════════

private object AmmarC37 {
    val Sky = Color(0xFFD8C8A8)
    val SkyHigh = Color(0xFF9AB0C8)
    val Ground = Color(0xFFB89A6A)
    val Brick = Color(0xFFB8704A)
    val BrickLine = Color(0xFF7A4A30)
    val Tent = listOf(Color(0xFF6A5A4A), Color(0xFF5A4A5A))
    val Dust = Color(0xFFE8D8B8)
    val Alarm = Color(0xFFE85A4A)
}

/**
 * Beats: twobricks (the mosque's first walls; «عَمَّارٌ» carries two bricks at a time beside his light, and dust is
 * brushed from him), twocamps (years later, two camps on a plain, «عَلِيٌّ» and «مُعَاوِيَةُ»; Ammar's roundel in the
 * first dims, a martyr), alarm (in the other camp «عَمْرُو بْنُ الْعَاصِ» starts up, alarm rings round his name),
 * certain (above, the hadith they were sure of: a scroll under a seal, «يَقِينٌ»).
 */
internal fun DrawScope.siffinScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twobricks" to Shot(Offset(w * 0.3f, h * 0.6f), 1.4f),
        "twocamps" to Shot(Offset(w * 0.55f, h * 0.62f), 1.1f),
        "alarm" to Shot(Offset(w * 0.75f, h * 0.6f), 1.35f),
        "certain" to Shot(Offset(w * 0.55f, h * 0.35f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { siffinPlain(a) }
}

private fun DrawScope.siffinPlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tb = a.b("twobricks")
    val tc = a.b("twocamps")
    val al = a.b("alarm")
    val ce = a.b("certain")
    val ground = h * 0.8f
    drawRect(Brush.verticalGradient(listOf(AmmarC37.SkyHigh, AmmarC37.Sky), endY = ground), size = Size(w, ground))
    drawRect(AmmarC37.Ground, Offset(0f, ground), Size(w, h - ground))
    // the mosque's first walls
    val wallF = easeOut(seg(tb, 0f, 0.3f)) * (1f - easeOut(seg(tc, 0f, 0.4f)))
    if (wallF > 0f) {
        for (r in 0 until 3) for (k in 0 until 8) {
            val x = w * (0.08f + 0.04f * k) + (if (r % 2 == 1) w * 0.02f else 0f)
            val y = ground - h * 0.03f * (r + 1)
            drawRect(AmmarC37.Brick, Offset(x, y), Size(w * 0.038f, h * 0.028f), alpha = wallF)
            drawRect(AmmarC37.BrickLine, Offset(x, y), Size(w * 0.038f, h * 0.028f), alpha = wallF, style = Stroke(0.8f * dp))
        }
        val light = Offset(w * 0.18f, h * 0.42f)
        sirajMunir(light, w * 0.011f, wallF, t, nameAt = light + Offset(0f, -h * 0.12f))
        val carry = easeInOut(seg(tb, 0.2f, 0.9f))
        val am = Offset(w * 0.5f - w * 0.14f * carry, ground - h * 0.2f)
        nameRoundel(am, w * 0.04f, a.tag("twobricks"), Roundel.Fills[0], Roundel.Rim, wallF)
        for (k in 0 until 2) {
            val b = am + Offset(w * (-0.03f + 0.06f * k), h * 0.08f)
            drawRect(AmmarC37.Brick, Offset(b.x - w * 0.018f, b.y - h * 0.014f), Size(w * 0.036f, h * 0.028f), alpha = wallF)
        }
        val brush = seg(tb, 0.7f, 1f)
        for (k in 0 until 6) {
            val p = am + Offset(w * 0.03f * cos(k * 1.1f), -h * 0.05f - h * 0.04f * brush * (k % 3))
            drawCircle(AmmarC37.Dust, w * 0.004f, p, alpha = wallF * (1f - brush) * 0.8f)
        }
    }
    // two camps
    if (tc > 0f) {
        val f = easeOut(seg(tc, 0.2f, 0.5f))
        val camps = listOf(w * 0.34f, w * 0.76f)
        for ((k, x) in camps.withIndex()) {
            for (j in 0 until 4) {
                val tx = x + w * (-0.08f + 0.05f * j)
                drawPath(Path().apply { moveTo(tx - w * 0.025f, ground); lineTo(tx, ground - h * 0.06f); lineTo(tx + w * 0.025f, ground); close() }, AmmarC37.Tent[k], alpha = f)
            }
            nameRoundel(Offset(x, ground - h * 0.16f), w * 0.038f, a.tag("twocamps", 0, k), Roundel.Fills[if (k == 0) 0 else 1], Roundel.Rim, f)
        }
        // Ammar in the first camp, then a martyr
        val am = Offset(w * 0.22f, ground - h * 0.26f)
        val dim = seg(tc, 0.6f, 0.9f)
        nameRoundel(am, w * 0.034f, a.tag("twobricks"), lerpColor(Roundel.Fills[0], Roundel.PlainFill, dim), Roundel.Rim, f)
        if (dim > 0f) {
            drawCircle(Color(0xFFC8384A), w * 0.04f, am, alpha = dim, style = Stroke(2.5f * dp))
            lightShafts(am, -PI.toFloat() / 2f, 0.4f, h * 0.15f, Color(0xFFFFE8C0), 0.5f * dim, t, rays = 4)
        }
    }
    // alarm in the other camp
    if (al > 0f) {
        val f = easeOut(seg(al, 0f, 0.3f))
        val c = Offset(w * 0.86f, ground - h * 0.3f)
        nameRoundel(c, w * 0.036f, a.tag("alarm"), Roundel.Fills[3], Roundel.Rim, f)
        for (k in 0 until 3) {
            val r = fract(t * 1.2f + k / 3f)
            drawCircle(AmmarC37.Alarm, w * (0.04f + 0.04f * r), c, alpha = 0.6f * f * (1f - r), style = Stroke(2f * dp))
        }
    }
    // the hadith they were sure of
    if (ce > 0f) {
        val f = easeOut(seg(ce, 0f, 0.4f))
        val c = Offset(w * 0.55f, h * 0.3f)
        softGlow(c, w * 0.1f, ArtInk.Gold, 0.5f * f)
        scroll14(c, w * 0.12f, h * 0.26f, easeOut(seg(ce, 0.1f, 0.6f)), f)
        tagAt(a.tag("certain"), c + Offset(0f, -h * 0.2f), seg(ce, 0.5f, 0.75f), back = Back37, ink = ArtInk.Gold)
    }
}

// ═════════════ 3. the lands he named (bs-futuh) ═════════════

private object MapC37 {
    val Paper = Color(0xFFEADCB8)
    val PaperLow = Color(0xFFD8C498)
    val Ink = Color(0xFF6A4A2A)
    val Lit = Color(0xFFE8A83A)
    val Dim = Color(0xFF9A8A70)
    val Dome = Color(0xFFD8A83A)
    val Wall = Color(0xFF9A7A58)
    val Man = Color(0xFF2E2620)
    val Nile = Color(0xFF6A9AC8)
}

/**
 * Beats: quds (a map; from Medina a dashed road reaches Jerusalem and its dome lights up: «بَيْتُ الْمَقْدِسِ»),
 * threelands (three more roads and three names light: «الْيَمَنُ», «الشَّامُ», «الْعِرَاقُ»), constantinople (far to the
 * north-west a walled city of domes lights up; farther still another stays a dashed outline: «الْقُسْطَنْطِينِيَّةُ»,
 * «رُومِيَّةُ»), aman (by the Nile a man comes out of the desert to his church: «الْأَمَانُ»).
 */
internal fun DrawScope.conquestsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "quds" to Shot(Offset(w * 0.55f, h * 0.45f), 1.3f),
        "threelands" to Shot(Offset(w * 0.6f, h * 0.5f), 1.05f),
        "constantinople" to Shot(Offset(w * 0.3f, h * 0.25f), 1.35f),
        "aman" to Shot(Offset(w * 0.28f, h * 0.62f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { conquestMap(a) }
}

private fun DrawScope.conquestMap(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val qd = a.b("quds")
    val tl = a.b("threelands")
    val cn = a.b("constantinople")
    val am = a.b("aman")
    drawRect(Brush.radialGradient(listOf(MapC37.Paper, MapC37.PaperLow), Offset(w * 0.5f, h * 0.5f), w * 0.7f), size = Size(w, h))
    // the sea to the north-west, faintly
    drawPath(Path().apply { moveTo(0f, h * 0.38f); quadraticTo(w * 0.25f, h * 0.44f, w * 0.48f, h * 0.36f); lineTo(w * 0.48f, h * 0.3f); quadraticTo(w * 0.3f, h * 0.34f, 0f, h * 0.28f); close() }, Color(0xFFB8CAD0), alpha = 0.6f)
    val medina = Offset(w * 0.62f, h * 0.72f)
    drawCircle(MapC37.Ink, w * 0.008f, medina)
    drawCircle(MapC37.Ink, w * 0.014f, medina, style = Stroke(1.2f * dp))
    fun road(to: Offset, p: Float) {
        if (p <= 0f) return
        drawLine(MapC37.Ink, medina, medina + (to - medina) * p, 1.6f * dp, alpha = 0.8f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp)))
    }
    // Jerusalem
    val quds = Offset(w * 0.52f, h * 0.42f)
    val qr = easeInOut(seg(qd, 0.1f, 0.6f))
    road(quds, qr)
    if (qr >= 1f) {
        softGlow(quds, w * 0.04f, MapC37.Lit, 0.7f)
        drawArc(MapC37.Dome, 180f, 180f, true, Offset(quds.x - w * 0.02f, quds.y - h * 0.035f), Size(w * 0.04f, h * 0.05f))
        drawRect(MapC37.Wall, Offset(quds.x - w * 0.025f, quds.y - h * 0.01f), Size(w * 0.05f, h * 0.02f))
    }
    tagAt(a.tag("quds"), quds + Offset(0f, -h * 0.09f), seg(qd, 0.55f, 0.8f), back = Back37, ink = ArtInk.Gold)
    // Yemen, Sham, Iraq
    val lands = listOf(Offset(w * 0.7f, h * 0.94f), Offset(w * 0.6f, h * 0.33f), Offset(w * 0.86f, h * 0.42f))
    for ((k, p) in lands.withIndex()) {
        val r = easeInOut(seg(tl, 0.1f + k * 0.15f, 0.5f + k * 0.15f))
        road(p, r)
        if (r >= 1f) {
            drawCircle(MapC37.Lit, w * 0.01f, p)
            softGlow(p, w * 0.03f, MapC37.Lit, 0.6f)
        }
        tagAt(a.tag("threelands", 0, k), p + Offset(0f, if (k == 0) -h * 0.07f else -h * 0.07f), seg(tl, 0.4f + k * 0.15f, 0.6f + k * 0.15f), back = Back37, ink = ArtInk.Ivory)
    }
    // Constantinople lit, Rome still an outline
    val kons = Offset(w * 0.3f, h * 0.2f)
    val rome = Offset(w * 0.08f, h * 0.18f)
    if (cn > 0f) {
        val f = easeOut(seg(cn, 0.1f, 0.5f))
        drawRect(MapC37.Wall, Offset(kons.x - w * 0.05f, kons.y - h * 0.01f), Size(w * 0.1f, h * 0.035f), alpha = f)
        for (k in 0 until 3) drawArc(MapC37.Dome, 180f, 180f, true, Offset(kons.x - w * 0.045f + k * w * 0.032f, kons.y - h * 0.045f), Size(w * 0.026f, h * 0.05f), alpha = f)
        softGlow(kons, w * 0.06f, MapC37.Lit, 0.6f * f)
        drawCircle(MapC37.Dim, w * 0.03f, rome, alpha = 0.8f * f, style = Stroke(1.5f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
        tagAt(a.tag("constantinople", 0, 0), kons + Offset(0f, -h * 0.1f), seg(cn, 0.3f, 0.55f), back = Back37, ink = ArtInk.Gold)
        tagAt(a.tag("constantinople", 0, 1), rome + Offset(w * 0.04f, h * 0.08f), seg(cn, 0.5f, 0.75f), back = Back37, ink = Color(0xFFD8D0C0))
    }
    // Egypt: the Nile, the desert, a man coming home to his church
    val nile = Path().apply { moveTo(w * 0.24f, h); cubicTo(w * 0.26f, h * 0.8f, w * 0.22f, h * 0.65f, w * 0.27f, h * 0.5f) }
    drawPath(nile, MapC37.Nile, alpha = 0.7f, style = Stroke(3f * dp))
    if (am > 0f) {
        val f = easeOut(seg(am, 0f, 0.3f))
        val church = Offset(w * 0.3f, h * 0.68f)
        drawRect(Color(0xFFE8DCC4), Offset(church.x - w * 0.03f, church.y - h * 0.06f), Size(w * 0.06f, h * 0.06f), alpha = f)
        drawArc(Color(0xFFC8B898), 180f, 180f, true, Offset(church.x - w * 0.02f, church.y - h * 0.1f), Size(w * 0.04f, h * 0.08f), alpha = f)
        drawPath(archPath26(church.x, church.y - h * 0.04f, church.y, w * 0.01f), Color(0xFF6A5040), alpha = f)
        val walk = easeInOut(seg(am, 0.2f, 0.85f))
        figure(Offset(mix(w * 0.12f, church.x - w * 0.04f, walk), church.y + h * 0.01f), h * 0.12f, MapC37.Man, f * (1f - seg(am, 0.9f, 1f)), walk = if (walk in 0.01f..0.99f) t * 4f else 0f)
        tagAt(a.tag("aman"), church + Offset(0f, -h * 0.17f), seg(am, 0.5f, 0.75f), back = Back37, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. the shepherds' towers (bs-ashrat) ═════════════

private object SkyC37 {
    val Sky = Color(0xFFE8C8A0)
    val SkyHigh = Color(0xFF7A9AC0)
    val Sand = Color(0xFFD8B47A)
    val SandDark = Color(0xFFB8925A)
    val Tower = listOf(Color(0xFF7A8AA0), Color(0xFF8A9AB0), Color(0xFF6A7A90), Color(0xFF9AAAC0), Color(0xFF5A6A80))
    val Glass = Color(0xFFC8E0F0)
    val Man = Color(0xFF2E2420)
    val Wool = Color(0xFFF2EEE4)
    val Node = Color(0xFFFFE6A8)
    val Flood = Color(0xFF6A5A48)
    val Foam = Color(0xFFF2EEE4)
}

/**
 * Beats: shepherds (a shepherd and his sheep on the open sand, barefoot and poor), skyward (towers rise one past the
 * other, the tallest the tallest on earth), markets (across the sky, cities joined by arcs that pulse: markets drawn
 * near), foam (a flood runs along the bottom carrying a crowd of foam: many, and light as foam).
 */
internal fun DrawScope.skylineScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "shepherds" to Shot(Offset(w * 0.3f, h * 0.7f), 1.45f),
        "skyward" to Shot(Offset(w * 0.6f, h * 0.48f), 1.05f),
        "markets" to Shot(Offset(w * 0.5f, h * 0.28f), 1.2f),
        "foam" to Shot(Offset(w * 0.5f, h * 0.78f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { skylineDesert(a) }
}

private fun DrawScope.skylineDesert(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sp = a.b("shepherds")
    val sw = a.b("skyward")
    val mk = a.b("markets")
    val fo = a.b("foam")
    val ground = h * 0.76f
    drawRect(Brush.verticalGradient(listOf(SkyC37.SkyHigh, SkyC37.Sky), endY = ground), size = Size(w, ground))
    drawRect(Brush.verticalGradient(listOf(SkyC37.Sand, SkyC37.SandDark), startY = ground, endY = h), Offset(0f, ground), Size(w, h - ground))
    // towers rising one past the other
    if (sw > 0f) {
        val xs = listOf(0.48f, 0.56f, 0.64f, 0.72f, 0.8f)
        val tops = listOf(0.42f, 0.34f, 0.08f, 0.3f, 0.4f)
        for (k in xs.indices) {
            val rise = easeOut(seg(sw, 0.05f + k * 0.1f, 0.5f + k * 0.1f + (if (k == 2) 0.2f else 0f)))
            val top = mix(ground, h * tops[k], rise)
            val x = w * xs[k]
            val wd = w * (if (k == 2) 0.04f else 0.05f)
            if (k == 2) drawPath(Path().apply { moveTo(x - wd / 2f, ground); lineTo(x - wd * 0.15f, top + h * 0.05f); lineTo(x, top); lineTo(x + wd * 0.15f, top + h * 0.05f); lineTo(x + wd / 2f, ground); close() }, SkyC37.Tower[k])
            else drawRect(SkyC37.Tower[k], Offset(x - wd / 2f, top), Size(wd, ground - top))
            for (r in 0 until 12) {
                val y = top + h * 0.03f + r * h * 0.05f
                if (y > ground - h * 0.02f) break
                drawLine(SkyC37.Glass, Offset(x - wd * 0.3f, y), Offset(x + wd * 0.3f, y), 1f * dp, alpha = 0.6f)
            }
            if (k == 2 && rise >= 1f) glint(Offset(x, top), w * 0.008f, Color.White, 1f)
        }
    }
    // the shepherd and his sheep
    val sf = easeOut(seg(sp, 0f, 0.3f))
    if (sf > 0f) {
        figure(Offset(w * 0.24f, ground + h * 0.06f), h * 0.16f, SkyC37.Man, sf, arms = 0.3f)
        drawLine(Color(0xFF6A4A32), Offset(w * 0.26f, ground + h * 0.06f), Offset(w * 0.27f, ground - h * 0.1f), 1.6f * dp, alpha = sf)
        for (k in 0 until 4) sheep26(Offset(w * (0.1f + 0.05f * k), ground + h * (0.1f + 0.02f * (k % 2))), w * 0.03f, SkyC37.Wool, sf)
    }
    // markets drawn near
    if (mk > 0f) {
        val f = easeOut(seg(mk, 0f, 0.3f))
        val nodes = listOf(Offset(w * 0.1f, h * 0.2f), Offset(w * 0.3f, h * 0.12f), Offset(w * 0.5f, h * 0.22f), Offset(w * 0.7f, h * 0.1f), Offset(w * 0.9f, h * 0.2f))
        for (i in nodes.indices) for (j in i + 1 until nodes.size) {
            val p = nodes[i]
            val q = nodes[j]
            val mid = (p + q) / 2f + Offset(0f, -h * 0.06f)
            val path = Path().apply { moveTo(p.x, p.y); quadraticTo(mid.x, mid.y, q.x, q.y) }
            drawPath(path, SkyC37.Node, alpha = 0.35f * f, style = Stroke(1f * dp))
            val u = fract(t * 0.5f + (i + j) * 0.17f)
            val dot = p * ((1 - u) * (1 - u)) + mid * (2 * u * (1 - u)) + q * (u * u)
            drawCircle(SkyC37.Node, w * 0.004f, dot, alpha = f)
        }
        for (n in nodes) {
            drawCircle(SkyC37.Node, w * 0.008f, n, alpha = f)
            softGlow(n, w * 0.02f, SkyC37.Node, 0.6f * f)
        }
    }
    // a flood carrying foam
    if (fo > 0f) {
        val f = easeOut(seg(fo, 0f, 0.3f))
        val y = h * 0.86f
        val water = Path().apply {
            moveTo(0f, h)
            lineTo(0f, y)
            for (k in 0..30) lineTo(w * k / 30f, y + sin(k * 0.8f - t * 3f) * h * 0.012f)
            lineTo(w, h); close()
        }
        drawPath(water, SkyC37.Flood, alpha = 0.9f * f)
        for (k in 0 until 40) {
            val x = w * fract(w8Rnd(k, 3.3f) + t * 0.08f)
            drawCircle(SkyC37.Foam, w * (0.006f + 0.006f * w8Rnd(k, 7.1f)), Offset(x, y + h * 0.01f * sin(t * 3f + k)), alpha = 0.85f * f)
        }
    }
}

// ═════════════ 5. Arabia green again (bs-muruj) ═════════════

private object ArabC37 {
    val Sea = Color(0xFF3A6A8A)
    val SeaLow = Color(0xFF2A5070)
    val Sand = Color(0xFFD8B47A)
    val Green = Color(0xFF6A9A4A)
    val River = Color(0xFF6AB8E8)
    val Gold = Color(0xFFE8BE4A)
    val Silver = Color(0xFFD0D4DC)
    val Panel = Color(0xFF1E1A2A)
    val Line = Color(0xFFE85A4A)
}

private fun arabia37(w: Float, h: Float): Path = Path().apply {
    moveTo(w * 0.12f, h * 0.18f)
    lineTo(w * 0.36f, h * 0.12f)
    lineTo(w * 0.46f, h * 0.3f)
    lineTo(w * 0.56f, h * 0.48f)
    lineTo(w * 0.6f, h * 0.62f)
    lineTo(w * 0.5f, h * 0.8f)
    lineTo(w * 0.34f, h * 0.9f)
    lineTo(w * 0.26f, h * 0.82f)
    lineTo(w * 0.18f, h * 0.56f)
    lineTo(w * 0.1f, h * 0.36f)
    close()
}

/**
 * Beats: greening (the peninsula from above: green spreads over the sand and rivers run in it — "until it returns"),
 * buriedrivers (a scanning band sweeps the sand and shows old river valleys and lake basins buried beneath it),
 * goldpillars (beside it, pillars of gold and silver rise out of the ground and no one takes them), belowzero (a
 * price line falls through zero: «-37$»).
 */
internal fun DrawScope.greenarabiaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "greening" to Shot(Offset(w * 0.35f, h * 0.5f), 1.15f),
        "buriedrivers" to Shot(Offset(w * 0.35f, h * 0.5f), 1.25f),
        "goldpillars" to Shot(Offset(w * 0.8f, h * 0.35f), 1.4f),
        "belowzero" to Shot(Offset(w * 0.8f, h * 0.72f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { arabiaMap(a) }
}

private fun DrawScope.arabiaMap(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val gr = a.b("greening")
    val br = a.b("buriedrivers")
    val gp = a.b("goldpillars")
    val bz = a.b("belowzero")
    drawRect(Brush.verticalGradient(listOf(ArabC37.Sea, ArabC37.SeaLow)), size = Size(w, h))
    val land = arabia37(w, h)
    drawPath(land, ArabC37.Sand)
    // rivers that run, and green that spreads
    val rivers = listOf(
        listOf(Offset(w * 0.2f, h * 0.3f), Offset(w * 0.3f, h * 0.42f), Offset(w * 0.42f, h * 0.48f), Offset(w * 0.55f, h * 0.56f)),
        listOf(Offset(w * 0.3f, h * 0.2f), Offset(w * 0.33f, h * 0.35f), Offset(w * 0.3f, h * 0.5f), Offset(w * 0.34f, h * 0.7f)),
        listOf(Offset(w * 0.24f, h * 0.62f), Offset(w * 0.32f, h * 0.66f), Offset(w * 0.42f, h * 0.7f), Offset(w * 0.5f, h * 0.78f)),
    )
    val green = easeOut(seg(gr, 0.1f, 0.9f))
    if (green > 0f) {
        for (k in 0 until 14) {
            val p = Offset(w * (0.18f + 0.34f * w8Rnd(k, 2.9f)), h * (0.22f + 0.6f * w8Rnd(k, 6.3f)))
            drawCircle(ArabC37.Green, w * (0.03f + 0.03f * w8Rnd(k, 4.4f)) * seg(green, k * 0.04f, 0.5f + k * 0.04f), p, alpha = 0.7f)
        }
        for (r in rivers) {
            val pts = w8Smooth(r, 8)
            val head = w8Head(pts, seg(green, 0.2f, 1f))
            for (i in 1 until head.size) drawLine(ArabC37.River, head[i - 1], head[i], 2.5f * dp, StrokeCap.Round)
        }
    }
    // the scan that shows the buried valleys
    if (br > 0f) {
        val sweep = easeInOut(seg(br, 0f, 0.8f))
        val x = w * (0.08f + 0.56f * sweep)
        drawRect(Color(0xFFB8E8FF), Offset(x - w * 0.01f, h * 0.08f), Size(w * 0.02f, h * 0.86f), alpha = 0.35f * (1f - seg(br, 0.8f, 1f)))
        for ((k, r) in rivers.withIndex()) {
            val pts = w8Smooth(r.map { it + Offset(w * 0.02f, h * 0.03f * (k - 1)) }, 8)
            for (i in 1 until pts.size) if (pts[i].x < x) drawLine(Color(0xFF2A6A9A), pts[i - 1], pts[i], 1.6f * dp, alpha = 0.8f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp)))
        }
        for (k in 0 until 4) {
            val c = Offset(w * (0.22f + 0.08f * k), h * (0.4f + 0.1f * (k % 2)))
            if (c.x < x) drawOval(Color(0xFF2A6A9A), Offset(c.x - w * 0.025f, c.y - h * 0.015f), Size(w * 0.05f, h * 0.03f), alpha = 0.6f, style = Stroke(1.4f * dp))
        }
    }
    // pillars of gold and silver no one takes
    if (gp > 0f) {
        val f = easeOut(seg(gp, 0f, 0.2f))
        drawRoundRect(ArabC37.Panel, Offset(w * 0.66f, h * 0.06f), Size(w * 0.3f, h * 0.44f), CornerRadius(6f * dp), alpha = 0.85f * f)
        val base = h * 0.46f
        drawLine(Color(0xFF8A7A5A), Offset(w * 0.68f, base), Offset(w * 0.94f, base), 2f * dp, alpha = f)
        for (k in 0 until 4) {
            val rise = easeOut(seg(gp, 0.15f + k * 0.12f, 0.55f + k * 0.12f))
            val x = w * (0.71f + 0.065f * k)
            val top = base - h * (0.18f + 0.04f * (k % 2)) * rise
            drawRect(if (k % 2 == 0) ArabC37.Gold else ArabC37.Silver, Offset(x - w * 0.015f, top), Size(w * 0.03f, base - top), alpha = f)
            if (rise >= 1f) glint(Offset(x - w * 0.006f, top + h * 0.02f), w * 0.005f, Color.White, f)
        }
    }
    // the price line through zero
    if (bz > 0f) {
        val f = easeOut(seg(bz, 0f, 0.2f))
        val box = Offset(w * 0.66f, h * 0.56f)
        drawRoundRect(ArabC37.Panel, box, Size(w * 0.3f, h * 0.38f), CornerRadius(6f * dp), alpha = 0.9f * f)
        val zero = box.y + h * 0.2f
        drawLine(Color(0xFFD8D0C0), Offset(box.x + w * 0.02f, zero), Offset(box.x + w * 0.28f, zero), 1f * dp, alpha = f)
        val run = easeInOut(seg(bz, 0.15f, 0.85f))
        val pts = listOf(Offset(0.03f, -0.12f), Offset(0.08f, -0.1f), Offset(0.13f, -0.08f), Offset(0.18f, -0.04f), Offset(0.22f, 0.02f), Offset(0.26f, 0.13f))
        val path = Path()
        val n = 1 + ((pts.size - 1) * run).toInt()
        for (k in 0 until n.coerceAtMost(pts.size)) {
            val p = Offset(box.x + w * pts[k].x, zero + h * pts[k].y)
            if (k == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
        }
        drawPath(path, ArabC37.Line, alpha = f, style = Stroke(2.5f * dp, cap = StrokeCap.Round))
        tagAt(a.tag("belowzero"), Offset(box.x + w * 0.2f, zero + h * 0.08f), seg(bz, 0.7f, 0.95f), back = Back37, ink = Color(0xFFFFB0A0))
    }
}
