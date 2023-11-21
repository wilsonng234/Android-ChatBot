package com.example.android_chatbot.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "role")
    val role: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "created_time")
    val createdTime: Long
) {
    override fun toString(): String {
        return """
            {
                "role": "$role",
                "content": "$content"
            }
        """.trimIndent()
    }
}
