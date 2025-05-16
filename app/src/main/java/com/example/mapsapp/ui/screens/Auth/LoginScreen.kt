package com.example.mapsapp.ui.screens.Auth

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.AuthViewModelFactory
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.ViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mapsapp.utils.AuthState

@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: ViewModel = viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))

    val authState by viewModel.authState.observeAsState()
    val showError by viewModel.showError.observeAsState()
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")

    if (authState == AuthState.Authenticated) {
        navigateToHome()
    } else {
        if (showError == true) {
            val errorMessage = (authState as AuthState.Error).message
            if (errorMessage!!.contains("invalid_credentials")) {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "An error has occurred", Toast.LENGTH_LONG).show()
            }
            viewModel.errorMessageShowed()
        }

        // User Interface
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            BasicTextField(
                value = email,
                onValueChange = { viewModel.editEmail(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        if (email.isEmpty()) Text("Email")
                        innerTextField()
                    }
                }
            )

            BasicTextField(
                value = password,
                onValueChange = { viewModel.editPassword(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        if (password.isEmpty()) Text("Password")
                        innerTextField()
                    }
                }
            )

            Button(
                onClick = {
                    viewModel.editEmail(email)
                    viewModel.editPassword(password)
                    viewModel.signIn()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            TextButton(onClick = {navigateToRegister()}) {
                Text("Don't have an account? Register")
            }
        }
    }
}