package com.example.mapsapp.ui.screens.Auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.AuthViewModelFactory
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.ViewModel

@Composable
fun RegistreScreen(
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: ViewModel =
        viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))

    val authState by viewModel.authState.observeAsState()
    val showError by viewModel.showError.observeAsState()
    val refresh by viewModel.refresh.observeAsState(false)

    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")

    Log.d("MAVOI", authState.toString())
    Log.d("MAVOI", (authState == AuthState.Authenticated).toString())

    if(refresh){
        viewModel.refresh.value = false
        Toast.makeText(context, "User created", Toast.LENGTH_LONG).show()
    }

    if (authState != AuthState.Authenticated) {
        if (showError == true) {
            val errorMessage = (authState as AuthState.Error).message
            if (errorMessage!!.contains("weak_password")) {
                Toast.makeText(
                    context,
                    "Password should be at least 6 characters",
                    Toast.LENGTH_LONG
                ).show()
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
                text = "Register",
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
                    viewModel.signUp()
                },
                modifier = Modifier.fillMaxWidth ()
            ) {
                Text("Register")
            }
        }
    } else {
        navigateToHome()
    }
}