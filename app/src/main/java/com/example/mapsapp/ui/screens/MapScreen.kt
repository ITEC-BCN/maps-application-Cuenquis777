package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.AuthViewModelFactory
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.AuthViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsScreen(
    modifier: Modifier = Modifier,
    navigateToMaker: (Double, Double) -> Unit,
) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))
    val markers = viewModel.markersList.observeAsState(emptyList())

    viewModel.getAllMarkers()

    Column(modifier.fillMaxSize()) {
        val itb = LatLng(41.4534225, 2.1837151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 17f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = {
                navigateToMaker(it.latitude, it.longitude)
            }
        ) {
            markers.value.forEach { marker ->
                val position = LatLng(marker.latitude, marker.longitude)
                Marker(
                    state = MarkerState(position = position),
                    title = marker.name,
                    snippet = "Lat: ${marker.latitude}, Lng: ${marker.longitude}",
                )
            }
        }
    }
}