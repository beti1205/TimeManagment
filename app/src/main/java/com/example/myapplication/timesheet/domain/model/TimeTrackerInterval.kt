package com.example.myapplication.timesheet.domain.model

import com.example.myapplication.data.TimeTrackerEntity
import com.example.myapplication.utils.calculateDaysBetween
import java.time.Instant

data class TimeTrackerInterval(
    val id: Int,
    val timeElapsed: Long,
    val startTime: Instant?,
    val endTime: Instant?,
    val workingSubject: String,
    val date: String
){
    val additionalDays = calculateDaysBetween(startTime, endTime)
}

fun TimeTrackerEntity.toTimeTrackerInterval(): TimeTrackerInterval{
    return TimeTrackerInterval(
        id = id,
        timeElapsed = endTime!!.epochSecond - startTime!!.epochSecond,
        startTime = startTime,
        endTime = endTime,
        workingSubject = workingSubject,
        date = date
    )
}