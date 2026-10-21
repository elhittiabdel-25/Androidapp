package app.usulcards

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import app.usulcards.audio.AudioPlayer
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import app.usulcards.reminder.Reminders
import app.usulcards.ui.UiVisibility
import app.usulcards.ui.UsulApp

class MainActivity : ComponentActivity() {
    private lateinit var player: AudioPlayer
    private lateinit var repo: ContentRepo
    private lateinit var progress: ProgressStore

    /** A card to open, asked by the daily reminder's notification. */
    private val openRequest = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        repo = ContentRepo.load(this)
        progress = ProgressStore(this)
        player = AudioPlayer.get(this)
        if (savedInstanceState == null) openRequest.value = intent?.getStringExtra(Reminders.EXTRA_OPEN)
        if (progress.reminderOn) Reminders.schedule(this)
        setContent { UsulApp(repo, progress, player, openRequest.value) { openRequest.value = null } }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.getStringExtra(Reminders.EXTRA_OPEN)?.let { openRequest.value = it }
    }

    // Listening goes on when the screen is locked or the app goes to the background (PlaybackService);
    // the deck only needs to know whether it is on screen, to animate the cards or not.
    override fun onStart() {
        super.onStart()
        UiVisibility.visible = true
    }

    override fun onStop() {
        super.onStop()
        UiVisibility.visible = false
        if (progress.reminderOn) Reminders.saveHint(this, repo, progress)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) player.stop()     // the user left the app (back): stop listening
    }
}
