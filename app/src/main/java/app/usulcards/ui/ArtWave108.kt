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
 * Wave 108 (2026-11-17): «مواقفه ﷺ وأحواله», what he ﷺ loved and disliked (Zad level 4).
 * He ﷺ is only his named light. Abu Bakr, Umar, Uthman are their roundels, Ali and his daughters the family's,
 * Aisha and Zaynab bint Khuzayma the Mothers' colours, every other named person the plain roundel; crowds are
 * faceless figures.
 */

private val Ink108 = Color(0xFF14102A)
private val Bad108 = Color(0xCC401A20)
private val Good108 = Color(0xCC1A3020)

private fun DrawScope.night108(a: ArtCtx, ground: Float) {
    nightSky(a.t, S97.Night, S97.NightLow, ground, 0.7f)
    dunes97(ground, Color(0xFF4A3A2A), Color(0xFF3A2E24))
}

private fun DrawScope.day108(a: ArtCtx, ground: Float) {
    val w = size.width
    daySky12(Qn.DayTop, Color(0xFFF2E2B8), ground, Color(0xFFB09868))
    for (k in 0 until 3) house12(Offset(w * (0.12f + 0.38f * k), ground), w * 0.12f, Color(0xFFE2CCA0), Color(0xFF8A6A40), 0.55f)
}

private fun DrawScope.veil108(alpha: Float) = drawRect(Ink108, alpha = 0.6f * alpha)

private fun DrawScope.no108(c: Offset, r: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(Qn.Ember, r, c, alpha = alpha, style = Stroke(2.5f * density))
    drawLine(Qn.Ember, c + Offset(-r * 0.7f, -r * 0.7f), c + Offset(r * 0.7f, r * 0.7f), 2.5f * density, alpha = alpha)
}

/** Sound waves from [c] toward +x ([dir] 1) or -x, [n] arcs, cut at [stop] (0..1 of the way) if blocked. */
private fun DrawScope.waves108(c: Offset, len: Float, t: Float, alpha: Float, dir: Float = 1f, stop: Float = 1f) {
    for (k in 0 until 4) {
        val ph = ((t * 0.6f + k / 4f) % 1f) * stop
        val r = len * ph
        drawArc(Qn.GoldLight, if (dir > 0) -40f else 140f, 80f, false, c + Offset(-r, -r), Size(2 * r, 2 * r), alpha = alpha * (1f - ph), style = Stroke(1.6f * density))
    }
}

/** A bowl seen from the side at [c]. */
private fun DrawScope.bowl108(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawArc(Color(0xFF8A5A34), 0f, 180f, true, c + Offset(-s, -s * 0.6f), Size(2 * s, s * 1.2f), alpha = alpha)
    drawOval(Color(0xFFD8B080), c + Offset(-s, -s * 0.12f), Size(2 * s, s * 0.24f), alpha = alpha)
}

/** A row of [n] small faceless figures standing from [x0] to [x1], or seated (lower, wider) when [seated]. */
private fun DrawScope.crowd108(x0: Float, x1: Float, ground: Float, n: Int, alpha: Float, seated: Boolean = false, t: Float = 0f) {
    for (k in 0 until n) {
        val x = mix(x0, x1, k / (n - 1f).coerceAtLeast(1f))
        if (seated) {
            drawCircle(Qn.Person, size.height * 0.012f, Offset(x, ground - size.height * 0.05f), alpha = alpha)
            drawRoundRect(Qn.Person, Offset(x - size.width * 0.02f, ground - size.height * 0.038f), Size(size.width * 0.04f, size.height * 0.038f), CornerRadius(6f * density), alpha = alpha)
        } else person99(Offset(x, ground), size.height * 0.07f, Qn.Person, alpha, walk = t)
    }
}

// ═════════════ ah-tufayl ═════════════
internal fun DrawScope.ahtufaylScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    val tufayl = Offset(w * 0.72f, h * 0.42f)
    film98(a, "ahtufayl", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night108(a, ground); qIcon("kaaba", Offset(w * 0.22f, ground - h * 0.1f), w * 0.12f, Color(0xFF1A1A1A), 0.9f, a.t) },
        { p, f ->
            val go = easeInOut(seg(p, 0.05f, 0.5f))
            plain97(Offset(mix(w * 1.05f, tufayl.x, go), tufayl.y), w * 0.08f, a, "ahtufayl0", 0, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("ahtufayl0", 0, 1), tufayl + Offset(0f, h * 0.14f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
            crowd108(w * 0.4f, w * 0.55f, ground, 3, f * seg(p, 0.55f, 0.7f))
        },
        { p, f ->
            veil108(f)
            plain97(tufayl, w * 0.08f, a, "ahtufayl0", 0, f)
            for (k in 0 until 4) drawCircle(Color.White, w * (0.018f + 0.004f * (k % 2)), tufayl + Offset(-w * 0.1f + w * 0.012f * k, -h * 0.02f + h * 0.01f * (k % 2)), alpha = 0.9f * f * seg(p, 0.2f + 0.08f * k, 0.3f + 0.08f * k))
            sirajMunir(Offset(w * 0.25f, h * 0.42f), w * 0.035f, f, a.t, named = false)
            waves108(Offset(w * 0.3f, h * 0.42f), w * 0.3f, a.t, f * seg(p, 0.4f, 0.5f), stop = 0.85f)
            tagAt(a.tag("ahtufayl1", 0, 0), tufayl + Offset(0f, h * 0.14f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil108(f)
            sirajMunir(Offset(w * 0.25f, h * 0.42f), w * 0.035f, f, a.t, named = false)
            waves108(Offset(w * 0.3f, h * 0.42f), w * 0.42f, a.t, f * seg(p, 0.05f, 0.2f))
            softGlow(tufayl, w * 0.13f, Qn.Jade, 0.4f * f * seg(p, 0.5f, 0.7f))
            plain97(tufayl, w * 0.08f, a, "ahtufayl0", 0, f)
            tagAt(a.tag("ahtufayl2", 0, 0), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ahtufayl2", 0, 1), tufayl + Offset(0f, h * 0.14f), f * seg(p, 0.6f, 0.75f), back = Good108, ink = ArtInk.Jade)
        },
        { p, f ->
            veil108(f)
            qIcon("ear", Offset(w * 0.5f, h * 0.36f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ahtufayl3", 0, 0), Offset(w * 0.5f, h * 0.56f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("ahtufayl3", 0, 1), Offset(w * 0.5f, h * 0.66f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ah-adab ═════════════
internal fun DrawScope.ahadabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahadab", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night108(a, ground) },
        { p, f ->
            veil108(f)
            qIcon("lips", Offset(w * 0.5f, h * 0.36f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            drawRoundRect(Color(0xFFE2D6BC), Offset(w * 0.36f, h * 0.27f), Size(w * 0.28f, h * 0.18f), CornerRadius(10f * density), alpha = 0.35f * f * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("ahadab0", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("ahadab0", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil108(f)
            sirajMunir(Offset(w * 0.5f, h * 0.3f), w * 0.035f, f, a.t, named = false)
            for (k in 0 until 3) drawCircle(Qn.GoldLight, w * (0.08f + 0.06f * k), Offset(w * 0.5f, h * 0.3f), alpha = 0.6f * f * seg(p, 0.1f + 0.12f * k, 0.2f + 0.12f * k), style = Stroke(1.5f * density))
            tagAt(a.tag("ahadab1", 0, 0), Offset(w * 0.5f, h * 0.12f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("ahadab1", 0, 1), Offset(w * 0.3f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahadab1", 0, 2), Offset(w * 0.7f, h * 0.62f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil108(f)
            moon12(Offset(w * 0.85f, h * 0.14f), w * 0.035f, f)
            qIcon("bed", Offset(w * 0.27f, h * 0.5f), w * 0.13f, Qn.Lapis, f * seg(p, 0.05f, 0.2f), a.t)
            crowd108(w * 0.73f, w * 0.73f, h * 0.56f, 1, f * seg(p, 0.05f, 0.2f), seated = true)
            sirajMunir(Offset(w * 0.5f, h * 0.24f), w * 0.026f, f, a.t, named = false)
            val soft = f * seg(p, 0.2f, 0.35f)
            for (k in 0 until 3) drawCircle(Qn.GoldLight, w * 0.04f * (k + 1), Offset(w * 0.5f, h * 0.3f), alpha = 0.3f * soft, style = Stroke(1f * density))
            tagAt(a.tag("ahadab2", 0, 0), Offset(w * 0.27f, h * 0.66f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ivory)
            softGlow(Offset(w * 0.73f, h * 0.5f), w * 0.08f, Qn.Jade, 0.4f * f * seg(p, 0.55f, 0.7f))
            tagAt(a.tag("ahadab2", 0, 1), Offset(w * 0.73f, h * 0.66f), f * seg(p, 0.6f, 0.75f), back = Good108, ink = ArtInk.Jade)
        },
        { p, f ->
            veil108(f)
            qIcon("hands", Offset(w * 0.5f, h * 0.38f), w * 0.11f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ahadab3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ahadab3", 0, 1), Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ah-ahabb ═════════════
internal fun DrawScope.ahahabbScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahahabb", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night108(a, ground) },
        { p, f ->
            veil108(f)
            plain97(Offset(w * 0.5f, h * 0.2f), w * 0.07f, a, "ahahabb0", 0, f * seg(p, 0.02f, 0.15f))
            mother102(Offset(w * 0.78f, h * 0.52f), w * 0.075f, a, "ahahabb0", 1, 1, f * seg(p, 0.2f, 0.35f))
            close97(Offset(w * 0.5f, h * 0.52f), w * 0.075f, a, "ahahabb0", 2, 0, f * seg(p, 0.45f, 0.6f))
            close97(Offset(w * 0.22f, h * 0.52f), w * 0.075f, a, "ahahabb0", 3, 1, f * seg(p, 0.7f, 0.85f))
        },
        { p, f ->
            veil108(f)
            val lift = easeInOut(seg(p, 0.2f, 0.5f))
            drawRoundRect(Qn.Gold, Offset(w * 0.25f, h * 0.5f - h * 0.04f * lift), Size(w * 0.5f, h * 0.02f), CornerRadius(4f * density), alpha = 0.7f * f)
            close97(Offset(w * 0.62f, h * 0.38f - h * 0.04f * lift), w * 0.07f, a, "ahahabb0", 2, 0, f)
            close97(Offset(w * 0.38f, h * 0.38f - h * 0.04f * lift), w * 0.07f, a, "ahahabb0", 3, 1, f)
            for (k in 0 until 6) drawCircle(Roundel.PlainFill, w * 0.03f, Offset(w * (0.2f + 0.12f * k), h * 0.64f), alpha = f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("ahahabb1", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ahahabb1", 0, 1), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil108(f)
            close97(Offset(w * 0.25f, h * 0.3f), w * 0.075f, a, "ahahabb2", 0, 2, f * seg(p, 0.02f, 0.15f))
            close97(Offset(w * 0.14f, h * 0.56f), w * 0.055f, a, "ahahabb2", 1, 3, f * seg(p, 0.15f, 0.28f))
            close97(Offset(w * 0.36f, h * 0.56f), w * 0.055f, a, "ahahabb2", 2, 3, f * seg(p, 0.25f, 0.38f))
            close97(Offset(w * 0.72f, h * 0.36f), w * 0.08f, a, "ahahabb2", 3, 3, f * seg(p, 0.5f, 0.65f))
            val flag = f * seg(p, 0.65f, 0.8f)
            drawLine(Color(0xFFB09868), Offset(w * 0.84f, h * 0.2f), Offset(w * 0.84f, h * 0.5f), 2.5f * density, alpha = flag)
            drawRect(Qn.Ember, Offset(w * 0.84f, h * 0.2f + sin(a.t * 3f) * h * 0.003f), Size(w * 0.1f, h * 0.06f), alpha = flag)
            tagAt(a.tag("ahahabb2", 0, 4), Offset(w * 0.86f, h * 0.58f), flag, ink = ArtInk.Gold)
        },
        { p, f ->
            veil108(f)
            plain97(Offset(w * 0.5f, h * 0.36f), w * 0.09f, a, "ahahabb3", 0, f * seg(p, 0.05f, 0.25f))
            qIcon("heart", Offset(w * 0.5f, h * 0.62f), w * 0.06f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
        },
    )
}

// ═════════════ ah-ansar ═════════════
internal fun DrawScope.ahansarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahansar", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { day108(a, ground) },
        { p, f ->
            crowd108(w * 0.25f, w * 0.75f, ground, 6, f * seg(p, 0.05f, 0.25f))
            for (k in 0 until 4) qIcon("heart", Offset(w * (0.3f + 0.13f * k), h * 0.48f - h * 0.03f * sin(a.t * 2f + k)), w * 0.035f, Qn.Rose, f * seg(p, 0.3f + 0.08f * k, 0.45f + 0.08f * k), a.t)
            tagAt(a.tag("ahansar0", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("ahansar0", 0, 1), Offset(w * 0.5f, h * 0.34f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            val go = easeInOut(seg(p, 0.02f, 0.5f))
            crowd108(mix(w * 1.1f, w * 0.5f, go), mix(w * 1.4f, w * 0.85f, go), ground, 5, f, t = a.t * 2f)
            sirajMunir(Offset(w * 0.25f, ground - h * 0.14f), w * 0.03f, f, a.t, named = false)
            tagAt(a.tag("ahansar1", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil108(f)
            for (k in 0 until 3) {
                val c = Offset(w * (0.8f - 0.3f * k), h * 0.42f)
                val on = f * seg(p, 0.1f + 0.22f * k, 0.25f + 0.22f * k)
                qIcon(listOf("check", "cross", "heart")[k], c, w * 0.07f, listOf(Qn.Jade, Qn.Ember, Qn.Rose)[k], on, a.t)
                tagAt(a.tag("ahansar2", 0, k), c + Offset(0f, h * 0.13f), on, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil108(f)
            mother102(Offset(w * 0.5f, h * 0.36f), w * 0.09f, a, "ahansar3", 0, 3, f * seg(p, 0.05f, 0.25f))
            crowd108(w * 0.25f, w * 0.75f, h * 0.7f, 5, f * seg(p, 0.3f, 0.45f), seated = true)
            tagAt(a.tag("ahansar3", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ah-taam ═════════════
internal fun DrawScope.ahtaamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahtaam", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night108(a, ground) },
        { p, f ->
            veil108(f)
            bowl108(Offset(w * 0.5f, h * 0.5f), w * 0.16f, f * seg(p, 0.05f, 0.2f))
            val pick = easeInOut(seg(p, 0.35f, 0.6f))
            val bone = Offset(w * 0.5f, h * 0.44f - h * 0.12f * pick)
            drawLine(Color(0xFFE8DCC0), bone + Offset(-w * 0.06f, 0f), bone + Offset(w * 0.06f, 0f), 6f * density, StrokeCap.Round, alpha = f * seg(p, 0.2f, 0.3f))
            drawOval(Color(0xFFA0583A), bone + Offset(-w * 0.04f, -h * 0.02f), Size(w * 0.07f, h * 0.04f), alpha = f * seg(p, 0.2f, 0.3f))
            tagAt(a.tag("ahtaam0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            tagAt(a.tag("ahtaam0", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil108(f)
            bowl108(Offset(w * 0.4f, h * 0.5f), w * 0.15f, f)
            for (k in 0 until 5) drawCircle(Color(0xFF7AA05A), w * 0.016f, Offset(w * (0.32f + 0.04f * k), h * 0.48f), alpha = f * (1f - seg(p, 0.3f + 0.08f * k, 0.38f + 0.08f * k)))
            plain97(Offset(w * 0.78f, h * 0.38f), w * 0.07f, a, "ahtaam1", 0, f * seg(p, 0.1f, 0.3f))
            qIcon("heart", Offset(w * 0.78f, h * 0.56f), w * 0.04f, Qn.Rose, f * seg(p, 0.75f, 0.9f), a.t)
            tagAt(a.tag("ahtaam1", 0, 1), Offset(w * 0.4f, h * 0.66f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil108(f)
            drawRoundRect(Color(0xFFF2E6B0), Offset(w * 0.72f, h * 0.38f), Size(w * 0.14f, h * 0.08f), CornerRadius(8f * density), alpha = f * seg(p, 0.05f, 0.2f))
            for (k in 0 until 4) drawOval(Color(0xFF6A3A1E), Offset(w * (0.42f + 0.045f * k), h * 0.39f), Size(w * 0.035f, h * 0.06f), alpha = f * seg(p, 0.2f + 0.04f * k, 0.3f + 0.04f * k))
            qIcon("cup", Offset(w * 0.2f, h * 0.42f), w * 0.07f, Color(0xFF9CC8E8), f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("ahtaam2", 0, 0), Offset(w * 0.79f, h * 0.56f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahtaam2", 0, 1), Offset(w * 0.49f, h * 0.56f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahtaam2", 0, 2), Offset(w * 0.2f, h * 0.56f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            house12(Offset(w * 0.5f, h * 0.62f), w * 0.3f, Color(0xFFE2CCA0), Color(0xFF8A6A40), f * seg(p, 0.05f, 0.2f), lit = 0.3f)
            bowl108(Offset(w * 0.5f, h * 0.74f), w * 0.07f, f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("ahtaam3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahtaam3", 0, 1), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.5f, 0.65f), back = Bad108, ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ah-makka ═════════════
internal fun DrawScope.ahmakkaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahmakka", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night108(a, ground) },
        { p, f ->
            // the Name in the tags above, the House below them
            qIcon("kaaba", Offset(w * 0.5f, h * 0.6f), w * 0.16f, Color(0xFF1A1A1A), f * seg(p, 0.05f, 0.2f), a.t)
            softGlow(Offset(w * 0.5f, h * 0.6f), w * 0.2f, Qn.Gold, 0.35f * f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("ahmakka0", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ahmakka0", 0, 1), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            mosque12(Offset(w * 0.6f, ground), w * 0.3f, Color(0xFFE8DCC0), Color(0xFF6FA88A), f * seg(p, 0.05f, 0.2f), lit = 0.5f)
            qIcon("heart", Offset(w * 0.6f, h * 0.36f), w * 0.06f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("ahmakka1", 0, 0), Offset(w * 0.6f, h * 0.24f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            tagAt(a.tag("ahmakka1", 0, 1), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            qIcon("mountain", Offset(w * 0.5f, h * 0.6f), w * 0.3f, Color(0xFF8A5A3E), f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("heart", Offset(w * 0.5f, h * 0.36f + sin(a.t * 2f) * h * 0.01f), w * 0.06f, Qn.Rose, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("ahmakka2", 0, 0), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ahmakka2", 0, 1), Offset(w * 0.5f, h * 0.22f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil108(f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.4f), w * 0.12f, Color(0xFFD8C4A8), f * seg(p, 0.05f, 0.2f), a.t)
            no108(Offset(w * 0.5f, h * 0.4f), w * 0.1f, f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("ahmakka3", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahmakka3", 0, 1), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), back = Bad108, ink = ArtInk.Ivory)
        },
    )
}

// ═════════════ ah-ayyam ═════════════
internal fun DrawScope.ahayyamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahayyam", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night108(a, ground) },
        { p, f ->
            veil108(f)
            for (d in 0 until 7) {
                val thu = d == 5
                val c = Offset(w * (0.9f - 0.12f * d), h * 0.4f)
                drawRoundRect(if (thu) Qn.Gold else Color(0xFF4A5068), c + Offset(-w * 0.045f, -h * 0.04f), Size(w * 0.09f, h * 0.08f), CornerRadius(6f * density), alpha = f * seg(p, 0.05f + 0.03f * d, 0.15f + 0.03f * d) * (if (thu) 1f else 0.7f))
                if (thu) softGlow(c, w * 0.08f, Qn.Gold, 0.4f * f * seg(p, 0.4f, 0.55f))
            }
            tagAt(a.tag("ahayyam0", 0, 0), Offset(w * 0.9f - w * 0.6f, h * 0.54f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            tagAt(a.tag("ahayyam0", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil108(f)
            for (k in 0 until 5) {
                val ph = (a.t * 0.4f + k / 5f) % 1f
                qIcon("light", Offset(w * (0.2f + 0.15f * k), mix(h * 0.6f, h * 0.15f, ph)), w * 0.03f, Qn.GoldLight, f * seg(p, 0.1f, 0.25f) * (1f - ph), a.t)
            }
            tagAt(a.tag("ahayyam1", 0, 0), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            tagAt(a.tag("ahayyam1", 0, 1), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil108(f)
            val join = easeInOut(seg(p, 0.4f, 0.7f))
            drawRoundRect(Qn.Lapis, Offset(w * 0.56f - w * 0.04f * join, h * 0.36f), Size(w * 0.28f, h * 0.1f), CornerRadius(8f * density), alpha = f * seg(p, 0.05f, 0.2f))
            drawRoundRect(Qn.Gold, Offset(w * 0.16f + w * 0.04f * join, h * 0.36f), Size(w * 0.28f, h * 0.1f), CornerRadius(8f * density), alpha = f * seg(p, 0.25f, 0.4f))
            qIcon("crescent", Offset(w * 0.3f, h * 0.26f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.3f, 0.45f), a.t)
            tagAt(a.tag("ahayyam2", 0, 0), Offset(w * 0.7f, h * 0.56f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahayyam2", 0, 1), Offset(w * 0.3f, h * 0.56f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil108(f)
            for (g in 0 until 5) crowd108(w * (0.12f + 0.17f * g), w * (0.18f + 0.17f * g), h * 0.6f, 2, f * seg(p, 0.1f + 0.1f * g, 0.25f + 0.1f * g))
            tagAt(a.tag("ahayyam3", 0, 0), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ah-tayammun ═════════════
internal fun DrawScope.ahtayammunScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahtayammun", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night108(a, ground) },
        { p, f ->
            veil108(f)
            for (k in 0 until 4) {
                val c = Offset(w * (0.8f - 0.2f * k), h * 0.42f)
                val on = f * seg(p, 0.1f + 0.15f * k, 0.25f + 0.15f * k)
                softGlow(c, w * 0.06f, Qn.Jade, 0.35f * on)
                qIcon(listOf("point", "hands", "drop", "check")[k], c, w * 0.06f, Qn.Jade, on, a.t)
                tagAt(a.tag("ahtayammun0", 0, k), c + Offset(0f, h * 0.12f), on, ink = ArtInk.Ivory)
            }
        },
        { p, f ->
            veil108(f)
            drawLine(Color(0xFF8A8494), Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.72f), 1.5f * density, alpha = f)
            qIcon("hands", Offset(w * 0.73f, h * 0.42f), w * 0.09f, Qn.Jade, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ahtayammun1", 0, 0), Offset(w * 0.73f, h * 0.6f), f * seg(p, 0.1f, 0.25f), back = Good108, ink = ArtInk.Jade)
            qIcon("drop", Offset(w * 0.27f, h * 0.42f), w * 0.07f, Color(0xFF9CC8E8), f * seg(p, 0.45f, 0.6f), a.t)
            tagAt(a.tag("ahtayammun1", 0, 1), Offset(w * 0.27f, h * 0.6f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Ivory)
        },
        { p, f ->
            veil108(f)
            qIcon("cloud", Offset(w * 0.5f, h * 0.2f), w * 0.14f, Color(0xFFC8CCD4), f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 7) {
                val ph = (a.t * 0.8f + k / 7f) % 1f
                drawLine(Color(0xFF9CC8E8), Offset(w * (0.38f + 0.04f * k), mix(h * 0.26f, h * 0.55f, ph)), Offset(w * (0.38f + 0.04f * k), mix(h * 0.26f, h * 0.55f, ph) + h * 0.02f), 1.6f * density, alpha = f * seg(p, 0.15f, 0.3f))
            }
            qIcon("cup", Offset(w * 0.5f, h * 0.62f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.2f, 0.35f), a.t)
            tagAt(a.tag("ahtayammun2", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil108(f)
            plain97(Offset(w * 0.3f, h * 0.36f), w * 0.08f, a, "ahtayammun3", 0, f * seg(p, 0.1f, 0.3f))
            qIcon("mat", Offset(w * 0.7f, h * 0.42f), w * 0.12f, Qn.Jade, f * seg(p, 0.35f, 0.5f), a.t)
            softGlow(Offset(w * 0.7f, h * 0.42f), w * 0.14f, Qn.Jade, 0.35f * f * seg(p, 0.5f, 0.7f))
            tagAt(a.tag("ahtayammun3", 0, 1), Offset(w * 0.7f, h * 0.62f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ah-fal ═════════════
internal fun DrawScope.ahfalScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahfal", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { day108(a, ground) },
        { p, f ->
            veil108(f)
            drawLine(Color(0xFF8A8494), Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.72f), 1.5f * density, alpha = f)
            qIcon("nest", Offset(w * 0.27f, h * 0.42f), w * 0.08f, Color(0xFF9A8A80), f * seg(p, 0.35f, 0.5f), a.t)
            no108(Offset(w * 0.27f, h * 0.42f), w * 0.08f, f * seg(p, 0.5f, 0.62f))
            tagAt(a.tag("ahfal0", 0, 0), Offset(w * 0.27f, h * 0.6f), f * seg(p, 0.45f, 0.6f), back = Bad108, ink = ArtInk.Ivory)
            qIcon("sun", Offset(w * 0.73f, h * 0.42f), w * 0.09f, Qn.Gold, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ahfal0", 0, 1), Offset(w * 0.73f, h * 0.6f), f * seg(p, 0.1f, 0.25f), back = Good108, ink = ArtInk.Jade)
        },
        { p, f ->
            val go = easeInOut(seg(p, 0.05f, 0.7f))
            drawLine(Color(0xFFD8C4A8), Offset(w * 0.1f, ground - h * 0.02f), Offset(w * 0.9f, ground - h * 0.02f), 4f * density, alpha = f)
            sirajMunir(Offset(mix(w * 0.15f, w * 0.7f, go), ground - h * 0.12f), w * 0.026f, f, a.t, named = false)
            tagAt(a.tag("ahfal1", 0, 0), Offset(w * 0.3f, h * 0.36f), f * seg(p, 0.2f, 0.35f), back = Good108, ink = ArtInk.Jade)
            tagAt(a.tag("ahfal1", 0, 1), Offset(w * 0.7f, h * 0.36f), f * seg(p, 0.5f, 0.65f), back = Good108, ink = ArtInk.Jade)
            qIcon("check", Offset(w * 0.85f, ground - h * 0.12f), w * 0.05f, Qn.Jade, f * seg(p, 0.7f, 0.85f), a.t)
        },
        { p, f ->
            veil108(f)
            qIcon("scale", Offset(w * 0.5f, h * 0.36f), w * 0.14f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("ahfal2", 0, 0), Offset(w * 0.27f, h * 0.6f), f * seg(p, 0.2f, 0.35f), back = Bad108, ink = ArtInk.Ivory)
            tagAt(a.tag("ahfal2", 0, 1), Offset(w * 0.73f, h * 0.6f), f * seg(p, 0.5f, 0.65f), back = Good108, ink = ArtInk.Jade)
        },
        { p, f ->
            veil108(f)
            val icons = listOf("eye", "ear", "hourglass", "star")
            for (k in 0 until 4) {
                val c = Offset(w * (0.8f - 0.2f * k), h * 0.4f)
                val on = f * seg(p, 0.1f + 0.15f * k, 0.25f + 0.15f * k)
                qIcon(icons[k], c, w * 0.06f, Color(0xFF9A8A80), on, a.t)
                no108(c, w * 0.06f, f * seg(p, 0.75f, 0.85f))
                tagAt(a.tag("ahfal3", 0, k), c + Offset(0f, h * 0.12f), on, ink = ArtInk.Ivory)
            }
        },
    )
}

// ═════════════ ah-yakrah ═════════════
internal fun DrawScope.ahyakrahScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "ahyakrah", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { night108(a, ground) },
        { p, f ->
            veil108(f)
            qIcon("lips", Offset(w * 0.5f, h * 0.4f), w * 0.1f, Color(0xFF9A8A80), f * seg(p, 0.05f, 0.2f), a.t)
            no108(Offset(w * 0.5f, h * 0.4f), w * 0.11f, f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("ahyakrah0", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.5f, 0.65f), back = Bad108, ink = ArtInk.Ivory)
        },
        { p, f ->
            veil108(f)
            bowl108(Offset(w * 0.5f, h * 0.48f), w * 0.14f, f * seg(p, 0.05f, 0.2f))
            drawOval(Color(0xFFF2EEE4), Offset(w * 0.46f, h * 0.38f), Size(w * 0.08f, h * 0.06f), alpha = f * seg(p, 0.1f, 0.25f))
            for (k in 0 until 3) drawLine(Color(0xFF9AA060), Offset(w * (0.44f + 0.05f * k), h * 0.34f - h * 0.03f * sin(a.t * 2f + k)), Offset(w * (0.46f + 0.05f * k), h * 0.26f - h * 0.03f * sin(a.t * 2f + k)), 1.5f * density, alpha = f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("ahyakrah1", 0, 0), Offset(w * 0.27f, h * 0.66f), f * seg(p, 0.35f, 0.5f), back = Good108, ink = ArtInk.Jade)
            tagAt(a.tag("ahyakrah1", 0, 1), Offset(w * 0.73f, h * 0.66f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil108(f)
            crowd108(w * 0.15f, w * 0.85f, h * 0.66f, 6, f * seg(p, 0.05f, 0.2f), seated = true)
            val go = easeInOut(seg(p, 0.15f, 0.5f))
            sirajMunir(Offset(mix(w * 1.05f, w * 0.5f, go), h * 0.4f), w * 0.03f, f, a.t, named = false)
            tagAt(a.tag("ahyakrah2", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ivory)
            tagAt(a.tag("ahyakrah2", 0, 1), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil108(f)
            bowl108(Offset(w * 0.5f, h * 0.5f), w * 0.2f, f * seg(p, 0.05f, 0.2f))
            softGlow(Offset(w * 0.5f, h * 0.48f), w * 0.07f, Qn.GoldLight, 0.6f * f * seg(p, 0.2f, 0.4f))
            for (k in 0 until 2) qIcon("hands", Offset(w * (0.24f + 0.52f * k), h * 0.46f), w * 0.05f, Qn.Person, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("ahyakrah3", 0, 0), Offset(w * 0.5f, h * 0.28f), f * seg(p, 0.25f, 0.4f), ink = ArtInk.Gold)
            tagAt(a.tag("ahyakrah3", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ivory)
        },
    )
}
