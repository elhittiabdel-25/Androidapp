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
 * Wave 127 (2026-11-18): Hidayat al-Qari: Hafs and 'Asim (plain roundels, 'Ali his family roundel), the three speeds
 * of reading as sound waves, the pillars of a valid reading, clear and hidden mistakes, stopping, and the basmala
 * between two suras drawn as three blocks joined or apart. No verse is written in the scenes.
 */

private val Fig127 = Color(0xFFEDE4D0)
private val Dim127 = Color(0xFF8A8296)

private fun DrawScope.night127(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil127(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row127(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

/** A sound wave across the frame: `speed` sets how dense it is. */
private fun DrawScope.wave127(y: Float, speed: Float, amp: Float, color: Color, alpha: Float, t: Float, reveal: Float = 1f) {
    val w = size.width
    val path = Path()
    val n = 80
    for (i in 0..n) {
        val x = w * (0.9f - 0.8f * reveal * i / n)
        val yy = y + amp * sin(i * 0.25f * speed + t * 2f * speed)
        if (i == 0) path.moveTo(x, yy) else path.lineTo(x, yy)
    }
    drawPath(path, color, alpha = alpha, style = Stroke(w * 0.007f, cap = StrokeCap.Round))
}

/** Three blocks right to left: sura, basmala, next sura; `j1`, `j2` join them (1 = joined). */
private fun DrawScope.blocks127(y: Float, j1: Float, j2: Float, alpha: Float, cross: Float = 0f, t: Float = 0f) {
    val w = size.width
    val cols = listOf(Qn.Lapis, Qn.Gold, Qn.Jade)
    val xs = listOf(0.78f, 0.5f, 0.22f)
    for (i in 0 until 3) drawRoundRect(cols[i], Offset(w * xs[i] - w * 0.1f, y - w * 0.04f), Size(w * 0.2f, w * 0.08f), CornerRadius(w * 0.02f), alpha = alpha)
    if (j1 > 0f) drawLine(Qn.GoldLight, Offset(w * 0.68f, y), Offset(w * 0.6f, y), w * 0.012f * j1, alpha = alpha, cap = StrokeCap.Round)
    if (j2 > 0f) drawLine(Qn.GoldLight, Offset(w * 0.4f, y), Offset(w * 0.32f, y), w * 0.012f * j2, alpha = alpha, cap = StrokeCap.Round)
    if (cross > 0f) qIcon("cross", Offset(w * 0.5f, y - w * 0.09f), w * 0.05f, Qn.Rose, alpha * cross, t)
}

// ═════════════ hq-hafs ═════════════
internal fun DrawScope.hqhafsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "hqhafs", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night127(a) },
        { p, f ->
            plain97(Offset(w * 0.7f, h * 0.42f), w * 0.09f, a, "hqhafs0", 0, f * seg(p, 0.05f, 0.25f))
            plain97(Offset(w * 0.3f, h * 0.42f), w * 0.09f, a, "hqhafs0", 1, f * seg(p, 0.35f, 0.55f))
            drawLine(Qn.GoldLight, Offset(w * 0.6f, h * 0.42f), Offset(mix(w * 0.6f, w * 0.4f, seg(p, 0.25f, 0.5f)), h * 0.42f), w * 0.008f, alpha = f, cap = StrokeCap.Round)
        },
        { p, f ->
            veil127(f)
            plain97(Offset(w * 0.5f, h * 0.38f), w * 0.09f, a, "hqhafs1", 1, f)
            qIcon("check", Offset(w * 0.5f, h * 0.58f), w * 0.06f, Qn.Jade, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("hqhafs1", 0, 0), Offset(w * 0.5f, h * 0.74f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            plain97(Offset(w * 0.5f, h * 0.4f), w * 0.1f, a, "hqhafs2", 1, f)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.6f + k / 3f) % 1f)
                val r = w * (0.12f + 0.12f * ph)
                drawCircle(Qn.GoldLight, r, Offset(w * 0.5f, h * 0.4f), alpha = 0.5f * f * (1f - ph) * seg(p, 0.2f, 0.35f), style = Stroke(w * 0.005f))
            }
            tagAt(a.tag("hqhafs2", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil127(f)
            for (i in 0 until 3) {
                val x = w * (0.78f - 0.28f * i)
                val q = seg(p, 0.05f + 0.25f * i, 0.2f + 0.25f * i)
                if (i == 2) close97(Offset(x, h * 0.44f), w * 0.085f, a, "hqhafs3", i, 3, f * q) else plain97(Offset(x, h * 0.44f), w * 0.085f, a, "hqhafs3", i, f * q)
                if (i > 0) drawLine(Qn.GoldLight, Offset(x + w * 0.19f, h * 0.44f), Offset(x + w * 0.1f, h * 0.44f), w * 0.006f, alpha = f * q, cap = StrokeCap.Round)
            }
        },
    )
}

// ═════════════ hq-maratib ═════════════
internal fun DrawScope.hqmaratibScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "hqmaratib", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night127(a) },
        { p, f ->
            veil127(f)
            for (i in 0 until 3) wave127(h * (0.32f + 0.16f * i), listOf(0.6f, 2.2f, 1.2f)[i], h * 0.03f, Qn.GoldLight, f * seg(p, 0.1f + 0.2f * i, 0.25f + 0.2f * i), a.t)
            for (i in 0 until 3) tagAt(a.tag("hqmaratib0", 0, i), Offset(w * 0.5f, h * (0.25f + 0.16f * i)), f * seg(p, 0.1f + 0.2f * i, 0.25f + 0.2f * i), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil127(f)
            wave127(h * 0.5f, 0.6f, h * 0.05f, Qn.GoldLight, f, a.t, reveal = easeInOut(seg(p, 0.05f, 0.7f)))
            qIcon("heart", Offset(w * 0.5f, h * 0.3f), w * 0.07f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("hqmaratib1", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil127(f)
            wave127(h * 0.38f, 2.2f, h * 0.03f, Qn.GoldLight, f, a.t, reveal = easeInOut(seg(p, 0.05f, 0.4f)))
            wave127(h * 0.6f, 1.2f, h * 0.04f, Qn.Jade, f * seg(p, 0.45f, 0.6f), a.t, reveal = easeInOut(seg(p, 0.45f, 0.85f)))
            tagAt(a.tag("hqmaratib2", 0, 0), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("hqmaratib2", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil127(f)
            for (i in 0 until 3) {
                val y = h * (0.3f + 0.17f * i)
                drawCircle(Qn.Gold, w * 0.03f * (1f - 0.2f * i), Offset(w * 0.78f, y), alpha = f * seg(p, 0.1f + 0.2f * i, 0.25f + 0.2f * i))
                tagAt(a.tag("hqmaratib3", 0, i), Offset(w * 0.45f, y), f * seg(p, 0.1f + 0.2f * i, 0.25f + 0.2f * i), ink = if (i == 0) ArtInk.Gold else ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ hq-arkan ═════════════
internal fun DrawScope.hqarkanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "hqarkan", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night127(a) },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.14f, Fig127, f)
            qIcon("book", Offset(w * 0.72f, g - h * 0.26f), w * 0.07f, Qn.Gold, f, a.t)
            person99(Offset(w * 0.4f, g), h * 0.12f, Fig127, f)
            for (k in 0 until 3) {
                val ph = ((a.t * 0.6f + k / 3f) % 1f)
                drawArc(Qn.GoldLight, 140f, 80f, false, Offset(mix(w * 0.64f, w * 0.48f, ph) - w * 0.04f, g - h * 0.17f - w * 0.04f), Size(w * 0.08f, w * 0.08f), alpha = f * (1f - ph) * seg(p, 0.2f, 0.35f), style = Stroke(w * 0.006f))
            }
            qIcon("book", Offset(w * 0.15f, g - h * 0.06f), w * 0.07f, Dim127, f * seg(p, 0.5f, 0.6f), a.t)
            qIcon("cross", Offset(w * 0.15f, g - h * 0.16f), w * 0.04f, Qn.Rose, f * seg(p, 0.6f, 0.7f), a.t)
            tagAt(a.tag("hqarkan0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil127(f)
            qIcon("ear", Offset(w * 0.5f, h * 0.4f), w * 0.14f, Fig127, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("hqarkan1", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil127(f)
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                val q = easeInOut(seg(p, 0.1f + 0.22f * i, 0.3f + 0.22f * i))
                drawRoundRect(Qn.ParchLow, Offset(x - w * 0.05f, h * 0.62f - h * 0.3f * q), Size(w * 0.1f, h * 0.3f * q), CornerRadius(w * 0.01f), alpha = f)
            }
            drawRect(Qn.Gold, Offset(w * 0.12f, h * 0.29f), Size(w * 0.76f, h * 0.03f), alpha = f * seg(p, 0.75f, 0.85f))
            row127(a, "hqarkan2", 3, h * 0.72f, p, f, from = 0.12f, step = 0.22f)
        },
        { p, f ->
            person99(Offset(w * 0.5f, g), h * 0.13f, Dim127, f, lean = 0.1f * sin(a.t * 2f))
            qIcon("book", Offset(w * 0.5f, g - h * 0.25f), w * 0.07f, Dim127, f, a.t)
            for (k in 0 until 3) qIcon("point", Offset(w * (0.4f + 0.1f * k), h * 0.36f), w * 0.03f, Qn.GoldLight, f * seg(p, 0.2f + 0.1f * k, 0.3f + 0.1f * k), a.t)
            tagAt(a.tag("hqarkan3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ hq-lahn ═════════════
internal fun DrawScope.hqlahnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "hqlahn", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night127(a) },
        { p, f ->
            veil127(f)
            row127(a, "hqlahn0", 2, h * 0.45f, p, f, from = 0.3f, step = 0.3f)
        },
        { p, f ->
            veil127(f)
            for (k in 0 until 7) {
                val bad = k == 3
                val c = Offset(w * (0.8f - 0.1f * k), h * 0.42f)
                drawRoundRect(if (bad) Qn.Rose else Qn.GoldLight, Offset(c.x - w * 0.035f, c.y - h * 0.04f), Size(w * 0.07f, h * 0.08f), CornerRadius(w * 0.01f), alpha = f * (if (bad) seg(p, 0.3f, 0.45f) else 0.8f))
            }
            tagAt(a.tag("hqlahn1", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil127(f)
            wave127(h * 0.42f, 1.0f, h * 0.04f, Qn.GoldLight, f, a.t)
            drawCircle(Qn.Rose, w * 0.03f, Offset(w * 0.5f, h * 0.42f), alpha = 0.6f * f * seg(p, 0.35f, 0.5f), style = Stroke(w * 0.005f))
            tagAt(a.tag("hqlahn2", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil127(f)
            for (k in 0 until 3) {
                person99(Offset(w * (0.75f - 0.25f * k), h * 0.75f), h * 0.12f, Fig127, f)
                qIcon("ear", Offset(w * (0.75f - 0.25f * k), h * 0.48f), w * 0.06f, if (k == 1) Qn.GoldLight else Dim127, f * (if (k == 1) seg(p, 0.25f, 0.4f) else 0.5f), a.t)
            }
            tagAt(a.tag("hqlahn3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ hq-waqf ═════════════
internal fun DrawScope.hqwaqfScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "hqwaqf", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night127(a) },
        { p, f ->
            veil127(f)
            val cut = seg(p, 0.3f, 0.4f)
            wave127(h * 0.42f, 1.0f, h * 0.04f, Qn.GoldLight, f, a.t, reveal = mix(easeInOut(seg(p, 0.05f, 0.3f)) * 0.45f, 0.45f, cut))
            for (k in 0 until 3) {
                val ph = ((a.t * 0.5f + k / 3f) % 1f)
                drawCircle(Color(0xFFDCEEFA), w * (0.01f + 0.02f * ph), Offset(w * 0.5f - w * 0.05f * ph, h * 0.42f - h * 0.08f * ph), alpha = 0.6f * f * (1f - ph) * seg(p, 0.4f, 0.5f))
            }
            wave127(h * 0.42f, 1.0f, h * 0.04f, Qn.GoldLight, f * seg(p, 0.65f, 0.75f), a.t, reveal = 0.45f * easeInOut(seg(p, 0.65f, 0.9f)))
            tagAt(a.tag("hqwaqf0", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil127(f)
            row127(a, "hqwaqf1", 3, h * 0.45f, p, f, from = 0.1f, step = 0.22f)
        },
        { p, f ->
            val g = h * 0.8f
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig127, f, lean = 0.2f * seg(p, 0.3f, 0.4f) * (1f - seg(p, 0.5f, 0.6f)))
            for (k in 0 until 5) drawCircle(Color(0xFFDCEEFA), w * 0.012f, Offset(w * (0.44f - 0.03f * k), g - h * 0.17f + h * 0.01f * k), alpha = 0.6f * f * seg(p, 0.35f, 0.45f) * (1f - seg(p, 0.55f, 0.65f)))
            tagAt(a.tag("hqwaqf2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil127(f)
            for (k in 0 until 2) drawRoundRect(if (k == 0) Qn.Gold else Qn.Jade, Offset(w * (0.6f - 0.3f * k), h * 0.4f), Size(w * 0.22f, h * 0.08f), CornerRadius(w * 0.02f), alpha = f)
            drawLine(Qn.GoldLight, Offset(w * 0.6f, h * 0.44f), Offset(w * 0.52f, h * 0.44f), w * 0.012f, alpha = f * seg(p, 0.2f, 0.35f), cap = StrokeCap.Round)
            qIcon("cross", Offset(w * 0.56f, h * 0.32f), w * 0.05f, Qn.Rose, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("hqwaqf3", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ hq-basmala ═════════════
internal fun DrawScope.hqbasmalaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "hqbasmala", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night127(a) },
        { p, f ->
            veil127(f)
            blocks127(h * 0.45f, 0f, 0f, f * seg(p, 0.05f, 0.25f))
            row127(a, "hqbasmala0", 1, h * 0.62f, p, f, from = 0.3f)
        },
        { p, f ->
            veil127(f)
            blocks127(h * 0.26f, 0f, 0f, f * seg(p, 0.05f, 0.15f))
            blocks127(h * 0.46f, 0f, 1f, f * seg(p, 0.3f, 0.4f))
            blocks127(h * 0.66f, 1f, 1f, f * seg(p, 0.55f, 0.65f))
            for (i in 0 until 3) tagAt(a.tag("hqbasmala1", 0, i), Offset(w * 0.5f, h * (0.18f + 0.2f * i)), f * seg(p, 0.08f + 0.25f * i, 0.18f + 0.25f * i), ink = ArtInk.Gold)
        },
        { p, f ->
            veil127(f)
            blocks127(h * 0.46f, 1f, 0f, f, cross = seg(p, 0.35f, 0.5f), t = a.t)
            tagAt(a.tag("hqbasmala2", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil127(f)
            drawRoundRect(Qn.Lapis, Offset(w * 0.12f, h * 0.4f), Size(w * 0.76f, h * 0.09f), CornerRadius(w * 0.02f), alpha = f)
            drawCircle(Qn.GoldLight, w * 0.02f, Offset(w * 0.5f, h * 0.445f), alpha = f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("hqbasmala3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}
