package com.example.drawing_board_rma.ui

import AuthViewModel
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.smarttoolfactory.screenshot.rememberScreenshotState
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DrawingCanvasScreen(
    paths: MutableList<DrawItem>,
    authViewModel: AuthViewModel,
    storage: FirebaseStorage,
    handleUserNotLoggedIn: () -> Unit,
    modifier: Modifier
) {
    var context = LocalContext.current
    val isUserSignedIn by authViewModel.isUserSignedIn.collectAsState()
    val cpController = rememberColorPickerController()
    var isColorPickerOn by remember {
        mutableStateOf(false)
    }
    var isConfirmSavePopup by remember {
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

    val confirmSaveClicked = {

        if (isUserSignedIn) {
            screenshotState.imageBitmap?.let {
                val bitmap = it.asAndroidBitmap()
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val data = stream.toByteArray()
                val imageStorage: StorageReference = storage.reference
                val date = LocalDateTime.now().toString()
                val imageRef =
                    imageStorage.child(authViewModel.auth.currentUser?.uid + "/" + date + ".png")
                        .putBytes(data)
                        .addOnCompleteListener {
                            val toast = Toast(context)
                            toast.setText("Saved to cloud!")
                            toast.show()
                        }
                        .addOnFailureListener { e ->
                            val toast = Toast(context)
                            toast.setText("Failed save to cloud!")
                            toast.show()
                        }
            }
        }
    }
    Column(modifier = Modifier.background(color = Secondary)) {
        DrawingMenu(
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
                val toast = Toast(context)
                toast.setText("Whiteboard cleared!")
                toast.show()
            },
            onSaveClicked = {
                if (authViewModel.auth.currentUser != null) {
                    screenshotState.capture()
                    isConfirmSavePopup = true
                } else {
                    handleUserNotLoggedIn()
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
        if (isConfirmSavePopup) {
            val popupAlignment = Alignment.Center
            Popup(
                alignment = popupAlignment,
                onDismissRequest = {
                    isColorPickerOn = false
                }
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(300.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Background)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Do you want to save this drawing?")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                isConfirmSavePopup = false
                            }
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                confirmSaveClicked()
                                isConfirmSavePopup = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }

        if (isColorPickerOn) {
            val popupAlignment = Alignment.Center

            Popup(
                alignment = popupAlignment,
                onDismissRequest = {
                    isColorPickerOn = false
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(400.dp)
                        .background(Background)
                        .padding(16.dp), contentAlignment = popupAlignment
                ) {
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