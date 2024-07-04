package com.example.myapplication.timesheet.domain.usecases.filtering

import com.example.myapplication.timesheet.presentation.model.DaySection
import com.example.myapplication.utils.convertSecondsToTimeString

interface GetFilteredDaySectionsUseCase {

    operator fun invoke(
        searchText: String,
        isSearching: Boolean,
        filteredDaySectionsByDay: List<DaySection>
    ): List<DaySection>
}

class GetFilteredDaySectionsUseCaseImpl : GetFilteredDaySectionsUseCase {
    override fun invoke(
        searchText: String,
        isSearching: Boolean,
        filteredDaySectionsByDay: List<DaySection>
    ) = if (searchText.isNotEmpty() && isSearching) {
        filteredDaySectionsByDay.mapNotNull { section ->
            section.toDaySectionWithMatchingIntervals(searchText)
        }
    } else {
        filteredDaySectionsByDay
    }

    private fun DaySection.toDaySectionWithMatchingIntervals(
        searchText: String
    ): DaySection? {
        val matchingIntervals = this.timeIntervalsSections.filter { timeIntervalsSection ->
            timeIntervalsSection.timeIntervals.any { timeInterval ->
                timeInterval.workingSubject == searchText
            }
        }

        return if (matchingIntervals.isNotEmpty()) {
            DaySection(
                dateHeader = this.dateHeader,
                timeAmountHeader = matchingIntervals.flatMap { timeIntervalsSection ->
                    timeIntervalsSection.timeIntervals
                }.sumOf { timeInterval -> timeInterval.timeElapsed }.convertSecondsToTimeString(),
                timeIntervalsSections = matchingIntervals
            )
        } else {
            null
        }
    }
}