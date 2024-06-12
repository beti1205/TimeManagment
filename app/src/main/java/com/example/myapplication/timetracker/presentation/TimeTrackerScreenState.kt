package com.example.myapplication.timetracker.presentation

import java.time.Instant

data class TimeTrackerScreenState(
    val isActive: Boolean = false,
    val timeElapsed: Long = 0L,
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val workingSubject: String = "",
    val isSubjectErrorOccurred: Boolean = false,
    val filteredSubjectList: List<String> = emptyList()
)
