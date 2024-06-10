package com.example.myapplication.timesheet.presentation.model

data class SearchBarState(
    val isSearching: Boolean = false,
    val searchText: String = ""
)