package com.example.myapplication.presentation

data class StopWatchState(
    val isActive: Boolean = false,
    val timeAmount: String = "00:00:00"
)