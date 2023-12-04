package com.example.android_chatbot.data.setting

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg setting: Setting): List<Long>

    @Query("SELECT * FROM setting")
    fun getAll(): Flow<List<Setting>>

    @Query("SELECT * FROM setting WHERE service = :service")
    fun getSettingByService(service: String): Setting

    @Query("DELETE FROM setting")
    fun deleteAll()

    @Query("DELETE FROM setting WHERE id = :id")
    fun deleteById(id: Long)
}
