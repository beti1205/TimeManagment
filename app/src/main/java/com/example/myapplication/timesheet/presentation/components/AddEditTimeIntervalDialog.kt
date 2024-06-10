package com.example.myapplication.timesheet.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState
import com.example.myapplication.utils.MaskVisualTransformation

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
                Column {
                    OutlinedTextField(
                        textStyle = TextStyle.Default.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light
                        ),
                        value = state.date,
                        onValueChange = { if (it.length <= DateDefaults.DATE_LENGTH) onDateChanged(it) },
                        label = {
                            Text(
                                text = stringResource(R.string.edit_dialog_start_date_label),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done
                        ),
                        isError = state.isWrongDateError,
                        supportingText = {
                            if (state.isWrongDateError) {
                                Text(stringResource(R.string.edit_dialog_error_supporting_text))
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = onDatePickerSelected) {
                                Icon(
                                    imageVector = Icons.Outlined.DateRange,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = MaskVisualTransformation(DateDefaults.DATE_MASK),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        textStyle = TextStyle.Default.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light
                        ),
                        value = state.subject,
                        onValueChange = { onSubjectChanged(it) },
                        label = {
                            Text(
                                text = stringResource(R.string.subject),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth()) {
                        SetTimeTextField(
                            time = state.startTime,
                            isWrongTimeError = state.isWrongStartTimeError,
                            labelText = stringResource(R.string.edit_dialog_start_time_text_field),
                            onTimeChanged = onStartTimeChanged,
                            modifier = Modifier.weight(1f),
                            onTimePickerClicked = onStartTimePickerSelected
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SetTimeTextField(
                            time = state.endTime,
                            isWrongTimeError = state.isWrongEndTimeError,
                            labelText = stringResource(R.string.edit_dialog_end_time_text_field),
                            onTimeChanged = onEndTimeChanged,
                            modifier = Modifier.weight(1f),
                            onTimePickerClicked = onEndTimePickerSelected
                        )
                    }
                }
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
