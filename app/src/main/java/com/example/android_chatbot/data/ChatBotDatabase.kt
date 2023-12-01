package com.example.android_chatbot.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android_chatbot.data.channel.Channel
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.Message
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.data.setting.Setting
import com.example.android_chatbot.data.setting.SettingDAO

@Database(
    entities = [Channel::class, Message::class, Setting::class],
    version = 1,
    exportSchema = false
)
abstract class ChatBotDatabase : RoomDatabase() {
    abstract fun channelDAO(): ChannelDAO
    abstract fun messageDAO(): MessageDAO
    abstract fun settingDAO(): SettingDAO

    companion object {
        @Volatile
        private lateinit var INSTANCE: ChatBotDatabase

        fun getDatabase(context: Context): ChatBotDatabase {
            synchronized(this) {
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, ChatBotDatabase::class.java, "channel.db"
                    ).build()
                }

                return INSTANCE
            }
        }
    }
}
