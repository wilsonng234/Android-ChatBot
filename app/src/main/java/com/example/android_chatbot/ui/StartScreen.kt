package com.example.android_chatbot.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.R
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.ui.components.ChatHistoryCard

@Composable
fun StartScreen(
    channelDAO: ChannelDAO,
    messageDAO: MessageDAO,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val channels by channelDAO.getAll().collectAsState(initial = emptyList())

    Column(modifier = modifier) {
        Divider(
            modifier = modifier
                .padding(top = 8.dp)
                .padding(horizontal = 8.dp)
        )

        for (channel in channels) {
            val messages by messageDAO.getMessagesByChannelId(channel.id)
                .collectAsState(initial = emptyList())
            val lastMessage = messages.lastOrNull()
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

            ChatHistoryCard(
                iconId = ser,
                channelId = channel.id,
                service = channel.service,
                model = channel.model,
                topic = channel.topic,
                recentChat = lastMessage?.content ?: "",
                time = lastMessage?.createdTime,
                onClick = onClick,
                modifier = modifier
            )
        }
    }
}
