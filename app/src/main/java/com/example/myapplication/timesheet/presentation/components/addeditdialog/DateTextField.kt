package com.example.myapplication.timesheet.presentation.components.addeditdialog

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.utils.MaskVisualTransformation

@Composable
fun DateTextField(
    date: String,
    isWrongDateError: Boolean,
    onDateChanged: (String) -> Unit,
    onDatePickerSelected: () -> Unit
) {
    OutlinedTextField(
        textStyle = TextStyle.Default.copy(
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        ),
        value = date,
        onValueChange = { if (it.length <= DateDefaults.DATE_LENGTH) onDateChanged(it) },
        label = {
            Text(
                text = stringResource(R.string.edit_dialog_start_date_label),
                fontWeight = FontWeight.Bold
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        isError = isWrongDateError,
        supportingText = {
            if (isWrongDateError) {
                Text(stringResource(R.string.edit_dialog_error_supporting_text))
            }
        },
        trailingIcon = {
            IconButton(onClick = onDatePickerSelected) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = null
                )
            }
        },
        visualTransformation = MaskVisualTransformation(DateDefaults.DATE_MASK),
    )
}