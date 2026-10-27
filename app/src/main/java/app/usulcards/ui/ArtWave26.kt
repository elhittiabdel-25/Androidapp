package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextLayoutResult
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Round 14 of the redraw (2026-10-27): «الصفات واليوم الآخر». Nine scenes replace the night sky with words in it:
 * the verse of rising above the heavens and the people everywhere, and the moon that "goes with" the traveller;
 * revelation brought down and then recited, memorised and written; the believers whose faces are bright, and the
 * veiled; the trumpet, the rising, the books, the scale and the bridge; the names on a tiled wall and the three ways
 * of turning from them; a register that takes a name only from the texts; the open library and the sealed door of
 * the unseen; ten medallions of the signs of the Hour; and the grave, its questions and its two ends.
 * Allah is never drawn: only His names and verses, as words.
 */

/** A verse or a name set large (a.big), scaled down so it never runs wider than [maxW]. */
internal fun DrawScope.fitBig26(tl: TextLayoutResult?, c: Offset, maxW: Float, color: Color, alpha: Float, glow: Color? = null) {
    if (tl == null || alpha <= 0.01f) return
    val sc = min(1f, maxW / max(1f, tl.size.width.toFloat()))
    if (glow != null) softGlow(c, tl.size.width * sc * 0.55f, glow, 0.35f * alpha)
    bigText(tl, c, sc, color, alpha)
}

/** An angel as the codex draws it: a cool white light. */
internal fun DrawScope.angelLight26(c: Offset, r: Float, t: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 4.5f, Color(0xFFCFE4FF), 0.55f * alpha)
    bloom(c, r * 1.2f, Color(0xFFEAF4FF), 0.9f * alpha)
    drawCircle(Color.White, r * (0.55f + 0.05f * sin(t * 3f)), c, alpha = alpha)
}

/** A camel standing, facing left, its feet at [foot]. */
internal fun DrawScope.camel26(foot: Offset, s: Float, color: Color, alpha: Float, t: Float, walk: Float = 0f) {
    if (alpha <= 0.01f) return
    val body = foot + Offset(0f, -s * 0.62f)
    drawOval(color, Offset(body.x - s * 0.42f, body.y - s * 0.16f), Size(s * 0.84f, s * 0.32f), alpha = alpha)
    drawOval(color, Offset(body.x - s * 0.14f, body.y - s * 0.34f), Size(s * 0.34f, s * 0.3f), alpha = alpha)
    val neck = body + Offset(-s * 0.38f, -s * 0.04f)
    drawLine(color, neck, neck + Offset(-s * 0.16f, -s * 0.3f), s * 0.1f, StrokeCap.Round, alpha = alpha)
    drawOval(color, Offset(neck.x - s * 0.34f, neck.y - s * 0.4f), Size(s * 0.22f, s * 0.12f), alpha = alpha)
    for (k in 0 until 4) {
        val x = body.x - s * 0.3f + k * s * 0.2f
        val sw = if (walk != 0f) sin(walk + k * 1.6f) * s * 0.06f else 0f
        drawLine(color, Offset(x, body.y + s * 0.1f), Offset(x + sw, foot.y), s * 0.06f, StrokeCap.Round, alpha = alpha)
    }
}

/** A sheep, facing left. */
internal fun DrawScope.sheep26(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawOval(color, Offset(c.x - s * 0.5f, c.y - s * 0.3f), Size(s, s * 0.6f), alpha = alpha)
    drawOval(color, Offset(c.x - s * 0.72f, c.y - s * 0.32f), Size(s * 0.32f, s * 0.26f), alpha = alpha)
    for (k in 0 until 2) drawLine(color, c + Offset(-s * 0.25f + k * s * 0.5f, s * 0.2f), c + Offset(-s * 0.25f + k * s * 0.5f, s * 0.48f), s * 0.08f, alpha = alpha)
}

// ═════════════ 1. the heights, and the people everywhere (uluw-maiyya) ═════════════

private object HeiC26 {
    val Top = Color(0xFF0B1230)
    val Low = Color(0xFF34396E)
    val DuneFar = Color(0xFF2E2A4C)
    val Dune = Color(0xFF221D36)
    val Water = Color(0xFF223C64)
    val Ink = Color(0xFF0A0812)
    val Layer = Color(0xFFBFC8F0)
    val Gold = Color(0xFFF2D27A)
    val Moon = Color(0xFFF6F0DA)
    val Window = Color(0xFFFFD27A)
    val Back = Color(0xE60E1024)
    val Red = Color(0xFFE0584A)
}

/**
 * Beats: above (layers of the heavens, fading as they rise, and over them only the verse
 * ﴿ثُمَّ اسْتَوَى عَلَى الْعَرْشِ﴾), withall (the verse ﴿وَهُوَ مَعَكُمْ أَيْنَ مَا كُنْتُمْ﴾ and, below, people in four
 * places one after another: a traveller and his camel, a shepherd, a boatman, a man praying by his house), notmixed
 * («مُخْتَلِطٌ بِخَلْقِهِ» struck out between the heavens and the earth), moonwalk (the card's example: the moon rises
 * and keeps pace with the walking traveller while it stays in the sky; ﴿وَلِلَّهِ الْمَثَلُ الْأَعْلَى﴾).
 */
internal fun DrawScope.heightsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "above" to Shot(Offset(w * 0.5f, h * 0.12f), 1.3f),
        "withall" to Shot(Offset(w * 0.5f, h * 0.62f), 1.05f),
        "notmixed" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "moonwalk" to Shot(Offset(w * 0.8f, h * 0.5f), 1.45f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { heightsNight(a) }
}

private fun DrawScope.heightsNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ab = a.b("above")
    val wa = a.b("withall")
    val nm = a.b("notmixed")
    val mw = a.b("moonwalk")
    val horizon = h * 0.64f
    deepSky(t, horizon, HeiC26.Top, HeiC26.Low, starAlpha = 0.8f)
    // the heavens: layer over layer, fading as they rise
    for (k in 0 until 7) {
        val f = easeOut(seg(ab, k * 0.07f, k * 0.07f + 0.35f))
        if (f <= 0f) continue
        val rx = w * (0.62f + 0.05f * k)
        val ry = h * (0.5f + 0.025f * k)
        val c = Offset(w * 0.5f, h * 0.85f)
        drawArc(HeiC26.Layer, 200f, 140f, false, Offset(c.x - rx, c.y - ry), Size(rx * 2f, ry * 2f), alpha = f * (0.5f - 0.06f * k), style = Stroke((1.6f - 0.15f * k) * dp))
    }
    fitBig26(a.big("above"), Offset(w * 0.5f, h * 0.1f), w * 0.55f, HeiC26.Gold, easeOut(seg(ab, 0.45f, 0.8f)), glow = HeiC26.Gold)
    // the earth
    hills(HeiC26.DuneFar, horizon, h * 0.02f, 0.012f, 0.031f, 0.6f)
    drawRect(HeiC26.Water, Offset(w * 0.25f, h * 0.7f), Size(w * 0.24f, h * 0.06f), alpha = 0.9f)
    for (k in 0 until 4) drawLine(Color(0xFF6A8AC0), Offset(w * (0.27f + 0.05f * k) + sin(t + k) * w * 0.006f, h * 0.73f), Offset(w * (0.29f + 0.05f * k) + sin(t + k) * w * 0.006f, h * 0.73f), 1f * dp, alpha = 0.6f)
    hills(HeiC26.Dune, h * 0.78f, h * 0.03f, 0.009f, 0.022f, 2.1f)
    // the people in four places
    val tall = h * 0.13f
    val walk = easeInOut(seg(mw, 0.15f, 0.95f))
    val trav = Offset(w * mix(0.86f, 0.7f, walk), h * 0.83f)
    val stepping = walk in 0.01f..0.99f
    val v = List(4) { k -> easeOut(seg(wa, k * 0.2f, k * 0.2f + 0.3f)).coerceAtLeast(if (mw > 0f && k == 0) 1f else 0f) }
    camel26(trav + Offset(w * 0.07f, 0f), h * 0.16f, HeiC26.Ink, v[0], t, if (stepping) t * 4f else 0f)
    figure(trav, tall, HeiC26.Ink, v[0], walk = if (stepping) t * 5f else 0f)
    // the shepherd and his sheep
    figure(Offset(w * 0.6f, h * 0.84f), tall, HeiC26.Ink, v[1])
    drawLine(HeiC26.Ink, Offset(w * 0.585f, h * 0.84f), Offset(w * 0.575f, h * 0.84f - tall * 1.05f), 1.6f * dp, alpha = v[1])
    for (k in 0 until 3) sheep26(Offset(w * (0.51f - 0.045f * k), h * (0.84f - 0.01f * k)), w * 0.03f, Color(0xFF3A3450), v[1])
    // the boatman on the water
    if (v[2] > 0f) {
        val bob = sin(t * 1.4f) * h * 0.004f
        val bc = Offset(w * 0.37f, h * 0.72f + bob)
        drawPath(Path().apply { moveTo(bc.x - w * 0.06f, bc.y); lineTo(bc.x + w * 0.06f, bc.y); lineTo(bc.x + w * 0.04f, bc.y + h * 0.025f); lineTo(bc.x - w * 0.045f, bc.y + h * 0.025f); close() }, HeiC26.Ink, alpha = v[2])
        figure(bc + Offset(w * 0.01f, 0f), tall * 0.75f, HeiC26.Ink, v[2], arms = 0.5f)
        drawLine(HeiC26.Ink, bc + Offset(-w * 0.03f, -tall * 0.4f), bc + Offset(-w * 0.07f, h * 0.035f), 1.5f * dp, alpha = v[2])
    }
    // the man praying by his house, its window lit
    if (v[3] > 0f) {
        val hs = Offset(w * 0.16f, h * 0.84f)
        drawRect(HeiC26.Ink, Offset(hs.x - w * 0.05f, hs.y - h * 0.12f), Size(w * 0.1f, h * 0.12f), alpha = v[3])
        drawArc(HeiC26.Ink, 180f, 180f, true, Offset(hs.x - w * 0.035f, hs.y - h * 0.17f), Size(w * 0.07f, h * 0.1f), alpha = v[3])
        drawRect(HeiC26.Window, Offset(hs.x - w * 0.012f, hs.y - h * 0.09f), Size(w * 0.024f, h * 0.035f), alpha = v[3] * (0.8f + 0.2f * sin(t * 2f)))
        w8Worshipper(Offset(w * 0.07f, h * 0.85f), tall, HeiC26.Ink, 0f, bump(fract(t * 0.18f)).let { if (it > 0.6f) 1f else it / 0.6f }, v[3])
    }
    // with them wherever they are: the verse over the land
    fitBig26(a.big("withall"), Offset(w * 0.5f, h * 0.47f), w * 0.7f, HeiC26.Gold, easeOut(seg(wa, 0.05f, 0.3f)) * (1f - easeOut(seg(mw, 0f, 0.3f))), glow = HeiC26.Gold)
    // not mixed with what He created
    struckTag21(a.tag("notmixed"), Offset(w * 0.5f, h * 0.61f), easeOut(seg(nm, 0f, 0.25f)) * (1f - seg(mw, 0f, 0.25f)), seg(nm, 0.35f, 0.95f), HeiC26.Red, back = HeiC26.Back, ink = ArtInk.Ivory)
    // the card's example: the moon in the sky, and with the traveller
    if (mw > 0f) {
        val rise = easeOut(seg(mw, 0f, 0.3f))
        val moon = Offset(trav.x - w * 0.02f, mix(h * 0.6f, h * 0.36f, rise))
        softGlow(moon, w * 0.11f, HeiC26.Moon, 0.45f * rise)
        drawCircle(HeiC26.Moon, w * 0.035f, moon, alpha = rise)
        drawCircle(Color(0xFFE2DAC0), w * 0.008f, moon + Offset(-w * 0.01f, -w * 0.006f), alpha = 0.6f * rise)
        drawCircle(Color(0xFFE2DAC0), w * 0.006f, moon + Offset(w * 0.012f, w * 0.01f), alpha = 0.6f * rise)
        // its light on the traveller's road
        drawOval(HeiC26.Moon, Offset(trav.x - w * 0.08f, trav.y - h * 0.01f), Size(w * 0.16f, h * 0.025f), alpha = 0.12f * rise)
        val tg = seg(mw, 0.3f, 0.5f)
        tagAt(a.tag("moonwalk", 0, 0), moon + Offset(-w * 0.13f, 0f), tg, back = HeiC26.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("moonwalk", 0, 1), trav + Offset(0f, -tall * 1.45f), tg, back = HeiC26.Back, ink = ArtInk.Ivory)
        fitBig26(a.big("moonwalk", 0, 2), Offset(w * 0.72f, h * 0.27f), w * 0.34f, HeiC26.Gold, seg(mw, 0.6f, 0.85f), glow = HeiC26.Gold)
    }
}

// ═════════════ 2. revelation: brought down, then recited, memorised and written (quran-kalam) ═════════════

private object RevC26 {
    val SkyTop = Color(0xFF1C2850)
    val SkyLow = Color(0xFFE8B48A)
    val Mount = Color(0xFF3A2A2E)
    val MountFar = Color(0xFF6A4A4E)
    val Wall = Color(0xFFE9D9B8)
    val WallLow = Color(0xFFD6BF92)
    val Arch = Color(0xFFB88A52)
    val Carpet = Color(0xFF8A2E2E)
    val Ink = Color(0xFF2A2030)
    val Wood = Color(0xFF7A5230)
    val Page = Color(0xFFFBF4E0)
    val Rule = Color(0xFFB8A27A)
    val Thread = Color(0xFFF2C45A)
    val Back = Color(0xE6201810)
}

internal fun archPath26(cx: Float, top: Float, bottom: Float, hw: Float): Path = Path().apply {
    moveTo(cx - hw, bottom)
    lineTo(cx - hw, top + hw * 0.9f)
    quadraticTo(cx - hw, top, cx, top - hw * 0.25f)
    quadraticTo(cx + hw, top, cx + hw, top + hw * 0.9f)
    lineTo(cx + hw, bottom)
    close()
}

/**
 * Above, dawn over the mountains; below, a mosque's arcade. Beats: sendown (the angel's cool light comes down through
 * the heavens to the cave, where his ﷺ light shines, named), reciters (under the first arch a man recites, the sound
 * going out to the one who listens), memorize (under the second a child with his wooden board; a warmth gathers at
 * his chest), mushaf (under the third the open Mushaf on its stand, ﴿فِي كِتَابٍ مَكْنُونٍ﴾). A gold thread runs from
 * his light to each arch: one and the same Qur'an.
 */
internal fun DrawScope.revelationScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sendown" to Shot(Offset(w * 0.72f, h * 0.32f), 1.25f),
        "reciters" to Shot(Offset(w * 0.8f, h * 0.8f), 1.55f),
        "memorize" to Shot(Offset(w * 0.5f, h * 0.8f), 1.6f),
        "mushaf" to Shot(Offset(w * 0.2f, h * 0.78f), 1.55f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { revelationDawn(a) }
}

private fun DrawScope.revelationDawn(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sd = a.b("sendown")
    val rc = a.b("reciters")
    val mz = a.b("memorize")
    val ms = a.b("mushaf")
    val ground = h * 0.56f
    drawRect(Brush.verticalGradient(listOf(RevC26.SkyTop, RevC26.SkyLow), endY = ground), size = Size(w, ground))
    for (k in 0 until 4) {
        val rx = w * (0.7f + 0.06f * k)
        val ry = h * (0.42f + 0.05f * k)
        drawArc(Color.White, 205f, 130f, false, Offset(w * 0.5f - rx, h * 0.6f - ry), Size(rx * 2f, ry * 2f), alpha = 0.16f - 0.03f * k, style = Stroke(1.2f * dp))
    }
    // the mountains, the cave
    drawPath(Path().apply {
        moveTo(0f, ground); lineTo(w * 0.1f, h * 0.44f); lineTo(w * 0.22f, h * 0.5f); lineTo(w * 0.36f, h * 0.4f); lineTo(w * 0.5f, h * 0.49f)
        lineTo(w * 0.66f, h * 0.36f); lineTo(w * 0.86f, h * 0.42f); lineTo(w, h * 0.38f); lineTo(w, ground); close()
    }, RevC26.MountFar)
    drawPath(Path().apply {
        moveTo(w * 0.55f, ground); lineTo(w * 0.7f, h * 0.44f); lineTo(w * 0.8f, h * 0.41f); lineTo(w * 0.95f, h * 0.47f); lineTo(w, h * 0.5f); lineTo(w, ground); close()
    }, RevC26.Mount)
    val cave = Offset(w * 0.8f, h * 0.5f)
    drawArc(Color(0xFF140C10), 180f, 180f, true, Offset(cave.x - w * 0.03f, cave.y - h * 0.04f), Size(w * 0.06f, h * 0.08f))
    // the angel's light comes down
    val down = easeInOut(seg(sd, 0.05f, 0.6f))
    if (sd > 0f) {
        val from = Offset(w * 0.6f, -h * 0.04f)
        val to = cave + Offset(-w * 0.04f, -h * 0.07f)
        val ctrl = Offset(w * 0.56f, h * 0.3f)
        val trail = Path()
        for (k in 0..24) {
            val u = k / 24f * down
            val p = (from * ((1 - u) * (1 - u))) + (ctrl * (2 * u * (1 - u))) + (to * (u * u))
            if (k == 0) trail.moveTo(p.x, p.y) else trail.lineTo(p.x, p.y)
        }
        drawPath(trail, Color(0xFFDDEBFF), alpha = 0.55f, style = Stroke(2f * dp, cap = StrokeCap.Round))
        val u = down
        val head = (from * ((1 - u) * (1 - u))) + (ctrl * (2 * u * (1 - u))) + (to * (u * u))
        angelLight26(head, w * 0.022f, t, easeOut(seg(sd, 0f, 0.15f)))
    }
    // his ﷺ light in the cave, named
    sirajMunir(cave + Offset(0f, -h * 0.01f), w * 0.011f, easeOut(seg(sd, 0.5f, 0.85f)), t, nameAt = cave + Offset(-w * 0.15f, -h * 0.03f))
    // the arcade below
    drawRect(Brush.verticalGradient(listOf(RevC26.Wall, RevC26.WallLow), startY = ground, endY = h), Offset(0f, ground), Size(w, h - ground))
    drawRect(RevC26.Arch, Offset(0f, ground), Size(w, h * 0.012f))
    val xs = listOf(w * 0.8f, w * 0.5f, w * 0.2f)
    for (x in xs) {
        val arch = archPath26(x, h * 0.64f, h * 0.97f, w * 0.12f)
        drawPath(arch, Color(0xFFF6EBD2))
        drawPath(arch, RevC26.Arch, style = Stroke(3f * dp))
    }
    drawRect(RevC26.Carpet, Offset(0f, h * 0.95f), Size(w, h * 0.05f))
    // the gold thread: from his light down to each arch in turn
    val th = listOf(easeOut(seg(rc, 0f, 0.4f)), easeOut(seg(mz, 0f, 0.4f)), easeOut(seg(ms, 0f, 0.4f)))
    if (th[0] > 0f) {
        val from = cave + Offset(0f, h * 0.03f)
        drawLine(RevC26.Thread, from, from + Offset(0f, (h * 0.66f - from.y) * th[0]), 1.6f * dp, StrokeCap.Round, alpha = 0.9f)
        drawLine(RevC26.Thread, Offset(xs[0], h * 0.66f), Offset(mix(xs[0], xs[1], th[1]), h * 0.66f), 1.6f * dp, StrokeCap.Round, alpha = 0.9f * th[1].coerceAtLeast(0f))
        if (th[2] > 0f) drawLine(RevC26.Thread, Offset(xs[1], h * 0.66f), Offset(mix(xs[1], xs[2], th[2]), h * 0.66f), 1.6f * dp, StrokeCap.Round, alpha = 0.9f)
        for ((k, f) in th.withIndex()) if (f > 0.95f) softGlow(Offset(xs[k], h * 0.66f), w * 0.03f, RevC26.Thread, 0.6f)
    }
    // 1. recited by the tongue, heard by the ear
    if (rc > 0f) {
        val o = easeOut(seg(rc, 0.1f, 0.4f))
        val seat = Offset(w * 0.86f, h * 0.94f)
        w8Seated(seat, h * 0.2f, RevC26.Ink, -1f, o, speak = 0.5f + 0.5f * sin(t * 6f))
        w8Seated(Offset(w * 0.72f, h * 0.94f), h * 0.18f, RevC26.Ink, 1f, o)
        val mouth = seat + Offset(-w * 0.03f, -h * 0.17f)
        for (k in 0 until 3) {
            val f = fract(t * 0.7f + k / 3f)
            val r = w * (0.015f + 0.06f * f)
            drawArc(RevC26.Thread, 150f, 60f, false, Offset(mouth.x - r, mouth.y - r), Size(r * 2f, r * 2f), alpha = o * (1f - f), style = Stroke(1.6f * dp, cap = StrokeCap.Round))
        }
    }
    // 2. kept in the breast: a child with his board
    if (mz > 0f) {
        val o = easeOut(seg(mz, 0.1f, 0.4f))
        val seat = Offset(w * 0.53f, h * 0.94f)
        val tall = h * 0.15f
        w8Seated(seat, tall, RevC26.Ink, -1f, o)
        val board = seat + Offset(-w * 0.055f, -tall * 0.55f)
        drawRoundRect(RevC26.Wood, Offset(board.x - w * 0.025f, board.y - h * 0.06f), Size(w * 0.05f, h * 0.085f), CornerRadius(w * 0.006f), alpha = o)
        drawRoundRect(Color(0xFFE9D3A8), Offset(board.x - w * 0.019f, board.y - h * 0.05f), Size(w * 0.038f, h * 0.065f), CornerRadius(w * 0.004f), alpha = o)
        for (r in 0 until 4) drawLine(RevC26.Rule, Offset(board.x - w * 0.014f, board.y - h * 0.04f + r * h * 0.014f), Offset(board.x + w * 0.014f, board.y - h * 0.04f + r * h * 0.014f), 1f * dp, alpha = o)
        val chest = seat + Offset(-w * 0.005f, -tall * 0.27f)
        val keep = easeOut(seg(mz, 0.35f, 1f))
        for (k in 0 until 4) {
            val f = fract(t * 0.6f + k / 4f)
            drawCircle(RevC26.Thread, w * 0.004f, board + (chest - board) * f + Offset(0f, -h * 0.02f * bump(f)), alpha = o * 0.9f * bump(f))
        }
        softGlow(chest, w * 0.045f, RevC26.Thread, 0.7f * keep * (0.85f + 0.15f * sin(t * 2.5f)))
    }
    // 3. written in the copies: the open Mushaf on its stand
    if (ms > 0f) {
        val o = easeOut(seg(ms, 0f, 0.35f))
        val c = Offset(w * 0.2f, h * 0.84f)
        drawLine(RevC26.Wood, c + Offset(-w * 0.06f, h * 0.1f), c + Offset(w * 0.05f, -h * 0.01f), 3f * dp, alpha = o)
        drawLine(RevC26.Wood, c + Offset(w * 0.06f, h * 0.1f), c + Offset(-w * 0.05f, -h * 0.01f), 3f * dp, alpha = o)
        openBook21(c + Offset(0f, -h * 0.03f), w * 0.15f, RevC26.Page, RevC26.Rule, o, glow = 0.6f * easeOut(seg(ms, 0.3f, 0.8f)))
        fitBig26(a.big("mushaf"), Offset(w * 0.2f, h * 0.71f), w * 0.2f, Color(0xFF6A4418), seg(ms, 0.4f, 0.75f))
    }
}

// ═════════════ 3. the bright faces, and the veiled (ruya) ═════════════

private object GazeC26 {
    val GardenTop = Color(0xFFF6E7C0)
    val GardenLow = Color(0xFFCFE2B8)
    val Grass = Color(0xFF6A9A5A)
    val River = Color(0xFF6AA8D8)
    val Leaf = Color(0xFF3E7A4A)
    val Trunk = Color(0xFF7A5A3A)
    val Ink = Color(0xFF2A2430)
    val Gold = Color(0xFF8A5A10)
    val Gloom = Color(0xFF2A2834)
    val GloomLow = Color(0xFF3E3A44)
    val Curtain = Color(0xFF1A161E)
    val Fold = Color(0xFF2E2834)
    val Pale = Color(0xFFD8D2DE)
    val Back = Color(0xE6201810)
}

/**
 * Split in two. Beats: lookup (on the right, a garden by a river: the believers stand looking up, their faces bright;
 * above them nothing is drawn but the verse ﴿وُجُوهٌ يَوْمَئِذٍ نَاضِرَةٌ ۝ إِلَى رَبِّهَا نَاظِرَةٌ﴾), veiled (on the left, in
 * the gloom, a heavy curtain comes down before the others, their heads bowed; the verse ﴿كَلَّا إِنَّهُمْ عَنْ رَبِّهِمْ
 * يَوْمَئِذٍ لَمَحْجُوبُونَ﴾ on it), infer (wide: «فِي حَالِ السَّخَطِ» under the veiled, «فِي حَالِ الرِّضَى» under the believers,
 * an arrow from the one to the other).
 */
internal fun DrawScope.gazeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "lookup" to Shot(Offset(w * 0.76f, h * 0.42f), 1.35f),
        "veiled" to Shot(Offset(w * 0.24f, h * 0.42f), 1.35f),
        "infer" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { gazeTwoSides(a) }
}

private fun DrawScope.gazeTwoSides(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val lu = a.b("lookup")
    val ve = a.b("veiled")
    val inf = a.b("infer")
    val ground = h * 0.84f
    // the garden (right)
    drawRect(Brush.verticalGradient(listOf(GazeC26.GardenTop, GazeC26.GardenLow), endY = ground), Offset(w * 0.5f, 0f), Size(w * 0.5f, ground))
    drawRect(GazeC26.Grass, Offset(w * 0.5f, ground), Size(w * 0.5f, h - ground))
    drawRect(GazeC26.River, Offset(w * 0.5f, h * 0.9f), Size(w * 0.5f, h * 0.06f))
    for (k in 0 until 6) {
        val x = w * (0.52f + 0.08f * k) + fract(t * 0.05f + k * 0.3f) * w * 0.06f
        drawLine(Color.White, Offset(x, h * 0.93f), Offset(x + w * 0.025f, h * 0.93f), 1.2f * dp, alpha = 0.6f)
    }
    w8Palm(Offset(w * 0.95f, ground + h * 0.02f), h * 0.42f, GazeC26.Trunk, GazeC26.Leaf, t)
    w8Palm(Offset(w * 0.55f, ground + h * 0.02f), h * 0.34f, GazeC26.Trunk, GazeC26.Leaf, t + 1f)
    // the gloom (left)
    drawRect(Brush.verticalGradient(listOf(GazeC26.Gloom, GazeC26.GloomLow), endY = ground), Offset(0f, 0f), Size(w * 0.5f, ground))
    drawRect(Color(0xFF242028), Offset(0f, ground), Size(w * 0.5f, h - ground))
    drawRect(Brush.horizontalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.25f), Color.Transparent), startX = w * 0.47f, endX = w * 0.53f), Offset(w * 0.47f, 0f), Size(w * 0.06f, h))
    // the believers, looking up, their faces bright
    val bel = easeOut(seg(lu, 0f, 0.3f)).coerceAtLeast(if (ve > 0f || inf > 0f) 1f else 0f)
    val tall = h * 0.2f
    for (k in 0 until 5) {
        val foot = Offset(w * (0.62f + 0.06f * k), ground - h * 0.01f * (k % 2))
        val f = easeOut(seg(lu, k * 0.06f, 0.3f + k * 0.06f)).coerceAtLeast(if (ve > 0f) 1f else 0f) * bel
        val head = foot + Offset(tall * 0.07f, -tall * 0.92f)
        softGlow(head, w * 0.03f, Color(0xFFFFF4D0), 0.8f * f * easeOut(seg(lu, 0.3f, 0.8f)).coerceAtLeast(if (ve > 0f) 1f else 0f))
        figure(foot, tall * (0.92f + 0.04f * (k % 3)), GazeC26.Ink, f, lean = -0.12f)
    }
    fitBig26(a.big("lookup", 0, 0), Offset(w * 0.72f, h * 0.2f), w * 0.36f, GazeC26.Gold, seg(lu, 0.3f, 0.6f), glow = Color(0xFFFFF0B0))
    fitBig26(a.big("lookup", 0, 1), Offset(w * 0.72f, h * 0.32f), w * 0.36f, GazeC26.Gold, seg(lu, 0.45f, 0.75f), glow = Color(0xFFFFF0B0))
    // the veiled: heads bowed, a heavy curtain comes down before them
    val vf = easeOut(seg(ve, 0f, 0.25f)).coerceAtLeast(if (inf > 0f) 1f else 0f)
    for (k in 0 until 4) figure(Offset(w * (0.1f + 0.08f * k), ground - h * 0.01f * (k % 2)), tall * 0.95f, Color(0xFF0E0C12), vf, lean = 0.3f)
    val drop = easeInOut(seg(ve, 0.15f, 0.6f)).coerceAtLeast(if (inf > 0f) 1f else 0f)
    if (drop > 0f) {
        val bottom = h * 0.5f * drop
        drawRect(GazeC26.Curtain, Offset(w * 0.02f, 0f), Size(w * 0.45f, bottom))
        for (k in 0 until 9) drawLine(GazeC26.Fold, Offset(w * (0.04f + 0.05f * k), 0f), Offset(w * (0.04f + 0.05f * k) + sin(t * 0.8f + k) * w * 0.003f, bottom), 2.5f * dp)
        drawRect(Color(0xFF4A3A2A), Offset(w * 0.02f, bottom - h * 0.012f), Size(w * 0.45f, h * 0.012f))
        fitBig26(a.big("veiled", 0, 0), Offset(w * 0.27f, bottom - h * 0.3f), w * 0.36f, GazeC26.Pale, seg(ve, 0.5f, 0.75f))
        fitBig26(a.big("veiled", 0, 1), Offset(w * 0.27f, bottom - h * 0.16f), w * 0.36f, GazeC26.Pale, seg(ve, 0.6f, 0.85f))
    }
    // the inference: veiled in His wrath, so seen in His pleasure
    if (inf > 0f) {
        val o = easeOut(seg(inf, 0.1f, 0.4f))
        tagAt(a.tag("infer", 0, 1), Offset(w * 0.24f, h * 0.92f), o, back = GazeC26.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("infer", 0, 0), Offset(w * 0.76f, h * 0.76f), seg(inf, 0.45f, 0.75f), back = GazeC26.Back, ink = ArtInk.BrassLight)
        val ar = easeOut(seg(inf, 0.3f, 0.7f))
        val from = Offset(w * 0.37f, h * 0.92f)
        val to = Offset(w * 0.37f + w * 0.26f * ar, h * 0.92f - h * 0.14f * ar)
        drawLine(ArtInk.Gold, from, to, 2f * dp, StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp)))
        if (ar > 0.2f) arrowHead(to, Offset(0.88f, -0.47f), w * 0.022f, ArtInk.Gold, 2f * dp)
    }
}

// ═════════════ 4. the trumpet, the rising, the books, the scale, the bridge (akhira) ═════════════

private object ResC26 {
    val SkyTop = Color(0xFF8A86A0)
    val SkyLow = Color(0xFFD8CFC4)
    val Ground = Color(0xFF9A8A70)
    val GroundLow = Color(0xFF6A5C4A)
    val Mound = Color(0xFF7A6A52)
    val Stone = Color(0xFF5A5048)
    val Horn = Color(0xFFEDE3CC)
    val HornEdge = Color(0xFFA89878)
    val Ink = Color(0xFF221C28)
    val Page = Color(0xFFF6EED8)
    val Dark = Color(0xFF3A3038)
    val Brass = Color(0xFFB88A3A)
    val Fire = Color(0xFFE8602A)
    val FireDeep = Color(0xFF7A1E10)
    val Back = Color(0xE6201810)
}

private fun graveX26(k: Int): Float = 0.7f + 0.045f * k

/**
 * One wide land read from right to left. Beats: horn (the trumpet at the top, a cool white light at its mouth, rings of
 * sound spreading over the graves), arise (people rise from every grave), scrollsfly (two books come down: one taken
 * in the right hand, glowing, «بِيَمِينِهِ», one behind the back, dark, «وَرَاءَ ظَهْرِهِ»), weigh (the scale with its two
 * pans and its tongue, «الْمِيزَانُ»: one pan sinks with light), sirat (a hair-thin bridge over the fire: walkers cross,
 * one slips).
 */
internal fun DrawScope.resurrectionScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "horn" to Shot(Offset(w * 0.84f, h * 0.3f), 1.35f),
        "arise" to Shot(Offset(w * 0.82f, h * 0.72f), 1.4f),
        "scrollsfly" to Shot(Offset(w * 0.6f, h * 0.62f), 1.5f),
        "weigh" to Shot(Offset(w * 0.37f, h * 0.5f), 1.4f),
        "sirat" to Shot(Offset(w * 0.15f, h * 0.62f), 1.5f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { resurrectionLand(a) }
}

private fun DrawScope.resurrectionLand(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val hn = a.b("horn")
    val ar = a.b("arise")
    val sf = a.b("scrollsfly")
    val wg = a.b("weigh")
    val sr = a.b("sirat")
    val ground = h * 0.66f
    drawRect(Brush.verticalGradient(listOf(ResC26.SkyTop, ResC26.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(Brush.verticalGradient(listOf(ResC26.Ground, ResC26.GroundLow), startY = ground, endY = h), Offset(0f, ground), Size(w, h - ground))
    // the graves
    for (k in 0 until 6) {
        val c = Offset(w * graveX26(k), h * (0.8f + 0.04f * (k % 2)))
        drawArc(ResC26.Mound, 180f, 180f, true, Offset(c.x - w * 0.02f, c.y - h * 0.025f), Size(w * 0.04f, h * 0.05f))
        drawRect(ResC26.Stone, Offset(c.x + w * 0.012f, c.y - h * 0.05f), Size(w * 0.006f, h * 0.05f))
    }
    // the trumpet and the angel's light at its mouth
    val horn = easeOut(seg(hn, 0f, 0.3f))
    if (horn > 0f) {
        val mouth = Offset(w * 0.95f, h * 0.08f)
        val bell = Offset(w * 0.78f, h * 0.26f)
        drawPath(Path().apply {
            moveTo(mouth.x, mouth.y - h * 0.008f)
            quadraticTo(w * 0.88f, h * 0.1f, bell.x + w * 0.02f, bell.y - h * 0.05f)
            lineTo(bell.x - w * 0.03f, bell.y + h * 0.02f)
            quadraticTo(w * 0.89f, h * 0.15f, mouth.x, mouth.y + h * 0.008f)
            close()
        }, ResC26.Horn, alpha = horn)
        drawLine(ResC26.HornEdge, bell + Offset(w * 0.02f, -h * 0.05f), bell + Offset(-w * 0.03f, h * 0.02f), 2f * dp, alpha = horn)
        angelLight26(mouth, w * 0.02f, t, horn)
        val blow = seg(hn, 0.25f, 1f).coerceAtLeast(if (ar > 0f) 1f else 0f)
        if (blow > 0f) for (k in 0 until 4) {
            val f = fract(t * 0.45f + k / 4f)
            val r = w * (0.04f + 0.3f * f)
            drawArc(Color.White, 100f, 90f, false, Offset(bell.x - r, bell.y - r), Size(r * 2f, r * 2f), alpha = 0.5f * (1f - f) * min(1f, blow * 2f) * (1f - seg(sf, 0f, 0.4f)), style = Stroke(1.6f * dp))
        }
        tagAt(a.tag("horn"), Offset(w * 0.77f, h * 0.12f), seg(hn, 0.3f, 0.6f) * (1f - seg(ar, 0.5f, 1f)), back = ResC26.Back, ink = ArtInk.Ivory)
    }
    // people rise from the graves
    val tall = h * 0.15f
    for (k in 0 until 6) {
        val up = easeOut(seg(ar, 0.08f * k, 0.08f * k + 0.45f))
        if (up <= 0f) continue
        val base = Offset(w * graveX26(k) - w * 0.005f, h * (0.8f + 0.04f * (k % 2)) - h * 0.02f)
        clipPath(Path().apply { addRect(Rect(0f, 0f, w, base.y + 1f)) }) {
            figure(base + Offset(0f, tall * (1f - up)), tall, ResC26.Ink, min(1f, up * 2f))
        }
    }
    // the books come down: in the right hand, and behind the back
    if (sf > 0f) {
        for (k in 0 until 2) {
            val foot = Offset(w * (0.62f - 0.1f * k), h * 0.86f)
            val o = easeOut(seg(sf, 0f, 0.25f))
            figure(foot, tall * 1.1f, ResC26.Ink, o, arms = if (k == 0) 0.8f else -0.6f)
            val land = easeInOut(seg(sf, 0.15f + 0.25f * k, 0.55f + 0.25f * k))
            val hand = if (k == 0) foot + Offset(-tall * 0.42f, -tall * 0.75f) else foot + Offset(tall * 0.3f, -tall * 0.55f)
            val from = Offset(foot.x + w * 0.04f, -h * 0.05f)
            val p = from + (hand - from) * land
            withTransform({ rotate(360f * (1f - land), p) }) {
                drawRoundRect(if (k == 0) ResC26.Page else ResC26.Dark, Offset(p.x - w * 0.015f, p.y - h * 0.02f), Size(w * 0.03f, h * 0.04f), CornerRadius(w * 0.004f))
                drawRect(if (k == 0) ResC26.Brass else Color(0xFF1A161C), Offset(p.x - w * 0.015f, p.y - h * 0.02f), Size(w * 0.03f, h * 0.008f))
            }
            if (land >= 1f) {
                if (k == 0) softGlow(hand, w * 0.04f, ArtInk.Gold, 0.6f)
                tagAt(a.tag("scrollsfly", 0, k), foot + Offset(if (k == 0) w * 0.05f else -w * 0.05f, -tall * 1.5f - h * 0.09f * k), seg(sf, 0.55f + 0.25f * k, 0.7f + 0.25f * k) * (1f - seg(wg, 0f, 0.3f)), back = ResC26.Back, ink = if (k == 0) ArtInk.BrassLight else ArtInk.Ivory)
            }
        }
    }
    // the scale with its two pans and its tongue
    if (wg > 0f) {
        val o = easeOut(seg(wg, 0f, 0.25f))
        val top = Offset(w * 0.37f, h * 0.3f)
        val base = Offset(w * 0.37f, h * 0.76f)
        drawLine(ResC26.Brass, base, top, 4f * dp, alpha = o)
        drawLine(ResC26.Brass, base + Offset(-w * 0.05f, 0f), base + Offset(w * 0.05f, 0f), 4f * dp, StrokeCap.Round, alpha = o)
        val settle = easeOut(seg(wg, 0.35f, 0.9f))
        val tilt = (sin(t * 3f) * 6f * (1f - settle)) + 12f * settle
        val beamHalf = w * 0.14f
        val rad = tilt * PI.toFloat() / 180f
        val right = top + Offset(cos(rad) * beamHalf, sin(rad) * beamHalf)
        val left = top + Offset(-cos(rad) * beamHalf, -sin(rad) * beamHalf)
        drawLine(ResC26.Brass, left, right, 3f * dp, StrokeCap.Round, alpha = o)
        drawLine(ResC26.Brass, top, top + Offset(sin(rad) * h * 0.06f, -cos(rad) * h * 0.06f), 2.5f * dp, StrokeCap.Round, alpha = o)
        for ((k, end) in listOf(right, left).withIndex()) {
            val pan = end + Offset(0f, h * 0.16f)
            drawLine(ResC26.Brass, end, pan + Offset(-w * 0.04f, 0f), 1f * dp, alpha = o)
            drawLine(ResC26.Brass, end, pan + Offset(w * 0.04f, 0f), 1f * dp, alpha = o)
            drawArc(ResC26.Brass, 0f, 180f, true, Offset(pan.x - w * 0.045f, pan.y - h * 0.02f), Size(w * 0.09f, h * 0.05f), alpha = o)
            if (k == 0) {
                for (m in 0 until 3) drawCircle(ArtInk.Gold, w * 0.008f, pan + Offset(w * (-0.015f + 0.015f * m), -h * 0.005f), alpha = o * settle)
                softGlow(pan, w * 0.05f, ArtInk.Gold, 0.5f * settle)
            } else {
                drawCircle(Color(0xFF5A5060), w * 0.007f, pan + Offset(0f, -h * 0.004f), alpha = o * settle)
            }
        }
        tagAt(a.tag("weigh"), top + Offset(0f, -h * 0.11f), seg(wg, 0.2f, 0.45f), back = ResC26.Back, ink = ArtInk.BrassLight)
    }
    // the bridge over the fire
    if (sr > 0f) {
        val o = easeOut(seg(sr, 0f, 0.25f))
        val y = h * 0.6f
        drawRect(Brush.verticalGradient(listOf(ResC26.Fire, ResC26.FireDeep), startY = h * 0.7f, endY = h), Offset(w * 0.02f, h * 0.7f), Size(w * 0.26f, h * 0.3f), alpha = o)
        for (k in 0 until 7) {
            val x = w * (0.04f + 0.035f * k)
            val fl = h * (0.05f + 0.03f * sin(t * 6f + k * 1.3f))
            drawPath(Path().apply { moveTo(x - w * 0.012f, h * 0.72f); quadraticTo(x, h * 0.72f - fl * 2f, x + w * 0.012f, h * 0.72f); close() }, Color(0xFFFFB04A), alpha = 0.8f * o)
        }
        drawLine(Color.White, Offset(w * 0.0f, y), Offset(w * 0.3f, y), 1f * dp, alpha = o)
        softGlow(Offset(w * 0.15f, y), w * 0.12f, Color.White, 0.15f * o)
        val walk = seg(sr, 0.2f, 1f)
        for (k in 0 until 3) {
            val prog = (walk * 1.2f - k * 0.18f).coerceIn(0f, 1f)
            val x = mix(w * 0.29f, w * 0.02f, prog)
            if (k == 1 && prog > 0.4f) {
                // he slips and falls
                val fall = easeOut(seg(prog, 0.4f, 0.9f))
                val p = Offset(mix(w * 0.29f, w * 0.02f, 0.4f), y + h * 0.3f * fall)
                withTransform({ rotate(80f * fall, p) }) { figure(p, h * 0.1f, ResC26.Ink, o * (1f - fall)) }
            } else {
                figure(Offset(x, y), h * 0.1f, ResC26.Ink, o * (if (prog >= 1f) 0f else 1f), walk = if (prog in 0.01f..0.99f) t * 6f + k else 0f)
            }
        }
        tagAt(a.tag("sirat"), Offset(w * 0.15f, h * 0.45f), seg(sr, 0.2f, 0.45f), back = ResC26.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 5. the names on a tiled wall, and the three ways of turning from them (ilhad-asma) ═════════════

private object NamC26 {
    val Tile1 = Color(0xFF1E5A6A)
    val Tile2 = Color(0xFFE9E2D0)
    val Tile3 = Color(0xFF2E7A5A)
    val Plaster = Color(0xFFF2E8D2)
    val Plaque = Color(0xFFFBF6EA)
    val Rim = Color(0xFFC9A04A)
    val Ink = Color(0xFF1E3A30)
    val Yard = Color(0xFFD9C6A0)
    val YardLow = Color(0xFFB8A27A)
    val Idol = Color(0xFF7A6A5A)
    val Red = Color(0xFFC0463A)
    val Fig = Color(0xFF3A3040)
    val Back = Color(0xE6201810)
}

private fun plaqueX26(k: Int): Float = 0.84f - 0.22f * k

private fun DrawScope.star8_26(c: Offset, r: Float, color: Color) {
    val p = Path()
    for (k in 0 until 16) {
        val ang = k * PI.toFloat() / 8f
        val rr = if (k % 2 == 0) r else r * 0.62f
        val q = c + Offset(cos(ang) * rr, sin(ang) * rr)
        if (k == 0) p.moveTo(q.x, q.y) else p.lineTo(q.x, q.y)
    }
    p.close()
    drawPath(p, color)
}

/**
 * A tiled wall in daylight. Beats: husna (four plaques, «الْإِلَهُ» «الْعَزِيزُ» «السَّمِيعُ» «الْبَصِيرُ», under the verse
 * ﴿وَلِلَّهِ الْأَسْمَاءُ الْحُسْنَى فَادْعُوهُ بِهَا﴾), idolnames (from the first two, a line bends away to two carved stones:
 * «اللَّاتُ», «الْعُزَّى»), tashbih (under the last two, a figure with an equals sign to the plaque; the sign is struck,
 * ﴿لَيْسَ كَمِثْلِهِ شَيْءٌ﴾), emptied (the gold drains from every name and leaves an empty outline: «نَفَوْا مَعَانِيَهَا»).
 */
internal fun DrawScope.nameswallScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "husna" to Shot(Offset(w * 0.5f, h * 0.32f), 1.08f),
        "idolnames" to Shot(Offset(w * 0.74f, h * 0.62f), 1.35f),
        "tashbih" to Shot(Offset(w * 0.27f, h * 0.62f), 1.35f),
        "emptied" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { nameswallYard(a) }
}

private fun DrawScope.nameswallYard(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val hu = a.b("husna")
    val idn = a.b("idolnames")
    val ts = a.b("tashbih")
    val em = a.b("emptied")
    // the tiled wall
    drawRect(NamC26.Tile2, size = Size(w, h * 0.6f))
    val cell = w * 0.08f
    var row = 0
    var y = cell * 0.5f
    while (y < h * 0.62f) {
        var x = if (row % 2 == 0) cell * 0.5f else 0f
        while (x < w + cell) {
            star8_26(Offset(x, y), cell * 0.42f, if ((row + (x / cell).toInt()) % 2 == 0) NamC26.Tile1 else NamC26.Tile3)
            x += cell
        }
        y += cell * 0.5f
        row++
    }
    drawRect(NamC26.Plaster, Offset(0f, h * 0.23f), Size(w, h * 0.33f))
    drawRect(NamC26.Rim, Offset(0f, h * 0.23f), Size(w, h * 0.008f))
    drawRect(NamC26.Rim, Offset(0f, h * 0.552f), Size(w, h * 0.008f))
    // the yard below
    drawRect(Brush.verticalGradient(listOf(NamC26.Yard, NamC26.YardLow), startY = h * 0.56f, endY = h), Offset(0f, h * 0.56f), Size(w, h * 0.44f))
    // the verse over the wall
    drawRect(NamC26.Plaster, Offset(w * 0.06f, h * 0.04f), Size(w * 0.88f, h * 0.15f), alpha = 0.92f)
    fitBig26(a.big("husna", 0, 4), Offset(w * 0.5f, h * 0.115f), w * 0.82f, NamC26.Ink, easeOut(seg(hu, 0.55f, 0.9f)).coerceAtLeast(if (idn > 0f) 1f else 0f))
    // the four plaques
    val drain = easeInOut(seg(em, 0.2f, 0.85f))
    for (k in 0 until 4) {
        val c = Offset(w * plaqueX26(k), h * 0.39f)
        val o = easeOut(seg(hu, k * 0.1f, k * 0.1f + 0.35f)).coerceAtLeast(if (idn > 0f) 1f else 0f)
        if (o <= 0f) continue
        val tl = Offset(c.x - w * 0.095f, c.y - h * 0.12f)
        drawRoundRect(NamC26.Plaque, tl, Size(w * 0.19f, h * 0.24f), CornerRadius(w * 0.012f), alpha = o * (1f - 0.5f * drain))
        drawRoundRect(NamC26.Rim, tl, Size(w * 0.19f, h * 0.24f), CornerRadius(w * 0.012f), alpha = o * (1f - 0.6f * drain), style = Stroke(2f * dp))
        if (drain > 0f) drawRoundRect(NamC26.Fig, tl, Size(w * 0.19f, h * 0.24f), CornerRadius(w * 0.012f), alpha = 0.6f * drain, style = Stroke(1.4f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp))))
        fitBig26(a.big("husna", 0, k), c, w * 0.16f, lerpColor(NamC26.Ink, Color(0xFFB8AE9A), drain), o * (1f - 0.75f * drain))
    }
    // 1. the idolaters: the names bent away onto stones
    if (idn > 0f) {
        for (k in 0 until 2) {
            val f = easeInOut(seg(idn, 0.1f + 0.3f * k, 0.5f + 0.3f * k))
            val from = Offset(w * plaqueX26(k), h * 0.52f)
            val stone = Offset(w * plaqueX26(k) - w * 0.06f, h * 0.86f)
            val ctrl = Offset(from.x + w * 0.07f, h * 0.68f)
            val path = Path()
            for (m in 0..16) {
                val u = m / 16f * f
                val p = (from * ((1 - u) * (1 - u))) + (ctrl * (2 * u * (1 - u))) + (stone * (u * u))
                if (m == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
            }
            drawPath(path, NamC26.Red, style = Stroke(1.8f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp))))
            val so = easeOut(seg(idn, 0.05f + 0.3f * k, 0.25f + 0.3f * k))
            drawRoundRect(NamC26.Idol, Offset(stone.x - w * 0.035f, stone.y - h * 0.02f), Size(w * 0.07f, h * 0.1f), CornerRadius(w * 0.01f), alpha = so)
            drawRoundRect(NamC26.Idol, Offset(stone.x - w * 0.025f, stone.y - h * 0.08f), Size(w * 0.05f, h * 0.07f), CornerRadius(w * 0.015f), alpha = so)
            drawCircle(Color(0xFF6A5A4A), w * 0.022f, stone + Offset(0f, -h * 0.1f), alpha = so)
            tagAt(a.tag("idolnames", 0, k), stone + Offset(w * 0.075f, -h * 0.03f), seg(f, 0.8f, 1f), back = NamC26.Back, ink = Color(0xFFFFB0A0))
        }
    }
    // 2. those who liken: a creature set equal to the name; the sign struck
    if (ts > 0f) {
        for (k in 2 until 4) {
            val c = Offset(w * plaqueX26(k), h * 0.52f)
            val o = easeOut(seg(ts, 0.05f, 0.3f))
            val foot = Offset(c.x, h * 0.92f)
            figure(foot, h * 0.18f, NamC26.Fig, o)
            val eq = c + Offset(0f, h * 0.1f)
            for (dy in listOf(-1f, 1f)) drawLine(NamC26.Fig, eq + Offset(-w * 0.025f, dy * h * 0.012f), eq + Offset(w * 0.025f, dy * h * 0.012f), 3f * dp, StrokeCap.Round, alpha = o)
            val st = easeOut(seg(ts, 0.35f, 0.6f))
            drawLine(NamC26.Red, eq + Offset(w * 0.02f, -h * 0.035f), eq + Offset(w * 0.02f - w * 0.04f * st, -h * 0.035f + h * 0.07f * st), 3f * dp, StrokeCap.Round)
        }
        fitBig26(a.big("tashbih"), Offset(w * 0.27f, h * 0.97f - h * 0.04f), w * 0.4f, NamC26.Ink, seg(ts, 0.6f, 0.9f))
    }
    // 3. those who empty them
    tagAt(a.tag("emptied"), Offset(w * 0.5f, h * 0.6f), seg(em, 0.5f, 0.8f), back = NamC26.Back, ink = ArtInk.Ivory)
}

// ═════════════ 6. a register that takes a name only from the texts (asma-tawqif) ═════════════

private object RegC26 {
    val Wall = Color(0xFFEDE2C8)
    val WallLow = Color(0xFFDCCBA6)
    val Desk = Color(0xFF7A5232)
    val DeskTop = Color(0xFF946640)
    val Page = Color(0xFFFBF5E4)
    val Rule = Color(0xFFB8A27A)
    val Gold = Color(0xFFC9962E)
    val Seal = Color(0xFF2E8A5A)
    val Red = Color(0xFFC0463A)
    val Ink = Color(0xFF2A2430)
    val Dark = Color(0xFF3A3040)
    val Back = Color(0xE6201810)
}

/**
 * A study by day. Beats: fromtext (from the Mushaf and a volume of the Sunnah, gold threads write names into a framed
 * register, each sealed), byopinion (a man offers a slip of his own, «رَأْيٌ»; it is struck and drops), qadim (a book of
 * the theologians pushes «الْقَدِيمُ» up; a light runs over the two sources and finds nothing; it is struck and falls
 * back), awwal (from the Mushaf ﴿هُوَ الْأَوَّلُ وَالْآخِرُ﴾ rises and takes the head of the register; small arrows from the
 * rows below turn up to it).
 */
internal fun DrawScope.registerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fromtext" to Shot(Offset(w * 0.6f, h * 0.48f), 1.15f),
        "byopinion" to Shot(Offset(w * 0.22f, h * 0.55f), 1.35f),
        "qadim" to Shot(Offset(w * 0.42f, h * 0.55f), 1.1f),
        "awwal" to Shot(Offset(w * 0.45f, h * 0.3f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { registerStudy(a) }
}

private fun DrawScope.registerStudy(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ft = a.b("fromtext")
    val op = a.b("byopinion")
    val qd = a.b("qadim")
    val aw = a.b("awwal")
    drawRect(Brush.verticalGradient(listOf(RegC26.Wall, RegC26.WallLow)))
    lightShafts(Offset(w * 0.98f, -h * 0.05f), 2.3f, 0.35f, w * 0.9f, Color(0xFFFFF4D8), 0.35f, t, 5)
    drawRect(RegC26.DeskTop, Offset(0f, h * 0.74f), Size(w, h * 0.03f))
    drawRect(RegC26.Desk, Offset(0f, h * 0.77f), Size(w, h * 0.23f))
    // the two sources
    val mushaf = Offset(w * 0.86f, h * 0.6f)
    drawLine(RegC26.Desk, mushaf + Offset(-w * 0.06f, h * 0.14f), mushaf + Offset(w * 0.05f, h * 0.02f), 3f * dp)
    drawLine(RegC26.Desk, mushaf + Offset(w * 0.06f, h * 0.14f), mushaf + Offset(-w * 0.05f, h * 0.02f), 3f * dp)
    openBook21(mushaf, w * 0.16f, RegC26.Page, RegC26.Rule, 1f, glow = 0.3f + 0.4f * easeOut(seg(aw, 0f, 0.4f)))
    val sunna = Offset(w * 0.67f, h * 0.68f)
    openBook21(sunna, w * 0.14f, Color(0xFFF2EAD4), RegC26.Rule, 1f)
    tagAt(a.tag("fromtext", 0, 0), mushaf + Offset(0f, -h * 0.1f), seg(ft, 0.1f, 0.3f), back = RegC26.Back, ink = ArtInk.BrassLight)
    tagAt(a.tag("fromtext", 0, 1), sunna + Offset(0f, h * 0.15f), seg(ft, 0.2f, 0.4f), back = RegC26.Back, ink = ArtInk.BrassLight)
    // the register
    val reg = Rect(w * 0.27f, h * 0.12f, w * 0.57f, h * 0.7f)
    drawRect(RegC26.Page, reg.topLeft, reg.size)
    drawRect(RegC26.Gold, reg.topLeft, reg.size, style = Stroke(2.5f * dp))
    drawRect(RegC26.Gold, reg.topLeft + Offset(w * 0.008f, w * 0.008f), Size(reg.width - w * 0.016f, reg.height - w * 0.016f), style = Stroke(0.8f * dp))
    val rows = 7
    for (r in 0 until rows) {
        val y = reg.top + h * 0.13f + r * h * 0.062f
        val f = easeOut(seg(ft, 0.2f + r * 0.08f, 0.35f + r * 0.08f))
        if (f <= 0f) continue
        val x1 = reg.right - w * 0.03f
        val len = (reg.width - w * 0.09f) * (0.55f + 0.35f * fract(r * 0.618f))
        drawLine(RegC26.Ink, Offset(x1, y), Offset(x1 - len * f, y), 1.6f * dp, StrokeCap.Round, alpha = 0.75f)
        drawCircle(RegC26.Seal, w * 0.008f * f, Offset(reg.left + w * 0.025f, y), alpha = f)
        // the arrows of «الأول»: every row turns up to the first
        val up = easeOut(seg(aw, 0.55f + r * 0.04f, 0.75f + r * 0.04f))
        if (up > 0f) {
            val p = Offset(reg.left + w * 0.04f, y - h * 0.01f)
            drawLine(RegC26.Gold, p, p + Offset(0f, -h * 0.035f * up), 1.2f * dp, alpha = up)
            arrowHead(p + Offset(0f, -h * 0.035f * up), Offset(0f, -1f), w * 0.008f, RegC26.Gold, 1.2f * dp, up)
        }
    }
    // gold threads from the sources into the rows
    if (ft > 0f && ft < 1f) for (k in 0 until 2) {
        val src = if (k == 0) mushaf else sunna
        val f = fract(ft * 4f + k * 0.5f)
        val dst = Offset(reg.right - w * 0.03f, reg.top + h * 0.13f + ((ft * 7f).toInt().coerceAtMost(6)) * h * 0.062f)
        val p = src + (dst - src) * f + Offset(0f, -h * 0.06f * bump(f))
        drawCircle(RegC26.Gold, w * 0.006f, p, alpha = bump(f))
    }
    // the man with an opinion of his own
    if (op > 0f) {
        val o = easeOut(seg(op, 0f, 0.25f))
        val foot = Offset(w * 0.12f, h * 0.95f)
        withTransform({ scale(-1f, 1f, pivot = foot) }) { figure(foot, h * 0.24f, RegC26.Ink, o, arms = 0.9f * easeOut(seg(op, 0.1f, 0.35f))) }
        val fly = easeInOut(seg(op, 0.2f, 0.55f))
        val back = easeIn26(seg(op, 0.65f, 1f))
        val start = foot + Offset(w * 0.07f, -h * 0.17f)
        val hit = Offset(reg.left - w * 0.03f, h * 0.42f)
        val p = start + (hit - start) * fly + Offset(-w * 0.02f * back, h * 0.35f * back)
        struckTag21(a.tag("byopinion"), p, o * (1f - seg(op, 0.9f, 1f)), seg(op, 0.5f, 0.7f), RegC26.Red, back = RegC26.Back, ink = ArtInk.Ivory)
    }
    // the theologians' book and «القديم»
    if (qd > 0f) {
        val o = easeOut(seg(qd, 0f, 0.2f))
        val book = Offset(w * 0.14f, h * 0.71f)
        drawRoundRect(RegC26.Dark, Offset(book.x - w * 0.07f, book.y - h * 0.035f), Size(w * 0.14f, h * 0.05f), CornerRadius(w * 0.006f), alpha = o)
        drawRect(Color(0xFF8A7A6A), Offset(book.x - w * 0.07f, book.y - h * 0.035f), Size(w * 0.14f, h * 0.01f), alpha = o)
        tagAt(a.tag("qadim", 0, 1), book + Offset(0f, h * 0.09f), seg(qd, 0.1f, 0.25f), back = RegC26.Back, ink = ArtInk.Ivory)
        val rise = easeOut(seg(qd, 0.1f, 0.35f))
        val fall = easeIn26(seg(qd, 0.8f, 1f))
        val tile = Offset(mix(book.x, w * 0.2f, rise), mix(book.y, h * 0.38f, rise) + (book.y - h * 0.38f) * fall)
        // a light runs over both sources: nothing there
        val scan = seg(qd, 0.35f, 0.7f)
        if (scan > 0f && scan < 1f) for (src in listOf(mushaf, sunna)) {
            val x = src.x + w * 0.08f - w * 0.16f * scan
            drawLine(Color(0xFFFFF0B0), Offset(x, src.y - h * 0.03f), Offset(x, src.y + h * 0.05f), 3f * dp, alpha = 0.7f)
        }
        qmark21(mushaf + Offset(-w * 0.1f, -h * 0.15f), h * 0.06f, RegC26.Red, bump(seg(qd, 0.55f, 0.95f)))
        struckTag21(a.tag("qadim", 0, 0), tile, o * (1f - fall * 0.8f), seg(qd, 0.65f, 0.82f), RegC26.Red, back = RegC26.Back, ink = ArtInk.Ivory)
    }
    // «الأول» from the Mushaf to the head of the register
    if (aw > 0f) {
        val rise = easeInOut(seg(aw, 0.05f, 0.5f))
        val c = mushaf + (Offset((reg.left + reg.right) / 2f, reg.top + h * 0.055f) - mushaf) * rise
        fitBig26(a.big("awwal"), c, reg.width * 0.92f, Color(0xFF6A4418), easeOut(seg(aw, 0f, 0.2f)), glow = ArtInk.Gold)
    }
}

private fun easeIn26(x: Float): Float = x.coerceIn(0f, 1f).let { it * it }

// ═════════════ 7. the open library and the sealed door of the unseen (ilm-ghayb) ═════════════

private object UnsC26 {
    val Hall = Color(0xFFD8CBB0)
    val HallLow = Color(0xFFBFAE8C)
    val Floor = Color(0xFF8A7458)
    val Shelf = Color(0xFF6A4A2E)
    val Door = Color(0xFF4A3424)
    val Iron = Color(0xFF2A2A30)
    val Mist = Color(0xFFE6E8EE)
    val Ink = Color(0xFF241E2A)
    val Gold = Color(0xFF8A5A10)
    val Back = Color(0xE6201810)
    val Spines = listOf(Color(0xFF1F3A5E), Color(0xFF6A2632), Color(0xFF2E5A44), Color(0xFF5A4A1E), Color(0xFF3A2A4E))
}

/**
 * A hall. Beats: known (the book of al-Tahawi opens at the top; on the right a library lights up, a man reading at
 * his table), sealdoor (on the left a great door, bound in iron and locked, mist under it: «الْغَيْبُ»), denyknown (a man
 * turns his back on the shelves and pushes the books away: a red sign), claim (another tries the door with a key of
 * his own: it will not turn; a red sign), onlyallah (on the door the verse ﴿قُلْ لَا يَعْلَمُ مَنْ فِي السَّمَاوَاتِ
 * وَالْأَرْضِ الْغَيْبَ إِلَّا اللَّهُ﴾).
 */
internal fun DrawScope.unseenScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "known" to Shot(Offset(w * 0.62f, h * 0.36f), 1.15f),
        "sealdoor" to Shot(Offset(w * 0.24f, h * 0.5f), 1.3f),
        "denyknown" to Shot(Offset(w * 0.7f, h * 0.62f), 1.4f),
        "claim" to Shot(Offset(w * 0.3f, h * 0.62f), 1.4f),
        "onlyallah" to Shot(Offset(w * 0.26f, h * 0.42f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { unseenHall(a) }
}

private fun DrawScope.unseenHall(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val kn = a.b("known")
    val sd = a.b("sealdoor")
    val dn = a.b("denyknown")
    val cl = a.b("claim")
    val oa = a.b("onlyallah")
    val floor = h * 0.86f
    drawRect(Brush.verticalGradient(listOf(UnsC26.Hall, UnsC26.HallLow), endY = floor), size = Size(w, floor))
    drawRect(UnsC26.Floor, Offset(0f, floor), Size(w, h - floor))
    // the book of al-Tahawi at the top
    val book = Offset(w * 0.47f, h * 0.13f)
    val open = easeOut(seg(kn, 0f, 0.35f))
    if (open < 0.5f) {
        drawRoundRect(Color(0xFF5A2A20), Offset(book.x - w * 0.04f, book.y - h * 0.05f), Size(w * 0.08f, h * 0.1f), CornerRadius(w * 0.006f))
        drawRect(ArtInk.Gold, Offset(book.x - w * 0.04f, book.y - h * 0.035f), Size(w * 0.08f, h * 0.008f))
    } else openBook21(book, w * 0.16f, Color(0xFFFBF4E0), Color(0xFFB8A27A), 1f, glow = 0.4f)
    tagAt(a.tag("known"), book + Offset(0f, h * 0.1f), seg(kn, 0.2f, 0.45f), back = UnsC26.Back, ink = ArtInk.BrassLight)
    // the library on the right
    val lit = easeOut(seg(kn, 0.25f, 0.7f))
    drawRect(UnsC26.Shelf, Offset(w * 0.57f, h * 0.24f), Size(w * 0.4f, h * 0.5f))
    for (s in 0 until 4) {
        val y = h * (0.3f + 0.11f * s)
        for (b in 0 until 13) {
            val bw = w * (0.022f + 0.006f * ((b * 7 + s * 3) % 3))
            val x = w * 0.585f + b * w * 0.029f
            if (x + bw > w * 0.96f) break
            val fallen = s == 2 && b in 3..6 && dn > 0f
            if (!fallen) drawRect(UnsC26.Spines[(b + s) % 5], Offset(x, y), Size(bw, h * 0.085f))
        }
        drawRect(Color(0xFF4A321E), Offset(w * 0.57f, y + h * 0.085f), Size(w * 0.4f, h * 0.012f))
    }
    drawRect(Color.Black, Offset(w * 0.57f, h * 0.24f), Size(w * 0.4f, h * 0.5f), alpha = 0.45f * (1f - lit))
    if (lit > 0f) {
        softGlow(Offset(w * 0.77f, h * 0.45f), w * 0.25f, Color(0xFFFFF0C0), 0.35f * lit)
        val table = Offset(w * 0.84f, h * 0.84f)
        drawRect(UnsC26.Shelf, Offset(table.x - w * 0.08f, table.y - h * 0.08f), Size(w * 0.16f, h * 0.015f), alpha = lit)
        drawLine(UnsC26.Shelf, Offset(table.x - w * 0.07f, table.y - h * 0.07f), Offset(table.x - w * 0.07f, floor), 2f * dp, alpha = lit)
        drawLine(UnsC26.Shelf, Offset(table.x + w * 0.07f, table.y - h * 0.07f), Offset(table.x + w * 0.07f, floor), 2f * dp, alpha = lit)
        openBook21(table + Offset(-w * 0.02f, -h * 0.1f), w * 0.07f, Color(0xFFFBF4E0), Color(0xFFB8A27A), lit)
        w8Seated(Offset(w * 0.93f, floor - h * 0.002f), h * 0.2f, UnsC26.Ink, -1f, lit)
    }
    // the sealed door on the left
    val door = easeOut(seg(sd, 0f, 0.3f)).coerceAtLeast(if (dn > 0f || cl > 0f || oa > 0f) 1f else 0f)
    val dl = w * 0.08f
    val dr = w * 0.4f
    val dt = h * 0.24f
    val doorPath = Path().apply {
        moveTo(dl, floor); lineTo(dl, dt + (dr - dl) * 0.5f); quadraticTo(dl, dt, (dl + dr) / 2f, dt); quadraticTo(dr, dt, dr, dt + (dr - dl) * 0.5f); lineTo(dr, floor); close()
    }
    drawPath(doorPath, Color(0xFF2A2018), alpha = 0.35f)
    if (door > 0f) {
        drawPath(doorPath, UnsC26.Door, alpha = door)
        for (k in 0 until 3) drawRect(UnsC26.Iron, Offset(dl, h * (0.4f + 0.16f * k)), Size(dr - dl, h * 0.02f), alpha = door)
        drawLine(Color(0xFF2A1A10), Offset((dl + dr) / 2f, dt), Offset((dl + dr) / 2f, floor), 2f * dp, alpha = door)
        val lock = Offset((dl + dr) / 2f, h * 0.62f)
        padlock21(lock, w * 0.06f, Color(0xFF8A7A5A), 0f, door)
        for (k in 0 until 6) {
            val f = fract(t * 0.15f + k / 6f)
            drawOval(UnsC26.Mist, Offset(dl - w * 0.02f + f * (dr - dl), floor - h * 0.03f - h * 0.02f * sin(f * 6f + k)), Size(w * 0.09f, h * 0.035f), alpha = 0.35f * bump(f) * door)
        }
        tagAt(a.tag("sealdoor"), Offset((dl + dr) / 2f, h * 0.31f), seg(sd, 0.25f, 0.5f) * (1f - seg(oa, 0f, 0.3f)), back = UnsC26.Back, ink = ArtInk.Ivory)
    }
    // the one who denies what is known
    if (dn > 0f) {
        val o = easeOut(seg(dn, 0f, 0.2f))
        val foot = Offset(w * 0.68f, h * 0.9f)
        withTransform({ scale(-1f, 1f, pivot = foot) }) { figure(foot, h * 0.22f, UnsC26.Ink, o, arms = -0.9f * easeOut(seg(dn, 0.1f, 0.4f))) }
        for (k in 0 until 4) {
            val f = easeIn26(seg(dn, 0.2f + k * 0.05f, 0.6f + k * 0.05f))
            val p = Offset(w * (0.67f + 0.03f * k), h * 0.52f + (floor - h * 0.54f) * f)
            withTransform({ rotate(90f * f * (if (k % 2 == 0) 1f else -1f), p) }) { drawRect(UnsC26.Spines[k], Offset(p.x - w * 0.012f, p.y - h * 0.04f), Size(w * 0.024f, h * 0.08f), alpha = o) }
        }
        noSign11pub(foot + Offset(w * 0.0f, -h * 0.3f), w * 0.035f, easeOut(seg(dn, 0.55f, 0.8f)))
    }
    // the one who claims what is hidden
    if (cl > 0f) {
        val o = easeOut(seg(cl, 0f, 0.2f))
        val foot = Offset(w * 0.48f, h * 0.9f)
        figure(foot, h * 0.22f, UnsC26.Ink, o, arms = 0.9f)
        val lock = Offset((dl + dr) / 2f, h * 0.62f)
        val reach = easeInOut(seg(cl, 0.1f, 0.45f))
        val jiggle = if (cl in 0.45f..0.8f) sin(t * 30f) * 8f else 0f
        val kp = Offset(mix(foot.x - w * 0.06f, lock.x + w * 0.06f, reach), mix(h * 0.72f, lock.y + h * 0.01f, reach))
        key21(kp, w * 0.06f, Color(0xFFB8A27A), o, rot = jiggle)
        if (cl in 0.45f..0.8f) for (m in 0 until 4) glint(lock + Offset(w * (0.02f + 0.02f * m), -h * 0.03f * m), w * 0.01f, Color(0xFFFFE0A0), 0.6f * bump(fract(t * 3f + m * 0.25f)))
        noSign11pub(foot + Offset(0f, -h * 0.3f), w * 0.035f, easeOut(seg(cl, 0.7f, 0.9f)))
    }
    // the unseen: only Allah knows it
    if (oa > 0f) {
        val o = easeOut(seg(oa, 0.1f, 0.45f))
        softGlow(Offset((dl + dr) / 2f, h * 0.45f), w * 0.2f, Color(0xFFFFE6A8), 0.4f * o)
        fitBig26(a.big("onlyallah", 0, 0), Offset((dl + dr) / 2f, h * 0.4f), (dr - dl) * 1.05f, Color(0xFFFFF0C8), o)
        fitBig26(a.big("onlyallah", 0, 1), Offset((dl + dr) / 2f, h * 0.53f), (dr - dl) * 0.9f, Color(0xFFFFF0C8), seg(oa, 0.25f, 0.6f))
    }
}

// ═════════════ 8. ten medallions: the signs before the Hour (ashrat-saa) ═════════════

private object TenC26 {
    val Parch = Color(0xFFF2E6C8)
    val ParchLow = Color(0xFFE2D0A6)
    val Rim = Color(0xFFB8862E)
    val Well = Color(0xFFFBF6E6)
    val Ink = Color(0xFF3A2E24)
    val Smoke = Color(0xFF8A8690)
    val Fog = Color(0xFF3A2E44)
    val Beast = Color(0xFF5A4030)
    val Sun = Color(0xFFF2A23A)
    val Sky = Color(0xFFCFE0EE)
    val Sand = Color(0xFFD8B880)
    val Brick = Color(0xFFA0704A)
    val Fire = Color(0xFFE8602A)
    val Back = Color(0xE6201810)
}

private fun medal26(w: Float, h: Float, m: Int): Offset = Offset(w * (0.86f - 0.18f * (m % 5)), h * (if (m < 5) 0.3f else 0.7f))

/**
 * A parchment chart in daylight. Beats: tenring (ten empty medallions in two rows, in the order of the hadith from the
 * right), sixsigns (one after another as they are named: smoke; the dark fog of the Dajjal's trial (he is never drawn);
 * the beast; the sun rising over the western rim; a white minaret and a small warm light coming down, «عِيسَى عَلَيْهِ
 * السَّلَامُ»; a wall broken open and a flood pouring out), sinkings (three craters: in the east, in the west, in Arabia),
 * yemenfire (the last medallion: a fire, and dots driven north before it).
 */
internal fun DrawScope.tensignsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "tenring" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sixsigns" to Shot(Offset(w * 0.55f, h * 0.42f), 1.08f),
        "sinkings" to Shot(Offset(w * 0.5f, h * 0.7f), 1.3f),
        "yemenfire" to Shot(Offset(w * 0.14f, h * 0.7f), 1.7f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { tensignsChart(a) }
}

private fun DrawScope.tensignsChart(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tr = a.b("tenring")
    val six = a.b("sixsigns")
    val sk = a.b("sinkings")
    val yf = a.b("yemenfire")
    drawRect(Brush.radialGradient(listOf(TenC26.Parch, TenC26.ParchLow), Offset(w * 0.5f, h * 0.5f), w * 0.7f))
    drawRect(TenC26.Rim, Offset(w * 0.02f, h * 0.03f), Size(w * 0.96f, h * 0.94f), alpha = 0.6f, style = Stroke(1.5f * dp))
    drawRect(TenC26.Rim, Offset(w * 0.03f, h * 0.045f), Size(w * 0.94f, h * 0.91f), alpha = 0.4f, style = Stroke(0.8f * dp))
    val r = w * 0.068f
    // the thread of the order, from medallion to medallion
    for (m in 0 until 9) {
        val f = easeOut(seg(tr, 0.1f + m * 0.07f, 0.2f + m * 0.07f))
        if (f <= 0f) continue
        val p = medal26(w, h, m)
        val q = medal26(w, h, m + 1)
        if (m == 4) continue
        drawLine(TenC26.Rim, Offset(p.x - r, p.y), Offset(p.x - r - (p.x - q.x - 2 * r) * f, p.y), 1.2f * dp, alpha = 0.6f)
    }
    for (m in 0 until 10) {
        val c = medal26(w, h, m)
        val ring = easeOut(seg(tr, m * 0.07f, m * 0.07f + 0.3f))
        if (ring <= 0f) continue
        drawCircle(TenC26.Well, r * ring, c)
        drawCircle(TenC26.Rim, r * ring, c, style = Stroke(2.5f * dp))
        drawCircle(TenC26.Rim, r * ring * 0.9f, c, alpha = 0.5f, style = Stroke(0.8f * dp))
        // which beat fills this one, and how far
        val fill = when {
            m < 6 -> easeOut(seg(six, m / 6f, m / 6f + 0.12f))
            m < 9 -> easeOut(seg(sk, (m - 6) / 3f, (m - 6) / 3f + 0.25f))
            else -> easeOut(seg(yf, 0f, 0.3f))
        }
        if (fill <= 0f) continue
        val clip = Path().apply { addOval(Rect(c.x - r * 0.88f, c.y - r * 0.88f, c.x + r * 0.88f, c.y + r * 0.88f)) }
        clipPath(clip) { medalIcon26(m, c, r * 0.88f, t, fill, yf) }
        // its name: above for odd columns, below for even ones
        val col = m % 5
        val tl = when {
            m < 6 -> a.tag("sixsigns", 0, m)
            m < 9 -> a.tag("sinkings", 0, m - 6)
            else -> a.tag("yemenfire")
        }
        // the names of a group step aside when the next group is named, so the frame stays readable
        val keep = when {
            m < 6 -> 1f - seg(sk, 0f, 0.15f)
            m < 9 -> 1f - seg(yf, 0f, 0.15f)
            else -> 1f
        }
        tagAt(tl, c + Offset(0f, if (col % 2 == 1) -r * 1.55f else r * 1.55f), seg(fill, 0.4f, 1f) * keep, back = TenC26.Back, ink = ArtInk.Ivory)
    }
}

private fun DrawScope.medalIcon26(m: Int, c: Offset, r: Float, t: Float, f: Float, yf: Float) {
    val dp = density
    val o = f
    when (m) {
        0 -> { // smoke
            for (k in 0 until 5) {
                val u = fract(t * 0.2f + k / 5f)
                drawCircle(TenC26.Smoke, r * (0.25f + 0.2f * u), c + Offset(r * (-0.4f + 0.2f * k), r * 0.5f - r * 1.1f * u), alpha = 0.6f * o * (1f - u * 0.6f))
            }
        }
        1 -> { // the dark fog of his trial
            for (k in 0 until 6) {
                val ang = t * 0.8f + k * 1.05f
                drawOval(TenC26.Fog, Offset(c.x + cos(ang) * r * 0.25f - r * 0.4f, c.y + sin(ang) * r * 0.2f - r * 0.18f), Size(r * 0.8f, r * 0.36f), alpha = 0.4f * o)
            }
            drawCircle(Color(0xFF1A121E), r * 0.3f, c, alpha = 0.7f * o)
        }
        2 -> { // the beast
            val b = c + Offset(0f, r * 0.15f)
            drawOval(TenC26.Beast, Offset(b.x - r * 0.45f, b.y - r * 0.2f), Size(r * 0.9f, r * 0.4f), alpha = o)
            drawLine(TenC26.Beast, b + Offset(-r * 0.4f, -r * 0.05f), b + Offset(-r * 0.6f, -r * 0.35f), r * 0.14f, StrokeCap.Round, alpha = o)
            drawOval(TenC26.Beast, Offset(b.x - r * 0.78f, b.y - r * 0.48f), Size(r * 0.3f, r * 0.18f), alpha = o)
            for (k in 0 until 4) drawLine(TenC26.Beast, b + Offset(-r * 0.3f + k * r * 0.2f, r * 0.12f), b + Offset(-r * 0.3f + k * r * 0.2f, r * 0.55f), r * 0.08f, StrokeCap.Round, alpha = o)
        }
        3 -> { // the sun over the western rim
            drawRect(TenC26.Sky, Offset(c.x - r, c.y - r), Size(2 * r, 2 * r), alpha = o)
            val rise = easeOut(f)
            val sun = Offset(c.x - r * 0.45f, c.y + r * 0.35f - r * 0.45f * rise)
            softGlow(sun, r * 0.6f, TenC26.Sun, 0.6f * o)
            drawCircle(TenC26.Sun, r * 0.28f, sun, alpha = o)
            drawRect(TenC26.Sand, Offset(c.x - r, c.y + r * 0.35f), Size(2 * r, r), alpha = o)
            drawPath(Path().apply { moveTo(c.x + r * 0.5f, c.y + r * 0.2f); quadraticTo(c.x + r * 0.2f, c.y - r * 0.5f, c.x - r * 0.15f, c.y - r * 0.45f) }, TenC26.Ink, alpha = 0.6f * o, style = Stroke(1.2f * dp))
            arrowHead(Offset(c.x - r * 0.15f, c.y - r * 0.45f), Offset(-1f, 0.05f), r * 0.16f, TenC26.Ink, 1.2f * dp, 0.6f * o)
        }
        4 -> { // the white minaret, and a small warm light coming down (a prophet: light, with his name)
            drawRect(TenC26.Sky, Offset(c.x - r, c.y - r), Size(2 * r, 2 * r), alpha = o)
            val base = Offset(c.x + r * 0.25f, c.y + r * 0.9f)
            drawRect(Color.White, Offset(base.x - r * 0.1f, base.y - r * 1.1f), Size(r * 0.2f, r * 1.1f), alpha = o)
            drawRect(Color(0xFFE2DED4), Offset(base.x - r * 0.16f, base.y - r * 0.85f), Size(r * 0.32f, r * 0.06f), alpha = o)
            drawPath(Path().apply { moveTo(base.x - r * 0.1f, base.y - r * 1.1f); lineTo(base.x, base.y - r * 1.35f); lineTo(base.x + r * 0.1f, base.y - r * 1.1f); close() }, Color.White, alpha = o)
            val lc = Offset(c.x - r * 0.2f, c.y - r * 0.55f + r * 0.35f * easeOut(f))
            softGlow(lc, r * 0.5f, Color(0xFFFFE6B0), 0.8f * o)
            drawCircle(Color(0xFFFFF4DA), r * 0.12f, lc, alpha = o)
        }
        5 -> { // the wall broken open, a flood pouring out
            for (row in 0 until 3) for (k in 0 until 5) {
                if (row < 2 && k == 2) continue
                drawRect(TenC26.Brick, Offset(c.x - r + k * r * 0.42f + (if (row % 2 == 1) r * 0.2f else 0f), c.y - r * 0.6f + row * r * 0.3f), Size(r * 0.38f, r * 0.26f), alpha = o)
            }
            for (k in 0 until 14) {
                val u = fract(t * 0.5f + k / 14f)
                drawCircle(TenC26.Ink, r * 0.05f, Offset(c.x + r * (-0.1f + 0.2f * fract(k * 0.37f)) + r * 0.8f * u * (if (k % 2 == 0) 1f else -1f), c.y - r * 0.2f + r * 0.9f * u), alpha = o * (1f - u))
            }
        }
        6, 7 -> { // a crater, the sun marking east (right) or west (left)
            drawRect(TenC26.Sky, Offset(c.x - r, c.y - r), Size(2 * r, 2 * r), alpha = o)
            drawRect(TenC26.Sand, Offset(c.x - r, c.y), Size(2 * r, r), alpha = o)
            val east = m == 6
            drawCircle(TenC26.Sun, r * 0.18f, Offset(c.x + (if (east) r * 0.6f else -r * 0.6f), c.y - r * 0.05f), alpha = o)
            val sink = easeOut(f)
            drawOval(Color(0xFF3A2A1E), Offset(c.x - r * 0.4f, c.y + r * 0.2f), Size(r * 0.8f, r * 0.3f * sink), alpha = o)
            for (k in 0 until 4) drawCircle(TenC26.Brick, r * 0.05f, Offset(c.x - r * 0.3f + k * r * 0.2f, c.y + r * 0.25f + r * 0.2f * sink * fract(t + k * 0.3f)), alpha = o)
        }
        8 -> { // a crater in Arabia
            drawRect(Color(0xFF8AB0D0), Offset(c.x - r, c.y - r), Size(2 * r, 2 * r), alpha = o)
            drawPath(Path().apply {
                moveTo(c.x - r * 0.55f, c.y - r * 0.6f); lineTo(c.x + r * 0.45f, c.y - r * 0.55f); lineTo(c.x + r * 0.7f, c.y + r * 0.1f)
                lineTo(c.x + r * 0.35f, c.y + r * 0.6f); lineTo(c.x - r * 0.1f, c.y + r * 0.7f); lineTo(c.x - r * 0.4f, c.y + r * 0.1f); close()
            }, TenC26.Sand, alpha = o)
            drawCircle(Color(0xFF3A2A1E), r * 0.14f * easeOut(f), c + Offset(0f, r * 0.05f), alpha = o)
        }
        else -> { // the fire from Yemen, driving people north
            drawRect(Color(0xFF3A1E14), Offset(c.x - r, c.y - r), Size(2 * r, 2 * r), alpha = o)
            for (k in 0 until 6) {
                val x = c.x + r * (-0.6f + 0.24f * k)
                val fl = r * (0.35f + 0.15f * sin(t * 7f + k))
                drawPath(Path().apply { moveTo(x - r * 0.12f, c.y + r); quadraticTo(x, c.y + r - fl * 2f, x + r * 0.12f, c.y + r); close() }, if (k % 2 == 0) TenC26.Fire else Color(0xFFFFB04A), alpha = o)
            }
            val drive = seg(yf, 0.3f, 1f)
            if (drive > 0f) for (k in 0 until 12) {
                val u = fract(t * 0.35f + k / 12f)
                drawCircle(Color(0xFFF2E6C8), r * 0.05f, Offset(c.x + r * (-0.6f + 0.11f * k), c.y + r * 0.2f - r * 1.2f * u), alpha = drive * (1f - u))
            }
            arrowHead(c + Offset(0f, -r * 0.75f), Offset(0f, -1f), r * 0.2f, Color(0xFFF2E6C8), 1.5f * dp, drive)
        }
    }
}

// ═════════════ 9. the grave: two angels, three questions, and its two ends (qabr) ═════════════

private object BarC26 {
    val SkyTop = Color(0xFF3A2E54)
    val SkyLow = Color(0xFFD8946A)
    val Earth = Color(0xFF5A4232)
    val EarthLow = Color(0xFF3A2A20)
    val Strata = Color(0xFF6A5040)
    val Niche = Color(0xFF241A14)
    val Marker = Color(0xFFB8AC98)
    val Ink = Color(0xFFD8D0C4)
    val Garden = Color(0xFF4A9A6A)
    val GardenLight = Color(0xFFBFF0C8)
    val Fire = Color(0xFFE8602A)
    val Back = Color(0xE6201810)
}

private fun nicheC26(w: Float, h: Float, right: Boolean): Offset = Offset(w * (if (right) 0.72f else 0.28f), h * 0.78f)

/**
 * The earth cut open at dusk. Beats: twoangels (two cool white lights come down into each grave and the dead sit up),
 * threeq (three questions in turn: «مَنْ رَبُّكَ؟», «مَا دِينُكَ؟», «مَنْ نَبِيُّكَ؟»), answers (on the right, an answer in gold
 * beside each question; on the left, only «هَاهْ هَاهْ، لَا أَدْرِي»), rawda (the right grave widens and turns to a garden),
 * hufra (the left one closes in and reddens with fire).
 */
internal fun DrawScope.barzakhScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twoangels" to Shot(Offset(w * 0.5f, h * 0.62f), 1.12f),
        "threeq" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "answers" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "rawda" to Shot(Offset(w * 0.72f, h * 0.72f), 1.4f),
        "hufra" to Shot(Offset(w * 0.28f, h * 0.72f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { barzakhEarth(a) }
}

private fun DrawScope.barzakhEarth(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val an = a.b("twoangels")
    val q = a.b("threeq")
    val ans = a.b("answers")
    val rw = a.b("rawda")
    val hf = a.b("hufra")
    val surface = h * 0.3f
    drawRect(Brush.verticalGradient(listOf(BarC26.SkyTop, BarC26.SkyLow), endY = surface), size = Size(w, surface))
    drawRect(Brush.verticalGradient(listOf(BarC26.Earth, BarC26.EarthLow), startY = surface, endY = h), Offset(0f, surface), Size(w, h - surface))
    for (k in 0 until 5) {
        val y = h * (0.38f + 0.12f * k)
        drawLine(BarC26.Strata, Offset(0f, y), Offset(w, y + sin(k * 1.7f) * h * 0.01f), 1f * dp, alpha = 0.5f)
    }
    // the markers above, a bare tree
    for (right in listOf(true, false)) {
        val x = nicheC26(w, h, right).x
        drawArc(Color(0xFF6A5240), 180f, 180f, true, Offset(x - w * 0.08f, surface - h * 0.03f), Size(w * 0.16f, h * 0.06f))
        drawRoundRect(BarC26.Marker, Offset(x + w * 0.05f, surface - h * 0.09f), Size(w * 0.016f, h * 0.08f), CornerRadius(w * 0.006f))
    }
    drawLine(Color(0xFF2A1E18), Offset(w * 0.5f, surface), Offset(w * 0.5f, surface - h * 0.2f), 3f * dp)
    for (k in 0 until 3) drawLine(Color(0xFF2A1E18), Offset(w * 0.5f, surface - h * (0.1f + 0.04f * k)), Offset(w * (0.5f + (if (k % 2 == 0) 0.05f else -0.05f)), surface - h * (0.16f + 0.04f * k)), 1.6f * dp)
    // the two graves
    val widen = easeInOut(seg(rw, 0.1f, 0.7f))
    val close = easeInOut(seg(hf, 0.1f, 0.7f))
    for (right in listOf(true, false)) {
        val c = nicheC26(w, h, right)
        val gw = w * 0.26f * (if (right) 1f + 0.35f * widen else 1f - 0.3f * close)
        val gh = h * 0.16f * (if (right) 1f + 0.4f * widen else 1f - 0.2f * close)
        val tl = Offset(c.x - gw / 2f, c.y - gh / 2f)
        val fill = when {
            right && widen > 0f -> lerpColor(BarC26.Niche, BarC26.Garden, widen)
            !right && close > 0f -> lerpColor(BarC26.Niche, Color(0xFF5A1A10), close)
            else -> BarC26.Niche
        }
        drawRoundRect(fill, tl, Size(gw, gh), CornerRadius(w * 0.03f))
        if (right && widen > 0f) {
            softGlow(c, gw * 0.6f, BarC26.GardenLight, 0.5f * widen)
            for (k in 0 until 8) {
                val fx = tl.x + gw * (0.08f + 0.12f * k)
                drawLine(Color(0xFF2E7A4A), Offset(fx, tl.y + gh), Offset(fx, tl.y + gh * 0.82f), 1.2f * dp, alpha = widen)
                drawCircle(if (k % 2 == 0) Color(0xFFFFF0A0) else Color(0xFFFFC0D0), w * 0.006f, Offset(fx, tl.y + gh * 0.8f), alpha = widen)
            }
        }
        if (!right && close > 0f) {
            softGlow(c + Offset(0f, gh * 0.3f), gw * 0.6f, BarC26.Fire, 0.5f * close)
            for (k in 0 until 6) {
                val fx = tl.x + gw * (0.1f + 0.16f * k)
                val fl = gh * (0.25f + 0.12f * sin(t * 7f + k))
                drawPath(Path().apply { moveTo(fx - w * 0.01f, tl.y + gh); quadraticTo(fx, tl.y + gh - fl, fx + w * 0.01f, tl.y + gh); close() }, Color(0xFFFFB04A), alpha = close)
            }
        }
        // the one laid in it: lying, then sitting up when the angels come
        val sit = easeInOut(seg(an, 0.45f, 0.8f))
        val tall = h * 0.13f
        val shake = if (!right && close > 0f) sin(t * 20f) * w * 0.002f * close else 0f
        if (sit < 1f) {
            val y = tl.y + gh * 0.72f
            val lx = c.x - w * 0.07f
            drawCircle(BarC26.Ink, tall * 0.1f, Offset(lx, y - tall * 0.05f), alpha = 1f - sit)
            drawLine(BarC26.Ink, Offset(lx + tall * 0.1f, y - tall * 0.03f), Offset(lx + tall * 0.95f, y), max(1.7f * dp, tall * 0.065f), StrokeCap.Round, alpha = 1f - sit)
        }
        if (sit > 0f) w8Seated(Offset(c.x + w * 0.02f + shake, tl.y + gh * 0.82f), tall, BarC26.Ink, if (right) -1f else 1f, sit)
        // the two angels, as cool lights
        for (s in listOf(-1f, 1f)) {
            val down = easeOut(seg(an, 0f, 0.5f))
            val target = c + Offset(s * gw * 0.36f, -gh * 0.2f)
            val p = Offset(target.x, mix(surface - h * 0.15f, target.y, down))
            angelLight26(p, w * 0.012f, t + s, easeOut(seg(an, 0f, 0.2f)) * (1f - 0.6f * max(widen, close)))
        }
    }
    // the three questions, and the answers
    val fadeQ = 1f - easeOut(seg(rw, 0f, 0.3f))
    val ys = listOf(h * 0.42f, h * 0.51f, h * 0.6f)
    for (k in 0 until 3) {
        tagAt(a.tag("threeq", 0, k), Offset(w * 0.5f, ys[k]), seg(q, k * 0.28f, k * 0.28f + 0.2f) * fadeQ, back = BarC26.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("answers", 0, k), Offset(w * 0.77f, ys[k]), seg(ans, k * 0.18f, k * 0.18f + 0.12f) * fadeQ, back = BarC26.Back, ink = ArtInk.Gold)
    }
    tagAt(a.tag("answers", 0, 3), Offset(w * 0.22f, ys[1]), seg(ans, 0.62f, 0.75f) * fadeQ * (1f - seg(hf, 0f, 0.2f)), back = BarC26.Back, ink = Color(0xFFC8C0D0))
    qmark21(Offset(w * 0.22f, ys[0]), h * 0.06f, Color(0xFFC8C0D0), seg(ans, 0.7f, 0.85f) * fadeQ)
}
