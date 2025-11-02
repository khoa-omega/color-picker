package io.github.komega.colorpicker.common

import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Suppress("NOTHING_TO_INLINE")
internal object Math {
    inline fun sanitize(degrees: Float): Float {
        val degrees = degrees % 360f
        return if (degrees < 0f) degrees + 360f else degrees
    }

    inline fun degrees(radians: Float): Float {
        return (radians * 57.29577951308232).toFloat()
    }

    inline fun radians(degrees: Float): Float {
        return (degrees * 0.017453292519943295).toFloat()
    }

    inline fun between(current: Float, start: Float, sweep: Float): Boolean {
        val delta = sanitize(current - start)
        return delta <= sweep || delta >= 360f + sweep
    }

    inline fun distance(a: Float, b: Float): Float {
        val distance = sanitize(a - b)
        return if (distance > 180f) 360f - distance else distance
    }

    fun fraction(current: Float, start: Float, sweep: Float): Float {
        return if (between(current, start, sweep)) {
            if (sweep > 0f) {
                sanitize(current - start) / sweep
            } else {
                sanitize(start - current) / -sweep
            }
        } else {
            if (distance(current, start) < distance(current, start + sweep)) {
                0f
            } else {
                1f
            }
        }
    }

    fun lerp(fraction: Float, start: Float, sweep: Float): Float {
        return sanitize(start + sweep * fraction)
    }

    fun polar(center: Offset, position: Offset): Float {
        val dx = position.x - center.x
        val dy = position.y - center.y
        val radians = atan2(dy, dx)
        return sanitize(degrees(radians))
    }

    fun cartesian(center: Offset, radius: Float, degrees: Float): Offset {
        val radians = radians(degrees)
        val x = center.x + radius * cos(radians)
        val y = center.y + radius * sin(radians)
        return Offset(x, y)
    }
}
