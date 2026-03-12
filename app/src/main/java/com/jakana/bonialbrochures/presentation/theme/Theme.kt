package com.jakana.bonialbrochures.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BonialColorScheme = lightColorScheme(
    primary = BonialPrimary,
    onPrimary = Color.White,
    primaryContainer = BonialPrimaryLight,
    secondary = BonialSecondary,
    onSecondary = Color.White,
    background = BonialBackground,
    surface = BonialSurface,
    onBackground = BonialTextDark,
    onSurface = BonialTextDark,
    onSurfaceVariant = BonialTextMuted
)

@Composable
fun BonialBrochuresTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BonialColorScheme,
        typography = Typography,
        content = content
    )
}