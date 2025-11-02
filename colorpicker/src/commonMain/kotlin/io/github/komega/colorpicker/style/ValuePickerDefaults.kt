package io.github.komega.colorpicker.style

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.komega.colorpicker.shape.ArcShape
import io.github.komega.colorpicker.state.PickerState
import io.github.komega.colorpicker.state.draggable
import io.github.komega.colorpicker.state.tappable
import kotlin.math.PI
import kotlin.math.abs

object ValuePickerDefaults {
    @Composable
    fun Thumb(
        state: PickerState,
        modifier: Modifier = Modifier
    ) {
        Spacer(
            modifier = modifier
                .size(20.dp)
                .shadow(1.dp, CircleShape)
                .background(Color.White)
        )
    }

    @Composable
    fun Track(
        state: PickerState,
        ringState: PickerState,
        thickness: Dp,
        enabled: Boolean,
        interactionSource: MutableInteractionSource,
        modifier: Modifier = Modifier
    ) {
        val shape = ArcShape(
            thickness,
            state.start,
            state.sweep
        )
        Spacer(
            modifier = modifier
                .size(240.dp)
                .shadow(1.dp, shape)
                .drawWithCache {
                    val thickness = shape.thickness.toPx()
                    val diameter = size.minDimension - thickness
                    val extend = thickness * 180f / (diameter * PI).toFloat()
                    val start = Color.hsv(0f, 0f, 0f)
                    val end = Color.hsv(0f, 0f, 1f)
                    val brush = Brush.sweepGradient(
                        0f to start,
                        extend / 360f to start,
                        abs(shape.sweep + extend) / 360f to end,
                        (abs(shape.sweep) + 2 * extend) / 360f to end
                    ) as ShaderBrush
                    brush.transform = Matrix().apply {
                        resetToPivotedTransform(
                            pivotX = size.width * 0.5f,
                            pivotY = size.height * 0.5f,
                            rotationZ = shape.start - extend
                        )
                    }
                    val outline = shape.createOutline(size, layoutDirection, this)
                    onDrawBehind {
                        drawOutline(outline, brush)
                    }
                }
                .draggable(state, enabled, interactionSource)
                .tappable(state)
        )
    }
}
