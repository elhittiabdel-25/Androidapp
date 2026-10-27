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
 * Wave 41 (2026-10-27): «بصائر»، الجزء الثالث، «القرآن المحفوظ». A time line on which the Syriac tale lands too late; the
 * two horns of a king who reached east and west, and a gate that stays open; dots moved on paper while the voice goes
 * on from mouth to ear; a parchment written, wiped and corrected under the dome of Sana'a; one spring in seven streams;
 * a staircase of gradual law. Letters are never imitated: words are bars and dots. He ﷺ is only light, Ibrahim a
 * small light with his name, companions their names in roundels, the angel a light.
 */

private val Back41 = Color(0xE6181420)

/** A line of "writing" without letters: short rounded bars with a few dots, so no script is imitated. */
private fun DrawScope.bars41(start: Offset, len: Float, h: Float, color: Color, alpha: Float, seed: Int, dots: Boolean = true) {
    if (alpha <= 0.01f) return
    var x = start.x
    var k = seed
    while (x > start.x - len) {
        val wd = len * (0.05f + 0.07f * w8Rnd(k, 3.1f))
        drawRoundRect(color, Offset(x - wd, start.y - h * 0.15f), Size(wd, h * 0.3f), CornerRadius(h * 0.15f, h * 0.15f), alpha = alpha)
        if (dots && w8Rnd(k, 7.7f) > 0.6f) drawCircle(color, h * 0.12f, Offset(x - wd * 0.5f, start.y - h * 0.4f), alpha = alpha)
        x -= wd + len * 0.03f
        k++
    }
}

// ═════════════ 1. a tale dated too late (bs-suryani) ═════════════

private object DateC41 {
    val Sky = Color(0xFF1A1C30)
    val SkyLow = Color(0xFF3A3448)
    val Ground = Color(0xFF6A5A48)
    val Mecca = Color(0xFF3A6A5A)
    val Medina = Color(0xFF5A5A8A)
    val Scroll = Color(0xFFD8C8A0)
    val Red = Color(0xFFE0584A)
    val Sheep = Color(0xFFF2EEE4)
    val Far = Color(0xFF7A6A8A)
}

/**
 * Beats: claimscroll (the claim: a Syriac scroll and the open Book, a dashed arrow from one to the other), timeline629
 * (a time line, the Meccan years then the Medinan; the scroll slides to its place near the end, «629»), meccankahf (the
 * Book glows in the Meccan years, «مَكِّيَّةٌ», and the claimed arrow is struck out), farchurch (his light with a flock
 * by the House; far away a domed building, «آشُورَ», and the dashed road between them breaks).
 */
internal fun DrawScope.datedscrollsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "claimscroll" to Shot(Offset(w * 0.5f, h * 0.3f), 1.3f),
        "timeline629" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "meccankahf" to Shot(Offset(w * 0.32f, h * 0.45f), 1.25f),
        "farchurch" to Shot(Offset(w * 0.5f, h * 0.78f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { datedNight(a) }
}

private fun DrawScope.datedNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cl = a.b("claimscroll")
    val tl = a.b("timeline629")
    val mk = a.b("meccankahf")
    val fc = a.b("farchurch")
    val ground = h * 0.68f
    nightSky(t, DateC41.Sky, DateC41.SkyLow, ground, starAlpha = 0.6f)
    drawRect(DateC41.Ground, Offset(0f, ground), Size(w, h - ground))
    // the time line
    val lineY = h * 0.5f
    val tf = easeOut(seg(tl, 0f, 0.4f))
    if (tf > 0f) {
        drawRect(DateC41.Mecca, Offset(w * 0.1f, lineY - h * 0.02f), Size(w * 0.5f * tf, h * 0.04f), alpha = 0.9f)
        drawRect(DateC41.Medina, Offset(w * 0.6f, lineY - h * 0.02f), Size(w * 0.3f * seg(tl, 0.2f, 0.5f), h * 0.04f), alpha = 0.9f)
        val m = Offset(w * 0.86f, lineY)
        drawLine(DateC41.Red, m + Offset(0f, -h * 0.05f), m + Offset(0f, h * 0.05f), 2.5f * dp, alpha = seg(tl, 0.45f, 0.6f))
        tagAt(a.tag("timeline629"), m + Offset(0f, h * 0.09f), seg(tl, 0.5f, 0.7f), back = Back41, ink = Color(0xFFFFD8D0))
    }
    // the scroll: first beside the Book, then where it belongs on the time line
    val book = Offset(w * 0.32f, h * 0.3f)
    val scrollFrom = Offset(w * 0.68f, h * 0.3f)
    val scrollTo = Offset(w * 0.86f, lineY - h * 0.12f)
    val sf = easeOut(seg(cl, 0f, 0.3f))
    if (sf > 0f) {
        val move = easeInOut(seg(tl, 0.4f, 0.8f))
        val sc = scrollFrom + (scrollTo - scrollFrom) * move
        scroll14(sc, w * 0.12f * (1f - 0.3f * move), h * 0.16f * (1f - 0.3f * move), 1f, sf)
        val glow = easeOut(seg(mk, 0f, 0.5f))
        val bookAt = book + (Offset(w * 0.3f, lineY - h * 0.12f) - book) * easeInOut(seg(tl, 0.4f, 0.8f))
        openBook21(bookAt, w * 0.14f, Color(0xFFF6EED8), Color(0xFF8A6A3A), sf, glow = 0.3f + 0.7f * glow)
        // the claimed arrow from the scroll to the Book, later struck out
        val arr = seg(cl, 0.3f, 0.7f) * (1f - seg(tl, 0f, 0.3f)) + seg(mk, 0.1f, 0.4f) * 0.8f
        val from = sc + Offset(-w * 0.08f, 0f)
        val to = bookAt + Offset(w * 0.1f, 0f)
        if (arr > 0f) {
            drawLine(Color(0xFFE8D8B8), from, to, 2f * dp, alpha = arr, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp)))
            arrowHead(to, to - from, w * 0.02f, Color(0xFFE8D8B8), 2f * dp, arr)
            cross21((from + to) / 2f, w * 0.04f, DateC41.Red, seg(mk, 0.4f, 0.7f), 4f * dp)
        }
        tagAt(a.tag("meccankahf"), bookAt + Offset(0f, -h * 0.1f), seg(mk, 0.3f, 0.6f) * (1f - seg(fc, 0f, 0.3f)), back = Back41, ink = ArtInk.Gold)
    }
    // a shepherd's light by the House, and a church a world away
    if (fc > 0f) {
        val f = easeOut(seg(fc, 0f, 0.3f))
        val house = Offset(w * 0.2f, h * 0.9f)
        kaaba29(house, w * 0.05f, f)
        sirajMunir(house + Offset(w * 0.09f, -h * 0.1f), w * 0.01f, f, t, nameAt = house + Offset(w * 0.09f, -h * 0.19f))
        for (k in 0 until 4) sheep26(house + Offset(w * (0.14f + 0.04f * k), -h * 0.01f * (k % 2)), w * 0.02f, DateC41.Sheep, f)
        val far = Offset(w * 0.84f, h * 0.88f)
        domed29(far, w * 0.06f, DateC41.Far, Color(0xFF9A8AA8), f * 0.8f)
        tagAt(a.tag("farchurch"), far + Offset(0f, -h * 0.12f), seg(fc, 0.3f, 0.55f), back = Back41, ink = Color(0xFFE0D8E8))
        val road = seg(fc, 0.3f, 0.7f)
        drawLine(Color(0xFFB8A888), house + Offset(w * 0.32f, -h * 0.04f), house + Offset(w * 0.32f + w * 0.28f * road, -h * 0.04f), 1.5f * dp, alpha = f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp)))
        cross21(Offset(w * 0.67f, h * 0.86f), w * 0.03f, DateC41.Red, seg(fc, 0.7f, 0.95f), 3f * dp)
    }
}

// ═════════════ 2. east and west, and a gate that stays open (bs-dhulqarnayn) ═════════════

private object HornC41 {
    val Sky = Color(0xFFE8B888)
    val SkyHigh = Color(0xFF6A7AA8)
    val Ground = Color(0xFF8A6A48)
    val Sun = Color(0xFFFFD890)
    val Coin = Color(0xFFC8A060)
    val Crown = Color(0xFFD8B04A)
    val Ram = Color(0xFF4A3A30)
    val Gate = Color(0xFF7A5A3A)
    val Red = Color(0xFFE0584A)
}

/**
 * Beats: eastwest (a long road from the setting sun on one side to the rising sun on the other: «الْمَشْرِقُ»,
 * «الْمَغْرِبُ»), idolhorns (a coin's profile with two small curled horns, struck out: «قَرْنَا تَأْلِيهٍ»), twohorns (a crown
 * with two great horns reaching both ways: «شَرْقًا», «غَرْبًا»), danielram (a ram with two tall horns: «مَادِي», «فَارِسَ»),
 * opendoors (two leaves of a great gate swing open and stay open).
 */
internal fun DrawScope.tworamsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "eastwest" to Shot(Offset(w * 0.5f, h * 0.5f), 1.02f),
        "idolhorns" to Shot(Offset(w * 0.25f, h * 0.3f), 1.4f),
        "twohorns" to Shot(Offset(w * 0.5f, h * 0.3f), 1.25f),
        "danielram" to Shot(Offset(w * 0.72f, h * 0.62f), 1.3f),
        "opendoors" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { hornsPlain(a) }
}

private fun DrawScope.hornsPlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ew = a.b("eastwest")
    val ih = a.b("idolhorns")
    val th = a.b("twohorns")
    val dr = a.b("danielram")
    val od = a.b("opendoors")
    val ground = h * 0.66f
    drawRect(Brush.verticalGradient(listOf(HornC41.SkyHigh, HornC41.Sky), endY = ground), size = Size(w, ground))
    drawRect(HornC41.Ground, Offset(0f, ground), Size(w, h - ground))
    // the road from sunset to sunrise
    val road = easeInOut(seg(ew, 0.1f, 0.7f))
    if (ew > 0f) {
        val f = easeOut(seg(ew, 0f, 0.3f))
        for ((k, x) in listOf(w * 0.06f, w * 0.94f).withIndex()) {
            val c = Offset(x, ground - h * 0.01f)
            softGlow(c, w * 0.08f, HornC41.Sun, 0.6f * f)
            drawArc(HornC41.Sun, 180f, 180f, true, Offset(c.x - w * 0.045f, c.y - w * 0.045f), Size(w * 0.09f, w * 0.09f), alpha = f)
            tagAt(a.tag("eastwest", 0, k), c + Offset(if (k == 0) w * 0.08f else -w * 0.08f, -h * 0.1f), seg(ew, 0.5f + 0.15f * k, 0.75f + 0.15f * k) * (1f - seg(th, 0f, 0.3f)), back = Back41, ink = ArtInk.Gold)
        }
        drawLine(Color(0xFFE8D2A0), Offset(w * 0.5f - w * 0.42f * road, ground + h * 0.05f), Offset(w * 0.5f + w * 0.42f * road, ground + h * 0.05f), 4f * dp, StrokeCap.Round, alpha = f)
    }
    // a coin with the small horns of a god: not him
    if (ih > 0f) {
        val f = easeOut(seg(ih, 0f, 0.3f)) * (1f - 0.7f * seg(th, 0f, 0.4f))
        val c = Offset(w * 0.25f, h * 0.3f)
        drawCircle(HornC41.Coin, w * 0.08f, c, alpha = f)
        drawCircle(Color(0xFF8A6A3A), w * 0.08f, c, alpha = f, style = Stroke(2f * dp))
        drawOval(Color(0xFF8A6A3A), Offset(c.x - w * 0.03f, c.y - h * 0.05f), Size(w * 0.06f, h * 0.09f), alpha = f * 0.5f)
        drawArc(Color(0xFF5A4020), 90f, 270f, false, Offset(c.x - w * 0.045f, c.y - h * 0.035f), Size(w * 0.03f, h * 0.035f), alpha = f, style = Stroke(2.5f * dp))
        cross21(c, w * 0.07f, HornC41.Red, seg(ih, 0.5f, 0.8f) * f, 4f * dp)
        tagAt(a.tag("idolhorns"), c + Offset(0f, h * 0.12f), seg(ih, 0.3f, 0.6f) * (1f - seg(th, 0f, 0.4f)), back = Back41, ink = Color(0xFFFFE0D0))
    }
    // a crown with two great horns, east and west
    if (th > 0f) {
        val f = easeOut(seg(th, 0f, 0.3f)) * (1f - 0.5f * seg(dr, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.3f)
        drawRect(HornC41.Crown, Offset(c.x - w * 0.06f, c.y - h * 0.02f), Size(w * 0.12f, h * 0.05f), alpha = f)
        for (k in 0 until 3) drawPath(Path().apply { val x = c.x - w * 0.05f + k * w * 0.05f; moveTo(x - w * 0.015f, c.y - h * 0.02f); lineTo(x, c.y - h * 0.06f); lineTo(x + w * 0.015f, c.y - h * 0.02f); close() }, HornC41.Crown, alpha = f)
        val reach = easeOut(seg(th, 0.2f, 0.7f))
        for (s in listOf(-1f, 1f)) {
            val p = Path().apply {
                moveTo(c.x + s * w * 0.06f, c.y)
                cubicTo(c.x + s * w * 0.14f, c.y - h * 0.02f, c.x + s * w * (0.14f + 0.12f * reach), c.y - h * 0.08f, c.x + s * w * (0.12f + 0.24f * reach), c.y - h * 0.14f)
            }
            drawPath(p, HornC41.Crown, alpha = f, style = Stroke(w * 0.018f, cap = StrokeCap.Round))
        }
        tagAt(a.tag("twohorns", 0, 0), c + Offset(w * 0.32f, -h * 0.05f), seg(th, 0.6f, 0.85f) * (1f - seg(dr, 0f, 0.4f)), back = Back41, ink = ArtInk.Gold)
        tagAt(a.tag("twohorns", 0, 1), c + Offset(-w * 0.32f, -h * 0.05f), seg(th, 0.65f, 0.9f) * (1f - seg(dr, 0f, 0.4f)), back = Back41, ink = ArtInk.Gold)
    }
    // Daniel's ram with two high horns
    if (dr > 0f) {
        val f = easeOut(seg(dr, 0f, 0.3f)) * (1f - 0.5f * seg(od, 0f, 0.4f))
        val c = Offset(w * 0.72f, h * 0.62f)
        drawOval(HornC41.Ram, Offset(c.x - w * 0.09f, c.y - h * 0.05f), Size(w * 0.18f, h * 0.1f), alpha = f)
        for (k in 0 until 4) drawLine(HornC41.Ram, c + Offset(w * (-0.06f + 0.04f * k), h * 0.03f), c + Offset(w * (-0.06f + 0.04f * k), h * 0.1f), w * 0.012f, StrokeCap.Round, alpha = f)
        val head = c + Offset(-w * 0.1f, -h * 0.05f)
        drawCircle(HornC41.Ram, w * 0.03f, head, alpha = f)
        val grow = easeOut(seg(dr, 0.2f, 0.6f))
        for (k in 0 until 2) drawLine(HornC41.Crown, head + Offset(w * 0.01f * k, -h * 0.02f), head + Offset(-w * 0.01f + w * 0.03f * k, -h * (0.06f + 0.03f * k) * grow - h * 0.02f), w * 0.01f, StrokeCap.Round, alpha = f)
        tagAt(a.tag("danielram", 0, 0), c + Offset(-w * 0.08f, -h * 0.17f), seg(dr, 0.5f, 0.75f) * (1f - seg(od, 0f, 0.4f)), back = Back41, ink = ArtInk.Ivory)
        tagAt(a.tag("danielram", 0, 1), c + Offset(w * 0.1f, -h * 0.13f), seg(dr, 0.6f, 0.85f) * (1f - seg(od, 0f, 0.4f)), back = Back41, ink = ArtInk.Ivory)
    }
    // the gates opened before him
    if (od > 0f) {
        val f = easeOut(seg(od, 0f, 0.3f))
        val c = Offset(w * 0.5f, h * 0.62f)
        val open = easeInOut(seg(od, 0.25f, 0.75f))
        softGlow(c, w * 0.12f * open, ArtInk.Gold, 0.5f * open * f)
        drawRect(Color(0xFFFFF0C8), Offset(c.x - w * 0.09f, c.y - h * 0.16f), Size(w * 0.18f, h * 0.2f), alpha = f * open * 0.8f)
        for (s in listOf(-1f, 1f)) {
            val leaf = w * 0.09f * (1f - 0.85f * open)
            val x0 = c.x + s * w * 0.09f
            drawRect(HornC41.Gate, Offset(if (s < 0) x0 else x0 - leaf, c.y - h * 0.16f), Size(leaf, h * 0.2f), alpha = f)
        }
        drawRect(ArtInk.BrassDark, Offset(c.x - w * 0.1f, c.y - h * 0.17f), Size(w * 0.2f, h * 0.22f), alpha = f, style = Stroke(3f * dp))
    }
}

// ═════════════ 3. dots on paper, a voice from mouth to ear (bs-luxenberg) ═════════════

private object DotC41 {
    val Wall = Color(0xFF2A2638)
    val WallLow = Color(0xFF3E3650)
    val Paper = Color(0xFFF2EAD8)
    val Ink = Color(0xFF3A3028)
    val Stamp = Color(0xFFD04A3A)
    val Reciter = Color(0xFFE8DCC8)
    val Wave = Color(0xFFF4D68A)
    val Root = Color(0xFF8A6A3A)
}

/**
 * Beats: movedots (a page of bars; a stylus picks the dots up and sets them elsewhere), amateur (a red stamp on the page:
 * «هَاوٍ»), voicechain (a row of reciters, the sound passing from each to the next, unchanged; the page fades), rootqara
 * (a root and its word: «قَرَأَ», «الْقُرْآنُ»).
 */
internal fun DrawScope.dotsvoiceScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "movedots" to Shot(Offset(w * 0.5f, h * 0.3f), 1.3f),
        "amateur" to Shot(Offset(w * 0.6f, h * 0.32f), 1.35f),
        "voicechain" to Shot(Offset(w * 0.5f, h * 0.68f), 1.1f),
        "rootqara" to Shot(Offset(w * 0.5f, h * 0.35f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { dotsRoom(a) }
}

private fun DrawScope.dotsRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val md = a.b("movedots")
    val am = a.b("amateur")
    val vc = a.b("voicechain")
    val rq = a.b("rootqara")
    val floor = h * 0.86f
    drawRect(Brush.verticalGradient(listOf(DotC41.Wall, DotC41.WallLow), endY = floor), size = Size(w, floor))
    drawRect(Color(0xFF2A2228), Offset(0f, floor), Size(w, h - floor))
    // the page and its dots
    val page = easeOut(seg(md, 0f, 0.3f)) * (1f - 0.8f * seg(vc, 0.2f, 0.6f))
    if (page > 0f) {
        val c = Offset(w * 0.5f, h * 0.3f)
        drawRect(DotC41.Paper, Offset(c.x - w * 0.25f, c.y - h * 0.16f), Size(w * 0.5f, h * 0.32f), alpha = page)
        for (r in 0 until 5) bars41(Offset(c.x + w * 0.21f, c.y - h * 0.1f + r * h * 0.05f), w * 0.42f, h * 0.035f, DotC41.Ink, page, r * 9, dots = false)
        for (k in 0 until 6) {
            val home = Offset(c.x + w * (0.18f - 0.07f * k), c.y - h * 0.125f + (k % 5) * h * 0.05f)
            val moved = home + Offset(w * 0.03f * (if (k % 2 == 0) 1f else -1f), h * 0.02f)
            val mv = easeInOut(seg(md, 0.3f + 0.08f * k, 0.5f + 0.08f * k))
            drawCircle(DotC41.Ink, h * 0.006f, home + (moved - home) * mv, alpha = page)
        }
        val pen = Offset(c.x + w * (0.18f - 0.07f * (seg(md, 0.3f, 0.9f) * 5f)), c.y - h * 0.15f)
        drawLine(Color(0xFF6A4A2A), pen, pen + Offset(w * 0.06f, -h * 0.1f), 3f * dp, StrokeCap.Round, alpha = page * seg(md, 0.25f, 0.35f) * (1f - seg(md, 0.9f, 1f)))
    }
    // the stamp: an amateur
    if (am > 0f) {
        val f = easeOut(seg(am, 0.1f, 0.4f)) * (1f - 0.8f * seg(vc, 0.2f, 0.6f))
        val c = Offset(w * 0.62f, h * 0.34f)
        val s = 1f + 0.6f * (1f - easeOut(seg(am, 0.1f, 0.35f)))
        drawCircle(DotC41.Stamp, w * 0.07f * s, c, alpha = f, style = Stroke(3f * dp))
        drawCircle(DotC41.Stamp, w * 0.055f * s, c, alpha = f * 0.6f, style = Stroke(1.5f * dp))
        tagAt(a.tag("amateur"), c, f, back = Color(0xE6501818), ink = Color(0xFFFFE0D8))
    }
    // the voice: from mouth to ear, generation after generation
    if (vc > 0f) {
        val f = easeOut(seg(vc, 0f, 0.3f)) * (1f - 0.5f * seg(rq, 0f, 0.4f))
        for (k in 0 until 6) {
            val foot = Offset(w * (0.9f - 0.15f * k), floor)
            figure(foot, h * 0.16f, DotC41.Reciter, f * seg(vc, 0.05f * k, 0.2f + 0.05f * k))
            if (k < 5) {
                val mouth = foot + Offset(-w * 0.03f, -h * 0.13f)
                for (j in 0 until 3) {
                    val u = fract(t * 0.8f + j / 3f + k * 0.1f)
                    val r = w * (0.02f + 0.05f * u)
                    drawArc(DotC41.Wave, 120f, 120f, false, Offset(mouth.x - r, mouth.y - r), Size(r * 2f, r * 2f), alpha = f * (1f - u) * seg(vc, 0.2f + 0.08f * k, 0.4f + 0.08f * k), style = Stroke(2f * dp))
                }
            }
        }
    }
    // the root and its word
    if (rq > 0f) {
        val f = easeOut(seg(rq, 0f, 0.3f))
        val root = Offset(w * 0.5f, h * 0.48f)
        for (k in 0 until 5) {
            val an = PI.toFloat() * (0.2f + 0.15f * k)
            drawLine(DotC41.Root, root, root + Offset(cos(an) * w * 0.12f, sin(an) * h * 0.08f) * easeOut(seg(rq, 0.1f, 0.5f)), 3f * dp, StrokeCap.Round, alpha = f)
        }
        drawLine(DotC41.Root, root, root + Offset(0f, -h * 0.2f * easeOut(seg(rq, 0.2f, 0.6f))), 5f * dp, StrokeCap.Round, alpha = f)
        softGlow(root + Offset(0f, -h * 0.22f), w * 0.08f, ArtInk.Gold, 0.5f * f * seg(rq, 0.5f, 0.8f))
        tagAt(a.tag("rootqara", 0, 0), root + Offset(0f, h * 0.05f), seg(rq, 0.2f, 0.45f), back = Back41, ink = ArtInk.BrassLight)
        tagAt(a.tag("rootqara", 0, 1), root + Offset(0f, -h * 0.26f), seg(rq, 0.55f, 0.8f), back = Back41, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. a page wiped and corrected, and the Book in the hearts (bs-sanaa) ═════════════

private object SanaaC41 {
    val Sky = Color(0xFF141A2C)
    val SkyLow = Color(0xFF2E2E44)
    val Ground = Color(0xFF4A3E36)
    val Mosque = Color(0xFFB89A7A)
    val Dome = Color(0xFFD8C098)
    val Parch = Color(0xFFE8D8B0)
    val Faint = Color(0xFFB8A880)
    val Ink = Color(0xFF4A3A28)
    val Red = Color(0xFFC8384A)
    val Reciter = Color(0xFFE8DCC8)
    val Heart = Color(0xFFF2C45A)
}

/**
 * Beats: mosquefind (an old domed mosque; from a hollow in its ceiling, sheets of parchment drift down), erasedpage (a
 * child's sheet: lines written, wiped, written again, a teacher's red mark beside one), inhearts (a heart, larger than the
 * sheet, full of light and of sound), isnadchain (a reciter, and above him a chain of lights going back to his light ﷺ
 * and, above that, the angel's light).
 */
internal fun DrawScope.palimpsestScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "mosquefind" to Shot(Offset(w * 0.3f, h * 0.5f), 1.2f),
        "erasedpage" to Shot(Offset(w * 0.62f, h * 0.4f), 1.35f),
        "inhearts" to Shot(Offset(w * 0.5f, h * 0.45f), 1.1f),
        "isnadchain" to Shot(Offset(w * 0.78f, h * 0.45f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { sanaaNight(a) }
}

private fun DrawScope.sanaaNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val mf = a.b("mosquefind")
    val ep = a.b("erasedpage")
    val ih = a.b("inhearts")
    val ic = a.b("isnadchain")
    val ground = h * 0.84f
    nightSky(t, SanaaC41.Sky, SanaaC41.SkyLow, ground, starAlpha = 0.6f)
    drawRect(SanaaC41.Ground, Offset(0f, ground), Size(w, h - ground))
    // the old mosque and the sheets found in its ceiling
    val m = easeOut(seg(mf, 0f, 0.35f)) * (1f - 0.5f * seg(ih, 0f, 0.4f))
    if (m > 0f) {
        val c = Offset(w * 0.28f, ground)
        drawRect(SanaaC41.Mosque, Offset(c.x - w * 0.2f, c.y - h * 0.26f), Size(w * 0.4f, h * 0.26f), alpha = m)
        drawArc(SanaaC41.Dome, 180f, 180f, true, Offset(c.x - w * 0.09f, c.y - h * 0.36f), Size(w * 0.18f, h * 0.2f), alpha = m)
        drawRect(SanaaC41.Mosque, Offset(c.x + w * 0.16f, c.y - h * 0.44f), Size(w * 0.04f, h * 0.44f), alpha = m)
        for (k in 0 until 3) drawPath(archPath26(c.x - w * 0.12f + k * w * 0.12f, c.y - h * 0.16f, c.y, w * 0.03f), Color(0xFF3A2E28), alpha = m)
        for (k in 0 until 4) {
            val u = easeInOut(seg(mf, 0.3f + 0.1f * k, 0.8f + 0.05f * k))
            val p = Offset(c.x - w * 0.05f + w * 0.03f * k + w * 0.03f * sin(u * 6f + k), c.y - h * 0.24f + h * 0.2f * u)
            drawRect(SanaaC41.Parch, Offset(p.x - w * 0.02f, p.y - h * 0.015f), Size(w * 0.04f, h * 0.03f), alpha = m * seg(mf, 0.3f + 0.1f * k, 0.4f + 0.1f * k))
        }
    }
    // a child's sheet: written, wiped, corrected
    if (ep > 0f) {
        val f = easeOut(seg(ep, 0f, 0.3f)) * (1f - 0.6f * seg(ih, 0f, 0.4f))
        val c = Offset(w * 0.64f, h * 0.38f)
        drawRect(SanaaC41.Parch, Offset(c.x - w * 0.16f, c.y - h * 0.18f), Size(w * 0.32f, h * 0.36f), alpha = f)
        for (r in 0 until 5) {
            val y = c.y - h * 0.12f + r * h * 0.06f
            val tiltY = h * 0.006f * sin(r * 2.3f)
            val wipe = if (r == 2) seg(ep, 0.35f, 0.55f) else 0f
            val rewrite = if (r == 2) seg(ep, 0.55f, 0.8f) else 1f
            bars41(Offset(c.x + w * 0.13f, y + tiltY), w * 0.26f, h * 0.03f, SanaaC41.Faint, f * 0.5f, r * 5 + 3, dots = false)
            bars41(Offset(c.x + w * 0.13f, y + tiltY), w * 0.26f * (if (r == 2) rewrite else 1f), h * 0.03f, SanaaC41.Ink, f * (1f - wipe * (1f - rewrite)), r * 5, dots = true)
        }
        drawLine(SanaaC41.Red, Offset(c.x - w * 0.15f, c.y + h * 0.01f), Offset(c.x - w * 0.12f, c.y - h * 0.01f), 2.5f * dp, StrokeCap.Round, alpha = f * seg(ep, 0.75f, 0.95f))
    }
    // the Book in the hearts
    if (ih > 0f) {
        val f = easeOut(seg(ih, 0f, 0.4f)) * (1f - 0.5f * seg(ic, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.45f)
        softGlow(c, w * 0.2f * f, SanaaC41.Heart, 0.6f * f)
        drawPath(w8Heart(c, w * 0.09f * f), SanaaC41.Heart, alpha = f)
        for (j in 0 until 3) {
            val u = fract(t * 0.6f + j / 3f)
            drawCircle(SanaaC41.Heart, w * (0.1f + 0.15f * u), c, alpha = f * (1f - u) * 0.6f, style = Stroke(2f * dp))
        }
    }
    // the chain: reciter to reciter, up to him ﷺ, from the angel
    if (ic > 0f) {
        val f = easeOut(seg(ic, 0f, 0.3f))
        val foot = Offset(w * 0.78f, ground)
        figure(foot, h * 0.16f, SanaaC41.Reciter, f)
        var prev = foot + Offset(0f, -h * 0.2f)
        for (k in 0 until 4) {
            val p = Offset(w * 0.78f + w * 0.02f * sin(k * 1.7f), h * (0.56f - 0.07f * k))
            val g = seg(ic, 0.1f + 0.1f * k, 0.3f + 0.1f * k)
            drawLine(ArtInk.BrassLight, prev, p, 1.5f * dp, alpha = f * g)
            drawCircle(ArtInk.BrassLight, w * 0.01f, p, alpha = f * g)
            prev = p
        }
        val light = Offset(w * 0.78f, h * 0.24f)
        val gl = seg(ic, 0.55f, 0.8f)
        drawLine(ArtInk.BrassLight, prev, light, 1.5f * dp, alpha = f * gl)
        sirajMunir(light, w * 0.011f, f * gl, t, nameAt = light + Offset(-w * 0.14f, 0f))
        angelLight26(Offset(w * 0.78f, h * 0.1f), w * 0.016f, t, f * seg(ic, 0.75f, 1f))
    }
}

// ═════════════ 5. one spring in seven streams (bs-qiraat) ═════════════

private object StreamC41 {
    val Sky = Color(0xFF1A2236)
    val SkyLow = Color(0xFF34405A)
    val Ground = Color(0xFF3A4A3A)
    val Water = Color(0xFF8AC8E8)
    val Cup = Color(0xFFD8B04A)
    val Papyrus = Color(0xFFD8C48A)
    val Ink = Color(0xFF5A4028)
}

/**
 * Beats: sevenbranches (from his light a spring runs down and splits into seven streams: «سَبْعَةُ أَحْرُفٍ»),
 * twocompanions (two roundels come to his light, «عُمَرُ» and «هِشَامُ بْنُ حَكِيمٍ»), bothright (a tick beside each),
 * choosebranches (two streams, from «ابْنُ مَسْعُودٍ» and «أُبَيٌّ», run into one cup: a reading), dottedpapyrus (an old
 * papyrus with dotted bars: «22»).
 */
internal fun DrawScope.sevenstreamsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sevenbranches" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "twocompanions" to Shot(Offset(w * 0.5f, h * 0.25f), 1.3f),
        "bothright" to Shot(Offset(w * 0.5f, h * 0.25f), 1.3f),
        "choosebranches" to Shot(Offset(w * 0.35f, h * 0.72f), 1.25f),
        "dottedpapyrus" to Shot(Offset(w * 0.78f, h * 0.72f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { streamsNight(a) }
}

private fun DrawScope.streamsNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sb = a.b("sevenbranches")
    val tc = a.b("twocompanions")
    val br = a.b("bothright")
    val cb = a.b("choosebranches")
    val dpp = a.b("dottedpapyrus")
    val ground = h * 0.45f
    nightSky(t, StreamC41.Sky, StreamC41.SkyLow, ground, starAlpha = 0.6f)
    drawRect(StreamC41.Ground, Offset(0f, ground), Size(w, h - ground))
    val src = Offset(w * 0.5f, h * 0.2f)
    val sf = easeOut(seg(sb, 0f, 0.3f))
    sirajMunir(src, w * 0.012f, sf, t, nameAt = src + Offset(0f, -h * 0.1f))
    // seven streams
    if (sb > 0f) {
        val run = easeInOut(seg(sb, 0.15f, 0.7f))
        for (k in 0 until 7) {
            val endX = w * (0.12f + 0.127f * k)
            val p = Path().apply {
                moveTo(src.x, src.y + h * 0.05f)
                cubicTo(src.x, h * 0.42f, endX, h * 0.5f, endX, h * (0.55f + 0.4f * run))
            }
            drawPath(p, StreamC41.Water, alpha = sf * 0.75f * run, style = Stroke(w * 0.012f, cap = StrokeCap.Round))
        }
        tagAt(a.tag("sevenbranches"), Offset(w * 0.5f, h * 0.38f), seg(sb, 0.55f, 0.8f) * (1f - seg(tc, 0f, 0.3f)), back = Back41, ink = ArtInk.Gold)
    }
    // two companions, two ways of reading, both as revealed
    if (tc > 0f) {
        val f = easeOut(seg(tc, 0f, 0.4f)) * (1f - seg(cb, 0f, 0.4f))
        for (k in 0 until 2) {
            val come = easeInOut(seg(tc, 0.1f + 0.15f * k, 0.6f + 0.15f * k))
            val from = Offset(if (k == 0) w * 0.08f else w * 0.92f, h * 0.32f)
            val to = Offset(w * (0.36f + 0.28f * k), h * 0.26f)
            val p = from + (to - from) * come
            nameRoundel(p, w * 0.045f, a.tag("twocompanions", 0, k), Roundel.Fills[k], Roundel.Rim, f)
            check21(p + Offset(0f, h * 0.08f), w * 0.018f, ArtInk.Jade, seg(br, 0.1f + 0.3f * k, 0.4f + 0.3f * k) * (1f - seg(cb, 0f, 0.4f)), 3f * dp)
        }
    }
    // a reading: two streams into one cup
    if (cb > 0f) {
        val f = easeOut(seg(cb, 0f, 0.3f)) * (1f - 0.5f * seg(dpp, 0f, 0.4f))
        val cup = Offset(w * 0.35f, h * 0.85f)
        for (k in 0 until 2) {
            val r = Offset(w * (0.18f + 0.34f * k), h * 0.62f)
            nameRoundel(r, w * 0.04f, a.tag("choosebranches", 0, k), Roundel.Fills[2 + k], Roundel.Rim, f)
            val flow = easeInOut(seg(cb, 0.3f + 0.15f * k, 0.7f + 0.15f * k))
            drawLine(StreamC41.Water, r + Offset(0f, h * 0.05f), r + Offset(0f, h * 0.05f) + (cup + Offset(0f, -h * 0.05f) - r - Offset(0f, h * 0.05f)) * flow, w * 0.01f, StrokeCap.Round, alpha = f * 0.8f)
        }
        drawPath(Path().apply { moveTo(cup.x - w * 0.06f, cup.y - h * 0.05f); lineTo(cup.x + w * 0.06f, cup.y - h * 0.05f); lineTo(cup.x + w * 0.035f, cup.y + h * 0.03f); lineTo(cup.x - w * 0.035f, cup.y + h * 0.03f); close() }, StreamC41.Cup, alpha = f)
        softGlow(cup, w * 0.06f, ArtInk.Gold, 0.4f * f * seg(cb, 0.7f, 1f))
    }
    // dots already known in the year 22
    if (dpp > 0f) {
        val f = easeOut(seg(dpp, 0f, 0.35f))
        val c = Offset(w * 0.78f, h * 0.74f)
        drawRect(StreamC41.Papyrus, Offset(c.x - w * 0.13f, c.y - h * 0.1f), Size(w * 0.26f, h * 0.2f), alpha = f)
        for (j in 0 until 6) drawLine(Color(0xFFB8A070), Offset(c.x - w * 0.13f + j * w * 0.05f, c.y - h * 0.1f), Offset(c.x - w * 0.13f + j * w * 0.05f, c.y + h * 0.1f), 0.8f * dp, alpha = f * 0.5f)
        for (r in 0 until 3) bars41(Offset(c.x + w * 0.11f, c.y - h * 0.05f + r * h * 0.05f), w * 0.22f, h * 0.03f, StreamC41.Ink, f, r * 7 + 1, dots = true)
        tagAt(a.tag("dottedpapyrus"), c + Offset(0f, -h * 0.14f), seg(dpp, 0.4f, 0.7f), back = Back41, ink = ArtInk.Gold)
    }
}

// ═════════════ 6. a staircase of gradual law (bs-naskh) ═════════════

private object StepC41 {
    val Sky = Color(0xFFE8C8A0)
    val SkyHigh = Color(0xFF7A9AC0)
    val Ground = Color(0xFFB89A70)
    val Ram = Color(0xFFF2EEE4)
    val Step = Color(0xFFD8C098)
    val Cup = Color(0xFF8A3A4A)
    val Red = Color(0xFFE0584A)
    val Pie = Color(0xFF8A7A6A)
}

/**
 * Beats: ransomram (Ibrahim's light with his name, and a white ram appears beside it, the ransom), graduated (a
 * staircase; a cup on the lowest step, and on each step up its line is narrowed until it is struck out at the top),
 * stilltrue (an open book; every one of its lines still glows), willthird (a disc with one third lit: «الثُّلُثُ»),
 * fewnine (nine small marks, all there are: «تِسْعُ آيَاتٍ»).
 */
internal fun DrawScope.stairstepsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "ransomram" to Shot(Offset(w * 0.22f, h * 0.5f), 1.3f),
        "graduated" to Shot(Offset(w * 0.55f, h * 0.55f), 1.1f),
        "stilltrue" to Shot(Offset(w * 0.5f, h * 0.25f), 1.3f),
        "willthird" to Shot(Offset(w * 0.8f, h * 0.3f), 1.35f),
        "fewnine" to Shot(Offset(w * 0.5f, h * 0.85f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { stairsDay(a) }
}

private fun DrawScope.stairsDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rr = a.b("ransomram")
    val gr = a.b("graduated")
    val st = a.b("stilltrue")
    val wt = a.b("willthird")
    val fn = a.b("fewnine")
    val ground = h * 0.78f
    drawRect(Brush.verticalGradient(listOf(StepC41.SkyHigh, StepC41.Sky), endY = ground), size = Size(w, ground))
    drawRect(StepC41.Ground, Offset(0f, ground), Size(w, h - ground))
    // the ransom
    if (rr > 0f) {
        val f = easeOut(seg(rr, 0f, 0.3f)) * (1f - seg(gr, 0f, 0.4f))
        val light = Offset(w * 0.16f, h * 0.42f)
        nabiLight14(light, w * 0.01f, f, t, a.tag("ransomram"), light + Offset(0f, -h * 0.1f))
        val ram = Offset(w * 0.3f, ground - h * 0.06f)
        val g = easeOut(seg(rr, 0.4f, 0.8f)) * f
        softGlow(ram, w * 0.08f, Color.White, 0.4f * g)
        sheep26(ram, w * 0.05f, StepC41.Ram, g)
    }
    // gradual: step by step to the full ruling
    if (gr > 0f) {
        val f = easeOut(seg(gr, 0f, 0.3f)) * (1f - 0.5f * seg(st, 0f, 0.4f))
        for (k in 0 until 4) {
            val x = w * (0.38f + 0.12f * k)
            val top = ground - h * 0.07f * (k + 1)
            drawRect(StepC41.Step, Offset(x, top), Size(w * 0.12f, ground - top), alpha = f * seg(gr, 0.05f * k, 0.2f + 0.05f * k))
        }
        val climb = (seg(gr, 0.2f, 0.9f) * 3.99f).toInt()
        val c = Offset(w * (0.44f + 0.12f * climb), ground - h * 0.07f * (climb + 1) - h * 0.04f)
        drawPath(Path().apply { moveTo(c.x - w * 0.02f, c.y - h * 0.03f); lineTo(c.x + w * 0.02f, c.y - h * 0.03f); lineTo(c.x + w * 0.012f, c.y + h * 0.02f); lineTo(c.x - w * 0.012f, c.y + h * 0.02f); close() }, StepC41.Cup, alpha = f)
        if (climb == 3) cross21(c, w * 0.035f, StepC41.Red, seg(gr, 0.85f, 1f) * f, 3f * dp)
    }
    // still in force: every line of the Book lit
    if (st > 0f) {
        val f = easeOut(seg(st, 0f, 0.35f)) * (1f - 0.5f * seg(wt, 0f, 0.4f))
        openBook21(Offset(w * 0.5f, h * 0.24f), w * 0.2f, Color(0xFFF6EED8), Color(0xFF8A6A3A), f, glow = f * seg(st, 0.3f, 0.8f))
    }
    // a third
    if (wt > 0f) {
        val f = easeOut(seg(wt, 0f, 0.35f)) * (1f - 0.5f * seg(fn, 0f, 0.4f))
        val c = Offset(w * 0.8f, h * 0.3f)
        drawCircle(StepC41.Pie, w * 0.08f, c, alpha = f)
        drawArc(ArtInk.Gold, -90f, 120f * easeOut(seg(wt, 0.3f, 0.7f)), true, Offset(c.x - w * 0.08f, c.y - w * 0.08f), Size(w * 0.16f, w * 0.16f), alpha = f)
        tagAt(a.tag("willthird"), c + Offset(0f, h * 0.12f), seg(wt, 0.5f, 0.8f) * (1f - seg(fn, 0f, 0.4f)), back = Back41, ink = ArtInk.Gold)
    }
    // few: nine
    if (fn > 0f) {
        val f = easeOut(seg(fn, 0f, 0.3f))
        for (k in 0 until 9) {
            val g = seg(fn, 0.1f + 0.05f * k, 0.25f + 0.05f * k)
            drawRoundRect(ArtInk.BrassLight, Offset(w * (0.27f + 0.05f * k), h * 0.85f), Size(w * 0.03f, h * 0.05f), CornerRadius(3f * dp), alpha = f * g)
        }
        tagAt(a.tag("fewnine"), Offset(w * 0.5f, h * 0.95f), seg(fn, 0.6f, 0.85f), back = Back41, ink = ArtInk.Gold)
    }
}
