package com.example.myapplication.timesheet.presentation

import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval

data class TimesheetScreenState(
    val daySections: List<DaySection> = emptyList(),
    val addEditIntervalDialogState: AddEditIntervalDialogState? = null,
    val searchBarState: SearchBarState = SearchBarState(),
    val subjects: List<String> = emptyList(),
    val selectedFilter: DateFilterType = DateFilterType.All
)

data class AddEditIntervalDialogState(
    val id: Int? = null,
    val subject: String,
    val startTime: Time?,
    val endTime: Time?,
    val date: String,
    val isWrongStartTimeError: Boolean = false,
    val isWrongEndTimeError: Boolean = false,
    val isWrongDateError: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = !isWrongStartTimeError && !isWrongEndTimeError && !isWrongDateError && subject.isNotEmpty()
                && date.isNotEmpty() && startTime != null && endTime != null
}

data class Time(
    val hours: String,
    val minutes: String,
    val seconds: String
)

data class DaySection(
    val headerDate: String,
    val headerTimeAmount: String,
    val timeIntervals: List<TimeTrackerInterval>
)

data class SearchBarState(
    val isSearching: Boolean = false,
    val searchText: String = ""
)