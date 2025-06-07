package com.example.spellcastingcompanion

import android.app.Application
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage


class SpellcastingApplication : Application() {

    companion object {
        lateinit var supabaseClient: SupabaseClient
            private set // Make setter private to control initialization
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Supabase client
        // IMPORTANT: Replace with your actual URL and Anon Key from your Supabase dashboard screenshot
        val supabaseUrl = "https://jxpningafearufautyzn.supabase.co" // <-- REPLACE THIS
        val supabaseAnonKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imp4cG5pbmdhZmVhcnVmYXV0eXpuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgzNjQzODQsImV4cCI6MjA2Mzk0MDM4NH0.hUeqcKU84nYJ34MJjQYjZvxctKyPt3yALA31RaJrKkw"   // <-- REPLACE THIS

        supabaseClient = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseAnonKey
        ) {
            install(Auth) {
                // autoLoadFromStorage = true
                // autoSaveToStorage = true
            }
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }
}