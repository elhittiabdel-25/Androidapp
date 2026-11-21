package app.usulcards.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import app.usulcards.data.Gloss

/** A hard word found in a text: its character range and its explanation. */
data class GlossHit(val word: String, val start: Int, val end: Int, val def: String)

private fun isLetter(c: Char) = c in 'ء'..'ي' || c == 'ٱ'
private fun isMark(c: Char) = c in 'ً'..'ْ' || c == 'ٰ' || c == 'ـ'
private const val PREFIXES = "وفبلك"

/**
 * Where each gloss word first appears in [text] (shown with or without tashkeel). The word may carry one prefix
 * letter (و ف ب ل ك) and must end at a word boundary. Words already used earlier on the card ([taken]) are skipped.
 */
fun findGlosses(text: String, glosses: List<Gloss>, taken: MutableSet<String>): List<GlossHit> {
    if (glosses.isEmpty()) return emptyList()
    val bare = StringBuilder()
    val at = ArrayList<Int>(text.length)
    text.forEachIndexed { i, c -> if (!isMark(c)) { bare.append(c); at.add(i) } }
    val plain = bare.toString()
    val hits = ArrayList<GlossHit>()
    for (g in glosses) {
        if (g.word in taken) continue
        val w = stripHarakat(g.word)
        var from = 0
        while (true) {
            val k = plain.indexOf(w, from)
            if (k < 0) break
            from = k + 1
            val before = if (k > 0) plain[k - 1] else ' '
            val startOk = !isLetter(before) || (before in PREFIXES && (k < 2 || !isLetter(plain[k - 2])))
            val after = k + w.length
            val endOk = after >= plain.length || !isLetter(plain[after])
            if (!startOk || !endOk) continue
            var end = at[after - 1] + 1
            while (end < text.length && isMark(text[end])) end++
            if (hits.none { k < it.end && end > it.start }) {
                hits.add(GlossHit(g.word, at[k], end, g.def)); taken.add(g.word)
            }
            break
        }
    }
    return hits
}

/**
 * Text whose hard words are underlined; a tap on one opens its explanation in a small bubble above it, a second tap
 * (or a tap on another word) closes it. [open] is shared by all the texts of a card, so only one bubble is shown.
 */
@Composable
fun GlossText(
    text: AnnotatedString,
    hits: List<GlossHit>,
    open: MutableState<String?>,
    style: TextStyle,
    modifier: Modifier = Modifier,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    val pal = LocalPalette.current
    if (hits.isEmpty()) {
        Text(text, style = style, onTextLayout = onTextLayout, modifier = modifier)
        return
    }
    var layout by remember { mutableStateOf<TextLayoutResult?>(null) }
    val marked = remember(text, hits, pal) {
        AnnotatedString.Builder(text).apply {
            for (h in hits) addStyle(SpanStyle(color = pal.accent, textDecoration = TextDecoration.Underline), h.start, h.end)
        }.toAnnotatedString()
    }
    Text(
        marked,
        style = style,
        onTextLayout = { layout = it; onTextLayout(it) },
        modifier = modifier
            .semantics { contentDescription = text.text }
            .pointerInput(hits) {
                detectTapGestures { pos ->
                    val l = layout ?: return@detectTapGestures
                    val o = l.getOffsetForPosition(pos)
                    val h = hits.firstOrNull { o >= it.start && o < it.end } ?: return@detectTapGestures
                    open.value = if (open.value == h.word) null else h.word
                }
            },
    )
    val shownHit = hits.firstOrNull { it.word == open.value }
    val l = layout
    if (shownHit != null && l != null) {
        val r = wordRect(l, shownHit)
        Popup(popupPositionProvider = AboveWord(r), properties = PopupProperties(focusable = false)) {
            Text(
                shownHit.def,
                style = TextStyle(fontFamily = Fonts.naskh, fontSize = 15.sp, lineHeight = 26.sp, color = pal.surface, textAlign = TextAlign.Center),
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .shadow(6.dp, RoundedCornerShape(12.dp))
                    .background(pal.ink, RoundedCornerShape(12.dp))
                    .border(1.dp, pal.accent, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            )
        }
    }
}

/** The box of the word on its first line (a word broken across lines keeps the bubble on its start). */
private fun wordRect(l: TextLayoutResult, h: GlossHit): Rect {
    val line = l.getLineForOffset(h.start)
    var r = l.getBoundingBox(h.start)
    for (i in h.start + 1 until h.end) {
        if (l.getLineForOffset(i) != line) break
        r = r.union(l.getBoundingBox(i))
    }
    return r
}

private fun Rect.union(o: Rect) = Rect(minOf(left, o.left), minOf(top, o.top), maxOf(right, o.right), maxOf(bottom, o.bottom))

/** Puts the bubble centred above the word, inside the window; below it when there is no room above. */
private class AboveWord(private val word: Rect) : PopupPositionProvider {
    override fun calculatePosition(anchorBounds: IntRect, windowSize: IntSize, layoutDirection: LayoutDirection, popupContentSize: IntSize): IntOffset {
        val gap = 12
        val cx = anchorBounds.left + word.center.x
        val x = (cx - popupContentSize.width / 2f).toInt().coerceIn(gap, maxOf(gap, windowSize.width - popupContentSize.width - gap))
        val above = (anchorBounds.top + word.top - popupContentSize.height - gap).toInt()
        val y = if (above >= gap) above else (anchorBounds.top + word.bottom + gap).toInt()
        return IntOffset(x, y)
    }
}
