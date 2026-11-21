package app.usulcards.ui

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import app.usulcards.data.Card
import app.usulcards.data.ContentRepo
import app.usulcards.data.Scene
import java.io.File
import kotlinx.coroutines.launch

/*
 * Sharing a card (owner request 2026-11-17). Text: title, face, book and author, «Re-Iman», Play link. Image: one
 * beat of the card's scene drawn in code (no image files), with its caption and a footer naming the book. The
 * scenes move fast, so the sheet shows every beat as a still; the beat on screen is pre-selected, else the last
 * beat (the card's main idea). No GIF or video.
 */

internal const val PLAY_LINK = "https://play.google.com/store/apps/details?id=app.reiman"

/** The beat each card's scene is showing right now (set by SceneHost), so Share can pre-select it. */
internal object SceneNow {
    val shown = HashMap<String, Int>()
}

private fun sourceLine(card: Card, repo: ContentRepo): String {
    val b = card.sources.firstOrNull()?.let { repo.books[it.book] } ?: return ""
    return if (b.author.isNotBlank()) "من كتاب «${b.title}» لـ${b.author}" else "من كتاب «${b.title}»"
}

private fun bookTitle(card: Card, repo: ContentRepo): String =
    card.sources.firstOrNull()?.let { repo.books[it.book]?.title } ?: ""

internal fun shareText(card: Card, repo: ContentRepo): String = buildString {
    appendLine(stripHarakat(card.title))
    appendLine()
    appendLine(stripHarakat(card.faceSentences.joinToString(" ")))
    val src = sourceLine(card, repo)
    if (src.isNotEmpty()) { appendLine(); appendLine(src) }
    appendLine()
    appendLine("Re-Iman")
    append(PLAY_LINK)
}

internal fun shareCardText(ctx: Context, card: Card, repo: ContentRepo) {
    val send = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, shareText(card, repo))
    ctx.startActivity(Intent.createChooser(send, null))
}

/** Progress of each beat for a still of beat [k]: the beats up to it complete, the rest not yet shown. */
private fun stillBeats(scene: Scene, k: Int): List<Float> = List(scene.items.size) { if (it <= k) 1f else 0f }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShareSheet(card: Card, repo: ContentRepo, showDiacritics: Boolean, onDismiss: () -> Unit) {
    val pal = LocalPalette.current
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val scene = card.scene
    val n = scene?.items?.size ?: 0
    val now = SceneNow.shown[card.id] ?: 0
    var pick by remember(card.id) { mutableIntStateOf(if (now in 1..n) now - 1 else n - 1) }
    val layer = rememberGraphicsLayer()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true), containerColor = pal.bg) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("مشاركة البطاقة", color = pal.ink, fontSize = 18.sp, fontFamily = Fonts.ui)
            if (scene != null && n > 0) {
                Text("اختر لقطة من الرسم", color = pal.muted, fontSize = 14.sp, fontFamily = Fonts.ui)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(scene.items) { k, _ ->
                        Box(
                            Modifier.width(150.dp).clip(RoundedCornerShape(10.dp))
                                .border(BorderStroke(if (k == pick) 3.dp else 1.dp, if (k == pick) pal.gold else pal.line), RoundedCornerShape(10.dp))
                                .clickable { pick = k },
                        ) { ArtFrame(scene, { stillBeats(scene, k) }, { 2f }, k + 1, showDiacritics) }
                    }
                }
                // the image that is shared: the chosen still, its caption, and a footer with the source
                Column(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).drawWithContent {
                        layer.record { this@drawWithContent.drawContent() }
                        drawLayer(layer)
                    }.background(pal.bg),
                ) {
                    ArtFrame(scene, { stillBeats(scene, pick) }, { 2f }, pick + 1, showDiacritics)
                    Text(
                        "Re-Iman · ${bookTitle(card, repo)}\nGoogle Play",
                        style = TextStyle(fontFamily = Fonts.ui, fontSize = 13.sp, color = pal.gold, textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (scene != null && n > 0) ActionButton("شارك الصورة", filled = true, onClick = {
                    scope.launch {
                        val bmp = layer.toImageBitmap().asAndroidBitmap()
                        val dir = File(ctx.cacheDir, "share").apply { mkdirs() }
                        val f = File(dir, "re-iman-${card.id}.png")
                        f.outputStream().use { bmp.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it) }
                        val uri = FileProvider.getUriForFile(ctx, ctx.packageName + ".share", f)
                        val send = Intent(Intent.ACTION_SEND).setType("image/png")
                            .putExtra(Intent.EXTRA_STREAM, uri)
                            .putExtra(Intent.EXTRA_TEXT, shareText(card, repo))
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        send.clipData = ClipData.newRawUri("", uri)
                        ctx.startActivity(Intent.createChooser(send, null))
                        onDismiss()
                    }
                })
                ActionButton("شارك النص", onClick = { shareCardText(ctx, card, repo); onDismiss() })
            }
        }
    }
}
