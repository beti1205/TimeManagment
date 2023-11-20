package com.example.myapplication.timesheet.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val timesheetScreenRoute = "time_sheet"

fun NavGraphBuilder.timesheetScreen(){
    composable(timesheetScreenRoute){
        TimesheetScreen()
    }
}

fun NavController.navigateToTimesheetScreen(){
    this.navigate(timesheetScreenRoute)
}