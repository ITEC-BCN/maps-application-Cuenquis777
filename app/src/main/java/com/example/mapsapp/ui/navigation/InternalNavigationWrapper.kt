package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapsapp.ui.screens.CreateMarker
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerList

@Composable
fun InternalNavigationWrapper(navController: NavHostController, padding: Modifier) {
    NavHost(navController, Destinations.Map) {
        //Entramaos a la pantalla del mapa
        composable<Destinations.Map>{
            MapsScreen(
                modifier = padding,
                navigateToMaker = { latitud, longitud ->
                    navController.navigate(Destinations.MarkerCreation(latitud, longitud))
                },
            )
        }

        //Entramos a la pantalla de la lista
        composable<Destinations.List>{
            MarkerList()
        }

        composable<Destinations.MarkerCreation> {
            CreateMarker()
        }
    }
}
