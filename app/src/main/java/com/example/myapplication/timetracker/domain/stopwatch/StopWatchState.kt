package com.example.myapplication.timetracker.domain.stopwatch

import java.time.Instant

data class StopWatchState(
    val isActive: Boolean = false,
    val timeElapsed: Long = 0L,
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val isTimeTrackingFinished: Boolean = false
)