package com.example.myapplication.timesheet.presentation.components.daysections

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimeIntervalHeadlineContent(workingSubject: String) {
    val tooltipState = remember { TooltipState() }
    val scope = rememberCoroutineScope()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = {
            RichTooltip {
                Text(
                    text = workingSubject,
                )
            }
        },
        state = tooltipState
    ) {
        Text(
            text = workingSubject,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable { scope.launch { tooltipState.show() } }
        )
    }
}