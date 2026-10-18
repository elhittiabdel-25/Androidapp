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
import app.usulcards.audio.NextCard
import app.usulcards.audio.PlayItem
import app.usulcards.data.Card
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** The cards of the open module (one path), in order, and where the reader is in them.
 *  `deepByDefault` says whether a card opens with its «تعمّق» layer expanded (the reader's setting). */
class DeckState(private val repo: ContentRepo, private val deepByDefault: () -> Boolean = { false }) {
    var filter by mutableStateOf<String?>(null)   // the open module (path)
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

    /** One module (path). */
    fun setPath(path: String?) {
        filter = path
        rebuild()
    }

    /** Opens module [path] at card [cardId] (when it belongs to it), else at its first card. */
    fun open(path: String, cardId: String?) {
        if (filter != path) setPath(path)
        val i = cardId?.let { queue.indexOf(it) } ?: -1
        goTo(if (i >= 0) i else 0)
    }

    private fun rebuild() {
        queue.clear()
        queue.addAll(repo.cardsFor(null, filter).map { it.id })
        index = 0
        resetCard()
    }

    private fun resetCard() {
        deepOpen = deepByDefault()
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

    fun goTo(i: Int) {
        if (i !in queue.indices) return
        index = i
        resetCard()
    }

    /** Position of the first [id] after the current card (the queue can hold a card twice). */
    fun indexAfter(id: String, from: Int = index): Int? =
        (from + 1 until queue.size).firstOrNull { queue[it] == id }

    /** "Show me again later": the card comes back three cards from now. */
    fun requeue() {
        val at = minOf(index + 4, queue.size)
        queue.add(at, queue[index])
    }

    /** Shows card [id], opening its own module when it belongs to another one. */
    fun jumpTo(id: String) {
        if (queue.indexOf(id) < 0) repo.byId[id]?.let { setPath(it.path) }
        val i = queue.indexOf(id)
        if (i >= 0) {
            index = i
            resetCard()
        }
    }
}

/** Whether the app is on screen (set by MainActivity): card animations only run when it is. */
object UiVisibility {
    @Volatile
    var visible = true
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

    /**
     * Reads [card]. In continuous listening the player queues each next card itself before the current
     * one ends (so the voice goes on with the screen locked), and tells us when it moves on: the deck then
     * follows, with the card animation if the app is on screen.
     */
    fun listen(card: Card) {
        player.play(
            playlist(card, deck.deepOpen), progress.speed, card.id, card.title,
            next = { last -> if (progress.continuous) nextAfter(last) else null },
            onAdvance = { id -> follow(id) },
        ) {}
    }

    /** The card after [id] in the deck, with the clips it will play (its «تعمّق» as a new card opens). */
    private fun nextAfter(id: String): NextCard? {
        val at = if (deck.queue.getOrNull(deck.index) == id) deck.index else deck.indexAfter(id) ?: return null
        val nextId = deck.queue.getOrNull(at + 1) ?: return null
        val c = repo.byId[nextId] ?: return null
        return NextCard(c.id, c.title, playlist(c, progress.deepByDefault))
    }

    /** The voice reached card [id]: show it. */
    private fun follow(id: String) {
        val target = deck.indexAfter(id) ?: return
        if (!UiVisibility.visible || busy) {
            deck.goTo(target)
            return
        }
        busy = true
        scope.launch {
            offsetX.animateTo(1600f, tween(240))
            deck.goTo(target)
            offsetX.snapTo(0f)
            enter.snapTo(0f)
            busy = false
            enter.animateTo(1f, tween(380, easing = FastOutSlowInEasing))
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
