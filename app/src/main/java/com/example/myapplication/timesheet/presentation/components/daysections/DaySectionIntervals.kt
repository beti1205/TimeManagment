package com.example.myapplication.timesheet.presentation.components.daysections

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval

@Composable
fun DaySectionIntervals(
    timeInterval: TimeTrackerInterval,
    onDeleteClicked: (Int) -> Unit,
    onEditClicked: (Int) -> Unit,
    onTimeTrackerStarted: (String) -> Unit
) {
    if (timeInterval.startTime != null && timeInterval.endTime != null) {
        ListItem(
            headlineContent = {
                TimeIntervalHeadlineContent(workingSubject = timeInterval.workingSubject)
            },
            supportingContent = {
                TimeIntervalSupportingContent(
                    startTime = timeInterval.startTime,
                    endTime = timeInterval.endTime,
                    timeInterval = timeInterval
                )
            },
            trailingContent = {
                TimeIntervalTrailingContent(
                    timeInterval = timeInterval,
                    onTimeTrackerStarted = onTimeTrackerStarted,
                    onDeleteClicked = onDeleteClicked,
                    onEditClicked = onEditClicked
                )
            },
            shadowElevation = 2.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
