package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/*
 * Wave 10, round 3 of the redraw (2026-10-21): «ترك الجدال والأهواء». A sitting room by day (two who quarrel
 * strike sparks of doubt; the one who left them brings the Book and the Sunna), a lane in the afternoon (the
 * one who asks is let in and taught; the one who comes to debate is kept at the door; two in a tug of war both
 * leave the path), a hilltop from dusk to night (thinking about the Lord fogs the mind; the verse clears it;
 * then all of creation to think about), a roadside at noon (a sinner on the Sunna, whose clear water stays
 * clear; a worshipper of desire, whose murky words are not listened to).
 */

// ───────────── helpers ─────────────

/** A small heart on a figure's chest; [smoke] 0..1 sends grey smoke of doubt up from it. */
private fun DrawScope.chestHeart10(foot: Offset, tall: Float, smoke: Float, t: Float, lean: Float = 0f, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    val c = foot + Offset(-sin(lean) * tall * 0.5f, -tall * 0.62f)
    val col = lerpColor(Color(0xFFF0B8A8), Color(0xFF7A7480), smoke)
    drawPath(w8Heart(c, tall * 0.06f), col, alpha = alpha)
    if (smoke > 0.05f) for (k in 0 until 6) {
        val f = fract(t * 0.5f + k / 6f)
        val p = c + Offset(sin(f * 7f + k) * tall * 0.06f, -tall * 0.5f * f)
        drawCircle(Color(0xFF6E6876), tall * (0.04f + 0.06f * f), p, alpha = 0.45f * smoke * (1f - f) * alpha)
    }
}

/** A closed book standing up, [s] tall, with a gold frame on its cover. */
private fun DrawScope.book10(c: Offset, s: Float, cover: Color, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    val bw = s * 0.72f
    drawRoundRect(Color.Black, topLeft = c - Offset(bw / 2 - 2f * density, s / 2 - 3f * density), size = Size(bw, s), cornerRadius = CornerRadius(2f * density), alpha = 0.25f * alpha)
    drawRoundRect(cover, topLeft = c - Offset(bw / 2, s / 2), size = Size(bw, s), cornerRadius = CornerRadius(2f * density), alpha = alpha)
    drawRect(ArtInk.Gold, topLeft = c - Offset(bw * 0.38f, s * 0.4f), size = Size(bw * 0.76f, s * 0.8f), alpha = 0.9f * alpha, style = Stroke(1f * density))
    drawPath(starPath(c, bw * 0.18f), ArtInk.Gold, alpha = alpha)
    drawRect(Color.Black, topLeft = c - Offset(bw / 2, s / 2), size = Size(bw * 0.1f, s), alpha = 0.25f * alpha)
}

/** An open book lying flat, [s] wide, its pages glowing by [glow]. */
private fun DrawScope.openBook10(c: Offset, s: Float, glow: Float, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    if (glow > 0f) softGlow(c + Offset(0f, -s * 0.15f), s * 1.1f, Color(0xFFFFF0C8), 0.7f * glow * alpha)
    for (sd in listOf(-1f, 1f)) {
        val page = Path().apply {
            moveTo(c.x, c.y)
            quadraticBezierTo(c.x + sd * s * 0.25f, c.y - s * 0.1f, c.x + sd * s * 0.5f, c.y - s * 0.04f)
            lineTo(c.x + sd * s * 0.5f, c.y + s * 0.08f)
            quadraticBezierTo(c.x + sd * s * 0.25f, c.y + s * 0.02f, c.x, c.y + s * 0.1f)
            close()
        }
        drawPath(page, Color(0xFFF8EED6), alpha = alpha)
        drawPath(page, Color(0xFF9A7A48), alpha = alpha, style = Stroke(0.8f * density))
        for (r in 0 until 3) drawLine(Color(0xFF9A8A6A), Offset(c.x + sd * s * 0.08f, c.y - s * 0.01f + r * s * 0.03f), Offset(c.x + sd * s * 0.42f, c.y - s * 0.03f + r * s * 0.03f), 0.6f * density, alpha = 0.7f * alpha)
    }
}

/** A glass flask of water: [murk] 0 clear and lit, 1 dark and clouded. */
private fun DrawScope.flask10(base: Offset, s: Float, murk: Float, t: Float, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    val body = Path().apply {
        moveTo(base.x - s * 0.12f, base.y - s)
        lineTo(base.x - s * 0.12f, base.y - s * 0.72f)
        quadraticBezierTo(base.x - s * 0.42f, base.y - s * 0.55f, base.x - s * 0.4f, base.y - s * 0.2f)
        quadraticBezierTo(base.x - s * 0.38f, base.y, base.x, base.y)
        quadraticBezierTo(base.x + s * 0.38f, base.y, base.x + s * 0.4f, base.y - s * 0.2f)
        quadraticBezierTo(base.x + s * 0.42f, base.y - s * 0.55f, base.x + s * 0.12f, base.y - s * 0.72f)
        lineTo(base.x + s * 0.12f, base.y - s)
        close()
    }
    val water = lerpColor(Color(0xFF9ED8F2), Color(0xFF3A3028), murk)
    if (murk < 0.5f) softGlow(base + Offset(0f, -s * 0.35f), s * 0.9f, Color(0xFFCFF0FF), 0.6f * (1f - 2f * murk) * alpha)
    clipRect(base.x - s * 0.5f, base.y - s * 0.6f, base.x + s * 0.5f, base.y) { drawPath(body, water, alpha = 0.9f * alpha) }
    if (murk > 0.3f) for (k in 0 until 4) {
        val ang = t * 1.2f + k * 1.6f
        drawCircle(Color(0xFF1A1410), s * 0.08f, base + Offset(cos(ang) * s * 0.18f, -s * 0.25f + sin(ang) * s * 0.1f), alpha = 0.6f * murk * alpha)
    }
    drawPath(body, Color.White, alpha = 0.35f * alpha)
    drawPath(body, Color(0xFF6A7A88), alpha = alpha, style = Stroke(1.2f * density))
    drawRect(Color(0xFF8A6A48), topLeft = Offset(base.x - s * 0.13f, base.y - s * 1.08f), size = Size(s * 0.26f, s * 0.1f), alpha = alpha)
    if (murk < 0.3f) glint(base + Offset(-s * 0.15f, -s * 0.45f), 4f * density, Color.White, 0.8f * alpha)
}

/** Sharp red marks of anger from [at], flying toward [dir] (1 right, -1 left). */
private fun DrawScope.anger10(at: Offset, dir: Float, s: Float, t: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 3) {
        val f = fract(t * 1.6f + k / 3f)
        val o = at + Offset(dir * s * (0.1f + 0.9f * f), -s * 0.15f * (k - 1))
        val zz = Path().apply { moveTo(o.x, o.y); lineTo(o.x + dir * s * 0.12f, o.y - s * 0.12f); lineTo(o.x + dir * s * 0.24f, o.y); lineTo(o.x + dir * s * 0.36f, o.y - s * 0.12f) }
        drawPath(zz, Color(0xFFD0402E), alpha = alpha * (1f - f), style = Stroke(1.8f * density, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

// ═════════════ 1. majlis: sparks of a quarrel, and the Book and the Sunna (tark-mira) ═════════════

private object MajlisC10 {
    val Wall = Color(0xFFEDE3CF)
    val WallLow = Color(0xFFD9C8A6)
    val Arch = Color(0xFFC9AE7C)
    val Window = Color(0xFFFFF6DC)
    val Floor = Color(0xFF9A6A48)
    val Rug = Color(0xFF7A2E34)
    val RugLine = Color(0xFFD9A75A)
    val Cushion = Color(0xFF2F5A6A)
    val Wood = Color(0xFF6A4428)
    val Ink = Color(0xFF2A2026)
    val Quran = Color(0xFF1F5A44)
    val Sunna = Color(0xFF6A2430)
    val Spark = Color(0xFFFFC85A)
    val Back = Color(0xE6302820)
}

/**
 * A sitting room by day. Beats: quarrel (two men argue, pointing in turn, red marks flying; a third turns away
 * from them and goes), tawqif (he sits at a lectern with two books, named, in the window's light), flint (the two
 * clash: sparks fly from their hands like flint on steel and fall on their hearts, which smoke with doubt), refer
 * (he brings the two books and sets them between them; they lower their hands and look), settle (they sit down
 * before the books; the smoke clears).
 */
internal fun DrawScope.majlisScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "quarrel" to Shot(Offset(w * 0.46f, h * 0.55f), 1.2f),
        "tawqif" to Shot(Offset(w * 0.8f, h * 0.56f), 1.5f),
        "flint" to Shot(Offset(w * 0.38f, h * 0.5f), 1.5f),
        "refer" to Shot(Offset(w * 0.47f, h * 0.56f), 1.2f),
        "settle" to Shot(Offset(w * 0.4f, h * 0.62f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { majlisRoom(a) }
}

private fun DrawScope.majlisRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val qr = a.b("quarrel")
    val tq = a.b("tawqif")
    val fl = a.b("flint")
    val rf = a.b("refer")
    val sl = a.b("settle")
    val floorY = h * 0.8f
    drawRect(Brush.verticalGradient(listOf(MajlisC10.Wall, MajlisC10.WallLow), endY = floorY))
    for (k in 0 until 4) {
        val x = w * (0.14f + 0.24f * k)
        drawPath(horseshoe(x, h * 0.28f, w * 0.06f, w * 0.05f, h * 0.58f), MajlisC10.Window)
        drawPath(horseshoe(x, h * 0.28f, w * 0.06f, w * 0.05f, h * 0.58f), MajlisC10.Arch, style = Stroke(2f * dp))
        lightShafts(Offset(x, h * 0.35f), PI.toFloat() * 0.6f, 0.2f, h * 0.6f, Color(0xFFFFF4D6), 0.5f, t, 3)
    }
    drawRect(MajlisC10.Floor, topLeft = Offset(0f, floorY), size = Size(w, h - floorY))
    drawRect(MajlisC10.Rug, topLeft = Offset(w * 0.08f, floorY + h * 0.03f), size = Size(w * 0.8f, h * 0.13f))
    drawRect(MajlisC10.RugLine, topLeft = Offset(w * 0.09f, floorY + h * 0.04f), size = Size(w * 0.78f, h * 0.11f), alpha = 0.7f, style = Stroke(1f * dp))
    for (k in 0 until 6) drawRoundRect(MajlisC10.Cushion, topLeft = Offset(w * (0.02f + 0.16f * k), floorY - h * 0.045f), size = Size(w * 0.13f, h * 0.05f), cornerRadius = CornerRadius(6f * dp), alpha = 0.85f)

    val tall = h * 0.36f
    // the lectern on the right, and the two books
    val lect = Offset(w * 0.86f, floorY)
    drawLine(MajlisC10.Wood, lect + Offset(-w * 0.03f, 0f), lect + Offset(w * 0.02f, -h * 0.12f), 2.4f * dp, StrokeCap.Round)
    drawLine(MajlisC10.Wood, lect + Offset(w * 0.03f, 0f), lect + Offset(-w * 0.02f, -h * 0.12f), 2.4f * dp, StrokeCap.Round)
    val carry = easeInOut(seg(rf, 0.15f, 0.6f))
    val booksAt = Offset(mix(lect.x, w * 0.38f, carry), mix(floorY - h * 0.16f, floorY - h * 0.14f, carry)) + Offset(0f, -h * 0.06f * bump(carry))
    val placed = rf >= 0.6f || sl > 0f
    if (placed) {
        // a low stand between the two men
        drawRect(MajlisC10.Wood, topLeft = Offset(w * 0.35f, floorY - h * 0.1f), size = Size(w * 0.06f, h * 0.1f))
    }
    val bookA = if (placed) Offset(w * 0.365f, floorY - h * 0.15f) else booksAt + Offset(-w * 0.018f, 0f)
    val bookB = if (placed) Offset(w * 0.395f, floorY - h * 0.15f) else booksAt + Offset(w * 0.018f, 0f)
    book10(bookA, h * 0.09f, MajlisC10.Quran)
    book10(bookB, h * 0.09f, MajlisC10.Sunna)
    val named = seg(tq, 0.25f, 0.45f) * (1f - 0.6f * seg(fl, 0f, 0.2f)) + (if (rf > 0.6f) seg(rf, 0.6f, 0.8f) else 0f)
    tagAt(a.tag("tawqif", 0, 0), bookA + Offset(-w * 0.07f, -h * 0.09f), named.coerceIn(0f, 1f), back = MajlisC10.Back, ink = ArtInk.Ivory)
    tagAt(a.tag("tawqif", 0, 1), bookB + Offset(w * 0.07f, -h * 0.17f), named.coerceIn(0f, 1f), back = MajlisC10.Back, ink = ArtInk.Ivory)
    if (tq > 0f && rf < 0.15f) softGlow(lect + Offset(0f, -h * 0.17f), w * 0.08f, Color(0xFFFFF0C8), 0.5f * seg(tq, 0.2f, 0.5f))

    // the two who quarrel
    val fa = Offset(w * 0.28f, floorY + h * 0.05f)
    val fb = Offset(w * 0.5f, floorY + h * 0.05f)
    val calmDown = easeOut(seg(rf, 0.55f, 0.9f))
    val sitDown = easeInOut(seg(sl, 0f, 0.5f))
    val argue = (if (qr > 0f) 1f else 0f) * (1f - calmDown)
    val ta = 0.5f + 0.5f * sin(t * 4f)
    val tb = 0.5f + 0.5f * sin(t * 4f + PI.toFloat())
    val clash = bump(seg(fl, 0.1f, 0.5f)) + bump(seg(fl, 0.45f, 0.85f))
    val smoke = easeOut(seg(fl, 0.35f, 0.8f)) * (1f - easeOut(seg(sl, 0.2f, 0.9f)))
    if (sitDown < 0.5f) {
        withTransform({ scale(-1f, 1f, pivot = fa) }) {
            figure(fa, tall, MajlisC10.Ink, lean = -0.06f * argue * ta - 0.1f * calmDown, arms = (0.4f + 0.6f * ta) * argue + 0.9f * clash.coerceIn(0f, 1f) * (1f - calmDown) + 0.2f * calmDown)
        }
        figure(fb, tall, MajlisC10.Ink, lean = 0.06f * argue * tb + 0.1f * calmDown, arms = (0.4f + 0.6f * tb) * argue + 0.9f * clash.coerceIn(0f, 1f) * (1f - calmDown) + 0.2f * calmDown)
        anger10(fa + Offset(tall * 0.32f, -tall * 0.72f), 1f, tall * 0.4f, t, argue * ta * (1f - seg(fl, 0f, 0.1f)) * (if (fl >= 1f) 0f else 1f) + argue * ta * seg(fl, 0.9f, 1f))
        anger10(fb + Offset(-tall * 0.32f, -tall * 0.72f), -1f, tall * 0.4f, t, argue * tb * (1f - seg(fl, 0f, 0.1f)))
        chestHeart10(fa, tall, smoke, t, alpha = seg(fl, 0.25f, 0.4f))
        chestHeart10(fb, tall, smoke, t, alpha = seg(fl, 0.25f, 0.4f))
    } else {
        val s2 = (sitDown - 0.5f) * 2f
        w8Seated(Offset(w * 0.3f, floorY + h * 0.03f), tall * 0.95f, MajlisC10.Ink, facing = 1f, alpha = s2)
        w8Seated(Offset(w * 0.46f, floorY + h * 0.03f), tall * 0.95f, MajlisC10.Ink, facing = -1f, alpha = s2)
        chestHeart10(Offset(w * 0.3f, floorY + h * 0.03f), tall * 0.62f, smoke, t, alpha = s2)
        chestHeart10(Offset(w * 0.46f, floorY + h * 0.03f), tall * 0.62f, smoke, t, alpha = s2)
        softGlow(Offset(w * 0.38f, floorY - h * 0.15f), w * 0.12f, Color(0xFFFFF0C8), 0.5f * s2)
    }
    // flint: where their hands meet, sparks, flying down onto their hearts
    if (fl > 0f && fl < 1f) {
        val hit = Offset(w * 0.39f, floorY + h * 0.05f - tall * 0.74f)
        val burst = clash.coerceIn(0f, 1f)
        if (burst > 0.3f) {
            softGlow(hit, w * 0.05f * burst, MajlisC10.Spark, 0.9f * burst)
            for (k in 0 until 10) {
                val ang = k * 0.63f + t
                drawLine(MajlisC10.Spark, hit, hit + Offset(cos(ang) * w * 0.04f * burst, sin(ang) * w * 0.04f * burst), 1.4f * dp, StrokeCap.Round, alpha = burst)
            }
        }
        for (k in 0 until 8) {
            val f = fract(t * 0.9f + k / 8f)
            val to = (if (k % 2 == 0) fa else fb) + Offset(0f, -tall * 0.62f)
            val p = hit + (to - hit) * f + Offset(0f, -h * 0.05f * bump(f))
            drawCircle(MajlisC10.Spark, 1.8f * dp, p, alpha = seg(fl, 0.15f, 0.3f) * (1f - f * 0.6f))
        }
    }

    // the one who left them: he turns away and goes to the lectern; later he brings the books
    val leave = easeInOut(seg(qr, 0.35f, 0.95f))
    val bring = easeInOut(seg(rf, 0f, 0.6f))
    val sitAtLect = easeOut(seg(tq, 0f, 0.25f)) * (1f - easeOut(seg(rf, 0f, 0.15f)))
    val cx = when {
        rf > 0f -> mix(w * 0.79f, w * 0.6f, bring)
        else -> mix(w * 0.64f, w * 0.79f, leave)
    }
    val cfoot = Offset(cx, floorY + h * 0.05f)
    if (sitAtLect > 0.5f) {
        w8Seated(Offset(w * 0.79f, floorY + h * 0.02f), tall * 0.95f, MajlisC10.Ink, facing = 1f)
        chestHeart10(Offset(w * 0.79f, floorY + h * 0.02f), tall * 0.62f, 0f, t)
    } else if (qr > 0f) {
        val moving = (qr in 0.36f..0.94f && tq <= 0f) || (rf in 0.01f..0.59f)
        withTransform({ scale(if (rf > 0f) 1f else -1f, 1f, pivot = cfoot) }) {
            figure(cfoot, tall, MajlisC10.Ink, walk = if (moving) t * 6f else 0f, arms = if (rf > 0f && rf < 0.6f) 0.7f else 0f)
        }
    }
}

// ═════════════ 2. lane: let in to learn, kept at the door, and the tug of war (mustarshid) ═════════════

private object LaneC10 {
    val Sky = Color(0xFF8EC3E8)
    val SkyLow = Color(0xFFEAF2F0)
    val Wall = Color(0xFFF3ECDD)
    val WallShade = Color(0xFFD9CDB4)
    val Door = Color(0xFF6A4428)
    val DoorDark = Color(0xFF3A2414)
    val Inside = Color(0xFFFFE6A8)
    val Road = Color(0xFFE2D2B0)
    val RoadEdge = Color(0xFFB8A27A)
    val Ditch = Color(0xFF9A8460)
    val Ink = Color(0xFF2A2026)
    val Rope = Color(0xFF9A7A48)
    val Palm = Color(0xFF55703C)
    val Trunk = Color(0xFF7A5A38)
    val Bench = Color(0xFF8A6A48)
}

/**
 * A lane in the afternoon, a house with a wooden door. Beats: knock (a man comes humbly and knocks; the door opens
 * on a warm room and its owner), teach (they sit on the bench by the door over an open book; he nods as he
 * learns), debater (another strides up, finger raised, anger flying; the owner raises his palm and steps back, the
 * door half shut), tug (out on the road, two locked in a tug of war pull and pull, and both stagger off the road
 * into the ditch, leaving it empty and lit).
 */
internal fun DrawScope.laneScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "knock" to Shot(Offset(w * 0.62f, h * 0.55f), 1.35f),
        "teach" to Shot(Offset(w * 0.5f, h * 0.6f), 1.45f),
        "debater" to Shot(Offset(w * 0.66f, h * 0.55f), 1.3f),
        "tug" to Shot(Offset(w * 0.26f, h * 0.62f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { laneStreet(a) }
}

private fun DrawScope.laneStreet(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val kn = a.b("knock")
    val tc = a.b("teach")
    val db = a.b("debater")
    val tg = a.b("tug")
    val roadTop = h * 0.78f
    val roadBot = h * 0.88f
    drawRect(Brush.verticalGradient(listOf(LaneC10.Sky, LaneC10.SkyLow), endY = roadTop), size = Size(w, roadTop))
    w8Palm(Offset(w * 0.08f, roadTop), h * 0.5f, LaneC10.Trunk, LaneC10.Palm, t)
    // the house on the right
    val hl = w * 0.42f
    drawRect(LaneC10.Wall, topLeft = Offset(hl, h * 0.16f), size = Size(w - hl, roadTop - h * 0.16f))
    drawRect(LaneC10.WallShade, topLeft = Offset(hl, h * 0.16f), size = Size(w - hl, h * 0.03f))
    for (x in listOf(0.5f, 0.86f)) {
        drawPath(horseshoe(w * x, h * 0.32f, w * 0.03f, w * 0.025f, h * 0.44f), Color(0xFF5A7A8A))
        drawPath(horseshoe(w * x, h * 0.32f, w * 0.03f, w * 0.025f, h * 0.44f), LaneC10.Door, style = Stroke(1.5f * dp))
    }
    // the door: opens for the one who asks, half shut on the one who comes to debate
    val dx = w * 0.66f
    val dw = w * 0.1f
    val doorTop = h * 0.4f
    val open = easeOut(seg(kn, 0.45f, 0.65f)) * (1f - 0.55f * easeOut(seg(db, 0.4f, 0.7f)))
    val doorway = horseshoe(dx, doorTop + dw * 0.5f, dw / 2, dw * 0.42f, roadTop)
    drawPath(doorway, lerpColor(LaneC10.DoorDark, LaneC10.Inside, open))
    if (open > 0f) softGlow(Offset(dx, roadTop - h * 0.15f), w * 0.08f, LaneC10.Inside, 0.6f * open)
    withTransform({ scale(1f - 0.85f * open, 1f, pivot = Offset(dx - dw / 2, roadTop)) }) {
        drawPath(doorway, LaneC10.Door)
        drawLine(LaneC10.DoorDark, Offset(dx, doorTop), Offset(dx, roadTop), 1.2f * dp)
        drawCircle(Color(0xFFC9A04A), w * 0.006f, Offset(dx + dw * 0.3f, h * 0.6f))
    }
    drawPath(doorway, LaneC10.DoorDark, style = Stroke(2f * dp))
    // the bench by the door
    drawRect(LaneC10.Bench, topLeft = Offset(w * 0.45f, roadTop - h * 0.07f), size = Size(w * 0.13f, h * 0.018f))
    for (x in listOf(0.46f, 0.57f)) drawRect(LaneC10.Bench, topLeft = Offset(w * x, roadTop - h * 0.055f), size = Size(w * 0.008f, h * 0.055f))
    // the road, and the ditch below it
    drawRect(LaneC10.Ditch, topLeft = Offset(0f, roadBot), size = Size(w, h - roadBot))
    val lit = 0.6f + 0.4f * seg(tg, 0.7f, 1f)
    drawRect(lerpColor(LaneC10.Road, Color(0xFFFFF2CC), 0.4f * seg(tg, 0.7f, 1f)), topLeft = Offset(0f, roadTop), size = Size(w, roadBot - roadTop))
    drawLine(LaneC10.RoadEdge, Offset(0f, roadBot), Offset(w, roadBot), 1.4f * dp, alpha = lit)
    for (k in 0 until 14) drawLine(LaneC10.RoadEdge, Offset(w * k / 14f, roadTop + h * 0.05f), Offset(w * k / 14f + w * 0.03f, roadTop + h * 0.05f), 0.8f * dp, alpha = 0.5f)

    val tall = h * 0.34f
    // the owner: in the doorway, then on the bench, then standing his ground
    val owner = seg(kn, 0.55f, 0.7f)
    val benchSit = easeInOut(seg(tc, 0f, 0.3f)) * (1f - easeInOut(seg(db, 0.1f, 0.3f)))
    val oFoot = Offset(mix(dx, w * 0.54f, benchSit), roadTop + h * 0.02f)
    if (owner > 0f && tg <= 0.15f) {
        if (benchSit > 0.5f) {
            w8Seated(Offset(w * 0.565f, roadTop - h * 0.05f), tall * 0.9f, LaneC10.Ink, facing = -1f, speak = 0.5f + 0.5f * sin(t * 3f))
        } else {
            val palm = easeOut(seg(db, 0.3f, 0.5f))
            figure(oFoot, tall, LaneC10.Ink, alpha = owner * (1f - seg(tg, 0f, 0.15f)), arms = 0.3f * (1f - palm) + palm, lean = -0.1f * palm)
            if (palm > 0.3f) {
                // the raised palm: wait
                val hand = oFoot + Offset(-tall * 0.28f, -tall * 0.76f)
                drawLine(LaneC10.Ink, hand, hand + Offset(0f, -tall * 0.1f), max(1.6f * dp, tall * 0.05f), StrokeCap.Round, alpha = palm)
            }
        }
    }
    // the one who asks: comes humbly, knocks, then sits and learns
    if (kn > 0f && tg <= 0.15f) {
        val come = easeInOut(seg(kn, 0f, 0.35f))
        val sit = benchSit
        val vFoot = Offset(mix(w * 0.25f, dx - dw * 0.85f, come), roadTop + h * 0.04f)
        if (sit > 0.5f || (tc > 0f && db <= 0.1f)) {
            val seat = Offset(w * 0.465f, roadTop - h * 0.05f)
            val nod = 0.5f + 0.5f * sin(t * 4f)
            w8Seated(seat, tall * 0.9f, LaneC10.Ink, facing = 1f, alpha = 1f - seg(db, 0.6f, 0.8f))
            openBook10(Offset(w * 0.51f, roadTop - h * 0.12f), w * 0.07f, seg(tc, 0.2f, 0.5f), alpha = 1f - seg(db, 0.6f, 0.8f))
            for (k in 0 until 3) {
                val f = fract(t * 0.6f + k / 3f)
                drawCircle(Color(0xFFFFE6A8), 1.6f * dp, Offset(mix(w * 0.51f, w * 0.48f, f), mix(roadTop - h * 0.14f, roadTop - h * 0.28f + nod * h * 0.005f, f)), alpha = seg(tc, 0.3f, 0.5f) * (1f - f))
            }
        } else if (sit <= 0.5f) {
            val knock = if (kn in 0.35f..0.5f) 0.5f + 0.5f * sin(t * 18f) else 0f
            figure(vFoot, tall, LaneC10.Ink, alpha = 1f - seg(db, 0.05f, 0.25f), walk = if (come in 0.01f..0.99f) t * 5f else 0f, lean = 0.12f, arms = if (kn > 0.35f) 0.8f + 0.2f * knock else 0.15f)
            if (kn in 0.35f..0.5f) for (r in 0 until 2) {
                val f = fract(t * 2f + r * 0.5f)
                drawArc(LaneC10.DoorDark, -50f, 100f, false, topLeft = Offset(dx - dw * 0.45f - f * 12f * dp, h * 0.55f - f * 12f * dp), size = Size(f * 24f * dp, f * 24f * dp), alpha = 1f - f, style = Stroke(1.2f * dp))
            }
        }
    }
    // the one who comes to debate: strides up, finger raised, anger flying
    if (db > 0f && tg <= 0.15f) {
        val stride = easeInOut(seg(db, 0f, 0.35f))
        val dFoot = Offset(mix(w * 1.05f, w * 0.8f, stride), roadTop + h * 0.04f)
        figure(dFoot, tall * 1.04f, LaneC10.Ink, walk = if (stride < 1f) t * 7f else 0f, lean = 0.05f, arms = 1f, stride = 1.3f)
        val tip = dFoot + Offset(-tall * 0.3f, -tall * 0.8f)
        drawLine(LaneC10.Ink, tip, tip + Offset(0f, -tall * 0.1f), max(1.4f * dp, tall * 0.04f), StrokeCap.Round)
        anger10(tip + Offset(-tall * 0.05f, 0f), -1f, tall * 0.45f, t, seg(db, 0.25f, 0.4f))
    }
    // tug: two locked in a tug of war; both stagger off the road into the ditch
    if (tg > 0f) {
        val come = easeOut(seg(tg, 0f, 0.15f))
        val pull = 0.5f + 0.5f * sin(t * 5f)
        val off = w8EaseIn(seg(tg, 0.55f, 0.85f))
        val l = Offset(w * 0.17f, mix(roadTop + h * 0.06f, roadBot + h * 0.05f, off))
        val r = Offset(w * 0.4f, mix(roadTop + h * 0.06f, roadBot + h * 0.05f, off))
        val sway = (pull - 0.5f) * w * 0.02f
        val al = come
        withTransform({ scale(-1f, 1f, pivot = l + Offset(sway, 0f)) }) {
            figure(l + Offset(sway, 0f), tall, LaneC10.Ink, alpha = al, lean = -0.25f - 0.3f * off, arms = 0.9f)
        }
        figure(r + Offset(sway, 0f), tall, LaneC10.Ink, alpha = al, lean = -0.25f - 0.3f * off, arms = 0.9f)
        val hl2 = l + Offset(sway + tall * 0.3f, -tall * 0.6f)
        val hr2 = r + Offset(sway - tall * 0.3f, -tall * 0.6f)
        drawPath(Path().apply { moveTo(hl2.x, hl2.y); quadraticBezierTo((hl2.x + hr2.x) / 2f, (hl2.y + hr2.y) / 2f + h * 0.01f, hr2.x, hr2.y) }, LaneC10.Rope, alpha = al, style = Stroke(2f * dp, cap = StrokeCap.Round))
        anger10(l + Offset(0f, -tall * 1.05f), 1f, tall * 0.35f, t, al * (1f - off))
        anger10(r + Offset(0f, -tall * 1.05f), -1f, tall * 0.35f, t, al * (1f - off))
        if (off > 0f && off < 1f) for (k in 0 until 8) drawCircle(LaneC10.RoadEdge, w * (0.012f + 0.006f * k % 3), Offset(w * (0.1f + 0.04f * k), roadBot + h * 0.04f * off), alpha = 0.6f * (1f - off))
        // the road stays: empty and lit
        if (tg > 0.75f) softGlow(Offset(w * 0.26f, (roadTop + roadBot) / 2f), w * 0.16f, Color(0xFFFFF2CC), 0.5f * seg(tg, 0.75f, 1f))
    }
}

// ═════════════ 3. hilltop: the fog of thinking about the Lord, the verse, and creation (tafakkur) ═════════════

private object HillC10 {
    val SkyTop = Color(0xFF1C1E44)
    val SkyLow = Color(0xFFB06A7A)
    val Sea = Color(0xFF3A4A7A)
    val Hill = Color(0xFF2A2436)
    val HillFar = Color(0xFF453A58)
    val Ink = Color(0xFF130F1C)
    val Fog = Color(0xFF8E8A98)
    val Moon = Color(0xFFF2EAD0)
    val Bird = Color(0xFF1A1626)
    val Sprout = Color(0xFF7DB86A)
}

/**
 * A hilltop above the sea, from dusk to night. Beats: fog (a man sits thinking; a cloud of fog with question
 * marks grows over his head: nothing can be pictured in it), doubtspark (sparks fall from it to his heart, which
 * smokes, and he holds his head), revealed (he opens the Qur'an on his knees; the verse in gold; the fog clears),
 * creation (the world around him lights up for him to think about: the stars, the moon rising over the sea, birds
 * crossing, a seed at his feet sprouting).
 */
internal fun DrawScope.hilltopScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "mindfog" to Shot(Offset(w * 0.5f, h * 0.5f), 1.4f),
        "doubtspark" to Shot(Offset(w * 0.5f, h * 0.56f), 1.55f),
        "revealed" to Shot(Offset(w * 0.5f, h * 0.48f), 1.25f),
        "creation" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { hilltopView(a) }
}

private fun DrawScope.hilltopView(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val fg = a.b("mindfog")
    val ds = a.b("doubtspark")
    val rv = a.b("revealed")
    val cr = a.b("creation")
    val life = easeOut(seg(cr, 0f, 0.5f))
    val horizon = h * 0.6f
    nightSky(t, HillC10.SkyTop, lerpColor(HillC10.SkyLow, Color(0xFF2A2E5A), 0.6f * life), 0.6f, starAlpha = 0.35f + 0.65f * life)
    if (life > 0f) for (k in 0 until 40) {
        val p = Offset(w8Rnd(k, 31.7f) * w, w8Rnd(k, 57.1f) * horizon * 0.85f)
        glint(p, (2.5f + 3f * w8Rnd(k, 2.2f)) * dp * (0.7f + 0.3f * sin(t * 2f + k)), Color.White, 0.7f * life * seg(cr, 0.02f * (k % 10), 0.3f + 0.02f * (k % 10)))
    }
    // the moon rises over the sea
    val moon = Offset(w * 0.8f, mix(horizon + h * 0.02f, h * 0.18f, easeOut(seg(cr, 0.1f, 0.7f))))
    if (cr > 0f) {
        softGlow(moon, w * 0.08f, HillC10.Moon, 0.6f)
        drawCircle(HillC10.Moon, w * 0.03f, moon)
    }
    // the sea and the far hills
    drawRect(Brush.verticalGradient(listOf(HillC10.Sea, Color(0xFF1E2448)), startY = horizon, endY = h), topLeft = Offset(0f, horizon), size = Size(w, h - horizon))
    for (k in 0 until 10) {
        val y = horizon + h * (0.02f + 0.03f * k)
        val x = fract(w8Rnd(k, 4.4f) + t * 0.01f) * w
        drawLine(Color(0xFFE8E0F0), Offset(x, y), Offset(x + w * 0.06f, y), 0.8f * dp, alpha = 0.15f + 0.45f * life)
    }
    if (cr > 0f) drawLine(HillC10.Moon, Offset(moon.x, horizon + h * 0.02f), Offset(moon.x, h * 0.72f), 3f * dp, StrokeCap.Round, alpha = 0.35f * seg(cr, 0.4f, 0.8f))
    hills(HillC10.HillFar, horizon, h * 0.025f, 0.02f, 0.05f, 1.7f)
    // the hilltop he sits on, and a small olive tree
    val hilltop = Path().apply {
        moveTo(0f, h * 0.9f); cubicTo(w * 0.25f, h * 0.74f, w * 0.6f, h * 0.72f, w * 1.0f, h * 0.82f); lineTo(w, h); lineTo(0f, h); close()
    }
    drawPath(hilltop, HillC10.Hill)
    val tree = Offset(w * 0.27f, h * 0.79f)
    drawLine(HillC10.Ink, tree, tree + Offset(-w * 0.01f, -h * 0.16f), 3f * dp, StrokeCap.Round)
    for (k in 0 until 5) drawCircle(Color(0xFF2E3A2A), w * 0.035f, tree + Offset(w * (-0.04f + 0.02f * k), -h * (0.17f + 0.03f * (k % 2))), alpha = 0.95f)
    // birds crossing
    if (cr > 0.2f) for (k in 0 until 4) {
        val f = fract(t * 0.05f + k * 0.13f)
        val b = Offset(mix(-w * 0.05f, w * 1.05f, f), h * (0.3f + 0.04f * k) + h * 0.01f * sin(t * 2f + k))
        val flap = sin(t * 9f + k) * w * 0.006f
        drawPath(Path().apply { moveTo(b.x - w * 0.012f, b.y - flap); quadraticBezierTo(b.x - w * 0.005f, b.y - w * 0.004f, b.x, b.y); quadraticBezierTo(b.x + w * 0.005f, b.y - w * 0.004f, b.x + w * 0.012f, b.y - flap) }, HillC10.Bird, alpha = seg(cr, 0.2f, 0.4f), style = Stroke(1.4f * dp, cap = StrokeCap.Round))
    }

    // the man, sitting
    val seat = Offset(w * 0.5f, h * 0.76f)
    val tall = h * 0.3f
    val holdHead = easeOut(seg(ds, 0.3f, 0.5f)) * (1f - easeOut(seg(rv, 0f, 0.3f)))
    val lookUp = easeOut(seg(cr, 0.3f, 0.6f))
    w8Seated(seat, tall, HillC10.Ink, facing = -1f, speak = 0f)
    if (holdHead > 0f) {
        val sh = seat + Offset(tall * 0.02f, -tall * 0.38f)
        drawLine(HillC10.Ink, sh, sh + Offset(-tall * 0.16f, -tall * 0.08f), max(1.7f * dp, tall * 0.06f), StrokeCap.Round, alpha = holdHead)
        drawLine(HillC10.Ink, sh + Offset(-tall * 0.16f, -tall * 0.08f), sh + Offset(-tall * 0.07f, -tall * 0.2f), max(1.7f * dp, tall * 0.06f), StrokeCap.Round, alpha = holdHead)
    }
    if (lookUp > 0f) {
        val sh = seat + Offset(tall * 0.02f, -tall * 0.38f)
        for (sd in listOf(-1f, 1f)) drawLine(HillC10.Ink, sh, sh + Offset(sd * tall * 0.15f, -tall * 0.12f * lookUp), max(1.7f * dp, tall * 0.06f), StrokeCap.Round, alpha = lookUp)
    }
    val smoke = easeOut(seg(ds, 0.3f, 0.7f)) * (1f - easeOut(seg(rv, 0.2f, 0.7f)))
    chestHeart10(seat + Offset(0f, tall * 0.3f), tall * 0.95f, smoke, t, alpha = seg(ds, 0.1f, 0.3f) * (1f - seg(cr, 0f, 0.2f)) + seg(rv, 0.5f, 0.8f) * (1f - seg(cr, 0f, 0.2f)))
    // the fog over his head: no image in it, only question marks
    val fog = easeOut(seg(fg, 0.05f, 0.6f)) * (1f - easeOut(seg(rv, 0.2f, 0.7f)))
    val fc = seat + Offset(-tall * 0.05f, -tall * 0.85f)
    if (fog > 0f) {
        for (k in 0 until 2) drawCircle(HillC10.Fog, tall * (0.03f + 0.02f * k), seat + Offset(-tall * 0.03f, -tall * (0.58f + 0.08f * k)), alpha = 0.8f * fog)
        for (k in 0 until 7) {
            val ang = k * 0.9f + t * 0.3f
            drawCircle(HillC10.Fog, tall * (0.12f + 0.04f * sin(t + k)) * (0.6f + 0.4f * fog), fc + Offset(cos(ang) * tall * 0.13f, sin(ang) * tall * 0.07f), alpha = 0.55f * fog * (1f + 0.4f * seg(ds, 0f, 0.5f)).coerceAtMost(1.4f) / 1.4f)
        }
        for (k in 0 until 3) {
            val q = seg(fg, 0.3f + 0.15f * k, 0.5f + 0.15f * k) * fog
            if (q <= 0f) continue
            val qc = fc + Offset((k - 1) * tall * 0.11f, -tall * 0.01f + tall * 0.02f * sin(t * 2f + k))
            val qr = tall * 0.035f
            val sw = max(1.4f * dp, tall * 0.02f)
            drawArc(Color(0xFF2A2436), 190f, 260f, false, topLeft = qc - Offset(qr, qr * 1.6f), size = Size(2 * qr, 2 * qr), alpha = q, style = Stroke(sw, cap = StrokeCap.Round))
            drawLine(Color(0xFF2A2436), qc + Offset(0f, qr * 0.4f), qc + Offset(0f, qr * 0.9f), sw, StrokeCap.Round, alpha = q)
            drawCircle(Color(0xFF2A2436), sw * 0.7f, qc + Offset(0f, qr * 1.5f), alpha = q)
        }
        // doubtspark: sparks fall from the fog to his heart
        if (ds > 0f) for (k in 0 until 6) {
            val f = fract(t * 1.1f + k / 6f)
            val to = seat + Offset(0f, -tall * 0.32f)
            val p = fc + (to - fc) * f + Offset(sin(f * 6f + k) * tall * 0.04f, 0f)
            drawCircle(Color(0xFFFFC85A), 1.8f * dp, p, alpha = seg(ds, 0f, 0.2f) * fog * (1f - 0.5f * f))
        }
    }
    // revealed: the Qur'an open on his knees, and the verse
    val open = easeOut(seg(rv, 0f, 0.3f))
    if (open > 0f) openBook10(seat + Offset(-tall * 0.2f, -tall * 0.1f), w * 0.07f, open, alpha = open)
    callout(Offset(w * 0.5f, h * 0.2f), a.big("revealed", 0, 0), w * 0.66f, easeOut(seg(rv, 0.25f, 0.5f)) * (1f - 0.7f * seg(cr, 0f, 0.3f)), ArtInk.Shade, ArtInk.Gold)
    // creation: a seed at his feet sprouts
    if (cr > 0f) {
        val s = seat + Offset(-tall * 0.42f, tall * 0.02f)
        val g = easeOut(seg(cr, 0.3f, 0.9f))
        drawLine(HillC10.Sprout, s, s + Offset(0f, -tall * 0.15f * g), 1.6f * dp, StrokeCap.Round)
        for (sd in listOf(-1f, 1f)) drawOval(HillC10.Sprout, topLeft = s + Offset(if (sd < 0f) -tall * 0.07f * g else 0f, -tall * 0.15f * g - tall * 0.02f), size = Size(tall * 0.07f * g, tall * 0.035f * g))
    }
}

// ═════════════ 4. wayside: clear water and murky words (majalisat-ahwa) ═════════════

private object WaysideC10 {
    val Sky = Color(0xFFA8D4F0)
    val SkyLow = Color(0xFFF6F0DC)
    val Field = Color(0xFFD9C58E)
    val Road = Color(0xFFE6D6B4)
    val RoadEdge = Color(0xFFB8A27A)
    val Trunk = Color(0xFF6A4E30)
    val Leaf = Color(0xFF557A3E)
    val Shade = Color(0xFF8A7A50)
    val Ink = Color(0xFF2A2026)
    val Stain = Color(0xFF5A4030)
    val Mat = Color(0xFF8A3A34)
    val Murk = Color(0xFF2E2620)
}

/**
 * A road at noon, a shade tree on the left and a prayer mat on the right. Beats: sinner (under the tree sits a man
 * in a stained robe), unharmed (beside him, his water flask is clear and lit: he is on the Sunna; the traveller sits
 * with him, and the stains stay on the sinner's robe, the traveller's own flask clear), zealot (on the mat a man
 * prays and prays, his robe spotless, but his flask is dark and clouded; he turns and talks, and dark rings come
 * from his words), rise (the traveller gets up, hands over his ears, and walks away; the dark rings fall short),
 * sound (he goes on down the road holding up his flask, still clear).
 */
internal fun DrawScope.waysideScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "sinner" to Shot(Offset(w * 0.3f, h * 0.6f), 1.35f),
        "unharmed" to Shot(Offset(w * 0.32f, h * 0.62f), 1.5f),
        "zealot" to Shot(Offset(w * 0.7f, h * 0.58f), 1.4f),
        "getup" to Shot(Offset(w * 0.58f, h * 0.6f), 1.25f),
        "sound" to Shot(Offset(w * 0.4f, h * 0.6f), 1.12f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { waysideRoad(a) }
}

private fun DrawScope.waysideRoad(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sn = a.b("sinner")
    val uh = a.b("unharmed")
    val zl = a.b("zealot")
    val rs = a.b("getup")
    val sd = a.b("sound")
    val horizon = h * 0.55f
    drawRect(Brush.verticalGradient(listOf(WaysideC10.Sky, WaysideC10.SkyLow), endY = horizon), size = Size(w, horizon))
    softGlow(Offset(w * 0.5f, h * 0.06f), w * 0.08f, Color(0xFFFFF6D0), 0.9f)
    drawRect(WaysideC10.Field, topLeft = Offset(0f, horizon), size = Size(w, h - horizon))
    hills(Color(0xFFC9B47A), horizon, h * 0.02f, 0.02f, 0.05f, 0.9f)
    val roadTop = h * 0.84f
    drawRect(WaysideC10.Road, topLeft = Offset(0f, roadTop), size = Size(w, h * 0.1f))
    drawLine(WaysideC10.RoadEdge, Offset(0f, roadTop), Offset(w, roadTop), 1.2f * dp)
    drawLine(WaysideC10.RoadEdge, Offset(0f, roadTop + h * 0.1f), Offset(w, roadTop + h * 0.1f), 1.2f * dp)
    // the shade tree on the left
    val tree = Offset(w * 0.24f, h * 0.8f)
    drawOval(WaysideC10.Shade, topLeft = Offset(tree.x - w * 0.16f, h * 0.79f), size = Size(w * 0.32f, h * 0.05f), alpha = 0.6f)
    drawLine(WaysideC10.Trunk, tree, tree + Offset(0f, -h * 0.3f), 6f * dp, StrokeCap.Round)
    for (k in 0 until 7) drawCircle(WaysideC10.Leaf, w * 0.06f, tree + Offset(w * (-0.1f + 0.035f * k), -h * (0.34f + 0.06f * (k % 2))), alpha = 0.95f)
    // the prayer mat on the right
    drawRect(WaysideC10.Mat, topLeft = Offset(w * 0.64f, h * 0.8f), size = Size(w * 0.14f, h * 0.025f))

    val tall = h * 0.3f
    // the sinner under the tree: a stained robe; his flask clear and lit (he is on the Sunna)
    val sIn = easeOut(seg(sn, 0f, 0.3f))
    val sSeat = Offset(w * 0.2f, h * 0.81f)
    if (sIn > 0f) {
        w8Seated(sSeat, tall, WaysideC10.Ink, facing = 1f, alpha = sIn)
        drawPath(Path().apply { moveTo(sSeat.x - tall * 0.08f, sSeat.y - tall * 0.36f); lineTo(sSeat.x + tall * 0.06f, sSeat.y - tall * 0.36f); lineTo(sSeat.x + tall * 0.26f, sSeat.y); lineTo(sSeat.x - tall * 0.14f, sSeat.y); close() }, Color(0xFFCFC2A8), alpha = sIn)
        for (k in 0 until 5) drawCircle(WaysideC10.Stain, tall * (0.03f + 0.012f * (k % 2)), sSeat + Offset(tall * (-0.06f + 0.06f * k), -tall * (0.06f + 0.07f * (k % 3))), alpha = 0.85f * sIn)
        flask10(sSeat + Offset(-tall * 0.2f, 0f), tall * 0.24f, 0f, t, alpha = seg(uh, 0f, 0.3f) * sIn + (1f - seg(uh, 0f, 0.3f)) * 0f)
    }
    // the worshipper of desire on the mat: he prays and prays; his flask dark
    val zIn = easeOut(seg(zl, 0f, 0.25f))
    val zFoot = Offset(w * 0.73f, h * 0.81f)
    val turnTalk = easeOut(seg(zl, 0.6f, 0.75f))
    if (zIn > 0f) {
        val bow = if (turnTalk < 0.5f) bump(fract(t * 0.6f)) else 0f
        if (turnTalk < 0.5f) w8Worshipper(zFoot, tall, WaysideC10.Ink, 0f, bow, zIn)
        else figure(zFoot, tall, WaysideC10.Ink, alpha = zIn, arms = 0.6f + 0.3f * sin(t * 4f))
        // his spotless white robe
        val zb = if (turnTalk < 0.5f) 0.47f * bow else 0f
        drawPath(Path().apply { moveTo(zFoot.x - tall * 0.06f + tall * 0.25f * -zb, zFoot.y - tall * 0.72f + tall * 0.3f * zb); lineTo(zFoot.x + tall * 0.06f, zFoot.y - tall * 0.5f); lineTo(zFoot.x + tall * 0.15f, zFoot.y); lineTo(zFoot.x - tall * 0.15f, zFoot.y); close() }, Color(0xFFF8F6F0), alpha = zIn)
        flask10(zFoot + Offset(tall * 0.32f, 0f), tall * 0.24f, 1f, t, alpha = zIn)
        // his words: dark rings rolling out toward the traveller, falling short once he goes
        if (turnTalk > 0.5f) for (k in 0 until 4) {
            val f = fract(t * 0.7f + k / 4f)
            val reach = if (rs > 0.3f) 0.55f else 1f
            val c = zFoot + Offset(-tall * 0.3f - w * 0.22f * f * reach, -tall * 0.75f + h * 0.1f * f * (if (rs > 0.3f) 1f else 0f))
            val r = (6f + 10f * f) * dp
            drawArc(WaysideC10.Murk, 130f, 100f, false, topLeft = c - Offset(r, r), size = Size(2 * r, 2 * r), alpha = 0.8f * (1f - f) * (1f - seg(sd, 0.5f, 1f)), style = Stroke(2.2f * dp, cap = StrokeCap.Round))
        }
    }
    // the traveller: comes and sits with the sinner; then near the zealot; then gets up, ears covered, and goes
    val come = easeInOut(seg(sn, 0.2f, 0.9f))
    val sitWith = easeOut(seg(uh, 0.2f, 0.4f)) * (1f - easeOut(seg(zl, 0f, 0.15f)))
    val toZealot = easeInOut(seg(zl, 0.1f, 0.5f))
    val leave = easeInOut(seg(rs, 0.15f, 1f))
    val onward = easeInOut(seg(sd, 0f, 1f))
    val tx = when {
        sd > 0f -> mix(w * 0.36f, w * 0.12f, onward)
        rs > 0f -> mix(w * 0.55f, w * 0.36f, leave)
        zl > 0f -> mix(w * 0.3f, w * 0.55f, toZealot)
        else -> mix(w * 0.85f, w * 0.3f, come)
    }
    val tFoot = Offset(tx, h * 0.9f)
    if (sn > 0.15f) {
        val ears = easeOut(seg(rs, 0f, 0.2f)) * (1f - easeOut(seg(sd, 0.3f, 0.6f)))
        if (sitWith > 0.5f) {
            w8Seated(Offset(w * 0.3f, h * 0.81f), tall, WaysideC10.Ink, facing = -1f)
            flask10(Offset(w * 0.36f, h * 0.81f), tall * 0.24f, 0f, t)
        } else {
            val moving = (sn in 0.2f..0.9f && uh <= 0f) || (zl in 0.1f..0.5f) || (rs in 0.15f..0.99f) || (sd in 0.01f..0.99f)
            val faceRight = zl > 0f && rs <= 0.15f
            withTransform({ scale(if (faceRight) -1f else 1f, 1f, pivot = tFoot) }) {
                figure(tFoot, tall, WaysideC10.Ink, walk = if (moving) t * 6f else 0f, arms = if (ears > 0f) 0f else 0.2f)
            }
            if (ears > 0f) {
                val sh = tFoot + Offset(0f, -tall * 0.76f)
                for (s2 in listOf(-1f, 1f)) {
                    drawLine(WaysideC10.Ink, sh, sh + Offset(s2 * tall * 0.13f, -tall * 0.02f), max(1.6f * dp, tall * 0.06f), StrokeCap.Round, alpha = ears)
                    drawLine(WaysideC10.Ink, sh + Offset(s2 * tall * 0.13f, -tall * 0.02f), sh + Offset(s2 * tall * 0.09f, -tall * 0.15f), max(1.6f * dp, tall * 0.06f), StrokeCap.Round, alpha = ears)
                }
            }
            // his own flask, held up at the end, clear
            val held = easeOut(seg(sd, 0.2f, 0.5f))
            val fpos = if (held > 0f) tFoot + Offset(-tall * 0.32f, -tall * (0.45f + 0.35f * held)) else tFoot + Offset(tall * 0.15f, -tall * 0.25f)
            flask10(fpos, tall * 0.2f, 0f, t)
        }
    }
}
