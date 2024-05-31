package com.example.myapplication.timesheet.presentation.components.datefilter

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.presentation.DateFilterType

@Composable
fun DateChip(
    selectedFilter: DateFilterType,
    onShowBottomSheetClicked: () -> Unit
) {
    InputChip(
        selected = false,
        onClick = onShowBottomSheetClicked,
        label = { Text(selectedFilter.strName) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null
            )
        },
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}