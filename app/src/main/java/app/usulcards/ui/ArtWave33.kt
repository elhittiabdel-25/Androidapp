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
 * Wave 33 (2026-10-27): «بصائر»، الجزء الثاني، «بشارات الكتب السابقة» (1). One tree and its two branches, the sons of
 * Israel and the sons of Ishmael; three peaks lit one after another; the land of Kedar and the hill of Sela; a house
 * in a dry valley that is never shut; and a vineyard handed to new keepers, beside a wall whose rejected stone becomes
 * its corner.
 */

private val Back33 = Color(0xE6181420)

// ═════════════ 1. the two branches (bs-musa) ═════════════

private object TreeC33 {
    val Sky = Color(0xFF2A2240)
    val SkyLow = Color(0xFF7A5A6A)
    val Ground = Color(0xFF3A2E2A)
    val Bark = Color(0xFF6A4A32)
    val BarkLight = Color(0xFF8A6442)
    val Leaf = Color(0xFF4E7A4A)
    val Candle = Color(0xFFF2E6CC)
    val Flame = Color(0xFFFFB45A)
}

private fun branchPts33(w: Float, h: Float, right: Boolean): List<Offset> {
    val s = if (right) 1f else -1f
    return w8Smooth(listOf(Offset(w * 0.5f, h * 0.62f), Offset(w * (0.5f + 0.06f * s), h * 0.5f), Offset(w * (0.5f + 0.18f * s), h * 0.36f), Offset(w * (0.5f + 0.3f * s), h * 0.24f)), 8)
}

/**
 * Beats: twobranch (one trunk from one root splits into two great branches: «بَنُو إِسْرَائِيلَ» and «بَنُو
 * إِسْمَاعِيلَ» — the brethren), ishmaellight (at the tip of Ishmael's branch, his light), likemusa (at the tip of the
 * other, Moses' light with his name; between the two, what they share: «شَرِيعَةٌ», «جِهَادٌ»), falsefade (below, a
 * candle that claims to be a light gutters and goes out), fulfilled (three seals along his branch light up one by
 * one: «وَقَعَ»).
 */
internal fun DrawScope.brethrenScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twobranch" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "ishmaellight" to Shot(Offset(w * 0.72f, h * 0.35f), 1.35f),
        "likemusa" to Shot(Offset(w * 0.5f, h * 0.35f), 1.15f),
        "falsefade" to Shot(Offset(w * 0.36f, h * 0.56f), 1.1f),
        "fulfilled" to Shot(Offset(w * 0.68f, h * 0.45f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { brethrenTree(a) }
}

private fun DrawScope.brethrenTree(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tb = a.b("twobranch")
    val il = a.b("ishmaellight")
    val lm = a.b("likemusa")
    val ff = a.b("falsefade")
    val fu = a.b("fulfilled")
    val ground = h * 0.84f
    nightSky(t, TreeC33.Sky, TreeC33.SkyLow, ground, starAlpha = 0.5f)
    drawRect(TreeC33.Ground, Offset(0f, ground), Size(w, h - ground))
    // trunk and root
    val grow = easeOut(seg(tb, 0f, 0.4f))
    drawLine(TreeC33.Bark, Offset(w * 0.5f, ground), Offset(w * 0.5f, ground - (ground - h * 0.62f) * grow), w * 0.04f, StrokeCap.Round)
    for (k in 0 until 4) {
        val ang = PI.toFloat() * (0.15f + 0.23f * k)
        drawLine(TreeC33.Bark, Offset(w * 0.5f, ground), Offset(w * 0.5f, ground) + Offset(cos(ang) * w * 0.08f, sin(ang) * h * 0.05f) * grow, w * 0.012f, StrokeCap.Round)
    }
    // the two branches
    for (side in 0 until 2) {
        val pts = branchPts33(w, h, side == 1)
        val run = easeOut(seg(tb, 0.3f + side * 0.1f, 0.8f + side * 0.1f))
        val head = w8Head(pts, run)
        for (i in 1 until head.size) {
            val u = i / pts.size.toFloat()
            drawLine(if (side == 1) TreeC33.BarkLight else TreeC33.Bark, head[i - 1], head[i], w * (0.03f - 0.018f * u), StrokeCap.Round)
        }
        if (run > 0.5f) for (k in 0 until 6) {
            val p = w8Along(pts, 0.35f + 0.1f * k)
            drawCircle(TreeC33.Leaf, w * 0.025f, p + Offset(w * 0.02f * sin(k * 2.1f), -h * 0.03f), alpha = 0.8f * seg(run, 0.5f, 1f))
        }
        tagAt(a.tag("twobranch", 0, side), w8Along(pts, 0.55f) + Offset(if (side == 1) w * 0.12f else -w * 0.12f, h * 0.06f),
            seg(tb, 0.6f + side * 0.15f, 0.85f + side * 0.15f) * (1f - seg(lm, 0.4f, 0.7f)), back = Back33, ink = ArtInk.Ivory)
    }
    val tipR = branchPts33(w, h, true).last()
    val tipL = branchPts33(w, h, false).last()
    // his light at the tip of Ishmael's branch
    sirajMunir(tipR + Offset(0f, -h * 0.04f), w * 0.012f, easeOut(seg(il, 0.1f, 0.5f)), t, nameAt = tipR + Offset(0f, -h * 0.16f))
    // Moses' light at the other tip, and what the two share
    if (lm > 0f) {
        nabiLight14(tipL + Offset(0f, -h * 0.04f), w * 0.01f, easeOut(seg(lm, 0f, 0.4f)), t, a.tag("likemusa", 0, 0), tipL + Offset(0f, -h * 0.16f))
        val bridge = easeInOut(seg(lm, 0.3f, 0.7f))
        val y = h * 0.15f
        drawLine(ArtInk.BrassLight, Offset(tipL.x + w * 0.05f, y), Offset(tipL.x + w * 0.05f + (tipR.x - tipL.x - w * 0.1f) * bridge, y), 1.5f * dp, alpha = 0.7f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        for (k in 0 until 2) {
            tagAt(a.tag("likemusa", 0, 1 + k), Offset(w * (0.38f + 0.14f * k), y + h * 0.07f), seg(lm, 0.55f + 0.15f * k, 0.8f + 0.15f * k), back = Back33, ink = ArtInk.Gold)
        }
    }
    // a false light: a candle that goes out
    if (ff > 0f) {
        val f = easeOut(seg(ff, 0f, 0.2f))
        val c = Offset(w * 0.18f, ground - h * 0.02f)
        drawRect(TreeC33.Candle, Offset(c.x - w * 0.012f, c.y - h * 0.12f), Size(w * 0.024f, h * 0.12f), alpha = f)
        val out = seg(ff, 0.35f, 0.75f)
        val flick = 0.8f + 0.2f * sin(t * 13f)
        val flameAt = Offset(c.x, c.y - h * 0.15f)
        drawOval(TreeC33.Flame, Offset(flameAt.x - w * 0.008f, flameAt.y - h * 0.035f * (1f - out)), Size(w * 0.016f, h * 0.05f * (1f - out) + 0.1f), alpha = f * flick * (1f - out))
        softGlow(flameAt, w * 0.04f, TreeC33.Flame, 0.5f * f * (1f - out))
        for (k in 0 until 3) {
            val s = fract(t * 0.5f + k / 3f)
            drawCircle(Color(0xFF8A8A9A), w * (0.006f + 0.01f * s), flameAt + Offset(w * 0.01f * sin(t + k), -h * 0.08f * s), alpha = 0.5f * out * (1f - s))
        }
    }
    // what he foretold, fulfilled
    if (fu > 0f) {
        val pts = branchPts33(w, h, true)
        for (k in 0 until 3) {
            val p = w8Along(pts, 0.35f + 0.22f * k) + Offset(w * 0.04f, h * 0.02f)
            val f = seg(fu, 0.1f + 0.2f * k, 0.4f + 0.2f * k)
            check21(p, w * 0.016f, Color(0xFF2E9A55), f, 2.5f * dp)
        }
        tagAt(a.tag("fulfilled"), Offset(w * 0.86f, h * 0.5f), seg(fu, 0.6f, 0.85f), back = Back33, ink = Color(0xFFB8F0C8))
    }
}

// ═════════════ 2. three peaks (bs-faran) ═════════════

private object PeakC33 {
    val Night = Color(0xFF1A1A30)
    val Dawn = Color(0xFFE8B888)
    val Day = Color(0xFFBCD4EA)
    val Rock = listOf(Color(0xFF6A5A66), Color(0xFF7A6A5A), Color(0xFF8A6A48))
    val Plain = Color(0xFF4A3E3A)
    val Tablet = Color(0xFFD8D0C0)
    val Olive = Color(0xFF6A8A4A)
    val Fig = Color(0xFF7A4A6A)
}

/**
 * Beats: sinai (night; on the first peak two stone tablets glow: «سِينَاءُ»), seir (dawn; on the second a book glows:
 * «سَعِيرُ»), paran (full light over the third and highest: his light rises over «فَارَانُ»), names3 (beneath each,
 * whose land it is: «مُوسَى», «عِيسَى», «إِسْمَاعِيلُ»), tin (above, the three oaths of Surat al-Tin, each over its
 * land: «التِّينُ وَالزَّيْتُونُ», «طُورُ سِينِينَ», «الْبَلَدُ الْأَمِينُ»).
 */
internal fun DrawScope.threepeaksScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sinai" to Shot(Offset(w * 0.2f, h * 0.55f), 1.4f),
        "seir" to Shot(Offset(w * 0.48f, h * 0.55f), 1.35f),
        "paran" to Shot(Offset(w * 0.78f, h * 0.45f), 1.3f),
        "names3" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "tin" to Shot(Offset(w * 0.5f, h * 0.4f), 1.02f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { threePeaks(a) }
}

private fun DrawScope.threePeaks(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val si = a.b("sinai")
    val se = a.b("seir")
    val pa = a.b("paran")
    val n3 = a.b("names3")
    val tn = a.b("tin")
    val dawn = easeOut(seg(se, 0f, 0.6f))
    val day = easeOut(seg(pa, 0f, 0.6f))
    val top = lerpColor(lerpColor(PeakC33.Night, PeakC33.Dawn, dawn), PeakC33.Day, day)
    drawRect(Brush.verticalGradient(listOf(top, lerpColor(Color(0xFF3A2E40), Color(0xFFF2DCC0), maxOf(dawn, day))), endY = h * 0.8f), size = Size(w, h))
    val base = h * 0.82f
    val peaks = listOf(Offset(w * 0.2f, h * 0.42f), Offset(w * 0.48f, h * 0.38f), Offset(w * 0.78f, h * 0.28f))
    for ((k, p) in peaks.withIndex()) {
        drawPath(Path().apply { moveTo(p.x - w * 0.2f, base); lineTo(p.x - w * 0.03f, p.y + h * 0.03f); lineTo(p.x, p.y); lineTo(p.x + w * 0.04f, p.y + h * 0.04f); lineTo(p.x + w * 0.2f, base); close() },
            lerpColor(PeakC33.Rock[k], Color(0xFF1A1820), (1f - maxOf(dawn, day)) * 0.5f))
    }
    drawRect(PeakC33.Plain, Offset(0f, base), Size(w, h - base))
    // the tablets on the first peak
    if (si > 0f) {
        val f = easeOut(seg(si, 0f, 0.4f))
        val c = peaks[0] + Offset(0f, -h * 0.06f)
        softGlow(c, w * 0.07f, Color(0xFFFFF2D0), 0.6f * f)
        for (s in listOf(-1f, 1f)) {
            val o = Offset(c.x + s * w * 0.016f, c.y)
            drawRoundRect(PeakC33.Tablet, Offset(o.x - w * 0.014f, o.y - h * 0.04f), Size(w * 0.028f, h * 0.07f), CornerRadius(w * 0.014f, w * 0.014f), alpha = f)
            for (r in 0 until 3) drawLine(Color(0xFF8A8070), Offset(o.x - w * 0.008f, o.y - h * 0.015f + r * h * 0.012f), Offset(o.x + w * 0.008f, o.y - h * 0.015f + r * h * 0.012f), 1f * dp, alpha = f)
        }
        tagAt(a.tag("sinai"), peaks[0] + Offset(0f, h * 0.16f), seg(si, 0.35f, 0.6f), back = Back33, ink = ArtInk.Ivory)
    }
    // the book on the second
    if (se > 0f) {
        val f = easeOut(seg(se, 0.1f, 0.5f))
        openBook21(peaks[1] + Offset(0f, -h * 0.07f), w * 0.07f, Color(0xFFF2E8D2), Color(0xFF8A7A5A), f, glow = f)
        tagAt(a.tag("seir"), peaks[1] + Offset(0f, h * 0.16f), seg(se, 0.35f, 0.6f), back = Back33, ink = ArtInk.Ivory)
    }
    // his light over the third
    if (pa > 0f) {
        val rise = easeOut(seg(pa, 0f, 0.6f))
        sirajMunir(peaks[2] + Offset(0f, -h * 0.04f - h * 0.08f * rise), w * 0.013f, rise, t, nameAt = peaks[2] + Offset(w * 0.1f, h * 0.06f))
        tagAt(a.tag("paran"), peaks[2] + Offset(0f, h * 0.2f), seg(pa, 0.4f, 0.65f), back = Back33, ink = ArtInk.Gold)
    }
    // whose land each is
    for (k in 0 until 3) {
        tagAt(a.tag("names3", 0, k), Offset(peaks[k].x, base + h * 0.08f), seg(n3, 0.1f + k * 0.2f, 0.35f + k * 0.2f), back = Back33, ink = ArtInk.BrassLight)
    }
    // the three oaths, each over its land
    if (tn > 0f) {
        val over = listOf(peaks[1], peaks[0], peaks[2])
        for (k in 0 until 3) {
            val f = easeOut(seg(tn, 0.1f + k * 0.2f, 0.35f + k * 0.2f))
            val c = Offset(over[k].x, h * 0.1f)
            when (k) {
                0 -> {
                    drawCircle(PeakC33.Fig, w * 0.014f, c + Offset(-w * 0.07f, h * 0.06f), alpha = f)
                    drawLine(PeakC33.Olive, c + Offset(-w * 0.04f, h * 0.08f), c + Offset(-w * 0.02f, h * 0.04f), 1.6f * dp, alpha = f)
                    drawOval(PeakC33.Olive, Offset(c.x - w * 0.035f, c.y + h * 0.045f), Size(w * 0.014f, h * 0.02f), alpha = f)
                }
                1 -> drawPath(Path().apply { moveTo(c.x - w * 0.09f, c.y + h * 0.08f); lineTo(c.x - w * 0.07f, c.y + h * 0.04f); lineTo(c.x - w * 0.05f, c.y + h * 0.08f); close() }, PeakC33.Rock[0], alpha = f)
                else -> drawRect(Color(0xFF14121A), Offset(c.x - w * 0.085f, c.y + h * 0.045f), Size(w * 0.028f, h * 0.035f), alpha = f)
            }
            tagAt(a.tag("tin", 0, k), c + Offset(w * 0.02f, h * 0.06f), f, back = Back33, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 3. the land of Kedar and the hill of Sela (bs-qaydar) ═════════════

private object KedarC33 {
    val Sky = Color(0xFF3A3058)
    val SkyLow = Color(0xFFD89A7A)
    val Sand = Color(0xFFC8A26A)
    val SandDark = Color(0xFF9A7A48)
    val Stall = listOf(Color(0xFFB8463A), Color(0xFF3A6A9A), Color(0xFFD8A83A), Color(0xFF5A8A4A))
    val House = Color(0xFFD8C098)
    val Hill = Color(0xFF6A5A52)
    val Lid = Color(0xFF8A6A58)
}

/**
 * Beats: servant (dusk over the desert; his light appears, held up from beneath by a soft glow: «عَبْدِي الَّذِي
 * أَعْضُدُهُ»), quietstreet (a market street, its stalls loud with rings of noise; his light passes along it with no
 * noise of its own: «لَا يَصِيحُ»), eyesopen (a row of closed eyes opens to the light), kedarhomes (the houses and the
 * House of Mecca, where Kedar lived: «قِيدَارُ»), selasong (rings of voice rise from the top of a hill: «سَالِعُ»).
 */
internal fun DrawScope.kedarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "servant" to Shot(Offset(w * 0.5f, h * 0.35f), 1.3f),
        "quietstreet" to Shot(Offset(w * 0.5f, h * 0.72f), 1.35f),
        "eyesopen" to Shot(Offset(w * 0.46f, h * 0.44f), 1.3f),
        "kedarhomes" to Shot(Offset(w * 0.2f, h * 0.6f), 1.35f),
        "selasong" to Shot(Offset(w * 0.8f, h * 0.5f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { kedarDusk(a) }
}

private fun DrawScope.kedarDusk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sv = a.b("servant")
    val qs = a.b("quietstreet")
    val eo = a.b("eyesopen")
    val kh = a.b("kedarhomes")
    val sl = a.b("selasong")
    val horizon = h * 0.62f
    drawRect(Brush.verticalGradient(listOf(KedarC33.Sky, KedarC33.SkyLow), endY = horizon), size = Size(w, horizon))
    // the hill of Sela, right
    drawPath(Path().apply { moveTo(w * 0.62f, horizon); quadraticTo(w * 0.8f, h * 0.3f, w * 0.98f, horizon); close() }, KedarC33.Hill)
    drawRect(Brush.verticalGradient(listOf(KedarC33.Sand, KedarC33.SandDark), startY = horizon, endY = h), Offset(0f, horizon), Size(w, h - horizon))
    // the houses of Mecca and the House, left
    if (kh > 0f) {
        val f = easeOut(seg(kh, 0f, 0.4f))
        for (k in 0 until 5) {
            val x = w * (0.05f + 0.06f * k)
            val hh = h * (0.06f + 0.025f * (k % 3))
            drawRect(KedarC33.House, Offset(x, horizon - hh), Size(w * 0.05f, hh), alpha = f)
        }
        kaaba29(Offset(w * 0.2f, horizon + h * 0.04f), w * 0.07f, f)
        softGlow(Offset(w * 0.2f, horizon - h * 0.02f), w * 0.08f, ArtInk.Gold, 0.3f * f)
        tagAt(a.tag("kedarhomes"), Offset(w * 0.2f, horizon - h * 0.2f), seg(kh, 0.3f, 0.55f), back = Back33, ink = ArtInk.Gold)
    }
    // voices from the hilltop
    if (sl > 0f) {
        val f = easeOut(seg(sl, 0f, 0.3f))
        val top = Offset(w * 0.8f, h * 0.46f)
        for (k in 0 until 4) {
            val r = fract(t * 0.35f + k / 4f)
            drawArc(Color(0xFFFFF0C8), 200f, 140f, false, Offset(top.x - w * 0.2f * r, top.y - h * 0.25f * r), Size(w * 0.4f * r, h * 0.5f * r), alpha = 0.7f * f * (1f - r), style = Stroke(2f * dp))
        }
        tagAt(a.tag("selasong"), top + Offset(0f, -h * 0.28f), seg(sl, 0.3f, 0.55f), back = Back33, ink = ArtInk.Gold)
    }
    // the market street, loud, and his light passing quietly
    val streetY = h * 0.8f
    if (qs > 0f) {
        val f = easeOut(seg(qs, 0f, 0.3f))
        for (k in 0 until 5) {
            val x = w * (0.28f + 0.11f * k)
            drawRect(Color(0xFF6A4A32), Offset(x - w * 0.04f, streetY - h * 0.06f), Size(w * 0.08f, h * 0.06f), alpha = f)
            drawPath(Path().apply { moveTo(x - w * 0.05f, streetY - h * 0.06f); lineTo(x, streetY - h * 0.1f); lineTo(x + w * 0.05f, streetY - h * 0.06f); close() }, KedarC33.Stall[k % 4], alpha = f)
            // noise from each stall
            for (n in 0 until 2) {
                val r = fract(t * 1.2f + n * 0.5f + k * 0.17f)
                drawArc(Color(0xFF2A2020), -60f, 120f, false, Offset(x + w * 0.04f - w * 0.02f * r, streetY - h * 0.1f - h * 0.02f * r), Size(w * 0.04f * r, h * 0.04f * r), alpha = 0.5f * f * (1f - r), style = Stroke(1.2f * dp))
            }
        }
    }
    // his light: high over the desert, then along the street
    val walk = easeInOut(seg(qs, 0.2f, 1f))
    val down = easeInOut(seg(qs, 0f, 0.3f))
    val lightAt = Offset(mix(w * 0.5f, w * 0.3f + w * 0.42f * walk, down), mix(h * 0.3f, streetY - h * 0.18f, down))
    val shown = easeOut(seg(sv, 0.1f, 0.5f))
    if (shown > 0f) softGlow(lightAt + Offset(0f, h * 0.06f), w * 0.05f, Color(0xFFFFE8B0), 0.4f * shown * (1f - down))
    sirajMunir(lightAt, w * 0.012f, shown, t, nameAt = lightAt + Offset(0f, -h * 0.12f))
    tagAt(a.tag("quietstreet"), Offset(w * 0.5f, h * 0.94f), seg(qs, 0.4f, 0.65f), back = Back33, ink = Color(0xFFFFE6B0))
    // eyes opening
    if (eo > 0f) {
        for (k in 0 until 5) {
            val c = Offset(w * (0.22f + 0.1f * k), h * 0.38f)
            val f = easeOut(seg(eo, k * 0.08f, 0.25f + k * 0.08f)) * (1f - seg(kh, 0f, 0.3f))
            if (f <= 0f) continue
            val open = easeInOut(seg(eo, 0.3f + k * 0.08f, 0.6f + k * 0.08f))
            val ew = w * 0.035f
            drawOval(Color.White, Offset(c.x - ew, c.y - h * 0.025f * open), Size(2 * ew, h * 0.05f * open + 0.5f), alpha = f)
            drawCircle(Color(0xFF3A2A20), h * 0.018f * open, c, alpha = f)
            drawArc(KedarC33.Lid, 180f, 180f, false, Offset(c.x - ew, c.y - h * 0.03f), Size(2 * ew, h * 0.06f), alpha = f, style = Stroke(1.6f * dp))
            if (open > 0.6f) glint(c + Offset(-ew * 0.2f, -h * 0.006f), h * 0.006f, Color.White, f)
        }
    }
}

// ═════════════ 4. the house that is never shut (bs-bayt) ═════════════

private object HouseC33 {
    val Day = Color(0xFFBCD8EC)
    val DayLow = Color(0xFFF2E6CC)
    val Night = Color(0xFF12142A)
    val NightLow = Color(0xFF2E2A44)
    val Hill = Color(0xFF8A7058)
    val Valley = Color(0xFFC8A878)
    val Sheep = Color(0xFFF2EEE4)
    val Water = Color(0xFF6EC3E8)
    val Bird = Color(0xFF3A3440)
}

/**
 * Beats: flocks (two flocks come down the slopes to a house in a dry valley: «قِيدَارُ», «نَبَايُوتُ»), dayandnight
 * (day and night pass over it again and again; its door stays lit), welling (water wells up from the dry ground
 * beside it: «وَادِي بَكَّا»), pilgrims (from the horizon flocks of travellers come flying like doves).
 */
internal fun DrawScope.openhouseScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "flocks" to Shot(Offset(w * 0.5f, h * 0.62f), 1.15f),
        "dayandnight" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "welling" to Shot(Offset(w * 0.6f, h * 0.7f), 1.45f),
        "pilgrims" to Shot(Offset(w * 0.5f, h * 0.42f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { openValley(a) }
}

private fun DrawScope.openValley(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fl = a.b("flocks")
    val dn = a.b("dayandnight")
    val we = a.b("welling")
    val pi = a.b("pilgrims")
    // day and night pass over the house
    val cycles = 2f * seg(dn, 0.05f, 0.95f)
    val night = if (dn <= 0f || dn >= 0.95f) 0f else 0.5f - 0.5f * cos(2f * PI.toFloat() * cycles)
    drawRect(Brush.verticalGradient(listOf(lerpColor(HouseC33.Day, HouseC33.Night, night), lerpColor(HouseC33.DayLow, HouseC33.NightLow, night)), endY = h * 0.6f), size = Size(w, h))
    if (night > 0.3f) for (k in 0 until 30) drawCircle(Color.White, w * 0.0025f, Offset(w * w8Rnd(k, 9.1f), h * 0.45f * w8Rnd(k, 4.7f)), alpha = (night - 0.3f) * (0.6f + 0.4f * sin(t * 2f + k)))
    // hills round the valley
    drawPath(Path().apply { moveTo(0f, h * 0.75f); lineTo(0f, h * 0.38f); quadraticTo(w * 0.18f, h * 0.3f, w * 0.34f, h * 0.62f); lineTo(w * 0.34f, h * 0.75f); close() }, lerpColor(HouseC33.Hill, Color(0xFF2A2430), night * 0.6f))
    drawPath(Path().apply { moveTo(w, h * 0.75f); lineTo(w, h * 0.36f); quadraticTo(w * 0.82f, h * 0.28f, w * 0.66f, h * 0.62f); lineTo(w * 0.66f, h * 0.75f); close() }, lerpColor(HouseC33.Hill, Color(0xFF2A2430), night * 0.6f))
    drawRect(lerpColor(HouseC33.Valley, Color(0xFF3A3038), night * 0.6f), Offset(0f, h * 0.68f), Size(w, h * 0.32f))
    // the house, its door always lit
    val foot = Offset(w * 0.5f, h * 0.74f)
    kaaba29(foot, w * 0.12f, 1f)
    softGlow(foot + Offset(w * 0.03f, -h * 0.04f), w * 0.05f, ArtInk.Gold, 0.35f + 0.4f * night)
    // the flocks
    if (fl > 0f) {
        for (side in 0 until 2) {
            val s = if (side == 0) -1f else 1f
            for (k in 0 until 6) {
                val come = easeInOut(seg(fl, 0.05f + k * 0.05f, 0.6f + k * 0.05f))
                val from = Offset(w * 0.5f + s * w * (0.42f + 0.03f * k), h * (0.48f + 0.03f * (k % 2)))
                val to = Offset(w * 0.5f + s * w * (0.12f + 0.025f * k), h * (0.78f + 0.025f * (k % 3)))
                sheep26(from + (to - from) * come, w * 0.03f, HouseC33.Sheep, easeOut(seg(fl, k * 0.05f, 0.2f + k * 0.05f)))
            }
            tagAt(a.tag("flocks", 0, side), Offset(w * 0.5f + s * w * 0.28f, h * 0.58f), seg(fl, 0.4f + side * 0.15f, 0.65f + side * 0.15f) * (1f - seg(pi, 0f, 0.3f)), back = Back33, ink = ArtInk.Ivory)
        }
    }
    // the spring in the dry valley
    if (we > 0f) {
        val f = easeOut(seg(we, 0.1f, 0.6f))
        val c = Offset(w * 0.62f, h * 0.84f)
        drawOval(HouseC33.Water, Offset(c.x - w * 0.05f * f, c.y - h * 0.02f * f), Size(w * 0.1f * f, h * 0.04f * f))
        for (k in 0 until 6) {
            val r = fract(t * 1.2f + k / 6f)
            val ang = -PI.toFloat() / 2f + (k - 2.5f) * 0.25f
            drawCircle(HouseC33.Water, w * 0.005f, c + Offset(cos(ang) * w * 0.03f * r, -h * 0.08f * sin(PI.toFloat() * r)), alpha = f * (1f - r))
        }
        glint(c + Offset(-w * 0.01f, -h * 0.005f), w * 0.008f, Color.White, f)
        tagAt(a.tag("welling"), c + Offset(0f, -h * 0.13f), seg(we, 0.4f, 0.65f), back = Back33, ink = Color(0xFFB8E8FF))
    }
    // travellers coming like doves
    if (pi > 0f) {
        for (k in 0 until 14) {
            val go = fract(seg(pi, 0f, 1f) * 0.8f + k * 0.07f)
            val from = Offset(w * (if (k % 2 == 0) -0.05f else 1.05f), h * (0.1f + 0.03f * (k % 5)))
            val to = Offset(w * 0.5f, h * 0.55f)
            val p = from + (to - from) * go
            val flap = sin(t * 8f + k) * w * 0.006f
            val s = w * 0.012f
            drawLine(HouseC33.Bird, p, p + Offset(-s, -s * 0.4f - flap), 1.5f * dp, StrokeCap.Round, alpha = easeOut(seg(pi, 0f, 0.2f)) * (1f - go * go))
            drawLine(HouseC33.Bird, p, p + Offset(s, -s * 0.4f - flap), 1.5f * dp, StrokeCap.Round, alpha = easeOut(seg(pi, 0f, 0.2f)) * (1f - go * go))
        }
    }
}

// ═════════════ 5. the vineyard and the corner stone (bs-hajar) ═════════════

private object VineC33 {
    val Sky = Color(0xFFCADCEA)
    val SkyLow = Color(0xFFF0E4C8)
    val Ground = Color(0xFFB89A6A)
    val Vine = Color(0xFF5A7A3A)
    val Grape = Color(0xFF6A3A6A)
    val Tower = Color(0xFFA08870)
    val Wall = Color(0xFFD2BA92)
    val WallLine = Color(0xFF9A8260)
    val Stone = Color(0xFFE8D4A8)
    val Man = Color(0xFF2E2620)
    val Keeper = Color(0xFF3A3A2A)
}

/**
 * Beats: vineyard (rows of vines and a watchtower; servants come for the fruit and are driven back), newkeepers (other
 * keepers take the vineyard and bring baskets of grapes in season), rejected (beside a wall the builders roll a stone
 * away), cornerset (the same stone rises into the empty corner and the wall is whole: «رَأْسُ الزَّاوِيَةِ»), lastlit
 * (his light over the corner).
 */
internal fun DrawScope.vineyardScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "vineyard" to Shot(Offset(w * 0.3f, h * 0.6f), 1.35f),
        "newkeepers" to Shot(Offset(w * 0.3f, h * 0.62f), 1.3f),
        "rejected" to Shot(Offset(w * 0.75f, h * 0.65f), 1.4f),
        "cornerset" to Shot(Offset(w * 0.72f, h * 0.5f), 1.3f),
        "lastlit" to Shot(Offset(w * 0.6f, h * 0.45f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { vineyardField(a) }
}

private fun DrawScope.vineyardField(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val vy = a.b("vineyard")
    val nk = a.b("newkeepers")
    val rj = a.b("rejected")
    val cs = a.b("cornerset")
    val ll = a.b("lastlit")
    val ground = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(VineC33.Sky, VineC33.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(VineC33.Ground, Offset(0f, ground), Size(w, h - ground))
    // vines and the tower
    val vf = easeOut(seg(vy, 0f, 0.3f))
    for (r in 0 until 3) {
        val y = h * (0.62f + 0.08f * r)
        drawLine(Color(0xFF7A5A3A), Offset(w * 0.04f, y), Offset(w * 0.46f, y), 1.5f * dp, alpha = vf)
        for (k in 0 until 7) {
            val c = Offset(w * (0.06f + 0.065f * k), y - h * 0.02f)
            drawCircle(VineC33.Vine, w * 0.02f, c, alpha = vf)
            if ((k + r) % 2 == 0) drawCircle(VineC33.Grape, w * 0.008f, c + Offset(0f, h * 0.015f), alpha = vf)
        }
    }
    drawRect(VineC33.Tower, Offset(w * 0.42f, h * 0.36f), Size(w * 0.05f, h * 0.22f), alpha = vf)
    drawPath(Path().apply { moveTo(w * 0.41f, h * 0.36f); lineTo(w * 0.445f, h * 0.31f); lineTo(w * 0.48f, h * 0.36f); close() }, Color(0xFF8A6A50), alpha = vf)
    // servants come and are driven back
    if (vy > 0f && nk < 0.3f) {
        val fade = 1f - seg(nk, 0f, 0.3f)
        for (k in 0 until 3) {
            val come = easeInOut(seg(vy, 0.1f + k * 0.08f, 0.45f + k * 0.08f))
            val back = easeInOut(seg(vy, 0.6f + k * 0.05f, 0.95f))
            val x = w * (0.0f - 0.04f * k) + w * 0.2f * come - w * 0.22f * back
            figure(Offset(x, ground), h * 0.14f, VineC33.Man, vf * fade, walk = if (come in 0.01f..0.99f || back in 0.01f..0.99f) t * 4f else 0f, lean = if (back > 0f) -0.2f else 0.1f)
        }
        for (k in 0 until 2) figure(Offset(w * (0.3f + 0.06f * k), ground), h * 0.15f, Color(0xFF4A2A22), vf * fade, arms = 0.8f * seg(vy, 0.55f, 0.7f))
    }
    // the new keepers, with baskets
    if (nk > 0f) {
        for (k in 0 until 3) {
            val f = easeOut(seg(nk, 0.1f + k * 0.1f, 0.4f + k * 0.1f))
            val walk = easeInOut(seg(nk, 0.3f + k * 0.08f, 0.9f))
            val foot = Offset(w * (0.1f + 0.08f * k) + w * 0.1f * walk, ground)
            figure(foot, h * 0.15f, VineC33.Keeper, f, walk = if (walk in 0.01f..0.99f) t * 4f else 0f, arms = 0.6f)
            val basket = foot + Offset(w * 0.025f, -h * 0.07f)
            drawArc(Color(0xFF9A6A3A), 0f, 180f, true, Offset(basket.x - w * 0.018f, basket.y - h * 0.015f), Size(w * 0.036f, h * 0.03f), alpha = f)
            for (g in 0 until 3) drawCircle(VineC33.Grape, w * 0.006f, basket + Offset(w * (-0.008f + 0.008f * g), -h * 0.002f), alpha = f)
        }
    }
    // the wall with its empty corner
    val wallX = w * 0.58f
    for (r in 0 until 5) {
        val y = ground - h * 0.07f * (r + 1)
        val right = if (r == 4) w * 0.86f else w * 0.94f
        drawRect(VineC33.Wall, Offset(wallX, y), Size(right - wallX, h * 0.07f))
        drawLine(VineC33.WallLine, Offset(wallX, y), Offset(right, y), 1f * dp)
        for (k in 0 until 6) {
            val x = wallX + (right - wallX) * (k + (if (r % 2 == 0) 0.5f else 0f)) / 6f
            if (x < right) drawLine(VineC33.WallLine, Offset(x, y), Offset(x, y + h * 0.07f), 1f * dp)
        }
    }
    // the stone, rolled away and then set as the corner
    val corner = Offset(w * 0.9f, ground - h * 0.07f * 4.5f)
    val aside = Offset(w * 0.68f, ground - h * 0.03f)
    val rolled = easeOut(seg(rj, 0.2f, 0.7f))
    val set = easeInOut(seg(cs, 0f, 0.6f))
    val stoneAt = if (set > 0f) aside + (corner - aside) * set else Offset(w * 0.9f, ground - h * 0.03f) + (aside - Offset(w * 0.9f, ground - h * 0.03f)) * rolled
    val sf = easeOut(seg(rj, 0f, 0.2f))
    if (sf > 0f) {
        drawRoundRect(VineC33.Stone, Offset(stoneAt.x - w * 0.04f, stoneAt.y - h * 0.035f), Size(w * 0.08f, h * 0.07f), CornerRadius(3f * dp), alpha = sf)
        drawRoundRect(Color(0xFFB89A6A), Offset(stoneAt.x - w * 0.04f, stoneAt.y - h * 0.035f), Size(w * 0.08f, h * 0.07f), CornerRadius(3f * dp), alpha = sf, style = Stroke(1.2f * dp))
        if (set > 0.95f) softGlow(stoneAt, w * 0.08f, ArtInk.Gold, 0.6f)
        if (rj > 0f && set <= 0f) for (k in 0 until 2) figure(Offset(w * (0.8f + 0.06f * k), ground), h * 0.14f, VineC33.Man, sf * (1f - seg(cs, 0f, 0.2f)), lean = -0.3f, arms = 0.7f)
    }
    tagAt(a.tag("cornerset"), Offset(w * 0.8f, h * 0.2f), seg(cs, 0.5f, 0.75f), back = Back33, ink = ArtInk.Gold)
    // his light over the corner
    sirajMunir(corner + Offset(0f, -h * 0.14f), w * 0.012f, easeOut(seg(ll, 0.1f, 0.5f)), t, nameAt = corner + Offset(-w * 0.16f, -h * 0.2f))
}
