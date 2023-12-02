package com.example.android_chatbot.ui.setting_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android_chatbot.data.setting.Setting
import com.example.android_chatbot.data.setting.SettingDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(private val settingDAO: SettingDAO) : ViewModel() {
    class Factory(
        private val settingDAO: SettingDAO,
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingViewModel(settingDAO) as T
    }

    val settings = mutableStateListOf<Setting>()

    fun insertSetting(setting: Setting) {
        settings.add(setting)

        viewModelScope.launch(Dispatchers.IO) {
            settingDAO.insertAll(setting)
        }
    }
}