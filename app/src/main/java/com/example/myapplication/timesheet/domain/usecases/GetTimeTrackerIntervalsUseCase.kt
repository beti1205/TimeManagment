package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.timesheet.domain.model.toTimeTrackerInterval
import com.example.myapplication.timesheet.presentation.model.DaySection
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
                        headerDate = group.key,
                        headerTimeAmount = timeAmount,
                        timeIntervals = group.value
                    )
                }
        }
    }
}
