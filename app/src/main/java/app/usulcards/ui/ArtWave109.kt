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
 * Wave 109 (2026-11-17): «مواقفه ﷺ وأحواله» (2): his weeping for his ummah, laughter, play, anger, joy, grief,
 * reflection, how he spurred people on and how he corrected them (Zad level 4).
 * He ﷺ is only his named light; Jibril cool white light; Umar his roundel, Jafar and Ibn Abbas the family's,
 * Aisha the Mothers' colour, every other named person the plain roundel; crowds faceless. The devil is never drawn.
 */

private val Bad109 = Color(0xCC401A20)
private val Good109 = Color(0xCC1A3020)

private fun DrawScope.night109(a: ArtCtx, ground: Float) {
    nightSky(a.t, S97.Night, S97.NightLow, ground, 0.8f)
    dunes97(ground, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil109(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.6f * alpha)

private fun DrawScope.no109(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(Qn.Ember, r, c, alpha = alpha, style = Stroke(2.5f * density))
    drawLine(Qn.Ember, c + Offset(-r * 0.7f, -r * 0.7f), c + Offset(r * 0.7f, r * 0.7f), 2.5f * density, alpha = alpha)
}

private fun DrawScope.tears109(c: Offset, s: Float, t: Float, alpha: Float, n: Int = 2) {
    for (k in 0 until n) {
        val ph = (t * 0.5f + k / n.toFloat()) % 1f
        qIcon("drop", c + Offset((k - (n - 1) / 2f) * s * 1.6f, s * 3f * ph), s, Color(0xFF9CC8E8), alpha * (1f - ph), t)
    }
}

private fun DrawScope.people109(x0: Float, x1: Float, ground: Float, n: Int, alpha: Float, t: Float = 0f) {
    for (k in 0 until n) person99(Offset(mix(x0, x1, k / (n - 1f).coerceAtLeast(1f)), ground), size.height * 0.065f, Qn.Person, alpha, walk = t)
}

private fun DrawScope.mound109(c: Offset, w0: Float, alpha: Float) =
    drawArc(Color(0xFF6A5640), 180f, 180f, true, c + Offset(-w0 / 2f, -w0 * 0.2f), Size(w0, w0 * 0.4f), alpha = alpha)

// ═════════════ ah-ummati ═════════════
internal fun DrawScope.ahummatiScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahummati", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night109(a, ground) },
        { p, f ->
            tagAt(a.tag("ahummati0", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            sirajMunir(Offset(w * 0.5f, h * 0.36f), w * 0.04f, f, a.t, named = false)
            qIcon("hands", Offset(w * 0.5f, h * 0.26f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            tears109(Offset(w * 0.5f, h * 0.44f), w * 0.018f, a.t, f * seg(p, 0.55f, 0.7f))
            people109(w * 0.15f, w * 0.85f, ground, 7, f * seg(p, 0.25f, 0.4f))
        },
        { p, f ->
            val down = easeInOut(seg(p, 0.05f, 0.5f))
            angel98(Offset(w * 0.75f, mix(-h * 0.05f, h * 0.3f, down)), w * 0.05f, f)
            sirajMunir(Offset(w * 0.3f, h * 0.4f), w * 0.035f, f, a.t, named = false)
            tagAt(a.tag("ahummati1", 0, 0), Offset(w * 0.75f, h * 0.46f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            people109(w * 0.15f, w * 0.85f, ground, 7, f)
            softGlow(Offset(w * 0.5f, ground - h * 0.06f), w * 0.4f, Qn.GoldLight, 0.35f * f * seg(p, 0.3f, 0.6f))
            tagAt(a.tag("ahummati2", 0, 0), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("ahummati2", 0, 1), Offset(w * 0.5f, h * 0.38f), f * seg(p, 0.5f, 0.65f), back = Good109, ink = ArtInk.Jade)
        },
        { p, f ->
            mound109(Offset(w * 0.5f, ground), w * 0.32f, f * seg(p, 0.05f, 0.2f))
            sirajMunir(Offset(w * 0.5f, h * 0.5f), w * 0.03f, f, a.t, named = false)
            tears109(Offset(w * 0.5f, h * 0.57f), w * 0.016f, a.t, f * seg(p, 0.25f, 0.4f))
            tagAt(a.tag("ahummati3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ah-dahik ═════════════
internal fun DrawScope.ahdahikScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahdahik", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night109(a, ground); house12(Offset(w * 0.5f, ground), w * 0.5f, Color(0xFFE2CCA0), Color(0xFF8A6A40), 0.5f, lit = 0.5f) },
        { p, f ->
            veil109(f)
            sirajMunir(Offset(w * 0.5f, h * 0.38f), w * 0.04f, f, a.t, named = false)
            drawArc(Qn.GoldLight, 20f, 140f, false, Offset(w * 0.43f, h * 0.42f), Size(w * 0.14f, h * 0.06f), alpha = f * seg(p, 0.3f, 0.5f), style = Stroke(2f * density, cap = StrokeCap.Round))
            tagAt(a.tag("ahdahik0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil109(f)
            sirajMunir(Offset(w * 0.75f, h * 0.4f), w * 0.03f, f, a.t, named = false)
            val hide = easeInOut(seg(p, 0.55f, 0.85f))
            people109(mix(w * 0.3f, w * 0.12f, hide), mix(w * 0.55f, w * 0.2f, hide), h * 0.7f, 4, f * seg(p, 0.05f, 0.2f))
            for (k in 0 until 3) drawArc(Qn.Ember, -60f, 120f, false, Offset(w * 0.42f + w * 0.03f * k, h * 0.5f - h * 0.02f * k), Size(w * 0.06f, h * 0.1f + h * 0.04f * k), alpha = 0.6f * f * seg(p, 0.15f, 0.3f) * (1f - hide), style = Stroke(1.5f * density))
            drawRect(Color(0xFF6A3E5A), Offset(w * 0.04f, h * 0.4f), Size(w * 0.06f, h * 0.32f), alpha = f * seg(p, 0.5f, 0.6f))
            tagAt(a.tag("ahdahik1", 0, 0), Offset(w * 0.42f, h * 0.28f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahdahik1", 0, 1), Offset(w * 0.25f, h * 0.82f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil109(f)
            sirajMunir(Offset(w * 0.3f, h * 0.4f), w * 0.035f, f, a.t, named = false)
            drawArc(Qn.GoldLight, 20f, 140f, false, Offset(w * 0.24f, h * 0.44f), Size(w * 0.12f, h * 0.05f), alpha = f, style = Stroke(2f * density, cap = StrokeCap.Round))
            close97(Offset(w * 0.72f, h * 0.4f), w * 0.085f, a, "ahdahik2", 0, 1, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("ahdahik2", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil109(f)
            // two paths that part: Umar on one, a dark cloud turning off on the other (the devil is never drawn)
            val fork = Offset(w * 0.5f, h * 0.7f)
            drawLine(Color(0xFFD8C4A8), fork, Offset(w * 0.78f, h * 0.24f), 5f * density, StrokeCap.Round, alpha = f * seg(p, 0.05f, 0.2f))
            drawLine(Color(0xFF6A6474), fork, Offset(w * 0.22f, h * 0.24f), 5f * density, StrokeCap.Round, alpha = f * seg(p, 0.05f, 0.2f))
            drawLine(Color(0xFFB09868), Offset(w * 0.5f, ground), fork, 5f * density, StrokeCap.Round, alpha = f * seg(p, 0.05f, 0.2f))
            val go = easeInOut(seg(p, 0.2f, 0.7f))
            close97(Offset(mix(w * 0.5f, w * 0.72f, go), mix(h * 0.7f, h * 0.32f, go)), w * 0.06f, a, "ahdahik2", 0, 1, f)
            qIcon("cloud", Offset(mix(w * 0.45f, w * 0.26f, go), mix(h * 0.6f, h * 0.3f, go)), w * 0.08f, Color(0xFF3A3440), f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("ahdahik3", 0, 0), Offset(w * 0.72f, h * 0.14f), f * seg(p, 0.4f, 0.55f), back = Good109, ink = ArtInk.Jade)
            tagAt(a.tag("ahdahik3", 0, 1), Offset(w * 0.28f, h * 0.14f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ah-mulaaba ═════════════
internal fun DrawScope.ahmulaabaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahmulaaba", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night109(a, ground) },
        { p, f ->
            veil109(f)
            sirajMunir(Offset(w * 0.75f, h * 0.38f), w * 0.03f, f, a.t, named = false)
            plain97(Offset(w * 0.3f, h * 0.38f), w * 0.08f, a, "ahmulaaba0", 0, f * seg(p, 0.05f, 0.25f))
            for (k in 0 until 2) qIcon("heart", Offset(w * (0.24f + 0.12f * k), h * 0.58f + sin(a.t * 2f + k) * h * 0.01f), w * 0.04f, Qn.Rose, f * seg(p, 0.45f + 0.1f * k, 0.6f + 0.1f * k), a.t)
            tagAt(a.tag("ahmulaaba0", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            house12(Offset(w * 0.5f, ground), w * 0.42f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = seg(p, 0.2f, 0.6f))
            for (k in 0 until 4) qIcon("heart", Offset(w * (0.38f + 0.08f * k), h * 0.4f - h * 0.04f * ((a.t * 0.4f + k / 4f) % 1f)), w * 0.03f, Qn.Rose, f * seg(p, 0.3f + 0.08f * k, 0.45f + 0.08f * k), a.t)
            tagAt(a.tag("ahmulaaba1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ahmulaaba1", 0, 1), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil109(f)
            sirajMunir(Offset(w * 0.5f, h * 0.36f), w * 0.035f, f, a.t, named = false)
            drawArc(Qn.GoldLight, 20f, 140f, false, Offset(w * 0.44f, h * 0.4f), Size(w * 0.12f, h * 0.05f), alpha = f * seg(p, 0.1f, 0.25f), style = Stroke(2f * density, cap = StrokeCap.Round))
            tagAt(a.tag("ahmulaaba2", 0, 0), Offset(w * 0.72f, h * 0.6f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            qIcon("check", Offset(w * 0.28f, h * 0.52f), w * 0.05f, Qn.Jade, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("ahmulaaba2", 0, 1), Offset(w * 0.28f, h * 0.64f), f * seg(p, 0.55f, 0.7f), back = Good109, ink = ArtInk.Jade)
        },
        { p, f ->
            val tent = Offset(w * 0.5f, ground)
            drawArc(Color(0xFF8A5A3E), 180f, 180f, true, tent + Offset(-w * 0.2f, -h * 0.22f), Size(w * 0.4f, h * 0.44f), alpha = f * seg(p, 0.05f, 0.2f))
            drawRect(Color(0xFF2A1E14), tent + Offset(-w * 0.04f, -h * 0.12f), Size(w * 0.08f, h * 0.12f), alpha = f * seg(p, 0.05f, 0.2f))
            sirajMunir(tent + Offset(0f, -h * 0.07f), w * 0.022f, f, a.t, named = false)
            plain97(Offset(w * 0.84f, h * 0.5f), w * 0.07f, a, "ahmulaaba3", 0, f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("ahmulaaba3", 0, 1), Offset(w * 0.72f, h * 0.26f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahmulaaba3", 0, 2), Offset(w * 0.3f, h * 0.26f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ah-ghadab ═════════════
internal fun DrawScope.ahghadabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahghadab", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night109(a, ground) },
        { p, f ->
            people109(w * 0.15f, w * 0.45f, ground, 3, f)
            people109(w * 0.6f, w * 0.85f, ground, 3, f)
            for (k in 0 until 3) drawLine(Qn.Ember, Offset(w * (0.4f + 0.04f * k), h * 0.6f), Offset(w * (0.6f - 0.04f * k), h * 0.66f), 1.5f * density, alpha = 0.6f * f * seg(p, 0.1f, 0.25f))
            val red = seg(p, 0.45f, 0.7f)
            sirajMunir(Offset(w * 0.5f, h * 0.32f), w * 0.035f, f * seg(p, 0.3f, 0.45f), a.t, named = false)
            softGlow(Offset(w * 0.5f, h * 0.32f), w * 0.08f, Qn.Ember, 0.4f * f * red)
            tagAt(a.tag("ahghadab0", 0, 0), Offset(w * 0.5f, h * 0.48f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahghadab0", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.55f, 0.7f), back = Bad109, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil109(f)
            qIcon("book", Offset(w * 0.5f, h * 0.36f), w * 0.11f, Color(0xFFD8C4A8), f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 2) {
                val side = if (k == 0) -1f else 1f
                drawLine(Qn.Ember, Offset(w * 0.5f + side * w * 0.08f, h * 0.36f), Offset(w * 0.5f + side * w * 0.28f, h * 0.36f + h * 0.04f * sin(a.t * 6f)), 2f * density, alpha = f * seg(p, 0.1f, 0.25f))
            }
            tagAt(a.tag("ahghadab1", 0, 0), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahghadab1", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), back = Bad109, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil109(f)
            qIcon("hands", Offset(w * 0.72f, h * 0.4f), w * 0.08f, Color(0xFF9A8A80), f * seg(p, 0.05f, 0.2f), a.t)
            no109(Offset(w * 0.72f, h * 0.4f), w * 0.08f, f * seg(p, 0.2f, 0.32f))
            tagAt(a.tag("ahghadab2", 0, 0), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            qIcon("scale", Offset(w * 0.28f, h * 0.4f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("ahghadab2", 0, 1), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil109(f)
            qIcon("lock", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ahghadab3", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ahghadab3", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.5f, 0.65f), back = Good109, ink = ArtInk.Jade)
        },
    )
}

// ═════════════ ah-farah ═════════════
internal fun DrawScope.ahfarahScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahfarah", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night109(a, ground) },
        { p, f ->
            val bright = seg(p, 0.2f, 0.6f)
            sirajMunir(Offset(w * 0.5f, h * 0.36f), w * (0.03f + 0.015f * bright), f, a.t, nameAt = Offset(w * 0.5f, h * 0.14f))
            softGlow(Offset(w * 0.5f, h * 0.36f), w * 0.2f, Qn.GoldLight, 0.35f * f * bright)
            tagAt(a.tag("ahfarah0", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahfarah0", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil109(f)
            mother102(Offset(w * 0.68f, h * 0.38f), w * 0.08f, a, "ahfarah1", 0, 1, f * seg(p, 0.05f, 0.25f))
            plain97(Offset(w * 0.32f, h * 0.38f), w * 0.07f, a, "ahfarah1", 1, f * seg(p, 0.55f, 0.75f))
            for (k in 0 until 2) qIcon("sun", Offset(w * (0.68f - 0.36f * k), h * 0.58f), w * 0.04f, Qn.Gold, f * seg(p, 0.3f + 0.4f * k, 0.45f + 0.4f * k), a.t)
        },
        { p, f ->
            veil109(f)
            plain97(Offset(w * 0.68f, h * 0.3f), w * 0.07f, a, "ahfarah2", 0, f * seg(p, 0.05f, 0.2f))
            plain97(Offset(w * 0.32f, h * 0.3f), w * 0.07f, a, "ahfarah2", 1, f * seg(p, 0.1f, 0.25f))
            for (k in 0 until 2) {
                val x = w * (0.68f - 0.36f * k)
                for (s in 0 until 2) drawOval(Qn.Person, Offset(x - w * 0.035f + w * 0.04f * s, h * 0.5f), Size(w * 0.03f, h * 0.06f), alpha = f * seg(p, 0.3f, 0.45f))
            }
            drawLine(Qn.Jade, Offset(w * 0.4f, h * 0.53f), Offset(w * 0.6f, h * 0.53f), 2f * density, alpha = f * seg(p, 0.55f, 0.7f))
            tagAt(a.tag("ahfarah2", 0, 2), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.6f, 0.75f), back = Good109, ink = ArtInk.Jade)
        },
        { p, f ->
            veil109(f)
            qIcon("mat", Offset(w * 0.5f, h * 0.46f), w * 0.14f, Qn.Jade, f * seg(p, 0.05f, 0.2f), a.t)
            sirajMunir(Offset(w * 0.5f, h * 0.42f), w * 0.026f, f * seg(p, 0.2f, 0.35f), a.t, named = false)
            softGlow(Offset(w * 0.5f, h * 0.44f), w * 0.15f, Qn.Jade, 0.35f * f * seg(p, 0.35f, 0.6f))
            tagAt(a.tag("ahfarah3", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ah-huzn ═════════════
internal fun DrawScope.ahhuznScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahhuzn", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night109(a, ground) },
        { p, f ->
            veil109(f)
            plain97(Offset(w * 0.78f, h * 0.36f), w * 0.075f, a, "ahhuzn0", 0, f * seg(p, 0.05f, 0.2f))
            close97(Offset(w * 0.5f, h * 0.36f), w * 0.075f, a, "ahhuzn0", 1, 3, f * seg(p, 0.2f, 0.35f))
            plain97(Offset(w * 0.22f, h * 0.36f), w * 0.075f, a, "ahhuzn0", 2, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("ahhuzn0", 0, 3), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val dim = seg(p, 0.1f, 0.5f)
            sirajMunir(Offset(w * 0.5f, h * 0.4f), w * (0.04f - 0.012f * dim), f, a.t, named = false)
            drawRect(Color(0xFF0A0E18), alpha = 0.35f * f * dim)
            tagAt(a.tag("ahhuzn1", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            people109(w * 0.12f, w * 0.88f, ground, 8, f * seg(p, 0.05f, 0.2f))
            sirajMunir(Offset(w * 0.5f, h * 0.3f), w * 0.03f, f, a.t, named = false)
            for (k in 0 until 8) drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.33f), Offset(mix(w * 0.12f, w * 0.88f, k / 7f), ground - h * 0.1f), 0.8f * density, alpha = 0.35f * f * seg(p, 0.3f + 0.04f * k, 0.45f + 0.04f * k))
            tagAt(a.tag("ahhuzn2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ahhuzn2", 0, 1), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil109(f)
            qIcon("mat", Offset(w * 0.5f, h * 0.42f), w * 0.14f, Qn.Jade, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ahhuzn3", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ah-tafakkur ═════════════
internal fun DrawScope.ahtafakkurScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahtafakkur", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night109(a, ground); moon12(Offset(w * 0.8f, h * 0.15f), w * 0.04f, 0.9f) },
        { p, f ->
            sirajMunir(Offset(w * 0.6f, ground - h * 0.14f), w * 0.03f, f, a.t, named = false)
            for (k in 0 until 3) drawLine(Qn.GoldLight, Offset(w * 0.6f, ground - h * 0.18f), Offset(w * (0.3f + 0.2f * k), h * 0.12f), 0.8f * density, alpha = 0.4f * f * seg(p, 0.3f, 0.5f))
            close97(Offset(w * 0.25f, h * 0.62f), w * 0.065f, a, "ahtafakkur0", 0, 3, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("ahtafakkur0", 0, 1), Offset(w * 0.6f, h * 0.48f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil109(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.38f), w * 0.12f, Color(0xFFD8C4A8), f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ahtafakkur1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("ahtafakkur1", 0, 1), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            qIcon("star", Offset(w * 0.5f, h * 0.26f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.05f, 0.25f), a.t)
            softGlow(Offset(w * 0.5f, h * 0.26f), w * 0.1f, Qn.GoldLight, 0.4f * f * seg(p, 0.25f, 0.5f))
            sirajMunir(Offset(w * 0.5f, ground - h * 0.14f), w * 0.03f, f, a.t, named = false)
            tagAt(a.tag("ahtafakkur2", 0, 0), Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil109(f)
            qIcon("hourglass", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ahtafakkur3", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ah-tahfiz ═════════════
internal fun DrawScope.ahtahfizScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahtahfiz", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night109(a, ground) },
        { p, f ->
            veil109(f)
            qIcon("coin", Offset(w * 0.72f, h * 0.4f), w * 0.07f, Qn.Gold, f * seg(p, 0.1f, 0.25f), a.t)
            qIcon("tree", Offset(w * 0.28f, h * 0.4f), w * 0.1f, Qn.Leaf, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("ahtahfiz0", 0, 0), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahtahfiz0", 0, 1), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil109(f)
            plain97(Offset(w * 0.7f, h * 0.38f), w * 0.08f, a, "ahtahfiz1", 0, f * seg(p, 0.05f, 0.25f))
            qIcon("tree", Offset(w * 0.28f, h * 0.38f), w * 0.1f, Qn.Leaf, f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("ahtahfiz1", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil109(f)
            qIcon("tree", Offset(w * 0.5f, h * 0.38f), w * 0.14f, Qn.Leaf, f, a.t)
            softGlow(Offset(w * 0.5f, h * 0.38f), w * 0.2f, Qn.Jade, 0.4f * f * seg(p, 0.2f, 0.5f))
            tagAt(a.tag("ahtahfiz2", 0, 0), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.25f, 0.4f), back = Good109, ink = ArtInk.Jade)
        },
        { p, f ->
            veil109(f)
            qIcon("mountain", Offset(w * 0.5f, h * 0.5f), w * 0.22f, Color(0xFF6A5640), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("coin", Offset(w * 0.5f, h * 0.3f), w * 0.06f, Qn.Gold, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("ahtahfiz3", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahtahfiz3", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ah-tazir ═════════════
internal fun DrawScope.ahtazirScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahtazir", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night109(a, ground) },
        { p, f ->
            veil109(f)
            val burn = seg(p, 0.45f, 0.7f)
            drawCircle(Qn.Gold, w * 0.07f, Offset(w * 0.5f, h * 0.38f), alpha = f * seg(p, 0.05f, 0.2f) * (1f - burn), style = Stroke(6f * density))
            qIcon("flame", Offset(w * 0.5f, h * 0.36f), w * 0.09f, Qn.Ember, f * burn, a.t)
            tagAt(a.tag("ahtazir0", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ahtazir0", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.55f, 0.7f), back = Bad109, ink = ArtInk.Ivory)
        },
        { p, f ->
            val go = easeInOut(seg(p, 0.3f, 0.8f))
            drawCircle(Qn.Gold, w * 0.03f, Offset(w * 0.35f, ground - h * 0.02f), alpha = f, style = Stroke(3f * density))
            person99(Offset(mix(w * 0.45f, w * 0.85f, go), ground), h * 0.075f, Qn.Person, f, walk = a.t * 2f * go)
            tagAt(a.tag("ahtazir1", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.1f, 0.25f), back = Good109, ink = ArtInk.Jade)
            tagAt(a.tag("ahtazir1", 0, 1), Offset(w * 0.5f, h * 0.4f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil109(f)
            for (k in 0 until 2) drawRoundRect(Color(0xFFE8822A), Offset(w * (0.3f + 0.22f * k), h * 0.32f), Size(w * 0.18f, h * 0.14f), CornerRadius(6f * density), alpha = f * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k))
            no109(Offset(w * 0.5f, h * 0.39f), w * 0.16f, f * seg(p, 0.5f, 0.62f))
            tagAt(a.tag("ahtazir2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahtazir2", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), back = Bad109, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil109(f)
            val icons = listOf("lips", "lock", "point", "hands")
            for (k in 0 until 4) {
                val c = Offset(w * (0.8f - 0.2f * k), h * 0.42f)
                val on = f * seg(p, 0.1f + 0.18f * k, 0.25f + 0.18f * k)
                qIcon(icons[k], c, w * 0.06f, Qn.GoldLight, on, a.t)
                tagAt(a.tag("ahtazir3", 0, k), c + Offset(0f, h * 0.12f), on, ink = ArtInk.Ivory)
            }
        },
    )
}
