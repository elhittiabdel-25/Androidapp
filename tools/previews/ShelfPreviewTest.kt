package app.usulcards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.usulcards.data.BookPart
import app.usulcards.data.ShelfBook
import app.usulcards.ui.BookCover
import app.usulcards.ui.BookScreen
import app.usulcards.ui.BooksTabIcon
import app.usulcards.ui.DarkPalette
import app.usulcards.ui.Fonts
import app.usulcards.ui.LightPalette
import app.usulcards.ui.LocalPalette
import app.usulcards.ui.NewBooksCallout
import app.usulcards.ui.ShelfProgress
import app.usulcards.ui.ShelfScreen
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.Density
import org.junit.Rule
import org.junit.Test

/**
 * Renders the books section without a device (tools/previews, run by the Scene previews workflow): the shelf,
 * a book open on its parts, the drawn cover, and the "new" callout over the tab bar (stills, dark theme, and
 * the glow as frames for a GIF).
 */
class ShelfPreviewTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(density = Density.HIGH, softButtons = false),
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    private val book = PreviewShelf.book

    private val prog = ShelfProgress(
        cardCount = { p -> PreviewShelf.counts[p] ?: 0 },
        doneCount = { p -> if (p == book.parts[0].paths.first()) 2 else 0 },
        started = { p -> p == book.parts[0].paths.first() },
    )

    @Test
    fun books() {
        for ((k, dark) in listOf("s00" to false, "s01" to true)) {
            paparazzi.snapshot(name = "books-shelf__$k") { Screen(dark) { ShelfScreen(listOf(book), prog) {} } }
            paparazzi.snapshot(name = "books-book__$k") { Screen(dark) { BookScreen(book, prog, onBack = {}) {} } }
        }
        paparazzi.snapshot(name = "books-cover__s00") { Frame(false) { BookCover(book, Modifier.width(240.dp)) } }
        paparazzi.snapshot(name = "books-cover__s01") { Frame(false) { BookCover(book, Modifier.width(112.dp), compact = true) } }
        paparazzi.snapshot(name = "books-callout__s00") { Callout(false, 0f) }
        paparazzi.snapshot(name = "books-callout__s01") { Callout(false, 0.25f) }
        paparazzi.snapshot(name = "books-callout__s02") { Callout(false, 0.5f) }
        paparazzi.snapshot(name = "books-callout__dark") { Callout(true, 0.25f) }
        for (i in 0 until 24) {
            paparazzi.snapshot(name = "books-callout__m%03d".format(i)) { Callout(false, i / 24f) }
        }
    }

    @Composable
    private fun Screen(dark: Boolean, content: @Composable () -> Unit) {
        val pal = if (dark) DarkPalette else LightPalette
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, LocalPalette provides pal) {
            Box(Modifier.width(380.dp).height(760.dp).background(pal.bg)) { content() }
        }
    }

    @Composable
    private fun Frame(dark: Boolean, content: @Composable () -> Unit) {
        val pal = if (dark) DarkPalette else LightPalette
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, LocalPalette provides pal) {
            Box(Modifier.background(pal.bg).padding(16.dp)) { content() }
        }
    }

    /** The bottom of the home screen: the callout above the tab bar, the books tab glowing in the middle. */
    @Composable
    private fun Callout(dark: Boolean, phase: Float) {
        val pal = if (dark) DarkPalette else LightPalette
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, LocalPalette provides pal) {
            Column(Modifier.width(380.dp).background(pal.bg)) {
                Spacer(Modifier.height(28.dp))
                NewBooksCallout(book, onOpen = {}, onClose = {}, phase = phase)
                Spacer(Modifier.height(4.dp))
                NavigationBar(containerColor = pal.surface) {
                    val colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = pal.accent, selectedTextColor = pal.accent, indicatorColor = pal.accentSoft,
                        unselectedIconColor = pal.muted, unselectedTextColor = pal.muted,
                    )
                    NavigationBarItem(true, {}, { Icon(Icons.Filled.Menu, null) }, label = { Text("الأقسام", fontFamily = Fonts.ui) }, colors = colors)
                    NavigationBarItem(false, {}, { BooksTabIcon(glow = true, phase = phase) }, label = { Text("الكتب", fontFamily = Fonts.ui) }, colors = colors)
                    NavigationBarItem(false, {}, { Icon(Icons.Filled.Favorite, null) }, label = { Text("محفوظاتي", fontFamily = Fonts.ui) }, colors = colors)
                }
            }
        }
    }
}

/** The book as the pack will describe it, with its first part's chapters (filled in by the batch). */
object PreviewShelf {
    val book = ShelfBook(
        id = "basaer", book = "73540", title = "بصائر",
        subtitle = "أكبر مشروع فكري في نقد الإلحاد وبيان بعض أدلة صحة الإسلام",
        author = "د. طلعت هيثم", cover = "basaer",
        parts = listOf(
            BookPart("الجزء الأول", "تفنيد آراء الملحدين في الكون والحياة", listOf("نشأة الكون", "آيات الخالق في الكون", "الحياة والمعلومة")),
            BookPart("الجزء الثاني", "بصائر صحة الإسلام", emptyList()),
            BookPart("الجزء الثالث", "الرد على أشهر شبهات الملحدين", emptyList()),
        ),
    )
    val counts = mapOf("نشأة الكون" to 9, "آيات الخالق في الكون" to 8, "الحياة والمعلومة" to 10)
}
