package com.example.myapplication.timetracker.domain.stopwatch

import com.example.myapplication.utils.convertSecondsToTimeString
import com.example.myapplication.utils.formatToLongDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

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

            delay(100)

            state.update {
                it.copy(
                    timeElapsed = timeElapsed,
                    endTime = startTime
                )
            }
        }
    }

    fun adjustTime(minutes: Int) {
        if (state.value.endTime == null || isNotPossibleToSubtract(minutes)) return

        val adjustedEndTime = state.value.endTime?.plus(Duration.ofMinutes(minutes.toLong()))
        updateEndTime(adjustedEndTime)
    }

    private fun updateEndTime(newEndTime: Instant?) {
        state.update {
            it.copy(
                endTime = newEndTime
            )
        }
    }

    private fun isNotPossibleToSubtract(minutes: Int): Boolean {
        val seconds = abs(minutes) * 60

        if (minutes < 0 && state.value.timeElapsed < seconds) {
            return true
        } else {
            return false
        }
    }
}
