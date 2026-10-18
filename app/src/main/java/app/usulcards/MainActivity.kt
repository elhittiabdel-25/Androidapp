package app.usulcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.usulcards.audio.AudioPlayer
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import app.usulcards.ui.UiVisibility
import app.usulcards.ui.UsulApp

class MainActivity : ComponentActivity() {
    private lateinit var player: AudioPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repo = ContentRepo.load(this)
        val progress = ProgressStore(this)
        player = AudioPlayer.get(this)
        setContent { UsulApp(repo, progress, player) }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) player.stop()     // the user left the app (back): stop listening
    }
}
