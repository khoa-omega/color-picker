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
import io.github.komega.colorpicker.modifier.dragRing
import io.github.komega.colorpicker.modifier.tapRing
import io.github.komega.colorpicker.shape.RingShape
import io.github.komega.colorpicker.state.RingPickerState

object RingPickerDefaults {
    @Composable
    fun Thumb(
        state: RingPickerState,
        modifier: Modifier = Modifier
    ) {
        Spacer(
            modifier = modifier
                .size(32.dp)
                .shadow(1.dp, CircleShape)
                .border(4.dp, Color.White, CircleShape)
                .drawWithContent {
                    drawCircle(Color.hsv(state.value, 1f, 1f))
                }
        )
    }

    @Composable
    fun Track(
        state: RingPickerState,
        thickness: Dp,
        enabled: Boolean,
        interactionSource: MutableInteractionSource,
        modifier: Modifier = Modifier
    ) {
        val shape = remember (thickness) { RingShape(thickness) }
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
                .dragRing(state, enabled, interactionSource)
                .tapRing(state)
        )
    }
}
