package com.example.myapplication.timesheet.presentation.components.datefilter

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.utils.formatToLongDate

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DateRangePickerDialog(onDismiss: () -> Unit, onSaveSelectedDate: (String, String) -> Unit) {
    val dateRangePickerState = rememberDateRangePickerState()
    val dateFormatter = remember { DatePickerDefaults.dateFormatter() }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
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
            ) { Text(stringResource(R.string.dialog_confirm_button)) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) { Text(stringResource(R.string.dialog_dismiss_button)) }
        }
    ) {
        DateRangePicker(
            dateFormatter = dateFormatter,
            state = dateRangePickerState,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .weight(1f, false),
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
}
