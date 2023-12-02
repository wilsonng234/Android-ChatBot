package com.example.android_chatbot

import android.app.Application
import com.example.android_chatbot.data.ChatBotDatabase

class ChatBotApplication : Application() {
    private val chatBotDatabase by lazy { ChatBotDatabase.getDatabase(this) }

    val channelDAO by lazy { chatBotDatabase.channelDAO() }
    val messageDAO by lazy { chatBotDatabase.messageDAO() }
    val settingDAO by lazy { chatBotDatabase.settingDAO() }
}
