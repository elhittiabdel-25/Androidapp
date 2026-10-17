package app.usulcards.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sqrt

/*
 * Re-Iman brand: a gold ring that turns back on itself (renewal) around an eight-pointed star,
 * and the wordmark "Re-Iman". Same drawing as the launcher icon (res/drawable/ic_launcher_foreground.xml).
 */

const val APP_NAME = "Re-Iman"
const val APP_TAGLINE = "جَدِّدْ إِيمَانَكَ"

private var markHasTurned = false   // the ring turns into place once per launch, not on every screen

/** The ring-and-star mark; the ring turns once into place the first time it appears after launch. */
@Composable
fun BrandMark(size: Dp, modifier: Modifier = Modifier, ring: Color? = null, star: Color? = null) {
    val pal = LocalPalette.current
    val ringColor = ring ?: pal.gold
    val starColor = star ?: pal.accent
    val spin = remember { Animatable(if (markHasTurned) 0f else -300f) }
    LaunchedEffect(Unit) {
        if (spin.value != 0f) {
            markHasTurned = true
            spin.animateTo(0f, tween(1500, easing = FastOutSlowInEasing))
        }
    }
    Canvas(modifier.size(size)) {
        val u = this.size.minDimension / 48f          // drawn on a 48 x 48 grid
        rotate(spin.value, center) { drawRenewalRing(u, ringColor) }
        drawStar(u, starColor, ringColor)
    }
}

private fun DrawScope.drawRenewalRing(u: Float, color: Color) {
    val r = 19f * u
    val c = center
    // 300 degrees clockwise from the upper right to the top; the gap is where the arrow points
    drawArc(
        color, startAngle = -30f, sweepAngle = 300f, useCenter = false,
        topLeft = Offset(c.x - r, c.y - r), size = Size(2 * r, 2 * r),
        style = Stroke(3.6f * u, cap = StrokeCap.Round),
    )
    val head = Path().apply {
        moveTo(c.x + 6f * u, c.y - r)
        lineTo(c.x - 1f * u, c.y - r - 5.2f * u)
        lineTo(c.x - 1f * u, c.y - r + 5.2f * u)
        close()
    }
    drawPath(head, color)
}

private fun DrawScope.drawStar(u: Float, color: Color, dot: Color) {
    val c = center
    val h = 10.5f * u                                   // centre to point
    val a = h / sqrt(2f)
    drawRect(color, topLeft = Offset(c.x - a, c.y - a), size = Size(2 * a, 2 * a))
    drawPath(Path().apply {
        moveTo(c.x, c.y - h); lineTo(c.x + h, c.y); lineTo(c.x, c.y + h); lineTo(c.x - h, c.y); close()
    }, color)
    drawCircle(dot, radius = 2.6f * u, center = c)
}

/** "Re-Iman": "Re" in gold (renewal), "-Iman" in ink. Latin, so it reads left to right inside the Arabic UI. */
@Composable
fun Wordmark(fontSize: TextUnit = 24.sp) {
    val pal = LocalPalette.current
    val text = buildAnnotatedString {
        withStyle(SpanStyle(color = pal.gold)) { append("Re") }
        withStyle(SpanStyle(color = pal.ink)) { append("-Iman") }
    }
    Text(text, fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = fontSize)
}

/** Wordmark with the tagline under it. */
@Composable
fun BrandTitle() {
    val pal = LocalPalette.current
    Column {
        Wordmark()
        Text(APP_TAGLINE, color = pal.muted, fontFamily = Fonts.naskh, fontSize = 13.sp)
    }
}

/** Colour of a category: blue for tawhid, amber for what opposes it, green for iman, gold for the method. */
fun categoryTone(p: Palette, id: String, index: Int): Pair<Color, Color> = when (id) {
    "tawhid" -> p.accent to p.accentSoft
    "shirk" -> p.warn to p.warnSoft
    "iman" -> p.ok to p.okSoft
    "manhaj" -> p.gold to p.goldSoft
    else -> listOf(p.accent to p.accentSoft, p.gold to p.goldSoft, p.ok to p.okSoft, p.warn to p.warnSoft)[index % 4]
}

/** Small line emblem of a category: one (a ring around a single point), shield, pillars, path. */
@Composable
fun CategoryGlyph(kind: String, color: Color, size: Dp = 26.dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val sw = w * 0.085f
        val line = Stroke(sw, cap = StrokeCap.Round, join = StrokeJoin.Round)
        when (kind) {
            "one" -> {
                drawCircle(color, radius = w * 0.4f, center = center, style = line)
                drawCircle(color, radius = w * 0.13f, center = center)
            }
            "shield" -> {
                val p = Path().apply {
                    moveTo(w * 0.5f, h * 0.08f)
                    lineTo(w * 0.86f, h * 0.22f)
                    lineTo(w * 0.82f, h * 0.56f)
                    cubicTo(w * 0.76f, h * 0.76f, w * 0.62f, h * 0.88f, w * 0.5f, h * 0.94f)
                    cubicTo(w * 0.38f, h * 0.88f, w * 0.24f, h * 0.76f, w * 0.18f, h * 0.56f)
                    lineTo(w * 0.14f, h * 0.22f)
                    close()
                }
                drawPath(p, color, style = line)
                drawCircle(color, radius = w * 0.09f, center = Offset(w * 0.5f, h * 0.48f))
            }
            "pillars" -> {
                val roof = Path().apply {
                    moveTo(w * 0.1f, h * 0.32f); lineTo(w * 0.5f, h * 0.1f); lineTo(w * 0.9f, h * 0.32f); close()
                }
                drawPath(roof, color, style = line)
                for (x in listOf(0.24f, 0.5f, 0.76f)) {
                    drawLine(color, Offset(w * x, h * 0.42f), Offset(w * x, h * 0.78f), sw, StrokeCap.Round)
                }
                drawLine(color, Offset(w * 0.1f, h * 0.9f), Offset(w * 0.9f, h * 0.9f), sw, StrokeCap.Round)
            }
            else -> {   // "path": a road that winds up to a point
                val road = Path().apply {
                    moveTo(w * 0.18f, h * 0.92f)
                    cubicTo(w * 0.18f, h * 0.55f, w * 0.82f, h * 0.65f, w * 0.78f, h * 0.36f)
                    cubicTo(w * 0.75f, h * 0.2f, w * 0.55f, h * 0.18f, w * 0.5f, h * 0.18f)
                }
                drawPath(road, color, style = line)
                drawCircle(color, radius = w * 0.1f, center = Offset(w * 0.4f, h * 0.18f))
            }
        }
    }
}
