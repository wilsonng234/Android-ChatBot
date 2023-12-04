package com.example.android_chatbot.ui.select_bot_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.R
import com.example.android_chatbot.data.DataSource.servicesToModels
import com.example.android_chatbot.data.channel.Channel
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.ui.components.BotInformationCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class BotInformation(
    val iconId: Int, val service: String, val model: String
)

@Composable
fun SelectBotScreen(
    channelDAO: ChannelDAO,
    settingDAO: SettingDAO,
    handleChatRoomClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var botInformationList by remember { mutableStateOf<List<BotInformation>>(emptyList()) }
    val allSettings by settingDAO.getAll().collectAsState(initial = emptyList())

    LaunchedEffect(allSettings) {
        val temp = botInformationList.map { it }.toMutableList()

        for (entry in servicesToModels.entries.iterator()) {
            val service = entry.key
            if (!allSettings.any { it.service == service }) {
                continue
            }

            val iconId = when (service) {
                "Azure OpenAI" -> {
                    R.drawable.azure
                }

                "OpenAI" -> {
                    R.drawable.openai
                }

                else -> {
                    throw IllegalStateException("Unknown service")
                }
            }

            for (model in entry.value) {
                temp.add(BotInformation(iconId, service, model))
            }
        }

        botInformationList = temp
    }

    Column {
        Divider(
            modifier = modifier
                .padding(top = 8.dp)
                .padding(horizontal = 8.dp)
        )

        LazyColumn {
            items(botInformationList) {
                BotInformationCard(
                    iconId = it.iconId,
                    contentDescription = null,
                    service = it.service,
                    model = it.model,
                    handleOnClicked = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val channelIds = channelDAO.insertAll(
                                Channel(
                                    service = it.service, model = it.model
                                )
                            )
                            val channelId = channelIds[0]

                            withContext(Dispatchers.Main) {
                                handleChatRoomClicked(channelId)
                            }
                        }
                    },
                    modifier = modifier
                )

                Divider(modifier = modifier.padding(horizontal = 8.dp))
            }
        }
    }
}
