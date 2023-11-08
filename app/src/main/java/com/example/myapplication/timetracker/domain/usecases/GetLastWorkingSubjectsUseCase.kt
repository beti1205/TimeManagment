package com.example.myapplication.timetracker.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetLastWorkingSubjectsUseCase {
    operator fun invoke(): Flow<String>
}

class GetLastWorkingSubjectsUseCaseImpl @Inject constructor(
    private val repository: TimeTrackerRepository
): GetLastWorkingSubjectsUseCase {
    override fun invoke(): Flow<String> {
        return repository.getLastWorkingSubject()
    }
}