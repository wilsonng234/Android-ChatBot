package com.example.android_chatbot.data.setting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "setting", indices = [Index(value = ["service"], unique = true)])
data class Setting(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    @ColumnInfo(name = "service") val service: String,

    @ColumnInfo(name = "api_key") val apiKey: String
)
