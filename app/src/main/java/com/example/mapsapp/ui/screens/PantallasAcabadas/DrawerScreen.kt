package com.example.mapsapp.ui.screens.PantallasAcabadas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destinations
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.ui.unit.dp
import com.example.mapsapp.ui.navigation.InternalNavigationWrapper
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.ViewModel
import kotlinx.coroutines.launch

enum class DrawerItem(
    val icon: ImageVector,
    val text: String,
    val route: Destinations
) {
    HOME(Icons.Default.LocationOn, "Map", Destinations.Map),
    List(Icons.AutoMirrored.Filled.List, "List", Destinations.List),
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    ModalNavigationDrawer(
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp)
            ) {
                DrawerItem.entries.forEachIndexed { index, drawerItem ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = drawerItem.icon,
                                contentDescription = drawerItem.text
                            )
                        },
                        label = { Text(text = drawerItem.text) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            selectedItemIndex = index
                            scope.launch { drawerState.close() }
                            navController.navigate(drawerItem.route)
                        }
                    )
                }
            }
        },
        drawerState = drawerState
    )
    {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Awesome App") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            InternalNavigationWrapper(
                navController, Modifier.padding(innerPadding)
            )
        }
    }
}


