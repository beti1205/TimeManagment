package com.example.myapplication.timesheet.domain.usecases

import com.example.myapplication.data.TimeTrackerEntity
import java.time.Instant

data class TimeIntervalParameters(
    val id: Int = 0,
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