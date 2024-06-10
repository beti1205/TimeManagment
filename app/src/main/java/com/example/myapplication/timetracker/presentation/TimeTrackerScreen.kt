package com.example.myapplication.timetracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.timetracker.domain.stopwatch.formatTime
import com.example.myapplication.timetracker.domain.stopwatch.toTime
import com.example.myapplication.timetracker.presentation.components.SubjectDropDown

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
        timeAmount = state.timeElapsed.toTime().formatTime(),
        isActive = state.isActive,
        workingSubject = state.workingSubject,
        isSubjectErrorOccurred = state.isSubjectErrorOccurred,
        filteredSubjectList = state.filteredSubjectList,
        onTimerToggled = viewModel::toggleTimer,
        onResetClicked = viewModel::reset,
        onSubjectErrorChanged = viewModel::onSubjectErrorChanged,
        onWorkingSubjectChanged = viewModel::onWorkingSubjectChanged,
        onNavigateToTimeSheet = onNavigateToTimeSheet
    )
}

@Composable
fun TimeTrackerScreen(
    timeAmount: String,
    isActive: Boolean,
    workingSubject: String,
    isSubjectErrorOccurred: Boolean,
    filteredSubjectList: List<String>,
    onTimerToggled: () -> Unit = {},
    onResetClicked: () -> Unit = {},
    onSubjectErrorChanged: (Boolean) -> Unit = {},
    onWorkingSubjectChanged: (String) -> Unit = {},
    onNavigateToTimeSheet: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .clip(CircleShape)
                    .background(Color.LightGray)
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
        val focusManager = LocalFocusManager.current
        SubjectDropDown(
            subject = workingSubject,
            filteredSubjectList = filteredSubjectList,
            isSubjectChangeEnabled = !isActive,
            isSubjectErrorOccurred = isSubjectErrorOccurred,
            onWorkingSubjectChanged = onWorkingSubjectChanged,
            clearFocus = { focusManager.clearFocus() }
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