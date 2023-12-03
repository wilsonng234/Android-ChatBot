package com.example.android_chatbot.ui.select_bot_screen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.R
import com.example.android_chatbot.data.DataSource.servicesToModels
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
    LazyColumn() {
        for (entry in servicesToModels.entries.iterator()) {
            val iconId = if (entry.key == "Azure OpenAI") {
                R.drawable.azure
            } else if (entry.key == "OpenAI") {
                R.drawable.openai
            } else {
                throw IllegalStateException("Unknown service")
            }
            items(entry.value.size) {
                BotInformationCard(
                    iconId = iconId,
                    service = entry.key,
                    model = entry.value[it],
                    handleOnClicked = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val channelIds = channelDAO.insertAll(
                                Channel(
                                    service = entry.key, model = entry.value[it]
                                )
                            )
                            val channelId = channelIds[0]

                            withContext(Dispatchers.Main) {
                                handleChatRoomClicked(channelId)
                            }
                        }
                    },
                    modifier = modifier.height(125.dp)
                )
                Divider(color = Color.LightGray, modifier = Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}
