package com.example.myapplication.timetracker.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository
import kotlinx.coroutines.flow.Flow

interface GetLastWorkingSubjectsUseCase {
    operator fun invoke(): Flow<String>
}

class GetLastWorkingSubjectsUseCaseImpl(
    private val repository: TimeTrackerRepository
) : GetLastWorkingSubjectsUseCase {
    override fun invoke(): Flow<String> {
        return repository.getLastWorkingSubject()
    }
}