package com.example.myapplication.domain.usecase

import com.example.myapplication.data.TimeTrackerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetAllWorkingSubjectsUseCase {
    operator fun invoke(): Flow<List<String>>
}

class GetAllWorkingSubjectsUseCaseImpl @Inject constructor(
    private val repository: TimeTrackerRepository
): GetAllWorkingSubjectsUseCase {
    override fun invoke(): Flow<List<String>> {
        return repository.getAllWorkingSubject()
    }
}