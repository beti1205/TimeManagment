package com.example.myapplication.timesheet.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timetracker.domain.stopwatch.formatTime
import com.example.myapplication.timetracker.domain.stopwatch.toTime
import com.example.myapplication.utils.formatToTime

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DaySectionIntervals(
    timeInterval: TimeTrackerInterval,
    onDeleteClicked: (Int) -> Unit
) {
    if (timeInterval.startTime != null && timeInterval.endTime != null) {
        ListItem(
            headlineText = {
                Text(
                    text = timeInterval.workingSubject,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingText = {
                Text(
                    text = timeInterval.startTime.formatToTime() + " - " +
                            timeInterval.endTime.formatToTime()
                )
            },
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = timeInterval.timeElapsed.toTime().formatTime()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { onDeleteClicked(timeInterval.id) }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null
                        )
                    }
                }
            },
            shadowElevation = 2.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
