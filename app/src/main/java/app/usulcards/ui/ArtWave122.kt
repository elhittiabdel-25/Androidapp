package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 122 (2026-11-18): al-Albani's Mukhtasar Sahih al-Bukhari: the first revelation, Khadija and Waraqa, the three
 * men at the circle, «لا حسد إلا في اثنتين», Musa's «أي الناس أعلم», the parable of the rain, «سلوني», and the women's
 * day. The Prophet ﷺ is only his named light, Jibril a cool light, Musa and Yusha' named lights; Khadija, 'Umar and
 * 'A'isha are roundels, Waraqa a plain roundel; everyone else faceless. Tags with the Name of Allah stay at the top.
 */

private val Fig122 = Color(0xFFEDE4D0)
private val Dim122 = Color(0xFF8A8296)
private val Mom122 = Color(0xFFD8C2E0)

private fun DrawScope.night122(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.day122(): Float {
    val g = size.height * 0.8f
    daySky12(Qn.DayTop, Qn.DayLow, g, Color(0xFFB09868))
    return g
}

private fun DrawScope.veil122(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row122(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

private fun DrawScope.col122(a: ArtCtx, kind: String, n: Int, x: Float, top: Float, gap: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) tagAt(a.tag(kind, 0, i), Offset(x, top + gap * i), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
}

/** A mountain with a cave mouth; `lit` glows inside. */
private fun DrawScope.cave122(c: Offset, s: Float, alpha: Float, lit: Float = 0f) {
    drawPath(Path().apply { moveTo(c.x - s, c.y); lineTo(c.x - s * 0.1f, c.y - s * 0.9f); lineTo(c.x + s, c.y); close() }, Color(0xFF5A4A3A), alpha = alpha)
    drawArc(Color(0xFF14101A), 180f, 180f, true, Offset(c.x - s * 0.18f, c.y - s * 0.55f), Size(s * 0.36f, s * 0.4f), alpha = alpha)
    drawRect(Color(0xFF14101A), Offset(c.x - s * 0.18f, c.y - s * 0.35f), Size(s * 0.36f, s * 0.2f), alpha = alpha)
    if (lit > 0f) softGlow(Offset(c.x, c.y - s * 0.3f), s * 0.3f, Qn.GoldLight, lit * alpha)
}

/** The study circle: a ring of seated people around his named light. */
private fun DrawScope.circle122(a: ArtCtx, c: Offset, r: Float, n: Int, alpha: Float, gapAt: Int = -1, filled: Float = 0f) {
    sirajMunir(c, r * 0.22f, alpha, a.t, nameAt = Offset(c.x, c.y - r * 0.6f))
    for (k in 0 until n) {
        val ang = k / n.toFloat() * 6.283f + 1.57f
        val p = Offset(c.x + r * cos(ang), c.y + r * 0.45f * sin(ang))
        val al = if (k == gapAt) alpha * filled else alpha
        drawCircle(Fig122, r * 0.1f, p, alpha = al)
    }
}

// ═════════════ mb-wahy ═════════════
internal fun DrawScope.mbwahyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mbwahy", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night122(a) },
        { p, f ->
            val dawn = easeInOut(seg(p, 0.1f, 0.7f))
            drawRect(Brush.verticalGradient(listOf(Color(0x00000000), Color(0xFFF6C27A)), h * 0.45f, h * 0.8f), alpha = f * dawn)
            drawArc(Qn.GoldLight, 180f, 180f, true, Offset(w * 0.4f, h * 0.8f - w * 0.1f * dawn), Size(w * 0.2f, w * 0.2f), alpha = f * dawn)
            tagAt(a.tag("mbwahy0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            cave122(Offset(w * 0.5f, h * 0.8f), w * 0.45f, f, lit = seg(p, 0.2f, 0.45f))
            sirajMunir(Offset(w * 0.5f, h * 0.58f), w * 0.025f, f * seg(p, 0.2f, 0.45f), a.t, named = false)
            for (k in 0 until 3) qIcon("moon", Offset(w * (0.2f + 0.3f * k), h * 0.14f), w * 0.04f, Qn.GoldLight, f * seg(p, 0.4f + 0.12f * k, 0.5f + 0.12f * k), a.t)
            tagAt(a.tag("mbwahy1", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            cave122(Offset(w * 0.5f, h * 0.8f), w * 0.45f, f, lit = 0.5f)
            sirajMunir(Offset(w * 0.5f, h * 0.58f), w * 0.025f, f, a.t, nameAt = Offset(w * 0.32f, h * 0.5f))
            angel98(Offset(w * 0.72f, mix(h * 0.1f, h * 0.36f, easeInOut(seg(p, 0.05f, 0.35f)))), w * 0.04f, f)
            tagAt(a.tag("mbwahy2", 0, 0), Offset(w * 0.72f, h * 0.16f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("mbwahy2", 0, 1), Offset(w * 0.32f, h * 0.36f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val g = h * 0.8f
            house12(Offset(w * 0.28f, g), w * 0.34f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.6f)
            val go = easeInOut(seg(p, 0.05f, 0.5f))
            val jit = (1f - seg(p, 0.6f, 0.9f)) * w * 0.006f * sin(a.t * 40f)
            sirajMunir(Offset(mix(w * 0.85f, w * 0.38f, go) + jit, h * 0.6f), w * 0.03f, f, a.t, named = false)
            tagAt(a.tag("mbwahy3", 0, 0), Offset(w * 0.6f, h * 0.3f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ mb-khadija ═════════════
internal fun DrawScope.mbkhadijaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "mbkhadija", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night122(a) },
        { p, f ->
            tagAt(a.tag("mbkhadija0", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            close97(Offset(w * 0.68f, h * 0.48f), w * 0.1f, a, "mbkhadija0", 0, 0, f * seg(p, 0.05f, 0.25f))
            sirajMunir(Offset(w * 0.32f, h * 0.5f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.32f, h * 0.38f))
        },
        { p, f ->
            veil122(f)
            col122(a, "mbkhadija1", 5, w * 0.5f, h * 0.18f, h * 0.13f, p, f, from = 0.05f, step = 0.17f)
        },
        { p, f ->
            plain97(Offset(w * 0.7f, h * 0.42f), w * 0.09f, a, "mbkhadija2", 1, f * seg(p, 0.1f, 0.3f))
            sirajMunir(Offset(w * 0.3f, h * 0.5f), w * 0.03f, f, a.t, nameAt = Offset(w * 0.3f, h * 0.38f))
            tagAt(a.tag("mbkhadija2", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            plain97(Offset(w * 0.5f, h * 0.36f), w * 0.1f, a, "mbkhadija3", 1, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("mbkhadija3", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ mb-thalatha ═════════════
internal fun DrawScope.mbthalathaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val c = Offset(w * 0.5f, h * 0.55f)
    film98(a, "mbthalatha", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night122(a) },
        { p, f ->
            circle122(a, c, w * 0.28f, 10, f, gapAt = 2, filled = 0f)
            val come = easeInOut(seg(p, 0.2f, 0.8f))
            for (k in 0 until 3) person99(Offset(mix(w * 1.1f, w * 0.86f, come) + k * w * 0.06f, h * 0.88f), h * 0.09f, Fig122, f, walk = a.t * 2f)
            tagAt(a.tag("mbthalatha0", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val sit = seg(p, 0.15f, 0.35f)
            circle122(a, c, w * 0.28f, 10, f, gapAt = 2, filled = sit)
            drawCircle(Fig122, w * 0.028f, Offset(w * 0.5f, c.y + w * 0.28f * 0.45f + h * 0.08f), alpha = f * seg(p, 0.4f, 0.55f))
            person99(Offset(mix(w * 0.86f, w * 1.1f, easeInOut(seg(p, 0.6f, 0.9f))), h * 0.9f), h * 0.09f, Dim122, f, walk = a.t * 2f)
            row122(a, "mbthalatha1", 3, h * 0.12f, p, f, from = 0.15f, step = 0.25f)
        },
        { p, f ->
            veil122(f)
            row122(a, "mbthalatha2", 3, h * 0.16f, p, f, from = 0.1f, step = 0.25f, ink = ArtInk.Gold)
            softGlow(Offset(w * 0.78f, h * 0.5f), w * 0.1f, Qn.GoldLight, 0.7f * f * seg(p, 0.1f, 0.3f))
            drawCircle(Fig122, w * 0.03f, Offset(w * 0.78f, h * 0.5f), alpha = f)
            drawCircle(Fig122, w * 0.03f, Offset(w * 0.5f, h * 0.5f), alpha = f * 0.8f)
            drawCircle(Dim122, w * 0.03f, Offset(w * 0.22f, h * 0.5f), alpha = f * (1f - 0.6f * seg(p, 0.6f, 0.85f)))
        },
        { p, f ->
            circle122(a, c, w * 0.28f, 10, f, gapAt = 2, filled = 1f)
            softGlow(Offset(c.x + w * 0.28f * cos(2 / 10f * 6.283f + 1.57f), c.y + w * 0.28f * 0.45f * sin(2 / 10f * 6.283f + 1.57f)), w * 0.06f, Qn.GoldLight, 0.8f * f * seg(p, 0.2f, 0.4f))
            tagAt(a.tag("mbthalatha3", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mb-hasad ═════════════
internal fun DrawScope.mbhasadScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mbhasad", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night122(a) },
        { p, f ->
            for (k in 0 until 2) {
                val x = w * (0.68f - 0.36f * k)
                person99(Offset(x, g), h * 0.14f, Fig122, f)
                softGlow(Offset(x, g - h * 0.1f), w * 0.12f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f + 0.2f * k, 0.4f + 0.2f * k))
            }
            tagAt(a.tag("mbhasad0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
        { p, f ->
            person99(Offset(w * 0.7f, g), h * 0.14f, Fig122, f, arms = 0.6f)
            for (k in 0 until 3) person99(Offset(w * (0.16f + 0.12f * k), g), h * 0.1f, Dim122, f)
            for (k in 0 until 5) {
                val q = seg(p, 0.15f + 0.1f * k, 0.4f + 0.1f * k)
                qIcon("coin", Offset(mix(w * 0.62f, w * (0.16f + 0.12f * (k % 3)), q), g - h * 0.16f - h * 0.08f * sin(q * 3.1416f)), w * 0.035f, Qn.Gold, f * q * (1f - seg(p, 0.9f, 1f)), a.t)
            }
            tagAt(a.tag("mbhasad1", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.7f, g), h * 0.14f, Fig122, f)
            qIcon("book", Offset(w * 0.7f, g - h * 0.26f), w * 0.08f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 4) person99(Offset(w * (0.12f + 0.1f * k), g), h * 0.1f, Fig122, f * seg(p, 0.3f + 0.08f * k, 0.4f + 0.08f * k))
            tagAt(a.tag("mbhasad2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil122(f)
            person99(Offset(w * 0.7f, g), h * 0.14f, Fig122, f)
            softGlow(Offset(w * 0.7f, g - h * 0.1f), w * 0.12f, Qn.GoldLight, 0.6f * f)
            person99(Offset(w * 0.3f, g), h * 0.14f, Fig122, f)
            softGlow(Offset(w * 0.3f, g - h * 0.1f), w * 0.12f * seg(p, 0.3f, 0.7f), Qn.GoldLight, 0.6f * f * seg(p, 0.3f, 0.7f))
            tagAt(a.tag("mbhasad3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mb-musa ═════════════
private fun DrawScope.namedLight122(c: Offset, r: Float, a: ArtCtx, kind: String, part: Int, alpha: Float, cool: Boolean = false) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 3f, if (cool) Color(0xFFCFE6F2) else Qn.GoldLight, 0.7f * alpha)
    drawCircle(Color(0xFFF8F4E8), r, c, alpha = alpha)
    tagAt(a.tag(kind, 0, part), Offset(c.x, c.y - r * 3.2f), alpha, ink = ArtInk.Ivory)
}

internal fun DrawScope.mbmusaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mbmusa", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night122(a) },
        { p, f ->
            namedLight122(Offset(w * 0.5f, h * 0.42f), w * 0.03f, a, "mbmusa0", 0, f * seg(p, 0.05f, 0.25f), cool = true)
            for (k in 0 until 7) person99(Offset(w * (0.14f + 0.12f * k), g + h * 0.04f), h * 0.08f, Dim122, f)
            tagAt(a.tag("mbmusa0", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil122(f)
            tagAt(a.tag("mbmusa1", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            namedLight122(Offset(w * 0.5f, h * 0.5f), w * 0.03f * mix(1f, 0.8f, seg(p, 0.2f, 0.5f)), a, "mbmusa0", 0, f, cool = true)
        },
        { p, f ->
            val meet = easeInOut(seg(p, 0.1f, 0.6f))
            drawRect(Color(0xFF2C6A96), Offset(0f, h * 0.62f), Size(w * mix(0.3f, 0.52f, meet), h * 0.2f), alpha = f)
            drawRect(Color(0xFF4A9AB8), Offset(w * mix(0.7f, 0.48f, meet), h * 0.62f), Size(w, h * 0.2f), alpha = f)
            softGlow(Offset(w * 0.5f, h * 0.66f), w * 0.1f, Color(0xFFFFFFFF), 0.5f * f * seg(p, 0.55f, 0.7f))
            tagAt(a.tag("mbmusa2", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF2C6A96), Offset(0f, h * 0.7f), Size(w, h * 0.12f), alpha = f)
            val go = easeInOut(seg(p, 0.05f, 0.8f))
            namedLight122(Offset(mix(w * 0.9f, w * 0.4f, go), h * 0.6f), w * 0.022f, a, "mbmusa3", 0, f, cool = true)
            namedLight122(Offset(mix(w * 1.02f, w * 0.52f, go), h * 0.6f), w * 0.016f, a, "mbmusa3", 1, f * seg(p, 0.2f, 0.35f), cool = true)
        },
    )
}

// ═════════════ mb-ghayth ═════════════
private fun DrawScope.lands122(g: Float, p: Float, f: Float, t: Float, grow: Float, pool: Float) {
    val w = size.width
    val h = size.height
    drawRect(Color(0xFF6B8A4A), Offset(w * 0.67f, g - h * 0.01f), Size(w * 0.33f, h * 0.06f), alpha = f)
    drawRect(Color(0xFF9A8A6A), Offset(w * 0.34f, g - h * 0.01f), Size(w * 0.33f, h * 0.06f), alpha = f)
    drawRect(Color(0xFFC8B48A), Offset(0f, g - h * 0.01f), Size(w * 0.34f, h * 0.06f), alpha = f)
    for (k in 0 until 4) qIcon("sprout", Offset(w * (0.72f + 0.07f * k), g - h * 0.04f * grow), w * 0.07f * grow, Qn.Leaf, f * grow, t)
    drawOval(Color(0xFF6FB4E0), Offset(w * 0.4f, g - h * 0.005f), Size(w * 0.2f * pool, h * 0.03f), alpha = f * pool)
}

internal fun DrawScope.mbghaythScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mbghayth", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night122(a) },
        { p, f ->
            for (k in 0 until 3) qIcon("cloud", Offset(w * (0.2f + 0.3f * k), h * 0.18f), w * 0.16f, Color(0xFFDCE4EC), f, a.t)
            for (k in 0 until 24) {
                val ph = ((a.t * 1.2f + k * 0.37f) % 1f)
                drawLine(Color(0xFF8FC1E3), Offset(w * (0.05f + 0.04f * k), h * 0.24f + ph * h * 0.5f), Offset(w * (0.05f + 0.04f * k), h * 0.27f + ph * h * 0.5f), w * 0.004f, alpha = f * seg(p, 0.15f, 0.3f))
            }
            lands122(g, p, f, a.t, 0f, 0f)
            tagAt(a.tag("mbghayth0", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            lands122(g, p, f, a.t, easeInOut(seg(p, 0.1f, 0.45f)), easeInOut(seg(p, 0.5f, 0.8f)))
            person99(Offset(w * 0.56f, g), h * 0.09f, Fig122, f * seg(p, 0.7f, 0.85f), arms = 0.5f)
            tagAt(a.tag("mbghayth1", 0, 0), Offset(w * 0.83f, h * 0.5f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("mbghayth1", 0, 1), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            lands122(g, p, f, a.t, 1f, 1f)
            drawRect(Color(0xFFFFFFFF), Offset(0f, g - h * 0.01f), Size(w * 0.34f, h * 0.06f), alpha = 0.2f * f * seg(p, 0.2f, 0.4f))
            tagAt(a.tag("mbghayth2", 0, 0), Offset(w * 0.17f, h * 0.5f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.7f, g), h * 0.14f, Fig122, f)
            softGlow(Offset(w * 0.7f, g - h * 0.1f), w * 0.12f, Qn.GoldLight, 0.6f * f)
            for (k in 0 until 4) {
                person99(Offset(w * (0.14f + 0.1f * k), g), h * 0.1f, Fig122, f * seg(p, 0.3f + 0.08f * k, 0.4f + 0.08f * k))
                softGlow(Offset(w * (0.14f + 0.1f * k), g - h * 0.07f), w * 0.05f, Qn.GoldLight, 0.5f * f * seg(p, 0.6f + 0.06f * k, 0.7f + 0.06f * k))
            }
            tagAt(a.tag("mbghayth3", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mb-salouni ═════════════
internal fun DrawScope.mbsalouniScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mbsalouni", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night122(a) },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.38f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.26f))
            for (k in 0 until 6) {
                val x = w * (0.12f + 0.15f * k)
                person99(Offset(x, g), h * 0.1f, Dim122, f)
                qIcon("point", Offset(x, g - h * 0.2f), w * 0.03f, Qn.GoldLight, f * seg(p, 0.1f + 0.08f * k, 0.2f + 0.08f * k), a.t)
            }
            tagAt(a.tag("mbsalouni0", 0, 0), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            row122(a, "mbsalouni1", 3, h * 0.14f, p, f, from = 0.2f, step = 0.18f, ink = ArtInk.Gold)
            close97(Offset(w * 0.5f, h * 0.5f), w * 0.1f, a, "mbsalouni1", 3, 1, f * seg(p, 0.05f, 0.2f))
        },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, h * 0.42f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.3f))
            softGlow(Offset(w * 0.5f, h * 0.42f), w * 0.3f, Qn.GoldLight, 0.3f * f * seg(p, 0.2f, 0.6f))
            tagAt(a.tag("mbsalouni2", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.7f, h * 0.42f), w * 0.03f, f, a.t, named = false)
            for (k in 0 until 3) {
                val q = seg(p, 0.1f + 0.22f * k, 0.3f + 0.22f * k)
                val r = w * (0.06f + 0.06f * k)
                drawArc(Qn.GoldLight, 140f, 80f, false, Offset(w * 0.66f - r, h * 0.42f - r), Size(2 * r, 2 * r), alpha = f * q, style = Stroke(w * 0.008f, cap = StrokeCap.Round))
            }
            person99(Offset(w * 0.24f, g), h * 0.12f, Fig122, f)
            softGlow(Offset(w * 0.24f, g - h * 0.17f), w * 0.06f, Qn.GoldLight, 0.6f * f * seg(p, 0.75f, 0.9f))
            tagAt(a.tag("mbsalouni3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ mb-niswa ═════════════
internal fun DrawScope.mbniswaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "mbniswa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night122(a) },
        { p, f ->
            sirajMunir(Offset(w * 0.72f, h * 0.5f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.72f, h * 0.38f))
            val come = easeInOut(seg(p, 0.05f, 0.5f))
            for (k in 0 until 4) woman12(Offset(mix(w * -0.05f, w * 0.42f, come) - k * w * 0.08f, g), h * 0.12f, Mom122, f, walk = a.t * 2f * (1f - come))
            tagAt(a.tag("mbniswa0", 0, 0), Offset(w * 0.4f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.55f)
            sirajMunir(c, w * 0.035f, f, a.t, nameAt = Offset(c.x, c.y - h * 0.12f))
            for (k in 0 until 8) {
                val ang = k / 8f * 6.283f + 0.4f
                woman12(Offset(c.x + w * 0.32f * cos(ang), c.y + h * 0.2f + h * 0.08f * sin(ang)), h * 0.08f, Mom122, f)
            }
            tagAt(a.tag("mbniswa1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
        { p, f ->
            woman12(Offset(w * 0.5f, g - h * 0.12f), h * 0.14f, Mom122, f)
            drawRect(Qn.Ember, Offset(0f, g + h * 0.02f), Size(w, h * 0.08f), alpha = 0.5f * f)
            for (k in 0 until 3) {
                val c = Offset(w * (0.36f + 0.14f * k), g)
                softGlow(c, w * 0.08f, Qn.GoldLight, 0.7f * f * seg(p, 0.15f + 0.12f * k, 0.3f + 0.12f * k))
                drawCircle(Color(0xFFF8F4E8), w * 0.025f, c, alpha = f * seg(p, 0.15f + 0.12f * k, 0.3f + 0.12f * k))
            }
            tagAt(a.tag("mbniswa2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            mother102(Offset(w * 0.68f, h * 0.42f), w * 0.1f, a, "mbniswa3", 1, 1, f * seg(p, 0.05f, 0.25f))
            for (k in 0 until 3) qIcon("point", Offset(w * (0.42f - 0.08f * k), h * (0.36f + 0.03f * k)), w * 0.03f, Qn.GoldLight, f * seg(p, 0.3f + 0.12f * k, 0.4f + 0.12f * k), a.t)
            tagAt(a.tag("mbniswa3", 0, 0), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
    )
}
