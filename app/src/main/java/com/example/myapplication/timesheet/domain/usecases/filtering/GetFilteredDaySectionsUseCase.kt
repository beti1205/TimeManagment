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
        val matchingIntervals = this.timeIntervals.filter { interval ->
            interval.workingSubject == searchText
        }

        return if (matchingIntervals.isNotEmpty()) {
            DaySection(
                headerDate = this.headerDate,
                headerTimeAmount = matchingIntervals.sumOf {
                    it.timeElapsed
                }.convertSecondsToTimeString(),
                timeIntervals = matchingIntervals
            )
        } else {
            null
        }
    }
}