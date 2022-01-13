package com.lexneoapps.motivodoroapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM project_table")
    fun getProjects() : Flow<List<Project>>

    @Query("SELECT * FROM project_table")
    suspend fun listProjects() : List<Project>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

}