package com.example.android_chatbot.ui.chat_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.model.azure.AzureOpenAIService

//
class ChatViewModel(
    private val messageDAO: MessageDAO,
    private val settingDAO: SettingDAO,
    private val channelId: Int
) : ViewModel() {
    class Factory(
        private val messageDAO: MessageDAO,
        private val settingDAO: SettingDAO,
        private val channelId: Int
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ChatViewModel(messageDAO, settingDAO, channelId) as T
    }

    init {
        AzureOpenAIService.init(settingDAO)
    }
}
