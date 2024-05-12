package com.example.myapplication.timesheet.presentation

import com.example.myapplication.timesheet.domain.usecases.DaySection

data class TimesheetScreenState(
    val daySections: List<DaySection> = emptyList(),
    val addEditIntervalDialogState: AddEditIntervalDialogState? = null
)

data class AddEditIntervalDialogState(
    val id: Int? = null,
    val subject: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val isWrongStartTimeError: Boolean = false,
    val isWrongEndTimeError: Boolean = false,
    val isWrongDateError: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = !isWrongStartTimeError && !isWrongEndTimeError && !isWrongDateError && subject.isNotEmpty()
                && date.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty()
}