package com.example.mapsapp.ui.screens

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.R
import com.example.mapsapp.viewmodels.MyViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMarker(
    navigateToCamera: () -> Unit,
    capturedImage: Bitmap?,
    viewModel: MyViewModel = viewModel()
) {

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                singleLine = true
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                singleLine = true
            )
            if (capturedImage != null) {
                Image(
                    bitmap = capturedImage.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier
                        .size(128.dp)
                        .clickable { navigateToCamera() }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Icono cámara",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable { navigateToCamera() }
                )
            }

            Button(onClick = {
                viewModel.insertNewStudent(title.text, description.text, capturedImage)
            }) {
                Text("Añadir")
            }
        }
    }
}
