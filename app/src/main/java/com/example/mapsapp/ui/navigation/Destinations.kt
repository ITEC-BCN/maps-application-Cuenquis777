package com.example.mapsapp.ui.navigation

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    object Login : Destinations() //Pantalla de login

    @Serializable
    object Register : Destinations() //Pantalla de registro

    @Serializable
    object Home : Destinations() //Pantalla de inicio

    @Serializable
    object Permissions : Destinations() //Pantalla de permisos

    @Serializable
    object Drawer : Destinations() //Pantalla de drawer

    @Serializable
    object Map : Destinations() // Mapa principal

    @Serializable
    object List : Destinations() //Lista de puntos guardados

    @Serializable
    data class MarkerCreation(val latitud: Double, val longitud : Double) : Destinations() //Pantalla de creación de un marcador

    @Serializable
    data class MarkerDetails(val id: Int) : Destinations() //Pantalla de detalles de un marcador



}
