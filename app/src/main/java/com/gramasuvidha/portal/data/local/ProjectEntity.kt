package com.gramasuvidha.portal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gramasuvidha.portal.data.model.Project
import com.gramasuvidha.portal.data.model.ProjectStatus

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val budget: Long,
    val startDate: String,
    val expectedCompletion: String,
    val progress: Int,
    val status: String,
    val department: String,
    val contractor: String,
    val budgetUtilization: Long,
    val imageUrl: String,
    val beforeImages: List<String>,
    val afterImages: List<String>,
    val updates: List<String>,
    val cachedAtMillis: Long = System.currentTimeMillis()
)

fun ProjectEntity.toDomain() = Project(
    id = id,
    title = title,
    description = description,
    budget = budget,
    startDate = startDate,
    expectedCompletion = expectedCompletion,
    progress = progress,
    status = ProjectStatus.fromRaw(status),
    department = department,
    contractor = contractor,
    budgetUtilization = budgetUtilization,
    imageUrl = imageUrl,
    beforeImages = beforeImages,
    afterImages = afterImages,
    updates = updates
)
