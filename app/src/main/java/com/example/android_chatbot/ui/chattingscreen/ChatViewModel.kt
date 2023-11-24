package com.example.android_chatbot.ui.chattingscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messages = mutableStateListOf<Message>()

    fun sendMessage(text: String, isUser: Boolean = true) {
        messages.add(Message(text, "user"))
        if (isUser) {
            viewModelScope.launch {
//                val response = messages
            }
        }
    }

    fun printMessage(text: String, isUser: Boolean) {
        if (isUser) {
            messages.add(Message(text, "user"))
        } else {
            messages.add(Message(text, "bot"))
        }
    }
}

data class Message(val content: String, val role: String) {
    val isUser: Boolean
        get() = role == "user"
}