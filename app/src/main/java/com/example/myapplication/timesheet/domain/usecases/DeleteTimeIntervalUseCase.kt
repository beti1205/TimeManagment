package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository
import javax.inject.Inject

interface DeleteTimeIntervalUseCase {
    suspend operator fun invoke(id: Int)
}

class DeleteTimeIntervalUseCaseImpl(
    private val repository: TimeTrackerRepository
) : DeleteTimeIntervalUseCase {
    override suspend fun invoke(id: Int) {
        return repository.deleteTimeInterval(id)
    }
}