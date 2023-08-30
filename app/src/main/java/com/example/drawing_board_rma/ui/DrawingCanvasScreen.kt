package com.example.drawing_board_rma.ui

import AuthViewModel
import FirebaseAuthManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.drawing_board_rma.ui.components.DrawItem
import com.example.drawing_board_rma.ui.components.DrawMode
import com.example.drawing_board_rma.ui.components.DrawingCanvas
import com.example.drawing_board_rma.ui.components.DrawingMenu
import com.example.drawing_board_rma.ui.theme.Background
import com.example.drawing_board_rma.ui.theme.EraserColor
import com.example.drawing_board_rma.ui.theme.Primary
import com.example.drawing_board_rma.ui.theme.Secondary
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.smarttoolfactory.screenshot.rememberScreenshotState
import java.io.ByteArrayOutputStream
import kotlin.random.Random


@Composable
fun DrawingCanvasScreen(
    paths: MutableList<DrawItem>,
    authViewModel: AuthViewModel,
    modifier: Modifier
) {
    val isUserSignedIn by authViewModel.isUserSignedIn.collectAsState()
    val cpController = rememberColorPickerController()
    var isColorPickerOn by remember {
        mutableStateOf(false)
    }
    val paths = remember { paths }
    val action = remember {
        mutableStateOf<Pair<Boolean, Pair<Float, Float>>?>(null)
    }
    var currentDrawMode by remember {
        mutableStateOf(DrawMode.Draw)
    }
    var isDrawMode by remember { mutableStateOf(false) }
    var isEraseMode by remember { mutableStateOf(false) }
    var color by remember { mutableStateOf(Primary) }
    var brushColor by remember { mutableStateOf(Primary) }
    var futureColor by remember { mutableStateOf(Primary) }
    var brushStrokeWidth by remember {
        mutableStateOf(5f)
    }
    var eraserStrokeWidth by remember {
        mutableStateOf(5f)
    }
    var screenshotState = rememberScreenshotState()
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
            onBrushStrokeWidthChanged = { value ->
                brushStrokeWidth = value
            },
            onEraserStrokeWidthChanged = { value ->
                eraserStrokeWidth = value
            },
            onClearClicked = {
                paths.clear()
            },
            onSaveClicked = {
                if (isUserSignedIn) {
                    screenshotState.capture()
                    screenshotState.imageBitmap?.let {
                        val bitmap = it.asAndroidBitmap()
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val data = stream.toByteArray()
                        val storage = Firebase.storage
                        val imageStorage: StorageReference = storage.reference
                        val imageRef = imageStorage.child(authViewModel.auth.currentUser?.uid + "/image"+ Random.nextInt().toString()+".png").putBytes(data)
                            .addOnSuccessListener {
                                var result = it
                            }
                            .addOnFailureListener { e ->
                                var error = e
                            }
                    }
                }
            },
            onTurnOnColorPicker = { value ->
                isColorPickerOn = value
            },
            isColorPickerOn = isColorPickerOn,
            brushColor = brushColor,
            changeBrushColor = {
                brushColor = it
            }
        )
        if (isDrawMode) {
            color = brushColor
        } else if (isEraseMode) {
            color = EraserColor
        }
        currentDrawMode = if (isDrawMode) {
            DrawMode.Draw
        } else if (isEraseMode) {
            DrawMode.Erase
        } else {
            DrawMode.Touch
        }
        if (isColorPickerOn) {
            val popupAlignment = Alignment.Center

            Popup(
                alignment = popupAlignment,
                onDismissRequest = {
                    isColorPickerOn = false
                }
            ) {
                Box(modifier = Modifier
                    .size(400.dp)
                    .background(Background)
                    .padding(16.dp), contentAlignment = popupAlignment) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Button(
                            onClick = {
                                brushColor = futureColor
                                isColorPickerOn = false
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = "Choose")
                        }
                        HsvColorPicker(
                            modifier = Modifier
                                .height(450.dp)
                                .padding(10.dp),
                            controller = cpController,
                            initialColor = brushColor,
                            onColorChanged = { colorEnvelope: ColorEnvelope ->
                                futureColor = colorEnvelope.color
                            }
                        )
                    }
                }
            }
        }
        DrawingCanvas(
            drawMode = DrawMode.Draw,
            collectList = paths,
            screenshotState = screenshotState,
            action = action,
            color = color,
            brushStrokeWidth = if (isEraseMode) eraserStrokeWidth else brushStrokeWidth,
        )
    }

}