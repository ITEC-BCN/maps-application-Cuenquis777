package com.example.mapsapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mapsapp.data.Marker
import com.example.mapsapp.viewmodels.ViewModelMap.AuthViewModel
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.AuthViewModelFactory
import com.example.mapsapp.utils.SharedPreferencesHelper

@Composable
fun MarkerList(
    modifier: Modifier,
    navigateToDetail: (Int) -> Unit
) {
    val context = LocalContext.current
    val viewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))

    val showLoading: Boolean by viewModel.loading.observeAsState(true)
    val markers by viewModel.markersList.observeAsState(emptyList())

    viewModel.getAllMarkers()

    if (showLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
    } else {
        if (markers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay favoritos",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp)
            ) {
                LazyColumn(Modifier
                    .fillMaxWidth()
                    .weight(0.6f)) {
                    items(markers, key = { it.id }) { marker ->
                        val dismissState = rememberSwipeToDismissBoxState()

                        LaunchedEffect(dismissState.currentValue) {
                            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteMark(marker.id, image = marker.imageUrl.toString())
                            }
                        }

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                val alignment =
                                    if (dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd)
                                        Alignment.CenterStart else Alignment.CenterEnd
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = Color.White
                                    )
                                }
                            }
                        ) {
                            MarkerItem(marker = marker) {
                                navigateToDetail(marker.id)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MarkerItem(marker: Marker, onClick: () -> Unit) {
    Card(
        border = BorderStroke(2.dp, Color.LightGray),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = marker.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}