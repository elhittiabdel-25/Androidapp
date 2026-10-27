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
 * Wave 43 (2026-10-27): «بصائر»، الجزء الثالث، «السنة النبوية». The Book and its explaining scroll, and the bridge that
 * opens when the scroll is taken away; scrolls written in his lifetime and kept in two cities; a chain of rings tested
 * by five sieves; one trusted messenger who turns the rows of a mosque; a cloak spread and filled; a hundred cards mixed
 * and set right. He ﷺ is only light; companions are their names in roundels; scholars are books; no figures.
 */

private val Back43 = Color(0xE6181420)

/** A closed book standing on a shelf, [s] high, with a title band. */
private fun DrawScope.book43(foot: Offset, s: Float, cover: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(cover, Offset(foot.x - s * 0.18f, foot.y - s), Size(s * 0.36f, s), CornerRadius(s * 0.04f, s * 0.04f), alpha = alpha)
    drawRect(ArtInk.Gold, Offset(foot.x - s * 0.14f, foot.y - s * 0.75f), Size(s * 0.28f, s * 0.06f), alpha = alpha * 0.9f)
    drawRect(ArtInk.Gold, Offset(foot.x - s * 0.14f, foot.y - s * 0.3f), Size(s * 0.28f, s * 0.04f), alpha = alpha * 0.6f)
}

// ═════════════ 1. the Book, its scroll, and the bridge (bs-munkir-sunna) ═════════════

private object BridgeC43 {
    val Sky = Color(0xFF161A30)
    val SkyLow = Color(0xFF2E2E48)
    val Land = Color(0xFF4A4038)
    val Chasm = Color(0xFF0E0C14)
    val Fog = Color(0xFF6A6A78)
    val Plank = Color(0xFF8A6A48)
    val Mat = Color(0xFF6A3A3A)
    val Fish = Color(0xFF9AC0D8)
    val Dome = Color(0xFFD8A83A)
}

/**
 * Beats: scrollaway (the Book and beside it the scroll that explains it; the scroll is pulled away), onerakah (two
 * prayer mats, one under a crescent, one under the dawn, each with a single bow), bridge (from the Book's ground a
 * bridge reaches out over a chasm into fog: «قَنْطَرَةُ الْإِلْحَادِ الْأُولَى»), explains (the scroll comes back, tied to the
 * Book by a gold cord, and small things it makes plain appear: the bows of the prayer, a fish, a locust), qiblaturn (a
 * needle points to a far dome, then turns to the House).
 */
internal fun DrawScope.bridgegapScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "scrollaway" to Shot(Offset(w * 0.32f, h * 0.45f), 1.25f),
        "onerakah" to Shot(Offset(w * 0.7f, h * 0.35f), 1.3f),
        "sunnabridge" to Shot(Offset(w * 0.55f, h * 0.6f), 1.05f),
        "explains" to Shot(Offset(w * 0.3f, h * 0.45f), 1.2f),
        "qiblaturn" to Shot(Offset(w * 0.75f, h * 0.3f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { bridgeNight(a) }
}

private fun DrawScope.bridgeNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sa = a.b("scrollaway")
    val orr = a.b("onerakah")
    val br = a.b("sunnabridge")
    val ex = a.b("explains")
    val qt = a.b("qiblaturn")
    val ground = h * 0.62f
    nightSky(t, BridgeC43.Sky, BridgeC43.SkyLow, ground, starAlpha = 0.6f)
    drawRect(BridgeC43.Land, Offset(0f, ground), Size(w * 0.42f, h - ground))
    drawRect(BridgeC43.Chasm, Offset(w * 0.42f, ground), Size(w * 0.58f, h - ground))
    cloud38(Offset(w * 0.85f, ground + h * 0.05f), w * 0.15f, BridgeC43.Fog, 0.7f)
    // the Book, and the scroll that is pulled away and comes back
    val bf = easeOut(seg(sa, 0f, 0.3f))
    val book = Offset(w * 0.24f, ground - h * 0.12f)
    openBook21(book, w * 0.16f, Color(0xFFF6EED8), Color(0xFF8A6A3A), bf, glow = 0.5f * bf)
    val away = easeInOut(seg(sa, 0.4f, 0.8f)) * (1f - easeInOut(seg(ex, 0f, 0.4f)))
    val scroll = Offset(w * 0.4f + w * 0.5f * away, ground - h * 0.12f - h * 0.2f * away)
    scroll14(scroll, w * 0.08f, h * 0.18f, 1f, bf * (1f - 0.7f * away))
    if (ex > 0f) {
        val g = easeOut(seg(ex, 0.3f, 0.6f))
        drawLine(ArtInk.Gold, book + Offset(w * 0.08f, 0f), scroll + Offset(-w * 0.04f, 0f), 2f * dp, alpha = g)
        val items = listOf(Offset(w * 0.12f, h * 0.24f), Offset(w * 0.26f, h * 0.2f), Offset(w * 0.4f, h * 0.24f))
        for ((k, p) in items.withIndex()) {
            val f = easeOut(seg(ex, 0.4f + 0.15f * k, 0.6f + 0.15f * k))
            softGlow(p, w * 0.04f, ArtInk.Gold, 0.4f * f)
            when (k) {
                0 -> {
                    for (j in 0 until 3) drawArc(ArtInk.BrassLight, 180f, 180f, false, Offset(p.x - w * 0.03f + j * w * 0.02f, p.y - h * 0.01f), Size(w * 0.02f, h * 0.02f), alpha = f, style = Stroke(2f * dp))
                }
                1 -> {
                    drawOval(BridgeC43.Fish, Offset(p.x - w * 0.03f, p.y - h * 0.012f), Size(w * 0.06f, h * 0.024f), alpha = f)
                    drawPath(Path().apply { moveTo(p.x + w * 0.025f, p.y); lineTo(p.x + w * 0.045f, p.y - h * 0.015f); lineTo(p.x + w * 0.045f, p.y + h * 0.015f); close() }, BridgeC43.Fish, alpha = f)
                }
                else -> {
                    drawOval(Color(0xFF9AB05A), Offset(p.x - w * 0.03f, p.y - h * 0.008f), Size(w * 0.06f, h * 0.016f), alpha = f)
                    for (s in listOf(-1f, 1f)) drawLine(Color(0xFF9AB05A), p + Offset(s * w * 0.01f, 0f), p + Offset(s * w * 0.025f, h * 0.02f), 1.2f * dp, alpha = f)
                }
            }
        }
    }
    // one bow at dusk, one at dawn
    if (orr > 0f) {
        val f = easeOut(seg(orr, 0f, 0.3f)) * (1f - 0.7f * seg(br, 0f, 0.4f))
        for (k in 0 until 2) {
            val c = Offset(w * (0.62f + 0.18f * k), h * 0.4f)
            drawRoundRect(BridgeC43.Mat, Offset(c.x - w * 0.05f, c.y - h * 0.08f), Size(w * 0.1f, h * 0.16f), CornerRadius(4f * dp), alpha = f)
            drawArc(ArtInk.BrassLight, 180f, 180f, false, Offset(c.x - w * 0.025f, c.y - h * 0.02f), Size(w * 0.05f, h * 0.04f), alpha = f * seg(orr, 0.3f + 0.2f * k, 0.5f + 0.2f * k), style = Stroke(3f * dp))
            val sky = c + Offset(0f, -h * 0.15f)
            if (k == 0) {
                drawCircle(Color(0xFFF2EAD0), w * 0.02f, sky, alpha = f)
                drawCircle(BridgeC43.SkyLow, w * 0.017f, sky + Offset(w * 0.008f, -h * 0.004f), alpha = f)
            } else {
                drawArc(Color(0xFFFFC870), 180f, 180f, true, Offset(sky.x - w * 0.025f, sky.y - w * 0.01f), Size(w * 0.05f, w * 0.05f), alpha = f)
            }
        }
    }
    // the bridge into the fog
    if (br > 0f) {
        val f = easeOut(seg(br, 0f, 0.3f)) * (1f - 0.5f * seg(ex, 0f, 0.4f))
        val run = easeInOut(seg(br, 0.1f, 0.7f))
        val y = ground + h * 0.02f
        drawLine(BridgeC43.Plank, Offset(w * 0.4f, y), Offset(w * (0.4f + 0.5f * run), y + h * 0.03f * run), 5f * dp, alpha = f)
        for (k in 0 until 10) {
            val u = k / 9f
            if (u <= run) drawLine(BridgeC43.Plank, Offset(w * (0.4f + 0.5f * u), y + h * 0.03f * u - h * 0.03f), Offset(w * (0.4f + 0.5f * u), y + h * 0.03f * u), 2f * dp, alpha = f)
        }
        tagAt(a.tag("sunnabridge"), Offset(w * 0.65f, ground - h * 0.08f), seg(br, 0.5f, 0.8f) * (1f - seg(ex, 0f, 0.4f)), back = Back43, ink = Color(0xFFF0C8C8))
    }
    // the qibla turned: no verse ordered the first, the Sunnah did
    if (qt > 0f) {
        val f = easeOut(seg(qt, 0f, 0.3f))
        val c = Offset(w * 0.75f, h * 0.32f)
        drawCircle(Color(0xFF2A2E44), w * 0.08f, c, alpha = f)
        drawCircle(ArtInk.BrassLight, w * 0.08f, c, alpha = f, style = Stroke(2f * dp))
        val turn = easeInOut(seg(qt, 0.4f, 0.8f))
        val an = -PI.toFloat() / 2f + PI.toFloat() * turn
        drawLine(ArtInk.Gold, c, c + Offset(cos(an), sin(an)) * (w * 0.065f), 3f * dp, StrokeCap.Round, alpha = f)
        val dome = c + Offset(0f, -h * 0.14f)
        drawArc(BridgeC43.Dome, 180f, 180f, true, Offset(dome.x - w * 0.025f, dome.y - h * 0.02f), Size(w * 0.05f, h * 0.05f), alpha = f * (1f - 0.6f * turn))
        kaaba29(c + Offset(0f, h * 0.18f), w * 0.035f, f * seg(qt, 0.5f, 0.8f))
    }
}

// ═════════════ 2. written in his lifetime (bs-kitaba) ═════════════

private object WriteC43 {
    val Wall = Color(0xFF2A2230)
    val WallLow = Color(0xFF3E3244)
    val Shelf = Color(0xFF6A4A32)
    val Line = Color(0xFF8A7A6A)
    val Red = Color(0xFFE0584A)
    val Sheet = Color(0xFFE8DCC0)
    val City = Color(0xFF8A7A9A)
    val Dot = Color(0xFFE8D8B0)
    val Cover = listOf(Color(0xFF5A2E2E), Color(0xFF2E4A5A), Color(0xFF3E5A3A), Color(0xFF5A4A2A))
}

/**
 * Beats: claim200 (a time line from his light; the claim «بَعْدَ قَرْنَيْنِ» sits far along it and is struck out, for
 * writing begins at the start), sahifa (a companion's roundel and a scroll filling with lines: «عَبْدُ اللَّهِ بْنُ
 * عَمْرٍو», «أَلْفُ حَدِيثٍ»), twocopies (one book in two cities: «دِمَشْقَ», «بَرْلِينَ»), fiftytwo (a grid of small quills,
 * «52»), twentythousand (a hall of twenty thousand points of light round one book).
 */
internal fun DrawScope.scrollrackScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "claim200" to Shot(Offset(w * 0.5f, h * 0.2f), 1.2f),
        "sahifa" to Shot(Offset(w * 0.3f, h * 0.45f), 1.3f),
        "twocopies" to Shot(Offset(w * 0.65f, h * 0.5f), 1.2f),
        "fiftytwo" to Shot(Offset(w * 0.5f, h * 0.75f), 1.25f),
        "twentythousand" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { writeRoom(a) }
}

private fun DrawScope.writeRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cl = a.b("claim200")
    val sh = a.b("sahifa")
    val tc = a.b("twocopies")
    val ft = a.b("fiftytwo")
    val tt = a.b("twentythousand")
    val floor = h * 0.9f
    drawRect(Brush.verticalGradient(listOf(WriteC43.Wall, WriteC43.WallLow), endY = floor), size = Size(w, floor))
    drawRect(WriteC43.Shelf, Offset(0f, floor), Size(w, h - floor))
    val away = 1f - 0.85f * seg(tt, 0f, 0.4f)
    // the time line and the claim
    if (cl > 0f) {
        val f = easeOut(seg(cl, 0f, 0.3f)) * away
        val y = h * 0.18f
        val start = Offset(w * 0.12f, y)
        sirajMunir(start, w * 0.01f, f, t, nameAt = start + Offset(w * 0.05f, h * 0.075f))
        drawLine(WriteC43.Line, start, Offset(w * 0.9f, y), 2f * dp, alpha = f)
        val m = Offset(w * 0.78f, y)
        drawLine(WriteC43.Red, m + Offset(0f, -h * 0.03f), m + Offset(0f, h * 0.03f), 2.5f * dp, alpha = f)
        tagAt(a.tag("claim200"), m + Offset(0f, h * 0.07f), seg(cl, 0.3f, 0.55f) * away * (1f - seg(sh, 0.3f, 0.6f)), back = Back43, ink = Color(0xFFFFD8D0))
        cross21(m, w * 0.03f, WriteC43.Red, seg(cl, 0.6f, 0.85f) * f, 3f * dp)
        val ink = easeOut(seg(cl, 0.5f, 0.9f))
        drawLine(ArtInk.Gold, start + Offset(w * 0.03f, 0f), start + Offset(w * 0.03f + w * 0.08f * ink, 0f), 5f * dp, StrokeCap.Round, alpha = f)
    }
    // a companion's scroll filling with lines
    if (sh > 0f) {
        val f = easeOut(seg(sh, 0f, 0.3f)) * away * (1f - 0.5f * seg(tc, 0f, 0.4f))
        val r = Offset(w * 0.16f, h * 0.45f)
        nameRoundel(r, w * 0.05f, a.tag("sahifa", 0, 0), Roundel.Fills[0], Roundel.Rim, f)
        val c = Offset(w * 0.38f, h * 0.46f)
        drawRect(WriteC43.Sheet, Offset(c.x - w * 0.1f, c.y - h * 0.12f), Size(w * 0.2f, h * 0.24f), alpha = f)
        val fill = seg(sh, 0.2f, 0.85f)
        for (k in 0 until 8) {
            val lf = seg(fill, k / 8f, (k + 1) / 8f)
            drawLine(Color(0xFF5A4A3A), Offset(c.x + w * 0.08f, c.y - h * 0.1f + k * h * 0.027f), Offset(c.x + w * 0.08f - w * 0.16f * lf, c.y - h * 0.1f + k * h * 0.027f), 1.5f * dp, alpha = f)
        }
        tagAt(a.tag("sahifa", 0, 1), c + Offset(0f, h * 0.16f), seg(sh, 0.6f, 0.85f) * away * (1f - seg(tc, 0f, 0.4f)), back = Back43, ink = ArtInk.Gold)
    }
    // the same book kept in two cities
    if (tc > 0f) {
        val f = easeOut(seg(tc, 0f, 0.3f)) * away * (1f - 0.5f * seg(ft, 0f, 0.4f))
        for (k in 0 until 2) {
            val base = Offset(w * (0.58f + 0.24f * k), h * 0.62f)
            for (j in 0 until 4) drawRect(WriteC43.City, Offset(base.x - w * 0.09f + j * w * 0.045f, base.y - h * (0.06f + 0.03f * ((j + k) % 3))), Size(w * 0.04f, h * (0.06f + 0.03f * ((j + k) % 3))), alpha = f * 0.6f)
            val g = easeOut(seg(tc, 0.2f + 0.2f * k, 0.5f + 0.2f * k))
            book43(base + Offset(0f, -h * 0.12f), h * 0.12f, WriteC43.Cover[1], f * g)
            softGlow(base + Offset(0f, -h * 0.18f), w * 0.05f, ArtInk.Gold, 0.4f * f * g)
            tagAt(a.tag("twocopies", 0, k), base + Offset(0f, h * 0.05f), seg(tc, 0.4f + 0.2f * k, 0.65f + 0.2f * k) * away * (1f - seg(ft, 0f, 0.3f)), back = Back43, ink = ArtInk.Ivory)
        }
    }
    // fifty-two companions who wrote
    if (ft > 0f) {
        val f = easeOut(seg(ft, 0f, 0.3f)) * away
        for (k in 0 until 52) {
            val g = seg(ft, 0.05f + 0.012f * k, 0.15f + 0.012f * k)
            val p = Offset(w * (0.2f + 0.046f * (k % 13)), h * (0.74f + 0.035f * (k / 13)))
            drawLine(WriteC43.Dot, p, p + Offset(w * 0.01f, -h * 0.02f), 1.5f * dp, StrokeCap.Round, alpha = f * g)
        }
        tagAt(a.tag("fiftytwo"), Offset(w * 0.5f, h * 0.7f), seg(ft, 0.6f, 0.85f) * away, back = Back43, ink = ArtInk.Gold)
    }
    // twenty thousand round one book
    if (tt > 0f) {
        val f = easeOut(seg(tt, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.55f)
        book43(c + Offset(0f, h * 0.05f), h * 0.14f, WriteC43.Cover[0], f)
        softGlow(c, w * 0.08f, ArtInk.Gold, 0.5f * f)
        for (k in 0 until 140) {
            val ring = k / 20
            val an = k * 2.399f
            val r = w * (0.12f + 0.045f * ring)
            val g = seg(tt, 0.1f + 0.004f * k, 0.3f + 0.004f * k)
            drawCircle(WriteC43.Dot, w * 0.004f, c + Offset(cos(an) * r, sin(an) * r * 0.6f), alpha = f * g * (0.6f + 0.4f * sin(t * 2f + k)))
        }
    }
}

// ═════════════ 3. a chain of rings through five sieves (bs-isnad) ═════════════

private object ChainC43 {
    val Sky = Color(0xFF181C30)
    val SkyLow = Color(0xFF2E3048)
    val Ring = Color(0xFFD8B04A)
    val Gap = Color(0xFFE0584A)
    val Sieve = Color(0xFF6A7A9A)
    val Road = Color(0xFF8A7A6A)
    val Moon = Color(0xFFF2EAD0)
}

/**
 * Beats: everyletter (a chain of rings hangs from his light down to a book: every word with its chain), fiveconditions
 * (five sieves at the side, named: «اتِّصَالُ السَّنَدِ», «عَدَالَةُ الرَّاوِي», «ضَبْطُهُ», «الشُّذُوذِ», «الْعِلَّةِ
 * الْقَادِحَةِ»), missinglink (one ring goes missing near the top and the gap glows red: «مُرْسَلًا ضَعِيفًا»), travel (a long
 * road; a roundel walks it while the moon goes through its month: «جَابِرُ بْنُ عَبْدِ اللَّهِ»).
 */
internal fun DrawScope.chainlinksScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "everyletter" to Shot(Offset(w * 0.35f, h * 0.45f), 1.1f),
        "fiveconditions" to Shot(Offset(w * 0.7f, h * 0.45f), 1.15f),
        "missinglink" to Shot(Offset(w * 0.35f, h * 0.3f), 1.4f),
        "travel" to Shot(Offset(w * 0.5f, h * 0.82f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { chainNight(a) }
}

private fun DrawScope.chainNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val el = a.b("everyletter")
    val fc = a.b("fiveconditions")
    val ml = a.b("missinglink")
    val tr = a.b("travel")
    nightSky(t, ChainC43.Sky, ChainC43.SkyLow, h, starAlpha = 0.5f)
    val away = 1f - 0.7f * seg(tr, 0f, 0.4f)
    // the chain
    val top = Offset(w * 0.35f, h * 0.1f)
    val cf = easeOut(seg(el, 0f, 0.3f)) * away
    if (cf > 0f) {
        sirajMunir(top, w * 0.011f, cf, t, nameAt = top + Offset(-w * 0.16f, h * 0.06f))
        val n = 8
        for (k in 0 until n) {
            val c = Offset(top.x, h * (0.17f + 0.06f * k))
            val g = seg(el, 0.1f + 0.07f * k, 0.25f + 0.07f * k)
            val missing = k == 1
            val gone = if (missing) easeOut(seg(ml, 0.2f, 0.5f)) else 0f
            drawOval(ChainC43.Ring, Offset(c.x - w * 0.018f, c.y - h * 0.028f), Size(w * 0.036f, h * 0.056f), alpha = cf * g * (1f - gone), style = Stroke(3f * dp))
            if (missing && gone > 0f) {
                softGlow(c, w * 0.04f, ChainC43.Gap, 0.6f * gone * cf)
                drawOval(ChainC43.Gap, Offset(c.x - w * 0.018f, c.y - h * 0.028f), Size(w * 0.036f, h * 0.056f), alpha = cf * gone, style = Stroke(1.5f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
            }
        }
        val book = Offset(top.x, h * 0.17f + h * 0.06f * n + h * 0.02f)
        openBook21(book, w * 0.12f, Color(0xFFF6EED8), Color(0xFF8A6A3A), cf * seg(el, 0.6f, 0.85f), glow = 0.4f * cf)
        tagAt(a.tag("missinglink"), Offset(top.x + w * 0.17f, h * 0.23f), seg(ml, 0.4f, 0.7f) * (1f - seg(tr, 0f, 0.3f)), back = Color(0xE6501818), ink = Color(0xFFFFD8D0))
    }
    // five sieves
    if (fc > 0f) {
        val f = easeOut(seg(fc, 0f, 0.25f)) * away
        for (k in 0 until 5) {
            val c = Offset(w * 0.74f, h * (0.18f + 0.11f * k))
            val g = easeOut(seg(fc, 0.05f + 0.15f * k, 0.25f + 0.15f * k))
            drawArc(ChainC43.Sieve, 0f, 180f, false, Offset(c.x - w * 0.1f, c.y - h * 0.03f), Size(w * 0.2f, h * 0.06f), alpha = f * g, style = Stroke(2.5f * dp))
            for (j in 0 until 5) drawCircle(ChainC43.Sieve, w * 0.003f, c + Offset(w * (-0.06f + 0.03f * j), h * 0.02f), alpha = f * g)
            tagAt(a.tag("fiveconditions", 0, k), c + Offset(0f, -h * 0.01f), g * away * (1f - seg(ml, 0f, 0.3f)), back = Back43, ink = ArtInk.Ivory)
        }
    }
    // a month's ride for one hadith
    if (tr > 0f) {
        val f = easeOut(seg(tr, 0f, 0.3f))
        val y = h * 0.85f
        drawLine(ChainC43.Road, Offset(w * 0.08f, y), Offset(w * 0.92f, y), 3f * dp, alpha = f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f * dp, 6f * dp)))
        val go = easeInOut(seg(tr, 0.2f, 0.95f))
        nameRoundel(Offset(w * (0.16f + 0.68f * go), y - h * 0.07f), w * 0.045f, a.tag("travel"), Roundel.Fills[1], Roundel.Rim, f)
        val moon = Offset(w * 0.5f, h * 0.62f)
        val phase = go
        drawCircle(ChainC43.Moon, w * 0.035f, moon, alpha = f)
        drawCircle(ChainC43.SkyLow, w * 0.035f, moon + Offset(w * 0.07f * (phase * 2f - 1f), 0f), alpha = f)
    }
}

// ═════════════ 4. one trusted messenger (bs-ahad) ═════════════

private object OneC43 {
    val Sky = Color(0xFFE8C8A0)
    val SkyHigh = Color(0xFF7A9AC0)
    val Ground = Color(0xFFC8A87A)
    val Gold = Color(0xFFF2C45A)
    val Grey = Color(0xFF8A8A96)
    val Row = Color(0xFF4A3A30)
    val Minaret = Color(0xFFE8DCC4)
}

/**
 * Beats: classify (a small pyramid: three at the top and hundreds below, still called «آحاد»), witness (one bright
 * witness beside a crowd of grey unknowns with question marks), qubaturn (rows of worshippers face one way; one messenger
 * arrives and every row turns), envoys (from his light three roundels go out to Yemen: «عَلِيًّا», «مُعَاذًا», «أَبَا
 * مُوسَى»), muezzin (one minaret, one voice, and everyone prays by it).
 */
internal fun DrawScope.onemessengerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "classify" to Shot(Offset(w * 0.25f, h * 0.35f), 1.3f),
        "eyewitness" to Shot(Offset(w * 0.7f, h * 0.35f), 1.3f),
        "qubaturn" to Shot(Offset(w * 0.5f, h * 0.72f), 1.2f),
        "envoys" to Shot(Offset(w * 0.5f, h * 0.4f), 1.05f),
        "muezzin" to Shot(Offset(w * 0.82f, h * 0.55f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { oneDay(a) }
}

private fun DrawScope.oneDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cl = a.b("classify")
    val wi = a.b("eyewitness")
    val qt = a.b("qubaturn")
    val en = a.b("envoys")
    val mu = a.b("muezzin")
    val ground = h * 0.58f
    drawRect(Brush.verticalGradient(listOf(OneC43.SkyHigh, OneC43.Sky), endY = ground), size = Size(w, ground))
    drawRect(OneC43.Ground, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - 0.6f * seg(en, 0f, 0.4f)
    // three at the top, hundreds below
    if (cl > 0f) {
        val f = easeOut(seg(cl, 0f, 0.3f)) * away
        for (k in 0 until 3) drawCircle(OneC43.Gold, w * 0.012f, Offset(w * (0.21f + 0.04f * k), h * 0.18f), alpha = f)
        for (r in 0 until 4) for (k in 0 until 6 + 4 * r) {
            val g = seg(cl, 0.2f + 0.1f * r, 0.4f + 0.1f * r)
            drawCircle(Color(0xFFE8E0D0), w * 0.005f, Offset(w * (0.25f - 0.012f * (6 + 4 * r) / 2f + 0.012f * k), h * (0.24f + 0.05f * r)), alpha = f * g)
        }
        tagAt(a.tag("classify"), Offset(w * 0.25f, h * 0.48f), seg(cl, 0.6f, 0.85f) * away * (1f - seg(wi, 0f, 0.3f)), back = Back43, ink = ArtInk.Gold)
    }
    // one trusted witness, many unknowns
    if (wi > 0f) {
        val f = easeOut(seg(wi, 0f, 0.3f)) * away * (1f - 0.6f * seg(qt, 0f, 0.4f))
        val one = Offset(w * 0.56f, h * 0.32f)
        softGlow(one, w * 0.05f, OneC43.Gold, 0.6f * f)
        drawCircle(OneC43.Gold, w * 0.02f, one, alpha = f)
        check21(one + Offset(0f, h * 0.06f), w * 0.015f, ArtInk.Jade, seg(wi, 0.4f, 0.6f), 2.5f * dp)
        for (k in 0 until 8) {
            val p = Offset(w * (0.72f + 0.06f * (k % 4)), h * (0.26f + 0.08f * (k / 4)))
            drawCircle(OneC43.Grey, w * 0.015f, p, alpha = f * seg(wi, 0.2f + 0.04f * k, 0.4f + 0.04f * k))
            qmark21(p + Offset(0f, -h * 0.035f), h * 0.02f, Color(0xFF5A5A66), f * seg(wi, 0.5f, 0.8f))
        }
    }
    // the rows of Quba turn on one man's word
    if (qt > 0f) {
        val f = easeOut(seg(qt, 0f, 0.3f)) * away
        val c = Offset(w * 0.45f, h * 0.76f)
        val turn = easeInOut(seg(qt, 0.45f, 0.85f))
        for (r in 0 until 3) for (k in 0 until 7) {
            val p = c + Offset(w * (-0.15f + 0.05f * k), h * (-0.05f + 0.05f * r))
            val an = -PI.toFloat() / 2f + PI.toFloat() * turn
            drawLine(OneC43.Row, p, p + Offset(cos(an), sin(an)) * (h * 0.025f), 3f * dp, StrokeCap.Round, alpha = f)
            drawCircle(OneC43.Row, w * 0.007f, p + Offset(cos(an), sin(an)) * (h * 0.03f), alpha = f)
        }
        val come = easeInOut(seg(qt, 0.1f, 0.4f))
        val m = Offset(w * (0.95f - 0.25f * come), h * 0.76f)
        drawCircle(OneC43.Gold, w * 0.014f, m, alpha = f)
        softGlow(m, w * 0.04f, OneC43.Gold, 0.5f * f)
    }
    // envoys, one at a time, teaching faith itself
    if (en > 0f) {
        val f = easeOut(seg(en, 0f, 0.3f)) * (1f - 0.5f * seg(mu, 0f, 0.4f))
        val src = Offset(w * 0.2f, h * 0.22f)
        sirajMunir(src, w * 0.011f, f, t, nameAt = src + Offset(0f, -h * 0.1f))
        for (k in 0 until 3) {
            val go = easeInOut(seg(en, 0.15f + 0.15f * k, 0.6f + 0.15f * k))
            val to = Offset(w * (0.62f + 0.12f * k), h * (0.32f + 0.06f * (k % 2)))
            val p = src + (to - src) * go
            nameRoundel(p, w * 0.04f, a.tag("envoys", 0, k), Roundel.Fills[k], Roundel.Rim, f * seg(en, 0.1f + 0.15f * k, 0.2f + 0.15f * k))
        }
    }
    // one muezzin
    if (mu > 0f) {
        val f = easeOut(seg(mu, 0f, 0.3f))
        val base = Offset(w * 0.84f, ground + h * 0.1f)
        drawRect(OneC43.Minaret, Offset(base.x - w * 0.025f, base.y - h * 0.3f), Size(w * 0.05f, h * 0.3f), alpha = f)
        drawArc(OneC43.Minaret, 180f, 180f, true, Offset(base.x - w * 0.03f, base.y - h * 0.34f), Size(w * 0.06f, h * 0.06f), alpha = f)
        for (j in 0 until 3) {
            val u = fract(t * 0.6f + j / 3f)
            val r = w * (0.03f + 0.12f * u)
            drawArc(OneC43.Gold, 200f, 140f, false, Offset(base.x - r, base.y - h * 0.3f - r), Size(r * 2f, r * 2f), alpha = f * (1f - u), style = Stroke(2f * dp))
        }
    }
}

// ═════════════ 5. a cloak spread and filled (bs-abu-hurayra) ═════════════

private object CloakC43 {
    val Sky = Color(0xFF1C1E34)
    val SkyLow = Color(0xFF3A3450)
    val Cloth = Color(0xFF8A6A4A)
    val Light = Color(0xFFFFE8B0)
    val Ledger = Color(0xFFF2EAD8)
    val Ink = Color(0xFF5A4A3A)
    val Dot = Color(0xFFE8D8B0)
}

/**
 * Beats: closest (his light, and close beside it, never far, a roundel: «أَبُو هُرَيْرَةَ»), cloak (a cloak spread out;
 * handfuls of light poured into it, then it is folded), marwantest (two ledgers a year apart: line for line the same, a
 * tick on each), eighthundred (from his roundel, many small lights go out: «800»).
 */
internal fun DrawScope.cloakfullScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "closest" to Shot(Offset(w * 0.35f, h * 0.3f), 1.3f),
        "cloak" to Shot(Offset(w * 0.35f, h * 0.6f), 1.3f),
        "marwantest" to Shot(Offset(w * 0.72f, h * 0.45f), 1.25f),
        "eighthundred" to Shot(Offset(w * 0.5f, h * 0.45f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { cloakNight(a) }
}

private fun DrawScope.cloakNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cl = a.b("closest")
    val ck = a.b("cloak")
    val mt = a.b("marwantest")
    val eh = a.b("eighthundred")
    nightSky(t, CloakC43.Sky, CloakC43.SkyLow, h, starAlpha = 0.6f)
    val light = Offset(w * 0.3f, h * 0.24f)
    val lf = easeOut(seg(cl, 0f, 0.3f)) * (1f - 0.7f * seg(eh, 0f, 0.4f))
    sirajMunir(light, w * 0.012f, lf, t, nameAt = light + Offset(-w * 0.15f, h * 0.01f))
    // one roundel for him all through: beside the light, then at the centre of those who took from him
    val ab = Offset(w * 0.42f + w * 0.01f * sin(t * 0.8f), h * 0.3f) +
        (Offset(w * 0.5f, h * 0.45f) - Offset(w * 0.42f, h * 0.3f)) * easeInOut(seg(eh, 0f, 0.4f))
    val rf = easeOut(seg(cl, 0.2f, 0.5f))
    // the cloak
    if (ck > 0f) {
        val f = easeOut(seg(ck, 0f, 0.3f)) * (1f - 0.6f * seg(mt, 0f, 0.4f))
        val c = Offset(w * 0.35f, h * 0.62f)
        val fold = easeInOut(seg(ck, 0.75f, 0.95f))
        val half = w * 0.16f * (1f - 0.6f * fold)
        drawRoundRect(CloakC43.Cloth, Offset(c.x - half, c.y - h * 0.06f), Size(half * 2f, h * 0.12f), CornerRadius(6f * dp), alpha = f)
        for (k in 0 until 3) {
            val u = seg(ck, 0.25f + 0.15f * k, 0.45f + 0.15f * k)
            val p = light + (c + Offset(w * (-0.06f + 0.06f * k), 0f) - light) * easeInOut(u)
            drawCircle(CloakC43.Light, w * 0.012f, p, alpha = f * bump(u))
        }
        softGlow(c, w * 0.12f, CloakC43.Light, 0.5f * f * seg(ck, 0.5f, 0.9f))
    }
    // Marwan's test: the same, a year later
    if (mt > 0f) {
        val f = easeOut(seg(mt, 0f, 0.3f)) * (1f - 0.6f * seg(eh, 0f, 0.4f))
        for (k in 0 until 2) {
            val c = Offset(w * (0.62f + 0.2f * k), h * 0.48f)
            val g = easeOut(seg(mt, 0.1f + 0.3f * k, 0.4f + 0.3f * k))
            drawRect(CloakC43.Ledger, Offset(c.x - w * 0.08f, c.y - h * 0.12f), Size(w * 0.16f, h * 0.24f), alpha = f * g)
            for (r in 0 until 7) {
                val len = w * (0.08f + 0.05f * w8Rnd(r, 5.3f))
                drawLine(CloakC43.Ink, Offset(c.x + w * 0.06f, c.y - h * 0.09f + r * h * 0.028f), Offset(c.x + w * 0.06f - len, c.y - h * 0.09f + r * h * 0.028f), 1.5f * dp, alpha = f * g)
            }
            check21(c + Offset(0f, h * 0.16f), w * 0.02f, ArtInk.Jade, seg(mt, 0.75f, 0.95f) * f, 3f * dp)
        }
    }
    nameRoundel(ab, w * 0.05f, a.tag("closest"), Roundel.Fills[1], Roundel.Rim, rf * (1f - 0.3f * seg(ck, 0f, 0.3f) * (1f - seg(eh, 0f, 0.3f))))
    // eight hundred who took from him
    if (eh > 0f) {
        val f = easeOut(seg(eh, 0f, 0.3f))
        val c = Offset(w * 0.5f, h * 0.45f)
        for (k in 0 until 80) {
            val an = k * 2.399f
            val r = w * (0.09f + 0.3f * fract(k * 0.618f)) * easeOut(seg(eh, 0.1f, 0.7f))
            drawCircle(CloakC43.Dot, w * 0.004f, c + Offset(cos(an) * r, sin(an) * r * 0.7f), alpha = f * (0.6f + 0.4f * sin(t * 2f + k)))
        }
        tagAt(a.tag("eighthundred"), c + Offset(0f, h * 0.1f), seg(eh, 0.5f, 0.8f), back = Back43, ink = ArtInk.Gold)
    }
}

// ═════════════ 6. a hundred cards mixed and set right (bs-bukhari) ═════════════

private object CardC43 {
    val Wall = Color(0xFF241E2C)
    val WallLow = Color(0xFF3A3040)
    val Shelf = Color(0xFF6A4A32)
    val Top = listOf(Color(0xFFD8B04A), Color(0xFF6AA0C8), Color(0xFF8AC08A), Color(0xFFE08A6A), Color(0xFFB08AD0))
    val House = Color(0xFFD8C8A8)
    val Door = Color(0xFF6A4A32)
    val Covers = listOf(Color(0xFF5A2E2E), Color(0xFF2E4A5A), Color(0xFF3E5A3A))
}

/**
 * Beats: olderbooks (older books on a shelf, the hadiths running from them into his: «مُوَطَّأُ مَالِكٍ»), swapped (ten
 * cards, each half of a pair matched to the wrong half), restored (the halves slide home, colour to colour, in order),
 * witnessgate (a house whose door is set in a ring of light: «بَابُ الْإِسْلَامِ الصَّحَابَةُ»).
 */
internal fun DrawScope.hundredswapScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "olderbooks" to Shot(Offset(w * 0.3f, h * 0.35f), 1.25f),
        "swapped" to Shot(Offset(w * 0.55f, h * 0.65f), 1.15f),
        "restored" to Shot(Offset(w * 0.55f, h * 0.65f), 1.15f),
        "witnessgate" to Shot(Offset(w * 0.5f, h * 0.45f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { cardsRoom(a) }
}

private fun DrawScope.cardsRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ob = a.b("olderbooks")
    val sw = a.b("swapped")
    val rs = a.b("restored")
    val wg = a.b("witnessgate")
    val floor = h * 0.9f
    drawRect(Brush.verticalGradient(listOf(CardC43.Wall, CardC43.WallLow), endY = floor), size = Size(w, floor))
    drawRect(CardC43.Shelf, Offset(0f, floor), Size(w, h - floor))
    val away = 1f - 0.85f * seg(wg, 0f, 0.4f)
    // the older books, and his
    if (ob > 0f) {
        val f = easeOut(seg(ob, 0f, 0.3f)) * away
        val shelfY = h * 0.4f
        drawRect(CardC43.Shelf, Offset(w * 0.05f, shelfY), Size(w * 0.5f, h * 0.015f), alpha = f)
        for (k in 0 until 4) book43(Offset(w * (0.1f + 0.07f * k), shelfY), h * (0.14f + 0.02f * (k % 2)), CardC43.Covers[k % 3], f * seg(ob, 0.05f * k, 0.2f + 0.05f * k))
        val his = Offset(w * 0.48f, shelfY)
        book43(his, h * 0.2f, Color(0xFF2E2A5A), f * seg(ob, 0.3f, 0.5f))
        for (k in 0 until 4) {
            val u = fract(t * 0.4f + k * 0.25f)
            val from = Offset(w * (0.1f + 0.07f * k), shelfY - h * 0.16f)
            val to = his + Offset(0f, -h * 0.12f)
            drawCircle(ArtInk.Gold, w * 0.005f, from + (to - from) * u + Offset(0f, -h * 0.06f * sin(PI.toFloat() * u)), alpha = f * seg(ob, 0.4f, 0.6f) * (1f - u))
        }
        tagAt(a.tag("olderbooks"), Offset(w * 0.17f, shelfY + h * 0.06f), seg(ob, 0.5f, 0.8f) * away * (1f - seg(sw, 0f, 0.3f)), back = Back43, ink = ArtInk.Gold)
    }
    // ten cards: chains on wrong texts, then each set home
    if (sw > 0f) {
        val f = easeOut(seg(sw, 0f, 0.3f)) * away
        val back = easeInOut(seg(rs, 0.1f, 0.85f))
        val wrong = listOf(3, 0, 4, 1, 2)
        for (k in 0 until 5) {
            val x = w * (0.2f + 0.15f * k)
            val y = h * 0.66f
            drawRoundRect(CardC43.Top[k], Offset(x - w * 0.05f, y - h * 0.1f), Size(w * 0.1f, h * 0.07f), CornerRadius(4f * dp), alpha = f * seg(sw, 0.05f * k, 0.2f + 0.05f * k))
            val j = wrong[k]
            val fromX = w * (0.2f + 0.15f * k)
            val homeX = w * (0.2f + 0.15f * j)
            val bx = mix(fromX, homeX, back)
            val by = y + h * 0.02f + h * 0.08f * bump(back)
            drawRoundRect(CardC43.Top[j], Offset(bx - w * 0.05f, by - h * 0.02f + h * 0.04f), Size(w * 0.1f, h * 0.07f), CornerRadius(4f * dp), alpha = f * seg(sw, 0.2f + 0.05f * k, 0.35f + 0.05f * k) * 0.75f)
        }
        if (back >= 1f) for (k in 0 until 5) check21(Offset(w * (0.2f + 0.15f * k), h * 0.86f), w * 0.015f, ArtInk.Jade, seg(rs, 0.85f, 1f), 2.5f * dp)
    }
    // the door of Islam is its companions
    if (wg > 0f) {
        val f = easeOut(seg(wg, 0f, 0.35f))
        val c = Offset(w * 0.5f, floor)
        drawRect(CardC43.House, Offset(c.x - w * 0.22f, c.y - h * 0.4f), Size(w * 0.44f, h * 0.4f), alpha = f)
        drawPath(Path().apply { moveTo(c.x - w * 0.26f, c.y - h * 0.4f); lineTo(c.x, c.y - h * 0.56f); lineTo(c.x + w * 0.26f, c.y - h * 0.4f); close() }, Color(0xFF8A5A3A), alpha = f)
        drawPath(archPath26(c.x, c.y - h * 0.22f, c.y, w * 0.06f), CardC43.Door, alpha = f)
        val ring = easeOut(seg(wg, 0.3f, 0.7f))
        for (k in 0 until 10) {
            val an = PI.toFloat() * (1f + k / 9f)
            val p = Offset(c.x, c.y - h * 0.12f) + Offset(cos(an) * w * 0.1f, sin(an) * h * 0.12f)
            drawCircle(ArtInk.Gold, w * 0.008f, p, alpha = f * seg(ring, k / 10f, (k + 1) / 10f))
        }
        softGlow(Offset(c.x, c.y - h * 0.12f), w * 0.12f, ArtInk.Gold, 0.4f * f * ring)
        tagAt(a.tag("witnessgate"), Offset(c.x, c.y - h * 0.62f), seg(wg, 0.5f, 0.8f), back = Back43, ink = ArtInk.Gold)
    }
}
