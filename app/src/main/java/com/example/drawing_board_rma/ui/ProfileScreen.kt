package com.example.drawing_board_rma.ui

import AuthViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel
) {
    val isUserSignedIn by authViewModel.isUserSignedIn.collectAsState()
    var isLoginModule by remember {
        mutableStateOf(true)
    }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    if (isUserSignedIn) {
        email = authViewModel.auth.currentUser?.email.toString()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Email")
            Text(text = email)
            Button(
                onClick = {
                    authViewModel.auth.signOut()
                    authViewModel.checkUserSignIn()
                    email = ""
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Logout")

            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SliderButton("Login", isLoginModule) {
                    if (!isLoginModule) {
                        isLoginModule = true
                        errorMessage = ""
                        email = ""
                        password = ""
                        repeatPassword = ""
                    }
                }
                SliderButton("Register", !isLoginModule) {
                    if (isLoginModule) {
                        isLoginModule = false
                        errorMessage = ""
                        email = ""
                        password = ""
                        repeatPassword = ""
                    }
                }
            }
            Text(
                text = if (isLoginModule) "Login" else "Register",
                modifier = Modifier.padding(16.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .padding(16.dp)
                        .height(60.dp)
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(16.dp)
                        .height(60.dp)
                )
                if (!isLoginModule) {
                    TextField(
                        value = repeatPassword,
                        onValueChange = { repeatPassword = it },
                        label = { Text("Confirm Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .padding(16.dp)
                            .height(60.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .height(60.dp)
                    ) {

                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SliderButton(if (isLoginModule) "Login" else "Register", false) {
                        if (validateUserInfo(isLoginModule, email, password, repeatPassword)) {
                            if (isLoginModule) {
                                authViewModel.auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener() { task ->
                                        if (task.isSuccessful) {
                                            val user = authViewModel.auth.currentUser
                                            authViewModel.checkUserSignIn()
                                            errorMessage = ""
                                            email = ""
                                            password = ""
                                            repeatPassword = ""
                                        } else {
                                            errorMessage = "Wrong user information given"
                                        }
                                    }

                            } else {
                                authViewModel.auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener() { task ->
                                        if (task.isSuccessful) {
                                            val user = authViewModel.auth.currentUser
                                            authViewModel.checkUserSignIn()
                                            errorMessage = ""
                                        } else {
                                            errorMessage = "Failed to login, user already exists"
                                        }
                                    }
                            }
                        } else {
                            errorMessage = "Input fields are incorrect"
                        }
                    }
                }
                Text(text = errorMessage, color = Color.Red)

            }
        }
    }
}

fun validateUserInfo(
    isLogin: Boolean,
    email: String,
    password: String,
    repeatPassword: String
): Boolean {
    if (isLogin) {
        if (email.isEmpty() || password.isEmpty()) {
            return false
        }

    } else {
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            return false
        }
        if (password != repeatPassword) {
            return false
        }
    }
    return true
}

@Composable
fun SliderButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color.Gray else Color.White
    val contentColor = if (isSelected) Color.White else Color.Black

    Button(
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .height(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Text(text = text)
    }
}




