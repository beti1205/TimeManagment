package com.example.myapplication.timesheet

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.example.myapplication.R
import com.example.myapplication.timesheet.domain.model.TimeTrackerInterval
import com.example.myapplication.timesheet.presentation.DateFilter
import com.example.myapplication.timesheet.presentation.TimesheetScreen
import com.example.myapplication.timesheet.presentation.TimesheetScreenState
import com.example.myapplication.timesheet.presentation.model.AddEditIntervalDialogState
import com.example.myapplication.timesheet.presentation.model.DaySection
import com.example.myapplication.timesheet.presentation.model.SearchBarState
import com.example.myapplication.timesheet.presentation.model.TimeIntervalsSection
import com.example.myapplication.timesheet.presentation.model.toTime
import com.example.myapplication.utils.convertSecondsToTimeString
import com.example.myapplication.utils.formatToDateWithoutDash
import com.example.myapplication.utils.formatToDayMonthDate
import com.example.myapplication.utils.formatToLongDate
import com.example.myapplication.utils.formatToLongTimeStr
import com.example.myapplication.utils.formatToTimeStr
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class TimesheetScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun initialState_isRendered() {
        composeTestRule.setContent {
            TimesheetScreen(TimesheetScreenState())
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.empty_day_sections_message))
    }

    @Test
    fun contentLoadedState_isRendered() {
        composeTestRule.setContent {
            TimesheetScreen(TimesheetScreenState(daySections = daySections))
        }

        composeTestRule.onNodeWithText(Instant.now().formatToDayMonthDate()).isDisplayed()
        composeTestRule
            .onAllNodesWithText(timeElapsed.convertSecondsToTimeString())
            .assertCountEquals(2)

        composeTestRule.onNodeWithText(workingSubject).isDisplayed()
        composeTestRule
            .onNodeWithText(startTime.formatToTimeStr() + "-" + endTime.formatToTimeStr())
            .isDisplayed()
    }

    @Test
    fun editIntervalDialog_contentIsDisplayed() {
        composeTestRule.setContent {
            TimesheetScreen(
                TimesheetScreenState(addEditIntervalDialogState = editIntervalDialogState)
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.edit_dialog_start_date_label),
                useUnmergedTree = true
            )
            .assertTextEquals(date.formatToLongDate())

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.subject),
                useUnmergedTree = true
            )
            .assertTextEquals(workingSubject)

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.edit_dialog_start_time_text_field),
                useUnmergedTree = true
            )
            .assertTextEquals(startTime.formatToLongTimeStr())

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.edit_dialog_end_time_text_field),
                useUnmergedTree = true
            )
            .assertTextEquals(endTime.formatToLongTimeStr())
    }

    @Test
    fun addIntervalDialog_initialState_contentIsEmpty() {
        composeTestRule.setContent {
            TimesheetScreen(
                TimesheetScreenState(addEditIntervalDialogState = addIntervalDialogState)
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.edit_dialog_start_date_label),
                useUnmergedTree = true
            )
            .assertTextEquals("")

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.subject),
                useUnmergedTree = true
            )
            .assertTextEquals("")

        val defaultTime = "00:00:00"
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.edit_dialog_start_time_text_field),
                useUnmergedTree = true
            )
            .assertTextEquals(defaultTime)

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.edit_dialog_end_time_text_field),
                useUnmergedTree = true
            )
            .assertTextEquals(defaultTime)
    }

    @Test
    fun searchBar_isSearching_contentIsDisplayed() {
        composeTestRule.setContent {
            TimesheetScreen(
                TimesheetScreenState(
                    subjects = listOf(workingSubject),
                    searchBarState = SearchBarState(
                        isSearching = true,
                        searchText = workingSubject
                    )
                ),
                filterDateOptions = dateFilters
            )
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.date_filter_all))
            .isDisplayed()

        composeTestRule.onAllNodesWithText(workingSubject).assertCountEquals(2)
    }


    @Composable
    private fun TimesheetScreen(
        state: TimesheetScreenState,
        filterDateOptions: List<DateFilter> = emptyList()
    ) {
        TimesheetScreen(
            state = state,
            filterDateOptions = filterDateOptions,
            onDeleteTimeInterval = {},
            onSaveClicked = {},
            onSubjectChanged = {},
            onStartTimeChanged = {},
            onEndTimeChanged = {},
            onEditClicked = {},
            onDismissAddEditDialog = {},
            onTimeTrackerStarted = {},
            onResetActionClicked = {},
            onDateChanged = {},
            onAddClicked = {},
            onSearchTextChanged = {},
            onSearchToggled = {},
            onSubjectSelected = {},
            onSelectedFilterChanged = {},
            onIntervalsSectionExpanded = {}
        )
    }

    companion object {
        val date = Instant.now()
        val startTime = Instant.now().minus(2, ChronoUnit.SECONDS)
        val endTime = Instant.now()
        val workingSubject = "subject"
        val timeElapsed = 2L

        val timeInterval = TimeTrackerInterval(
            id = "1",
            timeElapsed = timeElapsed,
            startTime = startTime,
            endTime = endTime,
            workingSubject = workingSubject,
            date = date.formatToLongDate()
        )
        val daySections = listOf(
            DaySection(
                dateHeader = date.formatToLongDate(),
                timeAmountHeader = "00:00:02",
                timeIntervalsSections = listOf(
                    TimeIntervalsSection(
                        numberOfIntervals = 1,
                        groupedIntervalsSectionHeader = timeInterval,
                        timeIntervals = listOf(timeInterval)
                    )
                )
            )
        )

        val editIntervalDialogState = AddEditIntervalDialogState(
            id = timeInterval.id,
            subject = timeInterval.workingSubject,
            startTime = timeInterval.startTime!!.toTime(),
            endTime = timeInterval.endTime!!.toTime(),
            date = timeInterval.date.formatToDateWithoutDash()
        )

        val addIntervalDialogState = AddEditIntervalDialogState(
            subject = "",
            startTime = null,
            endTime = null,
            date = ""
        )

        val dateFilters = listOf(
            DateFilter.All,
            DateFilter.Today,
            DateFilter.Yesterday,
            DateFilter.ThisWeek,
            DateFilter.LastWeek,
            DateFilter.CustomRange("", "")
        )
    }
}
