package com.example.android_chatbot.ui.chat_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android_chatbot.data.message.Message
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
        AzureOpenAIService.init(settingDAO)
    }

    fun insertMessage(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            messageDAO.insertAll(message)
        }
    }

    fun sendMessage(text: String, channelMessages: List<Message>) {
        insertMessage(
            Message(
                channelId = channelId,
                role = "user",
                content = text,
                createdTime = System.currentTimeMillis()
            )
        )

        viewModelScope.launch {
            val response = AzureOpenAIService.getChatResponse(channelMessages)
            if (response.second) {
                insertMessage(
                    Message(
                        channelId = channelId,
                        role = "assistant",
                        content = response.first,
                        createdTime = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}
