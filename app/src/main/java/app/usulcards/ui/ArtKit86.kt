package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Kit 86 (2026-11-04): v1.4, the Quran category (tafsir of al-Fatiha and Juz' 'Amma, the virtues of the Quran).
 * After the users' review: what a beat names is drawn as its word with a symbol (a glyph card), never as a row of
 * identical dots; a surah is announced by its header band, as in the mushaf; Allah is never drawn, only His names
 * as words, high in the picture.
 */

internal object Qn {
    val Night = Color(0xFF0B1424)
    val NightLow = Color(0xFF1C2B46)
    val Teal = Color(0xFF15393C)
    val TealLight = Color(0xFF2E6B66)
    val Parch = Color(0xFFF3E6C4)
    val ParchLow = Color(0xFFE2CC98)
    val Gold = Color(0xFFE8B84A)
    val GoldLight = Color(0xFFF6D88A)
    val Ink = Color(0xFF2A1E10)
    val Lapis = Color(0xFF2C4A86)
    val Rose = Color(0xFFC0566A)
    val Leaf = Color(0xFF5FA36E)
    val Soil = Color(0xFF6B4F35)
    val Back = Color(0xE6121A24)
    val Person = Color(0xFFEDE4D0)
    val DayTop = Color(0xFF8FC1E3)
    val DayLow = Color(0xFFF4E3C1)
    val Smoke = Color(0xFF2A2430)
    val Jade = Color(0xFF6FCB9A)
    val Ember = Color(0xFFE8794A)
}

/** [tl] drawn at [c], scaled down to fit [maxW] x [maxH]. */
internal fun DrawScope.qFit(tl: TextLayoutResult?, c: Offset, maxW: Float, maxH: Float, color: Color, alpha: Float, glow: Color? = null) {
    if (tl == null || alpha <= 0.01f) return
    val sc = min(1f, min(maxW / max(1f, tl.size.width.toFloat()), maxH / max(1f, tl.size.height.toFloat())))
    if (glow != null) softGlow(c, tl.size.width * sc * 0.6f, glow, 0.4f * alpha)
    bigText(tl, c, sc, color, alpha)
}

/** A verse-end rosette: a gold eight-pointed star round a teal disc. */
internal fun DrawScope.qRosette(c: Offset, r: Float, alpha: Float, fill: Color = Qn.Teal) {
    if (alpha <= 0.01f) return
    drawPath(starPath(c, r), Qn.Gold, alpha = alpha)
    drawCircle(fill, r * 0.62f, c, alpha = alpha)
    drawCircle(Qn.GoldLight, r * 0.62f, c, alpha = alpha, style = Stroke(r * 0.08f))
}

/** A surah's header band, as in the mushaf: teal with gold rules and pointed ends, the name in a cartouche. */
internal fun DrawScope.qPlate(c: Offset, wd: Float, name: TextLayoutResult?, alpha: Float) {
    if (alpha <= 0.01f) return
    val ht = wd * 0.17f
    val band = Path().apply {
        moveTo(c.x - wd / 2f, c.y); lineTo(c.x - wd / 2f + ht * 0.5f, c.y - ht / 2f); lineTo(c.x + wd / 2f - ht * 0.5f, c.y - ht / 2f)
        lineTo(c.x + wd / 2f, c.y); lineTo(c.x + wd / 2f - ht * 0.5f, c.y + ht / 2f); lineTo(c.x - wd / 2f + ht * 0.5f, c.y + ht / 2f); close()
    }
    drawPath(band, Qn.Teal, alpha = alpha)
    drawPath(band, Qn.Gold, alpha = alpha, style = Stroke(max(1.2f * density, wd * 0.008f)))
    for (s in listOf(-1f, 1f)) {
        drawLine(Qn.Gold, Offset(c.x - wd / 2f + ht * 0.62f, c.y + s * ht * 0.36f), Offset(c.x + wd / 2f - ht * 0.62f, c.y + s * ht * 0.36f), max(0.8f * density, wd * 0.004f), alpha = alpha * 0.7f)
        qRosette(Offset(c.x + s * wd * 0.37f, c.y), ht * 0.3f, alpha)
    }
    val cw = wd * 0.56f
    val ch = ht * 0.64f
    drawRoundRect(Qn.Parch, Offset(c.x - cw / 2f, c.y - ch / 2f), Size(cw, ch), CornerRadius(ch / 2f), alpha = alpha)
    drawRoundRect(Qn.Gold, Offset(c.x - cw / 2f, c.y - ch / 2f), Size(cw, ch), CornerRadius(ch / 2f), alpha = alpha, style = Stroke(max(1f * density, wd * 0.005f)))
    qFit(name, c, cw * 0.88f, ch * 0.95f, Qn.Ink, alpha)
}

/**
 * A word with its symbol: a small dark card, the symbol above the word. It rises into place as [alpha] grows.
 * [icon] draws the symbol at a centre with a size and an alpha.
 */
internal fun DrawScope.glyphCard(c: Offset, s: Float, tl: TextLayoutResult?, alpha: Float, rim: Color = Qn.Gold, lit: Float = 0f, icon: DrawScope.(Offset, Float, Float) -> Unit) {
    if (alpha <= 0.01f) return
    val a = alpha.coerceIn(0f, 1f)
    val rise = (1f - easeOut(a)) * 10f * density
    val tw = (tl?.size?.width ?: 0).toFloat()
    val th = (tl?.size?.height ?: 0).toFloat()
    val cw = max(s, tw + 12f * density)
    val ch = s * 0.74f + th + 6f * density
    val o = Offset(c.x - cw / 2f, c.y - ch / 2f + rise)
    val cr = CornerRadius(8f * density)
    if (lit > 0f) softGlow(c, cw * 0.9f, rim, 0.45f * lit * a)
    drawRoundRect(Color.Black, o + Offset(0f, 3f * density), Size(cw, ch), cr, alpha = 0.25f * a)
    drawRoundRect(Qn.Back, o, Size(cw, ch), cr, alpha = a)
    drawRoundRect(rim, o, Size(cw, ch), cr, alpha = a * (0.6f + 0.4f * lit), style = Stroke(1.3f * density))
    icon(Offset(c.x, o.y + s * 0.4f), s * 0.52f, a)
    if (tl != null) drawText(tl, color = ArtInk.Ivory, topLeft = Offset(c.x - tw / 2f, o.y + s * 0.74f), alpha = a)
}

/** A simple symbol by name, about [s] across, centred at [c]. */
internal fun DrawScope.qIcon(name: String, c: Offset, s: Float, color: Color, alpha: Float, t: Float = 0f) {
    if (alpha <= 0.01f) return
    val sw = max(1.4f * density, s * 0.08f)
    when (name) {
        "sprout" -> {
            drawLine(Qn.Soil, c + Offset(-s * 0.45f, s * 0.42f), c + Offset(s * 0.45f, s * 0.42f), sw, StrokeCap.Round, alpha = alpha)
            drawLine(Qn.Leaf, c + Offset(0f, s * 0.42f), c + Offset(0f, -s * 0.15f), sw, StrokeCap.Round, alpha = alpha)
            for (k in listOf(-1f, 1f)) drawOval(Qn.Leaf, Offset(c.x + (if (k < 0) -s * 0.42f else 0f), c.y - s * 0.32f + (if (k < 0) s * 0.1f else 0f)), Size(s * 0.42f, s * 0.2f), alpha = alpha)
        }
        "wheat" -> {
            drawLine(Qn.GoldLight, c + Offset(0f, s * 0.48f), c + Offset(0f, -s * 0.45f), sw * 0.8f, StrokeCap.Round, alpha = alpha)
            for (k in 0 until 4) for (side in listOf(-1f, 1f)) {
                val y = c.y - s * 0.38f + k * s * 0.17f
                drawOval(Qn.Gold, Offset(c.x + (if (side < 0) -s * 0.2f else s * 0.02f), y), Size(s * 0.18f, s * 0.1f), alpha = alpha)
            }
        }
        "nest" -> {
            drawArc(Qn.Soil, 0f, 180f, true, Offset(c.x - s * 0.42f, c.y), Size(s * 0.84f, s * 0.4f), alpha = alpha)
            drawOval(Qn.Parch, Offset(c.x - s * 0.2f, c.y - s * 0.08f), Size(s * 0.18f, s * 0.22f), alpha = alpha)
            drawOval(Qn.Parch, Offset(c.x + s * 0.02f, c.y - s * 0.06f), Size(s * 0.18f, s * 0.22f), alpha = alpha)
            drawLine(color, c + Offset(-s * 0.45f, -s * 0.45f), c + Offset(-s * 0.08f, -s * 0.2f), sw, StrokeCap.Round, alpha = alpha)
            arrowHead(c + Offset(-s * 0.06f, -s * 0.18f), Offset(1f, 0.7f), s * 0.16f, color, sw, alpha)
        }
        "heart" -> heart12(c, s * 0.9f, Qn.Rose, alpha, fill = true)
        "book" -> openBook21(c + Offset(0f, -s * 0.12f), s * 1.1f, Qn.Parch, Qn.Ink, alpha)
        "coin" -> {
            coin12(c + Offset(s * 0.14f, -s * 0.06f), s * 0.3f, alpha)
            coin12(c + Offset(-s * 0.14f, s * 0.1f), s * 0.3f, alpha)
        }
        "eye" -> eye73(c, s, color, alpha)
        "ear" -> ear73(c, s, color, alpha)
        "lips" -> lips73(c, s * 0.9f, Qn.Rose, alpha)
        "hands" -> hands12(c + Offset(0f, -s * 0.05f), s, color, alpha)
        "sun" -> sun12(c, s * 0.26f, alpha)
        "moon" -> crescent12(c, s * 0.4f, Qn.GoldLight, Qn.Back, alpha)
        "star" -> drawPath(starPath(c, s * 0.45f), color, alpha = alpha)
        "cloud" -> cloud38(c, s * 0.3f, color, alpha)
        "drop" -> fqDrop(c + Offset(0f, s * 0.15f), s * 0.24f, Fq.Water, alpha)
        "flame" -> qFlame(c + Offset(0f, s * 0.45f), s * 0.9f, Qn.Ember, alpha, t)
        "scale" -> balance12(c + Offset(0f, s * 0.45f), s * 0.9f, 0f, color, alpha)
        "hourglass" -> hourglass73(c, s * 0.95f, 0.5f, Qn.Gold, alpha)
        "scroll" -> scroll14(c, s * 0.95f, s * 0.7f, 1f, alpha)
        "key" -> key21(c, s * 0.75f, color, alpha)
        "lock" -> padlock21(c, s * 0.55f, color, 0f, alpha)
        "check" -> checkMark(c, s * 0.38f, Qn.Jade, sw * 1.3f, alpha)
        "cross" -> crossMark(c, s * 0.32f, Qn.Rose, sw * 1.3f, alpha)
        "mat" -> {
            drawRoundRect(Qn.Rose, Offset(c.x - s * 0.28f, c.y - s * 0.45f), Size(s * 0.56f, s * 0.9f), CornerRadius(s * 0.04f), alpha = alpha)
            drawPath(archPath26(c.x, c.y - s * 0.36f, c.y - s * 0.02f, s * 0.15f), Qn.GoldLight, alpha = alpha * 0.9f)
            drawLine(Qn.GoldLight, c + Offset(-s * 0.2f, s * 0.3f), c + Offset(s * 0.2f, s * 0.3f), sw * 0.7f, alpha = alpha)
        }
        "crescent" -> crescent12(c, s * 0.38f, Qn.GoldLight, Qn.Back, alpha)
        "people" -> for (k in -1..1) {
            val p = c + Offset(k * s * 0.3f, if (k == 0) -s * 0.04f else s * 0.06f)
            drawCircle(color, s * 0.11f, p + Offset(0f, -s * 0.16f), alpha = alpha)
            drawArc(color, 180f, 180f, true, Offset(p.x - s * 0.16f, p.y - s * 0.02f), Size(s * 0.32f, s * 0.36f), alpha = alpha)
        }
        "tree" -> {
            drawLine(Qn.Soil, c + Offset(0f, s * 0.48f), c + Offset(0f, 0f), sw * 1.4f, StrokeCap.Round, alpha = alpha)
            drawCircle(Qn.Leaf, s * 0.3f, c + Offset(0f, -s * 0.12f), alpha = alpha)
        }
        "house" -> {
            drawRect(color, Offset(c.x - s * 0.32f, c.y - s * 0.05f), Size(s * 0.64f, s * 0.48f), alpha = alpha)
            drawPath(Path().apply { moveTo(c.x - s * 0.42f, c.y - s * 0.05f); lineTo(c.x, c.y - s * 0.42f); lineTo(c.x + s * 0.42f, c.y - s * 0.05f); close() }, color, alpha = alpha)
            drawRect(Qn.Back, Offset(c.x - s * 0.08f, c.y + s * 0.15f), Size(s * 0.16f, s * 0.28f), alpha = alpha)
        }
        "kaaba" -> kaaba29(c + Offset(0f, s * 0.4f), s * 0.8f, alpha)
        "light" -> {
            softGlow(c, s * 0.8f, Qn.GoldLight, 0.7f * alpha)
            drawCircle(Color(0xFFFFF4D2), s * 0.2f, c, alpha = alpha)
            for (k in 0 until 8) {
                val an = k * PI.toFloat() / 4f + t * 0.2f
                drawLine(Qn.GoldLight, c + Offset(cos(an), sin(an)) * (s * 0.28f), c + Offset(cos(an), sin(an)) * (s * 0.46f), sw * 0.7f, StrokeCap.Round, alpha = alpha * 0.8f)
            }
        }
        "chain" -> chain72(c + Offset(-s * 0.45f, 0f), c + Offset(s * 0.45f, 0f), s * 0.22f, color, alpha)
        "cup" -> cup12(c + Offset(0f, s * 0.4f), s * 0.8f, color, alpha, Color(0xFFF8F4EA))
        "mountain" -> drawPath(Path().apply { moveTo(c.x - s * 0.5f, c.y + s * 0.4f); lineTo(c.x - s * 0.1f, c.y - s * 0.4f); lineTo(c.x + s * 0.1f, c.y - s * 0.05f); lineTo(c.x + s * 0.22f, c.y - s * 0.22f); lineTo(c.x + s * 0.5f, c.y + s * 0.4f); close() }, color, alpha = alpha)
        "pen" -> quill72(c + Offset(-s * 0.3f, s * 0.35f), s * 0.9f, color, alpha)
        else -> drawCircle(color, s * 0.3f, c, alpha = alpha)
    }
}

/** A flame standing on [base], [s] tall; it flickers with [t]. */
internal fun DrawScope.qFlame(base: Offset, s: Float, color: Color, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    val wob = sin(t * 7f) * s * 0.05f
    for ((k, col) in listOf(color, Color(0xFFF6C35A)).withIndex()) {
        val f = 1f - k * 0.4f
        drawPath(Path().apply {
            moveTo(base.x, base.y)
            cubicTo(base.x - s * 0.34f * f, base.y - s * 0.1f, base.x - s * 0.24f * f, base.y - s * 0.55f * f, base.x + wob, base.y - s * f)
            cubicTo(base.x + s * 0.24f * f, base.y - s * 0.55f * f, base.x + s * 0.34f * f, base.y - s * 0.1f, base.x, base.y)
            close()
        }, col, alpha = alpha)
    }
}

/** A beam of light from [from] toward [to], [p] of the way. */
internal fun DrawScope.qBeam(from: Offset, to: Offset, p: Float, color: Color, alpha: Float, wd: Float) {
    if (alpha <= 0.01f || p <= 0.001f) return
    val end = from + (to - from) * p.coerceIn(0f, 1f)
    drawLine(color, from, end, wd * 3f, StrokeCap.Round, alpha = alpha * 0.14f)
    drawLine(color, from, end, wd, StrokeCap.Round, alpha = alpha * 0.85f)
}

/** A plant that grows from [base]: a stem [ht] tall, leaves, and a flower at [grow] near 1. */
internal fun DrawScope.qPlant(base: Offset, ht: Float, grow: Float, alpha: Float, t: Float = 0f) {
    if (alpha <= 0.01f) return
    val g = grow.coerceIn(0f, 1f)
    drawOval(Qn.Soil, Offset(base.x - ht * 0.06f, base.y - ht * 0.03f), Size(ht * 0.12f, ht * 0.07f), alpha = alpha)
    if (g <= 0.02f) return
    val sway = sin(t * 1.3f) * ht * 0.03f * g
    val top = base + Offset(sway, -ht * g)
    drawLine(Qn.Leaf, base, top, max(1.5f * density, ht * 0.045f), StrokeCap.Round, alpha = alpha)
    val leaves = (g * 4f).toInt().coerceAtMost(3)
    for (k in 0 until leaves) {
        val y = base.y - ht * g * (0.25f + 0.22f * k)
        val side = if (k % 2 == 0) -1f else 1f
        val lw = ht * 0.24f * seg(g, 0.2f + 0.2f * k, 0.5f + 0.2f * k)
        drawOval(Qn.Leaf, Offset(if (side < 0) base.x - lw + sway * 0.5f else base.x + sway * 0.5f, y - lw * 0.22f), Size(lw, lw * 0.44f), alpha = alpha)
    }
    val fl = seg(g, 0.75f, 1f)
    if (fl > 0f) {
        for (k in 0 until 5) {
            val an = k * 2f * PI.toFloat() / 5f
            drawCircle(Qn.Rose, ht * 0.06f * fl, top + Offset(cos(an), sin(an)) * (ht * 0.06f * fl), alpha = alpha)
        }
        drawCircle(Qn.GoldLight, ht * 0.04f * fl, top, alpha = alpha)
    }
}

/** A name plate: a dark rounded stone with a name on it (for a named person who is never drawn as a figure). */
internal fun DrawScope.qNamePlate(c: Offset, tl: TextLayoutResult?, alpha: Float, fill: Color = Color(0xFF3A3340), ink: Color = ArtInk.Ivory) {
    if (alpha <= 0.01f || tl == null) return
    val wd = tl.size.width + 18f * density
    val ht = tl.size.height + 12f * density
    drawRoundRect(fill, Offset(c.x - wd / 2f, c.y - ht / 2f), Size(wd, ht), CornerRadius(ht * 0.3f), alpha = alpha)
    drawRoundRect(Color(0xFF6A6070), Offset(c.x - wd / 2f, c.y - ht / 2f), Size(wd, ht), CornerRadius(ht * 0.3f), alpha = alpha, style = Stroke(1.2f * density))
    drawText(tl, color = ink, topLeft = Offset(c.x - tl.size.width / 2f, c.y - tl.size.height / 2f), alpha = alpha.coerceIn(0f, 1f))
}

/** A plain parchment page with ruled lines, [wd] wide, centred at [c]. */
internal fun DrawScope.qPage(c: Offset, wd: Float, ht: Float, alpha: Float, lines: Int = 7, frame: Boolean = true) {
    if (alpha <= 0.01f) return
    val o = Offset(c.x - wd / 2f, c.y - ht / 2f)
    drawRoundRect(Color.Black, o + Offset(3f * density, 4f * density), Size(wd, ht), CornerRadius(4f * density), alpha = 0.3f * alpha)
    drawRoundRect(Qn.Parch, o, Size(wd, ht), CornerRadius(4f * density), alpha = alpha)
    if (frame) drawRoundRect(Qn.Gold, o + Offset(wd * 0.04f, wd * 0.04f), Size(wd * 0.92f, ht - wd * 0.08f), CornerRadius(2f * density), alpha = alpha * 0.8f, style = Stroke(1.2f * density))
    for (k in 0 until lines) {
        val y = o.y + ht * (0.2f + 0.66f * k / max(1, lines - 1))
        drawLine(Qn.Ink, Offset(o.x + wd * 0.14f, y), Offset(o.x + wd * 0.86f, y), max(1f * density, wd * 0.012f), StrokeCap.Round, alpha = alpha * 0.4f)
    }
}
