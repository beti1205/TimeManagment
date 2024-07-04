package com.example.myapplication.timesheet.presentation.model

data class DaySection(
    val dateHeader: String,
    val timeAmountHeader: String,
    val timeIntervalsSections: List<TimeIntervalsSection>
)