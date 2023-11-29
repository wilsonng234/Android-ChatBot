package com.example.android_chatbot.data.channel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Channel::class], version = 1)
abstract class ChannelDatabase : RoomDatabase() {
    abstract fun channelDAO(): ChannelDAO

    companion object {
        @Volatile
        private lateinit var INSTANCE: ChannelDatabase

        fun getDatabase(context: Context): ChannelDatabase {
            synchronized(this) {
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, ChannelDatabase::class.java, "channel.db"
                    ).build()
                }

                return INSTANCE
            }
        }
    }
}
