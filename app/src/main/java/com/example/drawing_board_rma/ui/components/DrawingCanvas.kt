package com.example.drawing_board_rma.ui.components

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.example.drawing_board_rma.ui.theme.EraserColor
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.ScreenshotState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    action: MutableState<Pair<Boolean, Pair<Float, Float>>?>,
    collectList: MutableList<DrawItem>,
    screenshotState: ScreenshotState,
    color: Color,
    drawMode: DrawMode,
    brushStrokeWidth: Float,
    modifier: Modifier = Modifier,
) {
    val path = Path()
    ScreenshotBox(screenshotState = screenshotState) {
        Canvas(modifier = modifier
            .fillMaxSize()
            .background(EraserColor)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        action.value = Pair(true, Pair(it.x, it.y))
                        path.moveTo(it.x, it.y)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        action.value = Pair(false, Pair(it.x, it.y))
                        path.lineTo(it.x, it.y)
                    }

                    MotionEvent.ACTION_UP -> {
                        collectList.add(
                            DrawItem(
                                path = path,
                                eraseMode = drawMode == DrawMode.Erase,
                                color = color,
                                strokeWidth = brushStrokeWidth
                            )
                        )
                    }

                    else -> false

                }
                true
            }) {
            action.value = Pair(true, Pair(0f, 0f))
            action.value.let {
                collectList.forEach { item ->
                    drawPath(
                        path = item.path,
                        color = item.color,
                        alpha = 1f,
                        style = Stroke(item.strokeWidth)
                    )
                }
            }
        }
    }

}
