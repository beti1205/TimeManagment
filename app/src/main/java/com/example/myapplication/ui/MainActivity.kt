package com.example.myapplication.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.myapplication.timetracker.domain.stopwatch.formatTime
import com.example.myapplication.timetracker.domain.stopwatch.toTime
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.services.TimeTrackerService
import com.example.myapplication.timetracker.presentation.TimeTrackerScreen
import com.example.myapplication.timetracker.presentation.TimeTrackerViewModel
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
                    val viewModel: TimeTrackerViewModel by viewModels()

                    Screen(viewModel)
                }
            }
        }
    }

    @Composable
    private fun Screen(
        viewModel: TimeTrackerViewModel
    ) {

        val state by viewModel.state.collectAsState()

        LaunchedEffect(key1 = state.isActive) {
            Intent(applicationContext, TimeTrackerService::class.java).also {
                if (state.isActive) {
                    it.action = TimeTrackerService.Actions.START.toString()
                } else {
                    it.action = TimeTrackerService.Actions.STOP.toString()
                }
                startService(it)
            }
        }

        LaunchedEffect(key1 = state.workingSubject) {
            if (state.workingSubject.isNotBlank()) {
                viewModel.onSubjectErrorChanged(false)
            }
            viewModel.clearElapsedTimeWhenSubjectChanged()
        }

        TimeTrackerScreen(
            timeAmount = state.timeElapsed.toTime().formatTime(),
            isActive = state.isActive,
            workingSubject = state.workingSubject,
            isSubjectErrorOccurred = state.isSubjectErrorOccurred,
            filteredSubjectList = state.filteredSubjectList,
            onStart = viewModel::start,
            onRestart = viewModel::restart,
            onSubjectErrorChanged = viewModel::onSubjectErrorChanged,
            onWorkingSubjectChanged = viewModel::onWorkingSubjectChanged
        )
    }
}

