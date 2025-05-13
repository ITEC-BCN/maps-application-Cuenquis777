package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapsapp.ui.screens.CreateMarker
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerDetailScreen
import com.example.mapsapp.ui.screens.MarkerList
import com.example.mapsapp.viewmodels.ViewModelMap.MyViewModel

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
                myViewModel = MyViewModel()
            )
            
        }

        composable<Destinations.List> {
            MarkerList(
                myViewModel = MyViewModel(),
                modifier = padding,
                navigateToCreateMarker = { id ->
                    navController.navigate(
                        Destinations.MarkerDetails(
                            id
                        )
                    )
                }
            )
            navController.popBackStack(Destinations.List, inclusive = false)
        }


        composable<Destinations.MarkerDetails> { backStackEnrty ->
            val id = backStackEnrty.arguments?.getInt("id") ?: 0
            MarkerDetailScreen(
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
                viewModel = MyViewModel()
            )
        }
    }
}
