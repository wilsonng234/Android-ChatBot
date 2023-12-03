package com.example.android_chatbot.data.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.android_chatbot.data.channel.Channel

@Entity(
    tableName = "message",
    indices = [Index(value = ["channel_id"], unique = false)],
    foreignKeys = [ForeignKey(
        entity = Channel::class,
        parentColumns = ["id"],
        childColumns = ["channel_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    @ColumnInfo(name = "channel_id") val channelId: Long,

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
