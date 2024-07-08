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
import com.example.myapplication.R
import com.example.myapplication.timetracker.presentation.TimeAdjustment

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
                label = {
                    Text(
                        getTypeName(chip)
                    )
                },
                selected = selectedTimeAdjustment == chip,
                onClick = { onTypeSelected(chip) },
                enabled = chipsEnabled
            )
        }
    }
}

@Composable
fun getTypeName(chip: TimeAdjustment) =
    when (chip) {
        TimeAdjustment.PLUS_5 -> stringResource(R.string.plus_5)
        TimeAdjustment.PLUS_15 -> stringResource(R.string.plus_15)
        TimeAdjustment.PLUS_30 -> stringResource(R.string.plus_30)
        TimeAdjustment.MINUS_5 -> stringResource(R.string.minus_5)
        TimeAdjustment.MINUS_15 -> stringResource(R.string.minus_15)
        TimeAdjustment.MINUS_30 -> stringResource(R.string.minus_30)
    }