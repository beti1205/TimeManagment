package com.example.myapplication.timesheet.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.timesheet.domain.usecases.IntervalsListItem
import com.example.myapplication.timetracker.domain.stopwatch.formatTime
import com.example.myapplication.timetracker.domain.stopwatch.toTime
import com.example.myapplication.utils.formatToTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimesheetScreen(
    viewModel: TimesheetViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(state) { item ->
            when (item) {
                is IntervalsListItem.Header -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    ListItem(
                        headlineText = { Text(item.date) },
                        shadowElevation = 2.dp,
                        colors = ListItemDefaults.colors(
                            containerColor = Color.LightGray
                        ),
                        trailingContent = { Text(item.timeAmount) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                is IntervalsListItem.Interval -> {
                    val interval = item.interval
                    val startTime = interval.startTime
                    val endTime = interval.endTime

                    if (startTime != null && endTime != null) {
                        ListItem(
                            headlineText = {
                                Text(
                                    text = interval.workingSubject,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            supportingText = {
                                Text(
                                    text = startTime.formatToTime() + " - " + endTime.formatToTime()
                                )
                            },
                            trailingContent = {
                                Text(
                                    text = interval.timeElapsed.toTime().formatTime()
                                )
                            },
                            shadowElevation = 2.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TimeSheetScreenPreview() {
    TimesheetScreen()
}
