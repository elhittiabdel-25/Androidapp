package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 115 (2026-11-17): Ibn Rajab's Jami' al-Ulum wal-Hikam: the comprehensive words, the hadiths the religion
 * turns on, the two meanings of intention, riya and its kinds, and the early glad tidings. The heart is drawn as a
 * heart icon, the deed as a small lamp of light; people are faceless, never the Prophet ﷺ (only his light).
 */

private val Good115 = Color(0xCC1A3020)
private val Bad115 = Color(0xCC401A20)

private fun DrawScope.night115(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.84f, 0.7f)
    dunes97(size.height * 0.84f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil115(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.6f * alpha)

private fun DrawScope.row115(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             back: Color = Color(0xB3101426), ink: Color = ArtInk.Ivory, first: Int = 0) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, first + i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), back = back, ink = ink)
    }
}

// ═════════════ jr-jawami ═════════════
internal fun DrawScope.jrjawamiScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jrjawami", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night115(a) },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.34f), w * 0.04f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.14f))
            for (k in 0 until 8) {
                val ang = k / 8f * 6.283f + a.t * 0.2f
                val r = mix(w * 0.35f, w * 0.12f, easeInOut(seg(p, 0.2f, 0.7f)))
                drawCircle(Qn.GoldLight, w * 0.012f, Offset(w * 0.5f + r * kotlin.math.cos(ang), h * 0.34f + r * kotlin.math.sin(ang) * 0.6f), alpha = f * seg(p, 0.1f, 0.25f))
            }
            tagAt(a.tag("jrjawami0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil115(f)
            qIcon("book", Offset(w * 0.5f, h * 0.38f), w * 0.13f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("jrjawami1", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil115(f)
            sirajMunir(Offset(w * 0.5f, h * 0.36f), w * 0.035f, f, a.t, named = false)
            qIcon("lips", Offset(w * 0.5f, h * 0.5f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("jrjawami2", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil115(f)
            for (k in 0 until 3) {
                val x = w * (0.78f - 0.28f * k)
                val hh = h * (0.12f + 0.08f * k) * easeInOut(seg(p, 0.1f + 0.25f * k, 0.35f + 0.25f * k))
                drawRoundRect(listOf(Qn.Lapis, Qn.Jade, Qn.Gold)[k], Offset(x - w * 0.07f, h * 0.56f - hh), Size(w * 0.14f, hh), CornerRadius(5f * density), alpha = f)
            }
            row115(a, "jrjawami3", 3, h * 0.66f, p, f, step = 0.25f)
        },
    )
}

// ═════════════ jr-usul ═════════════
internal fun DrawScope.jrusulScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jrusul", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night115(a) },
        { p, f ->
            veil115(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("jrusul0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("jrusul0", 0, 1), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil115(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.36f), w * 0.1f, Qn.Rose, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("jrusul1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("jrusul1", 0, 1), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil115(f)
            for (k in 0 until 40) drawCircle(Roundel.PlainFill, w * 0.01f, Offset(w * (0.62f + 0.03f * (k % 8)), h * (0.26f + 0.05f * (k / 8))), alpha = f * seg(p, 0.05f, 0.2f))
            for (k in 0 until 4) {
                val go = easeInOut(seg(p, 0.35f + 0.08f * k, 0.65f + 0.08f * k))
                drawLine(Qn.GoldLight, Offset(w * 0.6f, h * (0.3f + 0.06f * k)), Offset(mix(w * 0.6f, w * 0.32f, go), h * (0.3f + 0.06f * k)), 1.2f * density, alpha = 0.6f * f)
                drawCircle(Qn.Gold, w * 0.022f, Offset(w * 0.28f, h * (0.3f + 0.06f * k)), alpha = f * seg(p, 0.6f + 0.06f * k, 0.7f + 0.06f * k))
            }
            tagAt(a.tag("jrusul2", 0, 0), Offset(w * 0.73f, h * 0.62f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("jrusul2", 0, 1), Offset(w * 0.28f, h * 0.62f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil115(f)
            for (k in 0 until 4) tagAt(a.tag("jrusul3", 0, k), Offset(w * 0.5f, h * (0.24f + 0.12f * k)), f * seg(p, 0.08f + 0.2f * k, 0.22f + 0.2f * k), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ jr-niyya ═════════════
internal fun DrawScope.jrniyyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jrniyya", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night115(a) },
        { p, f ->
            veil115(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.36f), w * 0.12f, Qn.Rose, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 2) drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.46f), Offset(w * (0.3f + 0.4f * k), h * 0.6f), 1.6f * density, StrokeCap.Round, alpha = f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("jrniyya0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil115(f)
            qIcon("mat", Offset(w * 0.72f, h * 0.38f), w * 0.1f, Qn.Jade, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("bed", Offset(w * 0.28f, h * 0.38f), w * 0.1f, Color(0xFF8A8494), f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("jrniyya1", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("jrniyya1", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil115(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.4f), w * 0.1f, Qn.Rose, f, a.t)
            qIcon("light", Offset(w * 0.72f, h * 0.24f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            qIcon("people", Offset(w * 0.28f, h * 0.24f), w * 0.07f, Color(0xFF8A8494), f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("jrniyya2", 0, 0), Offset(w * 0.72f, h * 0.62f), f * seg(p, 0.2f, 0.35f), back = Good115, ink = ArtInk.Jade)
            tagAt(a.tag("jrniyya2", 0, 1), Offset(w * 0.28f, h * 0.62f), f * seg(p, 0.5f, 0.65f), back = Bad115, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil115(f)
            qIcon("cup", Offset(w * 0.5f, h * 0.3f), w * 0.07f, Color(0xFF9A8A80), f * seg(p, 0.05f, 0.2f), a.t)
            row115(a, "jrniyya3", 3, h * 0.56f, p, f, step = 0.2f)
        },
    )
}

// ═════════════ jr-riya ═════════════
internal fun DrawScope.jrriyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jrriya", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night115(a) },
        { p, f ->
            veil115(f)
            qIcon("people", Offset(w * 0.5f, h * 0.24f), w * 0.1f, Color(0xFF8A8494), f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("eye", Offset(w * 0.5f, h * 0.42f), w * 0.08f, Qn.Ember, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("jrriya0", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.1f, 0.25f), back = Bad115, ink = ArtInk.Ivory)
            tagAt(a.tag("jrriya0", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil115(f)
            val fall = seg(p, 0.2f, 0.7f)
            qIcon("light", Offset(w * 0.5f, h * (0.3f + 0.25f * fall)), w * 0.07f, Color(0xFF8A8494), f * (1f - 0.7f * fall), a.t)
            tagAt(a.tag("jrriya1", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.5f, 0.65f), back = Bad115, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil115(f)
            qIcon("light", Offset(w * 0.62f, h * 0.38f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("eye", Offset(w * 0.4f, h * 0.38f), w * 0.06f, Qn.Ember, f * seg(p, 0.35f, 0.5f), a.t)
            drawCircle(Qn.Ember, w * 0.16f, Offset(w * 0.5f, h * 0.38f), alpha = f * seg(p, 0.55f, 0.7f), style = Stroke(2f * density))
            tagAt(a.tag("jrriya2", 0, 0), Offset(w * 0.68f, h * 0.62f), f * seg(p, 0.1f, 0.25f), back = Good115, ink = ArtInk.Jade)
            tagAt(a.tag("jrriya2", 0, 1), Offset(w * 0.32f, h * 0.62f), f * seg(p, 0.4f, 0.55f), back = Bad115, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil115(f)
            tagAt(a.tag("jrriya3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            qIcon("light", Offset(w * 0.5f, h * 0.44f), w * 0.07f, Color(0xFF8A8494), f * (1f - seg(p, 0.5f, 0.8f)), a.t)
            tagAt(a.tag("jrriya3", 0, 1), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.5f, 0.65f), back = Bad115, ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ jr-bushra ═════════════
internal fun DrawScope.jrbushraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jrbushra", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night115(a) },
        { p, f ->
            veil115(f)
            qIcon("light", Offset(w * 0.5f, h * 0.36f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 4) qIcon("heart", Offset(w * (0.26f + 0.16f * k), h * 0.58f), w * 0.035f, Qn.Rose, f * seg(p, 0.45f + 0.06f * k, 0.6f + 0.06f * k), a.t)
            tagAt(a.tag("jrbushra0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.1f, 0.25f), back = Good115, ink = ArtInk.Jade)
            tagAt(a.tag("jrbushra0", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil115(f)
            qIcon("sun", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.25f), a.t)
            tagAt(a.tag("jrbushra1", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil115(f)
            qIcon("check", Offset(w * 0.72f, h * 0.38f), w * 0.07f, Qn.Jade, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.28f, h * 0.38f), w * 0.07f, Qn.Ember, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("jrbushra2", 0, 0), Offset(w * 0.72f, h * 0.56f), f * seg(p, 0.1f, 0.25f), back = Good115, ink = ArtInk.Jade)
            tagAt(a.tag("jrbushra2", 0, 1), Offset(w * 0.28f, h * 0.56f), f * seg(p, 0.5f, 0.65f), back = Bad115, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil115(f)
            qIcon("mountain", Offset(w * 0.5f, h * 0.42f), w * 0.2f, Color(0xFF6A5640), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("heart", Offset(w * 0.5f, h * 0.24f), w * 0.06f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("jrbushra3", 0, 0), Offset(w * 0.72f, h * 0.64f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("jrbushra3", 0, 1), Offset(w * 0.28f, h * 0.64f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}
