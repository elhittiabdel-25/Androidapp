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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 22 (2026-10-27): «بصائر»، «نشأة الكون» (and two cards of «دلائل الخالق» that share its scenes). Deep space,
 * where nature stands as a stone idol and then the universe begins from nothing, bubbles of other universes each
 * with its own start, the box of the quantum vacuum that is never empty, and the sealed box of energy and the cup
 * that cools; a billiard table under a floating law of gravity; and the dominoes of cause and effect, the endless
 * row that never starts to fall, and the soldiers who wait forever for an order.
 */

private object CosC22 {
    val Deep = Color(0xFF05060F)
    val Navy = Color(0xFF0E1530)
    val Violet = Color(0xFF2A1E4A)
    val Stone = Color(0xFF6E6A66)
    val StoneDark = Color(0xFF4A4644)
    val Ground = Color(0xFF1A1820)
    val Ink = Color(0xFF0A0A12)
    val Field = Color(0xFF7AC8FF)
    val Hot = Color(0xFFFF9A5A)
    val Red = Color(0xFFE0604A)
    val Green = Color(0xFF7ED6A0)
    val Glass = Color(0xFFBFD8F0)
    val Back = Color(0xE60A0E20)
}

/** A spiral galaxy at [c], [r] wide, turned by [rot] (radians). */
internal fun DrawScope.galaxy22(c: Offset, r: Float, rot: Float, alpha: Float, tint: Color = Color(0xFFCFD8FF)) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 0.6f, tint, 0.5f * alpha)
    for (arm in 0 until 2) {
        for (k in 0 until 26) {
            val f = k / 26f
            val ang = rot + arm * PI.toFloat() + f * 4.2f
            val rr = r * (0.08f + 0.9f * f)
            drawCircle(tint, r * (0.035f - 0.02f * f), c + Offset(cos(ang) * rr, sin(ang) * rr * 0.55f), alpha = alpha * (0.85f - 0.6f * f))
        }
    }
    drawCircle(Color.White, r * 0.07f, c, alpha = alpha)
}

/** A clock face (time), [r] its radius; [spin] turns the hands. */
internal fun DrawScope.clock22(c: Offset, r: Float, color: Color, spin: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(color, r, c, alpha = alpha, style = Stroke(r * 0.1f))
    for (k in 0 until 12) {
        val ang = k * PI.toFloat() / 6f
        drawLine(color, c + Offset(cos(ang), sin(ang)) * (r * 0.78f), c + Offset(cos(ang), sin(ang)) * (r * 0.9f), r * 0.06f, alpha = alpha)
    }
    val a1 = spin * 2f * PI.toFloat() - PI.toFloat() / 2
    val a2 = spin * 0.17f * 2f * PI.toFloat() - PI.toFloat() / 2
    drawLine(color, c, c + Offset(cos(a1), sin(a1)) * (r * 0.7f), r * 0.07f, StrokeCap.Round, alpha = alpha)
    drawLine(color, c, c + Offset(cos(a2), sin(a2)) * (r * 0.45f), r * 0.1f, StrokeCap.Round, alpha = alpha)
}

/** A floor grid in perspective (space), spread by [open] 0..1 from its middle. */
internal fun DrawScope.grid22(c: Offset, wide: Float, deep: Float, color: Color, open: Float, alpha: Float) {
    if (alpha <= 0.01f || open <= 0f) return
    val hw = wide / 2 * open
    for (k in -5..5) {
        val x = c.x + k / 5f * hw
        drawLine(color, Offset(c.x + k / 5f * hw * 0.35f, c.y - deep / 2), Offset(x, c.y + deep / 2), 1f * density, alpha = alpha * 0.7f)
    }
    for (m in 0..5) {
        val f = m / 5f
        val y = c.y - deep / 2 + deep * f * f
        val half = hw * (0.35f + 0.65f * f * f)
        drawLine(color, Offset(c.x - half, y), Offset(c.x + half, y), 1f * density, alpha = alpha * 0.7f)
    }
}

/** A die seen at an angle, [s] its side; [spots] 1..6. */
internal fun DrawScope.die22(c: Offset, s: Float, spots: Int, rot: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    withTransform({ rotate(rot, c) }) {
        drawRoundRect(Color(0xFFF4F0E6), Offset(c.x - s / 2, c.y - s / 2), Size(s, s), CornerRadius(s * 0.18f), alpha = alpha)
        drawRoundRect(Color(0xFF8A8478), Offset(c.x - s / 2, c.y - s / 2), Size(s, s), CornerRadius(s * 0.18f), alpha = alpha, style = Stroke(s * 0.04f))
        val d = s * 0.27f
        val pos = when (spots) {
            1 -> listOf(Offset.Zero)
            2 -> listOf(Offset(-d, -d), Offset(d, d))
            3 -> listOf(Offset(-d, -d), Offset.Zero, Offset(d, d))
            4 -> listOf(Offset(-d, -d), Offset(d, -d), Offset(-d, d), Offset(d, d))
            5 -> listOf(Offset(-d, -d), Offset(d, -d), Offset.Zero, Offset(-d, d), Offset(d, d))
            else -> listOf(Offset(-d, -d), Offset(d, -d), Offset(-d, 0f), Offset(d, 0f), Offset(-d, d), Offset(d, d))
        }
        for (p in pos) drawCircle(Color(0xFF2A2420), s * 0.08f, c + p, alpha = alpha)
    }
}

private fun DrawScope.spaceStars22(t: Float, alpha: Float) {
    val w = size.width
    val h = size.height
    for (k in 0 until 70) {
        val x = fract(sin(k * 12.9898f) * 43758.5f)
        val y = fract(sin(k * 78.233f) * 12345.6f)
        drawCircle(Color.White, (0.6f + (k % 3) * 0.5f) * density, Offset(w * x, h * y), alpha = alpha * (0.35f + 0.25f * sin(t * 1.5f + k)))
    }
}

// ═════════════ 1. deep space (bs-bidaya, bs-akwan, bs-fragh, bs-taqa) ═════════════

/**
 * Deep space. Beginning (bidaya): monolith (on a dark plain a great stone rises, «الطَّبِيعَةُ», small figures looking
 * up at it), nothingness (it cracks and fades and everything goes black: no time, no place), bang (a point flashes;
 * a ring of light spreads, galaxies spin out, a clock and a floor grid open with it), fromnothing / itself /
 * hascreator (three plaques under the new universe: the first two are crossed out, the third is ticked and glows).
 * Multiverse (akwan): bubbles (dashed bubbles of other universes drift, each with a question in it), bubblestart
 * (from each a line runs back to its own flash of beginning: «لَهَا بِدَايَةٌ»), dice (a die rolls on a floor under a
 * clock, «الزَّمَانُ» and «الْمَكَانُ»), nodice (clock and floor are taken away: the die hangs, unrolled, and fades).
 * Vacuum (fragh): twoboxes (two glass boxes, «الْعَدَمَ» pitch black and «اللَّاشَيْءَ» seemingly empty), notempty
 * (the second fills with a shimmering field), energyfloor (a needle drains it: the level falls to a floor it never
 * goes below), pairs (pairs of particles flicker out of the field and vanish at once). Energy (taqa): sealedbox (a
 * sealed box whose energy meter holds steady, beside the first flash of the universe and a question), insidebox
 * (arrows run round inside the box, the meter unmoved), zerosum (plus and minus tokens meet on a balance «= 0»),
 * cooling (a hot cup gives its heat to the room and cools), deadborn (a cold, dark, starless universe is crossed out
 * beside ours, which began bright and ordered).
 */
internal fun DrawScope.cosmosScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "monolith" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "nothingness" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "bang" to Shot(Offset(w * 0.5f, h * 0.42f), 1.05f),
        "fromnothing" to Shot(Offset(w * 0.5f, h * 0.72f), 1.3f),
        "itself" to Shot(Offset(w * 0.5f, h * 0.72f), 1.3f),
        "hascreator" to Shot(Offset(w * 0.5f, h * 0.65f), 1.15f),
        "bubbles" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "bubblestart" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "dice" to Shot(Offset(w * 0.5f, h * 0.62f), 1.45f),
        "nodice" to Shot(Offset(w * 0.5f, h * 0.6f), 1.3f),
        "twoboxes" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "notempty" to Shot(Offset(w * 0.68f, h * 0.55f), 1.5f),
        "energyfloor" to Shot(Offset(w * 0.68f, h * 0.6f), 1.55f),
        "pairs" to Shot(Offset(w * 0.68f, h * 0.55f), 1.7f),
        "sealedbox" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "insidebox" to Shot(Offset(w * 0.35f, h * 0.55f), 1.5f),
        "zerosum" to Shot(Offset(w * 0.68f, h * 0.55f), 1.5f),
        "cooling" to Shot(Offset(w * 0.32f, h * 0.6f), 1.5f),
        "deadborn" to Shot(Offset(w * 0.6f, h * 0.45f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) {
        drawRect(Brush.verticalGradient(listOf(CosC22.Deep, CosC22.Navy, CosC22.Violet)))
        when {
            a.count("monolith") > 0 -> cosmosBeginning(a)
            a.count("bubbles") > 0 -> cosmosBubbles(a)
            a.count("twoboxes") > 0 -> cosmosVacuum(a)
            else -> cosmosEnergy(a)
        }
    }
}

private fun DrawScope.cosmosBeginning(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val mo = a.b("monolith")
    val no = a.b("nothingness")
    val bg = a.b("bang")
    val n1 = a.b("fromnothing")
    val n2 = a.b("itself")
    val n3 = a.b("hascreator")
    val dark = easeInOut(seg(no, 0f, 0.6f)) * (1f - easeOut(seg(bg, 0f, 0.3f)))
    // before: a night plain and the stone of «nature»
    val before = 1f - easeInOut(seg(no, 0f, 0.5f))
    if (before > 0f) {
        spaceStars22(t, 0.8f * before)
        drawRect(CosC22.Ground, Offset(0f, h * 0.78f), Size(w, h * 0.22f), alpha = before)
        val rise = easeOut(seg(mo, 0f, 0.5f))
        val top = h * 0.78f - h * 0.55f * rise
        val crack = seg(no, 0f, 0.4f)
        val stone = Path().apply {
            moveTo(w * 0.42f, h * 0.78f); lineTo(w * 0.44f, top + h * 0.05f); lineTo(w * 0.5f, top); lineTo(w * 0.56f, top + h * 0.04f); lineTo(w * 0.58f, h * 0.78f); close()
        }
        if (rise > 0.02f) drawPath(stone, Brush.horizontalGradient(listOf(CosC22.StoneDark, CosC22.Stone, CosC22.StoneDark), w * 0.42f, w * 0.58f), alpha = before * seg(rise, 0.02f, 0.15f))
        if (crack > 0f) {
            drawLine(Color.Black, Offset(w * 0.5f, top + h * 0.02f), Offset(w * 0.49f, top + h * 0.25f * crack), 2f * dp, alpha = before)
            drawLine(Color.Black, Offset(w * 0.49f, top + h * 0.15f), Offset(w * 0.53f, top + h * 0.35f * crack), 1.5f * dp, alpha = before)
        }
        for (k in 0 until 4) {
            val p = Offset(w * (0.2f + 0.08f * k + if (k > 1) 0.3f else 0f), h * 0.86f)
            figure(p, h * 0.1f, CosC22.Ink.copy(alpha = 1f), before * easeOut(seg(mo, 0.3f + k * 0.1f, 0.6f + k * 0.1f)), lean = if (k > 1) 0.15f else -0.15f)
        }
        tagAt(a.tag("monolith"), Offset(w * 0.5f, top - h * 0.06f), seg(mo, 0.4f, 0.6f) * (1f - seg(no, 0f, 0.3f)), back = CosC22.Back, ink = ArtInk.Ivory)
    }
    if (dark > 0f) drawRect(Color.Black, alpha = dark)
    // in the dark, a single point waits: the beginning
    val seed = easeOut(seg(no, 0.55f, 0.95f)) * (1f - seg(bg, 0f, 0.1f))
    if (seed > 0f) {
        val c = Offset(w * 0.5f, h * 0.42f)
        softGlow(c, w * (0.03f + 0.01f * sin(t * 3f)), Color(0xFFFFE8C0), 0.7f * seed)
        drawCircle(Color.White, w * 0.006f, c, alpha = seed)
    }
    // the beginning: a point, a ring, galaxies, time and space with it
    if (bg > 0f) {
        val c = Offset(w * 0.5f, h * 0.42f)
        val flash = bump(seg(bg, 0f, 0.25f))
        bloom(c, w * 0.04f + w * 0.06f * flash, Color.White, 0.4f + 1.2f * flash)
        val ring = easeOut(seg(bg, 0.05f, 0.6f))
        drawCircle(Color(0xFFFFE8C0), w * 0.6f * ring, c, alpha = 0.5f * (1f - ring), style = Stroke(4f * dp))
        spaceStars22(t, easeOut(seg(bg, 0.3f, 0.8f)))
        for (k in 0 until 6) {
            val ang = k * 1.05f
            val r = w * (0.12f + 0.06f * (k % 3)) * easeOut(seg(bg, 0.15f + k * 0.04f, 0.7f + k * 0.04f))
            galaxy22(c + Offset(cos(ang) * r * 1.6f, sin(ang) * r * 0.8f), w * 0.04f, t * 0.2f + k, easeOut(seg(bg, 0.2f + k * 0.04f, 0.6f + k * 0.04f)))
        }
        clock22(Offset(w * 0.2f, h * 0.27f), w * 0.045f, ArtInk.BrassLight, t * 0.2f, easeOut(seg(bg, 0.45f, 0.75f)))
        grid22(Offset(w * 0.5f, h * 0.86f), w * 0.9f, h * 0.18f, CosC22.Field, easeOut(seg(bg, 0.45f, 0.9f)), 0.6f)
    }
    // the three ways the universe could have come about
    if (n1 > 0f || n2 > 0f || n3 > 0f) {
        val ys = h * 0.72f
        for (k in 0 until 3) {
            val p = when (k) { 0 -> n1; 1 -> n2; else -> n3 }
            val c = Offset(w * (0.78f - 0.28f * k), ys)
            val show = easeOut(seg(p, 0f, 0.3f)).coerceAtLeast(if (k == 0 && (n2 > 0f || n3 > 0f)) 1f else if (k == 1 && n3 > 0f) 1f else 0f)
            if (show <= 0f) continue
            drawRoundRect(Color(0xFF1A2040), c + Offset(-w * 0.12f, -h * 0.05f), Size(w * 0.24f, h * 0.1f), CornerRadius(h * 0.02f), alpha = 0.9f * show)
            val tl = a.tag(when (k) { 0 -> "fromnothing"; 1 -> "itself"; else -> "hascreator" })
            if (k < 2) struckTag21(tl, c, show, seg(p, 0.4f, 0.9f).coerceAtLeast(if ((k == 0 && n2 > 0f) || n3 > 0f) 1f else 0f), CosC22.Red, back = Color.Transparent, ink = ArtInk.Ivory)
            else {
                tagAt(tl, c, show, back = Color.Transparent, ink = ArtInk.Ivory)
                softGlow(c, w * 0.12f, ArtInk.Gold, 0.5f * easeOut(seg(p, 0.3f, 0.8f)))
                check21(c + Offset(w * 0.13f, 0f), h * 0.035f, CosC22.Green, seg(p, 0.3f, 0.7f), 3f * dp)
            }
        }
    }
}

private fun DrawScope.cosmosBubbles(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bu = a.b("bubbles")
    val bs = a.b("bubblestart")
    val di = a.b("dice")
    val nd = a.b("nodice")
    spaceStars22(t, 1f)
    val fadeForDice = easeOut(seg(di, 0f, 0.3f))
    // bubbles of other universes, drawn dashed (a guess), each with a question
    val dash = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp))
    val centres = List(6) { k -> Offset(w * (0.18f + 0.13f * k) + w * 0.02f * sin(t * 0.3f + k), h * (0.32f + 0.18f * ((k * 7) % 3) / 2f)) }
    for ((k, c) in centres.withIndex()) {
        val f = easeOut(seg(bu, k * 0.08f, 0.4f + k * 0.08f)) * (1f - 0.75f * fadeForDice)
        if (f <= 0f) continue
        val r = w * (0.06f + 0.015f * (k % 3))
        drawCircle(CosC22.Field, r * f, c, alpha = 0.12f * f)
        drawCircle(CosC22.Field, r * f, c, alpha = 0.7f * f, style = Stroke(1.4f * dp, pathEffect = dash))
        galaxy22(c, r * 0.5f, t * 0.3f + k, 0.5f * f, tint = listOf(Color(0xFFCFD8FF), Color(0xFFFFD8C0), Color(0xFFD8FFD0))[k % 3])
        if (bs <= 0f) qmark21(c + Offset(r * 0.55f, -r * 0.45f), h * 0.05f, ArtInk.Ivory, 0.7f * f)
        // each one's own beginning
        if (bs > 0f) {
            val start = c + Offset(-r * 1.25f, r * 0.9f)
            val line = easeOut(seg(bs, 0.1f + k * 0.05f, 0.5f + k * 0.05f))
            drawLine(ArtInk.BrassLight, c + (start - c) * 0.6f, c + (start - c) * (0.6f + 0.4f * line), 1.6f * dp, StrokeCap.Round, alpha = f)
            if (line > 0.95f) bloom(start, w * 0.008f, Color.White, (0.6f + 0.4f * sin(t * 4f + k)) * f)
        }
    }
    tagAt(a.tag("bubblestart"), Offset(w * 0.5f, h * 0.12f), seg(bs, 0.4f, 0.65f) * (1f - fadeForDice), back = CosC22.Back, ink = ArtInk.BrassLight)
    // chance needs time and place: a die on a floor under a clock, then nothing to roll on
    if (di > 0f) {
        val floorC = Offset(w * 0.5f, h * 0.78f)
        val gone = easeInOut(seg(nd, 0.1f, 0.6f))
        grid22(floorC, w * 0.6f, h * 0.14f, CosC22.Field, easeOut(seg(di, 0f, 0.3f)), 1f - gone)
        val clockC = Offset(w * 0.5f, h * 0.3f)
        clock22(clockC, w * 0.05f, ArtInk.BrassLight, if (gone < 1f) t * 0.3f else 0f, easeOut(seg(di, 0f, 0.3f)) * (1f - gone))
        tagAt(a.tag("dice", 0, 0), clockC + Offset(0f, -h * 0.12f), seg(di, 0.2f, 0.4f) * (1f - gone), back = CosC22.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("dice", 0, 1), floorC + Offset(w * 0.25f, h * 0.02f), seg(di, 0.3f, 0.5f) * (1f - gone), back = CosC22.Back, ink = ArtInk.Ivory)
        if (nd > 0f) {
            cross21(clockC, w * 0.05f, CosC22.Red, seg(nd, 0f, 0.3f), 3f * dp)
            cross21(floorC + Offset(w * 0.25f, 0f), h * 0.04f, CosC22.Red, seg(nd, 0.1f, 0.4f), 3f * dp)
        }
        // the die: rolls across the floor; without them it hangs and fades
        val roll = easeOut(seg(di, 0.3f, 0.9f))
        val hang = if (nd > 0f) easeOut(seg(nd, 0.2f, 1f)) else 0f
        val p = Offset(mix(w * 0.3f, w * 0.48f, roll), floorC.y - h * 0.06f - h * 0.08f * bump(seg(roll, 0f, 0.5f)) - h * 0.12f * hang)
        die22(p, w * 0.05f, 1 + ((roll * 11).toInt() % 6), if (hang > 0f) 20f else roll * 540f, 1f - 0.8f * hang)
    }
}

private fun DrawScope.glassBox22(c: Offset, bw: Float, bh: Float, alpha: Float) {
    drawRoundRect(CosC22.Glass, Offset(c.x - bw / 2, c.y - bh / 2), Size(bw, bh), CornerRadius(bw * 0.04f), alpha = 0.08f * alpha)
    drawRoundRect(CosC22.Glass, Offset(c.x - bw / 2, c.y - bh / 2), Size(bw, bh), CornerRadius(bw * 0.04f), alpha = 0.7f * alpha, style = Stroke(1.6f * density))
    drawLine(Color.White, Offset(c.x - bw * 0.4f, c.y - bh * 0.42f), Offset(c.x - bw * 0.15f, c.y - bh * 0.42f), 1.4f * density, StrokeCap.Round, alpha = 0.5f * alpha)
}

private fun DrawScope.cosmosVacuum(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tb = a.b("twoboxes")
    val ne = a.b("notempty")
    val ef = a.b("energyfloor")
    val pa = a.b("pairs")
    spaceStars22(t, 0.6f)
    val bw = w * 0.3f
    val bh = h * 0.5f
    val left = Offset(w * 0.27f, h * 0.55f)
    val right = Offset(w * 0.68f, h * 0.55f)
    val o = easeOut(seg(tb, 0f, 0.4f))
    // nothingness: black, nothing in it ever
    drawRoundRect(Color.Black, Offset(left.x - bw / 2, left.y - bh / 2), Size(bw, bh), CornerRadius(bw * 0.04f), alpha = o)
    glassBox22(left, bw, bh, o)
    tagAt(a.tag("twoboxes", 0, 0), left + Offset(0f, -bh / 2 - h * 0.05f), seg(tb, 0.2f, 0.5f), back = CosC22.Back, ink = ArtInk.Ivory)
    // the "nothing" of physics: a field with energy in it
    glassBox22(right, bw, bh, o)
    tagAt(a.tag("twoboxes", 0, 1), right + Offset(0f, -bh / 2 - h * 0.05f), seg(tb, 0.4f, 0.7f), back = CosC22.Back, ink = ArtInk.Ivory)
    val fill = easeOut(seg(ne, 0.1f, 0.6f))
    val level = 1f - 0.72f * easeInOut(seg(ef, 0.15f, 0.7f))
    if (fill > 0f) {
        val top = right.y + bh / 2 - bh * 0.92f * level
        drawRect(Brush.verticalGradient(listOf(CosC22.Field.copy(alpha = 0.05f), CosC22.Field.copy(alpha = 0.35f)), startY = top, endY = right.y + bh / 2), Offset(right.x - bw / 2 + 2f, top), Size(bw - 4f, right.y + bh / 2 - top), alpha = fill)
        for (k in 0 until 7) {
            val y = right.y + bh / 2 - (k + 0.5f) * bh * 0.92f * level / 7f
            val p = Path().apply {
                moveTo(right.x - bw * 0.46f, y)
                for (m in 1..16) lineTo(right.x - bw * 0.46f + m * bw * 0.92f / 16f, y + sin(m * 0.9f + t * 3f + k) * h * 0.008f)
            }
            drawPath(p, CosC22.Field, alpha = 0.6f * fill, style = Stroke(1.1f * dp))
        }
    }
    // a needle drains it to a floor it never goes below
    if (ef > 0f) {
        val floorY = right.y + bh / 2 - bh * 0.92f * 0.28f
        val pin = easeOut(seg(ef, 0f, 0.2f))
        drawLine(Color(0xFFB8BCC8), Offset(right.x + bw * 0.6f, right.y - bh * 0.6f), Offset(right.x + bw * 0.2f, floorY + h * 0.02f), 2f * dp, alpha = pin)
        val stop = easeOut(seg(ef, 0.65f, 0.9f))
        drawLine(ArtInk.Gold, Offset(right.x - bw / 2, floorY), Offset(right.x + bw / 2, floorY), 2.5f * dp, alpha = stop)
        softGlow(Offset(right.x, floorY), bw * 0.4f, ArtInk.Gold, 0.35f * stop)
    }
    // pairs of virtual particles: out of the field and gone at once
    if (pa > 0f) {
        for (k in 0 until 10) {
            val ph = fract(t * 0.9f + k * 0.37f)
            val life = bump(seg(ph, 0f, 0.35f)) * easeOut(seg(pa, 0f, 0.2f))
            if (life <= 0.01f) continue
            val c = right + Offset(bw * (fract(k * 0.618f) - 0.5f) * 0.8f, bh * (0.3f - 0.2f * fract(k * 0.371f)))
            val sep = w * 0.018f * seg(ph, 0f, 0.2f)
            drawCircle(Color(0xFFFFE0A0), w * 0.006f, c + Offset(-sep, 0f), alpha = life)
            drawCircle(Color(0xFFA0D8FF), w * 0.006f, c + Offset(sep, 0f), alpha = life)
            drawLine(Color.White, c + Offset(-sep, 0f), c + Offset(sep, 0f), 0.8f * dp, alpha = 0.4f * life)
        }
    }
}

private fun DrawScope.cosmosEnergy(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sb = a.b("sealedbox")
    val ib = a.b("insidebox")
    val zs = a.b("zerosum")
    val co = a.b("cooling")
    val db = a.b("deadborn")
    spaceStars22(t, 0.7f)
    val boxC = Offset(w * 0.35f, h * 0.52f)
    val bw = w * 0.28f
    val bh = h * 0.42f
    val later = easeOut(seg(co, 0f, 0.3f))
    val o = easeOut(seg(sb, 0f, 0.3f)) * (1f - 0.8f * later)
    if (o > 0f) {
        glassBox22(boxC, bw, bh, o)
        // sealed: bolts at its corners
        for (cx in listOf(-1f, 1f)) for (cy in listOf(-1f, 1f)) drawCircle(ArtInk.Brass, w * 0.006f, boxC + Offset(cx * bw * 0.45f, cy * bh * 0.43f), alpha = o)
        // its energy meter, steady
        val m = boxC + Offset(-bw * 0.36f, bh * 0.3f)
        drawRect(Color(0xFF222838), Offset(m.x, m.y - bh * 0.5f), Size(w * 0.02f, bh * 0.5f), alpha = o)
        drawRect(CosC22.Green, Offset(m.x, m.y - bh * 0.35f), Size(w * 0.02f, bh * 0.35f), alpha = o)
        // energy running round inside
        val run = easeOut(seg(ib, 0f, 0.3f))
        if (run > 0f) {
            for (k in 0 until 6) {
                val ang = t * 1.4f + k * PI.toFloat() / 3f
                val p = boxC + Offset(cos(ang) * bw * 0.25f, sin(ang) * bh * 0.25f)
                val dir = Offset(-sin(ang), cos(ang))
                arrowHead(p, dir, w * 0.018f, CosC22.Hot, 2f * dp, run * o)
            }
        }
    }
    // the first flash of the universe, with the question
    val flashC = Offset(w * 0.75f, h * 0.35f)
    val q = easeOut(seg(sb, 0.3f, 0.6f)) * (1f - easeOut(seg(ib, 0f, 0.3f)))
    if (q > 0f) {
        bloom(flashC, w * 0.03f, Color.White, q)
        qmark21(flashC + Offset(w * 0.07f, -h * 0.05f), h * 0.08f, ArtInk.Ivory, q)
    }
    // plus and minus meet on a balance: zero
    if (zs > 0f) {
        val c = Offset(w * 0.72f, h * 0.55f)
        val f = easeOut(seg(zs, 0f, 0.3f)) * (1f - 0.8f * later)
        drawLine(ArtInk.BrassLight, c + Offset(0f, h * 0.15f), c, 2f * dp, alpha = f)
        drawLine(ArtInk.BrassLight, c + Offset(-w * 0.12f, 0f), c + Offset(w * 0.12f, 0f), 2f * dp, alpha = f)
        for (k in 0 until 3) {
            val drop = easeOut(seg(zs, 0.15f + k * 0.12f, 0.35f + k * 0.12f))
            val pl = c + Offset(-w * 0.1f, -h * 0.04f - k * h * 0.035f - h * 0.2f * (1f - drop))
            val mi = c + Offset(w * 0.1f, -h * 0.04f - k * h * 0.035f - h * 0.2f * (1f - drop))
            drawCircle(CosC22.Hot, w * 0.014f, pl, alpha = f * drop)
            drawLine(Color.White, pl + Offset(-w * 0.008f, 0f), pl + Offset(w * 0.008f, 0f), 1.6f * dp, alpha = f * drop)
            drawLine(Color.White, pl + Offset(0f, -w * 0.008f), pl + Offset(0f, w * 0.008f), 1.6f * dp, alpha = f * drop)
            drawCircle(CosC22.Field, w * 0.014f, mi, alpha = f * drop)
            drawLine(Color.White, mi + Offset(-w * 0.008f, 0f), mi + Offset(w * 0.008f, 0f), 1.6f * dp, alpha = f * drop)
        }
        tagAt(a.tag("zerosum"), c + Offset(0f, h * 0.22f), seg(zs, 0.6f, 0.85f) * (1f - later), back = CosC22.Back, ink = ArtInk.Ivory)
    }
    // the cup that cools: heat goes one way only
    if (co > 0f) {
        val cup = Offset(w * 0.3f, h * 0.68f)
        val cool = easeInOut(seg(co, 0.1f, 1f))
        val col = lerpColor(CosC22.Hot, Color(0xFF8AA0C0), cool)
        drawRoundRect(Color(0xFFEDE6DA), cup + Offset(-w * 0.05f, -h * 0.1f), Size(w * 0.1f, h * 0.12f), CornerRadius(w * 0.01f), alpha = later)
        drawArc(Color(0xFFEDE6DA), -90f, 180f, false, cup + Offset(w * 0.035f, -h * 0.08f), Size(w * 0.04f, h * 0.06f), alpha = later, style = Stroke(w * 0.008f))
        drawRect(col, cup + Offset(-w * 0.045f, -h * 0.095f), Size(w * 0.09f, h * 0.02f), alpha = later)
        for (k in 0 until 3) {
            val f = fract(t * 0.6f + k / 3f)
            val p = cup + Offset(w * (-0.02f + 0.02f * k) + sin(f * 6f + k) * w * 0.006f, -h * 0.12f - f * h * 0.12f)
            drawCircle(Color.White, w * 0.008f, p, alpha = 0.4f * (1f - f) * (1f - cool) * later)
        }
        for (k in 0 until 4) {
            val f = fract(t * 0.5f + k * 0.25f)
            arrowHead(cup + Offset(w * (0.07f + 0.15f * f), -h * 0.05f + k * h * 0.02f - h * 0.03f), Offset(1f, 0f), w * 0.015f, CosC22.Hot, 1.6f * dp, 0.6f * later * (1f - f))
        }
    }
    // a universe born dead, beside ours born bright
    if (db > 0f) {
        val f = easeOut(seg(db, 0f, 0.3f))
        val ours = Offset(w * 0.78f, h * 0.35f)
        val dead = Offset(w * 0.55f, h * 0.35f)
        galaxy22(ours, w * 0.07f, t * 0.3f, f)
        drawCircle(Color(0xFF3A3E48), w * 0.075f, dead, alpha = f)
        drawCircle(Color(0xFF22252C), w * 0.075f, dead, alpha = f, style = Stroke(2f * dp))
        cross21(dead, w * 0.05f, CosC22.Red, seg(db, 0.4f, 0.8f), 3f * dp)
    }
}

// ═════════════ 2. the billiard table under the law (bs-qawanin) ═════════════

private object BilC22 {
    val Felt = Color(0xFF1E6A48)
    val FeltLight = Color(0xFF2E8A60)
    val Rail = Color(0xFF6A4226)
    val Ball = Color(0xFFF4F0E6)
    val Card = Color(0xFF14203E)
    val Red = Color(0xFFE0604A)
    val Back = Color(0xE60A0E20)
}

/**
 * A billiard table in the dark. Beats: formula (a card with the law of gravity floats alone in the dark, glowing),
 * noclock (a clock appears, its hands run back, and it dissolves; «قَبْلَ» is struck out), emptytable (a billiard
 * table fades in under the law: empty), balltrace (a ball drops onto it and rolls; the law draws its path ahead of it
 * in dots, and the ball follows them), noproduce (the law flashes as if to make a ball of its own: a ghost of a ball
 * flickers and is gone, struck out).
 */
internal fun DrawScope.billiardScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "formula" to Shot(Offset(w * 0.5f, h * 0.3f), 1.5f),
        "noclock" to Shot(Offset(w * 0.25f, h * 0.35f), 1.5f),
        "emptytable" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "balltrace" to Shot(Offset(w * 0.5f, h * 0.62f), 1.25f),
        "noproduce" to Shot(Offset(w * 0.55f, h * 0.48f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.15f) { billiardRoom(a) }
}

private fun DrawScope.billiardRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fo = a.b("formula")
    val nc = a.b("noclock")
    val et = a.b("emptytable")
    val bt = a.b("balltrace")
    val np = a.b("noproduce")
    drawRect(Brush.radialGradient(listOf(Color(0xFF1A1E2E), Color(0xFF07080E)), Offset(w * 0.5f, h * 0.4f), w * 0.7f))
    // the table
    val tab = easeOut(seg(et, 0f, 0.4f))
    val tl = Offset(w * 0.18f, h * 0.55f)
    val tw = w * 0.64f
    val th = h * 0.3f
    if (tab > 0f) {
        softGlow(Offset(w * 0.5f, h * 0.45f), w * 0.4f, Color(0xFFFFF0C8), 0.25f * tab)
        drawRoundRect(BilC22.Rail, tl + Offset(-w * 0.025f, -h * 0.03f), Size(tw + w * 0.05f, th + h * 0.06f), CornerRadius(w * 0.02f), alpha = tab)
        drawRoundRect(Brush.radialGradient(listOf(BilC22.FeltLight, BilC22.Felt), tl + Offset(tw / 2, th / 2), tw * 0.6f), tl, Size(tw, th), CornerRadius(w * 0.01f), alpha = tab)
        for (cx in listOf(0f, 0.5f, 1f)) for (cy in listOf(0f, 1f)) drawCircle(Color.Black, w * 0.012f, tl + Offset(tw * cx, th * cy), alpha = tab)
    }
    // the law, alone, glowing
    val card = Offset(w * 0.5f, h * 0.28f)
    val co = easeOut(seg(fo, 0f, 0.4f)).coerceAtLeast(if (nc > 0f || et > 0f) 1f else 0f)
    if (co > 0f) {
        val pulse = bump(seg(np, 0f, 0.5f))
        softGlow(card, w * 0.18f, Color(0xFF9ABCFF), (0.4f + 0.5f * pulse) * co)
        drawRoundRect(BilC22.Card, card + Offset(-w * 0.15f, -h * 0.06f), Size(w * 0.3f, h * 0.12f), CornerRadius(h * 0.02f), alpha = 0.95f * co)
        drawRoundRect(Color(0xFF9ABCFF), card + Offset(-w * 0.15f, -h * 0.06f), Size(w * 0.3f, h * 0.12f), CornerRadius(h * 0.02f), alpha = 0.6f * co, style = Stroke(1.4f * dp))
        tagAt(a.tag("formula"), card, co, back = Color.Transparent, ink = Color(0xFFDDE8FF))
    }
    // no "before": the clock runs back and dissolves
    if (nc > 0f && et < 0.3f) {
        val c = Offset(w * 0.25f, h * 0.38f)
        val gone = easeInOut(seg(nc, 0.5f, 0.95f))
        clock22(c, w * 0.06f, ArtInk.BrassLight, -t * 0.8f, easeOut(seg(nc, 0f, 0.2f)) * (1f - gone))
        for (k in 0 until 8) {
            val f = gone
            drawCircle(ArtInk.BrassLight, w * 0.005f, c + Offset(cos(k * 0.8f), sin(k * 0.8f)) * (w * 0.06f + w * 0.08f * f), alpha = 0.6f * bump(f))
        }
        val tg = c + Offset(0f, h * 0.15f)
        struckTag21(a.tag("noclock"), tg, seg(nc, 0.2f, 0.4f) * (1f - seg(et, 0f, 0.3f)), seg(nc, 0.55f, 0.85f), BilC22.Red, back = BilC22.Back, ink = ArtInk.Ivory)
    }
    // the ball: dropped in, rolling along the law's dotted path
    if (bt > 0f) {
        val p0 = tl + Offset(tw * 0.15f, th * 0.35f)
        val p1 = tl + Offset(tw * 0.5f, th * 0.95f)
        val p2 = tl + Offset(tw * 0.8f, th * 0.1f)
        val p3 = tl + Offset(tw * 0.92f, th * 0.6f)
        val pred = easeOut(seg(bt, 0.15f, 0.5f))
        for (k in 0 until 20) {
            val f = k / 20f
            if (f > pred) break
            drawCircle(Color(0xFF9ABCFF), w * 0.003f, cubic(p0, p1, p2, p3, f), alpha = 0.9f)
        }
        // the law's light reaches down to the path it describes
        drawLine(Color(0xFF9ABCFF), card + Offset(0f, h * 0.06f), cubic(p0, p1, p2, p3, min(1f, pred)), 1f * dp, alpha = 0.25f * pred)
        val drop = easeOut(seg(bt, 0f, 0.2f))
        val roll = easeInOut(seg(bt, 0.35f, 1f))
        val ball = if (roll <= 0f) Offset(p0.x, mix(h * 0.05f, p0.y, drop)) else cubic(p0, p1, p2, p3, roll)
        drawCircle(Color.Black, w * 0.02f, ball + Offset(w * 0.004f, h * 0.008f), alpha = 0.3f)
        drawCircle(BilC22.Ball, w * 0.02f, ball)
        drawCircle(Color.White, w * 0.006f, ball + Offset(-w * 0.006f, -w * 0.006f), alpha = 0.8f)
    }
    // the law cannot make a ball of its own
    if (np > 0f) {
        val c = Offset(w * 0.62f, h * 0.48f)
        val ghost = bump(seg(np, 0.05f, 0.55f)) * (0.6f + 0.4f * sin(t * 25f))
        drawCircle(BilC22.Ball, w * 0.02f, c, alpha = 0.35f * ghost, style = Stroke(1.4f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp))))
        cross21(c, w * 0.025f, BilC22.Red, seg(np, 0.45f, 0.85f), 2.5f * dp)
    }
}

// ═════════════ 3. dominoes, endless rows, the soldiers (bs-sababiyya, bs-tasalsul) ═════════════

private object DomC22 {
    val Floor = Color(0xFF2A2A36)
    val FloorLow = Color(0xFF16161E)
    val Tile = Color(0xFFF2EEE4)
    val Dot = Color(0xFF22201C)
    val Ink = Color(0xFF0E0E16)
    val Soldier = Color(0xFF3A4430)
    val Red = Color(0xFFE0604A)
    val Back = Color(0xE60A0E20)
}

/** A domino standing on [base], tilted by [fall] (0 upright .. 1 flat, falling to the left). */
private fun DrawScope.domino22(base: Offset, hgt: Float, fall: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val wid = hgt * 0.45f
    withTransform({ rotate(-80f * fall, base) }) {
        drawRoundRect(DomC22.Tile, Offset(base.x - wid / 2, base.y - hgt), Size(wid, hgt), CornerRadius(wid * 0.12f), alpha = alpha)
        drawLine(DomC22.Dot, Offset(base.x - wid * 0.35f, base.y - hgt / 2), Offset(base.x + wid * 0.35f, base.y - hgt / 2), wid * 0.05f, alpha = alpha)
        drawCircle(DomC22.Dot, wid * 0.09f, Offset(base.x, base.y - hgt * 0.75f), alpha = alpha)
        drawCircle(DomC22.Dot, wid * 0.09f, Offset(base.x - wid * 0.2f, base.y - hgt * 0.25f), alpha = alpha)
        drawCircle(DomC22.Dot, wid * 0.09f, Offset(base.x + wid * 0.2f, base.y - hgt * 0.25f), alpha = alpha)
    }
}

/**
 * A dark floor. Causality (sababiyya): domino (five dominoes stand in a row; the first is tipped and each knocks down
 * the next, an arrow from cause to effect), blurorder (under the row a ribbon of time whose marks blur and swap —
 * «الْحَتْمِيَّةِ» — while the fallen chain stays ticked), fuzzy (one particle as a blurred cloud of possible places),
 * steady (wide: planets turning steadily round their star, nothing out of place). Regress (tasalsul): civ (a far
 * planet with towers sends a beam to our universe), whomade (behind it a question, and another behind that), regress
 * (an endless row of dominoes fading to the horizon, each waiting for the one behind: none falls), soldiers (a line of
 * soldiers fading to the horizon, each turning back to the one behind him for the order; question marks pass back
 * along the line), nobullet (the first soldier's rifle stays silent, the target untouched).
 */
internal fun DrawScope.dominoesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "domino" to Shot(Offset(w * 0.5f, h * 0.62f), 1.35f),
        "blurorder" to Shot(Offset(w * 0.5f, h * 0.72f), 1.4f),
        "fuzzy" to Shot(Offset(w * 0.5f, h * 0.35f), 1.6f),
        "steady" to Shot(Offset(w * 0.5f, h * 0.4f), 1f),
        "civ" to Shot(Offset(w * 0.5f, h * 0.4f), 1.1f),
        "whomade" to Shot(Offset(w * 0.3f, h * 0.35f), 1.4f),
        "regress" to Shot(Offset(w * 0.4f, h * 0.6f), 1.2f),
        "soldiers" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "nobullet" to Shot(Offset(w * 0.75f, h * 0.6f), 1.5f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) {
        if (a.count("domino") > 0) dominoesCause(a) else dominoesRegress(a)
    }
}

private fun DrawScope.dominoesCause(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val dm = a.b("domino")
    val bo = a.b("blurorder")
    val fz = a.b("fuzzy")
    val st = a.b("steady")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF101428), DomC22.Floor, DomC22.FloorLow)))
    spaceStars22(t, 0.35f)
    val wide = easeInOut(seg(st, 0f, 0.5f))
    // the chain: cause then effect
    val row = 1f - wide
    if (row > 0f) {
        for (k in 0 until 5) {
            val base = Offset(w * (0.7f - 0.1f * k), h * 0.7f)
            val fall = easeInOut(seg(dm, 0.15f + k * 0.12f, 0.3f + k * 0.12f))
            domino22(base, h * 0.16f, fall, easeOut(seg(dm, 0f, 0.15f)) * row)
        }
        // the push
        val push = seg(dm, 0.05f, 0.18f)
        if (push in 0.01f..0.99f) arrowHead(Offset(w * 0.76f, h * 0.62f), Offset(-1f, 0f), w * 0.03f, ArtInk.Gold, 3f * dp)
        val arr = easeOut(seg(dm, 0.7f, 0.95f))
        if (arr > 0f) {
            drawLine(ArtInk.BrassLight, Offset(w * 0.68f, h * 0.45f), Offset(w * 0.68f - w * 0.36f * arr, h * 0.45f), 2f * dp, StrokeCap.Round, alpha = row)
            arrowHead(Offset(w * 0.68f - w * 0.36f * arr, h * 0.45f), Offset(-1f, 0f), w * 0.02f, ArtInk.BrassLight, 2f * dp, row)
        }
        // the ribbon of time underneath, its marks blurring and swapping
        if (bo > 0f) {
            val y = h * 0.82f
            val o = easeOut(seg(bo, 0f, 0.3f)) * row
            drawLine(Color(0xFF8A90A8), Offset(w * 0.2f, y), Offset(w * 0.8f, y), 2f * dp, alpha = o)
            val swap = easeInOut(seg(bo, 0.3f, 0.8f))
            for (k in 0 until 7) {
                val x0 = w * (0.25f + 0.085f * k)
                val x = x0 + sin(t * 3f + k * 2f) * w * 0.03f * swap
                drawLine(Color(0xFFB8BCD8), Offset(x, y - h * 0.02f), Offset(x, y + h * 0.02f), (1.5f + 3f * swap) * dp, StrokeCap.Round, alpha = o * (1f - 0.5f * swap))
            }
            tagAt(a.tag("blurorder"), Offset(w * 0.5f, y + h * 0.08f), seg(bo, 0.3f, 0.5f) * row, back = DomC22.Back, ink = ArtInk.Ivory)
            qmark21(Offset(w * 0.83f, y - h * 0.04f), h * 0.05f, ArtInk.Ivory, seg(bo, 0.4f, 0.6f) * row)
            check21(Offset(w * 0.32f, h * 0.38f), h * 0.04f, Color(0xFF7ED6A0), seg(bo, 0.5f, 0.8f) * row, 3f * dp)
        }
    }
    // one particle: a cloud of where it may be
    if (fz > 0f) {
        val c = Offset(w * 0.5f, h * 0.33f)
        val o = easeOut(seg(fz, 0f, 0.3f)) * (1f - wide)
        softGlow(c, w * 0.09f, Color(0xFF9ABCFF), 0.5f * o)
        for (k in 0 until 40) {
            val ang = k * 2.39996f + t * 0.5f
            val r = w * 0.08f * kotlin.math.sqrt(fract(k * 0.618f + t * 0.3f))
            drawCircle(Color(0xFFCFE0FF), w * 0.004f, c + Offset(cos(ang) * r, sin(ang) * r * 0.7f), alpha = 0.6f * o)
        }
        qmark21(c + Offset(w * 0.12f, -h * 0.05f), h * 0.06f, ArtInk.Ivory, o * (1f - seg(st, 0f, 0.3f)))
    }
    // yet the whole stays steady
    if (st > 0f) {
        val c = Offset(w * 0.5f, h * 0.42f)
        val o = easeOut(seg(st, 0.1f, 0.5f))
        bloom(c, w * 0.03f, Color(0xFFFFD27A), o)
        for (k in 0 until 4) {
            val r = w * (0.09f + 0.07f * k)
            drawOval(Color(0xFF8A90A8), Offset(c.x - r, c.y - r * 0.4f), Size(2 * r, r * 0.8f), alpha = 0.4f * o, style = Stroke(1f * dp))
            val ang = t * (0.9f - 0.18f * k) + k
            drawCircle(listOf(Color(0xFFB8C8FF), Color(0xFFFFC8A0), Color(0xFF9AE0C0), Color(0xFFE0D0FF))[k], w * (0.01f + 0.003f * k), c + Offset(cos(ang) * r, sin(ang) * r * 0.4f), alpha = o)
        }
    }
}

private fun DrawScope.dominoesRegress(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cv = a.b("civ")
    val wm = a.b("whomade")
    val rg = a.b("regress")
    val so = a.b("soldiers")
    val nb = a.b("nobullet")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF101428), Color(0xFF242436), DomC22.FloorLow)))
    spaceStars22(t, 0.6f)
    val ground = easeOut(seg(rg, 0f, 0.3f)).coerceAtLeast(if (so > 0f) 1f else 0f)
    val sky = 1f - ground
    // a far civilisation said to have made our universe
    if (sky > 0f && (cv > 0f || wm > 0f)) {
        val ours = Offset(w * 0.75f, h * 0.45f)
        val civ = Offset(w * 0.35f, h * 0.4f)
        galaxy22(ours, w * 0.08f, t * 0.3f, sky)
        val o = easeOut(seg(cv, 0f, 0.3f)) * sky
        drawCircle(Color(0xFF5A6A8A), w * 0.06f, civ, alpha = o)
        for (k in 0 until 4) drawRect(Color(0xFFDCE4FF), Offset(civ.x - w * 0.04f + k * w * 0.022f, civ.y - w * 0.06f - h * (0.04f + 0.02f * (k % 2))), Size(w * 0.01f, h * (0.04f + 0.02f * (k % 2))), alpha = o)
        val beam = easeOut(seg(cv, 0.3f, 0.8f))
        drawLine(ArtInk.BrassLight, civ + Offset(w * 0.06f, 0f), civ + Offset(w * 0.06f, 0f) + (ours - civ - Offset(w * 0.14f, 0f)) * beam, 2f * dp, StrokeCap.Round, alpha = o)
        // and who made them? and them?
        for (k in 0 until 3) {
            val f = easeOut(seg(wm, k * 0.2f, 0.3f + k * 0.2f)) * sky
            val p = civ - Offset(w * (0.1f + 0.08f * k), h * (0.04f - 0.04f * k))
            drawLine(Color(0xFF8A90A8), p + Offset(w * 0.06f, 0f), p + Offset(w * 0.02f, 0f), 1.5f * dp, alpha = f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp)))
            qmark21(p, h * 0.06f - k * h * 0.012f, ArtInk.Ivory, f * (1f - 0.25f * k))
        }
    }
    if (ground <= 0f) return
    drawRect(Brush.verticalGradient(listOf(DomC22.Floor, DomC22.FloorLow), startY = h * 0.55f, endY = h), Offset(0f, h * 0.55f), Size(w, h * 0.45f), alpha = ground)
    if (so <= 0f) {
        // an endless row of dominoes waiting for a push that never comes
        for (k in 0 until 14) {
            val f = k / 13f
            val base = Offset(mix(w * 0.85f, w * 0.12f, kotlin.math.sqrt(f)), mix(h * 0.86f, h * 0.6f, kotlin.math.sqrt(f)))
            val s = mix(1f, 0.25f, kotlin.math.sqrt(f))
            val shake = sin(t * 3f + k) * 1.5f
            withTransform({ rotate(shake * seg(rg, 0.3f, 1f), base) }) { domino22(base, h * 0.16f * s, 0f, ground * (1f - 0.6f * f)) }
        }
        val fade = Offset(w * 0.1f, h * 0.58f)
        softGlow(fade, w * 0.1f, Color(0xFF8A90A8), 0.3f * ground)
        qmark21(fade + Offset(0f, -h * 0.08f), h * 0.06f, ArtInk.Ivory, seg(rg, 0.4f, 0.7f))
        return
    }
    // the soldiers: each turns back to the one behind for the order
    for (k in 0 until 10) {
        val f = k / 9f
        val foot = Offset(mix(w * 0.72f, w * 0.12f, kotlin.math.sqrt(f)), mix(h * 0.88f, h * 0.62f, kotlin.math.sqrt(f)))
        val s = mix(1f, 0.3f, kotlin.math.sqrt(f))
        val tall = h * 0.24f * s
        val al = easeOut(seg(so, f * 0.3f, 0.2f + f * 0.3f)) * (1f - 0.5f * f)
        val turn = seg(so, 0.25f + f * 0.4f, 0.35f + f * 0.4f) > 0f
        withTransform({ if (!turn) scale(-1f, 1f, pivot = foot) }) { figure(foot, tall, DomC22.Soldier, al) }
        // the rifle, held upright at rest
        drawLine(Color(0xFF2A2420), foot + Offset(-tall * 0.15f, -tall * 0.45f), foot + Offset(-tall * 0.15f, -tall * 1.05f), max(1.5f * dp, tall * 0.04f), StrokeCap.Round, alpha = al)
        val q = bump(seg(so, 0.3f + f * 0.4f, 0.5f + f * 0.4f))
        if (q > 0f) qmark21(foot + Offset(0f, -tall * 1.3f), h * 0.05f * s, ArtInk.Ivory, q * al)
    }
    // the target, untouched
    val target = Offset(w * 0.86f, h * 0.55f)
    val to = easeOut(seg(so, 0f, 0.3f))
    for (k in 0 until 3) drawCircle(if (k % 2 == 0) Color(0xFFE8E0D0) else DomC22.Red, w * (0.045f - 0.014f * k), target, alpha = to)
    if (nb > 0f) {
        val c = Offset(w * 0.8f, h * 0.55f)
        val dash = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp))
        drawLine(ArtInk.BrassLight, Offset(w * 0.73f, h * 0.6f), c + Offset(w * 0.06f, 0f), 1.5f * dp, alpha = easeOut(seg(nb, 0f, 0.3f)) * 0.7f, pathEffect = dash)
        cross21(c, h * 0.04f, DomC22.Red, seg(nb, 0.3f, 0.7f), 3f * dp)
    }
}
