package com.example.myapplication.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Instant.formatDateTime(): String {
        val formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
        return formatter.format(this)
    }