package com.example.myapplication.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.services.TimeTrackerService
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.timesheet.presentation.navigateToTimesheetScreen
import com.example.myapplication.timesheet.presentation.timesheetScreen
import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import com.example.myapplication.timetracker.presentation.timeTrackerScreen
import com.example.myapplication.timetracker.presentation.timeTrackerScreenRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var timeTracker: TimeTracker

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
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = timeTrackerScreenRoute){
                        timeTrackerScreen(
                            onNavigateToTimeSheet = {
                                navController.navigateToTimesheetScreen()
                            }
                        )
                        timesheetScreen()
                    }
                }
            }
        }

        lifecycleScope.launch {
            timeTracker.state.map { it.isActive }.distinctUntilChanged().collect { isActive ->
                Intent(this@MainActivity, TimeTrackerService::class.java).also {
                    if (isActive) {
                        it.action = TimeTrackerService.Actions.START.toString()
                    } else {
                        it.action = TimeTrackerService.Actions.STOP.toString()
                    }
                    startService(it)
                }
            }
        }
    }
}

