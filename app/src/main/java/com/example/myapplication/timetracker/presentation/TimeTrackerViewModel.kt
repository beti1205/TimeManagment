package com.example.myapplication.timetracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import com.example.myapplication.timetracker.domain.usecases.GetAllWorkingSubjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TimeTrackerViewModel @Inject constructor(
    private val timeTracker: TimeTracker,
    getAllWorkingSubjectsUseCase: GetAllWorkingSubjectsUseCase
) : ViewModel() {

    private val timeTrackerState = timeTracker.state

    private val isSubjectErrorOccurred: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val subjects: StateFlow<List<String>> = getAllWorkingSubjectsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state: StateFlow<TimeTrackerScreenState> = combine(
        timeTrackerState,
        subjects,
        isSubjectErrorOccurred
    ) { timeTrackerState, subjects, isSubjectError ->
        val filteredSubjects = subjects
            .distinct()
            .filter { it.contains(timeTrackerState.workingSubject, true) }

        TimeTrackerScreenState(
            isActive = timeTrackerState.isActive,
            timeElapsed = timeTrackerState.timeElapsed,
            startTime = timeTrackerState.startTime,
            endTime = timeTrackerState.endTime,
            workingSubject = timeTrackerState.workingSubject,
            isTimeTrackingFinished = timeTrackerState.isTimeTrackingFinished,
            isSubjectErrorOccurred = isSubjectError,
            filteredSubjectList = filteredSubjects
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TimeTrackerScreenState())

    fun start() {
        timeTracker.start()
    }

    fun restart() {
        timeTracker.reset()
    }

    fun onWorkingSubjectChanged(workingSubject: String) {
        timeTracker.onWorkingSubjectChanged(workingSubject)
    }

    fun onSubjectErrorChanged(isSubjectErrorOccurred: Boolean) {
        this.isSubjectErrorOccurred.update { isSubjectErrorOccurred }
    }
}
