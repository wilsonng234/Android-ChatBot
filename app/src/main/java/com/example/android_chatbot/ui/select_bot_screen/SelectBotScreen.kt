package com.example.android_chatbot.ui.select_bot_screen

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.data.channel.Channel
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.ui.components.BotInformationCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun SelectBotScreen(
    channelDAO: ChannelDAO, handleChatRoomClicked: (Long) -> Unit, modifier: Modifier = Modifier
) {
    BotInformationCard(
        icon = Icons.Filled.Menu,
        service = "Azure OpenAI",
        description = "Hello! How can I assist you today?",
        handleOnClicked = {
            CoroutineScope(Dispatchers.IO).launch {
                val channelIds = channelDAO.insertAll(Channel(service = "Azure OpenAI"))
                val channelId = channelIds[0]

                withContext(Dispatchers.Main) {
                    handleChatRoomClicked(channelId)
                }
            }
        },
        modifier = modifier.height(150.dp)
    )
}
