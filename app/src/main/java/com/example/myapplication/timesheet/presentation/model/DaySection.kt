package com.example.myapplication.timesheet.presentation.model

import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval

data class DaySection(
    val headerDate: String,
    val headerTimeAmount: String,
    val timeIntervals: List<TimeTrackerInterval>
)