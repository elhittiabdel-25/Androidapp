package app.usulcards.reminder

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import app.usulcards.MainActivity
import app.usulcards.R
import app.usulcards.data.Card
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

/**
 * The daily reminder, scheduled on the phone itself (the app has no server and no internet).
 *
 * Inexact alarms (the only kind an app may set without a special permission) can come late by a share of how far
 * away they are, so a reminder set a day ahead is approached in halves: each hop lands before the target, and only
 * the last one, under 10 minutes away, aims at it. A handful of tiny wake-ups a day, and the reminder comes on time.
 */
object Reminders {
    const val EXTRA_OPEN = "openCard"
    private const val CHANNEL = "daily"
    private const val ACTION_TICK = "app.usulcards.REMINDER_TICK"
    private const val NOTIFICATION_ID = 1
    private const val LAST_HOP_MS = 10 * 60_000L
    private const val LATE_LIMIT_MS = 3 * 3_600_000L     // a reminder this late (phone was off) is skipped

    private fun prefs(ctx: Context) = ctx.getSharedPreferences(ProgressStore.PREFS, Context.MODE_PRIVATE)

    /** Arms the reminder for the next occurrence of the chosen time, or clears it when the reminder is off. */
    fun schedule(ctx: Context) {
        val p = prefs(ctx)
        if (!p.getBoolean("reminderOn", false)) {
            cancel(ctx)
            return
        }
        val now = ZonedDateTime.now()
        var at = now.with(LocalTime.of(p.getInt("reminderHour", 20), p.getInt("reminderMinute", 30)))
        if (!at.isAfter(now.plusMinutes(1))) at = at.plusDays(1)
        val target = at.toInstant().toEpochMilli()
        p.edit().putLong("remindAt", target).apply()
        hop(ctx, target)
    }

    fun cancel(ctx: Context) {
        ctx.getSystemService(AlarmManager::class.java)?.cancel(tick(ctx))
        prefs(ctx).edit().remove("remindAt").apply()
    }

    private fun hop(ctx: Context, target: Long) {
        val now = System.currentTimeMillis()
        val left = target - now
        val at = if (left > LAST_HOP_MS) now + left / 2 else target
        ctx.getSystemService(AlarmManager::class.java)
            ?.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, tick(ctx))
    }

    private fun tick(ctx: Context): PendingIntent = PendingIntent.getBroadcast(
        ctx, 0, Intent(ctx, ReminderReceiver::class.java).setAction(ACTION_TICK),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

    internal fun onTick(ctx: Context) {
        val p = prefs(ctx)
        if (!p.getBoolean("reminderOn", false)) return
        val target = p.getLong("remindAt", 0L)
        val now = System.currentTimeMillis()
        when {
            target == 0L -> schedule(ctx)
            now >= target - 30_000L -> {
                if (now - target < LATE_LIMIT_MS) show(ctx, test = false)
                schedule(ctx)                                     // tomorrow
            }
            else -> hop(ctx, target)
        }
    }

    /**
     * Posts the reminder. On a normal day it is skipped once the daily goal is met; [test] always posts it.
     * Returns false when notifications are not allowed.
     */
    fun show(ctx: Context, test: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return false
        val nm = NotificationManagerCompat.from(ctx)
        if (!nm.areNotificationsEnabled()) return false

        val p = prefs(ctx)
        val today = LocalDate.now()
        val done = if (p.getString("day", "") == today.toString()) p.getInt("dayCount", 0) else 0
        if (!test && done >= ProgressStore.DAILY_GOAL) return true

        ctx.getSystemService(NotificationManager::class.java)?.createNotificationChannel(
            NotificationChannel(CHANNEL, "تذكير يومي", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "تذكير ببطاقة اليوم في الوقت الذي تختاره"
            },
        )

        val streak = p.getInt("streak", 1)
        val streakAtRisk = p.getString("streakLast", null) == today.minusDays(1).toString() && streak >= 2
        val goal = goalLine(done)
        val hintId = p.getString("hintId", null)
        val next = p.getString("hintTitle", null)?.let { "التالية: «$it»" } ?: "جدّد إيمانك ببطاقة اليوم"
        val title = if (streakAtRisk) "سلسلتك ${days(streak)}، لا تقطعها" else goal
        val text = if (streakAtRisk) "$goal\n$next" else next

        val open = Intent(ctx, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .apply { if (hintId != null) putExtra(EXTRA_OPEN, hintId) }
        val content = PendingIntent.getActivity(
            ctx, 1, open, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val n = NotificationCompat.Builder(ctx, CHANNEL)
            .setSmallIcon(R.drawable.ic_notify)
            .setColor(0xFF23439A.toInt())
            .setContentTitle(title)
            .setContentText(next)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(content)
            .setAutoCancel(true)
            .build()
        return try {
            nm.notify(NOTIFICATION_ID, n)
            true
        } catch (_: SecurityException) {
            false
        }
    }

    /** Called when the app goes to the background: remembers the card the reminder will open. */
    fun saveHint(ctx: Context, repo: ContentRepo, progress: ProgressStore) {
        val card = nextCard(repo, progress)
        val title = card?.let { if (progress.showDiacritics) it.diac["title"] ?: it.title else it.title }
        prefs(ctx).edit().putString("hintId", card?.id).putString("hintTitle", title).apply()
    }

    /** The first card not yet mastered, from where the reader stopped in their last module onward. */
    private fun nextCard(repo: ContentRepo, progress: ProgressStore): Card? {
        val paths = repo.paths
        if (paths.isEmpty()) return null
        val start = paths.indexOf(progress.lastModule()).coerceAtLeast(0)
        for (k in paths.indices) {
            val path = paths[(start + k) % paths.size]
            val cards = repo.cardsFor(null, path)
            val from = if (k == 0) cards.indexOfFirst { it.id == progress.lastCardIn(path) }.coerceAtLeast(0) else 0
            val order = cards.drop(from) + cards.take(from)
            order.firstOrNull { it.id !in progress.learned }?.let { return it }
        }
        return null
    }

    private fun goalLine(done: Int): String {
        val left = ProgressStore.DAILY_GOAL - done
        return when {
            done <= 0 -> "ابدأ هدف اليوم: ${ProgressStore.DAILY_GOAL} بطاقات"
            left <= 0 -> "أتممت هدف اليوم، وزيادة الخير خير"
            left == 1 -> "بقيت بطاقة واحدة لهدف اليوم"
            left == 2 -> "بقيت بطاقتان لهدف اليوم"
            else -> "بقيت $left بطاقات لهدف اليوم"
        }
    }

    private fun days(n: Int): String = when {
        n == 2 -> "يومان"
        n in 3..10 -> "$n أيام"
        else -> "$n يومًا"
    }
}

/** Wakes for the reminder's hops, and re-arms it after a restart, an update or a change of time or time zone. */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        when (intent.action) {
            "app.usulcards.REMINDER_TICK" -> Reminders.onTick(ctx)
            else -> Reminders.schedule(ctx)
        }
    }
}
