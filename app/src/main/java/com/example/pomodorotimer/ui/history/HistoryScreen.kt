package com.example.pomodorotimer.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistoryScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "History Screen")
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}