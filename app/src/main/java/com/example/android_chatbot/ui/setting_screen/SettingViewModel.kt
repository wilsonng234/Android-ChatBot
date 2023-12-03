package com.example.android_chatbot.ui.setting_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_chatbot.data.setting.SettingDAO

class SettingViewModel(private val settingDAO: SettingDAO) : ViewModel() {
    class Factory(
        private val settingDAO: SettingDAO,
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingViewModel(settingDAO) as T
    }
}
