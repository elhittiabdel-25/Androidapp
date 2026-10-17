package app.usulcards.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.usulcards.audio.AudioPlayer
import app.usulcards.audio.PlayItem
import app.usulcards.data.Card
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** Which cards are in the deck, in which order, and where the reader is. */
class DeckState(private val repo: ContentRepo) {
    var filter by mutableStateOf<String?>(null)
        private set
    val queue = mutableStateListOf<String>()
    var index by mutableIntStateOf(0)
        private set
    var deepOpen by mutableStateOf(false)
    var flipped by mutableStateOf(false)
    val seen = mutableStateListOf<String>()

    init {
        rebuild()
    }

    fun setPath(path: String?) {
        filter = path
        rebuild()
    }

    private fun rebuild() {
        queue.clear()
        val list = repo.cards.filter { filter == null || it.path == filter }
        val ordered = if (filter == null) list else list.sortedBy { it.order }
        queue.addAll(ordered.map { it.id })
        index = 0
        resetCard()
    }

    private fun resetCard() {
        deepOpen = false
        flipped = false
        queue.getOrNull(index)?.let { if (it !in seen) seen.add(it) }
    }

    val current: Card get() = repo.byId.getValue(queue[index])
    fun canGo(d: Int): Boolean = (index + d) in queue.indices

    fun go(d: Int) {
        if (!canGo(d)) return
        index += d
        resetCard()
    }

    /** "Show me again later": the card comes back three cards from now. */
    fun requeue() {
        val at = minOf(index + 4, queue.size)
        queue.add(at, queue[index])
    }

    fun jumpTo(id: String) {
        if (queue.indexOf(id) < 0) setPath(null)
        val i = queue.indexOf(id)
        if (i >= 0) {
            index = i
            resetCard()
        }
    }
}

/** Card navigation with its fly-out/rise-in animation, and the listen queue (including continuous mode). */
class DeckController(
    private val repo: ContentRepo,
    private val deck: DeckState,
    private val player: AudioPlayer,
    private val progress: ProgressStore,
    private val scope: CoroutineScope,
) {
    val offsetX = Animatable(0f)
    val enter = Animatable(1f)
    private var busy = false

    fun navigate(d: Int, autoPlay: Boolean = false) {
        if (busy || !deck.canGo(d)) return
        val keepPlaying = autoPlay || (player.isPlaying && progress.continuous)
        player.stop()
        busy = true
        scope.launch {
            offsetX.animateTo(if (d > 0) 1600f else -1600f, tween(240))
            deck.go(d)
            offsetX.snapTo(0f)
            enter.snapTo(0f)
            busy = false
            if (keepPlaying) listen(deck.current)
            enter.animateTo(1f, tween(380, easing = FastOutSlowInEasing))
        }
    }

    fun settle() {
        scope.launch { offsetX.animateTo(0f, tween(220)) }
    }

    fun drag(dx: Float) {
        scope.launch { offsetX.snapTo(offsetX.value + dx) }
    }

    fun toggleListen(card: Card) {
        if (player.isPlaying) player.stop() else listen(card)
    }

    fun listen(card: Card) {
        player.play(playlist(card, deck.deepOpen), progress.speed, card.id) {
            if (progress.continuous && deck.canGo(1)) navigate(1, autoPlay = true)
        }
    }

    fun playVerse(files: List<String>, key: String) {
        player.play(files.map { PlayItem(it, key) }, 1f) {}
    }

    private fun playlist(card: Card, includeDeep: Boolean): List<PlayItem> {
        val items = mutableListOf<PlayItem>()
        card.audio.filter { it.layer == "face" }.forEach { items += PlayItem(it.file, it.key) }
        if (includeDeep) {
            card.audio.filter { it.key.startsWith("deep-") }.forEach { items += PlayItem(it.file, it.key) }
            var hadith = 0
            card.evidence.forEachIndexed { i, e ->
                if (e.kind == "quran") {
                    repo.recitationFiles(e).forEach { items += PlayItem(it, "ev-$i") }
                } else {
                    card.audio.firstOrNull { it.key == "had-$hadith" }?.let { items += PlayItem(it.file, "ev-$i") }
                    hadith++
                }
            }
        }
        return items
    }
}
