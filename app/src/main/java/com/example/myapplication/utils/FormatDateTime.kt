package com.example.myapplication.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Instant.formatToTime(): String {
    val formatter = DateTimeFormatter
        .ofPattern("HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

fun Instant.formatToLongTime(): String {
    val formatter = DateTimeFormatter
        .ofPattern("HH:mm:ss")
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

fun Instant.formatToTimeWithoutColons(): String {
    val formatter = DateTimeFormatter
        .ofPattern("HHmmss")
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}
fun String.formatToDateWithoutColons(): String {
    val inputDate = LocalDate.parse(this)
    val outputFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
   return inputDate.format(outputFormat)
}

fun Instant.formatToDate(): String {
    val formatter = DateTimeFormatter
        .ofPattern("E, MMMd")
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

fun Instant.formatToLongDate(): String {
    val formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

fun Long.formatToDateWithoutColons(): String {
    val date = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter
        .ofPattern("yyyyMMdd")
        .withZone(ZoneId.systemDefault())
    return formatter.format(date)
}

fun formatToInstant(date: String, time: String): Instant {
    val strDateTime = date + "T" + time
    val ldt = LocalDateTime.parse(strDateTime)
    return ldt.atZone(ZoneId.systemDefault()).toInstant()
}

fun formatToInstantWithAdditionalDay(date: String, time: String): Instant {
    val strDateTime = date + "T" + time
    val ldt = LocalDateTime.parse(strDateTime).plusDays(1)
    return ldt.atZone(ZoneId.systemDefault()).toInstant()
}

fun changeDateFormat(date: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("E, MMM d", Locale.ENGLISH)

    val date = LocalDate.parse(date, inputFormatter)
    return date.format(outputFormatter)
}

fun String.formatTimeWithColon(): String {
    require(this.length == 6) { "Input string must be of length 6" }

    val hour = this.substring(0, 2)
    val minute = this.substring(2, 4)
    val second = this.substring(4)

    return "$hour:$minute:$second"
}

fun String.formatDateWithColon(): String {
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