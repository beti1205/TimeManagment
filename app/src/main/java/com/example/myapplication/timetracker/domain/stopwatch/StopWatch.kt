package com.example.myapplication.timetracker.domain.stopwatch

import com.example.myapplication.utils.formatToLongDate
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

    fun toggleTimer() {
        if (state.value.isActive) {
            stop()
        } else {
            start()
        }
    }

    fun start() {
        counterJob = scope.launch { startCountingUp() }
        state.update {
            it.copy(
                isActive = true,
                timeElapsed = 0L
            )
        }
    }

    fun stop() {
        counterJob?.cancel()
        state.update {
            it.copy(
                isActive = false
            )
        }
    }

    fun reset() {
        if (state.value.isActive) {
            counterJob?.cancel()
        }
        state.update {
            it.copy(
                isActive = false,
                timeElapsed = 0L,
                startTime = null,
                endTime = null
            )
        }
    }

    private suspend fun startCountingUp() {
        var startTime = Instant.now()

        state.update {
            it.copy(
                startTime = startTime,
                date = startTime.formatToLongDate(),
                endTime = startTime
            )
        }

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
