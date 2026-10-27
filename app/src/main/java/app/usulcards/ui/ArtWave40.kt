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
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 40 (2026-10-27): «بصائر»، الجزء الثالث، «القدر والوحي». A strong and just citadel above two roads; strings that
 * never held the students and a teacher who only knew; a seizure trace beside verses set in their places, a camel
 * kneeling under the weight, and an eclipse not used; a glass box inside the light; a storm and a chain of «why».
 * He ﷺ is only light, the other prophets are small lights, the angels are light; people are faceless figures; Allah is
 * never drawn.
 */

private val Back40 = Color(0xE6181420)

// ═════════════ 1. a just citadel above two roads (bs-yudillu) ═════════════

private object RoadsC40 {
    val Sky = Color(0xFF1C2038)
    val SkyLow = Color(0xFF4A3E58)
    val Ground = Color(0xFF3A3238)
    val Wall = Color(0xFF8A7A6A)
    val Pan = Color(0xFFD8B04A)
    val Bright = Color(0xFFFFF0C8)
    val Fog = Color(0xFF6A6A78)
    val Walker = Color(0xFFE8E0D0)
    val Weight = Color(0xFF5A4A50)
}

/**
 * Beats: citadel (a strong citadel on the hill), fairscales (a balance over its gate: only the one that earned it goes
 * down), tworoads (two roads from a fork below, one climbing into light, one sinking into fog: «الضَّلَالُ»,
 * «الْهِدَايَةُ»), addict (on the dark road a walker drags a weight that grows, step by step), stepmore (on the bright road
 * one step lights the way far ahead of him).
 */
internal fun DrawScope.twopathsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "citadel" to Shot(Offset(w * 0.5f, h * 0.28f), 1.3f),
        "fairscales" to Shot(Offset(w * 0.5f, h * 0.3f), 1.45f),
        "forkroads" to Shot(Offset(w * 0.5f, h * 0.62f), 1.05f),
        "addict" to Shot(Offset(w * 0.25f, h * 0.72f), 1.3f),
        "stepmore" to Shot(Offset(w * 0.72f, h * 0.6f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { twoRoads(a) }
}

private fun DrawScope.twoRoads(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ci = a.b("citadel")
    val fs = a.b("fairscales")
    val tr = a.b("forkroads")
    val ad = a.b("addict")
    val sm = a.b("stepmore")
    val ground = h * 0.5f
    nightSky(t, RoadsC40.Sky, RoadsC40.SkyLow, ground, starAlpha = 0.6f)
    drawRect(RoadsC40.Ground, Offset(0f, ground), Size(w, h - ground))
    // the citadel
    val cf = easeOut(seg(ci, 0f, 0.4f))
    if (cf > 0f) {
        val base = Offset(w * 0.5f, ground)
        drawRect(RoadsC40.Wall, Offset(base.x - w * 0.18f, base.y - h * 0.16f), Size(w * 0.36f, h * 0.16f), alpha = cf)
        for (k in 0 until 7) drawRect(RoadsC40.Wall, Offset(base.x - w * 0.18f + k * w * 0.055f, base.y - h * 0.19f), Size(w * 0.03f, h * 0.03f), alpha = cf)
        for (s in listOf(-1f, 1f)) drawRect(RoadsC40.Wall, Offset(base.x + s * w * 0.2f - w * 0.035f, base.y - h * 0.26f), Size(w * 0.07f, h * 0.26f), alpha = cf)
        drawPath(archPath26(base.x, base.y - h * 0.11f, base.y, w * 0.04f), Color(0xFF2A2228), alpha = cf)
    }
    // a just balance over the gate
    if (fs > 0f) {
        val f = easeOut(seg(fs, 0f, 0.3f))
        val c = Offset(w * 0.5f, h * 0.24f)
        val tilt = easeInOut(seg(fs, 0.35f, 0.8f)) * 0.25f
        drawLine(RoadsC40.Pan, c, c + Offset(0f, h * 0.06f), 2f * dp, alpha = f)
        val l = c + Offset(-w * 0.1f * cos(tilt), -w * 0.1f * sin(tilt))
        val r = c + Offset(w * 0.1f * cos(tilt), w * 0.1f * sin(tilt))
        drawLine(RoadsC40.Pan, l, r, 3f * dp, StrokeCap.Round, alpha = f)
        for ((k, p) in listOf(l, r).withIndex()) {
            drawLine(RoadsC40.Pan, p, p + Offset(0f, h * 0.04f), 1.2f * dp, alpha = f)
            drawArc(RoadsC40.Pan, 0f, 180f, true, Offset(p.x - w * 0.035f, p.y + h * 0.03f), Size(w * 0.07f, h * 0.03f), alpha = f)
            if (k == 1) drawRect(RoadsC40.Weight, Offset(p.x - w * 0.012f, p.y + h * 0.02f), Size(w * 0.024f, h * 0.02f), alpha = f * seg(fs, 0.2f, 0.35f))
        }
    }
    // two roads
    val fork = Offset(w * 0.5f, h * 0.92f)
    val lit = Offset(w * 0.88f, h * 0.56f)
    val dark = Offset(w * 0.1f, h * 0.62f)
    if (tr > 0f) {
        val f = easeOut(seg(tr, 0f, 0.4f))
        drawLine(RoadsC40.Bright, fork, fork + (lit - fork) * easeOut(seg(tr, 0.1f, 0.6f)), w * 0.035f, StrokeCap.Round, alpha = f * 0.85f)
        drawLine(RoadsC40.Fog, fork, fork + (dark - fork) * easeOut(seg(tr, 0.2f, 0.7f)), w * 0.035f, StrokeCap.Round, alpha = f * 0.85f)
        cloud38(dark + Offset(w * 0.02f, -h * 0.02f), w * 0.07f, Color(0xFF8A8A98), 0.6f * f * seg(tr, 0.5f, 0.8f))
        softGlow(lit, w * 0.08f, ArtInk.Gold, 0.5f * f * seg(tr, 0.4f, 0.8f))
        tagAt(a.tag("forkroads", 0, 0), dark + Offset(w * 0.12f, h * 0.1f), seg(tr, 0.5f, 0.75f), back = Back40, ink = Color(0xFFC8C8D0))
        tagAt(a.tag("forkroads", 0, 1), lit + Offset(-w * 0.12f, h * 0.1f), seg(tr, 0.6f, 0.85f), back = Back40, ink = ArtInk.Gold)
    }
    // the addict: full will at first, then weaker with every step
    if (ad > 0f) {
        val f = easeOut(seg(ad, 0f, 0.25f)) * (1f - 0.6f * seg(sm, 0f, 0.3f))
        val u = easeInOut(seg(ad, 0.1f, 0.9f))
        val foot = fork + (dark - fork) * (0.15f + 0.55f * u)
        figure(foot, h * 0.14f, RoadsC40.Walker, f, walk = if (ad in 0.1f..0.9f) t * 4f else 0f, lean = 0.25f * u)
        val wt = w * (0.01f + 0.03f * u)
        drawLine(RoadsC40.Weight, foot + Offset(w * 0.01f, -h * 0.06f), foot + Offset(w * 0.07f, -h * 0.005f), 1.5f * dp, alpha = f)
        drawCircle(RoadsC40.Weight, wt, foot + Offset(w * 0.07f + wt * 0.5f, -wt), alpha = f)
    }
    // one step toward the light, and the light goes far ahead
    if (sm > 0f) {
        val f = easeOut(seg(sm, 0f, 0.25f))
        val foot = fork + (lit - fork) * (0.35f + 0.08f * easeInOut(seg(sm, 0.1f, 0.35f)))
        figure(foot, h * 0.14f, RoadsC40.Walker, f, walk = if (sm in 0.1f..0.35f) t * 5f else 0f)
        val reach = easeOut(seg(sm, 0.35f, 0.9f))
        val end = foot + (lit - foot) * reach
        drawLine(Color(0xFFFFF8E0), foot, end, w * 0.05f, StrokeCap.Round, alpha = 0.5f * f * reach)
        softGlow(end, w * 0.07f, ArtInk.Gold, 0.6f * f * reach)
    }
}

// ═════════════ 2. a teacher who only knew (bs-ilm-sabiq) ═════════════

private object ClassC40 {
    val Wall = Color(0xFFE4DCC8)
    val WallLow = Color(0xFFCFC4AA)
    val Floor = Color(0xFF8A7458)
    val Board = Color(0xFF2E4A3E)
    val Chalk = Color(0xFFF0F0E6)
    val Desk = Color(0xFF7A5A3A)
    val Pupil = Color(0xFF2E2620)
    val Cord = Color(0xFFB8A888)
    val House = Color(0xFFD8C098)
}

/**
 * Beats: cutstrings (strings hang over a walker as over a puppet, and every one is cut: knowing is not forcing),
 * teacher (a board with two predictions, a tick and a cross, over two desks), forward (two walkers, one steps forward
 * and one back: «تَقَدَّمَ», «تَأَخَّرَ»), messengers (small lights come down over three houses: no one left without a
 * reminder), willinside (a small circle of choice, wholly inside a wide field of light).
 */
internal fun DrawScope.teacherdeskScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "cutstrings" to Shot(Offset(w * 0.22f, h * 0.5f), 1.35f),
        "teacher" to Shot(Offset(w * 0.6f, h * 0.4f), 1.2f),
        "forward" to Shot(Offset(w * 0.5f, h * 0.72f), 1.2f),
        "messengers" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "willinside" to Shot(Offset(w * 0.5f, h * 0.45f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { classRoom(a) }
}

private fun DrawScope.classRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cs = a.b("cutstrings")
    val te = a.b("teacher")
    val fw = a.b("forward")
    val ms = a.b("messengers")
    val wi = a.b("willinside")
    val floor = h * 0.8f
    drawRect(Brush.verticalGradient(listOf(ClassC40.Wall, ClassC40.WallLow), endY = floor), size = Size(w, floor))
    drawRect(ClassC40.Floor, Offset(0f, floor), Size(w, h - floor))
    val away = 1f - 0.75f * seg(ms, 0f, 0.4f)
    // the strings that do not hold him
    if (cs > 0f) {
        val f = easeOut(seg(cs, 0f, 0.3f)) * away
        val foot = Offset(w * 0.22f, floor)
        figure(foot, h * 0.24f, ClassC40.Pupil, f, walk = if (cs > 0.7f) t * 4f else 0f)
        for (k in 0 until 3) {
            val top = Offset(w * (0.17f + 0.05f * k), h * 0.08f)
            val bottom = foot + Offset(w * (-0.04f + 0.04f * k), -h * (0.22f - 0.05f * (k % 2)))
            val cut = seg(cs, 0.4f + 0.1f * k, 0.55f + 0.1f * k)
            val mid = top + (bottom - top) * 0.5f
            drawLine(ClassC40.Cord, top, mid + Offset(0f, h * 0.05f * cut), 1.2f * dp, alpha = f)
            drawLine(ClassC40.Cord, mid + Offset(0f, h * 0.04f + h * 0.08f * cut), bottom, 1.2f * dp, alpha = f * (1f - cut))
        }
    }
    // the teacher's board: he knew, he did not force
    if (te > 0f) {
        val f = easeOut(seg(te, 0f, 0.3f)) * away
        val c = Offset(w * 0.62f, h * 0.3f)
        drawRect(ClassC40.Board, Offset(c.x - w * 0.2f, c.y - h * 0.12f), Size(w * 0.4f, h * 0.24f), alpha = f)
        drawRect(ClassC40.Desk, Offset(c.x - w * 0.2f, c.y - h * 0.12f), Size(w * 0.4f, h * 0.24f), alpha = f, style = Stroke(4f * dp))
        check21(c + Offset(-w * 0.09f, 0f), w * 0.04f, ClassC40.Chalk, seg(te, 0.3f, 0.5f) * f, 3f * dp)
        cross21(c + Offset(w * 0.09f, 0f), w * 0.04f, ClassC40.Chalk, seg(te, 0.45f, 0.65f) * f, 3f * dp)
        for (k in 0 until 2) {
            val d = Offset(w * (0.53f + 0.18f * k), floor - h * 0.08f)
            drawRect(ClassC40.Desk, Offset(d.x - w * 0.06f, d.y), Size(w * 0.12f, h * 0.02f), alpha = f)
            drawLine(ClassC40.Desk, d + Offset(-w * 0.05f, 0f), d + Offset(-w * 0.05f, h * 0.08f), 2f * dp, alpha = f)
            drawLine(ClassC40.Desk, d + Offset(w * 0.05f, 0f), d + Offset(w * 0.05f, h * 0.08f), 2f * dp, alpha = f)
        }
    }
    // free to go forward or back
    if (fw > 0f) {
        val f = easeOut(seg(fw, 0f, 0.3f)) * away
        val step = easeInOut(seg(fw, 0.2f, 0.7f))
        figure(Offset(w * 0.5f + w * 0.1f * step, floor + h * 0.12f), h * 0.12f, ClassC40.Pupil, f, walk = if (fw in 0.2f..0.7f) t * 6f else 0f)
        figure(Offset(w * 0.42f - w * 0.08f * step, floor + h * 0.12f), h * 0.12f, Color(0xFF5A4A40), f, lean = 0.15f * step)
        tagAt(a.tag("forward", 0, 0), Offset(w * 0.62f, floor + h * 0.16f), seg(fw, 0.5f, 0.75f) * away, back = Back40, ink = ArtInk.Gold)
        tagAt(a.tag("forward", 0, 1), Offset(w * 0.3f, floor + h * 0.16f), seg(fw, 0.6f, 0.85f) * away, back = Back40, ink = Color(0xFFD8D0C8))
    }
    // messengers sent to every town
    if (ms > 0f) {
        val f = easeOut(seg(ms, 0.1f, 0.4f)) * (1f - 0.6f * seg(wi, 0f, 0.4f))
        drawRect(Color(0xFF1C2238), Offset(0f, 0f), Size(w, floor), alpha = 0.9f * f)
        for (k in 0 until 3) {
            val c = Offset(w * (0.22f + 0.28f * k), floor - h * 0.06f)
            drawRect(ClassC40.House, Offset(c.x - w * 0.06f, c.y - h * 0.08f), Size(w * 0.12f, h * 0.14f), alpha = f)
            drawPath(Path().apply { moveTo(c.x - w * 0.07f, c.y - h * 0.08f); lineTo(c.x, c.y - h * 0.15f); lineTo(c.x + w * 0.07f, c.y - h * 0.08f); close() }, Color(0xFF8A5A3A), alpha = f)
            val down = easeOut(seg(ms, 0.2f + 0.15f * k, 0.5f + 0.15f * k))
            nabiLight14(Offset(c.x, h * 0.08f + (c.y - h * 0.3f - h * 0.08f) * down), w * 0.008f, down * f, t, null, c)
        }
    }
    // the servant's choice, wholly inside a wider will
    if (wi > 0f) {
        val f = easeOut(seg(wi, 0f, 0.4f))
        val c = Offset(w * 0.5f, h * 0.42f)
        softGlow(c, w * 0.4f * f, Color(0xFFFFF0C8), 0.45f * f)
        drawCircle(Color(0xFFFFF4D8), w * 0.3f * f, c, alpha = 0.25f * f)
        val s = easeOut(seg(wi, 0.3f, 0.7f))
        drawCircle(ArtInk.Gold, w * 0.06f * s, c, alpha = f, style = Stroke(3f * dp))
        drawCircle(ArtInk.Gold, w * 0.06f * s, c, alpha = 0.25f * f)
    }
}

// ═════════════ 3. revelation, remembered and set in order (bs-sar) ═════════════

private object RevC40 {
    val Sky = Color(0xFF141A30)
    val SkyLow = Color(0xFF2E3050)
    val Sand = Color(0xFF8A7458)
    val Strip = Color(0xFFF2EEE2)
    val Trace = Color(0xFFE0584A)
    val Shelf = Color(0xFF6A4A32)
    val Slot = Color(0xFFF2E8D2)
    val Camel = Color(0xFFC8A070)
    val Sun = Color(0xFFFFD27A)
    val Moon = Color(0xFF1A1A28)
}

/**
 * Beats: seizurewave (the claim: a paper strip with a wild seizure trace), blank (the trace goes flat and a memory box
 * empties: the one who has the seizure does not know what happened), verseorder (his light, and verses set one by one in
 * their places on a shelf of surahs), onmount (his light over a camel that kneels under the weight), eclipse (the sun is
 * covered and comes out again: it did not darken for anyone's death).
 */
internal fun DrawScope.steadyrevelationScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "seizurewave" to Shot(Offset(w * 0.5f, h * 0.22f), 1.3f),
        "blank" to Shot(Offset(w * 0.5f, h * 0.25f), 1.3f),
        "verseorder" to Shot(Offset(w * 0.6f, h * 0.55f), 1.15f),
        "onmount" to Shot(Offset(w * 0.25f, h * 0.68f), 1.3f),
        "eclipse" to Shot(Offset(w * 0.8f, h * 0.25f), 1.4f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { revNight(a) }
}

private fun DrawScope.revNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sw = a.b("seizurewave")
    val bl = a.b("blank")
    val vo = a.b("verseorder")
    val om = a.b("onmount")
    val ec = a.b("eclipse")
    val ground = h * 0.82f
    nightSky(t, RevC40.Sky, RevC40.SkyLow, ground, starAlpha = 0.6f)
    drawRect(RevC40.Sand, Offset(0f, ground), Size(w, h - ground))
    // the claim: a seizure trace, then nothing remembered
    val strip = easeOut(seg(sw, 0f, 0.3f)) * (1f - 0.8f * seg(vo, 0f, 0.4f))
    if (strip > 0f) {
        val y = h * 0.2f
        drawRect(RevC40.Strip, Offset(w * 0.15f, y - h * 0.07f), Size(w * 0.7f, h * 0.14f), alpha = strip)
        val run = seg(sw, 0.1f, 0.9f)
        val flat = seg(bl, 0f, 0.4f)
        val p = Path()
        val n = 60
        for (i in 0..n) {
            val u = i / n.toFloat()
            if (u > run) break
            val wild = if (u > 0.3f) (sin(u * 90f) * 0.8f + sin(u * 37f) * 0.5f) else sin(u * 30f) * 0.2f
            val x = w * (0.17f + 0.66f * u)
            val yy = y + h * 0.05f * wild * (1f - flat)
            if (i == 0) p.moveTo(x, yy) else p.lineTo(x, yy)
        }
        drawPath(p, RevC40.Trace, alpha = strip, style = Stroke(2f * dp))
        if (bl > 0f) {
            val box = Offset(w * 0.5f, h * 0.36f)
            val g = easeOut(seg(bl, 0.2f, 0.5f)) * (1f - 0.8f * seg(vo, 0f, 0.4f))
            drawRect(Color(0xFF3A4A60), Offset(box.x - w * 0.08f, box.y - h * 0.05f), Size(w * 0.16f, h * 0.1f), alpha = g, style = Stroke(2f * dp))
            for (k in 0 until 4) drawRect(ArtInk.BrassLight, Offset(box.x - w * 0.06f + k * w * 0.032f, box.y - h * 0.02f), Size(w * 0.022f, h * 0.04f), alpha = g * (1f - seg(bl, 0.4f + 0.1f * k, 0.55f + 0.1f * k)))
            qmark21(box + Offset(0f, 0f), h * 0.05f, Color(0xFFB8C0E0), g * seg(bl, 0.8f, 1f))
        }
    }
    // he remembered every word, and set each verse in its place
    if (vo > 0f) {
        val f = easeOut(seg(vo, 0f, 0.3f)) * (1f - 0.5f * seg(om, 0f, 0.4f))
        val light = Offset(w * 0.3f, h * 0.36f)
        sirajMunir(light, w * 0.011f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        val shelfY = h * 0.6f
        for (r in 0 until 2) {
            drawRect(RevC40.Shelf, Offset(w * 0.45f, shelfY + r * h * 0.1f), Size(w * 0.45f, h * 0.012f), alpha = f)
            for (k in 0 until 6) {
                val i = r * 6 + k
                val x = w * (0.47f + 0.07f * k)
                val slotTop = shelfY + r * h * 0.1f - h * 0.07f
                drawRect(RevC40.Slot, Offset(x, slotTop), Size(w * 0.05f, h * 0.07f), alpha = f * 0.25f, style = Stroke(1f * dp))
                val order = listOf(3, 0, 7, 1, 10, 5, 2, 8, 4, 11, 6, 9).indexOf(i)
                val put = easeInOut(seg(vo, 0.15f + 0.06f * order, 0.3f + 0.06f * order))
                if (put > 0f) {
                    val from = light + Offset(w * 0.03f, 0f)
                    val to = Offset(x, slotTop)
                    val p = from + (to - from) * put
                    drawRect(RevC40.Slot, p, Size(w * 0.05f, h * 0.07f), alpha = f)
                }
            }
        }
    }
    // on his mount: the camel kneels under the weight
    if (om > 0f) {
        val f = easeOut(seg(om, 0f, 0.3f)) * (1f - 0.5f * seg(ec, 0f, 0.4f))
        val foot = Offset(w * 0.25f, ground + h * 0.02f)
        val kneel = easeInOut(seg(om, 0.35f, 0.7f))
        camel26(foot + Offset(0f, h * 0.04f * kneel), h * 0.22f, RevC40.Camel, f, t, 0f)
        val c = foot + Offset(0f, -h * 0.3f + h * 0.04f * kneel)
        sirajMunir(c, w * 0.011f, f, t, nameAt = c + Offset(0f, -h * 0.1f))
    }
    // the eclipse he would not use
    if (ec > 0f) {
        val f = easeOut(seg(ec, 0f, 0.25f))
        val c = Offset(w * 0.8f, h * 0.22f)
        val cover = bump(seg(ec, 0.15f, 0.85f))
        softGlow(c, w * 0.1f * (1f - 0.7f * cover), RevC40.Sun, 0.6f * f)
        drawCircle(RevC40.Sun, w * 0.05f, c, alpha = f)
        drawCircle(RevC40.Moon, w * 0.05f, c + Offset(w * 0.1f * (1f - 2f * seg(ec, 0.15f, 0.85f)), 0f), alpha = f * (if (abs(cover) > 0.01f) 1f else 0f))
    }
}

// ═════════════ 4. a glass box inside the light (bs-mujiza) ═════════════

private object BoxC40 {
    val Sky = Color(0xFF16182C)
    val SkyLow = Color(0xFF2E2A44)
    val Ground = Color(0xFF3A3236)
    val Glass = Color(0xFFBFD8E8)
    val Ball = Color(0xFFE8A04A)
    val Grey = Color(0xFF8A8A96)
    val Dome = Color(0xFFD8A83A)
    val Wall = Color(0xFF9A7A58)
    val Line = Color(0xFF8A7A6A)
}

/**
 * Beats: breaknorm (a row of the same grey event, and one that breaks the pattern and shines), glassbox (a ball bouncing
 * the same way in a closed glass box), notisolated (light from above passes through the walls: the box sits inside it),
 * qudsraised (his light near the House; before it the dome of Jerusalem rises into view, «بَيْتُ الْمَقْدِسِ»),
 * latescroll (a time line: the night journey first, the Persian tale far later, «أَرْدَا وِيرَاف»).
 */
internal fun DrawScope.isolatedboxScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "breaknorm" to Shot(Offset(w * 0.5f, h * 0.2f), 1.3f),
        "glassbox" to Shot(Offset(w * 0.3f, h * 0.55f), 1.3f),
        "notisolated" to Shot(Offset(w * 0.3f, h * 0.45f), 1.15f),
        "qudsraised" to Shot(Offset(w * 0.72f, h * 0.55f), 1.25f),
        "latescroll" to Shot(Offset(w * 0.5f, h * 0.85f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { boxNight(a) }
}

private fun DrawScope.boxNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bn = a.b("breaknorm")
    val gb = a.b("glassbox")
    val ni = a.b("notisolated")
    val qr = a.b("qudsraised")
    val ls = a.b("latescroll")
    val ground = h * 0.74f
    nightSky(t, BoxC40.Sky, BoxC40.SkyLow, ground, starAlpha = 0.6f)
    drawRect(BoxC40.Ground, Offset(0f, ground), Size(w, h - ground))
    // the same, the same, and then the sign
    if (bn > 0f) {
        val f = easeOut(seg(bn, 0f, 0.3f)) * (1f - 0.7f * seg(gb, 0f, 0.4f))
        for (k in 0 until 7) {
            val c = Offset(w * (0.2f + 0.1f * k), h * 0.2f)
            val g = seg(bn, 0.05f + 0.06f * k, 0.25f + 0.06f * k)
            if (k == 5) {
                val s = easeOut(seg(bn, 0.55f, 0.85f))
                softGlow(c, w * 0.06f * s, ArtInk.Gold, 0.7f * f)
                drawPath(starPath(c, w * 0.03f * s), ArtInk.Gold, alpha = f)
            } else drawCircle(BoxC40.Grey, w * 0.018f, c, alpha = f * g)
        }
    }
    // a closed box: the law repeats
    val box = Offset(w * 0.3f, h * 0.55f)
    if (gb > 0f) {
        val f = easeOut(seg(gb, 0f, 0.3f)) * (1f - 0.5f * seg(qr, 0f, 0.4f))
        drawRect(BoxC40.Glass, Offset(box.x - w * 0.15f, box.y - h * 0.15f), Size(w * 0.3f, h * 0.3f), alpha = f * 0.15f)
        drawRect(BoxC40.Glass, Offset(box.x - w * 0.15f, box.y - h * 0.15f), Size(w * 0.3f, h * 0.3f), alpha = f, style = Stroke(2f * dp))
        val bounce = abs(sin(t * 3f))
        drawCircle(BoxC40.Ball, w * 0.025f, Offset(box.x + w * 0.08f * sin(t * 1.3f), box.y + h * 0.12f - h * 0.2f * bounce), alpha = f)
    }
    // nothing is closed off from Him: the light goes through the walls
    if (ni > 0f) {
        val f = easeOut(seg(ni, 0.1f, 0.6f)) * (1f - 0.5f * seg(qr, 0f, 0.4f))
        lightShafts(Offset(box.x, 0f), PI.toFloat() / 2f, 0.8f, h * 0.8f, Color(0xFFFFF0C8), 0.5f * f, t, rays = 7)
        softGlow(box, w * 0.22f, Color(0xFFFFF0C8), 0.3f * f)
    }
    // Jerusalem raised before his eyes
    if (qr > 0f) {
        val f = easeOut(seg(qr, 0f, 0.3f)) * (1f - 0.5f * seg(ls, 0f, 0.4f))
        kaaba29(Offset(w * 0.56f, ground), w * 0.06f, f)
        val light = Offset(w * 0.56f, ground - h * 0.18f)
        sirajMunir(light, w * 0.011f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
        val rise = easeOut(seg(qr, 0.25f, 0.7f))
        val dome = Offset(w * 0.82f, h * 0.6f - h * 0.08f * rise)
        drawRoundRect(Color(0xFFFFF0C8), Offset(dome.x - w * 0.12f, dome.y - h * 0.12f), Size(w * 0.24f, h * 0.2f), CornerRadius(10f * dp), alpha = 0.18f * f * rise)
        drawRect(BoxC40.Wall, Offset(dome.x - w * 0.07f, dome.y - h * 0.01f), Size(w * 0.14f, h * 0.05f), alpha = f * rise)
        drawArc(BoxC40.Dome, 180f, 180f, true, Offset(dome.x - w * 0.045f, dome.y - h * 0.06f), Size(w * 0.09f, h * 0.1f), alpha = f * rise)
        softGlow(dome, w * 0.1f, ArtInk.Gold, 0.4f * f * rise)
        tagAt(a.tag("qudsraised"), dome + Offset(0f, -h * 0.15f), seg(qr, 0.5f, 0.8f) * (1f - seg(ls, 0f, 0.4f)), back = Back40, ink = ArtInk.Gold)
    }
    // a time line: the tale came long after
    if (ls > 0f) {
        val f = easeOut(seg(ls, 0f, 0.3f))
        val y = h * 0.87f
        val run = easeInOut(seg(ls, 0.1f, 0.6f))
        drawLine(BoxC40.Line, Offset(w * 0.15f, y), Offset(w * (0.15f + 0.7f * run), y), 3f * dp, StrokeCap.Round, alpha = f)
        val m1 = Offset(w * 0.25f, y)
        drawCircle(ArtInk.Gold, w * 0.015f, m1, alpha = f)
        softGlow(m1, w * 0.04f, ArtInk.Gold, 0.5f * f)
        val m2 = Offset(w * 0.78f, y)
        val sc = seg(ls, 0.55f, 0.8f)
        scroll14(m2 + Offset(0f, -h * 0.05f), w * 0.07f, h * 0.08f, sc, f * sc)
        tagAt(a.tag("latescroll"), m2 + Offset(0f, -h * 0.13f), seg(ls, 0.65f, 0.9f), back = Back40, ink = Color(0xFFE0D8C8))
    }
}

// ═════════════ 5. the storm and the chain of «why» (bs-malak-sahab) ═════════════

private object StormC40 {
    val Sky = Color(0xFF2A3044)
    val SkyLow = Color(0xFF5A6070)
    val Ground = Color(0xFF3A4A3A)
    val Cloud = Color(0xFF8A90A0)
    val Rain = Color(0xFFB8D0E8)
    val Bolt = Color(0xFFFFF0A0)
    val Pot = Color(0xFF5A5A62)
    val Water = Color(0xFF8AC0E0)
    val Fire = Color(0xFFF08A3A)
}

/**
 * Beats: cloudangel (heavy clouds, rain and a flash; within them a soft angel's light), whychain (a chain of question
 * marks going down, step after step, ending at «هَكَذَا جَرَى الْأَمْرُ»), mechanism (inside one cloud small gears turn:
 * this is how it works, not why), boilpot (a pot boils over a fire, «100»), wingsfaith (the angel's light once more,
 * calm, above the clouds).
 */
internal fun DrawScope.stormcauseScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "cloudangel" to Shot(Offset(w * 0.45f, h * 0.3f), 1.2f),
        "whychain" to Shot(Offset(w * 0.75f, h * 0.5f), 1.2f),
        "mechanism" to Shot(Offset(w * 0.35f, h * 0.28f), 1.35f),
        "boilpot" to Shot(Offset(w * 0.25f, h * 0.75f), 1.4f),
        "wingsfaith" to Shot(Offset(w * 0.5f, h * 0.2f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { stormSky(a) }
}

private fun DrawScope.stormSky(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ca = a.b("cloudangel")
    val wc = a.b("whychain")
    val me = a.b("mechanism")
    val bp = a.b("boilpot")
    val wf = a.b("wingsfaith")
    val ground = h * 0.86f
    drawRect(Brush.verticalGradient(listOf(StormC40.Sky, StormC40.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(StormC40.Ground, Offset(0f, ground), Size(w, h - ground))
    // clouds, rain, a flash, and an angel's light within
    val cl = easeOut(seg(ca, 0f, 0.3f))
    if (cl > 0f) {
        for (k in 0 until 3) cloud38(Offset(w * (0.25f + 0.22f * k), h * (0.22f + 0.03f * (k % 2))), w * 0.13f, StormC40.Cloud, cl)
        for (k in 0 until 18) {
            val u = fract(t * 1.4f + k * 0.37f)
            val x = w * (0.15f + 0.04f * k)
            drawLine(StormC40.Rain, Offset(x, h * 0.3f + h * 0.5f * u), Offset(x - w * 0.01f, h * 0.33f + h * 0.5f * u), 1.2f * dp, alpha = cl * 0.6f)
        }
        val flash = bump(seg(fract(t * 0.25f), 0f, 0.08f))
        if (flash > 0f) drawPath(Path().apply {
            moveTo(w * 0.52f, h * 0.28f); lineTo(w * 0.48f, h * 0.42f); lineTo(w * 0.52f, h * 0.42f); lineTo(w * 0.47f, h * 0.58f)
        }, StormC40.Bolt, alpha = flash * cl, style = Stroke(3f * dp))
        angelLight26(Offset(w * 0.47f, h * 0.2f), w * 0.02f, t, cl * seg(ca, 0.3f, 0.7f) * (1f - 0.5f * seg(wc, 0f, 0.4f)))
    }
    // why, and why again, down to: so it has been made to run
    if (wc > 0f) {
        val f = easeOut(seg(wc, 0f, 0.25f)) * (1f - 0.6f * seg(me, 0f, 0.4f))
        for (k in 0 until 4) {
            val c = Offset(w * 0.78f, h * (0.36f + 0.09f * k))
            val g = seg(wc, 0.05f + 0.12f * k, 0.25f + 0.12f * k)
            drawCircle(Color(0xFF2A2E40), w * 0.03f, c, alpha = f * g)
            qmark21(c, h * 0.035f, Color(0xFFD8DCF0), f * g)
            if (k < 3) drawLine(Color(0xFFB8BCD0), c + Offset(0f, h * 0.03f), c + Offset(0f, h * 0.06f), 1.5f * dp, alpha = f * g)
        }
        tagAt(a.tag("whychain"), Offset(w * 0.78f, h * 0.78f), seg(wc, 0.6f, 0.85f) * (1f - 0.6f * seg(me, 0f, 0.4f)), back = Back40, ink = ArtInk.Gold)
    }
    // the mechanism: how it runs
    if (me > 0f) {
        val f = easeOut(seg(me, 0f, 0.35f)) * (1f - 0.6f * seg(bp, 0f, 0.4f))
        val c = Offset(w * 0.3f, h * 0.24f)
        for (k in 0 until 2) {
            val g = c + Offset(w * 0.045f * (if (k == 0) -1f else 1f), 0f)
            val spin = t * (if (k == 0) 1.4f else -1.4f)
            drawCircle(Color(0xFFD8DCE4), w * 0.025f, g, alpha = f)
            for (j in 0 until 8) {
                val an = spin + j * PI.toFloat() / 4f
                drawLine(Color(0xFFD8DCE4), g, g + Offset(cos(an), sin(an)) * (w * 0.035f), w * 0.008f, alpha = f)
            }
            drawCircle(StormC40.Cloud, w * 0.008f, g, alpha = f)
        }
    }
    // water boils at a hundred
    if (bp > 0f) {
        val f = easeOut(seg(bp, 0f, 0.3f)) * (1f - 0.5f * seg(wf, 0f, 0.4f))
        val pot = Offset(w * 0.25f, ground - h * 0.06f)
        for (k in 0 until 3) {
            val fl = 0.8f + 0.2f * sin(t * 9f + k)
            val x = pot.x + w * 0.03f * (k - 1)
            drawPath(Path().apply { moveTo(x - w * 0.015f, ground); quadraticTo(x, ground - h * 0.05f * fl, x + w * 0.015f, ground); close() }, StormC40.Fire, alpha = f)
        }
        drawRect(StormC40.Pot, Offset(pot.x - w * 0.08f, pot.y - h * 0.1f), Size(w * 0.16f, h * 0.1f), alpha = f)
        drawRect(StormC40.Water, Offset(pot.x - w * 0.07f, pot.y - h * 0.1f), Size(w * 0.14f, h * 0.012f), alpha = f)
        for (k in 0 until 5) {
            val u = fract(t * 0.9f + k * 0.2f)
            drawCircle(Color.White, w * 0.006f * (1f - u), pot + Offset(w * (-0.05f + 0.025f * k), -h * 0.11f - h * 0.08f * u), alpha = f * seg(bp, 0.3f, 0.6f) * (1f - u))
        }
        tagAt(a.tag("boilpot"), pot + Offset(w * 0.14f, -h * 0.08f), seg(bp, 0.4f, 0.7f) * (1f - seg(wf, 0f, 0.4f)), back = Back40, ink = ArtInk.Ivory)
    }
    // the angels: a light we believe in without asking how
    if (wf > 0f) {
        val f = easeOut(seg(wf, 0.1f, 0.6f))
        angelLight26(Offset(w * 0.5f, h * 0.12f), w * 0.028f, t, f)
        softGlow(Offset(w * 0.5f, h * 0.12f), w * 0.15f, Color(0xFFE8F0FF), 0.35f * f)
    }
}
