package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.sin

/*
 * Wave 120 (2026-11-18): Sharh 'Umdat al-Ahkam, purification: hijra, «لا يقبل الله صلاة أحدكم إذا أحدث», the heels,
 * the nose and the hands on waking, still water, the vessel washed seven times, and 'Uthman's wudu. People are
 * faceless; 'Uthman is his roundel; the Prophet ﷺ only his named light; no animal is drawn (only the vessel).
 */

private val Fig120 = Color(0xFFEDE4D0)
private val Dim120 = Color(0xFF8A8296)
private val Water120 = Color(0xFF6FB4E0)
private val Soil120 = Color(0xFF8A6A40)

private fun DrawScope.day120(): Float {
    val g = size.height * 0.8f
    daySky12(Qn.DayTop, Qn.DayLow, g, Color(0xFFB09868))
    return g
}

private fun DrawScope.night120(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil120(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row120(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

/** A bowl seen from the side, with water at `level` (0..1). */
private fun DrawScope.bowl120(c: Offset, s: Float, level: Float, alpha: Float, water: Color = Water120) {
    if (alpha <= 0.01f) return
    drawArc(Color(0xFFB89E72), 0f, 180f, true, Offset(c.x - s / 2, c.y - s * 0.4f), Size(s, s * 0.8f), alpha = alpha)
    if (level > 0f) drawOval(water, Offset(c.x - s * 0.45f, c.y - s * 0.06f), Size(s * 0.9f, s * 0.12f), alpha = alpha * level)
    drawLine(Color(0xFF8A6A40), Offset(c.x - s / 2, c.y), Offset(c.x + s / 2, c.y), s * 0.03f, alpha = alpha)
}

/** A bare foot seen from the side (toes to the right), heel wet by `wet`. */
private fun DrawScope.foot120(c: Offset, s: Float, wet: Float, alpha: Float, ankles: Float = 0f) {
    if (alpha <= 0.01f) return
    val path = Path().apply {
        moveTo(c.x - s * 0.45f, c.y)
        lineTo(c.x + s * 0.5f, c.y)
        quadraticBezierTo(c.x + s * 0.55f, c.y - s * 0.12f, c.x + s * 0.3f, c.y - s * 0.16f)
        lineTo(c.x - s * 0.2f, c.y - s * 0.3f)
        lineTo(c.x - s * 0.25f, c.y - s * 0.65f)
        lineTo(c.x - s * 0.42f, c.y - s * 0.65f)
        lineTo(c.x - s * 0.42f, c.y - s * 0.22f)
        quadraticBezierTo(c.x - s * 0.52f, c.y - s * 0.12f, c.x - s * 0.45f, c.y)
        close()
    }
    drawPath(path, Color(0xFFE8D2B4), alpha = alpha)
    drawPath(path, Water120, alpha = 0.55f * alpha * wet)
    drawCircle(Color(0xFF6B4F35), s * 0.06f, Offset(c.x - s * 0.44f, c.y - s * 0.08f), alpha = alpha * (1f - wet))
    if (ankles > 0f) drawCircle(Qn.Gold, s * 0.05f, Offset(c.x - s * 0.33f, c.y - s * 0.3f), alpha = alpha * ankles)
}

// ═════════════ ua-hijra ═════════════
internal fun DrawScope.uahijraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "uahijra", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night120(a) },
        { p, f ->
            val g = h * 0.8f
            drawRect(Color(0xFF241C2C), Offset(0f, 0f), Size(w * 0.45f, g), alpha = 0.5f * f)
            softGlow(Offset(w * 0.78f, h * 0.45f), w * 0.3f, Qn.GoldLight, 0.4f * f)
            val go = easeInOut(seg(p, 0.15f, 0.75f))
            person99(Offset(mix(w * 0.2f, w * 0.75f, go), g), h * 0.13f, Fig120, f, walk = a.t * 2f * (1f - go))
            tagAt(a.tag("uahijra0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            val g = h * 0.8f
            for (k in 0 until 6) drawLine(Color(0xFF9AA0AA), Offset(w * 0.5f, g - h * 0.05f * k - h * 0.02f), Offset(w * 0.62f, g - h * 0.05f * k - h * 0.02f), w * 0.01f, alpha = f * seg(p, 0.1f, 0.3f))
            for (k in 0 until 2) drawLine(Color(0xFF9AA0AA), Offset(w * (0.5f + 0.12f * k), g), Offset(w * (0.5f + 0.12f * k), g - h * 0.3f), w * 0.012f, alpha = f * seg(p, 0.1f, 0.3f))
            person99(Offset(w * 0.36f, g), h * 0.13f, Fig120, f, arms = 0.4f)
            softGlow(Offset(w * 0.36f, g - h * 0.1f), w * 0.1f, Qn.Jade, 0.5f * f * seg(p, 0.5f, 0.75f))
            tagAt(a.tag("uahijra1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            for (k in 0 until 3) person99(Offset(w * (0.08f + 0.08f * k), g), h * 0.11f, Color(0xFF4A4458), f)
            for (k in 0 until 3) person99(Offset(w * (0.72f + 0.08f * k), g), h * 0.11f, Fig120, f)
            softGlow(Offset(w * 0.8f, g - h * 0.1f), w * 0.18f, Qn.GoldLight, 0.4f * f)
            val go = easeInOut(seg(p, 0.15f, 0.7f))
            person99(Offset(mix(w * 0.32f, w * 0.64f, go), g), h * 0.12f, Fig120, f, walk = a.t * 2f * (1f - go))
            tagAt(a.tag("uahijra2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil120(f)
            val up = easeInOut(seg(p, 0.3f, 0.8f))
            drawCircle(Qn.Ember, w * 0.08f, Offset(w * 0.18f, mix(h * 0.86f, h * 0.55f, up)), alpha = f)
            tagAt(a.tag("uahijra3", 0, 0), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ua-qabul ═════════════
internal fun DrawScope.uaqabulScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "uaqabul", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night120(a) },
        { p, f ->
            val g = h * 0.8f
            qIcon("mat", Offset(w * 0.5f, g + h * 0.02f), w * 0.22f, Qn.Lapis, f, a.t)
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig120, f)
            qIcon("cross", Offset(w * 0.72f, h * 0.42f), w * 0.06f, Qn.Rose, f * seg(p, 0.1f, 0.2f) * (1f - seg(p, 0.5f, 0.6f)), a.t)
            qIcon("drop", Offset(w * 0.72f, h * 0.42f), w * 0.06f, Water120, f * seg(p, 0.55f, 0.7f), a.t)
            softGlow(Offset(w * 0.5f, g - h * 0.1f), w * 0.15f, Qn.GoldLight, 0.5f * f * seg(p, 0.6f, 0.85f))
            tagAt(a.tag("uaqabul0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil120(f)
            qIcon("cross", Offset(w * 0.7f, h * 0.42f), w * 0.1f, Qn.Rose, f * seg(p, 0.15f, 0.3f), a.t)
            qIcon("scale", Offset(w * 0.3f, h * 0.42f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            row120(a, "uaqabul1", 2, h * 0.6f, p, f, from = 0.15f, step = 0.35f)
        },
        { p, f ->
            veil120(f)
            val c = Offset(w * 0.4f, h * 0.45f)
            drawCircle(Qn.GoldLight, w * 0.2f, c, alpha = f, style = Stroke(w * 0.01f))
            qIcon("key", c, w * 0.08f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("lock", Offset(w * 0.82f, h * 0.45f), w * 0.07f, Dim120, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("uaqabul2", 0, 0), Offset(w * 0.4f, h * 0.78f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("uaqabul2", 0, 1), Offset(w * 0.82f, h * 0.62f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil120(f)
            qIcon("drop", Offset(w * 0.5f, h * 0.3f), w * 0.08f, Water120, f, a.t)
            for (k in 0 until 5) {
                val c = Offset(w * (0.82f - 0.16f * k), h * 0.56f)
                drawCircle(Qn.Gold, w * 0.028f, c, alpha = f * seg(p, 0.1f + 0.12f * k, 0.2f + 0.12f * k))
                softGlow(c, w * 0.05f, Qn.GoldLight, 0.5f * f * seg(p, 0.1f + 0.12f * k, 0.2f + 0.12f * k))
            }
            row120(a, "uaqabul3", 2, h * 0.74f, p, f, from = 0.3f, step = 0.3f)
        },
    )
}

// ═════════════ ua-aqab ═════════════
internal fun DrawScope.uaaqabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "uaaqab", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day120() },
        { p, f ->
            val g = h * 0.8f
            drawCircle(Qn.Ember, w * 0.06f, Offset(w * 0.15f, h * 0.62f), alpha = f)
            for (k in 0 until 3) person99(Offset(w * (0.45f + 0.15f * k), g), h * 0.12f, Fig120, f, lean = 0.2f, arms = 0.6f)
            for (k in 0 until 3) qIcon("drop", Offset(w * (0.47f + 0.15f * k), g - h * 0.03f), w * 0.02f, Water120, f * 0.6f, a.t)
            tagAt(a.tag("uaaqab0", 0, 0), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil120(f)
            foot120(Offset(w * 0.52f, h * 0.62f), w * 0.5f, easeInOut(seg(p, 0.5f, 0.8f)), f)
            sirajMunir(Offset(w * 0.5f, h * 0.2f), w * 0.03f, f * seg(p, 0.05f, 0.2f), a.t, named = false)
            tagAt(a.tag("uaaqab1", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil120(f)
            foot120(Offset(w * 0.52f, h * 0.62f), w * 0.5f, 1f, f)
            val fill = easeInOut(seg(p, 0.15f, 0.6f))
            for (k in 0 until 8) qIcon("drop", Offset(w * (0.3f + 0.06f * k), mix(h * 0.2f, h * 0.5f, fill) + h * 0.02f * (k % 2)), w * 0.02f, Water120, f * (1f - fill), a.t)
            tagAt(a.tag("uaaqab2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil120(f)
            foot120(Offset(w * 0.52f, h * 0.62f), w * 0.5f, 1f, f, ankles = seg(p, 0.2f, 0.4f))
            tagAt(a.tag("uaaqab3", 0, 0), Offset(w * 0.4f, h * 0.24f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ua-istinshaq ═════════════
internal fun DrawScope.uaistinshaqScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "uaistinshaq", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night120(a) },
        { p, f ->
            veil120(f)
            val inP = seg(p, 0.1f, 0.4f)
            val outP = seg(p, 0.45f, 0.75f)
            qIcon("drop", Offset(w * 0.7f, mix(h * 0.65f, h * 0.4f, inP)), w * 0.05f, Water120, f * (1f - outP), a.t)
            for (k in 0 until 4) drawCircle(Water120, w * 0.012f, Offset(mix(w * 0.6f, w * 0.25f, outP) + k * w * 0.03f, h * 0.42f + h * 0.03f * sin(k + a.t * 3f)), alpha = f * outP * (1f - seg(p, 0.85f, 0.95f)))
            row120(a, "uaistinshaq0", 2, h * 0.2f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            veil120(f)
            for (s in 0 until 2) {
                val y = h * (0.4f + 0.18f * s)
                val q = seg(p, 0.1f + 0.4f * s, 0.35f + 0.4f * s)
                val x0 = if (s == 0) w * 0.2f else w * 0.8f
                val x1 = if (s == 0) w * 0.8f else w * 0.2f
                drawLine(Water120, Offset(x0, y), Offset(mix(x0, x1, q), y), w * 0.012f, alpha = f, cap = StrokeCap.Round)
            }
            tagAt(a.tag("uaistinshaq1", 0, 0), Offset(w * 0.5f, h * 0.32f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("uaistinshaq1", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            bowl120(Offset(w * 0.3f, h * 0.62f), w * 0.3f, 1f, f)
            qIcon("hands", Offset(w * 0.68f, h * 0.48f), w * 0.14f, Fig120, f, a.t)
            for (k in 0 until 3) {
                val q = seg(p, 0.15f + 0.15f * k, 0.28f + 0.15f * k)
                qIcon("drop", Offset(w * 0.68f, h * 0.3f + h * 0.12f * q), w * 0.025f, Water120, f * q * (1f - q), a.t)
                drawCircle(Qn.Gold, w * 0.02f, Offset(w * (0.74f - 0.06f * k), h * 0.7f), alpha = f * seg(p, 0.25f + 0.15f * k, 0.32f + 0.15f * k))
            }
            tagAt(a.tag("uaistinshaq2", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil120(f)
            qIcon("moon", Offset(w * 0.75f, h * 0.2f), w * 0.08f, Qn.GoldLight, f, a.t)
            qIcon("hands", Offset(w * 0.5f, h * 0.48f), w * 0.14f, Fig120, f, a.t)
            drawCircle(Qn.Smoke, w * 0.14f, Offset(w * 0.5f, h * 0.48f), alpha = 0.4f * f * seg(p, 0.2f, 0.45f))
            tagAt(a.tag("uaistinshaq3", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ua-daim ═════════════
private fun DrawScope.pond120(c: Offset, rx: Float, ry: Float, murk: Float, alpha: Float, flow: Float = 0f, t: Float = 0f) {
    drawOval(lerp120(Water120, Color(0xFF7A7A4A), murk), Offset(c.x - rx, c.y - ry), Size(2 * rx, 2 * ry), alpha = alpha)
    if (flow > 0f) for (k in 0 until 4) {
        val ph = ((t * 0.6f + k / 4f) % 1f)
        drawLine(Color(0xFFDCEEFA), Offset(c.x - rx + 2 * rx * ph, c.y - ry * 0.3f + ry * 0.2f * k), Offset(c.x - rx + 2 * rx * ph + rx * 0.2f, c.y - ry * 0.3f + ry * 0.2f * k), ry * 0.08f, alpha = alpha * flow)
    }
}

private fun lerp120(a: Color, b: Color, t: Float) = Color(a.red + (b.red - a.red) * t, a.green + (b.green - a.green) * t, a.blue + (b.blue - a.blue) * t, 1f)

internal fun DrawScope.uadaimScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "uadaim", List(4) { Shot(Offset(w * 0.5f, h * 0.6f), 1f) },
        { day120() },
        { p, f ->
            pond120(Offset(w * 0.5f, h * 0.84f), w * 0.36f, h * 0.06f, 0f, f)
            qIcon("cross", Offset(w * 0.5f, h * 0.6f), w * 0.08f, Qn.Rose, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("uadaim0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            pond120(Offset(w * 0.72f, h * 0.84f), w * 0.2f, h * 0.05f, 0f, f)
            pond120(Offset(w * 0.28f, h * 0.84f), w * 0.22f, h * 0.05f, 0f, f, flow = 1f, t = a.t)
            tagAt(a.tag("uadaim1", 0, 0), Offset(w * 0.72f, h * 0.6f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("uadaim1", 0, 1), Offset(w * 0.28f, h * 0.6f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            pond120(Offset(w * 0.75f, h * 0.84f), w * 0.12f, h * 0.04f, seg(p, 0.15f, 0.4f), f)
            pond120(Offset(w * 0.32f, h * 0.84f), w * 0.26f, h * 0.06f, 0.3f * seg(p, 0.5f, 0.75f), f)
            row120(a, "uadaim2", 2, h * 0.6f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            val g = h * 0.8f
            pond120(Offset(w * 0.35f, h * 0.86f), w * 0.3f, h * 0.05f, 0f, f)
            person99(Offset(w * 0.72f, g), h * 0.13f, Fig120, f, lean = 0.25f * seg(p, 0.1f, 0.3f), arms = 0.6f)
            qIcon("cup", Offset(mix(w * 0.6f, w * 0.68f, seg(p, 0.4f, 0.7f)), g - h * 0.05f), w * 0.04f, Qn.Gold, f, a.t)
            tagAt(a.tag("uadaim3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ua-kalb ═════════════
internal fun DrawScope.uakalbScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "uakalb", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night120(a) },
        { p, f ->
            bowl120(Offset(w * 0.5f, h * 0.62f), w * 0.34f, 1f, f)
            for (k in 0 until 7) {
                val c = Offset(w * (0.82f - 0.107f * k), h * 0.3f)
                drawCircle(if (k == 0) Soil120 else Water120, w * 0.022f, c, alpha = f * seg(p, 0.1f + 0.08f * k, 0.18f + 0.08f * k))
            }
            row120(a, "uakalb0", 2, h * 0.84f, p, f, from = 0.3f, step = 0.35f)
        },
        { p, f ->
            bowl120(Offset(w * 0.5f, h * 0.62f), w * 0.34f, 1f, f)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.7f + k / 3f) % 1f)
                drawOval(Color(0xFFDCEEFA), Offset(w * 0.5f - w * 0.15f * ph, h * 0.62f - h * 0.02f - h * 0.02f * ph), Size(w * 0.3f * ph, h * 0.04f * ph),
                    alpha = f * (1f - ph) * seg(p, 0.1f, 0.25f), style = Stroke(w * 0.004f))
            }
            tagAt(a.tag("uakalb1", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil120(f)
            for (k in 0 until 7) {
                val q = seg(p, 0.08f + 0.09f * k, 0.16f + 0.09f * k)
                drawRoundRect(if (k == 0) Soil120 else Water120, Offset(w * (0.8f - 0.1f * k) - w * 0.03f, h * 0.62f - h * 0.2f * q), Size(w * 0.06f, h * 0.2f * q), CornerRadius(w * 0.01f), alpha = f)
            }
            tagAt(a.tag("uakalb2", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil120(f)
            val c = Offset(w * 0.5f, h * 0.48f)
            drawCircle(Color(0xFFDCEEFA), w * 0.2f, c, alpha = 0.15f * f)
            val clean = easeInOut(seg(p, 0.35f, 0.75f))
            for (k in 0 until 14) {
                val ang = k * 2.2f
                val d = w * (0.05f + 0.01f * (k % 9))
                drawCircle(Qn.Jade, w * 0.012f, Offset(c.x + d * kotlin.math.cos(ang), c.y + d * sin(ang)), alpha = f * (1f - clean))
            }
            for (k in 0 until 10) drawCircle(Soil120, w * 0.008f, Offset(c.x - w * 0.15f + w * 0.03f * k, mix(h * 0.2f, c.y + h * 0.05f * ((k % 3) - 1), seg(p, 0.15f, 0.45f))), alpha = f * seg(p, 0.15f, 0.25f))
            tagAt(a.tag("uakalb3", 0, 0), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ua-uthman ═════════════
internal fun DrawScope.uauthmanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "uauthman", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night120(a) },
        { p, f ->
            close97(Offset(w * 0.72f, h * 0.36f), w * 0.1f, a, "uauthman0", 0, 2, f * seg(p, 0.05f, 0.25f))
            bowl120(Offset(w * 0.3f, h * 0.66f), w * 0.28f, 1f, f)
            qIcon("hands", Offset(w * 0.3f, h * 0.44f), w * 0.12f, Fig120, f * seg(p, 0.2f, 0.35f), a.t)
            for (k in 0 until 3) drawCircle(Qn.Gold, w * 0.02f, Offset(w * (0.38f - 0.08f * k), h * 0.82f), alpha = f * seg(p, 0.35f + 0.12f * k, 0.45f + 0.12f * k))
            tagAt(a.tag("uauthman0", 0, 1), Offset(w * 0.72f, h * 0.62f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil120(f)
            for (i in 0 until 4) {
                val y = h * (0.22f + 0.15f * i)
                val q = seg(p, 0.05f + 0.22f * i, 0.2f + 0.22f * i)
                tagAt(a.tag("uauthman1", 0, i), Offset(w * 0.68f, y), f * q, ink = ArtInk.Ivory)
                val n = if (i == 2) 1 else 3
                for (k in 0 until n) drawCircle(Water120, w * 0.018f, Offset(w * (0.38f - 0.07f * k), y), alpha = f * q)
            }
        },
        { p, f ->
            veil120(f)
            sirajMunir(Offset(w * 0.5f, h * 0.4f), w * 0.04f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.24f))
            close97(Offset(w * 0.8f, h * 0.66f), w * 0.07f, a, "uauthman2", 1, 2, f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("uauthman2", 0, 0), Offset(w * 0.4f, h * 0.7f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            val g = h * 0.8f
            qIcon("mat", Offset(w * 0.5f, g + h * 0.02f), w * 0.22f, Qn.Lapis, f, a.t)
            val bow = seg(p, 0.1f, 0.3f) * (1f - seg(p, 0.35f, 0.5f))
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig120, f, lean = 0.4f * bow)
            for (k in 0 until 2) drawCircle(Qn.Gold, w * 0.02f, Offset(w * (0.56f - 0.12f * k), h * 0.3f), alpha = f * seg(p, 0.2f + 0.25f * k, 0.3f + 0.25f * k))
            softGlow(Offset(w * 0.5f, g - h * 0.1f), w * 0.25f, Qn.GoldLight, 0.5f * f * seg(p, 0.6f, 0.85f))
            tagAt(a.tag("uauthman3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}
