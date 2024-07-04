package com.example.myapplication.timesheet.presentation.components.daysections

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun TimeIntervalMenuIcons(
    onTimeTrackerStarted: () -> Unit,
    onExpand: () -> Unit
) {
    IconButton(onClick = onTimeTrackerStarted) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null
        )
    }
    IconButton(onClick = onExpand) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = null
        )
    }
}