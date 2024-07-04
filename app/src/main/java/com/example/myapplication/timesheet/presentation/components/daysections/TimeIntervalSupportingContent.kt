package com.example.myapplication.timesheet.presentation.components.daysections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.utils.formatToTimeStr
import java.time.Instant

@Composable
fun TimeIntervalSupportingContent(
    startTime: Instant,
    endTime: Instant,
    timeInterval: TimeTrackerInterval
) {
    Row {
        Text(
            text = startTime.formatToTimeStr() + " - " +
                    endTime.formatToTimeStr()
        )
        if (timeInterval.additionalDays != "0") {
            Text(
                text = "+ ${timeInterval.additionalDays} days",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            )
        }
    }
}