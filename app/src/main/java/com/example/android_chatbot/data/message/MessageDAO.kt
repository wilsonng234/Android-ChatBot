package com.example.android_chatbot.data.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg messages: Message)

    @Query("SELECT * FROM message order by created_time asc")
    fun getAll(): List<Message>

    @Query("SELECT * FROM message where channel = :channel order by created_time asc")
    fun getMessagesByChannel(channel: String): List<Message>
}
