package com.example.android_chatbot.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputField(
    label: String,
    initialValue: String = "",
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    var value by rememberSaveable { mutableStateOf(initialValue) }

    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        modifier = modifier,
        label = { Text(label) },
        singleLine = singleLine,
    )
}

@Preview
@Composable
fun FormInputFieldPreview() {
    FormInputField(label = "label", initialValue = "initialValue")
}
