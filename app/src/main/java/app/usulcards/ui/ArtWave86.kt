package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 86 (2026-11-04): v1.4, the Quran 1: al-Fatiha (the Lord who nurtures; what the surah gathers), al-Falaq and
 * al-Nas, al-Ikhlas, and al-Masad. Allah is never drawn: His names are words, high in the picture. Abu Lahab and his
 * wife are names on dark plates, never figures.
 */

// ═════════════ 1. tf-fatiha-rabb: the Lord who nurtures ═════════════

/**
 * Beats: nurtureall (a seed grows to a flower, an egg to a bird, a lamb to a sheep, under sun and rain: «الْمُرَبِّي»),
 * generalcare (cards for creation, provision and guidance over everything that lives: «عَامَّةٌ»), specialcare (one
 * person's heart lights with faith and stepping stones light toward an open door: «بِالْإِيمَانِ», «لِكُلِّ خَيْرٍ»),
 * rabbduas (an open mushaf whose lines begin in gold with «رَبِّ» and «رَبَّنَا»), needaid (steps of prayer, fasting and
 * charity light one by one under a beam from above: «إِلَى عَوْنِ اللَّهِ»).
 */
internal fun DrawScope.rabbgardenScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "nurtureall" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "generalcare" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "specialcare" to Shot(Offset(w * 0.6f, h * 0.55f), 1.15f),
        "rabbduas" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "needaid" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { rabbGardenDraw(a) }
}

private fun DrawScope.rabbGardenDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val na = fqIn(a, "nurtureall", "generalcare")
    val gc = fqIn(a, "generalcare", "specialcare")
    val sc = fqIn(a, "specialcare", "rabbduas")
    val rd = fqIn(a, "rabbduas", "needaid")
    val nd = fqIn(a, "needaid")
    val ground = h * 0.8f
    daySky12(Qn.DayTop, Qn.DayLow, ground, Color(0xFF9DBA78))
    sun12(Offset(w * 0.86f, h * 0.14f), w * 0.04f, 1f)
    // the garden stays behind the first two beats
    val garden = na + gc * 0.8f
    if (garden > 0f) {
        val p = a.b("nurtureall")
        val g = easeOut(seg(p, 0.05f, 0.8f))
        rain12(w * 0.38f, w * 0.62f, h * 0.18f, ground, t, Color(0xFF8FB8DA), 0.6f * na * (1f - seg(p, 0.6f, 0.9f)))
        qPlant(Offset(w * 0.5f, ground), h * 0.32f, g, garden, t)
        // an egg that hatches into a bird that flies
        val nest = Offset(w * 0.2f, ground - h * 0.02f)
        qIcon("nest", nest, w * 0.1f, Qn.Soil, garden * (1f - seg(g, 0.5f, 0.7f)))
        fqBird(Offset(mix(nest.x, w * 0.28f, seg(g, 0.5f, 1f)), mix(nest.y - h * 0.05f, h * 0.3f, seg(g, 0.5f, 1f))), w * 0.035f, Color(0xFF3A3A4A), garden * seg(g, 0.45f, 0.6f), t)
        // a lamb that grows into a sheep
        sheep26(Offset(w * 0.8f, ground - h * 0.06f * mix(0.5f, 1f, g)), w * mix(0.06f, 0.12f, g), Color(0xFFF4F0E6), garden)
        tagAt(a.tag("nurtureall", 0, 0), Offset(w * 0.5f, h * 0.1f), na * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("nurtureall", 0, 1), Offset(w * 0.5f, h * 0.2f), na * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (gc > 0f) {
        val p = a.b("generalcare")
        // one wide arc over all that lives: the general nurturing
        val arc = easeOut(seg(p, 0.05f, 0.35f))
        drawArc(Qn.Gold, 200f, 140f * arc, false, Offset(w * 0.08f, h * 0.12f), androidx.compose.ui.geometry.Size(w * 0.84f, h * 1.1f), alpha = gc, style = Stroke(3f * density, cap = StrokeCap.Round))
        tagAt(a.tag("generalcare", 0, 0), Offset(w * 0.5f, h * 0.08f), gc * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        val icons = listOf("sprout", "wheat", "nest")
        for (k in 0 until 3) {
            val c = Offset(w * (0.72f - 0.22f * k), h * 0.34f)
            glyphCard(c, w * 0.1f, a.tag("generalcare", 0, k + 1), gc * seg(p, 0.3f + 0.15f * k, 0.45f + 0.15f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al, t) }
        }
    }
    if (sc > 0f) {
        val p = a.b("specialcare")
        drawRect(Color(0xFF1A2236), alpha = 0.55f * sc)
        val foot = Offset(w * 0.32f, ground)
        figure(foot, h * 0.3f, Qn.Person, sc)
        val heart = foot + Offset(0f, -h * 0.2f)
        val glow = easeOut(seg(p, 0.15f, 0.4f))
        softGlow(heart, w * 0.06f, Qn.GoldLight, glow * sc)
        heart12(heart, w * 0.035f, Qn.Gold, sc * glow, fill = true)
        tagAt(a.tag("specialcare", 0, 1), heart + Offset(-w * 0.13f, -h * 0.04f), sc * seg(p, 0.25f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
        // stepping stones light one after another toward an open door: every good thing made easy
        for (k in 0 until 4) {
            val c = Offset(w * (0.42f + 0.1f * k), ground - h * 0.01f)
            val on = seg(p, 0.4f + 0.08f * k, 0.5f + 0.08f * k)
            drawOval(lerpColor(Color(0xFF5A5A66), Qn.GoldLight, on), Offset(c.x - w * 0.035f, c.y - h * 0.012f), androidx.compose.ui.geometry.Size(w * 0.07f, h * 0.024f), alpha = sc)
            if (on > 0f) softGlow(c, w * 0.04f, Qn.GoldLight, 0.4f * on * sc)
        }
        val door = seg(p, 0.7f, 0.9f)
        door12(Offset(w * 0.86f, ground), w * 0.1f, h * 0.3f, door, Color(0xFF8A6A40), Color(0xFF4A3420), sc)
        tagAt(a.tag("specialcare", 0, 0), Offset(w * 0.5f, h * 0.1f), sc * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("specialcare", 0, 2), Offset(w * 0.86f, h * 0.36f), sc * seg(p, 0.75f, 0.9f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (rd > 0f) {
        val p = a.b("rabbduas")
        drawRect(Qn.Teal, alpha = 0.92f * rd)
        val c = Offset(w * 0.5f, h * 0.4f)
        openBook21(c, w * 0.78f, Qn.Parch, Qn.Ink, rd, glow = 0.4f)
        // out of the open book rise the words its duas begin with, «رَبِّ» and «رَبَّنَا», large and gold
        val spots = listOf(Offset(0.78f, 0.22f), Offset(0.58f, 0.14f), Offset(0.36f, 0.2f), Offset(0.2f, 0.12f), Offset(0.66f, 0.3f), Offset(0.28f, 0.32f))
        for (k in 0 until 6) {
            val f = easeOut(seg(p, 0.08f + 0.1f * k, 0.3f + 0.1f * k))
            val at = Offset(mix(c.x, w * spots[k].x, f), mix(c.y + h * 0.05f, h * spots[k].y, f))
            qFit(a.big("rabbduas", 0, k % 2), at, w * 0.2f, h * 0.12f, Qn.GoldLight, rd * f, glow = Qn.Gold)
        }
        tagAt(a.tag("rabbduas", 0, 2), Offset(w * 0.5f, h * 0.84f), rd * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nd > 0f) {
        val p = a.b("needaid")
        drawRect(Color(0xFF1A2236), alpha = 0.7f * nd)
        val icons = listOf("mat", "crescent", "coin")
        val src = Offset(w * 0.12f, h * 0.06f)
        // three steps up to the left, each an act of worship; each lights only when the help from above reaches it
        val rise = listOf(0.22f, 0.36f, 0.5f)
        for (k in 0 until 3) {
            val top = Offset(w * (0.74f - 0.22f * k), ground - h * rise[k])
            val lit = seg(p, 0.2f + 0.2f * k, 0.32f + 0.2f * k)
            drawRect(lerpColor(Color(0xFF4A4A58), Color(0xFF8A7448), lit), Offset(top.x - w * 0.11f, top.y), androidx.compose.ui.geometry.Size(w * 0.22f, ground - top.y), alpha = nd)
            drawLine(Qn.GoldLight, Offset(top.x - w * 0.11f, top.y), Offset(top.x + w * 0.11f, top.y), 2f * density, alpha = nd * lit)
            qBeam(src, top + Offset(0f, -h * 0.02f), lit, Qn.GoldLight, nd * 0.8f, 2f * density)
            // the card sits on the face of its step, clear of the one who climbs
            glyphCard(Offset(top.x, top.y + h * 0.11f), w * 0.09f, a.tag("needaid", 0, k + 1), nd * (0.4f + 0.6f * lit), lit = lit) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al, a.t) }
        }
        val climb = seg(p, 0.2f, 0.85f)
        val step = (climb * 3f).toInt().coerceAtMost(2)
        val onStep = climb >= 0.05f
        val foot = Offset(w * (0.78f - 0.22f * step), if (onStep) ground - h * rise[step] else ground)
        figure(Offset(if (onStep) foot.x else w * 0.94f, foot.y), h * 0.18f, Qn.Person, nd)
        tagAt(a.tag("needaid", 0, 0), Offset(w * 0.3f, h * 0.08f), nd * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. tf-fatiha-jami: what al-Fatiha gathers ═════════════

/**
 * Beats: shortholds (the surah's header and its seven verse rosettes, small, and a ring of light that grows far past
 * them: «مَا لَمْ تَحْتَوِ عَلَيْهِ سُورَةٌ»), fatihacontents (five cards fan out of it: lordship, worship, the names and
 * attributes, prophethood, just recompense), everyrakah (a straight road to a lit horizon, and four prayer mats that light
 * in turn: «فِي كُلِّ رَكْعَةٍ»), twohidayas (an arrow onto the road, then signposts along it: «إِلَى الصِّرَاطِ»,
 * «فِي الصِّرَاطِ», «عِلْمًا وَعَمَلًا»), ikhlasistiana (two cards, worship and seeking help, joined under one point of
 * light: «لِلَّهِ تَعَالَى»).
 */
internal fun DrawScope.fatihasevenScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "shortholds" to Shot(Offset(w * 0.5f, h * 0.48f), 1.05f),
        "fatihacontents" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "everyrakah" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "twohidayas" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "ikhlasistiana" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { fatihaSevenDraw(a) }
}

private fun DrawScope.fatihaSevenDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val sh = fqIn(a, "shortholds", "fatihacontents")
    val fc = fqIn(a, "fatihacontents", "everyrakah")
    val er = fqIn(a, "everyrakah", "twohidayas")
    val th = fqIn(a, "twohidayas", "ikhlasistiana")
    val ik = fqIn(a, "ikhlasistiana")
    // a teal page with a fine geometric border, like the frame of a mushaf's first page
    drawRect(Qn.Teal)
    for (k in 0 until 14) {
        val x = w * (k + 0.5f) / 14f
        drawPath(starPath(Offset(x, h * 0.035f), w * 0.016f), Qn.Gold, alpha = 0.35f)
        drawPath(starPath(Offset(x, h * 0.965f), w * 0.016f), Qn.Gold, alpha = 0.35f)
    }
    val plate = Offset(w * 0.5f, h * 0.36f)
    val plateA = sh + fc
    if (plateA > 0f) {
        val p = a.b("shortholds")
        qPlate(plate, w * 0.42f, a.big("shortholds", 0, 0), plateA.coerceAtMost(1f))
        for (k in 0 until 7) qRosette(Offset(w * (0.38f + 0.04f * k), h * 0.47f), w * 0.013f, plateA.coerceAtMost(1f) * seg(p, 0.1f + 0.04f * k, 0.18f + 0.04f * k))
        // a ring of light grows far past the small page: it holds what no other surah holds
        val ring = easeOut(seg(p, 0.45f, 0.95f))
        if (sh > 0f && ring > 0f) {
            for (k in 0 until 3) {
                val r = w * (0.12f + 0.3f * ring) * (1f - 0.18f * k)
                drawCircle(Qn.GoldLight, r, Offset(w * 0.5f, h * 0.42f), alpha = sh * (0.5f - 0.12f * k), style = Stroke(2f * density))
            }
            tagAt(a.tag("shortholds", 0, 1), Offset(w * 0.5f, h * 0.7f), sh * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    if (fc > 0f) {
        val p = a.b("fatihacontents")
        val icons = listOf("sun", "mat", "star", "scroll", "scale")
        val spots = listOf(Offset(0.82f, 0.2f), Offset(0.5f, 0.14f), Offset(0.18f, 0.2f), Offset(0.3f, 0.72f), Offset(0.7f, 0.72f))
        for (k in 0 until 5) {
            val f = easeOut(seg(p, 0.08f + 0.16f * k, 0.24f + 0.16f * k))
            val c = Offset(mix(plate.x, w * spots[k].x, f), mix(plate.y, h * spots[k].y, f))
            glyphCard(c, w * 0.1f, a.tag("fatihacontents", 0, k), fc * f, rim = if (k < 3) Qn.Gold else Qn.Lapis) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al, t) }
        }
    }
    val horizon = h * 0.42f
    if (er > 0f || th > 0f) {
        val road = er + th
        drawRect(Color(0xFF203A3C), Offset(0f, horizon), androidx.compose.ui.geometry.Size(w, h - horizon), alpha = road.coerceAtMost(1f))
        softGlow(Offset(w * 0.5f, horizon), w * 0.25f, Qn.GoldLight, 0.7f * road.coerceAtMost(1f))
        drawPath(Path().apply {
            moveTo(w * 0.48f, horizon); lineTo(w * 0.52f, horizon); lineTo(w * 0.66f, h); lineTo(w * 0.34f, h); close()
        }, Qn.ParchLow, alpha = road.coerceAtMost(1f))
        drawLine(Qn.Gold, Offset(w * 0.5f, horizon), Offset(w * 0.5f, h), 2f * density, alpha = 0.8f * road.coerceAtMost(1f))
    }
    if (er > 0f) {
        val p = a.b("everyrakah")
        val walk = seg(p, 0.1f, 0.9f)
        figure(Offset(w * 0.5f, mix(h * 0.95f, h * 0.7f, walk)), h * mix(0.2f, 0.14f, walk), Qn.Person, er, walk = t * 4f)
        tagAt(a.tag("everyrakah", 0, 0), Offset(w * 0.5f, h * 0.2f), er * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        for (k in 0 until 4) {
            val c = Offset(w * 0.12f + (k % 2) * w * 0.1f, h * (0.56f + (k / 2) * 0.2f))
            val on = seg(p, 0.3f + 0.12f * k, 0.4f + 0.12f * k)
            if (on > 0f) softGlow(c, w * 0.05f, Qn.GoldLight, 0.5f * on * er)
            qIcon("mat", c, w * 0.07f, Qn.GoldLight, er * (0.3f + 0.7f * on))
        }
        tagAt(a.tag("everyrakah", 0, 1), Offset(w * 0.17f, h * 0.44f), er * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (th > 0f) {
        val p = a.b("twohidayas")
        // onto the road
        val inA = easeOut(seg(p, 0.05f, 0.3f))
        val from = Offset(w * 0.12f, h * 0.9f)
        val to = Offset(w * 0.44f, h * 0.78f)
        drawLine(Qn.GoldLight, from, from + (to - from) * inA, 3f * density, StrokeCap.Round, alpha = th)
        if (inA > 0.9f) arrowHead(to, to - from, w * 0.03f, Qn.GoldLight, 3f * density, th)
        tagAt(a.tag("twohidayas", 0, 0), Offset(w * 0.2f, h * 0.76f), th * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        // along the road: the details, in knowledge and in deeds
        for (k in 0 until 3) {
            val y = h * (0.72f - 0.1f * k)
            val x = w * (0.6f - 0.025f * k)
            val on = seg(p, 0.35f + 0.1f * k, 0.45f + 0.1f * k)
            drawLine(Qn.Soil, Offset(x, y), Offset(x, y - h * 0.08f), 2f * density, alpha = th * on)
            drawRect(Qn.Gold, Offset(x, y - h * 0.08f), androidx.compose.ui.geometry.Size(w * 0.05f, h * 0.03f), alpha = th * on)
        }
        tagAt(a.tag("twohidayas", 0, 1), Offset(w * 0.78f, h * 0.56f), th * seg(p, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
        glyphCard(Offset(w * 0.86f, h * 0.76f), w * 0.09f, a.tag("twohidayas", 0, 2), th * seg(p, 0.65f, 0.8f)) { at, s, al -> qIcon("book", at, s, Qn.GoldLight, al) }
    }
    if (ik > 0f) {
        val p = a.b("ikhlasistiana")
        drawRect(Qn.Teal, alpha = 0.95f * ik)
        val top = Offset(w * 0.5f, h * 0.14f)
        val pt = easeOut(seg(p, 0.05f, 0.3f))
        softGlow(top, w * 0.1f * pt, Qn.GoldLight, ik)
        tagAt(a.tag("ikhlasistiana", 0, 0), top + Offset(0f, h * 0.02f), ik * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val icons = listOf("mat", "hands")
        for (k in 0 until 2) {
            val c = Offset(w * (0.36f + 0.28f * k), h * 0.62f)
            val f = seg(p, 0.3f + 0.15f * k, 0.5f + 0.15f * k)
            qBeam(c + Offset(0f, -h * 0.12f), top + Offset(0f, h * 0.06f), f, Qn.GoldLight, ik, 2f * density)
            glyphCard(c, w * 0.12f, a.tag("ikhlasistiana", 0, k + 1), ik * f, lit = seg(p, 0.7f, 0.9f)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
        }
    }
}

// ═════════════ 3. tf-nas: al-Falaq and al-Nas ═════════════

/**
 * Beats: allevils (daybreak splits the night; a dome of light over a person turns back three dark things outside it,
 * the knotted cord of magic among them: «الِاسْتِعَاذَةَ», «السِّحْرَ لَهُ حَقِيقَةٌ»), rabbmalikilah (three words high over
 * a crowd, «بِرَبِّ النَّاسِ», «وَمَالِكِهِمْ», «وَإِلَهِهِمْ», and below, the dark smoke at the root of every evil),
 * whisperretreat (smoke winds into a chest and swaps the signs of good and evil; the person remembers his Lord, rings
 * of gold go out, and the smoke shrinks back: «إِذَا ذَكَرَ الْعَبْدُ رَبَّهُ»), jinnins (whispers from a pale flame and
 * from a person who whispers: «مِنَ الْجِنِّ», «مِنَ الْإِنْسِ»).
 */
internal fun DrawScope.falaqnasScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "allevils" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "rabbmalikilah" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "whisperretreat" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "jinnins" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { falaqNasDraw(a) }
}

private fun DrawScope.falaqNasDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ae = fqIn(a, "allevils", "rabbmalikilah")
    val rm = fqIn(a, "rabbmalikilah", "whisperretreat")
    val wr = fqIn(a, "whisperretreat", "jinnins")
    val ji = fqIn(a, "jinnins")
    val ground = h * 0.82f
    val dawn = seg(a.b("allevils"), 0.05f, 0.4f)
    deepSky(t, ground, Qn.Night, Qn.NightLow, glow = Color(0xFFB07A5A), starAlpha = 1f - 0.5f * dawn, shine = 0.3f * dawn)
    // daybreak: a seam of light splits the dark at the horizon (al-falaq)
    softGlow(Offset(w * 0.5f, ground), w * 0.35f * dawn, Color(0xFFF2B27A), 0.8f * dawn)
    drawLine(Color(0xFFFFE2B0), Offset(w * (0.5f - 0.4f * dawn), ground), Offset(w * (0.5f + 0.4f * dawn), ground), 2.5f * density, alpha = dawn)
    drawRect(Color(0xFF151A24), Offset(0f, ground), androidx.compose.ui.geometry.Size(w, h - ground))
    val person = Offset(w * 0.5f, ground)
    if (ae > 0f) {
        val p = a.b("allevils")
        figure(person, h * 0.24f, Qn.Person, ae)
        val dome = seg(p, 0.2f, 0.45f)
        guardDome12(person, w * 0.17f, dome, t, ae)
        tagAt(a.tag("allevils", 0, 0), Offset(w * 0.5f, h * 0.36f), ae * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        // three dark things outside the dome, each pushed back as it comes near: a knotted cord, an envious eye, a dark cloud
        val things = listOf(Offset(0.16f, 0.58f), Offset(0.84f, 0.58f), Offset(0.78f, 0.24f))
        for (k in 0 until 3) {
            val in0 = seg(p, 0.3f + 0.1f * k, 0.45f + 0.1f * k)
            val back = bump(seg(p, 0.45f + 0.1f * k, 0.75f + 0.1f * k))
            val c0 = Offset(w * things[k].x, h * things[k].y)
            val c = c0 + (person + Offset(0f, -h * 0.12f) - c0) * (0.25f * back)
            when (k) {
                0 -> {   // a cord with knots: magic
                    drawLine(Color(0xFFB8A890), c + Offset(-w * 0.09f, -h * 0.07f), c + Offset(w * 0.09f, h * 0.07f), 4f * density, StrokeCap.Round, alpha = ae * in0)
                    for (n in 0 until 3) drawCircle(Color(0xFF8A7A6A), w * 0.016f, c + Offset(w * (-0.05f + 0.05f * n), h * (-0.04f + 0.04f * n)), alpha = ae * in0)
                }
                1 -> eye73(c, w * 0.12f, Color(0xFFC08AAA), ae * in0)
                else -> cloud38(c, w * 0.08f, Color(0xFF5A5468), ae * in0)
            }
        }
        tagAt(a.tag("allevils", 0, 1), Offset(w * 0.22f, h * 0.24f), ae * seg(p, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("allevils", 0, 2), Offset(w * 0.16f, h * 0.7f), ae * seg(p, 0.62f, 0.78f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (rm > 0f) {
        val p = a.b("rabbmalikilah")
        drawRect(Qn.Night, alpha = 0.5f * rm)
        // people: a crowd at the bottom
        for (k in 0 until 7) figure(Offset(w * (0.2f + 0.1f * k), ground + h * 0.02f * (k % 2)), h * (0.16f + 0.02f * (k % 3)), Qn.Person, rm * 0.85f)
        // three words high above them, one after another
        for (k in 0 until 3) {
            val c = Offset(w * (0.78f - 0.28f * k), h * 0.16f)
            val f = seg(p, 0.08f + 0.18f * k, 0.25f + 0.18f * k)
            qFit(a.big("rabbmalikilah", 0, k), c, w * 0.26f, h * 0.12f, Qn.GoldLight, rm * f, glow = Qn.Gold)
        }
        // the dark smoke at the root of every evil, low on the left
        val sm = seg(p, 0.6f, 0.9f)
        qSmoke(Offset(w * 0.02f, h * 0.7f), Offset(w * 0.26f, h * 0.5f), sm, t, rm, w * 0.025f)
        tagAt(a.tag("rabbmalikilah", 0, 3), Offset(w * 0.2f, h * 0.4f), rm * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (wr > 0f) {
        val p = a.b("whisperretreat")
        drawRect(Qn.Night, alpha = 0.6f * wr)
        figure(person, h * 0.34f, Qn.Person, wr)
        val chest = person + Offset(0f, -h * 0.22f)
        val creep = easeOut(seg(p, 0.05f, 0.4f))
        val retreat = easeOut(seg(p, 0.6f, 0.9f))
        qSmoke(Offset(w * 0.04f, h * 0.2f), chest, creep * (1f - 0.85f * retreat), t, wr, w * 0.025f)
        // the signs swap while the whisper is in: evil made to look fair, good made to look ugly
        val swap = seg(p, 0.25f, 0.45f) * (1f - retreat)
        tagAt(a.tag("whisperretreat", 0, 0), Offset(w * 0.24f, h * 0.14f), wr * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("whisperretreat", 0, 1), Offset(w * 0.78f, h * 0.3f), wr * swap, back = Color(0xE63A1A24), ink = ArtInk.Ivory)
        // he remembers his Lord: rings of gold from his mouth, and the smoke draws back
        voice12(person + Offset(-h * 0.03f, -h * 0.32f), w * 0.07f, t, Qn.GoldLight, wr * seg(p, 0.55f, 0.65f), -1f)
        softGlow(chest, w * 0.08f, Qn.GoldLight, wr * retreat)
        tagAt(a.tag("whisperretreat", 0, 2), Offset(w * 0.74f, h * 0.12f), wr * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ji > 0f) {
        val p = a.b("jinnins")
        drawRect(Qn.Night, alpha = 0.6f * ji)
        figure(person, h * 0.28f, Qn.Person, ji)
        val chest = person + Offset(0f, -h * 0.18f)
        // from the jinn: a pale smokeless flame on the left
        val jf = Offset(w * 0.16f, ground)
        jinnFlame20(jf, w * 0.13f, t, 0, ji * seg(p, 0.05f, 0.2f))
        // from people: someone whispering behind his hand on the right
        val hf = Offset(w * 0.84f, ground)
        figure(hf, h * 0.26f, Color(0xFF9A90A8), ji * seg(p, 0.35f, 0.5f), lean = -0.15f)
        for ((src, start) in listOf(jf + Offset(0f, -h * 0.12f) to 0.15f, hf + Offset(-w * 0.03f, -h * 0.22f) to 0.45f)) {
            val f = seg(p, start, start + 0.25f)
            for (k in 0 until 3) {
                val q = fract(f + k * 0.33f + t * 0.3f) * f
                val pt = src + (chest - src) * q
                drawArc(Color(0xFFC8C0E0), -40f, 80f, false, Offset(pt.x - w * 0.025f, pt.y - w * 0.025f), androidx.compose.ui.geometry.Size(w * 0.05f, w * 0.05f), alpha = ji * f * (1f - 0.6f * q), style = Stroke(3f * density))
            }
        }
        tagAt(a.tag("jinnins", 0, 0), Offset(w * 0.16f, h * 0.5f), ji * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("jinnins", 0, 1), Offset(w * 0.84f, h * 0.42f), ji * seg(p, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 4. tf-ikhlas: al-Ahad, al-Samad ═════════════

/**
 * Beats: ahadunique (a field of shapes, each lit beside its twin, and high above them one word alone, «الْأَحَدُ», with
 * «لَا نَظِيرَ لَهُ وَلَا مِثْلَ»), samadneed (lines of need rise from people, birds, beasts and plants below and from the
 * stars above toward one word, «الصَّمَدُ»), nochild (shapes below that come from one another, linked like generations;
 * the words above stand apart: «لِكَمَالِ غِنَاهُ»), asmathree (three names in a row, each over its card: knowledge,
 * forbearance, mercy over everything), asmasifat (all the names gather in a ring round «تَوْحِيدِ الْأَسْمَاءِ وَالصِّفَاتِ»).
 */
internal fun DrawScope.samadneedsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "ahadunique" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "samadneed" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "nochild" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "asmathree" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "asmasifat" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { samadNeedsDraw(a) }
}

private fun DrawScope.samadNeedsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ah = fqIn(a, "ahadunique", "samadneed")
    val sn = fqIn(a, "samadneed", "nochild")
    val nc = fqIn(a, "nochild", "asmathree")
    val tn = fqIn(a, "asmathree", "asmasifat")
    val am = fqIn(a, "asmasifat")
    drawRect(Color(0xFF0E2426))
    // a quiet geometric ground: a lattice of faint stars
    for (r in 0 until 4) for (c in 0 until 9) drawPath(starPath(Offset(w * (0.06f + 0.11f * c + (r % 2) * 0.055f), h * (0.56f + 0.12f * r)), w * 0.02f), Qn.TealLight, alpha = 0.25f)
    if (ah > 0f) {
        val p = a.b("ahadunique")
        // below: every made thing has its like; pairs of twins light up together
        val icons = listOf("star", "drop", "sprout", "moon")
        for (k in 0 until 4) {
            val on = seg(p, 0.4f + 0.1f * k, 0.5f + 0.1f * k)
            for (twin in 0 until 2) {
                val c = Offset(w * (0.14f + 0.22f * k + 0.07f * twin), h * 0.72f)
                qIcon(icons[k], c, w * 0.06f, Qn.GoldLight, ah * (0.3f + 0.7f * on), t)
            }
            if (on > 0f) drawLine(Qn.GoldLight, Offset(w * (0.14f + 0.22f * k), h * 0.8f), Offset(w * (0.21f + 0.22f * k), h * 0.8f), 1.5f * density, alpha = ah * on)
        }
        qFit(a.big("ahadunique", 0, 0), Offset(w * 0.5f, h * 0.17f), w * 0.4f, h * 0.16f, Qn.GoldLight, ah * seg(p, 0.05f, 0.25f), glow = Qn.Gold)
        tagAt(a.tag("ahadunique", 0, 1), Offset(w * 0.5f, h * 0.36f), ah * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sn > 0f) {
        val p = a.b("samadneed")
        val top = Offset(w * 0.5f, h * 0.14f)
        qFit(a.big("samadneed", 0, 0), top, w * 0.4f, h * 0.14f, Qn.GoldLight, sn * seg(p, 0.05f, 0.2f), glow = Qn.Gold)
        // the lower world: people, a bird, a beast, a plant; the upper world: stars. Lines of need rise from all toward the word
        val low = listOf(Offset(0.12f, 0.86f), Offset(0.3f, 0.88f), Offset(0.5f, 0.88f), Offset(0.7f, 0.86f), Offset(0.88f, 0.86f))
        for ((k, s0) in low.withIndex()) {
            val c = Offset(w * s0.x, h * s0.y)
            when (k) {
                0, 3 -> figure(c, h * 0.13f, Qn.Person, sn)
                1 -> fqBird(c + Offset(0f, -h * 0.08f), w * 0.03f, Qn.Person, sn, t)
                2 -> sheep26(c + Offset(0f, -h * 0.04f), w * 0.07f, Qn.Person, sn)
                else -> qPlant(c, h * 0.12f, 1f, sn, t)
            }
            val f = seg(p, 0.25f + 0.06f * k, 0.5f + 0.06f * k)
            qBeam(c + Offset(0f, -h * 0.15f), top + Offset(0f, h * 0.06f), f, Qn.GoldLight, sn * 0.6f, 1.2f * density)
        }
        for (k in 0 until 4) {
            val c = Offset(w * (0.1f + 0.27f * k), h * 0.36f)
            drawPath(starPath(c, w * 0.012f), Color(0xFFDDE6FF), alpha = sn)
            qBeam(c, top + Offset(0f, h * 0.05f), seg(p, 0.55f + 0.05f * k, 0.75f + 0.05f * k), Color(0xFFDDE6FF), sn * 0.5f, 1.2f * density)
        }
        tagAt(a.tag("samadneed", 0, 1), Offset(w * 0.5f, h * 0.28f), sn * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("samadneed", 0, 2), Offset(w * 0.5f, h * 0.55f), sn * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nc > 0f) {
        val p = a.b("nochild")
        // below: made things come from one another, generation after generation
        for (k in 0 until 4) {
            val f = seg(p, 0.1f + 0.15f * k, 0.25f + 0.15f * k)
            val c = Offset(w * (0.16f + 0.22f * k), h * (0.82f - 0.06f * k))
            qPlant(c, h * (0.08f + 0.03f * k), f, nc, t)
            if (k > 0) drawLine(Qn.Leaf, Offset(w * (0.16f + 0.22f * (k - 1)), h * (0.82f - 0.06f * (k - 1))), c, 1.2f * density, alpha = nc * f * 0.6f)
        }
        // above, apart from all of it
        qFit(a.big("nochild", 0, 0), Offset(w * 0.5f, h * 0.16f), w * 0.5f, h * 0.13f, Qn.GoldLight, nc * seg(p, 0.05f, 0.2f), glow = Qn.Gold)
        tagAt(a.tag("nochild", 0, 1), Offset(w * 0.5f, h * 0.32f), nc * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tn > 0f) {
        val p = a.b("asmathree")
        val icons = listOf("book", "hourglass", "cloud")
        for (k in 0 until 3) {
            val x = w * (0.78f - 0.28f * k)
            val f = seg(p, 0.05f + 0.25f * k, 0.25f + 0.25f * k)
            qFit(a.big("asmathree", 0, k), Offset(x, h * 0.16f), w * 0.24f, h * 0.12f, Qn.GoldLight, tn * f, glow = Qn.Gold)
            glyphCard(Offset(x, h * 0.48f), w * 0.11f, a.tag("asmathree", 0, k + 3), tn * seg(p, 0.12f + 0.25f * k, 0.3f + 0.25f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al, t) }
        }
        // mercy over everything: a wide arc over the field below
        val arc = seg(p, 0.7f, 0.95f)
        drawArc(Qn.GoldLight, 200f, 140f * arc, false, Offset(w * 0.05f, h * 0.62f), androidx.compose.ui.geometry.Size(w * 0.9f, h * 0.7f), alpha = tn * 0.8f, style = Stroke(2.5f * density, cap = StrokeCap.Round))
    }
    if (am > 0f) {
        val p = a.b("asmasifat")
        val c = Offset(w * 0.5f, h * 0.47f)
        val names = listOf(a.big("ahadunique"), a.big("samadneed"), a.big("asmathree", 0, 0), a.big("asmathree", 0, 1), a.big("asmathree", 0, 2))
        for ((k, tl) in names.withIndex()) {
            val an = -PI.toFloat() / 2f + k * 2f * PI.toFloat() / 5f
            val f = easeOut(seg(p, 0.05f + 0.08f * k, 0.3f + 0.08f * k))
            val at = c + Offset(cos(an) * w * 0.3f, sin(an) * h * 0.34f) * f
            qFit(tl, at, w * 0.18f, h * 0.1f, Qn.GoldLight, am * f)
        }
        drawCircle(Qn.Gold, w * 0.16f, c, alpha = am * seg(p, 0.5f, 0.7f) * 0.6f, style = Stroke(1.5f * density))
        tagAt(a.tag("asmasifat", 0, 0), c, am * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. tf-masad: a sign foretold ═════════════

/**
 * Beats: revealedalive (a page comes down in light; two dark name plates, Abu Lahab and his wife, each with a beating
 * line of life: «حَيَّانِ لَمْ يَهْلِكَا»), foretoldtwo (two lines are written on the page: «سَيُعَذَّبَانِ فِي النَّارِ وَلَا بُدَّ»,
 * «أَنَّهُمَا لَا يُسْلِمَانِ»), cametrue (years pass, the plates' lines go flat, both lines on the page are ticked:
 * «فَمَاتَا عَلَى الْكُفْرِ», «دَلِيلًا عَلَى صِدْقِ الْقُرْآنِ»), wealthnohelp (coins and a family line are heaped on one pan
 * and weigh nothing: «مَالُهُ», «وَلَا كَسْبُهُ»), faithsaves (a heart and a hand of good deeds bring the other pan down:
 * «بِالْإِيمَانِ وَالْعَمَلِ»).
 */
internal fun DrawScope.masadsignScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "revealedalive" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "foretoldtwo" to Shot(Offset(w * 0.38f, h * 0.48f), 1.1f),
        "cametrue" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "wealthnohelp" to Shot(Offset(w * 0.5f, h * 0.52f), 1f),
        "faithsaves" to Shot(Offset(w * 0.5f, h * 0.52f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { masadSignDraw(a) }
}

private fun DrawScope.masadSignDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ra = fqIn(a, "revealedalive", "foretoldtwo")
    val ft = fqIn(a, "foretoldtwo", "cametrue")
    val ct = fqIn(a, "cametrue", "wealthnohelp")
    val wn = fqIn(a, "wealthnohelp", "faithsaves")
    val fs = fqIn(a, "faithsaves")
    drawRect(Color(0xFF1A1622))
    softGlow(Offset(w * 0.3f, 0f), w * 0.5f, Qn.GoldLight, 0.25f)
    val page = Offset(w * 0.3f, h * 0.48f)
    val story = ra + ft + ct
    if (story > 0f) {
        val s = story.coerceAtMost(1f)
        val down = easeOut(seg(a.b("revealedalive"), 0f, 0.3f))
        qBeam(Offset(w * 0.3f, 0f), page + Offset(0f, -h * 0.3f), down, Qn.GoldLight, s * (1f - seg(a.b("foretoldtwo"), 0f, 0.5f)), 3f * density)
        qPage(Offset(page.x, mix(h * 0.2f, page.y, down)), w * 0.34f, h * 0.56f, s, lines = 6)
        // the two plates, each with a line of life
        val flat = seg(a.b("cametrue"), 0.2f, 0.6f)
        for (k in 0 until 2) {
            val c = Offset(w * 0.76f, h * (0.3f + 0.3f * k))
            val dark = lerpColor(Color(0xFF3A3340), Color(0xFF1E1A24), flat)
            qNamePlate(c, a.tag("revealedalive", 0, k), s * (1f - 0.5f * flat), fill = dark)
            val y = c.y + h * 0.1f
            val pts = (0..20).map { i ->
                val x = c.x - w * 0.1f + w * 0.2f * i / 20f
                val ph = fract(i / 20f - t * 0.6f)
                Offset(x, y - (1f - flat) * h * 0.04f * (if (ph in 0.45f..0.55f) sin((ph - 0.45f) * 10f * PI.toFloat()) else 0f))
            }
            for (i in 0 until pts.size - 1) drawLine(lerpColor(Qn.Jade, Color(0xFF6A6070), flat), pts[i], pts[i + 1], 1.6f * density, alpha = s)
        }
        tagAt(a.tag("revealedalive", 0, 2), Offset(w * 0.76f, h * 0.1f), ra * seg(a.b("revealedalive"), 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (ft > 0f || ct > 0f) {
        val p = a.b("foretoldtwo")
        val wr = ft + ct
        for (k in 0 until 2) {
            val y = page.y + h * (-0.08f + 0.16f * k)
            val f = seg(p, 0.1f + 0.35f * k, 0.4f + 0.35f * k)
            tagAt(a.tag("foretoldtwo", 0, k), Offset(page.x, y), wr.coerceAtMost(1f) * f, back = Color(0xF0F3E6C4), ink = Qn.Ink)
            val tick = seg(a.b("cametrue"), 0.45f + 0.15f * k, 0.6f + 0.15f * k)
            if (tick > 0f) tick14(Offset(page.x + w * 0.19f, y), w * 0.025f, tick)
        }
    }
    if (ct > 0f) {
        val p = a.b("cametrue")
        // the years pass: a sun and a moon go round over the plates
        val turn = seg(p, 0f, 0.4f)
        for (k in 0 until 3) {
            val an = PI.toFloat() * (1f + fract(turn * 2f + k / 3f))
            val c = Offset(w * 0.76f + cos(an) * w * 0.14f, h * 0.45f + sin(an) * h * 0.35f)
            if (k == 0) sun12(c, w * 0.015f, ct * (1f - seg(p, 0.4f, 0.5f))) else drawCircle(Color(0xFFDDE6FF), w * 0.01f, c, alpha = ct * (1f - seg(p, 0.4f, 0.5f)))
        }
        tagAt(a.tag("cametrue", 0, 0), Offset(w * 0.76f, h * 0.88f), ct * seg(p, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
        softGlow(page, w * 0.3f, Qn.GoldLight, 0.5f * ct * seg(p, 0.75f, 0.95f))
        tagAt(a.tag("cametrue", 0, 1), Offset(page.x, h * 0.1f), ct * seg(p, 0.75f, 0.9f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val scale = wn + fs
    if (scale > 0f) {
        val s = scale.coerceAtMost(1f)
        drawRect(Color(0xFF1A1622), alpha = 0.9f * s)
        val foot = Offset(w * 0.5f, h * 0.86f)
        val sz = h * 0.55f
        // wealth and kinship crumble to nothing on the right pan; faith and deeds bring the left pan down
        val tilt = -0.8f * seg(a.b("faithsaves"), 0.1f, 0.5f)
        balance12(foot, sz, tilt, Qn.GoldLight, s)
        val (l, r) = balancePans12(foot, sz, tilt)
        // the wealth and the kinship stay in view, dulled: they are there and avail him nothing
        val dull = seg(a.b("wealthnohelp"), 0.55f, 0.9f).let { if (fs > 0f) 1f else it }
        val pw = (wn + fs).coerceAtMost(1f)
        fqCoins(r + Offset(-w * 0.03f, -h * 0.01f), w * 0.022f, 4, pw, lerpColor(Fq.Gold, Color(0xFF8A8494), dull))
        chain72(r + Offset(w * 0.0f, -h * 0.02f), r + Offset(w * 0.08f, -h * 0.06f), w * 0.02f, Color(0xFFB8AFA0), pw)
        tagAt(a.tag("wealthnohelp", 0, 0), r + Offset(w * 0.12f, -h * 0.2f), wn * seg(a.b("wealthnohelp"), 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("wealthnohelp", 0, 1), r + Offset(w * 0.12f, -h * 0.32f), wn * seg(a.b("wealthnohelp"), 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
        if (fs > 0f) {
            val p = a.b("faithsaves")
            val inn = seg(p, 0f, 0.15f)
            heart12(l + Offset(-w * 0.04f, -h * 0.06f), w * 0.07f, Qn.Rose, fs * inn, fill = true)
            qIcon("hands", l + Offset(w * 0.045f, -h * 0.07f), w * 0.08f, Qn.GoldLight, fs * inn)
            softGlow(l, w * 0.08f, Qn.GoldLight, 0.5f * fs * seg(p, 0.4f, 0.6f))
            tagAt(a.tag("faithsaves", 0, 0), Offset(w * 0.5f, h * 0.1f), fs * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
            tagAt(a.tag("faithsaves", 0, 1), r + Offset(0f, -h * 0.18f), fs * seg(p, 0.55f, 0.7f), back = Color(0xE63A1A24), ink = ArtInk.Ivory)
        }
    }
}
