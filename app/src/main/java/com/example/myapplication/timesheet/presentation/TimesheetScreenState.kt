package com.example.myapplication.timesheet.presentation

import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState
import com.example.myapplication.timesheet.presentation.model.DaySection
import com.example.myapplication.timesheet.presentation.model.SearchBarState

data class TimesheetScreenState(
    val daySections: List<DaySection> = emptyList(),
    val addEditIntervalDialogState: AddEditIntervalDialogState? = null,
    val searchBarState: SearchBarState = SearchBarState(),
    val subjects: List<String> = emptyList(),
    val selectedFilter: DateFilter = DateFilter.All
) {
    val isNotFindingResults = daySections.isEmpty() && (selectedFilter != DateFilter.All
            || searchBarState.searchText.isNotBlank())
}

