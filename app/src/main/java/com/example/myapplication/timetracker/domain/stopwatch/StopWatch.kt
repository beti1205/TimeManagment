package com.example.myapplication.timetracker.domain.stopwatch

import com.example.myapplication.utils.formatToDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopWatch @Inject constructor(private val scope: CoroutineScope) {

    val state = MutableStateFlow(StopWatchState())

    private var counterJob: Job? = null

    fun start() {
        scope.launch {
            if (state.value.isActive) {
                counterJob?.cancel()
                state.update {
                    it.copy(
                        isActive = false,
                        isTimeTrackingFinished = true
                    )
                }
            } else {
                counterJob = launch { startCountingUp() }
                state.update {
                    it.copy(
                        isActive = true,
                        timeElapsed = 0L,
                        isTimeTrackingFinished = false
                    )
                }
            }
        }
    }


    fun reset() {
        if (state.value.isActive) {
            counterJob?.cancel()
        }
        state.update { it.copy(isActive = false, timeElapsed = 0L, isTimeTrackingFinished = false) }
    }

    private suspend fun startCountingUp() {
        var startTime = Instant.now()

        state.update { it.copy(startTime = startTime, date = startTime.formatToDate()) }

        while (true) {
            val timeAmount = Instant.now().epochSecond - startTime.epochSecond
            startTime = Instant.now()
            val timeElapsed = state.value.timeElapsed + timeAmount

            delay(1000)

            state.update {
                it.copy(
                    timeElapsed = timeElapsed,
                    endTime = startTime
                )
            }
        }
    }
}

fun Time.formatTime(): String {
    return String.format(
        "%02d:%02d:%02d",
        this.hours,
        this.minutes,
        this.seconds
    )
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