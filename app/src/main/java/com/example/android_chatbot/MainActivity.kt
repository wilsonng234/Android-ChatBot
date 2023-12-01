package com.example.android_chatbot

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android_chatbot.data.ChatBotApplication
import com.example.android_chatbot.data.channel.Channel
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.data.setting.Setting
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.ui.chattingscreen.ChattingScreen
import com.example.android_chatbot.ui.theme.AndroidChatBotTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MainActivity : ComponentActivity() {
    private val channelDAO: ChannelDAO by lazy { (application as ChatBotApplication).channelDAO }
    private val messageDAO: MessageDAO by lazy { (application as ChatBotApplication).messageDAO }
    private val settingDAO: SettingDAO by lazy { (application as ChatBotApplication).settingDAO }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            channelDAO.insertAll(
                Channel(id=1, service = "azure")
            )
            settingDAO.insertAll(
                Setting(service = "azure", apiKey = "ea86fbb837a84230aa8acb2993eae139")
            )

            Log.d("onCreate", "onCreate: ${channelDAO.getAll()}")
            Log.d("onCreate", "onCreate: ${messageDAO.getAll()}")
            Log.d("onCreate", "onCreate: ${settingDAO.getAll()}")
        }

        setContent {
            AndroidChatBotTheme {
                ChattingScreen(
                    channelDAO = channelDAO,
                    messageDAO = messageDAO,
                    settingDAO = settingDAO,
                    channelId = 1
                )
            }
        }
    }
}
