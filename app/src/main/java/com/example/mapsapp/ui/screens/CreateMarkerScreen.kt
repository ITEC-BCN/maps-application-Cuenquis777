package com.example.mapsapp.ui.screens

import android.content.Context
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.mapsapp.AuthViewModelFactory
import com.example.mapsapp.R
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.CameraViewModel
import com.example.mapsapp.viewmodels.ViewModelMap.AuthViewModel
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMarker(
    latitude: Double,
    longitude: Double,
    navigateToBack: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))

    val cameraViewModel: CameraViewModel = viewModel()
    val name by viewModel.markerName.observeAsState("")
    val mark by viewModel.markerMark.observeAsState("")
    var showDialog by remember { mutableStateOf(false) }
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    //estado de la imagen capturada
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

    // Mostrar el diálogo para elegir entre tomar una foto o elegir de la galería
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

    // Mostrar la interfaz de usuario para crear un nuevo marcador
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
                onValueChange = { viewModel.markerName.value = it },
                label = { Text("Título") },
                singleLine = true
            )
            TextField(
                value = mark,
                onValueChange = { viewModel.markerMark.value = it },
                label = { Text("Descripción") },
                singleLine = true
            )

            // Mostrar imagen capturada o ícono de cámara si no hay imagen
            if (cameraViewModel.capturedImage.value != null) {
                val capturedImage = cameraViewModel.capturedImage.value!!
                Image(
                    bitmap = capturedImage.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier
                        .size(128.dp)
                        .clickable { showDialog = true }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Icono de cámara",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable { showDialog = true }
                )
            }
            Button(onClick = {
                viewModel.insertNewMarker(
                    name = name,
                    mark = mark,
                    image = cameraViewModel.capturedImage.value,
                    latitude = latitude,
                    longitude = longitude
                )
                navigateToBack()
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