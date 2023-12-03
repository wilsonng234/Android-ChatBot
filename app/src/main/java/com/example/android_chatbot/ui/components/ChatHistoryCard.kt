package com.example.android_chatbot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatHistoryCard(
    iconId: Int,
    channelId: Int,
    service: String,
    model: String,
    topic: String,
    recentChat: String,
    time: Long?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var formattedDate = "";
    if (time != null) {
        val dateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
        val date = time?.let { Date(it) }
        formattedDate = dateFormat.format(date)
    }

    Column(
        modifier = modifier.height(140.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxHeight(0.99f)
                .clickable { onClick(channelId) }) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = iconId.toString(),
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
                        text = "$service $model",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = modifier.fillMaxWidth(0.7f)
                    )
                    Text(
                        text = formattedDate, modifier = modifier.fillMaxWidth(),
                    )
                }

                Text(
                    text = topic,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier.padding(top = 2.dp, bottom = 3.dp),
                )

                Text(
                    text = recentChat,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier
                )
            }
        }

        Divider()
    }
}

@Preview
@Composable
fun ChatHistoryCardPreview() {
    ChatHistoryCard(iconId = R.drawable.azure,
        channelId = 0,
        service = "Azure OpenAI",
        model = "gpt-4",
        topic = "title",
        recentChat = "recent chat ...",
        time = 12L,
        onClick = {})
}
