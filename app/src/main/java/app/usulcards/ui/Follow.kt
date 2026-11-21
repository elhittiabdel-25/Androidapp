package app.usulcards.ui

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextLayoutResult

/** Keeps the screen from dimming while [on] (the voice is reading and the reader follows along). */
@Composable
fun KeepScreenOn(on: Boolean) {
    val view = LocalView.current
    DisposableEffect(view, on) {
        view.keepScreenOn = on
        onDispose { view.keepScreenOn = false }
    }
}

/**
 * Where each readable part of a card sits on screen (root coordinates, refreshed on every layout), so the
 * page can scroll to the sentence the voice is reading. Parts are keyed like the audio clips: "title",
 * "face-0", "deep-1", "ev-2". Face sentences share one text, so their places come from its text layout.
 */
class FollowState {
    var viewTop = 0f
    var viewHeight = 0f
    private var touchedAt = 0L
    private val tops = HashMap<String, Float>()
    private var faceCard = ""
    private var faceTop = 0f
    private var faceLayout: TextLayoutResult? = null
    private var faceStarts: List<Int> = emptyList()

    fun place(card: String, key: String, top: Float) {
        tops["$card/$key"] = top
    }

    fun placeFace(card: String, top: Float) {
        if (faceCard != card) {
            faceCard = card
            faceLayout = null
        }
        faceTop = top
    }

    fun faceText(card: String, layout: TextLayoutResult, starts: List<Int>) {
        faceCard = card
        faceLayout = layout
        faceStarts = starts
    }

    fun topOf(card: String, key: String): Float? {
        if (key.startsWith("face-")) {
            if (card != faceCard) return null
            val i = key.removePrefix("face-").toIntOrNull() ?: return null
            val layout = faceLayout ?: return null
            val start = faceStarts.getOrNull(i) ?: return null
            if (start >= layout.layoutInput.text.length) return null
            return faceTop + layout.getLineTop(layout.getLineForOffset(start))
        }
        return tops["$card/$key"]
    }

    /** The reader scrolled by hand: leave the page alone for a few seconds. */
    fun touched() {
        touchedAt = SystemClock.uptimeMillis()
    }

    fun readerIsScrolling(): Boolean = SystemClock.uptimeMillis() - touchedAt < 4000
}
