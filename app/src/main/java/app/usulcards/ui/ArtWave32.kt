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
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 32 (2026-10-27): «بصائر»، الجزء الثاني، «كيف ظهر الدين؟». A man crouching under a storm and a temple of pride;
 * one clear spring and the muddy streams below it; six blind men round an elephant; a dark plate that glows only in
 * the light it is given; the hill of Safa; a path into the mist beside a wall; and a pot that feeds a thousand.
 */

private val Back32 = Color(0xE6181420)

// ═════════════ 1. fear and the storm (bs-khawf) ═════════════

private object FearC32 {
    val Night = Color(0xFF1A1E34)
    val NightLow = Color(0xFF3A3450)
    val Day = Color(0xFFBFD6EA)
    val DayLow = Color(0xFFF2E2C2)
    val Land = Color(0xFF4A3E36)
    val LandDay = Color(0xFFC9A970)
    val Cloud = Color(0xFF2A2A3E)
    val Bolt = Color(0xFFF4F0FF)
    val Ink = Color(0xFF0E0C14)
    val Stone = Color(0xFFD8B880)
    val StoneDark = Color(0xFFA8844C)
    val Sun = Color(0xFFFFD27A)
}

/**
 * Beats: fearstorm (night, a storm: lightning over a man crouched by a rock, «الْخَوْفُ؟»), fourq (four numbered
 * questions hang over the claim), ownsource (a clean shaft of light from above, apart from storm and temple:
 * «مَنْشَأٌ مُسْتَقِلٌّ»), then in the deeper layer: sameday (the sun crosses the sky again and again: what repeats grows familiar, and
 * the man stands and walks), grandeur (a stepped temple of pride rises, crowned with a sun disc: «الْعَظَمَةُ»،
 * «الْقُوَّةُ»، «الِاسْتِعْلَاءُ»).
 */
internal fun DrawScope.dawnfearScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fearstorm" to Shot(Offset(w * 0.3f, h * 0.62f), 1.35f),
        "fourq" to Shot(Offset(w * 0.5f, h * 0.4f), 1.05f),
        "sameday" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "grandeur" to Shot(Offset(w * 0.72f, h * 0.6f), 1.3f),
        "ownsource" to Shot(Offset(w * 0.4f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { dawnfearPlain(a) }
}

private fun DrawScope.dawnfearPlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fs = a.b("fearstorm")
    val fq = a.b("fourq")
    val sd = a.b("sameday")
    val gr = a.b("grandeur")
    val os = a.b("ownsource")
    val horizon = h * 0.74f
    val day = easeInOut(seg(sd, 0.1f, 0.6f))
    drawRect(Brush.verticalGradient(listOf(lerpColor(FearC32.Night, FearC32.Day, day), lerpColor(FearC32.NightLow, FearC32.DayLow, day)), endY = horizon), size = Size(w, horizon))
    // the sun crossing the sky again and again
    if (sd > 0f) {
        val laps = 3f * seg(sd, 0.05f, 0.9f)
        val u = fract(laps)
        val sunAt = Offset(mix(w * 0.05f, w * 0.95f, u), horizon - sin(PI.toFloat() * u) * h * 0.55f)
        val sa = if (sd >= 0.9f) 0f else easeOut(seg(sd, 0f, 0.1f))
        drawCircle(FearC32.Sun, w * 0.03f, sunAt, alpha = sa)
        softGlow(sunAt, w * 0.07f, FearC32.Sun, 0.5f * sa)
        // the arc it keeps tracing
        val arc = Path()
        for (k in 0..30) {
            val v = k / 30f
            val p = Offset(mix(w * 0.05f, w * 0.95f, v), horizon - sin(PI.toFloat() * v) * h * 0.55f)
            if (k == 0) arc.moveTo(p.x, p.y) else arc.lineTo(p.x, p.y)
        }
        drawPath(arc, FearC32.Sun, alpha = 0.35f * easeOut(seg(sd, 0.3f, 0.6f)), style = Stroke(1.2f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))))
    }
    // storm clouds and lightning, fading as the days come
    val storm = easeOut(seg(fs, 0f, 0.3f)) * (1f - day)
    if (storm > 0f) {
        for (k in 0 until 7) {
            val c = Offset(w * (0.05f + 0.15f * k) + w * 0.01f * sin(t * 0.4f + k), h * (0.12f + 0.04f * (k % 2)))
            drawCircle(FearC32.Cloud, w * (0.09f + 0.02f * (k % 3)), c, alpha = 0.9f * storm)
        }
        val flash = fract(t * 0.45f)
        if (flash < 0.12f) {
            val bolt = Path().apply {
                moveTo(w * 0.42f, h * 0.18f); lineTo(w * 0.38f, h * 0.36f); lineTo(w * 0.43f, h * 0.36f); lineTo(w * 0.36f, h * 0.58f)
            }
            drawPath(bolt, FearC32.Bolt, alpha = storm * (1f - flash / 0.12f), style = Stroke(3f * dp, cap = StrokeCap.Round))
            drawRect(Color.White, size = Size(w, h), alpha = 0.12f * storm * (1f - flash / 0.12f))
        }
        for (k in 0 until 24) {
            val f = fract(t * 1.6f + k * 0.13f)
            val x = w * fract(k * 0.618f)
            drawLine(Color(0xFFB0B8D8), Offset(x, h * 0.2f + f * h * 0.55f), Offset(x - w * 0.01f, h * 0.24f + f * h * 0.55f), 1f * dp, alpha = 0.5f * storm)
        }
    }
    drawRect(Brush.verticalGradient(listOf(lerpColor(FearC32.Land, FearC32.LandDay, day), lerpColor(FearC32.Land, FearC32.StoneDark, day)), startY = horizon, endY = h), Offset(0f, horizon), Size(w, h - horizon))
    // the rock he hides by
    drawPath(Path().apply { moveTo(w * 0.12f, horizon + h * 0.02f); quadraticTo(w * 0.15f, horizon - h * 0.12f, w * 0.24f, horizon - h * 0.1f); quadraticTo(w * 0.28f, horizon - h * 0.02f, w * 0.27f, horizon + h * 0.02f); close() },
        lerpColor(Color(0xFF2E2A30), Color(0xFF8A7458), day))
    // the temple of pride
    if (gr > 0f) {
        val rise = easeOut(seg(gr, 0f, 0.6f))
        val base = Offset(w * 0.74f, horizon + h * 0.02f)
        for (k in 0 until 4) {
            val wd = w * (0.26f - 0.055f * k)
            val ht = h * 0.07f
            val y = base.y - ht * (k + 1) * rise
            drawRect(lerpColor(FearC32.Stone, FearC32.StoneDark, k * 0.15f), Offset(base.x - wd / 2f, y), Size(wd, ht * rise + 1f), alpha = easeOut(seg(gr, k * 0.1f, 0.3f + k * 0.1f)))
        }
        val top = Offset(base.x, base.y - h * 0.28f * rise - h * 0.05f)
        val crown = easeOut(seg(gr, 0.5f, 0.8f))
        drawCircle(FearC32.Sun, w * 0.03f, top, alpha = crown)
        lightShafts(top, -PI.toFloat() / 2f, 1.4f, h * 0.2f, FearC32.Sun, 0.5f * crown, t, rays = 7)
        for (k in 0 until 3) {
            val f = seg(gr, 0.45f + k * 0.12f, 0.65f + k * 0.12f)
            tagAt(a.tag("grandeur", 0, k), Offset(w * (0.6f + 0.13f * k), h * (0.32f + 0.08f * (k % 2))), f, back = Back32, ink = ArtInk.BrassLight)
        }
    }
    // the man: crouched in fear, then standing as the days grow familiar
    val manA = easeOut(seg(fs, 0f, 0.25f))
    val stand = easeInOut(seg(sd, 0.5f, 0.9f))
    val walkX = w * 0.06f * easeInOut(seg(sd, 0.6f, 1f))
    figure(Offset(w * 0.32f + walkX, horizon + h * 0.04f), h * mix(0.13f, 0.2f, stand), lerpColor(FearC32.Ink, Color(0xFF3A2E28), day), manA,
        walk = if (sd in 0.6f..0.99f) t * 4f else 0f, lean = mix(0.55f, 0f, stand), arms = mix(1f, 0.2f, stand))
    tagAt(a.tag("fearstorm"), Offset(w * 0.32f, h * 0.44f), seg(fs, 0.2f, 0.45f) * (1f - seg(fq, 0.6f, 1f)), back = Back32, ink = Color(0xFFFFC0A0))
    // the four questions over the claim
    if (fq > 0f) {
        for (k in 0 until 4) {
            val c = Offset(w * (0.2f + 0.2f * k), h * 0.27f)
            val f = easeOut(seg(fq, k * 0.15f, 0.3f + k * 0.15f)) * (1f - seg(os, 0f, 0.25f))
            if (f <= 0f) continue
            drawCircle(Color(0xFF2A2440), w * 0.03f * f, c, alpha = 0.9f)
            drawCircle(ArtInk.BrassLight, w * 0.03f * f, c, alpha = 0.8f, style = Stroke(1.4f * dp))
            qmark21(c + Offset(w * 0.045f, -h * 0.01f), h * 0.05f * f, ArtInk.BrassLight, f)
            tagAt(a.tag("fourq", 0, k), c, f, back = Color(0x00000000), ink = ArtInk.BrassLight)
        }
    }
    // a light of its own, from above
    if (os > 0f) {
        val f = easeOut(seg(os, 0.1f, 0.6f)) * (1f - 0.7f * seg(sd, 0f, 0.3f))
        val from = Offset(w * 0.36f, -h * 0.05f)
        lightShafts(from, PI.toFloat() / 2f, 0.32f, h * 0.85f, Color(0xFFFFF2D0), 0.7f * f, t, rays = 6)
        softGlow(Offset(w * 0.38f, horizon), w * 0.08f, Color(0xFFFFE8B0), 0.5f * f)
        tagAt(a.tag("ownsource"), Offset(w * 0.36f, h * 0.2f), seg(os, 0.35f, 0.6f) * (1f - seg(sd, 0f, 0.2f)), back = Back32, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. one spring, many streams (bs-tawhid-awwal) ═════════════

private object SpringC32 {
    val Sky = Color(0xFFCADDEB)
    val SkyLow = Color(0xFFEDE6D4)
    val Hill = Color(0xFF8C9A6E)
    val HillDark = Color(0xFF66744E)
    val Clear = Color(0xFF6EC3E8)
    val Mud = Color(0xFF9A7A52)
    val Rock = Color(0xFFA09080)
    val Book = listOf(Color(0xFF6A3A2A), Color(0xFF2E4A6A), Color(0xFF3E5A34))
}

/** The stream from the spring at [top] down to the plain; [k] picks one of five branches lower down. */
private fun branch32(w: Float, h: Float, k: Int): List<Offset> {
    val spread = (k - 2) * 0.17f
    return listOf(Offset(w * 0.5f, h * 0.27f), Offset(w * 0.5f, h * 0.45f), Offset(w * (0.5f + spread * 0.35f), h * 0.62f),
        Offset(w * (0.5f + spread * 0.8f), h * 0.78f), Offset(w * (0.5f + spread * 1.2f), h * 0.98f))
}

/**
 * Beats: spring (a clear spring on the summit, «التَّوْحِيدُ», and a bright stream), muddy (downhill it splits into
 * five branches that turn muddy as they go, «التَّعَدُّدُ»), witness (three books on the bank, each a scholar's name
 * — «شْمِيت»، «أَنْدْرُو لَانْج»، «شِيلِنْج» — trace the streams back to the one spring), fromabove (light comes down
 * to the spring from above: religion came to man).
 */
internal fun DrawScope.springScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "spring" to Shot(Offset(w * 0.5f, h * 0.3f), 1.45f),
        "muddy" to Shot(Offset(w * 0.5f, h * 0.7f), 1.15f),
        "witness" to Shot(Offset(w * 0.25f, h * 0.55f), 1.25f),
        "fromabove" to Shot(Offset(w * 0.5f, h * 0.4f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { springValley(a) }
}

private fun DrawScope.springValley(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sp = a.b("spring")
    val md = a.b("muddy")
    val wi = a.b("witness")
    val fa = a.b("fromabove")
    drawRect(Brush.verticalGradient(listOf(SpringC32.Sky, SpringC32.SkyLow)), size = Size(w, h))
    // the mountain
    drawPath(Path().apply { moveTo(0f, h); lineTo(0f, h * 0.7f); quadraticTo(w * 0.3f, h * 0.45f, w * 0.5f, h * 0.22f); quadraticTo(w * 0.7f, h * 0.45f, w, h * 0.68f); lineTo(w, h); close() },
        Brush.verticalGradient(listOf(SpringC32.Hill, SpringC32.HillDark), startY = h * 0.22f, endY = h))
    // light from above
    if (fa > 0f) {
        val f = easeOut(seg(fa, 0f, 0.5f))
        lightShafts(Offset(w * 0.5f, -h * 0.1f), PI.toFloat() / 2f, 0.4f, h * 0.45f, Color(0xFFFFF4D0), 0.8f * f, t, rays = 7)
        softGlow(Offset(w * 0.5f, h * 0.26f), w * 0.1f, Color(0xFFFFF0C0), 0.6f * f)
    }
    // the spring and its streams
    val clearRun = easeOut(seg(sp, 0.2f, 0.8f))
    val split = easeInOut(seg(md, 0f, 0.7f))
    for (k in 0 until 5) {
        val pts = w8Smooth(branch32(w, h, k), 8)
        val run = if (k == 2) clearRun * (0.45f + 0.55f * split) else clearRun * 0.45f * (if (split > 0f) 1f else 0f) + 0.55f * split
        if (run <= 0f) continue
        val head = w8Head(pts, run.coerceIn(0f, 1f))
        if (head.size < 2) continue
        // each branch muddies further from the spring
        for (i in 1 until head.size) {
            val u = i / pts.size.toFloat()
            val mud = if (k == 2 && split <= 0f) 0f else seg(u, 0.35f, 0.9f) * split
            drawLine(lerpColor(SpringC32.Clear, SpringC32.Mud, mud), head[i - 1], head[i], (w * 0.012f) * (1f + 0.6f * u) * (if (k == 2) 1f else 0.8f), StrokeCap.Round)
        }
    }
    if (sp > 0f) {
        val f = easeOut(seg(sp, 0f, 0.3f))
        drawOval(SpringC32.Clear, Offset(w * 0.47f, h * 0.25f), Size(w * 0.06f, h * 0.04f), alpha = f)
        for (k in 0 until 3) {
            val r = fract(t * 0.6f + k / 3f)
            drawOval(Color.White, Offset(w * 0.5f - w * 0.03f * r, h * 0.27f - h * 0.02f * r), Size(w * 0.06f * r, h * 0.04f * r), alpha = 0.5f * (1f - r) * f, style = Stroke(1f * dp))
        }
        glint(Offset(w * 0.5f, h * 0.26f), w * 0.012f, Color.White, f)
        tagAt(a.tag("spring"), Offset(w * 0.5f, h * 0.16f), seg(sp, 0.3f, 0.55f), back = Back32, ink = Color(0xFFB8E8FF))
    }
    if (md > 0f) tagAt(a.tag("muddy"), Offset(w * 0.5f, h * 0.88f), seg(md, 0.5f, 0.8f), back = Back32, ink = Color(0xFFE8C8A0))
    // three scholars as books on the bank, tracing the streams back up
    if (wi > 0f) {
        for (k in 0 until 3) {
            val c = Offset(w * (0.12f + 0.11f * k), h * (0.5f + 0.1f * k))
            val f = easeOut(seg(wi, k * 0.15f, 0.3f + k * 0.15f))
            spine21(c, w * 0.035f, h * 0.12f, SpringC32.Book[k], 3, f)
            val trace = seg(wi, 0.35f + k * 0.12f, 0.75f + k * 0.08f)
            if (trace > 0f) {
                val to = Offset(w * 0.49f, h * 0.27f)
                drawLine(ArtInk.BrassLight, c + Offset(w * 0.02f, -h * 0.05f), c + Offset(w * 0.02f, -h * 0.05f) + (to - c - Offset(w * 0.02f, -h * 0.05f)) * trace, 1.2f * dp, alpha = 0.7f * f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp)))
            }
            tagAt(a.tag("witness", 0, k), c + Offset(0f, h * 0.1f), seg(wi, 0.1f + k * 0.15f, 0.3f + k * 0.15f), back = Back32, ink = ArtInk.Ivory)
        }
    }
}

// ═════════════ 3. the blind men and the elephant (bs-fil) ═════════════

private object FilC32 {
    val Wall = Color(0xFF2A2638)
    val WallLow = Color(0xFF4A3E52)
    val Floor = Color(0xFF6A5A48)
    val Ele = Color(0xFF8A8A96)
    val EleDark = Color(0xFF6A6A76)
    val Tusk = Color(0xFFF2EAD6)
    val Man = Color(0xFF1A1620)
    val Band = Color(0xFFE8E0D0)
}

private fun DrawScope.elephant32(c: Offset, s: Float, alpha: Float, glow: Float, t: Float) {
    if (alpha <= 0.01f) return
    val col = lerpColor(FilC32.Ele, Color(0xFFB8B4C0), glow)
    // legs
    for (k in 0 until 4) {
        val x = c.x + s * (-0.55f + 0.33f * k + (if (k > 1) 0.08f else 0f))
        drawRect(lerpColor(FilC32.EleDark, col, 0.3f), Offset(x, c.y + s * 0.15f), Size(s * 0.2f, s * 0.55f), alpha = alpha)
    }
    drawOval(col, Offset(c.x - s * 0.7f, c.y - s * 0.4f), Size(s * 1.4f, s * 0.75f), alpha = alpha)
    // head, ear, trunk, tusk
    val head = c + Offset(-s * 0.72f, -s * 0.1f)
    drawCircle(col, s * 0.3f, head, alpha = alpha)
    drawOval(FilC32.EleDark, Offset(head.x - s * 0.05f, head.y - s * 0.3f), Size(s * 0.32f, s * 0.42f), alpha = alpha)
    val sway = sin(t * 0.8f) * s * 0.04f
    val trunk = Path().apply {
        moveTo(head.x - s * 0.2f, head.y + s * 0.05f)
        cubicTo(head.x - s * 0.42f, head.y + s * 0.25f, head.x - s * 0.36f + sway, head.y + s * 0.6f, head.x - s * 0.46f + sway, head.y + s * 0.78f)
    }
    drawPath(trunk, col, alpha = alpha, style = Stroke(s * 0.11f, cap = StrokeCap.Round))
    drawPath(Path().apply { moveTo(head.x - s * 0.14f, head.y + s * 0.15f); quadraticTo(head.x - s * 0.3f, head.y + s * 0.38f, head.x - s * 0.52f, head.y + s * 0.3f) },
        FilC32.Tusk, alpha = alpha, style = Stroke(s * 0.05f, cap = StrokeCap.Round))
    // tail
    drawLine(FilC32.EleDark, c + Offset(s * 0.7f, -s * 0.1f), c + Offset(s * 0.82f, s * 0.25f), s * 0.03f, StrokeCap.Round, alpha = alpha)
    if (glow > 0f) drawOval(Color(0xFFFFF2D0), Offset(c.x - s * 0.7f, c.y - s * 0.4f), Size(s * 1.4f, s * 0.75f), alpha = 0.5f * glow * alpha, style = Stroke(2f * density))
}

/**
 * Beats: blindsix (close in the dark: six blindfolded men, each with his hands on one part of something huge),
 * parts (what each one says: «رُمْحٌ» at the tusk, «ثُعْبَانٌ» at the trunk, «شَجَرَةٌ» at the leg, «جِدَارٌ» at the
 * flank), pullout (the camera draws back: the whole elephant, seen from outside), overview (light from above over
 * the whole scene, and what only it answers: «الْغَايَةُ»، «الْمَعْنَى»، «الْقِيمَةُ»).
 */
internal fun DrawScope.elephantScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "blindsix" to Shot(Offset(w * 0.32f, h * 0.66f), 1.9f),
        "parts" to Shot(Offset(w * 0.4f, h * 0.62f), 1.4f),
        "pullout" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "overview" to Shot(Offset(w * 0.5f, h * 0.48f), 1.02f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.55f), 1f), dim = 0.1f) { elephantHall(a) }
}

private fun DrawScope.elephantHall(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val bs = a.b("blindsix")
    val pa = a.b("parts")
    val po = a.b("pullout")
    val ov = a.b("overview")
    val floor = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(FilC32.Wall, FilC32.WallLow), endY = floor), size = Size(w, floor))
    drawRect(FilC32.Floor, Offset(0f, floor), Size(w, h - floor))
    val light = easeOut(seg(ov, 0f, 0.5f))
    if (light > 0f) {
        lightShafts(Offset(w * 0.5f, -h * 0.1f), PI.toFloat() / 2f, 0.9f, h, Color(0xFFFFF4D8), 0.6f * light, t, rays = 9)
        drawRect(Color(0xFFFFF4D8), size = Size(w, h), alpha = 0.12f * light)
    }
    val ec = Offset(w * 0.55f, h * 0.5f)
    val s = w * 0.25f
    val see = easeOut(seg(po, 0.1f, 0.6f))
    elephant32(ec, s, 0.55f + 0.45f * maxOf(see, light), light, t)
    // six blindfolded men, hands on six parts
    val spots = listOf(
        Offset(w * 0.24f, floor) to (ec + Offset(-s * 1.08f, s * 0.25f)),      // tusk
        Offset(w * 0.17f, floor) to (ec + Offset(-s * 1.12f, s * 0.62f)),      // trunk
        Offset(w * 0.38f, floor) to (ec + Offset(-s * 0.5f, s * 0.45f)),       // leg
        Offset(w * 0.56f, floor) to (ec + Offset(-s * 0.02f, s * 0.2f)),       // flank
        Offset(w * 0.72f, floor) to (ec + Offset(s * 0.42f, s * 0.48f)),       // hind leg
        Offset(w * 0.86f, floor) to (ec + Offset(s * 0.8f, s * 0.2f)),         // tail
    )
    for ((k, sp) in spots.withIndex()) {
        val f = easeOut(seg(bs, k * 0.08f, 0.3f + k * 0.08f))
        val tall = h * 0.2f
        figure(sp.first, tall, FilC32.Man, f, lean = if (sp.second.x < sp.first.x) 0.18f else -0.18f, arms = 1f)
        // the blindfold
        val head = sp.first - Offset(0f, tall * 0.92f)
        drawLine(FilC32.Band, head + Offset(-tall * 0.07f, 0f), head + Offset(tall * 0.07f, 0f), tall * 0.035f, StrokeCap.Round, alpha = f)
        // the hand that touches
        drawLine(FilC32.Man, sp.first - Offset(0f, tall * 0.7f), sp.second, tall * 0.04f, StrokeCap.Round, alpha = 0.8f * f)
        drawCircle(Color(0xFFFFE8B0), w * 0.006f, sp.second, alpha = f * (0.5f + 0.5f * sin(t * 3f + k)))
    }
    // what each one is sure it is
    val partsAt = listOf(Offset(w * 0.22f, h * 0.42f), Offset(w * 0.09f, h * 0.6f), Offset(w * 0.37f, h * 0.38f), Offset(w * 0.58f, h * 0.26f))
    for (k in 0 until 4) {
        tagAt(a.tag("parts", 0, k), partsAt[k], seg(pa, 0.1f + k * 0.15f, 0.3f + k * 0.15f) * (1f - seg(ov, 0f, 0.3f)), back = Back32, ink = Color(0xFFFFD0A8))
    }
    // what only the light from outside answers
    for (k in 0 until 3) {
        tagAt(a.tag("overview", 0, k), Offset(w * (0.3f + 0.2f * k), h * 0.12f), seg(ov, 0.3f + k * 0.15f, 0.55f + k * 0.15f), back = Back32, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. the plate that only shows the light it is given (bs-baqaya) ═════════════

private object PlateC32 {
    val Night = Color(0xFF101428)
    val NightLow = Color(0xFF242A44)
    val Land = Color(0xFF1C1A26)
    val House = Color(0xFF2E2A3A)
    val Ember = Color(0xFFFFB45A)
    val Bars = Color(0xFF8A8E9A)
    val Plate = Color(0xFF16182A)
    val PlateLit = Color(0xFFFFE8B0)
    val Man = Color(0xFF0C0A12)
}

/**
 * Beats: embers (a dark land; here and there a small warm light still glows in a window: «بَقَايَا النُّبُوَّاتِ»),
 * cage (a cage on show, a man behind its bars, under a cold lamp), cageopen (an ember's light reaches it, the bars
 * lift, he walks out), plate (a dark plate on a stand: a shaft of light comes down to it), plateglow (it glows only
 * where the light falls: «الْفِطْرَةُ»، «النُّبُوَّةُ»).
 */
internal fun DrawScope.detectorScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "embers" to Shot(Offset(w * 0.4f, h * 0.55f), 1.15f),
        "cage" to Shot(Offset(w * 0.78f, h * 0.62f), 1.5f),
        "cageopen" to Shot(Offset(w * 0.72f, h * 0.62f), 1.35f),
        "plate" to Shot(Offset(w * 0.42f, h * 0.6f), 1.35f),
        "plateglow" to Shot(Offset(w * 0.42f, h * 0.55f), 1.2f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { detectorNight(a) }
}

private fun DrawScope.detectorNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val em = a.b("embers")
    val cg = a.b("cage")
    val co = a.b("cageopen")
    val pl = a.b("plate")
    val pg = a.b("plateglow")
    val ground = h * 0.78f
    nightSky(t, PlateC32.Night, PlateC32.NightLow, ground, starAlpha = 0.6f)
    drawRect(PlateC32.Land, Offset(0f, ground), Size(w, h - ground))
    // houses, a few still holding a small warm light
    val houses = listOf(0.06f, 0.16f, 0.27f, 0.6f, 0.92f)
    for ((k, x) in houses.withIndex()) {
        val hw = w * 0.07f
        val hh = h * (0.12f + 0.03f * (k % 2))
        drawRect(PlateC32.House, Offset(w * x - hw / 2f, ground - hh), Size(hw, hh))
        drawPath(Path().apply { moveTo(w * x - hw * 0.6f, ground - hh); lineTo(w * x, ground - hh - h * 0.05f); lineTo(w * x + hw * 0.6f, ground - hh); close() }, PlateC32.House)
        if (k % 2 == 0) {
            val f = easeOut(seg(em, 0.1f + k * 0.1f, 0.4f + k * 0.1f))
            val win = Offset(w * x, ground - hh * 0.55f)
            drawRect(PlateC32.Ember, Offset(win.x - w * 0.012f, win.y - h * 0.02f), Size(w * 0.024f, h * 0.04f), alpha = f * (0.85f + 0.15f * sin(t * 3f + k)))
            softGlow(win, w * 0.05f, PlateC32.Ember, 0.6f * f)
        }
    }
    tagAt(a.tag("embers"), Offset(w * 0.17f, h * 0.36f), seg(em, 0.4f, 0.65f) * (1f - seg(pl, 0f, 0.3f)), back = Back32, ink = ArtInk.Gold)
    // the cage on show
    val cageC = Offset(w * 0.78f, ground)
    if (cg > 0f) {
        val f = easeOut(seg(cg, 0f, 0.3f))
        val cw = w * 0.16f
        val ch = h * 0.3f
        drawRect(Color(0xFF242436), Offset(cageC.x - cw / 2f, cageC.y - ch), Size(cw, ch), alpha = 0.7f * f)
        val walkOut = easeInOut(seg(co, 0.35f, 0.9f))
        figure(Offset(cageC.x - w * 0.15f * walkOut, ground), h * 0.2f, PlateC32.Man, f, walk = if (co in 0.35f..0.95f) t * 4f else 0f, arms = mix(0.1f, 0.5f, walkOut))
        val lift = easeInOut(seg(co, 0.15f, 0.45f))
        for (k in 0 until 7) {
            val x = cageC.x - cw / 2f + cw * k / 6f
            drawLine(PlateC32.Bars, Offset(x, cageC.y - ch - ch * lift), Offset(x, cageC.y - ch * lift), 2f * dp, alpha = f * (1f - 0.7f * lift))
        }
        drawLine(PlateC32.Bars, Offset(cageC.x - cw / 2f, cageC.y - ch), Offset(cageC.x + cw / 2f, cageC.y - ch), 3f * dp, alpha = f)
        // the ember's light reaching it
        if (co > 0f) {
            val reach = easeOut(seg(co, 0f, 0.35f))
            val from = Offset(w * 0.6f, ground - h * 0.08f)
            drawLine(PlateC32.Ember, from, from + (cageC - Offset(0f, h * 0.15f) - from) * reach, 2f * dp, StrokeCap.Round, alpha = 0.6f)
            softGlow(cageC - Offset(0f, h * 0.15f), w * 0.1f, PlateC32.Ember, 0.4f * reach)
        }
    }
    // the plate and the light given to it
    val plC = Offset(w * 0.42f, ground - h * 0.12f)
    if (pl > 0f) {
        val f = easeOut(seg(pl, 0f, 0.3f))
        drawLine(Color(0xFF3A3A48), Offset(plC.x, plC.y + h * 0.05f), Offset(plC.x, ground), 3f * dp, alpha = f)
        drawRoundRect(PlateC32.Plate, Offset(plC.x - w * 0.09f, plC.y - h * 0.06f), Size(w * 0.18f, h * 0.11f), CornerRadius(4f * dp), alpha = f)
        drawRoundRect(Color(0xFF5A5E70), Offset(plC.x - w * 0.09f, plC.y - h * 0.06f), Size(w * 0.18f, h * 0.11f), CornerRadius(4f * dp), alpha = f, style = Stroke(1.5f * dp))
        val beam = easeOut(seg(pl, 0.3f, 0.8f))
        lightShafts(Offset(plC.x, -h * 0.05f), PI.toFloat() / 2f, 0.12f, (plC.y - h * 0.06f + h * 0.05f) * beam, Color(0xFFFFF4D8), 0.8f * beam, t, rays = 4)
        val lit = maxOf(seg(pl, 0.7f, 1f) * 0.5f, easeOut(seg(pg, 0f, 0.5f)))
        drawRoundRect(PlateC32.PlateLit, Offset(plC.x - w * 0.09f * lit, plC.y - h * 0.06f), Size(w * 0.18f * lit, h * 0.11f), CornerRadius(4f * dp), alpha = 0.85f * lit)
        if (lit > 0f) softGlow(plC, w * 0.12f, PlateC32.PlateLit, 0.5f * lit)
    }
    for (k in 0 until 2) {
        tagAt(a.tag("plateglow", 0, k), plC + Offset(if (k == 0) w * 0.17f else -w * 0.17f, -h * 0.12f), seg(pg, 0.3f + 0.2f * k, 0.55f + 0.2f * k), back = Back32, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. the hill of Safa (bs-asdaq) ═════════════

private object SafaC32 {
    val Sky = Color(0xFFF2D9A8)
    val SkyHigh = Color(0xFF8AA6C8)
    val Hill = Color(0xFFB89A6A)
    val HillDark = Color(0xFF8C7048)
    val Valley = Color(0xFF6A5A48)
    val Crowd = Color(0xFF2E2620)
    val True = Color(0xFFF4D27A)
    val False = Color(0xFF5A4A5A)
}

/**
 * Beats: twopoles (two pillars and nothing between: «أَصْدَقُ الصَّادِقِينَ», bright, and «أَكْذَبُ الْكَاذِبِينَ»,
 * dark), safahill (morning on the hill of Safa: his light on the top), witnessed (his people gather below and answer:
 * «مَا جَرَّبْنَا عَلَيْكَ إِلَّا صِدْقًا»), samethread (a thread of light comes from far back on the horizon to his
 * light: what the prophets before him brought, «التَّوْحِيدُ»، «مَكَارِمُ الْأَخْلَاقِ»).
 */
internal fun DrawScope.safaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twopoles" to Shot(Offset(w * 0.5f, h * 0.45f), 1.1f),
        "safahill" to Shot(Offset(w * 0.55f, h * 0.4f), 1.25f),
        "witnessed" to Shot(Offset(w * 0.52f, h * 0.62f), 1.2f),
        "samethread" to Shot(Offset(w * 0.45f, h * 0.4f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { safaMorning(a) }
}

private fun DrawScope.safaMorning(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tp = a.b("twopoles")
    val sh = a.b("safahill")
    val wt = a.b("witnessed")
    val st = a.b("samethread")
    drawRect(Brush.verticalGradient(listOf(SafaC32.SkyHigh, SafaC32.Sky), endY = h * 0.7f), size = Size(w, h))
    farHills(Color(0xFFA08A6A), SafaC32.Sky, h * 0.66f, h * 0.05f, 0.7f)
    // the hill
    val top = Offset(w * 0.58f, h * 0.3f)
    drawPath(Path().apply { moveTo(w * 0.28f, h * 0.82f); quadraticTo(w * 0.45f, h * 0.36f, top.x, top.y); quadraticTo(w * 0.72f, h * 0.36f, w * 0.9f, h * 0.82f); close() },
        Brush.verticalGradient(listOf(SafaC32.Hill, SafaC32.HillDark), startY = top.y, endY = h * 0.82f))
    drawRect(SafaC32.Valley, Offset(0f, h * 0.8f), Size(w, h * 0.2f))
    // the two pillars, nothing between them
    val poles = easeOut(seg(tp, 0f, 0.3f)) * (1f - easeOut(seg(sh, 0f, 0.3f)))
    if (poles > 0f) {
        for (k in 0 until 2) {
            val x = if (k == 0) w * 0.2f else w * 0.8f
            val col = if (k == 0) SafaC32.True else SafaC32.False
            drawRect(col, Offset(x - w * 0.025f, h * 0.3f), Size(w * 0.05f, h * 0.5f), alpha = poles)
            if (k == 0) softGlow(Offset(x, h * 0.3f), w * 0.08f, SafaC32.True, 0.6f * poles)
            tagAt(a.tag("twopoles", 0, k), Offset(x, h * 0.22f), seg(tp, 0.2f + 0.2f * k, 0.45f + 0.2f * k) * poles, back = Back32, ink = if (k == 0) ArtInk.Gold else Color(0xFFD0C0D8))
        }
        drawLine(Color(0xFF8A7A6A), Offset(w * 0.3f, h * 0.55f), Offset(w * 0.7f, h * 0.55f), 1.5f * dp, alpha = 0.6f * poles, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp)))
    }
    // the thread of light from the prophets before him
    if (st > 0f) {
        val run = easeInOut(seg(st, 0f, 0.6f))
        val from = Offset(-w * 0.02f, h * 0.5f)
        val ctrl = Offset(w * 0.3f, h * 0.2f)
        val path = Path()
        for (k in 0..24) {
            val u = k / 24f * run
            val q = from * ((1 - u) * (1 - u)) + ctrl * (2 * u * (1 - u)) + top * (u * u)
            if (k == 0) path.moveTo(q.x, q.y) else path.lineTo(q.x, q.y)
        }
        drawPath(path, Color(0xFFFFF0C0), alpha = 0.9f, style = Stroke(2.5f * dp, cap = StrokeCap.Round))
        for (k in 0 until 2) {
            val u = 0.35f + 0.3f * k
            val q = from * ((1 - u) * (1 - u)) + ctrl * (2 * u * (1 - u)) + top * (u * u)
            tagAt(a.tag("samethread", 0, k), q + Offset(0f, -h * 0.07f), seg(st, 0.4f + 0.2f * k, 0.65f + 0.2f * k), back = Back32, ink = ArtInk.Gold)
        }
    }
    // his light on the top
    sirajMunir(top + Offset(0f, -h * 0.05f), w * 0.012f, easeOut(seg(sh, 0.1f, 0.5f)), t, nameAt = top + Offset(0f, -h * 0.17f))
    // his people below
    if (wt > 0f) {
        for (k in 0 until 11) {
            val f = easeOut(seg(wt, k * 0.04f, 0.25f + k * 0.04f))
            val x = w * (0.3f + 0.05f * k) + (if (k % 2 == 0) 0f else w * 0.01f)
            val y = h * (0.86f + 0.04f * (k % 3))
            figure(Offset(x, y), h * 0.12f, SafaC32.Crowd, f, lean = (x - top.x) / w * 0.3f)
        }
        tagAt(a.tag("witnessed"), Offset(w * 0.5f, h * 0.66f), seg(wt, 0.4f, 0.7f) * (1f - seg(st, 0f, 0.3f)), back = Back32, ink = ArtInk.Ivory)
    }
}

// ═════════════ 6. the wall and the mist (bs-muhal) ═════════════

private object MistC32 {
    val Sky = Color(0xFFD6DDE8)
    val SkyLow = Color(0xFFEFE8DA)
    val Ground = Color(0xFFB8A888)
    val Road = Color(0xFFDCCCA8)
    val Wall = Color(0xFF7A6E66)
    val Mist = Color(0xFFF4F4F8)
    val Peak = Color(0xFF8C98B0)
    val Sea = Color(0xFF4A6070)
    val Mud = Color(0xFF6A5A40)
    val Sun = Color(0xFFFFB050)
    val Tablet = Color(0xFFE8DCC0)
    val Red = Color(0xFFD23C30)
}

/**
 * Beats: impossible (a road ends at a wall bearing a shape that cannot be — a circle that is also a square — struck
 * red: «مُحَالٌ»), wonder (another road climbs into a bright mist over the peaks: the mind cannot see through it, yet
 * nothing blocks it, «مُحَارٌ»), align (a blurred tile slides into the clear frame of a tablet and becomes sharp:
 * «الْمُتَشَابِهُ» read by «الْمُحْكَمُ»), sunsea (far off, the sun seems to sink into a dark sea; a dotted path shows
 * it going on beyond the horizon).
 */
internal fun DrawScope.mistpathScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "impossible" to Shot(Offset(w * 0.22f, h * 0.5f), 1.45f),
        "wonder" to Shot(Offset(w * 0.72f, h * 0.4f), 1.35f),
        "align" to Shot(Offset(w * 0.5f, h * 0.75f), 1.45f),
        "sunsea" to Shot(Offset(w * 0.5f, h * 0.38f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { mistLand(a) }
}

private fun DrawScope.mistLand(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val im = a.b("impossible")
    val wo = a.b("wonder")
    val al = a.b("align")
    val ss = a.b("sunsea")
    val horizon = h * 0.42f
    drawRect(Brush.verticalGradient(listOf(MistC32.Sky, MistC32.SkyLow), endY = horizon), size = Size(w, horizon))
    // the far sea, centre
    drawRect(MistC32.Sea, Offset(w * 0.36f, horizon - h * 0.005f), Size(w * 0.28f, h * 0.04f))
    if (ss > 0f) {
        val sink = easeInOut(seg(ss, 0.1f, 0.7f))
        val sun = Offset(w * 0.5f, horizon - h * 0.08f + h * 0.09f * sink)
        drawCircle(MistC32.Sun, w * 0.03f, sun, alpha = easeOut(seg(ss, 0f, 0.15f)))
        drawRect(MistC32.Mud, Offset(w * 0.36f, horizon), Size(w * 0.28f, h * 0.04f), alpha = 0.9f)
        // what the eye sees ends here; the dotted course goes on
        val beyond = seg(ss, 0.6f, 1f)
        if (beyond > 0f) {
            val arc = Path()
            for (k in 0..20) {
                val u = k / 20f * beyond
                val p = Offset(w * 0.5f + w * 0.25f * u, horizon + h * 0.02f + sin(PI.toFloat() * u) * h * 0.08f - h * 0.08f * u * u)
                if (k == 0) arc.moveTo(p.x, p.y) else arc.lineTo(p.x, p.y)
            }
            drawPath(arc, MistC32.Sun, alpha = 0.8f, style = Stroke(1.5f * dp, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dp, 3f * dp))))
        }
    }
    drawRect(MistC32.Ground, Offset(0f, horizon + h * 0.035f), Size(w, h))
    // the peaks and the mist, right
    drawPath(Path().apply { moveTo(w * 0.62f, horizon + h * 0.04f); lineTo(w * 0.74f, h * 0.18f); lineTo(w * 0.84f, h * 0.28f); lineTo(w * 0.92f, h * 0.14f); lineTo(w, horizon); lineTo(w, horizon + h * 0.04f); close() }, MistC32.Peak)
    // two roads from the foreground
    val fork = Offset(w * 0.5f, h * 0.62f)
    drawPath(Path().apply { moveTo(fork.x - w * 0.04f, h); lineTo(fork.x + w * 0.04f, h); lineTo(fork.x + w * 0.02f, fork.y); lineTo(fork.x - w * 0.02f, fork.y); close() }, MistC32.Road)
    drawLine(MistC32.Road, fork, Offset(w * 0.22f, h * 0.5f), w * 0.025f, StrokeCap.Round)
    drawLine(MistC32.Road, fork, Offset(w * 0.76f, h * 0.36f), w * 0.025f, StrokeCap.Round)
    // the wall with the shape that cannot be
    if (im > 0f) {
        val f = easeOut(seg(im, 0f, 0.3f))
        drawRect(MistC32.Wall, Offset(w * 0.1f, h * 0.36f), Size(w * 0.2f, h * 0.18f), alpha = f)
        for (r in 0 until 3) drawLine(Color(0xFF5A5048), Offset(w * 0.1f, h * (0.42f + 0.06f * r)), Offset(w * 0.3f, h * (0.42f + 0.06f * r)), 1f * dp, alpha = f)
        val morph = 0.5f + 0.5f * sin(t * 1.5f)
        val c = Offset(w * 0.2f, h * 0.45f)
        val r = w * 0.04f
        drawRoundRect(Color(0xFFF2E8D6), Offset(c.x - r, c.y - r), Size(2 * r, 2 * r), CornerRadius(r * morph), alpha = f, style = Stroke(2f * dp))
        cross21(c, r * 1.2f, MistC32.Red, seg(im, 0.4f, 0.8f), 3f * dp)
        tagAt(a.tag("impossible"), Offset(w * 0.2f, h * 0.3f), seg(im, 0.5f, 0.75f), back = Back32, ink = Color(0xFFFFB0A0))
    }
    // the bright mist the road climbs into
    if (wo > 0f) {
        val f = easeOut(seg(wo, 0f, 0.5f))
        for (k in 0 until 8) {
            val c = Offset(w * (0.66f + 0.045f * k) + w * 0.01f * sin(t * 0.3f + k), h * (0.24f + 0.04f * (k % 3)))
            drawCircle(MistC32.Mist, w * (0.06f + 0.015f * (k % 2)), c, alpha = 0.55f * f)
        }
        softGlow(Offset(w * 0.8f, h * 0.24f), w * 0.1f, Color(0xFFFFF4D8), 0.5f * f)
        check21(Offset(w * 0.68f, h * 0.44f), w * 0.022f, Color(0xFF2E9A55), seg(wo, 0.4f, 0.8f), 3f * dp)
        tagAt(a.tag("wonder"), Offset(w * 0.8f, h * 0.1f), seg(wo, 0.4f, 0.65f), back = Back32, ink = Color(0xFFD8ECFF))
    }
    // the tablet and the tile
    if (al > 0f) {
        val f = easeOut(seg(al, 0f, 0.3f))
        val frame = Offset(w * 0.5f, h * 0.8f)
        drawRoundRect(MistC32.Tablet, Offset(frame.x - w * 0.12f, frame.y - h * 0.09f), Size(w * 0.24f, h * 0.18f), CornerRadius(6f * dp), alpha = f)
        drawRect(Color(0xFF5A4A36), Offset(frame.x - w * 0.05f, frame.y - h * 0.05f), Size(w * 0.1f, h * 0.1f), alpha = f, style = Stroke(2f * dp))
        val slide = easeInOut(seg(al, 0.3f, 0.8f))
        val tile = Offset(mix(w * 0.8f, frame.x, slide), mix(h * 0.7f, frame.y, slide))
        val blur = 1f - slide
        for (k in 0 until 4) {
            val o = w * 0.006f * blur * k
            drawRect(Color(0xFF8A6A3A), Offset(tile.x - w * 0.045f + o, tile.y - h * 0.045f - o), Size(w * 0.09f, h * 0.09f), alpha = f * (if (k == 0) 0.9f else 0.25f * blur))
        }
        // a clear mark appears in it once it sits in the frame
        drawLine(ArtInk.Ivory, tile + Offset(-w * 0.025f, 0f), tile + Offset(w * 0.025f, 0f), 2.5f * dp, StrokeCap.Round, alpha = f * slide)
        drawLine(ArtInk.Ivory, tile + Offset(0f, -h * 0.025f), tile + Offset(0f, h * 0.025f), 2.5f * dp, StrokeCap.Round, alpha = f * slide)
        tagAt(a.tag("align", 0, 0), Offset(w * 0.8f, h * 0.6f), seg(al, 0.1f, 0.35f) * (1f - slide), back = Back32, ink = Color(0xFFE8D8C0))
        tagAt(a.tag("align", 0, 1), frame + Offset(0f, -h * 0.14f), seg(al, 0.5f, 0.8f), back = Back32, ink = ArtInk.Gold)
    }
}

// ═════════════ 7. a thousand signs, many chains, one pot (bs-taeed) ═════════════

private object TawC32 {
    val Night = Color(0xFF141830)
    val NightLow = Color(0xFF2E2A48)
    val Ground = Color(0xFF2A2430)
    val Spark = Color(0xFFFFE8A8)
    val Dot = Color(0xFFE8D8B0)
    val Pot = Color(0xFF8A4A2A)
    val PotDark = Color(0xFF5A2E1A)
    val Bowl = Color(0xFFC89A5A)
    val Food = Color(0xFFF2E2C0)
}

/**
 * Beats: sparks (the night fills with small lights, more than a thousand: «1000+»), converge (chains of dots — men
 * who passed the news on — come from every side into one bright point: «مُتَوَاتِرٌ»), pot (a pot over a fire, and
 * bowl after bowl filled from it, ring after ring), ringclose (a ring of light closes round all of it: he was
 * supported until what he brought was complete).
 */
internal fun DrawScope.tawaturScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sparks" to Shot(Offset(w * 0.5f, h * 0.35f), 1.1f),
        "converge" to Shot(Offset(w * 0.5f, h * 0.45f), 1.15f),
        "pot" to Shot(Offset(w * 0.5f, h * 0.7f), 1.35f),
        "ringclose" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { tawaturNight(a) }
}

private fun DrawScope.tawaturNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sk = a.b("sparks")
    val cv = a.b("converge")
    val po = a.b("pot")
    val rc = a.b("ringclose")
    val ground = h * 0.86f
    nightSky(t, TawC32.Night, TawC32.NightLow, ground, starAlpha = 0.4f)
    drawRect(TawC32.Ground, Offset(0f, ground), Size(w, h - ground))
    // more than a thousand small lights
    if (sk > 0f) {
        val n = (180 * easeOut(seg(sk, 0f, 0.8f))).toInt()
        for (k in 0 until n) {
            val p = Offset(w * w8Rnd(k, 12.9898f), h * 0.62f * w8Rnd(k, 78.233f))
            drawCircle(TawC32.Spark, w * (0.002f + 0.003f * w8Rnd(k, 3.7f)), p, alpha = 0.5f + 0.5f * sin(t * 2f + k))
        }
        tagAt(a.tag("sparks"), Offset(w * 0.5f, h * 0.1f), seg(sk, 0.4f, 0.7f) * (1f - seg(cv, 0f, 0.3f)), back = Back32, ink = ArtInk.Gold)
    }
    // chains from every side into one point
    val hub = Offset(w * 0.5f, h * 0.42f)
    if (cv > 0f) {
        for (c in 0 until 9) {
            val ang = PI.toFloat() * (c / 8f) + PI.toFloat()
            val start = hub + Offset(cos(ang) * w * 0.48f, sin(ang) * h * 0.4f + h * 0.15f)
            val run = easeOut(seg(cv, c * 0.04f, 0.5f + c * 0.04f))
            for (k in 0 until 6) {
                val u = k / 6f
                if (u > run) break
                val p = start + (hub - start) * u
                drawCircle(TawC32.Dot, w * 0.006f, p, alpha = 0.85f)
                if (k > 0) drawLine(TawC32.Dot, start + (hub - start) * ((k - 1) / 6f), p, 1f * dp, alpha = 0.4f)
            }
        }
        val core = easeOut(seg(cv, 0.5f, 0.8f))
        softGlow(hub, w * 0.06f, TawC32.Spark, 0.8f * core)
        drawCircle(TawC32.Spark, w * 0.012f, hub, alpha = core)
        tagAt(a.tag("converge"), hub + Offset(0f, -h * 0.12f), seg(cv, 0.6f, 0.85f), back = Back32, ink = ArtInk.Gold)
    }
    // the pot that fed the whole camp
    if (po > 0f) {
        val f = easeOut(seg(po, 0f, 0.2f))
        val c = Offset(w * 0.5f, ground - h * 0.08f)
        for (k in 0 until 5) {
            val fl = fract(t * 1.5f + k * 0.2f)
            drawCircle(Color(0xFFFF9A3A), w * 0.012f * (1f - fl), c + Offset(w * (-0.03f + 0.015f * k), h * 0.07f - h * 0.04f * fl), alpha = f * (1f - fl))
        }
        drawOval(TawC32.Pot, Offset(c.x - w * 0.06f, c.y - h * 0.06f), Size(w * 0.12f, h * 0.12f), alpha = f)
        drawRect(TawC32.PotDark, Offset(c.x - w * 0.05f, c.y - h * 0.065f), Size(w * 0.1f, h * 0.02f), alpha = f)
        for (k in 0 until 3) {
            val fl = fract(t * 0.5f + k / 3f)
            drawCircle(Color.White, w * (0.01f + 0.015f * fl), c + Offset(w * (-0.02f + 0.02f * k), -h * 0.08f - h * 0.08f * fl), alpha = 0.35f * (1f - fl) * f)
        }
        val bowls = (24 * easeOut(seg(po, 0.2f, 0.95f))).toInt()
        for (k in 0 until bowls) {
            val ring = k / 8
            val ang = PI.toFloat() * (0.08f + 0.84f * ((k % 8) / 7f)) + PI.toFloat()
            val r = w * (0.12f + 0.07f * ring)
            val p = c + Offset(cos(ang) * r, sin(ang) * r * 0.45f + h * 0.05f)
            drawArc(TawC32.Bowl, 0f, 180f, true, Offset(p.x - w * 0.018f, p.y - h * 0.018f), Size(w * 0.036f, h * 0.036f))
            drawOval(TawC32.Food, Offset(p.x - w * 0.016f, p.y - h * 0.006f), Size(w * 0.032f, h * 0.012f))
        }
    }
    // the ring of light closes
    if (rc > 0f) {
        val sweep = 360f * easeInOut(seg(rc, 0f, 0.7f))
        val r = h * 0.42f
        drawArc(Color(0xFFFFE6A8), -90f, sweep, false, Offset(w * 0.5f - r, h * 0.48f - r), Size(2 * r, 2 * r), alpha = 0.9f, style = Stroke(3f * dp, cap = StrokeCap.Round))
        if (sweep >= 359f) softGlow(Offset(w * 0.5f, h * 0.48f), r, Color(0xFFFFE6A8), 0.25f)
    }
}
