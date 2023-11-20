package com.example.myapplication.timesheet.domain.model

import com.example.myapplication.data.TimeTrackerEntity
import java.time.Instant

data class TimeTrackerIntervals(
    val timeElapsed: Long,
    val startTime: Instant?,
    val endTime: Instant?,
    val workingSubject: String,
    val date: String
)

fun TimeTrackerEntity.toTimeTrackerIntervals(): TimeTrackerIntervals{
    return TimeTrackerIntervals(
        timeElapsed = timeElapsed,
        startTime = startTime,
        endTime = endTime,
        workingSubject = workingSubject,
        date = date
    )
}