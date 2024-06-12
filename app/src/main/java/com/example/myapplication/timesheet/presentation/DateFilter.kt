package com.example.myapplication.timesheet.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R

sealed class DateFilter {
    data object All : DateFilter()
    data object Today : DateFilter()
    data object Yesterday : DateFilter()
    data object ThisWeek : DateFilter()
    data object LastWeek : DateFilter()
    data class CustomRange(val startDate: String, val endDate: String) : DateFilter()
}

@Composable
fun DateFilter.getFilterName() =
    when (this) {
        DateFilter.All -> stringResource(R.string.date_filter_all)
        is DateFilter.CustomRange -> "${this.startDate}..${this.endDate}"
        DateFilter.LastWeek -> stringResource(R.string.date_filter_last_week)
        DateFilter.ThisWeek -> stringResource(R.string.date_filter_this_week)
        DateFilter.Today -> stringResource(R.string.date_filter_today)
        DateFilter.Yesterday -> stringResource(R.string.date_filter_yesterday)
    }