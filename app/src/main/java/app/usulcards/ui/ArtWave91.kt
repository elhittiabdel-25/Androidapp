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
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 91 (2026-11-04): v1.4, the Quran 6: al-Ghashiya, al-A'la, al-Tariq, al-Buruj, al-Inshiqaq. Allah's names are words
 * high in the picture; the guardians over each soul are small cool lights.
 */

/** A speech bubble at [c], [wd] wide, its tail toward [tail]. */
private fun DrawScope.bubble91(c: Offset, wd: Float, tail: Offset, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val ht = wd * 0.6f
    drawPath(Path().apply { moveTo(c.x - wd * 0.12f, c.y + ht * 0.4f); lineTo(tail.x, tail.y); lineTo(c.x + wd * 0.12f, c.y + ht * 0.4f); close() }, color, alpha = alpha)
    drawRoundRect(color, Offset(c.x - wd / 2f, c.y - ht / 2f), Size(wd, ht), CornerRadius(ht / 2f), alpha = alpha)
}

// ═════════════ 26. tf-ghashiya: the talk of the people of Paradise, and the flat round earth ═════════════

/**
 * Beats: goodtalk (in a garden two sit facing; a dark scribbled bubble is crossed out, and gold bubbles rise with the
 * remembrance of Allah and His blessings: «كَلَامٌ لَاغٍ», «حَسَنٌ نَافِعٌ»), kindmanners (warm hearts light between them:
 * «الَّتِي تَسُرُّ الْقُلُوبَ وَتَشْرَحُ الصُّدُورَ»), flatround (one stands on flat ground; the picture draws back until the ground
 * is a great globe: «تَسْطِيحَ الْأَرْضِ», «كُرَةٌ مُسْتَدِيرَةٌ»), bigflat (a small ball pressed flat loses its roundness, while the
 * huge globe keeps it with a flat patch under the person: «كُرَوِيًّا مُسَطَّحًا», «وَلَا يَتَنَافَى الْأَمْرَانِ»).
 */
internal fun DrawScope.ghashiyaearthScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "goodtalk" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "kindmanners" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "flatround" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "bigflat" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { ghashiyaDraw(a) }
}

private fun DrawScope.ghashiyaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val gt = fqIn(a, "goodtalk", "kindmanners")
    val km = fqIn(a, "kindmanners", "flatround")
    val fr = fqIn(a, "flatround", "bigflat")
    val bf = fqIn(a, "bigflat")
    val garden = gt + km
    if (garden > 0f) {
        val g = garden.coerceAtMost(1f)
        daySky12(Color(0xFF9CD0C8), Color(0xFFEAF4DC), h * 0.8f, Color(0xFF6FAE6A))
        palm12(Offset(w * 0.1f, h * 0.82f), h * 0.4f, Color(0xFF6A4A30), Qn.Leaf, g, t)
        palm12(Offset(w * 0.9f, h * 0.82f), h * 0.45f, Color(0xFF6A4A30), Qn.Leaf, g, t)
        drawPath(Path().apply { moveTo(0f, h * 0.92f); cubicTo(w * 0.3f, h * 0.86f, w * 0.7f, h * 0.98f, w, h * 0.9f) }, Fq.Water, alpha = g, style = Stroke(h * 0.04f, cap = StrokeCap.Round))
        sit12(Offset(w * 0.34f, h * 0.8f), h * 0.26f, Color(0xFFF4ECD8), g, 1f)
        sit12(Offset(w * 0.66f, h * 0.8f), h * 0.26f, Color(0xFFF4ECD8), g, -1f)
    }
    if (gt > 0f) {
        val p = a.b("goodtalk")
        // the idle, dark talk: crossed out
        val lg = seg(p, 0.05f, 0.2f)
        bubble91(Offset(w * 0.5f, h * 0.3f), w * 0.14f, Offset(w * 0.4f, h * 0.44f), Color(0xFF3A3440), gt * lg * (1f - 0.6f * seg(p, 0.4f, 0.6f)))
        for (k in 0 until 3) drawLine(Color(0xFF8A8494), Offset(w * (0.46f + 0.03f * k), h * 0.28f), Offset(w * (0.47f + 0.03f * k), h * 0.32f), 2f * density, alpha = gt * lg * (1f - 0.6f * seg(p, 0.4f, 0.6f)))
        val x = seg(p, 0.25f, 0.4f)
        if (x > 0f) cross14(Offset(w * 0.5f, h * 0.3f), h * 0.07f, x)
        tagAt(a.tag("goodtalk", 0, 0), Offset(w * 0.5f, h * 0.12f), gt * seg(p, 0.15f, 0.3f), back = Color(0xE6403040), ink = ArtInk.Ivory)
        // good, useful talk: gold bubbles that rise
        for (k in 0 until 3) {
            val f = seg(p, 0.45f + 0.12f * k, 0.6f + 0.12f * k)
            val c = Offset(w * (0.3f + 0.2f * k), mix(h * 0.5f, h * (0.36f - 0.05f * (k % 2)), f))
            bubble91(c, w * 0.1f, c + Offset(0f, h * 0.1f), Qn.GoldLight, gt * f)
        }
        tagAt(a.tag("goodtalk", 0, 1), Offset(w * 0.5f, h * 0.56f), gt * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (km > 0f) {
        val p = a.b("kindmanners")
        for (k in 0 until 3) {
            val f = seg(p, 0.1f + 0.15f * k, 0.3f + 0.15f * k)
            val c = Offset(w * 0.5f, h * (0.62f - 0.14f * k))
            heart12(c, w * (0.05f + 0.015f * k), Qn.Rose, km * f, fill = true)
            softGlow(c, w * 0.06f, Qn.GoldLight, 0.4f * km * f)
        }
        tagAt(a.tag("kindmanners", 0, 0), Offset(w * 0.5f, h * 0.12f), km * seg(p, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fr > 0f || bf > 0f) {
        val s = (fr + bf).coerceAtMost(1f)
        drawRect(Brush.verticalGradient(listOf(Color(0xFF0A1028), Color(0xFF1A2448))), alpha = s)
    }
    if (fr > 0f) {
        val p = a.b("flatround")
        // the camera draws back: the flat ground curves into a globe
        val back = easeInOut(seg(p, 0.25f, 0.75f))
        val r = mix(w * 4f, w * 0.3f, back)
        val c = Offset(w * 0.5f, h * 0.78f + r - mix(0f, h * 0.3f, back))
        drawCircle(Color(0xFF3A7A6A), r, c, alpha = fr)
        drawCircle(Color(0xFF7CB8D8), r, c, alpha = fr * 0.35f * back)
        val top = Offset(c.x, c.y - r)
        figure(top, mix(h * 0.3f, h * 0.05f, back), Qn.Person, fr)
        tagAt(a.tag("flatround", 0, 0), Offset(w * 0.5f, h * 0.12f), fr * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("flatround", 0, 1), Offset(w * 0.5f, h * 0.9f), fr * seg(p, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (bf > 0f) {
        val p = a.b("bigflat")
        // a small ball: pressed flat, it is no longer round
        val small = Offset(w * 0.2f, h * 0.6f)
        val press = easeOut(seg(p, 0.1f, 0.4f))
        drawOval(Color(0xFFC88A5A), Offset(small.x - w * mix(0.05f, 0.08f, press), small.y - h * mix(0.07f, 0.025f, press)), Size(w * mix(0.1f, 0.16f, press), h * mix(0.14f, 0.05f, press)), alpha = bf)
        drawLine(Color(0xFFE8DCC0), Offset(small.x - w * 0.1f, small.y - h * mix(0.1f, 0.04f, press)), Offset(small.x + w * 0.1f, small.y - h * mix(0.1f, 0.04f, press)), 3f * density, alpha = bf * seg(p, 0.05f, 0.15f))
        // the great globe keeps its roundness, with a flat patch under the one standing on it
        val c = Offset(w * 0.64f, h * 0.62f)
        drawCircle(Color(0xFF3A7A6A), w * 0.26f, c, alpha = bf)
        drawLine(Color(0xFFE8DCC0), c + Offset(-w * 0.02f, -w * 0.26f), c + Offset(w * 0.02f, -w * 0.26f), 3f * density, alpha = bf)
        figure(c + Offset(0f, -w * 0.26f), h * 0.05f, Qn.Person, bf)
        tagAt(a.tag("bigflat", 0, 0), c + Offset(0f, -w * 0.33f), bf * seg(p, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("bigflat", 0, 1), Offset(w * 0.5f, h * 0.94f), bf * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 27. tf-ala: glorify, and remind ═════════════

/**
 * Beats: sabbih (three cards, remembrance, worship and humbling oneself before His majesty, under the words
 * «تَسْبِيحًا يَلِيقُ بِعَظَمَتِهِ»), guideall (a bird finds its nest, a bee its flower, a lamb its mother, each along a line of
 * light: «فَهَدَى كُلَّ مَخْلُوقٍ لِمَصْلَحَتِهِ»), remindtwo (rings of a reminder reach two people: one turns to it and lights up,
 * the other turns away and grows dim), fearkeeps (one steps back from a dark doorway and walks on to lit ones:
 * «الِانْكِفَافَ عَنِ الْمَعَاصِي», «وَالسَّعْيَ فِي الْخَيْرَاتِ»).
 */
internal fun DrawScope.alaguideScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sabbih" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "guideall" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "remindtwo" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "fearkeeps" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { alaDraw(a) }
}

private fun DrawScope.alaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val sb = fqIn(a, "sabbih", "guideall")
    val ga = fqIn(a, "guideall", "remindtwo")
    val tk = fqIn(a, "remindtwo", "fearkeeps")
    val fk = fqIn(a, "fearkeeps")
    val ground = h * 0.84f
    daySky12(Color(0xFF7FA8D0), Color(0xFFF0E2C0), ground, Color(0xFF9AB070))
    if (sb > 0f) {
        val p = a.b("sabbih")
        drawRect(Color(0xFF14243A), alpha = 0.85f * sb)
        tagAt(a.tag("sabbih", 0, 0), Offset(w * 0.5f, h * 0.14f), sb * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        for (k in 0 until 3) glyphCard(Offset(w * (0.76f - 0.26f * k), h * 0.54f), w * 0.15f, a.tag("sabbih", 0, k + 1), sb * seg(p, 0.2f + 0.18f * k, 0.35f + 0.18f * k)) { at, s, al ->
            when (k) {
                0 -> beads12(at, s * 0.35f, Qn.GoldLight, al, turn = t * 0.4f)
                1 -> qIcon("mat", at, s, Qn.GoldLight, al)
                else -> qIcon("hands", at, s, Qn.GoldLight, al)
            }
        }
    }
    if (ga > 0f) {
        val p = a.b("guideall")
        // three creatures, each guided to what is good for it
        for (k in 0 until 3) {
            val f = easeInOut(seg(p, 0.1f + 0.22f * k, 0.4f + 0.22f * k))
            val x0 = w * (0.82f - 0.32f * k)
            when (k) {
                0 -> {   // a bird to its nest in a tree
                    qIcon("tree", Offset(x0 - w * 0.06f, ground - h * 0.16f), w * 0.16f, Qn.Leaf, ga)
                    qIcon("nest", Offset(x0 - w * 0.06f, ground - h * 0.25f), w * 0.06f, Qn.Soil, ga)
                    fqBird(Offset(mix(x0 + w * 0.08f, x0 - w * 0.06f, f), mix(h * 0.15f, ground - h * 0.3f, f)), w * 0.025f, Color(0xFF3A3A4A), ga, t)
                }
                1 -> {   // a bee to its flower
                    qPlant(Offset(x0, ground), h * 0.2f, 1f, ga, t)
                    val bee = Offset(mix(x0 + w * 0.1f, x0, f), mix(h * 0.2f, ground - h * 0.21f, f) + sin(t * 12f) * h * 0.005f)
                    drawOval(Color(0xFFE8B030), Offset(bee.x - w * 0.012f, bee.y - h * 0.008f), Size(w * 0.024f, h * 0.016f), alpha = ga)
                    drawOval(Color(0xCCFFFFFF), Offset(bee.x - w * 0.006f, bee.y - h * 0.022f), Size(w * 0.012f, h * 0.016f), alpha = ga)
                }
                else -> {   // a lamb to its mother
                    sheep26(Offset(x0 - w * 0.04f, ground - h * 0.06f), w * 0.1f, Color(0xFFF4F0E6), ga)
                    sheep26(Offset(mix(x0 + w * 0.12f, x0 + w * 0.05f, f), ground - h * 0.04f), w * 0.05f, Color(0xFFF4F0E6), ga)
                }
            }
        }
        tagAt(a.tag("guideall", 0, 0), Offset(w * 0.5f, h * 0.12f), ga * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tk > 0f) {
        val p = a.b("remindtwo")
        drawRect(Color(0xFF1A2236), alpha = 0.7f * tk)
        val src = Offset(w * 0.5f, h * 0.3f)
        for (k in 0 until 3) {
            val r = w * 0.4f * fract(seg(p, 0.05f, 1f) * 1.5f + k * 0.33f)
            drawCircle(Qn.GoldLight, r, src, alpha = tk * 0.5f * (1f - r / (w * 0.4f)), style = Stroke(2f * density))
        }
        val turn = seg(p, 0.3f, 0.5f)
        val good = Offset(w * 0.74f, ground)
        figure(good, h * 0.28f, lerpColor(Qn.Person, Color(0xFFFFF0C8), turn), tk)
        softGlow(good + Offset(0f, -h * 0.18f), w * 0.07f, Qn.GoldLight, tk * seg(p, 0.4f, 0.6f))
        val bad = Offset(w * 0.26f, ground)
        withTransform({ scale(-1f, 1f, pivot = bad) }) { figure(bad, h * 0.28f, lerpColor(Qn.Person, Color(0xFF4A4A58), seg(p, 0.5f, 0.7f)), tk, lean = 0.2f * seg(p, 0.5f, 0.7f)) }
        tagAt(a.tag("remindtwo", 0, 0), Offset(w * 0.74f, h * 0.44f), tk * seg(p, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("remindtwo", 0, 1), Offset(w * 0.26f, h * 0.44f), tk * seg(p, 0.6f, 0.75f), back = Color(0xE6403040), ink = ArtInk.Ivory)
    }
    if (fk > 0f) {
        val p = a.b("fearkeeps")
        drawRect(Color(0xFF1A2236), alpha = 0.6f * fk)
        // a dark doorway he steps back from, lit ones he goes to
        val darkDoor = Offset(w * 0.2f, ground)
        drawPath(archPath26(darkDoor.x, ground - h * 0.4f, ground, w * 0.07f), Color(0xFF0A0810), alpha = fk)
        for (k in 0 until 2) door12(Offset(w * (0.62f + 0.2f * k), ground), w * 0.1f, h * 0.34f, 1f, Color(0xFF8A6A40), Color(0xFF4A3420), fk)
        val back = easeOut(seg(p, 0.1f, 0.3f))
        val go = easeInOut(seg(p, 0.4f, 0.9f))
        val foot = Offset(mix(w * 0.32f, w * 0.38f, back) + (w * 0.22f) * go, ground)
        figure(foot, h * 0.26f, Qn.Person, fk, walk = if (go in 0.01f..0.99f) t * 6f else 0f, lean = -0.15f * back * (1f - go))
        tagAt(a.tag("fearkeeps", 0, 0), Offset(w * 0.22f, h * 0.3f), fk * seg(p, 0.15f, 0.3f), back = Color(0xE6403040), ink = ArtInk.Ivory)
        tagAt(a.tag("fearkeeps", 0, 1), Offset(w * 0.72f, h * 0.3f), fk * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 28. tf-tariq: the piercing star ═════════════

/**
 * Beats: piercingstar (one bright star whose light cuts through layers of dark: «بِالطَّارِقِ», «النَّجْمُ الثَّاقِبُ»), allstars
 * (from it the whole sky lights, star after star: «اسْمُ جِنْسٍ», «سَائِرَ النُّجُومِ الثَّوَاقِبِ»), comesbynight (the day fades and
 * the star comes like one who knocks at night, rings at a door: «يَطْرُقُ لَيْلًا»), guardian (over each person a small cool light
 * writes in a scroll: «كُلَّ نَفْسٍ عَلَيْهَا حَافِظٌ», «يَحْفَظُ عَلَيْهَا أَعْمَالَهَا»).
 */
internal fun DrawScope.tariqstarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "piercingstar" to Shot(Offset(w * 0.6f, h * 0.35f), 1.1f),
        "allstars" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "comesbynight" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "guardian" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { tariqDraw(a) }
}

private fun DrawScope.tariqDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ps = fqIn(a, "piercingstar", "allstars")
    val al = fqIn(a, "allstars", "comesbynight")
    val cn = fqIn(a, "comesbynight", "guardian")
    val gd = fqIn(a, "guardian")
    val ground = h * 0.86f
    val dayF = if (cn > 0f) 1f - seg(a.b("comesbynight"), 0.05f, 0.5f) else 0f
    drawRect(Brush.verticalGradient(listOf(lerpColor(Color(0xFF050818), Color(0xFF8AB0D8), dayF), lerpColor(Color(0xFF141C38), Color(0xFFF2DCB0), dayF))))
    val star = Offset(w * 0.62f, h * 0.28f)
    if (ps > 0f) {
        val p = a.b("piercingstar")
        // layers of dark, and the star's light cutting through them
        for (k in 0 until 3) drawRect(Color(0xFF02030A), Offset(0f, h * (0.1f * k)), Size(w, h * 0.12f), alpha = 0.35f * ps)
        val cut = easeOut(seg(p, 0.15f, 0.6f))
        lightShafts(star, PI.toFloat() / 2f, PI.toFloat() * 2f, w * 0.6f * cut, Color(0xFFDDE6FF), 0.6f * ps, t, 10)
        softGlow(star, w * 0.08f, Color(0xFFDDE6FF), ps)
        drawPath(starPath(star, w * 0.03f), Color.White, alpha = ps)
        tagAt(a.tag("piercingstar", 0, 0), Offset(w * 0.25f, h * 0.2f), ps * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("piercingstar", 0, 1), Offset(w * 0.62f, h * 0.48f), ps * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (al > 0f) {
        val p = a.b("allstars")
        softGlow(star, w * 0.05f, Color(0xFFDDE6FF), al)
        drawPath(starPath(star, w * 0.025f), Color.White, alpha = al)
        for (k in 0 until 30) {
            val r = fract(sin(k * 9.1f) * 31.7f)
            val c = Offset(w * fract(r * 7.3f), h * (0.05f + 0.6f * fract(r * 3.1f)))
            val f = seg(p, 0.1f + 0.02f * k, 0.2f + 0.02f * k)
            drawPath(starPath(c, w * (0.006f + 0.008f * r)), Color(0xFFEAF0FF), alpha = al * f)
        }
        tagAt(a.tag("allstars", 0, 0), Offset(w * 0.5f, h * 0.76f), al * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("allstars", 0, 1), Offset(w * 0.5f, h * 0.88f), al * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (cn > 0f) {
        val p = a.b("comesbynight")
        drawRect(Color(0xFF2A2430), Offset(0f, ground), Size(w, h - ground), alpha = cn)
        house12(Offset(w * 0.3f, ground), w * 0.22f, Color(0xFFC8B08A), Color(0xFF6A5040), cn, lit = seg(p, 0.5f, 0.7f))
        val up = seg(p, 0.45f, 0.75f)
        softGlow(star, w * 0.05f, Color(0xFFDDE6FF), cn * up)
        drawPath(starPath(star, w * 0.025f), Color.White, alpha = cn * up)
        // like one who knocks at night: rings at the door
        for (k in 0 until 3) {
            val q = fract(t * 1.2f + k / 3f)
            drawArc(Color(0xFFDDE6FF), -60f, 120f, false, Offset(w * 0.36f - h * 0.04f * q, ground - h * 0.12f - h * 0.04f * q), Size(h * 0.08f * (1f + q), h * 0.08f * (1f + q)), alpha = cn * up * (1f - q), style = Stroke(2f * density))
        }
        tagAt(a.tag("comesbynight", 0, 0), Offset(w * 0.62f, h * 0.5f), cn * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (gd > 0f) {
        val p = a.b("guardian")
        drawRect(Color(0xFF2A2430), Offset(0f, ground), Size(w, h - ground), alpha = gd)
        for (k in 0 until 4) {
            val foot = Offset(w * (0.18f + 0.22f * k), ground)
            figure(foot, h * 0.26f, Qn.Person, gd * seg(p, 0.05f + 0.05f * k, 0.15f + 0.05f * k))
            val lc = foot + Offset(w * 0.04f, -h * 0.36f)
            angelLight26(lc, w * 0.01f, t + k, gd * seg(p, 0.15f + 0.06f * k, 0.3f + 0.06f * k))
            // a little scroll in which it records
            val wr = seg(p, 0.35f + 0.08f * k, 0.6f + 0.08f * k)
            drawRoundRect(Qn.Parch, Offset(lc.x + w * 0.02f, lc.y - h * 0.02f), Size(w * 0.05f, h * 0.05f), CornerRadius(2f * density), alpha = gd * wr)
            for (m in 0 until 3) drawLine(Qn.Ink, Offset(lc.x + w * 0.027f, lc.y - h * 0.01f + m * h * 0.012f), Offset(lc.x + w * (0.027f + 0.035f * seg(wr, m * 0.3f, m * 0.3f + 0.3f)), lc.y - h * 0.01f + m * h * 0.012f), 1f * density, alpha = gd)
        }
        tagAt(a.tag("guardian", 0, 0), Offset(w * 0.5f, h * 0.1f), gd * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("guardian", 0, 1), Offset(w * 0.5f, h * 0.22f), gd * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 29. tf-buruj: the Forgiving, the Loving ═════════════

/**
 * Beats: mansions (the sun, the moon and the planets go round in their courses, each in its order: «ذَاتِ الْمَنَازِلِ»,
 * «الْمُنْتَظِمَةِ فِي سَيْرِهَا»), wadud (the name «الْوَدُودُ» high, and hearts rising from people below), ghafurwadud (one with dark
 * stains turns back; the stains wash off and his heart lights under the two names, «الْغَفُورُ» and «الْوَدُودُ»), lostmount (in
 * the desert one sits alone, his camel gone; then it is there at his side and he lifts his arms: «أَضَلَّ رَاحِلَتَهُ فِي أَرْضٍ
 * مُهْلِكَةٍ», «ثُمَّ وَجَدَهَا عَلَى رَأْسِهِ»).
 */
internal fun DrawScope.burujskyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "mansions" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "wadud" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "ghafurwadud" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "lostmount" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { burujDraw(a) }
}

private fun DrawScope.burujDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val mn = fqIn(a, "mansions", "wadud")
    val wd = fqIn(a, "wadud", "ghafurwadud")
    val gw = fqIn(a, "ghafurwadud", "lostmount")
    val lm = fqIn(a, "lostmount")
    deepSky(t, h * 0.86f, Color(0xFF0A1028), Color(0xFF243052), glow = Color(0xFF5A6A9A))
    val ground = h * 0.86f
    if (mn > 0f) {
        val p = a.b("mansions")
        val c = Offset(w * 0.5f, h * 0.48f)
        val rings = listOf(0.12f, 0.2f, 0.28f, 0.36f)
        for ((k, r) in rings.withIndex()) {
            drawOval(Qn.Gold, Offset(c.x - w * r, c.y - h * r * 0.9f), Size(w * r * 2f, h * r * 1.8f), alpha = mn * 0.5f * seg(p, 0.05f + 0.1f * k, 0.2f + 0.1f * k), style = Stroke(1.2f * density))
            // twelve marks on the outer ring: the mansions
            if (k == 3) for (m in 0 until 12) {
                val an = m * PI.toFloat() / 6f
                drawCircle(Qn.GoldLight, w * 0.006f, c + Offset(cos(an) * w * r, sin(an) * h * r * 0.9f), alpha = mn * seg(p, 0.5f, 0.7f))
            }
            val an = t * (0.6f - 0.1f * k) + k * 1.7f
            val pos = c + Offset(cos(an) * w * r, sin(an) * h * r * 0.9f)
            when (k) {
                0 -> moon12(pos, w * 0.015f, mn)
                else -> drawCircle(listOf(Color(0xFFE8A070), Color(0xFF9AC0E8), Color(0xFFD8C8A0))[k - 1], w * 0.012f, pos, alpha = mn)
            }
        }
        sun12(c, w * 0.03f, mn)
        tagAt(a.tag("mansions", 0, 0), Offset(w * 0.2f, h * 0.1f), mn * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("mansions", 0, 1), Offset(w * 0.76f, h * 0.9f), mn * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (wd > 0f) {
        val p = a.b("wadud")
        qFit(a.big("wadud", 0, 0), Offset(w * 0.5f, h * 0.16f), w * 0.4f, h * 0.15f, Color(0xFFFFF4D2), wd * seg(p, 0.05f, 0.2f), glow = Qn.Gold)
        for (k in 0 until 5) {
            val foot = Offset(w * (0.18f + 0.16f * k), ground)
            figure(foot, h * 0.2f, Qn.Person, wd)
            val f = fract(seg(p, 0.2f, 1f) * 1.3f + k * 0.2f)
            heart12(foot + Offset(0f, -h * (0.26f + 0.3f * f)), w * 0.03f, Qn.Rose, wd * seg(p, 0.2f, 0.3f) * (1f - f), fill = true)
        }
        tagAt(a.tag("wadud", 0, 1), Offset(w * 0.5f, h * 0.32f), wd * seg(p, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (gw > 0f) {
        val p = a.b("ghafurwadud")
        for (k in 0 until 2) qFit(a.big("ghafurwadud", 0, k), Offset(w * (0.7f - 0.4f * k), h * 0.15f), w * 0.3f, h * 0.13f, Color(0xFFFFF4D2), gw * seg(p, 0.05f + 0.15f * k, 0.2f + 0.15f * k), glow = Qn.Gold)
        val foot = Offset(w * 0.5f, ground)
        val turn = seg(p, 0.25f, 0.4f)
        figure(foot, h * 0.34f, Qn.Person, gw, lean = 0.1f * (1f - turn))
        val wash = seg(p, 0.4f, 0.7f)
        for (k in 0 until 5) drawCircle(Color(0xFF1A141C), w * 0.012f, foot + Offset(w * (-0.03f + 0.015f * k), -h * (0.12f + 0.04f * (k % 3))), alpha = gw * (1f - wash))
        val lit = seg(p, 0.65f, 0.85f)
        heart12(foot + Offset(0f, -h * 0.24f), w * 0.035f, Qn.Rose, gw * lit, fill = true)
        softGlow(foot + Offset(0f, -h * 0.24f), w * 0.08f, Qn.GoldLight, gw * lit)
        tagAt(a.tag("ghafurwadud", 0, 2), Offset(w * 0.5f, h * 0.36f), gw * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (lm > 0f) {
        val p = a.b("lostmount")
        drawRect(Brush.verticalGradient(listOf(Color(0xFFE8C890), Color(0xFFD8A870))), alpha = lm)
        drawRect(Color(0xFFC89A60), Offset(0f, ground), Size(w, h - ground), alpha = lm)
        val man = Offset(w * 0.46f, ground)
        val found = seg(p, 0.55f, 0.7f)
        // alone and lost: he sits with his head down; then the camel is there and he rises, arms up
        if (found < 0.5f) sit12(man, h * 0.28f, Color(0xFF4A3A30), lm, 1f)
        else figure(man, h * 0.3f, Color(0xFF4A3A30), lm, arms = 1f)
        camel26(Offset(w * 0.64f, ground), w * 0.16f, Color(0xFF8A5A30), lm * found, t)
        for (k in 0 until 4) drawLine(Color(0xFFB08A58), Offset(w * (0.1f + 0.25f * k), ground + h * 0.04f), Offset(w * (0.18f + 0.25f * k), ground + h * 0.04f), 2f * density, alpha = lm * 0.6f)
        tagAt(a.tag("lostmount", 0, 0), Offset(w * 0.5f, h * 0.12f), lm * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("lostmount", 0, 1), Offset(w * 0.5f, h * 0.26f), lm * seg(p, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 30. tf-inshiqaq: the easy reckoning ═════════════

/**
 * Beats: rightbook (a book comes down into one's right hand and his road is smooth: «كِتَابَهُ بِيَمِينِهِ», «حِسَابًا يَسِيرًا»),
 * sinveiled (his page has dark marks; a cloth of light is drawn over them: «سَتَرْتُهَا عَلَيْكَ فِي الدُّنْيَا», «فَأَنَا أَسْتُرُهَا لَكَ
 * الْيَوْمَ»), reckonstages (a line of stages, each a card: a drop, a child, the pen of duty, death, the raising), underrule (a small
 * figure under a vast sky, and the names high above: «فَقِيرٌ عَاجِزٌ», «تَحْتَ تَدْبِيرِ الْعَزِيزِ الرَّحِيمِ»).
 */
internal fun DrawScope.easyreckonScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rightbook" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "sinveiled" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "reckonstages" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "underrule" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { reckonDraw(a) }
}

private fun DrawScope.reckonDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val rb = fqIn(a, "rightbook", "sinveiled")
    val vl = fqIn(a, "sinveiled", "reckonstages")
    val sg = fqIn(a, "reckonstages", "underrule")
    val ur = fqIn(a, "underrule")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF14183A), Color(0xFF2A2E50))))
    val ground = h * 0.86f
    if (rb > 0f) {
        val p = a.b("rightbook")
        drawPath(Path().apply { moveTo(w * 0.4f, h); lineTo(w * 0.6f, h); lineTo(w * 0.53f, ground - h * 0.3f); lineTo(w * 0.47f, ground - h * 0.3f); close() }, Qn.ParchLow, alpha = rb * 0.8f)
        val foot = Offset(w * 0.5f, ground)
        figure(foot, h * 0.34f, Qn.Person, rb, arms = 0.5f)
        // the book comes down into his right hand (on the right of the picture: he faces us)
        val down = easeOut(seg(p, 0.1f, 0.4f))
        val hand = foot + Offset(-h * 0.11f, -h * 0.2f)
        val bk = Offset(hand.x, mix(-h * 0.05f, hand.y - h * 0.03f, down))
        drawRoundRect(Qn.Jade, Offset(bk.x - w * 0.03f, bk.y - h * 0.04f), Size(w * 0.06f, h * 0.08f), CornerRadius(2f * density), alpha = rb)
        drawLine(Qn.GoldLight, Offset(bk.x, bk.y - h * 0.04f), Offset(bk.x, bk.y + h * 0.04f), 1.5f * density, alpha = rb)
        softGlow(bk, w * 0.05f, Qn.GoldLight, 0.5f * rb * down)
        tagAt(a.tag("rightbook", 0, 0), hand + Offset(-w * 0.14f, -h * 0.06f), rb * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("rightbook", 0, 1), Offset(w * 0.5f, h * 0.12f), rb * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (vl > 0f) {
        val p = a.b("sinveiled")
        val c = Offset(w * 0.5f, h * 0.52f)
        qPage(c, w * 0.34f, h * 0.6f, vl, lines = 6)
        for (k in 0 until 5) drawCircle(Color(0xFF2A1E28), w * 0.012f, c + Offset(w * (-0.1f + 0.05f * k), h * (-0.15f + 0.07f * (k % 3))), alpha = vl * seg(p, 0.05f + 0.04f * k, 0.12f + 0.04f * k))
        val cover = easeInOut(seg(p, 0.45f, 0.8f))
        drawRoundRect(Color(0xFFFFF2CC), Offset(c.x - w * 0.18f, c.y - h * 0.31f), Size(w * 0.36f, h * 0.62f * cover), CornerRadius(4f * density), alpha = vl * 0.92f)
        softGlow(c, w * 0.22f, Qn.GoldLight, 0.4f * vl * cover)
        tagAt(a.tag("sinveiled", 0, 0), Offset(w * 0.5f, h * 0.1f), vl * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("sinveiled", 0, 1), Offset(w * 0.5f, h * 0.92f), vl * seg(p, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sg > 0f) {
        val p = a.b("reckonstages")
        drawLine(Qn.GoldLight, Offset(w * 0.94f, h * 0.55f), Offset(w * mix(0.94f, 0.06f, easeOut(seg(p, 0.05f, 0.9f))), h * 0.55f), 2f * density, StrokeCap.Round, alpha = sg)
        for (k in 0 until 5) {
            val f = seg(p, 0.08f + 0.17f * k, 0.22f + 0.17f * k)
            glyphCard(Offset(w * (0.86f - 0.18f * k), h * (if (k % 2 == 0) 0.4f else 0.7f)), w * 0.12f, a.tag("reckonstages", 0, k), sg * f) { at, s, al ->
                when (k) {
                    0 -> qIcon("drop", at, s, Qn.GoldLight, al)
                    1 -> qIcon("child", at, s, Qn.Person, al)
                    2 -> qIcon("pen", at, s, Qn.GoldLight, al)
                    3 -> fqMound(at + Offset(0f, s * 0.25f), s * 0.8f, s * 0.3f, Color(0xFF8A7A62), al)
                    else -> qIcon("light", at, s, Qn.GoldLight, al, t)
                }
            }
        }
    }
    if (ur > 0f) {
        val p = a.b("underrule")
        deepSky(t, ground, Color(0xFF060A1C), Color(0xFF1A2448), glow = Color(0xFF4A5A8A))
        drawRect(Color(0xFF14101C), Offset(0f, ground), Size(w, h - ground), alpha = ur)
        figure(Offset(w * 0.5f, ground), h * 0.1f, Qn.Person, ur)
        tagAt(a.tag("underrule", 0, 0), Offset(w * 0.5f, h * 0.74f), ur * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        for (k in 0 until 2) qFit(a.big("underrule", 0, k + 1), Offset(w * (0.66f - 0.32f * k), h * 0.16f), w * 0.28f, h * 0.13f, Color(0xFFFFF4D2), ur * seg(p, 0.45f + 0.15f * k, 0.6f + 0.15f * k), glow = Qn.Gold)
    }
}
