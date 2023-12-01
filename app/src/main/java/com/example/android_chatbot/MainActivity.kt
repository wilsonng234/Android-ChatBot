package com.example.android_chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android_chatbot.data.ChatBotApplication
import com.example.android_chatbot.data.channel.ChannelRepository
import com.example.android_chatbot.data.message.MessageRepository
import com.example.android_chatbot.data.setting.SettingRepository
import com.example.android_chatbot.ui.chattingscreen.ChattingScreen
import com.example.android_chatbot.ui.theme.AndroidChatBotTheme

class MainActivity : ComponentActivity() {
    private val channelRepository: ChannelRepository by lazy { (application as ChatBotApplication).channelRepository }
    private val messageRepository: MessageRepository by lazy { (application as ChatBotApplication).messageRepository }
    private val settingRepository: SettingRepository by lazy { (application as ChatBotApplication).settingRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidChatBotTheme {
                ChattingScreen(channelId = 0)
            }
        }
    }
}
