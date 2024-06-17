package com.example.myapplication.timetracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import com.example.myapplication.timetracker.domain.usecases.GetAllWorkingSubjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TimeTrackerViewModel @Inject constructor(
    private val timeTracker: TimeTracker,
    getAllWorkingSubjectsUseCase: GetAllWorkingSubjectsUseCase
) : ViewModel() {

    private val timeTrackerState = timeTracker.state

    private val isSubjectErrorOccurred: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val workingSubject: MutableStateFlow<String> = MutableStateFlow("")

    private val subjects: StateFlow<List<String>> = getAllWorkingSubjectsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state: StateFlow<TimeTrackerScreenState> = combine(
        timeTrackerState,
        subjects,
        isSubjectErrorOccurred,
        workingSubject
    ) { timeTrackerState, subjects, isSubjectError, workingSubject ->
        val filteredSubjects = subjects
            .distinct()
            .filter { it.contains(workingSubject, true) }

        TimeTrackerScreenState(
            isActive = timeTrackerState.isActive,
            timeElapsed = timeTrackerState.timeElapsed,
            startTime = timeTrackerState.startTime,
            endTime = timeTrackerState.endTime,
            workingSubject = workingSubject,
            isSubjectErrorOccurred = isSubjectError,
            filteredSubjectList = filteredSubjects
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TimeTrackerScreenState())

    init {
        viewModelScope.launch {
            workingSubject
                .debounce(500)
                .collectLatest { workingSubject ->
                    onTimeTrackerWorkingSubjectChanged(workingSubject)
                }
        }
    }

    fun toggleTimer() {
        timeTracker.toggleTimer()
    }

    fun reset() {
        timeTracker.reset()
    }

    private fun onTimeTrackerWorkingSubjectChanged(workingSubject: String) {
        timeTracker.onWorkingSubjectChanged(workingSubject)
    }

    fun onWorkingSubjectChanged(workingSubject: String) {
        this.workingSubject.update { workingSubject }
    }

    fun onSubjectErrorChanged(isSubjectErrorOccurred: Boolean) {
        this.isSubjectErrorOccurred.update { isSubjectErrorOccurred }
    }
}
