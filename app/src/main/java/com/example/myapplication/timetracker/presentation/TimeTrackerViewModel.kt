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

    private val selectedTimeAdjustment: MutableStateFlow<TimeAdjustment?> =
        MutableStateFlow(null)

    val state: StateFlow<TimeTrackerScreenState> = combine(
        timeTrackerState,
        subjects,
        isSubjectErrorOccurred,
        workingSubject,
        selectedTimeAdjustment
    ) { timeTrackerState, subjects, isSubjectError, workingSubject, timeAdjustment ->
        val filteredSubjects = getFilteredSubject(
            subjects = subjects,
            workingSubject = workingSubject,
            isActive = timeTrackerState.isActive
        )

        TimeTrackerScreenState(
            isActive = timeTrackerState.isActive,
            timeElapsed = timeTrackerState.timeElapsed,
            startTime = timeTrackerState.startTime,
            endTime = timeTrackerState.endTime,
            workingSubject = workingSubject,
            isSubjectErrorOccurred = isSubjectError,
            filteredSubjects = filteredSubjects,
            selectedTimeAdjustment = timeAdjustment
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

    fun setWorkingSubjectIfTimerHasBeenResumed() {
        val workingSubject = timeTrackerState.value.workingSubject

        if (workingSubject.isNotBlank()) {
            onWorkingSubjectChanged(workingSubject)
        }
    }

    private fun getFilteredSubject(
        subjects: List<String>,
        workingSubject: String,
        isActive: Boolean
    ): List<String> {
        val filteredSubjects = subjects
            .distinct()
            .filter { it.contains(workingSubject, true) }
            .filter { it != workingSubject }

        return if (workingSubject.isNotBlank() && !isActive) {
            filteredSubjects
        } else {
            emptyList()
        }
    }

    fun toggleTimer() {
        handleSelectedTimeAdjustment()

        timeTracker.toggleTimer()
    }

    private fun handleSelectedTimeAdjustment() {
        clearPreviouslySelectedTimeAdjustmentBeforeStarted()
        correctEndTimeWhenStopped()
    }

    private fun clearPreviouslySelectedTimeAdjustmentBeforeStarted() {
        val endTime = state.value.endTime
        val isActive = state.value.isActive

        if (!isActive && selectedTimeAdjustment.value != null && endTime != null) {
            clearSelectedTimeAdjustment()
        }
    }

    fun reset() {
        if (selectedTimeAdjustment.value != null) {
            clearSelectedTimeAdjustment()
        }
        timeTracker.reset()
    }

    private fun onTimeTrackerWorkingSubjectChanged(workingSubject: String) {
        timeTracker.onWorkingSubjectChanged(workingSubject)
    }

    fun onWorkingSubjectChanged(workingSubject: String) {
        updateWorkingSubject(workingSubject)
        validateSubject(workingSubject)
    }

    private fun updateWorkingSubject(workingSubject: String) {
        this.workingSubject.update { workingSubject }
    }

    private fun validateSubject(workingSubject: String) {
        if (workingSubject.isNotBlank()) {
            onSubjectErrorChanged(false)
        } else {
            onSubjectErrorChanged(true)
        }
    }

    fun onSubjectErrorChanged(isSubjectErrorOccurred: Boolean) {
        this.isSubjectErrorOccurred.update { isSubjectErrorOccurred }
    }

    fun onTypeSelected(type: TimeAdjustment) {
        if (type == selectedTimeAdjustment.value) {
            clearSelectedTimeAdjustment()
        } else {
            this.selectedTimeAdjustment.update { type }
        }
    }

    private fun clearSelectedTimeAdjustment() {
        this.selectedTimeAdjustment.update { null }
    }

    private fun correctEndTimeWhenStopped() {
        if (selectedTimeAdjustment.value == null || !state.value.isActive) return

        val amountToChange = selectedTimeAdjustment.value!!.amount
        timeTracker.adjustTime(amountToChange)
    }
}

enum class TimeAdjustment(val amount: Int) {
    MINUS_30(-30),
    MINUS_15(-15),
    MINUS_5(-5),
    PLUS_5(5),
    PLUS_15(15),
    PLUS_30(30)
}