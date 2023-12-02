package com.example.android_chatbot.data.channel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg channels: Channel)

    @Query("SELECT * FROM channel")
    fun getAll(): Flow<List<Channel>>

    @Query("SELECT * FROM channel where id = :id")
    fun getChannelById(id: Int): Channel
}
