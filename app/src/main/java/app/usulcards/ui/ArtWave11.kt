package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 11, round 4 of the redraw (2026-10-21): «الجماعة وولاة الأمر». A flock on one tether at golden hour, a
 * ship's deck at sea, a dam above a village, a Friday courtyard seen from above, a morning market, a rope bridge
 * over a gorge at dusk, and a house at night while strife fills the street.
 */

// ───────────── helpers ─────────────

/** A sheep in silhouette: [c] the middle of its body, [s] its length, [facing] -1 left / 1 right. */
private fun DrawScope.sheep11(c: Offset, s: Float, facing: Float, graze: Float, walk: Float, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    val wool = Color(0xFFF1E9D6)
    val dark = Color(0xFF2E2620)
    for (k in 0 until 4) {
        val x = c.x + (k - 1.5f) * s * 0.22f
        val swing = sin(walk + k * PI.toFloat()) * s * 0.06f
        drawLine(dark, Offset(x, c.y + s * 0.18f), Offset(x + swing, c.y + s * 0.45f), max(1.2f * density, s * 0.06f), StrokeCap.Round, alpha = alpha)
    }
    drawOval(Color.Black, topLeft = Offset(c.x - s * 0.45f, c.y + s * 0.4f), size = Size(s * 0.9f, s * 0.1f), alpha = 0.15f * alpha)
    drawOval(wool, topLeft = c - Offset(s * 0.5f, s * 0.3f), size = Size(s, s * 0.6f), alpha = alpha)
    for (k in 0 until 5) drawCircle(wool, s * 0.16f, c + Offset((k - 2f) * s * 0.18f, -s * 0.22f + s * 0.03f * (k % 2)), alpha = alpha)
    val head = c + Offset(facing * s * 0.52f, -s * 0.12f + s * 0.28f * graze)
    drawOval(dark, topLeft = head - Offset(s * 0.13f, s * 0.11f), size = Size(s * 0.26f, s * 0.22f), alpha = alpha)
    drawOval(dark, topLeft = head + Offset(-facing * s * 0.1f - s * 0.06f, -s * 0.12f), size = Size(s * 0.12f, s * 0.06f), alpha = alpha)
}

/** A plain red "not this" sign over [c], [r] its radius. */
private fun DrawScope.noSign11(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(Color(0xFFD23C30), r, c, alpha = alpha, style = Stroke(r * 0.18f))
    drawLine(Color(0xFFD23C30), c + Offset(-r * 0.7f, -r * 0.7f), c + Offset(r * 0.7f, r * 0.7f), r * 0.18f, alpha = alpha)
}

// ═════════════ 1. flock: one tether, one that slips it (luzum-jamaa) ═════════════

private object FlockC11 {
    val SkyTop = Color(0xFF6A8CC0)
    val SkyLow = Color(0xFFF6C98A)
    val Sun = Color(0xFFFFE2A0)
    val HillFar = Color(0xFF9AA86A)
    val Hill = Color(0xFF8CA04E)
    val Grass = Color(0xFF7E9A44)
    val GrassLush = Color(0xFF6EAA4A)
    val Gloom = Color(0xFF2A2A3A)
    val Rope = Color(0xFF8A6A40)
    val Stake = Color(0xFF5A3E24)
    val Ink = Color(0xFF2A2420)
    val Rain = Color(0xFFDDEBF6)
}

/**
 * A pasture at golden hour. Beats: fold (the flock grazes, each sheep's neck in a loop of one long tether), unloop
 * (one pulls its head out of its loop and wanders off toward the dark at the edge), astray (a second slips out and
 * follows it; eyes glint in the dark), gather (a man walks down with his staff and brings both back to their
 * loops), mercy (soft rain on the flock and the grass goes green; over the empty dark edge, a storm).
 */
internal fun DrawScope.flockScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fold" to Shot(Offset(w * 0.45f, h * 0.6f), 1.15f),
        "unloop" to Shot(Offset(w * 0.66f, h * 0.62f), 1.35f),
        "astray" to Shot(Offset(w * 0.75f, h * 0.6f), 1.3f),
        "gather" to Shot(Offset(w * 0.6f, h * 0.6f), 1.2f),
        "mercy" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { flockField(a) }
}

private fun DrawScope.flockField(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fb = a.b("fold")
    val ub = a.b("unloop")
    val ab = a.b("astray")
    val gb = a.b("gather")
    val mb = a.b("mercy")
    val horizon = h * 0.5f
    drawRect(Brush.verticalGradient(listOf(FlockC11.SkyTop, FlockC11.SkyLow), endY = horizon), size = Size(w, horizon))
    softGlow(Offset(w * 0.14f, horizon - h * 0.06f), w * 0.12f, FlockC11.Sun, 0.9f)
    drawCircle(FlockC11.Sun, w * 0.04f, Offset(w * 0.14f, horizon - h * 0.06f))
    hills(FlockC11.HillFar, horizon, h * 0.03f, 0.02f, 0.05f, 1.4f)
    val lush = easeOut(seg(mb, 0.2f, 0.9f))
    drawRect(Brush.verticalGradient(listOf(lerpColor(FlockC11.Hill, FlockC11.GrassLush, lush), lerpColor(FlockC11.Grass, FlockC11.GrassLush, 0.8f * lush)), startY = horizon, endY = h), topLeft = Offset(0f, horizon + h * 0.02f), size = Size(w, h - horizon))
    // the dark edge on the right: a ravine and its bushes, where a stray is lost
    val gloomX = w * 0.84f
    drawRect(Brush.horizontalGradient(listOf(Color.Transparent, FlockC11.Gloom.copy(alpha = 0.85f)), startX = gloomX - w * 0.12f, endX = w), topLeft = Offset(gloomX - w * 0.12f, horizon), size = Size(w - gloomX + w * 0.12f, h - horizon))
    for (k in 0 until 5) drawCircle(Color(0xFF1E2418), w * 0.04f, Offset(w * (0.86f + 0.04f * k), h * (0.6f + 0.06f * (k % 2))), alpha = 0.9f)
    val eyes = seg(ab, 0.4f, 0.7f) * (1f - seg(gb, 0.3f, 0.6f))
    if (eyes > 0f) for (sd in listOf(-1f, 1f)) drawCircle(Color(0xFFFFE08A), 2f * dp, Offset(w * 0.92f + sd * w * 0.008f, h * 0.64f), alpha = eyes * (0.6f + 0.4f * sin(t * 3f)))
    // the storm over the dark edge at the end
    if (mb > 0f) {
        for (k in 0 until 4) drawCircle(Color(0xFF3A3A4A), w * 0.06f, Offset(w * (0.82f + 0.06f * k), h * 0.1f + h * 0.02f * (k % 2)), alpha = 0.9f * seg(mb, 0f, 0.4f))
        if (sin(t * 3.3f) > 0.93f) drawLine(Color(0xFFFFF4C8), Offset(w * 0.9f, h * 0.14f), Offset(w * 0.88f, h * 0.4f), 1.6f * dp, alpha = seg(mb, 0.3f, 0.5f))
    }

    // the tether: one long rope across the pasture, on stakes, a loop for every neck
    val ropeY = h * 0.66f
    val ropeL = w * 0.16f
    val ropeR = w * 0.7f
    drawLine(FlockC11.Rope, Offset(ropeL, ropeY), Offset(ropeR, ropeY), 2f * dp, StrokeCap.Round)
    for (x in listOf(ropeL, ropeR)) drawLine(FlockC11.Stake, Offset(x, ropeY - h * 0.03f), Offset(x, ropeY + h * 0.05f), 3f * dp, StrokeCap.Round)
    val s = w * 0.07f
    val show = easeOut(seg(fb, 0f, 0.3f)).let { if (ub > 0f || gb > 0f || mb > 0f) 1f else it }
    for (k in 0 until 6) {
        val home = Offset(mix(ropeL + w * 0.04f, ropeR - w * 0.02f, k / 5f), ropeY + h * 0.07f)
        // sheep 5 slips first, sheep 4 follows it
        val away = when (k) {
            5 -> easeInOut(seg(ub, 0.2f, 1f))
            4 -> easeInOut(seg(ab, 0.1f, 1f))
            else -> 0f
        }
        val back = easeInOut(seg(gb, 0.25f, 0.9f))
        val lost = away * (1f - back)
        val pos = home + Offset(w * (0.18f - 0.03f * (5 - k)) * lost, h * (0.06f + 0.01f * k) * lost)
        val loose = (if (k == 5) seg(ub, 0f, 0.2f) else if (k == 4) seg(ab, 0f, 0.1f) else 0f) * (1f - seg(gb, 0.85f, 1f))
        // the loop: around the neck while tied; dropped on the rope once slipped
        val neck = pos + Offset(-s * 0.5f, -s * 0.12f)
        if (loose < 0.5f) {
            drawLine(FlockC11.Rope, Offset(home.x - s * 0.4f, ropeY), neck, 1.4f * dp, alpha = show)
            drawCircle(FlockC11.Rope, s * 0.11f, neck, alpha = show, style = Stroke(1.4f * dp))
        } else {
            drawCircle(FlockC11.Rope, s * 0.1f, Offset(home.x - s * 0.4f, ropeY + s * 0.1f), alpha = show, style = Stroke(1.4f * dp))
        }
        val moving = (k == 5 && ub in 0.2f..0.99f && gb <= 0f) || (k == 4 && ab in 0.1f..0.99f && gb <= 0f) || ((k >= 4) && gb in 0.25f..0.9f)
        val graze = if (moving) 0f else 0.5f + 0.5f * sin(t * 0.8f + k * 1.3f)
        sheep11(pos, s, if (moving && gb <= 0f) 1f else -1f, graze, if (moving) t * 8f else 0f, show)
    }
    // the man who brings them back
    if (gb > 0f) {
        val tall = h * 0.32f
        val come = easeInOut(seg(gb, 0f, 0.3f))
        val bring = easeInOut(seg(gb, 0.25f, 0.9f))
        val x = mix(w * 0.98f, w * 0.92f, come) - (w * 0.18f) * bring
        val foot = Offset(x, h * 0.86f)
        figure(foot, tall, FlockC11.Ink, walk = if (gb < 0.92f) t * 6f else 0f, arms = 0.5f)
        drawLine(FlockC11.Stake, foot + Offset(-tall * 0.25f, 0f), foot + Offset(-tall * 0.2f, -tall * 1.05f), 2f * dp, StrokeCap.Round)
        drawArc(FlockC11.Stake, 180f, 180f, false, topLeft = foot + Offset(-tall * 0.28f, -tall * 1.13f), size = Size(tall * 0.1f, tall * 0.1f), style = Stroke(2f * dp))
    }
    // mercy: soft rain over the flock
    if (mb > 0f) for (k in 0 until 50) {
        val x = mix(w * 0.05f, w * 0.76f, w8Rnd(k, 5.5f))
        val f = fract(w8Rnd(k, 2.2f) + t * 0.9f)
        val y = mix(h * 0.1f, h * 0.9f, f)
        drawLine(FlockC11.Rain, Offset(x, y), Offset(x - w * 0.004f, y + h * 0.03f), 1f * dp, alpha = 0.6f * seg(mb, 0f, 0.3f))
    }
}

// ═════════════ 2. deck: obedience in good, never in sin (samaa-taa) ═════════════

private object DeckC11 {
    val Sky = Color(0xFF8EC6EC)
    val SkyLow = Color(0xFFE6F2F6)
    val Sea = Color(0xFF2E6A9A)
    val SeaLow = Color(0xFF1E4A74)
    val Foam = Color(0xFFEAF6FA)
    val Hull = Color(0xFF6A4428)
    val HullDark = Color(0xFF4A2E1A)
    val Deck = Color(0xFFB08A5A)
    val Sail = Color(0xFFF6EED8)
    val Mast = Color(0xFF5A3A22)
    val Rope = Color(0xFF9A7A48)
    val Ink = Color(0xFF2A2026)
    val Jar = Color(0xFF7A2A3A)
    val Pledge = Color(0xFFF2C45A)
}

/**
 * A ship's deck at sea in the morning, the captain at the helm. Beats: heave (he points ahead; the crew haul the
 * ropes in time, the sail fills and the ship runs on), badorder (he points to a jar of wine for one of them), refuse
 * (that one holds up his palm and turns from it: a red sign over the jar), carryon (he goes straight back to his rope
 * with the others), pledge (no one reaches for the helm: a gold line of their pledge runs from each to the captain,
 * unbroken).
 */
internal fun DrawScope.deckScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "heave" to Shot(Offset(w * 0.5f, h * 0.5f), 1.08f),
        "badorder" to Shot(Offset(w * 0.6f, h * 0.6f), 1.4f),
        "refuse" to Shot(Offset(w * 0.55f, h * 0.6f), 1.5f),
        "carryon" to Shot(Offset(w * 0.45f, h * 0.55f), 1.2f),
        "pledge" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { deckSea(a) }
}

private fun DrawScope.deckSea(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val hv = a.b("heave")
    val bo = a.b("badorder")
    val rf = a.b("refuse")
    val co = a.b("carryon")
    val pl = a.b("pledge")
    val horizon = h * 0.5f
    drawRect(Brush.verticalGradient(listOf(DeckC11.Sky, DeckC11.SkyLow), endY = horizon), size = Size(w, horizon))
    for (k in 0 until 3) {
        val c = Offset(fract(0.2f + 0.33f * k - t * 0.01f * (if (hv > 0f) 1f else 0.2f)) * w * 1.2f - w * 0.1f, h * (0.1f + 0.05f * k))
        for (o in listOf(Offset(-w * 0.03f, 0f), Offset(0f, -h * 0.02f), Offset(w * 0.03f, 0f))) drawCircle(Color.White, w * 0.028f, c + o, alpha = 0.85f)
    }
    drawRect(Brush.verticalGradient(listOf(DeckC11.Sea, DeckC11.SeaLow), startY = horizon, endY = h), topLeft = Offset(0f, horizon), size = Size(w, h - horizon))
    val run = if (hv > 0.3f) 1f else seg(hv, 0f, 0.3f)
    for (k in 0 until 14) {
        val y = horizon + h * (0.03f + 0.035f * k)
        val x = fract(w8Rnd(k, 3.3f) + t * 0.05f * run * (1 + k % 3)) * w * 1.2f
        drawLine(DeckC11.Foam, Offset(w - x, y), Offset(w - x + w * 0.06f, y), 0.9f * dp, alpha = 0.4f)
    }
    val bob = sin(t * 1.3f) * h * 0.006f
    // the hull and the deck
    val deckY = h * 0.72f + bob
    val hull = Path().apply {
        moveTo(w * 0.04f, deckY); lineTo(w * 0.96f, deckY - h * 0.04f); lineTo(w * 0.9f, deckY + h * 0.14f); quadraticBezierTo(w * 0.5f, deckY + h * 0.2f, w * 0.12f, deckY + h * 0.13f); close()
    }
    drawPath(hull, DeckC11.Hull)
    drawPath(hull, DeckC11.HullDark, style = Stroke(1.6f * dp))
    drawLine(DeckC11.Deck, Offset(w * 0.04f, deckY), Offset(w * 0.96f, deckY - h * 0.04f), 3f * dp, StrokeCap.Round)
    for (k in 0 until 9) drawCircle(DeckC11.HullDark, 2f * dp, Offset(w * (0.16f + 0.08f * k), deckY + h * 0.06f - h * 0.004f * k))
    // the mast and the sail, filling when the ropes are hauled
    val mastX = w * 0.42f
    drawLine(DeckC11.Mast, Offset(mastX, deckY - h * 0.01f), Offset(mastX, h * 0.06f + bob), 3.4f * dp, StrokeCap.Round)
    val fill = easeOut(seg(hv, 0.2f, 0.7f))
    val sail = Path().apply {
        moveTo(mastX + w * 0.006f, h * 0.09f + bob)
        quadraticBezierTo(mastX + w * (0.06f + 0.12f * fill), h * 0.3f + bob, mastX + w * 0.006f, h * (0.56f - 0.12f * (1f - fill)) + bob)
        close()
    }
    drawPath(sail, DeckC11.Sail)
    drawPath(sail, Color(0xFFB8A88A), style = Stroke(1.2f * dp))
    val sail2 = Path().apply {
        moveTo(mastX - w * 0.006f, h * 0.12f + bob)
        quadraticBezierTo(mastX - w * (0.04f + 0.08f * fill), h * 0.3f + bob, mastX - w * 0.006f, h * (0.5f - 0.1f * (1f - fill)) + bob)
        close()
    }
    drawPath(sail2, DeckC11.Sail, alpha = 0.9f)
    // the helm at the stern, and the captain
    val helm = Offset(w * 0.86f, deckY - h * 0.1f)
    drawCircle(DeckC11.Mast, w * 0.03f, helm, style = Stroke(2.2f * dp))
    for (k in 0 until 6) {
        val ang = k * PI.toFloat() / 3f + t * 0.2f
        drawLine(DeckC11.Mast, helm, helm + Offset(cos(ang) * w * 0.04f, sin(ang) * w * 0.04f), 1.6f * dp, StrokeCap.Round)
    }
    val tall = h * 0.3f
    val cap = Offset(w * 0.8f, deckY - h * 0.035f)
    val pointJar = easeOut(seg(bo, 0f, 0.3f)) * (1f - easeOut(seg(co, 0f, 0.3f)))
    figure(cap, tall * 1.05f, DeckC11.Ink, arms = if (hv > 0f && hv < 1f && bo <= 0f) 0.9f else 0.4f + 0.5f * pointJar, lean = -0.04f)
    // the jar of wine he points to
    val jar = Offset(w * 0.64f, deckY - h * 0.02f)
    val jarShow = seg(bo, 0f, 0.25f)
    if (jarShow > 0f) {
        drawOval(DeckC11.Jar, topLeft = jar - Offset(w * 0.022f, h * 0.07f), size = Size(w * 0.044f, h * 0.07f), alpha = jarShow)
        drawRect(DeckC11.Jar, topLeft = jar - Offset(w * 0.008f, h * 0.09f), size = Size(w * 0.016f, h * 0.025f), alpha = jarShow)
        drawLine(Color.White, jar + Offset(-w * 0.01f, -h * 0.055f), jar + Offset(-w * 0.012f, -h * 0.02f), 1.2f * dp, alpha = 0.5f * jarShow)
        noSign11(jar + Offset(0f, -h * 0.045f), w * 0.04f, easeOut(seg(rf, 0.2f, 0.45f)))
    }
    // the crew: three at the ropes; the one nearest the jar is the one ordered
    val pull = 0.5f + 0.5f * sin(t * 5f)
    val hauling = hv > 0f
    for (k in 0 until 3) {
        val home = Offset(w * (0.2f + 0.1f * k), deckY - h * 0.005f * k)
        val nearJar = k == 2
        val steps = if (nearJar) easeInOut(seg(bo, 0.25f, 0.7f)) * (1f - easeInOut(seg(co, 0f, 0.4f))) else 0f
        val foot = home + Offset(w * 0.32f * steps, -h * 0.01f * steps)
        val refusing = nearJar && rf > 0.1f && co <= 0.2f
        if (refusing) {
            // facing the captain, palm up, head turned away from the jar
            withTransform({ scale(-1f, 1f, pivot = foot) }) { figure(foot, tall, DeckC11.Ink, arms = 0.9f, lean = 0.15f) }
            val palm = foot + Offset(tall * 0.28f, -tall * 0.78f)
            drawLine(DeckC11.Ink, palm, palm + Offset(0f, -tall * 0.1f), max(1.6f * dp, tall * 0.05f), StrokeCap.Round)
        } else {
            val atRope = hauling && steps < 0.05f
            figure(foot, tall, DeckC11.Ink, walk = if (steps in 0.02f..0.98f) t * 6f else 0f, lean = if (atRope) -0.15f - 0.15f * pull else 0f, arms = if (atRope) -0.6f - 0.3f * pull else 0.2f)
            if (atRope) {
                val hand = foot + Offset(tall * 0.2f, -tall * 0.65f)
                drawLine(DeckC11.Rope, hand, Offset(mastX, h * (0.2f + 0.04f * k) + bob), 1.4f * dp, StrokeCap.Round)
            }
        }
        // pledge: a gold line from each of them to the captain, holding
        if (pl > 0f) {
            val p = easeOut(seg(pl, 0.1f * k, 0.4f + 0.1f * k))
            val from = foot + Offset(0f, -tall * 0.6f)
            val to = cap + Offset(0f, -tall * 0.65f)
            drawPath(cubicPart(from, from + Offset(w * 0.05f, -h * 0.12f), to + Offset(-w * 0.05f, -h * 0.12f), to, 0f, p), DeckC11.Pledge, alpha = 0.85f, style = Stroke(1.6f * dp, cap = StrokeCap.Round))
            if (p >= 1f) glint(to + Offset(0f, -h * 0.02f), 5f * dp, DeckC11.Pledge, 0.8f)
        }
    }
}

// ═════════════ 3. dam: patience with the keeper, and the flood of rebellion (khuruj) ═════════════

private object DamC11 {
    val Sky = Color(0xFF9CC8E6)
    val SkyLow = Color(0xFFF0EAD6)
    val Storm = Color(0xFF4A4E5E)
    val Rock = Color(0xFF8E7E6A)
    val RockDark = Color(0xFF6A5A4A)
    val Water = Color(0xFF4E8CC0)
    val Wall = Color(0xFFB8A88E)
    val WallDark = Color(0xFF8A7A62)
    val Dry = Color(0xFFD2B878)
    val Green = Color(0xFF7AAE52)
    val House = Color(0xFFE2D2B2)
    val Roof = Color(0xFF9A5A3A)
    val Ink = Color(0xFF2A2420)
    val Mud = Color(0xFF7A6448)
    val Banner = Color(0xFF8A2A2A)
}

/**
 * A dam high in a valley holding a lake, its keeper's house on it, and a village below. Beats: picks (the keeper
 * runs the water to his own garden and the village fields dry; villagers come up with picks, and an elder stands
 * in their way with open arms until they lower them), repent (they kneel and raise their hands), incline (the
 * keeper turns the sluice and the water runs down to the village; its fields go green), breach (another band with a
 * banner swings picks at the dam; cracks run through it), flood (it bursts: the water tears down the valley,
 * through fields and houses, and scatters everyone).
 */
internal fun DrawScope.damScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "picks" to Shot(Offset(w * 0.45f, h * 0.55f), 1.2f),
        "repent" to Shot(Offset(w * 0.55f, h * 0.65f), 1.35f),
        "incline" to Shot(Offset(w * 0.55f, h * 0.55f), 1.05f),
        "breach" to Shot(Offset(w * 0.3f, h * 0.42f), 1.35f),
        "flood" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { damValley(a) }
}

private fun DrawScope.damValley(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pk = a.b("picks")
    val rp = a.b("repent")
    val ic = a.b("incline")
    val br = a.b("breach")
    val fl = a.b("flood")
    val storm = easeOut(seg(br, 0.3f, 1f))
    drawRect(Brush.verticalGradient(listOf(lerpColor(DamC11.Sky, DamC11.Storm, storm), lerpColor(DamC11.SkyLow, Color(0xFF8A8E9A), storm)), endY = h * 0.6f), size = Size(w, h * 0.6f))
    // the mountains and the lake behind the dam (upper left)
    drawPath(Path().apply { moveTo(0f, h * 0.45f); lineTo(w * 0.12f, h * 0.12f); lineTo(w * 0.26f, h * 0.3f); lineTo(w * 0.4f, h * 0.08f); lineTo(w * 0.58f, h * 0.36f); lineTo(w * 0.58f, h * 0.6f); lineTo(0f, h * 0.6f); close() }, DamC11.Rock)
    val burst = easeOut(seg(fl, 0f, 0.35f))
    val lakeTop = mix(h * 0.27f, h * 0.38f, burst)
    drawPath(Path().apply { moveTo(w * 0.03f, lakeTop); lineTo(w * 0.27f, lakeTop); lineTo(w * 0.27f, h * 0.42f); lineTo(w * 0.06f, h * 0.42f); close() }, DamC11.Water)
    // the valley floor, the fields and the village (lower right)
    drawRect(Brush.verticalGradient(listOf(Color(0xFFC8B07A), Color(0xFFB09868)), startY = h * 0.55f, endY = h), topLeft = Offset(0f, h * 0.55f), size = Size(w, h * 0.45f))
    drawPath(Path().apply { moveTo(0f, h * 0.6f); lineTo(w * 0.58f, h * 0.55f); lineTo(w * 0.58f, h * 0.6f); lineTo(0f, h * 0.62f); close() }, DamC11.RockDark)
    val green = easeOut(seg(ic, 0.3f, 0.9f)) * (1f - easeOut(seg(fl, 0.3f, 0.7f)))
    for (k in 0 until 4) {
        val fx = w * (0.6f + 0.09f * k)
        drawRect(lerpColor(DamC11.Dry, DamC11.Green, green), topLeft = Offset(fx, h * 0.82f), size = Size(w * 0.08f, h * 0.1f))
        for (r in 0 until 3) drawLine(lerpColor(Color(0xFFB09858), Color(0xFF5A8A3A), green), Offset(fx, h * (0.84f + 0.03f * r)), Offset(fx + w * 0.08f, h * (0.84f + 0.03f * r)), 0.8f * dp)
    }
    // the village houses: they stand, then the flood takes them
    for (k in 0 until 4) {
        val x = w * (0.64f + 0.09f * k)
        val y = h * 0.78f
        val hit = w8EaseIn(seg(fl, 0.25f + 0.08f * k, 0.6f + 0.08f * k))
        withTransform({ translate(w * 0.06f * hit, h * 0.06f * hit); rotate(35f * hit * (if (k % 2 == 0) 1f else -1f), Offset(x, y)) }) {
            drawRect(DamC11.House, topLeft = Offset(x - w * 0.03f, y - h * 0.07f), size = Size(w * 0.06f, h * 0.07f), alpha = 1f - 0.4f * hit)
            drawPath(Path().apply { moveTo(x - w * 0.035f, y - h * 0.07f); lineTo(x, y - h * 0.11f); lineTo(x + w * 0.035f, y - h * 0.07f); close() }, DamC11.Roof, alpha = 1f - 0.4f * hit)
            drawRect(DamC11.Ink, topLeft = Offset(x - w * 0.006f, y - h * 0.035f), size = Size(w * 0.012f, h * 0.035f), alpha = 1f - 0.4f * hit)
        }
    }
    // the dam wall, its cracks, and the keeper's house with his own lush garden
    val damL = w * 0.26f
    val damR = w * 0.32f
    if (burst < 1f) {
        drawPath(Path().apply { moveTo(damL, h * 0.24f); lineTo(damR, h * 0.24f); lineTo(damR + w * 0.03f, h * 0.6f); lineTo(damL - w * 0.01f, h * 0.6f); close() }, DamC11.Wall, alpha = 1f - burst)
        for (r in 0 until 6) drawLine(DamC11.WallDark, Offset(damL, h * (0.3f + 0.05f * r)), Offset(damR + w * 0.005f * r, h * (0.3f + 0.05f * r)), 0.8f * dp, alpha = 1f - burst)
        val cracks = easeOut(seg(br, 0.3f, 1f))
        if (cracks > 0f) for (k in 0 until 4) {
            val start = Offset(mix(damL, damR, 0.3f + 0.15f * k), h * (0.35f + 0.06f * k))
            val pts = List(6) { i -> start + Offset(w * 0.006f * (if (i % 2 == 0) 1f else -1f), h * 0.03f * i * cracks) }
            drawPath(w8Poly(pts), Color(0xFF3A2E22), alpha = 1f - burst, style = Stroke(1.4f * dp, join = StrokeJoin.Round))
        }
    }
    val keeper = Offset(w * 0.36f, h * 0.24f)
    drawRect(DamC11.House, topLeft = Offset(keeper.x - w * 0.025f, keeper.y - h * 0.06f), size = Size(w * 0.05f, h * 0.06f))
    drawPath(Path().apply { moveTo(keeper.x - w * 0.03f, keeper.y - h * 0.06f); lineTo(keeper.x, keeper.y - h * 0.09f); lineTo(keeper.x + w * 0.03f, keeper.y - h * 0.06f); close() }, DamC11.Roof)
    for (k in 0 until 4) drawCircle(DamC11.Green, w * 0.018f, Offset(w * (0.42f + 0.025f * k), h * (0.22f + 0.01f * (k % 2))))
    // the water: to his garden, or (once he turns the sluice) down the canal to the village
    val toVillage = easeInOut(seg(ic, 0f, 0.5f)) * (1f - burst)
    val canal = listOf(Offset(damR, h * 0.3f), Offset(w * 0.4f, h * 0.42f), Offset(w * 0.5f, h * 0.6f), Offset(w * 0.6f, h * 0.78f), Offset(w * 0.75f, h * 0.88f))
    drawPath(w8Poly(canal), Color(0xFFA08A62), style = Stroke(5f * dp, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (toVillage > 0f) drawPath(w8Poly(w8Head(w8Smooth(canal, 6), toVillage)), DamC11.Water, style = Stroke(3.4f * dp, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (burst < 1f) drawLine(DamC11.Water, Offset(damR, h * 0.27f), Offset(w * 0.42f, h * 0.24f), 2.6f * dp * (1f - toVillage), StrokeCap.Round, alpha = 1f - toVillage)
    // the sluice wheel he turns
    val wheel = Offset(damR + w * 0.01f, h * 0.28f)
    drawCircle(DamC11.Ink, w * 0.012f, wheel, style = Stroke(1.4f * dp))
    drawLine(DamC11.Ink, wheel + Offset(-w * 0.012f * cos(ic * 9f), -w * 0.012f * sin(ic * 9f)), wheel + Offset(w * 0.012f * cos(ic * 9f), w * 0.012f * sin(ic * 9f)), 1.4f * dp)

    val tall = h * 0.2f
    // picks: villagers come up with picks; the elder stops them; then they kneel (repent)
    val kneel = easeOut(seg(rp, 0f, 0.3f)) * (1f - seg(br, 0f, 0.2f))
    if (pk > 0f && br < 0.2f) {
        val come = easeInOut(seg(pk, 0f, 0.4f))
        val lower = easeOut(seg(pk, 0.55f, 0.8f))
        for (k in 0 until 3) {
            val foot = Offset(mix(w * (0.82f + 0.06f * k), w * (0.5f + 0.06f * k), come), h * (0.74f + 0.012f * k))
            if (kneel > 0.5f) {
                w8Seated(foot, tall, DamC11.Ink, facing = -1f, alpha = 1f - seg(br, 0f, 0.2f))
                for (sd in listOf(-1f, 1f)) drawLine(DamC11.Ink, foot + Offset(0f, -tall * 0.36f), foot + Offset(sd * tall * 0.12f, -tall * 0.55f), max(1.4f * dp, tall * 0.06f), StrokeCap.Round)
            } else {
                figure(foot, tall, DamC11.Ink, walk = if (come < 1f) t * 6f + k else 0f, arms = 0.2f)
                val grip = foot + Offset(-tall * 0.1f, -tall * 0.55f)
                val up = 1f - lower
                val head = grip + Offset(-tall * 0.25f * (1f - up) - tall * 0.05f, -tall * 0.45f * up + tall * 0.1f * (1f - up))
                drawLine(Color(0xFF6A4A2A), grip, head, 1.6f * dp, StrokeCap.Round)
                drawLine(Color(0xFF5A5A62), head + Offset(-tall * 0.08f, tall * 0.02f), head + Offset(tall * 0.08f, -tall * 0.02f), 2f * dp, StrokeCap.Round)
            }
        }
        // the elder in their way, arms open
        val eFoot = Offset(w * 0.46f, h * 0.73f)
        val stand = seg(pk, 0.25f, 0.4f) * (1f - kneel)
        if (stand > 0f) {
            figure(eFoot, tall * 1.02f, DamC11.Ink, alpha = stand)
            for (sd in listOf(-1f, 1f)) drawLine(DamC11.Ink, eFoot + Offset(0f, -tall * 0.76f), eFoot + Offset(sd * tall * 0.3f, -tall * 0.72f), max(1.4f * dp, tall * 0.06f), StrokeCap.Round, alpha = stand)
        }
        if (kneel > 0.5f) w8Seated(eFoot, tall, DamC11.Ink, facing = -1f)
    }
    // breach: another band with a banner swings picks at the dam
    if (br > 0f && burst < 1f) {
        for (k in 0 until 3) {
            val foot = Offset(w * (0.36f + 0.05f * k), h * 0.6f)
            val swing = 0.5f + 0.5f * sin(t * 7f + k * 1.4f)
            withTransform({ scale(-1f, 1f, pivot = foot) }) { figure(foot, tall, DamC11.Ink, alpha = seg(br, 0f, 0.2f) * (1f - burst), arms = 0.4f + 0.6f * swing, lean = 0.1f * swing) }
            val grip = foot + Offset(-tall * 0.25f, -tall * 0.6f)
            val head = grip + Offset(-tall * 0.25f * swing, -tall * 0.35f * (1f - swing))
            drawLine(Color(0xFF6A4A2A), grip, head, 1.6f * dp, StrokeCap.Round, alpha = 1f - burst)
        }
        val pole = Offset(w * 0.47f, h * 0.6f)
        drawLine(DamC11.Ink, pole, pole + Offset(0f, -h * 0.2f), 1.4f * dp, alpha = 1f - burst)
        drawRect(DamC11.Banner, topLeft = pole + Offset(0f, -h * 0.2f), size = Size(w * 0.05f, h * 0.05f), alpha = 1f - burst)
    }
    // flood: the torrent down the valley
    if (fl > 0f) {
        val reach = easeOut(seg(fl, 0f, 0.6f))
        val path = listOf(Offset(w * 0.28f, h * 0.35f), Offset(w * 0.36f, h * 0.55f), Offset(w * 0.55f, h * 0.7f), Offset(w * 0.8f, h * 0.84f), Offset(w * 1.05f, h * 0.9f))
        val head = w8Head(w8Smooth(path, 8), reach)
        drawPath(w8Poly(head), DamC11.Mud, alpha = 0.9f, style = Stroke(h * 0.12f * reach, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(w8Poly(head), DamC11.Water, alpha = 0.7f, style = Stroke(h * 0.07f * reach, cap = StrokeCap.Round, join = StrokeJoin.Round))
        for (k in 0 until 16) {
            val u = fract(t * 0.6f + k / 16f) * reach
            glint(w8Along(w8Smooth(path, 8), u) + Offset(0f, h * 0.03f * sin(k * 2f)), 4f * dp, Color.White, 0.6f)
        }
        // people scattered by it
        for (k in 0 until 4) {
            val f = seg(fl, 0.4f + 0.08f * k, 1f)
            if (f <= 0f) continue
            val foot = Offset(w * (0.6f + 0.1f * k) + w * 0.08f * f * (if (k % 2 == 0) 1f else -1f), h * (0.94f - 0.02f * k))
            figure(foot, tall * 0.9f, DamC11.Ink, walk = t * 9f + k, lean = 0.3f * (if (k % 2 == 0) -1f else 1f), arms = -0.8f)
        }
    }
}

// ═════════════ 4. aerial: the rows of Friday seen from above (salat-khalfahum) ═════════════

private object AerialC11 {
    val Stone = Color(0xFFE9DFC8)
    val Tile = Color(0xFFD8CBAE)
    val Wall = Color(0xFFB89A6A)
    val Mihrab = Color(0xFF2F6B66)
    val Gold = Color(0xFFD8B05A)
    val Head = Color(0xFF3A2A22)
    val Robe = Color(0xFFF6F2E8)
    val Robe2 = Color(0xFFD8D0C0)
    val Imam = Color(0xFF6A5A4A)
    val Anger = Color(0xFFD0402E)
}

/**
 * A Friday courtyard seen from straight above, the qibla wall at the top. Beats: rows (people come in from the
 * gates and settle into straight rows behind the one leading, then all bow together), scatter (some step out:
 * the rows break into separate knots with gaps between them, sharp red marks across the gaps, one knot behind a
 * second leader), regroup (the knots come back and close the rows into one body again).
 */
internal fun DrawScope.aerialScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rows" to Shot(Offset(w * 0.5f, h * 0.48f), 1.05f),
        "scatter" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "regroup" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { aerialCourt(a) }
}

private fun DrawScope.aerialCourt(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rb = a.b("rows")
    val sb = a.b("scatter")
    val gb = a.b("regroup")
    drawRect(AerialC11.Stone)
    // the tiled floor
    val s = w * 0.06f
    var y = h * 0.14f
    var row = 0
    while (y < h) {
        var x = (if (row % 2 == 0) 0f else s / 2)
        while (x < w) {
            drawPath(starPath(Offset(x, y), s * 0.3f), AerialC11.Tile)
            x += s
        }
        y += s * 0.8f
        row++
    }
    // the qibla wall and the mihrab, the gates at the bottom
    drawRect(AerialC11.Wall, size = Size(w, h * 0.1f))
    drawArc(AerialC11.Mihrab, 0f, 180f, true, topLeft = Offset(w * 0.46f, h * 0.06f), size = Size(w * 0.08f, h * 0.08f))
    drawArc(AerialC11.Gold, 0f, 180f, false, topLeft = Offset(w * 0.46f, h * 0.06f), size = Size(w * 0.08f, h * 0.08f), style = Stroke(1.6f * dp))
    for (x in listOf(0.2f, 0.8f)) drawRect(AerialC11.Wall, topLeft = Offset(w * x - w * 0.05f, h * 0.97f), size = Size(w * 0.1f, h * 0.03f))
    // the one leading, in front
    val imam = Offset(w * 0.5f, h * 0.2f)
    val bow = if (sb <= 0f) bump(seg(rb, 0.75f, 1f)) else 0f
    fun person(c: Offset, r: Float, robe: Color, alpha: Float, bowing: Float) {
        if (alpha <= 0.01f) return
        drawOval(robe, topLeft = Offset(c.x - r * 1.2f, c.y - r * 0.7f + r * 0.6f), size = Size(r * 2.4f, r * 1.5f), alpha = alpha)
        drawOval(Color.Black, topLeft = Offset(c.x - r * 1.2f, c.y - r * 0.1f), size = Size(r * 2.4f, r * 0.9f), alpha = 0.06f * alpha)
        drawCircle(AerialC11.Head, r * 0.6f, c + Offset(0f, -r * 0.9f * bowing), alpha = alpha)
    }
    person(imam, w * 0.016f, AerialC11.Imam, 1f, bow)
    // the rows: six of twelve; each person has a place in the rows and a place in a broken knot
    val cols = 12
    val rows = 6
    val r = w * 0.014f
    val split = easeInOut(seg(sb, 0.1f, 0.7f)) * (1f - easeInOut(seg(gb, 0.05f, 0.8f)))
    for (j in 0 until rows) for (i in 0 until cols) {
        val n = j * cols + i
        val target = Offset(w * (0.2f + 0.6f * i / (cols - 1f)), h * (0.32f + 0.1f * j))
        // coming in from the gates
        val gate = Offset(w * (if (i < cols / 2) 0.2f else 0.8f), h * 1.02f)
        val arrive = easeInOut(seg(rb, 0.02f + 0.008f * (rows - j) * cols / 12f + 0.004f * i, 0.32f + 0.008f * (rows - j) * cols / 12f + 0.004f * i))
        var p = gate + (target - gate) * arrive
        // the knots they break into
        val knot = when {
            i < 4 -> Offset(w * 0.18f, h * 0.62f)
            i < 8 -> Offset(w * 0.5f, h * 0.7f)
            else -> Offset(w * 0.82f, h * 0.6f)
        }
        val jitter = Offset((w8Rnd(n, 3.3f) - 0.5f) * w * 0.14f, (w8Rnd(n, 7.7f) - 0.5f) * h * 0.32f)
        val inKnot = knot + jitter
        p += (inKnot - p) * split
        person(p, r, if (n % 3 == 0) AerialC11.Robe2 else AerialC11.Robe, seg(arrive, 0f, 0.05f).coerceAtLeast(if (rb > 0.3f) 1f else 0f), bow)
    }
    // the second one leading a knot of his own, and the sharp marks between the knots
    if (split > 0.1f) {
        person(Offset(w * 0.82f, h * 0.42f), w * 0.016f, AerialC11.Imam, split, 0f)
        for ((x, yy) in listOf(0.34f to 0.62f, 0.66f to 0.62f)) {
            val c = Offset(w * x, h * yy)
            for (k in 0 until 3) {
                val f = fract(t * 1.4f + k / 3f)
                val zz = Path().apply {
                    moveTo(c.x - w * 0.02f, c.y - h * 0.06f + h * 0.05f * k)
                    lineTo(c.x, c.y - h * 0.08f + h * 0.05f * k)
                    lineTo(c.x + w * 0.02f, c.y - h * 0.06f + h * 0.05f * k)
                }
                drawPath(zz, AerialC11.Anger, alpha = split * (0.4f + 0.6f * (1f - f)), style = Stroke(1.8f * dp, cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
        }
    }
    // one body again
    if (gb > 0.7f) softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.4f, Color(0xFFFFF0C8), 0.35f * seg(gb, 0.7f, 1f))
}

// ═════════════ 5. souk: the hand, the tongue, the heart, and no sword (amr-maruf) ═════════════

private object SoukC11 {
    val Sky = Color(0xFFBFE0F2)
    val Wall = Color(0xFFE6D4B0)
    val WallShade = Color(0xFFC9B48A)
    val Awning = listOf(Color(0xFFB8463A), Color(0xFF3A6A8A), Color(0xFFD8A040))
    val Ground = Color(0xFFD8C49A)
    val Stall = Color(0xFF8A6440)
    val Brass = Color(0xFFC9A04A)
    val Ink = Color(0xFF2A2026)
    val Fake = Color(0xFFD0402E)
    val True = Color(0xFFF2C45A)
    val Hill = Color(0xFFB8A27A)
    val Palace = Color(0xFFD8C8A4)
    val Blade = Color(0xFFD8DCE4)
    val Dark = Color(0xFF5A2A2A)
}

/**
 * A morning market. Beats: cheat (at his stall a merchant presses the scale's pan down with his thumb, and the
 * buyer gets less), three (one with the authority lifts the thumb away and puts a true weight on, and the scale
 * evens; another speaks to the merchant, gently; an old woman passing lays her hand on her heart and shakes her
 * head: hand, tongue and heart, in turn), sheathe (a youth runs up drawing a sword; the one with the authority
 * stops his arm and the sword goes back in its sheath), swords (up on the hill a band with drawn swords marches on
 * the palace: the other view, under a dark sky).
 */
internal fun DrawScope.soukScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "cheat" to Shot(Offset(w * 0.5f, h * 0.62f), 1.4f),
        "three" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "sheathe" to Shot(Offset(w * 0.36f, h * 0.62f), 1.35f),
        "swords" to Shot(Offset(w * 0.7f, h * 0.38f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { soukStreet(a) }
}

private fun DrawScope.soukStreet(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ch = a.b("cheat")
    val th = a.b("three")
    val sh = a.b("sheathe")
    val sw = a.b("swords")
    val dark = easeOut(seg(sw, 0.2f, 0.8f))
    drawRect(lerpColor(SoukC11.Sky, Color(0xFF6A6474), dark), size = Size(w, h * 0.5f))
    // the hill with the palace, far behind
    drawPath(Path().apply { moveTo(w * 0.45f, h * 0.5f); quadraticBezierTo(w * 0.72f, h * 0.16f, w * 1.02f, h * 0.36f); lineTo(w * 1.02f, h * 0.5f); close() }, SoukC11.Hill)
    drawRect(SoukC11.Palace, topLeft = Offset(w * 0.8f, h * 0.18f), size = Size(w * 0.12f, h * 0.08f))
    drawArc(SoukC11.Palace, 180f, 180f, true, topLeft = Offset(w * 0.83f, h * 0.13f), size = Size(w * 0.06f, h * 0.1f))
    // the market street: walls and awnings, the ground
    drawRect(SoukC11.Wall, topLeft = Offset(0f, h * 0.3f), size = Size(w * 0.62f, h * 0.45f))
    drawRect(SoukC11.WallShade, topLeft = Offset(0f, h * 0.3f), size = Size(w * 0.62f, h * 0.025f))
    for (k in 0 until 3) {
        val x0 = w * (0.02f + 0.2f * k)
        drawPath(Path().apply { moveTo(x0, h * 0.42f); lineTo(x0 + w * 0.18f, h * 0.42f); lineTo(x0 + w * 0.2f, h * 0.5f); lineTo(x0 - w * 0.02f, h * 0.5f); close() }, SoukC11.Awning[k])
        for (s in 0 until 4) drawLine(Color.White, Offset(x0 + w * 0.045f * s, h * 0.42f), Offset(x0 + w * 0.045f * s - w * 0.01f, h * 0.5f), 2f * dp, alpha = 0.35f)
    }
    drawRect(SoukC11.Ground, topLeft = Offset(0f, h * 0.75f), size = Size(w, h * 0.25f))
    // the merchant's stall and the scale
    val stall = Offset(w * 0.5f, h * 0.78f)
    drawRect(SoukC11.Stall, topLeft = Offset(stall.x - w * 0.1f, stall.y - h * 0.1f), size = Size(w * 0.2f, h * 0.1f))
    val pivot = Offset(stall.x, stall.y - h * 0.24f)
    drawLine(SoukC11.Brass, Offset(stall.x, stall.y - h * 0.1f), pivot, 2f * dp)
    val fixed = easeOut(seg(th, 0.05f, 0.3f))
    val tip = (if (ch > 0.2f) 0.25f else 0f) * (1f - fixed)
    val armL = pivot + Offset(-w * 0.08f, h * 0.06f * tip)
    val armR = pivot + Offset(w * 0.08f, -h * 0.06f * tip)
    drawLine(SoukC11.Brass, armL, armR, 2f * dp, StrokeCap.Round)
    for ((k, end) in listOf(armL, armR).withIndex()) {
        val pan = end + Offset(0f, h * 0.06f)
        drawLine(SoukC11.Brass, end, pan + Offset(-w * 0.02f, 0f), 0.8f * dp)
        drawLine(SoukC11.Brass, end, pan + Offset(w * 0.02f, 0f), 0.8f * dp)
        drawArc(SoukC11.Brass, 0f, 180f, true, topLeft = pan - Offset(w * 0.025f, h * 0.012f), size = Size(w * 0.05f, h * 0.025f))
        if (k == 0) for (g in 0 until 4) drawCircle(Color(0xFF8A5A2A), w * 0.006f, pan + Offset((g - 1.5f) * w * 0.008f, -h * 0.006f))
        else {
            val weight = if (fixed > 0.5f) SoukC11.True else SoukC11.Fake
            drawRect(weight, topLeft = pan - Offset(w * 0.01f, h * 0.03f), size = Size(w * 0.02f, h * 0.022f))
        }
    }
    // the merchant behind his stall, his thumb on the pan
    val tall = h * 0.3f
    val mFoot = Offset(stall.x + w * 0.04f, stall.y - h * 0.02f)
    figure(mFoot, tall, SoukC11.Ink, arms = if (ch > 0.2f && fixed < 0.5f) 0.9f else 0.2f, lean = -0.05f)
    if (ch > 0.2f && fixed < 0.5f) drawCircle(SoukC11.Fake, w * 0.012f, armL + Offset(0f, h * 0.05f), alpha = 0.8f * (0.6f + 0.4f * sin(t * 6f)), style = Stroke(1.6f * dp))
    // the buyer, and the bag he gets
    val bFoot = Offset(w * 0.3f, h * 0.9f)
    withTransform({ scale(-1f, 1f, pivot = bFoot) }) { figure(bFoot, tall, SoukC11.Ink, arms = 0.5f) }
    // three: hand, tongue, heart, picked out in turn
    val focus = seg(th, 0f, 1f) * 3f
    fun spot(c: Offset, k: Int) {
        val on = bump(((focus - k) * 0.5f + 0.25f).coerceIn(0f, 1f) * 1f)
        if (th > 0f) softGlow(c, w * 0.08f, Color(0xFFFFF0C8), 0.5f * on)
    }
    // the one with authority: hand
    val iFoot = Offset(w * 0.4f, h * 0.88f)
    val stop = easeOut(seg(sh, 0.3f, 0.5f))
    spot(iFoot + Offset(0f, -tall * 0.6f), 0)
    withTransform({ scale(-1f, 1f, pivot = iFoot) }) { figure(iFoot, tall * 1.04f, Color(0xFF1E3A4A), arms = if (th in 0.01f..0.35f) 0.9f else 0.3f + 0.6f * stop) }
    // the one who speaks: tongue
    val tFoot = Offset(w * 0.66f, h * 0.9f)
    spot(tFoot + Offset(0f, -tall * 0.6f), 1)
    figure(tFoot, tall, SoukC11.Ink, arms = if (th in 0.34f..0.7f) 0.6f else 0.1f)
    if (th in 0.34f..0.8f) for (k in 0 until 3) {
        val f = fract(t * 1.2f + k / 3f)
        val c = tFoot + Offset(-tall * 0.3f - tall * 0.2f * f, -tall * 0.85f)
        drawArc(SoukC11.Ink, 140f, 80f, false, topLeft = c - Offset((5f + 7f * f) * dp, (5f + 7f * f) * dp), size = Size((10f + 14f * f) * dp, (10f + 14f * f) * dp), alpha = 0.7f * (1f - f), style = Stroke(1.4f * dp, cap = StrokeCap.Round))
    }
    // the old woman passing: heart
    val wFoot = Offset(w * 0.86f, h * 0.92f)
    spot(wFoot + Offset(0f, -tall * 0.6f), 2)
    val shake = if (th > 0.66f) sin(t * 8f) * tall * 0.02f else 0f
    figure(wFoot, tall * 0.9f, Color(0xFF3A2A3A), arms = 0.1f)
    drawPath(Path().apply { moveTo(wFoot.x - tall * 0.12f, wFoot.y - tall * 0.4f); lineTo(wFoot.x + tall * 0.12f, wFoot.y - tall * 0.4f); lineTo(wFoot.x + tall * 0.08f, wFoot.y - tall * 0.02f); lineTo(wFoot.x - tall * 0.08f, wFoot.y - tall * 0.02f); close() }, Color(0xFF3A2A3A))
    drawCircle(Color(0xFF3A2A3A), tall * 0.1f, wFoot + Offset(shake, -tall * 0.9f * 0.85f - tall * 0.06f))
    if (th > 0.66f) {
        val chest = wFoot + Offset(0f, -tall * 0.52f)
        drawPath(w8Heart(chest, tall * 0.06f), Color(0xFFF0B8A8))
        drawLine(Color(0xFF3A2A3A), wFoot + Offset(0f, -tall * 0.62f), chest + Offset(-tall * 0.04f, 0f), max(1.4f * dp, tall * 0.05f), StrokeCap.Round)
    }
    // sheathe: a youth runs up drawing a sword; the hand that has the right stops his arm; back in the sheath
    if (sh > 0f) {
        val run = easeInOut(seg(sh, 0f, 0.3f))
        val yFoot = Offset(mix(w * 0.02f, w * 0.3f, run), h * 0.92f)
        val back = easeInOut(seg(sh, 0.5f, 0.85f))
        withTransform({ scale(-1f, 1f, pivot = yFoot) }) { figure(yFoot, tall * 0.95f, SoukC11.Ink, walk = if (run < 1f) t * 9f else 0f, arms = 0.9f * (1f - back)) }
        val hilt = yFoot + Offset(tall * 0.28f, -tall * 0.72f + tall * 0.3f * back)
        val bladeEnd = hilt + Offset(tall * 0.45f * (1f - back), -tall * 0.15f * (1f - back) + tall * 0.35f * back)
        drawLine(SoukC11.Blade, hilt, bladeEnd, 2f * dp, StrokeCap.Round, alpha = 1f - 0.6f * back)
        drawLine(Color(0xFF6A4A2A), yFoot + Offset(tall * 0.05f, -tall * 0.4f), yFoot + Offset(tall * 0.12f, -tall * 0.05f), 3f * dp, StrokeCap.Round)
    }
    // swords: up on the hill, a band marches on the palace with drawn swords
    if (sw > 0f) {
        val march = easeInOut(seg(sw, 0.1f, 1f))
        for (k in 0 until 5) {
            val foot = Offset(mix(w * 0.55f, w * 0.72f, march) + w * 0.03f * k, h * (0.42f - 0.025f * k) - h * 0.06f * march)
            withTransform({ scale(-1f, 1f, pivot = foot) }) { figure(foot, h * 0.1f, SoukC11.Dark, alpha = seg(sw, 0f, 0.2f), walk = t * 7f + k, arms = 0.9f) }
            drawLine(SoukC11.Blade, foot + Offset(h * 0.03f, -h * 0.075f), foot + Offset(h * 0.05f, -h * 0.14f), 1.4f * dp, StrokeCap.Round, alpha = seg(sw, 0f, 0.2f))
        }
        softGlow(Offset(w * 0.72f, h * 0.3f), w * 0.14f, Color(0xFFB03A2A), 0.35f * dark)
    }
}

// ═════════════ 6. bridge: the warning given, and kept back (nasiha) ═════════════

private object BridgeC11 {
    val SkyTop = Color(0xFF3A3A6A)
    val SkyLow = Color(0xFFE89A6A)
    val Cliff = Color(0xFF5A4636)
    val CliffDark = Color(0xFF3A2C22)
    val Gorge = Color(0xFF1A1418)
    val Plank = Color(0xFFB08A5A)
    val Rope = Color(0xFF8A6A40)
    val Ink = Color(0xFF1E1620)
    val White = Color(0xFFF4EEE2)
    val Stain = Color(0xFF6A4A3A)
    val Call = Color(0xFFFFE6A8)
}

/**
 * A rope bridge over a gorge at dusk, one plank missing in the middle, a guide at the near end. Beats: warn (a
 * traveller steps on; the guide calls and points at the gap, and he steps over it holding the rope), both (two
 * more, one in white with prayer beads, one in a stained robe: the guide warns each the same, and both get
 * across), silent (another stands at the post, sees one walking to the gap, and turns his face away: the
 * traveller falls through and hangs from the rope), unravel (the ropes fray and planks drop one after another:
 * the harm reaches everyone).
 */
internal fun DrawScope.bridgeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "warn" to Shot(Offset(w * 0.4f, h * 0.5f), 1.3f),
        "both" to Shot(Offset(w * 0.45f, h * 0.5f), 1.15f),
        "silent" to Shot(Offset(w * 0.4f, h * 0.55f), 1.35f),
        "unravel" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { bridgeGorge(a) }
}

private fun DrawScope.bridgeGorge(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val wn = a.b("warn")
    val bt = a.b("both")
    val sl = a.b("silent")
    val ur = a.b("unravel")
    drawRect(Brush.verticalGradient(listOf(BridgeC11.SkyTop, BridgeC11.SkyLow), endY = h * 0.6f), size = Size(w, h * 0.6f))
    softGlow(Offset(w * 0.5f, h * 0.55f), w * 0.2f, Color(0xFFFFC890), 0.6f)
    drawRect(BridgeC11.Gorge, topLeft = Offset(0f, h * 0.55f), size = Size(w, h * 0.45f))
    // the two cliffs
    drawPath(Path().apply { moveTo(0f, h * 0.48f); lineTo(w * 0.2f, h * 0.48f); lineTo(w * 0.17f, h); lineTo(0f, h); close() }, BridgeC11.Cliff)
    drawPath(Path().apply { moveTo(w, h * 0.48f); lineTo(w * 0.8f, h * 0.48f); lineTo(w * 0.83f, h); lineTo(w, h); close() }, BridgeC11.Cliff)
    for (k in 0 until 6) drawLine(BridgeC11.CliffDark, Offset(w * 0.02f * k, h * (0.55f + 0.07f * k)), Offset(w * 0.15f, h * (0.58f + 0.07f * k)), 1f * dp, alpha = 0.6f)
    // the bridge: a sagging rope and its planks; one is missing in the middle; at the end the ropes fray
    val l = Offset(w * 0.2f, h * 0.5f)
    val r = Offset(w * 0.8f, h * 0.5f)
    fun deck(u: Float) = Offset(mix(l.x, r.x, u), mix(l.y, r.y, u) + h * 0.08f * 4f * u * (1f - u))
    val n = 20
    val gapAt = 10
    for (k in 0 until n) {
        val u = (k + 0.5f) / n
        if (k == gapAt) continue
        val drop = w8EaseIn(seg(ur, 0.1f + 0.035f * ((k * 7) % n), 0.4f + 0.035f * ((k * 7) % n)))
        val p = deck(u) + Offset(0f, h * 0.5f * drop)
        withTransform({ rotate(60f * drop * (if (k % 2 == 0) 1f else -1f), p) }) {
            drawRect(BridgeC11.Plank, topLeft = p - Offset(w * 0.012f, h * 0.008f), size = Size(w * 0.024f, h * 0.016f), alpha = 1f - 0.5f * drop)
        }
    }
    val fray = easeOut(seg(ur, 0f, 0.8f))
    for (dy in listOf(0f, -h * 0.1f)) {
        val pts = List(31) { i -> deck(i / 30f) + Offset(0f, dy) }
        val cutU = 0.5f
        val leftPart = pts.filter { (it.x - l.x) / (r.x - l.x) < cutU - 0.02f * fray }
        val rightPart = pts.filter { (it.x - l.x) / (r.x - l.x) > cutU + 0.02f * fray }
        drawPath(w8Poly(leftPart.map { it + Offset(0f, h * 0.3f * fray * ((it.x - l.x) / (r.x - l.x)) * 2f) }), BridgeC11.Rope, style = Stroke(1.6f * dp))
        drawPath(w8Poly(rightPart.map { it + Offset(0f, h * 0.3f * fray * (1f - (it.x - l.x) / (r.x - l.x)) * 2f) }), BridgeC11.Rope, style = Stroke(1.6f * dp))
    }
    for (p in listOf(l, r)) drawLine(BridgeC11.CliffDark, p + Offset(0f, h * 0.01f), p + Offset(0f, -h * 0.14f), 3f * dp, StrokeCap.Round)
    // the gap, marked
    val gap = deck((gapAt + 0.5f) / n)
    val tall = h * 0.22f
    // the guide at the near end: he calls and points to the gap
    val gFoot = Offset(w * 0.13f, h * 0.48f)
    val calling = (if (wn in 0.1f..0.9f) 1f else 0f) + (if (bt in 0.05f..0.95f) 1f else 0f)
    val turnedAway = sl > 0.1f
    if (!turnedAway || ur > 0f) {
        figure(gFoot, tall, BridgeC11.Ink, alpha = if (turnedAway) 0f else 1f, arms = if (calling > 0f) 1f else 0.2f, lean = 0f)
    }
    if (calling > 0f && !turnedAway) {
        for (k in 0 until 3) {
            val f = fract(t * 1.4f + k / 3f)
            val c = gFoot + Offset(tall * 0.35f + w * 0.12f * f, -tall * 0.82f + (gap.y - gFoot.y) * 0.6f * f)
            drawArc(BridgeC11.Call, -45f, 90f, false, topLeft = c - Offset((5f + 6f * f) * dp, (5f + 6f * f) * dp), size = Size((10f + 12f * f) * dp, (10f + 12f * f) * dp), alpha = 0.9f * (1f - f), style = Stroke(1.8f * dp, cap = StrokeCap.Round))
        }
        softGlow(gap, w * 0.03f, BridgeC11.Call, 0.6f * (0.6f + 0.4f * sin(t * 5f)))
    }
    // the one who keeps silent: at the post, his face turned away, arms folded
    if (turnedAway && ur <= 0.3f) {
        withTransform({ scale(-1f, 1f, pivot = gFoot) }) { figure(gFoot, tall, BridgeC11.Ink, arms = 0f) }
        drawLine(BridgeC11.Ink, gFoot + Offset(-tall * 0.1f, -tall * 0.6f), gFoot + Offset(tall * 0.1f, -tall * 0.6f), max(1.6f * dp, tall * 0.06f), StrokeCap.Round)
    }
    // the travellers: each crosses step by step; at the gap they step over (warned) or fall in (not warned)
    fun walker(start: Float, end: Float, beat: Float, color: Color, warned: Boolean, kit: Int) {
        if (beat <= 0f) return
        val u = mix(start, end, easeInOut(seg(beat, 0.05f, 0.95f)))
        val atGap = abs(u - (gapAt + 0.5f) / n) < 0.03f
        val fall = if (!warned) w8EaseIn(seg(beat, 0.55f, 0.75f)) else 0f
        val uu = if (!warned) min(u, (gapAt + 0.5f) / n) else u
        var foot = deck(uu)
        if (warned && atGap) foot += Offset(0f, -h * 0.03f)
        foot += Offset(0f, tall * 0.9f * fall)
        val alpha = 1f - seg(ur, 0.2f, 0.4f)
        if (alpha <= 0f) return
        withTransform({ scale(-1f, 1f, pivot = foot) }) {
            figure(foot, tall, color, alpha = alpha, walk = if (beat in 0.05f..0.95f && fall <= 0f) t * 6f else 0f, arms = if (fall > 0f) -1f else if (atGap) 0.9f else 0.1f)
        }
        if (kit == 1) for (b in 0 until 5) drawCircle(Color(0xFF6A4A2A), 1.2f * dp, foot + Offset(tall * 0.12f, -tall * (0.42f + 0.03f * b)), alpha = alpha)
        if (kit == 2) for (s in 0 until 3) drawCircle(BridgeC11.Stain, tall * 0.035f, foot + Offset(tall * 0.02f * s, -tall * (0.5f + 0.08f * s)), alpha = alpha)
        if (fall > 0f) {
            // he hangs from the rope by his hands
            drawLine(BridgeC11.Ink, foot + Offset(0f, -tall * 0.76f), gap + Offset(0f, -h * 0.005f), max(1.4f * dp, tall * 0.05f), StrokeCap.Round, alpha = alpha)
        }
    }
    walker(0.02f, 0.98f, wn, BridgeC11.Ink, true, 0)
    walker(0.02f, 0.98f, seg(bt, 0f, 0.7f), BridgeC11.White, true, 1)
    walker(0.0f, 0.96f, seg(bt, 0.25f, 1f), BridgeC11.Ink, true, 2)
    walker(0.02f, 0.9f, sl, BridgeC11.Ink, false, 0)
}

// ═════════════ 7. refuge: the house at night while strife fills the street (fitna) ═════════════

private object RefugeC11 {
    val SkyTop = Color(0xFF07091A)
    val SkyLow = Color(0xFF2A1A2A)
    val House = Color(0xFF2A2638)
    val HouseLight = Color(0xFF3A3448)
    val Door = Color(0xFF14101C)
    val Window = Color(0xFFFFCF7A)
    val Shutter = Color(0xFF5A4030)
    val Ground = Color(0xFF1E1A26)
    val Fire = Color(0xFFFF7A3A)
    val Smoke = Color(0xFF4A4250)
    val Crowd = Color(0xFF0E0A12)
    val BannerA = Color(0xFF8A2A2A)
    val BannerB = Color(0xFF2A4A8A)
    val Torch = Color(0xFFFFB45A)
    val Stranger = Color(0xFFEDEBE4)
}

/**
 * A town at night: one house in the middle, its window warm. Beats: strife (torches appear at both ends of the
 * street, fire glows behind the roofs, smoke rises), stayin (a man walking in the street turns, goes into his house
 * and shuts the door), banners (two banners of two clans rise over the two crowds; men rally to them), melee (the
 * crowds clash in the street, sticks swinging, sparks), shutters (he swings the shutters closed), seated (through
 * the slats, he sits inside, in the warm light, while the street rages on).
 */
internal fun DrawScope.refugeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "strife" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "stayin" to Shot(Offset(w * 0.5f, h * 0.62f), 1.35f),
        "banners" to Shot(Offset(w * 0.5f, h * 0.45f), 1.1f),
        "melee" to Shot(Offset(w * 0.5f, h * 0.62f), 1.15f),
        "shutters" to Shot(Offset(w * 0.5f, h * 0.48f), 1.6f),
        "seated" to Shot(Offset(w * 0.5f, h * 0.5f), 1.45f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.2f) { refugeTown(a) }
}

private fun DrawScope.refugeTown(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sf = a.b("strife")
    val si = a.b("stayin")
    val bn = a.b("banners")
    val ml = a.b("melee")
    val su = a.b("shutters")
    val st = a.b("seated")
    val ground = h * 0.78f
    val glow = easeOut(seg(sf, 0f, 0.6f))
    drawRect(Brush.verticalGradient(listOf(RefugeC11.SkyTop, lerpColor(RefugeC11.SkyLow, Color(0xFF5A2A20), glow)), endY = ground), size = Size(w, ground))
    for (k in 0 until 20) drawCircle(Color.White, (0.5f + 0.7f * w8Rnd(k, 2.9f)) * dp, Offset(w8Rnd(k, 11.7f) * w, w8Rnd(k, 33.3f) * h * 0.3f), alpha = 0.4f * (1f - 0.5f * glow))
    // fire behind the roofs, and smoke
    if (glow > 0f) {
        for (x in listOf(0.12f, 0.88f)) softGlow(Offset(w * x, ground - h * 0.25f), w * 0.2f, RefugeC11.Fire, 0.55f * glow * (0.8f + 0.2f * sin(t * 5f + x * 9f)))
        for (k in 0 until 10) {
            val f = fract(t * 0.12f + k / 10f)
            val x = w * (if (k % 2 == 0) 0.12f else 0.88f) + w * 0.06f * sin(f * 4f + k)
            drawCircle(RefugeC11.Smoke, w * (0.04f + 0.06f * f), Offset(x, ground - h * 0.3f - h * 0.4f * f), alpha = 0.5f * glow * (1f - f))
        }
    }
    // the houses, the one in the middle his
    for ((k, x) in listOf(0.1f, 0.25f, 0.5f, 0.75f, 0.9f).withIndex()) {
        val hw = if (k == 2) w * 0.22f else w * 0.13f
        val top = ground - h * (if (k == 2) 0.42f else 0.34f + 0.04f * (k % 2))
        drawRect(if (k % 2 == 0) RefugeC11.House else RefugeC11.HouseLight, topLeft = Offset(w * x - hw / 2, top), size = Size(hw, ground - top))
        if (k != 2) drawRect(RefugeC11.Window, topLeft = Offset(w * x - w * 0.015f, top + h * 0.06f), size = Size(w * 0.03f, h * 0.05f), alpha = 0.2f)
    }
    val hx = w * 0.5f
    val door = Offset(hx + w * 0.06f, ground)
    val shut = easeOut(seg(si, 0.55f, 0.75f))
    drawPath(horseshoe(door.x, ground - h * 0.12f, w * 0.025f, w * 0.02f, ground), if (shut > 0.5f) RefugeC11.Door else Color(0xFF8A6A40))
    // his window: warm; the shutters close over it; through the slats he sits within
    val win = Offset(hx - w * 0.04f, ground - h * 0.26f)
    val ww = w * 0.07f
    val wh = h * 0.1f
    softGlow(win, w * 0.08f, RefugeC11.Window, 0.6f)
    drawRect(RefugeC11.Window, topLeft = win - Offset(ww / 2, wh / 2), size = Size(ww, wh))
    if (st > 0f || su > 0.5f) {
        val seat = win + Offset(0f, wh * 0.42f)
        w8Seated(seat, wh * 0.85f, Color(0xFF3A2A1A), facing = 1f, alpha = seg(su, 0.4f, 0.8f))
    }
    val close = easeInOut(seg(su, 0.1f, 0.6f))
    if (close > 0f) for (sd in listOf(-1f, 1f)) {
        val hinge = win + Offset(sd * ww / 2, 0f)
        val sw = ww / 2 * close
        drawRect(RefugeC11.Shutter, topLeft = Offset(if (sd < 0f) hinge.x else hinge.x - sw, win.y - wh / 2), size = Size(sw, wh))
        for (r in 0 until 5) drawLine(Color(0xFFFFCF7A), Offset(if (sd < 0f) hinge.x else hinge.x - sw, win.y - wh / 2 + wh * (0.15f + 0.17f * r)), Offset(if (sd < 0f) hinge.x + sw else hinge.x, win.y - wh / 2 + wh * (0.15f + 0.17f * r)), 1f * dp, alpha = 0.5f)
    }
    drawRect(Color(0xFF14101C), topLeft = win - Offset(ww / 2, wh / 2), size = Size(ww, wh), style = Stroke(2f * dp))
    drawRect(RefugeC11.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))

    // the two crowds with their torches and banners
    val tall = h * 0.2f
    val gather = easeOut(seg(sf, 0.2f, 0.8f))
    val clash = easeInOut(seg(ml, 0f, 0.5f))
    for (side in listOf(-1f, 1f)) {
        val color = if (side < 0f) RefugeC11.BannerA else RefugeC11.BannerB
        for (k in 0 until 5) {
            val baseX = w * 0.5f + side * w * (0.42f - 0.035f * k)
            val x = baseX - side * w * 0.2f * clash
            val foot = Offset(x, ground + h * (0.06f + 0.035f * (k % 3)))
            val swing = if (ml > 0f) 0.5f + 0.5f * sin(t * 8f + k * 1.3f) else 0f
            withTransform({ scale(-side, 1f, pivot = foot) }) {
                figure(foot, tall, RefugeC11.Crowd, alpha = gather, walk = if (clash in 0.01f..0.99f) t * 8f + k else 0f, arms = 0.4f + 0.6f * swing)
            }
            // a torch for some, a stick for the others
            val hand = foot + Offset(-side * tall * 0.3f, -tall * 0.7f)
            if (k % 2 == 0) {
                drawLine(Color(0xFF6A4A2A), hand, hand + Offset(0f, -tall * 0.3f), 1.6f * dp, StrokeCap.Round, alpha = gather)
                softGlow(hand + Offset(0f, -tall * 0.33f), tall * 0.12f, RefugeC11.Torch, 0.8f * gather * (0.8f + 0.2f * sin(t * 9f + k)))
            } else if (ml > 0f) {
                drawLine(Color(0xFF6A4A2A), hand, hand + Offset(-side * tall * 0.3f * swing, -tall * 0.3f * (1f - swing)), 1.6f * dp, StrokeCap.Round)
            }
        }
        // the banner of the clan
        val rise = easeOut(seg(bn, 0f, 0.6f))
        if (rise > 0f) {
            val pole = Offset(w * 0.5f + side * w * (0.36f - 0.2f * clash), ground + h * 0.06f)
            drawLine(Color(0xFF6A4A2A), pole, pole + Offset(0f, -h * 0.4f * rise), 1.8f * dp)
            val flag = pole + Offset(0f, -h * 0.4f * rise)
            drawPath(Path().apply {
                moveTo(flag.x, flag.y)
                quadraticBezierTo(flag.x - side * w * 0.04f, flag.y + h * (0.02f + 0.01f * sin(t * 4f)), flag.x - side * w * 0.08f, flag.y + h * 0.01f)
                lineTo(flag.x - side * w * 0.08f, flag.y + h * 0.07f)
                quadraticBezierTo(flag.x - side * w * 0.04f, flag.y + h * 0.08f, flag.x, flag.y + h * 0.07f)
                close()
            }, color, alpha = rise)
        }
    }
    // sparks where they clash
    if (ml > 0.3f) for (k in 0 until 10) {
        val f = fract(t * 2f + k / 10f)
        val c = Offset(w * 0.5f, ground - h * 0.05f)
        val ang = k * 0.63f
        drawCircle(RefugeC11.Torch, 1.6f * dp, c + Offset(cos(ang) * w * 0.06f * f, sin(ang) * h * 0.06f * f - h * 0.05f * f), alpha = (1f - f) * seg(ml, 0.3f, 0.5f))
    }
    // the man in the street who turns and goes in
    if (si > 0f && si < 0.75f) {
        val go = easeInOut(seg(si, 0f, 0.55f))
        val foot = Offset(mix(w * 0.3f, door.x, go), ground + h * 0.03f - h * 0.02f * go)
        withTransform({ scale(-1f, 1f, pivot = foot) }) {
            figure(foot, tall * 1.05f, RefugeC11.Stranger, alpha = 1f - seg(si, 0.55f, 0.7f), walk = t * 6f)
        }
    }
}
