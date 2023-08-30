package com.example.drawing_board_rma.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.drawing_board_rma.ui.theme.Primary
import androidx.compose.ui.graphics.vector.PathParser
import android.graphics.PathMeasure
import androidx.compose.ui.graphics.asAndroidPath

class DrawItem (
    var strokeWidth: Float = 10f,
    var path: Path,
    var color: Color = Primary,
    var eraseMode: Boolean = false,
) {

    fun pathToString(): String {
        val pathMeasure = PathMeasure(path.asAndroidPath(), false)
        val length = pathMeasure.length

        val flattenedPath = StringBuilder()
        val position = FloatArray(2)
        val tangent = FloatArray(2)

        val step = 0.01f // Adjust this value to control the level of detail

        var distance = 0f
        while (distance <= length) {
            pathMeasure.getPosTan(distance, position, tangent)
            flattenedPath.append("${position[0]},${position[1]},")
            distance += step
        }

        return flattenedPath.toString()
    }
}