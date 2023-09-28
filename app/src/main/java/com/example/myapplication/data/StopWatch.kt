package com.example.myapplication.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

class StopWatch(private val scope: CoroutineScope) {

    val state = MutableStateFlow(StopWatchState())

    private var counterJob: Job? = null

    fun start() {
        scope.launch {
            if (state.value.isActive) {
                counterJob?.cancel()
                state.update { it.copy(isActive = false) }
            } else {
                counterJob = launch { startCountingUp() }
                state.update { it.copy(isActive = true, timeAmount = "00:00:00") }
            }
        }
    }


    fun reset() {
        if (state.value.isActive) {
            counterJob?.cancel()
        }
        state.update { it.copy(isActive = false, timeAmount = "00:00:00") }
    }

    private suspend fun startCountingUp() {
        val startTime = Instant.now()

        while (true) {
            val timeAmount = Instant.now().epochSecond - startTime.epochSecond
            val time = timeAmount.toTime()

            delay(100)

            state.update {
                it.copy(
                    timeAmount = String.format(
                        "%02d:%02d:%02d",
                        time.hours,
                        time.minutes,
                        time.seconds
                    )
                )
            }
        }
    }
}

fun Long.toTime(): Time {
    val seconds = this % 60
    val minutes = (this % 3600) / 60
    val hours = this / 3600

    return Time(hours, minutes, seconds)
}

data class Time(
    val hours: Long,
    val minutes: Long,
    val seconds: Long
)