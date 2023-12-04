package com.example.android_chatbot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_chatbot.R

@Composable
fun BotInformationCard(
    iconId: Int,
    contentDescription: String? = null,
    service: String,
    model: String,
    handleOnClicked: () -> Unit,
    modifier: Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth()
            .clickable { handleOnClicked() }) {
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
                text = service,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = modifier
            )

            Text(
                text = model,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.padding(top = 4.dp)
            )
        }
    }
}


@Preview
@Composable
fun BotInformationCardPreview() {
    BotInformationCard(
        iconId = R.drawable.azure,
        service = "Azure",
        model = "gpt-4",
        handleOnClicked = {},
        modifier = Modifier.height(125.dp)
    )
}