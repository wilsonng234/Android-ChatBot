package com.example.android_chatbot.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatHistoryCard(
    iconId: Int,
    cnlId: Int,
    service: String,
    model: String,
    title: String,
    recentChat: String,
    time: Long?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var formattedDate:String = "";
    if(time != null){
        val dateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
        val date = time?.let { Date(it) }
        formattedDate = dateFormat.format(date)
    }

    Box(modifier = modifier
        .background(color = MaterialTheme.colorScheme.background)
        .fillMaxWidth()
        .clickable { onClick(cnlId) }
        .border(BorderStroke(1.dp, Color.Black.copy(alpha = 0.3f)))) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()
        ) {
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
                Text(
                    text = "$service $model",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = modifier
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = modifier.padding(top = 2.dp, bottom = 3.dp),
                )
                Text(
                    text = recentChat,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = modifier
                )
            }
        }
        Text(
            text = formattedDate,
            modifier = modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Preview
@Composable
fun ChatHistoryCardPreview() {
    ChatHistoryCard(iconId = R.drawable.azure,
        cnlId= 0,
        service = "Azure OpenAI",
        model = "gpt-4",
        title = "title",
        recentChat = "recent chat ...",
        time = 12L,
        onClick = {})
}