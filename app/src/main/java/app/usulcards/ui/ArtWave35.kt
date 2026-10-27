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
 * Wave 35 (2026-10-27): «بصائر»، الجزء الثاني، «الإعجاز العلمي». The book that took none of its age's myths (a brain
 * cooling the blood, a sun drawn by seven horses); three gates a scientific reading must pass; smoke, not water, and a
 * sky that is built and widens; the morning's breath, the moon gone dark, the wave beneath the wave; two waters
 * mingled; and the small bone at the base of the spine.
 */

private val Back35 = Color(0xE6181420)

// ═════════════ 1. the myths of the age (bs-khurafat) ═════════════

private object MythC35 {
    val Sky = Color(0xFF2A2240)
    val SkyLow = Color(0xFF7A5A5A)
    val Floor = Color(0xFF2E2420)
    val Page = Color(0xFFF6EED8)
    val Brain = Color(0xFFE8A0A8)
    val Fin = Color(0xFF9AA0AA)
    val Sun = Color(0xFFFFC85A)
    val Horse = Color(0xFF6A4A3A)
    val Ash = Color(0xFF6A6470)
}

/** A plain horse in profile, facing left, its hooves at [foot]; [s] its length. */
private fun DrawScope.horse35(foot: Offset, s: Float, color: Color, alpha: Float, gait: Float) {
    if (alpha <= 0.01f) return
    val body = foot + Offset(0f, -s * 0.55f)
    drawOval(color, Offset(body.x - s * 0.4f, body.y - s * 0.16f), Size(s * 0.8f, s * 0.32f), alpha = alpha)
    drawLine(color, body + Offset(-s * 0.32f, -s * 0.05f), body + Offset(-s * 0.5f, -s * 0.38f), s * 0.12f, StrokeCap.Round, alpha = alpha)
    drawOval(color, Offset(body.x - s * 0.66f, body.y - s * 0.46f), Size(s * 0.24f, s * 0.13f), alpha = alpha)
    for (k in 0 until 4) {
        val x = body.x + s * (-0.28f + 0.18f * k)
        val sw = sin(gait + k * 1.6f) * s * 0.08f
        drawLine(color, Offset(x, body.y + s * 0.1f), Offset(x + sw, foot.y), s * 0.06f, StrokeCap.Round, alpha = alpha)
    }
    drawLine(color, body + Offset(s * 0.38f, -s * 0.05f), body + Offset(s * 0.5f, s * 0.2f), s * 0.04f, StrokeCap.Round, alpha = alpha)
}

/**
 * Beats: manyverses (an open Book; round it, the signs it speaks of — rain, a star, a seed, a womb's drop — more than a
 * thousand verses), radiator (Aristotle's brain: a radiator that cools the blood, «أَرِسْطُو»), sevenhorses (a sun
 * drawn by seven horses across the sky, «الرِّيجْ فِيدَا»), clean (the myths grey and crumble; the Book stays clean
 * and lit).
 */
internal fun DrawScope.mythsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "manyverses" to Shot(Offset(w * 0.5f, h * 0.55f), 1.25f),
        "radiator" to Shot(Offset(w * 0.2f, h * 0.45f), 1.45f),
        "sevenhorses" to Shot(Offset(w * 0.72f, h * 0.3f), 1.35f),
        "clean" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { mythRoom(a) }
}

private fun DrawScope.mythRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val mv = a.b("manyverses")
    val ra = a.b("radiator")
    val sh = a.b("sevenhorses")
    val cl = a.b("clean")
    val floor = h * 0.84f
    nightSky(t, MythC35.Sky, MythC35.SkyLow, floor, starAlpha = 0.5f)
    drawRect(MythC35.Floor, Offset(0f, floor), Size(w, h - floor))
    val ash = easeOut(seg(cl, 0.1f, 0.6f))
    val fall = w8EaseIn(seg(cl, 0.4f, 1f))
    // Aristotle's brain, a radiator for the blood
    if (ra > 0f) {
        val f = easeOut(seg(ra, 0f, 0.3f)) * (1f - fall)
        val c = Offset(w * 0.2f, h * 0.48f + h * 0.3f * fall)
        val col = lerpColor(MythC35.Brain, MythC35.Ash, ash)
        drawOval(col, Offset(c.x - w * 0.08f, c.y - h * 0.08f), Size(w * 0.09f, h * 0.15f), alpha = f)
        drawOval(col, Offset(c.x - w * 0.01f, c.y - h * 0.08f), Size(w * 0.09f, h * 0.15f), alpha = f)
        for (k in 0 until 5) drawLine(lerpColor(Color(0xFFB8707A), MythC35.Ash, ash), Offset(c.x - w * 0.06f + k * w * 0.03f, c.y - h * 0.06f), Offset(c.x - w * 0.055f + k * w * 0.03f, c.y + h * 0.05f), 1.2f * dp, alpha = f)
        for (k in 0 until 6) drawRect(MythC35.Fin, Offset(c.x - w * 0.07f + k * w * 0.026f, c.y + h * 0.08f), Size(w * 0.012f, h * 0.06f), alpha = f)
        for (k in 0 until 3) {
            val r = fract(t * 0.6f + k / 3f)
            drawLine(Color(0xFF9AD0F0), Offset(c.x - w * 0.04f + k * w * 0.04f, c.y + h * 0.15f + h * 0.04f * r), Offset(c.x - w * 0.03f + k * w * 0.04f, c.y + h * 0.18f + h * 0.04f * r), 1.5f * dp, alpha = f * (1f - r) * (1f - ash))
        }
        tagAt(a.tag("radiator"), Offset(w * 0.2f, h * 0.24f), seg(ra, 0.3f, 0.55f) * (1f - ash), back = Back35, ink = Color(0xFFFFD0C8))
    }
    // the sun and its seven horses
    if (sh > 0f) {
        val f = easeOut(seg(sh, 0f, 0.3f)) * (1f - fall)
        val run = seg(sh, 0f, 1f)
        val sun = Offset(w * (0.92f - 0.2f * run), h * 0.22f + h * 0.4f * fall)
        val col = lerpColor(MythC35.Sun, MythC35.Ash, ash)
        drawCircle(col, w * 0.04f, sun, alpha = f)
        if (ash < 1f) softGlow(sun, w * 0.08f, MythC35.Sun, 0.5f * f * (1f - ash))
        for (k in 0 until 7) {
            val foot = sun + Offset(-w * (0.08f + 0.05f * (k % 4)), h * 0.04f + h * 0.04f * (k / 4))
            horse35(foot, w * 0.045f, lerpColor(MythC35.Horse, MythC35.Ash, ash), f, t * 6f + k)
            drawLine(lerpColor(Color(0xFFE8C88A), MythC35.Ash, ash), foot + Offset(w * 0.02f, -h * 0.03f), sun, 0.8f * dp, alpha = 0.6f * f)
        }
        tagAt(a.tag("sevenhorses"), Offset(w * 0.72f, h * 0.08f), seg(sh, 0.3f, 0.55f) * (1f - ash), back = Back35, ink = Color(0xFFFFE0A8))
    }
    // the Book and the signs it speaks of
    val book = Offset(w * 0.5f, h * 0.7f)
    val bf = easeOut(seg(mv, 0f, 0.4f))
    if (bf > 0f) {
        openBook21(book, w * 0.18f, MythC35.Page, Color(0xFF8A6A3A), bf, glow = bf * (0.6f + 0.4f * easeOut(seg(cl, 0.4f, 0.9f))))
        val icons = 6
        for (k in 0 until icons) {
            val ang = t * 0.25f + k * 2f * PI.toFloat() / icons
            val p = book + Offset(cos(ang) * w * 0.15f, -h * 0.14f + sin(ang) * h * 0.07f)
            val f = easeOut(seg(mv, 0.2f + k * 0.08f, 0.45f + k * 0.08f))
            when (k % 3) {
                0 -> drawPath(Path().apply { moveTo(p.x, p.y - h * 0.02f); quadraticTo(p.x + w * 0.012f, p.y, p.x, p.y + h * 0.012f); quadraticTo(p.x - w * 0.012f, p.y, p.x, p.y - h * 0.02f) }, Color(0xFF8AC8E8), alpha = f)
                1 -> drawPath(starPath(p, w * 0.012f), Color(0xFFFFE6A8), alpha = f)
                else -> {
                    drawOval(Color(0xFF8A6A3A), Offset(p.x - w * 0.008f, p.y - h * 0.008f), Size(w * 0.016f, h * 0.016f), alpha = f)
                    drawLine(Color(0xFF6AA84A), p, p + Offset(w * 0.006f, -h * 0.02f), 1.5f * dp, alpha = f)
                }
            }
        }
    }
}

// ═════════════ 2. three gates (bs-dawabit) ═════════════

private object GateC35 {
    val Sky = Color(0xFF1E2A44)
    val SkyLow = Color(0xFF4A5A74)
    val Sea = Color(0xFF223248)
    val Land = Color(0xFF4A4038)
    val Tower = Color(0xFFE8E0D0)
    val Band = Color(0xFFC0443A)
    val Beam = Color(0xFFFFF0C0)
    val Arch = Color(0xFFB89A6A)
    val Orb = Color(0xFFE8F0FF)
    val Dim = Color(0xFF6A6A7A)
}

/**
 * Beats: beacon (a lighthouse, a book of guidance: its beam sweeps the sea, and in its margin small signs of the
 * world), gateone (a row of three arches; small lights — readings — come to the first, and the doubtful ones turn back:
 * «صِحَّةُ الْمَسْأَلَةِ»), gatetwo (at the second, the ones the text does not clearly point to turn back: «إِشَارَةٌ
 * صَرِيحَةٌ»), gatethree (what passes joins other lights in a tray, one meaning among several: «وَجْهٌ، لَا الْقَوْلُ
 * الْوَحِيدُ»).
 */
internal fun DrawScope.gatesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "beacon" to Shot(Offset(w * 0.25f, h * 0.45f), 1.3f),
        "gateone" to Shot(Offset(w * 0.5f, h * 0.6f), 1.3f),
        "gatetwo" to Shot(Offset(w * 0.66f, h * 0.6f), 1.3f),
        "gatethree" to Shot(Offset(w * 0.8f, h * 0.6f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { gatesShore(a) }
}

private fun DrawScope.gatesShore(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bc = a.b("beacon")
    val g1 = a.b("gateone")
    val g2 = a.b("gatetwo")
    val g3 = a.b("gatethree")
    val sea = h * 0.6f
    nightSky(t, GateC35.Sky, GateC35.SkyLow, sea, starAlpha = 0.6f)
    drawRect(GateC35.Sea, Offset(0f, sea), Size(w, h * 0.12f))
    drawRect(GateC35.Land, Offset(0f, h * 0.72f), Size(w, h * 0.28f))
    // the lighthouse
    val top = Offset(w * 0.2f, h * 0.24f)
    drawPath(Path().apply { moveTo(top.x - w * 0.025f, top.y); lineTo(top.x + w * 0.025f, top.y); lineTo(top.x + w * 0.045f, h * 0.74f); lineTo(top.x - w * 0.045f, h * 0.74f); close() }, GateC35.Tower)
    for (k in 0 until 3) drawRect(GateC35.Band, Offset(top.x - w * (0.03f + 0.005f * k), top.y + h * (0.1f + 0.14f * k)), Size(w * (0.06f + 0.01f * k), h * 0.04f))
    val lit = easeOut(seg(bc, 0f, 0.3f))
    if (lit > 0f) {
        val ang = PI.toFloat() * (0.15f + 0.1f * sin(t * 0.6f))
        lightShafts(top, -ang, 0.35f, w * 0.7f, GateC35.Beam, 0.7f * lit, t, rays = 5)
        drawCircle(GateC35.Beam, w * 0.015f, top, alpha = lit)
        softGlow(top, w * 0.05f, GateC35.Beam, lit)
        // small signs in the beam's margin
        for (k in 0 until 4) {
            val f = seg(bc, 0.4f + k * 0.1f, 0.6f + k * 0.1f)
            val p = Offset(w * (0.36f + 0.1f * k), h * (0.14f + 0.05f * (k % 2)))
            drawCircle(GateC35.Orb, w * 0.006f, p, alpha = 0.8f * f)
            drawOval(GateC35.Orb, Offset(p.x - w * 0.016f, p.y - h * 0.008f), Size(w * 0.032f, h * 0.016f), alpha = 0.6f * f, style = Stroke(1f * dp))
        }
    }
    // three arches on the shore road
    val arches = listOf(w * 0.5f, w * 0.66f, w * 0.82f)
    val gates = listOf(g1, g2, g3)
    val road = h * 0.8f
    for ((k, x) in arches.withIndex()) {
        val f = maxOf(0.4f * easeOut(seg(g1, 0f, 0.3f)), easeOut(seg(gates[k], 0f, 0.3f)))
        drawPath(archPath26(x, road - h * 0.2f, road, w * 0.04f), GateC35.Arch, alpha = f, style = Stroke(4f * dp))
        tagAt(a.tag(listOf("gateone", "gatetwo", "gatethree")[k], 0, 0), Offset(x, road - h * 0.3f), seg(gates[k], 0.3f, 0.6f), back = Back35, ink = ArtInk.BrassLight)
    }
    // the readings that come, and those that turn back
    if (g1 > 0f) {
        for (k in 0 until 6) {
            val stopAt = when (k) { 0, 3 -> 0; 1, 4 -> 1; else -> 2 }
            val p1 = easeInOut(seg(g1, 0.1f + k * 0.05f, 0.6f + k * 0.04f))
            val p2 = if (stopAt >= 1) easeInOut(seg(g2, 0.1f + k * 0.03f, 0.6f)) else 0f
            val p3 = if (stopAt >= 2) easeInOut(seg(g3, 0.1f + k * 0.03f, 0.6f)) else 0f
            var x = mix(w * 0.34f, arches[0], p1)
            if (p2 > 0f) x = mix(arches[0], arches[1], p2)
            if (p3 > 0f) x = mix(arches[1], w * 0.92f, p3)
            val turned = when (stopAt) {
                0 -> seg(g1, 0.75f, 1f)
                1 -> seg(g2, 0.75f, 1f)
                else -> 0f
            }
            if (stopAt == 2 && p3 >= 1f) continue
            val p = Offset(x - w * 0.12f * turned, road - h * (0.03f + 0.02f * (k % 3)) - h * 0.05f * sin(PI.toFloat() * turned))
            val col = lerpColor(GateC35.Orb, GateC35.Dim, turned)
            drawCircle(col, w * 0.009f, p, alpha = 1f - 0.6f * turned)
        }
    }
    // the tray: one meaning among several
    if (g3 > 0f) {
        val f = easeOut(seg(g3, 0.5f, 0.9f))
        val c = Offset(w * 0.92f, road - h * 0.02f)
        drawRoundRect(Color(0xFF8A6A3A), Offset(c.x - w * 0.06f, c.y - h * 0.01f), Size(w * 0.12f, h * 0.03f), CornerRadius(4f * density), alpha = f)
        for (k in 0 until 4) drawCircle(if (k == 1) GateC35.Orb else Color(0xFFE8D8B0), w * 0.009f, c + Offset(w * (-0.04f + 0.027f * k), -h * 0.02f), alpha = f)
    }
}

// ═════════════ 3. smoke, not water (bs-dukhan) ═════════════

private object NebC35 {
    val Space = Color(0xFF080A18)
    val SpaceLow = Color(0xFF1A1630)
    val Water = Color(0xFF1E3A5A)
    val Smoke = Color(0xFF8A7A88)
    val Warm = Color(0xFFE8A060)
    val Star = Color(0xFFFFF4D8)
    val Grid = Color(0xFF6A8AD0)
}

/**
 * Beats: primalsea (the old myths' first water — «سُومَرُ», «بَابِلُ», «مِصْرُ», «كَنْعَانُ» — dark waves at the bottom
 * of space), dukhan (it fades; in its place a cloud of smoke turns, warm-lit), condense (the smoke draws together into
 * stars and a glowing nebula), fabric (a grid spreads under the stars and bends round the heaviest, ripples running
 * through it), widen (the grid stretches and everything on it moves apart).
 */
internal fun DrawScope.nebulaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "primalsea" to Shot(Offset(w * 0.5f, h * 0.7f), 1.2f),
        "dukhan" to Shot(Offset(w * 0.5f, h * 0.45f), 1.2f),
        "condense" to Shot(Offset(w * 0.5f, h * 0.45f), 1.35f),
        "fabric" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "widen" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { nebulaSpace(a) }
}

private fun DrawScope.nebulaSpace(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ps = a.b("primalsea")
    val dk = a.b("dukhan")
    val cd = a.b("condense")
    val fb = a.b("fabric")
    val wd = a.b("widen")
    drawRect(Brush.verticalGradient(listOf(NebC35.Space, NebC35.SpaceLow)), size = Size(w, h))
    for (k in 0 until 40) drawCircle(Color.White, w * 0.0018f, Offset(w * w8Rnd(k, 3.3f), h * w8Rnd(k, 8.8f)), alpha = 0.4f + 0.3f * sin(t + k))
    val c = Offset(w * 0.5f, h * 0.45f)
    // the myths' first water
    val sea = easeOut(seg(ps, 0f, 0.3f)) * (1f - easeOut(seg(dk, 0f, 0.4f)))
    if (sea > 0f) {
        val y = h * 0.76f
        val wave = Path().apply {
            moveTo(0f, h)
            lineTo(0f, y)
            for (k in 0..40) {
                val x = w * k / 40f
                lineTo(x, y + sin(k * 0.6f + t * 1.5f) * h * 0.015f)
            }
            lineTo(w, h); close()
        }
        drawPath(wave, NebC35.Water, alpha = sea)
        for (k in 0 until 4) tagAt(a.tag("primalsea", 0, k), Offset(w * (0.2f + 0.2f * k), h * 0.66f), seg(ps, 0.2f + k * 0.12f, 0.4f + k * 0.12f) * sea, back = Back35, ink = Color(0xFFB8D8F0))
    }
    // the smoke, then the stars it draws into
    val smoke = easeOut(seg(dk, 0.1f, 0.6f))
    val pull = easeInOut(seg(cd, 0f, 0.8f))
    val wide = easeInOut(seg(wd, 0f, 0.9f))
    if (smoke > 0f) {
        for (k in 0 until 26) {
            val ang = k * 2.4f + t * 0.15f * (1f + (k % 3))
            val r = w * (0.06f + 0.18f * w8Rnd(k, 2.2f)) * (1f - 0.75f * pull)
            val p = c + Offset(cos(ang) * r, sin(ang) * r * 0.6f)
            drawCircle(lerpColor(NebC35.Smoke, NebC35.Warm, w8Rnd(k, 5.1f) * 0.6f), w * (0.05f + 0.03f * w8Rnd(k, 7.4f)) * (1f - 0.6f * pull), p, alpha = 0.22f * smoke * (1f - 0.7f * pull))
        }
    }
    val stars = (0 until 9).map { k ->
        val ang = k * 0.7f + 0.4f
        val r = w * (0.03f + 0.12f * w8Rnd(k, 6.6f))
        c + Offset(cos(ang) * r, sin(ang) * r * 0.6f)
    }
    if (pull > 0f) {
        softGlow(c, w * 0.12f, NebC35.Warm, 0.35f * pull * (1f - wide))
        for ((k, s0) in stars.withIndex()) {
            val s = c + (s0 - c) * (1f + 1.2f * wide)
            val f = seg(cd, 0.3f + k * 0.05f, 0.6f + k * 0.05f)
            drawCircle(NebC35.Star, w * (0.004f + 0.003f * (k % 3)), s, alpha = f)
            softGlow(s, w * 0.02f, NebC35.Star, 0.5f * f)
        }
    }
    // the fabric under the stars, bent by the heaviest, then stretched
    if (fb > 0f) {
        val f = easeOut(seg(fb, 0f, 0.4f))
        val heavy = c + (stars[4] - c) * (1f + 1.2f * wide)
        val spread = 1f + 0.8f * wide
        val rows = 7
        val cols = 11
        for (r in 0 until rows) {
            val path = Path()
            for (k in 0..cols) {
                val base = Offset(c.x + (k - cols / 2f) * w * 0.07f * spread, h * 0.66f + (r - rows / 2f) * h * 0.05f * spread)
                val d = (base - heavy).getDistance()
                val dip = h * 0.08f * f / (1f + (d / (w * 0.08f)) * (d / (w * 0.08f)))
                val ripple = h * 0.01f * sin(d / (w * 0.04f) - t * 3f) * seg(fb, 0.4f, 0.8f)
                val p = base + Offset(0f, dip + ripple)
                if (k == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
            }
            drawPath(path, NebC35.Grid, alpha = 0.5f * f, style = Stroke(1f * dp))
        }
        for (k in 0..cols) {
            val path = Path()
            for (r in 0 until rows) {
                val base = Offset(c.x + (k - cols / 2f) * w * 0.07f * spread, h * 0.66f + (r - rows / 2f) * h * 0.05f * spread)
                val d = (base - heavy).getDistance()
                val dip = h * 0.08f * f / (1f + (d / (w * 0.08f)) * (d / (w * 0.08f)))
                val p = base + Offset(0f, dip)
                if (r == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
            }
            drawPath(path, NebC35.Grid, alpha = 0.35f * f, style = Stroke(1f * dp))
        }
    }
}

// ═════════════ 4. the breathing morning, the darkened moon, the wave beneath (bs-ard) ═════════════

private object EarthC35 {
    val Night = Color(0xFF1A1E3A)
    val Dawn = Color(0xFFF2B888)
    val Land = Color(0xFF6A5A3E)
    val Warm = Color(0xFFFF9A5A)
    val Cool = Color(0xFF8AC8F0)
    val Lava = Color(0xFFFF6A2A)
    val Moon = Color(0xFFD8D8DE)
    val Maria = Color(0xFF8A8A96)
    val SeaTop = Color(0xFF3A7AA8)
    val SeaDeep = Color(0xFF14304A)
    val Drop = Color(0xFF9AD4F0)
}

/**
 * Beats: breathe (dawn over land: the ground warms, warm air curls up, cool clean air comes down — the morning
 * breathes), moonfade (high up, a molten moon cools: its fire goes out and its dark seas remain), innerwave (a cut
 * through the sea: small waves on top, and beneath them, between the light water and the deep, one huge slow wave),
 * waterlife (a drop of water, and in it a sprout and a small fish).
 */
internal fun DrawScope.earthmoonScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "breathe" to Shot(Offset(w * 0.22f, h * 0.6f), 1.35f),
        "moonfade" to Shot(Offset(w * 0.5f, h * 0.25f), 1.45f),
        "innerwave" to Shot(Offset(w * 0.78f, h * 0.68f), 1.35f),
        "waterlife" to Shot(Offset(w * 0.5f, h * 0.6f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { earthPanorama(a) }
}

private fun DrawScope.earthPanorama(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val br = a.b("breathe")
    val mf = a.b("moonfade")
    val iw = a.b("innerwave")
    val wl = a.b("waterlife")
    val dawn = easeOut(seg(br, 0f, 0.5f))
    drawRect(Brush.verticalGradient(listOf(EarthC35.Night, lerpColor(EarthC35.Night, EarthC35.Dawn, dawn))), size = Size(w, h * 0.55f))
    // land, left; sea, right
    drawRect(lerpColor(Color(0xFF2E2820), EarthC35.Land, dawn), Offset(0f, h * 0.55f), Size(w * 0.56f, h * 0.45f))
    val seaX = w * 0.56f
    drawRect(Brush.verticalGradient(listOf(EarthC35.SeaTop, EarthC35.SeaDeep), startY = h * 0.55f, endY = h), Offset(seaX, h * 0.55f), Size(w - seaX, h * 0.45f))
    // the morning breath
    if (br > 0f) {
        val warm = easeOut(seg(br, 0.2f, 0.6f))
        drawRect(EarthC35.Warm, Offset(0f, h * 0.55f), Size(w * 0.56f, h * 0.02f), alpha = 0.5f * warm)
        for (k in 0 until 4) {
            val x = w * (0.08f + 0.12f * k)
            val r = fract(t * 0.35f + k * 0.25f)
            val up = Path().apply {
                moveTo(x, h * 0.55f)
                cubicTo(x + w * 0.03f, h * 0.5f, x - w * 0.03f, h * 0.43f, x + w * 0.01f, h * 0.55f - h * 0.25f * r - h * 0.05f)
            }
            drawPath(up, EarthC35.Warm, alpha = 0.7f * warm * (1f - r), style = Stroke(2f * dp, cap = StrokeCap.Round))
            val dx = x + w * 0.06f
            drawLine(EarthC35.Cool, Offset(dx, h * 0.25f + h * 0.2f * r), Offset(dx, h * 0.3f + h * 0.2f * r), 2f * dp, StrokeCap.Round, alpha = 0.7f * warm * (1f - r))
            arrowHead(Offset(dx, h * 0.3f + h * 0.2f * r), Offset(0f, 1f), w * 0.01f, EarthC35.Cool, 2f * dp, 0.7f * warm * (1f - r))
        }
    }
    // the moon, molten, then cooled
    if (mf > 0f) {
        val f = easeOut(seg(mf, 0f, 0.25f))
        val cool = easeInOut(seg(mf, 0.25f, 0.85f))
        val c = Offset(w * 0.5f, h * 0.22f)
        val r = w * 0.06f
        drawCircle(lerpColor(EarthC35.Lava, EarthC35.Moon, cool), r, c, alpha = f)
        softGlow(c, r * 2f, EarthC35.Lava, 0.7f * f * (1f - cool))
        for (k in 0 until 4) {
            val p = c + Offset(cos(k * 1.7f) * r * 0.45f, sin(k * 1.7f) * r * 0.4f)
            drawCircle(lerpColor(Color(0xFFFFD24A), EarthC35.Maria, cool), r * (0.18f + 0.06f * (k % 2)), p, alpha = f)
        }
    }
    // the wave beneath the waves
    if (iw > 0f) {
        val f = easeOut(seg(iw, 0f, 0.3f))
        val top = h * 0.55f
        val layer = h * 0.74f
        val surf = Path().apply {
            moveTo(seaX, top)
            for (k in 0..20) lineTo(seaX + (w - seaX) * k / 20f, top + sin(k * 1.4f + t * 3f) * h * 0.006f)
        }
        drawPath(surf, Color.White, alpha = 0.7f * f, style = Stroke(1.5f * dp))
        val deep = Path().apply {
            moveTo(seaX, h)
            for (k in 0..30) {
                val x = seaX + (w - seaX) * k / 30f
                lineTo(x, layer + sin(k * 0.2f - t * 0.8f) * h * 0.07f * f)
            }
            lineTo(w, h); close()
        }
        drawPath(deep, Color(0xFF0A1A2C), alpha = 0.85f * f)
    }
    // a drop of water, and life in it
    if (wl > 0f) {
        val f = easeOut(seg(wl, 0f, 0.4f))
        val c = Offset(w * 0.46f, h * 0.68f)
        val r = w * 0.06f
        val drop = Path().apply {
            moveTo(c.x, c.y - r * 1.6f)
            cubicTo(c.x + r * 0.5f, c.y - r * 0.9f, c.x + r, c.y - r * 0.2f, c.x + r, c.y + r * 0.2f)
            cubicTo(c.x + r, c.y + r * 0.9f, c.x - r, c.y + r * 0.9f, c.x - r, c.y + r * 0.2f)
            cubicTo(c.x - r, c.y - r * 0.2f, c.x - r * 0.5f, c.y - r * 0.9f, c.x, c.y - r * 1.6f)
        }
        drawPath(drop, EarthC35.Drop, alpha = 0.75f * f)
        drawPath(drop, Color.White, alpha = 0.6f * f, style = Stroke(1.5f * dp))
        val g = seg(wl, 0.4f, 0.8f)
        drawLine(Color(0xFF4A9A4A), c + Offset(-r * 0.3f, r * 0.5f), c + Offset(-r * 0.3f, r * 0.5f - r * 0.6f * g), 2f * dp, StrokeCap.Round, alpha = f)
        drawOval(Color(0xFF6AB84A), Offset(c.x - r * 0.3f, c.y + r * 0.5f - r * 0.6f * g - r * 0.1f), Size(r * 0.25f * g, r * 0.14f * g), alpha = f)
        val fish = c + Offset(r * 0.3f + r * 0.1f * sin(t * 2f), -r * 0.1f)
        drawOval(Color(0xFFE88A4A), Offset(fish.x - r * 0.15f, fish.y - r * 0.07f), Size(r * 0.3f * g, r * 0.14f * g), alpha = f)
        glint(c + Offset(-r * 0.4f, -r * 0.5f), r * 0.12f, Color.White, f)
    }
}

// ═════════════ 5. two waters mingled (bs-amshaj) ═════════════

private object WaterC35 {
    val Bg = Color(0xFF1E1A2E)
    val BgLow = Color(0xFF3A2E44)
    val Milk = Color(0xFFF6F2E8)
    val Curd = Color(0xFFE8D8A8)
    val Bowl = Color(0xFF8A5A3A)
    val One = Color(0xFF8AB8E8)
    val Two = Color(0xFFE8A0B8)
    val Mixed = Color(0xFFD8C0E8)
    val Spark = Color(0xFFFFF0C0)
    val Red = Color(0xFFD23C30)
}

/**
 * Beats: curdle (Aristotle's picture: milk in a bowl setting into cheese — the man's part working on the woman's as a
 * passive vessel; struck through: «أَرِسْطُو»), mixing (two streams of different colour braid into one drop),
 * extract (from a pool of countless specks, one bright speck is drawn out), stages (three shapes in turn: a drop, a
 * small clinging form, a chewed-looking lump — «نُطْفَةٌ», «عَلَقَةٌ», «مُضْغَةٌ»).
 */
internal fun DrawScope.twowatersScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "curdle" to Shot(Offset(w * 0.2f, h * 0.55f), 1.45f),
        "mixing" to Shot(Offset(w * 0.45f, h * 0.45f), 1.3f),
        "extract" to Shot(Offset(w * 0.45f, h * 0.7f), 1.4f),
        "stages" to Shot(Offset(w * 0.75f, h * 0.5f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { twoWaters(a) }
}

private fun DrawScope.twoWaters(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cu = a.b("curdle")
    val mx = a.b("mixing")
    val ex = a.b("extract")
    val sg = a.b("stages")
    drawRect(Brush.verticalGradient(listOf(WaterC35.Bg, WaterC35.BgLow)), size = Size(w, h))
    // the bowl of milk turning to curd
    if (cu > 0f) {
        val f = easeOut(seg(cu, 0f, 0.25f))
        val set = easeInOut(seg(cu, 0.2f, 0.7f))
        val c = Offset(w * 0.2f, h * 0.62f)
        drawArc(WaterC35.Bowl, 0f, 180f, true, Offset(c.x - w * 0.09f, c.y - h * 0.08f), Size(w * 0.18f, h * 0.16f), alpha = f)
        drawOval(lerpColor(WaterC35.Milk, WaterC35.Curd, set), Offset(c.x - w * 0.085f, c.y - h * 0.02f), Size(w * 0.17f, h * 0.04f), alpha = f)
        if (set > 0f) for (k in 0 until 5) drawCircle(WaterC35.Curd, w * 0.012f, c + Offset(w * (-0.05f + 0.025f * k), -h * 0.01f - h * 0.01f * (k % 2)), alpha = f * set)
        struckTag21(a.tag("curdle"), Offset(w * 0.2f, h * 0.42f), seg(cu, 0.3f, 0.5f), seg(cu, 0.6f, 0.95f), WaterC35.Red, Back35, Color(0xFFE8D8C8))
    }
    // two streams braid into one drop
    val drop = Offset(w * 0.45f, h * 0.55f)
    if (mx > 0f) {
        val run = easeInOut(seg(mx, 0f, 0.7f))
        for (s in 0 until 2) {
            val path = Path()
            for (k in 0..24) {
                val u = k / 24f * run
                val x = drop.x + (if (s == 0) -1f else 1f) * w * 0.05f * (1f - u) * cos(u * 6f)
                val y = h * 0.12f + (drop.y - h * 0.12f) * u
                if (k == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path, if (s == 0) WaterC35.One else WaterC35.Two, alpha = 0.85f, style = Stroke(3f * dp, cap = StrokeCap.Round))
        }
        val d = easeOut(seg(mx, 0.65f, 0.95f))
        drawCircle(WaterC35.Mixed, w * 0.025f * d, drop, alpha = d)
        softGlow(drop, w * 0.05f, WaterC35.Mixed, 0.5f * d)
    }
    // one speck drawn from a pool of countless
    if (ex > 0f) {
        val f = easeOut(seg(ex, 0f, 0.3f))
        val pool = Offset(w * 0.45f, h * 0.8f)
        drawOval(Color(0xFF4A5A7A), Offset(pool.x - w * 0.12f, pool.y - h * 0.04f), Size(w * 0.24f, h * 0.08f), alpha = 0.7f * f)
        for (k in 0 until 50) {
            val p = pool + Offset(w * 0.11f * (w8Rnd(k, 2.7f) * 2f - 1f), h * 0.03f * (w8Rnd(k, 9.3f) * 2f - 1f))
            drawCircle(Color(0xFFC8D0E8), w * 0.0025f, p + Offset(w * 0.003f * sin(t * 3f + k), 0f), alpha = 0.6f * f)
        }
        val up = easeInOut(seg(ex, 0.35f, 0.9f))
        val sp = pool + (drop + Offset(0f, h * 0.06f) - pool) * up
        drawCircle(WaterC35.Spark, w * 0.006f, sp, alpha = f)
        softGlow(sp, w * 0.02f, WaterC35.Spark, f)
    }
    // three stages
    if (sg > 0f) {
        val xs = listOf(w * 0.64f, w * 0.76f, w * 0.88f)
        for (k in 0 until 3) {
            val f = easeOut(seg(sg, 0.1f + k * 0.2f, 0.35f + k * 0.2f))
            if (f <= 0f) continue
            val c = Offset(xs[k], h * 0.5f)
            when (k) {
                0 -> drawCircle(WaterC35.Mixed, w * 0.018f, c, alpha = f)
                1 -> {
                    val p = Path().apply {
                        moveTo(c.x - w * 0.03f, c.y + h * 0.02f)
                        cubicTo(c.x - w * 0.02f, c.y - h * 0.06f, c.x + w * 0.03f, c.y - h * 0.05f, c.x + w * 0.025f, c.y + h * 0.03f)
                    }
                    drawPath(p, Color(0xFFC86A6A), alpha = f, style = Stroke(w * 0.016f, cap = StrokeCap.Round))
                }
                else -> {
                    drawOval(Color(0xFFD08A7A), Offset(c.x - w * 0.035f, c.y - h * 0.05f), Size(w * 0.07f, h * 0.1f), alpha = f)
                    for (j in 0 until 5) drawArc(Color(0xFF9A5A50), 200f, 140f, false, Offset(c.x - w * 0.02f, c.y - h * 0.04f + j * h * 0.016f), Size(w * 0.04f, h * 0.02f), alpha = f, style = Stroke(1.2f * dp))
                }
            }
            if (k < 2) arrowHead(Offset(xs[k] + w * 0.05f, h * 0.5f), Offset(1f, 0f), w * 0.012f, ArtInk.BrassLight, 1.5f * dp, f)
            tagAt(a.tag("stages", 0, k), Offset(xs[k], h * 0.66f), seg(sg, 0.2f + k * 0.2f, 0.45f + k * 0.2f), back = Back35, ink = ArtInk.Ivory)
        }
    }
}

// ═════════════ 6. the root of the tail (bs-ajb) ═════════════

private object SpineC35 {
    val Soil = Color(0xFF3A2C22)
    val SoilLow = Color(0xFF2A1E18)
    val Bone = Color(0xFFE8DCC0)
    val Gold = Color(0xFFF2C45A)
    val Disc = Color(0xFFE8B0A0)
    val Streak = Color(0xFF8A3A4A)
    val Dot = Color(0xFFE8D8B0)
}

/**
 * Beats: coccyx (a cut through the soil: a spine fading into earth, all but the small bone at its base, which keeps
 * a gold glow: «عَجْبُ الذَّنَبِ»), streak (an embryonic disc, a line drawn down its middle, cells spreading from it:
 * «الشَّرِيطُ الْأَوَّلِيُّ»), sinks (the line shrinks to the lower end of the disc, where the same small bone glows),
 * joints (a ring of 360 small dots fills round the spine, «360»).
 */
internal fun DrawScope.spineScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "coccyx" to Shot(Offset(w * 0.3f, h * 0.62f), 1.4f),
        "streak" to Shot(Offset(w * 0.68f, h * 0.45f), 1.4f),
        "sinks" to Shot(Offset(w * 0.68f, h * 0.55f), 1.35f),
        "joints" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { spineSoil(a) }
}

private fun DrawScope.spineSoil(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cc = a.b("coccyx")
    val sk = a.b("streak")
    val sn = a.b("sinks")
    val jt = a.b("joints")
    drawRect(Brush.verticalGradient(listOf(SpineC35.Soil, SpineC35.SoilLow)), size = Size(w, h))
    for (r in 0 until 5) drawLine(Color(0xFF4A3A2E), Offset(0f, h * (0.2f + 0.16f * r)), Offset(w, h * (0.22f + 0.16f * r)), 1f * dp, alpha = 0.6f)
    // a spine fading into the earth; the small bone at its base stays
    val spineX = w * 0.3f
    if (cc > 0f) {
        val f = easeOut(seg(cc, 0f, 0.25f))
        val fade = easeOut(seg(cc, 0.3f, 0.8f))
        for (k in 0 until 14) {
            val y = h * (0.2f + 0.042f * k)
            drawRoundRect(SpineC35.Bone, Offset(spineX - w * (0.022f - 0.0008f * k), y), Size(w * (0.044f - 0.0016f * k), h * 0.032f), CornerRadius(3f * dp), alpha = f * (1f - fade))
            if (fade > 0f) for (d in 0 until 3) drawCircle(Color(0xFF6A5A48), w * 0.004f, Offset(spineX + w * 0.02f * sin(k + d * 2f), y + h * 0.015f + h * 0.02f * fade * d), alpha = 0.5f * fade * (1f - fade))
        }
        val base = Offset(spineX, h * 0.82f)
        drawPath(Path().apply { moveTo(base.x - w * 0.012f, base.y - h * 0.02f); lineTo(base.x + w * 0.012f, base.y - h * 0.02f); lineTo(base.x, base.y + h * 0.03f); close() }, SpineC35.Gold, alpha = f)
        softGlow(base, w * 0.04f, SpineC35.Gold, 0.6f * f)
        tagAt(a.tag("coccyx"), base + Offset(0f, -h * 0.12f), seg(cc, 0.5f, 0.75f) * (1f - seg(jt, 0f, 0.3f)), back = Back35, ink = ArtInk.Gold)
    }
    // the embryonic disc and its line
    val disc = Offset(w * 0.68f, h * 0.45f)
    if (sk > 0f) {
        val f = easeOut(seg(sk, 0f, 0.3f)) * (1f - easeOut(seg(jt, 0f, 0.3f)))
        drawOval(SpineC35.Disc, Offset(disc.x - w * 0.09f, disc.y - h * 0.16f), Size(w * 0.18f, h * 0.32f), alpha = 0.85f * f)
        val draw = easeOut(seg(sk, 0.2f, 0.6f))
        val shrink = easeInOut(seg(sn, 0f, 0.8f))
        val top = disc.y - h * 0.1f + h * 0.2f * shrink
        val bottom = disc.y + h * 0.12f
        drawLine(SpineC35.Streak, Offset(disc.x, bottom), Offset(disc.x, bottom - (bottom - top) * draw), w * 0.008f, StrokeCap.Round, alpha = f)
        if (draw > 0.5f) for (k in 0 until 8) {
            val r = fract(t * 0.4f + k / 8f)
            val side = if (k % 2 == 0) 1f else -1f
            drawCircle(SpineC35.Streak, w * 0.004f, Offset(disc.x + side * w * 0.07f * r, mix(top, bottom, (k / 8f))), alpha = 0.6f * f * (1f - r) * (1f - shrink))
        }
        if (sn > 0f) {
            val g = easeOut(seg(sn, 0.6f, 1f))
            softGlow(Offset(disc.x, bottom), w * 0.03f, SpineC35.Gold, 0.8f * g * f)
            drawCircle(SpineC35.Gold, w * 0.008f, Offset(disc.x, bottom), alpha = g * f)
        }
        tagAt(a.tag("streak"), disc + Offset(0f, -h * 0.24f), seg(sk, 0.4f, 0.65f) * (1f - seg(jt, 0f, 0.2f)), back = Back35, ink = Color(0xFFFFD8D0))
    }
    // three hundred and sixty joints
    if (jt > 0f) {
        val c = Offset(w * 0.5f, h * 0.5f)
        val n = (360 * easeInOut(seg(jt, 0.1f, 0.85f))).toInt()
        val r = h * 0.38f
        for (k in 0 until n) {
            val ang = -PI.toFloat() / 2f + 2f * PI.toFloat() * k / 360f
            drawCircle(SpineC35.Dot, w * 0.0022f, c + Offset(cos(ang) * r, sin(ang) * r), alpha = 0.9f)
        }
        tagAt(a.tag("joints"), c, seg(jt, 0.6f, 0.9f), back = Back35, ink = ArtInk.Gold)
    }
}
