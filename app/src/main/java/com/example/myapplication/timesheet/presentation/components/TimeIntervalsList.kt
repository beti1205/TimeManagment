package com.example.myapplication.timesheet.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.domain.usecases.DaySection

@Composable
fun TimeIntervalsList(
    daySections: List<DaySection>,
    onDeleteClicked: (Int) -> Unit,
    onEditClicked: (Int) -> Unit,
    onTimeTrackerStarted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val collapsedState = remember(daySections) { daySections.map { false }.toMutableStateList() }

    LazyColumn(contentPadding = PaddingValues(16.dp), modifier = modifier) {
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
                        onEditClicked = onEditClicked,
                        onTimeTrackerStarted = onTimeTrackerStarted
                    )
                }
            }
        }
    }
}