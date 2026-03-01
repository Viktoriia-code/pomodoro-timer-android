package com.example.pomodorotimer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.pomodorotimer.ui.settings.SettingsViewModel

private val DarkColorScheme = darkColorScheme(
    primary      = CyanBlue,      // buttons, accents
    background   = BgDark,        // screen backgrounds
    surface      = SurfaceDark,   // card backgrounds
    onPrimary    = Color.White,
    onBackground = TextPrimary,
    onSurface    = TextPrimary,
    primaryContainer = Color(0xFF0288D1),
    onPrimaryContainer = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary      = CyanBlue,
    background   = Color(0xFFFFFBFE),
    surface      = Color(0xFFFFFFFF),
    onPrimary    = Color(0xFF388E3C),
    onBackground = Color(0xFF1C1B1F),
    onSurface    = Color(0xFF1C1B1F),
    primaryContainer = Color(0xFF388E3C),
    onPrimaryContainer = Color.White,
)

@Composable
fun PomodoroTheme(
    settingsViewModel: SettingsViewModel,
    content: @Composable () -> Unit
) {
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()

    MaterialTheme(
        colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}