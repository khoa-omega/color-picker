package io.github.komega.colorpicker.shape

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

@Immutable
class RingShape(val thickness: Dp) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = with(density) {
        val thickness = thickness.toPx()
        val radius = size.minDimension * 0.5f
        val path = Path()
        path.fillType = PathFillType.EvenOdd
        val outerRect = Rect(size.center, radius)
        path.addOval(outerRect)
        val innerRect = outerRect.inflate(-thickness)
        path.addOval(innerRect)
        Outline.Generic(path)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RingShape) return false
        return thickness == other.thickness
    }

    override fun hashCode(): Int = thickness.hashCode()
}
