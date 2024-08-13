package com.example.myapplication.timesheet.presentation.components.datefilter

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.timesheet.presentation.DateFilter
import com.example.myapplication.timesheet.presentation.getFilterName

@Composable
fun DateChip(
    selectedFilter: DateFilter,
    onShowBottomSheetClicked: () -> Unit
) {
    InputChip(
        selected = false,
        onClick = onShowBottomSheetClicked,
        label = { Text(selectedFilter.getFilterName()) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = stringResource(R.string.date_filter)
            )
        },
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}