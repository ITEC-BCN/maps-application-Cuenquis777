package com.example.mapsapp

import androidx.lifecycle.ViewModelProvider
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelMap.ViewModel


class AuthViewModelFactory(private val shredPreferences: SharedPreferencesHelper): ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModel::class.java)) {
            return ViewModel(shredPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
