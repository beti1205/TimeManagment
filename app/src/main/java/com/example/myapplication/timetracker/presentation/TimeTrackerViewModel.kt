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

    private val selectedChangesType: MutableStateFlow<TimeAmountChangesType?> =
        MutableStateFlow(null)

    val state: StateFlow<TimeTrackerScreenState> = combine(
        timeTrackerState,
        subjects,
        isSubjectErrorOccurred,
        workingSubject,
        selectedChangesType
    ) { timeTrackerState, subjects, isSubjectError, workingSubject, type ->
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
            selectedChangesType = type
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
        handleSelectedChangesType()

        timeTracker.toggleTimer()
    }

    private fun handleSelectedChangesType() {
        clearPreviouslySelectedTypeBeforeStarted()
        correctEndTimeWhenStopped()
    }

    private fun clearPreviouslySelectedTypeBeforeStarted() {
        val endTime = state.value.endTime
        val isActive = state.value.isActive

        if (!isActive && selectedChangesType.value != null && endTime != null) {
            clearSelectedType()
        }
    }

    fun reset() {
        if (selectedChangesType.value != null) {
            clearSelectedType()
        }
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

    fun onTypeSelected(type: TimeAmountChangesType) {
        if (type == selectedChangesType.value) {
            clearSelectedType()
        } else {
            this.selectedChangesType.update { type }
        }
    }

    private fun clearSelectedType() {
        this.selectedChangesType.update { null }
    }

    private fun correctEndTimeWhenStopped() {
        if (selectedChangesType.value == null || !state.value.isActive) return

        val amountToChange = selectedChangesType.value!!.amount
        timeTracker.adjustTime(amountToChange)
    }
}

enum class TimeAmountChangesType(val amount: Int) {
    REDUCED_30(-30),
    REDUCED_15(-15),
    REDUCED_5(-5),
    INCREASED_5(5),
    INCREASED_15(15),
    INCREASED_30(30)
}