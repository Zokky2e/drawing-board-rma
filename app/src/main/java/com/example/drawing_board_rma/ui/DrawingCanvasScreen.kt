package com.example.drawing_board_rma.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.drawing_board_rma.ui.components.DrawItem
import com.example.drawing_board_rma.ui.components.DrawMode
import com.example.drawing_board_rma.ui.components.DrawingCanvas
import com.example.drawing_board_rma.ui.components.DrawingMenu
import com.example.drawing_board_rma.ui.theme.EraserColor
import com.example.drawing_board_rma.ui.theme.Primary
import com.example.drawing_board_rma.ui.theme.Secondary

@Composable
fun DrawingCanvasScreen(
    modifier: Modifier
) {
    val paths = remember { mutableListOf<DrawItem>() }
    val action = remember {
        mutableStateOf<Pair<Boolean,Pair<Float,Float>>?>(null)
    }
    var currentDrawMode by remember {
        mutableStateOf(DrawMode.Draw)
    }
    var isDrawMode by remember { mutableStateOf(false) }
    var isEraseMode by remember { mutableStateOf(false) }
    var brushColor by remember { mutableStateOf(Primary) }
    var brushStrokeWidth by remember {
        mutableStateOf(5f)
    }
    var eraserStrokeWidth by remember {
        mutableStateOf(5f)
    }
    Column(modifier = Modifier.background(color = Secondary)) {
        DrawingMenu(
            collectList = paths,
            onDrawingEnabled = { value ->
                isDrawMode = value
            }, onErasingEnabled = { value ->
                isEraseMode = value
            },
            brushStrokeWidth = brushStrokeWidth,
            eraserStrokeWidth = eraserStrokeWidth,
            onBrushStrokeWidthChanged = {
                value -> brushStrokeWidth = value
            },
            onEraserStrokeWidthChanged = {
                    value -> eraserStrokeWidth = value
            },
        onClearClicked = {
            paths.clear()
        })
        if (isDrawMode) {
            brushColor = Primary
        } else if (isEraseMode) {
            brushColor = EraserColor
        }
        currentDrawMode = if (isDrawMode) {
            DrawMode.Draw
        } else if (isEraseMode) {
            DrawMode.Erase
        } else {
            DrawMode.Touch
        }
        DrawingCanvas(
            drawMode = DrawMode.Draw,
            collectList = paths,
            action = action,
            color = brushColor,
            brushStrokeWidth = if(isEraseMode) eraserStrokeWidth else brushStrokeWidth,
        )
    }
}