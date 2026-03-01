package com.example.pomodorotimer.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.data.preferences.ThemePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    val isDarkTheme: StateFlow<Boolean> = ThemePreferences
        .isDarkTheme(context)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            true // default dark theme
        )

    fun toggleTheme(dark: Boolean) {
        viewModelScope.launch {
            ThemePreferences.saveDarkTheme(context, dark)
        }
    }
}