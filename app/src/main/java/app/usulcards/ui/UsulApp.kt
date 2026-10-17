package app.usulcards.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.usulcards.audio.AudioPlayer
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore

@Composable
fun UsulApp(repo: ContentRepo, progress: ProgressStore, player: AudioPlayer) {
    UsulTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            val pal = LocalPalette.current
            val deck = remember { DeckState(repo) }
            val confetti = remember { ConfettiState() }
            val snackbar = remember { SnackbarHostState() }
            var tab by remember { mutableIntStateOf(0) }
            Box(Modifier.fillMaxSize().background(pal.bg)) {
                Scaffold(
                    containerColor = pal.bg,
                    snackbarHost = { SnackbarHost(snackbar) },
                    bottomBar = {
                        NavigationBar(containerColor = pal.surface) {
                            val itemColors = NavigationBarItemDefaults.colors(
                                selectedIconColor = pal.accent, selectedTextColor = pal.accent, indicatorColor = pal.accentSoft,
                                unselectedIconColor = pal.muted, unselectedTextColor = pal.muted,
                            )
                            NavigationBarItem(
                                selected = tab == 0, onClick = { tab = 0 }, colors = itemColors,
                                icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                                label = { Text("البطاقات", fontFamily = Fonts.ui) },
                            )
                            NavigationBarItem(
                                selected = tab == 1, onClick = { tab = 1 }, colors = itemColors,
                                icon = { Icon(Icons.Filled.Menu, contentDescription = null) },
                                label = { Text("المسارات", fontFamily = Fonts.ui) },
                            )
                            NavigationBarItem(
                                selected = tab == 2, onClick = { tab = 2 }, colors = itemColors,
                                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                                label = { Text("محفوظاتي", fontFamily = Fonts.ui) },
                            )
                        }
                    },
                ) { inner ->
                    Box(Modifier.padding(inner)) {
                        when (tab) {
                            0 -> DeckScreen(repo, deck, progress, player, confetti, snackbar)
                            1 -> PathsScreen(repo, progress) { path -> deck.setPath(path); tab = 0 }
                            else -> SavedScreen(repo, progress) { id -> deck.jumpTo(id); tab = 0 }
                        }
                    }
                }
                ConfettiLayer(confetti)
            }
        }
    }
}

@Composable
private fun PathsScreen(repo: ContentRepo, progress: ProgressStore, onOpen: (String?) -> Unit) {
    val pal = LocalPalette.current
    val rows = listOf<String?>(null) + repo.paths
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Text("مسارات التعلم", fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = pal.ink,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
        }
        items(rows) { p ->
            val cards = repo.cards.filter { p == null || it.path == p }
            val done = cards.count { it.id in progress.learned }
            val frac = if (cards.isEmpty()) 0f else done.toFloat() / cards.size
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(pal.surface)
                    .border(1.dp, pal.line, RoundedCornerShape(14.dp))
                    .clickable { onOpen(p) }
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(p ?: "كل البطاقات", fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = pal.ink)
                    Text("$done / ${cards.size}", fontFamily = Fonts.ui, fontSize = 13.sp, color = pal.muted)
                }
                Box(Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(pal.line)) {
                    Box(Modifier.fillMaxWidth(frac).height(6.dp).clip(RoundedCornerShape(3.dp)).background(pal.ok))
                }
                if (p != null) {
                    Text(
                        cards.sortedBy { it.order }.joinToString(" · ") { it.title },
                        fontFamily = Fonts.ui, fontSize = 13.sp, lineHeight = 20.sp, color = pal.muted, maxLines = 2,
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedScreen(repo: ContentRepo, progress: ProgressStore, onOpen: (String) -> Unit) {
    val pal = LocalPalette.current
    val saved = repo.cards.filter { it.id in progress.saved }
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Text("محفوظاتي", fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = pal.ink,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
        }
        if (saved.isEmpty()) {
            item {
                Text(
                    "لم تحفظ أي بطاقة بعد. اضغط «احفظ» على أي بطاقة لتجدها هنا.",
                    fontFamily = Fonts.ui, fontSize = 15.sp, lineHeight = 24.sp, color = pal.muted,
                )
            }
        }
        items(saved, key = { it.id }) { c ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(pal.surface)
                    .border(1.dp, pal.line, RoundedCornerShape(14.dp))
                    .clickable { onOpen(c.id) }
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Pill(c.type, pal.accentSoft, pal.accent, bold = true)
                Text(c.title, fontFamily = Fonts.kufi, fontWeight = FontWeight.Medium, fontSize = 18.sp, lineHeight = 28.sp, color = pal.ink)
                Text(c.faceSentences.firstOrNull() ?: "", fontFamily = Fonts.naskh, fontSize = 15.sp, lineHeight = 28.sp, color = pal.muted, maxLines = 2)
            }
        }
    }
}
