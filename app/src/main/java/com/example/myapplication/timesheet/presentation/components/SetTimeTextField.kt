package com.example.myapplication.timesheet.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.utils.MaskVisualTransformation

object TimeDefaults {
    const val TIME_MASK = "##:##:##"
    const val TIME_LENGTH = 6
}

@Composable
fun SetTimeTextField(
    time: String,
    isWrongTimeError: Boolean,
    labelText: String,
    onTimeChanged: (String) -> Unit,
    onTimePickerClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        modifier = modifier,
        textStyle = TextStyle.Default.copy(
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        ),
        value = time,
        onValueChange = { if (it.length <= TimeDefaults.TIME_LENGTH) onTimeChanged(it) },
        label = {
            Text(
                text = labelText,
                fontWeight = FontWeight.Bold
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        isError = isWrongTimeError,
        supportingText = {
            if (isWrongTimeError) {
                Text(stringResource(R.string.incorrect_time_was_entered))
            }
        },
        visualTransformation = MaskVisualTransformation(TimeDefaults.TIME_MASK),
        trailingIcon = {
            IconButton(onClick = onTimePickerClicked) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null
                )
            }
        }
    )
}
