package com.example.myapplication.timesheet.presentation.components.datefilter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.timesheet.presentation.DateFilter
import com.example.myapplication.timesheet.presentation.getFilterName
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DateBottomSheet(
    selectedFilter: DateFilter,
    filterDateOptions: List<DateFilter>,
    onSelectedDate: (DateFilter) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onShowDatePickerRangeClicked: () -> Unit,
    onSearchToggled: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismissBottomSheet,
        sheetState = sheetState,
        dragHandle = null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismissBottomSheet()
                            }
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.bottom_sheet_date_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
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

            filterDateOptions.forEach { date ->
                if (date !is DateFilter.CustomRange)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = date == selectedFilter,
                            onClick = {
                                onSelectedDate(date)
                                onDismissBottomSheet()
                                onSearchToggled()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = date.getFilterName(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
            }
        }
    }
}
