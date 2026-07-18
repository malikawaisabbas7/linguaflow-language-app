package com.linguaflow.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = DeepPurple,
    secondary = TealAccent,
    tertiary = SunshineYellow,
    background = BackgroundLight,
    surface = CardLight,
    error = ErrorRed,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight
)

private val DarkColors = darkColorScheme(
    primary = DeepPurpleLight,
    secondary = TealAccent,
    tertiary = SunshineYellow,
    background = BackgroundDark,
    surface = CardDark,
    error = ErrorRed,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

@Composable
fun LinguaFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = LinguaFlowTypography,
        shapes = LinguaFlowShapes,
        content = content
    )
}
