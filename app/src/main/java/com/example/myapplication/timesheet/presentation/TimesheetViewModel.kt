package com.example.myapplication.timesheet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.timesheet.domain.usecases.DeleteTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.GetTimeTrackerIntervalsUseCase
import com.example.myapplication.timesheet.domain.usecases.TimeIntervalParameters
import com.example.myapplication.timesheet.domain.usecases.TimeValidator
import com.example.myapplication.timesheet.domain.usecases.UpdateTimeIntervalUseCase
import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import com.example.myapplication.utils.formatDateWithColon
import com.example.myapplication.utils.formatTimeWithColon
import com.example.myapplication.utils.formatToDateWithoutColons
import com.example.myapplication.utils.formatToInstant
import com.example.myapplication.utils.formatToInstantWithAdditionalDay
import com.example.myapplication.utils.formatToTimeWithoutColons
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
    private val timeValidator: TimeValidator,
    private val timeTracker: TimeTracker,
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
            startTime = interval.startTime!!.formatToTimeWithoutColons(),
            endTime = interval.endTime!!.formatToTimeWithoutColons(),
            date = interval.date.formatToDateWithoutColons()
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

    fun onDateChanged(date: String) {
        editIntervalDialogState.value = editIntervalDialogState.value?.copy(
            date = date
        )
    }

    fun onDismissEditDialog(){
        editIntervalDialogState.value = null
    }

    private fun onUpdateTimeSheet() = viewModelScope.launch {
        editIntervalDialogState.value?.let {
            val editedEndTimeSmaller = isEditedEndTimeSmaller(
                startTime = it.startTime,
                endTime = it.endTime
            )
            updateTimeIntervalUseCase(
                TimeIntervalParameters(
                id = it.id,
                subject = it.subject,
                startTime = formatToInstant(date = it.date, time = it.startTime),
                endTime = when {
                    editedEndTimeSmaller -> formatToInstantWithAdditionalDay(it.date, it.endTime)
                    else -> formatToInstant(it.date, it.endTime)
                },
                date = it.date
                )
            )
        }
    }

    fun onSaveClicked(){
        saveFormattedTime()
        onUpdateTimeSheet()
    }

    private fun saveFormattedTime(){
        editIntervalDialogState.value = editIntervalDialogState.value?.copy(
            startTime = editIntervalDialogState.value!!.startTime.formatTimeWithColon(),
            endTime = editIntervalDialogState.value!!.endTime.formatTimeWithColon(),
            date = editIntervalDialogState.value!!.date.formatDateWithColon()
        )
    }

    private fun isEditedEndTimeSmaller(startTime: String, endTime: String): Boolean{
        val result = startTime.compareTo(endTime)

        return when {
            result > 0 -> true
            result < 0 -> false
            else -> false
        }
    }

    fun start(workingSubject: String) {
        onWorkingSubjectChanged(workingSubject)
        timeTracker.start()
    }

    fun reset() {
        timeTracker.reset()
    }


    private fun onWorkingSubjectChanged(workingSubject: String) {
        timeTracker.onWorkingSubjectChanged(workingSubject)
    }
}