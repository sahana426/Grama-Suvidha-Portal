package com.gramasuvidha.portal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gramasuvidha.portal.data.model.Project
import com.gramasuvidha.portal.data.model.ProjectStatus
import com.gramasuvidha.portal.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val projects: List<Project> = emptyList(),
    val query: String = "",
    val selectedStatus: ProjectStatus? = null,
    val isRefreshing: Boolean = false,
    val isOffline: Boolean = false,
    val languageTag: String = "en"
) {
    val filteredProjects: List<Project> = projects.filter { project ->
        (query.isBlank() || project.title.contains(query, ignoreCase = true)) &&
            (selectedStatus == null || project.status == selectedStatus)
    }
    val completedCount: Int = projects.count { it.status == ProjectStatus.Completed }
    val activeBudget: Long = projects.filterNot { it.status == ProjectStatus.Completed }.sumOf { it.budget }
}

class HomeViewModel(private val repository: ProjectRepository) : ViewModel() {
    private val query = MutableStateFlow("")
    private val selectedStatus = MutableStateFlow<ProjectStatus?>(null)
    private val refreshing = MutableStateFlow(false)
    private val languageTag = MutableStateFlow("en")

    val uiState: StateFlow<HomeUiState> = combine(
        repository.observeProjects(),
        query,
        selectedStatus,
        refreshing,
        repository.isOffline,
        languageTag
    ) { projects, q, status, isRefreshing, isOffline, language ->
        HomeUiState(projects, q, status, isRefreshing, isOffline, language)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    init {
        viewModelScope.launch { repository.seedIfEmpty() }
    }

    fun updateQuery(value: String) { query.value = value }
    fun updateStatus(status: ProjectStatus?) { selectedStatus.value = status }
    fun toggleLanguage() { languageTag.value = if (languageTag.value == "en") "kn" else "en" }

    fun refresh() = viewModelScope.launch {
        refreshing.value = true
        repository.refreshProjects()
        refreshing.value = false
    }

    class Factory(private val repository: ProjectRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
    }
}
