package com.gramasuvidha.portal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.data.model.Project
import com.gramasuvidha.portal.data.model.ProjectStatus
import com.gramasuvidha.portal.ui.components.AnimatedProjectProgress
import com.gramasuvidha.portal.ui.components.ProgressAnalyticsChart
import com.gramasuvidha.portal.ui.components.StatusChip
import com.gramasuvidha.portal.ui.components.SummaryTile
import com.gramasuvidha.portal.ui.components.formatRupees
import com.gramasuvidha.portal.utils.localized
import com.gramasuvidha.portal.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, onProjectClick: (Int) -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val language = state.languageTag
    val offlineMessage = localized(R.string.offline_mode, language)

    LaunchedEffect(state.isOffline) {
        if (state.isOffline) snackbarHostState.showSnackbar(offlineMessage)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(localized(R.string.home_title, language), fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = viewModel::toggleLanguage) {
                        Text(if (language == "en") localized(R.string.language_kannada, language) else localized(R.string.language_english, language))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (state.isOffline) {
                    item { OfflineBanner(offlineMessage) }
                }
                item { PanchayatSummary(state.projects.size, state.completedCount, state.activeBudget, language) }
                item { SearchAndFilters(state.query, state.selectedStatus, language, viewModel::updateQuery, viewModel::updateStatus) }
                items(state.filteredProjects, key = { it.id }) { project ->
                    ProjectCard(project = project, languageTag = language, onClick = { onProjectClick(project.id) })
                }
                item { ProgressAnalyticsChart(projects = state.projects, languageTag = language, modifier = Modifier.padding(vertical = 18.dp)) }
            }
        }
    }
}

@Composable
private fun OfflineBanner(message: String) {
    AssistChip(onClick = {}, label = { Text(message, fontWeight = FontWeight.Bold) }, modifier = Modifier.fillMaxWidth())
}

@Composable
private fun PanchayatSummary(total: Int, completed: Int, activeBudget: Long, languageTag: String) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(localized(R.string.summary_dashboard, languageTag), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            SummaryTile(localized(R.string.total_projects, languageTag), total.toString(), Modifier.weight(1f))
            SummaryTile(localized(R.string.completed_projects, languageTag), completed.toString(), Modifier.weight(1f))
        }
        SummaryTile(localized(R.string.active_budget, languageTag), formatRupees(activeBudget), Modifier.fillMaxWidth())
        if (completed > 0) AssistChip(onClick = {}, label = { Text("🏅 ${localized(R.string.completed_badge, languageTag)} x$completed") })
    }
}

@Composable
private fun SearchAndFilters(
    query: String,
    selectedStatus: ProjectStatus?,
    languageTag: String,
    onQueryChange: (String) -> Unit,
    onStatusChange: (ProjectStatus?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(value = query, onValueChange = onQueryChange, modifier = Modifier.fillMaxWidth(), singleLine = true, label = { Text(localized(R.string.search_hint, languageTag)) })
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FilterChip(selected = selectedStatus == null, onClick = { onStatusChange(null) }, label = { Text(localized(R.string.filter_all, languageTag)) })
            ProjectStatus.entries.forEach { status ->
                FilterChip(selected = selectedStatus == status, onClick = { onStatusChange(status) }, label = { Text(localized(status.labelRes, languageTag)) })
            }
        }
    }
}

@Composable
private fun ProjectCard(project: Project, languageTag: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AsyncImage(model = project.imageUrl, contentDescription = project.title, modifier = Modifier.fillMaxWidth().height(170.dp), contentScale = ContentScale.Crop)
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(project.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    StatusChip(project.status, languageTag)
                }
                Text("${localized(R.string.budget, languageTag)}: ${formatRupees(project.budget)}")
                Text("${localized(R.string.start_date, languageTag)}: ${project.startDate}")
                Text("${localized(R.string.expected_completion, languageTag)}: ${project.expectedCompletion}")
                AnimatedProjectProgress(project.progress)
            }
        }
    }
}
