package com.example.android_chatbot.ui

import androidx.compose.runtime.Composable
import com.example.android_chatbot.R
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.ui.components.ChatHistoryCard

@Composable
fun StartScreen(channelDAO: ChannelDAO,
                messageDAO: MessageDAO
){
    val channels = channelDAO.getAll()
    var ser:Int = 0
    for( channel in channels){
        var message = messageDAO.getMessagesByChannelId(channel.id)[0]
        if(channel.service.contains("azure")){
            ser = R.drawable.azure
        }else if(channel.service.contains("azure")){
            ser = R.drawable.openai
        }
        ChatHistoryCard(ser,channel.service, model = "",
            service = channel.service, title= " ", recentChat= message.content,
            time = message.createdTime.toString()){}
    }

}