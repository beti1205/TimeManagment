package com.example.myapplication.timesheet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.timesheet.domain.usecases.DeleteTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.GetTimeTrackerIntervalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimesheetViewModel @Inject constructor(
    getTimeTrackerIntervalsUseCase: GetTimeTrackerIntervalsUseCase,
    private val deleteTimeIntervalUseCase: DeleteTimeIntervalUseCase
) : ViewModel() {

    val state: StateFlow<TimesheetScreenState> = getTimeTrackerIntervalsUseCase()
        .map { daySections ->
            TimesheetScreenState(daySections)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500L),
            initialValue = TimesheetScreenState()
        )

    fun deleteTimeInterval(id: Int) = viewModelScope.launch {
        deleteTimeIntervalUseCase(id)
    }
}