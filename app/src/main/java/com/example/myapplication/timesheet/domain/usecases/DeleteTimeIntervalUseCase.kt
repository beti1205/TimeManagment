package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository
import javax.inject.Inject

interface DeleteTimeIntervalUseCase {
    suspend operator fun invoke(id: String)
}

class DeleteTimeIntervalUseCaseImpl(
    private val repository: TimeTrackerRepository
) : DeleteTimeIntervalUseCase {
    override suspend fun invoke(id: String) {
        return repository.deleteTimeInterval(id.toInt())
    }
}