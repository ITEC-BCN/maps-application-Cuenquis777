package com.example.mapsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mapsapp.ui.navigation.MainNavigationWrapper
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.theme.MapsAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            MapsAppTheme {
                MapsScreen()
            }
        }
    }
}


