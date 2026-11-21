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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 63 (2026-11-03): «الأربعون النووية», hadiths 31–35. Doing without and being loved; no harm and no returning harm; proof
 * on the one who claims; changing a wrong by hand, tongue or heart; no envy, and be brothers. Allah is never drawn; prophets are
 * lights with their names; Iblis is dark smoke; companions are names in roundels; others are generic figures or plain roundels.
 */

private val Back63 = Color(0xE6181420)

// ═════════════ 1. do without, and be loved (nw-31) ═════════════

private object ZuhdC63 {
    val Night = Color(0xFF161A2C)
    val NightLow = Color(0xFF34324E)
    val Ground = Color(0xFF221E2C)
    val Gold = Color(0xFFF2C860)
    val Heart = Color(0xFFE88A8A)
    val Coin = Color(0xFFE8C25A)
    val Hand = Color(0xFFDCD4C4)
    val Weight = Color(0xFF3A3040)
    val Rung = Color(0xFFD8CCB4)
    val Plate = Color(0xFFD8C4A0)
    val Food = Color(0xFFC0602A)
    val Bad = Color(0xFFD06A4A)
    val Pillar = Color(0xFFE8DCC4)
}

/**
 * Beats: twoloves (a plain roundel asks; one light comes from above and hearts come from people round him: «يُحِبُّهُ اللَّهُ عَلَيْهِ
 * وَيُحِبُّهُ النَّاسُ»), stepaside (roundels crowd round a heap of coins; one steps back and hearts turn to him: «ازْهَدْ فِي
 * الدُّنْيَا يُحِبَّكَ اللَّهُ», «وَازْهَدْ فِيمَا عِنْدَ النَّاسِ يُحِبَّكَ النَّاسُ»), handvsheart (an empty hand over a heavy heart,
 * then a full hand over a light one), zuhdrungs (five rungs: «الشِّرْكِ وَالْكُفْرِ», «الْبِدَعِ», «الْمَعَاصِي», «الْمَكْرُوهِ»,
 * «فُضُولِ الْمُبَاحِ»), noshunhalal (a lawful plate pushed away as worship is struck out), poetfour (four columns with the poet's
 * four lines).
 */
internal fun DrawScope.zuhdloveScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twoloves" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "stepaside" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "handvsheart" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "zuhdrungs" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "noshunhalal" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "poetfour" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { zuhdNight(a) }
}

private fun DrawScope.zuhdNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tl = a.b("twoloves")
    val sa = a.b("stepaside")
    val hh = a.b("handvsheart")
    val zr = a.b("zuhdrungs")
    val ns = a.b("noshunhalal")
    val pf = a.b("poetfour")
    val ground = h * 0.86f
    nightSky(t, ZuhdC63.Night, ZuhdC63.NightLow, ground, starAlpha = 0.8f)
    drawRect(ZuhdC63.Ground, Offset(0f, ground), Size(w, h - ground))
    if (tl > 0f) {
        val f = easeOut(seg(tl, 0f, BEAT_IN)) * (1f - seg(sa, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.55f)
        nameRoundel(c, w * 0.06f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        lightShafts(Offset(w * 0.5f, 0f), PI.toFloat() / 2f, 0.25f, h * 0.48f, ZuhdC63.Gold, 0.45f * f * seg(tl, 0.2f, 0.45f), t, rays = 5)
        for (k in 0 until 5) {
            val ang = PI.toFloat() * (0.15f + 0.7f * k / 4f)
            val p = c + Offset(cos(ang) * w * 0.3f, sin(ang) * h * 0.2f)
            nameRoundel(p, w * 0.03f, null, Roundel.PlainFill, Roundel.PlainRim, f * seg(tl, 0.4f, 0.55f))
            heart12(p + (c - p) * seg(tl, 0.55f + 0.05f * k, 0.8f + 0.04f * k), w * 0.02f, ZuhdC63.Heart, f * seg(tl, 0.5f + 0.05f * k, 0.6f + 0.05f * k), fill = true)
        }
        tagAt(a.tag("twoloves"), Offset(w * 0.5f, h * 0.2f), f * seg(tl, 0.1f, 0.3f), back = Back63, ink = ArtInk.Gold)
    }
    if (sa > 0f) {
        val f = easeOut(seg(sa, 0f, BEAT_IN)) * (1f - seg(hh, 0f, BEAT_OUT))
        val heap = Offset(w * 0.4f, ground - h * 0.04f)
        for (k in 0 until 9) coin12(heap + Offset(w * (0.03f * (k % 4) - 0.045f), -h * 0.025f * (k / 4)), w * 0.018f, f, ZuhdC63.Coin)
        for (k in 0 until 6) {
            val ang = PI.toFloat() * (1.05f + 0.9f * k / 5f)
            nameRoundel(heap + Offset(cos(ang) * w * 0.14f + sin(t * 6f + k) * w * 0.004f, sin(ang) * h * 0.1f), w * 0.026f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        }
        val back = easeInOut(seg(sa, 0.2f, 0.5f))
        val me = Offset(mix(w * 0.55f, w * 0.8f, back), ground - h * 0.12f)
        nameRoundel(me, w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        for (k in 0 until 3) heart12(me + Offset(w * 0.05f * (k - 1), -h * 0.1f - h * 0.03f * (k % 2)), w * 0.018f, ZuhdC63.Heart, f * seg(sa, 0.55f + 0.08f * k, 0.65f + 0.08f * k), fill = true)
        softGlow(me, w * 0.08f, ZuhdC63.Gold, 0.4f * f * seg(sa, 0.5f, 0.7f))
        tagAt(a.tag("stepaside", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(sa, 0.05f, 0.2f), back = Back63, ink = ArtInk.Gold)
        tagAt(a.tag("stepaside", 0, 1), Offset(w * 0.5f, h * 0.32f), f * seg(sa, 0.4f, 0.55f), back = Back63, ink = ArtInk.Gold)
    }
    if (hh > 0f) {
        val f = easeOut(seg(hh, 0f, BEAT_IN)) * (1f - seg(zr, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val g = seg(hh, 0.1f + 0.4f * k, 0.3f + 0.4f * k)
            val x = w * (0.7f - 0.4f * k)
            val hand = Offset(x, h * 0.65f)
            hands12(hand, h * 0.12f, ZuhdC63.Hand, f * g)
            if (k == 1) for (j in 0 until 5) coin12(hand + Offset(w * 0.02f * (j - 2), -h * 0.07f), w * 0.014f, f * g, ZuhdC63.Coin)
            val heart = Offset(x, h * 0.35f)
            heart12(heart, w * 0.06f, if (k == 0) ZuhdC63.Weight else ZuhdC63.Gold, f * g, fill = true)
            if (k == 0) for (j in 0 until 3) drawRect(ZuhdC63.Weight, Offset(heart.x - w * 0.05f + w * 0.035f * j, heart.y + h * 0.06f), Size(w * 0.025f, h * 0.03f), alpha = f * g)
            else softGlow(heart, w * 0.1f, ZuhdC63.Gold, 0.5f * f * g)
        }
    }
    if (zr > 0f) {
        val f = easeOut(seg(zr, 0f, BEAT_IN)) * (1f - seg(ns, 0f, BEAT_OUT))
        drawLine(ZuhdC63.Rung, Offset(w * 0.32f, h * 0.15f), Offset(w * 0.32f, h * 0.9f), 4f * dp, alpha = f)
        drawLine(ZuhdC63.Rung, Offset(w * 0.68f, h * 0.15f), Offset(w * 0.68f, h * 0.9f), 4f * dp, alpha = f)
        for (k in 0 until 5) {
            val g = seg(zr, 0.08f + 0.17f * k, 0.22f + 0.17f * k)
            val y = h * (0.2f + 0.15f * k)
            drawLine(if (k == 0) ZuhdC63.Gold else ZuhdC63.Rung, Offset(w * 0.32f, y), Offset(w * 0.68f, y), 4f * dp, alpha = f * g)
            tagAt(a.tag("zuhdrungs", 0, k), Offset(w * 0.5f, y - h * 0.04f), f * g, back = Back63, ink = if (k == 0) ArtInk.Gold else Color.Unspecified)
        }
    }
    if (ns > 0f) {
        val f = easeOut(seg(ns, 0f, BEAT_IN)) * (1f - seg(pf, 0f, BEAT_OUT))
        val plate = Offset(w * 0.5f, h * 0.6f)
        val push = easeInOut(seg(ns, 0.15f, 0.45f))
        val p = plate + Offset(w * 0.2f * push, 0f)
        drawOval(ZuhdC63.Plate, Offset(p.x - w * 0.12f, p.y - h * 0.025f), Size(w * 0.24f, h * 0.05f), alpha = f)
        for (k in 0 until 4) drawCircle(ZuhdC63.Food, w * 0.015f, p + Offset(w * 0.04f * (k - 1.5f), -h * 0.02f), alpha = f)
        hands12(Offset(w * 0.25f, h * 0.62f), h * 0.1f, ZuhdC63.Hand, f)
        crossMark(Offset(w * 0.5f, h * 0.42f), w * 0.06f, ZuhdC63.Bad, 4f * dp, f * seg(ns, 0.5f, 0.7f))
    }
    if (pf > 0f) {
        val f = easeOut(seg(pf, 0f, 0.25f))
        for (k in 0 until 4) {
            val g = seg(pf, 0.08f + 0.2f * k, 0.25f + 0.2f * k)
            val x = w * (0.82f - 0.21f * k)
            drawRect(ZuhdC63.Pillar, Offset(x - w * 0.035f, ground - h * 0.4f * g), Size(w * 0.07f, h * 0.4f * g), alpha = f)
            tagAt(a.tag("poetfour", 0, k), Offset(x, ground - h * 0.48f - h * 0.06f * (k % 2)), f * g, back = Back63, ink = ArtInk.Gold)
        }
        drawRect(ZuhdC63.Rung, Offset(w * 0.08f, ground - h * 0.44f), Size(w * 0.84f, h * 0.03f), alpha = f * seg(pf, 0.85f, 1f))
    }
}

// ═════════════ 2. no harm, no returning harm (nw-32) ═════════════

private object HarmC63 {
    val Sky = Color(0xFFC4D8E6)
    val SkyLow = Color(0xFFF2E6CE)
    val Ground = Color(0xFFCDB890)
    val Dark = Color(0xFF3A3040)
    val Shield = Color(0xFF7AB8E0)
    val Bad = Color(0xFFB4492A)
    val Door = Color(0xFF8A5A3A)
    val Frame = Color(0xFF5A3A22)
    val Fence = Color(0xFF6A4A32)
    val Grass = Color(0xFF8AB46A)
    val Sheep = Color(0xFFF2EEE4)
    val Wall = Color(0xFFE2D2B4)
    val Trim = Color(0xFF8A6A44)
    val Smoke = Color(0xFF6A6A70)
    val Gold = Color(0xFFD89A2A)
    val Rope = Color(0xFFB89A6A)
}

/**
 * Beats: harmwall (one roundel's dark push toward another is stopped by a wall of light), twoharms (two dark arrows, the first harm
 * and the harm sent back, both struck: «الضَّرَرُ الِابْتِدَاءُ», «وَالضِّرَارُ الْمُجَازَاةُ»), fourchapters (four doors open in turn:
 * «الرَّضَاعِ», «وَالنَّفَقَاتِ», «وَالْحِمَى», «وَالْجِوَارِ»), openpasture (a fenced pasture opens its gate and everyone's sheep come
 * in), noharmneighbor (a house sends smoke onto its neighbour; the smoke is stopped and turned back), threadsstrong (thin threads
 * twist into a strong rope: «يُقَوِّي بَعْضُهَا بَعْضًا»).
 */
internal fun DrawScope.harmblockedScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "harmwall" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "twoharms" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "fourchapters" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
        "openpasture" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
        "noharmneighbor" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "threadsstrong" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { harmDay(a) }
}

private fun DrawScope.harmDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val hw = a.b("harmwall")
    val th = a.b("twoharms")
    val fc = a.b("fourchapters")
    val op = a.b("openpasture")
    val nn = a.b("noharmneighbor")
    val ts = a.b("threadsstrong")
    val ground = h * 0.84f
    daySky12(HarmC63.Sky, HarmC63.SkyLow, ground, HarmC63.Ground)
    if (hw > 0f) {
        val f = easeOut(seg(hw, 0f, BEAT_IN)) * (1f - seg(th, 0f, 0.4f))
        val l = Offset(w * 0.78f, h * 0.55f)
        val r = Offset(w * 0.22f, h * 0.55f)
        nameRoundel(l, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(r, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val push = easeOut(seg(hw, 0.1f, 0.45f))
        drawCircle(HarmC63.Dark, w * 0.03f, Offset(mix(l.x - w * 0.08f, w * 0.55f, push), l.y), alpha = f * (1f - seg(hw, 0.6f, 0.8f)))
        val wall = seg(hw, 0.3f, 0.5f)
        drawLine(HarmC63.Shield, Offset(w * 0.5f, h * 0.55f - h * 0.18f * wall), Offset(w * 0.5f, h * 0.55f + h * 0.18f * wall), 6f * dp, StrokeCap.Round, alpha = f)
        softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.1f, HarmC63.Shield, 0.5f * f * wall)
    }
    if (th > 0f) {
        val f = easeOut(seg(th, 0f, 0.25f)) * (1f - seg(fc, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val y = h * (0.38f + 0.25f * k)
            val dir = if (k == 0) -1f else 1f
            val g = seg(th, 0.1f + 0.35f * k, 0.3f + 0.35f * k)
            val from = Offset(w * 0.5f - dir * w * 0.25f, y)
            val to = Offset(w * 0.5f + dir * w * 0.25f, y)
            drawLine(HarmC63.Dark, from, from + (to - from) * g, 4f * dp, StrokeCap.Round, alpha = f)
            arrowHead(from + (to - from) * g, to - from, w * 0.025f, HarmC63.Dark, 4f * dp, f * g)
            crossMark(Offset(w * 0.5f, y), w * 0.05f, HarmC63.Bad, 4f * dp, f * seg(th, 0.25f + 0.35f * k, 0.4f + 0.35f * k))
            tagAt(a.tag("twoharms", 0, k), Offset(w * 0.5f, y - h * 0.08f), f * g, back = Back63)
        }
    }
    if (fc > 0f) {
        val f = easeOut(seg(fc, 0f, BEAT_IN)) * (1f - seg(op, 0f, BEAT_OUT))
        for (k in 0 until 4) {
            val g = seg(fc, 0.08f + 0.2f * k, 0.25f + 0.2f * k)
            val sill = Offset(w * (0.82f - 0.21f * k), ground)
            door12(sill, w * 0.12f, h * 0.3f, g, HarmC63.Frame, HarmC63.Door, f)
            tagAt(a.tag("fourchapters", 0, k), sill + Offset(0f, -h * 0.38f), f * g, back = Back63, ink = ArtInk.Gold)
        }
    }
    if (op > 0f) {
        val f = easeOut(seg(op, 0f, BEAT_IN)) * (1f - seg(nn, 0f, BEAT_OUT))
        drawRect(HarmC63.Grass, Offset(w * 0.1f, ground - h * 0.18f), Size(w * 0.5f, h * 0.18f), alpha = f * 0.6f)
        for (k in 0..8) {
            val x = w * (0.1f + 0.0625f * k)
            if (k in 7..8 && op > 0.3f) continue
            drawLine(HarmC63.Fence, Offset(x, ground), Offset(x, ground - h * 0.08f), 3f * dp, StrokeCap.Round, alpha = f)
        }
        val gate = easeInOut(seg(op, 0.2f, 0.4f))
        drawLine(HarmC63.Fence, Offset(w * 0.6f, ground - h * 0.05f), Offset(w * 0.6f + w * 0.1f * cos(gate * 1.4f), ground - h * 0.05f - h * 0.06f * sin(gate * 1.4f)), 3f * dp, alpha = f)
        for (k in 0 until 5) {
            val u = easeInOut(seg(op, 0.35f + 0.08f * k, 0.75f + 0.05f * k))
            sheep26(Offset(mix(w * (0.75f + 0.05f * k), w * (0.2f + 0.08f * k), u), ground - h * 0.05f), w * 0.05f, HarmC63.Sheep, f)
        }
    }
    if (nn > 0f) {
        val f = easeOut(seg(nn, 0f, BEAT_IN)) * (1f - seg(ts, 0f, BEAT_OUT))
        val a1 = Offset(w * 0.7f, ground)
        val b1 = Offset(w * 0.3f, ground)
        house12(a1, w * 0.22f, HarmC63.Wall, HarmC63.Trim, f, 0.3f)
        house12(b1, w * 0.22f, HarmC63.Wall, HarmC63.Trim, f, 0.6f)
        val go = seg(nn, 0.1f, 0.4f) * (1f - easeInOut(seg(nn, 0.5f, 0.85f)))
        smoke12(a1 + Offset(-w * 0.05f, -h * 0.2f), Offset(mix(a1.x, b1.x + w * 0.05f, go), h * 0.4f), seg(nn, 0.1f, 0.4f), t, f * (1f - seg(nn, 0.75f, 0.9f)), HarmC63.Smoke)
        drawLine(HarmC63.Shield, Offset(w * 0.5f, ground), Offset(w * 0.5f, ground - h * 0.35f * seg(nn, 0.4f, 0.55f)), 5f * dp, StrokeCap.Round, alpha = f)
    }
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, 0.25f))
        val tw = easeInOut(seg(ts, 0.15f, 0.7f))
        for (k in 0 until 5) {
            val y0 = h * (0.3f + 0.1f * k)
            val path = Path().apply {
                moveTo(w * 0.9f, y0)
                for (j in 1..30) {
                    val x = w * 0.9f - w * 0.8f * j / 30f
                    val yy = mix(y0, h * 0.5f + sin(j * 0.8f + k * 1.25f) * h * 0.02f, minOf(1f, tw * 1.4f * j / 30f + tw * 0.4f))
                    lineTo(x, yy)
                }
            }
            drawPath(path, HarmC63.Rope, alpha = f, style = Stroke((1.5f + 3f * tw) * dp, cap = StrokeCap.Round))
        }
        softGlow(Offset(w * 0.2f, h * 0.5f), w * 0.1f, HarmC63.Gold, 0.5f * f * seg(ts, 0.6f, 0.85f))
        tagAt(a.tag("threadsstrong"), Offset(w * 0.5f, h * 0.18f), f * seg(ts, 0.5f, 0.7f), back = Back63, ink = ArtInk.Gold)
    }
}

// ═════════════ 3. proof on the claimant (nw-33) ═════════════

private object ProofC63 {
    val Night = Color(0xFF14182A)
    val NightLow = Color(0xFF2E3050)
    val Ground = Color(0xFF1E2030)
    val Gold = Color(0xFFF2C860)
    val Voice = Color(0xFF8A8AA8)
    val Coin = Color(0xFFE8C25A)
    val Wall = Color(0xFFC8B494)
    val Trim = Color(0xFF6A5040)
    val Beam = Color(0xFFB89A62)
    val Block = Color(0xFF7A6A8A)
    val Feather = Color(0xFFE8E2D6)
    val Thread = Color(0xFFD06A5A)
    val Good = Color(0xFF6FCB9F)
}

/**
 * Beats: bareclaims (many voices reach for coins and houses with nothing but words), proofoath (a scale: a heavy block on the
 * claimant's side, a light feather on the other's: «الْبَيِّنَةُ», «وَالْيَمِينُ»), twoaimsqada (a tangle is cut, and a coin goes
 * back to its owner: «قَطْعُ النِّزَاعِ», «وَإِيصَالُ الْحُقُوقِ إِلَى أَرْبَابِهَا»), halfwaymeet (two roundels come to meet in
 * the middle: «الصُّلْحُ جَائِزٌ بَيْنَ الْمُسْلِمِينَ»), halfdebt (two roundels with their names, «كَعْبٍ» and «ابْنِ أَبِي
 * حَدْرَدٍ»; half of a pile of coins is set aside), bothcontent (a roundel with his name, «أَبُو شُرَيْحٍ», between two groups who
 * both glow content).
 */
internal fun DrawScope.proofonclaimantScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "bareclaims" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "proofoath" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "twoaimsqada" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "halfwaymeet" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "halfdebt" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "bothcontent" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { proofNight(a) }
}

private fun DrawScope.proofNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bc = a.b("bareclaims")
    val po = a.b("proofoath")
    val ta = a.b("twoaimsqada")
    val hm = a.b("halfwaymeet")
    val hd = a.b("halfdebt")
    val bt = a.b("bothcontent")
    val ground = h * 0.86f
    nightSky(t, ProofC63.Night, ProofC63.NightLow, ground, starAlpha = 0.8f)
    drawRect(ProofC63.Ground, Offset(0f, ground), Size(w, h - ground))
    if (bc > 0f) {
        val f = easeOut(seg(bc, 0f, BEAT_IN)) * (1f - seg(po, 0f, BEAT_OUT))
        house12(Offset(w * 0.3f, ground), w * 0.2f, ProofC63.Wall, ProofC63.Trim, f, 0.4f)
        for (k in 0 until 6) coin12(Offset(w * (0.65f + 0.03f * (k % 3)), ground - h * (0.03f + 0.03f * (k / 3))), w * 0.018f, f, ProofC63.Coin)
        for (k in 0 until 6) {
            val c = Offset(w * (0.1f + 0.16f * k), h * (0.25f + 0.08f * (k % 2)))
            voice12(c, w * 0.035f, t + k, ProofC63.Voice, f * seg(bc, 0.1f + 0.08f * k, 0.25f + 0.08f * k), if (k % 2 == 0) 1f else -1f)
        }
    }
    if (po > 0f) {
        val f = easeOut(seg(po, 0f, BEAT_IN)) * (1f - seg(ta, 0f, BEAT_OUT))
        val foot = Offset(w * 0.5f, ground)
        val s = h * 0.6f
        val tilt = 0.5f * easeInOut(seg(po, 0.3f, 0.6f))
        balance12(foot, s, tilt, ProofC63.Beam, f)
        val (lp, rp) = balancePans12(foot, s, tilt)
        drawRect(ProofC63.Block, Offset(lp.x - w * 0.04f, lp.y - h * 0.07f), Size(w * 0.08f, h * 0.065f), alpha = f * seg(po, 0.1f, 0.25f))
        drawOval(ProofC63.Feather, Offset(rp.x - w * 0.04f, rp.y - h * 0.025f), Size(w * 0.08f, h * 0.02f), alpha = f * seg(po, 0.4f, 0.55f))
        tagAt(a.tag("proofoath", 0, 0), lp + Offset(0f, -h * 0.14f), f * seg(po, 0.1f, 0.3f), back = Back63, ink = ArtInk.Gold)
        tagAt(a.tag("proofoath", 0, 1), rp + Offset(0f, -h * 0.1f), f * seg(po, 0.4f, 0.6f), back = Back63)
    }
    if (ta > 0f) {
        val f = easeOut(seg(ta, 0f, BEAT_IN)) * (1f - seg(hm, 0f, BEAT_OUT))
        val c = Offset(w * 0.7f, h * 0.5f)
        val cut = seg(ta, 0.25f, 0.4f)
        val tangle = Path().apply {
            moveTo(c.x - w * 0.15f, c.y)
            for (j in 1..20) lineTo(c.x - w * 0.15f + w * 0.3f * j / 20f, c.y + sin(j * 1.9f) * h * 0.06f * (1f - cut))
        }
        drawPath(tangle, ProofC63.Thread, alpha = f * (1f - 0.6f * cut), style = Stroke(3f * dp, cap = StrokeCap.Round))
        if (cut > 0f) drawLine(ProofC63.Gold, Offset(c.x, c.y - h * 0.08f), Offset(c.x, c.y + h * 0.08f), 3f * dp, StrokeCap.Round, alpha = f * cut)
        val back = easeInOut(seg(ta, 0.55f, 0.85f))
        val owner = Offset(w * 0.2f, h * 0.55f)
        nameRoundel(owner, w * 0.045f, null, Roundel.PlainFill, Roundel.PlainRim, f * seg(ta, 0.45f, 0.6f))
        coin12(Offset(mix(w * 0.45f, owner.x, back), mix(h * 0.4f, owner.y - h * 0.08f, back)), w * 0.025f, f * seg(ta, 0.5f, 0.6f), ProofC63.Coin)
        tagAt(a.tag("twoaimsqada", 0, 0), Offset(w * 0.7f, h * 0.3f), f * seg(ta, 0.15f, 0.35f), back = Back63, ink = ArtInk.Gold)
        tagAt(a.tag("twoaimsqada", 0, 1), Offset(w * 0.3f, h * 0.75f), f * seg(ta, 0.6f, 0.8f), back = Back63, ink = ArtInk.Gold)
    }
    if (hm > 0f) {
        val f = easeOut(seg(hm, 0f, BEAT_IN)) * (1f - seg(hd, 0f, BEAT_OUT))
        val meet = easeInOut(seg(hm, 0.1f, 0.6f))
        for (k in 0 until 2) {
            val dir = if (k == 0) 1f else -1f
            nameRoundel(Offset(w * 0.5f + dir * mix(w * 0.35f, w * 0.06f, meet), h * 0.55f), w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        }
        softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.1f, ProofC63.Good, 0.6f * f * seg(hm, 0.55f, 0.8f))
        tagAt(a.tag("halfwaymeet"), Offset(w * 0.5f, h * 0.3f), f * seg(hm, 0.4f, 0.6f), back = Back63, ink = ArtInk.Gold)
    }
    if (hd > 0f) {
        val f = easeOut(seg(hd, 0f, BEAT_IN)) * (1f - seg(bt, 0f, 0.4f))
        nameRoundel(Offset(w * 0.78f, h * 0.45f), w * 0.09f, a.big("halfdebt", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(hd, 0.05f, 0.2f))
        nameRoundel(Offset(w * 0.22f, h * 0.45f), w * 0.09f, a.big("halfdebt", 0, 1), Roundel.Fills[1], Roundel.Rim, f * seg(hd, 0.15f, 0.3f))
        val half = easeInOut(seg(hd, 0.45f, 0.75f))
        for (k in 0 until 8) {
            val gone = k >= 4
            val base = Offset(w * (0.45f + 0.03f * (k % 4)), h * 0.72f - h * 0.03f * (k / 4))
            coin12(base + if (gone) Offset(w * 0.15f * half, h * 0.1f * half) else Offset.Zero, w * 0.018f, f * (if (gone) 1f - 0.7f * half else 1f), ProofC63.Coin)
        }
    }
    if (bt > 0f) {
        val f = easeOut(seg(bt, 0f, 0.25f))
        nameRoundel(Offset(w * 0.5f, h * 0.4f), w * 0.1f, a.big("bothcontent"), Roundel.Fills[2], Roundel.Rim, f * seg(bt, 0.05f, 0.2f))
        for (side in 0 until 2) for (k in 0 until 3) {
            val c = Offset(w * (if (side == 0) 0.82f else 0.18f) + w * 0.04f * (k - 1), h * (0.7f + 0.04f * (k % 2)))
            nameRoundel(c, w * 0.025f, null, Roundel.PlainFill, Roundel.PlainRim, f)
            softGlow(c, w * 0.05f, ProofC63.Good, 0.6f * f * seg(bt, 0.4f + 0.1f * side, 0.6f + 0.1f * side))
        }
    }
}

// ═════════════ 4. change it by hand, tongue or heart (nw-34) ═════════════

private object ChangeC63 {
    val Sky = Color(0xFF181C30)
    val SkyLow = Color(0xFF3A3654)
    val Ground = Color(0xFF242030)
    val Gold = Color(0xFFF2C860)
    val Dark = Color(0xFF0E0C14)
    val Hand = Color(0xFFDCD4C4)
    val Voice = Color(0xFF8AC4EA)
    val Heart = Color(0xFFE8A0A0)
    val Lamp = Color(0xFFB8925A)
    val Gauge = Color(0xFFD8CCB4)
    val Wall = Color(0xFF5A5468)
    val Person = Color(0xFFE2DACA)
}

/**
 * Beats: darkblots (dark blots of different sizes; the biggest is named: «الشِّرْكُ»), handtongueheart (three steps: a hand wipes a
 * blot away, a voice speaks against it, a heart turns from it: «بِالْيَدِ», «بِاللِّسَانِ», «بِالْقَلْبِ»), dimbutlit (three lamps, each
 * dimmer; the last is low but still lit: «وَذَلِكَ أَضْعَفُ الْإِيمَانِ»), faithgauge (a gauge that rises and falls: «يَزِيدُ
 * وَيَنْقُصُ»), barrierexcused (a figure heading to a duty is stopped by a wall; his glow stays with him).
 */
internal fun DrawScope.changeevilScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "darkblots" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "handtongueheart" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "dimbutlit" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "faithgauge" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "barrierexcused" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { changeNight(a) }
}

private fun DrawScope.changeNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val db = a.b("darkblots")
    val th = a.b("handtongueheart")
    val dl = a.b("dimbutlit")
    val fg = a.b("faithgauge")
    val be = a.b("barrierexcused")
    val ground = h * 0.86f
    nightSky(t, ChangeC63.Sky, ChangeC63.SkyLow, ground, starAlpha = 0.8f)
    drawRect(ChangeC63.Ground, Offset(0f, ground), Size(w, h - ground))
    if (db > 0f) {
        val f = easeOut(seg(db, 0f, BEAT_IN)) * (1f - seg(th, 0f, 0.4f))
        for (k in 0 until 6) {
            val c = Offset(w * (0.2f + 0.6f * fract(k * 0.43f)), h * (0.35f + 0.4f * fract(k * 0.71f)))
            drawCircle(ChangeC63.Dark, w * (0.02f + 0.006f * k), c, alpha = f * seg(db, 0.05f + 0.06f * k, 0.15f + 0.06f * k))
        }
        val big = Offset(w * 0.5f, h * 0.45f)
        drawCircle(ChangeC63.Dark, w * 0.1f, big, alpha = f * seg(db, 0.45f, 0.6f))
        tagAt(a.tag("darkblots"), big + Offset(0f, -h * 0.13f), f * seg(db, 0.5f, 0.7f), back = Back63, ink = Color(0xFFE0A0A0))
    }
    if (th > 0f) {
        val f = easeOut(seg(th, 0f, 0.25f)) * (1f - seg(dl, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val g = seg(th, 0.08f + 0.28f * k, 0.25f + 0.28f * k)
            val x = w * (0.8f - 0.3f * k)
            val blot = Offset(x, h * 0.62f)
            val clear = if (k == 0) seg(th, 0.2f, 0.35f) else 0f
            drawCircle(ChangeC63.Dark, w * 0.04f * (1f - clear), blot, alpha = f * g)
            when (k) {
                0 -> hands12(blot + Offset(0f, -h * 0.03f), h * 0.07f, ChangeC63.Hand, f * g)
                1 -> voice12(blot + Offset(w * 0.08f, -h * 0.05f), w * 0.04f, t, ChangeC63.Voice, f * g, -1f)
                else -> heart12(blot + Offset(w * 0.08f, -h * 0.12f), w * 0.035f, ChangeC63.Heart, f * g, fill = true)
            }
            tagAt(a.tag("handtongueheart", 0, k), Offset(x, h * 0.35f), f * g, back = Back63, ink = ArtInk.Gold)
        }
    }
    if (dl > 0f) {
        val f = easeOut(seg(dl, 0f, BEAT_IN)) * (1f - seg(fg, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val c = Offset(w * (0.75f - 0.25f * k), ground - h * 0.22f)
            val g = seg(dl, 0.08f + 0.2f * k, 0.25f + 0.2f * k)
            drawLine(ChangeC63.Lamp, c + Offset(0f, h * 0.05f), Offset(c.x, ground), 2f * dp, alpha = f * g)
            drawOval(ChangeC63.Lamp, Offset(c.x - w * 0.03f, c.y - h * 0.03f), Size(w * 0.06f, h * 0.08f), alpha = f * g * (1f - 0.3f * k))
            softGlow(c, w * (0.1f - 0.035f * k), ChangeC63.Gold, f * g * listOf(1f, 0.6f, 0.22f)[k] * (0.85f + 0.15f * sin(t * 4f + k)))
            // the hand (strongest), the tongue, the heart (weakest, yet still lit)
            tagAt(a.tag("dimbutlit", 0, k + 1), Offset(c.x, ground - h * 0.06f), f * g, back = Back63, ink = if (k == 2) Color(0xFFD8C890) else ArtInk.Gold)
        }
        tagAt(a.tag("dimbutlit"), Offset(w * 0.25f, h * 0.3f), f * seg(dl, 0.6f, 0.8f), back = Back63, ink = ArtInk.Gold)
    }
    if (fg > 0f) {
        val f = easeOut(seg(fg, 0f, BEAT_IN)) * (1f - seg(be, 0f, BEAT_OUT))
        val x = w * 0.5f
        val top = h * 0.2f
        val bot = h * 0.8f
        drawRoundRect(ChangeC63.Gauge, Offset(x - w * 0.05f, top), Size(w * 0.1f, bot - top), CornerRadius(w * 0.05f), alpha = f, style = Stroke(3f * dp))
        val level = 0.5f + 0.35f * sin(fg * 9f)
        drawRoundRect(ChangeC63.Gold, Offset(x - w * 0.035f, bot - (bot - top) * level), Size(w * 0.07f, (bot - top) * level - h * 0.01f), CornerRadius(w * 0.035f), alpha = f * 0.8f)
        tagAt(a.tag("faithgauge"), Offset(w * 0.5f, h * 0.9f), f * seg(fg, 0.2f, 0.4f), back = Back63, ink = ArtInk.Gold)
    }
    if (be > 0f) {
        val f = easeOut(seg(be, 0f, 0.25f))
        val stop = seg(be, 0.4f, 0.45f)
        val x = mix(w * 0.85f, w * 0.55f, easeOut(seg(be, 0.05f, 0.4f)))
        drawRect(ChangeC63.Wall, Offset(w * 0.42f, ground - h * 0.4f), Size(w * 0.06f, h * 0.4f), alpha = f * seg(be, 0.2f, 0.35f))
        softGlow(Offset(w * 0.18f, ground - h * 0.25f), w * 0.06f, ChangeC63.Gold, 0.6f * f)
        figure(Offset(x, ground), h * 0.3f, ChangeC63.Person, f, walk = t * 5f * (1f - stop))
        softGlow(Offset(x, ground - h * 0.18f), w * 0.07f, ChangeC63.Gold, 0.5f * f * seg(be, 0.5f, 0.75f))
        // he is kept from the good he was walking to by a wall he did not build
        tagAt(a.tag("barrierexcused", 0, 0), Offset(w * 0.45f, ground - h * 0.48f), f * seg(be, 0.3f, 0.45f), back = Back63, ink = Color(0xFFF0ECE4))
        tagAt(a.tag("barrierexcused", 0, 1), Offset(w * 0.18f, ground - h * 0.4f), f * seg(be, 0.1f, 0.25f), back = Back63, ink = ArtInk.Gold)
        tagAt(a.tag("barrierexcused", 0, 2), Offset(x + w * 0.1f, ground - h * 0.42f), f * seg(be, 0.55f, 0.75f), back = Back63, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. no envy; be brothers (nw-35) ═════════════

private object EnvyC63 {
    val Dusk = Color(0xFF241E38)
    val DuskLow = Color(0xFF5A4466)
    val Ground = Color(0xFF2A2432)
    val Gold = Color(0xFFF2C860)
    val Tile = Color(0xFF3E3A58)
    val Lamp = Color(0xFFB8925A)
    val Person = Color(0xFFE2DACA)
    val Smoke = Color(0xFF14101A)
    val Bad = Color(0xFFD06A5A)
}

/**
 * Beats: twelvephrases (twelve tiles light one by one; the first is named: «النَّهْيُ عَنِ التَّحَاسُدِ»), envysmoke (a figure's
 * lamp glows; another looks at it wishing it out, and dark smoke rises from his chest: «مَحَبَّةُ زَوَالِ النِّعْمَةِ عَنْ
 * صَاحِبِهَا»), lightmytoo (beside the first lamp another lights a lamp of his own, and the first stays lit: «الْغِبْطَةُ»),
 * firstenvy (a light with his name, «آدَمَ», and dark smoke that turns away from it: «إِبْلِيسَ»), backturned (two roundels meet; one
 * turns his back), fournots (four promises written between two brothers, joined at last by a line of light: «لَا يَظْلِمُهُ», «وَلَا يَخْذُلُهُ», «وَلَا يَكْذِبُهُ»,
 * «وَلَا يَحْقِرُهُ»).
 */
internal fun DrawScope.nojealousyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twelvephrases" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "envysmoke" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "lightmytoo" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "firstenvy" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "backturned" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "fournots" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { envyDusk(a) }
}

private fun DrawScope.envyDusk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tp = a.b("twelvephrases")
    val es = a.b("envysmoke")
    val lm = a.b("lightmytoo")
    val fe = a.b("firstenvy")
    val bt = a.b("backturned")
    val fn = a.b("fournots")
    val ground = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(EnvyC63.Dusk, EnvyC63.DuskLow), 0f, ground), Offset.Zero, Size(w, ground))
    drawRect(EnvyC63.Ground, Offset(0f, ground), Size(w, h - ground))
    if (tp > 0f) {
        val f = easeOut(seg(tp, 0f, BEAT_IN)) * (1f - seg(es, 0f, BEAT_OUT))
        for (k in 0 until 12) {
            val c = Offset(w * (0.8f - 0.2f * (k % 4)), h * (0.35f + 0.15f * (k / 4)))
            val g = seg(tp, 0.05f + 0.05f * k, 0.12f + 0.05f * k)
            drawRoundRect(if (k == 0) EnvyC63.Gold else EnvyC63.Tile, Offset(c.x - w * 0.08f, c.y - h * 0.05f), Size(w * 0.16f, h * 0.1f), CornerRadius(6f * dp), alpha = f * g * (if (k == 0) 0.5f else 0.9f))
        }
        tagAt(a.tag("twelvephrases"), Offset(w * 0.5f, h * 0.18f), f * seg(tp, 0.1f, 0.3f), back = Back63, ink = ArtInk.Gold)
    }
    val lampAt = { c: Offset, lit: Float, alpha: Float ->
        drawLine(EnvyC63.Lamp, c + Offset(0f, h * 0.04f), Offset(c.x, ground), 2f * dp, alpha = alpha)
        drawOval(EnvyC63.Lamp, Offset(c.x - w * 0.025f, c.y - h * 0.025f), Size(w * 0.05f, h * 0.06f), alpha = alpha)
        softGlow(c, w * 0.07f, EnvyC63.Gold, alpha * lit)
    }
    if (es > 0f || lm > 0f) {
        val f = easeOut(seg(es, 0f, BEAT_IN)) * (1f - seg(lm, 0f, BEAT_OUT)) + easeOut(seg(lm, 0f, BEAT_IN)) * (1f - seg(fe, 0f, BEAT_OUT))
        val ff = f.coerceIn(0f, 1f)
        lampAt(Offset(w * 0.7f, ground - h * 0.18f), 1f, ff)
        figure(Offset(w * 0.8f, ground), h * 0.3f, EnvyC63.Person, ff)
        figure(Offset(w * 0.25f, ground), h * 0.3f, EnvyC63.Person, ff, lean = 0.06f)
        if (lm <= 0f) {
            smoke12(Offset(w * 0.25f, ground - h * 0.2f), Offset(w * 0.3f, h * 0.25f), seg(es, 0.3f, 0.7f), t, ff, EnvyC63.Smoke)
            crossMark(Offset(w * 0.3f, h * 0.3f), w * 0.05f, EnvyC63.Bad, 4f * dp, ff * seg(es, 0.75f, 0.9f))
            tagAt(a.tag("envysmoke"), Offset(w * 0.5f, h * 0.15f), ff * seg(es, 0.15f, 0.35f), back = Back63, ink = EnvyC63.Bad)
        } else {
            lampAt(Offset(w * 0.35f, ground - h * 0.18f), seg(lm, 0.3f, 0.6f), ff * seg(lm, 0.1f, 0.3f))
            tagAt(a.tag("lightmytoo"), Offset(w * 0.5f, h * 0.18f), ff * seg(lm, 0.4f, 0.6f), back = Back63, ink = ArtInk.Gold)
        }
    }
    if (fe > 0f) {
        val f = easeOut(seg(fe, 0f, BEAT_IN)) * (1f - seg(bt, 0f, 0.4f))
        val c = Offset(w * 0.68f, h * 0.38f)
        nabiLight14(c, w * 0.013f, f * seg(fe, 0.05f, 0.25f), t, a.tag("firstenvy", 0, 1), c + Offset(0f, -h * 0.12f))
        val turn = easeInOut(seg(fe, 0.45f, 0.85f))
        val sm = Offset(mix(w * 0.4f, w * 0.12f, turn), ground - h * 0.05f)
        smoke12(sm, sm + Offset(0f, -h * 0.3f), seg(fe, 0.25f, 0.45f), t, f, EnvyC63.Smoke)
        tagAt(a.tag("firstenvy", 0, 0), sm + Offset(0f, -h * 0.4f), f * seg(fe, 0.3f, 0.5f), back = Back63, ink = EnvyC63.Bad)
    }
    if (bt > 0f) {
        val f = easeOut(seg(bt, 0f, 0.25f)) * (1f - seg(fn, 0f, BEAT_OUT))
        val come = easeInOut(seg(bt, 0.05f, 0.4f))
        val l = Offset(mix(w * 0.95f, w * 0.62f, come), h * 0.55f)
        val r = Offset(mix(w * 0.05f, w * 0.38f, come), h * 0.55f)
        nameRoundel(l, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val turn = seg(bt, 0.5f, 0.7f)
        nameRoundel(r + Offset(-w * 0.1f * turn, 0f), w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f * (1f - 0.4f * turn))
        drawLine(EnvyC63.Bad, Offset(w * 0.5f, h * 0.45f), Offset(w * 0.5f, h * 0.65f), 3f * dp, StrokeCap.Round, alpha = f * turn)
    }
    if (fn > 0f) {
        val f = easeOut(seg(fn, 0f, 0.25f))
        nameRoundel(Offset(w * 0.85f, h * 0.55f), w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(Offset(w * 0.15f, h * 0.55f), w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        drawLine(EnvyC63.Gold, Offset(w * 0.22f, h * 0.55f), Offset(w * 0.78f, h * 0.55f), 2f * dp, alpha = f * seg(fn, 0.7f, 0.9f))
        for (k in 0 until 4) {
            val g = seg(fn, 0.08f + 0.16f * k, 0.22f + 0.16f * k)
            tagAt(a.tag("fournots", 0, k), Offset(w * 0.5f, h * (0.2f + 0.12f * k + (if (k >= 2) 0.16f else 0f))), f * g, back = Back63, ink = ArtInk.Gold)
        }
    }
}
