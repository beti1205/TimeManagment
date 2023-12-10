package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.utils.formatToInstant
import javax.inject.Inject

interface UpdateTimeIntervalUseCase {
    suspend operator fun invoke(
        id: Int,
        subject: String,
        startTime: String,
        endTime: String,
        date: String
    )
}

class UpdateTimeIntervalUseCaseImpl @Inject constructor(
    private val repository: TimeTrackerRepository
) : UpdateTimeIntervalUseCase {
    override suspend fun invoke(
        id: Int,
        subject: String,
        startTime: String,
        endTime: String,
        date: String
    ) {
        return repository.updateTimeInterval(
            id = id,
            subject = subject,
            startTime = formatToInstant(date, startTime),
            endTime = formatToInstant(date, endTime)
        )
    }
}