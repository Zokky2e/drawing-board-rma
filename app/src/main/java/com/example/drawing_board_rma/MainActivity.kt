package com.example.drawing_board_rma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.navigation.compose.NavHost
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drawing_board_rma.ui.DrawingCanvasScreen
import com.example.drawing_board_rma.ui.GalleryScreen
import com.example.drawing_board_rma.ui.theme.Background
import com.example.drawing_board_rma.ui.theme.ButtonText
import com.example.drawing_board_rma.ui.theme.DrawingboardrmaTheme
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person

enum class CurrentScreen(@StringRes val title: Int) {
    Start(title = R.string.drawing),
    Profile(title = R.string.profile),
    Gallery(title = R.string.gallery)
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private var currentScreen: CurrentScreen = CurrentScreen.Start
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            DrawingboardrmaTheme {
                Box(
                    modifier = Modifier
                ) {
                    Scaffold(
                        modifier = Modifier,
                        bottomBar = {
                            BottomNavigation(
                                backgroundColor = Background,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                                content = {
                                    Button(
                                        onClick = {
                                            currentScreen = CurrentScreen.Start
                                            navController.navigate(currentScreen.name)
                                        },
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .padding(start = 16.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Icon(Icons.Rounded.Create, contentDescription = "Draw")
                                            Text(text = "Draw", color = ButtonText)
                                        }
                                    }
                                    Button(
                                        onClick = {
                                            currentScreen = CurrentScreen.Profile
                                            navController.navigate(currentScreen.name)
                                        },
                                        modifier = Modifier
                                            .padding(4.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Icon(Icons.Rounded.Person, contentDescription = "Draw")
                                            Text(text = "Profile", color = ButtonText)
                                        }
                                    }
                                    Button(
                                        onClick = {
                                            currentScreen = CurrentScreen.Gallery
                                            navController.navigate(currentScreen.name)
                                        },
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .padding(end = 16.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Icon(Icons.Rounded.List, contentDescription = "Draw")
                                            Text(text = "Gallery", color = ButtonText)
                                        }
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = currentScreen.name,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(route = CurrentScreen.Start.name) {
                                DrawingCanvasScreen(
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                            composable(route = CurrentScreen.Profile.name) {
                                Column {
                                    Text(text = "Profile")
                                }
                            }
                            composable(route = CurrentScreen.Gallery.name) {
                                GalleryScreen()
                            }
                        }
                    }
                }


            }
        }
    }
}


