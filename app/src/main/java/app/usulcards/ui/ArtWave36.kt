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
 * Wave 36 (2026-10-27): «بصائر»، الجزء الثاني، «أعظم برهان». The Book and a dark globe that lights up house by house;
 * a man of Quraysh who came with offers and went back changed; the challenge no one met; a life recorded to its last
 * detail; and a valley of stones that dawn reached.
 */

private val Back36 = Color(0xE6181420)

// ═════════════ 1. one light on a dark globe, then every house (bs-ayah-kubra) ═════════════

private object GlobeC36 {
    val Space = Color(0xFF0A0C1C)
    val SpaceLow = Color(0xFF1C1A34)
    val Sea = Color(0xFF14243E)
    val Land = Color(0xFF243A2E)
    val Light = Color(0xFFFFD890)
    val Page = Color(0xFFF6EED8)
    val Mud = Color(0xFFB08A5A)
    val Tent = Color(0xFF6A4A3A)
}

/**
 * Beats: openquran (an open Book, lit, its light rising), onelight (a dark globe; on it one small light — the few who
 * prayed with him that night), everyhouse (lights spread over the globe, house after house; a house of clay and a
 * house of hair light up: «بَيْتُ مَدَرٍ», «بَيْتُ وَبَرٍ»), daily (under the Book, a row of days, each with its
 * portion read).
 */
internal fun DrawScope.nightglobeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "openquran" to Shot(Offset(w * 0.25f, h * 0.55f), 1.35f),
        "onelight" to Shot(Offset(w * 0.64f, h * 0.48f), 1.45f),
        "everyhouse" to Shot(Offset(w * 0.62f, h * 0.5f), 1.1f),
        "daily" to Shot(Offset(w * 0.25f, h * 0.7f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { globeNight(a) }
}

private fun DrawScope.globeNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val oq = a.b("openquran")
    val ol = a.b("onelight")
    val eh = a.b("everyhouse")
    val dl = a.b("daily")
    drawRect(Brush.verticalGradient(listOf(GlobeC36.Space, GlobeC36.SpaceLow)), size = Size(w, h))
    for (k in 0 until 50) drawCircle(Color.White, w * 0.0018f, Offset(w * w8Rnd(k, 4.4f), h * w8Rnd(k, 6.1f)), alpha = 0.3f + 0.3f * sin(t * 1.3f + k))
    // the Book
    val book = Offset(w * 0.25f, h * 0.55f)
    val bf = easeOut(seg(oq, 0f, 0.4f))
    if (bf > 0f) {
        lightShafts(book, -PI.toFloat() / 2f, 0.7f, h * 0.4f, GlobeC36.Light, 0.5f * bf, t, rays = 7)
        openBook21(book, w * 0.2f, GlobeC36.Page, Color(0xFF8A6A3A), bf, glow = bf)
    }
    // the globe
    val g = Offset(w * 0.64f, h * 0.48f)
    val r = h * 0.3f
    val gf = easeOut(seg(ol, 0f, 0.3f)).coerceAtLeast(easeOut(seg(eh, 0f, 0.3f)))
    if (gf > 0f) {
        drawCircle(GlobeC36.Sea, r, g, alpha = gf)
        val spin = t * 0.05f
        for (k in 0 until 6) {
            val ang = spin + k * 1.05f
            val x = g.x + sin(ang) * r * 0.7f
            if (cos(ang) < 0f) continue
            drawOval(GlobeC36.Land, Offset(x - r * 0.18f * cos(ang), g.y - r * (0.45f - 0.15f * (k % 3))), Size(r * 0.36f * cos(ang), r * (0.35f + 0.1f * (k % 2))), alpha = gf)
        }
        drawCircle(Color(0xFF6A8AC8), r, g, alpha = 0.4f * gf, style = Stroke(1.5f * dp))
        // one small light
        val one = g + Offset(-r * 0.1f, -r * 0.05f)
        val of = easeOut(seg(ol, 0.3f, 0.6f))
        drawCircle(GlobeC36.Light, w * 0.006f, one, alpha = of)
        softGlow(one, w * 0.03f, GlobeC36.Light, 0.8f * of)
        // then house after house
        val n = (70 * easeOut(seg(eh, 0.1f, 0.85f))).toInt()
        for (k in 0 until n) {
            val ang = 2f * PI.toFloat() * w8Rnd(k, 3.9f)
            val rr = r * 0.92f * kotlin.math.sqrt(w8Rnd(k, 7.3f))
            val p = g + Offset(cos(ang) * rr, sin(ang) * rr)
            drawCircle(GlobeC36.Light, w * 0.003f, p, alpha = 0.6f + 0.4f * sin(t * 2f + k))
        }
        if (n > 0) softGlow(g, r, GlobeC36.Light, 0.15f * seg(eh, 0.3f, 1f))
    }
    // a house of clay and a house of hair
    if (eh > 0f) {
        val f1 = easeOut(seg(eh, 0.3f, 0.6f))
        val mud = Offset(w * 0.86f, h * 0.84f)
        drawRect(GlobeC36.Mud, Offset(mud.x - w * 0.035f, mud.y - h * 0.08f), Size(w * 0.07f, h * 0.08f), alpha = f1)
        drawRect(GlobeC36.Light, Offset(mud.x - w * 0.008f, mud.y - h * 0.05f), Size(w * 0.016f, h * 0.025f), alpha = f1)
        val f2 = easeOut(seg(eh, 0.45f, 0.75f))
        val tent = Offset(w * 0.42f, h * 0.86f)
        drawPath(Path().apply { moveTo(tent.x - w * 0.05f, tent.y); lineTo(tent.x, tent.y - h * 0.09f); lineTo(tent.x + w * 0.05f, tent.y); close() }, GlobeC36.Tent, alpha = f2)
        drawPath(Path().apply { moveTo(tent.x - w * 0.01f, tent.y); lineTo(tent.x, tent.y - h * 0.04f); lineTo(tent.x + w * 0.01f, tent.y); close() }, GlobeC36.Light, alpha = f2)
        tagAt(a.tag("everyhouse", 0, 0), mud + Offset(0f, -h * 0.15f), seg(eh, 0.5f, 0.75f), back = Back36, ink = ArtInk.Gold)
        tagAt(a.tag("everyhouse", 0, 1), tent + Offset(0f, -h * 0.16f), seg(eh, 0.6f, 0.85f), back = Back36, ink = ArtInk.Gold)
    }
    // a portion every day
    if (dl > 0f) {
        for (k in 0 until 7) {
            val c = Offset(w * (0.09f + 0.053f * k), h * 0.78f)
            val f = easeOut(seg(dl, k * 0.1f, 0.2f + k * 0.1f))
            drawRoundRect(Color(0xFF2E2A44), Offset(c.x - w * 0.022f, c.y - h * 0.035f), Size(w * 0.044f, h * 0.07f), CornerRadius(3f * dp), alpha = f)
            check21(c, w * 0.01f, Color(0xFF2E9A55), seg(dl, 0.1f + k * 0.1f, 0.25f + k * 0.1f), 2f * dp)
        }
    }
}

// ═════════════ 2. he came with offers and went back changed (bs-utba) ═════════════

private object UtbaC36 {
    val Sky = Color(0xFF2A2448)
    val SkyLow = Color(0xFFB0705A)
    val Ground = Color(0xFF6A5040)
    val Man = Color(0xFF1E1814)
    val Coin = Color(0xFFE8BE4A)
    val Crown = Color(0xFFF2CC5A)
    val Wave = Color(0xFFFFF0C8)
    val Heart = Color(0xFFE86A6A)
}

/**
 * Beats: offers (evening in Mecca: a man of Quraysh before his light, gold and a crown set down between them —
 * «عُتْبَةُ», «الْمَالُ», «الْمُلْكُ»), fussilat (recitation reaches him in waves of light; the offers fade; he leans
 * back), returns (he walks back to his people, changed, and says: «مَا هُوَ بِالشِّعْرِ وَلَا بِالسِّحْرِ»), heartrise (a
 * heart lifts on wings: a heart that nearly flew).
 */
internal fun DrawScope.listenerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "offers" to Shot(Offset(w * 0.38f, h * 0.62f), 1.35f),
        "fussilat" to Shot(Offset(w * 0.35f, h * 0.55f), 1.25f),
        "returns" to Shot(Offset(w * 0.68f, h * 0.6f), 1.25f),
        "heartrise" to Shot(Offset(w * 0.5f, h * 0.35f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { listenerEvening(a) }
}

private fun DrawScope.listenerEvening(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val of = a.b("offers")
    val fs = a.b("fussilat")
    val rt = a.b("returns")
    val hr = a.b("heartrise")
    val ground = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(UtbaC36.Sky, UtbaC36.SkyLow), endY = ground), size = Size(w, ground))
    farHills(Color(0xFF5A4048), UtbaC36.SkyLow, h * 0.72f, h * 0.06f, 1.3f)
    drawRect(UtbaC36.Ground, Offset(0f, ground), Size(w, h - ground))
    val light = Offset(w * 0.2f, h * 0.55f)
    sirajMunir(light, w * 0.012f, easeOut(seg(of, 0f, 0.3f)), t, nameAt = light + Offset(0f, -h * 0.14f))
    // the man of Quraysh: before the light, then walking back
    val walk = easeInOut(seg(rt, 0.1f, 0.8f))
    val manX = mix(w * 0.42f, w * 0.68f, walk)
    val lean = -0.25f * easeOut(seg(fs, 0.3f, 0.7f)) * (1f - walk)
    figure(Offset(manX, ground), h * 0.22f, UtbaC36.Man, easeOut(seg(of, 0f, 0.3f)), walk = if (walk in 0.01f..0.99f) t * 4f else 0f, lean = lean, arms = if (fs > 0f && rt <= 0f) 0.6f else 0.2f)
    tagAt(a.tag("offers", 0, 0), Offset(manX, ground - h * 0.32f), seg(of, 0.2f, 0.45f) * (1f - seg(rt, 0f, 0.2f)), back = Back36, ink = ArtInk.Ivory)
    // the offers
    val offerF = easeOut(seg(of, 0.3f, 0.6f)) * (1f - easeOut(seg(fs, 0.2f, 0.6f)))
    if (offerF > 0f) {
        val coins = Offset(w * 0.33f, ground - h * 0.01f)
        for (k in 0 until 6) drawOval(UtbaC36.Coin, Offset(coins.x - w * 0.025f + (k % 3) * w * 0.012f, coins.y - h * 0.012f * (k / 3 + 1) - h * 0.005f * k), Size(w * 0.025f, h * 0.012f), alpha = offerF)
        val crown = Offset(w * 0.33f, ground - h * 0.12f)
        drawPath(Path().apply {
            moveTo(crown.x - w * 0.025f, crown.y); lineTo(crown.x - w * 0.028f, crown.y - h * 0.045f); lineTo(crown.x - w * 0.012f, crown.y - h * 0.02f)
            lineTo(crown.x, crown.y - h * 0.05f); lineTo(crown.x + w * 0.012f, crown.y - h * 0.02f); lineTo(crown.x + w * 0.028f, crown.y - h * 0.045f); lineTo(crown.x + w * 0.025f, crown.y); close()
        }, UtbaC36.Crown, alpha = offerF)
        tagAt(a.tag("offers", 0, 1), coins + Offset(0f, h * 0.08f), seg(of, 0.5f, 0.7f) * offerF, back = Back36, ink = ArtInk.Gold)
        tagAt(a.tag("offers", 0, 2), crown + Offset(0f, -h * 0.1f), seg(of, 0.6f, 0.8f) * offerF, back = Back36, ink = ArtInk.Gold)
    }
    // the recitation reaching him
    if (fs > 0f && rt < 1f) {
        val f = easeOut(seg(fs, 0f, 0.3f)) * (1f - seg(rt, 0f, 0.4f))
        for (k in 0 until 4) {
            val r = fract(t * 0.4f + k / 4f)
            val reach = w * 0.22f * r
            drawArc(UtbaC36.Wave, -30f, 60f, false, Offset(light.x - reach, light.y - reach * 0.8f), Size(2 * reach, 1.6f * reach), alpha = 0.7f * f * (1f - r), style = Stroke(2f * dp))
        }
    }
    // his people, and what he told them
    if (rt > 0f) {
        val f = easeOut(seg(rt, 0f, 0.3f))
        for (k in 0 until 4) figure(Offset(w * (0.78f + 0.05f * k), ground), h * 0.2f, Color(0xFF2E2420), f, lean = -0.1f)
        tagAt(a.tag("returns"), Offset(w * 0.72f, h * 0.36f), seg(rt, 0.6f, 0.85f) * (1f - seg(hr, 0f, 0.3f)), back = Back36, ink = ArtInk.Ivory)
    }
    // a heart that nearly flew
    if (hr > 0f) {
        val f = easeOut(seg(hr, 0f, 0.3f))
        val c = Offset(w * 0.5f, mix(h * 0.5f, h * 0.28f, easeInOut(seg(hr, 0.1f, 0.9f))))
        val s = w * 0.03f
        drawPath(w8Heart(c, s), UtbaC36.Heart, alpha = f)
        val flap = sin(t * 7f) * s * 0.4f
        for (side in listOf(-1f, 1f)) {
            val wing = Path().apply {
                moveTo(c.x + side * s * 0.6f, c.y - s * 0.2f)
                quadraticTo(c.x + side * s * 1.6f, c.y - s * 1.2f - flap, c.x + side * s * 2.4f, c.y - s * 0.6f - flap)
                quadraticTo(c.x + side * s * 1.5f, c.y - s * 0.1f, c.x + side * s * 0.6f, c.y + s * 0.2f)
                close()
            }
            drawPath(wing, Color(0xFFFBF6EA), alpha = f)
        }
    }
}

// ═════════════ 3. the challenge no one met (bs-tahaddi) ═════════════

private object DareC36 {
    val Sky = Color(0xFF1E1C34)
    val SkyLow = Color(0xFF4A3A52)
    val Floor = Color(0xFF4A3E36)
    val Stand = Color(0xFF8A6A48)
    val Tablet = Color(0xFFF6EED8)
    val Man = Color(0xFF1A1418)
    val Spear = Color(0xFF9A9AA8)
    val Pearl = Color(0xFFF4F0F8)
    val Thread = Color(0xFFE8C87A)
}

/**
 * Beats: dare (a lit tablet on its stand; facing it an empty stand: «؟»; challengers step up to it and step back),
 * spears (behind the ridge spears and banners rise: they chose war over a single surah), pearls (above, pearls
 * strung one by one on a thread: the best words in the best order), depth (the tablet opens in depth, layer under
 * layer: «اللَّفْظُ», «الْمَعْنَى»).
 */
internal fun DrawScope.challengeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "dare" to Shot(Offset(w * 0.5f, h * 0.62f), 1.2f),
        "spears" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "pearls" to Shot(Offset(w * 0.5f, h * 0.22f), 1.35f),
        "depth" to Shot(Offset(w * 0.32f, h * 0.55f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { challengeArena(a) }
}

private fun DrawScope.challengeArena(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val dr = a.b("dare")
    val sp = a.b("spears")
    val pr = a.b("pearls")
    val dp2 = a.b("depth")
    val floor = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(DareC36.Sky, DareC36.SkyLow), endY = floor), size = Size(w, floor))
    // spears and banners behind the ridge
    if (sp > 0f) {
        val rise = easeOut(seg(sp, 0.1f, 0.7f))
        for (k in 0 until 16) {
            val x = w * (0.04f + 0.062f * k)
            val top = h * 0.5f - h * 0.18f * rise - h * 0.03f * (k % 3)
            drawLine(DareC36.Spear, Offset(x, h * 0.56f), Offset(x, top), 1.5f * dp)
            drawPath(Path().apply { moveTo(x, top - h * 0.03f); lineTo(x - w * 0.006f, top); lineTo(x + w * 0.006f, top); close() }, DareC36.Spear)
            if (k % 4 == 1) drawRect(Color(0xFF8A2A2A), Offset(x, top + h * 0.01f), Size(w * 0.03f, h * 0.03f), alpha = rise)
        }
    }
    drawPath(Path().apply { moveTo(0f, h * 0.6f); quadraticTo(w * 0.5f, h * 0.5f, w, h * 0.6f); lineTo(w, floor); lineTo(0f, floor); close() }, Color(0xFF2E2634))
    drawRect(DareC36.Floor, Offset(0f, floor), Size(w, h - floor))
    // the tablet on its stand, the empty stand facing it
    val tab = Offset(w * 0.32f, h * 0.6f)
    val empty = Offset(w * 0.7f, h * 0.6f)
    val f = easeOut(seg(dr, 0f, 0.3f))
    for (c in listOf(tab, empty)) drawRect(DareC36.Stand, Offset(c.x - w * 0.03f, c.y + h * 0.08f), Size(w * 0.06f, floor - c.y - h * 0.08f), alpha = f)
    if (f > 0f) {
        softGlow(tab, w * 0.1f, ArtInk.Gold, 0.5f * f)
        drawRoundRect(DareC36.Tablet, Offset(tab.x - w * 0.07f, tab.y - h * 0.09f), Size(w * 0.14f, h * 0.17f), CornerRadius(6f * dp), alpha = f)
        for (r in 0 until 5) drawLine(Color(0xFF8A6A3A), Offset(tab.x - w * 0.05f, tab.y - h * 0.06f + r * h * 0.03f), Offset(tab.x + w * 0.05f, tab.y - h * 0.06f + r * h * 0.03f), 1.2f * dp, alpha = 0.7f * f)
        drawRoundRect(Color(0xFF6A6478), Offset(empty.x - w * 0.07f, empty.y - h * 0.09f), Size(w * 0.14f, h * 0.17f), CornerRadius(6f * dp), alpha = f, style = Stroke(1.5f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))))
        tagAt(a.tag("dare"), empty, seg(dr, 0.3f, 0.5f), back = Back36, ink = Color(0xFFD8D0E8))
    }
    // challengers step up and step back
    if (dr > 0f) {
        for (k in 0 until 3) {
            val go = bump(seg(dr, 0.2f + k * 0.22f, 0.5f + k * 0.22f))
            val x = w * (0.86f + 0.04f * k) - w * 0.1f * go
            figure(Offset(x, floor), h * 0.18f, DareC36.Man, f * (1f - seg(sp, 0.5f, 1f)), walk = if (go > 0.01f) t * 4f else 0f, lean = -0.1f * go, arms = 0.5f * go)
        }
    }
    // pearls on a thread
    if (pr > 0f) {
        val n = (12 * easeOut(seg(pr, 0f, 0.85f))).toInt()
        val y = h * 0.2f
        drawLine(DareC36.Thread, Offset(w * 0.18f, y), Offset(w * 0.82f, y + h * 0.01f), 1.2f * dp)
        for (k in 0 until n) {
            val p = Offset(w * (0.2f + 0.052f * k), y + h * 0.01f * k / 12f)
            drawCircle(DareC36.Pearl, w * 0.013f, p)
            glint(p + Offset(-w * 0.004f, -h * 0.006f), w * 0.004f, Color.White, 1f)
        }
    }
    // the tablet in depth
    if (dp2 > 0f) {
        val open = easeOut(seg(dp2, 0.1f, 0.7f))
        for (k in 1..4) {
            val inset = w * 0.012f * k * open
            drawRoundRect(lerpColor(DareC36.Tablet, ArtInk.Gold, k * 0.2f), Offset(tab.x - w * 0.07f + inset, tab.y - h * 0.09f + inset), Size(w * 0.14f - 2 * inset, h * 0.17f - 2 * inset), CornerRadius(4f * dp), alpha = 0.35f * open, style = Stroke(1.5f * dp))
        }
        softGlow(tab, w * 0.05f * (1f + open), ArtInk.Gold, 0.6f * open)
        tagAt(a.tag("depth", 0, 0), tab + Offset(0f, -h * 0.16f), seg(dp2, 0.2f, 0.45f), back = Back36, ink = ArtInk.Ivory)
        tagAt(a.tag("depth", 0, 1), tab + Offset(0f, h * 0.14f), seg(dp2, 0.5f, 0.75f), back = Back36, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. a life recorded to its last detail (bs-sira) ═════════════

private object LifeC36 {
    val Bg = Color(0xFF1A1E30)
    val BgLow = Color(0xFF2E2A40)
    val Ribbon = Color(0xFFE8DCC0)
    val Mark = Color(0xFF8A6A3A)
    val Link = Color(0xFFC8B088)
    val Bar = Color(0xFF4A4660)
}

/**
 * Beats: dense (a long ribbon — his life, his light at its start — fills with small marks: every detail recorded; a
 * lens slides along it: «أَكْمَلُ»), links (a chain of links runs down from the ribbon to the bottom of the picture:
 * passed on, trustworthy hand to hand, «أَوْثَقُ»), topofhundred (a column of a hundred bars; the topmost is lit:
 * «أَعْظَمُ»).
 */
internal fun DrawScope.lifelineScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "dense" to Shot(Offset(w * 0.45f, h * 0.35f), 1.25f),
        "links" to Shot(Offset(w * 0.45f, h * 0.6f), 1.2f),
        "topofhundred" to Shot(Offset(w * 0.82f, h * 0.45f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { lifeRibbon(a) }
}

private fun DrawScope.lifeRibbon(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val dn = a.b("dense")
    val lk = a.b("links")
    val th = a.b("topofhundred")
    drawRect(Brush.verticalGradient(listOf(LifeC36.Bg, LifeC36.BgLow)), size = Size(w, h))
    val y = h * 0.34f
    val x0 = w * 0.12f
    val x1 = w * 0.7f
    val rf = easeOut(seg(dn, 0f, 0.3f))
    if (rf > 0f) {
        drawRoundRect(LifeC36.Ribbon, Offset(x0, y - h * 0.035f), Size((x1 - x0) * rf, h * 0.07f), CornerRadius(4f * dp))
        val n = (140 * easeOut(seg(dn, 0.2f, 0.9f))).toInt()
        for (k in 0 until n) {
            val x = x0 + (x1 - x0) * w8Rnd(k, 2.3f)
            val hh = h * (0.01f + 0.04f * w8Rnd(k, 5.9f))
            drawLine(LifeC36.Mark, Offset(x, y - hh / 2f), Offset(x, y + hh / 2f), 1f * dp, alpha = 0.8f)
        }
        // the lens sliding along it
        val lx = mix(x0 + w * 0.05f, x1 - w * 0.05f, 0.5f + 0.5f * sin(t * 0.5f))
        val lens = Offset(lx, y)
        drawCircle(Color.White, w * 0.04f, lens, alpha = 0.15f * rf)
        drawCircle(Color(0xFFD8C8A0), w * 0.04f, lens, alpha = 0.9f * rf, style = Stroke(2f * dp))
        drawLine(Color(0xFFD8C8A0), lens + Offset(w * 0.028f, h * 0.04f), lens + Offset(w * 0.05f, h * 0.08f), 3f * dp, StrokeCap.Round, alpha = rf)
        tagAt(a.tag("dense"), Offset((x0 + x1) / 2f, y - h * 0.14f), seg(dn, 0.4f, 0.65f), back = Back36, ink = ArtInk.Gold)
    }
    sirajMunir(Offset(x0 - w * 0.04f, y), w * 0.011f, easeOut(seg(dn, 0f, 0.3f)), t, nameAt = Offset(x0 + w * 0.06f, y + h * 0.1f))
    // a chain from the record down to us
    if (lk > 0f) {
        val n = (10 * easeOut(seg(lk, 0f, 0.8f))).toInt()
        for (k in 0 until n) {
            val c = Offset(w * 0.45f, y + h * 0.08f + k * h * 0.055f)
            if (k % 2 == 0) drawOval(LifeC36.Link, Offset(c.x - w * 0.012f, c.y - h * 0.03f), Size(w * 0.024f, h * 0.06f), style = Stroke(2.2f * dp))
            else drawOval(LifeC36.Link, Offset(c.x - w * 0.003f, c.y - h * 0.03f), Size(w * 0.006f, h * 0.06f), style = Stroke(2.2f * dp))
        }
        tagAt(a.tag("links"), Offset(w * 0.33f, h * 0.68f), seg(lk, 0.4f, 0.65f), back = Back36, ink = ArtInk.Gold)
    }
    // one hundred, his at the top
    if (th > 0f) {
        val f = easeOut(seg(th, 0f, 0.3f))
        val left = w * 0.78f
        val top = h * 0.14f
        val n = (100 * easeOut(seg(th, 0f, 0.6f))).toInt()
        for (k in 0 until n) {
            val col = k / 25
            val row = k % 25
            val x = left + col * w * 0.035f
            val yy = top + row * h * 0.028f
            drawRect(if (k == 0) ArtInk.Gold else LifeC36.Bar, Offset(x, yy), Size(w * 0.028f, h * 0.018f), alpha = f)
        }
        if (n > 0) softGlow(Offset(left + w * 0.014f, top + h * 0.009f), w * 0.03f, ArtInk.Gold, 0.8f * seg(th, 0.5f, 0.8f))
        tagAt(a.tag("topofhundred"), Offset(left - w * 0.06f, top), seg(th, 0.5f, 0.75f), back = Back36, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. the valley of stones and the dawn (bs-jahiliyya) ═════════════

private object ValeC36 {
    val Night = Color(0xFF14122A)
    val NightLow = Color(0xFF2E2440)
    val Dawn = Color(0xFFF2C48A)
    val Day = Color(0xFFBCD6EA)
    val Ground = Color(0xFF3A2E2A)
    val GroundDay = Color(0xFFB89A6A)
    val Stone = Color(0xFF8A7E72)
    val Better = Color(0xFFA89A88)
    val Jar = Color(0xFF7A4A32)
    val Die = Color(0xFFE8E0D0)
}

/**
 * Beats: darkvale (a night valley: wine jars, dice, a stone on a plinth), swapstone (a finer stone turns up; the old
 * one is tossed aside and the new one set up in its place), daybreak (dawn reaches the valley; the stones topple:
 * «التَّوْحِيدُ»), rights (three signs light up — an open hand, a sapling, a balance with two pans: «الْيَتِيمُ»,
 * «الطِّفْلُ», «الْمِيرَاثُ»).
 */
internal fun DrawScope.darkvaleScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "darkvale" to Shot(Offset(w * 0.45f, h * 0.65f), 1.2f),
        "swapstone" to Shot(Offset(w * 0.55f, h * 0.62f), 1.45f),
        "daybreak" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "rights" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { valeNight(a) }
}

private fun DrawScope.valeNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val dv = a.b("darkvale")
    val ss = a.b("swapstone")
    val db = a.b("daybreak")
    val rg = a.b("rights")
    val ground = h * 0.74f
    val day = easeOut(seg(db, 0.1f, 0.7f))
    val sky = lerpColor(lerpColor(ValeC36.Night, ValeC36.Dawn, (day * 2f).coerceAtMost(1f)), ValeC36.Day, ((day - 0.5f) * 2f).coerceIn(0f, 1f))
    drawRect(Brush.verticalGradient(listOf(sky, lerpColor(ValeC36.NightLow, ValeC36.Dawn, day)), endY = ground), size = Size(w, ground))
    if (day < 1f) for (k in 0 until 30) drawCircle(Color.White, w * 0.002f, Offset(w * w8Rnd(k, 5.5f), ground * 0.8f * w8Rnd(k, 2.9f)), alpha = (1f - day) * (0.5f + 0.4f * sin(t + k)))
    if (db > 0f) {
        val sun = Offset(w * 0.5f, ground - h * 0.25f * day)
        drawCircle(Color(0xFFFFD27A), w * 0.04f, sun, alpha = day)
        lightShafts(sun, -PI.toFloat() / 2f, 3f, h * 0.6f, Color(0xFFFFE8B0), 0.5f * day, t, rays = 12)
    }
    drawPath(Path().apply { moveTo(0f, ground); lineTo(0f, h * 0.48f); quadraticTo(w * 0.2f, h * 0.44f, w * 0.32f, ground); close() }, lerpColor(Color(0xFF221C2A), Color(0xFF8A7058), day))
    drawPath(Path().apply { moveTo(w, ground); lineTo(w, h * 0.46f); quadraticTo(w * 0.8f, h * 0.42f, w * 0.68f, ground); close() }, lerpColor(Color(0xFF221C2A), Color(0xFF8A7058), day))
    drawRect(lerpColor(ValeC36.Ground, ValeC36.GroundDay, day), Offset(0f, ground), Size(w, h - ground))
    val vf = easeOut(seg(dv, 0f, 0.4f))
    val topple = easeInOut(seg(db, 0.4f, 0.9f))
    // jars and dice
    for (k in 0 until 3) {
        val c = Offset(w * (0.14f + 0.06f * k), ground + h * 0.06f)
        drawOval(ValeC36.Jar, Offset(c.x - w * 0.02f, c.y - h * 0.08f), Size(w * 0.04f, h * 0.08f), alpha = vf * (1f - 0.7f * topple))
        drawRect(ValeC36.Jar, Offset(c.x - w * 0.008f, c.y - h * 0.1f), Size(w * 0.016f, h * 0.025f), alpha = vf * (1f - 0.7f * topple))
    }
    for (k in 0 until 2) {
        val c = Offset(w * (0.8f + 0.05f * k), ground + h * 0.08f)
        drawRoundRect(ValeC36.Die, Offset(c.x - w * 0.015f, c.y - w * 0.015f), Size(w * 0.03f, w * 0.03f), CornerRadius(3f * dp), alpha = vf * (1f - 0.7f * topple))
        for (d in 0 until 1 + k * 2) drawCircle(Color(0xFF2A2420), w * 0.0035f, c + Offset(w * 0.008f * (d - k), -w * 0.008f * (d - k)), alpha = vf * (1f - 0.7f * topple))
    }
    // the stone on its plinth, swapped for a finer one
    val plinth = Offset(w * 0.55f, ground + h * 0.02f)
    drawRect(Color(0xFF5A4A40), Offset(plinth.x - w * 0.05f, plinth.y - h * 0.04f), Size(w * 0.1f, h * 0.04f), alpha = vf)
    val toss = easeOut(seg(ss, 0.3f, 0.7f))
    val oldAt = Offset(plinth.x + w * 0.2f * toss, plinth.y - h * 0.1f + h * 0.08f * toss - sin(PI.toFloat() * toss) * h * 0.1f)
    val oldTilt = 1f - 0.3f * topple
    drawOval(ValeC36.Stone, Offset(oldAt.x - w * 0.04f, oldAt.y - h * 0.06f * oldTilt), Size(w * 0.08f, h * 0.12f * oldTilt), alpha = vf)
    if (ss > 0f) {
        val come = easeInOut(seg(ss, 0.2f, 0.9f))
        val from = Offset(w * 0.3f, ground - h * 0.02f)
        val to = plinth + Offset(0f, -h * 0.11f)
        val p = from + (to - from) * come - Offset(0f, sin(PI.toFloat() * come) * h * 0.08f)
        val s = 1f - 0.4f * topple
        drawOval(ValeC36.Better, Offset(p.x - w * 0.045f, p.y - h * 0.07f * s + h * 0.07f * (1f - s)), Size(w * 0.09f, h * 0.14f * s), alpha = easeOut(seg(ss, 0f, 0.2f)))
        if (come >= 1f && db <= 0f) glint(p + Offset(-w * 0.015f, -h * 0.04f), w * 0.006f, Color.White, 1f)
    }
    tagAt(a.tag("daybreak"), Offset(w * 0.5f, h * 0.12f), seg(db, 0.4f, 0.65f), back = Back36, ink = ArtInk.Gold)
    // three rights
    if (rg > 0f) {
        val xs = listOf(w * 0.25f, w * 0.5f, w * 0.75f)
        for (k in 0 until 3) {
            val f = easeOut(seg(rg, 0.1f + 0.2f * k, 0.35f + 0.2f * k))
            if (f <= 0f) continue
            val c = Offset(xs[k], h * 0.36f)
            drawCircle(Color(0xFF2A2440), w * 0.045f, c, alpha = 0.85f * f)
            when (k) {
                0 -> {
                    drawArc(Color(0xFFF2D8B0), 20f, 140f, false, Offset(c.x - w * 0.025f, c.y - h * 0.03f), Size(w * 0.05f, h * 0.06f), alpha = f, style = Stroke(3f * dp, cap = StrokeCap.Round))
                    drawCircle(ArtInk.Gold, w * 0.008f, c + Offset(0f, -h * 0.01f), alpha = f)
                }
                1 -> {
                    drawLine(Color(0xFF6AA84A), c + Offset(0f, h * 0.03f), c + Offset(0f, -h * 0.02f), 2.5f * dp, StrokeCap.Round, alpha = f)
                    drawOval(Color(0xFF8AC86A), Offset(c.x, c.y - h * 0.035f), Size(w * 0.018f, h * 0.02f), alpha = f)
                    drawOval(Color(0xFF8AC86A), Offset(c.x - w * 0.018f, c.y - h * 0.02f), Size(w * 0.018f, h * 0.02f), alpha = f)
                }
                else -> {
                    drawLine(ArtInk.Gold, c + Offset(-w * 0.025f, -h * 0.02f), c + Offset(w * 0.025f, -h * 0.02f), 2f * dp, alpha = f)
                    drawLine(ArtInk.Gold, c + Offset(0f, -h * 0.035f), c + Offset(0f, h * 0.03f), 2f * dp, alpha = f)
                    for (s in listOf(-1f, 1f)) drawArc(ArtInk.Gold, 0f, 180f, false, Offset(c.x + s * w * 0.025f - w * 0.012f, c.y - h * 0.01f), Size(w * 0.024f, h * 0.02f), alpha = f, style = Stroke(1.5f * dp))
                }
            }
            tagAt(a.tag("rights", 0, k), c + Offset(0f, h * 0.1f), seg(rg, 0.2f + 0.2f * k, 0.45f + 0.2f * k), back = Back36, ink = ArtInk.Ivory)
        }
    }
}
