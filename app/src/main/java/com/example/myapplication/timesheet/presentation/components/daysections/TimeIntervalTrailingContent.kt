package com.example.myapplication.timesheet.presentation.components.daysections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.utils.convertSecondsToTimeString

@Composable
fun TimeIntervalTrailingContent(
    timeInterval: TimeTrackerInterval,
    onTimeTrackerStarted: (String) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    onEditClicked: (Int) -> Unit,
    onIntervalsSectionExpanded: (Int) -> Unit,
    numberOfIntervals: Int? = null
) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = timeInterval.timeElapsed.convertSecondsToTimeString()
        )
        Spacer(modifier = Modifier.width(8.dp))
        if (numberOfIntervals != null) {
            GroupedIntervalsSectionHeaderNumberBox(
                numberOfIntervals = numberOfIntervals,
                onIntervalsSectionExpanded = { onIntervalsSectionExpanded(timeInterval.id) }
            )
        } else {
            TimeIntervalMenuIcons(
                onTimeTrackerStarted = { onTimeTrackerStarted(timeInterval.workingSubject) },
                onExpand = { isExpanded = true }
            )
        }
        TimeIntervalDropdownMenu(
            isExpanded = isExpanded,
            onDeleteClicked = onDeleteClicked,
            timeInterval = timeInterval,
            onEditClicked = onEditClicked,
            onDismiss = { isExpanded = false }
        )
    }
}
