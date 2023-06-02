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
import com.example.drawing_board_rma.ui.theme.Primary


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    action: MutableState<Pair<Boolean, Pair<Float, Float>>?>,
    path: Path,
    collectList: MutableList<Pair<Boolean, Pair<Float, Float>>>,
    modifier: Modifier = Modifier.background(Color.White),
) {

    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInteropFilter {
        when (it.action) {
            MotionEvent.ACTION_DOWN -> {
                action.value = Pair(true, Pair(it.x, it.y))
                path.moveTo(it.x, it.y)
                collectList.add(Pair(true, Pair(it.x, it.y)))
            }

            MotionEvent.ACTION_MOVE -> {
                action.value = Pair(false, Pair(it.x, it.y))
                path.lineTo(it.x, it.y)
                collectList.add(Pair(false, Pair(it.x, it.y)))
            }

            MotionEvent.ACTION_UP -> {

            }

            else -> false

        }
        true
    }) {
        action.value?.let {
            drawPath(
                path = collectList.toPath(),
                color = Primary,
                alpha = 1f,
                style = Stroke(10f)
            )
        }
    }
}

fun MutableList<Pair<Boolean, Pair<Float, Float>>>.toPath(): Path {
    val path = Path()
    forEach {
        if(it.first) {
            path.moveTo(it.second.first, it.second.second)
        } else {
            path.lineTo(it.second.first, it.second.second)
        }
    }

    return path
}