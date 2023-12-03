package com.example.android_chatbot.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.android_chatbot.R
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.ui.components.ChatHistoryCard

@Composable
fun StartScreen(
    channelDAO: ChannelDAO, messageDAO: MessageDAO,
    onClick: (Int)->Unit,modifier: Modifier = Modifier
) {
    val channels by channelDAO.getAll().collectAsState(initial = emptyList())
    if(channels.isNotEmpty()){
        for (channel in channels) {
            val messages by messageDAO.getMessagesByChannelId(channel.id)
                .collectAsState(initial = emptyList())
            val lastMessage = messages.lastOrNull()
            var ser = if(channel.service.contains("azure")){
                R.drawable.azure
            }else if(channel.service.contains("openai")){
                R.drawable.openai
            }else{
                Log.i("service", channel.service)
                throw IllegalStateException("Unknown service")
            }


            ChatHistoryCard(
                iconId = ser,
                cnlId = channel.id  ,
                service = channel.service,
                model = "ChatGPTToDO",
                title = "Title",
                recentChat = lastMessage?.content ?: "",
                time = lastMessage?.createdTime,
                onClick = onClick,
                modifier = modifier
            )
        }
    }

}
