package com.example.android_chatbot.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.R
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.ui.components.ChatHistoryCard
import com.example.android_chatbot.ui.components.RoundedInputField

@Composable
fun StartScreen(
    channelDAO: ChannelDAO,
    messageDAO: MessageDAO,
    onClick: (Long) -> Unit,
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

    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = modifier) {
        LazyColumn(modifier = modifier.fillMaxHeight(0.85f)) {
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

        RoundedInputField(
            value = inputPrompt,
            onValueChange = setInputPrompt,
            onSendMessage = { /*TODO*/ },
            modifier = modifier
        )
    }
}
