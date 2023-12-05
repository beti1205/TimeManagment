package com.example.myapplication.timesheet.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onDeleteConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.delete_warning_title))
        },
        text = {
            Text(text = stringResource(R.string.delete_warning_body))
        },
        confirmButton = {
            Button(onClick = { onDeleteConfirm(); onDismissRequest() }) {
                Text(stringResource(R.string.delete_confirm_button))
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(stringResource(R.string.delete_dismiss_button))
            }
        }
    )
}