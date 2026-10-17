package app.usulcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.usulcards.audio.AudioPlayer
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import app.usulcards.ui.UsulApp

class MainActivity : ComponentActivity() {
    private lateinit var player: AudioPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repo = ContentRepo.load(this)
        val progress = ProgressStore(this)
        player = AudioPlayer(this)
        setContent { UsulApp(repo, progress, player) }
    }

    override fun onStop() {
        super.onStop()
        player.stop()
    }
}
