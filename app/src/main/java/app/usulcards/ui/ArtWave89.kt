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
 * Wave 89 (2026-11-04): v1.4, the Quran 4: al-Bayyina, al-Qadr, al-'Alaq, al-Tin, al-Sharh. The Prophet ﷺ and the other
 * prophets are named lights; no figure stands beside them. The odd nights are written in Western digits.
 */

// ═════════════ 16. tf-bayyina: the clear proof ═════════════

/**
 * Beats: messengerbook (his ﷺ named light and an open book; threads go out from them and dim hearts light up one by
 * one: «الْبَيِّنَةَ», «يُعَلِّمُهُمُ الْحِكْمَةَ وَيُزَكِّيهِمْ»), purepages (pages inside a dome of light; dark smoke comes and
 * falls back; two cards, true reports and just commands), seekerclear (a light rises between two people: one walks into
 * it, the other turns away into shadow: «طَالِبُ الْحَقِّ», «مِمَّنْ لَيْسَ لَهُ مَقْصِدٌ فِي طَلَبِهِ»), darknesslight (people walk
 * from layers of dark through an open book into light: «مِنَ الظُّلُمَاتِ», «إِلَى النُّورِ»).
 */
internal fun DrawScope.bayyinascrollScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "messengerbook" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "purepages" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "seekerclear" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "darknesslight" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { bayyinaDraw(a) }
}

private fun DrawScope.bayyinaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val mb = fqIn(a, "messengerbook", "purepages")
    val pp = fqIn(a, "purepages", "seekerclear")
    val sc = fqIn(a, "seekerclear", "darknesslight")
    val dl = fqIn(a, "darknesslight")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF101A30), Color(0xFF1E2C44))))
    if (mb > 0f) {
        val p = a.b("messengerbook")
        val src = Offset(w * 0.3f, h * 0.3f)
        sirajMunir(src, w * 0.022f, mb * seg(p, 0f, 0.2f), t, nameAt = src + Offset(0f, -h * 0.13f))
        openBook21(Offset(w * 0.3f, h * 0.48f), w * 0.22f, Qn.Parch, Qn.Ink, mb * seg(p, 0.1f, 0.25f), glow = 0.5f)
        for (k in 0 until 6) {
            val c = Offset(w * (0.6f + 0.13f * (k % 3)), h * (0.36f + 0.26f * (k / 3)))
            val f = seg(p, 0.3f + 0.08f * k, 0.5f + 0.08f * k)
            qBeam(Offset(w * 0.36f, h * 0.5f), c, f, Qn.GoldLight, mb * 0.6f, 1.2f * density)
            heart12(c, w * 0.05f, lerpColor(Color(0xFF6A6070), Qn.Rose, f), mb, fill = true)
            if (f > 0.9f) softGlow(c, w * 0.04f, Qn.GoldLight, 0.4f * mb)
        }
        tagAt(a.tag("messengerbook", 0, 0), Offset(w * 0.3f, h * 0.72f), mb * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("messengerbook", 0, 1), Offset(w * 0.72f, h * 0.16f), mb * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (pp > 0f) {
        val p = a.b("purepages")
        val c = Offset(w * 0.5f, h * 0.62f)
        qPage(c + Offset(-w * 0.04f, -h * 0.1f), w * 0.14f, h * 0.24f, pp, lines = 5)
        qPage(c + Offset(w * 0.04f, -h * 0.08f), w * 0.14f, h * 0.24f, pp, lines = 5)
        guardDome12(c + Offset(0f, h * 0.1f), w * 0.2f, seg(p, 0.1f, 0.3f), t, pp, Qn.GoldLight)
        // dark smoke comes near and falls back
        for (k in 0 until 2) {
            val near = bump(seg(p, 0.3f + 0.2f * k, 0.7f + 0.2f * k))
            val from = Offset(if (k == 0) w * 0.08f else w * 0.92f, h * 0.3f)
            smoke12(from, from + (c - from) * 0.45f, near, t, pp)
        }
        tagAt(a.tag("purepages", 0, 0), Offset(w * 0.5f, h * 0.1f), pp * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("purepages", 0, 1), Offset(w * 0.5f, h * 0.22f), pp * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Ivory)
        val icons = listOf("check", "scale")
        for (k in 0 until 2) glyphCard(Offset(w * (0.86f - 0.72f * k), h * 0.62f), w * 0.11f, a.tag("purepages", 0, k + 2), pp * seg(p, 0.6f + 0.15f * k, 0.75f + 0.15f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
    }
    val ground = h * 0.86f
    if (sc > 0f) {
        val p = a.b("seekerclear")
        val lc = Offset(w * 0.5f, h * 0.3f)
        val rise = easeOut(seg(p, 0.05f, 0.3f))
        softGlow(lc, w * 0.25f * rise, Qn.GoldLight, sc)
        drawCircle(Color(0xFFFFF4D2), w * 0.03f * rise, lc, alpha = sc)
        val go = easeInOut(seg(p, 0.3f, 0.8f))
        figure(Offset(mix(w * 0.72f, w * 0.56f, go), ground), h * 0.24f, Qn.Person, sc, walk = if (go in 0.01f..0.99f) t * 6f else 0f)
        softGlow(Offset(mix(w * 0.72f, w * 0.56f, go), ground - h * 0.12f), w * 0.06f, Qn.GoldLight, 0.5f * go * sc)
        val away = easeInOut(seg(p, 0.45f, 0.9f))
        figure(Offset(mix(w * 0.36f, w * 0.12f, away), ground), h * 0.24f, lerpColor(Qn.Person, Color(0xFF4A4A58), away), sc, walk = if (away in 0.01f..0.99f) t * 6f else 0f)
        tagAt(a.tag("seekerclear", 0, 0), Offset(w * 0.72f, h * 0.5f), sc * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("seekerclear", 0, 1), Offset(w * 0.24f, h * 0.5f), sc * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (dl > 0f) {
        val p = a.b("darknesslight")
        // layers of dark on the right, light on the left, an open book in between
        for (k in 0 until 3) drawRect(Color(0xFF05070E), Offset(w * (0.62f + 0.12f * k), 0f), Size(w * (0.38f - 0.12f * k), h), alpha = 0.35f * dl)
        drawRect(Brush.horizontalGradient(listOf(Color(0xFFFFF2CC), Color(0x00FFF2CC)), 0f, w * 0.45f), alpha = 0.7f * dl * seg(p, 0.2f, 0.5f))
        openBook21(Offset(w * 0.5f, h * 0.42f), w * 0.16f, Qn.Parch, Qn.Ink, dl, glow = 0.8f)
        for (k in 0 until 4) {
            val u = fract(seg(p, 0.1f, 1f) * 1.2f - k * 0.22f).coerceIn(0f, 1f) * (if (seg(p, 0.1f, 1f) * 1.2f - k * 0.22f < 0f) 0f else 1f)
            val x = mix(w * 0.94f, w * 0.08f, u)
            figure(Offset(x, ground), h * 0.2f, lerpColor(Color(0xFF3A3A4A), Qn.Person, seg(u, 0.4f, 0.6f)), dl * (1f - seg(u, 0.92f, 1f)), walk = t * 6f + k)
        }
        tagAt(a.tag("darknesslight", 0, 0), Offset(w * 0.82f, h * 0.16f), dl * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("darknesslight", 0, 1), Offset(w * 0.2f, h * 0.16f), dl * seg(p, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 17. tf-qadr: a night of great worth ═════════════

/**
 * Beats: greatworth (a quiet night; the name, «لَيْلَةُ الْقَدْرِ», then a ring of twelve months round it, each lit with
 * the year's provision and terms: «لِعِظَمِ قَدْرِهَا», «يُقَدَّرُ فِيهَا مَا يَكُونُ فِي الْعَامِ»), exalted (the sky deepens and
 * the stars brighten as the word grows: «شَأْنُهَا جَلِيلٌ», «وَخَطَرُهَا عَظِيمٌ»), witrnights (ten nights in a row, the odd ones
 * lit and numbered 21 to 29: «الْعَشْرِ الْأَوَاخِرِ», «فِي أَوْتَارِهِ»), itikafnight (a mosque with lit windows deep in the
 * night, a mat and beads inside: «يَعْتَكِفُ», «رَجَاءَ لَيْلَةِ الْقَدْرِ»).
 */
internal fun DrawScope.qadrnightScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "greatworth" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "exalted" to Shot(Offset(w * 0.5f, h * 0.45f), 1.08f),
        "witrnights" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "itikafnight" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { qadrDraw(a) }
}

private fun DrawScope.qadrDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val gw = fqIn(a, "greatworth", "exalted")
    val ex = fqIn(a, "exalted", "witrnights")
    val on = fqIn(a, "witrnights", "itikafnight")
    val ik = fqIn(a, "itikafnight")
    val deep = seg(a.b("exalted"), 0f, 0.6f)
    deepSky(t, h * 0.86f, lerpColor(Color(0xFF101A38), Color(0xFF060A1C), deep), Color(0xFF243458), glow = Color(0xFF4A5A9A), starAlpha = 0.7f + 0.3f * deep, shine = 0.3f * deep)
    moon12(Offset(w * 0.84f, h * 0.14f), w * 0.03f, 1f)
    val c = Offset(w * 0.5f, h * 0.48f)
    if (gw > 0f || ex > 0f) {
        val g = (gw + ex).coerceAtMost(1f)
        val p = a.b("greatworth")
        val grow = 1f + 0.35f * easeOut(seg(a.b("exalted"), 0.1f, 0.6f))
        qFit(a.big("greatworth", 0, 0), c, w * 0.34f * grow, h * 0.13f * grow, Color(0xFFFFF4D2), g * seg(p, 0.05f, 0.2f), glow = Qn.Gold)
        if (gw > 0f) {
            tagAt(a.tag("greatworth", 0, 1), c + Offset(0f, h * 0.11f), gw * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
            // the year's ring: twelve months, each written with what is decreed in it
            for (k in 0 until 12) {
                val an = -PI.toFloat() / 2f + k * PI.toFloat() / 6f
                val m = c + Offset(cos(an) * w * 0.36f, sin(an) * h * 0.36f)
                val f = seg(p, 0.4f + 0.04f * k, 0.5f + 0.04f * k)
                drawCircle(Color(0xFF243458), w * 0.03f, m, alpha = gw)
                drawCircle(Qn.Gold, w * 0.03f, m, alpha = gw * (0.4f + 0.6f * f), style = Stroke(1.2f * density))
                qIcon(if (k % 2 == 0) "wheat" else "hourglass", m, w * 0.035f, Qn.GoldLight, gw * f)
            }
            tagAt(a.tag("greatworth", 0, 2), Offset(w * 0.5f, h * 0.94f), gw * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (ex > 0f) {
        val p = a.b("exalted")
        tagAt(a.tag("exalted", 0, 0), Offset(w * 0.3f, h * 0.78f), ex * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("exalted", 0, 1), Offset(w * 0.7f, h * 0.78f), ex * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (on > 0f) {
        val p = a.b("witrnights")
        drawRect(Color(0xFF060A1C), alpha = 0.6f * on)
        tagAt(a.tag("witrnights", 0, 0), Offset(w * 0.5f, h * 0.18f), on * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        // ten nights, right to left: 21 … 30; the odd ones light and carry their number
        for (k in 0 until 10) {
            val x = w * (0.91f - 0.091f * k)
            val y = h * 0.5f
            val odd = k % 2 == 0
            val f = seg(p, 0.15f + 0.05f * k, 0.25f + 0.05f * k)
            val lit = if (odd) seg(p, 0.45f + 0.06f * (k / 2), 0.55f + 0.06f * (k / 2)) else 0f
            drawRoundRect(Color(0xFF1C2A4A), Offset(x - w * 0.04f, y - h * 0.09f), Size(w * 0.08f, h * 0.18f), CornerRadius(4f * density), alpha = on * f)
            crescent12(Offset(x, y - h * 0.03f), w * 0.018f, lerpColor(Color(0xFF8A94B0), Qn.GoldLight, lit), Color(0xFF1C2A4A), on * f)
            if (lit > 0f) {
                softGlow(Offset(x, y), w * 0.05f, Qn.GoldLight, 0.5f * lit * on)
                tagAt(a.tag("witrnights", 0, 2 + k / 2), Offset(x, y + h * 0.05f), on * lit, back = Color(0xE61C2A4A), ink = ArtInk.Gold)
            }
        }
        tagAt(a.tag("witrnights", 0, 1), Offset(w * 0.5f, h * 0.78f), on * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ik > 0f) {
        val p = a.b("itikafnight")
        drawRect(Color(0xFF060A1C), alpha = 0.5f * ik)
        mosque12(Offset(w * 0.5f, h * 0.86f), w * 0.4f, Color(0xFFE8DCC0), Qn.Gold, ik, lit = seg(p, 0.1f, 0.4f))
        softGlow(Offset(w * 0.5f, h * 0.7f), w * 0.2f, Qn.GoldLight, 0.4f * ik * seg(p, 0.2f, 0.5f))
        qIcon("mat", Offset(w * 0.2f, h * 0.74f), w * 0.08f, Qn.GoldLight, ik * seg(p, 0.3f, 0.45f))
        beads12(Offset(w * 0.82f, h * 0.72f), w * 0.04f, Qn.GoldLight, ik * seg(p, 0.4f, 0.55f), turn = t * 0.4f)
        tagAt(a.tag("itikafnight", 0, 0), Offset(w * 0.5f, h * 0.18f), ik * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("itikafnight", 0, 1), Offset(w * 0.5f, h * 0.3f), ik * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 18. tf-alaq: the first to be revealed ═════════════

/**
 * Beats: firstrevealed (a mountain at night with a cave; a cool light comes down to his ﷺ named light inside, and one word
 * shines: «﴿اقْرَأْ﴾», «أَوَّلُ السُّوَرِ الْقُرْآنِيَّةِ نُزُولًا»), fromalaq (from all that is made — stars, plants, beasts — the
 * picture narrows to one person, and three small steps of his beginning: «عُمُومَ الْخَلْقِ», «الْإِنْسَانَ», «مِنْ عَلَقٍ»),
 * commandsforbid (three cards: command and prohibition, messengers, books), forbidder (a hand raised before a lit door of
 * good, a prayer mat inside it, and the barring hand is crossed out: «عَامٌّ لِكُلِّ نَاهٍ عَنِ الْخَيْرِ»).
 */
internal fun DrawScope.iqrafirstScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "firstrevealed" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "fromalaq" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "commandsforbid" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "forbidder" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { iqraDraw(a) }
}

private fun DrawScope.iqraDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val fr = fqIn(a, "firstrevealed", "fromalaq")
    val fa = fqIn(a, "fromalaq", "commandsforbid")
    val cf = fqIn(a, "commandsforbid", "forbidder")
    val fb = fqIn(a, "forbidder")
    deepSky(t, h * 0.9f, Color(0xFF0C1430), Color(0xFF2A2E50), glow = Color(0xFF6A5A8A))
    if (fr > 0f) {
        val p = a.b("firstrevealed")
        // the mountain and its cave
        val peak = Path().apply { moveTo(w * 0.2f, h); lineTo(w * 0.55f, h * 0.36f); lineTo(w * 0.64f, h * 0.44f); lineTo(w * 0.9f, h); close() }
        drawPath(peak, Color(0xFF3A3040), alpha = fr)
        val cave = Offset(w * 0.6f, h * 0.62f)
        drawOval(Color(0xFF14101A), Offset(cave.x - w * 0.04f, cave.y - h * 0.05f), Size(w * 0.08f, h * 0.08f), alpha = fr)
        sirajMunir(cave, w * 0.012f, fr * seg(p, 0.05f, 0.2f), t, nameAt = cave + Offset(w * 0.16f, h * 0.06f))
        val down = easeInOut(seg(p, 0.15f, 0.5f))
        angelLight26(Offset(mix(w * 0.9f, cave.x + w * 0.08f, down), mix(-h * 0.05f, cave.y - h * 0.16f, down)), w * 0.018f, t, fr * seg(p, 0.1f, 0.2f))
        qFit(a.big("firstrevealed", 0, 0), Offset(w * 0.3f, h * 0.3f), w * 0.28f, h * 0.16f, Color(0xFFFFF4D2), fr * seg(p, 0.5f, 0.65f), glow = Qn.Gold)
        tagAt(a.tag("firstrevealed", 0, 1), Offset(w * 0.3f, h * 0.48f), fr * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fa > 0f) {
        val p = a.b("fromalaq")
        drawRect(Color(0xFF0C1430), alpha = 0.6f * fa)
        // all that is made, then the picture narrows to the person
        val narrow = easeInOut(seg(p, 0.3f, 0.55f))
        val icons = listOf("star", "tree", "bird", "moon", "sprout", "mountain")
        for (k in 0 until 6) {
            val an = k * PI.toFloat() / 3f
            val r = mix(w * 0.32f, w * 0.06f, narrow)
            qIcon(icons[k], Offset(w * 0.5f, h * 0.44f) + Offset(cos(an) * r, sin(an) * r * 0.8f), w * 0.06f, Qn.GoldLight, fa * seg(p, 0.02f * k, 0.1f + 0.02f * k) * (1f - narrow), t)
        }
        tagAt(a.tag("fromalaq", 0, 0), Offset(w * 0.5f, h * 0.1f), fa * seg(p, 0.05f, 0.2f) * (1f - narrow), back = Fq.Back, ink = ArtInk.Ivory)
        figure(Offset(w * 0.5f, h * 0.66f), h * 0.34f, Qn.Person, fa * narrow)
        tagAt(a.tag("fromalaq", 0, 1), Offset(w * 0.5f, h * 0.1f), fa * seg(p, 0.5f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
        // three small steps: a drop, a clinging thing, a child
        val stepA = seg(p, 0.6f, 0.75f)
        val base = Offset(w * 0.5f, h * 0.86f)
        fqDrop(base + Offset(w * 0.18f, 0f), w * 0.012f, Fq.Water, fa * stepA)
        drawLine(Color(0xFF8A7A9A), base + Offset(-w * 0.03f, -h * 0.03f), base + Offset(w * 0.03f, -h * 0.03f), 1.5f * density, alpha = fa * stepA)
        drawOval(Qn.Rose, Offset(base.x - w * 0.01f, base.y - h * 0.03f), Size(w * 0.02f, h * 0.04f), alpha = fa * seg(p, 0.68f, 0.8f))
        child12(base + Offset(-w * 0.18f, h * 0.03f), h * 0.1f, Qn.Person, fa * seg(p, 0.75f, 0.88f))
        for (k in 0 until 2) arrowHead(base + Offset(w * (0.09f - 0.18f * k), 0f), Offset(-1f, 0f), w * 0.015f, Qn.GoldLight, 1.5f * density, fa * stepA)
        tagAt(a.tag("fromalaq", 0, 2), Offset(w * 0.8f, h * 0.76f), fa * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (cf > 0f) {
        val p = a.b("commandsforbid")
        drawRect(Color(0xFF0C1430), alpha = 0.7f * cf)
        for (k in 0 until 3) {
            val f = seg(p, 0.08f + 0.25f * k, 0.28f + 0.25f * k)
            glyphCard(Offset(w * (0.78f - 0.28f * k), h * 0.5f), w * 0.15f, a.tag("commandsforbid", 0, k), cf * f) { at, s, al ->
                when (k) {
                    0 -> { checkMark(at + Offset(s * 0.22f, 0f), s * 0.2f, Qn.Jade, 2.5f * density, al); crossMark(at + Offset(-s * 0.22f, 0f), s * 0.16f, Qn.Rose, 2.5f * density, al) }
                    1 -> qIcon("light", at, s, Qn.GoldLight, al, t)
                    else -> qIcon("book", at, s, Qn.GoldLight, al)
                }
            }
        }
    }
    if (fb > 0f) {
        val p = a.b("forbidder")
        drawRect(Color(0xFF1A1E30), alpha = 0.8f * fb)
        val ground = h * 0.86f
        // a lit door of good (a mat inside), and a hand raised before it to bar the way: the barring is crossed out.
        // No one is drawn at prayer, so no figure stands where the Prophet ﷺ is spoken of.
        val door = Offset(w * 0.6f, ground)
        door12(door, w * 0.16f, h * 0.52f, 1f, Color(0xFF8A6A40), Color(0xFF4A3420), fb)
        qIcon("mat", door + Offset(0f, -h * 0.2f), w * 0.07f, Qn.GoldLight, fb)
        val come = easeOut(seg(p, 0.1f, 0.35f))
        val palm = Offset(mix(w * 0.15f, w * 0.42f, come), h * 0.5f)
        val pa = fb * seg(p, 0.05f, 0.15f)
        drawRoundRect(Color(0xFF8A7A8A), Offset(palm.x - w * 0.035f, palm.y - h * 0.02f), Size(w * 0.07f, h * 0.1f), CornerRadius(w * 0.02f), alpha = pa)
        for (k in 0 until 4) drawLine(Color(0xFF8A7A8A), Offset(palm.x - w * 0.025f + k * w * 0.017f, palm.y - h * 0.01f), Offset(palm.x - w * 0.025f + k * w * 0.017f, palm.y - h * 0.08f), w * 0.013f, StrokeCap.Round, alpha = pa)
        drawLine(Color(0xFF8A7A8A), Offset(palm.x + w * 0.03f, palm.y + h * 0.03f), Offset(palm.x + w * 0.055f, palm.y - h * 0.01f), w * 0.013f, StrokeCap.Round, alpha = pa)
        val x = seg(p, 0.5f, 0.7f)
        if (x > 0f) cross14(palm, h * 0.09f, x)
        tagAt(a.tag("forbidder", 0, 0), Offset(w * 0.4f, h * 0.14f), fb * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 19. tf-tin: the lands of the prophets ═════════════

/**
 * Beats: figolive (on a map, fig and olive trees grow in al-Sham, and a small named light: «بِالتِّينِ وَالزَّيْتُونِ»,
 * «أَرْضِ الشَّامِ», «عِيسَى عَلَيْهِ السَّلَامُ»), sinaimakkah (a mountain in Sinai with its named light, then Makkah with his ﷺ
 * named light: «بِطُورِ سِينَاءَ», «مُوسَى», «مَكَّةُ»), threeplaces (a line of light joins the three places from north to south:
 * «أَفْضَلَ النُّبُوَّاتِ وَأَشْرَفَهَا»), bestform (a person drawn with fine lines of proportion that glow:
 * «فِي أَحْسَنِ صُورَةٍ», «وَأَكْمَلِ خِلْقَةٍ»).
 */
internal fun DrawScope.tinmapScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "figolive" to Shot(Offset(w * 0.6f, h * 0.3f), 1.15f),
        "sinaimakkah" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "threeplaces" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "bestform" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { tinDraw(a) }
}

private fun DrawScope.tinDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val fo = fqIn(a, "figolive", "sinaimakkah")
    val sm = fqIn(a, "sinaimakkah", "threeplaces")
    val tp = fqIn(a, "threeplaces", "bestform")
    val bf = fqIn(a, "bestform")
    // the map: land and the sea's edge
    drawRect(Color(0xFF7CA8C4))
    drawPath(Path().apply {
        moveTo(w * 0.35f, 0f); lineTo(w, 0f); lineTo(w, h); lineTo(w * 0.55f, h)
        cubicTo(w * 0.52f, h * 0.8f, w * 0.42f, h * 0.6f, w * 0.36f, h * 0.48f)
        cubicTo(w * 0.3f, h * 0.36f, w * 0.38f, h * 0.2f, w * 0.35f, 0f); close()
    }, Color(0xFFE4CFA2))
    drawRect(Qn.Ink, Offset(w * 0.02f, h * 0.03f), Size(w * 0.96f, h * 0.94f), alpha = 0.35f, style = Stroke(1.5f * density))
    val sham = Offset(w * 0.62f, h * 0.2f)
    val sinai = Offset(w * 0.46f, h * 0.5f)
    val makkah = Offset(w * 0.66f, h * 0.8f)
    val all = fo + sm + tp
    if (all > 0f) {
        val p = a.b("figolive")
        val g = easeOut(seg(p, 0.05f, 0.4f)).coerceAtLeast(if (sm + tp > 0f) 1f else 0f)
        qIcon("tree", sham + Offset(-w * 0.07f, h * 0.02f), w * 0.09f * g.coerceAtLeast(0.01f), Qn.Leaf, all.coerceAtMost(1f))
        // an olive: a narrower grey-green crown
        drawLine(Qn.Soil, sham + Offset(w * 0.07f, h * 0.06f), sham + Offset(w * 0.07f, h * 0.02f), 3f * density, alpha = all.coerceAtMost(1f) * g)
        drawOval(Color(0xFF8A9A6A), Offset(sham.x + w * 0.04f, sham.y - h * 0.05f), Size(w * 0.06f * g, h * 0.07f * g), alpha = all.coerceAtMost(1f))
        val pl = seg(p, 0.45f, 0.6f).coerceAtLeast(if (sm + tp > 0f) 1f else 0f)
        softGlow(sham + Offset(0f, h * 0.12f), w * 0.04f, Qn.GoldLight, all.coerceAtMost(1f) * pl)
        drawCircle(Color(0xFFFFF4D2), w * 0.01f, sham + Offset(0f, h * 0.12f), alpha = all.coerceAtMost(1f) * pl)
        if (fo > 0f) {
            tagAt(a.tag("figolive", 0, 0), sham + Offset(0f, -h * 0.13f), fo * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
            tagAt(a.tag("figolive", 0, 1), sham + Offset(w * 0.2f, h * 0.06f), fo * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Ivory)
        }
        tagAt(a.tag("figolive", 0, 2), sham + Offset(0f, h * 0.2f), all.coerceAtMost(1f) * pl, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sm > 0f || tp > 0f) {
        val s = (sm + tp).coerceAtMost(1f)
        val p = a.b("sinaimakkah")
        val f1 = seg(p, 0.05f, 0.3f).coerceAtLeast(if (tp > 0f) 1f else 0f)
        qIcon("mountain", sinai, w * 0.1f, Color(0xFF9A7A5A), s * f1)
        softGlow(sinai + Offset(0f, -h * 0.08f), w * 0.04f, Qn.GoldLight, s * f1)
        drawCircle(Color(0xFFFFF4D2), w * 0.01f, sinai + Offset(0f, -h * 0.08f), alpha = s * f1)
        tagAt(a.tag("sinaimakkah", 0, 0), sinai + Offset(-w * 0.14f, h * 0.06f), s * f1, back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("sinaimakkah", 0, 1), sinai + Offset(-w * 0.14f, -h * 0.08f), s * seg(p, 0.2f, 0.35f).coerceAtLeast(if (tp > 0f) 1f else 0f), back = Fq.Back, ink = ArtInk.Gold)
        val f2 = seg(p, 0.5f, 0.75f).coerceAtLeast(if (tp > 0f) 1f else 0f)
        kaaba29(makkah + Offset(0f, h * 0.04f), w * 0.04f, s * f2)
        sirajMunir(makkah + Offset(w * 0.08f, -h * 0.06f), w * 0.014f, s * f2, t, nameAt = makkah + Offset(w * 0.2f, -h * 0.06f))
        tagAt(a.tag("sinaimakkah", 0, 2), makkah + Offset(-w * 0.12f, 0f), s * f2, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tp > 0f) {
        val p = a.b("threeplaces")
        val run = easeOut(seg(p, 0.05f, 0.6f))
        val a1 = sham + Offset(0f, h * 0.12f)
        val b1 = sinai + Offset(0f, -h * 0.08f)
        val c1 = makkah + Offset(w * 0.08f, -h * 0.06f)
        qBeam(a1, b1, seg(run, 0f, 0.5f), Qn.GoldLight, tp, 2f * density)
        qBeam(b1, c1, seg(run, 0.5f, 1f), Qn.GoldLight, tp, 2f * density)
        tagAt(a.tag("threeplaces", 0, 0), Offset(w * 0.2f, h * 0.2f), tp * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (bf > 0f) {
        val p = a.b("bestform")
        drawRect(Color(0xFF14243A), alpha = 0.93f * bf)
        val foot = Offset(w * 0.5f, h * 0.88f)
        val tall = h * 0.66f
        figure(foot, tall, Qn.Person, bf * seg(p, 0.05f, 0.25f))
        // fine lines of proportion that draw themselves and glow
        val lines = seg(p, 0.25f, 0.75f)
        drawCircle(Qn.GoldLight, tall * 0.5f, foot + Offset(0f, -tall * 0.5f), alpha = bf * 0.6f * seg(lines, 0f, 0.4f), style = Stroke(1.2f * density))
        for (k in 0 until 4) {
            val y = foot.y - tall * (0.25f * k + 0.05f)
            drawLine(Qn.GoldLight, Offset(w * 0.3f, y), Offset(w * mix(0.3f, 0.7f, seg(lines, 0.2f + 0.15f * k, 0.45f + 0.15f * k)), y), 1f * density, alpha = bf * 0.6f)
        }
        tagAt(a.tag("bestform", 0, 0), Offset(w * 0.18f, h * 0.3f), bf * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("bestform", 0, 1), Offset(w * 0.82f, h * 0.5f), bf * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 20. tf-sharh: one hardship, two eases ═════════════

/**
 * Beats: chestopen (a heart's outline widens, and three cards fill it: the laws of the religion, calling to Allah, noble
 * character), withhardship (a dark stone named «الْعُسْرُ» rolls on a path and a light named «الْيُسْرَ» goes with it, step for
 * step), oneandtwo (a balance: one stone on one pan, two lights on the other, and the two outweigh: «فَلَنْ يَغْلِبَ عُسْرٌ
 * يُسْرَيْنِ»), lizardhole (the stone goes into a small burrow, the light follows it in and brings it out: «جُحْرَ ضَبٍّ»).
 */
internal fun DrawScope.usryusraynScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "chestopen" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "withhardship" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "oneandtwo" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "lizardhole" to Shot(Offset(w * 0.45f, h * 0.62f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { usrDraw(a) }
}

private fun DrawScope.usrDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val co = fqIn(a, "chestopen", "withhardship")
    val wh = fqIn(a, "withhardship", "oneandtwo")
    val ot = fqIn(a, "oneandtwo", "lizardhole")
    val lh = fqIn(a, "lizardhole")
    daySky12(Color(0xFFB0D0E8), Color(0xFFF4E8CC), h * 0.8f, Color(0xFFC8AE84))
    if (co > 0f) {
        val p = a.b("chestopen")
        drawRect(Color(0xFF1A2236), alpha = 0.85f * co)
        val c = Offset(w * 0.5f, h * 0.52f)
        val widen = mix(0.35f, 1f, easeOut(seg(p, 0.05f, 0.4f)))
        softGlow(c, w * 0.3f * widen, Qn.GoldLight, 0.4f * co)
        heart12(c, w * 0.62f * widen, Qn.GoldLight, co, fill = false)
        val icons = listOf("book", "hands", "heart")
        for (k in 0 until 3) {
            val f = seg(p, 0.35f + 0.18f * k, 0.5f + 0.18f * k)
            glyphCard(c + Offset(w * (0.16f - 0.16f * k), -h * 0.04f), w * 0.11f, a.tag("chestopen", 0, k), co * f) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
        }
    }
    val path = h * 0.78f
    if (wh > 0f) {
        val p = a.b("withhardship")
        val roll = easeInOut(seg(p, 0.1f, 0.9f))
        val stone = Offset(mix(w * 0.9f, w * 0.3f, roll), path - h * 0.06f)
        drawCircle(Color(0xFF4A4450), h * 0.06f, stone, alpha = wh)
        drawCircle(Color(0xFF2A2430), h * 0.06f, stone, alpha = wh, style = Stroke(2f * density))
        val light = stone + Offset(-w * 0.09f, -h * 0.06f + sin(t * 3f) * h * 0.01f)
        softGlow(light, w * 0.06f, Qn.GoldLight, wh * seg(p, 0.05f, 0.2f))
        drawCircle(Color(0xFFFFF4D2), w * 0.018f, light, alpha = wh * seg(p, 0.05f, 0.2f))
        tagAt(a.tag("withhardship", 0, 0), stone + Offset(0f, -h * 0.16f), wh * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("withhardship", 0, 1), light + Offset(0f, -h * 0.12f), wh * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ot > 0f) {
        val p = a.b("oneandtwo")
        drawRect(Color(0xFF1A2236), alpha = 0.7f * ot)
        val foot = Offset(w * 0.5f, h * 0.84f)
        val sz = h * 0.5f
        val tilt = -0.7f * easeOut(seg(p, 0.45f, 0.75f))
        balance12(foot, sz, tilt, Qn.GoldLight, ot)
        val (l, r) = balancePans12(foot, sz, tilt)
        drawCircle(Color(0xFF4A4450), h * 0.04f, r + Offset(0f, -h * 0.04f), alpha = ot * seg(p, 0.05f, 0.15f))
        tagAt(a.tag("oneandtwo", 0, 0), r + Offset(0f, -h * 0.16f), ot * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Ivory)
        for (k in 0 until 2) {
            val f = seg(p, 0.2f + 0.12f * k, 0.32f + 0.12f * k)
            val c = l + Offset((k - 0.5f) * w * 0.05f, -h * 0.05f)
            softGlow(c, w * 0.04f, Qn.GoldLight, ot * f)
            drawCircle(Color(0xFFFFF4D2), w * 0.014f, c, alpha = ot * f)
        }
        tagAt(a.tag("oneandtwo", 0, 1), l + Offset(0f, -h * 0.16f), ot * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("oneandtwo", 0, 2), Offset(w * 0.5f, h * 0.1f), ot * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (lh > 0f) {
        val p = a.b("lizardhole")
        val hole = Offset(w * 0.4f, path + h * 0.02f)
        drawOval(Color(0xFF2A2018), Offset(hole.x - w * 0.05f, hole.y - h * 0.03f), Size(w * 0.1f, h * 0.06f), alpha = lh)
        // the stone goes in; the light follows; both come out, the light pushing
        val inn = easeInOut(seg(p, 0.05f, 0.3f))
        val out = easeInOut(seg(p, 0.55f, 0.85f))
        val stone = Offset(mix(w * 0.7f, hole.x, inn) + (w * 0.25f) * out, mix(path - h * 0.06f, hole.y, inn) - (h * 0.08f) * out)
        drawCircle(Color(0xFF4A4450), h * 0.035f * (1f - 0.5f * inn * (1f - out)), stone, alpha = lh)
        val fol = easeInOut(seg(p, 0.3f, 0.5f))
        val light = Offset(mix(w * 0.8f, hole.x, fol) + (w * 0.18f) * out, mix(path - h * 0.12f, hole.y, fol) - (h * 0.1f) * out)
        softGlow(light, w * 0.05f, Qn.GoldLight, lh * seg(p, 0.25f, 0.35f))
        drawCircle(Color(0xFFFFF4D2), w * 0.014f, light, alpha = lh * seg(p, 0.25f, 0.35f))
        tagAt(a.tag("lizardhole", 0, 0), hole + Offset(-w * 0.14f, -h * 0.1f), lh * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}
