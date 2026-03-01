package com.example.pomodorotimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.pomodorotimer.ui.navigation.AppNavigation
import com.example.pomodorotimer.ui.settings.SettingsViewModel
import com.example.pomodorotimer.ui.theme.PomodoroTheme

class MainActivity : ComponentActivity() {
    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTheme(settingsViewModel) {
                AppNavigation()
            }
        }
    }
}