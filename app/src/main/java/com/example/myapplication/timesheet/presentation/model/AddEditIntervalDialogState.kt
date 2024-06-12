package com.example.myapplication.timesheet.presentation.model

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