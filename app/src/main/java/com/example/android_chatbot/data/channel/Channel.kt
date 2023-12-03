package com.example.android_chatbot.data.channel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channel")
data class Channel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    @ColumnInfo(name = "service") val service: String,

    @ColumnInfo(name = "model") val model: String,

    @ColumnInfo(name = "topic") val topic: String = "New Chat",
)
