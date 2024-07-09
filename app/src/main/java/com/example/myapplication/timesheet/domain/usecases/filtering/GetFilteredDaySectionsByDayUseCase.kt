package com.example.myapplication.timesheet.domain.usecases.filtering

import com.example.myapplication.timesheet.presentation.DateFilter
import com.example.myapplication.timesheet.presentation.model.DaySection
import com.example.myapplication.utils.formatToLongDate
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

interface GetFilteredDaySectionsByDayUseCase {
    operator fun invoke(
        selectedFilter: DateFilter,
        daySections: List<DaySection>
    ): List<DaySection>
}

class GetFilteredDaySectionsByDayUseCaseImpl : GetFilteredDaySectionsByDayUseCase {
    override fun invoke(
        selectedFilter: DateFilter,
        daySections: List<DaySection>
    ): List<DaySection> {
        return when (selectedFilter) {
            is DateFilter.All -> daySections
            is DateFilter.Today -> daySections.filter { daySection ->
                daySection.dateHeader == Instant.now().formatToLongDate()
            }

            is DateFilter.Yesterday -> daySections.filter { daySection ->
                daySection.dateHeader == (Instant.now().minus(Duration.ofDays(1))
                    .formatToLongDate())
            }

            is DateFilter.ThisWeek -> daySections.filter { daySection ->
                daySection.dateHeader.isInWeek()
            }

            is DateFilter.LastWeek -> daySections.filter { daySection ->
                daySection.dateHeader.isInWeek(lastWeek = true)
            }

            is DateFilter.CustomRange -> daySections.filter { daySection ->
                daySection.dateHeader.isInCustomRange(
                    startDate = selectedFilter.startDate,
                    endDate = selectedFilter.endDate
                )
            }
        }
    }

    private fun String.isInCustomRange(
        startDate: String,
        endDate: String
    ): Boolean {
        val inputDate = LocalDate.parse(this)
        val startLocalDate = LocalDate.parse(startDate)
        val endLocalDate = LocalDate.parse(endDate)

        return inputDate in startLocalDate..endLocalDate
    }

    private fun String.isInWeek(lastWeek: Boolean = false): Boolean {
        val inputDate = LocalDate.parse(this)
        val currentDate = LocalDate.now()
        val weeksToSubtract: Long = if (lastWeek) 1 else 0
        val startOfWeek = currentDate.minusWeeks(weeksToSubtract).with(DayOfWeek.MONDAY)
        val endOfWeek = currentDate.minusWeeks(weeksToSubtract).with(DayOfWeek.SUNDAY)

        return inputDate in startOfWeek..endOfWeek
    }
}