package app.usulcards.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.usulcards.audio.PlaybackView
import app.usulcards.data.Card
import app.usulcards.data.ContentRepo
import app.usulcards.data.ProgressStore
import app.usulcards.data.Quiz

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardView(
    card: Card,
    repo: ContentRepo,
    progress: ProgressStore,
    player: PlaybackView,
    deepOpen: Boolean,
    flipped: Boolean,
    follow: FollowState?,
    onToggleDeep: () -> Unit,
    onListen: () -> Unit,
    onSave: () -> Unit,
    onFlip: () -> Unit,
    onGrade: (knewIt: Boolean, at: Offset) -> Unit,
    onRelated: (String) -> Unit,
    onPlayVerse: (files: List<String>, key: String) -> Unit,
    modifier: Modifier = Modifier,
    quizKey: Int = 0,
    hasNext: Boolean = true,
    onAnswer: (right: Boolean, at: Offset) -> Unit = { _, _ -> },
    onNext: (right: Boolean) -> Unit = {},
) {
    val pal = LocalPalette.current
    val learned = card.id in progress.learned
    val saved = card.id in progress.saved
    // highlight only this card's own voice (in continuous listening the next card may already be speaking)
    val playingKey = player.currentKey.takeIf { player.owner == null || player.owner == card.id }
    fun shown(target: String, plain: String) = if (progress.showDiacritics) card.diac[target] ?: plain else plain

    Box(modifier) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(pal.surface)
                .border(if (learned) 2.dp else 1.dp, if (learned) pal.ok else pal.line, RoundedCornerShape(18.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Pill(card.type, pal.accentSoft, pal.accent, bold = true)
                Pill("${repo.categoryOf(card.path)?.title ?: card.topic} · ${card.path}", Color.Transparent, pal.muted, border = pal.line)
                if (card.books.size > 1) Pill("من كتابين", pal.okSoft, pal.ok)
                if (card.needsReview) Pill("تحتاج مراجعتك", pal.warnSoft, pal.warn)
            }

            Text(
                text = shown("title", card.title),
                style = TextStyle(fontFamily = Fonts.kufi, fontWeight = FontWeight.Medium, fontSize = 23.sp, lineHeight = 36.sp, color = pal.ink),
                modifier = Modifier
                    .onGloballyPositioned { follow?.place(card.id, "title", it.positionInRoot().y) }
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (playingKey == "title") pal.highlight else Color.Transparent),
            )

            // Light animation of the card's idea; it follows the voice while the card is read.
            card.scene?.let { scene ->
                key(card.id) { SceneHost(card, scene, player, progress.showDiacritics) }
            }

            // Face: one span per sentence so the sentence being read is highlighted (and found on screen).
            val starts = ArrayList<Int>(card.faceSentences.size)
            val face = buildAnnotatedString {
                card.faceSentences.forEachIndexed { i, s ->
                    if (i > 0) append(" ")
                    starts.add(length)
                    val bg = if (playingKey == "face-$i") pal.highlight else Color.Transparent
                    withStyle(SpanStyle(background = bg)) { append(shown("face:$i", s)) }
                }
            }
            Text(
                face,
                style = TextStyle(fontFamily = Fonts.naskh, fontSize = 19.sp, lineHeight = 36.sp, color = pal.ink),
                onTextLayout = { follow?.faceText(card.id, it, starts) },
                modifier = Modifier.onGloballyPositioned { follow?.placeFace(card.id, it.positionInRoot().y) },
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                ActionButton(if (deepOpen) "إخفاء التفاصيل" else "تعمّق", filled = true, onClick = onToggleDeep)
                ActionButton(if (player.isPlaying) "إيقاف" else "استمع", active = player.isPlaying, onClick = onListen, playing = player.isPlaying)
                ActionButton(if (saved) "محفوظة" else "احفظ", active = saved, onClick = onSave)
            }

            AnimatedVisibility(
                visible = deepOpen,
                enter = expandVertically(tween(320)) + fadeIn(tween(320)),
                exit = shrinkVertically(tween(220)) + fadeOut(tween(160)),
            ) {
                DeepLayer(card, repo, progress, playingKey, follow, { t: String, p: String -> shown(t, p) }, onRelated, onPlayVerse)
            }

            if (card.quiz.choices.size >= 2) {
                QuizChoice(card.id, card.quiz, quizKey, hasNext, onAnswer = onAnswer, onNext = onNext)
            } else {
                QuizFlip(card, flipped, onFlip, onGrade)
            }
        }

        if (learned) Stamp(card.id, Modifier.align(Alignment.TopEnd).padding(14.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DeepLayer(
    card: Card,
    repo: ContentRepo,
    progress: ProgressStore,
    playingKey: String?,
    follow: FollowState?,
    shown: (String, String) -> String,
    onRelated: (String) -> Unit,
    onPlayVerse: (List<String>, String) -> Unit,
) {
    val pal = LocalPalette.current
    val ctx = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(pal.line))
        var section = 0
        Section("شرح أعمق", section++) {
            card.deeper.forEachIndexed { i, d ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.onGloballyPositioned { follow?.place(card.id, "deep-$i", it.positionInRoot().y) },
                ) {
                    Text("•", color = pal.accent, fontSize = 18.sp, modifier = Modifier.padding(top = 4.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        shown("deeper:$i", d),
                        style = TextStyle(fontFamily = Fonts.naskh, fontSize = 17.sp, lineHeight = 32.sp, color = pal.ink),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (playingKey == "deep-$i") pal.highlight else Color.Transparent),
                    )
                }
            }
        }
        Section("الأدلة", section++) {
            var hadithIndex = 0
            card.evidence.forEachIndexed { i, e ->
                val active = playingKey == "ev-$i"
                if (e.kind == "quran") {
                    val files = repo.recitationFiles(e)
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { follow?.place(card.id, "ev-$i", it.positionInRoot().y) }
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (active) pal.highlight else pal.goldSoft)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            "﴿ ${repo.verseText(e)} ﴾",
                            style = TextStyle(fontFamily = Fonts.quran, fontSize = 21.sp, lineHeight = 44.sp, color = pal.ink),
                        )
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("[${e.label}]", color = pal.gold, fontSize = 13.sp, fontFamily = Fonts.ui)
                            if (files.isNotEmpty()) {
                                Text(
                                    "▶ استمع للتلاوة",
                                    color = pal.gold, fontSize = 13.sp, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { onPlayVerse(files, "ev-$i") }.padding(6.dp),
                                )
                            }
                        }
                    }
                } else {
                    val h = hadithIndex++
                    Row(Modifier.onGloballyPositioned { follow?.place(card.id, "ev-$i", it.positionInRoot().y) }) {
                        Box(Modifier.width(3.dp).height(64.dp).background(pal.gold))
                        Spacer(Modifier.width(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                "«${shown("hadith:$h", e.text)}»",
                                style = TextStyle(fontFamily = Fonts.naskh, fontSize = 18.sp, lineHeight = 34.sp, color = pal.ink),
                                modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(if (active) pal.highlight else Color.Transparent),
                            )
                            Text(e.takhrij, color = pal.gold, fontSize = 13.sp, fontFamily = Fonts.ui)
                            if (e.note.isNotEmpty()) Text(e.note, color = pal.warn, fontSize = 14.sp, lineHeight = 22.sp, fontFamily = Fonts.ui)
                        }
                    }
                }
            }
        }
        if (card.verify.isNotEmpty()) {
            Section("التحقق", section++) {
                card.verify.forEach { f ->
                    val (bg, fg) = if (f.level == "fixed") pal.okSoft to pal.ok else pal.warnSoft to pal.warn
                    Text(
                        f.text, color = fg, fontSize = 14.sp, lineHeight = 22.sp, fontFamily = Fonts.ui,
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(bg).padding(10.dp),
                    )
                }
            }
        }
        Section("المصادر", section++) {
            card.sources.forEach { s ->
                val b = repo.books[s.book]
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        "${b?.title ?: s.book} — ${b?.author ?: ""}",
                        color = pal.accent, fontSize = 14.sp, fontFamily = Fonts.ui,
                        modifier = Modifier.weight(1f).clickable {
                            try {
                                ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://islamcontent.com/ar/content/${s.book}")))
                            } catch (e: Exception) {
                                // no browser available
                            }
                        },
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (s.pages.firstOrNull()?.isDigit() == true) "ص ${s.pages}" else s.pages, color = pal.muted, fontSize = 13.sp, fontFamily = Fonts.ui)
                }
            }
        }
        val related = card.related.mapNotNull { repo.byId[it] }
        if (related.isNotEmpty()) {
            Section("بطاقات مرتبطة", section++) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    related.forEach { r ->
                        // a card stays a surprise until its module has been opened
                        val opened = r.path == card.path || r.id in progress.learned || progress.lastCardIn(r.path) != null
                        Text(
                            if (opened) r.title else "🔒 بطاقة من مسار «${r.path}»", color = pal.accent, fontSize = 14.sp, fontFamily = Fonts.ui,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(pal.accentSoft).clickable { onRelated(r.id) }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                        )
                    }
                }
            }
        }
    }
}

/** A section of the deeper layer that slides in shortly after the previous one. */
@Composable
private fun Section(title: String, order: Int, content: @Composable () -> Unit) {
    val pal = LocalPalette.current
    val anim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(order * 70L)
        anim.animateTo(1f, tween(360, easing = FastOutSlowInEasing))
    }
    Column(
        Modifier.graphicsLayer {
            alpha = anim.value
            translationY = (1f - anim.value) * 40f
        },
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(title, color = pal.muted, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = Fonts.ui)
        content()
    }
}

@Composable
private fun QuizFlip(card: Card, flipped: Boolean, onFlip: () -> Unit, onGrade: (Boolean, Offset) -> Unit) {
    val pal = LocalPalette.current
    val density = LocalDensity.current.density
    val rotation = remember(card.id) { Animatable(if (flipped) 180f else 0f) }
    LaunchedEffect(flipped, card.id) {
        rotation.animateTo(if (flipped) 180f else 0f, tween(600, easing = FastOutSlowInEasing))
    }
    var okCenter by remember { mutableStateOf(Offset.Zero) }
    Box(
        Modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 14f * density
            },
    ) {
        if (rotation.value <= 90f) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(pal.bg).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("اختبر نفسك", color = pal.muted, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = Fonts.ui)
                Text(card.quiz.q, style = TextStyle(fontFamily = Fonts.naskh, fontSize = 17.sp, lineHeight = 32.sp, color = pal.ink))
                ActionButton("اقلب البطاقة", onClick = onFlip)
            }
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .graphicsLayer { rotationY = 180f }
                    .clip(RoundedCornerShape(14.dp))
                    .background(pal.okSoft)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("الجواب", color = pal.ok, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = Fonts.ui)
                Text(card.quiz.a, style = TextStyle(fontFamily = Fonts.naskh, fontSize = 17.sp, lineHeight = 32.sp, color = pal.ink))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.onGloballyPositioned { okCenter = it.boundsInRoot().center }) {
                        ActionButton("عرفتها", filled = true, color = pal.ok, onClick = { onGrade(true, okCenter) })
                    }
                    ActionButton("أعدها لاحقًا", onClick = { onGrade(false, okCenter) })
                }
            }
        }
    }
}

/** How one answer of the quiz looks: not answered yet, the right one, the one picked wrongly, or another one. */
internal enum class ChoiceState { Open, Right, Wrong, Other }

/**
 * The quiz at the bottom of the card: the question and its answers in a shuffled order (stable for this visit of
 * the card, [quizKey]). The answer picked is checked at once: the right one turns green, a wrong pick amber with the
 * right one shown in green; then the explanation and «التالية». [initialPick] is for previews only.
 */
@Composable
internal fun QuizChoice(
    cardId: String,
    quiz: Quiz,
    quizKey: Int,
    hasNext: Boolean,
    initialPick: Int = -1,
    onAnswer: (right: Boolean, at: Offset) -> Unit,
    onNext: (right: Boolean) -> Unit,
) {
    val pal = LocalPalette.current
    val order = remember(cardId, quizKey) { quiz.choices.indices.shuffled(kotlin.random.Random(cardId.hashCode() * 31 + quizKey)) }
    var picked by remember(cardId, quizKey) { mutableIntStateOf(initialPick) }   // index into quiz.choices; 0 is the right one
    val answered = picked >= 0
    val right = picked == 0
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(pal.bg).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("اختبر نفسك", color = pal.muted, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = Fonts.ui)
        Text(quiz.q, style = TextStyle(fontFamily = Fonts.naskh, fontSize = 17.sp, lineHeight = 32.sp, color = pal.ink))
        order.forEach { i ->
            key(i) {
                var center by remember { mutableStateOf(Offset.Zero) }
                val state = when {
                    !answered -> ChoiceState.Open
                    i == 0 -> ChoiceState.Right
                    i == picked -> ChoiceState.Wrong
                    else -> ChoiceState.Other
                }
                Choice(quiz.choices[i], state, Modifier.onGloballyPositioned { center = it.boundsInRoot().center }) {
                    if (picked < 0) {
                        picked = i
                        onAnswer(i == 0, center)
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = answered,
            enter = expandVertically(tween(260)) + fadeIn(tween(260)),
            exit = shrinkVertically(tween(160)) + fadeOut(tween(120)),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    if (right) "أحسنت" else "الجواب الصحيح بالأخضر",
                    color = if (right) pal.ok else pal.warn, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = Fonts.ui,
                )
                Text(quiz.a, style = TextStyle(fontFamily = Fonts.naskh, fontSize = 16.sp, lineHeight = 30.sp, color = pal.ink))
                if (!right || hasNext) {
                    ActionButton("التالية", filled = true, color = if (right) pal.ok else pal.accent, onClick = { onNext(right) })
                }
            }
        }
    }
}

@Composable
private fun Choice(text: String, state: ChoiceState, modifier: Modifier, onClick: () -> Unit) {
    val pal = LocalPalette.current
    val bg by animateColorAsState(
        when (state) {
            ChoiceState.Right -> pal.okSoft
            ChoiceState.Wrong -> pal.warnSoft
            else -> pal.surface
        },
        tween(220), label = "choiceBg",
    )
    val edge by animateColorAsState(
        when (state) {
            ChoiceState.Right -> pal.ok
            ChoiceState.Wrong -> pal.warn
            else -> pal.line
        },
        tween(220), label = "choiceEdge",
    )
    val said = when (state) {
        ChoiceState.Right -> "الجواب الصحيح"
        ChoiceState.Wrong -> "اخترته، وهو غير صحيح"
        else -> ""
    }
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(if (state == ChoiceState.Right || state == ChoiceState.Wrong) 1.5.dp else 1.dp, edge, RoundedCornerShape(12.dp))
            .clickable(enabled = state == ChoiceState.Open, role = Role.Button, onClick = onClick)
            .semantics { if (said.isNotEmpty()) stateDescription = said }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text,
            style = TextStyle(
                fontFamily = Fonts.naskh, fontSize = 16.sp, lineHeight = 28.sp,
                color = if (state == ChoiceState.Other) pal.muted else pal.ink,
            ),
            modifier = Modifier.weight(1f),
        )
        when (state) {
            ChoiceState.Right -> Icon(Icons.Filled.Check, contentDescription = null, tint = pal.ok, modifier = Modifier.size(20.dp))
            ChoiceState.Wrong -> Icon(Icons.Filled.Close, contentDescription = null, tint = pal.warn, modifier = Modifier.size(20.dp))
            else -> {}
        }
    }
}

/** "Mastered" stamp that pops onto the card. */
@Composable
private fun Stamp(cardId: String, modifier: Modifier) {
    val pal = LocalPalette.current
    val scale = remember(cardId) { Animatable(2.2f) }
    LaunchedEffect(cardId) { scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)) }
    Text(
        "أتقنتها", color = pal.ok, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = Fonts.ui,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale.value; scaleY = scale.value; rotationZ = -6f
                alpha = (2.2f - scale.value).coerceIn(0f, 1f) + 0.2f
            }
            .border(1.5.dp, pal.ok, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )
}

@Composable
fun Pill(text: String, bg: Color, fg: Color, bold: Boolean = false, border: Color? = null) {
    Text(
        text, color = fg, fontSize = 12.sp, fontFamily = Fonts.ui,
        fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .then(if (border != null) Modifier.border(1.dp, border, RoundedCornerShape(50)) else Modifier)
            .padding(horizontal = 10.dp, vertical = 3.dp),
    )
}

@Composable
fun ActionButton(
    label: String,
    filled: Boolean = false,
    active: Boolean = false,
    playing: Boolean = false,
    color: Color? = null,
    onClick: () -> Unit,
) {
    val pal = LocalPalette.current
    val base = color ?: pal.accent
    val bg = when {
        filled -> base
        active -> pal.goldSoft
        else -> pal.surface
    }
    val fg = when {
        filled -> pal.surface
        active -> pal.gold
        else -> pal.ink
    }
    val pressScale = remember { Animatable(1f) }
    Row(
        Modifier
            .graphicsLayer { scaleX = pressScale.value; scaleY = pressScale.value }
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .border(1.dp, if (filled) base else pal.line, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (playing) Equalizer(fg)
        Text(label, color = fg, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = Fonts.ui)
    }
}

/** Three bouncing bars shown while audio plays. */
@Composable
private fun Equalizer(color: Color) {
    val bars = List(3) { remember { Animatable(0.3f) } }
    bars.forEachIndexed { i, a ->
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(i * 150L)
            while (true) {
                a.animateTo(1f, tween(450))
                a.animateTo(0.3f, tween(450))
            }
        }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.Bottom, modifier = Modifier.height(14.dp)) {
        bars.forEach { a -> Box(Modifier.width(3.dp).height((14 * a.value).dp).clip(RoundedCornerShape(2.dp)).background(color)) }
    }
}
