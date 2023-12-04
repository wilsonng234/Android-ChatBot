package com.example.android_chatbot.ui.chat_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_chatbot.data.channel.Channel
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.Message
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.model.azure.AzureOpenAIService
import com.example.android_chatbot.model.openai.OpenAIService
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
    var channel by remember { mutableStateOf<Channel?>(null) }
    val channelMessages by messageDAO.getMessagesByChannelId(channelId)
        .collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            channel = channelDAO.getChannelById(channelId)
        }
    }

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
                channelDAO,
                messageDAO,
                settingDAO,
                channel!!,
                channelMessages,
                inputPrompt,
                setInputPrompt
            )
        }, modifier = modifier
        )
    }
}

private fun handleOnSendMessage(
    channelDAO: ChannelDAO,
    messageDAO: MessageDAO,
    settingDAO: SettingDAO,
    channel: Channel,
    channelMessages: List<Message>,
    inputPrompt: String,
    setInputPrompt: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val service = when (channel.service) {
            "Azure OpenAI" -> AzureOpenAIService
            "OpenAI" -> OpenAIService
            else -> throw Exception("Invalid service")
        }
        service.init(settingDAO)

        val inputMessage = Message(
            channelId = channel.id,
            role = "user",
            content = inputPrompt,
            createdTime = System.currentTimeMillis()
        )

        messageDAO.insertAll(inputMessage)
        setInputPrompt("")

        val response = service.getChatResponse(
            messages = channelMessages.plus(inputMessage), model = channel.model
        )
        messageDAO.insertAll(
            Message(
                channelId = channel.id,
                role = "assistant",
                content = response.first,
                createdTime = System.currentTimeMillis()
            )
        )

        val topicQuestion = Message(
            channelId = channel.id,
            role = "user",
            content = "Give me the topic of this sentence with a sentence not more than three words.\\n:$inputPrompt",
            createdTime = System.currentTimeMillis()
        )
        val topicResponse = service.getChatResponse(
            messages = listOf(topicQuestion), model = channel.model
        )

        if (topicResponse.second) {
            channelDAO.updateChannelTopic(topicResponse.first, channel.id)
        }
    }
}
