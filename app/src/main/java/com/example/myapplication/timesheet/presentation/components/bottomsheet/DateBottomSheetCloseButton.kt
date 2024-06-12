package com.example.myapplication.timesheet.presentation.components.bottomsheet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateBottomSheetCloseButton(
    sheetState: SheetState,
    onDismissBottomSheet: () -> Unit
) {
    val scope = rememberCoroutineScope()

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
}