package com.example.android_chatbot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MenuItemCard(
    icon: ImageVector,
    content: String,
    handleMenuItemClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxSize()
            .border(10.dp, color = MaterialTheme.colorScheme.background)
            .background(color = MaterialTheme.colorScheme.background)
            .clickable { handleMenuItemClicked() }) {
        Icon(
            imageVector = icon,
            contentDescription = "Menu Icon",
            modifier.padding(horizontal = 10.dp)
        )
        Text(text = content)
    }
}

@Preview
@Composable
fun MenuItemCardPreview() {
    MenuItemCard(
        icon = Icons.Filled.Menu,
        content = "Menu Item",
        handleMenuItemClicked = {},
        modifier = Modifier.height(60.dp)
    )
}
