package com.example.myapplication.timesheet.presentation.components.bottomsheet

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateBottomSheetHeader(
    sheetState: SheetState,
    onDismissBottomSheet: () -> Unit,
    onShowDatePickerRangeClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        DateBottomSheetCloseButton(
            sheetState = sheetState,
            onDismissBottomSheet = onDismissBottomSheet
        )
        Spacer(modifier = Modifier.width(8.dp))
        DateBottomSheetTitle()
        Spacer(modifier = Modifier.weight(1f))
        DateBottomSheetCustomOption(onShowDatePickerRangeClicked)
    }
}