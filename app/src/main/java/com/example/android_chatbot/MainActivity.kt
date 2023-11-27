package com.example.android_chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_chatbot.ui.chattingscreen.ChattingViewModel
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

@Preview(showBackground = true)
@Composable
fun ChattingPreview() {
    AndroidChatBotTheme {
        val chattingViewModel = viewModel<ChattingViewModel>()
        ChattingScreen(chattingViewModel)
        chattingViewModel.printMessage("Hi", true)
        chattingViewModel.printMessage("Hello! How can I assist you today?", false)
    }
}