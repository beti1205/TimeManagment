package com.example.myapplication.timetracker

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.example.myapplication.R
import com.example.myapplication.timetracker.presentation.TimeAdjustment
import com.example.myapplication.timetracker.presentation.TimeTrackerScreen
import com.example.myapplication.timetracker.presentation.TimeTrackerScreenState
import com.example.myapplication.timetracker.presentation.getTypeName
import com.example.myapplication.utils.convertSecondsToTimeString
import com.example.myapplication.utils.formatToDayMonthDate
import com.example.myapplication.utils.formatToLongTimeStr
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class TimeTrackerScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun initialState_isRendered() {
        composeTestRule.setContent {
            TimeTrackerScreen(TimeTrackerScreenState())
        }
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.subject_field),
                useUnmergedTree = true
            )
            .assertTextEquals("")

        composeTestRule
            .onAllNodesWithText(composeTestRule.activity.getString(R.string.emptyTime))
            .assertCountEquals(2)

        composeTestRule.onNodeWithText(Instant.now().formatToDayMonthDate()).isDisplayed()

        composeTestRule.onNodeWithText(0L.convertSecondsToTimeString()).isDisplayed()

        TimeAdjustment.entries.forEach { adjustment ->
            val name = adjustment.getTypeName()
            composeTestRule
                .onNodeWithText(composeTestRule.activity.getString(name))
                .isDisplayed()

            composeTestRule
                .onNodeWithText(composeTestRule.activity.getString(name))
                .assertIsNotSelected()
        }
    }

    @Test
    fun subjectDropDown_setUpSubject_subjectIsDisplayed() {
        val subject = "subject"
        composeTestRule.setContent {
            TimeTrackerScreen(
                TimeTrackerScreenState(
                    workingSubject = subject
                )
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.subject_field),
                useUnmergedTree = true
            )
            .assertTextEquals(subject)
    }

    @Test
    fun timeAdjustmentChips_setUpChip_selectedChipIsDisplayed() {
        composeTestRule.setContent {
            TimeTrackerScreen(
                TimeTrackerScreenState(
                    selectedTimeAdjustment = TimeAdjustment.PLUS_5
                )
            )
        }
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.plus_5))
            .assertIsSelected()
    }

    @Test
    fun timeInterval_setUpTimeInterval_timeIntervalIsDisplayed() {
        val startTime = Instant.now().minus(1, ChronoUnit.HOURS)
        val endTime = Instant.now()
        composeTestRule.setContent {
            TimeTrackerScreen(
                TimeTrackerScreenState(
                    startTime = startTime,
                    endTime = endTime
                )
            )
        }

        composeTestRule.onNodeWithText(startTime.formatToLongTimeStr()).isDisplayed()
        composeTestRule.onNodeWithText(endTime.formatToLongTimeStr()).isDisplayed()
        composeTestRule.onNodeWithText(endTime.formatToDayMonthDate()).isDisplayed()
    }

    @Test
    fun timer_setUpTimeElapsed_isDisplayed() {
        val timeElapsed = 12L
        composeTestRule.setContent {
            TimeTrackerScreen(
                TimeTrackerScreenState(
                    timeElapsed = timeElapsed
                )
            )
        }

        composeTestRule.onNodeWithText(timeElapsed.convertSecondsToTimeString())
    }

    @Composable
    private fun TimeTrackerScreen(state: TimeTrackerScreenState) {
        TimeTrackerScreen(
            timeAmount = state.timeElapsed.convertSecondsToTimeString(),
            timeAmountInMilliseconds = state.timeAmountInMilliseconds,
            isActive = state.isActive,
            showEndTime = state.showEndTime,
            startTime = state.startTime,
            endTime = state.endTime,
            workingSubject = state.workingSubject,
            chipsEnabled = state.chipsEnabled,
            isSubjectErrorOccurred = state.isSubjectErrorOccurred,
            selectedTimeAdjustment = state.selectedTimeAdjustment,
            filteredSubjects = state.filteredSubjects,
            onTimerToggled = {},
            onResetClicked = {},
            onSubjectErrorChanged = {},
            onWorkingSubjectChanged = {},
            onNavigateToTimeSheet = {},
            onTypeSelected = {}
        )
    }
}
