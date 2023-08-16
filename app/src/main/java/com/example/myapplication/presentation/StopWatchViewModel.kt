package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

class StopWatchViewModel : ViewModel() {

    var state = MutableStateFlow(StopWatchState())
        private set

    private var counterJob: Job? = null

    fun start() {
        viewModelScope.launch {
            if (state.value.isActive) {
                counterJob?.cancel()
                state.update { it.copy(isActive = false, timeAmount = "00:00:00") }
            } else {
                counterJob = launch { startCountingUp() }
                state.update { it.copy(isActive = true) }
            }

        }
    }


    fun restart() {
        if (!state.value.isActive) {
            return
        } else {
            counterJob?.cancel()
            state.update { it.copy(isActive = false, timeAmount = "00:00:00") }
        }
    }

    suspend fun startCountingUp() {
        val startTime = Instant.now()

        while (true) {
            val timeAmount = Instant.now().epochSecond - startTime.epochSecond
            val seconds = timeAmount % 60
            val minutes = timeAmount / 60
            val hours = timeAmount / 360

            delay(100)

            state.update {
                it.copy(
                    timeAmount = String.format(
                        "%02d:%02d:%02d",
                        hours,
                        minutes,
                        seconds
                    )
                )
            }
        }
    }
}