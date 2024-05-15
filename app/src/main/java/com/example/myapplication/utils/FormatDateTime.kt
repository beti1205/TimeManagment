package com.example.myapplication.utils

import android.annotation.SuppressLint
import com.example.myapplication.timesheet.presentation.Time
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("DefaultLocale")
fun Instant.formatToTime(): Time {
    val dateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())
    val hours = String.format("%02d", dateTime.hour)
    val minutes = String.format("%02d", dateTime.minute)
    val seconds = String.format("%02d", dateTime.second)

    return Time(hours, minutes, seconds)
}

fun formatToInstant(date: String, time: Time): Instant {
    val timeString = "${time.hours}:${time.minutes}:${time.seconds}"
    val strDateTime = date + "T" + timeString
    val ldt = LocalDateTime.parse(strDateTime)
    return ldt.atZone(ZoneId.systemDefault()).toInstant()
}

fun Instant.formatToTimeStr(): String {
    val formatter = DateTimeFormatter
        .ofPattern("HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

fun String.formatToDateWithoutDash(): String {
    val inputDate = LocalDate.parse(this)
    val outputFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
   return inputDate.format(outputFormat)
}

fun Instant.formatToLongDate(): String {
    val formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

fun Long.formatToDateWithoutDash(): String {
    val date = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter
        .ofPattern("yyyyMMdd")
        .withZone(ZoneId.systemDefault())
    return formatter.format(date)
}

fun formatToInstantWithAdditionalDay(date: String, time: Time): Instant {
    val timeString = "${time.hours}:${time.minutes}:${time.seconds}"
    val strDateTime = date + "T" + timeString
    val ldt = LocalDateTime.parse(strDateTime).plusDays(1)
    return ldt.atZone(ZoneId.systemDefault()).toInstant()
}

fun changeDateFormat(date: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("E, MMM d", Locale.ENGLISH)

    val date = LocalDate.parse(date, inputFormatter)
    return date.format(outputFormatter)
}

fun String.formatDateWithDash(): String {
    require(this.length == 8) { "Input string must be of length 6" }

    val year = this.substring(0, 4)
    val month = this.substring(4, 6)
    val day = this.substring(6,8)

    return "$year-$month-$day"
}

fun getDaysBetween(start: Instant, end: Instant): String {
    val startDate = start.atZone(ZoneId.systemDefault()).toLocalDate()
    val endDate = end.atZone(ZoneId.systemDefault()).toLocalDate()
    val days = endDate.toEpochDay() - startDate.toEpochDay()
    return days.toString()
}