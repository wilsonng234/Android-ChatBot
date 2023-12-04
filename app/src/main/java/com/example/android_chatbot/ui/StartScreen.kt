package com.example.android_chatbot.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.android_chatbot.R
import com.example.android_chatbot.data.DataSource
import com.example.android_chatbot.data.channel.Channel
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.Message
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.model.ChatBotService
import com.example.android_chatbot.model.azure.AzureOpenAIService
import com.example.android_chatbot.model.openai.OpenAIService
import com.example.android_chatbot.ui.components.BotChip
import com.example.android_chatbot.ui.components.ChatHistoryCard
import com.example.android_chatbot.ui.components.RoundedInputField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun StartScreen(
    channelDAO: ChannelDAO,
    messageDAO: MessageDAO,
    settingDAO: SettingDAO,
    onClick: (Long) -> Unit,
    handleEnteringChatRoom: (channelId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val channels by channelDAO.getAll().collectAsState(initial = emptyList())
    val (inputPrompt, setInputPrompt) = remember { mutableStateOf("") }
    val channelsLastMessage = channels.map { channel ->
        val messages by messageDAO.getMessagesByChannelId(channel.id)
            .collectAsState(initial = emptyList())
        channel to messages.lastOrNull()
    }
    val sortedChannelsLastMessage = channelsLastMessage.sortedByDescending { channelsLastMessage ->
        channelsLastMessage.second?.createdTime ?: 0

    }
    var availableBots by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    val allSettings by settingDAO.getAll().collectAsState(initial = emptyList())

    LaunchedEffect(allSettings) {
        val temp = mutableListOf<Pair<String, String>>()

        for (entry in DataSource.servicesToModels.entries.iterator()) {
            val service = entry.key
            if (!allSettings.any { it.service == service }) {
                continue
            }

            for (model in entry.value) {
                temp.add(Pair(service, model))
            }
        }

        availableBots = temp
    }

    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = modifier) {
        LazyColumn(modifier = modifier.fillMaxHeight(0.8f)) {
            items(sortedChannelsLastMessage) { channelLastMessage ->
                val channel = channelLastMessage.first
                val lastMessage = channelLastMessage.second

                val ser = when (channel.service) {
                    "Azure OpenAI" -> {
                        R.drawable.azure
                    }

                    "OpenAI" -> {
                        R.drawable.openai
                    }

                    else -> {
                        throw IllegalStateException("Unknown service")
                    }
                }

                val recentChat = lastMessage?.content ?: ""
                val time = lastMessage?.createdTime

                ChatHistoryCard(
                    iconId = ser,
                    channelId = channel.id,
                    service = channel.service,
                    model = channel.model,
                    topic = channel.topic,
                    recentChat = recentChat,
                    time = time,
                    onClick = onClick,
                    cardHeight = 110,
                    modifier = modifier
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            val (selectedService, setSelectedService) = remember { mutableStateOf("") }
            val (selectedModel, setSelectedModel) = remember { mutableStateOf("") }

            LazyRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
            ) {
                items(availableBots) { availableBot ->
                    val service = availableBot.first
                    val model = availableBot.second

                    BotChip(
                        label = "$service $model".uppercase(),
                        selected = selectedService == service && selectedModel == model,
                        handleBotChipClicked = {
                            setSelectedService(service)
                            setSelectedModel(model)
                        },
                        leadingIcon = {},
                        modifier = modifier.padding(horizontal = 5.dp)
                    )
                }
            }

            RoundedInputField(
                value = inputPrompt,
                onValueChange = setInputPrompt,
                enabled = selectedService.isNotEmpty() && selectedModel.isNotEmpty(),
                onSendMessage = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val service: ChatBotService = when (selectedService) {
                            "Azure OpenAI" -> AzureOpenAIService
                            "OpenAI" -> OpenAIService
                            else -> throw Exception("Invalid service")
                        }
                        service.init(settingDAO)

                        val channelIds = channelDAO.insertAll(
                            Channel(
                                service = selectedService, model = selectedModel
                            )
                        )
                        val channelId = channelIds[0]

                        val inputMessage = Message(
                            channelId = channelId,
                            role = "user",
                            content = inputPrompt,
                            createdTime = System.currentTimeMillis()
                        )
                        messageDAO.insertAll(inputMessage)

                        async {
                            val response = service.getChatResponse(
                                messages = listOf(inputMessage), model = selectedModel
                            )
                            messageDAO.insertAll(
                                Message(
                                    channelId = channelId,
                                    role = "assistant",
                                    content = response.first,
                                    createdTime = System.currentTimeMillis()
                                )
                            )

                            val topicQuestion = Message(
                                channelId = channelId,
                                role = "user",
                                content = "Give me the topic of this sentence with a sentence not more than three words.\\n:$inputPrompt",
                                createdTime = System.currentTimeMillis()
                            )
                            val topicResponse = service.getChatResponse(
                                messages = listOf(topicQuestion), model = selectedModel
                            )

                            if (topicResponse.second) {
                                channelDAO.updateChannelTopic(topicResponse.first, channelId)
                            }
                        }

                        withContext(Dispatchers.Main) {
                            handleEnteringChatRoom(channelId)
                        }

                        setInputPrompt("")
                    }
                },
                modifier = modifier
            )
        }
    }
}
