package com.example.myapplication.timesheet.presentation.components.addeditdialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R

@Composable
fun addEditIntervalDialogHeader(id: Int?) =
    when {
        id != null -> stringResource(id = R.string.edit_menu_item_label)
        else -> stringResource(R.string.add_dialog_header)
    }