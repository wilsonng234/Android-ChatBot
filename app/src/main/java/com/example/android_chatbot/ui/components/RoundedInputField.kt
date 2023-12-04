package com.example.android_chatbot.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.R
import com.example.android_chatbot.ui.theme.AndroidChatBotTheme

/**
 * Composable that allows the user to input message
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(50.dp),
        trailingIcon = { SendIconButton(onClick = onSendMessage, modifier = modifier) },
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
    )
}

/**
 * Composable that displays the send icon button
 */
@Composable
private fun SendIconButton(
    onClick: () -> Unit = {},
    color: Color = MaterialTheme.colorScheme.secondary,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.ArrowForward,
            contentDescription = stringResource(R.string.send),
            tint = color,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun RoundedInputFieldPreview() {
    AndroidChatBotTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier, color = MaterialTheme.colorScheme.background
        ) {
            var prompt by remember { mutableStateOf("") }
            val onSendMessage = {}

            RoundedInputField(
                value = prompt, onValueChange = { prompt = it }, onSendMessage = onSendMessage
            )
        }
    }
}
