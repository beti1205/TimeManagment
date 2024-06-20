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
import com.example.myapplication.timetracker.presentation.TimeAmountChangesType

@Composable
fun TimeAmountChangesChips(
    selectedChangesType: TimeAmountChangesType?,
    chipsEnabled: Boolean,
    onTypeSelected: (TimeAmountChangesType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimeAmountChangesType.entries.forEach { chip ->
            FilterChip(
                label = {
                    Text(
                        getTypeName(chip)
                    )
                },
                selected = selectedChangesType == chip,
                onClick = { onTypeSelected(chip) },
                enabled = chipsEnabled
            )
        }
    }
}

@Composable
fun getTypeName(chip: TimeAmountChangesType) =
    when (chip) {
        TimeAmountChangesType.INCREASED_5 -> stringResource(R.string.increased_5)
        TimeAmountChangesType.INCREASED_15 -> stringResource(R.string.increased_15)
        TimeAmountChangesType.INCREASED_30 -> stringResource(R.string.increased_30)
        TimeAmountChangesType.REDUCED_5 -> stringResource(R.string.reduced_5)
        TimeAmountChangesType.REDUCED_15 -> stringResource(R.string.reduced_15)
        TimeAmountChangesType.REDUCED_30 -> stringResource(R.string.reduced_30)
    }