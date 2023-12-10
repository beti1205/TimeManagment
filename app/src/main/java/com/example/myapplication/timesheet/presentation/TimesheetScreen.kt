package com.example.myapplication.timesheet.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timesheet.domain.usecases.DaySection
import com.example.myapplication.timesheet.presentation.components.DaySectionHeader
import com.example.myapplication.timesheet.presentation.components.DaySectionIntervals
import com.example.myapplication.timesheet.presentation.components.DeleteDialog
import com.example.myapplication.timesheet.presentation.components.EditDialog
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
        onDismissEditDialog = viewModel::onDismissEditDialog
    )
}

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
    onDismissEditDialog: () -> Unit
) {
    var idToBeDeleted: Int? by remember { mutableStateOf(null) }

    TimeIntervalsList(
        daySections = daySections,
        onDeleteClicked = { idToBeDeleted = it },
        onEditClicked = onEditClicked
    )

    editDialogState?.let { state ->
        EditDialog(
            state = state,
            onDismissRequest = onDismissEditDialog,
            onSaveClicked = onSaveClicked,
            onStartTimeChanged = onStartTimeChanged,
            onEndTimeChanged = onEndTimeChanged,
            onSubjectChanged = onSubjectChanged
        )
    }

    idToBeDeleted?.let { id ->
        DeleteDialog(
            onDismissRequest = { idToBeDeleted = null },
            onDeleteConfirm = { onDeleteTimeInterval(id) }
        )
    }
}

@Composable
private fun TimeIntervalsList(
    daySections: List<DaySection>,
    onDeleteClicked: (Int) -> Unit,
    onEditClicked: (Int) -> Unit
) {
    val collapsedState = remember(daySections) { daySections.map { false }.toMutableStateList() }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        daySections.forEachIndexed { i, daySection ->
            val collapsed = collapsedState[i]

            item(key = "header_$i") {
                DaySectionHeader(
                    daySection = daySection,
                    index = i,
                    collapsed = collapsed,
                    collapsedState = collapsedState
                )
            }

            if (!collapsed) {
                items(daySection.timeIntervals) { timeInterval ->
                    DaySectionIntervals(
                        timeInterval = timeInterval,
                        onDeleteClicked = onDeleteClicked,
                        onEditClicked = onEditClicked
                    )
                }
            }
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
                        endTime = Instant.now()
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
        onDismissEditDialog = {}
    )
}
