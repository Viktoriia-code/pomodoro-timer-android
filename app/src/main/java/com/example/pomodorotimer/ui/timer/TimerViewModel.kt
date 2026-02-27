package com.example.pomodorotimer.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pomodorotimer.data.local.DatabaseProvider
import com.example.pomodorotimer.data.local.SessionEntity

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DatabaseProvider
        .getDatabase(application)
        .sessionDao()

    private val sessionDuration = 25
    private val focusDuration = sessionDuration * 60L // 25 minutes in seconds

    private val _timeLeft = MutableStateFlow(focusDuration)
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private var timerJob: Job? = null

    val progress: StateFlow<Float> =
        _timeLeft.map {

            it.toFloat() / focusDuration.toFloat()

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            1f
        )


    fun startPauseTimer() {

        if (_isRunning.value) {
            pauseTimer()
        } else {
            startTimer()
        }
    }


    private fun startTimer() {
        if (_timeLeft.value == focusDuration) {
            viewModelScope.launch {
                dao.insertSession(
                    SessionEntity(durationMinutes = sessionDuration)
                )
            }
        }

        _isRunning.value = true

        timerJob = viewModelScope.launch {

            while (_timeLeft.value > 0) {

                delay(1000)

                _timeLeft.value -= 1
            }

            _isRunning.value = false
        }
    }


    private fun pauseTimer() {

        timerJob?.cancel()

        _isRunning.value = false
    }


    fun resetTimer() {

        timerJob?.cancel()

        _timeLeft.value = focusDuration

        _isRunning.value = false
    }

    fun finishSession() {

        timerJob?.cancel()
        timerJob = null

        _isRunning.value = false

        _timeLeft.value = 0L
    }

    fun formatTime(seconds: Long): String {

        val minutes = seconds / 60
        val sec = seconds % 60

        return "%02d:%02d".format(minutes, sec)
    }
}