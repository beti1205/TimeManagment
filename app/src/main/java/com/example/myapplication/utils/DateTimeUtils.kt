package com.example.myapplication.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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

fun Long.formatToLongDate(): String {
    val date = Instant.ofEpochMilli(this)

    return date.formatToLongDate()
}

fun String.reformatDate(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("E, MMM d", Locale.ENGLISH)
    val date = LocalDate.parse(this, inputFormatter)

    return date.format(outputFormatter)
}

fun String.formatDateWithDash(): String {
    require(this.length == 8) { "Input string must be of length 6" }

    val year = this.substring(0, 4)
    val month = this.substring(4, 6)
    val day = this.substring(6, 8)

    return "$year-$month-$day"
}

fun calculateDaysBetween(start: Instant, end: Instant): String {
    val startDate = start.atZone(ZoneId.systemDefault()).toLocalDate()
    val endDate = end.atZone(ZoneId.systemDefault()).toLocalDate()
    val days = endDate.toEpochDay() - startDate.toEpochDay()

    return days.toString()
}

fun Long.convertSecondsToTimeString(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}