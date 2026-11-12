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
import kotlin.math.min
import kotlin.math.sin

/*
 * Kit 94 (2026-11-12): boards for the cards of batches 18 to 20 (how the Salaf explained the Quran, Juz' 28 and 29, tajwid).
 * A board shows, beat by beat, a few named things: a symbol with its word, a word in a tile, a plain figure, the places
 * of articulation, or the length of a madd. Everything drawn carries its name from the card (the clarity rule of the
 * November review); a beat never shows more than four things.
 */

internal const val ROW94 = 0     // things side by side, the first on the right
internal const val FLOW94 = 1    // the same, with an arrow from each to the next
internal const val VS94 = 2      // two things either side of a line
internal const val FAN94 = 3     // several things that come down to one (the last item)
internal const val TILES94 = 4   // words in tiles
internal const val TRACT94 = 5   // the mouth and throat from the side, a zone lit
internal const val WAVE94 = 6    // a sound that runs as long as its count

/** One thing in a beat: its symbol ("txt" a word tile, "fig" a figure, a zone or a count in the special modes), the tag part that names it (-1 none), a mark (1 tick, 2 cross). */
internal class It94(val icon: String, val part: Int, val mark: Int = 0, val tone: Color = Qn.Gold)

/** One beat of a board. */
internal class Bt94(val kind: String, val mode: Int, val items: List<It94>, val head: Int = -1, val foot: Int = -1)

private val SKY94: List<List<Color>> = listOf(
    listOf(Qn.Night, Qn.NightLow),
    listOf(Color(0xFF12283A), Color(0xFF1E3A44)),
    listOf(Qn.Teal, Qn.Night),
    listOf(Color(0xFF2A2430), Color(0xFF3A2E3E)),
    listOf(Color(0xFF1B2240), Qn.Lapis),
)

internal fun DrawScope.board94(a: ArtCtx, sky: Int, beats: List<Bt94>) {
    drawRect(Brush.verticalGradient(SKY94[sky % SKY94.size]))
    val w = size.width
    val h = size.height
    for (k in 0 until 14) {
        val x = ((k * 73 + 11) % 100) / 100f
        val y = ((k * 37 + 5) % 60) / 100f
        drawCircle(Color.White, (0.6f + (k % 3) * 0.4f) * density, Offset(w * x, h * y), alpha = 0.1f + 0.07f * sin(a.t * 0.8f + k))
    }
    for (i in beats.indices) {
        val b = beats[i]
        val al = fqIn(a, b.kind, beats.getOrNull(i + 1)?.kind)
        if (al > 0.01f) beat94(a, b, al)
    }
}

private fun DrawScope.beat94(a: ArtCtx, b: Bt94, al: Float) {
    val w = size.width
    val h = size.height
    val p = a.b(b.kind)
    val n = b.items.size.coerceAtLeast(1)
    if (b.head >= 0) tagAt(a.tag(b.kind, 0, b.head), Offset(w * 0.5f, h * 0.14f), al * seg(p, 0.02f, 0.14f), back = Qn.Back, ink = ArtInk.Gold)
    if (b.foot >= 0) tagAt(a.tag(b.kind, 0, b.foot), Offset(w * 0.5f, h * 0.88f), al * seg(p, 0.62f, 0.78f), back = Qn.Back, ink = ArtInk.Ivory)
    fun shown(i: Int): Float = al * easeOut(seg(p, 0.08f + 0.5f * i / n, 0.24f + 0.5f * i / n))
    fun marked(i: Int): Float = seg(p, 0.3f + 0.5f * i / n, 0.45f + 0.5f * i / n)
    val cy = h * 0.52f
    when (b.mode) {
        TRACT94 -> tract94(a, b, al, p)
        WAVE94 -> for (i in b.items.indices) {
            val it = b.items[i]
            val units = it.icon.toIntOrNull() ?: 2
            val y = h * (if (b.items.size == 1) 0.52f else 0.36f + 0.3f * i)
            val f = easeOut(seg(p, 0.1f + 0.35f * i, 0.6f + 0.35f * i))
            val x0 = w * 0.78f
            val unit = w * 0.1f
            val len = unit * units * f
            val path = Path().apply {
                moveTo(x0, y)
                var x = 0f
                while (x < len) {
                    x += 2f * density
                    lineTo(x0 - x, y + sin(x / unit * 2f * PI.toFloat()) * h * 0.035f)
                }
            }
            drawPath(path, it.tone, alpha = al, style = Stroke(2.4f * density, cap = StrokeCap.Round))
            for (k in 1..units) if (len >= unit * k - 1f) drawCircle(Qn.GoldLight, 3f * density, Offset(x0 - unit * k, y + h * 0.08f), alpha = al)
            drawCircle(it.tone, 5f * density, Offset(x0, y), alpha = al)
            if (it.part >= 0) tagAt(a.tag(b.kind, 0, it.part), Offset(w * 0.8f, y - h * 0.12f), al * seg(p, 0.05f + 0.35f * i, 0.2f + 0.35f * i), back = Qn.Back, ink = ArtInk.Ivory)
        }
        FAN94 -> {
            val m = (b.items.size - 1).coerceAtLeast(1)
            val s = min(w * 0.19f, w * 0.62f / m)
            val target = Offset(w * 0.5f, h * 0.68f)
            for (i in 0 until b.items.size - 1) {
                val c = Offset(w * (0.5f + (m - 1) * 0.13f - i * 0.26f), h * 0.36f)
                val f = seg(p, 0.42f + 0.08f * i, 0.7f + 0.08f * i)
                val from = c + Offset(0f, s * 0.62f)
                val to = target + Offset((c.x - target.x) * 0.25f, -s * 0.7f)
                if (f > 0f) drawLine(Qn.GoldLight, from, from + (to - from) * f, 1.6f * density, StrokeCap.Round, alpha = al * 0.8f)
                item94(a, b.kind, b.items[i], c, s, shown(i), marked(i))
            }
            item94(a, b.kind, b.items.last(), target, s * 1.1f, al * easeOut(seg(p, 0.62f, 0.8f)), seg(p, 0.82f, 0.95f))
        }
        VS94 -> {
            val s = w * 0.24f
            drawLine(Color.White, Offset(w * 0.5f, h * 0.3f), Offset(w * 0.5f, h * 0.74f), 1.2f * density, StrokeCap.Round, alpha = 0.35f * al * seg(p, 0.1f, 0.3f))
            for (i in 0 until min(b.items.size, 2)) item94(a, b.kind, b.items[i], Offset(w * (if (i == 0) 0.74f else 0.26f), cy), s, shown(i), marked(i))
        }
        else -> {
            val tiles = b.mode == TILES94
            if (tiles && b.items.size >= 4) {
                for (i in b.items.indices) {
                    val c = Offset(w * (if (i % 2 == 0) 0.73f else 0.27f), h * (0.38f + 0.27f * (i / 2)))
                    item94(a, b.kind, b.items[i], c, w * 0.4f, shown(i), marked(i))
                }
            } else {
                val gap = min(if (tiles) 0.6f else 0.3f, (if (tiles) 0.92f else 0.86f) / n)
                val s = min(w * (if (tiles) 0.5f else 0.22f), w * gap * (if (b.mode == FLOW94) 0.6f else 0.84f))
                for (i in b.items.indices) {
                    val c = Offset(w * (0.5f + (n - 1) * 0.5f * gap - i * gap), cy)
                    if (b.mode == FLOW94 && i > 0) {
                        val f = seg(p, 0.12f + 0.5f * i / n, 0.26f + 0.5f * i / n)
                        val from = Offset(c.x + w * gap - s * 0.62f, cy)
                        val to = Offset(c.x + s * 0.62f, cy)
                        if (f > 0f) {
                            drawLine(Qn.GoldLight, from, from + (to - from) * f, 1.8f * density, StrokeCap.Round, alpha = al)
                            if (f > 0.9f) arrowHead(to, Offset(-1f, 0f), 5f * density, Qn.GoldLight, 1.8f * density, al)
                        }
                    }
                    item94(a, b.kind, b.items[i], c, s, shown(i), marked(i))
                }
            }
        }
    }
}

private fun DrawScope.item94(a: ArtCtx, kind: String, it: It94, c: Offset, s: Float, al: Float, mp: Float) {
    if (al <= 0.01f) return
    val tl = if (it.part >= 0) a.tag(kind, 0, it.part) else null
    var mc = c + Offset(-s * 0.54f, -s * 0.7f)   // the mark sits on the card's corner, clear of its symbol
    when (it.icon) {
        "fig" -> {
            figure(Offset(c.x, c.y + s * 0.6f), s * 1.3f, Qn.Person, al)
            tagAt(tl, Offset(c.x, c.y - s * 0.98f), al, back = Qn.Back, ink = ArtInk.Ivory)
            mc = c + Offset(-s * 0.42f, -s * 0.5f)
        }
        "txt" -> {
            val bw = s
            val bh = min(s * 0.62f, size.height * 0.26f)
            val o = Offset(c.x - bw / 2f, c.y - bh / 2f + (1f - al) * 8f * density)
            drawRoundRect(Qn.Back, o, Size(bw, bh), CornerRadius(8f * density), alpha = al)
            drawRoundRect(it.tone, o, Size(bw, bh), CornerRadius(8f * density), alpha = al * 0.8f, style = Stroke(1.3f * density))
            qFit(tl, Offset(c.x, o.y + bh / 2f), bw * 0.86f, bh * 0.72f, ArtInk.Ivory, al)
            mc = Offset(o.x + 3f * density, o.y + 3f * density)
        }
        else -> glyphCard(c, s, tl, al, rim = it.tone, lit = if (it.mark == 1) mp else 0f) { cc, sz, aa -> qIcon(it.icon, cc, sz, it.tone, aa, a.t) }
    }
    if (it.mark == 1 && mp > 0f) tick14(mc, s * 0.16f, mp) else if (it.mark == 2 && mp > 0f) cross14(mc, s * 0.16f, mp)
}

/** The mouth and throat from the side (lips on the left, the throat going down on the right): the zones named by the beat light up. */
private fun DrawScope.tract94(a: ArtCtx, b: Bt94, al: Float, p: Float) {
    val w = size.width
    val h = size.height
    val ink = Color(0xFFE8DCC0)
    val sw = 2.2f * density
    fun zone(name: String): Float {
        val i = b.items.indexOfFirst { it.icon == name }
        return if (i < 0) 0f else easeOut(seg(p, 0.15f + 0.3f * i, 0.4f + 0.3f * i))
    }
    val jawf = zone("jawf")
    val halq = zone("halq")
    val lisan = zone("lisan")
    val shafa = zone("shafa")
    val khay = zone("khayshum")
    val lip = Offset(w * 0.24f, h * 0.5f)
    val roof = Path().apply {
        moveTo(lip.x, lip.y - h * 0.035f)
        cubicTo(w * 0.34f, h * 0.36f, w * 0.56f, h * 0.36f, w * 0.66f, h * 0.46f)
        lineTo(w * 0.66f, h * 0.84f)
    }
    val floor = Path().apply {
        moveTo(lip.x, lip.y + h * 0.035f)
        cubicTo(w * 0.3f, h * 0.62f, w * 0.5f, h * 0.64f, w * 0.57f, h * 0.6f)
        lineTo(w * 0.57f, h * 0.84f)
    }
    val nose = Path().apply {
        moveTo(w * 0.2f, h * 0.38f)
        cubicTo(w * 0.34f, h * 0.24f, w * 0.56f, h * 0.26f, w * 0.64f, h * 0.4f)
    }
    if (jawf > 0f) softGlow(Offset(w * 0.45f, h * 0.47f), w * 0.2f, Qn.GoldLight, 0.7f * jawf * al)
    if (halq > 0f) {
        softGlow(Offset(w * 0.615f, h * 0.68f), w * 0.13f, Qn.GoldLight, 0.7f * halq * al)
        for (k in 0 until 3) drawCircle(Qn.Gold, 3.2f * density, Offset(w * 0.615f, h * (0.58f + 0.1f * k)), alpha = al * seg(halq, 0.2f * k, 0.4f + 0.2f * k))
    }
    if (khay > 0f) softGlow(Offset(w * 0.42f, h * 0.31f), w * 0.15f, Qn.GoldLight, 0.7f * khay * al)
    drawPath(nose, if (khay > 0f) Qn.Gold else ink, alpha = al * (0.5f + 0.5f * khay), style = Stroke(sw, cap = StrokeCap.Round))
    drawPath(roof, ink, alpha = al, style = Stroke(sw, cap = StrokeCap.Round))
    drawPath(floor, ink, alpha = al, style = Stroke(sw, cap = StrokeCap.Round))
    val tongue = Path().apply {
        moveTo(w * 0.29f, h * 0.575f)
        cubicTo(w * 0.34f, h * 0.5f, w * 0.5f, h * 0.47f, w * 0.56f, h * 0.56f)
        cubicTo(w * 0.5f, h * 0.62f, w * 0.36f, h * 0.62f, w * 0.29f, h * 0.575f)
        close()
    }
    drawPath(tongue, if (lisan > 0f) Qn.Gold else Qn.Rose, alpha = al * (0.55f + 0.45f * lisan))
    for (k in listOf(-1f, 1f)) {
        drawLine(if (shafa > 0f) Qn.Gold else ink, lip + Offset(-w * 0.035f, k * h * 0.035f), lip + Offset(w * 0.01f, k * h * 0.035f), sw * (1.6f + shafa), StrokeCap.Round, alpha = al)
    }
    if (shafa > 0f) softGlow(lip, w * 0.09f, Qn.GoldLight, 0.6f * shafa * al)
    for (it in b.items) {
        val at = when (it.icon) {
            "jawf" -> Offset(w * 0.45f, h * 0.2f)
            "halq" -> Offset(w * 0.82f, h * 0.68f)
            "lisan" -> Offset(w * 0.42f, h * 0.76f)
            "shafa" -> Offset(w * 0.17f, h * 0.68f)
            "khayshum" -> Offset(w * 0.42f, h * 0.15f)
            else -> null
        }
        if (at != null && it.part >= 0) tagAt(a.tag(b.kind, 0, it.part), at, al * zone(it.icon), back = Qn.Back, ink = ArtInk.Gold)
    }
}
