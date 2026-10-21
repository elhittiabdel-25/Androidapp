package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 17, round 10 of the redraw (2026-10-21): «ما يضاد التوحيد». A man swept down a river grabs at an idol, a
 * banner and a crown on the bank, which break, and then at the firm handhold set in stone; and the circle of the
 * religion, with the one who leaves it, the one who stays in it stained, and the four kinds of major kufr.
 */

// ═════════════ 1. river: the false holds and the firmest handhold (taghut) ═════════════

private object RiverC17 {
    val SkyTop = Color(0xFF8CC0E8)
    val SkyLow = Color(0xFFE8F0E8)
    val Bank = Color(0xFF8AA866)
    val BankEdge = Color(0xFF6A5A3A)
    val Water = Color(0xFF3A78B0)
    val WaterDeep = Color(0xFF24507E)
    val Foam = Color(0xFFF2F8FF)
    val Ink = Color(0xFF2A2430)
    val Idol = Color(0xFF9A8A72)
    val Banner = Color(0xFF8A2A2A)
    val Pole = Color(0xFF6A4A2A)
    val Crown = Color(0xFFD8A83A)
    val Stone = Color(0xFF8A8478)
    val StoneDark = Color(0xFF5E5850)
    val Ring = Color(0xFFF2C45A)
    val Back = Color(0xE6141826)
}

/**
 * A fast river by day, flowing to the right under its far bank. Beats: torrent (a man is swept in from the left,
 * his arms thrashing), holdidol (he grabs at an idol on the bank: it cracks and topples into the water), holdbanner
 * (at a banner's pole: it snaps), holdcrown (at a crown on a post: it breaks off), letgo (he opens his hands and the
 * broken things are carried off downstream), firmhold (he takes hold of a great gold ring set in a stone pier, and
 * holds: the water parts round him), twowords («لَا إِلَهَ» over what the water took, «إِلَّا اللَّهُ» over him).
 */
internal fun DrawScope.riverScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "torrent" to Shot(Offset(w * 0.3f, h * 0.52f), 1.25f),
        "holdidol" to Shot(Offset(w * 0.32f, h * 0.48f), 1.4f),
        "holdbanner" to Shot(Offset(w * 0.44f, h * 0.48f), 1.4f),
        "holdcrown" to Shot(Offset(w * 0.56f, h * 0.48f), 1.4f),
        "letgo" to Shot(Offset(w * 0.65f, h * 0.52f), 1.15f),
        "firmhold" to Shot(Offset(w * 0.7f, h * 0.5f), 1.4f),
        "twowords" to Shot(Offset(w * 0.62f, h * 0.48f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { riverDay(a) }
}

private fun DrawScope.riverDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tr = a.b("torrent")
    val hi = a.b("holdidol")
    val hb = a.b("holdbanner")
    val hc = a.b("holdcrown")
    val lg = a.b("letgo")
    val fh = a.b("firmhold")
    val tw = a.b("twowords")
    val edge = h * 0.44f
    drawRect(Brush.verticalGradient(listOf(RiverC17.SkyTop, RiverC17.SkyLow), endY = h * 0.3f), size = Size(w, h * 0.3f))
    hills(Color(0xFFA8C08A), h * 0.3f, h * 0.03f, 2.2f, 4.6f, 0.3f)
    drawRect(RiverC17.Bank, topLeft = Offset(0f, h * 0.3f), size = Size(w, edge - h * 0.3f))
    drawRect(RiverC17.BankEdge, topLeft = Offset(0f, edge - h * 0.012f), size = Size(w, h * 0.02f))

    // the three false holds on the bank, and the stone pier with its ring
    val idolX = w * 0.3f
    val bannerX = w * 0.42f
    val crownX = w * 0.54f
    val pierX = w * 0.74f
    fun breakOf(b: Float) = easeOut(seg(b, 0.35f, 0.75f))
    // where each broken thing is: on the bank, falling, or carried off
    fun carried(home: Offset, br: Float, k: Int): Offset {
        if (br <= 0f) return home
        val fallIn = home + Offset(w * 0.02f, h * 0.14f)
        val down = easeInOut(seg(lg, 0.05f, 0.9f))
        val drift = Offset(mix(0f, w * (0.92f - 0.05f * k) - fallIn.x, down), h * 0.03f * sin(t * 2f + k))
        return home + (fallIn - home) * br + drift
    }
    val ib = breakOf(hi)
    val bb = breakOf(hb)
    val cb = breakOf(hc)
    val idolP = carried(Offset(idolX, edge), ib, 0)
    val bannerP = carried(Offset(bannerX, edge), bb, 1)
    val crownP = carried(Offset(crownX, edge), cb, 2)
    // the idol
    withTransform({ rotate(100f * ib, idolP) }) {
        drawRect(RiverC17.Idol, topLeft = idolP + Offset(-w * 0.02f, -h * 0.035f), size = Size(w * 0.04f, h * 0.035f))
        drawRect(RiverC17.Idol, topLeft = idolP + Offset(-w * 0.014f, -h * 0.12f), size = Size(w * 0.028f, h * 0.085f))
        drawCircle(RiverC17.Idol, w * 0.016f, idolP + Offset(0f, -h * 0.14f))
        if (ib > 0f) drawLine(RiverC17.StoneDark, idolP + Offset(-w * 0.01f, -h * 0.11f), idolP + Offset(w * 0.008f, -h * 0.05f), 1.2f * dp)
    }
    // the banner on its pole
    withTransform({ rotate(-80f * bb, bannerP) }) {
        drawLine(RiverC17.Pole, bannerP, bannerP + Offset(0f, -h * 0.22f), 2.2f * dp, StrokeCap.Round)
        val flap = sin(t * 4f) * h * 0.008f
        drawPath(Path().apply {
            moveTo(bannerP.x, bannerP.y - h * 0.22f)
            lineTo(bannerP.x + w * 0.07f, bannerP.y - h * 0.2f + flap)
            lineTo(bannerP.x, bannerP.y - h * 0.15f)
            close()
        }, RiverC17.Banner)
    }
    // the crown on its post
    withTransform({ rotate(70f * cb, crownP) }) {
        drawLine(RiverC17.Pole, crownP, crownP + Offset(0f, -h * 0.1f), 2.6f * dp)
        val c = crownP + Offset(0f, -h * 0.12f)
        drawPath(Path().apply {
            moveTo(c.x - w * 0.025f, c.y + h * 0.02f)
            lineTo(c.x - w * 0.025f, c.y - h * 0.02f)
            lineTo(c.x - w * 0.012f, c.y)
            lineTo(c.x, c.y - h * 0.035f)
            lineTo(c.x + w * 0.012f, c.y)
            lineTo(c.x + w * 0.025f, c.y - h * 0.02f)
            lineTo(c.x + w * 0.025f, c.y + h * 0.02f)
            close()
        }, RiverC17.Crown)
    }

    // the river
    drawRect(Brush.verticalGradient(listOf(RiverC17.Water, RiverC17.WaterDeep), startY = edge, endY = h), topLeft = Offset(0f, edge), size = Size(w, h - edge))

    // the man, swept along; he reaches for each hold in turn
    val manX = when {
        fh > 0f -> mix(w * 0.64f, pierX - w * 0.05f, easeOut(seg(fh, 0f, 0.4f)))
        lg > 0f -> mix(crownX + w * 0.02f, w * 0.64f, easeInOut(lg))
        hc > 0f -> mix(bannerX + w * 0.02f, crownX + w * 0.02f, easeOut(seg(hc, 0f, 0.35f)))
        hb > 0f -> mix(idolX + w * 0.02f, bannerX + w * 0.02f, easeOut(seg(hb, 0f, 0.35f)))
        hi > 0f -> mix(w * 0.22f, idolX + w * 0.02f, easeOut(seg(hi, 0f, 0.35f)))
        else -> mix(-w * 0.08f, w * 0.22f, easeOut(seg(tr, 0f, 0.6f)))
    }
    val tall = h * 0.34f
    val bob = h * 0.012f * sin(t * 3f)
    val neck = Offset(manX, h * 0.5f + bob)
    val hip = neck + Offset(-tall * 0.05f, tall * 0.31f)
    val target: Offset? = when {
        fh > 0f -> Offset(pierX - w * 0.022f, h * 0.5f)
        lg > 0f -> null
        hc > 0f -> Offset(crownX, edge - h * 0.03f)
        hb > 0f -> Offset(bannerX, edge - h * 0.04f)
        hi > 0f -> Offset(idolX, edge - h * 0.03f)
        else -> null
    }
    val thrash = if (target == null && fh <= 0f) 1f else 0f
    val handA: Offset
    val handB: Offset
    if (target != null) {
        val reach = when {
            fh > 0f -> easeOut(seg(fh, 0.1f, 0.4f))
            hc > 0f -> easeOut(seg(hc, 0.05f, 0.35f)) * (1f - easeOut(seg(hc, 0.6f, 0.9f)))
            hb > 0f -> easeOut(seg(hb, 0.05f, 0.35f)) * (1f - easeOut(seg(hb, 0.6f, 0.9f)))
            else -> easeOut(seg(hi, 0.05f, 0.35f)) * (1f - easeOut(seg(hi, 0.6f, 0.9f)))
        }
        val rest = neck + Offset(tall * 0.15f, tall * 0.12f)
        handA = rest + (target - rest) * reach
        handB = neck + Offset(-tall * 0.2f, -tall * 0.05f + tall * 0.08f * sin(t * 5f) * (1f - reach))
    } else {
        val open = if (lg > 0f) 1f else 0f
        handA = neck + Offset(tall * 0.2f, -tall * (0.12f + 0.12f * sin(t * 6f)) * thrash - tall * 0.15f * open)
        handB = neck + Offset(-tall * 0.2f, -tall * (0.12f + 0.12f * sin(t * 6f + 2f)) * thrash - tall * 0.15f * open)
    }
    if (tr > 0f) posed14(hip, neck, handA, handB, hip + Offset(-tall * 0.08f, tall * 0.4f), hip + Offset(tall * 0.08f, tall * 0.4f), tall, RiverC17.Ink, seg(tr, 0f, 0.1f))
    // the water over his body, foam round him, the current
    drawRect(RiverC17.Water, topLeft = Offset(0f, h * 0.55f), size = Size(w, h * 0.45f), alpha = 0.82f)
    for (k in 0 until 16) {
        val f = fract(t * (0.35f + 0.05f * (k % 3)) + k * 0.137f)
        val y = h * (0.5f + 0.48f * w8Rnd(k, 3.7f))
        val x = mix(-w * 0.1f, w * 1.1f, f)
        val held = fh > 0f && kotlin.math.abs(x - manX) < w * 0.05f && kotlin.math.abs(y - h * 0.56f) < h * 0.06f
        drawLine(RiverC17.Foam, Offset(x, y), Offset(x + w * 0.06f, y), 1.4f * dp, StrokeCap.Round, alpha = if (held) 0.2f else 0.55f)
    }
    if (tr > 0f) {
        val splash = if (fh > 0f) 0.5f else 1f
        for (k in 0 until 5) {
            val f = fract(t * 2.4f + k / 5f)
            drawCircle(RiverC17.Foam, (1.5f + 1.5f * f) * dp, Offset(manX + (k - 2) * tall * 0.07f, h * 0.55f - h * 0.04f * bump(f)), alpha = splash * (1f - f) * seg(tr, 0f, 0.1f))
        }
        drawOval(RiverC17.Foam, topLeft = Offset(manX - tall * 0.18f, h * 0.545f), size = Size(tall * 0.36f, h * 0.018f), alpha = 0.7f * seg(tr, 0f, 0.1f))
    }

    // the stone pier and its ring
    if (fh > 0f || tw > 0f || lg > 0f) {
        val show = max(easeOut(seg(lg, 0.5f, 1f)), if (fh > 0f) 1f else 0f)
        drawRect(RiverC17.StoneDark, topLeft = Offset(pierX - w * 0.035f, h * 0.38f), size = Size(w * 0.07f, h * 0.34f), alpha = show)
        drawRect(RiverC17.Stone, topLeft = Offset(pierX - w * 0.03f, h * 0.38f), size = Size(w * 0.06f, h * 0.3f), alpha = show)
        for (k in 0 until 3) drawLine(RiverC17.StoneDark, Offset(pierX - w * 0.03f, h * (0.45f + 0.08f * k)), Offset(pierX + w * 0.03f, h * (0.45f + 0.08f * k)), 1f * dp, alpha = show)
        val rc = Offset(pierX - w * 0.034f, h * 0.5f)
        if (fh > 0f) softGlow(rc, h * 0.08f, RiverC17.Ring, 0.6f * easeOut(seg(fh, 0.3f, 0.6f)))
        drawCircle(RiverC17.Ring, h * 0.032f, rc, alpha = show, style = Stroke(3f * dp))
        if (fh > 0.4f) glint(rc + Offset(-h * 0.02f, -h * 0.025f), h * 0.05f, Color.White, bump(seg(fh, 0.4f, 0.9f)))
        tagAt(a.tag("firmhold"), Offset(pierX, h * 0.3f), seg(fh, 0.4f, 0.8f) * (1f - seg(tw, 0f, 0.3f)), back = RiverC17.Back, ink = ArtInk.Gold)
    }
    // the broken things in the water as they are carried off
    for ((k, p) in listOf(idolP, bannerP, crownP).withIndex()) {
        val br = listOf(ib, bb, cb)[k]
        if (br > 0.6f) drawOval(RiverC17.Foam, topLeft = Offset(p.x - w * 0.03f, p.y - h * 0.005f), size = Size(w * 0.06f, h * 0.014f), alpha = 0.5f)
    }
    // the two words
    if (tw > 0f) {
        tagAt(a.tag("twowords", 0, 0), Offset(w * 0.88f, h * 0.44f), seg(tw, 0.05f, 0.4f), back = RiverC17.Back, ink = Color(0xFFF0A090))
        tagAt(a.tag("twowords", 0, 1), Offset(manX, h * 0.28f), seg(tw, 0.35f, 0.7f), back = RiverC17.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. milla: the circle of the religion and the four kinds of major kufr (kufr-anwa) ═════════════

private object MillaC17 {
    val SkyTop = Color(0xFF9EC4E2)
    val SkyLow = Color(0xFFF2E8D2)
    val Ground = Color(0xFFD6C29A)
    val GroundLow = Color(0xFFC2AA7E)
    val Ring = Color(0xFFF2C45A)
    val In = Color(0xFF2A2430)
    val Out = Color(0xFF7A7280)
    val Page = Color(0xFFFBF2DC)
    val Stand = Color(0xFF6A4A2A)
    val Stain = Color(0xFF3A2E34)
    val Bright = Color(0xFFF6F2E8)
    val Red = Color(0xFFD23C30)
}

/**
 * Open ground by day; a ring of light on it, the religion, and a book shining on a stand inside it. Beats: outmilla
 * (one walks out across the ring: it flashes red where he crosses and he greys outside), inmilla (one inside takes
 * a dark stain on his chest; he stays within the ring), belie (outside, one faces the book with his hands over his
 * ears and shakes his head), conceal (one whose heart lights up as he sees it, who turns his back and hides it under
 * his cloak), haughty (one on a step, chin up, arms folded, leaning away), twoface (one standing astride the ring:
 * bright on the outside, his heart dark).
 */
internal fun DrawScope.millaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "outmilla" to Shot(Offset(w * 0.62f, h * 0.6f), 1.2f),
        "inmilla" to Shot(Offset(w * 0.45f, h * 0.6f), 1.35f),
        "belie" to Shot(Offset(w * 0.2f, h * 0.6f), 1.4f),
        "conceal" to Shot(Offset(w * 0.3f, h * 0.6f), 1.4f),
        "haughty" to Shot(Offset(w * 0.78f, h * 0.58f), 1.4f),
        "twoface" to Shot(Offset(w * 0.62f, h * 0.6f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.55f), 1f), dim = 0.12f) { millaGround(a) }
}

private fun DrawScope.millaGround(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val om = a.b("outmilla")
    val im = a.b("inmilla")
    val be = a.b("belie")
    val co = a.b("conceal")
    val ha = a.b("haughty")
    val tf = a.b("twoface")
    val horizon = h * 0.5f
    drawRect(Brush.verticalGradient(listOf(MillaC17.SkyTop, MillaC17.SkyLow), endY = horizon), size = Size(w, horizon))
    hills(Color(0xFFB8C49A), horizon, h * 0.03f, 2.1f, 3.9f, 0.9f)
    drawRect(Brush.verticalGradient(listOf(MillaC17.Ground, MillaC17.GroundLow), startY = horizon, endY = h), topLeft = Offset(0f, horizon), size = Size(w, h - horizon))

    // the ring of the religion
    val c = Offset(w * 0.5f, h * 0.76f)
    val rx = w * 0.2f
    val ry = h * 0.1f
    drawOval(MillaC17.Ring.copy(alpha = 0.16f), topLeft = c - Offset(rx, ry), size = Size(2 * rx, 2 * ry))
    drawOval(MillaC17.Ring, topLeft = c - Offset(rx, ry), size = Size(2 * rx, 2 * ry), style = Stroke(2.4f * dp))
    // the book on its stand at its middle
    val bk = c + Offset(w * 0.06f, -h * 0.02f)
    drawLine(MillaC17.Stand, bk, bk + Offset(-w * 0.015f, -h * 0.1f), 2.4f * dp, StrokeCap.Round)
    drawLine(MillaC17.Stand, bk, bk + Offset(w * 0.015f, -h * 0.1f), 2.4f * dp, StrokeCap.Round)
    val page = bk + Offset(0f, -h * 0.12f)
    softGlow(page, w * 0.08f, Color(0xFFFFF0C0), 0.8f)
    for (sd in listOf(-1f, 1f)) {
        drawPath(Path().apply {
            moveTo(page.x, page.y + h * 0.02f)
            lineTo(page.x + sd * w * 0.04f, page.y + h * 0.005f)
            lineTo(page.x + sd * w * 0.04f, page.y - h * 0.04f)
            lineTo(page.x, page.y - h * 0.025f)
            close()
        }, MillaC17.Page)
        for (k in 0 until 3) drawLine(Color(0xFF9A8060), Offset(page.x + sd * w * 0.008f, page.y - h * (0.015f - 0.012f * k)), Offset(page.x + sd * w * 0.032f, page.y - h * (0.025f - 0.012f * k)), 0.8f * dp)
    }

    val tall = h * 0.3f
    // the one who walks out
    if (om > 0f) {
        val go = easeInOut(seg(om, 0.05f, 0.75f))
        val x = mix(w * 0.56f, w * 0.88f, go)
        val foot = Offset(x, c.y + h * 0.02f)
        val outside = x > c.x + rx * 0.95f
        val col = lerpColor(MillaC17.In, MillaC17.Out, seg(go, 0.55f, 0.8f))
        val gone = 1f - easeOut(seg(be, 0f, 0.3f))
        withTransform({ scale(-1f, 1f, pivot = foot) }) { figure(foot, tall, col, gone, walk = if (go in 0.01f..0.99f) t * 6f else 0f) }
        val flash = bump(seg(go, 0.5f, 0.75f))
        if (flash > 0f) {
            val cross = Offset(c.x + rx, c.y)
            softGlow(cross, w * 0.06f, MillaC17.Red, 0.9f * flash)
            drawArc(MillaC17.Red, -25f, 50f, false, topLeft = c - Offset(rx, ry), size = Size(2 * rx, 2 * ry), style = Stroke(3.4f * dp), alpha = flash)
        }
        if (outside) drawCircle(MillaC17.Out, tall * 0.14f, foot + Offset(0f, -tall * 0.62f), alpha = 0.25f * gone)
    }
    // the one who stays inside, stained
    if (im > 0f) {
        val foot = Offset(w * 0.42f, c.y + h * 0.02f)
        figure(foot, tall, MillaC17.In, seg(im, 0f, 0.15f))
        val chest = foot + Offset(0f, -tall * 0.6f)
        val stain = easeOut(seg(im, 0.2f, 0.5f))
        drawCircle(MillaC17.Stain, tall * 0.07f * stain, chest)
        drawCircle(MillaC17.Stain, tall * 0.035f * stain, chest + Offset(tall * 0.05f, tall * 0.04f))
        // still within the ring: it glows round his feet
        drawOval(MillaC17.Ring, topLeft = foot - Offset(tall * 0.2f, tall * 0.04f), size = Size(tall * 0.4f, tall * 0.08f), alpha = 0.6f * easeOut(seg(im, 0.5f, 0.8f)), style = Stroke(1.6f * dp))
    }
    // belie: hands over his ears, shaking his head
    if (be > 0f) {
        val foot = Offset(w * 0.12f, c.y + h * 0.04f)
        val shake = tall * 0.03f * sin(t * 10f) * seg(be, 0.2f, 0.4f)
        val hip = foot + Offset(0f, -tall * 0.45f)
        val neck = hip + Offset(0f, -tall * 0.31f)
        val head = neck + Offset(shake, -tall * 0.15f)
        val al = easeOut(seg(be, 0f, 0.2f))
        posed14(hip, neck, head + Offset(-tall * 0.1f, 0f), head + Offset(tall * 0.1f, 0f), foot + Offset(-tall * 0.07f, 0f), foot + Offset(tall * 0.07f, 0f), tall, MillaC17.Out, al)
        drawCircle(MillaC17.Out, tall * 0.1f, head, alpha = al)
        for (sd in listOf(-1f, 1f)) drawArc(MillaC17.Out, if (sd < 0) 150f else -30f, 60f, false, topLeft = head - Offset(tall * 0.22f, tall * 0.22f), size = Size(tall * 0.44f, tall * 0.44f), style = Stroke(1.4f * dp, cap = StrokeCap.Round), alpha = al * seg(be, 0.2f, 0.4f))
    }
    // conceal: his heart lights as he sees it; he turns away and hides it under his cloak
    if (co > 0f) {
        val foot = Offset(w * 0.26f, c.y + h * 0.05f)
        val turn = easeOut(seg(co, 0.4f, 0.7f))
        val al = easeOut(seg(co, 0f, 0.2f))
        val heart = foot + Offset(0f, -tall * 0.62f)
        val lit = easeOut(seg(co, 0.1f, 0.35f))
        if (turn < 0.5f) {
            withTransform({ scale(-1f, 1f, pivot = foot) }) { figure(foot, tall, MillaC17.Out, al) }
            softGlow(heart, tall * 0.18f, Color(0xFFFFD27A), 0.8f * lit)
            drawPath(w8Heart(heart, tall * 0.05f), Color(0xFFFFD27A).copy(alpha = lit))
        } else {
            figure(foot, tall, MillaC17.Out, al)
            // a little of the light still shows round the edge of his cloak
            softGlow(heart + Offset(tall * 0.05f, 0f), tall * 0.1f, Color(0xFFFFD27A), 0.5f)
            drawPath(Path().apply {
                moveTo(heart.x - tall * 0.12f, heart.y - tall * 0.14f)
                lineTo(heart.x + tall * 0.12f, heart.y - tall * 0.14f)
                lineTo(heart.x + tall * 0.14f, heart.y + tall * 0.3f)
                lineTo(heart.x - tall * 0.14f, heart.y + tall * 0.3f)
                close()
            }, Color(0xFF5A4A5A).copy(alpha = turn))
        }
    }
    // haughty: up on a step, chin up, arms folded, leaning away
    if (ha > 0f) {
        val al = easeOut(seg(ha, 0f, 0.2f))
        val step = Offset(w * 0.8f, c.y + h * 0.03f)
        drawRect(Color(0xFFA8926A), topLeft = step + Offset(-w * 0.05f, -h * 0.05f), size = Size(w * 0.1f, h * 0.05f), alpha = al)
        val foot = step + Offset(0f, -h * 0.05f)
        val lean = 0.18f * easeOut(seg(ha, 0.2f, 0.5f))
        val hip = foot + Offset(0f, -tall * 0.45f)
        val neck = hip + Offset(tall * 0.31f * lean, -tall * 0.31f)
        val arms = neck + Offset(tall * 0.02f, tall * 0.12f)
        posed14(hip, neck, arms + Offset(-tall * 0.1f, 0f), arms + Offset(tall * 0.1f, -tall * 0.01f), foot + Offset(-tall * 0.08f, 0f), foot + Offset(tall * 0.08f, 0f), tall * 1.05f, MillaC17.Out, al)
        // chin up: a short tilt line over his head
        drawLine(MillaC17.Out, neck + Offset(tall * 0.04f, -tall * 0.26f), neck + Offset(tall * 0.14f, -tall * 0.3f), 1.6f * dp, StrokeCap.Round, alpha = al * seg(ha, 0.3f, 0.6f))
    }
    // twoface: astride the ring, bright outside, dark within
    if (tf > 0f) {
        val al = easeOut(seg(tf, 0f, 0.25f))
        val foot = Offset(c.x + rx * 0.92f, c.y - ry * 0.4f)
        softGlow(foot + Offset(0f, -tall * 0.5f), tall * 0.55f, Color(0xFF3A3040), 0.45f * al)
        figure(foot, tall, MillaC17.Bright, al)
        drawCircle(MillaC17.In, tall * 0.1f, foot + Offset(0f, -tall * 0.91f), alpha = al, style = Stroke(1f * dp))
        val heart = foot + Offset(0f, -tall * 0.62f)
        drawPath(w8Heart(heart, tall * 0.06f), MillaC17.Stain.copy(alpha = al * easeOut(seg(tf, 0.3f, 0.6f))))
        for (k in 0 until 3) {
            val f = fract(t * 0.7f + k / 3f)
            drawCircle(MillaC17.Stain, tall * 0.03f, heart + Offset(tall * 0.04f * (k - 1), -tall * 0.1f * f), alpha = al * 0.5f * (1f - f))
        }
    }
}
