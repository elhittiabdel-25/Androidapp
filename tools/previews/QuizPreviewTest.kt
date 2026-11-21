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
import app.usulcards.data.Quiz
import app.usulcards.ui.DarkPalette
import app.usulcards.ui.LightPalette
import app.usulcards.ui.LocalPalette
import app.usulcards.ui.QuizChoice
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.Density
import org.junit.Rule
import org.junit.Test

/**
 * Renders the card quiz without a device (tools/previews, run by the Scene previews workflow): for a quiz with
 * long answers and one with short ones, the open question ("__s00"), a right answer ("__s01"), a wrong answer
 * ("__s02"), and the wrong answer in dark theme ("__dark").
 */
class QuizPreviewTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(density = Density.HIGH, softButtons = false),
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    private val long = Quiz(
        "لأي شيء خلق الله النجوم، بحسب قول قتادة؟",
        "خلقها الله لثلاث، فمن تأول فيها غير ذلك أخطأ؛ ومن قال: مطرنا بنوء كذا، فهو كافر بالله مؤمن بالكوكب.",
        listOf(
            "زينة للسماء، ورجوما للشياطين، وعلامات يهتدى بها",
            "لمعرفة الحظوظ والسعود، والإخبار بما سيقع",
            "لإنزال المطر بأنوائها، وتدبير أحوال الناس في الأرض",
        ),
    )
    private val short = Quiz(
        "بم وكل إسرافيل؟",
        "جبريل موكل بالوحي الذي به حياة القلوب، وميكائيل بالقطر الذي به حياة الأرض، وإسرافيل بالنفخ في الصور الذي به حياة الخلق بعد مماتهم.",
        listOf("بالنفخ في الصور", "بالوحي إلى الرسل", "بالقطر والمطر"),
    )

    @Test
    fun quizzes() {
        for ((id, quiz) in listOf("quiz-long" to long, "quiz-short" to short)) {
            paparazzi.snapshot(name = "${id}__s00") { Frame(id, quiz, -1, dark = false) }
            paparazzi.snapshot(name = "${id}__s01") { Frame(id, quiz, 0, dark = false) }
            paparazzi.snapshot(name = "${id}__s02") { Frame(id, quiz, 1, dark = false) }
            paparazzi.snapshot(name = "${id}__dark") { Frame(id, quiz, 2, dark = true) }
        }
    }

    @Composable
    private fun Frame(id: String, quiz: Quiz, pick: Int, dark: Boolean) {
        val pal = if (dark) DarkPalette else LightPalette
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, LocalPalette provides pal) {
            Box(Modifier.width(360.dp).background(pal.surface).padding(12.dp)) {
                QuizChoice(id, quiz, 0, hasNext = true, initialPick = pick, onAnswer = { _, _ -> }, onNext = {})
            }
        }
    }
}
