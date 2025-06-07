package com.example.spellcastingcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.spellcastingcompanion.navigation.AppNavigation
import com.example.spellcastingcompanion.ui.theme.SpellcastingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // For modern edge-to-edge display
        setContent {
            SpellcastingApp()
        }
    }
}

@Composable
fun SpellcastingApp() {
    SpellcastingTheme {
        Surface { // Surface from Material 3, can take color from theme
            val navController = rememberNavController()
            AppNavigation(navController = navController)
        }
    }
}