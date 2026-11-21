package app.usulcards.ui

import android.icu.util.Calendar
import android.icu.util.IslamicCalendar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import kotlin.math.roundToLong

/**
 * A season of worship coming up, and the paths that prepare for it (after zadi.net's advice to study a season's
 * rulings before it comes, so as to worship Allah upon knowledge).
 */
internal data class Season(val title: String, val line: String, val paths: List<String>)

private const val SHABAN = 7
private const val RAMADAN = 8
private const val DHU_AL_QIDAH = 10
private const val DHU_AL_HIJJAH = 11

private fun hijri(): IslamicCalendar = IslamicCalendar().apply { calculationType = IslamicCalendar.CalculationType.ISLAMIC_UMALQURA }

/** A count of days in Arabic, with Western digits: يوم، يومين، 3 أيام … 10 أيام، 11 يومًا … */
internal fun daysAr(n: Long): String = when {
    n <= 1L -> "يوم"
    n == 2L -> "يومين"
    n <= 10L -> "$n أيام"
    else -> "$n يومًا"
}

/** The season for [now] (by the Umm al-Qura calendar), or null outside the weeks before and during one. */
internal fun currentSeason(now: Long = System.currentTimeMillis()): Season? {
    val c = hijri().apply { timeInMillis = now }
    val y = c.get(Calendar.YEAR)
    val m = c.get(Calendar.MONTH)
    val d = c.get(Calendar.DAY_OF_MONTH)
    c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0); c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
    fun daysTo(month: Int): Long {
        val t = hijri().apply { clear(); set(y, month, 1) }
        return ((t.timeInMillis - c.timeInMillis) / 86_400_000.0).roundToLong()
    }
    return when {
        m == SHABAN && d >= 10 -> Season(
            "رمضان بعد نحو ${daysAr(daysTo(RAMADAN))}",
            "تهيّأ له قبل أن يدخل: أحكام الصيام وآدابه، وقيام رمضان والاعتكاف.",
            listOf("الصيام", "قيام رمضان والاعتكاف"),
        )
        m == RAMADAN && d < 20 -> Season(
            "رمضان مبارك",
            "صُم وقُم على بصيرة: أحكام الصيام، وقيام رمضان.",
            listOf("الصيام", "قيام رمضان والاعتكاف"),
        )
        m == RAMADAN -> Season(
            "العشر الأواخر",
            "ليلة القدر والاعتكاف، وزكاة الفطر قبل صلاة العيد.",
            listOf("قيام رمضان والاعتكاف", "الصيام"),
        )
        m == DHU_AL_QIDAH && d >= 15 -> Season(
            "عشر ذي الحجة بعد نحو ${daysAr(daysTo(DHU_AL_HIJJAH))}",
            "تهيّأ لها قبل أن تدخل: أحكام الحج والعمرة، والأضحية.",
            listOf("الحج والعمرة", "الأضحية والذكاة"),
        )
        m == DHU_AL_HIJJAH && d <= 13 -> Season(
            if (d <= 10) "عشر ذي الحجة" else "أيام التشريق",
            "أحكام الحج والعمرة، والأضحية وما يجتنبه المضحي.",
            listOf("الحج والعمرة", "الأضحية والذكاة"),
        )
        else -> null
    }
}

/** The season's card on the home screen: a crescent, the season, and its paths with the reader's progress. */
@Composable
internal fun SeasonCard(season: Season, repo: ContentRepo, progress: ProgressStore, onOpen: (String) -> Unit) {
    val pal = LocalPalette.current
    val paths = season.paths.filter { it in repo.paths }
    if (paths.isEmpty()) return
    SeasonCardView(season) {
        paths.forEach { p ->
            val cards = repo.cardsFor(null, p)
            val done = cards.count { it.id in progress.learned }
            ModuleRowView(p, done, cards.size, done > 0, pal.gold) { onOpen(p) }
        }
    }
}

/** The card's frame, apart from the data, so a preview can draw it. */
@Composable
internal fun SeasonCardView(season: Season, rows: @Composable () -> Unit) {
    val pal = LocalPalette.current
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(pal.surface)
            .border(1.5.dp, pal.gold, RoundedCornerShape(16.dp)),
    ) {
        Row(
            Modifier.fillMaxWidth().background(pal.goldSoft).padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(Modifier.size(44.dp).clip(RoundedCornerShape(22.dp)).background(Color(0xFF0E1838)), contentAlignment = Alignment.Center) {
                Crescent(Modifier.size(26.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("موسم الخير", fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = pal.gold)
                Text(season.title, fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 28.sp, color = pal.ink)
                Text(season.line, fontFamily = Fonts.ui, fontSize = 12.sp, lineHeight = 19.sp, color = pal.muted)
            }
        }
        rows()
        Box(Modifier.height(4.dp))
    }
}

@Composable
private fun Crescent(modifier: Modifier) {
    Canvas(modifier.graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }) {
        val r = size.minDimension * 0.42f
        val c = Offset(size.width / 2f, size.height / 2f)
        drawCircle(Color(0xFFF4D68A), r, c)
        drawCircle(Color.Black, r * 0.86f, c + Offset(r * 0.42f, -r * 0.18f), blendMode = BlendMode.Clear)
    }
}
