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
    private val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

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
    /** Every card opens with its «تعمّق» layer already expanded (off: closed until tapped). */
    var deepByDefault by mutableStateOf(prefs.getBoolean("deepByDefault", false))
        private set
    /** The screen stays on while the voice reads, so the reader can follow along hands-free. */
    var keepScreenOn by mutableStateOf(prefs.getBoolean("keepScreenOn", true))
        private set
    /** The page scrolls by itself to the sentence being read. */
    var followReading by mutableStateOf(prefs.getBoolean("followReading", true))
        private set

    val dailyGoal = DAILY_GOAL

    /** The reader opened the books section once: its "new" glow and banner stop. */
    var booksSeen by mutableStateOf(prefs.getBoolean("booksSeen", false))
        private set

    /** The daily reminder (see reminder/Reminders.kt): off until the reader turns it on, 20:30 by default. */
    var reminderOn by mutableStateOf(prefs.getBoolean("reminderOn", false))
        private set
    var reminderHour by mutableIntStateOf(prefs.getInt("reminderHour", 20))
        private set
    var reminderMinute by mutableIntStateOf(prefs.getInt("reminderMinute", 30))
        private set

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

    fun updateDeepByDefault(v: Boolean) {
        deepByDefault = v
        prefs.edit().putBoolean("deepByDefault", v).apply()
    }

    fun updateKeepScreenOn(v: Boolean) {
        keepScreenOn = v
        prefs.edit().putBoolean("keepScreenOn", v).apply()
    }

    fun updateFollowReading(v: Boolean) {
        followReading = v
        prefs.edit().putBoolean("followReading", v).apply()
    }

    fun markBooksSeen() {
        if (booksSeen) return
        booksSeen = true
        prefs.edit().putBoolean("booksSeen", true).apply()
    }

    fun updateReminder(on: Boolean) {
        reminderOn = on
        prefs.edit().putBoolean("reminderOn", on).apply()
    }

    fun updateReminderTime(hour: Int, minute: Int) {
        reminderHour = hour
        reminderMinute = minute
        prefs.edit().putInt("reminderHour", hour).putInt("reminderMinute", minute).apply()
    }

    /** The module the reader was in last (the reminder points to its next card). */
    fun lastModule(): String? = prefs.getString("lastModule", null)

    /** The card last opened in a module, so the module reopens where the reader left it. */
    fun lastCardIn(module: String): String? = prefs.getString("last:$module", null)

    fun rememberCard(module: String, id: String) {
        if (prefs.getString("last:$module", null) != id || prefs.getString("lastModule", null) != module) {
            prefs.edit().putString("last:$module", id).putString("lastModule", module).apply()
        }
    }

    companion object {
        const val PREFS = "progress"
        const val DAILY_GOAL = 5
    }
}
