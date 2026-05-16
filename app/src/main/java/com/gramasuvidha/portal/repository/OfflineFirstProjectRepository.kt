package com.gramasuvidha.portal.repository

import com.gramasuvidha.portal.data.local.ProjectDao
import com.gramasuvidha.portal.data.local.toDomain
import com.gramasuvidha.portal.data.mock.LocalAssetProjectLoader
import com.gramasuvidha.portal.domain.repository.ProjectRepository
import com.gramasuvidha.portal.utils.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstProjectRepository(
    private val dao: ProjectDao,
    private val assetLoader: LocalAssetProjectLoader,
    connectivityObserver: ConnectivityObserver
) : ProjectRepository {
    override val isOffline: Flow<Boolean> = connectivityObserver.isOnline.map { online -> !online }

    override fun observeProjects() = dao.observeProjects().map { entities -> entities.map { it.toDomain() } }

    override fun observeProject(id: Int) = dao.observeProject(id).map { it?.toDomain() }

    override suspend fun refreshProjects() {
        dao.upsertAll(assetLoader.loadProjects())
    }

    override suspend fun seedIfEmpty() {
        if (dao.count() == 0) refreshProjects()
    }
}
