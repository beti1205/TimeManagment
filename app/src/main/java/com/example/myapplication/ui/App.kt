package com.example.myapplication.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.timesheet.presentation.navigateToTimesheetScreen
import com.example.myapplication.timesheet.presentation.timesheetScreen
import com.example.myapplication.timetracker.presentation.timeTrackerScreen
import com.example.myapplication.timetracker.presentation.timeTrackerScreenRoute
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun App() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = timeTrackerScreenRoute
            ) {
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