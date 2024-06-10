package com.example.myapplication.timesheet.presentation.model

import android.annotation.SuppressLint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class Time(
    val hours: String,
    val minutes: String,
    val seconds: String
): Comparable<Time> {
    override fun compareTo(other: Time): Int {
        val thisHours = this.hours.toInt()
        val thisMinutes = this.minutes.toInt()
        val thisSeconds = this.seconds.toInt()

        val otherHours = other.hours.toInt()
        val otherMinutes = other.minutes.toInt()
        val otherSeconds = other.seconds.toInt()

        if (thisHours != otherHours) {
            return thisHours - otherHours
        }
        if (thisMinutes != otherMinutes) {
            return thisMinutes - otherMinutes
        }

        return thisSeconds - otherSeconds
    }
}

fun String.toTime(): Time {
    var adjustedTime = this
    while (adjustedTime.length < 6) {
        adjustedTime += " "
    }
    val hours = adjustedTime.substring(0, 2)
    val minutes = adjustedTime.substring(2, 4)
    val seconds = adjustedTime.substring(4, 6)

    return Time(hours, minutes, seconds)
}

@SuppressLint("DefaultLocale")
fun Instant.toTime(): Time {
    val dateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())
    val hours = String.format("%02d", dateTime.hour)
    val minutes = String.format("%02d", dateTime.minute)
    val seconds = String.format("%02d", dateTime.second)

    return Time(hours, minutes, seconds)
}

fun Time.toInstant(date: String): Instant {
    val timeString = "${this.hours}:${this.minutes}:${this.seconds}"
    val strDateTime = date + "T" + timeString
    val ldt = LocalDateTime.parse(strDateTime)

    return ldt.atZone(ZoneId.systemDefault()).toInstant()
}