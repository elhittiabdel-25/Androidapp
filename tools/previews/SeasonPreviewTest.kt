package app.usulcards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.usulcards.ui.DarkPalette
import app.usulcards.ui.LightPalette
import app.usulcards.ui.LocalPalette
import app.usulcards.ui.ModuleRowView
import app.usulcards.ui.Season
import app.usulcards.ui.SeasonCardView
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.Density
import org.junit.Rule
import org.junit.Test

/** Renders the season card of the home screen (before Ramadan, and before the ten days of Dhul-Hijjah). */
class SeasonPreviewTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(density = Density.HIGH, softButtons = false),
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    private val ramadan = Season("رمضان بعد نحو 12 يومًا", "تهيّأ له قبل أن يدخل: أحكام الصيام وآدابه، وقيام رمضان والاعتكاف.", listOf("الصيام", "قيام رمضان والاعتكاف"))
    private val hijja = Season("عشر ذي الحجة بعد نحو 9 أيام", "تهيّأ لها قبل أن تدخل: أحكام الحج والعمرة، والأضحية.", listOf("الحج والعمرة", "الأضحية والذكاة"))

    @Test
    fun seasons() {
        paparazzi.snapshot(name = "season-card__s00") { Frame(false, ramadan, listOf(12 to 3, 10 to 0)) }
        paparazzi.snapshot(name = "season-card__s01") { Frame(true, ramadan, listOf(12 to 3, 10 to 0)) }
        paparazzi.snapshot(name = "season-card__s02") { Frame(false, hijja, listOf(12 to 0, 6 to 0)) }
    }

    @Composable
    private fun Frame(dark: Boolean, season: Season, counts: List<Pair<Int, Int>>) {
        val pal = if (dark) DarkPalette else LightPalette
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, LocalPalette provides pal) {
            Box(Modifier.width(380.dp).background(pal.bg).padding(16.dp)) {
                SeasonCardView(season) {
                    season.paths.forEachIndexed { i, p ->
                        val (total, done) = counts[i]
                        ModuleRowView(p, done, total, done > 0, pal.gold) {}
                    }
                }
            }
        }
    }
}
