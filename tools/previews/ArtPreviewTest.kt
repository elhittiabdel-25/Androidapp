package app.usulcards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.usulcards.data.Scene
import app.usulcards.ui.ArtFrame
import app.usulcards.ui.DarkPalette
import app.usulcards.ui.LightPalette
import app.usulcards.ui.LocalPalette
import app.usulcards.ui.beatMillis
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.Density
import org.junit.Rule
import org.junit.Test

/**
 * Renders the illustrated scenes without a device (tools/previews, run by the Scene previews workflow):
 * one still per beat for every scene ("<id>__sNN"), the last one in dark theme too ("<id>__dark"), and for the
 * motion scenes every frame at 12 per second ("<id>__mNNNN"), each beat lasting as long as in the app.
 */
class ArtPreviewTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(density = Density.HIGH, softButtons = false),
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun scenes() {
        for ((id, scene) in PreviewData.scenes) {
            val n = scene.items.size
            for (k in 0..n) {
                val beats = List(n) { i -> if (i < k) 1f else 0f }
                paparazzi.snapshot(name = "${id}__s%02d".format(k)) { Frame(scene, beats, 2f + 1.7f * k, k, dark = false) }
            }
            paparazzi.snapshot(name = "${id}__dark") { Frame(scene, List(n) { 1f }, 3f, n, dark = true) }
            if (id in PreviewData.motion) motion(id, scene)
        }
    }

    private fun motion(id: String, scene: Scene) {
        val n = scene.items.size
        val fps = 12f
        var frame = 0
        var t = 0f
        fun shoot(beats: List<Float>, shown: Int) {
            paparazzi.snapshot(name = "${id}__m%04d".format(frame)) { Frame(scene, beats, t, shown, dark = false) }
            frame++
            t += 1f / fps
        }
        repeat(6) { shoot(List(n) { 0f }, 0) }
        for (k in 0 until n) {
            val steps = (beatMillis(scene.items[k].kind) * fps / 1000f).toInt().coerceAtLeast(6)
            for (j in 1..steps) shoot(List(n) { i -> if (i < k) 1f else if (i == k) j / steps.toFloat() else 0f }, k + 1)
            repeat(if (k == n - 1) 14 else 7) { shoot(List(n) { i -> if (i <= k) 1f else 0f }, k + 1) }
        }
    }

    @Composable
    private fun Frame(scene: Scene, beats: List<Float>, t: Float, shown: Int, dark: Boolean) {
        val pal = if (dark) DarkPalette else LightPalette
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, LocalPalette provides pal) {
            Box(Modifier.width(320.dp).background(pal.surface).padding(10.dp)) {
                Box(Modifier.clip(RoundedCornerShape(14.dp)).background(pal.bg)) {
                    ArtFrame(scene, { beats }, { t }, shown, true)
                }
            }
        }
    }
}
