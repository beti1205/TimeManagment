package com.example.myapplication.timesheet.presentation.components.datefilter

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DateRangePicker(
    dateRangePickerState: DateRangePickerState,
    modifier: Modifier
) {
    val dateFormatter = remember { DatePickerDefaults.dateFormatter() }

    androidx.compose.material3.DateRangePicker(
        dateFormatter = dateFormatter,
        state = dateRangePickerState,
        modifier = modifier,
        title = {
            DateRangePickerDefaults.DateRangePickerTitle(
                displayMode = dateRangePickerState.displayMode,
                modifier = Modifier.padding(start = 32.dp, end = 12.dp)
            )
        },
        headline = {
            DateRangePickerDefaults.DateRangePickerHeadline(
                selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis,
                selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis,
                displayMode = dateRangePickerState.displayMode,
                dateFormatter = dateFormatter,
                modifier = Modifier.padding(start = 32.dp, bottom = 12.dp)
            )
        }
    )
}