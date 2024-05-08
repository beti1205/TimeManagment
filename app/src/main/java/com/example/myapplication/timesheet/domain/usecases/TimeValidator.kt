package com.example.myapplication.timesheet.domain.usecases

import javax.inject.Inject

val TIME_PATTERN: Regex = Regex("""^([0-1]\d|2[0-3])[0-5]\d[0-5]\d$""")

interface TimeValidator {
    operator fun invoke(timeString: String): Boolean
}

class TimeValidatorImpl @Inject constructor() : TimeValidator {
    override fun invoke(timeString: String): Boolean {
        return TIME_PATTERN.matches(timeString)
    }
}