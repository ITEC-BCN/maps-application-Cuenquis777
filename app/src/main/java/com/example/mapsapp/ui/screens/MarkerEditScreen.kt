package com.example.mapsapp.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.ViewModelMap.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.scale
import coil.compose.AsyncImage
import com.example.mapsapp.AuthViewModelFactory
import com.example.mapsapp.R
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.CameraViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MarkerEditScreen(
    markerId: Int, navigateBack: () -> Unit) {

    val context = LocalContext.current
    val viewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))

    val markerName: String by viewModel.markerName.observeAsState("")
    val markerMark: String by viewModel.markerMark.observeAsState("")
    var showDialog by remember { mutableStateOf(false) }
    val cameraViewModel: CameraViewModel = viewModel()
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageUrl: String? by viewModel.markerImageUrl.observeAsState()

    viewModel.getMarker(markerId)

    Box(modifier = Modifier.fillMaxSize()) {

        // Botón de volver arriba a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = navigateBack,
                contentPadding = PaddingValues(4.dp)
            ) {
                Text("Volver", fontSize = 24.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 64.dp)
                .padding(top = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = markerName,
                onValueChange = { viewModel.editStudentName(it) },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = markerMark,
                onValueChange = { viewModel.editStudentMark(it) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            // Configuración cámara/galería
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

            // Imagen
            if (cameraViewModel.capturedImage.value != null) {
                Image(
                    bitmap = cameraViewModel.capturedImage.value!!.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier
                        .size(128.dp)
                        .clickable { showDialog = true }
                )
            } else if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Imagen guardada",
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

            //Botones abajo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = {
                        if (markerName.isBlank() || markerMark.isBlank() || cameraViewModel.capturedImage.value == null) {
                            Toast.makeText(context, "Actualiza todos los campos", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            viewModel.updateMarker(
                                name = markerName,
                                mark = markerMark,
                                image = cameraViewModel.capturedImage.value
                            )
                            Log.d("MarkerDetailScreen", "Updating marker with id: $markerId")
                            navigateBack()
                        }
                    },
                ) {
                    Text("Update", fontSize = 24.sp)
                }
            }
        }
    }
}
