package com.example.myapplication.timesheet.presentation

import com.example.myapplication.timesheet.domain.usecases.DaySection

data class TimesheetScreenState(
    val daySections: List<DaySection> = emptyList(),
    val isEditDialogVisible: Boolean = false,
    val editIntervalDialogState: EditIntervalDialogState? = null
)

data class EditIntervalDialogState(
    val id: Int,
    val subject: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val isWrongStartTimeError: Boolean = false,
    val isWrongEndTimeError: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = !isWrongStartTimeError && !isWrongEndTimeError
}