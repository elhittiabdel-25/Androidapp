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
 * Wave 87 (2026-11-04): v1.4, the Quran 2: al-Nasr, al-Kafirun, al-Kawthar, al-Ma'un, al-Fil and Quraysh. The Prophet ﷺ
 * is only his named light, and no figure stands where he is spoken of; the people of Makkah are small faceless figures.
 */

// ═════════════ 6. tf-nasr: glad tidings, and a sign ═════════════

/**
 * Beats: gladtidings (dawn over the valley of Makkah; groups of people come in wave after wave toward the Kaaba:
 * «بِنَصْرِ اللَّهِ», «وَفَتْحِ مَكَّةَ», «أَفْوَاجًا»), tasbihistighfar (the valley quiet; two phrases rise as light,
 * «فَيُسَبِّحُ بِحَمْدِهِ», «وَيَسْتَغْفِرُهُ», over turning beads), sealedwith (two cards, prayer and hajj, each closed by a
 * gold seal of seeking forgiveness, while the sky turns to sunset: «تُخْتَمُ بِالِاسْتِغْفَارِ»), nasrgrows (rings of light
 * spread from Makkah over a wide land, mosques rising in them), subhanaka (a mihrab arch at night and the words he ﷺ
 * said in his bowing and prostration, with no figure).
 */
internal fun DrawScope.nasrdawnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "gladtidings" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "tasbihistighfar" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "sealedwith" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "nasrgrows" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "subhanaka" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { nasrDawnDraw(a) }
}

private fun DrawScope.nasrDawnDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val gt = fqIn(a, "gladtidings", "tasbihistighfar")
    val ts = fqIn(a, "tasbihistighfar", "sealedwith")
    val sw = fqIn(a, "sealedwith", "nasrgrows")
    val ng = fqIn(a, "nasrgrows", "subhanaka")
    val sb = fqIn(a, "subhanaka")
    val ground = h * 0.78f
    // dawn at first; the sky warms to sunset at the sign of his ﷺ term drawing near
    val dusk = seg(a.b("sealedwith"), 0.3f, 0.9f).coerceAtLeast(if (ng + sb > 0f) 1f else 0f)
    val top = lerpColor(Color(0xFF6E8FC4), Color(0xFF3A2E5A), dusk)
    val low = lerpColor(Color(0xFFF6D2A0), Color(0xFFE8885A), dusk)
    daySky12(top, low, ground, Color(0xFFB89A70))
    // the valley: two hill lines and the Kaaba in the middle
    farHills(Color(0xFF8A7050), low, ground, h * 0.12f, 0.3f)
    val kaaba = Offset(w * 0.5f, ground + h * 0.06f)
    val valley = gt + ts
    if (valley > 0f) {
        kaaba29(kaaba, w * 0.09f, valley.coerceAtMost(1f))
        softGlow(kaaba + Offset(0f, -h * 0.05f), w * 0.12f, Qn.GoldLight, 0.4f * valley.coerceAtMost(1f))
    }
    if (gt > 0f) {
        val p = a.b("gladtidings")
        // groups that come one after another from both sides, in waves
        for (g in 0 until 6) {
            val side = if (g % 2 == 0) -1f else 1f
            val start = 0.1f + 0.1f * g
            val f = easeOut(seg(p, start, start + 0.4f))
            if (f <= 0f) continue
            for (k in 0 until 3) {
                val x = kaaba.x + side * w * mix(0.55f, 0.12f + 0.05f * k + 0.04f * (g / 2), f)
                figure(Offset(x, ground + h * (0.1f + 0.04f * (g / 2))), h * 0.1f, Color(0xFF3A2E2A), gt * f, walk = if (f < 1f) t * 6f + k else 0f)
            }
        }
        tagAt(a.tag("gladtidings", 0, 0), Offset(w * 0.5f, h * 0.12f), gt * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("gladtidings", 0, 1), Offset(w * 0.5f, h * 0.24f), gt * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("gladtidings", 0, 2), Offset(w * 0.2f, h * 0.6f), gt * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ts > 0f) {
        val p = a.b("tasbihistighfar")
        beads12(Offset(w * 0.5f, h * 0.56f), w * 0.05f, Qn.GoldLight, ts, turn = t * 0.5f)
        for (k in 0 until 2) {
            val f = seg(p, 0.1f + 0.35f * k, 0.4f + 0.35f * k)
            val y = mix(h * 0.5f, h * (0.14f + 0.16f * k), easeOut(f))
            qFit(a.big("tasbihistighfar", 0, k), Offset(w * 0.5f, y), w * 0.6f, h * 0.13f, Color(0xFFFFF4D2), ts * f, glow = Qn.Gold)
        }
    }
    if (sw > 0f) {
        val p = a.b("sealedwith")
        drawRect(Color(0xFF1A1830), alpha = 0.55f * sw)
        val icons = listOf("mat", "kaaba")
        for (k in 0 until 2) {
            val y = h * (0.36f + 0.26f * k)
            val f = seg(p, 0.08f + 0.25f * k, 0.25f + 0.25f * k)
            glyphCard(Offset(w * 0.76f, y), w * 0.12f, a.tag("sealedwith", 0, k + 1), sw * f) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
            // the act runs to its end, and is closed with a gold seal
            val run = easeOut(seg(p, 0.2f + 0.25f * k, 0.45f + 0.25f * k))
            drawLine(Qn.GoldLight, Offset(w * 0.66f, y), Offset(w * mix(0.66f, 0.36f, run), y), 3f * density, StrokeCap.Round, alpha = sw * f)
            val seal = seg(p, 0.45f + 0.25f * k, 0.6f + 0.25f * k)
            if (seal > 0f) {
                qRosette(Offset(w * 0.3f, y), w * 0.045f * easeOut(seal), sw)
                softGlow(Offset(w * 0.3f, y), w * 0.06f, Qn.GoldLight, 0.5f * seal * sw)
            }
        }
        tagAt(a.tag("sealedwith", 0, 0), Offset(w * 0.3f, h * 0.14f), sw * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ng > 0f) {
        val p = a.b("nasrgrows")
        drawRect(Color(0xFF14243A), alpha = 0.92f * ng)
        // a wide land seen from above, and rings of light from Makkah spreading over it
        val c = Offset(w * 0.46f, h * 0.56f)
        for (k in 0 until 5) drawOval(Color(0xFF3A5A4A), Offset(w * (0.05f + 0.18f * k), h * (0.3f + 0.08f * (k % 2))), Size(w * 0.22f, h * 0.4f), alpha = ng * 0.8f)
        val grow = easeOut(seg(p, 0.05f, 0.85f))
        for (k in 0 until 4) {
            val r = w * 0.6f * fract(grow * 0.999f + k * 0.25f) * grow
            drawCircle(Qn.GoldLight, r, c, alpha = ng * 0.5f * (1f - r / (w * 0.6f)), style = Stroke(2f * density))
        }
        kaaba29(c + Offset(0f, h * 0.02f), w * 0.035f, ng)
        val spots = listOf(Offset(0.2f, 0.4f), Offset(0.74f, 0.36f), Offset(0.84f, 0.7f), Offset(0.12f, 0.72f), Offset(0.6f, 0.82f), Offset(0.32f, 0.84f))
        for ((k, s0) in spots.withIndex()) {
            val f = seg(p, 0.2f + 0.1f * k, 0.32f + 0.1f * k)
            mosque12(Offset(w * s0.x, h * s0.y), w * 0.06f, Color(0xFFE8DCC0), Qn.Gold, ng * f, lit = f)
        }
        tagAt(a.tag("nasrgrows", 0, 0), Offset(w * 0.5f, h * 0.12f), ng * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sb > 0f) {
        val p = a.b("subhanaka")
        drawRect(Color(0xFF0E1428), alpha = 0.95f * sb)
        // a mihrab arch, empty: the words alone
        val c = Offset(w * 0.5f, h * 0.9f)
        drawPath(archPath26(c.x, h * 0.12f, c.y, w * 0.2f), Color(0xFF1E2A44), alpha = sb)
        drawPath(archPath26(c.x, h * 0.12f, c.y, w * 0.2f), Qn.Gold, alpha = sb * 0.8f, style = Stroke(2f * density))
        softGlow(Offset(c.x, h * 0.45f), w * 0.25f, Qn.GoldLight, 0.25f * sb)
        for (k in 0 until 2) {
            val f = seg(p, 0.1f + 0.35f * k, 0.35f + 0.35f * k)
            qFit(a.big("subhanaka", 0, k), Offset(w * 0.5f, h * (0.34f + 0.2f * k)), w * 0.36f, h * 0.14f, Color(0xFFFFF4D2), sb * f, glow = Qn.Gold)
        }
    }
}

// ═════════════ 7. tf-kafirun: disowning shirk ═════════════

/**
 * Beats: disown (a gold line splits the picture: carved idols, a hung tree and a shrine fade grey on the left; on the
 * right his ﷺ named light, and two cards, the outward and the inward: «ظَاهِرًا», «وَبَاطِنًا»), mixedcup (a cup of clear
 * water takes one drop of ink and clouds through, and is crossed out: «الْمُقْتَرِنَةُ بِالشِّرْكِ», «لَا تُسَمَّى عِبَادَةً»),
 * twicesaid (a single mark on a line of time for the act, then words cut into stone for what has become their lasting
 * trait: «عَدَمِ وُجُودِ الْفِعْلِ», «صَارَ وَصْفًا لَازِمًا»), keybaraa (a door marked «التَّوْحِيدُ» opens only to a key marked
 * «الْبَرَاءَةِ»), purecup (the cup clear again and shining: «خَالِصَةً لِلَّهِ وَحْدَهُ»).
 */
internal fun DrawScope.baraaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "disown" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "mixedcup" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "twicesaid" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "keybaraa" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "purecup" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { baraaDraw(a) }
}

private fun DrawScope.baraaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val di = fqIn(a, "disown", "mixedcup")
    val mc = fqIn(a, "mixedcup", "twicesaid")
    val tw = fqIn(a, "twicesaid", "keybaraa")
    val kb = fqIn(a, "keybaraa", "purecup")
    val pc = fqIn(a, "purecup")
    // ink and parchment: a pale page with a fine border
    drawRect(Color(0xFFEDE2C6))
    drawRect(Qn.Gold, Offset(w * 0.02f, h * 0.03f), Size(w * 0.96f, h * 0.94f), alpha = 0.5f, style = Stroke(1.5f * density))
    val ground = h * 0.8f
    if (di > 0f) {
        val p = a.b("disown")
        val split = easeOut(seg(p, 0.05f, 0.3f))
        drawLine(Qn.Gold, Offset(w * 0.5f, h * 0.06f), Offset(w * 0.5f, h * (0.06f + 0.88f * split)), 3f * density, StrokeCap.Round, alpha = di)
        // the left: what they worshipped besides Allah, fading to grey
        val grey = seg(p, 0.3f, 0.6f)
        val col = lerpColor(Color(0xFF8A6A4A), Color(0xFFB8B0A4), grey)
        idol27(Offset(w * 0.12f, ground), h * 0.2f, col, di)
        idol27(Offset(w * 0.24f, ground), h * 0.14f, col, di)
        drawLine(lerpColor(Color(0xFF6A4A30), Color(0xFFB8B0A4), grey), Offset(w * 0.38f, ground), Offset(w * 0.38f, ground - h * 0.3f), 4f * density, alpha = di)
        drawCircle(lerpColor(Color(0xFF5A7A4A), Color(0xFFC8C2B6), grey), w * 0.06f, Offset(w * 0.38f, ground - h * 0.34f), alpha = di)
        for (k in 0 until 3) drawLine(lerpColor(Color(0xFFC0463A), Color(0xFFC8C2B6), grey), Offset(w * (0.35f + 0.03f * k), ground - h * 0.3f), Offset(w * (0.35f + 0.03f * k), ground - h * 0.22f), 2f * density, alpha = di)
        drawRect(Color(0xFFEDE2C6), Offset(0f, 0f), Size(w * 0.49f, h), alpha = 0.45f * grey * di)
        // the right: his ﷺ light, and the outward and the inward
        sirajMunir(Offset(w * 0.75f, h * 0.22f), w * 0.02f, di * seg(p, 0.1f, 0.3f), t, nameAt = Offset(w * 0.75f, h * 0.36f))
        val icons = listOf("hands", "heart")
        for (k in 0 until 2) glyphCard(Offset(w * (0.84f - 0.18f * k), h * 0.64f), w * 0.11f, a.tag("disown", 0, k), di * seg(p, 0.5f + 0.15f * k, 0.65f + 0.15f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
    }
    val cup = Offset(w * 0.5f, h * 0.7f)
    if (mc > 0f) {
        val p = a.b("mixedcup")
        val drop = seg(p, 0.15f, 0.35f)
        val cloud = easeOut(seg(p, 0.35f, 0.7f))
        cup12(cup, w * 0.2f, Color(0xFF8A7A6A), mc, lerpColor(Color(0xFFCFE8F4), Color(0xFF6A6A70), cloud))
        if (drop < 1f && drop > 0f) fqDrop(Offset(w * 0.5f, mix(h * 0.1f, h * 0.42f, drop)), w * 0.015f, Color(0xFF2A2430), mc)
        for (k in 0 until 4) if (cloud > 0f) drawCircle(Color(0xFF3A3440), w * 0.02f * cloud, cup + Offset(w * (-0.04f + 0.027f * k), -h * (0.12f + 0.03f * (k % 2))), alpha = 0.5f * mc)
        tagAt(a.tag("mixedcup", 0, 0), Offset(w * 0.5f, h * 0.14f), mc * seg(p, 0.3f, 0.45f), back = Color(0xE62A2430), ink = ArtInk.Ivory)
        val x = seg(p, 0.7f, 0.85f)
        if (x > 0f) cross14(cup + Offset(0f, -h * 0.12f), h * 0.14f, x)
        tagAt(a.tag("mixedcup", 0, 1), Offset(w * 0.8f, h * 0.5f), mc * seg(p, 0.75f, 0.9f), back = Color(0xE6701E1E), ink = ArtInk.Ivory)
    }
    if (tw > 0f) {
        val p = a.b("twicesaid")
        // first: one mark on the line of time — the act is not there now
        val y = h * 0.3f
        val run = easeOut(seg(p, 0.05f, 0.3f))
        drawLine(Qn.Ink, Offset(w * 0.9f, y), Offset(w * mix(0.9f, 0.1f, run), y), 2f * density, StrokeCap.Round, alpha = tw * 0.7f)
        val m = seg(p, 0.2f, 0.35f)
        if (m > 0f) crossMark(Offset(w * 0.6f, y), h * 0.04f, Color(0xFFC0463A), 3f * density, tw * m)
        tagAt(a.tag("twicesaid", 0, 0), Offset(w * 0.6f, y - h * 0.1f), tw * seg(p, 0.25f, 0.4f), back = Color(0xF0F3E6C4), ink = Qn.Ink)
        // then: cut into stone, for good — it has become their lasting trait
        val st = seg(p, 0.5f, 0.65f)
        val stone = Offset(w * 0.5f, h * 0.66f)
        drawRoundRect(Color(0xFF8A8478), Offset(stone.x - w * 0.3f, stone.y - h * 0.12f), Size(w * 0.6f, h * 0.24f), CornerRadius(6f * density), alpha = tw * st)
        drawRoundRect(Color(0xFF5A564E), Offset(stone.x - w * 0.3f, stone.y - h * 0.12f), Size(w * 0.6f, h * 0.24f), CornerRadius(6f * density), alpha = tw * st, style = Stroke(2f * density))
        val carve = seg(p, 0.6f, 0.9f)
        qFit(a.big("twicesaid", 0, 1), stone, w * 0.52f * carve.coerceAtLeast(0.01f), h * 0.16f, Color(0xFF2A2620), tw * carve)
    }
    if (kb > 0f) {
        val p = a.b("keybaraa")
        val door = Offset(w * 0.32f, ground + h * 0.08f)
        val open = easeOut(seg(p, 0.6f, 0.85f))
        door12(door, w * 0.16f, h * 0.5f, open, Color(0xFF8A6A40), Color(0xFF4A3420), kb)
        qFit(a.big("keybaraa", 0, 0), door + Offset(0f, -h * 0.58f), w * 0.3f, h * 0.1f, Qn.Ink, kb * seg(p, 0.05f, 0.2f))
        val reach = easeInOut(seg(p, 0.25f, 0.6f))
        val kp = Offset(mix(w * 0.78f, door.x + w * 0.06f, reach), mix(h * 0.5f, door.y - h * 0.22f, reach))
        key21(kp, w * 0.08f, Qn.Gold, kb * seg(p, 0.15f, 0.25f), rot = 0f)
        tagAt(a.tag("keybaraa", 0, 1), Offset(w * 0.74f, h * 0.36f), kb * seg(p, 0.2f, 0.35f), back = Color(0xF0F3E6C4), ink = Qn.Ink)
    }
    if (pc > 0f) {
        val p = a.b("purecup")
        val shine = seg(p, 0.15f, 0.5f)
        softGlow(cup + Offset(0f, -h * 0.12f), w * 0.2f, Qn.GoldLight, 0.6f * shine * pc)
        cup12(cup, w * 0.2f, Color(0xFF8A7A6A), pc, Color(0xFFDDF2FA))
        glint(cup + Offset(-w * 0.04f, -h * 0.16f), w * 0.04f, Color.White, pc * bump(shine))
        tagAt(a.tag("purecup", 0, 0), Offset(w * 0.5f, h * 0.16f), pc * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 8. tf-kawthar: abundant good ═════════════

/**
 * Beats: muchgood (gifts of light pour down without end over his ﷺ named light: «الْخَيْرَ الْكَثِيرَ», «وَالْفَضْلَ الْغَزِيرَ»),
 * riverhawd (a river winds down to a great square basin: «الْكَوْثَرُ», «الْحَوْضِ»), whitesweet (four cards round the basin:
 * whiter than milk, sweeter than honey, cups like the stars, never thirsty again), prayersacrifice (two cards, the
 * prayer and the sacrifice, made sincerely for his Lord: «الصَّلَاةَ», «وَالنَّحْرَ»), branchcut (far from the river a branch is
 * cut from its tree and dries out: «الْمَقْطُوعُ مِنْ كُلِّ خَيْرٍ»).
 */
internal fun DrawScope.kawtharriverScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "muchgood" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "riverhawd" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "whitesweet" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "prayersacrifice" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "branchcut" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { kawtharDraw(a) }
}

private fun DrawScope.kawtharDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val mg = fqIn(a, "muchgood", "riverhawd")
    val rh = fqIn(a, "riverhawd", "whitesweet")
    val ws = fqIn(a, "whitesweet", "prayersacrifice")
    val ps = fqIn(a, "prayersacrifice", "branchcut")
    val co = fqIn(a, "branchcut")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF0E2A3A), Color(0xFF1E4A50))))
    if (mg > 0f) {
        val p = a.b("muchgood")
        val c = Offset(w * 0.5f, h * 0.62f)
        sirajMunir(c, w * 0.025f, mg * seg(p, 0f, 0.2f), t, nameAt = c + Offset(0f, h * 0.14f))
        // gifts of light that keep coming down
        val pour = seg(p, 0.15f, 1f)
        for (k in 0 until 26) {
            val r = fract(sin(k * 7.13f) * 91.7f)
            val q = fract(r + t * (0.25f + 0.15f * r))
            val x = w * (0.2f + 0.6f * r)
            val pt = Offset(mix(x, c.x, q * q), mix(-h * 0.05f, c.y, q))
            if (k * 0.04f < pour) drawPath(starPath(pt, w * (0.008f + 0.006f * r)), Qn.GoldLight, alpha = mg * (1f - q * 0.6f))
        }
        tagAt(a.tag("muchgood", 0, 0), Offset(w * 0.3f, h * 0.2f), mg * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("muchgood", 0, 1), Offset(w * 0.7f, h * 0.3f), mg * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val basin = Offset(w * 0.5f, h * 0.68f)
    val bw = w * 0.36f
    val bh = h * 0.2f
    val land = rh + ws
    if (land > 0f) {
        val l = land.coerceAtMost(1f)
        val p = a.b("riverhawd")
        // a river that winds down from the top into the basin
        val flow = easeOut(seg(p, 0.05f, 0.5f)).coerceAtLeast(if (ws > 0f) 1f else 0f)
        val river = Path().apply {
            moveTo(w * 0.2f, -h * 0.02f)
            cubicTo(w * 0.7f, h * 0.15f, w * 0.15f, h * 0.35f, basin.x, basin.y - bh * 0.5f)
        }
        drawPath(river, Color(0xFFF4F8FA), alpha = 0.9f * l * flow, style = Stroke(w * 0.035f, cap = StrokeCap.Round))
        drawPath(river, Qn.GoldLight, alpha = 0.5f * l * flow, style = Stroke(w * 0.045f, cap = StrokeCap.Round))
        drawPath(river, Color(0xFFF4F8FA), alpha = 0.9f * l * flow, style = Stroke(w * 0.03f, cap = StrokeCap.Round))
        val bf = easeOut(seg(p, 0.35f, 0.6f)).coerceAtLeast(if (ws > 0f) 1f else 0f)
        drawRoundRect(Qn.Gold, Offset(basin.x - bw / 2f, basin.y - bh / 2f), Size(bw, bh), CornerRadius(6f * density), alpha = l * bf)
        drawRoundRect(Color(0xFFF4F8FA), Offset(basin.x - bw / 2f + w * 0.01f, basin.y - bh / 2f + w * 0.01f), Size(bw - w * 0.02f, bh - w * 0.02f), CornerRadius(4f * density), alpha = l * bf)
        tagAt(a.tag("riverhawd", 0, 0), Offset(w * 0.62f, h * 0.18f), rh * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("riverhawd", 0, 1), basin + Offset(0f, bh * 0.75f), rh * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ws > 0f) {
        val p = a.b("whitesweet")
        // cups like stars round the basin
        for (k in 0 until 14) {
            val an = k * 2f * PI.toFloat() / 14f
            val c = basin + Offset(cos(an) * bw * 0.62f, sin(an) * bh * 0.85f)
            val on = seg(p, 0.45f + 0.02f * k, 0.55f + 0.02f * k)
            drawPath(starPath(c, w * 0.012f), Qn.GoldLight, alpha = ws * on * (0.7f + 0.3f * sin(t * 3f + k)))
        }
        val spots = listOf(Offset(0.84f, 0.2f), Offset(0.84f, 0.5f), Offset(0.16f, 0.2f), Offset(0.16f, 0.5f))
        for (k in 0 until 4) {
            val f = seg(p, 0.05f + 0.2f * k, 0.2f + 0.2f * k)
            glyphCard(Offset(w * spots[k].x, h * spots[k].y), w * 0.1f, a.tag("whitesweet", 0, k), ws * f) { at, s, al ->
                when (k) {
                    0 -> cup12(at + Offset(0f, s * 0.4f), s * 0.8f, Color(0xFFB8B0A4), al, Color(0xFFFFFFFF))
                    1 -> {   // a jar of honey
                        drawRoundRect(Color(0xFFE8A030), at - Offset(s * 0.3f, s * 0.3f), Size(s * 0.6f, s * 0.7f), CornerRadius(s * 0.15f), alpha = al)
                        drawRect(Color(0xFF8A5A2A), at - Offset(s * 0.32f, s * 0.42f), Size(s * 0.64f, s * 0.14f), alpha = al)
                    }
                    2 -> for (m in 0 until 3) drawPath(starPath(at + Offset((m - 1) * s * 0.32f, if (m == 1) -s * 0.15f else s * 0.1f), s * 0.16f), Qn.GoldLight, alpha = al)
                    else -> {
                        fqDrop(at + Offset(-s * 0.12f, s * 0.12f), s * 0.18f, Fq.Water, al)
                        checkMark(at + Offset(s * 0.2f, 0f), s * 0.18f, Qn.Jade, 2f * density, al)
                    }
                }
            }
        }
    }
    if (ps > 0f) {
        val p = a.b("prayersacrifice")
        drawRect(Color(0xFF102030), alpha = 0.85f * ps)
        for (k in 0 until 2) {
            val f = seg(p, 0.1f + 0.25f * k, 0.3f + 0.25f * k)
            glyphCard(Offset(w * (0.64f - 0.28f * k), h * 0.56f), w * 0.15f, a.tag("prayersacrifice", 0, k), ps * f, lit = seg(p, 0.7f, 0.9f)) { at, s, al ->
                if (k == 0) qIcon("mat", at, s, Qn.GoldLight, al) else camel26(at + Offset(s * 0.1f, s * 0.45f), s * 0.8f, Color(0xFFD8C8A8), al, t)
            }
        }
        tagAt(a.tag("prayersacrifice", 0, 2), Offset(w * 0.5f, h * 0.16f), ps * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (co > 0f) {
        val p = a.b("branchcut")
        drawRect(Color(0xFF2A2420), alpha = 0.9f * co)
        val foot = Offset(w * 0.4f, h * 0.84f)
        drawRect(Color(0xFF6A5A48), Offset(0f, foot.y), Size(w, h - foot.y), alpha = co)
        // the tree, and a branch cut from it that dries
        drawLine(Color(0xFF5A4030), foot, foot + Offset(0f, -h * 0.45f), w * 0.025f, StrokeCap.Round, alpha = co)
        drawCircle(Qn.Leaf, w * 0.12f, foot + Offset(0f, -h * 0.5f), alpha = co)
        val cut = easeOut(seg(p, 0.15f, 0.4f))
        val dry = seg(p, 0.35f, 0.8f)
        val bA = foot + Offset(w * 0.02f, -h * 0.28f)
        val off = Offset(w * 0.25f * cut, h * 0.3f * cut * cut)
        drawLine(lerpColor(Color(0xFF5A4030), Color(0xFF8A7A6A), dry), bA + off, bA + off + Offset(w * 0.18f, -h * 0.08f), w * 0.012f, StrokeCap.Round, alpha = co)
        drawCircle(lerpColor(Qn.Leaf, Color(0xFF9A8A6A), dry), w * 0.045f, bA + off + Offset(w * 0.2f, -h * 0.1f), alpha = co * (1f - 0.5f * dry))
        tagAt(a.tag("branchcut", 0, 0), Offset(w * 0.68f, h * 0.2f), co * seg(p, 0.45f, 0.65f), back = Color(0xE6403020), ink = ArtInk.Ivory)
    }
}

// ═════════════ 9. tf-maun: the orphan, the prayer, small kindnesses ═════════════

/**
 * Beats: orphanhonor (at a doorway one puts a hand on a child's head and gives bread to a poor man: «إِكْرَامِ الْيَتِيمِ»,
 * «وَالْمَسَاكِينِ»), prayerguard (a clock's hand reaches the prayer and he is on his mat in time, his heart lit:
 * «وَالْمُحَافَظَةُ عَلَيْهَا», «الْإِخْلَاصِ»), lendsmall (over a garden wall between two houses a pot, a bucket and a book pass
 * from hand to hand: «الْإِنَاءِ», «وَالدَّلْوِ», «وَالْكِتَابِ»), sahun (the clock's hand goes past the prayer while the mat lies
 * empty and grey: «الْمُضَيِّعُونَ لَهَا», «التَّارِكُونَ لِوَقْتِهَا»), sahwfi (one at prayer forgets a little, a question
 * mark rises, and a soft green tick: «فَيَقَعُ مِنْ كُلِّ أَحَدٍ»).
 */
internal fun DrawScope.maunhouseScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "orphanhonor" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "prayerguard" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "lendsmall" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sahun" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "sahwfi" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { maunDraw(a) }
}

private fun DrawScope.maunDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val oh = fqIn(a, "orphanhonor", "prayerguard")
    val pg = fqIn(a, "prayerguard", "lendsmall")
    val ls = fqIn(a, "lendsmall", "sahun")
    val su = fqIn(a, "sahun", "sahwfi")
    val sf = fqIn(a, "sahwfi")
    val ground = h * 0.84f
    daySky12(Color(0xFFA8CBE4), Color(0xFFF3E4C6), ground, Color(0xFFC8AE84))
    val clock = Offset(w * 0.78f, h * 0.28f)
    if (oh > 0f) {
        val p = a.b("orphanhonor")
        door12(Offset(w * 0.2f, ground), w * 0.14f, h * 0.48f, 1f, Color(0xFF8A6A40), Color(0xFF4A3420), oh)
        val host = Offset(w * 0.4f, ground)
        figure(host, h * 0.32f, Color(0xFF3A3040), oh, arms = 0.5f)
        child12(Offset(w * 0.52f, ground), h * 0.18f, Color(0xFF5A4A60), oh)
        // the hand on the child's head
        val pat = easeOut(seg(p, 0.15f, 0.35f))
        drawLine(Color(0xFF3A3040), host + Offset(w * 0.01f, -h * 0.22f), host + Offset(mix(w * 0.04f, w * 0.11f, pat), -h * mix(0.2f, 0.19f, pat)), 2.5f * density, StrokeCap.Round, alpha = oh)
        softGlow(Offset(w * 0.52f, ground - h * 0.17f), w * 0.04f, Qn.GoldLight, 0.6f * pat * oh)
        // bread handed to a poor man
        val poor = Offset(w * 0.74f, ground)
        figure(poor, h * 0.28f, Color(0xFF7A6A6A), oh * seg(p, 0.4f, 0.55f), lean = 0.12f)
        val give = easeInOut(seg(p, 0.5f, 0.8f))
        drawOval(Color(0xFFD8A860), Offset(mix(w * 0.44f, w * 0.68f, give) - w * 0.025f, ground - h * 0.2f), Size(w * 0.05f, h * 0.03f), alpha = oh * seg(p, 0.45f, 0.5f))
        tagAt(a.tag("orphanhonor", 0, 0), Offset(w * 0.5f, h * 0.36f), oh * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("orphanhonor", 0, 1), Offset(w * 0.76f, h * 0.36f), oh * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    // the clock and the mat for the two beats on prayer
    fun clockAt(alpha: Float, hand: Float) {
        drawCircle(Color(0xFFFBF3D5), w * 0.07f, clock, alpha = alpha)
        drawCircle(Qn.Ink, w * 0.07f, clock, alpha = alpha, style = Stroke(2f * density))
        // the prayer's time: a gold mark at the top right
        drawLine(Qn.Gold, clock + Offset(cos(-0.9f), sin(-0.9f)) * (w * 0.05f), clock + Offset(cos(-0.9f), sin(-0.9f)) * (w * 0.068f), 4f * density, StrokeCap.Round, alpha = alpha)
        val an = -PI.toFloat() / 2f + hand * 2f * PI.toFloat()
        drawLine(Qn.Ink, clock, clock + Offset(cos(an), sin(an)) * (w * 0.055f), 2.5f * density, StrokeCap.Round, alpha = alpha)
    }
    if (pg > 0f) {
        val p = a.b("prayerguard")
        drawRect(Color(0xFFF3E4C6), alpha = 0.6f * pg)
        clockAt(pg, mix(0f, 0.105f, easeOut(seg(p, 0f, 0.4f))))
        val mat = Offset(w * 0.42f, ground)
        drawRoundRect(Qn.Rose, Offset(mat.x - w * 0.12f, mat.y - h * 0.015f), Size(w * 0.24f, h * 0.04f), CornerRadius(3f * density), alpha = pg)
        val come = easeInOut(seg(p, 0.05f, 0.35f))
        val foot = Offset(mix(w * 0.1f, mat.x, come), ground - h * 0.01f)
        figure(foot, h * 0.32f, Color(0xFF3A3040), pg, walk = if (come in 0.01f..0.99f) t * 6f else 0f)
        val lit = seg(p, 0.5f, 0.7f)
        softGlow(foot + Offset(0f, -h * 0.2f), w * 0.05f, Qn.GoldLight, lit * pg)
        heart12(foot + Offset(0f, -h * 0.2f), w * 0.025f, Qn.Gold, pg * lit, fill = true)
        tagAt(a.tag("prayerguard", 0, 0), Offset(w * 0.42f, h * 0.16f), pg * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("prayerguard", 0, 1), foot + Offset(-w * 0.16f, -h * 0.22f), pg * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ls > 0f) {
        val p = a.b("lendsmall")
        drawRect(Color(0xFFF3E4C6), alpha = 0.5f * ls)
        // two houses and the low wall between them
        for (k in 0 until 2) qIcon("house", Offset(w * (0.14f + 0.72f * k), ground - h * 0.16f), w * 0.22f, Color(0xFFE2CCA0), ls)
        drawRect(Color(0xFFB89A70), Offset(w * 0.3f, ground - h * 0.08f), Size(w * 0.4f, h * 0.08f), alpha = ls)
        val icons = listOf("cup", "bucket", "book")
        for (k in 0 until 3) {
            val f = easeInOut(seg(p, 0.1f + 0.25f * k, 0.35f + 0.25f * k))
            val x = mix(w * 0.72f, w * 0.28f, f)
            val y = ground - h * (0.42f + 0.06f * k) - h * 0.1f * bump(f)
            glyphCard(Offset(x, y), w * 0.1f, a.tag("lendsmall", 0, k), ls * seg(p, 0.08f + 0.25f * k, 0.15f + 0.25f * k)) { at, s, al ->
                if (k == 1) {   // a bucket
                    drawPath(Path().apply { moveTo(at.x - s * 0.3f, at.y - s * 0.25f); lineTo(at.x + s * 0.3f, at.y - s * 0.25f); lineTo(at.x + s * 0.22f, at.y + s * 0.35f); lineTo(at.x - s * 0.22f, at.y + s * 0.35f); close() }, Color(0xFF9A8A7A), alpha = al)
                    drawArc(Color(0xFF9A8A7A), 180f, 180f, false, at - Offset(s * 0.3f, s * 0.55f), Size(s * 0.6f, s * 0.6f), alpha = al, style = Stroke(1.5f * density))
                } else qIcon(icons[k], at, s, Qn.GoldLight, al)
            }
        }
    }
    if (su > 0f) {
        val p = a.b("sahun")
        drawRect(Color(0xFF4A4A58), alpha = 0.55f * su)
        clockAt(su, mix(0.05f, 0.5f, easeInOut(seg(p, 0.05f, 0.8f))))
        // the empty mat, grey, and one turned away, busy
        drawRoundRect(lerpColor(Qn.Rose, Color(0xFF8A8A90), seg(p, 0.3f, 0.6f)), Offset(w * 0.3f, ground - h * 0.015f), Size(w * 0.24f, h * 0.04f), CornerRadius(3f * density), alpha = su)
        figure(Offset(w * 0.72f, ground), h * 0.3f, Color(0xFF2A2430), su, lean = 0.15f)
        coin12(Offset(w * 0.62f, ground - h * 0.18f), w * 0.02f, su)
        tagAt(a.tag("sahun", 0, 0), Offset(w * 0.42f, h * 0.2f), su * seg(p, 0.15f, 0.3f), back = Color(0xE6403040), ink = ArtInk.Ivory)
        tagAt(a.tag("sahun", 0, 1), Offset(w * 0.42f, h * 0.32f), su * seg(p, 0.45f, 0.6f), back = Color(0xE6403040), ink = ArtInk.Ivory)
    }
    if (sf > 0f) {
        val p = a.b("sahwfi")
        drawRect(Color(0xFFF3E4C6), alpha = 0.6f * sf)
        val mat = Offset(w * 0.5f, ground)
        drawRoundRect(Qn.Rose, Offset(mat.x - w * 0.12f, mat.y - h * 0.015f), Size(w * 0.24f, h * 0.04f), CornerRadius(3f * density), alpha = sf)
        takbir80(mat + Offset(0f, -h * 0.01f), h * 0.34f, Color(0xFF3A3040), sf, 0f)
        val q = seg(p, 0.15f, 0.35f) * (1f - seg(p, 0.6f, 0.75f))
        qMark14(mat + Offset(w * 0.08f, -h * 0.42f), w * 0.04f, Color(0xFF8A7A9A), sf * q)
        val ok = seg(p, 0.6f, 0.8f)
        if (ok > 0f) checkMark(mat + Offset(w * 0.1f, -h * 0.42f), w * 0.03f, Qn.Jade, 3f * density, sf * ok)
        tagAt(a.tag("sahwfi", 0, 0), Offset(w * 0.5f, h * 0.14f), sf * seg(p, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 10. tf-fil: the year of the elephant, and Quraysh ═════════════

/**
 * Beats: yearborn (on a map, an elephant comes up from the south toward the Kaaba; birds come over, it turns back; then
 * a light rises at Makkah, his ﷺ name beside it: «عَامُ الْفِيلِ», «مِنْ مُقَدِّمَاتِ رِسَالَتِهِ»), forquraysh (the houses of
 * Makkah under a dome of safety: «لِأَجْلِ قُرَيْشٍ», «وَأَمْنِهِمْ»), twojourneys (a caravan goes south under a winter cloud and
 * north under the summer sun: «فِي الشِّتَاءِ إِلَى الْيَمَنِ», «وَفِي الصَّيْفِ إِلَى الشَّامِ»), worshiplord (both caravans come home
 * safe; the House shines: «رَبَّ هَذَا الْبَيْتِ»).
 */
internal fun DrawScope.filqurayshScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "yearborn" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "forquraysh" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "twojourneys" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "worshiplord" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { filDraw(a) }
}

private fun DrawScope.filDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val yb = fqIn(a, "yearborn", "forquraysh")
    val fq = fqIn(a, "forquraysh", "twojourneys")
    val tj = fqIn(a, "twojourneys", "worshiplord")
    val wl = fqIn(a, "worshiplord")
    // a map: sand, with Makkah in the middle, Yemen to the south (below), al-Sham to the north (above)
    drawRect(Color(0xFFE4CFA2))
    for (k in 0 until 6) drawOval(Color(0xFFD6BC8A), Offset(w * (0.05f + 0.16f * k), h * (0.1f + 0.13f * (k % 3))), Size(w * 0.2f, h * 0.12f), alpha = 0.6f)
    drawRect(Qn.Ink, Offset(w * 0.02f, h * 0.03f), Size(w * 0.96f, h * 0.94f), alpha = 0.35f, style = Stroke(1.5f * density))
    val makkah = Offset(w * 0.5f, h * 0.52f)
    kaaba29(makkah + Offset(0f, h * 0.05f), w * 0.06f, 1f)
    if (yb > 0f) {
        val p = a.b("yearborn")
        val come = easeInOut(seg(p, 0.05f, 0.4f))
        val back = easeInOut(seg(p, 0.5f, 0.75f))
        val ele = Offset(w * 0.5f, mix(h * 0.98f, h * 0.78f, come) + (h * 0.25f) * back)
        elephant32(ele, w * 0.07f, yb * (1f - seg(p, 0.7f, 0.8f)), 0f, t)
        for (k in 0 until 7) {
            val f = seg(p, 0.3f + 0.02f * k, 0.6f)
            val c = Offset(w * (0.3f + 0.06f * k), mix(-h * 0.05f, h * 0.62f, f) + sin(t * 3f + k) * h * 0.01f)
            fqBird(c, w * 0.015f, Color(0xFF3A3A4A), yb * f * (1f - seg(p, 0.7f, 0.8f)), t + k)
        }
        tagAt(a.tag("yearborn", 0, 0), Offset(w * 0.75f, h * 0.82f), yb * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        val born = seg(p, 0.75f, 0.9f)
        if (born > 0f) sirajMunir(makkah + Offset(-w * 0.12f, -h * 0.12f), w * 0.016f, yb * born, t, nameAt = makkah + Offset(-w * 0.12f, -h * 0.25f))
        tagAt(a.tag("yearborn", 0, 1), Offset(w * 0.24f, h * 0.82f), yb * seg(p, 0.85f, 0.95f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fq > 0f) {
        val p = a.b("forquraysh")
        for (k in 0 until 5) qIcon("house", makkah + Offset(w * (-0.16f + 0.08f * k), h * (0.12f + 0.03f * (k % 2))), w * 0.06f, Color(0xFFC8A878), fq)
        guardDome12(makkah + Offset(0f, h * 0.18f), w * 0.22f, seg(p, 0.15f, 0.45f), t, fq, Qn.GoldLight)
        tagAt(a.tag("forquraysh", 0, 0), Offset(w * 0.5f, h * 0.14f), fq * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("forquraysh", 0, 1), Offset(w * 0.5f, h * 0.86f), fq * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val trips = tj + wl
    if (trips > 0f) {
        val tr = trips.coerceAtMost(1f)
        val p = a.b("twojourneys")
        // the two roads, south and north
        drawLine(Qn.Soil, makkah + Offset(0f, h * 0.08f), Offset(w * 0.42f, h * 0.95f), 2f * density, alpha = 0.6f * tr)
        drawLine(Qn.Soil, makkah + Offset(0f, -h * 0.04f), Offset(w * 0.6f, h * 0.06f), 2f * density, alpha = 0.6f * tr)
        val home = seg(a.b("worshiplord"), 0.1f, 0.6f)
        val south = easeInOut(seg(p, 0.05f, 0.45f)) * (1f - home)
        val north = easeInOut(seg(p, 0.5f, 0.9f)) * (1f - home)
        camel26(Offset(mix(makkah.x, w * 0.43f, south), mix(makkah.y + h * 0.12f, h * 0.86f, south)), w * 0.05f, Color(0xFF6A4A30), tr, t, if (south in 0.01f..0.99f) t * 4f else 0f)
        camel26(Offset(mix(makkah.x, w * 0.59f, north), mix(makkah.y, h * 0.16f, north)), w * 0.05f, Color(0xFF6A4A30), tr * seg(p, 0.45f, 0.5f).coerceAtLeast(if (wl > 0f) 1f else 0f), t, if (north in 0.01f..0.99f) t * 4f else 0f)
        cloud38(Offset(w * 0.3f, h * 0.84f), w * 0.04f, Color(0xFF8A9AAA), tj * seg(p, 0.15f, 0.3f))
        sun12(Offset(w * 0.75f, h * 0.12f), w * 0.025f, tj * seg(p, 0.55f, 0.7f))
        tagAt(a.tag("twojourneys", 0, 0), Offset(w * 0.2f, h * 0.7f), tj * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("twojourneys", 0, 1), Offset(w * 0.8f, h * 0.28f), tj * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (wl > 0f) {
        val p = a.b("worshiplord")
        softGlow(makkah + Offset(0f, h * 0.02f), w * 0.2f, Qn.GoldLight, 0.7f * wl * seg(p, 0.3f, 0.6f))
        tagAt(a.tag("worshiplord", 0, 0), Offset(w * 0.5f, h * 0.2f), wl * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
