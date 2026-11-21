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
 * Wave 76 (2026-11-03): v1.3 fiqh, «الفقه الميسر» (a committee of scholars), purification 1. Pure water and its sources; vessels; the
 * manners of the washroom, told only with a door, footprints and a compass; the siwak; the obligatory acts of wuḍūʾ and its
 * sunnahs. Bodies are never shown beyond the plain faceless figure, hands and feet.
 */

internal object ThrC {
    val Top = Color(0xFF14284A)
    val Low = Color(0xFF3A5A7A)
    val Ground = Color(0xFF1A2230)
    val Water = Color(0xFF7CC4E8)
    val Deep = Color(0xFF3A86B8)
    val Person = Color(0xFFE8DCC8)
    val Gold = Color(0xFFF2C860)
    val Silver = Color(0xFFC8CCD4)
    val Clay = Color(0xFFB07A48)
    val Wood = Color(0xFF8A6A48)
    val Stone = Color(0xFF8A8478)
    val Green = Color(0xFF6FBF8A)
    val Murky = Color(0xFF7A7A4A)
}

private fun DrawScope.thrNight(t: Float, ground: Float) {
    nightSky(t, ThrC.Top, ThrC.Low, ground, starAlpha = 0.5f)
    drawRect(ThrC.Ground, Offset(0f, ground), Size(size.width, size.height - ground))
}

/** A footprint, heel at the bottom; [right] mirrors it. */
internal fun DrawScope.foot76(c: Offset, s: Float, color: Color, alpha: Float, right: Boolean) {
    if (alpha <= 0.01f) return
    val m = if (right) 1f else -1f
    drawOval(color, Offset(c.x - s * 0.18f, c.y - s * 0.4f), Size(s * 0.36f, s * 0.8f), alpha = alpha)
    for (k in 0 until 5) {
        val tx = c.x + m * s * (-0.14f + 0.07f * k)
        drawCircle(color, s * (0.07f - 0.008f * k), Offset(tx, c.y - s * (0.48f - 0.02f * k)), alpha = alpha)
    }
}

/** A glass of water around [c], tinted by [murk]. */
internal fun DrawScope.glass76(c: Offset, s: Float, murk: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val p = Path().apply {
        moveTo(c.x - s * 0.3f, c.y - s * 0.5f); lineTo(c.x + s * 0.3f, c.y - s * 0.5f)
        lineTo(c.x + s * 0.22f, c.y + s * 0.5f); lineTo(c.x - s * 0.22f, c.y + s * 0.5f); close()
    }
    val fill = Path().apply {
        moveTo(c.x - s * 0.27f, c.y - s * 0.25f); lineTo(c.x + s * 0.27f, c.y - s * 0.25f)
        lineTo(c.x + s * 0.22f, c.y + s * 0.5f); lineTo(c.x - s * 0.22f, c.y + s * 0.5f); close()
    }
    drawPath(fill, lerpColor(ThrC.Water, ThrC.Murky, murk), alpha = alpha * 0.8f)
    drawPath(p, ThrC.Silver, alpha = alpha, style = Stroke(s * 0.04f))
}

/** A bowl with a rim, seen from the side, its base at [foot]. */
internal fun DrawScope.bowl76(foot: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawArc(color, 0f, 180f, true, Offset(foot.x - s * 0.5f, foot.y - s * 0.55f), Size(s, s * 0.6f), alpha = alpha)
    drawOval(lerpColor(color, Color.White, 0.2f), Offset(foot.x - s * 0.5f, foot.y - s * 0.3f), Size(s, s * 0.1f), alpha = alpha)
}

// ═════════════ 1. thr-tahara: purity and pure water ═════════════

/**
 * Beats: keyofprayer (a key of water turns and the door of prayer opens: «مِفْتَاحُ الصَّلَاةِ»), twopurities (a heart washed of its dark spots,
 * and drops over one standing: «طَهَارَةُ الْقَلْبِ», «وَطَهَارَةُ الْبَدَنِ»), minormajor (a small jug for wuḍūʾ and a full pour for ghusl:
 * «أَصْغَرُ يَرْتَفِعُ بِالْوُضُوءِ», «وَأَكْبَرُ يَرْتَفِعُ بِالْغُسْلِ»), fourwaters (rain, a river, a spring and the sea: «كَمَاءِ الْمَطَرِ»,
 * «وَالْأَنْهَارِ», «وَالْعُيُونِ», «وَالْبِحَارِ»), threechanges (a clear glass clouds; its smell, taste and colour change: «رِيحَهُ», «طَعْمَهُ»,
 * «لَوْنَهُ»).
 */
internal fun DrawScope.purewaterScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "keyofprayer" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "twopurities" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "minormajor" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "fourwaters" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "threechanges" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { pureWaterDraw(a) }
}

private fun DrawScope.pureWaterDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    thrNight(t, ground)
    val kp = fqIn(a, "keyofprayer", "twopurities")
    val tp = fqIn(a, "twopurities", "minormajor")
    val mm = fqIn(a, "minormajor", "fourwaters")
    val fw = fqIn(a, "fourwaters", "threechanges")
    val tc = fqIn(a, "threechanges")
    if (kp > 0f) {
        val p = a.b("keyofprayer")
        val door = Offset(w * 0.62f, ground)
        door12(door, w * 0.2f, h * 0.48f, seg(p, 0.45f, 0.75f), ThrC.Wood, Color(0xFF5A4030), kp)
        val turn = seg(p, 0.2f, 0.45f)
        key21(Offset(mix(w * 0.2f, w * 0.48f, easeInOut(seg(p, 0f, 0.25f))), h * 0.5f), w * 0.16f, ThrC.Water, kp, 90f * turn)
        softGlow(Offset(w * 0.4f, h * 0.5f), w * 0.08f, ThrC.Water, 0.4f * kp)
        tagAt(a.tag("keyofprayer"), Offset(w * 0.4f, h * 0.18f), kp * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tp > 0f) {
        val p = a.b("twopurities")
        val hc = Offset(w * 0.72f, h * 0.48f)
        val clean = seg(p, 0.15f, 0.5f)
        heart12(hc, w * 0.12f, lerpColor(Color(0xFF6A5A6A), ThrC.Gold, clean), tp, fill = true)
        for (k in 0 until 5) drawCircle(Color(0xFF2A2430), w * 0.012f, hc + Offset(w * (-0.04f + 0.02f * k), -h * 0.02f + (k % 2) * h * 0.04f), alpha = tp * (1f - clean))
        softGlow(hc, w * 0.12f, ThrC.Gold, 0.4f * tp * clean)
        tagAt(a.tag("twopurities", 0, 0), hc + Offset(0f, h * 0.2f), tp * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(p, 0.5f, 0.7f)
        val foot = Offset(w * 0.28f, ground)
        figure(foot, h * 0.34f, ThrC.Person, tp * g)
        rain12(w * 0.18f, w * 0.38f, h * 0.15f, ground - h * 0.05f, t, ThrC.Water, tp * g, 18)
        tagAt(a.tag("twopurities", 0, 1), Offset(w * 0.28f, h * 0.12f), tp * g, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (mm > 0f) {
        val p = a.b("minormajor")
        val g0 = seg(p, 0.05f, 0.25f)
        fqEwer(Offset(w * 0.72f, ground - h * 0.05f), h * 0.3f, ThrC.Clay, mm * g0, pour = 0.6f * g0, t = t)
        tagAt(a.tag("minormajor", 0, 0), Offset(w * 0.72f, h * 0.18f), mm * g0, back = Fq.Back, ink = ArtInk.Ivory)
        val g1 = seg(p, 0.5f, 0.7f)
        val c = Offset(w * 0.28f, h * 0.3f)
        drawOval(ThrC.Silver, Offset(c.x - w * 0.08f, c.y - h * 0.03f), Size(w * 0.16f, h * 0.06f), alpha = mm * g1)
        rain12(c.x - w * 0.12f, c.x + w * 0.12f, c.y + h * 0.03f, ground, t * 1.5f, ThrC.Water, mm * g1, 40)
        drawOval(ThrC.Deep, Offset(c.x - w * 0.14f, ground - h * 0.02f), Size(w * 0.28f, h * 0.04f), alpha = mm * g1 * 0.6f)
        tagAt(a.tag("minormajor", 0, 1), Offset(w * 0.28f, h * 0.14f), mm * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (fw > 0f) {
        val p = a.b("fourwaters")
        val pw = w * 0.22f
        val ph = h * 0.42f
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.22f * k, 0.22f + 0.22f * k)
            val c = Offset(w * (0.86f - 0.24f * k), h * 0.48f)
            val tl = Offset(c.x - pw / 2f, c.y - ph / 2f)
            drawRoundRect(Color(0xFF0E1A2E), tl, Size(pw, ph), CornerRadius(w * 0.02f), alpha = fw * g)
            when (k) {
                0 -> {
                    cloud38(Offset(c.x, tl.y + ph * 0.22f), pw * 0.22f, Color(0xFFB8C4D8), fw * g)
                    rain12(tl.x + pw * 0.15f, tl.x + pw * 0.85f, tl.y + ph * 0.32f, tl.y + ph * 0.95f, t, ThrC.Water, fw * g, 14)
                }
                1 -> for (r in 0 until 4) {
                    val y = tl.y + ph * (0.4f + 0.13f * r)
                    drawPath(Path().apply { moveTo(tl.x, y); quadraticTo(c.x, y - ph * 0.06f * sin(t * 2f + r), tl.x + pw, y) }, ThrC.Water, alpha = fw * g, style = Stroke(3f * dp))
                }
                2 -> {
                    drawPath(Path().apply { moveTo(tl.x, tl.y + ph); lineTo(c.x - pw * 0.1f, tl.y + ph * 0.55f); lineTo(c.x + pw * 0.15f, tl.y + ph * 0.6f); lineTo(tl.x + pw, tl.y + ph); close() }, ThrC.Stone, alpha = fw * g)
                    for (j in 0 until 5) {
                        val u = fract(t * 0.8f + j / 5f)
                        fqDrop(Offset(c.x + (j - 2) * pw * 0.04f * u * 3f, tl.y + ph * (0.55f - 0.25f * sin(PI.toFloat() * u))), pw * 0.025f, ThrC.Water, fw * g * (1f - u * 0.5f))
                    }
                }
                else -> {
                    drawRect(ThrC.Deep, Offset(tl.x, tl.y + ph * 0.5f), Size(pw, ph * 0.5f), alpha = fw * g)
                    for (r in 0 until 3) drawLine(ThrC.Water, Offset(tl.x + pw * (0.1f + 0.1f * r), tl.y + ph * (0.6f + 0.12f * r)), Offset(tl.x + pw * (0.5f + 0.1f * r), tl.y + ph * (0.6f + 0.12f * r)), 2f * dp, StrokeCap.Round, alpha = fw * g * (0.5f + 0.5f * sin(t * 2f + r)))
                }
            }
            tagAt(a.tag("fourwaters", 0, k), Offset(c.x, tl.y + ph + h * 0.07f), fw * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (tc > 0f) {
        val p = a.b("threechanges")
        val c = Offset(w * 0.5f, h * 0.55f)
        val murk = seg(p, 0.1f, 0.8f)
        glass76(c, h * 0.36f, murk, tc)
        val spots = listOf(Offset(w * 0.78f, h * 0.3f), Offset(w * 0.78f, h * 0.7f), Offset(w * 0.22f, h * 0.5f))
        for (k in 0 until 3) {
            val g = seg(p, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            drawLine(ThrC.Silver, c, spots[k], 1.2f * dp, alpha = tc * g * 0.5f)
            tagAt(a.tag("threechanges", 0, k), spots[k], tc * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
        cross14(c + Offset(0f, -h * 0.3f), w * 0.05f, seg(p, 0.8f, 0.95f))
    }
}

// ═════════════ 2. thr-aniya: vessels ═════════════

/**
 * Beats: allvessels (a shelf of jugs, cups and bowls, each ticked: «الْأَصْلُ فِي الْآنِيَةِ الْإِبَاحَةُ»), goldsilver (a gold cup and a silver
 * one, and a plated one, struck out: «الذَّهَبِ وَالْفِضَّةِ», «الْخَالِصُ مِنْهَا وَالْمَطْلِيُّ بِهِمَا»), otherspots (a jug of uncertain use is
 * washed and then ticked: «إِلَّا إِذَا عُلِمَتْ نَجَاسَتُهَا»), silverpatch (a cracked bowl mended with a small band of silver, ticked: «ضَبَّةٌ
 * يَسِيرَةٌ مِنْ فِضَّةٍ»), goldpatch (the same bowl mended with gold, struck out: «مِنْ ذَهَبٍ حَرُمَ»).
 */
internal fun DrawScope.vesselrulesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "allvessels" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "goldsilver" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "otherspots" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "silverpatch" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "goldpatch" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { vesselsDraw(a) }
}

private fun DrawScope.vesselsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    drawRect(Color(0xFF2A2230), Offset.Zero, size)
    drawRect(Color(0xFF3A3040), Offset(0f, ground), Size(w, h - ground))
    val av = fqIn(a, "allvessels", "goldsilver")
    val gs = fqIn(a, "goldsilver", "otherspots")
    val os = fqIn(a, "otherspots", "silverpatch")
    val sp = fqIn(a, "silverpatch", "goldpatch")
    val gp = fqIn(a, "goldpatch")
    if (av > 0f) {
        val p = a.b("allvessels")
        drawRect(ThrC.Wood, Offset(w * 0.08f, ground - h * 0.02f), Size(w * 0.84f, h * 0.025f), alpha = av)
        for (k in 0 until 5) {
            val g = seg(p, 0.05f + 0.12f * k, 0.2f + 0.12f * k)
            val foot = Offset(w * (0.84f - 0.17f * k), ground - h * 0.02f)
            when (k % 3) {
                0 -> fqEwer(foot, h * 0.24f, if (k == 0) ThrC.Clay else Color(0xFF6A8AA8), av * g)
                1 -> cup12(foot, w * 0.07f, Color(0xFF8AA0B8), av * g)
                else -> bowl76(foot, w * 0.12f, Color(0xFF9A7A5A), av * g)
            }
            tick14(foot + Offset(0f, -h * 0.3f), w * 0.025f, seg(p, 0.6f + 0.05f * k, 0.7f + 0.05f * k))
        }
        tagAt(a.tag("allvessels"), Offset(w * 0.5f, h * 0.14f), av * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (gs > 0f) {
        val p = a.b("goldsilver")
        val g0 = seg(p, 0.05f, 0.25f)
        cup12(Offset(w * 0.75f, ground), w * 0.12f, ThrC.Gold, gs * g0)
        glint(Offset(w * 0.73f, ground - h * 0.18f), w * 0.03f, Color.White, gs * g0 * (0.5f + 0.5f * sin(t * 3f)))
        cup12(Offset(w * 0.52f, ground), w * 0.12f, ThrC.Silver, gs * g0)
        tagAt(a.tag("goldsilver", 0, 0), Offset(w * 0.63f, h * 0.14f), gs * g0, back = Fq.Back, ink = ArtInk.Gold)
        val g1 = seg(p, 0.5f, 0.7f)
        cup12(Offset(w * 0.25f, ground), w * 0.12f, Color(0xFF6A7A9A), gs * g1)
        drawRect(ThrC.Gold, Offset(w * 0.25f - w * 0.06f, ground - h * 0.18f), Size(w * 0.12f, h * 0.025f), alpha = gs * g1)
        tagAt(a.tag("goldsilver", 0, 1), Offset(w * 0.3f, h * 0.3f), gs * g1, back = Fq.Back, ink = ArtInk.Ivory)
        for (x in listOf(0.75f, 0.52f, 0.25f)) cross14(Offset(w * x, ground - h * 0.32f), w * 0.035f, seg(p, 0.3f + (0.75f - x) * 0.8f, 0.45f + (0.75f - x) * 0.8f))
    }
    if (os > 0f) {
        val p = a.b("otherspots")
        val foot = Offset(w * 0.5f, ground)
        fqEwer(foot, h * 0.34f, Color(0xFF6A8AA8), os)
        val wash = seg(p, 0.3f, 0.7f)
        rain12(w * 0.38f, w * 0.62f, h * 0.15f, ground - h * 0.05f, t * 1.5f, ThrC.Water, os * bump(wash), 24)
        tick14(foot + Offset(w * 0.18f, -h * 0.35f), w * 0.04f, seg(p, 0.7f, 0.85f))
        tagAt(a.tag("otherspots"), Offset(w * 0.5f, h * 0.12f), os * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val mended = { alpha: Float, band: Color, ok: Boolean, prog: Float ->
        val foot = Offset(w * 0.5f, ground)
        bowl76(foot, w * 0.34f, ThrC.Wood, alpha)
        drawLine(Color(0xFF2A1A10), foot + Offset(-w * 0.02f, -h * 0.2f), foot + Offset(w * 0.02f, -h * 0.04f), 2f * dp, alpha = alpha)
        val g = seg(prog, 0.2f, 0.45f)
        for (k in 0 until 3) drawRoundRect(band, Offset(foot.x - w * 0.025f, foot.y - h * (0.18f - 0.05f * k)), Size(w * 0.05f, h * 0.022f), CornerRadius(h * 0.01f), alpha = alpha * g)
        if (ok) tick14(foot + Offset(w * 0.22f, -h * 0.25f), w * 0.04f, seg(prog, 0.5f, 0.7f)) else cross14(foot + Offset(w * 0.22f, -h * 0.25f), w * 0.04f, seg(prog, 0.5f, 0.7f))
    }
    if (sp > 0f) {
        val p = a.b("silverpatch")
        mended(sp, ThrC.Silver, true, p)
        tagAt(a.tag("silverpatch"), Offset(w * 0.5f, h * 0.14f), sp * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (gp > 0f) {
        val p = a.b("goldpatch")
        mended(gp, ThrC.Gold, false, p)
        tagAt(a.tag("goldpatch"), Offset(w * 0.5f, h * 0.14f), gp * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ember)
    }
}

// ═════════════ 3. thr-khala: the manners of the washroom ═════════════

/**
 * Beats: leftfootin (at a closed door the left foot steps in first: «رِجْلَهُ الْيُسْرَى», «بِسْمِ اللَّهِ»), rightfootout (coming out, the right
 * foot first: «قَدَّمَ الْيُمْنَى», «غُفْرَانَكَ»), noqibla (a compass with the House on it; the line to and from the qibla is struck out: «وَلَا
 * يَجُوزُ اسْتِقْبَالُ الْقِبْلَةِ وَلَا اسْتِدْبَارُهَا»), waterorstones (a jug of water, three stones, and the two together lit best: «بِالْمَاءِ»,
 * «بِالْحِجَارَةِ», «وَالْجَمْعُ بَيْنَهُمَا أَفْضَلُ»).
 */
internal fun DrawScope.restroomwayScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "leftfootin" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "rightfootout" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "noqibla" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "waterorstones" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { restroomWayDraw(a) }
}

private fun DrawScope.restroomWayDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val ground = h * 0.84f
    drawRect(Color(0xFF242a38), Offset.Zero, size)
    drawRect(Color(0xFF30384A), Offset(0f, h * 0.6f), Size(w, h * 0.4f))
    val li = fqIn(a, "leftfootin", "rightfootout")
    val ro = fqIn(a, "rightfootout", "noqibla")
    val nq = fqIn(a, "noqibla", "waterorstones")
    val ws = fqIn(a, "waterorstones")
    val doorAt = Offset(w * 0.5f, h * 0.6f)
    val dooring = maxOf(li, ro)
    if (dooring > 0f) door12(doorAt, w * 0.18f, h * 0.45f, 0.15f, Color(0xFF8A94B8), Color(0xFF5A6278), dooring)
    if (li > 0f) {
        val p = a.b("leftfootin")
        val l = seg(p, 0.1f, 0.3f)
        foot76(Offset(w * 0.45f, mix(ground, h * 0.68f, l)), h * 0.12f, ThrC.Gold, li * (0.4f + 0.6f * l), right = false)
        foot76(Offset(w * 0.55f, ground), h * 0.12f, ThrC.Silver, li * 0.5f, right = true)
        tagAt(a.tag("leftfootin", 0, 0), Offset(w * 0.22f, h * 0.7f), li * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("leftfootin", 0, 1), Offset(w * 0.5f, h * 0.08f), li * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ro > 0f) {
        val p = a.b("rightfootout")
        val r = seg(p, 0.1f, 0.3f)
        foot76(Offset(w * 0.55f, mix(h * 0.68f, ground, r)), h * 0.12f, ThrC.Gold, ro * (0.4f + 0.6f * r), right = true)
        foot76(Offset(w * 0.45f, h * 0.68f), h * 0.12f, ThrC.Silver, ro * 0.5f, right = false)
        tagAt(a.tag("rightfootout", 0, 0), Offset(w * 0.78f, h * 0.7f), ro * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("rightfootout", 0, 1), Offset(w * 0.5f, h * 0.08f), ro * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (nq > 0f) {
        val p = a.b("noqibla")
        drawRect(Color(0xFF14182A), Offset.Zero, size, alpha = 0.85f * nq)
        val c = Offset(w * 0.5f, h * 0.52f)
        val r = h * 0.3f
        drawCircle(ThrC.Silver, r, c, alpha = nq, style = Stroke(2f * dp))
        for (k in 0 until 8) {
            val ang = PI.toFloat() * k / 4f
            drawLine(ThrC.Silver, c + Offset(cos(ang), sin(ang)) * (r * 0.85f), c + Offset(cos(ang), sin(ang)) * r, 2f * dp, alpha = nq)
        }
        kaaba29(c + Offset(0f, -r * 1.12f), w * 0.05f, nq)
        val g = seg(p, 0.15f, 0.4f)
        drawLine(Color(0xFFC0503A), c, c + Offset(0f, -r * 0.8f * g), 4f * dp, StrokeCap.Round, alpha = nq)
        drawLine(Color(0xFFC0503A), c, c + Offset(0f, r * 0.8f * g), 4f * dp, StrokeCap.Round, alpha = nq)
        cross14(c + Offset(0f, -r * 0.45f), w * 0.03f, seg(p, 0.35f, 0.5f))
        cross14(c + Offset(0f, r * 0.45f), w * 0.03f, seg(p, 0.4f, 0.55f))
        val side = seg(p, 0.6f, 0.8f)
        drawLine(ThrC.Green, c, c + Offset(r * 0.8f * side, 0f), 4f * dp, StrokeCap.Round, alpha = nq)
        drawLine(ThrC.Green, c, c + Offset(-r * 0.8f * side, 0f), 4f * dp, StrokeCap.Round, alpha = nq)
        tagAt(a.tag("noqibla"), Offset(w * 0.5f, h * 0.94f), nq * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ws > 0f) {
        val p = a.b("waterorstones")
        drawRect(Color(0xFF14182A), Offset.Zero, size, alpha = 0.6f * ws)
        val g0 = seg(p, 0.05f, 0.25f)
        fqEwer(Offset(w * 0.78f, ground - h * 0.08f), h * 0.3f, ThrC.Clay, ws * g0)
        tagAt(a.tag("waterorstones", 0, 0), Offset(w * 0.78f, h * 0.3f), ws * g0, back = Fq.Back, ink = ArtInk.Ivory)
        val g1 = seg(p, 0.3f, 0.5f)
        for (k in 0 until 3) drawOval(ThrC.Stone, Offset(w * (0.18f + 0.07f * k), ground - h * 0.14f - (k % 2) * h * 0.03f), Size(w * 0.06f, h * 0.06f), alpha = ws * g1)
        tagAt(a.tag("waterorstones", 0, 1), Offset(w * 0.25f, h * 0.3f), ws * g1, back = Fq.Back, ink = ArtInk.Ivory)
        val both = seg(p, 0.6f, 0.8f)
        softGlow(Offset(w * 0.5f, ground - h * 0.15f), w * 0.3f, ThrC.Gold, 0.35f * ws * both)
        drawLine(ThrC.Gold, Offset(w * 0.35f, ground - h * 0.12f), Offset(w * 0.68f, ground - h * 0.12f), 2.5f * dp, StrokeCap.Round, alpha = ws * both)
        tagAt(a.tag("waterorstones", 0, 2), Offset(w * 0.5f, h * 0.14f), ws * both, back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. thr-siwak: the siwak and the sunnahs of fitra ═════════════

/**
 * Beats: anytime (a siwak with the sun and moon turning round it: «فِي جَمِيعِ الْأَوْقَاتِ», «حَتَّى لِلصَّائِمِ»), fourmoments (four moments light
 * around it: a jug, a prayer rug, an open book, the sun of waking: «الْوُضُوءِ», «وَالصَّلَاةِ», «وَقِرَاءَةِ الْقُرْآنِ», «وَالِانْتِبَاهِ مِنَ
 * النَّوْمِ»), aracktwig (a green branch of arak and a fresh stick cut from it: «بِعُودٍ رَطْبٍ», «عُودِ أَرَاكٍ»), fitrafive (five small lights in
 * a ring round a pair of scissors: «سُنَنِ الْفِطْرَةِ»), siwakbenefits (a tooth made firm, the gum held, a voice made clear: «يُقَوِّي
 * الْأَسْنَانَ», «وَيَشُدُّ اللِّثَةَ», «وَيُنَقِّي الصَّوْتَ»).
 */
internal fun DrawScope.miswakScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "anytime" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "fourmoments" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "aracktwig" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "fitrafive" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "siwakbenefits" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { miswakDraw(a) }
}

private fun DrawScope.siwak76(c: Offset, s: Float, alpha: Float, rot: Float = -30f) {
    if (alpha <= 0.01f) return
    val d = Offset(cos(rot * PI.toFloat() / 180f), sin(rot * PI.toFloat() / 180f))
    drawLine(Color(0xFF9A7A50), c - d * (s * 0.5f), c + d * (s * 0.5f), s * 0.08f, StrokeCap.Round, alpha = alpha)
    for (f in 0 until 5) drawLine(Color(0xFFE8D8B0), c + d * (s * 0.5f), c + d * (s * 0.62f) + Offset(-d.y, d.x) * (s * 0.03f * (f - 2)), 1.5f * density, alpha = alpha)
}

private fun DrawScope.miswakDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    thrNight(t, h * 0.9f)
    val at = fqIn(a, "anytime", "aracktwig")
    val fm = fqIn(a, "fourmoments", "aracktwig")
    val ak = fqIn(a, "aracktwig", "fitrafive")
    val ff = fqIn(a, "fitrafive", "siwakbenefits")
    val sb = fqIn(a, "siwakbenefits")
    val c = Offset(w * 0.5f, h * 0.5f)
    val centre = maxOf(at, fm)
    if (centre > 0f) siwak76(c, h * 0.36f, centre)
    if (at > 0f) {
        val p = a.b("anytime")
        val ang = t * 0.6f
        sun12(c + Offset(cos(ang) * w * 0.3f, sin(ang) * h * 0.32f), w * 0.03f, at * (1f - fm))
        moon12(c - Offset(cos(ang) * w * 0.3f, sin(ang) * h * 0.32f), w * 0.025f, at * (1f - fm))
        tagAt(a.tag("anytime", 0, 0), Offset(w * 0.5f, h * 0.1f), at * seg(p, 0.1f, 0.3f) * (1f - fm), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("anytime", 0, 1), Offset(w * 0.5f, h * 0.9f), at * seg(p, 0.4f, 0.6f) * (1f - fm), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (fm > 0f) {
        val p = a.b("fourmoments")
        val spots = listOf(Offset(w * 0.8f, h * 0.28f), Offset(w * 0.8f, h * 0.72f), Offset(w * 0.2f, h * 0.28f), Offset(w * 0.2f, h * 0.72f))
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.22f * k, 0.22f + 0.22f * k)
            val s = spots[k]
            drawLine(ThrC.Silver, c, s, 1.2f * dp, alpha = fm * g * 0.4f)
            softGlow(s, w * 0.06f, ThrC.Gold, 0.3f * fm * g)
            when (k) {
                0 -> fqEwer(s + Offset(0f, h * 0.06f), h * 0.12f, ThrC.Clay, fm * g)
                1 -> fqRug(s, w * 0.07f, h * 0.12f, Color(0xFF6A2A34), ThrC.Gold, fm * g)
                2 -> openBook21(s, w * 0.1f, Color(0xFFF2E8CE), Color(0xFFB8A888), fm * g)
                else -> sun12(s, w * 0.025f, fm * g)
            }
            tagAt(a.tag("fourmoments", 0, k), s + Offset(0f, (if (k % 2 == 0) -1f else 1f) * h * 0.11f), fm * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (ak > 0f) {
        val p = a.b("aracktwig")
        val root = Offset(w * 0.75f, h * 0.85f)
        drawLine(Color(0xFF6A5A3A), root, root + Offset(-w * 0.05f, -h * 0.55f), w * 0.015f, StrokeCap.Round, alpha = ak)
        for (k in 0 until 6) {
            val b = root + Offset(-w * 0.01f * k, -h * (0.15f + 0.07f * k))
            val dir = if (k % 2 == 0) 1f else -1f
            drawLine(Color(0xFF6A5A3A), b, b + Offset(dir * w * 0.08f, -h * 0.05f), w * 0.006f, StrokeCap.Round, alpha = ak)
            drawOval(ThrC.Green, Offset(b.x + dir * w * 0.08f - w * 0.025f, b.y - h * 0.07f), Size(w * 0.05f, h * 0.03f), alpha = ak)
        }
        tagAt(a.tag("aracktwig", 0, 1), Offset(w * 0.75f, h * 0.12f), ak * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Jade)
        val cut = easeInOut(seg(p, 0.35f, 0.65f))
        siwak76(Offset(mix(w * 0.72f, w * 0.35f, cut), mix(h * 0.5f, h * 0.55f, cut)), h * 0.3f, ak * seg(p, 0.3f, 0.4f), -20f)
        tagAt(a.tag("aracktwig", 0, 0), Offset(w * 0.3f, h * 0.3f), ak * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ff > 0f) {
        val p = a.b("fitrafive")
        fqScissors(c, w * 0.1f, ThrC.Silver, ff, 0.5f + 0.5f * sin(t * 4f))
        for (k in 0 until 5) {
            val g = seg(p, 0.1f + 0.12f * k, 0.25f + 0.12f * k)
            val ang = -PI.toFloat() / 2f + 2f * PI.toFloat() * k / 5f
            val s = c + Offset(cos(ang) * w * 0.22f, sin(ang) * h * 0.3f)
            softGlow(s, w * 0.05f, ThrC.Gold, 0.5f * ff * g)
            drawCircle(ThrC.Gold, w * 0.015f, s, alpha = ff * g)
        }
        tagAt(a.tag("fitrafive"), Offset(w * 0.5f, h * 0.92f), ff * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sb > 0f) {
        val p = a.b("siwakbenefits")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val s = Offset(w * (0.78f - 0.28f * k), h * 0.48f)
            val ic = w * 0.08f
            when (k) {
                0 -> drawPath(Path().apply {
                    moveTo(s.x - ic * 0.5f, s.y - ic * 0.6f); quadraticTo(s.x, s.y - ic * 0.85f, s.x + ic * 0.5f, s.y - ic * 0.6f)
                    lineTo(s.x + ic * 0.35f, s.y + ic * 0.7f); lineTo(s.x + ic * 0.1f, s.y + ic * 0.2f); lineTo(s.x - ic * 0.1f, s.y + ic * 0.2f)
                    lineTo(s.x - ic * 0.35f, s.y + ic * 0.7f); close()
                }, Color(0xFFF2EEE2), alpha = sb * g)
                1 -> {
                    drawArc(Color(0xFFD88A8A), 180f, 180f, true, Offset(s.x - ic * 0.9f, s.y - ic * 0.5f), Size(ic * 1.8f, ic), alpha = sb * g)
                    for (j in 0 until 4) drawRect(Color(0xFFF2EEE2), Offset(s.x - ic * 0.7f + j * ic * 0.38f, s.y - ic * 0.1f), Size(ic * 0.3f, ic * 0.4f), alpha = sb * g)
                }
                else -> {
                    voice12(s + Offset(-ic * 0.3f, 0f), ic * 0.8f, t, ThrC.Water, sb * g, 1f)
                    voice12(s + Offset(ic * 0.3f, 0f), ic * 0.8f, t, ThrC.Water, sb * g, -1f)
                }
            }
            softGlow(s, w * 0.08f, ThrC.Gold, 0.3f * sb * g)
            tagAt(a.tag("siwakbenefits", 0, k), s + Offset(0f, h * 0.18f), sb * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
}

// ═════════════ 5. thr-wudu: the obligatory acts of wuḍūʾ ═════════════

/**
 * Beats: wuduneeded (three things that ask for wuḍūʾ, each waiting on a drop: «كَالطَّوَافِ وَمَسِّ الْمُصْحَفِ»), sixfard (a plain figure; water
 * runs over the face, the arms, a wipe over the head, then the feet: «غَسْلُ الْوَجْهِ», «وَغَسْلُ الْيَدَيْنِ إِلَى الْمِرْفَقَيْنِ», «وَمَسْحُ
 * الرَّأْسِ مَعَ الْأُذُنَيْنِ», «وَغَسْلُ الرِّجْلَيْنِ إِلَى الْكَعْبَيْنِ»), orderflow (four marks in order with no gap between them:
 * «وَالتَّرْتِيبُ», «وَالْمُوَالَاةُ»), wuduconds (a heart, a drop and a spot wiped off the skin: «النِّيَّةُ», «وَالْمَاءُ الطَّهُورُ», «وَإِزَالَةُ مَا
 * يَمْنَعُ وُصُولَ الْمَاءِ»), dryspot (a foot with one dry patch where the water did not reach: «لُمْعَةٌ لَمْ يُصِبْهَا الْمَاءُ»).
 */
internal fun DrawScope.wudusixScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "wuduneeded" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sixfard" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "orderflow" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "wuduconds" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "dryspot" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { wuduSixDraw(a) }
}

private fun DrawScope.wuduSixDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.86f
    thrNight(t, ground)
    val wn = fqIn(a, "wuduneeded", "sixfard")
    val sf = fqIn(a, "sixfard", "orderflow")
    val of = fqIn(a, "orderflow", "wuduconds")
    val wc = fqIn(a, "wuduconds", "dryspot")
    val ds = fqIn(a, "dryspot")
    if (wn > 0f) {
        val p = a.b("wuduneeded")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.2f * k, 0.25f + 0.2f * k)
            val s = Offset(w * (0.78f - 0.28f * k), h * 0.5f)
            when (k) {
                0 -> fqRug(s, w * 0.1f, h * 0.24f, Color(0xFF6A2A34), ThrC.Gold, wn * g)
                1 -> {
                    fqCircling(s + Offset(0f, h * 0.04f), w * 0.1f, h * 0.04f, t, ThrC.Person, wn * g, 10)
                    kaaba29(s + Offset(0f, h * 0.06f), w * 0.07f, wn * g)
                }
                else -> openBook21(s, w * 0.14f, Color(0xFFF2E8CE), Color(0xFFB8A888), wn * g)
            }
            fqDrop(s + Offset(0f, -h * 0.2f), w * 0.02f, ThrC.Water, wn * seg(p, 0.6f, 0.75f))
        }
        tagAt(a.tag("wuduneeded"), Offset(w * 0.5f, h * 0.84f), wn * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (sf > 0f) {
        val p = a.b("sixfard")
        val foot = Offset(w * 0.5f, ground)
        val tall = h * 0.66f
        figure(foot, tall, ThrC.Person, sf)
        val hip = foot - Offset(0f, tall * 0.45f)
        val sh = hip - Offset(0f, tall * 0.31f)
        val head = sh - Offset(0f, tall * 0.15f)
        val parts = listOf(head, sh + Offset(0f, tall * 0.14f), head + Offset(0f, -tall * 0.08f), foot)
        val spots = listOf(Offset(w * 0.8f, h * 0.18f), Offset(w * 0.82f, h * 0.48f), Offset(w * 0.2f, h * 0.18f), Offset(w * 0.2f, h * 0.78f))
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.22f * k, 0.2f + 0.22f * k)
            if (k == 1) {
                for (sg in listOf(-1f, 1f)) drawLine(ThrC.Water, sh + Offset(sg * tall * 0.05f, tall * 0.04f), sh + Offset(sg * tall * 0.12f, tall * 0.28f), tall * 0.045f, StrokeCap.Round, alpha = sf * g * 0.8f)
            } else if (k == 3) {
                drawOval(ThrC.Water, Offset(foot.x - tall * 0.16f, foot.y - tall * 0.05f), Size(tall * 0.32f, tall * 0.08f), alpha = sf * g * 0.8f)
            } else if (k == 2) {
                drawArc(ThrC.Water, 200f, 140f, false, Offset(head.x - tall * 0.12f, head.y - tall * 0.13f), Size(tall * 0.24f, tall * 0.2f), alpha = sf * g, style = Stroke(tall * 0.03f, cap = StrokeCap.Round))
            } else {
                drawCircle(ThrC.Water, tall * 0.1f, head, alpha = sf * g * 0.75f)
            }
            drawLine(ThrC.Silver, parts[k], spots[k], 1f * dp, alpha = sf * g * 0.4f)
            tagAt(a.tag("sixfard", 0, k), spots[k], sf * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (of > 0f) {
        val p = a.b("orderflow")
        drawRect(ThrC.Top, Offset.Zero, size, alpha = 0.7f * of)
        val y = h * 0.5f
        for (k in 0 until 4) {
            val g = seg(p, 0.05f + 0.12f * k, 0.15f + 0.12f * k)
            val c = Offset(w * (0.82f - 0.21f * k), y)
            drawCircle(ThrC.Water, w * 0.045f, c, alpha = of * g)
            for (j in 0..k) drawCircle(Color(0xFF0E1A2E), w * 0.006f, c + Offset(w * 0.012f * (j - k / 2f), 0f), alpha = of * g)
            if (k < 3) drawLine(ThrC.Water, c + Offset(-w * 0.05f, 0f), c + Offset(-w * 0.16f, 0f), 3f * dp, StrokeCap.Round, alpha = of * seg(p, 0.12f + 0.12f * k, 0.2f + 0.12f * k))
        }
        tagAt(a.tag("orderflow", 0, 0), Offset(w * 0.5f, h * 0.3f), of * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val flow = seg(p, 0.6f, 0.8f)
        drawLine(ThrC.Gold, Offset(w * 0.86f, y + h * 0.1f), Offset(mix(w * 0.86f, w * 0.14f, flow), y + h * 0.1f), 3f * dp, StrokeCap.Round, alpha = of)
        tagAt(a.tag("orderflow", 0, 1), Offset(w * 0.5f, h * 0.72f), of * flow, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (wc > 0f) {
        val p = a.b("wuduconds")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.25f * k, 0.25f + 0.25f * k)
            val s = Offset(w * (0.8f - 0.3f * k), h * 0.48f)
            softGlow(s, w * 0.08f, ThrC.Gold, 0.3f * wc * g)
            when (k) {
                0 -> heart12(s, w * 0.07f, ThrC.Gold, wc * g, fill = true)
                1 -> fqDrop(s + Offset(0f, h * 0.02f), w * 0.035f, ThrC.Water, wc * g)
                else -> {
                    drawRoundRect(Color(0xFFD8B898), Offset(s.x - w * 0.09f, s.y - h * 0.06f), Size(w * 0.18f, h * 0.12f), CornerRadius(h * 0.05f), alpha = wc * g)
                    drawCircle(Color(0xFF8A3A5A), w * 0.025f, s, alpha = wc * g * (1f - seg(p, 0.75f, 0.95f)))
                }
            }
            tagAt(a.tag("wuduconds", 0, k), s + Offset(0f, h * 0.18f), wc * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (ds > 0f) {
        val p = a.b("dryspot")
        val c = Offset(w * 0.5f, h * 0.55f)
        val s = h * 0.5f
        drawPath(Path().apply {
            moveTo(c.x - s * 0.5f, c.y + s * 0.2f); quadraticTo(c.x - s * 0.55f, c.y - s * 0.15f, c.x - s * 0.1f, c.y - s * 0.2f)
            lineTo(c.x + s * 0.5f, c.y - s * 0.05f); quadraticTo(c.x + s * 0.62f, c.y + s * 0.15f, c.x + s * 0.45f, c.y + s * 0.22f); close()
        }, Color(0xFFD8B898), alpha = ds)
        drawPath(Path().apply {
            moveTo(c.x - s * 0.5f, c.y + s * 0.2f); quadraticTo(c.x - s * 0.55f, c.y - s * 0.15f, c.x - s * 0.1f, c.y - s * 0.2f)
            lineTo(c.x + s * 0.5f, c.y - s * 0.05f); quadraticTo(c.x + s * 0.62f, c.y + s * 0.15f, c.x + s * 0.45f, c.y + s * 0.22f); close()
        }, ThrC.Water, alpha = ds * 0.45f * seg(p, 0.05f, 0.3f))
        val dry = c + Offset(s * 0.05f, -s * 0.05f)
        drawCircle(Color(0xFFE8C8A0), s * 0.05f, dry, alpha = ds * seg(p, 0.3f, 0.45f))
        drawCircle(ThrC.Gold, s * 0.08f, dry, alpha = ds * seg(p, 0.35f, 0.5f), style = Stroke(2f * dp))
        tagAt(a.tag("dryspot"), Offset(w * 0.5f, h * 0.18f), ds * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 6. thr-sunan: the sunnahs of wuḍūʾ ═════════════

/**
 * Beats: rewardnoblame (stars gather on a scale for the one who does them, and nothing is taken from the one who leaves them: «يُؤْجَرُ مَنْ
 * فَعَلَهَا», «وَلَا حَرَجَ عَلَى مَنْ تَرَكَهَا»), startsunnahs (a word of the name, a siwak, and water three times on the hands: «التَّسْمِيَةُ»,
 * «وَالسِّوَاكُ», «وَغَسْلُ الْكَفَّيْنِ ثَلَاثًا»), rightthrice (the right hand lit before the left, and three pours: «تَقْدِيمُ الْيُمْنَى عَلَى
 * الْيُسْرَى», «وَتَثْلِيثُ الْغَسْلِ»), onceorthrice (one drop, then three in gold: «فَالْوَاجِبُ مَرَّةٌ وَاحِدَةٌ», «وَيُسْتَحَبُّ ثَلَاثًا»),
 * allthreeways (rows of one, two and three drops, each ticked: «مَرَّةً مَرَّةً», «وَمَرَّتَيْنِ مَرَّتَيْنِ», «وَثَلَاثًا ثَلَاثًا»).
 */
internal fun DrawScope.wudusunnahsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rewardnoblame" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "startsunnahs" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "rightthrice" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "onceorthrice" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "allthreeways" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { wuduSunnahsDraw(a) }
}

/** A hand seen from the back, fingers up; [right] mirrors the thumb. */
private fun DrawScope.hand76(c: Offset, s: Float, color: Color, alpha: Float, right: Boolean) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, Offset(c.x - s * 0.22f, c.y - s * 0.1f), Size(s * 0.44f, s * 0.5f), CornerRadius(s * 0.12f), alpha = alpha)
    for (k in 0 until 4) drawLine(color, Offset(c.x - s * 0.16f + k * s * 0.107f, c.y - s * 0.05f), Offset(c.x - s * 0.16f + k * s * 0.107f, c.y - s * (0.42f - 0.04f * kotlin.math.abs(k - 1.5f))), s * 0.09f, StrokeCap.Round, alpha = alpha)
    val m = if (right) -1f else 1f
    drawLine(color, Offset(c.x + m * s * 0.2f, c.y + s * 0.15f), Offset(c.x + m * s * 0.36f, c.y - s * 0.08f), s * 0.09f, StrokeCap.Round, alpha = alpha)
}

private fun DrawScope.wuduSunnahsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    thrNight(t, h * 0.9f)
    val rn = fqIn(a, "rewardnoblame", "startsunnahs")
    val ss = fqIn(a, "startsunnahs", "rightthrice")
    val rt = fqIn(a, "rightthrice", "onceorthrice")
    val ot = fqIn(a, "onceorthrice", "allthreeways")
    val aw = fqIn(a, "allthreeways")
    if (rn > 0f) {
        val p = a.b("rewardnoblame")
        val c = Offset(w * 0.72f, h * 0.55f)
        for (k in 0 until 6) {
            val u = seg(p, 0.1f + 0.08f * k, 0.25f + 0.08f * k)
            drawPath(starPath(Offset(c.x + w * 0.03f * (k % 3 - 1), mix(h * 0.1f, c.y - h * 0.04f * (k / 3), easeOut(u))), w * 0.018f), ThrC.Gold, alpha = rn * u)
        }
        fqCoins(c + Offset(0f, h * 0.15f), w * 0.05f, 2, rn * 0.6f)
        tagAt(a.tag("rewardnoblame", 0, 0), Offset(w * 0.72f, h * 0.88f), rn * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(p, 0.55f, 0.75f)
        drawCircle(ThrC.Silver, w * 0.08f, Offset(w * 0.28f, h * 0.5f), alpha = rn * g * 0.5f, style = Stroke(2f * dp))
        tagAt(a.tag("rewardnoblame", 0, 1), Offset(w * 0.28f, h * 0.76f), rn * g, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ss > 0f) {
        val p = a.b("startsunnahs")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.25f * k, 0.25f + 0.25f * k)
            val s = Offset(w * (0.8f - 0.3f * k), h * 0.48f)
            when (k) {
                0 -> voice12(s, w * 0.08f, t, ThrC.Gold, ss * g, 1f)
                1 -> siwak76(s, h * 0.25f, ss * g)
                else -> {
                    hand76(s + Offset(-w * 0.04f, h * 0.05f), h * 0.2f, ThrC.Person, ss * g, false)
                    hand76(s + Offset(w * 0.04f, h * 0.05f), h * 0.2f, ThrC.Person, ss * g, true)
                    for (j in 0 until 3) fqDrop(s + Offset(w * (-0.03f + 0.03f * j), -h * 0.15f + h * 0.05f * fract(t + j / 3f)), w * 0.012f, ThrC.Water, ss * g)
                }
            }
            tagAt(a.tag("startsunnahs", 0, k), s + Offset(0f, h * 0.2f), ss * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (rt > 0f) {
        val p = a.b("rightthrice")
        val r = seg(p, 0.05f, 0.25f)
        val l = seg(p, 0.3f, 0.45f)
        hand76(Offset(w * 0.62f, h * 0.5f), h * 0.3f, lerpColor(ThrC.Person, ThrC.Gold, r * 0.5f), rt, true)
        softGlow(Offset(w * 0.62f, h * 0.45f), w * 0.08f, ThrC.Gold, 0.5f * rt * r)
        hand76(Offset(w * 0.38f, h * 0.5f), h * 0.3f, lerpColor(ThrC.Person, ThrC.Gold, l * 0.5f), rt, false)
        tagAt(a.tag("rightthrice", 0, 0), Offset(w * 0.5f, h * 0.14f), rt * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        for (k in 0 until 3) {
            val u = seg(p, 0.5f + 0.13f * k, 0.6f + 0.13f * k)
            if (u > 0f && u < 1f) rain12(w * 0.55f, w * 0.69f, h * 0.2f, h * 0.65f, t * 2f, ThrC.Water, rt, 12)
            drawCircle(ThrC.Water, w * 0.012f, Offset(w * (0.56f + 0.05f * k), h * 0.82f), alpha = rt * seg(u, 0.9f, 1f))
        }
        tagAt(a.tag("rightthrice", 0, 1), Offset(w * 0.5f, h * 0.92f), rt * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ot > 0f) {
        val p = a.b("onceorthrice")
        val g0 = seg(p, 0.05f, 0.25f)
        fqDrop(Offset(w * 0.72f, h * 0.48f), w * 0.04f, ThrC.Water, ot * g0)
        tagAt(a.tag("onceorthrice", 0, 0), Offset(w * 0.72f, h * 0.72f), ot * g0, back = Fq.Back, ink = ArtInk.Ivory)
        val g1 = seg(p, 0.45f, 0.65f)
        for (k in 0 until 3) fqDrop(Offset(w * (0.2f + 0.08f * k), h * 0.48f), w * 0.035f, ThrC.Gold, ot * seg(p, 0.45f + 0.07f * k, 0.55f + 0.07f * k))
        softGlow(Offset(w * 0.28f, h * 0.46f), w * 0.12f, ThrC.Gold, 0.3f * ot * g1)
        tagAt(a.tag("onceorthrice", 0, 1), Offset(w * 0.28f, h * 0.72f), ot * g1, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (aw > 0f) {
        val p = a.b("allthreeways")
        for (r in 0 until 3) {
            val g = seg(p, 0.05f + 0.27f * r, 0.25f + 0.27f * r)
            val y = h * (0.28f + 0.22f * r)
            for (k in 0..r) fqDrop(Offset(w * (0.78f - 0.07f * k), y), w * 0.022f, ThrC.Water, aw * g)
            tick14(Offset(w * 0.88f, y - h * 0.02f), w * 0.025f, seg(p, 0.15f + 0.27f * r, 0.3f + 0.27f * r))
            tagAt(a.tag("allthreeways", 0, r), Offset(w * 0.35f, y - h * 0.02f), aw * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
}
