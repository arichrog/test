package com.example.spellcastingcompanion.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.content.ContextCompat

// Define your light color scheme using the colors from Color.kt
private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor, // e.g., WoodDefault
    secondary = SecondaryColor, // e.g., ParchmentDark
    tertiary = TertiaryColor, // e.g., InkDefault
    background = BackgroundColor, // e.g., ParchmentLight
    surface = SurfaceColor, // e.g., ParchmentDefault
    onPrimary = OnPrimaryColor,
    onSecondary = OnSecondaryColor,
    onTertiary = OnTertiaryColor,
    onBackground = OnBackgroundColor,
    onSurface = OnSurfaceColor,
    // You can customize other colors like error, surfaceVariant, etc.
    // error = MagicRed (example)
)

// Define a dark color scheme if you want to support dark mode.
// You might need to create darker versions of your parchment/wood/ink colors.
// For simplicity, we'll use similar colors but you should adjust these.
private val DarkColorScheme = darkColorScheme(
    primary = WoodLight, // Lighter wood for dark theme
    secondary = ParchmentDarker,
    tertiary = InkLight,
    background = InkBlack,
    surface = InkDark,
    onPrimary = InkDark,
    onSecondary = ParchmentLight,
    onTertiary = ParchmentLight,
    onBackground = ParchmentLight,
    onSurface = ParchmentLight,
    // error = MagicRed.copy(alpha = 0.7f) // Example
)

@Composable
fun SpellcastingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb() // Or your desired status bar color
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            // For navigation bar color (optional)
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // From Type.kt
        // You can also define shapes here if needed
        content = content
    )
}