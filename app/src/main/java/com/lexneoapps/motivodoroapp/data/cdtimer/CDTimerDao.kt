package com.lexneoapps.motivodoroapp.data.cdtimer

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimer
import com.lexneoapps.motivodoroapp.data.record.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface CDTimerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCDTimer(cdTimer: CDTimer)

    @Update
    suspend fun updateCDTimer(cdTimer: CDTimer)

    @Query("SELECT * FROM cdtimer_table")
    fun getTimers(): Flow<List<CDTimer>>

    @Query("SELECT * FROM cdtimer_table WHERE id = :cdTimerID")
    suspend fun getTimerById(cdTimerID: Int): CDTimer


}