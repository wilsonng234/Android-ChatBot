package com.example.android_chatbot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MenuItemCard(icon: ImageVector, content: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(imageVector = icon, contentDescription = "Menu Icon")
        Spacer(modifier = modifier)
        Text(text = content)
    }
}

@Preview
@Composable
fun MenuItemCardPreview() {
    MenuItemCard(
        icon = Icons.Filled.Menu,
        content = "Menu Item",
        modifier = Modifier
            .height(60.dp)
            .padding(10.dp)
    )
}
