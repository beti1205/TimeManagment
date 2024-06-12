package com.example.myapplication.timesheet.presentation.components.addeditdialog

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.utils.formatToDateWithoutDash

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerDialog(
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
                            it.formatToDateWithoutDash()
                        )
                    }
                    onDismissDatePickerDialog()
                }
            ) {
                Text(stringResource(id = R.string.dialog_confirm_button))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissDatePickerDialog
            ) {
                Text(stringResource(id = R.string.dialog_dismiss_button))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}