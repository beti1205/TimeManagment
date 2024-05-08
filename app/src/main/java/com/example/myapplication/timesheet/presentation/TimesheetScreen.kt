package com.example.myapplication.timesheet.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timesheet.domain.usecases.DaySection
import com.example.myapplication.timesheet.presentation.components.DeleteDialog
import com.example.myapplication.timesheet.presentation.components.EditDialog
import com.example.myapplication.timesheet.presentation.components.TimeIntervalsList
import com.example.myapplication.timesheet.presentation.components.TimePickerDialogContent
import com.example.myapplication.utils.formatToDateWithoutColons
import com.example.myapplication.utils.formatToLongDate
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun TimesheetScreen(
    viewModel: TimesheetViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    TimesheetScreen(
        daySections = state.daySections,
        editDialogState = state.editIntervalDialogState,
        onDeleteTimeInterval = viewModel::deleteTimeInterval,
        onSaveClicked = viewModel::onSaveClicked,
        onSubjectChanged = viewModel::onSubjectChanged,
        onStartTimeChanged = viewModel::onStartTimeChanged,
        onEndTimeChanged = viewModel::onEndTimeChanged,
        onEditClicked = viewModel::onEditClicked,
        onDismissEditDialog = viewModel::onDismissEditDialog,
        onTimeTrackerStarted = viewModel::start,
        onResetActionClicked = viewModel::reset,
        onDateChanged = viewModel::onDateChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimesheetScreen(
    daySections: List<DaySection>,
    editDialogState: EditIntervalDialogState?,
    onDeleteTimeInterval: (Int) -> Unit,
    onSaveClicked: () -> Unit,
    onSubjectChanged: (String) -> Unit,
    onStartTimeChanged: (String) -> Unit,
    onEndTimeChanged: (String) -> Unit,
    onEditClicked: (Int) -> Unit,
    onDismissEditDialog: () -> Unit,
    onTimeTrackerStarted: (String) -> Unit,
    onResetActionClicked: () -> Unit,
    onDateChanged: (String) -> Unit,
) {
    var idToBeDeleted: Int? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var time: String? by remember { mutableStateOf(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (time != null) {
        TimePickerDialogContent(
            editDialogState = editDialogState,
            isStartTimePickerSelected = time == editDialogState?.startTime,
            onStartTimeChanged = onStartTimeChanged,
            onEndTimeChanged = onEndTimeChanged,
            onDismiss = { time = null }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateChanged(
                                it.formatToDateWithoutColons()
                            )
                        }
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }

        }
    ) { contentPadding ->
        TimeIntervalsList(
            daySections = daySections,
            onDeleteClicked = { idToBeDeleted = it },
            onEditClicked = onEditClicked,
            onTimeTrackerStarted = { subject ->
                onTimeTrackerStarted(subject)
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "Time tracker started",
                        actionLabel = "Reset",
                        duration = SnackbarDuration.Short
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            onResetActionClicked()
                        }

                        SnackbarResult.Dismissed -> Unit
                    }

                }

            },
            modifier = Modifier.padding(contentPadding)
        )

        editDialogState?.let { state ->
            EditDialog(
                state = state,
                onDismissRequest = onDismissEditDialog,
                onSaveClicked = onSaveClicked,
                onStartTimeChanged = onStartTimeChanged,
                onEndTimeChanged = onEndTimeChanged,
                onSubjectChanged = onSubjectChanged,
                onStartTimePickerSelected = { time = editDialogState.startTime },
                onEndTimePickerSelected = { time = editDialogState.endTime },
                onDatePickerSelected = { showDatePicker = true },
                onDateChanged = onDateChanged
            )
        }

        idToBeDeleted?.let { id ->
            DeleteDialog(
                onDismissRequest = { idToBeDeleted = null },
                onDeleteConfirm = { onDeleteTimeInterval(id) }
            )
        }
    }
}

@Preview
@Composable
fun TimeSheetScreenPreview() {
    TimesheetScreen(
        daySections = listOf(
            DaySection(
                headerDate = "Thu, Nov30",
                headerTimeAmount = "01:59:06",
                timeIntervals = listOf(
                    TimeTrackerInterval(
                        id = 1,
                        timeElapsed = 7,
                        workingSubject = "Upgrade SDK",
                        date = "Thu, Nov30",
                        startTime = Instant.now(),
                        endTime = Instant.now(),
                        additionalDays = "0"
                    )
                )
            )
        ),
        editDialogState = EditIntervalDialogState(
            id = 1,
            subject = "Upgrade SDK",
            startTime = "01:59:06",
            endTime = "02:59:06",
            date = "Thu, Nov30"
        ),
        onDeleteTimeInterval = {},
        onSaveClicked = {},
        onSubjectChanged = {},
        onStartTimeChanged = {},
        onEndTimeChanged = {},
        onEditClicked = {},
        onDismissEditDialog = {},
        onTimeTrackerStarted = {},
        onResetActionClicked = {},
        onDateChanged = {}
    )
}
