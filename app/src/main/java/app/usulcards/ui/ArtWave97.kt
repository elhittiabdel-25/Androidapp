package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 97 (2026-11-17): the eight cards of «قبل البعثة» redrawn in the illustrated style (they were boards in wave 95,
 * owner feedback: the identity and the motion had changed). Same beats and tags as before; each beat is a shot in one
 * world (desert night, Makkah at dawn, the Kaaba). The Prophet ﷺ is only his named light; companions and the Mothers
 * of the Believers are calligraphy roundels; ancestors are names on a golden thread; no figure stands for anyone named.
 */

internal object S97 {
    val Sand = Color(0xFFC9A46A)
    val SandDark = Color(0xFF8A6A3E)
    val Dusk = Color(0xFF2A2348)
    val DuskLow = Color(0xFF8A5A5A)
    val Thread = Color(0xFFF2C45A)
    val Night = Color(0xFF0E1530)
    val NightLow = Color(0xFF2A3560)
}

/** A desert floor with two dune lines at [ground]. */
internal fun DrawScope.dunes97(ground: Float, near: Color, far: Color, alpha: Float = 1f) {
    val w = size.width
    val h = size.height
    drawPath(Path().apply {
        moveTo(0f, ground - h * 0.06f)
        cubicTo(w * 0.3f, ground - h * 0.12f, w * 0.55f, ground - h * 0.02f, w, ground - h * 0.08f)
        lineTo(w, h); lineTo(0f, h); close()
    }, far, alpha = alpha)
    drawPath(Path().apply {
        moveTo(0f, ground)
        cubicTo(w * 0.35f, ground - h * 0.05f, w * 0.65f, ground + h * 0.03f, w, ground - h * 0.02f)
        lineTo(w, h); lineTo(0f, h); close()
    }, near, alpha = alpha)
}

/** A name hung on the golden thread: a small gold bead and the name beside it. */
internal fun DrawScope.bead97(c: Offset, tl: TextLayoutResult?, alpha: Float, glow: Float = 0f) {
    if (alpha <= 0.01f) return
    if (glow > 0f) softGlow(c, size.width * 0.05f, Qn.GoldLight, glow * alpha)
    drawCircle(S97.Thread, size.width * 0.012f, c, alpha = alpha)
    tagAt(tl, c + Offset(0f, -size.height * 0.07f), alpha, back = Color(0xCC1A1630), ink = ArtInk.Gold)
}

/** A companion or a Mother of the Believers: her or his name in calligraphy in a roundel of its own colour. */
internal fun DrawScope.close97(c: Offset, r: Float, a: ArtCtx, kind: String, part: Int, k: Int, grow: Float) =
    nameRoundel(c, r, a.big(kind, 0, part) ?: a.tag(kind, 0, part), Roundel.Fills[k % 4], Roundel.Rim, grow)

/** Anyone else named (not one of the closest companions): the plainer roundel, one muted colour. */
internal fun DrawScope.plain97(c: Offset, r: Float, a: ArtCtx, kind: String, part: Int, grow: Float) =
    nameRoundel(c, r, a.big(kind, 0, part) ?: a.tag(kind, 0, part), Roundel.PlainFill, Roundel.PlainRim, grow)

// ═════════════ sb-nasab: his lineage ═════════════

/**
 * Beats: the thread of his lineage descending from his named light (عبد المطلب, هاشم, … عدنان); the thread runs on to
 * إسماعيل and إبراهيم beside the Kaaba they raised; three nested rings close in (كنانة, قريش, بنو هاشم) to the light at
 * their centre; the light rises, chosen.
 */
internal fun DrawScope.sbnasabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sbnasab0" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "sbnasab1" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "sbnasab2" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "sbnasab3" to Shot(Offset(w * 0.5f, h * 0.4f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) {
        val t = a.t
        val b0 = fqIn(a, "sbnasab0", "sbnasab1")
        val b1 = fqIn(a, "sbnasab1", "sbnasab2")
        val b2 = fqIn(a, "sbnasab2", "sbnasab3")
        val b3 = fqIn(a, "sbnasab3")
        val ground = h * 0.86f
        nightSky(t, S97.Night, S97.NightLow, ground)
        dunes97(ground, S97.SandDark, Color(0xFF5A4630))
        if (b0 > 0f) {
            val p = a.b("sbnasab0")
            val top = Offset(w * 0.5f, h * 0.16f)
            sirajMunir(top, w * 0.06f, b0 * seg(p, 0f, 0.2f), t, nameAt = top + Offset(w * 0.12f, 0f))
            val down = easeInOut(seg(p, 0.15f, 0.8f))
            val end = Offset(w * 0.5f, mix(h * 0.2f, h * 0.8f, down))
            drawLine(S97.Thread, top, end, 2.2f * density, StrokeCap.Round, alpha = b0)
            for (k in 0 until 3) {
                val y = h * (0.36f + 0.2f * k)
                bead97(Offset(w * 0.5f, y), a.tag("sbnasab0", 0, k), b0 * seg(p, 0.25f + 0.18f * k, 0.38f + 0.18f * k))
            }
            tagAt(a.tag("sbnasab0", 0, 3), top + Offset(-w * 0.2f, h * 0.02f), b0 * seg(p, 0.05f, 0.2f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
        }
        if (b1 > 0f) {
            val p = a.b("sbnasab1")
            drawRect(Color(0xFF14102A), alpha = 0.55f * b1)
            kaaba29(Offset(w * 0.26f, ground), w * 0.1f, b1 * seg(p, 0f, 0.2f))
            softGlow(Offset(w * 0.26f, ground - h * 0.12f), w * 0.12f, Qn.GoldLight, 0.4f * b1)
            // the thread runs back in time, right to left: عدنان … إسماعيل … إبراهيم
            val run = easeInOut(seg(p, 0.1f, 0.75f))
            val y = h * 0.4f
            drawLine(S97.Thread, Offset(w * 0.86f, y), Offset(mix(w * 0.86f, w * 0.18f, run), y), 2.2f * density, StrokeCap.Round, alpha = b1)
            val xs = listOf(0.22f, 0.5f, 0.8f)
            for (k in 0 until 3) bead97(Offset(w * xs[k], y), a.tag("sbnasab1", 0, k), b1 * seg(p, 0.6f - 0.2f * k, 0.72f - 0.2f * k), glow = if (k < 2) 0.4f else 0f)
        }
        if (b2 > 0f) {
            val p = a.b("sbnasab2")
            drawRect(Color(0xFF14102A), alpha = 0.6f * b2)
            val c = Offset(w * 0.5f, h * 0.52f)
            val rs = listOf(0.4f, 0.27f, 0.15f)
            val tones = listOf(Color(0xFF3A3F6A), Color(0xFF4A3A6A), Color(0xFF6A4A3A))
            for (k in 0 until 3) {
                val on = seg(p, 0.08f + 0.2f * k, 0.25f + 0.2f * k)
                val r = h * rs[k] * (1.15f - 0.15f * easeOut(on))
                drawCircle(tones[k], r, c, alpha = 0.55f * on * b2)
                drawCircle(S97.Thread, r, c, alpha = on * b2, style = Stroke(1.6f * density))
                tagAt(a.tag("sbnasab2", 0, k), c + Offset(0f, -r + h * 0.035f), on * b2, back = Color(0xCC1A1630), ink = ArtInk.Gold)
            }
            sirajMunir(c + Offset(0f, h * 0.04f), w * 0.04f, b2 * seg(p, 0.7f, 0.9f), t, nameAt = c + Offset(0f, h * 0.13f))
        }
        if (b3 > 0f) {
            val p = a.b("sbnasab3")
            drawRect(Color(0xFF14102A), alpha = 0.6f * b3)
            val rise = easeInOut(seg(p, 0.1f, 0.6f))
            val c = Offset(w * 0.5f, mix(h * 0.6f, h * 0.36f, rise))
            for (k in 0 until 12) {
                val ang = k * PI.toFloat() / 6f + t * 0.1f
                drawLine(Qn.GoldLight, c + Offset(cos(ang), sin(ang)) * (w * 0.09f), c + Offset(cos(ang), sin(ang)) * (w * 0.16f), 1.4f * density, StrokeCap.Round, alpha = 0.5f * b3 * rise)
            }
            sirajMunir(c, w * 0.07f, b3, t, nameAt = c + Offset(0f, h * 0.2f))
            tagAt(a.tag("sbnasab3", 0, 0), Offset(w * 0.5f, h * 0.1f), b3 * seg(p, 0.5f, 0.7f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
    }
}

// ═════════════ sb-mawlid: his birth ═════════════

/**
 * Beats: a Monday dawn over Makkah in Rabi' al-Awwal of the Year of the Elephant (the crescent, then the sun); the
 * Kaaba stands while Abraha's army (a dark wave named «أبرهة») breaks and draws back; a light goes out from Makkah to
 * the palaces of Busra far north; a question over the celebration: no companion and no early generation did it.
 */
internal fun DrawScope.sbmawlidScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sbmawlid0" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sbmawlid1" to Shot(Offset(w * 0.45f, h * 0.6f), 1.08f),
        "sbmawlid2" to Shot(Offset(w * 0.55f, h * 0.45f), 1f),
        "sbmawlid3" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) {
        val t = a.t
        val b0 = fqIn(a, "sbmawlid0", "sbmawlid1")
        val b1 = fqIn(a, "sbmawlid1", "sbmawlid2")
        val b2 = fqIn(a, "sbmawlid2", "sbmawlid3")
        val b3 = fqIn(a, "sbmawlid3")
        val ground = h * 0.84f
        val p0 = a.b("sbmawlid0")
        val dawn = easeInOut(seg(p0, 0.2f, 0.8f))
        daySky12(lerpColor(S97.Dusk, Qn.DayTop, dawn), lerpColor(S97.DuskLow, Color(0xFFF2D6A0), dawn), ground, S97.SandDark)
        for (k in 0 until 4) drawPath(Path().apply {
            val x0 = w * (k * 0.28f - 0.05f)
            moveTo(x0, ground); lineTo(x0 + w * 0.14f, ground - h * (0.18f + 0.05f * (k % 2))); lineTo(x0 + w * 0.3f, ground); close()
        }, Color(0xFF6A5040), alpha = 0.8f)
        if (b0 > 0f) {
            softGlow(Offset(w * 0.5f, ground), w * 0.3f * dawn, Color(0xFFFFE2A0), 0.6f * b0 * dawn)
            drawCircle(Color(0xFFFFF0C8), w * 0.035f, Offset(w * 0.5f, mix(ground + h * 0.02f, ground - h * 0.12f, dawn)), alpha = b0 * dawn)
            drawPath(Path().apply {
                val c = Offset(w * 0.8f, h * 0.18f)
                addOval(androidx.compose.ui.geometry.Rect(c, w * 0.03f))
            }, Color(0xFFF6E7BE), alpha = b0 * (1f - dawn))
            for (k in 0 until 3) tagAt(a.tag("sbmawlid0", 0, k), Offset(w * (0.2f + 0.3f * k), h * 0.14f), b0 * seg(p0, 0.25f + 0.2f * k, 0.4f + 0.2f * k), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
        if (b1 > 0f) {
            val p = a.b("sbmawlid1")
            kaaba29(Offset(w * 0.32f, ground), w * 0.12f, b1)
            softGlow(Offset(w * 0.32f, ground - h * 0.14f), w * 0.16f, Qn.GoldLight, 0.5f * b1 * seg(p, 0.4f, 0.7f))
            // the army: a dark mass that advances, then breaks up and recedes
            val come = easeInOut(seg(p, 0f, 0.4f))
            val back = easeInOut(seg(p, 0.5f, 0.95f))
            val x = w * mix(mix(1.1f, 0.62f, come), 1.2f, back)
            for (k in 0 until 7) {
                val dx = (k % 4) * w * 0.035f
                val dy = (k / 4) * h * 0.03f
                drawCircle(Color(0xFF2A2030), w * 0.022f * (1f - 0.5f * back), Offset(x + dx, ground - h * 0.04f - dy), alpha = b1 * (1f - back))
            }
            tagAt(a.tag("sbmawlid1", 0, 1), Offset(x + w * 0.05f, ground - h * 0.16f), b1 * seg(p, 0.15f, 0.3f) * (1f - back), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("sbmawlid1", 0, 0), Offset(w * 0.32f, ground - h * 0.32f), b1 * seg(p, 0.6f, 0.8f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
        if (b2 > 0f) {
            val p = a.b("sbmawlid2")
            drawRect(S97.Night, alpha = 0.65f * b2)
            val from = Offset(w * 0.2f, ground - h * 0.05f)
            val to = Offset(w * 0.82f, h * 0.36f)
            val go = easeInOut(seg(p, 0.1f, 0.7f))
            val head = Offset(mix(from.x, to.x, go), mix(from.y, to.y, go))
            drawLine(Qn.GoldLight, from, head, 3f * density, StrokeCap.Round, alpha = 0.7f * b2)
            softGlow(head, w * 0.06f, Qn.GoldLight, 0.8f * b2)
            softGlow(from, w * 0.05f, Qn.GoldLight, 0.6f * b2)
            // the palaces of Busra
            for (k in 0 until 3) house12(Offset(w * (0.76f + 0.06f * k), h * 0.48f), w * 0.07f, Color(0xFFE2CCA0), Color(0xFF8A6A40), b2 * seg(p, 0.5f, 0.7f), lit = seg(p, 0.65f, 0.85f))
            tagAt(a.tag("sbmawlid2", 0, 0), from + Offset(0f, -h * 0.1f), b2 * seg(p, 0.05f, 0.2f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
            tagAt(a.tag("sbmawlid2", 0, 1), Offset(w * 0.82f, h * 0.22f), b2 * seg(p, 0.7f, 0.85f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
        }
        if (b3 > 0f) {
            val p = a.b("sbmawlid3")
            drawRect(Color(0xFF14102A), alpha = 0.8f * b3)
            // the three best generations as three empty lamps-less rows: no celebration among them
            for (k in 0 until 3) {
                val y = h * (0.36f + 0.16f * k)
                val on = seg(p, 0.1f + 0.15f * k, 0.25f + 0.15f * k)
                drawRoundRect(Color(0xFF2A2848), Offset(w * 0.2f, y - h * 0.05f), Size(w * 0.6f, h * 0.1f), CornerRadius(8f * density), alpha = b3 * on)
                for (j in 0 until 5) drawCircle(Qn.Person, w * 0.012f, Offset(w * (0.28f + 0.11f * j), y), alpha = 0.8f * b3 * on)
                drawLine(Qn.Ember, Offset(w * 0.22f, y), Offset(w * 0.78f, y), 2f * density, StrokeCap.Round, alpha = b3 * on * seg(p, 0.6f, 0.8f))
            }
            tagAt(a.tag("sbmawlid3", 0, 2), Offset(w * 0.5f, h * 0.14f), b3 * seg(p, 0.05f, 0.2f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
            tagAt(a.tag("sbmawlid3", 0, 1), Offset(w * 0.5f, h * 0.86f), b3 * seg(p, 0.6f, 0.75f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
        }
    }
}

// ═════════════ sb-ridaa: his nursing ═════════════

/**
 * Beats: two roundels, ثويبة and حمزة (his foster-brother); the women of بنو سعد ride out from Makkah into the desert
 * and the roundel of حليمة السعدية receives his light; her tent in the desert, the dry land greens and the she-camel's
 * milk flows; four years turn over the tent (four moons).
 */
internal fun DrawScope.sbridaaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sbridaa0" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "sbridaa1" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "sbridaa2" to Shot(Offset(w * 0.45f, h * 0.6f), 1.08f),
        "sbridaa3" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) {
        val t = a.t
        val b0 = fqIn(a, "sbridaa0", "sbridaa1")
        val b1 = fqIn(a, "sbridaa1", "sbridaa2")
        val b2 = fqIn(a, "sbridaa2", "sbridaa3")
        val b3 = fqIn(a, "sbridaa3")
        val ground = h * 0.84f
        daySky12(Qn.DayTop, Color(0xFFF2D6A0), ground, S97.Sand)
        dunes97(ground, S97.Sand, Color(0xFFB8925A))
        if (b0 > 0f) {
            val p = a.b("sbridaa0")
            drawRect(Color(0xFF1E2234), alpha = 0.5f * b0)
            sirajMunir(Offset(w * 0.5f, h * 0.36f), w * 0.04f, b0 * seg(p, 0f, 0.2f), t, nameAt = Offset(w * 0.5f, h * 0.5f))
            plain97(Offset(w * 0.24f, h * 0.62f), w * 0.08f, a, "sbridaa0", 0, b0 * seg(p, 0.15f, 0.4f))
            close97(Offset(w * 0.76f, h * 0.62f), w * 0.08f, a, "sbridaa0", 1, 2, b0 * seg(p, 0.5f, 0.75f))
        }
        if (b1 > 0f) {
            val p = a.b("sbridaa1")
            kaaba29(Offset(w * 0.12f, ground), w * 0.06f, b1)
            val go = easeInOut(seg(p, 0.05f, 0.6f))
            for (k in 0 until 3) camel26(Offset(mix(w * 0.18f, w * 0.62f, go) + k * w * 0.08f, ground - h * 0.01f), h * 0.12f, Color(0xFF5A4030), b1, t, walk = t * 2f)
            tagAt(a.tag("sbridaa1", 0, 0), Offset(w * 0.42f, h * 0.48f), b1 * seg(p, 0.1f, 0.25f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
            plain97(Offset(w * 0.8f, h * 0.36f), w * 0.08f, a, "sbridaa1", 1, b1 * seg(p, 0.55f, 0.8f))
            sirajMunir(Offset(w * 0.8f, h * 0.36f) + Offset(0f, h * 0.18f), w * 0.025f, b1 * seg(p, 0.7f, 0.9f), t, named = false)
        }
        if (b2 > 0f) {
            val p = a.b("sbridaa2")
            val green = easeInOut(seg(p, 0.2f, 0.8f))
            drawRect(Color(0xFF6A8A4A), Offset(0f, ground), Size(w, h - ground), alpha = 0.7f * b2 * green)
            for (k in 0 until 9) {
                val x = w * (0.08f + 0.1f * k)
                drawLine(Qn.Leaf, Offset(x, ground + h * 0.04f), Offset(x + w * 0.01f, ground + h * 0.04f - h * 0.05f * green), 2f * density, StrokeCap.Round, alpha = b2 * green)
            }
            // the tent and the camel whose milk flowed
            drawPath(Path().apply { moveTo(w * 0.3f, ground); lineTo(w * 0.42f, ground - h * 0.2f); lineTo(w * 0.56f, ground); close() }, Color(0xFF6A4A30), alpha = b2)
            camel26(Offset(w * 0.74f, ground), h * 0.16f, Color(0xFF7A5A3A), b2, t)
            for (k in 0 until 3) drawCircle(Color(0xFFFFFAF0), w * 0.006f, Offset(w * 0.72f, ground - h * 0.03f + h * 0.02f * ((t * 1.5f + k * 0.33f) % 1f)), alpha = b2 * seg(p, 0.5f, 0.7f))
            tagAt(a.tag("sbridaa2", 0, 0), Offset(w * 0.43f, ground - h * 0.27f), b2 * seg(p, 0.05f, 0.2f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
            tagAt(a.tag("sbridaa2", 0, 1), Offset(w * 0.5f, h * 0.14f), b2 * seg(p, 0.55f, 0.7f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        }
        if (b3 > 0f) {
            val p = a.b("sbridaa3")
            drawRect(S97.Night, alpha = 0.7f * b3)
            drawPath(Path().apply { moveTo(w * 0.38f, ground); lineTo(w * 0.5f, ground - h * 0.22f); lineTo(w * 0.62f, ground); close() }, Color(0xFF4A3424), alpha = b3)
            for (k in 0 until 4) {
                val on = seg(p, 0.1f + 0.18f * k, 0.25f + 0.18f * k)
                val ang = PI.toFloat() * (0.9f - 0.27f * k)
                val c = Offset(w * 0.5f + cos(ang) * w * 0.3f, h * 0.6f - sin(ang) * h * 0.42f)
                drawCircle(Color(0xFFF6E7BE), w * 0.022f, c, alpha = b3 * on)
                drawCircle(S97.Night, w * 0.02f, c + Offset(w * 0.01f, 0f), alpha = b3 * on)
            }
            tagAt(a.tag("sbridaa3", 0, 1), Offset(w * 0.5f, h * 0.1f), b3 * seg(p, 0.05f, 0.2f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
            tagAt(a.tag("sbridaa3", 0, 0), Offset(w * 0.5f, h * 0.92f), b3 * seg(p, 0.75f, 0.9f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
    }
}

// ═════════════ sb-sharh: the opening of his chest ═════════════

/**
 * Beats: two hearts of light, «مرتين»; the first time in the desert of بنو سعد, at four (a tent, a small light); the
 * second on the night of the Isra' (a night sky, the crescent and a star path upward); a golden basin, Zamzam water
 * poured into it and the heart of light made bright (Jibril as a cool white light beside it, never a figure).
 */
internal fun DrawScope.sbsharhScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sbsharh0" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "sbsharh1" to Shot(Offset(w * 0.4f, h * 0.6f), 1.05f),
        "sbsharh2" to Shot(Offset(w * 0.6f, h * 0.4f), 1.05f),
        "sbsharh3" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) {
        val t = a.t
        val b0 = fqIn(a, "sbsharh0", "sbsharh1")
        val b1 = fqIn(a, "sbsharh1", "sbsharh2")
        val b2 = fqIn(a, "sbsharh2", "sbsharh3")
        val b3 = fqIn(a, "sbsharh3")
        val ground = h * 0.84f
        nightSky(t, S97.Night, S97.NightLow, ground)
        dunes97(ground, S97.SandDark, Color(0xFF5A4630))
        if (b0 > 0f) {
            val p = a.b("sbsharh0")
            for (k in 0 until 2) {
                val c = Offset(w * (0.35f + 0.3f * k), h * 0.5f)
                val on = seg(p, 0.1f + 0.3f * k, 0.3f + 0.3f * k)
                softGlow(c, w * 0.1f, Qn.GoldLight, 0.6f * b0 * on)
                qIcon("heart", c, w * 0.12f, Qn.GoldLight, b0 * on, t)
            }
            tagAt(a.tag("sbsharh0", 0, 0), Offset(w * 0.5f, h * 0.18f), b0 * seg(p, 0f, 0.15f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
            tagAt(a.tag("sbsharh0", 0, 1), Offset(w * 0.5f, h * 0.78f), b0 * seg(p, 0.7f, 0.85f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
        }
        if (b1 > 0f) {
            val p = a.b("sbsharh1")
            val day = easeInOut(seg(p, 0f, 0.3f))
            drawRect(Color(0xFFE8C88A), alpha = 0.55f * b1 * day)
            drawPath(Path().apply { moveTo(w * 0.22f, ground); lineTo(w * 0.34f, ground - h * 0.2f); lineTo(w * 0.48f, ground); close() }, Color(0xFF6A4A30), alpha = b1)
            sirajMunir(Offset(w * 0.6f, ground - h * 0.12f), w * 0.025f, b1 * seg(p, 0.2f, 0.4f), t, named = false)
            tagAt(a.tag("sbsharh1", 0, 2), Offset(w * 0.5f, h * 0.12f), b1 * seg(p, 0.05f, 0.2f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
            tagAt(a.tag("sbsharh1", 0, 0), Offset(w * 0.34f, ground - h * 0.28f), b1 * seg(p, 0.3f, 0.45f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
            tagAt(a.tag("sbsharh1", 0, 1), Offset(w * 0.62f, ground - h * 0.25f), b1 * seg(p, 0.55f, 0.7f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
        }
        if (b2 > 0f) {
            val p = a.b("sbsharh2")
            drawRect(S97.Night, alpha = 0.6f * b2)
            drawCircle(Color(0xFFF6E7BE), w * 0.04f, Offset(w * 0.78f, h * 0.2f), alpha = b2)
            drawCircle(S97.Night, w * 0.037f, Offset(w * 0.795f, h * 0.19f), alpha = b2)
            val up = easeInOut(seg(p, 0.2f, 0.9f))
            for (k in 0 until 7) {
                val f = k / 6f
                drawCircle(Qn.GoldLight, w * 0.006f, Offset(mix(w * 0.3f, w * 0.6f, f), mix(h * 0.75f, h * 0.15f, f)), alpha = b2 * seg(up, f - 0.1f, f))
            }
            tagAt(a.tag("sbsharh2", 0, 2), Offset(w * 0.3f, h * 0.14f), b2 * seg(p, 0.05f, 0.2f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
            tagAt(a.tag("sbsharh2", 0, 0), Offset(w * 0.78f, h * 0.33f), b2 * seg(p, 0.25f, 0.4f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
        }
        if (b3 > 0f) {
            val p = a.b("sbsharh3")
            drawRect(S97.Night, alpha = 0.75f * b3)
            val basin = Offset(w * 0.5f, h * 0.7f)
            drawArc(Qn.Gold, 0f, 180f, true, Offset(basin.x - w * 0.16f, basin.y - h * 0.12f), Size(w * 0.32f, h * 0.24f), alpha = b3)
            drawLine(Color(0xFFFFE9A8), basin + Offset(-w * 0.16f, 0f), basin + Offset(w * 0.16f, 0f), 2.5f * density, alpha = b3)
            // Jibril: a cool white light (never wings, never a figure)
            softGlow(Offset(w * 0.2f, h * 0.3f), w * 0.1f, Color(0xFFDDEBFF), 0.7f * b3 * seg(p, 0.05f, 0.25f))
            drawCircle(Color(0xFFF2F8FF), w * 0.025f, Offset(w * 0.2f, h * 0.3f), alpha = b3 * seg(p, 0.05f, 0.25f))
            val pour = seg(p, 0.35f, 0.75f)
            for (k in 0 until 6) {
                val f = ((t * 1.4f + k / 6f) % 1f)
                drawCircle(Color(0xFF9AD0F0), w * 0.007f, Offset(w * 0.5f + w * 0.01f * sin(k.toFloat()), mix(h * 0.32f, h * 0.68f, f)), alpha = b3 * pour)
            }
            val bright = seg(p, 0.6f, 0.9f)
            softGlow(Offset(w * 0.5f, h * 0.5f), w * 0.12f, Qn.GoldLight, b3 * bright)
            qIcon("heart", Offset(w * 0.5f, h * 0.5f), w * 0.11f, lerpColor(Qn.Rose, Qn.GoldLight, bright), b3 * seg(p, 0.15f, 0.3f), t)
            tagAt(a.tag("sbsharh3", 0, 1), basin + Offset(0f, h * 0.17f), b3 * seg(p, 0.2f, 0.35f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
            tagAt(a.tag("sbsharh3", 0, 2), Offset(w * 0.78f, h * 0.36f), b3 * seg(p, 0.45f, 0.6f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        }
    }
}

// ═════════════ sb-yutm: an orphan ═════════════

/**
 * Beats: a small named light alone under a wide night (his father's roundel already dimmed); the road back from Madinah,
 * his mother's roundel fading at al-Abwa', six moons, then six years; the light passes from the roundel of عبد المطلب to
 * that of أبو طالب as eight years turn; a warm circle of his grandfather's tenderness around the light.
 */
internal fun DrawScope.sbyutmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sbyutm0" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "sbyutm1" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "sbyutm2" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sbyutm3" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) {
        val t = a.t
        val b0 = fqIn(a, "sbyutm0", "sbyutm1")
        val b1 = fqIn(a, "sbyutm1", "sbyutm2")
        val b2 = fqIn(a, "sbyutm2", "sbyutm3")
        val b3 = fqIn(a, "sbyutm3")
        val ground = h * 0.86f
        nightSky(t, Color(0xFF0A1028), Color(0xFF22305A), ground)
        dunes97(ground, Color(0xFF3A2E22), Color(0xFF2A2218))
        if (b0 > 0f) {
            val p = a.b("sbyutm0")
            sirajMunir(Offset(w * 0.5f, h * 0.6f), w * 0.035f, b0, t, nameAt = Offset(w * 0.5f, h * 0.74f))
            plain97(Offset(w * 0.78f, h * 0.3f), w * 0.07f, a, "sbyutm0", 1, b0 * seg(p, 0.2f, 0.4f) * (1f - 0.6f * seg(p, 0.5f, 0.8f)))
            tagAt(a.tag("sbyutm0", 0, 2), Offset(w * 0.3f, h * 0.2f), b0 * seg(p, 0.6f, 0.75f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
        }
        if (b1 > 0f) {
            val p = a.b("sbyutm1")
            val road = easeInOut(seg(p, 0f, 0.5f))
            drawLine(Color(0xFF8A7458), Offset(w * 0.9f, ground - h * 0.02f), Offset(mix(w * 0.9f, w * 0.15f, road), ground - h * 0.02f), 3f * density, StrokeCap.Round, alpha = b1)
            plain97(Offset(w * 0.4f, h * 0.42f), w * 0.08f, a, "sbyutm1", 0, b1 * seg(p, 0.1f, 0.3f) * (1f - 0.65f * seg(p, 0.55f, 0.85f)))
            sirajMunir(Offset(w * 0.62f, h * 0.62f), w * 0.03f, b1, t, named = false)
            tagAt(a.tag("sbyutm1", 0, 1), Offset(w * 0.75f, h * 0.18f), b1 * seg(p, 0.6f, 0.75f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
        if (b2 > 0f) {
            val p = a.b("sbyutm2")
            drawRect(S97.Night, alpha = 0.55f * b2)
            plain97(Offset(w * 0.25f, h * 0.45f), w * 0.09f, a, "sbyutm2", 0, b2 * seg(p, 0.05f, 0.2f) * (1f - 0.6f * seg(p, 0.55f, 0.75f)))
            plain97(Offset(w * 0.75f, h * 0.45f), w * 0.09f, a, "sbyutm2", 1, b2 * seg(p, 0.5f, 0.7f))
            val move = easeInOut(seg(p, 0.45f, 0.8f))
            val c = Offset(mix(w * 0.25f, w * 0.75f, move), h * 0.68f)
            sirajMunir(c, w * 0.03f, b2, t, named = false)
            tagAt(a.tag("sbyutm2", 0, 2), Offset(w * 0.5f, h * 0.15f), b2 * seg(p, 0.4f, 0.55f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
        if (b3 > 0f) {
            val p = a.b("sbyutm3")
            drawRect(S97.Night, alpha = 0.6f * b3)
            val c = Offset(w * 0.5f, h * 0.55f)
            val warm = easeInOut(seg(p, 0.1f, 0.6f))
            softGlow(c, w * 0.28f * warm, Color(0xFFFFB070), 0.5f * b3)
            drawCircle(Color(0xFFFFC890), w * 0.2f * warm, c, alpha = 0.8f * b3, style = Stroke(2f * density))
            sirajMunir(c, w * 0.035f, b3, t, nameAt = c + Offset(0f, h * 0.16f))
            tagAt(a.tag("sbyutm3", 0, 1), Offset(w * 0.5f, h * 0.12f), b3 * seg(p, 0.05f, 0.2f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
            tagAt(a.tag("sbyutm3", 0, 0), c + Offset(w * 0.24f, -h * 0.12f), b3 * seg(p, 0.5f, 0.65f), back = Color(0xCC402818), ink = ArtInk.Gold)
        }
    }
}

// ═════════════ sb-hifz: his protection before prophethood ═════════════

/**
 * Beats: his named light among the Makkans, a crown of noble character above it; three things he never touched (wine,
 * meat slaughtered on idol stones, the idols' feasts) set apart and struck through; the house of ابن جدعان where the
 * clans gathered for the pact; the scale of justice settling: the wronged person's right returned.
 */
internal fun DrawScope.sbhifzScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sbhifz0" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sbhifz1" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "sbhifz2" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "sbhifz3" to Shot(Offset(w * 0.5f, h * 0.5f), 1.08f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) {
        val t = a.t
        val b0 = fqIn(a, "sbhifz0", "sbhifz1")
        val b1 = fqIn(a, "sbhifz1", "sbhifz2")
        val b2 = fqIn(a, "sbhifz2", "sbhifz3")
        val b3 = fqIn(a, "sbhifz3")
        val ground = h * 0.84f
        daySky12(Color(0xFF5A6A9A), Color(0xFFE8C8A0), ground, Color(0xFF8A7458))
        for (k in 0 until 5) house12(Offset(w * (0.08f + 0.21f * k), ground), w * 0.12f, Color(0xFFD8C0A0), Color(0xFF8A6A40), 0.8f)
        if (b0 > 0f) {
            val p = a.b("sbhifz0")
            for (k in 0 until 6) figure(Offset(w * (0.1f + 0.16f * k), ground), h * 0.12f, Qn.Person, b0 * 0.7f)
            val c = Offset(w * 0.5f, h * 0.5f)
            sirajMunir(c, w * 0.045f, b0 * seg(p, 0f, 0.2f), t, nameAt = c + Offset(0f, h * 0.15f))
            qIcon("crown", c + Offset(0f, -h * 0.2f), w * 0.08f, Qn.GoldLight, b0 * seg(p, 0.4f, 0.6f), t)
            tagAt(a.tag("sbhifz0", 0, 1), c + Offset(0f, -h * 0.32f), b0 * seg(p, 0.5f, 0.65f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
        if (b1 > 0f) {
            val p = a.b("sbhifz1")
            drawRect(S97.Night, alpha = 0.6f * b1)
            val icons = listOf("cup", "flame", "mountain")
            for (k in 0 until 3) {
                val c = Offset(w * (0.2f + 0.3f * k), h * 0.5f)
                val on = seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k)
                qIcon(icons[k], c, w * 0.11f, Color(0xFFB0A090), b1 * on, t)
                val x = seg(p, 0.25f + 0.2f * k, 0.4f + 0.2f * k)
                drawLine(Qn.Ember, c + Offset(-w * 0.07f, -h * 0.1f), c + Offset(-w * 0.07f + w * 0.14f * x, -h * 0.1f + h * 0.2f * x), 3f * density, StrokeCap.Round, alpha = b1)
                tagAt(a.tag("sbhifz1", 0, k), c + Offset(0f, h * 0.18f), b1 * on, back = Color(0xCC401A20), ink = ArtInk.Ivory)
            }
        }
        if (b2 > 0f) {
            val p = a.b("sbhifz2")
            drawRect(Color(0xFF1E2234), alpha = 0.55f * b2)
            house12(Offset(w * 0.5f, ground), w * 0.34f, Color(0xFFE2CCA0), Color(0xFF8A6A40), b2, lit = seg(p, 0.3f, 0.6f))
            val gather = easeInOut(seg(p, 0.1f, 0.6f))
            for (k in 0 until 8) {
                val side = if (k % 2 == 0) -1f else 1f
                val x = w * 0.5f + side * mix(w * 0.45f, w * (0.2f + 0.03f * (k / 2)), gather)
                figure(Offset(x, ground), h * 0.11f, Qn.Person, b2, walk = t * 2f * (1f - gather))
            }
            tagAt(a.tag("sbhifz2", 0, 0), Offset(w * 0.5f, ground - h * 0.42f), b2 * seg(p, 0.05f, 0.2f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
            tagAt(a.tag("sbhifz2", 0, 2), Offset(w * 0.5f, h * 0.1f), b2 * seg(p, 0.55f, 0.7f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
        if (b3 > 0f) {
            val p = a.b("sbhifz3")
            drawRect(S97.Night, alpha = 0.7f * b3)
            val foot = Offset(w * 0.5f, h * 0.86f)
            val tilt = 0.6f * (1f - easeInOut(seg(p, 0.35f, 0.75f)))
            balance12(foot, h * 0.55f, tilt, Qn.GoldLight, b3)
            val (l, r) = balancePans12(foot, h * 0.55f, tilt)
            figure(l + Offset(0f, -h * 0.005f), h * 0.1f, Qn.Person, b3 * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("sbhifz3", 0, 0), l + Offset(0f, -h * 0.17f), b3 * seg(p, 0.15f, 0.3f), back = Color(0xCC401A20), ink = ArtInk.Ivory)
            tagAt(a.tag("sbhifz3", 0, 1), Offset(w * 0.5f, h * 0.1f), b3 * seg(p, 0.7f, 0.85f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        }
    }
}

// ═════════════ sb-kaaba: rebuilding the Kaaba ═════════════

/**
 * Beats: Quraysh raise the walls of the Kaaba course by course while stones are carried up (his named light among them,
 * the roundel of العباس beside it); العباس's advice, in his words; his ﷺ words «إِزَارِي» as the light steadies; a soft
 * veil of protection closes round the light: Allah guarded him before the mission.
 */
internal fun DrawScope.sbkaabaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sbkaaba0" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "sbkaaba1" to Shot(Offset(w * 0.6f, h * 0.5f), 1.08f),
        "sbkaaba2" to Shot(Offset(w * 0.45f, h * 0.55f), 1.12f),
        "sbkaaba3" to Shot(Offset(w * 0.45f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) {
        val t = a.t
        val b0 = fqIn(a, "sbkaaba0", "sbkaaba1")
        val b1 = fqIn(a, "sbkaaba1", "sbkaaba2")
        val b2 = fqIn(a, "sbkaaba2", "sbkaaba3")
        val b3 = fqIn(a, "sbkaaba3")
        val ground = h * 0.86f
        daySky12(Qn.DayTop, Color(0xFFF2D6A0), ground, Color(0xFF9A8060))
        val p0 = a.b("sbkaaba0")
        // the walls rising, course by course
        val rise = if (b0 > 0f) easeInOut(seg(p0, 0f, 0.7f)) else 1f
        val kx = w * 0.3f
        for (k in 0 until 6) {
            val on = seg(rise, k / 6f, (k + 1) / 6f)
            drawRect(Color(0xFF5A4A3A), Offset(kx - w * 0.14f, ground - h * 0.06f * (k + 1)), Size(w * 0.28f, h * 0.055f), alpha = on)
            drawLine(Color(0xFF3A2E24), Offset(kx - w * 0.14f, ground - h * 0.06f * (k + 1)), Offset(kx + w * 0.14f, ground - h * 0.06f * (k + 1)), 1f * density, alpha = on)
        }
        if (b0 > 0f) {
            for (k in 0 until 5) figure(Offset(w * (0.5f + 0.09f * k), ground), h * 0.12f, Qn.Person, b0, arms = 0.7f, walk = t * 2f)
            for (k in 0 until 3) drawRoundRect(Color(0xFF8A7A6A), Offset(w * (0.53f + 0.18f * k), ground - h * 0.17f - h * 0.01f * sin(t * 3f + k)), Size(w * 0.04f, h * 0.035f), CornerRadius(3f * density), alpha = b0)
            sirajMunir(Offset(w * 0.62f, h * 0.42f), w * 0.03f, b0 * seg(p0, 0.3f, 0.5f), t, nameAt = Offset(w * 0.62f, h * 0.54f))
            close97(Offset(w * 0.84f, h * 0.36f), w * 0.07f, a, "sbkaaba0", 2, 2, b0 * seg(p0, 0.5f, 0.7f))
            tagAt(a.tag("sbkaaba0", 0, 0), Offset(kx, ground - h * 0.45f), b0 * seg(p0, 0.6f, 0.75f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
        if (b1 > 0f) {
            val p = a.b("sbkaaba1")
            drawRect(Color(0xFF1E2234), alpha = 0.55f * b1)
            close97(Offset(w * 0.72f, h * 0.42f), w * 0.1f, a, "sbkaaba1", 0, 2, b1)
            sirajMunir(Offset(w * 0.4f, h * 0.48f), w * 0.035f, b1, t, named = false)
            tagAt(a.tag("sbkaaba1", 0, 1), Offset(w * 0.56f, h * 0.18f), b1 * seg(p, 0.25f, 0.4f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
        }
        if (b2 > 0f) {
            val p = a.b("sbkaaba2")
            drawRect(Color(0xFF1E2234), alpha = 0.6f * b2)
            val c = Offset(w * 0.45f, h * 0.5f)
            val steady = easeInOut(seg(p, 0.2f, 0.7f))
            sirajMunir(c + Offset(w * 0.02f * sin(t * 9f) * (1f - steady), 0f), w * 0.04f, b2 * (0.5f + 0.5f * steady), t, nameAt = c + Offset(0f, h * 0.15f))
            tagAt(a.tag("sbkaaba2", 0, 0), c + Offset(0f, -h * 0.2f), b2 * seg(p, 0.3f, 0.45f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
        if (b3 > 0f) {
            val p = a.b("sbkaaba3")
            drawRect(S97.Night, alpha = 0.55f * b3)
            val c = Offset(w * 0.45f, h * 0.5f)
            val close = easeInOut(seg(p, 0.1f, 0.6f))
            for (k in 0 until 3) drawCircle(Qn.Jade, w * (0.1f + 0.05f * k) * (1.6f - 0.6f * close), c, alpha = 0.5f * b3 * close, style = Stroke((2.5f - 0.6f * k) * density))
            sirajMunir(c, w * 0.04f, b3, t, nameAt = c + Offset(0f, h * 0.2f))
            tagAt(a.tag("sbkaaba3", 0, 0), c + Offset(w * 0.28f, -h * 0.16f), b3 * seg(p, 0.5f, 0.65f), back = Color(0xCC1A3020), ink = ArtInk.Jade)
        }
    }
}

// ═════════════ sb-khadija: trade and marriage ═════════════

/**
 * Beats: a caravan leaves Makkah for Syria carrying خديجة's goods; at the market his truthful speech, the honest scale and
 * the goods multiplying (blessing); the roundel of خديجة and, through نفيسة, the offer reaching his light; the roundels of
 * his children appear around them one by one.
 */
internal fun DrawScope.sbkhadijaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sbkhadija0" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "sbkhadija1" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "sbkhadija2" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sbkhadija3" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) {
        val t = a.t
        val b0 = fqIn(a, "sbkhadija0", "sbkhadija1")
        val b1 = fqIn(a, "sbkhadija1", "sbkhadija2")
        val b2 = fqIn(a, "sbkhadija2", "sbkhadija3")
        val b3 = fqIn(a, "sbkhadija3")
        val ground = h * 0.84f
        daySky12(Qn.DayTop, Color(0xFFF2D6A0), ground, S97.Sand)
        dunes97(ground, S97.Sand, Color(0xFFB8925A))
        if (b0 > 0f) {
            val p = a.b("sbkhadija0")
            kaaba29(Offset(w * 0.1f, ground), w * 0.06f, b0)
            for (k in 0 until 3) drawPath(Path().apply { val x = w * (0.72f + 0.1f * k); moveTo(x, ground - h * 0.02f); lineTo(x + w * 0.06f, ground - h * 0.2f); lineTo(x + w * 0.12f, ground - h * 0.02f); close() }, Color(0xFF7A8A9A), alpha = b0 * 0.8f)
            val go = easeInOut(seg(p, 0.05f, 0.85f))
            for (k in 0 until 3) {
                val foot = Offset(mix(w * 0.18f, w * 0.55f, go) + k * w * 0.08f, ground)
                camel26(foot, h * 0.13f, Color(0xFF6A4A30), b0, t, walk = t * 2f)
                drawRoundRect(Qn.Gold, Offset(foot.x - w * 0.02f, foot.y - h * 0.15f), Size(w * 0.04f, h * 0.03f), CornerRadius(3f * density), alpha = b0)
            }
            sirajMunir(Offset(mix(w * 0.2f, w * 0.57f, go), ground - h * 0.25f), w * 0.022f, b0, t, named = false)
            tagAt(a.tag("sbkhadija0", 0, 0), Offset(w * 0.1f, ground - h * 0.2f), b0 * seg(p, 0f, 0.15f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
            tagAt(a.tag("sbkhadija0", 0, 1), Offset(w * 0.42f, h * 0.2f), b0 * seg(p, 0.2f, 0.35f), back = Color(0xCC1A1630), ink = ArtInk.Gold)
            tagAt(a.tag("sbkhadija0", 0, 2), Offset(w * 0.84f, ground - h * 0.27f), b0 * seg(p, 0.7f, 0.85f), back = Color(0xCC1A1630), ink = ArtInk.Ivory)
        }
        if (b1 > 0f) {
            val p = a.b("sbkhadija1")
            drawRect(Color(0xFF3A2A1E), alpha = 0.55f * b1)
            // the market stall: a balance that stays level, goods that multiply
            drawRect(Color(0xFF8A5A3A), Offset(w * 0.15f, h * 0.62f), Size(w * 0.7f, h * 0.05f), alpha = b1)
            balance12(Offset(w * 0.3f, h * 0.62f), h * 0.32f, 0f, Qn.GoldLight, b1 * seg(p, 0.2f, 0.4f))
            val more = (seg(p, 0.4f, 0.9f) * 6f).toInt()
            for (k in 0 until 2 + more) drawRoundRect(lerpColor(Qn.Gold, Qn.Leaf, (k % 3) / 3f), Offset(w * (0.5f + 0.045f * (k % 6)), h * (0.57f - 0.045f * (k / 6))), Size(w * 0.04f, h * 0.04f), CornerRadius(3f * density), alpha = b1)
            sirajMunir(Offset(w * 0.3f, h * 0.24f), w * 0.03f, b1, t, named = false)
            for (k in 0 until 3) tagAt(a.tag("sbkhadija1", 0, k), Offset(w * (0.22f + 0.28f * k), h * 0.8f), b1 * seg(p, 0.15f + 0.22f * k, 0.3f + 0.22f * k), back = Color(0xCC1A1630), ink = ArtInk.Gold)
        }
        if (b2 > 0f) {
            val p = a.b("sbkhadija2")
            drawRect(S97.Night, alpha = 0.6f * b2)
            close97(Offset(w * 0.2f, h * 0.45f), w * 0.1f, a, "sbkhadija2", 0, 0, b2 * seg(p, 0.05f, 0.25f))
            plain97(Offset(w * 0.5f, h * 0.62f), w * 0.07f, a, "sbkhadija2", 1, b2 * seg(p, 0.3f, 0.5f))
            sirajMunir(Offset(w * 0.8f, h * 0.45f), w * 0.04f, b2 * seg(p, 0.5f, 0.7f), t, nameAt = Offset(w * 0.8f, h * 0.6f))
            val link = easeInOut(seg(p, 0.55f, 0.9f))
            drawLine(S97.Thread, Offset(w * 0.3f, h * 0.45f), Offset(mix(w * 0.3f, w * 0.72f, link), h * 0.45f), 2f * density, StrokeCap.Round, alpha = b2)
        }
        if (b3 > 0f) {
            val p = a.b("sbkhadija3")
            drawRect(S97.Night, alpha = 0.7f * b3)
            val c = Offset(w * 0.5f, h * 0.48f)
            sirajMunir(c, w * 0.035f, b3, t, named = false)
            for (k in 0 until 6) {
                val ang = PI.toFloat() * (1.1f - 0.24f * k)
                val at = c + Offset(cos(ang) * w * 0.33f, -sin(ang) * h * 0.34f + h * 0.05f)
                close97(at, w * 0.06f, a, "sbkhadija3", k, k, b3 * seg(p, 0.1f + 0.13f * k, 0.25f + 0.13f * k))
            }
        }
    }
}
