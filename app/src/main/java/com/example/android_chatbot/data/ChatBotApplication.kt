package com.example.android_chatbot.data

import android.app.Application
import com.example.android_chatbot.data.channel.ChannelRepository
import com.example.android_chatbot.data.message.MessageRepository
import com.example.android_chatbot.data.setting.SettingRepository

class ChatBotApplication : Application() {
    private val chatBotDatabase by lazy { ChatBotDatabase.getDatabase(this) }

    val channelRepository by lazy { ChannelRepository(chatBotDatabase.channelDAO()) }
    val messageRepository by lazy { MessageRepository(chatBotDatabase.messageDAO()) }
    val settingRepository by lazy { SettingRepository(chatBotDatabase.settingDAO()) }
}
