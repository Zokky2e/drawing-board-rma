package com.example.drawing_board_rma.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawingMenu(
    collectList: MutableList<DrawItem>,
    onClearClicked: () -> Unit,
    onDrawingEnabled: (Boolean) -> Unit,
    onErasingEnabled: (Boolean) -> Unit,
    brushStrokeWidth: Float,
    eraserStrokeWidth: Float,
    onBrushStrokeWidthChanged: (Float) -> Unit,
    onEraserStrokeWidthChanged: (Float) -> Unit,
) {
    var isDrawModeSelected by remember { mutableStateOf(true) }
    var isEraseModeSelected by remember { mutableStateOf(false) }

    onDrawingEnabled(isDrawModeSelected)
    onErasingEnabled(isEraseModeSelected)

    val drawButtonText = if (isDrawModeSelected) "Stop Draw" else "Draw"
    val eraseButtonText = if (isEraseModeSelected) "Stop Erase" else "Erase"

    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)) {
        if(!isEraseModeSelected) {
            Row(
                modifier =  if (isDrawModeSelected)
                            Modifier.fillMaxWidth()
                                else Modifier,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    isDrawModeSelected = !isDrawModeSelected
                    isEraseModeSelected = false
                },
                    modifier = if (isDrawModeSelected) Modifier.padding(end = 16.dp) else Modifier
                ) {
                    Text(text = drawButtonText)
                }
                var sliderPosition by remember { mutableStateOf(brushStrokeWidth) }
                if (isDrawModeSelected) {
                    Slider(
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it },
                        valueRange = 5f..50f,
                        onValueChangeFinished = {
                            onBrushStrokeWidthChanged(sliderPosition)
                        },
                        steps = 5,
                    )
                }
            }
        }

        if(!isDrawModeSelected){
            Row(modifier = if (isEraseModeSelected) Modifier.fillMaxWidth() else Modifier) {
                Button(onClick = {
                    isEraseModeSelected = !isEraseModeSelected
                    isDrawModeSelected = false
                }) {
                    Text(text = eraseButtonText)
                }

                var eraserSliderPosition by remember { mutableStateOf(eraserStrokeWidth) }
                if (isEraseModeSelected) {
                    Slider(
                        value = eraserSliderPosition,
                        onValueChange = { eraserSliderPosition = it },
                        valueRange = 5f..50f,
                        onValueChangeFinished = {
                            onEraserStrokeWidthChanged(eraserSliderPosition)
                        },
                        steps = 5,
                    )
                }
            }
        }
        if(!isDrawModeSelected && !isEraseModeSelected) {
            Button(onClick = {
                onClearClicked()
            }) {
                Text(text = "Clear")
            }
            Button(onClick = {
                /*TODO*/
            }, modifier = Modifier.padding(end = 16.dp)) {
                Text(text = "Save")
            }
        }
    }
}