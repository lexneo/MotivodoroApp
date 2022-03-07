package com.lexneoapps.motivodoroapp.data.cdtimer

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cdtimer_table")
data class CDTimer(
        val countdown: Int = 0,
        val pause: Int = 0,
        val repeat: Int = 1,
        val name: String = "",
//        val color: Int = -30800,
        @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
}