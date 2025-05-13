package com.example.mapsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Marker(
    val id: Int = 0,
    val name: String,
    val mark: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String?
)
