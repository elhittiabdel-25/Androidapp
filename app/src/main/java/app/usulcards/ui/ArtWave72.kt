package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sin

/*
 * Wave 72 (2026-11-03): v1.3 fiqh, «مجالس شهر رمضان» (Ibn ʿUthaymīn), the first six fasting cards. The gates opened and shut and the
 * devils as chained smoke; the fast as a secret, its scent, its shield and its gate; the new moon and the thirty days; who must
 * fast; the traveller and the sick; the days a woman makes up, drawn only as days on a calendar. People are faceless figures; the
 * companions, where the text names them, are plain roundels.
 */

internal object RamC {
    val Top = Color(0xFF0E1430)
    val Low = Color(0xFF2E2A5A)
    val Ground = Color(0xFF1A1826)
    val Gate = Color(0xFFD8C8A0)
    val Paradise = Color(0xFF8AE0B0)
    val Fire = Color(0xFFE0603A)
    val Chain = Color(0xFF9A9AA8)
    val Smoke = Color(0xFF2A2430)
    val Lit = Color(0xFFF2C860)
    val Dark = Color(0xFF3A3040)
    val Rose = Color(0xFFB07A86)
    val Bowl = Color(0xFFB07A48)
    val Date = Color(0xFF6A3A22)
    val Person = Color(0xFFE8DCC8)
    val Rayyan = Color(0xFF7CC4E8)
    val Cell = Color(0xFF3A4468)
    val Rim = Color(0xFF8A94B8)
}

/** A Ramadan night: deep sky, dark ground and the crescent high on the right. */
internal fun DrawScope.ramNight72(t: Float, ground: Float, moon: Float = 1f) {
    nightSky(t, RamC.Top, RamC.Low, ground, starAlpha = 0.8f)
    drawRect(RamC.Ground, Offset(0f, ground), Size(size.width, size.height - ground))
    if (moon > 0.01f) crescent12(Offset(size.width * 0.86f, size.height * 0.12f), size.width * 0.04f, RamC.Lit, RamC.Top, moon)
}

/** A gate of two leaves under an arch, its sill centred on [sill]; [open] 0..1 swings the leaves back and lets [glow] through. */
internal fun DrawScope.gate72(sill: Offset, wd: Float, ht: Float, open: Float, frame: Color, glow: Color, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    val hw = wd / 2f
    val arch = archPath26(sill.x, sill.y - ht, sill.y, hw)
    drawPath(arch, Color.Black, alpha = alpha * 0.5f)
    if (open > 0.01f) {
        drawPath(arch, glow, alpha = alpha * 0.85f * open)
        softGlow(Offset(sill.x, sill.y - ht * 0.45f), wd * (0.6f + 0.1f * sin(t * 2f)), glow, alpha * 0.55f * open)
    }
    val leaf = hw * (1f - 0.85f * easeOut(open))
    for (sg in listOf(-1f, 1f)) {
        val x0 = sill.x + sg * hw
        val x1 = x0 - sg * leaf
        drawRect(lerpColor(frame, Color.Black, 0.4f), Offset(minOf(x0, x1), sill.y - ht * 0.78f), Size(abs(x1 - x0), ht * 0.78f), alpha = alpha)
    }
    drawPath(arch, frame, alpha = alpha, style = Stroke(wd * 0.05f))
}

/** A chain of links from [from] to [to]. */
internal fun DrawScope.chain72(from: Offset, to: Offset, link: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val d = to - from
    val n = (d.getDistance() / (link * 1.5f)).toInt().coerceAtLeast(1)
    val ang = atan2(d.y, d.x) * 180f / PI.toFloat()
    for (i in 0 until n) {
        val c = from + d * ((i + 0.5f) / n)
        rotate(ang, c) {
            if (i % 2 == 0) drawOval(color, Offset(c.x - link, c.y - link * 0.5f), Size(link * 2f, link), alpha = alpha, style = Stroke(link * 0.3f))
            else drawLine(color, c - Offset(link * 0.9f, 0f), c + Offset(link * 0.9f, 0f), link * 0.35f, StrokeCap.Round, alpha = alpha)
        }
    }
}

/** A bowl of dates, its base at [foot]. */
internal fun DrawScope.dateBowl72(foot: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 5) {
        val c = foot + Offset(s * (-0.3f + 0.15f * k), -s * (0.32f + 0.06f * (k % 2)))
        drawOval(RamC.Date, Offset(c.x - s * 0.1f, c.y - s * 0.06f), Size(s * 0.2f, s * 0.12f), alpha = alpha)
    }
    drawArc(RamC.Bowl, 0f, 180f, true, Offset(foot.x - s * 0.45f, foot.y - s * 0.55f), Size(s * 0.9f, s * 0.6f), alpha = alpha)
}

/** A quill, its nib at [nib], leaning right. */
internal fun DrawScope.quill72(nib: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    rotate(35f, nib) {
        drawOval(color, Offset(nib.x - s * 0.1f, nib.y - s), Size(s * 0.2f, s * 0.8f), alpha = alpha)
        drawLine(lerpColor(color, Color.Black, 0.4f), nib, nib + Offset(0f, -s * 0.95f), s * 0.03f, alpha = alpha)
    }
}

// ═════════════ 1. ram-fadl: the gates of Ramadan ═════════════

/**
 * Beats: jannahopen (the gate on the right swings open on a green light: «أَبْوَابُ الْجَنَّةِ»), narshut (the gate on the left, red behind
 * it, swings shut: «أَبْوَابُ النَّارِ»), chainedsmoke (dark smoke between them, bound by two chains: «وَتُصَفَّدُ الشَّيَاطِينُ»),
 * forgivenpast (a dark slate of marks is wiped clean by a band of light: «إِيمَانًا وَاحْتِسَابًا», «غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ»),
 * threespans (three spans, a day of five prayers, a week, a year, each clearing the dark between its ends: «الصَّلَوَاتُ الْخَمْسُ»,
 * «وَالْجُمُعَةُ إِلَى الْجُمُعَةِ», «وَرَمَضَانُ إِلَى رَمَضَانَ»).
 */
internal fun DrawScope.ramadangatesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "jannahopen" to Shot(Offset(w * 0.62f, h * 0.55f), 1.12f),
        "narshut" to Shot(Offset(w * 0.38f, h * 0.55f), 1.12f),
        "chainedsmoke" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "forgivenpast" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "threespans" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { ramadanGatesDraw(a) }
}

private fun DrawScope.ramadanGatesDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    ramNight72(t, ground)
    val jo = fqIn(a, "jannahopen", "forgivenpast")
    val ns = fqIn(a, "narshut", "forgivenpast")
    val cs = fqIn(a, "chainedsmoke", "forgivenpast")
    val fp = fqIn(a, "forgivenpast", "threespans")
    val ts = fqIn(a, "threespans")
    if (jo > 0f) {
        val p = a.b("jannahopen")
        gate72(Offset(w * 0.74f, ground), w * 0.3f, h * 0.48f, seg(p, 0.15f, 0.7f), RamC.Gate, RamC.Paradise, jo, t)
        tagAt(a.tag("jannahopen"), Offset(w * 0.74f, ground + h * 0.08f), jo * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (ns > 0f) {
        val p = a.b("narshut")
        gate72(Offset(w * 0.26f, ground), w * 0.3f, h * 0.48f, 1f - seg(p, 0.1f, 0.6f), RamC.Gate, RamC.Fire, ns, t)
        tagAt(a.tag("narshut"), Offset(w * 0.26f, ground + h * 0.08f), ns * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (cs > 0f) {
        val p = a.b("chainedsmoke")
        val c = Offset(w * 0.5f, h * 0.3f)
        val g = seg(p, 0f, 0.3f)
        for (k in 0 until 9) {
            val ang = k * 0.7f + t * 0.6f
            val r = w * (0.03f + 0.015f * (k % 3))
            drawCircle(RamC.Smoke, r, c + Offset(sin(ang) * w * 0.05f, sin(ang * 1.3f + 1f) * h * 0.035f), alpha = cs * g * 0.85f)
        }
        val bind = easeOut(seg(p, 0.25f, 0.6f))
        chain72(Offset(w * 0.08f, h * 0.18f), Offset(w * 0.08f, h * 0.18f) + (c - Offset(w * 0.08f, h * 0.18f)) * bind, w * 0.014f, RamC.Chain, cs)
        chain72(Offset(w * 0.92f, h * 0.18f), Offset(w * 0.92f, h * 0.18f) + (c - Offset(w * 0.92f, h * 0.18f)) * bind, w * 0.014f, RamC.Chain, cs)
        tagAt(a.tag("chainedsmoke"), Offset(w * 0.5f, h * 0.45f), cs * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (fp > 0f) {
        val p = a.b("forgivenpast")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.75f * fp)
        val slate = Offset(w * 0.5f, h * 0.5f)
        val sw = w * 0.72f
        val sh = h * 0.24f
        drawRoundRect(RamC.Cell, Offset(slate.x - sw / 2f, slate.y - sh / 2f), Size(sw, sh), CornerRadius(sh * 0.12f), alpha = fp)
        val wipe = easeInOut(seg(p, 0.35f, 0.8f))
        val wx = slate.x + sw / 2f - sw * wipe
        for (k in 0 until 14) {
            val x = slate.x - sw * 0.44f + sw * 0.88f * fract(k * 0.618f)
            val y = slate.y - sh * 0.35f + sh * 0.7f * fract(k * 0.381f + 0.1f)
            if (x < wx) drawCircle(RamC.Dark, w * 0.014f, Offset(x, y), alpha = fp)
        }
        if (wipe > 0f && wipe < 1f) softGlow(Offset(wx, slate.y), sh * 0.6f, RamC.Lit, fp * 0.8f)
        drawRoundRect(RamC.Rim, Offset(slate.x - sw / 2f, slate.y - sh / 2f), Size(sw, sh), CornerRadius(sh * 0.12f), alpha = fp, style = Stroke(1.5f * dp))
        tagAt(a.tag("forgivenpast", 0, 0), Offset(w * 0.5f, h * 0.25f), fp * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("forgivenpast", 0, 1), Offset(w * 0.5f, h * 0.75f), fp * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Jade)
    }
    if (ts > 0f) {
        val p = a.b("threespans")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.75f * ts)
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.3f * k, 0.2f + 0.3f * k)
            val clean = seg(p, 0.15f + 0.3f * k, 0.32f + 0.3f * k)
            val y = h * (0.3f + 0.22f * k)
            val x0 = w * 0.9f
            val x1 = w * 0.1f
            drawLine(RamC.Rim, Offset(x0, y), Offset(x1, y), 2f * dp, alpha = ts * g)
            val ends = when (k) { 0 -> 5; 1 -> 2; else -> 2 }
            for (e in 0 until ends) {
                val x = mix(x0, x1, if (ends == 1) 0f else e / (ends - 1f))
                if (k == 2) crescent12(Offset(x, y - h * 0.03f), w * 0.025f, RamC.Lit, RamC.Top, ts * g)
                else drawCircle(RamC.Lit, (if (k == 0) 4f else 6f) * dp, Offset(x, y), alpha = ts * g)
            }
            for (d in 0 until 6) {
                val x = mix(x0, x1, (d + 0.5f) / 6f)
                drawCircle(RamC.Dark, w * 0.012f, Offset(x, y + h * 0.035f), alpha = ts * g * (1f - clean))
                glint(Offset(x, y + h * 0.035f), w * 0.016f, RamC.Lit, ts * bump(clean))
            }
            tagAt(a.tag("threespans", 0, k), Offset(w * 0.5f, y - h * 0.07f), ts * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 2. ram-qudsi: «الصوم لي وأنا أجزي به» ═════════════

/**
 * Beats: beyondcount (a measuring glass fills with gold past its marks and keeps overflowing: «بِغَيْرِ حِسَابٍ»), secretheart (a house shut
 * for the night; a heart glows in its window and a thread of light goes straight up from it: «سِرٌّ بَيْنَ الْعَبْدِ وَرَبِّهِ»),
 * muskscent (a phial of musk and its curling scent: «أَطْيَبُ عِنْدَ اللَّهِ مِنْ رِيحِ الْمِسْكِ»), fastshield (a dome of light over one who
 * fasts; sparks glance off it: «جُنَّةٌ»), twojoys (a bowl of dates at sunset, then a gate of light: «فَرْحَةٌ عِنْدَ فِطْرِهِ», «وَفَرْحَةٌ
 * عِنْدَ لِقَاءِ رَبِّهِ»), rayyangate (a gate with water running under it: «الرَّيَّانُ»).
 */
internal fun DrawScope.fastsecretScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "beyondcount" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "secretheart" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "muskscent" to Shot(Offset(w * 0.5f, h * 0.52f), 1.1f),
        "fastshield" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "twojoys" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "rayyangate" to Shot(Offset(w * 0.5f, h * 0.55f), 1.08f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { fastSecretDraw(a) }
}

private fun DrawScope.fastSecretDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    ramNight72(t, ground, moon = 0.8f)
    val bc = fqIn(a, "beyondcount", "secretheart")
    val sh = fqIn(a, "secretheart", "muskscent")
    val ms = fqIn(a, "muskscent", "fastshield")
    val fs = fqIn(a, "fastshield", "twojoys")
    val tj = fqIn(a, "twojoys", "rayyangate")
    val rg = fqIn(a, "rayyangate")
    if (bc > 0f) {
        val p = a.b("beyondcount")
        val c = Offset(w * 0.5f, h * 0.55f)
        val gw = w * 0.26f
        val gh = h * 0.4f
        val fill = easeOut(seg(p, 0.05f, 0.5f))
        drawRect(RamC.Lit, Offset(c.x - gw / 2f, c.y + gh / 2f - gh * fill), Size(gw, gh * fill), alpha = bc * 0.75f)
        drawRect(Color.White, Offset(c.x - gw / 2f, c.y - gh / 2f), Size(gw, gh), alpha = bc * 0.1f)
        for (k in 1 until 5) drawLine(RamC.Rim, Offset(c.x + gw / 2f - gw * 0.25f, c.y + gh / 2f - gh * k / 5f), Offset(c.x + gw / 2f, c.y + gh / 2f - gh * k / 5f), 1.5f * dp, alpha = bc)
        drawRect(RamC.Rim, Offset(c.x - gw / 2f, c.y - gh / 2f), Size(gw, gh), alpha = bc, style = Stroke(2f * dp))
        val over = seg(p, 0.45f, 0.6f)
        for (k in 0 until 18) {
            val u = fract(t * 0.5f + k / 18f)
            val x = c.x + (fract(k * 0.618f) - 0.5f) * gw * (1f + 2.2f * u)
            drawCircle(RamC.Lit, w * 0.008f, Offset(x, c.y - gh / 2f - u * h * 0.32f), alpha = bc * over * (1f - u))
        }
        tagAt(a.tag("beyondcount"), Offset(w * 0.5f, h * 0.9f), bc * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sh > 0f) {
        val p = a.b("secretheart")
        val foot = Offset(w * 0.5f, ground)
        house12(foot, w * 0.34f, Color(0xFFCDBFA6), Color(0xFF6A5A48), sh, lit = 0.2f)
        val win = foot + Offset(0f, -w * 0.2f)
        softGlow(win, w * 0.07f, RamC.Lit, sh * seg(p, 0.15f, 0.35f))
        heart12(win, w * 0.045f, RamC.Lit, sh * seg(p, 0.15f, 0.35f), fill = true)
        val up = easeOut(seg(p, 0.35f, 0.75f))
        drawLine(RamC.Lit, win + Offset(0f, -w * 0.05f), Offset(win.x, mix(win.y - w * 0.05f, 0f, up)), 2f * dp, StrokeCap.Round, alpha = sh * 0.8f)
        tagAt(a.tag("secretheart"), Offset(w * 0.5f, ground + h * 0.09f), sh * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ms > 0f) {
        val p = a.b("muskscent")
        val foot = Offset(w * 0.5f, h * 0.68f)
        softGlow(foot + Offset(0f, -h * 0.12f), w * 0.2f, Color(0xFFB8A0D8), 0.35f * ms)
        fqPhial(foot, h * 0.3f, Color(0xFFB8A0D8), ms * seg(p, 0f, 0.2f), t)
        tagAt(a.tag("muskscent"), Offset(w * 0.5f, h * 0.83f), ms * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fs > 0f) {
        val p = a.b("fastshield")
        val seat = Offset(w * 0.5f, ground)
        sit12(seat, h * 0.22f, RamC.Person, fs, 1f)
        guardDome12(seat + Offset(0f, -h * 0.02f), w * 0.26f, seg(p, 0.1f, 0.45f), t, fs)
        for (k in 0 until 6) {
            val u = fract(t * 0.6f + k / 6f)
            val ang = PI.toFloat() * (0.15f + 0.7f * fract(k * 0.618f))
            val dir = Offset(-kotlin.math.cos(ang), -sin(ang))
            val r = w * 0.26f * (1.9f - 0.9f * bump(u).coerceAtLeast(0f))
            drawCircle(RamC.Fire, w * 0.009f, seat + dir * r, alpha = fs * seg(p, 0.4f, 0.6f) * (1f - u))
        }
        tagAt(a.tag("fastshield"), Offset(w * 0.5f, h * 0.15f), fs * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tj > 0f) {
        val p = a.b("twojoys")
        val left = seg(p, 0.05f, 0.25f)
        dateBowl72(Offset(w * 0.72f, ground - h * 0.05f), w * 0.26f, tj * left)
        cup12(Offset(w * 0.88f, ground - h * 0.03f), w * 0.07f, RamC.Rim, tj * left, RamC.Rayyan)
        softGlow(Offset(w * 0.76f, ground - h * 0.12f), w * 0.16f, RamC.Lit, 0.4f * tj * left)
        tagAt(a.tag("twojoys", 0, 0), Offset(w * 0.74f, h * 0.36f), tj * left, back = Fq.Back, ink = ArtInk.Gold)
        val right = seg(p, 0.5f, 0.7f)
        gate72(Offset(w * 0.26f, ground), w * 0.26f, h * 0.44f, right, RamC.Gate, RamC.Lit, tj * right, t)
        tagAt(a.tag("twojoys", 0, 1), Offset(w * 0.28f, h * 0.24f), tj * right, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (rg > 0f) {
        val p = a.b("rayyangate")
        val sill = Offset(w * 0.5f, ground)
        gate72(sill, w * 0.4f, h * 0.55f, seg(p, 0.15f, 0.6f), RamC.Gate, RamC.Rayyan, rg, t)
        for (k in 0 until 5) {
            val y = ground + h * (0.03f + 0.035f * k)
            val ph = t * 2f + k
            drawLine(RamC.Rayyan, Offset(w * (0.3f + 0.04f * sin(ph)), y), Offset(w * (0.7f + 0.04f * sin(ph + 1f)), y), 2f * dp, StrokeCap.Round, alpha = rg * seg(p, 0.3f, 0.6f) * (1f - k * 0.15f))
        }
        tagAt(a.tag("rayyangate"), Offset(w * 0.5f, h * 0.14f), rg * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Jade)
    }
}

// ═════════════ 3. ram-hilal: the new moon ═════════════

/**
 * Beats: year2 (ten years after the hijra as ten dots; the second lights with a small crescent: «فِي السَّنَةِ الثَّانِيَةِ مِنَ الْهِجْرَةِ»),
 * sighting (at dusk one on a hill looks west where a thin crescent hangs low: «رُؤْيَةُ هِلَالِهِ»), thirtydays (or thirty days of Shaʿbān
 * fill a calendar: «إِكْمَالُ شَعْبَانَ ثَلَاثِينَ يَوْمًا»), onewitness (one who saw it speaks, and the houses light one after another: «وَجَبَ
 * الصَّوْمُ عَلَى الْجَمِيعِ»), monthlength (two rows of days, twenty-nine and thirty: «ثَلَاثِينَ يَوْمًا», «تِسْعَةٍ وَعِشْرِينَ»).
 */
internal fun DrawScope.hilalwatchScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "year2" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sighting" to Shot(Offset(w * 0.4f, h * 0.55f), 1.1f),
        "thirtydays" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "onewitness" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "monthlength" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { hilalWatchDraw(a) }
}

private fun DrawScope.hilalWatchDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    nightSky(t, RamC.Top, Color(0xFFC88A6A), ground, starAlpha = 0.6f)
    hills(Color(0xFF2A2230), ground, h * 0.06f, 2.1f, 4.3f, 0.6f)
    drawRect(RamC.Ground, Offset(0f, ground), Size(w, h - ground))
    val y2 = fqIn(a, "year2", "sighting")
    val sg = fqIn(a, "sighting", "thirtydays")
    val td = fqIn(a, "thirtydays", "onewitness")
    val ow = fqIn(a, "onewitness", "monthlength")
    val ml = fqIn(a, "monthlength")
    if (y2 > 0f) {
        val p = a.b("year2")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.6f * y2)
        for (k in 0 until 10) {
            val g = seg(p, 0.03f * k, 0.03f * k + 0.1f)
            val c = Offset(w * (0.88f - 0.085f * k), h * 0.5f)
            drawCircle(if (k == 1) RamC.Lit else RamC.Rim, (if (k == 1) 7f else 4f) * dp, c, alpha = y2 * g * (if (k == 1) 1f else 0.6f))
        }
        val lit = seg(p, 0.35f, 0.55f)
        val two = Offset(w * 0.795f, h * 0.5f)
        softGlow(two, w * 0.06f, RamC.Lit, 0.6f * y2 * lit)
        crescent12(two + Offset(0f, -h * 0.07f), w * 0.03f, RamC.Lit, RamC.Top, y2 * lit)
        tagAt(a.tag("year2"), Offset(w * 0.5f, h * 0.68f), y2 * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sg > 0f) {
        val p = a.b("sighting")
        val moon = Offset(w * 0.18f, ground - h * 0.12f)
        val m = seg(p, 0.25f, 0.5f)
        softGlow(moon, w * 0.05f, RamC.Lit, 0.5f * sg * m)
        crescent12(moon, w * 0.022f, RamC.Lit, Color(0xFF8A6A7A), sg * m)
        figure(Offset(w * 0.66f, ground - h * 0.02f), h * 0.24f, RamC.Person, sg, arms = 0.9f * seg(p, 0.4f, 0.6f))
        tagAt(a.tag("sighting"), Offset(w * 0.4f, h * 0.2f), sg * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (td > 0f) {
        val p = a.b("thirtydays")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.6f * td)
        val cell = w * 0.1f
        val n = (seg(p, 0.05f, 0.8f) * 30f).toInt()
        fqCalendar(Offset(w * 0.5f - cell * 3.4f, h * 0.24f), 6, 30, cell, td, RamC.Rim) { i -> if (i < n) RamC.Cell else null }
        crescent12(Offset(w * 0.5f, h * 0.17f), w * 0.025f, RamC.Lit, RamC.Top, td * seg(p, 0.8f, 0.95f))
        tagAt(a.tag("thirtydays"), Offset(w * 0.5f, h * 0.9f), td * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ow > 0f) {
        val p = a.b("onewitness")
        val me = Offset(w * 0.18f, ground)
        figure(me, h * 0.24f, RamC.Person, ow, arms = 0.6f)
        voice12(me + Offset(w * 0.08f, -h * 0.2f), w * 0.08f, t, RamC.Lit, ow * seg(p, 0.1f, 0.25f), 1f)
        for (k in 0 until 4) {
            val foot = Offset(w * (0.42f + 0.16f * k), ground)
            house12(foot, w * 0.12f, Color(0xFFCDBFA6), Color(0xFF6A5A48), ow, lit = seg(p, 0.25f + 0.12f * k, 0.35f + 0.12f * k))
        }
        crescent12(Offset(w * 0.5f, h * 0.15f), w * 0.025f, RamC.Lit, RamC.Top, ow)
        tagAt(a.tag("onewitness"), Offset(w * 0.6f, h * 0.3f), ow * seg(p, 0.45f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ml > 0f) {
        val p = a.b("monthlength")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.6f * ml)
        val cell = w * 0.024f
        for (row in 0 until 2) {
            val n = if (row == 0) 30 else 29
            val g = seg(p, 0.05f + 0.4f * row, 0.35f + 0.4f * row)
            val y = h * (0.38f + 0.22f * row)
            fqCalendar(Offset(w * 0.5f - cell * 1.15f * 15f, y), 30, n, cell, ml * g, RamC.Rim) { i -> if (i.toFloat() < g * n) RamC.Lit.copy(alpha = 0.6f) else null }
            tagAt(a.tag("monthlength", 0, row), Offset(w * 0.5f, y - h * 0.06f), ml * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 4. ram-wujub: on whom the fast is due ═════════════

/**
 * Beats: fiveconds (one standing, and five ticks come down a list: «الْمُسْلِمِ», «الْبَالِغِ», «الْعَاقِلِ», «الْمُقِيمِ», «الْقَادِرِ»), childtrain
 * (a child climbs three low steps beside a grown one: «تَمْرِينًا لَهُ عَلَى الطَّاعَةِ»), feedperday (from each day of a row a bowl goes
 * down to a poor man's mat: «يُطْعِمُ عَنْ كُلِّ يَوْمٍ مِسْكِينًا»), newmuslim (a month of days, grey up to a bright mark and lit after it:
 * «لَمْ يَلْزَمْهُ قَضَاءُ مَا مَضَى»), liftedpen (an open book whose pen lifts away from it: «لِسُقُوطِ التَّكْلِيفِ عَنْهُمَا»).
 */
internal fun DrawScope.whomustfastScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fiveconds" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "childtrain" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "feedperday" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "newmuslim" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "liftedpen" to Shot(Offset(w * 0.5f, h * 0.5f), 1.08f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { whoMustFastDraw(a) }
}

private fun DrawScope.whoMustFastDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.82f
    ramNight72(t, ground, moon = 0.7f)
    val fc = fqIn(a, "fiveconds", "childtrain")
    val ct = fqIn(a, "childtrain", "feedperday")
    val fd = fqIn(a, "feedperday", "newmuslim")
    val nm = fqIn(a, "newmuslim", "liftedpen")
    val lp = fqIn(a, "liftedpen")
    if (fc > 0f) {
        val p = a.b("fiveconds")
        figure(Offset(w * 0.2f, ground), h * 0.34f, RamC.Person, fc)
        for (k in 0 until 5) {
            val g = seg(p, 0.08f + 0.17f * k, 0.2f + 0.17f * k)
            val y = h * (0.16f + 0.14f * k)
            tick14(Offset(w * 0.88f, y), w * 0.03f, g)
            tagAt(a.tag("fiveconds", 0, k), Offset(w * 0.64f, y), fc * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (ct > 0f) {
        val p = a.b("childtrain")
        for (k in 0 until 3) {
            val top = ground - h * 0.05f * (k + 1)
            drawRect(Color(0xFF5A5268), Offset(w * (0.62f - 0.14f * k), top), Size(w * 0.14f, ground - top), alpha = ct)
        }
        val step = (seg(p, 0.1f, 0.85f) * 3f).coerceAtMost(2.999f)
        val k = step.toInt()
        val x = w * (0.69f - 0.14f * k) - w * 0.14f * fract(step) * 0.3f
        child12(Offset(x, ground - h * 0.05f * (k + 1)), h * 0.26f, RamC.Person, ct, walk = t * 5f)
        figure(Offset(w * 0.85f, ground), h * 0.32f, RamC.Person, ct, arms = 0.5f)
        crescent12(Offset(w * 0.3f, h * 0.2f), w * 0.03f, RamC.Lit, RamC.Top, ct * seg(p, 0.6f, 0.8f))
        tagAt(a.tag("childtrain"), Offset(w * 0.45f, h * 0.36f), ct * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fd > 0f) {
        val p = a.b("feedperday")
        val cell = w * 0.1f
        val tl = Offset(w * 0.5f - cell * 1.15f * 3f, h * 0.14f)
        fqCalendar(tl, 6, 6, cell, fd, RamC.Rim) { RamC.Cell }
        val mat = Offset(w * 0.5f, ground)
        drawRect(Color(0xFF7A5A4A), Offset(mat.x - w * 0.3f, mat.y - h * 0.015f), Size(w * 0.6f, h * 0.03f), alpha = fd)
        sit12(mat + Offset(w * 0.2f, -h * 0.015f), h * 0.22f, RamC.Person, fd, -1f)
        for (k in 0 until 6) {
            val u = seg(p, 0.1f + 0.12f * k, 0.25f + 0.12f * k)
            val from = tl + Offset((5 - k) * cell * 1.15f + cell / 2f, cell)
            val to = Offset(mat.x + w * 0.1f - k * w * 0.07f, mat.y - h * 0.02f)
            val c = from + (to - from) * easeInOut(u)
            if (u > 0f) drawArc(RamC.Bowl, 0f, 180f, true, Offset(c.x - w * 0.03f, c.y - h * 0.02f), Size(w * 0.06f, h * 0.035f), alpha = fd)
        }
        tagAt(a.tag("feedperday"), Offset(w * 0.5f, h * 0.48f), fd * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nm > 0f) {
        val p = a.b("newmuslim")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.6f * nm)
        val cell = w * 0.1f
        val mark = 12
        fqCalendar(Offset(w * 0.5f - cell * 3.4f, h * 0.24f), 6, 30, cell, nm, RamC.Rim) { i ->
            if (i < mark) Color(0xFF2A2E40) else if (seg(p, 0.35f, 0.9f) * 18f > i - mark) RamC.Lit.copy(alpha = 0.7f) else null
        }
        val mc = Offset(w * 0.5f - cell * 3.4f + (5 - mark % 6) * cell * 1.15f, h * 0.24f + (mark / 6) * cell * 1.15f)
        softGlow(mc + Offset(cell / 2f, cell / 2f), cell, RamC.Lit, nm * seg(p, 0.2f, 0.35f))
        tagAt(a.tag("newmuslim"), Offset(w * 0.5f, h * 0.9f), nm * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (lp > 0f) {
        val p = a.b("liftedpen")
        val c = Offset(w * 0.5f, h * 0.6f)
        openBook21(c, w * 0.6f, Color(0xFFF2E8CE), Color(0xFFB8A888), lp)
        val up = easeOut(seg(p, 0.2f, 0.7f))
        quill72(c + Offset(w * 0.06f, -h * 0.02f - h * 0.3f * up), h * 0.2f, Color(0xFFE8E0D0), lp)
        tagAt(a.tag("liftedpen"), Offset(w * 0.5f, h * 0.88f), lp * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 5. ram-safar: the traveller and the sick ═════════════

/**
 * Beats: travelchoice (a traveller reaches a fork; both roads are lit, one to the crescent of fasting, one to a cup: «الصِّيَامِ», «وَالْفِطْرِ»),
 * easierway (a steep zigzag and a gentle road; the gentle one lights: «الْأَسْهَلَ عَلَيْهِ»), sickgauge (one lying ill and a dial above him
 * whose needle moves from green to amber to red: «إِنْ شَقَّ عَلَيْهِ أَفْطَرَ», «وَإِنْ ضَرَّهُ وَجَبَ عَلَيْهِ الْفِطْرُ»), noblame (two plain
 * roundels on the road, a cup over one and a crescent over the other, joined by a warm line: «فَلَمْ يَعِبِ الصَّائِمُ عَلَى الْمُفْطِرِ»),
 * medicaldoctor (a green cross, and missed days moving later on the calendar: «ثُمَّ يَقْضِي إِذَا زَالَ الْخَطَرُ»).
 */
internal fun DrawScope.travelersickScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "travelchoice" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "easierway" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "sickgauge" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "nofaultfound" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "medicaldoctor" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { travelerSickDraw(a) }
}

private fun DrawScope.travelerSickDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.45f
    nightSky(t, RamC.Top, Color(0xFF6A5A7A), ground, starAlpha = 0.6f)
    drawRect(Color(0xFF2A2430), Offset(0f, ground), Size(w, h - ground))
    val tc = fqIn(a, "travelchoice", "easierway")
    val ew = fqIn(a, "easierway", "sickgauge")
    val sk = fqIn(a, "sickgauge", "nofaultfound")
    val nb = fqIn(a, "nofaultfound", "medicaldoctor")
    val md = fqIn(a, "medicaldoctor")
    val road = Color(0xFF4A3E32)
    if (tc > 0f) {
        val p = a.b("travelchoice")
        val fork = Offset(w * 0.5f, h * 0.66f)
        fqRoad(Offset(w * 0.5f, h * 1.02f), fork, w * 0.12f, road, tc)
        val ends = listOf(Offset(w * 0.8f, ground + h * 0.02f), Offset(w * 0.2f, ground + h * 0.02f))
        for (k in 0 until 2) {
            val g = seg(p, 0.3f + 0.2f * k, 0.45f + 0.2f * k)
            fqRoad(fork, fork + (ends[k] - fork) * easeOut(g), w * 0.07f, road, tc)
            softGlow(ends[k] + Offset(0f, -h * 0.08f), w * 0.07f, RamC.Lit, 0.5f * tc * g)
            if (k == 0) crescent12(ends[k] + Offset(0f, -h * 0.08f), w * 0.03f, RamC.Lit, RamC.Top, tc * g)
            else cup12(ends[k] + Offset(0f, -h * 0.03f), w * 0.07f, RamC.Rim, tc * g, RamC.Rayyan)
            tagAt(a.tag("travelchoice", 0, k), ends[k] + Offset(0f, -h * 0.18f), tc * g, back = Fq.Back, ink = ArtInk.Gold)
        }
        val u = easeOut(seg(p, 0f, 0.35f))
        val foot = Offset(w * 0.5f, mix(h * 0.98f, fork.y + h * 0.04f, u))
        figure(foot, h * 0.2f, RamC.Person, tc, walk = if (u < 1f) t * 6f else 0f)
        fqSack(foot + Offset(h * 0.035f, -h * 0.09f), h * 0.06f, Fq.SandDark, tc)
    }
    if (ew > 0f) {
        val p = a.b("easierway")
        val start = Offset(w * 0.5f, h * 0.95f)
        val steep = Path().apply {
            moveTo(start.x, start.y)
            for (k in 1..6) lineTo(w * (0.5f + (if (k % 2 == 0) 0.06f else 0.32f)), start.y - h * 0.08f * k)
        }
        drawPath(steep, road, alpha = ew * (1f - 0.5f * seg(p, 0.4f, 0.6f)), style = Stroke(w * 0.035f, cap = StrokeCap.Round))
        val gentle = Path().apply { moveTo(start.x, start.y); quadraticTo(w * 0.05f, h * 0.8f, w * 0.3f, h * 0.5f) }
        drawPath(gentle, road, alpha = ew, style = Stroke(w * 0.05f, cap = StrokeCap.Round))
        drawPath(gentle, RamC.Lit, alpha = ew * seg(p, 0.4f, 0.6f) * 0.7f, style = Stroke(w * 0.012f, cap = StrokeCap.Round))
        tagAt(a.tag("easierway"), Offset(w * 0.3f, h * 0.42f), ew * seg(p, 0.45f, 0.65f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sk > 0f) {
        val p = a.b("sickgauge")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.6f * sk)
        val bed = Offset(w * 0.5f, h * 0.82f)
        drawRoundRect(Color(0xFF6A5A4A), Offset(bed.x - w * 0.36f, bed.y - h * 0.06f), Size(w * 0.72f, h * 0.06f), CornerRadius(h * 0.01f), alpha = sk)
        drawRoundRect(Color(0xFFDAD2C2), Offset(bed.x - w * 0.3f, bed.y - h * 0.11f), Size(w * 0.56f, h * 0.06f), CornerRadius(h * 0.03f), alpha = sk)
        drawCircle(RamC.Person, h * 0.035f, Offset(bed.x + w * 0.3f, bed.y - h * 0.1f), alpha = sk)
        val dial = Offset(w * 0.5f, h * 0.5f)
        val r = w * 0.26f
        val cols = listOf(Color(0xFF4E9A6A), Color(0xFFE0A040), Color(0xFFC0503A))
        for (k in 0 until 3) drawArc(cols[k], 180f + 60f * k, 58f, false, Offset(dial.x - r, dial.y - r), Size(r * 2f, r * 2f), alpha = sk, style = Stroke(w * 0.04f))
        val needle = 0.17f + 0.33f * easeInOut(seg(p, 0.15f, 0.4f)) + 0.33f * easeInOut(seg(p, 0.55f, 0.8f))
        val ang = PI.toFloat() * (1f + needle)
        drawLine(RamC.Person, dial, dial + Offset(kotlin.math.cos(ang), sin(ang)) * (r * 0.85f), 3f * dp, StrokeCap.Round, alpha = sk)
        drawCircle(RamC.Person, 5f * dp, dial, alpha = sk)
        tagAt(a.tag("sickgauge", 0, 0), Offset(w * 0.5f, h * 0.16f), sk * seg(p, 0.3f, 0.45f) * (1f - seg(p, 0.6f, 0.7f)), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("sickgauge", 0, 1), Offset(w * 0.5f, h * 0.16f), sk * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (nb > 0f) {
        val p = a.b("nofaultfound")
        fqRoad(Offset(w * 1.05f, h * 0.7f), Offset(-w * 0.05f, h * 0.7f), h * 0.06f, road, nb)
        val r = w * 0.08f
        val c1 = Offset(w * 0.68f, h * 0.6f)
        val c2 = Offset(w * 0.32f, h * 0.6f)
        nameRoundel(c1, r, null, Roundel.PlainFill, Roundel.PlainRim, nb * seg(p, 0f, 0.2f))
        nameRoundel(c2, r, null, Roundel.PlainFill, Roundel.PlainRim, nb * seg(p, 0.05f, 0.25f))
        crescent12(c1 + Offset(0f, -r * 2f), w * 0.025f, RamC.Lit, RamC.Top, nb * seg(p, 0.2f, 0.35f))
        cup12(c2 + Offset(0f, -r * 1.5f), w * 0.06f, RamC.Rim, nb * seg(p, 0.2f, 0.35f), RamC.Rayyan)
        val link = seg(p, 0.35f, 0.6f)
        drawLine(RamC.Lit, c1 - Offset(r, 0f), c1 - Offset(r, 0f) + (c2 + Offset(r, 0f) - (c1 - Offset(r, 0f))) * link, 2.5f * dp, StrokeCap.Round, alpha = nb * 0.8f)
        tagAt(a.tag("nofaultfound"), Offset(w * 0.5f, h * 0.88f), nb * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (md > 0f) {
        val p = a.b("medicaldoctor")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.6f * md)
        val cx = Offset(w * 0.2f, h * 0.3f)
        val arm = w * 0.06f
        drawRect(Color(0xFF4E9A6A), Offset(cx.x - arm * 0.35f, cx.y - arm), Size(arm * 0.7f, arm * 2f), alpha = md)
        drawRect(Color(0xFF4E9A6A), Offset(cx.x - arm, cx.y - arm * 0.35f), Size(arm * 2f, arm * 0.7f), alpha = md)
        val cell = w * 0.08f
        val tl = Offset(w * 0.42f, h * 0.2f)
        val move = seg(p, 0.35f, 0.75f)
        fqCalendar(tl, 6, 18, cell * 0.8f, md, RamC.Rim) { i ->
            when {
                i in 2..4 -> RamC.Dark.copy(alpha = 1f - move)
                i in 13..15 -> RamC.Lit.copy(alpha = move * 0.8f)
                else -> null
            }
        }
        tagAt(a.tag("medicaldoctor"), Offset(w * 0.5f, h * 0.75f), md * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 6. ram-hayd: days excused and made up ═════════════

/**
 * Beats: makeupdays (a month of days with a few in a muted rose; they move across to the next month and turn gold: «يَجِبُ عَلَيْهِمَا
 * قَضَاءُ مَا فَاتَهُمَا»), nursingfear (a cradle under a dome of care, and the days it costs set later: «وَقَضَتْ حِينَ يَزُولُ عَنْهَا
 * الْخَوْفُ»), beforenext (between two crescents a year runs; the make-up days sit early, well before a bar at the second: «وَلَا يَجُوزُ
 * تَأْخِيرُهُ إِلَى رَمَضَانَ الثَّانِي بِدُونِ عُذْرٍ»), rescuer (one on the shore reaches into the waves and draws another out: «لِإِنْقَاذِ
 * مَعْصُومٍ مِنْ غَرَقٍ أَوْ حَرِيقٍ»). Women are never drawn here: only their days.
 */
internal fun DrawScope.excusedaysScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "makeupdays" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "nursingfear" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "beforenext" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "rescuer" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { excuseDaysDraw(a) }
}

private fun DrawScope.excuseDaysDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.82f
    ramNight72(t, ground, moon = 0.6f)
    val mu = fqIn(a, "makeupdays", "nursingfear")
    val nf = fqIn(a, "nursingfear", "beforenext")
    val bn = fqIn(a, "beforenext", "rescuer")
    val rs = fqIn(a, "rescuer")
    val cell = w * 0.055f
    val excused = setOf(7, 8, 9, 10, 11, 12)
    if (mu > 0f) {
        val p = a.b("makeupdays")
        val right = Offset(w * 0.53f, h * 0.3f)
        val left = Offset(w * 0.06f, h * 0.3f)
        val move = easeInOut(seg(p, 0.35f, 0.75f))
        fqCalendar(right, 6, 30, cell, mu, RamC.Rim) { i -> if (i in excused) RamC.Rose.copy(alpha = 1f - 0.7f * move) else RamC.Cell }
        fqCalendar(left, 6, 30, cell, mu * seg(p, 0.2f, 0.35f), RamC.Rim) { i -> if (i < 6 && move >= 1f) RamC.Lit.copy(alpha = 0.8f) else null }
        if (move > 0f && move < 1f) for ((j, i) in excused.withIndex()) {
            val from = right + Offset((5 - i % 6) * cell * 1.15f, (i / 6) * cell * 1.15f)
            val to = left + Offset((5 - j % 6) * cell * 1.15f, 0f)
            val c = from + (to - from) * move
            drawRoundRect(RamC.Lit, c, Size(cell, cell), CornerRadius(cell * 0.2f), alpha = mu * 0.8f)
        }
        crescent12(Offset(right.x + cell * 3.4f, h * 0.22f), w * 0.02f, RamC.Lit, RamC.Top, mu)
        tagAt(a.tag("makeupdays"), Offset(w * 0.5f, h * 0.84f), mu * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nf > 0f) {
        val p = a.b("nursingfear")
        val c = Offset(w * 0.5f, h * 0.62f)
        drawArc(Color(0xFFB08A6A), 0f, 180f, true, Offset(c.x - w * 0.16f, c.y - h * 0.08f), Size(w * 0.32f, h * 0.16f), alpha = nf)
        drawCircle(Color(0xFFF0E4D0), w * 0.035f, c + Offset(w * 0.07f, -h * 0.02f), alpha = nf)
        drawRoundRect(Color(0xFFD8C4E8), Offset(c.x - w * 0.12f, c.y - h * 0.035f), Size(w * 0.17f, h * 0.05f), CornerRadius(h * 0.02f), alpha = nf)
        for (k in 0 until 2) drawLine(Color(0xFF8A6A4A), c + Offset(w * (-0.12f + 0.24f * k), h * 0.07f), c + Offset(w * (-0.16f + 0.32f * k), h * 0.13f), 3f * dp, StrokeCap.Round, alpha = nf)
        guardDome12(c + Offset(0f, h * 0.02f), w * 0.26f, seg(p, 0.1f, 0.4f), t, nf, Color(0xFFF2D8A0))
        tagAt(a.tag("nursingfear"), Offset(w * 0.5f, h * 0.2f), nf * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (bn > 0f) {
        val p = a.b("beforenext")
        drawRect(RamC.Top, Offset.Zero, size, alpha = 0.6f * bn)
        val y = h * 0.55f
        val x0 = w * 0.88f
        val x1 = w * 0.12f
        val run = easeOut(seg(p, 0f, 0.3f))
        drawLine(RamC.Rim, Offset(x0, y), Offset(mix(x0, x1, run), y), 2f * dp, alpha = bn)
        crescent12(Offset(x0, y - h * 0.06f), w * 0.03f, RamC.Lit, RamC.Top, bn)
        crescent12(Offset(x1, y - h * 0.06f), w * 0.03f, RamC.Lit, RamC.Top, bn * seg(p, 0.25f, 0.35f))
        for (k in 0 until 6) {
            val g = seg(p, 0.35f + 0.05f * k, 0.45f + 0.05f * k)
            drawRoundRect(RamC.Lit, Offset(mix(x0, x1, 0.12f + 0.045f * k) - cell * 0.4f, y + h * 0.03f), Size(cell * 0.8f, cell * 0.8f), CornerRadius(cell * 0.15f), alpha = bn * g * 0.85f)
        }
        val bar = seg(p, 0.6f, 0.75f)
        drawLine(Color(0xFFC0503A), Offset(x1 + w * 0.06f, y - h * 0.12f), Offset(x1 + w * 0.06f, y + h * 0.1f), 4f * dp, StrokeCap.Round, alpha = bn * bar)
        tagAt(a.tag("beforenext"), Offset(w * 0.5f, h * 0.3f), bn * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (rs > 0f) {
        val p = a.b("rescuer")
        val shore = Offset(w * 0.72f, ground)
        drawRect(Color(0xFF1E4A6A), Offset(0f, ground - h * 0.06f), Size(w * 0.58f, h - ground + h * 0.06f), alpha = rs)
        for (k in 0 until 4) {
            val yy = ground - h * 0.04f + k * h * 0.04f
            val path = Path().apply {
                moveTo(0f, yy)
                var x = 0f
                while (x < w * 0.58f) { quadraticTo(x + w * 0.04f, yy - h * 0.015f * sin(t * 2f + k + x / w * 9f), x + w * 0.08f, yy); x += w * 0.08f }
            }
            drawPath(path, Color(0xFF7CC4E8), alpha = rs * 0.6f, style = Stroke(1.5f * dp))
        }
        val pull = easeInOut(seg(p, 0.3f, 0.8f))
        figure(shore, h * 0.28f, RamC.Person, rs, lean = 0.35f, arms = 1f)
        val head = Offset(mix(w * 0.36f, w * 0.56f, pull), mix(ground - h * 0.01f, ground - h * 0.08f, pull))
        drawCircle(RamC.Person, h * 0.028f, head, alpha = rs)
        drawLine(RamC.Person, head + Offset(h * 0.02f, h * 0.02f), shore + Offset(-h * 0.14f, -h * 0.2f), 3f * dp, StrokeCap.Round, alpha = rs * seg(p, 0.15f, 0.3f))
        tagAt(a.tag("rescuer"), Offset(w * 0.5f, h * 0.2f), rs * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
