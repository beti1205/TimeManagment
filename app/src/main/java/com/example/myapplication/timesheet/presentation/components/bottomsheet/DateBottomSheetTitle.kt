package com.example.myapplication.timesheet.presentation.components.bottomsheet

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R

@Composable
fun DateBottomSheetTitle() {
    Text(
        text = stringResource(R.string.bottom_sheet_date_title),
        style = MaterialTheme.typography.titleMedium
    )
}