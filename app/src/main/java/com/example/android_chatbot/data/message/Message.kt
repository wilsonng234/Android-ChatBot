package com.example.android_chatbot.data.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "message", indices = [Index(value = ["channel"], unique = false)])
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "channel") val channel: String,

    @ColumnInfo(name = "role") val role: String,

    @ColumnInfo(name = "content") val content: String,

    @ColumnInfo(name = "created_time") val createdTime: Long
) {
    override fun toString(): String {
        return """
            {
                "role": "$role",
                "content": "${content.replace("\n", "\\n")}"
            }
        """.trimIndent()
    }
}
