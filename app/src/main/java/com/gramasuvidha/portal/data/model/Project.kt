package com.gramasuvidha.portal.data.model

data class Project(
    val id: Int,
    val title: String,
    val description: String,
    val budget: Long,
    val startDate: String,
    val expectedCompletion: String,
    val progress: Int,
    val status: ProjectStatus,
    val department: String,
    val contractor: String,
    val budgetUtilization: Long,
    val imageUrl: String,
    val beforeImages: List<String>,
    val afterImages: List<String>,
    val updates: List<String>
)
