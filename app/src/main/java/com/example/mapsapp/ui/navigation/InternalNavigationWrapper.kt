package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapsapp.ui.screens.CameraScreen
import com.example.mapsapp.ui.screens.CreateMarker
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerList
import com.example.mapsapp.viewmodels.CameraViewModel

@Composable
fun InternalNavigationWrapper(navController: NavHostController, padding: Modifier) {
    val cameraViewModel: CameraViewModel = viewModel()

    NavHost(navController, Destinations.Map) {
        composable<Destinations.Map> {
            MapsScreen(
                modifier = padding,
                navigateToMaker = { latitud, longitud ->
                    navController.navigate(Destinations.MarkerCreation(latitud, longitud))
                }
            )
        }

        composable<Destinations.List> {
            MarkerList()
        }

        composable<Destinations.MarkerCreation> {
            CreateMarker(
                navigateToCamera = {
                    navController.navigate(Destinations.Camera)
                },
                capturedImage = cameraViewModel.capturedImage.value
            )
        }

        composable<Destinations.Camera> {
            CameraScreen(
                navigateBack = { navController.popBackStack() },
                cameraViewModel = cameraViewModel
            )
        }
    }
}
