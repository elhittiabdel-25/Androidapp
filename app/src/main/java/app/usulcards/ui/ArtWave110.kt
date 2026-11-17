package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/*
 * Wave 110 (2026-11-17): four cards of «معرفة النبي ﷺ» from al-Mukhtasar al-Mufid.
 * He ﷺ is only his named light; his children, uncles and Ahl al-Bayt are roundels in the family's colour, Khadija
 * in hers, every other named person the plain roundel; crowds faceless.
 */

private val Good110 = Color(0xCC1A3020)
private val Bad110 = Color(0xCC401A20)

private fun DrawScope.night110(a: ArtCtx, ground: Float) {
    nightSky(a.t, S97.Night, S97.NightLow, ground, 0.7f)
    dunes97(ground, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.veil110(alpha: Float) = drawRect(Color(0xFF14102A), alpha = 0.6f * alpha)

private fun DrawScope.people110(x0: Float, x1: Float, ground: Float, n: Int, alpha: Float, t: Float = 0f) {
    for (k in 0 until n) person99(Offset(mix(x0, x1, k / (n - 1f).coerceAtLeast(1f)), ground), size.height * 0.065f, Qn.Person, alpha, walk = t)
}

private fun DrawScope.family110(c: Offset, r: Float, a: ArtCtx, kind: String, part: Int, grow: Float) = close97(c, r, a, kind, part, 3, grow)

// ═════════════ mn-tazim ═════════════
internal fun DrawScope.mntazimScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mntazim", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night110(a, ground) },
        { p, f ->
            veil110(f)
            plain97(Offset(w * 0.5f, h * 0.32f), w * 0.09f, a, "mntazim0", 0, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("mntazim0", 0, 1), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, ground - h * 0.16f), w * 0.035f, f, a.t, named = false)
            val go = easeInOut(seg(p, 0.15f, 0.55f))
            people110(mix(w * 0.05f, w * 0.25f, go), mix(w * 0.2f, w * 0.38f, go), ground, 3, f, a.t * 2f * go)
            people110(mix(w * 0.95f, w * 0.75f, go), mix(w * 0.8f, w * 0.62f, go), ground, 3, f, a.t * 2f * go)
            tagAt(a.tag("mntazim1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("mntazim1", 0, 1), Offset(w * 0.5f, h * 0.34f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            sirajMunir(Offset(w * 0.5f, ground - h * 0.16f), w * 0.035f, f, a.t, named = false)
            people110(w * 0.15f, w * 0.85f, ground, 6, f)
            val hush = seg(p, 0.2f, 0.6f)
            for (k in 0 until 3) drawCircle(Qn.GoldLight, w * (0.12f - 0.03f * k) * (1f - 0.6f * hush), Offset(w * 0.5f, ground - h * 0.16f), alpha = 0.4f * f, style = Stroke(1.2f * density))
            tagAt(a.tag("mntazim2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("mntazim2", 0, 1), Offset(w * 0.5f, h * 0.34f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            people110(w * 0.1f, w * 0.4f, ground, 4, f)
            sirajMunir(Offset(w * 0.55f, ground - h * 0.16f), w * 0.035f, f, a.t, named = false)
            for (k in 0 until 3) drawLine(Qn.Ember, Offset(w * 0.9f, ground - h * (0.05f + 0.05f * k)), Offset(w * 0.75f, ground - h * (0.05f + 0.05f * k)), 2f * density, alpha = f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("mntazim3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("mntazim3", 0, 1), Offset(w * 0.5f, h * 0.34f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ mn-taa ═════════════
internal fun DrawScope.mntaaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mntaa", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night110(a, ground) },
        { p, f ->
            veil110(f)
            qIcon("heart", Offset(w * 0.5f, h * 0.32f), w * 0.12f, Qn.Rose, f * seg(p, 0.05f, 0.2f), a.t)
            sirajMunir(Offset(w * 0.5f, h * 0.32f), w * 0.022f, f * seg(p, 0.15f, 0.3f), a.t, named = false)
            for (k in 0 until 3) {
                val c = Offset(w * (0.78f - 0.28f * k), h * 0.6f)
                tagAt(a.tag("mntaa0", 0, k), c, f * seg(p, 0.35f + 0.15f * k, 0.5f + 0.15f * k), ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil110(f)
            qIcon("tree", Offset(w * 0.72f, h * 0.36f), w * 0.12f, Qn.Leaf, f * seg(p, 0.05f, 0.2f), a.t)
            softGlow(Offset(w * 0.72f, h * 0.36f), w * 0.14f, Qn.Jade, 0.35f * f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("mntaa1", 0, 0), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.35f, 0.5f), back = Good110, ink = ArtInk.Jade)
            qIcon("lock", Offset(w * 0.28f, h * 0.36f), w * 0.08f, Color(0xFF9A8A80), f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("mntaa1", 0, 1), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.6f, 0.75f), back = Bad110, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil110(f)
            drawRect(Color(0xFF0A0E18), alpha = 0.4f * f * seg(p, 0.2f, 0.5f))
            qIcon("chain", Offset(w * 0.5f, h * 0.38f), w * 0.12f, Color(0xFF8A8494), f * seg(p, 0.1f, 0.3f), a.t)
            tagAt(a.tag("mntaa2", 0, 0), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.4f, 0.55f), back = Bad110, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil110(f)
            qIcon("light", Offset(w * 0.72f, h * 0.38f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("hands", Offset(w * 0.28f, h * 0.38f), w * 0.08f, Qn.Jade, f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("mntaa3", 0, 0), Offset(w * 0.72f, h * 0.58f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("mntaa3", 0, 1), Offset(w * 0.28f, h * 0.58f), f * seg(p, 0.5f, 0.65f), back = Good110, ink = ArtInk.Jade)
        },
    )
}

// ═════════════ mn-masajid ═════════════
internal fun DrawScope.mnmasajidScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mnmasajid", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night110(a, ground) },
        { p, f ->
            tagAt(a.tag("mnmasajid0", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            qIcon("kaaba", Offset(w * 0.5f, h * 0.56f), w * 0.15f, Color(0xFF1A1A1A), f * seg(p, 0.05f, 0.2f), a.t)
            drawCircle(Qn.GoldLight, w * 0.3f, Offset(w * 0.5f, h * 0.56f), alpha = 0.5f * f * seg(p, 0.3f, 0.5f), style = Stroke(1.5f * density))
            tagAt(a.tag("mnmasajid0", 0, 0), Offset(w * 0.5f, h * 0.74f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
        },
        { p, f ->
            qIcon("kaaba", Offset(w * 0.5f, h * 0.56f), w * 0.15f, Color(0xFF1A1A1A), f, a.t)
            softGlow(Offset(w * 0.5f, h * 0.56f), w * 0.25f, Qn.Gold, 0.4f * f * seg(p, 0.2f, 0.5f))
            tagAt(a.tag("mnmasajid1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 3) {
                val c = Offset(w * (0.8f - 0.3f * k), ground)
                val on = f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k)
                if (k == 0) qIcon("kaaba", c + Offset(0f, -h * 0.1f), w * 0.11f, Color(0xFF1A1A1A), on, a.t)
                else mosque12(c, w * 0.2f, Color(0xFFE8DCC0), if (k == 1) Color(0xFF6FA88A) else Color(0xFFD8B050), on, lit = 0.5f)
                tagAt(a.tag("mnmasajid2", 0, k), Offset(c.x, h * 0.36f), on, ink = ArtInk.Gold)
            }
        },
        { p, f ->
            veil110(f)
            val vals = listOf(1f, 0.32f, 0.2f)
            for (k in 0 until 3) {
                val x = w * (0.75f - 0.25f * k)
                val hh = h * 0.45f * vals[k] * easeInOut(seg(p, 0.1f + 0.15f * k, 0.4f + 0.15f * k))
                drawRoundRect(listOf(Qn.Gold, Qn.Jade, Qn.Lapis)[k], Offset(x - w * 0.06f, h * 0.72f - hh), Size(w * 0.12f, hh), CornerRadius(6f * density), alpha = f)
                tagAt(a.tag("mnmasajid3", 0, k), Offset(x, h * 0.8f), f * seg(p, 0.15f + 0.15f * k, 0.3f + 0.15f * k), ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ mn-awlad ═════════════
internal fun DrawScope.mnawladScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "mnawlad", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night110(a, ground) },
        { p, f ->
            veil110(f)
            for (k in 0 until 7) {
                val row = if (k < 3) 0 else 1
                val col = if (row == 0) k else k - 3
                val n = if (row == 0) 3 else 4
                val c = Offset(w * (0.5f - (col - (n - 1) / 2f) * 0.22f), h * (0.3f + 0.26f * row))
                family110(c, w * 0.065f, a, "mnawlad0", k, f * seg(p, 0.05f + 0.1f * k, 0.18f + 0.1f * k))
            }
        },
        { p, f ->
            veil110(f)
            nameRoundel(Offset(w * 0.68f, h * 0.38f), w * 0.09f, a.big("mnawlad1", 0, 0) ?: a.tag("mnawlad1", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(p, 0.05f, 0.25f))
            plain97(Offset(w * 0.32f, h * 0.38f), w * 0.08f, a, "mnawlad1", 1, f * seg(p, 0.5f, 0.7f))
        },
        { p, f ->
            veil110(f)
            family110(Offset(w * 0.5f, h * 0.36f), w * 0.1f, a, "mnawlad2", 0, f * seg(p, 0.05f, 0.25f))
            softGlow(Offset(w * 0.5f, h * 0.36f), w * 0.16f, Qn.GoldLight, 0.35f * f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("mnawlad2", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil110(f)
            family110(Offset(w * 0.68f, h * 0.4f), w * 0.09f, a, "mnawlad3", 0, f * seg(p, 0.05f, 0.25f))
            family110(Offset(w * 0.32f, h * 0.4f), w * 0.09f, a, "mnawlad3", 1, f * seg(p, 0.35f, 0.55f))
            for (k in 0 until 2) softGlow(Offset(w * (0.68f - 0.36f * k), h * 0.4f), w * 0.14f, Qn.Jade, 0.3f * f * seg(p, 0.6f, 0.8f))
        },
    )
}
