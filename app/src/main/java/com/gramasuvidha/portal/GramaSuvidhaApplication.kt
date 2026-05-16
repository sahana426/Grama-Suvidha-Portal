package com.gramasuvidha.portal

import android.app.Application
import com.gramasuvidha.portal.data.local.AppDatabase
import com.gramasuvidha.portal.data.mock.LocalAssetProjectLoader
import com.gramasuvidha.portal.domain.repository.ProjectRepository
import com.gramasuvidha.portal.repository.OfflineFirstProjectRepository
import com.gramasuvidha.portal.utils.ConnectivityObserver

class GramaSuvidhaApplication : Application() {
    val projectRepository: ProjectRepository by lazy {
        val database = AppDatabase.getInstance(this)
        OfflineFirstProjectRepository(
            dao = database.projectDao(),
            assetLoader = LocalAssetProjectLoader(this),
            connectivityObserver = ConnectivityObserver(this)
        )
    }
}
