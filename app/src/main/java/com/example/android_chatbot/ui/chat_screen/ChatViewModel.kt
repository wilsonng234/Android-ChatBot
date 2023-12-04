package com.example.android_chatbot.ui.chat_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.model.azure.AzureOpenAIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//
class ChatViewModel(
    private val messageDAO: MessageDAO,
    private val settingDAO: SettingDAO,
    private val channelId: Long
) : ViewModel() {
    class Factory(
        private val messageDAO: MessageDAO,
        private val settingDAO: SettingDAO,
        private val channelId: Long
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ChatViewModel(messageDAO, settingDAO, channelId) as T
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            AzureOpenAIService.init(settingDAO)
        }
    }
}
