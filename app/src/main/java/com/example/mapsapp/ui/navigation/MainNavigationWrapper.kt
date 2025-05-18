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
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.PermissionsScreen
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController, Permissions) {

        //
        composable<Permissions> {
            PermissionsScreen {
                navController.navigate(Login)
            }
        }

        //Navegacion al Login
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

        //Navegacion al registro
        composable<Destinations.Register> {
            RegistreScreen(
                navigateToHome = {
                    navController.navigate(Drawer) {
                    }
                },
                navigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo<Login> { inclusive = true }
                    }
                }
            )
        }

        //Navegacion al Drawer
        composable<Drawer> {
            DrawerScreen(
                logout = {
                    navController.navigate(Login) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }
    }
}