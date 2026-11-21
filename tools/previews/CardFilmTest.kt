package app.usulcards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.usulcards.audio.PlaybackView
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import app.usulcards.ui.CardView
import app.usulcards.ui.FilmFrame
import app.usulcards.ui.LightPalette
import app.usulcards.ui.LocalFilm
import app.usulcards.ui.LocalPalette
import app.usulcards.ui.beatMillis
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.Density
import java.io.File
import org.junit.Rule
import org.junit.Test

/**
 * Records whole cards as they play on the phone (tools/previews, run by the Card film workflow): the card's own
 * screen, its voice clips on a timeline (FilmData), the sentence being read highlighted, and the scene's beats
 * starting when the voice reaches them, 12 frames a second. film_assemble.py adds the sound.
 */
class CardFilmTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(density = Density.HIGH, softButtons = false),
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    private class Voice(override val owner: String?) : PlaybackView {
        override var currentKey: String? = null
        override var isPlaying: Boolean = false
        var pos = 0f
        override fun positionFraction(): Float = pos
    }

    @Test
    fun films() {
        val uthmani = File("src/main/assets/quran/uthmani.json").takeIf { it.exists() }?.readText()
        val repo = ContentRepo.parse(File("src/main/assets/content/cards.json").readText(), uthmani, emptySet())
        val progress = ProgressStore(paparazzi.context)
        for (film in FilmData.films) {
            val card = repo.byId.getValue(film.id)
            val items = card.scene?.items ?: emptyList()
            // each beat starts when the voice reaches its cue
            val starts = items.map { item ->
                film.clips.firstOrNull { it.key == item.at }?.let { it.start + item.frac * it.dur } ?: Float.MAX_VALUE
            }
            val voice = Voice(card.id)
            val fps = 12
            val first = film.clips.first().start
            val last = film.clips.last().let { it.start + it.dur }
            for (f in 0 until (film.total * fps).toInt()) {
                val t = f / fps.toFloat()
                val clip = film.clips.firstOrNull { t >= it.start && t < it.start + it.dur }
                voice.currentKey = clip?.key
                voice.pos = if (clip != null) (t - clip.start) / clip.dur else 0f
                voice.isPlaying = t >= first && t < last
                val beats = items.mapIndexed { i, it -> ((t - starts[i]) * 1000f / beatMillis(it.kind)).coerceIn(0f, 1f) }
                val frame = FilmFrame(t + 2f, beats, starts.count { it <= t })
                paparazzi.snapshot(name = "film__${film.id}__f%04d".format(f)) {
                    CompositionLocalProvider(
                        LocalLayoutDirection provides LayoutDirection.Rtl,
                        LocalPalette provides LightPalette,
                        LocalFilm provides frame,
                    ) {
                        Box(Modifier.width(392.dp).height(860.dp).background(LightPalette.bg).padding(horizontal = 12.dp, vertical = 14.dp)) {
                            CardView(
                                card, repo, progress, voice, deepOpen = false, flipped = false, follow = null,
                                onToggleDeep = {}, onListen = {}, onSave = {}, onFlip = {}, onGrade = { _, _ -> },
                                onRelated = {}, onPlayVerse = { _, _ -> },
                            )
                        }
                    }
                }
            }
        }
    }
}
