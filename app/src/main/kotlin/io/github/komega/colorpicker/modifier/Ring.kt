package io.github.komega.colorpicker.modifier

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import io.github.komega.colorpicker.state.RingPickerState

@Stable
fun Modifier.dragRing(
    state: RingPickerState,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null
): Modifier = draggable2D(
    state = state,
    enabled = enabled,
    interactionSource = interactionSource,
    startDragImmediately = state.dragging,
    onDragStopped = { state.onGestureEnd() }
)

@Stable
fun Modifier.tapRing(state: RingPickerState): Modifier =
    pointerInput(state) {
        detectTapGestures(
            onPress = { position ->
                state.onPress(position)
            },
            onTap = {
                state.dispatchRawDelta(Offset.Zero)
                state.onGestureEnd()
            }
        )
    }

