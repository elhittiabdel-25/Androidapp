package app.usulcards.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import app.usulcards.R

@Immutable
data class Palette(
    val bg: Color, val surface: Color, val ink: Color, val muted: Color, val line: Color,
    val accent: Color, val accentSoft: Color, val gold: Color, val goldSoft: Color,
    val ok: Color, val okSoft: Color, val warn: Color, val warnSoft: Color, val highlight: Color,
)

val LightPalette = Palette(
    bg = Color(0xFFEEF1F6), surface = Color(0xFFFFFFFF), ink = Color(0xFF172031), muted = Color(0xFF5A6577),
    line = Color(0xFFD9DFE8), accent = Color(0xFF23439A), accentSoft = Color(0xFFE4EAF8), gold = Color(0xFF9A6F18),
    goldSoft = Color(0xFFF6EFE0), ok = Color(0xFF2C7A57), okSoft = Color(0xFFE3F2EA), warn = Color(0xFFA3520B),
    warnSoft = Color(0xFFFBEEDD), highlight = Color(0xFFFFF1C2),
)

val DarkPalette = Palette(
    bg = Color(0xFF0D121A), surface = Color(0xFF161D29), ink = Color(0xFFE6EBF3), muted = Color(0xFF9AA5B7),
    line = Color(0xFF283346), accent = Color(0xFF8FA8F2), accentSoft = Color(0xFF1F2A44), gold = Color(0xFFD8AD57),
    goldSoft = Color(0xFF2A2416), ok = Color(0xFF62C79B), okSoft = Color(0xFF16302A), warn = Color(0xFFF0A75A),
    warnSoft = Color(0xFF33240F), highlight = Color(0xFF4A3F17),
)

val LocalPalette = staticCompositionLocalOf { LightPalette }

object Fonts {
    val kufi = FontFamily(Font(R.font.kufi_medium, FontWeight.Medium), Font(R.font.kufi_bold, FontWeight.Bold))
    val naskh = FontFamily(Font(R.font.naskh_regular, FontWeight.Normal), Font(R.font.naskh_bold, FontWeight.Bold))
    val quran = FontFamily(Font(R.font.amiri_quran, FontWeight.Normal))
    val ui = FontFamily(Font(R.font.plex_regular, FontWeight.Normal), Font(R.font.plex_semibold, FontWeight.SemiBold))
}

@Composable
fun UsulTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val p = if (dark) DarkPalette else LightPalette
    val scheme = if (dark) {
        darkColorScheme(
            primary = p.accent, onPrimary = p.surface, secondary = p.gold, background = p.bg, surface = p.surface,
            onBackground = p.ink, onSurface = p.ink, surfaceVariant = p.accentSoft, onSurfaceVariant = p.muted,
            secondaryContainer = p.accentSoft, onSecondaryContainer = p.accent, outline = p.line,
        )
    } else {
        lightColorScheme(
            primary = p.accent, onPrimary = p.surface, secondary = p.gold, background = p.bg, surface = p.surface,
            onBackground = p.ink, onSurface = p.ink, surfaceVariant = p.accentSoft, onSurfaceVariant = p.muted,
            secondaryContainer = p.accentSoft, onSecondaryContainer = p.accent, outline = p.line,
        )
    }
    CompositionLocalProvider(LocalPalette provides p) {
        MaterialTheme(colorScheme = scheme, content = content)
    }
}
