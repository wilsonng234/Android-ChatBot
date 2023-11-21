package com.example.android_chatbot.data.message

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageRepository(private val messageDAO: MessageDAO) {
    suspend fun insertMessages(vararg messages: Message) {
        withContext(Dispatchers.IO) {
            messageDAO.insertAll(*messages)
        }
    }

    suspend fun getAllMessages(): List<Message> {
        return withContext(Dispatchers.IO) {
            messageDAO.getAll()
        }
    }
}
