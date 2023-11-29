package com.example.android_chatbot.ui.chattingscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android_chatbot.data.message.Message
import com.example.android_chatbot.model.azure.AzureOpenAIService
import kotlinx.coroutines.launch

//
class ChattingViewModel(private val channelId: Int) : ViewModel() {
    class Factory(private val channelId: Int) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ChattingViewModel(channelId) as T
    }

    val messages = mutableStateListOf<Message>()
    private val chatBotService = AzureOpenAIService(
        "ea86fbb837a84230aa8acb2993eae139",
        "https://hkust.azure-api.net/openai/deployments/gpt-35-turbo/chat/completions?api-version=2023-05-15"
    )

    fun insertMessage(message: Message) {
        messages.add(message)
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
            val response = chatBotService.getChatResponse(messages.toList())
            insertMessage(
                Message(
                    channelId = channelId,
                    role = "assistant",
                    content = response,
                    createdTime = System.currentTimeMillis()
                )
            )
        }
    }
}
