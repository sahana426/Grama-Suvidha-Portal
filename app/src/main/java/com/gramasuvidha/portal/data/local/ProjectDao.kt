package com.gramasuvidha.portal.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY id ASC")
    fun observeProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun observeProject(id: Int): Flow<ProjectEntity?>

    @Query("SELECT COUNT(*) FROM projects")
    suspend fun count(): Int

    @Upsert
    suspend fun upsertAll(projects: List<ProjectEntity>)
}
