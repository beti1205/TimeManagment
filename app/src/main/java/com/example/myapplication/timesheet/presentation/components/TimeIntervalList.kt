package com.example.myapplication.timesheet.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.timesheet.presentation.model.DaySection
import kotlinx.coroutines.launch

@Composable
fun TimeIntervalList(
    daySections: List<DaySection>,
    snackbarHostState: SnackbarHostState,
    onEditClicked: (Int) -> Unit,
    onTimeTrackerStarted: (String) -> Unit,
    onResetActionClicked: () -> Unit,
    onDeleteClicked: (Int) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    TimeIntervalList(
        daySections = daySections,
        onDeleteClicked = onDeleteClicked,
        onEditClicked = onEditClicked,
        onTimeTrackerStarted = { subject ->
            onTimeTrackerStarted(subject)
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_time_tracker_started),
                    actionLabel = context.getString(R.string.snackbar_action_reset),
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
}

@Composable
fun TimeIntervalList(
    daySections: List<DaySection>,
    onDeleteClicked: (Int) -> Unit,
    onEditClicked: (Int) -> Unit,
    onTimeTrackerStarted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val collapsedState = remember(daySections) { daySections.map { false }.toMutableStateList() }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier
    ) {
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
                items(items = daySection.timeIntervals, key = { it.id }) { timeInterval ->
                    DaySectionIntervals(
                        timeInterval = timeInterval,
                        onDeleteClicked = onDeleteClicked,
                        onEditClicked = onEditClicked,
                        onTimeTrackerStarted = onTimeTrackerStarted
                    )
                }
            }
        }
    }
}