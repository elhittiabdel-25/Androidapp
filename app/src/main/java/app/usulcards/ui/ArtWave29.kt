package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Round 17 of the redraw (2026-10-27): «مَعْرِفَةُ النَّبِيِّ ﷺ». Four new scenes: the road from the land of idols to the
 * land of the mosque, open until the Hour, and those who cannot take it; a scroll unrolled on five proofs; the palace
 * finished with its last brick, the false claimants whose flames go out, and the light that reaches men and jinn;
 * the night journey from Makkah to Bayt al-Maqdis and up through the seven heavens. His ﷺ light always carries his
 * name; the other prophets are smaller lights with their names; Allah is never drawn.
 */

/** A woman as a modest silhouette: a long robe and a covering over head and shoulders. */
private fun DrawScope.woman29(foot: Offset, tall: Float, color: Color, alpha: Float, walk: Float = 0f) {
    if (alpha <= 0.01f) return
    val sway = sin(walk) * tall * 0.02f
    val shoulder = foot.y - tall * 0.74f
    drawPath(Path().apply {
        moveTo(foot.x - tall * 0.1f, shoulder)
        lineTo(foot.x + tall * 0.1f, shoulder)
        lineTo(foot.x + tall * 0.17f + sway, foot.y)
        lineTo(foot.x - tall * 0.17f + sway, foot.y)
        close()
    }, color, alpha = alpha)
    val head = Offset(foot.x, shoulder - tall * 0.11f)
    drawPath(Path().apply {
        moveTo(head.x - tall * 0.13f, shoulder + tall * 0.08f)
        quadraticTo(head.x - tall * 0.13f, head.y - tall * 0.14f, head.x, head.y - tall * 0.13f)
        quadraticTo(head.x + tall * 0.13f, head.y - tall * 0.14f, head.x + tall * 0.13f, shoulder + tall * 0.08f)
        close()
    }, color, alpha = alpha)
}

/** An old man bent over his stick. */
private fun DrawScope.elder29(foot: Offset, tall: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    figure(foot, tall * 0.92f, color, alpha, lean = 0.35f, arms = 0.4f)
    drawLine(color, foot + Offset(-tall * 0.28f, -tall * 0.45f), foot + Offset(-tall * 0.32f, 0f), max(1.4f * density, tall * 0.035f), StrokeCap.Round, alpha = alpha)
}

/** A jinn as the codex draws it: a pale, smokeless blue flame. */
private fun DrawScope.paleFlame29(base: Offset, s: Float, t: Float, k: Int, alpha: Float) {
    if (alpha <= 0.01f) return
    val fl = 0.08f * sin(t * 7f + k * 1.7f) + 0.05f * sin(t * 11f + k)
    val tip = base + Offset(s * fl, -s)
    val p = Path().apply {
        moveTo(base.x - s * 0.22f, base.y)
        quadraticTo(base.x - s * 0.3f, base.y - s * 0.45f, tip.x, tip.y)
        quadraticTo(base.x + s * 0.3f, base.y - s * 0.45f, base.x + s * 0.22f, base.y)
        close()
    }
    softGlow(base + Offset(0f, -s * 0.4f), s * 0.6f, Color(0xFFBFD8FF), 0.5f * alpha)
    drawPath(p, Brush.verticalGradient(listOf(Color(0xFFEAF4FF), Color(0xFF9AC0F0), Color(0xFF5A8AE0)), startY = tip.y, endY = base.y), alpha = 0.85f * alpha)
}

/** The Kaaba, seen from the front, its base at [foot]. */
internal fun DrawScope.kaaba29(foot: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(Color(0xFF14121A), Offset(foot.x - s * 0.5f, foot.y - s * 0.9f), Size(s, s * 0.9f), alpha = alpha)
    drawRect(Color(0xFFD8A83A), Offset(foot.x - s * 0.5f, foot.y - s * 0.72f), Size(s, s * 0.07f), alpha = alpha)
    drawRect(Color(0xFFD8A83A), Offset(foot.x + s * 0.18f, foot.y - s * 0.42f), Size(s * 0.16f, s * 0.3f), alpha = alpha)
}

/** A mosque with a dome, its base at [foot]. */
internal fun DrawScope.domed29(foot: Offset, s: Float, body: Color, dome: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRect(body, Offset(foot.x - s * 0.55f, foot.y - s * 0.5f), Size(s * 1.1f, s * 0.5f), alpha = alpha)
    drawRect(lerpColor(body, Color.Black, 0.15f), Offset(foot.x - s * 0.3f, foot.y - s * 0.62f), Size(s * 0.6f, s * 0.12f), alpha = alpha)
    drawArc(dome, 180f, 180f, true, Offset(foot.x - s * 0.3f, foot.y - s * 0.92f), Size(s * 0.6f, s * 0.6f), alpha = alpha)
    drawLine(Color(0xFFE2C46A), Offset(foot.x, foot.y - s * 0.92f), Offset(foot.x, foot.y - s * 1.02f), s * 0.03f, alpha = alpha)
    drawPath(archPath26(foot.x, foot.y - s * 0.32f, foot.y, s * 0.1f), Color(0xFF2A2430), alpha = alpha)
}

// ═════════════ 1. the road from the land of idols to the land of the mosque (hijra) ═════════════

private object LandC29 {
    val Sky = Color(0xFFBCD8EC)
    val SkyLow = Color(0xFFF2E6CC)
    val Dusk = Color(0xFF9A7A8A)
    val Shirk = Color(0xFFB08A6A)
    val ShirkLow = Color(0xFF8A6A50)
    val Green = Color(0xFF8AB06A)
    val GreenLow = Color(0xFF6A9050)
    val Road = Color(0xFFE6D2A6)
    val Wall = Color(0xFFEDE3CC)
    val Dome = Color(0xFF4A8A6A)
    val Idol = Color(0xFF6A5E56)
    val Ink = Color(0xFF2A2430)
    val Back = Color(0xE6201810)
}

private fun road29(w: Float, h: Float, u: Float): Offset = Offset(w * (0.95f - 0.9f * u), h * (0.8f - 0.03f * sin(u * PI.toFloat())))

/**
 * Two lands side by side under one sky. Beats: crossover (on the right, a town of stone idols; on the left, green land
 * and a mosque: «بَلَدُ الشِّرْكِ», «بَلَدُ الْإِسْلَامِ»; a family with its camel takes the road from one to the other),
 * neverclosed (the sun and the moon cross the sky again and again, and others keep taking the road:
 * «لَا تَنْقَطِعُ الْهِجْرَةُ حَتَّى تَنْقَطِعَ التَّوْبَةُ»), excused (left behind in the first land, an old man on his stick, a
 * mother with her child, a sick man: a soft light on each; ﴿لَا يَسْتَطِيعُونَ حِيلَةً وَلَا يَهْتَدُونَ سَبِيلًا﴾).
 */
internal fun DrawScope.twolandsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "crossover" to Shot(Offset(w * 0.5f, h * 0.62f), 1.1f),
        "neverclosed" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "excused" to Shot(Offset(w * 0.76f, h * 0.6f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { twolandsRoad(a) }
}

private fun DrawScope.twolandsRoad(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val co = a.b("crossover")
    val nc = a.b("neverclosed")
    val ex = a.b("excused")
    val horizon = h * 0.58f
    drawRect(Brush.verticalGradient(listOf(LandC29.Sky, LandC29.SkyLow), endY = horizon), size = Size(w, horizon))
    drawRect(Brush.horizontalGradient(listOf(Color.Transparent, LandC29.Dusk.copy(alpha = 0.45f)), startX = w * 0.45f, endX = w), size = Size(w, horizon))
    // the sun and the moon, crossing again and again
    if (nc > 0f) {
        val turns = seg(nc, 0.05f, 0.95f) * 2.5f
        val u = fract(turns)
        val p = Offset(mix(w * 0.95f, w * 0.05f, u), horizon - sin(u * PI.toFloat()) * h * 0.42f)
        val isSun = (turns.toInt() % 2) == 0
        if (isSun) {
            softGlow(p, w * 0.06f, Color(0xFFFFE6A0), 0.6f)
            drawCircle(Color(0xFFFFE6A0), w * 0.025f, p)
        } else {
            drawCircle(Color(0xFFF2EEDC), w * 0.02f, p)
            drawCircle(LandC29.Sky, w * 0.018f, p + Offset(w * 0.01f, -w * 0.004f))
        }
    }
    // the two lands
    drawRect(Brush.verticalGradient(listOf(LandC29.Green, LandC29.GreenLow), startY = horizon, endY = h), Offset(0f, horizon), Size(w * 0.5f, h - horizon))
    drawRect(Brush.verticalGradient(listOf(LandC29.Shirk, LandC29.ShirkLow), startY = horizon, endY = h), Offset(w * 0.5f, horizon), Size(w * 0.5f, h - horizon))
    drawPath(Path().apply { moveTo(w * 0.44f, h); quadraticTo(w * 0.5f, h * 0.7f, w * 0.48f, horizon); lineTo(w * 0.54f, horizon); quadraticTo(w * 0.56f, h * 0.7f, w * 0.52f, h); close() }, Color(0xFF9A8A6A), alpha = 0.5f)
    // the road
    val road = Path()
    for (k in 0..24) { val p = road29(w, h, k / 24f); if (k == 0) road.moveTo(p.x, p.y) else road.lineTo(p.x, p.y) }
    drawPath(road, LandC29.Road, style = Stroke(h * 0.05f, cap = StrokeCap.Round))
    // the mosque on the left, the town of idols on the right
    val lands = easeOut(seg(co, 0f, 0.3f)).coerceAtLeast(if (nc > 0f || ex > 0f) 1f else 0f)
    domed29(Offset(w * 0.2f, horizon + h * 0.02f), w * 0.17f, LandC29.Wall, LandC29.Dome, lands)
    drawRect(LandC29.Wall, Offset(w * 0.32f, horizon - h * 0.2f), Size(w * 0.025f, h * 0.22f), alpha = lands)
    drawArc(LandC29.Dome, 180f, 180f, true, Offset(w * 0.32f, horizon - h * 0.225f), Size(w * 0.025f, h * 0.04f), alpha = lands)
    for (k in 0 until 3) {
        val x = w * (0.66f + 0.11f * k)
        drawRect(Color(0xFF9A8070), Offset(x - w * 0.045f, horizon - h * 0.1f), Size(w * 0.09f, h * 0.12f), alpha = lands)
        drawRect(LandC29.Idol, Offset(x - w * 0.02f, horizon - h * 0.135f), Size(w * 0.04f, h * 0.012f), alpha = lands)
        drawRoundRect(LandC29.Idol, Offset(x - w * 0.012f, horizon - h * 0.2f), Size(w * 0.024f, h * 0.07f), CornerRadius(w * 0.008f), alpha = lands)
        drawCircle(LandC29.Idol, w * 0.01f, Offset(x, horizon - h * 0.21f), alpha = lands)
    }
    val tagLands = seg(co, 0.15f, 0.35f)
    tagAt(a.tag("crossover", 0, 0), Offset(w * 0.77f, h * 0.12f), tagLands * (1f - seg(ex, 0f, 0.2f)), back = LandC29.Back, ink = Color(0xFFFFB0A0))
    tagAt(a.tag("crossover", 0, 1), Offset(w * 0.23f, h * 0.12f), tagLands * (1f - seg(ex, 0f, 0.2f)), back = LandC29.Back, ink = ArtInk.BrassLight)
    // the family on the road, then others after them
    val tall = h * 0.16f
    val walkers = mutableListOf<Pair<Float, Int>>()
    walkers += easeInOut(seg(co, 0.2f, 0.95f)) to 0
    if (nc > 0f) for (g in 1..3) walkers += easeInOut(seg(nc, 0.05f + 0.27f * (g - 1), 0.45f + 0.27f * (g - 1))) to g
    for ((u, g) in walkers) {
        if (u <= 0f) continue
        val arrived = u >= 1f
        val fade = if (arrived && g > 0) 0f else 1f
        val lead = road29(w, h, 0.05f + 0.7f * u)
        val walking = u in 0.01f..0.99f
        if (g == 0) {
            figure(lead, tall, LandC29.Ink, 1f, walk = if (walking) t * 5f else 0f)
            woman29(road29(w, h, 0.05f + 0.7f * u - 0.06f), tall * 0.95f, Color(0xFF3A2A3A), 1f, if (walking) t * 5f else 0f)
            figure(road29(w, h, 0.05f + 0.7f * u - 0.1f), tall * 0.55f, LandC29.Ink, 1f, walk = if (walking) t * 6f else 0f)
            camel26(road29(w, h, 0.05f + 0.7f * u - 0.17f), h * 0.15f, Color(0xFF7A5A3A), 1f, t, if (walking) t * 4f else 0f)
        } else {
            figure(lead, tall * 0.85f, LandC29.Ink, fade, walk = if (walking) t * 5f + g else 0f)
            if (g != 2) woman29(road29(w, h, 0.05f + 0.7f * u - 0.055f), tall * 0.8f, Color(0xFF3A2A3A), fade, if (walking) t * 5f else 0f)
            else figure(road29(w, h, 0.05f + 0.7f * u - 0.05f), tall * 0.85f, LandC29.Ink, fade, walk = if (walking) t * 5f + 1f else 0f)
        }
    }
    tagAt(a.tag("neverclosed"), Offset(w * 0.5f, h * 0.24f), seg(nc, 0.15f, 0.35f) * (1f - seg(ex, 0f, 0.25f)), back = LandC29.Back, ink = ArtInk.BrassLight)
    // those who cannot take the road: excused
    if (ex > 0f) {
        val o = easeOut(seg(ex, 0.05f, 0.35f))
        val pts = listOf(Offset(w * 0.64f, h * 0.88f), Offset(w * 0.77f, h * 0.9f), Offset(w * 0.9f, h * 0.88f))
        for ((k, p) in pts.withIndex()) {
            val g = easeOut(seg(ex, 0.35f + k * 0.12f, 0.6f + k * 0.12f))
            softGlow(p + Offset(0f, -h * 0.07f), w * 0.06f, Color(0xFFCFF0C0), 0.6f * g)
            when (k) {
                0 -> elder29(p, tall, LandC29.Ink, o)
                1 -> {
                    w8Seated(p, tall * 0.85f, Color(0xFF3A2A3A), -1f, o)
                    figure(p + Offset(w * 0.04f, 0f), tall * 0.5f, LandC29.Ink, o)
                }
                else -> {
                    // a sick man lying on a mat
                    drawRoundRect(Color(0xFFD8C8A8), Offset(p.x - w * 0.06f, p.y - h * 0.012f), Size(w * 0.12f, h * 0.02f), CornerRadius(h * 0.01f), alpha = o)
                    drawLine(LandC29.Ink, p + Offset(-w * 0.04f, -h * 0.025f), p + Offset(w * 0.04f, -h * 0.025f), tall * 0.07f, StrokeCap.Round, alpha = o)
                    drawCircle(LandC29.Ink, tall * 0.1f, p + Offset(-w * 0.055f, -h * 0.03f), alpha = o)
                }
            }
        }
        fitBig26(a.big("excused", 0, 0), Offset(w * 0.72f, h * 0.2f), w * 0.36f, Color(0xFF4A2E1A), seg(ex, 0.3f, 0.55f))
        fitBig26(a.big("excused", 0, 1), Offset(w * 0.72f, h * 0.29f), w * 0.36f, Color(0xFF4A2E1A), seg(ex, 0.45f, 0.7f))
    }
}

// ═════════════ 2. a scroll unrolled on five proofs (dalail-nubuwwa) ═════════════

private object ProofC29 {
    val Desk = Color(0xFF3A2A20)
    val Parch = Color(0xFFF4E9CF)
    val ParchLow = Color(0xFFE2CFA4)
    val Roller = Color(0xFF7A5230)
    val Gold = Color(0xFFE2BC5A)
    val Ink = Color(0xFF2A2430)
    val Quraysh = Color(0xFF4A3A3A)
    val Sand = Color(0xFFE2C890)
    val Back = Color(0xE6201810)
}

/**
 * A scroll on a dark table, unrolled from the right to a new picture at each proof. Beats: refusegold (his ﷺ light,
 * named; men bring heaps of gold and a crown before it: it takes nothing), trusted (men leave their boxes in his
 * keeping, «الصَّادِقُ الْأَمِينُ»; even when they turn against him the name stays), fewtomany (a handful of people in a
 * small circle on a map; then lights spring up across it), reproach (an open Mushaf: ﴿عَفَا اللَّهُ عَنْكَ﴾), saferider (a
 * road from «صَنْعَاءُ» to «حَضْرَمَوْتُ»: a lone rider goes along it in safety, past a grazing flock).
 */
internal fun DrawScope.proofscrollScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    followBeats(a, List(a.size) { null }, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0f) { proofscrollTable(a) }
}

private fun DrawScope.proofscrollTable(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val beats = listOf(a.b("refusegold"), a.b("trusted"), a.b("fewtomany"), a.b("reproach"), a.b("saferider"))
    drawRect(Brush.verticalGradient(listOf(ProofC29.Desk, Color(0xFF1E1610))))
    val band = Rect(w * 0.06f, h * 0.1f, w * 0.94f, h * 0.9f)
    val first = easeOut(seg(beats[0], 0f, 0.25f))
    if (first <= 0f) return
    // each picture unrolls over the one before it, from the right
    var edge = band.right
    for (k in 0 until 5) {
        val b = beats[k]
        if (b <= 0f) continue
        val reveal = easeInOut(seg(b, 0f, 0.3f))
        val left = band.right - band.width * reveal
        edge = left
        clipRect(left, band.top, band.right, band.bottom) {
            drawRect(Brush.verticalGradient(listOf(ProofC29.Parch, ProofC29.ParchLow), startY = band.top, endY = band.bottom), band.topLeft, band.size)
            drawRect(ProofC29.Gold, band.topLeft + Offset(w * 0.012f, h * 0.015f), Size(band.width - w * 0.024f, band.height - h * 0.03f), alpha = 0.5f, style = Stroke(1.2f * dp))
            when (k) {
                0 -> proofGold29(a, band, b, t)
                1 -> proofTrust29(a, band, b, t)
                2 -> proofSpread29(a, band, b, t)
                3 -> proofReproach29(a, band, b, t)
                else -> proofRider29(a, band, b, t)
            }
        }
    }
    // the rollers: one fixed at the right, one riding the edge as it unrolls
    for (x in listOf(band.right, edge)) {
        drawRoundRect(ProofC29.Roller, Offset(x - w * 0.012f, band.top - h * 0.03f), Size(w * 0.024f, band.height + h * 0.06f), CornerRadius(w * 0.012f), alpha = first)
        drawRect(Color(0xFF5A3A20), Offset(x - w * 0.004f, band.top - h * 0.03f), Size(w * 0.008f, band.height + h * 0.06f), alpha = 0.5f * first)
    }
}

private fun DrawScope.proofGold29(a: ArtCtx, r: Rect, p: Float, t: Float) {
    val w = size.width
    val h = size.height
    val light = Offset(r.left + r.width * 0.28f, r.top + r.height * 0.42f)
    sirajMunir(light, w * 0.012f, easeOut(seg(p, 0.1f, 0.35f)), t, nameAt = light + Offset(0f, -h * 0.12f))
    // heaps of gold and a crown pushed toward it; it takes nothing
    val push = easeInOut(seg(p, 0.35f, 0.7f))
    val heap = Offset(r.left + r.width * (0.78f - 0.18f * push), r.top + r.height * 0.78f)
    for (row in 0 until 3) for (k in 0 until 4 - row) {
        drawOval(ProofC29.Gold, Offset(heap.x - w * 0.05f + k * w * 0.026f + row * w * 0.013f, heap.y - h * 0.02f - row * h * 0.025f), Size(w * 0.026f, h * 0.018f), alpha = easeOut(seg(p, 0.25f, 0.45f)))
    }
    val crown = heap + Offset(w * 0.07f, -h * 0.05f)
    drawPath(Path().apply {
        moveTo(crown.x - w * 0.03f, crown.y); lineTo(crown.x - w * 0.03f, crown.y - h * 0.04f); lineTo(crown.x - w * 0.015f, crown.y - h * 0.02f)
        lineTo(crown.x, crown.y - h * 0.05f); lineTo(crown.x + w * 0.015f, crown.y - h * 0.02f); lineTo(crown.x + w * 0.03f, crown.y - h * 0.04f); lineTo(crown.x + w * 0.03f, crown.y); close()
    }, ProofC29.Gold, alpha = easeOut(seg(p, 0.3f, 0.5f)))
    for (k in 0 until 2) figure(Offset(r.left + r.width * (0.88f + 0.06f * k) - r.width * 0.18f * push, r.top + r.height * 0.88f), h * 0.2f, ProofC29.Quraysh, easeOut(seg(p, 0.2f, 0.4f)), arms = 0.7f)
    // the line it never crosses
    val stay = seg(p, 0.75f, 1f)
    if (stay > 0f) drawLine(Color(0xFFB8A27A), Offset(light.x + w * 0.1f, r.top + r.height * 0.25f), Offset(light.x + w * 0.1f, r.top + r.height * 0.9f), 1.4f * density, alpha = stay, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * density, 4f * density)))
}

private fun DrawScope.proofTrust29(a: ArtCtx, r: Rect, p: Float, t: Float) {
    val w = size.width
    val h = size.height
    val light = Offset(r.left + r.width * 0.3f, r.top + r.height * 0.45f)
    sirajMunir(light, w * 0.012f, 1f, t, nameAt = light + Offset(0f, -h * 0.12f))
    // men leave their boxes in his keeping
    for (k in 0 until 3) {
        val come = easeInOut(seg(p, 0.1f + k * 0.12f, 0.35f + k * 0.12f))
        val foot = Offset(r.left + r.width * (0.9f - 0.12f * k) - r.width * 0.15f * come, r.top + r.height * 0.9f)
        val turned = easeOut(seg(p, 0.7f, 0.85f))
        figure(foot, h * 0.2f, lerpColor(ProofC29.Quraysh, Color(0xFF7A2E2E), turned), 1f, walk = if (come in 0.01f..0.99f) t * 5f + k else 0f, arms = if (turned > 0f) -0.5f * turned else 0.5f)
        val set = easeOut(seg(p, 0.35f + k * 0.12f, 0.5f + k * 0.12f))
        val box = Offset(light.x + w * (0.06f + 0.05f * k), r.top + r.height * 0.82f)
        if (set > 0f) {
            drawRect(Color(0xFF8A5A2A), Offset(box.x - w * 0.018f, box.y - h * 0.03f * set), Size(w * 0.036f, h * 0.03f * set))
            drawRect(ProofC29.Gold, Offset(box.x - w * 0.018f, box.y - h * 0.03f * set), Size(w * 0.036f, h * 0.005f))
        }
    }
    tagAt(a.tag("trusted"), light + Offset(w * 0.2f, -h * 0.2f), seg(p, 0.5f, 0.7f), back = ProofC29.Back, ink = ArtInk.BrassLight)
}

private fun DrawScope.proofSpread29(a: ArtCtx, r: Rect, p: Float, t: Float) {
    val w = size.width
    val h = size.height
    // a plain map of land and sea
    val land = Path().apply {
        moveTo(r.left + r.width * 0.15f, r.top + r.height * 0.2f)
        quadraticTo(r.left + r.width * 0.5f, r.top + r.height * 0.1f, r.left + r.width * 0.85f, r.top + r.height * 0.25f)
        quadraticTo(r.left + r.width * 0.9f, r.top + r.height * 0.6f, r.left + r.width * 0.7f, r.top + r.height * 0.88f)
        quadraticTo(r.left + r.width * 0.4f, r.top + r.height * 0.95f, r.left + r.width * 0.2f, r.top + r.height * 0.7f)
        close()
    }
    drawRect(Color(0xFFBFD8E2), r.topLeft, r.size, alpha = 0.5f)
    drawPath(land, ProofC29.Sand, alpha = 0.9f)
    drawPath(land, Color(0xFFB89A62), style = Stroke(1.2f * density))
    // a handful in a small circle, his light among them
    val c = Offset(r.left + r.width * 0.55f, r.top + r.height * 0.55f)
    val few = easeOut(seg(p, 0.05f, 0.3f))
    drawCircle(Color(0xFFB89A62), w * 0.07f, c, alpha = 0.6f * few, style = Stroke(1.2f * density, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * density, 3f * density))))
    for (k in 0 until 4) {
        val ang = k * PI.toFloat() / 2f + 0.4f
        figure(c + Offset(cos(ang) * w * 0.04f, sin(ang) * h * 0.03f + h * 0.04f), h * 0.08f, ProofC29.Ink, few)
    }
    sirajMunir(c + Offset(0f, -h * 0.02f), w * 0.008f, few, t, nameAt = c + Offset(0f, -h * 0.11f))
    // then lights spring up across the land
    val spread = seg(p, 0.45f, 0.95f)
    for (k in 0 until 26) {
        val u = fract(sin(k * 12.9898f) * 43758.5f)
        val v = fract(sin(k * 78.233f) * 12345.6f)
        val q = Offset(r.left + r.width * (0.22f + 0.6f * u), r.top + r.height * (0.25f + 0.6f * v))
        val f = easeOut(seg(spread, k / 32f, k / 32f + 0.25f))
        if (f <= 0f) continue
        softGlow(q, w * 0.02f * f, Color(0xFFFFE6A0), 0.7f * f)
        drawCircle(Color(0xFFFFF4DA), w * 0.004f * f, q)
    }
}

private fun DrawScope.proofReproach29(a: ArtCtx, r: Rect, p: Float, t: Float) {
    val w = size.width
    val h = size.height
    val c = Offset(r.center.x, r.top + r.height * 0.66f)
    val o = easeOut(seg(p, 0.1f, 0.35f))
    drawLine(Color(0xFF6A4A2A), c + Offset(-w * 0.06f, h * 0.16f), c + Offset(w * 0.06f, h * 0.04f), w * 0.01f, alpha = o)
    drawLine(Color(0xFF6A4A2A), c + Offset(w * 0.06f, h * 0.16f), c + Offset(-w * 0.06f, h * 0.04f), w * 0.01f, alpha = o)
    openBook21(c, w * 0.22f, Color(0xFFFBF4E0), Color(0xFFB8A27A), o, glow = 0.5f)
    fitBig26(a.big("reproach"), Offset(r.center.x, r.top + r.height * 0.3f), r.width * 0.5f, Color(0xFF5A3A10), seg(p, 0.35f, 0.65f), glow = ArtInk.Gold)
}

private fun DrawScope.proofRider29(a: ArtCtx, r: Rect, p: Float, t: Float) {
    val w = size.width
    val h = size.height
    drawRect(Brush.verticalGradient(listOf(Color(0xFFF2D8A8), ProofC29.Sand), startY = r.top, endY = r.bottom), r.topLeft, r.size)
    val from = Offset(r.left + r.width * 0.88f, r.top + r.height * 0.72f)
    val to = Offset(r.left + r.width * 0.12f, r.top + r.height * 0.6f)
    drawLine(Color(0xFFE6D2A6), from, to, h * 0.035f, StrokeCap.Round)
    for (q in listOf(from, to)) {
        for (k in 0 until 3) drawRect(Color(0xFFC8A878), Offset(q.x - w * 0.03f + k * w * 0.02f, q.y - h * (0.06f + 0.02f * (k % 2))), Size(w * 0.018f, h * (0.06f + 0.02f * (k % 2))))
    }
    tagAt(a.tag("saferider", 0, 0), from + Offset(0f, h * 0.08f), seg(p, 0.1f, 0.3f), back = ProofC29.Back, ink = ArtInk.Ivory)
    tagAt(a.tag("saferider", 0, 1), to + Offset(0f, h * 0.08f), seg(p, 0.15f, 0.35f), back = ProofC29.Back, ink = ArtInk.Ivory)
    // a flock grazing by the road, untroubled
    for (k in 0 until 4) sheep26(Offset(r.left + r.width * (0.4f + 0.05f * k), r.top + r.height * (0.85f - 0.02f * (k % 2))), w * 0.025f, Color(0xFFF2EEE4), 1f)
    // the lone rider, at peace
    val go = easeInOut(seg(p, 0.2f, 1f))
    val pos = from + (to - from) * go - Offset(0f, h * 0.005f)
    camel26(pos, h * 0.16f, Color(0xFF7A5A3A), 1f, t, if (go in 0.01f..0.99f) t * 4f else 0f)
    drawCircle(ProofC29.Ink, h * 0.016f, pos + Offset(w * 0.005f, -h * 0.19f))
    drawLine(ProofC29.Ink, pos + Offset(w * 0.005f, -h * 0.175f), pos + Offset(w * 0.005f, -h * 0.13f), h * 0.012f, StrokeCap.Round)
}

// ═════════════ 3. the palace and its last brick (khatm-nubuwwa) ═════════════

private object BrickC29 {
    val Sky = Color(0xFFC2DAEC)
    val SkyLow = Color(0xFFF4E6C8)
    val Ground = Color(0xFFD8C094)
    val Brick = Color(0xFFD8B07A)
    val BrickDark = Color(0xFFB88A52)
    val Mortar = Color(0xFFF0E2C6)
    val Gap = Color(0xFF2A1E16)
    val Ink = Color(0xFF2A2430)
    val Seal = Color(0xFFD8A83A)
    val Red = Color(0xFFC0463A)
    val Back = Color(0xE6201810)
}

private val gap29 = 0.74f to 0.6f

/**
 * A palace on a plain by day. Beats: sealed (his ﷺ light, named, and under it a gold seal pressed into place), liars
 * (men come after with small flames of their own: each goes out, «ثَلَاثُونَ كَذَّابُونَ»), palacegap (the palace stands,
 * finished and fine but for one dark gap where a brick is missing; people walk round it and point to it), lastone (his
 * light comes down into the gap and becomes the last brick: the palace shines whole), toall (its light spreads to both
 * sides: to men on one, «الْإِنْسُ», to pale smokeless flames on the other, «الْجِنُّ»).
 */
internal fun DrawScope.lastbrickScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sealofall" to Shot(Offset(w * 0.5f, h * 0.3f), 1.2f),
        "liars" to Shot(Offset(w * 0.8f, h * 0.72f), 1.35f),
        "palacegap" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "lastone" to Shot(Offset(w * gap29.first, h * gap29.second), 1.45f),
        "toall" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { lastbrickPlain(a) }
}

private fun DrawScope.lastbrickPlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val se = a.b("sealofall")
    val li = a.b("liars")
    val pg = a.b("palacegap")
    val lo = a.b("lastone")
    val ta = a.b("toall")
    val ground = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(BrickC29.Sky, BrickC29.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(BrickC29.Ground, Offset(0f, ground), Size(w, h - ground))
    // the palace: built up row by row, a gap left at one place
    val built = easeOut(seg(pg, 0f, 0.45f)).coerceAtLeast(if (lo > 0f || ta > 0f) 1f else 0f)
    val left = w * 0.22f
    val right = w * 0.78f
    val top = h * 0.36f
    val rows = 9
    val bw = w * 0.056f
    val bh = (ground - top) / rows
    val gap = Offset(w * gap29.first, h * gap29.second)
    val fill = easeOut(seg(lo, 0.45f, 0.75f))
    if (built > 0f) {
        for (r in 0 until rows) {
            val rowF = seg(built, r / rows.toFloat(), (r + 1) / rows.toFloat())
            if (rowF <= 0f) continue
            val y = ground - (r + 1) * bh
            var x = left - (if (r % 2 == 1) bw * 0.5f else 0f)
            while (x < right) {
                val x0 = max(x, left)
                val x1 = min(x + bw, right)
                val isGap = gap.x in x0..x1 && gap.y in y..(y + bh)
                if (!isGap) {
                    drawRect(if ((r + (x / bw).toInt()) % 3 == 0) BrickC29.BrickDark else BrickC29.Brick, Offset(x0, y), Size(x1 - x0, bh), alpha = rowF)
                    drawRect(BrickC29.Mortar, Offset(x0, y), Size(x1 - x0, bh), alpha = rowF, style = Stroke(1f * dp))
                } else {
                    drawRect(lerpColor(BrickC29.Gap, ArtInk.Gold, fill), Offset(x0, y), Size(x1 - x0, bh), alpha = rowF)
                    if (fill > 0f) softGlow(Offset((x0 + x1) / 2f, y + bh / 2f), w * 0.06f, ArtInk.Gold, 0.7f * fill)
                }
                x += bw
            }
        }
        // crenellations and an arched door
        val crown = seg(built, 0.9f, 1f)
        for (k in 0 until 9) drawRect(BrickC29.Brick, Offset(left + k * (right - left) / 9f + w * 0.005f, top - bh * 0.6f), Size((right - left) / 9f - w * 0.02f, bh * 0.6f), alpha = crown)
        drawPath(archPath26(w * 0.5f, ground - bh * 3.5f, ground, w * 0.05f), Color(0xFF3A2A20), alpha = crown)
    }
    // the whole palace shines once the last brick is in
    if (fill > 0.9f) softGlow(Offset(w * 0.5f, h * 0.6f), w * 0.4f, Color(0xFFFFE6A0), 0.25f * seg(fill, 0.9f, 1f) * (0.85f + 0.15f * sin(t * 1.5f)))
    // people walk round and point at the gap
    val people = easeOut(seg(pg, 0.4f, 0.6f)) * (1f - easeOut(seg(lo, 0.6f, 0.9f)) * 0.5f)
    if (people > 0f) for (k in 0 until 3) {
        val walk = seg(pg, 0.4f, 1f)
        val foot = Offset(w * (0.9f - 0.08f * k) - w * 0.05f * walk, h * 0.94f)
        val point = easeOut(seg(pg, 0.7f + 0.08f * k, 0.85f + 0.08f * k)) * (1f - easeOut(seg(lo, 0.6f, 0.9f)))
        figure(foot, h * 0.15f, BrickC29.Ink, people, walk = if (point < 0.05f) t * 5f + k else 0f, arms = point)
    }
    // 1. sealed: his light, and the seal pressed home
    val sl = easeOut(seg(se, 0f, 0.3f))
    val lightAt = if (lo > 0f) mix(h * 0.18f, gap.y, easeInOut(seg(lo, 0f, 0.5f))) else h * 0.18f
    val lc = Offset(if (lo > 0f) mix(w * 0.5f, gap.x, easeInOut(seg(lo, 0f, 0.5f))) else w * 0.5f, lightAt)
    val lightOn = sl * (if (fill >= 1f) 0f else 1f)
    sirajMunir(lc, w * 0.013f, lightOn, t, nameAt = lc + Offset(-w * 0.15f, -h * 0.02f))
    if (fill >= 1f) {
        // his name stays beside the brick that completed the building
        sirajMunir(gap, w * 0.006f, 1f, t, nameAt = gap + Offset(w * 0.0f, -h * 0.1f))
    }
    val stamp = easeOut(seg(se, 0.35f, 0.65f)) * (1f - easeOut(seg(pg, 0f, 0.3f)))
    if (stamp > 0f) {
        val c = Offset(w * 0.5f, h * 0.33f)
        val drop = (1f - stamp) * h * 0.06f
        drawCircle(BrickC29.Seal, w * 0.04f, c + Offset(0f, -drop), alpha = stamp)
        drawCircle(Color(0xFFF2D27A), w * 0.032f, c + Offset(0f, -drop), alpha = stamp, style = Stroke(1.5f * dp))
        for (k in 0 until 2) withTransform({ rotate(45f * k, c + Offset(0f, -drop)) }) {
            drawRect(Color(0xFFF2D27A), Offset(c.x - w * 0.016f, c.y - drop - w * 0.016f), Size(w * 0.032f, w * 0.032f), alpha = stamp, style = Stroke(1.2f * dp))
        }
    }
    // 2. liars: small flames of their own, each going out
    if (li > 0f) {
        val o = easeOut(seg(li, 0f, 0.25f)) * (1f - easeOut(seg(pg, 0f, 0.3f)))
        for (k in 0 until 3) {
            val foot = Offset(w * (0.7f + 0.1f * k), h * 0.92f)
            figure(foot, h * 0.17f, Color(0xFF4A3A3A), o, arms = 0.6f)
            val hand = foot + Offset(-h * 0.17f * 0.32f, -h * 0.17f * 0.6f)
            val out = easeOut(seg(li, 0.3f + k * 0.15f, 0.5f + k * 0.15f))
            val fl = w * 0.012f * (1f - out) * (0.85f + 0.15f * sin(t * 11f + k))
            if (fl > 0.5f) drawPath(Path().apply { moveTo(hand.x - fl * 0.5f, hand.y); quadraticTo(hand.x, hand.y - fl * 2.2f, hand.x + fl * 0.5f, hand.y); close() }, Color(0xFFFFB04A), alpha = o)
            if (out > 0.5f) for (m in 0 until 2) {
                val f = fract(t * 0.6f + m * 0.5f + k * 0.3f)
                drawCircle(Color(0xFF9A9AA4), w * (0.004f + 0.006f * f), hand + Offset(sin(f * 6f) * w * 0.006f, -h * 0.02f - f * h * 0.05f), alpha = 0.5f * (1f - f) * o)
            }
        }
        tagAt(a.tag("liars"), Offset(w * 0.8f, h * 0.6f), seg(li, 0.2f, 0.4f) * (1f - seg(pg, 0f, 0.3f)), back = BrickC29.Back, ink = Color(0xFFFFB0A0))
    }
    // 3. to all: men on one side, pale flames on the other
    if (ta > 0f) {
        val o = easeOut(seg(ta, 0.1f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.55f)
        lightShafts(c, PI.toFloat(), 0.5f, w * 0.45f, Color(0xFFFFE6A0), 0.35f * o, t, rays = 5)
        lightShafts(c, 0f, 0.5f, w * 0.45f, Color(0xFFFFE6A0), 0.35f * o, t, rays = 5)
        for (k in 0 until 3) figure(Offset(w * (0.05f + 0.06f * k), h * 0.95f), h * 0.13f, BrickC29.Ink, easeOut(seg(ta, 0.3f + k * 0.08f, 0.5f + k * 0.08f)))
        for (k in 0 until 3) paleFlame29(Offset(w * (0.83f + 0.06f * k), h * 0.95f), h * 0.1f, t, k, easeOut(seg(ta, 0.35f + k * 0.08f, 0.55f + k * 0.08f)))
        tagAt(a.tag("toall", 0, 1), Offset(w * 0.11f, h * 0.68f), seg(ta, 0.5f, 0.7f), back = BrickC29.Back, ink = ArtInk.BrassLight)
        tagAt(a.tag("toall", 0, 0), Offset(w * 0.89f, h * 0.68f), seg(ta, 0.55f, 0.75f), back = BrickC29.Back, ink = Color(0xFFBFD8FF))
    }
}

// ═════════════ 4. by night from Makkah to Bayt al-Maqdis, and up through the seven heavens (isra-miraj) ═════════════

private object NightC29 {
    val Top = Color(0xFF080C24)
    val Low = Color(0xFF2A2650)
    val Ground = Color(0xFF14122A)
    val Layer = Color(0xFFBFC8FF)
    val Warm = Color(0xFFFFE6B0)
    val Back = Color(0xE60E1018)
}

private fun heaven29(h: Float, i: Int): Float = h * (0.7f - 0.083f * i)

/**
 * A night sky over a wide land. Beats: nightride (his ﷺ light, named, at the Kaaba «مَكَّةُ», travels by night to the
 * domed mosque «بَيْتُ الْمَقْدِسِ»), ledprayer (rows of smaller lights stand behind his: «الْأَنْبِيَاءُ»; then his light
 * rises), sevenheavens (up through seven arcs of heaven; at each a smaller named light: «آدَمُ», «يَحْيَى وَعِيسَى»,
 * «يُوسُفُ», «إِدْرِيسُ», «هَارُونُ», «مُوسَى», «إِبْرَاهِيمُ»), lotetop (above the seventh, a great tree of soft light),
 * fiftytofive (his light goes back and forth between that height and the sixth heaven: «50» becomes «5»).
 */
internal fun DrawScope.nightjourneyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "nightride" to Shot(Offset(w * 0.5f, h * 0.7f), 1.15f),
        "ledprayer" to Shot(Offset(w * 0.24f, h * 0.75f), 1.4f),
        "sevenheavens" to Shot(Offset(w * 0.3f, h * 0.4f), 1.12f),
        "lotetop" to Shot(Offset(w * 0.3f, h * 0.15f), 1.35f),
        "fiftytofive" to Shot(Offset(w * 0.3f, h * 0.22f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { nightjourneySky(a) }
}

private fun DrawScope.nightjourneySky(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val nr = a.b("nightride")
    val lp = a.b("ledprayer")
    val sv = a.b("sevenheavens")
    val lt = a.b("lotetop")
    val ff = a.b("fiftytofive")
    val ground = h * 0.86f
    deepSky(t, ground, NightC29.Top, NightC29.Low, starAlpha = 0.9f)
    drawRect(NightC29.Ground, Offset(0f, ground), Size(w, h - ground))
    val kaaba = Offset(w * 0.82f, ground)
    val quds = Offset(w * 0.22f, ground)
    val on = easeOut(seg(nr, 0f, 0.25f))
    kaaba29(kaaba, w * 0.08f, on)
    domed29(quds, w * 0.13f, Color(0xFFD8D2C4), Color(0xFF8A9AB0), on)
    tagAt(a.tag("nightride", 0, 0), kaaba + Offset(0f, h * 0.07f), seg(nr, 0.1f, 0.3f), back = NightC29.Back, ink = ArtInk.Ivory)
    tagAt(a.tag("nightride", 0, 1), quds + Offset(0f, h * 0.07f), seg(nr, 0.6f, 0.8f), back = NightC29.Back, ink = ArtInk.Ivory)
    // the seven heavens: arcs over the mosque, shown as he rises
    val col = w * 0.3f
    for (i in 0 until 7) {
        val f = easeOut(seg(sv, i / 7f, i / 7f + 0.2f)).coerceAtLeast(if (lt > 0f || ff > 0f) 1f else 0f)
        if (f <= 0f) continue
        val y = heaven29(h, i)
        drawArc(NightC29.Layer, 200f, 140f, false, Offset(col - w * 0.36f, y - h * 0.05f), Size(w * 0.72f, h * 0.2f), alpha = (0.3f - 0.02f * i) * f, style = Stroke(1.4f * dp))
    }
    // the prophets met in each heaven: smaller lights with their names
    for (i in 0 until 7) {
        val f = easeOut(seg(sv, i / 7f + 0.05f, i / 7f + 0.14f)).coerceAtLeast(if (lt > 0f || ff > 0f) 1f else 0f)
        if (f <= 0f) continue
        val y = heaven29(h, i) - h * 0.012f
        val side = if (i % 2 == 0) 1f else -1f
        val x = col + side * w * 0.11f
        val n = if (i == 1) 2 else 1
        for (m in 0 until n) {
            val c = Offset(x + m * side * w * 0.03f, y)
            softGlow(c, w * 0.025f, NightC29.Warm, 0.6f * f)
            drawCircle(Color(0xFFFFF4DA), w * 0.005f, c, alpha = f)
        }
        tagAt(a.tag("sevenheavens", 0, i), Offset(x + side * w * (0.09f + 0.015f * (n - 1)), y), f * (1f - 0.6f * seg(ff, 0f, 0.3f)), back = NightC29.Back, ink = ArtInk.Ivory)
    }
    // the rows of prophets at Bayt al-Maqdis, behind his light
    val rows = easeOut(seg(lp, 0.05f, 0.35f)) * (1f - easeOut(seg(sv, 0f, 0.3f)))
    if (rows > 0f) {
        for (r in 0 until 3) for (k in 0 until 5) {
            val c = Offset(quds.x + w * (0.1f + 0.035f * k), ground - h * (0.06f + 0.035f * r))
            softGlow(c, w * 0.015f, NightC29.Warm, 0.5f * rows)
            drawCircle(Color(0xFFFFF4DA), w * 0.0035f, c, alpha = rows)
        }
        tagAt(a.tag("ledprayer"), Offset(quds.x + w * 0.17f, ground - h * 0.2f), rows * seg(lp, 0.2f, 0.4f), back = NightC29.Back, ink = ArtInk.Ivory)
    }
    // the lote tree at the top: a great tree of soft light
    val tree = easeOut(seg(lt, 0.1f, 0.6f)).coerceAtLeast(if (ff > 0f) 1f else 0f)
    val treeC = Offset(col, h * 0.1f)
    if (tree > 0f) {
        softGlow(treeC, w * 0.16f, Color(0xFFF2FFE6), 0.5f * tree)
        drawLine(Color(0xFFE8F4D8), treeC + Offset(0f, h * 0.07f), treeC + Offset(0f, -h * 0.01f), w * 0.008f, StrokeCap.Round, alpha = tree)
        for (k in 0 until 7) {
            val ang = PI.toFloat() * (1.1f + 0.8f * k / 6f)
            drawOval(Color(0xFFE8F4D8), Offset(treeC.x + cos(ang) * w * 0.07f - w * 0.035f, treeC.y + sin(ang) * h * 0.05f - h * 0.02f), Size(w * 0.07f, h * 0.04f), alpha = 0.55f * tree)
        }
    }
    // his light: from the Kaaba to Bayt al-Maqdis, then up
    val ride = easeInOut(seg(nr, 0.2f, 0.85f))
    var pos = kaaba + Offset(0f, -h * 0.14f)
    val endRide = quds + Offset(0f, -h * 0.16f)
    if (ride > 0f) {
        val ctrl = Offset(w * 0.52f, h * 0.45f)
        pos = kaaba + Offset(0f, -h * 0.14f)
        val p0 = pos
        pos = p0 * ((1 - ride) * (1 - ride)) + ctrl * (2 * ride * (1 - ride)) + endRide * (ride * ride)
        val trail = Path()
        for (k in 0..20) {
            val u = k / 20f * ride
            val q = p0 * ((1 - u) * (1 - u)) + ctrl * (2 * u * (1 - u)) + endRide * (u * u)
            if (k == 0) trail.moveTo(q.x, q.y) else trail.lineTo(q.x, q.y)
        }
        drawPath(trail, NightC29.Warm, alpha = 0.35f * (1f - easeOut(seg(sv, 0f, 0.4f))), style = Stroke(1.5f * dp, cap = StrokeCap.Round))
    }
    val rise = easeInOut(seg(lp, 0.75f, 1f)) * 0.06f + easeInOut(seg(sv, 0f, 0.95f)) * 0.94f
    if (lp > 0f || sv > 0f) pos = Offset(mix(endRide.x, col, easeOut(seg(rise, 0f, 0.15f))), mix(endRide.y, heaven29(h, 6) - h * 0.03f, rise))
    if (lt > 0f) pos = Offset(col, mix(heaven29(h, 6) - h * 0.03f, treeC.y + h * 0.08f, easeInOut(seg(lt, 0f, 0.6f))))
    if (ff > 0f) {
        // back and forth between the top and the sixth heaven, where Musa is
        val trips = seg(ff, 0.05f, 0.9f) * 4f
        val u = fract(trips)
        val upDown = if (trips >= 4f) 0f else sin(u * PI.toFloat())
        pos = Offset(col, mix(treeC.y + h * 0.08f, heaven29(h, 5) - h * 0.03f, upDown))
        val fifty = 1f - seg(ff, 0.8f, 0.9f)
        tagAt(a.tag("fiftytofive", 0, 0), Offset(col + w * 0.32f, h * 0.34f), seg(ff, 0.05f, 0.15f) * fifty, back = NightC29.Back, ink = ArtInk.Gold)
        tagAt(a.tag("fiftytofive", 0, 1), Offset(col + w * 0.32f, h * 0.34f), seg(ff, 0.88f, 0.98f), back = NightC29.Back, ink = ArtInk.Gold)
    }
    val strength = easeOut(seg(nr, 0.05f, 0.3f))
    sirajMunir(pos, w * 0.012f, strength, t, nameAt = pos + Offset(w * 0.14f, h * 0.02f))
}
