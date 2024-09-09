package com.example.myapplication.timesheet.presentation.components.addeditdialog

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun SubjectTextField(
    subject: String,
    onSubjectChanged: (String) -> Unit
) {
    val description = stringResource(R.string.subject)
    OutlinedTextField(
        textStyle = TextStyle.Default.copy(
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        ),
        value = TextFieldValue(subject, selection = TextRange(subject.length)),
        onValueChange = { onSubjectChanged(it.text) },
        label = {
            Text(
                text = description,
                fontWeight = FontWeight.Bold
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        maxLines = 3,
        modifier = Modifier.semantics { contentDescription = description }
    )
}