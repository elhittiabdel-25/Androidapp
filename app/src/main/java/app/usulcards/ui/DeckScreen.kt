package app.usulcards.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.usulcards.audio.AudioPlayer
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DeckScreen(
    repo: ContentRepo,
    deck: DeckState,
    progress: ProgressStore,
    player: AudioPlayer,
    confetti: ConfettiState,
    snackbar: SnackbarHostState,
) {
    val pal = LocalPalette.current
    val scope = rememberCoroutineScope()
    val ctl = remember { DeckController(repo, deck, player, progress, scope) }
    DisposableEffect(Unit) { onDispose { player.stop() } }
    val card = deck.current

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Header(progress)

        // Learning paths as chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val all = listOf<String?>(null) + repo.paths
            items(all) { p ->
                val ids = repo.cards.filter { p == null || it.path == p }.map { it.id }
                val done = ids.count { it in progress.learned }
                val selected = deck.filter == p
                Row(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selected) pal.accentSoft else pal.surface)
                        .border(1.dp, if (selected) pal.accentSoft else pal.line, RoundedCornerShape(12.dp))
                        .clickable { player.stop(); deck.setPath(p) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(p ?: "كل البطاقات", color = if (selected) pal.accent else pal.ink, fontSize = 14.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal, fontFamily = Fonts.ui)
                    Text("$done/${ids.size}", color = pal.muted, fontSize = 12.sp, fontFamily = Fonts.ui)
                }
            }
        }

        // Progress through the deck
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RoundNav("→", enabled = deck.canGo(-1)) { ctl.navigate(-1) }
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                deck.queue.forEachIndexed { i, id ->
                    val color = when {
                        id in progress.learned -> pal.ok
                        i <= deck.index || id in deck.seen -> pal.accent
                        else -> pal.line
                    }
                    Box(Modifier.weight(1f).height(5.dp).clip(RoundedCornerShape(3.dp)).background(color))
                }
            }
            Text("${deck.index + 1} / ${deck.queue.size}", color = pal.muted, fontSize = 13.sp, fontFamily = Fonts.ui)
            RoundNav("←", enabled = deck.canGo(1)) { ctl.navigate(1) }
        }

        // Listening and reading options
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("السرعة", color = pal.muted, fontSize = 13.sp, fontFamily = Fonts.ui)
            Row(Modifier.clip(RoundedCornerShape(10.dp)).border(1.dp, pal.line, RoundedCornerShape(10.dp))) {
                listOf(0.8f, 1f, 1.2f).forEach { s ->
                    val on = progress.speed == s
                    Text(
                        "×${if (s == 1f) "1" else s.toString()}",
                        color = if (on) pal.accent else pal.muted, fontSize = 13.sp, fontFamily = Fonts.ui,
                        fontWeight = if (on) FontWeight.SemiBold else FontWeight.Normal,
                        modifier = Modifier.background(if (on) pal.accentSoft else pal.surface)
                            .clickable { progress.updateSpeed(s) }.padding(horizontal = 10.dp, vertical = 5.dp),
                    )
                }
            }
            Toggle("استماع متواصل", progress.continuous) { progress.updateContinuous(it) }
            Toggle("التشكيل", progress.showDiacritics) { progress.updateShowDiacritics(it) }
        }

        // The deck: two sheets behind the active card
        Box(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Box(
                Modifier.matchParentSize().padding(horizontal = 36.dp).graphicsLayer { translationY = 22f }
                    .clip(RoundedCornerShape(18.dp)).background(pal.surface).border(1.dp, pal.line, RoundedCornerShape(18.dp))
            )
            Box(
                Modifier.matchParentSize().padding(horizontal = 18.dp).graphicsLayer { translationY = 11f }
                    .clip(RoundedCornerShape(18.dp)).background(pal.surface).border(1.dp, pal.line, RoundedCornerShape(18.dp))
            )
            CardView(
                card = card,
                repo = repo,
                progress = progress,
                player = player,
                deepOpen = deck.deepOpen,
                flipped = deck.flipped,
                onToggleDeep = { if (player.isPlaying) player.stop(); deck.deepOpen = !deck.deepOpen },
                onListen = { ctl.toggleListen(card) },
                onSave = {
                    val nowSaved = progress.toggleSaved(card.id)
                    if (nowSaved) scope.launch { snackbar.showSnackbar("حُفظت في محفوظاتك") }
                },
                onFlip = { deck.flipped = true },
                onGrade = { knew, at ->
                    if (knew) {
                        val first = progress.markLearned(card.id)
                        if (first) {
                            confetti.burst(at, listOf(pal.ok, pal.gold, pal.accent))
                            if (progress.dailyCount == progress.dailyGoal) {
                                scope.launch { snackbar.showSnackbar("أحسنت! أتممت هدف اليوم") }
                            }
                        }
                        scope.launch {
                            kotlinx.coroutines.delay(700)
                            if (deck.canGo(1)) {
                                ctl.navigate(1)
                            } else {
                                deck.flipped = false
                            }
                        }
                    } else {
                        deck.requeue()
                        scope.launch { snackbar.showSnackbar("ستعود هذه البطاقة بعد ثلاث بطاقات") }
                        ctl.navigate(1)
                    }
                },
                onRelated = { id -> player.stop(); deck.jumpTo(id) },
                onPlayVerse = { files, key -> ctl.playVerse(files, key) },
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationX = ctl.offsetX.value
                        rotationZ = ctl.offsetX.value / 45f
                        val e = ctl.enter.value
                        alpha = 0.35f + 0.65f * e
                        translationY = (1f - e) * 60f
                        scaleX = 0.95f + 0.05f * e
                        scaleY = 0.95f + 0.05f * e
                    }
                    .pointerInput(card.id, deck.index) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                val dx = ctl.offsetX.value
                                val d = if (dx > 0) 1 else -1
                                if (abs(dx) > 220f && deck.canGo(d)) ctl.navigate(d) else ctl.settle()
                            },
                            onDragCancel = { ctl.settle() },
                            onHorizontalDrag = { change, amount ->
                                change.consume()
                                ctl.drag(amount)
                            },
                        )
                    },
            )
        }
    }
}

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
        Column {
            Text("بطاقات الأصول", fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = pal.ink)
            Text("من مكتبة المحتوى الإسلامي", color = pal.muted, fontSize = 12.sp, fontFamily = Fonts.ui)
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

@Composable
private fun RoundNav(label: String, enabled: Boolean, onClick: () -> Unit) {
    val pal = LocalPalette.current
    Box(
        Modifier
            .size(40.dp)
            .graphicsLayer { alpha = if (enabled) 1f else 0.35f }
            .clip(CircleShape)
            .background(pal.surface)
            .border(1.dp, pal.line, CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, fontSize = 18.sp, color = pal.ink)
    }
}

@Composable
private fun Toggle(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    val pal = LocalPalette.current
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, color = pal.muted, fontSize = 13.sp, fontFamily = Fonts.ui)
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(checkedTrackColor = pal.accent, checkedThumbColor = pal.surface),
        )
    }
}
