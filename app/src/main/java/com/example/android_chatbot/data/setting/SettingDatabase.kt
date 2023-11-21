package com.example.android_chatbot.data.setting

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android_chatbot.data.message.MessageDatabase

abstract class SettingDatabase: RoomDatabase() {
    abstract fun settingDAO(): SettingDAO

    companion object
    {
        @Volatile
        private lateinit var INSTANCE: SettingDatabase

        fun getDatabase(context: Context): SettingDatabase {
            synchronized(this) {
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        SettingDatabase::class.java,
                        "setting.db"
                    ).build()
                }

                return INSTANCE
            }
        }
    }
}