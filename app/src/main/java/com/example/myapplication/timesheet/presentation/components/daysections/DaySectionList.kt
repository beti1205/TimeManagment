package com.example.myapplication.timesheet.presentation.components.daysections

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
fun DaySectionList(
    daySections: List<DaySection>,
    snackbarHostState: SnackbarHostState,
    onEditClicked: (String) -> Unit,
    onTimeTrackerStarted: (String) -> Unit,
    onResetActionClicked: () -> Unit,
    onDeleteClicked: (String) -> Unit,
    onIntervalsSectionExpanded: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    DaySectionList(
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
        onIntervalsSectionExpanded = onIntervalsSectionExpanded

    )
}

@Composable
fun DaySectionList(
    daySections: List<DaySection>,
    onDeleteClicked: (String) -> Unit,
    onEditClicked: (String) -> Unit,
    onTimeTrackerStarted: (String) -> Unit,
    onIntervalsSectionExpanded: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val daySectionsCollapsedState =
        remember(daySections) { daySections.map { false }.toMutableStateList() }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier
    ) {
        daySections.forEachIndexed { i, daySection ->
            val collapsed = daySectionsCollapsedState[i]

            item(key = "header_$i") {
                DaySectionHeader(
                    daySection = daySection,
                    collapsed = collapsed,
                    onDaySectionCollapsed = { daySectionsCollapsedState[i] = !collapsed }
                )
            }
            if (!collapsed) {
                daySection.timeIntervalsSections.forEachIndexed { index, timeIntervalsSection ->
                    val groupedIntervalsSectionHeader = timeIntervalsSection.groupedIntervalsSectionHeader

                    if (groupedIntervalsSectionHeader != null) {
                        item(groupedIntervalsSectionHeader.id) {
                            DaySectionInterval(
                                timeInterval = groupedIntervalsSectionHeader,
                                numberOfIntervals = timeIntervalsSection.numberOfIntervals,
                                onDeleteClicked = {},
                                onEditClicked = {},
                                onTimeTrackerStarted = {},
                                onIntervalsSectionExpanded = onIntervalsSectionExpanded
                            )
                        }
                    }

                    if (timeIntervalsSection.expanded || groupedIntervalsSectionHeader == null) {
                        items(
                            items = timeIntervalsSection.timeIntervals,
                            key = { it.id }) { timeInterval ->
                            DaySectionInterval(
                                timeInterval = timeInterval,
                                onDeleteClicked = onDeleteClicked,
                                onEditClicked = onEditClicked,
                                onTimeTrackerStarted = onTimeTrackerStarted,
                                onIntervalsSectionExpanded = onIntervalsSectionExpanded
                            )
                        }
                    }
                }
            }
        }
    }
}
