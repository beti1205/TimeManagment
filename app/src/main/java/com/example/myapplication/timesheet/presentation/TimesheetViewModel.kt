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
import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState
import com.example.myapplication.timesheet.presentation.model.SearchBarState
import com.example.myapplication.timesheet.presentation.model.toInstant
import com.example.myapplication.timesheet.presentation.model.toTime
import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import com.example.myapplication.utils.formatDateWithDash
import com.example.myapplication.utils.formatToDateWithoutDash
import com.example.myapplication.utils.formatToInstantWithAdditionalDay
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

    val filterDateOptions: List<DateFilter> = listOf(
        DateFilter.All,
        DateFilter.Today,
        DateFilter.Yesterday,
        DateFilter.ThisWeek,
        DateFilter.LastWeek,
        DateFilter.CustomRange("", "")
    )

    private val addEditIntervalDialogState: MutableStateFlow<AddEditIntervalDialogState?> =
        MutableStateFlow(null)

    private val searchBarState: MutableStateFlow<SearchBarState> =
        MutableStateFlow(SearchBarState())

    private val selectedFilter: MutableStateFlow<DateFilter> =
        MutableStateFlow(DateFilter.All)

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
        val startTime = interval.startTime!!.toTime()
        val endTime = interval.endTime!!.toTime()

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
                startTime.toTime()
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
                endTime.toTime()
            }
        )
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

    private fun editTimeInterval() = viewModelScope.launch {
        addEditIntervalDialogState.value?.let { dialogState ->
            if (dialogState.startTime == null) {
                return@launch
            }

            updateTimeIntervalUseCase(
                TimeIntervalParameters(
                    id = dialogState.id!!,
                    subject = dialogState.subject,
                    startTime = dialogState.startTime.toInstant(
                        date = dialogState.date
                    ),
                    endTime = getEndTimeWithCorrectDay(),
                    date = dialogState.date
                )
            )
        }
    }

    private fun addTimeInterval() = viewModelScope.launch {
        addEditIntervalDialogState.value?.let { dialogState ->
            if (dialogState.startTime == null) {
                return@launch
            }

            addTimeIntervalUseCase(
                TimeIntervalParameters(
                    subject = dialogState.subject,
                    startTime = dialogState.startTime.toInstant(
                        date = dialogState.date
                    ),
                    endTime = getEndTimeWithCorrectDay(),
                    date = dialogState.date
                )
            )
        }
    }

    fun onSaveClicked() {
        saveFormattedTime()
        addEditIntervalDialogState.value?.let { editState ->
            if (editState.id != null) {
                editTimeInterval()
            } else {
                addTimeInterval()
            }
        }
    }

    private fun getEndTimeWithCorrectDay(): Instant {
        var endTime = Instant.now()
        addEditIntervalDialogState.value?.let { state ->
            val stateStartTime = state.startTime
            val stateEndTime = state.endTime

            if (stateStartTime == null || stateEndTime == null) {
                return@let
            }

            val difference = stateStartTime.compareTo(stateEndTime)

            endTime = when {
                difference > 0 -> {
                    formatToInstantWithAdditionalDay(
                        state.date,
                        stateEndTime
                    )
                }

                else -> { stateEndTime.toInstant(state.date) }
            }
        }

        return endTime
    }

    private fun saveFormattedTime() {
        addEditIntervalDialogState.value = addEditIntervalDialogState.value?.copy(
            date = addEditIntervalDialogState.value!!.date.formatDateWithDash()
        )
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

    fun onSelectedFilterChanged(filterType: DateFilter) {
        selectedFilter.value = filterType
    }
}
