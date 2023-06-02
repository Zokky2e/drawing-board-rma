package com.example.drawing_board_rma.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.drawing_board_rma.ui.components.DrawingCanvas
import com.example.drawing_board_rma.ui.theme.Background
import com.example.drawing_board_rma.ui.theme.Secondary

@Composable
fun DrawingCanvasScreen(
    action: MutableState<Pair<Boolean, Pair<Float, Float>>?>,
    path: Path,
    collectList: MutableList<Pair<Boolean, Pair<Float, Float>>>,
    modifier: Modifier
) {
    Column(modifier = Modifier.background(color = Secondary)) {
        Button(onClick = { collectList.clear() }) {
            Text(text = "Clear")
        }
        DrawingCanvas(action = action, path = path, collectList = collectList)
    }
}