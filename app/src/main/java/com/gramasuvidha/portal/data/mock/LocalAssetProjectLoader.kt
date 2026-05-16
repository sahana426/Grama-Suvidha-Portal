package com.gramasuvidha.portal.data.mock

import android.content.Context
import com.gramasuvidha.portal.data.local.ProjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class LocalAssetProjectLoader(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun loadProjects(): List<ProjectEntity> = withContext(Dispatchers.IO) {
        val raw = context.assets.open("projects.json").bufferedReader().use { it.readText() }
        json.decodeFromString<List<ProjectDto>>(raw).map { dto ->
            ProjectEntity(
                id = dto.id,
                title = dto.title,
                description = dto.description,
                budget = dto.budget,
                startDate = dto.startDate,
                expectedCompletion = dto.expectedCompletion,
                progress = dto.progress,
                status = dto.status,
                department = dto.department,
                contractor = dto.contractor,
                budgetUtilization = dto.budgetUtilization,
                imageUrl = dto.imageUrl,
                beforeImages = dto.beforeImages,
                afterImages = dto.afterImages,
                updates = dto.updates
            )
        }
    }
}
