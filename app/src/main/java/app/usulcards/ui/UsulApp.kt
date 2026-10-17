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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.usulcards.audio.AudioPlayer
import app.usulcards.data.Card
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
                                label = { Text("الأقسام", fontFamily = Fonts.ui) },
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
                            1 -> CategoriesScreen(repo, progress) { cat, path ->
                                if (path != null) deck.setPath(path) else deck.openCategory(cat)
                                tab = 0
                            }
                            else -> SavedScreen(repo, progress) { id -> deck.jumpTo(id); tab = 0 }
                        }
                    }
                }
                ConfettiLayer(confetti)
            }
        }
    }
}

/** Categories, each with its learning paths (modules); a category or a single path opens as a deck. */
@Composable
private fun CategoriesScreen(repo: ContentRepo, progress: ProgressStore, onOpen: (category: String?, path: String?) -> Unit) {
    val pal = LocalPalette.current
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("الأقسام", fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = pal.ink)
                Text(
                    "الأقسام: ${repo.categories.size} · المسارات: ${repo.paths.size} · البطاقات: ${repo.cards.size}",
                    fontFamily = Fonts.ui, fontSize = 13.sp, color = pal.muted,
                )
            }
        }
        item {
            val ids = repo.cards.map { it.id }
            val done = ids.count { it in progress.learned }
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(pal.surface)
                    .border(1.dp, pal.line, RoundedCornerShape(16.dp))
                    .clickable { onOpen(null, null) }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                BrandMark(44.dp)
                Column(Modifier.weight(1f)) {
                    Text("كل البطاقات", fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = pal.ink)
                    Text("بطاقات كل الأقسام معًا", fontFamily = Fonts.ui, fontSize = 12.sp, color = pal.muted)
                }
                CountRing(done, ids.size, pal.accent)
            }
        }
        itemsIndexed(repo.categories, key = { _, c -> c.id }) { i, cat ->
            val (fg, bg) = categoryTone(pal, cat.id, i)
            val catCards = repo.cardsFor(cat.id, null)
            val done = catCards.count { it.id in progress.learned }
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(pal.surface)
                    .border(1.dp, pal.line, RoundedCornerShape(16.dp)),
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(bg)
                        .clickable { onOpen(cat.id, null) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(Modifier.size(44.dp).clip(CircleShape).background(pal.surface), contentAlignment = Alignment.Center) {
                        CategoryGlyph(cat.glyph, fg, 26.dp)
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(cat.title, fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 28.sp, color = pal.ink)
                        if (cat.about.isNotEmpty()) {
                            Text(cat.about, fontFamily = Fonts.ui, fontSize = 12.sp, lineHeight = 19.sp, color = pal.muted)
                        }
                    }
                    CountRing(done, catCards.size, fg)
                }
                cat.paths.filter { it in repo.paths }.forEachIndexed { k, p ->
                    if (k > 0) Box(Modifier.fillMaxWidth().padding(horizontal = 14.dp).height(1.dp).background(pal.line))
                    ModuleRow(p, repo.cardsFor(null, p), progress, fg) { onOpen(cat.id, p) }
                }
            }
        }
        item {
            Text(
                "مصدر المحتوى: كتب «مكتبة المحتوى الإسلامي» (islamcontent.com)، مع الكتاب والصفحة في كل بطاقة.",
                fontFamily = Fonts.ui, fontSize = 12.sp, lineHeight = 19.sp, color = pal.muted,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
            )
        }
    }
}

@Composable
private fun ModuleRow(path: String, cards: List<Card>, progress: ProgressStore, color: Color, onClick: () -> Unit) {
    val pal = LocalPalette.current
    val done = cards.count { it.id in progress.learned }
    val frac = if (cards.isEmpty()) 0f else done.toFloat() / cards.size
    Column(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(path, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = pal.ink)
            Text("$done / ${cards.size}", fontFamily = Fonts.ui, fontSize = 12.sp, color = pal.muted)
        }
        Box(Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)).background(pal.line)) {
            Box(Modifier.fillMaxWidth(frac).height(5.dp).clip(RoundedCornerShape(3.dp)).background(color))
        }
        Text(
            cards.joinToString(" · ") { it.title },
            fontFamily = Fonts.ui, fontSize = 12.sp, lineHeight = 19.sp, color = pal.muted, maxLines = 1, overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CountRing(done: Int, total: Int, color: Color) {
    val pal = LocalPalette.current
    Box(contentAlignment = Alignment.Center) {
        GoalRing(if (total == 0) 0f else done.toFloat() / total, 44.dp, pal.line, color)
        Text("$done/$total", fontFamily = Fonts.ui, fontSize = 11.sp, color = pal.ink)
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
