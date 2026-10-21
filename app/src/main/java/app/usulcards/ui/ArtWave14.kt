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
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextLayoutResult
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 14, round 7 of the redraw (2026-10-21): «الإيمان بالقدر». The five decrees as the nested rings of a brass
 * astrolabe on a desk by day; the two extremes on qadar (a puppet on strings, a man carving himself out of stone)
 * and the middle way (a man who truly sows, and the growth is not his); Adam and Musa as two small lights; and two
 * men who try to prise open a sealed chest, then sit down and leave it.
 */

// ───────────── shared ─────────────

/** A faceless figure posed by its joints: [hip], [neck] (the top of the body), and hands and feet anywhere. */
private fun DrawScope.posed14(hip: Offset, neck: Offset, handA: Offset, handB: Offset, footA: Offset, footB: Offset, tall: Float, color: Color, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    val sw = max(1.7f * density, tall * 0.065f)
    drawLine(color, hip, footA, sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, footB, sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, neck, sw, StrokeCap.Round, alpha = alpha)
    val d = neck - hip
    val len = max(1f, d.getDistance())
    drawCircle(color, tall * 0.1f, neck + d / len * (tall * 0.15f), alpha = alpha)
    drawLine(color, neck, handA, sw * 0.9f, StrokeCap.Round, alpha = alpha)
    drawLine(color, neck, handB, sw * 0.9f, StrokeCap.Round, alpha = alpha)
}

/** A prophet other than him ﷺ: a smaller, whiter light, always with his name. */
private fun DrawScope.nabiLight14(c: Offset, r: Float, strength: Float, t: Float, name: TextLayoutResult?, nameAt: Offset) {
    if (strength <= 0.01f) return
    val s = strength.coerceIn(0f, 1.4f)
    val col = Color(0xFFFFF4DA)
    drawCircle(Brush.radialGradient(listOf(col.copy(alpha = 0.2f * min(1f, s)), Color.Transparent), c, r * 8f), r * 8f, c)
    lightShafts(c, t * 0.06f, 2f * PI.toFloat(), r * 5.5f, col, 0.55f * min(1f, s), t, 8)
    bloom(c, r * 1.3f * s, col, min(1f, s))
    tagAt(name, nameAt, min(1f, s), back = Color(0xE6141826), ink = ArtInk.Ivory)
}

/** A green tick that draws itself (0..1). */
private fun DrawScope.tick14(c: Offset, r: Float, p: Float, color: Color = Color(0xFF2E9A55)) {
    if (p <= 0f) return
    val a = c + Offset(-r, 0f)
    val b = c + Offset(-r * 0.25f, r * 0.7f)
    val e = c + Offset(r, -r * 0.8f)
    val p1 = seg(p, 0f, 0.4f)
    val p2 = seg(p, 0.4f, 1f)
    drawCircle(Color.White, r * 1.5f, c, alpha = 0.85f * seg(p, 0f, 0.2f))
    drawLine(color, a, a + (b - a) * p1, r * 0.32f, StrokeCap.Round)
    if (p2 > 0f) drawLine(color, b, b + (e - b) * p2, r * 0.32f, StrokeCap.Round)
}

/** A red cross that draws itself (0..1). */
private fun DrawScope.cross14(c: Offset, r: Float, p: Float, color: Color = Color(0xFFD23C30)) {
    if (p <= 0f) return
    val p1 = seg(p, 0f, 0.5f)
    val p2 = seg(p, 0.5f, 1f)
    drawCircle(Color.White, r * 1.5f, c, alpha = 0.85f * seg(p, 0f, 0.2f))
    drawLine(color, c + Offset(-r, -r), c + Offset(-r + 2 * r * p1, -r + 2 * r * p1), r * 0.32f, StrokeCap.Round)
    if (p2 > 0f) drawLine(color, c + Offset(r, -r), c + Offset(r - 2 * r * p2, -r + 2 * r * p2), r * 0.32f, StrokeCap.Round)
}

/** A scroll at [c], [open] 0..1 unrolled downwards, with ruled lines (never imitation script) and a red seal. */
private fun DrawScope.scroll14(c: Offset, wd: Float, ht: Float, open: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val dp = density
    val o = easeOut(open)
    val top = c.y - ht / 2
    val bottom = top + ht * max(0.08f, o)
    drawRect(Color.Black, topLeft = Offset(c.x - wd / 2 + 2f * dp, top + 3f * dp), size = Size(wd, bottom - top), alpha = 0.25f * alpha)
    drawRect(Color(0xFFF6EBCF), topLeft = Offset(c.x - wd / 2, top), size = Size(wd, bottom - top), alpha = alpha)
    val lines = 5
    for (k in 0 until lines) {
        val y = top + ht * (0.2f + 0.13f * k)
        if (y > bottom - ht * 0.06f) break
        val inset = if (k == 0) wd * 0.28f else wd * 0.14f
        drawLine(Color(0xFF8A7250), Offset(c.x - wd / 2 + wd * 0.14f, y), Offset(c.x + wd / 2 - inset, y), 1.1f * dp, alpha = alpha * 0.8f)
    }
    if (o > 0.85f) {
        val sc = Offset(c.x + wd * 0.26f, top + ht * 0.86f)
        drawCircle(Color(0xFFB0302A), wd * 0.09f, sc, alpha = alpha * seg(o, 0.85f, 1f))
        drawPath(starPath(sc, wd * 0.05f), Color(0xFFF0C070).copy(alpha = alpha * seg(o, 0.85f, 1f)))
    }
    for (y in listOf(top, bottom)) {
        drawRoundRect(Color(0xFF8A6440), topLeft = Offset(c.x - wd / 2 - wd * 0.06f, y - ht * 0.025f), size = Size(wd * 1.12f, ht * 0.05f), cornerRadius = CornerRadius(ht * 0.025f), alpha = alpha)
    }
}

// ═════════════ 1. astrolabe: the five decrees, ring within ring (taqdirat) ═════════════

private object AstroC14 {
    val Wall = Color(0xFFEFE3C8)
    val WallLow = Color(0xFFD9C59C)
    val Desk = Color(0xFF8A5E3A)
    val DeskDark = Color(0xFF5A3A22)
    val Brass = Color(0xFFC99A48)
    val BrassDark = Color(0xFF85601F)
    val BrassLight = Color(0xFFF2D28A)
    val Plate = Color(0xFF3E2C1A)
    val Lit = Color(0xFF8A5E22)
    val Pen = Color(0xFFFFE7A6)
    val Star = Color(0xFFFFF1C4)
    val Soul = Color(0xFFFFF8E6)
    val Moon = Color(0xFFF4EED8)
    val Sun = Color(0xFFFFC64C)
    val Back = Color(0xE6261A0C)
}

private val ASTRO_RINGS = listOf("ringgeneral", "ringcovenant", "ringlife", "ringyear", "ringday")

/**
 * A brass astrolabe on a desk by a sunlit window, its rings one inside the other. Beats, one per ring from the
 * outside in, each written round by a pen of light and then filled with its sign, the camera closing in on it:
 * ringgeneral (all creation: stars and worlds), ringcovenant (the sons of Adam as countless small lights),
 * ringlife (each child in the womb), ringyear (the months, one night among them shining), ringday (a sun at the
 * centre, today). Each ring's name shows while it is the one being told.
 */
internal fun DrawScope.astrolabeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val c = Offset(w * 0.5f, h * 0.5f)
    val shots = shotsFor(a, mapOf(
        "ringgeneral" to Shot(c, 1.0f),
        "ringcovenant" to Shot(c, 1.1f),
        "ringlife" to Shot(c, 1.22f),
        "ringyear" to Shot(c, 1.38f),
        "ringday" to Shot(c, 1.6f),
    ))
    followBeats(a, shots, Shot(c, 1f), dim = 0.1f) { astrolabeDesk(a) }
}

private fun DrawScope.astrolabeDesk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val deskY = h * 0.8f
    drawRect(Brush.verticalGradient(listOf(AstroC14.Wall, AstroC14.WallLow), endY = deskY))
    // the window's light across the wall
    drawRect(Color(0xFFFFF8E4), topLeft = Offset(w * 0.04f, h * 0.06f), size = Size(w * 0.12f, h * 0.36f), alpha = 0.7f)
    drawRect(AstroC14.DeskDark, topLeft = Offset(w * 0.04f, h * 0.06f), size = Size(w * 0.12f, h * 0.36f), style = Stroke(2f * dp), alpha = 0.6f)
    drawLine(AstroC14.DeskDark, Offset(w * 0.1f, h * 0.06f), Offset(w * 0.1f, h * 0.42f), 1.4f * dp, alpha = 0.6f)
    lightShafts(Offset(w * 0.1f, h * 0.2f), 0.35f, 0.6f, w * 0.95f, Color(0xFFFFF4D6), 0.9f, t, 6)
    drawRect(Brush.verticalGradient(listOf(AstroC14.Desk, AstroC14.DeskDark), startY = deskY, endY = h), topLeft = Offset(0f, deskY), size = Size(w, h - deskY))
    for (k in 0 until 3) drawLine(AstroC14.DeskDark, Offset(0f, deskY + h * (0.05f + 0.06f * k)), Offset(w, deskY + h * (0.055f + 0.06f * k)), 0.8f * dp, alpha = 0.5f)
    // a reed pen and an inkwell on the desk
    drawOval(Color(0xFF2A1E14), topLeft = Offset(w * 0.83f, deskY + h * 0.03f), size = Size(w * 0.07f, h * 0.06f))
    drawLine(Color(0xFFB8945A), Offset(w * 0.86f, deskY + h * 0.04f), Offset(w * 0.95f, deskY - h * 0.1f), 2.2f * dp, StrokeCap.Round)

    val c = Offset(w * 0.5f, h * 0.5f)
    val R = h * 0.44f
    // its shadow, its hanging ring, the brass rim
    drawOval(Color.Black.copy(alpha = 0.16f), topLeft = Offset(c.x - R * 0.95f, c.y + R * 0.88f), size = Size(R * 1.9f, R * 0.24f))
    drawCircle(AstroC14.BrassDark, R * 0.08f, c + Offset(0f, -R * 1.04f), style = Stroke(R * 0.035f))
    drawCircle(AstroC14.Brass, R * 1.02f, c)
    drawCircle(AstroC14.BrassLight, R * 1.0f, c, style = Stroke(1.2f * dp), alpha = 0.7f)

    val rb = floatArrayOf(0.95f, 0.77f, 0.6f, 0.44f, 0.28f, 0f)
    var cur = -1
    for (k in ASTRO_RINGS.indices) if (a.b(ASTRO_RINGS[k]) > 0f) cur = k
    for (k in 0 until 5) {
        val ro = R * rb[k]
        val ri = R * rb[k + 1]
        val mid = (ro + ri) / 2f
        val band = ro - ri
        val p = a.b(ASTRO_RINGS[k])
        val lit = easeOut(seg(p, 0.05f, 0.45f)) * (if (k == cur) 1f else 0.75f)
        drawCircle(lerpColor(AstroC14.Plate, AstroC14.Lit, lit), ro, c)
        drawCircle(AstroC14.BrassDark, ro, c, style = Stroke(1.6f * dp))
        drawCircle(AstroC14.BrassLight, ro - 1.5f * dp, c, style = Stroke(0.7f * dp), alpha = 0.6f)
        // engraved degree marks along its outer edge
        val ticks = if (k == 4) 0 else 60 - k * 8
        for (m in 0 until ticks) {
            val ang = m * 2f * PI.toFloat() / ticks
            val l = if (m % 5 == 0) band * 0.2f else band * 0.1f
            val u = Offset(cos(ang), sin(ang))
            drawLine(AstroC14.BrassLight, c + u * (ro - 1.5f * dp), c + u * (ro - 1.5f * dp - l), 0.7f * dp, alpha = 0.55f)
        }
        if (p <= 0f) continue
        // the pen of light writes the ring round, from the top
        val sweep = easeInOut(seg(p, 0f, 0.4f))
        if (sweep > 0f) {
            val rr = if (k == 4) ri + band * 0.75f else mid
            drawArc(AstroC14.Pen, -90f, 360f * sweep, false, topLeft = c - Offset(rr, rr), size = Size(2 * rr, 2 * rr), style = Stroke(max(1.5f * dp, band * 0.12f), cap = StrokeCap.Round), alpha = 0.9f * (1f - 0.6f * seg(p, 0.4f, 0.7f)))
            if (sweep < 1f) {
                val ang = -PI.toFloat() / 2f + 2f * PI.toFloat() * sweep
                val tip = c + Offset(cos(ang), sin(ang)) * rr
                softGlow(tip, 12f * dp, AstroC14.Pen, 0.9f)
                glint(tip, 9f * dp, Color.White, 0.9f)
            }
        }
        // its sign, coming in behind the pen (the top is left for the name)
        val show = easeOut(seg(p, 0.25f, 0.65f))
        if (show <= 0f) continue
        fun around(n: Int, i: Int): Offset {
            val gap = 0.5f
            val ang = -PI.toFloat() / 2f + gap + (2f * PI.toFloat() - 2f * gap) * (i + 0.5f) / n
            return c + Offset(cos(ang), sin(ang)) * mid
        }
        when (k) {
            0 -> for (i in 0 until 12) {
                val q = seg(show * 12f - i, 0f, 1f)
                val pc = around(12, i)
                if (i % 3 == 1) {
                    drawCircle(Color(0xFF9EC6E8), band * 0.2f * q, pc)
                    drawOval(AstroC14.Star, topLeft = pc - Offset(band * 0.36f, band * 0.08f), size = Size(band * 0.72f * q, band * 0.16f * q), style = Stroke(0.8f * dp), alpha = q)
                } else {
                    drawPath(starPath(pc, band * 0.2f * q), AstroC14.Star.copy(alpha = q))
                }
            }
            1 -> for (i in 0 until 34) {
                val q = seg(show * 34f - i, 0f, 1f)
                val pc = around(34, i) + Offset(0f, band * 0.18f * sin(i * 2.3f))
                softGlow(pc, band * 0.3f, AstroC14.Soul, 0.5f * q)
                drawCircle(AstroC14.Soul.copy(alpha = q * (0.75f + 0.25f * sin(t * 2f + i))), band * 0.09f, pc)
            }
            2 -> for (i in 0 until 7) {
                val q = seg(show * 7f - i, 0f, 1f)
                val pc = around(7, i)
                val rr = band * 0.3f
                drawCircle(Color(0xFFF3C9B0), rr * 1.25f, pc, alpha = 0.35f * q)
                drawArc(Color(0xFFFFE3D0), 140f, 270f, false, topLeft = pc - Offset(rr * 0.7f, rr * 0.7f), size = Size(rr * 1.4f, rr * 1.4f), style = Stroke(rr * 0.42f, cap = StrokeCap.Round), alpha = q)
                drawCircle(Color(0xFFFFE3D0), rr * 0.42f, pc + Offset(-rr * 0.55f, -rr * 0.45f), alpha = q)
            }
            3 -> for (i in 0 until 12) {
                val q = seg(show * 12f - i, 0f, 1f)
                val pc = around(12, i)
                val rr = band * 0.24f
                val qadr = i == 8
                if (qadr) {
                    softGlow(pc, rr * 4f, AstroC14.Pen, 0.8f * q * (0.8f + 0.2f * sin(t * 2.4f)))
                    glint(pc, rr * 2.4f, Color.White, 0.8f * q)
                }
                drawCircle(if (qadr) Color.White else AstroC14.Moon, rr, pc, alpha = q)
                drawCircle(lerpColor(AstroC14.Plate, AstroC14.Lit, lit), rr * 0.85f, pc + Offset(rr * 0.42f, -rr * 0.1f), alpha = q)
            }
            else -> {
                val rr = ri + band * 0.42f
                softGlow(c, rr * 2.6f, AstroC14.Sun, 0.8f * show)
                for (m in 0 until 12) {
                    val ang = m * PI.toFloat() / 6f + t * 0.15f
                    val u = Offset(cos(ang), sin(ang))
                    drawLine(AstroC14.Sun, c + u * (rr * 0.62f), c + u * (rr * (0.62f + 0.32f * show)), 2f * dp, StrokeCap.Round, alpha = show)
                }
                drawCircle(AstroC14.Sun, rr * 0.5f * show, c)
                drawCircle(Color(0xFFFFF0C0), rr * 0.32f * show, c)
            }
        }
        // the ring's name while it is told
        val next = if (k + 1 < ASTRO_RINGS.size) a.b(ASTRO_RINGS[k + 1]) else 0f
        val ta = seg(p, 0.3f, 0.6f) * (1f - seg(next, 0f, 0.25f))
        val at = if (k == 4) c + Offset(0f, R * 0.2f) else c + Offset(0f, -mid)
        tagAt(a.tag(ASTRO_RINGS[k]), at, ta, back = AstroC14.Back, ink = AstroC14.Pen)
    }
    // the hub
    drawCircle(AstroC14.BrassDark, R * 0.035f, c)
    drawCircle(AstroC14.BrassLight, R * 0.018f, c)
}

// ═════════════ 2. stage: the two extremes and the middle way (qadar-firaq) ═════════════

private object StageC14 {
    val SkyTop = Color(0xFF9CC8E8)
    val SkyLow = Color(0xFFEFF3E4)
    val Hill = Color(0xFFA9C487)
    val HillFar = Color(0xFFC4D6A8)
    val Ground = Color(0xFFB89466)
    val Field = Color(0xFF9A7046)
    val Furrow = Color(0xFF7A5434)
    val Ink = Color(0xFF2A2430)
    val Str = Color(0xFFFFFFFF)
    val Stone = Color(0xFFA8A29A)
    val StoneDark = Color(0xFF6E6862)
    val Seed = Color(0xFFF6DC94)
    val Sprout = Color(0xFF4E9A3A)
    val Wheat = Color(0xFFE2B84A)
    val Cloud = Color(0xFFF6F8FA)
    val Rain = Color(0xFF7AA8D8)
    val Bag = Color(0xFF8A6A3A)
    val Back = Color(0xE622202A)
}

/**
 * A field by day, the two extremes at its sides and the middle way in it. Beats: marionette (on the right a man
 * lowered on strings from above, his arms jerked up and down by them), jitter (he shakes all over like a man with a
 * tremor: none of it is his), selfmade (on the left a man rising out of a block of stone, carving himself with a
 * mallet and chisel, then lifting his arms: he made himself), sower (in the middle a man walks the furrow casting
 * seed with his own hand; a cloud brings rain and the seed sprouts; the two at the sides fade under a red sign),
 * praised (the field ripens to gold and a gold star shines over him: his deed is his).
 */
internal fun DrawScope.stageScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "marionette" to Shot(Offset(w * 0.78f, h * 0.48f), 1.35f),
        "jitter" to Shot(Offset(w * 0.78f, h * 0.5f), 1.5f),
        "selfmade" to Shot(Offset(w * 0.22f, h * 0.52f), 1.4f),
        "sower" to Shot(Offset(w * 0.5f, h * 0.5f), 1.08f),
        "praised" to Shot(Offset(w * 0.5f, h * 0.52f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { stageField(a) }
}

private fun DrawScope.stageField(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    val mp = a.b("marionette")
    val jt = a.b("jitter")
    val sm = a.b("selfmade")
    val sw = a.b("sower")
    val pr = a.b("praised")
    drawRect(Brush.verticalGradient(listOf(StageC14.SkyTop, StageC14.SkyLow), endY = ground))
    softGlow(Offset(w * 0.86f, h * 0.1f), w * 0.12f, Color(0xFFFFF0B8), 0.8f)
    drawCircle(Color(0xFFFFF4CC), w * 0.03f, Offset(w * 0.86f, h * 0.1f))
    hills(StageC14.HillFar, ground - h * 0.1f, h * 0.04f, 2.1f, 4.3f, 0.6f)
    hills(StageC14.Hill, ground - h * 0.04f, h * 0.03f, 3.2f, 6.1f, 1.9f)
    drawRect(StageC14.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))
    // the ploughed field in the middle
    val fl = w * 0.33f
    val fr = w * 0.67f
    drawRect(StageC14.Field, topLeft = Offset(fl, ground), size = Size(fr - fl, h - ground))
    for (k in 0 until 4) drawLine(StageC14.Furrow, Offset(fl, ground + h * (0.03f + 0.045f * k)), Offset(fr, ground + h * (0.03f + 0.045f * k)), 1.6f * dp)

    val tall = h * 0.36f
    val wrong = easeOut(seg(sw, 0f, 0.25f))
    val sideA = 1f - 0.55f * wrong

    // ── the puppet on strings (the Jabriyya), on the right ──
    if (mp > 0f) {
        val x = w * 0.8f
        val drop = -h * 0.6f * (1f - easeOut(seg(mp, 0f, 0.3f)))
        val shake = easeOut(seg(jt, 0f, 0.15f))
        val tr = { k: Int -> Offset(tall * 0.03f * shake * sin(t * 47f + k * 1.9f), tall * 0.025f * shake * sin(t * 59f + k * 2.7f)) }
        val sway = tall * 0.04f * sin(t * 1.3f)
        val hip = Offset(x + sway, ground - h * 0.03f - tall * 0.45f + drop) + tr(0)
        val neck = hip + Offset(sway * 0.5f, -tall * 0.31f) + tr(1)
        val jerkA = tall * (0.12f + 0.1f * shake) * sin(t * 2.4f + 5f * shake * sin(t * 9f))
        val jerkB = tall * (0.12f + 0.1f * shake) * sin(t * 2.4f + 2.2f)
        val handA = neck + Offset(-tall * 0.24f, -tall * 0.06f + jerkA) + tr(2)
        val handB = neck + Offset(tall * 0.24f, -tall * 0.03f + jerkB) + tr(3)
        val kneeLift = tall * 0.08f * sin(t * 2.4f + 1f)
        val footA = hip + Offset(-tall * 0.06f, tall * 0.45f - max(0f, kneeLift)) + tr(4)
        val footB = hip + Offset(tall * 0.07f, tall * 0.45f - max(0f, -kneeLift)) + tr(5)
        val head = neck + Offset(0f, -tall * 0.15f)
        // the strings run up out of sight: no hand holding them is shown
        for (p in listOf(head + Offset(0f, -tall * 0.1f), handA, handB, footA + Offset(0f, -tall * 0.2f))) {
            drawLine(StageC14.Str, p, Offset(p.x + (x - p.x) * 0.3f, -h * 0.05f), 0.9f * dp, alpha = 0.9f * sideA)
        }
        posed14(hip, neck, handA, handB, footA, footB, tall, StageC14.Ink, sideA)
        // tremor marks beside him
        if (shake > 0f) {
            for (sd in listOf(-1f, 1f)) for (m in 0 until 3) {
                val f = fract(t * 3f + m / 3f)
                val cc = neck + Offset(sd * tall * (0.36f + 0.1f * m), tall * 0.1f)
                drawArc(StageC14.Ink, if (sd < 0) 120f else -60f, 120f, false, topLeft = cc - Offset(tall * 0.08f, tall * 0.12f), size = Size(tall * 0.16f, tall * 0.24f), style = Stroke(1.4f * dp, cap = StrokeCap.Round), alpha = shake * (1f - f) * sideA)
            }
        }
        tagAt(a.tag("marionette"), Offset(x, ground + h * 0.09f), seg(mp, 0.3f, 0.7f) * sideA, back = StageC14.Back, ink = ArtInk.Ivory)
        if (wrong > 0f) noSign11pub(Offset(x, hip.y - tall * 0.12f), h * 0.13f, wrong)
    }

    // ── the man carving himself out of stone (the Qadariyya), on the left ──
    if (sm > 0f) {
        val x = w * 0.21f
        val come = easeOut(seg(sm, 0f, 0.15f))
        val emerge = easeOut(seg(sm, 0.1f, 0.75f))
        val pride = easeOut(seg(sm, 0.78f, 0.95f))
        val blockTop = ground - h * mix(0.34f, 0.15f, emerge)
        val bl = x - w * 0.085f
        val br = x + w * 0.085f
        val col = lerpColor(StageC14.StoneDark, StageC14.Ink, emerge)
        val hip = Offset(x, ground - tall * 0.45f)
        val neck = hip + Offset(0f, -tall * 0.31f)
        val q = fract(t * 1.3f)
        val strike = if (q < 0.7f) 0f else bump(seg(q, 0.7f, 1f))
        val raised = neck + Offset(tall * 0.2f, -tall * 0.22f)
        val down = neck + Offset(-tall * 0.02f, tall * 0.1f)
        val chiselHand = neck + Offset(-tall * 0.2f, tall * 0.18f)
        var handB = raised + (down - raised) * strike
        var handA = chiselHand
        if (pride > 0f) {
            handA = handA + (neck + Offset(-tall * 0.22f, -tall * 0.26f) - handA) * pride
            handB = handB + (neck + Offset(tall * 0.22f, -tall * 0.26f) - handB) * pride
        }
        posed14(hip, neck, handA, handB, hip + Offset(-tall * 0.07f, tall * 0.45f), hip + Offset(tall * 0.07f, tall * 0.45f), tall, col, come)
        // the mallet and the chisel in his hands
        drawLine(Color(0xFF6A4A2A), handB, handB + Offset(tall * 0.02f, -tall * 0.12f), 2f * dp, StrokeCap.Round, alpha = come)
        drawRoundRect(Color(0xFF8A6A42), topLeft = handB + Offset(-tall * 0.05f, -tall * 0.17f), size = Size(tall * 0.14f, tall * 0.07f), cornerRadius = CornerRadius(2f * dp), alpha = come)
        if (pride < 0.5f) drawLine(Color(0xFF9AA0A8), handA, handA + Offset(tall * 0.09f, tall * 0.06f), 2.2f * dp, StrokeCap.Round, alpha = come * (1f - 2f * pride))
        // the block he is cut from, its top chipped
        val top = Path().apply {
            moveTo(bl, ground)
            lineTo(bl, blockTop + h * 0.02f)
            lineTo(bl + w * 0.03f, blockTop)
            lineTo(x - w * 0.02f, blockTop + h * 0.015f)
            lineTo(x + w * 0.02f, blockTop - h * 0.01f)
            lineTo(br - w * 0.02f, blockTop + h * 0.01f)
            lineTo(br, blockTop + h * 0.025f)
            lineTo(br, ground)
            close()
        }
        drawPath(top, StageC14.Stone.copy(alpha = come))
        drawPath(top, StageC14.StoneDark.copy(alpha = come), style = Stroke(1.2f * dp))
        drawLine(StageC14.StoneDark, Offset(bl + w * 0.03f, ground - h * 0.04f), Offset(bl + w * 0.06f, ground - h * 0.08f), 0.9f * dp, alpha = come)
        // chips fly from each blow
        if (strike > 0.2f && pride < 0.2f) {
            for (m in 0 until 5) {
                val f = seg(q, 0.82f, 1f)
                val ang = -PI.toFloat() * (0.2f + 0.15f * m)
                val p0 = handA + Offset(tall * 0.09f, tall * 0.06f)
                drawCircle(StageC14.Stone, 1.6f * dp, p0 + Offset(cos(ang), sin(ang)) * (tall * 0.25f * f), alpha = come * (1f - f))
            }
        }
        // the dust heap of what he cut away
        drawOval(StageC14.Stone, topLeft = Offset(br - w * 0.01f, ground - h * 0.02f), size = Size(w * 0.06f * emerge, h * 0.035f), alpha = come)
        tagAt(a.tag("selfmade"), Offset(x, ground + h * 0.09f), seg(sm, 0.3f, 0.7f) * sideA, back = StageC14.Back, ink = ArtInk.Ivory)
        if (wrong > 0f) noSign11pub(Offset(x, neck.y + tall * 0.05f), h * 0.13f, wrong)
    }

    // ── the sower in the middle (the people of the Sunna) ──
    if (sw > 0f) {
        val walk = easeInOut(seg(sw, 0.05f, 0.6f))
        val sx = mix(w * 0.64f, w * 0.4f, walk)
        val foot = Offset(sx, ground + h * 0.025f)
        val walking = walk > 0f && walk < 1f
        val cast = if (walking) 0.5f + 0.5f * sin(t * 5f) else 0.15f
        figure(foot, tall, StageC14.Ink, walk = if (walking) t * 6f else 0f, arms = cast)
        // his seed bag
        drawOval(StageC14.Bag, topLeft = foot + Offset(tall * 0.02f, -tall * 0.62f), size = Size(tall * 0.16f, tall * 0.2f))
        drawLine(StageC14.Bag, foot + Offset(tall * 0.08f, -tall * 0.62f), foot + Offset(-tall * 0.02f, -tall * 0.78f), 1.4f * dp)
        val hand = foot + Offset(-tall * 0.28f * (0.35f + 0.65f * cast), -tall * 0.76f + tall * 0.28f * (0.85f - 0.75f * cast))
        // the seed: each falls when he passes its place, then sprouts
        val rain = easeOut(seg(sw, 0.5f, 0.65f)) * (1f - easeOut(seg(pr, 0.2f, 0.6f)))
        val grow = easeOut(seg(sw, 0.62f, 1f))
        val ripe = easeOut(seg(pr, 0.1f, 0.7f))
        for (m in 0 until 12) {
            val xm = w * 0.61f - m * w * 0.018f
            val dropped = seg((xm - sx) / (w * 0.03f) + 0.2f, 0f, 1f)
            if (dropped <= 0f) continue
            val land = Offset(xm, ground + h * 0.03f)
            if (dropped < 1f) {
                val p = hand + (land - hand) * dropped
                drawCircle(StageC14.Seed, 1.8f * dp, p)
            } else if (grow <= 0f) {
                drawCircle(StageC14.Seed, 1.6f * dp, land)
            } else {
                val ht = h * (0.05f + 0.07f * ripe) * grow * (0.85f + 0.15f * w8Rnd(m, 3.7f))
                val tip = land + Offset(tall * 0.02f * sin(t * 1.5f + m), -ht)
                drawLine(lerpColor(StageC14.Sprout, Color(0xFFC9A040), ripe), land, tip, 1.6f * dp, StrokeCap.Round)
                drawOval(StageC14.Sprout.copy(alpha = 1f - ripe), topLeft = land + Offset(-ht * 0.35f, -ht * 0.6f), size = Size(ht * 0.3f, ht * 0.14f))
                drawOval(StageC14.Sprout.copy(alpha = 1f - ripe), topLeft = land + Offset(ht * 0.05f, -ht * 0.5f), size = Size(ht * 0.3f, ht * 0.14f))
                if (ripe > 0f) drawOval(StageC14.Wheat.copy(alpha = ripe), topLeft = tip - Offset(1.8f * dp, 5f * dp * ripe), size = Size(3.6f * dp, 8f * dp * ripe))
            }
        }
        // the cloud and its rain: the growing is not his
        if (rain > 0f) {
            val cc = Offset(w * 0.5f, h * 0.14f)
            for ((dx, rr) in listOf(-0.06f to 0.045f, 0f to 0.06f, 0.06f to 0.045f)) drawCircle(StageC14.Cloud, w * rr, cc + Offset(w * dx, 0f), alpha = rain)
            for (m in 0 until 14) {
                val f = fract(t * 1.6f + m * 0.37f)
                val x0 = w * (0.44f + 0.012f * m)
                val y0 = cc.y + h * 0.05f + f * (ground - cc.y - h * 0.05f)
                drawLine(StageC14.Rain, Offset(x0, y0), Offset(x0 - 2f * dp, y0 + h * 0.04f), 1.2f * dp, alpha = rain * 0.8f)
            }
        }
        // praise: a gold star over the man whose deed it is
        if (pr > 0f) {
            val st = easeOut(seg(pr, 0.05f, 0.45f))
            val sc = foot + Offset(0f, -tall * 1.25f)
            softGlow(sc, tall * 0.4f, ArtInk.Gold, 0.8f * st)
            drawPath(starPath(sc, tall * 0.14f * st), ArtInk.Gold)
            glint(sc, tall * 0.3f * st, Color.White, 0.8f * st)
        }
        tagAt(a.tag("sower"), Offset(w * 0.5f, h * 0.95f), seg(sw, 0.2f, 0.5f), back = StageC14.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 3. eden: Adam and Musa, and the servant's two ways (ihtijaj-qadar) ═════════════

private object EdenC14 {
    val SkyTop = Color(0xFF3A3A6E)
    val SkyMid = Color(0xFF8E7AA8)
    val SkyLow = Color(0xFFF0C8A0)
    val Ground = Color(0xFF5A4A3E)
    val GroundEdge = Color(0xFF7A6450)
    val Garden = Color(0xFF6FBF7A)
    val GardenDark = Color(0xFF3E8A52)
    val Gate = Color(0xFFF2DFA8)
    val Path = Color(0xFFE8D2A0)
    val Kin = Color(0xFF2A2236)
    val Tree = Color(0xFF5A3E2A)
    val Leaf = Color(0xFF4E8A4A)
    val Fruit = Color(0xFFD8584A)
    val Tear = Color(0xFFA8DCF4)
    val Bolt = Color(0xFFFFF2A8)
    val Blot = Color(0xFF1A141E)
    val Cloud = Color(0xFF5A5A70)
    val Back = Color(0xE6141826)
}

/**
 * Dawn, high above the earth; a garden glowing far up on the left. Beats: blame (two small lights, Adam's and
 * Musa's, each with his name; Musa's turns to Adam, while far below a line of his children walks out of the
 * garden's gate down to the earth), decree (a scroll comes down between them and unrolls: written before; Adam's
 * light grows, Musa's sinks a little), repented (beside Adam a tree with one fruit; his light bows low to the
 * ground and drops fall, then it rises higher and brighter, and a path of light opens ahead of it), twoways (on the
 * earth an ordinary man: lightning fells his tree, a calamity; a dark blot settles on his chest, his own fault; a
 * scroll over him: a green tick from the tree to it, a red cross from the blot), bothways (he bows and his tears
 * wash the blot away; then he sits by the stump, calm, and a new shoot rises from it).
 */
internal fun DrawScope.edenScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "blame" to Shot(Offset(w * 0.42f, h * 0.42f), 1.05f),
        "decree" to Shot(Offset(w * 0.52f, h * 0.36f), 1.3f),
        "repented" to Shot(Offset(w * 0.4f, h * 0.45f), 1.3f),
        "twoways" to Shot(Offset(w * 0.55f, h * 0.68f), 1.35f),
        "bothways" to Shot(Offset(w * 0.55f, h * 0.7f), 1.45f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.18f) { edenSky(a) }
}

private fun DrawScope.edenSky(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.86f
    val bl = a.b("blame")
    val dc = a.b("decree")
    val rp = a.b("repented")
    val tw = a.b("twoways")
    val bw = a.b("bothways")
    drawRect(Brush.verticalGradient(0f to EdenC14.SkyTop, 0.55f to EdenC14.SkyMid, 1f to EdenC14.SkyLow, endY = ground))
    for (k in 0 until 30) {
        val p = Offset(w * w8Rnd(k, 12.9f), h * 0.45f * w8Rnd(k, 78.2f))
        drawCircle(ArtInk.Ivory, (0.6f + 0.8f * w8Rnd(k, 3.3f)) * dp, p, alpha = 0.5f * (0.6f + 0.4f * sin(t * 1.3f + k)))
    }
    // the garden, far up on the left, and its gate
    val gc = Offset(w * 0.14f, h * 0.2f)
    softGlow(gc, w * 0.2f, EdenC14.Garden, 0.55f)
    drawOval(EdenC14.GardenDark, topLeft = gc - Offset(w * 0.13f, h * 0.02f), size = Size(w * 0.26f, h * 0.09f))
    drawOval(EdenC14.Garden, topLeft = gc - Offset(w * 0.12f, h * 0.035f), size = Size(w * 0.24f, h * 0.07f))
    for (k in 0 until 5) {
        val tx = gc.x + w * (-0.09f + 0.045f * k)
        drawLine(EdenC14.Tree, Offset(tx, gc.y), Offset(tx, gc.y - h * 0.05f), 1.6f * dp)
        drawCircle(EdenC14.Garden, w * 0.022f, Offset(tx, gc.y - h * 0.065f))
        drawCircle(EdenC14.GardenDark, w * 0.022f, Offset(tx, gc.y - h * 0.065f), style = Stroke(0.8f * dp), alpha = 0.6f)
    }
    val gateC = Offset(gc.x + w * 0.1f, gc.y + h * 0.005f)
    drawPath(horseshoe(gateC.x, gateC.y - h * 0.045f, w * 0.018f, w * 0.015f, gateC.y), EdenC14.Gate)
    // the way down from the gate to the earth
    val road = w8Smooth(listOf(gateC, Offset(w * 0.3f, h * 0.36f), Offset(w * 0.2f, h * 0.58f), Offset(w * 0.34f, h * 0.76f), Offset(w * 0.3f, ground)))
    val roadA = max(easeOut(seg(bl, 0.1f, 0.4f)), 0f) * (1f - 0.7f * easeOut(seg(rp, 0f, 0.3f)))
    if (roadA > 0f) drawPath(w8Poly(road), EdenC14.Path.copy(alpha = 0.45f * roadA), style = Stroke(1.6f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))))
    // the earth below
    drawRect(EdenC14.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))
    drawLine(EdenC14.GroundEdge, Offset(0f, ground), Offset(w, ground), 1.2f * dp)

    // his children walking out of the garden, down to the earth: the calamity
    if (bl > 0f) {
        val go = seg(bl, 0.15f, 1f) * (1f - easeOut(seg(rp, 0f, 0.3f)))
        for (m in 0 until 7) {
            val u = fract(t * 0.05f + m / 7f)
            val pc = w8Along(road, u)
            val tl = h * 0.06f * (0.6f + 0.4f * u)
            figure(pc, tl, EdenC14.Kin, alpha = go * seg(u, 0f, 0.06f) * (1f - seg(u, 0.92f, 1f)), walk = t * 5f + m)
        }
    }

    // the two lights
    val adam0 = Offset(w * 0.42f, h * 0.4f)
    val musa = Offset(w * 0.64f, h * 0.4f)
    val bow = easeInOut(seg(rp, 0.05f, 0.3f)) * (1f - easeInOut(seg(rp, 0.45f, 0.75f)))
    val up = easeInOut(seg(rp, 0.45f, 0.75f))
    val adam = adam0 + Offset(0f, h * 0.24f * bow - h * 0.12f * up)
    val lights = easeOut(seg(bl, 0f, 0.2f)) * (1f - 0.6f * easeOut(seg(tw, 0f, 0.3f)))
    val win = easeOut(seg(dc, 0.5f, 0.9f))
    val musaA = lights * (1f - 0.25f * win) * (1f - 0.55f * easeOut(seg(rp, 0f, 0.3f)))
    val musaC = musa + Offset(0f, h * 0.03f * win)
    // Musa's blame, turned to Adam
    if (bl > 0.2f && dc < 0.6f) {
        val q = seg(bl, 0.25f, 1f) * (1f - seg(dc, 0.3f, 0.6f))
        for (m in 0 until 4) {
            val f = fract(t * 0.8f + m / 4f)
            val p = musaC + (adam - musaC) * (0.15f + 0.6f * f)
            drawArc(ArtInk.Ivory, 150f, 60f, false, topLeft = p - Offset(h * 0.04f, h * 0.04f), size = Size(h * 0.08f, h * 0.08f), style = Stroke(1.3f * dp, cap = StrokeCap.Round), alpha = q * (1f - f))
        }
    }
    // the decree written before, between them
    val sd = easeOut(seg(dc, 0f, 0.4f)) * (1f - easeOut(seg(rp, 0f, 0.3f)))
    if (sd > 0f) {
        val sc = Offset(w * 0.53f, mix(-h * 0.1f, h * 0.22f, sd))
        scroll14(sc, w * 0.09f, h * 0.17f, seg(dc, 0.25f, 0.6f), sd)
        drawLine(ArtInk.Gold, sc + Offset(-w * 0.03f, h * 0.09f), adam, 1.2f * dp, alpha = 0.6f * win * sd)
    }
    nabiLight14(musaC, 3.6f * dp, musaA, t, a.tag("blame", 0, 1), musaC + Offset(0f, h * 0.08f))
    nabiLight14(adam, 3.6f * dp, lights * (1f + 0.35f * win + 0.25f * up), t, a.tag("blame", 0, 0), adam + Offset(0f, h * 0.08f))

    // repented: the tree, the bowing, the tears, the rise, the way opened ahead
    if (rp > 0f) {
        val tr = easeOut(seg(rp, 0f, 0.2f)) * (1f - easeOut(seg(tw, 0f, 0.3f)))
        val base = Offset(w * 0.3f, h * 0.7f)
        drawLine(EdenC14.Tree, base, base + Offset(0f, -h * 0.14f), 2.4f * dp, StrokeCap.Round, alpha = tr)
        drawCircle(EdenC14.Leaf, h * 0.07f, base + Offset(0f, -h * 0.18f), alpha = tr)
        drawCircle(EdenC14.Fruit, h * 0.016f, base + Offset(h * 0.03f, -h * 0.15f), alpha = tr)
        if (bow > 0.3f) {
            for (m in 0 until 5) {
                val f = fract(t * 1.4f + m / 5f)
                drawCircle(EdenC14.Tear, 1.6f * dp, adam + Offset((m - 2) * 3f * dp, h * 0.02f + f * h * 0.08f), alpha = bow * (1f - f))
            }
        }
        val way = easeOut(seg(rp, 0.7f, 1f)) * (1f - easeOut(seg(tw, 0f, 0.3f)))
        if (way > 0f) {
            val p0 = adam + Offset(-w * 0.02f, -h * 0.02f)
            val p3 = adam + Offset(-w * 0.2f, -h * 0.18f)
            drawPath(cubicPart(p0, p0 + Offset(-w * 0.05f, -h * 0.02f), p3 + Offset(w * 0.06f, h * 0.04f), p3, 0f, way), ArtInk.Gold.copy(alpha = 0.85f), style = Stroke(1.6f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp), -t * 18f * dp)))
            softGlow(cubic(p0, p0 + Offset(-w * 0.05f, -h * 0.02f), p3 + Offset(w * 0.06f, h * 0.04f), p3, way), 10f * dp, ArtInk.Gold, 0.8f * way)
        }
    }

    // the servant on the earth: a calamity and a fault
    if (tw > 0f) {
        val come = easeOut(seg(tw, 0f, 0.15f))
        val manFoot = Offset(w * 0.52f, ground)
        val tall = h * 0.2f
        val treeX = w * 0.7f
        val fell = easeInOut(seg(tw, 0.12f, 0.32f))
        val bolt = bump(seg(tw, 0.06f, 0.18f))
        // the storm cloud and the bolt
        val storm = come * (1f - easeOut(seg(bw, 0.4f, 0.8f)))
        val cl = Offset(treeX, h * 0.5f)
        for ((dx, rr) in listOf(-0.05f to 0.035f, 0f to 0.045f, 0.05f to 0.035f)) drawCircle(EdenC14.Cloud, w * rr, cl + Offset(w * dx, 0f), alpha = storm)
        if (bolt > 0f) {
            drawPath(w8Poly(listOf(cl + Offset(0f, h * 0.03f), cl + Offset(-w * 0.015f, h * 0.1f), cl + Offset(w * 0.01f, h * 0.1f), Offset(treeX, ground - h * 0.17f))), EdenC14.Bolt.copy(alpha = bolt), style = Stroke(2.2f * dp, cap = StrokeCap.Round))
            softGlow(Offset(treeX, ground - h * 0.17f), h * 0.12f, EdenC14.Bolt, bolt)
        }
        // the tree, felled
        val treeBase = Offset(treeX, ground)
        val ang = -PI.toFloat() / 2f + fell * PI.toFloat() * 0.45f
        val dir = Offset(cos(ang), sin(ang))
        val trunkTop = treeBase + Offset(0f, -h * 0.03f) + dir * (h * 0.15f)
        drawRect(EdenC14.Tree, topLeft = treeBase + Offset(-2.5f * dp, -h * 0.03f), size = Size(5f * dp, h * 0.03f), alpha = come)
        drawLine(EdenC14.Tree, treeBase + Offset(0f, -h * 0.03f), trunkTop, 3f * dp, StrokeCap.Round, alpha = come)
        drawCircle(EdenC14.Leaf, h * 0.05f, trunkTop + dir * (h * 0.03f), alpha = come)
        // the new shoot from the stump, at the end
        val shoot = easeOut(seg(bw, 0.6f, 1f))
        if (shoot > 0f) {
            drawLine(EdenC14.Leaf, treeBase + Offset(-w * 0.015f, -h * 0.02f), treeBase + Offset(-w * 0.018f, -h * (0.02f + 0.06f * shoot)), 1.6f * dp, StrokeCap.Round)
            drawOval(EdenC14.Leaf, topLeft = treeBase + Offset(-w * 0.03f, -h * (0.02f + 0.06f * shoot)), size = Size(w * 0.014f, h * 0.015f))
        }
        // the man: stands, then bows in repentance, then sits calm
        val kneel = easeOut(seg(bw, 0f, 0.15f)) * (1f - easeOut(seg(bw, 0.45f, 0.6f)))
        val sit = easeOut(seg(bw, 0.5f, 0.65f))
        val chest: Offset
        when {
            sit > 0.5f -> {
                w8Seated(Offset(manFoot.x + w * 0.04f, ground), tall, EdenC14.Kin, 1f)
                chest = Offset(manFoot.x + w * 0.04f, ground - tall * 0.25f)
            }
            kneel > 0.3f -> {
                w8Worshipper(manFoot, tall, EdenC14.Kin, 0f, min(1f, kneel))
                chest = manFoot + Offset(-tall * 0.15f, -tall * 0.55f)
                for (m in 0 until 4) {
                    val f = fract(t * 1.5f + m / 4f)
                    drawCircle(EdenC14.Tear, 1.5f * dp, manFoot + Offset(-tall * 0.42f + m * 2f * dp, -tall * 0.6f + f * tall * 0.55f), alpha = kneel * (1f - f))
                }
            }
            else -> {
                figure(manFoot, tall, EdenC14.Kin, come, arms = 0.4f * easeOut(seg(tw, 0.45f, 0.7f)) * (1f - easeOut(seg(bw, 0f, 0.1f))))
                chest = manFoot + Offset(0f, -tall * 0.62f)
            }
        }
        // his fault: a dark blot on his chest, washed away by his tears
        val blot = easeOut(seg(tw, 0.3f, 0.45f)) * (1f - easeOut(seg(bw, 0.1f, 0.45f)))
        if (blot > 0f) {
            for (m in 0 until 4) {
                val p = chest + Offset(tall * 0.05f * sin(t * 0.9f + m * 1.6f), -tall * 0.04f * m * (0.5f + 0.5f * fract(t * 0.4f + m * 0.3f)))
                drawCircle(EdenC14.Blot, tall * (0.09f - 0.012f * m), p, alpha = blot * (0.85f - 0.15f * m))
            }
        }
        // the scroll of the decree over him: a tick from the calamity, a cross from the fault
        val sa = easeOut(seg(tw, 0.4f, 0.55f)) * (1f - easeOut(seg(bw, 0.3f, 0.6f)))
        if (sa > 0f) {
            val sc = Offset(w * 0.58f, h * 0.53f)
            scroll14(sc, w * 0.07f, h * 0.12f, 1f, sa)
            val g = seg(tw, 0.45f, 0.7f)
            val fromT = trunkTop + Offset(0f, -h * 0.03f)
            val toS = sc + Offset(w * 0.04f, h * 0.02f)
            drawLine(Color(0xFF2E9A55), fromT, fromT + (toS - fromT) * g, 1.6f * dp, StrokeCap.Round, alpha = sa)
            tick14(fromT + (toS - fromT) * 0.5f, h * 0.022f, seg(tw, 0.6f, 0.75f) * sa)
            val r = seg(tw, 0.7f, 0.9f)
            val toS2 = sc + Offset(-w * 0.045f, h * 0.03f)
            if (r > 0f) drawLine(Color(0xFFD23C30), chest, chest + (toS2 - chest) * r, 1.6f * dp, StrokeCap.Round, alpha = sa)
            cross14(chest + (toS2 - chest) * 0.55f + Offset(-w * 0.03f, 0f), h * 0.02f, seg(tw, 0.82f, 0.97f) * sa)
        }
    }
}

// ═════════════ 4. casket: the sealed chest left unopened (qadar-khisam) ═════════════

private object CasketC14 {
    val Wall = Color(0xFFE8D6B0)
    val WallLow = Color(0xFFD0B88A)
    val Arch = Color(0xFFBFA272)
    val ArchIn = Color(0xFF8A6E4A)
    val Floor = Color(0xFFB08A62)
    val Tile = Color(0xFF9A7650)
    val Wood = Color(0xFF5A3A22)
    val WoodLight = Color(0xFF7A5232)
    val Band = Color(0xFFD8B05A)
    val Seal = Color(0xFFB0302A)
    val Ink = Color(0xFF2A2430)
    val Bar = Color(0xFF6E7078)
    val Cloth = Color(0xFF2E5A4A)
    val ClothEdge = Color(0xFFD8B05A)
    val Back = Color(0xE6201810)
}

/** A question mark drawn in strokes (no font), [s] high, at [c]. */
private fun DrawScope.qMark14(c: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    val sw = s * 0.16f
    drawArc(color, 180f, 270f, false, topLeft = c + Offset(-s * 0.25f, -s * 0.5f), size = Size(s * 0.5f, s * 0.45f), style = Stroke(sw, cap = StrokeCap.Round), alpha = alpha)
    drawLine(color, c + Offset(0f, -s * 0.05f), c + Offset(0f, s * 0.15f), sw, StrokeCap.Round, alpha = alpha)
    drawCircle(color, sw * 0.7f, c + Offset(0f, s * 0.38f), alpha = alpha)
}

/**
 * A courtyard by day with a sealed chest in it. Beats: pry (two men come at it with iron bars and try to prise
 * its lid open, rocking it, and argue across it: question marks and sharp lines fly between them), sealed (the
 * bars snap, the men stagger back, and the seal shines, unbroken), submit (they drop the broken bars and sit
 * down, a calm light in their hearts), followsaid (his ﷺ light comes over the courtyard with his name and they
 * lift their hands to it), hush (a cloth comes down over the chest; the question marks are gone).
 */
internal fun DrawScope.casketScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "pry" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "sealed" to Shot(Offset(w * 0.5f, h * 0.6f), 1.45f),
        "submit" to Shot(Offset(w * 0.5f, h * 0.6f), 1.2f),
        "followsaid" to Shot(Offset(w * 0.5f, h * 0.45f), 1.05f),
        "hush" to Shot(Offset(w * 0.5f, h * 0.6f), 1.25f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { casketYard(a) }
}

private fun DrawScope.casketYard(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    val py = a.b("pry")
    val se = a.b("sealed")
    val sb = a.b("submit")
    val fs = a.b("followsaid")
    val hu = a.b("hush")
    drawRect(Brush.verticalGradient(listOf(CasketC14.Wall, CasketC14.WallLow), endY = ground))
    for (k in 0 until 3) {
        val ax = w * (0.18f + 0.32f * k)
        drawPath(horseshoe(ax, h * 0.3f, w * 0.08f, w * 0.07f, ground - h * 0.02f), CasketC14.Arch)
        drawPath(horseshoe(ax, h * 0.3f, w * 0.065f, w * 0.055f, ground - h * 0.02f), CasketC14.ArchIn, alpha = 0.55f)
    }
    drawRect(CasketC14.Floor, topLeft = Offset(0f, ground), size = Size(w, h - ground))
    for (k in 0 until 9) drawLine(CasketC14.Tile, Offset(w * k / 8f, ground), Offset(w * (k / 8f - 0.5f) * 1.4f + w * 0.5f, h), 0.8f * dp)
    drawLine(CasketC14.Tile, Offset(0f, ground + h * 0.08f), Offset(w, ground + h * 0.08f), 0.8f * dp)

    val cx = w * 0.5f
    val cw = w * 0.24f
    val ch = h * 0.17f
    val rock = if (se < 0.1f) sin(t * 7f) * 2.5f * easeOut(seg(py, 0.25f, 0.4f)) else 0f
    val top = ground - ch
    // the chest
    withTransform({ rotate(rock, Offset(cx, ground)) }) {
        drawRect(Color.Black, topLeft = Offset(cx - cw / 2 + 3f * dp, top + 4f * dp), size = Size(cw, ch), alpha = 0.2f)
        drawRect(CasketC14.Wood, topLeft = Offset(cx - cw / 2, top), size = Size(cw, ch))
        drawRoundRect(CasketC14.WoodLight, topLeft = Offset(cx - cw / 2 - w * 0.01f, top - h * 0.05f), size = Size(cw + w * 0.02f, h * 0.06f), cornerRadius = CornerRadius(h * 0.025f))
        for (sd in listOf(-0.32f, 0.32f)) drawRect(CasketC14.Band, topLeft = Offset(cx + cw * sd - w * 0.008f, top - h * 0.05f), size = Size(w * 0.016f, ch + h * 0.05f))
        drawLine(CasketC14.Band, Offset(cx - cw / 2, top + h * 0.005f), Offset(cx + cw / 2, top + h * 0.005f), 1.6f * dp)
        val seal = Offset(cx, top + h * 0.01f)
        val shine = easeOut(seg(se, 0.2f, 0.6f)) * (1f - 0.6f * easeOut(seg(sb, 0f, 0.5f)))
        if (shine > 0f) {
            softGlow(seal, h * 0.16f, ArtInk.Gold, 0.9f * shine)
            for (m in 0 until 3) {
                val f = fract(t * 0.9f + m / 3f)
                drawCircle(ArtInk.Gold, h * (0.03f + 0.12f * f), seal, alpha = shine * (1f - f), style = Stroke(1.4f * dp))
            }
        }
        drawCircle(CasketC14.Seal, h * 0.035f, seal)
        drawPath(starPath(seal, h * 0.02f), CasketC14.Band)
    }
    // the cloth that comes down over it at the end
    val cl = easeOut(seg(hu, 0f, 0.6f))
    if (cl > 0f) {
        val drop = -h * 0.4f * (1f - cl)
        val ct = top - h * 0.07f + drop
        val cloth = Path().apply {
            moveTo(cx - cw / 2 - w * 0.03f, ct + h * 0.06f)
            quadraticTo(cx, ct - h * 0.03f, cx + cw / 2 + w * 0.03f, ct + h * 0.06f)
            lineTo(cx + cw / 2 + w * 0.035f, ct + h * 0.06f + (ground - ct - h * 0.06f) * cl)
            quadraticTo(cx, ct + h * 0.03f + (ground - ct) * cl, cx - cw / 2 - w * 0.035f, ct + h * 0.06f + (ground - ct - h * 0.06f) * cl)
            close()
        }
        drawPath(cloth, CasketC14.Cloth)
        drawPath(cloth, CasketC14.ClothEdge, style = Stroke(1.2f * dp), alpha = 0.8f)
    }
    tagAt(a.tag("pry"), Offset(cx, top - h * 0.14f), seg(py, 0.3f, 0.6f) * (1f - seg(sb, 0f, 0.3f)), back = CasketC14.Back, ink = ArtInk.Gold)

    // the two men, mirrored on either side
    val tall = h * 0.36f
    val come = easeOut(seg(py, 0f, 0.22f))
    val prying = seg(py, 0.22f, 1f) > 0f && se < 0.15f
    val snap = easeOut(seg(se, 0.05f, 0.3f))
    val back = easeOut(seg(se, 0.1f, 0.45f)) * (1f - easeOut(seg(sb, 0.1f, 0.5f)))
    val sit = easeOut(seg(sb, 0.2f, 0.55f))
    val lift = easeOut(seg(fs, 0.25f, 0.6f))
    for (sd in listOf(-1f, 1f)) {
        val baseX = cx + sd * w * 0.24f
        val x = mix(cx + sd * w * 0.6f, baseX, come) + sd * w * 0.04f * back
        val foot = Offset(x, ground + h * 0.03f)
        val pivot = Offset(x, foot.y)
        withTransform({ if (sd < 0f) scale(-1f, 1f, pivot) }) {
            // drawn facing left (towards the chest when on the right)
            if (sit > 0.5f) {
                w8Seated(Offset(x, ground + h * 0.05f), tall, CasketC14.Ink, -1f, speak = lift)
                val heart = Offset(x - tall * 0.02f, ground + h * 0.05f - tall * 0.27f)
                softGlow(heart, tall * 0.12f, ArtInk.Gold, 0.7f * sit)
                drawPath(w8Heart(heart, tall * 0.045f), ArtInk.Gold.copy(alpha = sit))
            } else {
                val heave = if (prying) 0.5f + 0.5f * sin(t * 7f + sd) else 0f
                val argue = if (prying) max(0f, sin(t * 2.2f + (if (sd > 0f) 0f else PI.toFloat()))) else 0f
                figure(foot, tall, CasketC14.Ink, come, walk = if (come < 1f) t * 6f else 0f, lean = 0.12f * heave * come - 0.2f * back, arms = if (come < 1f) 0.2f else 0.55f + 0.45f * argue)
                // the iron bar under the lid, levered
                val hand = foot + Offset(-tall * 0.3f, -tall * 0.5f)
                val tip = Offset(x - (w * 0.24f - cw / 2) - w * 0.01f, top - h * 0.005f)
                if (snap < 0.5f && come > 0.6f) {
                    drawLine(CasketC14.Bar, hand + Offset(0f, -tall * 0.04f * heave), tip, 2.6f * dp, StrokeCap.Round)
                } else if (snap >= 0.5f && sit < 0.5f) {
                    // the two halves of the broken bar on the floor
                    val f = easeOut(seg(se, 0.15f, 0.5f))
                    val p1 = hand + (Offset(hand.x - w * 0.03f, ground + h * 0.06f) - hand) * f
                    drawLine(CasketC14.Bar, p1, p1 + Offset(-w * 0.05f, h * 0.01f), 2.6f * dp, StrokeCap.Round)
                    val p2 = tip + (Offset(tip.x + w * 0.02f, ground + h * 0.07f) - tip) * f
                    drawLine(CasketC14.Bar, p2, p2 + Offset(w * 0.04f, h * 0.01f), 2.6f * dp, StrokeCap.Round)
                }
                if (snap > 0f && snap < 1f) glint(tip + Offset(w * 0.03f, -h * 0.02f), h * 0.06f, Color.White, bump(snap))
            }
            if (sit > 0.5f) {
                // the broken bars put down beside them
                drawLine(CasketC14.Bar, Offset(x + tall * 0.25f, ground + h * 0.07f), Offset(x + tall * 0.42f, ground + h * 0.075f), 2.6f * dp, StrokeCap.Round, alpha = 0.8f)
            }
        }
        // their argument across the chest
        if (prying || (se > 0f && se < 0.4f)) {
            val ga = come * (1f - seg(se, 0.1f, 0.4f))
            val head = Offset(x, foot.y - tall * 1.02f)
            val phase = sin(t * 2.2f + (if (sd > 0f) 0f else PI.toFloat()))
            val bubble = head + Offset(-sd * tall * 0.18f, -tall * 0.24f)
            if (phase > 0f) {
                drawCircle(Color.White, tall * 0.12f, bubble, alpha = 0.9f * ga * phase)
                qMark14(bubble, tall * 0.16f, CasketC14.Ink, ga * phase)
            }
            // sharp lines between them
            for (m in 0 until 3) {
                val f = fract(t * 1.4f + m / 3f + (if (sd > 0f) 0.5f else 0f))
                val p = Offset(mix(x, cx, f), head.y + tall * 0.05f - m * tall * 0.07f)
                drawPath(w8Poly(listOf(p, p + Offset(-sd * tall * 0.04f, -tall * 0.04f), p + Offset(-sd * tall * 0.08f, 0f), p + Offset(-sd * tall * 0.12f, -tall * 0.04f))), Color(0xFFC0392B).copy(alpha = ga * (1f - f)), style = Stroke(1.4f * dp, cap = StrokeCap.Round))
            }
        }
    }
    // his ﷺ light over the courtyard
    if (fs > 0f) {
        val lc = Offset(w * 0.5f, h * 0.17f)
        sirajMunir(lc, 4.4f * dp, easeOut(seg(fs, 0f, 0.3f)), t, nameAt = lc + Offset(w * 0.2f, 0f))
    }
}
