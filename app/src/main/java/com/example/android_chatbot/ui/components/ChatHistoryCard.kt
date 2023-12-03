package com.example.android_chatbot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_chatbot.R

@Composable
fun ChatHistoryCard(iconId:Int, contentDesc:String? = null, service:String, model:String, title:String, recentChat:String){
    Row(){
        Icon(painter = painterResource(id = iconId), contentDescription = contentDesc)
        Column() {
            Text(text = "$service $model")
            Text(text = title)
            Text(text = recentChat)
        }
    }
}

@Preview
@Composable
fun ChatHistoryCardPreview(){
    ChatHistoryCard(R.drawable.chatgpt,null, "OpenAI", "ChatGPT-4", "HKUST", "HKUST is a school that...")
}