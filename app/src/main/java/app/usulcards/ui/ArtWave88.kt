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
 * Wave 88 (2026-11-04): v1.4, the Quran 3: al-Humaza, then from al-'Asr: al-'Asr, al-Takathur, al-'Adiyat and
 * al-Qari'a, al-Zalzala. The fire is a glow and a few flames, never anyone burning; graves are plain mounds.
 */

// ═════════════ 11. tf-humaza: the slanderer and his wealth ═════════════

/**
 * Beats: slanderers (two cards: «الْهَمَّازُ», who faults people «بِالْإِشَارَةِ وَالْفِعْلِ», a pointing hand; «وَاللَّمَّازُ»,
 * who does it «بِقَوْلِهِ», lips with a jagged word), countcoins (coins hop from pile to pile, counted again and again:
 * «جَمْعُ الْمَالِ», «وَتَعْدِيدُهُ»), nogiving (a locked chest; the roads to good and to kin stay empty; an hourglass runs
 * beside it: «يَحْسَبُ أَنَّ مَالَهُ يُخْلِدُهُ»), hutama (a dark red glow and flames rising toward a heart: «تَطَّلِعُ عَلَى
 * الْأَفْئِدَةِ»).
 */
internal fun DrawScope.humazawealthScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "slanderers" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "countcoins" to Shot(Offset(w * 0.5f, h * 0.58f), 1.1f),
        "nogiving" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "hutama" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { humazaDraw(a) }
}

private fun DrawScope.humazaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val sl = fqIn(a, "slanderers", "countcoins")
    val cc = fqIn(a, "countcoins", "nogiving")
    val ng = fqIn(a, "nogiving", "hutama")
    val hu = fqIn(a, "hutama")
    // an object close-up: a dark wooden table under one lamp
    drawRect(Brush.verticalGradient(listOf(Color(0xFF1E1A22), Color(0xFF2E2620))))
    softGlow(Offset(w * 0.5f, h * 0.1f), w * 0.5f, Color(0xFFFFD890), 0.25f)
    val table = h * 0.8f
    drawRect(Color(0xFF4A3424), Offset(0f, table), Size(w, h - table))
    if (sl > 0f) {
        val p = a.b("slanderers")
        for (k in 0 until 2) {
            val c = Offset(w * (0.72f - 0.44f * k), h * 0.48f)
            val f = seg(p, 0.05f + 0.4f * k, 0.25f + 0.4f * k)
            qFit(a.big("slanderers", 0, k * 2), c + Offset(0f, -h * 0.28f), w * 0.3f, h * 0.12f, ArtInk.Ivory, sl * f, glow = Qn.Rose)
            glyphCard(c, w * 0.16f, a.tag("slanderers", 0, k * 2 + 1), sl * seg(p, 0.15f + 0.4f * k, 0.3f + 0.4f * k), rim = Qn.Rose) { at, s, al ->
                if (k == 0) qIcon("point", at, s * 1.2f, Color(0xFFE8DCC0), al)
                else {
                    lips73(at + Offset(s * 0.2f, 0f), s * 0.6f, Qn.Rose, al)
                    // a jagged word coming out
                    drawPath(Path().apply { moveTo(at.x - s * 0.1f, at.y); lineTo(at.x - s * 0.25f, at.y - s * 0.15f); lineTo(at.x - s * 0.35f, at.y + s * 0.05f); lineTo(at.x - s * 0.5f, at.y - s * 0.12f) }, Qn.Rose, alpha = al, style = Stroke(2f * density, cap = StrokeCap.Round))
                }
            }
        }
    }
    if (cc > 0f) {
        val p = a.b("countcoins")
        // coins hop from the right pile to the left and back: gathered and counted, over and over
        val piles = listOf(Offset(w * 0.66f, table), Offset(w * 0.34f, table))
        val n = 10
        val cycle = seg(p, 0.15f, 1f) * 3f
        for (k in 0 until n) {
            val leg = cycle - k * 0.08f
            val trip = leg.toInt().coerceIn(0, 3)
            val u = fract(leg).let { if (leg < 0f) 0f else if (trip >= 3) 1f else it }
            val from = piles[trip % 2]
            val to = piles[(trip + 1) % 2]
            val stackFrom = from + Offset(0f, -h * 0.02f * (k + 1))
            val stackTo = to + Offset(0f, -h * 0.02f * (k + 1))
            val pos = Offset(mix(stackFrom.x, stackTo.x, easeInOut(u)), mix(stackFrom.y, stackTo.y, u) - h * 0.15f * bump(u))
            drawOval(Color(0xFFE8C25A), Offset(pos.x - w * 0.03f, pos.y - h * 0.012f), Size(w * 0.06f, h * 0.024f), alpha = cc * seg(p, 0f, 0.1f))
            drawOval(Color(0xFF9A7224), Offset(pos.x - w * 0.03f, pos.y - h * 0.012f), Size(w * 0.06f, h * 0.024f), alpha = cc * seg(p, 0f, 0.1f), style = Stroke(1f * density))
        }
        tagAt(a.tag("countcoins", 0, 0), Offset(w * 0.5f, h * 0.16f), cc * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("countcoins", 0, 1), Offset(w * 0.5f, h * 0.3f), cc * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ng > 0f) {
        val p = a.b("nogiving")
        // a chest, locked, the coins inside
        val chest = Offset(w * 0.5f, table)
        drawRoundRect(Color(0xFF6A4A2A), Offset(chest.x - w * 0.12f, chest.y - h * 0.2f), Size(w * 0.24f, h * 0.2f), CornerRadius(4f * density), alpha = ng)
        drawRect(Color(0xFF8A6A3A), Offset(chest.x - w * 0.12f, chest.y - h * 0.2f), Size(w * 0.24f, h * 0.05f), alpha = ng)
        padlock21(chest + Offset(0f, -h * 0.12f), w * 0.04f, Qn.Gold, 0f, ng * seg(p, 0.05f, 0.2f))
        // the two roads to good and to kin, and nothing goes down them
        val icons = listOf("house", "people")
        for (k in 0 until 2) {
            val c = Offset(w * (0.86f - 0.72f * k), h * 0.42f)
            drawLine(Color(0xFF8A7A6A), chest + Offset(if (k == 0) w * 0.12f else -w * 0.12f, -h * 0.1f), c + Offset(0f, h * 0.1f), 1.5f * density, alpha = ng * 0.6f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(4f * density, 4f * density)))
            qIcon(icons[k], c, w * 0.1f, Color(0xFF8A8494), ng * seg(p, 0.15f, 0.3f))
        }
        tagAt(a.tag("nogiving", 0, 0), Offset(w * 0.5f, h * 0.16f), ng * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        // an hourglass runs out beside the chest: the wealth will not keep him
        hourglass73(Offset(w * 0.8f, h * 0.68f), h * 0.2f, seg(p, 0.4f, 0.95f), Qn.Gold, ng * seg(p, 0.35f, 0.45f))
        tagAt(a.tag("nogiving", 0, 1), Offset(w * 0.5f, h * 0.3f), ng * seg(p, 0.55f, 0.7f), back = Color(0xE6701E1E), ink = ArtInk.Ivory)
    }
    if (hu > 0f) {
        val p = a.b("hutama")
        drawRect(Color(0xFF1A0E0E), alpha = 0.9f * hu)
        val rise = easeOut(seg(p, 0.1f, 0.7f))
        softGlow(Offset(w * 0.5f, h * 0.95f), w * 0.5f, Color(0xFFB4402A), 0.7f * hu * rise)
        for (k in 0 until 7) {
            val x = w * (0.2f + 0.1f * k)
            qFlame(Offset(x, h * 0.98f), h * (0.18f + 0.1f * rise + 0.05f * sin(k * 1.7f)), Color(0xFFC0503A), hu * rise, t + k)
        }
        val heart = Offset(w * 0.5f, h * 0.42f)
        heart12(heart, w * 0.12f, Color(0xFF8A6A70), hu * seg(p, 0f, 0.2f), fill = false)
        softGlow(heart, w * 0.08f, Color(0xFFE8794A), 0.5f * hu * seg(p, 0.5f, 0.8f))
        tagAt(a.tag("hutama", 0, 0), Offset(w * 0.5f, h * 0.16f), hu * seg(p, 0.4f, 0.6f), back = Color(0xE6401818), ink = ArtInk.Ivory)
    }
}

// ═════════════ 12. tf-asr: four traits that save from loss ═════════════

/**
 * Beats: timepasses (day and night turn over a great hourglass; under it a man whose sand runs away: «بِالْعَصْرِ»,
 * «مَحَلُّ أَعْمَالِ الْعِبَادِ», «كُلَّ إِنْسَانٍ خَاسِرٌ»), fourtraits (four cards come one by one, and with each the falling sand
 * turns to gold: faith, good deeds, enjoining the truth, enjoining patience), selfothers (the first two cards shine in
 * the man; lines go out from him to two others with the last two: «يُكَمِّلُ الْإِنْسَانُ نَفْسَهُ», «يُكَمِّلُ غَيْرَهُ»),
 * asrprofit (the lower glass full of gold, a rising line of gain: «يَسْلَمُ الْإِنْسَانُ مِنَ الْخَسَارِ», «وَيَفُوزُ بِالرِّبْحِ الْعَظِيمِ»).
 */
internal fun DrawScope.asrhourglassScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "timepasses" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "fourtraits" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "selfothers" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "asrprofit" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { asrDraw(a) }
}

private fun DrawScope.asrDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val tp = fqIn(a, "timepasses", "fourtraits")
    val ft = fqIn(a, "fourtraits", "selfothers")
    val so = fqIn(a, "selfothers", "asrprofit")
    val pr = fqIn(a, "asrprofit")
    // day and night turn overhead in the first beat, then settle on the late afternoon (al-'asr)
    val spin = seg(a.b("timepasses"), 0f, 0.6f)
    val dayness = if (tp > 0f && spin < 1f) 0.5f + 0.5f * cos(spin * 6f * PI.toFloat()) else 0.6f
    drawRect(Brush.verticalGradient(listOf(lerpColor(Color(0xFF0E1630), Color(0xFF8AB0D8), dayness), lerpColor(Color(0xFF2A2A4A), Color(0xFFF2C890), dayness))))
    val glass = Offset(w * 0.5f, h * 0.48f)
    val gh = h * 0.62f
    // the sand that has run: grey loss until the traits come, then gold
    val gold = seg(a.b("fourtraits"), 0.1f, 0.95f).coerceAtLeast(if (so + pr > 0f) 1f else 0f)
    val fill = mix(0.2f, 0.75f, seg(a.b("timepasses"), 0.2f, 1f)).let { if (pr > 0f) mix(it, 0.95f, seg(a.b("asrprofit"), 0f, 0.6f)) else it }
    hourglass73(glass, gh, fill, lerpColor(Color(0xFF8A8494), Qn.Gold, gold), 1f)
    if (tp > 0f) {
        val p = a.b("timepasses")
        val an = PI.toFloat() + spin * 6f * PI.toFloat()
        sun12(glass + Offset(cos(an) * w * 0.38f, sin(an) * h * 0.4f), w * 0.025f, tp * (0.5f + 0.5f * dayness))
        moon12(glass + Offset(cos(an + PI.toFloat()) * w * 0.38f, sin(an + PI.toFloat()) * h * 0.4f), w * 0.02f, tp * (1f - dayness))
        figure(Offset(w * 0.78f, h * 0.9f), h * 0.22f, Color(0xFF2A2430), tp * seg(p, 0.4f, 0.55f))
        tagAt(a.tag("timepasses", 0, 0), Offset(w * 0.5f, h * 0.08f), tp * seg(p, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("timepasses", 0, 1), Offset(w * 0.2f, h * 0.3f), tp * seg(p, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("timepasses", 0, 2), Offset(w * 0.78f, h * 0.56f), tp * seg(p, 0.6f, 0.75f), back = Color(0xE6701E1E), ink = ArtInk.Ivory)
    }
    val icons = listOf("heart", "hands", "book", "mountain")
    val spots = listOf(Offset(0.84f, 0.3f), Offset(0.84f, 0.66f), Offset(0.16f, 0.3f), Offset(0.16f, 0.66f))
    if (ft > 0f) {
        val p = a.b("fourtraits")
        for (k in 0 until 4) {
            val f = seg(p, 0.05f + 0.22f * k, 0.22f + 0.22f * k)
            glyphCard(Offset(w * spots[k].x, h * spots[k].y), w * 0.12f, a.tag("fourtraits", 0, k), ft * f, lit = f) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
        }
    }
    if (so > 0f) {
        val p = a.b("selfothers")
        drawRect(Color(0xFF1A1E30), alpha = 0.6f * so)
        val me = Offset(w * 0.5f, h * 0.88f)
        figure(me, h * 0.3f, Qn.Person, so)
        val chest = me + Offset(0f, -h * 0.2f)
        for (k in 0 until 2) {
            val f = seg(p, 0.05f + 0.15f * k, 0.25f + 0.15f * k)
            softGlow(chest, w * 0.05f, Qn.GoldLight, 0.5f * f * so)
            qIcon(icons[k], chest + Offset((k - 0.5f) * w * 0.07f, -h * 0.12f), w * 0.05f, Qn.GoldLight, so * f)
        }
        tagAt(a.tag("selfothers", 0, 0), Offset(w * 0.5f, h * 0.12f), so * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
        for (k in 0 until 2) {
            val other = Offset(w * (0.16f + 0.68f * k), h * 0.88f)
            val f = seg(p, 0.45f + 0.15f * k, 0.65f + 0.15f * k)
            figure(other, h * 0.26f, Qn.Person, so * seg(p, 0.4f, 0.5f))
            qBeam(chest, other + Offset(0f, -h * 0.18f), f, Qn.GoldLight, so, 1.6f * density)
            qIcon(icons[k + 2], other + Offset(0f, -h * 0.36f), w * 0.06f, Qn.GoldLight, so * f)
        }
        tagAt(a.tag("selfothers", 0, 1), Offset(w * 0.5f, h * 0.26f), so * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (pr > 0f) {
        val p = a.b("asrprofit")
        softGlow(glass + Offset(0f, gh * 0.25f), w * 0.2f, Qn.GoldLight, 0.6f * pr * seg(p, 0.2f, 0.6f))
        // a line of gain climbing beside the glass
        val run = easeOut(seg(p, 0.3f, 0.8f))
        val pts = listOf(Offset(0.66f, 0.86f), Offset(0.74f, 0.74f), Offset(0.8f, 0.78f), Offset(0.9f, 0.48f))
        for (k in 0 until 3) {
            val f = seg(run, k / 3f, (k + 1) / 3f)
            if (f > 0f) drawLine(Qn.Jade, Offset(w * pts[k].x, h * pts[k].y), Offset(w * mix(pts[k].x, pts[k + 1].x, f), h * mix(pts[k].y, pts[k + 1].y, f)), 3f * density, StrokeCap.Round, alpha = pr)
        }
        tagAt(a.tag("asrprofit", 0, 0), Offset(w * 0.5f, h * 0.08f), pr * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("asrprofit", 0, 1), Offset(w * 0.24f, h * 0.3f), pr * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 13. tf-takathur: visitors, not dwellers ═════════════

/**
 * Beats: rivalry (three people side by side, each pile growing taller than the next, while a prayer mat lies forgotten
 * and dim: «اشْتِغَالِهِمْ عَمَّا خُلِقُوا لَهُ»), allkinds (three cards, what people vie in: wealth, children, standing),
 * graveguests (plain graves, a traveller's staff and bag left at one, footprints going in and on toward a far light:
 * «زَائِرِينَ», «مُقِيمِينَ» struck through, «الْبَعْثِ وَالْجَزَاءِ»), barzakhpass (a road through a dim waystation to a lit gate:
 * «الْبَرْزَخُ», «الدَّارِ الْبَاقِيَةِ»).
 */
internal fun DrawScope.visitorsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rivalry" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "allkinds" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "graveguests" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "barzakhpass" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { visitorsDraw(a) }
}

private fun DrawScope.visitorsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val rv = fqIn(a, "rivalry", "allkinds")
    val ak = fqIn(a, "allkinds", "graveguests")
    val vi = fqIn(a, "graveguests", "barzakhpass")
    val bp = fqIn(a, "barzakhpass")
    val ground = h * 0.84f
    val dusk = seg(a.b("graveguests"), 0f, 0.4f).coerceAtLeast(if (bp > 0f) 1f else 0f)
    daySky12(lerpColor(Color(0xFF9CC0E0), Color(0xFF4A4A70), dusk), lerpColor(Color(0xFFF2E2C2), Color(0xFFC89A7A), dusk), ground, lerpColor(Color(0xFFC8B08A), Color(0xFF7A6A5A), dusk))
    if (rv > 0f) {
        val p = a.b("rivalry")
        for (k in 0 until 3) {
            val foot = Offset(w * (0.25f + 0.22f * k), ground)
            figure(foot, h * 0.24f, Color(0xFF3A3040), rv, arms = 0.6f)
            val grow = easeOut(seg(p, 0.1f + 0.1f * k, 0.8f)) * (0.6f + 0.25f * k)
            val n = (grow * 8f).toInt()
            for (m in 0 until n) coin12(foot + Offset(w * 0.06f, -h * (0.02f + 0.03f * m)), w * 0.018f, rv)
        }
        // what they were made for, forgotten at the side
        qIcon("mat", Offset(w * 0.9f, ground - h * 0.08f), w * 0.1f, Qn.GoldLight, rv * (1f - 0.6f * seg(p, 0.3f, 0.8f)))
        tagAt(a.tag("rivalry", 0, 0), Offset(w * 0.5f, h * 0.14f), rv * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ak > 0f) {
        val p = a.b("allkinds")
        drawRect(Color(0xFF1E2234), alpha = 0.55f * ak)
        val icons = listOf("coin", "child", "crown")
        for (k in 0 until 3) {
            val f = seg(p, 0.1f + 0.22f * k, 0.3f + 0.22f * k)
            glyphCard(Offset(w * (0.75f - 0.25f * k), h * 0.5f), w * 0.14f, a.tag("allkinds", 0, k), ak * f) { at, s, al -> qIcon(icons[k], at, s, Qn.GoldLight, al) }
        }
    }
    val graves = listOf(Offset(0.3f, 0f), Offset(0.48f, 0.02f), Offset(0.66f, 0f))
    if (vi > 0f || bp > 0f) {
        val g = (vi + bp).coerceAtMost(1f)
        for (s0 in graves) fqMound(Offset(w * s0.x, ground + h * s0.y), w * 0.12f, h * 0.05f, Color(0xFF8A7A62), g * (if (bp > 0f) 0.5f else 1f))
    }
    if (vi > 0f) {
        val p = a.b("graveguests")
        // a traveller's staff and bag left at the grave: he is passing through
        val at = Offset(w * 0.48f, ground)
        drawLine(Color(0xFF6A4A30), at + Offset(w * 0.07f, h * 0.01f), at + Offset(w * 0.09f, -h * 0.14f), 2.5f * density, StrokeCap.Round, alpha = vi * seg(p, 0.1f, 0.25f))
        drawOval(Color(0xFF8A6A4A), Offset(at.x + w * 0.09f, at.y - h * 0.05f), Size(w * 0.04f, h * 0.05f), alpha = vi * seg(p, 0.1f, 0.25f))
        // footprints that go on toward a far light
        val go = seg(p, 0.3f, 0.8f)
        for (k in 0 until 10) {
            val f = k / 10f
            if (f > go) break
            drawOval(Color(0xFF4A3A2A), Offset(w * (0.56f + 0.035f * k), ground + h * (0.03f - 0.004f * k) + (if (k % 2 == 0) -2f else 2f) * density), Size(w * 0.012f, h * 0.01f), alpha = vi * 0.8f)
        }
        softGlow(Offset(w * 0.96f, ground - h * 0.1f), w * 0.12f, Qn.GoldLight, vi * go)
        tagAt(a.tag("graveguests", 0, 0), Offset(w * 0.48f, h * 0.44f), vi * seg(p, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        struckTag21(a.tag("graveguests", 0, 1), Offset(w * 0.24f, h * 0.44f), vi * seg(p, 0.3f, 0.45f), seg(p, 0.45f, 0.6f), Color(0xFFC0463A), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("graveguests", 0, 2), Offset(w * 0.8f, h * 0.24f), vi * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (bp > 0f) {
        val p = a.b("barzakhpass")
        // a road from this world through a dim waystation to a lit gate
        drawLine(Color(0xFFB8A27A), Offset(0f, ground + h * 0.06f), Offset(w, ground + h * 0.06f), 3f * density, alpha = bp)
        val mid = Offset(w * 0.5f, ground + h * 0.06f)
        drawPath(archPath26(mid.x, mid.y - h * 0.36f, mid.y, w * 0.1f), Color(0xFF3A3448), alpha = bp * 0.9f)
        gate72(Offset(w * 0.88f, ground + h * 0.06f), w * 0.12f, h * 0.42f, seg(p, 0.5f, 0.8f), Color(0xFFC8A060), Qn.GoldLight, bp, t)
        val walk = easeInOut(seg(p, 0.1f, 0.85f))
        figure(Offset(mix(w * 0.08f, w * 0.84f, walk), ground + h * 0.06f), h * 0.2f, Qn.Person, bp * (1f - seg(p, 0.88f, 1f)), walk = if (walk in 0.01f..0.99f) t * 6f else 0f)
        tagAt(a.tag("barzakhpass", 0, 0), Offset(w * 0.5f, h * 0.36f), bp * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("barzakhpass", 0, 1), Offset(w * 0.86f, h * 0.28f), bp * seg(p, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 14. tf-qaria: al-'Adiyat and al-Qari'a ═════════════

/**
 * Beats: stingy (a man holds a bag to his chest and turns from a hand held out; two cards, idleness and holding back:
 * «الْكَسَلُ», «وَالْمَنْعُ»), selfwitness (he sees himself in a mirror, bag and all: «شَاهِدٌ عَلَى ذَلِكَ مِنْ نَفْسِهِ»), qariastrike
 * (one great knock: rings of shock from above and people scattered small: «الْقَارِعَةُ», «تَقْرَعُ النَّاسَ وَتُزْعِجُهُمْ
 * بِأَهْوَالِهَا»), guidedgive (his hand opens and coins go out to the hand held out: «إِلَى وَصْفِ السَّمَاحِ بِأَدَاءِ الْحُقُوقِ»).
 */
internal fun DrawScope.qariadayScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "stingy" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "selfwitness" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "qariastrike" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "guidedgive" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { qariaDraw(a) }
}

private fun DrawScope.qariaDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val st = fqIn(a, "stingy", "selfwitness")
    val wi = fqIn(a, "selfwitness", "qariastrike")
    val qs = fqIn(a, "qariastrike", "guidedgive")
    val gg = fqIn(a, "guidedgive")
    val ground = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(Color(0xFF2A2E44), Color(0xFF4A4458))))
    drawRect(Color(0xFF3A3440), Offset(0f, ground), Size(w, h - ground))
    val man = Offset(w * 0.4f, ground)
    // the man with his bag, in the first, second and last beats
    val manA = st + wi + gg
    if (manA > 0f) {
        val m = manA.coerceAtMost(1f)
        val open = easeOut(seg(a.b("guidedgive"), 0.1f, 0.4f))
        withTransform2(man, if (gg > 0f) 1f else -1f) { figure(man, h * 0.3f, Qn.Person, m, arms = 0.2f + 0.5f * open) }
        if (open < 1f) {
            drawOval(Color(0xFF8A6A3A), Offset(man.x - w * 0.03f, man.y - h * 0.24f), Size(w * 0.06f, h * 0.08f), alpha = m * (1f - open))
        }
    }
    val asker = Offset(w * 0.78f, ground)
    if (st > 0f || gg > 0f) figure(asker, h * 0.26f, Color(0xFF8A8098), (st + gg).coerceAtMost(1f), arms = 0.8f, lean = 0.1f)
    if (st > 0f) {
        val p = a.b("stingy")
        val icons = listOf("bed", "lock")
        for (k in 0 until 2) glyphCard(Offset(w * (0.3f + 0.24f * k), h * 0.24f), w * 0.12f, a.tag("stingy", 0, k), st * seg(p, 0.3f + 0.2f * k, 0.45f + 0.2f * k), rim = Qn.Rose) { at, s, al -> qIcon(icons[k], at, s, Color(0xFFE8DCC0), al) }
    }
    if (wi > 0f) {
        val p = a.b("selfwitness")
        // a tall mirror to his left that shows him, bag and all
        val mc = Offset(w * 0.16f, ground - h * 0.25f)
        val f = seg(p, 0.1f, 0.3f)
        drawRoundRect(Color(0xFF8A7A5A), Offset(mc.x - w * 0.08f, mc.y - h * 0.27f), Size(w * 0.16f, h * 0.52f), CornerRadius(w * 0.08f), alpha = wi * f)
        drawRoundRect(Color(0xFF9AB0C0), Offset(mc.x - w * 0.07f, mc.y - h * 0.26f), Size(w * 0.14f, h * 0.5f), CornerRadius(w * 0.07f), alpha = wi * f)
        figure(Offset(mc.x, ground - h * 0.02f), h * 0.24f, Color(0xFF4A5A6A), wi * seg(p, 0.25f, 0.4f))
        drawOval(Color(0xFF6A5A3A), Offset(mc.x - w * 0.02f, ground - h * 0.21f), Size(w * 0.045f, h * 0.06f), alpha = wi * seg(p, 0.3f, 0.45f))
        tagAt(a.tag("selfwitness", 0, 0), Offset(w * 0.5f, h * 0.16f), wi * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (qs > 0f) {
        val p = a.b("qariastrike")
        drawRect(Color(0xFF0E0E18), alpha = 0.9f * qs)
        val src = Offset(w * 0.5f, h * 0.18f)
        val knock = seg(p, 0.1f, 1f)
        for (k in 0 until 4) {
            val r = w * 0.7f * fract(knock * 1.5f + k * 0.25f)
            drawCircle(Color(0xFFE8DCC0), r, src, alpha = qs * 0.6f * (1f - r / (w * 0.7f)), style = Stroke(2.5f * density))
        }
        for (k in 0 until 12) {
            val r = fract(sin(k * 3.7f) * 43.1f)
            val scatter = easeOut(seg(p, 0.2f, 0.7f))
            val c = Offset(w * (0.1f + 0.8f * r) + (r - 0.5f) * w * 0.2f * scatter, ground + h * 0.04f - h * 0.05f * scatter * fract(r * 7f))
            figure(c, h * 0.08f, Color(0xFF8A8098), qs, lean = (r - 0.5f) * scatter)
        }
        qFit(a.big("qariastrike", 0, 0), src + Offset(0f, h * 0.02f), w * 0.4f, h * 0.14f, Color(0xFFFFF0D8), qs * seg(p, 0.05f, 0.2f), glow = Color(0xFFE8794A))
        tagAt(a.tag("qariastrike", 0, 1), Offset(w * 0.5f, h * 0.5f), qs * seg(p, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (gg > 0f) {
        val p = a.b("guidedgive")
        softGlow(man + Offset(0f, -h * 0.2f), w * 0.1f, Qn.GoldLight, 0.6f * gg * seg(p, 0.1f, 0.4f))
        for (k in 0 until 5) {
            val f = easeInOut(seg(p, 0.3f + 0.08f * k, 0.6f + 0.08f * k))
            val c = Offset(mix(man.x + w * 0.05f, asker.x - w * 0.04f, f), ground - h * 0.2f - h * 0.08f * bump(f))
            coin12(c, w * 0.016f, gg * seg(p, 0.25f, 0.35f) * (1f - seg(f, 0.95f, 1f)))
        }
        tagAt(a.tag("guidedgive", 0, 0), Offset(w * 0.55f, h * 0.18f), gg * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

/** Draws [block] mirrored round [pivot] when [dir] is negative (a figure turned the other way). */
private fun DrawScope.withTransform2(pivot: Offset, dir: Float, block: DrawScope.() -> Unit) {
    if (dir >= 0f) block() else withTransform({ scale(-1f, 1f, pivot = pivot) }) { block() }
}

// ═════════════ 15. tf-zalzala: the weight of an atom ═════════════

/**
 * Beats: earthspeaks (the ground shakes, and the marks of what was done on it light up, gold and dark: «تَشْهَدُ الْأَرْضُ»,
 * «مِنْ خَيْرٍ وَشَرٍّ»), obeysorder (rings rise from the ground as it tells: «أَمَرَهَا أَنْ تُخْبِرَ»), atomspeck (a lens over
 * a balance: a speck no bigger than an atom tips its pan: «مِثْقَالَ الذَّرَّةِ», «فَمَا فَوْقَ ذَلِكَ مِنْ بَابِ أَوْلَى»),
 * evensmall (two small cards on the pans, a drop of water given and a spark of harm: «فِعْلِ الْخَيْرِ وَلَوْ قَلِيلًا», «مِنْ
 * فِعْلِ الشَّرِّ وَلَوْ حَقِيرًا»).
 */
internal fun DrawScope.atomweightScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "earthspeaks" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "obeysorder" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "atomspeck" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "evensmall" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { atomDraw(a) }
}

private fun DrawScope.atomDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val es = fqIn(a, "earthspeaks", "obeysorder")
    val oo = fqIn(a, "obeysorder", "atomspeck")
    val aw = fqIn(a, "atomspeck", "evensmall")
    val ev = fqIn(a, "evensmall")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF1A1E2E), Color(0xFF3A3046))))
    val ground = h * 0.7f
    val earth = es + oo
    if (earth > 0f) {
        val e = earth.coerceAtMost(1f)
        val p = a.b("earthspeaks")
        val shake = if (es > 0f) sin(t * 40f) * h * 0.006f * bump(seg(p, 0f, 0.4f)) else 0f
        drawRect(Color(0xFF5A4636), Offset(0f, ground + shake), Size(w, h - ground), alpha = e)
        for (k in 0 until 6) drawLine(Color(0xFF3A2A20), Offset(w * (0.1f + 0.15f * k), ground + shake), Offset(w * (0.14f + 0.15f * k), ground + h * 0.12f + shake), 1.5f * density, alpha = e * seg(p, 0.05f, 0.25f))
        // what was done on it: marks that light up, gold for good and dark red for evil
        for (k in 0 until 9) {
            val r = fract(sin(k * 5.3f) * 77.7f)
            val c = Offset(w * (0.08f + 0.1f * k), ground + h * (0.08f + 0.14f * r) + shake)
            val on = seg(p, 0.3f + 0.05f * k, 0.4f + 0.05f * k)
            val good = k % 3 != 1
            drawOval(if (good) Qn.GoldLight else Color(0xFFB4402A), Offset(c.x - w * 0.015f, c.y - h * 0.008f), Size(w * 0.03f, h * 0.016f), alpha = e * on)
            if (on > 0f) softGlow(c, w * 0.03f, if (good) Qn.GoldLight else Color(0xFFB4402A), 0.4f * e * on)
        }
        tagAt(a.tag("earthspeaks", 0, 0), Offset(w * 0.5f, h * 0.16f), es * seg(p, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("earthspeaks", 0, 1), Offset(w * 0.5f, h * 0.32f), es * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (oo > 0f) {
        val p = a.b("obeysorder")
        for (k in 0 until 4) {
            val r = w * 0.5f * fract(seg(p, 0.05f, 1f) * 1.5f + k * 0.25f)
            drawArc(Qn.GoldLight, 200f, 140f, false, Offset(w * 0.5f - r, ground - r), Size(r * 2f, r * 2f), alpha = oo * 0.6f * (1f - r / (w * 0.5f)), style = Stroke(2f * density))
        }
        tagAt(a.tag("obeysorder", 0, 0), Offset(w * 0.5f, h * 0.18f), oo * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val foot = Offset(w * 0.5f, h * 0.86f)
    val sz = h * 0.5f
    if (aw > 0f || ev > 0f) {
        val b = (aw + ev).coerceAtMost(1f)
        val p = a.b("atomspeck")
        val tilt = 0.5f * easeOut(seg(p, 0.35f, 0.6f)).coerceAtLeast(if (ev > 0f) 1f else 0f) * (if (ev > 0f) 1f - seg(a.b("evensmall"), 0.6f, 0.9f) * 2f else 1f)
        balance12(foot, sz, tilt, Qn.GoldLight, b)
        val (l, r) = balancePans12(foot, sz, tilt)
        if (aw > 0f) {
            // a speck on the right pan, and a lens that shows it
            val speck = r + Offset(0f, -h * 0.02f)
            drawCircle(Qn.GoldLight, 1.6f * density, speck, alpha = aw)
            val lens = speck + Offset(-w * 0.12f, -h * 0.22f)
            val lf = seg(p, 0.1f, 0.3f)
            drawCircle(Color(0x33FFFFFF), w * 0.07f, lens, alpha = aw * lf)
            drawCircle(Color(0xFFE8DCC0), w * 0.07f, lens, alpha = aw * lf, style = Stroke(3f * density))
            drawLine(Color(0xFFE8DCC0), lens + Offset(w * 0.05f, h * 0.07f), lens + Offset(w * 0.09f, h * 0.13f), 4f * density, StrokeCap.Round, alpha = aw * lf)
            drawCircle(Qn.GoldLight, w * 0.015f, lens, alpha = aw * lf)
            softGlow(lens, w * 0.03f, Qn.GoldLight, 0.6f * aw * lf)
            tagAt(a.tag("atomspeck", 0, 0), lens + Offset(0f, -h * 0.13f), aw * seg(p, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
            tagAt(a.tag("atomspeck", 0, 1), Offset(w * 0.5f, h * 0.08f), aw * seg(p, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
        }
        if (ev > 0f) {
            val q = a.b("evensmall")
            glyphCard(r + Offset(0f, -h * 0.12f), w * 0.1f, a.tag("evensmall", 0, 0), ev * seg(q, 0.05f, 0.2f), rim = Qn.Jade) { at, s, al -> qIcon("drop", at, s, Qn.GoldLight, al) }
            glyphCard(l + Offset(0f, -h * 0.12f), w * 0.1f, a.tag("evensmall", 0, 1), ev * seg(q, 0.45f, 0.6f), rim = Qn.Rose) { at, s, al -> qFlame(at + Offset(0f, s * 0.3f), s * 0.6f, Qn.Ember, al, t) }
        }
    }
}
