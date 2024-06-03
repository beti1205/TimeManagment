package com.example.myapplication.timesheet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.timesheet.domain.usecases.AddTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.DateValidator
import com.example.myapplication.timesheet.domain.usecases.DeleteTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.GetTimeTrackerIntervalsUseCase
import com.example.myapplication.timesheet.domain.usecases.TimeIntervalParameters
import com.example.myapplication.timesheet.domain.usecases.ValidateTime
import com.example.myapplication.timesheet.domain.usecases.UpdateTimeIntervalUseCase
import com.example.myapplication.timetracker.domain.stopwatch.formatTime
import com.example.myapplication.timetracker.domain.stopwatch.toTime
import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import com.example.myapplication.utils.formatDateWithDash
import com.example.myapplication.utils.formatToDateWithoutDash
import com.example.myapplication.utils.formatToInstant
import com.example.myapplication.utils.formatToInstantWithAdditionalDay
import com.example.myapplication.utils.formatToLongDate
import com.example.myapplication.utils.formatToTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TimesheetViewModel @Inject constructor(
    getTimeTrackerIntervalsUseCase: GetTimeTrackerIntervalsUseCase,
    private val deleteTimeIntervalUseCase: DeleteTimeIntervalUseCase,
    private val updateTimeIntervalUseCase: UpdateTimeIntervalUseCase,
    private val addTimeIntervalUseCase: AddTimeIntervalUseCase,
    private val validateTime: ValidateTime,
    private val dateValidator: DateValidator,
    private val timeTracker: TimeTracker,
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
        val filteredDaySectionsByDay = getFilteredDaySectionsByDay(
            selectedFilter = selectedFilter,
            daySections = daySections
        )

        val subjects = getFilteredSubjects(
            filteredDaySectionsByDay = filteredDaySectionsByDay,
            isSearching = searchBar.isSearching,
            searchText = searchText
        )

        val filteredDaySections = getFilteredDaySections(
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

    private fun getFilteredDaySections(
        searchText: String,
        isSearching: Boolean,
        filteredDaySectionsByDay: List<DaySection>
    ) = if (searchText.isNotEmpty() && isSearching) {
        filteredDaySectionsByDay.mapNotNull { section ->
            getFilteredDaySectionBySubject(section, searchText)
        }
    } else {
        filteredDaySectionsByDay
    }

    private fun getFilteredSubjects(
        filteredDaySectionsByDay: List<DaySection>,
        isSearching: Boolean,
        searchText: String
    ) = if (filteredDaySectionsByDay.isNotEmpty() && isSearching) {
        filteredDaySectionsByDay
            .flatMap { daySection ->
                daySection.timeIntervals.map { it.workingSubject }
            }
            .distinct()
            .filter { subject ->
                searchText.isBlank() || subject.contains(
                    searchText.trim(),
                    true
                )
            }
    } else {
        emptyList()
    }

    private fun getFilteredDaySectionBySubject(
        section: DaySection,
        searchText: String
    ): DaySection? {
        val matchingIntervals = section.timeIntervals.filter { interval ->
            interval.workingSubject == searchText
        }

        return if (matchingIntervals.isNotEmpty()) {
            DaySection(
                headerDate = section.headerDate,
                headerTimeAmount = matchingIntervals.sumOf { it.timeElapsed }.toTime()
                    .formatTime(),
                timeIntervals = matchingIntervals
            )
        } else {
            null
        }
    }

    private fun getFilteredDaySectionsByDay(
        selectedFilter: DateFilterType,
        daySections: List<DaySection>
    ): List<DaySection> {
        return when (selectedFilter) {
            is DateFilterType.All -> daySections
            is DateFilterType.Today -> daySections.filter { daySection ->
                daySection.headerDate == Instant.now().formatToLongDate()
            }

            is DateFilterType.Yesterday -> daySections.filter { daySection ->
                daySection.headerDate == (Instant.now().minus(Duration.ofDays(1))
                    .formatToLongDate())
            }

            is DateFilterType.ThisWeek -> {
                daySections.filter { daySection ->
                    dateContainsInWeek(daySection)
                }
            }

            is DateFilterType.LastWeek -> {
                daySections.filter { daySection ->
                    dateContainsInWeek(daySection = daySection, lastWeek = true)
                }
            }

            is DateFilterType.CustomRange -> daySections.filter { daySection ->
                dateContainsInCustomRange(daySection, selectedFilter)
            }
        }
    }

    private fun dateContainsInCustomRange(
        daySection: DaySection,
        selectedFilter: DateFilterType.CustomRange
    ): Boolean {
        val dateString = daySection.headerDate
        val inputDate = LocalDate.parse(dateString)
        val startDate = LocalDate.parse(selectedFilter.startDate)
        val endDate = LocalDate.parse(selectedFilter.endDate)
        return inputDate in startDate..endDate
    }

    private fun dateContainsInWeek(daySection: DaySection, lastWeek: Boolean = false): Boolean {
        val dateString = daySection.headerDate
        val inputDate = LocalDate.parse(dateString)
        val currentDate = LocalDate.now()
        val startOfWeek =
            if (lastWeek) currentDate.minusWeeks(1).with(DayOfWeek.MONDAY) else currentDate.with(
                DayOfWeek.MONDAY
            )
        val endOfWeek =
            if (lastWeek) currentDate.minusWeeks(1).with(DayOfWeek.SUNDAY) else currentDate.with(
                DayOfWeek.SUNDAY
            )
        return inputDate in startOfWeek..endOfWeek
    }

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
            isWrongDateError = !dateValidator(date)
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
