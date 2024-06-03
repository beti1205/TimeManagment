package com.example.myapplication.timesheet.presentation.components.datefilter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myapplication.timesheet.presentation.DateFilterType

@Composable
fun DateFilter(
    selectedFilter: DateFilterType,
    filterDateOptions: List<DateFilterType>,
    onSelectedFilterChanged: (DateFilterType) -> Unit,
    onSearchToggled: () -> Unit = {}
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    var showDateRangePicker by remember {
        mutableStateOf(false)
    }
    if (showDateRangePicker) {
        DateRangePickerDialog(
            onDismiss = { showDateRangePicker = false },
            onSaveSelectedDate = { startDate, endDate ->
                onSelectedFilterChanged(DateFilterType.CustomRange(startDate, endDate))
                showBottomSheet = false
                onSearchToggled()
            }
        )
    }

    if (showBottomSheet) {
        DateBottomSheet(
            selectedFilter = selectedFilter,
            filterDateOptions = filterDateOptions,
            onSelectedDate = { onSelectedFilterChanged(it) },
            onDismissBottomSheet = { showBottomSheet = false },
            onShowDatePickerRangeClicked = { showDateRangePicker = true },
            onSearchToggled = onSearchToggled
        )
    }

    DateChip(
        selectedFilter = selectedFilter,
        onShowBottomSheetClicked = { showBottomSheet = true }
    )
}