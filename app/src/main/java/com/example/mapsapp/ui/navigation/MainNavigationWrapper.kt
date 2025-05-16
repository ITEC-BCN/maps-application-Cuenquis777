package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destinations.Drawer
import com.example.mapsapp.ui.navigation.Destinations.Login
import com.example.mapsapp.ui.navigation.Destinations.Permissions
import com.example.mapsapp.ui.screens.Auth.LoginScreen
import com.example.mapsapp.ui.screens.Auth.RegistreScreen
import com.example.mapsapp.ui.screens.PantallasAcabadas.DrawerScreen
import com.example.mapsapp.ui.screens.PermissionsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController, Permissions) {
        composable<Permissions> {
            PermissionsScreen {
                navController.navigate(Login)
            }
        }

        composable<Login> {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(Drawer) {

                    }
                },
                navigateToRegister = {
                    navController.navigate(Destinations.Register) {

                    }
                }
            )
        }

        composable<Destinations.Register> {
            RegistreScreen(
                navigateToHome = {
                    navController.navigate(Drawer) {
                    }
                },
            )
        }

        composable<Drawer> {
            DrawerScreen()
        }
    }
}