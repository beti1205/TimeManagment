package com.example.myapplication.timetracker.presentation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun RestartButton(onResetClicked: () -> Unit) {
    Button(
        onClick = onResetClicked,
        shape = CircleShape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 7.dp)
    ) {
        Icon(imageVector = Icons.Outlined.RestartAlt, contentDescription = null)
    }
}