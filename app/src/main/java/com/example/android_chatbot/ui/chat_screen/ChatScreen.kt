package com.example.android_chatbot.ui.chat_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.Message
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.model.azure.AzureOpenAIService
import com.example.android_chatbot.ui.components.MessageBubble
import com.example.android_chatbot.ui.components.RoundedInputField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun ChatScreen(
    channelDAO: ChannelDAO,
    messageDAO: MessageDAO,
    settingDAO: SettingDAO,
    channelId: Long,
    modifier: Modifier = Modifier
) {
    val viewModel: ChatViewModel =
        viewModel(factory = ChatViewModel.Factory(messageDAO, settingDAO, channelId))
    val channelMessages by messageDAO.getMessagesByChannelId(channelId)
        .collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp), reverseLayout = true
        ) {
            items(channelMessages.reversed()) { message ->
                MessageBubble(message)
            }
        }

        val (inputPrompt, setInputPrompt) = remember { mutableStateOf("") }
        RoundedInputField(inputPrompt, onValueChange = { setInputPrompt(it) }, onSendMessage = {
            handleOnSendMessage(
                messageDAO, channelId, channelMessages, inputPrompt, setInputPrompt
            )
        }, modifier = modifier
        )
    }
}

private fun handleOnSendMessage(
    messageDAO: MessageDAO,
    channelId: Long,
    channelMessages: List<Message>,
    inputPrompt: String,
    setInputPrompt: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val inputMessage = Message(
            channelId = channelId,
            role = "user",
            content = inputPrompt,
            createdTime = System.currentTimeMillis()
        )

        messageDAO.insertAll(inputMessage)
        setInputPrompt("")

        val response = AzureOpenAIService.getChatResponse(channelMessages.plus(inputMessage))
        messageDAO.insertAll(
            Message(
                channelId = channelId,
                role = "assistant",
                content = response.first,
                createdTime = System.currentTimeMillis()
            )
        )
    }
}
