package com.example.myapplication.timesheet.presentation.model

data class FilteredDaySectionWithSubject(
    val filteredDaySections: List<DaySection>,
    val subjects: List<String>
)