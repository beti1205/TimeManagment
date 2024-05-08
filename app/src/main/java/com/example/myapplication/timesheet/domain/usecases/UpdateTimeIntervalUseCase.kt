package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerEntity
import com.example.myapplication.data.TimeTrackerRepository
import java.time.Instant
import javax.inject.Inject

interface UpdateTimeIntervalUseCase {
    suspend operator fun invoke(
        timeIntervalParameters: TimeIntervalParameters
    )
}

class UpdateTimeIntervalUseCaseImpl @Inject constructor(
    private val repository: TimeTrackerRepository
) : UpdateTimeIntervalUseCase {
    override suspend fun invoke(
     timeIntervalParameters: TimeIntervalParameters
    ) {
        return repository.updateTimeInterval(timeIntervalParameters.asTimeTrackerEntity())
    }
}

data class TimeIntervalParameters(
    val id: Int,
    val startTime: Instant,
    val endTime: Instant,
    val subject: String,
    val date: String
)

fun TimeIntervalParameters.asTimeTrackerEntity(): TimeTrackerEntity {
    return TimeTrackerEntity(
        id = id,
        startTime = startTime,
        endTime = endTime,
        workingSubject = subject,
        date = date
    )
}