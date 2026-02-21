package com.example.pomodorotimer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pomodorotimer.ui.screen.TimerScreen
import com.example.pomodorotimer.ui.screen.HistoryScreen
import com.example.pomodorotimer.ui.screen.SettingsScreen

sealed class Screen(val route: String) {
    object Timer : Screen("timer")
    object History : Screen("history")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Timer.route) {
        composable(Screen.Timer.route) {
            TimerScreen(
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}