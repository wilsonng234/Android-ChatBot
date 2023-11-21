package com.example.android_chatbot.data.setting

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SettingDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(vararg setting: Setting)

    @Query("SELECT * FROM setting")
    fun getAll(): List<Setting>
}
