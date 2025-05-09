package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapsapp.ui.screens.CreateMarker
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerList
import com.example.mapsapp.viewmodels.CameraViewModel
import com.example.mapsapp.viewmodels.MyViewModel

@RequiresApi(Build.VERSION_CODES.O)
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
                image = cameraViewModel.capturedImage.value,
                navigateToMap = { navController.navigate(Destinations.Map) },
                viewModel = MyViewModel()
            )
        }


    }
}
