package io.github.komega.colorpicker.component

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.compose.ui.util.fastRoundToInt
import io.github.komega.colorpicker.common.Math
import io.github.komega.colorpicker.state.PickerState
import io.github.komega.colorpicker.style.SaturationPickerDefaults

@Composable
fun SaturationPicker(
    state: PickerState,
    ringPickerState: PickerState,
    thickness: Dp,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (PickerState) -> Unit = { state ->
        SaturationPickerDefaults.Thumb(
            state = state
        )
    },
    track: @Composable (PickerState) -> Unit = { state ->
        SaturationPickerDefaults.Track(
            state = state,
            ringState = ringPickerState,
            thickness = thickness,
            enabled = true,
            interactionSource = interactionSource
        )
    }
) {
    Layout(
        content = {
            track(state)
            thumb(state)
        },
        modifier = modifier.focusable(enabled, interactionSource)
    ) { measurables, constraints ->
        val thickness = thickness.toPx()
        val thumbPlaceable = measurables[1].measure(constraints.copy(minWidth = 0, minHeight = 0))
        val trackPaddingX = (thumbPlaceable.width - thickness).coerceAtLeast(0f)
        val trackPaddingY = (thumbPlaceable.height - thickness).coerceAtLeast(0f)
        val trackPlaceable = measurables[0].measure(
            constraints.offset(
                horizontal = -trackPaddingX.fastRoundToInt(),
                vertical = -trackPaddingY.fastRoundToInt()
            )
        )
        val trackX = (trackPaddingX * 0.5f).fastRoundToInt()
        val trackY = (trackPaddingY * 0.5f).fastRoundToInt()
        val trackSize = Size(trackPlaceable.width.toFloat(), trackPlaceable.height.toFloat())
        val trackRadius = (trackSize.minDimension - thickness) * 0.5f
        state.update(trackSize.center, trackRadius)
        val totalWidth = trackPlaceable.width + (thumbPlaceable.width - thickness)
            .fastRoundToInt().fastCoerceAtLeast(0)
        val totalHeight = trackPlaceable.height + (thumbPlaceable.height - thickness)
            .fastRoundToInt().fastCoerceAtLeast(0)
        val thumbOffset = Offset(
            thumbPlaceable.width * 0.5f - trackX,
            thumbPlaceable.height * 0.5f - trackY
        )
        layout(totalWidth, totalHeight) {
            trackPlaceable.placeRelative(trackX, trackY)
            val thumbCenter = Math.cartesian(trackSize.center, trackRadius, state.degrees)
            val thumbPosition = thumbCenter - thumbOffset
            thumbPlaceable.placeRelative(
                thumbPosition.x.fastRoundToInt(),
                thumbPosition.y.fastRoundToInt()
            )
        }
    }
}
