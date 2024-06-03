package com.example.myapplication.timesheet.presentation.components

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.timesheet.presentation.AddEditIntervalDialogState
import com.example.myapplication.utils.TimePickerDialog

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerDialogContent(
    addEditDialogState: AddEditIntervalDialogState?,
    isStartTimePickerSelected: Boolean,
    onStartTimeChanged: (String) -> Unit,
    onEndTimeChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val startTime = addEditDialogState?.startTime
    val startTimePickerState = rememberTimePickerState(
        initialHour = startTime?.hours?.toInt() ?: 12,
        initialMinute = startTime?.minutes?.toInt() ?: 0
    )

    val endTime = addEditDialogState?.endTime
    val endTimePickerState = rememberTimePickerState(
        initialHour = endTime?.hours?.toInt() ?: 12,
        initialMinute = endTime?.minutes?.toInt() ?: 0
    )

    TimePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    when {
                        isStartTimePickerSelected -> onStartTimeChanged(
                            "${getFormattedFinalTime(startTimePickerState.hour)}${
                                getFormattedFinalTime(
                                    startTimePickerState.minute
                                )
                            }${addEditDialogState?.startTime?.seconds ?: "00"}"
                        )

                        else -> onEndTimeChanged(
                            "${getFormattedFinalTime(endTimePickerState.hour)}${
                                getFormattedFinalTime(
                                    endTimePickerState.minute
                                )
                            }${addEditDialogState?.endTime?.seconds ?: "00"}"
                        )
                    }
                    onDismiss()
                }
            ) { Text(stringResource(id = R.string.dialog_confirm_button)) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) { Text(stringResource(id = R.string.dialog_dismiss_button)) }
        },
    ) {
        TimePicker(
            state = if (isStartTimePickerSelected) {
                startTimePickerState
            } else {
                endTimePickerState
            }
        )
    }
}

@SuppressLint("DefaultLocale")
fun getFormattedFinalTime(time: Int): String {
    return String.format("%02d", time)
}
