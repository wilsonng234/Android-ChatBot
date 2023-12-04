package com.example.android_chatbot.model

import com.example.android_chatbot.data.message.Message
import com.example.android_chatbot.data.setting.SettingDAO

abstract class ChatBotService {
    abstract fun init(settingDAO: SettingDAO)
    abstract suspend fun getChatResponse(
        messages: List<Message>, model: String
    ): Pair<String, Boolean>
}
