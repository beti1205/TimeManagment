package com.example.myapplication.timesheet.presentation.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.presentation.DateFilter
import com.example.myapplication.timesheet.presentation.getFilterName

@Composable
fun DateRadioButtonRow(
    date: DateFilter,
    selectedFilter: DateFilter,
    onSelectedDate: (DateFilter) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onSearchToggled: () -> Unit
) {
    if (date !is DateFilter.CustomRange) {
        val onClick = {
            onSelectedDate(date)
            onDismissBottomSheet()
            onSearchToggled()
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onClick() }
        ) {
            RadioButton(
                selected = date == selectedFilter,
                onClick = onClick
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = date.getFilterName(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}