package com.example.myapplication

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAncestors
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.test.espresso.Espresso
import androidx.test.rule.GrantPermissionRule
import com.example.myapplication.di.AppModule
import com.example.myapplication.timesheet.presentation.model.toStr
import com.example.myapplication.timesheet.presentation.model.toTime
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.utils.formatToDayMonthDate
import com.example.myapplication.utils.formatToLongDate
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

@HiltAndroidTest
@UninstallModules(AppModule::class)
class TimeIntervalEndToEndTest {

    @get:Rule(order = 0)
    val grantPermissionRule =
        if (Build.VERSION.SDK_INT >= 33) {
            GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            GrantPermissionRule.grant()
        }

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun saveTimeInterval_editAfterwards() {
        val subject = "Write UI test"
        val date = Instant.now().minus(1, ChronoUnit.DAYS)
        val startTime = "225321"
        val endTime = "225322"

        //Enter subject for time interval
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.subject_field),
                useUnmergedTree = true
            )
            .performTextInput(subject)

        Espresso.closeSoftKeyboard()

        //Start timer
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.start_button))
            .performClick()

        //Stop timer
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.stop_button))
            .performClick()

        //Go to the time sheet screen
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.timesheet))
            .performClick()

        //Make sure there is a time interval with our subject
        composeTestRule.onNodeWithText(subject).assertIsDisplayed()

        //Perform click on time interval list item with our subject
        composeTestRule
            .onNodeWithText(subject)
            .onAncestors()
            .filter(hasClickAction())
            .onFirst()
            .performClick()

        //Check whether the subject is correct and then edit the subject
        composeTestRule
            .onNodeWithContentDescription(
                label = composeTestRule.activity.getString(R.string.subject),
                useUnmergedTree = true
            )
//            .assertTextEquals(subject)
            .performTextInput("- end to end")

        Espresso.closeSoftKeyboard()

        //Clear the start date
        composeTestRule
            .onNodeWithContentDescription(
                label = composeTestRule.activity.getString(R.string.edit_dialog_start_date_label),
                useUnmergedTree = true
            )
            .performTextClearance()

        //Edit the start date
        composeTestRule
            .onNodeWithContentDescription(
                label = composeTestRule.activity.getString(R.string.edit_dialog_start_date_label),
                useUnmergedTree = true
            )
            .assertTextEquals("")
            .performTextInput(date.formatToLongDate().replace("-", ""))

        //Clear the start time
        composeTestRule
            .onNodeWithContentDescription(
                label = composeTestRule.activity.getString(R.string.edit_dialog_start_time_text_field),
                useUnmergedTree = true
            )
            .performTextClearance()

        //Edit the start time
        composeTestRule
            .onNodeWithContentDescription(
                label = composeTestRule.activity.getString(R.string.edit_dialog_start_time_text_field),
                useUnmergedTree = true
            )
            .assertTextEquals("")
            .performTextInput(startTime)

        //Clear the end time
        composeTestRule
            .onNodeWithContentDescription(
                label = composeTestRule.activity.getString(R.string.edit_dialog_end_time_text_field),
                useUnmergedTree = true
            )
            .performTextClearance()

        //Edit the end time
        composeTestRule
            .onNodeWithContentDescription(
                label = composeTestRule.activity.getString(R.string.edit_dialog_end_time_text_field),
                useUnmergedTree = true
            )
            .assertTextEquals("")
            .performTextInput(endTime)

        Espresso.closeSoftKeyboard()

        //Save the edited time interval
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.edit_dialog_save_button))
            .performClick()

        //Check if the subject has changed
        val subjectAfterEdition = "$subject- end to end"
        composeTestRule.onNodeWithText(subjectAfterEdition).assertIsDisplayed()

        //Check if the date has changed
        composeTestRule.onAllNodes(isRoot(), true).printToLog("TREE", 100)
        Log.d("TREE", date.formatToDayMonthDate())
        composeTestRule.onNodeWithText(date.formatToDayMonthDate()).assertIsDisplayed()

        //Check if the start time and end time has changed
        composeTestRule.onNodeWithText(
            startTime.toTime().toStr() + " - " + endTime.toTime().toStr()
        ).assertIsDisplayed()

        //Start searching with Search Bar
        composeTestRule.onNodeWithContentDescription(
            label = composeTestRule.activity.getString(R.string.search_bar_search_placeholder),
            useUnmergedTree = true
        ).performTextInput("")

        //Check if subject is displayed in Search Bar subject list
        composeTestRule.onNodeWithText(subjectAfterEdition).assertIsDisplayed()

        //Open the Bottom Sheet with date filter options
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.date_filter)
        ).performClick()

        //Select date filter option which is yesterday
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.date_filter_yesterday))
            .performClick()

        //Check if subject is displayed after selecting filter
        composeTestRule.onNodeWithText(subjectAfterEdition).assertIsDisplayed()

    }
}
