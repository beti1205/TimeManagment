package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timesheet.domain.model.toTimeTrackerInterval
import com.example.myapplication.timesheet.presentation.DaySection
import com.example.myapplication.timetracker.domain.stopwatch.formatTime
import com.example.myapplication.timetracker.domain.stopwatch.toTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetTimeTrackerIntervalsUseCase {
    operator fun invoke(): Flow<List<DaySection>>
}

class GetTimeTrackerIntervalsUseCaseImpl @Inject constructor(
    private val repository: TimeTrackerRepository
) : GetTimeTrackerIntervalsUseCase {
    override fun invoke(): Flow<List<DaySection>> {
        return repository.getAllTimeTrackerEntity().map { entities ->
            entities.map { it.toTimeTrackerInterval() }
                .groupBy { it.date }
                .map { group ->
                    val timeAmount = group.value.sumOf { it.timeElapsed }.toTime().formatTime()
                    DaySection(
                        headerDate = group.key,
                        headerTimeAmount = timeAmount,
                        timeIntervals = group.value
                    )
                }
        }
    }
}
