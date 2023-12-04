package com.example.android_chatbot.data.channel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg channels: Channel): List<Long>

    @Query(
        value = """WITH cm AS (
               SELECT CHANNEL.id AS id, MAX(message.created_time) AS max_created_time
               FROM channel NATURAL JOIN message
               GROUP BY CHANNEL.id
          )
              SELECT channel.id, channel.service, channel.model, channel.topic
              FROM channel NATURAL JOIN message JOIN cm ON CHANNEL.id = cm.id and message.created_time = cm.max_created_time
              ORDER BY message.created_time DESC 
              LIMIT 5
          """
    )
    fun getFiveRecentChannels(): Flow<List<Channel>>

    @Query("SELECT * FROM channel")
    fun getAll(): Flow<List<Channel>>

    @Query("SELECT * FROM channel where id = :id")
    fun getChannelById(id: Long): Channel

    @Query("UPDATE channel SET topic = :topic WHERE id = :id")
    fun updateChannelTopic(topic: String, id: Long)
}
