@file:Suppress("unused", "UNUSED_PARAMETER")
package androidx.compose.ui.graphics.drawscope

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*

sealed class DrawStyle
object Fill : DrawStyle()
class Stroke(val width: Float = 0f, val miter: Float = 4f, val cap: StrokeCap = StrokeCap.Butt, val join: StrokeJoin = StrokeJoin.Miter, val pathEffect: PathEffect? = null) : DrawStyle()

interface DrawTransform {
    fun scale(scaleX: Float, scaleY: Float, pivot: Offset)
    fun rotate(degrees: Float, pivot: Offset)
    fun translate(left: Float = 0f, top: Float = 0f)
    fun clipRect(left: Float = 0f, top: Float = 0f, right: Float = 0f, bottom: Float = 0f)
}
interface DrawScope {
    val density: Float
    val size: Size
    val center: Offset
    fun drawLine(color: Color, start: Offset, end: Offset, strokeWidth: Float = 0f, cap: StrokeCap = StrokeCap.Butt, pathEffect: PathEffect? = null, alpha: Float = 1f)
    fun drawLine(brush: Brush, start: Offset, end: Offset, strokeWidth: Float = 0f, cap: StrokeCap = StrokeCap.Butt, pathEffect: PathEffect? = null, alpha: Float = 1f)
    fun drawCircle(color: Color, radius: Float = 0f, center: Offset = Offset.Zero, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawCircle(brush: Brush, radius: Float = 0f, center: Offset = Offset.Zero, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawRect(color: Color, topLeft: Offset = Offset.Zero, size: Size = Size.Zero, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawRect(brush: Brush, topLeft: Offset = Offset.Zero, size: Size = Size.Zero, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawRoundRect(color: Color, topLeft: Offset = Offset.Zero, size: Size = Size.Zero, cornerRadius: CornerRadius = CornerRadius.Zero, style: DrawStyle = Fill, alpha: Float = 1f, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawRoundRect(brush: Brush, topLeft: Offset = Offset.Zero, size: Size = Size.Zero, cornerRadius: CornerRadius = CornerRadius.Zero, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawOval(color: Color, topLeft: Offset = Offset.Zero, size: Size = Size.Zero, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawOval(brush: Brush, topLeft: Offset = Offset.Zero, size: Size = Size.Zero, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawPath(path: Path, color: Color, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawPath(path: Path, brush: Brush, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawArc(color: Color, startAngle: Float, sweepAngle: Float, useCenter: Boolean, topLeft: Offset = Offset.Zero, size: Size = Size.Zero, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
    fun drawArc(brush: Brush, startAngle: Float, sweepAngle: Float, useCenter: Boolean, topLeft: Offset = Offset.Zero, size: Size = Size.Zero, alpha: Float = 1f, style: DrawStyle = Fill, blendMode: BlendMode = BlendMode.SrcOver)
}
inline fun DrawScope.rotate(degrees: Float, pivot: Offset = center, block: DrawScope.() -> Unit) {}
inline fun DrawScope.scale(scale: Float, pivot: Offset = center, block: DrawScope.() -> Unit) {}
inline fun DrawScope.scale(scaleX: Float, scaleY: Float, pivot: Offset = center, block: DrawScope.() -> Unit) {}
inline fun DrawScope.translate(left: Float = 0f, top: Float = 0f, block: DrawScope.() -> Unit) {}
inline fun DrawScope.withTransform(transformBlock: DrawTransform.() -> Unit, drawBlock: DrawScope.() -> Unit) {}
inline fun DrawScope.clipRect(left: Float = 0f, top: Float = 0f, right: Float = size.width, bottom: Float = size.height, block: DrawScope.() -> Unit) {}
inline fun DrawScope.clipPath(path: Path, block: DrawScope.() -> Unit) {}
