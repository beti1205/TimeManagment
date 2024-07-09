package com.example.myapplication.timesheet.domain.usecases.filtering

import com.example.myapplication.timesheet.presentation.model.DaySection

interface GetFilteredSubjectsUseCase {

    operator fun invoke(
        filteredDaySectionsByDay: List<DaySection>,
        isSearching: Boolean,
        searchText: String
    ): List<String>
}

class GetFilteredSubjectsUseCaseUseCaseImpl : GetFilteredSubjectsUseCase {
    override fun invoke(
        filteredDaySectionsByDay: List<DaySection>,
        isSearching: Boolean,
        searchText: String
    ) = if (filteredDaySectionsByDay.isNotEmpty() && isSearching) {
        filteredDaySectionsByDay
            .flatMap { daySection ->
                daySection.timeIntervalsSections.flatMap { timeIntervalsSection ->
                    timeIntervalsSection.timeIntervals
                }.map { timeInterval -> timeInterval.workingSubject }
            }
            .distinct()
            .filter { subject ->
                searchText.isBlank() || subject.contains(
                    searchText.trim(),
                    true
                )
            }
    } else {
        emptyList()
    }
}
