package com.example.android_chatbot.ui.setting_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.data.DataSource
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.ui.components.FormInputField

@Composable
fun SettingScreen(
    settingDAO: SettingDAO, modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (service in DataSource.services) {
            var apiKey by remember { mutableStateOf("") }

            FormInputField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = service,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
