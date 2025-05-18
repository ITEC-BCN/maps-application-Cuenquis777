package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mapsapp.AuthViewModelFactory
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerDetailScreen(markerId: Int, navigateBack: () -> Unit, navigateToEdit: (Int) -> Unit) {

    val viewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(LocalContext.current)))

    val markerName by viewModel.markerName.observeAsState("")
    val markerMark by viewModel.markerMark.observeAsState("")
    val markerImageUrl by viewModel.markerImageUrl.observeAsState("")

    viewModel.getMarker(markerId)

    Box(modifier = Modifier.fillMaxSize()) {

        //Botón "Volver" arriba a la derecha
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Nombre

            Text(
                text = markerName,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen
            if (!markerImageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = markerImageUrl,
                    contentDescription = "Imagen del marcador",
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                Text(
                    text = "Imagen no disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = markerMark,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón editar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = { navigateToEdit(markerId) }
                ) {
                    Text("Editar", fontSize = 18.sp)
                }
            }
        }
    }
}
