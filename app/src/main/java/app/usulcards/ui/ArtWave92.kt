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
import kotlin.math.sin

/*
 * Wave 92 (2026-11-04): v1.4, the Quran 7: al-Mutaffifin, al-Infitar, al-Takwir, 'Abasa, al-Nazi'at, al-Naba'. Angels are
 * cool lights; the Prophet ﷺ is his named light; the blind man of 'Abasa, a companion, is a plain roundel; Pharaoh is only
 * a fallen crown. Nothing is drawn of seeing Allah.
 */

/** A plain roundel with a name on it (a companion, or a person the text names). */
private fun DrawScope.roundel92(c: Offset, r: Float, tl: androidx.compose.ui.text.TextLayoutResult?, fill: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawCircle(fill, r, c, alpha = alpha)
    drawCircle(Color(0xFFE8DCC0), r, c, alpha = alpha, style = Stroke(1.5f * density))
    qFit(tl, c, r * 1.7f, r * 1.2f, ArtInk.Ivory, alpha)
}

// ═════════════ 31. tf-mutaffifin: fairness, and the rust on hearts ═════════════

/**
 * Beats: fairmeasure (a balance: what he takes and what he gives weigh the same: «الَّذِي لَهُ», «كُلَّ مَا لَهُمْ»), fairargument
 * (two speech bubbles on the pans, his and his opponent's, set out fairly: «مَا لِخَصْمِهِ مِنَ الْحُجَجِ», «إِنْصَافُ الْإِنْسَانِ مِنْ
 * تَعَصُّبِهِ»), rayn (a heart covered layer by layer until its light is gone; then false and true swap: «تَرِينُ عَلَى الْقَلْبِ»,
 * «فَيَرَى الْبَاطِلَ حَقًّا»), tasnim (a cup from a high spring: «أَعْلَى أَشْرِبَةِ الْجَنَّةِ»).
 */
internal fun DrawScope.raynheartScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fairmeasure" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "fairargument" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "rayn" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "tasnim" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { raynDraw(a) }
}

private fun DrawScope.raynDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val fm = fqIn(a, "fairmeasure", "fairargument")
    val fa = fqIn(a, "fairargument", "rayn")
    val ry = fqIn(a, "rayn", "tasnim")
    val ts = fqIn(a, "tasnim")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF2A2234), Color(0xFF3E3040))))
    val foot = Offset(w * 0.5f, h * 0.88f)
    val sz = h * 0.56f
    val scaleA = fm + fa
    if (scaleA > 0f) {
        val s = scaleA.coerceAtMost(1f)
        val wob = if (fm > 0f) 0.3f * sin(t * 2f) * (1f - seg(a.b("fairmeasure"), 0.2f, 0.5f)) else 0f
        balance12(foot, sz, wob, Qn.GoldLight, s)
        val (l, r) = balancePans12(foot, sz, wob)
        if (fm > 0f) {
            val p = a.b("fairmeasure")
            fqCoins(r + Offset(-w * 0.03f, -h * 0.01f), w * 0.02f, 3, fm * seg(p, 0.05f, 0.2f))
            fqCoins(l + Offset(-w * 0.03f, -h * 0.01f), w * 0.02f, 3, fm * seg(p, 0.25f, 0.4f))
            tagAt(a.tag("fairmeasure", 0, 0), r + Offset(0f, -h * 0.18f), fm * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
            tagAt(a.tag("fairmeasure", 0, 1), l + Offset(0f, -h * 0.18f), fm * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        }
        if (fa > 0f) {
            val p = a.b("fairargument")
            for ((k, pan) in listOf(r, l).withIndex()) {
                val f = seg(p, 0.05f + 0.25f * k, 0.2f + 0.25f * k)
                val c = pan + Offset(0f, -h * 0.07f)
                drawRoundRect(if (k == 0) Qn.GoldLight else Color(0xFFB8C8E0), Offset(c.x - w * 0.05f, c.y - h * 0.04f), Size(w * 0.1f, h * 0.06f), CornerRadius(h * 0.03f), alpha = fa * f)
                for (m in 0 until 2) drawLine(Qn.Ink, Offset(c.x - w * 0.03f, c.y - h * 0.018f + m * h * 0.018f), Offset(c.x + w * 0.03f, c.y - h * 0.018f + m * h * 0.018f), 1.5f * density, alpha = fa * f * 0.6f)
            }
            tagAt(a.tag("fairargument", 0, 0), l + Offset(0f, -h * 0.2f), fa * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
            tagAt(a.tag("fairargument", 0, 1), Offset(w * 0.5f, h * 0.1f), fa * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    if (ry > 0f) {
        val p = a.b("rayn")
        val c = Offset(w * 0.5f, h * 0.46f)
        val cover = seg(p, 0.1f, 0.6f)
        softGlow(c, w * 0.16f * (1f - cover), Qn.GoldLight, ry)
        heart12(c, w * 0.26f, lerpColor(Color(0xFFE0566A), Color(0xFF3A2A30), cover), ry, fill = true)
        for (k in 0 until 6) {
            val f = seg(cover, k / 6f, (k + 1) / 6f)
            drawOval(Color(0xFF1A141C), Offset(c.x - w * 0.1f + (k % 3) * w * 0.04f, c.y - h * 0.12f + (k / 3) * h * 0.1f), Size(w * 0.1f, h * 0.07f), alpha = ry * f * 0.85f)
        }
        tagAt(a.tag("rayn", 0, 0), Offset(w * 0.5f, h * 0.1f), ry * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        // the signs swap once the light is gone
        val sw = seg(p, 0.65f, 0.8f)
        tagAt(a.tag("rayn", 0, 1), Offset(w * 0.5f, h * 0.86f), ry * sw, back = Color(0xE6401818), ink = ArtInk.Ivory)
    }
    if (ts > 0f) {
        val p = a.b("tasnim")
        drawRect(Brush.verticalGradient(listOf(Color(0xFF1E4A50), Color(0xFF9CD0C8))), alpha = ts)
        val spring = Offset(w * 0.5f, h * 0.12f)
        softGlow(spring, w * 0.12f, Color(0xFFFFFFFF), 0.6f * ts)
        val flow = easeOut(seg(p, 0.05f, 0.5f))
        drawLine(Color(0xFFF4F8FA), spring, Offset(w * 0.5f, mix(spring.y, h * 0.6f, flow)), w * 0.02f, StrokeCap.Round, alpha = ts)
        cup12(Offset(w * 0.5f, h * 0.84f), w * 0.18f, Qn.Gold, ts, Color(0xFFF4F8FA))
        tagAt(a.tag("tasnim", 0, 0), Offset(w * 0.5f, h * 0.3f), ts * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 32. tf-infitar: what deceived you about your Lord ═════════════

/**
 * Beats: ghurur (three cards, each with a question mark: making light of His rights, belittling His punishment,
 * disbelief in His reward), nobleScribes (two cool lights write over a person, one card for the deeds of the heart, one for
 * those of the limbs: «كِرَامًا يَكْتُبُونَ», «أَفْعَالُ الْقُلُوبِ وَالْجَوَارِحِ»), abrar (two cards, the rights of Allah and the
 * rights of His servants, and three lamps lit in turn: this world, the barzakh, the abiding home).
 */
internal fun DrawScope.kiramScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "ghurur" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "noblescribes" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "abrar" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { kiramDraw(a) }
}

private fun DrawScope.kiramDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val tq = fqIn(a, "ghurur", "noblescribes")
    val ns = fqIn(a, "noblescribes", "abrar")
    val ab = fqIn(a, "abrar")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF101A30), Color(0xFF2A3050))))
    if (tq > 0f) {
        val p = a.b("ghurur")
        for (k in 0 until 3) glyphCard(Offset(w * (0.76f - 0.26f * k), h * 0.5f), w * 0.16f, a.tag("ghurur", 0, k), tq * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k), rim = Qn.Rose) { at, s, al -> qMark14(at, s * 0.7f, Color(0xFFE8DCC0), al) }
    }
    if (ns > 0f) {
        val p = a.b("noblescribes")
        val foot = Offset(w * 0.5f, h * 0.9f)
        figure(foot, h * 0.4f, Qn.Person, ns)
        for (k in 0 until 2) {
            val side = if (k == 0) 1f else -1f
            val lc = foot + Offset(side * w * 0.12f, -h * 0.42f)
            angelLight26(lc, w * 0.014f, t + k, ns * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k))
            val wr = seg(p, 0.3f, 0.8f)
            val sc = lc + Offset(side * w * 0.1f, h * 0.02f)
            drawRoundRect(Qn.Parch, Offset(sc.x - w * 0.04f, sc.y - h * 0.05f), Size(w * 0.08f, h * 0.1f), CornerRadius(3f * density), alpha = ns * seg(p, 0.2f, 0.3f))
            for (m in 0 until 4) drawLine(Qn.Ink, Offset(sc.x - w * 0.03f, sc.y - h * 0.03f + m * h * 0.02f), Offset(sc.x - w * 0.03f + w * 0.06f * seg(wr, m * 0.25f, m * 0.25f + 0.25f), sc.y - h * 0.03f + m * h * 0.02f), 1f * density, alpha = ns)
        }
        tagAt(a.tag("noblescribes", 0, 0), Offset(w * 0.5f, h * 0.08f), ns * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        val icons = listOf("heart", "hands")
        for (k in 0 until 2) glyphCard(Offset(w * (0.86f - 0.72f * k), h * 0.62f), w * 0.12f, a.tag("noblescribes", 0, k + 1), ns * seg(p, 0.5f + 0.15f * k, 0.65f + 0.15f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
    }
    if (ab > 0f) {
        val p = a.b("abrar")
        val icons = listOf("mat", "people")
        for (k in 0 until 2) glyphCard(Offset(w * (0.68f - 0.36f * k), h * 0.3f), w * 0.15f, a.tag("abrar", 0, k), ab * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
        // three lamps lit in turn: this world, the barzakh, the abiding home
        for (k in 0 until 3) {
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.74f)
            val on = seg(p, 0.45f + 0.15f * k, 0.58f + 0.15f * k)
            softGlow(c, w * 0.07f, Qn.GoldLight, ab * on)
            drawCircle(lerpColor(Color(0xFF4A4A5A), Color(0xFFFFF4D2), on), w * 0.025f, c, alpha = ab)
            tagAt(a.tag("abrar", 0, k + 2), c + Offset(0f, h * 0.1f), ab * on, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 33. tf-takwir: the Day as if seen ═════════════

/**
 * Beats: sunfolded (the sun is rolled up and dims, the stars change and fall: «كُوِّرَتِ الشَّمْسُ», «جُمِعَتْ وَلُفَّتْ»), eachknows
 * (people stand apart, each before what he sent ahead, gold or dark: «وَعَلِمَ كُلُّ أَحَدٍ مَا قَدَّمَهُ»), asifseen (an eye and an open
 * page before it: «كَأَنَّهُ رَأْيُ عَيْنٍ», «فَلْيَتَدَبَّرْ هَذِهِ السُّورَةَ»), noblemessenger (a great cool light carrying an open book
 * down: «رَسُولٍ كَرِيمٍ مِنَ الْمَلَائِكَةِ», «شَرَفِ الْقُرْآنِ»).
 */
internal fun DrawScope.takwirScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sunfolded" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "eachknows" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "asifseen" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "noblemessenger" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { takwirDraw(a) }
}

private fun DrawScope.takwirDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val sf = fqIn(a, "sunfolded", "eachknows")
    val ek = fqIn(a, "eachknows", "asifseen")
    val ai = fqIn(a, "asifseen", "noblemessenger")
    val nm = fqIn(a, "noblemessenger")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF14101C), Color(0xFF2A1E28))))
    if (sf > 0f) {
        val p = a.b("sunfolded")
        val roll = easeInOut(seg(p, 0.1f, 0.7f))
        val c = Offset(w * 0.5f, h * 0.36f)
        softGlow(c, w * 0.25f * (1f - 0.8f * roll), Color(0xFFFFE0A0), sf)
        // the disc is rolled up like a cloth, narrowing and dimming
        drawOval(lerpColor(Color(0xFFFFF0C0), Color(0xFF6A5A4A), roll), Offset(c.x - w * 0.1f * (1f - 0.8f * roll), c.y - w * 0.1f), Size(w * 0.2f * (1f - 0.8f * roll), w * 0.2f), alpha = sf)
        for (k in 0 until 10) {
            val r = fract(sin(k * 4.3f) * 61.1f)
            val fall = easeIn2(seg(p, 0.3f + 0.04f * k, 0.8f + 0.02f * k))
            val s0 = Offset(w * (0.05f + 0.9f * r), h * (0.05f + 0.2f * fract(r * 5f)))
            drawPath(starPath(s0 + Offset(w * 0.05f * fall, h * 0.8f * fall), w * 0.01f), Color(0xFFEAF0FF), alpha = sf * (1f - fall))
        }
        tagAt(a.tag("sunfolded", 0, 0), Offset(w * 0.5f, h * 0.7f), sf * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("sunfolded", 0, 1), Offset(w * 0.5f, h * 0.82f), sf * seg(p, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ek > 0f) {
        val p = a.b("eachknows")
        val ground = h * 0.86f
        for (k in 0 until 4) {
            val foot = Offset(w * (0.16f + 0.23f * k), ground)
            val apart = seg(p, 0.05f, 0.3f)
            figure(foot + Offset((k - 1.5f) * w * 0.02f * apart, 0f), h * 0.24f, Qn.Person, ek)
            val good = k % 2 == 0
            val f = seg(p, 0.3f + 0.1f * k, 0.45f + 0.1f * k)
            val c = foot + Offset(w * 0.06f, -h * 0.04f)
            drawRoundRect(if (good) Qn.GoldLight else Color(0xFF3A2A30), Offset(c.x - w * 0.025f, c.y - h * 0.05f * f), Size(w * 0.05f, h * 0.05f * f), CornerRadius(2f * density), alpha = ek)
            if (good) softGlow(c, w * 0.04f, Qn.GoldLight, 0.4f * ek * f)
        }
        tagAt(a.tag("eachknows", 0, 0), Offset(w * 0.5f, h * 0.2f), ek * seg(p, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ai > 0f) {
        val p = a.b("asifseen")
        eye73(Offset(w * 0.3f, h * 0.45f), w * 0.24f, Color(0xFFE8DCC0), ai * seg(p, 0.05f, 0.2f))
        qPage(Offset(w * 0.7f, h * 0.45f), w * 0.22f, h * 0.5f, ai * seg(p, 0.25f, 0.4f), lines = 6)
        qBeam(Offset(w * 0.42f, h * 0.45f), Offset(w * 0.58f, h * 0.45f), seg(p, 0.4f, 0.6f), Qn.GoldLight, ai, 2f * density)
        tagAt(a.tag("asifseen", 0, 0), Offset(w * 0.3f, h * 0.14f), ai * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("asifseen", 0, 1), Offset(w * 0.7f, h * 0.85f), ai * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nm > 0f) {
        val p = a.b("noblemessenger")
        drawRect(Color(0xFF0A1028), alpha = 0.8f * nm)
        val down = easeInOut(seg(p, 0.05f, 0.5f))
        val c = Offset(w * 0.5f, mix(-h * 0.05f, h * 0.4f, down))
        angelLight26(c, w * 0.04f, t, nm)
        openBook21(c + Offset(0f, h * 0.18f), w * 0.2f, Qn.Parch, Qn.Ink, nm * seg(p, 0.3f, 0.5f), glow = 0.6f)
        tagAt(a.tag("noblemessenger", 0, 0), Offset(w * 0.5f, h * 0.84f), nm * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("noblemessenger", 0, 1), Offset(w * 0.5f, h * 0.1f), nm * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

private fun easeIn2(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ═════════════ 34. tf-abasa: turning to the one who seeks knowledge ═════════════

/**
 * Beats: twocome (his ﷺ named light; two roundels before it, «رَجُلٌ أَعْمَى مِنَ الْمُؤْمِنِينَ» and «رَجُلٌ مِنَ الْأَغْنِيَاءِ»),
 * gentlereproach (a gold thread that went to the second roundel turns back to the first: «الْعِتَابِ اللَّطِيفِ»), knownrule (a
 * balance: what is known outweighs what is only hoped: «أَمْرٌ مَعْلُومٌ», «لِأَمْرٍ مَوْهُومٍ»), welcomeseeker (a book opens before
 * the first roundel and its light reaches him: «الْإِقْبَالُ عَلَى طَالِبِ الْعِلْمِ»).
 */
internal fun DrawScope.abasaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twocome" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "gentlereproach" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "knownrule" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "welcomeseeker" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { abasaDraw(a) }
}

private fun DrawScope.abasaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val tc = fqIn(a, "twocome", "gentlereproach")
    val gr = fqIn(a, "gentlereproach", "knownrule")
    val kr = fqIn(a, "knownrule", "welcomeseeker")
    val ws = fqIn(a, "welcomeseeker")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF1E2A40), Color(0xFF3A3A50))))
    val light = Offset(w * 0.5f, h * 0.24f)
    val blind = Offset(w * 0.24f, h * 0.66f)
    val rich = Offset(w * 0.76f, h * 0.66f)
    val story = tc + gr + ws
    if (story > 0f) {
        val s = story.coerceAtMost(1f)
        sirajMunir(light, w * 0.02f, s, t, nameAt = light + Offset(0f, -h * 0.12f))
        roundel92(blind, w * 0.09f, a.big("twocome", 0, 0), Color(0xFF5A6A7A), s)
        roundel92(rich, w * 0.09f, a.big("twocome", 0, 1), Color(0xFF7A6A4A), s * seg(a.b("twocome"), 0.4f, 0.55f).coerceAtLeast(if (gr + ws > 0f) 1f else 0f))
        fqCoins(rich + Offset(w * 0.06f, h * 0.1f), w * 0.015f, 3, s * seg(a.b("twocome"), 0.5f, 0.6f).coerceAtLeast(if (gr + ws > 0f) 1f else 0f))
        tagAt(a.tag("twocome", 0, 2), blind + Offset(0f, h * 0.17f), tc * seg(a.b("twocome"), 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (gr > 0f) {
        val p = a.b("gentlereproach")
        val toRich = 1f - easeInOut(seg(p, 0.45f, 0.8f))
        val target = Offset(mix(blind.x, rich.x, toRich), blind.y - h * 0.09f)
        qBeam(light + Offset(0f, h * 0.04f), target, 1f, Qn.GoldLight, gr * 0.8f, 2f * density)
        tagAt(a.tag("gentlereproach", 0, 0), Offset(w * 0.5f, h * 0.9f), gr * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (kr > 0f) {
        val p = a.b("knownrule")
        drawRect(Color(0xFF1E2A40), alpha = 0.92f * kr)
        val foot = Offset(w * 0.5f, h * 0.88f)
        val sz = h * 0.55f
        val tilt = -0.7f * easeOut(seg(p, 0.35f, 0.65f))
        balance12(foot, sz, tilt, Qn.GoldLight, kr)
        val (l, r) = balancePans12(foot, sz, tilt)
        // the known: a solid lit block; the merely hoped: a faint dashed outline
        drawRoundRect(Qn.GoldLight, Offset(l.x - w * 0.04f, l.y - h * 0.08f), Size(w * 0.08f, h * 0.07f), CornerRadius(3f * density), alpha = kr * seg(p, 0.05f, 0.2f))
        drawRoundRect(Color(0xFFB8B0C8), Offset(r.x - w * 0.04f, r.y - h * 0.08f), Size(w * 0.08f, h * 0.07f), CornerRadius(3f * density), alpha = kr * seg(p, 0.15f, 0.3f), style = Stroke(1.5f * density, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(4f * density, 3f * density))))
        tagAt(a.tag("knownrule", 0, 0), l + Offset(0f, -h * 0.18f), kr * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("knownrule", 0, 1), r + Offset(0f, -h * 0.18f), kr * seg(p, 0.25f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ws > 0f) {
        val p = a.b("welcomeseeker")
        val bk = blind + Offset(w * 0.18f, -h * 0.02f)
        openBook21(bk, w * 0.14f, Qn.Parch, Qn.Ink, ws * seg(p, 0.1f, 0.25f), glow = 0.6f)
        qBeam(bk + Offset(-w * 0.05f, -h * 0.02f), blind + Offset(w * 0.06f, -h * 0.03f), seg(p, 0.3f, 0.6f), Qn.GoldLight, ws, 2f * density)
        softGlow(blind, w * 0.12f, Qn.GoldLight, 0.5f * ws * seg(p, 0.5f, 0.8f))
        tagAt(a.tag("welcomeseeker", 0, 0), Offset(w * 0.5f, h * 0.9f), ws * seg(p, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 35. tf-naziat: a lesson for whoever fears ═════════════

/**
 * Beats: angelsobey (cool lights streak out swiftly in every direction, sent on His command: «كَمَالِ انْقِيَادِهِمْ لِأَمْرِهِ»),
 * sixpillars (six cards in a row, the pillars of faith, and the second lights: the angels), pharaoh (a crown tumbles into
 * dark water: «كُلَّ مَنْ تَكَبَّرَ وَعَصَى عَاقَبَهُ اللَّهُ», «يَنْتَفِعُ بِالْآيَاتِ وَالْعِبَرِ»), nofear (signs like stars fall on a
 * closed, dark heart and slide off it: «تَرَحَّلَتْ خَشْيَةُ اللَّهِ مِنْ قَلْبِهِ»).
 */
internal fun DrawScope.naziatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "angelsobey" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sixpillars" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "pharaoh" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "nofear" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { naziatDraw(a) }
}

private fun DrawScope.naziatDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ao = fqIn(a, "angelsobey", "sixpillars")
    val sp = fqIn(a, "sixpillars", "pharaoh")
    val ph = fqIn(a, "pharaoh", "nofear")
    val nf = fqIn(a, "nofear")
    deepSky(t, h * 0.9f, Color(0xFF0A1028), Color(0xFF1E2A4A), glow = Color(0xFF5A6A9A))
    if (ao > 0f) {
        val p = a.b("angelsobey")
        val c = Offset(w * 0.5f, h * 0.15f)
        for (k in 0 until 7) {
            val an = PI.toFloat() * (0.15f + 0.7f * k / 6f)
            val f = easeOut(seg(p, 0.1f + 0.06f * k, 0.5f + 0.06f * k))
            val pos = c + Offset(cos(an) * w * 0.5f * f, sin(an) * h * 0.75f * f)
            drawLine(Color(0xFFDDEBFF), c + (pos - c) * (0.6f * f), pos, 2f * density, StrokeCap.Round, alpha = ao * 0.6f)
            angelLight26(pos, w * 0.012f, t + k, ao * seg(p, 0.1f + 0.06f * k, 0.2f + 0.06f * k))
        }
        tagAt(a.tag("angelsobey", 0, 0), Offset(w * 0.5f, h * 0.9f), ao * seg(p, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sp > 0f) {
        val p = a.b("sixpillars")
        val icons = listOf("", "light", "book", "scroll", "scale", "pen")   // no symbol on the first card: Allah is never drawn
        for (k in 0 until 6) {
            val f = seg(p, 0.05f + 0.06f * k, 0.15f + 0.06f * k)
            val lit = if (k == 1) seg(p, 0.5f, 0.7f) else 0f
            glyphCard(Offset(w * (0.9f - 0.16f * k), h * 0.5f), w * 0.12f, a.tag("sixpillars", 0, k), sp * f * (if (k == 1) 1f else 1f - 0.4f * seg(p, 0.5f, 0.7f)), rim = if (k == 1) Qn.GoldLight else Qn.Gold, lit = lit) { at, s, al ->
                if (k > 0) qIcon(icons[k], at, s, Qn.GoldLight, al, t)
            }
        }
        tagAt(a.tag("sixpillars", 0, 6), Offset(w * 0.5f, h * 0.16f), sp * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ph > 0f) {
        val p = a.b("pharaoh")
        val sea = h * 0.7f
        drawRect(Color(0xFF14243A), Offset(0f, sea), Size(w, h - sea), alpha = ph)
        for (k in 0 until 5) drawArc(Color(0xFF3A5A7A), 200f, 140f, false, Offset(w * (0.02f + 0.2f * k), sea - h * 0.02f + sin(t * 2f + k) * h * 0.01f), Size(w * 0.2f, h * 0.06f), alpha = ph, style = Stroke(2f * density))
        val fall = easeIn2(seg(p, 0.1f, 0.6f))
        val c = Offset(w * 0.5f + w * 0.05f * fall, mix(h * 0.25f, sea + h * 0.08f, fall))
        rotate(80f * fall, c) { qIcon("crown", c, w * 0.12f, Qn.Gold, ph * (1f - 0.6f * seg(p, 0.55f, 0.8f))) }
        tagAt(a.tag("pharaoh", 0, 0), Offset(w * 0.5f, h * 0.1f), ph * seg(p, 0.45f, 0.6f), back = Color(0xE6401818), ink = ArtInk.Ivory)
        tagAt(a.tag("pharaoh", 0, 1), Offset(w * 0.5f, h * 0.22f), ph * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nf > 0f) {
        val p = a.b("nofear")
        val c = Offset(w * 0.5f, h * 0.56f)
        heart12(c, w * 0.22f, Color(0xFF3A2A30), nf, fill = true)
        padlock21(c + Offset(0f, h * 0.02f), w * 0.05f, Color(0xFF6A6070), 0f, nf)
        for (k in 0 until 6) {
            val f = fract(seg(p, 0.1f, 1f) * 1.4f + k / 6f)
            val x = w * (0.36f + 0.05f * k)
            val y = mix(h * 0.05f, c.y - h * 0.12f, seg(f, 0f, 0.5f)) + h * 0.3f * seg(f, 0.5f, 1f)
            val off = (if (k % 2 == 0) -1f else 1f) * w * 0.12f * seg(f, 0.5f, 1f)
            drawPath(starPath(Offset(x + off, y), w * 0.012f), Qn.GoldLight, alpha = nf * seg(p, 0.1f, 0.2f) * (1f - seg(f, 0.8f, 1f)))
        }
        tagAt(a.tag("nofear", 0, 0), Offset(w * 0.5f, h * 0.9f), nf * seg(p, 0.3f, 0.45f), back = Color(0xE6401818), ink = ArtInk.Ivory)
    }
}

// ═════════════ 36. tf-naba: the Day none speaks without leave ═════════════

/**
 * Beats: rowsstand (rows of cool lights stand in ranks; two cards, the two conditions to speak: leave, and saying what is
 * right), nofalsehood (a false word, a mask of a tag, cracks and falls: «لَا يَرُوجُ فِيهِ الْبَاطِلُ», «وَلَا يَنْفَعُ فِيهِ
 * الْكَذِبُ»), greatnews (plain graves open and people rise toward a light: «النَّبَأُ الْعَظِيمُ», «الْبَعْثُ بَعْدَ الْمَوْتِ»),
 * speakright (a person whose words go up as gold, before the Day: «مِمَّنْ يَقُولُ الصَّوَابَ فِي الدُّنْيَا»).
 */
internal fun DrawScope.nabaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rowsstand" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "nofalsehood" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "greatnews" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "speakright" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { nabaDraw(a) }
}

private fun DrawScope.nabaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val rs = fqIn(a, "rowsstand", "nofalsehood")
    val nf = fqIn(a, "nofalsehood", "greatnews")
    val gn = fqIn(a, "greatnews", "speakright")
    val sr = fqIn(a, "speakright")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF0E1428), Color(0xFF242A44))))
    val ground = h * 0.86f
    if (rs > 0f) {
        val p = a.b("rowsstand")
        for (r in 0 until 3) for (k in 0 until 9) {
            val f = seg(p, 0.02f * (r * 9 + k), 0.1f + 0.02f * (r * 9 + k))
            angelLight26(Offset(w * (0.1f + 0.1f * k), h * (0.1f + 0.08f * r)), w * 0.008f, t + k + r, rs * f * (0.6f + 0.2f * r))
        }
        val icons = listOf("key", "check")
        for (k in 0 until 2) glyphCard(Offset(w * (0.68f - 0.36f * k), h * 0.62f), w * 0.16f, a.tag("rowsstand", 0, k), rs * seg(p, 0.5f + 0.2f * k, 0.65f + 0.2f * k)) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
    }
    if (nf > 0f) {
        val p = a.b("nofalsehood")
        val c = Offset(w * 0.5f, h * 0.45f)
        val crack = seg(p, 0.3f, 0.5f)
        val fall = easeIn2(seg(p, 0.5f, 0.9f))
        drawRoundRect(Color(0xFF6A5A6A), Offset(c.x - w * 0.12f, c.y - h * 0.08f + fall * h * 0.4f), Size(w * 0.24f, h * 0.16f), CornerRadius(h * 0.08f), alpha = nf * (1f - fall))
        if (crack > 0f) drawLine(Color(0xFF1A141C), Offset(c.x - w * 0.02f, c.y - h * 0.08f + fall * h * 0.4f), Offset(c.x + w * 0.03f * crack, c.y + h * 0.06f * crack + fall * h * 0.4f), 2.5f * density, alpha = nf * (1f - fall))
        tagAt(a.tag("nofalsehood", 0, 0), Offset(w * 0.5f, h * 0.14f), nf * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("nofalsehood", 0, 1), Offset(w * 0.5f, h * 0.26f), nf * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (gn > 0f) {
        val p = a.b("greatnews")
        drawRect(Color(0xFF2A2430), Offset(0f, ground), Size(w, h - ground), alpha = gn)
        softGlow(Offset(w * 0.5f, h * 0.1f), w * 0.25f, Qn.GoldLight, 0.5f * gn * seg(p, 0.3f, 0.6f))
        for (k in 0 until 4) {
            val g = Offset(w * (0.2f + 0.2f * k), ground + h * 0.04f)
            fqMound(g, w * 0.12f, h * 0.05f, Color(0xFF6A5A4A), gn)
            val up = easeOut(seg(p, 0.3f + 0.08f * k, 0.6f + 0.08f * k))
            figure(g + Offset(0f, -h * 0.2f * up), h * 0.2f, Qn.Person, gn * up)
        }
        tagAt(a.tag("greatnews", 0, 0), Offset(w * 0.5f, h * 0.16f), gn * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("greatnews", 0, 1), Offset(w * 0.5f, h * 0.3f), gn * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sr > 0f) {
        val p = a.b("speakright")
        drawRect(Color(0xFF2A2430), Offset(0f, ground), Size(w, h - ground), alpha = sr)
        val foot = Offset(w * 0.5f, ground)
        figure(foot, h * 0.34f, Qn.Person, sr)
        voice12(foot + Offset(-h * 0.04f, -h * 0.34f), w * 0.06f, t, Qn.GoldLight, sr * seg(p, 0.1f, 0.25f), -1f)
        for (k in 0 until 4) {
            val f = fract(seg(p, 0.15f, 1f) * 1.5f + k * 0.25f)
            drawPath(starPath(foot + Offset(-w * 0.06f - w * 0.04f * (k % 2), -h * (0.36f + 0.4f * f)), w * 0.012f), Qn.GoldLight, alpha = sr * seg(p, 0.15f, 0.25f) * (1f - f))
        }
        tagAt(a.tag("speakright", 0, 0), Offset(w * 0.5f, h * 0.12f), sr * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
