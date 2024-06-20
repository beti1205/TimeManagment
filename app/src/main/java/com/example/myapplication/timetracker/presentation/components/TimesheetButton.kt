package com.example.myapplication.timetracker.presentation.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R

@Composable
fun TimesheetButton(onNavigateToTimeSheet: () -> Unit) {
    Button(onClick = onNavigateToTimeSheet) {
        Text(text = stringResource(R.string.timesheet))
    }
}