package com.example.myapplication.timesheet.domain.usecases

import javax.inject.Inject

val DATE_PATTERN: Regex = Regex( """^(\d{4})(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])$""")

interface DateValidator {
    operator fun invoke(dateString: String): Boolean
}

class DateValidatorImpl @Inject constructor() : DateValidator {
    override fun invoke(dateString: String): Boolean {
        return DATE_PATTERN.matches(dateString)
    }
}