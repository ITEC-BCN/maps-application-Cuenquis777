package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySupabaseClient

class MyApp: Application() {
    companion object {
        lateinit var database: MySupabaseClient
    }
    override fun onCreate() {
        super.onCreate()
        database = MySupabaseClient(
            supabaseUrl = "https://luxphgkqoavsmerxhoka.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx1eHBoZ2txb2F2c21lcnhob2thIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU4Mjc5NzYsImV4cCI6MjA2MTQwMzk3Nn0.2LKW-sAyXzKoDBrEdYbFRaFqnJiUVZTA-BMNbxqzQJA"
        )
    }
}
