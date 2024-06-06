package com.example.myapplication.timesheet.domain.usecases.validators

import javax.inject.Inject

val DATE_PATTERN: Regex = Regex( """^(\d{4})(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])$""")

interface ValidateDate {
    operator fun invoke(dateString: String): Boolean
}

class ValidateDateImpl @Inject constructor() : ValidateDate {
    override fun invoke(dateString: String): Boolean {
        return DATE_PATTERN.matches(dateString)
    }
}