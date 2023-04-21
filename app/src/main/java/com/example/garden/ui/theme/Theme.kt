package com.example.garden.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple80,
    onPrimary = Purple20,
    secondary = Orange80,
    onSecondary = Orange20,
    error = Red80,
    onError = Red20,
    background = DarkPurpleGray10,
    onBackground = DarkPurpleGray90,
    surface = DarkPurpleGray10,
    onSurface = DarkPurpleGray90,
)

private val LightColorPalette = lightColors(
    primary = Green40,
    onPrimary = Color.White,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    error = Red40,
    onError = Color.White,
    background = GreenGray90,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray99,
    onSurface = DarkGreenGray10,
)

@Composable
fun GardenTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}