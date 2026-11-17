package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 101 (2026-11-17): the five cards of «عام الوفود وحجة الوداع» redrawn in the illustrated style (boards in wave 95).
 * Delegations are faceless groups under their tribe's name; Abu Bakr and Ali are roundels in their colours, Usama the
 * plain one; his ﷺ named light. Any line with the Name of Allah sits high in the frame.
 */

private val Jd101 = Color(0xFFC8E8D0)
private val Pil101 = Color(0xFFF2EEE4)

private fun DrawScope.group101(c: Offset, n: Int, color: Color, alpha: Float, walk: Float = 0f) {
    if (alpha <= 0.01f) return
    for (k in 0 until n) figure(Offset(c.x + (k - (n - 1) / 2f) * size.width * 0.03f, c.y - (k % 2) * size.height * 0.01f), size.height * 0.07f, color, alpha, walk = walk + k)
}

private fun DrawScope.mount101(c: Offset, w: Float, hgt: Float, color: Color, alpha: Float) {
    drawPath(Path().apply { moveTo(c.x - w / 2, c.y); lineTo(c.x - w * 0.1f, c.y - hgt); lineTo(c.x + w * 0.12f, c.y - hgt * 0.85f); lineTo(c.x + w / 2, c.y); close() }, color, alpha = alpha)
}

// ═════════════ wd-wufud ═════════════
internal fun DrawScope.wdwufudScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    val city = Offset(w * 0.5f, ground)
    film98(a, "wdwufud", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1.05f)),
        { daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB09868)) },
        { p, f ->
            house12(city, w * 0.16f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.4f)
            for (k in 0 until 6) {
                val ang = PI.toFloat() * (k / 5f)
                val come = easeInOut(seg(p, 0.1f + 0.06f * k, 0.6f + 0.06f * k))
                val from = city + Offset(-cos(ang) * w * 0.6f, -sin(ang) * h * 0.1f)
                val to = city + Offset(-cos(ang) * w * 0.22f, 0f)
                group101(Offset(mix(from.x, to.x, come), ground), 3, Pil101, f, walk = a.t * 2f * (1f - come))
            }
            tagAt(a.tag("wdwufud0", 0, 0), city + Offset(0f, -h * 0.24f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("wdwufud0", 0, 1), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.45f * f)
            for (k in 0 until 60) {
                val col = k % 12
                val row = k / 12
                drawCircle(Pil101, w * 0.012f, Offset(w * (0.12f + 0.068f * col), h * (0.4f + 0.08f * row)), alpha = f * seg(p, 0.01f * k, 0.05f + 0.01f * k))
            }
            tagAt(a.tag("wdwufud1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.45f * f)
            sirajMunir(Offset(w * 0.5f, h * 0.5f), w * 0.035f, f, a.t, named = false)
            for (k in 0 until 6) {
                val ang = 2f * PI.toFloat() * k / 6f - PI.toFloat() / 2f
                val at = Offset(w * 0.5f + cos(ang) * w * 0.32f, h * 0.5f + sin(ang) * h * 0.3f)
                val on = seg(p, 0.08f + 0.12f * k, 0.2f + 0.12f * k)
                drawLine(Qn.GoldLight, at, Offset(w * 0.5f, h * 0.5f), 1.2f * density, alpha = 0.35f * f * on)
                tagAt(a.tag("wdwufud2", 0, k), at, f * on, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            house12(city, w * 0.16f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f, lit = 0.4f)
            val come = easeInOut(seg(p, 0.05f, 0.5f))
            group101(Offset(mix(w * 1.05f, w * 0.75f, come), ground), 5, Color(0xFF9A8AB0), f, walk = a.t * 2f * (1f - come))
            tagAt(a.tag("wdwufud3", 0, 0), Offset(w * 0.78f, ground - h * 0.18f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            val give = easeInOut(seg(p, 0.55f, 0.85f))
            fqCoins(Offset(mix(w * 0.72f, w * 0.56f, give), ground - h * 0.03f), w * 0.02f, 3, f * seg(p, 0.5f, 0.6f))
            tagAt(a.tag("wdwufud3", 0, 1), Offset(w * 0.6f, h * 0.36f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ wd-baraa ═════════════
internal fun DrawScope.wdbaraaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "wdbaraa", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { nightSky(a.t, S97.Dusk, S97.DuskLow, ground, 0.6f); dunes97(ground, S97.SandDark, Color(0xFF6A5236)) },
        { p, f ->
            kaaba29(Offset(w * 0.68f, ground), w * 0.1f, f)
            close97(Offset(w * 0.25f, h * 0.36f), w * 0.08f, a, "wdbaraa0", 1, 0, f * seg(p, 0.2f, 0.4f))
            val go = easeInOut(seg(p, 0.3f, 0.9f))
            group101(Offset(mix(w * 0.15f, w * 0.45f, go), ground), 7, Pil101, f, walk = a.t * 2f)
            tagAt(a.tag("wdbaraa0", 0, 0), Offset(w * 0.68f, ground - h * 0.26f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
        },
        { p, f ->
            kaaba29(Offset(w * 0.8f, ground), w * 0.07f, f)
            group101(Offset(w * 0.6f, ground), 8, Pil101, f)
            close97(Offset(w * 0.22f, h * 0.36f), w * 0.08f, a, "wdbaraa1", 1, 3, f * seg(p, 0.15f, 0.35f))
            qIcon("book", Offset(w * 0.4f, h * 0.36f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 3) drawArc(Qn.GoldLight, -60f, 120f, false, Offset(w * 0.4f - w * 0.05f * (k + 1), h * 0.36f - h * 0.05f * (k + 1)), Size(w * 0.1f * (k + 1), h * 0.1f * (k + 1)), alpha = 0.45f * f * seg(p, 0.4f + 0.08f * k, 0.55f + 0.08f * k), style = Stroke(1.5f * density))
            tagAt(a.tag("wdbaraa1", 0, 0), Offset(w * 0.4f, h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            tagAt(a.tag("wdbaraa1", 0, 2), Offset(w * 0.6f, ground - h * 0.14f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.5f * f)
            kaaba29(Offset(w * 0.5f, h * 0.72f), w * 0.1f, f)
            for (k in 0 until 2) {
                val c = Offset(w * (0.22f + 0.56f * k), h * 0.4f)
                tagAt(a.tag("wdbaraa2", 0, k), c, f * seg(p, 0.1f + 0.3f * k, 0.25f + 0.3f * k), back = Color(0xCC401A20), ink = ArtInk.Ivory)
                drawLine(Qn.Ember, c + Offset(-w * 0.1f, h * 0.05f), c + Offset(w * 0.1f, -h * 0.05f), 2.5f * density, StrokeCap.Round, alpha = f * seg(p, 0.25f + 0.3f * k, 0.35f + 0.3f * k))
            }
        },
        { p, f ->
            drawRect(Color(0xFF1E2234), alpha = 0.5f * f)
            qIcon("scroll", Offset(w * 0.32f, h * 0.45f), w * 0.11f, Color(0xFFEFE2C0), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("hourglass", Offset(w * 0.68f, h * 0.45f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.4f, 0.55f), a.t)
            val run = seg(p, 0.5f, 1f)
            drawLine(Qn.GoldLight, Offset(w * 0.2f, h * 0.68f), Offset(w * (0.2f + 0.6f * run), h * 0.68f), 2f * density, StrokeCap.Round, alpha = f)
            tagAt(a.tag("wdbaraa3", 0, 0), Offset(w * 0.32f, h * 0.26f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("wdbaraa3", 0, 1), Offset(w * 0.68f, h * 0.26f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ wd-wada ═════════════
internal fun DrawScope.wdwadaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    film98(a, "wdwada", listOf(Shot(Offset(w * 0.5f, h * 0.55f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.55f), 1f)),
        { daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB8A070)) },
        { p, f ->
            mount101(Offset(w * 0.15f, ground), w * 0.3f, h * 0.14f, Color(0xFF8A7058), f)
            kaaba29(Offset(w * 0.85f, ground), w * 0.08f, f)
            val go = easeInOut(seg(p, 0.15f, 0.9f))
            for (k in 0 until 10) figure(Offset(mix(w * 0.1f, w * 0.6f, go) + k * w * 0.025f, ground), h * 0.07f, Pil101, f, walk = a.t * 2f)
            sirajMunir(Offset(mix(w * 0.18f, w * 0.72f, go), ground - h * 0.14f), w * 0.022f, f, a.t, named = false)
            tagAt(a.tag("wdwada0", 0, 0), Offset(w * 0.15f, ground - h * 0.22f), f * seg(p, 0f, 0.15f), ink = ArtInk.Ivory)
            tagAt(a.tag("wdwada0", 0, 1), Offset(w * 0.85f, ground - h * 0.24f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            val mt = Offset(w * 0.5f, ground)
            mount101(mt, w * 0.5f, h * 0.2f, Color(0xFF9A7E5E), f)
            for (k in 0 until 24) figure(Offset(w * (0.04f + 0.04f * k), ground + h * 0.02f * (k % 3)), h * 0.05f, Pil101, f, arms = 0.8f * seg(p, 0.6f, 0.8f))
            sirajMunir(Offset(w * 0.47f, ground - h * 0.22f), w * 0.035f, f, a.t, nameAt = Offset(w * 0.47f, h * 0.12f))
            for (k in 0 until 3) drawArc(Qn.GoldLight, -60f, 120f, false, Offset(w * 0.47f - w * 0.06f * (k + 1), ground - h * 0.22f - h * 0.05f * (k + 1)), Size(w * 0.12f * (k + 1), h * 0.1f * (k + 1)), alpha = 0.4f * f * seg(p, 0.3f + 0.06f * k, 0.45f + 0.06f * k), style = Stroke(1.4f * density))
            tagAt(a.tag("wdwada1", 0, 0), Offset(w * 0.22f, h * 0.36f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("wdwada1", 0, 1), Offset(w * 0.78f, h * 0.3f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("wdwada1", 0, 2), Offset(w * 0.78f, h * 0.44f), f * seg(p, 0.65f, 0.8f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.55f * f)
            kaaba29(Offset(w * 0.5f, h * 0.72f), w * 0.12f, f)
            val fade = seg(p, 0.4f, 0.9f)
            softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.25f * (1f + 0.3f * fade), Qn.GoldLight, 0.35f * f * (1f - 0.5f * fade))
            tagAt(a.tag("wdwada2", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.3f), ink = ArtInk.Gold)
        },
        { p, f ->
            val go = easeInOut(seg(p, 0.2f, 0.9f))
            mount101(Offset(w * 0.88f, ground), w * 0.3f, h * 0.12f, Color(0xFF6A5A58), f)
            qIcon95("flag", Offset(mix(w * 0.15f, w * 0.5f, go), ground - h * 0.22f), w * 0.08f, Qn.Gold, f, a.t)
            for (k in 0 until 7) figure(Offset(mix(w * 0.12f, w * 0.48f, go) - k * w * 0.035f, ground), h * 0.08f, Jd101, f, walk = a.t * 2f)
            plain97(Offset(w * 0.3f, h * 0.3f), w * 0.075f, a, "wdwada3", 1, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("wdwada3", 0, 0), Offset(w * 0.55f, h * 0.14f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            tagAt(a.tag("wdwada3", 0, 2), Offset(w * 0.88f, ground - h * 0.2f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ wd-marad ═════════════
internal fun DrawScope.wdmaradScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.84f
    val mosque = Offset(w * 0.62f, ground)
    film98(a, "wdmarad", listOf(Shot(Offset(w * 0.35f, h * 0.55f), 1.05f), Shot(Offset(w * 0.6f, h * 0.55f), 1.05f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.45f), 1f)),
        { nightSky(a.t, S97.Night, S97.NightLow, ground, 0.7f); dunes97(ground, Color(0xFF4A3A2A), Color(0xFF3A2E24)) },
        { p, f ->
            house12(Offset(w * 0.3f, ground), w * 0.24f, Color(0xFFD8C098), Color(0xFF7A5A38), f, lit = 0.6f)
            sirajMunir(Offset(w * 0.3f, ground - h * 0.1f), w * 0.022f, f * (0.6f + 0.15f * sin(a.t * 1.5f)), a.t, named = false)
            tagAt(a.tag("wdmarad0", 0, 0), Offset(w * 0.3f, ground - h * 0.32f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            tagAt(a.tag("wdmarad0", 0, 1), Offset(w * 0.7f, h * 0.3f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
        },
        { p, f ->
            drawRect(Color(0xFF8A6A40), Offset(mosque.x - w * 0.3f, mosque.y - h * 0.24f), Size(w * 0.6f, h * 0.24f), alpha = 0.4f * f)
            for (r in 0 until 2) for (k in 0 until 7) pray12(Offset(w * (0.42f + 0.06f * k), ground - r * h * 0.06f), h * 0.07f, Pil101, f * seg(p, 0.3f, 0.5f))
            close97(Offset(w * 0.32f, h * 0.36f), w * 0.08f, a, "wdmarad1", 1, 0, f * seg(p, 0.1f, 0.3f))
            tagAt(a.tag("wdmarad1", 0, 0), Offset(w * 0.62f, h * 0.24f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.5f * f)
            val c = Offset(w * 0.5f, h * 0.52f)
            close97(c, w * 0.1f, a, "wdmarad2", 1, 0, f * seg(p, 0.05f, 0.25f))
            val rise = easeOut(seg(p, 0.35f, 0.7f))
            qIcon("crown", c + Offset(0f, -h * (0.12f + 0.05f * rise)), w * 0.08f, Qn.GoldLight, f * rise, a.t)
            tagAt(a.tag("wdmarad2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.5f * f)
            fqCoins(Offset(w * 0.28f, h * 0.7f), w * 0.025f, 5, f * (1f - 0.6f * seg(p, 0.5f, 0.8f)))
            tagAt(a.tag("wdmarad3", 0, 0), Offset(w * 0.28f, h * 0.8f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ivory)
            qIcon("star", Offset(w * 0.68f, h * 0.24f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.35f, 0.55f), a.t)
            softGlow(Offset(w * 0.68f, h * 0.24f), w * 0.12f, Qn.GoldLight, 0.5f * f * seg(p, 0.45f, 0.75f))
            tagAt(a.tag("wdmarad3", 0, 1), Offset(w * 0.68f, h * 0.38f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
    )
}

// ═════════════ wd-wafat ═════════════
internal fun DrawScope.wdwafatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "wdwafat", listOf(Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.45f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f), Shot(Offset(w * 0.5f, h * 0.5f), 1f)),
        { nightSky(a.t, S97.Night, S97.NightLow, ground, 0.8f); dunes97(ground, Color(0xFF3A2E24), Color(0xFF2E241C)) },
        { p, f ->
            qIcon("sun", Offset(w * 0.2f, h * 0.3f), w * 0.08f, Qn.GoldLight, f * seg(p, 0f, 0.2f), a.t)
            qIcon("crescent", Offset(w * 0.5f, h * 0.3f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.25f, 0.45f), a.t)
            qIcon("hourglass", Offset(w * 0.8f, h * 0.3f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.5f, 0.7f), a.t)
            for (k in 0 until 3) tagAt(a.tag("wdwafat0", 0, k), Offset(w * (0.2f + 0.3f * k), h * 0.44f), f * seg(p, 0.05f + 0.25f * k, 0.2f + 0.25f * k), ink = ArtInk.Ivory)
            house12(Offset(w * 0.5f, ground), w * 0.2f, Color(0xFFC8B088), Color(0xFF6A4E30), f, lit = 0.4f)
        },
        { p, f ->
            val rise = easeInOut(seg(p, 0.2f, 0.9f))
            house12(Offset(w * 0.5f, ground), w * 0.2f, Color(0xFFC8B088), Color(0xFF6A4E30), f, lit = 0.4f * (1f - rise))
            val c = Offset(w * 0.5f, mix(ground - h * 0.12f, h * 0.3f, rise))
            sirajMunir(c, w * 0.035f, f, a.t, nameAt = Offset(w * 0.5f, h * 0.62f))
            qIcon("star", Offset(w * 0.5f, h * 0.12f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.5f, 0.75f), a.t)
            tagAt(a.tag("wdwafat1", 0, 1), Offset(w * 0.75f, h * 0.14f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.5f * f)
            for (k in 0 until 12) figure(Offset(w * (0.08f + 0.075f * k), ground), h * 0.08f, Color(0xFFB8B4C0), f, lean = 0.08f)
            close97(Offset(w * 0.5f, h * 0.56f), w * 0.08f, a, "wdwafat2", 2, 0, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("wdwafat2", 0, 0), Offset(w * 0.5f, h * 0.38f), f * seg(p, 0.25f, 0.4f) * (1f - 0.5f * seg(p, 0.55f, 0.7f)), ink = ArtInk.Ivory)
            softGlow(Offset(w * 0.5f, h * 0.15f), w * 0.2f, Qn.Jade, 0.3f * f * seg(p, 0.55f, 0.8f))
            tagAt(a.tag("wdwafat2", 0, 1), Offset(w * 0.5f, h * 0.15f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.45f * f)
            val c = Offset(w * 0.5f, h * 0.42f)
            qIcon("book", c, w * 0.12f, Qn.GoldLight, f * seg(p, 0.05f, 0.25f), a.t)
            for (k in 0 until 16) {
                val ang = 2f * PI.toFloat() * k / 16f + a.t * 0.1f
                val r = w * (0.12f + 0.3f * seg(p, 0.3f, 0.95f))
                drawCircle(Qn.GoldLight, w * 0.006f, c + Offset(cos(ang) * r, sin(ang) * r * 0.8f), alpha = f * seg(p, 0.3f, 0.45f))
            }
            tagAt(a.tag("wdwafat3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
            tagAt(a.tag("wdwafat3", 0, 1), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}
