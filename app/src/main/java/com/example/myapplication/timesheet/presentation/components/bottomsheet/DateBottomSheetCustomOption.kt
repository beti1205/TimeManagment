package com.example.myapplication.timesheet.presentation.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun DateBottomSheetCustomOption(onShowDatePickerRangeClicked: () -> Unit) {
    Text(
        text = stringResource(R.string.bottom_sheet_custom),
        style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.primary
        ),
        textDecoration = TextDecoration.Underline,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable { onShowDatePickerRangeClicked() }
    )
}