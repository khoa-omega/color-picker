package io.github.komega.colorpicker.component

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.compose.ui.util.fastRoundToInt
import io.github.komega.colorpicker.common.polar
import io.github.komega.colorpicker.state.RingPickerState
import io.github.komega.colorpicker.state.rememberRingPickerState
import io.github.komega.colorpicker.style.RingPickerDefaults

@Composable
fun RingPicker(
    state: RingPickerState,
    thickness: Dp,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (RingPickerState) -> Unit = { state ->
        RingPickerDefaults.Thumb(state = state)
    },
    track: @Composable (RingPickerState) -> Unit = { state ->
        RingPickerDefaults.Track(
            state = state,
            thickness = thickness,
            enabled = enabled,
            interactionSource = interactionSource,
            modifier = Modifier
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
            val thumbPosition = trackSize.center.polar(trackRadius, state.value) - thumbOffset
            thumbPlaceable.placeRelative(
                thumbPosition.x.fastRoundToInt(),
                thumbPosition.y.fastRoundToInt()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RingPickerPreview() {
    val state = rememberRingPickerState(120f)
    RingPicker(
        state = state,
        thickness = 32.dp
    )
}
