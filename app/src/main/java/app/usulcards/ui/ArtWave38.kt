package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 38 (2026-10-27): «بصائر»، الجزء الثالث، «تفكيك الشبهة». A field of clear stones and a few clouded ones; the
 * proofs brighter than the sun in a heart, reached by a path; desire carrying dark letters to a heart; a reaction that
 * repeats a billion times and three missing pillars; a doctor's diploma and a prescription; thoughts that pop up and
 * multiply; a road where shouts fade when ignored, and a mirage. People are faceless figures; he ﷺ is only light.
 */

private val Back38 = Color(0xE6181420)

/** A small clouded or clear stone on the ground, drawn as a rounded pebble. */
private fun DrawScope.pebble38(c: Offset, r: Float, fill: Color, alpha: Float, glow: Float = 0f) {
    if (alpha <= 0.01f) return
    if (glow > 0f) softGlow(c, r * 2.6f, fill, 0.45f * glow * alpha)
    drawOval(fill, Offset(c.x - r, c.y - r * 0.62f), Size(r * 2f, r * 1.24f), alpha = alpha)
    drawOval(Color.White, Offset(c.x - r * 0.55f, c.y - r * 0.5f), Size(r * 0.7f, r * 0.32f), alpha = 0.35f * alpha)
}

/** A soft grey cloud made of overlapping discs. */
internal fun DrawScope.cloud38(c: Offset, r: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val bumps = listOf(Offset(-0.55f, 0.1f), Offset(-0.15f, -0.25f), Offset(0.3f, -0.15f), Offset(0.6f, 0.12f), Offset(0f, 0.2f))
    for ((k, b) in bumps.withIndex()) drawCircle(color, r * (0.42f + 0.06f * (k % 2)), c + Offset(b.x * r, b.y * r), alpha = alpha * 0.92f)
}

// ═════════════ 1. clear stones and a few clouded ones (bs-mutashabih) ═════════════

private object FogC38 {
    val Sky = Color(0xFF2A2440)
    val SkyLow = Color(0xFFB88A6A)
    val Ground = Color(0xFF5A4A3E)
    val Clear = Color(0xFFF4DE9A)
    val Fog = Color(0xFF8A8A96)
    val Tablet = Color(0xFFE8DCC0)
    val Red = Color(0xFFE0584A)
    val Cell = Color(0xFF9AD0C0)
}

/**
 * Beats: twoages (two tablets side by side, «22» and «42», and a red sign between: a real contradiction needs no
 * prosecutor), fewfog (a field of many clear stones and a few clouded ones: «الْمُحْكَمُ», «الْمُتَشَابِهُ»),
 * threeorigins (three clouded stones are lifted and named: weak reports, Arabic, an unseen wisdom), cellglow (a lens
 * over one cell: rows upon rows of bright letters, beside which the clouded stone looks small).
 */
internal fun DrawScope.fogstonesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twoages" to Shot(Offset(w * 0.5f, h * 0.36f), 1.3f),
        "fewfog" to Shot(Offset(w * 0.5f, h * 0.68f), 1.05f),
        "threeorigins" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "cellglow" to Shot(Offset(w * 0.68f, h * 0.45f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { fogField(a) }
}

private fun DrawScope.fogField(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ta = a.b("twoages")
    val ff = a.b("fewfog")
    val to = a.b("threeorigins")
    val cg = a.b("cellglow")
    val ground = h * 0.6f
    nightSky(t, FogC38.Sky, FogC38.SkyLow, ground, starAlpha = 0.5f)
    drawRect(FogC38.Ground, Offset(0f, ground), Size(w, h - ground))
    // two tablets that cannot both be right
    val tab = easeOut(seg(ta, 0f, 0.35f)) * (1f - 0.75f * seg(ff, 0f, 0.4f))
    if (tab > 0f) {
        for (k in 0 until 2) {
            val c = Offset(w * (0.36f + 0.28f * k), h * 0.34f)
            drawRoundRect(FogC38.Tablet, Offset(c.x - w * 0.09f, c.y - h * 0.12f), Size(w * 0.18f, h * 0.24f), CornerRadius(w * 0.03f, w * 0.03f), alpha = tab)
            for (r in 0 until 4) drawLine(Color(0xFF9A8A6A), Offset(c.x - w * 0.06f, c.y - h * 0.06f + r * h * 0.035f), Offset(c.x + w * 0.06f, c.y - h * 0.06f + r * h * 0.035f), 1.2f * dp, alpha = tab * 0.7f)
            tagAt(a.tag("twoages", 0, k), c + Offset(0f, -h * 0.15f), seg(ta, 0.3f + 0.15f * k, 0.55f + 0.15f * k) * (1f - seg(ff, 0f, 0.4f)), back = Back38, ink = ArtInk.Gold)
        }
        val neq = seg(ta, 0.6f, 0.85f) * tab
        val m = Offset(w * 0.5f, h * 0.34f)
        drawLine(FogC38.Red, m + Offset(-w * 0.025f, -h * 0.012f), m + Offset(w * 0.025f, -h * 0.012f), 3f * dp, StrokeCap.Round, alpha = neq)
        drawLine(FogC38.Red, m + Offset(-w * 0.025f, h * 0.012f), m + Offset(w * 0.025f, h * 0.012f), 3f * dp, StrokeCap.Round, alpha = neq)
        drawLine(FogC38.Red, m + Offset(w * 0.015f, -h * 0.035f), m + Offset(-w * 0.015f, h * 0.035f), 3f * dp, StrokeCap.Round, alpha = neq)
    }
    // the field: many clear stones, a few clouded ones
    val field = easeOut(seg(ff, 0f, 0.5f))
    val fogged = setOf(4, 11, 17)
    if (field > 0f) {
        for (k in 0 until 21) {
            val row = k / 7
            val col = k % 7
            val c = Offset(w * (0.1f + 0.13f * col + 0.05f * (row % 2)), ground + h * (0.08f + 0.1f * row))
            val r = w * (0.03f + 0.006f * ((k * 7) % 3))
            val lifted = if (k in fogged) easeInOut(seg(to, 0.1f + 0.15f * fogged.indexOf(k), 0.5f + 0.15f * fogged.indexOf(k))) else 0f
            val at = c + Offset(0f, -h * 0.12f * lifted)
            val pop = seg(ff, 0.05f + 0.02f * k, 0.3f + 0.02f * k)
            if (k in fogged) {
                pebble38(at, r, FogC38.Fog, pop)
                cloud38(at + Offset(0f, -r * 0.4f), r * 1.1f, Color(0xFFB8B8C4), 0.55f * pop * (1f - 0.4f * lifted))
            } else {
                pebble38(at, r, FogC38.Clear, pop, glow = 0.5f + 0.2f * sin(t * 1.3f + k))
            }
        }
        tagAt(a.tag("fewfog", 0, 0), Offset(w * 0.3f, ground + h * 0.38f), seg(ff, 0.5f, 0.75f) * (1f - seg(to, 0f, 0.3f)), back = Back38, ink = ArtInk.Gold)
        tagAt(a.tag("fewfog", 0, 1), Offset(w * 0.72f, ground + h * 0.38f), seg(ff, 0.6f, 0.85f) * (1f - seg(to, 0f, 0.3f)), back = Back38, ink = Color(0xFFD8D8E0))
    }
    // the three sources of their doubts, named over the clouded stones
    if (to > 0f) {
        for (n in 0 until minOf(3, fogged.size)) {
            // spread in a row above the field, so the three names never sit on each other
            val c = Offset(w * (0.78f - 0.28f * n), ground - h * (0.1f + 0.06f * (n % 2)))
            tagAt(a.tag("threeorigins", 0, n), c, seg(to, 0.35f + 0.15f * n, 0.6f + 0.15f * n) * (1f - seg(cg, 0f, 0.3f)), back = Back38, ink = Color(0xFFE0E0E8))
        }
    }
    // one cell under the lens: billions of letters in order
    if (cg > 0f) {
        val f = easeOut(seg(cg, 0f, 0.35f))
        val c = Offset(w * 0.68f, h * 0.36f)
        val r = w * 0.15f * f
        drawCircle(Color(0xFF12201E), r, c, alpha = 0.9f * f)
        drawCircle(FogC38.Cell, r * 0.42f, c, alpha = 0.25f * f)
        val rows = 9
        for (j in 0 until rows) {
            val y = c.y - r * 0.8f + j * r * 0.2f
            val half = r * 0.95f * sin(PI.toFloat() * (j + 0.5f) / rows)
            val fill = seg(cg, 0.2f + j * 0.05f, 0.5f + j * 0.05f)
            var x = c.x - half
            var i = 0
            while (x < c.x - half + 2f * half * fill) {
                drawRect(if ((i + j) % 4 == 0) ArtInk.Gold else FogC38.Cell, Offset(x, y), Size(w * 0.006f, h * 0.008f), alpha = 0.85f * f)
                x += w * 0.011f
                i++
            }
        }
        drawCircle(ArtInk.BrassLight, r, c, alpha = f, style = Stroke(3f * dp))
        drawLine(ArtInk.BrassLight, c + Offset(r * 0.7f, r * 0.7f), c + Offset(r * 1.25f, r * 1.25f), 6f * dp, StrokeCap.Round, alpha = f)
        softGlow(c, r * 1.3f, FogC38.Cell, 0.25f * f)
    }
}

// ═════════════ 2. brighter than the sun, reached by a path (bs-shams-dalil) ═════════════

private object SunC38 {
    val Sky = Color(0xFF9AC0E0)
    val SkyLow = Color(0xFFF2E2C2)
    val Ground = Color(0xFFC8A87A)
    val Figure = Color(0xFF2E2620)
    val Sun = Color(0xFFFFE6A0)
    val Heart = Color(0xFFE8B04A)
    val Step = Color(0xFFE8D8B0)
    val Door = Color(0xFF5A4A40)
    val Exam = Color(0xFFF6F0E2)
}

/**
 * Beats: askingsky (a figure looks up at the bright, empty sky and a question mark), heartsun (in his chest a heart
 * lights up brighter than the sun above), seekpath (stepping stones climb toward it: «النَّظَرُ», «الْبَحْثُ»,
 * «التَّفَكُّرُ»), noexam (if all were forced, the path would vanish: an exam sheet already filled, «لَا اخْتِبَارَ»),
 * threedoors (three doors on the horizon; two stay dark, one is lit: «شِرْكِيَّاتُ الْأَوَّلِينَ», «جُنُونُ
 * الْإِلْحَادِ», «تَوْحِيدُ الْإِسْلَامِ»).
 */
internal fun DrawScope.sunproofScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "askingsky" to Shot(Offset(w * 0.3f, h * 0.5f), 1.25f),
        "heartsun" to Shot(Offset(w * 0.3f, h * 0.55f), 1.4f),
        "seekpath" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "noexam" to Shot(Offset(w * 0.72f, h * 0.45f), 1.3f),
        "threedoors" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { sunPlain(a) }
}

private fun DrawScope.sunPlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ak = a.b("askingsky")
    val hs = a.b("heartsun")
    val sp = a.b("seekpath")
    val ne = a.b("noexam")
    val td = a.b("threedoors")
    val ground = h * 0.78f
    drawRect(Brush.verticalGradient(listOf(SunC38.Sky, SunC38.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(SunC38.Ground, Offset(0f, ground), Size(w, h - ground))
    // the sun
    val sun = Offset(w * 0.78f, h * 0.16f)
    softGlow(sun, w * 0.12f, SunC38.Sun, 0.7f)
    drawCircle(SunC38.Sun, w * 0.045f, sun)
    // the one who asks
    val foot = Offset(w * 0.24f, ground)
    figure(foot, h * 0.26f, SunC38.Figure, easeOut(seg(ak, 0f, 0.3f)), lean = -0.18f * easeOut(seg(ak, 0.2f, 0.6f)))
    qmark21(foot + Offset(w * 0.07f, -h * 0.36f), h * 0.07f, Color(0xFF3A4A6A), seg(ak, 0.4f, 0.7f) * (1f - seg(hs, 0f, 0.4f)))
    // the heart brighter than the sun
    if (hs > 0f) {
        val f = easeOut(seg(hs, 0f, 0.5f))
        val hc = foot + Offset(0f, -h * 0.17f)
        softGlow(hc, w * (0.06f + 0.12f * f), SunC38.Heart, 0.7f * f)
        drawPath(w8Heart(hc, w * 0.03f), SunC38.Heart, alpha = f)
        lightShafts(hc, 0f, 2f * PI.toFloat(), w * 0.22f * f, SunC38.Sun, 0.5f * f, t, rays = 9)
    }
    // the path: looking, searching, thinking
    if (sp > 0f) {
        for (k in 0 until 6) {
            val u = k / 5f
            val c = Offset(w * (0.34f + 0.1f * k), ground - h * (0.02f + 0.06f * k))
            val g = easeOut(seg(sp, 0.05f + 0.1f * k, 0.3f + 0.1f * k)) * (1f - 0.8f * seg(ne, 0.2f, 0.6f))
            drawRoundRect(SunC38.Step, Offset(c.x - w * 0.04f, c.y - h * 0.012f), Size(w * 0.08f, h * 0.024f), CornerRadius(4f * dp), alpha = g)
            if (u > 0.9f) softGlow(c, w * 0.05f, SunC38.Heart, 0.4f * g)
        }
        for (k in 0 until 3) {
            val c = Offset(w * (0.39f + 0.2f * k), ground - h * (0.1f + 0.12f * k))
            tagAt(a.tag("seekpath", 0, k), c, seg(sp, 0.4f + 0.15f * k, 0.65f + 0.15f * k) * (1f - seg(ne, 0f, 0.3f)), back = Back38, ink = ArtInk.Gold)
        }
    }
    // no test: a sheet already filled in
    if (ne > 0f) {
        val f = easeOut(seg(ne, 0f, 0.35f)) * (1f - seg(td, 0f, 0.4f))
        val c = Offset(w * 0.72f, h * 0.42f)
        drawRect(SunC38.Exam, Offset(c.x - w * 0.09f, c.y - h * 0.13f), Size(w * 0.18f, h * 0.26f), alpha = f)
        for (r in 0 until 5) {
            val y = c.y - h * 0.09f + r * h * 0.045f
            drawLine(Color(0xFF8A8070), Offset(c.x - w * 0.06f, y), Offset(c.x + w * 0.02f, y), 1.2f * dp, alpha = f)
            check21(Offset(c.x + w * 0.05f, y), w * 0.012f, Color(0xFF2E9A55), seg(ne, 0.2f + r * 0.06f, 0.4f + r * 0.06f), 2f * dp)
        }
        tagAt(a.tag("noexam"), c + Offset(0f, h * 0.17f), seg(ne, 0.5f, 0.75f) * (1f - seg(td, 0f, 0.4f)), back = Back38, ink = Color(0xFFFFE0D0))
    }
    // three doors, only one lit
    if (td > 0f) {
        for (k in 0 until 3) {
            val x = w * (0.22f + 0.28f * k)
            val f = easeOut(seg(td, 0.05f + 0.12f * k, 0.35f + 0.12f * k))
            val lit = k == 2
            val top = h * 0.36f
            drawPath(archPath26(x, top, ground, w * 0.06f), if (lit) Color(0xFFFFF0C8) else SunC38.Door, alpha = f)
            drawPath(archPath26(x, top, ground, w * 0.06f), ArtInk.BrassDark, alpha = f, style = Stroke(2f * dp))
            if (lit) {
                val glow = seg(td, 0.6f, 1f)
                softGlow(Offset(x, h * 0.55f), w * 0.12f, ArtInk.Gold, 0.6f * glow)
                lightShafts(Offset(x, h * 0.5f), PI.toFloat() / 2f, 0.8f, h * 0.3f, Color(0xFFFFF0C8), 0.5f * glow, t, rays = 5)
            }
            tagAt(a.tag("threedoors", 0, k), Offset(x, top - h * 0.06f - h * 0.05f * (k % 2)), seg(td, 0.25f + 0.15f * k, 0.5f + 0.15f * k), back = Back38, ink = if (lit) ArtInk.Gold else Color(0xFFC8C0B8))
        }
    }
}

// ═════════════ 3. desire, the doubt's courier (bs-hawa) ═════════════

private object HawaC38 {
    val Sky = Color(0xFF1A1830)
    val SkyLow = Color(0xFF3A2E48)
    val Ground = Color(0xFF2A2230)
    val Pillar = Color(0xFF6A3A4A)
    val Roof = Color(0xFF4A3A44)
    val Heart = Color(0xFFF2C45A)
    val Letter = Color(0xFF3A3040)
    val Seal = Color(0xFF8A2A3A)
    val Door = Color(0xFF6A5A4A)
}

/**
 * Beats: pillarhawa (a crooked house of unbelief stands on a single pillar, «الْهَوَى»), dimheart (on the right, a heart of
 * certainty; dark sealed letters fly to it and it dims), doubtsfall (seeking forgiveness: the letters crumble and fall
 * and the heart burns bright again), twodoors (two doors in a row: lock the first and the second is locked too —
 * «ضَبْطُ الشَّهْوَةِ», «ضَبْطُ الشُّبْهَةِ»).
 */
internal fun DrawScope.postmanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "pillarhawa" to Shot(Offset(w * 0.28f, h * 0.5f), 1.3f),
        "dimheart" to Shot(Offset(w * 0.62f, h * 0.45f), 1.2f),
        "doubtsfall" to Shot(Offset(w * 0.7f, h * 0.45f), 1.3f),
        "twodoors" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { hawaNight(a) }
}

private fun DrawScope.hawaNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ph = a.b("pillarhawa")
    val dh = a.b("dimheart")
    val df = a.b("doubtsfall")
    val tdr = a.b("twodoors")
    val ground = h * 0.82f
    nightSky(t, HawaC38.Sky, HawaC38.SkyLow, ground, starAlpha = 0.5f)
    drawRect(HawaC38.Ground, Offset(0f, ground), Size(w, h - ground))
    // the house of unbelief on one pillar
    val house = easeOut(seg(ph, 0f, 0.4f)) * (1f - 0.6f * seg(tdr, 0f, 0.4f))
    if (house > 0f) {
        val base = Offset(w * 0.26f, ground)
        val sway = 0.04f * sin(t * 1.2f)
        drawLine(HawaC38.Pillar, base, base + Offset(w * sway * 0.5f, -h * 0.34f), w * 0.04f, StrokeCap.Butt, alpha = house)
        val top = base + Offset(w * sway * 0.5f, -h * 0.34f)
        drawPath(Path().apply {
            moveTo(top.x - w * 0.16f, top.y); lineTo(top.x + w * 0.16f, top.y + h * 0.02f)
            lineTo(top.x + w * 0.12f, top.y - h * 0.12f); lineTo(top.x - w * 0.13f, top.y - h * 0.13f); close()
        }, HawaC38.Roof, alpha = house)
        drawPath(Path().apply {
            moveTo(top.x - w * 0.15f, top.y - h * 0.13f); lineTo(top.x, top.y - h * 0.22f); lineTo(top.x + w * 0.13f, top.y - h * 0.12f); close()
        }, HawaC38.Pillar, alpha = house)
        tagAt(a.tag("pillarhawa"), base + Offset(0f, -h * 0.14f), seg(ph, 0.4f, 0.7f) * (1f - seg(tdr, 0f, 0.4f)), back = Back38, ink = Color(0xFFF0B0B8))
    }
    // the heart of certainty, dimmed by the letters, bright again after seeking forgiveness
    val hc = Offset(w * 0.72f, h * 0.42f)
    val heartOn = easeOut(seg(dh, 0f, 0.3f)) * (1f - 0.7f * seg(tdr, 0f, 0.4f))
    if (heartOn > 0f) {
        val dim = seg(dh, 0.4f, 0.9f) * (1f - easeOut(seg(df, 0.4f, 0.9f)))
        val bright = 1f - 0.75f * dim
        softGlow(hc, w * 0.14f * bright, HawaC38.Heart, 0.7f * heartOn * bright)
        drawPath(w8Heart(hc, w * 0.05f), lerpColor(HawaC38.Heart, Color(0xFF5A4A40), dim), alpha = heartOn)
        // dark sealed letters fly from the house to the heart, then fall apart
        for (k in 0 until 5) {
            val u = easeInOut(seg(dh, 0.05f + 0.08f * k, 0.55f + 0.08f * k))
            val from = Offset(w * 0.3f, h * 0.3f)
            val to = hc + Offset(w * (-0.08f + 0.04f * k), -h * 0.04f + h * 0.02f * (k % 2))
            var p = from + (to - from) * u + Offset(0f, -h * 0.1f * sin(PI.toFloat() * u))
            val fall = easeIn38(seg(df, 0.05f + 0.06f * k, 0.6f + 0.06f * k))
            p += Offset(w * 0.02f * sin(k * 2f), (ground - p.y) * fall)
            val al = heartOn * seg(dh, 0.05f + 0.08f * k, 0.15f + 0.08f * k) * (1f - fall)
            drawRect(HawaC38.Letter, Offset(p.x - w * 0.025f, p.y - h * 0.015f), Size(w * 0.05f, h * 0.03f), alpha = al)
            drawCircle(HawaC38.Seal, w * 0.006f, p, alpha = al)
            for (j in 0 until 3) drawCircle(HawaC38.Letter, w * 0.004f, p + Offset(w * 0.01f * (j - 1), h * 0.01f * j * fall), alpha = heartOn * fall * (1f - fall))
        }
    }
    // two doors: control the first and the second is controlled
    if (tdr > 0f) {
        for (k in 0 until 2) {
            val x = w * (0.36f + 0.28f * k)
            val f = easeOut(seg(tdr, 0.05f + 0.15f * k, 0.35f + 0.15f * k))
            val top = h * 0.5f
            drawRect(HawaC38.Door, Offset(x - w * 0.07f, top), Size(w * 0.14f, ground - top), alpha = f)
            drawRect(ArtInk.BrassDark, Offset(x - w * 0.07f, top), Size(w * 0.14f, ground - top), alpha = f, style = Stroke(2f * dp))
            val shut = seg(tdr, 0.45f + 0.2f * k, 0.65f + 0.2f * k)
            padlock21(Offset(x, h * 0.66f), w * 0.04f, ArtInk.Brass, 1f - shut, f)
            tagAt(a.tag("twodoors", 0, k), Offset(x, top - h * 0.05f), seg(tdr, 0.25f + 0.2f * k, 0.5f + 0.2f * k), back = Back38, ink = ArtInk.Gold)
        }
        arrowHead(Offset(w * 0.53f, h * 0.68f), Offset(1f, 0f), w * 0.025f, ArtInk.BrassLight, 2.5f * dp, seg(tdr, 0.5f, 0.7f))
        drawLine(ArtInk.BrassLight, Offset(w * 0.46f, h * 0.68f), Offset(w * 0.53f, h * 0.68f), 2.5f * dp, alpha = seg(tdr, 0.5f, 0.7f))
    }
}

private fun easeIn38(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ═════════════ 4. a reaction a billion times, and three missing pillars (bs-arkan-shubha) ═════════════

private object LabC38 {
    val Wall = Color(0xFF1E2A30)
    val WallLow = Color(0xFF2E3A40)
    val Bench = Color(0xFF5A4A3A)
    val Glass = Color(0xFFCFE4EE)
    val Acid = Color(0xFFE8C04A)
    val Base = Color(0xFF6AB0E8)
    val Salt = Color(0xFFF6F6F2)
    val Brain = Color(0xFFD89A9A)
    val Gear = Color(0xFF9AA4B0)
    val Brick = Color(0xFF8A6A5A)
}

/**
 * Beats: shouldnot (a scroll and a red question mark: something that should not have been), reaction (two flasks pour
 * into a beaker, white salt settles and water clears; a tally of the same result grows), braingears (a brain outline
 * fills with gears turning in lockstep), threepillars (three dashed pillar outlines under a roof that cannot stand:
 * «إِرَادَةٌ حُرَّةٌ», «قِيَمٌ أَخْلَاقِيَّةٌ مُطْلَقَةٌ», «عَقْلٌ أَحْكَامُهُ صَحِيحَةٌ»), innocentwall (a plain brick wall: it
 * knows no good and no evil).
 */
internal fun DrawScope.acidbaseScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "shouldnot" to Shot(Offset(w * 0.2f, h * 0.4f), 1.35f),
        "reaction" to Shot(Offset(w * 0.42f, h * 0.55f), 1.3f),
        "braingears" to Shot(Offset(w * 0.78f, h * 0.45f), 1.35f),
        "threepillars" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "innocentwall" to Shot(Offset(w * 0.5f, h * 0.75f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { labRoom(a) }
}

private fun DrawScope.labRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sn = a.b("shouldnot")
    val re = a.b("reaction")
    val bg = a.b("braingears")
    val tp = a.b("threepillars")
    val iw = a.b("innocentwall")
    val bench = h * 0.66f
    drawRect(Brush.verticalGradient(listOf(LabC38.Wall, LabC38.WallLow), endY = bench), size = Size(w, bench))
    drawRect(LabC38.Bench, Offset(0f, bench), Size(w, h - bench))
    val away = 1f - 0.7f * seg(tp, 0f, 0.4f)
    // what should not have been
    if (sn > 0f) {
        val f = easeOut(seg(sn, 0f, 0.35f)) * away
        scroll14(Offset(w * 0.2f, h * 0.4f), w * 0.14f, h * 0.24f, easeOut(seg(sn, 0.1f, 0.5f)), f)
        qmark21(Offset(w * 0.2f, h * 0.4f), h * 0.08f, Color(0xFFE0584A), seg(sn, 0.45f, 0.75f) * away)
    }
    // the reaction, the same every time
    if (re > 0f) {
        val f = easeOut(seg(re, 0f, 0.3f)) * away
        val beaker = Offset(w * 0.45f, bench - h * 0.02f)
        for (k in 0 until 2) {
            val tilt = easeInOut(seg(re, 0.15f + 0.12f * k, 0.4f + 0.12f * k)) * (1f - seg(re, 0.6f, 0.75f))
            val c = Offset(w * (0.33f + 0.24f * k), bench - h * 0.2f)
            val ang = (if (k == 0) 1f else -1f) * tilt * 1.1f
            val tip = c + Offset(sin(ang) * h * 0.08f, -cos(ang) * h * 0.08f)
            drawLine(LabC38.Glass, c, tip, w * 0.03f, StrokeCap.Round, alpha = f * 0.8f)
            drawCircle(if (k == 0) LabC38.Acid else LabC38.Base, w * 0.035f, c, alpha = f)
            if (tilt > 0.6f) drawLine(if (k == 0) LabC38.Acid else LabC38.Base, tip, beaker + Offset(0f, -h * 0.08f), 2f * dp, alpha = f * 0.8f)
        }
        drawRect(LabC38.Glass, Offset(beaker.x - w * 0.05f, beaker.y - h * 0.12f), Size(w * 0.1f, h * 0.12f), alpha = f * 0.35f)
        drawRect(LabC38.Glass, Offset(beaker.x - w * 0.05f, beaker.y - h * 0.12f), Size(w * 0.1f, h * 0.12f), alpha = f, style = Stroke(2f * dp))
        val salt = seg(re, 0.55f, 0.85f)
        for (k in 0 until 7) drawRect(LabC38.Salt, Offset(beaker.x - w * 0.04f + k * w * 0.012f, beaker.y - h * 0.012f - h * 0.006f * (k % 2)), Size(w * 0.009f, h * 0.009f), alpha = f * salt)
        // a tally: the same result, again and again
        val runs = (seg(re, 0.6f, 1f) * 18f).toInt()
        for (k in 0 until runs) {
            val x = w * 0.3f + (k % 9) * w * 0.035f
            val y = h * 0.14f + (k / 9) * h * 0.06f
            drawLine(ArtInk.BrassLight, Offset(x, y), Offset(x, y + h * 0.04f), 2f * dp, alpha = f)
        }
    }
    // the brain, under the same laws
    if (bg > 0f) {
        val f = easeOut(seg(bg, 0f, 0.35f)) * away
        val c = Offset(w * 0.78f, h * 0.4f)
        drawOval(LabC38.Brain, Offset(c.x - w * 0.12f, c.y - h * 0.11f), Size(w * 0.24f, h * 0.2f), alpha = f * 0.35f)
        drawOval(LabC38.Brain, Offset(c.x - w * 0.12f, c.y - h * 0.11f), Size(w * 0.24f, h * 0.2f), alpha = f, style = Stroke(2.5f * dp))
        for (k in 0 until 3) {
            val g = c + Offset(w * (-0.06f + 0.06f * k), h * (0.01f - 0.03f * (k % 2)))
            val spin = t * (if (k % 2 == 0) 1.2f else -1.2f)
            val gf = f * seg(bg, 0.2f + 0.12f * k, 0.5f + 0.12f * k)
            drawCircle(LabC38.Gear, w * 0.028f, g, alpha = gf)
            for (j in 0 until 8) {
                val an = spin + j * PI.toFloat() / 4f
                drawLine(LabC38.Gear, g, g + Offset(cos(an) * w * 0.038f, sin(an) * w * 0.038f), w * 0.01f, alpha = gf)
            }
            drawCircle(LabC38.Wall, w * 0.01f, g, alpha = gf)
        }
    }
    // three pillars that are not there
    if (tp > 0f) {
        val f = easeOut(seg(tp, 0f, 0.3f)) * (1f - 0.6f * seg(iw, 0f, 0.4f))
        val roofY = h * 0.16f
        val sag = easeInOut(seg(tp, 0.7f, 1f)) * h * 0.05f
        drawPath(Path().apply { moveTo(w * 0.14f, roofY + sag * 0.3f); lineTo(w * 0.86f, roofY + sag); lineTo(w * 0.8f, roofY - h * 0.06f + sag); lineTo(w * 0.2f, roofY - h * 0.06f); close() }, Color(0xFF6A6A78), alpha = f)
        for (k in 0 until 3) {
            val x = w * (0.24f + 0.26f * k)
            val g = seg(tp, 0.1f + 0.15f * k, 0.4f + 0.15f * k)
            drawRect(Color(0xFFC8C8D0), Offset(x - w * 0.035f, roofY + h * 0.02f), Size(w * 0.07f, bench - roofY - h * 0.02f), alpha = f * g,
                style = Stroke(2f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp))))
            tagAt(a.tag("threepillars", 0, k), Offset(x, h * (0.29f + 0.115f * k)), seg(tp, 0.25f + 0.15f * k, 0.5f + 0.15f * k) * (1f - seg(iw, 0f, 0.4f)), back = Back38, ink = Color(0xFFE8E8F0))
        }
    }
    // the wall behind you: no good, no evil
    if (iw > 0f) {
        val f = easeOut(seg(iw, 0f, 0.4f))
        for (r in 0 until 4) for (k in 0 until 9) {
            val x = w * (0.08f + 0.095f * k) + (if (r % 2 == 1) w * 0.045f else 0f)
            val y = h * 0.92f - h * 0.05f * (r + 1)
            drawRect(LabC38.Brick, Offset(x, y), Size(w * 0.09f, h * 0.045f), alpha = f)
            drawRect(Color(0xFF4A3A30), Offset(x, y), Size(w * 0.09f, h * 0.045f), alpha = f, style = Stroke(1f * dp))
        }
    }
}

// ═════════════ 5. a doctor's diploma and a prescription (bs-tabib) ═════════════

private object ClinicC38 {
    val Wall = Color(0xFFE6E2D6)
    val WallLow = Color(0xFFD2CCBC)
    val Floor = Color(0xFF8A7A66)
    val Frame = Color(0xFF8A6A3A)
    val Paper = Color(0xFFFBF7EC)
    val Seal = Color(0xFFC8384A)
    val Bottle = Color(0xFF5A8AB0)
    val Fog = Color(0xFF9A9AA6)
    val Leaf = Color(0xFF8A7A4A)
}

/**
 * Beats: threefogs (three small clouds drift in, named: «آثَارٌ ضَعِيفَةٌ», «اجْتِهَادَاتٌ غَيْرُ مَعْصُومَةٍ», «تَصَوُّرٌ
 * عَقْلِيٌّ»), diploma (a framed diploma with a red seal on the wall: he is a doctor), prescription (a bottle and a
 * prescription with a question mark you need not answer to trust it), proofsealed (his light rises over the room and
 * the clouds thin away), deadleaf (a withered leaf on the floor, left where it fell).
 */
internal fun DrawScope.clinicScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threefogs" to Shot(Offset(w * 0.5f, h * 0.3f), 1.15f),
        "diploma" to Shot(Offset(w * 0.3f, h * 0.42f), 1.35f),
        "prescription" to Shot(Offset(w * 0.68f, h * 0.6f), 1.35f),
        "proofsealed" to Shot(Offset(w * 0.5f, h * 0.4f), 1.05f),
        "deadleaf" to Shot(Offset(w * 0.3f, h * 0.85f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { clinicRoom(a) }
}

private fun DrawScope.clinicRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tf = a.b("threefogs")
    val di = a.b("diploma")
    val pr = a.b("prescription")
    val ps = a.b("proofsealed")
    val dl = a.b("deadleaf")
    val floor = h * 0.78f
    drawRect(Brush.verticalGradient(listOf(ClinicC38.Wall, ClinicC38.WallLow), endY = floor), size = Size(w, floor))
    drawRect(ClinicC38.Floor, Offset(0f, floor), Size(w, h - floor))
    // three clouds drift in
    val clear = easeOut(seg(ps, 0.3f, 0.9f))
    if (tf > 0f) {
        for (k in 0 until 3) {
            val drift = easeOut(seg(tf, 0.05f + 0.15f * k, 0.4f + 0.15f * k))
            val c = Offset(w * (0.2f + 0.3f * k) + w * 0.03f * sin(t * 0.5f + k), h * (0.22f + 0.05f * (k % 2)) - h * 0.1f * (1f - drift))
            cloud38(c, w * 0.08f, ClinicC38.Fog, 0.75f * drift * (1f - clear))
            tagAt(a.tag("threefogs", 0, k), c + Offset(0f, h * 0.08f), seg(tf, 0.3f + 0.15f * k, 0.55f + 0.15f * k) * (1f - seg(di, 0f, 0.3f)), back = Back38, ink = Color(0xFFE8E8F0))
        }
    }
    // the diploma
    if (di > 0f) {
        val f = easeOut(seg(di, 0f, 0.4f))
        val c = Offset(w * 0.3f, h * 0.44f)
        drawRect(ClinicC38.Frame, Offset(c.x - w * 0.12f, c.y - h * 0.1f), Size(w * 0.24f, h * 0.2f), alpha = f)
        drawRect(ClinicC38.Paper, Offset(c.x - w * 0.105f, c.y - h * 0.085f), Size(w * 0.21f, h * 0.17f), alpha = f)
        for (r in 0 until 3) drawLine(Color(0xFFB8A888), Offset(c.x - w * 0.07f, c.y - h * 0.05f + r * h * 0.03f), Offset(c.x + w * 0.07f, c.y - h * 0.05f + r * h * 0.03f), 1.4f * dp, alpha = f)
        val seal = easeOut(seg(di, 0.4f, 0.8f))
        drawCircle(ClinicC38.Seal, w * 0.02f * seal, c + Offset(w * 0.06f, h * 0.05f), alpha = f)
        glint(c + Offset(w * 0.06f, h * 0.05f), w * 0.01f, Color.White, seal * 0.8f)
    }
    // the prescription: a medicine whose wisdom you do not know
    if (pr > 0f) {
        val f = easeOut(seg(pr, 0f, 0.35f))
        val b = Offset(w * 0.62f, floor - h * 0.06f)
        drawRect(Color(0xFF6A5444), Offset(w * 0.5f, floor - h * 0.04f), Size(w * 0.36f, h * 0.04f), alpha = f)
        drawRoundRect(ClinicC38.Bottle, Offset(b.x - w * 0.03f, b.y - h * 0.12f), Size(w * 0.06f, h * 0.1f), CornerRadius(6f * dp), alpha = f)
        drawRect(ClinicC38.Paper, Offset(b.x - w * 0.02f, b.y - h * 0.15f), Size(w * 0.04f, h * 0.03f), alpha = f)
        val sheet = Offset(w * 0.76f, floor - h * 0.12f)
        drawRect(ClinicC38.Paper, Offset(sheet.x - w * 0.06f, sheet.y - h * 0.08f), Size(w * 0.12f, h * 0.16f), alpha = f)
        for (r in 0 until 4) drawLine(Color(0xFF8A8070), Offset(sheet.x - w * 0.04f, sheet.y - h * 0.05f + r * h * 0.03f), Offset(sheet.x + w * 0.04f, sheet.y - h * 0.05f + r * h * 0.03f), 1.2f * dp, alpha = f)
        val q = seg(pr, 0.3f, 0.6f) * (1f - seg(pr, 0.75f, 1f))
        qmark21(b + Offset(w * 0.07f, -h * 0.2f), h * 0.05f, Color(0xFF4A5A7A), q)
        check21(b + Offset(w * 0.07f, -h * 0.19f), w * 0.02f, Color(0xFF2E9A55), seg(pr, 0.8f, 1f), 3f * dp)
    }
    // his light: the proof that settles everything
    if (ps > 0f) {
        val f = easeOut(seg(ps, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.2f)
        sirajMunir(c, w * 0.012f, f, t, nameAt = c + Offset(0f, -h * 0.1f))
        lightShafts(c, PI.toFloat() / 2f, 1.4f, h * 0.6f, Color(0xFFFFF0C8), 0.35f * f, t, rays = 7)
    }
    // the dead doubt, left where it fell
    if (dl > 0f) {
        val f = easeOut(seg(dl, 0f, 0.5f))
        val c = Offset(w * 0.3f, floor + h * 0.07f)
        drawPath(Path().apply {
            moveTo(c.x - w * 0.05f, c.y); quadraticTo(c.x - w * 0.01f, c.y - h * 0.04f, c.x + w * 0.05f, c.y - h * 0.005f)
            quadraticTo(c.x, c.y + h * 0.02f, c.x - w * 0.05f, c.y); close()
        }, ClinicC38.Leaf, alpha = f)
        drawLine(Color(0xFF5A4A2A), Offset(c.x - w * 0.05f, c.y), Offset(c.x + w * 0.06f, c.y - h * 0.008f), 1.2f * dp, alpha = f)
    }
}

// ═════════════ 6. thoughts that pop up and multiply (bs-waswas) ═════════════

private object PopC38 {
    val Sky = Color(0xFF14162A)
    val SkyLow = Color(0xFF262842)
    val Mind = Color(0xFF3A4A6A)
    val Bubble = Color(0xFF5A4A6A)
    val Dark = Color(0xFF2A2236)
    val Answer = Color(0xFF6ABF8A)
    val Fire = Color(0xFFF08A3A)
    val Can = Color(0xFFB04A3A)
    val Core = Color(0xFFF6E2A8)
}

/**
 * Beats: popthoughts (a round mind; dark bubbles jump up out of it), oneq (one plain question gets its answer: a tick,
 * and it is gone), hydra (answer one bubble and two more pop up: «رُبَّمَا», «مَاذَا لَوْ»), fuelfire (a can pours onto a
 * small fire and it leaps), sarih (under all the bubbles a calm golden core: the faith that hates them).
 */
internal fun DrawScope.popcornScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "popthoughts" to Shot(Offset(w * 0.4f, h * 0.5f), 1.2f),
        "oneq" to Shot(Offset(w * 0.2f, h * 0.35f), 1.4f),
        "hydra" to Shot(Offset(w * 0.45f, h * 0.35f), 1.15f),
        "fuelfire" to Shot(Offset(w * 0.78f, h * 0.65f), 1.35f),
        "sarih" to Shot(Offset(w * 0.4f, h * 0.62f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { popMind(a) }
}

private fun DrawScope.popMind(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pt = a.b("popthoughts")
    val oq = a.b("oneq")
    val hy = a.b("hydra")
    val fu = a.b("fuelfire")
    val sa = a.b("sarih")
    nightSky(t, PopC38.Sky, PopC38.SkyLow, h, starAlpha = 0.4f)
    val mind = Offset(w * 0.4f, h * 0.66f)
    val mf = easeOut(seg(pt, 0f, 0.3f))
    drawCircle(PopC38.Mind, w * 0.15f, mind, alpha = mf)
    drawCircle(Color(0xFF8A9AC0), w * 0.15f, mind, alpha = mf, style = Stroke(2f * dp))
    // thoughts jumping out of it
    if (pt > 0f) {
        for (k in 0 until 6) {
            val u = fract(t * 0.25f + k / 6f)
            val p = mind + Offset(w * 0.11f * sin(k * 1.7f), -w * 0.12f - h * 0.28f * u)
            drawCircle(PopC38.Bubble, w * (0.02f + 0.015f * u), p, alpha = mf * (1f - u) * (1f - 0.7f * seg(sa, 0f, 0.5f)))
        }
    }
    // a doubt is one question: answered, and gone
    if (oq > 0f) {
        val f = easeOut(seg(oq, 0f, 0.3f)) * (1f - seg(oq, 0.75f, 1f))
        val c = Offset(w * 0.2f, h * 0.32f)
        drawCircle(Color(0xFF3A4A60), w * 0.05f, c, alpha = f)
        qmark21(c, h * 0.04f, Color(0xFFE0E0F0), f * (1f - seg(oq, 0.35f, 0.55f)))
        check21(c, w * 0.025f, PopC38.Answer, seg(oq, 0.4f, 0.6f), 3f * dp)
    }
    // answer one, two more pop up
    if (hy > 0f) {
        val f = easeOut(seg(hy, 0f, 0.25f)) * (1f - 0.6f * seg(sa, 0f, 0.4f))
        val gens = listOf(listOf(Offset(0.45f, 0.36f)), listOf(Offset(0.36f, 0.24f), Offset(0.55f, 0.22f)), listOf(Offset(0.28f, 0.13f), Offset(0.42f, 0.1f), Offset(0.52f, 0.11f), Offset(0.64f, 0.12f)))
        for ((g, list) in gens.withIndex()) {
            val born = seg(hy, 0.05f + 0.25f * g, 0.25f + 0.25f * g)
            val answered = if (g < 2) seg(hy, 0.2f + 0.25f * g, 0.3f + 0.25f * g) else 0f
            for (p in list) {
                val c = Offset(w * p.x, h * p.y)
                drawCircle(PopC38.Dark, w * 0.04f * easeOut(born), c, alpha = f)
                drawCircle(Color(0xFF8A7AA0), w * 0.04f * easeOut(born), c, alpha = f * born, style = Stroke(1.5f * dp))
                check21(c, w * 0.018f, PopC38.Answer, answered, 2.5f * dp)
            }
        }
        tagAt(a.tag("hydra", 0, 0), Offset(w * 0.28f, h * 0.05f), seg(hy, 0.6f, 0.8f) * (1f - seg(fu, 0f, 0.3f)), back = Back38, ink = Color(0xFFD8C8F0))
        tagAt(a.tag("hydra", 0, 1), Offset(w * 0.64f, h * 0.05f), seg(hy, 0.7f, 0.9f) * (1f - seg(fu, 0f, 0.3f)), back = Back38, ink = Color(0xFFD8C8F0))
    }
    // petrol on a fire
    if (fu > 0f) {
        val f = easeOut(seg(fu, 0f, 0.3f)) * (1f - 0.7f * seg(sa, 0f, 0.4f))
        val fire = Offset(w * 0.8f, h * 0.8f)
        val leap = 1f + 1.6f * easeOut(seg(fu, 0.5f, 0.8f))
        for (k in 0 until 5) {
            val fl = 0.8f + 0.2f * sin(t * 9f + k * 1.3f)
            val x = fire.x + w * 0.02f * (k - 2)
            drawPath(Path().apply {
                moveTo(x - w * 0.02f, fire.y); quadraticTo(x - w * 0.015f, fire.y - h * 0.06f * leap * fl, x, fire.y - h * 0.09f * leap * fl)
                quadraticTo(x + w * 0.015f, fire.y - h * 0.06f * leap * fl, x + w * 0.02f, fire.y); close()
            }, PopC38.Fire, alpha = f * 0.85f)
        }
        softGlow(fire + Offset(0f, -h * 0.05f * leap), w * 0.08f * leap, PopC38.Fire, 0.4f * f)
        val can = Offset(w * 0.9f, h * 0.55f)
        val tilt = easeInOut(seg(fu, 0.2f, 0.45f))
        drawRect(PopC38.Can, Offset(can.x - w * 0.03f, can.y - h * 0.05f), Size(w * 0.06f, h * 0.1f), alpha = f)
        val pour = seg(fu, 0.4f, 0.75f)
        if (pour > 0f) drawLine(Color(0xFFE8D27A), can + Offset(-w * 0.03f, -h * 0.04f), can + Offset(-w * 0.03f - w * 0.04f * tilt, h * 0.2f * pour), 2f * dp, alpha = f * 0.8f)
    }
    // the calm core: faith that hates these thoughts
    if (sa > 0f) {
        val f = easeOut(seg(sa, 0f, 0.5f))
        softGlow(mind, w * 0.16f * f, PopC38.Core, 0.6f * f)
        drawCircle(PopC38.Core, w * 0.04f * f, mind, alpha = f)
        bloom(mind, w * 0.03f * f, PopC38.Core, f)
    }
}

// ═════════════ 7. a road, a shout, a mirage (bs-intiha) ═════════════

private object RoadC38 {
    val Sky = Color(0xFFF2D8B0)
    val SkyHigh = Color(0xFF8AB0D0)
    val Sand = Color(0xFFD8B884)
    val Road = Color(0xFF9A8466)
    val Walker = Color(0xFF2E2620)
    val Shout = Color(0xFF5A3A4A)
    val Bush = Color(0xFF6A7A4A)
    val Mirage = Color(0xFFA8D0E8)
    val Shield = Color(0xFFFFF0C8)
}

/**
 * Beats: seekrefuge (a walker on the road; a soft dome of light settles round him), roadshout (at the roadside, jagged
 * shouts from behind a bush), morebubbles (he turns to answer, and the shouts multiply), ignorefade (he walks on without
 * looking; the shouts shrink and fade behind him), mirage (water shimmers ahead; he walks into it and there is nothing).
 */
internal fun DrawScope.madroadScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "seekrefuge" to Shot(Offset(w * 0.25f, h * 0.6f), 1.3f),
        "roadshout" to Shot(Offset(w * 0.38f, h * 0.55f), 1.25f),
        "morebubbles" to Shot(Offset(w * 0.4f, h * 0.5f), 1.15f),
        "ignorefade" to Shot(Offset(w * 0.55f, h * 0.6f), 1.1f),
        "mirage" to Shot(Offset(w * 0.78f, h * 0.65f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { madRoad(a) }
}

private fun DrawScope.madRoad(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sr = a.b("seekrefuge")
    val rs = a.b("roadshout")
    val mb = a.b("morebubbles")
    val ig = a.b("ignorefade")
    val mi = a.b("mirage")
    val ground = h * 0.62f
    drawRect(Brush.verticalGradient(listOf(RoadC38.SkyHigh, RoadC38.Sky), endY = ground), size = Size(w, ground))
    drawRect(RoadC38.Sand, Offset(0f, ground), Size(w, h - ground))
    drawPath(Path().apply { moveTo(0f, h * 0.86f); lineTo(w, h * 0.7f); lineTo(w, h * 0.76f); lineTo(0f, h * 0.96f); close() }, RoadC38.Road)
    // where the walker is: he walks on, stops to answer, walks on again
    val walk1 = easeInOut(seg(sr, 0f, 1f)) * 0.15f + easeInOut(seg(rs, 0f, 0.6f)) * 0.1f
    val walk2 = easeInOut(seg(ig, 0f, 1f)) * 0.3f + easeInOut(seg(mi, 0f, 0.7f)) * 0.25f
    val u = (0.12f + walk1 + walk2).coerceAtMost(0.95f)
    val foot = Offset(w * u, h * (0.91f - 0.2f * u))
    val moving = (sr in 0.01f..0.99f) || (ig in 0.01f..0.99f) || (mi in 0.01f..0.7f)
    val turned = bump(seg(mb, 0f, 1f))
    figure(foot, h * 0.2f, RoadC38.Walker, 1f, walk = if (moving) t * 6f else 0f, lean = 0.25f * turned, arms = 0.6f * turned)
    // refuge: a soft dome of light over him
    val dome = easeOut(seg(sr, 0.2f, 0.7f))
    if (dome > 0f) {
        val c = foot + Offset(0f, -h * 0.1f)
        drawCircle(RoadC38.Shield, h * 0.15f, c, alpha = 0.18f * dome)
        drawArc(RoadC38.Shield, 180f, 180f, false, Offset(c.x - h * 0.15f, c.y - h * 0.15f), Size(h * 0.3f, h * 0.3f), alpha = 0.6f * dome, style = Stroke(2f * dp))
    }
    // the shouts from the roadside
    val bush = Offset(w * 0.38f, h * 0.68f)
    drawCircle(RoadC38.Bush, w * 0.05f, bush)
    drawCircle(RoadC38.Bush, w * 0.04f, bush + Offset(w * 0.04f, h * 0.01f))
    val shoutOn = easeOut(seg(rs, 0.2f, 0.5f))
    val many = 2 + (4f * easeOut(seg(mb, 0.2f, 0.8f))).toInt()
    val fade = 1f - easeOut(seg(ig, 0.1f, 0.8f))
    if (shoutOn > 0f) {
        for (k in 0 until many) {
            val c = bush + Offset(w * (-0.06f + 0.05f * k), -h * (0.12f + 0.07f * (k % 3)))
            val s = w * (0.03f + 0.01f * (k % 2)) * (0.5f + 0.5f * fade) * (1f + 0.08f * sin(t * 8f + k))
            val p = Path()
            for (j in 0 until 10) {
                val an = j * PI.toFloat() / 5f
                val rr = if (j % 2 == 0) s else s * 0.55f
                val q = c + Offset(cos(an) * rr, sin(an) * rr)
                if (j == 0) p.moveTo(q.x, q.y) else p.lineTo(q.x, q.y)
            }
            p.close()
            drawPath(p, RoadC38.Shout, alpha = shoutOn * fade * 0.9f)
        }
    }
    // the mirage ahead
    if (mi > 0f) {
        val c = Offset(w * 0.82f, h * 0.74f)
        val shimmer = 0.6f + 0.2f * sin(t * 3f)
        val gone = 1f - easeOut(seg(mi, 0.55f, 0.85f))
        drawOval(RoadC38.Mirage, Offset(c.x - w * 0.12f, c.y - h * 0.025f), Size(w * 0.24f, h * 0.05f), alpha = easeOut(seg(mi, 0f, 0.3f)) * shimmer * gone)
        for (k in 0 until 3) drawLine(Color.White, Offset(c.x - w * 0.08f + k * w * 0.05f, c.y + h * 0.005f * sin(t * 4f + k)), Offset(c.x - w * 0.05f + k * w * 0.05f, c.y + h * 0.005f * sin(t * 4f + k)), 1.5f * dp, alpha = 0.6f * shimmer * gone * seg(mi, 0f, 0.3f))
    }
}
