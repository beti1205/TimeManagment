package com.example.myapplication.timesheet.presentation.components.daysections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval

@Composable
fun DaySectionInterval(
    timeInterval: TimeTrackerInterval,
    onDeleteClicked: (Int) -> Unit,
    onEditClicked: (Int) -> Unit,
    onTimeTrackerStarted: (String) -> Unit,
    onIntervalsSectionExpanded: (Int) -> Unit,
    numberOfIntervals: Int? = null
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
                    onEditClicked = onEditClicked,
                    onIntervalsSectionExpanded = onIntervalsSectionExpanded,
                    numberOfIntervals = numberOfIntervals
                )
            },
            shadowElevation = 2.dp,
            modifier = Modifier.clickable {
                if (numberOfIntervals != null) {
                    onIntervalsSectionExpanded(timeInterval.id)
                } else {
                    onEditClicked(timeInterval.id)
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
