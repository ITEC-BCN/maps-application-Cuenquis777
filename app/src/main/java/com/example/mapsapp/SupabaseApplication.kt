package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.SupabaseAuthentication

class SupabaseApplication: Application() {
    companion object {
        lateinit var auth: SupabaseAuthentication
    }
    override fun onCreate() {
        super.onCreate()
        auth = SupabaseAuthentication()
    }
}
