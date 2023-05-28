package com.example.drawing_board_rma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import com.example.drawing_board_rma.ui.components.DrawingCanvas
import com.example.drawing_board_rma.ui.theme.DrawingboardrmaTheme

class MainActivity : ComponentActivity() {


    private val action: MutableState<Pair<Boolean, Pair<Float, Float>>?> = mutableStateOf(null)
    private val path = Path()

    private val collectList = mutableListOf<Pair<Boolean, Pair<Float, Float>>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrawingboardrmaTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column() {
                        Button(onClick = { collectList.clear()}) {
                            Text(text = "Clear")
                        }
                        DrawingCanvas(action = action, path = path, collectList=collectList)
                    }
                }
            }
        }
    }
}
