package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository
import javax.inject.Inject

interface UpdateTimeIntervalUseCase {
    suspend operator fun invoke(
        timeIntervalParameters: TimeIntervalParameters
    )
}

class UpdateTimeIntervalUseCaseImpl(
    private val repository: TimeTrackerRepository
) : UpdateTimeIntervalUseCase {
    override suspend fun invoke(
        timeIntervalParameters: TimeIntervalParameters
    ) {
        return repository.updateTimeInterval(timeIntervalParameters.asTimeTrackerEntity())
    }
}