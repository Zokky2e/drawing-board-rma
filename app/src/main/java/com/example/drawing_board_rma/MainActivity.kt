package com.example.drawing_board_rma

import AuthViewModel
import FirebaseAuthManager
import GalleryScreen
import GalleryViewModel
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drawing_board_rma.ui.DrawingCanvasScreen
import com.example.drawing_board_rma.ui.ProfileScreen
import com.example.drawing_board_rma.ui.components.DrawItem
import com.example.drawing_board_rma.ui.theme.Background
import com.example.drawing_board_rma.ui.theme.ButtonText
import com.example.drawing_board_rma.ui.theme.DrawingboardrmaTheme
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

enum class CurrentScreen(@StringRes val title: Int) {
    Start(title = R.string.drawing),
    Profile(title = R.string.profile),
    Gallery(title = R.string.gallery)
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private var currentScreen: CurrentScreen = CurrentScreen.Start

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel = AuthViewModel(FirebaseAuthManager())
            val storage = Firebase.storage
            val navController = rememberNavController()
            val paths = mutableListOf<DrawItem>()
            val galleryViewModel = GalleryViewModel(storage, authViewModel)
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
                                            Icon(
                                                Icons.Rounded.Person,
                                                contentDescription = "Profile"
                                            )
                                            Text(text = "Profile", color = ButtonText)
                                        }
                                    }
                                    Button(
                                        onClick = {
                                            if (authViewModel.auth.currentUser != null) {
                                                currentScreen = CurrentScreen.Gallery
                                                navController.navigate(currentScreen.name)
                                            } else {
                                                currentScreen = CurrentScreen.Profile
                                                navController.navigate(currentScreen.name)
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .padding(end = 16.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Icon(Icons.Rounded.List, contentDescription = "Gallery")
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
                                    paths = paths,
                                    authViewModel,
                                    storage,
                                    handleUserNotLoggedIn = {
                                        if (authViewModel.auth.currentUser == null) {
                                            currentScreen = CurrentScreen.Profile
                                            navController.navigate(currentScreen.name)
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                            composable(route = CurrentScreen.Profile.name) {
                                ProfileScreen(authViewModel)
                            }
                            composable(route = CurrentScreen.Gallery.name) {
                                if (authViewModel.auth.currentUser != null) {
                                    authViewModel.auth.uid?.let {
                                        galleryViewModel.fetchImagesFromStorage(
                                            it
                                        )
                                        GalleryScreen(authViewModel, storage, galleryViewModel)
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }
    }
}


