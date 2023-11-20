package com.example.myapplication.timetracker.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val timeTrackerScreenRoute = "time_tracker"

fun NavGraphBuilder.timeTrackerScreen(
    onNavigateToTimeSheet: () -> Unit
) {
    composable(timeTrackerScreenRoute) {
        TimeTrackerScreen(
            onNavigateToTimeSheet = onNavigateToTimeSheet
        )
    }
}

fun NavController.navigateToTimeTrackerScreen() {
    this.navigate(timeTrackerScreenRoute)
}