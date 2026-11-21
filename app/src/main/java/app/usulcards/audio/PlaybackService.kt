package app.usulcards.audio

import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import app.usulcards.MainActivity

/**
 * Keeps listening alive with the screen locked or the app in the background: a media session around the
 * app's player, shown as the usual media notification and lock-screen controls (Media3 promotes the
 * service to the foreground while the voice plays). Started by [AudioPlayer] when listening begins.
 */
@OptIn(UnstableApi::class)
class PlaybackService : MediaSessionService() {
    private var session: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        val open = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val s = MediaSession.Builder(this, AudioPlayer.get(this).exo)
            .setSessionActivity(open)
            .build()
        session = s
        addSession(s)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = session

    /** The app was swiped away from the recent apps: stop listening. */
    override fun onTaskRemoved(rootIntent: Intent?) {
        AudioPlayer.get(this).stop()
        stopSelf()
    }

    override fun onDestroy() {
        session?.release()        // the player itself belongs to AudioPlayer and is kept
        session = null
        super.onDestroy()
    }
}
