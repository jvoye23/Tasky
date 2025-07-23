package com.jvoye.tasky.core.presentation.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = DarkModeBackground,
    onBackground = DarkModeOnBackground,
    surface = DarkModeSurface,
    surfaceVariant = DarkModeSurfaceHigher,
    surfaceContainerHigh = DarkModeSurfaceHigher,
    onSurface = DarkModeOnSurface,
    onSurfaceVariant = DarkModeOnSurfaceVariant,
    primary = DarkModePrimary,
    onPrimary = DarkModeOnPrimary,
    outline = DarkModeOutline,
    error = DarkModeError,
    secondary = BrandSecondary,
    tertiary = BrandTertiary
)

private val LightColorScheme = lightColorScheme(
    background = LightModeBackground,
    onBackground = LightModeOnBackground,
    surface = LightModeSurface,
    surfaceVariant = LightModeSurfaceHigher,
    surfaceContainerHigh = LightModeSurfaceHigher,
    onSurface = LightModeOnSurface,
    onSurfaceVariant = LightModeOnSurfaceVariant,
    primary = LightModePrimary,
    onPrimary = LightModeOnPrimary,
    outline = LightModeOutline,
    error = LightModeError,
    secondary = BrandSecondary,
    tertiary = BrandTertiary
)

val ColorScheme.surfaceHigher: Color
    @Composable
    get() = (if (isSystemInDarkTheme()) {
        DarkModeSurfaceHigher
    } else {
        LightModeSurfaceHigher
    })

val ColorScheme.success: Color
    @Composable
    get() = (if (isSystemInDarkTheme()) {
        DarkModeSuccess
    } else {
        LightModeSuccess
    })

val ColorScheme.link: Color
    @Composable
    get() = (if (isSystemInDarkTheme()) {
        DarkModeLink
    } else {
        LightModeLink
    })

val ColorScheme.supplementary: Color
    get() = BrandSupplementary




@Composable
fun TaskyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}