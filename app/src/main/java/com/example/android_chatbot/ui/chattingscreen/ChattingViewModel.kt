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

    fun sendMessage(text: String, isUser: Boolean = true) {
        messages.add(Message(text, "user"))
        if (isUser) {
            viewModelScope.launch {
                val service = AzureOpenAIService(
                    "ea86fbb837a84230aa8acb2993eae139",
                    "https://hkust.azure-api.net/openai/deployments/gpt-35-turbo/chat/completions?api-version=2023-05-15"
                )
                val response = service.getChatResponse(messages.toList())
                messages.add((Message(response, "assistant")))
            }
        }
    }

    fun printMessage(text: String, isUser: Boolean) {
        if (isUser) {
            messages.add(Message(text, "user"))
        } else {
            messages.add(Message(text, "assistant"))
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