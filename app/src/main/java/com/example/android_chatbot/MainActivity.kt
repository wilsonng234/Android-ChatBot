package com.example.android_chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_chatbot.ui.chattingscreen.ChatViewModel
import com.example.android_chatbot.ui.chattingscreen.ChattingScreen
import com.example.android_chatbot.ui.chattingscreen.MessageBubble

import com.example.android_chatbot.ui.theme.AndroidChatBotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidChatBotTheme {
                val chatViewModel = viewModel<ChatViewModel>()
                ChattingScreen(chatViewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidChatBotTheme {
        val chatViewModel = viewModel<ChatViewModel>()
        ChattingScreen(chatViewModel)
        chatViewModel.printMessage("Hi", true)
        chatViewModel.printMessage("Hello! How can I assist you today?", false)
    }
}