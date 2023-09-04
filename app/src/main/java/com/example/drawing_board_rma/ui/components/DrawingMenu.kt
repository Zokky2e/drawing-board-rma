package com.example.drawing_board_rma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.drawing_board_rma.R
import com.example.drawing_board_rma.ui.theme.ButtonBackground
import com.example.drawing_board_rma.ui.theme.ButtonText
import com.example.drawing_board_rma.ui.theme.Primary

@Composable
fun DrawingMenu(
    onClearClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onDrawingEnabled: (Boolean) -> Unit,
    onTurnOnColorPicker: (Boolean) -> Unit,
    isColorPickerOn: Boolean,
    brushColor: Color,
    changeBrushColor: (Color) -> Unit,
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        if (!isEraseModeSelected) {
            Row(
                modifier = if (isDrawModeSelected)
                    Modifier.fillMaxWidth()
                else Modifier,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val selectedModifier =
                    if (isDrawModeSelected) {

                        Modifier.padding(start = 16.dp)
                    } else {
                        Modifier
                    }
                Box(
                    modifier = selectedModifier
                        .size(32.dp)
                        .background(ButtonBackground)
                        .clickable {
                            isDrawModeSelected = !isDrawModeSelected
                            isEraseModeSelected = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isDrawModeSelected) {
                        Icon(Icons.Rounded.Close, contentDescription = "Draw", tint = ButtonText)
                    } else {
                        Icon(Icons.Rounded.Create, contentDescription = "Draw", tint = ButtonText)
                    }
                }
                var sliderPosition by remember { mutableStateOf(brushStrokeWidth) }
                var resetModifier = Modifier
                    .aspectRatio(1f)
                    .size(16.dp)

                if (isDrawModeSelected) {
                    Box(
                        modifier = resetModifier
                            .clickable { changeBrushColor(Primary) },
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material.Icon(
                            Icons.Rounded.Refresh,
                            contentDescription = "Draw",
                        )
                    }
                    Button(
                        onClick = {
                            onTurnOnColorPicker(!isColorPickerOn)
                        }, modifier = Modifier.padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = brushColor
                        )
                    ) {

                    }
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

        if (!isDrawModeSelected) {
            Row(
                modifier = if (isEraseModeSelected) Modifier.fillMaxWidth() else Modifier,

                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val selectedModifier =
                    if (isEraseModeSelected) {

                        Modifier.padding(start = 16.dp)
                    } else {
                        Modifier
                    }
                Box(
                    modifier = selectedModifier
                        .size(32.dp)
                        .background(ButtonBackground)
                        .clickable {
                            isEraseModeSelected = !isEraseModeSelected
                            isDrawModeSelected = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isEraseModeSelected) {
                        Icon(Icons.Rounded.Close, contentDescription = "Erase", tint = ButtonText)
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.eraser),
                            contentDescription = "Erase",
                            tint = ButtonText
                        )
                    }
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
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                }
            }
        }
        if (!isDrawModeSelected && !isEraseModeSelected) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(32.dp)
                    .background(ButtonBackground)
                    .clickable {
                        onClearClicked()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Delete, contentDescription = "Erase", tint = ButtonText)
            }
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(32.dp)
                    .background(ButtonBackground)
                    .clickable {
                        onSaveClicked()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.save),
                    contentDescription = "Save",
                    tint = ButtonText
                )
            }
        }
    }
}