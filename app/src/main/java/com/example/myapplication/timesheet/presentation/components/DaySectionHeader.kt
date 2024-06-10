package com.example.myapplication.timesheet.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.presentation.model.DaySection
import com.example.myapplication.utils.changeDateFormat

@Composable
fun DaySectionHeader(
    daySection: DaySection,
    index: Int,
    collapsed: Boolean,
    collapsedState: SnapshotStateList<Boolean>
) {
    Spacer(modifier = Modifier.height(16.dp))
    ListItem(
        headlineContent = { Text(changeDateFormat(daySection.headerDate)) },
        shadowElevation = 2.dp,
        colors = ListItemDefaults.colors(
            containerColor = Color.LightGray
        ),
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(daySection.headerTimeAmount)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    collapsedState[index] = !collapsed
                }) {
                    Icon(
                        imageVector = if (collapsed) {
                            Icons.Default.ArrowDropDown
                        } else {
                            Icons.Default.ArrowDropUp
                        },
                        contentDescription = null
                    )
                }
            }
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
}