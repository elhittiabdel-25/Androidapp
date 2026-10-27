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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 25 (2026-10-27): «بصائر»، «نقد نظرية التطور» — the author's arguments, drawn as his analogies. A row of eyes
 * and then the kunafa that chance would have to bake (a meteor, the rain, a volcanic stone — and no tray, until a
 * baker makes one); a sieve that names what survives "selected"; a kitchen whose few ingredients make several dishes,
 * and finches whose beaks come from one gene; cards of chromosome counts; and the shrinking list of "useless" organs.
 */

// ═════════════ 1. the eye and the kunafa (bs-ain) ═════════════

private object KunC25 {
    val Sky = Color(0xFFBFD8EA)
    val SkyLow = Color(0xFFF2E8D0)
    val Field = Color(0xFFD8B860)
    val FieldDark = Color(0xFFB8963E)
    val Flour = Color(0xFFF4F0E6)
    val Dough = Color(0xFFE8D2A8)
    val Lava = Color(0xFFE8602A)
    val Rock = Color(0xFF3A302C)
    val Tray = Color(0xFFB8862E)
    val Kunafa = Color(0xFFE8A03A)
    val Ink = Color(0xFF2A2430)
    val Eye = Color(0xFF3A5A7A)
    val Back = Color(0xE6201810)
}

/** One stage in a row of eyes, from a spot of light-sensitive cells ([k] = 0) to a full eye ([k] = 4). */
private fun DrawScope.eyeStage25(c: Offset, r: Float, k: Int, alpha: Float) {
    if (alpha <= 0.01f) return
    when (k) {
        0 -> drawCircle(KunC25.Eye, r * 0.35f, c, alpha = alpha)
        1 -> drawArc(KunC25.Eye, 200f, 140f, false, c - Offset(r * 0.6f, r * 0.6f), Size(r * 1.2f, r * 1.2f), alpha = alpha, style = Stroke(r * 0.25f))
        2 -> drawArc(KunC25.Eye, 120f, 300f, false, c - Offset(r * 0.6f, r * 0.6f), Size(r * 1.2f, r * 1.2f), alpha = alpha, style = Stroke(r * 0.25f))
        else -> {
            drawOval(Color.White, Offset(c.x - r, c.y - r * 0.55f), Size(2 * r, 1.1f * r), alpha = alpha)
            drawCircle(KunC25.Eye, r * 0.42f, c, alpha = alpha)
            drawCircle(Color.Black, r * 0.2f, c, alpha = alpha)
            if (k == 4) drawCircle(Color.White, r * 0.08f, c + Offset(-r * 0.1f, -r * 0.1f), alpha = alpha)
            drawOval(KunC25.Ink, Offset(c.x - r, c.y - r * 0.55f), Size(2 * r, 1.1f * r), alpha = alpha, style = Stroke(r * 0.08f))
        }
    }
}

/**
 * Beats: eyerow (a row of eyes appears, from a mere spot to a full eye, joined by dashed arrows: the steps are
 * assumed), meteor (a wheat field: a meteor streaks down and strikes it, and a cloud of flour rises), rainmix (rain on
 * the heap of flour: a lump of dough), lavabake (a volcanic stone glows hot beside it — and no tray of kunafa comes of
 * it, only a flickering outline and a question), baker (a baker at his counter lays the strands, the filling, the
 * syrup: a whole golden tray — knowledge made it).
 */
internal fun DrawScope.kunafaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "eyerow" to Shot(Offset(w * 0.5f, h * 0.3f), 1.3f),
        "meteor" to Shot(Offset(w * 0.3f, h * 0.6f), 1.4f),
        "rainmix" to Shot(Offset(w * 0.32f, h * 0.68f), 1.6f),
        "lavabake" to Shot(Offset(w * 0.45f, h * 0.65f), 1.45f),
        "baker" to Shot(Offset(w * 0.75f, h * 0.6f), 1.5f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { kunafaLand(a) }
}

private fun DrawScope.kunafaLand(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val er = a.b("eyerow")
    val me = a.b("meteor")
    val ra = a.b("rainmix")
    val lb = a.b("lavabake")
    val bk = a.b("baker")
    drawRect(Brush.verticalGradient(listOf(KunC25.Sky, KunC25.SkyLow), endY = h * 0.55f), size = Size(w, h * 0.55f))
    drawRect(Brush.verticalGradient(listOf(KunC25.Field, KunC25.FieldDark), startY = h * 0.55f, endY = h), Offset(0f, h * 0.55f), Size(w, h * 0.45f))
    for (k in 0 until 30) {
        val x = w * (0.02f + 0.022f * k)
        if (x > w * 0.6f) break
        val y = h * (0.6f + 0.08f * (k % 3))
        drawLine(Color(0xFF9A7A2E), Offset(x, y), Offset(x + sin(t + k) * w * 0.003f, y - h * 0.05f), 1.2f * dp)
    }
    // the row of eyes, simplest to fullest, the steps between them dashed
    if (er > 0f) {
        val fade = 1f - 0.6f * easeOut(seg(me, 0f, 0.3f))
        for (k in 0 until 5) {
            val c = Offset(w * (0.82f - 0.16f * k), h * 0.28f)
            val f = easeOut(seg(er, k * 0.12f, 0.2f + k * 0.12f))
            eyeStage25(c, w * 0.04f, k, f * fade)
            if (k < 4) {
                val ar = seg(er, 0.15f + k * 0.12f, 0.3f + k * 0.12f)
                drawLine(KunC25.Ink, c + Offset(-w * 0.05f, 0f), c + Offset(-w * 0.05f - w * 0.06f * ar, 0f), 1.4f * dp, alpha = fade, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp)))
            }
        }
    }
    // the meteor strikes the wheat
    val strike = Offset(w * 0.28f, h * 0.66f)
    if (me > 0f) {
        val fall = easeOut(seg(me, 0f, 0.35f))
        val from = Offset(w * 0.75f, -h * 0.1f)
        val p = from + (strike - from) * fall
        if (fall < 1f) {
            drawLine(Color(0xFFFFC07A), p, p + (from - p) * 0.25f, 4f * dp, StrokeCap.Round, alpha = 0.7f)
            drawCircle(KunC25.Rock, w * 0.012f, p)
            softGlow(p, w * 0.03f, KunC25.Lava, 0.7f)
        }
        val cloud = (if (me >= 1f) 0.8f else bump(seg(me, 0.3f, 1.3f))) * (1f - easeOut(seg(ra, 0f, 0.35f)))
        for (k in 0 until 8) {
            val ang = k * 0.8f
            drawCircle(KunC25.Flour, w * (0.02f + 0.01f * (k % 3)), strike + Offset(cos(ang) * w * 0.05f * seg(me, 0.3f, 0.8f), -h * 0.04f - sin(ang).let { it * it } * h * 0.05f), alpha = 0.7f * cloud)
        }
    }
    // the heap of flour, then dough under the rain
    val heap = easeOut(seg(me, 0.5f, 0.9f)).coerceAtLeast(if (ra > 0f) 1f else 0f)
    if (heap > 0f) {
        val dough = easeOut(seg(ra, 0.3f, 0.9f))
        drawOval(lerpColor(KunC25.Flour, KunC25.Dough, dough), Offset(strike.x - w * 0.05f, strike.y - h * 0.035f - h * 0.015f * dough), Size(w * 0.1f, h * 0.06f + h * 0.015f * dough), alpha = heap)
        if (dough > 0f) {
            // a kneaded lump: a fold and a sheen
            drawArc(Color(0xFFB8925A), 200f, 140f, false, Offset(strike.x - w * 0.03f, strike.y - h * 0.035f), Size(w * 0.06f, h * 0.04f), alpha = heap * dough, style = Stroke(1.6f * dp, cap = StrokeCap.Round))
            drawOval(Color.White, Offset(strike.x - w * 0.03f, strike.y - h * 0.04f), Size(w * 0.025f, h * 0.012f), alpha = 0.5f * heap * dough)
        }
        if (ra in 0.01f..0.8f) for (k in 0 until 12) {
            val f = fract(t * 1.8f + k * 0.11f)
            val x = strike.x + w * (-0.06f + 0.011f * k)
            drawLine(Color(0xFF6AA0D8), Offset(x, h * 0.35f + f * h * 0.3f), Offset(x - w * 0.003f, h * 0.37f + f * h * 0.3f), 1.4f * dp, alpha = 0.7f)
        }
    }
    // the volcanic stone: hot — and still no tray
    if (lb > 0f) {
        val rock = Offset(w * 0.46f, h * 0.72f)
        val o = easeOut(seg(lb, 0f, 0.3f))
        drawPath(Path().apply { moveTo(rock.x - w * 0.07f, rock.y + h * 0.04f); lineTo(rock.x - w * 0.04f, rock.y - h * 0.04f); lineTo(rock.x + w * 0.05f, rock.y - h * 0.03f); lineTo(rock.x + w * 0.07f, rock.y + h * 0.04f); close() }, KunC25.Rock, alpha = o)
        softGlow(rock, w * 0.07f, KunC25.Lava, 0.6f * o * (0.8f + 0.2f * sin(t * 5f)))
        for (k in 0 until 3) drawLine(KunC25.Lava, rock + Offset(w * (-0.03f + 0.03f * k), -h * 0.02f), rock + Offset(w * (-0.02f + 0.03f * k), h * 0.03f), 1.5f * dp, alpha = o)
        val ghost = (0.5f + 0.5f * sin(t * 16f)) * bump(seg(lb, 0.3f, 1.2f))
        drawOval(KunC25.Tray, Offset(rock.x - w * 0.06f, rock.y - h * 0.12f), Size(w * 0.12f, h * 0.05f), alpha = 0.5f * ghost, style = Stroke(1.5f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp))))
        qmark21(rock + Offset(w * 0.1f, -h * 0.14f), h * 0.06f, KunC25.Ink, seg(lb, 0.5f, 0.8f))
    }
    // the baker: knowledge makes the tray
    if (bk > 0f) {
        val counter = Offset(w * 0.75f, h * 0.72f)
        val o = easeOut(seg(bk, 0f, 0.25f))
        drawRect(Color(0xFF7A5A3A), Offset(counter.x - w * 0.16f, counter.y), Size(w * 0.32f, h * 0.2f), alpha = o)
        drawRect(Color(0xFFE6DCC8), Offset(counter.x - w * 0.17f, counter.y - h * 0.015f), Size(w * 0.34f, h * 0.02f), alpha = o)
        figure(counter + Offset(w * 0.12f, 0f), h * 0.26f, KunC25.Ink, o, arms = if (bk < 0.9f) 0.7f + 0.3f * sin(t * 4f) else 0f)
        val tray = counter + Offset(-w * 0.03f, -h * 0.04f)
        drawOval(KunC25.Tray, Offset(tray.x - w * 0.08f, tray.y - h * 0.03f), Size(w * 0.16f, h * 0.06f), alpha = o)
        val layer = easeOut(seg(bk, 0.25f, 0.85f))
        drawOval(KunC25.Kunafa, Offset(tray.x - w * 0.07f * layer, tray.y - h * 0.024f * layer), Size(w * 0.14f * layer, h * 0.048f * layer), alpha = o)
        for (k in 0 until 10) {
            val ang = k * 0.63f
            val r = w * 0.05f * layer
            drawLine(Color(0xFFC8781E), tray + Offset(cos(ang) * r * 0.3f, sin(ang) * r * 0.1f), tray + Offset(cos(ang + 0.4f) * r, sin(ang + 0.4f) * r * 0.32f), 1f * dp, alpha = o * layer)
        }
        if (layer > 0.95f) {
            for (k in 0 until 3) {
                val f = fract(t * 0.5f + k / 3f)
                drawCircle(Color.White, w * 0.006f, tray + Offset(w * (-0.03f + 0.03f * k), -h * 0.04f - f * h * 0.08f), alpha = 0.4f * (1f - f))
            }
            softGlow(tray, w * 0.08f, ArtInk.Gold, 0.4f)
        }
    }
}

// ═════════════ 2. the sieve that names what survives (bs-intikhab) ═════════════

private object SieveC25 {
    val Board = Color(0xFF2E3A34)
    val Card = Color(0xFFF6EEDC)
    val Rule = Color(0xFFB8A888)
    val Sieve = Color(0xFF8A6A3A)
    val Mesh = Color(0xFFB89A6A)
    val Critter = Color(0xFF6A8AB0)
    val Stamp = Color(0xFFD0503C)
    val Back = Color(0xE6101418)
}

private fun DrawScope.critter25(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawOval(color, Offset(c.x - s, c.y - s * 0.6f), Size(2 * s, 1.2f * s), alpha = alpha)
    drawCircle(color, s * 0.45f, c + Offset(-s * 0.95f, -s * 0.35f), alpha = alpha)
    drawCircle(Color.White, s * 0.12f, c + Offset(-s * 1.05f, -s * 0.42f), alpha = alpha)
    for (k in 0 until 2) drawLine(color, c + Offset(-s * 0.4f + k * s * 0.8f, s * 0.4f), c + Offset(-s * 0.5f + k * s * 0.8f, s * 0.85f), s * 0.15f, StrokeCap.Round, alpha = alpha)
}

/**
 * Beats: quotecard (a card pinned to a board, «كَارْل بُوبَر», its lines ruled), loop (little creatures drop through a
 * sieve; those below are stamped; a circle of arrows turns: alive → «selected» → alive), othercause (one survivor, and
 * round it three other things that may have kept it alive — a shelter, food, a sheltering cloud — each with a question).
 */
internal fun DrawScope.sieveScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "quotecard" to Shot(Offset(w * 0.5f, h * 0.4f), 1.4f),
        "loop" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "othercause" to Shot(Offset(w * 0.5f, h * 0.6f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { sieveRoom(a) }
}

private fun DrawScope.sieveRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val qc = a.b("quotecard")
    val lp = a.b("loop")
    val oc = a.b("othercause")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF3A4A44), SieveC25.Board)))
    // the quoted card
    val cardA = easeOut(seg(qc, 0f, 0.3f)) * (1f - easeOut(seg(lp, 0f, 0.3f)))
    if (cardA > 0f) {
        val c = Offset(w * 0.5f, h * 0.4f)
        drawRoundRect(SieveC25.Card, c + Offset(-w * 0.17f, -h * 0.16f), Size(w * 0.34f, h * 0.32f), CornerRadius(w * 0.006f), alpha = cardA)
        drawCircle(SieveC25.Stamp, w * 0.009f, c + Offset(0f, -h * 0.16f), alpha = cardA)
        for (k in 0 until 4) drawLine(SieveC25.Rule, c + Offset(-w * 0.13f, -h * 0.02f + k * h * 0.04f), c + Offset(w * 0.13f, -h * 0.02f + k * h * 0.04f), 1.2f * dp, alpha = cardA)
        tagAt(a.tag("quotecard"), c + Offset(0f, -h * 0.09f), cardA, back = Color.Transparent, ink = Color(0xFF2A2430))
    }
    // the sieve and the circle of arrows
    if (lp > 0f && oc < 0.3f) {
        val o = easeOut(seg(lp, 0f, 0.3f)) * (1f - easeOut(seg(oc, 0f, 0.3f)))
        val s = Offset(w * 0.38f, h * 0.45f)
        drawOval(SieveC25.Sieve, Offset(s.x - w * 0.14f, s.y - h * 0.04f), Size(w * 0.28f, h * 0.08f), alpha = o, style = Stroke(4f * dp))
        for (k in 0 until 9) drawLine(SieveC25.Mesh, Offset(s.x - w * 0.12f + k * w * 0.03f, s.y - h * 0.03f), Offset(s.x - w * 0.12f + k * w * 0.03f, s.y + h * 0.03f), 1f * dp, alpha = o)
        for (k in 0 until 5) {
            val f = fract(t * 0.25f + k * 0.2f)
            val p = Offset(s.x - w * 0.08f + k * w * 0.04f, mix(h * 0.15f, h * 0.85f, f))
            critter25(p, w * 0.018f, SieveC25.Critter, o * (if (f > 0.95f) 0f else 1f))
            if (f > 0.55f) drawCircle(SieveC25.Stamp, w * 0.007f, p + Offset(w * 0.008f, -h * 0.01f), alpha = o)
        }
        // alive → "selected" → alive
        val loopC = Offset(w * 0.75f, h * 0.5f)
        val r = w * 0.1f
        val turn = t * 1.2f
        drawCircle(ArtInk.BrassLight, r, loopC, alpha = 0.5f * o, style = Stroke(2f * dp))
        for (k in 0 until 3) {
            val ang = turn + k * 2.094f
            val p = loopC + Offset(cos(ang), sin(ang)) * r
            arrowHead(p, Offset(-sin(ang), cos(ang)), w * 0.02f, ArtInk.BrassLight, 2f * dp, o)
        }
        critter25(loopC, w * 0.025f, SieveC25.Critter, o)
        drawCircle(SieveC25.Stamp, w * 0.01f, loopC + Offset(w * 0.012f, -h * 0.015f), alpha = o * (0.5f + 0.5f * sin(t * 4f)))
    }
    // what else may have kept it alive?
    if (oc > 0f) {
        val c = Offset(w * 0.5f, h * 0.62f)
        val o = easeOut(seg(oc, 0f, 0.3f))
        critter25(c, w * 0.035f, SieveC25.Critter, o)
        val causes = listOf(Offset(-w * 0.2f, -h * 0.18f), Offset(0f, -h * 0.3f), Offset(w * 0.2f, -h * 0.18f))
        for ((k, d) in causes.withIndex()) {
            val f = easeOut(seg(oc, 0.2f + k * 0.2f, 0.4f + k * 0.2f))
            if (f <= 0f) continue
            val p = c + d
            when (k) {
                0 -> drawPath(Path().apply { moveTo(p.x - w * 0.04f, p.y + h * 0.02f); lineTo(p.x, p.y - h * 0.04f); lineTo(p.x + w * 0.04f, p.y + h * 0.02f); close() }, Color(0xFF8A5A3A), alpha = f)
                1 -> { drawCircle(Color(0xFFE85A3A), w * 0.02f, p, alpha = f); drawLine(Color(0xFF4A8A3A), p + Offset(0f, -w * 0.02f), p + Offset(w * 0.008f, -w * 0.032f), 2f * dp, alpha = f) }
                else -> for (m in 0 until 3) drawCircle(Color(0xFFE8ECF2), w * (0.018f + 0.006f * (m % 2)), p + Offset(w * (-0.02f + 0.02f * m), 0f), alpha = f)
            }
            drawLine(ArtInk.BrassLight, p + (c - p) * 0.25f, p + (c - p) * 0.75f, 1.4f * dp, alpha = f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp)))
            qmark21(p + Offset(w * 0.045f, -h * 0.04f), h * 0.045f, ArtInk.Ivory, f)
        }
    }
}

// ═════════════ 3. the kitchen and the finches (bs-takayyuf) ═════════════

private object KitC25 {
    val Wall = Color(0xFFF2E8D6)
    val Tile = Color(0xFFE2D4BC)
    val Counter = Color(0xFF8A6A48)
    val Top = Color(0xFFE6DCC8)
    val Pool = Color(0xFF8AB8D8)
    val Ink = Color(0xFF2A2430)
    val Bird = Color(0xFF7A5A3A)
    val Back = Color(0xE6201810)
    val Shades = listOf(Color(0xFF3A2A20), Color(0xFF8A6A50), Color(0xFF5A4030), Color(0xFFB08A6A), Color(0xFF2A2430))
}

/**
 * Beats: pool (a wide basin holds a row of people, tall and short, dark and light: «الْحَوْضِ الْجِينِيِّ»), pantry (on a
 * counter: a sack of flour, a jar of sugar, a bottle of oil, eggs), twotrays (two trays come out of the same things: a
 * cake and a pie), beaks (three finches with a short thick beak, a long fine one and a curved one, each joined by a
 * line to one strand of code).
 */
internal fun DrawScope.kitchenScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "pool" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "pantry" to Shot(Offset(w * 0.5f, h * 0.62f), 1.4f),
        "twotrays" to Shot(Offset(w * 0.5f, h * 0.6f), 1.3f),
        "beaks" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { kitchenRoom(a) }
}

private fun DrawScope.kitchenRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val po = a.b("pool")
    val pa = a.b("pantry")
    val tt = a.b("twotrays")
    val be = a.b("beaks")
    drawRect(KitC25.Wall)
    for (k in 0 until 12) for (m in 0 until 4) drawRect(KitC25.Tile, Offset(w * k / 12f + 1f, h * (0.08f + 0.09f * m)), Size(w / 12f - 2f, h * 0.085f), alpha = 0.5f)
    // the basin of one kind
    val poolA = easeOut(seg(po, 0f, 0.3f)) * (1f - easeOut(seg(pa, 0f, 0.3f)))
    if (poolA > 0f) {
        val c = Offset(w * 0.5f, h * 0.72f)
        drawOval(KitC25.Pool, Offset(c.x - w * 0.38f, c.y - h * 0.08f), Size(w * 0.76f, h * 0.18f), alpha = 0.5f * poolA)
        drawOval(Color(0xFF4A7A9A), Offset(c.x - w * 0.38f, c.y - h * 0.08f), Size(w * 0.76f, h * 0.18f), alpha = poolA, style = Stroke(2.5f * dp))
        for (k in 0 until 6) {
            val tall = h * (0.2f + 0.06f * ((k * 5) % 3))
            figure(Offset(w * (0.23f + 0.11f * k), c.y + h * 0.02f), tall, KitC25.Shades[k % 5], poolA * easeOut(seg(po, 0.1f + k * 0.08f, 0.3f + k * 0.08f)))
        }
        tagAt(a.tag("pool"), c + Offset(0f, -h * 0.42f), seg(po, 0.4f, 0.6f) * poolA, back = KitC25.Back, ink = ArtInk.BrassLight)
    }
    // the counter and its few things
    val kit = easeOut(seg(pa, 0f, 0.3f)) * (1f - easeOut(seg(be, 0f, 0.3f)))
    if (kit > 0f) {
        val top = h * 0.66f
        drawRect(KitC25.Counter, Offset(w * 0.08f, top), Size(w * 0.84f, h * 0.34f), alpha = kit)
        drawRect(KitC25.Top, Offset(w * 0.06f, top - h * 0.02f), Size(w * 0.88f, h * 0.025f), alpha = kit)
        val items = seg(pa, 0f, 0.9f)
        // flour sack, tied at the neck
        val s0 = easeOut(seg(items, 0f, 0.25f))
        drawRoundRect(Color(0xFFF2EEE4), Offset(w * 0.18f, top - h * 0.15f), Size(w * 0.08f, h * 0.13f), CornerRadius(w * 0.02f), alpha = kit * s0)
        drawRoundRect(Color(0xFFB8A888), Offset(w * 0.18f, top - h * 0.15f), Size(w * 0.08f, h * 0.13f), CornerRadius(w * 0.02f), alpha = kit * s0, style = Stroke(1.2f * dp))
        drawPath(Path().apply { moveTo(w * 0.2f, top - h * 0.15f); lineTo(w * 0.22f, top - h * 0.19f); lineTo(w * 0.24f, top - h * 0.15f); close() }, Color(0xFFE6E0D2), alpha = kit * s0)
        drawLine(Color(0xFF8A6A48), Offset(w * 0.2f, top - h * 0.15f), Offset(w * 0.24f, top - h * 0.15f), 2f * dp, alpha = kit * s0)
        // sugar jar
        val s1 = easeOut(seg(items, 0.25f, 0.5f))
        drawRoundRect(Color(0xFFDDEAF4), Offset(w * 0.3f, top - h * 0.13f), Size(w * 0.06f, h * 0.11f), CornerRadius(w * 0.008f), alpha = kit * s1)
        drawRect(Color.White, Offset(w * 0.305f, top - h * 0.07f), Size(w * 0.05f, h * 0.045f), alpha = kit * s1)
        drawRoundRect(Color(0xFF8AA0B8), Offset(w * 0.3f, top - h * 0.13f), Size(w * 0.06f, h * 0.11f), CornerRadius(w * 0.008f), alpha = kit * s1, style = Stroke(1.2f * dp))
        // oil bottle
        val s2 = easeOut(seg(items, 0.5f, 0.75f))
        drawRoundRect(Color(0xFFE8C84A), Offset(w * 0.39f, top - h * 0.15f), Size(w * 0.035f, h * 0.13f), CornerRadius(w * 0.008f), alpha = 0.85f * kit * s2)
        drawRect(Color(0xFF8A6A2A), Offset(w * 0.4f, top - h * 0.17f), Size(w * 0.015f, h * 0.025f), alpha = kit * s2)
        // three brown eggs
        val s3 = easeOut(seg(items, 0.75f, 1f))
        for (k in 0 until 3) {
            val eo = Offset(w * (0.46f + 0.032f * k), top - h * 0.05f)
            drawOval(Color(0xFFE2BC8E), eo, Size(w * 0.026f, h * 0.038f), alpha = kit * s3)
            drawOval(Color(0xFF9A7A52), eo, Size(w * 0.026f, h * 0.038f), alpha = kit * s3, style = Stroke(1f * dp))
        }
        // the two trays from the same things
        if (tt > 0f) {
            for (k in 0 until 2) {
                val f = easeOut(seg(tt, 0.1f + k * 0.35f, 0.45f + k * 0.35f))
                val c = Offset(w * (0.66f + 0.14f * k), top - h * 0.04f)
                drawOval(Color(0xFF9A9AA4), Offset(c.x - w * 0.065f, c.y - h * 0.02f), Size(w * 0.13f, h * 0.04f), alpha = kit * f)
                if (k == 0) {
                    drawRoundRect(Color(0xFFC88A4A), Offset(c.x - w * 0.05f, c.y - h * 0.08f), Size(w * 0.1f, h * 0.07f), CornerRadius(w * 0.015f), alpha = kit * f)
                    drawLine(Color(0xFFF4EEE4), Offset(c.x - w * 0.045f, c.y - h * 0.075f), Offset(c.x + w * 0.045f, c.y - h * 0.075f), 3f * dp, StrokeCap.Round, alpha = kit * f)
                } else {
                    drawOval(Color(0xFFE0A85A), Offset(c.x - w * 0.055f, c.y - h * 0.045f), Size(w * 0.11f, h * 0.05f), alpha = kit * f)
                    for (m in 0 until 4) drawLine(Color(0xFFB8782E), Offset(c.x - w * 0.04f + m * w * 0.027f, c.y - h * 0.04f), Offset(c.x - w * 0.03f + m * w * 0.027f, c.y - h * 0.005f), 1.2f * dp, alpha = kit * f)
                }
                if (f > 0.9f) for (m in 0 until 2) {
                    val ff = fract(t * 0.5f + m * 0.5f + k * 0.2f)
                    drawCircle(Color.White, w * 0.005f, c + Offset(w * 0.01f * m, -h * 0.1f - ff * h * 0.06f), alpha = 0.4f * (1f - ff) * kit)
                }
            }
        }
    }
    // the finches and their one gene
    if (be > 0f) {
        val o = easeOut(seg(be, 0f, 0.3f))
        drawRect(Brush.verticalGradient(listOf(Color(0xFFCFE4F0), Color(0xFFEAF2E0))), alpha = o)
        val gene = Offset(w * 0.5f, h * 0.82f)
        for (k in 0 until 16) {
            val x = gene.x - w * 0.2f + k * w * 0.027f
            val dy = sin(k * 0.9f + t) * h * 0.012f
            drawCircle(Color(0xFF7AC8FF), 2.2f * dp, Offset(x, gene.y + dy), alpha = o)
            drawCircle(Color(0xFFFFA0C8), 2.2f * dp, Offset(x, gene.y - dy), alpha = o)
        }
        for (k in 0 until 3) {
            val f = easeOut(seg(be, 0.15f + k * 0.2f, 0.4f + k * 0.2f))
            val b = Offset(w * (0.75f - 0.25f * k), h * 0.42f)
            // the bird: body, head, tail, a beak of its own shape
            drawOval(KitC25.Bird, Offset(b.x - w * 0.04f, b.y - h * 0.03f), Size(w * 0.08f, h * 0.07f), alpha = f)
            drawCircle(KitC25.Bird, w * 0.025f, b + Offset(-w * 0.04f, -h * 0.04f), alpha = f)
            drawCircle(Color.White, w * 0.005f, b + Offset(-w * 0.047f, -h * 0.047f), alpha = f)
            drawPath(Path().apply { moveTo(b.x + w * 0.03f, b.y); lineTo(b.x + w * 0.07f, b.y - h * 0.03f); lineTo(b.x + w * 0.065f, b.y + h * 0.01f); close() }, KitC25.Bird, alpha = f)
            val bk = b + Offset(-w * 0.062f, -h * 0.04f)
            when (k) {
                0 -> drawPath(Path().apply { moveTo(bk.x, bk.y - h * 0.018f); lineTo(bk.x - w * 0.028f, bk.y); lineTo(bk.x, bk.y + h * 0.018f); close() }, Color(0xFF3A3028), alpha = f)
                1 -> drawPath(Path().apply { moveTo(bk.x, bk.y - h * 0.006f); lineTo(bk.x - w * 0.055f, bk.y); lineTo(bk.x, bk.y + h * 0.006f); close() }, Color(0xFF3A3028), alpha = f)
                else -> drawPath(Path().apply { moveTo(bk.x, bk.y - h * 0.008f); quadraticTo(bk.x - w * 0.04f, bk.y - h * 0.01f, bk.x - w * 0.045f, bk.y + h * 0.025f); lineTo(bk.x, bk.y + h * 0.008f); close() }, Color(0xFF3A3028), alpha = f)
            }
            drawLine(ArtInk.Brass, b + Offset(0f, h * 0.04f), gene + Offset(w * (0.1f - 0.1f * k), 0f), 1.4f * dp, alpha = f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp)))
        }
        softGlow(gene, w * 0.15f, ArtInk.Gold, 0.3f * seg(be, 0.7f, 1f))
    }
}

// ═════════════ 4. chromosome counts (bs-kromosom) ═════════════

private object KarC25 {
    val Desk = Color(0xFF2A3040)
    val Card = Color(0xFFF2EEE4)
    val Edge = Color(0xFF9A8A6A)
    val Chrom = Color(0xFF9A6AC8)
    val Ink = Color(0xFF2A2430)
    val Back = Color(0xE6101418)
}

/** A chromosome as an X, [s] tall. */
private fun DrawScope.chromosome25(c: Offset, s: Float, color: Color, alpha: Float, rot: Float = 0f) {
    if (alpha <= 0.01f) return
    withTransform({ rotate(rot, c) }) {
        drawLine(color, c + Offset(-s * 0.2f, -s * 0.5f), c + Offset(s * 0.2f, s * 0.5f), s * 0.18f, StrokeCap.Round, alpha = alpha)
        drawLine(color, c + Offset(s * 0.2f, -s * 0.5f), c + Offset(-s * 0.2f, s * 0.5f), s * 0.18f, StrokeCap.Round, alpha = alpha)
        drawCircle(Color.White, s * 0.07f, c, alpha = 0.6f * alpha)
    }
}

private fun DrawScope.ape25(foot: Offset, tall: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val sw = tall * 0.08f
    val hip = foot - Offset(0f, tall * 0.4f)
    val sh = hip + Offset(-tall * 0.12f, -tall * 0.3f)
    drawLine(color, hip, foot + Offset(-tall * 0.1f, 0f), sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, foot + Offset(tall * 0.1f, 0f), sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, sh, sw * 1.4f, StrokeCap.Round, alpha = alpha)
    drawCircle(color, tall * 0.11f, sh + Offset(-tall * 0.06f, -tall * 0.08f), alpha = alpha)
    drawLine(color, sh, foot + Offset(-tall * 0.25f, -tall * 0.02f), sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, sh, foot + Offset(-tall * 0.05f, -tall * 0.02f), sw, StrokeCap.Round, alpha = alpha)
}

/**
 * Cards on a dark desk. Beats: counts (two cards: an ape's silhouette with «48», a man's with «46»), fuse (two
 * chromosomes slide together into one), potato (two more cards: a potato with «48», a small guppy with «46» — the
 * count alone proves nothing), lineage (a family line: a first man with 48, a descendant in whom two join, his children
 * with 46; a large question mark over who can tell which story is true).
 */
internal fun DrawScope.karyotypeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "counts" to Shot(Offset(w * 0.5f, h * 0.45f), 1.15f),
        "fuse" to Shot(Offset(w * 0.5f, h * 0.75f), 1.5f),
        "potato" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "lineage" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { karyotypeDesk(a) }
}

private fun DrawScope.countCard25(c: Offset, a: ArtCtx, kind: String, part: Int, alpha: Float, art: DrawScope.(Offset) -> Unit) {
    if (alpha <= 0.01f) return
    val w = size.width
    val h = size.height
    drawRoundRect(KarC25.Card, c + Offset(-w * 0.08f, -h * 0.17f), Size(w * 0.16f, h * 0.34f), CornerRadius(w * 0.01f), alpha = alpha)
    drawRoundRect(KarC25.Edge, c + Offset(-w * 0.08f, -h * 0.17f), Size(w * 0.16f, h * 0.34f), CornerRadius(w * 0.01f), alpha = alpha, style = Stroke(1.2f * density))
    art(c + Offset(0f, h * 0.05f))
    tagAt(a.tag(kind, 0, part), c + Offset(0f, h * 0.11f), alpha, back = KarC25.Back, ink = ArtInk.BrassLight)
}

private fun DrawScope.karyotypeDesk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val co = a.b("counts")
    val fu = a.b("fuse")
    val pt = a.b("potato")
    val li = a.b("lineage")
    drawRect(Brush.radialGradient(listOf(Color(0xFF3A4258), KarC25.Desk), Offset(w * 0.5f, h * 0.4f), w * 0.7f))
    val lin = easeOut(seg(li, 0f, 0.3f))
    val cards = 1f - lin
    // the ape and the man
    val y = h * 0.4f
    val c0 = easeOut(seg(co, 0f, 0.3f)) * cards
    val c1 = easeOut(seg(co, 0.25f, 0.55f)) * cards
    countCard25(Offset(w * 0.62f, y), a, "counts", 0, c0) { p -> ape25(p, h * 0.16f, KarC25.Ink, c0) }
    countCard25(Offset(w * 0.38f, y), a, "counts", 1, c1) { p -> figure(p, h * 0.17f, KarC25.Ink, c1) }
    // two chromosomes slide into one
    if (fu > 0f && cards > 0f) {
        val c = Offset(w * 0.5f, h * 0.78f)
        val j = easeInOut(seg(fu, 0.1f, 0.7f))
        chromosome25(c + Offset(-w * 0.06f * (1f - j), 0f), h * 0.1f, KarC25.Chrom, cards, rot = 90f * j)
        chromosome25(c + Offset(w * 0.06f * (1f - j), 0f), h * 0.1f, KarC25.Chrom, cards * (1f - 0.6f * j), rot = 90f * j)
        if (j > 0.95f) softGlow(c, w * 0.06f, KarC25.Chrom, 0.5f * cards)
    }
    // the potato and the guppy
    if (pt > 0f) {
        val f0 = easeOut(seg(pt, 0f, 0.3f)) * cards
        val f1 = easeOut(seg(pt, 0.4f, 0.7f)) * cards
        countCard25(Offset(w * 0.86f, y), a, "potato", 0, f0) { p ->
            drawOval(Color(0xFFB8925A), Offset(p.x - w * 0.045f, p.y - h * 0.06f), Size(w * 0.09f, h * 0.09f), alpha = f0)
            for (k in 0 until 3) drawCircle(Color(0xFF8A6A3A), w * 0.004f, p + Offset(w * (-0.02f + 0.02f * k), -h * (0.02f + 0.01f * (k % 2))), alpha = f0)
        }
        countCard25(Offset(w * 0.14f, y), a, "potato", 1, f1) { p ->
            drawOval(Color(0xFFE8803A), Offset(p.x - w * 0.03f, p.y - h * 0.045f), Size(w * 0.06f, h * 0.04f), alpha = f1)
            drawPath(Path().apply { moveTo(p.x + w * 0.025f, p.y - h * 0.025f); lineTo(p.x + w * 0.055f, p.y - h * 0.05f); lineTo(p.x + w * 0.055f, p.y); close() }, Color(0xFF3A8AD8), alpha = f1)
            drawCircle(Color.Black, w * 0.004f, p + Offset(-w * 0.018f, -h * 0.03f), alpha = f1)
        }
        // equal signs joining like counts
        val eq = seg(pt, 0.75f, 1f) * cards
        // an equals sign in each gap between like counts
        for (gx in listOf(w * 0.26f, w * 0.74f)) for (dy in listOf(-1f, 1f)) {
            drawLine(ArtInk.BrassLight, Offset(gx - w * 0.022f, y + dy * h * 0.014f), Offset(gx + w * 0.022f, y + dy * h * 0.014f), 3f * dp, StrokeCap.Round, alpha = eq)
        }
    }
    // the other story: one family line
    if (lin > 0f) {
        val top = Offset(w * 0.5f, h * 0.2f)
        figure(top + Offset(0f, h * 0.12f), h * 0.12f, Color(0xFFDCD4E8), lin)
        tagAt(a.tag("lineage", 0, 0), top + Offset(w * 0.06f, h * 0.05f), lin, back = KarC25.Back, ink = ArtInk.BrassLight)
        val mid = Offset(w * 0.5f, h * 0.5f)
        val g2 = easeOut(seg(li, 0.25f, 0.5f))
        drawLine(Color(0xFF8A90A8), top + Offset(0f, h * 0.14f), mid + Offset(0f, -h * 0.02f), 1.5f * dp, alpha = g2)
        figure(mid + Offset(0f, h * 0.12f), h * 0.12f, Color(0xFFDCD4E8), g2)
        chromosome25(mid + Offset(w * 0.06f, h * 0.06f), h * 0.05f, KarC25.Chrom, g2, rot = 90f * easeInOut(seg(li, 0.4f, 0.6f)))
        val g3 = easeOut(seg(li, 0.5f, 0.75f))
        for (k in 0 until 3) {
            val p = Offset(w * (0.36f + 0.14f * k), h * 0.82f)
            drawLine(Color(0xFF8A90A8), mid + Offset(0f, h * 0.14f), p + Offset(0f, -h * 0.12f), 1.5f * dp, alpha = g3)
            figure(p, h * 0.11f, Color(0xFFDCD4E8), g3)
        }
        tagAt(a.tag("lineage", 0, 1), Offset(w * 0.72f, h * 0.72f), g3, back = KarC25.Back, ink = ArtInk.BrassLight)
        qmark21(Offset(w * 0.18f, h * 0.45f), h * 0.14f, ArtInk.Gold, seg(li, 0.75f, 0.95f))
    }
}

// ═════════════ 5. the list of "useless" organs (bs-athariyya) ═════════════

private object AnaC25 {
    val Paper = Color(0xFFF4ECD8)
    val Rule = Color(0xFFB8A888)
    val Strike = Color(0xFFD0503C)
    val Body = Color(0xFF3A3448)
    val Gland = Color(0xFFFFC07A)
    val Grid = Color(0xFF8A90A8)
    val Back = Color(0xE6101418)
}

/**
 * Beats: list180 (a long scroll of entries unrolls, the number «180» at its head), shrinklist (one entry after another
 * lights up green — a use found — and is struck off; the scroll rolls up shorter and shorter), glands (a head and neck
 * in silhouette; three small glands light in turn, each named), graph (a chart: a dashed line climbing — what the theory
 * would expect — and the real line falling, year after year).
 */
internal fun DrawScope.anatomyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "list180" to Shot(Offset(w * 0.5f, h * 0.45f), 1.2f),
        "shrinklist" to Shot(Offset(w * 0.5f, h * 0.45f), 1.2f),
        "glands" to Shot(Offset(w * 0.5f, h * 0.45f), 1.15f),
        "graph" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { anatomyRoom(a) }
}

private fun DrawScope.anatomyRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val l1 = a.b("list180")
    val sh = a.b("shrinklist")
    val gl = a.b("glands")
    val gr = a.b("graph")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF1E2434), Color(0xFF2E3446))))
    // the scroll of "useless" organs
    val scroll = easeOut(seg(l1, 0f, 0.5f)) * (1f - easeOut(seg(gl, 0f, 0.3f)))
    if (scroll > 0f) {
        val left = w * 0.3f
        val right = w * 0.7f
        val top = h * 0.12f
        val shrink = easeInOut(seg(sh, 0.1f, 1f))
        val len = h * 0.78f * scroll * (1f - 0.75f * shrink)
        drawRect(AnaC25.Paper, Offset(left, top), Size(right - left, len))
        drawRoundRect(Color(0xFF8A6A48), Offset(left - w * 0.02f, top - h * 0.02f), Size(right - left + w * 0.04f, h * 0.03f), CornerRadius(h * 0.015f))
        drawRoundRect(Color(0xFF8A6A48), Offset(left - w * 0.02f, top + len - h * 0.01f), Size(right - left + w * 0.04f, h * 0.03f), CornerRadius(h * 0.015f))
        val rows = (len / (h * 0.035f)).toInt()
        for (r in 0 until rows) {
            val yy = top + h * 0.06f + r * h * 0.035f
            if (yy > top + len - h * 0.02f) break
            val wide = (right - left) * (0.5f + 0.35f * fract(r * 0.618f))
            drawLine(AnaC25.Rule, Offset(left + w * 0.03f, yy), Offset(left + w * 0.03f + wide, yy), 2f * dp, StrokeCap.Round)
            val found = seg(sh, 0.05f + r * 0.05f, 0.12f + r * 0.05f)
            if (found > 0f) {
                drawLine(AnaC25.Strike, Offset(left + w * 0.025f, yy), Offset(left + w * 0.025f + (wide + w * 0.01f) * found, yy), 1.6f * dp, StrokeCap.Round)
                drawCircle(Color(0xFF7ED6A0), w * 0.006f, Offset(right - w * 0.03f, yy), alpha = found)
            }
        }
        tagAt(a.tag("list180"), Offset(w * 0.5f, top - h * 0.06f), seg(l1, 0.3f, 0.6f) * (1f - seg(sh, 0f, 0.3f)), back = AnaC25.Back, ink = ArtInk.BrassLight)
    }
    // a head and neck, three small glands lit in turn
    val body = easeOut(seg(gl, 0f, 0.3f)) * (1f - easeOut(seg(gr, 0f, 0.3f)))
    if (body > 0f) {
        val head = Offset(w * 0.5f, h * 0.32f)
        drawOval(AnaC25.Body, Offset(head.x - w * 0.09f, head.y - h * 0.18f), Size(w * 0.18f, h * 0.34f), alpha = body)
        drawRect(AnaC25.Body, Offset(head.x - w * 0.04f, head.y + h * 0.12f), Size(w * 0.08f, h * 0.18f), alpha = body)
        drawPath(Path().apply { moveTo(head.x - w * 0.04f, h * 0.6f); lineTo(head.x - w * 0.2f, h * 0.72f); lineTo(head.x - w * 0.22f, h); lineTo(head.x + w * 0.22f, h); lineTo(head.x + w * 0.2f, h * 0.72f); lineTo(head.x + w * 0.04f, h * 0.6f); close() }, AnaC25.Body, alpha = body)
        val spots = listOf(head + Offset(0f, h * 0.24f), head + Offset(0f, -h * 0.01f), head + Offset(-w * 0.05f, -h * 0.07f))
        val tagsAt = listOf(Offset(w * 0.75f, h * 0.56f), Offset(w * 0.75f, h * 0.3f), Offset(w * 0.25f, h * 0.18f))
        for (k in 0 until 3) {
            val f = easeOut(seg(gl, 0.2f + k * 0.25f, 0.35f + k * 0.25f))
            if (f <= 0f) continue
            softGlow(spots[k], w * 0.035f, AnaC25.Gland, 0.8f * f * (0.8f + 0.2f * sin(t * 4f + k)))
            drawCircle(AnaC25.Gland, w * 0.01f, spots[k], alpha = f)
            drawLine(AnaC25.Gland, spots[k], tagsAt[k], 1f * dp, alpha = 0.6f * f)
            tagAt(a.tag("glands", 0, k), tagsAt[k], f, back = AnaC25.Back, ink = ArtInk.BrassLight)
        }
    }
    // expected against found
    if (gr > 0f) {
        val o = easeOut(seg(gr, 0f, 0.3f))
        val ox = w * 0.18f
        val oy = h * 0.82f
        drawLine(AnaC25.Grid, Offset(ox, oy), Offset(w * 0.86f, oy), 2f * dp, alpha = o)
        drawLine(AnaC25.Grid, Offset(ox, oy), Offset(ox, h * 0.12f), 2f * dp, alpha = o)
        val draw = seg(gr, 0.15f, 0.9f)
        val dash = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp))
        val exp = Path().apply { moveTo(ox, h * 0.5f); lineTo(ox + (w * 0.66f) * draw, h * 0.5f - h * 0.32f * draw) }
        drawPath(exp, Color(0xFFB8BCC8), alpha = o, style = Stroke(2.5f * dp, pathEffect = dash, cap = StrokeCap.Round))
        val real = Path().apply {
            moveTo(ox, h * 0.3f)
            val n = 20
            for (k in 1..(n * draw).toInt()) {
                val f = k / n.toFloat()
                lineTo(ox + w * 0.66f * f, h * 0.3f + h * 0.45f * (1f - (1f - f) * (1f - f)) + sin(k * 2.1f) * h * 0.008f)
            }
        }
        drawPath(real, Color(0xFF7ED6A0), alpha = o, style = Stroke(3f * dp, cap = StrokeCap.Round))
        arrowHead(Offset(ox + w * 0.66f * draw, h * 0.5f - h * 0.32f * draw), Offset(1f, -0.48f), w * 0.02f, Color(0xFFB8BCC8), 2.5f * dp, o * seg(draw, 0.2f, 0.4f))
    }
}
