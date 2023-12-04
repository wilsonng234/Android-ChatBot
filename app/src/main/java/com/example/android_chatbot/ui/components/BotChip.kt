package com.example.android_chatbot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotChip(
    label: String,
    selected: Boolean = false,
    handleBotChipClicked: () -> Unit = { },
    leadingIcon: @Composable (() -> Unit) = { },
    modifier: Modifier = Modifier
) {
    FilterChip(
        onClick = { handleBotChipClicked() },
        label = { Text(label) },
        selected = selected,
        leadingIcon = leadingIcon,
        modifier = modifier
    )
}

@Composable
@Preview
fun BotChipPreview() {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        BotChip(label = "GPT-3.5-Turbo")
        BotChip(label = "GPT-4")
    }
}
