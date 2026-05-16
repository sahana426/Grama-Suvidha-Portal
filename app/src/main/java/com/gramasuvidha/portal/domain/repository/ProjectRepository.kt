package com.gramasuvidha.portal.domain.repository

import com.gramasuvidha.portal.data.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    val isOffline: Flow<Boolean>
    fun observeProjects(): Flow<List<Project>>
    fun observeProject(id: Int): Flow<Project?>
    suspend fun refreshProjects()
    suspend fun seedIfEmpty()
}
