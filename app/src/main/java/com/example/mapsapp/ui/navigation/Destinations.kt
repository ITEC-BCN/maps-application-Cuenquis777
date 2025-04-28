package com.example.mapsapp.ui.navigation

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

sealed class Destinations {

    @Serializable
    object Permissions : Destinations()

    @Serializable
    object Drawer : Destinations()

    @Serializable
    object Map : Destinations()

    @Serializable
    object List : Destinations()

/*
    @Serializable
    data class MarkerCreation(val coordenades: LatLng)

    @Serializable
    data class MarkerDetails(val id: Int) : Destinations()

 */



}
