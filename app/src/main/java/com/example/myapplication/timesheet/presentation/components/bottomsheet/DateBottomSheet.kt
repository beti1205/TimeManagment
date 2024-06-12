package com.example.myapplication.timesheet.presentation.components.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.presentation.DateFilter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DateBottomSheet(
    selectedFilter: DateFilter,
    filterDateOptions: List<DateFilter>,
    onSelectedDate: (DateFilter) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onShowDatePickerRangeClicked: () -> Unit,
    onSearchToggled: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissBottomSheet,
        sheetState = sheetState,
        dragHandle = null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DateBottomSheetHeader(
                sheetState = sheetState,
                onDismissBottomSheet = onDismissBottomSheet,
                onShowDatePickerRangeClicked = onShowDatePickerRangeClicked
            )
            FilterDateOptions(
                filterDateOptions = filterDateOptions,
                selectedFilter = selectedFilter,
                onSelectedDate = onSelectedDate,
                onDismissBottomSheet = onDismissBottomSheet,
                onSearchToggled = onSearchToggled
            )
        }
    }
}

