package com.example.myapplication.timesheet.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timesheet.domain.usecases.DaySection
import com.example.myapplication.timesheet.presentation.components.DaySectionHeader
import com.example.myapplication.timesheet.presentation.components.DaySectionIntervals
import java.time.Instant

@Composable
fun TimesheetScreen(
    viewModel: TimesheetViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    TimesheetScreen(
        daySections = state.daySections
    )
}

@Composable
fun TimesheetScreen(
    daySections: List<DaySection>
) {
    TimeIntervalsList(daySections = daySections)
}

@Composable
private fun TimeIntervalsList(daySections: List<DaySection>) {
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
                    DaySectionIntervals(timeInterval)
                }
            }
        }
    }
}

@Preview
@Composable
fun TimeSheetScreenPreview() {
    TimesheetScreen(daySections = listOf(DaySection(
        headerDate = "Thu, Nov30",
        headerTimeAmount = "01:59:06",
        timeIntervals = listOf(TimeTrackerInterval(
            timeElapsed = 7,
            workingSubject = "Upgrade SDK",
            date = "Thu, Nov30",
            startTime = Instant.now(),
            endTime = Instant.now()
        ))
    )))
}
