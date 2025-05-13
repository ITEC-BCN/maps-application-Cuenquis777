/*
package com.example.mapsapp.ui.screens.PantallasAcabadas

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.ViewModelMap.CameraViewModel
import com.example.mapsapp.viewmodels.ViewModelMap.MyViewModel
import java.io.File

@Composable
fun CameraScreen(
    navigateBack: () -> Unit,
    cameraViewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    /*
    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri.value != null) {
                val stream = context.contentResolver.openInputStream(imageUri.value!!)
                cameraViewModel.capturedImage.value = BitmapFactory.decodeStream(stream)
                navigateBack()
            }
        }
       */

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val stream = context.contentResolver.openInputStream(imageUri.value!!)
                stream?.use {
                    // Decodificar el flujo a un Bitmap
                    val originalBitmap = BitmapFactory.decodeStream(it)

                    // Obtener las dimensiones originales de la imagen
                    val originalWidth = originalBitmap.width
                    val originalHeight = originalBitmap.height

                    // Definir el aspect ratio (relación entre ancho y alto)
                    val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()

                    // Establecer el tamaño máximo que deseas para la imagen (por ejemplo, un ancho máximo)
                    val maxWidth = 800 // Puedes establecer el valor que prefieras

                    // Calcular el nuevo ancho y alto manteniendo el aspect ratio
                    val newWidth = maxWidth
                    val newHeight = (newWidth / aspectRatio).toInt()

                    // Redimensionar el bitmap mientras se mantiene el aspect ratio
                    val resizedBitmap = originalBitmap.scale(newWidth, newHeight)

                    // Establecer el Bitmap redimensionado en el ViewModel
                    cameraViewModel.capturedImage.value = resizedBitmap
                    navigateBack()
                } ?: run {
                    Log.e("TakePicture", "Error al abrir InputStream para la URI de la imagen.")
                }
            } else {
                Log.e("TakePicture", "La imagen no fue tomada o la URI de la imagen es nula.")
            }
        }


    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri.value = it
                val stream = context.contentResolver.openInputStream(it)
                cameraViewModel.capturedImage.value = BitmapFactory.decodeStream(stream)
                navigateBack()
            }
        }

    var showDialog by remember { mutableStateOf(false) }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Abrir Cámara o Galería")
        }

        Spacer(modifier = Modifier.height(24.dp))

        cameraViewModel.capturedImage.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
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
 */