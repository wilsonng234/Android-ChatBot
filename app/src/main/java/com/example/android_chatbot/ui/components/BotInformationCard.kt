package com.example.android_chatbot.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BotInformationCard(
    icon: ImageVector,
    service: String,
    description: String,
    handleOnClicked: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier
            .padding(10.dp)
            .wrapContentSize()
            .clickable { handleOnClicked() }
    ) {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Menu Icon",
                modifier
                    .size(75.dp)
            )
            Column(modifier.padding(12.dp)) {
                Text(text = service, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = description, fontSize = 18.sp)
            }
        }
    }
}


@Preview
@Composable
fun BotInformationCardPreview() {
    BotInformationCard(
        icon = Icons.Filled.Menu,
        service = "Azure",
        description = "Hello! How can I assist you today?",
        handleOnClicked = {},
        modifier = Modifier.height(150.dp)
    )
}