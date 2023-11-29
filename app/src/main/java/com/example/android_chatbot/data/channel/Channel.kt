package com.example.android_chatbot.data.channel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "channel")
data class Channel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "service") val service: String
)
