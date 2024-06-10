package com.example.myapplication.timesheet.presentation.components.datefilter

import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.utils.formatToLongDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialogConfirmButton(
    dateRangePickerState: DateRangePickerState,
    onSaveSelectedDate: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis
    val selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis

    TextButton(
        enabled = selectedStartDateMillis != null && selectedEndDateMillis != null,
        onClick = {
            onSaveSelectedDate(
                selectedStartDateMillis!!.formatToLongDate(),
                selectedEndDateMillis!!.formatToLongDate()
            )
            onDismiss()
        }
    ) {
        Text(stringResource(R.string.dialog_confirm_button))
    }
}