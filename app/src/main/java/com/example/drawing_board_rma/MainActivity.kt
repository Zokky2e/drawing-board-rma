package com.example.drawing_board_rma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.navigation.compose.NavHost
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drawing_board_rma.ui.DrawingCanvasScreen
import com.example.drawing_board_rma.ui.theme.Background
import com.example.drawing_board_rma.ui.theme.ButtonText
import com.example.drawing_board_rma.ui.theme.DrawingboardrmaTheme

enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {


    private val action: MutableState<Pair<Boolean, Pair<Float, Float>>?> = mutableStateOf(null)
    private val path = Path()
    private val collectList = mutableListOf<Pair<Boolean, Pair<Float, Float>>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrawingboardrmaTheme {
                Box(
                    modifier = Modifier
                ) {
                    Scaffold(
                        modifier = Modifier,
                        {
                            TopAppBar(
                                title = {
                                    Text(text = "Drawing app")
                                },
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = Background
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = Background),
                                navigationIcon = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(Alignment.End)
                                            .wrapContentHeight(Alignment.CenterVertically)
                                            .padding(4.dp)
                                    ) {
                                        Text(
                                            text = "Drawing App",
                                            modifier = Modifier
                                                .padding(16.dp),
                                            )
                                        Button(
                                            onClick = { },
                                            modifier = Modifier
                                                .padding(4.dp)
                                        ) {
                                            Text(text = "Draw", color = ButtonText)
                                        }
                                        Button(
                                            onClick = { },
                                            modifier = Modifier
                                                .padding(4.dp)
                                        ) {
                                            Text(text = "Profile", color = ButtonText)
                                        }
                                        Button(
                                            onClick = { },
                                            modifier = Modifier
                                                .padding(4.dp)
                                        ) {
                                            Text(text = "Gallery", color = ButtonText)
                                        }
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = rememberNavController(),
                            startDestination = CupcakeScreen.Start.name,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(route = CupcakeScreen.Start.name) {
                                DrawingCanvasScreen(
                                    action = action,
                                    path = path,
                                    collectList = collectList,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                        }
                    }
                }


            }
        }
    }
}


