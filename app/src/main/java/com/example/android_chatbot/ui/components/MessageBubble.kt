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

@Composable
fun MessageBubble(text: String, alignment: Alignment.Horizontal) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .wrapContentWidth(alignment)
    ) {
        Text(
            text = text, modifier = Modifier.padding(16.dp), fontSize = 16.sp
        )
    }
}