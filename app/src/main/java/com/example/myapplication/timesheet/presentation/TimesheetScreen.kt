package com.example.myapplication.timesheet.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timesheet.presentation.components.DeleteDialog
import com.example.myapplication.timesheet.presentation.components.EmptyStateScreen
import com.example.myapplication.timesheet.presentation.components.NoSearchResultsScreen
import com.example.myapplication.timesheet.presentation.components.ScaffoldFloatingActionButton
import com.example.myapplication.timesheet.presentation.components.SearchBar
import com.example.myapplication.timesheet.presentation.components.addeditdialog.AddEditTimeIntervalDialog
import com.example.myapplication.timesheet.presentation.components.addeditdialog.DatePickerDialog
import com.example.myapplication.timesheet.presentation.components.addeditdialog.TimePickerDialog
import com.example.myapplication.timesheet.presentation.components.addeditdialog.addEditIntervalDialogHeader
import com.example.myapplication.timesheet.presentation.components.datefilter.DateFilter
import com.example.myapplication.timesheet.presentation.components.daysections.DaySectionList
import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState
import com.example.myapplication.timesheet.presentation.model.DaySection
import com.example.myapplication.timesheet.presentation.model.SearchBarState
import com.example.myapplication.timesheet.presentation.model.TimeIntervalsSection
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
        onSelectedFilterChanged = viewModel::onSelectedFilterChanged,
        onIntervalsSectionExpanded = viewModel::onIntervalsSectionExpanded
    )
}

@Composable
fun TimesheetScreen(
    state: TimesheetScreenState,
    filterDateOptions: List<DateFilter>,
    onDeleteTimeInterval: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onSubjectChanged: (String) -> Unit,
    onStartTimeChanged: (String) -> Unit,
    onEndTimeChanged: (String) -> Unit,
    onEditClicked: (String) -> Unit,
    onDismissAddEditDialog: () -> Unit,
    onTimeTrackerStarted: (String) -> Unit,
    onResetActionClicked: () -> Unit,
    onDateChanged: (String) -> Unit,
    onAddClicked: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onSearchToggled: () -> Unit,
    onSubjectSelected: (String) -> Unit,
    onSelectedFilterChanged: (DateFilter) -> Unit,
    onIntervalsSectionExpanded:(String) -> Unit
) {
    var idToBeDeleted: String? by remember { mutableStateOf(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    var isStartTimePickerSelected: Boolean? by remember { mutableStateOf(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    isStartTimePickerSelected?.let {
        TimePickerDialog(
            addEditDialogState = state.addEditIntervalDialogState,
            isStartTimePickerSelected = it,
            onStartTimeChanged = onStartTimeChanged,
            onEndTimeChanged = onEndTimeChanged,
            onDismiss = { isStartTimePickerSelected = null }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissDatePickerDialog = { showDatePicker = false },
            onDateChanged = onDateChanged
        )
    }

    state.addEditIntervalDialogState?.let { state ->
        AddEditTimeIntervalDialog(
            state = state,
            headerText = addEditIntervalDialogHeader(state.id),
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
            ScaffoldFloatingActionButton(onAddClicked = onAddClicked)

        },
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) { innerPadding ->
        Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {
            Spacer(Modifier.height(innerPadding.calculateTopPadding()))
            if (state.selectedFilter != DateFilter.All) {
                DateFilter(
                    selectedFilter = state.selectedFilter,
                    filterDateOptions = filterDateOptions,
                    onSelectedFilterChanged = onSelectedFilterChanged
                )
            }
            when {
                state.daySections.isNotEmpty() -> {
                    DaySectionList(
                        daySections = state.daySections,
                        snackbarHostState = snackbarHostState,
                        onEditClicked = onEditClicked,
                        onTimeTrackerStarted = onTimeTrackerStarted,
                        onResetActionClicked = onResetActionClicked,
                        onDeleteClicked = { idToBeDeleted = it },
                        onIntervalsSectionExpanded = onIntervalsSectionExpanded
                    )
                }

                state.isNotFindingResults -> {
                    NoSearchResultsScreen()
                }

                else -> {
                    EmptyStateScreen()
                }
            }
        }
    }
}

@Preview
@Composable
fun TimeSheetScreenPreview() {
    val interval = TimeTrackerInterval(
        id = "1",
        timeElapsed = 7,
        workingSubject = "Upgrade SDK",
        date = "Thu, Nov30",
        startTime = Instant.now(),
        endTime = Instant.now()
    )
    val timeIntervals = listOf(
        interval
    )
    TimesheetScreen(
        state = TimesheetScreenState(
            daySections = listOf(
                DaySection(
                    dateHeader = "Thu, Nov30",
                    timeAmountHeader = "01:59:06",
                    timeIntervalsSections = listOf(
                        TimeIntervalsSection(
                            numberOfIntervals = 1,
                            groupedIntervalsSectionHeader = interval,
                            timeIntervals = timeIntervals
                        )
                    )
                )
            ),
            addEditIntervalDialogState = AddEditIntervalDialogState(
                id = "1",
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
        onSelectedFilterChanged = {},
        onIntervalsSectionExpanded = {}
    )
}
