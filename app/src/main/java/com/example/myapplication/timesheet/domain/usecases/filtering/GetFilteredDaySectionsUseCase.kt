package com.example.myapplication.timesheet.domain.usecases.filtering

import com.example.myapplication.timesheet.presentation.DaySection
import com.example.myapplication.timetracker.domain.stopwatch.formatTime
import com.example.myapplication.timetracker.domain.stopwatch.toTime
import javax.inject.Inject

interface GetFilteredDaySectionsUseCase {

    operator fun invoke(
        searchText: String,
        isSearching: Boolean,
        filteredDaySectionsByDay: List<DaySection>
    ): List<DaySection>
}

class GetFilteredDaySectionsUseCaseImpl @Inject constructor() : GetFilteredDaySectionsUseCase {
    override fun invoke(
        searchText: String,
        isSearching: Boolean,
        filteredDaySectionsByDay: List<DaySection>
    ) = if (searchText.isNotEmpty() && isSearching) {
        filteredDaySectionsByDay.mapNotNull { section ->
            getDaySectionWithMatchingIntervals(section, searchText)
        }
    } else {
        filteredDaySectionsByDay
    }

    private fun getDaySectionWithMatchingIntervals(
        section: DaySection,
        searchText: String
    ): DaySection? {
        val matchingIntervals = section.timeIntervals.filter { interval ->
            interval.workingSubject == searchText
        }

        return if (matchingIntervals.isNotEmpty()) {
            DaySection(
                headerDate = section.headerDate,
                headerTimeAmount = matchingIntervals.sumOf { it.timeElapsed }.toTime()
                    .formatTime(),
                timeIntervals = matchingIntervals
            )
        } else {
            null
        }
    }
}