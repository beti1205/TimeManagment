package com.example.myapplication.timesheet.presentation.components.daysections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    onEditClicked: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = timeInterval.timeElapsed.convertSecondsToTimeString()
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { onTimeTrackerStarted(timeInterval.workingSubject) }) {
            Icon(
                imageVector = Icons.Outlined.PlayArrow,
                contentDescription = null
            )
        }
        IconButton(onClick = { isExpanded = true }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = null
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