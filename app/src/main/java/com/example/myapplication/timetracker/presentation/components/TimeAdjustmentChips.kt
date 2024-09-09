package com.example.myapplication.timetracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.timetracker.presentation.TimeAdjustment
import com.example.myapplication.timetracker.presentation.getTypeName

@Composable
fun TimeAdjustmentChips(
    selectedTimeAdjustment: TimeAdjustment?,
    chipsEnabled: Boolean,
    onTypeSelected: (TimeAdjustment) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimeAdjustment.entries.forEach { chip ->
            FilterChip(
                label = { Text(stringResource(chip.getTypeName())) },
                selected = selectedTimeAdjustment == chip,
                onClick = { onTypeSelected(chip) },
                enabled = chipsEnabled
            )
        }
    }
}
