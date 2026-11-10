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
 * Two alarms aim at the chosen minute: one that may fire while the phone dozes, and one bound to a 10-minute window
 * (or a single exact one where the phone allows exact alarms). Whichever comes first shows the reminder; the other is
 * ignored. (Until v1.4 the reminder hopped toward its time on inexact alarms, which phones delayed by up to an hour or
 * dropped, and it stayed silent once the day's goal was met: users reported that it never came.)
 */
object Reminders {
    const val EXTRA_OPEN = "openCard"
    private const val CHANNEL = "daily"
    private const val ACTION_TICK = "app.usulcards.REMINDER_TICK"
    private const val NOTIFICATION_ID = 1
    private const val LATE_LIMIT_MS = 3 * 3_600_000L     // a reminder this late (phone was off) is skipped

    private fun prefs(ctx: Context) = ctx.getSharedPreferences(ProgressStore.PREFS, Context.MODE_PRIVATE)

    /** Arms the reminder for the next occurrence of the chosen time, or clears it when the reminder is off. */
    fun schedule(ctx: Context) {
        val p = prefs(ctx)
        if (!p.getBoolean("reminderOn", true)) {
            cancel(ctx)
            return
        }
        val now = ZonedDateTime.now()
        var at = now.with(LocalTime.of(p.getInt("reminderHour", 20), p.getInt("reminderMinute", 30)))
        if (!at.isAfter(now.plusSeconds(5))) at = at.plusDays(1)
        if (at.toInstant().toEpochMilli() <= p.getLong("shownFor", 0L)) at = at.plusDays(1)   // today's was shown
        val target = at.toInstant().toEpochMilli()
        p.edit().putLong("remindAt", target).apply()
        arm(ctx, target)
    }

    fun cancel(ctx: Context) {
        val am = ctx.getSystemService(AlarmManager::class.java)
        am?.cancel(tick(ctx, 0))
        am?.cancel(tick(ctx, 2))
        prefs(ctx).edit().remove("remindAt").apply()
    }

    /** True when the phone lets this app set exact alarms (always before Android 12; a setting after it). */
    fun exactAllowed(ctx: Context): Boolean =
        Build.VERSION.SDK_INT < 31 || ctx.getSystemService(AlarmManager::class.java)?.canScheduleExactAlarms() == true

    private fun arm(ctx: Context, target: Long) {
        val am = ctx.getSystemService(AlarmManager::class.java) ?: return
        try {
            if (exactAllowed(ctx)) {
                am.cancel(tick(ctx, 2))
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, target, tick(ctx, 0))
                return
            }
        } catch (_: SecurityException) {
        }
        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, target, tick(ctx, 0))
        am.setWindow(AlarmManager.RTC_WAKEUP, target, 10 * 60_000L, tick(ctx, 2))
    }

    private fun tick(ctx: Context, code: Int): PendingIntent = PendingIntent.getBroadcast(
        ctx, code, Intent(ctx, ReminderReceiver::class.java).setAction(ACTION_TICK),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

    internal fun onTick(ctx: Context) {
        val p = prefs(ctx)
        if (!p.getBoolean("reminderOn", true)) return
        val target = p.getLong("remindAt", 0L)
        val now = System.currentTimeMillis()
        if (target != 0L && now >= target - 30_000L && p.getLong("shownFor", 0L) < target) {
            p.edit().putLong("shownFor", target).apply()
            if (now - target < LATE_LIMIT_MS) show(ctx, test = false)
        }
        schedule(ctx)          // the same target again if this came early, else tomorrow's
    }

    /** Posts the reminder (also when the day's goal is met: the text says so). False when notifications are not allowed. */
    fun show(ctx: Context, test: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return false
        val nm = NotificationManagerCompat.from(ctx)
        if (!nm.areNotificationsEnabled()) return false

        val p = prefs(ctx)
        val today = LocalDate.now()
        val done = if (p.getString("day", "") == today.toString()) p.getInt("dayCount", 0) else 0

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
