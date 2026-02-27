package com.example.pomodorotimer.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.data.local.DatabaseProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.ZoneId

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DatabaseProvider
        .getDatabase(application)
        .sessionDao()

    val sessions = dao
        .getAllSessions()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    suspend fun clearHistory() {
        dao.clearAll()
    }

    val groupedSessions =
        dao.getAllSessions()
            .map { sessions ->

                sessions.groupBy { session ->

                    val instant = Instant.ofEpochMilli(session.startedAt)
                    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                    localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                }

            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyMap()
            )
}