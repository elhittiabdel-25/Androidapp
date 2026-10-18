package app.usulcards.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.usulcards.audio.AudioPlayer
import app.usulcards.data.Card
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore

/**
 * The app opens on the sections. A module (learning path) shows its cards only once it is opened, so each
 * module keeps its surprises; the saved cards are the reader's own list.
 */
@Composable
fun UsulApp(repo: ContentRepo, progress: ProgressStore, player: AudioPlayer) {
    UsulTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            val pal = LocalPalette.current
            val deck = remember { DeckState(repo) { progress.deepByDefault } }
            val confetti = remember { ConfettiState() }
            val snackbar = remember { SnackbarHostState() }
            var tab by rememberSaveable { mutableIntStateOf(0) }
            var inModule by remember { mutableStateOf(false) }
            var showAbout by remember { mutableStateOf(false) }

            // the voice reads and the reader follows: the screen must not go dark meanwhile
            KeepScreenOn(player.audible && progress.keepScreenOn)

            fun openModule(path: String, cardId: String? = null) {
                deck.open(path, cardId ?: progress.lastCardIn(path))
                inModule = true
            }

            fun openCard(id: String) {
                repo.byId[id]?.let { openModule(it.path, id) }
            }

            BackHandler(enabled = inModule && !showAbout) {
                player.stop()
                inModule = false
            }

            Box(Modifier.fillMaxSize().background(pal.bg)) {
                when {
                    showAbout -> AboutScreen(repo) { showAbout = false }
                    inModule -> Scaffold(containerColor = pal.bg, snackbarHost = { SnackbarHost(snackbar) }) { inner ->
                        Box(Modifier.padding(inner)) {
                            ModuleScreen(repo, deck, progress, player, confetti, snackbar) {
                                player.stop()
                                inModule = false
                            }
                        }
                    }
                    else -> Scaffold(
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
                                    icon = { Icon(Icons.Filled.Menu, contentDescription = null) },
                                    label = { Text("الأقسام", fontFamily = Fonts.ui) },
                                )
                                NavigationBarItem(
                                    selected = tab == 1, onClick = { tab = 1 }, colors = itemColors,
                                    icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                                    label = { Text("محفوظاتي", fontFamily = Fonts.ui) },
                                )
                            }
                        },
                    ) { inner ->
                        Box(Modifier.padding(inner)) {
                            when (tab) {
                                0 -> HomeScreen(repo, progress, onAbout = { showAbout = true }) { path -> openModule(path) }
                                else -> SavedScreen(repo, progress) { id -> openCard(id) }
                            }
                        }
                    }
                }
                ConfettiLayer(confetti)
            }
        }
    }
}

/** The sections, each with its modules and the reader's progress in them (no card titles: they stay a surprise). */
@Composable
private fun HomeScreen(repo: ContentRepo, progress: ProgressStore, onAbout: () -> Unit, onOpen: (path: String) -> Unit) {
    val pal = LocalPalette.current
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { Box(Modifier.padding(top = 12.dp)) { Header(progress) } }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("الأقسام", fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = pal.ink)
                Text(
                    "اختر مسارًا لتبدأ بطاقاته، وتقدّمك يُحفظ في كل مسار.",
                    fontFamily = Fonts.ui, fontSize = 13.sp, lineHeight = 20.sp, color = pal.muted,
                )
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
                    Modifier.fillMaxWidth().background(bg).padding(14.dp),
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
                    ModuleRow(p, repo.cardsFor(null, p), progress, fg) { onOpen(p) }
                }
            }
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(pal.surface)
                    .border(1.dp, pal.line, RoundedCornerShape(16.dp))
                    .clickable(onClick = onAbout)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(Icons.Filled.Info, contentDescription = null, tint = pal.accent)
                Column(Modifier.weight(1f)) {
                    Text("حول Re-Iman والمصادر", fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = pal.ink)
                    Text("الكتب، والتلاوة، والصوت، والتراخيص، والخصوصية", fontFamily = Fonts.ui, fontSize = 12.sp, lineHeight = 19.sp, color = pal.muted)
                }
            }
        }
    }
}

/** Brand, today's goal, streak and points. */
@Composable
private fun Header(progress: ProgressStore) {
    val pal = LocalPalette.current
    val bump = remember { Animatable(1f) }
    val first = remember { mutableListOf(progress.xp) }
    LaunchedEffect(progress.xp) {
        if (progress.xp != first[0]) {
            first[0] = progress.xp
            bump.snapTo(1.35f)
            bump.animateTo(1f, tween(450))
        }
    }
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BrandMark(42.dp)
            BrandTitle()
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(contentAlignment = Alignment.Center) {
                GoalRing(progress.dailyCount.toFloat() / progress.dailyGoal, 46.dp, pal.line, pal.ok)
                Text("${minOf(progress.dailyCount, progress.dailyGoal)}/${progress.dailyGoal}", fontSize = 12.sp, color = pal.ink, fontFamily = Fonts.ui)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${progress.streak}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = pal.warn, fontFamily = Fonts.ui)
                Text("يوم", fontSize = 11.sp, color = pal.muted, fontFamily = Fonts.ui)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.graphicsLayer { scaleX = bump.value; scaleY = bump.value }) {
                Text("${progress.xp}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = pal.gold, fontFamily = Fonts.ui)
                Text("نقطة", fontSize = 11.sp, color = pal.muted, fontFamily = Fonts.ui)
            }
        }
    }
}

/** A module: its name, how far the reader got, and where it reopens. */
@Composable
private fun ModuleRow(path: String, cards: List<Card>, progress: ProgressStore, color: Color, onClick: () -> Unit) {
    val pal = LocalPalette.current
    val done = cards.count { it.id in progress.learned }
    val frac = if (cards.isEmpty()) 0f else done.toFloat() / cards.size
    val state = when {
        cards.isNotEmpty() && done == cards.size -> "أتممته ✓"
        done > 0 || progress.lastCardIn(path) != null -> "تابِع"
        else -> "ابدأ"
    }
    Column(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(path, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = pal.ink, modifier = Modifier.weight(1f))
            Text("$done / ${cards.size}", fontFamily = Fonts.ui, fontSize = 12.sp, color = pal.muted)
            Text(
                state, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = color,
                modifier = Modifier.clip(RoundedCornerShape(50)).border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 3.dp),
            )
        }
        Box(Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)).background(pal.line)) {
            Box(Modifier.fillMaxWidth(frac).height(5.dp).clip(RoundedCornerShape(3.dp)).background(color))
        }
    }
}

@Composable
fun CountRing(done: Int, total: Int, color: Color) {
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
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Pill(c.type, pal.accentSoft, pal.accent, bold = true)
                    Pill(c.path, Color.Transparent, pal.muted, border = pal.line)
                }
                Text(c.title, fontFamily = Fonts.kufi, fontWeight = FontWeight.Medium, fontSize = 18.sp, lineHeight = 28.sp, color = pal.ink)
                Text(c.faceSentences.firstOrNull() ?: "", fontFamily = Fonts.naskh, fontSize = 15.sp, lineHeight = 28.sp, color = pal.muted, maxLines = 2)
            }
        }
    }
}
