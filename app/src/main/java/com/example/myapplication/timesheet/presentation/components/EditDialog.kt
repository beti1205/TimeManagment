package com.example.myapplication.timesheet.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.timesheet.presentation.EditIntervalDialogState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
    state: EditIntervalDialogState?,
    onSubjectChanged: (String) -> Unit,
    onStartTimeChanged: (String) -> Unit,
    onEndTimeChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onSaveClicked: () -> Unit = {}
) {
    if (state != null) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = stringResource(id = R.string.edit_menu_item_label))
            },
            text = {
                Column {
                    OutlinedTextField(
                        modifier = Modifier,
                        textStyle = TextStyle.Default.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light
                        ),
                        value = state.subject,
                        onValueChange = { onSubjectChanged(it) },
                        label = {
                            Text(
                                text = stringResource(R.string.subject),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle.Default.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            ),
                            value = state.startTime,
                            onValueChange = { onStartTimeChanged(it) },
                            label = {
                                Text(
                                    text = stringResource(R.string.edit_dialog_start_time_text_field),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            singleLine = true,
                            isError = state.isWrongStartTimeError,
                            supportingText = {
                                if (state.isWrongStartTimeError) {
                                    Text("Please enter the correct time format, e.g. 23:59:59")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle.Default.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            ),
                            value = state.endTime,
                            onValueChange = { onEndTimeChanged(it) },
                            label = {
                                Text(
                                    text = stringResource(R.string.edit_dialog_end_time_text_field),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            singleLine = true,
                            isError = state.isWrongEndTimeError,
                            supportingText = {
                                if (state.isWrongEndTimeError) {
                                    Text("Please enter the correct time format, e.g. 23:59:59")
                                }
                            }
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    onSaveClicked()
                    onDismissRequest()
                }, enabled = state.isSaveEnabled) {
                    Text(stringResource(R.string.edit_dialog_save_button))
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismissRequest
                ) {
                    Text(stringResource(R.string.edit_dialog_cancel_button))
                }
            }
        )
    }
}
