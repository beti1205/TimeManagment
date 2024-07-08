package com.example.myapplication.timetracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtonRow(
    workingSubject: String,
    isActive: Boolean,
    clearDropDownFocus: () -> Unit,
    onTimerToggled: () -> Unit,
    onSubjectErrorChanged: (Boolean) -> Unit,
    onResetClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StartStopButton(
            workingSubject = workingSubject,
            clearDropDownFocus = clearDropDownFocus,
            onTimerToggled = onTimerToggled,
            onSubjectErrorChanged = onSubjectErrorChanged,
            isActive = isActive
        )
        RestartButton(onResetClicked)
    }
}