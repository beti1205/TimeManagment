package com.example.myapplication.timesheet.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun ScaffoldFloatingActionButton(onAddClicked: () -> Unit) {
    FloatingActionButton(onClick = onAddClicked) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}