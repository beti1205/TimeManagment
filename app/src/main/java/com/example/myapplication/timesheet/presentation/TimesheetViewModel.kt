package com.example.myapplication.timesheet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.timesheet.domain.usecases.DeleteTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.GetTimeTrackerIntervalsUseCase
import com.example.myapplication.timesheet.domain.usecases.TimeValidator
import com.example.myapplication.timesheet.domain.usecases.UpdateTimeIntervalUseCase
import com.example.myapplication.utils.formatToLongTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimesheetViewModel @Inject constructor(
    getTimeTrackerIntervalsUseCase: GetTimeTrackerIntervalsUseCase,
    private val deleteTimeIntervalUseCase: DeleteTimeIntervalUseCase,
    private val updateTimeIntervalUseCase: UpdateTimeIntervalUseCase,
    private val timeValidator: TimeValidator
) : ViewModel() {

    private val editIntervalDialogState: MutableStateFlow<EditIntervalDialogState?> =
        MutableStateFlow(null)

    val state: StateFlow<TimesheetScreenState> = combine(
        getTimeTrackerIntervalsUseCase(),
        editIntervalDialogState
    ) { daySections, editIntervalDialogState ->
        TimesheetScreenState(
            daySections = daySections,
            editIntervalDialogState = editIntervalDialogState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500L),
        initialValue = TimesheetScreenState()
    )

    fun deleteTimeInterval(id: Int) = viewModelScope.launch {
        deleteTimeIntervalUseCase(id)
    }

    fun onEditClicked(id: Int) {
        val interval = state.value.daySections.flatMap { it.timeIntervals }.first { interval ->
            interval.id == id
        }

        editIntervalDialogState.value = EditIntervalDialogState(
            id = id,
            subject = interval.workingSubject,
            startTime = interval.startTime!!.formatToLongTime(),
            endTime = interval.endTime!!.formatToLongTime(),
            date = interval.date
        )
    }

    fun onSubjectChanged(subject: String) {
        editIntervalDialogState.value = editIntervalDialogState.value?.copy(
            subject = subject
        )
    }

    fun onStartTimeChanged(startTime: String) {
        editIntervalDialogState.value = editIntervalDialogState.value?.copy(
            startTime = startTime,
            isWrongStartTimeError = !timeValidator(startTime)
        )
    }

    fun onEndTimeChanged(endTime: String) {
        editIntervalDialogState.value = editIntervalDialogState.value?.copy(
            endTime = endTime,
            isWrongEndTimeError = !timeValidator(endTime)
        )
    }

    fun onDismissEditDialog(){
        editIntervalDialogState.value = null
    }

    fun onSaveClicked() = viewModelScope.launch {
        editIntervalDialogState.value?.let {
            updateTimeIntervalUseCase(
                id = it.id,
                subject = it.subject,
                startTime = it.startTime,
                endTime = it.endTime,
                date = it.date
            )
        }
    }
}