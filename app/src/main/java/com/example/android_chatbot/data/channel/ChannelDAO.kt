package com.example.android_chatbot.data.channel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg channels: Channel): List<Long>

    @Query(
        value = """WITH channel_message AS (
               SELECT CHANNEL.id, MIN(message.created_time) AS min_created_time
               FROM channel NATURAL JOIN message
               GROUP BY CHANNEL.id
          )
              SELECT channel.id, channel.service, channel.model, channel.topic
              FROM channel NATURAL JOIN message JOIN channel_message cm ON CHANNEL.id = cm.id and message.created_time = cm.min_created_time"""
    )
    fun getFiveRecentChannels(): Flow<List<Channel>>

    @Query("SELECT * FROM channel")
    fun getAll(): Flow<List<Channel>>

    @Query("SELECT * FROM channel where id = :id")
    fun getChannelById(id: Long): Channel

    @Query("UPDATE channel SET topic = :topic WHERE id = :id")
    fun updateChannelTopic(topic: String, id: Long)
}
