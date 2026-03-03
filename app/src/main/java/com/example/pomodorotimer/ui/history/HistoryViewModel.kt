package com.example.pomodorotimer.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.data.local.DatabaseProvider
import com.example.pomodorotimer.data.local.SessionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    fun clearHistory() {
        viewModelScope.launch {
            dao.clearAll()
        }
    }

    fun deleteSession(session: SessionEntity) {
        viewModelScope.launch {
            dao.deleteSession(session)
        }
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