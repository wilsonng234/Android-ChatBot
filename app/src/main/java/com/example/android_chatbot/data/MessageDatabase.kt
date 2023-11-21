package com.example.android_chatbot.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 1)
abstract class MessageDatabase: RoomDatabase() {
    abstract fun messageDAO(): MessageDAO
}
