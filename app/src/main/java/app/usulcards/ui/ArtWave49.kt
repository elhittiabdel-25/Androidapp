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
 * Wave 49 (2026-11-03): «رياض الصالحين»، «مكارم الأخلاق» (3) and «آفات اللسان». A bright morning street where half a
 * date and a kind word count; two locks that open a garden; a word that falls between east and west; a drop of ink
 * that darkens the sea; a notebook of hearsay passed on; a curse that finds the sky's gates shut and comes home.
 * Shaytan is a dark smoke; the Prophet ﷺ is only light; Allah is never drawn.
 */

private val Back49 = Color(0xE6181420)

// ═════════════ 1. a bright face (riyad-wajh) ═════════════

private object FaceC49 {
    val Sky = Color(0xFFF3D7A8)
    val SkyTop = Color(0xFF9CC4E0)
    val Ground = Color(0xFFD6BE94)
    val Wall = Color(0xFFE2CCA2)
    val Trim = Color(0xFF8A6A44)
    val Person = Color(0xFF3A3040)
    val Warm = Color(0xFFFFD27A)
    val Date = Color(0xFF7A3A22)
    val Fire = Color(0xFFD8582E)
    val Green = Color(0xFF5AA06A)
    val Spike = Color(0xFF6A5A6A)
}

/**
 * Beats: smallgood (two pass in a morning street; a warm glow opens between them as they meet), halfdate (half a date
 * given, then a kind word; the fire behind draws back: «بِشِقِّ تَمْرَةٍ», «فَبِكَلِمَةٍ طَيِّبَةٍ»), drawnear (people come
 * close round the light ﷺ; a spiky shape elsewhere scatters the dots near it), listen (two sit: one speaks, the other
 * leans in and does not look away), goodnews (one runs with a green ribbon to a house, and the house lights up).
 */
internal fun DrawScope.brightfaceScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "smallgood" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "halfdate" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "drawnear" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "listen" to Shot(Offset(w * 0.5f, h * 0.65f), 1.3f),
        "goodnews" to Shot(Offset(w * 0.55f, h * 0.6f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { faceMorning(a) }
}

private fun DrawScope.faceMorning(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sg = a.b("smallgood")
    val hd = a.b("halfdate")
    val ga = a.b("drawnear")
    val ls = a.b("listen")
    val gn = a.b("goodnews")
    val ground = h * 0.84f
    daySky12(FaceC49.SkyTop, FaceC49.Sky, ground, FaceC49.Ground)
    sun12(Offset(w * 0.15f, h * 0.2f), w * 0.04f, 1f)
    for (k in 0 until 4) house12(Offset(w * (0.14f + 0.24f * k), ground - h * 0.01f), w * 0.18f, FaceC49.Wall, FaceC49.Trim, 0.3f)
    if (sg > 0f) {
        val f = easeOut(seg(sg, 0f, 0.3f)) * (1f - seg(hd, 0f, BEAT_OUT))
        val meet = easeInOut(seg(sg, 0.05f, 0.5f))
        val l = Offset(w * (0.15f + 0.27f * meet), ground)
        val r = Offset(w * (0.85f - 0.27f * meet), ground)
        figure(l, h * 0.3f, FaceC49.Person, f, walk = t * 6f * (1f - meet), arms = 0.3f * meet)
        figure(r, h * 0.3f, FaceC49.Person, f, walk = t * 6f * (1f - meet), arms = -0.2f * meet)
        val glow = seg(sg, 0.45f, 0.85f)
        softGlow(Offset(w * 0.5f, ground - h * 0.26f), w * 0.12f, FaceC49.Warm, 0.6f * f * glow)
        for (k in 0 until 2) softGlow(Offset(if (k == 0) l.x else r.x, ground - h * 0.27f), w * 0.04f, FaceC49.Warm, 0.7f * f * glow)
    }
    if (hd > 0f) {
        val f = easeOut(seg(hd, 0f, BEAT_IN)) * (1f - seg(ga, 0f, BEAT_OUT))
        val back = easeInOut(seg(hd, 0.3f, 0.9f))
        for (k in 0 until 6) {
            val x = w * (0.62f + 0.06f * k + 0.25f * back)
            drawOval(FaceC49.Fire, Offset(x, ground - h * (0.12f + 0.04f * sin(t * 6f + k))), Size(w * 0.04f, h * 0.14f), alpha = f * (1f - back) * 0.85f)
        }
        figure(Offset(w * 0.2f, ground), h * 0.3f, FaceC49.Person, f, arms = 0.7f)
        figure(Offset(w * 0.42f, ground), h * 0.3f, FaceC49.Person, f, arms = -0.3f)
        val g = seg(hd, 0.1f, 0.3f)
        drawOval(FaceC49.Date, Offset(w * 0.29f, ground - h * 0.19f), Size(w * 0.025f, h * 0.025f), alpha = f * g)
        val word = seg(hd, 0.45f, 0.65f)
        drawCircle(FaceC49.Green, w * 0.012f, Offset(w * 0.31f, ground - h * 0.36f), alpha = f * word)
        voice12(Offset(w * 0.24f, ground - h * 0.3f), w * 0.04f, t, FaceC49.Green, f * word)
        tagAt(a.tag("halfdate", 0, 0), Offset(w * 0.3f, ground - h * 0.48f), f * g, back = Back49, ink = ArtInk.Gold)
        tagAt(a.tag("halfdate", 0, 1), Offset(w * 0.3f, ground - h * 0.6f), f * word, back = Back49, ink = ArtInk.Gold)
    }
    if (ga > 0f) {
        val f = easeOut(seg(ga, 0f, BEAT_IN)) * (1f - seg(ls, 0f, BEAT_OUT))
        val c = Offset(w * 0.34f, h * 0.42f)
        sirajMunir(c, w * 0.014f, f, t, nameAt = c + Offset(0f, -h * 0.13f))
        val near = easeInOut(seg(ga, 0.15f, 0.7f))
        for (k in 0 until 6) {
            val ang = k * PI.toFloat() / 3f + 0.3f
            val rr = w * (0.26f - 0.12f * near)
            nameRoundel(Offset(c.x + cos(ang) * rr, ground - h * 0.06f - h * 0.02f * (k % 2)), w * 0.028f, null, Roundel.PlainFill, Roundel.PlainRim, f * 0.9f)
        }
        val sp = Offset(w * 0.8f, h * 0.5f)
        val spread = seg(ga, 0.5f, 0.95f)
        for (k in 0 until 8) {
            val ang = k * PI.toFloat() / 4f
            drawLine(FaceC49.Spike, sp, sp + Offset(cos(ang), sin(ang)) * (w * 0.035f), 2f * dp, StrokeCap.Round, alpha = f * seg(ga, 0.4f, 0.5f))
            drawCircle(FaceC49.Person, w * 0.008f, sp + Offset(cos(ang + 0.2f), sin(ang + 0.2f)) * (w * (0.05f + 0.1f * spread)), alpha = f * (1f - 0.5f * spread))
        }
    }
    if (ls > 0f) {
        val f = easeOut(seg(ls, 0f, BEAT_IN)) * (1f - seg(gn, 0f, BEAT_OUT))
        sit12(Offset(w * 0.38f, ground), h * 0.32f, FaceC49.Person, f, 1f)
        voice12(Offset(w * 0.44f, ground - h * 0.38f), w * 0.04f, t, FaceC49.Trim, f)
        val lean = easeInOut(seg(ls, 0.2f, 0.5f))
        sit12(Offset(w * 0.64f, ground), h * 0.32f, FaceC49.Person, f, -1f, bob = h * 0.01f * lean)
        drawLine(FaceC49.Warm, Offset(w * 0.45f, ground - h * 0.35f), Offset(w * 0.6f, ground - h * 0.35f), 1.5f * dp, alpha = f * lean * 0.7f)
    }
    if (gn > 0f) {
        val f = easeOut(seg(gn, 0f, 0.3f))
        val run = easeInOut(seg(gn, 0.05f, 0.55f))
        val p = Offset(w * (0.1f + 0.52f * run), ground)
        figure(p, h * 0.28f, FaceC49.Person, f, walk = t * 9f * (1f - seg(gn, 0.5f, 0.55f)), lean = 0.15f * (1f - run), arms = 0.8f)
        drawLine(FaceC49.Green, p + Offset(-w * 0.05f, -h * 0.22f), p + Offset(-w * 0.09f, -h * 0.2f + h * 0.02f * sin(t * 8f)), 3f * dp, StrokeCap.Round, alpha = f)
        val lit = seg(gn, 0.55f, 0.8f)
        house12(Offset(w * 0.8f, ground), w * 0.24f, FaceC49.Wall, FaceC49.Trim, f, lit)
        for (k in 0 until 10) {
            val u = fract(t * 0.5f + k / 10f)
            drawCircle(FaceC49.Warm, 2f * dp, Offset(w * (0.7f + 0.2f * (k / 10f)), ground - h * (0.25f + 0.25f * u)), alpha = f * lit * (1f - u))
        }
    }
}

// ═════════════ 2. two locks and a word that falls (riyad-lisan) ═════════════

private object TongueC49 {
    val Tile = Color(0xFF1A1E36)
    val Line = Color(0xFF34385A)
    val Gold = Color(0xFFE8C46A)
    val Person = Color(0xFFE6DCC8)
    val Thorn = Color(0xFF8A6A5A)
    val East = Color(0xFFF2B866)
    val West = Color(0xFFC0586A)
    val Stone = Color(0xFF7A7A8A)
    val Link = Color(0xFFB8BCC8)
    val Garden = Color(0xFF2E5A3A)
}

/**
 * Beats: safering (one stands in a circle of people and nothing hurtful leaves his hand or his mouth: the ring stays
 * calm), twolocks (two padlocks close on what is between the jaws and what is between the legs — «مَا بَيْنَ لَحْيَيْهِ»,
 * «وَمَا بَيْنَ رِجْلَيْهِ» — and a garden gate opens), fallword (a small stone of a word drops into a gulf as wide as
 * east to west), riseword (a light word lifts one up a stair), organs (a chain of links follows its first link: straight
 * when it is straight, crooked when it bends).
 */
internal fun DrawScope.tonguegateScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "safering" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "twolocks" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "fallword" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "riseword" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "organs" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { tongueNight(a) }
}

private fun DrawScope.tongueNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sr = a.b("safering")
    val tl = a.b("twolocks")
    val fw = a.b("fallword")
    val rw = a.b("riseword")
    val og = a.b("organs")
    drawRect(TongueC49.Tile)
    val step = w * 0.1f
    var x = 0f
    while (x < w) { drawLine(TongueC49.Line, Offset(x, 0f), Offset(x + h * 0.5f, h), 0.8f * dp, alpha = 0.5f); x += step }
    val ground = h * 0.86f
    if (sr > 0f) {
        val f = easeOut(seg(sr, 0f, BEAT_IN)) * (1f - seg(tl, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, ground)
        figure(c, h * 0.34f, TongueC49.Gold, f)
        drawOval(TongueC49.Gold, Offset(c.x - w * 0.36f, c.y - h * 0.08f), Size(w * 0.72f, h * 0.16f), alpha = f * 0.5f * seg(sr, 0.4f, 0.7f), style = Stroke(1.5f * dp))
        for (k in 0 until 4) {
            val p = Offset(c.x + listOf(-0.36f, -0.2f, 0.2f, 0.36f)[k] * w, ground + h * 0.03f * (k % 2))
            figure(p, h * 0.2f, TongueC49.Person, f)
        }
        // thorns that never leave him: drawn and then struck away
        for (k in 0 until 3) {
            val u = seg(sr, 0.15f + 0.1f * k, 0.35f + 0.1f * k)
            val p = c + Offset(w * 0.04f + w * 0.05f * u, -h * (0.28f - 0.06f * k))
            drawLine(TongueC49.Thorn, p, p + Offset(w * 0.02f, -h * 0.01f), 2f * dp, StrokeCap.Round, alpha = f * u * (1f - seg(sr, 0.5f, 0.6f)))
        }
        struckTag21(a.tag("safering", 0, 0), c + Offset(w * 0.16f, -h * 0.46f), f * seg(sr, 0.15f, 0.3f), seg(sr, 0.5f, 0.65f), Color(0xFFD23C30), Color(0xE6201810), Color(0xFFF0A090))
        struckTag21(a.tag("safering", 0, 1), c + Offset(-w * 0.16f, -h * 0.46f), f * seg(sr, 0.25f, 0.4f), seg(sr, 0.55f, 0.7f), Color(0xFFD23C30), Color(0xE6201810), Color(0xFFF0A090))
        tagAt(a.tag("safering", 0, 2), c + Offset(0f, h * 0.1f), f * seg(sr, 0.7f, 0.9f), back = Color(0xE6201810), ink = ArtInk.Gold)
    }
    if (tl > 0f) {
        val f = easeOut(seg(tl, 0f, BEAT_IN)) * (1f - seg(fw, 0f, BEAT_OUT))
        val open = easeInOut(seg(tl, 0.65f, 0.95f))
        drawRect(TongueC49.Garden, Offset(w * 0.62f, h * 0.2f), Size(w * 0.3f, ground - h * 0.2f), alpha = f * open)
        door12(Offset(w * 0.77f, ground), w * 0.16f, h * 0.56f, open, Color(0xFF5A4A3A), Color(0xFF3A2A1E), f, Color(0xFFBFE4A0))
        for (k in 0 until 2) {
            val c = Offset(w * 0.3f, h * (0.34f + 0.26f * k))
            val shut = seg(tl, 0.1f + 0.2f * k, 0.3f + 0.2f * k)
            padlock21(c, w * 0.05f, TongueC49.Gold, 1f - shut, f)
            tagAt(a.tag("twolocks", 0, k), c + Offset(w * 0.15f, 0f), f * seg(tl, 0.15f + 0.2f * k, 0.3f + 0.2f * k), back = Back49, ink = ArtInk.Gold)
        }
    }
    if (fw > 0f) {
        val f = easeOut(seg(fw, 0f, BEAT_IN)) * (1f - seg(rw, 0f, BEAT_OUT))
        val rim = h * 0.36f
        drawRect(Brush.horizontalGradient(listOf(TongueC49.East, Color(0xFF6A5A7A), TongueC49.West)), Offset(0f, h * 0.18f), Size(w, h * 0.12f), alpha = f * 0.6f)
        sun12(Offset(w * 0.06f, h * 0.24f), w * 0.025f, f)
        drawCircle(TongueC49.West, w * 0.022f, Offset(w * 0.94f, h * 0.24f), alpha = f)
        drawRect(Color(0xFF0C0E1C), Offset(w * 0.06f, rim), Size(w * 0.88f, h - rim), alpha = f)
        drawLine(TongueC49.Line, Offset(w * 0.06f, rim), Offset(w * 0.94f, rim), 2f * dp, alpha = f)
        val fall = seg(fw, 0.25f, 0.95f)
        val p = Offset(w * 0.5f + w * 0.02f * sin(fall * 8f), rim - h * 0.06f + (h - rim) * fall * fall)
        drawCircle(TongueC49.Stone, w * 0.015f, p, alpha = f * (1f - seg(fw, 0.85f, 1f)))
        figure(Offset(w * 0.5f, rim), h * 0.18f, TongueC49.Person, f * (1f - fall * 0.4f))
    }
    if (rw > 0f) {
        val f = easeOut(seg(rw, 0f, BEAT_IN)) * (1f - seg(og, 0f, BEAT_OUT))
        for (k in 0 until 6) drawRect(Color(0xFF2E3256), Offset(w * (0.2f + 0.1f * k), ground - h * 0.1f * (k + 1)), Size(w * 0.1f, h * 0.1f * (k + 1)), alpha = f)
        val up = easeInOut(seg(rw, 0.25f, 0.9f))
        val step = (up * 5f)
        val p = Offset(w * (0.25f + 0.1f * step), ground - h * 0.1f * (step.toInt() + 1).coerceAtMost(6))
        figure(p, h * 0.2f, TongueC49.Gold, f)
        softGlow(p + Offset(0f, -h * 0.26f), w * 0.02f, TongueC49.Gold, 0.9f * f * seg(rw, 0.1f, 0.25f))
    }
    if (og > 0f) {
        val f = easeOut(seg(og, 0f, 0.3f))
        val bend = easeInOut(seg(og, 0.55f, 0.8f)) * (1f - easeInOut(seg(og, 0.85f, 1f)))
        val n = 7
        for (k in 0 until n) {
            val u = k / (n - 1f)
            val c = Offset(w * (0.2f + 0.6f * u), h * 0.5f + h * 0.18f * bend * sin(u * 3.5f))
            val lead = k == 0
            drawOval(if (lead) TongueC49.Gold else TongueC49.Link, Offset(c.x - w * 0.04f, c.y - h * 0.035f), Size(w * 0.08f, h * 0.07f), alpha = f * seg(og, 0.05f * k, 0.15f + 0.05f * k), style = Stroke(3f * dp))
        }
        softGlow(Offset(w * 0.2f, h * 0.5f), w * 0.05f, TongueC49.Gold, 0.5f * f)
        tagAt(a.tag("organs", 0, 0), Offset(w * 0.2f, h * 0.36f), f * seg(og, 0.05f, 0.2f), back = Color(0xE6201810), ink = ArtInk.Gold)
        tagAt(a.tag("organs", 0, 1), Offset(w * 0.6f, h * 0.36f), f * seg(og, 0.2f, 0.4f) * (1f - bend), back = Color(0xE6201810), ink = Color(0xFFF0ECE4))
        tagAt(a.tag("organs", 0, 2), Offset(w * 0.5f, h * 0.8f), f * (1f - bend) * seg(og, 0.3f, 0.5f), back = Color(0xE6201810), ink = Color(0xFF9AE0A0))
        tagAt(a.tag("organs", 0, 3), Offset(w * 0.5f, h * 0.8f), f * bend, back = Color(0xE6201810), ink = Color(0xFFF0A090))
    }
}

// ═════════════ 3. a drop in the sea (riyad-ghiba) ═════════════

private object BiteC49 {
    val Sky = Color(0xFF151A2C)
    val SkyLow = Color(0xFF2E2E4E)
    val Ground = Color(0xFF221E2C)
    val Wall = Color(0xFF4A4256)
    val Person = Color(0xFFE6DCC8)
    val Absent = Color(0xFF8A8AA0)
    val Talk = Color(0xFF8A8296)
    val Red = Color(0xFFE0584A)
    val Coal = Color(0xFFF07A3A)
    val Sea = Color(0xFF2A5A7A)
    val Ink = Color(0xFF14121A)
    val Shield = Color(0xFFE8C46A)
    val Fire = Color(0xFFD8582E)
}

/**
 * Beats: absent (two talk; their words drift over a wall toward a third who is away and cannot answer), twoverdicts (a
 * word that matches him is still marked «اغْتَبْتَهُ»; a word that does not match is marked «بَهَتَّهُ» in red), coal (one
 * carries a burning coal from house to house and sets them against each other), inkdrop (a drop of ink falls into the
 * sea and spreads until the sea is dark), shieldback (one who hears it stands up for the absent one; a fire before him
 * draws away).
 */
internal fun DrawScope.backbiteScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "absent" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "twoverdicts" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "coal" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "inkdrop" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "shieldback" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { biteNight(a) }
}

private fun DrawScope.biteNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ab = a.b("absent")
    val tv = a.b("twoverdicts")
    val co = a.b("coal")
    val ik = a.b("inkdrop")
    val sb = a.b("shieldback")
    val ground = h * 0.84f
    nightSky(t, BiteC49.Sky, BiteC49.SkyLow, ground, starAlpha = 0.6f)
    drawRect(BiteC49.Ground, Offset(0f, ground), Size(w, h - ground))
    if (ab > 0f) {
        val f = easeOut(seg(ab, 0f, BEAT_IN)) * (1f - seg(tv, 0f, BEAT_OUT))
        drawRect(BiteC49.Wall, Offset(w * 0.56f, ground - h * 0.36f), Size(w * 0.04f, h * 0.36f), alpha = f)
        figure(Offset(w * 0.18f, ground), h * 0.3f, BiteC49.Person, f, arms = 0.4f)
        figure(Offset(w * 0.36f, ground), h * 0.3f, BiteC49.Person, f, arms = -0.4f)
        figure(Offset(w * 0.8f, ground), h * 0.3f, BiteC49.Absent, f, lean = 0.05f)
        for (k in 0 until 5) {
            val u = fract(t * 0.25f + k / 5f) * seg(ab, 0.1f, 0.3f)
            cloud38(Offset(w * (0.3f + 0.45f * u), h * (0.4f - 0.15f * sin(PI.toFloat() * u))), w * 0.03f, BiteC49.Talk, f * 0.7f * (1f - u * 0.5f))
        }
    }
    if (tv > 0f) {
        val f = easeOut(seg(tv, 0f, BEAT_IN)) * (1f - seg(co, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, ground)
        figure(c, h * 0.36f, BiteC49.Absent, f)
        val mark = c + Offset(w * 0.02f, -h * 0.2f)
        drawCircle(BiteC49.Talk, w * 0.012f, mark, alpha = f)
        for (k in 0 until 2) {
            val start = Offset(w * (0.15f + 0.7f * k), h * 0.25f)
            val target = if (k == 0) mark else c + Offset(w * 0.12f, -h * 0.12f)
            val go = easeInOut(seg(tv, 0.1f + 0.35f * k, 0.35f + 0.35f * k))
            val p = start + (target - start) * go
            drawCircle(if (k == 0) BiteC49.Talk else BiteC49.Red, w * 0.014f, p, alpha = f * seg(tv, 0.05f + 0.35f * k, 0.15f + 0.35f * k))
            tagAt(a.tag("twoverdicts", 0, k), start + Offset(0f, -h * 0.08f), f * seg(tv, 0.3f + 0.35f * k, 0.45f + 0.35f * k), back = Back49, ink = if (k == 0) ArtInk.Ivory else BiteC49.Red)
        }
    }
    if (co > 0f) {
        val f = easeOut(seg(co, 0f, BEAT_IN)) * (1f - seg(ik, 0f, BEAT_OUT))
        house12(Offset(w * 0.18f, ground), w * 0.22f, BiteC49.Wall, Color(0xFF2A2430), f, 0.5f)
        house12(Offset(w * 0.82f, ground), w * 0.22f, BiteC49.Wall, Color(0xFF2A2430), f, 0.5f)
        val trip = easeInOut(seg(co, 0.1f, 0.6f))
        val p = Offset(w * (0.3f + 0.4f * fract(trip * 2f)), ground)
        figure(p, h * 0.26f, BiteC49.Person, f * (1f - seg(co, 0.6f, 0.7f)), walk = t * 7f, arms = 0.6f)
        drawCircle(BiteC49.Coal, w * 0.01f, p + Offset(-w * 0.05f, -h * 0.17f), alpha = f * (1f - seg(co, 0.6f, 0.7f)))
        val burn = seg(co, 0.6f, 0.95f)
        for (k in 0 until 2) for (j in 0 until 3) {
            val x = w * (if (k == 0) 0.12f else 0.76f) + w * 0.04f * j
            drawOval(BiteC49.Fire, Offset(x, ground - h * (0.2f + 0.04f * sin(t * 7f + j + k))), Size(w * 0.03f, h * 0.1f), alpha = f * burn * 0.8f)
        }
    }
    if (ik > 0f) {
        val f = easeOut(seg(ik, 0f, BEAT_IN)) * (1f - seg(sb, 0f, BEAT_OUT))
        val sea = h * 0.5f
        drawRect(BiteC49.Sea, Offset(0f, sea), Size(w, h - sea), alpha = f)
        for (k in 0 until 8) drawLine(Color(0xFF6AA0C0), Offset(w * fract(k / 8f + t * 0.02f), sea + h * (0.05f + 0.05f * (k % 4))), Offset(w * fract(k / 8f + t * 0.02f) + w * 0.06f, sea + h * (0.05f + 0.05f * (k % 4))), 1.2f * dp, alpha = f * 0.5f)
        val drop = seg(ik, 0.1f, 0.3f)
        if (drop < 1f) drawCircle(BiteC49.Ink, w * 0.03f, Offset(w * 0.5f, h * 0.15f + (sea - h * 0.15f) * drop), alpha = f)
        sirajMunir(Offset(w * 0.8f, h * 0.2f), w * 0.012f, f, t, nameAt = Offset(w * 0.8f, h * 0.33f))
        tagAt(a.tag("inkdrop", 0, 0), Offset(w * 0.34f, h * 0.2f), f * seg(ik, 0.02f, 0.15f), back = Color(0xE6201810), ink = Color(0xFFF0A090))
        tagAt(a.tag("inkdrop", 0, 1), Offset(w * 0.5f, sea - h * 0.1f), f * seg(ik, 0.6f, 0.85f), back = Color(0xE6201810), ink = Color(0xFFF0ECE4))
        val spread = easeOut(seg(ik, 0.3f, 0.95f))
        drawOval(Brush.radialGradient(listOf(BiteC49.Ink.copy(alpha = 0.95f), BiteC49.Ink.copy(alpha = 0.7f), Color.Transparent), Offset(w * 0.5f, sea + h * 0.1f), w * 0.8f * spread + 1f), Offset(w * 0.5f - w * 0.8f * spread, sea), Size(w * 1.6f * spread + 1f, h * 0.5f), alpha = f)
    }
    if (sb > 0f) {
        val f = easeOut(seg(sb, 0f, 0.3f))
        figure(Offset(w * 0.18f, ground), h * 0.3f, BiteC49.Person, f, arms = 0.5f)
        val stand = easeOut(seg(sb, 0.1f, 0.35f))
        figure(Offset(w * 0.42f, ground), h * 0.3f, BiteC49.Shield, f, arms = -0.7f * stand)
        figure(Offset(w * 0.82f, ground), h * 0.3f, BiteC49.Absent, f)
        drawArc(BiteC49.Shield, -60f, 120f, false, Offset(w * 0.6f, ground - h * 0.36f), Size(w * 0.14f, h * 0.36f), alpha = f * stand, style = Stroke(3f * dp))
        val away = easeInOut(seg(sb, 0.5f, 0.95f))
        for (k in 0 until 4) drawOval(BiteC49.Fire, Offset(w * (0.32f + 0.04f * k) - w * 0.3f * away, ground - h * (0.1f + 0.03f * sin(t * 7f + k))), Size(w * 0.03f, h * 0.09f), alpha = f * seg(sb, 0.4f, 0.5f) * (1f - away))
    }
}

// ═════════════ 4. hearsay in the notebook (riyad-tathabbut) ═════════════

/**
 * Beats: sieve (sounds pour into a funnel: without a sieve all of it runs out and is struck out; through a sieve only
 * what was checked passes, ticked), falsedream (a sleeper with a dream cloud drawn in dashes — struck out), passedon (a
 * chain of dots: one is marked false, and each who passes it on is marked too), fourmarks (four boxes, each with its
 * mark: «خَانَ», «كَذَبَ», «غَدَرَ», «فَجَرَ»), twograins (two barley grains and a thread that slips each time it is tied).
 */
internal fun DrawScope.rumorchainScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sieve" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "falsedream" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "passedon" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "fourmarks" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "twograins" to Shot(Offset(w * 0.5f, h * 0.5f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0f) { rumorPage(a) }
}

private fun DrawScope.rumorPage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pp = Paper(a.dark)
    paperSheet(pp)
    val sv = a.b("sieve")
    val fd = a.b("falsedream")
    val po = a.b("passedon")
    val fm = a.b("fourmarks")
    val tg = a.b("twograins")
    val pen = 2.2f * dp
    if (sv > 0f) {
        val f = easeOut(seg(sv, 0f, BEAT_IN)) * (1f - seg(fd, 0f, BEAT_OUT))
        for (side in 0 until 2) {
            val c = Offset(w * (0.3f + 0.4f * side), h * 0.42f)
            penLine(listOf(c + Offset(-w * 0.1f, -h * 0.16f), c + Offset(-w * 0.015f, h * 0.08f), c + Offset(-w * 0.015f, h * 0.2f)), seg(sv, 0.05f, 0.25f), pp.ink, pen, f)
            penLine(listOf(c + Offset(w * 0.1f, -h * 0.16f), c + Offset(w * 0.015f, h * 0.08f), c + Offset(w * 0.015f, h * 0.2f)), seg(sv, 0.05f, 0.25f), pp.ink, pen, f)
            if (side == 1) penLine(listOf(c + Offset(-w * 0.05f, -h * 0.02f), c + Offset(w * 0.05f, -h * 0.02f)), seg(sv, 0.25f, 0.35f), pp.amber, pen, f, dash = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp)))
            for (k in 0 until 8) {
                val u = fract(t * 0.5f + k / 8f)
                val good = k % 3 == 0
                val y = c.y - h * 0.2f + h * 0.45f * u
                val pass = side == 0 || good || y < c.y - h * 0.02f
                if (!pass) continue
                drawCircle(if (good) pp.good else pp.bad, w * 0.008f, Offset(c.x + w * 0.04f * sin(k * 2.1f) * (1f - u), y), alpha = f * seg(sv, 0.2f, 0.35f))
            }
            if (side == 0) crossMark(c + Offset(0f, h * 0.32f), w * 0.03f, pp.bad, pen * 1.2f, f * seg(sv, 0.45f, 0.6f))
            else checkMark(c + Offset(0f, h * 0.32f), w * 0.03f, pp.good, pen * 1.2f, f * seg(sv, 0.65f, 0.8f))
        }
    }
    if (fd > 0f) {
        val f = easeOut(seg(fd, 0f, BEAT_IN)) * (1f - seg(po, 0f, BEAT_OUT))
        val bed = Offset(w * 0.3f, h * 0.72f)
        penLine(listOf(bed + Offset(-w * 0.14f, 0f), bed + Offset(w * 0.14f, 0f)), seg(fd, 0.05f, 0.2f), pp.ink, pen, f)
        drawLine(pp.ink, bed + Offset(-w * 0.08f, -h * 0.03f), bed + Offset(w * 0.1f, -h * 0.025f), h * 0.03f, StrokeCap.Round, alpha = f)
        drawCircle(pp.ink, h * 0.025f, bed + Offset(-w * 0.11f, -h * 0.04f), alpha = f)
        val cloud = Offset(w * 0.6f, h * 0.35f)
        drawOval(pp.faint, Offset(cloud.x - w * 0.16f, cloud.y - h * 0.12f), Size(w * 0.32f, h * 0.24f), alpha = f * seg(fd, 0.2f, 0.4f), style = Stroke(pen, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp))))
        for (k in 0 until 3) drawCircle(pp.faint, w * 0.008f * (k + 1), bed + Offset(w * (0.04f + 0.06f * k), -h * (0.1f + 0.06f * k)), alpha = f * seg(fd, 0.15f, 0.3f), style = Stroke(1.2f * dp))
        crossMark(cloud, w * 0.06f, pp.bad, pen * 1.4f, f * seg(fd, 0.55f, 0.75f))
    }
    if (po > 0f) {
        val f = easeOut(seg(po, 0f, BEAT_IN)) * (1f - seg(fm, 0f, BEAT_OUT))
        val n = 6
        for (k in 0 until n) {
            val c = Offset(w * (0.12f + 0.15f * k), h * 0.5f)
            val g = seg(po, 0.05f + 0.1f * k, 0.15f + 0.1f * k)
            if (k > 0) penLine(listOf(c + Offset(-w * 0.12f, 0f), c + Offset(-w * 0.03f, 0f)), g, pp.ink, pen, f)
            if (k > 0) arrowHead(c + Offset(-w * 0.03f, 0f), Offset(1f, 0f), w * 0.015f, pp.ink, pen, f * g)
            val bad = k >= 2
            drawCircle(if (bad) pp.bad else pp.ink, w * 0.022f, c, alpha = f * g, style = Stroke(pen))
            if (bad) crossMark(c, w * 0.012f, pp.bad, pen, f * seg(po, 0.25f + 0.1f * k, 0.35f + 0.1f * k))
        }
    }
    if (fm > 0f) {
        val f = easeOut(seg(fm, 0f, BEAT_IN)) * (1f - seg(tg, 0f, BEAT_OUT))
        for (k in 0 until 4) {
            val c = Offset(w * (0.2f + 0.2f * k), h * 0.5f)
            val g = seg(fm, 0.1f + 0.18f * k, 0.28f + 0.18f * k)
            penLine(listOf(c + Offset(-w * 0.07f, -h * 0.12f), c + Offset(w * 0.07f, -h * 0.12f), c + Offset(w * 0.07f, h * 0.12f), c + Offset(-w * 0.07f, h * 0.12f), c + Offset(-w * 0.07f, -h * 0.12f)), g, pp.ink, pen, f)
            note(a.tag("fourmarks", 0, k), c, f * g, pp, pp.bad)
        }
    }
    if (tg > 0f) {
        val f = easeOut(seg(tg, 0f, 0.25f))
        val l = Offset(w * 0.4f, h * 0.5f)
        val r = Offset(w * 0.6f, h * 0.5f)
        for (c in listOf(l, r)) drawOval(pp.amber, Offset(c.x - w * 0.02f, c.y - h * 0.035f), Size(w * 0.04f, h * 0.07f), alpha = f)
        val tie = fract(tg * 3f)
        val knot = bump(tie)
        val mid = (l + r) / 2f + Offset(0f, -h * 0.12f * knot)
        penLine(listOf(l, mid, r), seg(tg, 0.1f, 0.3f), pp.ink, pen, f)
        if (tie > 0.8f) drawCircle(pp.bad, w * 0.01f, mid, alpha = f)
    }
}

// ═════════════ 5. a curse that comes home (riyad-lan) ═════════════

private object CurseC49 {
    val Sky = Color(0xFF10142A)
    val SkyLow = Color(0xFF2A2848)
    val Ground = Color(0xFF1E1A28)
    val Gate = Color(0xFFE8C46A)
    val Curse = Color(0xFF6A3A5A)
    val Person = Color(0xFFE6DCC8)
    val Red = Color(0xFFE0584A)
    val Camel = Color(0xFFC8A070)
    val Load = Color(0xFF8A5A3A)
    val Smoke = Color(0xFF2A2430)
}

/**
 * Beats: fournot (four words, each struck out: «بِالطَّعَّانِ», «وَلَا اللَّعَّانِ», «وَلَا الْفَاحِشِ», «وَلَا الْبَذِيِّ»),
 * boomerang (a dart thrown at another turns in the air and comes back to the thrower), shutgates (a dark curse rises: the
 * sky's gates shut before it; it falls: the earth's gates shut; it wanders and goes back to the one who said it), freecamel
 * (a she-camel's load is taken off and she is left to go), noaid (dark smoke winds toward one who has fallen; harsh words
 * thicken it, a kind word thins it away).
 */
internal fun DrawScope.cursereturnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fournot" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "boomerang" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "shutgates" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "freecamel" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "noaid" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { curseNight(a) }
}

private fun DrawScope.curseNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fn = a.b("fournot")
    val bm = a.b("boomerang")
    val sg = a.b("shutgates")
    val fc = a.b("freecamel")
    val na = a.b("noaid")
    val ground = h * 0.84f
    nightSky(t, CurseC49.Sky, CurseC49.SkyLow, ground, starAlpha = 0.7f)
    drawRect(CurseC49.Ground, Offset(0f, ground), Size(w, h - ground))
    if (fn > 0f) {
        val f = easeOut(seg(fn, 0f, BEAT_IN)) * (1f - seg(bm, 0f, BEAT_OUT))
        for (k in 0 until 4) {
            val c = Offset(w * (0.2f + 0.2f * k), h * (0.4f + 0.12f * (k % 2)))
            struckTag21(a.tag("fournot", 0, k), c, f * seg(fn, 0.1f + 0.18f * k, 0.25f + 0.18f * k), seg(fn, 0.2f + 0.18f * k, 0.35f + 0.18f * k), CurseC49.Red, Back49, ArtInk.Ivory)
        }
    }
    if (bm > 0f) {
        val f = easeOut(seg(bm, 0f, BEAT_IN)) * (1f - seg(sg, 0f, 0.4f))
        val thrower = Offset(w * 0.22f, ground)
        figure(thrower, h * 0.3f, CurseC49.Person, f, arms = -0.6f * seg(bm, 0.1f, 0.2f))
        figure(Offset(w * 0.78f, ground), h * 0.3f, CurseC49.Person, f * 0.8f)
        val u = seg(bm, 0.15f, 0.85f)
        val ang = PI.toFloat() * u
        val p = Offset(w * 0.5f - cos(ang) * w * 0.26f, ground - h * 0.28f - sin(ang) * h * 0.18f * (1f - 0.6f * u))
        rotate(720f * u, p) { drawLine(CurseC49.Red, p + Offset(-w * 0.02f, 0f), p + Offset(w * 0.02f, 0f), 3f * dp, StrokeCap.Round, alpha = f * seg(bm, 0.15f, 0.2f)) }
        if (u > 0.95f) softGlow(thrower + Offset(0f, -h * 0.2f), w * 0.04f, CurseC49.Red, 0.6f * f)
    }
    if (sg > 0f) {
        val f = easeOut(seg(sg, 0f, 0.3f)) * (1f - seg(fc, 0f, BEAT_OUT))
        val speaker = Offset(w * 0.22f, ground)
        figure(speaker, h * 0.24f, CurseC49.Person, f)
        for (k in 0 until 3) {
            val x = w * (0.35f + 0.15f * k)
            val shutUp = seg(sg, 0.2f, 0.32f)
            drawRect(CurseC49.Gate, Offset(x - w * 0.05f * (1f - shutUp), h * 0.08f), Size(w * 0.1f * (1f - shutUp) + 2f * dp, h * 0.02f), alpha = f * 0.8f)
            drawRect(CurseC49.Gate, Offset(x - w * 0.05f, h * 0.08f), Size(w * 0.1f, h * 0.02f), alpha = f * shutUp * 0.8f)
            val shutDown = seg(sg, 0.48f, 0.6f)
            drawRect(CurseC49.Gate, Offset(x - w * 0.05f, ground), Size(w * 0.1f, h * 0.015f), alpha = f * shutDown * 0.8f)
        }
        val path = listOf(Offset(w * 0.24f, ground - h * 0.3f), Offset(w * 0.5f, h * 0.12f), Offset(w * 0.6f, ground - h * 0.03f), Offset(w * 0.8f, h * 0.5f), Offset(w * 0.24f, ground - h * 0.28f))
        val u = seg(sg, 0.05f, 0.95f) * (path.size - 1)
        val i = u.toInt().coerceAtMost(path.size - 2)
        val p = path[i] + (path[i + 1] - path[i]) * easeInOut(u - i)
        drawCircle(CurseC49.Curse, w * 0.025f, p, alpha = f)
        softGlow(p, w * 0.04f, CurseC49.Curse, 0.4f * f)
    }
    if (fc > 0f) {
        val f = easeOut(seg(fc, 0f, BEAT_IN)) * (1f - seg(na, 0f, BEAT_OUT))
        val go = easeInOut(seg(fc, 0.55f, 1f))
        val foot = Offset(w * (0.55f + 0.3f * go), ground)
        camel26(foot, h * 0.3f, CurseC49.Camel, f, t, t * 3f * go)
        val off = easeInOut(seg(fc, 0.15f, 0.45f))
        drawRoundRect(CurseC49.Load, Offset(w * 0.5f - w * 0.25f * off, ground - h * 0.27f + h * 0.2f * off), Size(w * 0.12f, h * 0.08f), CornerRadius(3f * dp), alpha = f)
        figure(Offset(w * 0.25f, ground), h * 0.28f, CurseC49.Person, f, arms = 0.5f * (1f - off))
    }
    if (na > 0f) {
        val f = easeOut(seg(na, 0f, 0.3f))
        val fallen = Offset(w * 0.5f, ground)
        sit12(fallen, h * 0.26f, CurseC49.Person, f, 1f)
        val heart = fallen + Offset(0f, -h * 0.18f)
        val thick = seg(na, 0.1f, 0.4f) * (1f - seg(na, 0.6f, 0.9f))
        smoke12(Offset(w * 0.9f, h * 0.2f), heart, 0.3f + 0.7f * thick, t, f * (0.4f + 0.6f * thick), CurseC49.Smoke)
        for (k in 0 until 2) figure(Offset(w * (0.18f + 0.62f * k), ground), h * 0.28f, CurseC49.Person, f * 0.8f, arms = if (k == 0) 0.6f else -0.6f)
        val kind = seg(na, 0.55f, 0.75f)
        softGlow(heart, w * 0.05f, ArtInk.Gold, 0.6f * f * kind)
    }
}
