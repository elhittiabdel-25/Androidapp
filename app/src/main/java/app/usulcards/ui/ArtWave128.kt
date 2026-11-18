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
 * Wave 128 (2026-11-18): al-'Abbad, Qutf al-Jana al-Dani: the creed of the fitra (the girl who answered «in the
 * heaven», faceless), neither ta'wil nor tafwid (Malik a plain roundel), the deniers who end in likening (the palm
 * stripped of everything a palm is, the stone that greeted), the theologians' regret (al-Razi, al-Juwayni and
 * al-Ghazali plain roundels), Adam and Musa as named lights, the debate on the decree (the donkey only named),
 * prophet and messenger as lights, and the two nations. The Prophet ﷺ is only his named light.
 */

private val Fig128 = Color(0xFFEDE4D0)
private val Dim128 = Color(0xFF8A8296)

private fun DrawScope.night128(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil128(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row128(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             first: Int = 0, ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, first + i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

/** A prophet other than ﷺ: a named light. */
private fun DrawScope.light128(c: Offset, r: Float, a: ArtCtx, kind: String, part: Int, alpha: Float, cool: Boolean = false) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 3f, if (cool) Color(0xFFCFE6F2) else Qn.GoldLight, 0.7f * alpha)
    drawCircle(Color(0xFFF8F4E8), r, c, alpha = alpha)
    if (part >= 0) tagAt(a.tag(kind, 0, part), Offset(c.x, c.y - r * 3.2f), alpha, ink = ArtInk.Ivory)
}

private fun DrawScope.book128(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, Offset(c.x - s, c.y - s * 0.65f), Size(s * 2f, s * 1.3f), CornerRadius(s * 0.12f), alpha = alpha)
    drawLine(Color(0xFF2A2140), Offset(c.x, c.y - s * 0.6f), Offset(c.x, c.y + s * 0.6f), s * 0.08f, alpha = alpha)
}

// ═════════════ ab-fitra ═════════════
internal fun DrawScope.abfitraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "abfitra", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night128(a) },
        { p, f ->
            val grow = easeInOut(seg(p, 0.05f, 0.6f))
            drawLine(Qn.Jade, Offset(w * 0.5f, g), Offset(w * 0.5f, g - h * 0.32f * grow), w * 0.012f, alpha = f, cap = StrokeCap.Round)
            for (k in 0 until 3) {
                val y = g - h * (0.08f + 0.09f * k) * grow
                val sgn = if (k % 2 == 0) 1f else -1f
                drawLine(Qn.Jade, Offset(w * 0.5f, y), Offset(w * 0.5f + sgn * w * 0.07f * grow, y - h * 0.04f * grow), w * 0.009f, alpha = f, cap = StrokeCap.Round)
            }
            softGlow(Offset(w * 0.5f, h * 0.12f), w * 0.12f, Qn.GoldLight, 0.5f * f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("abfitra0", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.75f, h * 0.5f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.75f, h * 0.38f))
            woman12(Offset(w * 0.32f, g), h * 0.15f, Fig128, f)
            val up = seg(p, 0.35f, 0.55f)
            drawLine(Qn.GoldLight, Offset(w * 0.32f, g - h * 0.2f), Offset(w * 0.32f, g - h * (0.2f + 0.22f * up)), w * 0.006f, alpha = f * up, cap = StrokeCap.Round)
            tagAt(a.tag("abfitra1", 0, 0), Offset(w * 0.32f, h * 0.2f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            tagAt(a.tag("abfitra1", 0, 1), Offset(w * 0.5f, h * 0.9f), f * seg(p, 0.7f, 0.82f), ink = ArtInk.Ivory)
        },
        { p, f ->
            woman12(Offset(w * 0.5f, g), h * 0.16f, Fig128, f)
            for (k in 0 until 5) {
                val ang = -Math.PI.toFloat() / 2f + (k - 2) * 0.22f
                val q = seg(p, 0.15f + 0.06f * k, 0.4f + 0.06f * k)
                val o = Offset(w * 0.5f, g - h * 0.22f)
                drawLine(Qn.GoldLight, o, Offset(o.x + cos(ang) * h * 0.3f * q, o.y + sin(ang) * h * 0.3f * q), w * 0.004f, alpha = 0.7f * f * q, cap = StrokeCap.Round)
            }
            tagAt(a.tag("abfitra2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
            tagAt(a.tag("abfitra2", 0, 1), Offset(w * 0.5f, h * 0.9f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
        { p, f ->
            child12(Offset(w * 0.7f, g), h * 0.11f, Fig128, f)
            book128(Offset(w * 0.7f, g - h * 0.16f), w * 0.04f, Qn.Gold, f * seg(p, 0.1f, 0.25f))
            person99(Offset(w * 0.3f, g), h * 0.14f, Fig128, f * seg(p, 0.3f, 0.45f))
            row128(a, "abfitra3", 2, h * 0.36f, p, f, from = 0.15f, step = 0.25f)
        },
    )
}

// ═════════════ ab-tafwid ═════════════
internal fun DrawScope.abtafwidScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "abtafwid", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night128(a) },
        { p, f ->
            veil128(f)
            row128(a, "abtafwid0", 3, h * 0.42f, p, f, from = 0.3f, step = 0.18f)
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                qIcon("cross", Offset(x, h * 0.56f), w * 0.04f, Qn.Rose, f * seg(p, 0.38f + 0.18f * i, 0.5f + 0.18f * i), a.t)
            }
            qIcon("check", Offset(w * 0.5f, h * 0.24f), w * 0.06f, Qn.Jade, f * seg(p, 0.05f, 0.2f), a.t)
        },
        { p, f ->
            veil128(f)
            plain97(Offset(w * 0.76f, h * 0.45f), w * 0.1f, a, "abtafwid1", 0, f * seg(p, 0.02f, 0.15f))
            for (i in 0 until 4) tagAt(a.tag("abtafwid1", 0, 1 + i), Offset(w * 0.36f, h * (0.24f + 0.14f * i)), f * seg(p, 0.15f + 0.18f * i, 0.27f + 0.18f * i),
                ink = if (i == 0) ArtInk.Gold else ArtInk.Ivory)
        },
        { p, f ->
            veil128(f)
            drawRoundRect(Qn.Gold, Offset(w * 0.58f, h * 0.36f), Size(w * 0.32f, h * 0.16f), CornerRadius(w * 0.03f), alpha = 0.85f * f * seg(p, 0.05f, 0.2f))
            qIcon("check", Offset(w * 0.74f, h * 0.29f), w * 0.04f, Qn.Jade, f * seg(p, 0.2f, 0.32f), a.t)
            tagAt(a.tag("abtafwid2", 0, 0), Offset(w * 0.74f, h * 0.6f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            drawRoundRect(Dim128, Offset(w * 0.1f, h * 0.36f), Size(w * 0.32f, h * 0.16f), CornerRadius(w * 0.03f), alpha = 0.6f * f * seg(p, 0.45f, 0.6f))
            qIcon("cloud", Offset(w * 0.26f, h * 0.44f), w * 0.07f, Color(0xFFDCE2EE), f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("abtafwid2", 0, 1), Offset(w * 0.26f, h * 0.6f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil128(f)
            row128(a, "abtafwid3", 3, h * 0.45f, p, f, from = 0.15f, step = 0.2f)
            for (i in 0 until 3) qIcon("cross", Offset(w * (0.5f + (1 - i) * 0.28f), h * 0.58f), w * 0.04f, Qn.Rose, f * seg(p, 0.25f + 0.2f * i, 0.35f + 0.2f * i), a.t)
        },
    )
}

// ═════════════ ab-tatil ═════════════
internal fun DrawScope.abtatilScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "abtatil", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night128(a) },
        { p, f ->
            for (k in 0 until 3) person99(Offset(w * (0.3f + 0.2f * k), g), h * 0.12f, Fig128, f * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k))
            drawRoundRect(Qn.GoldLight, Offset(w * 0.18f, g - h * 0.2f), Size(w * 0.64f, h * 0.22f), CornerRadius(w * 0.03f), alpha = f * seg(p, 0.35f, 0.5f), style = Stroke(w * 0.005f))
            tagAt(a.tag("abtatil0", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil128(f)
            val fade = seg(p, 0.2f, 0.6f)
            drawCircle(Qn.Gold, w * 0.11f, Offset(w * 0.5f, h * 0.42f), alpha = f * (1f - fade))
            for (k in 0 until 16) {
                val ang = k / 16f * 6.283f
                drawCircle(Dim128, w * 0.006f, Offset(w * 0.5f + cos(ang) * w * 0.11f, h * 0.42f + sin(ang) * w * 0.11f), alpha = f * fade)
            }
            tagAt(a.tag("abtatil1", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            val stone = Offset(w * 0.3f, g - h * 0.05f)
            drawOval(Color(0xFF6E6478), Offset(stone.x - w * 0.09f, stone.y - h * 0.05f), Size(w * 0.18f, h * 0.1f), alpha = f)
            sirajMunir(Offset(w * 0.72f, h * 0.48f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.72f, h * 0.36f))
            for (k in 0 until 3) {
                val ph = ((a.t * 0.5f + k / 3f) % 1f)
                val x = mix(w * 0.4f, w * 0.62f, ph)
                drawArc(Qn.GoldLight, -40f, 80f, false, Offset(x - w * 0.03f, mix(g - h * 0.08f, h * 0.5f, ph) - w * 0.03f), Size(w * 0.06f, w * 0.06f),
                    alpha = f * (1f - ph) * seg(p, 0.25f, 0.4f), style = Stroke(w * 0.005f))
            }
            tagAt(a.tag("abtatil2", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            tagAt(a.tag("abtatil2", 0, 1), Offset(w * 0.3f, h * 0.9f), f * seg(p, 0.7f, 0.82f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val gone = seg(p, 0.25f, 0.7f)
            palm12(Offset(w * 0.5f, g), h * 0.34f, Color(0xFF7A5A3A), Qn.Jade, f * (1f - gone), a.t)
            drawRoundRect(Dim128, Offset(w * 0.47f, g - h * 0.34f), Size(w * 0.06f, h * 0.34f), CornerRadius(w * 0.02f), alpha = 0.6f * f * gone * (1f - seg(p, 0.75f, 0.9f)), style = Stroke(w * 0.004f))
            tagAt(a.tag("abtatil3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.7f, 0.85f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ab-hayra ═════════════
internal fun DrawScope.abhayraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "abhayra", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night128(a) },
        { p, f ->
            val path = Path()
            val n = 60
            for (i in 0..n) {
                val u = i / n.toFloat()
                val x = w * (0.08f + 0.36f * u) + w * 0.06f * sin(u * 31f)
                val y = h * 0.45f + h * 0.14f * sin(u * 17f + 1f)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path, Dim128, alpha = f * seg(p, 0.05f, 0.3f), style = Stroke(w * 0.004f))
            person99(Offset(w * 0.26f, g), h * 0.11f, Dim128, f, lean = 0.12f * sin(a.t * 1.5f))
            drawLine(Qn.Gold, Offset(w * 0.75f, g), Offset(w * 0.75f, h * 0.24f), w * 0.01f, alpha = f * seg(p, 0.15f, 0.35f), cap = StrokeCap.Round)
            book128(Offset(w * 0.75f, h * 0.2f), w * 0.04f, Qn.Gold, f * seg(p, 0.25f, 0.4f))
            tagAt(a.tag("abhayra0", 0, 0), Offset(w * 0.26f, h * 0.24f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil128(f)
            plain97(Offset(w * 0.5f, h * 0.3f), w * 0.09f, a, "abhayra1", 0, f * seg(p, 0.02f, 0.15f))
            drawLine(Dim128, Offset(w * 0.12f, h * 0.55f), Offset(w * 0.42f, h * 0.55f), w * 0.006f, alpha = f * seg(p, 0.15f, 0.3f))
            qIcon("cross", Offset(w * 0.27f, h * 0.55f), w * 0.04f, Qn.Rose, f * seg(p, 0.3f, 0.42f), a.t)
            book128(Offset(w * 0.72f, h * 0.56f), w * 0.05f, Qn.Gold, f * seg(p, 0.6f, 0.72f))
            softGlow(Offset(w * 0.72f, h * 0.56f), w * 0.12f, Qn.GoldLight, 0.5f * f * seg(p, 0.65f, 0.8f))
            tagAt(a.tag("abhayra1", 0, 1), Offset(w * 0.72f, h * 0.72f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil128(f)
            plain97(Offset(w * 0.72f, h * 0.4f), w * 0.09f, a, "abhayra2", 0, f * seg(p, 0.02f, 0.15f))
            woman12(Offset(w * 0.3f, h * 0.72f), h * 0.15f, Fig128, f * seg(p, 0.4f, 0.55f))
            softGlow(Offset(w * 0.3f, h * 0.58f), w * 0.1f, Qn.GoldLight, 0.4f * f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("abhayra2", 0, 1), Offset(w * 0.3f, h * 0.84f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil128(f)
            plain97(Offset(w * 0.5f, h * 0.36f), w * 0.1f, a, "abhayra3", 0, f * seg(p, 0.02f, 0.15f))
            book128(Offset(w * 0.5f, h * 0.56f), w * 0.05f, Qn.Gold, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("abhayra3", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ab-adam ═════════════
internal fun DrawScope.abadamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "abadam", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night128(a) },
        { p, f ->
            veil128(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.42f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.42f), w * 0.12f, Qn.Rose, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("abadam0", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val gp = seg(p, 0.4f, 0.8f)
            tree104(Offset(w * 0.5f, mix(h * 0.5f, h * 0.36f, gp)), h * 0.16f * (1f - 0.4f * gp), Qn.Jade, Qn.Gold, f * (1f - 0.6f * gp))
            light128(Offset(w * 0.76f, h * 0.62f), w * 0.03f, a, "abadam1", 0, f * seg(p, 0.02f, 0.15f))
            light128(Offset(w * 0.24f, h * 0.62f), w * 0.03f, a, "abadam1", 1, f * seg(p, 0.1f, 0.25f), cool = true)
            tagAt(a.tag("abadam1", 0, 2), Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil128(f)
            drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.24f), Offset(w * 0.5f, h * 0.36f), w * 0.008f, alpha = f)
            drawLine(Qn.GoldLight, Offset(w * 0.22f, h * 0.36f), Offset(w * 0.78f, h * 0.36f), w * 0.008f, alpha = f, cap = StrokeCap.Round)
            for (i in 0 until 2) {
                val x = w * (0.72f - 0.44f * i)
                drawArc(Qn.Gold, 0f, 180f, false, Offset(x - w * 0.1f, h * 0.38f), Size(w * 0.2f, h * 0.1f), alpha = f, style = Stroke(w * 0.007f))
                tagAt(a.tag("abadam2", 0, i), Offset(x, h * 0.58f), f * seg(p, 0.15f + 0.35f * i, 0.3f + 0.35f * i), ink = ArtInk.Ivory)
                qIcon(if (i == 0) "check" else "cross", Offset(x, h * 0.7f), w * 0.04f, if (i == 0) Qn.Jade else Qn.Rose, f * seg(p, 0.25f + 0.35f * i, 0.4f + 0.35f * i), a.t)
            }
        },
        { p, f ->
            val g = h * 0.8f
            person99(Offset(w * 0.72f, g), h * 0.13f, Fig128, f)
            qIcon("drop", Offset(w * 0.72f, g - h * 0.26f), w * 0.035f, Color(0xFFCFE6F2), f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("check", Offset(w * 0.72f, h * 0.3f), w * 0.04f, Qn.Jade, f * seg(p, 0.25f, 0.4f), a.t)
            tagAt(a.tag("abadam3", 0, 0), Offset(w * 0.72f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            person99(Offset(w * 0.28f, g), h * 0.13f, Dim128, f * seg(p, 0.5f, 0.6f))
            qSmoke(Offset(w * 0.28f, g - h * 0.12f), Offset(w * 0.28f, g - h * 0.32f), seg(p, 0.6f, 0.9f), a.t, f, w * 0.03f)
            tagAt(a.tag("abadam3", 0, 1), Offset(w * 0.28f, h * 0.2f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ab-munazara ═════════════
internal fun DrawScope.abmunazaraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "abmunazara", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night128(a) },
        { p, f ->
            veil128(f)
            plain97(Offset(w * 0.72f, h * 0.4f), w * 0.09f, a, "abmunazara0", 0, f * seg(p, 0.02f, 0.15f))
            tagAt(a.tag("abmunazara0", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil128(f)
            plain97(Offset(w * 0.72f, h * 0.3f), w * 0.07f, a, "abmunazara0", 0, 0.6f * f)
            plain97(Offset(w * 0.28f, h * 0.3f), w * 0.09f, a, "abmunazara1", 0, f * seg(p, 0.02f, 0.15f))
            tagAt(a.tag("abmunazara1", 0, 1), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("abmunazara1", 0, 2), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil128(f)
            plain97(Offset(w * 0.72f, h * 0.28f), w * 0.07f, a, "abmunazara0", 0, f)
            plain97(Offset(w * 0.28f, h * 0.28f), w * 0.07f, a, "abmunazara1", 0, f)
            for (i in 0 until 2) {
                val x = w * (0.68f - 0.36f * i)
                qIcon(if (i == 0) "coin" else "scale", Offset(x, h * 0.54f), w * 0.07f, Qn.Gold, f * seg(p, 0.45f + 0.2f * i, 0.58f + 0.2f * i), a.t)
                tagAt(a.tag("abmunazara2", 0, i), Offset(x, h * 0.7f), f * seg(p, 0.45f + 0.2f * i, 0.58f + 0.2f * i), ink = ArtInk.Gold)
            }
        },
        { p, f ->
            val g = h * 0.8f
            person99(Offset(w * 0.7f, g), h * 0.14f, Fig128, f)
            plain97(Offset(w * 0.3f, h * 0.48f), w * 0.08f, a, "abmunazara3", 0, f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("abmunazara3", 0, 1), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ab-nabi ═════════════
internal fun DrawScope.abnabiScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "abnabi", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night128(a) },
        { p, f ->
            light128(Offset(w * 0.72f, h * 0.42f), w * 0.03f, a, "abnabi0", 1, f * seg(p, 0.35f, 0.5f))
            for (k in 0 until 3) {
                val q = seg(p, 0.5f + 0.1f * k, 0.65f + 0.1f * k)
                val to = Offset(w * (0.6f + 0.12f * k), g - h * 0.14f)
                drawLine(Qn.GoldLight, Offset(w * 0.72f, h * 0.46f), Offset(mix(w * 0.72f, to.x, q), mix(h * 0.46f, to.y, q)), w * 0.004f, alpha = f * q)
                person99(Offset(w * (0.6f + 0.12f * k), g), h * 0.1f, Fig128, f * q)
            }
            light128(Offset(w * 0.28f, h * 0.42f), w * 0.03f, a, "abnabi0", 0, f * seg(p, 0.05f, 0.2f))
        },
        { p, f ->
            book128(Offset(w * 0.5f, h * 0.26f), w * 0.05f, Qn.Lapis, f * seg(p, 0.05f, 0.2f))
            for (k in 0 until 4) light128(Offset(w * (0.2f + 0.2f * k), h * 0.48f), w * 0.022f, a, "abnabi1", -1, f * seg(p, 0.15f + 0.08f * k, 0.3f + 0.08f * k))
            for (k in 0 until 5) person99(Offset(w * (0.15f + 0.175f * k), g), h * 0.09f, Fig128, f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("abnabi1", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil128(f)
            light128(Offset(w * 0.72f, h * 0.4f), w * 0.03f, a, "abnabi2", 0, f * seg(p, 0.02f, 0.15f))
            book128(Offset(w * 0.72f, h * 0.58f), w * 0.05f, Qn.Gold, f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("abnabi2", 0, 1), Offset(w * 0.72f, h * 0.72f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            light128(Offset(w * 0.28f, h * 0.4f), w * 0.03f, a, "abnabi2", 2, f * seg(p, 0.5f, 0.62f))
            book128(Offset(w * 0.28f, h * 0.58f), w * 0.05f, Dim128, f * seg(p, 0.6f, 0.72f))
            tagAt(a.tag("abnabi2", 0, 3), Offset(w * 0.28f, h * 0.72f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil128(f)
            val off = seg(p, 0.5f, 0.75f)
            drawLine(Qn.GoldLight, Offset(w * 0.12f, h * 0.42f), Offset(w * 0.88f, h * 0.42f), w * 0.004f, alpha = f * (1f - 0.7f * off))
            for (k in 0 until 6) light128(Offset(w * (0.12f + 0.152f * k), h * 0.42f), w * 0.022f, a, "abnabi3", -1, f * seg(p, 0.05f + 0.05f * k, 0.15f + 0.05f * k) * (1f - 0.75f * off))
            tagAt(a.tag("abnabi3", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ab-umma ═════════════
internal fun DrawScope.abummaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "abumma", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night128(a) },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, mix(h * 0.7f, h * 0.42f, easeInOut(seg(p, 0.05f, 0.5f)))), w * 0.035f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.28f))
            tagAt(a.tag("abumma0", 0, 0), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("abumma0", 0, 1), Offset(w * 0.5f, h * 0.9f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.3f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.18f))
            val spread = easeInOut(seg(p, 0.1f, 0.6f))
            drawArc(Qn.GoldLight, 20f, 140f, false, Offset(w * 0.5f - w * 0.45f * spread, h * 0.3f - h * 0.3f * spread), Size(w * 0.9f * spread, h * 0.6f * spread),
                alpha = 0.5f * f, style = Stroke(w * 0.004f))
            for (k in 0 until 7) person99(Offset(w * (0.08f + 0.14f * k), g), h * 0.09f, Fig128, f * seg(p, 0.3f + 0.04f * k, 0.45f + 0.04f * k))
            tagAt(a.tag("abumma1", 0, 0), Offset(w * 0.5f, h * 0.52f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("abumma1", 0, 1), Offset(w * 0.5f, h * 0.9f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.3f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.18f))
            for (k in 0 until 7) {
                val lit = k % 2 == 0
                val x = w * (0.08f + 0.14f * k)
                person99(Offset(x, g), h * 0.09f, if (lit) Fig128 else Dim128, f * (if (lit) 1f else 0.5f))
                if (lit) softGlow(Offset(x, g - h * 0.06f), w * 0.05f, Qn.GoldLight, 0.5f * f * seg(p, 0.3f + 0.05f * k, 0.45f + 0.05f * k))
            }
            tagAt(a.tag("abumma2", 0, 0), Offset(w * 0.5f, h * 0.52f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil128(f)
            book128(Offset(w * 0.5f, h * 0.32f), w * 0.07f, Qn.Gold, f * seg(p, 0.05f, 0.2f))
            softGlow(Offset(w * 0.5f, h * 0.32f), w * 0.16f, Qn.GoldLight, 0.4f * f * seg(p, 0.2f, 0.4f))
            book128(Offset(w * 0.3f, h * 0.56f), w * 0.045f, Dim128, f * seg(p, 0.3f, 0.45f))
            book128(Offset(w * 0.7f, h * 0.56f), w * 0.045f, Dim128, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("abumma3", 0, 0), Offset(w * 0.5f, h * 0.74f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}
