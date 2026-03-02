package com.example.pomodorotimer.ui.timer

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pomodorotimer.data.local.DatabaseProvider
import com.example.pomodorotimer.data.local.SessionEntity
import com.example.pomodorotimer.data.preferences.ThemePreferences
import kotlinx.coroutines.flow.combine

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DatabaseProvider.getDatabase(application).sessionDao()

    val sessionDuration: StateFlow<Int> =
        ThemePreferences
            .getSessionDuration(getApplication())
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                25
            )

    private val _timeLeft = MutableStateFlow(sessionDuration.value * 60L)
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private var timerJob: Job? = null
    private var currentSessionId: Long? = null

    val progress: StateFlow<Float> =
        combine(timeLeft, sessionDuration) { left, duration ->
            left.toFloat() / (duration * 60f)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            1f
        )

    init {
        viewModelScope.launch {
            sessionDuration.collect { duration ->
                if (!_isRunning.value) {
                    _timeLeft.value = duration * 60L
                }
            }
        }
    }

    fun startPauseTimer() {
        if (_isRunning.value) { pauseTimer() } else { startTimer() }
    }

    private fun startTimer() {
        if (_timeLeft.value == sessionDuration.value * 60L) {
            viewModelScope.launch {
                currentSessionId = dao.insertSession(SessionEntity(durationMinutes = sessionDuration.value))
            }
        }

        _isRunning.value = true

        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }

            _isRunning.value = false
            completeCurrentSession()
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _isRunning.value = false
    }


    fun resetTimer() {
        timerJob?.cancel()
        _timeLeft.value = sessionDuration.value * 60L
        _isRunning.value = false
        currentSessionId = null
    }

    private fun completeCurrentSession() {
        val id = currentSessionId ?: return
        viewModelScope.launch {
            dao.completeSession(id, System.currentTimeMillis())
        }
        currentSessionId = null
    }

    fun finishSession() {
        timerJob?.cancel()
        _isRunning.value = false
        _timeLeft.value = 0L
        completeCurrentSession()
    }

    fun formatTime(seconds: Long): String {
        val minutes = seconds / 60
        val sec = seconds % 60
        return "%02d:%02d".format(minutes, sec)
    }
}