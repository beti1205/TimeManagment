package com.example.myapplication.timetracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.timetracker.presentation.components.SubjectDropDown
import com.example.myapplication.utils.convertSecondsToTimeString
import com.example.myapplication.utils.formatToLongTimeStr
import com.example.myapplication.utils.formatToDayMonthDate
import java.time.Instant

@Composable
fun TimeTrackerScreen(
    viewModel: TimeTrackerViewModel = hiltViewModel(),
    onNavigateToTimeSheet: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.workingSubject) {
        if (state.workingSubject.isNotBlank()) {
            viewModel.onSubjectErrorChanged(false)
        }
    }

    TimeTrackerScreen(
        timeAmount = state.timeElapsed.convertSecondsToTimeString(),
        isActive = state.isActive,
        showEndTime = state.showEndTime,
        startTime = state.startTime,
        endTime = state.endTime,
        workingSubject = state.workingSubject,
        chipsEnabled = state.chipsEnabled,
        isSubjectErrorOccurred = state.isSubjectErrorOccurred,
        selectedChangesType = state.selectedChangesType,
        filteredSubjectList = state.filteredSubjectList,
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
    isActive: Boolean,
    showEndTime: Boolean,
    startTime: Instant?,
    endTime: Instant?,
    workingSubject: String,
    chipsEnabled: Boolean,
    isSubjectErrorOccurred: Boolean,
    selectedChangesType: TimeAmountChangesType?,
    filteredSubjectList: List<String>,
    onTimerToggled: () -> Unit = {},
    onResetClicked: () -> Unit = {},
    onSubjectErrorChanged: (Boolean) -> Unit = {},
    onWorkingSubjectChanged: (String) -> Unit = {},
    onNavigateToTimeSheet: () -> Unit,
    onTypeSelected: (TimeAmountChangesType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 32.dp),
    ) {
        Row {
            Text(
                text = stringResource(R.string.time_tracker_start_time),
                modifier = Modifier.weight(0.5f)
            )
            Text(text = startTime?.formatToLongTimeStr() ?: stringResource(R.string.emptyTime))
            Text(
                text = Instant.now().formatToDayMonthDate(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
        Row {
            Text(
                text = stringResource(R.string.time_tracker_end_time),
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = when {
                    showEndTime -> endTime?.formatToLongTimeStr()!!
                    else -> stringResource(R.string.emptyTime)
                }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .align(Alignment.Center)
            )
            Text(
                text = timeAmount,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimeAmountChangesType.entries.forEach { chip ->
                    FilterChip(
                        label = {
                            Text(
                                getTypeName(chip)
                            )
                        },
                        selected = selectedChangesType == chip,
                        onClick = { onTypeSelected(chip) },
                        enabled = chipsEnabled
                    )
                }
            }
            val focusManager = LocalFocusManager.current
            SubjectDropDown(
                subject = workingSubject,
                filteredSubjectList = filteredSubjectList,
                isSubjectChangeEnabled = !isActive,
                isSubjectErrorOccurred = isSubjectErrorOccurred,
                onWorkingSubjectChanged = onWorkingSubjectChanged,
                clearFocus = { focusManager.clearFocus() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (workingSubject.isNotBlank()) {
                            focusManager.clearFocus()
                            onTimerToggled()
                        } else {
                            onSubjectErrorChanged(true)
                        }
                    },
                    shape = CircleShape,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 7.dp)
                ) {
                    if (!isActive) {
                        Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = null)
                    } else {
                        Icon(imageVector = Icons.Outlined.Pause, contentDescription = null)
                    }
                }
                Button(
                    onClick = onResetClicked,
                    shape = CircleShape,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 7.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.RestartAlt, contentDescription = null)
                }
            }
            Button(onClick = onNavigateToTimeSheet) {
                Text(text = stringResource(R.string.timesheet))
            }
        }
    }
}

@Composable
private fun getTypeName(chip: TimeAmountChangesType) =
    when (chip) {
        TimeAmountChangesType.INCREASED_5 -> stringResource(R.string.increased_5)
        TimeAmountChangesType.INCREASED_15 -> stringResource(R.string.increased_15)
        TimeAmountChangesType.INCREASED_30 -> stringResource(R.string.increased_30)
        TimeAmountChangesType.REDUCED_5 -> stringResource(R.string.reduced_5)
        TimeAmountChangesType.REDUCED_15 -> stringResource(R.string.reduced_15)
        TimeAmountChangesType.REDUCED_30 -> stringResource(R.string.reduced_30)
    }