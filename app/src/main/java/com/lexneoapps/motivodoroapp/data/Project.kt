package com.lexneoapps.motivodoroapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lexneoapps.motivodoroapp.R

@Entity(tableName = "project_table")
data class Project(
    var name: String = "",
    var color: Int = R.color.primaryColor,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
}