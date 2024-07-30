package com.example.myapplication.timesheet.presentation

import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timesheet.domain.usecases.AddTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.DeleteTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.GetTimeTrackerIntervalsUseCase
import com.example.myapplication.timesheet.domain.usecases.UpdateTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.filtering.GetFilteredDaySectionsByDayUseCase
import com.example.myapplication.timesheet.domain.usecases.filtering.GetFilteredDaySectionsByDayUseCaseImpl
import com.example.myapplication.timesheet.domain.usecases.filtering.GetFilteredDaySectionsUseCase
import com.example.myapplication.timesheet.domain.usecases.filtering.GetFilteredDaySectionsUseCaseImpl
import com.example.myapplication.timesheet.domain.usecases.filtering.GetFilteredSubjectsUseCase
import com.example.myapplication.timesheet.domain.usecases.filtering.GetFilteredSubjectsUseCaseUseCaseImpl
import com.example.myapplication.timesheet.domain.usecases.validators.ValidateDate
import com.example.myapplication.timesheet.domain.usecases.validators.ValidateDateImpl
import com.example.myapplication.timesheet.domain.usecases.validators.ValidateTime
import com.example.myapplication.timesheet.domain.usecases.validators.ValidateTimeImpl
import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState
import com.example.myapplication.timesheet.presentation.model.DaySection
import com.example.myapplication.timesheet.presentation.model.SearchBarState
import com.example.myapplication.timesheet.presentation.model.TimeIntervalsSection
import com.example.myapplication.timesheet.presentation.model.toTime
import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import com.example.myapplication.utils.formatDateWithDash
import com.example.myapplication.utils.formatToDateWithoutDash
import com.example.myapplication.utils.formatToLongDate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class TimesheetViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var timesheetViewModel: TimesheetViewModel
    private lateinit var getTimeTrackerIntervalsUseCase: GetTimeTrackerIntervalsUseCase
    private lateinit var deleteTimeIntervalUseCase: DeleteTimeIntervalUseCase
    private lateinit var updateTimeIntervalUseCase: UpdateTimeIntervalUseCase
    private lateinit var addTimeIntervalUseCase: AddTimeIntervalUseCase
    private lateinit var validateTime: ValidateTime
    private lateinit var validateDate: ValidateDate
    private lateinit var timeTracker: TimeTracker
    private lateinit var getFilteredDaySectionsByDayUseCase: GetFilteredDaySectionsByDayUseCase
    private lateinit var getFilteredSubjectsUseCase: GetFilteredSubjectsUseCase
    private lateinit var getFilteredDaySectionsUseCase: GetFilteredDaySectionsUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getTimeTrackerIntervalsUseCase = mockk()
        deleteTimeIntervalUseCase = mockk()
        updateTimeIntervalUseCase = mockk()
        addTimeIntervalUseCase = mockk()
        validateTime = ValidateTimeImpl()
        validateDate = ValidateDateImpl()
        timeTracker = mockk()
        getFilteredDaySectionsByDayUseCase = GetFilteredDaySectionsByDayUseCaseImpl()
        getFilteredSubjectsUseCase = GetFilteredSubjectsUseCaseUseCaseImpl()
        getFilteredDaySectionsUseCase = GetFilteredDaySectionsUseCaseImpl()

        val daySectionsState = MutableStateFlow(daySections)
        every { getTimeTrackerIntervalsUseCase() } returns daySectionsState

        timesheetViewModel = TimesheetViewModel(
            getTimeTrackerIntervalsUseCase = getTimeTrackerIntervalsUseCase,
            deleteTimeIntervalUseCase = deleteTimeIntervalUseCase,
            updateTimeIntervalUseCase = updateTimeIntervalUseCase,
            addTimeIntervalUseCase = addTimeIntervalUseCase,
            validateTime = validateTime,
            validateDate = validateDate,
            timeTracker = timeTracker,
            getFilteredDaySectionsByDayUseCase = getFilteredDaySectionsByDayUseCase,
            getFilteredSubjectsUseCase = getFilteredSubjectsUseCase,
            getFilteredDaySectionsUseCase = getFilteredDaySectionsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getFilterDateOptions_listWasNotEmpty() {
        assertNotEquals(emptyList<DateFilter>(), timesheetViewModel.filterDateOptions)
    }

    @Test
    fun getState_wasCollectedCorrectly() = runTest {
        timesheetViewModel.onAddClicked()
        timesheetViewModel.onSubjectSelected(timeInterval.workingSubject)
        timesheetViewModel.onSelectedFilterChanged(DateFilter.Today)

        val result = timesheetViewModel.state.first()
        val expectedState = TimesheetScreenState(
            daySections = daySections,
            addEditIntervalDialogState = AddEditIntervalDialogState(
                subject = "",
                startTime = null,
                endTime = null,
                date = ""
            ),
            searchBarState = SearchBarState(
                searchText = timeInterval.workingSubject,
                isSearching = true
            ),
            selectedFilter = DateFilter.Today,
            subjects = listOf(timeInterval.workingSubject)
        )

        assertEquals(expectedState, result)
    }

    @Test
    fun deleteTimeInterval_wasCalled() = runTest {
        coEvery { deleteTimeIntervalUseCase(any()) } returns Unit

        timesheetViewModel.deleteTimeInterval(timeInterval.id)

        coVerify { deleteTimeIntervalUseCase(timeInterval.id) }
    }

    @Test
    fun onEditClicked_dialogStateWasAssignedCorrectly() = runTest {
        timesheetViewModel.state.first()
        timesheetViewModel.onEditClicked(timeInterval.id)

        val result = timesheetViewModel.state.first().addEditIntervalDialogState
        val expected = AddEditIntervalDialogState(
            id = timeInterval.id,
            subject = timeInterval.workingSubject,
            startTime = timeInterval.startTime!!.toTime(),
            endTime = timeInterval.endTime!!.toTime(),
            date = timeInterval.date.formatToDateWithoutDash()
        )

        assertEquals(expected, result)
    }

    @Test
    fun onAddClicked_dialogStateWasInitialized() = runTest {
        timesheetViewModel.onAddClicked()
        val result = timesheetViewModel.state.first().addEditIntervalDialogState

        val expected = AddEditIntervalDialogState(
            subject = "",
            startTime = null,
            endTime = null,
            date = ""
        )
        assertEquals(expected, result)

    }

    @Test
    fun onSubjectChanged_dialogStateWasUpdatedWithNewSubject() = runTest {
        timesheetViewModel.onAddClicked()

        timesheetViewModel.onSubjectChanged(WORKING_SUBJECT)

        val result = timesheetViewModel.state.first().addEditIntervalDialogState
        assertEquals(WORKING_SUBJECT, result!!.subject)
    }

    @Test
    fun onStartTimeChanged_setCorrectTime_dialogStateWasUpdatedAndErrorWasNotShown() = runTest {
        timesheetViewModel.onAddClicked()

        timesheetViewModel.onStartTimeChanged(TIME)

        val result = timesheetViewModel.state.first().addEditIntervalDialogState

        assertFalse(result!!.isWrongStartTimeError)
        assertEquals(TIME.toTime(), result.startTime)
    }

    @Test
    fun onStartTimeChanged_setIncorrectTime_errorHasOccurred() = runTest {
        timesheetViewModel.onAddClicked()

        timesheetViewModel.onStartTimeChanged(INCORRECT_TIME)

        val result = timesheetViewModel.state.first().addEditIntervalDialogState

        assertTrue(result!!.isWrongStartTimeError)
        assertNull(result.startTime)
    }

    @Test
    fun onEndTimeChanged_setCorrectTime_dialogStateWasUpdatedAndErrorWasNotShown() = runTest {
        timesheetViewModel.onAddClicked()

        timesheetViewModel.onEndTimeChanged(TIME)

        val result = timesheetViewModel.state.first().addEditIntervalDialogState

        assertFalse(result!!.isWrongEndTimeError)
        assertEquals(TIME.toTime(), result.endTime)
    }

    @Test
    fun onEndTimeChanged_setIncorrectTime_errorHasOccurred() = runTest {
        timesheetViewModel.onAddClicked()

        timesheetViewModel.onEndTimeChanged(INCORRECT_TIME)

        val result = timesheetViewModel.state.first().addEditIntervalDialogState

        assertTrue(result!!.isWrongEndTimeError)
        assertNull(result.endTime)
    }

    @Test
    fun onDateChanged_setCorrectDate_dialogStateWasUpdatedAndErrorWasNotShown() = runTest {
        timesheetViewModel.onAddClicked()

        timesheetViewModel.onDateChanged(DATE)

        val result = timesheetViewModel.state.first().addEditIntervalDialogState
        assertEquals(DATE, result!!.date)
        assertFalse(result.isWrongDateError)
    }

    @Test
    fun onDateChanged_setIncorrectDate_errorHasOccurred() = runTest {
        timesheetViewModel.onAddClicked()

        timesheetViewModel.onDateChanged(INCORRECT_DATE)

        val result = timesheetViewModel.state.first().addEditIntervalDialogState
        assertEquals(INCORRECT_DATE, result!!.date)
        assertTrue(result.isWrongDateError)
    }

    @Test
    fun onDismissEditDialog_dialogStateWasCleared() = runTest {
        timesheetViewModel.onAddClicked()

        timesheetViewModel.onDismissEditDialog()
        val result = timesheetViewModel.state.first().addEditIntervalDialogState

        assertNull(result)
    }

    @Test
    fun onSaveClicked_dateWasCorrectlyFormattedAndAddTimeIntervalWasCalled() = runTest {
        coEvery { addTimeIntervalUseCase(any()) } returns Unit
        timesheetViewModel.onAddClicked()
        timesheetViewModel.onSubjectChanged(timeInterval.workingSubject)
        timesheetViewModel.onDateChanged(DATE)
        timesheetViewModel.onStartTimeChanged(TIME)
        timesheetViewModel.onEndTimeChanged(END_TIME)

        timesheetViewModel.onSaveClicked()

        val result = timesheetViewModel.state.first().addEditIntervalDialogState

        assertEquals(DATE.formatDateWithDash(), result!!.date)
        assertNull(result.id)
        coVerify { addTimeIntervalUseCase(any()) }
    }

    @Test
    fun onSaveClicked_dialogStateHasId_updateTimeIntervalWasCalled() = runTest {
        coEvery { updateTimeIntervalUseCase(any()) } returns Unit
        timesheetViewModel.state.first()
        timesheetViewModel.onEditClicked(timeInterval.id)

        timesheetViewModel.onSaveClicked()

        val result = timesheetViewModel.state.first().addEditIntervalDialogState

        assertEquals(timeInterval.id, result!!.id)
        coVerify { updateTimeIntervalUseCase(any()) }
    }

    @Test
    fun start_startOrResetTimerWasCalled() = runTest {
        every { timeTracker.startOrResetTimer(timeInterval.workingSubject) } returns Unit

        timesheetViewModel.start(timeInterval.workingSubject)

        verify { timeTracker.startOrResetTimer(timeInterval.workingSubject) }
    }

    @Test
    fun reset_wasCalled() = runTest {
        every { timeTracker.reset() } returns Unit

        timesheetViewModel.reset()

        verify { timeTracker.reset() }
    }

    @Test
    fun onSearchTextChanged_stateWasChanged() = runTest {
        timesheetViewModel.onSearchTextChanged(WORKING_SUBJECT)

        val result = timesheetViewModel.state.first()

        assertEquals(WORKING_SUBJECT, result.searchBarState.searchText)
    }

    @Test
    fun onSearchToggled_isSearchingWasTrue() = runTest {
        timesheetViewModel.onSearchToggled()

        val result = timesheetViewModel.state.first()
        assertTrue(result.searchBarState.isSearching)
    }

    @Test
    fun onSearchToggled_isSearchingCorrectlyWasChangedAndSearchTextWasCleared() = runTest {
        timesheetViewModel.onSubjectSelected(timeInterval.workingSubject)

        val result = timesheetViewModel.state.first()
        assertTrue(result.searchBarState.isSearching)
        assertEquals(timeInterval.workingSubject, result.searchBarState.searchText)

        timesheetViewModel.onSearchToggled()

        val result2 = timesheetViewModel.state.first()
        assertFalse(result2.searchBarState.isSearching)
        assertEquals("", result2.searchBarState.searchText)
    }

    @Test
    fun onSubjectSelected_isSearchingWasTrueAndSearchTextWasChanged() = runTest {
        timesheetViewModel.onSubjectSelected(timeInterval.workingSubject)

        val result = timesheetViewModel.state.first()
        assertTrue(result.searchBarState.isSearching)
        assertEquals(timeInterval.workingSubject, result.searchBarState.searchText)
    }

    @Test
    fun onSelectedFilterChanged_dateFilterWasChanged() = runTest {
        val result = timesheetViewModel.state.first()
        assertEquals(DateFilter.All, result.selectedFilter)

        timesheetViewModel.onSelectedFilterChanged(DateFilter.Today)

        val result2 = timesheetViewModel.state.first()
        assertEquals(DateFilter.Today, result2.selectedFilter)
    }

    @Test
    fun onIntervalsSectionExpanded_idWasAddedToExpandedIntervalsSectionIds() = runTest {
        timesheetViewModel.onIntervalsSectionExpanded(timeInterval.id)

        val result = timesheetViewModel.state.first()

        assertTrue(result.daySections.first().timeIntervalsSections.first().expanded)
    }

    @Test
    fun onIntervalsSectionExpanded_idIsAlreadyInList_idWasRemovedFromExpandedIntervalsSectionIds() =
        runTest {
            timesheetViewModel.onIntervalsSectionExpanded(timeInterval.id)

            val result = timesheetViewModel.state.first()

            assertTrue(result.daySections.first().timeIntervalsSections.first().expanded)

            timesheetViewModel.onIntervalsSectionExpanded(timeInterval.id)

            val result2 = timesheetViewModel.state.first()

            assertFalse(result2.daySections.first().timeIntervalsSections.first().expanded)
        }

    companion object {
        const val WORKING_SUBJECT = "workingSubject"
        const val TIME = "121000"
        const val INCORRECT_TIME = "126000"
        const val DATE = "20240525"
        const val INCORRECT_DATE = "20241325"
        const val END_TIME = "122524"
        val timeInterval = TimeTrackerInterval(
            id = "1",
            timeElapsed = 0L,
            startTime = Instant.now(),
            endTime = Instant.now(),
            workingSubject = "subject",
            date = Instant.now().formatToLongDate()
        )
        val daySections = listOf(
            DaySection(
                dateHeader = Instant.now().formatToLongDate(),
                timeAmountHeader = "00:00:00",
                timeIntervalsSections = listOf(
                    TimeIntervalsSection(
                        numberOfIntervals = 1,
                        groupedIntervalsSectionHeader = timeInterval,
                        timeIntervals = listOf(timeInterval)
                    )
                )
            )
        )
    }
}