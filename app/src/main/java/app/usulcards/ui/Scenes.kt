package app.usulcards.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.usulcards.audio.PlaybackView
import app.usulcards.data.Card
import app.usulcards.data.Scene
import app.usulcards.data.SceneItem
import app.usulcards.data.arabicDigits
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

/*
 * Card animations. A scene is a small set of items that appear one by one:
 *  - while the card's audio plays, each item appears when its sentence (and the right point in it) is read;
 *  - otherwise the scene previews itself once when the card is shown, and a tap replays it.
 * Rules kept by every scene: no people or animals, no depiction of Allah, the angels or the Throne, no music.
 * Illustrated scenes (type "art") live in Art.kt.
 */

private val HARAKAT = Regex("[\\u064B-\\u0652\\u0670]")

fun stripHarakat(s: String): String = s.replace(HARAKAT, "")

private fun keyIndex(keys: List<String>, k: String?): Int {
    if (k.isNullOrEmpty()) return -1
    val i = keys.indexOf(k)
    return if (i >= 0) i else keys.size + 1
}

/** One frame of a recorded card (tools/previews/CardFilmTest): the scene's beats and clock, set from outside. */
internal class FilmFrame(val t: Float, val beats: List<Float>, val shown: Int)

/** Set only when a card is being recorded frame by frame; the app never sets it. */
internal val LocalFilm = staticCompositionLocalOf<FilmFrame?> { null }

@Composable
fun SceneHost(card: Card, scene: Scene, player: PlaybackView, showDiacritics: Boolean) {
    val pal = LocalPalette.current
    val n = scene.items.size
    val keys = remember(card.id) { card.audio.map { it.key } }
    val atIdx = remember(card.id) { scene.items.map { keyIndex(keys, it.at) } }
    var shown by remember(card.id) { mutableIntStateOf(0) }
    var replay by remember(card.id) { mutableIntStateOf(0) }
    var wasFollowing by remember(card.id) { mutableStateOf(false) }
    val following = player.isPlaying && player.owner == card.id

    val film = LocalFilm.current
    if (film != null) shown = film.shown
    if (film == null) LaunchedEffect(card.id, replay, following) {
        if (following) {
            wasFollowing = true
            shown = 0
            while (true) {
                val cur = keyIndex(keys, player.currentKey)
                val pos = player.positionFraction()
                var c = 0
                while (c < n && (atIdx[c] < cur || (atIdx[c] == cur && pos >= scene.items[c].frac))) c++
                if (c > shown) shown = c
                delay(70)
            }
        } else if (wasFollowing) {
            wasFollowing = false
            shown = n
        } else {
            // no voice: the scene plays itself once (illustrated scenes at a calmer pace)
            shown = 0
            delay(450)
            while (shown < n) {
                shown += 1
                delay(if (scene.type == "art") 1800 else 1200)
            }
        }
    }

    val txt: (String) -> String = { s -> if (showDiacritics) s else stripHarakat(s) }
    val summary = remember(card.id) { scene.items.joinToString("، ") { stripHarakat(it.label) } }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(pal.bg)
                .clickable(enabled = !following) { replay += 1 }
                .semantics { contentDescription = summary },
        ) {
            when (scene.type) {
                "art" -> ArtScene(scene, shown, showDiacritics)
                "sides" -> SidesScene(scene, shown, txt)
                "chain" -> ChainScene(scene, shown, txt)
                "timeline" -> TimelineScene(scene, shown, txt)
                "circles" -> CirclesScene(scene, shown, txt)
                "meter" -> MeterScene(scene, shown, txt)
                "transform" -> TransformScene(scene, shown, txt)
                "moon" -> MoonScene(scene, shown, txt)
                "stars" -> StarsScene(scene, shown, txt)
                else -> StepsScene(scene, shown, txt)
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            for (i in 0 until n) {
                Box(
                    Modifier
                        .width(if (i == shown - 1) 16.dp else 8.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (i < shown) pal.gold else pal.line),
                )
            }
            Spacer(Modifier.weight(1f))
            if (!following && shown >= n) {
                Text("اضغط على المشهد لإعادته", color = pal.muted, fontSize = 11.sp, fontFamily = Fonts.ui)
            }
        }
        if (scene.note.isNotEmpty()) {
            Text(txt(scene.note), color = pal.gold, fontSize = 12.sp, lineHeight = 19.sp, fontFamily = Fonts.ui)
        }
    }
}

// ───────────── shared helpers ─────────────

private data class Tone(val fg: Color, val bg: Color)

private fun toneOf(p: Palette, name: String): Tone = when (name) {
    "ok" -> Tone(p.ok, p.okSoft)
    "warn" -> Tone(p.warn, p.warnSoft)
    "gold" -> Tone(p.gold, p.goldSoft)
    else -> Tone(p.accent, p.accentSoft)
}

/** 0 → 1 with a small spring overshoot when [on] becomes true. */
@Composable
private fun revealOf(on: Boolean, label: String): Float {
    val v by animateFloatAsState(if (on) 1f else 0f, spring(dampingRatio = 0.62f, stiffness = 240f), label = label)
    return v
}

private fun endSign(dir: LayoutDirection): Float = if (dir == LayoutDirection.Rtl) -1f else 1f

@Composable
private fun SceneTitle(text: String) {
    val pal = LocalPalette.current
    Text(text, color = pal.muted, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 20.sp)
}

@Composable
internal fun SceneCaption(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    Crossfade(targetState = shown - 1, label = "scene-caption") { i ->
        val text = if (i >= 0) scene.items[i].label else scene.title
        Text(
            txt(text),
            color = pal.ink,
            fontFamily = Fonts.naskh,
            fontSize = 15.sp,
            lineHeight = 26.sp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
        )
    }
}

@Composable
private fun NumberBadge(n: Int, p: Float, color: Color) {
    val pal = LocalPalette.current
    val on = p > 0.5f
    Box(
        Modifier
            .size(30.dp)
            .graphicsLayer {
                val s = 0.72f + 0.28f * p
                scaleX = s
                scaleY = s
            }
            .clip(CircleShape)
            .background(if (on) color else Color.Transparent)
            .border(1.5.dp, if (on) color else pal.line, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(arabicDigits(n), color = if (on) pal.surface else pal.muted, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}

/** Check mark for "ok", cross for "warn", dot otherwise. */
@Composable
private fun ToneIcon(tone: String, color: Color) {
    Canvas(Modifier.size(14.dp)) {
        val w = size.width
        val h = size.height
        val sw = 2.2.dp.toPx()
        when (tone) {
            "ok" -> {
                val path = Path().apply {
                    moveTo(w * 0.12f, h * 0.55f)
                    lineTo(w * 0.42f, h * 0.82f)
                    lineTo(w * 0.9f, h * 0.2f)
                }
                drawPath(path, color, style = Stroke(sw, cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
            "warn" -> {
                drawLine(color, Offset(w * 0.2f, h * 0.2f), Offset(w * 0.8f, h * 0.8f), sw, StrokeCap.Round)
                drawLine(color, Offset(w * 0.8f, h * 0.2f), Offset(w * 0.2f, h * 0.8f), sw, StrokeCap.Round)
            }
            else -> drawCircle(color, radius = w * 0.3f, center = center)
        }
    }
}

// ───────────── steps: list, grid, stairs ─────────────

@Composable
private fun StepsScene(scene: Scene, shown: Int, txt: (String) -> String) {
    Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (scene.title.isNotEmpty()) SceneTitle(txt(scene.title))
        when (scene.layout) {
            "grid" -> StepsGrid(scene, shown, txt)
            "stairs" -> StepsStairs(scene, shown, txt)
            else -> StepsList(scene, shown, txt)
        }
    }
}

@Composable
private fun StepsList(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val fromStart = -endSign(LocalLayoutDirection.current)
    // "head" items are unnumbered lines (an intro or a sub-heading); the others are numbered 1, 2, 3...
    val numbers = remember(scene) {
        var k = 0
        scene.items.map { if (it.kind == "head") 0 else ++k }
    }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        scene.items.forEachIndexed { i, item ->
            val p = revealOf(i < shown, "step$i")
            val active = i == shown - 1
            if (item.kind == "head") {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = p.coerceIn(0f, 1f)
                            translationY = (1f - p) * 10.dp.toPx()
                        }
                        .clip(RoundedCornerShape(10.dp))
                        .background(pal.goldSoft)
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                ) {
                    Text(txt(item.label), color = pal.ink, fontFamily = Fonts.naskh, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 24.sp)
                    if (item.sub.isNotEmpty()) {
                        Text(txt(item.sub), color = pal.muted, fontFamily = Fonts.naskh, fontSize = 13.sp, lineHeight = 21.sp)
                    }
                }
            } else {
                StepRow(scene, item, numbers[i], p, active, fromStart, txt)
            }
        }
    }
}

@Composable
private fun StepRow(scene: Scene, item: SceneItem, number: Int, p: Float, active: Boolean, fromStart: Float, txt: (String) -> String) {
    val pal = LocalPalette.current
    val t = toneOf(pal, item.kind.ifEmpty { scene.tone })
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (active) pal.goldSoft else Color.Transparent)
            .padding(horizontal = 6.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        NumberBadge(number, p, if (active) pal.gold else t.fg)
        Column(
            Modifier
                .weight(1f)
                .graphicsLayer {
                    alpha = p.coerceIn(0f, 1f)
                    translationX = (1f - p) * 26.dp.toPx() * fromStart
                },
        ) {
            Text(txt(item.label), color = pal.ink, fontFamily = Fonts.naskh, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 24.sp)
            if (item.sub.isNotEmpty()) {
                Text(txt(item.sub), color = pal.muted, fontFamily = Fonts.naskh, fontSize = 13.sp, lineHeight = 21.sp)
            }
        }
    }
}

@Composable
private fun StepsGrid(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val rows = scene.items.withIndex().toList().chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { (i, item) ->
                    val p = revealOf(i < shown, "grid$i")
                    val active = i == shown - 1
                    val t = toneOf(pal, item.kind.ifEmpty { scene.tone })
                    Row(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .graphicsLayer {
                                alpha = (0.2f + 0.8f * p).coerceIn(0f, 1f)
                                val s = 0.9f + 0.1f * p
                                scaleX = s
                                scaleY = s
                            }
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (active) pal.goldSoft else pal.surface)
                            .border(1.dp, if (active) pal.gold else if (p > 0.5f) t.fg.copy(alpha = 0.35f) else pal.line, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(arabicDigits(i + 1), color = if (active) pal.gold else t.fg, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text(
                            txt(item.label),
                            color = pal.ink,
                            fontFamily = Fonts.naskh,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            modifier = Modifier.graphicsLayer { alpha = p.coerceIn(0f, 1f) },
                        )
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StepsStairs(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val n = scene.items.size
    val stepH = 54.dp
    BoxWithConstraints(Modifier.fillMaxWidth().height(stepH * n)) {
        val boxW = maxWidth * 0.64f
        val dx = if (n > 1) (maxWidth - boxW) / (n - 1) else 0.dp
        scene.items.forEachIndexed { i, item ->
            val p = revealOf(i < shown, "stair$i")
            val active = i == shown - 1
            val t = toneOf(pal, item.kind.ifEmpty { scene.tone })
            Row(
                Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = dx * i, y = -(stepH * i))
                    .width(boxW)
                    .height(stepH - 6.dp)
                    .graphicsLayer {
                        alpha = (0.18f + 0.82f * p).coerceIn(0f, 1f)
                        translationY = (1f - p) * 18.dp.toPx()
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (active) pal.goldSoft else if (p > 0.5f) t.bg else pal.surface)
                    .border(1.dp, if (active) pal.gold else pal.line, RoundedCornerShape(10.dp))
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                NumberBadge(i + 1, p, if (active) pal.gold else t.fg)
                Text(
                    txt(item.label),
                    color = pal.ink,
                    fontFamily = Fonts.naskh,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    maxLines = 2,
                    modifier = Modifier.graphicsLayer { alpha = p.coerceIn(0f, 1f) },
                )
            }
        }
    }
}

// ───────────── two sides ─────────────

@Composable
private fun SidesScene(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val toEnd = endSign(LocalLayoutDirection.current)
    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (scene.title.isNotEmpty()) SceneTitle(txt(scene.title))
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (s in 0..1) {
                val (title, toneName) = scene.sides.getOrElse(s) { "" to "" }
                val t = toneOf(pal, toneName)
                // items slide in from the line between the two columns
                val fromCentre = if (s == 0) toEnd else -toEnd
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(t.bg.copy(alpha = 0.75f))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ToneIcon(toneName, t.fg)
                        Text(txt(title), color = t.fg, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 21.sp)
                    }
                    scene.items.forEachIndexed { i, item ->
                        if (item.side == s) {
                            val p = revealOf(i < shown, "side$i")
                            val active = i == shown - 1
                            Text(
                                txt(item.label),
                                color = pal.ink,
                                fontFamily = Fonts.naskh,
                                fontSize = 14.sp,
                                lineHeight = 23.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        alpha = p.coerceIn(0f, 1f)
                                        translationX = (1f - p) * 30.dp.toPx() * fromCentre
                                        val sc = if (active) 1f + 0.03f * p else 1f
                                        scaleX = sc
                                        scaleY = sc
                                    }
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(pal.surface)
                                    .border(1.dp, if (active) t.fg else pal.line, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 7.dp),
                            )
                        }
                    }
                }
            }
        }
        scene.items.forEachIndexed { i, item ->
            if (item.side < 0) {
                val p = revealOf(i < shown, "verdict$i")
                Text(
                    txt(item.label),
                    color = pal.ink,
                    textAlign = TextAlign.Center,
                    fontFamily = Fonts.naskh,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = p.coerceIn(0f, 1f)
                            translationY = (1f - p) * 12.dp.toPx()
                        }
                        .clip(RoundedCornerShape(10.dp))
                        .background(pal.goldSoft)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                )
            }
        }
    }
}

// ───────────── chain of causes (with optional fork) ─────────────

private fun groupRows(items: List<SceneItem>): List<List<Int>> {
    val rows = mutableListOf<List<Int>>()
    var i = 0
    while (i < items.size) {
        val a = items[i]
        val b = items.getOrNull(i + 1)
        if (a.side >= 0 && b != null && b.side >= 0 && b.side != a.side) {
            rows.add(listOf(i, i + 1))
            i += 2
        } else {
            rows.add(listOf(i))
            i += 1
        }
    }
    return rows
}

@Composable
private fun ChainScene(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val rows = remember(scene) { groupRows(scene.items) }
    Column(
        Modifier.fillMaxWidth().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        if (scene.title.isNotEmpty()) {
            Box(Modifier.fillMaxWidth().padding(bottom = 6.dp)) { SceneTitle(txt(scene.title)) }
        }
        rows.forEachIndexed { r, idxs ->
            val frac = if (rows.size > 1) r.toFloat() / (rows.size - 1) else 0f
            if (r > 0) {
                val arrowColor = when (scene.tone) {
                    "danger" -> lerp(pal.accent, pal.warn, frac)
                    "good" -> lerp(pal.accent, pal.ok, frac)
                    else -> pal.accent
                }
                ChainArrow(revealOf(idxs.first() < shown, "arrow$r"), arrowColor)
            }
            if (idxs.size == 1) {
                ChainNode(scene, idxs[0], shown, frac, txt, Modifier.fillMaxWidth(0.92f))
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    idxs.forEach { ChainNode(scene, it, shown, frac, txt, Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun ChainNode(scene: Scene, i: Int, shown: Int, frac: Float, txt: (String) -> String, modifier: Modifier) {
    val pal = LocalPalette.current
    val item = scene.items[i]
    val p = revealOf(i < shown, "node$i")
    val active = i == shown - 1
    val (fg, bg) = when {
        item.kind.isNotEmpty() -> toneOf(pal, item.kind).let { it.fg to it.bg }
        scene.tone == "danger" -> lerp(pal.accent, pal.warn, frac) to lerp(pal.accentSoft, pal.warnSoft, frac)
        scene.tone == "good" -> lerp(pal.accent, pal.ok, frac) to lerp(pal.accentSoft, pal.okSoft, frac)
        else -> pal.accent to pal.accentSoft
    }
    Column(
        modifier
            .graphicsLayer {
                alpha = (0.14f + 0.86f * p).coerceIn(0f, 1f)
                translationY = (1f - p) * -14.dp.toPx()
                val s = 0.94f + 0.06f * p
                scaleX = s
                scaleY = s
            }
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(if (active) 1.5.dp else 1.dp, if (active) fg else fg.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            txt(item.label),
            color = pal.ink,
            textAlign = TextAlign.Center,
            fontFamily = Fonts.naskh,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 24.sp,
            modifier = Modifier.graphicsLayer { alpha = p.coerceIn(0f, 1f) },
        )
        if (item.sub.isNotEmpty()) {
            Text(
                txt(item.sub),
                color = fg,
                textAlign = TextAlign.Center,
                fontFamily = Fonts.ui,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                modifier = Modifier.graphicsLayer { alpha = p.coerceIn(0f, 1f) },
            )
        }
    }
}

@Composable
private fun ChainArrow(p: Float, color: Color) {
    val pal = LocalPalette.current
    Canvas(Modifier.size(width = 24.dp, height = 22.dp)) {
        val x = size.width / 2f
        val h = size.height
        drawLine(pal.line, Offset(x, 0f), Offset(x, h), 1.5.dp.toPx())
        val end = h * p.coerceIn(0f, 1f)
        if (end > 1f) {
            drawLine(color, Offset(x, 0f), Offset(x, end), 2.dp.toPx(), StrokeCap.Round)
            if (p > 0.8f) {
                val a = 5.dp.toPx()
                val path = Path().apply {
                    moveTo(x - a, end - a * 1.2f)
                    lineTo(x, end)
                    lineTo(x + a, end - a * 1.2f)
                }
                drawPath(path, color, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
        }
    }
}

// ───────────── timeline ─────────────

@Composable
private fun TimelineScene(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val items = scene.items
    val ends = items.runningFold(0f) { a, item -> a + item.value }
    val total = ends.last().coerceAtLeast(1f)
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val tm = rememberTextMeasurer()
    val fills = items.indices.map { i ->
        animateFloatAsState(if (i < shown) 1f else 0f, tween(700, easing = FastOutSlowInEasing), label = "seg$i")
    }
    val dot by animateFloatAsState(ends[shown.coerceIn(0, items.size)] / total, tween(700, easing = FastOutSlowInEasing), label = "dot")
    val colors = listOf(pal.accent, pal.gold, pal.ok, pal.warn)
    val tickStyle = TextStyle(fontFamily = Fonts.ui, fontSize = 11.sp, color = pal.muted)
    val ticks = remember(ends, tickStyle) { ends.map { tm.measure(arabicDigits(it.toInt()), tickStyle) } }
    Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (scene.title.isNotEmpty()) SceneTitle(txt(scene.title))
        Canvas(Modifier.fillMaxWidth().height(66.dp)) {
            val w = size.width
            val barTop = 22.dp.toPx()
            val barH = 12.dp.toPx()
            val gap = 2.dp.toPx()
            fun xOf(f: Float) = if (rtl) w * (1f - f) else w * f
            drawRoundRect(pal.line, topLeft = Offset(0f, barTop), size = Size(w, barH), cornerRadius = CornerRadius(barH / 2f, barH / 2f))
            items.forEachIndexed { i, item ->
                val a = xOf(ends[i] / total)
                val b = xOf((ends[i] + item.value * fills[i].value) / total)
                val left = min(a, b) + gap / 2f
                val right = max(a, b) - gap / 2f
                if (right - left > 1f) {
                    drawRoundRect(colors[i % colors.size], topLeft = Offset(left, barTop), size = Size(right - left, barH), cornerRadius = CornerRadius(barH / 2f, barH / 2f))
                }
            }
            var lastTopX = -1e6f
            ends.forEachIndexed { k, v ->
                val x = xOf(v / total)
                drawLine(pal.muted.copy(alpha = 0.5f), Offset(x, barTop - 4.dp.toPx()), Offset(x, barTop + barH + 4.dp.toPx()), 1.dp.toPx())
                val tl = ticks[k]
                val tx = (x - tl.size.width / 2f).coerceIn(0f, max(0f, w - tl.size.width))
                val below = abs(x - lastTopX) < 26.dp.toPx()
                if (!below) lastTopX = x
                drawText(tl, topLeft = Offset(tx, if (below) barTop + barH + 6.dp.toPx() else 0f))
            }
            val cx = xOf(dot)
            val cy = barTop + barH / 2f
            drawCircle(pal.gold.copy(alpha = 0.3f), radius = 11.dp.toPx(), center = Offset(cx, cy))
            drawCircle(pal.gold, radius = 6.dp.toPx(), center = Offset(cx, cy))
            drawCircle(pal.surface, radius = 2.5.dp.toPx(), center = Offset(cx, cy))
        }
        Crossfade(targetState = shown - 1, label = "timeline-caption") { c ->
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (c >= 0) {
                    Text(txt(items[c].label), color = colors[c % colors.size], fontFamily = Fonts.naskh, fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 26.sp)
                    if (items[c].sub.isNotEmpty()) {
                        Text(txt(items[c].sub), color = pal.ink, fontFamily = Fonts.naskh, fontSize = 14.sp, lineHeight = 24.sp)
                    }
                } else {
                    Text(" ", fontSize = 16.sp, lineHeight = 26.sp)
                }
            }
        }
    }
}

// ───────────── nested circles ─────────────

@Composable
private fun CirclesScene(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val items = scene.items
    val n = items.size.coerceAtLeast(1)
    val tm = rememberTextMeasurer()
    val colors = listOf(pal.accent, pal.gold, pal.ok, pal.warn)
    val ps = items.indices.map { i ->
        animateFloatAsState(if (i < shown) 1f else 0f, spring(dampingRatio = 0.55f, stiffness = 170f), label = "ring$i")
    }
    val labels = items.map { txt(it.label) }
    val style = TextStyle(fontFamily = Fonts.naskh, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = pal.ink, textAlign = TextAlign.Center)
    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (scene.title.isNotEmpty()) SceneTitle(txt(scene.title))
        BoxWithConstraints(Modifier.fillMaxWidth().height(200.dp)) {
            val density = LocalDensity.current
            val outer = with(density) { min(maxWidth.toPx(), maxHeight.toPx()) } / 2f
            val step = outer * 0.74f / n
            val layouts = remember(labels, style, outer) {
                labels.mapIndexed { i, s ->
                    val r = outer - step * i
                    tm.measure(s, style, constraints = Constraints(maxWidth = (r * 1.4f).toInt().coerceAtLeast(1)))
                }
            }
            Canvas(Modifier.fillMaxSize()) {
                val c = center
                for (i in items.indices) {
                    val p = ps[i].value
                    val base = outer - step * i
                    if (p <= 0.01f) {
                        drawCircle(pal.line, radius = base, center = c, style = Stroke(1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 8f))))
                        continue
                    }
                    val a = p.coerceIn(0f, 1f)
                    val r = base * (0.55f + 0.45f * p)
                    val col = colors[i % colors.size]
                    drawCircle(col.copy(alpha = 0.1f + 0.1f * a), radius = r, center = c)
                    drawCircle(col.copy(alpha = 0.7f * a), radius = r, center = c, style = Stroke(if (i == shown - 1) 2.5.dp.toPx() else 1.5.dp.toPx()))
                }
                for (i in items.indices) {
                    val a = ps[i].value.coerceIn(0f, 1f)
                    if (a <= 0.01f) continue
                    val tl = layouts[i]
                    val y = if (i == items.size - 1) {
                        c.y - tl.size.height / 2f
                    } else {
                        c.y - (outer - step * i) + (step - tl.size.height) / 2f
                    }
                    drawText(tl, topLeft = Offset(c.x - tl.size.width / 2f, y), alpha = a)
                }
            }
        }
        Crossfade(targetState = shown - 1, label = "circles-caption") { c ->
            val text = if (c >= 0) listOf(items[c].label, items[c].sub).filter { it.isNotEmpty() }.joinToString(" · ") else " "
            Text(
                txt(text),
                color = if (c >= 0) colors[c % colors.size] else pal.ink,
                textAlign = TextAlign.Center,
                fontFamily = Fonts.naskh,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

// ───────────── meter (rises and falls) ─────────────

@Composable
private fun DirIcon(kind: String, color: Color) {
    Canvas(Modifier.size(16.dp)) {
        val w = size.width
        val h = size.height
        when (kind) {
            "up" -> drawPath(Path().apply { moveTo(w * 0.5f, h * 0.15f); lineTo(w * 0.9f, h * 0.8f); lineTo(w * 0.1f, h * 0.8f); close() }, color)
            "down" -> drawPath(Path().apply { moveTo(w * 0.5f, h * 0.85f); lineTo(w * 0.9f, h * 0.2f); lineTo(w * 0.1f, h * 0.2f); close() }, color)
            else -> drawRoundRect(color, topLeft = Offset(0f, h * 0.4f), size = Size(w, h * 0.2f), cornerRadius = CornerRadius(h * 0.1f, h * 0.1f))
        }
    }
}

@Composable
private fun MeterScene(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val items = scene.items
    val target = items.take(shown).fold(0.5f) { lvl, item ->
        when (item.kind) {
            "up" -> 0.86f
            "down" -> 0.4f
            else -> lvl
        }
    }
    val level by animateFloatAsState(target, spring(dampingRatio = 0.5f, stiffness = 90f), label = "level")
    val floorOn = items.take(shown).any { it.kind == "hold" }
    val floorA by animateFloatAsState(if (floorOn) 1f else 0f, tween(600), label = "floor")
    val inf = rememberInfiniteTransition(label = "meter")
    val phase by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(1300, easing = LinearEasing)), label = "phase")
    val motion = items.getOrNull(shown - 1)?.kind ?: ""
    Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (scene.title.isNotEmpty()) SceneTitle(txt(scene.title))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Canvas(Modifier.width(56.dp).height(170.dp)) {
                val w = size.width
                val h = size.height
                val r = w / 2f
                val inset = 4.dp.toPx()
                val room = h - 2f * inset
                drawRoundRect(pal.line, cornerRadius = CornerRadius(r, r), style = Stroke(2.dp.toPx()))
                val fillH = room * level.coerceIn(0f, 1f)
                val top = h - inset - fillH
                drawRoundRect(
                    Brush.verticalGradient(listOf(pal.ok, pal.accent), startY = top, endY = h),
                    topLeft = Offset(inset, top),
                    size = Size(w - 2f * inset, fillH),
                    cornerRadius = CornerRadius(r - inset, r - inset),
                )
                if (floorA > 0.01f) {
                    val fy = h - inset - room * 0.18f
                    drawLine(pal.gold.copy(alpha = floorA), Offset(-4.dp.toPx(), fy), Offset(w + 4.dp.toPx(), fy), 2.5.dp.toPx(), StrokeCap.Round)
                }
                if (motion == "up" || motion == "down") {
                    val col = if (motion == "up") pal.surface else pal.warn
                    val s = 6.dp.toPx()
                    val cx = w / 2f
                    for (k in 0 until 3) {
                        val f = (phase + k / 3f) % 1f
                        val y = if (motion == "up") h - inset - f * fillH else top - 30.dp.toPx() + f * 30.dp.toPx()
                        val path = Path().apply {
                            if (motion == "up") {
                                moveTo(cx - s, y + s / 2f); lineTo(cx, y - s / 2f); lineTo(cx + s, y + s / 2f)
                            } else {
                                moveTo(cx - s, y - s / 2f); lineTo(cx, y + s / 2f); lineTo(cx + s, y - s / 2f)
                            }
                        }
                        drawPath(path, col.copy(alpha = 0.85f * (1f - f)), style = Stroke(2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                    }
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items.forEachIndexed { i, item ->
                    val p = revealOf(i < shown, "meter$i")
                    val t = toneOf(
                        pal,
                        when (item.kind) {
                            "up" -> "ok"
                            "down" -> "warn"
                            else -> "gold"
                        },
                    )
                    Row(
                        Modifier.graphicsLayer {
                            alpha = p.coerceIn(0f, 1f)
                            translationY = (1f - p) * 12.dp.toPx()
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        DirIcon(item.kind, t.fg)
                        Text(txt(item.label), color = pal.ink, fontFamily = Fonts.naskh, fontSize = 15.sp, lineHeight = 24.sp)
                    }
                }
            }
        }
    }
}

// ───────────── transform: a vessel filled, then changed ─────────────

@Composable
private fun TransformScene(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val items = scene.items
    fun isOn(kind: String): Boolean {
        val i = items.indexOfFirst { it.kind == kind }
        return i in 0 until shown
    }
    val ring = revealOf(isOn("ring"), "ring")
    val fill by animateFloatAsState(if (isOn("fill")) 1f else 0f, tween(1600, easing = LinearEasing), label = "fill")
    val turn by animateFloatAsState(if (isOn("turn")) 1f else 0f, tween(1800, easing = FastOutSlowInEasing), label = "turn")
    val safe = revealOf(isOn("safe"), "safe")
    val inf = rememberInfiniteTransition(label = "transform")
    val pulse by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(2200, easing = LinearEasing)), label = "pulse")
    Column(Modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(190.dp).padding(top = 10.dp)) {
            val c = center
            val big = min(size.width, size.height) * 0.46f
            drawCircle(pal.line, radius = big, center = c, style = Stroke(1.5.dp.toPx()))
            if (ring > 0.01f) {
                val a = ring.coerceIn(0f, 1f)
                val breathe = 0.7f + 0.3f * sin(pulse * 2f * PI.toFloat())
                drawCircle(pal.gold.copy(alpha = 0.18f * a * breathe), radius = big + 8.dp.toPx(), center = c, style = Stroke(10.dp.toPx()))
                drawCircle(pal.gold.copy(alpha = a), radius = big, center = c, style = Stroke(2.5.dp.toPx()))
            }
            val count = 96
            for (k in 0 until count) {
                val vis = (fill * count - k).coerceIn(0f, 1f)
                if (vis <= 0f) continue
                val rr = big * 0.9f * sqrt((k + 0.5f) / count)
                val th = k * 2.39996f
                val pt = Offset(c.x + rr * cos(th), c.y + rr * sin(th))
                val m = ((turn * big * 1.25f - rr) / (big * 0.25f)).coerceIn(0f, 1f)
                drawCircle(lerp(pal.warn, pal.gold, m).copy(alpha = vis * (0.75f + 0.25f * m)), radius = (3f + 1.2f * m).dp.toPx(), center = pt)
            }
            if (turn > 0.01f) {
                drawCircle(Brush.radialGradient(listOf(pal.gold.copy(alpha = 0.22f * turn), Color.Transparent), center = c, radius = big * 1.1f), radius = big * 1.1f, center = c)
            }
            if (safe > 0.01f) {
                drawCircle(pal.ok.copy(alpha = 0.6f * safe.coerceIn(0f, 1f)), radius = big + 4.dp.toPx(), center = c, style = Stroke(3.dp.toPx()))
            }
        }
        SceneCaption(scene, shown, txt)
    }
}

// ───────────── night scenes ─────────────

private val SkyTop = Color(0xFF16224A)
private val SkyBottom = Color(0xFF3A4F86)
private val MoonColor = Color(0xFFFBF3D5)
private val HillFar = Color(0xFF263866)
private val HillNear = Color(0xFF1A2850)
private val RoadColor = Color(0xFF121C3A)
private val RoadLine = Color(0x59FFECBE)
private val LampColor = Color(0xFFF2C45A)
private val Footprint = Color(0x8CFFECBE)
private val SeaTop = Color(0xFF1B2A55)
private val SeaBottom = Color(0xFF0F1838)
private val CloudColor = Color(0xFF55648F)
private val BoatColor = Color(0xFF0B1230)
private val RainColor = Color(0xFFBFD0F5)

private class Star(val x: Float, val y: Float, val r: Float, val p: Float)

private val STARS: List<Star> = run {
    var seed = 7L
    fun rnd(): Float {
        seed = (seed * 16807L) % 2147483647L
        return seed / 2147483647f
    }
    List(70) { Star(rnd(), rnd() * 0.55f, 0.6f + rnd() * 1.1f, rnd() * 6.3f) }
}

private val CLOUDS = listOf(
    Triple(0.12f, 0.08f, 0.13f), Triple(0.28f, 0.05f, 0.16f), Triple(0.45f, 0.09f, 0.14f),
    Triple(0.62f, 0.04f, 0.17f), Triple(0.8f, 0.08f, 0.15f), Triple(0.95f, 0.05f, 0.12f),
)

private fun frac01(x: Float): Float = x - floor(x)

/** Seconds since the scene appeared, advanced every frame. */
@Composable
private fun rememberClock(): State<Float> {
    val t = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        var last = withFrameNanos { it }
        while (true) {
            withFrameNanos { now ->
                t.floatValue += ((now - last) / 1_000_000_000f).coerceAtMost(0.05f)
                last = now
            }
        }
    }
    return t
}

private fun DrawScope.drawHill(color: Color, base: Float, amp: Float, f1: Float, f2: Float, k: Float, scroll: Float, dens: Float) {
    val w = size.width
    val h = size.height
    val path = Path().apply {
        moveTo(0f, h)
        var x = 0f
        while (x <= w + 8f * dens) {
            val xd = (x - scroll * k) / dens
            lineTo(x, h * base + h * amp * (sin(xd * f1 + 1.3f) * 0.6f + sin(xd * f2 + 2.21f) * 0.4f))
            x += 8f * dens
        }
        lineTo(w, h)
        close()
    }
    drawPath(path, color)
}

private fun DrawScope.drawTag(tl: TextLayoutResult?, at: Offset, alpha: Float) {
    if (tl == null || alpha <= 0.01f) return
    drawText(tl, topLeft = Offset(at.x - tl.size.width / 2f, at.y - tl.size.height / 2f), alpha = alpha.coerceIn(0f, 1f))
}

/** The book's example: the moon stays in the sky yet goes "with" the traveller as the land passes by. */
@Composable
private fun MoonScene(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val clock = rememberClock()
    val pSky by animateFloatAsState(if (shown >= 1) 1f else 0f, tween(600), label = "sky")
    val pWith by animateFloatAsState(if (shown >= 2) 1f else 0f, tween(700), label = "with")
    val pApart by animateFloatAsState(if (shown >= 3) 1f else 0f, tween(700), label = "apart")
    val walking = rememberUpdatedState(shown >= 2)
    val scroll = remember { mutableFloatStateOf(0f) }
    val dens = LocalDensity.current.density
    LaunchedEffect(Unit) {
        var last = withFrameNanos { it }
        var speed = 0f
        while (true) {
            withFrameNanos { now ->
                val dt = ((now - last) / 1_000_000_000f).coerceAtMost(0.05f)
                last = now
                val target = if (walking.value) 1f else 0f
                speed += (target - speed) * min(1f, dt * 1.6f)
                scroll.floatValue += speed * dt * 60f * dens
            }
        }
    }
    val tm = rememberTextMeasurer()
    val tagStyle = TextStyle(fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = MoonColor)
    val tags = scene.items.map { txt(it.sub) }
    val tagLayouts = remember(tags) { tags.map { tm.measure(it, tagStyle) } }
    Column(Modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().aspectRatio(16f / 10f)) {
            val w = size.width
            val h = size.height
            val t = clock.value
            val sc = scroll.floatValue
            drawRect(Brush.verticalGradient(listOf(SkyTop, SkyBottom)))
            for (s in STARS) {
                val a = 0.45f + 0.55f * (0.5f + 0.5f * sin(t * 1.6f + s.p))
                drawCircle(MoonColor.copy(alpha = a), radius = s.r * dens, center = Offset(s.x * w, s.y * h))
            }
            val moonC = Offset(w * 0.28f, h * 0.24f)
            val moonR = max(14f * dens, w * 0.055f)
            val emph = if (shown == 1) 1f + 0.25f * sin(t * 4f) else 1f
            val haloR = moonR * 3.2f * emph
            drawCircle(Brush.radialGradient(listOf(MoonColor.copy(alpha = 0.35f), Color.Transparent), center = moonC, radius = haloR), radius = haloR, center = moonC)
            drawCircle(MoonColor, radius = moonR, center = moonC)
            drawCircle(Color(0x1F7D7356), radius = moonR * 0.22f, center = moonC + Offset(-0.3f * moonR, -0.2f * moonR))
            drawCircle(Color(0x1F7D7356), radius = moonR * 0.16f, center = moonC + Offset(0.25f * moonR, 0.15f * moonR))
            drawHill(HillFar, 0.62f, 0.06f, 0.010f, 0.023f, 0.18f, sc, dens)
            drawHill(HillNear, 0.72f, 0.05f, 0.016f, 0.037f, 0.55f, sc, dens)
            val roadY = h * 0.8f
            drawRect(RoadColor, topLeft = Offset(0f, roadY), size = Size(w, h - roadY))
            val period = 30f * dens
            val phase = ((-sc) % period + period) % period
            val laneY = roadY + (h - roadY) * 0.55f
            drawLine(RoadLine, Offset(0f, laneY), Offset(w, laneY), 2f * dens, pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f * dens, 16f * dens), phase))
            val lampX = w * 0.66f
            val step = 26f * dens
            if (sc > 0f) {
                val kMax = floor(sc / step).toInt()
                val kMin = max(1, floor((sc - (w - lampX)) / step).toInt())
                for (k in kMin..kMax) {
                    val x = lampX + 10f * dens + (sc - k * step)
                    if (x > w) continue
                    val fade = 1f - (x - lampX) / (w * 0.45f)
                    if (fade <= 0f) continue
                    val y = roadY + (h - roadY) * (0.38f + if (k % 2 == 0) -0.09f else 0.09f)
                    drawOval(Footprint.copy(alpha = 0.55f * fade), topLeft = Offset(x - 4.5f * dens, y - 2.4f * dens), size = Size(9f * dens, 4.8f * dens))
                }
            }
            val bob = if (walking.value) sin(t * 7f) * 2f * dens else 0f
            val lampY = roadY - h * 0.07f + bob
            if (pWith > 0.01f) {
                drawLine(
                    MoonColor.copy(alpha = 0.85f * pWith),
                    moonC + Offset(moonR * 0.7f, moonR * 0.7f),
                    Offset(lampX - 6f * dens, lampY - 10f * dens),
                    1.5f * dens,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dens, 6f * dens)),
                )
                drawTag(tagLayouts.getOrNull(1), Offset((moonC.x + lampX) / 2f + 12f * dens, (moonC.y + lampY) / 2f - 8f * dens), pWith)
            }
            drawTag(tagLayouts.getOrNull(0), Offset(moonC.x, moonC.y + moonR + 16f * dens), pSky)
            drawTag(tagLayouts.getOrNull(2), Offset(moonC.x, moonC.y - moonR - 14f * dens), pApart)
            val lamp = Offset(lampX, lampY)
            drawCircle(Brush.radialGradient(listOf(LampColor.copy(alpha = 0.45f), Color.Transparent), center = lamp, radius = 46f * dens), radius = 46f * dens, center = lamp)
            drawArc(LampColor, startAngle = 180f, sweepAngle = 180f, useCenter = false, topLeft = Offset(lampX - 4f * dens, lampY - 15f * dens), size = Size(8f * dens, 8f * dens), style = Stroke(1.6f * dens))
            drawRoundRect(LampColor, topLeft = Offset(lampX - 6f * dens, lampY - 8f * dens), size = Size(12f * dens, 15f * dens), cornerRadius = CornerRadius(3f * dens, 3f * dens))
            drawRect(RoadColor.copy(alpha = 0.35f), topLeft = Offset(lampX - 4f * dens, lampY - 5f * dens), size = Size(8f * dens, 9f * dens))
        }
        Box(Modifier.fillMaxWidth().background(pal.bg)) { SceneCaption(scene, shown, txt) }
    }
}

/** Stars as decoration, shooting stars, a guiding star for a boat at sea, then rain. */
@Composable
private fun StarsScene(scene: Scene, shown: Int, txt: (String) -> String) {
    val pal = LocalPalette.current
    val clock = rememberClock()
    fun isOn(kind: String): Boolean {
        val i = scene.items.indexOfFirst { it.kind == kind }
        return i in 0 until shown
    }
    val pDecor by animateFloatAsState(if (isOn("decor")) 1f else 0f, tween(900), label = "decor")
    val pMeteor by animateFloatAsState(if (isOn("meteor")) 1f else 0f, tween(500), label = "meteor")
    val pGuide by animateFloatAsState(if (isOn("guide")) 1f else 0f, tween(800), label = "guide")
    val pRain by animateFloatAsState(if (isOn("rain")) 1f else 0f, tween(1400), label = "rain")
    val dens = LocalDensity.current.density
    Column(Modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().aspectRatio(16f / 10f)) {
            val w = size.width
            val h = size.height
            val t = clock.value
            drawRect(Brush.verticalGradient(listOf(SkyTop, SkyBottom)))
            val dim = 1f - 0.65f * pRain
            for (s in STARS) {
                val tw = 0.5f + 0.5f * sin(t * 1.6f + s.p)
                val a = (0.18f + 0.82f * pDecor * (0.55f + 0.45f * tw)) * dim
                drawCircle(MoonColor.copy(alpha = a.coerceIn(0f, 1f)), radius = s.r * dens * (1f + 0.3f * pDecor), center = Offset(s.x * w, s.y * h))
            }
            val gs = Offset(w * 0.78f, h * 0.16f)
            val ga = (0.35f + 0.65f * pGuide) * dim
            drawCircle(Brush.radialGradient(listOf(MoonColor.copy(alpha = 0.5f * pGuide * dim), Color.Transparent), center = gs, radius = 22f * dens), radius = 22f * dens, center = gs)
            val arm = (6f + 5f * pGuide) * dens
            drawLine(MoonColor.copy(alpha = ga), gs - Offset(arm, 0f), gs + Offset(arm, 0f), 1.5f * dens, StrokeCap.Round)
            drawLine(MoonColor.copy(alpha = ga), gs - Offset(0f, arm), gs + Offset(0f, arm), 1.5f * dens, StrokeCap.Round)
            drawCircle(MoonColor.copy(alpha = ga), radius = 2.6f * dens, center = gs)
            if (pMeteor > 0.01f) {
                val cycle = 1.9f
                val ci = floor(t / cycle).toInt()
                val f = (t % cycle) / cycle
                if (f < 0.45f) {
                    val q = f / 0.45f
                    val sx = w * (0.35f + 0.5f * frac01(ci * 0.37f))
                    val sy = h * (0.04f + 0.18f * frac01(ci * 0.61f))
                    val dir = Offset(-0.82f, 0.57f)
                    val len = w * 0.42f
                    val head = Offset(sx + dir.x * len * q, sy + dir.y * len * q)
                    val tail = head - dir * (w * 0.12f)
                    drawLine(
                        Brush.linearGradient(listOf(Color.Transparent, MoonColor.copy(alpha = pMeteor * (1f - q * 0.6f) * dim)), start = tail, end = head),
                        tail,
                        head,
                        2f * dens,
                        StrokeCap.Round,
                    )
                }
            }
            val seaY = h * 0.8f
            drawRect(Brush.verticalGradient(listOf(SeaTop, SeaBottom), startY = seaY, endY = h), topLeft = Offset(0f, seaY), size = Size(w, h - seaY))
            for (r in 0 until 3) {
                val y = seaY + (h - seaY) * (0.3f + 0.25f * r)
                val off = (t * 12f * dens * (1 + r)) % (40f * dens)
                drawLine(MoonColor.copy(alpha = 0.18f), Offset(-40f * dens + off, y), Offset(w, y), 1f * dens, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f * dens, 30f * dens)))
            }
            val bx = w * 0.3f
            val by = seaY + (h - seaY) * 0.42f + sin(t * 1.7f) * 1.5f * dens
            val hull = Path().apply {
                moveTo(bx - 22f * dens, by - 4f * dens)
                lineTo(bx + 22f * dens, by - 4f * dens)
                lineTo(bx + 15f * dens, by + 5f * dens)
                lineTo(bx - 15f * dens, by + 5f * dens)
                close()
            }
            drawPath(hull, BoatColor)
            val mastTop = Offset(bx, by - 34f * dens)
            drawLine(BoatColor, Offset(bx, by - 4f * dens), mastTop, 2f * dens)
            val sail = Path().apply {
                moveTo(bx + 1.5f * dens, by - 32f * dens)
                lineTo(bx + 20f * dens, by - 7f * dens)
                lineTo(bx + 1.5f * dens, by - 7f * dens)
                close()
            }
            drawPath(sail, MoonColor.copy(alpha = 0.85f))
            if (pGuide > 0.01f) {
                val dashPhase = (t * 18f * dens) % (9f * dens)
                drawLine(MoonColor.copy(alpha = 0.8f * pGuide * dim), mastTop, gs, 1.3f * dens, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * dens, 6f * dens), dashPhase))
            }
            if (pRain > 0.01f) {
                val shift = (1f - pRain) * -w * 0.4f
                for ((cx, cy, cr) in CLOUDS) {
                    drawCircle(CloudColor.copy(alpha = 0.92f * pRain), radius = cr * w, center = Offset(cx * w + shift, cy * h))
                }
                for (k in 0 until 46) {
                    val x0 = frac01(k * 0.6180339f) * w
                    val speedF = 0.9f + 0.4f * frac01(k * 0.37f)
                    val f = frac01(t * speedF * 0.9f + k * 0.13f)
                    val y = h * 0.2f + f * (seaY - h * 0.2f)
                    drawLine(RainColor.copy(alpha = 0.55f * pRain), Offset(x0, y), Offset(x0 - 3f * dens, y + 10f * dens), 1.2f * dens, StrokeCap.Round)
                }
            }
        }
        Box(Modifier.fillMaxWidth().background(pal.bg)) { SceneCaption(scene, shown, txt) }
    }
}
