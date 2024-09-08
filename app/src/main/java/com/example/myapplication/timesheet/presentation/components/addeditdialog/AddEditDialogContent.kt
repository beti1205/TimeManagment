package com.example.myapplication.timesheet.presentation.components.addeditdialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState

@Composable
fun AddEditDialogContent(
    state: AddEditIntervalDialogState,
    onDateChanged: (String) -> Unit,
    onDatePickerSelected: () -> Unit,
    onSubjectChanged: (String) -> Unit,
    onStartTimeChanged: (String) -> Unit,
    onStartTimePickerSelected: () -> Unit,
    onEndTimeChanged: (String) -> Unit,
    onEndTimePickerSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        DateTextField(
            date = state.date,
            isWrongDateError = state.isWrongDateError,
            onDateChanged = onDateChanged,
            onDatePickerSelected = onDatePickerSelected
        )
        Spacer(modifier = Modifier.height(8.dp))
        SubjectTextField(subject = state.subject, onSubjectChanged = onSubjectChanged)
        Spacer(modifier = Modifier.height(16.dp))
        TimeTextFieldRow(
            state = state,
            onStartTimeChanged = onStartTimeChanged,
            onStartTimePickerSelected = onStartTimePickerSelected,
            onEndTimeChanged = onEndTimeChanged,
            onEndTimePickerSelected = onEndTimePickerSelected
        )
        Spacer(
            Modifier.height(40.dp)
        )
    }
}