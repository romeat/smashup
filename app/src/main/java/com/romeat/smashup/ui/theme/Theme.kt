package com.romeat.smashup.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val AppBlack = Color(0xFF000000)
val AppDarkGrey = Color(0xFF111111)
val AppGrey = Color(0xFF262626)
val AppLightGrey = Color(0xFFEBEBEB) //Color(0xFFB9B9B9)
val AppMainColor = Color(0xFF2A1947)
val AppAltColor = Color(0xFFA887F8)
val AppGreenColor = Color(0xFF4CAF50)
val AppRedColor = Color(0xFFFF4545)

private val DarkColorPalette = darkColors(
    primary = AppMainColor,
    primaryVariant = AppAltColor,
    secondary = Teal200,
    background = AppBlack,
    surface = AppDarkGrey,
    onPrimary = Color.White,
    onSecondary = AppBlack,
    onBackground = AppLightGrey,
    onSurface = AppLightGrey,
    error = AppRedColor,
    onError = Color.White
)

// TODO light theme is not supported by design
private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun SmashupTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        DarkColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}