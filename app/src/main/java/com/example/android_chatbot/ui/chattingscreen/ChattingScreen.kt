package com.example.android_chatbot.ui.chattingscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_chatbot.ui.components.MessageBubble
import com.example.android_chatbot.ui.components.RoundedInputField


@Composable
fun ChattingScreen(channelId: Int, modifier: Modifier = Modifier) {
    val viewModel: ChattingViewModel = viewModel(factory = ChattingViewModel.Factory(channelId))

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp), reverseLayout = true
        ) {
            items(viewModel.messages.reversed()) { message ->
                MessageBubble(message)
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
