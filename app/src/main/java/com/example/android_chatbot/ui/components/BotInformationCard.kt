package com.example.android_chatbot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Box(
        modifier
            .wrapContentSize()
            .clickable { handleOnClicked() },
    ) {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = contentDescription,
                modifier = modifier
                    .padding(12.dp)
                    .weight(0.2f)
            )
            Column(
                modifier = modifier
                    .padding(8.dp)
                    .padding(top = 12.dp)
                    .weight(0.8f)
            ) {
                Text(
                    text = service,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = model,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = modifier.padding(top = 4.dp)
                )
            }
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