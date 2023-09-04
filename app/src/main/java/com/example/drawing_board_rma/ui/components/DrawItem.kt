package com.example.drawing_board_rma.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.drawing_board_rma.ui.theme.Primary

class DrawItem(
    var strokeWidth: Float = 10f,
    var path: Path,
    var color: Color = Primary,
    var eraseMode: Boolean = false,
)
