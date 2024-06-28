package com.example.myapplication.timetracker.presentation

import java.time.Instant

data class TimeTrackerScreenState(
    val isActive: Boolean = false,
    val timeElapsed: Long = 0L,
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val workingSubject: String = "",
    val isSubjectErrorOccurred: Boolean = false,
    val filteredSubjects: List<String> = emptyList(),
    val selectedChangesType: TimeAmountChangesType? = null
) {
    val showEndTime = endTime != null && !isActive
    val chipsEnabled = isActive || (timeElapsed == 0L && endTime == null)
    val timeAmountInMilliseconds = (endTime?.toEpochMilli()
        ?: startTime?.toEpochMilli() ?: 0L) - (startTime?.toEpochMilli() ?: 0L)
}
