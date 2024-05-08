package com.example.myapplication.timesheet.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timetracker.domain.stopwatch.formatTime
import com.example.myapplication.timetracker.domain.stopwatch.toTime
import com.example.myapplication.utils.formatToTime

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
                Text(
                    text = timeInterval.workingSubject,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Row {
                    Text(
                        text = timeInterval.startTime.formatToTime() + " - " +
                                timeInterval.endTime.formatToTime()
                    )
                    if (timeInterval.additionalDays != "0") {
                        Text(
                            text = "+ ${timeInterval.additionalDays}",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                        )
                    }
                }
            },
            trailingContent = {
                var isExpanded by remember {
                    mutableStateOf(false)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = timeInterval.timeElapsed.toTime().formatTime()
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
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(R.string.delete_menu_item_label))
                            },
                            onClick = {
                                onDeleteClicked(timeInterval.id)
                                isExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(R.string.edit_menu_item_label))
                            },
                            onClick = {
                                onEditClicked(timeInterval.id)
                                isExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            },
            shadowElevation = 2.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

