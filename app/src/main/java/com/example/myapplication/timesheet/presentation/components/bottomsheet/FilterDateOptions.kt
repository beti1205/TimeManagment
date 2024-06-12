package com.example.myapplication.timesheet.presentation.components.bottomsheet

import androidx.compose.runtime.Composable
import com.example.myapplication.timesheet.presentation.DateFilter

@Composable
fun FilterDateOptions(
    filterDateOptions: List<DateFilter>,
    selectedFilter: DateFilter,
    onSelectedDate: (DateFilter) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onSearchToggled: () -> Unit
) {
    filterDateOptions.forEach { date ->
        DateRadioButtonRow(
            date = date,
            selectedFilter = selectedFilter,
            onSelectedDate = onSelectedDate,
            onDismissBottomSheet = onDismissBottomSheet,
            onSearchToggled = onSearchToggled
        )
    }
}