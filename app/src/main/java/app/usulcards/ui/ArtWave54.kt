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
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/*
 * Wave 54 (2026-11-03): «حصن المسلم»، «أذكار الأحوال» (2). Rough ground made smooth and the door that «لو» opens; a
 * mountain of debt lifted; a broken jar and a better one in its place, and the roundel of a Mother of the Believers;
 * a bed visited seven times; rain that is asked to fall around the town and not on it. Allah is never drawn; the
 * Prophet ﷺ is only light; companions and Ahl al-Bayt are names in roundels; Shaytan is a dark smoke.
 */

private val Back54 = Color(0xE6181420)

// ═════════════ 1. rough ground made smooth (hisn-istisab) ═════════════

private object RoughC54 {
    val Sky = Color(0xFFC0D6E6)
    val SkyLow = Color(0xFFF2E6CC)
    val Ground = Color(0xFFC8AE84)
    val Rock = Color(0xFF8A7860)
    val Smooth = Color(0xFFDCC8A0)
    val Person = Color(0xFF3A3040)
    val Gold = Color(0xFFE8B84A)
    val Smoke = Color(0xFF2A2430)
    val Red = Color(0xFFC0482E)
    val Load = Color(0xFF8A6A44)
}

/**
 * Beats: smoothed (a walker meets a field of sharp rocks, «الْحَزْنَ»; as he goes on they settle into a smooth path),
 * nolaw (he looks back at a fork; a thought marked «لَوْ» opens a door from which dark smoke comes — struck out — and
 * the words «قَدَرُ اللَّهِ وَمَا شَاءَ فَعَلَ» shine instead), strongweak (two who carry loads, one heavier than the
 * other; a glow over each, a larger one over the stronger), whispercut (a whisper of smoke comes at one's head and is cut
 * off; a word rises: «آمَنْتُ بِاللَّهِ وَرُسُلِهِ»).
 */
internal fun DrawScope.roughgroundScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "smoothed" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "nolaw" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "strongweak" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "whispercut" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { roughDay(a) }
}

private fun DrawScope.roughDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sm = a.b("smoothed")
    val nl = a.b("nolaw")
    val sw = a.b("strongweak")
    val co = a.b("whispercut")
    val ground = h * 0.82f
    daySky12(RoughC54.Sky, RoughC54.SkyLow, ground, RoughC54.Ground)
    if (sm > 0f) {
        val f = easeOut(seg(sm, 0f, BEAT_IN)) * (1f - seg(nl, 0f, BEAT_OUT))
        val go = easeInOut(seg(sm, 0.1f, 0.95f))
        // rough ground (الحَزْن) is made smooth under his feet as he goes; what is ahead is still rough
        val manX = w * (0.08f + 0.6f * go)
        drawLine(RoughC54.Smooth, Offset(0f, ground - h * 0.004f), Offset(manX, ground - h * 0.004f), h * 0.02f, StrokeCap.Round, alpha = f)
        for (k in 0 until 9) {
            val x = w * (0.12f + 0.09f * k)
            val flat = seg(manX - x, -w * 0.04f, w * 0.03f)
            val ht = h * (0.06f + 0.05f * ((k * 7) % 3) / 2f) * (1f - flat)
            drawPath(Path().apply { moveTo(x - w * 0.045f, ground); lineTo(x - w * 0.01f, ground - ht); lineTo(x + w * 0.02f, ground - ht * 0.7f); lineTo(x + w * 0.045f, ground); close() }, lerpColor(RoughC54.Rock, RoughC54.Smooth, flat), alpha = f)
        }
        figure(Offset(manX, ground), h * 0.26f, RoughC54.Person, f, walk = if (go < 0.99f) t * 5f else 0f)
        tagAt(a.tag("smoothed", 0, 0), Offset(max(manX + w * 0.16f, w * 0.82f), ground - h * 0.16f), f * seg(sm, 0.1f, 0.25f), back = Back54, ink = Color(0xFFF0C8A0))
        tagAt(a.tag("smoothed", 0, 1), Offset(max(manX - w * 0.2f, w * 0.14f), ground - h * 0.1f), f * seg(sm, 0.35f, 0.55f), back = Back54, ink = ArtInk.Gold)
    }
    if (nl > 0f) {
        val f = easeOut(seg(nl, 0f, BEAT_IN)) * (1f - seg(sw, 0f, 0.4f))
        figure(Offset(w * 0.5f, ground), h * 0.28f, RoughC54.Person, f, lean = -0.1f)
        val d = Offset(w * 0.22f, ground)
        door12(d, w * 0.1f, h * 0.3f, seg(nl, 0.15f, 0.35f), RoughC54.Rock, Color(0xFF3A2A1E), f, Color(0xFF4A4050))
        smoke12(d + Offset(0f, -h * 0.15f), Offset(w * 0.46f, ground - h * 0.25f), seg(nl, 0.3f, 0.5f) * (1f - seg(nl, 0.55f, 0.7f)), t, f, RoughC54.Smoke)
        struckTag21(a.tag("nolaw", 0, 0), Offset(w * 0.3f, h * 0.3f), f * seg(nl, 0.1f, 0.25f), seg(nl, 0.45f, 0.6f), RoughC54.Red, Back54, ArtInk.Ivory)
        tagAt(a.tag("nolaw", 0, 1), Offset(w * 0.66f, h * 0.3f), f * seg(nl, 0.6f, 0.8f), back = Back54, ink = ArtInk.Gold)
    }
    if (sw > 0f) {
        // the strong believer carries the load upright on his shoulders; the weak one bends under a small one; both are lit
        val f = easeOut(seg(sw, 0f, 0.3f)) * (1f - seg(co, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val p = Offset(w * (0.68f - 0.36f * k), ground)
            val big = k == 0
            val tall = h * 0.3f
            figure(p, tall, RoughC54.Person, f, lean = if (big) 0f else 0.22f, arms = 1f)
            val top = p + Offset(if (big) 0f else tall * 0.2f, -tall * (if (big) 1.02f else 0.9f))
            drawRoundRect(RoughC54.Load, Offset(top.x - w * (if (big) 0.07f else 0.035f), top.y - h * (if (big) 0.12f else 0.05f)), Size(w * (if (big) 0.14f else 0.07f), h * (if (big) 0.12f else 0.05f)), CornerRadius(3f * dp), alpha = f)
            softGlow(p + Offset(0f, -tall * 0.6f), w * (if (big) 0.1f else 0.06f), RoughC54.Gold, (if (big) 0.7f else 0.4f) * f * seg(sw, 0.3f + 0.2f * k, 0.5f + 0.2f * k))
            tagAt(a.tag("strongweak", 0, k), p + Offset(0f, h * 0.08f), f * seg(sw, 0.2f + 0.2f * k, 0.4f + 0.2f * k), back = Back54, ink = if (big) ArtInk.Gold else ArtInk.Ivory)
        }
        tagAt(a.tag("strongweak", 0, 2), Offset(w * 0.5f, h * 0.12f), f * seg(sw, 0.65f, 0.85f), back = Back54, ink = ArtInk.Gold)
    }
    if (co > 0f) {
        // the whisper comes as dark smoke toward his head; he says the words, and it breaks up and is gone
        val f = easeOut(seg(co, 0f, 0.3f))
        val c = Offset(w * 0.45f, ground)
        figure(c, h * 0.32f, RoughC54.Person, f)
        val cut = seg(co, 0.45f, 0.7f)
        smoke12(Offset(w * 0.92f, h * 0.25f), c + Offset(w * 0.05f, -h * 0.3f), (0.3f + 0.6f * seg(co, 0.05f, 0.4f)), t, f * (1f - 0.7f * cut), RoughC54.Smoke)
        tagAt(a.tag("whispercut", 0, 1), Offset(w * 0.78f, h * 0.42f), f * seg(co, 0.1f, 0.3f) * (1f - 0.5f * cut), back = Back54, ink = Color(0xFFC8B8D0))
        softGlow(c + Offset(0f, -h * 0.26f), w * 0.12f, RoughC54.Gold, 0.6f * f * cut)
        tagAt(a.tag("whispercut"), Offset(w * 0.45f, h * 0.2f), f * seg(co, 0.4f, 0.6f), back = Back54, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. a mountain of debt (hisn-dayn) ═════════════

private object DebtC54 {
    val Sky = Color(0xFFB8CCDC)
    val SkyLow = Color(0xFFEFE2C8)
    val Ground = Color(0xFFC8B08A)
    val Mount = Color(0xFF7A6A5E)
    val Person = Color(0xFF3A3040)
    val Ali = Color(0xFF1B2A5C)
    val Gold = Color(0xFFE8B84A)
    val Chain = Color(0xFF8A8494)
    val Field = Color(0xFF7A9A54)
    val Purse = Color(0xFF8A5A3A)
}

/**
 * Beats: comestoali (a plain roundel comes to the roundel of «عَلِيٍّ», and words of light pass to it), mountainpaid (a mountain stands
 * over him; it crumbles into coins that roll away as two words shine: «اكْفِنِي بِحَلَالِكَ عَنْ حَرَامِكَ», «وَأَغْنِنِي
 * بِفَضْلِكَ عَمَّنْ سِوَاكَ»), heavyload (a heavy chain on one's shoulders bends him), workandask (side by side: one works a
 * field, and the same raises his hands at dusk), repaid (a purse is handed back with a glow of thanks).
 */
internal fun DrawScope.mountaindebtScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "comestoali" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "mountainpaid" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "heavyload" to Shot(Offset(w * 0.5f, h * 0.6f), 1.25f),
        "workandask" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "repaid" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { debtDay(a) }
}

private fun DrawScope.debtDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ca = a.b("comestoali")
    val mp = a.b("mountainpaid")
    val hl = a.b("heavyload")
    val wa = a.b("workandask")
    val rp = a.b("repaid")
    val ground = h * 0.84f
    daySky12(DebtC54.Sky, DebtC54.SkyLow, ground, DebtC54.Ground)
    if (ca > 0f) {
        val f = easeOut(seg(ca, 0f, BEAT_IN)) * (1f - seg(mp, 0f, BEAT_OUT))
        val come = easeInOut(seg(ca, 0.05f, 0.45f))
        nameRoundel(Offset(w * (0.85f - 0.25f * come), ground - h * 0.2f), w * 0.045f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val r = Offset(w * 0.34f, ground - h * 0.2f)
        nameRoundel(r, w * 0.06f, a.big("comestoali"), DebtC54.Ali, Roundel.Rim, f * seg(ca, 0.1f, 0.3f))
        for (k in 0 until 4) {
            val u = fract(t * 0.6f + k / 4f) * seg(ca, 0.5f, 0.6f)
            drawCircle(DebtC54.Gold, 2f * dp, r + (Offset(w * 0.58f, ground - h * 0.25f) - r) * u, alpha = f * (1f - u))
        }
    }
    if (mp > 0f) {
        val f = easeOut(seg(mp, 0f, BEAT_IN)) * (1f - seg(hl, 0f, BEAT_OUT))
        val crumble = seg(mp, 0.35f, 0.8f)
        val c = Offset(w * 0.5f, ground)
        if (crumble < 1f) drawPath(Path().apply { moveTo(c.x - w * 0.3f, ground); lineTo(c.x - w * 0.05f, h * (0.12f + 0.5f * crumble)); lineTo(c.x + w * 0.08f, h * (0.2f + 0.45f * crumble)); lineTo(c.x + w * 0.3f, ground); close() }, DebtC54.Mount, alpha = f * (1f - crumble * 0.6f))
        for (k in 0 until 10) {
            val u = seg(mp, 0.4f + 0.03f * k, 0.8f + 0.02f * k)
            coin12(Offset(c.x + w * (0.03f * (k - 4.5f)) + w * 0.4f * u * (if (k % 2 == 0) 1f else -1f), ground - h * 0.03f), w * 0.015f, f * u * (1f - seg(u, 0.85f, 1f)))
        }
        figure(Offset(w * 0.5f, ground), h * 0.22f, DebtC54.Person, f)
        for (k in 0 until 2) tagAt(a.tag("mountainpaid", 0, k), Offset(w * (0.3f + 0.4f * k), h * 0.18f + h * 0.08f * k), f * seg(mp, 0.2f + 0.25f * k, 0.35f + 0.25f * k), back = Back54, ink = ArtInk.Gold)
    }
    if (hl > 0f) {
        val f = easeOut(seg(hl, 0f, BEAT_IN)) * (1f - seg(wa, 0f, BEAT_OUT))
        val bend = easeInOut(seg(hl, 0.1f, 0.5f))
        val p = Offset(w * 0.5f, ground)
        figure(p, h * 0.32f, DebtC54.Person, f, lean = 0.4f * bend, walk = t * 2f)
        for (k in 0 until 5) drawOval(DebtC54.Chain, Offset(p.x - w * 0.02f + w * 0.03f * k, p.y - h * 0.3f + h * 0.03f * bend + h * 0.01f * k), Size(w * 0.035f, h * 0.03f), alpha = f, style = Stroke(2.5f * dp))
        drawRoundRect(Color(0xFF5A5060), Offset(p.x + w * 0.1f, p.y - h * 0.12f), Size(w * 0.08f, h * 0.12f), CornerRadius(3f * dp), alpha = f)
    }
    if (wa > 0f) {
        val f = easeOut(seg(wa, 0f, BEAT_IN)) * (1f - seg(rp, 0f, BEAT_OUT))
        drawRect(DebtC54.Field, Offset(w * 0.05f, ground), Size(w * 0.4f, h * 0.05f), alpha = f)
        val dig = sin(t * 4f)
        figure(Offset(w * 0.25f, ground), h * 0.3f, DebtC54.Person, f, lean = 0.25f + 0.1f * dig, arms = 0.8f)
        drawLine(Color(0xFF6A4A2E), Offset(w * 0.19f, ground - h * 0.17f), Offset(w * 0.16f, ground + h * 0.01f * dig), 3f * dp, StrokeCap.Round, alpha = f)
        drawRect(Brush.verticalGradient(listOf(Color(0xFF3A3058), Color(0xFFD8805A)), h * 0.1f, ground), Offset(w * 0.55f, h * 0.1f), Size(w * 0.4f, ground - h * 0.1f), alpha = f * 0.8f)
        sit12(Offset(w * 0.74f, ground), h * 0.28f, DebtC54.Person, f, -1f)
        hands12(Offset(w * 0.72f, ground - h * 0.32f), h * 0.08f, DebtC54.Person, f)
        drawLine(Color(0xFF6A5A4A), Offset(w * 0.5f, h * 0.15f), Offset(w * 0.5f, ground), 1.5f * dp, alpha = f * 0.6f)
    }
    if (rp > 0f) {
        val f = easeOut(seg(rp, 0f, 0.3f))
        figure(Offset(w * 0.3f, ground), h * 0.3f, DebtC54.Person, f, arms = 0.6f)
        figure(Offset(w * 0.7f, ground), h * 0.3f, DebtC54.Person, f, arms = -0.4f)
        val pass = easeInOut(seg(rp, 0.1f, 0.5f))
        val c = Offset(w * (0.36f + 0.24f * pass), ground - h * 0.18f)
        drawOval(DebtC54.Purse, Offset(c.x - w * 0.025f, c.y - h * 0.03f), Size(w * 0.05f, h * 0.06f), alpha = f)
        softGlow(Offset(w * 0.5f, ground - h * 0.3f), w * 0.1f, DebtC54.Gold, 0.5f * f * seg(rp, 0.5f, 0.8f))
    }
}

// ═════════════ 3. a better one in its place (hisn-musiba) ═════════════

private object LossC54 {
    val Night = Color(0xFF151A2E)
    val NightLow = Color(0xFF3A3456)
    val Ground = Color(0xFF242230)
    val Jar = Color(0xFFB08458)
    val Better = Color(0xFFE8C46A)
    val Mother = Color(0xFF3A2A4A)
    val Gold = Color(0xFFE8C46A)
    val Grave = Color(0xFF8A8494)
    val Glass = Color(0xFFE8DCC0)
    val Sand = Color(0xFFD8B46A)
}

/**
 * Beats: brokenjar (a jar falls and breaks; in its place a finer one rises, glowing), ummsalama (a roundel, «أُمُّ
 * سَلَمَةَ», beside a fading plain roundel, «أَبُو سَلَمَةَ»; then the light ﷺ comes beside her), takengiven (two pans: what
 * He took and what He gave, «مَا أَخَذَ», «مَا أَعْطَى», and an hourglass for the set term), visitgraves (plain graves under
 * the night; a soft light passes over them with the greeting: «أَهْلَ الدِّيَارِ»).
 */
internal fun DrawScope.betterjarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "brokenjar" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "ummsalama" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "takengiven" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "visitgraves" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { lossNight(a) }
}

private fun DrawScope.lossNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bj = a.b("brokenjar")
    val us = a.b("ummsalama")
    val tg = a.b("takengiven")
    val vg = a.b("visitgraves")
    val ground = h * 0.82f
    nightSky(t, LossC54.Night, LossC54.NightLow, ground, starAlpha = 0.7f)
    drawRect(LossC54.Ground, Offset(0f, ground), Size(w, h - ground))
    if (bj > 0f) {
        val f = easeOut(seg(bj, 0f, BEAT_IN)) * (1f - seg(us, 0f, BEAT_OUT))
        val fall = seg(bj, 0.05f, 0.25f)
        val broke = seg(bj, 0.25f, 0.3f)
        val c = Offset(w * 0.5f, h * 0.3f + (ground - h * 0.3f) * fall * fall)
        if (broke <= 0f) cup12(c, w * 0.1f, LossC54.Jar, f)
        else for (k in 0 until 6) {
            val ang = k * PI.toFloat() / 3f
            val u = seg(bj, 0.25f, 0.5f)
            val p = Offset(w * 0.5f + cos(ang) * w * 0.08f * u, ground - h * 0.01f - kotlin.math.abs(sin(ang)) * h * 0.04f * u)
            rotate(60f * k * u, p) { drawRect(LossC54.Jar, Offset(p.x - w * 0.012f, p.y - h * 0.01f), Size(w * 0.024f, h * 0.02f), alpha = f * (1f - seg(bj, 0.55f, 0.75f))) }
        }
        val rise = easeOut(seg(bj, 0.6f, 0.95f))
        if (rise > 0f) {
            softGlow(Offset(w * 0.5f, ground - h * 0.1f), w * 0.1f, LossC54.Better, 0.6f * f * rise)
            cup12(Offset(w * 0.5f, ground - h * 0.01f), w * 0.13f * rise, LossC54.Better, f)
        }
    }
    if (us > 0f) {
        val f = easeOut(seg(us, 0f, BEAT_IN)) * (1f - seg(tg, 0f, BEAT_OUT))
        val m = Offset(w * 0.4f, h * 0.5f)
        nameRoundel(m, w * 0.06f, a.big("ummsalama", 0, 0), LossC54.Mother, Roundel.Rim, f * seg(us, 0.05f, 0.2f))
        val fade = seg(us, 0.25f, 0.5f)
        nameRoundel(Offset(w * 0.62f, h * 0.5f), w * 0.05f, a.big("ummsalama", 0, 1), Roundel.PlainFill, Roundel.PlainRim, f * seg(us, 0.1f, 0.2f) * (1f - 0.75f * fade))
        val light = Offset(w * 0.64f, h * 0.36f)
        sirajMunir(light, w * 0.013f, f * seg(us, 0.6f, 0.85f), t, nameAt = light + Offset(0f, -h * 0.12f))
    }
    if (tg > 0f) {
        val f = easeOut(seg(tg, 0f, BEAT_IN)) * (1f - seg(vg, 0f, BEAT_OUT))
        for (k in 0 until 2) {
            val c = Offset(w * (0.28f + 0.44f * k), h * 0.5f)
            val g = seg(tg, 0.1f + 0.2f * k, 0.3f + 0.2f * k)
            drawArc(LossC54.Gold, 0f, 180f, false, Offset(c.x - w * 0.09f, c.y - h * 0.05f), Size(w * 0.18f, h * 0.12f), alpha = f * g, style = Stroke(2.5f * dp))
            tagAt(a.tag("takengiven", 0, k), c + Offset(0f, h * 0.15f), f * g, back = Back54, ink = ArtInk.Gold)
        }
        val hg = Offset(w * 0.5f, h * 0.4f)
        val sand = seg(tg, 0.5f, 1f)
        drawPath(Path().apply { moveTo(hg.x - w * 0.03f, hg.y - h * 0.08f); lineTo(hg.x + w * 0.03f, hg.y - h * 0.08f); lineTo(hg.x, hg.y); lineTo(hg.x + w * 0.03f, hg.y + h * 0.08f); lineTo(hg.x - w * 0.03f, hg.y + h * 0.08f); lineTo(hg.x, hg.y); close() }, LossC54.Glass, alpha = f * seg(tg, 0.4f, 0.55f), style = Stroke(2f * dp))
        drawPath(Path().apply { moveTo(hg.x - w * 0.02f * sand, hg.y + h * 0.075f); lineTo(hg.x + w * 0.02f * sand, hg.y + h * 0.075f); lineTo(hg.x, hg.y + h * 0.075f - h * 0.05f * sand); close() }, LossC54.Sand, alpha = f * seg(tg, 0.4f, 0.55f))
    }
    if (vg > 0f) {
        val f = easeOut(seg(vg, 0f, 0.3f))
        for (k in 0 until 6) {
            val x = w * (0.12f + 0.15f * k)
            drawPath(Path().apply { moveTo(x - w * 0.05f, ground); quadraticTo(x, ground - h * 0.06f, x + w * 0.05f, ground); close() }, LossC54.Grave, alpha = f * 0.8f)
            drawLine(LossC54.Grave, Offset(x + w * 0.04f, ground), Offset(x + w * 0.04f, ground - h * 0.05f), 2f * dp, alpha = f)
        }
        val u = seg(vg, 0.2f, 0.9f)
        softGlow(Offset(w * (0.08f + 0.84f * u), ground - h * 0.12f), w * 0.05f, LossC54.Gold, 0.6f * f * bump(u))
        tagAt(a.tag("visitgraves"), Offset(w * 0.5f, h * 0.35f), f * seg(vg, 0.25f, 0.45f), back = Back54, ink = ArtInk.Ivory)
    }
}

// ═════════════ 4. the sick bed (hisn-marid) ═════════════

private object HealC54 {
    val Wall = Color(0xFF3A3040)
    val WallLit = Color(0xFF6A5448)
    val Floor = Color(0xFF2A2228)
    val Bed = Color(0xFF7A5A44)
    val Blanket = Color(0xFFB0A090)
    val Person = Color(0xFFE6DCC8)
    val Gold = Color(0xFFF2C45A)
    val Ring = Color(0xFFBFE2FF)
    val Garden = Color(0xFF3E6A44)
}

/**
 * Beats: nobas (the light ﷺ comes to a sick bed and a word stays by it: «طَهُورٌ»), sevenvisits (a visitor sits by a sick
 * man; seven soft rings close round the bed one by one, and the sick man sits up), handonpain (one sits with his hand on
 * the place of pain; three rings, then seven), lastword (a small lamp burns low by a bed; the words stand over it,
 * «لَا إِلَهَ إِلَّا اللَّهُ», and a garden gate opens).
 */
internal fun DrawScope.healingbedScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "nobas" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "sevenvisits" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "handonpain" to Shot(Offset(w * 0.5f, h * 0.6f), 1.25f),
        "lastword" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { healRoom(a) }
}

private fun DrawScope.healRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val nb = a.b("nobas")
    val sr = a.b("sevenvisits")
    val hp = a.b("handonpain")
    val lw = a.b("lastword")
    val floor = h * 0.82f
    val warm = (nb + sr).coerceAtMost(1f)
    drawRect(lerpColor(HealC54.Wall, HealC54.WallLit, 0.4f * warm), Offset.Zero, Size(w, floor))
    drawRect(HealC54.Floor, Offset(0f, floor), Size(w, h - floor))
    val bed = Offset(w * 0.48f, floor - h * 0.06f)
    fun bedWith(alpha: Float, sitUp: Float) {
        drawRoundRect(HealC54.Bed, Offset(bed.x - w * 0.2f, bed.y), Size(w * 0.4f, h * 0.05f), CornerRadius(4f * dp), alpha = alpha)
        if (sitUp < 0.5f) {
            drawRoundRect(HealC54.Blanket, Offset(bed.x - w * 0.14f, bed.y - h * 0.04f), Size(w * 0.3f, h * 0.05f), CornerRadius(h * 0.025f), alpha = alpha)
            drawCircle(HealC54.Person, h * 0.03f, Offset(bed.x - w * 0.17f, bed.y - h * 0.03f), alpha = alpha)
        } else sit12(Offset(bed.x - w * 0.05f, bed.y), h * 0.26f, HealC54.Person, alpha, 1f)
    }
    if (nb > 0f) {
        val f = easeOut(seg(nb, 0f, BEAT_IN)) * (1f - seg(sr, 0f, BEAT_OUT))
        drawRoundRect(HealC54.Bed, Offset(bed.x - w * 0.2f, bed.y), Size(w * 0.4f, h * 0.05f), CornerRadius(4f * dp), alpha = f)
        drawRoundRect(HealC54.Blanket, Offset(bed.x - w * 0.16f, bed.y - h * 0.05f), Size(w * 0.32f, h * 0.06f), CornerRadius(h * 0.03f), alpha = f)
        val come = easeInOut(seg(nb, 0.05f, 0.45f))
        val light = Offset(w * (0.9f - 0.2f * come), h * 0.42f)
        sirajMunir(light, w * 0.013f, f, t, nameAt = light + Offset(0f, -h * 0.12f))
        tagAt(a.tag("nobas"), Offset(bed.x, bed.y - h * 0.16f), f * seg(nb, 0.5f, 0.7f), back = Back54, ink = ArtInk.Gold)
    }
    if (sr > 0f) {
        val f = easeOut(seg(sr, 0f, BEAT_IN)) * (1f - seg(hp, 0f, BEAT_OUT))
        bedWith(f, seg(sr, 0.85f, 0.95f))
        sit12(Offset(w * 0.78f, floor), h * 0.28f, HealC54.Person, f, -1f)
        for (k in 0 until 7) {
            val g = seg(sr, 0.1f + 0.1f * k, 0.2f + 0.1f * k)
            val rr = w * (0.12f + 0.018f * k)
            drawOval(HealC54.Ring, Offset(bed.x - rr, bed.y - rr * 0.45f), Size(rr * 2f, rr * 0.9f), alpha = f * g * 0.5f, style = Stroke(1.3f * dp))
        }
    }
    if (hp > 0f) {
        val f = easeOut(seg(hp, 0f, BEAT_IN)) * (1f - seg(lw, 0f, BEAT_OUT))
        val p = Offset(w * 0.5f, floor)
        sit12(p, h * 0.32f, HealC54.Person, f, 1f)
        // his hand rests on the place of pain at his side: shoulder, elbow, hand, all joined to the body
        val tall = h * 0.32f
        val shoulder = p + Offset(-tall * 0.03f, -tall * 0.37f)
        val spot = p + Offset(tall * 0.07f, -tall * 0.2f)
        val elbow = shoulder + Offset(tall * 0.14f, tall * 0.06f)
        val sw = max(1.7f * dp, tall * 0.06f)
        drawLine(HealC54.Person, shoulder, elbow, sw, StrokeCap.Round, alpha = f)
        drawLine(HealC54.Person, elbow, spot, sw, StrokeCap.Round, alpha = f)
        softGlow(spot, w * 0.04f, ArtInk.Gold, 0.6f * f * (0.7f + 0.3f * sin(t * 5f)))
        tagAt(a.tag("handonpain"), spot + Offset(w * 0.16f, -h * 0.06f), f * seg(hp, 0.15f, 0.35f), ink = ArtInk.Gold)
        for (k in 0 until 10) {
            val g = seg(hp, 0.1f + 0.08f * k, 0.18f + 0.08f * k)
            val rr = w * (0.02f + 0.01f * (k % 5))
            drawCircle(if (k < 3) ArtInk.Gold else HealC54.Ring, rr, spot, alpha = f * bump(g) * 0.7f, style = Stroke(1.5f * dp))
        }
    }
    if (lw > 0f) {
        val f = easeOut(seg(lw, 0f, 0.3f))
        drawRoundRect(HealC54.Bed, Offset(bed.x - w * 0.2f, bed.y), Size(w * 0.4f, h * 0.05f), CornerRadius(4f * dp), alpha = f)
        val lamp = Offset(w * 0.2f, floor - h * 0.02f)
        drawOval(Color(0xFF8A5A3A), Offset(lamp.x - w * 0.025f, lamp.y - h * 0.012f), Size(w * 0.05f, h * 0.025f), alpha = f)
        val low = 1f - 0.6f * seg(lw, 0.1f, 0.6f)
        drawOval(HealC54.Gold, Offset(lamp.x - w * 0.005f, lamp.y - h * 0.05f * low), Size(w * 0.01f, h * 0.04f * low), alpha = f)
        fitBig26(a.big("lastword"), Offset(w * 0.5f, h * 0.3f), w * 0.45f, ArtInk.Gold, f * seg(lw, 0.25f, 0.5f), glow = ArtInk.Gold)
        val open = seg(lw, 0.6f, 0.95f)
        drawRect(HealC54.Garden, Offset(w * 0.78f, h * 0.3f), Size(w * 0.16f, floor - h * 0.3f), alpha = f * open)
        door12(Offset(w * 0.86f, floor), w * 0.1f, h * 0.4f, open, Color(0xFF5A4A3A), Color(0xFF3A2A1E), f, Color(0xFFDFF2B0))
    }
}

// ═════════════ 5. rain and wind (hisn-matar) ═════════════

private object RainC54 {
    val Sky = Color(0xFF8AA0B8)
    val SkyLow = Color(0xFFD8D4CC)
    val Dry = Color(0xFFC8A878)
    val Green = Color(0xFF5A9A5A)
    val Rain = Color(0xFF5A8AB0)
    val Cloud = Color(0xFFE4E8EE)
    val Dark = Color(0xFF5A5E6E)
    val Star = Color(0xFFE8E0C0)
    val Red = Color(0xFFC0482E)
    val Palm = Color(0xFF3E6A44)
    val Trunk = Color(0xFF6A4A32)
    val Wall = Color(0xFFDCC6A0)
    val Trim = Color(0xFF8A6A44)
    val Person = Color(0xFF3A3040)
    val Bolt = Color(0xFFF4EAB0)
}

/**
 * Beats: usefulrain (rain falls on dry ground and green springs up: «صَيِّبًا نَافِعًا»), notthestar (rain falls; a small
 * cluster of stars in the corner is struck out and the words shine: «بِفَضْلِ اللَّهِ وَرَحْمَتِهِ»), strongwind (a wind
 * bends the palms; it brings a raining cloud one way, a dust cloud the other), rainaround (the rain moves off the town onto
 * the hills round it, and the town dries in the sun), thunderhush (a flash and a roll of thunder; one who was talking
 * stops, and a soft glow of glorifying settles on him).
 */
internal fun DrawScope.rainwindScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "usefulrain" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "notthestar" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "strongwind" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "rainaround" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "thunderhush" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { rainField(a) }
}

private fun DrawScope.rainField(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ur = a.b("usefulrain")
    val ns = a.b("notthestar")
    val sw = a.b("strongwind")
    val ar = a.b("rainaround")
    val th = a.b("thunderhush")
    val ground = h * 0.82f
    daySky12(RainC54.Sky, RainC54.SkyLow, ground, RainC54.Dry)
    if (ur > 0f) {
        val f = easeOut(seg(ur, 0f, BEAT_IN)) * (1f - seg(ns, 0f, BEAT_OUT))
        cloud38(Offset(w * 0.5f, h * 0.16f), w * 0.2f, RainC54.Cloud, f)
        rain12(w * 0.15f, w * 0.85f, h * 0.22f, ground, t * 0.7f, RainC54.Rain, f * seg(ur, 0.1f, 0.25f), 36)
        val green = seg(ur, 0.3f, 0.95f)
        drawRect(RainC54.Green, Offset(0f, ground), Size(w, h * 0.04f * green), alpha = f)
        for (k in 0 until 24) drawLine(RainC54.Green, Offset(w * (0.03f + 0.04f * k), ground), Offset(w * (0.03f + 0.04f * k) + w * 0.004f * sin(t + k), ground - h * 0.07f * green * (0.6f + 0.4f * (k % 3) / 2f)), 2f * dp, StrokeCap.Round, alpha = f)
        tagAt(a.tag("usefulrain"), Offset(w * 0.5f, h * 0.36f), f * seg(ur, 0.15f, 0.3f), back = Back54, ink = ArtInk.Gold)
    }
    if (ns > 0f) {
        val f = easeOut(seg(ns, 0f, BEAT_IN)) * (1f - seg(sw, 0f, 0.4f))
        cloud38(Offset(w * 0.45f, h * 0.2f), w * 0.18f, RainC54.Cloud, f)
        rain12(w * 0.2f, w * 0.7f, h * 0.26f, ground, t * 0.7f, RainC54.Rain, f, 26)
        val sc = Offset(w * 0.86f, h * 0.18f)
        drawRect(Color(0xFF2A2E48), Offset(sc.x - w * 0.08f, sc.y - h * 0.1f), Size(w * 0.16f, h * 0.2f), alpha = f)
        for (k in 0 until 5) drawPath(starPath(sc + Offset(w * (0.05f * cos(k * 1.3f)), h * (0.06f * sin(k * 1.3f))), w * 0.008f), RainC54.Star, alpha = f)
        crossMark(sc, w * 0.06f, RainC54.Red, 3f * dp, f * seg(ns, 0.45f, 0.6f))
        tagAt(a.tag("notthestar"), Offset(w * 0.45f, h * 0.45f), f * seg(ns, 0.1f, 0.3f), back = Back54, ink = ArtInk.Gold)
    }
    if (sw > 0f) {
        val f = easeOut(seg(sw, 0f, 0.3f)) * (1f - seg(ar, 0f, BEAT_OUT))
        val gust = 0.5f + 0.5f * sin(t * 2.5f)
        for (k in 0 until 4) {
            val foot = Offset(w * (0.15f + 0.23f * k), ground)
            rotate(-12f * gust, foot) { palm12(foot, h * 0.4f, RainC54.Trunk, RainC54.Palm, f, t * 3f) }
        }
        val drift = fract(t * 0.08f)
        cloud38(Offset(w * (0.2f + 0.3f * drift), h * 0.18f), w * 0.12f, RainC54.Cloud, f)
        rain12(w * (0.12f + 0.3f * drift), w * (0.3f + 0.3f * drift), h * 0.24f, ground, t, RainC54.Rain, f * 0.8f, 14)
        cloud38(Offset(w * (0.8f - 0.2f * drift), h * 0.3f), w * 0.1f, Color(0xFFB09878), f * 0.8f)
        for (k in 0 until 10) {
            val y = h * (0.35f + 0.04f * k)
            val x = w * fract(t * 0.7f + k * 0.17f)
            drawLine(Color.White, Offset(x, y), Offset(x + w * 0.08f, y), 1.2f * dp, StrokeCap.Round, alpha = f * 0.5f)
        }
    }
    if (ar > 0f) {
        val f = easeOut(seg(ar, 0f, BEAT_IN)) * (1f - seg(th, 0f, BEAT_OUT))
        drawPath(Path().apply { moveTo(0f, ground); quadraticTo(w * 0.12f, h * 0.45f, w * 0.26f, ground); close() }, Color(0xFF8A9A6A), alpha = f)
        drawPath(Path().apply { moveTo(w * 0.74f, ground); quadraticTo(w * 0.88f, h * 0.45f, w, ground); close() }, Color(0xFF8A9A6A), alpha = f)
        for (k in 0 until 4) house12(Offset(w * (0.34f + 0.1f * k), ground), w * 0.09f, RainC54.Wall, RainC54.Trim, f)
        val move = easeInOut(seg(ar, 0.2f, 0.7f))
        for (side in listOf(-1f, 1f)) {
            val cx = w * 0.5f + side * w * 0.38f * move
            cloud38(Offset(cx, h * 0.2f), w * 0.1f, RainC54.Cloud, f)
            rain12(cx - w * 0.08f, cx + w * 0.08f, h * 0.25f, ground, t * 0.7f, RainC54.Rain, f, 14)
        }
        sun12(Offset(w * 0.5f, h * 0.15f), w * 0.035f, f * seg(ar, 0.6f, 0.9f))
    }
    if (th > 0f) {
        val f = easeOut(seg(th, 0f, 0.3f))
        drawRect(RainC54.Dark, Offset.Zero, Size(w, ground), alpha = f * 0.8f)
        val flash = bump(seg(th, 0.1f, 0.2f))
        drawRect(Color.White, alpha = f * 0.35f * flash)
        drawPath(Path().apply { moveTo(w * 0.7f, h * 0.05f); lineTo(w * 0.66f, h * 0.2f); lineTo(w * 0.7f, h * 0.2f); lineTo(w * 0.64f, h * 0.38f) }, RainC54.Bolt, alpha = f * flash, style = Stroke(3f * dp))
        figure(Offset(w * 0.3f, ground), h * 0.3f, RainC54.Person, f)
        figure(Offset(w * 0.46f, ground), h * 0.3f, RainC54.Person, f)
        val talking = 1f - seg(th, 0.2f, 0.3f)
        voice12(Offset(w * 0.33f, ground - h * 0.27f), w * 0.03f, t, Color.White, f * talking)
        softGlow(Offset(w * 0.3f, ground - h * 0.3f), w * 0.05f, ArtInk.Gold, 0.6f * f * seg(th, 0.35f, 0.6f))
    }
}
