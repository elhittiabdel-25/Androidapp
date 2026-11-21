package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 112 (2026-11-17): «كيف ندرس السيرة؟», the sources of the Sira and how its reports are sifted
 * (al-'Umari, al-Sira al-Nabawiyya al-Sahiha). A study scene: shelves, books, a chain of narrators as linked rings,
 * a balance; the scholars named are plain roundels.
 */

private val Good112 = Color(0xCC1A3020)
private val Bad112 = Color(0xCC401A20)
private val Spines112 = listOf(Color(0xFF6A3E2A), Color(0xFF2E4E6A), Color(0xFF4A5A2E), Color(0xFF6A2E4A), Color(0xFF5A4A2A))

private fun DrawScope.study112(a: ArtCtx) {
    drawRect(Color(0xFF1A1420))
    val w = size.width
    val h = size.height
    for (s in 0 until 2) {
        val y = h * (0.3f + 0.34f * s)
        drawRect(Color(0xFF4A3424), Offset(0f, y), Size(w, h * 0.02f), alpha = 0.8f)
        for (k in 0 until 14) drawRect(Spines112[(k + s) % 5], Offset(w * (0.03f + 0.07f * k), y - h * (0.12f + 0.02f * ((k * 7 + s) % 3))), Size(w * 0.05f, h * (0.12f + 0.02f * ((k * 7 + s) % 3))), alpha = 0.35f)
    }
}

private fun DrawScope.veil112(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.65f * alpha)

private fun DrawScope.book112(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(color, c + Offset(-s * 0.4f, -s * 0.55f), Size(s * 0.8f, s * 1.1f), CornerRadius(s * 0.08f), alpha = alpha)
    drawRect(Qn.GoldLight, c + Offset(-s * 0.4f, -s * 0.55f), Size(s * 0.1f, s * 1.1f), alpha = 0.6f * alpha)
}

/** A chain of [n] linked rings from [from] to [to], lit up to [lit] (0..1). */
private fun DrawScope.chain112(from: Offset, to: Offset, n: Int, alpha: Float, lit: Float) {
    for (k in 0 until n) {
        val f = k / (n - 1f)
        val c = Offset(mix(from.x, to.x, f), mix(from.y, to.y, f))
        drawCircle(if (f <= lit) Qn.GoldLight else Color(0xFF6A6474), size.width * 0.03f, c, alpha = alpha, style = Stroke(3f * density))
    }
}

// ═════════════ ss-masadir ═════════════
internal fun DrawScope.ssmasadirScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ssmasadir", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study112(a) },
        { p, f ->
            veil112(f)
            for (k in 0 until 4) {
                val c = Offset(w * (0.8f - 0.2f * k), h * 0.36f)
                val on = f * seg(p, 0.05f + 0.18f * k, 0.2f + 0.18f * k)
                book112(c, w * 0.09f, if (k == 0) Color(0xFF1E5A3E) else Spines112[k], on)
                tagAt(a.tag("ssmasadir0", 0, k), c + Offset(0f, h * 0.14f), on, ink = if (k == 0) ArtInk.Gold else ArtInk.Ivory)
            }
        },
        { p, f ->
            veil112(f)
            for (k in 0 until 3) {
                val c = Offset(w * (0.78f - 0.28f * k), h * 0.36f)
                val on = f * seg(p, 0.1f + 0.22f * k, 0.25f + 0.22f * k)
                book112(c, w * 0.07f, Spines112[(k + 2) % 5], 0.7f * on)
                tagAt(a.tag("ssmasadir1", 0, k), c + Offset(0f, h * 0.14f), on, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil112(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.15f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ssmasadir2", 0, 0), Offset(w * 0.72f, h * 0.56f), f * seg(p, 0.2f, 0.35f), back = Good112, ink = ArtInk.Jade)
            tagAt(a.tag("ssmasadir2", 0, 1), Offset(w * 0.28f, h * 0.56f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil112(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Color(0xFF9A8A80), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Qn.Ember, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("ssmasadir3", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.3f, 0.45f), back = Bad112, ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ss-quran ═════════════
internal fun DrawScope.ssquranScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ssquran", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study112(a) },
        { p, f ->
            veil112(f)
            qIcon("book", Offset(w * 0.5f, h * 0.3f), w * 0.13f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            softGlow(Offset(w * 0.5f, h * 0.3f), w * 0.16f, Qn.Gold, 0.3f * f * seg(p, 0.15f, 0.35f))
            for (k in 0 until 4) {
                val c = Offset(w * (0.8f - 0.2f * k), h * 0.58f)
                tagAt(a.tag("ssquran0", 0, k), c, f * seg(p, 0.3f + 0.12f * k, 0.42f + 0.12f * k), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil112(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.36f), w * 0.09f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("ssquran1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ssquran1", 0, 1), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil112(f)
            qIcon("book", Offset(w * 0.5f, h * 0.36f), w * 0.12f, Qn.Gold, f, a.t)
            tagAt(a.tag("ssquran2", 0, 0), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("ssquran2", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), back = Good112, ink = ArtInk.Jade)
        },
        { p, f ->
            veil112(f)
            for (k in 0 until 2) {
                val c = Offset(w * (0.68f - 0.36f * k), h * 0.38f)
                val on = f * seg(p, 0.15f + 0.3f * k, 0.3f + 0.3f * k)
                book112(c, w * 0.1f, Spines112[k + 1], on)
                tagAt(a.tag("ssquran3", 0, k), c + Offset(0f, h * 0.16f), on, ink = ArtInk.Gold)
            }
        },
    )
}

// ═════════════ ss-naqd ═════════════
internal fun DrawScope.ssnaqdScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ssnaqd", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study112(a) },
        { p, f ->
            veil112(f)
            chain112(Offset(w * 0.85f, h * 0.36f), Offset(w * 0.15f, h * 0.36f), 6, f * seg(p, 0.05f, 0.2f), seg(p, 0.3f, 0.8f))
            tagAt(a.tag("ssnaqd0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            qIcon("book", Offset(w * 0.5f, h * 0.56f), w * 0.08f, Spines112[1], f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("ssnaqd0", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil112(f)
            chain112(Offset(w * 0.85f, h * 0.36f), Offset(w * 0.25f, h * 0.36f), 5, f, seg(p, 0.05f, 0.6f))
            qIcon("eye", Offset(w * 0.14f, h * 0.36f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("ssnaqd1", 0, 0), Offset(w * 0.18f, h * 0.5f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
            tagAt(a.tag("ssnaqd1", 0, 1), Offset(w * 0.55f, h * 0.6f), f * seg(p, 0.65f, 0.8f), back = Good112, ink = ArtInk.Jade)
        },
        { p, f ->
            veil112(f)
            for (k in 0 until 3) {
                val x = w * (0.75f - 0.25f * k)
                val hh = h * (0.36f - 0.1f * k) * easeInOut(seg(p, 0.1f + 0.2f * k, 0.35f + 0.2f * k))
                drawRoundRect(listOf(Qn.Jade, Qn.Gold, Color(0xFF8A8494))[k], Offset(x - w * 0.07f, h * 0.66f - hh), Size(w * 0.14f, hh), CornerRadius(6f * density), alpha = f)
                tagAt(a.tag("ssnaqd2", 0, k), Offset(x, h * 0.74f), f * seg(p, 0.15f + 0.2f * k, 0.3f + 0.2f * k), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil112(f)
            qIcon("lock", Offset(w * 0.5f, h * 0.38f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ssnaqd3", 0, 0), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Gold)
            tagAt(a.tag("ssnaqd3", 0, 1), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ss-maghazi ═════════════
internal fun DrawScope.ssmaghaziScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ssmaghazi", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study112(a) },
        { p, f ->
            veil112(f)
            plain97(Offset(w * 0.5f, h * 0.3f), w * 0.09f, a, "ssmaghazi0", 0, f * seg(p, 0.05f, 0.25f))
            chain112(Offset(w * 0.8f, h * 0.58f), Offset(w * 0.2f, h * 0.58f), 5, f * seg(p, 0.3f, 0.45f), seg(p, 0.4f, 0.8f))
            tagAt(a.tag("ssmaghazi0", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil112(f)
            plain97(Offset(w * 0.68f, h * 0.36f), w * 0.09f, a, "ssmaghazi1", 0, f * seg(p, 0.05f, 0.25f))
            book112(Offset(w * 0.3f, h * 0.36f), w * 0.09f, Qn.Lapis, f * seg(p, 0.3f, 0.45f))
            softGlow(Offset(w * 0.3f, h * 0.36f), w * 0.12f, Qn.Jade, 0.35f * f * seg(p, 0.5f, 0.7f))
            tagAt(a.tag("ssmaghazi1", 0, 1), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.5f, 0.65f), back = Good112, ink = ArtInk.Jade)
        },
        { p, f ->
            veil112(f)
            book112(Offset(w * 0.5f, h * 0.38f), w * 0.07f, Qn.Lapis, f)
            softGlow(Offset(w * 0.5f, h * 0.38f), w * 0.1f, Qn.Jade, 0.35f * f)
            tagAt(a.tag("ssmaghazi2", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil112(f)
            plain97(Offset(w * 0.68f, h * 0.38f), w * 0.08f, a, "ssmaghazi3", 0, f * seg(p, 0.1f, 0.3f))
            plain97(Offset(w * 0.32f, h * 0.38f), w * 0.08f, a, "ssmaghazi3", 1, f * seg(p, 0.45f, 0.65f))
        },
    )
}

// ═════════════ ss-ibnishaq ═════════════
internal fun DrawScope.ssibnishaqScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "ssibnishaq", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { study112(a) },
        { p, f ->
            veil112(f)
            plain97(Offset(w * 0.5f, h * 0.32f), w * 0.09f, a, "ssibnishaq0", 0, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("ssibnishaq0", 0, 1), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.45f, 0.6f), back = Good112, ink = ArtInk.Jade)
        },
        { p, f ->
            veil112(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.13f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ssibnishaq1", 0, 0), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.15f, 0.3f), back = Good112, ink = ArtInk.Jade)
            tagAt(a.tag("ssibnishaq1", 0, 1), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.5f, 0.65f), back = Bad112, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil112(f)
            plain97(Offset(w * 0.5f, h * 0.32f), w * 0.09f, a, "ssibnishaq2", 0, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("ssibnishaq2", 0, 1), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.45f, 0.6f), back = Bad112, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil112(f)
            for (k in 0 until 3) {
                val c = Offset(w * (0.78f - 0.28f * k), h * 0.4f)
                plain97(c, w * 0.07f, a, "ssibnishaq3", k, f * seg(p, 0.1f + 0.22f * k, 0.25f + 0.22f * k))
                if (k < 2) drawLine(Qn.GoldLight, c + Offset(-w * 0.08f, 0f), c + Offset(-w * 0.2f, 0f), 1.6f * density, alpha = f * seg(p, 0.25f + 0.22f * k, 0.35f + 0.22f * k))
            }
        },
    )
}
