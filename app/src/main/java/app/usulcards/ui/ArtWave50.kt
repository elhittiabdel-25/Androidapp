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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 50 (2026-11-03): «رياض الصالحين»، «آفات اللسان» (end) and «آداب يومية». Two who turn their backs for three
 * nights until one greets first; a street where the greeting leads to love; a door knocked three times; a shared tray
 * seen from above; a sitting circle that makes room. Shaytan is a dark smoke; Allah is never drawn; the Prophet ﷺ is
 * only light.
 */

private val Back50 = Color(0xE6181420)

// ═════════════ 1. the one who greets first (riyad-hijran) ═════════════

private object TurnC50 {
    val Sky = Color(0xFF1A2038)
    val SkyLow = Color(0xFF8A6A7A)
    val Ground = Color(0xFF3A3236)
    val Person = Color(0xFFE6DCC8)
    val Gold = Color(0xFFE8C46A)
    val Moon = Color(0xFFF2EEDC)
    val Light = Color(0xFFBFE2FF)
    val Smoke = Color(0xFF2A2430)
}

/**
 * Beats: backs (two meet and each turns away; three moons cross the sky one after another), greetfirst (one turns back
 * and greets; a warm glow over him, and over the other too when he answers), records (many small lights rise on two
 * days of the week; the two who quarrel keep theirs held down by a thread until they make peace), sowing (dark smoke
 * winds between two and sets them apart).
 */
internal fun DrawScope.turnbackScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "backs" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "greetfirst" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "records" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "sowing" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { turnDusk(a) }
}

private fun DrawScope.turnDusk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bk = a.b("backs")
    val gf = a.b("greetfirst")
    val rc = a.b("records")
    val sw = a.b("sowing")
    val ground = h * 0.82f
    nightSky(t, TurnC50.Sky, TurnC50.SkyLow, ground, starAlpha = 0.6f)
    drawRect(TurnC50.Ground, Offset(0f, ground), Size(w, h - ground))
    if (bk > 0f) {
        val f = easeOut(seg(bk, 0f, 0.3f)) * (1f - seg(gf, 0f, 0.4f))
        val turn = seg(bk, 0.15f, 0.3f)
        figure(Offset(w * 0.4f, ground), h * 0.32f, TurnC50.Person, f, lean = 0.12f * turn, walk = t * 5f * (1f - turn))
        figure(Offset(w * 0.6f, ground), h * 0.32f, TurnC50.Person, f, lean = -0.12f * turn, walk = t * 5f * (1f - turn))
        for (k in 0 until 3) {
            val u = seg(bk, 0.25f + 0.22f * k, 0.5f + 0.22f * k)
            if (u <= 0f || u >= 1f) continue
            val c = Offset(w * (0.1f + 0.8f * u), h * (0.3f - 0.18f * sin(PI.toFloat() * u)))
            crescent12(c, w * 0.025f, TurnC50.Moon, TurnC50.Sky, f * bump(u))
        }
    }
    if (gf > 0f) {
        val f = easeOut(seg(gf, 0f, 0.3f)) * (1f - seg(rc, 0f, 0.4f))
        val back = easeInOut(seg(gf, 0.1f, 0.35f))
        figure(Offset(w * 0.38f, ground), h * 0.34f, TurnC50.Gold, f, lean = 0.12f * (1f - back), arms = 0.7f * back)
        voice12(Offset(w * 0.42f, ground - h * 0.3f), w * 0.04f, t, TurnC50.Gold, f * back)
        softGlow(Offset(w * 0.38f, ground - h * 0.32f), w * 0.06f, TurnC50.Gold, 0.6f * f * seg(gf, 0.3f, 0.5f))
        val answer = seg(gf, 0.55f, 0.8f)
        figure(Offset(w * 0.62f, ground), h * 0.34f, TurnC50.Person, f, lean = -0.12f * (1f - answer), arms = -0.4f * answer)
        softGlow(Offset(w * 0.62f, ground - h * 0.32f), w * 0.06f, TurnC50.Gold, 0.6f * f * answer)
    }
    if (rc > 0f) {
        val f = easeOut(seg(rc, 0f, 0.3f)) * (1f - seg(sw, 0f, 0.4f))
        for (k in 0 until 7) {
            val x = w * (0.08f + 0.12f * k)
            val held = k == 3 || k == 4
            figure(Offset(x, ground), h * 0.22f, TurnC50.Person, f * 0.85f)
            val rise = if (held) 0.08f * sin(t * 2f + k) else easeOut(seg(rc, 0.15f + 0.05f * k, 0.6f + 0.05f * k))
            val c = Offset(x, ground - h * 0.28f - h * 0.4f * rise)
            angelLight26(c, w * 0.006f, t + k, f)
            if (held) drawLine(TurnC50.Light, c, Offset(x, ground - h * 0.24f), 1f * dp, alpha = f * 0.6f)
        }
        drawLine(Color(0xFFE0584A), Offset(w * 0.44f, ground - h * 0.12f), Offset(w * 0.52f, ground - h * 0.12f), 2f * dp, alpha = f * 0.7f)
    }
    if (sw > 0f) {
        val f = easeOut(seg(sw, 0f, 0.3f))
        val apart = easeInOut(seg(sw, 0.35f, 0.9f))
        val l = Offset(w * (0.42f - 0.18f * apart), ground)
        val r = Offset(w * (0.58f + 0.18f * apart), ground)
        figure(l, h * 0.32f, TurnC50.Person, f, lean = 0.1f * apart)
        figure(r, h * 0.32f, TurnC50.Person, f, lean = -0.1f * apart)
        smoke12(Offset(w * 0.5f, ground - h * 0.02f), Offset(w * 0.5f, h * 0.3f), seg(sw, 0.1f, 0.6f), t, f, TurnC50.Smoke)
    }
}

// ═════════════ 2. spread the greeting (riyad-salam) ═════════════

private object SalamC50 {
    val Sky = Color(0xFFB4D4E8)
    val SkyLow = Color(0xFFF4E8CC)
    val Ground = Color(0xFFD6BE94)
    val Wall = Color(0xFFE2CCA2)
    val Trim = Color(0xFF8A6A44)
    val Person = Color(0xFF3A3040)
    val Gold = Color(0xFFE8B84A)
    val Heart = Color(0xFFE0645A)
    val Gate = Color(0xFF6A8A5A)
    val Camel = Color(0xFFB88A58)
    val Plate = Color(0xFFF2EADA)
    val Room = Color(0xFF2A2430)
}

/**
 * Beats: lovechain (a chain lights link by link: the greeting, then love, then a light of faith, then a garden gate:
 * «السَّلَامِ», «الْمَحَبَّةِ»), plateall (one gives food and greets a neighbour he knows and a traveller he does not),
 * whofirst (four small pairs, each with its arrow: the rider to the walker, the walker to the seated, the few to the
 * many, the young to the old), tens (three greetings, each longer, and their counts: «10», «20», «30»), quietnight (a
 * dark room: sleepers and one awake; a soft greeting passes over the sleepers and reaches him).
 */
internal fun DrawScope.salamstreetScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "lovechain" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "plateall" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "whofirst" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "tens" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "quietnight" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { salamDay(a) }
}

private fun DrawScope.salamDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val lc = a.b("lovechain")
    val pa = a.b("plateall")
    val wf = a.b("whofirst")
    val tn = a.b("tens")
    val qn = a.b("quietnight")
    val ground = h * 0.84f
    daySky12(SalamC50.Sky, SalamC50.SkyLow, ground, SalamC50.Ground)
    if (lc > 0f) {
        val f = easeOut(seg(lc, 0f, 0.3f)) * (1f - seg(pa, 0f, 0.4f))
        for (k in 0 until 4) {
            val c = Offset(w * (0.16f + 0.23f * k), h * 0.48f)
            val g = easeOut(seg(lc, 0.1f + 0.2f * k, 0.3f + 0.2f * k))
            if (k > 0) drawLine(SalamC50.Gold, c + Offset(-w * 0.17f, 0f), c + Offset(-w * 0.06f, 0f), 3f * dp, StrokeCap.Round, alpha = f * g)
            drawCircle(Color(0xFFFFF6E0), w * 0.06f, c, alpha = f * g)
            drawCircle(SalamC50.Gold, w * 0.06f, c, alpha = f * g, style = Stroke(2f * dp))
            when (k) {
                0 -> voice12(c + Offset(-w * 0.02f, 0f), w * 0.03f, t, SalamC50.Trim, f * g)
                1 -> heart12(c, w * 0.06f, SalamC50.Heart, f * g, fill = true)
                2 -> softGlow(c, w * 0.04f, SalamC50.Gold, f * g)
                else -> door12(c + Offset(0f, h * 0.06f), w * 0.05f, h * 0.12f, seg(lc, 0.85f, 1f), SalamC50.Gate, Color(0xFF3A4A2A), f * g, Color(0xFFDFF2B0))
            }
            if (k < 2) tagAt(a.tag("lovechain", 0, k), c + Offset(0f, h * 0.14f), f * g, back = Back50, ink = ArtInk.Gold)
        }
    }
    if (pa > 0f) {
        val f = easeOut(seg(pa, 0f, 0.3f)) * (1f - seg(wf, 0f, 0.4f))
        house12(Offset(w * 0.16f, ground), w * 0.2f, SalamC50.Wall, SalamC50.Trim, f, 0.4f)
        figure(Offset(w * 0.32f, ground), h * 0.3f, SalamC50.Person, f, arms = 0.6f)
        drawOval(SalamC50.Plate, Offset(w * 0.24f, ground - h * 0.2f), Size(w * 0.05f, h * 0.018f), alpha = f)
        figure(Offset(w * 0.5f, ground), h * 0.3f, SalamC50.Person, f * seg(pa, 0.1f, 0.25f), arms = -0.3f)
        val trav = easeInOut(seg(pa, 0.35f, 0.7f))
        figure(Offset(w * (0.95f - 0.25f * trav), ground), h * 0.3f, SalamC50.Person, f, walk = t * 5f * (1f - trav))
        drawLine(SalamC50.Trim, Offset(w * (0.97f - 0.25f * trav), ground), Offset(w * (0.97f - 0.25f * trav), ground - h * 0.32f), 2f * dp, alpha = f)
        voice12(Offset(w * 0.36f, ground - h * 0.28f), w * 0.04f, t, SalamC50.Gold, f * seg(pa, 0.15f, 0.3f))
        voice12(Offset(w * 0.56f, ground - h * 0.28f), w * 0.05f, t, SalamC50.Gold, f * seg(pa, 0.6f, 0.75f))
    }
    if (wf > 0f) {
        val f = easeOut(seg(wf, 0f, 0.3f)) * (1f - seg(tn, 0f, 0.4f))
        for (k in 0 until 4) {
            val cx = w * (0.14f + 0.24f * k)
            val g = seg(wf, 0.1f + 0.2f * k, 0.3f + 0.2f * k)
            val y = ground
            when (k) {
                0 -> { camel26(Offset(cx - w * 0.04f, y), h * 0.2f, SalamC50.Camel, f * g, t); figure(Offset(cx + w * 0.06f, y), h * 0.2f, SalamC50.Person, f * g) }
                1 -> { figure(Offset(cx - w * 0.04f, y), h * 0.2f, SalamC50.Person, f * g, walk = t * 5f); sit12(Offset(cx + w * 0.06f, y), h * 0.2f, SalamC50.Person, f * g, -1f) }
                2 -> { figure(Offset(cx - w * 0.06f, y), h * 0.2f, SalamC50.Person, f * g); for (j in 0 until 3) figure(Offset(cx + w * (0.02f + 0.03f * j), y), h * 0.2f, SalamC50.Person, f * g * 0.8f) }
                else -> { child12(Offset(cx - w * 0.04f, y), h * 0.22f, SalamC50.Person, f * g); figure(Offset(cx + w * 0.05f, y), h * 0.22f, SalamC50.Person, f * g, lean = 0.1f) }
            }
            val from = Offset(cx - w * 0.06f, y - h * 0.28f)
            penLine(listOf(from, from + Offset(w * 0.1f, 0f)), seg(wf, 0.2f + 0.2f * k, 0.35f + 0.2f * k), SalamC50.Gold, 2.5f * dp, f)
            arrowHead(from + Offset(w * 0.1f, 0f), Offset(1f, 0f), w * 0.015f, SalamC50.Gold, 2.5f * dp, f * seg(wf, 0.3f + 0.2f * k, 0.35f + 0.2f * k))
        }
    }
    if (tn > 0f) {
        val f = easeOut(seg(tn, 0f, 0.3f)) * (1f - seg(qn, 0f, 0.4f))
        for (k in 0 until 3) {
            val y = h * (0.3f + 0.2f * k)
            val g = easeOut(seg(tn, 0.1f + 0.25f * k, 0.3f + 0.25f * k))
            drawRoundRect(SalamC50.Gold, Offset(w * 0.2f, y - h * 0.025f), Size(w * 0.18f * (k + 1) * g, h * 0.05f), CornerRadius(h * 0.025f), alpha = f * 0.75f)
            tagAt(a.tag("tens", 0, k), Offset(w * 0.82f, y), f * g, back = Back50, ink = ArtInk.Gold)
        }
    }
    if (qn > 0f) {
        val f = easeOut(seg(qn, 0f, 0.3f))
        drawRect(SalamC50.Room, Offset(w * 0.06f, h * 0.18f), Size(w * 0.88f, ground - h * 0.18f), alpha = f)
        for (k in 0 until 3) {
            val c = Offset(w * (0.2f + 0.16f * k), ground - h * 0.06f)
            nameRoundel(c, w * 0.03f, null, Roundel.PlainFill, Roundel.PlainRim, f * 0.45f)
        }
        nameRoundel(Offset(w * 0.78f, ground - h * 0.14f), w * 0.035f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val u = seg(qn, 0.2f, 0.85f)
        val c = Offset(w * (0.1f + 0.64f * u), ground - h * 0.14f)
        softGlow(c, w * 0.03f, SalamC50.Gold, 0.6f * f * bump(seg(qn, 0.15f, 0.95f)))
        if (u > 0.95f) softGlow(Offset(w * 0.78f, ground - h * 0.14f), w * 0.06f, SalamC50.Gold, 0.6f * f)
    }
}

// ═════════════ 3. three knocks (riyad-istidhan) ═════════════

private object KnockC50 {
    val Sky = Color(0xFFC0D8E8)
    val SkyLow = Color(0xFFF2E6CC)
    val Ground = Color(0xFFCBB48C)
    val Wall = Color(0xFFDCC4A0)
    val Trim = Color(0xFF8A6A44)
    val Leaf = Color(0xFF6A4A2E)
    val Person = Color(0xFF3A3040)
    val Gold = Color(0xFFE8B84A)
    val Sight = Color(0xFF8AA0C0)
    val Red = Color(0xFFC0482E)
}

/**
 * Beats: threetimes (a closed door; three knocks ring out, «1», «2», «3»; no answer, and he turns home), aside (he stands
 * to the side of the door, not before it: a line of sight from the doorway falls on the wall), askin (at the door his
 * greeting and his asking: «السَّلَامُ عَلَيْكُمْ», «أَأَدْخُلُ»), sayname (a companion, a plain roundel, knocks; from within, a
 * question; the answer «أَنَا» is met with a puzzled mark, until he gives his name), grownchild (a boy grows tall and knocks at his parents' door
 * too).
 */
internal fun DrawScope.threeknocksScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threetimes" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "aside" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "askin" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "sayname" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "grownchild" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { knockDay(a) }
}

private fun DrawScope.knockDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tt = a.b("threetimes")
    val asd = a.b("aside")
    val ak = a.b("askin")
    val sn = a.b("sayname")
    val gc = a.b("grownchild")
    val ground = h * 0.84f
    daySky12(KnockC50.Sky, KnockC50.SkyLow, ground, KnockC50.Ground)
    val anyBeat = tt > 0f || asd > 0f || ak > 0f || sn > 0f || gc > 0f
    if (anyBeat) {
        drawRect(KnockC50.Wall, Offset(w * 0.3f, ground - h * 0.6f), Size(w * 0.5f, h * 0.6f))
        drawRect(KnockC50.Trim, Offset(w * 0.29f, ground - h * 0.62f), Size(w * 0.52f, h * 0.03f))
    }
    val sill = Offset(w * 0.55f, ground)
    if (tt > 0f) {
        val f = easeOut(seg(tt, 0f, 0.3f)) * (1f - seg(asd, 0f, 0.4f))
        door12(sill, w * 0.14f, h * 0.44f, 0f, KnockC50.Trim, KnockC50.Leaf, f)
        val home = easeInOut(seg(tt, 0.72f, 1f))
        figure(Offset(w * (0.36f - 0.3f * home), ground), h * 0.3f, KnockC50.Person, f, walk = t * 5f * home, arms = if (home > 0f) 0f else 0.7f)
        for (k in 0 until 3) {
            val g = seg(tt, 0.1f + 0.18f * k, 0.25f + 0.18f * k)
            if (g > 0f && g < 1f) drawCircle(KnockC50.Gold, w * (0.02f + 0.05f * g), sill + Offset(-w * 0.05f, -h * 0.22f), alpha = f * (1f - g), style = Stroke(2f * dp))
            tagAt(a.tag("threetimes", 0, k), Offset(w * (0.42f + 0.07f * k), h * 0.2f), f * seg(tt, 0.1f + 0.18f * k, 0.2f + 0.18f * k), back = Back50, ink = ArtInk.Gold)
        }
    }
    if (asd > 0f) {
        val f = easeOut(seg(asd, 0f, 0.3f)) * (1f - seg(ak, 0f, 0.4f))
        door12(sill, w * 0.14f, h * 0.44f, 0.4f * seg(asd, 0.3f, 0.6f), KnockC50.Trim, KnockC50.Leaf, f)
        val step = easeInOut(seg(asd, 0.1f, 0.4f))
        figure(Offset(w * (0.55f - 0.2f * step), ground), h * 0.3f, KnockC50.Person, f, walk = t * 5f * (1f - step))
        penLine(listOf(sill + Offset(0f, -h * 0.25f), sill + Offset(0f, -h * 0.25f) + Offset(0f, h * 0.24f)), seg(asd, 0.5f, 0.7f), KnockC50.Sight, 2f * dp, f, dash = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp)))
    }
    if (ak > 0f) {
        val f = easeOut(seg(ak, 0f, 0.3f)) * (1f - seg(sn, 0f, 0.4f))
        door12(sill, w * 0.14f, h * 0.44f, 0f, KnockC50.Trim, KnockC50.Leaf, f)
        figure(Offset(w * 0.38f, ground), h * 0.3f, KnockC50.Person, f)
        for (k in 0 until 2) {
            voice12(Offset(w * 0.42f, ground - h * 0.28f), w * 0.04f, t + k, KnockC50.Gold, f * seg(ak, 0.1f + 0.35f * k, 0.3f + 0.35f * k))
            tagAt(a.tag("askin", 0, k), Offset(w * 0.3f, h * (0.24f + 0.12f * k)), f * seg(ak, 0.15f + 0.35f * k, 0.3f + 0.35f * k), back = Back50, ink = ArtInk.Gold)
        }
    }
    if (sn > 0f) {
        val f = easeOut(seg(sn, 0f, 0.3f)) * (1f - seg(gc, 0f, 0.4f))
        door12(sill, w * 0.14f, h * 0.44f, 0f, KnockC50.Trim, KnockC50.Leaf, f)
        nameRoundel(Offset(w * 0.38f, ground - h * 0.15f), w * 0.045f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        qMark14(sill + Offset(0f, -h * 0.55f), h * 0.12f, KnockC50.Sight, f * seg(sn, 0.15f, 0.3f))
        tagAt(a.tag("sayname"), Offset(w * 0.3f, h * 0.28f), f * seg(sn, 0.35f, 0.5f), back = Back50, ink = ArtInk.Ivory)
        qMark14(Offset(w * 0.45f, h * 0.22f), h * 0.1f, KnockC50.Red, f * seg(sn, 0.5f, 0.65f) * (1f - seg(sn, 0.75f, 0.85f)))
        tick14(Offset(w * 0.45f, h * 0.22f), w * 0.03f, seg(sn, 0.85f, 1f) * f)
    }
    if (gc > 0f) {
        val f = easeOut(seg(gc, 0f, 0.3f))
        door12(sill, w * 0.14f, h * 0.44f, 0f, KnockC50.Trim, KnockC50.Leaf, f)
        val grow = easeInOut(seg(gc, 0.1f, 0.6f))
        figure(Offset(w * 0.38f, ground), h * (0.16f + 0.14f * grow), KnockC50.Person, f, arms = 0.7f * seg(gc, 0.6f, 0.7f))
        val k = seg(gc, 0.7f, 0.95f)
        if (k > 0f && k < 1f) drawCircle(KnockC50.Gold, w * (0.02f + 0.05f * k), sill + Offset(-w * 0.05f, -h * 0.22f), alpha = f * (1f - k), style = Stroke(2f * dp))
    }
}

// ═════════════ 4. the shared tray (riyad-taam) ═════════════

private object TrayC50 {
    val Rug = Color(0xFF6A2E2E)
    val RugEdge = Color(0xFFD8A65A)
    val Tray = Color(0xFFC8A060)
    val TrayRim = Color(0xFF8A6430)
    val Food = Color(0xFFE8C88A)
    val Rice = Color(0xFFF4ECD6)
    val Hand = Color(0xFFE6D2B4)
    val Cup = Color(0xFFD8C4A0)
    val Breath = Color(0xFFF4F0E8)
    val Person = Color(0xFFE6DCC8)
    val Gold = Color(0xFFF2C45A)
    val Smoke = Color(0xFF2A2430)
}

/**
 * Beats: yourside (a round tray seen from above: a ring of light wanders all over it, then settles on the near side: «سَمِّ
 * اللَّهَ», «وَكُلْ بِيَمِينِكَ», «وَكُلْ مِمَّا يَلِيكَ»), latename (half the tray eaten; the words of the late naming
 * come), noblame (dishes in a row; one is pushed aside quietly, with no mark against it), threebreaths (a cup and three
 * breaths taken away from its rim), rightward (a cup goes round a circle of people, always to the right), nosupper (a
 * house door where the name of Allah is said; a dark smoke turns away from it).
 */
internal fun DrawScope.sharedtrayScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "yourside" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "latename" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "noblame" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "threebreaths" to Shot(Offset(w * 0.5f, h * 0.5f), 1.3f),
        "rightward" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "nosupper" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { trayTop(a) }
}

private fun DrawScope.trayTop(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ys = a.b("yourside")
    val ln = a.b("latename")
    val nb = a.b("noblame")
    val tb = a.b("threebreaths")
    val rw = a.b("rightward")
    val ns = a.b("nosupper")
    drawRect(TrayC50.Rug)
    drawRect(TrayC50.RugEdge, Offset(w * 0.03f, h * 0.05f), Size(w * 0.94f, h * 0.9f), style = Stroke(3f * dp))
    for (k in 0 until 10) drawPath(starPath(Offset(w * (0.07f + 0.1f * k), h * 0.09f), w * 0.012f), TrayC50.RugEdge, alpha = 0.5f)
    val c = Offset(w * 0.5f, h * 0.52f)
    val r = h * 0.32f
    if (ys > 0f || ln > 0f) {
        val f = easeOut(seg(ys, 0f, 0.3f)) * (1f - seg(nb, 0f, 0.4f))
        drawCircle(TrayC50.TrayRim, r * 1.06f, c, alpha = f)
        drawCircle(TrayC50.Tray, r, c, alpha = f)
        val eaten = seg(ln, 0.1f, 0.6f)
        for (k in 0 until 40) {
            val ang = k * 2.4f
            val rr = r * 0.85f * kotlin.math.sqrt((k + 0.5f) / 40f)
            val p = c + Offset(cos(ang) * rr, sin(ang) * rr)
            if (p.y > c.y + r * 0.3f - r * 1.2f * eaten) continue
            drawCircle(TrayC50.Rice, w * 0.008f, p, alpha = f)
        }
        val settle = easeInOut(seg(ys, 0.35f, 0.65f))
        val wander = c + Offset(cos(t * 2.1f) * r * 0.6f, sin(t * 1.7f) * r * 0.5f)
        val near = c + Offset(0f, r * 0.7f)
        val hand = wander + (near - wander) * settle
        softGlow(hand, w * 0.04f, TrayC50.Gold, 0.6f * f)
        drawCircle(TrayC50.Gold, w * 0.025f, hand, alpha = f, style = Stroke(2.5f * dp))
        for (k in 0 until 3) tagAt(a.tag("yourside", 0, k), Offset(w * (0.18f + 0.32f * k), h * 0.08f + h * 0.05f * (k % 2)), f * seg(ys, 0.1f + 0.25f * k, 0.25f + 0.25f * k), back = Back50, ink = ArtInk.Gold)
        if (ln > 0f) {
            val g = easeOut(seg(ln, 0.55f, 0.75f))
            softGlow(c, r * 0.5f, TrayC50.Gold, 0.4f * g * f)
        }
    }
    if (nb > 0f) {
        val f = easeOut(seg(nb, 0f, 0.3f)) * (1f - seg(tb, 0f, 0.4f))
        for (k in 0 until 4) {
            val push = if (k == 2) easeInOut(seg(nb, 0.45f, 0.75f)) else 0f
            val p = Offset(w * (0.2f + 0.2f * k), h * (0.5f + 0.25f * push))
            drawCircle(TrayC50.TrayRim, w * 0.07f, p, alpha = f * (1f - 0.3f * push))
            drawCircle(TrayC50.Food, w * 0.055f, p, alpha = f * (1f - 0.3f * push))
        }
    }
    if (tb > 0f) {
        val f = easeOut(seg(tb, 0f, 0.3f)) * (1f - seg(rw, 0f, 0.4f))
        val cup = Offset(w * 0.45f, h * 0.62f)
        cup12(cup, w * 0.14f, TrayC50.Cup, f, Color(0xFFBFE0F0))
        for (k in 0 until 3) {
            val u = seg(tb, 0.15f + 0.25f * k, 0.38f + 0.25f * k)
            if (u <= 0f || u >= 1f) continue
            drawCircle(TrayC50.Breath, w * (0.02f + 0.04f * u), cup + Offset(w * (0.14f + 0.1f * u), -h * 0.12f), alpha = f * 0.5f * (1f - u))
        }
        for (k in 0 until 3) drawCircle(TrayC50.Gold, w * 0.012f, Offset(w * (0.75f + 0.05f * k), h * 0.3f), alpha = f * seg(tb, 0.2f + 0.25f * k, 0.3f + 0.25f * k))
    }
    if (rw > 0f) {
        val f = easeOut(seg(rw, 0f, 0.3f)) * (1f - seg(ns, 0f, 0.4f))
        val rr = h * 0.3f
        for (k in 0 until 6) {
            val ang = -PI.toFloat() / 2f + k * PI.toFloat() / 3f
            val p = c + Offset(cos(ang) * rr * 1.3f, sin(ang) * rr)
            drawCircle(TrayC50.Person, w * 0.03f, p, alpha = f)
        }
        val u = seg(rw, 0.1f, 0.95f) * 6f
        val ang = -PI.toFloat() / 2f + u * PI.toFloat() / 3f
        val cp = c + Offset(cos(ang) * rr * 1.0f, sin(ang) * rr * 0.75f)
        cup12(cp + Offset(0f, h * 0.02f), w * 0.04f, TrayC50.Gold, f)
        drawArc(TrayC50.Gold, -90f, 60f * u, false, Offset(c.x - rr * 0.8f, c.y - rr * 0.6f), Size(rr * 1.6f, rr * 1.2f), alpha = f * 0.5f, style = Stroke(1.5f * dp))
    }
    if (ns > 0f) {
        val f = easeOut(seg(ns, 0f, 0.3f))
        drawRect(Color(0xFF1C1A26), alpha = f)
        val sill = Offset(w * 0.5f, h * 0.86f)
        door12(sill, w * 0.16f, h * 0.5f, 0.3f, Color(0xFF6A5A4A), Color(0xFF3A2A1E), f)
        softGlow(sill + Offset(0f, -h * 0.25f), w * 0.1f, TrayC50.Gold, 0.5f * f * seg(ns, 0.1f, 0.3f))
        val come = seg(ns, 0.1f, 0.5f)
        val turn = easeInOut(seg(ns, 0.5f, 0.9f))
        val tip = Offset(w * (0.9f - 0.22f * come + 0.3f * turn), h * 0.5f)
        smoke12(Offset(w * 1.02f, h * 0.35f), tip, 0.6f + 0.4f * come, t, f * (1f - turn * 0.6f), TrayC50.Smoke)
    }
}

// ═════════════ 5. a circle that makes room (riyad-majlis) ═════════════

private object CircleC50 {
    val Wall = Color(0xFF3A2E2A)
    val WallLit = Color(0xFF5A4636)
    val Floor = Color(0xFF6A4A36)
    val Rug = Color(0xFF8A3A2E)
    val Person = Color(0xFFE6DCC8)
    val New = Color(0xFFE8C46A)
    val Scribble = Color(0xFF8A8296)
    val Gold = Color(0xFFF2C45A)
    val Red = Color(0xFFE0584A)
    val Grey = Color(0xFF6A6470)
}

/**
 * Beats: makeroom (a circle of people sitting; one comes, and they move apart to make room rather than raising anyone),
 * atedge (one who comes sits where the circle ends, not between two), clearing (scribbles of idle talk fill the air over
 * a gathering; at its close a clean light sweeps them away), threesit (three sit; two lean together to whisper and the
 * third is left out — struck out), remembered (two gatherings: one grey where Allah is not remembered, one warm where
 * He is).
 */
internal fun DrawScope.majlisringScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "makeroom" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "atedge" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "clearing" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "threesit" to Shot(Offset(w * 0.5f, h * 0.6f), 1.25f),
        "remembered" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { circleRoom(a) }
}

private fun DrawScope.circleRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val mr = a.b("makeroom")
    val ae = a.b("atedge")
    val cl = a.b("clearing")
    val ts = a.b("threesit")
    val rm = a.b("remembered")
    val floor = h * 0.62f
    drawRect(Brush.verticalGradient(listOf(CircleC50.Wall, CircleC50.WallLit), 0f, floor), Offset.Zero, Size(w, floor))
    drawRect(CircleC50.Floor, Offset(0f, floor), Size(w, h - floor))
    for (k in 0 until 3) drawPath(archPath26(w * (0.25f + 0.25f * k), h * 0.12f, floor, w * 0.07f), Color(0xFF2A201C), alpha = 0.6f)
    val c = Offset(w * 0.5f, h * 0.8f)
    fun ring(n: Int, spread: Float, alpha: Float, color: Color = CircleC50.Person, skip: Int = -1) {
        for (k in 0 until n) {
            if (k == skip) continue
            val ang = PI.toFloat() * (0.05f + 0.9f * k / (n - 1)) * spread + PI.toFloat() * (1f - spread) / 2f
            val p = Offset(c.x - cos(ang) * w * 0.34f, c.y - sin(ang) * h * 0.14f)
            sit12(p, h * 0.2f, color, alpha, if (p.x < c.x) 1f else -1f)
        }
    }
    if (mr > 0f || ae > 0f) {
        val f = easeOut(seg(mr, 0f, 0.3f)) * (1f - seg(cl, 0f, 0.4f))
        drawOval(CircleC50.Rug, Offset(c.x - w * 0.3f, c.y - h * 0.1f), Size(w * 0.6f, h * 0.14f), alpha = f)
        val open = easeInOut(seg(mr, 0.3f, 0.7f))
        ring(6, 1f - 0.12f * open, f)
        if (ae <= 0f) {
            val come = easeInOut(seg(mr, 0.05f, 0.6f))
            if (come < 1f) figure(Offset(w * (0.95f - 0.2f * come), floor + h * 0.12f), h * 0.26f, CircleC50.New, f, walk = t * 5f)
            else sit12(Offset(w * 0.78f, c.y - h * 0.02f), h * 0.2f, CircleC50.New, f, -1f)
        }
    }
    if (ae > 0f) {
        val f = easeOut(seg(ae, 0f, 0.3f)) * (1f - seg(cl, 0f, 0.4f))
        val gap = Offset(w * 0.5f, c.y - h * 0.14f)
        crossMark(gap + Offset(0f, -h * 0.2f), w * 0.025f, CircleC50.Red, 3f * dp, f * seg(ae, 0.2f, 0.35f) * (1f - seg(ae, 0.6f, 0.7f)))
        val go = easeInOut(seg(ae, 0.3f, 0.8f))
        val p = Offset(w * (0.6f + 0.22f * go), c.y + h * 0.06f * go)
        if (go < 1f) figure(p, h * 0.26f, CircleC50.New, f, walk = t * 5f)
        else sit12(Offset(w * 0.84f, c.y + h * 0.04f), h * 0.2f, CircleC50.New, f, -1f)
        tick14(Offset(w * 0.88f, c.y - h * 0.24f), w * 0.025f, seg(ae, 0.8f, 0.95f) * f)
    }
    if (cl > 0f) {
        val f = easeOut(seg(cl, 0f, 0.3f)) * (1f - seg(ts, 0f, 0.4f))
        ring(6, 1f, f)
        val sweep = easeInOut(seg(cl, 0.55f, 0.9f))
        for (k in 0 until 12) {
            val p = Offset(w * (0.14f + 0.065f * k), h * (0.3f + 0.12f * sin(k * 1.7f)))
            if (p.x < w * sweep) continue
            val sc = Path().apply { moveTo(p.x - w * 0.02f, p.y); for (j in 1..5) lineTo(p.x - w * 0.02f + w * 0.008f * j, p.y + (if (j % 2 == 0) -1f else 1f) * h * 0.02f) }
            drawPath(sc, CircleC50.Scribble, alpha = f * seg(cl, 0.02f * k, 0.15f + 0.02f * k), style = Stroke(1.6f * dp))
        }
        if (sweep > 0f && sweep < 1f) drawRect(Brush.horizontalGradient(listOf(Color.Transparent, CircleC50.Gold.copy(alpha = 0.5f), Color.Transparent), w * sweep - w * 0.1f, w * sweep + w * 0.02f), Offset(w * sweep - w * 0.1f, h * 0.15f), Size(w * 0.12f, h * 0.4f), alpha = f)
    }
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, 0.3f)) * (1f - seg(rm, 0f, 0.4f))
        val lean = easeInOut(seg(ts, 0.15f, 0.4f))
        sit12(Offset(w * 0.38f, c.y), h * 0.24f, CircleC50.Person, f, 1f, bob = h * 0.02f * lean)
        sit12(Offset(w * 0.5f, c.y), h * 0.24f, CircleC50.Person, f, -1f, bob = h * 0.02f * lean)
        sit12(Offset(w * 0.68f, c.y), h * 0.24f, CircleC50.Grey, f, -1f)
        voice12(Offset(w * 0.44f, c.y - h * 0.22f), w * 0.02f, t, CircleC50.Scribble, f * lean)
        crossMark(Offset(w * 0.44f, c.y - h * 0.38f), w * 0.03f, CircleC50.Red, 3f * dp, f * seg(ts, 0.5f, 0.7f))
    }
    if (rm > 0f) {
        val f = easeOut(seg(rm, 0f, 0.3f))
        for (side in 0 until 2) {
            val cx = w * (0.27f + 0.46f * side)
            val warm = side == 1
            val g = seg(rm, 0.1f + 0.3f * side, 0.3f + 0.3f * side)
            for (k in 0 until 4) {
                val ang = PI.toFloat() * (0.1f + 0.8f * k / 3f)
                val p = Offset(cx - cos(ang) * w * 0.12f, c.y - sin(ang) * h * 0.08f)
                sit12(p, h * 0.16f, if (warm) CircleC50.Person else CircleC50.Grey, f * g, if (p.x < cx) 1f else -1f)
            }
            if (warm) softGlow(Offset(cx, c.y - h * 0.18f), w * 0.14f, CircleC50.Gold, 0.5f * f * g)
            else drawCircle(CircleC50.Grey, w * 0.12f, Offset(cx, c.y - h * 0.16f), alpha = f * g * 0.25f)
        }
    }
}
