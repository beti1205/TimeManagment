package com.example.myapplication.timesheet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.timesheet.domain.usecases.AddTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.DateValidator
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
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TimesheetViewModel @Inject constructor(
    getTimeTrackerIntervalsUseCase: GetTimeTrackerIntervalsUseCase,
    private val deleteTimeIntervalUseCase: DeleteTimeIntervalUseCase,
    private val updateTimeIntervalUseCase: UpdateTimeIntervalUseCase,
    private val addTimeIntervalUseCase: AddTimeIntervalUseCase,
    private val timeValidator: TimeValidator,
    private val dateValidator: DateValidator,
    private val timeTracker: TimeTracker,
) : ViewModel() {

    private val addEditIntervalDialogState: MutableStateFlow<AddEditIntervalDialogState?> =
        MutableStateFlow(null)

    val state: StateFlow<TimesheetScreenState> = combine(
        getTimeTrackerIntervalsUseCase(),
        addEditIntervalDialogState
    ) { daySections, editIntervalDialogState ->
        TimesheetScreenState(
            daySections = daySections,
            addEditIntervalDialogState = editIntervalDialogState
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

        addEditIntervalDialogState.value = AddEditIntervalDialogState(
            id = id,
            subject = interval.workingSubject,
            startTime = interval.startTime!!.formatToTimeWithoutColons(),
            endTime = interval.endTime!!.formatToTimeWithoutColons(),
            date = interval.date.formatToDateWithoutColons()
        )
    }

    fun onAddClicked() {
        addEditIntervalDialogState.value = AddEditIntervalDialogState(
            subject = "",
            startTime = "",
            endTime = "",
            date = ""
        )
    }

    fun onSubjectChanged(subject: String) {
        addEditIntervalDialogState.value = addEditIntervalDialogState.value?.copy(
            subject = subject
        )
    }

    fun onStartTimeChanged(startTime: String) {
        addEditIntervalDialogState.value = addEditIntervalDialogState.value?.copy(
            startTime = startTime,
            isWrongStartTimeError = !timeValidator(startTime)
        )
    }

    fun onEndTimeChanged(endTime: String) {
        addEditIntervalDialogState.value = addEditIntervalDialogState.value?.copy(
            endTime = endTime,
            isWrongEndTimeError = !timeValidator(endTime)
        )
    }

    fun onDateChanged(date: String) {
        addEditIntervalDialogState.value = addEditIntervalDialogState.value?.copy(
            date = date,
            isWrongDateError = !dateValidator(date)
        )
    }

    fun onDismissEditDialog() {
        addEditIntervalDialogState.value = null
    }

    private fun editTimeIntervalEntity() = viewModelScope.launch {
        addEditIntervalDialogState.value?.let {
            updateTimeIntervalUseCase(
                TimeIntervalParameters(
                    id = it.id!!,
                    subject = it.subject,
                    startTime = formatToInstant(date = it.date, time = it.startTime),
                    endTime = saveEndTimeWithCorrectDay(),
                    date = it.date
                )
            )
        }
    }

    private fun addTimeIntervalEntity() = viewModelScope.launch {
        addEditIntervalDialogState.value?.let {
            addTimeIntervalUseCase(
                TimeIntervalParameters(
                    subject = it.subject,
                    startTime = formatToInstant(date = it.date, time = it.startTime),
                    endTime = saveEndTimeWithCorrectDay(),
                    date = it.date
                )
            )
        }
    }

    fun onSaveClicked() {
        saveFormattedTime()
        addEditIntervalDialogState.value?.let { editState ->
            if (editState.id != null) {
                editTimeIntervalEntity()
            } else {
               addTimeIntervalEntity()
            }
        }
    }

    private fun saveEndTimeWithCorrectDay(): Instant {
        var endTime = Instant.now()
        addEditIntervalDialogState.value?.let { state ->
            val editedEndTimeSmaller = isEditedEndTimeSmaller(
                startTime = state.startTime,
                endTime = state.endTime
            )
            endTime = when {
                editedEndTimeSmaller -> formatToInstantWithAdditionalDay(
                    state.date,
                    state.endTime
                )

                else -> formatToInstant(state.date, state.endTime)
            }
        }
        return endTime
    }

    private fun saveFormattedTime() {
        addEditIntervalDialogState.value = addEditIntervalDialogState.value?.copy(
            startTime = addEditIntervalDialogState.value!!.startTime.formatTimeWithColon(),
            endTime = addEditIntervalDialogState.value!!.endTime.formatTimeWithColon(),
            date = addEditIntervalDialogState.value!!.date.formatDateWithColon()
        )
    }

    private fun isEditedEndTimeSmaller(startTime: String, endTime: String): Boolean {
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

    private fun getInitialTime(time: String?): InitialTime {
        val initialHour = if (time.isNullOrEmpty()) 12 else time.substring(0, 2).toInt()
        val initialMinute = if (time.isNullOrEmpty()) 0 else time.substring(2, 4).toInt()
        return InitialTime(
            hour = initialHour, minute = initialMinute
        )
    }

    fun getFinalSeconds(time: String?): String {
        return when {
            !time.isNullOrEmpty() -> time.substring(4, 6)
            else -> "00"
        }
    }

    data class InitialTime(
        val hour: Int,
        val minute: Int
    )
}