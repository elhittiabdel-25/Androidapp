package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 124 (2026-11-18): Zubdat al-Tafsir, the opening of al-Baqara: the thorny road of taqwa, the seal on hearts, the
 * parable of the fire, Adam the khalifa, fear and grief, «حطة». Prophets (Adam, Ya'qub) are named lights, angels cool
 * lights, Iblis never drawn (only dark smoke); people are faceless.
 */

private val Fig124 = Color(0xFFEDE4D0)
private val Dim124 = Color(0xFF8A8296)

private fun DrawScope.night124(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.day124(): Float {
    val g = size.height * 0.8f
    daySky12(Qn.DayTop, Qn.DayLow, g, Color(0xFFB09868))
    return g
}

private fun DrawScope.veil124(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row124(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

private fun DrawScope.thorns124(y: Float, x0: Float, x1: Float, alpha: Float) {
    val w = size.width
    var x = x0
    var k = 0
    while (x < x1) {
        val s = w * (0.018f + 0.008f * (k % 3))
        drawPath(Path().apply { moveTo(x, y); lineTo(x + s * 0.5f, y - s * 1.6f); lineTo(x + s, y); close() }, Color(0xFF5A4A30), alpha = alpha)
        x += w * 0.07f + w * 0.02f * (k % 2); k++
    }
}

private fun DrawScope.named124(c: Offset, r: Float, a: ArtCtx, kind: String, part: Int, alpha: Float) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 3f, Qn.GoldLight, 0.7f * alpha)
    drawCircle(Color(0xFFF8F4E8), r, c, alpha = alpha)
    tagAt(a.tag(kind, 0, part), Offset(c.x, c.y - r * 3.2f), alpha, ink = ArtInk.Ivory)
}

// ═════════════ zb-taqwa ═════════════
internal fun DrawScope.zbtaqwaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "zbtaqwa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day124() },
        { p, f ->
            veil124(f)
            val c = Offset(w * 0.5f, h * 0.45f)
            softGlow(c, w * 0.22f, Qn.GoldLight, 0.6f * f * seg(p, 0.1f, 0.4f))
            qIcon("book", c, w * 0.18f, Qn.Gold, f, a.t)
            tagAt(a.tag("zbtaqwa0", 0, 0), Offset(w * 0.5f, h * 0.74f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil124(f)
            qIcon("lock", Offset(w * 0.7f, h * 0.42f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            qIcon("heart", Offset(w * 0.3f, h * 0.42f), w * 0.1f, Qn.Rose, f * seg(p, 0.5f, 0.65f), a.t)
            row124(a, "zbtaqwa1", 2, h * 0.62f, p, f, from = 0.2f, step = 0.35f)
        },
        { p, f ->
            val g = h * 0.8f
            drawRect(Color(0xFFC8B07A), Offset(0f, g - h * 0.02f), Size(w, h * 0.05f), alpha = f)
            thorns124(g - h * 0.01f, w * 0.05f, w * 0.95f, f * seg(p, 0.1f, 0.3f))
            person99(Offset(w * 0.88f, g - h * 0.02f), h * 0.12f, Fig124, f)
            tagAt(a.tag("zbtaqwa2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            drawRect(Color(0xFFC8B07A), Offset(0f, g - h * 0.02f), Size(w, h * 0.05f), alpha = f)
            thorns124(g - h * 0.01f, w * 0.05f, w * 0.95f, f)
            val go = easeInOut(seg(p, 0.05f, 0.9f))
            val x = mix(w * 0.9f, w * 0.1f, go)
            val hop = h * 0.04f * kotlin.math.abs(sin(go * 18f))
            person99(Offset(x, g - h * 0.02f - hop), h * 0.12f, Fig124, f, walk = a.t * 2f)
            row124(a, "zbtaqwa3", 3, h * 0.24f, p, f, from = 0.1f, step = 0.25f, ink = ArtInk.Gold)
        },
    )
}

// ═════════════ zb-khatm ═════════════
internal fun DrawScope.zbkhatmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "zbkhatm", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night124(a) },
        { p, f ->
            val cols = listOf(Fig124, Color(0xFF3A3040), Dim124)
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                for (k in 0 until 2) person99(Offset(x + (k - 0.5f) * w * 0.07f, g), h * 0.11f, cols[i], f * seg(p, 0.05f + 0.2f * i, 0.2f + 0.2f * i))
                if (i == 0) softGlow(Offset(x, g - h * 0.08f), w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.05f, 0.2f))
            }
            row124(a, "zbkhatm0", 3, h * 0.3f, p, f, from = 0.1f, step = 0.2f)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.75f, h * 0.48f), w * 0.03f, f, a.t, named = false)
            person99(Offset(w * 0.3f, g), h * 0.13f, Color(0xFF3A3040), f)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.6f + k / 3f) % 1f)
                val x = mix(w * 0.68f, w * 0.36f, ph)
                drawArc(Qn.GoldLight, 140f, 80f, false, Offset(x - w * 0.04f, h * 0.48f - w * 0.04f), Size(w * 0.08f, w * 0.08f), alpha = f * (1f - ph) * seg(p, 0.1f, 0.25f), style = Stroke(w * 0.006f))
            }
            tagAt(a.tag("zbkhatm1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil124(f)
            val c = Offset(w * 0.5f, h * 0.46f)
            qIcon("heart", c, w * 0.18f, Qn.Rose, f, a.t)
            for (k in 0 until 4) drawCircle(Color(0xFF1A1418), w * (0.06f + 0.015f * k), c, alpha = 0.25f * f * seg(p, 0.15f + 0.15f * k, 0.3f + 0.15f * k))
            tagAt(a.tag("zbkhatm2", 0, 0), Offset(w * 0.5f, h * 0.74f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil124(f)
            qIcon("eye", Offset(w * 0.5f, h * 0.45f), w * 0.16f, Fig124, f, a.t)
            drawRoundRect(Color(0xFF241C2C), Offset(w * 0.36f, h * 0.45f - h * 0.05f * seg(p, 0.2f, 0.5f)), Size(w * 0.28f, h * 0.1f * seg(p, 0.2f, 0.5f)), CornerRadius(w * 0.02f), alpha = 0.9f * f)
            tagAt(a.tag("zbkhatm3", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ zb-nar ═════════════
internal fun DrawScope.zbnarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "zbnar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night124(a) },
        { p, f ->
            val inP = easeInOut(seg(p, 0.05f, 0.5f))
            softGlow(Offset(w * 0.75f, g - h * 0.1f), w * 0.2f, Qn.GoldLight, 0.5f * f)
            for (k in 0 until 4) person99(Offset(mix(w * 0.1f, w * 0.62f, inP) + k * w * 0.05f, g), h * 0.1f, if (k < 2) Fig124 else Dim124, f, walk = a.t * 2f * (1f - inP))
            val back = easeInOut(seg(p, 0.55f, 0.85f))
            for (k in 2 until 4) person99(Offset(mix(w * 0.72f, w * 0.3f, back) + k * w * 0.05f, g), h * 0.1f, Color(0xFF4A4458), f * back)
            tagAt(a.tag("zbnar0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF05040A), alpha = 0.6f * f)
            val lit = seg(p, 0.15f, 0.35f)
            softGlow(Offset(w * 0.5f, g - h * 0.04f), w * 0.4f * lit, Qn.Ember, 0.6f * f * lit)
            qIcon("flame", Offset(w * 0.5f, g - h * 0.05f), w * 0.09f, Qn.Ember, f * lit, a.t)
            person99(Offset(w * 0.62f, g), h * 0.13f, Fig124, f)
            for (k in 0 until 3) drawCircle(Color(0xFF6B5A48), w * 0.018f, Offset(w * (0.25f + 0.08f * k), g - h * 0.01f), alpha = f * lit)
            tagAt(a.tag("zbnar1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF05040A), alpha = 0.6f * f)
            val out = seg(p, 0.15f, 0.4f)
            softGlow(Offset(w * 0.5f, g - h * 0.04f), w * 0.4f * (1f - out), Qn.Ember, 0.6f * f * (1f - out))
            qIcon("flame", Offset(w * 0.5f, g - h * 0.05f), w * 0.09f, Qn.Ember, f * (1f - out), a.t)
            drawCircle(Qn.Smoke, w * 0.04f * out, Offset(w * 0.5f, g - h * 0.12f), alpha = 0.6f * f * out)
            person99(Offset(w * 0.62f + sin(a.t * 3f) * w * 0.02f * out, g), h * 0.13f, Dim124, f, lean = 0.15f * sin(a.t * 2f) * out)
            tagAt(a.tag("zbnar2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil124(f)
            val icons = listOf("ear", "lips", "eye")
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                qIcon(icons[i], Offset(x, h * 0.42f), w * 0.1f, Dim124, f * seg(p, 0.08f + 0.22f * i, 0.2f + 0.22f * i), a.t)
                qIcon("cross", Offset(x, h * 0.42f), w * 0.06f, Qn.Rose, f * seg(p, 0.15f + 0.22f * i, 0.27f + 0.22f * i), a.t)
            }
            row124(a, "zbnar3", 3, h * 0.62f, p, f, from = 0.1f, step = 0.22f)
        },
    )
}

// ═════════════ zb-khalifa ═════════════
internal fun DrawScope.zbkhalifaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "zbkhalifa", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night124(a) },
        { p, f ->
            named124(Offset(w * 0.5f, g - h * 0.12f), w * 0.03f, a, "zbkhalifa0", 1, f * seg(p, 0.2f, 0.45f))
            tagAt(a.tag("zbkhalifa0", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 5) {
                val ang = 3.1416f + (k + 0.5f) / 5f * 3.1416f
                angel98(Offset(w * 0.5f + w * 0.34f * cos(ang), h * 0.5f + h * 0.25f * sin(ang)), w * 0.02f, f * seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k))
            }
            tagAt(a.tag("zbkhalifa1", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil124(f)
            for (k in 0 until 3) angel98(Offset(w * (0.3f + 0.2f * k), h * 0.4f), w * 0.02f, f)
            qIcon("lock", Offset(w * 0.5f, h * 0.58f), w * 0.08f, Dim124, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("zbkhalifa2", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            named124(Offset(w * 0.5f, g - h * 0.1f), w * 0.028f, a, "zbkhalifa3", 1, f)
            val bow = easeInOut(seg(p, 0.1f, 0.4f))
            for (k in 0 until 4) angel98(Offset(w * (0.2f + 0.2f * k), mix(h * 0.3f, h * 0.45f, bow)), w * 0.018f, f)
            for (k in 0 until 3) drawCircle(Qn.Smoke, w * (0.04f + 0.01f * k), Offset(w * 0.88f, h * (0.3f - 0.04f * k)), alpha = 0.7f * f * seg(p, 0.45f, 0.65f))
            tagAt(a.tag("zbkhalifa3", 0, 0), Offset(w * 0.72f, h * 0.16f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ zb-khawf ═════════════
internal fun DrawScope.zbkhawfScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "zbkhawf", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night124(a) },
        { p, f ->
            val go = easeInOut(seg(p, 0.05f, 0.8f))
            drawLine(Qn.GoldLight, Offset(w * 0.95f, g + h * 0.02f), Offset(w * 0.05f, g + h * 0.02f), w * 0.008f, alpha = 0.6f * f)
            person99(Offset(mix(w * 0.9f, w * 0.3f, go), g), h * 0.12f, Fig124, f, walk = a.t * 2f)
            softGlow(Offset(mix(w * 0.9f, w * 0.3f, go), g - h * 0.08f), w * 0.12f, Qn.GoldLight, 0.5f * f)
            row124(a, "zbkhawf0", 2, h * 0.24f, p, f, from = 0.3f, step = 0.3f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil124(f)
            drawLine(Qn.GoldLight, Offset(w * 0.9f, h * 0.5f), Offset(w * 0.1f, h * 0.5f), w * 0.006f, alpha = f, cap = StrokeCap.Round)
            drawCircle(Qn.Gold, w * 0.02f, Offset(w * 0.6f, h * 0.5f), alpha = f)
            drawPath(Path().apply { moveTo(w * 0.1f, h * 0.5f); lineTo(w * 0.14f, h * 0.47f); lineTo(w * 0.14f, h * 0.53f); close() }, Qn.GoldLight, alpha = f)
            tagAt(a.tag("zbkhawf1", 0, 0), Offset(w * 0.28f, h * 0.36f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("zbkhawf1", 0, 1), Offset(w * 0.6f, h * 0.64f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil124(f)
            drawRect(Qn.Ember, Offset(0f, h * 0.7f), Size(w, h * 0.12f), alpha = 0.5f * f * seg(p, 0.1f, 0.3f))
            for (k in 0 until 4) person99(Offset(w * (0.2f + 0.2f * k), h * 0.7f), h * 0.1f, Color(0xFF4A4458), f)
            tagAt(a.tag("zbkhawf2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            named124(Offset(w * 0.5f, h * 0.5f), w * 0.03f, a, "zbkhawf3", 1, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("zbkhawf3", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ zb-hitta ═════════════
internal fun DrawScope.zbhittaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "zbhitta", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day124() },
        { p, f ->
            val g = h * 0.8f
            drawRect(Color(0xFFCDB58A), Offset(w * 0.55f, g - h * 0.4f), Size(w * 0.45f, h * 0.4f), alpha = f)
            door12(Offset(w * 0.7f, g), w * 0.14f, h * 0.24f, 1f, Color(0xFF8A6A40), Color(0xFF6B4F35), f)
            for (k in 0 until 4) person99(Offset(w * (0.5f - 0.1f * k), g), h * 0.1f, Fig124, f, lean = 0.35f * seg(p, 0.2f, 0.4f), walk = a.t * 2f)
            tagAt(a.tag("zbhitta0", 0, 0), Offset(w * 0.3f, h * 0.36f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil124(f)
            tagAt(a.tag("zbhitta1", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("zbhitta1", 0, 1), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            drawRect(Color(0xFFCDB58A), Offset(w * 0.55f, g - h * 0.4f), Size(w * 0.45f, h * 0.4f), alpha = f)
            door12(Offset(w * 0.7f, g), w * 0.14f, h * 0.24f, 1f, Color(0xFF8A6A40), Color(0xFF6B4F35), f)
            val go = easeInOut(seg(p, 0.15f, 0.8f))
            for (k in 0 until 4) drawRoundRect(Color(0xFF8A7A6A), Offset(mix(w * 0.15f, w * 0.55f, go) - k * w * 0.1f, g - h * 0.04f), Size(w * 0.07f, h * 0.04f), CornerRadius(w * 0.02f), alpha = f)
            tagAt(a.tag("zbhitta2", 0, 0), Offset(w * 0.3f, h * 0.36f), f * seg(p, 0.35f, 0.5f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            for (k in 0 until 30) {
                val ph = ((a.t * 0.4f + k * 0.13f) % 1f)
                drawCircle(Color(0xFFFFFFFF), w * 0.006f, Offset(w * (0.05f + 0.031f * k), mix(h * 0.1f, g, ph)), alpha = 0.8f * f * (1f - ph) * seg(p, 0.1f, 0.3f))
            }
            for (k in 0 until 4) person99(Offset(w * (0.2f + 0.2f * k), g), h * 0.1f, Fig124, f, arms = 0.6f)
            tagAt(a.tag("zbhitta3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}
