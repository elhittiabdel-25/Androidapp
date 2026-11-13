package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.max
import kotlin.math.sin

/*
 * Kit 95 (2026-11-13): five more symbols for the Sira boards (wave 95): a banner (الراية), a shield (النصرة), a tent,
 * the trench and a closed door. Anything else falls through to qIcon (kit 86).
 */
internal fun DrawScope.qIcon95(name: String, c: Offset, s: Float, color: Color, alpha: Float, t: Float = 0f) {
    val sw = max(1.4f * density, s * 0.08f)
    when (name) {
        "flag" -> {
            drawLine(Qn.Soil, c + Offset(-s * 0.3f, s * 0.46f), c + Offset(-s * 0.3f, -s * 0.46f), sw, StrokeCap.Round, alpha = alpha)
            val w = sin(t * 6.28f) * s * 0.04f
            drawPath(Path().apply {
                moveTo(c.x - s * 0.3f, c.y - s * 0.42f)
                quadraticBezierTo(c.x, c.y - s * 0.5f + w, c.x + s * 0.38f, c.y - s * 0.36f)
                lineTo(c.x + s * 0.38f, c.y - s * 0.02f)
                quadraticBezierTo(c.x, c.y - s * 0.14f - w, c.x - s * 0.3f, c.y - s * 0.06f)
                close()
            }, color, alpha = alpha)
        }
        "shield" -> {
            val p = Path().apply {
                moveTo(c.x, c.y - s * 0.44f)
                lineTo(c.x + s * 0.36f, c.y - s * 0.3f)
                quadraticBezierTo(c.x + s * 0.34f, c.y + s * 0.22f, c.x, c.y + s * 0.46f)
                quadraticBezierTo(c.x - s * 0.34f, c.y + s * 0.22f, c.x - s * 0.36f, c.y - s * 0.3f)
                close()
            }
            drawPath(p, color, alpha = alpha * 0.85f)
            drawPath(p, Qn.GoldLight, alpha = alpha, style = Stroke(sw * 0.6f))
        }
        "tent" -> {
            drawPath(Path().apply {
                moveTo(c.x - s * 0.46f, c.y + s * 0.36f); lineTo(c.x, c.y - s * 0.4f); lineTo(c.x + s * 0.46f, c.y + s * 0.36f); close()
            }, color, alpha = alpha)
            drawPath(Path().apply {
                moveTo(c.x - s * 0.12f, c.y + s * 0.36f); lineTo(c.x, c.y + s * 0.02f); lineTo(c.x + s * 0.12f, c.y + s * 0.36f); close()
            }, Qn.Back, alpha = alpha)
            drawLine(Qn.Soil, c + Offset(-s * 0.5f, s * 0.38f), c + Offset(s * 0.5f, s * 0.38f), sw * 0.7f, StrokeCap.Round, alpha = alpha)
        }
        "trench" -> {
            drawLine(Qn.Soil, c + Offset(-s * 0.48f, -s * 0.05f), c + Offset(-s * 0.18f, -s * 0.05f), sw, StrokeCap.Round, alpha = alpha)
            drawLine(Qn.Soil, c + Offset(s * 0.18f, -s * 0.05f), c + Offset(s * 0.48f, -s * 0.05f), sw, StrokeCap.Round, alpha = alpha)
            drawPath(Path().apply {
                moveTo(c.x - s * 0.18f, c.y - s * 0.05f); lineTo(c.x - s * 0.12f, c.y + s * 0.38f)
                lineTo(c.x + s * 0.12f, c.y + s * 0.38f); lineTo(c.x + s * 0.18f, c.y - s * 0.05f)
            }, color, alpha = alpha, style = Stroke(sw, cap = StrokeCap.Round))
            for (k in listOf(-1f, 1f)) drawRect(color, Offset(c.x + k * s * 0.33f - s * 0.08f, c.y - s * 0.3f), Size(s * 0.16f, s * 0.22f), alpha = alpha * 0.6f)
        }
        "door" -> {
            drawRect(color, Offset(c.x - s * 0.26f, c.y - s * 0.42f), Size(s * 0.52f, s * 0.84f), alpha = alpha)
            drawRect(Qn.Back, Offset(c.x - s * 0.18f, c.y - s * 0.34f), Size(s * 0.36f, s * 0.3f), alpha = alpha * 0.35f)
            drawCircle(Qn.GoldLight, s * 0.05f, c + Offset(s * 0.14f, s * 0.08f), alpha = alpha)
        }
        else -> qIcon(name, c, s, color, alpha, t)
    }
}
