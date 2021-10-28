package com.lexneoapps.motivodoroapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cdtimer_table")
data class CDTimer(
        var countdown: Long = 0L,
        var pause: Long = 0L,
        var repeat: Int = 1,
        var name: String = "",
        @PrimaryKey(autoGenerate = true) var id: Int = 0
) {
}