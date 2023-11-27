package com.example.android_chatbot.ui.chattingscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android_chatbot.model.azure.AzureOpenAIService
import kotlinx.coroutines.launch

//
class ChattingViewModel(private val channel: String) : ViewModel() {
    class Factory(private val channel: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ChattingViewModel(channel) as T
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
        insertMessage(Message(text, "user"))

        viewModelScope.launch {
            val response = chatBotService.getChatResponse(messages.toList())
            insertMessage(Message(response, "assistant"))
        }
    }
}

data class Message(val content: String, val role: String) {
    val isUser: Boolean
        get() = role == "user"

    override fun toString(): String {
        return """
            {
                "role": "$role",
                "content": "${content.replace("\n", "\\n")}"
            }
        """.trimIndent()
    }
}