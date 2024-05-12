package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerEntity
import com.example.myapplication.data.TimeTrackerRepository
import java.time.Instant
import javax.inject.Inject

interface AddTimeIntervalUseCase {
    suspend operator fun invoke(
        timeIntervalParameters: TimeIntervalParameters
    )
}

class AddTimeIntervalUseCaseImpl @Inject constructor(
    private val repository: TimeTrackerRepository
) : AddTimeIntervalUseCase {
    override suspend fun invoke(
     timeIntervalParameters: TimeIntervalParameters
    ) {
        return repository.insert(timeIntervalParameters.asTimeTrackerEntity())
    }
}