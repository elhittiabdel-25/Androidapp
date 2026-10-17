package app.usulcards.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private class Particle(
    var x: Float, var y: Float, var vx: Float, var vy: Float,
    var life: Float, var rot: Float, val size: Float, val color: Color,
)

/** Small celebration burst when a card is mastered or the daily goal is reached. */
class ConfettiState {
    private val particles = ArrayList<Particle>()
    var frame by mutableIntStateOf(0)
        private set
    var trigger by mutableIntStateOf(0)
        private set

    fun burst(at: Offset, colors: List<Color>, count: Int = 44) {
        repeat(count) { i ->
            val a = Random.nextFloat() * 6.2832f
            val s = 6f + Random.nextFloat() * 12f
            particles.add(
                Particle(at.x, at.y, cos(a) * s, sin(a) * s - 9f, 1f, Random.nextFloat() * 6f,
                    6f + Random.nextFloat() * 8f, colors[i % colors.size])
            )
        }
        trigger++
    }

    internal fun step(): Boolean {
        val it = particles.iterator()
        while (it.hasNext()) {
            val p = it.next()
            p.x += p.vx; p.y += p.vy; p.vy += 0.55f; p.life -= 0.016f; p.rot += 0.2f
            if (p.life <= 0f) it.remove()
        }
        frame++
        return particles.isNotEmpty()
    }

    internal fun forEach(block: (x: Float, y: Float, life: Float, rot: Float, size: Float, color: Color) -> Unit) {
        for (p in particles) block(p.x, p.y, p.life, p.rot, p.size, p.color)
    }
}

@Composable
fun ConfettiLayer(state: ConfettiState, modifier: Modifier = Modifier) {
    LaunchedEffect(state.trigger) {
        if (state.trigger == 0) return@LaunchedEffect
        while (true) {
            withFrameNanos { }
            if (!state.step()) break
        }
    }
    Canvas(modifier.fillMaxSize()) {
        @Suppress("UNUSED_VARIABLE") val f = state.frame   // read so each frame redraws
        state.forEach { x, y, life, rot, size, color ->
            rotate(degrees = rot * 57f, pivot = Offset(x, y)) {
                drawRect(color.copy(alpha = life.coerceIn(0f, 1f)), topLeft = Offset(x - size / 2, y - size / 4), size = Size(size, size / 2))
            }
        }
    }
}

/** Daily-goal ring: fills smoothly as cards are mastered today. */
@Composable
fun GoalRing(progress: Float, size: Dp, track: Color, fill: Color) {
    val p by animateFloatAsState(progress.coerceIn(0f, 1f), tween(800), label = "goal")
    Canvas(Modifier.size(size)) {
        val stroke = this.size.minDimension * 0.12f
        val inset = stroke / 2
        val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
        drawArc(track, 0f, 360f, false, topLeft = Offset(inset, inset), size = arcSize, style = Stroke(stroke))
        drawArc(fill, -90f, 360f * p, false, topLeft = Offset(inset, inset), size = arcSize, style = Stroke(stroke, cap = StrokeCap.Round))
    }
}
