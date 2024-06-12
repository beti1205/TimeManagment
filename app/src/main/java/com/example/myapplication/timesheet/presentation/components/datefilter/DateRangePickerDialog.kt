package com.example.myapplication.timesheet.presentation.components.datefilter

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DateRangePickerDialog(
    onDismiss: () -> Unit,
    onSaveSelectedDate: (String, String) -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DateRangePickerDialogConfirmButton(
                dateRangePickerState = dateRangePickerState,
                onSaveSelectedDate = onSaveSelectedDate,
                onDismiss = onDismiss
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.dialog_dismiss_button))
            }
        }
    ) {
        DateRangePicker(
            dateRangePickerState = dateRangePickerState,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .weight(1f, false)
        )
    }
}
