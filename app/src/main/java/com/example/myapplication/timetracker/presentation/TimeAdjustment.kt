package com.example.myapplication.timetracker.presentation

import com.example.myapplication.R

enum class TimeAdjustment(val amount: Int) {
    MINUS_30(-30),
    MINUS_15(-15),
    MINUS_5(-5),
    PLUS_5(5),
    PLUS_15(15),
    PLUS_30(30)
}

fun TimeAdjustment.getTypeName() =
    when (this) {
        TimeAdjustment.PLUS_5 -> R.string.plus_5
        TimeAdjustment.PLUS_15 -> R.string.plus_15
        TimeAdjustment.PLUS_30 ->R.string.plus_30
        TimeAdjustment.MINUS_5 -> R.string.minus_5
        TimeAdjustment.MINUS_15 -> R.string.minus_15
        TimeAdjustment.MINUS_30 -> R.string.minus_30
    }