package com.example.myapplication.timesheet.presentation.components.daysections

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval

@Composable
fun TimeIntervalDropdownMenu(
    isExpanded: Boolean,
    onDeleteClicked: (String) -> Unit,
    timeInterval: TimeTrackerInterval,
    onEditClicked: (String) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismiss,
    ) {
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.delete_menu_item_label))
            },
            onClick = {
                onDeleteClicked(timeInterval.id)
                onDismiss()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.edit_menu_item_label))
            },
            onClick = {
                onEditClicked(timeInterval.id)
                onDismiss()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null
                )
            }
        )
    }
}