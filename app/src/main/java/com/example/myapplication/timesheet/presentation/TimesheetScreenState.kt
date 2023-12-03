package com.example.myapplication.timesheet.presentation

import com.example.myapplication.timesheet.domain.usecases.DaySection

data class TimesheetScreenState(
    val daySections: List<DaySection> = emptyList()
)
