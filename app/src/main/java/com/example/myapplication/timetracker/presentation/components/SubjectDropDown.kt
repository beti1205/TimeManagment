package com.example.myapplication.timetracker.presentation.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDropDown(
    subject: String,
    isSubjectChangeEnabled: Boolean,
    isSubjectErrorOccurred: Boolean,
    filteredSubjectList: List<String>,
    onWorkingSubjectChanged: (String) -> Unit = {},
    clearFocus: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier.padding(horizontal = 64.dp),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .offset(y = (-32).dp),
            textStyle = TextStyle.Default.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            ),
            enabled = isSubjectChangeEnabled,
            value = subject,
            onValueChange = onWorkingSubjectChanged,
            isError = isSubjectErrorOccurred,
            label = { Text(text = stringResource(R.string.subject), fontWeight = FontWeight.Bold) },
            trailingIcon = { TrailingIcon(expanded = expanded) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            maxLines = 3,
        )
        if (filteredSubjectList.isNotEmpty() && subject.isNotBlank() && isSubjectChangeEnabled) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                filteredSubjectList.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onWorkingSubjectChanged(selectionOption)
                            expanded = false
                            clearFocus()
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}