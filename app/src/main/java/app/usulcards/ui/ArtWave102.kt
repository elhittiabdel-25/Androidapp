package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 102 (2026-11-17): the eleven cards of «أمهات المؤمنين» redrawn in the illustrated style (boards in wave 96).
 * Each Mother of the Believers is her name in calligraphy in a roundel of her own colour (never a figure); Jibril is
 * cool white light; other companions are roundels (his family in colour, the rest plain); his ﷺ named light.
 */

/** One colour per Mother of the Believers, in the order of the path: Khadija's is Roundel.Fills[0], as in wave 97. */
internal val MotherFill = listOf(
    Color(0xFF2E6B5E), // سودة
    Color(0xFF8A3E5A), // عائشة
    Color(0xFF2E4E7A), // حفصة
    Color(0xFF6B5A2E), // زينب بنت خزيمة
    Color(0xFF5A3E7A), // أم سلمة
    Color(0xFF7A4A2E), // زينب بنت جحش
    Color(0xFF2E6B7A), // جويرية
    Color(0xFF6B2E3E), // أم حبيبة
    Color(0xFF4A6B2E), // صفية
    Color(0xFF3E4A7A), // ميمونة
)

internal fun DrawScope.mother102(c: Offset, r: Float, a: ArtCtx, kind: String, part: Int, m: Int, grow: Float) {
    if (grow > 0.01f) softGlow(c, r * 1.6f, MotherFill[m], 0.35f * grow)
    nameRoundel(c, r, a.big(kind, 0, part) ?: a.tag(kind, 0, part), MotherFill[m], Roundel.Rim, grow)
}

private fun DrawScope.home102(a: ArtCtx, ground: Float, night: Boolean) {
    if (night) {
        nightSky(a.t, S97.Night, S97.NightLow, ground, 0.8f)
        dunes97(ground, Color(0xFF4A3A2A), Color(0xFF3A2E24))
    } else {
        daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB09868))
    }
}

private fun DrawScope.veil102(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.55f * alpha)

/** Years as a row of beads with the last one lit: [n] beads, [lit] 0..1. */
internal fun DrawScope.years102(c: Offset, n: Int, lit: Float, alpha: Float) {
    val w = size.width
    for (k in 0 until n) {
        val x = c.x + (k - (n - 1) / 2f) * w * 0.05f
        drawCircle(if (k == n - 1) Qn.GoldLight else Color(0xFF8A8494), w * 0.012f, Offset(x, c.y), alpha = alpha * if (k == n - 1) lit else 1f)
    }
}

private fun DrawScope.journey102(from: Offset, to: Offset, u: Float, alpha: Float) {
    val w = size.width
    for (k in 0 until 12) {
        val v = k / 11f
        if (v > u) break
        drawCircle(Qn.GoldLight, w * 0.005f, Offset(mix(from.x, to.x, v), mix(from.y, to.y, v) - sin(v * PI.toFloat()) * size.height * 0.12f), alpha = alpha)
    }
}

// ═════════════ um-khasais ═════════════
internal fun DrawScope.umkhasaisScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "umkhasais", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, true) },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.5f)
            for (k in 0 until 11) {
                val ang = 2f * PI.toFloat() * k / 11f - PI.toFloat() / 2f
                val col = if (k == 0) Roundel.Fills[0] else MotherFill[k - 1]
                drawCircle(col, w * 0.035f, c + Offset(cos(ang) * w * 0.3f, sin(ang) * h * 0.28f), alpha = f * seg(p, 0.04f * k, 0.1f + 0.04f * k))
                drawCircle(Roundel.Rim, w * 0.035f, c + Offset(cos(ang) * w * 0.3f, sin(ang) * h * 0.28f), alpha = f * seg(p, 0.04f * k, 0.1f + 0.04f * k), style = Stroke(1.5f * density))
            }
            qIcon("crown", c + Offset(0f, -h * 0.04f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.4f, 0.6f), a.t)
            tagAt(a.tag("umkhasais0", 0, 0), c + Offset(0f, h * 0.08f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
            tagAt(a.tag("umkhasais0", 0, 1), Offset(w * 0.5f, h * 0.92f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil102(f)
            qIcon("star", Offset(w * 0.22f, h * 0.4f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("crown", Offset(w * 0.5f, h * 0.4f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.25f, 0.4f), a.t)
            qIcon("lock", Offset(w * 0.78f, h * 0.4f), w * 0.07f, Qn.Jade, f * seg(p, 0.55f, 0.7f), a.t)
            for (k in 0 until 3) tagAt(a.tag("umkhasais1", 0, k), Offset(w * (0.22f + 0.28f * k), h * 0.55f), f * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k), ink = if (k == 2) ArtInk.Jade else ArtInk.Gold)
        },
        { p, f ->
            veil102(f)
            qIcon("check", Offset(w * 0.28f, h * 0.42f), w * 0.09f, Qn.Jade, f * seg(p, 0.05f, 0.25f), a.t)
            qIcon("cross", Offset(w * 0.72f, h * 0.42f), w * 0.09f, Qn.Ember, f * seg(p, 0.45f, 0.65f), a.t)
            drawLine(Color(0xFF8A8494), Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.7f), 1.5f * density, alpha = f)
            tagAt(a.tag("umkhasais2", 0, 0), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("umkhasais2", 0, 1), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil102(f)
            qIcon("drop", Offset(w * 0.3f, h * 0.42f), w * 0.08f, Color(0xFFBFE0F0), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("star", Offset(w * 0.7f, h * 0.42f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.25f, 0.4f), a.t)
            tagAt(a.tag("umkhasais3", 0, 0), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("umkhasais3", 0, 1), Offset(w * 0.7f, h * 0.56f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.2f), w * 0.2f, Qn.Jade, 0.3f * f * seg(p, 0.55f, 0.8f))
            tagAt(a.tag("umkhasais3", 0, 2), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ um-sawda ═════════════
internal fun DrawScope.umsawdaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "umsawda", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, false) },
        { p, f ->
            kaaba29(Offset(w * 0.8f, ground), w * 0.07f, f)
            qIcon("crown", Offset(w * 0.18f, ground - h * 0.12f), w * 0.08f, Qn.Gold, f * seg(p, 0.4f, 0.55f), a.t)
            journey102(Offset(w * 0.78f, ground - h * 0.15f), Offset(w * 0.2f, ground - h * 0.22f), easeInOut(seg(p, 0.15f, 0.7f)), f)
            mother102(Offset(w * 0.5f, h * 0.3f), w * 0.09f, a, "umsawda0", 2, 0, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("umsawda0", 0, 0), Offset(w * 0.8f, ground - h * 0.22f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("umsawda0", 0, 1), Offset(w * 0.18f, ground - h * 0.25f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil102(f)
            nameRoundel(Offset(w * 0.28f, h * 0.45f), w * 0.08f, a.big("umsawda1", 0, 0) ?: a.tag("umsawda1", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(p, 0.05f, 0.25f) * (1f - 0.4f * seg(p, 0.4f, 0.6f)))
            mother102(Offset(w * 0.72f, h * 0.45f), w * 0.1f, a, "umsawda1", 1, 0, f * seg(p, 0.4f, 0.6f))
            drawLine(Qn.GoldLight, Offset(w * 0.38f, h * 0.45f), Offset(w * 0.6f, h * 0.45f), 1.5f * density, alpha = 0.6f * f * seg(p, 0.35f, 0.5f))
        },
        { p, f ->
            veil102(f)
            mother102(Offset(w * 0.25f, h * 0.45f), w * 0.085f, a, "umsawda2", 0, 0, f * seg(p, 0.05f, 0.2f))
            mother102(Offset(w * 0.75f, h * 0.45f), w * 0.085f, a, "umsawda2", 2, 1, f * seg(p, 0.5f, 0.65f))
            val give = easeInOut(seg(p, 0.3f, 0.8f))
            qIcon("sun", Offset(mix(w * 0.32f, w * 0.68f, give), h * 0.28f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("umsawda2", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil102(f)
            qIcon("heart", Offset(w * 0.3f, h * 0.45f), w * 0.1f, Qn.Rose, f * seg(p, 0.05f, 0.25f), a.t)
            sirajMunir(Offset(w * 0.7f, h * 0.42f), w * 0.04f, f * seg(p, 0.3f, 0.5f), a.t, nameAt = Offset(w * 0.7f, h * 0.6f))
            drawLine(Qn.GoldLight, Offset(w * 0.38f, h * 0.45f), Offset(w * 0.62f, h * 0.43f), 1.5f * density, alpha = 0.6f * f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("umsawda3", 0, 0), Offset(w * 0.3f, h * 0.62f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ um-aisha ═════════════
internal fun DrawScope.umaishaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "umaisha", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, false) },
        { p, f ->
            kaaba29(Offset(w * 0.2f, ground), w * 0.07f, f)
            house12(Offset(w * 0.8f, ground), w * 0.12f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.4f)
            journey102(Offset(w * 0.22f, ground - h * 0.15f), Offset(w * 0.78f, ground - h * 0.18f), easeInOut(seg(p, 0.3f, 0.8f)), f)
            mother102(Offset(w * 0.5f, h * 0.3f), w * 0.09f, a, "umaisha0", 2, 1, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("umaisha0", 0, 0), Offset(w * 0.2f, ground - h * 0.22f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("umaisha0", 0, 1), Offset(w * 0.8f, ground - h * 0.24f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil102(f)
            angel98(Offset(w * 0.3f, h * 0.35f), w * 0.06f, f * seg(p, 0.05f, 0.3f))
            tagAt(a.tag("umaisha1", 0, 0), Offset(w * 0.3f, h * 0.52f), f * seg(p, 0.15f, 0.3f), back = Color(0xCC20283A), ink = ArtInk.Ivory)
            for (k in 0 until 3) drawArc(Color(0xFFE8F0FF), -30f, 60f, false, Offset(w * 0.3f - w * 0.06f * (k + 1), h * 0.35f - h * 0.05f * (k + 1)), Size(w * 0.12f * (k + 1), h * 0.1f * (k + 1)), alpha = 0.5f * f * seg(p, 0.35f + 0.07f * k, 0.5f + 0.07f * k), style = Stroke(1.4f * density))
            mother102(Offset(w * 0.72f, h * 0.42f), w * 0.085f, a, "umaisha0", 2, 1, f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("umaisha1", 0, 1), Offset(w * 0.6f, h * 0.18f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil102(f)
            qIcon("book", Offset(w * 0.5f, h * 0.34f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.25f), a.t)
            for (k in 0 until 10) {
                val ang = PI.toFloat() * (0.15f + 0.07f * k)
                val r = w * 0.3f * seg(p, 0.45f, 0.9f)
                drawCircle(Qn.GoldLight, w * 0.008f, Offset(w * 0.5f + cos(ang) * r, h * 0.34f + sin(ang) * r), alpha = f * seg(p, 0.45f, 0.55f))
            }
            qIcon("pen", Offset(w * 0.5f, h * 0.7f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("umaisha2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("umaisha2", 0, 1), Offset(w * 0.5f, h * 0.82f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil102(f)
            qIcon("cup", Offset(w * 0.3f, h * 0.45f), w * 0.11f, Qn.Jade, f * seg(p, 0.05f, 0.25f), a.t)
            softGlow(Offset(w * 0.3f, h * 0.45f), w * 0.12f, Qn.GoldLight, 0.4f * f * seg(p, 0.5f, 0.75f))
            for (k in 0 until 3) qIcon("wheat", Offset(w * (0.62f + 0.1f * k), h * 0.48f), w * 0.06f, Color(0xFF9A8A6A), f * seg(p, 0.3f + 0.05f * k, 0.45f + 0.05f * k), a.t)
            tagAt(a.tag("umaisha3", 0, 0), Offset(w * 0.3f, h * 0.62f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("umaisha3", 0, 1), Offset(w * 0.72f, h * 0.62f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ um-hafsa ═════════════
internal fun DrawScope.umhafsaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "umhafsa", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, true) },
        { p, f ->
            mother102(Offset(w * 0.5f, h * 0.24f), w * 0.085f, a, "umhafsa0", 2, 2, f * seg(p, 0.05f, 0.25f))
            plain97(Offset(w * 0.3f, h * 0.55f), w * 0.075f, a, "umhafsa0", 0, f * seg(p, 0.25f, 0.45f) * (1f - 0.4f * seg(p, 0.6f, 0.8f)))
            qIcon("star", Offset(w * 0.7f, h * 0.55f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("umhafsa0", 0, 1), Offset(w * 0.7f, h * 0.68f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil102(f)
            sirajMunir(Offset(w * 0.3f, h * 0.42f), w * 0.04f, f * seg(p, 0.05f, 0.25f), a.t, nameAt = Offset(w * 0.3f, h * 0.6f))
            mother102(Offset(w * 0.7f, h * 0.42f), w * 0.085f, a, "umhafsa0", 2, 2, f * seg(p, 0.25f, 0.45f))
            years102(Offset(w * 0.5f, h * 0.78f), 3, seg(p, 0.5f, 0.7f), f)
            tagAt(a.tag("umhafsa1", 0, 1), Offset(w * 0.5f, h * 0.86f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil102(f)
            mother102(Offset(w * 0.5f, h * 0.4f), w * 0.085f, a, "umhafsa0", 2, 2, f)
            qIcon("crescent", Offset(w * 0.25f, h * 0.24f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("mat", Offset(w * 0.75f, h * 0.24f), w * 0.07f, Qn.Jade, f * seg(p, 0.25f, 0.4f), a.t)
            tagAt(a.tag("umhafsa2", 0, 0), Offset(w * 0.25f, h * 0.36f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("umhafsa2", 0, 1), Offset(w * 0.75f, h * 0.36f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("umhafsa2", 0, 2), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil102(f)
            val gather = easeInOut(seg(p, 0.1f, 0.5f))
            for (k in 0 until 6) {
                val from = Offset(w * (0.1f + 0.16f * k), h * (0.25f + 0.1f * (k % 2)))
                drawRoundRect(Color(0xFFEFE2C0), Offset(mix(from.x, w * 0.25f, gather) - w * 0.03f, mix(from.y, h * 0.45f, gather) - h * 0.04f), Size(w * 0.06f, h * 0.08f), CornerRadius(3f * density), alpha = f)
            }
            mother102(Offset(w * 0.5f, h * 0.45f), w * 0.07f, a, "umhafsa3", 1, 2, f * seg(p, 0.3f, 0.45f))
            qIcon("book", Offset(w * 0.78f, h * 0.45f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.65f, 0.85f), a.t)
            tagAt(a.tag("umhafsa3", 0, 0), Offset(w * 0.25f, h * 0.62f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("umhafsa3", 0, 2), Offset(w * 0.78f, h * 0.62f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ um-zaynab-k ═════════════
internal fun DrawScope.umzaynabkScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "umzaynabk", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, false) },
        { p, f ->
            mother102(Offset(w * 0.5f, h * 0.24f), w * 0.085f, a, "umzaynabk0", 2, 3, f * seg(p, 0.05f, 0.25f))
            house12(Offset(w * 0.3f, ground), w * 0.14f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.5f)
            for (k in 0 until 5) person99(Offset(w * (0.5f + 0.08f * k), ground), h * 0.08f, Color(0xFF9A8A80), f * seg(p, 0.3f + 0.04f * k, 0.45f + 0.04f * k))
            for (k in 0 until 5) qIcon("wheat", Offset(mix(w * 0.36f, w * (0.5f + 0.08f * k), easeInOut(seg(p, 0.5f, 0.85f))), ground - h * 0.12f), w * 0.035f, Qn.Gold, f * seg(p, 0.45f, 0.55f), a.t)
            tagAt(a.tag("umzaynabk0", 0, 0), Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("umzaynabk0", 0, 1), Offset(w * 0.66f, ground - h * 0.2f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil102(f)
            plain97(Offset(w * 0.25f, h * 0.42f), w * 0.075f, a, "umzaynabk1", 0, f * seg(p, 0.05f, 0.25f) * (1f - 0.4f * seg(p, 0.4f, 0.6f)))
            mother102(Offset(w * 0.5f, h * 0.42f), w * 0.07f, a, "umzaynabk0", 2, 3, f * seg(p, 0.2f, 0.4f))
            sirajMunir(Offset(w * 0.76f, h * 0.4f), w * 0.04f, f * seg(p, 0.5f, 0.7f), a.t, nameAt = Offset(w * 0.76f, h * 0.58f))
        },
        { p, f ->
            veil102(f)
            mother102(Offset(w * 0.5f, h * 0.36f), w * 0.085f, a, "umzaynabk0", 2, 3, f)
            for (k in 0 until 3) qIcon("crescent", Offset(w * (0.35f + 0.15f * k), h * 0.62f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.15f + 0.15f * k, 0.3f + 0.15f * k) * if (k == 2) 0.5f else 1f, a.t)
            tagAt(a.tag("umzaynabk2", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil102(f)
            qIcon("crescent", Offset(w * 0.3f, h * 0.4f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            years102(Offset(w * 0.68f, h * 0.42f), 4, seg(p, 0.35f, 0.55f), f)
            tagAt(a.tag("umzaynabk3", 0, 0), Offset(w * 0.3f, h * 0.55f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("umzaynabk3", 0, 1), Offset(w * 0.68f, h * 0.55f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.2f), w * 0.1f, MotherFill[3], 0.4f * f * seg(p, 0.6f, 0.9f))
        },
    )
}

// ═════════════ um-salama ═════════════
internal fun DrawScope.umsalamaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "umsalama", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, false) },
        { p, f ->
            qIcon("crown", Offset(w * 0.18f, ground - h * 0.12f), w * 0.08f, Qn.Gold, f, a.t)
            house12(Offset(w * 0.82f, ground), w * 0.12f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.4f)
            journey102(Offset(w * 0.2f, ground - h * 0.2f), Offset(w * 0.8f, ground - h * 0.18f), easeInOut(seg(p, 0.3f, 0.85f)), f)
            mother102(Offset(w * 0.5f, h * 0.26f), w * 0.085f, a, "umsalama0", 2, 4, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("umsalama0", 0, 0), Offset(w * 0.18f, ground - h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("umsalama0", 0, 1), Offset(w * 0.82f, ground - h * 0.24f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil102(f)
            qIcon("hands", Offset(w * 0.3f, h * 0.45f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.25f), a.t)
            tagAt(a.tag("umsalama1", 0, 0), Offset(w * 0.3f, h * 0.62f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            sirajMunir(Offset(w * 0.72f, h * 0.42f), w * 0.04f, f * seg(p, 0.5f, 0.75f), a.t, nameAt = Offset(w * 0.72f, h * 0.6f))
        },
        { p, f ->
            for (k in 0 until 3) tent102(Offset(w * (0.12f + 0.12f * k), ground), w * 0.08f, f)
            sirajMunir(Offset(w * 0.5f, ground - h * 0.18f), w * 0.028f, f, a.t, named = false)
            mother102(Offset(w * 0.25f, h * 0.3f), w * 0.07f, a, "umsalama0", 2, 4, f * seg(p, 0.05f, 0.2f))
            val follow = easeInOut(seg(p, 0.45f, 0.85f))
            for (k in 0 until 8) person99(Offset(mix(w * (0.62f + 0.045f * k), w * (0.56f + 0.045f * k), follow), ground), h * 0.08f, Color(0xFFC8E8D0), f, arms = 0.6f * follow)
            tagAt(a.tag("umsalama2", 0, 2), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            tagAt(a.tag("umsalama2", 0, 0), Offset(w * 0.25f, h * 0.45f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("umsalama2", 0, 1), Offset(w * 0.76f, ground - h * 0.18f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil102(f)
            close97(Offset(w * 0.4f, h * 0.42f), w * 0.085f, a, "umsalama3", 0, 3, f * seg(p, 0.05f, 0.25f))
            qIcon("drop", Offset(w * 0.65f, h * 0.45f), w * 0.07f, Qn.Rose, f * seg(p, 0.45f, 0.65f), a.t)
            tagAt(a.tag("umsalama3", 0, 1), Offset(w * 0.65f, h * 0.6f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

private fun DrawScope.tent102(foot: Offset, s: Float, alpha: Float) {
    val p = androidx.compose.ui.graphics.Path().apply { moveTo(foot.x - s * 0.6f, foot.y); lineTo(foot.x, foot.y - s); lineTo(foot.x + s * 0.6f, foot.y); close() }
    drawPath(p, Color(0xFFD8C8A8), alpha = alpha)
}

// ═════════════ um-zaynab-j ═════════════
internal fun DrawScope.umzaynabjScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "umzaynabj", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, true) },
        { p, f ->
            mother102(Offset(w * 0.3f, h * 0.42f), w * 0.085f, a, "umzaynabj0", 0, 5, f * seg(p, 0.05f, 0.25f))
            plain97(Offset(w * 0.7f, h * 0.42f), w * 0.075f, a, "umzaynabj0", 1, f * seg(p, 0.3f, 0.5f))
        },
        { p, f ->
            veil102(f)
            qIcon("scroll", Offset(w * 0.3f, h * 0.42f), w * 0.1f, Color(0xFFEFE2C0), f * seg(p, 0.05f, 0.2f), a.t)
            drawLine(Qn.Ember, Offset(w * 0.22f, h * 0.34f), Offset(w * 0.38f, h * 0.5f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.3f, 0.45f))
            qIcon("book", Offset(w * 0.7f, h * 0.42f), w * 0.1f, Qn.Jade, f * seg(p, 0.45f, 0.65f), a.t)
            tagAt(a.tag("umzaynabj1", 0, 0), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("umzaynabj1", 0, 1), Offset(w * 0.7f, h * 0.6f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            for (k in 0 until 7) drawArc(Qn.GoldLight, 200f, 140f, false, Offset(w * 0.5f - w * 0.06f * (k + 2), h * 0.02f + h * 0.012f * k), Size(w * 0.12f * (k + 2), h * 0.1f + h * 0.02f * k), alpha = 0.35f * f * seg(p, 0.35f + 0.04f * k, 0.5f + 0.04f * k), style = Stroke(1.2f * density))
            tagAt(a.tag("umzaynabj2", 0, 1), Offset(w * 0.5f, h * 0.1f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
            for (k in 0 until 4) person99(Offset(w * (0.12f + 0.06f * k), ground), h * 0.08f, Color(0xFF9A8AB0), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("umzaynabj2", 0, 0), Offset(w * 0.21f, ground - h * 0.2f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            mother102(Offset(w * 0.68f, h * 0.55f), w * 0.085f, a, "umzaynabj0", 0, 5, f * seg(p, 0.3f, 0.5f))
        },
        { p, f ->
            veil102(f)
            qIcon("hands", Offset(w * 0.35f, h * 0.42f), w * 0.11f, Qn.GoldLight, f * seg(p, 0.05f, 0.25f), a.t)
            fqCoins(Offset(w * 0.65f, h * 0.5f), w * 0.025f, 5, f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("umzaynabj3", 0, 0), Offset(w * 0.35f, h * 0.62f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("umzaynabj3", 0, 1), Offset(w * 0.65f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ um-juwayriya ═════════════
internal fun DrawScope.umjuwayScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "umjuway", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, false) },
        { p, f ->
            mother102(Offset(w * 0.4f, h * 0.4f), w * 0.085f, a, "umjuway0", 0, 6, f * seg(p, 0.05f, 0.25f))
            qIcon("lock", Offset(w * 0.68f, h * 0.42f), w * 0.07f, Color(0xFF8A8494), f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("umjuway0", 0, 1), Offset(w * 0.68f, h * 0.56f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil102(f)
            qIcon("scroll", Offset(w * 0.22f, h * 0.42f), w * 0.08f, Color(0xFFEFE2C0), f * seg(p, 0.05f, 0.2f), a.t)
            val pay = easeInOut(seg(p, 0.35f, 0.65f))
            fqCoins(Offset(mix(w * 0.75f, w * 0.3f, pay), h * 0.6f), w * 0.022f, 4, f * seg(p, 0.3f, 0.4f))
            sirajMunir(Offset(w * 0.75f, h * 0.4f), w * 0.04f, f * seg(p, 0.2f, 0.4f), a.t, nameAt = Offset(w * 0.75f, h * 0.24f))
            tagAt(a.tag("umjuway1", 0, 0), Offset(w * 0.22f, h * 0.56f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("umjuway1", 0, 1), Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 10) house12(Offset(w * (0.06f + 0.098f * k), ground - (k % 2) * h * 0.05f), w * 0.07f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = seg(p, 0.1f + 0.06f * k, 0.25f + 0.06f * k))
            for (k in 0 until 10) qIcon("key", Offset(w * (0.06f + 0.098f * k), ground - h * 0.2f - (k % 2) * h * 0.05f), w * 0.03f, Qn.GoldLight, f * seg(p, 0.15f + 0.06f * k, 0.3f + 0.06f * k), a.t)
            tagAt(a.tag("umjuway2", 0, 1), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("umjuway2", 0, 0), Offset(w * 0.5f, h * 0.38f), f * seg(p, 0.6f, 0.75f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil102(f)
            val swap = easeInOut(seg(p, 0.35f, 0.7f))
            tagAt(a.tag("umjuway3", 0, 0), Offset(w * 0.5f, mix(h * 0.42f, h * 0.24f, swap)), f * (1f - 0.7f * swap), ink = ArtInk.Ivory)
            mother102(Offset(w * 0.5f, mix(h * 0.7f, h * 0.48f, swap)), w * 0.09f, a, "umjuway3", 1, 6, f * swap)
        },
    )
}

// ═════════════ um-habiba ═════════════
internal fun DrawScope.umhabibaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "umhabiba", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, false) },
        { p, f ->
            kaaba29(Offset(w * 0.8f, ground), w * 0.07f, f)
            qIcon("crown", Offset(w * 0.18f, ground - h * 0.12f), w * 0.08f, Qn.Gold, f * seg(p, 0.4f, 0.55f), a.t)
            journey102(Offset(w * 0.78f, ground - h * 0.15f), Offset(w * 0.2f, ground - h * 0.22f), easeInOut(seg(p, 0.15f, 0.7f)), f)
            mother102(Offset(w * 0.5f, h * 0.28f), w * 0.085f, a, "umhabiba0", 2, 7, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("umhabiba0", 0, 0), Offset(w * 0.8f, ground - h * 0.22f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("umhabiba0", 0, 1), Offset(w * 0.18f, ground - h * 0.25f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil102(f)
            plain97(Offset(w * 0.3f, h * 0.42f), w * 0.08f, a, "umhabiba1", 0, f * seg(p, 0.05f, 0.25f))
            qIcon("crown", Offset(w * 0.3f, h * 0.26f), w * 0.05f, Qn.Gold, f * seg(p, 0.15f, 0.3f), a.t)
            fqCoins(Offset(w * 0.7f, h * 0.5f), w * 0.025f, 6, f * seg(p, 0.45f, 0.65f))
            tagAt(a.tag("umhabiba1", 0, 1), Offset(w * 0.7f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil102(f)
            plain97(Offset(w * 0.25f, h * 0.42f), w * 0.075f, a, "umhabiba2", 0, f * seg(p, 0.05f, 0.25f))
            val fold = easeInOut(seg(p, 0.4f, 0.75f))
            drawRoundRect(Color(0xFF6A8A6A), Offset(w * 0.55f, h * 0.5f - h * 0.06f * (1f - fold)), Size(w * 0.3f * (1f - 0.6f * fold), h * 0.08f + h * 0.06f * (1f - fold)), CornerRadius(4f * density), alpha = f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("umhabiba2", 0, 1), Offset(w * 0.7f, h * 0.3f), f * seg(p, 0.3f, 0.45f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            mother102(Offset(w * 0.5f, h * 0.75f), w * 0.06f, a, "umhabiba0", 2, 7, f * seg(p, 0.5f, 0.7f))
        },
        { p, f ->
            veil102(f)
            years102(Offset(w * 0.5f, h * 0.5f), 7, seg(p, 0.3f, 0.6f), f)
            tagAt(a.tag("umhabiba3", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.68f), w * 0.1f, MotherFill[7], 0.4f * f * seg(p, 0.5f, 0.8f))
        },
    )
}

// ═════════════ um-safiyya ═════════════
internal fun DrawScope.umsafiyyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "umsafiyya", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, false) },
        { p, f ->
            for (k in 0 until 3) drawRect(Color(0xFF8A7A66), Offset(w * (0.62f + 0.11f * k), ground - h * 0.14f), Size(w * 0.08f, h * 0.14f), alpha = f)
            mother102(Offset(w * 0.32f, h * 0.42f), w * 0.085f, a, "umsafiyya0", 0, 8, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("umsafiyya0", 0, 1), Offset(w * 0.73f, ground - h * 0.2f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ivory)
            tagAt(a.tag("umsafiyya0", 0, 2), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil102(f)
            qIcon("key", Offset(w * 0.3f, h * 0.42f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.25f), a.t)
            val turn = easeInOut(seg(p, 0.4f, 0.75f))
            fqCoins(Offset(mix(w * 0.3f, w * 0.7f, turn), h * 0.5f), w * 0.022f, 3, f * seg(p, 0.35f, 0.45f))
            tagAt(a.tag("umsafiyya1", 0, 0), Offset(w * 0.3f, h * 0.6f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("umsafiyya1", 0, 1), Offset(w * 0.7f, h * 0.62f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil102(f)
            qIcon("book", Offset(w * 0.5f, h * 0.4f), w * 0.11f, Qn.Jade, f * seg(p, 0.05f, 0.25f), a.t)
            for (k in 0 until 12) person99(Offset(w * (0.08f + 0.077f * k), ground), h * 0.06f, Color(0xFFB8B4C0), f * seg(p, 0.3f + 0.03f * k, 0.45f + 0.03f * k))
            tagAt(a.tag("umsafiyya2", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.2f, 0.35f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            veil102(f)
            years102(Offset(w * 0.5f, h * 0.5f), 7, seg(p, 0.3f, 0.6f), f)
            tagAt(a.tag("umsafiyya3", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.68f), w * 0.1f, MotherFill[8], 0.4f * f * seg(p, 0.5f, 0.8f))
        },
    )
}

// ═════════════ um-maymuna ═════════════
internal fun DrawScope.ummaymunaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ummaymuna", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { home102(a, ground, false) },
        { p, f ->
            kaaba29(Offset(w * 0.5f, ground), w * 0.1f, f)
            for (k in 0 until 10) {
                val ang = a.t * 0.5f + k * 0.63f
                person99(Offset(w * 0.5f + cos(ang) * w * 0.25f, ground + sin(ang) * h * 0.03f), h * 0.07f, Color(0xFFF2EEE4), f * seg(p, 0.1f, 0.3f))
            }
            tagAt(a.tag("ummaymuna0", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            tagAt(a.tag("ummaymuna0", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil102(f)
            val swap = easeInOut(seg(p, 0.35f, 0.7f))
            tagAt(a.tag("ummaymuna1", 0, 0), Offset(w * 0.5f, mix(h * 0.42f, h * 0.24f, swap)), f * (1f - 0.7f * swap), ink = ArtInk.Ivory)
            mother102(Offset(w * 0.5f, mix(h * 0.7f, h * 0.48f, swap)), w * 0.09f, a, "ummaymuna1", 1, 9, f * swap)
        },
        { p, f ->
            veil102(f)
            mother102(Offset(w * 0.5f, h * 0.3f), w * 0.08f, a, "ummaymuna2", 1, 9, f * seg(p, 0.05f, 0.2f))
            close97(Offset(w * 0.25f, h * 0.6f), w * 0.07f, a, "ummaymuna2", 0, 3, f * seg(p, 0.2f, 0.4f))
            plain97(Offset(w * 0.75f, h * 0.6f), w * 0.07f, a, "ummaymuna2", 2, f * seg(p, 0.5f, 0.7f))
            for (k in 0 until 2) drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.38f), Offset(w * (0.27f + 0.46f * k), h * 0.52f), 1.4f * density, alpha = 0.6f * f * seg(p, 0.3f + 0.3f * k, 0.45f + 0.3f * k))
        },
        { p, f ->
            veil102(f)
            drawPath(androidx.compose.ui.graphics.Path().apply { moveTo(w * 0.15f, h * 0.7f); quadraticBezierTo(w * 0.5f, h * 0.45f, w * 0.85f, h * 0.7f); close() }, Color(0xFF6A5A48), alpha = f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("ummaymuna3", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            softGlow(Offset(w * 0.5f, h * 0.3f), w * 0.1f, MotherFill[9], 0.5f * f * seg(p, 0.45f, 0.75f))
        },
    )
}
