package com.example.android_chatbot.data.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(vararg messages: Message)

    @Query("SELECT * FROM message")
    fun getAll(): List<Message>
}
