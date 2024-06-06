package com.example.myapplication.timesheet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.timesheet.domain.usecases.AddTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.validators.ValidateDate
import com.example.myapplication.timesheet.domain.usecases.DeleteTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.filtering.GetFilteredDaySectionsByDayUseCase
import com.example.myapplication.timesheet.domain.usecases.filtering.GetFilteredDaySectionsUseCase
import com.example.myapplication.timesheet.domain.usecases.filtering.GetFilteredSubjectsUseCase
import com.example.myapplication.timesheet.domain.usecases.GetTimeTrackerIntervalsUseCase
import com.example.myapplication.timesheet.domain.usecases.TimeIntervalParameters
import com.example.myapplication.timesheet.domain.usecases.validators.ValidateTime
import com.example.myapplication.timesheet.domain.usecases.UpdateTimeIntervalUseCase
import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import com.example.myapplication.utils.formatDateWithDash
import com.example.myapplication.utils.formatToDateWithoutDash
import com.example.myapplication.utils.formatToInstant
import com.example.myapplication.utils.formatToInstantWithAdditionalDay
import com.example.myapplication.utils.formatToTime
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
    private val validateTime: ValidateTime,
    private val validateDate: ValidateDate,
    private val timeTracker: TimeTracker,
    private val getFilteredDaySectionsByDayUseCase: GetFilteredDaySectionsByDayUseCase,
    private val getFilteredSubjectsUseCase: GetFilteredSubjectsUseCase,
    private val getFilteredDaySectionsUseCase: GetFilteredDaySectionsUseCase
) : ViewModel() {

    val filterDateOptions: List<DateFilterType> = listOf(
        DateFilterType.All,
        DateFilterType.Today,
        DateFilterType.Yesterday,
        DateFilterType.ThisWeek,
        DateFilterType.LastWeek,
        DateFilterType.CustomRange(null, null)
    )

    private val addEditIntervalDialogState: MutableStateFlow<AddEditIntervalDialogState?> =
        MutableStateFlow(null)

    private val searchBarState: MutableStateFlow<SearchBarState> =
        MutableStateFlow(SearchBarState())

    private val selectedFilter: MutableStateFlow<DateFilterType> =
        MutableStateFlow(DateFilterType.All)

    val state: StateFlow<TimesheetScreenState> = combine(
        getTimeTrackerIntervalsUseCase(),
        addEditIntervalDialogState,
        searchBarState,
        selectedFilter
    ) { daySections, editIntervalDialogState, searchBar, selectedFilter ->

        val searchText = searchBar.searchText
        val filteredDaySectionsByDay = getFilteredDaySectionsByDayUseCase(
            selectedFilter = selectedFilter,
            daySections = daySections
        )

        val subjects = getFilteredSubjectsUseCase(
            filteredDaySectionsByDay = filteredDaySectionsByDay,
            isSearching = searchBar.isSearching,
            searchText = searchText
        )

        val filteredDaySections = getFilteredDaySectionsUseCase(
            searchText = searchText,
            isSearching = !searchBar.isSearching,
            filteredDaySectionsByDay = filteredDaySectionsByDay
        )

        val daySectionsSortedByDates = filteredDaySections.sortedByDescending { it.headerDate }

        TimesheetScreenState(
            daySections = daySectionsSortedByDates,
            addEditIntervalDialogState = editIntervalDialogState,
            searchBarState = searchBar,
            subjects = subjects,
            selectedFilter = selectedFilter
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
        val startTime = interval.startTime!!.formatToTime()
        val endTime = interval.endTime!!.formatToTime()

        addEditIntervalDialogState.value = AddEditIntervalDialogState(
            id = id,
            subject = interval.workingSubject,
            startTime = startTime,
            endTime = endTime,
            date = interval.date.formatToDateWithoutDash()
        )
    }

    fun onAddClicked() {
        addEditIntervalDialogState.value = AddEditIntervalDialogState(
            subject = "",
            startTime = null,
            endTime = null,
            date = ""
        )
    }

    fun onSubjectChanged(subject: String) {
        addEditIntervalDialogState.value = addEditIntervalDialogState.value?.copy(
            subject = subject
        )
    }

    fun onStartTimeChanged(startTime: String) {
        val hasError = !validateTime(startTime)
        addEditIntervalDialogState.value = addEditIntervalDialogState.value?.copy(
            isWrongStartTimeError = hasError,
            startTime = if (hasError) {
                addEditIntervalDialogState.value?.startTime
            } else {
                getTime(
                    startTime
                )
            }
        )
    }

    fun onEndTimeChanged(endTime: String) {
        val hasError = !validateTime(endTime)
        addEditIntervalDialogState.value = addEditIntervalDialogState.value?.copy(
            isWrongEndTimeError = hasError,
            endTime = if (hasError) {
                addEditIntervalDialogState.value?.endTime
            } else {
                getTime(
                    endTime
                )
            }
        )
    }

    private fun getTime(time: String): Time {
        var adjustedTime = time
        while (adjustedTime.length < 6) {
            adjustedTime += " "
        }
        val hours = adjustedTime.substring(0, 2)
        val minutes = adjustedTime.substring(2, 4)
        val seconds = adjustedTime.substring(4, 6)
        return Time(hours, minutes, seconds)
    }

    fun onDateChanged(date: String) {
        addEditIntervalDialogState.value = addEditIntervalDialogState.value?.copy(
            date = date,
            isWrongDateError = !validateDate(date)
        )
    }

    fun onDismissEditDialog() {
        addEditIntervalDialogState.value = null
    }

    private fun editTimeIntervalEntity() = viewModelScope.launch {
        addEditIntervalDialogState.value?.let {
            if (it.startTime == null) return@launch
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
            if (it.startTime == null) return@launch
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
            if (state.startTime == null || state.endTime == null) return@let
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
            date = addEditIntervalDialogState.value!!.date.formatDateWithDash()
        )
    }

    private fun isEditedEndTimeSmaller(startTime: Time, endTime: Time): Boolean {
        if (startTime.hours != endTime.hours) {
            return startTime.hours > endTime.hours
        }
        if (startTime.minutes != endTime.minutes) {
            return startTime.minutes > endTime.minutes
        }
        return startTime.seconds > endTime.seconds
    }

    fun start(workingSubject: String) {
        timeTracker.startOrResetTimer(workingSubject)
    }

    fun reset() {
        timeTracker.reset()
    }

    fun onSearchTextChanged(text: String) {
        searchBarState.value = searchBarState.value.copy(
            searchText = text
        )
    }

    fun onSearchToggled() {
        onIsSearchingChanged()
        if (!searchBarState.value.isSearching) {
            onSearchTextChanged("")
        }
    }

    private fun onIsSearchingChanged() {
        searchBarState.value = searchBarState.value.copy(
            isSearching = !searchBarState.value.isSearching
        )
    }

    fun onSubjectSelected(subject: String) {
        onIsSearchingChanged()
        onSearchTextChanged(subject)
    }

    fun onSelectedFilterChanged(filterType: DateFilterType) {
        selectedFilter.value = filterType
    }
}

sealed class DateFilterType {
    data object All : DateFilterType()
    data object Today : DateFilterType()
    data object Yesterday : DateFilterType()
    data object ThisWeek : DateFilterType()
    data object LastWeek : DateFilterType()
    data class CustomRange(val startDate: String?, val endDate: String?) : DateFilterType()
}
