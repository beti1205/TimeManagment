package com.example.myapplication.timesheet.presentation.components.addeditdialog

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.R
import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerDialog(
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
                            getString(
                                hour = startTimePickerState.hour,
                                minute = startTimePickerState.minute,
                                seconds = addEditDialogState?.startTime?.seconds
                            )
                        )

                        else -> onEndTimeChanged(
                            getString(
                                hour = endTimePickerState.hour,
                                minute = endTimePickerState.minute,
                                seconds = addEditDialogState?.endTime?.seconds
                            )
                        )
                    }
                    onDismiss()
                }
            ) {
                Text(stringResource(id = R.string.dialog_confirm_button))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(id = R.string.dialog_dismiss_button))
            }
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

private fun getString(
    hour: Int,
    minute: Int,
    seconds: String?
) = "${getFormattedFinalTime(hour)}${getFormattedFinalTime(minute)}${seconds ?: "00"}"

@SuppressLint("DefaultLocale")
private fun getFormattedFinalTime(time: Int): String {
    return String.format("%02d", time)
}

@Composable
fun TimePickerDialog(
    title: String = stringResource(R.string.select_time),
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}
