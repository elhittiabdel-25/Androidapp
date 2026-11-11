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
 * Wave 51 (2026-11-03): «حصن المسلم»، «أذكار اليوم والليلة» (1). Waking at dawn and the stars of the ten last verses;
 * a robe that comes from beyond the wearer's power and a curtain drawn against the jinn; eight gates after the
 * ablution; three words at the door and the dome they raise; the right foot into the mosque and the lights asked for on
 * the way. Allah is never drawn; jinn are pale blue flames; Shaytan is a dark smoke.
 */

private val Back51 = Color(0xE6181420)

// ═════════════ 1. waking at dawn (hisn-istiqadh) ═════════════

private object WakeC51 {
    val Night = Color(0xFF151A2E)
    val Dawn = Color(0xFFE8A07A)
    val Wall = Color(0xFF2E2838)
    val WallLit = Color(0xFF5A4A50)
    val Mat = Color(0xFF7A5A44)
    val Person = Color(0xFFE6DCC8)
    val Gold = Color(0xFFF2C45A)
    val Stone = Color(0xFF8A8494)
    val Bead = Color(0xFFD8B46A)
}

/**
 * Beats: wakeup (a dark room; the window turns to dawn and the sleeper sits up), sleepsister (a sleeper on a mat and,
 * beside him, the low arch of a grave; dawn rises behind both), nightwake (in the night one wakes, turns the beads,
 * raises his hands, then stands to pray), lastten (the window opens on a sky of stars that turn slowly over the
 * one who looks).
 */
internal fun DrawScope.wakedawnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "wakeup" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "sleepsister" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "nightwake" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "lastten" to Shot(Offset(w * 0.6f, h * 0.4f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { wakeRoom(a) }
}

private fun DrawScope.wakeRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val rs = a.b("wakeup")
    val ss = a.b("sleepsister")
    val nw = a.b("nightwake")
    val lt = a.b("lastten")
    val floor = h * 0.82f
    drawRect(WakeC51.Night)
    val win = Offset(w * 0.68f, h * 0.18f)
    val winSize = Size(w * 0.18f, h * 0.3f)
    fun room(dawn: Float, alpha: Float) {
        drawRect(lerpColor(WakeC51.Wall, WakeC51.WallLit, dawn), Offset.Zero, Size(w, floor), alpha = alpha)
        drawRect(Color(0xFF221C26), Offset(0f, floor), Size(w, h - floor), alpha = alpha)
        drawRect(Brush.verticalGradient(listOf(lerpColor(WakeC51.Night, Color(0xFF8AA8D0), dawn), lerpColor(WakeC51.Night, WakeC51.Dawn, dawn)), win.y, win.y + winSize.height), win, winSize, alpha = alpha)
        drawRect(Color(0xFF3A2A22), win, winSize, alpha = alpha, style = Stroke(4f * dp))
        drawLine(Color(0xFF3A2A22), win + Offset(winSize.width / 2f, 0f), win + Offset(winSize.width / 2f, winSize.height), 3f * dp, alpha = alpha)
        if (dawn > 0f) drawRect(Brush.verticalGradient(listOf(WakeC51.Dawn.copy(alpha = 0.25f * dawn), Color.Transparent), win.y + winSize.height, floor), Offset(win.x - w * 0.1f, win.y + winSize.height), Size(winSize.width + w * 0.2f, floor - win.y - winSize.height), alpha = alpha)
    }
    if (rs > 0f) {
        val f = easeOut(seg(rs, 0f, 0.3f)) * (1f - seg(ss, 0f, BEAT_OUT))
        val dawn = seg(rs, 0.1f, 0.6f)
        room(dawn, f)
        drawRect(WakeC51.Mat, Offset(w * 0.2f, floor - h * 0.03f), Size(w * 0.34f, h * 0.03f), alpha = f)
        val up = easeInOut(seg(rs, 0.45f, 0.8f))
        if (up < 0.5f) {
            drawLine(WakeC51.Person, Offset(w * 0.26f, floor - h * 0.06f), Offset(w * 0.5f, floor - h * 0.05f), h * 0.035f, StrokeCap.Round, alpha = f)
            drawCircle(WakeC51.Person, h * 0.03f, Offset(w * 0.24f, floor - h * 0.075f), alpha = f)
        } else sit12(Offset(w * 0.32f, floor - h * 0.02f), h * 0.3f, WakeC51.Person, f, 1f)
        softGlow(Offset(w * 0.32f, floor - h * 0.35f), w * 0.05f, WakeC51.Gold, 0.5f * f * seg(rs, 0.8f, 1f))
    }
    if (ss > 0f) {
        val f = easeOut(seg(ss, 0f, BEAT_IN)) * (1f - seg(nw, 0f, 0.4f))
        val dawn = seg(ss, 0.4f, 0.95f)
        drawRect(Brush.verticalGradient(listOf(lerpColor(WakeC51.Night, Color(0xFF6A88B8), dawn), lerpColor(WakeC51.Night, WakeC51.Dawn, dawn)), 0f, floor), Offset.Zero, Size(w, floor), alpha = f)
        drawRect(Color(0xFF221C26), Offset(0f, floor), Size(w, h - floor), alpha = f)
        sun12(Offset(w * 0.5f, floor - h * 0.04f * dawn), w * 0.04f, f * dawn)
        drawRect(WakeC51.Mat, Offset(w * 0.12f, floor - h * 0.03f), Size(w * 0.3f, h * 0.03f), alpha = f)
        drawLine(WakeC51.Person, Offset(w * 0.17f, floor - h * 0.06f), Offset(w * 0.38f, floor - h * 0.05f), h * 0.035f, StrokeCap.Round, alpha = f)
        drawCircle(WakeC51.Person, h * 0.03f, Offset(w * 0.15f, floor - h * 0.075f), alpha = f)
        drawPath(Path().apply { moveTo(w * 0.6f, floor); quadraticTo(w * 0.74f, floor - h * 0.12f, w * 0.88f, floor); close() }, WakeC51.Stone, alpha = f)
        drawLine(WakeC51.Stone, Offset(w * 0.86f, floor), Offset(w * 0.86f, floor - h * 0.1f), 3f * dp, alpha = f)
    }
    if (nw > 0f) {
        val f = easeOut(seg(nw, 0f, 0.3f)) * (1f - seg(lt, 0f, BEAT_OUT))
        room(0f, f)
        val p = Offset(w * 0.4f, floor)
        when {
            nw < 0.35f -> { sit12(p, h * 0.3f, WakeC51.Person, f, 1f); beads12(p + Offset(w * 0.08f, -h * 0.14f), w * 0.025f, WakeC51.Bead, f, t * 3f) }
            nw < 0.65f -> { sit12(p, h * 0.3f, WakeC51.Person, f, 1f); hands12(p + Offset(w * 0.02f, -h * 0.32f), h * 0.08f, WakeC51.Person, f); for (k in 0 until 4) { val u = fract(t * 0.5f + k / 4f); drawCircle(WakeC51.Gold, 2f * dp, p + Offset(w * 0.02f, -h * (0.36f + 0.3f * u)), alpha = f * (1f - u)) } }
            else -> pray12(p, h * 0.32f, WakeC51.Person, f)
        }
    }
    if (lt > 0f) {
        val f = easeOut(seg(lt, 0f, 0.3f))
        room(0f, f)
        val open = easeInOut(seg(lt, 0.1f, 0.5f))
        val big = Size(winSize.width * (1f + 1.6f * open), winSize.height * (1f + 1.2f * open))
        val tl = Offset(win.x - (big.width - winSize.width) * 0.6f, win.y - (big.height - winSize.height) * 0.2f)
        drawRect(Color(0xFF0E1226), tl, big, alpha = f)
        val cc = tl + Offset(big.width / 2f, big.height / 2f)
        for (k in 0 until 40) {
            val r1 = fract(sin(k * 12.9898f) * 43758.547f)
            val r2 = fract(sin(k * 78.233f) * 12345.679f)
            val ang = 2f * PI.toFloat() * r1 + t * 0.05f
            val rr = big.minDimension * 0.48f * kotlin.math.sqrt(r2)
            drawCircle(Color.White, (0.8f + 1.4f * r2) * dp, cc + Offset(cos(ang) * rr * (big.width / big.height), sin(ang) * rr), alpha = f * (0.4f + 0.6f * sin(t * 2f + k).coerceAtLeast(0f)))
        }
        drawRect(Color(0xFF3A2A22), tl, big, alpha = f, style = Stroke(4f * dp))
        sit12(Offset(w * 0.32f, floor), h * 0.3f, WakeC51.Person, f, 1f)
    }
}

// ═════════════ 2. the new robe (hisn-libas) ═════════════

private object RobeC51 {
    val Back = Color(0xFFEFE4CC)
    val BackDark = Color(0xFFD8C8A6)
    val Stand = Color(0xFF8A6A44)
    val Robe = Color(0xFF3E6A8A)
    val RobeOld = Color(0xFF8A8478)
    val Turban = Color(0xFFF2EEE2)
    val Cloak = Color(0xFF8A5A3A)
    val Gold = Color(0xFFE8B84A)
    val Room = Color(0xFF2A2430)
    val Jinn = Color(0xFF9ED0FF)
    val Curtain = Color(0xFFE8DCC0)
}

/**
 * Beats: comesdown (a robe comes down onto a stand from above, where no hand of the wearer reaches; a gold tick),
 * threegarments (a turban, a shirt and a cloak appear one after another, each with a small glow), wornrenewed (an old
 * robe frays away and a new one takes its place), curtain (a curtain is drawn across a doorway; outside, pale blue
 * flames cannot see in), rightsleeve (an arrow to the right sleeve first).
 */
internal fun DrawScope.newrobeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "comesdown" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
        "threegarments" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "wornrenewed" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "curtain" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "rightsleeve" to Shot(Offset(w * 0.5f, h * 0.5f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { robeRoom(a) }
}

private fun DrawScope.robe51(c: Offset, s: Float, color: Color, alpha: Float, fray: Float = 0f) {
    if (alpha <= 0.01f) return
    drawPath(Path().apply {
        moveTo(c.x - s * 0.18f, c.y - s * 0.5f); lineTo(c.x + s * 0.18f, c.y - s * 0.5f)
        lineTo(c.x + s * 0.42f, c.y - s * 0.32f); lineTo(c.x + s * 0.34f, c.y - s * 0.2f); lineTo(c.x + s * 0.24f, c.y - s * 0.26f)
        lineTo(c.x + s * 0.28f, c.y + s * 0.5f * (1f - 0.3f * fray)); lineTo(c.x - s * 0.28f, c.y + s * 0.5f * (1f - 0.3f * fray))
        lineTo(c.x - s * 0.24f, c.y - s * 0.26f); lineTo(c.x - s * 0.34f, c.y - s * 0.2f); lineTo(c.x - s * 0.42f, c.y - s * 0.32f); close()
    }, color, alpha = alpha)
    drawLine(Color.White, Offset(c.x, c.y - s * 0.5f), Offset(c.x, c.y - s * 0.2f), 1.5f * density, alpha = alpha * 0.4f)
}

private fun DrawScope.robeRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cd = a.b("comesdown")
    val tg = a.b("threegarments")
    val wr = a.b("wornrenewed")
    val ct = a.b("curtain")
    val rs = a.b("rightsleeve")
    drawRect(Brush.verticalGradient(listOf(RobeC51.Back, RobeC51.BackDark)))
    if (cd > 0f) {
        val f = easeOut(seg(cd, 0f, BEAT_IN)) * (1f - seg(tg, 0f, 0.4f))
        val stand = Offset(w * 0.5f, h * 0.9f)
        drawLine(RobeC51.Stand, stand, stand + Offset(0f, -h * 0.66f), 4f * dp, StrokeCap.Round, alpha = f)
        drawLine(RobeC51.Stand, stand + Offset(-w * 0.08f, 0f), stand + Offset(w * 0.08f, 0f), 4f * dp, StrokeCap.Round, alpha = f)
        val down = easeInOut(seg(cd, 0.1f, 0.6f))
        val c = Offset(w * 0.5f, h * (-0.1f + 0.62f * down))
        softGlow(Offset(w * 0.5f, 0f), w * 0.3f, RobeC51.Gold, 0.3f * f * (1f - down))
        robe51(c, h * 0.56f, RobeC51.Robe, f)
        tick14(Offset(w * 0.72f, h * 0.24f), w * 0.035f, seg(cd, 0.7f, 0.9f) * f)
    }
    if (tg > 0f) {
        val f = easeOut(seg(tg, 0f, 0.3f)) * (1f - seg(wr, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val c = Offset(w * (0.22f + 0.28f * k), h * 0.52f)
            val g = easeOut(seg(tg, 0.1f + 0.25f * k, 0.3f + 0.25f * k))
            softGlow(c, w * 0.1f, RobeC51.Gold, 0.35f * f * g)
            tagAt(a.tag("threegarments", 0, k), c + Offset(0f, h * 0.28f), f * g, back = Color(0xE6201810), ink = ArtInk.Gold)
            when (k) {
                0 -> { drawOval(RobeC51.Turban, Offset(c.x - w * 0.07f, c.y - h * 0.06f), Size(w * 0.14f, h * 0.12f), alpha = f * g); for (j in 0 until 3) drawArc(RobeC51.BackDark, 200f, 140f, false, Offset(c.x - w * 0.06f, c.y - h * 0.05f + h * 0.025f * j), Size(w * 0.12f, h * 0.06f), alpha = f * g, style = Stroke(1.2f * dp)) }
                1 -> robe51(c, h * 0.36f, RobeC51.Robe, f * g)
                else -> drawPath(Path().apply { moveTo(c.x - w * 0.03f, c.y - h * 0.17f); lineTo(c.x + w * 0.03f, c.y - h * 0.17f); lineTo(c.x + w * 0.1f, c.y + h * 0.18f); lineTo(c.x - w * 0.1f, c.y + h * 0.18f); close() }, RobeC51.Cloak, alpha = f * g)
            }
        }
    }
    if (wr > 0f) {
        val f = easeOut(seg(wr, 0f, BEAT_IN)) * (1f - seg(ct, 0f, BEAT_OUT))
        val fray = seg(wr, 0.1f, 0.55f)
        robe51(Offset(w * 0.4f, h * 0.52f), h * 0.5f, RobeC51.RobeOld, f * (1f - seg(wr, 0.45f, 0.65f)), fray)
        for (k in 0 until 6) drawLine(RobeC51.RobeOld, Offset(w * (0.33f + 0.025f * k), h * (0.72f - 0.05f * fray)), Offset(w * (0.33f + 0.025f * k), h * (0.75f + 0.04f * fray)), 1.2f * dp, alpha = f * fray * (1f - seg(wr, 0.45f, 0.65f)))
        val n = easeOut(seg(wr, 0.5f, 0.85f))
        robe51(Offset(w * 0.6f, h * 0.52f), h * 0.5f * n, RobeC51.Robe, f * n)
        robe51(Offset(w * 0.4f, h * 0.52f), h * 0.5f, RobeC51.RobeOld, f * 0.35f * seg(wr, 0.45f, 0.65f), 1f)
        tagAt(a.tag("wornrenewed", 0, 0), Offset(w * 0.36f, h * 0.86f), f * seg(wr, 0.15f, 0.35f), back = Color(0xE6201810), ink = Color(0xFFC8C0B0))
        tagAt(a.tag("wornrenewed", 0, 1), Offset(w * 0.64f, h * 0.86f), f * n, back = Color(0xE6201810), ink = ArtInk.Gold)
    }
    if (ct > 0f) {
        val f = easeOut(seg(ct, 0f, BEAT_IN)) * (1f - seg(rs, 0f, 0.4f))
        drawRect(RobeC51.Room, Offset(w * 0.08f, h * 0.1f), Size(w * 0.84f, h * 0.8f), alpha = f)
        for (k in 0 until 4) {
            val c = Offset(w * (0.68f + 0.06f * k), h * 0.75f)
            jinnFlame20(c, h * 0.12f, t, k, f * 0.8f)
        }
        val close = easeInOut(seg(ct, 0.25f, 0.65f))
        val x0 = w * 0.18f
        val x1 = x0 + w * 0.42f * close
        for (k in 0 until 8) {
            val x = x0 + (x1 - x0) * k / 7f
            drawLine(RobeC51.Curtain, Offset(x, h * 0.14f), Offset(x + w * 0.005f * sin(t + k), h * 0.86f), (x1 - x0) / 7f + 2f * dp, alpha = f * 0.85f)
        }
        drawLine(RobeC51.Stand, Offset(w * 0.16f, h * 0.14f), Offset(w * 0.62f, h * 0.14f), 3f * dp, alpha = f)
    }
    if (rs > 0f) {
        val f = easeOut(seg(rs, 0f, 0.3f))
        val c = Offset(w * 0.5f, h * 0.55f)
        robe51(c, h * 0.6f, RobeC51.Robe, f)
        val sleeve = c + Offset(-h * 0.22f, -h * 0.17f)
        val tip = sleeve + Offset(-w * 0.04f, -h * 0.04f)
        penLine(listOf(tip + Offset(-w * 0.12f, -h * 0.12f), tip), seg(rs, 0.2f, 0.5f), RobeC51.Gold, 3f * dp, f)
        arrowHead(tip, Offset(1f, 1f), w * 0.02f, RobeC51.Gold, 3f * dp, f * seg(rs, 0.45f, 0.55f))
        softGlow(sleeve, w * 0.05f, RobeC51.Gold, 0.6f * f * seg(rs, 0.5f, 0.8f))
    }
}

// ═════════════ 3. eight gates (hisn-khala) ═════════════

private object GatesC51 {
    val Sky = Color(0xFFCFE2EE)
    val SkyLow = Color(0xFFF4EAD2)
    val Ground = Color(0xFFD8C8A4)
    val Door = Color(0xFF7A5A3A)
    val Frame = Color(0xFFB89A6A)
    val Smoke = Color(0xFF2A2430)
    val Green = Color(0xFF4A9A6A)
    val Water = Color(0xFF7AB8E0)
    val Jug = Color(0xFF9A6A3A)
    val Person = Color(0xFF3A3040)
    val Gold = Color(0xFFE8B84A)
    val Light = Color(0xFFFFE8B0)
}

/**
 * Beats: inout (a small door: going in, a ring of refuge keeps the dark smoke off; coming out, a word: «غُفْرَانَكَ»),
 * namedwater (a jug pours, and the first drops shine), eightdoors (eight gates in an arc open together before one who
 * has finished his ablution), twopure (two vessels side by side, one of water and one of light: «بِالْمَاءِ»,
 * «بِالتَّوْبَةِ»).
 */
internal fun DrawScope.eightgatesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "inout" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "namedwater" to Shot(Offset(w * 0.5f, h * 0.5f), 1.3f),
        "eightdoors" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "twopure" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { gatesDay(a) }
}

private fun DrawScope.gatesDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val io = a.b("inout")
    val nw = a.b("namedwater")
    val ed = a.b("eightdoors")
    val tp = a.b("twopure")
    val ground = h * 0.84f
    daySky12(GatesC51.Sky, GatesC51.SkyLow, ground, GatesC51.Ground)
    if (io > 0f) {
        val f = easeOut(seg(io, 0f, BEAT_IN)) * (1f - seg(nw, 0f, 0.4f))
        val sill = Offset(w * 0.5f, ground)
        door12(sill, w * 0.12f, h * 0.4f, 0.3f, GatesC51.Frame, GatesC51.Door, f)
        val inn = seg(io, 0.05f, 0.4f)
        val ring = seg(io, 0.1f, 0.3f) * (1f - seg(io, 0.45f, 0.55f))
        guardDome12(sill + Offset(-w * 0.12f, 0f), w * 0.12f, ring, t, f)
        smoke12(Offset(w * 0.05f, h * 0.3f), sill + Offset(-w * 0.26f, -h * 0.2f), 0.7f, t, f * ring, GatesC51.Smoke)
        figure(Offset(w * (0.2f + 0.18f * inn), ground), h * 0.26f, GatesC51.Person, f * (1f - seg(io, 0.4f, 0.45f)), walk = t * 5f)
        val out = seg(io, 0.55f, 0.9f)
        figure(Offset(w * (0.6f + 0.2f * out), ground), h * 0.26f, GatesC51.Person, f * seg(io, 0.55f, 0.6f), walk = t * 5f)
        tagAt(a.tag("inout"), Offset(w * 0.8f, ground - h * 0.38f), f * seg(io, 0.65f, 0.8f), back = Back51, ink = GatesC51.Green)
    }
    if (nw > 0f) {
        val f = easeOut(seg(nw, 0f, 0.3f)) * (1f - seg(ed, 0f, BEAT_OUT))
        val jug = Offset(w * 0.42f, h * 0.36f)
        rotate(50f * seg(nw, 0.1f, 0.3f), jug) { cup12(jug, w * 0.09f, GatesC51.Jug, f) }
        for (k in 0 until 10) {
            val u = fract(t * 1.5f + k / 10f)
            val p = Offset(w * 0.47f + w * 0.01f * sin(k * 2f), h * 0.38f + h * 0.4f * u)
            val first = k < 3
            drawCircle(GatesC51.Water, 2.4f * dp, p, alpha = f * seg(nw, 0.2f, 0.3f))
            if (first) softGlow(p, w * 0.015f, GatesC51.Gold, 0.6f * f * seg(nw, 0.25f, 0.4f))
        }
        drawOval(GatesC51.Water, Offset(w * 0.4f, h * 0.78f), Size(w * 0.14f, h * 0.04f), alpha = f * 0.6f)
    }
    if (ed > 0f) {
        val f = easeOut(seg(ed, 0f, BEAT_IN)) * (1f - seg(tp, 0f, BEAT_OUT))
        val open = easeInOut(seg(ed, 0.3f, 0.75f))
        for (k in 0 until 8) {
            val ang = PI.toFloat() * (0.1f + 0.8f * k / 7f)
            val sill = Offset(w * 0.5f - cos(ang) * w * 0.4f, ground - sin(ang) * h * 0.38f + h * 0.06f)
            door12(sill, w * 0.06f, h * 0.18f, open, GatesC51.Frame, GatesC51.Door, f, GatesC51.Light)
        }
        figure(Offset(w * 0.5f, ground), h * 0.24f, GatesC51.Person, f)
    }
    if (tp > 0f) {
        val f = easeOut(seg(tp, 0f, 0.3f))
        for (k in 0 until 2) {
            val c = Offset(w * (0.34f + 0.32f * k), h * 0.6f)
            val g = easeOut(seg(tp, 0.1f + 0.3f * k, 0.35f + 0.3f * k))
            cup12(c + Offset(0f, h * 0.12f), w * 0.16f, Color(0xFFE8DCC0), f * g, if (k == 0) GatesC51.Water else GatesC51.Light)
            if (k == 1) softGlow(c, w * 0.1f, GatesC51.Gold, 0.6f * f * g)
            tagAt(a.tag("twopure", 0, k), c + Offset(0f, h * 0.24f), f * g, back = Back51, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 4. three words at the door (hisn-manzil) ═════════════

private object DoorC51 {
    val Sky = Color(0xFFB8D4E6)
    val SkyLow = Color(0xFFF2E6CC)
    val Ground = Color(0xFFCDB68E)
    val Wall = Color(0xFFDCC6A0)
    val Trim = Color(0xFF8A6A44)
    val Person = Color(0xFF3A3040)
    val Gold = Color(0xFFE8B84A)
    val Smoke = Color(0xFF2A2430)
    val Pit = Color(0xFF6A5236)
    val Room = Color(0xFF3A2E2A)
}

/**
 * Beats: threewords (one steps out of his door and three words float up with him: «بِسْمِ اللَّهِ», «تَوَكَّلْتُ عَلَى
 * اللَّهِ», «وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ»), threegifts (three more words circle him under a dome of light —
 * «هُدِيتَ», «وَكُفِيتَ», «وَوُقِيتَ» — and a dark smoke slides away), homegreet (he comes in and greets his family;
 * the room glows and the smoke finds no place), pitfalls (the road ahead has four pits; he walks by every one).
 */
internal fun DrawScope.threewordsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threewords" to Shot(Offset(w * 0.45f, h * 0.5f), 1.1f),
        "threegifts" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
        "homegreet" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "pitfalls" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { doorDay(a) }
}

private fun DrawScope.doorDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tw = a.b("threewords")
    val tg = a.b("threegifts")
    val hg = a.b("homegreet")
    val pf = a.b("pitfalls")
    val ground = h * 0.84f
    daySky12(DoorC51.Sky, DoorC51.SkyLow, ground, DoorC51.Ground)
    if (tw > 0f) {
        val f = easeOut(seg(tw, 0f, BEAT_IN)) * (1f - seg(tg, 0f, 0.4f))
        house12(Offset(w * 0.22f, ground), w * 0.3f, DoorC51.Wall, DoorC51.Trim, f, 0.3f)
        val out = easeInOut(seg(tw, 0.05f, 0.35f))
        val p = Offset(w * (0.2f + 0.24f * out), ground)
        figure(p, h * 0.3f, DoorC51.Person, f, walk = t * 5f * (1f - seg(tw, 0.3f, 0.35f)))
        for (k in 0 until 3) tagAt(a.tag("threewords", 0, k), Offset(w * (0.5f + 0.12f * k), h * (0.22f + 0.12f * k)), f * seg(tw, 0.2f + 0.22f * k, 0.35f + 0.22f * k), back = Back51, ink = ArtInk.Gold)
    }
    if (tg > 0f) {
        val f = easeOut(seg(tg, 0f, 0.3f)) * (1f - seg(hg, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, ground)
        figure(c, h * 0.3f, DoorC51.Person, f)
        guardDome12(c, h * 0.42f, seg(tg, 0.1f, 0.5f), t, f)
        for (k in 0 until 3) {
            val ang = PI.toFloat() * (1.2f + 0.3f * k) + t * 0.2f
            tagAt(a.tag("threegifts", 0, k), c + Offset(cos(ang) * w * 0.3f, -h * 0.22f + sin(ang) * h * 0.18f), f * seg(tg, 0.2f + 0.15f * k, 0.35f + 0.15f * k), back = Back51, ink = ArtInk.Gold)
        }
        val away = easeInOut(seg(tg, 0.55f, 0.95f))
        smoke12(Offset(w * (0.95f + 0.2f * away), h * 0.3f), Offset(w * (0.72f + 0.3f * away), ground - h * 0.2f), 1f, t, f * (1f - away), DoorC51.Smoke)
    }
    if (hg > 0f) {
        val f = easeOut(seg(hg, 0f, BEAT_IN)) * (1f - seg(pf, 0f, BEAT_OUT))
        val glow = seg(hg, 0.35f, 0.7f)
        drawRect(lerpColor(DoorC51.Room, Color(0xFF6A5040), glow), Offset(w * 0.06f, h * 0.18f), Size(w * 0.88f, ground - h * 0.18f), alpha = f)
        door12(Offset(w * 0.86f, ground), w * 0.1f, h * 0.4f, 0.8f, DoorC51.Trim, Color(0xFF5A3A24), f)
        val inn = easeInOut(seg(hg, 0.05f, 0.35f))
        figure(Offset(w * (0.86f - 0.2f * inn), ground), h * 0.3f, DoorC51.Gold, f, walk = t * 5f * (1f - inn), arms = 0.5f * seg(hg, 0.35f, 0.5f))
        sit12(Offset(w * 0.3f, ground), h * 0.26f, Color(0xFFE6DCC8), f, 1f)
        womanSit12(Offset(w * 0.44f, ground), h * 0.24f, Color(0xFFE6DCC8), f, 1f)
        child12(Offset(w * 0.54f, ground), h * 0.3f, Color(0xFFE6DCC8), f)
        softGlow(Offset(w * 0.45f, ground - h * 0.25f), w * 0.2f, DoorC51.Gold, 0.35f * f * glow)
    }
    if (pf > 0f) {
        val f = easeOut(seg(pf, 0f, 0.3f))
        drawLine(Color(0xFFBFA77E), Offset(0f, ground + h * 0.06f), Offset(w, ground + h * 0.06f), h * 0.06f, alpha = f)
        for (k in 0 until 4) drawOval(DoorC51.Pit, Offset(w * (0.2f + 0.18f * k) - w * 0.035f, ground + h * (0.03f + 0.06f * (k % 2))), Size(w * 0.07f, h * 0.025f), alpha = f)
        val go = easeInOut(seg(pf, 0.1f, 0.95f))
        val x = w * (0.08f + 0.84f * go)
        val dodge = 0.06f * sin(go * 4f * PI.toFloat() * 2f)
        figure(Offset(x, ground + h * 0.06f * (0.5f + dodge * 8f).coerceIn(0f, 1f)), h * 0.26f, DoorC51.Person, f, walk = t * 6f)
        guardDome12(Offset(x, ground + h * 0.06f), h * 0.32f, 1f, t, f * 0.5f)
    }
}

// ═════════════ 5. the right foot into the mosque (hisn-masjid) ═════════════

private object MosqueC51 {
    val Sky = Color(0xFFBCD8EA)
    val SkyLow = Color(0xFFF4E8CE)
    val Ground = Color(0xFFD2BC94)
    val Body = Color(0xFFEDE2C8)
    val Dome = Color(0xFF3E7A6A)
    val Foot = Color(0xFF3A3040)
    val Mercy = Color(0xFFFFE8A8)
    val Coin = Color(0xFFE8C25A)
    val Person = Color(0xFF3A3040)
    val Light = Color(0xFFFFE6A0)
    val Smoke = Color(0xFF2A2430)
    val Gold = Color(0xFFE8B84A)
}

/**
 * Beats: rightin (a threshold: the right foot crosses first and the doorway fills with light: «الْيُمْنَى»), leftout (the
 * left foot steps out to a busy street where coins of provision shine: «الْيُسْرَى»), fourlights (on the way, four small
 * lights settle on one who walks: at the heart, the mouth, the ears and the eyes), keptsafe (the dark smoke comes close
 * and turns back from the mosque's door), manylights (a grid of small lights: «25»).
 */
internal fun DrawScope.rightfootScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "rightin" to Shot(Offset(w * 0.5f, h * 0.6f), 1.3f),
        "leftout" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "fourlights" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "keptsafe" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "manylights" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { mosqueDay(a) }
}

private fun DrawScope.foot51(heel: Offset, s: Float, color: Color, alpha: Float, right: Boolean) {
    if (alpha <= 0.01f) return
    drawOval(color, Offset(heel.x - s * 0.18f, heel.y - s), Size(s * 0.36f, s), alpha = alpha)
    for (k in 0 until 5) {
        val x = heel.x + (if (right) 1f else -1f) * s * (0.14f - 0.07f * k)
        drawCircle(color, s * (0.07f - 0.008f * k), Offset(x, heel.y - s * 1.06f + s * 0.03f * k), alpha = alpha)
    }
}

private fun DrawScope.mosqueDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ri = a.b("rightin")
    val lo = a.b("leftout")
    val fl = a.b("fourlights")
    val ks = a.b("keptsafe")
    val ml = a.b("manylights")
    val ground = h * 0.84f
    daySky12(MosqueC51.Sky, MosqueC51.SkyLow, ground, MosqueC51.Ground)
    if (ri > 0f) {
        val f = easeOut(seg(ri, 0f, BEAT_IN)) * (1f - seg(lo, 0f, BEAT_OUT))
        drawRect(MosqueC51.Body, Offset(0f, h * 0.1f), Size(w, h * 0.5f), alpha = f)
        val glow = seg(ri, 0.35f, 0.8f)
        drawPath(archPath26(w * 0.5f, h * 0.14f, h * 0.6f, w * 0.16f), lerpColor(Color(0xFF3A2E26), MosqueC51.Mercy, glow), alpha = f)
        drawRect(Color(0xFF9A8460), Offset(0f, h * 0.6f), Size(w, h * 0.03f), alpha = f)
        val step = easeInOut(seg(ri, 0.1f, 0.4f))
        foot51(Offset(w * 0.56f, h * (0.92f - 0.25f * step)), h * 0.18f, MosqueC51.Foot, f, true)
        foot51(Offset(w * 0.44f, h * 0.95f), h * 0.18f, MosqueC51.Foot, f, false)
        tagAt(a.tag("rightin"), Offset(w * 0.7f, h * 0.7f), f * seg(ri, 0.2f, 0.35f), back = Back51, ink = ArtInk.Gold)
    }
    if (lo > 0f) {
        val f = easeOut(seg(lo, 0f, BEAT_IN)) * (1f - seg(fl, 0f, BEAT_OUT))
        for (k in 0 until 4) house12(Offset(w * (0.12f + 0.25f * k), h * 0.6f), w * 0.2f, Color(0xFFDCC6A0), Color(0xFF8A6A44), f, 0.2f)
        drawRect(Color(0xFF9A8460), Offset(0f, h * 0.6f), Size(w, h * 0.03f), alpha = f)
        for (k in 0 until 6) {
            val c = Offset(w * (0.1f + 0.16f * k), h * (0.5f + 0.04f * sin(t + k)))
            coin12(c, w * 0.015f, f * seg(lo, 0.4f + 0.06f * k, 0.55f + 0.06f * k), MosqueC51.Coin)
        }
        val step = easeInOut(seg(lo, 0.1f, 0.4f))
        foot51(Offset(w * 0.44f, h * (0.95f - 0.25f * step)), h * 0.18f, MosqueC51.Foot, f, false)
        foot51(Offset(w * 0.56f, h * 0.92f), h * 0.18f, MosqueC51.Foot, f, true)
        tagAt(a.tag("leftout"), Offset(w * 0.28f, h * 0.7f), f * seg(lo, 0.2f, 0.35f), back = Back51, ink = ArtInk.Gold)
    }
    if (fl > 0f) {
        val f = easeOut(seg(fl, 0f, BEAT_IN)) * (1f - seg(ks, 0f, BEAT_OUT))
        mosque12(Offset(w * 0.8f, ground), w * 0.3f, MosqueC51.Body, MosqueC51.Dome, f)
        val go = easeInOut(seg(fl, 0.05f, 0.9f))
        val p = Offset(w * (0.12f + 0.42f * go), ground)
        figure(p, h * 0.36f, MosqueC51.Person, f, walk = t * 5f)
        val spots = listOf(Offset(0f, -0.22f), Offset(0.0f, -0.4f), Offset(0.03f, -0.43f), Offset(0.0f, -0.45f))
        for ((k, s) in spots.withIndex()) {
            val g = seg(fl, 0.15f + 0.18f * k, 0.3f + 0.18f * k)
            softGlow(p + Offset(w * s.x * (if (k == 2) 1f else 0f), h * s.y), w * 0.02f, MosqueC51.Light, 0.9f * f * g)
        }
    }
    if (ks > 0f) {
        val f = easeOut(seg(ks, 0f, BEAT_IN)) * (1f - seg(ml, 0f, BEAT_OUT))
        mosque12(Offset(w * 0.42f, ground), w * 0.42f, MosqueC51.Body, MosqueC51.Dome, f, 0.7f)
        val come = seg(ks, 0.1f, 0.45f)
        val back = easeInOut(seg(ks, 0.5f, 0.9f))
        val tip = Offset(w * (0.95f - 0.3f * come + 0.35f * back), ground - h * 0.15f)
        smoke12(Offset(w * 1.05f, h * 0.3f), tip, 0.8f, t, f * (1f - 0.7f * back), MosqueC51.Smoke)
        guardDome12(Offset(w * 0.42f, ground), w * 0.3f, seg(ks, 0.4f, 0.6f), t, f)
    }
    if (ml > 0f) {
        val f = easeOut(seg(ml, 0f, 0.3f))
        drawRect(Color(0xFF1E2238), alpha = f)
        for (k in 0 until 25) {
            val c = Offset(w * (0.26f + 0.12f * (k % 5)), h * (0.18f + 0.13f * (k / 5)))
            softGlow(c, w * 0.022f, MosqueC51.Light, 0.9f * f * seg(ml, 0.05f + 0.025f * k, 0.15f + 0.025f * k))
        }
        tagAt(a.tag("manylights"), Offset(w * 0.12f, h * 0.5f), f * seg(ml, 0.6f, 0.8f), back = Back51, ink = ArtInk.Gold)
    }
}
