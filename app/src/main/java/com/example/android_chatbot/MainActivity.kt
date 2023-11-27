package com.example.android_chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android_chatbot.ui.chattingscreen.ChattingScreen
import com.example.android_chatbot.ui.theme.AndroidChatBotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidChatBotTheme {
                ChattingScreen()
            }
        }
    }
}
