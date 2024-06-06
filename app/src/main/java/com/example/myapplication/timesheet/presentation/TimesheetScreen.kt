package com.example.myapplication.timesheet.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timesheet.presentation.components.AddEditTimeIntervalDialog
import com.example.myapplication.timesheet.presentation.components.datefilter.DateFilter
import com.example.myapplication.timesheet.presentation.components.DatePickerDialogContent
import com.example.myapplication.timesheet.presentation.components.DeleteDialog
import com.example.myapplication.timesheet.presentation.components.SearchBar
import com.example.myapplication.timesheet.presentation.components.TimeIntervalsList
import com.example.myapplication.timesheet.presentation.components.TimePickerDialogContent
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun TimesheetScreen(
    viewModel: TimesheetViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    TimesheetScreen(
        state = state,
        filterDateOptions = viewModel.filterDateOptions,
        onDeleteTimeInterval = viewModel::deleteTimeInterval,
        onSaveClicked = viewModel::onSaveClicked,
        onSubjectChanged = viewModel::onSubjectChanged,
        onStartTimeChanged = viewModel::onStartTimeChanged,
        onEndTimeChanged = viewModel::onEndTimeChanged,
        onEditClicked = viewModel::onEditClicked,
        onDismissAddEditDialog = viewModel::onDismissEditDialog,
        onTimeTrackerStarted = viewModel::start,
        onResetActionClicked = viewModel::reset,
        onDateChanged = viewModel::onDateChanged,
        onAddClicked = viewModel::onAddClicked,
        onSearchTextChanged = viewModel::onSearchTextChanged,
        onSearchToggled = viewModel::onSearchToggled,
        onSubjectSelected = viewModel::onSubjectSelected,
        onSelectedFilterChanged = viewModel::onSelectedFilterChanged
    )
}

@Composable
fun TimesheetScreen(
    state: TimesheetScreenState,
    filterDateOptions: List<DateFilterType>,
    onDeleteTimeInterval: (Int) -> Unit,
    onSaveClicked: () -> Unit,
    onSubjectChanged: (String) -> Unit,
    onStartTimeChanged: (String) -> Unit,
    onEndTimeChanged: (String) -> Unit,
    onEditClicked: (Int) -> Unit,
    onDismissAddEditDialog: () -> Unit,
    onTimeTrackerStarted: (String) -> Unit,
    onResetActionClicked: () -> Unit,
    onDateChanged: (String) -> Unit,
    onAddClicked: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onSearchToggled: () -> Unit,
    onSubjectSelected: (String) -> Unit,
    onSelectedFilterChanged: (DateFilterType) -> Unit
) {
    var idToBeDeleted: Int? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isStartTimePickerSelected: Boolean? by remember { mutableStateOf(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    isStartTimePickerSelected?.let {
        TimePickerDialogContent(
            addEditDialogState = state.addEditIntervalDialogState,
            isStartTimePickerSelected = it,
            onStartTimeChanged = onStartTimeChanged,
            onEndTimeChanged = onEndTimeChanged,
            onDismiss = { isStartTimePickerSelected = null }
        )
    }

    if (showDatePicker) {
        DatePickerDialogContent(
            onDismissDatePickerDialog = { showDatePicker = false },
            onDateChanged = onDateChanged
        )
    }

    state.addEditIntervalDialogState?.let { state ->
        AddEditTimeIntervalDialog(
            state = state,
            headerText = when {
                state.id != null -> stringResource(id = R.string.edit_menu_item_label)
                else -> stringResource(R.string.add_dialog_header)
            },
            onDismissRequest = onDismissAddEditDialog,
            onSaveClicked = onSaveClicked,
            onStartTimeChanged = onStartTimeChanged,
            onEndTimeChanged = onEndTimeChanged,
            onSubjectChanged = onSubjectChanged,
            onStartTimePickerSelected = { isStartTimePickerSelected = true },
            onEndTimePickerSelected = { isStartTimePickerSelected = false },
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


    Scaffold(
        topBar = {
            SearchBar(
                isSearching = state.searchBarState.isSearching,
                searchText = state.searchBarState.searchText,
                subjects = state.subjects,
                selectedFilter = state.selectedFilter,
                filterDateOptions = filterDateOptions,
                onSearchTextChanged = onSearchTextChanged,
                onSearchToggled = onSearchToggled,
                onSubjectSelected = onSubjectSelected,
                onSelectedFilterChanged = onSelectedFilterChanged
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClicked) {
                Icon(Icons.Default.Add, contentDescription = null)
            }

        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            if (state.selectedFilter != DateFilterType.All) {
                DateFilter(
                    selectedFilter = state.selectedFilter,
                    filterDateOptions = filterDateOptions,
                    onSelectedFilterChanged = onSelectedFilterChanged
                )
            }
            if (state.daySections.isNotEmpty()) {
                TimeIntervalsList(
                    daySections = state.daySections,
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
                )
            } else if (state.isNotFindingResults) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Rounded.SearchOff, contentDescription = null)
                    Text(text = stringResource(R.string.search_no_matches_info))
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
fun TimeSheetScreenPreview() {
    TimesheetScreen(
        state = TimesheetScreenState(
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
            addEditIntervalDialogState = AddEditIntervalDialogState(
                id = 1,
                subject = "Upgrade SDK",
                startTime = null,
                endTime = null,
                date = "Thu, Nov30"
            ),
            searchBarState = SearchBarState(),
            subjects = emptyList()
        ),
        filterDateOptions = emptyList(),
        onDeleteTimeInterval = {},
        onSaveClicked = {},
        onSubjectChanged = {},
        onStartTimeChanged = {},
        onEndTimeChanged = {},
        onEditClicked = {},
        onDismissAddEditDialog = {},
        onTimeTrackerStarted = {},
        onResetActionClicked = {},
        onDateChanged = {},
        onAddClicked = {},
        onSearchToggled = {},
        onSearchTextChanged = {},
        onSubjectSelected = {},
        onSelectedFilterChanged = {}
    )
}
