package com.example.myapplication.timesheet.presentation.components.daysections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GroupedIntervalsSectionHeaderNumberBox(
    numberOfIntervals: Int,
    onIntervalsSectionExpanded: () -> Unit
) {
    Spacer(modifier = Modifier.size(48.dp))
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(32.dp)
            .shadow(elevation = 8.dp)
            .background(MaterialTheme.colorScheme.tertiary)
            .clickable { onIntervalsSectionExpanded() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = numberOfIntervals.toString(),
            color = MaterialTheme.colorScheme.onTertiary,
            fontSize = 14.sp
        )
    }
}