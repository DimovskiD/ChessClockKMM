package com.deluxe1.chessclock.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(red = 244, green = 120, blue = 54),
    primaryVariant = Color(red = 225, green = 80, blue = 3),
    secondary = Color(red = 244, green = 120, blue = 54),
    background = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    error = Color(red = 244, green = 67, blue = 54),
    onBackground = Color.White,
)

private val LightColorPalette = lightColors(
    primary = Color(red = 244, green = 120, blue = 54),
    primaryVariant = Color(red = 225, green = 80, blue = 3),
    secondary = Color(red = 244, green = 120, blue = 54),
    background = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    error = Color(red = 244, green = 67, blue = 54),
    onBackground = Color.Black

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

object ChessClockTheme {
    val colors
        @Composable get() = if (isSystemInDarkTheme()) {
            DarkColorPalette
        } else {
            LightColorPalette
        }
}

@Composable
fun ChessClockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
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
