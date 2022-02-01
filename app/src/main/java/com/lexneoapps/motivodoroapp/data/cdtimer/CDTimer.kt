package com.lexneoapps.motivodoroapp.data.cdtimer

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cdtimer_table")
data class CDTimer(
        val countdown: Long = 0L,
        val pause: Long = 0L,
        val repeat: Int = 1,
        val name: String = "",
        @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
}