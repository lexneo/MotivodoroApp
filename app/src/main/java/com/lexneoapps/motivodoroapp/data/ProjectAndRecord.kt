package com.lexneoapps.motivodoroapp.data

import androidx.room.Embedded
import androidx.room.Relation

data class ProjectAndRecord (
    @Embedded val project: Project,
    @Relation(
        parentColumn = "name",
        entityColumn = "projectName"
    )
    val record: Record
)
