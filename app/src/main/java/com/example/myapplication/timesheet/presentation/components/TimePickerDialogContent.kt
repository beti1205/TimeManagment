package com.example.myapplication.timesheet.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import com.example.myapplication.timesheet.presentation.EditIntervalDialogState
import com.example.myapplication.utils.TimePickerDialog

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerDialogContent(
    editDialogState: EditIntervalDialogState?,
    isStartTimePickerSelected: Boolean,
    onStartTimeChanged: (String) -> Unit,
    onEndTimeChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val startTimePickerState = rememberTimePickerState(
        initialHour = editDialogState?.startTime?.substring(0, 2)?.toInt() ?: 12,
        initialMinute = editDialogState?.startTime?.substring(2, 4)?.toInt() ?: 0
    )

    val endTimePickerState = rememberTimePickerState(
        initialHour = editDialogState?.endTime?.substring(0, 2)?.toInt() ?: 12,
        initialMinute = editDialogState?.endTime?.substring(2, 4)?.toInt() ?: 0
    )

    TimePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    when {
                        isStartTimePickerSelected -> onStartTimeChanged(
                            "${startTimePickerState.hour}${startTimePickerState.minute}${
                                editDialogState!!.startTime.substring(
                                    4,
                                    6
                                )
                            }"
                        )

                        else -> onEndTimeChanged(
                            "${endTimePickerState.hour}${endTimePickerState.minute}${
                                editDialogState!!.endTime.substring(
                                    4,
                                    6
                                )
                            }"
                        )
                    }
                    onDismiss()
                }
            ) { Text("OK") }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) { Text("Cancel") }
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