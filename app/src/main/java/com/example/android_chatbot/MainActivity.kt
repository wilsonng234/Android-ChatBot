package com.example.android_chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android_chatbot.data.channel.Channel
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.data.setting.Setting
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.ui.theme.AndroidChatBotTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val channelDAO: ChannelDAO by lazy { (application as ChatBotApplication).channelDAO }
    private val messageDAO: MessageDAO by lazy { (application as ChatBotApplication).messageDAO }
    private val settingDAO: SettingDAO by lazy { (application as ChatBotApplication).settingDAO }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            channelDAO.insertAll(
                Channel(id = 1, service = "Azure OpenAI", model = "gpt-35-turbo"),
                Channel(id = 2, service = "Azure OpenAI", model = "gpt-4"),
                Channel(id = 3, service = "OpenAI", model = "gpt-3.5-turbo")
            )

            settingDAO.insertAll(
                Setting(service = "Azure OpenAI", apiKey = "908b37ffe5c74e158d70c41bd9e54479"),
                Setting(
                    service = "OpenAI",
                    apiKey = "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"
                )
            )
        }

        setContent {
            AndroidChatBotTheme {
                ChatBotApp(
                    channelDAO = channelDAO,
                    messageDAO = messageDAO,
                    settingDAO = settingDAO,
                )
            }
        }
    }
}
