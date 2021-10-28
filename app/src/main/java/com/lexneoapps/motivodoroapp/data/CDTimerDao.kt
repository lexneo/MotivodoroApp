package com.lexneoapps.motivodoroapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface CDTimerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCDTimer(cdTimer: CDTimer)

    @Update
    suspend fun updateCDTimer(cdTimer: CDTimer)
}