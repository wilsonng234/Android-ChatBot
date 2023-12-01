package com.example.android_chatbot.ui.chattingscreen

import androidx.compose.runtime.mutableStateListOf
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
class ChattingViewModel(
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
            ChattingViewModel(messageDAO, settingDAO, channelId) as T
    }

    val messages = mutableStateListOf<Message>()

    init {
        AzureOpenAIService.init(settingDAO)
    }

    fun insertMessage(message: Message) {
        messages.add(message)

        viewModelScope.launch(Dispatchers.IO) {
            messageDAO.insertAll(message)
        }
    }

    fun sendMessage(text: String) {
        insertMessage(
            Message(
                channelId = channelId,
                role = "user",
                content = text,
                createdTime = System.currentTimeMillis()
            )
        )

        viewModelScope.launch {
            val response = AzureOpenAIService.getChatResponse(messages.toList())
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
