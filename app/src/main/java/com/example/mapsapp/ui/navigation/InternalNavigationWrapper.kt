package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapsapp.ui.screens.MapsScreen

@Composable
fun InternalNavigationWrapper(navController: NavHostController, padding: Modifier) {
    NavHost(navController, Destinations.Map) {
        composable<Destinations.Map>{
            MapsScreen()
        }
    }
}
