package com.example.mapsapp.ui.navigation

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

sealed class Destinations {

    @Serializable
    object Permissions : Destinations()

    @Serializable
    object Drawer : Destinations()

    @Serializable
    object Map : Destinations() // Mapa principal

    @Serializable
    object List : Destinations() //Lista de puntos guardados

    @Serializable
    object Camera : Destinations() //Pantalla de la cámara

    @Serializable
    data class Detail (val id: Int)

    @Serializable
    data class MarkerCreation(val latitud: Double, val longitud : Double) : Destinations() //Pantalla de creación de un marcador

    @Serializable
    data class MarkerDetails(val id: Int) : Destinations() //Pantalla de detalles de un marcador





}
