package com.example.myapplication.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.myapplication.presentation.ui.theme.MyApplicationTheme
import com.example.myapplication.services.StopWatchService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: StopWatchViewModel by viewModels()
                    val state by viewModel.state.collectAsState()

                    LaunchedEffect(key1 = state.isActive){
                        Intent.ACTION_AIRPLANE_MODE_CHANGED
                        Intent(applicationContext, StopWatchService::class.java).also {
                            if (state.isActive) {
                                it.action = StopWatchService.Actions.START.toString()
                            } else {
                                it.action = StopWatchService.Actions.STOP.toString()
                            }
                            startService(it)
                        }
                    }

                    StopWatchScreen(
                        timeAmount = state.timeAmount,
                        isActive = state.isActive,
                        onStart = viewModel::start,
                        onRestart = viewModel::restart
                    )
                }
            }
        }
    }
}

