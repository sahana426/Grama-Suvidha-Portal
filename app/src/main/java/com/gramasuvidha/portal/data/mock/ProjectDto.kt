package com.gramasuvidha.portal.data.mock

import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    val id: Int,
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
    val updates: List<String>
)
