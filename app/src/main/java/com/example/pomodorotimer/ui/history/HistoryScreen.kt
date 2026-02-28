package com.example.pomodorotimer.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.data.local.SessionEntity
import com.example.pomodorotimer.utils.toTimeString
import com.example.pomodorotimer.utils.toFullDateString

@ExperimentalMaterial3Api
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    viewModel: HistoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val groupedSessions by viewModel.groupedSessions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("History")
                },

                actions = {
                    IconButton( onClick = onBack ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (groupedSessions.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {

                Text("No sessions yet")
            }

        } else {

            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                groupedSessions.forEach { (date, sessions) ->

                    item {

                        Text(
                            text = date.toFullDateString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    items(sessions) { session ->

                        SessionItem(session)
                    }
                }
            }
        }
    }
}

@Composable
fun SessionItem(session: SessionEntity) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        val isCompleted = session.completedAt != null

        Row(
            modifier = Modifier
                .padding(16.dp, 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = if (isCompleted)
                    Icons.Default.CheckCircle
                else
                    Icons.Default.Schedule,
                contentDescription = null,
                tint = if (isCompleted)
                    Color(0xFF4CAF50)
                else
                    MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.width(18.dp))

            Column {

                Text(
                    text = "Focus Session (${session.durationMinutes} min)",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Started at: ${session.startedAt.toTimeString()}"
                )

                session.completedAt?.let { completedAt ->
                    Text(
                        text = "Completed at: ${completedAt.toTimeString()}"
                    )
                }
            }
        }
    }
}