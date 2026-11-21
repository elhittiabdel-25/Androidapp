package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 113 (2026-11-17): five cards of «كيف فسر السلف القرآن؟» from Sharh Muqaddima fi Usul al-Tafsir.
 * The mushaf is a closed green book with gold (never open text); Companions are roundels (Ibn Abbas the
 * family's colour), the Followers and scholars plain roundels; no figure for him ﷺ.
 */

private val Good113 = Color(0xCC1A3020)
private val Bad113 = Color(0xCC401A20)
private val Mushaf113 = Color(0xFF1E5A3E)

private fun DrawScope.room113(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.6f)
    drawRect(Color(0xFF2A1E16), Offset(0f, size.height * 0.8f), Size(size.width, size.height * 0.2f))
}

private fun DrawScope.veil113(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.6f * alpha)

private fun DrawScope.mushaf113(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Mushaf113, c + Offset(-s * 0.45f, -s * 0.6f), Size(s * 0.9f, s * 1.2f), CornerRadius(s * 0.08f), alpha = alpha)
    drawRoundRect(Qn.Gold, c + Offset(-s * 0.35f, -s * 0.5f), Size(s * 0.7f, s), CornerRadius(s * 0.06f), alpha = alpha, style = Stroke(1.6f * density))
    drawCircle(Qn.Gold, s * 0.12f, c, alpha = alpha, style = Stroke(1.6f * density))
}

private fun DrawScope.steps113(a: ArtCtx, kind: String, n: Int, p: Float, f: Float, y: Float) {
    for (k in 0 until n) {
        val x = size.width * (0.82f - 0.64f * k / (n - 1f).coerceAtLeast(1f))
        val on = f * seg(p, 0.08f + 0.16f * k, 0.22f + 0.16f * k)
        drawRoundRect(listOf(Qn.Gold, Qn.Jade, Qn.Lapis, Color(0xFF8A8494))[k % 4], Offset(x - size.width * 0.07f, y - size.height * (0.04f + 0.04f * (n - 1 - k))), Size(size.width * 0.14f, size.height * (0.04f + 0.04f * (n - 1 - k))), CornerRadius(4f * density), alpha = 0.8f * on)
        tagAt(a.tag(kind, 0, k), Offset(x, y + size.height * 0.06f), on, ink = ArtInk.Ivory)
    }
}

// ═════════════ tq-ashr ═════════════
internal fun DrawScope.tqashrScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tqashr", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room113(a) },
        { p, f ->
            veil113(f)
            mushaf113(Offset(w * 0.5f, h * 0.38f), w * 0.14f, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("tqashr0", 0, 0), Offset(w * 0.72f, h * 0.62f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
            tagAt(a.tag("tqashr0", 0, 1), Offset(w * 0.28f, h * 0.62f), f * seg(p, 0.5f, 0.65f), back = Good113, ink = ArtInk.Jade)
        },
        { p, f ->
            veil113(f)
            for (k in 0 until 10) drawCircle(Qn.GoldLight, w * 0.018f, Offset(w * (0.8f - 0.065f * k), h * 0.3f), alpha = f * seg(p, 0.05f + 0.03f * k, 0.12f + 0.03f * k))
            tagAt(a.tag("tqashr1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("tqashr1", 0, 1), Offset(w * 0.68f, h * 0.52f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
            tagAt(a.tag("tqashr1", 0, 2), Offset(w * 0.32f, h * 0.52f), f * seg(p, 0.65f, 0.8f), back = Good113, ink = ArtInk.Jade)
        },
        { p, f ->
            veil113(f)
            mushaf113(Offset(w * 0.5f, h * 0.34f), w * 0.1f, f)
            qIcon("light", Offset(w * 0.72f, h * 0.34f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            qIcon("hands", Offset(w * 0.28f, h * 0.34f), w * 0.07f, Qn.Jade, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("tqashr2", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil113(f)
            plain97(Offset(w * 0.68f, h * 0.36f), w * 0.08f, a, "tqashr3", 0, f * seg(p, 0.05f, 0.25f))
            mushaf113(Offset(w * 0.3f, h * 0.36f), w * 0.09f, f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("tqashr3", 0, 1), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tq-sunna ═════════════
internal fun DrawScope.tqsunnaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tqsunna", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room113(a) },
        { p, f ->
            veil113(f)
            steps113(a, "tqsunna0", 2, p, f, h * 0.5f)
        },
        { p, f ->
            veil113(f)
            plain97(Offset(w * 0.5f, h * 0.32f), w * 0.09f, a, "tqsunna1", 0, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("tqsunna1", 0, 1), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil113(f)
            mushaf113(Offset(w * 0.68f, h * 0.38f), w * 0.11f, f * seg(p, 0.05f, 0.2f))
            qIcon("scroll", Offset(w * 0.32f, h * 0.38f), w * 0.11f, Color(0xFFD8C4A8), f * seg(p, 0.35f, 0.5f), a.t)
            drawLine(Qn.GoldLight, Offset(w * 0.58f, h * 0.38f), Offset(w * 0.42f, h * 0.38f), 2f * density, StrokeCap.Round, alpha = f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("tqsunna2", 0, 0), Offset(w * 0.68f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("tqsunna2", 0, 1), Offset(w * 0.32f, h * 0.58f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil113(f)
            plain97(Offset(w * 0.68f, h * 0.38f), w * 0.08f, a, "tqsunna3", 0, f * seg(p, 0.05f, 0.25f))
            close97(Offset(w * 0.32f, h * 0.38f), w * 0.08f, a, "tqsunna3", 1, 3, f * seg(p, 0.35f, 0.55f))
        },
    )
}

// ═════════════ tq-israiliyyat ═════════════
internal fun DrawScope.tqisraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tqisra", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room113(a) },
        { p, f ->
            veil113(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.36f), w * 0.11f, Color(0xFFB0A080), f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tqisra0", 0, 0), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.2f, 0.35f), back = Good113, ink = ArtInk.Jade)
            tagAt(a.tag("tqisra0", 0, 1), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.5f, 0.65f), back = Bad113, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil113(f)
            qIcon("check", Offset(w * 0.72f, h * 0.38f), w * 0.08f, Qn.Jade, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.28f, h * 0.38f), w * 0.08f, Qn.Ember, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("tqisra1", 0, 0), Offset(w * 0.72f, h * 0.56f), f * seg(p, 0.1f, 0.25f), back = Good113, ink = ArtInk.Jade)
            tagAt(a.tag("tqisra1", 0, 1), Offset(w * 0.28f, h * 0.56f), f * seg(p, 0.5f, 0.65f), back = Bad113, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil113(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.13f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tqisra2", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("tqisra2", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil113(f)
            for (k in 0 until 3) {
                val c = Offset(w * (0.78f - 0.28f * k), h * 0.4f)
                val on = f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k)
                qIcon(listOf("people", "point", "tree")[k], c, w * 0.06f, Color(0xFF9A8A80), on, a.t)
                tagAt(a.tag("tqisra3", 0, k), c + Offset(0f, h * 0.13f), on, ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ tq-tabiin ═════════════
internal fun DrawScope.tqtabiinScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tqtabiin", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room113(a) },
        { p, f ->
            veil113(f)
            steps113(a, "tqtabiin0", 4, p, f, h * 0.5f)
        },
        { p, f ->
            veil113(f)
            close97(Offset(w * 0.68f, h * 0.34f), w * 0.08f, a, "tqtabiin1", 1, 3, f * seg(p, 0.05f, 0.25f))
            plain97(Offset(w * 0.32f, h * 0.34f), w * 0.08f, a, "tqtabiin1", 0, f * seg(p, 0.15f, 0.35f))
            for (k in 0 until 3) {
                val ph = ((a.t * 0.5f + k / 3f) % 1f)
                mushaf113(Offset(mix(w * 0.4f, w * 0.6f, ph), h * 0.34f), w * 0.04f, f * seg(p, 0.4f, 0.55f) * (1f - ph))
            }
            tagAt(a.tag("tqtabiin1", 0, 2), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil113(f)
            for (k in 0 until 4) drawCircle(Roundel.PlainFill, w * 0.03f, Offset(w * (0.6f + 0.06f * k), h * 0.36f), alpha = f * seg(p, 0.05f, 0.2f))
            drawLine(Qn.Jade, Offset(w * 0.58f, h * 0.42f), Offset(w * 0.8f, h * 0.42f), 2f * density, alpha = f * seg(p, 0.15f, 0.3f))
            for (k in 0 until 4) drawCircle(Roundel.PlainFill, w * 0.03f, Offset(w * (0.2f + 0.06f * k), h * (0.32f + 0.04f * (k % 2))), alpha = f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("tqtabiin2", 0, 0), Offset(w * 0.7f, h * 0.56f), f * seg(p, 0.2f, 0.35f), back = Good113, ink = ArtInk.Jade)
            tagAt(a.tag("tqtabiin2", 0, 1), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil113(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tqtabiin3", 0, 0), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("tqtabiin3", 0, 1), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ tq-ray ═════════════
internal fun DrawScope.tqrayScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tqray", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room113(a) },
        { p, f ->
            veil113(f)
            mushaf113(Offset(w * 0.5f, h * 0.36f), w * 0.12f, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("tqray0", 0, 0), Offset(w * 0.72f, h * 0.6f), f * seg(p, 0.2f, 0.35f), back = Bad113, ink = ArtInk.Ivory)
            tagAt(a.tag("tqray0", 0, 1), Offset(w * 0.28f, h * 0.6f), f * seg(p, 0.45f, 0.6f), back = Bad113, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil113(f)
            drawRoundRect(Color(0xFF6A4A30), Offset(w * 0.4f, h * 0.22f), Size(w * 0.2f, h * 0.34f), CornerRadius(6f * density), alpha = f * seg(p, 0.05f, 0.2f))
            drawCircle(Qn.Gold, w * 0.012f, Offset(w * 0.56f, h * 0.4f), alpha = f * seg(p, 0.05f, 0.2f))
            val go = easeInOut(seg(p, 0.2f, 0.6f))
            person99(Offset(mix(w * 0.15f, w * 0.3f, go), h * 0.6f), h * 0.07f, Qn.Person, f, walk = a.t * 2f * go)
            qIcon("cross", Offset(w * 0.3f, h * 0.36f), w * 0.05f, Qn.Ember, f * seg(p, 0.6f, 0.72f), a.t)
            tagAt(a.tag("tqray1", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.45f, 0.6f), back = Bad113, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil113(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.13f, Color(0xFF9A8A80), f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("tqray2", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.15f, 0.3f), back = Bad113, ink = ArtInk.Ivory)
            tagAt(a.tag("tqray2", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil113(f)
            qIcon("key", Offset(w * 0.5f, h * 0.24f), w * 0.07f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            steps113(a, "tqray3", 4, p, f, h * 0.56f)
        },
    )
}
