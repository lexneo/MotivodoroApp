package com.lexneoapps.motivodoroapp.data.project

import androidx.room.*
import com.lexneoapps.motivodoroapp.data.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    fun getProjects(query: String, sortOrder: SortOrder) : Flow<List<Project>> =
        when(sortOrder){
            SortOrder.BY_RECENT -> getProjectsSortedByRecentlyTracked(query)
            SortOrder.BY_TOTAL -> getProjectsSortedByTotalTime(query)
            SortOrder.BY_NAME -> getProjectsSortedByName(query)
        }

    @Query("SELECT * FROM project_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY totalTime DESC")
    fun getProjectsSortedByTotalTime(searchQuery : String) : Flow<List<Project>>

    @Query("SELECT * FROM project_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY lastRecord DESC")
    fun getProjectsSortedByRecentlyTracked(searchQuery : String) : Flow<List<Project>>

    @Query("SELECT * FROM project_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ")
    fun getProjectsSortedByName(searchQuery : String) : Flow<List<Project>>


/*    @Query("SELECT * FROM project_table")
    suspend fun listProjects() : List<Project>*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

}