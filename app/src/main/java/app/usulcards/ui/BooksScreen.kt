package app.usulcards.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.usulcards.data.ShelfBook
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * The books section: whole books turned into cards. The shelf shows each book with its drawn cover; a book
 * opens on its parts, each part on its chapters (paths), and a chapter on its cards like any module.
 * Everything here takes plain values (cards per path, learned ids) so the screens can be previewed.
 */
class ShelfProgress(
    val cardCount: (path: String) -> Int,
    val doneCount: (path: String) -> Int,
    val started: (path: String) -> Boolean,
)

@Composable
fun ShelfScreen(shelf: List<ShelfBook>, prog: ShelfProgress, onOpenBook: (ShelfBook) -> Unit) {
    val pal = LocalPalette.current
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("الكتب", fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = pal.ink)
                Text(
                    "كتب كاملة في بطاقات، مرتبة حسب أجزاء كل كتاب.",
                    fontFamily = Fonts.ui, fontSize = 13.sp, lineHeight = 20.sp, color = pal.muted,
                )
            }
        }
        items(shelf, key = { it.id }) { book ->
            val total = book.paths.sumOf(prog.cardCount)
            val done = book.paths.sumOf(prog.doneCount)
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(pal.surface)
                    .border(1.dp, pal.line, RoundedCornerShape(16.dp))
                    .clickable { onOpenBook(book) }
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                BookCover(book, Modifier.width(112.dp), compact = true)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(book.title, fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = pal.ink)
                    Text(book.author, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = pal.gold)
                    if (book.subtitle.isNotEmpty()) {
                        Text(book.subtitle, fontFamily = Fonts.ui, fontSize = 12.sp, lineHeight = 19.sp, color = pal.muted)
                    }
                    Spacer(Modifier.height(2.dp))
                    val ready = book.parts.count { it.paths.isNotEmpty() }
                    Text(
                        "${partsWord(book.parts.size)} · $total ${cardsWord(total)}" + if (ready < book.parts.size) " · ${partsReady(ready)}" else "",
                        fontFamily = Fonts.ui, fontSize = 12.sp, color = pal.muted,
                    )
                    Box(Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)).background(pal.line)) {
                        Box(
                            Modifier.fillMaxWidth(if (total == 0) 0f else done.toFloat() / total).height(5.dp)
                                .clip(RoundedCornerShape(3.dp)).background(pal.gold),
                        )
                    }
                }
            }
        }
        item {
            Text(
                "وستُضاف كتب أخرى إن شاء الله.",
                fontFamily = Fonts.ui, fontSize = 13.sp, color = pal.muted, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
fun BookScreen(book: ShelfBook, prog: ShelfProgress, onBack: () -> Unit, onOpenPath: (String) -> Unit) {
    val pal = LocalPalette.current
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                // the screen is right-to-left: "back" points to the right
                Box(Modifier.size(42.dp).clip(RoundedCornerShape(50)).clickable(onClick = onBack), contentAlignment = Alignment.Center) {
                    Text("→", fontSize = 22.sp, color = pal.ink)
                }
                Text("الكتب", fontFamily = Fonts.ui, fontSize = 14.sp, color = pal.muted)
            }
        }
        item {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BookCover(book, Modifier.width(176.dp))
                Text(book.title, fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = pal.ink)
                Text(book.author, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = pal.gold)
                if (book.subtitle.isNotEmpty()) {
                    Text(book.subtitle, fontFamily = Fonts.ui, fontSize = 13.sp, lineHeight = 20.sp, color = pal.muted, textAlign = TextAlign.Center)
                }
            }
        }
        items(book.parts, key = { it.title }) { part ->
            val ready = part.paths.isNotEmpty()
            val total = part.paths.sumOf(prog.cardCount)
            val done = part.paths.sumOf(prog.doneCount)
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(pal.surface)
                    .border(1.dp, pal.line, RoundedCornerShape(16.dp)),
            ) {
                Row(
                    Modifier.fillMaxWidth().background(if (ready) pal.goldSoft else pal.bg).padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(part.title, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = if (ready) pal.gold else pal.muted)
                        Text(part.name, fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 17.sp, lineHeight = 27.sp, color = if (ready) pal.ink else pal.muted)
                    }
                    if (ready) {
                        CountRing(done, total, pal.gold)
                    } else {
                        Text(
                            "قريبًا", fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = pal.muted,
                            modifier = Modifier.clip(RoundedCornerShape(50)).border(1.dp, pal.line, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 3.dp),
                        )
                    }
                }
                part.paths.forEachIndexed { k, p ->
                    if (k > 0) Box(Modifier.fillMaxWidth().padding(horizontal = 14.dp).height(1.dp).background(pal.line))
                    ModuleRowView(p, prog.doneCount(p), prog.cardCount(p), prog.started(p), pal.gold) { onOpenPath(p) }
                }
            }
        }
        item { Spacer(Modifier.height(12.dp)) }
    }
}

private fun partsWord(n: Int) = when (n) {
    1 -> "جزء واحد"
    2 -> "جزآن"
    in 3..10 -> "$n أجزاء"
    else -> "$n جزءًا"
}

private fun cardsWord(n: Int) = when {
    n == 1 -> "بطاقة"
    n == 2 -> "بطاقتان"
    n % 100 in 3..10 -> "بطاقات"          // 3–10, 103–110, 203–210 …
    else -> "بطاقة"
}

private fun partsReady(n: Int) = when (n) {
    1 -> "الجزء الأول متاح"
    else -> "$n أجزاء متاحة"
}

/**
 * A drawn cover (no image files): a night sky over an open book whose light rises in rays, a double gold frame
 * with eight-pointed stars at its corners, the title in Kufi, and the author. [compact] drops the subtitle.
 */
@Composable
fun BookCover(book: ShelfBook, modifier: Modifier = Modifier, compact: Boolean = false) {
    val gold = Color(0xFFE2BC62)
    val cream = Color(0xFFF4EAD0)
    BoxWithConstraints(
        modifier.aspectRatio(2f / 3f).clip(RoundedCornerShape(6.dp)).background(Color(0xFF0B1430)),
    ) {
        val w = maxWidth.value
        Canvas(Modifier.fillMaxSize()) { coverArt(gold, cream) }
        Column(
            Modifier.fillMaxSize().padding(horizontal = (w * 0.12f).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height((w * 0.2f).dp))
            Text(book.title, fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = (w * 0.2f).sp, color = gold, maxLines = 1)
            if (!compact && book.subtitle.isNotEmpty()) {
                Spacer(Modifier.height((w * 0.02f).dp))
                Text(
                    book.subtitle, fontFamily = Fonts.ui, fontSize = (w * 0.052f).sp, lineHeight = (w * 0.08f).sp,
                    color = cream.copy(alpha = 0.85f), textAlign = TextAlign.Center, maxLines = 3,
                )
            }
            Spacer(Modifier.weight(1f))
            Text(book.author, fontFamily = Fonts.naskh, fontWeight = FontWeight.Bold, fontSize = (w * 0.075f).sp, color = cream, maxLines = 1)
            Spacer(Modifier.height((w * 0.11f).dp))
        }
    }
}

private fun DrawScope.coverArt(gold: Color, cream: Color) {
    val w = size.width
    val h = size.height
    drawRect(Brush.verticalGradient(listOf(Color(0xFF0B1430), Color(0xFF14254D), Color(0xFF1C2F5E))))
    // stars
    var seed = 7
    repeat(26) {
        seed = (seed * 1103515245 + 12345) and 0x7fffffff
        val x = (seed % 1000) / 1000f
        seed = (seed * 1103515245 + 12345) and 0x7fffffff
        val y = (seed % 1000) / 1000f
        drawCircle(cream.copy(alpha = 0.25f + 0.5f * ((seed / 7) % 10) / 10f), radius = w * (0.004f + 0.004f * (it % 3)), center = Offset(w * (0.12f + 0.76f * x), h * (0.1f + 0.5f * y)))
    }
    // the light rising from the open book
    val src = Offset(w * 0.5f, h * 0.665f)
    for (k in 0 until 9) {
        val a = (-160f + k * 17.5f) * PI.toFloat() / 180f
        val spread = 0.07f
        val len = h * (0.36f + 0.06f * ((k + 1) % 3))
        val p = Path().apply {
            moveTo(src.x, src.y)
            lineTo(src.x + cos(a - spread) * len, src.y + sin(a - spread) * len)
            lineTo(src.x + cos(a + spread) * len, src.y + sin(a + spread) * len)
            close()
        }
        drawPath(p, Brush.radialGradient(listOf(gold.copy(alpha = 0.42f), gold.copy(alpha = 0f)), center = src, radius = len))
    }
    drawCircle(Brush.radialGradient(listOf(gold.copy(alpha = 0.55f), gold.copy(alpha = 0f)), center = src, radius = w * 0.26f), radius = w * 0.26f, center = src)
    // the open book
    val bw = w * 0.27f
    val top = h * 0.655f
    val bot = h * 0.735f
    for (side in listOf(-1f, 1f)) {
        val page = Path().apply {
            moveTo(src.x, top + h * 0.012f)
            cubicTo(src.x + side * bw * 0.35f, top - h * 0.018f, src.x + side * bw * 0.75f, top - h * 0.012f, src.x + side * bw, top)
            lineTo(src.x + side * bw, bot - h * 0.01f)
            cubicTo(src.x + side * bw * 0.7f, bot - h * 0.02f, src.x + side * bw * 0.35f, bot - h * 0.012f, src.x, bot)
            close()
        }
        drawPath(page, cream)
        for (r in 1..3) {
            val y = top + (bot - top) * r / 4.2f
            drawLine(Color(0xFFB9A57A), Offset(src.x + side * bw * 0.16f, y + h * 0.004f), Offset(src.x + side * bw * 0.84f, y - h * 0.004f), w * 0.006f, StrokeCap.Round)
        }
    }
    drawLine(Color(0xFF9C8457), Offset(src.x, top + h * 0.012f), Offset(src.x, bot), w * 0.008f)
    // the frame and its corner stars
    val m1 = w * 0.05f
    val m2 = w * 0.075f
    drawRoundRect(gold, Offset(m1, m1), Size(w - 2 * m1, h - 2 * m1), CornerRadius(w * 0.02f), style = Stroke(w * 0.012f))
    drawRoundRect(gold.copy(alpha = 0.55f), Offset(m2, m2), Size(w - 2 * m2, h - 2 * m2), CornerRadius(w * 0.015f), style = Stroke(w * 0.005f))
    for (c in listOf(Offset(m1, m1), Offset(w - m1, m1), Offset(m1, h - m1), Offset(w - m1, h - m1))) {
        eightStar(c, w * 0.045f, gold)
    }
}

private fun DrawScope.eightStar(c: Offset, r: Float, color: Color) {
    for (turn in listOf(0f, 45f)) {
        rotate(turn, c) { drawRect(color, Offset(c.x - r * 0.7f, c.y - r * 0.7f), Size(r * 1.4f, r * 1.4f)) }
    }
    drawCircle(Color(0xFF0B1430), r * 0.38f, c)
}

/** The books tab's icon: an open book; while the section is new, a slow gold halo pulses behind it. */
@Composable
fun BooksTabIcon(glow: Boolean, phase: Float? = null) {
    val color = LocalContentColor.current
    val gold = LocalPalette.current.gold
    val t = phase ?: if (glow) {
        val pulse = rememberInfiniteTransition(label = "books-glow")
        val v by pulse.animateFloat(0f, 1f, infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Restart), label = "books-glow-t")
        v
    } else 0f
    Box(contentAlignment = Alignment.Center) {
        if (glow) {
            Canvas(Modifier.size(52.dp)) {
                val c = center
                // two rings spreading out of the icon, one after the other
                for (k in 0..1) {
                    val f = (t + k * 0.5f) % 1f
                    drawCircle(gold.copy(alpha = 0.55f * (1f - f)), radius = size.minDimension * (0.22f + 0.28f * f), center = c, style = Stroke(size.minDimension * 0.045f))
                }
                drawCircle(Brush.radialGradient(listOf(gold.copy(alpha = 0.45f), gold.copy(alpha = 0f)), center = c, radius = size.minDimension * 0.42f), radius = size.minDimension * 0.42f, center = c)
            }
        }
        BookGlyph(if (glow) gold else color, 24.dp)
        if (glow) {
            Text(
                "جديد", fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 9.sp, color = Color(0xFF1B1405),
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 0.dp).clip(RoundedCornerShape(50)).background(gold)
                    .padding(horizontal = 5.dp),
            )
        }
    }
}

/** An open book in line style, like the category emblems. */
@Composable
fun BookGlyph(color: Color, size: Dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val line = Stroke(w * 0.085f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        for (side in listOf(-1f, 1f)) {
            val p = Path().apply {
                moveTo(w * 0.5f, h * 0.3f)
                cubicTo(w * (0.5f + side * 0.14f), h * 0.2f, w * (0.5f + side * 0.3f), h * 0.2f, w * (0.5f + side * 0.42f), h * 0.24f)
                lineTo(w * (0.5f + side * 0.42f), h * 0.78f)
                cubicTo(w * (0.5f + side * 0.3f), h * 0.74f, w * (0.5f + side * 0.14f), h * 0.74f, w * 0.5f, h * 0.84f)
                close()
            }
            drawPath(p, color, style = line)
        }
        drawLine(color, Offset(w * 0.5f, h * 0.3f), Offset(w * 0.5f, h * 0.84f), w * 0.085f, StrokeCap.Round)
    }
}

/**
 * The "new" callout: a glowing card that sits just above the tab bar and points down at the books tab (the
 * middle one). It shows until the reader opens the books section or closes it.
 */
@Composable
fun NewBooksCallout(book: ShelfBook, onOpen: () -> Unit, onClose: () -> Unit, modifier: Modifier = Modifier, phase: Float? = null) {
    val pal = LocalPalette.current
    val gold = pal.gold
    val t = phase ?: run {
        val pulse = rememberInfiniteTransition(label = "callout-glow")
        val v by pulse.animateFloat(0f, 1f, infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Restart), label = "callout-glow-t")
        v
    }
    val breathe = 0.5f + 0.5f * sin(t * 2f * PI.toFloat())
    Column(modifier.padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .fillMaxWidth()
                .drawBehind {
                    // the glow: soft gold layers around the card, breathing
                    for (k in 1..4) {
                        val g = k * 3.dp.toPx() * (0.6f + 0.4f * breathe)
                        drawRoundRect(
                            gold.copy(alpha = (0.16f - k * 0.03f) * (0.55f + 0.45f * breathe)),
                            Offset(-g, -g), Size(size.width + 2 * g, size.height + 2 * g), CornerRadius(16.dp.toPx() + g),
                        )
                    }
                }
                .clip(RoundedCornerShape(16.dp))
                .background(pal.surface)
                .border(
                    1.5.dp,
                    Brush.sweepGradient(listOf(gold, gold.copy(alpha = 0.25f), gold, gold.copy(alpha = 0.25f), gold)),
                    RoundedCornerShape(16.dp),
                )
                .clickable(onClick = onOpen)
                .padding(start = 12.dp, top = 10.dp, bottom = 10.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BookCover(book, Modifier.width(40.dp), compact = true)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("جديد: قسم الكتب", fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = gold)
                Text(
                    "أول كتبه «${book.title}»، تأليف ${book.author}، في بطاقات مرتبة حسب أجزائه.",
                    fontFamily = Fonts.ui, fontSize = 12.sp, lineHeight = 19.sp, color = pal.ink,
                )
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "إغلاق", tint = pal.muted)
            }
        }
        // the pointer, aimed at the books tab, bobbing gently
        Canvas(Modifier.size(width = 22.dp, height = 14.dp).padding(top = (2f * breathe).dp)) {
            val p = Path().apply {
                moveTo(0f, 0f); lineTo(size.width, 0f); lineTo(size.width / 2f, size.height); close()
            }
            drawPath(p, gold)
        }
    }
}
