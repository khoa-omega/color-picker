@file:Suppress("NOTHING_TO_INLINE")

package io.github.komega.colorpicker.common

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.util.fastCoerceIn
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

internal inline fun Float.sanitize(): Float {
    val degrees = this % 360f
    return if (degrees < 0f) degrees + 360f else degrees
}

internal inline fun Float.degrees(): Float {
    return (this * 180.0 / PI).toFloat()
}

internal inline fun Float.radians(): Float {
    return (this * PI / 180.0).toFloat()
}

internal fun Float.between(start: Float, end: Float): Boolean {
    return if (start > end) {
        this >= start || this <= end
    } else {
        this in start..end
    }
}

fun distance(a: Float, b: Float): Float {
    val distance = abs(a - b)
    return if (distance > 180f) 360f - distance else distance
}

internal fun Float.normalize(start: Float, sweep: Float): Float {
    var best = 0f
    var min = Float.MAX_VALUE
    for (k in -1..1) {
        val s = this + 360f * k - start
        val d = when {
            s < 0f -> -s
            s > sweep -> s - sweep
            else -> 0f
        }
        if (min > d) {
            min = d
            best = s
        }
    }
    return (best / sweep).fastCoerceIn(0f, 1f)
}

internal inline fun Float.denormalize(start: Float, sweep: Float): Float {
    return (start + fastCoerceIn(0f, 1f) * sweep).sanitize()
}

internal fun Offset.polar(radius: Float, degrees: Float): Offset {
    val radians = degrees.radians()
    val x = x + radius * cos(radians)
    val y = y + radius * sin(radians)
    return Offset(x, y)
}

internal fun Offset.degrees(offset: Offset): Float {
    val dx = offset.x - x
    val dy = offset.y - y
    val radians = atan2(dy, dx)
    return radians.degrees().sanitize()
}
