package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timesheet.domain.model.toTimeTrackerInterval
import com.example.myapplication.timesheet.presentation.model.DaySection
import com.example.myapplication.timesheet.presentation.model.TimeIntervalsSection
import com.example.myapplication.utils.convertSecondsToTimeString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetTimeTrackerIntervalsUseCase {
    operator fun invoke(): Flow<List<DaySection>>
}

class GetTimeTrackerIntervalsUseCaseImpl(
    private val repository: TimeTrackerRepository
) : GetTimeTrackerIntervalsUseCase {
    override fun invoke(): Flow<List<DaySection>> {
        return repository.getAllTimeTrackerEntity().map { entities ->
            entities.map { it.toTimeTrackerInterval() }
                .groupBy { it.date }
                .map { group ->
                    val timeAmount = group.value.sumOf {
                        it.timeElapsed
                    }.convertSecondsToTimeString()
                    DaySection(
                        dateHeader = group.key,
                        timeAmountHeader = timeAmount,
                        timeIntervalsSections = getGroupedTimeIntervals(group.value)
                    )
                }
        }
    }
}

private fun getGroupedTimeIntervals(
    intervalsOfDay: List<TimeTrackerInterval>
): List<TimeIntervalsSection> {
    return intervalsOfDay.groupBy { it.workingSubject }.map { groupedBySubject ->
        groupedBySubject.value.run {
            val numberOfIntervals = size
            val earliestIntervalStartTime = last().startTime
            val earliestIntervalId = last().id

            TimeIntervalsSection(
                numberOfIntervals = numberOfIntervals,
                groupedIntervalsSectionHeader = if (numberOfIntervals > 1) {
                    TimeTrackerInterval(
                        id = "${groupedBySubject.key} $earliestIntervalStartTime $earliestIntervalId",
                        timeElapsed = sumOf { it.timeElapsed },
                        startTime = earliestIntervalStartTime,
                        endTime = first().endTime,
                        workingSubject = first().workingSubject,
                        date = last().date
                    )
                } else {
                    null
                },
                timeIntervals = this
            )
        }
    }
}
