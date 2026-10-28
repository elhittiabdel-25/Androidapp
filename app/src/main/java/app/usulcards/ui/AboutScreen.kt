package app.usulcards.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.usulcards.data.ContentRepo

/** Public contact address (shown here and on the store listing). */
const val CONTACT_EMAIL = "Reiman.app@gmail.com"

/** Public privacy policy page; empty until it is published. */
const val PRIVACY_URL = "https://claude.ai/artifact/Q15UeL4ewuizXCnQmK3p58"

private data class Licence(val name: String, val file: String)

private val FONT_LICENCES = listOf(
    Licence("Amiri Quran", "licenses/OFL-amiriquran.txt"),
    Licence("Noto Naskh Arabic", "licenses/OFL-notonaskharabic.txt"),
    Licence("Reem Kufi", "licenses/OFL-reemkufi.txt"),
    Licence("IBM Plex Sans Arabic", "licenses/OFL-ibmplexsansarabic.txt"),
)

private fun openLink(ctx: Context, url: String) {
    try {
        ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (e: Exception) {
        // no browser or mail app: nothing to open
    }
}

private fun versionName(ctx: Context): String = try {
    ctx.packageManager.getPackageInfo(ctx.packageName, 0).versionName ?: ""
} catch (e: Exception) {
    ""
}

/** About Re-Iman: what it is, where every part comes from (with the credits the sources ask for), privacy. */
@Composable
fun AboutScreen(repo: ContentRepo, onClose: () -> Unit) {
    val pal = LocalPalette.current
    val ctx = LocalContext.current
    BackHandler(onBack = onClose)
    Column(
        Modifier
            .fillMaxSize()
            .background(pal.bg)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BrandMark(46.dp)
                BrandTitle()
            }
            Text(
                "رجوع",
                color = pal.accent, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable(onClick = onClose).padding(horizontal = 12.dp, vertical = 8.dp),
            )
        }
        val version = versionName(ctx)
        if (version.isNotEmpty()) Text("الإصدار $version", color = pal.muted, fontFamily = Fonts.ui, fontSize = 13.sp)

        Section("عن التطبيق") {
            Body("بطاقات معرفية قصيرة مأخوذة من كتب أهل العلم في العقيدة، لكل بطاقة أدلتها من القرآن والسنة، ومصدرها بالكتاب والصفحة، وتُقرأ بصوت مسموع مع حركة توضيحية، ثم تُراجَع باختبار قصير.")
            Body("البطاقات تلخيص لكلام المؤلفين، والمرجع عند الاختلاف هو الكتاب نفسه.")
        }

        Section("الكتب") {
            Body("من «موسوعة المحتوى الإسلامي»، مكتبة مجانية على الإنترنت:")
            repo.books.values.forEach { b ->
                Text("• ${b.title} — ${b.author}", color = pal.ink, fontFamily = Fonts.ui, fontSize = 14.sp, lineHeight = 22.sp)
            }
            LinkText("islamcontent.com") { openLink(ctx, "https://islamcontent.com/ar/content") }
        }

        Section("القرآن الكريم") {
            Body("نص الآيات بالرسم العثماني من مشروع «تنزيل» (Tanzil.net)، عبر AlQuran.cloud، كما هو دون أي تغيير.")
            LinkText("tanzil.net") { openLink(ctx, "https://tanzil.net") }
            Body("التلاوة بصوت الشيخ مشاري راشد العفاسي، عبر AlQuran.cloud (Islamic Network).")
            LinkText("alquran.cloud") { openLink(ctx, "https://alquran.cloud") }
            Body("الآيات تُسمع دائمًا بتلاوة حقيقية، لا بصوت آلي.")
        }

        Section("صوت البطاقات") {
            Body("صوت عربي مولَّد آليًّا (Piper، صوت «كريم»، رخصة MIT)، بنصّ مشكول كاملًا مع تصحيح للنطق، خصوصًا لفظ الجلالة والشدّة.")
        }

        Section("الخطوط") {
            Body("رخصة الخطوط المفتوحة SIL Open Font License 1.1. اضغط على الاسم لعرض نص الرخصة:")
            FONT_LICENCES.forEach { LicenceRow(it) }
        }

        Section("الخصوصية") {
            Body("لا يجمع التطبيق أي بيانات شخصية ولا يرسلها، ولا يحتاج إلى الإنترنت، ولا يحتوي على إعلانات. يُحفظ تقدّمك (البطاقات المتعلَّمة والمحفوظة والإعدادات) على جهازك فقط.")
            Body("التذكير اليومي اختياري، ويُجدوَل على الهاتف نفسه دون أي خادم، ولا يطلب التطبيق إذن الإشعارات إلا حين تفعّله.")
            if (PRIVACY_URL.isNotEmpty()) LinkText("سياسة الخصوصية") { openLink(ctx, PRIVACY_URL) }
        }

        if (CONTACT_EMAIL.isNotEmpty()) {
            Section("للتواصل") {
                Body("إن وجدت خطأ في بطاقة أو في النطق، فراسلنا:")
                LinkText(CONTACT_EMAIL) { openLink(ctx, "mailto:$CONTACT_EMAIL") }
            }
        }
        Spacer(Modifier.padding(bottom = 24.dp))
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    val pal = LocalPalette.current
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(pal.surface)
            .border(1.dp, pal.line, RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(title, color = pal.ink, fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        content()
    }
}

@Composable
private fun Body(text: String) {
    val pal = LocalPalette.current
    Text(text, color = pal.ink, fontFamily = Fonts.ui, fontSize = 14.sp, lineHeight = 23.sp)
}

@Composable
private fun LinkText(text: String, onClick: () -> Unit) {
    val pal = LocalPalette.current
    Text(
        text,
        color = pal.accent, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(onClick = onClick).padding(vertical = 4.dp),
    )
}

@Composable
private fun LicenceRow(l: Licence) {
    val pal = LocalPalette.current
    val ctx = LocalContext.current
    var open by remember { mutableStateOf(false) }
    val text = remember(l.file) {
        try {
            ctx.assets.open(l.file).bufferedReader(Charsets.UTF_8).use { it.readText() }
        } catch (e: Exception) {
            ""
        }
    }
    Column(Modifier.fillMaxWidth()) {
        Text(
            (if (open) "▾ " else "▸ ") + l.name,
            color = pal.accent, fontFamily = Fonts.ui, fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth().clickable { open = !open }.padding(vertical = 4.dp),
        )
        AnimatedVisibility(open) {
            Text(
                text,
                style = TextStyle(textDirection = TextDirection.Ltr),
                color = pal.muted, fontFamily = FontFamily.Monospace, fontSize = 10.sp, lineHeight = 14.sp,
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(pal.bg).padding(8.dp),
            )
        }
    }
}
