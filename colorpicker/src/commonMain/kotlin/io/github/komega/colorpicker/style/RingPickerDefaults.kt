package io.github.komega.colorpicker.style

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.komega.colorpicker.shape.RingShape
import io.github.komega.colorpicker.state.PickerState
import io.github.komega.colorpicker.state.draggable
import io.github.komega.colorpicker.state.tappable

object RingPickerDefaults {
    @Composable
    fun Thumb(
        state: PickerState,
        modifier: Modifier = Modifier
    ) {
        Spacer(
            modifier = modifier
                .size(32.dp)
                .shadow(1.dp, CircleShape)
                .border(4.dp, Color.White, CircleShape)
                .drawWithContent {
                    drawCircle(Color.hsv(state.degrees, 1f, 1f))
                }
        )
    }

    @Composable
    fun Track(
        state: PickerState,
        thickness: Dp,
        enabled: Boolean,
        interactionSource: MutableInteractionSource,
        modifier: Modifier = Modifier
    ) {
        val shape = remember (thickness) {
            RingShape(
                thickness
            )
        }
        Spacer(
            modifier = modifier
                .size(160.dp)
                .shadow(1.dp, shape)
                .background(
                    brush = remember {
                        Brush.sweepGradient(
                            listOf(
                                Color.Red, Color.Yellow, Color.Green,
                                Color.Cyan, Color.Blue, Color.Magenta, Color.Red
                            )
                        )
                    },
                    shape = shape
                )
                .draggable(state, enabled, interactionSource)
                .tappable(state)
        )
    }
}
