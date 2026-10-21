package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 9, round 2 of the redraw (2026-10-21): «براهين التوحيد». A stone-carver's workshop by day (what is
 * worshipped besides Allah is made, owns nothing, hears nothing), the seven heavens and their angels as white
 * lights (their awe at the revelation), a room at night (Abu Talib: the two kinds of guidance) and a city of
 * tyrants under a sky that is rolled up (the greatness of Allah, never drawn).
 */

// ═════════════ 1. atelier: the idol is made, owns nothing, hears nothing (ajz-alihah) ═════════════

private object AtelierC9 {
    val Wall = Color(0xFFE9DCC2)
    val WallLow = Color(0xFFD5C29E)
    val Floor = Color(0xFFB59D78)
    val FloorLine = Color(0xFF8E7656)
    val Stone = Color(0xFFA7A196)
    val StoneLight = Color(0xFFC9C3B6)
    val StoneDark = Color(0xFF77716A)
    val Wood = Color(0xFF7A5232)
    val WoodDark = Color(0xFF4E3320)
    val Iron = Color(0xFF4A4A52)
    val Ink = Color(0xFF2A2420)
    val Sky = Color(0xFFBFE0F2)
    val SkyLow = Color(0xFFF3E6C4)
    val Court = Color(0xFFE6D3A8)
    val Palm = Color(0xFF55703C)
    val Trunk = Color(0xFF7A5A38)
    val Date = Color(0xFF6B3A1E)
    val Bowl = Color(0xFF9A6A3A)
    val Gold = Color(0xFFE9C25A)
    val Plum = Color(0xFF4E2A5C)
    val Call = Color(0xFF7A4A22)
}

/**
 * A stone-carver's workshop by day, an arch on the right open on a sunny courtyard. Beats: carve (a carver
 * chisels a block on a plinth and the idol comes out of it, chunk by chunk: it is made), steal (a fly comes in by
 * the window and carries off a date from the offering at its foot; it does not stir), unheard (a man kneels and
 * calls on it: his calls break on the stone, and the one that reaches it gets nothing back), daughter (in the
 * courtyard, his ﷺ light and Fatima's name in its roundel; coins rise for her from his open chest, the words he
 * said on a slip), nothing (the coins fall back and the chest closes; the camera pulls back on both).
 */
internal fun DrawScope.atelierScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "chisel" to Shot(Offset(w * 0.44f, h * 0.56f), 1.4f),
        "steal" to Shot(Offset(w * 0.4f, h * 0.68f), 1.8f),
        "unheard" to Shot(Offset(w * 0.33f, h * 0.58f), 1.45f),
        "daughter" to Shot(Offset(w * 0.82f, h * 0.52f), 1.55f),
        "nothing" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { atelierRoom(a) }
}

private fun DrawScope.atelierRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cv = a.b("chisel")
    val st = a.b("steal")
    val uh = a.b("unheard")
    val dg = a.b("daughter")
    val nt = a.b("nothing")
    val floorY = h * 0.8f
    // the wall, the floor
    drawRect(Brush.verticalGradient(listOf(AtelierC9.Wall, AtelierC9.WallLow), endY = floorY))
    drawRect(AtelierC9.Floor, topLeft = Offset(0f, floorY), size = Size(w, h - floorY))
    for (k in 0 until 9) drawLine(AtelierC9.FloorLine, Offset(w * k / 9f, floorY), Offset(w * k / 9f - w * 0.04f, h), 0.8f * dp, alpha = 0.45f)
    drawLine(AtelierC9.FloorLine, Offset(0f, floorY), Offset(w, floorY), 1.2f * dp, alpha = 0.6f)
    // the courtyard through a great arch on the right
    val archL = w * 0.66f
    val archR = w * 0.98f
    val arch = horseshoe((archL + archR) / 2f, h * 0.34f, (archR - archL) / 2f, (archR - archL) * 0.42f, floorY)
    clipRect(archL - w * 0.02f, 0f, archR + w * 0.02f, floorY) {
        drawPath(arch, Brush.verticalGradient(listOf(AtelierC9.Sky, AtelierC9.SkyLow), startY = h * 0.05f, endY = h * 0.66f))
        drawRect(AtelierC9.Court, topLeft = Offset(archL, h * 0.66f), size = Size(archR - archL, floorY - h * 0.66f))
        w8Palm(Offset(w * 0.94f, h * 0.68f), h * 0.36f, AtelierC9.Trunk, AtelierC9.Palm, t)
        drawRect(AtelierC9.WallLow, topLeft = Offset(archL, h * 0.6f), size = Size(w * 0.14f, h * 0.06f))
    }
    drawPath(arch, AtelierC9.WoodDark, style = Stroke(3f * dp))
    // a window on the left, its light falling to the plinth
    val win = horseshoe(w * 0.17f, h * 0.2f, w * 0.05f, w * 0.04f, h * 0.38f)
    drawPath(win, AtelierC9.Sky)
    drawPath(win, AtelierC9.WoodDark, style = Stroke(2f * dp))
    drawLine(AtelierC9.WoodDark, Offset(w * 0.17f, h * 0.15f), Offset(w * 0.17f, h * 0.38f), 1.4f * dp)
    lightShafts(Offset(w * 0.17f, h * 0.27f), PI.toFloat() * 0.3f, 0.35f, h * 0.75f, Color(0xFFFFF4D6), 0.75f, t, 4)
    // the carver's tools on the wall
    drawLine(AtelierC9.Wood, Offset(w * 0.27f, h * 0.24f), Offset(w * 0.47f, h * 0.24f), 2.4f * dp, StrokeCap.Round)
    for (k in 0 until 5) {
        val x = w * (0.29f + 0.04f * k)
        drawLine(AtelierC9.Iron, Offset(x, h * 0.25f), Offset(x, h * (0.33f + 0.02f * (k % 2))), 1.8f * dp, StrokeCap.Round)
        drawRect(AtelierC9.Wood, topLeft = Offset(x - w * 0.005f, h * 0.245f), size = Size(w * 0.01f, h * 0.03f))
    }

    // the plinth, and the block that becomes an idol
    val px = w * 0.4f
    drawRect(AtelierC9.StoneDark, topLeft = Offset(px - w * 0.075f, h * 0.66f), size = Size(w * 0.15f, floorY - h * 0.66f))
    drawRect(AtelierC9.StoneLight, topLeft = Offset(px - w * 0.08f, h * 0.65f), size = Size(w * 0.16f, h * 0.02f))
    val idol = Path().apply {
        moveTo(px - w * 0.04f, h * 0.65f)
        lineTo(px - w * 0.036f, h * 0.52f)
        quadraticBezierTo(px - w * 0.045f, h * 0.47f, px - w * 0.022f, h * 0.46f)
        lineTo(px - w * 0.016f, h * 0.44f)
        quadraticBezierTo(px - w * 0.026f, h * 0.38f, px, h * 0.375f)
        quadraticBezierTo(px + w * 0.026f, h * 0.38f, px + w * 0.016f, h * 0.44f)
        lineTo(px + w * 0.022f, h * 0.46f)
        quadraticBezierTo(px + w * 0.045f, h * 0.47f, px + w * 0.036f, h * 0.52f)
        lineTo(px + w * 0.04f, h * 0.65f)
        close()
    }
    drawPath(idol, AtelierC9.Stone)
    drawPath(idol, Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.25f), Color.Transparent, Color.Black.copy(alpha = 0.18f)), startX = px - w * 0.045f, endX = px + w * 0.045f))
    drawLine(AtelierC9.StoneDark, Offset(px - w * 0.022f, h * 0.53f), Offset(px + w * 0.022f, h * 0.53f), 1f * dp, alpha = 0.6f)
    // the stone not yet cut away, falling off in chunks as the chisel goes
    val chunks = listOf(
        Triple(Offset(px - w * 0.05f, h * 0.36f), Size(w * 0.05f, h * 0.09f), 0.12f),
        Triple(Offset(px, h * 0.36f), Size(w * 0.05f, h * 0.09f), 0.3f),
        Triple(Offset(px - w * 0.05f, h * 0.45f), Size(w * 0.03f, h * 0.08f), 0.46f),
        Triple(Offset(px + w * 0.02f, h * 0.45f), Size(w * 0.03f, h * 0.08f), 0.6f),
        Triple(Offset(px - w * 0.05f, h * 0.53f), Size(w * 0.012f, h * 0.12f), 0.72f),
        Triple(Offset(px + w * 0.038f, h * 0.53f), Size(w * 0.012f, h * 0.12f), 0.8f),
    )
    for ((tl, sz, at) in chunks) {
        val off = w8EaseIn(seg(cv, at, at + 0.12f))
        if (off >= 1f) continue
        val drop = Offset((if (tl.x < px) -1f else 1f) * w * 0.04f * off, (floorY - tl.y - sz.height) * off)
        withTransform({ translate(drop.x, drop.y); rotate((if (tl.x < px) -50f else 50f) * off, tl + Offset(sz.width / 2, sz.height / 2)) }) {
            drawRect(AtelierC9.StoneLight, topLeft = tl, size = sz, alpha = 1f - 0.3f * off)
            drawRect(AtelierC9.StoneDark, topLeft = tl, size = sz, alpha = 0.5f * (1f - off), style = Stroke(0.8f * dp))
        }
    }
    // the chunks on the floor once they have fallen
    for ((k, c) in chunks.withIndex()) {
        if (cv < c.third + 0.12f) continue
        val x = px + (if (c.first.x < px) -1f else 1f) * w * (0.06f + 0.012f * k)
        drawRect(AtelierC9.StoneLight, topLeft = Offset(x - w * 0.012f, floorY - h * 0.02f), size = Size(w * 0.024f, h * 0.022f))
    }

    // the carver: hammering, then he steps back and goes
    val tall = h * 0.36f
    val gone = easeInOut(seg(st, 0f, 0.8f)).let { if (uh > 0f) 1f else it }
    val carverX = mix(w * 0.53f, w * 0.64f, gone)
    if (gone < 1f) {
        val foot = Offset(carverX, floorY + h * 0.03f)
        val hit = if (cv in 0.02f..0.92f) 0.5f + 0.5f * sin(t * 14f) else 0f
        figure(foot, tall, AtelierC9.Ink, alpha = 1f - seg(gone, 0.6f, 1f), walk = if (gone in 0.01f..0.99f) t * 6f else 0f, arms = if (gone > 0f) 0.2f else 0.75f + 0.2f * hit, lean = 0.08f * hit)
        if (gone <= 0f) {
            // chisel and mallet in his hands, chips flying where they meet the stone
            val tip = Offset(px + w * 0.045f, h * (0.42f + 0.2f * seg(cv, 0f, 0.9f)))
            drawLine(AtelierC9.Iron, tip, tip + Offset(w * 0.035f, -h * 0.01f), 2f * dp, StrokeCap.Round)
            drawRect(AtelierC9.Wood, topLeft = tip + Offset(w * (0.04f + 0.01f * hit), -h * 0.03f), size = Size(w * 0.018f, h * 0.035f))
            if (cv in 0.02f..0.92f) for (k in 0 until 6) {
                val f = fract(t * 2.4f + k / 6f)
                drawCircle(AtelierC9.StoneLight, (1.2f + w8Rnd(k, 3.1f)) * dp, tip + Offset(w * 0.03f * f * (0.5f + w8Rnd(k, 7.7f)), -h * 0.05f * bump(f) + h * 0.05f * f), alpha = 1f - f)
            }
        }
    }

    // steal: dates offered at its foot; a fly takes one away, and the idol does nothing
    val offer = easeOut(seg(st, 0f, 0.2f)).let { if (uh > 0f || dg > 0f) 1f else it }
    val bowlC = Offset(px + w * 0.02f, floorY + h * 0.035f)
    if (offer > 0f) {
        drawArc(AtelierC9.Bowl, 0f, 180f, true, topLeft = bowlC - Offset(w * 0.035f, h * 0.025f), size = Size(w * 0.07f, h * 0.05f), alpha = offer)
        val dates = if (st >= 0.55f || uh > 0f || dg > 0f) 4 else 5
        for (k in 0 until dates) drawOval(AtelierC9.Date, topLeft = bowlC + Offset((k - 2f) * w * 0.012f - w * 0.006f, -h * (0.014f + 0.006f * (k % 2))), size = Size(w * 0.012f, h * 0.018f), alpha = offer)
    }
    if (st > 0f && st < 1f) {
        val f = st
        val from = Offset(w * 0.17f, h * 0.27f)
        val at = bowlC + Offset(w * 0.024f, -h * 0.03f)
        val p = when {
            f < 0.45f -> cubic(from, from + Offset(w * 0.1f, h * 0.2f), at + Offset(-w * 0.1f, -h * 0.15f), at, easeInOut(f / 0.45f))
            f < 0.55f -> at + Offset(0f, h * 0.004f * sin(t * 30f))
            else -> cubic(at, at + Offset(w * 0.05f, -h * 0.2f), from + Offset(w * 0.12f, h * 0.05f), from, easeInOut((f - 0.55f) / 0.45f))
        }
        val s = w * 0.008f
        for (sd in listOf(-1f, 1f)) drawOval(Color.White, topLeft = p + Offset(if (sd < 0f) -s * 2.2f else s * 0.2f, -s * (1.4f + 0.6f * sin(t * 60f))), size = Size(s * 2f, s * 1.2f), alpha = 0.7f)
        drawOval(AtelierC9.Ink, topLeft = p - Offset(s, s * 0.6f), size = Size(s * 2f, s * 1.2f))
        if (f >= 0.55f) drawOval(AtelierC9.Date, topLeft = p + Offset(-s * 0.6f, s * 0.4f), size = Size(s * 1.2f, s * 1.6f))
    }

    // unheard: a man kneels and calls on it; his calls break on the stone; the last gets nothing back
    if (uh > 0f && dg <= 0.3f) {
        val come = easeOut(seg(uh, 0f, 0.15f))
        val seat = Offset(w * 0.25f, floorY + h * 0.03f)
        w8Seated(seat, tall * 0.92f, AtelierC9.Ink, facing = 1f, alpha = come * (1f - seg(dg, 0f, 0.3f)), speak = 0.6f + 0.4f * sin(t * 3f))
        val mouth = seat + Offset(w * 0.035f, -tall * 0.55f)
        val head = Offset(px, h * 0.41f)
        for (k in 0 until 3) {
            val f = seg(uh, 0.15f + 0.25f * k, 0.4f + 0.25f * k)
            if (f <= 0f || f >= 1f) continue
            val c = mouth + (head - mouth) * easeOut(f)
            if (k < 2 || f < 0.7f) {
                for (r in 0 until 3) drawArc(AtelierC9.Call, -40f, 80f, false, topLeft = c - Offset((8f + 6f * r) * dp, (8f + 6f * r) * dp), size = Size((16f + 12f * r) * dp, (16f + 12f * r) * dp), alpha = 0.9f * (1f - f * 0.5f), style = Stroke(2.2f * dp, cap = StrokeCap.Round))
            }
            // on the stone: the rings break into dust, or the last one sinks in and nothing comes back
            if (f > 0.7f) {
                val g = seg(f, 0.7f, 1f)
                for (d in 0 until 6) drawCircle(AtelierC9.StoneDark, 1.4f * dp, head + Offset(-w * 0.01f - w * 0.012f * d * 0.4f, h * 0.08f * g * (0.4f + 0.12f * d)), alpha = 1f - g)
            }
        }
        val silent = seg(uh, 0.88f, 1f)
        if (silent > 0f) for (k in 0 until 3) drawCircle(AtelierC9.StoneDark, 2f * dp, Offset(mix(px - w * 0.05f, mouth.x + w * 0.02f, 0.25f + 0.25f * k), h * 0.4f), alpha = silent * (0.3f + 0.3f * sin(t * 3f + k)))
    }

    // daughter: in the courtyard, his ﷺ light, Fatima's name in its roundel, and coins rising for her from his chest
    if (dg > 0f) {
        val light = Offset(w * 0.9f, h * 0.42f)
        sirajMunir(light, 4.6f * dp, easeOut(seg(dg, 0f, 0.2f)), t)
        val rc = Offset(w * 0.74f, h * 0.56f)
        nameRoundel(rc, w * 0.04f, a.big("daughter", 0, 1), AtelierC9.Plum, Roundel.Rim, easeOut(seg(dg, 0.1f, 0.3f)))
        val chest = Offset(w * 0.86f, h * 0.74f)
        val lid = easeOut(seg(dg, 0.25f, 0.4f)) * (1f - easeOut(seg(nt, 0.2f, 0.5f)))
        val cw = w * 0.07f
        val ch = h * 0.07f
        drawRect(AtelierC9.Wood, topLeft = Offset(chest.x - cw / 2, chest.y - ch), size = Size(cw, ch), alpha = seg(dg, 0.2f, 0.3f))
        drawRect(AtelierC9.Gold, topLeft = Offset(chest.x - cw / 2, chest.y - ch * 0.55f), size = Size(cw, ch * 0.08f), alpha = seg(dg, 0.2f, 0.3f))
        if (dg > 0.2f) {
            if (lid > 0.05f) for (k in 0 until 5) drawCircle(AtelierC9.Gold, w * 0.007f, Offset(chest.x + (k - 2) * w * 0.011f, chest.y - ch), alpha = lid)
            withTransform({ rotate(-100f * lid, Offset(chest.x - cw / 2, chest.y - ch)) }) {
                drawRect(AtelierC9.WoodDark, topLeft = Offset(chest.x - cw / 2, chest.y - ch - h * 0.022f), size = Size(cw, h * 0.022f))
            }
        }
        // the coins: rising to her while he speaks, falling back as the chest closes
        val give = seg(dg, 0.4f, 1f)
        val back = easeInOut(seg(nt, 0f, 0.4f))
        if (give > 0f) for (k in 0 until 6) {
            val f = if (back > 0f) 1f - back else fract(t * 0.45f + k / 6f)
            val from = Offset(chest.x + (k - 2.5f) * w * 0.006f, chest.y - ch)
            val to = rc + Offset((k - 2.5f) * w * 0.01f, h * 0.075f)
            val q = from + (to - from) * f + Offset(0f, -h * 0.12f * bump(f))
            drawCircle(AtelierC9.Gold, w * 0.006f, q, alpha = (if (back > 0f) 1f - back else 1f) * seg(give, 0f, 0.2f))
        }
        callout(Offset(w * 0.7f, h * 0.3f), a.big("daughter", 0, 0), w * 0.28f, seg(dg, 0.35f, 0.55f) * (1f - 0.6f * seg(nt, 0.3f, 0.7f)), ArtInk.Shade, Color(0xFFB0453A))
    }
}

// ═════════════ 2. heavens: the angels and the revelation (khawf-malaika) ═════════════

private object HeavensC9 {
    val Top = Color(0xFF02030A)
    val Low = Color(0xFF101836)
    val Band = Color(0xFFBFD4FF)
    val Angel = Color(0xFFF4F8FF)
    val AngelGlow = Color(0xFFD6E4FF)
    val Wave = Color(0xFFFFF4D8)
    val Earth = Color(0xFF0A0C16)
    val EarthRim = Color(0xFF3A4060)
    val Town = Color(0xFF151A2C)
    val Ink = Color(0xFF8C93B4)
    val Call = Color(0xFFE8D9B0)
}

/**
 * The seven heavens seen from the earth, each a faint arc with its angels as small white lights (no wings, no
 * figures). Beats: layers (the heavens and their angels appear, from the lowest to the highest), quake (a wave of
 * sound comes down from above through every heaven; each in turn trembles, flickering fast as if beating, and
 * bows low, dimmed by awe), calm (the trembling stops and their light comes back), truth (they pass word to one
 * another, heaven to heaven, in threads of light), nocall (on the earth a man calls up to them; his call fades out
 * under the lowest heaven, whose angels are bowed).
 */
internal fun DrawScope.heavensScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "heights" to Shot(Offset(w * 0.5f, h * 0.47f), 1.02f),
        "tremble" to Shot(Offset(w * 0.5f, h * 0.4f), 1.18f),
        "stillness" to Shot(Offset(w * 0.5f, h * 0.45f), 1.06f),
        "relay" to Shot(Offset(w * 0.5f, h * 0.42f), 1.15f),
        "callup" to Shot(Offset(w * 0.5f, h * 0.7f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { heavensSky(a) }
}

private fun DrawScope.heavensSky(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ly = a.b("heights")
    val qk = a.b("tremble")
    val cm = a.b("stillness")
    val tr = a.b("relay")
    val nc = a.b("callup")
    nightSky(t, HeavensC9.Top, HeavensC9.Low, 0.86f)
    // the seven heavens: arcs over the earth, the lowest nearest
    val cx = w * 0.5f
    val cy = h * 1.55f
    fun radius(k: Int) = h * (0.83f + 0.1f * k)          // k = 0 is the lowest heaven
    fun on(k: Int, u: Float): Offset {                     // u in -1..1 across the frame
        val r = radius(k)
        val ang = -PI.toFloat() / 2f + u * 0.42f * (1.05f - 0.05f * k)
        return Offset(cx + cos(ang) * r * 1.25f, cy + sin(ang) * r)
    }
    val waveY = mix(-h * 0.1f, h * 0.9f, w8EaseIn(seg(qk, 0f, 0.7f)))
    for (k in 0 until 7) {
        val show = easeOut(seg(ly, 0.04f + 0.11f * k, 0.18f + 0.11f * k))
        if (show <= 0f) continue
        val pts = List(41) { i -> on(k, -1.2f + 2.4f * i / 40f) }
        drawPath(w8Poly(pts), HeavensC9.Band, alpha = 0.16f * show, style = Stroke(5f * dp, cap = StrokeCap.Round))
        drawPath(w8Poly(pts), HeavensC9.Band, alpha = 0.45f * show, style = Stroke(0.9f * dp, cap = StrokeCap.Round))
        // where this heaven is when the wave passes, and how much it is still shaken
        val bandY = on(k, 0f).y
        val struck = seg(waveY, bandY - h * 0.02f, bandY + h * 0.04f)
        val ease = seg(cm, 0.05f + 0.1f * (6 - k), 0.35f + 0.1f * (6 - k))      // calm comes from the top down
        val shake = struck * (1f - ease)
        val bowAgain = if (nc > 0f && k == 0) easeOut(seg(nc, 0.3f, 0.6f)) else 0f
        val bow = max(easeOut(seg(struck, 0.3f, 1f)) * (1f - ease), bowAgain)
        val n = 9 - k / 2
        for (j in 0 until n) {
            val u = -0.85f + 1.7f * (j + 0.5f) / n + 0.03f * sin(k * 1.7f + j)
            var p = on(k, u)
            p += Offset(sin(t * 47f + j * 3.1f + k) * 1.6f * dp * shake, sin(t * 39f + j * 1.7f) * 1.2f * dp * shake)
            p += Offset(0f, h * 0.018f * bow)
            val lit = show * (1f - 0.55f * bow)
            softGlow(p, 6f * dp, HeavensC9.AngelGlow, 0.55f * lit)
            drawCircle(HeavensC9.Angel, 1.9f * dp, p, alpha = lit)
            // trembling: quick flashes to either side, like a beat
            if (shake > 0.05f) {
                val beat = 0.5f + 0.5f * sin(t * 26f + j)
                for (sd in listOf(-1f, 1f)) drawLine(HeavensC9.Angel, p + Offset(sd * 3f * dp, 0f), p + Offset(sd * (6f + 6f * beat) * dp, -2f * dp * beat), 1.1f * dp, StrokeCap.Round, alpha = 0.7f * shake * beat)
            }
        }
        // truth: word passed down from this heaven to the one below
        if (tr > 0f && k > 0) {
            val f = seg(tr, 0.08f * (6 - k), 0.08f * (6 - k) + 0.35f)
            if (f > 0f) for (j in 0 until 3) {
                val u = -0.5f + 0.5f * j
                val from = on(k, u + 0.05f)
                val to = on(k - 1, u - 0.04f)
                val mid = (from + to) / 2f + Offset(w * 0.02f, 0f)
                drawPath(cubicPart(from, mid, mid, to, 0f, easeOut(f)), HeavensC9.Wave, alpha = 0.7f * (1f - 0.4f * seg(tr, 0.8f, 1f)), style = Stroke(1.1f * dp, cap = StrokeCap.Round))
                if (f < 1f) glint(cubic(from, mid, mid, to, easeOut(f)), 4f * dp, HeavensC9.Wave, 0.9f)
            }
        }
    }
    // quake: the wave of the decree coming down through the heavens, from above them all
    if (qk > 0f && qk < 0.85f) {
        val fade = 1f - seg(qk, 0.7f, 0.85f)
        val pts = List(41) { i ->
            val x = w * (-0.05f + 1.1f * i / 40f)
            Offset(x, waveY + h * 0.06f * ((x - cx) / w).let { it * it * 4f } + h * 0.006f * (if (i % 2 == 0) 1f else -1f))
        }
        drawPath(w8Poly(pts), HeavensC9.Wave, alpha = 0.25f * fade, style = Stroke(9f * dp, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(w8Poly(pts), HeavensC9.Wave, alpha = 0.9f * fade, style = Stroke(1.4f * dp, join = StrokeJoin.Round))
    }
    // the earth, a small town, and the man who calls up to them
    drawPath(Path().apply {
        moveTo(0f, h * 0.86f); quadraticBezierTo(w * 0.5f, h * 0.8f, w, h * 0.86f); lineTo(w, h); lineTo(0f, h); close()
    }, HeavensC9.Earth)
    drawPath(Path().apply { moveTo(0f, h * 0.86f); quadraticBezierTo(w * 0.5f, h * 0.8f, w, h * 0.86f) }, HeavensC9.EarthRim, alpha = 0.7f, style = Stroke(1.2f * dp))
    for ((k, x) in listOf(0.12f, 0.17f, 0.21f, 0.76f, 0.8f, 0.86f).withIndex()) {
        val bh = h * (0.04f + 0.02f * (k % 3))
        val y = h * 0.86f - h * 0.035f * (1f - ((x - 0.5f) * 2f).let { it * it })
        drawRect(HeavensC9.Town, topLeft = Offset(w * x - w * 0.02f, y - bh), size = Size(w * 0.04f, bh + h * 0.02f))
    }
    if (nc > 0f) {
        val foot = Offset(w * 0.5f, h * 0.83f)
        val tall = h * 0.15f
        val raise = easeOut(seg(nc, 0f, 0.2f))
        figure(foot, tall, HeavensC9.Ink, alpha = raise, lean = -0.05f * raise)
        for (sd in listOf(-1f, 1f)) drawLine(HeavensC9.Ink, foot + Offset(0f, -tall * 0.76f), foot + Offset(sd * tall * 0.14f, -tall * (0.76f + 0.24f * raise)), max(1.6f * dp, tall * 0.06f), StrokeCap.Round, alpha = raise)
        // his call rises and fades out before it reaches them
        for (k in 0 until 4) {
            val f = fract(t * 0.5f + k / 4f)
            val c = foot + Offset(0f, -tall - (h * 0.32f) * f)
            val r = (8f + 26f * f) * dp
            drawArc(HeavensC9.Call, 200f, 140f, false, topLeft = c - Offset(r, r * 0.5f), size = Size(2 * r, r), alpha = 0.7f * (1f - f) * raise, style = Stroke(1.3f * dp, cap = StrokeCap.Round))
        }
    }
}

// ═════════════ 3. chamber: Abu Talib, the call, and the lock on the heart (hidayat-tawfiq) ═════════════

private object ChamberC9 {
    val Wall = Color(0xFF2E2128)
    val WallLow = Color(0xFF1E161C)
    val Floor = Color(0xFF3A2A22)
    val Rug = Color(0xFF6E2A2A)
    val RugLine = Color(0xFFC9964A)
    val Bed = Color(0xFF8A7458)
    val Blanket = Color(0xFF5A4A6A)
    val Pillow = Color(0xFFD9CCB0)
    val Night = Color(0xFF0B1230)
    val Moon = Color(0xFFF2EAD0)
    val Heart = Color(0xFFE8B8A0)
    val Iron = Color(0xFF3A3A44)
    val IronLight = Color(0xFF8A8A98)
    val Warm = Color(0xFFFFDFA0)
    val Smoke = Color(0xFF7A7484)
    val Shadow = Color(0xFF0E0A10)
    val Road = Color(0xFF3A3448)
    val Lit = Color(0xFFFFE2A0)
    val Back = Color(0xE6140E12)
}

/**
 * A room at night, a window on the right. Beats: bedside (a low bed, its blanket drawn up, his name on a tag; over
 * it, his heart with a padlock on it; his ﷺ light comes to the bedside), call (from the light, warm words go to the
 * heart, on a slip: «say: none has the right to be worshipped but Allah»; the heart flickers), turn (two dark
 * figures at the head of the bed blow grey smoke at it, on a tag: the creed of Abd al-Muttalib; the warm words are
 * pushed back, the heart goes grey and the room dims), verse (the verse that came down about it, in gold), lock (the
 * padlock: the heart opens only by Allah's guidance), show (through the window his light lays a lit road with
 * markers, and people walk it: the guidance of showing the way).
 */
internal fun DrawScope.chamberScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "bedside" to Shot(Offset(w * 0.46f, h * 0.56f), 1.15f),
        "call" to Shot(Offset(w * 0.52f, h * 0.48f), 1.3f),
        "sway" to Shot(Offset(w * 0.34f, h * 0.52f), 1.3f),
        "verse" to Shot(Offset(w * 0.46f, h * 0.36f), 1.12f),
        "padlock" to Shot(Offset(w * 0.45f, h * 0.4f), 1.3f),
        "guideway" to Shot(Offset(w * 0.82f, h * 0.5f), 1.55f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.16f) { chamberRoom(a) }
}

/** A heart with a padlock on it (the Qur'an's own image of hearts that are locked). [grey] 0..1 drains it. */
private fun DrawScope.lockedHeart(c: Offset, r: Float, grey: Float, flicker: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val col = lerpColor(ChamberC9.Heart, Color(0xFF6E6A72), grey)
    if (flicker > 0f) softGlow(c, r * 2.2f, ChamberC9.Warm, 0.6f * flicker * alpha)
    drawPath(w8Heart(c, r), col, alpha = alpha)
    drawPath(w8Heart(c, r), Color.Black, alpha = 0.25f * alpha, style = Stroke(1.2f * density))
    // the padlock
    val lb = Offset(c.x, c.y + r * 0.15f)
    drawArc(ChamberC9.IronLight, 180f, 180f, false, topLeft = lb - Offset(r * 0.3f, r * 0.62f), size = Size(r * 0.6f, r * 0.6f), alpha = alpha, style = Stroke(r * 0.11f))
    drawRoundRect(ChamberC9.Iron, topLeft = lb - Offset(r * 0.42f, r * 0.32f), size = Size(r * 0.84f, r * 0.62f), cornerRadius = CornerRadius(r * 0.1f), alpha = alpha)
    drawCircle(Color.Black, r * 0.08f, lb, alpha = alpha)
    drawLine(Color.Black, lb, lb + Offset(0f, r * 0.16f), r * 0.07f, alpha = alpha)
}

private fun DrawScope.chamberRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bs = a.b("bedside")
    val cl = a.b("call")
    val tn = a.b("sway")
    val vs = a.b("verse")
    val lk = a.b("padlock")
    val sh = a.b("guideway")
    val dim = easeOut(seg(tn, 0.6f, 1f))
    val floorY = h * 0.8f
    drawRect(Brush.verticalGradient(listOf(ChamberC9.Wall, ChamberC9.WallLow), endY = floorY))
    drawRect(ChamberC9.Floor, topLeft = Offset(0f, floorY), size = Size(w, h - floorY))
    drawRect(ChamberC9.Rug, topLeft = Offset(w * 0.14f, floorY + h * 0.03f), size = Size(w * 0.56f, h * 0.12f))
    drawRect(ChamberC9.RugLine, topLeft = Offset(w * 0.15f, floorY + h * 0.04f), size = Size(w * 0.54f, h * 0.1f), alpha = 0.6f, style = Stroke(1f * dp))
    // the doorway on the left, dark
    drawPath(horseshoe(w * 0.08f, h * 0.36f, w * 0.055f, w * 0.045f, floorY), ChamberC9.Shadow)
    // the window on the right: the night, the moon; later, the lit road beyond it
    val wl = w * 0.76f
    val wr = w * 0.96f
    val win = horseshoe((wl + wr) / 2f, h * 0.3f, (wr - wl) / 2f, (wr - wl) * 0.42f, h * 0.74f)
    clipRect(wl, 0f, wr, h * 0.74f) {
        drawPath(win, ChamberC9.Night)
        for (k in 0 until 10) drawCircle(Color.White, (0.6f + 0.6f * w8Rnd(k, 2.3f)) * dp, Offset(mix(wl, wr, w8Rnd(k, 9.1f)), h * (0.12f + 0.3f * w8Rnd(k, 4.4f))), alpha = 0.6f)
        drawCircle(ChamberC9.Moon, w * 0.016f, Offset(wr - w * 0.04f, h * 0.18f))
        // the road out there, lit by his light, with its markers and those who walk it
        val lit = easeOut(seg(sh, 0.1f, 0.5f))
        val roadTop = Offset((wl + wr) / 2f + w * 0.02f, h * 0.5f)
        drawPath(Path().apply { moveTo(roadTop.x - w * 0.006f, roadTop.y); lineTo(roadTop.x + w * 0.006f, roadTop.y); lineTo(wr, h * 0.74f); lineTo(wl + w * 0.02f, h * 0.74f); close() }, lerpColor(ChamberC9.Road, ChamberC9.Lit, 0.55f * lit))
        drawRect(Color(0xFF141A30), topLeft = Offset(wl, h * 0.48f), size = Size(wr - wl, h * 0.02f))
        if (lit > 0f) {
            for (k in 0 until 3) {
                val f = 0.25f + 0.25f * k
                val y = mix(roadTop.y, h * 0.74f, f)
                val x = mix(roadTop.x, wl + w * 0.03f, f) - w * 0.008f
                val on = seg(sh, 0.3f + 0.15f * k, 0.45f + 0.15f * k)
                drawLine(ChamberC9.Lit, Offset(x, y), Offset(x, y - h * 0.05f * (0.5f + f)), 1.4f * dp, alpha = on)
                drawRect(ChamberC9.Lit, topLeft = Offset(x - w * 0.012f * (0.5f + f), y - h * 0.06f * (0.5f + f)), size = Size(w * 0.024f * (0.5f + f), h * 0.016f * (0.5f + f)), alpha = on)
                softGlow(Offset(x, y - h * 0.05f * (0.5f + f)), w * 0.02f, ChamberC9.Lit, 0.6f * on)
            }
            for (k in 0 until 2) {
                val f = 0.9f - 0.55f * fract(t * 0.06f + k * 0.4f)
                val foot = Offset(mix(roadTop.x, (wl + wr) / 2f, f), mix(roadTop.y, h * 0.74f, f))
                figure(foot, h * 0.15f * (0.3f + 0.7f * f), Color(0xFF2A2030), alpha = lit * seg(sh, 0.5f, 0.7f), walk = t * 6f + k)
            }
        }
    }
    drawPath(win, Color(0xFF5A4030), style = Stroke(3f * dp))
    drawLine(Color(0xFF5A4030), Offset((wl + wr) / 2f, h * 0.18f), Offset((wl + wr) / 2f, h * 0.74f), 1.4f * dp, alpha = 1f - easeOut(seg(sh, 0f, 0.3f)))
    // the bed, its blanket drawn up over him
    val bedL = w * 0.2f
    val bedR = w * 0.6f
    val bedTop = h * 0.72f
    drawRect(ChamberC9.Bed, topLeft = Offset(bedL, bedTop), size = Size(bedR - bedL, floorY - bedTop + h * 0.02f))
    drawRect(Color.Black, topLeft = Offset(bedL, floorY), size = Size(bedR - bedL, h * 0.02f), alpha = 0.3f)
    drawRoundRect(ChamberC9.Pillow, topLeft = Offset(bedL + w * 0.01f, bedTop - h * 0.04f), size = Size(w * 0.07f, h * 0.05f), cornerRadius = CornerRadius(8f * dp))
    val cover = Path().apply {
        moveTo(bedL + w * 0.07f, bedTop + h * 0.005f)
        cubicTo(bedL + w * 0.1f, bedTop - h * 0.07f, bedR - w * 0.12f, bedTop - h * 0.045f, bedR - w * 0.03f, bedTop - h * 0.03f)
        lineTo(bedR, bedTop + h * 0.005f)
        close()
    }
    drawPath(cover, lerpColor(ChamberC9.Blanket, Color(0xFF3A3242), dim))
    drawPath(cover, Color.Black, alpha = 0.2f, style = Stroke(1f * dp))
    // the room's warm light from his presence, which dims at the end
    val here = easeOut(seg(bs, 0.3f, 0.6f))
    val lightC = Offset(w * 0.68f, h * 0.5f)
    softGlow(lightC, w * 0.3f, ChamberC9.Warm, 0.18f * here * (1f - 0.5f * dim))
    sirajMunir(lightC, 4.6f * dp, here, t, nameAt = lightC + Offset(0f, h * 0.12f))
    tagAt(a.tag("bedside"), Offset(w * 0.3f, h * 0.6f), seg(bs, 0.15f, 0.4f) * (1f - 0.5f * dim), back = ChamberC9.Back, ink = ArtInk.Ivory)

    // his heart over the bed, locked
    val hc = Offset(w * 0.4f, h * 0.44f)
    val words = easeOut(seg(cl, 0.1f, 0.5f)) * (1f - easeOut(seg(tn, 0.25f, 0.6f)))
    val flicker = if (cl > 0.4f && tn < 0.6f) (0.5f + 0.5f * sin(t * 5f)) * words else 0f
    val grey = easeOut(seg(tn, 0.4f, 0.9f))
    val heartShow = easeOut(seg(bs, 0.1f, 0.35f))
    val shake = if (cl > 0.45f && tn <= 0f) sin(t * 18f) * 1.5f * dp else 0f
    lockedHeart(hc + Offset(shake, 0f), w * 0.06f, grey, flicker, heartShow)
    // lock: the padlock under the verse, picked out in gold: only His guidance opens it
    if (lk > 0f) {
        val pulse = 0.5f + 0.5f * sin(t * 2.5f)
        drawCircle(ArtInk.Gold, w * (0.075f + 0.01f * pulse), hc + Offset(0f, w * 0.008f), alpha = 0.7f * easeOut(seg(lk, 0f, 0.3f)), style = Stroke(1.6f * dp))
    }
    // call: his words go from the light to the heart
    if (words > 0f) {
        val from = lightC + Offset(-w * 0.02f, 0f)
        val to = hc + Offset(w * 0.04f, 0f)
        val c1 = from + Offset(-w * 0.06f, -h * 0.12f)
        val c2 = to + Offset(w * 0.08f, -h * 0.1f)
        drawPath(cubicPart(from, c1, c2, to, 0f, words), ChamberC9.Warm, alpha = 0.3f, style = Stroke(8f * dp, cap = StrokeCap.Round))
        drawPath(cubicPart(from, c1, c2, to, 0f, words), ChamberC9.Warm, alpha = 0.9f, style = Stroke(1.6f * dp, cap = StrokeCap.Round))
        for (k in 0 until 4) {
            val f = fract(t * 0.5f + k / 4f) * words
            glint(cubic(from, c1, c2, to, f), 4f * dp, ChamberC9.Warm, 0.8f)
        }
    }
    callout(Offset(w * 0.58f, h * 0.22f), a.big("call", 0, 0), w * 0.36f, seg(cl, 0.15f, 0.35f) * (1f - easeOut(seg(tn, 0.3f, 0.6f))), ArtInk.Shade, Color(0xFFB0453A))

    // turn: two dark figures at the head of the bed, and their grey smoke around the heart
    if (tn > 0f) {
        val come = easeInOut(seg(tn, 0f, 0.3f))
        val leave = easeOut(seg(tn, 0.85f, 1f))
        for (k in 0 until 2) {
            val foot = Offset(mix(w * (0.06f + 0.04f * k), w * (0.12f + 0.045f * k), come), floorY + h * 0.02f)
            withTransform({ scale(-1f, 1f, pivot = foot) }) {
                figure(foot, h * 0.36f * (0.97f + 0.03f * k), ChamberC9.Shadow, alpha = (1f - leave) * seg(tn, 0f, 0.15f), walk = if (come < 1f) t * 6f + k else 0f, lean = -0.12f * come, arms = 0.6f * come)
            }
        }
        val smoke = easeOut(seg(tn, 0.2f, 0.6f)) * (1f - 0.6f * leave)
        for (k in 0 until 14) {
            val f = fract(t * 0.25f + k / 14f)
            val ang = f * PI.toFloat() * 2f + k
            val rr = w * (0.05f + 0.03f * sin(t + k))
            val p = mix(w * 0.2f, hc.x, smoke).let { Offset(it, mix(h * 0.5f, hc.y, smoke)) } + Offset(cos(ang) * rr, sin(ang) * rr * 0.7f)
            drawCircle(ChamberC9.Smoke, w * (0.018f + 0.012f * w8Rnd(k, 3.3f)), p, alpha = 0.35f * smoke)
        }
        tagAt(a.tag("sway"), Offset(w * 0.2f, h * 0.3f), seg(tn, 0.25f, 0.45f) * (1f - leave), back = ChamberC9.Back, ink = Color(0xFFC9C2D6))
    }
    if (dim > 0f) drawRect(Color.Black, alpha = 0.25f * dim * (1f - 0.6f * easeOut(seg(sh, 0f, 0.4f))))
    // verse: what came down about it
    val vAl = easeOut(seg(vs, 0.05f, 0.3f)) * (1f - 0.7f * easeOut(seg(sh, 0f, 0.3f)))
    callout(Offset(w * 0.47f, h * 0.11f), a.big("verse", 0, 0), w * 0.52f, vAl, ArtInk.Shade, ArtInk.Gold)
    callout(Offset(w * 0.47f, h * 0.23f), a.big("verse", 0, 1), w * 0.56f, easeOut(seg(vs, 0.2f, 0.45f)) * (1f - 0.7f * easeOut(seg(sh, 0f, 0.3f))), ArtInk.Shade, ArtInk.Gold)
    // show: his light sends its beam out of the window onto the road
    if (sh > 0f) {
        val beam = easeOut(seg(sh, 0f, 0.35f))
        lightShafts(lightC, -0.05f, 0.3f, w * 0.3f * beam, ChamberC9.Warm, 0.8f * beam, t, 4)
    }
}

// ═════════════ 4. scroll: the sky rolled up over a city of tyrants (azamat-allah) ═════════════

private object ScrollC9 {
    val SkyTop = Color(0xFF4F8FD0)
    val SkyLow = Color(0xFFCDE6F5)
    val Sun = Color(0xFFFFF0B0)
    val Cloud = Color(0xFFF6F8FA)
    val Void = Color(0xFFF2EEE6)
    val VoidLow = Color(0xFFE2D9C6)
    val Roll = Color(0xFF6F9FCF)
    val RollEdge = Color(0xFF3E6A98)
    val Sand = Color(0xFFD8B47E)
    val SandDark = Color(0xFFB48C58)
    val Tower = Color(0xFFB98A5A)
    val TowerDark = Color(0xFF8E623C)
    val Window = Color(0xFF3A2618)
    val Banner = Color(0xFF9A2A2A)
    val Crown = Color(0xFFE6B93E)
    val Dust = Color(0xFFCDB089)
    val Idol = Color(0xFF8C857A)
    val Ink = Color(0xFF3A2A1E)
}

/**
 * A city of tyrants under a blue sky. Beats: towers (their towers and palace rise, crowned and bannered), roll (the
 * sky, with its sun and clouds, rolls up from both sides like a scroll and is taken away, leaving only a pale
 * emptiness: nothing is drawn in its place), crumble (the towers fall to dust and their crowns roll in the sand),
 * idols (among the ruins men still bow to small carved stones; the verse, in gold).
 */
internal fun DrawScope.scrollScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "towers" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "furl" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "crumble" to Shot(Offset(w * 0.5f, h * 0.64f), 1.2f),
        "ruins" to Shot(Offset(w * 0.3f, h * 0.66f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { scrollCity(a) }
}

/** A crown: a gold band with five points, centred at [c], [s] wide. */
private fun DrawScope.crown9(c: Offset, s: Float, alpha: Float, tilt: Float = 0f) {
    if (alpha <= 0.01f) return
    withTransform({ rotate(tilt, c) }) {
        val p = Path().apply {
            moveTo(c.x - s / 2, c.y)
            lineTo(c.x - s / 2, c.y - s * 0.35f)
            lineTo(c.x - s * 0.3f, c.y - s * 0.18f)
            lineTo(c.x - s * 0.15f, c.y - s * 0.45f)
            lineTo(c.x, c.y - s * 0.2f)
            lineTo(c.x + s * 0.15f, c.y - s * 0.45f)
            lineTo(c.x + s * 0.3f, c.y - s * 0.18f)
            lineTo(c.x + s / 2, c.y - s * 0.35f)
            lineTo(c.x + s / 2, c.y)
            close()
        }
        drawPath(p, ScrollC9.Crown, alpha = alpha)
        drawPath(p, Color(0xFF8A6418), alpha = alpha, style = Stroke(0.8f * density))
        drawCircle(Color(0xFFB0302A), s * 0.06f, c + Offset(0f, -s * 0.1f), alpha = alpha)
    }
}

private fun DrawScope.scrollCity(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tw = a.b("towers")
    val rl = a.b("furl")
    val cr = a.b("crumble")
    val id = a.b("ruins")
    val ground = h * 0.8f
    // behind the sky: a pale emptiness, nothing drawn in it
    drawRect(Brush.verticalGradient(listOf(ScrollC9.Void, ScrollC9.VoidLow), endY = ground), size = Size(w, ground))
    // the sky: one sheet, rolled up from both sides into one scroll, then taken away
    val closeIn = easeInOut(seg(rl, 0.05f, 0.7f))
    val away = easeIn9(seg(rl, 0.72f, 1f))
    val left = mix(0f, w * 0.5f, closeIn)
    val right = mix(w, w * 0.5f, closeIn)
    if (away < 1f) {
        clipRect(left, 0f, right, ground) {
            drawRect(Brush.verticalGradient(listOf(ScrollC9.SkyTop, ScrollC9.SkyLow), endY = ground), size = Size(w, ground))
            val sun = Offset(w * 0.76f, h * 0.2f)
            softGlow(sun, w * 0.09f, ScrollC9.Sun, 0.9f)
            drawCircle(ScrollC9.Sun, w * 0.035f, sun)
            for (k in 0 until 3) {
                val c = Offset(w * (0.15f + 0.3f * k), h * (0.12f + 0.06f * (k % 2)))
                for (o in listOf(Offset(-w * 0.03f, 0f), Offset(0f, -h * 0.02f), Offset(w * 0.03f, 0f))) drawCircle(ScrollC9.Cloud, w * 0.03f, c + o, alpha = 0.85f)
            }
        }
        // the two rolls at the edges of what is left, growing thicker as they gather the sky
        val thick = w * (0.008f + 0.03f * closeIn)
        val rise = away * h * 0.5f
        val shrink = 1f - away
        for (x in listOf(left, right)) {
            if (closeIn <= 0.01f) continue
            val tl = Offset(x - thick, -h * 0.02f + rise)
            val sz = Size(thick * 2f, (ground + h * 0.02f) * shrink)
            drawRoundRect(Brush.horizontalGradient(listOf(ScrollC9.RollEdge, ScrollC9.Roll, ScrollC9.RollEdge), startX = tl.x, endX = tl.x + sz.width), topLeft = tl, size = sz, cornerRadius = CornerRadius(thick), alpha = 1f - away)
        }
    }
    // the desert floor
    drawRect(Brush.verticalGradient(listOf(ScrollC9.Sand, ScrollC9.SandDark), startY = ground, endY = h), topLeft = Offset(0f, ground), size = Size(w, h - ground))
    // the towers of the tyrants and the palace: they rise, then crumble to dust
    val towers = listOf(0.14f to 0.42f, 0.27f to 0.52f, 0.5f to 0.36f, 0.73f to 0.5f, 0.86f to 0.4f)
    for ((k, tw0) in towers.withIndex()) {
        val (x, th) = tw0
        val up = easeOut(seg(tw, 0.05f + 0.1f * k, 0.45f + 0.1f * k))
        val fall = w8EaseIn(seg(cr, 0.05f + 0.08f * k, 0.55f + 0.08f * k))
        val height = h * th * up * (1f - fall)
        if (up <= 0f) continue
        val bw = if (k == 2) w * 0.16f else w * 0.075f
        val top = ground - height
        if (height > 1f) {
            drawRect(ScrollC9.Tower, topLeft = Offset(w * x - bw / 2, top), size = Size(bw, height))
            drawRect(ScrollC9.TowerDark, topLeft = Offset(w * x + bw * 0.2f, top), size = Size(bw * 0.3f, height), alpha = 0.5f)
            var y = top + h * 0.04f
            while (y < ground - h * 0.05f) {
                drawRect(ScrollC9.Window, topLeft = Offset(w * x - bw * 0.15f, y), size = Size(bw * 0.12f, h * 0.03f))
                y += h * 0.07f
            }
            if (k == 2) drawArc(ScrollC9.Tower, 180f, 180f, true, topLeft = Offset(w * x - bw * 0.4f, top - bw * 0.4f), size = Size(bw * 0.8f, bw * 0.8f))
            // the banner
            if (fall <= 0f) {
                drawLine(ScrollC9.TowerDark, Offset(w * x, top - h * 0.01f), Offset(w * x, top - h * 0.08f), 1.4f * dp)
                drawPath(Path().apply {
                    moveTo(w * x, top - h * 0.08f)
                    quadraticBezierTo(w * x + w * 0.02f, top - h * (0.075f + 0.008f * sin(t * 4f + k)), w * x + w * 0.04f, top - h * 0.07f)
                    lineTo(w * x, top - h * 0.055f)
                    close()
                }, ScrollC9.Banner)
            }
        }
        // the crown on top, falling and rolling in the sand
        val topNow = ground - h * th * up
        val crownTop = Offset(w * x, (if (k == 2) topNow - bw * 0.4f else topNow) - h * 0.012f)
        val drop = w8EaseIn(seg(cr, 0.1f + 0.08f * k, 0.5f + 0.08f * k))
        val crownAt = Offset(crownTop.x + w * 0.03f * drop * (if (k % 2 == 0) 1f else -1f), mix(crownTop.y, ground + h * 0.06f + h * 0.02f * (k % 3), drop))
        crown9(crownAt, w * (if (k == 2) 0.05f else 0.035f), seg(tw, 0.4f + 0.1f * k, 0.55f + 0.1f * k), tilt = 110f * drop * (if (k % 2 == 0) 1f else -1f))
        // dust where it fell
        if (fall > 0f && fall < 1f) for (d in 0 until 6) {
            val c = Offset(w * x + (d - 2.5f) * bw * 0.3f, ground - h * 0.03f * d * fall)
            drawCircle(ScrollC9.Dust, w * (0.025f + 0.01f * d) * fall, c, alpha = 0.5f * (1f - fall))
        }
        if (fall >= 1f) drawArc(ScrollC9.SandDark, 180f, 180f, true, topLeft = Offset(w * x - bw * 0.7f, ground - h * 0.03f), size = Size(bw * 1.4f, h * 0.06f))
    }
    // idols: among the ruins, men still bow to small carved stones
    if (id > 0f) {
        val come = easeOut(seg(id, 0f, 0.25f))
        for (k in 0 until 3) {
            val c = Offset(w * (0.12f + 0.065f * k), ground + h * 0.09f)
            val s = w * (0.034f - 0.005f * (k % 2))
            drawRect(ScrollC9.Idol, topLeft = Offset(c.x - s * 0.5f, c.y - s * 1.4f), size = Size(s, s * 1.4f), alpha = come)
            drawCircle(ScrollC9.Idol, s * 0.38f, Offset(c.x, c.y - s * 1.7f), alpha = come)
        }
        for (k in 0 until 2) {
            val foot = Offset(w * (0.34f + 0.08f * k), ground + h * 0.09f)
            val bow = bump(fract(seg(id, 0.2f, 1f) * 1.5f + k * 0.2f)).let { 0.4f + 0.6f * it }
            w8Worshipper(foot, h * 0.22f, ScrollC9.Ink, 0f, bow * come, come)
        }
        callout(Offset(w * 0.36f, h * 0.47f), a.big("ruins", 0, 0), w * 0.38f, seg(id, 0.3f, 0.5f), ArtInk.Shade, ArtInk.Gold)
    }
}

private fun easeIn9(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }
