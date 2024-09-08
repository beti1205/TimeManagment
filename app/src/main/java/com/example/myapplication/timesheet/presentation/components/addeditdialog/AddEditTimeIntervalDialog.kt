package com.example.myapplication.timesheet.presentation.components.addeditdialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState

object DateDefaults {
    const val DATE_MASK = "####-##-##"
    const val DATE_LENGTH = 8
}

@OptIn(ExperimentalMaterial3Api::class)
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
        BasicAlertDialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = headerText)
                    Spacer(modifier = Modifier.height(24.dp))
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

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.align(Alignment.End)) {
                        TextButton(
                            onClick = onDismissRequest
                        ) {
                            Text(stringResource(R.string.edit_dialog_cancel_button))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(
                            onClick = {
                                onSaveClicked()
                                onDismissRequest()
                            },
                            enabled = state.isSaveEnabled
                        ) {
                            Text(stringResource(R.string.edit_dialog_save_button))
                        }
                    }
                }
            }
        }
    }
}
