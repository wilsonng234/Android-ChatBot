package com.example.android_chatbot.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_chatbot.ui.chattingscreen.Message

@Composable
fun MessageBubble(message: Message) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .wrapContentWidth(if (message.role == "user") Alignment.End else Alignment.Start)
    ) {
        Text(
            text = message.content, modifier = Modifier.padding(16.dp), fontSize = 16.sp
        )
    }
}