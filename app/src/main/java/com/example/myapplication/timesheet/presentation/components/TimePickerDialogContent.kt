package com.example.myapplication.timesheet.presentation.components

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
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
    val initialStartTime = getInitialTime(startTime)
    val startTimePickerState = rememberTimePickerState(
        initialHour = initialStartTime.hour,
        initialMinute = initialStartTime.minute
    )

    val endTime = addEditDialogState?.endTime
    val initialEndTime = getInitialTime(endTime)
    val endTimePickerState = rememberTimePickerState(
        initialHour = initialEndTime.hour,
        initialMinute = initialEndTime.hour,
    )

    val startTimeSeconds = getFinalSeconds(startTime)
    val endTimeSeconds = getFinalSeconds(endTime)

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
                            }${startTimeSeconds}"
                        )

                        else -> onEndTimeChanged(
                            "${getFormattedFinalTime(endTimePickerState.hour)}${
                                getFormattedFinalTime(
                                    endTimePickerState.minute
                                )
                            }${endTimeSeconds}"
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

@SuppressLint("DefaultLocale")
fun getFormattedFinalTime(time: Int): String {
    return if (time < 10) {
        String.format("%02d", time)
    } else {
        time.toString()
    }
}
