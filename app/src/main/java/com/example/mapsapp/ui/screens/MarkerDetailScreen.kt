package com.example.mapsapp.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.ViewModelMap.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import coil.compose.AsyncImage
import com.example.mapsapp.AuthViewModelFactory
import com.example.mapsapp.R
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.CameraViewModel


@Composable
fun MarkerDetailScreen(markerId: Int, navigateBack: () -> Unit) {

    val factory = AuthViewModelFactory(
        SharedPreferencesHelper(LocalContext.current)
    )

    val myViewModel: ViewModel = viewModel(factory = factory)

    val markerName: String by myViewModel.studentName.observeAsState("")
    val markerMark: String by myViewModel.studentMark.observeAsState("")
    var showDialog by remember { mutableStateOf(false) }
    val cameraViewModel: CameraViewModel = viewModel()
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    myViewModel.getMarker(markerId)

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = markerName, onValueChange = { myViewModel.editStudentName(it) })
        TextField(value = markerMark, onValueChange = { myViewModel.editStudentMark(it) })
        val imageUrl: String? by myViewModel.studentImageUrl.observeAsState()

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
        if (cameraViewModel.capturedImage.value != null) {
            val capturedImage = cameraViewModel.capturedImage.value!!
            Image(
                bitmap = capturedImage.asImageBitmap(),
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

        Button(onClick = {
            myViewModel.deleteMark(
                id = markerId,
                image = imageUrl.toString()
            )
            navigateBack()
        }) {
            Text("Delete")
        }

        Button(onClick = {
            myViewModel.updateMarker(
                name = markerName,
                mark = markerMark,
                image = cameraViewModel.capturedImage.value
            )
            Log.d("MarkerDetailScreen", "Updating marker with id: $markerId")
            Log.d("MarkerDetailScreen", "Name: $markerName $markerMark")
            navigateBack()
        }) {
            Text("Update")
        }
    }
}