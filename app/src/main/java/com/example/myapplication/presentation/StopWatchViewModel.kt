package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.StopWatch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StopWatchViewModel @Inject constructor(
    private val stopWatch: StopWatch
    ) : ViewModel() {

    val state = stopWatch.state

    fun start() {
        stopWatch.start()
    }

    fun restart() {
        stopWatch.reset()
    }
}