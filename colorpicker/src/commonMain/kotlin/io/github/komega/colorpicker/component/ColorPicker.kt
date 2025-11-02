package io.github.komega.colorpicker.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.komega.colorpicker.state.rememberPickerState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ColorPicker(modifier: Modifier = Modifier) {
    val hue = rememberPickerState(0f, 0f, 360f)
    val saturation =
        rememberPickerState(
            1f,
            300f,
            120f
        )
    val value =
        rememberPickerState(
            1f,
            120f,
            120f
        )
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        RingPicker(
            state = hue,
            thickness = 32.dp
        )
        SaturationPicker(
            state = saturation,
            ringPickerState = hue,
            thickness = 24.dp
        )
        ValuePicker(
            state = value,
            ringPickerState = hue,
            thickness = 24.dp
        )
        Spacer(modifier = Modifier.size(64.dp).drawBehind {
            val color = Color.hsv(hue.degrees, saturation.fraction, value.fraction)
            drawCircle(color)
        })
    }
}
