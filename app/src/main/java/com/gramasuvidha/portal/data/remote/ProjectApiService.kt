package com.gramasuvidha.portal.data.remote

import com.gramasuvidha.portal.data.mock.ProjectDto
import retrofit2.http.GET

interface ProjectApiService {
    @GET("projects")
    suspend fun getProjects(): List<ProjectDto>
}
