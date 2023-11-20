package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.timesheet.domain.model.TimeTrackerIntervals
import com.example.myapplication.timesheet.domain.model.toTimeTrackerIntervals
import com.example.myapplication.timetracker.domain.stopwatch.formatTime
import com.example.myapplication.timetracker.domain.stopwatch.toTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetTimeTrackerIntervalsUseCase {
    operator fun invoke(): Flow<List<IntervalsListItem>>
}

sealed class IntervalsListItem {
    data class Header(val date: String, val timeAmount: String) : IntervalsListItem()
    data class Interval(val interval: TimeTrackerIntervals) : IntervalsListItem()
}

class GetTimeTrackerIntervalsUseCaseImpl @Inject constructor(
    private val repository: TimeTrackerRepository
) : GetTimeTrackerIntervalsUseCase {
    override fun invoke(): Flow<List<IntervalsListItem>> {
        return repository.getAllTimeTrackerEntity().map { entities ->
            entities.map { it.toTimeTrackerIntervals() }
                .groupBy { it.date }
                .flatMap { group ->
                    val timeAmount = group.value.sumOf { it.timeElapsed }.toTime().formatTime()
                    buildList {
                        add(IntervalsListItem.Header(date = group.key, timeAmount = timeAmount ))
                        addAll(group.value.map { IntervalsListItem.Interval(it) })
                    }
                }
        }
    }
}