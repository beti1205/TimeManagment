package com.example.myapplication.timesheet.presentation.components.addeditdialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState

object DateDefaults {
    const val DATE_MASK = "####-##-##"
    const val DATE_LENGTH = 8
}


@Composable
fun AddEditTimeIntervalDialog(
    state: AddEditIntervalDialogState?,
    headerText: String,
    onSubjectChanged: (String) -> Unit,
    onStartTimeChanged: (String) -> Unit,
    onEndTimeChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onStartTimePickerSelected: () -> Unit,
    onEndTimePickerSelected: () -> Unit,
    onDatePickerSelected: () -> Unit,
    onDateChanged: (String) -> Unit,
    onSaveClicked: () -> Unit = {}
) {
    if (state != null) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = headerText)
            },
            text = {
                AddEditDialogContent(
                    state = state,
                    onDateChanged = onDateChanged,
                    onDatePickerSelected = onDatePickerSelected,
                    onSubjectChanged = onSubjectChanged,
                    onStartTimeChanged = onStartTimeChanged,
                    onStartTimePickerSelected = onStartTimePickerSelected,
                    onEndTimeChanged = onEndTimeChanged,
                    onEndTimePickerSelected = onEndTimePickerSelected
                )
            },
            confirmButton = {
                Button(onClick = {
                    onSaveClicked()
                    onDismissRequest()
                }, enabled = state.isSaveEnabled) {
                    Text(stringResource(R.string.edit_dialog_save_button))
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismissRequest
                ) {
                    Text(stringResource(R.string.edit_dialog_cancel_button))
                }
            }
        )
    }
}
