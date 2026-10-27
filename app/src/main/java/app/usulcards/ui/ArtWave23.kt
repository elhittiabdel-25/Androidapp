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
import androidx.compose.ui.text.drawText
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 23 (2026-10-27): «بصائر»، «دلائل الخالق». Three framed windows for the three proofs (a house that has been
 * built, an orchid that wears the shape of a bee, rain that becomes bread); a man counting his few coins and then the
 * sky that spends without end, and the star that forged the iron in our blood; a scale where a child outweighs the
 * mountains and a man who takes up the trust; and planets, a forest, a desert — and the letters written in a cell.
 */

// ═════════════ 1. three windows: making, mastery, care (bs-adilla) ═════════════

private object PanC23 {
    val Wall = Color(0xFFEDE4D0)
    val Frame = Color(0xFF8A6238)
    val Sky = Color(0xFFBFDDF2)
    val Land = Color(0xFFC8B488)
    val Grass = Color(0xFF7FB06A)
    val Brick = Color(0xFFC08A5E)
    val Petal = Color(0xFFD8A0D0)
    val PetalDark = Color(0xFF8A4A8A)
    val Bee = Color(0xFFE8B83A)
    val Cloud = Color(0xFFF2F4F8)
    val Rain = Color(0xFF6AA0D8)
    val Bread = Color(0xFFD8A060)
    val Ink = Color(0xFF2A2430)
    val Back = Color(0xE6201810)
}

private fun panelC23(w: Float, h: Float, k: Int): Offset = Offset(w * (0.82f - 0.32f * k), h * 0.56f)

/**
 * A wall with three framed windows, each opening on a small landscape. Beats: frames (the three windows appear,
 * «الْإِيجَادِ»، «الْإِتْقَانِ»، «الرِّعَايَةِ» over them), made (in the first, on an empty plot, a house goes up brick by
 * brick: something made needs a maker), orchid (in the second, an orchid whose lip is shaped and coloured like a bee;
 * a bee comes to it and leaves with pollen on its head), care (in the third, a cloud rains on a field, the wheat rises
 * and a basket of bread is set before a seated man).
 */
internal fun DrawScope.panelsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "frames" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "made" to Shot(panelC23(w, h, 0) - Offset(0f, h * 0.04f), 1.7f),
        "orchid" to Shot(panelC23(w, h, 1) - Offset(0f, h * 0.04f), 1.7f),
        "care" to Shot(panelC23(w, h, 2) - Offset(0f, h * 0.04f), 1.7f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.25f) { panelsWall(a) }
}

private fun DrawScope.panelsWall(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fr = a.b("frames")
    val md = a.b("made")
    val oc = a.b("orchid")
    val ca = a.b("care")
    drawRect(PanC23.Wall)
    val pw = w * 0.27f
    val ph = h * 0.5f
    for (k in 0 until 3) {
        val c = panelC23(w, h, k)
        val o = easeOut(seg(fr, k * 0.15f, 0.35f + k * 0.15f))
        if (o <= 0f) continue
        val tl = c - Offset(pw / 2, ph / 2)
        drawRect(PanC23.Sky, tl, Size(pw, ph * 0.6f), alpha = o)
        drawRect(PanC23.Land, tl + Offset(0f, ph * 0.6f), Size(pw, ph * 0.4f), alpha = o)
        drawRect(PanC23.Frame, tl - Offset(w * 0.01f, w * 0.01f), Size(pw + w * 0.02f, ph + w * 0.02f), alpha = o, style = Stroke(w * 0.012f))
    }
    // 1. the house that went up: something made needs a maker
    if (md > 0f) {
        val c = panelC23(w, h, 0)
        val base = c + Offset(0f, ph * 0.28f)
        val rows = 5
        val built = md * 1.15f
        for (r in 0 until rows) for (m in 0 until 4) {
            val idx = r * 4 + m
            val f = easeOut(seg(built, idx / 20f, idx / 20f + 0.08f))
            if (f <= 0f) continue
            val bx = base.x - w * 0.06f + m * w * 0.03f + (if (r % 2 == 1) w * 0.015f else 0f)
            val by = base.y - (r + 1) * h * 0.035f - h * 0.05f * (1f - f)
            drawRect(PanC23.Brick, Offset(bx, by), Size(w * 0.028f, h * 0.032f), alpha = f)
        }
        val roof = easeOut(seg(md, 0.85f, 1f))
        if (roof > 0f) drawPath(Path().apply {
            moveTo(base.x - w * 0.075f, base.y - rows * h * 0.035f); lineTo(base.x + w * 0.02f, base.y - rows * h * 0.035f - h * 0.07f * roof); lineTo(base.x + w * 0.11f, base.y - rows * h * 0.035f); close()
        }, Color(0xFF8A3A2A), alpha = roof)
        // the tools left beside it
        drawLine(Color(0xFF5A4030), base + Offset(w * 0.1f, 0f), base + Offset(w * 0.08f, -h * 0.06f), 2f * dp, alpha = seg(md, 0.2f, 0.4f))
    }
    // 2. the orchid shaped like a bee
    if (oc > 0f) {
        val c = panelC23(w, h, 1)
        val stem = c + Offset(0f, ph * 0.3f)
        val bloomC = c + Offset(0f, -ph * 0.02f)
        val grow = easeOut(seg(oc, 0f, 0.3f))
        drawLine(Color(0xFF4A8A3A), stem, stem + (bloomC - stem) * grow, 2.5f * dp, StrokeCap.Round)
        if (grow > 0.8f) {
            for (k in 0 until 3) {
                val ang = -PI.toFloat() / 2 + (k - 1) * 1.1f
                val p = bloomC + Offset(cos(ang), sin(ang)) * (w * 0.03f)
                drawOval(PanC23.Petal, Offset(p.x - w * 0.022f, p.y - h * 0.025f), Size(w * 0.044f, h * 0.05f))
            }
            // the lip: a bee's body, striped
            drawOval(PanC23.PetalDark, Offset(bloomC.x - w * 0.018f, bloomC.y - h * 0.01f), Size(w * 0.036f, h * 0.06f))
            for (s in 0 until 3) drawLine(PanC23.Bee, Offset(bloomC.x - w * 0.014f, bloomC.y + h * (0.005f + s * 0.015f)), Offset(bloomC.x + w * 0.014f, bloomC.y + h * (0.005f + s * 0.015f)), 1.6f * dp, alpha = 0.8f)
        }
        // the bee comes, lands, leaves with pollen
        val fly = seg(oc, 0.3f, 1f)
        if (fly > 0f) {
            val land = easeInOut(seg(fly, 0f, 0.45f))
            val leave = easeInOut(seg(fly, 0.65f, 1f))
            val p = Offset(mix(c.x + pw * 0.45f, bloomC.x, land) - pw * 0.4f * leave, mix(c.y - ph * 0.35f, bloomC.y - h * 0.02f, land) - ph * 0.3f * leave) + Offset(0f, sin(t * 20f) * h * 0.004f)
            drawOval(PanC23.Bee, Offset(p.x - w * 0.012f, p.y - h * 0.01f), Size(w * 0.024f, h * 0.02f))
            drawLine(PanC23.Ink, p + Offset(-w * 0.004f, -h * 0.01f), p + Offset(-w * 0.004f, h * 0.01f), 1.2f * dp)
            val wing = 0.5f + 0.5f * sin(t * 40f)
            drawOval(Color.White, Offset(p.x - w * 0.006f, p.y - h * (0.02f + 0.008f * wing)), Size(w * 0.014f, h * 0.014f), alpha = 0.7f)
            if (leave > 0f) drawCircle(Color(0xFFFFE070), w * 0.005f, p + Offset(-w * 0.012f, -h * 0.008f))
        }
    }
    // 3. care: rain, wheat, bread before a man
    if (ca > 0f) {
        val c = panelC23(w, h, 2)
        val cloud = c + Offset(0f, -ph * 0.2f)
        val o = easeOut(seg(ca, 0f, 0.2f))
        for (k in 0 until 3) drawCircle(PanC23.Cloud, w * (0.03f + 0.008f * (k % 2)), cloud + Offset(w * (-0.035f + 0.035f * k), 0f), alpha = o)
        val rain = seg(ca, 0.1f, 0.6f)
        for (k in 0 until 10) {
            val f = fract(t * 1.6f + k * 0.13f)
            val x = cloud.x + w * (-0.06f + 0.012f * k)
            drawLine(PanC23.Rain, Offset(x, cloud.y + h * 0.02f + f * ph * 0.45f), Offset(x - w * 0.003f, cloud.y + h * 0.04f + f * ph * 0.45f), 1.4f * dp, alpha = 0.8f * bump(rain).coerceAtLeast(if (rain in 0.3f..0.9f) 0.6f else 0f))
        }
        val rise = easeOut(seg(ca, 0.35f, 0.8f))
        for (k in 0 until 9) {
            val x = c.x - pw * 0.4f + k * pw * 0.1f
            val y0 = c.y + ph * 0.42f
            drawLine(Color(0xFFC8A040), Offset(x, y0), Offset(x, y0 - h * 0.08f * rise), 1.6f * dp, StrokeCap.Round)
            if (rise > 0.6f) drawOval(Color(0xFFD8B050), Offset(x - w * 0.004f, y0 - h * 0.1f * rise), Size(w * 0.008f, h * 0.025f))
        }
        val give = easeOut(seg(ca, 0.7f, 1f))
        val seat = c + Offset(pw * 0.28f, ph * 0.36f)
        w8Seated(seat, h * 0.14f, PanC23.Ink, -1f, o)
        if (give > 0f) {
            val basket = seat + Offset(-w * 0.05f, -h * 0.01f)
            drawArc(Color(0xFF8A5A2A), 0f, 180f, true, basket + Offset(-w * 0.025f, -h * 0.025f), Size(w * 0.05f, h * 0.05f), alpha = give)
            for (k in 0 until 3) drawOval(PanC23.Bread, Offset(basket.x - w * 0.02f + k * w * 0.013f, basket.y - h * 0.02f), Size(w * 0.016f, h * 0.018f), alpha = give)
            softGlow(basket, w * 0.04f, ArtInk.Gold, 0.4f * give)
        }
    }
    // the names over the windows, drawn last; while the camera is on one window the others' names step aside
    val focus = when { ca > 0f -> 2; oc > 0f -> 1; md > 0f -> 0; else -> -1 }
    val into = when (focus) { 2 -> ca; 1 -> oc; 0 -> md; else -> 0f }
    for (k in 0 until 3) {
        val o = easeOut(seg(fr, k * 0.15f, 0.35f + k * 0.15f))
        val keep = if (focus < 0 || focus == k) 1f else 1f - seg(into, 0f, 0.12f)
        tagAt(a.tag("frames", 0, k), panelC23(w, h, k) + Offset(0f, -ph / 2 - h * 0.06f), o * keep, back = PanC23.Back, ink = ArtInk.BrassLight)
    }
}

// ═════════════ 2. thrift and plenty; the star that forged iron (bs-dakhama) ═════════════

private object ForgeC23 {
    val Room = Color(0xFF3A2E28)
    val RoomLow = Color(0xFF241C18)
    val Table = Color(0xFF6A4A2A)
    val Coin = Color(0xFFE2BC5A)
    val Ink = Color(0xFF12101A)
    val Star = Color(0xFFFFB04A)
    val Iron = Color(0xFFFF7A3A)
    val Blood = Color(0xFFC8303A)
    val Earth = Color(0xFF3A7AC8)
    val Back = Color(0xE6201810)
}

/**
 * Beats: thrift (in a dim room a man counts his few coins and lays a short row of bricks — he spends as little as he
 * can), vast (the room falls away: the sky beyond, where galaxies keep appearing out to every edge without end),
 * forgeiron (a great star burns; out of its furnace comes a glowing grain, «الْحَدِيدِ», which travels down to the
 * earth and into the red cells of a man's blood).
 */
internal fun DrawScope.forgeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "thrift" to Shot(Offset(w * 0.5f, h * 0.62f), 1.5f),
        "vast" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "forgeiron" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { forgeSky(a) }
}

private fun DrawScope.forgeSky(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val th = a.b("thrift")
    val va = a.b("vast")
    val fi = a.b("forgeiron")
    drawRect(Brush.verticalGradient(listOf(Color(0xFF05060F), Color(0xFF141A38), Color(0xFF2A1E4A))))
    // the sky without end: galaxies keep appearing
    val sky = easeOut(seg(va, 0f, 0.4f)).coerceAtLeast(if (fi > 0f) 1f else 0f)
    if (sky > 0f) {
        for (k in 0 until 90) {
            val x = fract(sin(k * 12.9898f) * 43758.5f)
            val y = fract(sin(k * 78.233f) * 12345.6f)
            drawCircle(Color.White, (0.6f + (k % 3) * 0.5f) * dp, Offset(w * x, h * y), alpha = sky * (0.3f + 0.3f * sin(t + k)))
        }
        val many = seg(va, 0.1f, 1f).coerceAtLeast(if (fi > 0f) 1f else 0f)
        for (k in 0 until 16) {
            val f = easeOut(seg(many, k / 16f, k / 16f + 0.15f))
            if (f <= 0f) continue
            val x = fract(k * 0.618f + 0.1f)
            val y = fract(k * 0.382f + 0.2f)
            galaxy22(Offset(w * (0.05f + 0.9f * x), h * (0.08f + 0.6f * y)), w * (0.03f + 0.02f * (k % 3)), t * 0.2f + k, f * 0.8f * (1f - 0.5f * easeOut(seg(fi, 0f, 0.3f))))
        }
    }
    // the room of the careful spender
    val room = 1f - easeInOut(seg(va, 0f, 0.4f))
    if (room > 0f) {
        drawRect(Brush.verticalGradient(listOf(ForgeC23.Room, ForgeC23.RoomLow)), alpha = room)
        softGlow(Offset(w * 0.5f, h * 0.45f), w * 0.25f, Color(0xFFFFD28A), 0.4f * room)
        val table = Offset(w * 0.5f, h * 0.7f)
        drawRect(ForgeC23.Table, Offset(table.x - w * 0.15f, table.y), Size(w * 0.3f, h * 0.025f), alpha = room)
        w8Seated(Offset(w * 0.68f, h * 0.84f), h * 0.22f, ForgeC23.Ink, 1f, room)
        val n = 5
        for (k in 0 until n) {
            val f = easeOut(seg(th, 0.1f + k * 0.08f, 0.2f + k * 0.08f))
            drawOval(ForgeC23.Coin, Offset(table.x - w * 0.02f + k * w * 0.012f, table.y - h * 0.012f - k * h * 0.006f), Size(w * 0.025f, h * 0.012f), alpha = f * room)
        }
        // a short row of bricks, no more than he needs
        for (k in 0 until 4) {
            val f = easeOut(seg(th, 0.55f + k * 0.08f, 0.65f + k * 0.08f))
            drawRect(Color(0xFFC08A5E), Offset(w * 0.2f + k * w * 0.035f, h * 0.82f), Size(w * 0.032f, h * 0.03f), alpha = f * room)
        }
    }
    // the star's furnace and the iron in our blood
    if (fi > 0f) {
        val star = Offset(w * 0.22f, h * 0.25f)
        val so = easeOut(seg(fi, 0f, 0.25f))
        bloom(star, w * 0.05f * (1f + 0.05f * sin(t * 3f)), ForgeC23.Star, 1.2f * so)
        for (k in 0 until 10) {
            val ang = t * 0.6f + k * 0.63f
            drawLine(ForgeC23.Star, star + Offset(cos(ang), sin(ang)) * (w * 0.05f), star + Offset(cos(ang), sin(ang)) * (w * 0.075f), 2f * dp, StrokeCap.Round, alpha = 0.5f * so)
        }
        val earth = Offset(w * 0.62f, h * 0.62f)
        drawCircle(ForgeC23.Earth, w * 0.06f, earth, alpha = so)
        drawCircle(Color(0xFF6AB06A), w * 0.025f, earth + Offset(-w * 0.015f, -w * 0.01f), alpha = so)
        // the man, and a few red cells in him
        val man = Offset(w * 0.86f, h * 0.88f)
        figure(man, h * 0.26f, Color(0xFFDCD4E8), so)
        val go = easeInOut(seg(fi, 0.25f, 0.85f))
        val path = { f: Float -> cubic(star, Offset(w * 0.45f, h * 0.05f), Offset(w * 0.75f, h * 0.35f), man + Offset(0f, -h * 0.16f), f) }
        for (k in 0 until 12) {
            val f = k / 12f
            if (f > go) break
            drawCircle(ForgeC23.Iron, w * 0.003f, path(f), alpha = 0.6f)
        }
        val grain = path(go)
        bloom(grain, w * 0.008f, ForgeC23.Iron, so)
        tagAt(a.tag("forgeiron"), grain + Offset(0f, -h * 0.07f), seg(fi, 0.3f, 0.5f), back = ForgeC23.Back, ink = ArtInk.BrassLight)
        val cells = easeOut(seg(fi, 0.85f, 1f))
        for (k in 0 until 3) {
            val c = man + Offset(w * (-0.01f + 0.01f * k), -h * (0.14f + 0.02f * (k % 2)))
            drawOval(ForgeC23.Blood, Offset(c.x - w * 0.009f, c.y - h * 0.008f), Size(w * 0.018f, h * 0.016f), alpha = cells)
        }
    }
}

// ═════════════ 3. a child outweighs the mountains; the trust (bs-markaz) ═════════════

private object ScaleC23 {
    val SkyTop = Color(0xFF0B1230)
    val SkyLow = Color(0xFF2A3466)
    val Hill = Color(0xFF14182A)
    val Brass = Color(0xFFC8A050)
    val Mount = Color(0xFF5A6478)
    val Ink = Color(0xFFDCD4E8)
    val Back = Color(0xE60A0E20)
}

/**
 * Beats: tiny (under a vast sky with a turning galaxy, a very small man on a hill; the camera closes in on him), balance
 * (a great balance: mountains on one pan, a small child on the other — and the child's pan goes down), trust (the
 * mountains behind fall dark and draw back; the man lifts a chest of light, «الْأَمَانَةُ», and its light falls on him).
 */
internal fun DrawScope.scalesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ti = a.b("tiny")
    val shots = shotsFor(a, mapOf(
        "tiny" to Shot(Offset(w * 0.5f, mix(h * 0.5f, h * 0.75f, easeInOut(seg(ti, 0.4f, 1f)))), mix(1f, 2.2f, easeInOut(seg(ti, 0.4f, 1f)))),
        "balance" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "trust" to Shot(Offset(w * 0.5f, h * 0.6f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { scalesNight(a) }
}

private fun DrawScope.scalesNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ti = a.b("tiny")
    val ba = a.b("balance")
    val tr = a.b("trust")
    drawRect(Brush.verticalGradient(listOf(ScaleC23.SkyTop, ScaleC23.SkyLow)))
    for (k in 0 until 70) {
        val x = fract(sin(k * 12.9898f) * 43758.5f)
        val y = fract(sin(k * 78.233f) * 12345.6f)
        drawCircle(Color.White, (0.6f + (k % 3) * 0.4f) * dp, Offset(w * x, h * 0.7f * y), alpha = 0.3f + 0.3f * sin(t + k))
    }
    val inBalance = easeOut(seg(ba, 0f, 0.3f)) * (1f - easeOut(seg(tr, 0f, 0.3f)))
    galaxy22(Offset(w * 0.3f, h * 0.28f), w * 0.2f, t * 0.1f, 0.8f * (1f - inBalance))
    // the hill and the small man on it
    drawPath(Path().apply { moveTo(0f, h * 0.85f); cubicTo(w * 0.3f, h * 0.76f, w * 0.7f, h * 0.76f, w, h * 0.85f); lineTo(w, h); lineTo(0f, h); close() }, ScaleC23.Hill)
    if (inBalance < 1f) {
        val man = Offset(w * 0.5f, h * 0.79f)
        figure(man, h * 0.06f, ScaleC23.Ink, (1f - inBalance) * easeOut(seg(ti, 0f, 0.3f)).coerceAtLeast(if (tr > 0f) 1f else 0f))
    }
    // the balance: mountains against a child
    if (inBalance > 0f) {
        val pivot = Offset(w * 0.5f, h * 0.22f)
        val tilt = easeInOut(seg(ba, 0.35f, 0.85f))
        val ang = -12f * tilt   // the child's side (left) goes down
        drawLine(ScaleC23.Brass, pivot, Offset(pivot.x, h * 0.85f), 3f * dp, alpha = inBalance)
        withTransform({ rotate(ang, pivot) }) {
            drawLine(ScaleC23.Brass, pivot + Offset(-w * 0.3f, 0f), pivot + Offset(w * 0.3f, 0f), 3f * dp, StrokeCap.Round, alpha = inBalance)
        }
        val rad = ang * PI.toFloat() / 180f
        for (side in listOf(-1f, 1f)) {
            val end = pivot + Offset(side * w * 0.3f * cos(rad), side * w * 0.3f * sin(rad))
            val pan = end + Offset(0f, h * 0.3f)
            drawLine(ScaleC23.Brass, end, pan + Offset(-w * 0.08f, 0f), 1.2f * dp, alpha = inBalance)
            drawLine(ScaleC23.Brass, end, pan + Offset(w * 0.08f, 0f), 1.2f * dp, alpha = inBalance)
            drawArc(ScaleC23.Brass, 0f, 180f, true, pan + Offset(-w * 0.08f, -h * 0.03f), Size(w * 0.16f, h * 0.06f), alpha = inBalance)
            if (side > 0f) {
                // mountains on the right pan
                drawPath(Path().apply {
                    moveTo(pan.x - w * 0.075f, pan.y); lineTo(pan.x - w * 0.03f, pan.y - h * 0.14f); lineTo(pan.x, pan.y - h * 0.08f)
                    lineTo(pan.x + w * 0.035f, pan.y - h * 0.17f); lineTo(pan.x + w * 0.075f, pan.y); close()
                }, ScaleC23.Mount, alpha = inBalance)
                drawPath(Path().apply { moveTo(pan.x + w * 0.02f, pan.y - h * 0.13f); lineTo(pan.x + w * 0.035f, pan.y - h * 0.17f); lineTo(pan.x + w * 0.05f, pan.y - h * 0.13f); close() }, Color.White, alpha = inBalance)
            } else {
                // a small child on the left pan, a little light about it
                softGlow(pan + Offset(0f, -h * 0.05f), w * 0.05f, ArtInk.Gold, 0.5f * inBalance * tilt)
                figure(pan, h * 0.09f, ScaleC23.Ink, inBalance)
            }
        }
    }
    // the trust: mountains draw back, the man lifts a chest of light
    if (tr > 0f) {
        val f = easeOut(seg(tr, 0f, 0.35f))
        for (k in 0 until 3) {
            val back = easeInOut(seg(tr, 0.1f, 0.6f))
            val x = w * (0.15f + 0.35f * k)
            drawPath(Path().apply { moveTo(x - w * 0.15f, h * 0.8f); lineTo(x, h * (0.38f + 0.1f * back)); lineTo(x + w * 0.15f, h * 0.8f); close() }, ScaleC23.Mount, alpha = f * (0.7f - 0.45f * back))
        }
        val man = Offset(w * 0.5f, h * 0.84f)
        val lift = easeOut(seg(tr, 0.35f, 0.75f))
        figure(man, h * 0.24f, ScaleC23.Ink, f, arms = 0f)
        val chest = man + Offset(0f, -h * 0.26f - h * 0.06f * lift)
        drawLine(ScaleC23.Ink, man + Offset(-w * 0.012f, -h * 0.17f), chest + Offset(-w * 0.025f, h * 0.02f), 2f * dp, StrokeCap.Round, alpha = f)
        drawLine(ScaleC23.Ink, man + Offset(w * 0.012f, -h * 0.17f), chest + Offset(w * 0.025f, h * 0.02f), 2f * dp, StrokeCap.Round, alpha = f)
        softGlow(chest, w * 0.12f, ArtInk.Gold, 0.7f * lift)
        drawRoundRect(Color(0xFF8A5A2A), chest + Offset(-w * 0.035f, -h * 0.025f), Size(w * 0.07f, h * 0.045f), CornerRadius(w * 0.006f), alpha = f)
        drawRect(ArtInk.Gold, chest + Offset(-w * 0.035f, -h * 0.004f), Size(w * 0.07f, h * 0.006f), alpha = f)
        lightShafts(chest, PI.toFloat() / 2, 1.2f, h * 0.35f, ArtInk.Gold, 0.6f * lift, t)
        tagAt(a.tag("trust"), chest + Offset(0f, -h * 0.1f), seg(tr, 0.6f, 0.8f), back = ScaleC23.Back, ink = ArtInk.BrassLight)
    }
}

// ═════════════ 4. planets, a forest, a desert, a cell (bs-itqan) ═════════════

private object DesC23 {
    val Space = Color(0xFF0A0E22)
    val Forest = Color(0xFF2E5A34)
    val ForestLight = Color(0xFF4A8A4A)
    val Fruit = Color(0xFFE85A3A)
    val Sand = Color(0xFFE2C38A)
    val SandDark = Color(0xFFC8A060)
    val Plate = Color(0xFFF2EEE6)
    val Chip = Color(0xFF2A4A3A)
    val Cell = Color(0xFFF2E6F2)
    val Red = Color(0xFFE0604A)
    val Back = Color(0xE6201810)
}

/**
 * Beats: planets (many planets drift past in the dark; among them one blue, green and lit), forest (a forest heavy with
 * fruit, and in the middle an empty plate that stays empty: no cooked dish comes of the plenty), sandchip (desert dunes
 * blowing; a microchip tries to take shape out of the sand and never does), letters (inside a cell a long ribbon of
 * letters runs by, A T G C, in their exact order; one letter flips and the ribbon flashes red).
 */
internal fun DrawScope.desertScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "planets" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "forest" to Shot(Offset(w * 0.5f, h * 0.62f), 1.35f),
        "sandchip" to Shot(Offset(w * 0.5f, h * 0.6f), 1.35f),
        "letters" to Shot(Offset(w * 0.5f, h * 0.5f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { desertWorlds(a) }
}

private fun DrawScope.desertWorlds(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pl = a.b("planets")
    val fo = a.b("forest")
    val sc = a.b("sandchip")
    val le = a.b("letters")
    val stage = when {
        le > 0f -> 3
        sc > 0f -> 2
        fo > 0f -> 1
        else -> 0
    }
    when (stage) {
        0 -> {
            drawRect(DesC23.Space)
            for (k in 0 until 50) {
                val x = fract(sin(k * 12.9898f) * 43758.5f)
                val y = fract(sin(k * 78.233f) * 12345.6f)
                drawCircle(Color.White, 1f * dp, Offset(w * x, h * y), alpha = 0.5f)
            }
            val cols = listOf(Color(0xFFC88A5A), Color(0xFF8A8AA0), Color(0xFFD8C08A), Color(0xFFA06A4A), Color(0xFF7A9AB0), Color(0xFFB0A090))
            for (k in 0 until 6) {
                val f = easeOut(seg(pl, k * 0.07f, 0.3f + k * 0.07f))
                val drift = fract(t * 0.02f + k * 0.17f)
                val c = Offset(w * (1.1f - 1.2f * drift), h * (0.2f + 0.12f * k))
                val r = w * (0.025f + 0.012f * (k % 3))
                drawCircle(cols[k], r, c, alpha = f)
                drawCircle(Color.Black, r, c + Offset(r * 0.35f, 0f), alpha = 0.3f * f)
            }
            val earth = Offset(w * 0.5f, h * 0.55f)
            val e = easeOut(seg(pl, 0.4f, 0.8f))
            softGlow(earth, w * 0.12f, Color(0xFF6AB0FF), 0.4f * e)
            drawCircle(Color(0xFF2E6AC0), w * 0.07f, earth, alpha = e)
            drawCircle(Color(0xFF5AA060), w * 0.03f, earth + Offset(-w * 0.02f, -w * 0.015f), alpha = e)
            drawCircle(Color(0xFF5AA060), w * 0.02f, earth + Offset(w * 0.025f, w * 0.02f), alpha = e)
            drawCircle(Color.White, w * 0.07f, earth, alpha = 0.35f * e, style = Stroke(2f * dp))
        }
        1 -> {
            drawRect(Brush.verticalGradient(listOf(Color(0xFFBFE0F0), Color(0xFFEAF2E0))))
            drawRect(Color(0xFF6A8A4A), Offset(0f, h * 0.75f), Size(w, h * 0.25f))
            for (k in 0 until 9) {
                val x = w * (0.05f + 0.11f * k)
                val y = h * (0.72f + 0.04f * (k % 2))
                drawLine(Color(0xFF5A3A20), Offset(x, y), Offset(x, y - h * 0.15f), w * 0.012f)
                drawCircle(if (k % 2 == 0) DesC23.Forest else DesC23.ForestLight, w * 0.06f, Offset(x, y - h * 0.22f))
                for (m in 0 until 3) drawCircle(DesC23.Fruit, w * 0.008f, Offset(x + w * (-0.03f + 0.03f * m), y - h * (0.2f + 0.04f * (m % 2))))
            }
            val plate = Offset(w * 0.5f, h * 0.84f)
            val o = easeOut(seg(fo, 0.1f, 0.4f))
            drawOval(DesC23.Plate, Offset(plate.x - w * 0.08f, plate.y - h * 0.03f), Size(w * 0.16f, h * 0.06f), alpha = o)
            drawOval(Color(0xFFD8D0C4), Offset(plate.x - w * 0.05f, plate.y - h * 0.018f), Size(w * 0.1f, h * 0.036f), alpha = o)
            // the cooked dish that the plenty never makes: only its dashed outline flickers over the plate
            val want = easeOut(seg(fo, 0.25f, 0.5f)) * (0.35f + 0.65f * (0.5f + 0.5f * sin(t * 16f)) * (1f - seg(fo, 0.7f, 1f)))
            val dash = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))
            drawArc(Color(0xFF7A5A3A), 180f, 180f, false, Offset(plate.x - w * 0.05f, plate.y - h * 0.07f), Size(w * 0.1f, h * 0.1f), alpha = 0.7f * want, style = Stroke(2f * dp, pathEffect = dash))
            for (m in 0 until 3) {
                val x = plate.x + w * (-0.025f + 0.025f * m)
                drawPath(Path().apply { moveTo(x, plate.y - h * 0.08f); quadraticTo(x + w * 0.01f, plate.y - h * 0.1f, x, plate.y - h * 0.12f) }, Color(0xFF7A5A3A), alpha = 0.5f * want, style = Stroke(1.5f * dp, pathEffect = dash))
            }
            qmark21(plate + Offset(w * 0.1f, -h * 0.12f), h * 0.07f, Color(0xFF5A5060), seg(fo, 0.5f, 0.75f))
        }
        2 -> {
            drawRect(Brush.verticalGradient(listOf(Color(0xFFF2D8A8), Color(0xFFF6EAD0))))
            for (k in 0 until 3) {
                val y = h * (0.6f + 0.12f * k)
                drawPath(Path().apply {
                    moveTo(0f, y); for (m in 1..8) lineTo(w * m / 8f, y - sin(m * 1.3f + k) * h * 0.04f); lineTo(w, h); lineTo(0f, h); close()
                }, if (k % 2 == 0) DesC23.Sand else DesC23.SandDark)
            }
            for (k in 0 until 14) {
                val f = fract(t * 0.5f + k * 0.07f)
                drawCircle(DesC23.SandDark, 1.2f * dp, Offset(w * f, h * (0.55f + 0.03f * (k % 5))), alpha = 0.6f)
            }
            // the chip that never forms
            val c = Offset(w * 0.5f, h * 0.55f)
            val flick = easeOut(seg(sc, 0.15f, 0.4f)) * (0.35f + 0.65f * (0.5f + 0.5f * sin(t * 18f)) * (1f - seg(sc, 0.7f, 1f)))
            val dash = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp))
            drawRoundRect(DesC23.Chip, c + Offset(-w * 0.06f, -h * 0.07f), Size(w * 0.12f, h * 0.14f), CornerRadius(w * 0.006f), alpha = 0.6f * flick, style = Stroke(2f * dp, pathEffect = dash))
            for (k in 0 until 5) for (side in listOf(-1f, 1f)) drawLine(DesC23.Chip, c + Offset(side * w * 0.06f, -h * 0.05f + k * h * 0.025f), c + Offset(side * w * 0.075f, -h * 0.05f + k * h * 0.025f), 1.5f * dp, alpha = 0.6f * flick)
            qmark21(c + Offset(w * 0.12f, -h * 0.1f), h * 0.07f, Color(0xFF7A5A3A), seg(sc, 0.6f, 0.85f))
        }
        else -> {
            drawRect(Brush.radialGradient(listOf(Color(0xFF4A2A4A), Color(0xFF1A0E22)), Offset(w * 0.5f, h * 0.5f), w * 0.7f))
            val o = easeOut(seg(le, 0f, 0.25f))
            drawOval(DesC23.Cell, Offset(w * 0.08f, h * 0.12f), Size(w * 0.84f, h * 0.76f), alpha = 0.12f * o)
            drawOval(DesC23.Cell, Offset(w * 0.08f, h * 0.12f), Size(w * 0.84f, h * 0.76f), alpha = 0.5f * o, style = Stroke(2.5f * dp))
            // the ribbon of letters
            val y = h * 0.5f
            drawLine(Color(0xFF9A8AC8), Offset(w * 0.12f, y - h * 0.045f), Offset(w * 0.88f, y - h * 0.045f), 2f * dp, alpha = o)
            drawLine(Color(0xFF9A8AC8), Offset(w * 0.12f, y + h * 0.045f), Offset(w * 0.88f, y + h * 0.045f), 2f * dp, alpha = o)
            val flipAt = 7
            val bad = seg(le, 0.65f, 0.75f) > 0f
            val badFlash = bump(seg(le, 0.65f, 1f))
            val run = t * 0.06f
            for (k in 0 until 14) {
                val x = w * (0.14f + 0.055f * k)
                val tl = a.tag("letters", 0, (k * 7 + (run * 14).toInt()) % 4)
                if (tl != null) {
                    val wrong = bad && k == flipAt
                    val col = if (wrong) DesC23.Red else Color(0xFFE8E0FF)
                    drawText(tl, color = col, topLeft = Offset(x - tl.size.width / 2f, y - tl.size.height / 2f), alpha = o)
                }
            }
            if (badFlash > 0f) {
                drawOval(DesC23.Red, Offset(w * 0.08f, h * 0.12f), Size(w * 0.84f, h * 0.76f), alpha = 0.6f * badFlash, style = Stroke(5f * dp))
                drawCircle(DesC23.Red, w * 0.03f, Offset(w * (0.14f + 0.055f * flipAt), y), alpha = 0.8f * badFlash, style = Stroke(2f * dp))
            }
        }
    }
}
