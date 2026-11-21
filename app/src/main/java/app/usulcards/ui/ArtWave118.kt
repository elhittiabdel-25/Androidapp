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
 * Wave 118 (2026-11-18): al-Wabil al-Sayyib (2): the iron cloaks of the miser and the giver, «سبق المفردون»,
 * gatherings with and without dhikr, the rust of the heart, the benefits of dhikr, the plants of Paradise, and Ibn
 * Taymiyya's paradise in the chest. People are faceless, angels are lights, Ibn Taymiyya is a plain roundel, the devil
 * only dark smoke; tags holding the Name of Allah stay at the top.
 */

private val Fig118 = Color(0xFFEDE4D0)
private val Dim118 = Color(0xFF8A8296)
private val Iron118 = Color(0xFF6E7480)
private val Rust118 = Color(0xFF8A4A2A)

private fun DrawScope.night118(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.day118(): Float {
    val g = size.height * 0.8f
    daySky12(Qn.DayTop, Qn.DayLow, g, Color(0xFFB09868))
    return g
}

private fun DrawScope.veil118(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row118(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory, first: Int = 0) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, first + i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

/** An iron cloak over a standing figure: `open` 0 = tight to the collar, 1 = down to the feet. */
private fun DrawScope.cloak118(foot: Offset, tall: Float, open: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val top = foot.y - tall * 1.05f
    val len = tall * mix(0.28f, 1.0f, open)
    val half = tall * mix(0.14f, 0.24f, open)
    drawPath(Path().apply {
        moveTo(foot.x - tall * 0.12f, top); lineTo(foot.x + tall * 0.12f, top)
        lineTo(foot.x + half, top + len); lineTo(foot.x - half, top + len); close()
    }, Iron118, alpha = 0.9f * alpha)
    val rings = 6
    for (k in 0 until rings) {
        val y = top + len * (k + 0.5f) / rings
        drawLine(Color(0xFF9AA0AA), Offset(foot.x - half * 0.8f, y), Offset(foot.x + half * 0.8f, y), tall * 0.012f, alpha = 0.6f * alpha)
    }
}

/** A round mirror with rust spots: `rust` 0 = clear, 1 = dark. */
private fun DrawScope.mirror118(c: Offset, r: Float, rust: Float, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    drawCircle(Color(0xFF8A6A40), r * 1.08f, c, alpha = alpha)
    drawCircle(Color(0xFFE8EEF2), r, c, alpha = alpha)
    if (rust < 0.2f) softGlow(c, r * 1.2f, Color(0xFFFFFFFF), 0.4f * alpha * (1f - rust * 5f))
    for (k in 0 until 9) {
        val ang = k * 2.4f
        val d = r * (0.2f + 0.08f * (k % 5))
        drawCircle(if (k % 3 == 0) Color(0xFF2A1E18) else Rust118, r * (0.18f + 0.05f * (k % 3)) * rust, Offset(c.x + d * cos(ang), c.y + d * sin(ang)), alpha = alpha * rust)
    }
    drawCircle(Color(0xFF1A1418), r, c, alpha = alpha * (rust - 0.7f).coerceAtLeast(0f) * 2f)
}

// ═════════════ ws-jubba ═════════════
internal fun DrawScope.wsjubbaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    val tall = h * 0.15f
    film98(a, "wsjubba", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night118(a) },
        { p, f ->
            for (k in 0 until 2) {
                val x = w * (0.7f - 0.4f * k)
                person99(Offset(x, g), tall, Fig118, f)
                cloak118(Offset(x, g), tall, 0.3f, f * seg(p, 0.2f, 0.4f))
            }
            row118(a, "wsjubba0", 2, h * 0.3f, p, f, from = 0.4f, step = 0.2f)
        },
        { p, f ->
            val open = easeInOut(seg(p, 0.15f, 0.7f))
            person99(Offset(w * 0.5f, g), tall * 1.2f, Fig118, f)
            cloak118(Offset(w * 0.5f, g), tall * 1.2f, mix(0.3f, 1f, open), f)
            for (k in 0 until 4) {
                val ph = seg(p, 0.15f + 0.12f * k, 0.4f + 0.12f * k)
                qIcon("coin", Offset(mix(w * 0.56f, w * (0.7f + 0.06f * k), ph), g - h * 0.12f + h * 0.08f * ph), w * 0.03f, Qn.Gold, f * ph * (1f - seg(p, 0.85f, 0.95f)), a.t)
            }
            tagAt(a.tag("wsjubba1", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            val tight = easeInOut(seg(p, 0.2f, 0.6f))
            person99(Offset(w * 0.5f, g), tall * 1.2f, Dim118, f, arms = 0.4f * (1f - tight))
            cloak118(Offset(w * 0.5f, g), tall * 1.2f, mix(0.35f, 0f, tight), f)
            drawCircle(Iron118, w * 0.06f * (1f - 0.4f * tight), Offset(w * 0.5f, g - tall * 1.25f), alpha = f, style = Stroke(w * 0.012f))
            tagAt(a.tag("wsjubba2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil118(f)
            qIcon("heart", Offset(w * 0.7f, h * 0.45f), w * 0.15f * mix(0.6f, 1f, seg(p, 0.4f, 0.7f)), Qn.Rose, f * seg(p, 0.35f, 0.5f), a.t)
            softGlow(Offset(w * 0.7f, h * 0.45f), w * 0.15f, Qn.GoldLight, 0.5f * f * seg(p, 0.45f, 0.7f))
            qIcon("heart", Offset(w * 0.3f, h * 0.45f), w * 0.07f, Dim118, f * seg(p, 0.05f, 0.2f), a.t)
            drawCircle(Iron118, w * 0.06f, Offset(w * 0.3f, h * 0.45f), alpha = f * seg(p, 0.1f, 0.25f), style = Stroke(w * 0.01f))
            tagAt(a.tag("wsjubba3", 0, 0), Offset(w * 0.3f, h * 0.64f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("wsjubba3", 0, 1), Offset(w * 0.7f, h * 0.64f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ws-mufarridun ═════════════
internal fun DrawScope.wsmufarridunScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wsmufarridun", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night118(a) },
        { p, f ->
            qIcon("mountain", Offset(w * 0.3f, g - h * 0.12f), w * 0.34f, Color(0xFF5A4A3A), f, a.t)
            val go = easeInOut(seg(p, 0.1f, 0.9f))
            sirajMunir(Offset(mix(w * 0.95f, w * 0.55f, go), h * 0.42f), w * 0.03f, f, a.t, nameAt = Offset(mix(w * 0.95f, w * 0.55f, go), h * 0.3f))
            for (k in 0 until 4) person99(Offset(mix(w * 1.05f, w * 0.62f, go) + k * w * 0.07f, g), h * 0.09f, Fig118, f, walk = a.t * 2f)
            tagAt(a.tag("wsmufarridun0", 0, 0), Offset(w * 0.3f, h * 0.36f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("wsmufarridun0", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil118(f)
            for (k in 0 until 2) {
                val x = w * (0.62f - 0.24f * k)
                if (k == 0) person99(Offset(x, g), h * 0.14f, Fig118, f) else woman12(Offset(x, g), h * 0.14f, Color(0xFFD8C2E0), f)
                for (j in 0 until 3) {
                    val ph = ((a.t * 0.5f + j / 3f) % 1f)
                    drawCircle(Qn.GoldLight, w * 0.008f, Offset(x + w * 0.02f, g - h * 0.2f - ph * h * 0.18f), alpha = f * (1f - ph) * seg(p, 0.1f, 0.25f))
                }
            }
            tagAt(a.tag("wsmufarridun1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil118(f)
            person99(Offset(w * 0.7f, g), h * 0.14f, Fig118, f)
            softGlow(Offset(w * 0.7f, g - h * 0.1f), w * 0.15f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f, 0.4f))
            person99(Offset(w * 0.3f, g), h * 0.14f, Color(0xFF5A5466), f * mix(1f, 0.6f, seg(p, 0.5f, 0.8f)))
            row118(a, "wsmufarridun2", 2, h * 0.3f, p, f, from = 0.25f, step = 0.35f)
        },
        { p, f ->
            veil118(f)
            tagAt(a.tag("wsmufarridun3", 0, 1), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
            val tilt = 0.16f * easeInOut(seg(p, 0.55f, 0.85f))
            val c = Offset(w * 0.5f, h * 0.36f)
            val l = Offset(c.x - w * 0.28f * cos(tilt), c.y + w * 0.28f * sin(tilt))
            val r = Offset(c.x + w * 0.28f * cos(tilt), c.y - w * 0.28f * sin(tilt))
            drawLine(Color(0xFF8A6A40), Offset(c.x, c.y), Offset(c.x, g), w * 0.012f, alpha = f)
            drawLine(Color(0xFF8A6A40), l, r, w * 0.01f, alpha = f, cap = StrokeCap.Round)
            softGlow(Offset(l.x, l.y + h * 0.06f), w * 0.08f, Qn.GoldLight, 0.8f * f)
            for (k in 0 until 3) qIcon("coin", Offset(r.x - w * 0.04f + k * w * 0.04f, r.y + h * 0.06f), w * 0.035f, Qn.Gold, f, a.t)
            tagAt(a.tag("wsmufarridun3", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ws-majlis ═════════════
private fun DrawScope.circle118(c: Offset, r: Float, n: Int, color: Color, alpha: Float, leave: Float = 0f) {
    for (k in 0 until n) {
        val ang = k / n.toFloat() * 6.283f
        val d = r * (1f + 1.2f * leave)
        drawCircle(color, r * 0.16f, Offset(c.x + d * cos(ang), c.y + d * sin(ang) * 0.45f), alpha = alpha * (1f - 0.7f * leave))
    }
}

internal fun DrawScope.wsmajlisScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "wsmajlis", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night118(a) },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.62f)
            for (k in 0 until 3) drawCircle(Qn.Smoke, w * (0.1f + 0.03f * k), Offset(w * (0.4f + 0.1f * k), h * 0.4f), alpha = 0.7f * f * seg(p, 0.2f, 0.45f))
            circle118(c, w * 0.18f, 8, Dim118, f, leave = easeInOut(seg(p, 0.45f, 0.8f)))
            tagAt(a.tag("wsmajlis0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.62f)
            circle118(c, w * 0.18f, 8, Dim118, f)
            drawCircle(Color(0xFF241C2C), w * 0.12f, c, alpha = 0.5f * f * seg(p, 0.2f, 0.5f))
            tagAt(a.tag("wsmajlis1", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.62f)
            softGlow(c, w * 0.32f, Qn.GoldLight, 0.5f * f * seg(p, 0.3f, 0.6f))
            circle118(c, w * 0.18f, 8, Fig118, f)
            for (k in 0 until 6) {
                val ang = k / 6f * 3.1416f + 3.1416f
                angel98(Offset(c.x + w * 0.36f * cos(ang), c.y + h * 0.2f * sin(ang)), w * 0.018f, f * seg(p, 0.08f + 0.05f * k, 0.2f + 0.05f * k))
            }
            row118(a, "wsmajlis2", 2, h * 0.86f, p, f, from = 0.3f, step = 0.3f)
        },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.62f)
            softGlow(c, w * 0.3f, Qn.GoldLight, 0.5f * f)
            circle118(c, w * 0.18f, 8, Fig118, f)
            val up = easeInOut(seg(p, 0.15f, 0.6f))
            drawLine(Qn.GoldLight, c, Offset(c.x, mix(c.y, h * 0.1f, up)), w * 0.006f, alpha = 0.7f * f, cap = StrokeCap.Round)
            tagAt(a.tag("wsmajlis3", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ws-sada ═════════════
internal fun DrawScope.wssadaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    val c = Offset(w * 0.5f, h * 0.44f)
    film98(a, "wssada", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night118(a) },
        { p, f ->
            mirror118(c, w * 0.18f, 1f - easeInOut(seg(p, 0.3f, 0.8f)), f, a.t)
            tagAt(a.tag("wssada0", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.75f, 0.9f), ink = ArtInk.Gold)
        },
        { p, f ->
            val rust = seg(p, 0.05f, 0.35f) * (1f - easeInOut(seg(p, 0.55f, 0.9f)))
            mirror118(c, w * 0.18f, rust, f, a.t)
            row118(a, "wssada1", 2, h * 0.12f, p, f, from = 0.05f, step = 0.15f)
            row118(a, "wssada1", 2, h * 0.76f, p, f, from = 0.55f, step = 0.15f, ink = ArtInk.Gold, first = 2)
        },
        { p, f ->
            mirror118(c, w * 0.18f, 0.9f, f, a.t)
            val sw = easeInOut(seg(p, 0.35f, 0.75f))
            tagAt(a.tag("wssada2", 0, 0), Offset(mix(w * 0.78f, w * 0.22f, sw), h * 0.76f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("wssada2", 0, 1), Offset(mix(w * 0.22f, w * 0.78f, sw), h * 0.76f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil118(f)
            person99(Offset(w * 0.7f, g), h * 0.14f, Fig118, f)
            softGlow(Offset(w * 0.7f, g - h * 0.1f), w * 0.13f, Qn.GoldLight, 0.6f * f * seg(p, 0.1f, 0.3f))
            person99(Offset(w * 0.3f, g), h * 0.14f, Color(0xFF5A5466), f)
            drawCircle(Qn.Smoke, w * 0.06f, Offset(w * 0.3f, g - h * 0.2f), alpha = 0.6f * f * seg(p, 0.3f, 0.5f))
            row118(a, "wssada3", 2, h * 0.3f, p, f, from = 0.15f, step = 0.3f)
        },
    )
}

// ═════════════ ws-fawaid ═════════════
internal fun DrawScope.wsfawaidScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wsfawaid", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night118(a) },
        { p, f ->
            for (i in 0 until 100) {
                val c = Offset(w * (0.27f + 0.05f * (i % 10)), h * (0.3f + 0.035f * (i / 10)))
                drawCircle(Qn.Gold, w * 0.01f, c, alpha = f * seg(p, 0.05f + 0.006f * i, 0.1f + 0.006f * i))
            }
            tagAt(a.tag("wsfawaid0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.5f)
            softGlow(c, w * 0.22f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f, 0.5f))
            qIcon("heart", c, w * 0.16f, Qn.Rose, f, a.t)
            val out = easeInOut(seg(p, 0.15f, 0.6f))
            for (k in 0 until 3) drawCircle(Qn.Smoke, w * 0.06f, Offset(c.x + mix(w * 0.05f, w * 0.42f, out) * (if (k % 2 == 0) 1f else -1f), c.y - h * 0.05f * k), alpha = 0.7f * f * (1f - out))
            row118(a, "wsfawaid1", 3, h * 0.14f, p, f, from = 0.25f, step = 0.18f)
        },
        { p, f ->
            veil118(f)
            val icons = listOf("sun", "heart", "wheat")
            val cols = listOf(Qn.Gold, Qn.Rose, Qn.Gold)
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                qIcon(icons[i], Offset(x, h * 0.42f), w * 0.12f, cols[i], f * seg(p, 0.1f + 0.22f * i, 0.22f + 0.22f * i), a.t)
            }
            row118(a, "wsfawaid2", 3, h * 0.62f, p, f, from = 0.12f, step = 0.22f)
        },
        { p, f ->
            kaaba29(Offset(w * 0.18f, g), w * 0.16f, f)
            val go = easeInOut(seg(p, 0.2f, 0.8f))
            person99(Offset(mix(w * 0.6f, w * 0.3f, go), g), h * 0.1f, Fig118, f, walk = a.t * 2f)
            drawRoundRect(Color(0xFF6B4F35), Offset(w * 0.76f, g - h * 0.05f), Size(w * 0.1f, h * 0.05f), CornerRadius(w * 0.01f), alpha = f)
            person99(Offset(w * 0.81f, g - h * 0.05f), h * 0.07f, Dim118, f)
            tagAt(a.tag("wsfawaid3", 0, 0), Offset(w * 0.6f, h * 0.3f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ws-ghiras ═════════════
internal fun DrawScope.wsghirasScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "wsghiras", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day118() },
        { p, f ->
            val g = h * 0.8f
            drawRect(Color(0xFFC8B07A), Offset(0f, g - h * 0.02f), Size(w, h * 0.06f), alpha = f)
            val flow = easeInOut(seg(p, 0.2f, 0.7f))
            drawLine(Color(0xFF6FB4E0), Offset(w * 0.02f, g + h * 0.02f), Offset(mix(w * 0.02f, w * 0.98f, flow), g + h * 0.02f), h * 0.012f, alpha = f, cap = StrokeCap.Round)
            tagAt(a.tag("wsghiras0", 0, 0), Offset(w * 0.5f, h * 0.4f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            for (i in 0 until 4) {
                val x = w * (0.5f + (1.5f - i) * 0.21f)
                val grow = easeInOut(seg(p, 0.1f + 0.2f * i, 0.3f + 0.2f * i))
                qIcon("tree", Offset(x, g - h * 0.1f * grow), w * 0.14f * grow, Qn.Leaf, f * grow, a.t)
            }
            row118(a, "wsghiras1", 4, h * 0.16f, p, f, from = 0.1f, step = 0.2f, ink = ArtInk.Gold)
        },
        { p, f ->
            val g = h * 0.8f
            val grow = easeInOut(seg(p, 0.2f, 0.7f))
            drawLine(Color(0xFF8A6A40), Offset(w * 0.5f, g), Offset(w * 0.5f, g - h * 0.4f * grow), w * 0.025f, alpha = f, cap = StrokeCap.Round)
            for (k in 0 until 6) {
                val ang = 3.1416f + k / 5f * 3.1416f
                val tip = Offset(w * 0.5f + w * 0.18f * grow * cos(ang), g - h * 0.4f * grow + h * 0.1f * grow * sin(ang) + h * 0.06f * grow)
                drawLine(Qn.Leaf, Offset(w * 0.5f, g - h * 0.4f * grow), tip, w * 0.014f, alpha = f * grow, cap = StrokeCap.Round)
            }
            tagAt(a.tag("wsghiras2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("wsghiras2", 0, 1), Offset(w * 0.78f, h * 0.5f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            for (i in 0 until 12) {
                val x = w * (0.06f + 0.08f * i)
                val grow = easeInOut(seg(p, 0.05f + 0.05f * i, 0.25f + 0.05f * i))
                qIcon("tree", Offset(x, g - h * 0.06f * grow - h * 0.02f * (i % 2)), w * 0.08f * grow, Qn.Leaf, f * grow, a.t)
            }
            tagAt(a.tag("wsghiras3", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ws-jannat ═════════════
internal fun DrawScope.wsjannatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "wsjannat", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night118(a) },
        { p, f ->
            plain97(Offset(w * 0.5f, h * 0.24f), w * 0.09f, a, "wsjannat0", 0, f * seg(p, 0.05f, 0.25f))
            val c = Offset(w * 0.5f, h * 0.55f)
            softGlow(c, w * 0.2f, Qn.Jade, 0.5f * f * seg(p, 0.4f, 0.7f))
            qIcon("heart", c, w * 0.22f, Qn.Rose, f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("tree", Offset(c.x, c.y + w * 0.01f), w * 0.08f, Qn.Jade, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("wsjannat0", 0, 1), Offset(w * 0.5f, h * 0.8f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.5f)
            for (k in 0 until 5) {
                val ang = k / 5f * 6.283f + 0.3f
                val d = mix(w * 0.42f, w * 0.3f, easeInOut(seg(p, 0.05f, 0.4f)))
                drawCircle(Qn.Smoke, w * 0.05f, Offset(c.x + d * cos(ang), c.y + d * sin(ang) * 0.7f), alpha = 0.7f * f)
            }
            softGlow(c, w * 0.22f, Qn.Jade, 0.6f * f * seg(p, 0.3f, 0.6f))
            qIcon("heart", c, w * 0.2f, Qn.Rose, f, a.t)
            qIcon("tree", Offset(c.x, c.y + w * 0.01f), w * 0.08f, Qn.Jade, f, a.t)
            tagAt(a.tag("wsjannat1", 0, 0), Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil118(f)
            val icons = listOf("lock", "star", "mountain")
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                qIcon(icons[i], Offset(x, h * 0.42f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.08f + 0.25f * i, 0.2f + 0.25f * i), a.t)
            }
            row118(a, "wsjannat2", 3, h * 0.6f, p, f, from = 0.1f, step = 0.25f, ink = ArtInk.Gold)
        },
        { p, f ->
            plain97(Offset(w * 0.7f, h * 0.4f), w * 0.08f, a, "wsjannat3", 0, f * seg(p, 0.05f, 0.2f))
            val go = easeInOut(seg(p, 0.15f, 0.6f))
            for (k in 0 until 3) {
                val x = mix(w * 0.1f, w * 0.45f, go) - k * w * 0.08f
                person99(Offset(x, g), h * 0.1f, Fig118, f, walk = a.t * 2f * (1f - go))
                drawCircle(Qn.Smoke, w * 0.035f, Offset(x, g - h * 0.17f), alpha = 0.6f * f * (1f - seg(p, 0.55f, 0.85f)))
            }
            softGlow(Offset(w * 0.55f, g - h * 0.1f), w * 0.25f, Qn.GoldLight, 0.4f * f * seg(p, 0.6f, 0.85f))
            tagAt(a.tag("wsjannat3", 0, 1), Offset(w * 0.4f, h * 0.2f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
    )
}
