package app.usulcards.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** One clip to play from the app's assets; [key] tells the UI which sentence to highlight. */
data class PlayItem(val asset: String, val key: String)

/**
 * Plays a queue of short clips one after another (one per sentence, or one per verse),
 * exposing the key of the clip being played so the matching sentence can be highlighted.
 */
class AudioPlayer(private val context: Context) {
    var currentKey by mutableStateOf<String?>(null)
        private set
    var isPlaying by mutableStateOf(false)
        private set

    /** Id of the card whose audio is playing, so only that card's animation follows the voice. */
    var owner by mutableStateOf<String?>(null)
        private set

    private var player: MediaPlayer? = null
    private var queue: List<PlayItem> = emptyList()
    private var index = 0
    private var onDone: (() -> Unit)? = null
    private var speed = 1f
    private var session = 0

    /** How far the current clip has played, from 0 to 1 (0 between clips). */
    fun positionFraction(): Float {
        val p = player ?: return 0f
        return try {
            val d = p.duration
            if (d > 0) (p.currentPosition.toFloat() / d).coerceIn(0f, 1f) else 0f
        } catch (e: Exception) {
            0f
        }
    }

    fun play(items: List<PlayItem>, speed: Float, owner: String? = null, onFinished: () -> Unit) {
        stop()
        if (items.isEmpty()) return
        this.owner = owner
        this.speed = speed
        queue = items
        index = 0
        onDone = onFinished
        isPlaying = true
        session++
        playCurrent(session)
    }

    private fun playCurrent(s: Int) {
        if (s != session) return
        if (index >= queue.size) {
            release()
            isPlaying = false
            currentKey = null
            val cb = onDone
            onDone = null
            cb?.invoke()
            return
        }
        val item = queue[index]
        currentKey = item.key
        try {
            val afd = context.assets.openFd(item.asset)
            val mp = MediaPlayer()
            mp.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            mp.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            mp.setOnCompletionListener {
                it.release()
                if (player === it) player = null
                index++
                playCurrent(s)
            }
            mp.setOnErrorListener { p, _, _ ->
                p.release()
                if (player === p) player = null
                index++
                playCurrent(s)
                true
            }
            mp.prepare()
            if (speed != 1f) {
                mp.playbackParams = mp.playbackParams.setSpeed(speed)
            }
            mp.start()
            player = mp
        } catch (e: Exception) {
            index++
            playCurrent(s)
        }
    }

    private fun release() {
        player?.let {
            try {
                it.stop()
            } catch (e: Exception) {
                // already stopped
            }
            it.release()
        }
        player = null
    }

    fun stop() {
        session++
        release()
        queue = emptyList()
        onDone = null
        isPlaying = false
        currentKey = null
        owner = null
    }
}
