package com.example.pomodorotimer.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

object ThemePreferences {
    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_enabled")

    suspend fun saveDarkTheme(context: Context, isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_THEME_KEY] = isDark
        }
    }

    fun isDarkTheme(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[DARK_THEME_KEY] ?: false // default light theme
        }
}