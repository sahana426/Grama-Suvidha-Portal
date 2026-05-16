package com.gramasuvidha.portal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1B5E20),
    secondary = Color(0xFF1976D2),
    tertiary = Color(0xFFFFB300),
    background = Color(0xFFF6FAF7),
    surface = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    secondaryContainer = Color(0xFFBBDEFB)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF81C784),
    secondary = Color(0xFF90CAF9),
    tertiary = Color(0xFFFFD54F),
    background = Color(0xFF101A12),
    surface = Color(0xFF18231A),
    primaryContainer = Color(0xFF255D2A),
    secondaryContainer = Color(0xFF17486F)
)

@Composable
fun GramaSuvidhaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colorScheme, typography = MaterialTheme.typography, content = content)
}
