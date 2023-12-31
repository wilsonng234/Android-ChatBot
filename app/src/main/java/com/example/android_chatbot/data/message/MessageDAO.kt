package com.example.android_chatbot.data.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg messages: Message): List<Long>

    @Query("SELECT * FROM message order by created_time asc")
    fun getAll(): Flow<List<Message>>

    @Query("SELECT * FROM message where channel_id = :channelId order by created_time asc")
    fun getMessagesByChannelId(channelId: Long): Flow<List<Message>>

    @Query("SELECT * FROM message where channel_id = :channelId order by created_time desc limit 1")
    fun getLastMessageByChannelId(channelId: Long): Flow<Message?>
}
