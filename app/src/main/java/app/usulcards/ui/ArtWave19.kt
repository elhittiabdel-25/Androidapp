package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/*
 * Wave 19, round 12 of the redraw (2026-10-21): «نواقض الإسلام». A mountain road that doubles back on itself, and
 * along it ten pits, one opening as each nullifier is named, the camera going from one to the next while a
 * traveller with a lit heart steps round each.
 */

private object PitC19 {
    val SkyTop = Color(0xFF8CBCE4)
    val SkyLow = Color(0xFFF0EEE0)
    val Mount = Color(0xFFB0A088)
    val MountDark = Color(0xFF8A7A64)
    val Road = Color(0xFFDCC8A0)
    val RoadEdge = Color(0xFF9A8460)
    val Pit = Color(0xFF1A1418)
    val PitRim = Color(0xFF5A4A3A)
    val Flag = Color(0xFFD23C30)
    val Pole = Color(0xFF5A4030)
    val Ink = Color(0xFF2A2430)
    val Heart = Color(0xFFFFD27A)
    val Back = Color(0xE6201810)
}

/** A point on the road, [s] 0..1: along the upper lane leftwards, round the bend, then along the lower lane rightwards. */
private fun DrawScope.road19(s: Float): Offset {
    val w = size.width
    val h = size.height
    return when {
        s < 0.45f -> Offset(mix(w * 0.95f, w * 0.14f, s / 0.45f), h * 0.46f)
        s < 0.55f -> {
            val th = -PI.toFloat() / 2f + PI.toFloat() * (s - 0.45f) / 0.1f
            Offset(w * 0.14f - h * 0.16f * cos(th), h * 0.62f + h * 0.16f * sin(th))
        }
        else -> Offset(mix(w * 0.14f, w * 0.95f, (s - 0.55f) / 0.45f), h * 0.78f)
    }
}

private fun pitAt19(k: Int): Float = if (k < 5) 0.06f + k * 0.085f else 0.6f + (k - 5) * 0.085f

/**
 * A mountainside by day with a road that doubles back. Beats: road10 (the road comes into view, a traveller with a
 * lit heart at its start, up on the right), pitfall ×10 (a pit opens in the road ahead of him, a red flag goes up
 * beside it with its name, and he steps carefully round it; the camera moves along from pit to pit), beware (the
 * whole road seen at once, ten red flags along it, and him at its end, his heart still lit).
 */
internal fun DrawScope.pitroadScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    var n = 0
    val shots = List(a.size) { i ->
        when (a.kindAt(i)) {
            "road10" -> Shot(Offset(w * 0.78f, h * 0.48f), 1.35f)
            "pitfall" -> {
                val p = road19(pitAt19(n++))
                Shot(Offset(p.x.coerceIn(w * 0.3f, w * 0.7f), p.y - h * 0.05f), 1.7f)
            }
            "beware" -> Shot(Offset(w * 0.5f, h * 0.55f), 1f)
            else -> null
        }
    }
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.55f), 1f), dim = 0.12f) { pitroadSide(a) }
}

private fun DrawScope.pitroadSide(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rd = a.b("road10")
    val bw = a.b("beware")
    drawRect(Brush.verticalGradient(listOf(PitC19.SkyTop, PitC19.SkyLow), endY = h * 0.4f), size = Size(w, h * 0.4f))
    drawPath(Path().apply {
        moveTo(0f, h * 0.36f); lineTo(w * 0.2f, h * 0.16f); lineTo(w * 0.42f, h * 0.3f); lineTo(w * 0.66f, h * 0.1f); lineTo(w, h * 0.3f); lineTo(w, h); lineTo(0f, h); close()
    }, Brush.verticalGradient(listOf(PitC19.Mount, PitC19.MountDark), startY = h * 0.1f, endY = h))
    // the road, doubling back
    val pts = List(61) { road19(it / 60f) }
    val show = easeOut(seg(rd, 0f, 0.3f))
    drawPath(w8Poly(pts), PitC19.RoadEdge.copy(alpha = show), style = Stroke(h * 0.085f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(w8Poly(pts), PitC19.Road.copy(alpha = show), style = Stroke(h * 0.07f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    for (y in listOf(h * 0.5f, h * 0.82f)) drawLine(PitC19.MountDark, Offset(w * 0.14f, y), Offset(w, y), 1.4f * dp, alpha = 0.6f * show)

    // the pits, opened one by one, each with its flag and (while it is told) its name
    val count = a.count("pitfall")
    var latest = -1
    for (k in 0 until count) if (a.b("pitfall", k) > 0f) latest = k
    for (k in 0 until count) {
        val p = a.b("pitfall", k)
        if (p <= 0f) continue
        val c = road19(pitAt19(k))
        val open = easeOut(seg(p, 0f, 0.3f))
        val rw = h * 0.075f * open
        val rh = h * 0.028f * open
        val jag = Path().apply {
            for (m in 0..12) {
                val ang = m * 2f * PI.toFloat() / 12f
                val r = 1f + 0.18f * (if (m % 2 == 0) 1f else -1f)
                val q = c + Offset(cos(ang) * rw * r, sin(ang) * rh * r)
                if (m == 0) moveTo(q.x, q.y) else lineTo(q.x, q.y)
            }
            close()
        }
        drawPath(jag, PitC19.PitRim)
        withTransform({ scale(0.82f, 0.75f, pivot = c) }) { drawPath(jag, PitC19.Pit) }
        softGlow(c + Offset(0f, rh * 0.2f), rw * 0.8f, Color(0xFFB03A2A), 0.35f * open)
        // its flag
        val up = easeOut(seg(p, 0.2f, 0.45f))
        val base = c + Offset(rw * 1.25f, 0f)
        drawLine(PitC19.Pole, base, base + Offset(0f, -h * 0.1f * up), 1.6f * dp, StrokeCap.Round)
        if (up > 0f) {
            val top = base + Offset(0f, -h * 0.1f * up)
            val flap = sin(t * 5f + k) * h * 0.006f
            drawPath(Path().apply { moveTo(top.x, top.y); lineTo(top.x + w * 0.035f, top.y + h * 0.014f + flap); lineTo(top.x, top.y + h * 0.03f); close() }, PitC19.Flag.copy(alpha = up))
        }
        val ta = seg(p, 0.25f, 0.5f) * (if (k == latest && bw <= 0f) 1f else 0f)
        tagAt(a.tag("pitfall", k), c + Offset(0f, -h * 0.11f), ta, back = PitC19.Back, ink = Color(0xFFF6B8A8))
    }

    // the traveller: walks to each pit as it opens and steps round it
    if (rd > 0f) {
        var s = 0.01f + 0.03f * easeOut(seg(rd, 0.3f, 1f))
        var hop = 0f
        if (latest >= 0) {
            val p = a.b("pitfall", latest)
            val sk = pitAt19(latest)
            val from = if (latest == 0) 0.04f else pitAt19(latest - 1) + 0.035f
            val reach = easeInOut(seg(p, 0f, 0.35f))
            val pass = easeInOut(seg(p, 0.4f, 0.9f))
            s = mix(from, sk - 0.03f, reach) + 0.065f * pass
            hop = bump(pass)
        }
        if (bw > 0f) s = mix(s, 0.985f, easeInOut(seg(bw, 0f, 0.6f)))
        val foot = road19(s) + Offset(0f, h * 0.012f - h * 0.06f * hop)
        val tall = h * 0.2f
        val right = s > 0.5f
        val moving = latest >= 0 && a.b("pitfall", latest) in 0.01f..0.95f || (bw in 0.01f..0.6f) || rd in 0.3f..0.99f
        withTransform({ if (right) scale(-1f, 1f, pivot = foot) }) {
            figure(foot, tall, PitC19.Ink, seg(rd, 0f, 0.2f), walk = if (moving) t * 6f else 0f, arms = if (hop > 0.1f) -0.6f else 0f)
        }
        val heart = foot + Offset(0f, -tall * 0.62f)
        softGlow(heart, tall * 0.22f, PitC19.Heart, 0.75f * seg(rd, 0f, 0.2f))
        drawPath(w8Heart(heart, tall * 0.06f), PitC19.Heart.copy(alpha = seg(rd, 0f, 0.2f)))
    }
}
