package app.usulcards.audio

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

/** One clip to play from the app's assets; [key] tells the UI which sentence to highlight. */
data class PlayItem(val asset: String, val key: String)

/** The card to play after the current one in continuous listening: its id, title and clips. */
data class NextCard(val owner: String, val title: String, val items: List<PlayItem>)

/**
 * Plays a queue of short clips one after another (one per sentence, or one per verse), exposing the key
 * of the clip being played so the matching sentence can be highlighted.
 *
 * One player for the whole app, shared with [PlaybackService]: listening goes on with the screen locked,
 * with play/pause on the lock screen and in the notification, and pauses by itself for a call or when
 * headphones are unplugged. In continuous listening the next card's clips are queued before the current
 * card ends, so the voice never stops between cards (the app may be in the background at that moment).
 */
@OptIn(UnstableApi::class)
class AudioPlayer private constructor(private val app: Context) : PlaybackView {
    override var currentKey by mutableStateOf<String?>(null)
        private set

    /** True while a listening session is on (playing, or paused from the lock screen). */
    override var isPlaying by mutableStateOf(false)
        private set

    /** Id of the card whose audio is playing, so only that card's animation follows the voice. */
    override var owner by mutableStateOf<String?>(null)
        private set

    /** True only while sound is actually coming out (false when paused or between sessions). */
    var audible by mutableStateOf(false)
        private set

    val exo: ExoPlayer = ExoPlayer.Builder(app)
        .setAudioAttributes(
            AudioAttributes.Builder().setUsage(C.USAGE_MEDIA).setContentType(C.AUDIO_CONTENT_TYPE_SPEECH).build(),
            /* handleAudioFocus = */ true,
        )
        .setHandleAudioBecomingNoisy(true)
        .setWakeMode(C.WAKE_MODE_LOCAL)
        .build()

    private var onDone: (() -> Unit)? = null
    private var next: ((String) -> NextCard?)? = null   // given the last queued card, the card after it
    private var onAdvance: ((String) -> Unit)? = null   // the voice moved on to this card
    private var lastQueued: String? = null

    init {
        exo.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                sync()
                queueNextIfLast()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED && isPlaying) finished()
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                audible = playing
            }

            override fun onPlayerError(error: PlaybackException) {
                // a clip that cannot be read is skipped
                if (exo.hasNextMediaItem()) {
                    exo.seekToNextMediaItem()
                    exo.prepare()
                    exo.play()
                } else if (isPlaying) {
                    finished()
                }
            }
        })
    }

    /** How far the current clip has played, from 0 to 1. */
    override fun positionFraction(): Float {
        val d = exo.duration
        if (d <= 0L || d == C.TIME_UNSET) return 0f
        return (exo.currentPosition.toFloat() / d).coerceIn(0f, 1f)
    }

    /**
     * Plays [items]. [title] names the card on the lock screen. With [next], each time the last clip
     * of the queue starts, the following card is asked for and queued; [onAdvance] says when its voice
     * begins. [onFinished] runs when everything queued has been played.
     */
    fun play(
        items: List<PlayItem>,
        speed: Float,
        owner: String? = null,
        title: String = "",
        next: ((String) -> NextCard?)? = null,
        onAdvance: ((String) -> Unit)? = null,
        onFinished: () -> Unit,
    ) {
        clear()
        if (items.isEmpty()) return
        this.owner = owner
        this.onDone = onFinished
        this.next = next
        this.onAdvance = onAdvance
        lastQueued = owner
        isPlaying = true
        startService()
        exo.setMediaItems(items.mapIndexed { i, it -> media(it, owner, title, i) })
        exo.setPlaybackSpeed(speed)
        exo.prepare()
        exo.play()
    }

    fun stop() {
        clear()
        stopService()
    }

    private fun clear() {
        onDone = null
        next = null
        onAdvance = null
        lastQueued = null
        isPlaying = false
        audible = false
        currentKey = null
        owner = null
        if (exo.mediaItemCount > 0 || exo.playbackState != Player.STATE_IDLE) {
            exo.stop()
            exo.clearMediaItems()
        }
    }

    private fun finished() {
        val cb = onDone
        stop()
        cb?.invoke()
    }

    /** Media id: "<card>|<n>|<key>" (card empty for a verse played alone). */
    private fun media(item: PlayItem, owner: String?, title: String, n: Int): MediaItem =
        MediaItem.Builder()
            .setUri(Uri.parse("asset:///${item.asset}"))
            .setMediaId("${owner.orEmpty()}|$n|${item.key}")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title.ifEmpty { "Re-Iman" })
                    .setArtist("Re-Iman")
                    .build()
            )
            .build()

    private fun sync() {
        val parts = exo.currentMediaItem?.mediaId?.split("|", limit = 3) ?: return
        if (parts.size < 3) return
        currentKey = parts[2]
        val card = parts[0].ifEmpty { null }
        if (card != null && card != owner) {
            owner = card
            onAdvance?.invoke(card)
        }
    }

    private fun queueNextIfLast() {
        val provide = next ?: return
        val last = lastQueued ?: return
        if (exo.currentMediaItemIndex != exo.mediaItemCount - 1) return
        val n = provide(last) ?: return
        if (n.items.isEmpty()) return
        exo.addMediaItems(n.items.mapIndexed { i, it -> media(it, n.owner, n.title, i) })
        lastQueued = n.owner
    }

    // Starting an already running service is harmless; a start refused in the background only means
    // that this session plays while the app is open.
    private fun startService() {
        try {
            app.startService(Intent(app, PlaybackService::class.java))
        } catch (e: Exception) {
            // not allowed right now
        }
    }

    private fun stopService() {
        try {
            app.stopService(Intent(app, PlaybackService::class.java))
        } catch (e: Exception) {
            // already stopped
        }
    }

    companion object {
        @Volatile
        private var instance: AudioPlayer? = null

        /** The app's single player (created on the main thread). */
        fun get(context: Context): AudioPlayer =
            instance ?: synchronized(this) {
                instance ?: AudioPlayer(context.applicationContext).also { instance = it }
            }
    }
}
