package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository

interface AddTimeIntervalUseCase {
    suspend operator fun invoke(
        timeIntervalParameters: TimeIntervalParameters
    )
}

class AddTimeIntervalUseCaseImpl(
    private val repository: TimeTrackerRepository
) : AddTimeIntervalUseCase {
    override suspend fun invoke(
        timeIntervalParameters: TimeIntervalParameters
    ) {
        return repository.insert(timeIntervalParameters.asTimeTrackerEntity())
    }
}