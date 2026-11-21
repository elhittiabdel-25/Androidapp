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
 * Wave 111 (2026-11-17): «من هديه ﷺ» (2) from Mukhtasar Zad al-Ma'ad: medicine, ruqyah, distress, dreams,
 * whispers, anger, names and words. He ﷺ is only his named light; Jibril cool white light; named people plain
 * roundels; the devil is never drawn (a dark cloud at most). Every Name of Allah sits in the upper tags.
 */

private val Good111 = Color(0xCC1A3020)
private val Bad111 = Color(0xCC401A20)
private val Water111 = Color(0xFF9CC8E8)

private fun DrawScope.night111(a: ArtCtx, ground: Float) {
    nightSky(a.t, S97.Night, S97.NightLow, ground, 0.7f)
    dunes97(ground, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil111(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.6f * alpha)

private fun DrawScope.no111(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(Qn.Ember, r, c, alpha = alpha, style = Stroke(2.5f * density))
    drawLine(Qn.Ember, c + Offset(-r * 0.7f, -r * 0.7f), c + Offset(r * 0.7f, r * 0.7f), 2.5f * density, alpha = alpha)
}

/** Tags [parts] of [kind] in a row, right to left, at height [y], each appearing in turn from [from]. */
private fun DrawScope.row111(a: ArtCtx, kind: String, parts: List<Int>, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             back: Color = Color(0xB3101426), ink: Color = ArtInk.Ivory) {
    val n = parts.size
    parts.forEachIndexed { i, part ->
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.8f / n.coerceAtLeast(1)))
        tagAt(a.tag(kind, 0, part), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.15f), back = back, ink = ink)
    }
}

/** A cup at [c], [fill] 0..1 of [color]. */
private fun DrawScope.cup111(c: Offset, s: Float, fill: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val fl = fill.coerceIn(0f, 1f)
    drawRoundRect(color, c + Offset(-s * 0.4f, s * 0.5f - s * fl), Size(s * 0.8f, s * fl), CornerRadius(s * 0.15f), alpha = 0.85f * alpha)
    drawRoundRect(Color(0xFFD8DCE4), c + Offset(-s * 0.45f, -s * 0.5f), Size(s * 0.9f, s), CornerRadius(s * 0.15f), alpha = alpha, style = Stroke(1.6f * density))
}

private fun DrawScope.smoke111(c: Offset, s: Float, t: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 4) drawCircle(Color(0xFF2E2836), s * (0.5f + 0.15f * k), c + Offset(s * 0.5f * sin(t * 1.3f + k), -s * 0.3f * k), alpha = 0.7f * alpha)
}

// ═════════════ hd-asbab ═════════════
internal fun DrawScope.hdasbabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdasbab", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground) },
        { p, f ->
            veil111(f)
            tagAt(a.tag("hdasbab0", 0, 2), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
            qIcon("flame", Offset(w * 0.7f, h * 0.42f), w * 0.07f, Qn.Ember, f * seg(p, 0.05f, 0.2f), a.t)
            cup111(Offset(w * 0.3f, h * 0.42f), w * 0.08f, seg(p, 0.3f, 0.6f), Qn.Jade, f * seg(p, 0.25f, 0.35f))
            drawLine(Qn.GoldLight, Offset(w * 0.4f, h * 0.42f), Offset(w * 0.6f, h * 0.42f), 1.5f * density, alpha = f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("hdasbab0", 0, 0), Offset(w * 0.7f, h * 0.58f), f * seg(p, 0.1f, 0.25f), back = Bad111, ink = ArtInk.Ivory)
            tagAt(a.tag("hdasbab0", 0, 1), Offset(w * 0.3f, h * 0.58f), f * seg(p, 0.3f, 0.45f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            for (k in 0 until 3) {
                val y = h * (0.3f + 0.14f * k)
                qIcon("lock", Offset(w * 0.68f, y), w * 0.05f, Color(0xFF9A8A80), f * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k), a.t)
                val go = easeInOut(seg(p, 0.35f + 0.1f * k, 0.6f + 0.1f * k))
                qIcon("key", Offset(mix(w * 0.3f, w * 0.6f, go), y), w * 0.05f, Qn.Gold, f * seg(p, 0.2f, 0.3f), a.t)
            }
            tagAt(a.tag("hdasbab1", 0, 0), Offset(w * 0.75f, h * 0.76f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdasbab1", 0, 1), Offset(w * 0.3f, h * 0.76f), f * seg(p, 0.45f, 0.6f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            qIcon("hands", Offset(w * 0.7f, h * 0.38f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("heart", Offset(w * 0.3f, h * 0.38f), w * 0.08f, Qn.Rose, f * seg(p, 0.3f, 0.45f), a.t)
            drawLine(Qn.GoldLight, Offset(w * 0.4f, h * 0.38f), Offset(w * 0.6f, h * 0.38f), 2f * density, alpha = f * seg(p, 0.55f, 0.7f))
            tagAt(a.tag("hdasbab2", 0, 0), Offset(w * 0.7f, h * 0.56f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdasbab2", 0, 1), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdasbab2", 0, 2), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.6f, 0.75f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            qIcon("check", Offset(w * 0.5f, h * 0.38f), w * 0.08f, Qn.Jade, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("hdasbab3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("hdasbab3", 0, 1), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ hd-haram ═════════════
internal fun DrawScope.hdharamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdharam", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground) },
        { p, f ->
            veil111(f)
            cup111(Offset(w * 0.5f, h * 0.4f), w * 0.1f, 0.7f, Color(0xFF8A2A3A), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("hdharam0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdharam0", 0, 1), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            cup111(Offset(w * 0.5f, h * 0.4f), w * 0.1f, 0.7f, Color(0xFF8A2A3A), f)
            no111(Offset(w * 0.5f, h * 0.4f), w * 0.1f, f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("hdharam1", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.15f, 0.3f), back = Bad111, ink = ArtInk.Ivory)
            tagAt(a.tag("hdharam1", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.5f, 0.65f), back = Bad111, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            qIcon("lock", Offset(w * 0.5f, h * 0.38f), w * 0.09f, Color(0xFF9A8A80), f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("hdharam2", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdharam2", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.45f, 0.6f), back = Bad111, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            qIcon("heart", Offset(w * 0.7f, h * 0.4f), w * 0.08f, Qn.Rose, f * seg(p, 0.05f, 0.2f), a.t)
            cup111(Offset(w * 0.3f, h * 0.4f), w * 0.08f, 0.6f, Qn.Jade, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("hdharam3", 0, 0), Offset(w * 0.7f, h * 0.58f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("hdharam3", 0, 1), Offset(w * 0.3f, h * 0.58f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ hd-humma ═════════════
internal fun DrawScope.hdhummaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdhumma", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground) },
        { p, f ->
            veil111(f)
            val cool = seg(p, 0.4f, 0.8f)
            qIcon("flame", Offset(w * 0.5f, h * 0.42f), w * (0.1f - 0.05f * cool), Qn.Ember, f * seg(p, 0.05f, 0.2f) * (1f - 0.6f * cool), a.t)
            for (k in 0 until 5) {
                val ph = (a.t * 0.8f + k / 5f) % 1f
                qIcon("drop", Offset(w * (0.4f + 0.05f * k), mix(h * 0.18f, h * 0.38f, ph)), w * 0.02f, Water111, f * seg(p, 0.3f, 0.45f) * (1f - ph), a.t)
            }
            tagAt(a.tag("hdhumma0", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.1f, 0.25f), back = Bad111, ink = ArtInk.Ivory)
            tagAt(a.tag("hdhumma0", 0, 1), Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.45f, 0.6f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            val n = (seg(p, 0.1f, 0.9f) * 3f).toInt().coerceAtMost(2)
            cup111(Offset(w * 0.5f, h * 0.4f), w * 0.1f, ((seg(p, 0.1f, 0.9f) * 3f) % 1f), Qn.Gold, f)
            for (k in 0..n) drawCircle(Qn.Gold, w * 0.012f, Offset(w * (0.44f + 0.06f * k), h * 0.56f), alpha = f)
            tagAt(a.tag("hdhumma1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("hdhumma1", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            tagAt(a.tag("hdhumma2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            cup111(Offset(w * 0.5f, h * 0.42f), w * 0.09f, 0.8f, Qn.Gold, f)
            tagAt(a.tag("hdhumma2", 0, 1), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            qIcon("sprout", Offset(w * 0.7f, h * 0.4f), w * 0.08f, Qn.Leaf, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 4) qIcon("sprout", Offset(w * (0.2f + 0.05f * k), h * (0.38f + 0.03f * (k % 2))), w * 0.04f, Color(0xFF8A9A70), f * seg(p, 0.4f, 0.55f), a.t)
            no111(Offset(w * 0.27f, h * 0.4f), w * 0.09f, f * seg(p, 0.6f, 0.72f))
            tagAt(a.tag("hdhumma3", 0, 0), Offset(w * 0.7f, h * 0.58f), f * seg(p, 0.1f, 0.25f), back = Good111, ink = ArtInk.Jade)
            tagAt(a.tag("hdhumma3", 0, 1), Offset(w * 0.27f, h * 0.58f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ hd-taun ═════════════
internal fun DrawScope.hdtaunScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    val town = Offset(w * 0.55f, ground)
    film98(a, "hdtaun", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground); for (k in 0 until 3) house12(Offset(w * (0.42f + 0.13f * k), ground), w * 0.1f, Color(0xFFE2CCA0), Color(0xFF8A6A40), 0.6f) },
        { p, f ->
            smoke111(Offset(w * 0.55f, h * 0.4f), w * 0.06f, a.t, f * seg(p, 0.05f, 0.3f))
            tagAt(a.tag("hdtaun0", 0, 0), Offset(w * 0.55f, h * 0.16f), f * seg(p, 0.2f, 0.35f), back = Bad111, ink = ArtInk.Ivory)
            tagAt(a.tag("hdtaun0", 0, 1), Offset(w * 0.55f, h * 0.26f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawCircle(Qn.Ember, w * 0.26f, town + Offset(0f, -h * 0.05f), alpha = 0.6f * f * seg(p, 0.05f, 0.2f), style = Stroke(2f * density))
            person99(Offset(w * 0.12f, ground), h * 0.07f, Qn.Person, f * seg(p, 0.1f, 0.25f))
            no111(Offset(w * 0.2f, h * 0.6f), w * 0.04f, f * seg(p, 0.25f, 0.35f))
            person99(Offset(w * 0.6f, ground), h * 0.07f, Qn.Person, f * seg(p, 0.45f, 0.55f))
            tagAt(a.tag("hdtaun1", 0, 0), Offset(w * 0.2f, h * 0.36f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdtaun1", 0, 1), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            softGlow(town + Offset(0f, -h * 0.1f), w * 0.3f, Qn.GoldLight, 0.35f * f * seg(p, 0.2f, 0.5f))
            tagAt(a.tag("hdtaun2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.25f, 0.4f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.38f), w * 0.13f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("hdtaun3", 0, 0), Offset(w * 0.72f, h * 0.6f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdtaun3", 0, 1), Offset(w * 0.28f, h * 0.6f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ hd-ayn ═════════════
internal fun DrawScope.hdaynScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdayn", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground) },
        { p, f ->
            veil111(f)
            qIcon("eye", Offset(w * 0.5f, h * 0.38f), w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("hdayn0", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("hdayn0", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            plain97(Offset(w * 0.72f, h * 0.36f), w * 0.08f, a, "hdayn1", 0, f * seg(p, 0.05f, 0.2f))
            val fall = seg(p, 0.55f, 0.75f)
            plain97(Offset(w * 0.28f, h * (0.36f + 0.12f * fall)), w * 0.08f, a, "hdayn1", 1, f * seg(p, 0.15f, 0.3f) * (1f - 0.5f * fall))
            drawLine(Qn.Ember, Offset(w * 0.62f, h * 0.36f), Offset(w * 0.38f, h * 0.38f), 2f * density, StrokeCap.Round, alpha = f * seg(p, 0.35f, 0.5f))
        },
        { p, f ->
            veil111(f)
            cup111(Offset(w * 0.5f, h * 0.4f), w * 0.09f, seg(p, 0.3f, 0.6f), Water111, f * seg(p, 0.25f, 0.35f))
            tagAt(a.tag("hdayn2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("hdayn2", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            qIcon("hands", Offset(w * 0.7f, h * 0.38f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            drawRoundRect(Color(0xFFE2D6BC), Offset(w * 0.2f, h * 0.32f), Size(w * 0.18f, h * 0.12f), CornerRadius(8f * density), alpha = 0.7f * f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("hdayn3", 0, 0), Offset(w * 0.7f, h * 0.56f), f * seg(p, 0.1f, 0.25f), back = Good111, ink = ArtInk.Jade)
            tagAt(a.tag("hdayn3", 0, 1), Offset(w * 0.29f, h * 0.56f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ hd-ruqya ═════════════
internal fun DrawScope.hdruqyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdruqya", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground) },
        { p, f ->
            tagAt(a.tag("hdruqya0", 0, 1), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            angel98(Offset(w * 0.72f, h * 0.38f), w * 0.05f, f * seg(p, 0.05f, 0.25f))
            sirajMunir(Offset(w * 0.3f, h * 0.42f), w * 0.035f, f, a.t, named = false)
            tagAt(a.tag("hdruqya0", 0, 0), Offset(w * 0.72f, h * 0.56f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val rise = easeInOut(seg(p, 0.55f, 0.85f))
            person99(Offset(w * 0.3f, ground), h * 0.075f, Qn.Person, f, lean = 1.4f * (1f - rise))
            qIcon("scroll", Offset(w * 0.68f, h * 0.48f), w * 0.09f, Color(0xFFD8C4A8), f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("hdruqya1", 0, 0), Offset(w * 0.3f, h * 0.48f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdruqya1", 0, 1), Offset(w * 0.68f, h * 0.64f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil111(f)
            sirajMunir(Offset(w * 0.5f, h * 0.36f), w * 0.04f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.14f))
            tagAt(a.tag("hdruqya2", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.5f, 0.65f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.56f), w * 0.1f, Color(0xFFD8C4A8), f * seg(p, 0.05f, 0.2f), a.t)
            row111(a, "hdruqya3", listOf(0, 1, 2), h * 0.3f, p, f, ink = ArtInk.Gold)
        },
    )
}

// ═════════════ hd-karb ═════════════
internal fun DrawScope.hdkarbScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdkarb", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground) },
        { p, f ->
            row111(a, "hdkarb0", listOf(0, 1), h * 0.18f, p, f, from = 0.3f, ink = ArtInk.Gold)
            val part = easeInOut(seg(p, 0.4f, 0.8f))
            qIcon("cloud", Offset(w * (0.5f - 0.25f * part), h * 0.42f), w * 0.12f, Color(0xFF4A4458), f * (1f - 0.5f * part), a.t)
            qIcon("cloud", Offset(w * (0.5f + 0.25f * part), h * 0.42f), w * 0.12f, Color(0xFF4A4458), f * (1f - 0.5f * part), a.t)
            sirajMunir(Offset(w * 0.5f, h * 0.44f), w * 0.03f, f * part, a.t, named = false)
        },
        { p, f ->
            veil111(f)
            tagAt(a.tag("hdkarb1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("hdkarb1", 0, 1), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
            qIcon("hands", Offset(w * 0.5f, h * 0.5f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
        },
        { p, f ->
            veil111(f)
            tagAt(a.tag("hdkarb2", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            plain97(Offset(w * 0.5f, h * 0.46f), w * 0.09f, a, "hdkarb2", 1, f * seg(p, 0.05f, 0.25f))
        },
        { p, f ->
            veil111(f)
            for (k in 0 until 3) tagAt(a.tag("hdkarb3", 0, k), Offset(w * 0.5f, h * (0.16f + 0.1f * k)), f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k), ink = ArtInk.Gold)
            qIcon("light", Offset(w * 0.5f, h * 0.58f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.5f, 0.7f), a.t)
        },
    )
}

// ═════════════ hd-manam ═════════════
internal fun DrawScope.hdmanamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdmanam", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground); moon12(Offset(w * 0.82f, h * 0.14f), w * 0.04f, 0.9f) },
        { p, f ->
            veil111(f)
            qIcon("star", Offset(w * 0.7f, h * 0.38f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            smoke111(Offset(w * 0.3f, h * 0.42f), w * 0.04f, a.t, f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("hdmanam0", 0, 0), Offset(w * 0.7f, h * 0.58f), f * seg(p, 0.1f, 0.25f), back = Good111, ink = ArtInk.Jade)
            tagAt(a.tag("hdmanam0", 0, 1), Offset(w * 0.3f, h * 0.58f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            val icons = listOf("lips", "light", "lock")
            for (k in 0 until 3) {
                val c = Offset(w * (0.78f - 0.28f * k), h * 0.4f)
                val on = f * seg(p, 0.1f + 0.22f * k, 0.25f + 0.22f * k)
                qIcon(icons[k], c, w * 0.06f, Qn.GoldLight, on, a.t)
                tagAt(a.tag("hdmanam1", 0, k), c + Offset(0f, h * 0.13f), on, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil111(f)
            qIcon("bed", Offset(w * 0.7f, h * 0.42f), w * 0.11f, Qn.Lapis, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("mat", Offset(w * 0.3f, h * 0.42f), w * 0.11f, Qn.Jade, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("hdmanam2", 0, 0), Offset(w * 0.7f, h * 0.6f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("hdmanam2", 0, 1), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.5f, 0.65f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            qIcon("nest", Offset(w * 0.5f, h * 0.4f), w * 0.09f, Color(0xFFB09868), f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("hdmanam3", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("hdmanam3", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ hd-waswasa ═════════════
internal fun DrawScope.hdwaswasaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdwaswasa", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground) },
        { p, f ->
            veil111(f)
            plain97(Offset(w * 0.68f, h * 0.38f), w * 0.08f, a, "hdwaswasa0", 0, f * seg(p, 0.05f, 0.25f))
            qIcon("mat", Offset(w * 0.68f, h * 0.58f), w * 0.08f, Qn.Jade, f * seg(p, 0.1f, 0.25f), a.t)
            smoke111(Offset(w * 0.3f, h * 0.44f), w * 0.04f, a.t, f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("hdwaswasa0", 0, 1), Offset(w * 0.3f, h * 0.62f), f * seg(p, 0.55f, 0.7f), back = Bad111, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            tagAt(a.tag("hdwaswasa1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            qIcon("lock", Offset(w * 0.5f, h * 0.42f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f), a.t)
            smoke111(Offset(w * (0.3f - 0.15f * seg(p, 0.4f, 0.8f)), h * 0.44f), w * 0.03f, a.t, f * (1f - seg(p, 0.6f, 0.9f)))
            tagAt(a.tag("hdwaswasa1", 0, 1), Offset(w * 0.5f, h * 0.64f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            tagAt(a.tag("hdwaswasa2", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            smoke111(Offset(w * 0.5f, h * 0.46f), w * mix(0.07f, 0.02f, seg(p, 0.3f, 0.7f)), a.t, f)
            tagAt(a.tag("hdwaswasa2", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            val icons = listOf("drop", "mat", "lock")
            for (k in 0 until 3) {
                val c = Offset(w * (0.78f - 0.28f * k), h * 0.4f)
                val on = f * seg(p, 0.1f + 0.22f * k, 0.25f + 0.22f * k)
                qIcon(icons[k], c, w * 0.06f, Qn.Jade, on, a.t)
                tagAt(a.tag("hdwaswasa3", 0, k), c + Offset(0f, h * 0.13f), on, ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ hd-asma ═════════════
internal fun DrawScope.hdasmaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdasma", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground) },
        { p, f ->
            veil111(f)
            row111(a, "hdasma0", listOf(0, 1), h * 0.22f, p, f, from = 0.05f, back = Good111, ink = ArtInk.Jade)
            row111(a, "hdasma0", listOf(2, 3), h * 0.42f, p, f, from = 0.4f, ink = ArtInk.Ivory)
            row111(a, "hdasma0", listOf(4, 5), h * 0.62f, p, f, from = 0.65f, back = Bad111, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            qIcon("crown", Offset(w * 0.5f, h * 0.42f), w * 0.1f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            no111(Offset(w * 0.5f, h * 0.42f), w * 0.1f, f * seg(p, 0.3f, 0.42f))
            tagAt(a.tag("hdasma1", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
            tagAt(a.tag("hdasma1", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.1f, 0.25f), back = Bad111, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            for (r in 0 until 2) {
                val y = h * (0.32f + 0.24f * r)
                tagAt(a.tag("hdasma2", 0, 2 * r), Offset(w * 0.72f, y), f * seg(p, 0.05f + 0.35f * r, 0.2f + 0.35f * r), back = Bad111, ink = ArtInk.Ivory)
                drawLine(Qn.GoldLight, Offset(w * 0.6f, y), Offset(w * 0.42f, y), 2f * density, StrokeCap.Round, alpha = f * seg(p, 0.15f + 0.35f * r, 0.25f + 0.35f * r))
                tagAt(a.tag("hdasma2", 0, 2 * r + 1), Offset(w * 0.28f, y), f * seg(p, 0.2f + 0.35f * r, 0.35f + 0.35f * r), back = Good111, ink = ArtInk.Jade)
            }
        },
        { p, f ->
            veil111(f)
            row111(a, "hdasma3", listOf(0, 1, 2, 3), h * 0.42f, p, f, step = 0.12f)
        },
    )
}

// ═════════════ hd-alfadh ═════════════
internal fun DrawScope.hdalfadhScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "hdalfadh", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night111(a, ground) },
        { p, f ->
            veil111(f)
            qIcon("lips", Offset(w * 0.5f, h * 0.38f), w * 0.09f, Color(0xFF9A8A80), f * seg(p, 0.05f, 0.2f), a.t)
            no111(Offset(w * 0.5f, h * 0.38f), w * 0.1f, f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("hdalfadh0", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.15f, 0.3f), back = Bad111, ink = ArtInk.Ivory)
            tagAt(a.tag("hdalfadh0", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil111(f)
            tagAt(a.tag("hdalfadh1", 0, 0), Offset(w * 0.72f, h * 0.42f), f * seg(p, 0.1f, 0.25f), back = Bad111, ink = ArtInk.Ivory)
            drawLine(Qn.GoldLight, Offset(w * 0.6f, h * 0.42f), Offset(w * 0.42f, h * 0.42f), 2f * density, StrokeCap.Round, alpha = f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("hdalfadh1", 0, 1), Offset(w * 0.28f, h * 0.42f), f * seg(p, 0.5f, 0.65f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            val shrink = seg(p, 0.45f, 0.8f)
            smoke111(Offset(w * 0.5f, h * 0.46f), w * mix(0.02f, 0.08f, seg(p, 0.05f, 0.35f)) * (1f - 0.8f * shrink), a.t, f)
            tagAt(a.tag("hdalfadh2", 0, 0), Offset(w * 0.72f, h * 0.2f), f * seg(p, 0.05f, 0.2f) * (1f - 0.6f * shrink), back = Bad111, ink = ArtInk.Ivory)
            tagAt(a.tag("hdalfadh2", 0, 1), Offset(w * 0.28f, h * 0.2f), f * seg(p, 0.45f, 0.6f), back = Good111, ink = ArtInk.Jade)
        },
        { p, f ->
            veil111(f)
            tagAt(a.tag("hdalfadh3", 0, 0), Offset(w * 0.72f, h * 0.42f), f * seg(p, 0.1f, 0.25f), back = Bad111, ink = ArtInk.Ivory)
            no111(Offset(w * 0.72f, h * 0.42f), w * 0.13f, f * seg(p, 0.3f, 0.42f))
            tagAt(a.tag("hdalfadh3", 0, 1), Offset(w * 0.28f, h * 0.42f), f * seg(p, 0.5f, 0.65f), back = Good111, ink = ArtInk.Jade)
        },
    )
}
