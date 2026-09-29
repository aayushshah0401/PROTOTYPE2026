package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = SurfaceSubtle,
    onPrimaryContainer = NavyPrimary,
    secondary = TealAccent,
    onSecondary = Color.White,
    tertiary = SubtleGreen,
    background = AppBackground,
    onBackground = TextCharcoal,
    surface = SurfaceWhite,
    onSurface = TextCharcoal,
    surfaceVariant = SurfaceSubtle,
    onSurfaceVariant = TextMutedGray,
    outline = BorderLight,
    error = SubtleRed,
    onError = Color.White
)

@Composable
fun VoxSentinelTheme(
    darkTheme: Boolean = false, // Clean light professional theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
