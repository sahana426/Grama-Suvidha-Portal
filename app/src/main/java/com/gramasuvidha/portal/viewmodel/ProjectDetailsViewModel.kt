package com.gramasuvidha.portal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gramasuvidha.portal.data.model.Project
import com.gramasuvidha.portal.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProjectDetailsViewModel(repository: ProjectRepository, projectId: Int) : ViewModel() {
    val project: StateFlow<Project?> = repository.observeProject(projectId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    class Factory(
        private val repository: ProjectRepository,
        private val projectId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ProjectDetailsViewModel(repository, projectId) as T
    }
}
