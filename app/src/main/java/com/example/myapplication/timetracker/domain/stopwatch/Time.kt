package com.example.myapplication.timetracker.domain.stopwatch

import android.annotation.SuppressLint

data class Time(
    val hours: Long,
    val minutes: Long,
    val seconds: Long
)

@SuppressLint("DefaultLocale")
fun Time.formatTime(): String {
    return String.format(
        "%02d:%02d:%02d",
        this.hours,
        this.minutes,
        this.seconds
    )
}

fun Long.toTime(): Time {
    val seconds = this % 60
    val minutes = (this % 3600) / 60
    val hours = this / 3600

    return Time(hours, minutes, seconds)
}
