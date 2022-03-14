package com.lexneoapps.motivodoroapp.data.project

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lexneoapps.motivodoroapp.util.formatMillisToTimer

@Entity(tableName = "project_table")
data class Project(
    var name: String = "",
    var color: Int = -30080,
    var totalTime: Long = 0L,
    var lastRecord: Long? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {

    val totalTimeFormatted: String
        get() = formatMillisToTimer(totalTime)
}