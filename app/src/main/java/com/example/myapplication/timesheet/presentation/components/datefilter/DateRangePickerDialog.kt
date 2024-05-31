package com.example.myapplication.timesheet.presentation.components.datefilter

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.utils.formatToLongDate

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DateRangePickerDialog(onDismiss: () -> Unit, onSaveSelectedDate: (String) -> Unit) {
    val dateRangePickerState = rememberDateRangePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            val selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis
            val selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis
            TextButton(
                enabled = selectedStartDateMillis != null && selectedEndDateMillis != null,
                onClick = {
                    onSaveSelectedDate(
                        "${
                            selectedStartDateMillis?.formatToLongDate()
                        }..${selectedEndDateMillis?.formatToLongDate()}"
                    )
                    onDismiss()
                }
            ) { Text("OK") }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) { Text("Cancel") }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .weight(1f, false)
        )
    }
}