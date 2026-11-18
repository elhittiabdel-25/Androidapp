package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.sin

/*
 * Wave 130 (2026-11-18): al-Lahim, keys to pondering the Quran: the heart opened, love of the Quran (the child who
 * refuses the larger sum), the five aims, reading in prayer, by night, the portion kept and made up, a verse
 * repeated until dawn, and reciting aloud (Abu Bakr and 'Umar their companion roundels, 'A'isha hers, al-Hasan his
 * family roundel). The Prophet ﷺ is only his named light; people are faceless; no verse is written.
 */

private val Fig130 = Color(0xFFEDE4D0)
private val Dim130 = Color(0xFF8A8296)

private fun DrawScope.night130(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil130(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

private fun DrawScope.row130(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             first: Int = 0, ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, first + i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

private fun DrawScope.book130(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, Offset(c.x - s, c.y - s * 0.65f), Size(s * 2f, s * 1.3f), CornerRadius(s * 0.12f), alpha = alpha)
    drawLine(Color(0xFF2A2140), Offset(c.x, c.y - s * 0.6f), Offset(c.x, c.y + s * 0.6f), s * 0.08f, alpha = alpha)
}

/** Sound going out from `c`: `loud` sets how far the rings reach. */
private fun DrawScope.voice130(c: Offset, loud: Float, alpha: Float, t: Float) {
    val w = size.width
    for (k in 0 until 3) {
        val ph = ((t * 0.6f + k / 3f) % 1f)
        val r = w * (0.03f + 0.12f * loud * ph)
        drawCircle(Qn.GoldLight, r, c, alpha = 0.6f * alpha * (1f - ph), style = Stroke(w * 0.004f))
    }
}

// ═════════════ mt-qalb ═════════════
internal fun DrawScope.mtqalbScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mtqalb", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night130(a) },
        { p, f ->
            veil130(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.42f), w * 0.14f, Qn.Rose, f * seg(p, 0.05f, 0.2f), a.t)
            book130(Offset(w * 0.5f, h * 0.22f), w * 0.05f, Qn.Gold, f * seg(p, 0.45f, 0.6f))
            drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.27f), Offset(w * 0.5f, h * 0.34f), w * 0.005f, alpha = f * seg(p, 0.55f, 0.7f))
            tagAt(a.tag("mtqalb0", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil130(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.46f), w * 0.14f, Qn.Rose, f, a.t)
            qIcon("lock", Offset(w * 0.5f, h * 0.46f), w * 0.06f, Dim130, f * (1f - seg(p, 0.4f, 0.55f)), a.t)
            softGlow(Offset(w * 0.5f, h * 0.46f), w * 0.15f, Qn.GoldLight, 0.6f * f * seg(p, 0.5f, 0.7f))
            tagAt(a.tag("mtqalb1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig130, f)
            qIcon("hands", Offset(w * 0.5f, g - h * 0.27f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("mtqalb2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.13f, Fig130, f)
            qIcon("hands", Offset(w * 0.72f, g - h * 0.26f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            person99(Offset(w * 0.28f, g), h * 0.13f, Fig130, f * seg(p, 0.45f, 0.55f))
            book130(Offset(w * 0.28f, g - h * 0.2f), w * 0.04f, Qn.Gold, f * seg(p, 0.5f, 0.62f))
            row130(a, "mtqalb3", 2, h * 0.3f, p, f, from = 0.1f, step = 0.4f)
        },
    )
}

// ═════════════ mt-hubb ═════════════
internal fun DrawScope.mthubbScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mthubb", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night130(a) },
        { p, f ->
            veil130(f)
            book130(Offset(w * 0.5f, h * 0.44f), w * 0.07f, Qn.Gold, f * seg(p, 0.02f, 0.15f))
            for (k in 0 until 3) {
                val ph = ((a.t * 0.4f + k / 3f) % 1f)
                qIcon("heart", Offset(w * (0.42f + 0.08f * k), h * 0.36f - h * 0.16f * ph), w * 0.04f, Qn.Rose, f * (1f - ph) * seg(p, 0.2f, 0.35f), a.t)
            }
            tagAt(a.tag("mthubb0", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil130(f)
            book130(Offset(w * 0.5f, h * 0.3f), w * 0.05f, Qn.Gold, f)
            row130(a, "mthubb1", 3, h * 0.56f, p, f, from = 0.15f, step = 0.22f)
        },
        { p, f ->
            child12(Offset(w * 0.5f, g), h * 0.12f, Fig130, f)
            for (k in 0 until 5) qIcon("coin", Offset(w * (0.7f + 0.03f * (k % 2)), g - h * (0.03f + 0.04f * k)), w * 0.04f, Qn.Gold, f * (1f - 0.5f * seg(p, 0.45f, 0.6f)), a.t)
            qIcon("cross", Offset(w * 0.72f, g - h * 0.26f), w * 0.04f, Qn.Rose, f * seg(p, 0.45f, 0.58f), a.t)
            qIcon("coin", Offset(w * 0.32f, g - h * 0.05f), w * 0.035f, Qn.GoldLight, f * seg(p, 0.6f, 0.72f), a.t)
            tagAt(a.tag("mthubb2", 0, 0), Offset(w * 0.72f, h * 0.3f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
            tagAt(a.tag("mthubb2", 0, 1), Offset(w * 0.3f, h * 0.44f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil130(f)
            for (k in 0 until 4) book130(Offset(w * 0.5f, h * (0.6f - 0.08f * k)), w * 0.08f, if (k % 2 == 0) Qn.Lapis else Qn.Jade, f * seg(p, 0.05f + 0.1f * k, 0.18f + 0.1f * k))
            tagAt(a.tag("mthubb3", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mt-ahdaf ═════════════
internal fun DrawScope.mtahdafScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mtahdaf", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night130(a) },
        { p, f ->
            veil130(f)
            book130(Offset(w * 0.5f, h * 0.42f), w * 0.07f, Qn.Gold, f)
            qIcon("coin", Offset(w * 0.5f, h * 0.24f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            for (k in 0 until 4) drawCircle(Dim130, w * 0.02f, Offset(w * (0.2f + 0.2f * k), h * 0.62f), alpha = 0.5f * f * seg(p, 0.5f, 0.65f), style = Stroke(w * 0.003f))
            tagAt(a.tag("mtahdaf0", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil130(f)
            book130(Offset(w * 0.5f, h * 0.44f), w * 0.07f, Qn.Gold, f)
            for (k in 0 until 4) {
                val ph = ((a.t * 0.35f + k / 4f) % 1f)
                softGlow(Offset(w * (1.05f - 1.1f * ph), h * (0.3f + 0.08f * k)), w * 0.05f, Qn.GoldLight, 0.5f * f * seg(p, 0.35f, 0.5f))
            }
            tagAt(a.tag("mtahdaf1", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil130(f)
            val icons = listOf("coin", "hands", "drop", "book", "check")
            for (i in 0 until 5) {
                val x = w * (0.5f + (2f - i) * 0.168f)
                qIcon(icons[i], Offset(x, h * 0.4f), w * 0.06f, Qn.Gold, f * seg(p, 0.25f + 0.13f * i, 0.36f + 0.13f * i), a.t)
            }
            row130(a, "mtahdaf2", 5, h * 0.56f, p, f, from = 0.25f, step = 0.13f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil130(f)
            qIcon("coin", Offset(w * 0.68f, h * 0.42f), w * 0.07f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("chain", Offset(w * 0.32f, h * 0.42f), w * 0.07f, Qn.Jade, f * seg(p, 0.35f, 0.5f), a.t)
            row130(a, "mtahdaf3", 2, h * 0.6f, p, f, from = 0.15f, step = 0.25f)
        },
    )
}

// ═════════════ mt-qiyam ═════════════
internal fun DrawScope.mtqiyamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mtqiyam", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night130(a) },
        { p, f ->
            qIcon("mat", Offset(w * 0.5f, g + h * 0.02f), w * 0.12f, Qn.Jade, f, a.t)
            person99(Offset(w * 0.5f, g), h * 0.15f, Fig130, f)
            softGlow(Offset(w * 0.5f, g - h * 0.25f), w * 0.1f, Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("mtqiyam0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil130(f)
            book130(Offset(w * 0.7f, h * 0.42f), w * 0.06f, Qn.Gold, f * seg(p, 0.05f, 0.2f))
            qIcon("lock", Offset(w * 0.7f, h * 0.42f), w * 0.04f, Dim130, f * seg(p, 0.25f, 0.35f), a.t)
            for (k in 0 until 4) qIcon("coin", Offset(w * 0.3f, h * (0.48f - 0.05f * k)), w * 0.045f, Qn.Gold, f * seg(p, 0.45f + 0.04f * k, 0.55f + 0.04f * k), a.t)
            qIcon("lock", Offset(w * 0.3f, h * 0.52f), w * 0.04f, Dim130, f * seg(p, 0.65f, 0.75f), a.t)
            tagAt(a.tag("mtqiyam1", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 6) {
                val ph = ((a.t * 0.8f + k / 6f) % 1f)
                drawLine(Dim130, Offset(w * (1f - ph * 1.2f), h * (0.3f + 0.07f * k)), Offset(w * (1.15f - ph * 1.2f), h * (0.3f + 0.07f * k)), w * 0.004f, alpha = 0.6f * f)
            }
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig130, f, lean = 0.06f * sin(a.t * 2f))
            softGlow(Offset(w * 0.5f, g - h * 0.14f), w * 0.12f, Qn.GoldLight, 0.7f * f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("mtqiyam2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.45f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.32f))
            qIcon("mat", Offset(w * 0.5f, g), w * 0.12f, Qn.Jade, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("mtqiyam3", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mt-layl ═════════════
internal fun DrawScope.mtlaylScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mtlayl", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night130(a) },
        { p, f ->
            qIcon("moon", Offset(w * 0.75f, h * 0.2f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            drawRect(Color(0xFF3A2E5A), Offset(0f, h * 0.6f), Size(w, h * 0.2f), alpha = 0.3f * f * seg(p, 0.4f, 0.7f))
            tagAt(a.tag("mtlayl0", 0, 0), Offset(w * 0.4f, h * 0.36f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig130, f)
            book130(Offset(w * 0.5f, g - h * 0.2f), w * 0.04f, Qn.Gold, f)
            softGlow(Offset(w * 0.6f, g - h * 0.14f), w * 0.08f, Qn.GoldLight, 0.8f * f)
            tagAt(a.tag("mtlayl1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            close97(Offset(w * 0.78f, h * 0.36f), w * 0.07f, a, "mtlayl2", 1, 3, f * seg(p, 0.02f, 0.15f))
            for (k in 0 until 3) {
                val ph = ((a.t * 0.3f + k / 3f) % 1f)
                qIcon("scroll", Offset(w * (0.3f + 0.12f * k), mix(h * 0.15f, g - h * 0.2f, ph)), w * 0.04f, Qn.GoldLight, f * (1f - ph) * seg(p, 0.3f, 0.45f), a.t)
            }
            person99(Offset(w * 0.42f, g), h * 0.13f, Fig130, f)
            tagAt(a.tag("mtlayl2", 0, 0), Offset(w * 0.4f, h * 0.12f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil130(f)
            plain97(Offset(w * 0.5f, h * 0.38f), w * 0.09f, a, "mtlayl3", 0, f * seg(p, 0.02f, 0.15f))
            qIcon("heart", Offset(w * 0.5f, h * 0.58f), w * 0.06f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("mtlayl3", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mt-hizb ═════════════
internal fun DrawScope.mthizbScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mthizb", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night130(a) },
        { p, f ->
            veil130(f)
            for (k in 0 until 7) {
                val x = w * (0.85f - 0.117f * k)
                val q = seg(p, 0.05f + 0.07f * k, 0.15f + 0.07f * k)
                drawCircle(Qn.Gold, w * 0.025f, Offset(x, h * 0.42f), alpha = f * q)
                book130(Offset(x, h * 0.32f), w * 0.022f, Qn.GoldLight, f * q)
            }
            tagAt(a.tag("mthizb0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            val dawn = seg(p, 0.3f, 0.6f)
            drawRect(Color(0xFFE8B070), Offset(0f, h * 0.55f), Size(w, h * 0.25f), alpha = 0.25f * f * dawn)
            qIcon("moon", Offset(w * 0.8f, h * 0.2f), w * 0.06f, Qn.GoldLight, f * (1f - dawn), a.t)
            qIcon("sun", Offset(w * 0.2f, mix(h * 0.7f, h * 0.45f, dawn)), w * 0.07f, Qn.Gold, f * dawn, a.t)
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig130, f)
            book130(Offset(w * 0.5f, g - h * 0.2f), w * 0.04f, Qn.Gold, f * seg(p, 0.55f, 0.7f))
            tagAt(a.tag("mthizb1", 0, 0), Offset(w * 0.5f, h * 0.26f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil130(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.42f), w * 0.16f, Dim130, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("check", Offset(w * 0.38f, h * 0.5f), w * 0.04f, Qn.Jade, f * seg(p, 0.3f, 0.42f), a.t)
            qIcon("cross", Offset(w * 0.62f, h * 0.5f), w * 0.04f, Qn.Rose, f * seg(p, 0.35f, 0.47f), a.t)
            tagAt(a.tag("mthizb2", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            mother102(Offset(w * 0.5f, h * 0.36f), w * 0.09f, a, "mthizb3", 0, 1, f * seg(p, 0.02f, 0.15f))
            qIcon("mat", Offset(w * 0.5f, g), w * 0.12f, Qn.Jade, f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("mthizb3", 0, 1), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mt-tardid ═════════════
internal fun DrawScope.mttardidScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mttardid", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night130(a) },
        { p, f ->
            veil130(f)
            for (k in 0 until 4) {
                val q = seg(p, 0.1f + 0.15f * k, 0.25f + 0.15f * k)
                drawCircle(Qn.GoldLight, w * (0.05f + 0.07f * k), Offset(w * 0.5f, h * 0.42f), alpha = 0.6f * f * q, style = Stroke(w * 0.004f))
            }
            drawCircle(Qn.Gold, w * 0.025f, Offset(w * 0.5f, h * 0.42f), alpha = f)
            tagAt(a.tag("mttardid0", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil130(f)
            plain97(Offset(w * 0.5f, h * 0.3f), w * 0.09f, a, "mttardid1", 0, f * seg(p, 0.02f, 0.15f))
            tagAt(a.tag("mttardid1", 0, 1), Offset(w * 0.5f, h * 0.54f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("mttardid1", 0, 2), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val dawn = seg(p, 0.2f, 0.85f)
            drawRect(Color(0xFFE8B070), Offset(0f, h * 0.55f), Size(w, h * 0.25f), alpha = 0.3f * f * dawn)
            qIcon("moon", Offset(mix(w * 0.85f, w * 0.15f, dawn), h * (0.18f + 0.1f * dawn)), w * 0.06f, Qn.GoldLight, f * (1f - 0.7f * dawn), a.t)
            sirajMunir(Offset(w * 0.5f, h * 0.55f), w * 0.033f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.43f))
            voice130(Offset(w * 0.5f, h * 0.55f), 0.6f, f, a.t)
            tagAt(a.tag("mttardid2", 0, 0), Offset(w * 0.5f, h * 0.88f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil130(f)
            plain97(Offset(w * 0.5f, h * 0.38f), w * 0.09f, a, "mttardid3", 0, f * seg(p, 0.02f, 0.15f))
            qIcon("sun", Offset(w * 0.5f, h * 0.62f), w * 0.06f, Qn.Gold, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("mttardid3", 0, 1), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mt-jahr ═════════════
internal fun DrawScope.mtjahrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mtjahr", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night130(a) },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.24f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.13f))
            close97(Offset(w * 0.76f, h * 0.55f), w * 0.08f, a, "mtjahr0", 0, 0, f * seg(p, 0.05f, 0.2f))
            voice130(Offset(w * 0.76f, h * 0.55f), 0.25f, f * seg(p, 0.15f, 0.3f), a.t)
            close97(Offset(w * 0.24f, h * 0.55f), w * 0.08f, a, "mtjahr0", 1, 1, f * seg(p, 0.45f, 0.6f))
            voice130(Offset(w * 0.24f, h * 0.55f), 1f, f * seg(p, 0.55f, 0.7f), a.t)
        },
        { p, f ->
            close97(Offset(w * 0.76f, h * 0.36f), w * 0.07f, a, "mtjahr0", 0, 0, f)
            close97(Offset(w * 0.24f, h * 0.36f), w * 0.07f, a, "mtjahr0", 1, 1, f)
            tagAt(a.tag("mtjahr1", 0, 0), Offset(w * 0.72f, h * 0.6f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("mtjahr1", 0, 1), Offset(w * 0.28f, h * 0.72f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val q = easeInOut(seg(p, 0.2f, 0.7f))
            close97(Offset(w * 0.76f, h * 0.45f), w * 0.08f, a, "mtjahr0", 0, 0, f)
            voice130(Offset(w * 0.76f, h * 0.45f), mix(0.25f, 0.6f, q), f, a.t)
            close97(Offset(w * 0.24f, h * 0.45f), w * 0.08f, a, "mtjahr0", 1, 1, f)
            voice130(Offset(w * 0.24f, h * 0.45f), mix(1f, 0.6f, q), f, a.t)
            row130(a, "mtjahr2", 2, h * 0.72f, p, f, from = 0.1f, step = 0.45f, ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 3) {
                val x = w * (0.22f + 0.28f * k)
                qIcon("house", Offset(x, g - h * 0.08f), w * 0.1f, Dim130, f, a.t)
                voice130(Offset(x, g - h * 0.12f), 0.5f, f * seg(p, 0.15f + 0.12f * k, 0.3f + 0.12f * k), a.t)
            }
            tagAt(a.tag("mtjahr3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}
