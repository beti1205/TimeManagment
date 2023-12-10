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

fun formatToInstant(date: String, time: String): Instant {
    val strDateTime = date + "T" + time
    val ldt = LocalDateTime.parse(strDateTime)
    return ldt.atZone(ZoneId.systemDefault()).toInstant()
}

fun changeDateFormat(inputDateString: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("E, MMM d", Locale.ENGLISH)

    val date = LocalDate.parse(inputDateString, inputFormatter)
    return date.format(outputFormatter)
}
