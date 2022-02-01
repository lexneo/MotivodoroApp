package com.lexneoapps.motivodoroapp.data

import androidx.room.Embedded
import androidx.room.Relation
import com.lexneoapps.motivodoroapp.data.project.Project
import com.lexneoapps.motivodoroapp.data.record.Record

data class ProjectAndRecord (
    @Embedded val project: Project,
    @Relation(
        parentColumn = "name",
        entityColumn = "projectName"
    )
    val record: Record
)
