package com.example.mapsapp.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.R
import com.example.mapsapp.viewmodels.CameraViewModel
import com.example.mapsapp.viewmodels.MyViewModel
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMarker(
    navigateToMap: () -> Unit,
    image: Bitmap?, // este ya no se usa
    viewModel: MyViewModel = viewModel()
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var mark by remember { mutableStateOf(TextFieldValue("")) }
    val cameraViewModel: CameraViewModel = viewModel()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val stream = context.contentResolver.openInputStream(imageUri.value!!)
                stream?.use {
                    val originalBitmap = BitmapFactory.decodeStream(it)
                    val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height
                    val newWidth = 800
                    val newHeight = (newWidth / aspectRatio).toInt()
                    val resizedBitmap = originalBitmap.scale(newWidth, newHeight)
                    cameraViewModel.setImage(resizedBitmap)
                } ?: Log.e("CreateMarker", "No se pudo abrir InputStream.")
            }
        }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val stream = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(stream)
                cameraViewModel.setImage(bitmap)
            }
        }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Selecciona una opción") },
            text = { Text("¿Quieres tomar una foto o elegir una desde la galería?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    val uri = createImageUri(context)
                    imageUri.value = uri
                    takePictureLauncher.launch(uri!!)
                }) {
                    Text("Tomar Foto")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    pickImageLauncher.launch("image/*")
                }) {
                    Text("Elegir de Galería")
                }
            }
        )
    }

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
                value = name,
                onValueChange = { name = it },
                label = { Text("Título") },
                singleLine = true
            )
            TextField(
                value = mark,
                onValueChange = { mark = it },
                label = { Text("Descripción") },
                singleLine = true
            )

            // Imagen capturada o icono de cámara
            cameraViewModel.capturedImage.value?.let { captured ->
                Image(
                    bitmap = captured.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier
                        .size(128.dp)
                        .clickable { showDialog = true }
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "Icono cámara",
                modifier = Modifier
                    .size(64.dp)
                    .clickable { showDialog = true }
            )

            Button(onClick = {
                viewModel.insertNewStudent(
                    name = name.text,
                    mark = mark.text,
                    image = cameraViewModel.capturedImage.value
                )
                navigateToMap()
            }) {
                Text("Añadir")
            }
        }
    }
}

fun createImageUri(context: Context): Uri? {
    val file = File.createTempFile("temp_image_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}
