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
 * Wave 121 (2026-11-18): Ibn Baz's al-Ifham, the book of sales: the best earning, the option of the sitting, sales of
 * risk, meeting the caravans, the wet grain under the dry, riba, and possession before resale. A market by day; people
 * are faceless, the Prophet ﷺ only his named light, and no animal is drawn (the fish and the bird are only named).
 */

private val Fig121 = Color(0xFFEDE4D0)
private val Dim121 = Color(0xFF8A8296)
private val Wall121 = Color(0xFFE2CCA0)
private val Trim121 = Color(0xFF8A6A40)
private val Grain121 = Color(0xFFD8B46A)
private val Cloth121 = listOf(Color(0xFFC0566A), Color(0xFF2C4A86), Color(0xFF6FCB9A), Color(0xFFE8B84A))

private fun DrawScope.day121(): Float {
    val g = size.height * 0.8f
    daySky12(Qn.DayTop, Qn.DayLow, g, Color(0xFFB09868))
    return g
}

private fun DrawScope.veil121(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row121(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

/** A market stall: posts and an awning. */
private fun DrawScope.stall121(foot: Offset, s: Float, alpha: Float, cloth: Color = Cloth121[0]) {
    if (alpha <= 0.01f) return
    for (sx in listOf(-0.45f, 0.45f)) drawLine(Trim121, Offset(foot.x + sx * s, foot.y), Offset(foot.x + sx * s, foot.y - s * 0.75f), s * 0.03f, alpha = alpha)
    drawPath(Path().apply {
        moveTo(foot.x - s * 0.55f, foot.y - s * 0.72f); lineTo(foot.x + s * 0.55f, foot.y - s * 0.72f)
        lineTo(foot.x + s * 0.45f, foot.y - s * 0.9f); lineTo(foot.x - s * 0.45f, foot.y - s * 0.9f); close()
    }, cloth, alpha = alpha)
    drawRect(Trim121, Offset(foot.x - s * 0.45f, foot.y - s * 0.32f), Size(s * 0.9f, s * 0.06f), alpha = alpha)
}

/** A heap of grain; `wet` darkens its lower half. */
private fun DrawScope.heap121(foot: Offset, s: Float, alpha: Float, wet: Float = 0f, wetTop: Float = 0f) {
    if (alpha <= 0.01f) return
    drawArc(Grain121, 180f, 180f, true, Offset(foot.x - s / 2, foot.y - s * 0.45f), Size(s, s * 0.9f), alpha = alpha)
    if (wet > 0f) drawRect(Color(0xFF8A6A3A), Offset(foot.x - s * 0.47f, foot.y - s * 0.12f), Size(s * 0.94f, s * 0.12f), alpha = alpha * wet)
    if (wetTop > 0f) drawArc(Color(0xFF8A6A3A), 200f, 140f, true, Offset(foot.x - s * 0.3f, foot.y - s * 0.46f), Size(s * 0.6f, s * 0.4f), alpha = alpha * wetTop)
}

private fun DrawScope.sack121(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Color(0xFFC8A87A), Offset(c.x - s / 2, c.y - s * 0.6f), Size(s, s * 0.6f), CornerRadius(s * 0.2f), alpha = alpha)
    drawLine(Trim121, Offset(c.x - s * 0.15f, c.y - s * 0.6f), Offset(c.x + s * 0.15f, c.y - s * 0.6f), s * 0.06f, alpha = alpha)
}

// ═════════════ bm-kasb ═════════════
internal fun DrawScope.bmkasbScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "bmkasb", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day121() },
        { p, f ->
            val g = h * 0.8f
            val hit = (sin(a.t * 5f) + 1f) / 2f
            person99(Offset(w * 0.72f, g), h * 0.13f, Fig121, f, lean = 0.2f * hit, arms = 0.4f + 0.4f * hit)
            drawRect(Trim121, Offset(w * 0.6f, g - h * 0.06f), Size(w * 0.08f, h * 0.06f), alpha = f)
            stall121(Offset(w * 0.28f, g), w * 0.32f, f)
            person99(Offset(w * 0.28f, g), h * 0.12f, Fig121, f)
            row121(a, "bmkasb0", 2, h * 0.18f, p, f, from = 0.2f, step = 0.35f, ink = ArtInk.Gold)
        },
        { p, f ->
            val g = h * 0.8f
            person99(Offset(w * 0.72f, g), h * 0.13f, Fig121, f, arms = 0.5f)
            person99(Offset(w * 0.28f, g), h * 0.13f, Dim121, f, arms = 0.5f)
            val x = easeInOut(seg(p, 0.2f, 0.6f))
            qIcon("coin", Offset(mix(w * 0.65f, w * 0.35f, x), g - h * 0.14f), w * 0.04f, Qn.Gold, f, a.t)
            sack121(Offset(mix(w * 0.35f, w * 0.65f, x), g - h * 0.05f), w * 0.07f, f)
            tagAt(a.tag("bmkasb1", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil121(f)
            for (i in 0 until 9) {
                val c = Offset(w * (0.62f - 0.12f * (i % 3)), h * (0.3f + 0.12f * (i / 3)))
                val q = seg(p, 0.05f + 0.05f * i, 0.12f + 0.05f * i)
                drawRoundRect(if (i == 4) Qn.Rose else Qn.Jade, Offset(c.x - w * 0.05f, c.y - h * 0.045f), Size(w * 0.1f, h * 0.09f), CornerRadius(w * 0.015f), alpha = 0.7f * f * q)
            }
            qIcon("cross", Offset(w * 0.5f, h * 0.42f), w * 0.05f, Color(0xFFFFFFFF), f * seg(p, 0.6f, 0.7f), a.t)
            tagAt(a.tag("bmkasb2", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil121(f)
            val icons = listOf("wheat", "scale", "hands")
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                qIcon(icons[i], Offset(x, h * 0.42f), w * 0.11f, if (i == 2) Qn.GoldLight else Dim121, f * seg(p, 0.08f + 0.2f * i, 0.2f + 0.2f * i), a.t)
            }
            softGlow(Offset(w * 0.22f, h * 0.42f), w * 0.12f, Qn.GoldLight, 0.6f * f * seg(p, 0.7f, 0.9f))
            row121(a, "bmkasb3", 3, h * 0.62f, p, f, from = 0.1f, step = 0.2f)
        },
    )
}

// ═════════════ bm-khiyar ═════════════
internal fun DrawScope.bmkhiyarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "bmkhiyar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day121() },
        { p, f ->
            val g = h * 0.8f
            qIcon("mat", Offset(w * 0.5f, g + h * 0.02f), w * 0.4f, Qn.Rose, f, a.t)
            person99(Offset(w * 0.6f, g), h * 0.13f, Fig121, f, arms = 0.6f)
            person99(Offset(w * 0.4f, g), h * 0.13f, Dim121, f, arms = 0.6f)
            softGlow(Offset(w * 0.5f, g - h * 0.1f), w * 0.06f, Qn.GoldLight, 0.7f * f * seg(p, 0.2f, 0.4f))
            tagAt(a.tag("bmkhiyar0", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            val g = h * 0.8f
            val apart = easeInOut(seg(p, 0.15f, 0.6f))
            qIcon("mat", Offset(w * 0.5f, g + h * 0.02f), w * 0.4f, Qn.Rose, f, a.t)
            person99(Offset(w * (0.6f + 0.3f * apart), g), h * 0.13f, Fig121, f, walk = a.t * 2f * apart)
            person99(Offset(w * (0.4f - 0.3f * apart), g), h * 0.13f, Dim121, f, walk = a.t * 2f * apart)
            tagAt(a.tag("bmkhiyar1", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil121(f)
            qIcon("lock", Offset(w * 0.5f, h * 0.42f), w * 0.12f, Qn.Gold, f * seg(p, 0.3f, 0.45f), a.t)
            for (sx in listOf(-1f, 1f)) person99(Offset(w * (0.5f + 0.25f * sx), h * 0.8f), h * 0.12f, Fig121, f)
            tagAt(a.tag("bmkhiyar2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil121(f)
            for (i in 0 until 3) {
                val y = h * (0.3f + 0.17f * i)
                tagAt(a.tag("bmkhiyar3", 0, i), Offset(w * 0.5f, y), f * seg(p, 0.1f + 0.22f * i, 0.24f + 0.22f * i), ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ bm-gharar ═════════════
internal fun DrawScope.bmghararScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "bmgharar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day121() },
        { p, f ->
            val g = h * 0.8f
            for (k in 0 until 4) drawRoundRect(Cloth121[k], Offset(w * (0.18f + 0.17f * k), g - h * 0.03f), Size(w * 0.13f, h * 0.04f), CornerRadius(w * 0.01f), alpha = f)
            drawCircle(Color(0xFF8A8296), w * 0.018f, Offset(w * 0.5f, h * 0.42f), alpha = f)
            qIcon("cross", Offset(w * 0.5f, h * 0.55f), w * 0.07f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            row121(a, "bmgharar0", 2, h * 0.2f, p, f, from = 0.1f, step = 0.35f)
        },
        { p, f ->
            val g = h * 0.8f
            for (k in 0 until 4) drawRoundRect(Cloth121[k], Offset(w * (0.18f + 0.17f * k), g - h * 0.03f), Size(w * 0.13f, h * 0.04f), CornerRadius(w * 0.01f), alpha = f)
            person99(Offset(w * 0.9f, g), h * 0.12f, Fig121, f, arms = 0.7f)
            val t = easeInOut(seg(p, 0.15f, 0.6f))
            val x = mix(w * 0.86f, w * 0.31f, t)
            val y = mix(g - h * 0.15f, g - h * 0.04f, t) - h * 0.25f * sin(t * 3.1416f)
            drawCircle(Color(0xFF8A8296), w * 0.018f, Offset(x, y), alpha = f)
            softGlow(Offset(w * 0.245f, g - h * 0.02f), w * 0.08f, Qn.GoldLight, 0.6f * f * seg(p, 0.6f, 0.75f))
            tagAt(a.tag("bmgharar1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            drawOval(Color(0xFF6FB4E0), Offset(w * 0.52f, g - h * 0.02f), Size(w * 0.4f, h * 0.1f), alpha = f)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.5f + k / 3f) % 1f)
                drawOval(Color(0xFFDCEEFA), Offset(w * 0.72f - w * 0.12f * ph, g + h * 0.03f - h * 0.02f * ph), Size(w * 0.24f * ph, h * 0.04f * ph), alpha = f * (1f - ph), style = Stroke(w * 0.004f))
            }
            qIcon("cloud", Offset(w * 0.26f, h * 0.36f), w * 0.12f, Color(0xFFFFFFFF), f, a.t)
            tagAt(a.tag("bmgharar2", 0, 0), Offset(w * 0.72f, h * 0.6f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
            tagAt(a.tag("bmgharar2", 0, 1), Offset(w * 0.26f, h * 0.22f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            for (k in 0 until 7) qIcon("sprout", Offset(w * (0.14f + 0.12f * k), g - h * 0.05f), w * 0.07f, Qn.Leaf, f, a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.45f), w * 0.07f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("bmgharar3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ bm-hadir ═════════════
internal fun DrawScope.bmhadirScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "bmhadir", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day121() },
        { p, f ->
            val g = h * 0.8f
            for (k in 0 until 2) house12(Offset(w * (0.78f + 0.14f * k), g), w * 0.18f, Wall121, Trim121, f)
            val come = easeInOut(seg(p, 0.05f, 0.7f))
            for (k in 0 until 3) {
                val x = mix(w * -0.1f, w * 0.3f, come) - k * w * 0.08f
                person99(Offset(x, g), h * 0.11f, Dim121, f, walk = a.t * 2f)
                sack121(Offset(x + w * 0.02f, g - h * 0.13f), w * 0.05f, f)
            }
            person99(Offset(mix(w * 0.7f, w * 0.42f, seg(p, 0.3f, 0.6f)), g), h * 0.12f, Fig121, f, walk = a.t * 3f)
            qIcon("cross", Offset(w * 0.42f, h * 0.45f), w * 0.06f, Qn.Rose, f * seg(p, 0.65f, 0.75f), a.t)
            tagAt(a.tag("bmhadir0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            stall121(Offset(w * 0.8f, g), w * 0.3f, f, Cloth121[1])
            person99(Offset(w * 0.2f, g), h * 0.12f, Dim121, f)
            sack121(Offset(w * 0.12f, g), w * 0.06f, f)
            person99(Offset(w * 0.5f, g), h * 0.13f, Fig121, f, arms = 0.6f * seg(p, 0.2f, 0.4f))
            qIcon("cross", Offset(w * 0.5f, h * 0.4f), w * 0.06f, Qn.Rose, f * seg(p, 0.55f, 0.65f), a.t)
            tagAt(a.tag("bmhadir1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            for (k in 0 until 2) person99(Offset(w * (0.12f + 0.1f * k), g), h * 0.11f, Dim121, f)
            for (k in 0 until 3) house12(Offset(w * (0.62f + 0.14f * k), g), w * 0.14f, Wall121, Trim121, f)
            softGlow(Offset(w * 0.17f, g - h * 0.08f), w * 0.12f, Qn.GoldLight, 0.5f * f * seg(p, 0.1f, 0.3f))
            softGlow(Offset(w * 0.76f, g - h * 0.08f), w * 0.2f, Qn.GoldLight, 0.5f * f * seg(p, 0.5f, 0.7f))
            row121(a, "bmhadir2", 2, h * 0.24f, p, f, from = 0.1f, step = 0.4f)
        },
        { p, f ->
            val g = h * 0.8f
            stall121(Offset(w * 0.5f, g), w * 0.34f, f, Cloth121[3])
            for (k in 0 until 4) {
                val up = seg(p, 0.1f + 0.12f * k, 0.2f + 0.12f * k)
                person99(Offset(w * (0.14f + 0.24f * k), g + h * 0.1f), h * 0.1f, Fig121, f, arms = 0.9f * up)
            }
            qIcon("check", Offset(w * 0.5f, h * 0.3f), w * 0.07f, Qn.Jade, f * seg(p, 0.6f, 0.75f), a.t)
            tagAt(a.tag("bmhadir3", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ bm-ghish ═════════════
internal fun DrawScope.bmghishScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "bmghish", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day121() },
        { p, f ->
            val g = h * 0.8f
            heap121(Offset(w * 0.4f, g), w * 0.4f, f)
            sirajMunir(Offset(mix(w * 0.95f, w * 0.75f, easeInOut(seg(p, 0.05f, 0.5f))), h * 0.48f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.75f, h * 0.36f))
            tagAt(a.tag("bmghish0", 0, 0), Offset(w * 0.4f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            heap121(Offset(w * 0.4f, g), w * 0.4f, f, wet = seg(p, 0.15f, 0.35f))
            sirajMunir(Offset(w * 0.75f, h * 0.48f), w * 0.035f, f, a.t, named = false)
            for (k in 0 until 4) drawCircle(Color(0xFF6FB4E0), w * 0.008f, Offset(w * (0.28f + 0.08f * k), g - h * 0.03f), alpha = f * seg(p, 0.3f, 0.45f))
            person99(Offset(w * 0.1f, g), h * 0.12f, Dim121, f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("bmghish1", 0, 0), Offset(w * 0.4f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil121(f)
            tagAt(a.tag("bmghish2", 0, 0), Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            val move = easeInOut(seg(p, 0.2f, 0.6f))
            heap121(Offset(w * 0.5f, g), w * 0.44f, f, wet = 1f - move, wetTop = move)
            qIcon("check", Offset(w * 0.5f, h * 0.3f), w * 0.06f, Qn.Jade, f * seg(p, 0.65f, 0.8f), a.t)
            tagAt(a.tag("bmghish3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ bm-riba ═════════════
internal fun DrawScope.bmribaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "bmriba", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day121() },
        { p, f ->
            veil121(f)
            val g = h * 0.8f
            for (k in 0 until 5) {
                person99(Offset(w * (0.82f - 0.16f * k), g), h * 0.11f, Dim121, f * seg(p, 0.05f + 0.1f * k, 0.15f + 0.1f * k))
            }
            drawRect(Color(0xFF241C2C), Offset(0f, h * 0.55f), Size(w, h * 0.05f), alpha = 0.5f * f * seg(p, 0.6f, 0.8f))
            row121(a, "bmriba0", 1, h * 0.24f, p, f, from = 0.65f)
        },
        { p, f ->
            veil121(f)
            for (k in 0 until 10) qIcon("coin", Offset(w * 0.5f, h * 0.7f - h * 0.045f * k), w * 0.08f, Qn.Gold, f * seg(p, 0.05f + 0.06f * k, 0.12f + 0.06f * k), a.t)
            tagAt(a.tag("bmriba1", 0, 0), Offset(w * 0.78f, h * 0.4f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil121(f)
            val c = Offset(w * 0.5f, h * 0.36f)
            drawLine(Trim121, c, Offset(c.x, h * 0.78f), w * 0.012f, alpha = f)
            drawLine(Trim121, Offset(c.x - w * 0.28f, c.y), Offset(c.x + w * 0.28f, c.y), w * 0.01f, alpha = f, cap = StrokeCap.Round)
            for (sx in listOf(-1f, 1f)) qIcon("coin", Offset(c.x + sx * w * 0.28f, c.y + h * 0.07f), w * 0.07f, Qn.Gold, f, a.t)
            qIcon("check", Offset(c.x, c.y - h * 0.1f), w * 0.06f, Qn.Jade, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("bmriba2", 0, 0), Offset(w * 0.5f, h * 0.86f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil121(f)
            qIcon("pen", Offset(w * 0.7f, h * 0.42f), w * 0.1f, Dim121, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("eye", Offset(w * 0.3f, h * 0.42f), w * 0.1f, Dim121, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("bmriba3", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ bm-qabd ═════════════
internal fun DrawScope.bmqabdScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "bmqabd", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { day121() },
        { p, f ->
            val g = h * 0.8f
            stall121(Offset(w * 0.75f, g), w * 0.3f, f, Cloth121[2])
            for (k in 0 until 3) sack121(Offset(w * (0.68f + 0.07f * k), g), w * 0.06f, f)
            person99(Offset(w * 0.4f, g), h * 0.13f, Fig121, f)
            qIcon("coin", Offset(mix(w * 0.45f, w * 0.65f, seg(p, 0.15f, 0.4f)), g - h * 0.14f), w * 0.04f, Qn.Gold, f * (1f - seg(p, 0.42f, 0.5f)), a.t)
            tagAt(a.tag("bmqabd0", 0, 0), Offset(w * 0.4f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            val g = h * 0.8f
            house12(Offset(w * 0.18f, g), w * 0.26f, Wall121, Trim121, f, lit = 0.4f)
            val go = easeInOut(seg(p, 0.1f, 0.7f))
            person99(Offset(mix(w * 0.75f, w * 0.34f, go), g), h * 0.13f, Fig121, f, walk = a.t * 2f * (1f - go))
            sack121(Offset(mix(w * 0.78f, w * 0.37f, go), g - h * 0.12f), w * 0.07f, f)
            tagAt(a.tag("bmqabd1", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            val g = h * 0.8f
            stall121(Offset(w * 0.72f, g), w * 0.3f, f, Cloth121[2])
            person99(Offset(w * 0.72f, g), h * 0.12f, Dim121, f, arms = 0.5f)
            val back = easeInOut(seg(p, 0.25f, 0.6f))
            sack121(Offset(mix(w * 0.42f, w * 0.65f, back), g), w * 0.07f, f)
            person99(Offset(w * 0.32f, g), h * 0.13f, Fig121, f, arms = 0.6f * (1f - back))
            tagAt(a.tag("bmqabd2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil121(f)
            val icons = listOf("hands", "wheat", "house")
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                qIcon(icons[i], Offset(x, h * 0.42f), w * 0.11f, Qn.GoldLight, f * seg(p, 0.08f + 0.25f * i, 0.2f + 0.25f * i), a.t)
            }
            row121(a, "bmqabd3", 3, h * 0.62f, p, f, from = 0.1f, step = 0.25f)
        },
    )
}
