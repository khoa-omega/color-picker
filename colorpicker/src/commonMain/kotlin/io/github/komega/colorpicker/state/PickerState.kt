package io.github.komega.colorpicker.state

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.gestures.Drag2DScope
import androidx.compose.foundation.gestures.Draggable2DState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import io.github.komega.colorpicker.common.Math

@Stable
fun Modifier.draggable(
    state: PickerState,
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
fun Modifier.tappable(state: PickerState): Modifier =
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

@Composable
fun rememberPickerState(value: Float, start: Float, sweep: Float): PickerState {
    return rememberSaveable(saver = PickerState.Saver) {
        PickerState(value, start, sweep)
    }
}

@Stable
class PickerState(
    fraction: Float,
    val start: Float,
    val sweep: Float
) : Draggable2DState, Drag2DScope {
    var fraction by mutableFloatStateOf(fraction)
        private set

    var dragging by mutableStateOf(false)
        private set

    val degrees: Float
        get() = Math.sanitize(start + sweep * fraction)

    private val mutex = MutatorMutex()
    private var offset = Offset.Zero
    private var down = Offset.Zero
    private var center = Offset.Zero
    private var radius = 0f

    override suspend fun drag(
        dragPriority: MutatePriority,
        block: suspend Drag2DScope.() -> Unit
    ) {
        dragging = true
        mutex.mutateWith(this, dragPriority, block)
        dragging = false
    }

    override fun dispatchRawDelta(delta: Offset) {
        offset += down + delta
        down = Offset.Zero
        val degrees = Math.polar(center, offset)
        fraction = Math.fraction(degrees, start, sweep)
    }

    override fun dragBy(pixels: Offset) {
        dispatchRawDelta(pixels)
    }

    fun onGestureEnd() {
        offset = Math.cartesian(center, radius, fraction)
    }

    fun onPress(position: Offset) {
        down = position - offset
    }

    fun update(center: Offset, radius: Float) {
        this.center = center
        this.radius = radius
    }

    companion object {
        val Saver: Saver<PickerState, *> = listSaver(
            save = {
                listOf(it.degrees, it.start, it.sweep)
            },
            restore = {
                PickerState(it[0], it[1], it[2])
            }
        )
    }
}
