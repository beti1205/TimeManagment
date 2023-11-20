package com.example.myapplication.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Instant.formatToTime(): String {
        val formatter = DateTimeFormatter
            .ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault())
        return formatter.format(this)
    }

fun Instant.formatToDate(): String {
    val formatter = DateTimeFormatter
        .ofPattern("E, MMMd")
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}