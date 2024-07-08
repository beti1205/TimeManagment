package com.example.myapplication.timetracker.domain.timetracker

import com.example.myapplication.data.TimeTrackerEntity
import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.timetracker.domain.stopwatch.StopWatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

data class TimeTrackerState(
    val isActive: Boolean = false,
    val timeElapsed: Long = 0L,
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val workingSubject: String = "",
    val date: String = ""
)

@Singleton
class TimeTracker @Inject constructor(
    private val stopWatch: StopWatch,
    private val repository: TimeTrackerRepository,
    private val scope: CoroutineScope
) {

    private val workingSubject: MutableStateFlow<String> = MutableStateFlow("")

    val state: StateFlow<TimeTrackerState> = stopWatch.state
        .combine(workingSubject) { stopWatchState, subject ->
            stopWatchState.run {
                TimeTrackerState(
                    isActive = isActive,
                    timeElapsed = timeElapsed,
                    startTime = startTime,
                    endTime = endTime,
                    workingSubject = subject,
                    date = date
                )
            }
        }.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = TimeTrackerState()
        )

    fun toggleTimer() = scope.launch {
        if (state.value.isActive) {
            saveInterval()
        }
        stopWatch.toggleTimer()
    }

    private suspend fun saveInterval() {
        val state = stopWatch.state.value
        repository.insert(
            TimeTrackerEntity(
                startTime = state.startTime,
                endTime = state.endTime,
                workingSubject = workingSubject.value,
                date = state.date
            )
        )
    }

    fun reset() = stopWatch.reset()

    fun onWorkingSubjectChanged(workingSubject: String) {
        this.workingSubject.update { workingSubject }
    }

    fun startOrResetTimer(workingSubject: String) {
        scope.launch {
            if (state.value.isActive) {
                saveInterval()
                stopWatch.stop()
            }
            onWorkingSubjectChanged(workingSubject)
            stopWatch.start()
        }
    }

    fun adjustTime(minutes: Int) {
        stopWatch.adjustTime(minutes)
    }
}
