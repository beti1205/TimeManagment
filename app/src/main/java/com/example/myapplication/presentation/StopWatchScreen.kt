package com.example.myapplication.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StopWatchScreen(
    timeAmount: String,
    onStart: () -> Unit = {},
    onRestart: () -> Unit = {},
    isActive: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .align(Alignment.Center)
            )
            Text(
                text = timeAmount,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onStart,
                shape = CircleShape,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 7.dp)
            ) {
                if (!isActive) {
                    Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = null)
                } else {
                    Icon(imageVector = Icons.Outlined.Pause, contentDescription = null)
                }
            }
            Button(
                onClick = onRestart,
                shape = CircleShape,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 7.dp)
            ) {
                Icon(imageVector = Icons.Outlined.RestartAlt, contentDescription = null)
            }
        }
    }
}