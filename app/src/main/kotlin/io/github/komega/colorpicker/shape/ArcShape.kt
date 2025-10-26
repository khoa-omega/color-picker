package io.github.komega.colorpicker.shape

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import io.github.komega.colorpicker.common.polar
import io.github.komega.colorpicker.common.sanitize
import kotlin.math.sign

@Immutable
class ArcShape(
    val thickness: Dp,
    val start: Float,
    val sweep: Float
) : Shape {
    val end = (start + sweep).sanitize()

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = with(density) {
        val thickness = thickness.toPx()
        val halfThickness = thickness * 0.5f
        val center = size.center
        val outerRadius = size.minDimension * 0.5f
        val radius = outerRadius - halfThickness
        val outerRect = Rect(center, outerRadius)
        val innerRect = outerRect.inflate(-thickness)
        val startCapRect = Rect(center.polar(radius, start), halfThickness)
        val endCapRect = Rect(center.polar(radius, end), halfThickness)
        val sign = sweep.sign
        val path = Path()
        path.arcTo(outerRect, start, sweep, false)
        path.arcTo(endCapRect, end, 180f * sign, false)
        path.arcTo(innerRect, end, -sweep, false)
        path.arcTo(startCapRect, start + 180f, 180f * sign, false)
        Outline.Generic(path)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArcShape) return false
        if (thickness != other.thickness) return false
        if (start != other.start) return false
        if (sweep != other.sweep) return false
        return true
    }

    override fun hashCode(): Int {
        var result = thickness.hashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + sweep.hashCode()
        return result
    }
}
