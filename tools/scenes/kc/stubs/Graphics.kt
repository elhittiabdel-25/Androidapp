@file:Suppress("unused", "UNUSED_PARAMETER")
package androidx.compose.ui.graphics

import androidx.compose.ui.geometry.*

class Color(val value: ULong) {
    val red: Float get() = TODO()
    val green: Float get() = TODO()
    val blue: Float get() = TODO()
    val alpha: Float get() = TODO()
    fun copy(alpha: Float = this.alpha, red: Float = this.red, green: Float = this.green, blue: Float = this.blue): Color = TODO()
    companion object {
        val White: Color = TODO(); val Black: Color = TODO(); val Transparent: Color = TODO(); val Unspecified: Color = TODO()
        val Red: Color = TODO(); val Gray: Color = TODO(); val DarkGray: Color = TODO(); val LightGray: Color = TODO()
        val Yellow: Color = TODO(); val Green: Color = TODO(); val Blue: Color = TODO()
    }
}
fun Color(color: Long): Color = TODO()
fun Color(color: Int): Color = TODO()
fun Color(red: Float, green: Float, blue: Float, alpha: Float = 1f): Color = TODO()
fun Color(red: Int, green: Int, blue: Int, alpha: Int = 0xFF): Color = TODO()
fun lerp(start: Color, stop: Color, fraction: Float): Color = TODO()

class Path {
    fun moveTo(x: Float, y: Float) {}
    fun lineTo(x: Float, y: Float) {}
    fun relativeMoveTo(dx: Float, dy: Float) {}
    fun relativeLineTo(dx: Float, dy: Float) {}
    fun quadraticTo(x1: Float, y1: Float, x2: Float, y2: Float) {}
    fun quadraticBezierTo(x1: Float, y1: Float, x2: Float, y2: Float) {}
    fun cubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) {}
    fun arcTo(rect: Rect, startAngleDegrees: Float, sweepAngleDegrees: Float, forceMoveTo: Boolean) {}
    fun addOval(oval: Rect) {}
    fun addRect(rect: Rect) {}
    fun addArc(oval: Rect, startAngleDegrees: Float, sweepAngleDegrees: Float) {}
    fun close() {}
    fun reset() {}
}
abstract class Brush {
    companion object {
        fun radialGradient(colors: List<Color>, center: Offset = Offset.Unspecified, radius: Float = Float.POSITIVE_INFINITY, tileMode: TileMode = TileMode.Clamp): Brush = TODO()
        fun radialGradient(vararg colorStops: Pair<Float, Color>, center: Offset = Offset.Unspecified, radius: Float = Float.POSITIVE_INFINITY, tileMode: TileMode = TileMode.Clamp): Brush = TODO()
        fun linearGradient(colors: List<Color>, start: Offset = Offset.Zero, end: Offset = Offset.Unspecified, tileMode: TileMode = TileMode.Clamp): Brush = TODO()
        fun linearGradient(vararg colorStops: Pair<Float, Color>, start: Offset = Offset.Zero, end: Offset = Offset.Unspecified, tileMode: TileMode = TileMode.Clamp): Brush = TODO()
        fun verticalGradient(colors: List<Color>, startY: Float = 0f, endY: Float = Float.POSITIVE_INFINITY, tileMode: TileMode = TileMode.Clamp): Brush = TODO()
        fun verticalGradient(vararg colorStops: Pair<Float, Color>, startY: Float = 0f, endY: Float = Float.POSITIVE_INFINITY, tileMode: TileMode = TileMode.Clamp): Brush = TODO()
        fun horizontalGradient(colors: List<Color>, startX: Float = 0f, endX: Float = Float.POSITIVE_INFINITY, tileMode: TileMode = TileMode.Clamp): Brush = TODO()
        fun sweepGradient(colors: List<Color>, center: Offset = Offset.Unspecified): Brush = TODO()
    }
}
class TileMode { companion object { val Clamp = TileMode(); val Repeated = TileMode(); val Mirror = TileMode() } }
class StrokeCap { companion object { val Butt = StrokeCap(); val Round = StrokeCap(); val Square = StrokeCap() } }
class StrokeJoin { companion object { val Miter = StrokeJoin(); val Round = StrokeJoin(); val Bevel = StrokeJoin() } }
class PathEffect { companion object { fun dashPathEffect(intervals: FloatArray, phase: Float = 0f): PathEffect = TODO() } }
class BlendMode { companion object { val SrcOver = BlendMode(); val Plus = BlendMode(); val Screen = BlendMode() } }
