package com.example.mapsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: Int? = null,
    val name: String,
    val mark: Double
)
