package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 56 (2026-11-03): «حصن المسلم»، «الذكر والدعاء» (2). Two light words that weigh heavy; one blessing that comes back
 * ten; seventy and a hundred lights of asking forgiveness; hands raised, a brother's prayer and the nearest moment. Allah is
 * never drawn; the Prophet ﷺ is only light; angels are cool white lights; people are generic figures or plain roundels.
 */

private val Back56 = Color(0xE6181420)

// ═════════════ 1. light on the tongue, heavy in the scale (hisn-tasbih) ═════════════

private object HeavyC56 {
    val Night = Color(0xFF14182C)
    val NightLow = Color(0xFF2C2A4C)
    val Gold = Color(0xFFF0C860)
    val Beam = Color(0xFFB89A62)
    val Sea = Color(0xFF2A5A7A)
    val Foam = Color(0xFFE8F0F4)
    val Tile = Color(0xFF2E4A5A)
    val Chest = Color(0xFF7A5232)
    val Band = Color(0xFFD8A84A)
    val Dot = Color(0xFF9AD0A8)
}

/**
 * Beats: featherwords (two words drift down like feathers, «سُبْحَانَ اللَّهِ وَبِحَمْدِهِ» and «سُبْحَانَ اللَّهِ الْعَظِيمِ», and
 * land on a great scale whose pan sinks), seafoam (a sea full of foam; «100 مَرَّةٍ», and the foam is wiped away), fourwords
 * (four tiles, «سُبْحَانَ اللَّهِ», «وَالْحَمْدُ لِلَّهِ», «وَلَا إِلَهَ إِلَّا اللَّهُ», «وَاللَّهُ أَكْبَرُ», change places and stay
 * lit in any order), kanz (a chest opens on light: «لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ»), thousandgood (a hundred dots;
 * each grows ten more lights: «1000 حَسَنَةٍ»).
 */
internal fun DrawScope.lightheavyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "featherwords" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "seafoam" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "fourwords" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "kanz" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "thousandgood" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { heavyNight(a) }
}

private fun DrawScope.heavyNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tw = a.b("featherwords")
    val sf = a.b("seafoam")
    val fw = a.b("fourwords")
    val kz = a.b("kanz")
    val th = a.b("thousandgood")
    nightSky(t, HeavyC56.Night, HeavyC56.NightLow, h * 0.7f, starAlpha = 0.7f)
    if (tw > 0f) {
        val f = easeOut(seg(tw, 0f, 0.25f)) * (1f - seg(sf, 0f, 0.4f))
        val land = easeInOut(seg(tw, 0.15f, 0.6f))
        val tilt = mix(0f, 0.75f, easeInOut(seg(tw, 0.55f, 0.85f)))
        val foot = Offset(w * 0.5f, h * 0.92f)
        val s = h * 0.6f
        balance12(foot, s, tilt, HeavyC56.Beam, f)
        val (lp, rp) = balancePans12(foot, s, tilt)
        for (k in 0 until 2) {
            val sway = sin(t * 2f + k * 2f) * w * 0.05f * (1f - land)
            val c = Offset(lp.x + sway, mix(h * 0.08f + h * 0.1f * k, lp.y - h * (0.06f + 0.11f * k), land))
            softGlow(c, w * 0.05f, HeavyC56.Gold, 0.4f * f)
            tagAt(a.tag("featherwords", 0, k), c, f * seg(tw, 0.05f + 0.1f * k, 0.15f + 0.1f * k), back = Back56, ink = ArtInk.Gold)
        }
        drawCircle(HeavyC56.Beam, w * 0.012f, rp + Offset(0f, -h * 0.02f), alpha = f * 0.6f)
    }
    if (sf > 0f) {
        val f = easeOut(seg(sf, 0f, 0.25f)) * (1f - seg(fw, 0f, 0.4f))
        val top = h * 0.55f
        drawRect(Brush.verticalGradient(listOf(HeavyC56.Sea, HeavyC56.Night), top, h), Offset(0f, top), Size(w, h - top), alpha = f)
        val wipe = easeInOut(seg(sf, 0.35f, 0.9f))
        for (k in 0 until 60) {
            val x = w * fract(k * 0.137f + 0.05f)
            val y = top + (h - top) * fract(k * 0.291f) * 0.9f + sin(t * 1.5f + k) * h * 0.005f
            val gone = if (x > w * (1f - wipe)) 1f else 0f
            drawOval(HeavyC56.Foam, Offset(x - w * 0.02f, y - h * 0.006f), Size(w * 0.04f, h * 0.012f), alpha = f * 0.8f * (1f - gone))
        }
        if (wipe > 0f) drawRect(Brush.horizontalGradient(listOf(Color.Transparent, HeavyC56.Gold.copy(alpha = 0.25f)), w * (1f - wipe) - w * 0.1f, w * (1f - wipe)), Offset(w * (1f - wipe) - w * 0.1f, top), Size(w * 0.1f, h - top), alpha = f * (1f - seg(sf, 0.9f, 1f)))
        tagAt(a.tag("seafoam"), Offset(w * 0.5f, h * 0.3f), f * seg(sf, 0.15f, 0.35f), back = Back56, ink = ArtInk.Gold)
    }
    if (fw > 0f) {
        val f = easeOut(seg(fw, 0f, 0.25f)) * (1f - seg(kz, 0f, 0.4f))
        val sw = easeInOut(seg(fw, 0.55f, 0.85f))
        val slots = listOf(Offset(w * 0.3f, h * 0.35f), Offset(w * 0.7f, h * 0.35f), Offset(w * 0.3f, h * 0.65f), Offset(w * 0.7f, h * 0.65f))
        val order = listOf(3, 2, 0, 1)
        for (k in 0 until 4) {
            val c = slots[k] + (slots[order[k]] - slots[k]) * sw
            val g = seg(fw, 0.08f + 0.1f * k, 0.18f + 0.1f * k)
            drawRoundRect(HeavyC56.Tile, Offset(c.x - w * 0.17f, c.y - h * 0.08f), Size(w * 0.34f, h * 0.16f), CornerRadius(10f * dp), alpha = f * g)
            softGlow(c, w * 0.12f, HeavyC56.Gold, 0.25f * f * g)
            tagAt(a.tag("fourwords", 0, k), c, f * g, back = Color.Transparent, ink = ArtInk.Gold)
        }
    }
    if (kz > 0f) {
        val f = easeOut(seg(kz, 0f, 0.25f)) * (1f - seg(th, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.7f)
        val open = easeInOut(seg(kz, 0.2f, 0.5f))
        drawRoundRect(HeavyC56.Chest, Offset(c.x - w * 0.16f, c.y - h * 0.08f), Size(w * 0.32f, h * 0.16f), CornerRadius(5f * dp), alpha = f)
        drawLine(HeavyC56.Band, Offset(c.x - w * 0.16f, c.y), Offset(c.x + w * 0.16f, c.y), 3f * dp, alpha = f)
        if (open > 0f) lightShafts(Offset(c.x, c.y - h * 0.08f), -PI.toFloat() / 2f, 0.7f, h * 0.45f, HeavyC56.Gold, 0.6f * f * open, t)
        val lid = Path().apply {
            val hinge = Offset(c.x - w * 0.16f, c.y - h * 0.08f)
            val lift = h * 0.1f * open
            moveTo(hinge.x, hinge.y)
            lineTo(hinge.x + w * 0.32f * (1f - 0.5f * open), hinge.y - lift)
            lineTo(hinge.x + w * 0.32f * (1f - 0.5f * open), hinge.y - lift - h * 0.05f)
            lineTo(hinge.x, hinge.y - h * 0.05f)
            close()
        }
        drawPath(lid, HeavyC56.Chest, alpha = f)
        tagAt(a.tag("kanz"), Offset(w * 0.5f, h * 0.25f), f * seg(kz, 0.45f, 0.65f), back = Back56, ink = ArtInk.Gold)
    }
    if (th > 0f) {
        val f = easeOut(seg(th, 0f, 0.25f))
        val grow = seg(th, 0.3f, 0.85f)
        for (k in 0 until 100) {
            val c = Offset(w * (0.1f + 0.08f * (k % 10) + 0.04f), h * (0.3f + 0.06f * (k / 10)))
            drawCircle(HeavyC56.Dot, w * 0.006f, c, alpha = f * seg(th, 0.02f + 0.002f * k, 0.1f + 0.002f * k))
            val g = seg(grow, k / 130f, k / 130f + 0.25f)
            if (g > 0f) for (j in 0 until 10) {
                val ang = j * PI.toFloat() / 5f
                drawCircle(HeavyC56.Gold, w * 0.0025f, c + Offset(cos(ang), sin(ang)) * (w * 0.016f * g), alpha = f * g)
            }
        }
        tagAt(a.tag("thousandgood"), Offset(w * 0.5f, h * 0.16f), f * seg(th, 0.5f, 0.7f), back = Back56, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. one comes back ten (hisn-salat-nabi) ═════════════

private object TenC56 {
    val Night = Color(0xFF101828)
    val NightLow = Color(0xFF243250)
    val Ground = Color(0xFF1A2232)
    val Gold = Color(0xFFF2CC6A)
    val Lamp = Color(0xFFB8925A)
    val Dull = Color(0xFF4A4A54)
    val Land = Color(0xFF2E3E58)
    val Page = Color(0xFFF2E8D2)
    val Rule = Color(0xFFB8A888)
}

/**
 * Beats: tenback (one small light rises from below; ten come down in its place), misertag (lamps in a row light up one by
 * one; one stays dark with a word beside it: «الْبَخِيلُ»), fromeverywhere (small lights rise from every place across the
 * land and travel to the light ﷺ), nearestmost (plain roundels in a line; the more lights a roundel carries, the nearer it
 * comes to the light ﷺ), abrahamic (an open book with its name over it: «الصَّلَاةَ الْإِبْرَاهِيمِيَّةَ»).
 */
internal fun DrawScope.tenfoldScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "tenback" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "misertag" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "fromeverywhere" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "nearestmost" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "abrahamic" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { tenNight(a) }
}

private fun DrawScope.tenNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tb = a.b("tenback")
    val ms = a.b("misertag")
    val fe = a.b("fromeverywhere")
    val nm = a.b("nearestmost")
    val ab = a.b("abrahamic")
    val ground = h * 0.86f
    nightSky(t, TenC56.Night, TenC56.NightLow, ground, starAlpha = 0.8f)
    drawRect(TenC56.Ground, Offset(0f, ground), Size(w, h - ground))
    if (tb > 0f) {
        val f = easeOut(seg(tb, 0f, 0.25f)) * (1f - seg(ms, 0f, 0.4f))
        val up = easeInOut(seg(tb, 0.05f, 0.4f))
        softGlow(Offset(w * 0.5f, mix(ground - h * 0.05f, h * 0.15f, up)), w * 0.025f, TenC56.Gold, f * (1f - seg(tb, 0.38f, 0.45f)))
        for (k in 0 until 10) {
            val d = easeInOut(seg(tb, 0.42f + 0.03f * k, 0.75f + 0.02f * k))
            val x = w * (0.14f + 0.08f * k)
            val c = Offset(mix(w * 0.5f, x, d), mix(h * 0.15f, ground - h * 0.1f - h * 0.05f * (k % 2), d))
            softGlow(c, w * 0.022f, TenC56.Gold, 0.9f * f * seg(tb, 0.42f + 0.03f * k, 0.5f + 0.03f * k))
        }
    }
    if (ms > 0f) {
        val f = easeOut(seg(ms, 0f, 0.25f)) * (1f - seg(fe, 0f, 0.4f))
        for (k in 0 until 7) {
            val c = Offset(w * (0.1f + 0.13f * k), ground - h * 0.12f)
            val dark = k == 4
            drawLine(TenC56.Lamp, c + Offset(0f, h * 0.05f), Offset(c.x, ground), 2f * dp, alpha = f)
            drawOval(if (dark) TenC56.Dull else TenC56.Lamp, Offset(c.x - w * 0.025f, c.y - h * 0.02f), Size(w * 0.05f, h * 0.07f), alpha = f)
            if (!dark) softGlow(c, w * 0.04f, TenC56.Gold, 0.8f * f * seg(ms, 0.1f + 0.08f * k, 0.2f + 0.08f * k))
        }
        tagAt(a.tag("misertag"), Offset(w * 0.62f, ground - h * 0.35f), f * seg(ms, 0.6f, 0.75f), back = Back56, ink = Color(0xFFE0A0A0))
    }
    if (fe > 0f) {
        val f = easeOut(seg(fe, 0f, 0.25f)) * (1f - seg(nm, 0f, 0.4f))
        val light = Offset(w * 0.5f, h * 0.22f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        val land = Path().apply {
            moveTo(0f, ground); quadraticTo(w * 0.2f, ground - h * 0.1f, w * 0.4f, ground - h * 0.03f)
            quadraticTo(w * 0.65f, ground - h * 0.14f, w, ground - h * 0.04f); lineTo(w, h); lineTo(0f, h); close()
        }
        drawPath(land, TenC56.Land, alpha = f)
        for (k in 0 until 14) {
            val from = Offset(w * (0.04f + 0.07f * k), ground - h * (0.04f + 0.04f * ((k * 5) % 3)))
            val u = fract(t * 0.25f + k * 0.13f) * seg(fe, 0.1f, 0.3f)
            val p = from + (light - from) * easeInOut(u)
            softGlow(p, w * 0.012f, TenC56.Gold, 0.9f * f * sin(PI.toFloat() * u))
            drawCircle(TenC56.Gold, w * 0.004f, from, alpha = f)
        }
    }
    if (nm > 0f) {
        val f = easeOut(seg(nm, 0f, 0.25f)) * (1f - seg(ab, 0f, 0.4f))
        val light = Offset(w * 0.5f, h * 0.2f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        val sort = easeInOut(seg(nm, 0.35f, 0.75f))
        val counts = listOf(1, 5, 2, 7, 3, 6)
        for (k in 0 until 6) {
            val rank = counts.sortedDescending().indexOf(counts[k])
            val x0 = w * (0.12f + 0.15f * k)
            val y0 = ground - h * 0.08f
            val c = Offset(mix(x0, w * 0.5f + (if (rank % 2 == 0) -1f else 1f) * w * 0.07f * (rank / 2 + 1) * 0.9f, sort), mix(y0, h * 0.42f + h * 0.12f * rank, sort))
            nameRoundel(c, w * 0.03f, null, Roundel.PlainFill, Roundel.PlainRim, f)
            for (j in 0 until counts[k]) {
                val ang = j * 2f * PI.toFloat() / counts[k] + t * 0.5f
                drawCircle(TenC56.Gold, w * 0.004f, c + Offset(cos(ang), sin(ang)) * (w * 0.045f), alpha = f * seg(nm, 0.1f + 0.02f * j, 0.25f + 0.02f * j))
            }
        }
    }
    if (ab > 0f) {
        val f = easeOut(seg(ab, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.65f)
        openBook21(c, w * 0.5f, TenC56.Page, TenC56.Rule, f, seg(ab, 0.3f, 0.6f))
        softGlow(c + Offset(0f, -h * 0.1f), w * 0.2f, TenC56.Gold, 0.3f * f * seg(ab, 0.3f, 0.6f))
        fitBig26(a.big("abrahamic"), Offset(w * 0.5f, h * 0.3f), w * 0.8f, TenC56.Gold, f * seg(ab, 0.2f, 0.45f))
    }
}

// ═════════════ 3. seventy and a hundred (hisn-istighfar) ═════════════

private object ForgiveC56 {
    val Night = Color(0xFF0E1426)
    val NightLow = Color(0xFF283456)
    val Ground = Color(0xFF1C2030)
    val Gold = Color(0xFFF0C860)
    val Person = Color(0xFFDCD4C4)
    val Water = Color(0xFF8AC4EA)
    val Smudge = Color(0xFF3A3040)
    val Third = Color(0xFF3A4670)
}

/**
 * Beats: seventyplus (a ring of seventy small lights fills, and more after it: «أَكْثَرَ مِنْ 70 مَرَّةً»), turnall (plain
 * roundels turn toward the light one after another: «تُوبُوا إِلَى اللَّهِ وَاسْتَغْفِرُوهُ»), washpray (a figure washes; prays
 * two rakʿahs; a dark smudge lifts away from him), tallyhundred (ten rows of ten marks fill in a sitting: «رَبِّ اغْفِرْ لِي
 * وَتُبْ عَلَيَّ»), lastthird (the night in three bands; the last band glows and a figure prostrates in it).
 */
internal fun DrawScope.seventyhundredScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "seventyplus" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "turnall" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "washpray" to Shot(Offset(w * 0.5f, h * 0.65f), 1.15f),
        "tallyhundred" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "lastthird" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { forgiveNight(a) }
}

private fun DrawScope.forgiveNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sp = a.b("seventyplus")
    val ta = a.b("turnall")
    val wp = a.b("washpray")
    val th = a.b("tallyhundred")
    val lt = a.b("lastthird")
    val ground = h * 0.84f
    nightSky(t, ForgiveC56.Night, ForgiveC56.NightLow, ground, starAlpha = 0.9f)
    drawRect(ForgiveC56.Ground, Offset(0f, ground), Size(w, h - ground))
    if (sp > 0f) {
        val f = easeOut(seg(sp, 0f, 0.25f)) * (1f - seg(ta, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.5f)
        val fill = seg(sp, 0.1f, 0.75f)
        for (k in 0 until 70) {
            val ang = -PI.toFloat() / 2f + k * 2f * PI.toFloat() / 70f
            val on = if (k / 70f < fill) 1f else 0f
            drawCircle(ForgiveC56.Gold, w * 0.006f, c + Offset(cos(ang) * w * 0.3f, sin(ang) * w * 0.3f), alpha = f * (0.2f + 0.8f * on))
        }
        val more = seg(sp, 0.75f, 0.95f)
        for (k in 0 until 8) {
            val ang = -PI.toFloat() / 2f + k * 2f * PI.toFloat() / 8f + t * 0.4f
            softGlow(c + Offset(cos(ang) * w * 0.38f, sin(ang) * w * 0.38f), w * 0.015f, ForgiveC56.Gold, f * more)
        }
        tagAt(a.tag("seventyplus"), c, f * seg(sp, 0.2f, 0.4f), back = Back56, ink = ArtInk.Gold)
    }
    if (ta > 0f) {
        val f = easeOut(seg(ta, 0f, 0.25f)) * (1f - seg(wp, 0f, 0.4f))
        val light = Offset(w * 0.5f, h * 0.18f)
        softGlow(light, w * 0.08f, ForgiveC56.Gold, 0.7f * f)
        for (k in 0 until 9) {
            val c = Offset(w * (0.1f + 0.1f * k), ground - h * 0.08f - h * 0.08f * (k % 2))
            val turned = seg(ta, 0.15f + 0.06f * k, 0.3f + 0.06f * k)
            nameRoundel(c, w * 0.03f, null, Roundel.PlainFill, Roundel.PlainRim, f)
            val dir = (light - c)
            val n = dir / dir.getDistance()
            drawLine(ForgiveC56.Gold, c + n * (w * 0.035f), c + n * (w * (0.035f + 0.05f * turned)), 2f * dp, StrokeCap.Round, alpha = f * turned)
        }
        tagAt(a.tag("turnall"), Offset(w * 0.5f, h * 0.38f), f * seg(ta, 0.2f, 0.4f), back = Back56, ink = ArtInk.Gold)
    }
    if (wp > 0f) {
        val f = easeOut(seg(wp, 0f, 0.25f)) * (1f - seg(th, 0f, 0.4f))
        val foot = Offset(w * 0.5f, ground)
        val wash = 1f - seg(wp, 0.3f, 0.36f)
        figure(foot, h * 0.34f, ForgiveC56.Person, f * wash, arms = 0.4f)
        if (wash > 0f) for (k in 0 until 6) {
            val u = fract(t * 1.4f + k / 6f)
            drawCircle(ForgiveC56.Water, w * 0.005f, foot + Offset(w * 0.04f, -h * 0.2f + h * 0.2f * u), alpha = f * wash * (1f - u))
        }
        val pr = seg(wp, 0.34f, 0.9f)
        if (pr > 0f) {
            val cyc = fract(pr * 2f)
            val deep = if (cyc < 0.3f) 0f else if (cyc < 0.5f) 0.5f else if (cyc < 0.85f) 1f else 0f
            if (deep == 0f) pray12(foot, h * 0.34f, ForgiveC56.Person, f * (1f - wash))
            else bow12(foot, h * 0.34f, ForgiveC56.Person, f * (1f - wash), deep)
        }
        val lift = easeInOut(seg(wp, 0.75f, 1f))
        val sm = foot + Offset(0f, -h * 0.25f - h * 0.4f * lift)
        drawCircle(ForgiveC56.Smudge, w * 0.05f * (1f - 0.6f * lift), sm, alpha = f * 0.8f * (1f - lift))
        softGlow(foot + Offset(0f, -h * 0.2f), w * 0.1f, ForgiveC56.Gold, 0.5f * f * lift)
    }
    if (th > 0f) {
        val f = easeOut(seg(th, 0f, 0.25f)) * (1f - seg(lt, 0f, 0.4f))
        val fill = seg(th, 0.1f, 0.85f)
        for (k in 0 until 100) {
            val col = k % 10
            val row = k / 10
            val x = w * (0.2f + 0.065f * col)
            val y = h * (0.32f + 0.055f * row)
            val on = seg(fill, k / 100f, k / 100f + 0.02f)
            drawLine(ForgiveC56.Gold, Offset(x, y - h * 0.018f), Offset(x, y + h * 0.018f), 2f * dp, StrokeCap.Round, alpha = f * (0.15f + 0.85f * on))
        }
        tagAt(a.tag("tallyhundred"), Offset(w * 0.5f, h * 0.18f), f * seg(th, 0.2f, 0.4f), back = Back56, ink = ArtInk.Gold)
    }
    if (lt > 0f) {
        val f = easeOut(seg(lt, 0f, 0.25f))
        val band = ground / 3f
        for (k in 0 until 3) {
            val last = k == 2
            drawRect(if (last) ForgiveC56.Third else ForgiveC56.Night, Offset(0f, band * k), Size(w, band), alpha = f * (if (last) 0.4f + 0.4f * seg(lt, 0.2f, 0.5f) else 0.35f))
            if (k > 0) drawLine(ForgiveC56.Gold, Offset(0f, band * k), Offset(w, band * k), 1f * dp, alpha = f * 0.4f)
        }
        softGlow(Offset(w * 0.5f, band * 2.5f), w * 0.3f, ForgiveC56.Gold, 0.25f * f * seg(lt, 0.3f, 0.6f))
        bow12(Offset(w * 0.55f, ground), h * 0.3f, ForgiveC56.Person, f * seg(lt, 0.3f, 0.5f), 1f)
    }
}

// ═════════════ 4. hands raised (hisn-dua) ═════════════

private object HandsC56 {
    val Sky = Color(0xFF16203A)
    val SkyLow = Color(0xFF3C3A62)
    val Ground = Color(0xFF221E30)
    val Gold = Color(0xFFF2C860)
    val Person = Color(0xFFE2DACA)
    val Seed = Color(0xFF8A6A44)
    val Sprout = Color(0xFF6AAA6A)
    val Wall = Color(0xFF4A4458)
    val Fire = Color(0xFFD0603A)
    val Shield = Color(0xFFBFE6FF)
}

/**
 * Beats: duaworship (raised hands and a beam of light going up from them: «الدُّعَاءُ هُوَ الْعِبَادَةُ»), nohaste (a seed is
 * watered with asking; the sprout is about to break the soil when the asker turns away: «مَا لَمْ يَعْجَلْ»), forbrother (one
 * prays for a brother far beyond a wall; a cool white light answers over him and the same light comes back: «وَلَكَ بِمِثْلٍ»),
 * twogoods (two lights, «حَسَنَةِ الدُّنْيَا» and «وَحَسَنَةِ الْآخِرَةِ», over a shield that keeps fire below), nearestsujud
 * (a figure in prostration with the light very near).
 */
internal fun DrawScope.raisedhandsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "duaworship" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "nohaste" to Shot(Offset(w * 0.5f, h * 0.65f), 1.15f),
        "forbrother" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "twogoods" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "nearestsujud" to Shot(Offset(w * 0.5f, h * 0.7f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { handsNight(a) }
}

private fun DrawScope.handsNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val dw = a.b("duaworship")
    val nh = a.b("nohaste")
    val fb = a.b("forbrother")
    val tg = a.b("twogoods")
    val ns = a.b("nearestsujud")
    val ground = h * 0.84f
    nightSky(t, HandsC56.Sky, HandsC56.SkyLow, ground, starAlpha = 0.85f)
    drawRect(HandsC56.Ground, Offset(0f, ground), Size(w, h - ground))
    if (dw > 0f) {
        val f = easeOut(seg(dw, 0f, 0.25f)) * (1f - seg(nh, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.62f)
        hands12(c, h * 0.2f, HandsC56.Person, f)
        lightShafts(c + Offset(0f, -h * 0.08f), -PI.toFloat() / 2f, 0.25f, h * 0.55f, HandsC56.Gold, 0.6f * f * seg(dw, 0.2f, 0.5f), t, rays = 5)
        tagAt(a.tag("duaworship"), Offset(w * 0.5f, h * 0.2f), f * seg(dw, 0.3f, 0.5f), back = Back56, ink = ArtInk.Gold)
    }
    if (nh > 0f) {
        val f = easeOut(seg(nh, 0f, 0.25f)) * (1f - seg(fb, 0f, 0.4f))
        val seed = Offset(w * 0.62f, ground + h * 0.05f)
        drawOval(HandsC56.Seed, Offset(seed.x - w * 0.015f, seed.y - h * 0.012f), Size(w * 0.03f, h * 0.024f), alpha = f)
        val grow = seg(nh, 0.1f, 0.6f)
        drawLine(HandsC56.Sprout, seed, seed + Offset(0f, -h * 0.045f * grow), 2.5f * dp, StrokeCap.Round, alpha = f)
        val turn = easeInOut(seg(nh, 0.6f, 0.8f))
        val foot = Offset(w * (0.38f - 0.2f * turn), ground)
        if (turn < 0.05f) sit12(foot, h * 0.3f, HandsC56.Person, f, 1f)
        else figure(foot, h * 0.32f, HandsC56.Person, f, walk = t * 5f * turn, lean = -0.1f)
        if (turn < 0.05f) hands12(foot + Offset(w * 0.02f, -h * 0.3f), h * 0.06f, HandsC56.Person, f)
        for (k in 0 until 4) softGlow(seed + Offset(0f, -h * (0.08f + 0.06f * k)), w * 0.012f, HandsC56.Gold, 0.6f * f * (1f - turn) * seg(nh, 0.1f + 0.1f * k, 0.2f + 0.1f * k))
        tagAt(a.tag("nohaste"), Offset(w * 0.5f, h * 0.25f), f * seg(nh, 0.65f, 0.8f), back = Back56, ink = ArtInk.Gold)
    }
    if (fb > 0f) {
        val f = easeOut(seg(fb, 0f, 0.25f)) * (1f - seg(tg, 0f, 0.4f))
        drawRect(HandsC56.Wall, Offset(w * 0.48f, ground - h * 0.35f), Size(w * 0.04f, h * 0.35f), alpha = f)
        val me = Offset(w * 0.25f, ground)
        sit12(me, h * 0.3f, HandsC56.Person, f, 1f)
        hands12(me + Offset(w * 0.03f, -h * 0.3f), h * 0.06f, HandsC56.Person, f)
        figure(Offset(w * 0.78f, ground), h * 0.32f, HandsC56.Person, f)
        val go = seg(fb, 0.1f, 0.4f)
        val arc = { u: Float -> Offset(mix(w * 0.27f, w * 0.78f, u), ground - h * 0.4f - sin(PI.toFloat() * u) * h * 0.2f) }
        softGlow(arc(go), w * 0.02f, HandsC56.Gold, f * (1f - seg(fb, 0.4f, 0.45f)))
        guardDome12(Offset(w * 0.78f, ground), w * 0.12f, seg(fb, 0.35f, 0.55f), t, f)
        angelLight26(Offset(w * 0.5f, h * 0.18f), w * 0.009f, t, f * seg(fb, 0.4f, 0.55f))
        val back = seg(fb, 0.6f, 0.85f)
        softGlow(arc(1f - back) + Offset(0f, -h * 0.02f), w * 0.02f, HandsC56.Shield, f * back * (1f - seg(fb, 0.85f, 0.9f)))
        guardDome12(me, w * 0.12f, seg(fb, 0.8f, 0.95f), t, f)
        tagAt(a.tag("forbrother"), Offset(w * 0.5f, h * 0.32f), f * seg(fb, 0.45f, 0.6f), back = Back56, ink = ArtInk.Gold)
    }
    if (tg > 0f) {
        val f = easeOut(seg(tg, 0f, 0.25f)) * (1f - seg(ns, 0f, 0.4f))
        for (k in 0 until 2) {
            val c = Offset(w * (0.3f + 0.4f * k), h * 0.3f)
            softGlow(c, w * 0.08f, HandsC56.Gold, 0.7f * f * seg(tg, 0.1f + 0.25f * k, 0.3f + 0.25f * k))
            tagAt(a.tag("twogoods", 0, k), c + Offset(0f, h * 0.1f), f * seg(tg, 0.15f + 0.25f * k, 0.3f + 0.25f * k), back = Back56, ink = ArtInk.Gold)
        }
        val sh = seg(tg, 0.6f, 0.8f)
        val y = h * 0.66f
        drawArc(HandsC56.Shield, 200f, 140f, false, Offset(w * 0.2f, y - h * 0.05f), Size(w * 0.6f, h * 0.3f), alpha = f * sh, style = Stroke(5f * dp, cap = StrokeCap.Round))
        for (k in 0 until 7) {
            val x = w * (0.18f + 0.11f * k)
            val fl = 0.6f + 0.4f * sin(t * 8f + k)
            drawOval(HandsC56.Fire, Offset(x - w * 0.02f, ground + h * 0.04f - h * 0.08f * fl), Size(w * 0.04f, h * 0.08f * fl), alpha = f * 0.8f)
        }
    }
    if (ns > 0f) {
        val f = easeOut(seg(ns, 0f, 0.25f))
        val foot = Offset(w * 0.6f, ground)
        val down = easeInOut(seg(ns, 0.1f, 0.45f))
        bow12(foot, h * 0.34f, HandsC56.Person, f, mix(0f, 1f, down))
        val near = easeInOut(seg(ns, 0.4f, 0.8f))
        lightShafts(Offset(w * 0.5f, 0f), PI.toFloat() / 2f, 0.3f, ground * (0.4f + 0.55f * near), HandsC56.Gold, 0.5f * f * near, t, rays = 5)
        for (k in 0 until 5) {
            val u = fract(t * 0.35f + k / 5f)
            softGlow(Offset(w * (0.48f + 0.02f * sin(k * 2f)), ground - h * 0.08f - (ground - h * 0.1f) * u), w * 0.012f, HandsC56.Gold, f * near * sin(PI.toFloat() * u))
        }
    }
}
