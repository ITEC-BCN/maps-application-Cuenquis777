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
import com.example.mapsapp.ui.screens.MarkerDetailScreen
import com.example.mapsapp.ui.screens.MarkerEditScreen
import com.example.mapsapp.ui.screens.MarkerList
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InternalNavigationWrapper(navController: NavHostController, padding: Modifier) {

    NavHost(navController, Destinations.Map) {

        composable<Destinations.Map> {
            MapsScreen(
                modifier = padding,
                navigateToMaker = { latitud, longitud ->
                    navController.navigate(Destinations.MarkerCreation(latitud, longitud))
                },
            )
        }

        composable<Destinations.List> {
            MarkerList(
                modifier = padding,
                navigateToDetail = { id ->
                    navController.navigate(Destinations.MarkerDetails(id))
                }
            )
        }


        composable<Destinations.MarkerDetails> { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            MarkerDetailScreen(
                markerId = id,
                navigateBack = { navController.navigate(Destinations.List) },
                navigateToEdit = { id ->
                    navController.navigate(Destinations.MarkerEdit(id))
                },
            )
        }

        composable<Destinations.MarkerEdit> { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            MarkerEditScreen(
                markerId = id,
                navigateBack = { navController.navigate(Destinations.List) },
            )
        }


        composable<Destinations.MarkerCreation> { backStackEntry ->
            val latitude = backStackEntry.arguments?.getDouble("latitud") ?: 0.0
            val longitude = backStackEntry.arguments?.getDouble("longitud") ?: 0.0
            CreateMarker(
                latitude = latitude,
                longitude = longitude,
                navigateToBack = { navController.navigate(Destinations.Map) },
            )
        }
    }
}
