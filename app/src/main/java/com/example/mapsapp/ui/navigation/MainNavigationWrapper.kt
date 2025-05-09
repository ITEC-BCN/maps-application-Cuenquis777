package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destinations.Drawer
import com.example.mapsapp.ui.navigation.Destinations.Permissions
import com.example.mapsapp.ui.screens.PantallasAcabadas.DrawerScreen
import com.example.mapsapp.ui.screens.PermissionsScreen

@Composable
fun MainNavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController, Permissions) {
        composable<Permissions> {
            PermissionsScreen {
                navController.navigate(Drawer)
            }
        }
        composable<Drawer> {
            DrawerScreen()
        }
    }
}
