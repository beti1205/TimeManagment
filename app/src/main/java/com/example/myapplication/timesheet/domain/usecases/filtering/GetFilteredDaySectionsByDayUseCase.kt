package com.example.myapplication.timesheet.domain.usecases.filtering

import com.example.myapplication.timesheet.presentation.DateFilterType
import com.example.myapplication.timesheet.presentation.DaySection
import com.example.myapplication.utils.formatToLongDate
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

interface GetFilteredDaySectionsByDayUseCase {
    operator fun invoke(
        selectedFilter: DateFilterType,
        daySections: List<DaySection>
    ): List<DaySection>
}

class GetFilteredDaySectionsByDayUseCaseImpl @Inject constructor() :
    GetFilteredDaySectionsByDayUseCase {
    override fun invoke(
        selectedFilter: DateFilterType,
        daySections: List<DaySection>
    ): List<DaySection> {
        return when (selectedFilter) {
            is DateFilterType.All -> daySections
            is DateFilterType.Today -> daySections.filter { daySection ->
                daySection.headerDate == Instant.now().formatToLongDate()
            }

            is DateFilterType.Yesterday -> daySections.filter { daySection ->
                daySection.headerDate == (Instant.now().minus(Duration.ofDays(1))
                    .formatToLongDate())
            }

            is DateFilterType.ThisWeek -> { daySections.filter { daySection ->
                    dateContainsInWeek(daySection)
                }
            }

            is DateFilterType.LastWeek -> { daySections.filter { daySection ->
                    dateContainsInWeek(daySection = daySection, lastWeek = true)
                }
            }

            is DateFilterType.CustomRange -> daySections.filter { daySection ->
                dateContainsInCustomRange(
                    daySection = daySection,
                    startDate = selectedFilter.startDate!!,
                    endDate = selectedFilter.endDate!!
                )
            }
        }
    }

    private fun dateContainsInCustomRange(
        daySection: DaySection,
        startDate: String,
        endDate: String
    ): Boolean {
        val dateString = daySection.headerDate
        val inputDate = LocalDate.parse(dateString)
        val startLocalDate = LocalDate.parse(startDate)
        val endLocalDate = LocalDate.parse(endDate)
        return inputDate in startLocalDate..endLocalDate
    }

    private fun dateContainsInWeek(daySection: DaySection, lastWeek: Boolean = false): Boolean {
        val dateString = daySection.headerDate
        val inputDate = LocalDate.parse(dateString)
        val currentDate = LocalDate.now()
        val startOfWeek = if (lastWeek) {
            currentDate.minusWeeks(1).with(DayOfWeek.MONDAY)
        } else {
            currentDate.with(
                DayOfWeek.MONDAY
            )
        }
        val endOfWeek = if (lastWeek) {
            currentDate.minusWeeks(1).with(DayOfWeek.SUNDAY)
        } else {
            currentDate.with(
                DayOfWeek.SUNDAY
            )
        }
        return inputDate in startOfWeek..endOfWeek
    }
}