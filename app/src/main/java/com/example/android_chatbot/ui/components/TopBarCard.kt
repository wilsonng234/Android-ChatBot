package com.example.android_chatbot.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.R

@Composable
fun TopBarCard(
    channelId: Long, service: String, model: String, topic: String, modifier: Modifier = Modifier
) {
    val ser = when (service) {
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
    Column(
        modifier = modifier.height(140.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxHeight(0.99f)
        ) {
            Image(
                painter = painterResource(id = ser),
                contentDescription = ser.toString(),
                modifier = modifier
                    .padding(12.dp)
                    .weight(0.2f)
            )
            Column(
                modifier = modifier
                    .padding(8.dp)
                    .weight(0.8f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = topic,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = modifier.padding(top = 2.dp, bottom = 3.dp),
                    )
                }
                Text(
                    text = "$service $model",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = modifier.fillMaxWidth(0.7f)
                )


            }
        }
    }
}


