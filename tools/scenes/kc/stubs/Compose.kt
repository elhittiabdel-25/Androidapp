@file:Suppress("unused", "UNUSED_PARAMETER")
package androidx.compose.ui.geometry

class Offset(val x: Float, val y: Float) {
    operator fun plus(o: Offset): Offset = TODO()
    operator fun minus(o: Offset): Offset = TODO()
    operator fun times(f: Float): Offset = TODO()
    operator fun div(f: Float): Offset = TODO()
    operator fun unaryMinus(): Offset = TODO()
    fun getDistance(): Float = TODO()
    fun getDistanceSquared(): Float = TODO()
    fun copy(x: Float = this.x, y: Float = this.y): Offset = TODO()
    operator fun component1(): Float = x
    operator fun component2(): Float = y
    companion object { val Zero: Offset = TODO(); val Unspecified: Offset = TODO() }
}
fun lerp(start: Offset, stop: Offset, fraction: Float): Offset = TODO()
class Size(val width: Float, val height: Float) {
    val minDimension: Float get() = TODO()
    val maxDimension: Float get() = TODO()
    operator fun times(f: Float): Size = TODO()
    operator fun div(f: Float): Size = TODO()
    val center: Offset get() = TODO()
    companion object { val Zero: Size = TODO() }
}
class CornerRadius(val x: Float, val y: Float = x) { companion object { val Zero: CornerRadius = TODO() } }
class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float) {
    constructor(offset: Offset, size: Size) : this(0f, 0f, 0f, 0f)
    constructor(center: Offset, radius: Float) : this(0f, 0f, 0f, 0f)
    val center: Offset get() = TODO()
    val width: Float get() = TODO()
    val height: Float get() = TODO()
}
