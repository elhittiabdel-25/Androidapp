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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 45 (2026-10-27): «بصائر»، الجزء الثالث، «أفكار ضالة ويقين». A court whose pillars stand only on a fixed measure;
 * a brain that carries out and a television that does not make the programme; a dial tuned at the stars that brings
 * nothing; a seed that leans to the light and three doors of which one is lit; a world that bows and a ship kept afloat.
 * The prophets are only lights, he ﷺ is the brightest; Allah is never drawn: from above there is only light.
 */

private val Back45 = Color(0xE6181420)

// ═════════════ 1. the measure that does not move (bs-akhlaq) ═════════════

private object MeasureC45 {
    val Sky = Color(0xFF1C1E34)
    val SkyLow = Color(0xFF3E3654)
    val Ground = Color(0xFF3A3236)
    val Stone = Color(0xFFD8C8A8)
    val StoneDark = Color(0xFF8A7A66)
    val Book = Color(0xFF6A4A3A)
    val Page = Color(0xFFFBF7EC)
    val Heart = Color(0xFFE88A8A)
    val Red = Color(0xFFE0584A)
    val Flame = Color(0xFFF2B04A)
    val Bark = Color(0xFF6A4A32)
    val Leaf = Color(0xFF4A7A4A)
    val Wobble = Color(0xFF8A8AB0)
}

/**
 * Beats: courtfall (a court of four pillars: if morals were relative they would crack and fall), samebook (a book; a
 * heart on one side, a cross on the other; the book stays the same book), truthsame (three ages pass — pillars, domes,
 * towers — and the same flame burns in each: «الصِّدْقُ»), selfsaw (a saw cuts the branch its own seat is on), aboveref
 * (wavering threads of taste below; one straight plumb line hangs from the light above).
 */
internal fun DrawScope.truemeasureScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "courtfall" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "samebook" to Shot(Offset(w * 0.5f, h * 0.4f), 1.3f),
        "truthsame" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "selfsaw" to Shot(Offset(w * 0.45f, h * 0.4f), 1.3f),
        "aboveref" to Shot(Offset(w * 0.5f, h * 0.45f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { measureNight(a) }
}

private fun DrawScope.measureNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cf = a.b("courtfall")
    val sb = a.b("samebook")
    val ts = a.b("truthsame")
    val ss = a.b("selfsaw")
    val ar = a.b("aboveref")
    val ground = h * 0.84f
    nightSky(t, MeasureC45.Sky, MeasureC45.SkyLow, ground, starAlpha = 0.6f)
    drawRect(MeasureC45.Ground, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - seg(ts, 0f, 0.4f)
    // a court that falls without a fixed measure
    if (cf > 0f) {
        val f = easeOut(seg(cf, 0f, 0.3f)) * away * (1f - 0.6f * seg(sb, 0f, 0.4f))
        val base = Offset(w * 0.5f, ground)
        drawPath(Path().apply { moveTo(base.x - w * 0.3f, ground - h * 0.42f); lineTo(base.x, ground - h * 0.52f); lineTo(base.x + w * 0.3f, ground - h * 0.42f); close() }, MeasureC45.Stone, alpha = f * (1f - 0.6f * seg(cf, 0.6f, 0.9f)))
        for (k in 0 until 4) {
            val x = base.x - w * 0.22f + k * w * 0.147f
            val fall = easeInOut(seg(cf, 0.45f + 0.08f * k, 0.8f + 0.05f * k))
            rotate(70f * fall * (if (k % 2 == 0) -1f else 1f), Offset(x, ground)) {
                drawRect(MeasureC45.Stone, Offset(x - w * 0.025f, ground - h * 0.4f), Size(w * 0.05f, h * 0.4f), alpha = f)
                drawLine(MeasureC45.StoneDark, Offset(x - w * 0.01f, ground - h * 0.25f), Offset(x + w * 0.015f, ground - h * 0.2f), 1.5f * dp, alpha = f * seg(cf, 0.3f, 0.45f))
            }
        }
        drawRect(MeasureC45.Stone, Offset(base.x - w * 0.3f, ground - h * 0.03f), Size(w * 0.6f, h * 0.03f), alpha = f)
    }
    // the book is a book, loved or not
    if (sb > 0f) {
        val f = easeOut(seg(sb, 0f, 0.3f)) * away
        val c = Offset(w * 0.5f, h * 0.38f)
        drawRect(MeasureC45.Book, Offset(c.x - w * 0.08f, c.y - h * 0.08f), Size(w * 0.16f, h * 0.16f), alpha = f)
        drawRect(MeasureC45.Page, Offset(c.x - w * 0.07f, c.y - h * 0.07f), Size(w * 0.14f, h * 0.14f), alpha = f)
        for (r in 0 until 4) drawLine(Color(0xFF9A9080), Offset(c.x - w * 0.05f, c.y - h * 0.04f + r * h * 0.025f), Offset(c.x + w * 0.05f, c.y - h * 0.04f + r * h * 0.025f), 1.2f * dp, alpha = f)
        val l = seg(sb, 0.25f, 0.5f)
        drawPath(w8Heart(c + Offset(-w * 0.24f, 0f), w * 0.035f * l), MeasureC45.Heart, alpha = f)
        cross21(c + Offset(w * 0.24f, 0f), w * 0.035f, MeasureC45.Red, seg(sb, 0.5f, 0.75f) * f, 4f * dp)
        softGlow(c, w * 0.12f, ArtInk.Gold, 0.3f * f * seg(sb, 0.75f, 1f))
    }
    // the same flame through the ages
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, 0.3f)) * (1f - seg(ss, 0f, 0.4f))
        for (k in 0 until 3) {
            val g = easeOut(seg(ts, 0.1f + 0.2f * k, 0.35f + 0.2f * k))
            val foot = Offset(w * (0.2f + 0.3f * k), ground)
            when (k) {
                0 -> { for (j in 0 until 3) drawRect(MeasureC45.Stone, Offset(foot.x - w * 0.07f + j * w * 0.06f, foot.y - h * 0.18f), Size(w * 0.025f, h * 0.18f), alpha = f * g) }
                1 -> domed29(foot, w * 0.16f, MeasureC45.Stone, Color(0xFF3A8A5A), f * g)
                else -> { for (j in 0 until 3) drawRect(Color(0xFF8A9AB0), Offset(foot.x - w * 0.07f + j * w * 0.05f, foot.y - h * (0.2f + 0.05f * j)), Size(w * 0.04f, h * (0.2f + 0.05f * j)), alpha = f * g) }
            }
            val fl = foot + Offset(0f, -h * 0.33f)
            val fl2 = 1f + 0.08f * sin(t * 6f + k)
            drawOval(MeasureC45.Flame, Offset(fl.x - w * 0.018f, fl.y - h * 0.035f * fl2), Size(w * 0.036f, h * 0.05f * fl2), alpha = f * g)
            softGlow(fl, w * 0.05f, ArtInk.Gold, 0.4f * f * g)
            tagAt(a.tag("truthsame"), fl + Offset(0f, -h * 0.08f), (if (k == 1) seg(ts, 0.5f, 0.75f) else 0f) * (1f - seg(ss, 0f, 0.3f)), back = Back45, ink = ArtInk.Gold)
        }
        drawLine(ArtInk.BrassLight, Offset(w * 0.1f, ground - h * 0.42f), Offset(w * (0.1f + 0.8f * easeInOut(seg(ts, 0.1f, 0.9f))), ground - h * 0.42f), 1.5f * dp, alpha = f * 0.6f)
    }
    // sawing off the branch one sits on
    if (ss > 0f) {
        val f = easeOut(seg(ss, 0f, 0.3f)) * (1f - seg(ar, 0f, 0.4f))
        val trunk = Offset(w * 0.18f, ground)
        drawLine(MeasureC45.Bark, trunk, trunk + Offset(0f, -h * 0.5f), w * 0.05f, alpha = f)
        drawCircle(MeasureC45.Leaf, w * 0.1f, trunk + Offset(0f, -h * 0.55f), alpha = f)
        val cut = seg(ss, 0.55f, 0.75f)
        val drop = easeInOut(seg(ss, 0.75f, 1f))
        val root = trunk + Offset(w * 0.02f, -h * 0.36f)
        val cutAt = root + Offset(w * 0.08f, 0f)
        drawLine(MeasureC45.Bark, root, cutAt, h * 0.025f, alpha = f)
        rotate(40f * drop, cutAt) {
            drawLine(MeasureC45.Bark, cutAt, cutAt + Offset(w * 0.45f, 0f), h * 0.025f, alpha = f)
            drawRect(MeasureC45.StoneDark, Offset(cutAt.x + w * 0.25f, cutAt.y - h * 0.06f), Size(w * 0.1f, h * 0.05f), alpha = f)
            drawRect(Color(0xFF5A4A3A), Offset(cutAt.x + w * 0.25f, cutAt.y - h * 0.12f), Size(w * 0.015f, h * 0.06f), alpha = f)
        }
        val saw = cutAt + Offset(w * 0.01f * sin(t * 18f) * (1f - cut), -h * 0.06f + h * 0.04f * cut)
        drawRect(Color(0xFFB8C0C8), Offset(saw.x - w * 0.005f, saw.y - h * 0.02f), Size(w * 0.01f, h * 0.07f), alpha = f * (1f - drop))
        drawRect(MeasureC45.Bark, Offset(saw.x - w * 0.012f, saw.y - h * 0.045f), Size(w * 0.024f, h * 0.025f), alpha = f * (1f - drop))
    }
    // one plumb line from above
    if (ar > 0f) {
        val f = easeOut(seg(ar, 0f, 0.35f))
        val top = Offset(w * 0.5f, h * 0.08f)
        lightShafts(top, PI.toFloat() / 2f, 0.5f, h * 0.3f, Color(0xFFFFF0C8), 0.4f * f, t, rays = 5)
        for (k in 0 until 6) {
            val x0 = w * (0.12f + 0.15f * k)
            if (k == 2 || k == 3) continue
            val p = Path().apply {
                moveTo(x0, ground)
                for (j in 1..12) lineTo(x0 + w * 0.03f * sin(t * 2f + j * 0.9f + k), ground - h * 0.04f * j)
            }
            drawPath(p, MeasureC45.Wobble, alpha = 0.6f * f * seg(ar, 0.05f, 0.3f), style = Stroke(1.5f * dp))
        }
        val down = easeInOut(seg(ar, 0.25f, 0.7f))
        val bob = Offset(top.x, top.y + (ground - h * 0.08f - top.y) * down)
        drawLine(ArtInk.Gold, top, bob, 2f * dp, alpha = f)
        drawPath(Path().apply { moveTo(bob.x - w * 0.02f, bob.y); lineTo(bob.x + w * 0.02f, bob.y); lineTo(bob.x, bob.y + h * 0.05f); close() }, ArtInk.Brass, alpha = f)
        softGlow(bob, w * 0.05f, ArtInk.Gold, 0.4f * f * down)
    }
}

// ═════════════ 2. a brain that carries out, a set that does not make the programme (bs-mukh) ═════════════

private object BrainC45 {
    val Wall = Color(0xFF26304A)
    val WallLow = Color(0xFF3A4462)
    val Floor = Color(0xFF2A2A36)
    val Brain = Color(0xFFE8A8B0)
    val BrainDark = Color(0xFFB87880)
    val Gear = Color(0xFFD8B04A)
    val Person = Color(0xFFE8DCC8)
    val Spark = Color(0xFF8AD8FF)
    val Glass = Color(0xFFB8D8E8)
    val Drop = Color(0xFFE88AB0)
    val Paper = Color(0xFFFBF7EC)
    val Tv = Color(0xFF3A3A44)
    val Screen = Color(0xFF8AB8D8)
    val Atom = Color(0xFF8A8A9A)
    val Red = Color(0xFFE0584A)
}

/**
 * Beats: executor (a brain turns its gears under a scroll of orders that comes down to it: «تَنْفِيذِيٌّ» is ticked,
 * «تَشْرِيعِيٌّ» is struck out), penfield (a probe sparks, the man's arm rises by itself and he says «أَنَا لَمْ أَفْعَلْ
 * هَذَا»), filterpaper (drops fall from a flask onto a filter paper, and the paper feels nothing), tvset (a set takes
 * waves from a mast; its wires are cut, the screen goes dark, the waves still fill the air), spirit (a wall of atoms,
 * and beside it a man with a light inside).
 */
internal fun DrawScope.brainsetScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "executor" to Shot(Offset(w * 0.3f, h * 0.35f), 1.3f),
        "penfield" to Shot(Offset(w * 0.68f, h * 0.55f), 1.25f),
        "filterpaper" to Shot(Offset(w * 0.3f, h * 0.6f), 1.35f),
        "tvset" to Shot(Offset(w * 0.6f, h * 0.45f), 1.15f),
        "spirit" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { brainLab(a) }
}

private fun DrawScope.brainLab(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ex = a.b("executor")
    val pf = a.b("penfield")
    val fp = a.b("filterpaper")
    val tv = a.b("tvset")
    val sp = a.b("spirit")
    val floor = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(BrainC45.Wall, BrainC45.WallLow), endY = floor), size = Size(w, floor))
    drawRect(BrainC45.Floor, Offset(0f, floor), Size(w, h - floor))
    val away = 1f - seg(tv, 0f, 0.4f)
    // the brain carries out what it is given
    if (ex > 0f) {
        val f = easeOut(seg(ex, 0f, 0.3f)) * away * (1f - seg(fp, 0f, 0.35f))
        val c = Offset(w * 0.3f, h * 0.38f)
        drawOval(BrainC45.Brain, Offset(c.x - w * 0.13f, c.y - h * 0.08f), Size(w * 0.26f, h * 0.16f), alpha = f)
        drawLine(BrainC45.BrainDark, c + Offset(0f, -h * 0.08f), c + Offset(0f, h * 0.08f), 2f * dp, alpha = f)
        for (k in 0 until 2) {
            val g = c + Offset(w * 0.06f * (k * 2 - 1), 0f)
            rotate((if (k == 0) 1f else -1f) * t * 60f * seg(ex, 0.3f, 0.5f), g) {
                drawCircle(BrainC45.Gear, w * 0.03f, g, alpha = f, style = Stroke(4f * dp))
                for (j in 0 until 6) {
                    val an = j * PI.toFloat() / 3f
                    drawLine(BrainC45.Gear, g + Offset(cos(an), sin(an)) * (w * 0.03f), g + Offset(cos(an), sin(an)) * (w * 0.042f), 4f * dp, alpha = f)
                }
            }
        }
        val down = easeOut(seg(ex, 0.1f, 0.4f))
        scroll14(Offset(c.x, h * 0.08f + h * 0.08f * down), w * 0.14f, h * 0.06f, down, f)
        arrowHead(c + Offset(0f, -h * 0.1f), Offset(0f, 1f), w * 0.025f, ArtInk.BrassLight, 2f * dp, f * down)
        tagAt(a.tag("executor", 0, 0), c + Offset(-w * 0.08f, h * 0.15f), seg(ex, 0.4f, 0.65f) * away * (1f - seg(pf, 0f, 0.3f)), back = Back45, ink = ArtInk.Jade)
        tagAt(a.tag("executor", 0, 1), c + Offset(w * 0.1f, h * 0.15f), seg(ex, 0.6f, 0.85f) * away * (1f - seg(pf, 0f, 0.3f)), back = Back45, ink = Color(0xFFFFD0C8))
    }
    // the arm moves, the man disowns it
    if (pf > 0f) {
        val f = easeOut(seg(pf, 0f, 0.3f)) * away
        val foot = Offset(w * 0.7f, floor)
        val raise = easeInOut(seg(pf, 0.3f, 0.55f))
        figure(foot, h * 0.3f, BrainC45.Person, f, arms = raise)
        val head = foot + Offset(0f, -h * 0.28f)
        val probe = head + Offset(-w * 0.06f, -h * 0.04f)
        drawLine(Color(0xFFB8C0C8), probe + Offset(-w * 0.1f, -h * 0.05f), probe, 2f * dp, alpha = f)
        if (pf in 0.25f..0.45f) glint(probe, w * 0.015f, BrainC45.Spark, f)
        val bub = head + Offset(w * 0.08f, -h * 0.12f)
        tagAt(a.tag("penfield"), bub, seg(pf, 0.6f, 0.8f) * away * (1f - seg(fp, 0f, 0.3f)), back = Back45, ink = ArtInk.Ivory)
    }
    // hormones on paper feel nothing
    if (fp > 0f) {
        val f = easeOut(seg(fp, 0f, 0.3f)) * away
        val flask = Offset(w * 0.3f, h * 0.5f)
        drawPath(Path().apply { moveTo(flask.x - w * 0.015f, flask.y - h * 0.06f); lineTo(flask.x - w * 0.015f, flask.y - h * 0.02f); lineTo(flask.x - w * 0.05f, flask.y + h * 0.04f); lineTo(flask.x + w * 0.05f, flask.y + h * 0.04f); lineTo(flask.x + w * 0.015f, flask.y - h * 0.02f); lineTo(flask.x + w * 0.015f, flask.y - h * 0.06f); close() }, BrainC45.Glass, alpha = 0.7f * f)
        drawRect(BrainC45.Drop, Offset(flask.x - w * 0.04f, flask.y + h * 0.02f), Size(w * 0.08f, h * 0.02f), alpha = f)
        val paper = Offset(w * 0.3f, h * 0.75f)
        drawRect(BrainC45.Paper, Offset(paper.x - w * 0.07f, paper.y - h * 0.01f), Size(w * 0.14f, h * 0.02f), alpha = f)
        for (k in 0 until 3) {
            val u = fract(t * 0.8f + k / 3f) * seg(fp, 0.15f, 0.3f)
            drawCircle(BrainC45.Drop, w * 0.008f, Offset(flask.x, flask.y + h * 0.05f + (paper.y - flask.y - h * 0.06f) * u), alpha = f * (1f - seg(u, 0.9f, 1f)))
        }
        qmark21(paper + Offset(w * 0.12f, -h * 0.08f), h * 0.07f, ArtInk.BrassLight, f * seg(fp, 0.55f, 0.8f))
    }
    // the set shows the programme; it does not make it
    if (tv > 0f) {
        val f = easeOut(seg(tv, 0f, 0.3f)) * (1f - seg(sp, 0f, 0.4f))
        val mast = Offset(w * 0.2f, floor)
        drawLine(Color(0xFF8A8A9A), mast, mast + Offset(0f, -h * 0.45f), 3f * dp, alpha = f)
        for (k in 0 until 4) {
            val u = fract(t * 0.5f + k / 4f)
            drawArc(BrainC45.Screen, -40f, 80f, false, Offset(mast.x - w * 0.45f * u, mast.y - h * 0.45f - w * 0.45f * u), Size(w * 0.9f * u, w * 0.9f * u), alpha = f * (1f - u), style = Stroke(1.5f * dp))
        }
        val set = Offset(w * 0.66f, h * 0.55f)
        drawRoundRect(BrainC45.Tv, Offset(set.x - w * 0.14f, set.y - h * 0.1f), Size(w * 0.28f, h * 0.2f), CornerRadius(8f * dp), alpha = f)
        val dark = seg(tv, 0.6f, 0.8f)
        drawRect(lerpColor(BrainC45.Screen, Color(0xFF10141C), dark), Offset(set.x - w * 0.12f, set.y - h * 0.08f), Size(w * 0.24f, h * 0.16f), alpha = f)
        if (dark < 1f) for (r in 0 until 4) drawLine(Color.White, Offset(set.x - w * 0.1f, set.y - h * 0.05f + r * h * 0.03f), Offset(set.x + w * 0.1f * (0.3f + 0.7f * fract(t * 0.3f + r * 0.27f)), set.y - h * 0.05f + r * h * 0.03f), 1.5f * dp, alpha = f * (1f - dark) * 0.5f)
        val snap = seg(tv, 0.5f, 0.65f)
        val wireA = set + Offset(0f, h * 0.1f)
        drawLine(Color(0xFF6A6A70), wireA, wireA + Offset(-w * 0.05f * snap, h * 0.08f), 2f * dp, alpha = f)
        drawLine(Color(0xFF6A6A70), wireA + Offset(w * 0.05f * snap, h * 0.1f), Offset(w * 0.9f, floor), 2f * dp, alpha = f)
    }
    // atoms, and a man with a light inside
    if (sp > 0f) {
        val f = easeOut(seg(sp, 0f, 0.35f))
        for (r in 0 until 8) for (c in 0 until 5) drawCircle(BrainC45.Atom, w * 0.008f, Offset(w * (0.12f + 0.05f * c), floor - h * (0.04f + 0.05f * r)), alpha = f * 0.8f)
        val foot = Offset(w * 0.7f, floor)
        figure(foot, h * 0.32f, BrainC45.Person, f)
        val glow = easeOut(seg(sp, 0.35f, 0.8f))
        softGlow(foot + Offset(0f, -h * 0.2f), w * 0.08f, ArtInk.Gold, 0.6f * glow * f)
        lightShafts(foot + Offset(0f, -h * 0.75f), PI.toFloat() / 2f, 0.3f, h * 0.4f, Color(0xFFFFF0C8), 0.3f * glow * f, t, rays = 4)
    }
}

// ═════════════ 3. a dial tuned at the stars (bs-jadhb) ═════════════

private object WaveC45 {
    val Sky = Color(0xFF141830)
    val SkyLow = Color(0xFF2E2A4A)
    val Ground = Color(0xFF2E2A34)
    val Dial = Color(0xFF8A7AB0)
    val Wave = Color(0xFFB08AE8)
    val Planet = Color(0xFF8AB0D8)
    val Red = Color(0xFFE0584A)
    val Bottle = Color(0xFF6AB0A0)
    val Bead = Color(0xFF4A8AD8)
    val Steel = Color(0xFFB8C0C8)
    val Block = Color(0xFFD8B04A)
    val Battery = Color(0xFF3A3A44)
    val Bulb = Color(0xFFFFE08A)
}

/**
 * Beats: tunecosmos (a dial is turned and waves go out toward the planets; nothing comes back), noask (the lines to the
 * planets are struck out), threecures (an open book and a bottle, each ticked, and a hanging bead struck out: «سَبَبٌ
 * شَرْعِيٌّ», «سَبَبٌ قَدَرِيٌّ», «شِرْكٌ»), springmass (a block on a spring, bouncing: «الْكُتْلَةِ», «الْمُرُونَةِ»; no pull
 * toward anything), plusminus (a cell's signs change places and the lamp shines the same).
 */
internal fun DrawScope.falsewavesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "tunecosmos" to Shot(Offset(w * 0.4f, h * 0.45f), 1.1f),
        "noask" to Shot(Offset(w * 0.6f, h * 0.3f), 1.2f),
        "threecures" to Shot(Offset(w * 0.5f, h * 0.65f), 1.2f),
        "springmass" to Shot(Offset(w * 0.3f, h * 0.45f), 1.35f),
        "plusminus" to Shot(Offset(w * 0.7f, h * 0.45f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { wavesNight(a) }
}

private fun DrawScope.wavesNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tc = a.b("tunecosmos")
    val na = a.b("noask")
    val tr = a.b("threecures")
    val sm = a.b("springmass")
    val pm = a.b("plusminus")
    val ground = h * 0.86f
    nightSky(t, WaveC45.Sky, WaveC45.SkyLow, ground, starAlpha = 0.8f)
    drawRect(WaveC45.Ground, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - seg(tr, 0f, 0.4f)
    val dial = Offset(w * 0.2f, h * 0.62f)
    val planets = listOf(Offset(w * 0.55f, h * 0.18f), Offset(w * 0.75f, h * 0.3f), Offset(w * 0.88f, h * 0.14f))
    // a dial and waves sent to the planets
    if (tc > 0f) {
        val f = easeOut(seg(tc, 0f, 0.3f)) * away
        drawCircle(WaveC45.Dial, w * 0.06f, dial, alpha = f)
        val turn = 200f * easeInOut(seg(tc, 0.1f, 0.6f))
        rotate(turn, dial) { drawLine(Color.White, dial, dial + Offset(0f, -w * 0.05f), 3f * dp, StrokeCap.Round, alpha = f) }
        for ((k, p) in planets.withIndex()) {
            drawCircle(WaveC45.Planet, w * (0.025f + 0.008f * k), p, alpha = f * seg(tc, 0.2f + 0.1f * k, 0.4f + 0.1f * k))
            val dir = p - dial
            val pa = Path().apply {
                moveTo(dial.x, dial.y)
                for (j in 1..30) {
                    val u = j / 30f
                    val n = Offset(-dir.y, dir.x) / dir.getDistance()
                    val q = dial + dir * u + n * (w * 0.012f * sin(u * 30f - t * 8f))
                    lineTo(q.x, q.y)
                }
            }
            val cross = seg(na, 0.2f + 0.15f * k, 0.4f + 0.15f * k)
            drawPath(pa, WaveC45.Wave, alpha = f * seg(tc, 0.4f, 0.7f) * (1f - 0.6f * cross), style = Stroke(1.5f * dp))
            if (na > 0f) cross21(dial + dir * 0.6f, w * 0.025f, WaveC45.Red, cross * away, 3f * dp)
        }
    }
    // a lawful cause, a natural cause, and an amulet
    if (tr > 0f) {
        val f = easeOut(seg(tr, 0f, 0.3f)) * (1f - seg(sm, 0f, 0.4f))
        val y = h * 0.68f
        for (k in 0 until 3) {
            val c = Offset(w * (0.22f + 0.28f * k), y)
            val g = easeOut(seg(tr, 0.1f + 0.2f * k, 0.3f + 0.2f * k))
            when (k) {
                0 -> openBook21(c, w * 0.16f, Color(0xFFFBF7EC), Color(0xFF9A9080), f * g, glow = 0.4f * g)
                1 -> {
                    drawRoundRect(WaveC45.Bottle, Offset(c.x - w * 0.035f, c.y - h * 0.05f), Size(w * 0.07f, h * 0.1f), CornerRadius(6f * dp), alpha = f * g)
                    drawRect(Color.White, Offset(c.x - w * 0.015f, c.y - h * 0.075f), Size(w * 0.03f, h * 0.025f), alpha = f * g)
                }
                else -> {
                    drawLine(Color(0xFF8A8A96), c + Offset(0f, -h * 0.08f), c, 1.5f * dp, alpha = f * g)
                    drawCircle(WaveC45.Bead, w * 0.022f, c + Offset(0f, h * 0.01f), alpha = f * g)
                    drawCircle(Color.White, w * 0.008f, c + Offset(0f, h * 0.01f), alpha = f * g)
                }
            }
            if (k < 2) check21(c + Offset(w * 0.06f, -h * 0.08f), w * 0.018f, ArtInk.Jade, seg(tr, 0.25f + 0.2f * k, 0.4f + 0.2f * k) * f, 3f * dp)
            else cross21(c, w * 0.05f, WaveC45.Red, seg(tr, 0.7f, 0.9f) * f, 4f * dp)
            tagAt(a.tag("threecures", 0, k), c + Offset(0f, h * 0.11f), seg(tr, 0.2f + 0.2f * k, 0.4f + 0.2f * k) * (1f - seg(sm, 0f, 0.3f)), back = Back45, ink = if (k < 2) ArtInk.Jade else Color(0xFFFFD0C8))
        }
    }
    // a block on a spring: mass and stiffness, nothing more
    if (sm > 0f) {
        val f = easeOut(seg(sm, 0f, 0.3f)) * (1f - seg(pm, 0f, 0.4f))
        val top = Offset(w * 0.3f, h * 0.14f)
        drawLine(WaveC45.Steel, top + Offset(-w * 0.08f, 0f), top + Offset(w * 0.08f, 0f), 4f * dp, alpha = f)
        val y = h * (0.42f + 0.06f * sin(t * 4f) * seg(sm, 0.15f, 0.35f))
        val coil = Path().apply {
            moveTo(top.x, top.y)
            for (j in 1..14) lineTo(top.x + (if (j % 2 == 0) -1f else 1f) * w * 0.03f, top.y + (y - top.y) * j / 15f)
            lineTo(top.x, y)
        }
        drawPath(coil, WaveC45.Steel, alpha = f, style = Stroke(2f * dp))
        drawRect(WaveC45.Block, Offset(top.x - w * 0.05f, y), Size(w * 0.1f, h * 0.08f), alpha = f)
        tagAt(a.tag("springmass", 0, 0), Offset(top.x + w * 0.16f, y + h * 0.04f), seg(sm, 0.35f, 0.6f) * (1f - seg(pm, 0f, 0.3f)), back = Back45, ink = ArtInk.Gold)
        tagAt(a.tag("springmass", 0, 1), Offset(top.x + w * 0.16f, (top.y + y) / 2f), seg(sm, 0.55f, 0.8f) * (1f - seg(pm, 0f, 0.3f)), back = Back45, ink = ArtInk.Ivory)
    }
    // swap the signs: the lamp does not care
    if (pm > 0f) {
        val f = easeOut(seg(pm, 0f, 0.35f))
        val c = Offset(w * 0.7f, h * 0.5f)
        drawRoundRect(WaveC45.Battery, Offset(c.x - w * 0.1f, c.y - h * 0.04f), Size(w * 0.2f, h * 0.08f), CornerRadius(6f * dp), alpha = f)
        drawRect(WaveC45.Battery, Offset(c.x + w * 0.1f, c.y - h * 0.015f), Size(w * 0.015f, h * 0.03f), alpha = f)
        val swap = easeInOut(seg(pm, 0.35f, 0.65f))
        for (k in 0 until 2) {
            val side = if (k == 0) -1f else 1f
            val x = c.x + side * w * 0.06f * (1f - 2f * swap)
            val y = c.y - h * 0.03f * bump(swap) * side
            drawLine(Color.White, Offset(x - w * 0.015f, y), Offset(x + w * 0.015f, y), 3f * dp, alpha = f)
            if (k == 1) drawLine(Color.White, Offset(x, y - w * 0.015f), Offset(x, y + w * 0.015f), 3f * dp, alpha = f)
        }
        val bulb = c + Offset(0f, -h * 0.2f)
        drawLine(Color(0xFF8A8A96), c + Offset(-w * 0.1f, 0f), bulb + Offset(-w * 0.02f, h * 0.03f), 2f * dp, alpha = f)
        drawLine(Color(0xFF8A8A96), c + Offset(w * 0.115f, 0f), bulb + Offset(w * 0.02f, h * 0.03f), 2f * dp, alpha = f)
        drawCircle(WaveC45.Bulb, w * 0.035f, bulb, alpha = f)
        softGlow(bulb, w * 0.09f, WaveC45.Bulb, 0.5f * f)
    }
}

// ═════════════ 4. a seed to the light, many lights to one, one door lit (bs-limatha-muslim) ═════════════

private object WhyC45 {
    val Sky = Color(0xFF181C34)
    val SkyLow = Color(0xFF3A3656)
    val Ground = Color(0xFF3A3236)
    val Soil = Color(0xFF5A4232)
    val Sprout = Color(0xFF6AB06A)
    val Door = Color(0xFF6A4A32)
    val DoorDim = Color(0xFF3A3238)
    val Trunk = Color(0xFF8A6A48)
    val Water = Color(0xFF8AC8E8)
    val Block = Color(0xFFD8C8A8)
}

/**
 * Beats: innate (a seed under the soil sends a shoot straight up toward the light: «أَوَّلًا»), manyproofs (many small
 * lights come in from every side and join in one steady beam: «ثَانِيًا»), onlydoor (three doors: two go
 * dark and crumble, one opens in light: «الْإِلْحَادُ», «الشِّرْكِيَّاتُ», «الْإِسْلَامُ»), palmtrunk (a palm trunk beside
 * his light, rings of a soft sound coming from it), springwater (water rising from his light: «1500»), rebuilt (marked
 * stones fly back to their places and stand as an arch).
 */
internal fun DrawScope.whymuslimScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "innate" to Shot(Offset(w * 0.2f, h * 0.6f), 1.3f),
        "manyproofs" to Shot(Offset(w * 0.5f, h * 0.35f), 1.15f),
        "onlydoor" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "palmtrunk" to Shot(Offset(w * 0.35f, h * 0.5f), 1.25f),
        "springwater" to Shot(Offset(w * 0.45f, h * 0.45f), 1.3f),
        "rebuilt" to Shot(Offset(w * 0.7f, h * 0.6f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { whyNight(a) }
}

private fun DrawScope.whyNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val inn = a.b("innate")
    val mp = a.b("manyproofs")
    val od = a.b("onlydoor")
    val pt = a.b("palmtrunk")
    val swt = a.b("springwater")
    val rb = a.b("rebuilt")
    val ground = h * 0.84f
    nightSky(t, WhyC45.Sky, WhyC45.SkyLow, ground, starAlpha = 0.6f)
    drawRect(WhyC45.Ground, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - seg(od, 0f, 0.4f)
    // first: the seed leans to the light
    if (inn > 0f) {
        val f = easeOut(seg(inn, 0f, 0.3f)) * away
        val seed = Offset(w * 0.2f, ground + h * 0.06f)
        drawRect(WhyC45.Soil, Offset(seed.x - w * 0.12f, ground), Size(w * 0.24f, h * 0.1f), alpha = f)
        drawOval(Color(0xFFC8A070), Offset(seed.x - w * 0.015f, seed.y - h * 0.01f), Size(w * 0.03f, h * 0.02f), alpha = f)
        val grow = easeOut(seg(inn, 0.2f, 0.8f))
        val tip = seed + Offset(0f, -h * 0.32f * grow)
        drawLine(WhyC45.Sprout, seed, tip, 3f * dp, StrokeCap.Round, alpha = f)
        for (s in listOf(-1f, 1f)) drawOval(WhyC45.Sprout, Offset(tip.x + (if (s < 0) -w * 0.04f else 0f), tip.y), Size(w * 0.04f, h * 0.018f), alpha = f * seg(grow, 0.5f, 1f))
        lightShafts(Offset(seed.x, h * 0.04f), PI.toFloat() / 2f, 0.4f, h * 0.3f, Color(0xFFFFF0C8), 0.35f * f, t, rays = 4)
        tagAt(a.tag("innate"), seed + Offset(w * 0.12f, -h * 0.2f), seg(inn, 0.4f, 0.7f) * away * (1f - seg(mp, 0f, 0.3f)), back = Back45, ink = ArtInk.Gold)
    }
    // second: many lights, one certainty
    if (mp > 0f) {
        val f = easeOut(seg(mp, 0f, 0.3f)) * away
        val c = Offset(w * 0.55f, h * 0.36f)
        for (k in 0 until 14) {
            val an = k * 2f * PI.toFloat() / 14f + 0.2f
            val u = easeInOut(seg(mp, 0.1f + 0.02f * k, 0.6f + 0.02f * k))
            val from = c + Offset(cos(an), sin(an)) * (w * 0.32f)
            val p = from + (c - from) * u
            drawCircle(ArtInk.BrassLight, w * 0.008f, p, alpha = f * (1f - 0.7f * seg(u, 0.9f, 1f)))
        }
        val beam = seg(mp, 0.6f, 0.9f)
        softGlow(c, w * 0.08f * (0.5f + beam), ArtInk.Gold, 0.6f * f * beam)
        drawLine(ArtInk.Gold, c, c + Offset(0f, -h * 0.3f * beam), 4f * dp, StrokeCap.Round, alpha = f * beam)
        tagAt(a.tag("manyproofs"), c + Offset(0f, h * 0.12f), seg(mp, 0.3f, 0.55f) * away * (1f - seg(od, 0f, 0.3f)), back = Back45, ink = ArtInk.Gold)
    }
    // third: three doors, one lit
    if (od > 0f) {
        val f = easeOut(seg(od, 0f, 0.3f)) * (1f - 0.7f * seg(pt, 0f, 0.4f))
        for (k in 0 until 3) {
            val c = Offset(w * (0.22f + 0.28f * k), ground)
            val lit = k == 2
            val g = easeOut(seg(od, 0.1f + 0.15f * k, 0.3f + 0.15f * k))
            val dim = if (lit) 0f else seg(od, 0.55f, 0.8f)
            drawPath(archPath26(c.x, c.y - h * 0.3f, c.y, w * 0.08f), lerpColor(WhyC45.Door, WhyC45.DoorDim, dim), alpha = f * g * (1f - 0.5f * dim))
            if (lit) {
                val open = seg(od, 0.6f, 0.9f)
                drawPath(archPath26(c.x, c.y - h * 0.28f, c.y, w * 0.065f), Color(0xFFFFE8B0), alpha = f * open)
                softGlow(c + Offset(0f, -h * 0.15f), w * 0.15f, ArtInk.Gold, 0.5f * f * open)
            } else if (dim > 0f) {
                for (j in 0 until 3) drawRect(WhyC45.DoorDim, Offset(c.x - w * 0.06f + j * w * 0.04f, c.y - h * 0.02f - h * 0.01f * j), Size(w * 0.03f, h * 0.02f), alpha = f * dim)
            }
            tagAt(a.tag("onlydoor", 0, k), c + Offset(0f, -h * 0.36f), g * (1f - seg(pt, 0f, 0.3f)), back = Back45, ink = if (lit) ArtInk.Gold else ArtInk.Ivory)
        }
    }
    // the trunk that yearned
    val light = Offset(w * 0.48f, h * 0.38f)
    if (pt > 0f) {
        val f = easeOut(seg(pt, 0f, 0.3f)) * (1f - 0.7f * seg(rb, 0f, 0.4f))
        val trunk = Offset(w * 0.28f, ground)
        drawRect(WhyC45.Trunk, Offset(trunk.x - w * 0.025f, trunk.y - h * 0.4f), Size(w * 0.05f, h * 0.4f), alpha = f)
        for (j in 0 until 8) drawLine(Color(0xFF6A4A32), Offset(trunk.x - w * 0.025f, trunk.y - h * 0.05f * j), Offset(trunk.x + w * 0.025f, trunk.y - h * 0.05f * j - h * 0.01f), 1.5f * dp, alpha = f)
        for (k in 0 until 3) {
            val u = fract(t * 0.5f + k / 3f)
            drawArc(ArtInk.BrassLight, -30f, 60f, false, Offset(trunk.x - w * 0.15f * u, trunk.y - h * 0.3f - w * 0.15f * u), Size(w * 0.3f * u, w * 0.3f * u), alpha = f * (1f - u) * seg(pt, 0.3f, 0.5f), style = Stroke(1.5f * dp))
        }
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.1f))
    }
    // water rising from his light
    if (swt > 0f) {
        val f = easeOut(seg(swt, 0f, 0.3f)) * (1f - 0.7f * seg(rb, 0f, 0.4f))
        for (k in 0 until 10) {
            val u = fract(t * 0.6f + k / 10f)
            val an = -PI.toFloat() / 2f + (k - 4.5f) * 0.12f
            drawCircle(WhyC45.Water, w * 0.007f, light + Offset(cos(an), sin(an)) * (h * 0.18f * u) + Offset(0f, h * 0.12f * u * u), alpha = f * (1f - u))
        }
        tagAt(a.tag("springwater"), light + Offset(w * 0.16f, h * 0.06f), seg(swt, 0.4f, 0.7f) * (1f - seg(rb, 0f, 0.3f)), back = Back45, ink = ArtInk.Gold)
    }
    // marked stones fly back into an arch
    if (rb > 0f) {
        val f = easeOut(seg(rb, 0f, 0.3f))
        val c = Offset(w * 0.7f, ground)
        val r = w * 0.15f
        for (k in 0 until 9) {
            val an = PI.toFloat() * (1f - k / 8f)
            val home = c + Offset(cos(an) * r, -sin(an) * r * 1.4f)
            val from = Offset(w * (0.15f + 0.08f * ((k * 5) % 9)), h * (0.15f + 0.05f * ((k * 3) % 5)))
            val u = easeInOut(seg(rb, 0.1f + 0.06f * k, 0.45f + 0.06f * k))
            val p = from + (home - from) * u
            rotate(an * 180f / PI.toFloat() - 90f, p) {
                drawRect(WhyC45.Block, Offset(p.x - w * 0.025f, p.y - h * 0.02f), Size(w * 0.05f, h * 0.04f), alpha = f)
            }
            for (d in 0..(k % 3)) drawCircle(Color(0xFF6A5A4A), w * 0.004f, p + Offset(w * 0.01f * (d - 1), 0f), alpha = f)
        }
        softGlow(c + Offset(0f, -r), r, ArtInk.Gold, 0.3f * f * seg(rb, 0.8f, 1f))
    }
}

// ═════════════ 5. a world that bows, a ship kept afloat (bs-taslim) ═════════════

private object BowC45 {
    val Sky = Color(0xFF1A1E36)
    val SkyLow = Color(0xFF3E3A5A)
    val Ground = Color(0xFF3A3434)
    val Hill = Color(0xFF4A4A5A)
    val Stone = Color(0xFF8A8A96)
    val Sheep = Color(0xFFEDE6DA)
    val Camel = Color(0xFFC8A070)
    val Sea = Color(0xFF1E4A6A)
    val Hull = Color(0xFF7A5A3A)
    val Deck = Color(0xFFA07A50)
    val Leak = Color(0xFF8AC8E8)
    val Arch = Color(0xFFD8C8A8)
    val Red = Color(0xFFE0584A)
}

/**
 * Beats: prophetsrow (a row of the prophets' lights joined by one gold line, his light the last and brightest),
 * twoconds (two pillars stand under one arch: «الْإِخْلَاصُ», «الْمُتَابَعَةُ»), fourrakah (six small prayer arches: four
 * light up, two are struck out), stonesbow (a hill, stones, a sheep and a camel all lean toward the light), sharedship
 * (a ship of two decks; a hole is drilled below and water spurts, then it is patched and the ship rides on), khatma (a
 * book whose pages turn from first to last: «الْفَاتِحَةِ», «النَّاسِ»).
 */
internal fun DrawScope.surrenderScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "prophetsrow" to Shot(Offset(w * 0.5f, h * 0.25f), 1.15f),
        "twoconds" to Shot(Offset(w * 0.3f, h * 0.55f), 1.3f),
        "fourrakah" to Shot(Offset(w * 0.68f, h * 0.55f), 1.3f),
        "stonesbow" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
        "sharedship" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "khatma" to Shot(Offset(w * 0.5f, h * 0.4f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { bowNight(a) }
}

private fun DrawScope.bowNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pr = a.b("prophetsrow")
    val tc = a.b("twoconds")
    val fr = a.b("fourrakah")
    val sb = a.b("stonesbow")
    val sh = a.b("sharedship")
    val kh = a.b("khatma")
    val ground = h * 0.84f
    nightSky(t, BowC45.Sky, BowC45.SkyLow, ground, starAlpha = 0.6f)
    drawRect(BowC45.Ground, Offset(0f, ground), Size(w, h - ground))
    val away = 1f - seg(sb, 0f, 0.4f)
    // one religion: the prophets' lights in a row
    if (pr > 0f) {
        val f = easeOut(seg(pr, 0f, 0.3f)) * away
        val y = h * 0.24f
        val run = easeInOut(seg(pr, 0.1f, 0.8f))
        drawLine(ArtInk.Gold, Offset(w * 0.12f, y), Offset(w * (0.12f + 0.76f * run), y), 2f * dp, alpha = f * 0.7f)
        for (k in 0 until 5) {
            val c = Offset(w * (0.12f + 0.15f * k), y)
            val g = seg(pr, 0.1f + 0.12f * k, 0.3f + 0.12f * k)
            nabiLight14(c, w * 0.008f, f * g, t, null, c)
        }
        val last = Offset(w * 0.87f, y)
        sirajMunir(last, w * 0.012f, f * seg(pr, 0.7f, 0.9f), t, nameAt = last + Offset(0f, -h * 0.09f))
    }
    // two conditions under one arch
    if (tc > 0f) {
        val f = easeOut(seg(tc, 0f, 0.3f)) * away
        val c = Offset(w * 0.3f, ground)
        drawPath(archPath26(c.x, c.y - h * 0.34f, c.y - h * 0.24f, w * 0.13f), BowC45.Arch, alpha = f * seg(tc, 0.6f, 0.85f), style = Stroke(5f * dp))
        for (k in 0 until 2) {
            val x = c.x + w * 0.11f * (k * 2 - 1)
            val g = easeOut(seg(tc, 0.15f + 0.25f * k, 0.4f + 0.25f * k))
            drawRect(BowC45.Arch, Offset(x - w * 0.02f, c.y - h * 0.26f * g), Size(w * 0.04f, h * 0.26f * g), alpha = f)
            tagAt(a.tag("twoconds", 0, k), Offset(x, c.y - h * 0.42f - h * 0.05f * k), seg(tc, 0.25f + 0.25f * k, 0.45f + 0.25f * k) * away * (1f - seg(sb, 0f, 0.3f)), back = Back45, ink = ArtInk.Gold)
        }
    }
    // four rak'ahs, not six
    if (fr > 0f) {
        val f = easeOut(seg(fr, 0f, 0.3f)) * away
        for (k in 0 until 6) {
            val c = Offset(w * (0.57f + 0.06f * k), ground - h * 0.18f)
            val lit = k < 4
            val g = seg(fr, 0.1f + 0.08f * k, 0.25f + 0.08f * k)
            drawPath(archPath26(c.x, c.y - h * 0.08f, c.y, w * 0.022f), if (lit) Color(0xFFFFE8B0) else Color(0xFF6A6A76), alpha = f * g)
            if (lit) softGlow(c + Offset(0f, -h * 0.04f), w * 0.03f, ArtInk.Gold, 0.4f * f * g)
            else cross21(c + Offset(0f, -h * 0.04f), w * 0.025f, BowC45.Red, seg(fr, 0.6f + 0.1f * (k - 4), 0.75f + 0.1f * (k - 4)) * f, 3f * dp)
        }
    }
    // everything yields
    if (sb > 0f) {
        val f = easeOut(seg(sb, 0f, 0.3f)) * (1f - seg(sh, 0f, 0.4f))
        val bow = easeInOut(seg(sb, 0.3f, 0.7f))
        lightShafts(Offset(w * 0.5f, h * 0.02f), PI.toFloat() / 2f, 0.9f, h * 0.45f, Color(0xFFFFF0C8), 0.35f * f, t, rays = 7)
        drawPath(Path().apply { moveTo(w * 0.05f, ground); quadraticTo(w * 0.2f, ground - h * 0.3f, w * 0.38f, ground); close() }, BowC45.Hill, alpha = f)
        for (k in 0 until 3) {
            val c = Offset(w * (0.42f + 0.05f * k), ground - h * 0.02f)
            rotate(-12f * bow, Offset(c.x, ground)) { drawOval(BowC45.Stone, Offset(c.x - w * 0.02f, c.y - h * 0.02f), Size(w * 0.04f, h * 0.035f), alpha = f) }
        }
        rotate(-8f * bow, Offset(w * 0.66f, ground)) { sheep26(Offset(w * 0.66f, ground - h * 0.04f), h * 0.08f, BowC45.Sheep, f) }
        rotate(-6f * bow, Offset(w * 0.84f, ground)) { camel26(Offset(w * 0.84f, ground), h * 0.16f, BowC45.Camel, f, t, 0f) }
    }
    // the shared ship
    if (sh > 0f) {
        val f = easeOut(seg(sh, 0f, 0.3f)) * (1f - seg(kh, 0f, 0.4f))
        val sea = h * 0.7f
        drawRect(BowC45.Sea, Offset(0f, sea), Size(w, h - sea), alpha = f)
        val bob = h * 0.008f * sin(t * 1.5f)
        val c = Offset(w * 0.5f, sea + bob)
        drawPath(Path().apply { moveTo(c.x - w * 0.3f, c.y - h * 0.12f); lineTo(c.x + w * 0.3f, c.y - h * 0.12f); lineTo(c.x + w * 0.22f, c.y + h * 0.05f); lineTo(c.x - w * 0.22f, c.y + h * 0.05f); close() }, BowC45.Hull, alpha = f)
        drawLine(BowC45.Deck, Offset(c.x - w * 0.27f, c.y - h * 0.04f), Offset(c.x + w * 0.27f, c.y - h * 0.04f), 2f * dp, alpha = f)
        drawLine(Color(0xFF5A4A3A), c + Offset(0f, -h * 0.12f), c + Offset(0f, -h * 0.4f), 3f * dp, alpha = f)
        drawPath(Path().apply { moveTo(c.x + w * 0.01f, c.y - h * 0.38f); lineTo(c.x + w * 0.16f, c.y - h * 0.16f); lineTo(c.x + w * 0.01f, c.y - h * 0.16f); close() }, Color(0xFFEDE6DA), alpha = f)
        val hole = c + Offset(-w * 0.1f, h * 0.02f)
        val drill = seg(sh, 0.2f, 0.4f)
        val patch = seg(sh, 0.65f, 0.8f)
        if (drill > 0f && patch < 1f) {
            for (k in 0 until 5) {
                val u = fract(t * 1.2f + k / 5f)
                drawCircle(BowC45.Leak, w * 0.006f, hole + Offset(w * 0.02f * u * (k - 2), -h * 0.07f * u), alpha = f * drill * (1f - patch) * (1f - u))
            }
        }
        drawRect(ArtInk.Brass, Offset(hole.x - w * 0.02f, hole.y - h * 0.012f), Size(w * 0.04f, h * 0.024f), alpha = f * patch)
        for (k in 0 until 4) {
            val g = seg(sh, 0.8f, 1f)
            drawCircle(Color(0xFFFFE8B0), w * 0.008f, Offset(c.x - w * 0.18f + k * w * 0.12f, c.y - h * 0.08f), alpha = f * (0.4f + 0.6f * g))
        }
    }
    // a whole reading, first to last
    if (kh > 0f) {
        val f = easeOut(seg(kh, 0f, 0.35f))
        val c = Offset(w * 0.5f, h * 0.4f)
        openBook21(c, w * 0.42f, Color(0xFFFBF7EC), Color(0xFF9A9080), f, glow = 0.5f * seg(kh, 0.7f, 1f))
        val turn = fract(t * 0.7f) * seg(kh, 0.2f, 0.3f) * (1f - seg(kh, 0.85f, 0.95f))
        val sx = cos(PI.toFloat() * turn)
        drawRect(Color(0xFFF2EAD8), Offset(c.x + (if (sx < 0f) w * 0.2f * sx else 0f), c.y - h * 0.07f), Size(w * 0.2f * kotlin.math.abs(sx), h * 0.14f), alpha = f * seg(kh, 0.2f, 0.3f) * (1f - seg(kh, 0.85f, 0.95f)))
        tagAt(a.tag("khatma", 0, 0), c + Offset(w * 0.12f, h * 0.13f), seg(kh, 0.2f, 0.4f), back = Back45, ink = ArtInk.Gold)
        tagAt(a.tag("khatma", 0, 1), c + Offset(-w * 0.12f, h * 0.13f), seg(kh, 0.45f, 0.65f), back = Back45, ink = ArtInk.Gold)
    }
}
