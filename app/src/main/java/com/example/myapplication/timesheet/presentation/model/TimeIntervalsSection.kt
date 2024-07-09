package com.example.myapplication.timesheet.presentation.model

import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval

data class TimeIntervalsSection(
    val numberOfIntervals: Int,
    val groupedIntervalsSectionHeader: TimeTrackerInterval?,
    val timeIntervals: List<TimeTrackerInterval>,
    val expanded: Boolean = false
)