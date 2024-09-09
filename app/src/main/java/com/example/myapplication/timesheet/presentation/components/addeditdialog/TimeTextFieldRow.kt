package com.example.myapplication.timesheet.presentation.components.addeditdialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState

@Composable
fun TimeTextFieldRow(
    state: AddEditIntervalDialogState,
    onStartTimeChanged: (String) -> Unit,
    onStartTimePickerSelected: () -> Unit,
    onEndTimeChanged: (String) -> Unit,
    onEndTimePickerSelected: () -> Unit
) {
    val startTimeLabel = stringResource(R.string.edit_dialog_start_time_text_field)
    val endTimeLabel = stringResource(R.string.edit_dialog_end_time_text_field)

    Row(Modifier.fillMaxWidth()) {
        SetTimeTextField(
            time = state.startTime,
            isWrongTimeError = state.isWrongStartTimeError,
            labelText = startTimeLabel,
            onTimeChanged = onStartTimeChanged,
            modifier = Modifier.weight(1f).semantics { contentDescription = startTimeLabel },
            onTimePickerClicked = onStartTimePickerSelected
        )
        Spacer(modifier = Modifier.width(8.dp))
        SetTimeTextField(
            time = state.endTime,
            isWrongTimeError = state.isWrongEndTimeError,
            labelText = endTimeLabel,
            onTimeChanged = onEndTimeChanged,
            modifier = Modifier.weight(1f).semantics { contentDescription = endTimeLabel },
            onTimePickerClicked = onEndTimePickerSelected
        )
    }
}