package io.github.komega.colorpicker.state

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.gestures.Drag2DScope
import androidx.compose.foundation.gestures.Draggable2DState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import io.github.komega.colorpicker.common.degrees
import io.github.komega.colorpicker.common.polar

@Composable
fun rememberRingPickerState(value: Float): RingPickerState {
    return rememberSaveable(saver = RingPickerState.Saver) {
        RingPickerState(value)
    }
}

@Stable
class RingPickerState(value: Float) : Draggable2DState, Drag2DScope {
    var value by mutableFloatStateOf(value)
        private set

    var dragging by mutableStateOf(false)
        private set

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
        value = center.degrees(offset)
    }

    override fun dragBy(pixels: Offset) {
        dispatchRawDelta(pixels)
    }

    internal fun onGestureEnd() {
        offset = center.polar(radius, value)
    }

    internal fun onPress(position: Offset) {
        down = position - offset
    }

    internal fun update(center: Offset, radius: Float) {
        this.center = center
        this.radius = radius
    }

    companion object {
        val Saver = Saver<RingPickerState, Float>(
            save = { state -> state.value },
            restore = { value -> RingPickerState(value) }
        )
    }
}
