package com.example.myapplication.timetracker.presentation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun StartStopButton(
    workingSubject: String,
    isActive: Boolean,
    clearDropDownFocus: () -> Unit,
    onTimerToggled: () -> Unit,
    onSubjectErrorChanged: (Boolean) -> Unit
) {
    Button(
        onClick = {
            if (workingSubject.isNotBlank()) {
                clearDropDownFocus()
                onTimerToggled()
            } else {
                onSubjectErrorChanged(true)
            }
        },
        shape = CircleShape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 7.dp)
    ) {
        if (!isActive) {
            Icon(
                imageVector = Icons.Outlined.PlayArrow,
                contentDescription = stringResource(R.string.start_button)
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Pause,
                contentDescription = stringResource(R.string.stop_button)
            )
        }
    }
}