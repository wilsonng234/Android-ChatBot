package com.example.android_chatbot.data

import android.app.Application
import com.example.android_chatbot.data.channel.ChannelRepository
import com.example.android_chatbot.data.message.MessageRepository
import com.example.android_chatbot.data.setting.SettingRepository

class ChatBotApplication : Application() {
    val chatbotDatabase by lazy { ChatbotDatabase.getDatabase(this) }

    val channelRepository by lazy { ChannelRepository(chatbotDatabase.channelDAO()) }
    val messageRepository by lazy { MessageRepository(chatbotDatabase.messageDAO()) }
    val settingRepository by lazy { SettingRepository(chatbotDatabase.settingDAO()) }
}
