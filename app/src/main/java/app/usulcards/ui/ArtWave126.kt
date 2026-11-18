package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 126 (2026-11-18): the matn of Abu Shuja': heirs, the six shares (drawn as parts of a disc), the bequest of a
 * third, the guardian and witnesses, maintenance and custody, the kinds of killing (no violence shown: a target and
 * a scale), oaths and vows, the judge. People are faceless.
 */

private val Fig126 = Color(0xFFEDE4D0)
private val Dim126 = Color(0xFF8A8296)
private val Mom126 = Color(0xFFD8C2E0)

private fun DrawScope.night126(a: ArtCtx) {
    nightSky(a.t, Qn.Night, Qn.NightLow, size.height * 0.8f, 0.7f)
    dunes97(size.height * 0.8f, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil126(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Tags in a row, read right to left (first part on the right). */
private fun DrawScope.row126(a: ArtCtx, kind: String, n: Int, y: Float, p: Float, f: Float, from: Float = 0.1f, step: Float = 0.15f,
                             ink: Color = ArtInk.Ivory) {
    for (i in 0 until n) {
        val x = size.width * (0.5f + ((n - 1) / 2f - i) * (0.84f / n))
        tagAt(a.tag(kind, 0, i), Offset(x, y), f * seg(p, from + step * i, from + step * i + 0.14f), ink = ink)
    }
}

/** A disc with the share `frac` filled (from the top, clockwise). */
private fun DrawScope.pie126(c: Offset, r: Float, frac: Float, color: Color, alpha: Float) {
    drawCircle(Color(0x33FFFFFF), r, c, alpha = alpha)
    drawArc(color, -90f, 360f * frac, true, Offset(c.x - r, c.y - r), Size(2 * r, 2 * r), alpha = alpha)
    drawCircle(Qn.GoldLight, r, c, alpha = alpha, style = Stroke(r * 0.06f))
}

// ═════════════ ag-waritha ═════════════
internal fun DrawScope.agwarithaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "agwaritha", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night126(a) },
        { p, f ->
            for (k in 0 until 10) person99(Offset(w * (0.92f - 0.093f * k), g), h * 0.09f, Fig126, f * seg(p, 0.05f + 0.06f * k, 0.12f + 0.06f * k))
            tagAt(a.tag("agwaritha0", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 7) woman12(Offset(w * (0.86f - 0.12f * k), g), h * 0.1f, Mom126, f * seg(p, 0.05f + 0.08f * k, 0.12f + 0.08f * k))
            tagAt(a.tag("agwaritha1", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil126(f)
            for (k in 0 until 5) {
                val x = w * (0.84f - 0.17f * k)
                if (k == 1 || k == 3) woman12(Offset(x, g), h * 0.11f, Mom126, f) else person99(Offset(x, g), h * 0.11f, Fig126, f)
                softGlow(Offset(x, g - h * 0.07f), w * 0.07f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f + 0.08f * k, 0.35f + 0.08f * k))
            }
            row126(a, "agwaritha2", 3, h * 0.3f, p, f, from = 0.2f, step = 0.2f)
        },
        { p, f ->
            veil126(f)
            row126(a, "agwaritha3", 3, h * 0.45f, p, f, from = 0.1f, step = 0.22f)
            for (i in 0 until 3) qIcon("cross", Offset(w * (0.5f + (1 - i) * 0.28f), h * 0.6f), w * 0.05f, Qn.Rose, f * seg(p, 0.2f + 0.22f * i, 0.32f + 0.22f * i), a.t)
        },
    )
}

// ═════════════ ag-furud ═════════════
internal fun DrawScope.agfurudScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shares = listOf(0.5f, 0.25f, 0.125f, 0.6667f, 0.3333f, 0.1667f)
    film98(a, "agfurud", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night126(a) },
        { p, f ->
            veil126(f)
            for (i in 0 until 6) {
                val c = Offset(w * (0.78f - 0.28f * (i % 3)), h * (0.32f + 0.3f * (i / 3)))
                val q = seg(p, 0.05f + 0.12f * i, 0.18f + 0.12f * i)
                pie126(c, w * 0.08f, shares[i] * q, Qn.Gold, f * seg(p, 0.02f + 0.12f * i, 0.1f + 0.12f * i))
                tagAt(a.tag("agfurud0", 0, i), Offset(c.x, c.y + w * 0.12f), f * q, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil126(f)
            pie126(Offset(w * 0.5f, h * 0.42f), w * 0.16f, 0.5f * easeInOut(seg(p, 0.1f, 0.4f)), Qn.Gold, f)
            row126(a, "agfurud1", 2, h * 0.72f, p, f, from = 0.3f, step = 0.3f)
        },
        { p, f ->
            veil126(f)
            pie126(Offset(w * 0.7f, h * 0.42f), w * 0.13f, 0.25f * easeInOut(seg(p, 0.1f, 0.35f)), Qn.Jade, f)
            pie126(Offset(w * 0.3f, h * 0.42f), w * 0.13f, 0.125f * easeInOut(seg(p, 0.55f, 0.8f)), Qn.Rose, f * seg(p, 0.45f, 0.55f))
            tagAt(a.tag("agfurud2", 0, 0), Offset(w * 0.7f, h * 0.68f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("agfurud2", 0, 1), Offset(w * 0.3f, h * 0.68f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil126(f)
            pie126(Offset(w * 0.7f, h * 0.42f), w * 0.13f, 0.6667f * easeInOut(seg(p, 0.1f, 0.35f)), Qn.Gold, f)
            pie126(Offset(w * 0.3f, h * 0.42f), w * 0.13f, 0.3333f * easeInOut(seg(p, 0.5f, 0.75f)), Qn.Gold, f * seg(p, 0.4f, 0.5f))
            tagAt(a.tag("agfurud3", 0, 0), Offset(w * 0.7f, h * 0.68f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("agfurud3", 0, 1), Offset(w * 0.3f, h * 0.68f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ag-wasiyya ═════════════
internal fun DrawScope.agwasiyyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "agwasiyya", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night126(a) },
        { p, f ->
            veil126(f)
            qIcon("scroll", Offset(w * 0.72f, h * 0.4f), w * 0.14f, Qn.Parch, f, a.t)
            pie126(Offset(w * 0.32f, h * 0.42f), w * 0.13f, 0.3333f * easeInOut(seg(p, 0.25f, 0.6f)), Qn.Gold, f)
            tagAt(a.tag("agwasiyya0", 0, 0), Offset(w * 0.32f, h * 0.7f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil126(f)
            pie126(Offset(w * 0.5f, h * 0.36f), w * 0.13f, mix(0.3333f, 0.45f, seg(p, 0.1f, 0.3f)), Qn.Ember, f)
            qIcon("hourglass", Offset(w * 0.76f, h * 0.36f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.35f, 0.5f), a.t)
            for (k in 0 until 4) person99(Offset(w * (0.2f + 0.18f * k), g), h * 0.1f, Fig126, f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("agwasiyya1", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            for (k in 0 until 4) person99(Offset(w * (0.2f + 0.18f * k), g), h * 0.11f, if (k == 1) Fig126 else Dim126, f)
            qIcon("cross", Offset(w * 0.38f, h * 0.48f), w * 0.06f, Qn.Rose, f * seg(p, 0.15f, 0.3f) * (1f - seg(p, 0.65f, 0.8f)), a.t)
            qIcon("check", Offset(w * 0.38f, h * 0.48f), w * 0.06f, Qn.Jade, f * seg(p, 0.7f, 0.85f), a.t)
            tagAt(a.tag("agwasiyya2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
        },
        { p, f ->
            person99(Offset(w * 0.72f, g), h * 0.13f, Fig126, f, arms = 0.6f)
            drawRect(Color(0xFFCDB58A), Offset(w * 0.1f, g - h * 0.16f), Size(w * 0.32f, h * 0.16f), alpha = f)
            drawArc(Color(0xFFD8C08E), 180f, 180f, true, Offset(w * 0.18f, g - h * 0.28f), Size(w * 0.16f, h * 0.24f), alpha = f)
            qIcon("coin", Offset(mix(w * 0.66f, w * 0.3f, easeInOut(seg(p, 0.2f, 0.6f))), g - h * 0.2f), w * 0.04f, Qn.Gold, f, a.t)
            tagAt(a.tag("agwasiyya3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ag-wali ═════════════
internal fun DrawScope.agwaliScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "agwali", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night126(a) },
        { p, f ->
            person99(Offset(w * 0.5f, g), h * 0.14f, Fig126, f)
            softGlow(Offset(w * 0.5f, g - h * 0.1f), w * 0.1f, Qn.GoldLight, 0.5f * f * seg(p, 0.1f, 0.3f))
            for (s in listOf(-1f, 1f)) person99(Offset(w * (0.5f + 0.28f * s), g), h * 0.12f, Fig126, f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("agwali0", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("agwali0", 0, 1), Offset(w * 0.5f, h * 0.44f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil126(f)
            for (i in 0 until 6) {
                val col = i % 2
                val row = i / 2
                tagAt(a.tag("agwali1", 0, i), Offset(w * (0.7f - 0.4f * col), h * (0.28f + 0.17f * row)), f * seg(p, 0.05f + 0.12f * i, 0.17f + 0.12f * i), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            for (k in 0 until 6) {
                val x = w * (0.9f - 0.15f * k)
                person99(Offset(x, g), h * (0.12f - 0.006f * k), Fig126, f * seg(p, 0.05f + 0.1f * k, 0.15f + 0.1f * k))
                drawCircle(Qn.Gold, w * 0.016f, Offset(x, g + h * 0.06f), alpha = f * seg(p, 0.05f + 0.1f * k, 0.15f + 0.1f * k))
            }
            tagAt(a.tag("agwali2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil126(f)
            tagAt(a.tag("agwali3", 0, 0), Offset(w * 0.7f, h * 0.42f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            qIcon("cross", Offset(w * 0.7f, h * 0.56f), w * 0.05f, Qn.Rose, f * seg(p, 0.25f, 0.35f), a.t)
            tagAt(a.tag("agwali3", 0, 1), Offset(w * 0.3f, h * 0.42f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
            qIcon("check", Offset(w * 0.3f, h * 0.56f), w * 0.05f, Qn.Jade, f * seg(p, 0.65f, 0.75f), a.t)
        },
    )
}

// ═════════════ ag-nafaqa ═════════════
internal fun DrawScope.agnafaqaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "agnafaqa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night126(a) },
        { p, f ->
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                for (k in 0 until 3 - i) qIcon("coin", Offset(x - w * 0.03f + k * w * 0.03f, h * 0.45f), w * 0.04f, Qn.Gold, f * seg(p, 0.1f + 0.2f * i, 0.25f + 0.2f * i), a.t)
            }
            row126(a, "agnafaqa0", 3, h * 0.62f, p, f, from = 0.1f, step = 0.2f)
        },
        { p, f ->
            house12(Offset(w * 0.5f, g), w * 0.45f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.6f)
            qIcon("cup", Offset(w * 0.4f, g - h * 0.32f), w * 0.05f, Qn.Gold, f * seg(p, 0.2f, 0.35f), a.t)
            qIcon("wheat", Offset(w * 0.6f, g - h * 0.32f), w * 0.05f, Qn.Gold, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("agnafaqa1", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            woman12(Offset(w * 0.6f, g), h * 0.13f, Mom126, f)
            child12(Offset(w * 0.5f, g), h * 0.08f, Fig126, f)
            for (k in 0 until 7) drawCircle(Qn.Gold, w * 0.014f, Offset(w * (0.82f - 0.1f * k), h * 0.24f), alpha = f * seg(p, 0.2f + 0.07f * k, 0.27f + 0.07f * k))
            tagAt(a.tag("agnafaqa2", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            woman12(Offset(w * 0.82f, g), h * 0.13f, Mom126, f)
            person99(Offset(w * 0.18f, g), h * 0.14f, Fig126, f)
            val ch = sin(a.t * 1.2f) * 0.15f
            child12(Offset(w * (0.5f + ch), g), h * 0.1f, Fig126, f)
            tagAt(a.tag("agnafaqa3", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
        },
    )
}

private fun sin(x: Float) = kotlin.math.sin(x)

// ═════════════ ag-qatl ═════════════
internal fun DrawScope.agqatlScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "agqatl", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night126(a) },
        { p, f ->
            veil126(f)
            row126(a, "agqatl0", 3, h * 0.45f, p, f, from = 0.1f, step = 0.22f)
        },
        { p, f ->
            veil126(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.4f), w * 0.16f, Qn.GoldLight, f * seg(p, 0.1f, 0.3f), a.t)
            tagAt(a.tag("agqatl1", 0, 0), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil126(f)
            qIcon("scale", Offset(w * 0.68f, h * 0.4f), w * 0.12f, Qn.GoldLight, f, a.t)
            for (k in 0 until 6) qIcon("coin", Offset(w * (0.18f + 0.04f * k), h * 0.48f - h * 0.03f * k), w * 0.05f, Qn.Gold, f * seg(p, 0.2f + 0.08f * k, 0.3f + 0.08f * k), a.t)
            tagAt(a.tag("agqatl2", 0, 0), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            val c = Offset(w * 0.3f, h * 0.45f)
            for (k in 0 until 3) drawCircle(if (k % 2 == 0) Qn.Rose else Fig126, w * (0.08f - 0.025f * k), c, alpha = f)
            val fly = easeInOut(seg(p, 0.1f, 0.5f))
            drawLine(Color(0xFF8A6A40), Offset(mix(w * 0.9f, w * 0.45f, fly), h * 0.5f + h * 0.12f * fly), Offset(mix(w * 0.98f, w * 0.53f, fly), h * 0.5f + h * 0.12f * fly), w * 0.008f, alpha = f, cap = StrokeCap.Round)
            tagAt(a.tag("agqatl3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ag-ayman ═════════════
internal fun DrawScope.agaymanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "agayman", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night126(a) },
        { p, f ->
            veil126(f)
            tagAt(a.tag("agayman0", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            qIcon("hands", Offset(w * 0.5f, h * 0.5f), w * 0.14f, Fig126, f * seg(p, 0.3f, 0.45f), a.t)
        },
        { p, f ->
            veil126(f)
            val icons = listOf("key", "wheat", "mat", "moon")
            for (i in 0 until 4) {
                val x = w * (0.5f + (1.5f - i) * 0.21f)
                qIcon(icons[i], Offset(x, h * 0.4f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f + 0.2f * i, 0.18f + 0.2f * i), a.t)
            }
            row126(a, "agayman1", 4, h * 0.58f, p, f, from = 0.08f, step = 0.2f)
        },
        { p, f ->
            veil126(f)
            tagAt(a.tag("agayman2", 0, 0), Offset(w * 0.7f, h * 0.42f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            qIcon("cross", Offset(w * 0.7f, h * 0.56f), w * 0.05f, Qn.Rose, f * seg(p, 0.25f, 0.35f), a.t)
            tagAt(a.tag("agayman2", 0, 1), Offset(w * 0.3f, h * 0.42f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            qIcon("cross", Offset(w * 0.3f, h * 0.56f), w * 0.05f, Qn.Rose, f * seg(p, 0.65f, 0.75f), a.t)
        },
        { p, f ->
            veil126(f)
            qIcon("moon", Offset(w * 0.5f, h * 0.4f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("check", Offset(w * 0.5f, h * 0.58f), w * 0.06f, Qn.Jade, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("agayman3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ag-qada ═════════════
internal fun DrawScope.agqadaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val g = h * 0.8f
    film98(a, "agqada", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night126(a) },
        { p, f ->
            veil126(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.32f), w * 0.14f, Qn.GoldLight, f, a.t)
            row126(a, "agqada0", 3, h * 0.6f, p, f, from = 0.15f, step = 0.2f)
        },
        { p, f ->
            drawRoundRect(Color(0xFF6B4F35), Offset(w * 0.42f, g - h * 0.06f), Size(w * 0.16f, h * 0.06f), CornerRadius(w * 0.02f), alpha = f)
            person99(Offset(w * 0.5f, g - h * 0.06f), h * 0.1f, Fig126, f)
            for (s in listOf(-1f, 1f)) person99(Offset(w * (0.5f + 0.3f * s), g), h * 0.12f, Dim126, f)
            for (s in listOf(-1f, 1f)) drawLine(Qn.GoldLight, Offset(w * 0.5f, g - h * 0.2f), Offset(w * (0.5f + 0.3f * s), g - h * 0.2f), w * 0.004f, alpha = f * seg(p, 0.2f, 0.4f))
            row126(a, "agqada1", 3, h * 0.22f, p, f, from = 0.3f, step = 0.18f, ink = ArtInk.Gold)
        },
        { p, f ->
            veil126(f)
            val icons = listOf("coin", "flame", "cup")
            for (i in 0 until 3) {
                val x = w * (0.5f + (1 - i) * 0.28f)
                qIcon(icons[i], Offset(x, h * 0.42f), w * 0.09f, Dim126, f * seg(p, 0.08f + 0.2f * i, 0.2f + 0.2f * i), a.t)
                qIcon("cross", Offset(x, h * 0.42f), w * 0.06f, Qn.Rose, f * seg(p, 0.14f + 0.2f * i, 0.26f + 0.2f * i), a.t)
            }
            row126(a, "agqada2", 3, h * 0.62f, p, f, from = 0.1f, step = 0.2f)
        },
        { p, f ->
            for (k in 0 until 3) house12(Offset(w * (0.12f + 0.38f * k), g), w * 0.2f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f * 0.6f)
            softGlow(Offset(w * 0.5f, g - h * 0.06f), w * 0.16f, Qn.GoldLight, 0.5f * f * seg(p, 0.1f, 0.3f))
            person99(Offset(w * 0.5f, g), h * 0.1f, Fig126, f)
            tagAt(a.tag("agqada3", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}
