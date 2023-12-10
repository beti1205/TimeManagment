package com.example.myapplication.timetracker.domain.timetracker

import com.example.myapplication.data.TimeTrackerEntity
import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.timetracker.domain.stopwatch.StopWatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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
    val isTimeTrackingFinished: Boolean = false,
    val date: String = ""
)

@Singleton
class TimeTracker @Inject constructor(
    private val stopWatch: StopWatch,
    private val repository: TimeTrackerRepository,
    scope: CoroutineScope
) {

    private val workingSubject: MutableStateFlow<String> = MutableStateFlow("")

    val state: Flow<TimeTrackerState> = stopWatch.state
        .combine(workingSubject) { stopWatchState, subject ->
            stopWatchState.run {
                TimeTrackerState(
                    isActive = isActive,
                    timeElapsed = timeElapsed,
                    startTime = startTime,
                    endTime = endTime,
                    isTimeTrackingFinished = isTimeTrackingFinished,
                    workingSubject = subject,
                    date = date
                )
            }
        }

    init {
        scope.launch {
            stopWatch.state
                .map { it.isTimeTrackingFinished }
                .distinctUntilChanged()
                .collectLatest { isTimeTrackingFinished ->
                    if (isTimeTrackingFinished) {
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
                }
        }
    }

    fun start() = stopWatch.start()

    fun reset() = stopWatch.reset()

    fun onWorkingSubjectChanged(workingSubject: String) {
        this.workingSubject.update { workingSubject }
    }

}