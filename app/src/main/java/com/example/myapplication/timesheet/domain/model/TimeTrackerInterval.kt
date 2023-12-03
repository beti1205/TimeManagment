package com.example.myapplication.timesheet.domain.model

import com.example.myapplication.data.TimeTrackerEntity
import java.time.Instant

data class TimeTrackerInterval(
    val timeElapsed: Long,
    val startTime: Instant?,
    val endTime: Instant?,
    val workingSubject: String,
    val date: String
)

fun TimeTrackerEntity.toTimeTrackerInterval(): TimeTrackerInterval{
    return TimeTrackerInterval(
        timeElapsed = timeElapsed,
        startTime = startTime,
        endTime = endTime,
        workingSubject = workingSubject,
        date = date
    )
}