package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.mapsapp.viewmodels.ViewModelMap.ViewModel
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
    myViewModel: ViewModel
) {
    val markers = myViewModel.markersList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        myViewModel.getAllMarkers()
    }

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