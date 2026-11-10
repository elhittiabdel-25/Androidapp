package app.usulcards.ui

import android.Manifest
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import app.usulcards.reminder.Reminders
import java.util.Locale

/** The daily reminder on the home screen: on/off, the time, and a test. On from the first launch, at 20:30. */
@Composable
fun ReminderCard(repo: ContentRepo, progress: ProgressStore) {
    val pal = LocalPalette.current
    val ctx = LocalContext.current
    var blocked by remember { mutableStateOf(progress.reminderOn && !NotificationManagerCompat.from(ctx).areNotificationsEnabled()) }

    fun enable() {
        progress.updateReminder(true)
        Reminders.schedule(ctx)
        blocked = !NotificationManagerCompat.from(ctx).areNotificationsEnabled()
    }

    val ask = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) enable() else blocked = true
    }

    fun turnOn() {
        val needsAsk = Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        if (needsAsk) ask.launch(Manifest.permission.POST_NOTIFICATIONS) else enable()
    }

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(pal.surface)
            .border(1.dp, pal.line, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Filled.Notifications, contentDescription = null, tint = pal.accent)
            Column(Modifier.weight(1f)) {
                Text("تذكير يومي", fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = pal.ink)
                Text(
                    "إشعار كل يوم في الوقت الذي تختاره يدلّك على بطاقتك التالية",
                    fontFamily = Fonts.ui, fontSize = 12.sp, lineHeight = 19.sp, color = pal.muted,
                )
            }
            Switch(
                checked = progress.reminderOn,
                onCheckedChange = { on ->
                    if (on) turnOn() else {
                        progress.updateReminder(false)
                        Reminders.cancel(ctx)
                        blocked = false
                    }
                },
                colors = SwitchDefaults.colors(checkedTrackColor = pal.accent, checkedThumbColor = pal.surface),
            )
        }

        if (progress.reminderOn) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("الوقت", fontFamily = Fonts.ui, fontSize = 14.sp, color = pal.ink, modifier = Modifier.weight(1f))
                Text(
                    String.format(Locale.US, "%02d:%02d", progress.reminderHour, progress.reminderMinute),
                    fontFamily = Fonts.ui, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = pal.accent,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(pal.accentSoft)
                        .clickable {
                            TimePickerDialog(ctx, { _, h, m ->
                                progress.updateReminderTime(h, m)
                                Reminders.schedule(ctx)
                            }, progress.reminderHour, progress.reminderMinute, true).show()
                        }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                )
            }
            Text(
                "جرّب التذكير الآن",
                fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = pal.accent,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        Reminders.saveHint(ctx, repo, progress)
                        blocked = !Reminders.show(ctx, test = true)
                    }
                    .padding(vertical = 4.dp, horizontal = 2.dp),
            )
        }

        if (progress.reminderOn && !Reminders.exactAllowed(ctx) && Build.VERSION.SDK_INT >= 31) {
            Text(
                "ليصل التذكير في دقيقته: اسمح للتطبيق بضبط المنبهات",
                fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = pal.accent,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        runCatching {
                            ctx.startActivity(
                                Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, android.net.Uri.parse("package:" + ctx.packageName))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                            )
                        }
                    }
                    .padding(vertical = 4.dp, horizontal = 2.dp),
            )
        }

        if (blocked) {
            Text(
                "الإشعارات موقوفة لهذا التطبيق، فلن يصلك التذكير. اسمح بها من إعدادات الهاتف.",
                fontFamily = Fonts.ui, fontSize = 12.sp, lineHeight = 19.sp, color = pal.warn,
            )
            Text(
                "افتح إعدادات الإشعارات",
                fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = pal.accent,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        ctx.startActivity(
                            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                .putExtra(Settings.EXTRA_APP_PACKAGE, ctx.packageName)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        )
                    }
                    .padding(vertical = 4.dp, horizontal = 2.dp),
            )
        }
    }
}
