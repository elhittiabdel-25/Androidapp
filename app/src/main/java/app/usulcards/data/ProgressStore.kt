package app.usulcards.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDate

/** Learning progress kept on the phone: mastered cards, saved cards, points, daily goal and streak. */
class ProgressStore(ctx: Context) {
    private val prefs = ctx.getSharedPreferences("progress", Context.MODE_PRIVATE)

    var learned by mutableStateOf(prefs.getStringSet("learned", emptySet())?.toSet() ?: emptySet())
        private set
    var saved by mutableStateOf(prefs.getStringSet("saved", emptySet())?.toSet() ?: emptySet())
        private set
    var xp by mutableIntStateOf(prefs.getInt("xp", 0))
        private set
    var dailyCount by mutableIntStateOf(0)
        private set
    var streak by mutableIntStateOf(1)
        private set

    var showDiacritics by mutableStateOf(prefs.getBoolean("diacritics", true))
        private set
    var speed by mutableFloatStateOf(prefs.getFloat("speed", 1f))
        private set
    var continuous by mutableStateOf(prefs.getBoolean("continuous", false))
        private set

    val dailyGoal = 5

    init {
        val today = LocalDate.now().toString()
        dailyCount = if (prefs.getString("day", "") == today) prefs.getInt("dayCount", 0) else 0
        val last = prefs.getString("streakLast", null)
        val yesterday = LocalDate.now().minusDays(1).toString()
        streak = when (last) {
            null -> 1
            today, yesterday -> prefs.getInt("streak", 1)
            else -> 1
        }
    }

    /** Returns true when the card is mastered for the first time (it then earns points). */
    fun markLearned(id: String): Boolean {
        if (id in learned) return false
        learned = learned + id
        xp += 10
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()
        val last = prefs.getString("streakLast", null)
        streak = when (last) {
            today -> streak
            yesterday -> streak + 1
            else -> 1
        }
        dailyCount = (if (prefs.getString("day", "") == today) prefs.getInt("dayCount", 0) else 0) + 1
        prefs.edit()
            .putStringSet("learned", learned)
            .putInt("xp", xp)
            .putString("day", today)
            .putInt("dayCount", dailyCount)
            .putString("streakLast", today)
            .putInt("streak", streak)
            .apply()
        return true
    }

    fun toggleSaved(id: String): Boolean {
        saved = if (id in saved) saved - id else saved + id
        prefs.edit().putStringSet("saved", saved).apply()
        return id in saved
    }

    fun updateShowDiacritics(v: Boolean) {
        showDiacritics = v
        prefs.edit().putBoolean("diacritics", v).apply()
    }

    fun updateSpeed(v: Float) {
        speed = v
        prefs.edit().putFloat("speed", v).apply()
    }

    fun updateContinuous(v: Boolean) {
        continuous = v
        prefs.edit().putBoolean("continuous", v).apply()
    }
}
