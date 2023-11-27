package com.example.android_chatbot.ui.chattingscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_chatbot.ui.components.RoundedInputField


@Composable
fun ChattingScreen(channel: String, modifier: Modifier = Modifier) {
    val viewModel: ChattingViewModel = viewModel(factory = ChattingViewModel.Factory(channel))

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp), reverseLayout = true
        ) {
            items(viewModel.messages.reversed()) { message ->
                if (message.role == "user") {
                    MessageBubble(message.content, Alignment.End)
                } else {
                    MessageBubble(message.content, Alignment.Start)
                }
            }
        }

        var inputPrompt by remember { mutableStateOf("") }
        RoundedInputField(inputPrompt, onValueChange = { inputPrompt = it }, onSendMessage = {
            viewModel.sendMessage(inputPrompt)
            inputPrompt = ""
        }, modifier = modifier
        )
    }
}

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
