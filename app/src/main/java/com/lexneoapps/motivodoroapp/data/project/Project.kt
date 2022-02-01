package com.lexneoapps.motivodoroapp.data.project

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lexneoapps.motivodoroapp.R

@Entity(tableName = "project_table")
data class Project(
    var name: String = "",
    var color: Int = -30080,
    var totalTime: Int = 0,
    var lastRecord: Long? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
}