package com.example.android_chatbot.data.setting

import com.example.android_chatbot.data.message.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingRepository(private val settingDAO: SettingDAO) {
    suspend fun insertSettings(vararg settings: Setting) {
        withContext(Dispatchers.IO) {
            settingDAO.insertAll(*settings)
        }
    }

    suspend fun getAllMessages(): List<Setting> {
        return withContext(Dispatchers.IO) {
            settingDAO.getAll()
        }
    }

    suspend fun getSettingByCategory(category: String): Setting {
        return withContext(Dispatchers.IO) {
            settingDAO.getSettingByCategory(category)
        }
    }
}