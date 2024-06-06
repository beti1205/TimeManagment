package com.example.myapplication.timesheet.domain.usecases.filtering

import com.example.myapplication.timesheet.presentation.DaySection
import javax.inject.Inject

interface GetFilteredSubjectsUseCase {

    operator fun invoke(
        filteredDaySectionsByDay: List<DaySection>,
        isSearching: Boolean,
        searchText: String
    ): List<String>
}

class GetFilteredSubjectsUseCaseUseCaseImpl @Inject constructor() : GetFilteredSubjectsUseCase {
    override fun invoke(
        filteredDaySectionsByDay: List<DaySection>,
        isSearching: Boolean,
        searchText: String
    ) = if (filteredDaySectionsByDay.isNotEmpty() && isSearching) {
        filteredDaySectionsByDay
            .flatMap { daySection ->
                daySection.timeIntervals.map { it.workingSubject }
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