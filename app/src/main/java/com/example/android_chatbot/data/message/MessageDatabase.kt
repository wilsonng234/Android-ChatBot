package com.example.android_chatbot.data.message

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 1)
abstract class MessageDatabase: RoomDatabase() {
    abstract fun messageDAO(): MessageDAO

    companion object
    {
        @Volatile
        private lateinit var INSTANCE: MessageDatabase

        fun getDatabase(context: Context): MessageDatabase {
            synchronized(this) {
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MessageDatabase::class.java,
                        "message.db"
                    ).build()
                }

                return INSTANCE
            }
        }
    }
}
