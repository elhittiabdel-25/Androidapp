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
 * Wave 117 (2026-11-18): Ibn al-Qayyim's al-Wabil al-Sayyib: the three marks of happiness, the sin that leads to
 * Paradise, the two wings, and the hadith of al-Harith al-Ash'ari (the five words given to Yahya). Yahya is only a
 * named light; people are faceless; the devil is never drawn (only dark smoke outside the fortress).
 */

private val Fig117 = Color(0xFFEDE4D0)
private val Dim117 = Color(0xFF8A8296)
private val Foe117 = Color(0xFF3A3040)
private val Stone117 = Color(0xFFB8A27A)

private fun DrawScope.night117(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil117(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row117(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

/** Icons in a row, right to left, matching row117. */
private fun DrawScope.icons117(names: List<String>, colors: List<Color>, y: Float, s: Float, p: Float, f: Float, t: Float, from: Float = 0.1f, step: Float = 0.15f) {
    val n = names.size
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        qIcon(names[i], Offset(x, y), s, colors[i], f * seg(p, from + step * i, from + step * i + 0.12f), t)
    }
}

private fun DrawScope.fort117(foot: Offset, s: Float, alpha: Float, gate: Float, glow: Float = 0f) {
    if (alpha <= 0.01f) return
    if (glow > 0f) softGlow(Offset(foot.x, foot.y - s * 0.4f), s * 0.9f, Qn.GoldLight, glow * alpha)
    drawRect(Stone117, Offset(foot.x - s * 0.5f, foot.y - s * 0.6f), Size(s, s * 0.6f), alpha = alpha)
    for (k in 0 until 5) drawRect(Stone117, Offset(foot.x - s * 0.5f + k * s * 0.22f, foot.y - s * 0.7f), Size(s * 0.12f, s * 0.1f), alpha = alpha)
    for (sx in listOf(-1f, 1f)) drawRect(Color(0xFFA08A64), Offset(foot.x + sx * s * 0.5f - s * 0.1f, foot.y - s * 0.85f), Size(s * 0.2f, s * 0.85f), alpha = alpha)
    val gw = s * 0.22f
    drawRoundRect(Color(0xFF2A1E10), Offset(foot.x - gw / 2, foot.y - s * 0.32f), Size(gw, s * 0.32f), CornerRadius(gw / 2, gw / 2), alpha = alpha * (1f - gate))
    drawRect(Color(0xFF6B4F35), Offset(foot.x - gw / 2, foot.y - s * 0.32f), Size(gw, s * 0.32f * gate), alpha = alpha)
}

private fun DrawScope.smoke117(c: Offset, r: Float, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 4) drawCircle(Qn.Smoke, r * (0.5f + 0.15f * k), Offset(c.x + r * 0.4f * sin(t * 1.3f + k), c.y - r * 0.25f * k), alpha = 0.55f * alpha)
}

/** Two wings around a point of light; `lost` folds one wing away. */
private fun DrawScope.wings117(c: Offset, s: Float, alpha: Float, t: Float, lost: Float = 0f, colR: Color = Qn.GoldLight, colL: Color = Qn.Rose) {
    if (alpha <= 0.01f) return
    val flap = 0.15f * sin(t * 3f)
    for (side in listOf(1f, -1f)) {
        val a2 = if (side < 0) alpha * (1f - lost) else alpha
        val tip = Offset(c.x + side * s, c.y - s * (0.45f + flap))
        drawPath(Path().apply {
            moveTo(c.x, c.y)
            quadraticBezierTo(c.x + side * s * 0.5f, c.y - s * (0.9f + flap), tip.x, tip.y)
            quadraticBezierTo(c.x + side * s * 0.55f, c.y - s * 0.05f, c.x, c.y)
        }, if (side > 0) colR else colL, alpha = 0.85f * a2)
    }
    softGlow(c, s * 0.35f, Qn.GoldLight, 0.7f * alpha)
    drawCircle(Qn.GoldLight, s * 0.08f, c, alpha = alpha)
}

// ═════════════ ws-thalath ═════════════
internal fun DrawScope.wsthalathScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "wsthalath", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night117(a) },
        { p, f ->
            icons117(listOf("sun", "cloud", "drop"), listOf(Qn.Gold, Dim117, Color(0xFF8FC1E3)), h * 0.38f, w * 0.11f, p, f, a.t, from = 0.1f, step = 0.2f)
            row117(a, "wsthalath0", 3, h * 0.56f, p, f, from = 0.15f, step = 0.2f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil117(f)
            icons117(listOf("heart", "lips", "hands"), listOf(Qn.Rose, Qn.GoldLight, Qn.Jade), h * 0.38f, w * 0.1f, p, f, a.t, from = 0.1f, step = 0.22f)
            row117(a, "wsthalath1", 3, h * 0.56f, p, f, from = 0.15f, step = 0.22f)
        },
        { p, f ->
            veil117(f)
            icons117(listOf("heart", "lips", "hands"), listOf(Qn.Rose, Qn.GoldLight, Qn.Jade), h * 0.34f, w * 0.1f, p, f, a.t, from = 0.05f, step = 0.05f)
            icons117(listOf("lock", "lock", "lock"), listOf(Qn.Gold, Qn.Gold, Qn.Gold), h * 0.47f, w * 0.06f, p, f, a.t, from = 0.15f, step = 0.22f)
            row117(a, "wsthalath2", 3, h * 0.6f, p, f, from = 0.18f, step = 0.22f)
        },
        { p, f ->
            veil117(f)
            val turn = easeInOut(seg(p, 0.35f, 0.65f))
            val c = Offset(w * 0.5f, h * 0.42f)
            softGlow(c, w * 0.2f, Qn.GoldLight, 0.6f * f * turn)
            drawRoundRect(lerpC(Color(0xFF3A3040), Qn.Gold, turn), Offset(c.x - w * 0.1f, c.y - w * 0.08f), Size(w * 0.2f, w * 0.16f), CornerRadius(w * 0.02f), alpha = f)
            drawRect(lerpC(Color(0xFF241C2C), Qn.Rose, turn), Offset(c.x - w * 0.015f, c.y - w * 0.08f), Size(w * 0.03f, w * 0.16f), alpha = f)
            tagAt(a.tag("wsthalath3", 0, 0), Offset(w * 0.72f, h * 0.66f), f * seg(p, 0.1f, 0.25f) * (1f - 0.5f * turn), ink = ArtInk.Ivory)
            tagAt(a.tag("wsthalath3", 0, 1), Offset(w * 0.28f, h * 0.66f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

private fun lerpC(a: Color, b: Color, t: Float) = Color(
    a.red + (b.red - a.red) * t, a.green + (b.green - a.green) * t, a.blue + (b.blue - a.blue) * t, a.alpha + (b.alpha - a.alpha) * t)

// ═════════════ ws-dhanb ═════════════
internal fun DrawScope.wsdhanbScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wsdhanb", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night117(a) },
        { p, f ->
            val go = easeInOut(seg(p, 0.25f, 0.75f))
            // right: a sin (dark dot) that ends at the gate of light
            softGlow(Offset(w * 0.78f, h * 0.22f), w * 0.12f, Qn.GoldLight, 0.7f * f * go)
            door12(Offset(w * 0.78f, h * 0.3f), w * 0.1f, h * 0.12f, go, Qn.Gold, Color(0xFF6B4F35), f)
            drawCircle(Color(0xFF241C2C), w * 0.025f, Offset(w * 0.7f, mix(h * 0.62f, h * 0.32f, go)), alpha = f)
            // left: a good deed (bright star) that sinks into embers
            drawRect(Qn.Ember, Offset(w * 0.12f, h * 0.66f), Size(w * 0.24f, h * 0.04f), alpha = 0.6f * f * go)
            qIcon("star", Offset(w * 0.24f, mix(h * 0.32f, h * 0.62f, go)), w * 0.06f, Qn.GoldLight, f * (1f - 0.6f * go), a.t)
            row117(a, "wsdhanb0", 2, h * 0.12f, p, f, from = 0.05f, step = 0.15f)
        },
        { p, f ->
            val bow = seg(p, 0.1f, 0.4f)
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig117, f, lean = 0.35f * bow)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.6f + k / 3f) % 1f)
                qIcon("drop", Offset(w * 0.55f, g - h * 0.14f + ph * h * 0.12f), w * 0.02f, Color(0xFF8FC1E3), f * bow * (1f - ph), a.t)
            }
            softGlow(Offset(w * 0.5f, g - h * 0.12f), w * 0.25f, Qn.GoldLight, 0.5f * f * seg(p, 0.45f, 0.8f))
            tagAt(a.tag("wsdhanb1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            val rise = easeInOut(seg(p, 0.1f, 0.5f))
            drawRect(Dim117, Offset(w * 0.4f, g - h * 0.12f * rise), Size(w * 0.2f, h * 0.12f * rise), alpha = f)
            person99(Offset(w * 0.5f, g - h * 0.12f * rise), h * 0.14f, Fig117, f * (1f - 0.5f * seg(p, 0.6f, 0.9f)), lean = -0.15f * rise)
            qIcon("star", Offset(w * 0.62f, g - h * 0.3f), w * 0.04f, Qn.GoldLight, f * (1f - seg(p, 0.55f, 0.8f)), a.t)
            row117(a, "wsdhanb2", 2, h * 0.18f, p, f, from = 0.3f, step = 0.3f)
        },
        { p, f ->
            veil117(f)
            val y = g
            person99(Offset(w * 0.72f, y), h * 0.12f, Fig117, f)
            drawLine(Qn.GoldLight, Offset(w * 0.72f, h * 0.06f), Offset(w * 0.72f, y - h * 0.17f), w * 0.005f, alpha = 0.8f * f * seg(p, 0.1f, 0.3f))
            softGlow(Offset(w * 0.72f, y - h * 0.1f), w * 0.12f, Qn.GoldLight, 0.4f * f)
            person99(Offset(w * 0.28f, y), h * 0.12f, Dim117, f * (1f - 0.4f * seg(p, 0.5f, 0.8f)), lean = 0.2f * seg(p, 0.5f, 0.8f))
            tagAt(a.tag("wsdhanb3", 0, 0), Offset(w * 0.72f, h * 0.5f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("wsdhanb3", 0, 1), Offset(w * 0.28f, h * 0.5f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ws-janah ═════════════
internal fun DrawScope.wsjanahScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "wsjanah", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night117(a) },
        { p, f ->
            val c = Offset(w * 0.5f, mix(h * 0.6f, h * 0.42f, easeInOut(seg(p, 0.1f, 0.8f))))
            wings117(c, w * 0.28f, f * seg(p, 0.05f, 0.25f), a.t)
            tagAt(a.tag("wsjanah0", 0, 0), Offset(w * 0.74f, h * 0.68f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            tagAt(a.tag("wsjanah0", 0, 1), Offset(w * 0.26f, h * 0.68f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil117(f)
            val c = Offset(w * 0.5f, h * 0.45f)
            wings117(c, w * 0.28f, f, a.t)
            qIcon("heart", Offset(w * 0.68f, h * 0.3f), w * 0.07f, Qn.Rose, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("drop", Offset(w * 0.32f, h * 0.3f), w * 0.05f, Color(0xFF8FC1E3), f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("wsjanah1", 0, 0), Offset(w * 0.74f, h * 0.66f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("wsjanah1", 0, 1), Offset(w * 0.26f, h * 0.66f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil117(f)
            val lost = easeInOut(seg(p, 0.15f, 0.45f))
            val fall = easeInOut(seg(p, 0.4f, 0.9f))
            wings117(Offset(w * (0.5f + 0.08f * fall), mix(h * 0.4f, h * 0.68f, fall)), w * 0.24f, f, a.t, lost = lost)
            tagAt(a.tag("wsjanah2", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil117(f)
            wings117(Offset(w * 0.5f, h * 0.5f), w * 0.22f, f, a.t)
            tagAt(a.tag("wsjanah3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("wsjanah3", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ws-yahya ═════════════
internal fun DrawScope.wsyahyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wsyahya", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night117(a) },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.36f)
            softGlow(c, w * 0.14f, Color(0xFFCFE6F2), 0.8f * f * seg(p, 0.05f, 0.25f))
            drawCircle(Color(0xFFF2F7FA), w * 0.03f, c, alpha = f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("wsyahya0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            for (k in 0 until 5) {
                val x = w * (0.74f - 0.12f * k)
                drawCircle(Qn.Gold, w * 0.022f, Offset(x, h * 0.58f), alpha = f * seg(p, 0.35f + 0.08f * k, 0.45f + 0.08f * k))
            }
            tagAt(a.tag("wsyahya0", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.75f, 0.9f), ink = ArtInk.Gold)
        },
        { p, f ->
            val dome = Offset(w * 0.5f, g)
            drawRect(Color(0xFFCDB58A), Offset(dome.x - w * 0.3f, g - h * 0.16f), Size(w * 0.6f, h * 0.16f), alpha = f)
            drawArc(Color(0xFFD8C08E), 180f, 180f, true, Offset(dome.x - w * 0.13f, g - h * 0.3f), Size(w * 0.26f, h * 0.28f), alpha = f)
            for (k in 0 until 12) drawCircle(Fig117, w * 0.012f, Offset(w * (0.24f + 0.047f * k), g - h * 0.04f - h * 0.03f * (k % 2)), alpha = f * seg(p, 0.05f + 0.02f * k, 0.15f + 0.02f * k))
            row117(a, "wsyahya1", 5, h * 0.16f, p, f, from = 0.3f, step = 0.12f, ink = ArtInk.Gold)
        },
        { p, f ->
            house12(Offset(w * 0.72f, g), w * 0.32f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.5f)
            house12(Offset(w * 0.22f, g), w * 0.26f, Color(0xFF8A7A6A), Color(0xFF4A3A2A), f)
            val go = easeInOut(seg(p, 0.3f, 0.8f))
            val x = mix(w * 0.62f, w * 0.3f, go)
            person99(Offset(x, g), h * 0.11f, Dim117, f, walk = a.t * 2f * (if (go in 0.01f..0.99f) 1f else 0f))
            qIcon("coin", Offset(x, g - h * 0.17f), w * 0.035f, Qn.Gold, f, a.t)
            tagAt(a.tag("wsyahya2", 0, 0), Offset(w * 0.72f, h * 0.36f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("wsyahya2", 0, 1), Offset(w * 0.22f, h * 0.4f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil117(f)
            for (k in 0 until 5) {
                val ang = k / 5f * 6.283f + a.t * 0.3f
                val c = Offset(w * 0.5f + w * 0.18f * kotlin.math.cos(ang), h * 0.45f + h * 0.12f * sin(ang))
                softGlow(c, w * 0.05f, Qn.GoldLight, 0.5f * f)
                drawCircle(Qn.Gold, w * 0.022f, c, alpha = f * seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k))
            }
            tagAt(a.tag("wsyahya3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("wsyahya3", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ws-salat ═════════════
internal fun DrawScope.wssalatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wssalat", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night117(a) },
        { p, f ->
            qIcon("mat", Offset(w * 0.5f, g + h * 0.02f), w * 0.22f, Qn.Lapis, f, a.t)
            person99(Offset(w * 0.5f, g), h * 0.15f, Fig117, f)
            softGlow(Offset(w * 0.5f, h * 0.2f), w * 0.2f, Qn.GoldLight, 0.5f * f * seg(p, 0.1f, 0.3f))
            for (sx in listOf(-1f, 1f)) {
                val q = seg(p, 0.45f, 0.6f) * (1f - seg(p, 0.7f, 0.85f))
                drawLine(Dim117, Offset(w * 0.5f + sx * w * 0.06f, g - h * 0.17f), Offset(w * 0.5f + sx * w * 0.22f, g - h * 0.2f), w * 0.006f, alpha = f * q, cap = StrokeCap.Round)
                qIcon("cross", Offset(w * 0.5f + sx * w * 0.25f, g - h * 0.2f), w * 0.04f, Qn.Rose, f * q, a.t)
            }
            tagAt(a.tag("wssalat0", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            qIcon("crown", Offset(w * 0.74f, h * 0.36f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            drawRoundRect(Color(0xFF6B4F35), Offset(w * 0.68f, h * 0.44f), Size(w * 0.12f, g - h * 0.44f), CornerRadius(w * 0.02f), alpha = f)
            val look = sin(a.t * 2.4f) * seg(p, 0.3f, 0.45f)
            person99(Offset(w * 0.36f, g), h * 0.13f, Fig117, f, lean = 0.25f * look)
            tagAt(a.tag("wssalat1", 0, 0), Offset(w * 0.36f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 2) {
                val x = w * (0.66f - 0.32f * k)
                qIcon("mat", Offset(x, g + h * 0.02f), w * 0.18f, Qn.Lapis, f, a.t)
                person99(Offset(x, g), h * 0.13f, if (k == 0) Fig117 else Dim117, f)
            }
            val sep = easeInOut(seg(p, 0.3f, 0.7f))
            softGlow(Offset(w * 0.66f, mix(g - h * 0.1f, h * 0.15f, sep)), w * 0.1f, Qn.GoldLight, 0.7f * f * sep)
            drawLine(Qn.GoldLight, Offset(w * 0.66f, g - h * 0.2f), Offset(w * 0.66f, mix(g - h * 0.2f, h * 0.15f, sep)), w * 0.005f, alpha = 0.6f * f)
            tagAt(a.tag("wssalat2", 0, 0), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil117(f)
            val fr = listOf(0.5f, 0.333f, 0.25f, 0.1f)
            for (i in 0 until 4) {
                val y = h * (0.24f + 0.13f * i)
                val q = seg(p, 0.08f + 0.18f * i, 0.24f + 0.18f * i)
                drawRoundRect(Color(0x33FFFFFF), Offset(w * 0.12f, y - h * 0.02f), Size(w * 0.5f, h * 0.04f), CornerRadius(h * 0.02f), alpha = f * q)
                drawRoundRect(Qn.Gold, Offset(w * 0.12f + w * 0.5f * (1f - fr[i]), y - h * 0.02f), Size(w * 0.5f * fr[i], h * 0.04f), CornerRadius(h * 0.02f), alpha = f * q)
                tagAt(a.tag("wssalat3", 0, i), Offset(w * 0.78f, y), f * q, ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ ws-hisn ═════════════
internal fun DrawScope.wshisnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wshisn", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night117(a) },
        { p, f ->
            for (k in 0 until 5) {
                val x = w * (0.18f + 0.16f * k)
                person99(Offset(x, g), h * (if (k == 2) 0.13f else 0.11f), if (k == 2) Fig117 else Dim117, f, lean = if (k < 2) 0.12f else if (k > 2) -0.12f else 0f)
            }
            drawCircle(Qn.Soil, w * 0.022f, Offset(w * 0.53f, g - h * 0.08f), alpha = f)
            for (k in 0 until 4) {
                val ph = ((a.t * 0.4f + k / 4f) % 1f)
                drawCircle(Qn.Jade, w * (0.008f + 0.02f * ph), Offset(w * 0.53f + w * 0.12f * (ph - 0.5f) * (if (k % 2 == 0) 1f else -1f), g - h * 0.1f - h * 0.15f * ph),
                    alpha = 0.5f * f * (1f - ph) * seg(p, 0.15f, 0.3f))
            }
            tagAt(a.tag("wshisn0", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            val free = easeInOut(seg(p, 0.5f, 0.8f))
            for (k in 0 until 2) person99(Offset(w * (0.2f + 0.6f * k), g), h * 0.13f, Foe117, f * (1f - 0.7f * free))
            person99(Offset(w * 0.5f, g), h * 0.12f, Fig117, f, lean = 0.2f * (1f - free))
            drawCircle(Qn.Ember, w * 0.02f, Offset(w * 0.5f, g - h * 0.1f), alpha = f * (1f - free), style = Stroke(w * 0.006f))
            for (k in 0 until 5) {
                val ph = seg(p, 0.3f + 0.05f * k, 0.55f + 0.05f * k)
                qIcon("coin", Offset(mix(w * 0.5f, w * (if (k % 2 == 0) 0.25f else 0.75f), ph), g - h * 0.14f - h * 0.1f * sin(ph * 3.1416f)), w * 0.03f, Qn.Gold, f * ph * (1f - seg(p, 0.85f, 0.95f)), a.t)
            }
            row117(a, "wshisn1", 2, h * 0.2f, p, f, from = 0.1f, step = 0.55f)
        },
        { p, f ->
            fort117(Offset(w * 0.3f, g), w * 0.36f, f, gate = seg(p, 0.75f, 0.9f))
            val run = easeInOut(seg(p, 0.1f, 0.7f))
            person99(Offset(mix(w * 0.95f, w * 0.3f, run), g), h * 0.1f, Fig117, f * (1f - seg(p, 0.7f, 0.75f)), walk = a.t * 4f, lean = -0.2f)
            smoke117(Offset(mix(w * 1.15f, w * 0.62f, run), g - h * 0.06f), w * 0.08f, f, a.t)
            tagAt(a.tag("wshisn2", 0, 0), Offset(w * 0.3f, h * 0.3f), f * seg(p, 0.75f, 0.9f), ink = ArtInk.Gold)
        },
        { p, f ->
            fort117(Offset(w * 0.5f, g), w * 0.42f, f, gate = 1f, glow = seg(p, 0.1f, 0.4f))
            for (k in 0 until 2) smoke117(Offset(w * (0.12f + 0.76f * k), g - h * 0.05f), w * 0.07f, f * (1f - seg(p, 0.4f, 0.8f)), a.t + k)
            tagAt(a.tag("wshisn3", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}
