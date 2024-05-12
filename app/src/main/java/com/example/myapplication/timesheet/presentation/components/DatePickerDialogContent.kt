package com.example.myapplication.timesheet.presentation.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.example.myapplication.utils.formatToDateWithoutColons

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerDialogContent(
    onDismissDatePickerDialog: () -> Unit,
    onDateChanged: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismissDatePickerDialog,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateChanged(
                            it.formatToDateWithoutColons()
                        )
                    }
                    onDismissDatePickerDialog()
                }
            ) { Text("OK") }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissDatePickerDialog
            ) { Text("Cancel") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}