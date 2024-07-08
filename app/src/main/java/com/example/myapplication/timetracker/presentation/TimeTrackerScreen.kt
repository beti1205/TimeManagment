package com.example.myapplication.timetracker.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import com.example.myapplication.timetracker.presentation.components.ActionButtonRow
import com.example.myapplication.timetracker.presentation.components.SubjectDropDown
import com.example.myapplication.timetracker.presentation.components.TimeAdjustmentChips
import com.example.myapplication.timetracker.presentation.components.TimeInterval
import com.example.myapplication.timetracker.presentation.components.Timer
import com.example.myapplication.timetracker.presentation.components.TimesheetButton
import com.example.myapplication.utils.convertSecondsToTimeString
import java.time.Instant

@Composable
fun TimeTrackerScreen(
    viewModel: TimeTrackerViewModel = hiltViewModel(),
    onNavigateToTimeSheet: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    LifecycleStartEffect(viewModel) {
        viewModel.setWorkingSubjectIfTimerHasBeenResumed()
        viewModel.onSubjectErrorChanged(false)

        onStopOrDispose { }
    }

    TimeTrackerScreen(
        timeAmount = state.timeElapsed.convertSecondsToTimeString(),
        timeAmountInMilliseconds = state.timeAmountInMilliseconds,
        isActive = state.isActive,
        showEndTime = state.showEndTime,
        startTime = state.startTime,
        endTime = state.endTime,
        workingSubject = state.workingSubject,
        chipsEnabled = state.chipsEnabled,
        isSubjectErrorOccurred = state.isSubjectErrorOccurred,
        selectedTimeAdjustment = state.selectedTimeAdjustment,
        filteredSubjects = state.filteredSubjects,
        onTimerToggled = viewModel::toggleTimer,
        onResetClicked = viewModel::reset,
        onSubjectErrorChanged = viewModel::onSubjectErrorChanged,
        onWorkingSubjectChanged = viewModel::onWorkingSubjectChanged,
        onNavigateToTimeSheet = onNavigateToTimeSheet,
        onTypeSelected = viewModel::onTypeSelected
    )
}

@Composable
fun TimeTrackerScreen(
    timeAmount: String,
    timeAmountInMilliseconds: Long,
    isActive: Boolean,
    showEndTime: Boolean,
    startTime: Instant?,
    endTime: Instant?,
    workingSubject: String,
    chipsEnabled: Boolean,
    isSubjectErrorOccurred: Boolean,
    selectedTimeAdjustment: TimeAdjustment?,
    filteredSubjects: List<String>,
    onTimerToggled: () -> Unit = {},
    onResetClicked: () -> Unit = {},
    onSubjectErrorChanged: (Boolean) -> Unit = {},
    onWorkingSubjectChanged: (String) -> Unit = {},
    onNavigateToTimeSheet: () -> Unit,
    onTypeSelected: (TimeAdjustment) -> Unit
) {
    TimeInterval(
        startTime = startTime,
        showEndTime = showEndTime,
        endTime = endTime
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Timer(timeAmount = timeAmount, timeAmountInMilliseconds = timeAmountInMilliseconds)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimeAdjustmentChips(
                selectedTimeAdjustment = selectedTimeAdjustment,
                onTypeSelected = onTypeSelected,
                chipsEnabled = chipsEnabled
            )
            val focusManager = LocalFocusManager.current
            SubjectDropDown(
                subject = workingSubject,
                filteredSubjects = filteredSubjects,
                isSubjectChangeEnabled = !isActive,
                isSubjectErrorOccurred = isSubjectErrorOccurred,
                onWorkingSubjectChanged = onWorkingSubjectChanged,
                clearFocus = { focusManager.clearFocus() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            ActionButtonRow(
                workingSubject = workingSubject,
                clearDropDownFocus = { focusManager.clearFocus() },
                onTimerToggled = onTimerToggled,
                onSubjectErrorChanged = onSubjectErrorChanged,
                isActive = isActive,
                onResetClicked = onResetClicked
            )
            TimesheetButton(onNavigateToTimeSheet)
        }
    }
}
