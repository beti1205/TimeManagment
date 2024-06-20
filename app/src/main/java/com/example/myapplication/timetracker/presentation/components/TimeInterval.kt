package com.example.myapplication.timetracker.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.utils.formatToDayMonthDate
import com.example.myapplication.utils.formatToLongTimeStr
import java.time.Instant

@Composable
fun TimeInterval(
    startTime: Instant?,
    showEndTime: Boolean,
    endTime: Instant?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 32.dp),
    ) {
        Row {
            Text(
                text = stringResource(R.string.time_tracker_start_time),
                modifier = Modifier.weight(0.5f)
            )
            Text(text = startTime?.formatToLongTimeStr() ?: stringResource(R.string.emptyTime))
            Text(
                text = Instant.now().formatToDayMonthDate(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
        Row {
            Text(
                text = stringResource(R.string.time_tracker_end_time),
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = when {
                    showEndTime -> endTime?.formatToLongTimeStr()!!
                    else -> stringResource(R.string.emptyTime)
                }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}