package com.example.myapplication.timetracker.presentation

import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import com.example.myapplication.timetracker.domain.timetracker.TimeTrackerState
import com.example.myapplication.timetracker.domain.usecases.GetAllWorkingSubjectsUseCase
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class TimeTrackerViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var timeTrackerViewModel: TimeTrackerViewModel
    private lateinit var timeTracker: TimeTracker
    private lateinit var getAllWorkingSubjectsUseCase: GetAllWorkingSubjectsUseCase
    private lateinit var trackerState: MutableStateFlow<TimeTrackerState>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        timeTracker = mockk()
        getAllWorkingSubjectsUseCase = mockk()

        trackerState = MutableStateFlow(TimeTrackerState())
        val subjects = MutableStateFlow(emptyList<String>())

        every { getAllWorkingSubjectsUseCase() } returns subjects
        every { timeTracker.state } returns trackerState
        every { timeTracker.onWorkingSubjectChanged(any()) } returns Unit
        every { timeTracker.toggleTimer() } returns Job()
        every { timeTracker.adjustTime(any()) } returns Unit
        every { timeTracker.reset() } returns Unit

        timeTrackerViewModel = TimeTrackerViewModel(timeTracker, getAllWorkingSubjectsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetState() = runTest {
        val expectedState = TimeTrackerScreenState(
            isActive = true,
            timeElapsed = 0L,
            startTime = null,
            endTime = null,
            workingSubject = WORKING_SUBJECT,
            isSubjectErrorOccurred = false,
            filteredSubjects = emptyList(),
            selectedTimeAdjustment = TimeAdjustment.PLUS_5
        )

        trackerState.value = TimeTrackerState(isActive = true)

        timeTrackerViewModel.onSubjectErrorChanged(false)
        timeTrackerViewModel.onWorkingSubjectChanged(WORKING_SUBJECT)
        timeTrackerViewModel.onTypeSelected(TimeAdjustment.PLUS_5)

        val result = timeTrackerViewModel.state.first()
        assertEquals(expectedState, result)
    }

    @Test
    fun setWorkingSubjectIfTimerHasBeenResumed_workingSubjectIsBlank_wasNotCalled() = runTest {
        val spy = spyk(timeTrackerViewModel)

        spy.setWorkingSubjectIfTimerHasBeenResumed()
        verify(inverse = true) { spy.onWorkingSubjectChanged(any()) }
    }

    @Test
    fun setWorkingSubjectIfTimerHasBeenResumed_workingSubjectIsNotBlank_wasCalled() =
        runTest {
            trackerState.value = TimeTrackerState(workingSubject = WORKING_SUBJECT)
            val spy = spyk(timeTrackerViewModel)

            spy.setWorkingSubjectIfTimerHasBeenResumed()
            verify { spy.onWorkingSubjectChanged(WORKING_SUBJECT) }
        }

    @Test
    fun toggleTimer_timerIsNotActive_timeAdjustmentWasCleared() = runTest {
        timeTrackerViewModel.onTypeSelected(TimeAdjustment.PLUS_5)
        trackerState.value = TimeTrackerState(isActive = false, endTime = Instant.now())
        timeTrackerViewModel.state.first()

        timeTrackerViewModel.toggleTimer()

        val collectJob = launch(testDispatcher) { timeTrackerViewModel.state.collect() }

        assertNull(timeTrackerViewModel.state.value.selectedTimeAdjustment)
        verify { timeTracker.toggleTimer() }

        collectJob.cancel()
    }

    @Test
    fun toggleTimer_timerIsActive_timeWasAdjusted() = runTest {
        trackerState.value = TimeTrackerState(isActive = true)
        timeTrackerViewModel.onTypeSelected(TimeAdjustment.PLUS_5)

        val collectJob = launch(testDispatcher) { timeTrackerViewModel.state.collect() }

        timeTrackerViewModel.toggleTimer()

        verify { timeTracker.adjustTime(any()) }
        verify { timeTracker.toggleTimer() }

        collectJob.cancel()
    }

    @Test
    fun reset_wasCalled() = runTest {
        timeTrackerViewModel.reset()

        verify { timeTracker.reset() }
    }

    @Test
    fun reset_timeAdjustmentWasNotNull() = runTest {
        timeTrackerViewModel.onTypeSelected(TimeAdjustment.PLUS_5)

        timeTrackerViewModel.reset()

        val collectJob = launch(testDispatcher) { timeTrackerViewModel.state.collect() }

        assertNull(timeTrackerViewModel.state.value.selectedTimeAdjustment)
        verify { timeTracker.reset() }

        collectJob.cancel()
    }

    @Test
    fun onWorkingSubjectChanged_workingSubjectWasNotBlank() = runTest {
        timeTrackerViewModel.onWorkingSubjectChanged(WORKING_SUBJECT)

        val collectJob = launch(testDispatcher) { timeTrackerViewModel.state.collect() }

        assertEquals(WORKING_SUBJECT, timeTrackerViewModel.state.value.workingSubject)
        assertFalse(timeTrackerViewModel.state.value.isSubjectErrorOccurred)

        collectJob.cancel()
    }

    @Test
    fun onWorkingSubjectChanged_workingSubjectWasBlank() = runTest {
        timeTrackerViewModel.onWorkingSubjectChanged("")

        val collectJob = launch(testDispatcher) { timeTrackerViewModel.state.collect() }

        assertEquals("", timeTrackerViewModel.state.value.workingSubject)
        assertTrue(timeTrackerViewModel.state.value.isSubjectErrorOccurred)

        collectJob.cancel()
    }

    @Test
    fun onSubjectErrorChanged_wasTrue() = runTest {
        timeTrackerViewModel.onSubjectErrorChanged(true)

        val collectJob = launch(testDispatcher) { timeTrackerViewModel.state.collect() }

        assertTrue(timeTrackerViewModel.state.value.isSubjectErrorOccurred)

        collectJob.cancel()
    }

    @Test
    fun onSubjectErrorChanged_wasFalse() = runTest {
        timeTrackerViewModel.onSubjectErrorChanged(false)

        val collectJob = launch(testDispatcher) { timeTrackerViewModel.state.collect() }

        assertFalse(timeTrackerViewModel.state.value.isSubjectErrorOccurred)

        collectJob.cancel()
    }

    @Test
    fun onTypeSelected_newTimeAdjustmentWasSet() = runTest {
        timeTrackerViewModel.onTypeSelected(TimeAdjustment.PLUS_5)

        val collectJob = launch(testDispatcher) { timeTrackerViewModel.state.collect() }

        assertEquals(TimeAdjustment.PLUS_5, timeTrackerViewModel.state.value.selectedTimeAdjustment)

        collectJob.cancel()
    }

    @Test
    fun onTypeSelected_tmeAdjustmentWasClearedIfNewValueWasTheSame() = runTest {
        timeTrackerViewModel.onTypeSelected(TimeAdjustment.PLUS_5)

        val collectJob = launch(testDispatcher) { timeTrackerViewModel.state.collect() }

        assertEquals(TimeAdjustment.PLUS_5, timeTrackerViewModel.state.value.selectedTimeAdjustment)

        timeTrackerViewModel.onTypeSelected(TimeAdjustment.PLUS_5)
        assertNull(timeTrackerViewModel.state.value.selectedTimeAdjustment)

        collectJob.cancel()
    }

    companion object {
        const val WORKING_SUBJECT = "workingSubject"
    }
}