package app.usulcards.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.usulcards.audio.AudioPlayer
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * One module (learning path) opened from the sections: only its own cards, the reader's progress in it,
 * the reading options, and the card deck. While the voice reads, the page scrolls to the sentence being read.
 */
@Composable
fun ModuleScreen(
    repo: ContentRepo,
    deck: DeckState,
    progress: ProgressStore,
    player: AudioPlayer,
    confetti: ConfettiState,
    snackbar: SnackbarHostState,
    onBack: () -> Unit,
) {
    val pal = LocalPalette.current
    val scope = rememberCoroutineScope()
    val ctl = remember { DeckController(repo, deck, player, progress, scope) }
    val scroll = rememberScrollState()
    val follow = remember { FollowState() }
    DisposableEffect(Unit) { onDispose { player.stop() } }
    val card = deck.current
    val module = deck.filter ?: card.path
    val cat = repo.categoryOf(module)
    val tone = categoryTone(pal, cat?.id ?: "", cat?.let { repo.categories.indexOf(it) } ?: 0).first
    val moduleCards = repo.cardsFor(null, module)

    // A new card opens at its top (not at the scroll offset of the card before), and is remembered.
    LaunchedEffect(deck.index, card.id) {
        progress.rememberCard(module, card.id)
        if (scroll.value > 0) scroll.scrollTo(0)
    }

    // Follow the voice: the sentence being read comes up to the top part of the screen.
    val readingKey = player.currentKey.takeIf { player.owner == card.id }
    LaunchedEffect(readingKey, card.id) {
        val key = readingKey ?: return@LaunchedEffect
        if (!progress.followReading) return@LaunchedEffect
        // the title and the first paragraph stay in place, with the illustration beside them in view
        if (key == "title" || key.startsWith("face")) return@LaunchedEffect
        delay(150)   // a layer that is opening has its final place by now
        if (follow.readerIsScrolling()) return@LaunchedEffect
        val y = follow.topOf(card.id, key) ?: return@LaunchedEffect
        val target = (scroll.value + (y - follow.viewTop) - follow.viewHeight * 0.16f).roundToInt().coerceIn(0, scroll.maxValue)
        if (abs(target - scroll.value) > 12) scroll.animateScrollTo(target, tween(700, easing = FastOutSlowInEasing))
    }

    // Scrolling by hand (drag or fling) pauses the following for a few seconds.
    val byHand = remember(follow) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                follow.touched()
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                follow.touched()
                return Velocity.Zero
            }
        }
    }

    Column(Modifier.fillMaxSize()) {
        ModuleBar(module, cat?.title ?: "", moduleCards.count { it.id in progress.learned }, moduleCards.size, tone, onBack)
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .onGloballyPositioned {
                    follow.viewTop = it.positionInRoot().y
                    follow.viewHeight = it.size.height.toFloat()
                }
                .nestedScroll(byHand),
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scroll)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                // Progress through the module
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

                ReadingOptions(progress, deck, player)

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
                        follow = follow,
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
                                    if (moduleCards.all { it.id in progress.learned }) {
                                        scope.launch { snackbar.showSnackbar("أتممت مسار «$module»") }
                                    } else if (progress.dailyCount == progress.dailyGoal) {
                                        scope.launch { snackbar.showSnackbar("أحسنت! أتممت هدف اليوم") }
                                    }
                                }
                                scope.launch {
                                    delay(700)
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
                        quizKey = deck.index,
                        hasNext = deck.canGo(1),
                        // the quiz: a right answer marks the card learned at once; «التالية» then moves on,
                        // and after a wrong answer the card comes back three cards later
                        onAnswer = { right, at ->
                            if (right && progress.markLearned(card.id)) {
                                confetti.burst(at, listOf(pal.ok, pal.gold, pal.accent))
                                if (moduleCards.all { it.id in progress.learned }) {
                                    scope.launch { snackbar.showSnackbar("أتممت مسار «$module»") }
                                } else if (progress.dailyCount == progress.dailyGoal) {
                                    scope.launch { snackbar.showSnackbar("أحسنت! أتممت هدف اليوم") }
                                }
                            }
                        },
                        onNext = { right ->
                            if (!right) {
                                deck.requeue()
                                scope.launch { snackbar.showSnackbar("ستعود هذه البطاقة بعد ثلاث بطاقات") }
                            }
                            ctl.navigate(1)
                        },
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
    }
}

/** Back, the module's name and section, and how many of its cards are mastered. */
@Composable
private fun ModuleBar(module: String, category: String, done: Int, total: Int, color: Color, onBack: () -> Unit) {
    val pal = LocalPalette.current
    Column(Modifier.fillMaxWidth().background(pal.surface)) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // the screen is right-to-left: "back" points to the right
            Box(
                Modifier.size(42.dp).clip(CircleShape).clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Text("→", fontSize = 22.sp, color = pal.ink)
            }
            Column(Modifier.weight(1f)) {
                Text(
                    module, fontFamily = Fonts.kufi, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 28.sp,
                    color = pal.ink, maxLines = 1, overflow = TextOverflow.Ellipsis,
                )
                if (category.isNotEmpty()) Text(category, fontFamily = Fonts.ui, fontSize = 12.sp, color = pal.muted)
            }
            CountRing(done, total, color)
            Box(Modifier.size(4.dp))
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(pal.line))
    }
}

/** Listening and reading options, folded under one line that shows the main ones. */
@Composable
private fun ReadingOptions(progress: ProgressStore, deck: DeckState, player: AudioPlayer) {
    val pal = LocalPalette.current
    var open by rememberSaveable { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(pal.surface)
            .border(1.dp, pal.line, RoundedCornerShape(14.dp)),
    ) {
        Row(
            Modifier.fillMaxWidth().clickable { open = !open }.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("خيارات القراءة والاستماع", color = pal.ink, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = Fonts.ui,
                modifier = Modifier.weight(1f))
            val s = progress.speed
            Text(
                "×${if (s == 1f) "1" else s.toString()}" + if (progress.continuous) " · متواصل" else "",
                color = pal.muted, fontSize = 12.sp, fontFamily = Fonts.ui,
            )
            Text(if (open) "▴" else "▾", color = pal.muted, fontSize = 14.sp)
        }
        AnimatedVisibility(
            visible = open,
            enter = expandVertically(tween(260)) + fadeIn(tween(260)),
            exit = shrinkVertically(tween(200)) + fadeOut(tween(150)),
        ) {
            Column(Modifier.fillMaxWidth().padding(start = 14.dp, end = 14.dp, bottom = 10.dp)) {
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("السرعة", color = pal.ink, fontSize = 14.sp, fontFamily = Fonts.ui)
                    Row(Modifier.clip(RoundedCornerShape(10.dp)).border(1.dp, pal.line, RoundedCornerShape(10.dp))) {
                        listOf(0.8f, 1f, 1.2f).forEach { s ->
                            val on = progress.speed == s
                            Text(
                                "×${if (s == 1f) "1" else s.toString()}",
                                color = if (on) pal.accent else pal.muted, fontSize = 13.sp, fontFamily = Fonts.ui,
                                fontWeight = if (on) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.background(if (on) pal.accentSoft else pal.surface)
                                    .clickable { progress.updateSpeed(s) }.padding(horizontal = 12.dp, vertical = 6.dp),
                            )
                        }
                    }
                }
                ToggleRow("استماع متواصل", "ينتقل الصوت إلى البطاقة التالية وحده", progress.continuous) { progress.updateContinuous(it) }
                ToggleRow("الشاشة مضاءة أثناء الاستماع", "لتتابع القراءة دون لمس الهاتف", progress.keepScreenOn) { progress.updateKeepScreenOn(it) }
                ToggleRow("تتبّع موضع القراءة", "تتحرك الصفحة مع الجملة المقروءة", progress.followReading) { progress.updateFollowReading(it) }
                ToggleRow("التشكيل", "", progress.showDiacritics) { progress.updateShowDiacritics(it) }
                // applies to the card on screen too, then to every card that opens
                ToggleRow("افتح «تعمّق» تلقائيًا", "", progress.deepByDefault) {
                    progress.updateDeepByDefault(it)
                    if (deck.deepOpen != it) {
                        if (player.isPlaying) player.stop()
                        deck.deepOpen = it
                    }
                }
            }
        }
    }
}

@Composable
private fun ToggleRow(label: String, hint: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    val pal = LocalPalette.current
    Row(
        Modifier.fillMaxWidth().clickable { onChange(!checked) }.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(Modifier.weight(1f)) {
            Text(label, color = pal.ink, fontSize = 14.sp, fontFamily = Fonts.ui)
            if (hint.isNotEmpty()) Text(hint, color = pal.muted, fontSize = 12.sp, lineHeight = 18.sp, fontFamily = Fonts.ui)
        }
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(checkedTrackColor = pal.accent, checkedThumbColor = pal.surface),
        )
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
